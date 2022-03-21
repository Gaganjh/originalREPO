package com.manulife.pension.platform.web.ireports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;

import com.manulife.pension.ireports.report.streamingreport.impl.AICByAssetClassReport;
import com.manulife.pension.ireports.report.streamingreport.impl.AICByRiskReturnReport;
import com.manulife.pension.ireports.report.streamingreport.impl.FundCharacteristicsByAssetClassReport;
import com.manulife.pension.ireports.report.streamingreport.impl.FundCharacteristicsByRiskReturnReport;
import com.manulife.pension.ireports.report.streamingreport.impl.IR_AICByAssetClassReport;
import com.manulife.pension.ireports.report.streamingreport.impl.IR_AICByRiskReturnReport;
import com.manulife.pension.ireports.report.streamingreport.impl.InvestmentReturnsStandardDeviationsByAssetClassReport;
import com.manulife.pension.ireports.report.streamingreport.impl.InvestmentReturnsStandardDeviationsByRiskReturnReport;
import com.manulife.pension.ireports.report.streamingreport.impl.MarketReport;
import com.manulife.pension.ireports.report.streamingreport.impl.MorningstarRatingsAndTickersByAssetClassReport;
import com.manulife.pension.ireports.report.streamingreport.impl.MorningstarRatingsAndTickersByRiskReturnReport;
import com.manulife.pension.platform.web.fap.constants.FapConstants;

/**
 * This class holds the Constants used by the i:report functionality.
 * 
 * @author harlomte
 * 
 */
public class FundReportConstants {

    // The below Constants hold the Report Titles that will get displayed in the i:reports PDF.
    public static String EXPENSE_RATIO_REPORT_NAME = "Expense Ratios";
    public static String INVESTMENT_RETURN_AND_EXPENSE_RATIO_REPORT_NAME = "Investment Returns and Expense Ratios";
    public static String INVESTMENT_RETURN_AND_STANDARD_DEVIATION_REPORT_NAME = "Investment Returns and Standard Deviations";
    public static String FUND_CHARACTERISTICS_REPORT_NAME = "Fund Characteristics";
    public static String MORNINGSTAR_RATINGS_AND_TICKER_SYMBOL_REPORT_NAME = "Morningstar Ratings and Ticker Symbols";
    public static String MARKET_INDEX_REPORT_NAME = "Market Index Report";
    public static String LIFESTYLEFUND_REPORT_NAME = "Target Risk Fund";
    public static String LIFECYCLEFUND_REPORT_NAME = "Target Date Fund";

    // Market Index title 4 Name
    public static String MARKET_INDEX_TITLE4 = "Returns of Major Investment Benchmarks";
    
    // Portfolio Names to be displayed as part of Additional Name.
    public static String AGGRESSIVE_PORTFOLIO_NAME = "Aggressive Portfolio";
    public static String GROWTH_PORTFOLIO_NAME = "Growth Portfolio";
    public static String MODERATE_PORTFOLIO_NAME = "Moderate Portfolio";
    public static String BALANCED_PORTFOLIO_NAME = "Balanced Portfolio";
    public static String CONSERVATIVE_PORTFOLIO_NAME = "Conservative Portfolio";
    
    // Fund Menu ID's
    public static String ALL_FUNDS_MENU_ID = "allFundsMenu";
    public static String RETAIL_MENU_ID = "retailMenu";
    public static String SUB_ADVISED_MENU_ID = "subAdvisedMenu";
    
    // Fund Menu Numbers. These are used when creating the FundOffering object.
    public static Integer RETAIL_MENU_NUM = 1;
    public static Integer SUB_ADVISED_MENU_NUM = 2;
    public static Integer ALL_FUNDS_MENU_NUM = 3;

    // Fund Menu Titles that will be shown as Title on the i:reports PDF
    public static String ALL_FUNDS_MENU_TITLE = "All Funds Menu";
    public static String RETAIL_MENU_TITLE = "Retail Menu";
    public static String SUB_ADVISED_MENU_TITLE = "Sub-Advised Menu";
    public static String MODIFIED_LINEUP_TITLE = "Modified Lineup";
    public static String LOWEST_COST_SHORTLIST_TITLE = "Lowest Cost Shortlist";
    public static String TOP_PERFORMER_SHORTLIST_TITLE = "Top Performer Shortlist";
    public static String THREE_YEAR_PERFORMANCE_TITLE = "3 Year Performance Shortlist";
    public static String FIVE_YEAR_PERFORMANCE_TITLE = "5 Year Performance Shortlist";
        
    // Current View in F&P Page
    public static String ALL_FUNDS_VIEW = "allFundsView";
    public static String CONTRACT_VIEW = "contractView";

    // Title related Constants
    public static String FUND_MENU_ID = "fundMenu";
    public static String SHORTLIST_TYPE_ID = "shortlistType";
    public static String GENERIC_MODIFIED_ID = "genericModified";
    
    public static String PREPARED_ON_TITLE = "Prepared on ";
    public static String JH_SIGNATURE_TITLE = "JH Signature ";
    public static String CONTRACT_NAME_TITLE = "Contract Name: ";
    public static String CONTRACT_NUMBER_TITLE = "Contract Number: ";
    
    // Miscellaneous Constants.
    public static String EMPTY_STRING = "";
    public static String SINGLE_SPACE = " ";
    public static String HYPHON = " - ";
    public static String OPENING_BRACE = " (";
    public static String CLOSING_BRACE = ")";
    public static String Y_SYMBOL = "Y";
    
    /**
     * This Map holds the "report Id" as the key and "report class name" as the value.
     */
    public static Map<String, String> reportIdToClassNameMap = new HashMap<String, String>();

    /**
     * This Map holds the "report Id" as the key and "report class name" as the value only for those
     * reports that belong to "Other Reports" section.
     */
    public static Map<String, String> otherReportIdToClassNameMap = new HashMap<String, String>();

    /**
     * This Map holds the "Group By (Asset Category / Risk Return Category)" as the key and a Map of
     * reportsMap as the value. reportsMap holds the "report Id" as the key and "report class name"
     * as the value.
     */
    public static Map<String, Map<String, String>> groupIdToReportIdClassNameMap = new HashMap<String, Map<String, String>>();

    /**
     * This Map holds the "report ID" as the Key and "report Title" as the Value.
     */
    public static Map<String, String> reportIdToReportTitleMap = new HashMap<String, String>();

    /**
     * This Map holds the "additional report ID" as the Key and "additional report Title" as the
     * Value.
     */
    public static Map<String, String> additionalReportIdToAdditionalTitleMap = new HashMap<String, String>();

    /**
     * This Map will hold the FundMenuID as the Key and "Fund Menu Title" as the value.
     */
    public static Map<String, String> fundsMenuIDToFundMenuTitleMap = new HashMap<String, String>();
    
    /**
     * This Map will hold the FundMenuID as the Key and "Fund Menu Num" as the value.
     */
    public static Map<String, Integer> fundsMenuIDToFundMenuNumMap = new HashMap<String, Integer>();
    
    /**
     * This Map holds the shortListkey as the Key and shortlist Title as the value.
     */
    public static Map<String, String> shortlistKeyToTitleMap = new HashMap<String, String>();
    
    /**
     * This Map holds the lifestyle portfolio.
     */
    public static final Map<String, String> lifestylePortfolios = new LinkedMap(); 
    
     
    public static final List<String> NON_FUND_MENU_REPORTS = new ArrayList<String>();
    
    static {
    	
    	/**
         * This Map holds the "report Id" as the key and "report class name" as the value.
         */
        reportIdToClassNameMap.put(FapConstants.EXPENSE_RATIO_REPORT, AICByAssetClassReport.class
                .getName());
        reportIdToClassNameMap.put(FapConstants.FUND_CHARACTERISITICS_REPORT,
                FundCharacteristicsByAssetClassReport.class.getName());
        reportIdToClassNameMap.put(FapConstants.INVESTEMNT_RETURNS_AND_EXPENSE_RATIO_REPORT,
                IR_AICByAssetClassReport.class.getName());
        reportIdToClassNameMap.put(FapConstants.INVESTEMNT_RETURNS_AND_STANDARD_DEVIATION_REPORT,
                InvestmentReturnsStandardDeviationsByAssetClassReport.class.getName());
        reportIdToClassNameMap.put(FapConstants.MORNINGSTAR_RATINGS_AND_TICKR_SYMBOLS_REPORT,
                MorningstarRatingsAndTickersByAssetClassReport.class.getName());

        groupIdToReportIdClassNameMap.put(FapConstants.ASSET_CLASS_FILTER_KEY,
                reportIdToClassNameMap);

        reportIdToClassNameMap = new HashMap<String, String>();
        reportIdToClassNameMap.put(FapConstants.EXPENSE_RATIO_REPORT, AICByRiskReturnReport.class
                .getName());
        reportIdToClassNameMap.put(FapConstants.FUND_CHARACTERISITICS_REPORT,
                FundCharacteristicsByRiskReturnReport.class.getName());
        reportIdToClassNameMap.put(FapConstants.INVESTEMNT_RETURNS_AND_EXPENSE_RATIO_REPORT,
                IR_AICByRiskReturnReport.class.getName());
        reportIdToClassNameMap.put(FapConstants.INVESTEMNT_RETURNS_AND_STANDARD_DEVIATION_REPORT,
                InvestmentReturnsStandardDeviationsByRiskReturnReport.class.getName());
        reportIdToClassNameMap.put(FapConstants.MORNINGSTAR_RATINGS_AND_TICKR_SYMBOLS_REPORT,
                MorningstarRatingsAndTickersByRiskReturnReport.class.getName());

        groupIdToReportIdClassNameMap.put(FapConstants.RISK_CATEGORY_FILTER_KEY,
                reportIdToClassNameMap);

        
        /**
         * This Map holds the "report Id" as the key and "report class name" as the value only for
         * those reports that belong to "Other Reports" section.
         */
        otherReportIdToClassNameMap.put(FapConstants.MARKET_INDEX_REPORT, MarketReport.class
                .getName());

        
        /**
         * This Map holds the "report ID" as the Key and "report Title" as the Value.
         */
        reportIdToReportTitleMap.put(FapConstants.EXPENSE_RATIO_REPORT,
                FundReportConstants.EXPENSE_RATIO_REPORT_NAME);
        reportIdToReportTitleMap.put(FapConstants.INVESTEMNT_RETURNS_AND_EXPENSE_RATIO_REPORT,
                FundReportConstants.INVESTMENT_RETURN_AND_EXPENSE_RATIO_REPORT_NAME);
        reportIdToReportTitleMap.put(FapConstants.INVESTEMNT_RETURNS_AND_STANDARD_DEVIATION_REPORT,
                FundReportConstants.INVESTMENT_RETURN_AND_STANDARD_DEVIATION_REPORT_NAME);
        reportIdToReportTitleMap.put(FapConstants.FUND_CHARACTERISITICS_REPORT,
                FundReportConstants.FUND_CHARACTERISTICS_REPORT_NAME);
        reportIdToReportTitleMap.put(FapConstants.MORNINGSTAR_RATINGS_AND_TICKR_SYMBOLS_REPORT,
                FundReportConstants.MORNINGSTAR_RATINGS_AND_TICKER_SYMBOL_REPORT_NAME);
        reportIdToReportTitleMap.put(FapConstants.MARKET_INDEX_REPORT,
                FundReportConstants.MARKET_INDEX_REPORT_NAME);
       
        
        /**
         * This Map will hold the FundMenuID as the Key and "Fund Menu Title" as the value.
         */
        fundsMenuIDToFundMenuTitleMap.put(ALL_FUNDS_MENU_ID, ALL_FUNDS_MENU_TITLE);
        fundsMenuIDToFundMenuTitleMap.put(RETAIL_MENU_ID, RETAIL_MENU_TITLE);
        fundsMenuIDToFundMenuTitleMap.put(SUB_ADVISED_MENU_ID, SUB_ADVISED_MENU_TITLE);
        
        
        /**
         * This Map will hold the FundMenuID as the Key and "Fund Menu Num" as the value.
         */
        fundsMenuIDToFundMenuNumMap.put(ALL_FUNDS_MENU_ID, ALL_FUNDS_MENU_NUM);
        fundsMenuIDToFundMenuNumMap.put(RETAIL_MENU_ID, RETAIL_MENU_NUM);
        fundsMenuIDToFundMenuNumMap.put(SUB_ADVISED_MENU_ID, SUB_ADVISED_MENU_NUM);
        
        
        /**
         * This Map holds the shortListkey as the Key and shortlist Title as the value.
         */
        shortlistKeyToTitleMap.put(FapConstants.SHORTLIST_TYPE_LOWEST_COST_KEY,
                LOWEST_COST_SHORTLIST_TITLE);
        shortlistKeyToTitleMap.put(FapConstants.SHORTLIST_TYPE_TOP_PREFORMER_KEY,
                TOP_PERFORMER_SHORTLIST_TITLE);
        shortlistKeyToTitleMap.put(FapConstants.SHORTLIST_TYPE_3_YR_PERFORMANCE_KEY,
                THREE_YEAR_PERFORMANCE_TITLE);
        shortlistKeyToTitleMap.put(FapConstants.SHORTLIST_TYPE_5_YR_PERFORMANCE_KEY,
                FIVE_YEAR_PERFORMANCE_TITLE);

        /**
         * This Map holds the FundNames and superScript values.
         * 
         */
      
      
        
    }
}
