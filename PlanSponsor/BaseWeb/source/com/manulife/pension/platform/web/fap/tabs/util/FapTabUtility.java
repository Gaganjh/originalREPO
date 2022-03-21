package com.manulife.pension.platform.web.fap.tabs.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.web.fap.tabs.FundScoreCardMetricsSelection;
import com.manulife.pension.service.fund.util.Constants;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * Utility class to create the columns(Level1 & Level2) for the tabs
 * 					
 * @author ayyalsa
 *
 */
public class FapTabUtility {

	/**
	 * static variable to store the columns for the tabs
	 */
	public static Map<String, Object> cacheMap = new HashMap<String, Object>();
	
	/**
     * static variable to store the columns for the tabs
     */
    public static Map<String, Date> asOfDates = new HashMap<String, Date>();
	
	/**
	 * static variable to store the Level2 columns for the tabs
	 */
	private static Map<String, List<ColumnsInfoBean>> level2columnsMap;
	
    /**
     * static block which creates the Level2 columns object for the tabs
     * This is used only for the Down-load CSV
     */
	static {
		if (level2columnsMap == null || level2columnsMap.isEmpty()) { 
			level2columnsMap = new HashMap<String, List<ColumnsInfoBean>>();
			
			List<ColumnsInfoBean> level2columnsList = new ArrayList<ColumnsInfoBean>();
			level2columnsList.add(new ColumnsInfoBean("fundName", "Investment Option", null, true, "val_str", true, 0, null));
			level2columnsList.add(new ColumnsInfoBean("managerName", "Manager Name<sup>*3</sup>", null, true, "val_str", true, 0, null));
			level2columnsList.add(new ColumnsInfoBean("dateIntroduced", "Inception <br/>Date<sup>3A</sup>",
                    "MM/dd/yyyy", true, "date", true, 0, null));
			level2columnsList.add(new ColumnsInfoBean("tickerSymbol", "Ticker Symbol", null, false,
                    "val_str", true, 0, null));
			level2columnsList.add(new ColumnsInfoBean("fundClassMediumName", "Class", null, true, "date", true, 0, null));
			level2columnsMap.put(FapConstants.COMMON_LEVEL2COLUMNS1, level2columnsList);
	
			level2columnsList = new ArrayList<ColumnsInfoBean>();
			level2columnsList.add(new ColumnsInfoBean("fundName", "Investment Option", null, true, "val_str", "sort_ascending", null));
			level2columnsList.add(new ColumnsInfoBean("managerName", "Manager Name<sup>*3</sup>", null, true, "val_str", null, null));
			level2columnsMap.put(FapConstants.COMMON_LEVEL2COLUMNS2, level2columnsList);
			
			level2columnsList = new ArrayList<ColumnsInfoBean>();
			level2columnsList.add(new ColumnsInfoBean("fundName", "Investment Option", null, true, "val_str", "sort_ascending", null));
			level2columnsList.add(new ColumnsInfoBean("dateIntroduced", "Inception <br/>Date<sup>3A</sup>",
                    "MM/dd/yyyy", true, "date", null, null));
			level2columnsMap.put(FapConstants.COMMON_LEVEL2COLUMNS3, level2columnsList);
			
			level2columnsList = new ArrayList<ColumnsInfoBean>();
			level2columnsList.add(new ColumnsInfoBean("riskCategory", "Risk/Return Category<sup>*48</sup>", null, false, "val_str", true, 0, null));
			level2columnsList.add(new ColumnsInfoBean("assetClassDesc", "Asset Class<sup>*47</sup>", null, false, "val_str", true, 0, null));
            level2columnsList.add(new ColumnsInfoBean("marketIndexName", "Market Index Name", null,
                    true, "val_str", true, 0, null));
			level2columnsList.add(new ColumnsInfoBean("shareClass", "Share Class", null, false, "val_str", true, 0, null));
			level2columnsList.add(new ColumnsInfoBean("underlyingFundName", "Underlying Fund Name",
                    null, true, "val_str", true, 0, null));
			level2columnsMap.put(FapConstants.FUND_INFORMATION_LEVEL2COLUMNS, level2columnsList);
			
			level2columnsList = new ArrayList<ColumnsInfoBean>();
			level2columnsList.add(new ColumnsInfoBean("ror1YrAsOfMonthEnd", "1Yr", null, true, "cur", null, null, true));
			level2columnsList.add(new ColumnsInfoBean("ror3YrAsOfMonthEnd", "3Yr", null, true, "cur", null, null, true));
			level2columnsList.add(new ColumnsInfoBean("ror5YrAsOfMonthEnd", "5Yr", null, true, "cur", null, null, true));
			level2columnsList.add(new ColumnsInfoBean("ror10YrAsOfMonthEnd", "10Yr", null, true, "cur", null, null, true));
			level2columnsList.add(new ColumnsInfoBean("rorSinceInceptionAsOfMonthEnd",
                    "Since Inception", null, true, "cur", null, null, true));
			level2columnsList.add(new ColumnsInfoBean("ror3YrPercentileRankingAsOfMonthEnd", "3Yr % Rank<sup>*49</sup>", null, false, "cur", null, null));
			level2columnsMap.put(FapConstants.PERFORMANCE_AND_FEES_MONTHLY_LEVEL2COLUMNS, level2columnsList);
			
			level2columnsList = new ArrayList<ColumnsInfoBean>();
			level2columnsList.add(new ColumnsInfoBean("ror1YrAsOfQuaterEnd", "1Yr", null, true, "cur", null, null, true));
			level2columnsList.add(new ColumnsInfoBean("ror3YrAsOfQuaterEnd", "3Yr", null, true, "cur", null, null, true));
			level2columnsList.add(new ColumnsInfoBean("ror5YrAsOfQuaterEnd", "5Yr", null, true, "cur", null, null, true));
			level2columnsList.add(new ColumnsInfoBean("ror10YrAsOfQuaterEnd", "10Yr", null, true, "cur", null, null, true));
			level2columnsList.add(new ColumnsInfoBean("rorSinceInceptionQuaterEnd",
                    "Since Inception", null, true, "cur", null, null, true));
			level2columnsList.add(new ColumnsInfoBean("ror3YrPercentileRankingAsOfQuarterEnd", "3Yr % Rank<sup>*49</sup>", null, false, "cur", null, null));
			level2columnsMap.put(FapConstants.PERFORMANCE_AND_FEES_QUATERLY_LEVEL2COLUMNS, level2columnsList);
			
			level2columnsList = new ArrayList<ColumnsInfoBean>();
            level2columnsList.add(new ColumnsInfoBean("expenseRatioAsOfQuarterEnd",
                    "Expense Ratio", null, true, "cur", null, null));
            level2columnsList.add(new ColumnsInfoBean(
                    "expenseRatioPercentileRankingAsOfQuarterEnd", "Expense Ratio % Rank<sup>*50</sup>", null,
                    true, "cur", null, null));
			level2columnsMap.put(FapConstants.PERFORMANCE_AND_FEES_COMMON_LEVEL2COLUMNS, level2columnsList);
			
			level2columnsList = new ArrayList<ColumnsInfoBean>();
			level2columnsList.add(new ColumnsInfoBean("unitValue", "Unit Value<sup>*46</sup>", null, true, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("dailyChangeBydollar", "Daily Change($)",
                    null, true, "cur pct", null, null));
            level2columnsList.add(new ColumnsInfoBean("dailychangeByPercent", "Daily Change(%)",
                    null, true, "cur pct", null, null));
			level2columnsList.add(new ColumnsInfoBean("oneMonthRORAsOfMonthEnd", "1Mth", null, true, "cur pct", null, null));
			level2columnsList.add(new ColumnsInfoBean("threeMonthRORAsOfMonthEnd", "3Mth", null, true, "sub pct", null, null));
			level2columnsList.add(new ColumnsInfoBean("ytdRORAsOfMonthEnd", "YTD", null, true, "sub cur pct", null, null));
			level2columnsMap.put(FapConstants.PRICES_YTD_LEVEL2COLUMNS, level2columnsList);
			
			level2columnsList = new ArrayList<ColumnsInfoBean>();
			level2columnsList.add(new ColumnsInfoBean("threeYearSDAsOfQuaterEnd", "3Yr", null, true, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("fiveYearSDAsOfQuaterEnd", "5Yr", null, true, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("tenYearSDAsOfQuaterEnd", "10Yr", null, true, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("threeYrInvestmentSDPercentileRanking",
                    "Standard Deviation 3Yr % Rank<sup>*49</sup> ", null, true, "cur", null, null, true));
			level2columnsMap.put(FapConstants.STANDARD_DEVIATION_LEVEL2COLUMNS, level2columnsList);
			
			level2columnsList = new ArrayList<ColumnsInfoBean>();
			level2columnsList.add(new ColumnsInfoBean("trackingError", "Tracking Error", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("trackingErrorPercent", "Tracking Error % Rank", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("upsideCapture", "Upside Capture", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("upsideCapturePercent", "Upside Capture % Rank", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("downsideCapture", "Downside Capture", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("downsideCapturePercent", "Downside Capture % Rank", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("beta", "Beta", null, true, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("betaPercent", "Beta % Rank", null, false, "cur", null, null));
			level2columnsMap.put(FapConstants.FUND_CHAR2_LEVEL2COLUMNS, level2columnsList);
			
			level2columnsList = new ArrayList<ColumnsInfoBean>();
			level2columnsList.add(new ColumnsInfoBean("alpha", "Alpha", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("alphaPercent", "Alpha % Rank", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("rsquared", "R-Squared", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("rsquaredPercent", "R-Squared % Rank", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("sharpeRatio", "Sharpe Ratio", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("sharpeRatioPercent", "Sharpe Ratio % Rank", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("infoRatio", "Information Ratio", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("infoRatioPercent", "Information Ratio % Rank", null, false, "cur", null, null));
			level2columnsMap.put(FapConstants.FUND_CHAR1_LEVEL2COLUMNS, level2columnsList);
			
			level2columnsList = new ArrayList<ColumnsInfoBean>();
			level2columnsList.add(new ColumnsInfoBean("category", "Morningstar Category<sup>*7</sup>", null,
                    true, "name", null, null));
            level2columnsList.add(new ColumnsInfoBean("rating", "Morningstar Overall Rating", null, true,
                    "name", null, null));
			level2columnsList.add(new ColumnsInfoBean(
					"threeYearNumberofFundsInCategory",
					"Morningstar # Funds in Cat", null, false, "name", true, 1,
					null));
			level2columnsList.add(new ColumnsInfoBean("ror1Month", "1Mth", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("ror3Month", "3Mth", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("rorYTD", "YTD", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("ror1Yr", "1Yr", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("ror3Yr", "3Yr", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("ror5Yr", "5Yr", null, false, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("ror10Yr", "10Yr", null, false, "cur", null, null));			
			level2columnsList.add(new ColumnsInfoBean("sd3Yr", "3Yr", null, true, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("sd5Yr", "5Yr", null, true, "cur", null, null));
			level2columnsList.add(new ColumnsInfoBean("sd10Yr", "10Yr", null, true, "cur", null, null));			
			level2columnsList.add(new ColumnsInfoBean("averageER", "Cat Avg Expense Ratio<sup>*8</sup>", null, false, "cur", null, null));
			level2columnsMap.put(FapConstants.MORNINGSTAR_LEVEL2COLUMNS, level2columnsList);
			
			level2columnsList = new ArrayList<ColumnsInfoBean>();
            level2columnsList.add(new ColumnsInfoBean("ror1YrAsOfQuaterEnd", "1Yr", null, true,
                    "cur", null, null, true));
            level2columnsList.add(new ColumnsInfoBean("ror3YrAsOfQuaterEnd", "3Yr", null, true,
                    "cur", null, null, true));
            level2columnsList.add(new ColumnsInfoBean("ror5YrAsOfQuaterEnd", "5Yr", null, true,
                    "cur", null, null, true));
            level2columnsList.add(new ColumnsInfoBean("ror10YrAsOfQuaterEnd", "10Yr", null, true,
                    "cur", null, null, true));
            level2columnsList.add(new ColumnsInfoBean("rorSinceInceptionQuaterEnd",
                    "Since Inception", null, true, "cur", null, null, true));
            level2columnsList.add(new ColumnsInfoBean("expenseRatioAsOfQuarterEnd",
                    "Expense Ratio", null, true, "cur", null, null));
            level2columnsMap.put(FapConstants.PRICES_YTD_LEVEL2COLUMNS_CSV, level2columnsList);
            
     	}
	}
	
	/**
     * returns the map of level 2 columns
     * 
     * @return Map<String, List<FapTabColumnsInfoBean>>
     */
    public static Map<String, List<ColumnsInfoBean>> getLevel2columnsMap() {
        return level2columnsMap;
    }
	
    /**
	 * Creates the columns for the FundInformation tab. 
	 * The create column info beans are placed in a static map, only for the 
	 * column sorting option the beans are taken for the static map. 
	 * 
	 * FundInformation tab does not have Level-1 columns
	 * 
	 * @return Map, which has the Level2 columns  
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, List> createFundInformationTabColumns() {
		
		HashMap<String, List> fundInformationTabColumnsMap = new HashMap<String, List>();
		
		// Fund Information tab doesn't have 2 Levels of heading. 
		//i.e it has only Level 2 column headings
		
		/*
		 * Create Level-2 Columns 
		 */
		List<ColumnsInfoBean> level2ColumnsList = new ArrayList<ColumnsInfoBean>();
		level2ColumnsList.add(new ColumnsInfoBean("fundName", "Investment Option", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("managerName", "Manager Name<sup>*3</sup>", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("dateIntroduced", "Inception <br/>Date<sup>3A</sup>", "MM/dd/yyyy", false, "date", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("fundClassMediumName", "Class", null, false, "date", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("underlyingFundName", "Underlying <br/>Fund Name", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("shareClass", "Share Class", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("tickerSymbol", "Ticker<br/> Symbol", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("assetClassDesc", "Asset Class<sup>*47</sup>", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("riskCategory", "Risk/Return Category<sup>*48</sup>", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("marketIndexName", "Market Index<br/> Name", null, false, "val_str", true, 0, null));
		
		// Set the column headings - Level 2 List to the HashMap
		fundInformationTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_2, level2ColumnsList);
		
		// Place the HashMap in the Cache
		cacheMap.put(FapConstants.FUND_INFORMATION_TAB_ID, fundInformationTabColumnsMap);
		
		return fundInformationTabColumnsMap;
	}
	
	/**
	 * Creates the columns for the Performance Fees tab (monthly columns).
	 * The create column info beans are placed in a static map, only for the 
	 * column sorting option the beans are taken for the static map. 
	 * 
	 * PerformanceAndFees tab has both Level-1 & Level-2 columns
	 * The Level-1 columns also has a sub-tab in it. this information is kept
	 * in the ColumnsToggleInfoBean Object
	 * 
	 * @return Map, which has the level1 / Level2 columns  
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, List> createPerformanceAndFeesMonthlyTabColumns() {
		
		HashMap<String, List> performanceFeesTabColumnsMap = new HashMap<String, List>();
			
		// Create Column Headings - Level 1 List
		List<List<ColumnsInfoBean>> level1ColumnsList = new ArrayList<List<ColumnsInfoBean>>();

		/*
		 * The Level 1 column headings may be in one or more rows. For each row, 
		 * create a column List and set in the rows list,
		 */ 
		List<ColumnsInfoBean> level1ColumnsForRow1 = new ArrayList<ColumnsInfoBean>();
		level1ColumnsForRow1.add(new ColumnsInfoBean("&nbsp;", "val_str", "5", null));
		
		Date asOfDate = asOfDates.get("ROR");
        String formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        String text = "Average Annual Returns As Of " + formattedValue + "<sup>4A</sup>";
		
		/*
		 * The Performance and Fees Tab has a sub-tab option in the Level-1 
		 * column headings. This information is kept in the ColumnsToggleInfoBean.
		 */ 
		ColumnsInfoBean columnswithToggleOption = 
			new ColumnsInfoBean(text, "shadedRow", "5", null);
		
		ColumnsToggleInfoBean[] toggleInfoBeans = 
		{
			new ColumnsToggleInfoBean("PerformanceAndFeesQuarterly", "Quarterly", FapConstants.PERFORMANCE_FEES_URL, false),
			new ColumnsToggleInfoBean("PerformanceAndFeesMonthly", "Monthly", FapConstants.PERFORMANCE_FEES_URL, true)
		};
		
		columnswithToggleOption.setToggleInfoBeans(toggleInfoBeans);
		columnswithToggleOption.setToggleOption(true);
		
		level1ColumnsForRow1.add(columnswithToggleOption);
		
		asOfDate = asOfDates.get("MET");
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue;
        
		level1ColumnsForRow1.add(new ColumnsInfoBean(text, "shadedRow", null, null));
				
		asOfDate = asOfDates.get("FER");
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue + "<sup>5A</sup>";
        level1ColumnsForRow1.add(new ColumnsInfoBean(text, "shadedRow", null, null));
        
        asOfDate = asOfDates.get("METQE");
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue;
        level1ColumnsForRow1.add(new ColumnsInfoBean(text, "shadedRow", null, null));
		
		// Set the row list to the Column Headings - Level 1 List
		level1ColumnsList.add(level1ColumnsForRow1);
		
		// Set the column headings - Level 1 List to the HashMap
		performanceFeesTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_1, level1ColumnsList);
		
		//Create Level 2 columns
		List<ColumnsInfoBean> level2ColumnsList = new ArrayList<ColumnsInfoBean>();
		level2ColumnsList.add(new ColumnsInfoBean("fundName", "Investment Option", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("managerName", "Manager Name<sup>*3</sup>", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("dateIntroduced", "Inception Date<sup>3A</sup>", "MM/dd/yyyy", false, "date", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("tickerSymbol", "Ticker<br/> Symbol", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("fundClassMediumName", "Class", null, false, "date", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("ror1YrAsOfMonthEnd", "1Yr", "##0.00", true, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("ror3YrAsOfMonthEnd", "3Yr", "##0.00", true, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("ror5YrAsOfMonthEnd", "5Yr", "##0.00", true, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("ror10YrAsOfMonthEnd", "10Yr", "##0.00", true, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("rorSinceInceptionAsOfMonthEnd", "Since Inception", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("ror3YrPercentileRankingAsOfMonthEnd", "3Yr % Rank<sup>*49</sup>", "##0.00", false, "cur", true, 1, null));
		level2ColumnsList.add(new ColumnsInfoBean("expenseRatioAsOfQuarterEnd", "Expense Ratio", "##0.00", false, "cur", true, 1, null));
		level2ColumnsList.add(new ColumnsInfoBean("expenseRatioPercentileRankingAsOfQuarterEnd", "Expense Ratio % Rank<sup>*50</sup>", "##0.00", false, "cur", true, 1, null));
		
		// Set the column headings - Level 2 List to the HashMap
		performanceFeesTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_2, level2ColumnsList);
		
		// Place the HashMap in the Cache
		cacheMap.put("PerformanceAndFeesMonthly", performanceFeesTabColumnsMap);
		
		return performanceFeesTabColumnsMap;
	}

	/**
	 * Creates the columns for the Performance Fees tab (Quarterly columns).
	 * The create column info beans are placed in a static map, only for the 
	 * column sorting option the beans are taken for the static map. 
	 * 
	 * PerformanceAndFees tab has both Level-1 & Level-2 columns
	 * The Level-1 columns also has a sub-tab in it. this information is kept
	 * in the ColumnsToggleInfoBean Object
	 * 
	 * @return Map, which has the level1 / Level2 columns  
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, List> createPerformanceAndFeesQuarterlyTabColumns() {
		
		HashMap<String, List> performanceFeesTabColumnsMap = new HashMap<String, List>();
		
		// Create Column Headings - Level 1 List
		List<List<ColumnsInfoBean>> level1ColumnsList = new ArrayList<List<ColumnsInfoBean>>();
		
		/*
		 * The Level 1 column headings may be in one or more rows.
		 * For each row, create a column List and set in the rows list.
		 */ 
		List<ColumnsInfoBean> level1ColumnsForRow1 = new ArrayList<ColumnsInfoBean>();
		level1ColumnsForRow1.add(new ColumnsInfoBean("&nbsp;", "val_str", "5", null));
		
		Date asOfDate = asOfDates.get("RORQE");
        String formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        String text = "Average Annual Returns As Of " + formattedValue + "<sup>4A</sup>";
		
		ColumnsInfoBean columnswithToggleOption = 
			new ColumnsInfoBean(text, "shadedRow", "5", null);
		
		ColumnsToggleInfoBean[] toggleInfoBeans = 
		{
				new ColumnsToggleInfoBean("PerformanceAndFeesMonthly", "Monthly", FapConstants.PERFORMANCE_FEES_URL, false),
				new ColumnsToggleInfoBean("PerformanceAndFeesQuarterly", "Quarterly", FapConstants.PERFORMANCE_FEES_URL, true)
			
		};
		
		columnswithToggleOption.setToggleInfoBeans(toggleInfoBeans);
		columnswithToggleOption.setToggleOption(true);
		
		level1ColumnsForRow1.add(columnswithToggleOption);
		asOfDate = asOfDates.get("METQE");
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue;
		level1ColumnsForRow1.add(new ColumnsInfoBean(text, "shadedRow", null, null));
		
		asOfDate = asOfDates.get("FER");
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue + "<sup>5A</sup>";
		
		level1ColumnsForRow1.add(new ColumnsInfoBean(text, "shadedRow", null, null));
		
		asOfDate = asOfDates.get("METQE");
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue;
        level1ColumnsForRow1.add(new ColumnsInfoBean(text, "shadedRow", null, null));
		
		// Set the row list to the Column Headings - Level 1 List
		level1ColumnsList.add(level1ColumnsForRow1);
		
		// Set the column headings - Level 1 List to the HashMap
		performanceFeesTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_1, level1ColumnsList);
		
		// Create Level 2 Column Headings
		List<ColumnsInfoBean> level2ColumnsList = new ArrayList<ColumnsInfoBean>();
		
		level2ColumnsList.add(new ColumnsInfoBean("fundName", "Investment Option", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("managerName", "Manager Name<sup>*3</sup>", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("dateIntroduced", "Inception Date<sup>3A</sup>", "MM/dd/yyyy", false, "date", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("tickerSymbol", "Ticker<br/> Symbol", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("fundClassMediumName", "Class", null, false, "date", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("ror1YrAsOfQuaterEnd", "1Yr", "##0.00", true, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("ror3YrAsOfQuaterEnd", "3Yr", "##0.00", true, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("ror5YrAsOfQuaterEnd", "5Yr", "##0.00", true, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("ror10YrAsOfQuaterEnd", "10Yr", "##0.00", true, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("rorSinceInceptionQuaterEnd", "Since Inception", "##0.00", true, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("ror3YrPercentileRankingAsOfQuarterEnd", "3Yr % Rank<sup>*49</sup>", "##0.00", false, "cur", true, 1, null));
		level2ColumnsList.add(new ColumnsInfoBean("expenseRatioAsOfQuarterEnd", "Expense Ratio", "##0.00", false, "cur", true, 1, null));
		level2ColumnsList.add(new ColumnsInfoBean("expenseRatioPercentileRankingAsOfQuarterEnd", "Expense Ratio % Rank<sup>*50</sup>", "##0.00", false, "cur", true, 1, null));
		
		// Set the column headings - Level 2 List to the HashMap
		performanceFeesTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_2, level2ColumnsList);
		
		// Place the HashMap in the Cache
		cacheMap.put("PerformanceAndFeesQuarterly", performanceFeesTabColumnsMap);
		
		return performanceFeesTabColumnsMap;
	}
	
	/**
	 * Creates the columns for the Prices And YTD tab. This tab
	 * has both Level-1 and Level-2 column headers.
	 * 
	 * @return Map, which has the Level2 columns  
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, List> createPricesAndYTDTabColumns() {
		
		HashMap<String, List> pricesYTDTabColumnsMap = new HashMap<String, List>();
		
		// Create Column Headings - Level 1 List
		List<List<ColumnsInfoBean>> level1ColumnsList = new ArrayList<List<ColumnsInfoBean>>();
		
		/*
		 * The Level 1 column headings may be in one or more rows.
		 * For each row, create a column List and set in the rows list
		 */ 
		
		List<ColumnsInfoBean> level1ColumnsForRow1 = new ArrayList<ColumnsInfoBean>();
		level1ColumnsForRow1.add(new ColumnsInfoBean("&nbsp;", "val_str", "4", null, "5"));
		Date asOfDate = asOfDates.get("UV");
        String formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        
		level1ColumnsForRow1.add(new ColumnsInfoBean("Unit Values As Of " + formattedValue, "shadedRow", "3", null));
		
		asOfDate = asOfDates.get("ROR");
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
		level1ColumnsForRow1.add(new ColumnsInfoBean("Returns As Of " + formattedValue + "<sup>4A</sup>", "shadedRow", "3", null));
		
		// Set the row list to the Column Headings - Level 1 List
		level1ColumnsList.add(level1ColumnsForRow1);
		
		// Set the column headings - Level 1 List to the HashMap
		pricesYTDTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_1, level1ColumnsList);
		
		
		// Prices & YTD tab doesn't have 2 Levels of heading. i.e it has only Level 2
		List<ColumnsInfoBean> level2ColumnsList = new ArrayList<ColumnsInfoBean>();
		level2ColumnsList.add(new ColumnsInfoBean("fundName", "Investment Option", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("managerName", "Manager Name<sup>*3</sup>", null, false, "val_str", true, 0, null));

		level2ColumnsList.add(new ColumnsInfoBean("dateIntroduced", "Inception <br/>Date<sup>3A</sup>", "MM/dd/yyyy", false, "date", true, 0, null));

		level2ColumnsList.add(new ColumnsInfoBean("fundClassMediumName", "Class", null, false, "date", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("unitValue", "Unit Value<sup>*46</sup>", "##0.00", false, "cur", true, 1,null));
		level2ColumnsList.add(new ColumnsInfoBean("dailyChangeBydollar", "Daily <br/>Change ($)",
                "##0.00", false, "cur pct", true, 1, null));
        level2ColumnsList.add(new ColumnsInfoBean("dailychangeByPercent", "Daily <br/>Change (%)",
                "##0.00", false, "cur pct", true, 1, null));
		level2ColumnsList.add(new ColumnsInfoBean("oneMonthRORAsOfMonthEnd", "1Mth", "##0.00", true, "cur pct", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("threeMonthRORAsOfMonthEnd", "3Mth", "##0.00", true, "cur pct", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("ytdRORAsOfMonthEnd", "YTD", "##0.00", true, "cur pct", true, 1, null, true));
		
		// Set the column headings - Level 2 List to the HashMap
		pricesYTDTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_2, level2ColumnsList);
		
		// Place the HashMap in the Cache
		cacheMap.put(FapConstants.PRICES_YTD_TAB_ID, pricesYTDTabColumnsMap);
		
		return pricesYTDTabColumnsMap;
	}
	
	/**
	 * Creates the columns for the Standard Deviation tab. this tab
	 * has both Level-1 and Level-2 column headers
	 * 
	 * @return Map, which has the level1 / Level2 columns  
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, List> createStandardDeviationTabColumns() {
		
		HashMap<String, List> sdTabColumnsMap = new HashMap<String, List>();

		// Create Column Headings - Level 1 List
		List<List<ColumnsInfoBean>> level1ColumnsList = new ArrayList<List<ColumnsInfoBean>>();
		
		  Date asOfDate = asOfDates.get("DEVQE");
	      String formattedValue = DateRender.formatByPattern(asOfDate, null,
	                RenderConstants.MEDIUM_MDY_SLASHED);
	      String text = "Standard Deviation As Of " + formattedValue + "<sup>*15</sup>";
		
		/*
		 * The Level 1 column headings may be in one or more rows.
		 * For each row, create a column List and set in the rows list
		 */ 
		List<ColumnsInfoBean> level1ColumnsForRow1 = new ArrayList<ColumnsInfoBean>();
		level1ColumnsForRow1.add(new ColumnsInfoBean("&nbsp;", "val_str", "4", null));
		level1ColumnsForRow1.add(new ColumnsInfoBean(text, "shadedRow", "3", null));
		
		asOfDate = asOfDates.get("METQE");
	    formattedValue = DateRender.formatByPattern(asOfDate, null,
	                RenderConstants.MEDIUM_MDY_SLASHED);
	    text = "As Of " + formattedValue;
	      
		level1ColumnsForRow1.add(new ColumnsInfoBean(text, "shadedRow", "1", null));
		
		// Set the row list to the Column Headings - Level 1 List
		level1ColumnsList.add(level1ColumnsForRow1);
		
		// Set the column headings - Level 1 List to the HashMap
		sdTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_1, level1ColumnsList);
		
		/*
		 * Create the Column Headings - Level 2 List. the Level 2 column 
		 * headings will always be a single row
		 */ 
		List<ColumnsInfoBean> level2ColumnsList = new ArrayList<ColumnsInfoBean>();
		level2ColumnsList.add(new ColumnsInfoBean("fundName", "Investment Option", null, false, "val_str", true, 0, "1"));
		level2ColumnsList.add(new ColumnsInfoBean("managerName", "Manager Name<sup>*3</sup>", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("dateIntroduced", "Inception<br/>Date<sup>3A</sup>", "MM/dd/yyyy", false, "date", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("fundClassMediumName", "Class", null, false, "date", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("threeYearSDAsOfQuaterEnd", "3Yr", "##0.00", true, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("fiveYearSDAsOfQuaterEnd", "5Yr", "##0.00", true, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("tenYearSDAsOfQuaterEnd", "10Yr", "##0.00", true, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("threeYrInvestmentSDPercentileRanking", "Std Dev - <br/>3Yr % Rank<sup>*49</sup> ", "##0.00", false, "cur", true, 1, null, true));
		
		// Set the column headings - Level 2 List to the HashMap
		sdTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_2, level2ColumnsList);
		
		// Place the HashMap in the Cache
		cacheMap.put(FapConstants.STANDARD_DEVIATION_TAB_ID, sdTabColumnsMap);
		
		return sdTabColumnsMap;
	}
	
	/**
	 * Creates the columns for the Fund Characterics1 tab. This tab has 
	 * only the Level-2 column headers 
	 * 
	 * @return Map, which has the Level2 columns  
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, List> createFundCharacteristics1TabColumns() {
	
		HashMap<String, List> fundCharITabColumnsMap = new HashMap<String, List>();
		
		// Create Column Headings - Level 1 List
		List<List<ColumnsInfoBean>> level1ColumnsList = new ArrayList<List<ColumnsInfoBean>>();
		
		Date asOfDate = asOfDates.get("MET");
        String formattedValue = DateRender.formatByPattern(asOfDate, null,
                 RenderConstants.MEDIUM_MDY_SLASHED);
        String text = "As Of " + formattedValue ;
        
		/*
		 * The Level 1 column headings may be in one or more rows.
		 * For each row, create a column List and set in the rows list
		 */ 
		List<ColumnsInfoBean> level1ColumnsForRow1 = new ArrayList<ColumnsInfoBean>();
		level1ColumnsForRow1.add(new ColumnsInfoBean("&nbsp;", "val_str", "5", null));
		level1ColumnsForRow1.add(new ColumnsInfoBean(text, "shadedRow", "8", null));
		
		// Set the row list to the Column Headings - Level 1 List
		level1ColumnsList.add(level1ColumnsForRow1);
		
		// Set the column headings - Level 1 List to the HashMap
		fundCharITabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_1, level1ColumnsList);
		
		/*
		 * Create the Column Headings - Level 2 List. 
		 * the Level 2 column headings will always be a single row
		 */ 
		List<ColumnsInfoBean> level2ColumnsList = new ArrayList<ColumnsInfoBean>();
		level2ColumnsList.add(new ColumnsInfoBean("fundName", "Investment Option", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("managerName", "Manager Name<sup>*3</sup>", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("dateIntroduced", "Inception <br/>Date<sup>3A</sup>", "MM/dd/yyyy", false, "date", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("fundClassMediumName", "Class", "##0.00", false, "date", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("alpha", "Alpha", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("alphaPercent", "Alpha % Rank", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("rsquared", "R-Squared", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("rsquaredPercent", "R-Squared % Rank", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("sharpeRatio", "Sharpe Ratio", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("sharpeRatioPercent", "Sharpe Ratio % Rank", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("infoRatio", "Information Ratio", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("infoRatioPercent", "Information Ratio % Rank", "##0.00", false, "cur", true, 1, null, true));
		
		// Set the column headings - Level 2 List to the HashMap
		fundCharITabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_2, level2ColumnsList);
		
		// Place the HashMap in the Cache
		cacheMap.put(FapConstants.FUND_CHAR_I_TAB_ID, fundCharITabColumnsMap);
		
		return fundCharITabColumnsMap;
	}
	
	/**
	 * Creates the columns for the Fund Characterics2 tab. This tab
	 * has only level-2 column headers
	 * 
	 * @return Map, which has the Level2 columns  
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, List> createFundCharacteristics2TabColumns() {
		
		HashMap<String, List> fundCharIITabColumnsMap = new HashMap<String, List>();

		// Create Column Headings - Level 1 List
		List<List<ColumnsInfoBean>> level1ColumnsList = new ArrayList<List<ColumnsInfoBean>>();
		
		Date asOfDate = asOfDates.get("MET");
        String formattedValue = DateRender.formatByPattern(asOfDate, null,
                 RenderConstants.MEDIUM_MDY_SLASHED);
        String text = "As Of " + formattedValue ;
        
		/*
		 * The Level 1 column headings may be in one or more rows.
		 * For each row, create a column List and set in the rows list
		 */ 
		List<ColumnsInfoBean> level1ColumnsForRow1 = new ArrayList<ColumnsInfoBean>();
		level1ColumnsForRow1.add(new ColumnsInfoBean("&nbsp;", "val_str", "5", null));
		level1ColumnsForRow1.add(new ColumnsInfoBean(text, "shadedRow", "8", null));
		
		// Set the row list to the Column Headings - Level 1 List
		level1ColumnsList.add(level1ColumnsForRow1);
		
		// Set the column headings - Level 1 List to the HashMap
		fundCharIITabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_1, level1ColumnsList);
		
		/*
		 * Create the Column Headings - Level 2 List. 
		 * the Level 2 column headings will always be a single row
		 */ 
		List<ColumnsInfoBean> level2ColumnsList = new ArrayList<ColumnsInfoBean>();
		level2ColumnsList.add(new ColumnsInfoBean("fundName", "Investment Option", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("managerName", "Manager Name<sup>*3</sup>", null, false, "val_str", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("dateIntroduced", "Inception <br/>Date<sup>3A</sup>", "MM/dd/yyyy", false, "date", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("fundClassMediumName", "Class", null, false, "date", true, 0, null));
		level2ColumnsList.add(new ColumnsInfoBean("trackingError", "Tracking Error", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("trackingErrorPercent", "Tracking Error % Rank", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("upsideCapture", "Upside Capture", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("upsideCapturePercent", "Upside Capture % Rank", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("downsideCapture", "Downside Capture", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("downsideCapturePercent", "Downside Capture % Rank", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("beta", "Beta", "##0.00", false, "cur", true, 1, null, true));
		level2ColumnsList.add(new ColumnsInfoBean("betaPercent", "Beta % Rank", "##0.00", false, "cur", true, 1, null, true));
		
		// Set the column headings - Level 2 List to the HashMap
		fundCharIITabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_2, level2ColumnsList);
		
		// Place the HashMap in the Cache
		cacheMap.put(FapConstants.FUND_CHAR_II_TAB_ID, fundCharIITabColumnsMap);
		
		return fundCharIITabColumnsMap;
	}
	
	/**
	 * Creates the columns for the Morningstar tab. This tab has both
	 * Level-1 and Level-2 column headers
	 * 
	 * @param format
	 * 
	 * @return Map, which has the level1 / Level2 columns
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, List> createMorningstarTabColumns(
			String format) {

		HashMap<String, List> morningstarTabColumnsMap = new HashMap<String, List>();

		// Create Column Headings - Level 1 List
		List<List<ColumnsInfoBean>> level1ColumnsList = new ArrayList<List<ColumnsInfoBean>>();

		Date asOfDate = asOfDates.get(FapConstants.CONTEXT_MSP);
		String formattedValue = DateRender.formatByPattern(asOfDate, null,
				RenderConstants.MEDIUM_MDY_SLASHED);
		String text = null;
		if (format != null && FapConstants.CSV_FORMAT.equals(format)) {
			text = "Morningstar Category";
		} else {
			text = "Morningstar Information As Of " + formattedValue;
		}

		/*
		 * The Level 1 column headings may be in one or more rows. For each row,
		 * create a column List and set in the rows list The Morningstar tab has
		 * 2 rows, so morningstarTabLevel1ColumnsList will have to List
		 * objects(columnsForRow1 and columnsForRow2)
		 */
		// Create the columnList for the Row1
		List<ColumnsInfoBean> level1ColumnsForRow = new ArrayList<ColumnsInfoBean>();
		if (format != null && FapConstants.CSV_FORMAT.equals(format)) {
			level1ColumnsForRow.add(new ColumnsInfoBean(FapConstants.SPACE,
					FapConstants.VAL_STR, "5", "2"));
			level1ColumnsForRow.add(new ColumnsInfoBean(text,
					FapConstants.SHADED_ROW, "7", null));
			level1ColumnsForRow.add(new ColumnsInfoBean(text,
					FapConstants.SHADED_ROW, "3", null));
			level1ColumnsForRow.add(new ColumnsInfoBean(text,
					FapConstants.SHADED_ROW, null, null));
		} else {
			level1ColumnsForRow.add(new ColumnsInfoBean(FapConstants.SPACE,
					FapConstants.VAL_STR, "2", "2", "3"));
			level1ColumnsForRow.add(new ColumnsInfoBean(text,
					FapConstants.SHADED_ROW, "14", null));
		}
		// Set the row1 column list to the Column Headings - Level 1 List
		level1ColumnsList.add(level1ColumnsForRow);

		// Create the columnList for the Row2
		level1ColumnsForRow = new ArrayList<ColumnsInfoBean>();
		if (format != null && !FapConstants.CSV_FORMAT.equals(format)) {
			level1ColumnsForRow.add(new ColumnsInfoBean(FapConstants.SPACE,
					FapConstants.VAL_STR, "3", null));
		}
		level1ColumnsForRow.add(new ColumnsInfoBean("Investment Returns",
				FapConstants.SHADED_ROW, "7", null));
		level1ColumnsForRow.add(new ColumnsInfoBean("Standard Deviation",
				FapConstants.SHADED_ROW, "3", null));
		level1ColumnsForRow.add(new ColumnsInfoBean("Average Expense Ratio",
				FapConstants.SHADED_ROW, null, null));

		// Set the row2 column list to the Column Headings - Level 1 List
		level1ColumnsList.add(level1ColumnsForRow);

		// Set the column headings - Level 1 List to the HashMap
		morningstarTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_1,
				level1ColumnsList);

		/*
		 * Create the Column Headings - Level 2 List. the Level 2 column
		 * headings will always be a single row
		 */
		List<ColumnsInfoBean> level2ColumnsList = new ArrayList<ColumnsInfoBean>();
		level2ColumnsList.add(new ColumnsInfoBean("fundName",
				"Investment Option", null, false, FapConstants.VAL_STR, true,
				0, null));
		level2ColumnsList.add(new ColumnsInfoBean("managerName",
				"Manager Name<sup>*3</sup>", null, false, FapConstants.VAL_STR, true, 0,
				null));
		if (format != null && FapConstants.CSV_FORMAT.equals(format)) {
			level2ColumnsList.add(new ColumnsInfoBean("category",
					"Morningstar Category<sup>*7</sup>", null, false, FapConstants.NAME,
					true, 0, null));
			level2ColumnsList.add(new ColumnsInfoBean("ratingStars", "rating",
					"Morningstar <br/>Overall <br/>Rating", null, false,
					FapConstants.NAME, true, 1, null, FapConstants.IMAGE));
			level2ColumnsList.add(new ColumnsInfoBean(
					"threeYearNumberofFundsInCategory",
					"Morningstar # Funds in Cat", FapConstants.NUMBER_FORMAT, true,
					FapConstants.NAME, true, 1, null));
		} else {
			level2ColumnsList.add(new ColumnsInfoBean("category", "Category<sup>*7</sup>",
					null, false, FapConstants.NAME, true, 0, null));
			level2ColumnsList.add(new ColumnsInfoBean("ratingStars", "rating",
					"Overall <br/>Rating", null, false, FapConstants.NAME,
					true, 1, null, "IMAGE"));
			level2ColumnsList.add(new ColumnsInfoBean(
					"threeYearNumberofFundsInCategory",
					"# <br/>Funds<br/> in Cat", FapConstants.NUMBER_FORMAT, true, FapConstants.NAME,
					true, 1, null));
		}

		level2ColumnsList
				.add(new ColumnsInfoBean("ror1Month", "1Mth",
						FapConstants.DEFAULT_CUR, true, FapConstants.CUR, true,
						1, null));
		level2ColumnsList
				.add(new ColumnsInfoBean("ror3Month", "3Mth",
						FapConstants.DEFAULT_CUR, true, FapConstants.CUR, true,
						1, null));
		level2ColumnsList
				.add(new ColumnsInfoBean("rorYTD", "YTD",
						FapConstants.DEFAULT_CUR, true, FapConstants.CUR, true,
						1, null));
		level2ColumnsList
				.add(new ColumnsInfoBean("ror1Yr", "1Yr",
						FapConstants.DEFAULT_CUR, true, FapConstants.CUR, true,
						1, null));
		level2ColumnsList
				.add(new ColumnsInfoBean("ror3Yr", "3Yr",
						FapConstants.DEFAULT_CUR, true, FapConstants.CUR, true,
						1, null));
		level2ColumnsList
				.add(new ColumnsInfoBean("ror5Yr", "5Yr",
						FapConstants.DEFAULT_CUR, true, FapConstants.CUR, true,
						1, null));
		level2ColumnsList
				.add(new ColumnsInfoBean("ror10Yr", "10Yr",
						FapConstants.DEFAULT_CUR, true, FapConstants.CUR, true,
						1, null));
		level2ColumnsList
				.add(new ColumnsInfoBean("sd3Yr", "3Yr",
						FapConstants.DEFAULT_CUR, true, FapConstants.CUR, true,
						1, null));
		level2ColumnsList
				.add(new ColumnsInfoBean("sd5Yr", "5Yr",
						FapConstants.DEFAULT_CUR, true, FapConstants.CUR, true,
						1, null));
		level2ColumnsList
				.add(new ColumnsInfoBean("sd10Yr", "10Yr",
						FapConstants.DEFAULT_CUR, true, FapConstants.CUR, true,
						1, null));
		level2ColumnsList.add(new ColumnsInfoBean("averageER",
				"Cat Avg <br/>Expense Ratio<sup>*8</sup>", FapConstants.DEFAULT_CUR, false,
				FapConstants.CUR, true, 1, null));

		// Set the column headings - Level 2 List to the HashMap
		morningstarTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_2,
				level2ColumnsList);

		// Place the HashMap in the Cache
		cacheMap.put(FapConstants.MORNINGSTAR_TAB_ID, morningstarTabColumnsMap);

		return morningstarTabColumnsMap;
	}
	
	/**
	 * Creates the columns for the Fund Scorecard tab. This tab has 
	 *  only Level-2 column headers
	 * 
	 * @param format
	 * 
	 * @return Map, which has the level1 / Level2 columns
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, List> createFundScorecardTabColumns(FundScoreCardMetricsSelection fundScoreCardMetricsSelection) {

		HashMap<String, List> fundscorecardTabColumnsMap = new HashMap<String, List>();

		// Create Column Headings - Level 1 List
		List<List<ColumnsInfoBean>> level1ColumnsList = new ArrayList<List<ColumnsInfoBean>>();

		Date asOfDate = asOfDates.get("FUND_SCORECARD_EFFECTIVE_DATE_KEY");
		String formattedValue = DateRender.formatByPattern(asOfDate, null,
				RenderConstants.MEDIUM_MDY_SLASHED);
		String text = "JH Signature Fund Scorecard as of " + formattedValue;

		/*
		 * The Level 1 column headings may be in one or more rows. For each row,
		 * create a column List and set in the rows list
		 */
		List<ColumnsInfoBean> level1ColumnsForRow1 = new ArrayList<ColumnsInfoBean>();
		level1ColumnsForRow1.add(new ColumnsInfoBean("&nbsp;", "val_str", "6",
				null, "7"));
		level1ColumnsForRow1.add(new ColumnsInfoBean(text, "shadedRow", "14",
				null));

		// Set the row list to the Column Headings - Level 1 List
		level1ColumnsList.add(level1ColumnsForRow1);

		// Set the column headings - Level 1 List to the HashMap
		fundscorecardTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_1,
				level1ColumnsList);

		/*
		 * Create Level-2 Columns
		 */
		List<ColumnsInfoBean> level2ColumnsList = new ArrayList<ColumnsInfoBean>();

		level2ColumnsList.add(new ColumnsInfoBean("fundName", "Investment Option", null,
				false, FapConstants.VAL_STR_FONT, true, 0, null, 2));
		level2ColumnsList.add(new ColumnsInfoBean("dateIntroduced",
				"Inception <br/> Date<sup>3A</sup>", "MM/dd/yyyy", true, FapConstants.SCORE, true,
				0, null, 2));
		level2ColumnsList.add(new ColumnsInfoBean("tickerSymbol", "Ticker <sup>*53</sup>",
				FapConstants.DEFAULT_CUR, true, FapConstants.SCORE, true, 0,
				null, 2));
		level2ColumnsList.add(new ColumnsInfoBean("fundId", "Fund <br/> Code",
				FapConstants.DEFAULT_CUR, true, FapConstants.SCORE, true, 0,
				null, 2));
		level2ColumnsList.add(new ColumnsInfoBean("ER", "ER <sup>*54</sup>",
				FapConstants.DEFAULT_CUR, true, FapConstants.SCORE, true, 0,
				null, 2));
		level2ColumnsList.add(new ColumnsInfoBean("morningstarCategory",
				"Morningstar <br/> Category<sup>*7</sup>", FapConstants.DEFAULT_CUR, true,
				FapConstants.SCORE_WRAP, true, 0, null, 2));
		
		if(fundScoreCardMetricsSelection.isShowMorningstarScorecardMetrics()) {
			level2ColumnsList.add(new ColumnsInfoBean("morningstarRor1Yr",
					"morningstar1YrRank", "1Yr", FapConstants.DEFAULT_CUR_PCT,
					FapConstants.NUMBER_FORMAT, true, FapConstants.SCORE, true, 0,
					null));
			level2ColumnsList.add(new ColumnsInfoBean("morningstarRor5Yr",
					"morningstar5YrRank", "5Yr", FapConstants.DEFAULT_CUR_PCT,
					FapConstants.NUMBER_FORMAT, true, FapConstants.SCORE, true, 0,
					null));
			level2ColumnsList.add(new ColumnsInfoBean("morningstarRor10Yr",
					"morningstar10YrRank", "10Yr", FapConstants.DEFAULT_CUR_PCT,
					FapConstants.NUMBER_FORMAT, true, FapConstants.SCORE, true, 0,
					null));
			level2ColumnsList.add(new ColumnsInfoBean("morningstarRorYTD",
					"morningstarYTDRank", "YTD", FapConstants.DEFAULT_CUR_PCT,
					FapConstants.NUMBER_FORMAT, true, FapConstants.SCORE, true, 0,
					null));
			level2ColumnsList.add(new ColumnsInfoBean("morningstarOverallRating",
					"morningstarOverallNumberofFundsInCategory", "Overall",
					FapConstants.NUMBER_FORMAT, true, FapConstants.SCORE_BOLD, true, 0,
					false, FapConstants.MSTAR));
		}
		
		if(fundScoreCardMetricsSelection.isShowFi360ScorecardMetrics()) {
			level2ColumnsList.add(new ColumnsInfoBean("fi360OverallScore",
					"fi360OverallNumberOfPeers", "Score",
					FapConstants.NUMBER_FORMAT, true, FapConstants.SCORE,
					FapConstants.Fi360, true, 0, null));
			level2ColumnsList.add(new ColumnsInfoBean("fi3601YrScore",
					"fi3601YrNumberOfPeers", "1Yr", FapConstants.NUMBER_FORMAT,
					true, FapConstants.SCORE, FapConstants.Fi360, true, 0, null));
			level2ColumnsList.add(new ColumnsInfoBean("fi3603YrScore",
					"fi3603YrNumberOfPeers", "3Yr", FapConstants.NUMBER_FORMAT,
					true, FapConstants.SCORE, FapConstants.Fi360, true, 0, null));
			level2ColumnsList.add(new ColumnsInfoBean("fi3605YrScore",
					"fi3605YrNumberOfPeers", "5Yr", FapConstants.NUMBER_FORMAT,
					true, FapConstants.SCORE, FapConstants.Fi360, true, 0, null));
			level2ColumnsList.add(new ColumnsInfoBean("fi36010YrScore",
					"fi36010YrNumberOfPeers", "10Yr", FapConstants.NUMBER_FORMAT,
					true, FapConstants.SCORE, FapConstants.Fi360, true, 0, null));
		}
		
		if(fundScoreCardMetricsSelection.isShowRpagScorecardMetrics()) {
			level2ColumnsList.add(new ColumnsInfoBean("rpag4thLastQuarterScore",
					getQuarterNumber(asOfDate, 3), FapConstants.NUMBER_FORMAT, true,
					FapConstants.SCORE_BOLD, true, 0, null, 2, FapConstants.RPAG));
			level2ColumnsList.add(new ColumnsInfoBean("rpag3rdLastQuarterScore",
					getQuarterNumber(asOfDate, 2), FapConstants.NUMBER_FORMAT, true,
					FapConstants.SCORE_BOLD, true, 0, null, 2, FapConstants.RPAG));
			level2ColumnsList.add(new ColumnsInfoBean("rpag2ndLastQuarterScore",
					getQuarterNumber(asOfDate, 1), FapConstants.NUMBER_FORMAT, true,
					FapConstants.SCORE_BOLD, true, 0, null, 2, FapConstants.RPAG));
			level2ColumnsList.add(new ColumnsInfoBean("rpagLastQuartScore",
					getQuarterNumber(asOfDate, 0), FapConstants.NUMBER_FORMAT, true,
					FapConstants.SCORE_BOLD, true, 0, null, 2, FapConstants.RPAG));
		}
		

		// Set the column headings - Level 2 List to the HashMap
		fundscorecardTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_2,
				level2ColumnsList);

		// Place the HashMap in the Cache
		cacheMap.put(FapConstants.FUNDSCORECARD_TAB_ID,
				fundscorecardTabColumnsMap);

		return fundscorecardTabColumnsMap;
	}
	
	/**
     * returns the columns Map object for the specified tab
     * 
     * @return Map, which has the level1 / Level2 columns
     */
	@SuppressWarnings("unchecked")
	public static HashMap<String, List> getColumnsInfo(String tabName) {
		return  (HashMap<String, List>)cacheMap.get(tabName);
	}
	
	/**
     * Removes the columns Map object for the specified tab
     */
	public static void removeColumnsInfo(String tabName) {
		cacheMap.remove(tabName);
	}
	
	/**
     * Creates the columns for the PerformanceAndFees tab. This function
     * used only for the Down-load csv option. the CSV displays both the 
     * Quarterly and Monthly values
     * 
     * @return Map, which has the level1 / Level2 columns
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, List> createPerformanceAndFeesTabColumns() {

        HashMap<String, List> performanceFeesTabColumnsMap = new HashMap<String, List>();
        
        // Create Column Headings - Level 1 List
        List<List<ColumnsInfoBean>> level1ColumnsList = new ArrayList<List<ColumnsInfoBean>>();

        // The Level 1 column headings may be in one or more rows. For each row, create a column
        // List and set in the rows list,
        List<ColumnsInfoBean> level1ColumnsForRow1 = new ArrayList<ColumnsInfoBean>();
        level1ColumnsForRow1.add(new ColumnsInfoBean(" ", "val_str", "6", null));
        
        Date asOfDate = asOfDates.get("RORQE");
        String formattedValue = DateRender.formatByPattern(asOfDate, null,
                  RenderConstants.MEDIUM_MDY_SLASHED);
        String text = " Average Annual Returns As Of " + formattedValue + "<sup>4A</sup>" ;

        ColumnsInfoBean columnswithToggleOptionQuarterly = new ColumnsInfoBean(
                text, "shadedRow", "5",
                null);
        
        level1ColumnsForRow1.add(columnswithToggleOptionQuarterly);
        
        asOfDate = asOfDates.get("METQE");
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue;
        level1ColumnsForRow1.add(new ColumnsInfoBean(text, "val_str", null, null));
        
         asOfDate = asOfDates.get("ROR");
         formattedValue = DateRender.formatByPattern(asOfDate, null,
                  RenderConstants.MEDIUM_MDY_SLASHED);
         text = " Average Annual Returns As Of " + formattedValue + "<sup>4A</sup>" ;

        ColumnsInfoBean columnswithToggleOptionMonthly = new ColumnsInfoBean(
                text, "shadedRow", "5",
                null);
   
        
        level1ColumnsForRow1.add(columnswithToggleOptionMonthly);

        asOfDate = asOfDates.get("MET");
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue;
        level1ColumnsForRow1.add(new ColumnsInfoBean(text, "val_str", "1", null));
        
        asOfDate = asOfDates.get("FER");
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                 RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue + "<sup>5A</sup>" ;
        level1ColumnsForRow1.add(new ColumnsInfoBean(
                text, "shadedRow", null, null));
        
        asOfDate = asOfDates.get("METQE");
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue;
        level1ColumnsForRow1.add(new ColumnsInfoBean(text, "shadedRow", null, null));

        // Set the row list to the Column Headings - Level 1 List
        level1ColumnsList.add(level1ColumnsForRow1);

        // Set the column headings - Level 1 List to the HashMap
        performanceFeesTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_1, level1ColumnsList);

        List<ColumnsInfoBean> pfTabLevel2ColumnsInfoList = new ArrayList<ColumnsInfoBean>();
        pfTabLevel2ColumnsInfoList.addAll(level2columnsMap.get(FapConstants.COMMON_LEVEL2COLUMNS1));
        pfTabLevel2ColumnsInfoList.addAll(level2columnsMap
                .get(FapConstants.PERFORMANCE_AND_FEES_QUATERLY_LEVEL2COLUMNS));
        pfTabLevel2ColumnsInfoList.addAll(level2columnsMap
                .get(FapConstants.PERFORMANCE_AND_FEES_MONTHLY_LEVEL2COLUMNS));
        pfTabLevel2ColumnsInfoList.addAll(level2columnsMap
                .get(FapConstants.PERFORMANCE_AND_FEES_COMMON_LEVEL2COLUMNS));

        // Set the column headings - Level 2 List to the HashMap
        performanceFeesTabColumnsMap.put(FapConstants.COLUMN_HEADINGS_LEVEL_2,
                pfTabLevel2ColumnsInfoList);

        return performanceFeesTabColumnsMap;
    }
	
    
    /**
     * This is used only for the down-load CSV for all tabs option.
     * for this option, the data displayed in all the 8 tabs should be
     * displayed as a single CSV 
     * 
     * @return List of Level-1 column headers
     */
    public static List<ColumnsInfoBean> getLevel1HeadersForPricesAndYtd() {
        List<ColumnsInfoBean> level1ColumnsForRow1 = new ArrayList<ColumnsInfoBean>();
        
        Date asOfDate = asOfDates.get("RORQE");
        String formattedValue = DateRender.formatByPattern(asOfDate, null,
                  RenderConstants.MEDIUM_MDY_SLASHED);
        String text = "Returns As Of " + formattedValue + "<sup>4A</sup>" ;
        
        level1ColumnsForRow1.add(new ColumnsInfoBean(text,
                "val_str", "5", null));
        
         asOfDate = asOfDates.get("FER");
         formattedValue = DateRender.formatByPattern(asOfDate, null,
                  RenderConstants.MEDIUM_MDY_SLASHED);
         text = "As Of " + formattedValue + "<sup>5A</sup>" ;
        
        level1ColumnsForRow1.add(new ColumnsInfoBean(
                text, "shadedRow", null, null));
        return level1ColumnsForRow1;
    }

    /**
     * This is used only for the down-load CSV for all tabs option. for this option, the data
     * displayed in all the 8 tabs should be displayed as a single CSV
     * 
     * @return List of Level-1 column headers
     */
    public static List<List<ColumnsInfoBean>> createLevel1HeadersForCsvAll() {

        // The Level 1 column headings may be in one or more rows. For each row, create a column
        // List and set in the rows list,
        // Create Column Headings - Level 1 List
        List<List<ColumnsInfoBean>> level1ColumnsList = new ArrayList<List<ColumnsInfoBean>>();

        // The Level 1 column headings may be in one or more rows. For each row, create a column
        // List and set in the rows list
		// The Morningstar tab has 2 rows, so morningstarTabLevel1ColumnsList will have to List
        // objects(columnsForRow1 and columnsForRow2)

        // Create the columnList for the Row1
        List<ColumnsInfoBean> level1ColumnsForRow = new ArrayList<ColumnsInfoBean>();
        level1ColumnsForRow.add(new ColumnsInfoBean("", "", "0", "2"));
        level1ColumnsForRow.add(new ColumnsInfoBean("", "", "0", "2"));
        level1ColumnsForRow.add(new ColumnsInfoBean("", "", "0", "2"));
        level1ColumnsForRow.add(new ColumnsInfoBean("", "", "0", "2"));
        level1ColumnsForRow.add(new ColumnsInfoBean("", "", "0", "2"));
        level1ColumnsForRow.add(new ColumnsInfoBean("", "", "0", "2"));
        level1ColumnsForRow.add(new ColumnsInfoBean("", "", "0", "2"));
        level1ColumnsForRow.add(new ColumnsInfoBean("", "", "0", "2"));
        level1ColumnsForRow.add(new ColumnsInfoBean("", "", "0", "2"));
        level1ColumnsForRow.add(new ColumnsInfoBean("", "", "0", "2"));
               
        Date asOfDate = asOfDates.get(FapConstants.CONTEXT_UV);
        String formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        
        level1ColumnsForRow.add(new ColumnsInfoBean("Unit Values As Of " + formattedValue, "", "3", "2"));
        
        asOfDate = asOfDates.get(FapConstants.CONETXT_ROR);
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        level1ColumnsForRow.add(new ColumnsInfoBean("Returns As Of " + formattedValue + "<sup>4A</sup>", "", "3", "2"));
        
 
        String text = "Average Annual Returns As Of " + formattedValue +" <sup>4A</sup> Monthly";
        level1ColumnsForRow.add(new ColumnsInfoBean(
                text, "", "5", "2"));
        
        asOfDate = asOfDates.get(FapConstants.CONTEXT_MET);
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue;
        level1ColumnsForRow.add(new ColumnsInfoBean(text, "", "0", "2"));
        
        asOfDate = asOfDates.get(FapConstants.CONTEXT_RORQE);
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                 RenderConstants.MEDIUM_MDY_SLASHED);
        text = "Average Annual Returns As Of " + formattedValue + " <sup>4A</sup> Quarterly" ;
        level1ColumnsForRow.add(new ColumnsInfoBean(
                text, "", "5", "2"));
        
        asOfDate = asOfDates.get(FapConstants.CONTEXT_METQE);
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue;
        level1ColumnsForRow.add(new ColumnsInfoBean(text, "", "0", "2"));
        
        asOfDate = asOfDates.get(FapConstants.CONTEXT_FER);
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                 RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue  ;
        level1ColumnsForRow.add(new ColumnsInfoBean(text, "",
                "1", "2"));
        
        asOfDate = asOfDates.get(FapConstants.CONTEXT_METQE);
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue;
        level1ColumnsForRow.add(new ColumnsInfoBean(text, "shadedRow", "1", "2"));
        
        asOfDate = asOfDates.get(FapConstants.CONTEXT_DEVQE);
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                 RenderConstants.MEDIUM_MDY_SLASHED);
        text = "Standard Deviation As Of " + formattedValue  ;
        level1ColumnsForRow.add(new ColumnsInfoBean(text,
                "",
                "3", "2"));
        
        asOfDate = asOfDates.get(FapConstants.CONTEXT_METQE);
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                    RenderConstants.MEDIUM_MDY_SLASHED);
        text = "As Of " + formattedValue;
        level1ColumnsForRow.add(new ColumnsInfoBean(text, "shadedRow", "1", "2"));
        
         asOfDate = asOfDates.get(FapConstants.CONTEXT_MET);
         formattedValue = DateRender.formatByPattern(asOfDate, null,
                 RenderConstants.MEDIUM_MDY_SLASHED);
         text = "As Of " + formattedValue ;
        level1ColumnsForRow.add(new ColumnsInfoBean(text, "shadedRow", "16", "2"));
        
        asOfDate = asOfDates.get(FapConstants.CONTEXT_MSP);
        formattedValue = DateRender.formatByPattern(asOfDate, null,
                 RenderConstants.MEDIUM_MDY_SLASHED);

        level1ColumnsForRow.add(new ColumnsInfoBean("", "", "0", "2"));
        level1ColumnsForRow.add(new ColumnsInfoBean("", "", "0", "2"));
        level1ColumnsForRow.add(new ColumnsInfoBean("", "", "0", "2"));
        
        text = "Morningstar Category";

		level1ColumnsForRow.add(new ColumnsInfoBean(text, "", "7", null));
		level1ColumnsForRow.add(new ColumnsInfoBean(text, "", "3", null));
		level1ColumnsForRow.add(new ColumnsInfoBean(text, "", "1", null));
		
        // Set the row1 column list to the Column Headings - Level 1 List
        level1ColumnsList.add(level1ColumnsForRow);

        // Create the columnList for the Row2
        level1ColumnsForRow = new ArrayList<ColumnsInfoBean>();
        
		level1ColumnsForRow.add(new ColumnsInfoBean("Investment Returns",
				"shadedRow", "7", null));
        
        level1ColumnsForRow.add(new ColumnsInfoBean("Standard Deviation", "shadedRow", "3", null));
        
		level1ColumnsForRow.add(new ColumnsInfoBean("Average Expense Ratio ",
				"shadedRow", null, null));
		
		// Set the row2 column list to the Column Headings - Level 1 List
		level1ColumnsList.add(level1ColumnsForRow);

		// Create the columnList for the Row2
		level1ColumnsForRow = new ArrayList<ColumnsInfoBean>();

		level1ColumnsForRow
				.add(new ColumnsInfoBean("", "shadedRow", "11", null));

        // Set the row2 column list to the Column Headings - Level 1 List
        level1ColumnsList.add(level1ColumnsForRow);

        return level1ColumnsList;
    }
    
	private static String getQuarterNumber(Date asOfDate, int previousQuarterNumber) {
		Calendar calendar = Calendar.getInstance();
		if(asOfDate != null) {
		   calendar.setTime(asOfDate);
		}
		if(previousQuarterNumber > 0) {
			calendar.add(Calendar.MONTH, -3 * previousQuarterNumber);
		}
		switch (calendar.get(Calendar.MONTH)) {
		case 11:
			return " Q4 " + "<br/>" + calendar.get(Calendar.YEAR);
		case 8:
			return " Q3 " + "<br/>" + calendar.get(Calendar.YEAR);
		case 5:
			return " Q2 " + "<br/>" + calendar.get(Calendar.YEAR);
		default:
			return " Q1 " + "<br/>" + calendar.get(Calendar.YEAR);
		}
	}
}
