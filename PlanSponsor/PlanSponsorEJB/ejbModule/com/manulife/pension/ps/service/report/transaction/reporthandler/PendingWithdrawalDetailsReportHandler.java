package com.manulife.pension.ps.service.report.transaction.reporthandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.dao.PendingWithdrawalDetailsDAO;
import com.manulife.pension.ps.service.report.transaction.dao.PendingWithdrawalSummaryDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.participant.transaction.dao.WithdrawalDetailsDAOHelper;
import com.manulife.pension.service.report.participant.transaction.valueobject.PendingWithdrawalDetailsReportData;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalDetailsReportItem;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalGeneralInfoVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalMoneyTypeVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalPayeePaymentVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.util.render.SSNRender;

/**
 * Handler class for Pending Withdrawal Details page.
 * 
 * @author Puttaiah Arugunta
 *
 */
public class PendingWithdrawalDetailsReportHandler implements ReportHandler  {
	  
	/*
	 * Dummy date representing 9999-12-31 (GregorianCalendar's MONTH is 0 based)
	 */
	private static final long DUMMY_DATE = new GregorianCalendar(9999, 11, 31)
			.getTime().getTime();
	
	/**
	 * Method to get the report data for Pending Withdrawal Details page by 
	 * passing the Criteria object
	
	 * @param criteria ReportCriteria
	 * @return reportDate ReportData
	 * @throws SystemException, ReportServiceException
	 */
	public ReportData getReportData(ReportCriteria criteria) 
	throws SystemException, ReportServiceException {

		PendingWithdrawalDetailsReportData reportData = 
			new PendingWithdrawalDetailsReportData(criteria, 0) ;

		WithdrawalDetailsReportItem reportItem = 
			new WithdrawalDetailsReportItem();

		// get the transaction number and proposal number from criteria object
		String txnNumber = (String) criteria.getFilterValue(
				PendingWithdrawalDetailsReportData.FILTER_TRANSACTION_NUMBER);
		String proposalNumber = (String) criteria.getFilterValue(
				PendingWithdrawalDetailsReportData.FILTER_PROPOSAL_NUMBER);

		// fetch the General information VO for the given proposal and transaction number
		WithdrawalGeneralInfoVO withdrawalGeneralInfoVO = 
			PendingWithdrawalSummaryDAO.getWithdrawalGeneralInfoVOByTransactionNumber(
					proposalNumber, txnNumber);
		
		Date txnDate = withdrawalGeneralInfoVO.getWithdrawalDate();
		
		// Use the Transaction date as the Extract Txn date if the actual 
		// Transaction date is dummy date (i.e., 9999-12-31)
		if (isDummyDate(txnDate)) {
			txnDate = withdrawalGeneralInfoVO.getExtractTxnDate();
			withdrawalGeneralInfoVO.setWithdrawalDate(null);
		}
		
		// Mask the SSN as per the Participant’s SSN using standard 
		// SSN display format.
		withdrawalGeneralInfoVO.setSsn(SSNRender.format(
				withdrawalGeneralInfoVO.getSsn(), "", true));
	
		reportItem.setWithdrawalGeneralInfoVO(withdrawalGeneralInfoVO);
		
		// get the money list for the given proposal number, 
		// transaction number and Txn date 
		List<WithdrawalMoneyTypeVO> moneyTypeList = 
			PendingWithdrawalDetailsDAO.getMoneyTypeListByTransactionNumber(
					criteria, txnDate);
		
		// get the payee payment list for the given proposal number and transaction number
		List<WithdrawalPayeePaymentVO> payeePaymentList = 
			PendingWithdrawalDetailsDAO.getPayeePaymentListForTransaction(
					proposalNumber, txnNumber);
		
		//Check for Empty Payee payment list, 
		//if yes adding a dummy payee to show blank value in jsp
		if( payeePaymentList.isEmpty() )	{
			payeePaymentList.add(new WithdrawalPayeePaymentVO());
		}
		else{
			//Method call to set the required descriptions
			fillPayeeTypeDesc(payeePaymentList);
		}
		// Set the money type list and payee list to ReportItem class
		reportItem.setWithdrawalMoneyTypeVO(moneyTypeList);
		reportItem.setWithdrawalPayeePaymentVO(payeePaymentList);

		List<WithdrawalDetailsReportItem> reportItems = new ArrayList<WithdrawalDetailsReportItem>();
		reportItems.add(reportItem);
		// Set the report item to master VO
		reportData.setDetails(reportItems);

		return reportData;
	}
	/**
	 * Method to set the payee type (payment to) and bank account 
	 * descriptions from the LOOKUP table.
	 * 
	 * @param payeePaymentList
	 * @throws SystemException
	 */
	private void fillPayeeTypeDesc(List<WithdrawalPayeePaymentVO> payeePaymentList) 
	throws SystemException{
	
		if (payeePaymentList != null && !payeePaymentList.isEmpty()) {
			
			/* Retrieving the applicationId to know whether the invocation has been
			 made from
			 [PSW, EZK or FRW] which application*/
			String applicationId = new BaseEnvironment().getApplicationId();
			
			for(WithdrawalPayeePaymentVO withdrawalPayeePaymentVO:payeePaymentList){
				
				// Get the payee type description for the payee code.
				withdrawalPayeePaymentVO.setPaymentTo(
						WithdrawalDetailsDAOHelper.getPayeeTypeDesc(
								withdrawalPayeePaymentVO.getPaymentTo(), applicationId));

				// Get the bank account type description for the account type
				withdrawalPayeePaymentVO.setAccountType(
						WithdrawalDetailsDAOHelper.getBankAccountTypeDesc(
								withdrawalPayeePaymentVO.getAccountType(), applicationId));
			}
		}
	}
	
	/**
	 * Determine if date is a dummy date
	 */
	private static boolean isDummyDate(Date date) throws SystemException {
		return date.getTime() >= DUMMY_DATE;
	}
}
