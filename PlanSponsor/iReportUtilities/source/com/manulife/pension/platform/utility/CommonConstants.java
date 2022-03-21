package com.manulife.pension.platform.utility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import com.manulife.pension.platform.utility.util.CommonEnvironment;
import com.manulife.pension.service.fund.cache.FundInfoCache;
import com.manulife.pension.service.fund.valueobject.FundVO;

/**
 * Constants that are used in BaseWeb.   PSW/BD specific constants
 * can extend this.
 * 
 * @author guweigu
 *
 */
public interface CommonConstants {
    public static final String PS_APPLICATION_ID = "PS";

    public String APPLICATION_FACADE_KEY = "ApplicationFacadeKey";
	
    public String USERPROFILE_KEY = "USER_KEY";
    public String ERROR_KEY = "errorKey";
    
    public String LAYOUT_BEAN = "layoutBean";

    public String COMPANY_ID_US = "019";
    public String COMPANY_ID_NY = "094";
    public String COMPANY_NAME_US = "US";
    public String COMPANY_NAME_NY = "NY";
    
    public int  CONTRACT_NUMBER_MIN_LENGTH = 5;
    public int  CONTRACT_NUMBER_MAX_LENGTH= 7;
    public String STR_CONTRACT_NUMBER_MAX_LENGTH= "7";
    public int  CONTRACT_NUMBER_MIN_VALUE=10000;
    public int  CONTRACT_NUMBER_MAX_VALUE=9999999;
    public int  FIVE_DIGIT_CONTRACT_NUMBER_START_INDEX=5;

	public static final String HTTPS = "https";
    
    public static String YES = "Y";
    public static String NO = "N";
    //Added For PPT Indicator
    public static String YES_INDICATOR="yes";
    public static String NO_INDICATOR="no";
    
    public static String BLANK = "";
    
    public static String PDF_FILE_NAME_EXTENSION = ".pdf";
    
    public static String BD_WARNINGS_KEY = "bdWarnings";
    
    public static String IS_ERROR = "isError";

    // Unmanaged Images Directory
    public static final String UNMANAGED_IMAGE_FILE_PREFIX = "/images/";
    
    // page title images directory
    public static final String PAGE_TITLE_IMAGE_FILE_PREFIX = "/assets/pagetitleimages/";
    
    public static final String HEADER_FOOTER_IMAGE = "header_footer_image.gif";

    public static final String USERINFO_KEY = "USERINFO_KEY";
    
    // Report attribute keys
    public static final String REPORT_CRITERIA_KEY = "reportCriteria";

    public static final String REPORT_BEAN = "reportBean";
    
    public static final String FORM_BEAN="form";
    
    public static final String REPORT_BEAN_INVESTMENT = "reportBeanInvestment";

    public static final String ERROR_COLLECTION_BEAN = "errorCollectionBean";

    public static final String DEFAULT_PAGE_SIZE_KEY = "java:comp/env/defaultPageSize";

    public static final String CONTRACT_STATEMENTS_KEY = "contractStatements";
    
    public static final String CONTRACT_DOCUMENTS_KEY="contractDocuments";

    public static final String LOAN_SUMMARY_PAGE_SIZE_KEY = "java:comp/env/defaultPageSize.LoanSummary";

    public static final String FUNDSHEET_URL = "java:comp/env/FundSheetURL";

    public static final String RMI_REPORT_SERVER_NAME = "RMIReportServer";

    public static final String RMI_IFILE_SERVER_NAME = "RMIiFileServer";

    public static final String RMI_IFILE_SERVER_NAME_FAILOVER = "RMIiFileServerFailover";
    
    public static final String COMPANY_NAME = "java:comp/env/companyName";

    public static final String LOAN_REPAYMENT_DETAILS_REPORT_DATA = "loanRepaymentDetailsReportData";
    
    public static final String TRANSACTION_TYPES = "transactionTypes";

    public static final String TRANSACTION_DATE = "transactionDate";
    
    public static final String TYPES_DROPDOWN = "typesDropdown";
    
    public static final String LOANS_DROPDOWN = "noLoansTypesDropdown";

    // Transactions
    public static final String LOAN_TYPE_INTERNAL = "I";

    public static final String LOAN_TYPE_TRANSFER = "T";

    public static final String LOAN_ACTIVITY_REPAYMENT = "R";

    public static final String LOAN_ACTIVITY_REPAYMENT_DESC = "Repayment";

    public static final String LOAN_ACTIVITY_DEFAULTED = "D";

    public static final String LOAN_ACTIVITY_DEFAULTED_DESC = "Loan Closure";

    public static final String ACCOUNT_SERVICE_STATUS = "AccountService";

    public static final String ACCOUNT_SERVICE_MESSAGE = "AccountServiceMsg";

    public static final String TXN_HISTORY_FROM_DATES = "txHistFromDates";

    public static final String TXN_HISTORY_TO_DATES = "txHistToDates";    

    // Application attbute KEYS
    public static final String ENVIRONMENT_KEY = "environment";

    public static final String SITE_PROTOCOL = "siteProtocol";

    public static final String SITE_DOMAIN = "siteDomain";

    public static final String SITE_LOCATION = "java:comp/env/siteMode";

    public static final String COOKIE_DOMAIN = "cookieDomain";
    
    public static final String STATEMENTS_URL = "EPSIIAStatementsURL";

    public static final String FUND_PACKAGE_RETAIL = "RET";

    public static final String FUND_PACKAGE_HYBRID = "HYB";

    public static final String FUND_PACKAGE_VENTURE = "IFP";

    public static final String FUND_PACKAGE_BROKER = "VRS";

    public static final String FUND_PACKAGE_MULTICLASS = "MC";

    public static final String FUND_PACKAGE_BROKER_HYBRID = "BRD"; // LS May 2006 artificial fund
                                                                    // package code to support
                                                                    // multiple class phase1 forms

    public static final String FUND_PACKAGE_VRS = "VRS";

    //investment pages
    
    public static final String VIEW_BY_ASSET = "0";

    public static final String VIEW_BY_ACTIVITY = "1";
    
    public static final String CHART_DATE_PATTERN = "MM/yyyy";
    
    public static final String VIEW_FUNDS = "viewFunds";
    
    public static final String PERFORMANCE_CHART_CONTRACT = "performanceChartContract";
    
    public static final String VIEW_FUNDS_ORIGINAL = "viewFundsOriginal";
    
    public static final String CHART_DATA_BEAN = "ChartDataBean";
    
    public static final String PERFORMANCE_CHART_PARMS = "fundPerformanceParameters";
    
    // Forward instructions
    
    public static final String VIEW = "view";
    
    public static final String NEXT = "next";
    
    public static final String RESET = "reset";
    
    
    // LS multiple class phase1 fund series description - or shall we now call it fund class
    // description

    public static final String FUND_SERIES_DESCRIPTION_RETAIL = "John Hancock Series";// RET US

    public static final String FUND_SERIES_DESCRIPTION_RETAIL_NY = "John Hancock New York Series";// RET
                                                                                                    // NY

    public static final String FUND_SERIES_DESCRIPTION_VENTURE = "Venture Series"; // IFP, VRS us
                                                                                    // and ny

    public static final String FUND_SERIES_DESCRIPTION_HYBRID = "John Hancock Series and Venture Series"; // HYB
                                                                                                            // US

    public static final String FUND_SERIES_DESCRIPTION_HYBRID_NY = "John Hancock New York Series and Venture Series"; // HYB
                                                                                                                        // NY

    public static final String FUND_SERIES_DESCRIPTION_HYBRID_BD = "John Hancock Series and Venture Series"; // HYB
                                                                                                                // BD
                                                                                                                // US

    public static final String FUND_SERIES_DESCRIPTION_HYBRID_BD_NY = "John Hancock New York Series and Venture Series"; // HYB
                                                                                                                            // BD
                                                                                                                            // NY

    public static final String FUND_SERIES_DESCRIPTION_MULTICLASS = "JH Signature"; // MC

    public static final String FUND_SERIES_DESCRIPTION_MULTICLASS_NY = "JH Signature New York"; // MC
                                                                                                // NewYork
    public static final String BD_PRODUCT_ID = "ARABD";

    public static final String BD_PRODUCT_NY_ID = "ARABDY";

    public static final String[] IN_FUND_TYPE = { "IN", "in" ,"INDEX" };

    public static final String[] GA_FUND_TYPE = { "GA", "ga" ,"GUARANTEED"};

    public static final String[] PBA_FUND_TYPE = { "PBA", "NPB" };

    public static final String DUMMY_URL = "http://";
    
    public static final String SITEMODE_USA = "usa";

    public static final String SITEMODE_NY = "ny";

    public static final String SITE_MODE = "java:comp/env/siteMode";
    
    public static final String US = "US";
    
    public static final String NY = "NY";
    
    public static final String HYPHON_SYMBOL = "-";
    
    public static final String AMPERSAND_SYMBOL = "&";
    
    public static final String SLASH_SYMBOL = "/";
    
    public static final String SPACE_SYMBOL = "";
    
    public static final String SINGLE_SPACE_SYMBOL = " ";
    
    public static final String COLON_SYMBOL = ":";
    
    public static final String SEMICOLON_SYMBOL = ";";
    
    public static final String PERCENT_SYMBOL = "%";
    
    public static final String BRACKET_OPEN_SYMBOL = "(";

    public static final String BRACKET_CLOSE_SYMBOL = ")";
    
    public static final String COMMA_SYMBOL = ",";

    public static final int NUM_MINUS_1 = -1;

    public static final int NUM_23 = 23;
    
    public static final String INVALLOCATION_SORT_COLUMN = "IAsortColumn";

    public static final String INVALLOCATION_SORT_DIRECTION = "IAsortDirection";
    
    // Asset Allocation (Contract page)
    public static final class AssetAllocationPieChart {
        public static final String COLOR_LIFECYCLE = FundInfoCache.getRiskCategoryBkgColor("LC");
        public static final String COLOR_AGRESSIVE = FundInfoCache.getRiskCategoryBkgColor("AG");
        public static final String COLOR_GROWTH = FundInfoCache.getRiskCategoryBkgColor("GR");
        public static final String COLOR_GROWTH_INCOME = FundInfoCache
                .getRiskCategoryBkgColor("GI");
        public static final String COLOR_INCOME = FundInfoCache.getRiskCategoryBkgColor("IN");
        public static final String COLOR_CONSERVATIVE = FundInfoCache.getRiskCategoryBkgColor("CN");
        public static final String COLOR_PBA = "#000000";
        public static final String COLOR_BORDER = "#EAEAEA";
        public static final String COLOR_WEDGE_LABEL = "#FFFFFF";
        public static final String COLOR_WEDGE_LABEL_DARK = "#000000";
        public static final String[] COLOR_WEDGES = new String[] { COLOR_LIFECYCLE,
                COLOR_AGRESSIVE, COLOR_GROWTH, COLOR_GROWTH_INCOME, COLOR_INCOME,
                COLOR_CONSERVATIVE, COLOR_PBA };
    }
    

    //Contract Snapshot page
    public static final class ParticipantStatusPieChart {

    public static final String COLOR_ACTIVE_AND_CONTRIBUTING = "#115A3B";

    public static final String COLOR_ACTIVE_NOT_CONTRIBUTING = "#FF650C";

    public static final String COLOR_INACTIVE_WITH_BALANCE = "#527394";

    public static final String COLOR_ACTIVE = "#115A3B";

    public static final String COLOR_INACTIVE_UNINVESTED_MONEY = "#DDE65B";

    public static final String COLOR_INACTIVE = "#DDE694";

    public static final String COLOR_ACTIVE_NO_BALANCE = "#AD2320";
    
    public static final String COLOR_ACTIVE_OPTED_OUT = "#EDC558";

    public static final String COLOR_OPTED_OUT_NOT_VESTED = "#DFC4d7";

    public static final String COLOR_OPTED_OUT_ZERO_BALANCE = "#B2DD92";

    }
    public static final String CONTRACT_SNAPSHOT = "contractSnapshot";
    
    public static final String CONTRACT_SNAPSHOT_STATUS_PIECHART = "pieChart";

    public static final String CONTRACT_SNAPSHOT_ASSET_GROWTH = "assetGrowthBarChart";

    public static final String CONTRACT_SNAPSHOT_CONTR_WITHDRAWALS = "contrWithdrawalsBarChart";

    public static final String CONTRACT_SNAPSHOT_RISK_PIECHART = "riskPieChart";

    public static final String CONTRACT_SNAPSHOT_AGE_BELOW_30_PIECHART = "agePieCharts30Below";

    public static final String CONTRACT_SNAPSHOT_AGE_30_39_PIECHART = "agePieCharts30";

    public static final String CONTRACT_SNAPSHOT_AGE_40_49_PIECHART = "agePieCharts40_49";

    public static final String CONTRACT_SNAPSHOT_AGE_50_59_PIECHART = "agePieCharts50_59";

    public static final String CONTRACT_SNAPSHOT_AGE_60_ABOVE_PIECHART = "agePieCharts60_Above";  
    
    public static final String IPSR_CRITERIA_WEIGHTING_PIECHART = "criteriaAndWeightingPieChart";
    
    public static final String IPSR_CRITERIA_DESC = "criteriaDescMap";
    
    public static final String IPSR_NEW_ANNUAL_REVIEW_MONTH = "newAnnualReviewMonthMap";
    
    public static final String IPSR_NEW_ANNUAL_REVIEW_DATE = "newAnnualReviewDateMap";

    /**
     * Participant Account
     */
    public static final String[] RISK_GROUPS = new String[] { FundVO.RISK_LIFECYCLE,
            FundVO.RISK_AGGRESIVE, FundVO.RISK_GROWTH, FundVO.RISK_GROWTH_INCOME,
            FundVO.RISK_INCOME, FundVO.RISK_CONSERVATIVE, FundVO.RISK_PBA };

    // URLs
    public static final String PIE_CHART_APPLET_ARCHIVE = "/assets/unmanaged/applets/pieChartApplet.jar";
    public static final String PARTICIPANT_ACCOUNT_URL = "/bob/participant/participantAccount/";
    public static final String PARTICIPANT_ACCOUNT_MONEY_TYPE_SUMMARY_URL = "/bob/participant/participantAccountMoneyTypeSummary/";
    public static final String PARTICIPANT_ACCOUNT_MONEY_TYPE_DETAILS_URL = "/bob/participant/participantAccountMoneyTypeDetails/";
    public static final String PARTICIPANT_ACCOUNT_NET_CONTRIB_EARNINGS_URL = "/bob/participant/participantAccountNetContribEarnings/";
    public static final String PARTICIPANT_ACCOUNT_NET_DEFERRAL_URL = "/bob/participant/participantAccountNetDeferral/";

	// Defined Benefit URLs
    static final String DB_ACCOUNT_URL = "/bob/db/definedBenefitAccount/";
    static final String DB_ACCOUNT_MONEY_TYPE_SUMMARY_URL = "/bob/db/definedBenefitAccountMoneyTypeSummary/";
    static final String DB_ACCOUNT_MONEY_TYPE_DETAILS_URL = "/bob/db/definedBenefitAccountMoneyTypeDetails/";
	
	// forward names
	public static final String PARTICIPANT_ACCOUNT_FORWARD = "participantAccount";
	public static final String PPT_ACCOUNT_MONEYTYPE_SUMMARY_FORWARD = "participantAccountMoneyTypeSummary";
	public static final String PPT_ACCOUNT_MONEYTYPE_DETAILS_FORWARD = "participantAccountMoneyTypeDetails";
	public static final String PPT_ACCOUNT_NET_DEFERRAL_FORWARD = "participantAccountNetDeferral";
	public static final String PPT_ACCOUNT_NET_CONTRIB_EARNINGS_FORWARD = "participantAccountNetContribEarnings";
	public static final String PPT_DB_ACCOUNT_FORWARD = "definedBenefitAccount";
	public static final String PPT_DB_ACCOUNT_MONEYTYPE_SUMMARY_FORWARD = "definedBenefitAccountMoneyTypeSummary";
	public static final String PPT_DB_ACCOUNT_MONEYTYPE_DETAILS_FORWARD = "definedBenefitAccountMoneyTypeDetails";

	// Date Format
    public static final String DATE_MM_DD_SLASHED = "MM/dd";
	// Amount Format
	public static final String AMOUNT_FORMAT_TWO_DECIMALS = "###,###,##0.00";
	public static final String AMOUNT_FORMAT_SIX_DECIMALS = "###,###,##0.000000";
	public static final String AMT_PATTERN_TWO_TWO_DECIMALS = "###,###,##0.00;(###,###,##0.00)";
	
	// Percentage Format
	public static final String PERCENT_FORMAT = "###.###%";
	public static final String AMOUNT_3DECIMAL_FORMAT = "#0.000";
	
	public static final String HOMEPAGE_FINDER_FORWARD_REDIRECT = "homePageFinderRedirect";
	
	public static final String HOME_URL = "/do/home/";
	
	// ContractsOnWeb
    public static final String SELECT_AMENDMENT = "Select an amendment";
    public static final String IS_CONTRACT_DOC_PRESENT = "isContractDocPresent";
    public static final String CONTRACT_DOCUMENT = "contractDoc";
    public static final String AMENDMENT_DOCUMENT = "amendmentDocs";
    
    // Http MIME (Content) Types
    public static final String MIME_TYPE_PDF = "application/pdf";
    
    public static final String SECONDARY_WINDOW_ERROR_FORWARD = "secondaryWindowError";
    
    public static final String PARTICIPANT_ACCOUNT_URL_BD = "/do/bob/participant/participantAccount/";
    public static final String PARTICIPANT_ACCOUNT_URL_PS = "/do/participant/participantAccount/";
    
    public static final String DOLLAR_SIGN = "$";
    public static final String PERCENTAGE_SIGN = "%";
    
    public static final String SSN_DEFAULT_VALUE = "000000000";
    
    // PDF related
    public static final String JHRPS_LOGO_FILE = "JHRPS-logo-blue.jpg";

    public static final String FOP_CONFIG_FILE_KEY_NAME = "FOP_Config_File_Key";

    public static final String INCLUDED_XSL_FILES_PATH = "IncludedXSLPath";

    public static final String MAX_CAPPED_ROWS_IN_PDF = "MaximumCappedRowsInPDF";
    
    public static final String PROPERTIES_FILE_NAME = "./ReportsXSL.properties";
    
    public static final String FUND_EVALUATOR_INCLUDED_XSL_FILES_PATH = "FundEvaluatorIncludedXSLPath";
    
    public static final String FUND_EVALUATOR_XSL_FILE_NAME = "FundEvaluatorReport.XSLFile";
    
    public static final String DOUBLE_QUOTES = "\"";
    
    public static final String SINGLE_QUOTES = "'";
    
    public static final String GREEN_CHECK_IMAGE = "Check_green_sm.jpg";
    public static final String YELLOW_CHECK_IMAGE = "Check_yellow_sm.jpg";
    public static final String RED_X_IMAGE = "X_red_sm.jpg";
    public static final String NA_IMAGE = "NA_sm.jpg";
    public static final String X_BLUE_SM_IMAGE = "X_blue_sm.jpg";
    
    public static final String DO = "/do";
 
    // ireports related
    public static final String LIFECYCLE = "Target Date";
    public static final String LIFESTYLE = "Target Risk";
 
    // TPA BOB related
    public static final String TPAP = "TPAP";
    public static final String JHC = "JHC";
    public static final String SUBMITTED_BY_TPA = "Submitted by TPA";
    public static final String CALCULATED_BY_JH = "Calculated by JH";
    //Lifecycle fund family indicator
    public static final String RETIREMENT_CHOICES = "RC";
	public static final String RETIREMENT_LIVING = "RL";
	public static final String AMERICAN_CENTURY_ONE_CHOICEPORTFOLIOS="AM";
	public static final String JH_RETIREMENTTHROUGH_MANAGEDPORTFOLIOS="RM";
	public static final String T_ROWEPRICE_RETIREMENT_PORTFOLIOS="TR";
	public static final String RETIREMENT_LIVING_AND_CHOICES = "Y";
	//Completed withdrawal
	public static final String TRANSACTION_NUMBER = "transactionNumber";
	public static final String PARTICIPANT_ID = "pptId";
	public static final String REPORT_NAME = "withdrawalReport";
	public static final String CONTRACT_TYPE_DB = "isDBContract";
	public static final String LINE_BREAK = System.getProperty("line.separator");
	public static final String COMMA = ",";
	public static final String BLANK_STR="&nbsp;";
	public static final String VERSION_1="V1";
	public static final String VERSION_2="V2";
	public static final String MASK_ACCOUNT_NUMBER="xxxxxxxxxx";
	public static final DecimalFormat twoDecimals = new DecimalFormat("0.00");
	public static final String ERROR_KEYS = CommonEnvironment.getInstance().getErrorKey();
	
	public static final String BD_APPLICATION_ID = "BD";
	public static final String PAYMENT_METHOD_PC="Paid by check";
	public static final DecimalFormat threeDecimals = new DecimalFormat("0.000");
	
	// fee disclosure project
	public static final String FEE_DISCLOSURE_PROJECT = "408(b)(2) Disclosure";

	// Withdrawal on Web
	public static final String PBA_DISBURSEMENT_INDICATOR = "T";
	
	// Cash Account
	public static final String CSV_HEADER_FROM_DATE = "From date";
	public static final String CSV_HEADER_TO_DATE = "To date";
	public static final String CSV_HEADER_CURRENT_BALANCE = "Current balance";	
	
	public static final String IN_PROGRESS = "Transaction in progress";
	
	// IPS Review
	public static final List<String> ipsColorCode = Arrays.asList(new String[] { "#9E1B34",
			"#006233", "#CD5806", "#EBAB00", "#3E6476" });
	public static final String MEDIUM_MDY_DASHED = "MMMdd-yyyy";
	public static final String COUNTRY_NY = "NY";
	public static final String COUNTRY_USA = "USA";
	
	public static final String FOOTNOTE_MORNINGSTAR_PERFORMANCE = "77505";
	public static final String FOOTNOTE_MORNINGSTAR_SYMBOL_EXPLANATION = "72283";
	public static final String FIRST_FOOTNOTE_MORNINGSTAR_PERFORMANCE = "1";
	public static final String SECOND_FOOTNOTE_MORNINGSTAR_SYMBOL_EXPLANATION = "2";
	
	public static final String INFO_ICON_FILE = "info.gif";
	
	public static final String SELECTED_AS_OF_DATE = "selectedAsOfDate";
	
	public static final String MASKED_ACCOUNT_NUMBER = "XXXXXXXXXX";

	// Forms & Warranty certificate

	public static final String CONTRACT_HOLDER = "contractholder_name";

	public static final String CONTRACT_NUMBER = "contract_number";

	public static final String CONTRACT_NUMBER_8DIGIT = "contract_number_8digit";

	public static final String CONTRACT_CLIENT_ID = "client_id_no";

	public static final String AS_OF_DATE = "as_of_date";

	public static final String LIA_DETAILS = "liaDetails";
	
	public static final String LIA_IND_TEXT = "LIA";
	
	public static final String DSTO_STATEMENTS_FWD = "dstoStatements";
    public static final String TRANSACTIS_STATEMENTS_FWD = "transactisStatements";

	public static final String COFID_CONTRACT_DOCUMENTS_KEY="coFidContractDocuments";
	public static final String COFID321_CONTRACT_DOCUMENT = "coFid321contractDoc";
	
	public static final String LIFESTYLE_STRATEGIES = "LS";
	public static final String LIFESTYLE_MANAGED = "LM";
	public static final String LIFESTYLE_VOLATILITY="LV";
	
	//Control Reports
	public static final String NOTICE_MANAGER_PAGE = "PSW_Notice_Manager_Notice_Manager_Alert_Page";
	public static final String ALERT_MESSAGE = "Alert Message Generated";
	public static final String ALERT_DELETE = "PSW_Notice_Manager_Delete_Alert";
	public static final String NOTICE_DELETE = "PSW_Notice_Manager_Delete_Notice";
	public static final String NOTICE_DOWNLOAD= "PSW_Notice_Manager_Download_Package";
	public static final String UPLOAD_AND_SHARE_PAGE = "PSW_Notice_Manager_Upload_&_Share_Page";
	public static final String BUILD_YOUR_PACKAGE_PAGE = "PSW_Notice_Manager_Build_Your_Package_Page";
	public static final String ADD_PAGE = "PSW_Notice_Manager_Add_Notice_Page";
	public static final String EDIT_PAGE = "PSW_Notice_Manager_Edit_Notice_Page";
	public static final String ORDER_STATUS_PAGE = "PSW_Notice_Manager_Order_Status_Page";
	public static final String TERMS_AND_CONDITIONS_PAGE = "PSW_Notice_Manager_Terms_Of_Use_Page";
	public static final String CHANGE_HISTORY_PAGE = "PSW_Notice_Manager_Contract_Change_History_Page";
	
	
	public static final String DOWNLOAD_COLUMN_HEADING = "";

	public static final String WEDGE1 = "wedge1";

	public static final String WEDGE2 = "wedge2";

	public static final String WEDGE3 = "wedge3";

	public static final String WEDGE4 = "wedge4";

	public static final String WEDGE5 = "wedge5";

	public static final String WEDGE_FONT_COLOR = "#FFFFFF";

    public static final String MONTHLY_COLOR_WEDGE_LABEL = "#0099FF";

    public static final String QUARTERLY_COLOR_WEDGE_LABEL = "#CC0000";

    public static final String SEMI_ANNUALLY_COLOR_WEDGE_LABEL = "#66CC33";

    public static final String ANNUALLY_COLOR_WEDGE_LABEL = "#663366";

    public static final String ADHOC_COLOR_WEDGE_LABEL = "#66FFFF";

    public static final String VIEWING_PREFERENCE = "1";

    public static final int NUMBER_0 = 0;
    
    public static final String COLOR_BORDER = "#EAEAEA";
    
    public static final String ALERT_FREQUENCY_PIECHART = "pieChart";
    
    public static final String ALERT_REPORT_PAGE = "alertsReportPage";
    
    public static final String INVALID_DATE = "Invalid Date";
    
    public static final String NOTICE_DISTRIBUTION_BY_DUE_DATE = "Notice Distribution by Due Date";
    
    public static final String Q1_MONTH = "Q1 Month";
    
    public static final String Q2_MONTH = "Q2 Month";
    
    public static final String Q3_MONTH = "Q3 Month";
    
    public static final String Q4_MONTH = "Q4 Month";
    
    public static final String NO_OF_UNIQUE_USERS_SETTINGS_ALERTS = "No. of Unique users setting alerts";
    
    public static final String TOTAL_ALERTS = "Total alerts (Urgent + Normal)";
    
    public static final String NORMAL_ALERTS = "Normal alerts";
    
    public static final String URGENT_ALERTS = "Urgent alerts";
    
    public static final String NO_OF_DELETED_ALERTS = "No. of deleted alerts";
    
    public static final String USERS = "Users";
    
    public static final String AVERAGE_ALERTS_USERS = "Average alerts/user";
    
    public static final String DISTRIBUTION_MONTH_SELECTED_BY_USERS = "Distribution month selected by users";
    
    public static final String ALERT_FREQUENCIES = "Alert frequencies";
    
    public static final String NUMBER_OF_ALERT_FREQUENCY = "Number of alerts/frequency";
    
    public static final String PLANSPONSOR_WEB_SITE_REPORT_PAGE = "planSponsorWebsiteReportPage";
    
    public static final String USER_STATISTICS = "Usage statistics";
    
    public static final String INTERMEDIARY_CONTRACT = "Intermediary Contract";
    
    public static final String TOTAL_CARE_TPA = "TotalCare TPA";
    
    public static final String TPA = "TPA";
    
    public static final String PLAN_SPONSOR = "Plan Sponsor";
    
    public static final String PAGES_VISITED = "Pages visited";
    
    public static final String ALERT_REPORT_ACTIONFORM = "alertsReportForm";
    
    public static final String DEFAULT = "default";
    
    public static final String ALL_VISITS = "All visits";
    
    public static final String REPEAT_VISITORS = "Repeat visitors";
    
    public static final String UNIQUE_VISITORS = "Unique visitors";
    
    public static final String MONTH_WITH_MOST_VISITS = "Month with most visits";
    
    public static final String NUMBER_OF_DOCUMENT_VIEWS = "No. of document views";
    
    public static final String PARTICIPANT_WEBSITE_REPORT_PAGE = "participantWebsiteReportPage";
    
}