package com.manulife.pension.ps.service.report.transaction.reporthandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.dao.CashAccountDAO;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionHandlerFactory;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionHandlerKey;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.CreditTransactionReportItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData;
import com.manulife.pension.service.environment.EnvironmentServiceHelper;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * This is the report handler class for the Cash Account report. It creates
 * CashAccountReportData value object using the CashAccountDAO.
 * 
 * @author Maria Lee
 */
public class CashAccountReportHandler implements ReportHandler {

	private static final String DEBIT_NOT_TIED_TO_CREDIT = "N";

	private static final TransactionHandlerFactory factory = 
		  TransactionHandlerFactory.getInstance(false);

	/**
	 * Called by the ReportService to return a ReportData object.
	 * 
	 * Calls the CashAccountDAO to return the cash account transactions from
	 * Apollo. For each transaction returned from DAO, get the appropriate
	 * transaction handler to construct a CashAccountItem object augmented with
	 * additional info suitable for display on the CashAccount page.
	 *  
	 */
	public ReportData getReportData(ReportCriteria criteria)
			throws SystemException, ReportServiceException {

		java.util.Date asOfDate = EnvironmentServiceHelper.getInstance().getAsOfDate();
		criteria.addFilter(CashAccountReportData.FILTER_AS_OF_DATE, asOfDate);
		
		CashAccountDAO dao = new CashAccountDAO();
		CashAccountReportData reportData = dao.getSummaryData(criteria);
		List<TransactionDataItem> transactions = dao.getTransactionItems(criteria, reportData);

		Boolean isDBContract = (Boolean)criteria.getFilterValue(TransactionHistoryReportData.CONTRACT_TYPE_DB);
		if (isDBContract == null) isDBContract = Boolean.FALSE;
		
		// transactions will be null if it has too many items
		if (transactions != null) {
			// construct a list of CashAccountItem objects
			reportData.setDetails(transformTransactions(reportData, transactions, isDBContract.booleanValue(), criteria));
		} else {
			reportData.setDetails(new ArrayList());
		}
		return reportData;
	}


	/**
	 * Process the list of transactions returned from the CashAccountDAO
	 * 
	 * @param reportData
	 * @param transactions
	 * @param isDBContract
	 * @param criteria
	 * @return
	 * @throws SystemException
	 */
	private List<CashAccountItem> transformTransactions(CashAccountReportData reportData,
			List<TransactionDataItem> transactions, boolean isDBContract, ReportCriteria criteria) throws SystemException {

		List<CashAccountItem> items = new ArrayList<CashAccountItem>();

		/*
		 * list of transactions are in descending order so we must initialize
		 * runningBalance with closing balance
		 */
		BigDecimal runningBalance = reportData.getClosingBalanceForPeriod();

		CashAccountItem item = null;
		/* 
		 * we need to keep track of the previous item because that's how the running balance is calculated
		 *
		 * Running balance for each transaction.
		 *   For the first transaction (i.e. the latest dated one),  
		 *   running balance = closing balance   (retrieve opening balance for To Date + 1)
		 *   Other running balances = previous running balance - previous transaction amount (if credit) + previous transaction amount (if debit)
		 *   Opening balance = last running balance - last transaction amount (if credit) + last transaction amount (if debit)
		 *
		 */
		CashAccountItem previousItem = new CreditTransactionReportItem();
		previousItem.setRunningBalance(runningBalance);
		
		boolean previousDebitNotTiedToCredit = true; 
		
        boolean limitPageSize = true;
        int recordsSkippedSoFar = 1;
        int recordCountForThisPage = 0;
        int pageSize = criteria.getPageSize();
        int startIndex = criteria.getStartIndex();
        if (pageSize == ReportCriteria.NOLIMIT_PAGE_SIZE) {
            limitPageSize = false;
        }
		
		for (Iterator<TransactionDataItem> iterator = transactions.iterator(); iterator.hasNext(); previousItem = item) {

			TransactionDataItem tx = iterator.next();
			
			if (isDBContract) {
				tx.setDBContract(true);
			}
			/**
			 * Gets the appropriate transaction handler to create a
			 * CashAccountItem object. The CashAccountItem object has the
			 * description lines required for the CashAccount page.
			 *  
			 */
			item = (CashAccountItem) factory.getTransactionHandler(
    			        new TransactionHandlerKey(
    			                tx.getTransactionType(), 
    			                tx.getTransactionReasonCode(), 
    			                reportData.getReportCriteria().getReportId(), 
    			                tx.getTransactionReasonCodeExcessWD()
    			            )
    			        ).transform(tx);

			// as we loop through transaction items on the pages, we keep calculating the running balance  
			item.setRunningBalance( getRunningBalance(previousDebitNotTiedToCredit, previousItem) );
			previousDebitNotTiedToCredit = isDebitNotTiedToCredit(tx.getDebitTiedToCreditFlag());
			previousItem = item;

			// If we are not on 1st page, then we need to skip over the first (n-1) pages before landing on the n-th page  
			if(recordsSkippedSoFar++ < startIndex){
			    continue;
			}
			
			// either we have no page limit and show all records on one page 
			// or, we are now on the n-th, and need to show only specified records (pageSize) on this page  
			if( !limitPageSize || recordCountForThisPage < pageSize){
			    items.add(item);
			    recordCountForThisPage++;
			} else {
			    // if we are done displaying the n-th page,  
			    // then we no longer need to loop through the remaining records any more 
			    break;
			}
		}
		return items;
	}

	/**
	 *  
	 */
	private static BigDecimal getRunningBalance(boolean isDebitNotTiedToCredit, CashAccountItem item) {
		BigDecimal runningBalance = item.getRunningBalance();
		
		// don't include in running total if it's not tied to credit
		if (isDebitNotTiedToCredit) {
			return runningBalance;
		}
		
		BigDecimal debit = item.getDebitAmount();
		BigDecimal credit = item.getCreditAmount();

		BigDecimal result = runningBalance;

		if (debit != null) {
			result = result.add(debit);
		}
		if (credit != null) {
			result = result.subtract(credit);
		}
		return result;
		//			return runningBalance.add(item.getDebitAmount()).subtract(
		//					item.getCreditAmount());
	}

	/**
	 * Determine if debit is tied to credit
	 */
	public static boolean isDebitNotTiedToCredit(String flag) {
		return DEBIT_NOT_TIED_TO_CREDIT.equalsIgnoreCase(flag) ? true : false;
	}
}