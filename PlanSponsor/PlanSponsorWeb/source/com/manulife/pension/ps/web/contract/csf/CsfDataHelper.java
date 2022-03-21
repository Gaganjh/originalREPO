package com.manulife.pension.ps.web.contract.csf;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.delegate.PartyServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.event.exception.UnrecoverableEventException;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ServiceFeatureLookupException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.contract.csf.util.CsfUtil;
import com.manulife.pension.ps.web.contract.csf.util.ParticipantServicesSectionUtil;
import com.manulife.pension.ps.web.contract.csf.util.PlanSponsorServicesSectionUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.managedaccount.ManagedAccountServiceFeatureLite;
import com.manulife.pension.service.contract.util.ContractServiceFeatureUtil;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.util.ServiceFeatureLookup;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.contract.valueobject.ParticipantACIFeatureUpdateVO;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.party.valueobject.BusinessParameterValueObject;
import com.manulife.pension.service.plan.valueobject.MoneyTypeEligibilityCriterion;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.DefaultRolePermissions;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.security.valueobject.UserSearchCriteria;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * New Class to load/save the CSF values from/to the Database and to 
 * determine the page/user permissions for CSF pages. 
 * 
 * @author Puttaiah Arugunta
 *
 */
public class CsfDataHelper {
	
	private static final String MM_DD_YYYY = "MM/dd/yyyy";
	private static final String className = CsfDataHelper.class.getName();
	
	private static BaseEnvironment baseEnvironment= new BaseEnvironment();  
	
	private static Logger logger = Logger.getLogger(className);
	
	 private static CsfDataHelper localObject = null;
	 
	 private static final int DUMMY_YEAR = 1970;
	
	/**
     * Return an instance of the CsfDataValidator using the a Singleton pattern
     * 
     * @param datasource
     * @return ContractServiceDelegate
     */
    public static CsfDataHelper getInstance() {
        if (localObject == null) {
        	localObject = new CsfDataHelper();
        }
        return localObject;
    }
	/**
	 * Load the CsfForm
	 *
	 * @param contractId
	 * @param csfForm
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public void loadContractServiceFeatureData(Contract contract, Map csfMap,
			CsfForm csfForm, PlanDataLite planDataLite) throws SystemException {

		 int contractId = contract.getContractNumber();
		 
		// Participant online address changes are permitted for
		ContractServiceFeature addressManagementCSF = 
			(ContractServiceFeature) csfMap.get(ServiceFeatureConstants.ADDRESS_MANAGEMENT_FEATURE);
		
		String addressChanges[] = new String[4];
		int i = 0;
	
		if (addressManagementCSF != null) {
			
			if(ContractServiceFeature.internalToBoolean(addressManagementCSF
					.getAttributeValue(ServiceFeatureConstants.ADDRESS_MANAGEMENT_ACTIVE))){
				addressChanges[i++] = ServiceFeatureConstants.ADDRESS_MANAGEMENT_ACTIVE;
			}
			
			if(ContractServiceFeature.internalToBoolean(addressManagementCSF
					.getAttributeValue(ServiceFeatureConstants.ADDRESS_MANAGEMENT_TERMINATED))){
				addressChanges[i++] = ServiceFeatureConstants.ADDRESS_MANAGEMENT_TERMINATED;
			}
			
			if(ContractServiceFeature.internalToBoolean(addressManagementCSF
					.getAttributeValue(ServiceFeatureConstants.ADDRESS_MANAGEMENT_RETIRED))){
				addressChanges[i++] =ServiceFeatureConstants.ADDRESS_MANAGEMENT_RETIRED;
			}
			
			if(ContractServiceFeature.internalToBoolean(addressManagementCSF
					.getAttributeValue(ServiceFeatureConstants.ADDRESS_MANAGEMENT_DISABLED))){
				addressChanges[i++] = ServiceFeatureConstants.ADDRESS_MANAGEMENT_DISABLED;
			}
		}
		csfForm.setAddressChanges(addressChanges);
		
		//Management Deferrals and Online Enrollment
		ContractServiceFeature mdCSF = 
			(ContractServiceFeature) csfMap.get(ServiceFeatureConstants.MANAGING_DEFERRALS);

		if(mdCSF != null) {
			
			//Participants can specify deferral amounts as
			if(mdCSF.getAttributeValue(ServiceFeatureConstants.MD_DEFERRAL_TYPE) != null) {
				csfForm.setDeferralType(mdCSF.getAttributeValue(
						ServiceFeatureConstants.MD_DEFERRAL_TYPE).trim());
			} else {
				csfForm.setDeferralType(CsfConstants.EMPTY_STRING);
			}
			
			//Participants are allowed to change their deferrals online
			csfForm.setChangeDeferralsOnline(ContractServiceFeature.internalToBoolean(
					mdCSF.getAttributeValue(ServiceFeatureConstants.MD_CHANGE_DEFERRALS_ONLINE)).booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
			
			//Participants are allowed to enroll online
			csfForm.setEnrollOnline(ContractServiceFeature.internalToBoolean(mdCSF
					.getAttributeValue(ServiceFeatureConstants.MD_ENROLL_ONLINE)) ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
			
			//Payroll cut off for online deferral and auto enrollment changes
			csfForm.setPayrollCutoff(mdCSF.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE));
		}		

		// Auto Contribution values
		ContractServiceFeature aciCSF = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE);
		
		if(aciCSF != null){
			//Default scheduled deferral increase – $
			if(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_AMOUNT) != null) {
				csfForm.setDeferralLimitDollars(formatAmountForDisplay(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_AMOUNT).trim()));
			} else {
				csfForm.setDeferralLimitDollars(CsfConstants.EMPTY_STRING);
			}
			
			//Default scheduled deferral increase – maximum $
			if(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_AMOUNT) != null) {
				csfForm.setDeferralMaxLimitDollars(formatAmountForDisplay(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_AMOUNT).trim()));
			} else {
				csfForm.setDeferralMaxLimitDollars(CsfConstants.EMPTY_STRING);
			}
			
			//CSF.166	Default scheduled deferral increase display rules when "Auto" is being used
//			if(ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(csfForm.getAciSignupMethod())){
				
				//Default scheduled deferral increase – %
				if(planDataLite.getAciDefaultIncreasePercent() != null) {
					csfForm.setPlanDeferralLimitPercent(String.valueOf(planDataLite.getAciDefaultIncreasePercent().intValue()));
				} else {
					csfForm.setPlanDeferralLimitPercent(CsfConstants.EMPTY_STRING);
				}
				
				//Default scheduled deferral increase – maximum %
				if(planDataLite.getAciDefaultAutoIncreaseMaxPercent() != null) {
					csfForm.setPlanDeferralMaxLimitPercent(String.valueOf(planDataLite.getAciDefaultAutoIncreaseMaxPercent().intValue()));
				} else {
					csfForm.setPlanDeferralMaxLimitPercent(CsfConstants.EMPTY_STRING);
				}
				
//			} else {
				// Default scheduled deferral increase – %
				if(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_PERCENT) != null) {
					csfForm.setDeferralLimitPercent(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_PERCENT).trim());
				} else {
					csfForm.setDeferralLimitPercent(CsfConstants.EMPTY_STRING);
				}
				
				//Default scheduled deferral increase – maximum %
				if(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_PERCENT) != null) {
					csfForm.setDeferralMaxLimitPercent(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_PERCENT).trim());
				} else {
					csfForm.setDeferralMaxLimitPercent(CsfConstants.EMPTY_STRING);
				}
//			}
			
			//Turn on JH EZincrease
			if(aciCSF.getValue() != null) {
				csfForm.setAutoContributionIncrease(ContractServiceFeature
						.internalToBoolean(aciCSF.getValue())
						.booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
			} else {
				csfForm.setAutoContributionIncrease(CsfConstants.CSF_NO);
			}
			
			// Getting the value for (mm/dd) field from Plan Tables
			DayOfYear aciAnnualApplyDate = planDataLite.getAciAnnualApplyDate(); 
			if(aciAnnualApplyDate != null){
				csfForm.setAciAnniversaryDate(aciAnnualApplyDate.getData());
			}
			
			//First scheduled increase starts on [yyyy]
			if(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_ANNIVERSARY_DATE) != null) {
				csfForm.setAciAnniversaryYear(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_ANNIVERSARY_DATE));
			} else {
				csfForm.setAciAnniversaryYear(CsfConstants.EMPTY_STRING);
			}
			
			//Initial increase to take effect on first anniversary date after the enrollment
			if(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_INCREASE_NEW_ENROLLES_ANNIVERSARY) != null){
				csfForm.setIncreaseAnniversary(aciCSF.getAttributeValue(ServiceFeatureConstants.ACI_INCREASE_NEW_ENROLLES_ANNIVERSARY));
			} else {
				csfForm.setIncreaseAnniversary(CsfConstants.EMPTY_STRING);
			}
		}
		
		// Participants can initiate online loans
		ContractServiceFeature participantInitiateloansCSF = 
			(ContractServiceFeature) csfMap.get(ServiceFeatureConstants.PARTICIPANT_INITIATE_LOANS_FEATURE);

		if (participantInitiateloansCSF != null) {
			csfForm.setParticipantInitiateLoansInd(ContractServiceFeature
					.internalToBoolean(participantInitiateloansCSF.getValue())
					.booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
		}

		// Online Beneficiary Service values
		ContractServiceFeature obdCSF = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.ONLINE_BENEFICIARY_DESIGNATION);
		
		if(obdCSF != null) {
			csfForm.setOnlineBeneficiaryInd(ContractServiceFeature
					.internalToBoolean(obdCSF.getValue())
					.booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
		}
		
		// Plan Highlights are created by John Hancock
		ContractServiceFeature summaryPlanHighlightAvailable = (ContractServiceFeature) csfMap
				.get(ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_AVAILABLE);

		if (summaryPlanHighlightAvailable != null) {
			boolean value = ContractServiceFeature.internalToBoolean(
					summaryPlanHighlightAvailable.getValue()).booleanValue();
			csfForm.setSummaryPlanHighlightAvailable(value ? CsfConstants.CSF_YES
					: CsfConstants.CSF_NO);
			
			String summaryPlanHighlightReviewed = summaryPlanHighlightAvailable.getAttributeValue(ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_REVIEWDED);
			
			if (summaryPlanHighlightReviewed != null) {
				csfForm.setSummaryPlanHighlightReviewed(ContractServiceFeature.internalToBoolean(summaryPlanHighlightReviewed)
						.booleanValue() ? CsfConstants.CSF_YES
						: CsfConstants.CSF_NO);
			}   else {
				csfForm.setSummaryPlanHighlightReviewed(CsfConstants.CSF_NO);
			}
		}
		
		//Notice Generation service - To load the CSF page with selected values
		ContractServiceFeature noticeGenerationService = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_CD);
		if(noticeGenerationService !=null){
		    if(noticeGenerationService.getValue()!=null && noticeGenerationService.getValue()!=""){
		        boolean value = ContractServiceFeature.internalToBoolean(noticeGenerationService.getValue()).booleanValue();
		        if(value){
		            csfForm.setNoticeServiceInd(CsfConstants.CSF_YES);
		        }
		        else{
		            csfForm.setNoticeServiceInd(CsfConstants.CSF_NO);
		        }
		        String noticeOption = noticeGenerationService.getAttributeValue(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_OPTCD);
		        if(noticeOption!=null){
		            csfForm.setNoticeTypeSelected(noticeOption);
		        }
		        else{
		        	csfForm.setNoticeTypeSelected(null);
		        }
		    } 
		    else{
		        csfForm.setNoticeServiceInd(CsfConstants.CSF_NO);
		    }
		}
		else{
		    csfForm.setNoticeServiceInd(CsfConstants.CSF_NO);
		}
		
		// Turn on Eligibility Calculation Service
		ContractServiceDelegate service = ContractServiceDelegate.getInstance();
		
		ContractServiceFeature eligibilityCalculationCSF = (ContractServiceFeature) csfMap
				.get(ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);
		if (eligibilityCalculationCSF != null) {
			if (ServiceFeatureConstants.YES.equals(eligibilityCalculationCSF.getValue())) {
				csfForm.setEligibilityCalculationInd(CsfConstants.CSF_YES);
			} else if (ServiceFeatureConstants.NO.equals(eligibilityCalculationCSF.getValue())) {
				csfForm.setEligibilityCalculationInd(CsfConstants.CSF_NO);
			}
           
			//CSF 352[d]- Compare the contract money types with all the money types available for
			//Eligibility Calculation service feature
			List moneyTypeEligibilityCriteria=service.loadMoneyTypeEligibilityCriteria(contractId);
			csfForm.setMoneyTypeEligibilityCriteria(moneyTypeEligibilityCriteria);
			getEligibilityServiceMoneyTypes(csfForm,contractId,csfMap);

			List<MoneyTypeVO> contractMoneyTypes = service.getContractMoneyTypes(contractId, true);
			csfForm.setContractMoneyTypesList(contractMoneyTypes);

			Map<String,Date> moneyTypesWithFutureDatedDeletetionDate = service.getContractMoneyTypeIdsWithFutureDatedDeletionDate(contractId);
			csfForm.setMoneyTypesWithFutureDatedDeletionDate(moneyTypesWithFutureDatedDeletetionDate);

			//CSF 350 - The Eligibility Calculation service feature sub-group will be displayed for contracts that
			//do not have the 'RA457' product.
			boolean showEligibilitySection=true;
			if(contract.getProductId().equalsIgnoreCase(Constants.PRODUCT_RA457)){
				showEligibilitySection= false;
			}
			//whether to show/hide eligibility section based on business validations
			csfForm.setShowEligibilitySection(showEligibilitySection);
		}
		
		// Auto Enrollment values
		ContractServiceFeature autoEnrollCSF = 
			(ContractServiceFeature) csfMap.get(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
		
		if (autoEnrollCSF != null) {
			//Turn on JH EZstart
			csfForm.setAutoEnrollInd(ContractServiceFeature.internalToBoolean(autoEnrollCSF.getValue()).booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);

			//Initial enrollment date for the EZstart service is
			String initialEnrollmentStr = autoEnrollCSF.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_INITIAL_ENROLLMENT_DATE);
			csfForm.setInitialEnrollmentDate(CsfUtil.convertDateFormat(initialEnrollmentStr,
					CsfConstants.aciDBDateFormat, CsfConstants.aciDisplayDateFormat));
		}
		// Direct Mail of enrollment materials
		ContractServiceFeature directMailCSF = 
			(ContractServiceFeature) csfMap.get(ServiceFeatureConstants.DIRECT_MAIL_FEATURE);
		
		if (directMailCSF != null) {
			csfForm.setDirectMailInd(ContractServiceFeature.internalToBoolean(directMailCSF.getValue()).booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
		}  
		
		//Vesting will be
		ContractServiceFeature planDataCSF = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
		if (planDataCSF != null) {
			csfForm.setVestingPercentagesMethod(planDataCSF.getValue());
			
			//Reporting vesting percentages on participant statements
			csfForm.setVestingDataOnStatement(ContractServiceFeature.internalToBoolean(planDataCSF.getAttributeValue(ServiceFeatureConstants.VESTING_PERCENTAGE_ON_STATEMENT)) ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
		}

		if (csfForm.getVestingPercentagesMethod() == null) {
			csfForm.setVestingPercentagesMethod(CsfConstants.PLAN_VESTING_CALCULATION);
		}
		
		// Payroll Frequency
		ContractServiceFeature payrollFreqCSF = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.PAYROLL_FREQUENCY);
		if (payrollFreqCSF != null) {
			csfForm.setPlanFrequency(payrollFreqCSF.getValue());
		}else{
			csfForm.setPlanFrequency(CsfConstants.PAYROLL_FREQUENCY_UNSPECIFIED_CODE);
		}

		// Allow Payroll Path submissions
		ContractServiceFeature autoPayrollCSF = 
			(ContractServiceFeature) csfMap.get(ServiceFeatureConstants.PAYROLL_PATH_FEATURE);
		
		if (autoPayrollCSF != null) {
			csfForm.setAutoPayrollInd(ContractServiceFeature.internalToBoolean(autoPayrollCSF.getValue()).booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
		}

		// Consent to deliver the Contract and any subsequent amendments to the contract via plan sponsor website
		ContractServiceFeature consentCSF = 
			(ContractServiceFeature) csfMap.get(ServiceFeatureConstants.CONSENT_SERVICE_FEATURE);
		
		if (consentCSF != null) {
			if (ServiceFeatureConstants.YES.equals(consentCSF.getValue())) {
				csfForm.setConsentInd(CsfConstants.CSF_YES);
			} else if (ServiceFeatureConstants.NO.equals(consentCSF.getValue())) {
				csfForm.setConsentInd(CsfConstants.CSF_NO);
			} else if (ServiceFeatureConstants.NA.equals(consentCSF.getValue())) {
				csfForm.setConsentInd(CsfConstants.CONSENT_NA);
			} else {
				csfForm.setConsentInd(CsfConstants.CONSENT_BLANK);
			}
		}
		
		// check if the contract has TPA firm assigned
		TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(contractId);
		if (firmInfo != null) {
			csfForm.setTpaFirmExists(true);
		} else {
			csfForm.setTpaFirmExists(false);
		}
		
		// Set withdrawals values
		ContractServiceFeature withdrawalsCSF = 
			(ContractServiceFeature) csfMap.get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
		
		if (withdrawalsCSF != null) {
			csfForm.setWithdrawalInd(ContractServiceFeature.internalToBoolean(withdrawalsCSF.getValue()).booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
			csfForm.setCreatorMayApproveInd(ContractServiceFeature.internalToBoolean(withdrawalsCSF.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_APPROVE)).booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
			csfForm.setOnlineWithdrawalProcess(ContractServiceFeature.internalToBoolean(withdrawalsCSF.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_REVIEW)).booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
			csfForm.setSpecialTaxNotice(ContractServiceFeature.internalToBoolean(withdrawalsCSF.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_SPECIAL_TAX)).booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
			csfForm.setChecksMailedTo(withdrawalsCSF.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_MAILED_TO));
			csfForm.setParticipantWithdrawalInd(ContractServiceFeature.internalToBoolean(withdrawalsCSF.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_PARTICIPANT)).booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
			String whoWillReviewValue = withdrawalsCSF.getAttributeValue(ServiceFeatureConstants.WHO_WILL_REVIEW_WITHDRAWALS);
			// If the whoWillReviewValue is none, this may be new contract
			// so setting the value to TPA as per the DFS defaults
			if (firmInfo == null) {
				if (CsfConstants.WHO_WILL_REVIEW_WD_NOREVIEW.equals(whoWillReviewValue)) {
					csfForm.setWhoWillReviewWithdrawals(CsfConstants.WHO_WILL_REVIEW_WD_NOREVIEW);
				} else {
					csfForm.setWhoWillReviewWithdrawals(CsfConstants.WHO_WILL_REVIEW_WD_PS);
				}
			} else {
				csfForm.setWhoWillReviewWithdrawals(whoWillReviewValue);
			}
		} else if (withdrawalsCSF == null && firmInfo == null) {
			csfForm.setWhoWillReviewWithdrawals(CsfConstants.WHO_WILL_REVIEW_WD_PS);
		}

		// New logic to determine the Auto/Sign up method
		csfForm.setAciSignupMethod(ContractServiceDelegate.getInstance().determineSignUpMethod( contractId));
		
		csfForm.setPlanAciInd(planDataLite.getAciAllowed());
		
		csfForm.setIsFreezePeriod(isFreezePeriod(csfForm, planDataLite));
		
		if(Contract.STATUS_ACTIVE_CONTRACT.equals(contract.getStatus()) || Contract.STATUS_CONTRACT_FROZEN.equals(contract.getStatus())){
			csfForm.setDeferralEZiDisabled(isDeferralsEziValidForUpdate(csfForm, planDataLite));
		}
		
		ContractServiceFeature loansCSF = 
			(ContractServiceFeature) csfMap.get(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);

		if(loansCSF != null){
			csfForm.setAllowOnlineLoans(ContractServiceFeature
					.internalToBoolean(loansCSF.getValue())
					.booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
			if(!hasUOLCount(contract.getContractNumber())){
				if(isLoanCsfOn(contract) && 
						ContractServiceDelegate.getInstance().hasContractWithContractProductFeature(contractId,CsfConstants.CONTRACT_PRODUCT_FEATURE_LRK01)){	
					csfForm.setAllowOnlineLoans(CsfConstants.CSF_YES);
				} else {
					csfForm.setAllowOnlineLoans(CsfConstants.CSF_NO);
				}
			}

			csfForm
			.setCreatorMayApproveLoanRequestsInd(ContractServiceFeature
					.internalToBoolean(
							loansCSF
							.getAttributeValue(ServiceFeatureConstants.CREATOR_OF_LOANREQUEST_APPROVE))
							.booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
			csfForm
			.setAllowLoansPackageToGenerate(ContractServiceFeature
					.internalToBoolean(
							loansCSF
							.getAttributeValue(ServiceFeatureConstants.ALLOW_LOANS_PACKAGE_TO_GENERATE))
							.booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
			
			if (firmInfo == null) {
				csfForm.setWhoWillReviewLoanRequests(CsfConstants.WHO_WILL_REVIEW_WD_PS); 
			} else {
				csfForm.setWhoWillReviewLoanRequests(loansCSF.getAttributeValue(ServiceFeatureConstants.WHO_WILL_REVIEW_LOANS));
			}
			
			csfForm.setOutstandingOldILoanRequestCount(getOutstandingOldILoanRequestCount(contractId));
			csfForm.setLoansChecksMailedTo(loansCSF.getAttributeValue(ServiceFeatureConstants.LOANS_CHECKS_MAILED_TO));

		} else if (loansCSF == null && firmInfo == null) {
			csfForm.setWhoWillReviewLoanRequests(CsfConstants.WHO_WILL_REVIEW_WD_PS);
		}

		loadFinancialTransData(contractId,csfMap,csfForm);
		
		//loadCMAdata(csfMap,csfForm,contractId, planDataLite);
		
		// Payroll Feedback Service
		csfForm.setPayrollFeedbackService(CsfDataHelper.getPayrollFeedbackServiceDropdownEffectiveValue(csfMap));
		
		setEdeliveryForPlanNotices(csfMap, csfForm);
		
		setManagedAccountSectionValues(csfMap, csfForm, contractId, planDataLite);
		
	}

	public void setEdeliveryForPlanNotices(Map csfMap, CsfForm csfForm) throws SystemException {
		// edelivery for plan notices and statements
		csfForm.setShowNoticesEdeliveryInEditPage(Boolean.valueOf(isNoticesEdeliveryEditOrConfirmEnabled()));
		csfForm.setShowNoticesEdeliveryInViewPage(Boolean.valueOf(isNoticesEdeliveryViewEnabled()));
		
		ContractServiceFeature eDeliveryPlanNotices = (ContractServiceFeature) csfMap
				.get(ServiceFeatureConstants.EDELIVERY_PLAN_NOTICES_STATEMENTS);
		if (null != eDeliveryPlanNotices) {
			csfForm.setWiredAtWork(ContractServiceFeature
					.internalToBoolean(
							eDeliveryPlanNotices.getAttributeValue(ServiceFeatureConstants.WIRED_AT_WORK_CODE))
					.booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);

			csfForm.setNoticeOfInternetAvailability(
					ContractServiceFeature
					.internalToBoolean(eDeliveryPlanNotices
							.getAttributeValue(ServiceFeatureConstants.NOTICE_OF_INTERNET_AVAILABILITY))
					.booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
		} else {
			csfForm.setWiredAtWork(ContractServiceFeature.internalToBoolean(
					getAttributeDefaultValue(ServiceFeatureConstants.EDELIVERY_PLAN_NOTICES_STATEMENTS,
							ServiceFeatureConstants.WIRED_AT_WORK_CODE))
					.booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
			csfForm.setNoticeOfInternetAvailability(ContractServiceFeature.internalToBoolean(
					getAttributeDefaultValue(ServiceFeatureConstants.EDELIVERY_PLAN_NOTICES_STATEMENTS,
							ServiceFeatureConstants.NOTICE_OF_INTERNET_AVAILABILITY))
					.booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);

		}
	}

	private static String getPayrollFeedbackServiceDropdownEffectiveValue(Map<String, ContractServiceFeature> csfMap) {
		ContractServiceFeature payrollFeedbackCsf = csfMap.get(ServiceFeatureConstants.PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE);
		
		if(payrollFeedbackCsf != null) {
			if(ServiceFeatureConstants.YES.equals(payrollFeedbackCsf.getValue())) {
				if(ServiceFeatureConstants.YES.equals(payrollFeedbackCsf.getAttributeValue(ServiceFeatureConstants.PAYROLL_FEEDBACK_360_SERVICE_ATTRIBUTE_CODE))) {
					return ServiceFeatureConstants.PAYROLL_FEEDBACK_360_SERVICE_ATTRIBUTE_CODE;
				}
				return ServiceFeatureConstants.PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE;
			}
		}

		return ServiceFeatureConstants.NOT_AVAILABLE;
	}

	/**
	 * Method to load the Financial Transactions data
	 * 
	 * @param csfForm
	 */
	@SuppressWarnings("unchecked")
	private void loadFinancialTransData(int contractId, Map csfMap, CsfForm csfForm) throws SystemException{
		ContractServiceFeature pilCSF = 
			(ContractServiceFeature) csfMap.get(ServiceFeatureConstants.PARTICIPANT_INITIATE_LOANS_FEATURE);
		ContractServiceFeature withdrawalsCSF = 
			(ContractServiceFeature) csfMap.get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
		
		csfForm.setParticipantInitiateLoansInd(ContractServiceFeature.internalToBoolean(
				pilCSF.getValue()).booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
		
		csfForm.setParticipantWithdrawalInd(ContractServiceFeature.internalToBoolean(
				withdrawalsCSF.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_PARTICIPANT))
						.booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
		
		if(ContractServiceDelegate.getInstance().isParticipantIATOnlineAvailable(contractId)){
			csfForm.setParticiapntIATs(CsfConstants.CSF_YES);
		}else{
			csfForm.setParticiapntIATs(CsfConstants.CSF_NO);
		}
	}
	
	/**
	 * Method to load the Values from the CMA depends on the CSF values in the DB.
	 * 
	 * @param csfMap
	 * @param csfForm
	 * @param contractId
	 * @param planDataLite
	 * @throws SystemException
	 * @throws RemoteException 
	 * @throws ApplicationException 
	 */
	@SuppressWarnings("unchecked")
	public void loadCMAdata(Map csfMap, CsfForm csfForm, int contractId, PlanDataLite planDataLite)
	throws SystemException{
		
		ParticipantServicesData participantData = new ParticipantServicesData();
		
		ContractServiceFeature mdCSF = 
			(ContractServiceFeature) csfMap.get(ServiceFeatureConstants.MANAGING_DEFERRALS);
		
		ParticipantServicesSectionUtil.getAddressManagementView(participantData,csfMap);
		ParticipantServicesSectionUtil.getParticipantDeferrelAmtType(participantData, mdCSF);
		ParticipantServicesSectionUtil.getParticipantsAreAllowedToChangeTheirDeferralsOnline(participantData, csfMap, planDataLite);
		ParticipantServicesSectionUtil.getParticipantsAreAllowedToEnrollOnline(participantData, mdCSF);
		ParticipantServicesSectionUtil.determineAndDisplayStandardServices(contractId, participantData, csfMap);
		ParticipantServicesSectionUtil.setFinancialTransactionsSectionValues(contractId, csfMap, participantData);
		ParticipantServicesSectionUtil.setBeneficiaryInformationSectionValues(csfMap, participantData);
		csfForm.setParticipantServicesData(participantData);
		
		PlanSponsorServicesData planSponsorData = new PlanSponsorServicesData();
		
		PlanSponsorServicesSectionUtil.determinePlanHighlights(csfMap, planDataLite, planSponsorData);
		PlanSponsorServicesSectionUtil.determineNoticeGeneration(csfMap, planDataLite, planSponsorData);
		PlanSponsorServicesSectionUtil.determineEligibilityCalculationService(csfMap, planSponsorData, contractId);
		PlanSponsorServicesSectionUtil.determineJHEZstart(csfMap, planSponsorData);
		PlanSponsorServicesSectionUtil.determineJHEZIncrease(csfMap, planSponsorData);
		PlanSponsorServicesSectionUtil.determineVestingSchedules(csfMap, planSponsorData);
		PlanSponsorServicesSectionUtil.determineElectronicTransactions(csfMap, planSponsorData);
		PlanSponsorServicesSectionUtil.determineOnlineLoans(csfMap, planSponsorData);
		PlanSponsorServicesSectionUtil.determineIWithdrawals(csfMap, planSponsorData);

		PlanSponsorServicesSectionUtil.determineIps(contractId, planSponsorData);
		PlanSponsorServicesSectionUtil.determinePayrollFeedbackService(csfMap, planSponsorData);

		csfForm.setPlanSponsorServicesData(planSponsorData);
	}
	
	/**
	 * Validates the contract service feature data.
	 * 
	 * Boolean variable "isConfirmed" is used to determine whether we need to update 
	 * the user withdrawals/loan permissions or not. This will be very much useful, 
	 * when user does not want to save the CSF changes or going out from CSF page 
	 * without using any of the CSF buttons.  
	 * 
	 * @param csfForm
	 * @param request
	 * @param changedCsfCollection
	 * @param errors
	 * @param warnings
	 * @throws SystemException
	 * @throws SecurityServiceException
	 */
	@SuppressWarnings("unchecked")
	public void validateOtherContractServiceFeatureData(CsfForm csfForm, 
			HttpServletRequest request, Collection<ContractServiceFeature> changedCsfCollection,
			Collection<GenericException> errors, Collection<GenericException> warnings, boolean isConfirmed)
	throws SystemException, SecurityServiceException {
		
		UserProfile userProfile = getUserProfile(request);

		// withdrawalCSF changes
		// Need to determine if there are any updates to the TPAFirm
		TPAFirmInfo firmInfo = null;
		boolean lockFirm = false;
		boolean checkIfRequiredToLocktheTPAFirm = 
			checkIfRequiredToLocktheTPAFirm(csfForm, changedCsfCollection);

		if (checkIfRequiredToLocktheTPAFirm) {
			// obtain the TPA firm for the contract
			firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(
					userProfile.getCurrentContract().getContractNumber());

			if (firmInfo != null) {
				lockFirm = LockServiceDelegate.getInstance().lock(
						LockHelper.TPA_FIRM_LOCK_NAME,
						LockHelper.TPA_FIRM_LOCK_NAME + firmInfo.getId(),
						userProfile.getPrincipal().getProfileId());
			}
		}
		
		// if the firm is already locked then its a hard stop
		if ((firmInfo != null) && checkIfRequiredToLocktheTPAFirm && !lockFirm) {
			try {
				Lock lockInfo = LockServiceDelegate.getInstance().getLockInfo(
						LockHelper.TPA_FIRM_LOCK_NAME,
						LockHelper.TPA_FIRM_LOCK_NAME + firmInfo.getId());

				UserInfo lockOwnerUserInfo = SecurityServiceDelegate.getInstance().searchByProfileId(
						userProfile.getPrincipal(), lockInfo.getLockUserProfileId());

				String lockOwnerDisplayName = LockHelper.getLockOwnerDisplayName(
						userProfile, lockOwnerUserInfo);

				// setting the validation error
				errors.add(new GenericException(ErrorCodes.ERROR_TPA_PROFILE_LOCKED, 
						new String[] { lockOwnerDisplayName }));
			} catch (SecurityServiceException e) {
				throw new SystemException("Exception occured in " 
						+ "CsfDataHelper.validateOtherContractServiceFeatureData()  "
						+ "Failed to get the lock on TPA firm :"
						+ e.toString());
			}
		}

		handleWithdrawals(request, csfForm, (CsfForm) csfForm.getClonedForm(), 
				changedCsfCollection,  warnings, isConfirmed);
		
		if ((firmInfo != null) && checkIfRequiredToLocktheTPAFirm && lockFirm) {
			if(isConfirmed){
				// once the firm is locked handle the permissions for the firm
				handleTPAFirmPermissionsForWithdrawals(request, csfForm, firmInfo);
				handleTPAFirmPermissionsForLoans(request, csfForm, firmInfo);
			}
			// remove the tpa firm lock
			if (lockFirm) {
				LockServiceDelegate.getInstance().releaseLock(
						LockHelper.TPA_FIRM_LOCK_NAME,
						LockHelper.TPA_FIRM_LOCK_NAME + firmInfo.getId());
			}
		}
		
		handleLoanPermissions(request, csfForm, changedCsfCollection, firmInfo, warnings, isConfirmed);

		// Update the service features for the contract in session
		Map<String, ContractServiceFeature> serviceFeatureMap = 
			getUserProfile(request).getCurrentContract().getServiceFeatureMap();
		
		for (ContractServiceFeature changedServiceFeature : changedCsfCollection) {
			ContractServiceFeature serviceFeature = serviceFeatureMap.get(changedServiceFeature.getName());
			
			if(serviceFeature!=null){
				if (changedServiceFeature.getValue() != null) {
					serviceFeature.setValue(changedServiceFeature.getValue());
				}
				
				Iterator attrItr = changedServiceFeature.getAttributeNames().iterator();
				while (attrItr.hasNext()) {
					String attrName = (String) attrItr.next();
					serviceFeature.addAttribute(attrName, 
							changedServiceFeature.getAttributeValue(attrName));
				}
			}			
		}
	}
	
	/**
	 * Create a collection of ContractServiceFeatures that have changed on the
	 * Form
	 *
	 * @param csfForm
	 * @param principal
	 * @return
	 * @throws SystemException 
	 */
	public Collection<ContractServiceFeature> getChangedContractServiceFeatures(
			Contract contract, CsfForm csfForm, Principal principal,  
			ParticipantACIFeatureUpdateVO paf) throws SystemException {
		
		Collection<ContractServiceFeature> changedCsfCollection = 
			new ArrayList<ContractServiceFeature>();

		int contractId= contract.getContractNumber();
		
		CsfForm oldForm = (CsfForm) csfForm.getClonedForm();
		
		if (oldForm != null) {
			
			//Participant online address changes are permitted for
			ContractServiceFeature addressManagementCSF = new ContractServiceFeature();
			if (!StringUtils.equals(csfForm.getAddressManagementView(), oldForm.getAddressManagementView())) {
				if (CsfConstants.ADDRESS_MANAGEMENT_NONE.equals(csfForm.getAddressManagementView()) ^ CsfConstants.ADDRESS_MANAGEMENT_NONE.equals(oldForm.getAddressManagementView())) {
					addressManagementCSF.setValue(CsfConstants.ADDRESS_MANAGEMENT_NONE.equals(csfForm.getAddressManagementView()) ? ServiceFeatureConstants.NO
							: ServiceFeatureConstants.YES);
				}
			}
			
			checkCsfAttribute(csfForm, CsfConstants.FIELD_ACTIVE_ADDRESS, null, ServiceFeatureConstants.ADDRESS_MANAGEMENT_ACTIVE, null, addressManagementCSF);
			checkCsfAttribute(csfForm, CsfConstants.FIELD_TERMINATED_ADDRESS,null, ServiceFeatureConstants.ADDRESS_MANAGEMENT_TERMINATED, null, addressManagementCSF);
			checkCsfAttribute(csfForm, CsfConstants.FIELD_RETIRED_ADDRESS, null,  ServiceFeatureConstants.ADDRESS_MANAGEMENT_RETIRED, null, addressManagementCSF);
			checkCsfAttribute(csfForm, CsfConstants.FIELD_DISABLED_ADDRESS, null, ServiceFeatureConstants.ADDRESS_MANAGEMENT_DISABLED, null, addressManagementCSF);
			
			if (StringUtils.isNotEmpty(addressManagementCSF.getValue())
					|| addressManagementCSF.getAttributeMap().size() > 0) {
				addressManagementCSF.setContractId(contractId);
				addressManagementCSF.setName(ServiceFeatureConstants.ADDRESS_MANAGEMENT_FEATURE);
				setCSFUserInfo(addressManagementCSF, principal);
				changedCsfCollection.add(addressManagementCSF);
			}
			
			if("true".equals(csfForm.getResetDeferralValues())) {
				String minAmtDefaultValue = getAttributeDefaultValue(
						ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_AMOUNT);
				String maxAmtDefaultValue = getAttributeDefaultValue(
						ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_AMOUNT);
				String minPerDefaultValue = getAttributeDefaultValue(
						ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_PERCENT);
				String maxPerDefaultValue = getAttributeDefaultValue(
						ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_PERCENT);
					
				csfForm.setDeferralLimitDollars(minAmtDefaultValue);
				csfForm.setDeferralMaxLimitDollars(maxAmtDefaultValue);
				csfForm.setDeferralLimitPercent(minPerDefaultValue);
				csfForm.setDeferralMaxLimitPercent(maxPerDefaultValue);
			}
			
			ContractServiceFeature medCSF = new ContractServiceFeature();
			
			//Participants can specify deferral amounts as
			if(checkCsfAttribute(csfForm, CsfConstants.FIELD_DEFERRAL_TYPE, null, ServiceFeatureConstants.MD_DEFERRAL_TYPE, null , medCSF)){
			
				if(Constants.DEFERRAL_TYPE_PERCENT.equals(csfForm.getDeferralType())){
					String minAmtDefaultValue = getAttributeDefaultValue(
							ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_AMOUNT);
					String maxAmtDefaultValue = getAttributeDefaultValue(
							ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_AMOUNT);
					
					// Req. 301.1 -- reset amounts to default values
					if(csfForm.getDeferralLimitDollars() != null && csfForm.getDeferralLimitDollars().trim().length() > 0)
						csfForm.setDeferralLimitDollars(minAmtDefaultValue);
					if(csfForm.getDeferralMaxLimitDollars() != null && csfForm.getDeferralMaxLimitDollars().trim().length() > 0)
						csfForm.setDeferralMaxLimitDollars(maxAmtDefaultValue);
				}
	
				if(Constants.DEFERRAL_TYPE_DOLLAR.equals(csfForm.getDeferralType())){
					String minPerDefaultValue = getAttributeDefaultValue(
							ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_PERCENT);
					String maxPerDefaultValue = getAttributeDefaultValue(
							ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_PERCENT);
					
					// Req. 302.1 -- reset percentages to default values
					if(csfForm.getDeferralLimitPercent() != null && csfForm.getDeferralLimitPercent().trim().length() > 0)
						csfForm.setDeferralLimitPercent(minPerDefaultValue);
					if(csfForm.getDeferralMaxLimitPercent() != null && csfForm.getDeferralMaxLimitPercent().trim().length() > 0)
						csfForm.setDeferralMaxLimitPercent(maxPerDefaultValue);
				}
				
				paf.setOldDeferralType(oldForm.getDeferralType());
				paf.setNewDeferralType(csfForm.getDeferralType());
			}
			
			
			ContractServiceFeature aciCSF = new ContractServiceFeature();
			
			//Participants are allowed to change their deferrals online
			checkCsfAttribute(csfForm, CsfConstants.FIELD_CHANGE_DEFERRALS_ONLINE, null, ServiceFeatureConstants.MD_CHANGE_DEFERRALS_ONLINE, null, medCSF);
			
			// Method to set the Manage deferral values 
			setManageDeferralValues(csfForm, paf, aciCSF);
			
			//Participants are allowed to enroll online
			checkCsfAttribute(csfForm, CsfConstants.FIELD_ENROLL_ONLINE, null, ServiceFeatureConstants.MD_ENROLL_ONLINE, null, medCSF);
			
			//Payroll cut off for online deferral and auto enrollment changes
			checkCsfAttributeAsAmount(csfForm, CsfConstants.FIELD_PAYROLL_CUTOFF, null, ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE, null, medCSF);
			
			// PIL - Participants can initiate online loans 
			ContractServiceFeature participantInitiateLoansCSF = new ContractServiceFeature();
			checkCsfValue(csfForm, CsfConstants.FIELD_PARTICIPANT_INITIATE_LOANS, participantInitiateLoansCSF);
			if (StringUtils.isNotEmpty(participantInitiateLoansCSF.getValue())){
				participantInitiateLoansCSF.setContractId(contractId);
				participantInitiateLoansCSF.setName(ServiceFeatureConstants.PARTICIPANT_INITIATE_LOANS_FEATURE);
				setCSFUserInfo(participantInitiateLoansCSF, principal);
				changedCsfCollection.add(participantInitiateLoansCSF);
			}

			// Turn on Online Beneficiary service
			ContractServiceFeature obdCSF =new ContractServiceFeature();
			checkCsfValue(csfForm, CsfConstants.FIELD_ONLINE_BENEFICIARY_SERVICE, obdCSF);
			
			if (StringUtils.isNotEmpty(obdCSF.getValue())
					|| obdCSF.getAttributeMap().size() > 0) {
				obdCSF.setContractId(contractId);
				obdCSF.setName(ServiceFeatureConstants.ONLINE_BENEFICIARY_DESIGNATION);
				setCSFUserInfo(obdCSF, principal);
				changedCsfCollection.add(obdCSF);
			}
			
			// Plan Highlights are created by John Hancock
			ContractServiceFeature phCSF = new ContractServiceFeature();
			checkCsfValue(csfForm, CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE, phCSF);
			//Method to set the summary plan highlights reviewed value
			checkCsfAttribute(csfForm, CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_REVIEWED, CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE, 
					ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_REVIEWDED, ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_AVAILABLE, phCSF);
			
			if (StringUtils.isNotEmpty(phCSF.getValue())
					|| phCSF.getAttributeMap().size() > 0) {
				phCSF.setContractId(contractId);
				phCSF.setName(ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_AVAILABLE);
				setCSFUserInfo(phCSF, principal);
				changedCsfCollection.add(phCSF);
			}
			
			// edelivery for plan notices and statements
			ContractServiceFeature eDeliveryCSF = new ContractServiceFeature();
			checkCsfAttribute(csfForm, CsfConstants.FIELD_WIRED_AT_WORK, null,
					ServiceFeatureConstants.WIRED_AT_WORK_CODE,
					ServiceFeatureConstants.EDELIVERY_PLAN_NOTICES_STATEMENTS, eDeliveryCSF);
			checkCsfAttribute(csfForm, CsfConstants.FIELD_NOTICE_OF_INTERNET_AVAILABILITY, null,
					ServiceFeatureConstants.NOTICE_OF_INTERNET_AVAILABILITY,
					ServiceFeatureConstants.EDELIVERY_PLAN_NOTICES_STATEMENTS, eDeliveryCSF);
			if (StringUtils.isNotEmpty(eDeliveryCSF.getValue()) || eDeliveryCSF.getAttributeMap().size() > 0) {
				eDeliveryCSF.setContractId(contractId);
				// if NOIA flag is ‘N’ then keep edy as ‘N’
				String edfValue = StringUtils.equals(csfForm.getNoticeOfInternetAvailability(), Constants.NO_INDICATOR)
						? Constants.NO
						: Constants.YES;
				eDeliveryCSF.setName(ServiceFeatureConstants.EDELIVERY_PLAN_NOTICES_STATEMENTS);
				eDeliveryCSF.setValue(edfValue);
				setCSFUserInfo(eDeliveryCSF, principal);
				changedCsfCollection.add(eDeliveryCSF);
			}
			
			//Notice Generation Service
			ContractServiceFeature ngCSF = new ContractServiceFeature();
            boolean noticeIndChanged = checkCsfValue(csfForm, "noticeServiceInd", ngCSF);
            //Method to set the notice option value
            checkCsfAttribute(csfForm, "noticeTypeSelected", "noticeServiceInd", ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_OPTCD, ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_CD, ngCSF);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(MM_DD_YYYY);
            String strDate = sdf.format(date);
            if(noticeIndChanged && CsfConstants.CSF_YES.equalsIgnoreCase(csfForm.getNoticeServiceInd())){
                ngCSF.addAttribute(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_SELECTION_DATE_CD, strDate);
            }
            else if (noticeIndChanged && CsfConstants.CSF_NO.equalsIgnoreCase(csfForm.getNoticeServiceInd())){
            	ngCSF.addAttribute(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_DESELECTION_DATE_CD, strDate);
            	ngCSF.addAttribute(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_OPTCD, StringUtils.EMPTY);
            	
            }
            if (StringUtils.isNotEmpty(ngCSF.getValue())
                    || ngCSF.getAttributeMap().size() > 0) {
                ngCSF.setContractId(contractId);
                ngCSF.setName(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_CD);
                setCSFUserInfo(ngCSF, principal);
                changedCsfCollection.add(ngCSF);
            }
			
			//Turn on Eligibility Calculation Service
			getChangedECcsfValues(contractId,csfForm,principal,changedCsfCollection);
			
			ContractServiceFeature autoEnrollCSF = new ContractServiceFeature();
			// Turn on JH EZstart
			checkCsfValue(csfForm, CsfConstants.FIELD_AUTO_ENROLL_IND, autoEnrollCSF);
			
			//Method to set the Initial Enrollment Date value
			checkCsfAttribute(csfForm, CsfConstants.FIELD_INITIAL_ENROLL_DATE_AS_STRING, CsfConstants.FIELD_AUTO_ENROLL_IND, 
					ServiceFeatureConstants.AUTO_ENROLLMENT_INITIAL_ENROLLMENT_DATE, ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE, autoEnrollCSF);
			
			if (StringUtils.isNotEmpty(autoEnrollCSF.getValue())
					|| autoEnrollCSF.getAttributeMap().size() > 0) {
				autoEnrollCSF.setContractId(contractId);
				autoEnrollCSF.setName(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
				setCSFUserInfo(autoEnrollCSF, principal);
				changedCsfCollection.add(autoEnrollCSF);
			}
			
			// Direct Mail of enrollment materials
			ContractServiceFeature directMailCSF = new ContractServiceFeature();
			checkCsfValue(csfForm, CsfConstants.FIELD_DIRECT_MAIL_IND, directMailCSF );
			if (StringUtils.isNotEmpty(directMailCSF.getValue())
					|| directMailCSF.getAttributeMap().size() > 0) {
				directMailCSF.setContractId(contractId);
				directMailCSF.setName(ServiceFeatureConstants.DIRECT_MAIL_FEATURE);
				setCSFUserInfo(directMailCSF, principal);
				changedCsfCollection.add(directMailCSF);
			}
			
			// Method to populate the Auto Contribution Increase Values
			setAutoContributionValues(csfForm, aciCSF, paf);
			
			if (StringUtils.isNotEmpty(aciCSF.getValue())
					|| aciCSF.getAttributeMap().size() > 0) {
				aciCSF.setContractId(contractId);
				aciCSF.setName(ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE);
				setCSFUserInfo(aciCSF, principal);
				changedCsfCollection.add(aciCSF);
			}
			
			// Vesting will be
			//Reporting vesting percentages on participant statements
			ContractServiceFeature vestingCSF = new ContractServiceFeature();
			checkCsfValue(csfForm, CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD, vestingCSF);
			checkCsfAttribute(csfForm, CsfConstants.FIELD_VESTING_DATA_ON_STATEMENT, CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD, 
					ServiceFeatureConstants.VESTING_PERCENTAGE_ON_STATEMENT, ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE, vestingCSF );
			
			if (StringUtils.isNotEmpty(vestingCSF.getValue())
					|| vestingCSF.getAttributeMap().size() > 0) {
				vestingCSF.setContractId(contractId);
				vestingCSF.setName(ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
				setCSFUserInfo(vestingCSF, principal);
				changedCsfCollection.add(vestingCSF);
			}

			//Payroll Frequency
			ContractServiceFeature payrollCSF = new ContractServiceFeature();
			checkCsfValue(csfForm, CsfConstants.FIELD_PLAN_FREQUENCY, payrollCSF);
			if (StringUtils.isNotEmpty(payrollCSF.getValue())
					|| payrollCSF.getAttributeMap().size() > 0) {
				payrollCSF.setContractId(contractId);
				payrollCSF.setName(ServiceFeatureConstants.PAYROLL_FREQUENCY);
				setCSFUserInfo(payrollCSF, principal);
				changedCsfCollection.add(payrollCSF);
			}
			
			// Allow Payroll Path submissions
			ContractServiceFeature autoPayrollCSF = new ContractServiceFeature();
			checkCsfValue(csfForm, CsfConstants.FIELD_PAYROLL_PATH_IND, autoPayrollCSF );
			if (StringUtils.isNotEmpty(autoPayrollCSF.getValue())
					|| autoPayrollCSF.getAttributeMap().size() > 0) {
				autoPayrollCSF.setContractId(contractId);
				autoPayrollCSF.setName(ServiceFeatureConstants.PAYROLL_PATH_FEATURE);
				setCSFUserInfo(autoPayrollCSF, principal);
				changedCsfCollection.add(autoPayrollCSF);
			}
			
			//Consent to deliver the Contract and any subsequent amendments to the contract via plan sponsor website
			ContractServiceFeature consentCSF = new ContractServiceFeature();
			checkCsfValue(csfForm, CsfConstants.FIELD_CONSENT_IND, consentCSF );
			if (StringUtils.isNotEmpty(consentCSF.getValue())
					|| consentCSF.getAttributeMap().size() > 0) {
				consentCSF.setContractId(contractId);
				consentCSF.setName(ServiceFeatureConstants.CONSENT_SERVICE_FEATURE);
				setCSFUserInfo(consentCSF, principal);
				changedCsfCollection.add(consentCSF);
			}

			if (StringUtils.isNotEmpty(medCSF.getValue())
					|| medCSF.getAttributeMap().size() > 0) {
				medCSF.setContractId(contractId);
				medCSF.setName(ServiceFeatureConstants.MANAGING_DEFERRALS);
				setCSFUserInfo(medCSF, principal);
				changedCsfCollection.add(medCSF);
			}

			//Online Loans
			ContractServiceFeature loansCSF = new ContractServiceFeature();   
			// verify whether the contract id has UOL entry
			// call checkDefaultCsfValue() if UOL count is zero
			// call checkCsfValue() , if UOL count is not Zero.
			if(!hasUOLCount(contract.getContractNumber()) && isLoanCsfOn(contract)){ 
				checkDefaultCsfValue(csfForm,CsfConstants.FIELD_ALLOW_ONLINE_LOANS,loansCSF);
			}
			else{ 
				checkCsfValue(csfForm, CsfConstants.FIELD_ALLOW_ONLINE_LOANS, loansCSF);
			}
			
			checkCsfAttribute(csfForm, CsfConstants.FIELD_WHO_WILL_REVIEW_LOANS, CsfConstants.FIELD_ALLOW_ONLINE_LOANS, ServiceFeatureConstants.WHO_WILL_REVIEW_LOANS, ServiceFeatureConstants.ALLOW_LOANS_FEATURE, loansCSF);
			checkCsfAttribute(csfForm, CsfConstants.FIELD_ALLOW_LOANS_PKG_GENERATE, CsfConstants.FIELD_ALLOW_ONLINE_LOANS, ServiceFeatureConstants.ALLOW_LOANS_PACKAGE_TO_GENERATE, ServiceFeatureConstants.ALLOW_LOANS_FEATURE, loansCSF);
			checkCsfAttribute(csfForm, CsfConstants.FIELD_CREATOR_APPROVE_LOANS, CsfConstants.FIELD_ALLOW_ONLINE_LOANS, ServiceFeatureConstants.CREATOR_OF_LOANREQUEST_APPROVE,ServiceFeatureConstants.ALLOW_LOANS_FEATURE, loansCSF);
			
			if (StringUtils.isNotEmpty(loansCSF.getValue())
					|| loansCSF.getAttributeMap().size() > 0) {
				loansCSF.setContractId(contractId);
				loansCSF.setName(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
				setCSFUserInfo(loansCSF, principal);
				changedCsfCollection.add(loansCSF);
			}
			
			// i:withdrawals
			ContractServiceFeature withdrawalsCSF = new ContractServiceFeature();
			
			checkCsfAttribute(csfForm, CsfConstants.FIELD_SPECIAL_TAX_NOTICE, null, ServiceFeatureConstants.IWITHDRAWALS_SPECIAL_TAX, null,withdrawalsCSF);
			checkCsfAttribute(csfForm, CsfConstants.FIELD_WHO_WILL_REVIEW_WITHDRAWALS, null,  ServiceFeatureConstants.WHO_WILL_REVIEW_WITHDRAWALS, null,withdrawalsCSF);
			checkCsfAttribute(csfForm, CsfConstants.FIELD_APPROVE_WITHDRAWALS, null,ServiceFeatureConstants.IWITHDRAWALS_APPROVE, null, withdrawalsCSF);
			checkCsfAttribute(csfForm, CsfConstants.FIELD_PARTICIPANT_WITHDRAWAL_IND, null, ServiceFeatureConstants.IWITHDRAWALS_PARTICIPANT,null, withdrawalsCSF);
			checkCsfAttribute(csfForm, CsfConstants.FIELD_REVIEW_WITHDRAWALS, null, ServiceFeatureConstants.IWITHDRAWALS_REVIEW, null, withdrawalsCSF);
			
			if (StringUtils.isNotEmpty(withdrawalsCSF.getValue())
					|| withdrawalsCSF.getAttributeMap().size() > 0) {
				withdrawalsCSF.setContractId(contractId);
				withdrawalsCSF.setName(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
				setCSFUserInfo(withdrawalsCSF, principal);
				changedCsfCollection.add(withdrawalsCSF);
			}
			
			
			// Managed Account Service
			getChangedManagedAccountServiceCSF(csfForm, oldForm, principal)
				.ifPresent(csf -> changedCsfCollection.add(csf));
			
		}

		// Payroll Feedback Service
		CsfDataHelper.getChangedPayrollFeedbackServiceCSF(csfForm, oldForm)
			.ifPresent(csf -> changedCsfCollection.add(csf));	
		
		
		// Populate Contract ID and Principal where missing
		if(CollectionUtils.isNotEmpty(changedCsfCollection)) {
			changedCsfCollection.stream()
				.filter(csf -> csf.getContractId() < 1)
				.forEach(csf -> csf.setContractId(contractId));
			
			changedCsfCollection.stream()
				.filter(csf -> csf.getPrincipalProfileId() < 1)
				.forEach(csf -> setCSFUserInfo(csf, principal));
		}
		
		return changedCsfCollection;
	}
	
	
	/**
	 * Method opened to public to allow for unit testing.
	 * 
	 * @param form 
	 * @param oldForm 
	 * @return <code>ContractServiceFeature</code> for Payroll Feedback Service, if change is detected.
	 */
	public static Optional<ContractServiceFeature> getChangedPayrollFeedbackServiceCSF(CsfForm form, CsfForm oldForm) {
		ContractServiceFeature contractServiceFeature = null;
		String previousValue = oldForm == null ? null : oldForm.getPayrollFeedbackService();
		String postedValue = form == null ? null : form.getPayrollFeedbackService();

		if(!Objects.equals(previousValue, postedValue)) {
			contractServiceFeature = new ContractServiceFeature();
			contractServiceFeature.setName(ServiceFeatureConstants.PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE);
			contractServiceFeature.setValue(ServiceFeatureConstants.PAYROLL_FEEDBACK_360_SERVICE_ATTRIBUTE_CODE.equals(postedValue) 
					|| ServiceFeatureConstants.PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE.equals(postedValue)
					? ServiceFeatureConstants.YES : ServiceFeatureConstants.NO);
			contractServiceFeature.getAttributeMap()
				.put(ServiceFeatureConstants.PAYROLL_FEEDBACK_360_SERVICE_ATTRIBUTE_CODE, 
					ServiceFeatureConstants.PAYROLL_FEEDBACK_360_SERVICE_ATTRIBUTE_CODE.equals(postedValue) ? ServiceFeatureConstants.YES : ServiceFeatureConstants.NO);
		}

		return Optional.ofNullable(contractServiceFeature);
	}
	
	/**
	 * 
	 * @param form 
	 * @param oldForm 
	 * @param principal 
	 * @return <code>ContractServiceFeature</code> for Managed Account Service, if change is detected.
	 */
	private Optional<ContractServiceFeature> getChangedManagedAccountServiceCSF(CsfForm form, CsfForm oldForm,
			Principal principal) {
		ContractServiceFeature csf = null;
		String oldDate = oldForm.getManagedAccountServiceAvailableToPptDate();
		String newDate = form.getManagedAccountServiceAvailableToPptDate();
		if (form.isManagedAccountSectionEditable() && !Objects.equals(oldDate, newDate)) {
			csf = new ContractServiceFeature();
			csf.setName(ServiceFeatureConstants.MANAGED_ACCOUNT_GROUP_CODE);
			csf.setValue(form.getManagedAccountServiceFeature().getServiceCode());
			csf.getAttributeMap().put(ServiceFeatureConstants.MANAGED_ACCOUNT_SERVICE_AVAILABLE_DATE, newDate);
			setCSFUserInfo(csf, principal);
		}
		return Optional.ofNullable(csf);
	}
	
	/**
	 * @param csfForm
	 * @param aciCSF
	 * @param paf
	 */
	private void setAutoContributionValues(CsfForm csfForm,
			ContractServiceFeature aciCSF, ParticipantACIFeatureUpdateVO paf) {
		//Turn on JH EZincrease
		if(checkCsfValue(csfForm, CsfConstants.FIELD_AUTO_CONTRIBUTION_INCREASE, aciCSF)){
			if(isValueEitherYesOrTrue(csfForm.getAutoContributionIncrease())){
				paf.setAciIndicator(ServiceFeatureConstants.YES);
			}else{
				paf.setAciIndicator(ServiceFeatureConstants.NO);
			}
		}
		if(checkCsfAttribute(csfForm, CsfConstants.FIELD_ACI_ANNIVERSARY_YEAR, CsfConstants.FIELD_AUTO_CONTRIBUTION_INCREASE, 
				ServiceFeatureConstants.ACI_ANNIVERSARY_DATE, ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, aciCSF)){
			try {
				paf.setAnnivDate(CsfConstants.aciDisplayDateFormat.parse(csfForm.getAciAnniversaryDate()+"/"+csfForm.getAciAnniversaryYear()));
			} catch (ParseException e) {
				logger.error(e.getMessage());
			}
		}
		
		//Initial increase to take effect on first anniversary date after the enrollment
		checkCsfAttribute(csfForm, CsfConstants.FIELD_INCREASE_ANNIVERSARY, CsfConstants.FIELD_AUTO_CONTRIBUTION_INCREASE, 
				ServiceFeatureConstants.ACI_INCREASE_NEW_ENROLLES_ANNIVERSARY, ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, aciCSF);
	}
	/**
	 * Method to set the actual Deferral Values into ParticipantACIFeatureUpdateVO
	 * 
	 * @param csfForm
	 * @param paf
	 * @param aciCSF
	 */
	private void setManageDeferralValues(CsfForm csfForm,
			ParticipantACIFeatureUpdateVO paf, ContractServiceFeature aciCSF) {
		
		if(!ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(csfForm.getAciSignupMethod())){
			//Default scheduled deferral increase – $
			if(checkCsfAttribute(csfForm, CsfConstants.FIELD_DEFERRAL_LIMIT_PERCENT, CsfConstants.FIELD_CHANGE_DEFERRALS_ONLINE,
					ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_PERCENT,  ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, aciCSF))	{
				if(csfForm.getDeferralLimitPercent() != null && csfForm.getDeferralLimitPercent().trim().length() > 0)
					paf.setDeferralLimitPct(Integer.parseInt(csfForm.getDeferralLimitPercent()));
			}
			//Default scheduled deferral increase – maximum $
			if(checkCsfAttribute(csfForm, CsfConstants.FIELD_DEFERRAL_MAX_LIMIT_PERCENT, CsfConstants.FIELD_CHANGE_DEFERRALS_ONLINE, 
					ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_PERCENT,  ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, aciCSF)){
				if(csfForm.getDeferralMaxLimitPercent() != null && csfForm.getDeferralMaxLimitPercent().trim().length() > 0)
					paf.setDeferralIncreasePct(Integer.parseInt(csfForm.getDeferralMaxLimitPercent()));
			}
		}else{
			if(csfForm.getPlanDeferralLimitPercent() != null && csfForm.getPlanDeferralLimitPercent().trim().length() > 0)
				paf.setDeferralLimitPct(Integer.parseInt(csfForm.getPlanDeferralLimitPercent()));
			
			if(csfForm.getPlanDeferralMaxLimitPercent() != null && csfForm.getPlanDeferralMaxLimitPercent().trim().length() > 0)
				paf.setDeferralIncreasePct(Integer.parseInt(csfForm.getPlanDeferralMaxLimitPercent()));
		}
		//Default scheduled deferral increase – %
		if(checkCsfAttributeAsAmount(csfForm, CsfConstants.FIELD_DEFERRAL_LIMIT_DOLLARS, CsfConstants.FIELD_CHANGE_DEFERRALS_ONLINE,
				ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_AMOUNT,  ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, aciCSF)){
			if(csfForm.getDeferralLimitDollars() != null && csfForm.getDeferralLimitDollars().trim().length() > 0)
				paf.setDeferralLimitAmt(parseAmount(csfForm.getDeferralLimitDollars()).intValue());
		}
		
		//Default scheduled deferral increase – maximum %
		if(checkCsfAttributeAsAmount(csfForm, CsfConstants.FIELD_DEFERRAL_MAX_LIMIT_DOLLARS, CsfConstants.FIELD_CHANGE_DEFERRALS_ONLINE,
				ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_AMOUNT,  ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, aciCSF)){
			if(csfForm.getDeferralMaxLimitDollars() != null && csfForm.getDeferralMaxLimitDollars().trim().length() > 0)
				paf.setDeferralIncreaseAmt(parseAmount(csfForm.getDeferralMaxLimitDollars()).intValue());
		}
	}
	
	/**
	 * retrieves the client users with the given permission
	 * 
	 * @param contractNumber
	 * @param permissionCode
	 * @return
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public List<Long> retrieveClientUsersByPermission(
			int contractNumber, String permissionCode) throws SystemException {
		
		Map<String, List<Long>> clientPermissionMap = null;
		List<Long> clientUsersWithPermission = null;

		List<String> permissionList = new ArrayList<String>();
		permissionList.add(permissionCode);
		clientPermissionMap = 
			SecurityServiceDelegate.getInstance().getExternalUsersWithRolePermission(
					contractNumber, permissionList, null);
		
		if (clientPermissionMap != null){
			clientUsersWithPermission = clientPermissionMap.get(permissionCode);
		}
		
		return clientUsersWithPermission;
	}
	
	/**
	 * Helper method that determines the need for locking the TPA firm 
	 * based on the change to the Who will review withdrawal requests CSF
	 * 
	 * @param newForm
	 * @param oldForm
	 * @return true
	 */
	private boolean checkIfRequiredToLocktheTPAFirm(CsfForm newForm, 
			Collection<ContractServiceFeature> changedCsfCollection)
	throws SystemException {
		for (ContractServiceFeature newFeature : changedCsfCollection) {
			if (StringUtils.equals(newFeature.getName(),
					ServiceFeatureConstants.IWITHDRAWALS_FEATURE) || 
					StringUtils.equals(newFeature.getName(),
							ServiceFeatureConstants.ALLOW_LOANS_FEATURE)) {
				// old CSF values
				CsfForm oldForm = (CsfForm) newForm.getClonedForm();

				if (!(StringUtils.equals(oldForm.getAllowOnlineLoans(), newForm.getAllowOnlineLoans()))) {
					return true;
				} else if (!(StringUtils.equals(oldForm.getWithdrawalInd(), newForm.getWithdrawalInd()))) {
					return true;
				} else if (!(StringUtils.equals(oldForm.getWhoWillReviewWithdrawals(), newForm.getWhoWillReviewWithdrawals()))) {
					if (StringUtils.equals(CsfConstants.WHO_WILL_REVIEW_WD_TPA, oldForm.getWhoWillReviewWithdrawals()) || 
							StringUtils.equals(CsfConstants.WHO_WILL_REVIEW_WD_TPA, newForm.getWhoWillReviewWithdrawals()) ||
							StringUtils.equals(CsfConstants.WHO_WILL_REVIEW_WD_NOREVIEW, newForm.getWhoWillReviewWithdrawals()) )
					{ // Above No-Review condition is for csf 119 
						return true;
					} 

				} else if (!(StringUtils.equals(oldForm.getWhoWillReviewLoanRequests(), newForm.getWhoWillReviewLoanRequests()))) {
					if (StringUtils.equals(CsfConstants.WHO_WILL_REVIEW_WD_TPA, oldForm.getWhoWillReviewLoanRequests()) || 
							StringUtils.equals(CsfConstants.WHO_WILL_REVIEW_WD_TPA, newForm.getWhoWillReviewLoanRequests())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Handles the permission for TPA Firm based on who will review withdrawal CSF change.
	 * 
	 * @param request
	 * @param newForm
	 * @param firmInfo
	 * @throws SecurityServiceException
	 * @throws SystemException
	 */
	private void handleTPAFirmPermissionsForWithdrawals(final HttpServletRequest request,
			final CsfForm newForm, final TPAFirmInfo firmInfo) throws SecurityServiceException,
			SystemException {

		final CsfForm oldForm = (CsfForm) newForm.getClonedForm();
		final String oldWhoWillReviewWithdrawals = oldForm.getWhoWillReviewWithdrawals();
		final String newWhoWillReviewWithdrawals = newForm.getWhoWillReviewWithdrawals();
		final String oldWithdrawalsIndicator = oldForm.getWithdrawalInd();
		final String newWithdrawalsIndicator = newForm.getWithdrawalInd();
		final boolean oldWithdrawalsIndicatorIsOn = isValueEitherYesOrTrue(oldWithdrawalsIndicator);
		final boolean newWithdrawalsIndicatorIsOn = isValueEitherYesOrTrue(newWithdrawalsIndicator);

		final SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
		final UserProfile userProfile = getUserProfile(request);

		if (!(oldWithdrawalsIndicatorIsOn && newWithdrawalsIndicatorIsOn)) {
			if (CsfConstants.WHO_WILL_REVIEW_WD_TPA.equals(newWhoWillReviewWithdrawals)) {
				// If not locked, grant initiate/review permissions for the TPA Firm
				ContractPermission permissions = firmInfo.getContractPermission();
				permissions.setReviewIWithdrawals(true);
				permissions.setInitiateWithdrawalsAndViewMine(true);
				firmInfo.setContractPermission(permissions);
				service.updateTpaFirm(userProfile.getPrincipal(), firmInfo);

			} else if (CsfConstants.WHO_WILL_REVIEW_WD_PS.equals(newWhoWillReviewWithdrawals)) {
				// remove TPA firm permissions
				ContractPermission permissions = firmInfo.getContractPermission();
				permissions.setReviewIWithdrawals(false);
				permissions.setInitiateWithdrawalsAndViewMine(false);
				firmInfo.setContractPermission(permissions);
				service.updateTpaFirm(userProfile.getPrincipal(), firmInfo);
			}

		} else if (oldWhoWillReviewWithdrawals != null && newWhoWillReviewWithdrawals != null
				&& !StringUtils.equals(oldWhoWillReviewWithdrawals, newWhoWillReviewWithdrawals)) {
			// WHO WILL REVIEW WITHDRAWAL CSF change
			// if the value changes from from 'TPA' to 'Plan Sponsor' or 'No Review'
			// csf 119 - if the value changes from TPA/PS to No Review
			if (CsfConstants.WHO_WILL_REVIEW_WD_TPA.equals(oldWhoWillReviewWithdrawals) || 
					CsfConstants.WHO_WILL_REVIEW_WD_NOREVIEW.equals(newWhoWillReviewWithdrawals)) {
				// If not locked remove Initiate/review permissions for the TPA Firm
				ContractPermission permissions = firmInfo.getContractPermission();
				permissions.setReviewIWithdrawals(false);
				permissions.setInitiateWithdrawalsAndViewMine(false);
				firmInfo.setContractPermission(permissions);
				service.updateTpaFirm(userProfile.getPrincipal(), firmInfo);
			}
			// if the value changes from 'PS' or 'No Review' to 'TPA'
			else if (CsfConstants.WHO_WILL_REVIEW_WD_TPA.equals(newWhoWillReviewWithdrawals)) {
				// If not locked, grant initiate/review permissions for the TPA Firm
				ContractPermission permissions = firmInfo.getContractPermission();
				permissions.setReviewIWithdrawals(true);
				permissions.setInitiateWithdrawalsAndViewMine(true);
				firmInfo.setContractPermission(permissions);
				service.updateTpaFirm(userProfile.getPrincipal(), firmInfo);
			}
		}
	}
	
	/**
	 * Handles the loan permission
	 * 
	 * @param request
	 * @param newForm
	 * @param oldForm
	 * @throws SystemException
	 */
	private void handleLoanPermissions(HttpServletRequest request, CsfForm newForm,
			Collection<ContractServiceFeature> changedCsfCollection, 
			TPAFirmInfo firmInfo, Collection<GenericException> warnings, boolean isConfirmed)
	throws SystemException {
		for (ContractServiceFeature newFeature : changedCsfCollection) {
			if (StringUtils.equals(newFeature.getName(),
					ServiceFeatureConstants.ALLOW_LOANS_FEATURE)) {	   
				CsfForm oldForm = (CsfForm) newForm.getClonedForm();

				String oldValue = oldForm.getWhoWillReviewLoanRequests();
				String newValue = newForm.getWhoWillReviewLoanRequests();

				String oldIndicator1 = oldForm.getAllowOnlineLoans();
				String newIndicator1 = newForm.getAllowOnlineLoans();
				boolean oldValue1 = isValueEitherYesOrTrue(oldIndicator1);
				boolean newValue1 = isValueEitherYesOrTrue(newIndicator1);		
				try {		
					// Get the Principal from the request
					UserProfile userProfile = getUserProfile(request);
					// get the user profile object and set the current contract to null
					int contractId = userProfile.getCurrentContract().getContractNumber();

					String permissionCode = PermissionType.getPermissionCode(PermissionType.REVIEW_LOANS); 

					// check if there is at least one client user with review permission, If yes then don't do anything
					List <Long> clientUsersWithReviewLoans = retrieveClientUsersByPermission(contractId, permissionCode);
					String usersList = null;
					// Allow Online Loans CSF change
					if (!(oldValue1 && newValue1)) {
						if (CsfConstants.WHO_WILL_REVIEW_WD_PS.equals(newValue)) { //2937
							// If there is not even one client user with review permission
							if (CollectionUtils.isEmpty(clientUsersWithReviewLoans)) {
								// grant review permissions to all those users who can have 					
								usersList = grantReviewPermissionForClientUsers(getUserProfile(request), permissionCode, isConfirmed);
								if(!isConfirmed && StringUtils.isNotEmpty(usersList)){
									warnings.add(new ValidationError(new String[] {CsfConstants.EMPTY_STRING},ErrorCodes.WARNING_LOAN_REVIEW_PERMISSION_COULD_NOT_BE_GRANTED,new String[] { usersList }, ValidationError.Type.warning));
								}
							}
						} else if (CsfConstants.WHO_WILL_REVIEW_WD_TPA.equals(newValue)) { //2935
							if (CollectionUtils.isNotEmpty(clientUsersWithReviewLoans)) {
								// remove review permissions to all those users who have
								// give a message for those users that we couldn't remove the permission
								usersList = removeReviewPermissionForClientUsers(userProfile, permissionCode, clientUsersWithReviewLoans, isConfirmed);
								if(!isConfirmed && StringUtils.isNotEmpty(usersList)){
									warnings.add(new ValidationError(new String[] {CsfConstants.EMPTY_STRING},ErrorCodes.WARNING_LOAN_REVIEW_PERMISSION_COULD_NOT_BE_TAKEN,new String[] { usersList }, ValidationError.Type.warning));
								}
							}
						}
					} // WHO WILL REVIEW Loans CSF change
					else if (oldValue != null && newValue != null && !StringUtils.equals(oldValue, newValue)) {
						// if the value changes from from 'TPA' to 'Plan Sponsor'
						if (CsfConstants.WHO_WILL_REVIEW_WD_PS.equals(newValue)) {
							// If there is not even one client user with review permission
							if (CollectionUtils.isEmpty(clientUsersWithReviewLoans)) {
								// grant review permissions to all those users who can have 					
								usersList = grantReviewPermissionForClientUsers(getUserProfile(request), permissionCode, isConfirmed);
								if(!isConfirmed && StringUtils.isNotEmpty(usersList)){
									warnings.add(new ValidationError(new String[] {CsfConstants.EMPTY_STRING},ErrorCodes.WARNING_LOAN_REVIEW_PERMISSION_COULD_NOT_BE_GRANTED,new String[] { usersList }, ValidationError.Type.warning));
								}
							}
						}
						// if the value changes from from 'Plan Sponsor' to 'TPA'
						if (CsfConstants.WHO_WILL_REVIEW_WD_PS.equals(oldValue)) {
							// remove the review permission from all the client users
							// If there is not even one client user with review permission
							if (CollectionUtils.isNotEmpty(clientUsersWithReviewLoans)) {
								// grant review permissions to all those users who can have
								// give a msg for those users that we couldn't  remove the permission
								usersList = removeReviewPermissionForClientUsers(getUserProfile(request), permissionCode, clientUsersWithReviewLoans, isConfirmed);
								if(!isConfirmed && StringUtils.isNotEmpty(usersList)){
									warnings.add(new ValidationError(new String[] {CsfConstants.EMPTY_STRING},ErrorCodes.WARNING_LOAN_REVIEW_PERMISSION_COULD_NOT_BE_TAKEN,new String[] { usersList }, ValidationError.Type.warning));
								}
							}
						}
					}

				} catch (SecurityServiceException e) {
					throw new SystemException(e, 
							"Failed to set review loans permissions to client users in handleLoanPermissions() :"
							+ e.toString());
				}
			}
		}
	}


	/**
	 * return number of outstanding old i:loan requests
	 * 
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	private int getOutstandingOldILoanRequestCount(int contractId) throws SystemException {
		int oldILoanRequestsCount = 0; 
		oldILoanRequestsCount = 
			(LoanServiceDelegate.getInstance().getOutstandingOldILoanRequestCount(contractId)).intValue();
		return oldILoanRequestsCount;
	}

	/** This method returns true if the UOL count is greater than zero 
	 * else returns false. 
	 * 
	 * @param contractId
	 * @return uolCount
	 * @throws SystemException 
	 */
	private boolean hasUOLCount(int contractId) throws SystemException{
		int uolCount = 0;		
		uolCount = (LoanServiceDelegate.getInstance().getUOLCount(contractId)).intValue();
		
		// if the count is greater than zero means, the contract id is already having UOL entry
		if(uolCount>0){
			return true;
		}//else there is no entry in Contract_service_feature for UOL
		
		return false;
	}

	/**
	 * This method is used to compare effective date and loan launch date 
	 * and returns true if effective date is greater than launch date.
	 * The default value of allow online loans will be
	 * set based on this boolean value
	 * 
	 * @param contract
	 * @return
	 */
	private boolean isLoanCsfOn(Contract contract) {
		boolean defaultLoanCsfValue = false;
		BusinessParameterValueObject businessParameterObject = 
			PartyServiceDelegate.getInstance().getBuisnessParameterValueObject();
		
		Date effectiveDate = contract.getEffectiveDate();
		if (businessParameterObject != null) {
			Date loanLaunchDate = businessParameterObject.getLoansLaunchDate();
			if (effectiveDate != null && loanLaunchDate != null && contract != null) {
				// compare effective date with loan launch date
				if (effectiveDate.after(loanLaunchDate) && contract.isBusinessConverted()) {
					//indicates default value is  set for loan csf(allow online loans)
					defaultLoanCsfValue = true;
				}
			}
		}
		return defaultLoanCsfValue;
	}
	

	/**
     * Returns the user profile associated with the given request.
     * 
     * @param request The request object.
     * @return The user profile object associated with the request (or null if none is found).
     */
    public static UserProfile getUserProfile(final HttpServletRequest request) {
        return SessionHelper.getUserProfile(request);
    }
    
   /* *//**
	 * checkDirectMailValue 
	 * Checks if direct mail value changed
	 *
	 * @param csfForm
	 * @param fieldName
	 * @param ContractServiceFeature csf,
	 * @param List<String> changedFieldLis
	 * @return true if value changed
	 *//*
	private boolean checkDirectMailValue(CsfForm csfForm, String fieldName, ContractServiceFeature csf,
			List<String> changedFieldList) {
		boolean changed = false;
		try {
			Method getFieldValueMethod = CsfForm.class.getDeclaredMethod("get"
					+ StringUtils.capitalize(fieldName), new Class[] {});
			String oldValue = (String) getFieldValueMethod.invoke(csfForm.getClonedForm(),
					new Object[] {});
			String newValue = (String) getFieldValueMethod.invoke(csfForm, new Object[] {});
			if (!StringUtils.equals(newValue, oldValue)) {
				if (CsfConstants.CSF_YES.equals(newValue) || CsfConstants.CSF_TRUE.equals(newValue)) {
					csf.setValue(ServiceFeatureConstants.YES);
				} else if (CsfConstants.CSF_NO.equals(newValue) || CsfConstants.CSF_FALSE.equals(newValue)) {
					csf.setValue(ServiceFeatureConstants.NO);
				} else {
					csf.setValue(newValue);
				}
				changedFieldList.add(fieldName);
				changed = true;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return changed;
	}*/

	/**
	 * This method is used to set the default value for Loan csf, 
	 * and is implemented to break the warning message which will be displayed 
	 * when the user has not changed any values in the screen.
	 * 
	 * @param fieldName
	 * @param changedFieldList
	 */
	private void checkDefaultCsfValue(CsfForm csfForm, String fieldName, 
			ContractServiceFeature csf){
		if(StringUtils.isNotBlank(csfForm.getAllowOnlineLoans()) && 
				CsfConstants.CSF_YES.equalsIgnoreCase(csfForm.getAllowOnlineLoans())){
			csf.setValue(ServiceFeatureConstants.YES);
		}
	}

	/**
	 * Sets the value for the specified field and returns true if the value 
	 * is changed
	 * 
	 * @param csfForm
	 * @param fieldName
	 * @param csf
	 * @param changedFieldList
	 * @return TRUE - if the value is changed
	 */
	private boolean checkCsfValue(CsfForm csfForm, String fieldName, ContractServiceFeature csf) {
		
		boolean changed = false;    	

		try {
			//reflection to create the method name based on the fieldName
			Method getFieldValueMethod = CsfForm.class.getDeclaredMethod("get"
					+ StringUtils.capitalize(fieldName), new Class[] {});
			
			// get the old value for the field specified using reflection
			String oldValue = (String) getFieldValueMethod.invoke(csfForm.getClonedForm(),
					new Object[] {});

			// get the new value for the field specified using reflection			
			String newValue = (String) getFieldValueMethod.invoke(csfForm, new Object[] {});
			
			if (!StringUtils.equals(newValue, oldValue)) {
				setCSFValue(csf, newValue);
				changed = true;
			} 
		} catch (Exception e) {
			logger.error(e);
		}
		return changed;
	}
	
	/**
	 * Method to set the CSF value into the give csf object
	 * @param csf
	 * @param newValue
	 */
	private void setCSFValue(ContractServiceFeature csf, String newValue) {
		if (isValueEitherYesOrTrue(newValue)) {
			csf.setValue(ServiceFeatureConstants.YES);
		} else if (CsfConstants.CSF_NO.equals(newValue) 
				|| CsfConstants.CSF_FALSE.equals(newValue)) {
			csf.setValue(ServiceFeatureConstants.NO);
		} else {
			csf.setValue(newValue); 
		}
	}

	/**
	 * Sets the value for the specified csf attribute and returns true if the value 
	 * is changed
	 * 
	 * @param csfForm
	 * @param fieldName
	 * @param attributeCode
	 * @param csf
	 * @param changedFieldList
	 * @return TRUE - if the value is changed
	 */
	private boolean checkCsfAttribute(CsfForm csfForm, String fieldName, String parentFieldName,
			String attributeCode, String parentCode, ContractServiceFeature csf) {

		boolean changed = false;
		try {
			//reflection to create the method name based on the fieldName
			Method getFieldValueMethod = CsfForm.class.getDeclaredMethod("get"
					+ StringUtils.capitalize(fieldName), new Class[] {});
			
			// get the old value for the field specified using reflection
			String oldValue = (String) getFieldValueMethod.invoke(csfForm.getClonedForm(),
					new Object[] {});
			
			// get the new value for the field specified using reflection			
			String newValue = (String) getFieldValueMethod.invoke(csfForm, new Object[] {});
		
			String parentOldValue = null;
			String parentNewValue = null;
			
			if(parentFieldName != null && parentCode != null ){
				//reflection to create the method name based on the fieldName
				Method getParentValueMethod = CsfForm.class.getDeclaredMethod("get"
						+ StringUtils.capitalize(parentFieldName), new Class[] {});
				
				// get the old value for the field specified using reflection
				 parentOldValue = (String) getParentValueMethod.invoke(csfForm.getClonedForm(),
						new Object[] {});
				
				// get the new value for the field specified using reflection			
				 parentNewValue = (String) getParentValueMethod.invoke(csfForm, new Object[] {});
				
			}
		
			if(parentOldValue != null && parentNewValue!= null &&
					!StringUtils.equals(parentOldValue, parentNewValue) &&
					(CsfConstants.CSF_NO.equals(parentNewValue) 
							|| CsfConstants.CSF_FALSE.equals(parentNewValue))){
				String defaultValue = getAttributeDefaultValue(parentCode, attributeCode);
				if(CsfConstants.CSF_N.equals(defaultValue)) {
					defaultValue = CsfConstants.CSF_NO;
				} else if(CsfConstants.CSF_Y.equals(defaultValue)){
					defaultValue = CsfConstants.CSF_YES;
				}
				if((defaultValue == null && (CsfConstants.DATE_ATTRIBUTES.contains(attributeCode) || CsfConstants.NOTICE_SERVICE_ATTRIBUTES.contains(attributeCode)))
						||  (defaultValue!= null && !defaultValue.equals(oldValue))){
					csf.addAttribute(attributeCode,defaultValue);
					changed = true;
				}
				
			}else if (!StringUtils.equals(newValue, oldValue)) {
				setCSFAttrValue(csf, attributeCode, newValue);
				changed = true;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return changed;
	}
	
	/**
	 * Method to add the CSF Attribute value in to the given CSF object, 
	 * depends on the value provided from the form
	 * 
	 * @param csf
	 * @param attributeCode
	 * @param newValue
	 */
	private void setCSFAttrValue(ContractServiceFeature csf,
			String attributeCode, String newValue) {
		if (isValueEitherYesOrTrue(newValue)) {
			csf.addAttribute(attributeCode, ServiceFeatureConstants.YES);
		} else if (CsfConstants.CSF_NO.equals(newValue) 
				|| CsfConstants.CSF_FALSE.equals(newValue)) {
			csf.addAttribute(attributeCode, ServiceFeatureConstants.NO);
		} else {
			csf.addAttribute(attributeCode, newValue);
		}
	}
	/**
	 * validates the Amount fields are changed or not
	 * 
	 * @param csfForm
	 * @param fieldName
	 * @param attributeCode
	 * @param csf
	 * @param changedFieldList
	 * @return TRUE if the amount fields are changed
	 */
	private boolean checkCsfAttributeAsAmount(CsfForm csfForm, String fieldName, String parentFieldName,
			String attributeCode, String parentCode, ContractServiceFeature csf) {

		boolean changed = false;
		try {
			//reflection to create the method name based on the fieldName
			Method getFieldValueMethod = CsfForm.class.getDeclaredMethod("get"
					+ StringUtils.capitalize(fieldName), new Class[] {});

			// get the old value for the field specified using reflection
			String oldValue = (String) getFieldValueMethod.invoke(csfForm.getClonedForm(),
					new Object[] {});
			
			// get the new value for the field specified using reflection			
			String newValue = (String) getFieldValueMethod.invoke(csfForm, new Object[] {});
			String parentOldValue = null;
			String parentNewValue = null;
			
			if(parentFieldName != null && parentCode != null ){
				//reflection to create the method name based on the fieldName
				Method getParentValueMethod = CsfForm.class.getDeclaredMethod("get"
						+ StringUtils.capitalize(parentFieldName), new Class[] {});
				
				// get the old value for the field specified using reflection
				 parentOldValue = (String) getParentValueMethod.invoke(csfForm.getClonedForm(),
						new Object[] {});
				
				// get the new value for the field specified using reflection			
				 parentNewValue = (String) getParentValueMethod.invoke(csfForm, new Object[] {});
				
			}
		
			if(parentOldValue != null && parentNewValue!= null &&
					!StringUtils.equals(parentOldValue, parentNewValue) &&
					(CsfConstants.CSF_NO.equals(parentNewValue) 
							|| CsfConstants.CSF_FALSE.equals(parentNewValue))){
				
				String defaultValue = getAttributeDefaultValue(parentCode, attributeCode);
				if( defaultValue!= null && !defaultValue.equals(oldValue)){
					csf.addAttribute(attributeCode,defaultValue);
					changed = true;
				}
				
			}else if (!StringUtils.equals(newValue, oldValue)) {
				csf.addAttribute(attributeCode, formatAmount(newValue));
				changed = true;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return changed;
	}


	/**
	 * Formats the String by stripping the comma from the String
	 * 
	 * if 2,000 is passed, returns 2000
	 * 
	 * @param amount
	 * @return formated amount as String
	 * @throws NumberFormatException
	 */
	private static String formatAmount(String amount) 
	throws NumberFormatException {
		
		String amt = amount.trim();
		int index = amt.indexOf(',');
		if(index != -1)
		{
			if(index < 1 || index > 2)
				return null;
			else
				amt = amt.substring(0,index) + amt.substring(index+1);
		}
		return amt;
	}
	/**
	 * Method to determine CSF is in Standard view or Customized view
	 * @param csfForm
	 * @return
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public  boolean  isStandardView(CsfForm  csfForm, int contractId) throws SystemException{
		boolean isStandardView = true;
		
		Map <String, String> attributeMap = new HashMap<String, String>();
		
		//Applying standard view logic, if only parent service feature is on
		
		if(CsfConstants.CSF_YES.equals(csfForm.getAutoContributionIncrease())){
			attributeMap.put(ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE +"|"+ ServiceFeatureConstants.ACI_INCREASE_NEW_ENROLLES_ANNIVERSARY, CsfConstants.FIELD_INCREASE_ANNIVERSARY);
		}
		
		if(!CsfConstants.PLAN_VESTING_NA.equals(csfForm.getVestingPercentagesMethod())){
			attributeMap.put(ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE +"|"+ ServiceFeatureConstants.VESTING_PERCENTAGE_ON_STATEMENT, CsfConstants.FIELD_VESTING_DATA_ON_STATEMENT);
		}
		
		attributeMap.put(ServiceFeatureConstants.MANAGING_DEFERRALS +"|"+ ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE, CsfConstants.FIELD_PAYROLL_CUTOFF);
		
		if(CsfConstants.CSF_YES.equals(csfForm.getChangeDeferralsOnline())
				&& (ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(csfForm.getAciSignupMethod())
						|| ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(csfForm.getAciSignupMethod()))){
			if("%".equals(csfForm.getDeferralType()) 
					|| "E".equals(csfForm.getDeferralType())){
				attributeMap.put(ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE +"|"+ ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_PERCENT, CsfConstants.FIELD_DEFERRAL_LIMIT_PERCENT);
				attributeMap.put(ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE +"|"+ ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_PERCENT, CsfConstants.FIELD_DEFERRAL_MAX_LIMIT_PERCENT);
			}
			if("$".equals(csfForm.getDeferralType()) 
					|| "E".equals(csfForm.getDeferralType())){
				attributeMap.put(ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE +"|"+ ServiceFeatureConstants.ACI_DEFAULT_ANNUAL_INCREASE_BY_AMOUNT, CsfConstants.FIELD_DEFERRAL_LIMIT_DOLLARS);
				attributeMap.put(ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE +"|"+ ServiceFeatureConstants.ACI_DEFAULT_DEFERRAL_LIMIT_BY_AMOUNT, CsfConstants.FIELD_DEFERRAL_MAX_LIMIT_DOLLARS);
			}
		}
		
		if(csfForm.getParticipantServicesData().getIsJHdoesLoanRK()
				&& CsfConstants.CSF_YES.equals(csfForm.getAllowOnlineLoans())){
			attributeMap.put(ServiceFeatureConstants.ALLOW_LOANS_FEATURE +"|"+ ServiceFeatureConstants.WHO_WILL_REVIEW_LOANS, CsfConstants.FIELD_WHO_WILL_REVIEW_LOANS);
			attributeMap.put(ServiceFeatureConstants.ALLOW_LOANS_FEATURE +"|"+ ServiceFeatureConstants.CREATOR_OF_LOANREQUEST_APPROVE, CsfConstants.FIELD_CREATOR_APPROVE_LOANS);
			attributeMap.put(ServiceFeatureConstants.ALLOW_LOANS_FEATURE +"|"+ ServiceFeatureConstants.ALLOW_LOANS_PACKAGE_TO_GENERATE, CsfConstants.FIELD_ALLOW_LOANS_PKG_GENERATE);
		}
		
		if(CsfConstants.CSF_YES.equals(csfForm.getWithdrawalInd())){
			attributeMap.put(ServiceFeatureConstants.IWITHDRAWALS_FEATURE +"|"+ ServiceFeatureConstants.WHO_WILL_REVIEW_WITHDRAWALS, CsfConstants.FIELD_WHO_WILL_REVIEW_WITHDRAWALS);
			attributeMap.put(ServiceFeatureConstants.IWITHDRAWALS_FEATURE +"|"+ ServiceFeatureConstants.IWITHDRAWALS_APPROVE, CsfConstants.FIELD_APPROVE_WITHDRAWALS);
		}
		
		Iterator it = attributeMap.entrySet().iterator();
		
		while (it.hasNext() && isStandardView) {
			Map.Entry pairs = (Map.Entry)it.next();
			isStandardView = checkCsfDefaultValue(csfForm, (String)pairs.getKey(), (String)pairs.getValue(), contractId );
		}
		
		return isStandardView;
	}
	/**
	 * Method to compare the CSF values with the default CSF values to determine whether CSF page 
	 * is in Customized view or Standard View.  
	 * 
	 * @param csfForm
	 * @param fieldName
	 * @param csfAttrCode
	 * @return
	 */
	private  boolean checkCsfDefaultValue(CsfForm csfForm, String csfAttrCode, String fieldName, Integer contractId){
		boolean isEqual = false;
		
		//reflection to create the method name based on the fieldName
		Method getFieldValueMethod;
		try {
			getFieldValueMethod = CsfForm.class.getDeclaredMethod("get"
					+ StringUtils.capitalize(fieldName), new Class[] {});
			
			// get the new value for the field specified using reflection			
			String attrValue = (String) getFieldValueMethod.invoke(csfForm, new Object[] {});
			
			if (isValueEitherYesOrTrue(attrValue)) {
				attrValue =  ServiceFeatureConstants.YES;
			} else if (CsfConstants.CSF_NO.equals(attrValue) 
					|| CsfConstants.CSF_FALSE.equals(attrValue)) {
				attrValue =  ServiceFeatureConstants.NO;
			} 
			String defaultValue = null;
			
			if (csfAttrCode.endsWith("|"+ ServiceFeatureConstants.VESTING_PERCENTAGE_ON_STATEMENT)){
				defaultValue = ServiceFeatureConstants.YES;
			} else	if(csfAttrCode.endsWith("|"+ ServiceFeatureConstants.WHO_WILL_REVIEW_LOANS)){
				// check if the contract has TPA firm assigned
				TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(contractId);
				if (firmInfo != null) {
					defaultValue = CsfConstants.WHO_WILL_REVIEW_WD_TPA;
				} else {
					defaultValue = CsfConstants.WHO_WILL_REVIEW_WD_PS;
				}
			}else{
				defaultValue = getAttributeDefaultValue(csfAttrCode);
			}
			
			if (attrValue == null) {
				isEqual =  defaultValue == null;
			} else {
				isEqual =  attrValue.equals(defaultValue);
			}
			
		}  catch (Exception e) {
			logger.error(e);
		}

		return isEqual;
	}
	
	/**
	 * 
	 * @param attrCode
	 * @return
	 * @throws SystemException
	 */
	public static String getAttributeDefaultValue(String serviceCode, String attrCode)throws SystemException{
		ServiceFeatureLookup defaultLookup = null;
		try {
			defaultLookup = ServiceFeatureLookup.getInstance();
		} catch (ServiceFeatureLookupException e) {
			 throw new UnrecoverableEventException(e, 
     		"ServiceFeatureLookupException occured trying to get an instance of ServiceFeatureLookup");

		}
		String defaultValue = null;
		if(defaultLookup != null){
			defaultValue =  defaultLookup.getDefaultServiceFatureAttrValue(serviceCode, attrCode);
		}

		if(ServiceFeatureConstants.CSF_NON_DEFAULT_VALUE.equals(defaultValue)){
			return null;
		}
		return defaultValue;
	}
	/**
	 * 
	 * @param attrCode
	 * @return
	 * @throws SystemException
	 */
	public static String getAttributeDefaultValue(String serviceCode)throws SystemException{
		ServiceFeatureLookup defaultLookup = null;
		try {
			defaultLookup = ServiceFeatureLookup.getInstance();
		} catch (ServiceFeatureLookupException e) {
			 throw new UnrecoverableEventException(e, 
     		"ServiceFeatureLookupException occured trying to get an instance of ServiceFeatureLookup");

		}
		String defaultValue = null;
		if(defaultLookup != null){
			defaultValue =  defaultLookup.getDefaultServiceFatureValue(serviceCode);
		}

		if(ServiceFeatureConstants.CSF_NON_DEFAULT_VALUE.equals(defaultValue)){
			return null;
		}
		return defaultValue;
	}
	/**
	 * Method to handle the IWithdrawals once save button is 
	 * clicked in the CSF edit page 
	 * 
	 * @param request
	 * @param newForm
	 * @param oldForm
	 * @param changedCsfCollection
	 * @throws SystemException
	 */
	private void handleWithdrawals(final HttpServletRequest request, 
			final CsfForm newForm, final CsfForm oldForm, 
			final Collection<ContractServiceFeature> changedCsfCollection, 
			Collection<GenericException> warnings, boolean isConfirmed) throws SystemException {
		
		for (ContractServiceFeature newFeature : changedCsfCollection) {
			if (StringUtils.equals(newFeature.getName(),
					ServiceFeatureConstants.IWITHDRAWALS_FEATURE)) {
				
				List<String> features = new ArrayList<String>();
				features.add(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
				
				String oldIndicator1 = oldForm.getWithdrawalInd();
				String newIndicator1 = newForm.getWithdrawalInd();
				String oldIndicator2 = oldForm.getOnlineWithdrawalProcess();
				String newIndicator2 = newForm.getOnlineWithdrawalProcess();
				String oldIndicator3 = oldForm.getParticipantWithdrawalInd();
				String newIndicator3 = newForm.getParticipantWithdrawalInd();
				
				boolean oldValue1 = isValueEitherYesOrTrue(oldIndicator1);
				boolean newValue1 = isValueEitherYesOrTrue(newIndicator1);
				boolean oldValue2 = isValueEitherYesOrTrue(oldIndicator2);
				boolean newValue2 = isValueEitherYesOrTrue(newIndicator2);
				boolean oldValue3 = isValueEitherYesOrTrue(oldIndicator3);
				boolean newValue3 = isValueEitherYesOrTrue(newIndicator3);
				
				int contractId = newFeature.getContractId();
				int profileId = (int) getUserProfile(request).getPrincipal().getProfileId();
				
				if(isConfirmed){
					if (newValue1 && !oldValue1) {
						WithdrawalServiceDelegate.getInstance().handleEnableOnlineWithdrawals(
								newFeature.getContractId(), 
								(int) getUserProfile(request).getPrincipal().getProfileId());
					}
					
					if (!newValue1 && oldValue1) {
						WithdrawalServiceDelegate.getInstance().handleDisableOnlineWithdrawals(
								contractId,
								profileId,WithdrawalRequest.CMA_SITE_CODE_PSW);
					}
					
					if (!newValue2 && oldValue2) {
						WithdrawalServiceDelegate.getInstance().handleEnableOneStepApprovals(
								contractId,
								getUserProfile(request).getPrincipal());
					}
					
					if (newValue2 && !oldValue2) {
						WithdrawalServiceDelegate.getInstance().handleEnableTwoStepApprovals(
								contractId, profileId);
					}
					
					if(!newValue3 && oldValue3) {
						WithdrawalServiceDelegate.getInstance().handleDisableOnlineWithdrawals(
								contractId,
								profileId, WithdrawalRequest.CMA_SITE_CODE_EZK);
					}
				}
				String oldValue5 = oldForm.getWhoWillReviewWithdrawals();
				String newValue5 = newForm.getWhoWillReviewWithdrawals();
				String usersList = CsfConstants.EMPTY_STRING;
				
				try {
					if (!(oldValue1 && newValue1) && newValue5 != null) {
						String permissionCode = PermissionType.getPermissionCode(
								PermissionType.REVIEW_WITHDRAWALS);

						if (CsfConstants.WHO_WILL_REVIEW_WD_TPA.equals(newValue5)) { // 2936
							// check if there is at least one client user with review permission, If yes then don't do anything
							List <Long> clientUsersWithReviewWithdrawals = retrieveClientUsersByPermission(contractId, permissionCode);

							if (CollectionUtils.isNotEmpty(clientUsersWithReviewWithdrawals)) {
								// grant review permissions to all those users who can have
								// give a message for those users that we couldn't  remove the permission
								usersList = removeReviewPermissionForClientUsers(getUserProfile(request), permissionCode, clientUsersWithReviewWithdrawals, isConfirmed);
								if(!isConfirmed && StringUtils.isNotEmpty(usersList)){
									warnings.add(new ValidationError(new String[] {CsfConstants.EMPTY_STRING},ErrorCodes.WARNING_IWITHDRAWALS_PERMISSION_COULD_NOT_BE_TAKEN,new String[] { usersList }, ValidationError.Type.warning));
								}
							}
							
						} //REMOVED REQUIREMENT: 
						/*else if (CsfConstants.WHO_WILL_REVIEW_WD_PS.equals(newValue5)) { //2934
							// check if there is at least one client user with review permission, If yes then don't do anything
							List <Long> clientUsersWithReviewIWithdrawals = retrieveClientUsersByPermission(contractId, permissionCode);
							
							// If there is not even one client user with review permission
							if (CollectionUtils.isEmpty(clientUsersWithReviewIWithdrawals)) {
								// grant review permissions to all those users who can have
								usersList = grantReviewPermissionForClientUsers(getUserProfile(request), permissionCode);
								warnings.add(new ValidationError(new String[] {CsfConstants.EMPTY_STRING},ErrorCodes.WARNING_IWITHDRAWALS_REVIEW_PERMISSION_COULD_NOT_BE_GRANTED,new String[] { usersList }, ValidationError.Type.warning));
							}
						}*/
					} else if (oldValue5 != null && newValue5 != null &&
							((!StringUtils.equals(oldValue5, newValue5))) || (!(oldValue1 && newValue1))) {
						
						// WHO WILL REVIEW WITHDRAWAL CSF change
						String permissionCode = PermissionType.getPermissionCode(PermissionType.REVIEW_WITHDRAWALS);
						
						
						//REMOVED REQUIREMENT: if the value changes from 'TPA' to 'PS' or if the value changes from 'No Review' to 'PS'
						/*if (CsfConstants.WHO_WILL_REVIEW_WD_PS.equals(newValue5)) { // 2934
							// check if there is at least one client user with review permission, If yes then don't do anything
							List <Long> clientUsersWithReviewIWithdrawals = retrieveClientUsersByPermission(contractId, permissionCode);
							// If there is not even one client user with review permission
							if (CollectionUtils.isEmpty(clientUsersWithReviewIWithdrawals)) {
								// grant review permissions to all those users who can have
								usersList = grantReviewPermissionForClientUsers(getUserProfile(request), permissionCode);
								warnings.add(new ValidationError(new String[] {CsfConstants.EMPTY_STRING},ErrorCodes.WARNING_IWITHDRAWALS_REVIEW_PERMISSION_COULD_NOT_BE_GRANTED,new String[] { usersList }, ValidationError.Type.warning));
							}
						}*/
						// if the value changes from 'PS' to 'TPA' or 'No Review'
						 if (CsfConstants.WHO_WILL_REVIEW_WD_PS.equals(oldValue5)) {
							if (! CsfConstants.WHO_WILL_REVIEW_WD_NOREVIEW.equals(newValue5)) { // 2936
								// remove the review permission from all the client users
								// check if there is at least one client user with review permission, If no, then don't do anything
								List <Long> clientUsersWithReviewIWithdrawals = retrieveClientUsersByPermission(contractId, permissionCode);
								// If there are client users with review permission
								if (CollectionUtils.isNotEmpty(clientUsersWithReviewIWithdrawals)) {
									// remove review permissions to all those users who can have
									// give a msg for those users that we couldn't  remove the permission
									usersList = removeReviewPermissionForClientUsers(getUserProfile(request), permissionCode, clientUsersWithReviewIWithdrawals, isConfirmed);
									if(!isConfirmed && StringUtils.isNotEmpty(usersList)){
										warnings.add(new ValidationError(new String[] {CsfConstants.EMPTY_STRING},ErrorCodes.WARNING_IWITHDRAWALS_PERMISSION_COULD_NOT_BE_TAKEN,new String[] { usersList }, ValidationError.Type.warning));
									}
								}
							}
						}
					}
				} catch (SecurityServiceException e) {
					throw new SystemException(e,
							"Failed to set review withdrawal permissions to client users in handleWithdrawals() :"
							+ e.toString());
				}

			}
		}
	}
	
	/**
	 * method that helps removing a specific (review withdrawal/loan) permission
	 * to all the client users that has the permission for that contract
	 * 
	 * @param profile
	 * @param permissionCode
	 * @param clientUsers
	 * @return
	 * @throws SecurityServiceException
	 * @throws SystemException
	 */
	private String removeReviewPermissionForClientUsers(UserProfile profile,
			String permissionCode, List<Long> clientUsers, boolean isConfirmed)
	throws SecurityServiceException, SystemException {
		
		StringBuffer lockedUserNames = new StringBuffer();
		SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
		Principal principal = profile.getPrincipal();
		boolean isReviewWithdrawalPermission = false;
		boolean isReviewLoanPermission = false;
		
		if (permissionCode.equals(PermissionType
				.getPermissionCode(PermissionType.REVIEW_WITHDRAWALS))) {
			isReviewWithdrawalPermission = true;
		} else if (permissionCode.equals(PermissionType
				.getPermissionCode(PermissionType.REVIEW_LOANS))) {
			isReviewLoanPermission = true;
		}
		
		int contractNumber = profile.getCurrentContract().getContractNumber();
		
		for (Long profileId : clientUsers) {
			UserInfo userInfo = 
				SecurityServiceDelegate.getInstance().searchByProfileId(principal, profileId);
			// removing other contracts permissions from the UserInfo method
			// other than the current contract related one. 
			// SecurityService.updateUser() method saves all the 
			// permissions irrespective of whether there is a change or not
			// and its not required in this scenario
			if (userInfo != null) {
				ContractPermission currentContractPermission = 
					userInfo.getContractPermission(contractNumber);

				/*
				 * Remove all contract permission objects from the UserInfo
				 * object.
				 */
				userInfo.resetContractPermissions();

				/*
				 * Add back the contract permission that we are going to update.
				 */
				userInfo.addContractPermission(currentContractPermission);

				String componentKey = LockHelper.USER_PROFILE_LOCK_NAME + profileId;
				if (LockServiceDelegate.getInstance().lock(
						LockHelper.USER_PROFILE_LOCK_NAME, componentKey,
						principal.getProfileId())) {

					if (isReviewWithdrawalPermission) {
						userInfo.getContractPermission(contractNumber).setReviewIWithdrawals(false);
					} else if (isReviewLoanPermission) {
						userInfo.getContractPermission(contractNumber).setReviewLoans(false);
					}

					if(isConfirmed){
						service.updateUser(principal, 
								Environment.getInstance().getSiteLocation(), userInfo);
					}
					LockServiceDelegate.getInstance().releaseLock(
							LockHelper.USER_PROFILE_LOCK_NAME, componentKey);
				} else {
					lockedUserNames.append("\\n").append(
							userInfo.getFirstName()).append(" ").append(
									userInfo.getLastName());
				}
			}
		}
		return lockedUserNames.toString();
	}

	/**
	 * method that helps granting review withdrawal/loan permission 
	 * to all the client users that belongs to that contract.
	 * 
	 * 
	 * @param profile
	 * @param permissionCode
	 * @return
	 * @throws SecurityServiceException
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	private String grantReviewPermissionForClientUsers(UserProfile profile,
			String permissionCode, boolean isConfirmed) throws SecurityServiceException, SystemException {
	
		int contractNumber = profile.getCurrentContract().getContractNumber();
		
		UserSearchCriteria sc1 = new UserSearchCriteria();
		sc1.setContractId(contractNumber);
		sc1.setSearchCriteria(UserSearchCriteria.SEARCH_BY_CONTRACT_NUMBER);
		sc1.setSearchObject(String.valueOf(contractNumber));

		SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
		Principal principal = profile.getPrincipal();
		
		boolean isReviewWithdrawalPermission = false;
		boolean isReviewLoanPermission = false;
		
		if (permissionCode.equals(PermissionType
				.getPermissionCode(PermissionType.REVIEW_WITHDRAWALS))) {
			isReviewWithdrawalPermission = true;
		} else if (permissionCode.equals(PermissionType
				.getPermissionCode(PermissionType.REVIEW_LOANS))) {
			isReviewLoanPermission = true;
		}
		
		List<UserInfo> clientUsers = SecurityServiceDelegate.getInstance().searchUser(
					profile.getPrincipal(), sc1);
		
		StringBuffer lockedUserNames = new StringBuffer();
		for (UserInfo clientUser : clientUsers) {
			if (!clientUser.getProfileStatus().equalsIgnoreCase("deleted")) {
				UserInfo userInfo = 
					SecurityServiceDelegate.getInstance().searchByUserName(
							profile.getPrincipal(), clientUser.getUserName());
				
				// removing other contracts permissions from the UserInfo method
				// other than the current contract related one. 
				// SecurityService.updateUser() method saves all the 
				// permissions irrespective of whether there is a change or not
				// and its not required in this scenario
				if (userInfo != null) {
					ContractPermission currentContractPermission =  userInfo.getContractPermission(contractNumber);
					userInfo.resetContractPermissions();
					userInfo.addContractPermission(currentContractPermission);					
				}
				
				UserRole role = userInfo.getContractPermission(contractNumber).getRole();
				
				// get the default permission value for the user
				String defaultPermissionValue = CsfConstants.EMPTY_STRING;
				if (isReviewWithdrawalPermission) {
					defaultPermissionValue = role.getDefaultRolePermissions().getDefaultPermissionValue(
							PermissionType.REVIEW_WITHDRAWALS);
				} else if (isReviewLoanPermission) {
					defaultPermissionValue = role.getDefaultRolePermissions().getDefaultPermissionValue(
							PermissionType.REVIEW_LOANS);
				}
				
				// if the role is allowed to have the permission then proceed 
				if (DefaultRolePermissions.YES.equals(defaultPermissionValue)
						|| DefaultRolePermissions.TRUE
						.equals(defaultPermissionValue)) {
					String componentKey = LockHelper.USER_PROFILE_LOCK_NAME 
					+ userInfo.getProfileId();
					
					if (LockServiceDelegate.getInstance().lock(
							LockHelper.USER_PROFILE_LOCK_NAME, componentKey,
							principal.getProfileId())) {
						if (isReviewWithdrawalPermission) {
							userInfo.getContractPermission(contractNumber)
							.setReviewIWithdrawals(true);
						} else if (isReviewLoanPermission) {
							userInfo.getContractPermission(contractNumber)
							.setReviewLoans(true);
						}
						if(isConfirmed){
							service.updateUser(principal, Environment.getInstance()
									.getSiteLocation(), userInfo);
						}
						LockServiceDelegate.getInstance()
						.releaseLock(LockHelper.USER_PROFILE_LOCK_NAME,
								componentKey);
					} else {
						lockedUserNames.append("\\n").append(
								clientUser.getFirstName()).append(" ").append(
										clientUser.getLastName());
					}
				}
			}
		}
		return lockedUserNames.toString();
	}

	/**
	 * Handles the TPA firm permissions for loans
	 * 
	 * @param request
	 * @param newForm
	 * @param firmInfo
	 * @throws SecurityServiceException
	 * @throws SystemException
	 */
	private void handleTPAFirmPermissionsForLoans(HttpServletRequest request, 
			CsfForm newForm, TPAFirmInfo firmInfo) 
	throws SecurityServiceException, SystemException {
		
		CsfForm oldForm = (CsfForm) newForm.getClonedForm();
		String oldValue = oldForm.getWhoWillReviewLoanRequests();
		String newValue = newForm.getWhoWillReviewLoanRequests();

		String oldIndicator1 = oldForm.getAllowOnlineLoans();
		String newIndicator1 = newForm.getAllowOnlineLoans();
		boolean oldValue1 = isValueEitherYesOrTrue(oldIndicator1);
		boolean newValue1 = isValueEitherYesOrTrue(newIndicator1);		

		SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
		UserProfile userProfile = getUserProfile(request);

		if (!(oldValue1 && newValue1)) {
			if (CsfConstants.WHO_WILL_REVIEW_WD_TPA.equals(newValue)) {
				// If not locked remove Initiate/review permissions for the TPA Firm
				ContractPermission permissions = firmInfo.getContractPermission();
				permissions.setReviewLoans(true);
				permissions.setInitiateLoans(true);
				firmInfo.setContractPermission(permissions);
				service.updateTpaFirm(userProfile.getPrincipal(), firmInfo);
			}
			else if (CsfConstants.WHO_WILL_REVIEW_WD_PS.equals(newValue)) {
				// remove the TPA firm permissions
				// If not locked remove Initiate/review permissions for the TPA Firm
				ContractPermission permissions = firmInfo.getContractPermission();
				if (permissions.isReviewLoans() || permissions.isInitiateLoans()) {
					permissions.setReviewLoans(false);
					permissions.setInitiateLoans(false);
					firmInfo.setContractPermission(permissions);
					service.updateTpaFirm(userProfile.getPrincipal(), firmInfo);
				}
			}

		} else if (oldValue != null && newValue != null && !StringUtils.equals(oldValue, newValue)) {
			// WHO WILL REVIEW Loans CSF change
			// if the value changes from from 'TPA' to 'Plan Sponsor'
			if (CsfConstants.WHO_WILL_REVIEW_WD_TPA.equals(oldValue)) {
				// If not locked remove Initiate/review permissions for the TPA Firm
				ContractPermission permissions = firmInfo.getContractPermission();
				permissions.setReviewLoans(false);
				permissions.setInitiateLoans(false);
				firmInfo.setContractPermission(permissions);
				service.updateTpaFirm(userProfile.getPrincipal(), firmInfo);
			}

			// if the value changes from from 'Plan Sponsor' to 'TPA'
			if (CsfConstants.WHO_WILL_REVIEW_WD_TPA.equals(newValue)) {
				// If not locked remove Initiate/review permissions for the TPA Firm
				ContractPermission permissions = firmInfo.getContractPermission();
				permissions.setReviewLoans(true);
				permissions.setInitiateLoans(true);
				firmInfo.setContractPermission(permissions);
				service.updateTpaFirm(userProfile.getPrincipal(), firmInfo);
			}
		}
	}
	
	/**
	 * Sets the User info
	 * 
	 * @param csf
	 * @param principal
	 */
	private void setCSFUserInfo(ContractServiceFeature csf, 
			Principal principal) {
		
		csf.setUserName(principal.getUserName());
		csf.setPrincipalProfileId(principal.getProfileId());
		csf.setPrincipalFirstName(principal.getFirstName());
		csf.setPrincipalLastName(principal.getLastName());
		
		if(principal.getRole().isExternalUser()) {
			csf.setUserIdType(Constants.EXTERNAL_USER_ID_TYPE);
		} else {
			csf.setUserIdType(Constants.INTERNAL_USER_ID_TYPE);
		}
		
		csf.setSourceChannelCode(Constants.PS_SOURCE_CHANNEL_CODE);
	}
	
	/**
	 * Method to populate the selected Money type values to form bean
	 * @param CsfForm csfForm
	 * @return
	 */
	private void populateSelectedMoneyTypeValues(CsfForm csfForm){
		String[] selectedMoneyTypes = csfForm.getSelectedMoneyTypes();
		ArrayList<EligibilityCalculationMoneyType> eligibilityServiceMoneyTypeList = 
			csfForm.getEligibilityServiceMoneyTypes();
		
		List<String> ss = Arrays.asList(selectedMoneyTypes);
		
		if (eligibilityServiceMoneyTypeList != null) {
			int totalNumberOfMoneyTypes = eligibilityServiceMoneyTypeList.size();
			for (int i = 0; i < totalNumberOfMoneyTypes; i++) {
				EligibilityCalculationMoneyType currentECalcMoneyType = csfForm
						.getEligibilityServiceMoneyTypes().get(i);
				if(ss.contains(currentECalcMoneyType.getMoneyTypeShortName())){
					currentECalcMoneyType.setMoneyTypeValue(CsfConstants.CSF_YES);
				}else{
					currentECalcMoneyType.setMoneyTypeValue(CsfConstants.CSF_NO);
				}
			}
		}
	}
	/**
     * This method gets the changed EC csf values.
     * 
     * @param contractId
     * @param csfForm
     * @param principal
     * @param changedFieldList
     * @param changedCsfCollection
     * @return
     */
    public Collection<ContractServiceFeature>  getChangedECcsfValues(
    		int contractId, CsfForm csfForm, Principal principal, 
            Collection<ContractServiceFeature> changedCsfCollection ) {
    	
    	 // Check eligiblity calculation values
		ContractServiceFeature eligiblityCSF = new ContractServiceFeature();
		checkCsfValue(csfForm, CsfConstants.FIELD_ELIGIBLITY_CALCULATION_IND,
				eligiblityCSF);
		
		populateSelectedMoneyTypeValues(csfForm);
		
		ArrayList<EligibilityCalculationMoneyType> eligibilityServiceMoneyTypeList = 
			csfForm.getEligibilityServiceMoneyTypes();

		if (eligibilityServiceMoneyTypeList != null) {
			int totalNumberOfMoneyTypes = 
				eligibilityServiceMoneyTypeList.size();
			
			/*Iterator<EligibilityCalculationMoneyType> eligibilityServiceMoneyTypesIterator = 
				eligibilityServiceMoneyTypeList.iterator();
			
			ArrayList<EligibilityCalculationMoneyType> lastUpdatedMoneyTypeList = 
				csfForm.getLastUpdatedEligibilityServiceMoneyTypes();
			
			Iterator<EligibilityCalculationMoneyType> lastUpdatedEligibilityServiceIterator = 
				lastUpdatedMoneyTypeList.iterator();*/

			for (int i = 0; i < totalNumberOfMoneyTypes; i++) {
				EligibilityCalculationMoneyType currentECalcMoneyType = csfForm
						.getEligibilityServiceMoneyTypes().get(i);
				EligibilityCalculationMoneyType lastUpdatedECalcMoneyType = csfForm
						.getLastUpdatedEligibilityServiceMoneyTypes()
						.get(i);
				//String propertyName="eligibityServiceMoneyTypeId["+i+"].moneyTypeValue";
				// CSF 353- If the Eligibility Calculation service feature
				// is changed to 'No', eligibility
				// calculation for all money types will automatically be
				// turned off.

				if (!StringUtils.isEmpty(eligiblityCSF.getValue())
						&& eligiblityCSF.getValue().equalsIgnoreCase(Constants.CSF_NO_CODE)) {
					String oldValue = lastUpdatedECalcMoneyType
							.getMoneyTypeValue();
					if (CsfConstants.CSF_YES.equals(oldValue)) {
						eligiblityCSF.addAttribute(currentECalcMoneyType
								.getMoneyTypeName(),
								ServiceFeatureConstants.NO);
						currentECalcMoneyType
								.setMoneyTypeValue(CsfConstants.CSF_NO);
						
						
					}
				} else if(CsfConstants.CSF_YES.equalsIgnoreCase(csfForm.getEligibilityCalculationInd())){
					String oldValue = lastUpdatedECalcMoneyType
							.getMoneyTypeValue();
					String newValue = currentECalcMoneyType
							.getMoneyTypeValue();

					if (!StringUtils.equals(newValue, oldValue)) {
						if (isValueEitherYesOrTrue(newValue)) {
							eligiblityCSF.addAttribute(
									currentECalcMoneyType
											.getMoneyTypeName(),
									ServiceFeatureConstants.YES);

						} else if (CsfConstants.CSF_NO.equals(newValue)
								|| CsfConstants.CSF_FALSE.equals(newValue)) {
							eligiblityCSF.addAttribute(
									currentECalcMoneyType
											.getMoneyTypeName(),
									ServiceFeatureConstants.NO);
						} else {
							eligiblityCSF.addAttribute(
									currentECalcMoneyType
											.getMoneyTypeName(), newValue);
						}
					}
				}
			}
		}
		if (!StringUtils.isEmpty(eligiblityCSF.getValue())
				|| eligiblityCSF.getAttributeMap().size() > 0) {
			eligiblityCSF.setContractId(contractId);
			eligiblityCSF
					.setName(ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);
			setCSFUserInfo(eligiblityCSF, principal);
			changedCsfCollection.add(eligiblityCSF);
		}
		return changedCsfCollection;
    }
    
    /**
     * retruns the eligibility service money types
     * 
     * @param csfForm
     * @param contractId
     * @param csfMap
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
	private void getEligibilityServiceMoneyTypes(
    		CsfForm csfForm, int contractId, Map csfMap)throws SystemException{
    	
    	if(csfMap==null){
    		ContractServiceDelegate service = ContractServiceDelegate.getInstance();
    		try {
    			csfMap = service.getContractServiceFeatures(contractId);
    		} catch (ApplicationException ae) {
    			throw new SystemException(ae, "Exception in " 
    					+ "CsfDataHelper.loadContractServiceFeatureData() " 
    					+ ae.getDisplayMessage());
    		}
    	}
    	
		ContractServiceFeature eligibilityCalculationCSF = 
			(ContractServiceFeature) csfMap.get(
					ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);
		
    	ArrayList<EligibilityCalculationMoneyType> eligibilityCalculationMoneyTypeList = 
    		new ArrayList<EligibilityCalculationMoneyType>();
    	
    	if(csfForm.getMoneyTypeEligibilityCriteria()!= null){
    		
      		Iterator<MoneyTypeEligibilityCriterion> contractMoneyTypeIterator =
      			csfForm.getMoneyTypeEligibilityCriteria().iterator();
      		
      		while(contractMoneyTypeIterator.hasNext()){
      			   MoneyTypeEligibilityCriterion moneyTypeEligibilityCriterion=(MoneyTypeEligibilityCriterion)contractMoneyTypeIterator.next();
      		       EligibilityCalculationMoneyType eligibiltyCalculationMoneyType=new EligibilityCalculationMoneyType();
      				//Set Attribute name
      				eligibiltyCalculationMoneyType.setMoneyTypeName(moneyTypeEligibilityCriterion.getMoneyTypeId().trim());
      				eligibiltyCalculationMoneyType.setMoneyTypeShortName(moneyTypeEligibilityCriterion.getContractMoneyTypeShortName());
      				//Set Attribute value
      				String attributeValue=eligibilityCalculationCSF.getAttributeValue(eligibiltyCalculationMoneyType.getMoneyTypeName().trim());
      				eligibiltyCalculationMoneyType.setMoneyTypeValue(ContractServiceFeature.internalToBoolean(attributeValue).booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
      				
      				eligibiltyCalculationMoneyType.setCsfValue(ContractServiceFeature.internalToBoolean(attributeValue).booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);
      				
      				//Set Attribute description
      				eligibiltyCalculationMoneyType.setMoneyTypeDescription(moneyTypeEligibilityCriterion.getContractMoneyTypeLongName()+" ("+moneyTypeEligibilityCriterion.getContractMoneyTypeShortName()+")");
      				eligibilityCalculationMoneyTypeList.add(eligibiltyCalculationMoneyType);
      		}
      		populateCheckBoxValues(csfForm);
    		//set the eligible money type list in the form.
      		csfForm.setEligibilityServiceMoneyTypes(eligibilityCalculationMoneyTypeList);
    		//keep a copy of the money type values before it gets modified in the form, to
    		//identify the updated values while saving.
      		csfForm.setLastUpdatedEligibilityServiceMoneyTypes(csfForm.copyLastUpdatedEligibityServiceMoneyTypes(eligibilityCalculationMoneyTypeList));
      	    //This list size is used for script validations
    		csfForm.setEligibilityServiceMoneyTypesListSize(eligibilityCalculationMoneyTypeList.size());	
    	 }
    }
    
    /**
     * Method to populate the check boxes
     * @param csfForm
     */
    public void populateCheckBoxValues(CsfForm csfForm){

    	String[] selectedMoneyTypes = new String[CsfConstants.DEFAULT_EC_MONEY_TYPES_SIZE];
    	int i=0;

    	if(csfForm.getEligibilityServiceMoneyTypes ()!= null){

    		Iterator<EligibilityCalculationMoneyType> contractMoneyTypeIterator =
    			csfForm.getEligibilityServiceMoneyTypes().iterator();

    		while(contractMoneyTypeIterator.hasNext()){
    			EligibilityCalculationMoneyType eligibiltyCalculationMoneyType = 
    				contractMoneyTypeIterator.next();
    			if(CsfConstants.CSF_YES.equals(eligibiltyCalculationMoneyType.getMoneyTypeValue())){
    				selectedMoneyTypes[i++] = eligibiltyCalculationMoneyType.getMoneyTypeShortName();
    			}
    		}
    	}
    	csfForm.setSelectedMoneyTypes(selectedMoneyTypes);
    }
    
    /**
     * formats the String
     * 		2000 = 2,000
     * 
     * @param amount
     * @return
     */
    private String formatAmountForDisplay(String amount)	{
		String amt = null;
		if(amount != null){
			amt = amount.trim();
			if(amt.length() > 3 && amt.indexOf(",") == -1){
				amt = amt.substring(0,amt.length()-3) + "," 
						+ amt.substring(amt.length()-3);
			}
		}
		return amt;
	}
    

    
    
	/**
	 * 
	 * @param amount
	 * @return
	 * @throws NumberFormatException
	 */
	private static Integer parseAmount(String amount) throws NumberFormatException{
		String amt = amount.trim();
		int index = amt.indexOf(',');
		if(index != -1)	{
			if(index < 1 || index > 2)
				return null;
			else
				amt = amt.substring(0,index) + amt.substring(index+1);
		}
		Integer am = Integer.valueOf(amt);
		return am;
	}

	/**
	 * Validates the contract anniversary date is in freeze period
	 * i.e;  [current date + new “Payroll cut off for online deferral and 
	 *  auto enrollment changes is”] is > = Contract’s next anniversary 
	 *  
	 * @param form
	 * @return boolean
	 */
	public boolean isFreezePeriod(CsfForm form, PlanDataLite planDataLite) {
		
		boolean isFreezePeriod = false;

		Date nextAnniv = getNextAnniversary(form, planDataLite);

		if(nextAnniv != null && StringUtils.isNotEmpty(form.getPayrollCutoff())) {
			try {
				int dba = Integer.parseInt(form.getPayrollCutoff());
				isFreezePeriod = ContractServiceFeatureUtil.isFreezePeriod(dba, nextAnniv, false);
			} catch(NumberFormatException ne) {
				// do nothing
			}
		}
		return isFreezePeriod;
	}
	
	/**
	 * Calculates the next anniversary date
	 * 
	 * @param form
	 * @return calculated anniversary date
	 */
	private static Date getNextAnniversary(CsfForm form, PlanDataLite planDataLite) {
		
		Date contractAnniversaryDate = null;
		Calendar cal = Calendar.getInstance();
		clearTimeOnCalendar(cal);
		
		if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(form.getAciSignupMethod())) {
			contractAnniversaryDate = getAnniversaryDateAuto(cal, form, planDataLite);
		} else if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(form.getAciSignupMethod())) {
			contractAnniversaryDate = getAnniversaryDateSignUp(cal, form, planDataLite);
		}
		return contractAnniversaryDate;
		
	}
	 
	/**
	 * clear the hour,minutes,seconds
	 * 
	 * @param cal
	 * @return
	 */
	private static Calendar clearTimeOnCalendar(Calendar cal) {
		if (cal != null) {
			cal.clear(Calendar.HOUR_OF_DAY);
			cal.clear(Calendar.HOUR);
			cal.clear(Calendar.MINUTE);
			cal.clear(Calendar.SECOND);
			cal.clear(Calendar.MILLISECOND);
		}
		return cal;
	}
	
	/**
	 * gets the anniversary date (sign up)
	 * @param cal
	 * @param planData
	 * @return Date
	 */
	private static Date getAnniversaryDateSignUp(
			 Calendar cal, CsfForm form, PlanDataLite planDataLite) {
		Date anniversaryDate = null;
		DayOfYear dayOfYear = planDataLite.getPlanYearEnd();
		if(dayOfYear == null) {
			return anniversaryDate;
		}
		Date sysDate = cal.getTime();
		int curntYear = cal.get(Calendar.YEAR);
		int planMonth = dayOfYear.getMonth();
		int planDay = dayOfYear.getDay();
		// non leap year
		cal.set(DUMMY_YEAR, (planMonth -1), (planDay + 1));
		cal.set(Calendar.YEAR, curntYear);
		if (cal.getTime().compareTo(sysDate) >= 0) {
			anniversaryDate = cal.getTime();
		} else {
			cal.set(Calendar.YEAR, (cal.get(Calendar.YEAR) + 1));
			anniversaryDate = cal.getTime();
		}
		return anniversaryDate;
	}
	
	/**
	 * gets the anniversary date (auto)
	 * @param cal
	 * @param planData
	 * @return Date
	 */
	private static Date getAnniversaryDateAuto(
			 Calendar cal, CsfForm form, PlanDataLite planDataLite) {
		
		Date anniversaryDate = null;
		DayOfYear dayOfYear = planDataLite.getAciAnnualApplyDate();
		
		int conAnnYear = 0;
		
		if(StringUtils.isNotBlank(form.getAciAnniversaryYear())
				&& StringUtils.isNumeric(form.getAciAnniversaryYear())){
			conAnnYear = Integer.parseInt(form.getAciAnniversaryYear());
		}
		
		if(dayOfYear == null || conAnnYear == 0) {
			return anniversaryDate;
		}
		Date sysDate = cal.getTime();
		int curntMonth = cal.get(Calendar.MONTH) + 1;
		int curntDay = cal.get(Calendar.DAY_OF_MONTH);
		int curntYear = cal.get(Calendar.YEAR);
		int planMonth = dayOfYear.getMonth();
		int planDay = dayOfYear.getDay();
		
		Calendar contractAnniversaryDate = Calendar.getInstance();
		cal.set(DUMMY_YEAR, (planMonth-1), planDay);
		cal.set(Calendar.YEAR, conAnnYear);

		if ((cal.getTime().compareTo(sysDate) < 0)
				&& ((planMonth < curntMonth) || ((planMonth == curntMonth) && (planDay < curntDay)))) {
			cal.set(Calendar.YEAR, curntYear + 1);
		} else if ((cal.getTime().compareTo(sysDate) < 0)
				&& ((planMonth > curntMonth) || ((planMonth == curntMonth) && (planDay >= curntDay)))) {
			cal.set(Calendar.YEAR, curntYear);
		}
		contractAnniversaryDate.setTime(cal.getTime());
		anniversaryDate = contractAnniversaryDate.getTime();
		return anniversaryDate;
	}
	/**
	 * Method to calculate the logic (current date <= contract’s next anniversary 
	 * AND > [contract’s next anniversary – ‘Payroll cut off for online deferral and auto enrollment changes’]) 
	 *
	 * @param csfForm
	 * @return boolean
	 */
	private boolean isDeferralsEziValidForUpdate(CsfForm csfForm, PlanDataLite planDataLite){
		boolean isValid = false;
		
		Date nextAnniv = getNextAnniversary(csfForm, planDataLite);
		
		if(nextAnniv != null && StringUtils.isNotEmpty(csfForm.getPayrollCutoff())) {
			isValid = ContractServiceFeatureUtil.isFreezePeriod(Integer.valueOf(csfForm.getPayrollCutoff()), nextAnniv, true);
		}
		return isValid;
	}
	/**
	 * Returns a boolean true value if the test value provided is either {@link CsfConstants#CSF_YES} or
	 * {@link CsfConstants#CSF_TRUE} ({@value CsfConstants#CSF_YES} or {@value CsfConstants#CSF_TRUE}).
	 * 
	 * @param testValue The value to check.
	 * @return boolean - True if it's "yes" or "true", false otherwise.
	 */
	private boolean isValueEitherYesOrTrue(final String testValue) {
		return CsfConstants.CSF_YES.equals(testValue)
		|| CsfConstants.CSF_TRUE.equals(testValue);
	}
	
	/**
	 * Method use to set ManagedAccountSection Values
	 * @param csfMap - csfMap parameter
	 * @param csfForm - csfForm parameter
	 * @param contractId - contractId parameter
	 * @param planDataLite - planDataLite parameter
	 * @throws SystemException - Exception
	 */
	public static void setManagedAccountSectionValues(@SuppressWarnings("rawtypes") Map csfMap, CsfForm csfForm,
			int contractId, PlanDataLite planDataLite) throws SystemException {
		if (isPlanEligibleForManagedAccounts(csfForm, contractId, planDataLite)
				&& getManagedAccountServiceSelected(csfForm, contractId) != null) {
			final String date = getManagedAccountServiceAvailableToPptDate(csfMap);
			final Date convertedDate = CsfDataHelper.convertStringToDate(date);
			csfForm.setManagedAccountServiceAvailableToPptDate(date);
			if(date != null && convertedDate != null && convertedDate.compareTo(new Date()) > 0) {
				csfForm.setManagedAccountSectionEditable(true);
			}
		}
	}
	
	/**
	 * Method use to check plan Eligible for ManagedAccounts
	 * @param csfForm - csfForm parameter
	 * @param contractId - contractId parameter
	 * @param planDataLite - planDataLite parameter
	 * @return boolean
	 * @throws SystemException - Exception
	 */
	private static boolean isPlanEligibleForManagedAccounts(CsfForm csfForm, final int contractId, final PlanDataLite planDataLite) throws SystemException {
		final boolean isPlanEligibleForManagedAccounts = !PlanData.PLAN_TYPE_457B.equals(planDataLite.getPlanTypeCode())
				&& FeeServiceDelegate.getInstance(Environment.getInstance().getAppId()).isPinpoinContract(contractId);
		csfForm.setPlanEligibleForManagedAccounts(isPlanEligibleForManagedAccounts);
		return isPlanEligibleForManagedAccounts;
	}
	
	/**
	 * Method use to select manageAccountService 
	 * @param csfForm - csfForm parameter
	 * @param contractId - contractId parameter
	 * @return ManagedAccountServiceFeatureLite
	 * @throws SystemException - Exception
	 */
	private static ManagedAccountServiceFeatureLite getManagedAccountServiceSelected(CsfForm csfForm, final int contractId) throws SystemException {
		ManagedAccountServiceFeatureLite serviceFeature = ContractServiceDelegate.getInstance()
				.getContractSelectedManagedAccountServiceLite(contractId);
		if (serviceFeature != null) {
			csfForm.setManagedAccountServiceFeature(serviceFeature);
		}
		return serviceFeature;
	}
	
	/**
	 * Method to get managedAccount Service available to PPT Date
	 * @param csfMap - csfMap parameter
	 * @param managedAccountServiceFeature - managedAccountServiceFeature parameter
	 * @return String
	 */
	private static String getManagedAccountServiceAvailableToPptDate(@SuppressWarnings("rawtypes") Map csfMap) {
		ContractServiceFeature csf = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.MANAGED_ACCOUNT_GROUP_CODE);
		if (csf != null) {
			return csf.getAttributeValue(ServiceFeatureConstants.MANAGED_ACCOUNT_SERVICE_AVAILABLE_DATE);
		}	
		return null;
	}
	
	
	/**
	 * Method used to convert String to Date
	 * @param str - str parameter
	 * @return Date
	 */
	public static Date convertStringToDate(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat(MM_DD_YYYY);
        try {
        	if(StringUtils.isNotEmpty(str)){
        		return formatter.parse(str);
        	}
        	return null;
        } catch (ParseException e) {
        	logger.error(e);
        }
        return null;
	}
	
	/**
	 * Method use to format the given date
	 * @param date - date parameter
	 * @return String
	 */
	public static String formatDate(Date date) {
		
		SimpleDateFormat dtf =new SimpleDateFormat(MM_DD_YYYY);
		if(date == null){
			return dtf.format(new Date());
		}
		return dtf.format(date);
	}

	/**
	 * Method will return the flag, used to show/hide Notices edelivery section in
	 * view page true -> show false -> hide
	 * 
	 * @return String
	 */
	public static String isNoticesEdeliveryViewEnabled() {
		return baseEnvironment.getNamingVariable(CsfConstants.NOTICES_EDELIVERY_VIEW_ENABLED, null);
	}
		
	/**
	 * Method will return the flag, used to show/hide Notices edelivery section in
	 * edit or confirm pages true -> show false -> hide
	 * 
	 * @return String
	 */
	public static String isNoticesEdeliveryEditOrConfirmEnabled() {
		return baseEnvironment.getNamingVariable(CsfConstants.NOTICES_EDELIVERY_EDIT_CONFIRM_ENABLED, null);
	}
}
