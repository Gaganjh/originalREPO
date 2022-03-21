package com.manulife.pension.platform.web.report.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;


import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.report.TransactionDetailsWithdrawalForm;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.LIADisplayHelper;
import com.manulife.pension.service.account.valueobject.LoanGeneralInfoVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.LifeIncomeAmountDetailsVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.CompletedWithdrawalDetailsReportData;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalAfterTaxInfoVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalCalculatedInfoVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalDetailsReportItem;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalGeneralInfoVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalMoneyTypeVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalPayeePaymentVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.MessageProvider;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;
import com.manulife.util.render.ZipCodeRender;

/**
 * Utility class for the PSW & FRW Completed Withdrawal Detail page.
 * PSW & FRW Common functionality are implemented in this class
 *   
 * @author Tamilarasu Krishnamoorthy
 *
 */
public class WithdrawalDetailsUtility {

	public static final DecimalFormat twoDecimals = new DecimalFormat("0.00");
	public static final DecimalFormat threeDecimals = new DecimalFormat("0.000");
	
	//synchronizes this method to avoid race condition.
	public static synchronized String formatTwoFormatter(double value) { 
        return twoDecimals.format(value); 
    }
	
	public static synchronized String formatThreeFormatter(double value) { 
        return threeDecimals.format(value); 
    }
	
	/**
	 * Set Participant id ,transaction number, contract id in 
	 * reportCriteria object
	 * 
	 * @param criteria
	 * @param actionForm
	 * @param request
	 * @throws SystemException
	 */
	public static void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm actionForm, HttpServletRequest request,
			Contract currentContract)
	throws SystemException {
		
		TransactionDetailsWithdrawalForm form = null;
		String contractNumber=null;
			form = (TransactionDetailsWithdrawalForm)actionForm;
		//Get the transaction number from request
		String transactionNumber = (String)request.getParameter(CommonConstants.TRANSACTION_NUMBER);
		if (StringUtils.isBlank(transactionNumber)) {
			transactionNumber = form.getTransactionNumber();
		}
		if (StringUtils.isBlank(form.getTransactionNumber())) {
			form.setTransactionNumber(transactionNumber);
		}
		
		//Get the current Contract Number from Current contract instance
		if ( currentContract.getContractNumber()!=0) {
			contractNumber = String.valueOf(currentContract.getContractNumber());
		}
		String participantId = (String)request.getParameter(CommonConstants.PARTICIPANT_ID);
		if (StringUtils.isBlank(participantId)) {
				participantId = form.getPptId();
		}
		if (StringUtils.isBlank(form.getPptId())) {
			form.setPptId(participantId);
		}	
		
		//Checking whether it's DB contract , then add with criteria
		if (currentContract.isDefinedBenefitContract()) {
			criteria.addFilter(CommonConstants.CONTRACT_TYPE_DB, Boolean.TRUE);
		}else {
			criteria.addFilter(CommonConstants.CONTRACT_TYPE_DB, Boolean.FALSE);
		}
		
		//Transaction number, participant Id, contract number added as criteria
		criteria.addFilter(CompletedWithdrawalDetailsReportData.FILTER_TRANSACTION_NUMBER, transactionNumber);
		criteria.addFilter(CompletedWithdrawalDetailsReportData.FILTER_PRTID, participantId);
		criteria.addFilter(CompletedWithdrawalDetailsReportData.FILTER_CONTRACT_NUMBER, contractNumber);
	}
	
	/**
	 * Get the Report Id
	 * @return REPORT_ID
	 */
	public static String getReportId() {
		return CompletedWithdrawalDetailsReportData.REPORT_ID;
	}
	
	/**
	 * Get the report name 
	 * @return REPORT_NAME
	 */
	public static String getReportName() {
		return CommonConstants.REPORT_NAME;
	}
	
	/**
	 * This method will retrieve the data from APOLLO and 
	 * 	1. Sets the value for TransactionDetailsWithdrawalForm from reportData
	 *  2. Checks for correctionIndicator to display error message.
	 * 	3. And sets reportData and formBean in request.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public static void doCommon(
			BaseReportForm reportForm, 
			HttpServletRequest request,
			HttpServletResponse response,String applicationId) throws SystemException {
		
		WithdrawalGeneralInfoVO withdrawalGeneralInfoVO = null;
		List<WithdrawalPayeePaymentVO> payeePaymentVOList = null;
		
		CompletedWithdrawalDetailsReportData report = 
			(CompletedWithdrawalDetailsReportData)request.getAttribute(
					CommonConstants.REPORT_BEAN);
		
		TransactionDetailsWithdrawalForm form =
			(TransactionDetailsWithdrawalForm) reportForm;
		
		if (report.getDetails() != null && report.getDetails().size() != 0) {
			List<WithdrawalDetailsReportItem> details = 
				(List<WithdrawalDetailsReportItem>)report.getDetails();
			
			if (details!=null && !details.isEmpty()) {
				//Excepting details list with  single entry for 
				//completed withdrawal Detail page  	
				withdrawalGeneralInfoVO = 
					details.get(0).getWithdrawalGeneralInfoVO();
				payeePaymentVOList = 
					details.get(0).getWithdrawalPayeePaymentVO();
			}
			
			boolean liaWithdrawalInd = false;
			if (withdrawalGeneralInfoVO != null) {
				form.setTransactionNumber(
						withdrawalGeneralInfoVO.getTransactionNumber());
				
				withdrawalGeneralInfoVO.setMaskedSSN(SSNRender.format(
						withdrawalGeneralInfoVO.getSsn(), CommonConstants.BLANK, true));
				liaWithdrawalInd = withdrawalGeneralInfoVO.isLiaWithdrawalInd();
			}
			
			if (payeePaymentVOList != null && payeePaymentVOList.size()>0 ) {
				form.setPayeeCount(payeePaymentVOList.size());
				for(WithdrawalPayeePaymentVO payeePaymentVO : payeePaymentVOList) {
					if(CommonConstants.BD_APPLICATION_ID.equalsIgnoreCase(applicationId)){
						payeePaymentVO.setPaymentTo(WordUtils.capitalize(payeePaymentVO.getPaymentTo()));
						if(CommonConstants.PAYMENT_METHOD_PC.equalsIgnoreCase(payeePaymentVO.getPaymentMethod())){
							payeePaymentVO.setPaymentMethod("Paid by Check");
						}else {
							payeePaymentVO.setPaymentMethod(WordUtils.capitalize(payeePaymentVO.getPaymentMethod()));
						}
					}
					payeePaymentVO.setZip(ZipCodeRender.format(payeePaymentVO.getZip(),""));
					if(!StringUtils.isBlank(payeePaymentVO.getAccountNumber())){
						//If WithdrawalPayeePaymentVO.AccountNumber have value,  
						//it should be masked with xxxxxxxxxx .		
						payeePaymentVO.setMaskedAccountNumber(CommonConstants.MASK_ACCOUNT_NUMBER);
					}
				}
			}
			
			String participantId = (String)request.getParameter(CommonConstants.PARTICIPANT_ID);
			if (StringUtils.isBlank(participantId)) {
					participantId = form.getPptId();
			}
			LifeIncomeAmountDetailsVO liaDetails = ContractServiceDelegate
					.getInstance().getLIADetailsByParticipantId(participantId);
			if(liaWithdrawalInd){
				form.setShowLiaWithdrawalMessage(true);
			}
			if (LIADisplayHelper.isShowLIADetailsSection(liaDetails
					.getAnniversaryDate())
					&& withdrawalGeneralInfoVO.getWithdrawalDate().after(
							liaDetails.getEffectiveDate())) {
				form.setShowLiaWithdrawalNotification(true);
			}
			
		}

	}

	/**
	 * Creates a StringBuffer and the reportData is populated in the CSV format
	 * and converts the StringBuffer as byte[] and returns the byte[]
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @return byte[]
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public static byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request, 
			Contract currentContract, boolean maskSSN, String message,
			List<String> withdrawalFieldLabels, String appId) {
		
		WithdrawalGeneralInfoVO withdrawalGeneralInfoVO = null;
		List<WithdrawalMoneyTypeVO> moneyTypeVOList = null;
		List<WithdrawalPayeePaymentVO> payeePaymentVOList = null;
		int currentPayeeIndex = 1;
		int totalPayee = 0;

		CompletedWithdrawalDetailsReportData theReport = 
			(CompletedWithdrawalDetailsReportData)report;
		
		TransactionDetailsWithdrawalForm form =
			(TransactionDetailsWithdrawalForm) reportForm;
		
		List<WithdrawalDetailsReportItem> details = 
			(List<WithdrawalDetailsReportItem>)theReport.getDetails();
		
		if (details!=null && !details.isEmpty()) {
			withdrawalGeneralInfoVO = 
				details.get(0).getWithdrawalGeneralInfoVO();
			moneyTypeVOList = 
				details.get(0).getWithdrawalMoneyTypeVO();
			payeePaymentVOList = 
				details.get(0).getWithdrawalPayeePaymentVO();
		}
		WithdrawalCalculatedInfoVO withdrawalCalculatedInfoVO=theReport.getWithdrawalCalculatedInfoVO();
		// Create a StringBuffer
		StringBuffer buffer = new StringBuffer();
		
		//Contract and its Name displayed at top.
		buffer.append("Contract").append(CommonConstants.COMMA)
			.append(currentContract.getContractNumber()).append(CommonConstants.COMMA)
			.append(escapeField(currentContract.getCompanyName()));
		
		buffer.append(CommonConstants.LINE_BREAK);
		buffer.append(CommonConstants.LINE_BREAK);
		
		// if correction indicator is yes then add the error message
		if (CommonConstants.YES.equalsIgnoreCase(theReport.getTxnCorrectionIndicator())) {
			buffer.append(message);
		} else {
			//Section 1:Withdrawal general information
			buffer.append(withdrawalFieldLabels.get(32)).append(CommonConstants.LINE_BREAK);
				if (withdrawalGeneralInfoVO != null) {
					buffer.append(withdrawalFieldLabels.get(0)).append(CommonConstants.COMMA)
						.append(getAmountFormat(
								withdrawalGeneralInfoVO.getPaymentAmount()));
					buffer.append(CommonConstants.LINE_BREAK);
		
					/*buffer.append(withdrawalFieldLabels.get(1)).append(CommonConstants.COMMA)
						.append(withdrawalGeneralInfoVO.getTransactionType());
					buffer.append(CommonConstants.LINE_BREAK);*/
		
					buffer.append(withdrawalFieldLabels.get(2)).append(CommonConstants.COMMA)
						.append(withdrawalGeneralInfoVO.getTransactionTypeDescription());
					buffer.append(CommonConstants.LINE_BREAK);
					
					if(form.isShowLiaWithdrawalMessage()){
						buffer.append(ContentHelper.
								getContentText(CommonContentConstants.MISCELLANEOUS_WITHDRAWAL_AMOUNT_FOR_LIA, 
										ContentTypeManager.instance().MISCELLANEOUS, null));
						buffer.append(CommonConstants.LINE_BREAK);
					}
					
					if (!currentContract.isDefinedBenefitContract()) {
						buffer.append(withdrawalFieldLabels.get(3)).append(CommonConstants.COMMA)
							.append(escapeField(withdrawalGeneralInfoVO.getName()));
						buffer.append(CommonConstants.LINE_BREAK);
		
						buffer.append(withdrawalFieldLabels.get(4)).append(CommonConstants.COMMA)
							.append(SSNRender.format(
									withdrawalGeneralInfoVO.getSsn(), CommonConstants.BLANK, maskSSN));
						buffer.append(CommonConstants.LINE_BREAK);
					}
					
					buffer.append(withdrawalFieldLabels.get(5)).append(CommonConstants.COMMA)
						.append(DateRender.format(
								withdrawalGeneralInfoVO.getWithdrawalDate(),
								RenderConstants.MEDIUM_YMD_SLASHED));
					buffer.append(CommonConstants.LINE_BREAK);
					
					buffer.append(withdrawalFieldLabels.get(6)).append(CommonConstants.COMMA)
						.append(withdrawalGeneralInfoVO.getTransactionNumber());
					buffer.append(CommonConstants.LINE_BREAK);
					
				}
				
				if (withdrawalGeneralInfoVO != null) {
					buffer.append(CommonConstants.LINE_BREAK);
					if(withdrawalGeneralInfoVO.getRothStartedYears()>0 && withdrawalGeneralInfoVO.getRothStartedYears()!=9999 ){
						buffer.append(CommonConstants.LINE_BREAK);
						buffer.append(withdrawalFieldLabels.get(35)).append(CommonConstants.COMMA)
						.append(withdrawalGeneralInfoVO.getRothStartedYears());
						buffer.append(CommonConstants.LINE_BREAK);
					}
					if(withdrawalGeneralInfoVO.isPre87Present()){
						buffer.append(withdrawalFieldLabels.get(36)).append(CommonConstants.COMMA)
						.append(CommonConstants.DOLLAR_SIGN +withdrawalGeneralInfoVO.getPre87Amount());
						buffer.append(CommonConstants.LINE_BREAK);
					}
				}
			// Section 1: Withdrawal general information completed
				buffer.append(CommonConstants.LINE_BREAK);
				
				//Section 2:Withdrawal information detail which will display according to version .
				// Display roth tax ,if roth money type is available. 
				// Check for DB contract and display Tax section.
				buffer.append(withdrawalFieldLabels.get(34)).append(CommonConstants.LINE_BREAK);
				
				
				if (CommonConstants.VERSION_1.equals(theReport.getWebDisplayVersion())) {
					// money type table column headers
					buffer.append(withdrawalFieldLabels.get(16)).append(CommonConstants.COMMA)
						.append(withdrawalFieldLabels.get(18)).append(CommonConstants.COMMA)
						.append(withdrawalFieldLabels.get(19)).append(CommonConstants.COMMA)
						.append(withdrawalFieldLabels.get(20));
					buffer.append(CommonConstants.LINE_BREAK);
					
					if (moneyTypeVOList != null) {
						// money type table values
						for(WithdrawalMoneyTypeVO withdrawalMoneyTypeVO : moneyTypeVOList) {
							buffer.append(withdrawalMoneyTypeVO.getMoneyType())
								.append(CommonConstants.COMMA)
								.append(getAmountFormat(
										withdrawalMoneyTypeVO.getAccountBalance()))
								.append(CommonConstants.COMMA)
								.append(getAmountFormatWith3Decimal(withdrawalMoneyTypeVO.getVestingPercentage()))
								.append(CommonConstants.COMMA)
								.append(getAmountFormat(
										withdrawalMoneyTypeVO.getAvailableAmount()));
							buffer.append(CommonConstants.LINE_BREAK);
						}
					}
					
				} else if(CommonConstants.VERSION_2.equals(theReport.getWebDisplayVersion())) {
					
					buffer.append(withdrawalFieldLabels.get(16)).append(CommonConstants.COMMA).append(CommonConstants.BLANK)
						.append(CommonConstants.COMMA).append(withdrawalFieldLabels.get(17));
					buffer.append(CommonConstants.LINE_BREAK);
					
					if (moneyTypeVOList != null){
						for (WithdrawalMoneyTypeVO withdrawalMoneyTypeVO : moneyTypeVOList) {
							buffer.append(withdrawalMoneyTypeVO.getMoneyType())
								.append(CommonConstants.COMMA).append(CommonConstants.BLANK).append(CommonConstants.COMMA)
								.append(getAmountFormat(
										withdrawalMoneyTypeVO.getAvailableAmount()));
							buffer.append(CommonConstants.LINE_BREAK);
						}
					}
				}
				buffer.append(CommonConstants.LINE_BREAK);
				if(withdrawalCalculatedInfoVO != null) {
					if (CommonConstants.VERSION_1.equals(theReport.getWebDisplayVersion())){
						buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
					}
					buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
					if (CommonConstants.VERSION_2.equals(theReport.getWebDisplayVersion())){
						buffer.append(withdrawalFieldLabels.get(21)).append(CommonConstants.COMMA);
					}
					if (CommonConstants.VERSION_1.equals(theReport.getWebDisplayVersion())){
						buffer.append(withdrawalFieldLabels.get(22)).append(CommonConstants.COMMA);
					}
					buffer.append(getAmountFormat(
								withdrawalCalculatedInfoVO.getTotalAmount()))
						.append(CommonConstants.COMMA);
					
					buffer.append(CommonConstants.LINE_BREAK);
					
					//Checking MVA indicator is  equal to "Y " and display its value
					if (CommonConstants.YES.equalsIgnoreCase(withdrawalCalculatedInfoVO.getMvaAppliesIndicator())){
						if (CommonConstants.VERSION_1.equals(theReport.getWebDisplayVersion())){
								buffer.append(CommonConstants.COMMA).append(CommonConstants.BLANK);
							}
							buffer.append(CommonConstants.COMMA).append(CommonConstants.BLANK);
							buffer.append(withdrawalFieldLabels.get(23))
							.append(CommonConstants.COMMA)
							.append(getAmountFormat(
									withdrawalCalculatedInfoVO.getMvaAmount()));
							buffer.append(CommonConstants.LINE_BREAK);
					} else {
						buffer.append(CommonConstants.LINE_BREAK);
					}
					
					//check for Fund on deposit interest amount not empty.
					if (withdrawalCalculatedInfoVO.getFundsOnDepositInterest() != null ){
						if (CommonConstants.VERSION_1.equals(theReport.getWebDisplayVersion())) {
							buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
						}
						buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
						buffer.append(withdrawalFieldLabels.get(24))
							.append(CommonConstants.COMMA)
							.append(getAmountFormat(withdrawalCalculatedInfoVO.getFundsOnDepositInterest()))
							.append(CommonConstants.COMMA);
						buffer.append(CommonConstants.LINE_BREAK);
					}
					
					if (!currentContract.isDefinedBenefitContract()) {
						if (CommonConstants.VERSION_1.equals(theReport.getWebDisplayVersion())) {
							buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
						}
						buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
						buffer.append(withdrawalFieldLabels.get(25)).append(CommonConstants.COMMA)
							.append(getAmountFormat
									(withdrawalCalculatedInfoVO.getTaxableAmount()))
							.append(CommonConstants.COMMA);
						buffer.append(CommonConstants.LINE_BREAK);
						if (CommonConstants.VERSION_1.equals(theReport.getWebDisplayVersion())){
							buffer.append(CommonConstants.COMMA).append(CommonConstants.BLANK);
						}
						buffer.append(CommonConstants.COMMA).append(CommonConstants.BLANK);
						buffer.append(withdrawalFieldLabels.get(26)).append(CommonConstants.COMMA)
							.append(getAmountFormat(withdrawalCalculatedInfoVO.getStateTaxAmount()))
							.append(CommonConstants.COMMA);
						buffer.append(CommonConstants.LINE_BREAK);
						if (CommonConstants.VERSION_1.equals(theReport.getWebDisplayVersion())) {
							buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
						}
						buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
						buffer.append(withdrawalFieldLabels.get(27)).append(CommonConstants.COMMA)
							.append(getAmountFormat(withdrawalCalculatedInfoVO.getFederalTaxAmount()))
							.append(CommonConstants.COMMA);
						buffer.append(CommonConstants.LINE_BREAK);
						
						if (CommonConstants.YES.equals(withdrawalCalculatedInfoVO.getRothMoneyIndicator())) {
							if (CommonConstants.VERSION_1.equals(theReport.getWebDisplayVersion())) {
								buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
							}
							buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
							buffer.append(withdrawalFieldLabels.get(28)).append(CommonConstants.COMMA)
								.append(getAmountFormat(withdrawalCalculatedInfoVO.getRothTaxableAmount()))
								.append(CommonConstants.COMMA);
							buffer.append(CommonConstants.LINE_BREAK);
							
							if (CommonConstants.VERSION_1.equals(theReport.getWebDisplayVersion())) {
								buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
							}
							buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
							buffer.append(withdrawalFieldLabels.get(29)).append(CommonConstants.COMMA)
								.append(getAmountFormat(withdrawalCalculatedInfoVO.getRothStateTaxAmount()))
								.append(CommonConstants.COMMA);
							buffer.append(CommonConstants.LINE_BREAK);
							
							if (CommonConstants.VERSION_1.equals(theReport.getWebDisplayVersion())) {
								buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
							}
							buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
							buffer.append(withdrawalFieldLabels.get(30)).append(CommonConstants.COMMA)
								.append(getAmountFormat(withdrawalCalculatedInfoVO.getRothFederalTaxAmount()))
								.append(CommonConstants.COMMA);
							buffer.append(CommonConstants.LINE_BREAK);
						}
					}
					if (CommonConstants.VERSION_1.equals(theReport.getWebDisplayVersion())) {
					 buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
					}
					buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
					buffer.append(withdrawalFieldLabels.get(31)).append(CommonConstants.COMMA)
						.append(getAmountFormat(withdrawalCalculatedInfoVO.getTotalPaymentAmount()))
						.append(CommonConstants.COMMA);
					buffer.append(CommonConstants.LINE_BREAK);
					
					if (StringUtils.isNotBlank(withdrawalCalculatedInfoVO.getForfeitedOrUMText1()) &&
							StringUtils.isNotBlank(withdrawalCalculatedInfoVO.getForfeitedOrUMText2())) {
						if (CommonConstants.VERSION_1.equals(theReport.getWebDisplayVersion())) {
							 buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
						}
						buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
						if (StringUtils.equalsIgnoreCase(appId, CommonConstants.PS_APPLICATION_ID)) {
							buffer.append(withdrawalCalculatedInfoVO.getForfeitedOrUMText1()).append(CommonConstants.COMMA);
						} else {
							buffer.append(withdrawalCalculatedInfoVO.getForfeitedOrUMText1ForFRW()).append(CommonConstants.COMMA);
						}
						buffer.append(getAmountFormat(withdrawalCalculatedInfoVO.getForfeitedOrUnvestedERAmount()))
							.append(CommonConstants.COMMA);
						buffer.append(CommonConstants.LINE_BREAK);
						
						if (CommonConstants.VERSION_1.equals(theReport.getWebDisplayVersion())) {
							 buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
						}
						buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
						
						if (StringUtils.equalsIgnoreCase(appId, CommonConstants.PS_APPLICATION_ID)) {
							buffer.append(withdrawalCalculatedInfoVO.getForfeitedOrUMText2()).append(CommonConstants.COMMA);
						} else {
							buffer.append(withdrawalCalculatedInfoVO.getForfeitedOrUMText2ForFRW()).append(CommonConstants.COMMA);
						}
						buffer.append(CommonConstants.BLANK).append(CommonConstants.COMMA);
						buffer.append(CommonConstants.LINE_BREAK);
					}
					}
				
				buffer.append(CommonConstants.LINE_BREAK);
			//Section 3:Display payee information with payee address 
			// and bank detail. Maximum only 2 payee are displayed
			buffer.append(withdrawalFieldLabels.get(33)).append(CommonConstants.LINE_BREAK);
			
			buffer.append(CommonConstants.LINE_BREAK);
			if (payeePaymentVOList != null && !payeePaymentVOList.isEmpty()) {
				
				totalPayee = payeePaymentVOList.size();
				
				for(WithdrawalPayeePaymentVO withdrawalPayeePaymentVO : payeePaymentVOList) {
					if (totalPayee > 1) {
						buffer.append(currentPayeeIndex+ ". ") ;
					}
					
					buffer.append(withdrawalFieldLabels.get(37)).append(CommonConstants.COMMA)
					.append(withdrawalPayeePaymentVO.getPayeeName());
				buffer.append(CommonConstants.LINE_BREAK);
					
					buffer.append(withdrawalFieldLabels.get(0)).append(CommonConstants.COMMA)
						.append(getAmountFormat(withdrawalPayeePaymentVO.getPaymentAmount()));
					buffer.append(CommonConstants.LINE_BREAK);
					
				
					
					buffer.append(withdrawalFieldLabels.get(8)).append(CommonConstants.COMMA)
						.append(withdrawalPayeePaymentVO.getPaymentMethod());
					buffer.append(CommonConstants.LINE_BREAK);
					
					buffer.append(withdrawalFieldLabels.get(7)).append(CommonConstants.COMMA)
					.append(withdrawalPayeePaymentVO.getPaymentTo());
				buffer.append(CommonConstants.LINE_BREAK);
					
				/*	buffer.append(withdrawalFieldLabels.get(9)).append(CommonConstants.COMMA)
						.append(withdrawalPayeePaymentVO.getAccountType());
					buffer.append(CommonConstants.LINE_BREAK);
					buffer.append(CommonConstants.LINE_BREAK);*/
				buffer.append(CommonConstants.LINE_BREAK);
					buffer.append(withdrawalFieldLabels.get(10)).append(CommonConstants.COMMA).append(CommonConstants.LINE_BREAK);
					buffer.append(escapeField(withdrawalPayeePaymentVO.getPayeeName()));
					buffer.append(CommonConstants.LINE_BREAK);
					buffer.append(escapeField(withdrawalPayeePaymentVO.getAddressLine1()));
					buffer.append(CommonConstants.LINE_BREAK);
					buffer.append(escapeField(withdrawalPayeePaymentVO.getAddressLine2()));
					buffer.append(CommonConstants.LINE_BREAK);
					if (StringUtils.isNotBlank(withdrawalPayeePaymentVO.getCity())) {
						buffer.append(escapeField(withdrawalPayeePaymentVO.getCity() + ", "));
					}
					if (StringUtils.isNotBlank(withdrawalPayeePaymentVO.getState())) {
						buffer.append(withdrawalPayeePaymentVO.getState() + " "); 
					}
					buffer.append(withdrawalPayeePaymentVO.getZip());
					buffer.append(CommonConstants.LINE_BREAK);
					buffer.append(withdrawalPayeePaymentVO.getCountry());
					buffer.append(CommonConstants.LINE_BREAK);
					buffer.append(CommonConstants.LINE_BREAK);
					
					buffer.append(withdrawalFieldLabels.get(11)).append(CommonConstants.LINE_BREAK);
					buffer.append(withdrawalFieldLabels.get(12)).append(CommonConstants.COMMA)
						.append(escapeField(withdrawalPayeePaymentVO.getBankBranchName()));
					buffer.append(CommonConstants.LINE_BREAK);
					buffer.append(withdrawalFieldLabels.get(13)).append(CommonConstants.COMMA)
						.append(withdrawalPayeePaymentVO.getRoutingABAnumber());
					buffer.append(CommonConstants.LINE_BREAK);
					
					// WDMar2017 Changes - wilange : PSW WithdrawalReport Changes
					/*buffer.append(withdrawalFieldLabels.get(14)).append(CommonConstants.COMMA);
					if (StringUtils.isNotBlank(withdrawalPayeePaymentVO.getAccountNumber())) {
						buffer.append(CommonConstants.MASK_ACCOUNT_NUMBER);
					} else {
						buffer.append(CommonConstants.BLANK);	
					}
					
					buffer.append(CommonConstants.LINE_BREAK);
					*/
					buffer.append(withdrawalFieldLabels.get(15)).append(CommonConstants.COMMA)
						.append(escapeField(withdrawalPayeePaymentVO.getCreditPayeeName()));
					buffer.append(CommonConstants.LINE_BREAK);
					buffer.append(CommonConstants.LINE_BREAK);
					
					// We have display only 2 payee as per the requirement 
					/*if (currentPayeeIndex == 2) {
						break;
					}*/
					if(withdrawalPayeePaymentVO.getAfterTaxVO()!=null && !withdrawalPayeePaymentVO.getAfterTaxVO().isEmpty()) {
						buffer.append(withdrawalFieldLabels.get(42));
						buffer.append(CommonConstants.LINE_BREAK);
						buffer.append(withdrawalFieldLabels.get(38))
							.append(CommonConstants.COMMA).append(withdrawalFieldLabels.get(39)).append(CommonConstants.COMMA)
							.append(withdrawalFieldLabels.get(40)).append(CommonConstants.COMMA).append(withdrawalFieldLabels.get(41));
						buffer.append(CommonConstants.LINE_BREAK);
						for(WithdrawalAfterTaxInfoVO aftertaxObject : withdrawalPayeePaymentVO.getAfterTaxVO()) {

							buffer.append(aftertaxObject.getMoneyTypeInfo()).append(CommonConstants.COMMA);

							if(aftertaxObject.getAfterTaxNetContribution()!=null) {
								buffer.append(aftertaxObject.getAfterTaxNetContribution().toString() ).append(CommonConstants.COMMA);
							}
							if(aftertaxObject.getAfterTaxNetEarning()!=null) {
								buffer.append(aftertaxObject.getAfterTaxNetEarning().toString() ).append(CommonConstants.COMMA);
							}
							if(aftertaxObject.getAfterTaxWDAmt()!=null) {
								buffer.append(aftertaxObject.getAfterTaxWDAmt().toString() ).append(CommonConstants.COMMA);
							}
							buffer.append(CommonConstants.LINE_BREAK);
						}
					}
					
					
					buffer.append(CommonConstants.LINE_BREAK);
					
					currentPayeeIndex++;	
					
					
				}
		}
			
			
	
			
}
		//Buffered data return as byte.
		return buffer.toString().getBytes();
	}
	
	
	/**
	 * Get the amount format to display with 2 decimal places
	 * @param amount
	 */
	private static String getAmountFormat(BigDecimal amount){
		return formatTwoFormatter((amount.setScale(
				2, BigDecimal.ROUND_HALF_UP)).doubleValue());
		
	}

	/**
	 * Get the amount format to display with 3 decimal places
	 * @param amount
	 */
	private static String getAmountFormatWith3Decimal(BigDecimal amount){
		return formatThreeFormatter((amount.setScale(
				3, BigDecimal.ROUND_HALF_UP)).doubleValue());
		
	}
	
	/**
	 * If the current page has any errors, the same should be displayed in the 
	 * CSV. This method does that.
	 *  
	 * @param errorCollection
	 * @param currentContract
	 * @return CSV with error text
	 * @throws SystemException
	 */
	public static String getErrorDownload (
			Collection<GenericException> errorCollection, Contract currentContract) 
	throws SystemException {
		
	    try {
	        StringBuffer errorText = new StringBuffer();
	        
	        // get the error text for all the errors from CMA
	        String[] errors = 
	        	MessageProvider.getInstance().getMessages(errorCollection);
	      //Contract and its Name displayed at top.
        	errorText.append("Contract").append(CommonConstants.COMMA)
        		.append(currentContract.getContractNumber())
        		.append(CommonConstants.COMMA).append(escapeField(currentContract.getCompanyName()));
        	errorText.append(CommonConstants.LINE_BREAK);
        	errorText.append(CommonConstants.LINE_BREAK);
	        for (int i=0; i < errors.length; i++ ) {
	        	errorText.append(errors[i]);
	        	errorText.append(CommonConstants.LINE_BREAK);
	        }
	        
	        String errorDesc =  errorText.toString();
	        //To replace &nbsp; with blank in error message.
	        errorDesc=errorDesc.replaceAll(CommonConstants.BLANK_STR, CommonConstants.BLANK);
	        return errorDesc;
	    } catch (ContentException e) {
	        throw new SystemException (e,
	            "error getting error text");
	    }
	}
	
	/**
     * Method to display comma(,) in downloaded spread sheet.
     * @param field String
     * @return field String
     */
    private static String escapeField(String field){
        
    	if(field.indexOf(CommonConstants.COMMA) != -1 ){
            StringBuffer newField = new StringBuffer();
            newField = newField.append(CommonConstants.DOUBLE_QUOTES).append(field).append(CommonConstants.DOUBLE_QUOTES);
            return newField.toString();
        }else{
            return field;
        }
    }
}