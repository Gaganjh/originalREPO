package com.manulife.pension.ps.service.report.transaction.reporthandler;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.dao.CashAccountOtherDetailsDAO;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionHandlerFactory;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionHandlerKey;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountForfeituresReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.service.account.entity.ContractMoneyType;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Report handler class  for Cash Account Forfeitures page
 *  
 * @author Chavva Akhilesh
 */
public class CashAccountForfeituresReportHandler implements ReportHandler {
	
	private static final TransactionHandlerFactory factory = 
		  TransactionHandlerFactory.getInstance(false);

	/**
	 * Called by the ReportService to return a ReportData object.
	 * 
	 * @param criteria
	 * @return cash account forfeitures report data
	 * @throws SystemException
	 * @throws ReportServiceException 
	 */
	public ReportData getReportData(ReportCriteria criteria)
			throws SystemException, ReportServiceException {
		
		CashAccountOtherDetailsDAO dao = new CashAccountOtherDetailsDAO();
		
		// get the summary data i.e. total forfeitures in plan, in cash account
		// and in participant account
		CashAccountForfeituresReportData cashAccountForfeituresReportData = 
			dao.getForfeituresSummaryData(criteria);

		// get the UM money types for money types drop-down 
		List<ContractMoneyType> cashMoneyTypeList = 
			dao.getContractUMMoneyType(criteria);
		cashAccountForfeituresReportData.setListOfContractMoneyTypes(
				cashMoneyTypeList);
		
		// get the transaction items
		List<TransactionDataItem> transactions = 
			dao.getForfeituresTransactionItemsList(
					criteria, cashAccountForfeituresReportData);
		
		// transactions will be null if it has too many items
		if (transactions != null) {

			// construct a list of CashAccountItem objects			
			cashAccountForfeituresReportData.setDetails(transformTransactions(
					cashAccountForfeituresReportData, transactions));
		} else {
			cashAccountForfeituresReportData.setDetails(new ArrayList<CashAccountItem>());
		}
		
		return cashAccountForfeituresReportData;
	}
	
	/**
	 * Transforms the list of TransasctionDataItem to CashAccountItem
	 * 
	 * @param reportData
	 * @param transactions
	 * @return CashAccountItem List
	 * @throws SystemException
	 */
	private List<CashAccountItem> transformTransactions(
			CashAccountForfeituresReportData reportData,
			List<TransactionDataItem> transactions) throws SystemException {

		List<CashAccountItem> items = new ArrayList<CashAccountItem>();

		CashAccountItem item = null;

		for (Iterator<TransactionDataItem> iterator = transactions.iterator(); iterator.hasNext();) {

			TransactionDataItem tx = iterator.next();
					
			/**
			 * Gets the appropriate transaction handler to create a
			 * CashAccountItem object. The CashAccountItem object has the
			 * description lines required for the Cash Account Forfeitures page.
			 */
			item = (CashAccountItem) factory
					.getTransactionHandler(
							new TransactionHandlerKey(tx.getTransactionType(),
									tx.getTransactionReasonCode(), reportData
											.getReportCriteria().getReportId(), tx.getTransactionReasonCodeExcessWD()))
					.transform(tx);
			
			item.setAvailableAmount(tx.getAvailableAmount());
			item.setOriginalAmount(tx.getOriginalAmount());
			item.setMoneyType(tx.getMoneyType());
				
			items.add(item);
		}
		return items;
	}
}