/**
 * 
 */
package com.manulife.pension.ps.service.report.transaction.reporthandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.FastDateFormat;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.dao.PendingWithdrawalSummaryDAO;
import com.manulife.pension.ps.service.report.transaction.handler.AbstractTransactionHandler;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionHandlerFactory;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionHandlerKey;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.participant.transaction.dao.WithdrawalDetailsDAOHelper;
import com.manulife.pension.service.report.participant.transaction.valueobject.PendingWithdrawalSummaryReportData;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalDetailsReportItem;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalGeneralInfoVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalPayeePaymentVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.BaseEnvironment;

/**
 * Handler class for Pending Withdrawal Summary page.
 * 
 * @author Puttaiah Arugunta
 *
 */
public class PendingWithdrawalSummaryReportHandler implements ReportHandler {
	
	public static final String PENDING = "Pending";
	public static final String WITHDRAWAL = "WITHDRAWAL";
	
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe		
	private static final FastDateFormat STANDARD_DATE_FORMAT = FastDateFormat.getInstance( DATE_FORMAT );

	/**
	 * Method to get the report data for Pending Withdrawal Summary page by 
	 * passing the Criteria object
	 * 
	 * @param criteria ReportCriteria
	 * @return ReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 * 
	 */
	public ReportData getReportData(ReportCriteria criteria) 
	throws SystemException, ReportServiceException {
		
		PendingWithdrawalSummaryReportData reportData = 
			new PendingWithdrawalSummaryReportData(criteria, 0) ;
		
		String task = (String)criteria.getFilterValue(
				PendingWithdrawalSummaryReportData.FILTER_TASK);
		
		// task will determine whether the request is for 
		// Pending Summary online page or for CSV download
		if (PendingWithdrawalSummaryReportData.CSV_DOWNLOAD.equals(task)) {
			getCSVReportData(criteria, reportData);
		} else {
			getWebPageReportData(criteria, reportData);
		}

		return reportData;
	}

	/**
	 * Method to get the reportData for Pending Withdrawal Summary online page
	 * 
	 * @param criteria
	 * @param reportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	private void getWebPageReportData(ReportCriteria criteria,
			PendingWithdrawalSummaryReportData reportData)
	throws SystemException, ReportServiceException {
		
		List<TransactionDataItem> dataItems = 
			new ArrayList<TransactionDataItem>();
		
		// get the report data
		PendingWithdrawalSummaryDAO.getReportData(
				criteria, reportData, dataItems);
		
		// get the db contract flag from the filter map
		Boolean isDBContract = (Boolean)criteria.getFilterValue(
				PendingWithdrawalSummaryReportData.CONTRACT_TYPE_DB);
		
		if (isDBContract == null) {
			isDBContract = Boolean.FALSE;
		}
		
		if (dataItems != null) {

			TransactionHandlerFactory factory = 
				TransactionHandlerFactory.getInstance(isDBContract.booleanValue());

			/*
			 * For each transaction data item, transform it into a
			 * TransactionHistoryItem.
			 */
			List<TransactionHistoryItem> items = new ArrayList<TransactionHistoryItem>();		
			for (TransactionDataItem transactionDataItem: dataItems) {

				if (isDBContract) {
					transactionDataItem.setDBContract(true);
				}

				TransactionHistoryItem item = factory.getTransactionHandler(
						new TransactionHandlerKey(WITHDRAWAL, 
								PENDING )).transform(transactionDataItem);

				items.add(item);
			}

			reportData.setDetails(items);
		}
	}
	
	/**
	 * Method to get the CSV data for the Pending Withdrawal Summary Page.
	 * The CSV download needs the Summary & payee details for all the 
	 * transactions. 
	 * 
	 * @param criteria
	 * @param reportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	@SuppressWarnings("unchecked")
	private void getCSVReportData(ReportCriteria criteria,
			PendingWithdrawalSummaryReportData reportData)
	throws SystemException, ReportServiceException {
	
		//  get the proposal number from the filter map
		String proposalNumber = (String) criteria.getFilterValue(
				PendingWithdrawalSummaryReportData.FILTER_PROPOSAL_NUMBER);
		
		// get the from & to date from the filter Map
		Date fromDate = (Date) criteria.getFilterValue(
				PendingWithdrawalSummaryReportData.FILTER_FROM_DATE);
		
		Date toDate = (Date) criteria.getFilterValue(
				PendingWithdrawalSummaryReportData.FILTER_TO_DATE);
		
		// get the default date indicator  from the filter map
		Boolean isDefaultDate = (Boolean)criteria.getFilterValue(
				PendingWithdrawalSummaryReportData.DEFAULT_DATE_IND);
		
		// get the isTransactionSort flag from the filter map
		Boolean isTransactionSort = (Boolean)criteria.getFilterValue(
				PendingWithdrawalSummaryReportData.SORT_BY_TRANSACTION_DATE);
		
		// get the Withdrawal General Info List 
		List<WithdrawalGeneralInfoVO> generalInfoList = 
			PendingWithdrawalSummaryDAO.getWithdrawalGeneralInfoListByProposalNumber(criteria);
		
		// get the Payee List for all the transaction for a contract as a Map
		// where the key is the transaction number and the value is the 
		// payee list for that transaction.
		Map<String, List<WithdrawalPayeePaymentVO>> payeeListByTransaction = 
			PendingWithdrawalSummaryDAO.getPayeeListByProposalNumber(
					String.valueOf(proposalNumber), fromDate, toDate,isDefaultDate); 
		
		List<WithdrawalDetailsReportItem> withdrawalDetailsReportItems = 
			new ArrayList<WithdrawalDetailsReportItem>();
		
		//Block to validate the sorting of Default Date (9999-12-31) 
		//in  transaction date section
    	List<WithdrawalGeneralInfoVO> defaultDateList = null;
    	List<WithdrawalGeneralInfoVO> otherDateList = null;
    	
    	if (generalInfoList != null && !generalInfoList.isEmpty()) {
    		defaultDateList=new ArrayList<WithdrawalGeneralInfoVO>();

    		if(isTransactionSort){
    			otherDateList=new ArrayList<WithdrawalGeneralInfoVO>();
    			Calendar toCal = Calendar.getInstance();
    			toCal.set(9999, Calendar.DECEMBER, 31);

    			for(WithdrawalGeneralInfoVO withdrawalGeneralInfoVO : generalInfoList){
    				if(convertDateToString(toCal.getTime()).equals(
    						convertDateToString(withdrawalGeneralInfoVO.getWithdrawalDate()))){
    					defaultDateList.add(withdrawalGeneralInfoVO);
    				} else {
    					otherDateList.add(withdrawalGeneralInfoVO);
    				}
    			}
    			defaultDateList.addAll(otherDateList);
    		}
    		else
    		{
    			defaultDateList.addAll(generalInfoList);
    		}
			
			// iterate the generalInfoList and add the appropriate 
			// payee list for the transaction 
			for(WithdrawalGeneralInfoVO withdrawalGeneralInfoVO : defaultDateList){
				WithdrawalDetailsReportItem reportItem = new WithdrawalDetailsReportItem();
				
				// get the transaction number
				String transactionNo = withdrawalGeneralInfoVO.getTransactionNumber();
				//get the transaction date
				Date transactionDate = withdrawalGeneralInfoVO.getWithdrawalDate();
				
				// return null if the date is "9999-12-31"
				if (AbstractTransactionHandler.isDummyDate(transactionDate)) {
					withdrawalGeneralInfoVO.setWithdrawalDate(null);
				} 
				// set the current generalInfo Object in the WithdrawalDetailsReportItem
				reportItem.setWithdrawalGeneralInfoVO(withdrawalGeneralInfoVO);
				
				// get the Payee list for the transaction number 
				List payeeList = payeeListByTransaction.get(transactionNo);
			
				//Method call to set the required descriptions
				fillPayeeTypeDesc(payeeList);
				
				// Set the Payee List in the WithdrawalItem
				reportItem.setWithdrawalPayeePaymentVO(payeeList);

				// add the WithdrawalDetailsReportItem to the List
				withdrawalDetailsReportItems.add(reportItem);
			}
		}
		
		// set the WithdrawalDetailsReportItem List in the reportData.details
		reportData.setDetails(withdrawalDetailsReportItems);
	}
	/**
	 * Method to set the payee type (payment to) and bank account descriptions from the LOOKUP table.
	 * 
	 * @param payeePaymentList
	 * @throws SystemException
	 */
	private void fillPayeeTypeDesc(List<WithdrawalPayeePaymentVO> payeePaymentList) throws SystemException{
	
		if(payeePaymentList != null && !payeePaymentList.isEmpty()){
			// Retrieving the applicationId to know whether the invocation has been
			// made from [PSW, EZK or FRW] which application
			String applicationId = new BaseEnvironment().getApplicationId();
			
			for(WithdrawalPayeePaymentVO withdrawalPayeePaymentVO:payeePaymentList){
				withdrawalPayeePaymentVO.setPaymentTo(WithdrawalDetailsDAOHelper.getPayeeTypeDesc(withdrawalPayeePaymentVO.getPaymentTo(), applicationId));
				withdrawalPayeePaymentVO.setAccountType(WithdrawalDetailsDAOHelper.getBankAccountTypeDesc(withdrawalPayeePaymentVO.getAccountType(), applicationId));
			}
		}
	}
	
	/**
	 * Method to convert the Date object to String object in standard Date Format.
	 * @param inputDate 		
	 * @return String 
	 * 		as formatted output
	 */
	public static String convertDateToString(Date inputDate) {

		if (inputDate == null){			
			return null;
		}

		return STANDARD_DATE_FORMAT.format(inputDate);
	}
	
}
