package com.manulife.pension.ps.service.report.transaction.reporthandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.dao.CashAccountOtherDetailsDAO;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionHandlerFactory;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionHandlerKey;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountBalanceDetailsReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * This is the report handler class for the Cash Account Balance Details report. It creates
 * CashAccountBalanceDetailsReportData value object using the CashAccountOtherDetailsDAO.
 * 
 * @author Rajesh Rajendran
 */
public class CashAccountBalanceDetailsReportHandler implements ReportHandler {
	
	private static final TransactionHandlerFactory factory = 
		  TransactionHandlerFactory.getInstance(false);

	/**
	 * Called by the ReportService to return a ReportData object.
	 * 
	 * Calls the CashAccountOtherDetailsDAO to return the cash account transactions from
	 * Apollo. For each transaction returned from DAO, get the appropriate
	 * transaction handler to construct a CashAccountItem object augmented with
	 * additional info suitable for display on the CashAccountBalanceDetails page.
	 *  
	 * @param criteria
	 * @return reportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	public ReportData getReportData(ReportCriteria criteria)
			throws SystemException, ReportServiceException {

		CashAccountOtherDetailsDAO dao = new CashAccountOtherDetailsDAO();
		
		// get the summary data
		CashAccountBalanceDetailsReportData reportData = 
			dao.getBalanceDetailsSummaryData(criteria);
		
		// get the transaction items
		List<TransactionDataItem> transactions = 
			dao.getBalanceDetailsTransactionItems(criteria, reportData);
		
		Boolean isDBContract = (Boolean)criteria.getFilterValue(
				TransactionHistoryReportData.CONTRACT_TYPE_DB);
		if (isDBContract == null) {
			isDBContract = Boolean.FALSE;
		}
		
		// transactions will be null if it has too many items
		if (transactions != null) {
			// construct a list of CashAccountItem objects			
			reportData.setDetails(transformTransactions(reportData,
					transactions, isDBContract.booleanValue()));
		} else {
			reportData.setDetails(new ArrayList<CashAccountItem>());
		}
		
		return reportData;
	}
	
	/**
	 * Transforms the list of TransasctionDataItem to CashAccountItem
	 *  
	 * @param transactions
	 * @param isDBContract
	 * @param reportData
	 * @return List of CashAccountItem
	 * @throws SystemException
	 */
	private List<CashAccountItem> transformTransactions(
			CashAccountBalanceDetailsReportData reportData,
			List<TransactionDataItem> transactions, boolean isDBContract) throws SystemException {

		List<CashAccountItem> items = new ArrayList<CashAccountItem>();

		CashAccountItem item = null;

		for (Iterator<TransactionDataItem> iterator = transactions.iterator(); iterator.hasNext();) {

			TransactionDataItem tx = iterator.next();
			
			if (isDBContract) {
				tx.setDBContract(true);
			}
			/**
			 * Gets the appropriate transaction handler to create a
			 * CashAccountItem object. The CashAccountItem object has the
			 * description lines required for the CashAccount Balance details page.
			 */
			item = (CashAccountItem) factory.getTransactionHandler(
							new TransactionHandlerKey(
									tx.getTransactionType(),
									tx.getTransactionReasonCode(), 
									reportData.getReportCriteria().getReportId(), 
									tx.getTransactionReasonCodeExcessWD()))
						.transform(tx);

			item.setAvailableAmount(tx.getAvailableAmount());
			item.setOriginalAmount(tx.getOriginalAmount());
			items.add(item);			
		}
		return items;
	}
}