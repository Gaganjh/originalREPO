package com.manulife.pension.ps.web.contract.csf.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.contract.csf.CsfBaseController;
import com.manulife.pension.ps.web.contract.csf.CsfConstants;
import com.manulife.pension.ps.web.contract.csf.CsfDataHelper;
import com.manulife.pension.ps.web.contract.csf.EligibilityCalculationMoneyType;
import com.manulife.pension.ps.web.contract.csf.PlanSponsorServicesData;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.contract.valueobject.InvestmentPolicyStatementVO;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.plan.valueobject.MoneyTypeEligibilityCriterion;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;

/**
 * Class to load the the CSF view page PlanSponsor Section. 
 * 
 * This Class will determine the CMA keys for each filed in the 
 * PlanSponsor section and set in to PlanSponsorServicesData Value Object. 
 * This internally used to populate the CSF View page.
 * 
 * @author Puttaiah Arugunta
 *
 */
public class PlanSponsorServicesSectionUtil {
	
	/**
	 * Method to determine Plan Support Services: Plan Highlights 
	 * created by John Hancock
	 * 
	 * @param ContractServiceFeatures
	 * @param planDataLite
	 * @param data
	 */
	public static void determinePlanHighlights(
			Map ContractServiceFeatures, 
			PlanDataLite planDataLite, 
			PlanSponsorServicesData data){
		
		int phContentCode = 0;
		ContractServiceFeature summaryPlanHighlightAvailable = 
			(ContractServiceFeature) ContractServiceFeatures.get(
					ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_AVAILABLE);
		
		if(planDataLite != null ){
			
			if(PlanData.PLAN_TYPE_457B.equals(
					planDataLite.getPlanTypeCode())) {
				
				phContentCode = ContentConstants.PLAN_HIGHLIGHTS_CREATED_BY_JH_457_PLAN;
				
			} else if(StringUtils.isEmpty(planDataLite.getPlanLegalName())) {
				
				phContentCode = ContentConstants.PLAN_HIGHLIGHTS_CREATED_BY_JH_NO_PLAN_NAME;
				
			} else if (summaryPlanHighlightAvailable != null) {
				boolean value = ContractServiceFeature.internalToBoolean(
						summaryPlanHighlightAvailable.getValue()).booleanValue();
				
				phContentCode = value ? ContentConstants.PLAN_HIGHLIGHTS_CREATED_BY_JH_YES
						: ContentConstants.PLAN_HIGHLIGHTS_CREATED_BY_JH_NO;
			}
		}
		data.setSummaryPlanHighlightAvailable(phContentCode);
		

		phContentCode = 0;
		if (summaryPlanHighlightAvailable != null) {
			String summaryPlanHighlightReviewed = summaryPlanHighlightAvailable
					.getAttributeValue(ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_REVIEWDED);

			boolean summaryPlanHighlightAvailableValue = ContractServiceFeature
					.internalToBoolean(summaryPlanHighlightAvailable.getValue())
					.booleanValue();

			boolean summaryPlanHighlightReviewedValue = summaryPlanHighlightReviewed == null ? false
					:  ContractServiceFeature.internalToBoolean(summaryPlanHighlightReviewed).booleanValue();

			phContentCode = summaryPlanHighlightAvailableValue ? (summaryPlanHighlightReviewedValue ? ContentConstants.PLAN_HIGHLIGHTS_REVIEWED_YES
					: ContentConstants.PLAN_HIGHLIGHTS_REVIEWED_NO)
					: 0;
		}
		data.setSummaryPlanHighlightReviewed(phContentCode);
	}
	
	
	
	/**
	 * 
	 */
	public static void determineNoticeGeneration(Map ContractServiceFeatures, PlanDataLite planDataLite, PlanSponsorServicesData data){
	    ContractServiceFeature noticeGenerationService = (ContractServiceFeature) ContractServiceFeatures.get(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_CD);
	    if(noticeGenerationService!=null){
	        if(noticeGenerationService.getValue()!=null){
	            boolean value = ContractServiceFeature.internalToBoolean(noticeGenerationService.getValue()).booleanValue();
	            data.setNoticeGenerationServiceInd(value);
	            if(value){
	            	data.setNoticeServiceSelectedDate(noticeGenerationService.getAttributeValue(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_SELECTION_DATE_CD));
	            }
	            String noticeOption = noticeGenerationService.getAttributeValue(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_OPTCD);
	            if(noticeOption!=null){
	                if(CsfConstants.NOTICE_OPT_404A5.equals(noticeOption.trim())){
	                    data.setNoticeOption(CsfConstants.NOTICE_OPT_404A5_DESC);
	                }
	                else if(CsfConstants.NOTICE_OPT_QDIA.equals(noticeOption.trim())){
	                    data.setNoticeOption(CsfConstants.NOTICE_OPT_QDIA_DESC);
	                }
	                else if(CsfConstants.NOTICE_OPT_AUTO.equals(noticeOption.trim())){
                        data.setNoticeOption(CsfConstants.NOTICE_OPT_AUTO_DESC);
                    }
                    else if(CsfConstants.NOTICE_OPT_AUTO_QDIA.equals(noticeOption.trim())){
                        data.setNoticeOption(CsfConstants.NOTICE_OPT_AUTO_QDIA_DESC);
                    }
                    else if(CsfConstants.NOTICE_OPT_SH.equals(noticeOption.trim())){
                        data.setNoticeOption(CsfConstants.NOTICE_OPT_SH_DESC);
                    }
                    else if(CsfConstants.NOTICE_OPT_SH_QDIA.equals(noticeOption.trim())){
                        data.setNoticeOption(CsfConstants.NOTICE_OPT_SH_QDIA_DESC);
                    }	                
	            }
	        }
	        else{
	            data.setNoticeGenerationServiceInd(false);
	        }
	    }
	}
	
	
	/**
	 * Method to determine Plan Support Services: Eligibility Calculation Services
	 * 
	 * @param ContractServiceFeatures
	 * @param data
	 * @throws SystemException 
	 */
	@SuppressWarnings("unchecked")
	public static void determineEligibilityCalculationService(
			Map ContractServiceFeatures, 
			PlanSponsorServicesData data,
			int contractId) throws SystemException {
		
		ContractServiceFeature ecCSF = 
			(ContractServiceFeature) ContractServiceFeatures.get(
					ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);
		
		if(ecCSF != null){
			
			boolean value = ContractServiceFeature.internalToBoolean(
					ecCSF.getValue()).booleanValue();
			
			data.setEligibilityCalculationService(
					value ? ContentConstants.ELIGIBILITY_CALCULATION_SERVICE_YES
					: ContentConstants.ELIGIBILITY_CALCULATION_SERVICE_NO);
			
			
			if(value){
				
				List<MoneyTypeEligibilityCriterion> moneyTypeEligibilityCriteria = 
					ContractServiceDelegate.getInstance().loadMoneyTypeEligibilityCriteria(contractId);
				
				ArrayList<EligibilityCalculationMoneyType> eligibilityCalculationMoneyTypeList = 
					new ArrayList<EligibilityCalculationMoneyType>();
				
		    	 if(moneyTypeEligibilityCriteria != null) {
		    		 
		      		Iterator contractMoneyTypeIterator = moneyTypeEligibilityCriteria.iterator();
		      		
		      		while(contractMoneyTypeIterator.hasNext()){
		      			MoneyTypeEligibilityCriterion moneyTypeEligibilityCriterion=(MoneyTypeEligibilityCriterion)contractMoneyTypeIterator.next();
		      			String attributeValue=ecCSF.getAttributeValue(moneyTypeEligibilityCriterion.getMoneyTypeId().trim());
		      			// Condition to get only the selected Money types form the CSF tables
		      			if(ContractServiceFeature.internalToBoolean(attributeValue).booleanValue()){
		      				EligibilityCalculationMoneyType eligibiltyCalculationMoneyType=new EligibilityCalculationMoneyType();
		      				//Set Attribute name
		      				eligibiltyCalculationMoneyType.setMoneyTypeName(moneyTypeEligibilityCriterion.getMoneyTypeId().trim());
		      				eligibiltyCalculationMoneyType.setMoneyTypeShortName(moneyTypeEligibilityCriterion.getContractMoneyTypeShortName());
		      				//Set Attribute value
		      				eligibiltyCalculationMoneyType.setMoneyTypeValue(ContractServiceFeature.internalToBoolean(attributeValue).booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);

		      				eligibiltyCalculationMoneyType.setCsfValue(ContractServiceFeature.internalToBoolean(attributeValue).booleanValue() ? CsfConstants.CSF_YES : CsfConstants.CSF_NO);

		      				//Set Attribute description
		      				eligibiltyCalculationMoneyType.setMoneyTypeDescription(moneyTypeEligibilityCriterion.getContractMoneyTypeLongName()+" ("+moneyTypeEligibilityCriterion.getContractMoneyTypeShortName()+")");
		      				eligibilityCalculationMoneyTypeList.add(eligibiltyCalculationMoneyType);
		      			}
		      		}
		    		//set the eligible money type list in the PlanSponsorServicesData.
		      		data.setEligibilityServiceMoneyTypes(eligibilityCalculationMoneyTypeList);
		    	 }
			}
			
			if(data.getEligibilityServiceMoneyTypes() != null &&
				data.getEligibilityServiceMoneyTypes().size() > 0){
				value = true;
			}else{		
				value = false; 
			}
			
			// This value is to determine whether we need to show sub section or not.
			data.setECon(value);
		}
	}
	
	/**
	 * Method to determine Plan Support Services: JH EZstart
	 * 
	 * @param ContractServiceFeatures
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public static void determineJHEZstart(
			Map ContractServiceFeatures, PlanSponsorServicesData data){
		
		ContractServiceFeature jhEZstart = (ContractServiceFeature) ContractServiceFeatures
		.get(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
		
		ContractServiceFeature dmCSF = (ContractServiceFeature) ContractServiceFeatures
		.get(ServiceFeatureConstants.DIRECT_MAIL_FEATURE);
		
		 if (jhEZstart != null) {
				boolean value = ContractServiceFeature.internalToBoolean(
						jhEZstart.getValue()).booleanValue();
				data.setJhEZstart(value ? ContentConstants.JH_EZSTART_YES
						: ContentConstants.JH_EZSTART_NO);
				data.setEzStartOn(value);
				String  initialEnrollmentDate= jhEZstart.getAttributeValue(
						ServiceFeatureConstants.AUTO_ENROLLMENT_INITIAL_ENROLLMENT_DATE);
				
				data.setInitialEnrollmentParam(CsfUtil.convertDateFormat(initialEnrollmentDate, CsfConstants.aciDBDateFormat, CsfConstants.aciDisplayDateFormat));
				data.setInitialEnrollmentDate(ContentConstants.JH_EZSTART_INITIAL_ENROLLMENT_DATE);
		 }
		 if(dmCSF != null ){
			 boolean value = ContractServiceFeature.internalToBoolean(
					 dmCSF.getValue()).booleanValue();
			 data.setDirectMailEnrollment(value ? ContentConstants.JH_EZSTART_DM_ENROLLMENT_MATERIALS_YES
						: ContentConstants.JH_EZSTART_DM_ENROLLMENT_MATERIALS_NO);
		 }
	}
	
	/**
	 * Method to determine Plan Support Services: JH EZIncrease
	 * 
	 * @param ContractServiceFeatures
	 * @param data
	 * @throws SystemException 
	 */
	@SuppressWarnings("unchecked")
	public static void determineJHEZIncrease(
			Map ContractServiceFeatures, PlanSponsorServicesData data) throws  SystemException{

		ContractServiceFeature jhEZincrease = 
			(ContractServiceFeature) ContractServiceFeatures.get(
					ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE);


		if (jhEZincrease != null) {
			boolean value = ContractServiceFeature.internalToBoolean(
					jhEZincrease.getValue()).booleanValue();
			data.setJhEZincrease(value ? ContentConstants.JH_EZINCREASE_YES
					: ContentConstants.JH_EZINCREASE_NO);
			data.setEzIncreaseOn(value);

			String  anniversaryDate = 
				ContractServiceDelegate.getInstance().getContractAnniversaryDate(jhEZincrease.getContractId());
			if(anniversaryDate != null && StringUtils.isNotBlank(anniversaryDate)){
				data.setFirstSchedParam(CsfUtil.convertDateFormat(anniversaryDate, CsfConstants.aciDBDateFormat, CsfConstants.aciDisplayDateFormat));
				data.setFirstScheduledIncrease(ContentConstants.JH_EZINCREASE_ANNUAL_INCREASE);
			}
			
			String  initialIncreaseAnniversaryDate = 
				jhEZincrease.getAttributeValue(
						ServiceFeatureConstants.ACI_INCREASE_NEW_ENROLLES_ANNIVERSARY);
			
			String defaultInitialAnniversary = CsfDataHelper.getAttributeDefaultValue(
					ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE, ServiceFeatureConstants.ACI_INCREASE_NEW_ENROLLES_ANNIVERSARY);

			if(defaultInitialAnniversary != null && !defaultInitialAnniversary.equals(initialIncreaseAnniversaryDate)){
				data.setInitialIncreaseAnniversaryDate(ContentConstants.JH_EZINCREASE_ANNUAL_DATE_ENROLLMENT);
				data.setEzIncreaseCustomized(true);
			}
		}
	}
	
	/**
	 * Method to determine the Vesting Schedule
	 * 
	 * @param ContractServiceFeatures
	 * @param data
	 * @throws SystemException 
	 */
	@SuppressWarnings("unchecked")
	public static void determineVestingSchedules(
			Map ContractServiceFeatures, PlanSponsorServicesData data) throws SystemException{
		
		ContractServiceFeature vesting = (ContractServiceFeature) ContractServiceFeatures
		.get(ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
		
		int vestingCode = 0;
		boolean isVestingOn = false;
		
		 if (vesting != null) {
				String vestingValue = vesting.getValue();
				
				if(ServiceFeatureConstants.VESTING_PERCENTAGE_CALCULATED_BY_JH.equals(vestingValue)){
					vestingCode = ContentConstants.VESTING_CALCULATED_BY_JH;
					isVestingOn = true;
				}else if(/*ServiceFeatureConstants.VESTING_PERCENTAGE_SUBMITTED_BY_TPA*/"TPAP".equals(vestingValue)){
					vestingCode = ContentConstants.VESTING_SUBMITTED_BY_TPA;
					isVestingOn = true;
				}else if(ServiceFeatureConstants.VESTING_PERCENTAGE_NOT_AVAILABLE.equals(vestingValue)){
					vestingCode = ContentConstants.VESTING_SERVICE_NOT_AVAILABLE;
				}
				
				data.setVesting(vestingCode);
				data.setVestingOn(isVestingOn);
				
				String reportVesting = vesting.getAttributeValue(ServiceFeatureConstants.VESTING_PERCENTAGE_ON_STATEMENT);
				
				if(ServiceFeatureConstants.NO.equals(reportVesting) && 
						!ServiceFeatureConstants.VESTING_PERCENTAGE_NOT_AVAILABLE.equals(vestingValue) ){
					data.setReportVestingPercentages(ContentConstants.REPORT_VESTING_PERCENTAGES);
					data.setVestingLabelSurpressed(true);
				}
		 }
	}

	/**
	 * Method to determine the Electronic Transaction sub section
	 * 
	 * @param ContractServiceFeatures
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public static void determineElectronicTransactions(
			Map ContractServiceFeatures, PlanSponsorServicesData data){
		
		ContractServiceFeature payrollCSF = (ContractServiceFeature) ContractServiceFeatures
		.get(ServiceFeatureConstants.PAYROLL_FREQUENCY);
		
		ContractServiceFeature payrollPath = (ContractServiceFeature) ContractServiceFeatures
		.get(ServiceFeatureConstants.PAYROLL_PATH_FEATURE);
		
		ContractServiceFeature concentCSF = (ContractServiceFeature) ContractServiceFeatures
		.get(ServiceFeatureConstants.CONSENT_SERVICE_FEATURE);
		
		String payrollFrequency = null;
		
		if(payrollCSF != null){  
			payrollFrequency = payrollCSF.getValue();
		}

		if(CsfConstants.PAYROLL_FREQUENCY_UNSPECIFIED_CODE.equals(payrollFrequency)){
			data.setPayrollFrequency(ContentConstants.PAYROLL_FREQUENCY_UNSPECIFIED);
		}else{
			data.setPayrollFrequency(ContentConstants.PAYROLL_FREQUENCY);
			data.setPayrollFreqParam(getPayrollFrequencyDisplayValue(payrollFrequency));
		}
		
		if(payrollPath != null ){
			boolean value = ContractServiceFeature.internalToBoolean(
					payrollPath.getValue()).booleanValue();
			data.setAllowPayrollPathSubmissions(value ? ContentConstants.ALLOW_PAYROLL_PATH_YES
					: ContentConstants.ALLOW_PAYROLL_PATH_NO);
		}
		
		if(concentCSF != null ){
			String concentValue = concentCSF.getValue();
			if(ServiceFeatureConstants.YES.equals(concentValue)){
				data.setConsentSubsequentAmendments(ContentConstants.CONSENT_SUBSEQUENT_AMENDMENTS_YES);
			}else if(ServiceFeatureConstants.NO.equals(concentValue)){
				data.setConsentSubsequentAmendments(ContentConstants.CONSENT_SUBSEQUENT_AMENDMENTS_NO);
			}else{
				data.setConsentSubsequentAmendments(ContentConstants.CONSENT_SUBSEQUENT_AMENDMENTS_NA);
			}
		}
		//CSF.104	If contract effective date for selected contract was before the COW implementation date, do not display this
		
		data.setConsentallowed(true);
		
	}
	
	/**
	 * Method to get the payroll frequency display value
	 * @param planFrequencyCode
	 * @return
	 */
	private static String getPayrollFrequencyDisplayValue(String planFrequencyCode) {
		return CsfBaseController.payrollFrequencyMap.get(planFrequencyCode);
	}
	
	/**
	 * Method to determine the Online Loans section
	 * 
	 * @param ContractServiceFeatures
	 * @param data
	 * @throws SystemException 
	 */
	@SuppressWarnings("unchecked")
	public static void determineOnlineLoans(
			Map ContractServiceFeatures, PlanSponsorServicesData data) throws SystemException{
		ContractServiceFeature loansCSF = (ContractServiceFeature) ContractServiceFeatures
		.get(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
		
		if(loansCSF != null ){
			boolean value = ContractServiceFeature.internalToBoolean(
					loansCSF.getValue()).booleanValue();
			
			data.setOnlineLoans(value ? ContentConstants.ONLINE_LOANS_YES
					: ContentConstants.ONLINE_LOANS_NO);
			data.setOnlineLoansAllowed(value);
			
			if(value){
				String code = loansCSF.getAttributeValue(ServiceFeatureConstants.LOANS_CHECKS_MAILED_TO);
				if (CsfConstants.CHECKS_MAILED_TO_PAYEE.equals(code)) {
					data.setLoanMailedParam(CsfConstants.PAYEE);
				} else if (CsfConstants.CHECKS_MAILED_TO_TPA.equals(code)) {
					data.setLoanMailedParam(CsfConstants.TPA);
				} else if (CsfConstants.CHECKS_MAILED_TO_CLIENT.equals(code)) {
					data.setLoanMailedParam(CsfConstants.CLIENT);
				} else if (CsfConstants.CHECKS_MAILED_TO_TRUSTEE.equals(code)) {
					data.setLoanMailedParam(CsfConstants.TRUSTEE);
				}
				data.setLoanChecksMailed(ContentConstants.LOAN_CHECKS_MAILED);
			}
			
			boolean displayLoansFeatures = false;
			
			String loanPriorApprovel = loansCSF.getAttributeValue(ServiceFeatureConstants.WHO_WILL_REVIEW_LOANS);
			String loanApprover = loansCSF.getAttributeValue(ServiceFeatureConstants.CREATOR_OF_LOANREQUEST_APPROVE);
			String loanPackagesGenerated = loansCSF.getAttributeValue(ServiceFeatureConstants.ALLOW_LOANS_PACKAGE_TO_GENERATE);
			
			String defaultLoanPriorApprovel = CsfDataHelper.getAttributeDefaultValue(
					ServiceFeatureConstants.ALLOW_LOANS_FEATURE, ServiceFeatureConstants.WHO_WILL_REVIEW_LOANS);
			String defaultLoanApprover = CsfDataHelper.getAttributeDefaultValue(
					ServiceFeatureConstants.ALLOW_LOANS_FEATURE, ServiceFeatureConstants.CREATOR_OF_LOANREQUEST_APPROVE);
			String defaultLoanPackagesGenerated = CsfDataHelper.getAttributeDefaultValue(
					ServiceFeatureConstants.ALLOW_LOANS_FEATURE, ServiceFeatureConstants.ALLOW_LOANS_PACKAGE_TO_GENERATE);
			
			TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(loansCSF.getContractId());
			if (firmInfo != null) {
				defaultLoanPriorApprovel = ServiceFeatureConstants.WHO_WILL_REVIEW_TPA;
			} else {
				defaultLoanPriorApprovel = ServiceFeatureConstants.WHO_WILL_REVIEW_PS;
			}
			
			if(defaultLoanApprover != null && !defaultLoanApprover.equals(loanApprover)){
				data.setLoanApprover(ContentConstants.CAN_THE_INITIATOR_OF_A_LOAN_REQUEST_ALSO_APPROVE_IT_NO);
				displayLoansFeatures = true;
			}
			
			if(defaultLoanPackagesGenerated != null && !defaultLoanPackagesGenerated.equals(loanPackagesGenerated)){
				data.setLoanPackagesGenerated(ContentConstants.LOAN_PACKAGES_YES);
				displayLoansFeatures = true;
			}
			
			if(defaultLoanPriorApprovel != null && !defaultLoanPriorApprovel.equals(loanPriorApprovel)
					&& !ServiceFeatureConstants.WHO_WILL_REVIEW_PS.equals(defaultLoanPriorApprovel)){
				data.setLoanPriorApproval(ContentConstants.LOAN_PRIOR_APPROVAL);
				displayLoansFeatures = true;
			}
			data.setLoanLabelSurpressed(displayLoansFeatures);
		}
	}
	
	/**
	 * Method to determine IWIthdrawals section
	 * 
	 * @param ContractServiceFeatures
	 * @param data
	 * @throws SystemException 
	 */
	@SuppressWarnings("unchecked")
	public static void determineIWithdrawals(
			Map ContractServiceFeatures, PlanSponsorServicesData data) throws SystemException{
		ContractServiceFeature iWithdrawalCSF = (ContractServiceFeature) ContractServiceFeatures
		.get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
		
		
		if(iWithdrawalCSF != null ){
			boolean value = ContractServiceFeature.internalToBoolean(
					iWithdrawalCSF.getValue()).booleanValue();
			
			data.setiWithdrawals(value ? ContentConstants.IWITHDRAWALS_YES
					: ContentConstants.IWITHDRAWALS_NO);
			data.setIWithdrawalsAllowed(value);
			
			if(value){
				String withdrawalIRSSpecialTaxNotices = iWithdrawalCSF.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_SPECIAL_TAX);
				String withdrawalPriorApproval = iWithdrawalCSF.getAttributeValue(ServiceFeatureConstants.WHO_WILL_REVIEW_WITHDRAWALS);
				String withdrawalApproval = iWithdrawalCSF.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_APPROVE);
				
				if(ServiceFeatureConstants.YES.equals(withdrawalIRSSpecialTaxNotices)){
					data.setWithdrawalIRSSpecialTaxNotices(ContentConstants.JH_IRS_TAX_NOTICE_YES);
				}else if(ServiceFeatureConstants.NO.equals(withdrawalIRSSpecialTaxNotices)){
					data.setWithdrawalIRSSpecialTaxNotices(ContentConstants.JH_IRS_TAX_NOTICE_NO);
				}
				String code = iWithdrawalCSF.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_MAILED_TO);
				if (CsfConstants.CHECKS_MAILED_TO_PAYEE.equals(code)) {
					data.setWithdrawMailedParam(CsfConstants.PAYEE);
				} else if (CsfConstants.CHECKS_MAILED_TO_TPA.equals(code)) {
					data.setWithdrawMailedParam(CsfConstants.TPA);
				} else if (CsfConstants.CHECKS_MAILED_TO_CLIENT.equals(code)) {
					data.setWithdrawMailedParam(CsfConstants.CLIENT);
				} else if (CsfConstants.CHECKS_MAILED_TO_TRUSTEE.equals(code)) {
					data.setWithdrawMailedParam(CsfConstants.TRUSTEE);
				}
				data.setWithdrawalChecksMailed(ContentConstants.IWITHDRAWALS_CHECKS_MAILED);
				
				String defaultWithdrawalPriorApproval = CsfDataHelper.getAttributeDefaultValue(
						ServiceFeatureConstants.IWITHDRAWALS_FEATURE, ServiceFeatureConstants.WHO_WILL_REVIEW_WITHDRAWALS);
				
				String defaultWithdrawalApproval = CsfDataHelper.getAttributeDefaultValue(
						ServiceFeatureConstants.IWITHDRAWALS_FEATURE, ServiceFeatureConstants.IWITHDRAWALS_APPROVE);
				
				
				TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(iWithdrawalCSF.getContractId());
				if (firmInfo != null) {
					defaultWithdrawalPriorApproval = ServiceFeatureConstants.WHO_WILL_REVIEW_TPA;
				} else {
					defaultWithdrawalPriorApproval = ServiceFeatureConstants.WHO_WILL_REVIEW_PS;
				}
				boolean displayWithdrawFeatures = false;
				
				if(ServiceFeatureConstants.WHO_WILL_REVIEW_PS.equals(withdrawalPriorApproval) 
						&& !ServiceFeatureConstants.WHO_WILL_REVIEW_PS.equals(defaultWithdrawalPriorApproval)){
					data.setWithdrawalPriorApproval(ContentConstants.IWITHDRAWALSPRIOR_APPROVAL_PLAN_SPONSOR);
					displayWithdrawFeatures = true;
				}else if (ServiceFeatureConstants.WHO_WILL_REVIEW_NO_REVIEW.equals(withdrawalPriorApproval)
						&& !ServiceFeatureConstants.WHO_WILL_REVIEW_NO_REVIEW.equals(defaultWithdrawalPriorApproval)){
					data.setWithdrawalPriorApproval(ContentConstants.IWITHDRAWALSPRIOR_APPROVAL_NO_REVIEW);
					displayWithdrawFeatures = true;
				}
				if(defaultWithdrawalApproval != null && !defaultWithdrawalApproval.equals(withdrawalApproval)){
					data.setWithdrawalApproval(ContentConstants.CAN_THE_INITIATOR_OF_A_WITHDRAW_REQUEST_ALSO_APPROVE_IT_NO);
					displayWithdrawFeatures = true;
				}
				
				data.setWithdrawalLabelSurpressed(displayWithdrawFeatures);
			}
		}
	}
	
	/**
     * Method to determine Investment Policy Statement Service
     * 
     * @param int contractId
     * @param PlanSponsorServicesData data
     */
    public static void determineIps(int contractId, PlanSponsorServicesData data) throws SystemException {
        
        boolean ipsAvailable = false;
        DayOfYear annualReviewDate = null;
        
        InvestmentPolicyStatementVO ipsVO = ContractServiceDelegate.
            getInstance().getIpsBaseData(contractId);
        
        if (ipsVO != null) {
            ipsAvailable = ipsVO.isIpsAvailable();
            annualReviewDate = ipsVO.getAnnualReviewDate();
        }
        
        if(!ipsAvailable ) {
            data.setIpsService(CsfConstants.CSF_NO);
        } else {
            data.setIpsServiceContent(ContentConstants.INVESTMENT_POLICY_STATEMENT_SERVICE);
            data.setAnnualReviewProcessingDateContent(ContentConstants.ANNUAL_REVIEW_SERVICE_PROCESSING_DATE);
            data.setIpsService(CsfConstants.CSF_YES);
            if (annualReviewDate != null) {
                data.setAnnualReviewProcessingDateContentParam(annualReviewDate.toString());
            }
        }
    }
    
    /** 
     * Method to determine which CMA Content to use for Payroll Feedback Service.
     * 
     * @param csFeaturesMap
     * @param planSponsorData
     * 
     * @throws SystemException
     */
	public static void determinePayrollFeedbackService(Map csFeaturesMap, PlanSponsorServicesData planSponsorData)
			throws SystemException {
		planSponsorData.setPayrollFeedbackServiceContent(ContentConstants.PAYROLL_FEEDBACK_SERVICE_NOT_USED);

		ContractServiceFeature payrollFeedbackService = (ContractServiceFeature) csFeaturesMap
				.get(ServiceFeatureConstants.PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE);
		if (payrollFeedbackService != null && ServiceFeatureConstants.YES.equals(payrollFeedbackService.getValue())) {
			planSponsorData.setPayrollFeedbackServiceContent(ContentConstants.PAYROLL_FEEDBACK_SELF_SERVICE);
			if (ServiceFeatureConstants.YES.equals(payrollFeedbackService.getAttributeMap().get(ServiceFeatureConstants.PAYROLL_FEEDBACK_360_SERVICE_ATTRIBUTE_CODE))) {
				planSponsorData.setPayrollFeedbackServiceContent(ContentConstants.PAYROLL_FEEDBACK_360_SERVICE_PAYROLL);
			}
		}
	}
 
}
