package com.manulife.pension.ps.service.report.transaction.reporthandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.dao.TransactionHistoryDAO;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionHandlerFactory;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionHandlerKey;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionType;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData;
import com.manulife.pension.service.environment.EnvironmentServiceHelper;
import com.manulife.pension.service.environment.valueobject.ContractDatesVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author Charles Chan
 */
public class TransactionHistoryReportHandler implements ReportHandler {

	public ReportData getReportData(ReportCriteria criteria)
			throws SystemException, ReportServiceException {

		TransactionHistoryReportData reportData = new TransactionHistoryReportData(
				criteria, 0);
		List<TransactionDataItem> historyItems = new ArrayList<TransactionDataItem>();
		TransactionHistoryDAO.getReportData(criteria, reportData, historyItems);

		Boolean isDBContract = (Boolean)criteria.getFilterValue(TransactionHistoryReportData.CONTRACT_TYPE_DB);
		if (isDBContract == null) isDBContract = Boolean.FALSE;
		TransactionHandlerFactory factory = TransactionHandlerFactory.getInstance(isDBContract.booleanValue());

		/*
		 * For each transaction data item, transform it into a
		 * TransactionHistoryItem.
		 */
		List<TransactionHistoryItem> items = new ArrayList<TransactionHistoryItem>();		
		for (Iterator<TransactionDataItem> it = historyItems.iterator(); it.hasNext();) {

			TransactionDataItem tx = it.next();
			if (isDBContract) {
				tx.setDBContract(true);
			}

			TransactionHistoryItem item = factory.getTransactionHandler(
					new TransactionHandlerKey(tx.getTransactionType(), tx
							.getTransactionReasonCode(), reportData
							.getReportCriteria().getReportId())).transform(tx);

			items.add(item);
		}
		reportData.setDetails(items);

		doHasLoans(criteria, reportData);
		return reportData;
	}

	/**
	 * Determines if in the last 24 months there are any loan-realted transactions
	 * 
	 * 24 months is reduced if the contract effective is more recent
	 * 
	 * If the contract has no month end, the contract effective date is used
	 * along with the as of date
	 */
	public void doHasLoans(ReportCriteria criteria, TransactionHistoryReportData reportData)
			throws SystemException, ReportServiceException
	{
			Integer contractId = (Integer)criteria.getFilterValue(TransactionHistoryReportData.FILTER_CONTRACT_NUMBER);
			ContractDatesVO datesVO = EnvironmentServiceHelper.getInstance().getContractDates(contractId.intValue());
			Date asOfDate = datesVO.getAsOfDate();
			Date effectiveDate = EnvironmentServiceHelper.getInstance().getEffectiveDate(contractId.intValue());
			Date fromDate = null;
			if (datesVO.getMonthEndDates().size() == 0) {
				fromDate = effectiveDate;
			} else {
				Date lastMonthEnd = (Date)datesVO.getMonthEndDates().toArray()[datesVO.getMonthEndDates().size() -1];
				Calendar toCal = Calendar.getInstance();
				Calendar fromCal = Calendar.getInstance();

				toCal.setTime(lastMonthEnd);
				fromCal.set(Calendar.MONTH, toCal.get(Calendar.MONTH));
				fromCal.set(Calendar.YEAR, toCal.get(Calendar.YEAR));
				fromCal.set(Calendar.DATE, 1);
				fromDate = fromCal.getTime();
				if (effectiveDate.after(fromDate) ) {
					fromDate = effectiveDate;
				}
			}
	
			ReportCriteria newCriteria = new ReportCriteria(TransactionHistoryReportData.REPORT_ID);
			newCriteria.addFilter(TransactionHistoryReportData.FILTER_FROM_DATE, fromDate);
			newCriteria.addFilter(TransactionHistoryReportData.FILTER_TO_DATE, asOfDate);
			newCriteria.addFilter(TransactionHistoryReportData.FILTER_CONTRACT_NUMBER, contractId);

			reportData.setHasLoans(TransactionHistoryDAO.hasLoans(newCriteria));
	}	
















}