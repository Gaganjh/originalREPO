package com.manulife.pension.ps.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentReportData;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.IntermediaryContact;
import com.manulife.pension.service.security.role.PayrollAdministrator;
import com.manulife.pension.service.security.role.PlanSponsorUser;

/**
 * @author Ilker Celikyilmaz
 *
 * This class is the used to accomodate all the constants.
 */

public final class Constants implements com.manulife.pension.platform.web.CommonConstants {
	

	public static final String COUNTRY_USA = "USA";
	public static final String COUNTRY_USA_CODE = "019";
	public static final String COUNTRY_NY_CODE = "094";
	public static final String YES = "Y";
	public static final String NO = "N";
	public static final String X = "X";
	public static final String YES_INDICATOR = "Yes";
	public static final String NO_INDICATOR = "No";
	public static final String NONE = "None";
	public static final String UBS_COFID = "UBS";
	public static final BigDecimal BIG_DECIMAL_ZERO = new BigDecimal(0.0);
	public static final SimpleDateFormat dateFormat_MMMddyyyy = new SimpleDateFormat("MMM/dd/yyyy");

	// SecurityConstants for the Access Controller (Pilot Feature) entries See
	// AccessControlConfiguration.xml
	public static final String FUNCTION_ACCESS_KEY_TRANSACTION_HISTORY = "TransactionHistory";


	public static final String USERPROFILE_CONFIRMATION_KEY = "USER_CONFIRM_KEY";

	public static final String AVAILABLE_CONTRACTS_LIST_KEY = "AVAILABLE_CONTRACTS_KEY";

	public static final String EXISTING_SITE_USER = "existingSiteUser";

	public static final String CMA_NEWSLETTER_AVAILABLE = "java:comp/env/CMANewsLetterAvailable";

	public static final String MAX_NUMBER_OF_USERS = "maxNumberOfUsers";

	public static final String PLANSPONSOR_OTHER_SITE_MARKETING_URL = "java:comp/env/PSOtherSiteMarketingURL";

	public static final String TPA_OTHER_SITE_MARKETING_URL = "java:comp/env/TPAOtherSiteMarketingURL";

	public static final String GENERAL_CAR_PHONE_NUMBER = "java:comp/env/GeneralCARPhoneNumber";

	// TPA eDownload (TED) variables
	public static final String TED_FILE_DOWNLOAD_DIRECTORY = "ted.fileDownloadDirectory";

	//Send Service for Contract features
	public static final String SEND_SERVICE = "SEND Service";
	
	// enrollment state values
	public static final int NON_ENROLLED = 10;

	public static final int PRE_ENROLLED = 11; // Web or paper enrolled but

	// MAC/PIN not validated
	public static final int ENROLLED = 12;
	public static final int CANNOT_ENROLL = 13;
	public static final int  FIVE_DIGIT_ENDDATE_START_INDEX=10;
	public static final int  SIX_DIGIT_CONTRACT_NUMBER_START_INDEX=5;
	public static final int  SIX_DIGIT_ENDDATE_START_INDEX=11;
	public static final int  SEVEN_DIGIT_CONTRACT_NUMBER_START_INDEX=5;
	public static final int  SEVEN_DIGIT_ENDDATE_START_INDEX=12;
	public static final int  FIVE_DIGIT_CONTRACT_CHAR_VALUE=14;
	public static final int  SIX_DIGIT_CONTRACT_CHAR_VALUE=15;
	public static final int  SEVEN_DIGIT_CONTRACT_CHAR_VALUE=16;
	public static final int  FIVE_DIGIT_CONTRACT_FILE_INDEX=21;
	public static final int  SIX_DIGIT_CONTRACT_FILE_INDEX=22;
	public static final int  SEVEN_DIGIT_CONTRACT_FILE_INDEX=23;
	public static final int FIVE_DIGIT_YEAREND_START_INDEX = 20;
	public static final int SIX_DIGIT_YEAREND_START_INDEX = 21;
	public static final int SEVEN_DIGIT_YEAREND_START_INDEX = 22;



	// site location as is recorded in CSDB database
	public static final String DB_SITEMODE_USA = "US";

	public static final String DB_SITEMODE_NY = "NY";

	public static final String NOTIFICATIONS_LIST = "notifications";

	public static final String RECENT_NOTIFICATIONS_LIST = "recentNotifications";

	public static final String NOTIFICATION_RECENT_DAY_COUNT_URI = "java:comp/env/notificationRecentDayCount";

	public static final String MAX_RECENT_ALERT_COUNT_URI = "java:comp/env/maximumRecentAlertCount";

	public static final String MAX_RECENT_NOTIFICATION_COUNT_URI = "java:comp/env/maximumRecentNotificationCount";

	public static final String MAX_FILE_UPLOAD_SIZE_KBYTES = "java:comp/env/maximumFileUploadSize";

	public static final String CONTRACT_PROFILE = "contractProfile";
	
	public static final String MANAGED_ACCOUNT_SERVICE = "managedAccountService";


	public static final String CONTRACT_SNAPSHOT_DATES = "contractSnapshotDates";

	// Forms & Warranty certificate
	public static final String FORM_URL = "pdfFormURL";

	public static final String TPA_FIRM_NUMBER = "tpa_firm_number";

	/**
	 * Defines the attribute name for the contract ID being used if the user is a TPA and has not
	 * explicitly selected a contract to work with but one can be implicitly derived from the
	 * context of their work. This is useful in cases where a link requires contract information.
	 */
	public static final String TPA_CONTRACT_ID_KEY = "tpa_contract_id_key";

	/**
	 * Defines the attribute name for the contract name being used if the user is a TPA and has not
	 * explicitly selected a contract to work with but one can be implicitly derived from the
	 * context of their work. This is useful in cases where a link requires contract information.
	 */
	public static final String TPA_CONTRACT_NAME_KEY = "tpa_contract_name_key";

	// employee letter pdf fields - auto enrollment
	public static final String PDF_FIELD_NEXT_PLAN_ENTRY_DATE = "next_plan_entry_date";

	public static final String PDF_FIELD_DEFAULT_DEFERRAL = "default_deferral";

	public static final String PDF_FIELD_DEFAULT_INVESTMENT_OPTIONS = "default_investment_options";

	public static final String PDF_FIELD_OPT_OUT_DEADLINE = "opt_out_deadline";

	// PDF fields - EZincrease
	public static final String SHORT_CONTRACT_NAME = "short contract name";

	public static final String PDF_FIELD_PLAN_ENTRY_DATE = "plan entry date";

	public static final String PDF_FIELD_DEFAULT_DEFERRAL_RATE = "default_deferral_rate";

	public static final String PDF_FIELD_DEFAULT_INVESTMENT_OPTION = "default investment option";

	public static final String PDF_FIELD_OPT_OUT_DATE = "opt_out_date";

	public static final String PDF_FIELD_ANNIVERSARY_DATE = "anniversary date";

	public static final String PDF_FIELD_INCREASE_RATE = "$x or x%";

	public static final String PDF_FIELD_PLAN_DEFAULT_MAX = "plan default maximum";

	public static final String PDF_FIELD_PLAN_DEFAULT_INCREASE = "plan default increase";

	// Cash Account Page
	public static final String CASH_ACCOUNT_FROM_DATES = "fromDates";

	public static final String CASH_ACCOUNT_TO_DATES = "toDates";

	public static final String CASH_ACCOUNT_FROM_DATE = "fromDate";

	public static final String CASH_ACCOUNT_TO_DATE = "toDate";

	// Required for the secured home page - RH
	// this is the maximum number of participants to be shown in the list
	// from SPR 73
	// the BA's said that this would change very infrequently
	public static final int MAX_NUM_PART_FOR_SELECT_LIST = 200;

	/**
	 * Forward instructions
	 */
	public static final String HOMEPAGE_FINDER_FORWARD = "redirect:/do/home/homePageFinder/";

	public static final String RELOAD_PAGE_RESET_PAGE_NUMBER_FORWARD = "reloadPageResetPageNumber";

	public static final String FAILURE = "failure";

	public static final String RMI_REPORT_SERVER_NAME = "RMIReportServer";

	public static final String RMI_IFILE_SERVER_NAME = "RMIiFileServer";

	public static final String RMI_IFILE_SERVER_NAME_FAILOVER = "RMIiFileServerFailover";

	public static final String RMI_SUBMISSION_JOURNAL_SERVER_NAME = "RMISubJournalServer";

	public static final String RMI_STP_SERVER_NAME_PRIMARY = "RMISTPServerPrimary";

	public static final String RMI_STP_SERVER_NAME_FAILOVER = "RMISTPServerFailover";

    public static final String LEARNING_CENTER_URL = "LearningCenterURL";

	public static final String CONFIRM_TPA_E_DOWNLOADS = "java:comp/env/ConfirmTPAeDownloads";

	public static final String TED_CURRENT_FILES_REPORT_DATA = "tedCurrentFilesReportData";

	public static final String TED_HISTORY_FILES_REPORT_DATA = "tedHistoryFilesReportData";

	public static final String ESTATEMENTS_PAGE_SIZE_KEY = "java:comp/env/eStatementsPageSize";

	// investment pages

	public static final String[] POOLED_FUND_TYPE = { "UF", "uf" };

	public static final String CHART_RESULT = "result";

	public static final String CHART_INPUT_FORM = "performanceChartInputForm";

	public static final String CELL_BASE_ID = "cell";

	public static final String ANCHOR_BASE_ID = "anchor";

	public static final String ID_SEPARATOR = ".";

	public static final String TABLE_BASE_ID = "table";

	public static final String SECTION_ID = "sec";

	public static final String COLUMN_ID = "col";

	public static final String ROW_ID = "row";

	public static final String WEB_INVESTMENT_REPORT = "investmentReport";

	public static final String WEB_FUNDLOOKUP_REPORT = "fundLookupReport";

	public static final String WEB_SYMBOLS_FOOTNOTES = "symbolsArray";

	public static final String INVESTMENT_BY_RISK_CATEGORY = "investmentByRiskCategory";

	public static final String INVESTMENT_BY_ASSET = "investmentByAsset";

	public static final String VIEW_INVESTMENT = "investmentsView";

	public static final String VIEW_FUNDLOOKUP = "fundLookupView";

	public static final String QUICKREPORTS_KEY = "quickReports";

	// payroll notification event
	public static final String PAYROLL_EVENT_CODE = "12";

	// Contract Status
	public static final String CONTRACT_STATUS_PC = "PC";

	public static final String CONTRACT_STATUS_CA = "CA";

	public static final String CONTRACT_STATUS_CR = "CR";

	public static final String CONTRACT_STATUS_CV = "CV";

	public static final String CONTRACT_STATUS_AC = "AC";

	public static final String CONTRACT_STATUS_CF = "CF";

	public static final String CONTRACT_STATUS_DI = "DI";

	public static final String CONTRACT_STATUS_IA = "IA";

	// Contract Statements SecurityConstants
	public static final String EF_SHORT_NAME = "EF";

	public static final String PA_SHORT_NAME = "PA";

	public static final String SA_SHORT_NAME = "SA";

	public static final String EF_LONG_NAME = "Employer Financial";

	public static final String PA_LONG_NAME = "Plan Administrator";

	public static final String SA_LONG_NAME = "Schedule A";

	// Awaiting payment
	public static final String AWAITING_PAYMENT_STATUS = "95";

	public static final String AWAITING_PAYMENT_PAYROLL_COMPANY = "Payroll Company";

	// Notifications
	// obsolete - replaced by Message Center
	//    public static final String CONTRACT_MESSAGES_URL = "/do/contract/contractMessages";


	// sort option codes - also resides in EJB-tier constants
	public static final String EMPLOYEE_ID_SORT_OPTION_CODE = "EE";

	public static final String SSN_SORT_OPTION_CODE = "SS";

	// i:file constants
	public static final String FILE_UPLOAD_DETAIL_DATA = "fileDetailBean";

	// submission constants
	public static final String SUBMISSION_UPLOAD_HISTORY_DETAIL_DATA = "submissionDetailBean";

	public static final String PAYMENT_UPLOAD_HISTORY_DETAIL_DATA = "paymentDetailBean";

	public static final String EDIT_CONTRIBUTION_CONFIRM_DETAIL_DATA = "editContributionDetailBean";

	public static final String EDIT_VESTING_CONFIRM_DETAIL_DATA = "editVestingDetailBean";
	
	public static final String EDIT_LONG_TERM_PART_TIME_CONFIRM_DETAIL_DATA = "editLongTermPartTimeDetailBean";

	public static final String SUBMISSION_CASE_DATA = "submissionCaseData";

	public static final String CENSUS_DETAILS_HELPER = "censusDetailsHelper";

	public static final String VESTING_DETAILS_HELPER = "vestingDetailsHelper";
	
	public static final String LONG_TERM_PART_TIME_DETAILS_HELPER = "longTermPartTimeDetailsHelper";

	public static final String SHOW_JUST_MINE_FILTER = "showJustMineFilter";

	public static final String COPIED_ITEM_KEY = "copiedItem";

	public static final String COPY_WARNINGS = "copyWarnings";

	public static final String NUM_COPY_WARNINGS = "numCopyWarnings";

	public static final String COPY_IDS = "copyIds";

	public static final String ADDRPERMISSION = "addressPermission";

	public static final String SUBMISSION_HISTORY_ACCESS = "submissionHistoryAccess";

	public static final String VESTING_ACCESS = "vestingAccess";

	public static final String VESTING_CALCULATED = "vestingCalculated";

	// census tabs constants
	public static final String CENSUS_SUMMARY_FORM = "censusSummaryReportForm";

	public static final String EMPLOYEE_ENROLLMENT_SUMMARY_FORM = "employeeEnrollmentSummaryReportForm";


	public static final String DEFERRAL_REPORT_FORM = "deferralReportForm";

	// employee adress constants
	public static final String CENSUS_VESTING_FORM = "censusVestingReportForm";


	public static final String CENSUS_ADDRESS_FORM = "participantAddressesReportForm";

	// address actions (modes) for editing
	public static final String PARTICIPANT_ADDRESS_ACTION = "/participant/saveParticipantAddress/";

	public static final String ONLINE_ADDRESS_ACTION = "/tools/saveOnlineAddress/";

	public static final String SUBMITTED_ADDRESS_ACTION = "/tools/saveSubmittedAddress/";

	public static final String CONTRACT_NUMBER_PARAMETER = "cnno";

	public static final String PROFILE_ID_PARAMETER = "profileId";

	public static final String AS_OF_DATE_PARAMETER = "asOfDate";

	public static final String SOURCE_PARAMETER = "source";

	public static final String EDIT_MODE = "editMode";

	public static final String PARTICIPANT_ID_PARAMETER = "prtId";

	public static final String EMPLOYER_DESIGNATED_ID_PARAMETER = "prtIdNum";

	public static final String SUBMISSION_ID_PARAMETER = "subId";

	public static final String SEQUENCE_NUMBER_PARAMETER = "seqNum";

	// validation context
	public static final String VALIDATION_CONTEXT = "validationContext";

	//manage contacts
	public static final String LAST_VISITED_MANAGE_CONTACTS_PAGE = "lastVisitedManageContactsPage";

	// manage users
	public static final String LAST_VISITED_MANAGE_USERS_PAGE = "lastVisitedManageUsersPage";

	public static final String LAST_EXTERNAL_USER_WITH_PAYROLL_EMAIL_PERMISSION = "lastExternalUserWithPAEM";

	public static final String LAST_TPA_USER_WITH_PAYROLL_EMAIL_PERMISSION = "lastTPAUserWithPAEM";

	public static final String SHOW_PAYROLL_EMAIL_PERMISSION = "showPayrollEmail";

	// Delete last payroll email user
	public static final String DELETE_PAYROLL_EMAIL_MESSAGE = "deletePayrollEmailMessage";

	// Manage last payroll email users
	public static final int MIN_NO_OF_EXT_USERS_WITH_PAYROLL_EMAIL_PERMISSION = 1;

	public static final int MIN_NO_OF_TPA_USERS_WITH_PAYROLL_EMAIL_PERMISSION = 1;

	// add/edit user
	public static final String ADD_EDIT_USER_CONTRACT_LIST = "contractList";

	public static final String ADD_EDIT_USER_ROLE_LIST = "roleList";

	public static final String ADD_EDIT_USER_TPA_FIRM_LIST = "firmList";

	public static final String PROFILE_ACCESS_LEVEL = "profileAccessLevel";

	public static final String INTERNAL_ACCESS_LEVEL = "internalAccessLevel";

	public static final String EZK_ACCESS_LEVEL = "ezkAccessLevel";

	public static final String BD_ACCESS_LEVEL = "bdAccessLevel";

	public static final String RVP_NAMES = "rvpNames";

	public static final String EXTERNAL_USER_MANAGER_STRING = "PSEUM";

	public static final String PLAN_ADMIN_STRING = "PA";

	public static final String SHOW_EDIT_EXT_USER_BUTTON = "showEditExtUser";

	public static final String MESSAGE_CENTER_LEFT_MC_FROM_GLOBAL_CONTEXT = "mcLeftMCFromGlobalContext";

	public static final String MESSAGE_SELECT_CONTRACT = "mcSelectContract";

	public static final String CONTRACT_MESSAGE_ACCESSIBLE_TEMPLATES = "mcContractAccessibleTemplates";

	public static final String TPA_FIRM_MESSAGE_ACCESSIBLE_TEMPLATES = "mcTpaFirmAccessibleTemplates";

	public static final String MESSAGE_CENTER_CAR_VIEW_FORM = "carViewForm"; //this name obviously matches the struts config name

	// edit tpa firm permissions
	public static final String SHOW_EDIT_TPA_FIRM = "showEditTpaFirm";

	public static final String SHOW_TPA_PERMISSIONS_ONLY = "showTpaPermissionsOnly";


	public static final String ENROLLED_RESULT = "E";

	// enrollment state values

	// reset password
	public static final String TEMP_PASSWORD = "tempPassword";

	// arrows
	public static final String BLACK_RIGHT_ARROW_IMAGE_URL = "/assets/unmanaged/images/arrow_triangle_black.gif";

	public static final String BLACK_LEFT_ARROW_IMAGE_URL = "/assets/unmanaged/images/arrow_triangle_black_backward.gif";

	// icons
	public static final String WARNING_ICON = "<img src=\"/assets/unmanaged/images/warning2.gif\" alt=\"Warning\"/>";

	public static final String ERROR_ICON = "<img src=\"/assets/unmanaged/images/error.gif\" alt=\"Error\"/>";

	public static final String WARNING_ICON_NO_TITLE = "<img src=\"/assets/unmanaged/images/warning2.gif\" />";

	public static final String ERROR_ICON_NO_TITLE = "<img src=\"/assets/unmanaged/images/error.gif\" />";

	// lock key
	public static final String LOCK_MANAGER = "lockManager";

	// withdrawals
	public static final String WITHDRAWAL_REQUEST_CONTRACTID = "contractId";

	public static final String WITHDRAWAL_REQUEST_PROFILEID = "profileId";

	public static final String WITHDRAWAL_REQUEST_SUBMISSIONID = "submissionId";

	public static final String WITHDRAWAL_ACTION_FORM = "withdrawalForm"; // this constant
	// matches the name
	// of the action
	// form in the
	// struts config
	// file.

	public static final String WITHDRAWAL_VIEW_PAGE = "viewItem";

	public static final String WITHDRAWAL_EDIT_PAGE = "editItem";

	public static final String WITHDRAWAL_TOOLS_LINK_ACCESS = "withdrawalToolsLinkAccess";

	public static final String WITHDRAWAL_SEARCH_PAGE_BOOKMARK_KEY = "searchPageBookmark";

	// Create Withdrawal request on the PSW page
	public static final String LINKTYPE_CREATE_PSW = "CP";

	// List page link to the request page
	public static final String LINKTYPE_LIST_PSW = "LP";

	// Create withdrawal on the TPA List page
	public static final String LINKTYPE_CREATE_TPA = "CT";

	// List withdrawal Header on the TPA List page
	public static final String LINKTYPE_LIST_TPA = "LT";

	// Create Loan request for PSW users
	public static final String LINKTYPE_CREATE_LOAN_PSW = "CLP";

	// Create Loan on the TPA List page
	public static final String LINKTYPE_CREATE_LOAN_TPA = "CLT";

	//name space binding variable for MCC Link
	public static final String ICC_AVAILABILITY_DATE = "fcc.availability.date";

	// market timing pdfs
	public static final String EARLY_REDEMPTION_BROKER_USPDF_URL = "/assets/pdfs/I7484_early_redemption_hybrid.pdf";

	public static final String EARLY_REDEMPTION_HYBRID_USPDF_URL = "/assets/pdfs/I7484_early_redemption_hybrid.pdf";

	public static final String EARLY_REDEMPTION_RETAIL_USPDF_URL = "/assets/pdfs/I7484_early_redemption_fee_series.pdf";

	public static final String EARLY_REDEMPTION_VENTURE_USPDF_URL = "/assets/pdfs/I7484_early_redemption_venture_series.pdf";

	public static final String EARLY_REDEMPTION_BROKER_NYPDF_URL = "/assets/pdfs/I7583NY_early_redemption_broker.pdf";

	public static final String EARLY_REDEMPTION_HYBRID_NYPDF_URL = "/assets/pdfs/I7583NY_early_redemption_hybrid.pdf";

	public static final String EARLY_REDEMPTION_RETAIL_NYPDF_URL = "/assets/pdfs/I7583NY_early_redemption_series.pdf";

	public static final String EARLY_REDEMPTION_VENTURE_NYPDF_URL = "/assets/pdfs/I7583NY_early_redemption_venture_series.pdf";

	public static final String EARLY_REDEMPTION_HYBRIDNML_USPDF_URL = "/assets/pdfs/I7781_early_redemption_fee_hybridNML.pdf";

	public static final String EARLY_REDEMPTION_RETAILNML_USPDF_URL = "/assets/pdfs/I7781_early_redemption_fee_retailNML.pdf";

	public static final String EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL = "/assets/pdfs/I7782NY_early_redemption_fee_hybridNML.pdf";

	public static final String EARLY_REDEMPTION_RETAILNML_NYPDF_URL = "/assets/pdfs/I7782NY_early_redemption_fee_retailNML.pdf";


	// Quarterly Investment Guide page path and pdfs
	public static final String QIG_PATH = "/do/resources/quarterlyInvestmentGuide/";

	public static final String QIG_RETAIL_URL = "/assets/pdfs/PS4572-RT.pdf";

	public static final String QIG_VENTURE_URL = "/assets/pdfs/PS4572-V.pdf";

	public static final String QIG_NML_URL = "/assets/pdfs/PS4572-NML.pdf";

	public static final String QIG_BD_URL = "/assets/pdfs/PS4572-BD.pdf";

	public static final String QIG_RETAIL_NY_URL = "/assets/pdfs/PS4573-R-NY.pdf";

	public static final String QIG_VENTURE_NY_URL = "/assets/pdfs/PS4573-V-NY.pdf";

	public static final String QIG_NML_NY_URL = "/assets/pdfs/PS4573-NML-NY.pdf";

	public static final String QIG_BD_NY_URL = "/assets/pdfs/PS4573-BD-NY.pdf";

	public static final String QIG_BD_HYBRID_R_URL = "/assets/pdfs/PS4572_RBD.pdf";

	public static final String QIG_BD_HYBRID_R_NML_URL = "/assets/pdfs/PS4572_RBD.pdf";

	public static final String QIG_BD_HYBRID_V_URL = "/assets/pdfs/PS4572_VBD.pdf";

	public static final String QIG_BD_HYBRID_V_NML_URL = "/assets/pdfs/PS4572_VBD.pdf";

	public static final String QIG_BD_HYBRID_NY_R_URL = "/assets/pdfs/PS4573_RBD_NY.pdf";

	public static final String QIG_BD_HYBRID_NY_R_NML_URL = "/assets/pdfs/PS4573_RBD_NY.pdf";

	public static final String QIG_BD_HYBRID_NY_V_URL = "/assets/pdfs/PS4573_VBD_NY.pdf";

	public static final String QIG_BD_HYBRID_NY_V_NML_URL = "/assets/pdfs/PS4573_VBD_NY.pdf";

	public static final String QIG_VENTURE_BD_PRODUCT_ID = "ARABD";

	public static final String QIG_VENTURE_BD_PRODUCT_NY_ID = "ARABDY";

	public static final String QIG_BASE_URL = "/assets/pdfs/PS4572";

	public static final String QIG_BASE_NY_URL = "/assets/pdfs/PS4573";

	public static final String PDF_SUFFIX = ".pdf";

	public static final String NML = "NML";
	public static final String UNDERSCORE = "_";

	public static final String NY_LOCATION = "NY";

	public static final String CLASS_PREFIX = "CL";

	public static final int CLASS_LENGTH = 2;

	public static final String ILOANS_STATUS_CODE_DENIED = "DE";

	public static final String ILOANS_STATUS_CODE_APPROVED = "AP";

	public static final String ILOANS_STATUS_CODE_REVIEW = "RE";

	public static final String ILOANS_STATUS_CODE_PENDING = "PE";

	public static final String ILOANS_STATUS_TEXT_DENIED = "Denied";

	public static final String ILOANS_STATUS_TEXT_APPROVED = "Approved";

	public static final String ILOANS_STATUS_TEXT_REVIEW = "Outstanding";

	public static final String ILOANS_STATUS_TEXT_PENDING = "Pending";

	public static final String DI_DURATION_24_MONTH = "24";

	public static final String DI_DURATION_6_MONTH = "6";

	// auto enrollment service feature
	public static final String NEXT_PLAN_ENTRY_DATES = "nextPlanEntryDates";

	// profile constants
	public static final String USER_PROFILE_YES = "Yes";

	public static final String USER_PROFILE_NO = "No";

	// security role conversion constants
	public static final String SEARCH_CONTRACT_PAGE = "searchContract";

	public static final String MANAGE_USER_PAGE = "manageUser";

	public static final String WITHDRAWAL_INITIAL_MESSAGES_KEY = "withdrawalInitialMessages";

	// Participant pages constants
	public static final String INTERNET = "Internet";

	public static final String AUTO_ENROLL = "Auto";

	public static final String WAS_AUTO_ENROLL = "Was Auto";

	public static final String NA = "N/A";

	public static final String SYW_NA = "n/a";
	
	public static final String NOT_ENTERED = "Not Entered";

	public static final String AUTO_DEFAULTED = "Auto Defaulted";

	public static final String ELIGIBLE_TO_DEFER = "Yes";

	public static final String SIGNUP_METHOD = "signupMethod";
	
	//New Constants 
	
	public static final String USER_PROFILE	= "userprofile";
	
	public static final String CALCULATE_BUTTON= "Calculate";
	
	public static final String CALCULATE= "calculate";
	
	public static final String CLEAR = "clear";
	
	public static final String HOME = "home";
	
	public final static String ERROR_CALCADHOCROR_START_DATE_INVALID = "invalid start date";
	
	public final static String ERROR_CALCADHOCROR_START_DATE_REQUIRED = "start date required";
	
	public final static String ERROR_CALCADHOCROR_END_DATE_INVALID = "invalid end date";
	
	public final static String ERROR_CALCADHOCROR_END_DATE_REQUIRED = "end date required";
	
	public final static String ERROR_CALCADHOCROR_END_DATE_LATER_THEN_TODAY = "end date later then today";
	
	public final static String ERROR_CALCADHOCROR_START_DATE_LATER_THEN_END_DATE = "start date later then end date";
	
	public final static String ERROR_CALCADHOCROR_START_DATE_EQUAL_END_DATE_AND_TODAY = "start date equal end date and today";
	
	public final static String ERROR_CALCADHOCROR_TIME_PERIOD_FROM_TODAY_REQUIRED = "time period from today required";
	
	public final static String ERROR_CALCADHOCROR_TIME_PERIOD_FROM_TODAY_INVALID = " time period from today invalid";
	
	public final static String ERROR_CALCADHOCROR_MF_ROR_FOR_ONE_DAY = "ror for one day";
	
	public final static String ERROR_CALCADHOCROR_MF_CLOSING_BALANCE_THREE_DOLLARS = " closing balance three dollars";
	
	public final static String ERROR_CALCADHOCROR_MF_GENERAL_ERROR = " general error";
	
	public final static String ERROR_CALCADHOCROR_MF_ROR_FOR_ONE_DAY_WITH_CASH_FLOW_AT_END = "ror for one day with cashflow at end";
	
	public final static String ERROR_CALCADHOCROR_MF_CONTRACT_OR_PARTICIPANT_NOT_ACTIVE = "contract or participant not active";
	
	public final static String ERROR_ACCOUNT_BALANCE_OUTSIDE_SERVICE_HOURS = "account balence outside service hours";
	
	public final static String SYSTEM_UNAVAILABLE ="system unavailable";
	
	public static final String CLEAR_BUTTON = "Clear";
	
	public static final String HOME_BUTTON= "Home";
	
	public static final String RATE_OF_RETURN_CALCULATOR_FORM  = "rateofReturnCalculatorForm";
	
	public final static String CALCADHOCROR_START_DATE = "CALCADHOCROR_START_DATE";
	
	public final static String CALCADHOCROR_END_DATE = "CALCADHOCROR_END_DATE";
	
	public final static String CALCADHOCROR_TIMEPERIODFROMTODAY = "CALCADHOCROR_TIMEPERIODFROMTODAY";	
	//auto contribution increase attributes

	public static final String aciDefaultAnniversaryDate = "ADT";
	public static final String aciDefaultAnnualIncreaseByPercent = "AIP";
	public static final String aciDefaultAnnualIncreaseByAmount = "AID";
	public static final String aciDefaultDeferralLimitByPercent = "DLP";
	public static final String aciDefaultDeferralLimitByAmount = "DLD";
	public static final String aciIncreaseNewEnrollesAnniversary = "INF";

	public static final String AUTO_CONTRIBUTION = "ACI";
	public static final String MANAGING_DEFERRALS = "MD";
	public static final String ACI_MANUAL_SIGNUP_METHOD = "Manual";
	public static final String ACI_AUTO_SIGNUP_METHOD = "Auto";
	public static final String ACI_SIGNUP_METHOD = "aciSignUpMethod";

	public static final String medDeferralType = "DFT";

	public static final String CSF_YES_CODE="Y";
	public static final String CSF_NO_CODE="N";
	public static final String ACI_DEFAULTED_TO_YES="DY";
	public static final String DEFERRAL_TYPE_PERCENT="%";
	public static final String DEFERRAL_TYPE_DOLLAR="$";
	public static final String PERCENT="%";
	public static final String DOLLAR="$";
	public static final String DEFERRAL_TYPE_EITHER="E";
	public static final String ACI_AUTO="A";
	public static final String ACI_SIGNUP="S";
	public static final String ACI_APPLIES_TO_ALL="A";
	public static final String ACI_APPLIES_TO_NEW="N";
	public static final String ACI_NEXT="N";
	public static final String ACI_FOLLOWING="F";

	public static final String PLAN_ENTRY_FREQUENCY_MONTHLY = "M";
	public static final String PLAN_ENTRY_FREQUENCY_QUARTERLY = "Q";
	public static final String PLAN_ENTRY_FREQUENCY_SEMI_ANNUALLY = "S";
	public static final String PLAN_ENTRY_FREQUENCY_IMMEDIATE = "I";
	public static final String PLAN_ENTRY_FREQUENCY_ANNUALLY = "A";
	public static final String PLAN_ENTRY_FREQUENCY_OTHER = "O";

	public static final String INTERNAL_USER_ID_TYPE = "UPI";
	public static final String EXTERNAL_USER_ID_TYPE = "UPE";
	public static final String PS_SOURCE_CHANNEL_CODE = "PS";
	public static final String PS_SOURCE_FUNCTION_CODE_EES = "EES";

	/**
	 * Gateway phase 1 starts
	 * Participant BenefitBase Information Attributes
	 */

	public static final String AWAITING_DEPOSIT = "Awaiting deposit";

	public static final String DEFAULT_DATE = "9999-12-31";

	public static final String DEFAULT_ANNIVERSARY_DATE = "8888";

	public static final String ACCOUNT_DETAILS = "accountDetails";

	public static final String BENEFIT_DETAILS = "benefitDetails";

	public static final String BENEFIT_BASE_DETAILS = "details";

	public static final String DISPLAY_DATES = "displayDates";

	public static final String TRUE = "true";
	
	public static final String WILSHIRE_3_21_ADVISER_SERVICE = "Wilshire 3(21) Adviser Service";

	public static final String  PARTICIPANT_ID = "participantId";

	public static final String PROFILE_ID = "profileId";

	public static final String TECHNICAL_DIFFICULTIES = "technicalDifficulties";

	//G.I.F.L 1C start
	public static final String OUT_OF_SERVICE_HOURS = "outofservicehours";
	//G.I.F.L 1C end

	public static final String ENROLLMENT_METHOD_PIECHART = "enrollmentMethodChart";

	public static final String PARTICIPATION_RATE_PIECHART = "participationRateChart";

	public static final class EnrollmentMethodPieChart {
		public static final String AUTO = "#cc3300";

		public static final String DEFAULT = "#cc6600";

		public static final String PAPER = "#2a5400";

		public static final String INTERNET = "#06386b";

		public static final String COLOR_WEDGE_LABEL = "#FFFFFF";
	}

	public static final class ParticipationRatePieChart {
		public static final String PARTICIPANTS = "#9a3232";

		public static final String OPT_OUT = "#ff6600";

		public static final String PENDING_ELIGIBILITY = "#edc558";

		public static final String PENDING_ENROLLMENT = "#dfc4d7";

		public static final String NOT_ELIGIBLE = "#b2dd92";

		public static final String COLOR_WEDGE_LABEL = "#FFFFFF";
	}

	public static final String PERCENT_DEFERRALS_BARCHART = "percentDeferralsChart";
	public static final String DOLLAR_DEFERRALS_BARCHART = "dollarDeferralsChart";
	public static final String ACI_PARTICIPATION_PIECHART = "aciParticipationChart";
	public static final String UNKNOWN = "unknown";
	public static final String REQUEST_TYPE_LOAN = "LOAN";
	public static final String LOAN_REQUEST_STATUS_DRAFT = LoanStateEnum.DRAFT.getStatusCode();
	public static final String LOAN_REQUEST_STATUS_PENDINGREVIEW = LoanStateEnum.PENDING_REVIEW.getStatusCode();
	public static final String LOAN_REQUEST_STATUS_PENDINGAPPROVAL = LoanStateEnum.PENDING_APPROVAL.getStatusCode();

	public static final String DIRECT_URL_ATTR = "directURL";
	
	public static final String CONTRACT_MESSAGE_COUNTS = "contractMessageCounts";
	public static final String USER_MESSAGE_COUNTS = "userMessageCounts";

	//GIFL 1C
	public static final String GIFL_SELECTED = "Selected";
	public static final String GIFL_DESELECTED = "Deselected";
	public static final String GIFL_NEVER_SELECTED = "Never Selected";
	public static final String GIFL_DESELECTED_DATE = "9999-12-31";

	//	GIFL-2A -
	public static final String BB_BATCH_STATUS = "BBBatchDateLessThenETL";

	// Naming rules
	public static final String FIRST_NAME_LAST_NAME_RE = "^[0-9a-zA-Z\\-\\.\\']{1}[0-9a-zA-Z \\-\\.\\']{0,29}$";

	//tools menu online loans and withdrawal changes.
	public static final String REQUEST_TYPE = "requestType";

	// for FD
	//public static final String DISCLOSURE_ROADMAP_PARAMETER ="disclosureRoadMapContent";

	public static final String FILTER_CRITERIA_CENSUS_TABS = "filterCriteriaCensusTabs";

	public static final String PRODUCT_RA457="RA457";

	// Warnings Key
	public static final String PS_WARNINGS = "psWarnings";

	// Warnings Key
	public static final String PS_INFO_MESSAGES = "psInformation";

	// for TPA BOB
	public static final String DEFINED_BENEFIT_ID = "DB";

	public static final String DB_SESSION_ID = "tpaDbSessionID";

	public static final String TPA_ROLE = "TPA";

	public static final String TPA_BOB_PAGE_ID = "/tpabob/tpaBlockOfBusiness.jsp";

	public static final String TPA_BOB_PAGE_ID_PENDING_TAB = "/tpabob/tpaBlockOfBusinessPending.jsp";

	public static final String TPA_BOB_PAGE_ID_DISCONTINUED_TAB = "/tpabob/tpaBlockOfBusinessDiscontinued.jsp";

	public static final String TPA_CSV_DOWNLOAD_PAGE = "csvDownloadPage";

	public static final String TPA_BOB_INFO_MSG = "tpaBobInfoMsg";

	// This is the request parameter name of the parameter which will have the tpa userID.
	public static final String TPA_USERID_REQ_PARAM = "tpauserid";

	// TPA - BOB Tab ID's
	public static final String TPA_ACTIVE_TAB_ID = "activeTab";
	public static final String TPA_PENDING_TAB_ID = "pendingTab";
	public static final String TPA_DISCONTINUED_TAB_ID = "discontinuedTab";

	// TPA - BOB Tab Titles to be displayed in JSP page.
	public static final String TPA_ACTIVE_TAB_TITLE = "Active";
	public static final String TPA_PENDING_TAB_TITLE = "Pending";
	public static final String TPA_DISCONTINUED_TAB_TITLE = "Discontinued";

	// TPA - BOB Tab URL's.
	public static final String TPA_ACTIVE_TAB_URL = "/tpabob/tpaBlockOfBusiness/";
	public static final String TPA_PENDING_TAB_URL = "/tpabob/tpaBlockOfBusinessPending/";
	public static final String TPA_DISCONTINUED_TAB_URL = "/tpabob/tpaBlockOfBusinessDiscontinued/";

	public static final String TPA_BOB_TABS_LIST = "tpaBobTabsList";

	// TPA - BOB Tab values that will be sent to DAO.
	public static String ACTIVE_TAB_VAL = "AC";

	public static String PENDING_TAB_VAL = "PN";

	public static String DISCONTINUED_TAB_VAL = "DI";

	// TPA - BOB Filter title
	public static String FILTER_CONTRACT_NAME_TITLE = "Contract Name";

	public static String FILTER_CONTRACT_NUNBER_TITLE = "Contract Number";

	public static String FILTER_FINANCIAL_REP_OR_ORG_NAME_TITLE = "Financial Rep Name Or Organization Name";

	public static String FILTER_CAR_NAME_TITLE = "Car Name";

	// TPA - BOB Column title
	public static final String COL_CONTRACT_NAME_TITLE = "Contract Name";

	public static final String COL_CONTRACT_NUMBER_TITLE = "Contract Number";

	public static final String COL_CONTRACT_PLAN_YEAR_END_TITLE = "Plan Year End (mm/dd)";

	public static final String COL_EXPECTED_FIRST_YEAR_ASSETS_TITLE = "Expected First Year Assets ($)";

	public static final String COL_EXPECTED_NUM_OF_LIVES_TITLE = "Expected Number Of Lives";

	public static final String COL_NUM_OF_LIVES_TITLE = "Lives";

	public static final String COL_TOTAL_ASSETS_TITLE = "Total Assets ($)";

	public static final String COL_TRANSFERRED_OUT_ASSETS_TITLE = "Transferred Assets Prior To Charges ($)";

	public static final String COL_DISCONTINUANCE_DATE_TITLE = "Discontinuance Date";

	public static final String COL_FINANCIAL_REP_TITLE = "Financial Rep";

	public static final String COL_CAR_NAME_TITLE = "Client Acct Rep";

	// TPA - BOB Column titles for Extra CSV columns..

	public static final String COL_TPA_FIRM_TITLE = "TPA Firm ID";

	public static final String COL_CONTRACT_STATUS_TITLE = "Contract Status";

	public static final String COL_CONTRACT_EFFECTIVE_TITLE = "Contract Effective Date";

	public static final String COL_ALLOCATED_ASSETS_TITLE = "Allocated Assets";

	public static final String COL_LOAN_ASSETS_TITLE = "Loan Assets";

	public static final String COL_CASH_ACCOUNT_BALANCE_TITLE = "Cash Account Balance";

	public static final String COL_PBA_ASSETS_TITLE = "PBA Assets";

	public static final String COL_EZ_START_TITLE = "EZstart";

	public static final String COL_EZ_INCREASE_TITLE = "Scheduled Deferral Increase";

	public static final String COL_DIRECT_MAIL_TITLE = "Direct Mail";

	public static final String COL_GIFL_TITLE = "Guaranteed Income for Life";

	public static final String COL_GIFL_FEATURE_TITLE = "Guaranteed Income Feature";

	public static final String COL_GIFL_FEATURE_TITLE_SC = "Guaranteed Income feature";
	
	public static final String COL_MANAGED_ACCOUNT_TITLE = "Managed Accounts";
	
	public static final String COL_SEND_SERVICE_TITLE = "SEND Service";
	
	public static final String COL_JOHNHANCOCK_PASSIVE_TRUSTEE_CODE_TITLE = "John Hancock Trustee Service";

	public static final String COL_PAM_TITLE = "Participant Address Management";

	public static final String COL_ONLINE_WITHDRAWALS_TITLE = "Online Withdrawals";
	
	public static final String COL_SYS_WITHDRAWALS_TITLE = "Systematic Withdrawals";

	public static final String COL_VESTING_PERCENTAGE_TITLE = "Vesting Percentage";

	public static final String COL_VESTING_ON_STATEMENTS_TITLE = "Vesting On Statements";

	public static final String COL_PERMITTED_DISPARITY_TITLE = "Permitted Disparity";

	public static final String COL_FSW_TITLE = "Fiduciary Standards Warranty";

	public static final String COL_DIO_TITLE = "Default Investment Option(s)";

	public static final String COL_COW_TITLE = "Contracts On Web";
	
	public static final String COL_ONLINE_BENEFICIARY_DESIGNATION_TILTE = "Online Beneficiary Designation";
	
	public static final String COL_TPA_SIGNING_AUTHORITY_TITLE = "TPA Signing Authority";
	
	public static final String COL_PAYROLL_PATH_TITLE = "Payroll Path";
	
	public static final String COL_PAYROLL_FEEDBACK_SVC_TITLE = "Payroll Feedback";
	
	public static final String COL_PLAN_HIGHLIGHTS_TITLE = "Plan Highlights";
	
	public static final String COL_PLAN_HIGHLIGHTS_REVIEWED_TITLE = "Plan Highlights Reviewed";
	
	public static final String COL_INSTALLMENTS_TITLE = "Installments";

	// TPA - BOB JSP Fields.
	public static final String TPA_CONTRACT_NAME_FIELD = "contractName";

	public static final String TPA_USER_INFO = "tpaUserInfo";

	// Sort fields
	public static final String TPA_CONTRACT_NAME_SORT_FIELD = "contractName";

	public static final String TPA_CONTRACT_NUMBER_SORT_FIELD = "contractNumber";
	

	// PAM VALUES
	public static final String PAM_ACT_ID = "ACT";

	public static final String PAM_DSB_ID = "DSB";

	public static final String PAM_RTD_ID = "RTD";

	public static final String PAM_TRM_ID = "TRM";

	public static final String PAM_ACT_VALUE = "Active";

	public static final String PAM_DSB_VALUE = "Disabled";

	public static final String PAM_RTD_VALUE = "Retired";

	public static final String PAM_TRM_VALUE = "Terminated";

	// Edit Contract Addresses
	public static final String STATES_LIST = "states";

	public static final String PERIOD_OF_SERVICE_UNIT_MONTHS="M";
	public static final String PERIOD_OF_SERVICE_UNIT_DAYS="D";
	public static final String MONEY_TYPE_CATEGORY_EE="EE";
	public static final String MONEY_TYPE_CATEGORY_ER="ER";
	public static final String SERVICE_CREDITING_METHOD_UNSPECIFIED="U";
	public static final String SERVICE_CREDITING_METHOD_HOURS_OF_SERVICE="H";
	public static final String SERVICE_CREDITING_METHOD_ELAPSED_TIME_PERIOD="E";
	public static final String ELIGIBILITY_COMP_PERIOD_UNSPECIFIED="U";
	public static final String ELIGIBILITY_COMP_PERIOD_PLAN_YEAR="P";

	public static final String CONTRACT_CONTACT_LEVEL_TYPE_CODE = "CO";

	public static final String FORWARD_TPA_CONTACTS = "tpaContactsTab";

	public static final String FORWARD_PLANSPONSOR_CONTACTS = "planSponsorContacts";

	public static final String FUNDCHECK_URL = "/do/fundCheck";


	//for contibution - formattedDate
	public static final String FORMATTED_DATE = "formattedDate";

	//Different GIFL versions
	public static final String GIFL_VERSION_03 = "G03";
	public static final String GIFL_VERSION_02 = "G02";
	public static final String GIFL_VERSION_01 = "G01";

	//Plan Sponsor - key for Contract Profile page GIFL section content id
	public static final String PSW_CP_GIFL_SECTION = "PSW_CP_GIFL_SECTION";

	//Plan Sponsor - key for Contract Profile Page Foot Note section content id
	public static final String PSW_CP_GIFL_V3_FOOTNOTE = "PSW_CP_GIFL_V3_FOOTNOTE";

	//Plan Sponsor - Benefit Base Foot Note Content ID for GIFL version 3
	public static final String PSW_BB_FOOTNOTE = "PSW_BB_FOOTNOTE";

	//Key to set TradeRestriction object in the request
	public static final String TRADING_RESTRICTION = "TRADING_RESTRICTION";

	public static final String GIFL_VERSION_YES = "Yes";

	//Constants for GIFL related information which will be stored in content db in Forms content type
	public static final String GIFL_RELATED_BOTH = "Both";
	public static final String GIFL_RELATED_NON_GIFL_ONLY= "Non Gifl only";
	public static final String GIFL_RELATED_GIFL_ONLY = "Gifl only";
	
	//Constants for BGA related information which will be stored in content db in Forms content type
	public static final String BGA_RELATED_BOTH = "Both";
	public static final String BGA_NON_BUNDLED_ONLY= "Non Bundled only";
	public static final String BGA_BUNDLED_ONLY = "Bundled only";

	//Plan Sponsor - key for Performance Charting Gifl Intro message
	public static final String PSW_PC_GIFL_MESSAGE = "PSW_PC_GIFL_MESSAGE";

	public static final String GIFL_RISK_CATEGORY_CODE = "GW";

	public static final String FIRST_POINT_OF_CONTACT = "firstPointOfContact";

	//Plan Sponsor - constants for Password Reset Actions.
	public static final String PSW_RESET_PWD_DOB = "birthDate";
	public static final String RESET_PWD_CONFIRMATION = "resetPasswordConfirmation";
	public static final String PROFILEID = "PROFILE_ID";
	public static final String CONTRACT_ID = "CONTRACT_ID";
	public static final String SUSPENDED = "S";
	public static final String PSW_RESET_PWD_EMAIL = "employerProvidedEmailAddress";
	public static final String PSW_RESET_PIN_SUSPEND = "PINSuspend";
	public static final String PSW_RESET_PIN_MAXIMUM_EMAIL = "maxEmail";
	public static final String PSW_RESET_PWD_INVALID_EMPLOYMENT_STATUS = "deceasedEmployee";
	public static final String PSW_RESET_PIN_INCOMPLETE_PARTICIPANT_SETUP = "incompleteParticipantSetup";
	public static final String PSW_RESET_PIN_EMAIL_NO = "no";
	public static final String RESET_PASSWORD_FORWARD = "resetPassword";
	public static final String DISABLED = "disabled";
	public static final String FROMRESET = "fromReset";
	public static final String SHOWCONFIRMATIONTODO = "showConfirmationToDo";
	public static final String SOURCE = "source";
	public static final String EMAIL = "Email";
	public static final String PSW_RESET_PWD_EE = "EE";
	public static final String PSW_RESET_PWD_SPACE = "&nbsp";
	public static final String PSW_RESET_PWD_EDIT = "editEmployeeSnapshot";
	public static final String FALSE = "false";
	public static final String SUCCESS = "Success";
	public static final String ERROR = "Error";
	public static final String PSW_RESET_PWD_ACCESS_STATUS = "accessStatus";//PAR 
	public static final String PSW_RESET_PWD_MAX_REQUESTS = "maxPasswordRequests";//PAR
	public static final String PSW_RESET_PWD_WEB_REGIS_STATUS = "webRegistrationStatus";//PAR


	// first client contact
	public static final String FIELD_FIRST_CLIENT_CONTACT = "firstClientContact";
	public static final String FIELD_FIRST_CLIENT_CONTACT_OTHER_ATTRIBUTE = "firstClientContactOther";
	public static final String FIELD_FIRST_CLIENT_CONTACT_OTHER_TYPE_ATTRIBUTE = "firstClientContactOtherType";
	public static final EnumSet<FirstClientContactOtherAttributeValue> FIRST_CLIENT_CONTACT_OTHER_ATTRIBUTE_VALUES = EnumSet.allOf(FirstClientContactOtherAttributeValue.class);
	public static final EnumSet<FirstClientContactOtherTypeAttributeValue> FIRST_CLIENT_CONTACT_OTHER_TYPE_ATTRIBUTE_VALUES = EnumSet.allOf(FirstClientContactOtherTypeAttributeValue.class);


	public static final String FIRST_CLIENT_CONTACT_FEATURE = "FCC";
	public static final String FIRST_CLIENT_CONTACT_OTHER_ATTRIBUTE = "FCO";
	public static final String FIRST_CLIENT_CONTACT_OTHER_TYPE_ATTRIBUTE = "FCT";

	//Beneficiary page constants.
	public static final String VIEW_BENEFICIARY_PAGE_DEFAULT = "default";
	public static final String BACK_TO_VIEW_PAGE = "view";
	public static final String EDIT_BENEFICIARY_INFORMATION_PAGE = "edit";
	public static final String BENEFICIARY_REPORT = "beneficiaryDesignationData";
	public static final String ACTION ="action";
	public static final String DEFAULT ="default";
	public static final String DECIMAL_PATTERN = "0.00";
	public static final String COMMA_SPACE = ", ";
	public static final String OPEN_ANGLE_BRACKET = "<";
	public static final String CLOSED_ANGLE_BRACKET = ">";

	//Contract investment Option page constants.
	public static final String MONEY_MARKET_FUND_US = "MMR";
	public static final String MONEY_MARKET_FUND_NY = "NMM";
	//TODO:Added new money market fund part of post FFL2017 and in case of adding one more new fund we have to revisit the logic to auto populate.
	public static final String INVESTCO_PREMIER_US_GOVT_MONEY_FUND_US = "IPG";
	public static final String INVESTCO_PREMIER_US_GOVT_MONEY_FUND_NY = "NNQ";
	public static final String SALES_AND_SERVICE_VARIES = "Varies";
	public static final String RATE_TYPE_CL5 = "CL5";
	public static final String RATE_TYPE_CX0 = "CX0";
	public static final String RATE_TYPE_CY1 = "CY1";
	public static final String RATE_TYPE_CY2 = "CY2";
	
	public static final String PERCENT_INT_9_DEC_2_BRACKET =
		"###,###,##0.00'%';(###,###,##0.00'%')";
	
	public enum FirstClientContactFeatureValue {
		CLIENT("CLI", "Client"),
		TPA("TPA", "TPA"),
		BROKER("BRK", "Broker"),
		NON_CLIENT_TRUSTE("NCT", "Non-client Trustee"),
		CLIENT_AND_OTHER("CLO", "Client and other for selected issues"),
		TPA_FIRST_THEN_CLIENT("TFC", "TPA first, then client"),
		BROKER_FIRST_THEN_CLIENT("BFC", "Broker first, then client");

		private static final Map<String,FirstClientContactFeatureValue> lookup
		= new HashMap<String,FirstClientContactFeatureValue>();

		static {
			for(FirstClientContactFeatureValue s : EnumSet.allOf(FirstClientContactFeatureValue.class)) {
				lookup.put(s.getValue(), s);
			}
		}

		private String value;
		private String displayValue;
		private FirstClientContactFeatureValue(String value, String displayValue) {
			this.value = value;
			this.displayValue = displayValue;
		}

		public String toString() {
			return displayValue;
		}

		public String getValue() {
			return value;
		}

		public String getDisplayValue() {
			return displayValue;
		}

		public static FirstClientContactFeatureValue getFirstClientContractFeatureValue(String key) {
			return lookup.get(key);
		}
	}

	public enum FirstClientContactOtherAttributeValue {
		NO_SELECTION("",""),
		TPA("TPA", "TPA"),
		BROKER("BRK", "Broker"),
		NON_CLIENT_TRUSTE("NCT", "Non-client Trustee"),
		SEE_OTHER_COMMENTS("SOC", "See contract comments");

		private static final Map<String,FirstClientContactOtherAttributeValue> lookup
		= new HashMap<String,FirstClientContactOtherAttributeValue>();

		static {
			for(FirstClientContactOtherAttributeValue s : EnumSet.allOf(FirstClientContactOtherAttributeValue.class)) {
				lookup.put(s.getValue(), s);
			}
		}

		private String value;
		private String displayValue;
		private FirstClientContactOtherAttributeValue(String value, String displayValue) {
			this.value = value;
			this.displayValue = displayValue;
		}

		public String toString() {
			return displayValue;
		}

		public String getValue() {
			return value;
		}

		public String getDisplayValue() {
			return displayValue;
		}

		public static FirstClientContactOtherAttributeValue getFirstClientContactOtherAttributeValue(String key) {
			return lookup.get(key);
		}
	}


	public enum FirstClientContactOtherTypeAttributeValue {
		NO_SELECTION("",""),
		LOANS("LN", "Loans"),
		WITHDRAWALS("WD", "Withdrawals"),
		LOANS_AND_WITHDRWALS("LWD", "Loans and Withdrawals"),
		PAYROLL("PAY", "Payroll"),
		OTHER("OTH", "Other, see comments");

		private static final Map<String,FirstClientContactOtherTypeAttributeValue> lookup
		= new HashMap<String,FirstClientContactOtherTypeAttributeValue>();

		static {
			for(FirstClientContactOtherTypeAttributeValue s : EnumSet.allOf(FirstClientContactOtherTypeAttributeValue.class)) {
				lookup.put(s.getValue(), s);
			}
		}

		private String value;
		private String displayValue;
		private FirstClientContactOtherTypeAttributeValue(String value, String displayValue) {
			this.value = value;
			this.displayValue = displayValue;
		}

		public String toString() {
			return displayValue;
		}

		public String getValue() {
			return value;
		}

		public String getDisplayValue() {
			return displayValue;
		}
		
		public static FirstClientContactOtherTypeAttributeValue getFirstClientContactOtherTypeAttributeValue(String key) {
			return lookup.get(key);
		}
	}
	
	public static final String IPS_CANCEL_CONFIRMATION_TEXT = "cancelConfirmationText";
	public static final String IPS_CANCEL_NYSE_NOT_AVAILABLE_TEXT = "cancelNyseNotAvailableText";
	public static final String IPS_SERVICE_DATE_CHANGE_NOT_AVAILABLE_TEXT = "serviceDateChangeNotAvailableText";

	public static final String TXN_NUMBER_KEY = "txnNumber";
	public static final String PARTICIPANT_ID_KEY = "pptId";

	
	public static final String EMPTY_STRING = "";

	
	public static final String REPORT_DATES_FEE_DISCLOSURE="reportDatesFeeDisclosure";
	
	public static final String ESTIMATED_JH_RECORDKEEPING_COST_SUMMARY = "estimatedJhRecordKeepingCostSummary";
	
	public static final String MAX_ALLOWED_RECORDS_CONTROL_REPORTS = "max.allowed.records.control.reports";	
	 //OBDS PHASE 2 Beneficiary Internal Transfer
	public static final String BENEFICIARY_TRANSFER_COMMIT_EVERYXROWS = "beneficiary.transfer.commit.everyxrows";
	public static final String CONFIRM = "confirm";
	public static final String BENEFICIARY_TRANSFER_DEFAULT = "default";
	public static final String PRINT = "print";
	public static final String CANCEL = "cancel";
	public static final String CONTINUE = "continue";
	
	//OBDS PHASE 2 Beneficiary download report constants.
	public static final int BENEFICIAY_DESIGNATION_LEGAL_TEXT_CONTENT_KEY = 77175;
	public static final String US_CONTEXT_TEXT = "TEXT";
	public static final String NY_CONTEXT_TEXT = "NY_TEXT";

	// Investment related cost
	public static final String SELECTED_VIEW = "selectedView";
	public static final String SELECTED = "selected";
	public static final String AVAILABLE = "available";
	public static final String SHOW_LINK = "showLink";
	

	public static final String ON = "on";

	public static final String CON_NUMBER = "conNumber";
	
	public static final String PRINT_REPORT_ID = "RPT";
	public static final String PRINT_FOR_PARTICIPANT_ID = "PPT";
	
	public static final String WITHDRAWAL_CONFIRMATION_PAGE_ID= "/withdrawal/view/confirmation.jsp"; 
	public static final String VIEW_WITHDRAWAL_PAGE_ID = "/withdrawal/view/viewRequest.jsp" ;
	public static final String VIEW_ONLINE_LOANS_PAGE_ID = "/onlineloans/view.jsp";
	public static final String ONLINE_LOANS_CONFIRMATION_PAGE_ID = "/onlineloans/confirmation.jsp";
	
	//Standard FeeSchedule Related constants
	public static final String TPA_FEETYPE_VALUE = "amountValue";
	public static final String TPA_FEE_TYPE = "CustomFeeType";
	public static final String TPA_FEE_TYPE_AND_VALUE = "CustomFeeTypeAndValue";
	public static final String MESSAGE_WHEN_NO_FEE_SCHEDULE_EXISTS = "noFeeScheduleExists";
	public static final String MESSAGE_WHEN_NO_TPA_MANAGER_EXISTS = "noTPAManagerExists";
	
	
	public static final String JOHN_HANCOCK_REPRESENTATIVE = "John Hancock Representative";
	public static final String ADMINISTRATION  = "Administration";
	public static final String SYSTEM_USER_PROFILE_ID  = "8";
	
	public static final String EDIT_404a5_NOTICE_INFO_PAGE = "edit404a5NoticeInfo";
	public static final String CONFIRM_404a5_NOTICE_INFO_PAGE = "confirm404a5NoticeInfo";
	public static final String VIEW_404a5_NOTICE_INFO_PAGE = "view404a5NoticeInfo";
	
	public static final String EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE = "editTpaCustomizedContractFee";
	public static final String CONFIRM_TPA_CUSTOMIZE_CONTRACT_PAGE = "confirmTpaCustomizedContractFee";
	public static final String VIEW_TPA_CUSTOMIZE_CONTRACT_PAGE = "viewTpaCustomizedContractFee";
	public static final String VIEW_NOTICE_PLAN_DATA ="viewNoticePlanData";
	public static final String VIEW_TPA_CUSTOMIZE_CONTRACT_CHANGE_HISTORY_PAGE = "viewTpaCustomizedContractFeeChangeHistory";
	
	//Plan data
	public static final String VIEW_PLAN_DATA_TPA_CUSTOMIZE_CONTRACT_PAGE = "viewPlanDataTpaCustomizedContractFee";
	
	public static final String SELECTED_TPA_CONTRACT_IN_SESSION = "selectedTPAContractInSession";
	public static final String SELECTED_CUSTOMIZE_CONTRACT = "selectedCustomizeContract";
	public static final String SELECTED_CUSTOMIZE_CONTRACT_NAME = "selectedCustomizeContractName";
	public static final String SELECTED_STANDARDIZE_TPA_FIRM_ID = "selectedTpaFirmId";
	public static final String REDIRECTING_TO_VIEW_FEE_SCHEDULE_PAGE = "redirectingToViewPage";
	public static final String FORWARD_RESET_TO_STANDARD_SCHEDULE_PAGE = "resetToStandardSchedule";
	public static final String RESET_CONTRACT_CUSTOM_FEE_SCHEDULE = "isResetTostandardSchedule";
	
	public static final String FUND_FAMILY_CATEGORIZATION_SVF = "SVF";
	public static final String FUND_CODE_MSV = "MSV";
	public static final String FUND_CODE_NMY = "NMY";
	public static final String FUND_TYPE_GARUNTEED = "GA";
	
	public static final String TPA_FIRM_ID_REQUEST_PARAM = "tpaFirmId";
	
	//Manage Contacts 
	public static final String VIEW_PAGE= "viewPage";
	public static final String EDIT_PAGE= "editPage";
	public static final String DELETE_PAGE= "deletePage";
	public static final String MANAGE_PWD_PAGE= "managePasswordPage";
	public static final String SUSPEND_PAGE= "suspendPage";
	public static final String UNSUSPEND_PAGE= "unsuspendPage";
	public static final String EDIT_PROFILE_PAGE= "editProfilePage";
	
	public static final String VIEW_TPA_PAGE= "viewTpaPage";
	public static final String EDIT_TPA_PAGE= "editTpaPage";
	public static final String DELETE_TPA_PAGE= "deleteTpaPage";
	public static final String MANAGE_PWD_TPA_PAGE= "managePasswordTpaPage";
	public static final String SUSPEND_TPA_PAGE= "suspendTpaPage";
	public static final String UNSUSPEND_TPA_PAGE= "unsuspendTpaPage";
	
	public static final String VIEW_MANAGE_TPA_PAGE= "viewManageTpaPage";
	public static final String EDIT_MANAGE_TPA_PAGE= "editManageTpaPage";
	public static final String DELETE_MANAGE_TPA_PAGE= "deleteManageTpaPage";
	public static final String MANAGE_PWD_MANAGE_TPA_PAGE= "managePasswordPage";
	public static final String SUSPEND_MANAGE_TPA_PAGE= "suspendManageTpaPage";
	public static final String UNSUSPEND_MANAGE_TPA_PAGE= "unsuspendManageTpaPage";
	
	public static final String ERISA_USA_ONLINE_URL = "usa.ErisaOnlineUrl";
	public static final String ERISA_NY_ONLINE_URL = "ny.ErisaOnlineUrl";
	public static final String OMNITURE_WEB_ANALYTICS_CAPTURE_DESTINATION = "webAnalyticsDestination";
	public static final String USER_NAME = "userName";
	public static final String PATH = "path";
	public static final String FROM_TPA_CONTACTS_TAB = "fromTPAContactsTab";
	
	public static final String MANAGE_USER_HELPER_PROPERTIES_FILE_NAME = "./manage_user_helper.properties";
	public static final String TPA_USER_EDIT_PERFORMANCE_IMPROVED_FLAG = "tpa.user.edit.performance.improved.flag";
	public static final String TPA_USER_EDIT_PERFORMANCE_IMPROVED_FLAG_ENABLED = "Y";
	

	
	public static final String CUSTOM_DOCUMENT_LOCK = "CustomDocumentLock";
	
	//Notice Manager Constant
	public static final String NMC_PDF_READER=".pdf";	
	public static final String NMC_CAPS_PDF_READER=".PDF";
	public static final String PLAN_HIGHLIGHT_NOTICE = "planHighlights";
    public static final String PLAN_ICC_DOCUMENT = "icc";
    public static final String PLAN_INVESTMENT_DOCUMENT = "planInvestment";
    public static final String PLAN_HIGHLIGHT = "planhighlight";
    public static final String PLAN_INVESTMENT = "planinvestment";
    public static final String PROPERTIES_FILE_NAME = "./noticeManager.properties";
	//Notice Manager Terms and conditions Constant
	public static final String LOGGED = "TERMS_AND_CONDITIONS_PAGE";
	public static final String UPLOAD_PAGE_ACTION = "uploadandShare";
	public static final String ADD_PAGE_ACTION = "addPage";
	public static final String BUILD_PAGE_ACTION = "build";
	public static final String ACTION_PERFORMED = "actionPerformed";
	public static final String ADD = "Add";
	public static final String VISITED = "VISITED";
	
	//Notice Manager NoticeManagerOrderStatusUpdateAction Constant
	
	public static final String ORDER_NOT_COMPLETED_STATUS = "NotCompleted";
	public static final String ORDER_INPROGRESS_STATUS = "InProgress";
	public static final String ORDER_CANCELLED_STATUS = "Cancelled";
	public static final String ORDER_COMPLETED_STATUS = "Completed";
	public static final boolean DUMP_PARAMETERS = true;
	public  static final String STATUS = "status";
	public  static final String TRACKING_NUMBER = "trackingNumber";
	public  static final String ORDER_NUMBER = "orderNumber";
	public  static final String COLOR_IND = "colorInd";
	public  static final String PAGE_COUNT = "pageCount";
	public  static final String PARTICIPANT_COUNT = "participantCount";
	public  static final String VIP = "VIP";
	public  static final String MAIL_DATE = "mailDate";
	public  static final String TOTAL_COST = "totalCost";
	public  static final String  ORDER_STAPLED_IND = "orderStapledInd";
	public  static final String  LARGE_ENVELOPE_IND = "largeEnvelopeInd";
	public  static final String  BULK_ORDER_IND = "bulkOrderInd";
	public  static final String  ORDER_SEALED_IND = "orderSealedInd";
	
	//Notice Manager Upload and Share Page
	public static final String PDF_GENERATED = "pdfGenerated";
	public static final String PDF_NOT_GENERATED = "pdfNotGenerated";
	public static final String EMPTY_LAYOUT_ID = "/registration/authentication.jsp";
	public static final String DEFAULT_SORT_FIELD = PlanDocumentReportData.SORT_FIELD_DISPLAY_SORT_NUMBER;
	public static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	public static final String CUSTOM_SORT = "customSort";
	public static final String DOCUMENT_ID = "documentId";
	public static final String CUSTOM_SORT_ARROW = "up";
	public static final String DOCUMENT_FILE_NAME = "documentFileName";
	public static final String PLAN_VO_LIST_LENGTH = "planVoListLenght";
	public static final String DOCUMENT_VISITED ="0";
	public static final String VALIDATION_ERROR = "error";
	public static final String VALIDATION_SUCCESS = "success";
	public static final String UPLOAD_AND_SHARE = "uploadandsharepage";
	public static final String JOHN_HANCOCK = "John Hancock";
	public static final String TERMS_AND_CONDITIONS = "termsandconditions";
    public static final String ADD_NOTICE = "addnotice";
    public static final String VIEW = "view";
    public static final String LINK = "link";
    public static final String PLAN_HIGHLIGHT_DOCUMENT = "PlanHighlightDocument";
	public static final String PI_NOTICE = "pinotice";
	public static final String ICC_NOTICE = "iccNotice";
	public static final String CONTRACT_NO = "contractId";
	public static final String PLAN_DOCUMENT = "plandocument";
	public static final String CUSTOM_PLAN_DOCUMENT = "customPlanDocument";
	public static final String EDIT_NOTICE = "editnotice";
	public static final String POST_TO_PPT_IND = "postToPptInd";
	
	//Notice Manager Alerts
	public static final String DEFAULT_ACTION = "default";
	public static final int USER_ALERT_LIST_SIZE = 5;
	public static final int ASCII_MIN = 32;
	public static final int ASCII_MAX = 126;
	public static final int TIME_INTERVAL_MIN = 7;
	public static final int TIME_INTERVAL_MAX = 60;
	public static final String REGEX_FILE_NAME_VALIDATION = "^[^/\\\\:*@?\"<>=|]+$";
	public static final String SAVE_ACTION = "save";
	public static final String FINISH_ACTION= "finish";
	public static final String NOTICE_MANAGER_ALERTS_TAB = "noticepreference";
	public static final String CAR_VIEWS_NOTICE_MANAGER_ALERTS_TAB = "preference";
	public static final String MESSAGECENTER_PAGE= "messagecenter";
	public static final String ALERT_ERROR ="error";
	public static final String DELETE = "delete";
	public static final String MERRILL_ACCESS_URL = "merrill.access.url";
	public static final String ORDER_STATUS_UPDATE_URL = "status.update.url";

	public static final String LOG = "NOTICE_MANAGER_ALERTS";
	public static final String DATE_PARSING_FAILED="StartDate Parsing failed";
	public static final String SLASH="/";
	public static final String GET_CONTRACT="Getting contract number";
	public static final String DOES_NOT_EXIST=" doesn't existing  ";
	public static final String NULL_POINTER_EXPECTION="NoticePreferencesAction:ContractDoesNotExist-NullPointerException ";
	
	//Notice Manager Add Page Constants
	
    public static final String SUBMIT_ACTION_LABEL = "submit";
	public static final String DEFAULT_ACTION_LABEL = "default";
	public static final String UPLOAD_AND_SHARE_ACTION = "uploadandShare";
	public static final String ADD_ACTION = "add";	
	public  static final String BLANK = "";
	public static final String ADD_LOGGED = "ADD PAGE";
	public static final int DOCUMENT_VERSION_NUMBER = 1;
    
	//Notice Manager Edit Page Constants
	public static final int MAX_FILE_NAME_LENGTH = 40;
	public static final String EDIT_LOGGED = "EDIT_PAGE";
	public static final String EDIT_DEFAULT_ACTION = "default";
    public static final String UPLOAD_ACTION = "uploadandshare";
    public  static final String  MD5_HASH = "md5Hash";

	//Added this constant to refactor the code
	public static final String CLASS_ZERO = "CL0";
	
	//Added for the ECR Periodic Process
	public static final String ESTIMATED_COST_OF_RK_PERIODIC_PROCESS = "EstimatedCostOfRKPP";
	public static final String PENDING = "PN";
	public static final String FEE_DATA_PLAN_REVIEW_JOB = "FEE_DATA";
	
	//Notices: Plan data Track
	public static final String TPA_PLAN_DATA_FORM = "tpaPlanDataForm";
	public static final String CANCEL_AND_EXIT_BUTTON = "Cancel&Exit";
	public static final String SAVE_AND_EXIT_BUTTON = "Save&Exit";
	public static final String EXIT_BUTTON = "Exit";
	public static final String ALL_TABS = "allTabs";
	public static final String SUMMARY = "summary";
	public static final String CONTRIBUTION_AND_DISTRIBUTION = "contributionAndDistribution";
	public static final String SAFE_HARBOR = "safeHarbor";
	public static final String SAFE_HARBOUR = "safeHarbour";
	public static final String AUTOMATIC_CONTRIBUTION = "automaticContribution";
	public static final String INVESTMENT_INFO = "investmentInformation";
	public static final String CONTACT_INFORMATION = "contactInformation";
	public static final String NOTICE_PLAN_COMMON_VO = "noticePlanCommonVO";
	public static final String NOTICE_PLAN_DATA_VO = "noticePlanDataVO";
	
	
	public static final String ACA="ACA";
    public static final String EACA="EACA";
    public static final String QACA="QACA";
    
    //Notices: Plan data Track: InvestmentInfoTab
    public static final String TRANSFER_OUT_DAYS_30 = "30";
    public static final String TRANSFER_OUT_DAYS_60 = "60";
    public static final String TRANSFER_OUT_DAYS_90 = "90";
    public static final String TRANSFER_OUT_DAYS_CUSTOM_CODE = "00";
    
    //Notices: Plan data Track: Safe Harbor Tab
    public static final String SH_MATCH="M";
    public static final String SH_NON_ELECTIVE="N";
    public static final String ALL_ELIGIBLE_EMP="1";
    
    public static final String CONTRIBUTIONS_DAYS_30 = "30";
    public static final String CONTRIBUTIONS_DAYS_60 = "60";
    public static final String CONTRIBUTIONS_DAYS_90 = "90";
    public static final String CONTRIBUTIONS_DAYS_OTHER = "00";
    
    public static final String QACA_SHMAC = "SHMAC";
    public static final String QACA_SHNEC = "SHNEC";
	public static final String IND = "ind";
	public static final String CONTENT_KEY = "contentKey";
	public static final String PREVIOUS_IND = "pind";
	public static final String PREVIOUS_CONTETN_KEY = "pContetnKey";
	public static final String UNSPECIFIED = "U";
	public static final String  PRODUCT_ID_ARA06 = "ARA06";
	public static final String  PRODUCT_ID_ARA06Y = "ARA06Y";
	public static final List PRODUCT_IDS_MULTICLASS = Arrays.asList(new String[] {PRODUCT_ID_ARA06, PRODUCT_ID_ARA06Y});
	public static final String SEND_SERVICE_PLAN_DATA_FORM = "sendServicePlanDataForm";
	
	public static final String TRUE_COOKIES_SITE_NAME = "PlanSponsor";
	
	//PBA
	public static final String PBA_FEE_TYPE_CODE_CUSTOM = "CUST";
	
	public static final String COFID_338_FUND_SETUP_FORM_TEXT = "Addendum for Wilshire 3(38) Investment Management Service ({0} Line-Up) ";
	
	public static final String PSW_SHOW_PASSWORD_METER_IND = "PSW_SHOW_PASSWORD_METER_IND";

	public enum Cofid338InvestmentOptionProfile{
		
		ADVANCED("ADV","W338-ADV", "Advanced"),
		BALANCED("BAL","W338-BAL","Balanced"),
		ADVANTAGE("DEF","W338-DEF","Advantage"),
		FEE_SENSTIVE("FEE","W338-FEE","Fee Sensitive"); 
			
		private  String code;
		private  String dbCode;
		private  String description;
		 
		 Cofid338InvestmentOptionProfile(String code, String dbCode, String description){
			 this.code = code;
			 this.dbCode = dbCode;
			 this.description = description;
		 }
		 
		 public String getCode() {
			return code;
		}
		 
		 public String getDBCode() {
			return dbCode;
		}
		 
		 public String getDescription() {
			return description;
		}
		
		 public static Cofid338InvestmentOptionProfile getCofid338InvestmentOptionProfile(String code) {
			 
			 Cofid338InvestmentOptionProfile  cofid338InvestmentOptionProfile= null;
			 
			 for(Cofid338InvestmentOptionProfile obj : Cofid338InvestmentOptionProfile.values()) {
				 if(StringUtils.equalsIgnoreCase(code, obj.getCode())) {
					 cofid338InvestmentOptionProfile = obj;
					 break;
				 }
			 }

			 return cofid338InvestmentOptionProfile;
			 
		 }
	}
	
	// Symbols used to mark funds.
    public static final String MERRILL_RESRICTED_FUND_SYMBOL = "#";
    
    //Customer Experience Measurement System 2.0 (CXM2)
    public static final String MADELIA_SCRIPT_URL= "madelia.script.URL";
    public static final String MADELIA_FEEDBACK_BUTTON_SWITCH = "madelia.feedbackbutton.switch";
	
    public static final String PS_APPLICATION_ID = "PS";
    
    public static final String IS_FCP_CONTENT = "isFcpContent";
    
    public static final String JH_NAVIGATIOR_BASE_URL = "findliterature.jhNavigatorbaseURL";
    
    public static final String PASSWORD_CHANGE_NEW_USER = "passwordChangeNewUser";
    
    public static final String PASSCODE_FLOW_NEW_USER = "passcodeFlowNewuser";
    public static final String DE_API_DOWN = "deAPIDown";
    
    public static final String IS_CONTEXT_SPECIFIC_CMA = "isContextSpecificCMA";
    public static final String MATURITY_DATE_CUTTOFF_DATE =  "2019-12-31";
    // Naming Variable for MRL Logging Operational Indicator
 	public static final String ACCESS_CONTROL_VIOLATION_ENABLED = "isPswAccessControlViolationLoggingEnabled";
    
    public static final String LONG_TERM_PART_TIME_INFO_TEMPLATE_ACCESS = "longTermPartTimeInfoTemplateAccess";
    
    public static final String PS_NOTICE = "IPU PS";
   	public static final String APPLICATION_ID = "PSW";
   	public static final String APPLICATION_ID_FOR_TPA = "TPA";
   	public static final String PATCIPENT_NOTICE_SPANISH = "IPU PPT SP";
	public static final String PATCIPENT_NOTICE_ENGLISH = "IPU PPT EN";
}
