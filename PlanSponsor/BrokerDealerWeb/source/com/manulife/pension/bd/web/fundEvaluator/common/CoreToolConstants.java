package com.manulife.pension.bd.web.fundEvaluator.common;

import java.math.BigDecimal;

/**
 * Constants related to FundEvaluator
 * @author PWakode
 */

public interface CoreToolConstants {
	
	public static final String BROKERDEALER_RATETYPE = "PBD";
	public static final String GUARANTEED_RATETYPE = "NA";

	// Metrics percentiles
	public static final int PERCENTILE_MAXIMUM = 100;
	public static final int PERCENTILE_75TH = 75;
	public static final int PERCENTILE_MEDIAN = 50;
	public static final int PERCENTILE_25TH = 25;
	public static final int PERCENTILE_MINIMUM = 0;
    
	// Rank calculation related constants
    public static final BigDecimal SUBSTRACT_FACTOR = new BigDecimal(-0.000001);
    public static final BigDecimal HUNDRED = new BigDecimal(100);
    public static final int ONE = 1;

	// Asset class IDs
	public static final String ASSET_CLASS_ID_LARGE_CAP_BLEND = "LCB";
	public static final String ASSET_CLASS_ID_LARGE_CAP_GROWTH = "LCG";
	public static final String ASSET_CLASS_ID_LARGE_CAP_VALUE = "LCV";
	public static final String ASSET_CLASS_ID_MID_CAP_BLEND = "MCB";
	public static final String ASSET_CLASS_ID_MID_CAP_GROWTH = "MCG";
	public static final String ASSET_CLASS_ID_MID_CAP_VALUE = "MCV";
	public static final String ASSET_CLASS_ID_SMALL_CAP_BLEND = "SCB";
	public static final String ASSET_CLASS_ID_SMALL_CAP_GROWTH = "SCG";
	public static final String ASSET_CLASS_ID_SMALL_CAP_VALUE = "SCV";
	public static final String ASSET_CLASS_ID_MULTI_CAP_BLEND = "MBC";
	public static final String ASSET_CLASS_ID_MULTI_CAP_GROWTH = "MGC";
	public static final String ASSET_CLASS_ID_MULTI_CAP_VALUE = "MCF";
	public static final String ASSET_CLASS_ID_INTERNATIONAL_GLOBAL_BLEND = "IGB";
	public static final String ASSET_CLASS_ID_INTERNATIONAL_GLOBAL_GROWTH = "IGG";
	public static final String ASSET_CLASS_ID_INTERNATIONAL_GLOBAL_VALUE = "IGV";
    
    // Fund type
    public static final String GUARANTEED_FUND_TYPE = "GA";
    
	// The hybrid asset class is composed of these three
	public static final String ASSET_CLASS_ID_BALANCED = "BAL";
	public static final String ASSET_CLASS_ID_LIFECYCLE = "LCF";
	public static final String ASSET_CLASS_ID_LIFESTYLE = "LSF";

	// Global fund asset class
	public static final String ASSET_CLASS_ID_GLOBAL_BOND_FUND = "GLB";

	public static final String ASSET_CLASS_ID_INDEX = "IDX";
    public static final String ASSET_CLASS_ID_SPECIALTY = "SPE";
	public static final String ASSET_CLASS_ID_SECTOR = "SEC";
	public static final String ASSET_CLASS_ID_HIGH_QUALITY_SHORT_TERM = "HQS";
	public static final String ASSET_CLASS_ID_HIGH_QUALITY_INTERMEDIATE_TERM = "FXI";
	public static final String ASSET_CLASS_ID_HIGH_QUALITY_LONG_TERM = "FXL";
	// We have some asset classes on the Asset Houses that do not appear in the database. For Data integrity reasons, they are included in FundDAO - hence we define faked constants for them also.
	public static final String ASSET_CLASS_ID_MEDIUM_QUALITY_SHORT_TERM = "MQI";	// FAKED OUT
	public static final String ASSET_CLASS_ID_MEDIUM_QUALITY_INTERMEDIATE_TERM = "FXM";
	public static final String ASSET_CLASS_ID_MEDIUM_QUALITY_LONG_TERM = "MQL";		// FAKED OUT
	public static final String ASSET_CLASS_ID_LOW_QUALITY_SHORT_TERM = "LQS"; 		// FAKED OUT
	public static final String ASSET_CLASS_ID_LOW_QUALITY_INTERMEDIATE_TERM = "LQI";
	public static final String ASSET_CLASS_ID_LOW_QUALITY_LONG_TERM = "LQL";		// FAKED OUT
    public static final String ASSET_CLASS_ID_GLOBAL_SHORT_TERM = "GLS";       // FAKED OUT
    public static final String ASSET_CLASS_ID_GLOBAL_INTERMEDIATE_TERM = "GLI";
    public static final String ASSET_CLASS_ID_GLOBAL_LONG_TERM = "GLL";        // FAKED OUT
	public static final String ASSET_CLASS_ID_YEAR_3_GUARANTEED = "GA3";
	public static final String ASSET_CLASS_ID_YEAR_5_GUARANTEED = "GA5";
	public static final String ASSET_CLASS_ID_YEAR_10_GUARANTEED = "G10";
    
//  adding new ones for GIFL and Stable value
    public static final String ASSET_CLASS_ID_GIFL = "LSG";
    
    public static final String ASSET_CLASS_ID_STABLE_VALUE = "STV";
    public static final String ASSET_CLASS_ID_BLANK = "";//blank one exists in ASSETCLSGROUP table

	// Investment categories
	// NOTE: The INVESTMENTGROUP table has no PK column, so order->category is a hard coded link here
	//       If the DB order changes then these values must change as well
	public static final Integer INVESTMENT_CATEGORY_ID_AGGRESSIVE_GROWTH = new Integer(3);
	public static final Integer INVESTMENT_CATEGORY_ID_GROWTH = new Integer(4);
	public static final Integer INVESTMENT_CATEGORY_ID_GROWTH_AND_INCOME = new Integer(5);
	public static final Integer INVESTMENT_CATEGORY_ID_INCOME = new Integer(6);
	public static final Integer INVESTMENT_CATEGORY_ID_CONSERVATIVE = new Integer(7);
	public static final Integer INVESTMENT_CATEGORY_ID_LIFECYCLE = new Integer(0);
	public static final Integer INVESTMENT_CATEGORY_ID_LIFESTYLE = new Integer(1);
	//GIFL P3c
	public static final Integer INVESTMENT_CATEGORY_ID_GIFL = new Integer(2);
		
	// Guaranteed Funds
	public static final String TEN_YEAR_GUARANTEED_FUND = "10YC";
	public static final String FIVE_YEAR_GUARANTEED_FUND  = "5YC";
	public static final String THREE_YEAR_GUARANTEED_FUND  = "3YC";
		
	public static final String LOCATION_USA = "USA";
	public static final String LOCATION_NY = "NY";
	
	public static final String COMPANY_ID_USA = "019";
    public static final String COMPANY_ID_NY = "094";
    
	public static final String MANULIFE_SHORT_NAME_NY = "John Hancock New York";
	public static final String MANULIFE_SHORT_NAME_USA = "John Hancock USA";
	
	public static final String NON_APPLICABLE_STRING = "n/a";
	public static final String NON_APPLICABLE_STRING_WEB = "N/A";

	public static final String FUND_MENU_VENTURE = "VEN";
	public static final String FUND_MENU_ALL = "ALL";
	public static final String FUND_MENU_RETAIL = "RET";

	// Constants for FundTypes (from the FundType column of the GET_FUND stored procedure). 
	public static final String FUNDTYPE_INDEX = "INDEX";
	public static final String FUNDTYPE_GUARANTEED_ACCOUNT = "GUARANTEED";
	public static final String FUNDTYPE_POOLED = "POOLED";
	public static final String FUNDTYPE_PBA = "PBA";
    
    public static final String PROPERTIES_FILE_NAME = "./FundEvaluator.properties";
    public static final String DEFAULT_MONEY_MARKET_FUNDS = "defaultSelectedMoneyMarketFunds";
    public static final String DEFAULT_STABLE_VALUE_FUNDS = "defaultSelectedStableValueFunds";
    public static final String GIFL_V3_OPTIONAL_LSPS_FUNDS = "giflV3OptionalLSPSFunds";
    
    //Constants added for the alignment 
    public static final String ALIGNMENT_EFFECTIVE_DATE = "ALIGNMENT_EFFECTIVE_DATE";
    public static final String FUND_SHEET_UPDATE_DATE = "FUND_SHEET_UPDATE_DATE";
   
    
    //CMA Footnotes related constants
    public static final String FOOTNOTE_EXPIRY_MINUTES_KEY = "footnoteExpiryMinutes";
    public static final String FOOTNOTE_EXPIRY_MINUTES_DEFUALT = "60";
    
    //Risk category constant names
    public static final String RISK_CAT_LCF = "Asset Allocation - Target Date";
    public static final String RISK_CAT_LSF = "Asset Allocation - Target Risk";
    public static final String RISK_CAT_AGGRESSIVE_GROWTH = "Aggressive Growth";
    public static final String RISK_CAT_GROWTH = "Growth";
    public static final String RISK_CAT_GROWTH_INCOME = "Growth & Income";
    public static final String RISK_CAT_INCOME = "Income";
    public static final String RISK_CAT_CONSERVATIVE = "Conservative";
    public static final String RISK_CAT_LSG = "Guaranteed Income Feature";	//GIFL P3c
    
    //Sorting related constants
    public static final String REVERSE_ORDER ="reverse";
    public static final String DEFAULT_ORDER ="default";
    
    //General constants
    public static final String COMMA =",";
    public static final String SPACE =" ";
    
    public static final String DIST_CHANNEL_EDWARD_JONES = "EDJ";
	public static final String SVF = "SVF";
	public static final String MMF = "MMF";

    
}