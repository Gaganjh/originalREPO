package com.manulife.pension.bd.web;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.manulife.pension.service.security.BDUserRoleType;

/**
 * This interface contains general constants related to BD.
 * 
 * @author
 * 
 */
public interface BDConstants extends com.manulife.pension.platform.web.CommonConstants {

    public static final String BD_APPLICATION_ID = "BD";

    public static final String PS_APPLICATION_ID = "PS";

    public static String BD_LOGIN_TRACK = "security.loginTrack";

    public static String BD_MIMIC_TRACK = "security.mimicTrack";

    public static final String USER_NAVIGATION_KEY = "UserNavigationKey";

    public String APP_CONTENT_RESTRICTION_FACADE = "ContentRestrictionFacade";

    public String BOBCONTEXT_KEY = "BobContextKey";

    // Common constants
    public static final String TASK_KEY = "task";

    public static final String FILTER_TASK = "filter";

    public static final String SHOWALL_TASK = "showAll";

    public static final String SORT_TASK = "sort";

    public static final String PAGE_TASK = "page";

    public static final String DOWNLOAD_TASK = "download";

    public static final String PRINT_TASK = "print";

    public static final String DEFAULT_TASK = "default";

    public static final String PRINT_PDF_TASK = "printPDF";

    public static final String DOWNLOAD_ALL_TASK = "downloadAll";

    // Cash Account Page
    public static final String CASH_ACCOUNT_FROM_DATES = "fromDates";

    public static final String CASH_ACCOUNT_TO_DATES = "toDates";

    public static final String CSV_FILENAME_HOLDER = "csvFileName";

    // Fund to Fund Transaction + Rebalance Details Redemption fee.PDF link.
    public static final String REDEMPTION_FEE_PDF_URL = "/assets/pdfs/redemption_fee_FR.pdf";

    // Errors, Informational, Warning Messages
    public static final String INFO_MESSAGES = "bdMessages";

    public static final String WARNING_MESSAGES = "bdWarnings";

    // BD News
    public static final String MONTH_YEAR_FORMAT = "MMMM yyyy";

    public static final String MONTH_FORMAT = "MM";

    public static final String YEAR_FORMAT = "yyyy";

    /**
     * Participant Account
     */
    // Participant Account Tabs Name
    public static final String PARTICIPANT_ACCOUNT_TAB_NAME = "Participant Account";

    public static final String MONEY_TYPE_SUMMARY_TAB_NAME = "Money Type Summary";

    public static final String MONEY_TYPE_DETAILS_TAB_NAME = "Money Type Details";

    public static final String AFTER_TAX_MONEY_TAB_NAME = "After Tax Money";

    public static final String EE_DEFERRALS_TAB_NAME = "EE Deferrals";

    // Participant Account Tabs Name for csv filename
    public static final String PARTICIPANT_ACCOUNT_CSV_NAME = "ParticipantAccount";

    public static final String MONEY_TYPE_SUMMARY_CSV_NAME = "MoneyTypeSummary";

    public static final String MONEY_TYPE_DETAILS_CSV_NAME = "MoneyTypeDetails";

    public static final String AFTER_TAX_MONEY_CSV_NAME = "AfterTaxMoney";

    public static final String EE_DEFERRALS_CSV_NAME = "EEDeferrals";

    // Participant Account Tab Id
    public static final String PARTICIPANT_ACCOUNT_TAB_ID = "pptAccount";

    public static final String MONEY_TYPE_SUMMARY_TAB_ID = "moneyTypeSummary";

    public static final String MONEY_TYPE_DETAILS_TAB_ID = "moneyTypeDetails";

    public static final String AFTER_TAX_MONEY_TAB_ID = "netContribEarnings";

    public static final String EE_DEFERRALS_TAB_ID = "netDeferral";

    // defined benefit account
    public static final String DB_CSV_NAME = "DBAccount";

    public static final String DB_ACCOUNT_CSV_NAME = "DBAccountDetails";

    public static final String DB_ACCOUNT_TAB_NAME = "Defined Benefit Account";

    public static final String DB_ACCOUNT_TAB_ID = "dbAccount";

    // Participant Account - User navigation object
    public static final String PPT_ACCOUNT_USER_NAVIGATION = "pptAccountUserNavigation";

    public static final String DB_ACCOUNT_USER_NAVIGATION = "dbAccountUserNavigation";

    // Contract Investment Allocations organizing criteria
    public static final String VIEW_BY_ASSET_CLASS = "2";

    public static final String VIEW_BY_RISK_CATEGORY = "3";

    // render constants

    public static final String RATE_FORMAT = "###.00";

    public static final String PERCENT_TWO_DECIMAL_FORMAT = "##0.00%";

    public static final String DEFAULT_VALUE_ZERO_PERCENT = "0.00%";

    public static final String AMOUNT_FORMAT = "########0.00";

    public static final String INTEGER_FORMAT = "########0";

    public static final String DOLLAR_WITH_4_PLACES_2_DECIMALS = "$#,##0.00";

    public static final String DEFAULT_VALUE_ZERO = "0.00";

    public static final String DEFAULT_SSN_VALUE = "xxx-xx-xxxx";

    public static final String NOT_APPLICABLE = "n/a";

    public static final String ZERO_STRING = "0";
    
    public static final String SIGPLUS = "SIG+";

    public static final String CL2 = "CL2";

    public static final String CL5 = "CL5";

    public static final String YES = "Y";

    public static final String NO = "N";

    public static final String LAST_NAME = "lastName";

    public static final String HISTORY_PRINT = "historyPrint";

    public static final String PROFILE_ID = "profileId";

    // BD Firm Restrictions
    public static final String BD_FIRM_RESTRICTION_CODE = "BDW";

    public static String INCLUDE_RULE = "Include";

    public static String EXCLUE_RULE = "Exclude";

    public static String NO_RULE = "-";

    public static final String SSN = "SSN";

    public static final String NAME = "Name";

    public static final String TRANSACTION_TYPE = "Transaction Type";

    public static final String DEFERRAL_UPDATE = "Deferral update";

    public static final String EZINCREASE_SERVICE_CHANGE = "JH EZincrease service change";

    public static final String EZINCREASE_SERVICE_CHANGE_DETAILS = "JH EZincrease Settings Change Details";

    public static final String DETAILS = "Details - ";

    public static final String ATTR_MIMICKING_SESSION = "bd.mimicSession";

    public static final String SSN_MASK_CHARS = "xxx-xx-";

    // BlockOfBusiness Report related Constants.
    // BOB Filter ID's that are used in JSP only.
    public static final String FILTER_BLANK_CODE = "blankCode";

    public static final String DB_SESSION_ID = "dbSessionID";

    public static final String BRL_DB_SESSION_ID = "brlDbSessionID";

    // BOB - Struts config Forward.
    public static final String BOB_CONTRACT_INFO_REDIRECT_FORWARD = "bobToContractInformation";
    
    public static final String BOB_SECURE_DOCUMENT_UPLOAD_REDIRECT_FORWARD = "bobToSecureDocumentUpload";

    public static final String BOB_INPUT_REDIRECT_FORWARD = "inputRedirect";

    // BOB Filter and Column Info related constants.
    public static final String BOB_FILTER_CONFIG_FILE = "./BOBFilterInfo.xml";

    public static final String BOB_FILTER_CONTAINER_MAP = "bobFilterContainerMap";

    public static final String BOB_COLUMN_CONFIG_FILE = "./BOBColumnsInfo.xml";

    public static final String BOB_MENU_ID = "bobUserMenu";

    public static final String APPLICABLE_COLUMNS = "applicableColumns";

    public static final String BOB_FILTER_STORE_KEY = "bobFilterStoreKey";

    public static final String REQ_PARAM_SHOW_ADVANCE_FILTER = "showAdvanceFilter";

    public static final String STORED_PROC_SESSION_ID = "STORED_PROC_SESSION_ID";

    // BOB - tab related constants
    public static final String ACTIVE_TAB = "activeTab";

    public static final String OUTSTANDING_PROPOSALS_TAB = "outstandingProposalsTab";

    public static final String PENDING_TAB = "pendingTab";

    public static final String DISCONTINUED_TAB = "discontinuedTab";

    // TITLE for BOB tabs
    public static final String ACTIVE_TAB_TITLE = "Active";

    public static final String OUTSTANDING_PROPOSALS_TAB_TITLE = "Outstanding Proposals";

    public static final String PENDING_TAB_TITLE = "Pending";

    public static final String DISCONTINUED_TAB_TITLE = "Discontinued";

    // URL for BOB tabs
    public static final String ACTIVE_TAB_URL = "/bob/blockOfBusiness/Active/";

    public static final String OUTSTANDING_PROPOSALS_TAB_URL = "/bob/blockOfBusiness/OutstandingProposals/";

    public static final String PENDING_TAB_URL = "/bob/blockOfBusiness/Pending/";

    public static final String DISCONTINUED_TAB_URL = "/bob/blockOfBusiness/Discontinued/";

    // Forward for BOB tabs
    public static final String ACTIVE_TAB_FORWARD = "blockOfBusinessActive";

    public static final String OUTSTANDING_PROPOSALS_TAB_FORWARD = "blockOfBusinessOutstanding";

    public static final String PENDING_TAB_FORWARD = "blockOfBusinessPending";

    public static final String DISCONTINUED_TAB_FORWARD = "blockOfBusinessDiscontinued";

    // Page Accessed shown in Log statements.
    public static final String PAGE_ACCESSED_ACTIVE_TAB = "Active";

    public static final String PAGE_ACCESSED_OUTSTANDING_PROPOSALS_TAB = "Outstanding Proposals";

    public static final String PAGE_ACCESSED_PENDING_TAB = "Pending";

    public static final String PAGE_ACCESSED_DISCONTINUED_TAB = "Discontinued";

    public static final String TAB = "tab";

    // BOB Constants used during MRL Logging.
    public static final String BOB_LOG_USER_PROFILE_ID = "User Profile ID=";

    public static final String BOB_LOG_PAGE_ACCESSED = "Page Accessed=";

    public static final String BOB_LOG_BLOCK_OF_BUSINESS = "Block Of Business";

    public static final String BOB_LOG_MIMIC_MODE = "Mimic mode=";

    public static final String BOB_LOG_MIMICKED_USER_PROFILE_ID = "Mimicked User Profile ID=";

    public static final String BOB_LOG_DATE_OF_ACTION = "Date of action=";

    public static final String BOB_LOG_ACTION_TAKEN = "Action taken=";

    public static final String BOB_LOG_BD_BLOCK_OF_BUSINESS = "BD Block of Business, ";

    public static final String BOB_LOG_PAGE_ACCESS = " page access";

    public static final String BOB_LEVEL_ONE = "bobForLevelOne";

    // BOB Tab values that will be sent to DAO.
    public static String ACTIVE_TAB_VAL = "AC";

    public static String OUTSTANDING_PROPOSALS_TAB_VAL = "PP";

    public static String PENDING_TAB_VAL = "PN";

    public static String DISCONTINUED_TAB_VAL = "DI";

    // Column Info for BOB report
    public static final String COLUMNS_VERSION1 = "version1";

    public static final String COLUMNS_VERSION2 = "version2";

    public static final String COLUMNS_VERSION3 = "version3";

    // This columns is used in CSV, PDF.
    public static final String COL_CONTRACT_STATUS = "Contract Status";

    // BOB Column Width related Constants
    public static final String COL_CONTRACT_STATUS_ID = "contractStatus";

    // BOB Constants used in CSV.
    public static final String CSV_BLOCK_OF_BUSINESS = "Block Of Business";

    public static final String CSV_BDINTERNAL_USER_NAME = "User Name";

    public static final String CSV_FIRM_NAME = "Firm Name";

    public static final String CSV_FIRM_NAMES = "Firm Names";

    public static final String CSV_FINANCIAL_REP = "Financial Rep";

    public static final String CSV_PRODUCER_CODE = "Producer Code";

    public static final String CSV_ACTIVE_CONTRACT_ASSETS = "Active Contract Assets**";

    public static final String CSV_NUM_ACTIVE_CONTRACTS = "Number of Active Contracts";

    public static final String CSV_NUM_OF_LIVES = "Number of Lives";

    public static final String CSV_NUM_OUTSTANDING_PROPOSALS = "Number of Outstanding Proposals";

    public static final String CSV_NUM_PENDING_CONTRACTS = "Number of Pending Contracts";

    public static final String CSV_REPORT_AS_OF = "Report as of";

    public static final String CSV_FILTERS_USED = "Filters used";

    // BOB - Error, Warning, Informational Messages to be displayed.
    public static final String INFO_MSG_DISPLAY_UNDER_COLUMN_HEADER = "infoMsgUnderColHeader";
    
    public static final String INFO_MSG_DISPLAY_ABOVE_COLUMN_HEADER = "infoMsgAboveColHeader";

    public static final String ERROR_MSG_DISPLAY_UNDER_COLUMN_HEADER = "errorMsgUnderColHeader";

    public static final BigDecimal MAX_ASSET_VALUE = new BigDecimal("99999999999.99");

    public static final BigDecimal MIN_ASSET_VALUE = new BigDecimal("0");

    public static final String CONSECUTIVE_COMMAS = ",,";

    // BlockOfBusinessUtility related constants used for caching.
    public static final String US_NY_LIST = "usNyList";
    
    public static final String PRODUCT_TYPE_LIST = "productTypeList";

    public static final String CSF_LIST = "csfList";
    
    public static final String FUND_FAMILY_LOOKUP_MAP = "fundFamilyCodeMap";

    public static final String DC = "DC";

    public static final String REFRESH = "refresh";

    public static final String QTASK = "?task=";

    public static final String POST = "POST";

    // BlockOfBusinessUtility related constants
    public static final String FIDUCIARY_WARRANTY_TITLE = "Fiduciary Standards Warranty";

    public static final String PBA_TITLE = "Personal Brokerage Account";

    public static final String LOANS_TITLE = "Loans";

    public static final String GIFL_TITLE = "Guaranteed Income feature";
    
    public static final String MA_TITLE = "Managed Accounts";
    
    public static final String ROTH_TITLE = "ROTH";

    public static final String EZINCREASE_TITLE = "Scheduled deferral increase";

    public static final String EZSTART_TITLE = "EZStart";

    public static final String PLAN_CHECK_TITLE = "PlanCheck";

    public static final String EZGOALS_TITLE = "EZgoal";
    
    public static final String DF_PREFIX = "Default Fund = ";
    
    public static final String DF_MONEY_MARKET_FUNDS_TITLE = "Default Fund = Money Market Funds";

    public static final String DF_STABLE_VALUE_FUNDS_TITLE = "Default Fund = Stable Value Funds";

    public static final String DF_OTHERS_TITLE = "Default Fund = Others";
    
    public static final String IPSM_TITLE = "IPS Manager";
    
    public static final String JH_PASSIVE_TRUSTEE_SERVICE_TITLE = "JH Trustee Service";
    
    public static final String TOTAL_CARE_TITLE = "TotalCare";
    
    public static final String WILSHIRE321_TITLE = "Wilshire 3(21) Adviser Service";
    
    public static final String SYSTEMATIC_WITHDRAWAL_TITLE = "Systematic Withdrawals";
    
    public static final String SYSTEMATIC_WITHDRAWAL_IND = "SYW";

    public static final String SEND_SERVICE_TITLE = "SEND Service";
    
    public static final String WILSHIRE338_TITLE = "Wilshire 3(38) Investment Management Service";
    
    public static final String RJ_338_TITLE = "Raymond James 3(38) Retirement Plan Solution Service";
    
    public static final String JHI_INVESTMENT_SELECTION_TITLE = "JHI Investment Selection Credit Program";
    
    public static final String JH_SVGIF_CREDIT_TITLE = "JH SVGIF Credit Program";
    
    public static final String JH_CREDITS_PROGRAM_TITLE = "JH Credits Program";
    
    // BOB - currency related constants
    public static final String NUMBER_5_DIGITS = "##,###";

    public static final String NUMBER_9_DIGITS = "#,###,###,###";

    public static int SCALE_2 = 2;

    public static int SCALE_3 = 3;

    public static int INTDIGITS = 1;

    public static int ROUNDING_MODE = BigDecimal.ROUND_HALF_DOWN;

    // BOBColumnCriteria, BOBFilterCriteria constants
    public static final String ALL_COLUMNS = "allColumns";

    public static final String UNABLE_TO_GET_BOB_STORE = "Unable to get Column Store for BOB page from BOB_COLUMN_CONFIG_XML file.";

    public static final String ALL_FILTERS = "allFilters";

    public static final String UNABLE_TO_GET_FILTER_STORE = "Unable to get Filter Store for BOB page from BOB_FILTER_CONFIG_XML file.";

    public static final String UNABLE_TO_GET_FILTER_LIST = "Unable to retrieve Filter List for Block Of Business from BOB_FILTER_CONFIG_XML file.";

    public static final String RVP_ID = "rvpId";

    // BOB Misc constants
    public static final Integer NO_QUICK_OR_ADV_FILTER_SUBMITTED = 1;

    public static final Integer QUICK_FILTER_SUBMITTED = 2;

    // BobContext related Constants.
    public static final String CONTRACT_NUMBER = "contractNbr";

    public static final Integer ADV_FILTER_SUBMITTED = 3;

    // External Broker Registration
    public static final String BD_FIRM_PARTY = "O";

    public static final String INDEPENDENT_PARTY = "I";

    /**
     * Funds and Performance
     */
    public static final String BASE_FILTER_ALL_FUNDS = "All funds";

    public static final String BASE_FILTER_CONTRACT_FUNDS = "Contract funds";

    // Tab navigation title
    public static final String FUND_INFORMATION_TAB = "Fund Information";

    public static final String PRICES_YTD_TAB = "Prices & YTD";

    public static final String PERFORMANCE_FEES_TAB = "Performance & Fees";

    public static final String STANDARD_DEVIATION_TAB = "Standard Deviation";

    public static final String FUND_CHAR_I_TAB = "Fund Char. I";

    public static final String FUND_CHAR_II_TAB = "Fund Char. II";

    public static final String MORNINGSTAR_I_TAB = "Morningstar I";

    public static final String MORNINGSTAR_II_TAB = "Morningstar II";

    public static final String FUND_CHECK_TAB = "FundCheck";
    
    public static final String JH_SIGNATURE_FUNDSCORECARD_TAB = "JH Signature Fund Scorecard";

    // Tab navigation URL
    public static final String FUND_INFORMATION_URL = "Fund Information";

    public static final String PRICES_YTD_URL = "Prices & YTD";

    public static final String PERFORMANCE_FEES_URL = "Performance & Fees";

    public static final String STANDARD_DEVIATION_URL = "Standard Deviation";

    public static final String FUND_CHAR_I_URL = "Fund Char. I";

    public static final String FUND_CHAR_II_URL = "Fund Char. II";

    public static final String MORNINGSTAR_I_URL = "Morningstar I";

    public static final String MORNINGSTAR_II_URL = "Morningstar II";

    public static final String FUND_CHECK_URL = "FundCheck";
    
    public static final String JH_SIGNATURE_FUNDSCORECARD_URL = "JH Signature Fund Scorecard";

    public static final String BOB_PAGE_FORWARD = "bobPage";

    public static final String ID_TYPE_SSN = "ssn";

    public static final String ID_TYPE_TAX = "tax";

    // Contract Navigation Menu Item IDs.
    public static final String CONTRACT_INFO_ID = "contractInfo";

    public static final String CONTRACT_SNAPSHOT_ID = "contractSnapshot";

    public static final String CONTRACT_DOCUMENTS_ID = "contractDocuments";
    
    public static final String IPS_MANAGER_ID = "contractIPSMgr";

    public static final String DEFINED_BENEFIT_ACCT_ID = "definedBenefitAcct";

    public static final String CONTRACT_FUNDS_PERFORMANCE_ID = "contractsFandP";

    public static final String CONTRACT_INV_ALLOCATION = "contractInvAlloc";

    public static final String PARTICIPANT_SUMMARY = "pptSummary";

    public static final String PARTICIPANT_ACCOUNT = "pptAccount";

    public static final String PARTICIPANT_STATEMENTS = "pptStatements";
    
    public static final String ROR_CALCULATOR = "rorCalculator";
    
    public static final String PARTICIPANT_STATEMENTS_RESULTS = "pptStatementsResults";

    public static final String PARTICIPANT_TXN_HISTORY = "pptTxnHistory";
    
    public static final String PARTICIPANT_BENEFIT_BASE_INFORMATION = "pptBenefitBaseInfo";

    public static final String TXN_HISTORY = "txnHistory";

    public static final String CASH_ACCOUNT = "cashAccount";

    public static final String CURRENT_LOAN_SUMMARY = "currLoanSummary";

    public static final String CONTRACT_STATUS_AC = "AC";

    public static final String CONTRACT_STATUS_CF = "CF";

    public static final String CONTRACT_STATUS_DI = "DI";

    public static final String CONTRACT_STATUS_IA = "IA";
    
    public static final String CONTRACT_STATUS_PN = "PN";
    
    public static final String CONTRACT_STATUS_PS = "PS";
    
    public static final String CONTRACT_STATUS_DC = "DC";
    
    public static final String CONTRACT_STATUS_PC = "PC";
    
    public static final String CONTRACT_STATUS_CA = "CA"; 
    

    public static final String CONTRACT_NAVIG_XML_FILE = "/WEB-INF/contractNavigationMenu.xml";

    public static final String CONTRACT_NAVIG_XML_BEAN_NAME = "contractNavigMenu";

    // Broker Listing Related Constants.
    // Broker Listing Filters related constants.
    public static final String FILTER_BLANK_ID = "blank";

    public static final String FILTER_BLANK_TITLE = "";

    public static final String FILTER_FINANCIALREP_NAME_TITLE = "Financial Rep";

    public static final String FILTER_BDFIRM_NAME_TITLE = "Firm Name";

    public static final String FILTER_CITY_NAME_TITLE = "City";

    public static final String FILTER_STATE_CODE_TITLE = "State";

    public static final String FILTER_ZIP_CODE_TITLE = "ZIP Code";

    public static final String FILTER_PRODUCER_CODE_TITLE = "Producer Code";

    public static final String FILTER_RVP_TITLE = "RVP Name";

    public static final String FILTER_REGION_TITLE = "Region";

    public static final String FILTER_DIVISION_TITLE = "Division";

    // Broker Listing - MRL Logging related constants.
    public static final String BRL_LOG_USER_PROFILE_ID = "User Profile ID=";

    public static final String BRL_LOG_PAGE_ACCESSED = "Page Accessed=";

    public static final String BRL_LOG_FINANCIAL_REP_LISTING = "Financial Representative Listing";

    public static final String BRL_LOG_MIMIC_MODE = "Mimic mode=";

    public static final String BRL_LOG_MIMICKED_USER_PROFILE_ID = "Mimicked User Profile ID=";

    public static final String BRL_LOG_DATE_OF_ACTION = "Date of action=";

    public static final String BRL_LOG_ACTION_TAKEN = "Action taken=";

    public static final String BRL_LOG_BD_BLOCK_OF_BUSINESS = "BD Financial Representative Listing page access";

    // Broker Listing Column Titles
    public static final String COL_FINANCIAL_REP_NAME_TITLE = "Financial Representative";

    public static final String COL_FIRM_NAME_TITLE = "Firm Name";

    public static final String COL_CITY_TITLE = "City";

    public static final String COL_STATE_TITLE = "State";

    public static final String COL_ZIP_CODE_TITLE = "ZIP Code";

    public static final String COL_PRODUCER_CODE_TITLE = "Producer Code";

    public static final String COL_NUM_OF_CONTRACTS_TITLE = "Number Of Contracts";

    public static final String COL_BL_TOTAL_ASSETS_TITLE = "Total Assets($)";

    public static final String BROKER_LISTING_FORWARD = "brokerListing";

    public static final String CSV_FINANCIAL_REP_LISTING = "Financial Representative Listing";

    public static final String CSV_USER_NAME = "User Name";

    public static final String CSV_SUMMARY = "Summary";

    public static final String CSV_TOTAL_CONTRACT_ASSETS = "Total Contract Assets";

    public static final String CSV_TOTAL_NUM_OF_CONTRACTS = "Total Number Of Contracts";

    public static final String CSV_TOTAL_NUM_OF_FINANCIAL_REPS = "Total Number of Financial Representatives";

    // Broker Listing params sent to BOB when the user clicks on the Financial Rep Name link in
    // Broker Listing page
    public static final String REQ_PARAM_REPORT_AS_OF_DATE = "asOfDate";

    public static final String REQ_PARAM_FINANCIAL_REP_NAME = "financialRepName";

    // Participant Transaction History related constants.
    public static final String CSV_HEADER_FROM_DATE = "From date";

    public static final String CSV_HEADER_TO_DATE = "To date";

    public static final String CSV_HEADER_TRANSACTION_TYPE = "Type";

    public static final String CSV_DISPLAY_DATES = "displayDates";

    public static final String CSV_TRUE = "true";

    public static final String CSV_INVALID_DT_FORMAT = "invalid date format";

    public static final String CSV_INVALID_MONTH = "invalid month";

    public static final String CSV_INVALID_DAY = "invalid day";

    public static final String CSV_INVALID_CODE = "Invalid code [";

    public static final String CSV_PARTICIPANT_ID = "participantId";

    public static final String CSV_REQUEST_NAME = "requestName";

    public static final String CSV_PROFILE_ID = "profileId";

    public static final String CSV_PPT_TXN_HISTORY = "participantTransactionHistory";

    public static final String CSV_CONTRACT = "Contract";

    public static final String CSV_ASOF = "As of,";

    public static final String CSV_LAST_FIRST_NAME_SSN = "Last name,First name,SSN";

    public static final String CSV_PPT_TEXT = "text";

    public static final String CSV_0 = "0";

    public static final String CSV_WD = "WD";

    public static final String CSV_CLOSING_BRACKET = "]";

    public static final String CSV_ALL = "All";

    public static final String ERRORS_PRESENT = "errorsPresent";

    // Participant Transaction Details - Contribution related constants
    public static final String CSV_CONTRIBUTION_TYPE = "Contribution Type";

    public static final String CSV_EMPLOYEE_CONTRIB = "Contribution - Employee";

    public static final String CSV_EMPLOYER_CONTRIB = "Contribution - Employer";

    public static final String CSV_TOTAL_AMT = "Total Amount";

    public static final String CSV_TXN_NUM = "Transaction Number";

    public static final int NUMBER_0 = 0;

    public static final int NUMBER_12 = 12;

    public static final int NUMBER_31 = 31;

    public static final String ALL_TYPES = "ALL";

    public static final String NULL = "null";

    public static final String DUMMY_DATE = "01/01/0001";

    // My Profile Personal Info page
    public static final String BROKER_ROLE_ID = BDUserRoleType.FinancialRep.getRoleId();

    public static final String BASIC_BROKER_ROLE_ID = BDUserRoleType.BasicFinancialRep.getRoleId();

    public static final String BROKER_ASSISTANT_ROLE_ID = BDUserRoleType.FinancialRepAssistant
            .getRoleId();

    public static final String FIRM_REP_ROLE_ID = BDUserRoleType.FirmRep.getRoleId();
    
    public static final String RIA_USER_ROLE_ID = BDUserRoleType.RIAUser.getRoleId();

    public static final String CONTRACT = "Contract";

    public static final String DEFERRAL_CHANGE_SUMMARY = "Deferral change summary";

    public static final String BEAN_DETAILS = "details";

    public final String SSN_MESSAGE_PREFIX = "for SSN ";

    public final String TAX_ID_MESSAGE_PREFIX = "for Tax ID ";

    // iEvaluator web constants.
    public static final String CRITERIA_DETAILS = "criteriaDetails";

    public final String CONTRACT_NAVIGATION_MENU = "ContractNavigationMenu";

    public final String DATE_FORMAT_MMDDYYYY = "MMddyyyy";

	public static final String PLAN_HIGHLIGHT_XSS = "PLAN_HIGHLIGHT_XSS";

    public static final String MAIL_PROTOCOL = "smtp";

    public static final String IS_PARTICIPANT = "isParticipant";

    public static final String DEFAULT_VIEWING_PREFERENCE = "4";

    public static final String VIEWING_PREFERENCE = "1";

    // User Management - Broker
    public static final String NOT_REGISTERED = "Not Registered";

    public static final String YES_VALUE = "Yes";

    public static final String NO_VALUE = "No";

    public static final String COUNTRY_USA = "USA";

    public static final String FORMAT_DATE_LONG = "MMMM d, yyyy";

    public static final String FORMAT_DATE_SHORT_YMD = "yyyy-MM-dd";

    public static final int NUMBER_2 = 2;

    public static final int NUMBER_40 = 40;

    public static final String READY_FOR_UPDATE = "ReadyForUpdate";

    public static final String DEFINED_BENEFIT = "DB";

    public static final String ADMINISTRATION = "Administration";

    public static final String CONTRACT_INSTALLATION = "Contract Installation";

    public static final String INVESTMENT = "Investment";

    public static final String BD_FORMS = "bdForms";

    // public home page

    public static final String HOST_URL_NAME = "siteDomain";

    public static final String SITE_PROTOCOL_NAME = "siteProtocol";

    public static final String SVF_COMPETING_FUNDS = "svfCompetingFunds";

    public static final String SVF_FUNDS = "stableValueFunds";

    // My Profile - Broker Personal Info

    public static final String MAILING_ADDRESS = "mailingAddress";

    public static final String HOME_ADDRESS = "homeAddress";

    // Secured Home Page

    public static final String SHOW_ALL = "Show All";

    public static final String SHOW_CURRENT = "Show Current";

    // Fund Evaluator pages
    public static final String ASSSET_CLASS_INV_OPTIONS = "assetClassInvestmentOptions";

    public static final String FUND_EVALUATION_LOCATION = "fundEvaliationLocation";

    public static final String FUND_EVALUATOR_LOG_DATE_OF_ACTION = "Date of action=";

    public static final String FUND_EVALUATOR_LOG_USER_PROFILE_ID = "User Profile ID=";

    public static final String FUND_EVALUATOR_LOG_ACTION_TAKEN = "Action taken=";

    public static final String FUND_EVALUATOR_LOG_ACTION_RESULT = "FundEvaluator Report Request";

    public static final String FUND_EVALUATOR_LOG_YOUR_CLIENT = "Your Client=";

    public static final String FUND_EVALUATOR_LOG_NEW_PLAN = "New Plan";

    // BD Global Messages
    public static final String GLOBAL_MESSAGE_EXPIRE_ACTION_TEXT = "BD Expire Global message";

    public static final String GLOBAL_MESSAGE_PUBLISH_ACTION_TEXT = "BD Publish Global message";

    public static final String GLOBAL_MESSAGE_REPUBLISH_ACTION_TEXT = "BD Republish Global message";

    public static final String GLOBAL_MESSAGE_CONTENT_KEY_TEXT = "CMA key = ";

    public static final String FIRM_NAME_PARAM = "firmName";

    public static final String FUND_EVALUATOR_PRINT_DATA = "fundEvaluatorPrintData";

	//Site Map page use these constants to add the content text.
	public static final String HOME_MENU_TITLE = "Home";
	
	public static final String PARTNERING_WITH_US_MENU_TITLE = "Partnering with Us";
	
	public static final String PRIME_ELEMENTS_MENU_TITLE = "Retirement Solutions";
	
	public static final String NEWS_AND_EVENTS_MENU_TITLE = "News & Events";
	
	public static final String FIND_LITERATURE_MENU_TITLE = "Find Literature";
	
	public static final String FORMS_MENU_TITLE = "Forms";
	
	public static final String SITE_MAP_MENU_TITLE = "Site Map";
	
	public static final String BLOCK_OF_BUSINESS_MENU_TITLE = "Block of Business";
	
	public static final String HOME_PAGE_LINK = "Home Page";
	
	public static final String MESSAGE_CENTER_LINK = "Message Center";
	
	public static final String RIA_STATEMENTS_LINK = "RIA Statements";
	
	public static final String CONTACT_US_LINK = "Contact Us";
	
	public static final String MY_PROFILE_LINK = "My Profile";
	
	public static final String USER_MANAGEMENT_LINK = "User Management";

	public static final String GIFL_RISK_CATEGORY_CODE = "GW";

    /**
     * Gateway phase 3 starts
     * Participant BenefitBase Information Attributes
     */
	
	public static final String TECHNICAL_DIFFICULTIES = "technicalDifficulties";
	
    public static final String BENEFIT_DETAILS = "benefitDetails";	
    
    public static final String ACCOUNT_DETAILS = "accountDetails";
    
    //Key to set TradeRestriction object in the request
    public static final String TRADING_RESTRICTION = "TRADING_RESTRICTION";
    
    public static final String DEFAULT_DATE = "9999-12-31";
    
    public static final String AWAITING_DEPOSIT = "Awaiting deposit";

    public static final String DEFAULT_ANNIVERSARY_DATE = "8888";

    public static final String BENEFIT_BASE_DETAILS = "details";

    public static final String DISPLAY_DATES = "displayDates";

    public static final String TRUE = "true";
    
    public static final String WILSHIRE_3_21_ADVISER_SERVICE = "Wilshire 3(21) Adviser Service";

    public static final String  PARTICIPANT_ID = "participantId";
	
    public static final String NA = "N/A";
    
    public static final String GIFL_V1_V2_TITLE = "Guaranteed Income for Life ~";
   
    public static final String GIFL_SELECT_TITLE = "Guaranteed Income for Life Select ~";
    
    //Different GIFL versions
    public static final String GIFL_VERSION_03 = "G03";
    public static final String GIFL_VERSION_02 = "G02";
    public static final String GIFL_VERSION_01 = "G01";
    
    //	GIFL-2A - 
	public static final String BB_BATCH_STATUS = "BBBatchDateLessThenETL";  
	
	//  GIFL - Managed Volatility - LSPS Funds
	public static final List<String> LSPS_FUNDS = Arrays.asList(new String[] {"SPG", "SPB", "SPM", "SPC", "NAV", "NAU", "NAQ", "NAO"});
	
	
	//Withdrawal
	public  static final String VERSION_1="V1";
	
	public  static final String VERSION_2="V2";

	//FundCheck
	
	public static final String FUNDCHECK_LABEL = "FundCheck<sup>®</sup>";
	public static final String INVESTMENT_PLATFORM_UPDATE = "Investment Platform<br>Update";
	
	public static final String PRODUCER_LABEL = "Producer:";
	
	public static final String FUNDCHECK_INPUT_SELECT_KEY = "";
	public static final String FUNDCHECK_INPUT_SELECT_LABEL = "Please Select";
	public static final String FUNDCHECK_INPUT_CONTRACT_NAME_KEY = "contractName";
	public static final String FUNDCHECK_INPUT_CONTRACT_NUMBER_KEY = "contractNumber";
	public static final String FUNDCHECK_INPUT_FR_NAME_KEY = "finRepName";
	public static final String FUNDCHECK_INPUT_PRODUCER_CODE_KEY = "producerCode";
	public static final String FUNDCHECK_INPUT_CONTRACT_NAME_LABEL = "Contract Name";
	public static final String FUNDCHECK_INPUT_CONTRACT_NUMBER_LABEL = "Contract Number";
	public static final String FUNDCHECK_INPUT_FR_NAME_LABEL = "FR Name";
	public static final String FUNDCHECK_INPUT_PRODUCER_CODE_LABEL = "Producer Code";
	public static final int NUMBER_MAX_RESULT = 35;
	public static final int NUMBER_MAX_RESULT_TO_DISPLAY = 5;
	
	public static final String PREFIX_LOGIN_COOKIE = "User_name=";
	public static final String LOGIN_NAME_FROM_COOKIE_ATTRIBUTE = "COOKIE_USER_NAME";
	public static final String LOGIN_REMEBER_ME_ATTRIBUTE = "REMEMBER_ME_ATT";
	public static final String LOGIN_REMEMBER_ME_ON = "on";
	public static final String SEASON4 = "SFL";
	public static final String SEASON9 = "FFL";
	public static final String DIRECT_URL_ATTR = "directURL";
	
	
	public static final String LEGEND    = "Legend";
	public static final String LEGEND_1  = "Where Firm is blank, there is no Firm affiliation ";
	public static final String LEGEND_2  = "FR – Financial Representative";
	public static final String LEGEND_3  = "DB TR 1st Yr – Deposit Based Transfer 1st year";
	public static final String LEGEND_4  = "DB RG 1st Yr – Deposit Based Regular 1st year";
	public static final String LEGEND_5  = "DB RG Ren – Deposit Based Regular Renewal";
	public static final String LEGEND_6  = "AB 1st Yr – Asset Based 1st year";
	public static final String LEGEND_7  = "AB Ren – Asset Based Renewal";
    public static final String LEGEND_8 =  "Paid By John Hancock = Price Credits ";
    
    public static final String AESTRIK =  "*";
    
    public static final String COFID321_ID = "contractCoFid321";
    public static final String COFID_FUND_RECOMMENDATION_ID = "contractCoFidRecommendation";
    
    public static final String SYSTEMATIC_WITHDRAWAL_REPORT_ID = "sysWithdrawReport";
    
    public static final String FUND_ADMIN_ID = "fundAdmin";
    
    //JAN 2015 Release changes
    
    /*@ TODO to be checked and removed*/
	/*public static final String CONTRACT_REVIEW_REPORTS_TAB = "contractReviewReportsStep1Tab";
	public static final String CONTRACT_REVIEW_REPORTS_FORWARD = "contractReviewReportsStep1";
	
	public static final String C0NTRACT_IREVIEW_REPORT_BEAN = "contractIReviewReportBean";
	public static final String CONTRACT_REVIEW_REPORTS_FORWARD_STEP2 = "contractReviewReportsStep2";
	public static final String CONTRACT_REVIEW_REPORTS_TAB_STEP2 = "contractReviewReportsStep2Tab";*/
    
    public static final String C0NTRACT_REVIEW_REPORT_BEAN = "contractReviewReportBean";
    public static final String CONTRACT_REVIEW_REPORTS_STEP1 = "contractReviewReportsStep1";
	public static final String CONTRACT_REVIEW_REPORTS_STEP2 = "contractReviewReportsStep2";
	public static final String C0NTRACT_REVIEW_REPORT_RESULTS = "contractReviewReportResults";
	public static final String C0NTRACT_REVIEW_REPORT_PRINT = "contractReviewReportPrint";
	public static final String C0NTRACT_REVIEW_REPORT_HISTORY = "contractReviewReportshistory";
	public static final String NO_ERRORS = "no Errors";
	public static final String COVERIMAGE = "CoverImage";
	public static final String LOGOIMAGE = "LogoImage";
	public static final String COVER_PAGE_IMAGE_ORDER_INDEX = "path";
	public static final String COVER_IMAGE_INVALID_FORMAT="Error! Your cover image must be a .jpg image.  Please select another image.";
	public static final String COVER_IMAGE_INVALID_SIZE="Error! Your cover image cannot be larger than 2MB.  Please resize or select another image.";
	public static final String COVER_IMAGE_INVALID_DIMENSIONS="Error! Your cover image must fit within 1232x618 dimensions. Please select another image.";
	public static final String LOGO_IMAGE_INVALID_FORMAT="Error! Your logo image must be a .jpg image.  Please select another image.";
	public static final String LOGO_IMAGE_INVALID_SIZE="Error! Your logo image cannot be larger than 2MB.  Please resize or select another image.";
	public static final String LOGO_IMAGE_INVALID_DIMENSIONS="Error! Your logo image must fit within 600x400 dimensions. Please select another image.";
	public static final String CONTRACT_REVIEW_REPORT = "YES";

	public static final String C0NTRACT_REVIEW_REPORT_FILTER_DATA = "contractReviewReportFilterData";
	public static final String FORWARD_PLAN_REVIEW_REPORTS_STEP1_PAGE = "contractReviewReportStep1Page";
	public static final String IA_STATUS = "IA";
	public static final String CF_STATUS = "CF";
	public static final String DI_STATUS = "DI";
	/*@ TODO before commiting we need to chage it as -3*/
	public static final int LESS_THREE_MONTHS = -3;
	public static final int MORE_THREE_MONTHS = 3;
	public static final String PR_BOB_LEVEL_PARAMETER = "bob";
	public static final String PR_CONTRACT_LEVEL_PARAMETER = "contract";
	public static final String PLAN_REVIEW_REQUEST_ID ="planReviewRequestId";
	public static final String PLAN_REVIEW_ACTIVITY_ID ="planReviewActivityId";
	public static final String FILTER_DB_SESSION_ID = "sessionIdForDB";
	public static final String CONTRACT_DEFINED_BENEFIT_DIESABLED_TEXT = "Not Applicable";
	public static final String CONTRACT_EFF_DATE_AND_STATUS_DIESABLED_TEXT = "Plan review reports are currently not available for this contract";
	public static final String DEFAULT_COVER_IMAGE ="173445950-ACRcoverimage-2643x1247";
	public static final String REQUEST_FROM_HISTORY_OR_PRINT ="isRequestFromHistoryOrPrint";
	public static final String PAGE_INFO ="pageInfo";
	public static final int PR_HISTORY_MAX_LATEST_MONTH_END_DATES_DROP_DOWN = 26;
	public static final String FILTER_CONTRACT_NUMBER_SLEECTED ="ContractNumber";
	public static final String FILTER_CONTRACT_NAME_SLEECTED = "ContractName";
	// Unmanaged Images Directory
    public static final String UNMANAGED_PLAN_REVIEW_IMAGE_FILE_PREFIX = "/assets/unmanaged/images/planReviewCover/";
    //contract level message Plan review reports are currently not available for this contract
    public static final String CONTRACT_LEVEL_DIESABLED_TEXT = "Plan review reports are currently not available for this contract" ;
    
    // Naming Variable for Plan Review  Report Operational Indicator
	public static final String PLAN_REVIEW_REPORT_AVAILABILITY_NAMING_VARIABLE = "bd.isPlanReviewFunctionalityAvailable";
	// Naming Variable for Plan Review Launched Indicator
	public static final String PLAN_REVIEW_LAUNCHED_NAMING_VARIABLE = "bd.isPlanReviewLaunched";
	
	public static final String FORWARD_PLAN_REVIEW_REPORTS_UNAVAILABLE_PAGE = "planReviewReportsUnavailablePage";
	public static final String FORWARD_PLAN_REVIEW_REPORTS_BOB_PAGE = "bobPage";
	public static final String VIEW_DISABLE_REASON = "viewDisableReason";
	public static final String DISABLE_STATUS = "disableStatus";
	
	public static final String IS_PLAN_REVIEW_ADMIN_USER = "isPlanReviewAdminUser";

	//Block of Business Plan Review Reports pages
    public static final String SINGLE_RECORD ="1";
    public static final String NO_RECORD ="0";
    public static final String SCROLL_RECORD_LIMIT ="35"; 
    public static final String BOB_LEVEL_RESULTS_PAGE = "bobResults";
	public static final String IND_CONTRACT_PLAN_REVIEW_REPORT_DOWNLOAD = "view";
	public static final String IND_CONTRACT_EXECUTIVE_SUMMARY_DOWNLOAD = "summary";
	public static final String PR_CONTRACT_LEVEL_REQUEST_PARAMETER = "isPlanReviewContractLevel";

    
	// 60day page changes
    public static final String REGULATORY_DISCLOSURES = "regulatoryDisclosures";
    public static final String DISCONTINUED_STATUS = "DI";
	public static final String DISTRIBUTION_CHANNEL = "DISTRIBUTION_CHANNEL";
	public static final String GROUP_FIELD_OFFICE_NO = "GROUP_FIELD_OFFICE_NO";
	public static final String GFO_CODE_25270 = "25270";
	public static final String GFO_CODE_25280 = "25280";
	public static final String ACTIVE = "AC";
	public static final String FROZEN = "CF";
	public static final String MTA = "MTA";
	public static final String SELECTED_VIEW = "selectedView";
	public static final String SELECTED = "selected";
	public static final String AVAILABLE = "available";
	public static final String REPORT_DATES_FEE_DISCLOSURE = "reportDatesFeeDisclosure";
	public static final String SHOW_LINK = "showLink";
	public static final String WEB_SYMBOLS_FOOTNOTES = "symbolsArray";
	public static final String FUND_TYPE_GARUNTEED = "GA";
	public static final String FUND_FAMILY_CATEGORIZATION_SVF = "SVF";
	public static final String FUND_CODE_MSV = "MSV";
	public static final String FUND_CODE_NMY = "NMY";
	public static final String ESTIMATED_JH_RECORDKEEPING_COST_SUMMARY = "estimatedJhRecordKeepingCostSummary";
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
	
	public static final String OMNITURE_WEB_ANALYTICS_CAPTURE_DESTINATION = "webAnalyticsDestinationFRW";
	public static final String BROKER_ID = "BROKER_ID";
	public static final int THREE_MONTHS = 3;
	public static String PAGE_REGULARLY_NAVIGATED_IND = "isPageRegularlyNavigated";
	public static String IS_DB_CONTRACT = "ISDBCONTRACT";
	public static String ALL_MAGAZINE_INDUSTRY_DROPDOWN_VALUES = "allMagazineIndustryDropdownValues";
	public static String PLAN_REVIEW_COVER_PAGE_IMAGE_LIST = "PLAN_REVIEW_COVER_PAGE_IMAGE_LIST";
	public static String PR_HISTORY_SEARCH_BY_CONTRACT_NUMBER = "ContractNumber";
	public static String PR_HISTORY_SEARCH_BY_CONTRACT_NAME = "ContractName";
	public static final Integer DEFAULT_HISTORY_FROM_DATE_LESS_IN_MONTHS = -26;
	public static final String CONTRACTS_WHICH_ALEREADY_REACHED_LIMIT = "contractsWhichAlereadyReachedLimit";
	public static final String SHOW_PLAN_REVIEW_REPORTS_LINK = "showPlanReviewReportsLink";

	public static final String FIRM_TYPE = "firmType";
	public static final String TDF_TOOL_AUTHUNTICATION_KEY = "bd.targetDateFundCalcToolAccessKey";
	public static final String TDF_TOOL_URL = "bd.targetDateFundCalcToolUrl";
	
	public static final String INTERNAL_USER_ID_TYPE = "UPI";
	public static final String EXTERNAL_USER_ID_TYPE = "UPE";
	
	public static final String IPS_CANCEL_CONFIRMATION_TEXT = "cancelConfirmationText";
	public static final String IPS_CANCEL_NYSE_NOT_AVAILABLE_TEXT = "cancelNyseNotAvailableText";
	public static final String IPS_SERVICE_DATE_CHANGE_NOT_AVAILABLE_TEXT = "serviceDateChangeNotAvailableText";
	
	//Added this constant to refactor the code
	public static final String CLASS_ZERO = "CL0";
	public static final String SIGNATURE_PLUS ="CX0";
	public static final String CY1_RATE_TYPE ="CY1";
	public static final String CY2_RATE_TYPE ="CY2";
	public static final String STATE_MARYLAND ="MD";
	public static final String TRUE_COOKIES_SITE_NAME = "FRW";
	
	public static final String ERISA_ONLINE_URL = "bd.ErisaOnlineUrl";
	
	//RSA Step-up Authentication
    public static final String BD_LOGIN_VALUE_OBJECT = "BDLoginValueObject";
    public static final String USERID_KEY = "USERID";
        
    public static final String USERINFO_KEY = "USERINFO_KEY";
    public static final String TYPE_CODE = "TEMP";
    
    
    // Symbols used to mark funds.
    public static final String MERRILL_RESRICTED_FUND_SYMBOL = "#";
    
    public static final String JH_NAVIGATIOR_BASE_URL = "bd.jhNavigatorbaseURL";
    
    //Variable Added for MarketWatch SFL 
    public static final String MARKET_WATCH_SERVICE_URL = "bd.morningstarservice.url";
    public static final int SESSION_EXPIRED_TEXT = 97600;
    public static final int SESSION_EXPIRY_WARNING_MESSAGE = 97599;  

	public static final String FRW_SHOW_PASSWORD_METER_IND = "FRW_SHOW_PASSWORD_METER_IND";
	
	public static final String IS_EXTERNAL_USER = "isExternalUser";
	
	public static final String IS_INTERNAL_USER = "isInternalUser";
	
	public static final String DAO_FLAG = "daoFlag";
	
	// Naming Variable for MRL Logging Operational Indicator
	public static final String ACCESS_CONTROL_VIOLATION_ENABLED = "bd.isFrwAccessControlViolationLoggingEnabled";
	
	public static final String PATCIPENT_NOTICE_SPANISH = "IPU PPT SP";
	public static final String PATCIPENT_NOTICE_ENGLISH = "IPU PPT EN";
	public static final String PRODUCER_NOTICE_USA = "IPU FR USA";
	public static final String PRODUCER_NOTICE_NY = "IPU FR NY";
	public static final String PS_NOTICE = "IPU PS";
	public static final String APPLICATION_ID = "FRW";
	
	public static final String LOGIN_USER_PRINCIPAL= "loginUserPrincipal";
}