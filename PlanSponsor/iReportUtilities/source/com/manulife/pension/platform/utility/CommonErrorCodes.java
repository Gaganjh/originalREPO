package com.manulife.pension.platform.utility;

public interface CommonErrorCodes {
    int TECHNICAL_DIFFICULTIES = 1047;
    
    int OUT_OF_SERVICE_HOURS = 1010;

    public static final int USER_DOES_NOT_EXIST = 1001;

    // Login Errors
    public static final int EMPTY_USERNAME = 1007;

    public static final int EMPTY_PASSWORD = 1008;

    public static final int CHALLENGE_ANSWER_OVER_LIMIT = 1009;

    public static final int INVALID_NETWORK_LOCATION = 1517;

    // security related error codes
    public static final int FIRST_NAME_MANDATORY = 2126;

    public static final int FIRST_NAME_INVALID = 2127;

    public static final int LAST_NAME_MANDATORY = 2128;

    public static final int LAST_NAME_INVALID = 2129;

    public static final int PASSWORDS_DO_NOT_MATCH = 2008;

    public static final int PASSWORD_MANDATORY = 2009;

    public static final int CONFIRM_PASSWORD_MANDATORY = 2010;

    public static final int PASSWORD_FAILS_STANDARDS = 2011;

    public static final int SSN_INVALID = 2131;

    public static final int EMAIL_MANDATORY = 2132;

    public static final int EMAIL_INVALID = 2133;    

    public static final int PHONE_NOT_NUMERIC = 1118;
    
    public static final int PHONE_NOT_COMPLETE = 1349;
    
    public static final int PHONE_INVALID = 1118;

    public static final int FAX_NOT_NUMERIC = 1352;
    
    public static final int FAX_NOT_COMPLETE = 1350;
    
    public static final int FAX_INVALID = 1352;

    public static final int EXTENSION_NO_INVALID = 1351;
    
    public static final int PH_NOTENTERED_EXT_ENTERED = 1117;

    // Investment charting errors
    public static final int CHART_CHECKFUNDS_FUND_DUPLICATES = 1201;

    public static final int CHART_START_DATE = 1202;

    public static final int CHART_END_DATE = 1203;

    public static final int CHART_CHECKDATES_END_DATE_IN_THE_FUTURE = 1204;
    
    public static final int CHART_CHECKFUNDS_SIX_MONTHS = 1205;

    public static final int CHART_INTEGER_FUND_PERCENTAGE = 1206;

    public static final int CHART_RANGE_FUND_PERCENTAGE = 1207;

    public static final int CHART_CHECKFUNDS_FUND_MISSING = 1208;

    public static final int CHART_CHECKFUNDS_PCT_ASSIGNED_TO_INDEX = 1209;

    public static final int CHART_CHECKFUNDS_100PCT = 1210;

    public static final int CHART_NO_FUND_UNIT_VALUE_BEFORE = 1211;
    
    public static final int CHART_NO_FUND_UNIT_VALUE_AFTER = 1212;

    public static final int CHART_MISSING_UNIT_VALUE_DATA = 1213;
    
    public static final int CHART_NO_UNIT_VALUE_DATA_BEFORE = 1214;

    public static final int CHART_NO_UNIT_VALUE_DATA_AFTER = 1215;

    public static final int CHART_START_DATE_EXCEEDS_END_DATE = 1216;
    
    public static final int CHART_MISSING_FUND_CLASS = 1223;
    
    public static final int USER_NAME_MANDATORY = 2147;

    public static final int USER_NAME_INVALID = 2148;

    public static final int CONTRACT_NUMBER_MANDATORY = 2000;

    public static final int CONTRACT_NUMBER_INVALID = 1043;

    // Investment errors
    public static final int NO_ALLOCATIONS_FOUND = 3000;
    
    public static final int INVALID_ASOFDATE_SELECTED = 3001;
    
    // Report Service
    public static final int REPORT_SERVICE_UNAVAILABLE = 2123;

    public static final int REPORT_FILE_NOT_FOUND = 2124;
    
    // Snapshot errors
    public static final int EMPTY_VALUE = 1101;
    
    public static final int TOO_SMALL = 1102;

    public static final int INVALID_ENTRY = 1103;
    
    // Forgot password errors
    public static final int PERSONAL_IDENTIFIER_MANDATORY = 2001;

    /*------------ Funds and Performance---------------------*/
    // ShortList filter
    public static final int ALL_SHORTLIST_OPTIONS_NOT_SELECTED = 9218;

    // Search for a  Fund Filter 
    public static final int FUND_NAME_NOT_ENTERED = 9225;
    public static final int FUND_NAME_LESS_THAN_MINIMUM_REQUIRED_CHARS = 9226;
    public static final int NO_MATCH_FOUND_FOR_ENTERED_FUND_NAME = 2258;

    // Contract search filter
    public static final int CONTRACT_NAME_SEARCH_REQUIRES_MINIMUN_3_CHARARACTERS = 2159;
    public static final int NO_MATCH_FOR_CONTRACT_NUMBER = 9216;
    public static final int NO_MATCH_FOR_CONTRACT_NAME = 1046;
    public static final int CONTRACT_RESULTS_SIZE_GREATER_THAN_35 = 2258;
    
    // Custom Query filter
    public static final int CUSTOM_QUERY_MISSING_REQUIRED_ENTRIES = 9220;
    public static final int CUSTOM_QUERY_VALUE_DOES_NOT_MATCH_DATA_TYPE = 9221;
    public static final int CUSTOM_QUERY_VALUE_INVALID_DATE = 9274;
    public static final int CUSTOM_QUERY_MISMATCH_OR_INVALID_BRACKET_USAGE = 9222;
    public static final int CUSTOM_QUERY_INVALID_CHAR_IN_QUERY_NAME = 9223;
    public static final int CUSTOM_QUERY_SAVE_WITHOUT_NAME = 9224;
    public static final int CUSTOM_QUERY_EXCEEDS_25_LINES_LIMIT = 9243;
    
    // Save Fund List
    public static final int SAVE_LIST_WITHOUT_NAME = 9241;
    public static final int SAVE_LIST_NAME_INVALID = 9242;
    
    // MY Saved Lists filter
    public static final int MUST_SELECT_A_LIST_TO_DISPLAY = 9227;
    public static final int MUST_SELECT_A_LIST_DELETE = 9228;
    
    // MY Custom Queries filter
    public static final int MUST_SELECT_A_QUERY_TO_DISPLAY = 9238;
    public static final int MUST_SELECT_A_QUERY_TO_EDIT = 9239;
    public static final int MUST_SELECT_A_QUERY_DELETE = 9240;
    
    // common to all filters
    public static final int VIEW_RESULTS_WITHOUT_SELECTION = 9217;
    
    /*------------ Funds and Performance---------------------*/
    
    public static final int EMAIL_ADDRESS_MANDATORY = 2002;

    /* --------------- iReport ---------------------- */
    public static final int PORTFOLIO_NOT_SELECTED = 9276;
    
    // Fund Draft 
    public static final int TOTAL_WEIGHTING_PERCENT_SHOULD_BE_100 = 1289;
    public static final int DUPLICATE_CRITERIA_SELECTED = 1290;
    public static final int NON_NUMERIC_VALUE = 1291;
    public static final int MUST_ENTER_WHOLE_NUMBER = 1292;
    public static final int WEIGHTING_NOT_SELETED = 1293;
    public static final int CRITERIA_NOT_SELECTED = 1294;
    public static final int NO_ACCESS_ERROR = 1295;
    public static final int IPS_PC_REVIEW_EXISTS = 2958;
    public static final int IPS_INVALID_SERVICE_DATE = 2959;
    
    //IPSR
    public static final int FUND_SHEET_NOT_AVAILABLE_EXCEPTON = 1520;
    public static final int IPS_INVALID_IPS_IAT_EFFECTIVE_DATE_RANGE_ERROR = 1525;
    public static final int IPS_NON_BUSINESS_IPS_IAT_EFFECTIVE_DATE_ERROR = 1550;
    public static final int IPS_INVALID_IAT_EFFECTIVE_DATE_ERROR = 1523;
    public static final int IPS_NO_ACTION_TAKEN_ERROR = 1551;
    public static final int IPS_INVALID_SERVICE_DATE_FORMAT = 1552;
    public static final int IPS_CONFIRMATION_CHECKBOX_NOT_CHECKED = 1553;
    
    public static final int STMT_FIRST_NAME_INVALID = 3040;
    
}