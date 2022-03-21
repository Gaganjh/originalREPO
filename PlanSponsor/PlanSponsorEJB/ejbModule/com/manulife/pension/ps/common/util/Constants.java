package com.manulife.pension.ps.common.util;


public interface Constants {

    // sort option codes - also resides in web SecurityConstants

    public static final String EMPLOYEE_ID_SORT_OPTION_CODE = "EE";

    public static final String SSN_SORT_OPTION_CODE = "SS";

    public static final String PS_APPLICATION_ID = "PS";

    public static final int  FILE_NAME_LENGTH_1=10;
    public static final int  FILE_NAME_LENGTH_2=11;
    public static final int  FILE_NAME_LENGTH_3=12;
    public static final int YEAR_START_INDEX_1=5;
    public static final int YEAR_START_INDEX_2=6;
    public static final int YEAR_START_INDEX_3=7;
    
    public static final int QUARTER_START_INDEX_1=9;
    public static final int QUARTER_START_INDEX_2=10;
    public static final int QUARTER_START_INDEX_3=11;
    
    public static final String YES = "Y";
    
    public static final String ORGANIZE_BY_ASSET_CLASS = "2";
    public static final String ASSET_CLASS_LIFECYCLE = "LCF";
    public static final String ASSET_CLASS_LIFESTYLE = "LSF";
    public static final String ASSET_CLASS_GIFL = "LSG";
    
    public static final String PBA_CATEGORY_CODE = "PB";
    public static final String PBA_DESC = "Personal Brokerage Account";
    public static final int PBA_ORDER = 999;
    
    public static final String PHONE_NUMBER_SEPERATOR = ".";
    
    public static final String FIRST_YEAR = "FY";
    public static final String RENEWAL = "RN";
    public static final String MONEY_SOURCE_TRANSFER_CODE = "TR";
    public static final String MONEY_SOURCE_REGULAR_CODE = "RG";
    public static final String COMPENSATION_TYPE_UPFRONT_CODE = "DBU";
    public static final String COMPENSATION_TYPE_ACCRUED_CODE = "DBA";
    public static final String COMPENSATION_TYPE_ASSET_BASED_CODE = "AB";
    public static final String COMPENSATION_TYPE_PRICE_CREDIT_CODE = "FT";
    public static final String ACCESS_CHANNEL_IVR_CODE = "IIQ";
    public static final String ACCESS_CHANNEL_WEB_CODE = "WIQ";
    public static final String PARTICIPANT_INITIATE_LOAN_CODE = "PIL";
    public static final String LOAN_FEATURE_CODE = "LRK01";
    //GIFL V3 constants
    public static final String GIFL_VERSION_03 = "G03";
    public static final String GIFL_VERSION_02 = "G02";
    public static final String GIFL_VERSION_01 = "G01";
    public static final String GIFL_SELECT_TEXT = "G.I.F.L. Select";
    public static final String GIFL_TEXT = "G.I.F.L.";
    public static final String NO = "No";
    
    // MA constants
    public static final String MA_CODE_IPM = "IPM";
    public static final String MA_CODE_AMA = "AMA";
    public static final String MA_IPM_TEXT = "Retirement Advice";
    public static final String MA_AMA_TEXT = "Advisory Managed Account";
    
    // Added for Spring fund launch
    public static final String COMMA_SYMBOL = ",";    
    public static final String SINGLE_QUOTES = "'";
    
    public static final String JOHN_HANCOCK_REPRESENTATIVE = "John Hancock Representative";
	public static final String ADMINISTRATION  = "Administration";
	public static final String SYSTEM_USER_PROFILE_ID  = "8";
	
	public static final String PBA_YES="Yes";
	public static final String PBA_NO="No";
	
	
    
}
