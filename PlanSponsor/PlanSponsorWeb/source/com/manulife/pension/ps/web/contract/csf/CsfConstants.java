package com.manulife.pension.ps.web.contract.csf;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;


/**
 * Class to handle the CSF constants. 
 * 
 * @author Puttaiah Arugunta
 *
 */
public class CsfConstants {

	public final static String CSF_YES = "Yes";
	public final static String CSF_NO = "No";
	public final static String CSF_NO_DESELECTED = "No (De-selected)";
	public final static String CSF_TRUE = "true";
	public final static String CSF_FALSE = "false";
	
	// CSF button values
	public static final String EDIT_BUTTON = "edit";
	public static final String SAVE_BUTTON = "save";
	public static final String BACK_BUTTON = "cancel";
	
	public static final String VESTING_PERCENTAGES_NO = "N/A";

	// Payroll frequency display values
	public static final String PAYROLL_FREQUENCY_WEEKLY = "Weekly";
	public static final String PAYROLL_FREQUENCY_BI_WEEKLY = "Bi-weekly";
	public static final String PAYROLL_FREQUENCY_SEMI_MONTHLY = "Semi-monthly";
	public static final String PAYROLL_FREQUENCY_MONTHLY = "Monthly";
	public static final String PAYROLL_FREQUENCY_UNSPECIFIED = "Unspecified";
	// Payroll frequency database values 
	public static final String PAYROLL_FREQUENCY_WEEKLY_CODE = ServiceFeatureConstants.PAYROLL_FREQUENCY_WEEKLY_CODE;
	public static final String PAYROLL_FREQUENCY_BI_WEEKLY_CODE = ServiceFeatureConstants.PAYROLL_FREQUENCY_BI_WEEKLY_CODE;
	public static final String PAYROLL_FREQUENCY_SEMI_MONTHLY_CODE = ServiceFeatureConstants.PAYROLL_FREQUENCY_SEMI_MONTHLY_CODE;
	public static final String PAYROLL_FREQUENCY_MONTHLY_CODE = ServiceFeatureConstants.PAYROLL_FREQUENCY_MONTHLY_CODE;
	public static final String PAYROLL_FREQUENCY_UNSPECIFIED_CODE = ServiceFeatureConstants.PAYROLL_FREQUENCY_UNSPECIFIED_CODE;
	
	// Address management display values
	public static final String ADDRESS_MANAGEMENT_ALL = "All";
	public static final String ADDRESS_MANAGEMENT_NONE = "None";
	public static final String ADDRESS_MANAGEMENT_ACTIVE = "Active";
	public static final String ADDRESS_MANAGEMENT_TERMINATED = "Terminated";
	public static final String ADDRESS_MANAGEMENT_RETIRED = "Retired";
	public static final String ADDRESS_MANAGEMENT_DISABLED = "Disabled";
	
	// Plan vesting values
	public static final String PLAN_VESTING_NA = "NA";
	public static final String PLAN_VESTING_CALCULATION = "JHC";
	public static final String PLAN_VESTING_PROVIDED = "TPAP";
	public static final String CONSENT_NA = "NA";
	public static final String CONSENT_BLANK="";
	
	// Vesting CSF values
	public static final String FIELD_VESTING_PERCENTAGES_METHOD = "vestingPercentagesMethod";
	public static final String FIELD_VESTING_DATA_ON_STATEMENT = "vestingDataOnStatement";
	
	// Withdrawal CSF values
	public static final String FIELD_WITHDRAWALS_IND = "withdrawalInd";
	public static final String FIELD_APPROVE_WITHDRAWALS = "creatorMayApproveInd";
	public static final String FIELD_REVIEW_WITHDRAWALS = "onlineWithdrawalProcess";
	public static final String FIELD_WHO_WILL_REVIEW_WITHDRAWALS = "whoWillReviewWithdrawals";
	public static final String FIELD_PARTICIPANT_WITHDRAWAL_IND = "participantWithdrawalInd";
	public static final String FIELD_SPECIAL_TAX_NOTICE = "specialTaxNotice";
	public static final String FIELD_CHECKS_MAILED_TO = "checksMailedTo";
	
	// Who will review withdrawal values
	public static final String WHO_WILL_REVIEW_WD_NOREVIEW = "NR";
	public static final String WHO_WILL_REVIEW_WD_TPA = "TPA";
	public static final String WHO_WILL_REVIEW_WD_PS = "PS";
	
	// Allow Online loans fields
	public static final String FIELD_ALLOW_ONLINE_LOANS = "allowOnlineLoans";
	public static final String FIELD_PARTICIPANT_INITIATE_LOANS = "participantInitiateLoansInd";
	public static final String FIELD_WHO_WILL_REVIEW_LOANS = "whoWillReviewLoanRequests";
	public static final String FIELD_CREATOR_APPROVE_LOANS = "creatorMayApproveLoanRequestsInd";
	public static final String FIELD_LOANS_CHECKS_MAILED_TO = "loansChecksMailedTo";
	public static final String FIELD_ALLOW_LOANS_PKG_GENERATE = "allowLoansPackageToGenerate";
	
	// Auto Payroll CSF fields
	public static final String FIELD_PAYROLL_PATH_IND = "autoPayrollInd";
	
	// Direct Mail
	public static final String FIELD_DIRECT_MAIL_IND = "directMailInd";
	
	 // Auto Enrollment CSF values
	public static final String FIELD_AUTO_ENROLL_IND = "autoEnrollInd";
	public static final String FIELD_PLAN_FREQUENCY = "planFrequency";
	public static final String FIELD_PLAN_ENTRY_DATE = "basePlanEntryDate";
	public static final String FIELD_ENROLLMENT_DATE = "initialEnrollmentDate";
	public static final String FIELD_OPT_OUT_DAYS = "optOutDeadlineDays";
	public static final String FIELD_DEFERRAL_PERCENTAGE = "defaultDeferralPercentage";
	
	// Address management CSF values
	public static final String FIELD_ADDRESS_MANAGEMENT = "addressManagementView";
	public static final String FIELD_ACTIVE_ADDRESS = "activeAddress";
	public static final String FIELD_TERMINATED_ADDRESS = "terminatedAddress";
	public static final String FIELD_RETIRED_ADDRESS = "retiredAddress";
	public static final String FIELD_DISABLED_ADDRESS = "disabledAddress";
	
	// AE attribute introduced by AEE project
	public static final String PARTICIPANT_OPT_OUT_90DAYS = "participantOptOut90Days";
	public static final String FIELD_CHANGE_DEFERRALS_ONLINE = "changeDeferralsOnline";
	public static final String FIELD_ENROLL_ONLINE = "enrollOnline";
	
	
	//Eligibility Calculation values
	public static final  String FIELD_ELIGIBLITY_CALCULATION_IND="eligibilityCalculationInd";
	
	// ConsentInd section field
	public static final String FIELD_CONSENT_IND = "consentInd";
	
	// Plan Highlights field
	public static final String FIELD_SUMMARY_PLAN_HIGHLIGHT_AVAILABLE = "summaryPlanHighlightAvailable";
	public static final String FIELD_SUMMARY_PLAN_HIGHLIGHT_REVIEWED = "summaryPlanHighlightReviewed";
	
	public static final String REQ_PLAN_DATA_LITE = "planDataLite";
	public static final String REQ_IS_457B_PLAN = "is457bPlan";
	public static final String ERROR_MESSAGE = "Contract with payee Type of trustee cannot allow participants to initiate loan requests";
	
	// edelivery for plan notices and statements
	public static final String FIELD_WIRED_AT_WORK = "wiredAtWork";
	public static final String FIELD_NOTICE_OF_INTERNET_AVAILABILITY = "noticeOfInternetAvailability";
	
	// Vesting possible values
	public static final String VESTING_SERVICE_CREDIT_METHOD_HOURS_OF_SERVICE = "H";
	public static final String VESTING_SERVICE_CREDIT_METHOD_ELAPSED_TIME = "E";
	public static final String VESTING_SERVICE_CREDIT_METHOD_UNSPECIFIED = "U";
	public static final String VESTING_COMPUTATION_BASED_ON_HOS_FIRST_AND_EACH_ANNIVERSARY_THEREOF_CODE = "A";
	public static final String VESTING_COMPUTATION_PERIOD_PLAN_YEAR_CODE = "P";
	public static final String VESTING_COMPUTATION_PERIOD_UNSPECIFIED = "U";
	
	// Possible date formats in CSF pages
	public static FastDateFormat aciDBDateFormat = ContractDateHelper.getDateFormatterLocale("yyyy-MM-dd", Locale.US);
	public static FastDateFormat aciDisplayDateFormat = ContractDateHelper.getDateFormatterLocale("MM/dd/yyyy", Locale.US);
	public static FastDateFormat initialEnrollmentDateFormat = ContractDateHelper.getDateFormatterLocale("MM/dd/yyyy", Locale.US);
	public static FastDateFormat ecDeletionDateFormat = ContractDateHelper.getDateFormatterLocale("MMM/dd/yyyy", Locale.US);
	
	// Decimal formatter
	public static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("##,###.##");
	
	// page forward constants
	public static final String CSF_ELECTRON_CONTRACT_SERVICES_PAGE = "csfElectronServices";
	public static final String CSF_ELECTRON_CONTRACT_SERVICES_EDIT_PAGE = "csfElectronServicesEdit";
	public static final String CSF_ELECTRON_CONTRACT_SERVICES_CONFIRMATION_PAGE = "csfElectronServicesConf";

	public static final String FIELD_ELIGIBILITY_CALCULATION_IND = "eligibilityCalculationInd";
	public static final String FIELD_WHO_WILL_REVIEW_LOAN_REQUESTS = "whoWillReviewLoanRequests";
	public static final String FIELD_ACI_ANNIVERSARY_DATE = "aciAnniversaryDate";
	public static final String FIELD_ACI_ANNIVERSARY_YEAR = "aciAnniversaryYear";
	public static final String FIELD_INITIAL_ENROLLMENT_DATE = "initialEnrollmentDate";
	public static final String FIELD_PARTICIPANT_INITIATE_LOANS_IND = "participantInitiateLoansInd";
	public static final String FIELD_PAYROLL_CUTOFF = "payrollCutoff";
	public static final String FIELD_DEFERRAL_MAX_LIMIT_DOLLARS = "deferralMaxLimitDollars";
	public static final String FIELD_DEFERRAL_LIMIT_DOLLARS1 = "deferralLimitDollars";
	public static final String FIELD_DEFERRAL_MAX_LIMIT_PERCENT = "deferralMaxLimitPercent";
	public static final String FIELD_AUTO_CONTRIBUTION_INCREASE = "autoContributionIncrease";
	public static final String FIELD_INCREASE_ANNIVERSARY = "increaseAnniversary";
	public static final String FIELD_DEFERRAL_LIMIT_DOLLARS = "deferralLimitDollars";
	public static final String FIELD_DEFERRAL_LIMIT_PERCENT = "deferralLimitPercent";
	public static final String FIELD_DEFERRAL_TYPE = "deferralType";
	public static final String FIELD_PAYROLL_FEEDBACK_SERVICE = "payrollFeedbackService";
	
	public static final String EMPTY_STRING = "";
	
	public static final String CSF_TAB = "csfTab";	
	
	public static final String PLAN_FREQUENCY_SEMI_ANNUAL ="S";
	public static final String PLAN_FREQUENCY_QUARTERLY ="Q";
	public static final String PLAN_FREQUENCY_MONTHLY ="M";
	public static final String GLOBAL_HOME_PAGE_FINDER = "homePageFinder";
	public static final String PARTICIPANT_ACI_FEATURE_VO = "paf";
	public static final String CHANGED_CSF_COLLECTION = "changedCsfCollection";
	public static final String CONTRACT_PRODUCT_FEATURE_LRK01 = "%LRK01%";
	public static final String INCREASE_ANNIVERSARY_SECOND = "F";
	public static final String CHECKS_MAILED_TO_PAYEE = "PY";
	public static final String CHECKS_MAILED_TO_TPA = "TP";
	public static final String CHECKS_MAILED_TO_CLIENT = "CL";
	public static final String CHECKS_MAILED_TO_TRUSTEE = "TR";
	public static final String PAYEE = "Payee";
	public static final String TPA = "TPA";
	public static final String CLIENT = "Client";
	public static final String TRUSTEE = "Trustee";
	public static final String HIDE_CONSENT_INFORMATION = "hideConsent";
	public static final String CONTINUE_EDIT_VALUE = "continue editing";
	public static final String CSF_FORM = "csfForm";
	public static final String DEFAULT_ANNIVERSARY_YEAR = "1970";
	public static final String DEFAULT_DEFERRAL_MAX_AMOUNT = "500";
	public static final String DEFAULT_DEFERRAL_AMOUNT = "25";
	public static final String DEFAULT_DEFERRAL_MAX_PERCENTAGE = "10";
	public static final String DEFAULT_DEFERRAL_PERCENTAGE = "1";
	public static final int DEFAULT_EC_MONEY_TYPES_SIZE = 20;
	// Default values for the CSF page.
	
	public static final String FIELD_ACTIVE_ADDRESS_DEFAULT_VALUE = CSF_FALSE;
	public static final String FIELD_TERMINATED_ADDRESS_DEFAULT_VALUE = CSF_FALSE;
	public static final String FIELD_RETIRED_ADDRESS_DEFAULT_VALUE = CSF_FALSE;
	public static final String FIELD_DISABLED_ADDRESS_DEFAULT_VALUE = CSF_FALSE;
	public static final String FIELD_DEFERRAL_TYPE_DEFAULT_VALUE = "%";
	public static final String FIELD_CHANGE_DEFERRALS_ONLINE_DEFAULT_VALUE =CSF_NO;
	public static final String FIELD_ENROLL_ONLINE_DEFAULT_VALUE = CSF_NO;
	public static final String FIELD_PAYROLL_CUTOFF_DEFAULT_VALUE = "10";
	public static final String FIELD_PARTICIPANT_INITIATE_LOANS_DEFAULT_VALUE = CSF_NO;
	public static final String FIELD_SPH_DEFAULT_VALUE = CSF_NO;
	public static final  String FIELD_EC_IND_DEFAULT_VALUE=CSF_NO;
	public static final String FIELD_AUTO_ENROLL_IND_DEFAULT_VALUE = CSF_NO;
	public static final String FIELD_INITIAL_ENROLL_DATE_DEFAULT_VALUE = EMPTY_STRING;
	public static final String FIELD_DM_DEFAULT_VALUE=CSF_NO;
	public static final String FIELD_ACI_DEFAULT_VALUE = CSF_NO;
	public static final String FIELD_ANNUVASARY_DATE_DEFAULT_VALUE= EMPTY_STRING;
	public static final String FIELD_INCREASE_ANNIVERSARY_DEFAULT_VALUE ="F";
	
	// Vesting CSF values
	public static final String FIELD_VESTING_DEFAULT_VALUE = PLAN_VESTING_NA;
	public static final String FIELD_VESTING_DATA_DEFAULT_VALUE= CSF_NO;
	
	public static final String FIELD_PLAN_FREQUENCY_DEFAULT_VALUE = PAYROLL_FREQUENCY_UNSPECIFIED_CODE;
	
	// Withdrawal CSF values
	public static final String FIELD_WITHDRAWALS_DEFALUT_VALUE = CSF_YES;
	public static final String FIELD_SPECIAL_TAX_DEFAULT_VALUE = CSF_YES;
	public static final String FIELD_APPROVE_WITHDRAWALS_DEFAULT_VALULE= CSF_YES;
	public static final String FIELD_PARTICIPANT_WITHDRAW_DEFAULT_VLAUE = CSF_NO;
	
	// Allow Online loans fields
	public static final String FIELD_ONLINE_LOANS_DEFAULT_VALUE = CSF_NO;
	public static final String FIELD_WHO_WILL_REVIEW_LOANS_DEFAULT_VALUE = WHO_WILL_REVIEW_WD_PS;
	public static final String FIELD_CREATOR_APPROVE_LOANS_DEFAULT_VALUE = CSF_YES;
	public static final String FIELD_ALLOW_LOANS_PKG_GENERATE_DEFAULT_VLAUE = CSF_NO;
	
	// Auto Payroll CSF fields
	public static final String FIELD_PAYROLL_PATH_DEFAULT_VALUE = CSF_NO;
	
	public static final String PLAN_ACI_UNSPECIFIED ="U";
	public static final String PLAN_REQUIRES_SPOUSAL_CONSENT_UNSPECIFIED="U";
	public static final String FIELD_ANNIVERSAY_DATE_AS_STRING="anniversaryDateAsString";
	public static final String FIELD_INITIAL_ENROLL_DATE_AS_STRING="initialEnrollmentDateAsString";
	
	public static final String WHO_WILL_REVIEW_ATTR_CODE = "RPA";
	
	public static final List<String> DATE_ATTRIBUTES = 
	        Arrays.asList(ServiceFeatureConstants.ACI_ANNIVERSARY_DATE, //ADT
	                      ServiceFeatureConstants.AUTO_ENROLLMENT_INITIAL_ENROLLMENT_DATE); //IED
	
	public static final List<String> NOTICE_SERVICE_ATTRIBUTES = 
	        Arrays.asList(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_OPTCD); //OPTCD
	
	public static final String UNSPECIFIED ="U";
	public static final String MONEY_TYPE_EEROT ="EEROT";
	public static final String CSF_Y ="Y";
	public static final String CSF_N ="N";
	
	// OBDS Field	
	public static final String FIELD_ONLINE_BENEFICIARY_SERVICE = "onlineBeneficiaryInd";
	
	//Notices
	public static final String CONTRIBUTION_AND_DISTRIBUTION_IND = "contributionAndDistributionInd";
	public static final String SAFE_HARBOR_IND = "safeHarborInd";
	public static final String AUTOMATIC_CONTRIBUTION_IND = "automaticContributionInd";
	public static final String INVESTMENT_INFO_IND = "investmentInformationInd";
	public static final String DIO = "dio";
	public static final String TRANSFER_OUT_DAYS = "transferOutDays";
	public static final String AUTO_ENROLL_TYPE = "autoProvisionType";
    public static final String EACA_EMPLOYER_CONTRIB_IND = "eacaEmpContributionInd";
    public static final String QACA_ADDITIONAL_EMPLOYER_CONTRIB_IND = "qacaAddEmpContributionInd";
    public static final String SH_ADDITIONAL_EMP_CONTRIB_IND = "shAddEmpContributionInd";
	
	public static final String NOTICE_OPT_404A5 = "0";
    public static final String NOTICE_OPT_QDIA = "1";
    public static final String NOTICE_OPT_AUTO = "2";
    public static final String NOTICE_OPT_AUTO_QDIA = "3";
    public static final String NOTICE_OPT_SH = "4";
    public static final String NOTICE_OPT_SH_QDIA = "5";
    
    public static final String NOTICE_OPT_404A5_DESC = "404a-5 Plan & Investment Notice Only";
    public static final String NOTICE_OPT_QDIA_DESC = "Qualified Default Investment Alternative Only";
    public static final String NOTICE_OPT_AUTO_DESC = "Automatic Arrangement";
    public static final String NOTICE_OPT_AUTO_QDIA_DESC = "Automatic Arrangement with Qualified Default Investment Alternative";
    public static final String NOTICE_OPT_SH_DESC = "Safe Harbor";
    public static final String NOTICE_OPT_SH_QDIA_DESC = "Safe Harbor with Qualified Default Investment Alternative";
    
    public static final String AUTO_ENROLL_TYPE_EACA = "EACA";
    public static final String AUTO_ENROLL_TYPE_QACA = "QACA";
    public static final String AUTO_ENROLL_TYPE_ACA = "ACA";
    
    
    public static final String DISTRIBUTION_CHANNEL = "DISTRIBUTION_CHANNEL";
	public static final String GROUP_FIELD_OFFICE_NO = "GROUP_FIELD_OFFICE_NO";
	public static final String GFO_CODE_25270 = "25270";
	public static final String GFO_CODE_25280 = "25280";
	public static final String MTA = "MTA";
	
	public static final String CONTRACT_STATUS_DI = "DI";
	   
	// Managed Accounts Selection field
	public static final String MANAGED_ACCOUNT_SERVICE_AVAILABILITY_DATE_CONFIRM = "managedAccountServiceAvailabilityDate";
    
	   
	public static final String FIELD_PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE 			= "payrollFeedbackService";
	public static final String PAYROLL_FEEDBACK_SERVICE_NOT_AVAILABLE_OPTION_CODE	= ServiceFeatureConstants.NOT_AVAILABLE;
	public static final String PAYROLL_FEEDBACK_SERVICE_ENABLED_OPTION_CODE			= ServiceFeatureConstants.PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE;
	public static final String PAYROLL_FEEDBACK_360_SERVICE_ENABLED_OPTION_CODE		= ServiceFeatureConstants.PAYROLL_FEEDBACK_360_SERVICE_ATTRIBUTE_CODE;
	
	 // Flag to show/hide the Notices Edelivery section in edit/view pages
	public static final String NOTICES_EDELIVERY_EDIT_CONFIRM_ENABLED = "csf.noticesEdelivery.editOrConfirm.enabled";
	public static final String NOTICES_EDELIVERY_VIEW_ENABLED = "csf.noticesEdelivery.view.enabled";
}
