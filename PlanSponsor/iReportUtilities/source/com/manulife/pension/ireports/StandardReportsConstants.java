package com.manulife.pension.ireports;

import java.util.Arrays;
import java.util.List;

public interface StandardReportsConstants {

	// session attributes
	public static final String STANDARDREPORTS_SESSION = "standardreports_session";

	// companies
	public static final String STANDARDREPORTS_COMPANY_USA = "USA";
	public static final String STANDARDREPORTS_COMPANY_NY = "NY";

	public static final String COMPANY_ID_USA = "019";
	public static final String COMPANY_ID_NY = "094";
	
	// fund menu
	public static final int FUND_MENU_RETAIL = 1;	
	public static final int FUND_MENU_VENTURE = 2;	
	public static final int FUND_MENU_ALL = 3;

	public static final int FUND_MENU_SIZE = 3;
	
	public static final String RETAIL = "Retail";
	public static final String VENTURE = "Sub-advised";
	public static final String ALL = "All Funds";	

	// class
	public static final String CLASS_1 = "CL1";
	public static final String CLASS_2 = "CL2";
	public static final String CLASS_3 = "CL3";
	public static final String CLASS_4 = "CL4";
	public static final String CLASS_5 = "CL5";
	public static final String RATE_TYPE_CX0 = "CX0";
	

	// product
	public static final String  PRODUCT_ID_ARA92 = "ARA92";
	public static final String  PRODUCT_ID_ARANY = "ARANY";
	public static final String  PRODUCT_ID_ARABD = "ARABD";
	public static final String  PRODUCT_ID_ARABDY = "ARABDY";
	
	public static final String  PRODUCT_ID_ARA06 = "ARA06";
	public static final String  PRODUCT_ID_ARA06Y = "ARA06Y";
	
	public static final List PRODUCT_IDS_MULTICLASS = Arrays.asList(new String[] {PRODUCT_ID_ARA06, PRODUCT_ID_ARA06Y});
	public static final List PRODUCT_IDS_VENTURE_BD = Arrays.asList(new String[] {PRODUCT_ID_ARABD, PRODUCT_ID_ARABDY});

	// Package Series from Contract table
	public static final String PACKAGE_SERIES_RET = "RET";
	public static final String PACKAGE_SERIES_VENTURE = "IFP";
	public static final String PACKAGE_SERIES_HYBRID = "HYB";
	public static final String PACKAGE_SERIES_MULTI_CLASS = "MC";

	
	
	/**
	 *  Venture Retail Series - similar to Hybrid but only used by a small number of contracts. Will be treated the same as hybrid.
	 */
	public static final String PACKAGE_SERIES_VRS = "VRS";
	
	// fund Series for report lineup name
	public static final int FUND_SERIES_RETAIL = 1;
	public static final int FUND_SERIES_VENTURE = 2;
	public static final int FUND_SERIES_VENTURE_BD = 3;
	public static final int FUND_SERIES_HYBRID = 4;
	public static final int FUND_SERIES_HYBRID_BD = 5;
	public static final int FUND_SERIES_MULTICLASS = 6;
	
	// rate type
	public static final String BROKERDEALER_RATETYPE = "PBD";
	public static final String GUARANTEED_RATETYPE = "NA";

	
	// fund categories
	public static final String FUNDCATEGORY_BOTH = "B";
	public static final String FUNDCATEGORY_VENTURE = "I";
	public static final String FUNDCATEGORY_RETAIL = "R";
	public static final List CATEGORY_RETAIL = Arrays.asList(new String[] { FUNDCATEGORY_BOTH, FUNDCATEGORY_RETAIL});
	public static final List CATEGORY_VENTURE = Arrays.asList(new String[] { FUNDCATEGORY_BOTH, FUNDCATEGORY_VENTURE});
	
	// fund types
	public static final String FUNDTYPE_INDEX = "IN";
	public static final String FUNDTYPE_GIC = "GA";
	
	// report as-of-date contexts
    public static final String REPORT_CONTEXT_UNIT_VALUE = "UV";
	public static final String REPORT_CONTEXT_FUND_RETURNS = "ROR";
	public static final String REPORT_CONTEXT_FUND_METRICS = "MET";
	public static final String REPORT_CONTEXT_AICS = "AIC";
	public static final String REPORT_CONTEXT_FUND_EXPENSE_RATIOS = "FER";
	public static final String REPORT_CONTEXT_FUND_STD_DEVIATIONS = "DEV";
    public static final String REPORT_CONTEXT_FUND_STD_DEVIATION = "DEVQE";
	public static final String REPORT_CONTEXT_MARKET_INDEX_IB_PERFORMANCE = "MKP";
	public static final String REPORT_CONTEXT_MORNINGSTAR_CAT_PERFORMANCE = "MSP";
	
	// order for conservative group
	public static final int CONSERVATIVE_GROUP_ORDER = 6;

	// asset classes
	public static final String ASSET_CLASS_LIFECYCLE = "LCF"; // TODO This should be a flag of some sort rather than using Asset Class. Supposed to use LIFECYCLE_IND on UNDERLYING_FUND table
	public static final String ASSET_CLASS_LIFESTYLE = "LSF";
	public static final String ASSET_CLASS_GIFL = "LSG";
	
	public static final String RETIREMENT_LIVING_FUND_FAMILY_CD = "RL";
	public static final String RETIREMENT_CHOICES_FUND_FAMILY_CD = "RC";
	public static final String DOT_INDICATOR = "•";
	public static final String MERRILL_RESRICTED_FUND_SYMBOL = "#";
	// misc. helper constants
	public static final String FUND_SERIES_NOT_FOUND = "-404";
	public static final String FOOTNOTE_PARAGRAPH_MARKER = "<p>";
	
	// ROR constants used by Metrics objects
	public static final String ROR_YTD = "rorytd";
	public static final String ROR_5YR = "ror5yr";
	public static final String ROR_3YR = "ror3yr";
	public static final String ROR_3MTH = "ror3mth";
	public static final String ROR_1YR = "ror1yr";
	public static final String ROR_1MTH = "ror1mth";
	public static final String ROR_10YR = "ror10yr";
	public static final String ROR_SINCEINCEPTION = "rorSinceInception";
	public static final String INCEPTION_DATE = "inceptionDate";
	
	public static final String ROR_1YR_QE = "ror1yrqend";
	public static final String ROR_5YR_QE = "ror5yrqend";
	public static final String ROR_10YR_QE = "ror10yrqend";
	public static final String ROR_SINCEINCEPTION_QE = "rorSinceInceptionqend";
	
	// Title id's.
	public static final String TITLE_1 = "title1";
    public static final String TITLE_2 = "title2";
    public static final String TITLE_3 = "title3";
    public static final String TITLE_4 = "title4";
    public static final String TITLE_5 = "title5";
    public static final String TITLE_6 = "title6";
    
    public static final String NA = "n/a";
    public static final String BLANK = " ";
    public static final String SPACE = "";
    
    public static final int GLOBAL_FOOTNOTE_DISCLOSURE_ID = 64510;
    public static final int MORNINGSTAR_TICKER_REPORT_FOOTNOTE_DISCLOSURE_ID = 64952;
    
    public static final String STANDARD_REPORTS = "standardReports.staticFootnote.";
    
}
