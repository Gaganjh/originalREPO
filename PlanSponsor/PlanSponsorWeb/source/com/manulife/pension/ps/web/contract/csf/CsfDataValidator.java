package com.manulife.pension.ps.web.contract.csf;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;


import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.delegate.PartyServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.contract.csf.util.CsfUtil;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.sendservice.util.PlanDataWebUtility;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractProfileVO;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.contract.valueobject.VestingSchedule;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.fund.valueobject.Access404a5.MissingInformation;
import com.manulife.pension.service.fund.valueobject.Access404a5.Qualification;
import com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO;
import com.manulife.pension.service.notices.valueobject.NoticePlanVestingMTExcludeVO;
import com.manulife.pension.service.party.valueobject.BusinessParameterValueObject;
import com.manulife.pension.service.plan.valueobject.IrsAnnualMaximums;
import com.manulife.pension.service.plan.valueobject.MoneyTypeEligibilityCriterion;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.plan.valueobject.PlanEmployeeDeferralElection;
import com.manulife.pension.service.plan.valueobject.WithdrawalDistributionMethod;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * Class to validate the CSF values. Once all CSF values are validated then appropriate error 
 * messages or warnings messages will be generated. 
 * 
 * @author Puttaiah Arugunta
 *
 */
public class CsfDataValidator {
	
	private static CsfDataValidator localObject = null;
	
	private static final String className = CsfDataValidator.class.getName();
	
	private Logger logger = Logger.getLogger(className);
	
	private static final Set<String> SPECIFIED_PAYROLL_FREQUENCY_CODES = new HashSet<>(
			Arrays.asList(CsfConstants.PAYROLL_FREQUENCY_WEEKLY_CODE, CsfConstants.PAYROLL_FREQUENCY_BI_WEEKLY_CODE,
					CsfConstants.PAYROLL_FREQUENCY_SEMI_MONTHLY_CODE, CsfConstants.PAYROLL_FREQUENCY_MONTHLY_CODE));
	
    /**
     * Return an instance of the CsfDataValidator using the a Singleton pattern
     * 
     * @return CsfDataValidator
     */
    public static CsfDataValidator getInstance() {
        if (localObject == null) {
        	localObject = new CsfDataValidator();
        }
        return localObject;
    }
    
  
   /**
    * This method is being used to validate all sections in the CSF page. 
    * 
    * @param actionForm
    * @param request
    * @param userProfile
    * @param errors
    * @param warnings
    * @return
    * @throws SystemException 
    */
    protected Collection<GenericException> validateCSFValues(
    		ActionForm actionForm, HttpServletRequest request, 
    		UserProfile userProfile, WithdrawalDistributionMethod withdrawalDistributionMethod, Collection<GenericException> errors,
    		Collection<GenericException> warnings) throws SystemException{

    	int contractId = userProfile.getCurrentContract().getContractNumber();
        final PlanDataLite planDataLite = getPlanDataLite(
        		request, contractId);

        CsfForm csfForm = (CsfForm) actionForm ;
		((CsfForm)actionForm).removeLeadingZeros();
		if(userProfile.getRole().isInternalUser()) {
			validateManagingDeferrals(contractId, csfForm, planDataLite, errors, warnings);
			validateFinancialTxns(csfForm, planDataLite,  userProfile.getCurrentContract(),	errors,warnings);
			validatePayrollCutoff(csfForm,  planDataLite , userProfile , errors, warnings);
			validateECSection(actionForm, request, userProfile, planDataLite, errors, warnings);
			validateAutoEnrollment(csfForm,  planDataLite, errors, warnings);
			validateDirectMail(csfForm,  request, planDataLite, errors, warnings);
			validateAutoContributionIncrease(csfForm,  planDataLite, errors, warnings);
			validateVesting(csfForm,  planDataLite, errors, warnings);
			validateOnlineLoans(csfForm,  planDataLite, userProfile.getCurrentContract(), errors, warnings);
			validateIWithdrawals(contractId, csfForm,  planDataLite, errors, warnings);
			validateAdditionalRequirements(csfForm, planDataLite, errors);
			validatePlanHighLights(csfForm, planDataLite, withdrawalDistributionMethod, errors);
			validateSendService(csfForm, warnings, errors, userProfile);
			validatePayrollFeedbackService(csfForm, errors, warnings);
			validateManagedAccountServiceAvailableDateUpdate(csfForm, errors, warnings);
		}
		return errors;
	}

   
    /**
     * Validation logic for Payroll Feedback Service Feature.
     * <p />
     * Visibility opened to allow for unit testing.
     * <p />
     * @param csfForm
     * @param errors
     * @param warnings
     */
    protected void validatePayrollFeedbackService(CsfForm csfForm, Collection<GenericException> errors,
		Collection<GenericException> warnings) {
    	int errorCountOnEntry = errors.size();

    	boolean isPayrollFeedbackServiceEnabledOnPost = ServiceFeatureConstants.PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE.equals(csfForm.getPayrollFeedbackService())
    			|| ServiceFeatureConstants.PAYROLL_FEEDBACK_360_SERVICE_ATTRIBUTE_CODE.equals(csfForm.getPayrollFeedbackService());
    	if(!isPayrollFeedbackServiceEnabledOnPost) {
    		return; // feature not requested - nothing to validate
    	}    	
    	
    	if(!CsfConstants.CSF_YES.equals(csfForm.getChangeDeferralsOnline())) {
    		errors.add(new ValidationError(CsfConstants.FIELD_CHANGE_DEFERRALS_ONLINE, 
    				ErrorCodes.ERROR_PARTICIPANT_ONLINE_DEFERRAL_MGMT_MUST_BE_ON));	
    	}
    	
    	if(!CsfConstants.CSF_YES.equals(csfForm.getEnrollOnline())) {
    		errors.add(new ValidationError(CsfConstants.FIELD_ENROLL_ONLINE, 
    				ErrorCodes.ERROR_PARTICIPANT_ONLINE_ENROLLMENT_MUST_BE_ON));	
    	}
    	
    	if(!SPECIFIED_PAYROLL_FREQUENCY_CODES.contains(csfForm.getPlanFrequency())) {
    		errors.add(new ValidationError(CsfConstants.FIELD_PLAN_FREQUENCY, 
    				ErrorCodes.ERROR_PAYROLL_FREQUENCY_MUST_BE_SPECIFIED));	
    	}
    	
    	if(errorCountOnEntry < errors.size()) {
    		return; // errors added, so exit now - warning only to be added if error level validation passes.
    	}
    	
    	CsfForm loadedForm = (CsfForm) csfForm.getClonedForm();
    	String loadedPayrollFeedbackServiceValue = loadedForm == null ? null : loadedForm.getPayrollFeedbackService();
    	
    	if(ServiceFeatureConstants.PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE.equals(csfForm.getPayrollFeedbackService())) {
    		if(!ServiceFeatureConstants.PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE.equals(loadedPayrollFeedbackServiceValue)) {
    	    	warnings.add(new ValidationError(CsfConstants.FIELD_PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE, 
    	    			ErrorCodes.WARNING_PAYROLL_FEEDBACK_SERVICE_FEATURE_IMPACT, 
    	    			ValidationError.Type.warning));
    		}
    	}

    	if(ServiceFeatureConstants.PAYROLL_FEEDBACK_360_SERVICE_ATTRIBUTE_CODE.equals(csfForm.getPayrollFeedbackService())) {
    		if(!ServiceFeatureConstants.PAYROLL_FEEDBACK_360_SERVICE_ATTRIBUTE_CODE.equals(loadedPayrollFeedbackServiceValue)) {
    	    	warnings.add(new ValidationError(CsfConstants.FIELD_PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE, 
    	    			ErrorCodes.WARNING_PAYROLL_FEEDBACK_360_SERVICE_FEATURE_IMPACT, 
    	    			ValidationError.Type.warning));
    		}
    	}
    	
    }

   
	private void validateManagedAccountServiceAvailableDateUpdate(CsfForm csfForm, Collection<GenericException> errors,
			Collection<GenericException> warnings) {
		CsfForm oldForm = (CsfForm) csfForm.getClonedForm();
		if (csfForm.isManagedAccountSectionEditable()) {
			Date currentValue = CsfDataHelper.convertStringToDate(csfForm.getManagedAccountServiceAvailableToPptDate());
			Date previousValue = CsfDataHelper.convertStringToDate(oldForm.getManagedAccountServiceAvailableToPptDate());
			if (currentValue.after(previousValue)) {
				warnings.add(new ValidationError(CsfConstants.MANAGED_ACCOUNT_SERVICE_AVAILABILITY_DATE_CONFIRM,
						ErrorCodes.WARNING_CODE_MANAGED_ACCOUNT_DATE_CHANGED, ValidationError.Type.warning));
			} else if (currentValue.before((previousValue))) {
				errors.add(new ValidationError(CsfConstants.MANAGED_ACCOUNT_SERVICE_AVAILABILITY_DATE_CONFIRM,
						ErrorCodes.ERROR_CODE_MANAGED_ACCOUNT_DATE_LESS_THAN_DB_DATE, ValidationError.Type.error));
			}
		}
	}

	/**
     * Method to validate the CSF against the Plan Data. This method will be used to display
     * errors/ warnings in CSF View/Edit pages.
     * 
     * CSF.9 If the CSF and Plan attribute cross-edit errors and warnings indicated 
     *  
     * @param csfMap
     * @param planDataLite
     * @param errors
     */
   public void validateCSFAgainstPlanData(CsfForm csfForm, PlanDataLite planDataLite, WithdrawalDistributionMethod withdrawalDistributionMethod,
			Collection<GenericException> errors ){
	   
	   String csfAEInd = csfForm.getAutoEnrollInd();
	   String initialEnrollmentDate = csfForm.getInitialEnrollmentDate();
	   String csfACIInd = csfForm.getAutoContributionIncrease();
	   String aciAnniversaryDate = csfForm.getAciAnniversaryDate();
	   String aciAnniversaryYear = csfForm.getAciAnniversaryYear();
	   String csfVestingInd = csfForm.getVestingPercentagesMethod();
	   String changeDeferralsOnline = csfForm.getChangeDeferralsOnline();
	   
	   
	   // CSF. 164 is pending because plan data needs to come from PLAN_EMPLOYEE_DEFERRAL_ELECTION.DEFERRAL_ELECTION_CODE
	   PlanEmployeeDeferralElection deferralElection = planDataLite.getPlanEmployeeDeferralElection();
	   if(CsfConstants.CSF_YES.equals(changeDeferralsOnline) &&
			   deferralElection != null && (deferralElection.getEmployeeDeferralElectionCode() == null ||
					  StringUtils.isBlank(deferralElection.getEmployeeDeferralElectionCode()) ) ){
		   errors.add(new ValidationError(CsfConstants.FIELD_CHANGE_DEFERRALS_ONLINE,ErrorCodes.ERROR_CHANGE_DEFERRALS_ONLINE_CAN_NOT_BE_YES));
	   }
	  
	   // CSF. 214 EC releted, will update once EC related done.
	   
	   if (CsfConstants.CSF_YES.equals(csfAEInd) ){
		   
		   /*
		    *  CSF.228	If “Turn on JH EZstart” is “Yes” and the Plan attribute
		    *  “Does the plan provide for automatic enrollment?” is not “Yes” 
		    */
		   if(!StringUtils.equals(Constants.CSF_YES_CODE, 
				   planDataLite.getAutomaticEnrollmentAllowed())) {
			   errors.add(new ValidationError(
					   CsfConstants.FIELD_AUTO_ENROLL_IND,ErrorCodes.ERROR_AE_NOT_ALLOWED_IN_PLAN));
		   }
		   
		   /*
		    * CSF.229 If “Turn on JH EZstart” is “Yes” and the Plan attribute 
		    * “Plan entry frequency” for the EEDEF money type is not 
		    * “Quarterly”, “Semi-annually” or “Annually”, 
		    */
		   if (!StringUtils.equals(Constants.PLAN_ENTRY_FREQUENCY_MONTHLY, planDataLite.getPlanEntryFrequencyForMoneyTypeEedef())
				   && !StringUtils.equals(Constants.PLAN_ENTRY_FREQUENCY_SEMI_ANNUALLY, planDataLite.getPlanEntryFrequencyForMoneyTypeEedef()) 
				   && !StringUtils.equals(Constants.PLAN_ENTRY_FREQUENCY_QUARTERLY, planDataLite.getPlanEntryFrequencyForMoneyTypeEedef()) ) {
			   errors.add(new ValidationError(CsfConstants.FIELD_AUTO_ENROLL_IND,
					   ErrorCodes.ERROR_AE_NOT_ALLOWED_FOR_PLAN_WITH_CONFLICTING_PLAN_ENTRY_FREQUENCY));
		   }
		  //CSF.230  If CSF attribute “Turn on JH EZstart” is “Yes” and CSF attribute “Initial enrollment date for the EZstart service” < Plan attribute “AE Effective Date”, 
		   try {
			   if(initialEnrollmentDate != null && StringUtils.isNotBlank(initialEnrollmentDate)){
				   Date initialEnrollmentDt = CsfConstants.initialEnrollmentDateFormat.parse(initialEnrollmentDate);
	               if (planDataLite.getAutomaticEnrollmentEffectiveDate() == null 
	                       || initialEnrollmentDt.before(planDataLite.getAutomaticEnrollmentEffectiveDate())) {
	                    errors.add(new ValidationError(CsfConstants.FIELD_INITIAL_ENROLLMENT_DATE,
	                    		ErrorCodes.ERROR_INITIAL_ENROLLMENT_DATE_DOES_NOT_MATCH_PLAN)); 
	               }
			   }
           } catch (ParseException pe) {
               //logger.error(pe);
           }
	   }
	   
	   if (StringUtils.isNotEmpty(aciAnniversaryDate) 
			   && StringUtils.isNotEmpty(aciAnniversaryYear)
			   && StringUtils.isNumeric(aciAnniversaryYear)) {
		   Calendar annivCal = new GregorianCalendar();
		   	Date annivDate = (new DayOfYear(aciAnniversaryDate)).getAsDateNonLeapYear();
		   	annivCal.setTime(annivDate);
		   	annivCal.set(Calendar.YEAR, Integer.parseInt(aciAnniversaryYear));
		   //CSF.241	If “Turn on JH EZIncrease” is “Yes” and Plan attribute “Does the plan provide for automatic contribution increase?” is “Yes” and CSF attribute “First scheduled increase starts on” date (mm/dd/yyyy) < Plan attribute “ACI Effective date”, 
		   if (CsfConstants.CSF_YES.equals(csfACIInd) && StringUtils.equals(Constants.YES, planDataLite.getAciAllowed()) &&
				   ( planDataLite.getAciEffectiveDate() == null 
				   || annivCal.getTime().before(planDataLite.getAciEffectiveDate()))) {
			   errors.add(new ValidationError(CsfConstants.FIELD_ACI_ANNIVERSARY_YEAR,
					   ErrorCodes.ERROR_ACI_ANNIVERSARY_DATE_BEFORE_PLAN_ACI_EFFECTIVE_DATE));
		   }
		   //CSF.249- Removed	If CSF attribute “First scheduled increase starts on” (mm/dd/yyy) is earlier than the Plan attribute “Automatic contribution increase effective date”, 
		   /*if (planDataLite.getAciEffectiveDate() == null 
				   || annivCal.getTime().before(planDataLite.getAciEffectiveDate())) {
			   errors.add(new ValidationError(CsfConstants.EMPTY_STRING,ErrorCodes.ERROR_CSF_ACI_DATE_IS_LESSTHAN_PLAN_ACI));
		   }*/
	   }
	   if (CsfConstants.CSF_YES.equals(csfACIInd) ){
		   //CSF.242	If “Turn on JH EZincrease” is “Yes” and Plan attribute “Does the plan provide for automatic contribution increases?” is not “Yes
		   if (!StringUtils.equals(Constants.YES, planDataLite.getAciAllowed())) {
			   errors.add(new ValidationError(CsfConstants.FIELD_AUTO_CONTRIBUTION_INCREASE,
					   ErrorCodes.ERROR_ACI_AUTO_METHOD_NOT_ALLOWED_WHEN_PLAN_DOES_NOT_HAVE_ACI_ALLOWED));
		   }
	   }
	 //CSF attribute “Vesting will be” = “Calculated by John Hancock” 
   	if (CsfConstants.PLAN_VESTING_CALCULATION.equals(csfVestingInd)) {
   		
   		 //CSF.255	If CSF attribute “Vesting will be” = “Calculated by John Hancock”
   		 //and Plan attribute “Vesting computation shall be” is “Unspecified”,
   		 if(StringUtils.equals(CsfConstants.VESTING_COMPUTATION_PERIOD_UNSPECIFIED, 
   				 planDataLite.getVestingComputationPeriod())){
   			 errors.add(new ValidationError(CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD,ErrorCodes.ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_VESTING_UNSPECIFIED));
   		 }
   		 // CSF.256	If CSF attribute “Vesting will be” = “Calculated by John Hancock” 
   		 //and Plan attribute “Does the plan have two or more vesting schedules for any single money type” is “Unspecified”, 
   		 if(StringUtils.equals(CsfConstants.VESTING_COMPUTATION_PERIOD_UNSPECIFIED, 
   				 planDataLite.getMultipleVestingSchedulesForOneSingleMoneyType())){
   			 errors.add(new ValidationError(CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD,ErrorCodes.ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_TWO_OR_MORE_VESTINGS_UNSPECIFIED));
   		 }
   		 //CSF.257 If CSF attribute “Vesting will be” = “Calculated by John Hancock” 
		 // and Plan attribute “Vesting schedule” is missing for at least one ER money type, 
		 if(planDataLite.isVestingMissedForAnyERMoneyType()){
			 errors.add(new ValidationError(CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD,ErrorCodes.ERROR_CALCULATED_NOT_ALLOWED_WHEN_VESTING_MISSED_FOR_ANY_ER_TYPES));
		 }
           /* CSF. 258.   If the CSF “Vesting percentages to be:” = “Calculated by John Hancock” 
            * and Plan: “Does the plan have two or more vesting schedule for any single money type?” =”Yes”, 
            */
           if (StringUtils.equals(Constants.YES, planDataLite.getMultipleVestingSchedulesForOneSingleMoneyType())) {
               errors.add(new ValidationError(CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD,
            		   ErrorCodes.ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_HAS_MULTIPLE_VESTING_SCHEDULES));
           } else {
               /* CSF.259   If the CSF “Vesting service percentages to be” is set to “Calculated by John Hancock” 
                * and the Plan: “Vesting service crediting method” = “Hours of service’ 
                * and Plan “The vesting computation period shall be …” = “the date an Employee first performs an Hour of service and each anniversary thereof”, 
                */
               if (StringUtils.equals(CsfConstants.VESTING_SERVICE_CREDIT_METHOD_HOURS_OF_SERVICE, planDataLite.getVestingServiceCreditMethod())
                       && StringUtils.equals(CsfConstants.VESTING_COMPUTATION_BASED_ON_HOS_FIRST_AND_EACH_ANNIVERSARY_THEREOF_CODE, planDataLite.getVestingComputationPeriod())) {
                   errors.add(new ValidationError(CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD,
                		   ErrorCodes.ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_HAS_HOURS_OF_SERVICE_AND_ANNIVERSARY));                    
               } else {
               /* CSF.260. If the “Vesting service percentages to be” is set to “Calculated by John Hancock” 
                * and Plan: “Vesting service crediting method” = “Elapsed time’ 
                * and Plan: “The vesting computation period shall be…” = “the plan year”, 
                */
                   if (StringUtils.equals(CsfConstants.VESTING_SERVICE_CREDIT_METHOD_ELAPSED_TIME, planDataLite.getVestingServiceCreditMethod())
                           && StringUtils.equals(CsfConstants.VESTING_COMPUTATION_PERIOD_PLAN_YEAR_CODE, 
                        		   planDataLite.getVestingComputationPeriod())) {
                       errors.add(new ValidationError(CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD,
                    		   ErrorCodes.ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_HAS_ELAPSED_TIME_AND_PLAN_YEAR));
                   }                        
               }                
           }
       } 
    // CSF.270	If “Online Loans” = “Yes” and (Plan attribute “Does the plan allow loans” not = “Yes” OR Apollo Loans not = “Yes”),
	   if(CsfConstants.CSF_YES.equals(csfForm.getAllowOnlineLoans()) 
				&& !StringUtils.equals(ServiceFeatureConstants.YES, planDataLite.getLoansAllowedInd())
				&& CsfConstants.CSF_YES.equals(csfForm.getLoanRecordKeepingInd())){
		   errors.add(new ValidationError(CsfConstants.FIELD_ALLOW_ONLINE_LOANS,ErrorCodes.ERROR_ONLINE_LOANS_NOT_SUPPORTED));
	   }
	   validateAdditionalRequirements(csfForm, planDataLite, errors);
	   
	   // CR 21 - Dec Release Changes
	   validatePlanHighLights(csfForm, planDataLite, withdrawalDistributionMethod, errors);
	   
   }
   
   


/**
    * Method to validate the requirements from CSF.412 to 421.
    *  
    * @param csfForm
    * @param planDataLite
    * @param errors
    */
   private void validateAdditionalRequirements(CsfForm csfForm, PlanDataLite planDataLite, Collection<GenericException> errors ){
	   
	   if(CsfConstants.CSF_YES.equals(csfForm.getEnrollOnline()) &&
			   planDataLite.isAnyDeferralsMoneyTypesAvailable()){
		   //CSF. 413
		   if(planDataLite.getDeferralMaxPercent() == null){
			   errors.add(new ValidationError(CsfConstants.FIELD_ENROLL_ONLINE,2941));
		   }
		   //CSF. 414
		   if(CsfConstants.EMPTY_STRING.equals(csfForm.getDeferralType())){
			   errors.add(new ValidationError(CsfConstants.FIELD_ENROLL_ONLINE,2942));
		   }
		   //CSF. 415
		   if(CsfConstants.CSF_YES.equals(csfForm.getChangeDeferralsOnline()) &&
				   CsfConstants.PLAN_ACI_UNSPECIFIED.equals(planDataLite.getAciAllowed())){
			   errors.add(new ValidationError(CsfConstants.FIELD_ENROLL_ONLINE,2943));
		   }
	   }
	   
	   if(CsfConstants.CSF_YES.equals(csfForm.getParticipantInitiateLoansInd())
			   && CsfConstants.CSF_YES.equals(csfForm.getLoanRecordKeepingInd())){
		   //CSF.416
		   if(planDataLite.getMinimumLoanAmount() == null){
			   errors.add(new ValidationError(CsfConstants.FIELD_PARTICIPANT_INITIATE_LOANS,2944));
		   }
		   //CSF.417
		   if(planDataLite.getMaximumLoanAmount() == null){
			   errors.add(new ValidationError(CsfConstants.FIELD_PARTICIPANT_INITIATE_LOANS,2945));
		   }
		   //CSF.418
		   if(planDataLite.getMaximumNumberofOutstandingLoans() == 0){
			   errors.add(new ValidationError(CsfConstants.FIELD_PARTICIPANT_INITIATE_LOANS,2946));
		   }
		   //CSF.419
		   if(planDataLite.getLoanInterestRateAbovePrime() == null){
			   errors.add(new ValidationError(CsfConstants.FIELD_PARTICIPANT_INITIATE_LOANS,2947));
		   }
		   
	   }
	   
	   if(CsfConstants.CSF_YES.equals(csfForm.getParticipantWithdrawalInd())){
		   //420
		   if(CsfConstants.PLAN_REQUIRES_SPOUSAL_CONSENT_UNSPECIFIED.equals(planDataLite.getRequiresSpousalConsentForDistributions())){
			   errors.add(new ValidationError(CsfConstants.FIELD_PARTICIPANT_WITHDRAWAL_IND,2948));
		   }
		   //421
		   if(!planDataLite.isRetirementAgeSpecified()){
			   errors.add(new ValidationError(CsfConstants.FIELD_PARTICIPANT_WITHDRAWAL_IND,2949));
		   }
	   }
	   
	   String autoSignUpMethod = csfForm.getAciSignupMethod();
	   
	   if(CsfConstants.CSF_YES.equals(csfForm.getChangeDeferralsOnline())  &&
				( ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(autoSignUpMethod) 
						   || ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(autoSignUpMethod) )){
		 //CSF.443	If “Participants are allowed to change their deferrals online” = “Yes” and Plan attribute “Deferral Limits – Maximum %” is not specified
		   if(planDataLite.getDeferralMaxPercent() == null 
				   && !isExcpetionAdded(errors, ErrorCodes.ACI_DEFERRAL_LIMIT_MAX_EMPTY_ERROR)){
				errors.add(new ValidationError(CsfConstants.FIELD_CHANGE_DEFERRALS_ONLINE,ErrorCodes.ACI_DEFERRAL_LIMIT_MAX_EMPTY_ERROR));
			}

			if(Constants.DEFERRAL_TYPE_PERCENT.equals(csfForm.getDeferralType())){
				// CSF.170(3) and CSF.172 (2)
				if(!ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(csfForm.getAciSignupMethod())){
					validateAdditionalDeferralPercentages(csfForm, planDataLite, errors);
				}
			}
			
			if(Constants.DEFERRAL_TYPE_DOLLAR.equals(csfForm.getDeferralType())){
				//CSF.171(3) and CSF.173 (2)
				validateAdditionalDeferralAmounts(csfForm, planDataLite, errors);
			} 
			
			if(Constants.DEFERRAL_TYPE_EITHER.equals(csfForm.getDeferralType())){
				if(	ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(csfForm.getAciSignupMethod())){
					//CSF.171(3) and CSF.173 (2)
					validateAdditionalDeferralAmounts(csfForm,	planDataLite, errors);
				}else{
					// CSF.170(3) and CSF.172 (2)
					validateAdditionalDeferralPercentages(csfForm, planDataLite, errors);
					//CSF.171(3) and CSF.173 (2)
					validateAdditionalDeferralAmounts(csfForm,	planDataLite, errors);
				}
			}
	   }
   }


	/**
	 * Method to validate the additional cross edits with plan for deferral amounts
	 * @param csfForm
	 * @param planDataLite 
	 * @param errors
	 */
	private void validateAdditionalDeferralAmounts(CsfForm csfForm,
			PlanDataLite planDataLite, Collection<GenericException> errors) {
		
		Integer deferralMaxAmount = planDataLite.getDeferralMaxAmount();
		if(planDataLite.getDeferralIrsApplies()){
			IrsAnnualMaximums irsAnnualMaximums = planDataLite.getIrsAnnualMaximums();
			if(irsAnnualMaximums != null && irsAnnualMaximums.getIrsAnnualRegularMaximumAmount() != null){
				deferralMaxAmount = irsAnnualMaximums.getIrsAnnualRegularMaximumAmount().intValue();
			}
		}
		// CSF.171 (3)
		if(validateAmount(csfForm.getDeferralLimitDollars())
				&& !validateDeferralAmounts(csfForm.getDeferralLimitDollars(),	String.valueOf(deferralMaxAmount))
				&& !isExcpetionAdded(errors, ErrorCodes.ERROR_DEFERRAL_MAX_AMOUNT_IS_GREATER_THAN_CSF_DEFERRAL_INCREASE_AMT)){
			errors.add(new ValidationError(CsfConstants.FIELD_DEFERRAL_LIMIT_DOLLARS,ErrorCodes.ERROR_DEFERRAL_MAX_AMOUNT_IS_GREATER_THAN_CSF_DEFERRAL_INCREASE_AMT));
		}
		//CSF. 173 (3)
		if(validateAmount(csfForm.getDeferralMaxLimitDollars())
				&& !validateDeferralAmounts(csfForm.getDeferralMaxLimitDollars(),String.valueOf(deferralMaxAmount))
				&& !isExcpetionAdded(errors, ErrorCodes.ERROR_DEFERRAL_MAX_AMOUNT_IS_GREATER_THAN_CSF_DEFERRAL_INCREASE_LIMIT_AMT)){
			errors.add(new ValidationError(CsfConstants.FIELD_DEFERRAL_MAX_LIMIT_DOLLARS,ErrorCodes.ERROR_DEFERRAL_MAX_AMOUNT_IS_GREATER_THAN_CSF_DEFERRAL_INCREASE_LIMIT_AMT));
		}
	}
	
	/**
	 * Method to validate the additional cross edits with plan for deferral percentages 
	 * @param csfForm
	 * @param planDataLite
	 * @param errors
	 */
	private void validateAdditionalDeferralPercentages(CsfForm csfForm,
			PlanDataLite planDataLite, Collection<GenericException> errors) {
		Integer planMaxPer = 0;
		if(planDataLite.getDeferralMaxPercent() != null){
			planMaxPer = planDataLite.getDeferralMaxPercent().intValue();  
		}
		//CSF. 170 (3)
		if( validatePercentage(csfForm.getDeferralLimitPercent())
				&& ! validateDeferralPercentages(csfForm.getDeferralLimitPercent(),	String.valueOf(planMaxPer))
				&& !isExcpetionAdded(errors, ErrorCodes.ERROR_DEFERRAL_MAX_PERCENT_IS_GREATER_THAN_CSF_DEFERRAL_INCREASE_PER)){
			errors.add(new ValidationError(CsfConstants.FIELD_DEFERRAL_LIMIT_PERCENT,ErrorCodes.ERROR_DEFERRAL_MAX_PERCENT_IS_GREATER_THAN_CSF_DEFERRAL_INCREASE_PER));
		}
		//CSF. 172 (3)
		if( validatePercentage(csfForm.getDeferralMaxLimitPercent())
				&& ! validateDeferralPercentages(csfForm.getDeferralMaxLimitPercent(),	String.valueOf(planMaxPer))
				&& !isExcpetionAdded(errors, ErrorCodes.ERROR_DEFERRAL_MAX_PERCENT_IS_GREATER_THAN_CSF_DEFERRAL_INCREASE_LIMIT_PER)){
			errors.add(new ValidationError(CsfConstants.FIELD_DEFERRAL_MAX_LIMIT_PERCENT,ErrorCodes.ERROR_DEFERRAL_MAX_PERCENT_IS_GREATER_THAN_CSF_DEFERRAL_INCREASE_LIMIT_PER));
		}
	}
   
	/**
	 * Method to validate the Participants are allowed to change their deferrals online
	 * CSF.163	If “Auto”, then “Participants are allowed to change their deferrals online” must be “Yes”. 
	 * @param errors
	 * @param csfMap
	 *
	 * @throws SystemException
	 */
	protected void validateOnlineDeferrals(CsfForm csfForm, PlanDataLite planDataLite, Collection<GenericException> errors ) throws SystemException{
		
		 String onlineDeferralValue = csfForm.getChangeDeferralsOnline();
		 
		 if(ServiceFeatureConstants.YES.equals(planDataLite.getAciAllowed()) &&
				 CsfConstants.CSF_YES.equals(csfForm.getAutoContributionIncrease())){
			 if(!CsfConstants.CSF_YES.equals(onlineDeferralValue)){
				 errors.add(new ValidationError(CsfConstants.FIELD_CHANGE_DEFERRALS_ONLINE,ErrorCodes.ERROR_CHANGE_DEFERRALS_MUST_BE_YES_WHEN_AUTO));
			 }
		 }
	}

	
	/**
	 * Validate user input for plan data
	 *
	 * @param myForm
	 */
	protected void validatePlanData(PlanDataLite planDataLite, CsfForm myForm) {
		if (CsfConstants.PLAN_VESTING_NA.equals(myForm.getVestingPercentagesMethod())) {
			myForm.setVestingDataOnStatement(CsfConstants.CSF_NO);
		}
	}

	/**
	 * Method to determine the Auto/Sign up for the particular contract
	 * 
	 * @param csfForm
	 * @param planDataLite
	 * @return
	 */
	public String determineSignupMethod(CsfForm csfForm, PlanDataLite planDataLite){
		String autoSignUpMethod = null;
		
		if(CsfConstants.CSF_YES.equals(csfForm.getChangeDeferralsOnline())){
			if(ServiceFeatureConstants.NO.equals(planDataLite.getAciAllowed()) &&
					CsfConstants.CSF_NO.equals(csfForm.getAutoContributionIncrease())){
				autoSignUpMethod = ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP;
			}
			if(ServiceFeatureConstants.YES.equals(planDataLite.getAciAllowed()) &&
					CsfConstants.CSF_YES.equals(csfForm.getAutoContributionIncrease())){
				autoSignUpMethod = ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO;
			}
		}
		return autoSignUpMethod;
	}

	/**
	 * Method to validate "Participants can specify deferral amounts as" 
	 * and "Participants are allowed to change their deferrals online"
	 * 
	 * @param csfForm
	 * @param errors
	 * @param planDataLite
	 * @throws SystemException 
	 */
	private void validateManagingDeferrals(int contractId, CsfForm csfForm, PlanDataLite planDataLite, 
			Collection<GenericException> errors, Collection<GenericException> warnings) throws SystemException {

		String autoSignUpMethod = csfForm.getAciSignupMethod();
		
		if(CsfConstants.CSF_YES.equals(csfForm.getChangeDeferralsOnline()) &&
				( ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(autoSignUpMethod) 
				   || ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(autoSignUpMethod) )){

			if(Constants.DEFERRAL_TYPE_PERCENT.equals(csfForm.getDeferralType())){
				// CSF.167 If “Participants can specify deferral amount as” = “%” and “Sign Up”, then
				if(!ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(autoSignUpMethod)){
					//CSF.170 If “Default scheduled deferral increase %” is specified
					validateDeferralPercentages(csfForm, planDataLite, errors);
				}
			}
			
			if(Constants.DEFERRAL_TYPE_DOLLAR.equals(csfForm.getDeferralType())){
				//CSF.171 If “Default scheduled deferral increase $” is specified
				validateDeferralAmounts(csfForm, planDataLite, errors);
			} 
			
			// CSF.169 If “Participants can specify deferral amounts as” = “both $ and %”, 
			if(Constants.DEFERRAL_TYPE_EITHER.equals(csfForm.getDeferralType())){
				//CSF. 169 1). If “Auto”, then 
				if(	ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(autoSignUpMethod)){
					validateDeferralAmounts(csfForm, planDataLite, errors);
				}else{
					//CSF.170 If “Default scheduled deferral increase %” is specified
					validateDeferralPercentages(csfForm, planDataLite, errors);
					//CSF.171 If “Default scheduled deferral increase $” is specified
					validateDeferralAmounts(csfForm, planDataLite,  errors);
				}
			}
		}
		
		//CSF.163	If “Auto”, then “Participants are allowed to change their deferrals online” must be “Yes”. 
		validateOnlineDeferrals(csfForm, planDataLite,  errors );
		
		
		PlanEmployeeDeferralElection deferralElection = planDataLite.getPlanEmployeeDeferralElection();
		if(CsfConstants.CSF_YES.equals(csfForm.getChangeDeferralsOnline()) ){
			// CSF. 164 is pending because plan data needs to come from PLAN_EMPLOYEE_DEFERRAL_ELECTION.DEFERRAL_ELECTION_CODE
			if(deferralElection != null && (deferralElection.getEmployeeDeferralElectionCode() == null ||
					StringUtils.isBlank(deferralElection.getEmployeeDeferralElectionCode()) ) ){
				errors.add(new ValidationError(CsfConstants.FIELD_CHANGE_DEFERRALS_ONLINE,ErrorCodes.ERROR_CHANGE_DEFERRALS_ONLINE_CAN_NOT_BE_YES));
			}
			//CSF.443	If “Participants are allowed to change their deferrals online” = “Yes” and Plan attribute “Deferral Limits – Maximum %” is not specified
			if(planDataLite.getDeferralMaxPercent() == null
					&& !isExcpetionAdded(errors, ErrorCodes.ACI_DEFERRAL_LIMIT_MAX_EMPTY_ERROR)){
				errors.add(new ValidationError(CsfConstants.FIELD_CHANGE_DEFERRALS_ONLINE,ErrorCodes.ACI_DEFERRAL_LIMIT_MAX_EMPTY_ERROR));
			}
			validateDeferralOnlineChangeCsf(csfForm, planDataLite, errors);
		}
    }

	/**
	 * Method to validate the Deferral Amounts
	 * @param csfForm
	 * @param errors
	 */
	private void validateDeferralAmounts(CsfForm csfForm, PlanDataLite planDataLite,
			Collection<GenericException> errors) {
		//CSF.171 If “Default scheduled deferral increase $” is specified
		if(StringUtils.isEmpty(csfForm.getDeferralLimitDollars())){
			errors.add(new ValidationError(CsfConstants.FIELD_DEFERRAL_LIMIT_DOLLARS,ErrorCodes.ERROR_DEFERRAL_AMOUNT_CAN_NOT_BE_BLANK));
		}else if(!validateAmount(csfForm.getDeferralLimitDollars())){
			errors.add(new ValidationError(CsfConstants.FIELD_DEFERRAL_LIMIT_DOLLARS,ErrorCodes.ERROR_INVALID_DEFERRAL_INCREASE_AMOUNT));
		}
		if(StringUtils.isEmpty(csfForm.getDeferralMaxLimitDollars())){
			errors.add(new ValidationError(CsfConstants.FIELD_DEFERRAL_MAX_LIMIT_DOLLARS,ErrorCodes.ERROR_DEFERRAL_MAX_AMOUNT_CAN_NOT_BE_BLANK));
		}else if(!validateAmount(csfForm.getDeferralMaxLimitDollars())){
			errors.add(new ValidationError(CsfConstants.FIELD_DEFERRAL_MAX_LIMIT_DOLLARS,ErrorCodes.ERROR_INVALID_DEFERRAL_LIMIT_AMOUNT));
		}
		if(validateAmount(csfForm.getDeferralLimitDollars())
				&& validateAmount(csfForm.getDeferralMaxLimitDollars()) 
				&& ! validateDeferralAmounts(csfForm.getDeferralLimitDollars(),
				csfForm.getDeferralMaxLimitDollars())){
			errors.add(new ValidationError(CsfConstants.FIELD_DEFERRAL_LIMIT_DOLLARS,ErrorCodes.ERROR_DEFERRAL_MIN_AMOUNT_GREATER_THAN_MAX_AMOUNT));
		}
		//CSF.171(3) and CSF.173 (2)
		validateAdditionalDeferralAmounts(csfForm, planDataLite, errors);
	}

	/**
	 * Method to validate Deferral Percentages
	 * 
	 * @param csfForm
	 * @param errors
	 */
	private void validateDeferralPercentages(CsfForm csfForm, PlanDataLite planDataLite,
			Collection<GenericException> errors) {
		//CSF.170 If “Default scheduled deferral increase %” is specified
		if(StringUtils.isEmpty(csfForm.getDeferralLimitPercent())){
			errors.add(new ValidationError(CsfConstants.FIELD_DEFERRAL_LIMIT_PERCENT,ErrorCodes.ERROR_DEFERRAL_PERCENTAGE_CAN_NOT_BE_BLANK));
		}else if(!validatePercentage(csfForm.getDeferralLimitPercent())){
			errors.add(new ValidationError(CsfConstants.FIELD_DEFERRAL_LIMIT_PERCENT,ErrorCodes.ERROR_INVALID_DEFERRAL_PERCENTAGE));
		}
		
		if(StringUtils.isEmpty(csfForm.getDeferralMaxLimitPercent())){
			errors.add(new ValidationError(CsfConstants.FIELD_DEFERRAL_MAX_LIMIT_PERCENT,ErrorCodes.ERROR_DEFERRAL_MAX_PERCENTAGE_CAN_NOT_BE_BLANK));
		}else if(!validatePercentage(csfForm.getDeferralMaxLimitPercent())){
			errors.add(new ValidationError(CsfConstants.FIELD_DEFERRAL_MAX_LIMIT_PERCENT,ErrorCodes.ERROR_INVALID_DEFERRAL_MAX_PERCENTAGE));
		}
		if( validatePercentage(csfForm.getDeferralLimitPercent())
				&& validatePercentage(csfForm.getDeferralMaxLimitPercent())
				&& ! validateDeferralPercentages(csfForm.getDeferralLimitPercent(),
				csfForm.getDeferralMaxLimitPercent())){
			errors.add(new ValidationError(CsfConstants.FIELD_DEFERRAL_LIMIT_PERCENT,ErrorCodes.ERROR_DEFERRAL_MIN_PER_GREATER_THAN_MAX_PERCENTAGE));
		}
		//CSF.170(3) and CSF.172 (2)
		validateAdditionalDeferralPercentages(csfForm, planDataLite, errors);
	}
	/**
	 * Method to validate "Payroll cut off for online deferral and auto enrollment changes is"
	 * @param csfForm
	 * @param errors
	 * @param planDataLite
	 */
	private void validatePayrollCutoff(CsfForm csfForm, PlanDataLite planDataLite, UserProfile userProfile,
			Collection<GenericException> errors, Collection<GenericException> warnings ) {
		String autoSignUpMethod = csfForm.getAciSignupMethod();
		//CSF.180 The “Payroll cut off for online deferral and auto enrollment changes is” must be numeric, no decimals, and must be >= 5 and <= 30 days
		if (StringUtils.isEmpty(csfForm.getPayrollCutoff()) || 
				!validatePayrollCutoff(csfForm.getPayrollCutoff())) {
			errors.add(new ValidationError(CsfConstants.FIELD_PAYROLL_CUTOFF,ErrorCodes.ERROR_INVALID_PAYROLL_CUTOFF));
		}
		//CSF.181 If (“Auto” or “Sign-up”) and ‘Payroll cut off for online deferral and auto enrollment changes is” is changed, 
		if(ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(autoSignUpMethod) ||
				ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(autoSignUpMethod)){
			if(!((CsfForm)csfForm.getClonedForm()).getPayrollCutoff().equals(csfForm.getPayrollCutoff())){
				warnings.add(new ValidationError(CsfConstants.FIELD_PAYROLL_CUTOFF,ErrorCodes.WARNING_PAYROLL_CUTOFF_IMPACT_CODE,ValidationError.Type.warning));
			}
			/*CSF.182 The EZI Periodic Process will run x days prior to the anniversary date. 
			 *  If Contract status = (AC or CF) and (“Auto” or “Sign-up”) 
			 *  and [current date + new “Payroll cut off for online deferral and 
			 *  auto enrollment changes is”] is > = Contract’s next anniversary (in the freeze period),*/ 
			if ((userProfile.getCurrentContract().getStatus().equals("AC") ||
					userProfile.getCurrentContract().getStatus().equals("CF")) 
					&& (isValueChanged(csfForm, CsfConstants.FIELD_PAYROLL_CUTOFF) 
							|| isValueChanged(csfForm, CsfConstants.FIELD_ACI_ANNIVERSARY_YEAR) )
					&& CsfDataHelper.getInstance().isFreezePeriod(csfForm, planDataLite)){
				String property = null;
				if(isValueChanged(csfForm, CsfConstants.FIELD_PAYROLL_CUTOFF)){
					property = CsfConstants.FIELD_PAYROLL_CUTOFF;
				}
				if(isValueChanged(csfForm, CsfConstants.FIELD_ACI_ANNIVERSARY_YEAR)){
					property = CsfConstants.FIELD_ACI_ANNIVERSARY_YEAR;
				}
				errors.add(new ValidationError(property,ErrorCodes.ERROR_INVALID_DATE_TO_ANNIVERSARY));
			}
		}
	}
	
	/**
	 * Method to validate the financial transactions
	 * @param csfForm
	 * @param planDataLite
	 * @param errors
	 * @param warnings
	 */
	private void validateFinancialTxns(CsfForm newForm, PlanDataLite planDataLite, Contract currentContract,
			Collection<GenericException> errors, Collection<GenericException> warnings){

		//CSF.186	If “Participants can initiate online loans” = “Yes”
		//and the “PY” (Payee) column on Apollo = “Trustee”, 
		String checkPayableToCode = currentContract.getCheckPayableToCode().trim();
		if (isValueEitherYesOrTrue(newForm.getParticipantInitiateLoansInd())) {
			if (StringUtils.equalsIgnoreCase(checkPayableToCode,
					ServiceFeatureConstants.CHECK_PAYABLE_TO_TRUSTEE)) {
				errors.add(new ValidationError(CsfConstants.FIELD_PARTICIPANT_INITIATE_LOANS_IND,ErrorCodes.ERROR_PIL_NOT_ALLOWED_FOR_PAYEE));
			}
		}
		
		if(!StringUtils.equals(((CsfForm)newForm.getClonedForm()).getParticipantInitiateLoansInd(),
				newForm.getParticipantInitiateLoansInd())){
			//CSF.187 If “Participants can initiate online loans” is changed to “Yes” and the “PY” (Payee) column on Apollo = “Trustee”, 
			if (isValueEitherYesOrTrue(newForm.getParticipantInitiateLoansInd())) {
				if (StringUtils.equalsIgnoreCase(checkPayableToCode,
						ServiceFeatureConstants.CHECK_PAYABLE_TO_TRUSTEE)) {
					errors.add(new ValidationError(CsfConstants.FIELD_PARTICIPANT_INITIATE_LOANS_IND,ErrorCodes.ERROR_PIL_NOT_ALLOWED_FOR_PAYEE));
				}
			}else{
				//CSF.188 If “Participant can initiate online loans” is changed to “No”, display a warning message
				warnings.add(new ValidationError(CsfConstants.FIELD_PARTICIPANT_INITIATE_LOANS_IND,ErrorCodes.WARNING_PARTICIPANT_WILL_NO_LONGER_BE_ABLE_TO_INITIATE_LOANS,ValidationError.Type.warning));
			}
		}
	}

	/**
	 * Validate Auto Enrollment fields
	 *
	 * @param csfForm
	 * @param errors
	 */
	private void validateAutoEnrollment(CsfForm csfForm, PlanDataLite planDataLite, 
			Collection<GenericException> errors, Collection<GenericException> warnings) {
		// CSF 101: If Auto Enrollment is set to "Yes" then the rest of the AutoEnrollment
		// fields are required
		
		if(CsfConstants.CSF_YES.equals(csfForm.getAutoEnrollInd())){
			// CSF.223	If “Turn on JH EZstart” is “Yes” and “Participants can specify deferral amounts as” is “$”, 
			if(Constants.DEFERRAL_TYPE_DOLLAR.equals(csfForm.getDeferralType())){
				errors.add(new ValidationError(CsfConstants.FIELD_AUTO_ENROLL_IND,ErrorCodes.ERROR_INVALID_DEFERRAL_TYPE_FOR_AE));
			}
			// CSF. 228    EZStart cannot be turned on unless the plan offers automatic enrollment
			if (!StringUtils.equals(Constants.CSF_YES_CODE, planDataLite.getAutomaticEnrollmentAllowed())) {
				errors.add(new ValidationError(CsfConstants.FIELD_AUTO_ENROLL_IND,ErrorCodes.ERROR_AE_NOT_ALLOWED_IN_PLAN));

				/* CSF. 229   EZStart is not available for plan that has 
				 * immediate or annual Plan Entry Frequency for EEDEF money type
				 * or if plan:plan entry frequency is not one of the valid AE frequency (mthly, semi-annually and qtrly)
				 */
			} else {			    
				if (!StringUtils.equals(Constants.PLAN_ENTRY_FREQUENCY_MONTHLY, planDataLite.getPlanEntryFrequencyForMoneyTypeEedef())
						&& !StringUtils.equals(Constants.PLAN_ENTRY_FREQUENCY_SEMI_ANNUALLY, planDataLite.getPlanEntryFrequencyForMoneyTypeEedef()) 
						&& !StringUtils.equals(Constants.PLAN_ENTRY_FREQUENCY_QUARTERLY, planDataLite.getPlanEntryFrequencyForMoneyTypeEedef()) ) {
					errors.add(new ValidationError(CsfConstants.FIELD_AUTO_ENROLL_IND,ErrorCodes.ERROR_AE_NOT_ALLOWED_FOR_PLAN_WITH_CONFLICTING_PLAN_ENTRY_FREQUENCY));
				}			            
			}
			//CSF.232	If “Turn on JH EZstart” = “Yes”, 
			//the “Initial enrollment date for the EZstart service” must be specified
			if (StringUtils.isEmpty(csfForm.getInitialEnrollmentDate())) {
				errors.add(new ValidationError(CsfConstants.FIELD_INITIAL_ENROLLMENT_DATE,ErrorCodes.ERROR_INVALID_ENROLLMENT_DATE_VALUE));
			}else
				// CSF. 234 initialEnrollmentDateFormat validations
			if (!StringUtils.equals(((CsfForm)csfForm.getClonedForm()).getInitialEnrollmentDate(), csfForm.getInitialEnrollmentDate())){
	             try {
	                 Date initialEnrollmentDt = CsfConstants.initialEnrollmentDateFormat.parse(csfForm
	                         .getInitialEnrollmentDate());
	                 if (initialEnrollmentDt.before(new Date())) {
	                	 errors.add(new ValidationError(CsfConstants.FIELD_ENROLLMENT_DATE,ErrorCodes.ERROR_ENROLLMENT_DATE_LESS_THAN_CURRENT_DATE));
	                 }
	             } catch (ParseException pe) {
	            	 errors.add(new ValidationError(CsfConstants.FIELD_ENROLLMENT_DATE,ErrorCodes.ERROR_INVALID_ENROLLMENT_DATE_FORMAT));
	             }
	         }
			
			/* CSF.453 If “Turn on JH EZstart” = “Yes” 
			 * Plan attribute “An eligible employee who has satisfied the eligibility requirements will
			 * enter the plan on the plan entry date that:” must be set to “Coincides with or immediately follows the eligibility date” 
			 */			
			String planEntryBasis = planDataLite.getPlanEntryBasis() != null ? planDataLite.getPlanEntryBasis() : "";
			if ( !PlanData.ELIGIBLE_EMPLOYEE_PLAN_ENTRY_DATE_COINCIDENT_NEXT_CODE.equalsIgnoreCase(planEntryBasis)) {
				int	errorMessage = ErrorCodes.EC_EMPLOYEE_PLAN_ENTRY_BASIS_CODE_SHOULD_BE_CN_FOR_EZSTART;
				errors.add(new ValidationError(CsfConstants.FIELD_AUTO_ENROLL_IND,errorMessage));
			}
		}
		
		// “Turn on JH EZstart” is changed
		String oldAEValue = ((CsfForm)csfForm.getClonedForm()).getAutoEnrollInd();
		String newAEValue = csfForm.getAutoEnrollInd();
		if(!StringUtils.equals(newAEValue, oldAEValue)){
			
			//“Turn on JH EZstart” is changed to "Yes"
			if(CsfConstants.CSF_YES.equals(csfForm.getAutoEnrollInd())) {

				//CSF.224 If “Turn on JH EZstart” is changed to “Yes” and the contract does not have EEDEF money type 
				boolean EEDEFMoneyTypePresent = false;
				for (MoneyTypeVO moneyType : csfForm.getContractMoneyTypesList()) {
					if ((moneyType.getId().equalsIgnoreCase(ServiceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL))) {
						EEDEFMoneyTypePresent = true;
						break;
					}
				}
				if (!EEDEFMoneyTypePresent) {
					errors.add(new ValidationError(CsfConstants.FIELD_AUTO_ENROLL_IND,
							ErrorCodes.CONTRACT_MUST_HAVE_EEDEF_MONEY_TYPE_FOR_EZSTART));
				}

				// CSF.225 If “Turn on JH EZstart” is changed to “Yes” and 
				//there is no Contract Money Type History End effective date equal to 9999-12-31 for the EEDEF money type 
				Date deletionDate = csfForm
				.getMoneyTypesWithFutureDatedDeletionDate().get(ServiceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL);
				if (deletionDate != null) {
					errors.add(new ValidationError(CsfConstants.FIELD_AUTO_ENROLL_IND,
							ErrorCodes.EEDEF_MONEY_TYPE_HAS_FUTURE_DELETION_DATE, new Object[] { CsfConstants.ecDeletionDateFormat.format(deletionDate) }));
				}

				// CSF. 226 If “Turn on JH EZstart” is changed to “Yes”
				warnings.add(new ValidationError(CsfConstants.FIELD_AUTO_ENROLL_IND,ErrorCodes.WARNING_PS_RESPONSIBLE_FOR_OPT_OUT_DAYS,ValidationError.Type.warning));
				
			}else{
				// CSF.227	If “Turn on JH EZstart” is changed to “No” and current date >= “Initial enrollment date for the EZstart service
				if (StringUtils.isNotEmpty(csfForm.getInitialEnrollmentDate())) {
					try {
						Date initialEnrollmentDt = CsfConstants.initialEnrollmentDateFormat.parse(csfForm
								.getInitialEnrollmentDate());
						if ( initialEnrollmentDt.before(new Date())) {
							warnings.add(new ValidationError(CsfConstants.FIELD_AUTO_ENROLL_IND,ErrorCodes.WARNING_FOR_AE_TURNING_OFF, ValidationError.Type.warning)); 
						}
					} catch (ParseException pe) {
						//logger.error(pe);
					}
				}
			}
			//CSF.231 If “Turn on JH EZstart” is changed and when the ‘save” button is clicked,
			//1. if Apollo is unavailable (either Apollo or the link to it is down), then 
			// if AE changed check Apollo for availability
			try{
				if(!ContractServiceDelegate.getInstance().getApolloAvailabilityForAEFeatureUpdates())
					errors.add(new ValidationError(CsfConstants.FIELD_AUTO_ENROLL_IND,ErrorCodes.ERROR_APOLLO_UNAVAILABLE_FOR_AE));
			}catch(Exception e)
			{
				errors.add(new ValidationError(CsfConstants.FIELD_AUTO_ENROLL_IND,ErrorCodes.ERROR_APOLLO_UNAVAILABLE_FOR_AE));
			}
		}

		//CSF. 230    EZStart  edits invoked if one or more of the EZStart attributes are changed
		if (hasAnyAutoEnrollmenAttributesChanged((CsfForm)csfForm.getClonedForm(), csfForm)) {
			// onboarding added these edits
			if (CsfConstants.CSF_YES.equals(csfForm.getAutoEnrollInd())) {		    
				// CSF  Initial enrollment date must be less than Plan AE Effective date
				if (StringUtils.isNotEmpty(csfForm.getInitialEnrollmentDate())) {
					try {
						Date initialEnrollmentDt = CsfConstants.initialEnrollmentDateFormat.parse(csfForm
								.getInitialEnrollmentDate());
						if (planDataLite.getAutomaticEnrollmentEffectiveDate() == null 
								|| initialEnrollmentDt.before(planDataLite.getAutomaticEnrollmentEffectiveDate())) {
							errors.add(new ValidationError(CsfConstants.FIELD_INITIAL_ENROLLMENT_DATE,
									ErrorCodes.ERROR_INITIAL_ENROLLMENT_DATE_DOES_NOT_MATCH_PLAN)); 
						}
					} catch (ParseException pe) {
						//logger.error(pe);
					}
				}
			}
		}
	}
	/**
	 * Method to determine whether the JH Ezstart values are changed or not.
	 * @param clonedForm
	 * @param csfForm
	 * @return
	 */
    private boolean hasAnyAutoEnrollmenAttributesChanged(CsfForm clonedForm, CsfForm csfForm) {
        
        boolean hasChanged = false;
        
        if (!StringUtils.equals(clonedForm.getAutoEnrollInd(), csfForm.getAutoEnrollInd()) )
            hasChanged = true;
        else if (!StringUtils.equals(clonedForm.getInitialEnrollmentDate(), csfForm.getInitialEnrollmentDate()) )
            hasChanged = true; 
        else if (!StringUtils.equals(clonedForm.getDirectMailInd(), csfForm.getDirectMailInd()) )
            hasChanged = true;
        
        return hasChanged;
    }
    
    /**
     *  Validate DirectMail
	 *  CSF. 235 
	 *  -1: 1.If the Contract does not have a Client Mailing Address on Apollo, issue an error message when ‘Save’ is clicked., 
	 *  indicating that a  Client Mailing Address must be saved on Apollo before this feature can be turned on (CMA ID xxxxx).
	 *  -2: If  the Contract does not have one DIO fund on Apollo (either no DIO fund or more than one DIO fund), issue an error message when ‘Save’ is clicked, indicating 
	 *  that Contract must have one default investment option fund Apollo before this feature can be turned on (CMA ID xxxx)
	 *  
	 *   CSF.236	If “Direct mail of enrollment materials” is changed to “No” (or “Turn on JH EZstart” is changed to “No”) 
	 * and the current date is within 52 days of the plan’s upcoming plan entry date 
	 * 
	 * @param csfForm
	 * @param errors
	 */
	private void validateDirectMail(CsfForm csfForm, HttpServletRequest request, PlanDataLite planDataLite, 
			Collection<GenericException> errors, Collection<GenericException> warnings) {
		// first detrmine if the value was changed to yes
		Contract contract =PsController.getUserProfile(request).getCurrentContract();
		//boolean  isCurrentlyDM =  contract.isDM();
		String oldValue = ((CsfForm)csfForm.getClonedForm()).getDirectMailInd();
		String newValue = csfForm.getDirectMailInd();
		// this is to insure that 'DM' is turned off when AE is turned off
		if(CsfConstants.CSF_NO.equals(csfForm.getAutoEnrollInd()) && CsfConstants.CSF_YES.equals(csfForm.getDirectMailInd())){
			csfForm.setDirectMailInd (CsfConstants.CSF_NO);
		}
		if(!StringUtils.equals(newValue, oldValue) ){
			//CSF. 235
			if( CsfConstants.CSF_YES.equals(newValue)){ // DM indicator was changed from no to yes
				// check if contract has client address
				if (!isValidClientAddress(contract.getContractNumber(), request))//has to get client address , if empty - raise error
					//to do check with Apollo people if there is any address validation done on Apollo, if not
					//have to find out from Mark and Jim what would constitute a valid address
					errors.add(new ValidationError(CsfConstants.FIELD_DIRECT_MAIL_IND,ErrorCodes.ERROR_DM_MISSING_ADDRESS)); //set address error!!!  
				//  }
				//  next access Apollo for DIO option

				try{
					if(!contractDIOAllowsDM(contract.getContractNumber()))
						errors.add(new ValidationError(CsfConstants.FIELD_DIRECT_MAIL_IND,ErrorCodes.ERROR_DM_UNSUPPORTIVE_DIO)); //set DIO does not allow DM error! 
				}catch (SystemException e)	
				{
					//log the exception?	   
					errors.add (new ValidationError(CsfConstants.FIELD_DIRECT_MAIL_IND,ErrorCodes.ERROR_DM_APOLLO_UNAVAILABLE)); // set Apollo out of service error;
				}
				
			// CSF. 236
			}else if( CsfConstants.CSF_NO.equals(newValue)){
				Date basePlanEntryDate = null;
				String initialEnrollmentDate = null;
				String planFrequency = null;
				try {
					if(planDataLite.getFirstPlanEntryDate() != null)
						basePlanEntryDate = planDataLite.getFirstPlanEntryDate().getAsDate();
					initialEnrollmentDate =csfForm.getInitialEnrollmentDate();
					//have to map the frequency to ContractService notation
					if(basePlanEntryDate !=null)
						planFrequency = planDataLite.getPlanEntryFrequencyForMoneyTypeEedef();
					if (basePlanEntryDate !=null && planFrequency != null 
							&&initialEnrollmentDate !=null && initialEnrollmentDate.length()>0){
						if( CsfUtil.isDM52Warning(basePlanEntryDate, planFrequency, initialEnrollmentDate)){
							warnings.add (new ValidationError(CsfConstants.FIELD_DIRECT_MAIL_IND,ErrorCodes.WARNING_FOR_TURNING_OFF_DM,   ValidationError.Type.warning)); 
						}
					}
				} catch (Exception e) {
					logger.error("Exception calculating nextPED for contract " + planDataLite.getPlanId()+ ", initialEnrollmentDate " + initialEnrollmentDate
							+ " basePlanEntryDate " + basePlanEntryDate +  e);
				}
			}
		}
	}
	/**
	 * Method to check the Direct mail of enrollment materials” service feature cannot be 
	 * turned on as this contract has either no Default Investment Option specified 
	 * or has more than one specified. 
	 * 
	 * @param contractNumber
	 * @return
	 * @throws SystemException
	 */
	private boolean contractDIOAllowsDM (int contractNumber) throws SystemException {

		boolean allowDM =false;
		allowDM =com.manulife.pension.delegate.ContractServiceDelegate.getInstance().contractDIOAllowDM(contractNumber);
		return allowDM;

	}  
	/**
	 * Method to check whether the Contract has client address or not.
	 * @param contractNumber
	 * @param request
	 * @return
	 */
	public boolean isValidClientAddress (int contractNumber,HttpServletRequest request )
	{
		ContractProfileVO contractProfile =(ContractProfileVO) request.getAttribute(Constants.CONTRACT_PROFILE);
		if (contractProfile == null){
			try {
				contractProfile = ContractServiceDelegate.getInstance().getContractProfileDetails(
						contractNumber, Environment.getInstance().getSiteLocation());
			} catch (SystemException e) {
				logger.error(e);
			}
		}
		boolean validAddress =false;
		ContractProfileVO.Address address =contractProfile.getAddress();
		if(address!=null)
			if(address.getLine1()!=null&& address.getLine1().length()>0 && address.getCity()!=null&& address.getCity().length()>0)
				validAddress =true;
		return validAddress;
	}
	/**
	 * Validate Auto Contribution Increse fields
	 *
	 * @param csfForm
	 * @param errors
	 */
	private void validateAutoContributionIncrease(CsfForm csfForm,  PlanDataLite planDataLite, 
			Collection<GenericException> errors, Collection<GenericException> warnings ) {
		
		if( hasAnyAutoContributionIncreaseChanged((CsfForm)csfForm.getClonedForm(), csfForm)){
			
			if(StringUtils.equals(CsfConstants.CSF_YES, csfForm.getAutoContributionIncrease())) {
				//CSF.240	If “Turn on JH EZIncrease” is changed to “Yes” and Plan attribute 
				//“ACI Applies to” = Automatically-enrolled participants that have not made an election”,
				if("AU".equals(planDataLite.getAciApplyTo())){
					errors.add(new ValidationError(CsfConstants.FIELD_AUTO_CONTRIBUTION_INCREASE,ErrorCodes.ERROR_ACI_NOT_ALLOWED_WHEN_PLAN_APPLIES_IS_AU));
				}
			}else{
				// CSF.247	If “Turn on JH EZincrease” is changed from “Yes” to “No” and “First scheduled increase starts on (mm/dd/yyyy)” is <= current date
				if(StringUtils.isNotEmpty(csfForm.getAciAnniversaryYear()) 
						&& StringUtils.isNumeric(csfForm.getAciAnniversaryYear())
						&& StringUtils.isNotEmpty(csfForm.getAciAnniversaryDate())){
					Calendar annivCal = new GregorianCalendar();
					Date annivDate = (new DayOfYear(csfForm.getAciAnniversaryDate())).getAsDateNonLeapYear();
					annivCal.setTime(annivDate);
					annivCal.set(Calendar.YEAR, Integer.parseInt(csfForm.getAciAnniversaryYear()));
					
					if(annivCal.getTime().before(new Date())){
						warnings.add(new ValidationError(CsfConstants.FIELD_AUTO_CONTRIBUTION_INCREASE,ErrorCodes.WARNING_ACI_MAY_ALREADY_HAVE_HAPPENED,  ValidationError.Type.warning));
					}
				}
			}
		}
		
		if(StringUtils.equals(CsfConstants.CSF_YES, csfForm.getAutoContributionIncrease())) {

			/*if (StringUtils.isEmpty(csfForm.getAciAnniversaryDate())) {
				errors.add(new ValidationError("aciAnniversaryDate",1305));
			}*/
			//CSF.246 If “Turn on JH EZincrease” = “Yes”, then the “First scheduled increase starts on - year” must be specified.
			if (StringUtils.isEmpty(csfForm.getAciAnniversaryYear()) || !StringUtils.isNumeric(csfForm.getAciAnniversaryYear())) {
				errors.add(new ValidationError(CsfConstants.FIELD_ACI_ANNIVERSARY_YEAR,ErrorCodes.WARNING_ACI_YEAR_VALUE));
			}else	if (StringUtils.isNotEmpty(csfForm.getAciAnniversaryDate())) {
				Calendar annivCal = new GregorianCalendar();
				Date annivDate = (new DayOfYear(csfForm.getAciAnniversaryDate())).getAsDateNonLeapYear();
				annivCal.setTime(annivDate);
				annivCal.set(Calendar.YEAR, Integer.parseInt(csfForm.getAciAnniversaryYear()));
				
				//CSF.241	If “Turn on JH EZIncrease” is “Yes” and Plan attribute “Does the plan provide for automatic contribution increase?” is “Yes” 
				//and CSF attribute “First scheduled increase starts on” date (mm/dd/yyyy) < Plan attribute “ACI Effective date”,
				if (StringUtils.equals(Constants.YES, planDataLite.getAciAllowed()) &&
						   ( planDataLite.getAciEffectiveDate() == null 
								   || annivCal.getTime().before(planDataLite.getAciEffectiveDate()))) {
					errors.add(new ValidationError(CsfConstants.FIELD_ACI_ANNIVERSARY_YEAR,ErrorCodes.ERROR_ACI_ANNIVERSARY_DATE_BEFORE_PLAN_ACI_EFFECTIVE_DATE));
				}
				
				//CSF. 248 a) The “First scheduled increase starts on” (mm/dd/yyyy) must be >= current date
				if(annivCal.after(new Date())){
					errors.add(new ValidationError(CsfConstants.FIELD_ACI_ANNIVERSARY_YEAR,ErrorCodes.ERROR_ACI_ADT_CAN_NOT_BE_IN_PAST)); 
				}
				//CSF. 248	b)The “First scheduled increase starts on - year” cannot be > (current system date year + 2 years)
				Calendar currentDate = new GregorianCalendar();
				currentDate.setTime(new Date());
				if(annivCal.get(Calendar.YEAR) > currentDate.get(Calendar.YEAR)+2){
					errors.add(new ValidationError(CsfConstants.FIELD_ACI_ANNIVERSARY_YEAR,ErrorCodes.ERROR_ACI_YEAR_CAN_NOT_BE_MORETHAN_2_YEARS));
				}
			}
			
			//CSF.242	If “Turn on JH EZincrease” is “Yes” and Plan attribute “Does the plan provide for automatic contribution increases?” is not “Yes”, 
			if (!StringUtils.equals(Constants.YES, planDataLite.getAciAllowed())) {
				errors.add(new ValidationError(CsfConstants.FIELD_AUTO_CONTRIBUTION_INCREASE,ErrorCodes.ERROR_ACI_AUTO_METHOD_NOT_ALLOWED_WHEN_PLAN_DOES_NOT_HAVE_ACI_ALLOWED));
			}
			//CSF.249- Removed. If CSF attribute “First scheduled increase starts on” (mm/dd/yyy) is earlier than the Plan attribute “Automatic contribution increase effective date
			/*if (StringUtils.isNotEmpty(csfForm.getAciAnniversaryDate()) &&
					 StringUtils.isNotEmpty(csfForm.getAciAnniversaryYear()) ) {
				Calendar annivCal = new GregorianCalendar();
				Date annivDate = (new DayOfYear(csfForm.getAciAnniversaryDate())).getAsDateNonLeapYear();
				annivCal.setTime(annivDate);
				annivCal.set(Calendar.YEAR, Integer.parseInt(csfForm.getAciAnniversaryYear()));
				if (planDataLite.getAciEffectiveDate() == null 
						|| annivCal.getTime().before(planDataLite.getAciEffectiveDate())) {
					errors.add(new ValidationError(CsfConstants.FIELD_AUTO_CONTRIBUTION_INCREASE,ErrorCodes.ERROR_CSF_ACI_DATE_IS_LESSTHAN_PLAN_ACI));
				}
			}*/
		}
	}

	/**
	 * Method to determine whether the JH EZIncrease values are changed or not.
	 * @param clonedForm
	 * @param csfForm
	 * @return
	 */
    private boolean hasAnyAutoContributionIncreaseChanged(CsfForm clonedForm, CsfForm csfForm) {
        
        boolean hasChanged = false;
        
        if (!StringUtils.equals(clonedForm.getAutoContributionIncrease(), csfForm.getAutoContributionIncrease()) )
            hasChanged = true;        
        /*else if (!StringUtils.equals(clonedForm.getAciAnniversaryDate(), csfForm.getAciAnniversaryDate()) )
            hasChanged = true; 
        else if (!StringUtils.equals(clonedForm.getAciAnniversaryYear(), csfForm.getAciAnniversaryYear()) )
            hasChanged = true;
        else if (!StringUtils.equals(clonedForm.getIncreaseAnniversary(), csfForm.getIncreaseAnniversary()) )
            hasChanged = true;*/
      
        return hasChanged;
    }
	 /**
     * Method to validate the Vesting CSF
     * 
     * @param csfForm
     * @param errors
     * @param planDataLite
     */
    private void validateVesting(CsfForm csfForm,  PlanDataLite planDataLite,
    		Collection<GenericException> errors, Collection<GenericException> warnings) {
        
    	//CSF attribute “Vesting will be” = “Calculated by John Hancock” 
    	if (CsfConstants.PLAN_VESTING_CALCULATION.equals(csfForm.getVestingPercentagesMethod())) {
    		
    		//CSF.254	If CSF attribute “Vesting will be” = “Calculated by John Hancock” 
    		//and Plan attribute “Service crediting method” is “Unspecified”, 
    		 if (StringUtils.equals(CsfConstants.VESTING_SERVICE_CREDIT_METHOD_UNSPECIFIED,
    				 planDataLite.getVestingServiceCreditMethod()) && planDataLite.isAnyERMoneyTypeNotFullyVested()) {
                 errors.add(new ValidationError(CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD,ErrorCodes.ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_SCM_UNSPECIFIED));
             }
    		 //CSF.255	If CSF attribute “Vesting will be” = “Calculated by John Hancock”
    		 //and Plan attribute “Vesting computation shall be” is “Unspecified”,
    		 if(StringUtils.equals(CsfConstants.VESTING_COMPUTATION_PERIOD_UNSPECIFIED, 
    				 planDataLite.getVestingComputationPeriod())){
    			 errors.add(new ValidationError(CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD,ErrorCodes.ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_VESTING_UNSPECIFIED));
    		 }
    		 // CSF.256	If CSF attribute “Vesting will be” = “Calculated by John Hancock” 
    		 //and Plan attribute “Does the plan have two or more vesting schedules for any single money type” is “Unspecified”, 
    		 if(StringUtils.equals(CsfConstants.VESTING_COMPUTATION_PERIOD_UNSPECIFIED, 
    				 planDataLite.getMultipleVestingSchedulesForOneSingleMoneyType())){
    			 errors.add(new ValidationError(CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD,ErrorCodes.ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_TWO_OR_MORE_VESTINGS_UNSPECIFIED));
    		 }
    		 //CSF.257 If CSF attribute “Vesting will be” = “Calculated by John Hancock” 
    		 // and Plan attribute “Vesting schedule” is missing for at least one ER money type, 
    		 if(planDataLite.isVestingMissedForAnyERMoneyType()){
    			 errors.add(new ValidationError(CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD,ErrorCodes.ERROR_CALCULATED_NOT_ALLOWED_WHEN_VESTING_MISSED_FOR_ANY_ER_TYPES));
    		 }
    		 
            /* CSF. 258.   If the CSF “Vesting percentages to be:” = “Calculated by John Hancock” 
             * and Plan: “Does the plan have two or more vesting schedule for any single money type?” =”Yes”, 
             */
            if (StringUtils.equals(Constants.YES, planDataLite.getMultipleVestingSchedulesForOneSingleMoneyType())) {
                errors.add(new ValidationError(CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD,ErrorCodes.ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_HAS_MULTIPLE_VESTING_SCHEDULES));
            } else {
                /* CSF.259   If the CSF “Vesting service percentages to be” is set to “Calculated by John Hancock” 
                 * and the Plan: “Vesting service crediting method” = “Hours of service’ 
                 * and Plan “The vesting computation period shall be …” = “the date an Employee first performs an Hour of service and each anniversary thereof”, 
                 */
                if (StringUtils.equals(CsfConstants.VESTING_SERVICE_CREDIT_METHOD_HOURS_OF_SERVICE, planDataLite.getVestingServiceCreditMethod())
                        && StringUtils.equals(CsfConstants.VESTING_COMPUTATION_BASED_ON_HOS_FIRST_AND_EACH_ANNIVERSARY_THEREOF_CODE, planDataLite.getVestingComputationPeriod())) {
                    errors.add(new ValidationError(CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD,ErrorCodes.ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_HAS_HOURS_OF_SERVICE_AND_ANNIVERSARY));                    
                } else {
                /* CSF.260. If the “Vesting service percentages to be” is set to “Calculated by John Hancock” 
                 * and Plan: “Vesting service crediting method” = “Elapsed time’ 
                 * and Plan: “The vesting computation period shall be…” = “the plan year”, 
                 */
                    if (StringUtils.equals(CsfConstants.VESTING_SERVICE_CREDIT_METHOD_ELAPSED_TIME, planDataLite.getVestingServiceCreditMethod())
                            && StringUtils.equals(CsfConstants.VESTING_COMPUTATION_PERIOD_PLAN_YEAR_CODE, planDataLite.getVestingComputationPeriod())) {
                        errors.add(new ValidationError(CsfConstants.FIELD_VESTING_PERCENTAGES_METHOD,ErrorCodes.ERROR_CALCULATED_NOT_ALLOWED_WHEN_PLAN_HAS_ELAPSED_TIME_AND_PLAN_YEAR));
                    }                        
                }                
            }
        } 
    }
    
    /**
     * Method to validate Online Loans
     * 
     * @param csfForm
     * @param errors
     * @param planDataLite
     */
    
    private void validateOnlineLoans(CsfForm csfForm, PlanDataLite planDataLite, Contract contract,
    		 Collection<GenericException> errors, Collection<GenericException> warnings){
 	   
    	// CSF.270	If “Online Loans” = “Yes” and (Plan attribute “Does the plan allow loans” not = “Yes” OR Apollo Loans not = “Yes”), 
		if(CsfConstants.CSF_YES.equals(csfForm.getAllowOnlineLoans()) 
				&& !StringUtils.equals(ServiceFeatureConstants.YES, planDataLite.getLoansAllowedInd())
				&& CsfConstants.CSF_YES.equals(csfForm.getLoanRecordKeepingInd())){
			errors.add(new ValidationError(CsfConstants.FIELD_ALLOW_ONLINE_LOANS,ErrorCodes.ERROR_ONLINE_LOANS_NOT_SUPPORTED));
		}
    	
    	CsfForm oldForm = (CsfForm) csfForm.getClonedForm();

		// “Online Loans” is change
		if(!StringUtils.equals(oldForm.getAllowOnlineLoans(), csfForm.getAllowOnlineLoans())){
			
			if(isValueEitherYesOrTrue(csfForm.getAllowOnlineLoans())){
				//CSF.271 If “Online Loans” is changed to “Yes” display a warning message
				warnings.add(new ValidationError(CsfConstants.FIELD_ALLOW_ONLINE_LOANS,ErrorCodes.WARNING_FOR_TURNING_ON_LOANS, ValidationError.Type.warning));
				
				int outStandingLoanCount = 0;

				try {
					outStandingLoanCount =getOutstandingOldILoanRequestCount(contract.getContractNumber());
				} catch (SystemException e) {
					logger.error(e);
				}		
			    //CSF. 272 If there is any outstanding old i:loan request  in any of these statuses listed below 
				// and expiry date is greater than current date CMA key = 60598  
				if(outStandingLoanCount > 0 ){
					warnings.add(new ValidationError(new String[]{CsfConstants.FIELD_ALLOW_ONLINE_LOANS},
							ErrorCodes.WARNING_OUTSTANDING_iLOAN_COUNT, new Object[]{outStandingLoanCount,contract.getContractNumber()}, ValidationError.Type.warning));
				}
			}
		}
		
		// “Who will review the loan requests prior to approval” is changed 
		if(!StringUtils.equals(oldForm.getWhoWillReviewLoanRequests(), csfForm.getWhoWillReviewLoanRequests())){
			
			//CSF. 276 if the value changes from from 'TPA' to 'Plan Sponsor'
			if (CsfConstants.WHO_WILL_REVIEW_WD_PS.equals(csfForm.getWhoWillReviewLoanRequests())){
				warnings.add(new ValidationError(CsfConstants.FIELD_WHO_WILL_REVIEW_LOAN_REQUESTS,ErrorCodes.WARNING_LOANS_ARE_NO_LONGER_SUPPORTED, ValidationError.Type.warning));
			}
			//CSF. 277 if the value changes from from 'Plan Sponsor' to 'TPA'
			else if (CsfConstants.WHO_WILL_REVIEW_WD_TPA.equals(csfForm.getWhoWillReviewLoanRequests())) {
				warnings.add(new ValidationError(CsfConstants.FIELD_WHO_WILL_REVIEW_LOAN_REQUESTS,ErrorCodes.WARNING_REVIEW_LOAN_PERMISSION_FOR_TPA, ValidationError.Type.warning));
			}
		}
    }
    
    /**
     * Method to validate IWithdrawals
     * 
     * @param csfForm
     * @param errors
     * @param planDataLite
     * @throws SystemException 
     */
    
    private void validateIWithdrawals(int contractId, CsfForm newForm,  PlanDataLite planDataLite, 
    		Collection<GenericException> errors, Collection<GenericException> warnings) throws SystemException{
    	CsfForm oldForm = (CsfForm) newForm.getClonedForm();
    	
		String oldReviewIndicator = oldForm.getWhoWillReviewWithdrawals();
		String newReviewIndicator = newForm.getWhoWillReviewWithdrawals();
		
		if (!StringUtils.equals(oldReviewIndicator, newReviewIndicator)) {
			// WHO WILL REVIEW WITHDRAWAL CSF change
			
			//CSF.285 if the value changes from 'TPA' to 'PS' or if the value changes from 'No Review' to 'PS'
			if (CsfConstants.WHO_WILL_REVIEW_WD_PS.equals(newReviewIndicator)) {
				warnings.add(new ValidationError(CsfConstants.FIELD_WHO_WILL_REVIEW_WITHDRAWALS,ErrorCodes.WARNING_WITHDRAWAL_REVIEW_STAGE_PERMISSION_FOR_PS, ValidationError.Type.warning));
			}
			//CSF.287 if the value changes from 'TPA' to 'No Review' or if the value changes from 'PS' to 'No Review'
			else if(CsfConstants.WHO_WILL_REVIEW_WD_NOREVIEW.equals(newReviewIndicator)){
				warnings.add(new ValidationError(CsfConstants.FIELD_WHO_WILL_REVIEW_WITHDRAWALS,ErrorCodes.WARNING_WITHDRAWALS_WILL_NO_LOANGER_HAVE_REVIEW_STAGE, ValidationError.Type.warning));
			}
			//CSF.286 if the value is changed from “Plan Sponsor” to “TPA”,
			if(CsfConstants.WHO_WILL_REVIEW_WD_PS.equals(oldReviewIndicator) &&
					CsfConstants.WHO_WILL_REVIEW_WD_TPA.equals(newReviewIndicator)){
				String permissionCode = PermissionType.getPermissionCode(
						PermissionType.REVIEW_WITHDRAWALS);
				List <Long> clientUsersWithReviewWithdrawals = CsfDataHelper.getInstance().retrieveClientUsersByPermission(contractId, permissionCode );
				if (CollectionUtils.isNotEmpty(clientUsersWithReviewWithdrawals)) {
					warnings.add(new ValidationError(CsfConstants.FIELD_WHO_WILL_REVIEW_WITHDRAWALS,ErrorCodes.WARNING_WITHDRAWAL_REVIEW_STAGE_PERMISSION_FOR_TPA, ValidationError.Type.warning));
				}
			}
			//CSF.288 if the value is changed from “No review” or “Plan Sponsor” to “TPA”,
			//and there are Client user with Review i:withdrawals permission = “Yes”, 
			else if((CsfConstants.WHO_WILL_REVIEW_WD_PS.equals(oldReviewIndicator) 
					||CsfConstants.WHO_WILL_REVIEW_WD_NOREVIEW.equals(oldReviewIndicator))
					&&	CsfConstants.WHO_WILL_REVIEW_WD_TPA.equals(newReviewIndicator)){
				warnings.add(new ValidationError(CsfConstants.FIELD_WHO_WILL_REVIEW_WITHDRAWALS,ErrorCodes.WARNING_IWITHDRAWAL_PERMINSSION_TO_NO, ValidationError.Type.warning));
			}
		}
		//CSF.194: If “I:withdrawals” is changed to “No”, then display a warning message
		if (!StringUtils.equals( oldForm.getParticipantWithdrawalInd(), newForm.getParticipantWithdrawalInd()) ){
			if(!isValueEitherYesOrTrue(newForm.getParticipantWithdrawalInd())){
				warnings.add(new ValidationError(CsfConstants.FIELD_PARTICIPANT_WITHDRAWAL_IND,ErrorCodes.WARNING_WITHDRAWALS_WILL_NO_LOANGER_BE_ALLOWED, ValidationError.Type.warning));
			}
		}
    }
    
	/**
	 * Method to validate the Payroll Cutoff
	 * 
	 * @param noofDays
	 * @return
	 */
	private static boolean validatePayrollCutoff(String noofDays){
		boolean isValid = true;
		
		
		if (StringUtils.isNotEmpty(noofDays)) {
			isValid = true;

			try {
				int makeChangesDays = Integer.parseInt(noofDays);
				if (makeChangesDays < 5 || makeChangesDays > 30) {
					isValid = false;
				}
			} catch (Exception e) {
				isValid = false;
			}
		}
		return isValid;
	}
	/**
	 * Method to validate the Percentage
	 * @param percentage
	 * @return
	 */
	private static boolean validatePercentage(String percentage)
	{
		boolean isValid = true;
		try {
			int pr = Integer.parseInt(percentage);
			if (pr < 1 || pr > 100) {
				isValid = false;
			}
		} catch (Exception e) {
			isValid = false;
		}
		return isValid;
	}
	/**
	 * Method to validate the Deferral Percentages
	 * @param minPer
	 * @param maxPer
	 * @return
	 */
	private static boolean validateDeferralPercentages(String minPer, String maxPer){
		boolean isValid = true;
		try {
			int min  = Integer.parseInt(minPer);
			int max = Integer.parseInt(maxPer);
			if (min > max) {
				isValid = false;
			}
		} catch (Exception e) {
			isValid = false;
		}
		return isValid;
	}

	/**
	 * Method to validate the amount
	 * @param amount
	 * @return
	 */
	private static boolean validateAmount(String amount){
		boolean isValid = true;
		try {
			Integer amt = parseAmount(amount);
			if (amt == null || amt < 1 || amt > 99999) {
				isValid = false;
			}
		} catch (Exception e) {
			isValid = false;
		}
		return isValid;
	}
	/**
	 * Method to validate the min and max Deferral amounts
	 * @param minAmt
	 * @param maxAmt
	 * @return
	 */
	private static boolean validateDeferralAmounts(String minAmt, String maxAmt){
		boolean isValid = true;
		try {
			Integer min = parseAmount(minAmt);
			Integer max = parseAmount(maxAmt);
			if ( min > max ) {
				isValid = false;
			}
		} catch (Exception e) {
			isValid = false;
		}
		return isValid;
	}

	/**
	 * Method to parse the Interger value from the String property
	 * @param amount
	 * @return
	 * @throws NumberFormatException
	 */
	private static Integer parseAmount(String amount) throws NumberFormatException{
		String amt = amount.trim();
		int index = amt.indexOf(',');
		if(index != -1)
		{
			if(index < 1 || index > 2)
				return null;
			else
				amt = amt.substring(0,index) + amt.substring(index+1);
		}
		Integer am = Integer.valueOf(amt);
		return am;
	}

	/**
	 * Retrieves the lite verson of Plan data.
	 *
	 * @param request
	 * @param contractId
	 * @return
	 */
	protected PlanDataLite getPlanDataLite(HttpServletRequest request,
			Integer contractId) {

		PlanDataLite planDataLite = (PlanDataLite) request
				.getAttribute(CsfConstants.REQ_PLAN_DATA_LITE);

		if (planDataLite == null) {
			try {
				planDataLite = ContractServiceDelegate.getInstance()
						.getPlanDataLight(contractId);
				request.setAttribute(CsfConstants.REQ_PLAN_DATA_LITE, planDataLite);
			} catch (SystemException e) {
				throw new NestableRuntimeException(e);
			}
		}
		return planDataLite;

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
	 * return number of outstanding old i:loan requests
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	private int getOutstandingOldILoanRequestCount(int contractId) throws SystemException {
		int oldILoanRequestsCount = 0; 
		oldILoanRequestsCount = (LoanServiceDelegate.getInstance().getOutstandingOldILoanRequestCount(contractId)).intValue();
		return oldILoanRequestsCount;
	}
	
	 /**
     * Method to validate the Eligibility Calculation section. 
     * @param actionForm
     * @param request
     * @param userProfile
     * @param planDataLite
     * @param errors
     * @param warnings
     * 
     * @return
     */
    private Collection<GenericException> validateECSection(
			ActionForm actionForm, HttpServletRequest request, UserProfile userProfile, 
			PlanDataLite planDataLite, Collection<GenericException> errors,
			Collection<GenericException> warnings) {

    	
    	// validate - CSF 353 - When the Eligibility Calculation service 
    	// is changed to 'Yes', the service must be turned on for
   	    // at least one money type.
		Collection<ContractServiceFeature> changedCsfCollection = 
			new ArrayList<ContractServiceFeature>();
		
		changedCsfCollection =CsfDataHelper.getInstance().getChangedECcsfValues(
				userProfile.getCurrentContract().getContractNumber(),
				(CsfForm) actionForm,userProfile.getPrincipal(),
				changedCsfCollection);
		
		validateEligibilityCalculationService((CsfForm) actionForm,changedCsfCollection,request,errors, warnings);
    	
    	//CSF 354 - validate plan level eligibility calculation requirements.
    	validatePlanLevelECRequirements((CsfForm) actionForm, request, userProfile,errors);
    	
		return errors;
	}
    
	/**
	 * CSF 208 - When the Eligibility Calculation service is changed to 'Yes', the service must be turned on for
	 * atleast one money type.
	 * 
	 * @param csfForm
	 * @param changedCsfCollection
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Collection<GenericException> validateEligibilityCalculationService(CsfForm csfForm,
			Collection<ContractServiceFeature> changedCsfCollection,
			HttpServletRequest request,Collection<GenericException> errors, Collection<GenericException> warnings) {
		
		CsfForm oldForm = (CsfForm)csfForm.getClonedForm();
		
		String oldAEValue = oldForm.getEligibilityCalculationInd();
		String newAEValue = csfForm.getEligibilityCalculationInd();
		if(!StringUtils.equals(newAEValue, oldAEValue)){
			//CSF.218	If the “Turn on Eligibility Calculation Service” feature is changed to “No”, 
			if (StringUtils.equalsIgnoreCase(CsfConstants.CSF_NO, newAEValue)) {
				warnings.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,ErrorCodes.WARNING_FOR_EC_SERVICE_OFF,ValidationError.Type.warning));
			}
		}
		//CSF.219	If the Eligibility Calculation feature is deselected for any of the money types and the “Turn on Eligibility Calculation Service” is “Yes”,
		if(CsfConstants.CSF_YES.equals(newAEValue) 
				&& StringUtils.equals(newAEValue, oldAEValue)
				&& hasDeselectedMoneyTypes(csfForm) ){
			
			warnings.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,ErrorCodes.WARNING_FOR_EC_SERVICE_ON,ValidationError.Type.warning));
		}
		String eligibilityCalculationCsfValue = csfForm
		.getEligibilityCalculationInd();

		if (StringUtils.equalsIgnoreCase(CsfConstants.CSF_YES,
				eligibilityCalculationCsfValue)) {
			errors=checkIfEEDEFMoneyTypeTurnedOff(changedCsfCollection,errors);
			if(errors.size()>0){
				return errors;
			}
			List eligibilityServiceMoneyTypeList = csfForm
			.getEligibilityServiceMoneyTypes();
			int totalNumberOfMoneyTypes = eligibilityServiceMoneyTypeList
			.size();
			boolean EEDEFMoneyTypePresent=false;
			for (int i = 0; i < totalNumberOfMoneyTypes; i++) {
				EligibilityCalculationMoneyType eCalcMoneyType=csfForm.getEligibilityServiceMoneyTypes().get(i);
				if((eCalcMoneyType.getMoneyTypeName().equalsIgnoreCase(ServiceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL))){
					EEDEFMoneyTypePresent=true;
					if((eCalcMoneyType.getMoneyTypeValue().equalsIgnoreCase(CsfConstants.CSF_YES))){
						break;
					}else{
						errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,ErrorCodes.SELECT_EEDEF_MONEY_TYPE,new Object[]{eCalcMoneyType.getMoneyTypeShortName()}));
						return errors;
					}
				}
			}
			if(!EEDEFMoneyTypePresent){
				errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,ErrorCodes.CONTRACT_MUST_HAVE_EEDEF_MONEY_TYPE));
				return errors;
			}
		}

		return errors;
	}
	/**
	 * CSF.219	If the Eligibility Calculation feature is deselected for any of the money types 
	 * and the “Turn on Eligibility Calculation Service” is “Yes” then returns ture else false
	 * 
	 * @param csfForm
	 * @return
	 */
	private boolean hasDeselectedMoneyTypes(CsfForm csfForm){
		
		boolean hasDeselectedMoneyTypes = false;
		
		CsfForm oldForm = (CsfForm)csfForm.getClonedForm();
		
		List<String> oldMoneyTypes = prepareMoneyTypesList(oldForm.getSelectedMoneyTypes());
		List<String> newMoneyTypes = prepareMoneyTypesList(csfForm.getSelectedMoneyTypes());
		
		newMoneyTypes.remove(newMoneyTypes.size()-1);
		
		if(oldMoneyTypes.size() > newMoneyTypes.size()){
			hasDeselectedMoneyTypes = true;
		}else{
			for(String s: oldMoneyTypes){
				if(!newMoneyTypes.contains(s)){
					hasDeselectedMoneyTypes = true;
				}
			}
		}
		
		
		return hasDeselectedMoneyTypes;
	}
	/**
	 * Method to prepare the non - nullable list of the given money types string array
	 * @param s
	 * @return
	 */
	private List<String> prepareMoneyTypesList(String[] s){
		List<String> moneyTypes = new ArrayList<String>();
		
		if(s != null ){
			for(int i=0; i< s.length; i++){
				if(s[i] != null){
					moneyTypes.add(s[i]);
				}
			}
		}
		return moneyTypes;
	}
	/**
	 * CSF.206	If the “Turn on Eligibility Calculation service” is ‘Yes’, the service cannot be deselected for the EEDEF money type
	 *
	 * @param changedCsfCollection
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Collection<GenericException> checkIfEEDEFMoneyTypeTurnedOff(Collection<ContractServiceFeature> changedCsfCollection,Collection<GenericException> errors){
		for (ContractServiceFeature newFeature : changedCsfCollection) {
			if(StringUtils.equalsIgnoreCase(ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF, newFeature.getName())){
				Collection attributeCollection = newFeature
				.getAttributeNames();
				Iterator attributeIterator = attributeCollection.iterator();
				while (attributeIterator.hasNext()) {
					String attributeName = (String) attributeIterator.next();
					String attributeValue = newFeature.getAttributeValue(attributeName);
					if (attributeName.equalsIgnoreCase(ServiceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL)&&
							ServiceFeatureConstants.NO.equalsIgnoreCase(attributeValue)) {
						errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,ErrorCodes.CANNOT_TURN_OFF_EEDEF_MONEY_TYPE,new Object[]{attributeName}));

					}
				}
			}
		}
		return errors;
	}
	/**
	 * This method validates plan level eligibility calculation requirements,
	 * for those money types with EC on.
	 * 
	 * @param newForm
	 * @param moneyType
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	private Collection<GenericException> validatePlanLevelECRequirements(
			CsfForm newForm, HttpServletRequest request, UserProfile userProfile,
			Collection<GenericException> errors){

		try{
			BusinessParameterValueObject businessParameterObject = PartyServiceDelegate
			.getInstance().getBuisnessParameterValueObject();
			int totalNumberOfMoneyTypes = newForm
			.getEligibilityServiceMoneyTypes().size();
			String eligibilityCalculationCsfValue = newForm
			.getEligibilityCalculationInd();
			if (StringUtils.equalsIgnoreCase(CsfConstants.CSF_YES,
					eligibilityCalculationCsfValue)) {
				int contractId = userProfile.getCurrentContract()
				.getContractNumber();
				PlanDataLite planData=getPlanDataLite(request,contractId);
				String multipleEligibilityRules=planData.getMultipleEligibilityRulesForOneSingleMoneyType();

				//CSF 353[i]
				if(multipleEligibilityRules!=null && !(Constants.NO).equals(multipleEligibilityRules)){
					int	errorMessage = ErrorCodes.MULTIPLE_ELIGIBILITY_RULE_SHOULD_BE_NO;
					errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,errorMessage));
				}
				//CSF 353[e]
				if(planData.getPlanEffectiveDate()==null){
					int	errorMessage = ErrorCodes.PLAN_EFFECTIVE_DATE_IS_REQUIRED;
					errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,errorMessage));
				}
				for (int i = 0; i < totalNumberOfMoneyTypes; i++) {
					EligibilityCalculationMoneyType eCalcMoneyType = newForm
					.getEligibilityServiceMoneyTypes().get(i);
					if ((eCalcMoneyType.getMoneyTypeValue()
							.equalsIgnoreCase(CsfConstants.CSF_YES))) {
						validateMoneyTypeEligibilityCriteria(newForm,
								eCalcMoneyType.getMoneyTypeName(), request,errors,businessParameterObject,planData);
					}
				}
				
				/* CSF.452 If “Turn on Eligibility Calculation service” is “Yes”
				 * Plan attribute “An eligible employee who has satisfied the eligibility requirements will
				 * enter the plan on the plan entry date that:” must be set to “Coincides with or immediately follows the eligibility date”
				 */
				String planEntryBasis = planData.getPlanEntryBasis();
				if(planEntryBasis!=null && !(PlanData.ELIGIBLE_EMPLOYEE_PLAN_ENTRY_DATE_COINCIDENT_NEXT_CODE).equalsIgnoreCase(planEntryBasis)){
					int	errorMessage = ErrorCodes.EC_EMPLOYEE_PLAN_ENTRY_BASIS_CODE_SHOULD_BE_CN_FOR_EC_SERVICE;
					errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,errorMessage));
				}
				
			}
		} catch (SystemException e) {
			logger.error(e); 
		}
		return errors;
	}
	
	/**
	 * This method validates plan level eligibility calculation requirements.
	 * 
	 * @param newForm
	 * @param moneyType
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	private Collection<GenericException> validateMoneyTypeEligibilityCriteria(
			CsfForm newForm, String moneyType, HttpServletRequest request,
			Collection<GenericException> errors,
			BusinessParameterValueObject businessParameterObject,PlanDataLite planData)
			throws SystemException {

		int errorMessage = 0;

		List<MoneyTypeEligibilityCriterion> moneyTypeEligibilityCriteria = newForm
		.getMoneyTypeEligibilityCriteria();
		Iterator<MoneyTypeEligibilityCriterion> eligibilityCriteriaIterator = moneyTypeEligibilityCriteria
		.iterator();

		while (eligibilityCriteriaIterator.hasNext()) {
			MoneyTypeEligibilityCriterion moneyTypeEligibility = (MoneyTypeEligibilityCriterion) eligibilityCriteriaIterator
			.next();

			if (moneyTypeEligibility.getMoneyTypeId().equals(
					moneyType)) {
				// CSF 354 [a] The eligibility calculation service can only be turned on for money types that
				// meet the following conditions:The money type must have a valid eligibility calculation method.  I.e., it must either be: 
				//Set up for Immediate eligibility, or
				//The minimum age is provided (i.e., it is greater than zero) or
				//Hours of service crediting method is selected or
				//Elapsed Time crediting method is selected 
				if ((moneyTypeEligibility.getImmediateEligibilityIndicator() == null || !moneyTypeEligibility
						.getImmediateEligibilityIndicator())
						&& (moneyTypeEligibility.getMinimumAge() == null || moneyTypeEligibility
								.getMinimumAge().intValue() == 0)
								&& (moneyTypeEligibility.getServiceCreditingMethod() == null || moneyTypeEligibility
										.getServiceCreditingMethod().equals(Constants.SERVICE_CREDITING_METHOD_UNSPECIFIED))) {
					errorMessage = ErrorCodes.SPECIFY_PLAN_ELIGIBILITY_REQUIREMENTS;
					errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,errorMessage,new Object[]{moneyTypeEligibility.getContractMoneyTypeShortName()}));

				} else {
					if (moneyTypeEligibility.getMinimumAge() != null) {
						validateMinimumAge(moneyTypeEligibility,moneyType,errors,businessParameterObject);
					}
					if (moneyTypeEligibility.getHoursOfService()!=null) {
						validateHoursOfService(moneyTypeEligibility,moneyType,errors,businessParameterObject,planData);
					}
					//CSF354
					if (moneyTypeEligibility.getPeriodOfService()!=null) {
						validatePeriodOfService(moneyTypeEligibility,moneyType,errors,businessParameterObject);
					}
				}
				//CSF 354[d]	
				validatePlanEntryFrequency(moneyTypeEligibility,errors);

				//CSF 353[k]	
				validateifMoneyTypeHasFutureDeletionDate(moneyTypeEligibility,newForm.getMoneyTypesWithFutureDatedDeletionDate(),errors);

				break;
			}
		}

		return errors;
	}
	
	/**
	 * CSF 354[d] ‘Plan Entry Frequency’ specified as part of the eligibility requirements for 
	 * the money type, should be one of the following:  
		Monthly		Quarterly		Semi-annual
	 * @param moneyTypeEligibility
	 * @param errors
	 */
	private void validatePlanEntryFrequency(MoneyTypeEligibilityCriterion moneyTypeEligibility,
			Collection<GenericException> errors){
		int errorMessage = 0;
		if(moneyTypeEligibility.getPlanEntryFrequencyIndicator()!=null){
			if (!(moneyTypeEligibility.getPlanEntryFrequencyIndicator().equals(Constants.PLAN_ENTRY_FREQUENCY_MONTHLY)
					|| moneyTypeEligibility.getPlanEntryFrequencyIndicator().equals(Constants.PLAN_ENTRY_FREQUENCY_QUARTERLY) 
					|| moneyTypeEligibility.getPlanEntryFrequencyIndicator().equals(Constants.PLAN_ENTRY_FREQUENCY_SEMI_ANNUALLY))) {
				errorMessage = ErrorCodes.INVALID_PLAN_ENTRY_FREQUENCY;
				errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,errorMessage,new Object[]{moneyTypeEligibility.getContractMoneyTypeShortName()}));
			}
		}
	}
	
	/**
	 * CSF 353[j] Contract Money Type History End effective date equal to 9999-12-31 for the money type 
	 * @param moneyTypeEligibility
	 * moneyTypesWithFutureDatedDeletetionDate
	 * @param errors
	 */
	private void validateifMoneyTypeHasFutureDeletionDate(MoneyTypeEligibilityCriterion moneyTypeEligibility,
			Map<String, Date> moneyTypesWithFutureDatedDeletetionDate, Collection<GenericException> errors) {

		Date deletionDate = moneyTypesWithFutureDatedDeletetionDate.get(moneyTypeEligibility.getMoneyTypeId());
		if(deletionDate != null) {
			errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,ErrorCodes.MONEY_TYPE_HAS_FUTURE_DELETION_DATE,
					new Object[] { moneyTypeEligibility.getContractMoneyTypeShortName(), CsfConstants.ecDeletionDateFormat.format(deletionDate) }));
		}
	}
	
	/**
	 * CSF 354[e] If ‘Minimum Age’ been specified as part of the eligibility requirements for the money type,
	 * it should be greater than or equal to the eligibility calculation lower limit and less 
	 * than or equal to the eligibility calculation upper limit set for the money type 
	 * (based on its source - EE/ER)
	 * @param moneyTypeEligibility
	 * @param moneyType
	 * @param errors
	 * @param businessParameterObject
	 */
	
	private void validateMinimumAge(MoneyTypeEligibilityCriterion moneyTypeEligibility,String moneyType,
			Collection<GenericException> errors,
			BusinessParameterValueObject businessParameterObject){

		int errorMessage = 0;
		int eeMinimumAgeLimit = businessParameterObject
		.getEcEEMinimumAgeLimit().intValue();
		int eeMaximumAgeLimit = businessParameterObject
		.getEcEEMaximumAgeLimit().intValue();
		int erMinimumAgeLimit = businessParameterObject
		.getEcERMinimumAgeLimit().intValue();
		int erMaximumAgeLimit = businessParameterObject
		.getEcERMaximumAgeLimit().intValue();
		if (moneyTypeEligibility.getMoneyTypeCategoryCode() != null
				&& moneyTypeEligibility.getMoneyTypeCategoryCode()
				.equalsIgnoreCase(Constants.MONEY_TYPE_CATEGORY_EE)) {
			if (!(moneyTypeEligibility.getMinimumAge().intValue() >= eeMinimumAgeLimit || moneyTypeEligibility
					.getMinimumAge().intValue() <= eeMaximumAgeLimit)) {
				errorMessage = ErrorCodes.INVALID_MINIMUM_AGE;
				errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,errorMessage, new Object[] {
						moneyTypeEligibility.getContractMoneyTypeShortName(),
						eeMinimumAgeLimit, eeMaximumAgeLimit }));
			}
		} else if (moneyTypeEligibility.getMoneyTypeCategoryCode() != null
				&& moneyTypeEligibility.getMoneyTypeCategoryCode()
				.equalsIgnoreCase(Constants.MONEY_TYPE_CATEGORY_ER)) {
			if (!(moneyTypeEligibility.getMinimumAge().intValue() >= erMinimumAgeLimit || moneyTypeEligibility
					.getMinimumAge().intValue() <= erMaximumAgeLimit)) {
				errorMessage = ErrorCodes.INVALID_MINIMUM_AGE;
				errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,errorMessage, new Object[] {
						moneyTypeEligibility.getContractMoneyTypeShortName(),
						erMinimumAgeLimit, erMaximumAgeLimit }));
			}
		}
	}
	
	/**
	 * CSF 354 [b] If ‘Hours of Service’ has been specified as part of the eligibility requirements 
	 * for the money type, it should be less than or equal to the eligibility calculation 
	 * upper limit set for the money type (based on its source - EE/ER).  . 
	 * @param moneyTypeEligibility
	 * @param moneyType
	 * @param errors
	 * @param businessParameterObject
	 */
	private void validateHoursOfService(MoneyTypeEligibilityCriterion moneyTypeEligibility,
			String moneyType,Collection<GenericException> errors,
			BusinessParameterValueObject businessParameterObject,PlanDataLite planData){
		int errorMessage = 0;
		int eeMaximumLimit = businessParameterObject
		.getEcEEMoneyTypeHoursOfServiceLimit();
		int erMaximumLimit = businessParameterObject
		.getEcERMoneyTypeHoursOfServiceLimit();

		//CSF 353[j]
		String eligibilityCompPeriod=planData.getEligibilityComputationPeriodAfterTheInitialPeriod();
		if (!Constants.ELIGIBILITY_COMP_PERIOD_PLAN_YEAR.equals(eligibilityCompPeriod)
				&& !isExcpetionAdded(errors, ErrorCodes.INVALID_ELIGIBILITY_COMP_PERIOD)) {
			errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,ErrorCodes.INVALID_ELIGIBILITY_COMP_PERIOD));
		}

		if (moneyTypeEligibility.getMoneyTypeCategoryCode() != null
				&& moneyTypeEligibility.getMoneyTypeCategoryCode()
				.equalsIgnoreCase(Constants.MONEY_TYPE_CATEGORY_EE)) {
			if (!(moneyTypeEligibility.getHoursOfService().intValue() <= eeMaximumLimit)) {
				errorMessage = ErrorCodes.HOURS_OF_SERVICE_SHOULD_BE_WITHIN_UPPER_LIMIT;
				errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,errorMessage, new Object[] {
						moneyTypeEligibility.getContractMoneyTypeShortName(),
						eeMaximumLimit }));
			}
		} else if (moneyTypeEligibility.getMoneyTypeCategoryCode() != null
				&& moneyTypeEligibility.getMoneyTypeCategoryCode()
				.equalsIgnoreCase(Constants.MONEY_TYPE_CATEGORY_ER)) {
			if (!(moneyTypeEligibility.getHoursOfService().intValue() <= erMaximumLimit)) {
				errorMessage = ErrorCodes.HOURS_OF_SERVICE_SHOULD_BE_WITHIN_UPPER_LIMIT;
				errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,errorMessage, new Object[] {
						moneyTypeEligibility.getContractMoneyTypeShortName(),
						erMaximumLimit }));
			}
		}
	}
	
	/**
	 *  Method to check if exception is already added
	 *  
	 * @param errors
	 * @param errorCode
	 * @return
	 */
	private boolean isExcpetionAdded(Collection<GenericException> errors, int errorCode) {
		for(GenericException exception : errors) {
			if(exception.getErrorCode() == errorCode) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * CSF 354 [c] If ‘Period of Service’ has been specified as part of the eligibility 
	 * requirements for the money type, it should be less than or equal to the eligibility 
	 * calculation upper limit set for the money type (based on its source - EE/ER and the selected basis).  .  
	 * @param moneyTypeEligibility
	 * @param moneyType
	 * @param errors
	 * @param businessParameterObject
	 */
	private void validatePeriodOfService(MoneyTypeEligibilityCriterion moneyTypeEligibility,String moneyType,
			Collection<GenericException> errors,
			BusinessParameterValueObject businessParameterObject){
		int errorMessage = 0;
		int eeMaximumDaysLimit = businessParameterObject
		.getEcEEMoneyTypePeriodOfServiceDaysLimit();
		int erMaximumDaysLimit = businessParameterObject
		.getEcERMoneyTypePeriodOfServiceDaysLimit();
		int eeMaximumMonthsLimit = businessParameterObject
		.getEcEEMoneyTypePeriodOfServiceMonthsLimit();
		int erMaximumMonthsLimit = businessParameterObject
		.getEcERMoneyTypePeriodOfServiceMonthsLimit();
		if (moneyTypeEligibility.getPeriodOfServiceUnit() != null
				&&!( moneyTypeEligibility.getPeriodOfServiceUnit().
						equals(Constants.PERIOD_OF_SERVICE_UNIT_DAYS)|| 
						moneyTypeEligibility.getPeriodOfServiceUnit().equals(Constants.PERIOD_OF_SERVICE_UNIT_MONTHS))) {
			errorMessage = ErrorCodes.PERIOD_OF_SERVICE_SHOULD_BE_MONTHS_OR_DAYS;
			errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,
					errorMessage,new Object[]{moneyTypeEligibility.getContractMoneyTypeShortName()}));
		}else if (moneyTypeEligibility.getMoneyTypeCategoryCode() != null
				&& moneyTypeEligibility.getMoneyTypeCategoryCode().equals(Constants.MONEY_TYPE_CATEGORY_EE)) {
			if (moneyTypeEligibility.getPeriodOfServiceUnit() != null
					&& moneyTypeEligibility.getPeriodOfServiceUnit().equals(Constants.PERIOD_OF_SERVICE_UNIT_DAYS)) {
				if (!(moneyTypeEligibility.getPeriodOfService().intValue() <= eeMaximumDaysLimit)) {
					errorMessage = ErrorCodes.PERIOD_OF_SERVICE_SHOULD_BE_WITHIN_UPPER_LIMIT;
					errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,
							errorMessage,new Object[]{moneyTypeEligibility.getContractMoneyTypeShortName(),eeMaximumDaysLimit}));
				}
			} else if (moneyTypeEligibility
					.getPeriodOfServiceUnit() != null
					&& moneyTypeEligibility.getPeriodOfServiceUnit().equals(Constants.PERIOD_OF_SERVICE_UNIT_MONTHS)) {
				if (!(moneyTypeEligibility.getPeriodOfService()
						.intValue() <= eeMaximumMonthsLimit)) {
					errorMessage = ErrorCodes.PERIOD_OF_SERVICE_SHOULD_BE_WITHIN_UPPER_LIMIT;
					errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,
							errorMessage,new Object[]{moneyTypeEligibility.getContractMoneyTypeShortName(),eeMaximumMonthsLimit}));
				}
			}
		} else if (moneyTypeEligibility
				.getMoneyTypeCategoryCode() != null
				&& moneyTypeEligibility.getMoneyTypeCategoryCode().equals(Constants.MONEY_TYPE_CATEGORY_ER)) {
			if (moneyTypeEligibility.getPeriodOfServiceUnit() != null
					&& moneyTypeEligibility.getPeriodOfServiceUnit().equals(Constants.PERIOD_OF_SERVICE_UNIT_DAYS)) {
				if (!(moneyTypeEligibility.getPeriodOfService()
						.intValue() <= erMaximumDaysLimit)) {
					errorMessage = ErrorCodes.PERIOD_OF_SERVICE_SHOULD_BE_WITHIN_UPPER_LIMIT;
					errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,
							errorMessage,new Object[]{moneyTypeEligibility.getContractMoneyTypeShortName(),erMaximumDaysLimit}));
				}
			} else if (moneyTypeEligibility
					.getPeriodOfServiceUnit() != null
					&& moneyTypeEligibility	.getPeriodOfServiceUnit().equals(Constants.PERIOD_OF_SERVICE_UNIT_MONTHS)) {
				if (!(moneyTypeEligibility.getPeriodOfService()
						.intValue() <= erMaximumMonthsLimit)) {
					errorMessage = ErrorCodes.PERIOD_OF_SERVICE_SHOULD_BE_WITHIN_UPPER_LIMIT;
					errors.add(new ValidationError(CsfConstants.FIELD_ELIGIBILITY_CALCULATION_IND,
							errorMessage,new Object[]{moneyTypeEligibility.getContractMoneyTypeShortName(),erMaximumMonthsLimit}));
				}
			}
		}
	}
	
	/**
	 * checks if contract has  money type
	 * 
	 * @param csfForm
	 * @param moneyTypeId
	 * 
	 * @return boolean
	 */
	private boolean isMoneyTypePresent(CsfForm csfForm, String moneyTypeId) {
		boolean result = false;
		for(MoneyTypeVO moneyType : csfForm.getContractMoneyTypesList()) {
			if(moneyTypeId.equals(moneyType.getId())){
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**
	 * checks if contract has  money type
	 * 
	 * @param csfForm
	 * @param moneyTypeId
	 * 
	 * @return boolean
	 */
	private boolean isMoneyTypesPresent(CsfForm csfForm, String moneyTypeId1, String moneyTypeId2) {
		boolean result = false;
		for(MoneyTypeVO moneyType : csfForm.getContractMoneyTypesList()) {
			if(moneyTypeId1.equals(moneyType.getId()) || moneyTypeId2.equals(moneyType.getId())){
				result = true;
				break;
			}
		}
		return result;
	}
	
	
	/**
	 * To validate whether plan data information are available for particular notice type selection
	 * @param csfForm
	 * @param warnings
	 * @param errors
	 * @param userProfile
	 * @throws SystemException
	 */
	private void validateSendService(CsfForm csfForm, Collection<GenericException> warnings, Collection<GenericException> errors, UserProfile userProfile) throws SystemException{
	    if (logger.isDebugEnabled()){
            logger.debug(new StringBuffer("NoticePlanDAO.getPlanDataDetailsForCSF > Entry").toString());
        }
	    //to get the plan data completion ind
        HashMap<String, String> planDataMap =  ContractServiceDelegate.getInstance().getPlanDataDetailsForCSF(userProfile.getCurrentContract().getContractNumber());
	    
        //To get the PIF fields in Plan data page
        NoticePlanCommonVO noticePlanCommonVO =  ContractServiceDelegate.getInstance().readNoticePlanCommonData(userProfile.getCurrentContract().getContractNumber());        
        
        boolean sendServiceTurnOffInd = false;
        
        logger.debug("***validateNoticeGenerationService***");
        logger.debug("404a5:"+validate404a5(userProfile));
        logger.debug("DIOPIF:"+validateDIOPIF(noticePlanCommonVO));
        logger.debug("Contact:"+validateContactInfoPIF(noticePlanCommonVO));
        logger.debug("QDIA:"+validateQDIA(planDataMap));
        logger.debug("Auto:"+validateAuto(planDataMap, noticePlanCommonVO, userProfile.getCurrentContract().getContractNumber()));
        logger.debug("C&D:"+validateContributionAndDistribution(planDataMap, noticePlanCommonVO));
        logger.debug("SH:"+validateSafeHarbor(planDataMap, noticePlanCommonVO, userProfile.getCurrentContract().getContractNumber()));
        
        //To get the notice service selection end date
        Date serviceSelectionEndDate = ContractServiceDelegate.getInstance().getNoticeServiceEndDate(userProfile.getCurrentContract().getContractNumber());
        if(serviceSelectionEndDate == null){
        	sendServiceTurnOffInd = true;
        }
        else{
        	Calendar cal = Calendar.getInstance();
        	cal.setTime(serviceSelectionEndDate);
        	if(cal.get(Calendar.YEAR)!=9999){
        		sendServiceTurnOffInd = true;
        	}
        }
        String oldSendServiceValue = ((CsfForm)csfForm.getClonedForm()).getNoticeServiceInd();
		String newSendServiceValue = csfForm.getNoticeServiceInd();
		 String oldNoticeTypeValue = ((CsfForm)csfForm.getClonedForm()).getNoticeTypeSelected();
			String newNoticeTypeValue = csfForm.getNoticeTypeSelected();
		if(!StringUtils.equals(newSendServiceValue, oldSendServiceValue) || !StringUtils.equals(newNoticeTypeValue, oldNoticeTypeValue)  ){
				if( StringUtils.equals(CsfConstants.CSF_NO, newSendServiceValue) && !StringUtils.equals(newNoticeTypeValue, oldNoticeTypeValue)){
		            if(!sendServiceTurnOffInd){
		                errors.add(new ValidationError("noticeServiceInd", ErrorCodes.ERROR_SEND_SERVICE_DESELECTION));
		            }
		            else{
		            	csfForm.setNoticeTypeSelected(StringUtils.EMPTY);
		            }
		        }
				else if( StringUtils.equals(CsfConstants.CSF_NO, newSendServiceValue)){
		            if(!sendServiceTurnOffInd){
		                errors.add(new ValidationError("noticeServiceInd", ErrorCodes.ERROR_SEND_SERVICE_DESELECTION));
		            }
		        }
				else if(StringUtils.equals(CsfConstants.CSF_YES, newSendServiceValue) || !StringUtils.equals(newNoticeTypeValue, oldNoticeTypeValue)){
                    //CSF.503
                	
                	if(CsfUtil.isNullOrEmpty(csfForm.getNoticeTypeSelected())){
                        errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_NOTICE_TYPE_NOT_SELECTED));
                    }	else if(validate404a5(userProfile)){
                        errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_404A5_NOTICE_NOT_AVAILABLE)); 
                    } else if(CsfConstants.NOTICE_OPT_404A5.equals(csfForm.getNoticeTypeSelected())){
		                 //CSF.499 
		               	if(!validateQDIA(planDataMap)){
		               		warnings.add(new ValidationError("noticeTypeSelected", ErrorCodes.WARNING_QDIA_NOT_SELECTED,ValidationError.Type.warning));
		                }
	                    if(validateSummaryPIF(noticePlanCommonVO)){
	                        errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_SUMMARY_DATA_MISSING));
	                    }
	                    if(validateContactInfoPIF(noticePlanCommonVO)){
	                    	errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_CONTACT_INFO_MISSING));
	                    }
	                    if(validateDIOPIF(noticePlanCommonVO)){
	                    	errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_DIO_MORE_THAN_ONE));
	                    }                      
                    }
                    else if(CsfConstants.NOTICE_OPT_QDIA.equals(csfForm.getNoticeTypeSelected())){
                        if(validateSummaryPIF(noticePlanCommonVO)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_SUMMARY_DATA_MISSING));
                        }
                        if(validateContactInfoPIF(noticePlanCommonVO)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_CONTACT_INFO_MISSING));
                        }
                        if(validateQDIA(planDataMap)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_INV_INFO_DATA_INCOMPLETE));
                        }
                        if(validateDIOPIF(noticePlanCommonVO)){
                        	errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_DIO_MORE_THAN_ONE));
                        }
                    }
                    else if(CsfConstants.NOTICE_OPT_AUTO.equals(csfForm.getNoticeTypeSelected())){
                    	//CSF.499
                    	
                   	 if(!validateQDIA(planDataMap)){
                   		warnings.add(new ValidationError("noticeTypeSelected", ErrorCodes.WARNING_QDIA_NOT_SELECTED,ValidationError.Type.warning));
                         }
	                   	 if( CsfConstants.CSF_N.equalsIgnoreCase(planDataMap.get(CsfConstants.INVESTMENT_INFO_IND))){
	                  	   errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_INV_INFO_TAB_INCOMPLETE));
	                     }
                      
                        if(validateSummaryPIF(noticePlanCommonVO)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_SUMMARY_DATA_MISSING));
                        }
                        if(validateContactInfoPIF(noticePlanCommonVO)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_CONTACT_INFO_MISSING));
                        }
                        if(validateDIOPIF(noticePlanCommonVO)){
                        	errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_DIO_MORE_THAN_ONE));
                        }
                        if(CsfConstants.AUTO_ENROLL_TYPE_EACA.equalsIgnoreCase(planDataMap.get(CsfConstants.AUTO_ENROLL_TYPE))
                        		|| CsfConstants.AUTO_ENROLL_TYPE_QACA.equalsIgnoreCase(planDataMap.get(CsfConstants.AUTO_ENROLL_TYPE))){
                        	if(validateContributionAndDistribution(planDataMap, noticePlanCommonVO)){
                                errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_CONTRIB_DISTRI_DATA_INCOMPLETE));
                            }
                        }else if(validatePlanEntryFrequencySendService(noticePlanCommonVO)){
                       	 errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_CONTRIB_DISTRI_DATA_INCOMPLETE));
                       }
                      
                        if(validateAuto(planDataMap, noticePlanCommonVO, userProfile.getCurrentContract().getContractNumber())){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_AUTO_CONTRIB_DATA_INCOMPLETE));
                        }
                    }
                    else if(CsfConstants.NOTICE_OPT_AUTO_QDIA.equals(csfForm.getNoticeTypeSelected())){
                       
                        if(validateSummaryPIF(noticePlanCommonVO)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_SUMMARY_DATA_MISSING));
                        }
                        if(validateContactInfoPIF(noticePlanCommonVO)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_CONTACT_INFO_MISSING));
                        }
                        if(validateQDIA(planDataMap)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_INV_INFO_DATA_INCOMPLETE));
                        }
                        if(validateDIOPIF(noticePlanCommonVO)){
                        	errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_DIO_MORE_THAN_ONE));
                        }
                        if(CsfConstants.AUTO_ENROLL_TYPE_EACA.equalsIgnoreCase(planDataMap.get(CsfConstants.AUTO_ENROLL_TYPE))
                        		|| CsfConstants.AUTO_ENROLL_TYPE_QACA.equalsIgnoreCase(planDataMap.get(CsfConstants.AUTO_ENROLL_TYPE))){
                        	if(validateContributionAndDistribution(planDataMap, noticePlanCommonVO)){
                                errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_CONTRIB_DISTRI_DATA_INCOMPLETE));
                            }
                        } else if(validatePlanEntryFrequencySendService(noticePlanCommonVO)){
                          	 errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_CONTRIB_DISTRI_DATA_INCOMPLETE));
                          }                      
                        if(validateAuto(planDataMap, noticePlanCommonVO, userProfile.getCurrentContract().getContractNumber())){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_AUTO_CONTRIB_DATA_INCOMPLETE));
                        }
                    }
                    else if(CsfConstants.NOTICE_OPT_SH.equals(csfForm.getNoticeTypeSelected())){
                    	//CSF.499
                    	 if(!validateQDIA(planDataMap)){
                    		 warnings.add(new ValidationError("noticeTypeSelected", ErrorCodes.WARNING_QDIA_NOT_SELECTED,ValidationError.Type.warning));
                          }
                    	 if( CsfConstants.CSF_N.equalsIgnoreCase(planDataMap.get(CsfConstants.INVESTMENT_INFO_IND))){
                      	   errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_INV_INFO_TAB_INCOMPLETE));
                         }
                       
                        if(validateSummaryPIF(noticePlanCommonVO)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_SUMMARY_DATA_MISSING));
                        }
                        if(validateContactInfoPIF(noticePlanCommonVO)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_CONTACT_INFO_MISSING));
                        }
                        if(validateDIOPIF(noticePlanCommonVO)){
                        	errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_DIO_MORE_THAN_ONE));
                        }
                        if(validateContributionAndDistribution(planDataMap, noticePlanCommonVO)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_CONTRIB_DISTRI_DATA_INCOMPLETE));
                        }
                        if(validateSafeHarbor(planDataMap, noticePlanCommonVO, userProfile.getCurrentContract().getContractNumber())){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_SH_DATA_INCOMPLETE));
                        }
                    }
                    else if(CsfConstants.NOTICE_OPT_SH_QDIA.equals(csfForm.getNoticeTypeSelected())){
                      
                        if(validateSummaryPIF(noticePlanCommonVO)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_SUMMARY_DATA_MISSING));
                        }
                        if(validateContactInfoPIF(noticePlanCommonVO)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_CONTACT_INFO_MISSING));
                        }
                        if(validateQDIA(planDataMap)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_INV_INFO_DATA_INCOMPLETE));
                        }
                        if(validateDIOPIF(noticePlanCommonVO)){
                        	errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_DIO_MORE_THAN_ONE));
                        }
                        if(validateContributionAndDistribution(planDataMap, noticePlanCommonVO)){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_CONTRIB_DISTRI_DATA_INCOMPLETE));
                        }
                        if(validateSafeHarbor(planDataMap, noticePlanCommonVO, userProfile.getCurrentContract().getContractNumber())){
                            errors.add(new ValidationError("noticeTypeSelected", ErrorCodes.ERROR_SH_DATA_INCOMPLETE));
                        }
                    }else if(isValueChanged(csfForm, "noticeTypeSelected")){
                    	warnings.add(new ValidationError("noticeTypeSelected",  ErrorCodes.WARNING_NOTICE_TYPE_CHANGE,ValidationError.Type.warning));
                    }
                }
		
        }
        if (logger.isDebugEnabled()){
            logger.debug(new StringBuffer("NoticePlanDAO.getPlanDataDetailsForCSF > Exit").toString());
        }
	}
	
	private void validatePlanHighLights(CsfForm csfForm,
			PlanDataLite planDataLite,
			WithdrawalDistributionMethod withdrawalDistributionMethod,
			Collection<GenericException> errors) {
	   
		String isPlanHighlightsCreatedByJH = csfForm.getSummaryPlanHighlightAvailable();

		if (StringUtils.equals(CsfConstants.CSF_YES, isPlanHighlightsCreatedByJH)) {
			
		   
		   //CSF 425
		   if (StringUtils.equals(CsfConstants.UNSPECIFIED, planDataLite.getMultipleEligibilityRulesForOneSingleMoneyType())) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_ELIGIBILITY_MONEY_TYPE_RULE));
		   }
		   
		   //CSF 426
		   if (StringUtils.equals(CsfConstants.CSF_N, planDataLite.getMultipleEligibilityRulesForOneSingleMoneyType())
				   && !isPlanEligibilityRequirementProvidedForAllMoneyType(csfForm)) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_ELIGIBILITY_REQ_NOT_SPECIFIED_RULE));
		   }
		   
		   //CSF 427
		   if (StringUtils.equals(CsfConstants.CSF_N, planDataLite.getMultipleEligibilityRulesForOneSingleMoneyType())
				   && StringUtils.equals(CsfConstants.UNSPECIFIED, planDataLite.getRolloversDelayedUntilEligibilityReqtMet())
				   && isPlanEligibilityRequirementProvidedForAtLeastOneMoneyType(csfForm)) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_MUST_ROLLOVERS_BE_DELAYED_RULE));
		   }
		   
		   //CSF 428
		   if (StringUtils.equals(CsfConstants.UNSPECIFIED, planDataLite.getAutomaticEnrollmentAllowed())) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_AE_RULE));
		   }
		   
		   //CSF 429
		   if ((planDataLite.getPlanEmployeeDeferralElection() == null
				   || StringUtils.isEmpty(planDataLite.getPlanEmployeeDeferralElection().getEmployeeDeferralElectionCode())
				   || StringUtils.equals(CsfConstants.UNSPECIFIED, planDataLite.getPlanEmployeeDeferralElection().getEmployeeDeferralElectionCode()))
				   && isMoneyTypePresent(csfForm, ServiceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL)) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_EMPLOYEE_DEFERRAL_ELECTION_RULE));
		   }
		   
		   //CSF 430
		   if (StringUtils.equals(CsfConstants.UNSPECIFIED, planDataLite.getCatchUpContributionsAllowed())
				   && isMoneyTypesPresent(csfForm, ServiceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL, CsfConstants.MONEY_TYPE_EEROT)) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_CATCHUP_CONTRIBUTION_RULE));
		   }
		   
		   //CSF 431
		   if (planDataLite.getDeferralMaxPercent() == null 
				   && isMoneyTypePresent(csfForm, ServiceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL)) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_DEFFERAL_MAX_PCT_RULE));
		   }
		  
		   //CSF 432 Requirement Removed
		   
		   //CSF 433
		   if (StringUtils.equals(CsfConstants.UNSPECIFIED, planDataLite.getAciAllowed())) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_ACI_RULE));
		   }
		   
		   //CSF 434
		   if (StringUtils.equals(CsfConstants.VESTING_COMPUTATION_PERIOD_UNSPECIFIED, 
  				 planDataLite.getMultipleVestingSchedulesForOneSingleMoneyType())) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_VESTING_SCHEDULE_RULE));
		   }
		   
		   //CSF 435
		   if (StringUtils.equals(CsfConstants.CSF_N, 
  				 planDataLite.getMultipleVestingSchedulesForOneSingleMoneyType()) &&
  				 isMoneyTypeThereWithoutVestingSchedule(planDataLite.getVestingSchedules())) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_NO_VESTING_SCHEDULE_RULE));
		   }
		   
		   //CSF 436
		   if (StringUtils.equals(CsfConstants.UNSPECIFIED, planDataLite.getRequiresSpousalConsentForDistributions())) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_SPOUSAL_CONSENT_RULE));
		   }
		   
		   //CSF 437
		   if (isFormOfDistributionEmpty(csfForm, withdrawalDistributionMethod)) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_FORMS_OF_DISTRB_RULE));
		   }
		   
		   //CSF 438
		   if (StringUtils.equals(CsfConstants.UNSPECIFIED, planDataLite.getLoansAllowedInd())) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_LOAN_ALLOWED_UNSPECIFIED_RULE));
		   }
		   
		   //CSF 439
		   if (StringUtils.equals(CsfConstants.CSF_Y, planDataLite.getLoansAllowedInd()) && planDataLite.getMinimumLoanAmount() == null) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_MIN_LOAN_AMT_RULE));
		   }
		   
		   //CSF 440
		   if (StringUtils.equals(CsfConstants.CSF_Y, planDataLite.getLoansAllowedInd()) && planDataLite.getMaximumLoanAmount() == null) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_MAX_LOAN_AMT_RULE));
		   }
		   
		   //CSF 441
		   if (StringUtils.equals(CsfConstants.CSF_Y, planDataLite.getLoansAllowedInd()) && planDataLite.getMaximumLoanPct() == null) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_MAX_LOAN_PCT_RULE));
		   }
		   
		   //CSF 442
		   if (StringUtils.equals(CsfConstants.CSF_Y, planDataLite.getLoansAllowedInd()) 
				   && ( planDataLite.getMaximumNumberofOutstandingLoans() == null
						   || planDataLite.getMaximumNumberofOutstandingLoans() == 0)) {
				errors.add(new ValidationError(
								CsfConstants.FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE,
								ErrorCodes.ERROR_PLAN_HIGHLIGHTS_NUM_OUTSTANDING_LOANS_RULE));
		   }
	   }
	}

    /**
     * checks if plan eligibility requirement is provided for at least one money types
     * 
     * @param csfForm
     * @return boolean
     */
	private boolean isPlanEligibilityRequirementProvidedForAtLeastOneMoneyType(
			CsfForm csfForm) {
		List<MoneyTypeEligibilityCriterion> moneyTypeEligibilityCriteria = csfForm.getMoneyTypeEligibilityCriteria();
		for (MoneyTypeEligibilityCriterion moneyTypeEligibility : moneyTypeEligibilityCriteria) {
			if ((moneyTypeEligibility.getImmediateEligibilityIndicator() != null 
					&& moneyTypeEligibility.getImmediateEligibilityIndicator())
					|| (moneyTypeEligibility.getMinimumAge() != null && moneyTypeEligibility.getMinimumAge().intValue() != 0)
					|| (moneyTypeEligibility.getServiceCreditingMethod() != null && !moneyTypeEligibility
							.getServiceCreditingMethod().equals(Constants.SERVICE_CREDITING_METHOD_UNSPECIFIED))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * checks if form of distribution is blank
	 * 
	 * @param csfForm
	 * @param withdrawalDistributionMethod
	 * @return boolean
	 */
	private boolean isFormOfDistributionEmpty(CsfForm csfForm, WithdrawalDistributionMethod withdrawalDistributionMethod) {
		return !(withdrawalDistributionMethod.getAnnuityIndicator() || withdrawalDistributionMethod.getInstallmentsIndicator()
		       || withdrawalDistributionMethod.getLumpSumIndicator() || withdrawalDistributionMethod.getPartialWithdrawalIndicator()
		       || withdrawalDistributionMethod.getOtherIndicator());
	}

	/**
     * checks if plan eligibility requirement is provided for all money types
     * 
     * @param csfForm
     * @return boolean
     */
	private boolean isPlanEligibilityRequirementProvidedForAllMoneyType(
			CsfForm csfForm) {
		List<MoneyTypeEligibilityCriterion> moneyTypeEligibilityCriteria = csfForm.getMoneyTypeEligibilityCriteria();
		for (MoneyTypeEligibilityCriterion moneyTypeEligibility : moneyTypeEligibilityCriteria) {
			if ((moneyTypeEligibility.getImmediateEligibilityIndicator() == null 
					|| !moneyTypeEligibility.getImmediateEligibilityIndicator())
					&& (moneyTypeEligibility.getMinimumAge() == null || moneyTypeEligibility.getMinimumAge().intValue() == 0)
					&& (moneyTypeEligibility.getServiceCreditingMethod() == null || moneyTypeEligibility
							.getServiceCreditingMethod().equals(Constants.SERVICE_CREDITING_METHOD_UNSPECIFIED))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * checks if any money type is there without vesting schedule
	 * 
	 * @param vestingSchedules
	 * @return boolean
	 */
	private boolean isMoneyTypeThereWithoutVestingSchedule(Collection<VestingSchedule> vestingSchedules) {
		for(VestingSchedule vestingSchedule : vestingSchedules){
			if(StringUtils.isEmpty(vestingSchedule.getVestingScheduleType())){
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to verify whether value is changed or not.
	 * @param csfForm
	 * @param fieldName
	 * @return
	 */
	private boolean isValueChanged(CsfForm csfForm, String fieldName){
		boolean isValueChanged = false;
		
		//reflection to create the method name based on the fieldName
		Method getFieldValueMethod;
		try {
			getFieldValueMethod = CsfForm.class.getDeclaredMethod("get"
					+ StringUtils.capitalize(fieldName), new Class[] {});
		
			// get the old value for the field specified using reflection
			String oldValue = (String) getFieldValueMethod.invoke(csfForm.getClonedForm(),
					new Object[] {});
			
			// get the new value for the field specified using reflection			
			String newValue = (String) getFieldValueMethod.invoke(csfForm, new Object[] {});
		
			if (!StringUtils.equals(newValue, oldValue)) {
				isValueChanged = true;
			}
		} catch (Exception e) {
			logger.error(e);
		} 
		return isValueChanged;
	}
	
	/**
	 * Dont allow CSF changeDeferralOnline to be turned on if 
	 * default inc pct or deferral max pct has decimal values
	 * 
	 * @param csfForm
	 * @param planDataLite
	 * @param errors
	 */
	private void validateDeferralOnlineChangeCsf(CsfForm csfForm,
			PlanDataLite planDataLite, Collection<GenericException> errors) {
		BigDecimal aciDefaultIncreasePercent = planDataLite.getAciDefaultIncreasePercent();
		BigDecimal aciDefaultAutoIncreaseMaxPercent = planDataLite.getAciDefaultAutoIncreaseMaxPercent();
		if (aciDefaultIncreasePercent != null) {
			try {
				aciDefaultIncreasePercent.intValueExact();
			} catch (ArithmeticException e) {
				errors.add(new ValidationError(CsfConstants.FIELD_CHANGE_DEFERRALS_ONLINE,
						ErrorCodes.DEFERRAL_ANNUAL_INCREASE_HAS_DECIMAL_PLACE));
			}
		}
		if (aciDefaultAutoIncreaseMaxPercent != null) {
			try {
				aciDefaultAutoIncreaseMaxPercent.intValueExact();
			} catch (ArithmeticException e) {
				errors.add(new ValidationError(CsfConstants.FIELD_CHANGE_DEFERRALS_ONLINE,
						ErrorCodes.DEFAULT_MAX_PCT_HAS_DECIMAL_PLACE));
			}
		}
	}
	
	
	//CSF.494
	public boolean validate404a5(UserProfile userProfile) throws SystemException{	    
    	Access404a5 contractAccess = userProfile.getAccess404a5();
    	Qualification piNoticeQual = contractAccess.getAccess(Facility._404A5_PLAN_AND_INVESTMENT_NOTICE);
    	if(contractAccess.getAccessibleFacilities().isEmpty()  
    			|| (piNoticeQual != null && piNoticeQual.getTemporarilyMissingInformation().contains(MissingInformation.ICC_CONTACT))){
            return true;
        }	    
	    return false;	    
	}
	
	
	//CSF.495
	private boolean validateDIOPIF(NoticePlanCommonVO noticePlanCommonVO){
	    if(noticePlanCommonVO.getDefaultInvestments()!=null){
	        if(noticePlanCommonVO.getDefaultInvestments().size()>1){
	            return true;
	        }
	    }
	    return false;
	}
	
	
	//CSF.496
	private boolean validateContactInfoPIF(NoticePlanCommonVO noticePlanCommonVO){
	    if(CsfUtil.isNullOrEmpty(noticePlanCommonVO.getPrimaryContactName()) || CsfUtil.isNullOrEmpty(noticePlanCommonVO.getPlanSponsorMailingAddress())
	            || CsfUtil.isNullOrEmpty(noticePlanCommonVO.getPlanSponsorCitySateZip()) || CsfUtil.isNullOrEmpty(noticePlanCommonVO.getPlanSponsorEmailAddress())){
	        return true;
	    }
	    return false;
	}
	
	
	//CSF.497
	private boolean validateQDIA(HashMap<String, String> planDataMap){
	    if(!(CsfConstants.CSF_Y.equalsIgnoreCase(planDataMap.get(CsfConstants.DIO)) &&
	            CsfConstants.CSF_Y.equalsIgnoreCase(planDataMap.get(CsfConstants.INVESTMENT_INFO_IND)))){
	        return true;
	    }
	    return false;
	}
	
	
	//CSF.500
	private boolean validateAuto(HashMap<String, String> planDataMap, NoticePlanCommonVO noticePlanCommonVO, Integer contractId){
		if(!(CsfConstants.CSF_Y.equalsIgnoreCase(planDataMap.get(CsfConstants.AUTOMATIC_CONTRIBUTION_IND))
				&& noticePlanCommonVO.getAutomaticContributionEffectiveDate()!=null && noticePlanCommonVO.getDefaultDeferralPercentage()!=null 
				&&(CsfConstants.AUTO_ENROLL_TYPE_ACA.equalsIgnoreCase(planDataMap.get(CsfConstants.AUTO_ENROLL_TYPE))
				|| (CsfConstants.AUTO_ENROLL_TYPE_EACA.equalsIgnoreCase(planDataMap.get(CsfConstants.AUTO_ENROLL_TYPE)) 
								&&( CsfConstants.CSF_N.equalsIgnoreCase(planDataMap.get(CsfConstants.EACA_EMPLOYER_CONTRIB_IND)) 
										||( CsfConstants.CSF_Y.equalsIgnoreCase(planDataMap.get(CsfConstants.EACA_EMPLOYER_CONTRIB_IND)) &&   findEachTypeAvaiableMoneyTypeVesting(planDataMap.get(CsfConstants.AUTO_ENROLL_TYPE),noticePlanCommonVO, contractId)))
				|| (CsfConstants.AUTO_ENROLL_TYPE_QACA.equalsIgnoreCase(planDataMap.get(CsfConstants.AUTO_ENROLL_TYPE)) 
						&& (CsfConstants.CSF_N.equalsIgnoreCase(planDataMap.get(CsfConstants.QACA_ADDITIONAL_EMPLOYER_CONTRIB_IND))
								||( CsfConstants.CSF_Y.equalsIgnoreCase(planDataMap.get(CsfConstants.QACA_ADDITIONAL_EMPLOYER_CONTRIB_IND)) && findEachTypeAvaiableMoneyTypeVesting(planDataMap.get(CsfConstants.AUTO_ENROLL_TYPE),noticePlanCommonVO, contractId)))))))){
	        return true;
	    }
	    return false;
	}
	
	
	//CSF.501
	private boolean validateContributionAndDistribution(HashMap<String, String> planDataMap, NoticePlanCommonVO noticePlanCommonVO){
		//Section A
		if( !( CsfConstants.CSF_Y.equalsIgnoreCase(planDataMap.get(CsfConstants.CONTRIBUTION_AND_DISTRIBUTION_IND))
				 && !CsfUtil.isNullOrEmpty(noticePlanCommonVO.getCatchUpContributionsAllowed()) 
				 && !CsfUtil.isNullOrEmpty(noticePlanCommonVO.getPlanAllowRolloverContribution())
				 && !( CsfUtil.isNullOrEmpty(noticePlanCommonVO.getPlanAllowLoans()) 
						 || ( !CsfUtil.isNullOrEmpty(noticePlanCommonVO.getPlanAllowLoans())
						 		&& ( (noticePlanCommonVO.getLoanPercentAllowed()==null || noticePlanCommonVO.getLoanAmountAllowed()==null)
								        && Constants.YES.equalsIgnoreCase(noticePlanCommonVO.getPlanAllowLoans()))))
				&& (!CsfUtil.isNullOrEmpty(noticePlanCommonVO.getPlanAllowsHardshipWithdrawal()))) ||
				!(!CsfUtil.isNull(noticePlanCommonVO.getDeferralMaxPercent()) 
						|| !CsfUtil.isNull(noticePlanCommonVO.getDeferralMaxAmount()) 
								|| (!CsfUtil.isNull(noticePlanCommonVO.getDeferralIrsApplies()) && (Constants.TRUE).equals(noticePlanCommonVO.getDeferralIrsApplies().toString()) ))){
			return true;
		}
		else if(  validatePlanEntryFrequencySendService(noticePlanCommonVO)){
	    	 return true;
	    }
		return false;
	}


	/**
	 * CSF 498
	 * @param noticePlanCommonVO
	 */
	private boolean validatePlanEntryFrequencySendService(
			NoticePlanCommonVO noticePlanCommonVO) {
		if(((PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getContributionMoneyType()))
	    		&&(PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getMoneyTypeFreequency())
	    				||(!PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getMoneyTypeFreequency())&& (!noticePlanCommonVO.getMoneyTypeFreequency().equalsIgnoreCase(Constants.UNSPECIFIED)))
	    				))
	    	||(((!PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getContributionMoneyType()))
	    			&&(!noticePlanCommonVO.getContributionMoneyType().equalsIgnoreCase(Constants.UNSPECIFIED)))
	    			&&(PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getMoneyTypeFreequency())))
	    			
	    	||noticePlanCommonVO.getContributionMoneyType().equalsIgnoreCase(Constants.UNSPECIFIED)
	    	||PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanEntryFreequencyDate()))
        {
           return true;
        }
		return false;
	}	
	
	
	//CSF.502
	private boolean validateSafeHarbor(HashMap<String, String> planDataMap, NoticePlanCommonVO noticePlanCommonVO, Integer contractId){
        if(!(CsfConstants.CSF_Y.equalsIgnoreCase(planDataMap.get(CsfConstants.SAFE_HARBOR_IND))
                    &&(CsfConstants.CSF_N.equalsIgnoreCase(planDataMap.get(CsfConstants.SH_ADDITIONAL_EMP_CONTRIB_IND))|| (CsfConstants.CSF_Y.equalsIgnoreCase(planDataMap.get(CsfConstants.SH_ADDITIONAL_EMP_CONTRIB_IND))
                    	&& findEachTypeAvaiableMoneyTypeVesting("SH",noticePlanCommonVO, contractId))))){
            return true;
        }
        return false;
    }
	
	//CSF.508
	private boolean validateSummaryPIF(NoticePlanCommonVO noticePlanCommonVO){
	    if(noticePlanCommonVO ==null 
	    		|| noticePlanCommonVO.getPlanYearEnd()==null || CsfUtil.isNullOrEmpty(noticePlanCommonVO.getPlanName())){
	        return true;
	    }
	    return false;
	}
	
	private boolean findEachTypeAvaiableMoneyTypeVesting(String typeSelected ,NoticePlanCommonVO noticePlanCommonVO, Integer contractId) {
		boolean vestedMoneyTypeAvaiable = false;
		//To get the vesting schedules
        Collection<VestingSchedule> vestingSchedules = noticePlanCommonVO.getVestingSchedules();
      //To get the Excluded MoneyType details
        List<NoticePlanVestingMTExcludeVO> excludedMTList = new ArrayList<NoticePlanVestingMTExcludeVO>();
        try {
			excludedMTList = ContractServiceDelegate.getInstance().getExcludeDetailsForVestingSchedules(contractId, typeSelected);
		} catch (SystemException e) {
			logger.error("Unable to get the excluded money list for the contract :"+contractId,e);
			return false;
		}
        int  moneytypeCount= 0;
		for (VestingSchedule vestingSchedule : vestingSchedules) {
			if(StringUtils.isNotBlank(vestingSchedule.getVestingScheduleType())){
				for (NoticePlanVestingMTExcludeVO nPlanVestingList : excludedMTList) {
					if (nPlanVestingList.getMoneyTypeId().equals(vestingSchedule.getMoneyTypeId())) {
						continue;
					}
						
				}
				
				moneytypeCount++;
			}
		}
		if(moneytypeCount>0){	
			vestedMoneyTypeAvaiable = true;
		}
                
        return vestedMoneyTypeAvaiable;
	}	
}

