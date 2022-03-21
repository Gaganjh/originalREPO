package com.manulife.pension.platform.web.fap.constants;

import java.util.regex.Pattern;

import com.manulife.pension.platform.web.CommonConstants;

/**
 * constants that are specific to the Funds & Performance module
 * 
 * @author ayyalsa
 * 
 */
public interface FapConstants extends CommonConstants {

	/**
	 * Warnings, Information and Error key
	 */
    public static final String BDW_WARNING_MESSAGES = "bdWarnings";
	public static final String BDW_INFORMATION_MESSAGES = "bdMessages";
	/***/
	
	/**
	 * Value Objects
	 */
	public static final String VO_FUNDS_AND_PERFORMANCE = "fundsAndPerformance";
	public static final String VO_TAB_VALUE_OBJECT = "fundValueObject";
	public static final String VO_CURRENT_TAB = "currentTabObject";
	public static final String COLUMNS_INFO_OBJECT = "columnsInfo";
	public static final String VO_FUND_SCORECARD_SELECTION = "fundScoreCardSelection";
	
	/***/
	
	/**
	 * Request/Session Attributes
	 */
	public static final String ADVANCE_FILTER_USED = "advanceFilterUsed";
	public static final String LAST_EXECUTED_FILTER_CRITERIA = "lastExecutedFilterCriteria";
	public static final String OTHERS = "others";
	public static final String FILTERED_FUNDS_LIST = "filteredFundsList";
	public static final String SELECTED_SHORTLIST_OPTIONS = "selectedShortlistOptions";
	public static final String FUND_CHECK_ASOFDATE = "fundCheckAsOfDate";
	/***/

	/**
	 *  Action forwards 
	 */
	public static final String FORWARD_CUSTOM_QUERY = "customQuery";
	public static final String FORWARD_CONTINUE= "continue";
	public static final String FORWARD_ENABLE_TABS= "enableTabs";
	public static final String FORWARD_DISPLAY_TABS= "displayTabs";
	public static final String FORWARD_SESSION_EXPIRED = "sessionExpired";
	public static final String FORWARD_ASSET_CLASS_DEFINITIONS = "assetClassDefinitions";
	/***/
	
	/**
	 *  AS of Date Contexts 
	 */
	public static final String CONTEXT_UV = "UV";
	public static final String CONETXT_ROR = "ROR";
	public static final String CONTEXT_MET = "MET";
	public static final String CONTEXT_MSP = "MSP";
	public static final String CONTEXT_MKP = "MKP";
	public static final String CONTEXT_DEV = "DEV";
	public static final String CONTEXT_FCK = "FCK";
	public static final String CONTEXT_FER = "FER";
	public static final String CONTEXT_RORQE = "RORQE";
	public static final String CONTEXT_METQE = "METQE";
	public static final String CONTEXT_DEVQE = "DEVQE";
	/***/
	
	public static final String BASE_FUND_ARRAY = "baseFundArray";
	public static final String FUND_INFO_AND_SELECT_QUERY_OPTIONS = "fundInfoAndSelectQueryOptions";
	public static final String SESSION_EXPIRED = "sessionExpired";
	public static final String FILTER_FUND_IDS = "fundIds";
	public static final String MEESAGES = "messages";
	public static final String INNER_SELECT_OPTIONS = "innerSelectOptions";
	public static final String SHORT_LIST_INNER_SELECT_OPTIONS = "shortListinnerSelectOptions";
	public static final String CONTRACT_SERACH_RESULTS = "contractSearchResults";
	public static final String SLOSH_BOX_SORTING_RESULTS = "sloshBoxSortingResults";

	// Keys that are used to store the objects in cache
	public static final String BASE_FILTER_VIEW_OPTION_LIST = "viewOptionList";
	public static final String BASE_FILTER_VIEW_NO_CONTRACT_FUNDS = "viewOptionListNoContractFunds";
	public static final String BASE_FILTER_FUND_CLASS_LIST = "fundClassList";
	public static final String BASE_FILTER_GROUP_BY_LIST = "groupByList";
	public static final String OPTIONAL_FILTER_LIST_ALL_FUNDS = "optionalFilterListAllFunds";
	public static final String OPTIONAL_FILTER_LIST_CONTRACT_FUNDS = "optionalFilterListContractFunds";
	public static final String ASSET_CLASS_LIST = "assetClassList";
	public static final String ASSET_CLASS_LIST_NO_GIFL = "assetClassListNoGifl";
	public static final String RISK_CATEGORY_LIST = "riskCategoryList";
	public static final String RISK_CATEGORY_LIST_NO_GIFL = "riskCategoryListNoGifl";
	public static final String SHORT_LIST = "shortList";
	public static final String CUSTOM_QUERY_OPTIONS_LIST = "customQueryoptionsList";
	public static final String SAVED_LIST = "savedList";
	public static final String ALL_FUND_ID_LIST = "allFundIdList";

	/* Generic constants * */
	public static final String INSERT_KEYWORD = "insert";
	public static final String REMOVE_KEYWORD = "remove";
	public static final String AT_LAST_KEYWORD = "AtLast";

	/*
	 * Constants used for the tabs
	 */
	// Column Headings - Level 1
	public static final String COLUMN_HEADINGS_LEVEL_1 = "ColumnHeadingsLevel1";

	// Column Headings - Level 2
	public static final String COLUMN_HEADINGS_LEVEL_2 = "ColumnHeadingsLevel2";
	
	// Column Headings - Level 3
	public static final String COLUMN_HEADINGS_LEVEL_3 = "ColumnHeadingsLevel3";
		
	// Column Headings - Level 4
	public static final String COLUMN_HEADINGS_LEVEL_4 = "ColumnHeadingsLevel4";
	

	// Fund Information Tab Columns key
	public static final String FUND_INFO_TAB_COLUMNS_MAP = "fundInfoTabColumnsMap";

	// Prices & YTD Tab Columns key
	public static final String PRICES_YTD_TAB_COLUMNS_MAP = "pricesYTDTabColumnsMap";

	// Prices & YTD Tab Columns key
	public static final String PERFORMANCE_FEES_TAB_MONTHLY_COLUMNS_MAP = "monthlyPerformanceAndFees";
	public static final String PERFORMANCE_FEES_TAB_QUARTERLY_COLUMNS_MAP = "quarterlyPerformanceAndFees";

	// Standard Deviation Tab Columns key
	public static final String STANDARD_DEVIATION_TAB_COLUMNS_MAP = "standardDeviationTabColumnsMap";

	// Fund Char I Tab Columns key
	public static final String FUND_CHAR_I_TAB_COLUMNS_MAP = "fundCharITabColumnsMap";

	// Fund Char I Tab Columns key
	public static final String FUND_CHAR_II_TAB_COLUMNS_MAP = "fundCharIITabColumnsMap";

	// Morningstar Tab Columns key
	public static final String MORNINGSTAR_TAB_COLUMNS_MAP = "morningstarTabColumnsMap";

	// Fund Check Tab Columns key
	public static final String FUND_CHECK_TAB_COLUMNS_MAP = "fundCheckTabColumnsMap";
	
	// Fund Check Tab Columns key
	public static final String FUNDSCORECARD_TAB_COLUMNS_MAP = "fundscorecardTabColumnsMap"; 


	// Tab navigation title
	public static final String FUND_INFORMATION_TAB = "Fund <br/>Information";
	public static final String PRICES_YTD_TAB = "Prices & <br/>YTD";
	public static final String PERFORMANCE_FEES_TAB = "Performance & <br/>Fees";
	public static final String STANDARD_DEVIATION_TAB = "Standard <br/>Deviation";
	public static final String FUND_CHAR_I_TAB = "Fund <br/>Characteristics I";
	public static final String FUND_CHAR_II_TAB = "Fund <br/>Characteristics II";
	public static final String MORNINGSTAR_TAB = "Morningstar <br/>&nbsp;";
	public static final String FUNDSCORECARD_TAB = "JH Signature <br/> Fund Scorecard";


	// TPA Tab navigation title
	public static final String TPA_FUND_INFORMATION_TAB = "Fund <br/>Information";
	public static final String TPA_PRICES_YTD_TAB = "Prices & <br/>YTD";
	public static final String TPA_PERFORMANCE_FEES_TAB = "Performance & Fees";
	public static final String TPA_STANDARD_DEVIATION_TAB = "Standard <br/>Deviation";
	public static final String TPA_FUND_CHAR_I_TAB = "Fund Characteristics I";
	public static final String TPA_FUND_CHAR_II_TAB = "Fund Characteristics II";
	public static final String TPA_MORNINGSTAR_TAB = "Morningstar";
	public static final String TPA_FUNDSCORECARD_TAB = "JH Signature <br/> Fund Scorecard";

	
	// Tab navigation id
	public static final String FUND_INFORMATION_TAB_ID = "fundInformation";
	public static final String PRICES_YTD_TAB_ID = "pricesAndYTD";
	public static final String PERFORMANCE_FEES_TAB_ID = "performanceAndFees";
	public static final String STANDARD_DEVIATION_TAB_ID = "standardDeviation";
	public static final String FUND_CHAR_I_TAB_ID = "fundCharacteristics1";
	public static final String FUND_CHAR_II_TAB_ID = "fundCharacteristics2";
	public static final String MORNINGSTAR_TAB_ID = "morningstar";
	public static final String FUNDSCORECARD_TAB_ID = "fundScorecard";
	// Disclosure id
    public static final String FAP_GENERIC_VIEW_ID = "genericView";
    public static final String FAP_CONTRACT_VIEW_ID = "contractView";


	// Tab navigation URL
	public static final String FUND_INFORMATION_URL = "/do/fap/fapByFundInformation/";
	public static final String PRICES_YTD_URL = "/do/fap/fapByPricesAndYTD/";
	public static final String PERFORMANCE_FEES_URL = "/do/fap/fapByPerformanceAndFees/";
	public static final String STANDARD_DEVIATION_URL = "/do/fap/fapByStandardDeviation/";
	public static final String FUND_CHAR_I_URL = "/do/fap/fapByFundCharI/";
	public static final String FUND_CHAR_II_URL = "/do/fap/fapByFundCharII/";
	public static final String MORNINGSTAR_URL = "/do/fap/fapByMorningstar/";
	public static final String FUNDSCORECARD_URL = "/do/fap/fapByFundScorecard/";


	/*
	 * 
	 */
	public static final String COMMON_LEVEL2COLUMNS1 = "commonColumns1";
	public static final String COMMON_LEVEL2COLUMNS2 = "commonColumns2";
	public static final String COMMON_LEVEL2COLUMNS3 = "commonColumns3";
	public static final String FUND_INFORMATION_LEVEL2COLUMNS = "FundInformation";
	public static final String PERFORMANCE_AND_FEES_MONTHLY_LEVEL2COLUMNS = "PerformanceAndFeesMonthly";
	public static final String PERFORMANCE_AND_FEES_QUATERLY_LEVEL2COLUMNS = "PerformanceAndFeesQuarterly";
	public static final String PERFORMANCE_AND_FEES_COMMON_LEVEL2COLUMNS = "PerformanceAndFeesCommon";
	public static final String PRICES_YTD_LEVEL2COLUMNS = "PricesAndYTD";
	public static final String STANDARD_DEVIATION_LEVEL2COLUMNS = "StandardDeviation";
	public static final String FUND_CHAR1_LEVEL2COLUMNS = "FundChar1";
	public static final String FUND_CHAR2_LEVEL2COLUMNS = "FundChar2";
	public static final String MORNINGSTAR_LEVEL2COLUMNS = "Morningstar";
	public static final String PRICES_YTD_LEVEL2COLUMNS_CSV = "PricesAndYTDCsv";
	public static final String FUNDSCORECARD_LEVEL2COLUMNS = "FundScorecard";
		

	/*
	 * Constants - Related to Custom Query Filter
	 */
	// Data types
	public static final String DATA_TYPE_ALPHA_NUMBERIC = "alphaNumeric";
	public static final String DATA_TYPE_DATE = "date";
	public static final String DATA_TYPE_NUMERIC = "numeric";
	public static final String BASIC_DATA_TYPE_DATE = "D";
	public static final String BASIC_DATA_TYPE_ALPHA = "A";
	public static final String BASIC_DATA_TYPE_NUMERIC = "N";
	public static final String BASIC_DATA_TYPE_INTEGER_AS_ALPHA = "IA";
	public static final String BASIC_DATA_TYPE_MSTAR = "MSTAR";
	public static final String BASIC_DATA_TYPE_FI360 = "FI360";
	public static final String BASIC_DATA_TYPE_RPAG = "RPAG";

	public static final String ASSET_CLASS_LIST_FOR_CQ = "assetClassListForCQ";
	public static final String ASSET_CLASS_LIST_FOR_CQ_NO_GIFL = "assetClassListForCQNoGIFL";
	public static final String RISK_CATEGORY_LIST_FOR_CQ = "riskCategoryListForCQ";
	public static final String RISK_CATEGORY_LIST_FOR_CQ_NO_GIFL = "riskCategoryListForCQNoGIFL";
	
	// Property key in the file
	public static final String QUERY_FIELD_GROUP_PROPERTY_KEY = "query.field.group.count";
	public static final String LOGIC_KEY = "logic";
	public static final String OPERATOR_KEY = "operators";
	public static final String FIELD_KEY = "fields";

	public static final String LOGIC_AND = "AND";
	public static final String LOGIC_OR = "OR";

	public static final String VIEW_BY_ASSET_CLASS = "2";
	public static final String VIEW_BY_RISK_CATEGORY = "3";

	// For the Fund Short List options
	public static final String FUND_MENU_PACKAGE_SERIES = "fundMenu";
	public static final String SHORT_LIST_TYPE = "shortListType";
	public static final String INCOME_FUND = "conservativeFund";
	public static final String ASSET_ALLOCATION_GROUP = "assetAllocationGroup";

	// options constants for shortlist
	public static final String SHORTLIST_FUND_MENU_SELECT = "shortlistFundMenuSelect";
	public static final String SHORT_LIST_TYPE_SELECT = "shortlistTypeSelect";
	public static final String SHORT_LIST_INCOME_FUND_SELECT = "conservativeFundSelect";
	public static final String SHORT_LIST_ALLOCATION_GROUP_SELECT = "allocationGroupSelect";

	/*
	 * For the Custom Query Parser
	 */
	public static final String SQL_OPERATOR_LIKE = "LIKE";
	public static final String USER_DATA_FIELD_DELIMITER = "|";
    public static final String USER_DATA_ROW_DELIMITER = "[$$]";
    public static final String TYPE_SAVED_CUSTOM_QUERY = "CQ";
    public static final String TYPE_SAVED_FUND_LIST = "FL";
    
    /*
	 * For the My Custom Queries
	 */
	public static final String DISPLAY_QUERY_RESULTS = "displayQueryResults";
	public static final String EDIT_CRITERIA = "editCriteria";
	public static final String DELETE_QUERY = "deleteQuery";
	public static final String DISPLAY_LIST = "displayList";
	public static final String DELETE_LIST = "deleteList";
	
	/*
	 * request/session attributes
	 */
	public static final String ATTR_CONTRACT_NUMBER = "contractNum";
	public static final String ATTR_GROUP_BY = "groupby";
	public static final String ATTR_TAB_SELECTED = "tabselected";
	
	/*
	 * Pattern to validate the custom query name 
	 */
	public static final Pattern SAVE_NAME_PATTERN = Pattern.compile("[A-Za-z0-9-.\\s]+");
	
	/*
	 * For CSV
	 */
	public static final String CONTENT_DISPOSITION_TEXT = "Content-Disposition";
	public static final String CSV_TEXT = "text/csv";
	public static final String ATTACHMENT_TEXT = "attachment; filename=";
	public static final String CSV_FILE_NAME = "Generic_Funds&Performance";
	public static final String COMMA = ",";
	public static final String LINE_BREAK = System.getProperty("line.separator");
	public static final String DEFINED_BENEFIT_CONTRACT_PRODUCT_CODE_US = "DB06";
	public static final String DEFINED_BENEFIT_CONTRACT_PRODUCT_CODE_NY = "DBNY06";
	public static final String XSL_FILE_NAME = "fap.XSLFile";
	public static final String CSV_EXTENSION = ".csv";
	
	/**
	 * For the page drop-down or list box labels and values
	 */
	// LABELS: for the base filter drop-downs
	public static final String BASE_FILTER_ALL_FUNDS = "All Funds";
	public static final String BASE_FILTER_CONTRACT_FUNDS = "Contract Funds";
	// VALUES: for the base filter drop-downs
	public static final String BASE_FILTER_ALL_FUNDS_KEY = "allFunds";
	public static final String BASE_FILTER_CONTRACT_FUNDS_KEY = "contractFunds";

	// LABELS: default select option for drop down
	public static final String PLEASE_SELECT = "Please select:";
	public static final String DEFAULT_SELECT = "-select-";
	// VALUES: default select option for drop down
	public static final String NOT_APPLICABLE = "N/A";
	public static final String BLANK = "";

	// LABELS: constants for the optional filter drop-down
	public static final String ALL_FUNDS_FILTER = "All Available Funds";
	public static final String RETAIL_FUNDS_FILTER = "Retail Funds";
	public static final String SUB_ADVISED_FUNDS_FILTER = "Sub-Advised Funds";
	public static final String CONTRACT_AVAILABLE_FUNDS_FILTER = "Funds Available to Contract";
	public static final String CONTRACT_SELECTED_FUNDS_FILTER = "Funds Selected to Contract";
	public static final String ASSET_CLASS_FILTER = "Asset Classes";
	public static final String RISK_CATEGORY_FILTER = "Risk/Return Categories";
	public static final String SHORTLIST_FILTER = "Shortlist";
	public static final String CUSTOM_QUERY_FILTER = "Custom Query";
	public static final String SEARCH_FUND_FILTER = "Search for a Fund";
	public static final String SAVED_LIST_FILTER = "My Saved Lists";
	public static final String SAVED_CUSTOM_QUERIES_FILTER = "My Custom Queries";
	
	// VALUES: constants for the optional filter drop-down
	public static final String ALL_FUNDS_FILTER_KEY = "filterAllAvailableFunds";
	public static final String RETAIL_FUNDS_FILTER_KEY = "filterRetailFunds";
	public static final String SUB_ADVISED_FUNDS_FILTER_KEY = "filterSubadvisedFunds";
	public static final String CONTRACT_AVAILABLE_FUNDS_FILTER_KEY = "filterContractAvailableFunds";
	public static final String CONTRACT_SELECTED_FUNDS_FILTER_KEY = "filterContractSelectedFunds";
	public static final String ASSET_CLASS_FILTER_KEY = "filterAssetClassFunds";
	public static final String RISK_CATEGORY_FILTER_KEY = "filterRiskCategoryFunds";
	public static final String SHORTLIST_FILTER_KEY = "filterShortlistFunds";
	public static final String CUSTOM_QUERY_FILTER_KEY = "filterCustomQueryFunds";
	public static final String SEARCH_FUND_FILTER_KEY = "filterFundSearch";
	public static final String SAVED_LIST_FILTER_KEY = "filterMySavedLists";
	public static final String SAVED_CUSTOM_QUERIES_FILTER_KEY = "filterMySavedQueries";
	public static final String REPORT_AND_DOWNLOAD_FILTER_KEY = "reportsAndDownloads";

	/*
	 * LABELS & VLAUES : constants for the Custom Query option
	 */
	// LABELS : Constants for Logic Type
	public static final String LOGIC_TYPE_AND = "AND";
	public static final String LOGIC_TYPE_OR = "OR";
	// VALUES : Constants for Logic Type
	public static final String LOGIC_TYPE_AND_KEY = "and";
	public static final String LOGIC_TYPE_OR_KEY = "or";

	// LABELS : Constants for Fund Check Evaluation drop-down
	public static final String STRONG = "Strong";
	public static final String SATISFACTORY = "Satisfactory";
	public static final String UNSATISFACTORY = "Unsatisfactory";
	public static final String TO_BE_REMOVED = "To be removed";
	public static final String FUND_EVALUATION_NOT_APPLICABLE = "N/A";
	
	
	// LABELS : Constants for FI360 & RPAG drop-down
	public static final String FI360_FIRST_QUARTILE = "0 - 25";
	public static final String FI360_FIRST_QUARTILE_KEY = "FirstQuartile";
	public static final String FI360_SECOND_QUARTILE = "26 - 50";
	public static final String FI360_SECOND_QUARTILE_KEY = "SecondQuartile";
	public static final String FI360_THIRD_QUARTILE = "51 - 75";
	public static final String FI360_THIRD_QUARTILE_KEY = "ThridQuartile";
	public static final String FI360_FOURTH_QUARTILE = "76 - 100";
	public static final String FI360_FOURTH_QUARTILE_KEY = "FourthQuartile";
	public static final String RPAG_RATING_0 = "0";
	public static final String RPAG_RATING_1 = "1";
	public static final String RPAG_RATING_2 = "2";
	public static final String RPAG_RATING_3 = "3";
	public static final String RPAG_RATING_4 = "4";
	public static final String RPAG_RATING_5 = "5";
	public static final String RPAG_RATING_6 = "6";
	public static final String RPAG_RATING_7 = "7";
	public static final String RPAG_RATING_8 = "8";
	public static final String RPAG_RATING_9 = "9";
	public static final String RPAG_RATING_10 = "10";
	public static final String NOT_SCORED = "Not Scored";

	// VALUES : Constants for Fund Check Evaluation drop-down
	public static final String TO_BE_REMOVED_KEY = "4";
	public static final String STRONG_KEY = "3";
	public static final String SATISFACTORY_KEY = "2";
	public static final String UNSATISFACTORY_KEY = "1";
	public static final String FUND_EVALUATION_NOT_APPLICABLE_KEY = "0";

	// LABELS : Constants for Overall star rating drop-down
	public static final String STAR_RATING_NOT_RATED = "0";
	public static final String STAR_RATING_1 = "1";
	public static final String STAR_RATING_2 = "2";
	public static final String STAR_RATING_3 = "3";
	public static final String STAR_RATING_4 = "4";
	public static final String STAR_RATING_5 = "5";
	
	// VALUES : Constants for Overall star rating drop-down
	public static final String STAR_RATING_NOT_RATED_KEY = " ";
	public static final String STAR_RATING_1_KEY = "1";
	public static final String STAR_RATING_2_KEY = "2";
	public static final String STAR_RATING_3_KEY = "3";
	public static final String STAR_RATING_4_KEY = "4";
	public static final String STAR_RATING_5_KEY = "5";
	
	// LABELS & VALUES: Constants for OPERATOR
	public static final String OPERATOR_GREATER_THAN = ">";
	public static final String OPERATOR_LESS_THAN = "<";
	public static final String OPERATOR_GREATER_THAN_OR_EQUAL_TO = ">=";
	public static final String OPERATOR_LESS_THAN_OR_EQUAL_TO = "<=";
	public static final String OPERATOR_EQUALS = "=";
	public static final String OPERATOR_NOT_EQUAL_TO = "Not =";
	public static final String OPERATOR_CONTAINS = "Contains";
	public static final String OPERATOR_EXCLUDES = "Excludes";

	/*
	 * LABELS: constants for the short-list filter drop-down
	 */
	// Fund Menu drop down labels
	public static final String FUND_MENU_ALL_FUNDS_FILTER = "All Funds";
	public static final String FUND_MENU_RETAIL_FUNDS_FILTER = "Retail";
	public static final String FUND_MENU_SUB_ADVISED_FUNDS_FILTER = "Sub-Advised";
	// Fund Menu drop down values
	public static final String FUND_MENU_ALL_FUNDS_FILTER_KEY = "HYB";
	public static final String FUND_MENU_RETAIL_FUNDS_FILTER_KEY = "RET";
	public static final String FUND_MENU_SUB_ADVISED_FUNDS_FILTER_KEY = "VEN";
	// Shot-list type drop down labels
	public static final String SHORTLIST_TYPE_LOWEST_COST = "Lowest cost";
	public static final String SHORTLIST_TYPE_TOP_PREFORMER = "Top performer";
	public static final String SHORTLIST_TYPE_3_YR_PERFORMANCE = "3 year performance";
	public static final String SHORTLIST_TYPE_5_YR_PERFORMANCE = "5 year performance";
	// Shot-list type drop down values
	public static final String SHORTLIST_TYPE_LOWEST_COST_KEY = "VALUE";
	public static final String SHORTLIST_TYPE_TOP_PREFORMER_KEY = "PERFORM";
	public static final String SHORTLIST_TYPE_3_YR_PERFORMANCE_KEY = "3YR";
	public static final String SHORTLIST_TYPE_5_YR_PERFORMANCE_KEY = "5YR";
	
	//	 Conservative funds drop down labels
	public static final String CONSERVATIVE_FUNDS_MONEY_MARKET_FUND = "Money Market fund";
	public static final String CONSERVATIVE_FUNDS_STABLE_VALUE_FUND = "Stable Value fund";
	// Conservative funds drop down values
	public static final String CONSERVATIVE_FUNDS_MONEY_MARKET_FUND_KEY = "MM";
	public static final String CONSERVATIVE_FUNDS_STABLE_VALUE_FUND_KEY = "SV";
	
	/* 
	 * For the Reports And Down-loads drop-down
	 */
	// Keys
	public static final String DOWNLOAD_CSV = "downloadCsv";
	public static final String DOWNLOAD_CSV_ALL = "downloadCsvAll";
	public static final String PRINT_CURRENT_VIEW_PDF = "printPdf";
	public static final String EXPENSE_RATIO_REPORT = "expenseRatioReport";
	public static final String FUND_CHARACTERISITICS_REPORT = "fundCharacteristicsReport";
	public static final String INVESTEMNT_RETURNS_AND_EXPENSE_RATIO_REPORT = "investmentReturnAndExpenseRatioReport";
	public static final String INVESTEMNT_RETURNS_AND_STANDARD_DEVIATION_REPORT = "investmentReturnAndStandardDeviationReport";
	public static final String MORNINGSTAR_RATINGS_AND_TICKR_SYMBOLS_REPORT = "morningstarRatingsAndTickrSymbolReport";
	public static final String MARKET_INDEX_REPORT = "marketIndexReport";
	// labels
	public static final String DEFAULT_REPORTS_VALUE = "N/A";
	public static final String NON_REPORTS_VALUE = "";
	public static final String DEFAULT_REPORTS_TITLE = "Please select:";
	public static final String STANDARD_REPORTS_TITLE = "Standard Reports – PDF:";
	public static final String OTHER_REPORTS_TITLE = "Other Reports – PDF:";
	public static final String DOWNLOAD_CSV_TITLE = "Download current view to CSV";
	public static final String DOWNLOAD_CSV_ALL_TITLE = "Download all tabs to CSV";
	public static final String PRINT_CURRENT_VIEW_PDF_TITLE = "Print current view to PDF";
	public static final String EXPENSE_RATIO_REPORT_TITLE = "Expense Ratios";
	public static final String FUND_CHARACTERISITICS_REPORT_TITLE = "Fund Characteristics";
	public static final String INVESTEMNT_RETURNS_AND_EXPENSE_RATIO_REPORT_TITLE = "Investment Returns & Expense Ratios";
	public static final String INVESTEMNT_RETURNS_AND_STANDARD_DEVIATION_REPORT_TITLE = "Investment Returns & Standard Deviation";
	public static final String MORNINGSTAR_RATINGS_AND_TICKR_SYMBOLS_REPORT_TITLE = "Morningstar Ratings & Ticker Symbols";
	public static final String MARKET_INDEX_REPORT_TITLE = "Market Index";
	/**  ##############  */
	
	public static final String MODIFIED_LINEUP = "modifiedLineUp";
	public static final String VIEW = "View";
	public static final String CONTRACT_FUNDS = "contractFunds";
	public static final String CONTRACT_NUMBER = "Contract";
	public static final String FUND_MENU = "fundMenu";
	public static final String CONTRACT_NAME = "ContractName";
	public static final String CLASS = "Class";
	public static final String GROUP_BY = "GroupBy";
	public static final String CONTRACT_DEAILS = "ContractDetails";
	
	
	public static final String FAP_ACTION_FORM_IN_SESSION = "fapFormInSession";
	
	public static final String GIFL_SELECT_VERSION = "G03";

    public static final String PS_APPLICATION_ID = "PS";
    
    public static final String ASSETCLASS_EXCLUDE_LSG = "LSG";
    public static final String ASSETCLASS_EXCLUDE_STV = "STV";
	public static final String ASSETCLASS_EXCLUDE_PB = "PB";
	public static final String ASSETCLASS_EXCLUDE_MCF = "MCF";
	public static final String ASSETCLASS_EXCLUDE_MBC = "MBC";
	public static final String ASSETCLASS_EXCLUDE_MGC = "MGC";
	public static final String ASSETCLASS_EXCLUDE_IDX = "IDX";
	public static final String ASSETCLASS_EXCLUDE_FXS = "FXS";
	public static final String ASSETCLASS_EXCLUDE_HYF = "HYF";
	public static final String ASSETCLASS_EXCLUDE_GLB = "GLB";
	
	
	public static final String CSV_FORMAT = "csv";
	public static final String PDF_FORMAT = "pdf";
	public static final String WEB_FORMAT = "web";
	public static final String ALL_TABS = "All";
	
	public static final String VAL_STR = "val_str";
	public static final String SPACE = "&nbsp;";
	public static final String SHADED_ROW = "shadedRow";
	public static final String NAME = "name";
	public static final String CUR = "cur";
	public static final String SCORE = "score";
	public static final String SCORE_WRAP = "score score_wrap";
	public static final String SCORE_BOLD = "score score_bold";
	public static final String VAL_STR_FONT = "val_str val_str_font";
	public static final String Fi360 = "Fi360";
	public static final String RPAG = "RPAG";
	public static final String MSTAR = "MSTAR";
	public static final String IMAGE = "IMAGE";
	public static final String DEFAULT_CUR = "##0.00";
	public static final String DEFAULT_CUR_PCT = "##0.00'%'";
	public static final String NUMBER_FORMAT = "####";
	public static final String MORNINGSTAR_FOOTNOTE_CMA_ID = "morningstarFootNoteCMAId";
	public static final String MORNINGSTAR_FOOTNOTE_LIST = "morningstarFootNoteParamsList";
	public static final int MORNINGSTAR_3_5_10_YR_FOOTNOTE= 77502;
	public static final int MORNINGSTAR_3_5_YR_FOOTNOTE= 77503;
	public static final int MORNINGSTAR_3_YR_FOOTNOTE= 77504;
	
	public static final String PRODUCT_FEATURE_TYPE_CD_JHI = "JHI";
	public static final String PRODUCT_FEATURE_TYPE_CD_SVP = "SVP";
	
}