package com.manulife.pension.ps.web.contract.csf.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.contract.csf.CsfConstants;
import com.manulife.pension.ps.web.contract.csf.CsfDataHelper;
import com.manulife.pension.ps.web.contract.csf.ParticipantServicesData;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;

/**
 * Class to load the the CSF view page Participant Section. 
 * 
 * This Class will determine the CMA keys for each filed in the Participant section 
 * and set in to ParticipantServicesData Value Object. This internally used to populate the CSF View page.
 * 
 * @author Puttaiah Arugunta
 *
 */
public class ParticipantServicesSectionUtil {

	
	private static final String HTML_STRONG_END_TAG = "</strong>";
	private static final String HTML_STRONG_BEGIN_TAG = "<strong>";

	/**
	 * Method to determine Participant online address changes are permitted for
	 * 
	 * @param data
	 * @param addressManagementCSF
	 */
	@SuppressWarnings("unchecked")
	public static void getAddressManagementView(ParticipantServicesData data, Map csfMap) {
		ContractServiceFeature addressManagementCSF = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.ADDRESS_MANAGEMENT_FEATURE);
		String activeAddress = null;
		String terminatedAddress = null;
		String disabledAddress = null;
		String retiredAddress = null;

		if (addressManagementCSF != null) {
			activeAddress= ContractServiceFeature.internalToBoolean(addressManagementCSF
					.getAttributeValue(ServiceFeatureConstants.ADDRESS_MANAGEMENT_ACTIVE)) ? CsfConstants.CSF_TRUE : CsfConstants.CSF_FALSE;
			terminatedAddress = ContractServiceFeature.internalToBoolean(addressManagementCSF
					.getAttributeValue(ServiceFeatureConstants.ADDRESS_MANAGEMENT_TERMINATED)) ? CsfConstants.CSF_TRUE : CsfConstants.CSF_FALSE;
			disabledAddress = ContractServiceFeature.internalToBoolean(addressManagementCSF
					.getAttributeValue(ServiceFeatureConstants.ADDRESS_MANAGEMENT_DISABLED)) ? CsfConstants.CSF_TRUE : CsfConstants.CSF_FALSE;
			retiredAddress = ContractServiceFeature.internalToBoolean(addressManagementCSF
					.getAttributeValue(ServiceFeatureConstants.ADDRESS_MANAGEMENT_RETIRED)) ? CsfConstants.CSF_TRUE : CsfConstants.CSF_FALSE;
		}
		StringBuffer sf = new StringBuffer();
		
		if (CsfConstants.CSF_FALSE.equals(activeAddress) && CsfConstants.CSF_FALSE.equals(terminatedAddress)
				&& CsfConstants.CSF_FALSE.equals(retiredAddress) && CsfConstants.CSF_FALSE.equals(disabledAddress)) {
			data.setParticipantOnlineAddressChanges(ContentConstants.PARTICIPANT_ONLINE_ADDRESS_CHANGES_NONSELECTED);
		}
		int count= 0;
		if (CsfConstants.CSF_TRUE.equals(activeAddress)) {
			data.setParticipantOnlineAddressChanges(ContentConstants.PARTICIPANT_ONLINE_ADDRESS_CHANGES_ACTIVE);
			sf.append(CsfConstants.ADDRESS_MANAGEMENT_ACTIVE).append(",");
			count ++;
		}
		if (CsfConstants.CSF_TRUE.equals(terminatedAddress)) {
			data.setParticipantOnlineAddressChanges(ContentConstants.PARTICIPANT_ONLINE_ADDRESS_CHANGES_TERMINATED);
			sf.append(CsfConstants.ADDRESS_MANAGEMENT_TERMINATED).append(",");;
			count ++;   
		}
		if (CsfConstants.CSF_TRUE.equals(retiredAddress)) {
			data.setParticipantOnlineAddressChanges(ContentConstants.PARTICIPANT_ONLINE_ADDRESS_CHANGES_RETIRED);
			sf.append(CsfConstants.ADDRESS_MANAGEMENT_RETIRED).append(",");;
			count ++;
		}
		if (CsfConstants.CSF_TRUE.equals(disabledAddress)) {
			data.setParticipantOnlineAddressChanges(ContentConstants.PARTICIPANT_ONLINE_ADDRESS_CHANGES_DISABLED);
			sf.append(CsfConstants.ADDRESS_MANAGEMENT_DISABLED).append(",");;
			count ++;
		}
		if (CsfConstants.CSF_TRUE.equals(activeAddress) && CsfConstants.CSF_TRUE.equals(terminatedAddress)
				&& CsfConstants.CSF_TRUE.equals(retiredAddress) && CsfConstants.CSF_TRUE.equals(disabledAddress)) {

			data.setParticipantOnlineAddressChanges(ContentConstants.PARTICIPANT_ONLINE_ADDRESS_CHANGES_ALLSELECTED);
		}else if (count > 1 && sf.lastIndexOf(",") == sf.length() - 1){ // if last char is "," remove it.
			sf.deleteCharAt(sf.length() - 1);
			data.setAddressParam(sf.toString());
			data.setParticipantOnlineAddressChanges(ContentConstants.PARTICIPANT_ONLINE_ADDRESS_CHANGES_MORETHAN_ONE);
			data.setMoreThanOneOptionForAddress(sf.toString());

		}

	}

	/**
	 * Method to determine Participants can specify deferral amounts as
	 * 
	 * @param data
	 * @param medCSF
	 */
	public static void getParticipantDeferrelAmtType(ParticipantServicesData data, ContractServiceFeature medCSF){

		String deferralType = null;
		if(medCSF != null) {
			if(medCSF.getAttributeValue(ServiceFeatureConstants.MD_DEFERRAL_TYPE) != null)
				deferralType = medCSF.getAttributeValue(ServiceFeatureConstants.MD_DEFERRAL_TYPE).trim();
		}
		if(deferralType != null){
			if(Constants.DEFERRAL_TYPE_PERCENT.equals(deferralType)){
				data.setParticipantDeferrelAmtType(ContentConstants.PARTICIPANT_SPECIFY_DEFERRAL_TYPE_PERCENTAGE);
			}else if(Constants.DEFERRAL_TYPE_DOLLAR.equals(deferralType)){
				data.setParticipantDeferrelAmtType(ContentConstants.PARTICIPANT_SPECIFY_DEFERRAL_TYPE_DOLLAR);
			}else {
				data.setParticipantDeferrelAmtType(ContentConstants.PARTICIPANT_SPECIFY_DEFERRAL_TYPE_EITHER);
			}
		}else{
			data.setParticipantDeferrelAmtType(ContentConstants.PARTICIPANT_SPECIFY_DEFERRAL_TYPE_NONE);
		}
	}

	/**
	 * Method to determine Participants are allowed to change their deferrals online – 
	 * First sentence and Secone sentence
	 * 
	 * @param data
	 * @param csfMap
	 * @param planDataLite
	 */
	@SuppressWarnings("unchecked")
	public static void getParticipantsAreAllowedToChangeTheirDeferralsOnline(ParticipantServicesData data,
			Map csfMap, PlanDataLite planDataLite){
		ContractServiceFeature medCSF = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.MANAGING_DEFERRALS);
		String onlineDeferrels = null;
		if(medCSF != null) {
			if(medCSF.getAttributeValue(ServiceFeatureConstants.MD_CHANGE_DEFERRALS_ONLINE) != null)
				onlineDeferrels = ContractServiceFeature.internalToBoolean(medCSF
						.getAttributeValue(ServiceFeatureConstants.MD_CHANGE_DEFERRALS_ONLINE)) ? CsfConstants.CSF_TRUE : CsfConstants.CSF_FALSE;
		}
		if (CsfConstants.CSF_TRUE.equals(onlineDeferrels)) {
			data.setParticipantDeferrelsOnline1(ContentConstants.PARTICIPANT_ALLOWED_CHANGE_DEFERRAL);
		}else{
			data.setParticipantDeferrelsOnline1(ContentConstants.PARTICIPANT_NOT_ALLOWED_CHANGE_DEFERRAL);
		}

		ContractServiceFeature aciCSF = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE);

		if(CsfConstants.CSF_TRUE.equals(onlineDeferrels)){
			if(ServiceFeatureConstants.YES.equals(planDataLite.getAciAllowed())){
				if(ServiceFeatureConstants.YES.equals(aciCSF.getValue())){
					data.setParticipantDeferrelsOnline2(ContentConstants.PARTICIPANT_ALLOW_CHANGE_DEFERRAL_AUTOMATIC_CONTRIBUTION_JHINCREASE);
				}else{
					data.setParticipantDeferrelsOnline2(ContentConstants.PARTICIPANT_CHANGE_DEFERRAL_AUTOMATIC_CONTRIBUTION_NOJHINCREASE);
				}
			}else if(ServiceFeatureConstants.NO.equals(planDataLite.getAciAllowed())){
				data.setParticipantDeferrelsOnline2(ContentConstants.PARTICIPANT_ALLOW_CHANGE_DEFERRAL_NOAUTOMATIC_CONTRIBUTION_JHINCREASE);
			}else{
				data.setParticipantDeferrelsOnline2(ContentConstants.PARTICIPANT_ALLOW_CHANGE_DEFERRAL_UNSPECIFIED);
			}
		}else{
			data.setParticipantDeferrelsOnline2(ContentConstants.PARTICIPANT_CANT_CHANGE_DEFERRAL_AUTOMATIC_CONTRIBUTION_NOJHINCREASE);
		}
	}

	/**
	 * Method to determine Participants are allowed to enrolled online
	 * 
	 * @param data
	 * @param medCSF
	 */
	public static void getParticipantsAreAllowedToEnrollOnline(ParticipantServicesData data,
			ContractServiceFeature medCSF){
		String enrollOnline = null;
		if(medCSF != null) {
			if(medCSF.getAttributeValue(ServiceFeatureConstants.MD_ENROLL_ONLINE) != null)
				enrollOnline = ContractServiceFeature.internalToBoolean(medCSF
						.getAttributeValue(ServiceFeatureConstants.MD_ENROLL_ONLINE)) ? CsfConstants.CSF_TRUE : CsfConstants.CSF_FALSE;
		}
		if (CsfConstants.CSF_TRUE.equals(enrollOnline)) {
			data.setParticipantsAreAllowedToEnrolledOnline(ContentConstants.PARTICIPANTS_ALLOWED_ENROLL_ONLINE);
		}else{
			data.setParticipantsAreAllowedToEnrolledOnline(ContentConstants.PARTICIPANTS_NOTALLOWED_ENROLL_ONLINE);
		}
	}

	/**
	 * Method to determine  Payroll Support Services -
	 * “Our standard service offering has been customized, you have requested” label
	 * 
	 * @param contractId
	 * @param data
	 * @param medCSF
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public static void determineAndDisplayStandardServices(int contractId, ParticipantServicesData data
			,Map csfMap) throws SystemException{
		
		String minPer = null;
		String maxPer = null;
		String minAmt = null;
		String maxAmt = null;
		
		String deferralType = null;
		String payrollCutOff = null;
		boolean isStandardServiceEligible  = false; 
		
		String autoSignup = ContractServiceDelegate.getInstance().determineSignUpMethod(contractId);
		ContractServiceFeature medCSF = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.MANAGING_DEFERRALS);
		ContractServiceFeature aciCSF = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE);
		
		if(medCSF != null) {
			if(medCSF.getAttributeValue(ServiceFeatureConstants.MD_DEFERRAL_TYPE) != null)
				deferralType = medCSF.getAttributeValue(ServiceFeatureConstants.MD_DEFERRAL_TYPE).trim();
			
			if(medCSF.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE) != null)
				payrollCutOff = medCSF.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE);
		}
		
		//2.	Default deferral scheduled increase – amount and maximum
		if(aciCSF != null) {
			if(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_AMOUNT) != null)
				minAmt = aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_AMOUNT).trim();
		
			if(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_PERCENT) != null)
				minPer = aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_PERCENT).trim();
		
			if(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_AMOUNT) != null)
				maxAmt = aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_AMOUNT).trim();
		
			if(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_PERCENT) != null)
				maxPer = aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_PERCENT).trim();
		}
		List<String> params = new ArrayList<String>();
		if(ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(autoSignup) ){
			if(Constants.DEFERRAL_TYPE_PERCENT.equals(deferralType) &&
				isDefaultPercentages(minPer, maxPer, params)){
					data.setDefaultDeferralScheduledIncreaseAmtAndMax(ContentConstants.ACI_SIGNUP_IN_PERCENTAGE);
					isStandardServiceEligible = true;
			}
				
			if(Constants.DEFERRAL_TYPE_DOLLAR.equals(deferralType) &&
				isDefaultAmounts(minAmt, maxAmt, params)){
					data.setDefaultDeferralScheduledIncreaseAmtAndMax(ContentConstants.ACI_SIGNUP_IN_AMOUNT);
					isStandardServiceEligible = true;
			}
				
			if(Constants.DEFERRAL_TYPE_EITHER.equals(deferralType)){
				boolean isDefualtPercentages = isDefaultPercentages(minPer, maxPer, params) ;
				boolean isDefualtAmounts = isDefaultAmounts(minAmt, maxAmt, params);
				if(isDefualtPercentages || isDefualtAmounts){
					isStandardServiceEligible = true;
					data.setDefaultDeferralScheduledIncreaseAmtAndMax(ContentConstants.ACI_SIGNUP_IN_BOTH);
				}
			}
		}
		
		if(ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(autoSignup) ){
			if(Constants.DEFERRAL_TYPE_DOLLAR.equals(deferralType) &&
					isDefaultAmounts(minAmt, maxAmt, params)){
				isStandardServiceEligible = true;
				data.setDefaultDeferralScheduledIncreaseAmtAndMax(ContentConstants.ACI_AUTO_IN_AMOUNT);
			}
				
			if(Constants.DEFERRAL_TYPE_EITHER.equals(deferralType) && 
					isDefaultAmounts(minAmt, maxAmt, params)){
				isStandardServiceEligible = true;
				data.setDefaultDeferralScheduledIncreaseAmtAndMax(ContentConstants.ACI_AUTO_IN_BOTH);
			}
				
		}
		data.setDeferrelSchedParamas(params);
		// 4.	Payroll cut off for online deferral and auto enrollment changes 
		//  10 is the default value
		String payrollCutOffDefaultValue = CsfDataHelper.getAttributeDefaultValue(
				ServiceFeatureConstants.MANAGING_DEFERRALS, ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE);
		
		if (StringUtils.isNotEmpty(payrollCutOff) 
				&& StringUtils.isNotEmpty(payrollCutOffDefaultValue)
				&& Long.parseLong(payrollCutOff) != Long.parseLong(payrollCutOffDefaultValue)) {
			data.setPayrollCutOffForOnlineDeferralAndAutoEnrollmentChanges(ContentConstants.PAYROLL_CUTOFF_FOR_ONLINE_DEFERRAL_AND_AUTO_ENROLLMENT_CHANGES);
			data.setPayrollParam(payrollCutOff);
			isStandardServiceEligible = true;
		}
		data.setDetermineOurStandardServiceEligible(isStandardServiceEligible);
	}

	/**
	 * Method to check the default defarral percentages and add to the param array, if not
	 * 
	 * @param minPer
	 * @param maxPer
	 * @param params
	 * @throws SystemException 
	 */
	private static boolean isDefaultPercentages(String minPer, String maxPer,
			List<String> params) throws SystemException {
		boolean defaultValues = false;

		String minPerDefaultValue = CsfDataHelper.getAttributeDefaultValue(
				ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_PERCENT);
		String maxPerDefaultValue = CsfDataHelper.getAttributeDefaultValue(
				ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_PERCENT);
		
		if(StringUtils.isNotEmpty(minPer) && StringUtils.isNotEmpty(minPerDefaultValue)){
			if(Long.parseLong(minPer) != Long.parseLong(minPerDefaultValue)){
				params.add(HTML_STRONG_BEGIN_TAG+minPer+HTML_STRONG_END_TAG);
				defaultValues = true;
			}else{
				params.add(minPer);
			}
		}
		
		if(StringUtils.isNotEmpty(maxPer) && StringUtils.isNotEmpty(maxPerDefaultValue)){
			if(Long.parseLong(maxPer) != Long.parseLong(maxPerDefaultValue)){
				params.add(HTML_STRONG_BEGIN_TAG+maxPer+HTML_STRONG_END_TAG);
				defaultValues = true;
			}else{
				params.add(maxPer);
			}
		}
		return defaultValues;
	}
	
	/**
	 * Method to check the default deferral amounts and add to the param array, if not
	 * 
	 * @param minAmt
	 * @param maxAmt
	 * @param params
	 * @throws SystemException 
	 */
	private static boolean isDefaultAmounts(String minAmt, String maxAmt,
			List<String> params) throws SystemException {
		
		boolean defaultValues = false;
		
		String minAmtDefaultValue = CsfDataHelper.getAttributeDefaultValue(
				ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_AMOUNT);
		String maxAmtDefaultValue = CsfDataHelper.getAttributeDefaultValue(
				ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_AMOUNT);
		
		if(StringUtils.isNotEmpty(minAmt) && StringUtils.isNotEmpty(minAmtDefaultValue)){
			if(Long.parseLong(minAmt) != Long.parseLong(minAmtDefaultValue)){
				params.add(HTML_STRONG_BEGIN_TAG+minAmt+HTML_STRONG_END_TAG);
				defaultValues = true;
			}else{
				params.add(minAmt);
			}
		}
		
		if(StringUtils.isNotEmpty(maxAmt) && StringUtils.isNotEmpty(maxAmtDefaultValue)){
			if(Long.parseLong(maxAmt) != Long.parseLong(maxAmtDefaultValue)){
				params.add(HTML_STRONG_BEGIN_TAG+maxAmt+HTML_STRONG_END_TAG);
				defaultValues = true;
			}else{
				params.add(maxAmt);
			}
		}
		
		return defaultValues;
	}

	/**
	 * Method to determine Financial Transactions sub-section
	 * 
	 * @param csfMap
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public static void setFinancialTransactionsSectionValues(int contractId,Map csfMap, ParticipantServicesData data )
	throws SystemException{
		// Participants can initiate Inter-account transfers online- Value from APOLLO
		if(ContractServiceDelegate.getInstance().isParticipantIATOnlineAvailable(contractId)){
			data.setParticipantsCanInitiateInterAccountTransfersOnline(
					ContentConstants.PARTICIPANTS_CAN_INITIATE_INTER_ACCOUNT_TRANSFERS_ONLINE_YES);
		}else{
			data.setParticipantsCanInitiateInterAccountTransfersOnline(
					ContentConstants.PARTICIPANTS_CAN_INITIATE_INTER_ACCOUNT_TRANSFERS_ONLINE_NO);
		}

		String iWithdrawalRequest = null;
		ContractServiceFeature pilCSF = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.PARTICIPANT_INITIATE_LOANS_FEATURE);
		ContractServiceFeature withdrawalCSF = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
		ContractServiceFeature loanCSF = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
		
		if(pilCSF != null) {
			if(ServiceFeatureConstants.YES.equals(pilCSF.getValue())){
				data.setParticipantsCanInitiateOnlineLoanRequests(ContentConstants.PARTICIPANTS_CAN_INITIATE_ONLINE_LOAN_REQUESTS_YES);
			}else{
				data.setParticipantsCanInitiateOnlineLoanRequests(ContentConstants.PARTICIPANTS_CAN_INITIATE_ONLINE_LOAN_REQUESTS_NO);
			}
		}
		
		if(loanCSF != null){
			if(ServiceFeatureConstants.YES.equals(loanCSF.getValue())){
				data.setLoansAllowed(true);
			}else{
				data.setLoansAllowed(false);
			}
		}
		//Participants can initiate online loan requests
		data.setJHdoesLoanRK(ContractServiceDelegate.getInstance().hasContractWithContractProductFeature(contractId,CsfConstants.CONTRACT_PRODUCT_FEATURE_LRK01));
		//Participants can initiate withdrawal requests
		if(withdrawalCSF != null) {
			if(withdrawalCSF.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_PARTICIPANT) != null)
				iWithdrawalRequest = ContractServiceFeature.internalToBoolean(withdrawalCSF
						.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_PARTICIPANT)) ? CsfConstants.CSF_TRUE : CsfConstants.CSF_FALSE;
		}
		if (CsfConstants.CSF_TRUE.equals(iWithdrawalRequest)) {
			data.setParticipantsCanInitiateWithdrawalRequests(ContentConstants.PARTICIPANTS_CAN_INITIATE_WITHDRAWAL_REQUESTS_YES);
		}else{
			data.setParticipantsCanInitiateWithdrawalRequests(ContentConstants.PARTICIPANTS_CAN_INITIATE_WITHDRAWAL_REQUESTS_NO);
		}
	}
	
	/**
	 * Method to determine Beneficiary Information sub-section
	 * 
	 * @param contractId
	 * @param csfMap
	 * @param data
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public static void setBeneficiaryInformationSectionValues(Map csfMap, ParticipantServicesData data) 
	throws SystemException{
		ContractServiceFeature obdCSF = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.ONLINE_BENEFICIARY_DESIGNATION);
		
		if(obdCSF != null) {
			data.setOnlineBeneficiaryDesignationAllowed(ContractServiceFeature
					.internalToBoolean(obdCSF.getValue())
					.booleanValue() ? true : false);
		} else {
			data.setOnlineBeneficiaryDesignationAllowed(false);
		}
	}
}
