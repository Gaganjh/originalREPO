package com.manulife.pension.bd.web.fundEvaluator;

/**
 * This class contains general constants related to Fund Evaluator.
 * 
 * @author Ranjith Kumar
 * 
 */
public class FundEvaluatorConstants {

    public static String FUND_MENU_VALUE_ALL_FUNDS = "ALL";

    public static String FUND_MENU_VALUE_RETAIL_FUNDS = "RET";

    public static String FUND_MENU_VALUE_SUBADVISED_FUNDS = "VEN";

    public static String FUND_MENU_LABEL_ALL_FUNDS = "All funds";

    public static String FUND_MENU_LABEL_RETAIL_FUNDS = "Retails funds";

    public static String FUND_MENU_LABEL_SUBADVISED_FUNDS = "Subadvised funds";

    public static String FUNDS_PRESELECT_NO = "no";

    public static String FUNDS_PRESELECT_WARRANTY = "warranty";

    public static String FUNDS_PRESELECT_ALL = "all";

    public static String VIEW_INVESTMENT_OPTIONS_BY_RANKING = "viewByRanking";

    public static String VIEW_INVESTMENT_OPTIONS_BY_MEASUREMENT = "viewByMeasurement";

    public static String LIST_INVESTMENT_OPTIONS_BY_AVAILABLE = "listByAvailableInvestmentOptions";

    public static String LIST_INVESTMENT_OPTIONS_BY_SELECTED = "listBySelectedInvestmentOptions";

    public static String SORT_ASCENDING = "ASC";

    public static String SORT_DESCENDING = "DESC";

    public static final String PREVIEW_FUNDS = "previewFunds";

    public static final String ASSET_CLASS_ID_LIFECYCLE_AND_LIFESTYLE = "LSC";
    
    public static final String ASSET_CLASS_ID_BALANCED = "BAL";
    
    public static final String ASSET_CLASS_ID_GIFL_SELECT = "LSG";

    public static final String ASSET_CLASS_ID_LIFESTYLE = "LSF";

    public static final String ASSET_CLASS_ID_LIFECYCLE = "LCF";

    public static final String ASSET_CLASS_ID_INDEX = "IDX";

    public static final String CLIENT_EXISTING = "existingclient";

    public static final String CLIENT_NEWPLAN = "newplan";

    public static final String US_FUNDS = "USA";// changed

    public static final String NY_FUNDS = "NY";

    public static final String COMPULSORY_FUND_SVF = "svf";

    public static final String COMPULSORY_FUND_MMF = "mmf";

    public static final String EMPTY_STRING = "";

    public static final String FUNDLIST_ASSET_CLASS = "assetclass";

    public static final String CONTRACT_STATUS_AC = "AC";

    public static final String CONTRACT_STATUS_PS = "PS";

    public static final String CONTRACT_STATUS_DC = "DC";

    public static final String CONTRACT_STATUS_PC = "PC";

    public static final String CONTRACT_STATUS_CA = "CA";

    public static final String CONTRACT_STATUS_IP = "IP";

    public static final String CONTRACT_STATUS_CP = "CP";

    public static final String CONTRACT_STATUS_PA = "PA";

    public static final String CONTRACT_STATUS_AP = "AP";

    public static final String CONTRACT_STATUS_PP = "PP";

    public static final String COVER_SHEET_STANDARD = "standard";

    public static final String COVER_SHEET_TYPE1 = "type1";

    public static final String COVER_SHEET_TYPE2 = "type2";

    public static final String COVER_SHEET_TYPE3 = "type3";

    public static final String LIST_AVAILABLE_INVESTMENT_OPTIONS = "availableInvOptions";

    public static final String LIST_SELECTED_INVESTMENT_OPTIONS = "selectedInvOptions";

    // Constants for Report sections
    public static final String COVER_PAGE_SECTION = "COVP";

    public static final String TABLE_OF_CONTENTS_SECTION = "TOCS";

    public static final String SELECTING_INV_FOR_PLAN_SECTION = "SIFP";

    public static final String DIVERSIFICATION = "DIVR";

    public static final String CUSTOM_MEASUREMENT_CRITERIA_SECTION = "CMCR";

    public static final String RESULTS_SECTION = "REAC";

    public static final String DOCUMENTING_DUE_DILIGENCE_SECTION = "DIPS";

    public static final String RANKING_METHODOLOGY_SECTION = "RMTH";

    public static final String FUND_RANKING_SELECTED_FUNDS_SECTION = "FRSE";

    public static final String FUND_RANKING_ALL_AVAILABLE_FUNDS_SECTION = "FRAV";

    public static final String PERFORMANCE_BY_ASSET_CLASS_SECTION = "PEAC";

    public static final String PERFORMANCE_BY_RISK_CATEGORY_SECTION = "PERC";
    
    public static final String SIMPLE_AVERAGE = "SAVG";
    
    public static final String WEIGHTED_AVERAGE = "WAVG";
    
    public static final String WEIGHTED_AVG = "weightedAverage";
    
    public static final String SIMPLE_AND_WEIGHTED_AVERAGES = "BAVG";

    public static final String GLOSSARY_SECTION = "GLOS";

    public static final String IMPORTANT_NOTES_SECTION = "IMPN";

    public static final String BACK_COVER_SECTION = "BKCO";

    public static final String IPS_SECTION = "IPST";

    public static final String DEFAULT_INV_OPTION_SECTION = "DIOS";

    public static final String INV_SELECTION_FORM_SECTION = "IVSF";
    
    public static final String GIFL_SELECT_INFORMATION_SECTION = "GIFL";

    public static final String INV_REPLACEMENT_FORM_SECTION = "IREP";

    public static final String FORWARD_SELECT_YOUR_CLIENT = "selectYourClient";

    public static final String FORWARD_SELECT_CRITERIA = "selectCriteria";

    public static final String FORWARD_NARROW_YOUR_LIST = "narrowYourList";

    public static final String FORWARD_INVESTMENT_OPTIONS_SELECTION = "investmentOptionsSelection";

    public static final String FORWARD_LAUNCH_PRINT_WINDOW = "launchPrintWindow";

    public static final String FORWARD_CUSTOMIZE_REPORT = "customizeReport";

    public static final String FORWARD_INVESTMENT_OPTION_DETAILS = "invOptionDetails";

    public static final String FORWARD_UPDATE_INV_OPTION_DETAILS = "udpateInvOptionDetails";

    public static final String FORWARD_HOME_PAGE = "homePage";

    public static final String NAVIGATE_TO_PREVIOUS_PAGE = "previous";

    public static final String FORWARD_REPORT_ERROR_PAGE = "fundEvaluatorErrorPage";

    public static final String RESET_FILTERS = "resetFilters"; // Resest filters request parameter for overlays
    
    public static final String NEXT_ACTION_INAPPLICABLE = "NEXT_ACTION_INAPPLICABLE";
    
    public static final String SINGLE_SPACE = " ";
    
    public static final String CO_NAME_SEPERATOR = "-";
    
    public static final String PERIOD = ".";
    
    public static final String PDF_FILE_NAME_DATE_FORMAT = "MMMdd-yyyy";
    
    public static final String PDF_FILE_NAME_PREFIX = "FundEvaluator-";
    
    public static final String PDF_FILE_NAME_SUFFIX = ".pdf";

	public static final String COMPANY_ID_USA = "019";
	
	public static final String COMPANY_ID_NY = "094";
	
	public static final String RETIREMENT_LIVING_FUND_FAMILY_CODE = "RL";
	
	public static final String RETIREMENT_CHOICES_FUND_FAMILY_CODE = "RC";
	
	public static final String RUSSELL_FUND_FAMILY_CODE = "RS";
	
	public static final String LIFESTYLE_ACTIVE_STRATEGIES_FAMILY_CODE = "LS";
		
	// added for ISP
    public static final String NY_STATE = "NY";
    public static final String NML = "NML";
    public static final String EDWARD_JONES = "EDJ";
    public static final String MERRILL_LYNCH = "ML";
    public static final String PUERTO_RICO = "PR";
    


    
}