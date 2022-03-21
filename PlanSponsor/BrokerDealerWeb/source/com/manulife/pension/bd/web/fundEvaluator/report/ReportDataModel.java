package com.manulife.pension.bd.web.fundEvaluator.report;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.fundEvaluator.common.CoreToolConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.BenchmarkMetrics;
import com.manulife.pension.service.fund.valueobject.FundClassVO;
import com.manulife.pension.service.fund.valueobject.InvestmentGroup;

/**
 * This class models data by Asset class or Risk category
 * @author PWakode
 */

public class ReportDataModel implements CoreToolConstants {
    
    private static final Logger logger = Logger.getLogger(ReportDataModel.class);

	private com.manulife.pension.bd.web.fundEvaluator.report.ReportInputData reportInputData;
	
	public final static String PERCENT_SYMBOL = "%";
	public final static String TRUE = "true";
	public final static String FALSE = "false";
    
	//As of Date related constants
    public final static String ASOFDATE_FUNDMETRICS_KEY = "MET";
    public final static String ASOFDATE_ROR_KEY = "ROR";
    public final static String ASOFDATE_RORQE_KEY = "RORQE";//Quarter end
    public final static String ASOFDATE_FUND_EXP_RATIO_KEY = "FER";
    
    public final static String SEVEN_DAY_YIELD_DISCLOSURE = "7DY";
    public final static String FCR_DICLOSURE = "FCR";//Forward Looking Credit rate disclosure
    public final static String GUARANTEED = "GUARANTEED";

	private Map<String, AssetClassStyle> assetClassStyleTable = new Hashtable<String, AssetClassStyle>();
	private Map<Integer, InvestmentCategory> investmentCategoryTable = new Hashtable<Integer, InvestmentCategory>();
	private Map<String, String> fundFootnoteSymbolsInReport;

	private boolean isNoteRequiredForBrokerRecommendedFunds = false;

	private boolean isAICCriteriaSelected;
	private int[] orderedSelectedCriteriaIndices;
	private int[] orderedCriteriaIndices;
	private int[] onlySelectedCriteriaIndices;

	public static final String SUBSIDIARY_NY = LOCATION_NY;
	public static final String SUBSIDIARY_USA = LOCATION_USA;

	public static final String PRODUCT_TYPE_RETAIL = "RETAIL";
	public static final String PRODUCT_TYPE_VENTURE = "VENTURE";
	
	//changing labels as per UI - don't seem to be site specific any more
	public static final String PRODUCT_LABEL_RETAIL = "Retail Menu";
	public static final String PRODUCT_LABEL_VENTURE = "Sub-Advised Menu";
	public static final String PRODUCT_LABEL_ALL = "All Funds Menu";

	private static final Map<String, String> PRODUCT_LABELS = new HashMap<String, String>();
	
	static {
        PRODUCT_LABELS.put(FUND_MENU_RETAIL, PRODUCT_LABEL_RETAIL);
        PRODUCT_LABELS.put(FUND_MENU_VENTURE, PRODUCT_LABEL_VENTURE);      
        PRODUCT_LABELS.put(FUND_MENU_ALL, PRODUCT_LABEL_ALL);
    }
	
	private static final Map<String, String> CLASS_LABELS = new HashMap<String, String>(); 
	
	static {
	    try{
		    FundServiceDelegate delegate = FundServiceDelegate.getInstance();
		    
		    Map<String, FundClassVO> fundClassesMap = delegate.getAllFundClasses();
            
            Collection<FundClassVO> fundClasses = fundClassesMap.values();
            for (FundClassVO fundClass : fundClasses) {
                CLASS_LABELS.put(fundClass.getFundClassId(), fundClass.getFundClassLongName());
            }
		}
	    catch(Throwable e){
            logger.error("Problem occured in ReportDataModel initialization of static block for Class list initialization");
        }
	}
	
	public static final int[] DESCENDING_ORDERED_PERCENTILE_RANKS = new int[] {
        PERCENTILE_MAXIMUM, PERCENTILE_MEDIAN, PERCENTILE_MINIMUM
	};

	public static final Integer[] INVESTMENT_CATEGORY_IDS = new Integer[] {
		INVESTMENT_CATEGORY_ID_AGGRESSIVE_GROWTH,
		INVESTMENT_CATEGORY_ID_GROWTH,
		INVESTMENT_CATEGORY_ID_GROWTH_AND_INCOME,
		INVESTMENT_CATEGORY_ID_INCOME,
		INVESTMENT_CATEGORY_ID_CONSERVATIVE,
		INVESTMENT_CATEGORY_ID_LIFECYCLE,
		INVESTMENT_CATEGORY_ID_LIFESTYLE,
		INVESTMENT_CATEGORY_ID_GIFL
	};

	public static final Hashtable<String, String> REGISTERED_NAME_TABLE = new Hashtable<String, String>();

	static {
		REGISTERED_NAME_TABLE.put("Franklin Templeton", "Franklin® Templeton®");
		REGISTERED_NAME_TABLE.put("Fidelity", "Fidelity®");
		REGISTERED_NAME_TABLE.put("MFS", "MFS®");
	}

	//asset class related constants
	public static final String ASSET_CLASS_STYLE_ID_LARGE_CAP_VALUE = 			ASSET_CLASS_ID_LARGE_CAP_VALUE;
	public static final String ASSET_CLASS_STYLE_ID_LARGE_CAP_BLEND = 			ASSET_CLASS_ID_LARGE_CAP_BLEND;
	public static final String ASSET_CLASS_STYLE_ID_LARGE_CAP_GROWTH = 			ASSET_CLASS_ID_LARGE_CAP_GROWTH;
	public static final String ASSET_CLASS_STYLE_ID_MID_CAP_VALUE = 			ASSET_CLASS_ID_MID_CAP_VALUE;
	public static final String ASSET_CLASS_STYLE_ID_MID_CAP_BLEND = 			ASSET_CLASS_ID_MID_CAP_BLEND;
	public static final String ASSET_CLASS_STYLE_ID_MID_CAP_GROWTH = 			ASSET_CLASS_ID_MID_CAP_GROWTH;
	public static final String ASSET_CLASS_STYLE_ID_SMALL_CAP_VALUE = 			ASSET_CLASS_ID_SMALL_CAP_VALUE;
	public static final String ASSET_CLASS_STYLE_ID_SMALL_CAP_BLEND = 			ASSET_CLASS_ID_SMALL_CAP_BLEND;
	public static final String ASSET_CLASS_STYLE_ID_SMALL_CAP_GROWTH =	 		ASSET_CLASS_ID_SMALL_CAP_GROWTH;
	public static final String ASSET_CLASS_STYLE_ID_MULTI_CAP_VALUE = 			ASSET_CLASS_ID_MULTI_CAP_VALUE;
	public static final String ASSET_CLASS_STYLE_ID_MULTI_CAP_BLEND = 			ASSET_CLASS_ID_MULTI_CAP_BLEND;
	public static final String ASSET_CLASS_STYLE_ID_MULTI_CAP_GROWTH = 			ASSET_CLASS_ID_MULTI_CAP_GROWTH;
	public static final String ASSET_CLASS_STYLE_ID_INTERNATIONAL_GLOBAL_VALUE = 	ASSET_CLASS_ID_INTERNATIONAL_GLOBAL_VALUE;
	public static final String ASSET_CLASS_STYLE_ID_INTERNATIONAL_GLOBAL_BLEND = 	ASSET_CLASS_ID_INTERNATIONAL_GLOBAL_BLEND;
	public static final String ASSET_CLASS_STYLE_ID_INTERNATIONAL_GLOBAL_GROWTH = 	ASSET_CLASS_ID_INTERNATIONAL_GLOBAL_GROWTH;
	public static final String ASSET_CLASS_STYLE_ID_HYBRID =					ASSET_CLASS_ID_BALANCED + ASSET_CLASS_ID_LIFECYCLE + ASSET_CLASS_ID_LIFESTYLE;
	public static final String ASSET_CLASS_STYLE_ID_INDEX = 					ASSET_CLASS_ID_INDEX;
	public static final String ASSET_CLASS_STYLE_ID_SECTOR = 					ASSET_CLASS_ID_SECTOR;
	public static final String ASSET_CLASS_STYLE_ID_HIGH_QUALITY_SHORT_TERM = 	ASSET_CLASS_ID_HIGH_QUALITY_SHORT_TERM;
	public static final String ASSET_CLASS_STYLE_ID_HIGH_QUALITY_INTERMEDIATE_TERM = 	ASSET_CLASS_ID_HIGH_QUALITY_INTERMEDIATE_TERM;
	public static final String ASSET_CLASS_STYLE_ID_HIGH_QUALITY_LONG_TERM = 			ASSET_CLASS_ID_HIGH_QUALITY_LONG_TERM;
	public static final String ASSET_CLASS_STYLE_ID_MEDIUM_QUALITY_SHORT_TERM = 		ASSET_CLASS_ID_MEDIUM_QUALITY_SHORT_TERM;
	public static final String ASSET_CLASS_STYLE_ID_MEDIUM_QUALITY_INTERMEDIATE_TERM = 	ASSET_CLASS_ID_MEDIUM_QUALITY_INTERMEDIATE_TERM;
	public static final String ASSET_CLASS_STYLE_ID_MEDIUM_QUALITY_LONG_TERM = 			ASSET_CLASS_ID_MEDIUM_QUALITY_LONG_TERM;
	public static final String ASSET_CLASS_STYLE_ID_LOW_QUALITY_SHORT_TERM = 			ASSET_CLASS_ID_LOW_QUALITY_SHORT_TERM;
	public static final String ASSET_CLASS_STYLE_ID_LOW_QUALITY_INTERMEDIATE_TERM = 	ASSET_CLASS_ID_LOW_QUALITY_INTERMEDIATE_TERM;
	public static final String ASSET_CLASS_STYLE_ID_LOW_QUALITY_LONG_TERM = 	ASSET_CLASS_ID_LOW_QUALITY_LONG_TERM;
	public static final String ASSET_CLASS_STYLE_ID_YEAR_3_GUARANTEED = 		ASSET_CLASS_ID_YEAR_3_GUARANTEED;
	public static final String ASSET_CLASS_STYLE_ID_YEAR_5_GUARANTEED = 		ASSET_CLASS_ID_YEAR_5_GUARANTEED;
	public static final String ASSET_CLASS_STYLE_ID_YEAR_10_GUARANTEED =		ASSET_CLASS_ID_YEAR_10_GUARANTEED;
	public static final String ASSET_CLASS_STYLE_ID_GLOBAL_BOND_FUND =		ASSET_CLASS_ID_GLOBAL_BOND_FUND;

	public static final String[] ASSET_CLASS_STYLE_IDS = new String[] {
		ASSET_CLASS_STYLE_ID_LARGE_CAP_VALUE,
		ASSET_CLASS_STYLE_ID_LARGE_CAP_BLEND,
		ASSET_CLASS_STYLE_ID_LARGE_CAP_GROWTH,
		ASSET_CLASS_STYLE_ID_MID_CAP_VALUE,
		ASSET_CLASS_STYLE_ID_MID_CAP_BLEND,
		ASSET_CLASS_STYLE_ID_MID_CAP_GROWTH,
		ASSET_CLASS_STYLE_ID_SMALL_CAP_VALUE,
		ASSET_CLASS_STYLE_ID_SMALL_CAP_BLEND,
		ASSET_CLASS_STYLE_ID_SMALL_CAP_GROWTH,
		ASSET_CLASS_STYLE_ID_MULTI_CAP_VALUE,
		ASSET_CLASS_STYLE_ID_MULTI_CAP_BLEND,
		ASSET_CLASS_STYLE_ID_MULTI_CAP_GROWTH,
		ASSET_CLASS_STYLE_ID_INTERNATIONAL_GLOBAL_VALUE,
		ASSET_CLASS_STYLE_ID_INTERNATIONAL_GLOBAL_BLEND,
		ASSET_CLASS_STYLE_ID_INTERNATIONAL_GLOBAL_GROWTH,
		ASSET_CLASS_STYLE_ID_HYBRID,
		ASSET_CLASS_STYLE_ID_INDEX,
		ASSET_CLASS_STYLE_ID_SECTOR,
		ASSET_CLASS_STYLE_ID_HIGH_QUALITY_SHORT_TERM,
		ASSET_CLASS_STYLE_ID_HIGH_QUALITY_INTERMEDIATE_TERM,
		ASSET_CLASS_STYLE_ID_HIGH_QUALITY_LONG_TERM,
		ASSET_CLASS_STYLE_ID_MEDIUM_QUALITY_SHORT_TERM,
		ASSET_CLASS_STYLE_ID_MEDIUM_QUALITY_INTERMEDIATE_TERM,
		ASSET_CLASS_STYLE_ID_MEDIUM_QUALITY_LONG_TERM,
		ASSET_CLASS_STYLE_ID_LOW_QUALITY_SHORT_TERM,
		ASSET_CLASS_STYLE_ID_LOW_QUALITY_INTERMEDIATE_TERM,
		ASSET_CLASS_STYLE_ID_LOW_QUALITY_LONG_TERM,
		ASSET_CLASS_STYLE_ID_GLOBAL_BOND_FUND,
		ASSET_CLASS_STYLE_ID_YEAR_3_GUARANTEED,
		ASSET_CLASS_STYLE_ID_YEAR_5_GUARANTEED,
		ASSET_CLASS_STYLE_ID_YEAR_10_GUARANTEED
	};

	public static final String[] ASSET_CLASS_IDS = new String[] {
		ASSET_CLASS_ID_LARGE_CAP_VALUE,
		ASSET_CLASS_ID_LARGE_CAP_BLEND,
		ASSET_CLASS_ID_LARGE_CAP_GROWTH,
		ASSET_CLASS_ID_MID_CAP_VALUE,
		ASSET_CLASS_ID_MID_CAP_BLEND,
		ASSET_CLASS_ID_MID_CAP_GROWTH,
		ASSET_CLASS_ID_SMALL_CAP_VALUE,
		ASSET_CLASS_ID_SMALL_CAP_BLEND,
		ASSET_CLASS_ID_SMALL_CAP_GROWTH,
		ASSET_CLASS_ID_INTERNATIONAL_GLOBAL_VALUE,
		ASSET_CLASS_ID_INTERNATIONAL_GLOBAL_BLEND,
		ASSET_CLASS_ID_INTERNATIONAL_GLOBAL_GROWTH,
		ASSET_CLASS_ID_BALANCED,
		ASSET_CLASS_ID_GIFL,
		ASSET_CLASS_ID_LIFECYCLE,
		ASSET_CLASS_ID_LIFESTYLE,
		ASSET_CLASS_ID_SPECIALTY,
		ASSET_CLASS_ID_SECTOR,
		ASSET_CLASS_ID_HIGH_QUALITY_SHORT_TERM,
		ASSET_CLASS_ID_HIGH_QUALITY_INTERMEDIATE_TERM,
		ASSET_CLASS_ID_HIGH_QUALITY_LONG_TERM,
		ASSET_CLASS_ID_MEDIUM_QUALITY_SHORT_TERM,
		ASSET_CLASS_ID_MEDIUM_QUALITY_INTERMEDIATE_TERM,
		ASSET_CLASS_ID_MEDIUM_QUALITY_LONG_TERM,
		ASSET_CLASS_ID_LOW_QUALITY_SHORT_TERM,
		ASSET_CLASS_ID_LOW_QUALITY_INTERMEDIATE_TERM,
		ASSET_CLASS_ID_LOW_QUALITY_LONG_TERM,
		ASSET_CLASS_ID_GLOBAL_SHORT_TERM,
        ASSET_CLASS_ID_GLOBAL_INTERMEDIATE_TERM,
        ASSET_CLASS_ID_GLOBAL_LONG_TERM,
		ASSET_CLASS_ID_YEAR_3_GUARANTEED,
		ASSET_CLASS_ID_YEAR_5_GUARANTEED,
		ASSET_CLASS_ID_YEAR_10_GUARANTEED,
	};

    public ReportDataModel (ReportInputData impl) throws SystemException{
        reportInputData = impl;
    
        init();
    }
    
    /**
     * Initializes and populates maps which categorize funds by Asset class or InvestmentCategory
     * @throws SystemException
     */
    private void init() throws SystemException {
        ReportDataProcessor dataProcessor = new ReportDataProcessor(reportInputData);
        
        populateAssetClassStyleTable(dataProcessor);
        populateInvestmentCategoryTable(dataProcessor);
        
        orderedSelectedCriteriaIndices = dataProcessor.getOrderedSelectedCriteriaIndices();
        onlySelectedCriteriaIndices = dataProcessor.getOnlySelectedCriteriaIndices();
    
        isAICCriteriaSelected = dataProcessor.getIsAICCriteriaSelected();
        
        orderedCriteriaIndices = dataProcessor.getOrderedCriteriaIndices();
        orderedCriteriaIndices = dataProcessor.getOrderedCriteriaIndices();
    
        fundFootnoteSymbolsInReport = dataProcessor.getFundFootnoteSymbolsInReport();
        isNoteRequiredForBrokerRecommendedFunds = dataProcessor.isNoteRequiredForBrokerRecommendedFunds();
    }
    
    /**
     * Populates map by Asset class id as key and value as AssetClassStyle object
     * @param dataProcessor
     * @throws SystemException
     */
    private void populateAssetClassStyleTable(ReportDataProcessor dataProcessor) throws SystemException {
        try{
            Collection<BenchmarkMetrics> allBenchmarkMetrics = FundServiceDelegate.getInstance().getAllBenchmarkMetrics();//getBenchmarkMetricsObjTableForAllAssetClasses(assetClassIds);
        
            for (int i = 0; i < ASSET_CLASS_IDS.length; i++) {
                String assetClassId = ASSET_CLASS_IDS[i];
                    
                List<DecoratedFund> decoratedFunds = dataProcessor.getDecoratedFundsByAssetClass(assetClassId);
                List<DecoratedFund> percentileRankedDecoratedFunds = dataProcessor.getPercentileRankedDecoratedFundsByAssetClass(assetClassId);
                assetClassStyleTable.put(assetClassId, createAssetClassStyleFor(assetClassId, decoratedFunds, percentileRankedDecoratedFunds, allBenchmarkMetrics));
            }
        }
        catch(Throwable e){
            throw new SystemException(e, "ReportDataModel.populateAssetClassStyleTable" +
            "Problem occured while populating populating asset class table");
        }
    }

    /**
     * Populates map by Investment category id as key and value as Investment category object
     * @param dataProcessor
     * @throws SystemException
     */
    private void populateInvestmentCategoryTable(ReportDataProcessor dataProcessor) throws SystemException {
        try{
            List<String> investmentCategoryList = new ArrayList<String>();
            
            for (int i = 0; i < INVESTMENT_CATEGORY_IDS.length; i++) {
                investmentCategoryList.add(String.valueOf(INVESTMENT_CATEGORY_IDS[i]));
            }
            Hashtable<Integer, InvestmentGroup> investmentGroupObjTable = new Hashtable<Integer, InvestmentGroup>();
            
            investmentGroupObjTable = FundServiceDelegate.getInstance().getInvGroupObjTableForAllInvCatList(investmentCategoryList);
            
            for (int i = 0; i < INVESTMENT_CATEGORY_IDS.length; i++) {
                List<DecoratedFund> decoratedFunds = dataProcessor.getDecoratedFundsByInvestmentCategory(INVESTMENT_CATEGORY_IDS[i]);
                investmentCategoryTable.put(INVESTMENT_CATEGORY_IDS[i], createInvestmentCategoryFor(INVESTMENT_CATEGORY_IDS[i], decoratedFunds, investmentGroupObjTable));
            }
        }
        catch(Throwable e){
            throw new SystemException(e, "ReportDataModel.populateInvestmentCategoryTable" +
            "Problem occured while populating Investment category table");
        }
    }
    
    /**
     * Utility method to create AssetClassStyle object
     * @param assetClassId
     * @param decoratedFunds
     * @param percentileRankedDecoratedFunds
     * @param allBenchmarkMetrics
     * @return
     */
    private AssetClassStyle createAssetClassStyleFor(String assetClassId, List<DecoratedFund> decoratedFunds, List<DecoratedFund> percentileRankedDecoratedFunds, Collection<BenchmarkMetrics> allBenchmarkMetrics) {
        if (decoratedFunds == null || decoratedFunds.isEmpty()) {
            decoratedFunds = null;
        }
    
        if (percentileRankedDecoratedFunds == null || percentileRankedDecoratedFunds.isEmpty()) {
            percentileRankedDecoratedFunds = null;
        }
    
        return new AssetClassStyle(assetClassId, decoratedFunds, percentileRankedDecoratedFunds, allBenchmarkMetrics);
    }
    
    /**
     * Utility method to create Investment category object
     * @param investmentCategoryId
     * @param decoratedFunds
     * @param investmentGroupObjTable
     * @return
     */
    private InvestmentCategory createInvestmentCategoryFor(Integer investmentCategoryId, List<DecoratedFund> decoratedFunds, Hashtable<Integer, InvestmentGroup> investmentGroupObjTable){
        return new InvestmentCategory(investmentCategoryId, decoratedFunds, investmentGroupObjTable);
    }
    
    // given date --> result = Month dd, yyyy
    public static String formatDateLongForm(Date value) {
    	String result = "";
    	DateFormat formatter = new SimpleDateFormat("MMMMMMMM dd, yyyy");
    	result = formatter.format(value);
    	return result;
    }
    // returns only month -->September
    public static String formatDateLongFormMonthOnly(Date value) {
        String result = "";
        DateFormat formatter = new SimpleDateFormat("MMMMMMMM");
        result = formatter.format(value);
        return result;
    }
    
    //  given date --> result = MM/dd/yy 
    public static String formatDateShortUSForm(Date value) {
    	String result = "";
    	DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
    	result = formatter.format(value);
    	return result;
    }
    
    /**
     * given double value 12.34f --> result = 12.34%
     * given double value 12.3f --> result = 12.30%
     */
    public static String formatPercent(double value) {
    	return (formatRatio(value) + PERCENT_SYMBOL);
    }
    
    public static String formatPercent(BigDecimal value) {
        if (value != null) {
            return (formatRatio(value) + PERCENT_SYMBOL);
        }
        else{
            return ReportDataModel.NON_APPLICABLE_STRING;
        }
    }
    
    /**
     * validates whether the given String is a valid decimal value.
     * If TRUE, returns the formatted value
     * If FALSE, returns the given String  
     * 
     * @param valueAsString
     * @return
     */
    public static String formatPercent(String valueAsString) {
    	
    	BigDecimal valueAsBigDecimal = null;
    	
    	if (valueAsString != null) {
	    	try {
	    		valueAsBigDecimal = new BigDecimal(valueAsString);
	    	} catch(NumberFormatException ne) {
	    		return valueAsString;
	    	}
    	}
    	
    	return formatPercent(valueAsBigDecimal);
    }
    
    public static String formatRatio(double value) {
    	String result = "";
    	NumberFormat formatter = DecimalFormat.getNumberInstance();
    	formatter.setMaximumFractionDigits(2);
    	formatter.setMinimumFractionDigits(2);
    	result = formatter.format(value);
    	return result;
    }
    public static String formatRatio(BigDecimal value) {
        String result = ReportDataModel.NON_APPLICABLE_STRING;
        
        if (value != null) {
            result = value.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
        return result;
    }
    
    public static String formatRORValue(double value) {
        
        String result = ReportDataModel.NON_APPLICABLE_STRING;
        
        if (value != 0.00f) {
            result = ReportDataModel.formatPercent(value);
        }   
                
        return result;  
    }
    
    public static String formatRORValue(BigDecimal value) {
        
        String result = ReportDataModel.NON_APPLICABLE_STRING;
        
        if (value != null) {
            result = ReportDataModel.formatPercent(value);
        }   
                
        return result;  
    }

    public String getPresenterFirmName(){
    	return reportInputData.getPresenterFirmName();
    }
    public String getPresenterName(){
    	return reportInputData.getPresenterName();
    }
    
    public String getPreparedForCompanyName(){
    	return reportInputData.getPreparedForCompanyName();
    }
    
    public java.util.Date getAsOfDate(){
    	return reportInputData.getAsOfDate();
    }
    
    public AssetClassStyle getAssetClassStyleFor(String assetClassStyleId){
    	return assetClassStyleTable.get(assetClassStyleId);
    }
    
    public String getContractNumber(){
    	return reportInputData.getContractNumber();
    }
    
    public  LinkedHashMap<Integer, Integer> getCriteriaWeightings() {
    	return reportInputData.getCriteriaWeightings();
    }
    
    public String[] getColorForCriteria() {
    	return reportInputData.getColorForCriteria();
    }
    
    public String[] getOptionalSectionIds() {
        return reportInputData.getOptionalSectionIds();
    }
    
    public String getCoverSheetImageType() {
        return reportInputData.getCoverSheetImageType();
    }
    
    public boolean isBrokerFirmSmithBarneyAssociated() {
        return reportInputData.isBrokerFirmSmithBarneyAssociated();
    }
    
    public String getContractBaseClass() {
        return reportInputData.getContractBaseClass();
    }
    
    public String getContractBaseFundPackageSeries() {
        return reportInputData.getContractBaseFundPackageSeries();
    }
    
    public String getContractLocationId() {
        return reportInputData.getContractLocationId();
    }
    
    public Map<String, String> getFundFootnoteSymbolsInReport() {
    	return fundFootnoteSymbolsInReport;
    }
    
    public InvestmentCategory getInvestmentCategoryFor(Integer investmentCategoryId) {
    	InvestmentCategory investmentCategory = investmentCategoryTable.get(investmentCategoryId);
    	return investmentCategory;
    }
    
    public String getManulifeName() {
    	// currently the same
    	return getManulifeShortName();
    }
    
    public String getFundOffering() {
        String result = (String)PRODUCT_LABELS.get(reportInputData.getFundMenu());
        return result;
    }
    
    public String getClassLabel() {
        String result = CLASS_LABELS.get(reportInputData.getClassMenu());
        return result;
    }
    
    public String getContractBaseClassLabel(String baseClassRateType) {
        String result = CLASS_LABELS.get(baseClassRateType);
        return result;
    }
    
    public String getManulifeShortName() {
    	return (isNY()) ? MANULIFE_SHORT_NAME_NY : MANULIFE_SHORT_NAME_USA;
    }
    
    public int getNumberOfCriteriaSelected() {
    	return orderedSelectedCriteriaIndices.length;
    }
    
    public int[] getOrderedCriteriaIndices() {
    	return	orderedCriteriaIndices;
    }
    
    public int[] getOrderedSelectedCriteriaIndices() {
    	// returns only indices of criteria that were selected. 
    	return orderedSelectedCriteriaIndices;
    }
    
    public int[] getOnlySelectedCriteriaIndices() {
        // returns only indices of criteria that were selected. 
        return onlySelectedCriteriaIndices;
    }
    
    public String getSubsidiaryId() {
    	return  reportInputData.getSubsidiaryId();
    }
    
    public boolean isNY() {
    	return (LOCATION_NY.equals(getSubsidiaryId()));
    }

    public boolean isAICCriteriaSelected() {
    	return isAICCriteriaSelected;
    }

    public boolean isNoteRequiredForBrokerRecommendedFunds() {
    	return isNoteRequiredForBrokerRecommendedFunds;
    }

    public boolean isNML() {
    	return reportInputData.isNML();
    }

    public boolean isIncludeClosedFunds() {
        return reportInputData.isIncludeClosedFunds();
    }
    
    /**
	 * @return includeGIFLSelectFunds
	 */
    public boolean isIncludeGIFLSelectFunds() {
        return reportInputData.isIncludeGIFLSelectFunds();
    }
    
}