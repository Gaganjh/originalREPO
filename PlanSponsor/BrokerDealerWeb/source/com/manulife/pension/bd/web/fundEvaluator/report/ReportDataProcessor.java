package com.manulife.pension.bd.web.fundEvaluator.report;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.soap.util.xml.DOMWriter;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.fundEvaluator.FundEvaluatorConstants;
import com.manulife.pension.bd.web.fundEvaluator.common.CoreToolConstants;
import com.manulife.pension.bd.web.fundEvaluator.common.CoreToolGlobalData;
import com.manulife.pension.bd.web.fundEvaluator.processor.CoreToolHelper;
import com.manulife.pension.bd.web.fundEvaluator.session.SessionContext;
import com.manulife.pension.bd.web.fundEvaluator.util.FootnoteLookupHelper;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Footnote;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.view.MutableFootnote;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.cache.FootnoteCacheImpl;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.service.environment.util.BusinessParamConstants;
import com.manulife.pension.service.fund.coretool.model.common.GlobalData;
import com.manulife.pension.service.fund.coretool.model.common.SelectionCriteria;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.AssetClass;
import com.manulife.pension.service.fund.valueobject.BenchmarkMetrics;
import com.manulife.pension.service.fund.valueobject.Fund;
import com.manulife.pension.service.fund.valueobject.FundFootnote;
import com.manulife.pension.service.fund.valueobject.FundMetrics;
import com.manulife.pension.service.fund.valueobject.FundStandardDeviationAndUnitValues;
import com.manulife.pension.service.fund.valueobject.GuaranteedFundInterestRates;
import com.manulife.pension.service.fund.valueobject.HypotheticalInfo;
import com.manulife.pension.service.fund.valueobject.InvestmentGroup;
import com.manulife.pension.service.fund.valueobject.PercentileRankedFund;
import com.manulife.pension.service.fund.valueobject.SvgifFund;
import com.manulife.util.pdf.PdfConstants;
import com.manulife.util.piechart.PieChartBean;
import com.manulife.util.piechart.PieChartUtil;
import com.manulife.util.render.RenderConstants;

/**
 * This class does all data processing for FundEvaluator Pdf report
 * @author PWakode
 */

public class ReportDataProcessor implements CoreToolConstants {
    
    private static final Logger logger = Logger.getLogger(ReportDataProcessor.class);
    private ReportInputData inputData;
	
	private Map<String, DecoratedFund> decoratedFundsTable = new Hashtable<String, DecoratedFund>(); // for internal processing only! keys=fundIds

	private Map<String, String> fundFootnoteSymbolsInReport = new Hashtable<String, String>();

	private Map<Object, List<DecoratedFund>> decoratedFundsByAssetClassTable = new Hashtable<Object, List<DecoratedFund>>();
	
	private Map<String, Vector<DecoratedFund>> percentileRankedDecoratedFundsByAssetClassTable = new Hashtable<String, Vector<DecoratedFund>>();
	
	private Map<Integer, List<DecoratedFund>> decoratedFundsByInvestmentCategoryTable = new Hashtable<Integer, List<DecoratedFund>>();

	// Modified for Alignment : Performance and expence 
	private boolean isNoteRequiredForBrokerRecommendedFunds = false;
	private boolean isSelectedCriteriaContainPerformaceAttribute = false;
	private boolean isAICCriteriaSelected = false;
	
	private boolean showCalculatedFund = false;
	private boolean showManuallyAddedFund = false;
	private boolean showManuallyRemovedFund = false;
	private boolean showClosedToNBIcon = false;
	private boolean showClosedToNBIconSelectedOnly = false;
	private boolean showContractFundIcon = false;
	
	private List<String> lifecycleFundFamiliesSelected = new ArrayList<String>();
	private List<String> lifeStyleFundFamiliesSelected = new ArrayList<String>();
	
	private	int[] orderedSelectedCriteriaIndices;
	private int[] orderedCriteriaIndices;
	private int[] onlySelectedCriteriaIndices;
	
	@SuppressWarnings("unchecked")
    private static final Comparator DECORATED_FUND_COMPARATOR = new DecoratedFundComparator();
	private static final String PLACEHOLDER_STRING = new String("");
	
	private ArrayList<String> assetClassesWithSelectedFunds = new ArrayList<String>();
	
	protected static PieChartBean pieChartBean;
    private Environment env = Environment.getInstance();
    
    private static final int MAX_STRING_LENGTH = 48;
    private static final int ONE_THIRD_MAX_LENGTH = 18;
	
	public ReportDataProcessor(ReportInputData reportInputData) throws SystemException {
        this.inputData = reportInputData;
        init();
    }
    public ReportDataProcessor(){
    }
    
	/**
	 * This utility method creates Decorated Fund object
	 * @param fund
	 * @param fundLineupFundMetricTable
	 * @param fundLineupFundFootnoteTable
	 * @param fundLineupFundTable
	 * @return
	 * @throws SystemException
	 */
	private DecoratedFund createDecoratedFund(Fund fund, Hashtable<String, FundMetrics> fundLineupFundMetricTable, Hashtable<String, FundFootnote> fundLineupFundFootnoteTable, Hashtable<String, Fund> fundLineupFundTable) throws SystemException {
        String fundId = fund.getFundId();
        
        boolean isBrokerSelectedFund = isBrokerSelectedFund(fund, fundLineupFundTable);
        boolean isToolFund = isToolFund(fund, fundLineupFundTable);
        boolean isContractFund = isContractFund(fund, fundLineupFundTable);
        boolean isCheckedFund = isCheckedFund(fund, fundLineupFundTable);
        boolean isToolRecommendedFund = isToolFund(fund, fundLineupFundTable);    
    
        PercentileRankedFund percentileRankedFund = (PercentileRankedFund) inputData.getAllFundsInReport().get(fundId);
        DecoratedFund result = new DecoratedFund(fund, percentileRankedFund, fundLineupFundMetricTable, fundLineupFundFootnoteTable);
    
        result.setBrokerSelected(isBrokerSelectedFund);
        result.setToolSelected(isToolFund);
        result.setContractSelected(isContractFund);
        result.setChecked(isCheckedFund);
        result.setToolRecommended(isToolRecommendedFund);
        
        FundFootnote fundFootnote = fundLineupFundFootnoteTable.get(fundId);
        result.setFundFootnote(fundFootnote);
        result.setFundMetrics(fundLineupFundMetricTable.get(fundId + percentileRankedFund.getRateType()));
    
        //build up FundFootnoteSymbolsInReport
        if (fundFootnote != null) {
            String[] fundFootnotes = fundFootnote.getFootnoteIdsAsArray();
            for (int i = 0; i < fundFootnotes.length; i++){
                fundFootnoteSymbolsInReport.put(fundFootnotes[i], PLACEHOLDER_STRING);
            }
        }
    
        if (isBrokerSelectedFund) {
            isNoteRequiredForBrokerRecommendedFunds = true;
        }
        result.setFundManagerName(fund.getFundManagerName());
        result.setClosedToNB(fund.isClosedToNewBusiness());
        result.setIndex(fund.getAssetClassId().equals(ASSET_CLASS_ID_INDEX)? true:false);
        
        return result;
    }

	private void createOrderedCriteriaIndexArrays(LinkedHashMap<Integer, Integer> criteriaWeightings) throws SystemException {
	
		Vector<Integer> selectedCriteria = new Vector<Integer>();
		Vector<Integer> unselectedCriteria = new Vector<Integer>();
		Vector<Integer> allCriteria = new Vector<Integer>();
	
		// ordered array of selection or unselected criteria
		for (Entry<Integer, Integer> entry : criteriaWeightings.entrySet()) {
			if (entry.getValue() > 0) {
				if (SelectionCriteria.FEES == entry.getKey()) {
					isAICCriteriaSelected = true;
					isSelectedCriteriaContainPerformaceAttribute = true;
				} else if (SelectionCriteria.R2 == entry.getKey() || SelectionCriteria.INFORMATION_RATIO == entry.getKey() ||
							SelectionCriteria.SHARPE_RATIO ==entry.getKey() || SelectionCriteria.ALPHA == entry.getKey()) {
					isSelectedCriteriaContainPerformaceAttribute = true;
				}		
				selectedCriteria.addElement(entry.getKey());
			} else {
				unselectedCriteria.addElement(entry.getKey());
			}	
			 allCriteria.addElement(entry.getKey());
		}
	
		// Do some business rule validation
		if (selectedCriteria.size() < 1) {
	            throw new SystemException(
	            "At least one criterion must have a non-zero weight!");
	        
		}
		/*if (selectedCriteria.size() > 5) {
			throw new SystemException(
			        "No more than 5 criteria can have been selected. There are " +
                    selectedCriteria.size() + " non-zero weights!");
		}*/
	
		orderedSelectedCriteriaIndices = new int[criteriaWeightings.size()];
		orderedCriteriaIndices = new int[criteriaWeightings.size()];
		for (int i=0; i < allCriteria.size(); i++) {
		    orderedSelectedCriteriaIndices[i] = allCriteria.elementAt(i).intValue();
        }
		
		orderedSelectedCriteriaIndices = sortSelectedCriteriaWeights(orderedSelectedCriteriaIndices);
	
		for (int i = 0; i < orderedSelectedCriteriaIndices.length; i++) {
			orderedCriteriaIndices[i] = orderedSelectedCriteriaIndices[i];
		}	
		onlySelectedCriteriaIndices = new int[selectedCriteria.size()];
		for (int i=0; i < selectedCriteria.size(); i++) {
		    onlySelectedCriteriaIndices[i] = ((Integer)selectedCriteria.elementAt(i)).intValue();
        }
	}
	
	private void ensureNoBenchmarkMetricsForBalanceAndLifestyleAssetClasses() throws SystemException {
		
		try{
    	    if (FundServiceDelegate.getInstance().getBenchmarkMetricsForAssetClassID(ASSET_CLASS_ID_BALANCED)!= null ) {
         		throw new SystemException("Error: Benchmark metrics should not be available for " + ASSET_CLASS_ID_BALANCED);
            } else if ( FundServiceDelegate.getInstance().getBenchmarkMetricsForAssetClassID(ASSET_CLASS_ID_LIFESTYLE) != null) {
    			throw new SystemException("Error: Benchmark metrics should not be available for " + ASSET_CLASS_ID_LIFESTYLE);
    		}	
        }
        catch(SystemException e){
            throw new SystemException(e, "ensureNoBenchmarkMetricsForBalanceAndLifestyleAssetClasses : Error getting BenchmarkMetrics from FundService" + e.getMessage());
        }		
	}
	
	/**
	 * @param assetClassId
	 * @return List<DecoratedFund>
	 */
	public List<DecoratedFund> getDecoratedFundsByAssetClass(String assetClassId) {
		return decoratedFundsByAssetClassTable.get(assetClassId);
	}
	
	/**
	 * @param investmentCategoryId
	 * @return List<DecoratedFund>
	 */
	public List<DecoratedFund> getDecoratedFundsByInvestmentCategory(Integer investmentCategoryId) {
		return decoratedFundsByInvestmentCategoryTable.get(investmentCategoryId);
	}
	
	/**
	 * @return Map<String, String>
	 */
	public Map<String, String> getFundFootnoteSymbolsInReport() {
		return fundFootnoteSymbolsInReport;
	}
	
	public boolean getIsAICCriteriaSelected() {
		return isAICCriteriaSelected;
	}
	
	public boolean getIsSelectedCriteriaContainPerformaceAttribute() {
		return isSelectedCriteriaContainPerformaceAttribute;
	}
	
	/**
	 * 
	 * @return int[]
	 */
	public int[] getOrderedCriteriaIndices() {
		return orderedCriteriaIndices;
	}
	
	/**
	 * 
	 * @return int[]
	 */
	public int[] getOrderedSelectedCriteriaIndices() {
		return orderedSelectedCriteriaIndices;
	}
	public int[] getOnlySelectedCriteriaIndices() {
        return onlySelectedCriteriaIndices;
    }
	
	public Vector<DecoratedFund> getPercentileRankedDecoratedFundsByAssetClass(String assetClassId) {
		return percentileRankedDecoratedFundsByAssetClassTable.get(assetClassId);
	}

	
	/**
	 * This method initializes and populates required value objects needed for Report data generation
	 * @throws SystemException
	 */
	private void init() throws SystemException {
		initDecoratedFundVectorHashtables();
		populateDecoratedFundVectors();
		populatePercentileRankedDecoratedFundVectors();
		sortDecoratedFundByAssetClassVectors();
		sortDecoratedFundByInvestmentCategoryVectors();
		createOrderedCriteriaIndexArrays(inputData.getCriteriaWeightings());
		ensureNoBenchmarkMetricsForBalanceAndLifestyleAssetClasses();
		
		assetClassesWithSelectedFunds = new ArrayList<String>();
	}
	
	/**
     * This utility method merely initializes 3 maps( 2 with with keys as Asset class IDs and 3rd with key as Investment category IDs)
     * without populating values for the keys. These maps are populated by respective methods as below :
     * percentileRankedDecoratedFundsByAssetClassTable--> populated by method populatePercentileRankedDecoratedFundVectors
     * decoratedFundsByAssetClassTable & decoratedFundsByInvestmentCategoryTable --> populated by method populateDecoratedFundVectors() 
     */
	private void initDecoratedFundVectorHashtables() {
	    
		String[] assetClassIds = ReportDataModel.ASSET_CLASS_IDS;
	
		for (int i = 0; i < assetClassIds.length; i++) {
			decoratedFundsByAssetClassTable.put(assetClassIds[i], new Vector<DecoratedFund>()); 
			percentileRankedDecoratedFundsByAssetClassTable.put(assetClassIds[i], new Vector<DecoratedFund>()); 
		}	 
	
		Integer[] investmentCategoryIds = ReportDataModel.INVESTMENT_CATEGORY_IDS;
		for (int i = 0; i < investmentCategoryIds.length; i++) {
			decoratedFundsByInvestmentCategoryTable.put(investmentCategoryIds[i], new Vector<DecoratedFund>()); 
		}	 	
	}
	
    private boolean isBrokerSelectedFund(Fund fund, Hashtable<String, Fund> fundLineupFundTable) {
        Map<String, Fund> brokerSelectedFunds = inputData.getBrokerSelectedFunds(fundLineupFundTable);
        return brokerSelectedFunds.containsKey(fund.getFundId());
    }
    private boolean isToolFund(Fund fund, Hashtable<String, Fund> fundLineupFundTable) {
        Map<String, Fund> toolFunds = inputData.getToolRecommendedFunds(fundLineupFundTable);
        return toolFunds.containsKey(fund.getFundId());
    }
    private boolean isContractFund(Fund fund,Hashtable<String, Fund> fundLineupFundTable) {
        Map<String, Fund> contractFunds = inputData.getContractFunds(fundLineupFundTable);
        return contractFunds.containsKey(fund.getFundId());
    }
    private boolean isCheckedFund(Fund fund, Hashtable<String, Fund> fundLineupFundTable) {
        Map<String, Fund> checkedFunds = inputData.getCheckedFunds(fundLineupFundTable);
        return checkedFunds.containsKey(fund.getFundId());
    }
	
	public boolean isNoteRequiredForBrokerRecommendedFunds() {
		return isNoteRequiredForBrokerRecommendedFunds;
	}
	
	private boolean isToolRecommendedFund(Fund fund, Hashtable<String, Fund> fundLineupFundTable) {
		Map<String, String> toolRecommendedFunds = inputData.getToolRecommendedFunds();
		return toolRecommendedFunds.containsKey(fund.getFundId());
	}

    /**
     * Utility method to populate DecoratedFund objects and set them into :
     * Map with key as Asset class ID and value as List of Decorated funds
     * Map with key as Investment ID and value as List of Decorated funds
     * @throws SystemException
     */
    private void populateDecoratedFundVectors() throws SystemException {
        
        Map<String, PercentileRankedFund> allFundsInReport = inputData.getAllFundsInReport();
        
        List<String> investmentIds = new ArrayList<String>();
        List<String> investmentIdsPlusRateType = new ArrayList<String>();
        Iterator<String> fundIdKeys0 = allFundsInReport.keySet().iterator();
        while (fundIdKeys0.hasNext()) {
            String fundId = fundIdKeys0.next();
            investmentIds.add(fundId);
            PercentileRankedFund percentileRankedFund = (PercentileRankedFund) inputData.getAllFundsInReport().get(fundId);
            investmentIdsPlusRateType.add(fundId + percentileRankedFund.getRateType());
        }
        try{
            Hashtable<String, Fund> fundLineupFundTable = FundServiceDelegate.getInstance().getFundObjTableForFundList(investmentIds);

            Hashtable<String, FundMetrics> fundLineupFundMetricTable = FundServiceDelegate.getInstance().getFundMetricObjTableForFundAndRateList(investmentIdsPlusRateType);
            
            Hashtable<String, FundFootnote> fundLineupFundFootnoteTable = FundServiceDelegate.getInstance().getFundFootnoteObjTableForFundList(investmentIds);
       
            Iterator<String> fundIdKeys = allFundsInReport.keySet().iterator(); 
            while (fundIdKeys.hasNext()) {
                String fundId = fundIdKeys.next();
                
                Fund fund = fundLineupFundTable.get(fundId);
                
                DecoratedFund decoratedFund = createDecoratedFund(fund, fundLineupFundMetricTable, fundLineupFundFootnoteTable, fundLineupFundTable );
            
                //Populate decoratedFundHashtable for use in populating percentileRankedDecoratedFundVectors in PercentileRankedFunsByAssetClassTable
                decoratedFundsTable.put(fundId, decoratedFund);
        
                List<DecoratedFund> listByAssetClass = decoratedFundsByAssetClassTable.get(fund.getAssetClassId());
                listByAssetClass.add(decoratedFund);
        
                List<DecoratedFund> listByInvestmentCategories = decoratedFundsByInvestmentCategoryTable.get(new Integer(fund.getOrder()));
                listByInvestmentCategories.add(decoratedFund);  
           }
        }
        catch(Throwable e){
            logger.debug("populateDecoratedFundVectors : Problem occured while populating Decorated fund vectors" + e);
            throw new SystemException(e, "populateDecoratedFundVectors : Problem occured while populating Decorated fund vectors" + e.getMessage());
        }
    }
	
    /**
     * If funds are sorted by Asset class, secondary sorting is by Rank(best to worst), hence  this
     * utility method adds ranked funds into table with Asset class ID as key and value as collection of percentile 
     * ranked DecoratedFund objects as value
     * 
     * @throws SystemException
     */
	private void populatePercentileRankedDecoratedFundVectors() throws SystemException{
	    try{
    		Map<String, List<PercentileRankedFund>> percentileRankedFundVectors = inputData.getPercentileRankedFundVectors();
    	
    		Iterator<String> assetClassIds = percentileRankedFundVectors.keySet().iterator();
    		while (assetClassIds.hasNext()) {
    			String assetClassId = assetClassIds.next();
    			List<PercentileRankedFund> percentileRankedFunds = percentileRankedFundVectors.get(assetClassId);
    	
    			List<DecoratedFund> percentileRankedDecoratedFundVector = percentileRankedDecoratedFundsByAssetClassTable.get(assetClassId);
    			
    			for (int i = 0; i < percentileRankedFunds.size(); i++) {
    				PercentileRankedFund percentileRankedFund = (PercentileRankedFund)percentileRankedFunds.get(i);
    				String percentileRankedFundKey = percentileRankedFund.getFundID();
    				DecoratedFund decoratedFund = decoratedFundsTable.get(percentileRankedFundKey);
    				percentileRankedDecoratedFundVector.add(decoratedFund);
    			}
    		}
	    }
	    catch(Throwable e){
	        logger.debug("populatePercentileRankedDecoratedFundVectors : Problem occured while populating percentile ranked Decorated vectors" + e);
	        throw new SystemException(e, "populatePercentileRankedDecoratedFundVectors : Problem occured while populating percentile ranked Decorated vectors" + e.getMessage());
	    }
	}
	
	/**
     * If funds are sorted by Asset class, secondary sorting is by Rank(best to worst), tertiary is by sort # ascending
     * Comparator takes care of tertiary sorting only
     */
	@SuppressWarnings("unchecked")
    private void sortDecoratedFundByAssetClassVectors() {
	
		Iterator<Object> keys = decoratedFundsByAssetClassTable.keySet().iterator();
		while (keys.hasNext()) {
			Object key = keys.next();
			Vector<DecoratedFund> vector = (Vector)decoratedFundsByAssetClassTable.get(key);
			decoratedFundsByAssetClassTable.put(key, sortDecoratedFundVector(vector));
		}
	}
	
	/**
     * If funds are sorted by Investment category, secondary sorting is always by Sort # ascending
     * Comparator takes care of tertiary sorting by Sort #
     */
	private void sortDecoratedFundByInvestmentCategoryVectors() {
		Iterator<Integer> keys = decoratedFundsByInvestmentCategoryTable.keySet().iterator();
		while (keys.hasNext()) {
		    Integer key = keys.next();
			List<DecoratedFund> list = decoratedFundsByInvestmentCategoryTable.get(key);
			decoratedFundsByInvestmentCategoryTable.put(key, sortDecoratedFundVector(list));
		}
	}
	
	/**
     * Utility method which applies the comparator by sort #
     */
	@SuppressWarnings("unchecked")
    private List<DecoratedFund> sortDecoratedFundVector(List<DecoratedFund> decoratedFunds) {
		List<DecoratedFund> result = new Vector(decoratedFunds);
		Collections.sort(result, ReportDataProcessor.DECORATED_FUND_COMPARATOR);
		return result;
	}
	
	public int[] sortSelectedCriteriaWeights(int[] elements) {
		return elements;
	}
	
	@SuppressWarnings("unchecked")
    public static class RankedDecoratedFundComparator extends DecoratedFundComparator implements Comparator {
		public int compare(Object arg0, Object arg1) {
			DecoratedFund decoratedFund0 = (DecoratedFund) arg0;
			DecoratedFund decoratedFund1 = (DecoratedFund) arg1;
			
            BigDecimal rank0 = decoratedFund0.getPercentileRankedFund().getRank();
            BigDecimal rank1 = decoratedFund1.getPercentileRankedFund().getRank();
    
            if (rank0.compareTo(rank1) < 0) {
                return -1;
            } else if (rank0.compareTo(rank1) > 0) {
                return 1;
            } else {
                return super.compare(arg0, arg1);
                //Pri criteria rank used above, otherwise by asset class order, then sort # ,then name
            }
		}
	}
	
	/**
	 * If funds are sorted by Asset class, secondary sorting is by Rank(best to worst), tertiary is by sort # ascending
	 * Below comparator takes care of tertiary sorting only
	 * 
	 * If funds are sorted by Investment category, secondary sorting is always by Sort # ascending
	 *
	 */
	@SuppressWarnings("unchecked")
    public static class DecoratedFundComparator implements Comparator {
		public int compare(Object arg0, Object arg1) {
			DecoratedFund decoratedFund0 = (DecoratedFund)arg0;
			DecoratedFund decoratedFund1 = (DecoratedFund)arg1;
	
			/*int order0 = decoratedFund0.getFund().getAssetClassOrder();
			int order1 = decoratedFund1.getFund().getAssetClassOrder();
			if (order0 < order1) {
				return -1;
			} else if (order0 > order1) {
				return 1;
			} else {*/
				int sortNumber0 = decoratedFund0.getFund().getSortNumber();
				int sortNumber1 = decoratedFund1.getFund().getSortNumber();
				if (sortNumber0 < sortNumber1) {
					return -1;
				} else if (sortNumber0 > sortNumber1) {
					return 1;
				} else {
					return decoratedFund0.getFund().getFundName().compareTo(decoratedFund1.getFund().getFundName());
				}
			//}
		}
	}
    
	/**
	 * Creates fundsByAssetClass node in XML where fund line up funds are categorized by Asset houses, asset classes and fund IDs within each asset class
	 * @param rdm
	 * @param doc
	 * @param root
	 */
	@SuppressWarnings("unchecked")
	public void addFundsByAssetClass(ReportDataModel rdm, PDFDocument doc, Element root){
        
        Element fundsByAssetClass = doc.createElement("fundsByAssetClass");
        Element assetHouses = doc.createElement("assetHouses");
        
        Hashtable<String, AssetClass> assetClassTable = new Hashtable<String, AssetClass>();
        
        try{
            Iterator allAssetClasses = FundServiceDelegate.getInstance().getAllAssetClasses().iterator();
            while (allAssetClasses.hasNext()) {
                AssetClass assetClass = (AssetClass) allAssetClasses.next();
                String assetClassId = assetClass.getAssetClass();
                assetClassTable.put(assetClassId, assetClass);
            }
            Hashtable<String, List<String>> assetHouseStruct = FundServiceDelegate.getInstance().getAssetClassesByAssetHouseID();
            
            Iterator allAssetHouseIdAndNames = FundServiceDelegate.getInstance().getAssetHouseIdNameOrderedList().iterator();

        int totalCount = 0;
        while(allAssetHouseIdAndNames.hasNext()){
            String key = (String)allAssetHouseIdAndNames.next();
            
            Element assetHouse = doc.createElement("assetHouse");
            Element assetClasses = doc.createElement("assetClasses");
            
            assetHouse.setAttribute("id", key.substring(0, key.indexOf(",")));
            assetHouse.setAttribute("name", key.substring(key.indexOf(",")+1, key.length()));
           
            
            List<String> acList = assetHouseStruct.get(key);
            int fundCountPerAssetClass;
            for (Iterator<String> i = acList.iterator( ); i.hasNext( ); ) { //loop for each asset class
                fundCountPerAssetClass = 0;//rest count to zero
                String assetClassID = i.next( );       
                // GIFL funds will be ignored based on user selection
                // stable value funds will always be ignored
                if((rdm.isIncludeGIFLSelectFunds()? true : !assetClassID.equals(ASSET_CLASS_ID_GIFL)) && !assetClassID.equals(ASSET_CLASS_ID_STABLE_VALUE)){
                        
                        String assetClassDesc = null;
                        if(!assetClassID.equals(ReportDataModel.ASSET_CLASS_STYLE_ID_HYBRID)){
                            assetClassDesc = assetClassTable.get(assetClassID).getAssetClassDescription();
                        }
                        
                        AssetClassStyle assetClassStyle = rdm.getAssetClassStyleFor(assetClassID);
                        String isBenchmarkAvail =ReportDataModel.FALSE;
                        if(assetClassStyle != null){
                            isBenchmarkAvail = assetClassStyle.isBenchmarkAvailable()? ReportDataModel.TRUE:ReportDataModel.FALSE;
                        }
                        Element assetClass = doc.createElement("assetClass");
                        
                        assetClass.setAttribute("id", assetClassID);
                        assetClass.setAttribute("name", assetClassDesc);
                        assetClass.setAttribute("nameInCaps", StringUtils.upperCase(assetClassDesc));
                        assetClass.setAttribute("isBenchmarkMetricsAvailable", isBenchmarkAvail);
                        
                        boolean  atleastOneFundIsChecked= insertFundsByAssetClass(assetClassStyle , doc, assetClass, assetClassID);
                        
                        if(atleastOneFundIsChecked){//either is true
                            if(! assetClassesWithSelectedFunds.contains(assetClassID)){//if not already there (should not happen)
                                assetClassesWithSelectedFunds.add(assetClassID);
                            }
                        }

                        insertBenchmarkIndexForAssetClass(rdm, assetClassStyle , doc, assetClass, assetClassID);
                        
                        insertCategoryFootnoteForAssetClass(assetClassStyle , doc, assetClass, assetClassID, assetClassDesc);
                        
                        doc.appendElement(assetClasses, assetClass);
                    }
                totalCount = totalCount + fundCountPerAssetClass;
                }//end - for loop for asset classes
            
            doc.appendElement(assetHouse, assetClasses);
            doc.appendElement(assetHouses, assetHouse);
        }//end - while loop for asset houses
        }
        catch (Throwable e) {
            logger.debug("Error adding funds by Asset class"+ e);
            e.printStackTrace();
        }
        doc.appendElement(fundsByAssetClass, assetHouses);
        doc.appendElement(root, fundsByAssetClass);
    
    }
    
    /**
     * Creates criteriaSelections XML node, child nodes and attributes
     * @param reportDataModel
     * @param doc
     * @param root
     */
    public void addCriteriaSelections(ReportDataModel reportDataModel, PDFDocument doc, Element root){
        
        Element criteriaSelections = doc.createElement("criteriaSelections");
        Element criterions = doc.createElement("criterions");
        //Element criterion = doc.createElement("criterion");
        int[] orderedIndices = reportDataModel.getOrderedCriteriaIndices();
        LinkedHashMap<Integer, Integer> weights = reportDataModel.getCriteriaWeightings();
        int [] orderedCriteria = reportDataModel.getOrderedCriteriaIndices();
        String [] criteriaColors = reportDataModel.getColorForCriteria();
        int numberOfSelectedCriterion = 0;
        
        Element legends = doc.createElement("legends");
        Element legendRow = null;
        Element legend = null;
    
        Element criterion = null;
        
        boolean is_3_5_10_Year_Return_Selected = false;
        
        int row = 1;
        for (int i=0; i < orderedIndices.length; i++) {
        	
        	 if(i % 6 == 0) {
        		criterion = doc.createElement("criterion");
        		criterion.setAttribute("row", String.valueOf(row++));
             	doc.appendElement(criterions, criterion);
             }
        	 
        	 if(i % 2 == 0) {
        		 legendRow = doc.createElement("legendRow");
                 doc.appendElement(legends, legendRow);
             }
        	
            Element criteria = doc.createElement("criteria");
            String weight = weights.get(orderedIndices[i]) + ReportDataModel.PERCENT_SYMBOL;
            criteria.setAttribute("shortName", SelectionCriteria.shortNames[orderedCriteria[i]]);
            
            criteria.setAttribute("weight", weight);
            
            
            if(!weight.equals("0%")){
                numberOfSelectedCriterion ++;
                
				if (SelectionCriteria.TOTAL_RETURN == orderedCriteria[i]
						|| SelectionCriteria.TOTAL_RETURN_1YEAR == orderedCriteria[i]
						|| SelectionCriteria.TOTAL_RETURN_5YEAR == orderedCriteria[i]
						|| SelectionCriteria.TOTAL_RETURN_10YEAR == orderedCriteria[i]){
					is_3_5_10_Year_Return_Selected = true;
                }
            }
            
            legend = doc.createElement("legend");
   		    legend.setAttribute("shortName", SelectionCriteria.shortNames[orderedCriteria[i]]);
   		    String color = criteriaColors[i];
   		    legend.setAttribute("color", color);
   		    doc.appendElement(legendRow, legend);
            
            doc.appendElement(criterion, criteria);
           
        }
        Element numberOfSelectedCriterionElement = doc.createElement("numberOfSelectedCriterion");
        numberOfSelectedCriterionElement.setTextContent(String.valueOf(numberOfSelectedCriterion));
        
        if(is_3_5_10_Year_Return_Selected) {
        	Element criteria_Selected_1_3_5_10YR = doc.createElement("criteria_Selected_1_3_5_10YR");
            criteria_Selected_1_3_5_10YR.setTextContent("Yes");
            doc.appendElement(criteriaSelections, criteria_Selected_1_3_5_10YR);
        }
        
        doc.appendElement(criteriaSelections, criterions);
        doc.appendElement(criteriaSelections, numberOfSelectedCriterionElement);
        doc.appendElement(criteriaSelections, legends);
        
     // prepare the pie chart bean
        Element pieChartElement = doc.createElement("pieChartElement");
        
        pieChartBean = getPieChartBean(reportDataModel,weights,criteriaColors);
        
        String portNumber = System.getProperty("webcontainer.http.port") == null ? "9081" : System.getProperty("webcontainer.http.port");
        String baseURI = "http://localhost:" + portNumber;        
        
        String fileName = PieChartUtil.createURLStringFOP(pieChartBean);
        
        //TODO remove below once Piechartservlet jar is fixed
        int[] onlySelectedCriteriaIndices = reportDataModel.getOnlySelectedCriteriaIndices();
        int[] selectedCriteriaIndices = onlySelectedCriteriaIndices;
        String[] wedgeNames=getWedgeNames();
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < selectedCriteriaIndices.length; j++) {
            sb.append("&wedge"+(j+1)+"Label="+wedgeNames[selectedCriteriaIndices[j]]);
        }
        fileName = fileName +sb.toString();
        
        doc.appendTextNode(pieChartElement, BDPdfConstants.PIE_CHART_URL, baseURI
                + fileName.replaceAll(PdfConstants.AMPERSAND, CommonConstants.AMPERSAND_SYMBOL));
        logger.debug("**** piechart baseURI "+baseURI
                + fileName.replaceAll(PdfConstants.AMPERSAND, CommonConstants.AMPERSAND_SYMBOL));
        doc.appendElement(criteriaSelections, pieChartElement);
        doc.appendElement(root, criteriaSelections);
    }
    
    private String[] getWedgeNames() {
    	 final String[] wedgeNames = new String[SelectionCriteria.TOTAL];
    	 wedgeNames[SelectionCriteria.R2] = "R-squared";
    	 wedgeNames[SelectionCriteria.INFORMATION_RATIO] = "Information%20Ratio" ;
    	 wedgeNames[SelectionCriteria.FEES] = "Expense%20Ratio";
    	 wedgeNames[SelectionCriteria.TOTAL_RETURN] = "3%20Year%20Return";
    	 wedgeNames[SelectionCriteria.TOTAL_RETURN_5YEAR] = "5%20Year%20Return";
         wedgeNames[SelectionCriteria.TOTAL_RETURN_10YEAR] = "10%20Year%20Return";
         wedgeNames[SelectionCriteria.SHARPE_RATIO] = "Sharpe%20Ratio";
         wedgeNames[SelectionCriteria.ALPHA] = "Alpha";
         wedgeNames[SelectionCriteria.STANDARD_DEVIATION] = "Standard%20Deviation";
         wedgeNames[SelectionCriteria.UPSIDE_CAPTURE] = "Upside%20Capture";
         wedgeNames[SelectionCriteria.DOWNSIDE_CAPTURE] = "Downside%20Capture";
         wedgeNames[SelectionCriteria.BETA] = "Beta";
		return wedgeNames;
    }
    
    /**
     * Method sets all parameter required for Pie chart
     * @param reportDataModel
     * @param weights
     * @param criteriaColors
     * @return PieChartBean
     */
    private PieChartBean getPieChartBean(ReportDataModel reportDataModel,LinkedHashMap<Integer, Integer> weights, String[] criteriaColors) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getAssetAllocationByRiskPieChartBean");
        }
        
        PieChartBean pieChart = new PieChartBean();
        pieChart.setAppletArchive(BDConstants.PIE_CHART_APPLET_ARCHIVE);
        pieChart.setPresentationModel(PieChartBean.PRESENTATION_MODEL_SERVLET);
        pieChart.setBorderColor("#FFFFFF");
        pieChart.setShowWedgeLabels(true);
        pieChart.setUsePercentsAsWedgeLabels(true);
        pieChart.setPieStyle(PieChartBean.PIE_STYLE_FLAT);
        pieChart.setBorderWidth((float) 0);
        pieChart.setWedgeLabelOffset(70);
        pieChart.setFontSize(20);
        pieChart.setFontBold(true);
        pieChart.setDrawBorders(false);
        pieChart.setAppletWidth(390);//as per style guide
        pieChart.setAppletHeight(420);//as per style guide
        //pieChart.setPieWidth(186);
        pieChart.setPieWidth(250);
        pieChart.setHexBackgroundColor("#F0ECDA");
        pieChart.setKeyFontSize(22);//If reduced further - the Measured by Bold effect is lost
        pieChart.setKeyStyle(0);
        pieChart.setKeyFontBold(false);
        pieChart.setKeyLeftCoord(90);
        pieChart.setKeyPosition(0);
        pieChart.setKeyTopCoord(90);
        pieChart.setKeyTitleColumn1("Measured%20by:");
        pieChart.setPieLeftCoord(0);
        pieChart.setPieTopCoord(40);
        pieChart.setPieThickness(40);
        pieChart.setShowValueWithKey(false);
        pieChart.setKeyColumns(1);
        pieChart.setKeyMarkerScalar(1.5);
        pieChart.setKeySpacing(30);
        pieChart.setWedgeLabelExtrusion(120);
        pieChart.setWedgeLabelExtrusionThreshold(1);
        pieChart.setWedgeLabelExtrusionColor("#000000");
        
        
        int[] onlySelectedCriteriaIndices = reportDataModel.getOnlySelectedCriteriaIndices();
        int[] selectedCriteriaIndices = onlySelectedCriteriaIndices;
        
        for (int j = 0; j < selectedCriteriaIndices.length; j++) {
            
            String wedgeName = SelectionCriteria.shortNamesForScreen[selectedCriteriaIndices[j]];
            int weight = weights.get(selectedCriteriaIndices[j]);
            String color = criteriaColors[j];
            
            pieChart.addPieWedge(wedgeName, (float)weight, color, wedgeName, "","#FFFFFF", 0);
        }
                
        if (logger.isDebugEnabled()) {
            logger.debug("entry <- getPieChartBean");
        }
        return pieChart;
    }
    
    /**
     * Creates fundsByInvestmentCategory node in XML which categorizes fund line up funds by Investment category and fund IDs within each Investment category
     * @param rdm
     * @param doc
     * @param root
     * @param invCat
     */
    public void addFundsByInvestmentCategory(ReportDataModel rdm, PDFDocument doc, Element root, Hashtable<Integer, InvestmentGroup> invCat){
        Element fundsByInvestmentCategory = doc.createElement("fundsByInvestmentCategory");
        Element investmentCategories = doc.createElement("investmentCategories");
        
        ArrayList<Integer> invOrderList = new ArrayList<Integer>();
        Iterator<Integer> itrMap = invCat.keySet().iterator();
        while(itrMap.hasNext()){
            invOrderList.add(itrMap.next());
        }
        Collections.sort(invOrderList);
        Iterator<Integer> itrList = invOrderList.iterator();
        while(itrList.hasNext()){//loop on each Investment category
            
            Element investmentCategory = doc.createElement("investmentCategory");
            int key = itrList.next();
            investmentCategory.setAttribute("id", Integer.toString(key));
            investmentCategory.setAttribute("name", StringUtils.upperCase(invCat.get(key).getGroupname()));
            investmentCategory.setAttribute("colorCode", invCat.get(key).getColorcode());
            investmentCategory.setAttribute("fontColor", invCat.get(key).getFontcolor());
            
            InvestmentCategory ic = rdm.getInvestmentCategoryFor(key);
            Element funds = doc.createElement("funds");
            if(ic!=null){
                List<DecoratedFund> decoratedFunds = ic.getDecoratedFunds();
                
                if(decoratedFunds!=null){
                    Iterator<DecoratedFund> itr = decoratedFunds.iterator();
                    
                    while (itr.hasNext()) {//loop for each fund
                        Element fundElement = doc.createElement("fund");
                        DecoratedFund df = itr.next();
                        String fundID = df.getFund().getFundId();
                        fundElement.setTextContent(fundID);
                        doc.appendElement(funds, fundElement);
                  }
                }
            }
            doc.appendElement(investmentCategory, funds);
            doc.appendElement(investmentCategories, investmentCategory);
        }
        doc.appendElement(fundsByInvestmentCategory, investmentCategories);
        doc.appendElement(root, fundsByInvestmentCategory);
        
    }
    
    /**
     * Utility method to add benchmark category for each asset class
     * 
     * @param assetClassStyle
     * @param doc
     * @param assetClass
     * @param assetClassID
     * @param assetClassDescription
     */
    private void insertCategoryFootnoteForAssetClass(AssetClassStyle assetClassStyle, PDFDocument doc, Element assetClass, String assetClassID, String assetClassDescription){
        Element categoryFootnote = doc.createElement("categoryFootnote");
        
        if((assetClassStyle != null) && (assetClassStyle.isBenchmarkAvailable())){
            Element assetClassDesc = doc.createElement("assetClassDesc");
            
            assetClassDesc.setTextContent(assetClassDescription);
            Element benchmarkName = doc.createElement("benchmarkName");
            
            benchmarkName.setTextContent(assetClassStyle.getBenchmarkDescription());
            doc.appendElement(categoryFootnote, assetClassDesc);
            doc.appendElement(categoryFootnote, benchmarkName);
         }
        doc.appendElement(assetClass, categoryFootnote);
    }
    
    /**
     * Utility method to add benchmarkIndex Node in XML which indicates morningstar benchmark values to be displayed 
     * for Asset classes in Fund Ranking table of Pdf report
     * 
     * @param rdm
     * @param assetClassStyle
     * @param doc
     * @param assetClass
     * @param assetClassID
     */
    private void insertBenchmarkIndexForAssetClass(ReportDataModel rdm, AssetClassStyle assetClassStyle, PDFDocument doc, Element assetClass, String assetClassID){
        
        Element benchmarkIndex = doc.createElement("benchmarkIndex");
        int[] orderedPercentiles = ReportDataModel.DESCENDING_ORDERED_PERCENTILE_RANKS;
        
        int[] onlySelectedCriteriaIndices = rdm.getOnlySelectedCriteriaIndices();
        int[] selectedCriteriaIndices = onlySelectedCriteriaIndices;
        if((assetClassStyle != null) && (assetClassStyle.isBenchmarkAvailable())){
                    
        for (int j = 0; j < selectedCriteriaIndices.length; j++) {
        
                Element morningStarBenchmarkIndex = doc.createElement("morningStarBenchmarkIndex");
                
                morningStarBenchmarkIndex.setAttribute("criteriaName", SelectionCriteria.shortNamesForScreen[selectedCriteriaIndices[j]]);
                // put the benchmark based on SelectedCriteriaIndices
                String criteriaValueString = null;
                
                for (int i = 0; i < orderedPercentiles.length; i++) {//100,50,0
                    BenchmarkMetrics benchMetrics =  assetClassStyle.getBenchmarkMetrics(orderedPercentiles[i]);
                    if(benchMetrics!=null){
                        BigDecimal[] values= new BigDecimal[0];
                        switch(i){
                            
                            case 0:
                                Element percentileMax = doc.createElement("percentileMax");
                                values = benchMetrics.getValues();
                                try{
                                    if(checkFormatting(selectedCriteriaIndices,j)){
                                        criteriaValueString = ReportDataModel.formatRatio(values[selectedCriteriaIndices[j]]);
                                    }
                                    else{
                                        criteriaValueString = ReportDataModel.formatPercent(values[selectedCriteriaIndices[j]]);
                                    }
                                }
                                catch(Exception e){
                                    criteriaValueString = NON_APPLICABLE_STRING;
                                }
                                percentileMax.setAttribute("value", criteriaValueString);
                                percentileMax.setAttribute("percentile", "100%");
                                doc.appendElement(morningStarBenchmarkIndex,percentileMax);
                                break;
                                
                            case 1:
                                Element median = doc.createElement("median");
                                values = benchMetrics.getValues();
                                
                                try{
                                    if(checkFormatting(selectedCriteriaIndices,j)){
                                        criteriaValueString = ReportDataModel.formatRatio(values[selectedCriteriaIndices[j]]);
                                    }
                                    else{
                                        criteriaValueString = ReportDataModel.formatPercent(values[selectedCriteriaIndices[j]]);
                                    }
                                }
                                catch(Exception e){
                                    criteriaValueString = NON_APPLICABLE_STRING;
                                }
                                median.setAttribute("value", criteriaValueString);
                                median.setAttribute("percentile", "50%");
                                doc.appendElement(morningStarBenchmarkIndex,median);
                                break;
                                
                            case 2:
                                Element percentileMin = doc.createElement("percentileMin");
                                values = benchMetrics.getValues();
                                
                                try{
                                    if(checkFormatting(selectedCriteriaIndices,j)){
                                        criteriaValueString = ReportDataModel.formatRatio(values[selectedCriteriaIndices[j]]);
                                    }
                                    else{
                                        criteriaValueString = ReportDataModel.formatPercent(values[selectedCriteriaIndices[j]]);
                                    }
                                }
                                catch(Exception e){
                                    criteriaValueString = NON_APPLICABLE_STRING;
                                }
                                percentileMin.setAttribute("value", criteriaValueString);
                                percentileMin.setAttribute("percentile", "0%");
                                doc.appendElement(morningStarBenchmarkIndex,percentileMin);
                                break;
                        }
                    }
                }
                doc.appendElement(benchmarkIndex, morningStarBenchmarkIndex);
            }
        }
        doc.appendElement(assetClass, benchmarkIndex);
    }
    
    /**
     * Utility method to indicate whether to format metric value as Ratio or percent
     * @param selectedCriteriaIndices
     * @param j
     * @return boolean
     */
    private boolean checkFormatting(int[] selectedCriteriaIndices, int j){
        if (selectedCriteriaIndices[j] == SelectionCriteria.INFORMATION_RATIO ||
                selectedCriteriaIndices[j] == SelectionCriteria.SHARPE_RATIO) {
            return true;
        }
        else return false;
    }
    
    /**
     * Utility method to include fund IDs under an asset class in XML
     * 
     * @param assetClassStyle
     * @param doc
     * @param assetClass
     * @param assetClassID
     * @return boolean - which indicated that asset class contains at-least 1 fund which is selected (index or non-index)
     */
    private boolean insertFundsByAssetClass(AssetClassStyle assetClassStyle, PDFDocument doc, Element assetClass, String assetClassID){
        
        Element fundsElement = doc.createElement("funds");
        boolean atleastOneFundIsChecked = false;
        
        if (assetClassStyle != null && assetClassStyle.getNumberOfFunds() > 0) {
            
            List<DecoratedFund> funds = assetClassStyle.getDecoratedFundsSortedByPercentileRank();
            
            String decoratedFundAssetClass = null;
            
            for (int j = 0; j < funds.size(); j++) {//loop for each fund
                
                DecoratedFund decoratedFund = funds.get(j);
                decoratedFundAssetClass = decoratedFund.getFund().getAssetClassId();
                
                if(decoratedFundAssetClass.equals(assetClassID) || assetClassID.equals(ReportDataModel.ASSET_CLASS_STYLE_ID_HYBRID)){//if asset class is same, means it is not Index fund. Or special case is BALLLCFLSF (for which decorated fund asset class name will not match asset class id)
                    Element fund = doc.createElement("fund");
                    fund.setTextContent(decoratedFund.getFund().getFundId());
                    if(ReportDataModel.ASSET_CLASS_ID_LIFECYCLE.equals(assetClassID)){
                    	String fundFamilyCode = decoratedFund.getFund().getLifeCycleFundFamilyCode();
                    	// To identify LifeCycle Fund family suite. 
                        fund.setAttribute("fundFamilyCode", fundFamilyCode);
                        if(decoratedFund.isChecked()){
	                        if(!lifecycleFundFamiliesSelected.contains(fundFamilyCode)){
	                        	lifecycleFundFamiliesSelected.add(fundFamilyCode);
	                        }
                        }
                    }else if(ReportDataModel.ASSET_CLASS_ID_LIFESTYLE.equals(assetClassID)){
                    	String fundFamilyCode = decoratedFund.getFund().getLifeStyleFundFamilyCode();
                    	// To identify LifeStycle Fund family suite. 
                        fund.setAttribute("fundFamilyCode", fundFamilyCode);
                        if(decoratedFund.isChecked()){
	                        if(!lifeStyleFundFamiliesSelected.contains(fundFamilyCode)){
	                        	lifeStyleFundFamiliesSelected.add(fundFamilyCode);
	                        }
                        }
                    }
                    
                    doc.appendElement(fundsElement, fund);
                }
                if(atleastOneFundIsChecked == false){
                    if(decoratedFund.isChecked()){
                        atleastOneFundIsChecked = true;
                    }
                }
            }
            doc.appendElement(assetClass, fundsElement);
        }
        Element fundsBySortOrder = doc.createElement("fundsBySortOrder");
        if (assetClassStyle != null && assetClassStyle.getNumberOfFunds() > 0) {
            
            List<DecoratedFund> funds = assetClassStyle.getDecoratedFundsSortedByFundSortNumber();
            
            String decoratedFundAssetClass = null;
            
            for (int j = 0; j < funds.size(); j++) {//loop for each fund
                
                DecoratedFund decoratedFund = funds.get(j);
                decoratedFundAssetClass = decoratedFund.getFund().getAssetClassId();
                
                if(decoratedFundAssetClass.equals(assetClassID) || assetClassID.equals(ReportDataModel.ASSET_CLASS_STYLE_ID_HYBRID)){//Or special case is BALLLCFLSF (for which decorated fund asset class name will not match asset class id)
                    Element fund = doc.createElement("fund");
                    fund.setTextContent(decoratedFund.getFund().getFundId());
                    if(ReportDataModel.ASSET_CLASS_ID_LIFECYCLE.equals(assetClassID)){
	                    // To identify LifeCycle Fund family suite. 
	                    fund.setAttribute("fundFamilyCode", decoratedFund.getFund().getLifeCycleFundFamilyCode());
                    }
                    doc.appendElement(fundsBySortOrder, fund);
                }
            }
            doc.appendElement(assetClass, fundsBySortOrder);
        }
        return atleastOneFundIsChecked;
    
    }
    
    private String splitLongFundName(String sentence) {
        String firstHalf = "";
        String secondHalf = "";
        String returnString = sentence;
        int numberOfWords = 0;
        int wordAtMaxLength = 0;
        try {
            if (sentence != null && sentence.length() > MAX_STRING_LENGTH) {
                // Tokenize the entire sentence
                StringTokenizer st = new StringTokenizer(sentence, " ");

                // Count the number of words in the sentence.
                numberOfWords = st.countTokens();

                // Figure out the word located at the split point of
                // MAX_STRING_LENGTH characters.
                StringTokenizer firstHalfTokens = new StringTokenizer(new String(
                        sentence.substring(0, MAX_STRING_LENGTH)));
                wordAtMaxLength = firstHalfTokens.countTokens();

                // String reconstruction loop
                for (int i = 1; i < numberOfWords + 1; i++) {
                    if ((i < wordAtMaxLength)
                            && (firstHalf.length() < (MAX_STRING_LENGTH - ONE_THIRD_MAX_LENGTH)) && (wordAtMaxLength - i)>=2) {
                        // if the word is in the first half of the sentence
                        // AND
                        // the reconstructed first half of the sentence, is still
                        // less than 2/3rds of the MAX STRING LENGTH
                        // Add the word to the first half of the reconstructed
                        // sentence
                        // AND
                        // At-least 2 words remain to be placed in the  re-constructed 2nd half (business didn't want only 1 word to appear in 2nd line)
                        firstHalf = firstHalf.concat(st.nextToken().concat(" "));
                    } else {
                        // else add the word to the second half of the reconstructed
                        // sentence.
                        secondHalf = secondHalf.concat(st.nextToken().concat(" "));
                        // Note: adds an extra space
                    }
                }
                // Knock off the last character which is always an extra space.
                firstHalf = firstHalf.substring(0, firstHalf.length() - 1);

                // Return the reconstructed first and second halfs, with a break
                // point in between.
                returnString = firstHalf.concat("|".concat(secondHalf));
            }
        } catch (Exception e) {
            // Eat the exception and just return the orignial sentence if there are any problems.
        }
        return returnString;
    }
    
    /**
     * Provides boolean value to display static content related to  Performance Disclaimer in the “Performance & expenses” section 
     * based on AlignmentEffectiveDate and Fund Sheet Update date
     * @param doc
     * @param root
     * @throws SystemException
     * @throws ParseException
     */
	public void addDynamicPerformanceDisclaimer(PDFDocument doc, Element root, String contractId, String rateType)
			throws SystemException {
		
		//Retrieving the alignment implementation date from properties file
		String alignmentEffectiveDateString  = EnvironmentServiceDelegate.getInstance()
				.getBusinessParam(CoreToolConstants.ALIGNMENT_EFFECTIVE_DATE);
		String fundSheetUpdateDateString  = EnvironmentServiceDelegate.getInstance()
				.getBusinessParam(CoreToolConstants.FUND_SHEET_UPDATE_DATE);
		SimpleDateFormat dateFormat = new SimpleDateFormat(RenderConstants.MEDIUM_YMD_DASHED);
    	
		
		Date alignmentEffectiveDate = null;
		Date fundSheetUpdateDate = null;
		try {
			alignmentEffectiveDate = dateFormat.parse(alignmentEffectiveDateString);
			fundSheetUpdateDate = dateFormat.parse(fundSheetUpdateDateString);
		} catch (ParseException e) {
			throw new SystemException(e,"Exception occured while parsing alignment effective and fund sheet updated date ");
		}

		Element content = doc.createElement("dynamicPerformanceDisclaimer");
		Element isContentIncluded = doc.createElement("isContentIncluded");
		Element AlignmentEffectiveDate = doc.createElement("alignmentEffectiveDateElement");
		Element fwiDisclosureText = doc.createElement("fwiDisclosureTextElement");
		fwiDisclosureText.setTextContent(StringEscapeUtils.unescapeHtml(ContentHelper.getContentText(
				BDContentConstants.FEE_WAIVER_DISCLOSURE_TEXT,
				ContentTypeManager.instance().DISCLAIMER, null))); 

		Element jhiDisclosureText = doc.createElement("jhiDisclosureTextElement");
		jhiDisclosureText.setTextContent(ContentHelper.getContentText(CommonContentConstants.FAP_CONTRACT_VIEW_DISCLOSURE, ContentTypeManager.instance().DISCLAIMER, null));
				
		Date currentDate = Calendar.getInstance().getTime();
		
		// CR10: Between Implementation Date and the Q2 Fund Sheet Update date
		// append alignment specific disclaimer to the existing Performance & expenses
		// disclaimer
		if (alignmentEffectiveDate != null && fundSheetUpdateDate != null
				&& (DateUtils.isSameDay(currentDate, alignmentEffectiveDate) || (currentDate
						.after(alignmentEffectiveDate) && currentDate
						.before(fundSheetUpdateDate)))) {
			isContentIncluded.setAttribute("isIncluded", "yes");
			dateFormat = new SimpleDateFormat(RenderConstants.LONG_MDY);
			AlignmentEffectiveDate.setTextContent( dateFormat.format(alignmentEffectiveDate));
		} else {
			isContentIncluded.setAttribute("isIncluded", "no");
		}
		doc.appendElement(content, isContentIncluded);
		doc.appendElement(content, AlignmentEffectiveDate);
		doc.appendElement(content, fwiDisclosureText);
		doc.appendElement(content, jhiDisclosureText);
		doc.appendElement(root, content);

	}
    
    /**
     * Adds fundDetails node in XML which has all required fund related details, metrics, footnotes etc
     * @param rdm
     * @param doc
     * @param root
     * @param invCat
     * @param investmentIdsPlusRateType
     * @param contractId
     * @param rateType
     * @throws SystemException
     */
    public void addCommonFundDetails(ReportDataModel rdm, PDFDocument doc, Element root, Hashtable<Integer, InvestmentGroup> invCat, List<String> investmentIdsPlusRateType, String contractId, String rateType, String avgExpRatioMethod) throws SystemException{
      //for common - we are using Inv catogory to loop (less Inv cat compared  to Asset Classes)
        Element fundDetails = doc.createElement("fundDetails");
        String aaFundFactor  = EnvironmentServiceDelegate.getInstance()
				.getBusinessParam(BusinessParamConstants.AA_FUND_FACTOR);
        Element result;
        Enumeration<Integer> e1 = invCat.keys();
        
        List<String> moneyMarketFundMap = CoreToolGlobalData.defaultSelectedMoneyMarketFunds;
        List<String> stableValueFundMap = CoreToolGlobalData.defaultSelectedStableValueFunds;
        List<String> feeWaiverIndicatorFunds = FundServiceDelegate.getInstance().getFundFeeWaiverIndicator();
        List<SvgifFund> svgifDefaultFund = FundServiceDelegate.getInstance().getSVGIFDefaultFunds();
        
        List<String> svgiNonDefaultFunds = FundServiceDelegate.getInstance().getSVGIFFunds();
        List<String> svgiFunds = FundServiceDelegate.getInstance().getAllSVGIFunds();
        
        GuaranteedFundInterestRates guaranteedFundInterestRates = null;
        if( !StringUtils.equals(contractId, null) && StringUtils.isNotBlank(contractId)){
            guaranteedFundInterestRates = FundServiceDelegate.getInstance().getGuaranteedFundInterestRatesUsingContract(contractId);
        }
        else{
            guaranteedFundInterestRates = FundServiceDelegate.getInstance().getGuaranteedFundInterestRatesUsingRateType(rateType);
        }
        
        BigDecimal sumOfExpenseRatio = new BigDecimal(0);
        BigDecimal sumOfAssetAllocatedExpenseRatio = new BigDecimal(0);
        BigDecimal sumOfNonAssetAllocatedExpenseRatio = new BigDecimal(0);
        int selectedFundCount = 0;
        int aaSelectedFundCount = 0;
        int nonAASelectedFundCount = 0;
        
        while(e1.hasMoreElements()){  //loop on each Inv cat
            int key = (Integer)e1.nextElement();
            
            InvestmentCategory ic = rdm.getInvestmentCategoryFor(key);
            
            if(ic!=null){
                List<DecoratedFund> decoratedFunds = ic.getDecoratedFunds();
                
                if(decoratedFunds!=null){
                    Iterator<DecoratedFund> itr = decoratedFunds.iterator();
                    
                    while (itr.hasNext()) {//loop for each fund
                        
                        DecoratedFund decoratedFund = itr.next();

                        String fundID = decoratedFund.getFund().getFundId();
                        boolean isSVGIFFund = svgifDefaultFund.stream().filter(o -> o.getFundId().equals(fundID)).findFirst().isPresent();
                        
                        if((null==contractId || contractId.trim().length()< 1) && null!=rateType) {
                        	if((rateType.equalsIgnoreCase(BDConstants.CLASS_ZERO) || rateType.equalsIgnoreCase(BDConstants.SIGNATURE_PLUS)) &&
                    		svgiFunds.contains(fundID) && svgiNonDefaultFunds.contains(fundID)){
                    			continue;
                    		}else if(!(rateType.equalsIgnoreCase(BDConstants.CLASS_ZERO) || rateType.equalsIgnoreCase(BDConstants.SIGNATURE_PLUS)) &&
                            		svgiFunds.contains(fundID)){
                    			continue;
                    		}
                        }
                        		
                        //added new
                        Element fund = doc.createElement("fund");
						if (feeWaiverIndicatorFunds.contains(fundID)) {
							fund.setAttribute("isFeeWaiverIndicator", "yes");
						} else {
							fund.setAttribute("isFeeWaiverIndicator", "no");
						}
                        fund.setAttribute("id", decoratedFund.getFund().getFundId());
                        fund.setAttribute("name", decoratedFund.getFund().getFundName());
                        String longNameWithInsertedSeperatorIfNeeded = splitLongFundName(decoratedFund.getFund().getFundLongName());
                        fund.setAttribute("longName", longNameWithInsertedSeperatorIfNeeded);
                        fund.setAttribute("isChecked", (decoratedFund.isChecked()?"yes" : "no"));
                        fund.setAttribute("isContractFund", (decoratedFund.isContractSelected()?"yes" : "no"));
                        fund.setAttribute("isToolSelected", (decoratedFund.isToolSelected()?"yes" : "no"));
                        fund.setAttribute("isBrokerSelected", (decoratedFund.isBrokerSelected()?"yes" : "no"));
                        fund.setAttribute("assetManager", (decoratedFund.getFundManagerName()));
                        fund.setAttribute("isClosedToNB", (decoratedFund.isClosedToNB()?"yes":"no"));//added new
                        fund.setAttribute("isIndex", (decoratedFund.isIndex()?"yes":"no"));//added new
                        fund.setAttribute("fundMorningstarCategory", (decoratedFund.getFund().getFundCategoryName()));
                        
                        String oneYearNumberofFundsInCategory = decoratedFund.getFund().getOneYearNumberofFundsInCategory();
                        String threeYearNumberofFundsInCategory = decoratedFund.getFund().getThreeYearNumberofFundsInCategory();
                        String fiveYearNumberofFundsInCategory = decoratedFund.getFund().getFiveYearNumberofFundsInCategory();
                        String tenYearNumberofFundsInCategory = decoratedFund.getFund().getTenYearNumberofFundsInCategory();
                        
                        oneYearNumberofFundsInCategory = (StringUtils
								.isNotEmpty(oneYearNumberofFundsInCategory) && Integer
								.parseInt(oneYearNumberofFundsInCategory) != 0) ? formatFundsInCategory(oneYearNumberofFundsInCategory)
								: NON_APPLICABLE_STRING;
						threeYearNumberofFundsInCategory = (StringUtils
								.isNotEmpty(threeYearNumberofFundsInCategory) && Integer
								.parseInt(threeYearNumberofFundsInCategory) != 0) ? formatFundsInCategory(threeYearNumberofFundsInCategory)
								: NON_APPLICABLE_STRING;
						fiveYearNumberofFundsInCategory = (StringUtils
								.isNotEmpty(fiveYearNumberofFundsInCategory) && Integer
								.parseInt(fiveYearNumberofFundsInCategory) != 0) ? formatFundsInCategory(fiveYearNumberofFundsInCategory)
								: NON_APPLICABLE_STRING;
						tenYearNumberofFundsInCategory = (StringUtils
								.isNotEmpty(tenYearNumberofFundsInCategory) && Integer
								.parseInt(tenYearNumberofFundsInCategory) != 0) ? formatFundsInCategory(tenYearNumberofFundsInCategory)
								: NON_APPLICABLE_STRING;

                        fund.setAttribute("oneYearNumberofFundsInCategory", oneYearNumberofFundsInCategory);
                        fund.setAttribute("threeYearNumberofFundsInCategory", threeYearNumberofFundsInCategory);
                        fund.setAttribute("fiveYearNumberofFundsInCategory", fiveYearNumberofFundsInCategory);
                        fund.setAttribute("tenYearNumberofFundsInCategory", tenYearNumberofFundsInCategory);
                        
                        fund.setAttribute("fundCode", (decoratedFund.getFund().getFundCode()));
                        if(decoratedFund.getFund().getInceptionDate()!= null){//Guaranteed funds dont seem to have a introduced date
                            fund.setAttribute("fundDateIntroduced", ReportDataModel.formatDateShortUSForm((decoratedFund.getFund().getInceptionDate())));
                        }
                        //fundMetrics - start
                        //rankings - start
                        Element fundMetricsElement = doc.createElement("fundMetrics");
                        Element rankings = doc.createElement("rankings");
                        FundMetrics fundMetrics = decoratedFund.getFundMetrics();
                        
                        //set One year, 3 year, 5 year, 10 year ranking here.
                        if (fundMetrics != null){
                        	if(fundMetrics.getPercentileRankings()[SelectionCriteria.TOTAL_RETURN_1YEAR] != null){
		                        fund.setAttribute("oneYearFundRank", convertIndivPercentileToRankForDisplay(fundMetrics.getPercentileRankings()[SelectionCriteria.TOTAL_RETURN_1YEAR]));
                        	}
                        	if(fundMetrics.getPercentileRankings()[SelectionCriteria.TOTAL_RETURN] != null){
		                        fund.setAttribute("threeYearFundRank", convertIndivPercentileToRankForDisplay(fundMetrics.getPercentileRankings()[SelectionCriteria.TOTAL_RETURN]));
                        	}
                        	if(fundMetrics.getPercentileRankings()[SelectionCriteria.TOTAL_RETURN_5YEAR] != null){
		                        fund.setAttribute("fiveYearFundRank", convertIndivPercentileToRankForDisplay(fundMetrics.getPercentileRankings()[SelectionCriteria.TOTAL_RETURN_5YEAR]));
                        	}
                        	if(fundMetrics.getPercentileRankings()[SelectionCriteria.TOTAL_RETURN_10YEAR] != null){
		                        fund.setAttribute("tenYearFundRank", convertIndivPercentileToRankForDisplay(fundMetrics.getPercentileRankings()[SelectionCriteria.TOTAL_RETURN_10YEAR]));
                        	}
                        }
                        
                        for(Entry<Integer, Integer> entry : rdm.getCriteriaWeightings().entrySet()) {
                        	
                        	if(entry.getValue() == 0) {
                        		continue;
                        	}
                        	
                            Element criteria = doc.createElement("criteria");
                            
                            criteria.setAttribute("shortname", SelectionCriteria.shortNamesForScreen[entry.getKey()]);
                            result = doc.createElement("result");
                            
                            if (fundMetrics != null && fundMetrics.getPercentileRankings()[entry.getKey()] != null
                                    && (decoratedFund.getPercentileRankedFund().isBenchmarkMetricsAvailable()
                                        || entry.getKey() == SelectionCriteria.FEES && fundMetrics.getValues()[entry.getKey()] != null)) {                            
                                
                                result.setAttribute("measure", formatMeasureValueToPercentIfApplicable(ReportDataModel.formatRatio(fundMetrics.getValues()[entry.getKey()]),SelectionCriteria.shortNamesForScreen[entry.getKey()] ));
                                result.setAttribute("rank",convertIndivPercentileToRankForDisplay(fundMetrics.getPercentileRankings()[entry.getKey()]));
                            }   else { 
                                result.setAttribute("rank",NON_APPLICABLE_STRING);
                                result.setAttribute("measure",NON_APPLICABLE_STRING);

                            }
                            doc.appendElement(criteria, result);
                            doc.appendElement(rankings,criteria);
                        	
                        }
                        
                        
                        Element overall = doc.createElement("overall");
                        overall.setAttribute("ranking",decoratedFund.getPercentileRankedFund().isBenchmarkMetricsAvailable()
                                ? String.valueOf(decoratedFund.getPercentileRankedFund().getRank())
                                        : NON_APPLICABLE_STRING);
                        
                        overall.setAttribute("displayRanking",decoratedFund.getPercentileRankedFund().isBenchmarkMetricsAvailable()
                                ? String.valueOf(decoratedFund.getPercentileRankedFund().getDisplayRank())
                                        : NON_APPLICABLE_STRING);
                        
                        doc.appendElement(fundMetricsElement,rankings);
                        doc.appendElement(fundMetricsElement,overall);
                        
                        //rankings - end
                        
                        //ror - start
                        Element ror = doc.createElement("ror");
                        Element asOfDate = doc.createElement("asOfDate");
                        Element ror1month = doc.createElement("ror1month");
                        Element ror3month = doc.createElement("ror3month");
                        Element rorYtd = doc.createElement("rorYtd");
                        
                        if (fundMetrics != null) {
                            HypotheticalInfo hypotheticalInfo = fundMetrics.getHypotheticalInfo();
                            
                            BigDecimal ror1monthValue = fundMetrics.getRorMonthEnds()[SelectionCriteria.ROR_1_MONTH];
                            BigDecimal ror3monthValue = fundMetrics.getRorMonthEnds()[SelectionCriteria.ROR_3_MONTH];
                            BigDecimal rorYtdValue = fundMetrics.getRorMonthEnds()[SelectionCriteria.ROR_YTD_MONTH];
                            
                            if(ror1monthValue != null && hypotheticalInfo.isRor1mthHypothetical()){
                                ror1month.setAttribute("isHypothetical", ReportDataModel.TRUE);
                            }
                            if(ror3monthValue != null && hypotheticalInfo.isRor3mthHypothetical()){
                                ror3month.setAttribute("isHypothetical", ReportDataModel.TRUE);
                            }
                            if(rorYtdValue != null && hypotheticalInfo.isRorYtdHypothetical()){
                                rorYtd.setAttribute("isHypothetical", ReportDataModel.TRUE);
                            }
                            
                            asOfDate.setTextContent(ReportDataModel.formatDateShortUSForm(GlobalData.asOfDateMap.get(ReportDataModel.ASOFDATE_FUNDMETRICS_KEY)));
                            
                            if(isSVGIFFund) {
                            	ror1month.setTextContent(ReportDataModel.NON_APPLICABLE_STRING);
                                ror3month.setTextContent(ReportDataModel.NON_APPLICABLE_STRING);
                                rorYtd.setTextContent(ReportDataModel.NON_APPLICABLE_STRING);
                            }else { 
                            ror1month.setTextContent(ReportDataModel.formatRORValue(ror1monthValue));
                            ror3month.setTextContent(ReportDataModel.formatRORValue(ror3monthValue));
                            rorYtd.setTextContent(ReportDataModel.formatRORValue(rorYtdValue));
                        }
                        }
                        doc.appendElement(ror,ror1month);
                        doc.appendElement(ror,ror3month);
                        doc.appendElement(ror,rorYtd);
                        
                        doc.appendElement(fundMetricsElement,ror);
                        //ror - end
                        
//                      rorQe - start
                        Element rorQe = doc.createElement("rorQe");
                        Element ror1yrQe = doc.createElement("ror1yrQe");
                        Element ror3yrQe = doc.createElement("ror3yrQe");
                        Element ror5yrQe = doc.createElement("ror5yrQe");
                        Element ror10yrQe = doc.createElement("ror10yrQe");
                        Element rorSinceInceptionQe = doc.createElement("rorSinceInceptionQe");
                        
                        if (fundMetrics != null) {
                            HypotheticalInfo hypotheticalInfo = fundMetrics.getHypotheticalInfo();
                            
                            BigDecimal ror1yrQeValue = fundMetrics.getRorQuarterEnds()[SelectionCriteria.ROR_1_YEAR];
                            BigDecimal ror3yrQeValue = fundMetrics.getRorQuarterEnds()[SelectionCriteria.ROR_3_YEAR];
                            BigDecimal ror5yrQeValue = fundMetrics.getRorQuarterEnds()[SelectionCriteria.ROR_5_YEAR];
                            BigDecimal ror10yrQeValue = fundMetrics.getRorQuarterEnds()[SelectionCriteria.ROR_10_YEAR];
                            BigDecimal rorSinceInceptionQeValue = fundMetrics.getRorQuarterEnds()[SelectionCriteria.ROR_SINCE_INCEPTION_YEAR];
                            
                            if(hypotheticalInfo!=null)
                            {
                            	if(ror1yrQeValue != null && hypotheticalInfo.isRor1yrQeHypothetical()){
                                    ror1yrQe.setAttribute("isHypothetical", ReportDataModel.TRUE);
                                }
                                if(ror3yrQeValue != null && hypotheticalInfo.isRor3yrQeHypothetical()){
                                    ror3yrQe.setAttribute("isHypothetical", ReportDataModel.TRUE);
                                }
                                if(ror5yrQeValue != null && hypotheticalInfo.isRor5yrQeHypothetical()){
                                    ror5yrQe.setAttribute("isHypothetical", ReportDataModel.TRUE);
                                }
                                if(ror10yrQeValue != null && hypotheticalInfo.isRor10yrQeHypothetical()){
                                    ror10yrQe.setAttribute("isHypothetical", ReportDataModel.TRUE);
                                }
                                if(rorSinceInceptionQeValue != null && hypotheticalInfo.isRorSinceInceptionQeHypothetical()){
                                	rorSinceInceptionQe.setAttribute("isHypothetical", ReportDataModel.TRUE);
                                }
                            }
                            
                            ror1yrQe.setTextContent(ReportDataModel.formatRORValue(ror1yrQeValue));
                            ror3yrQe.setTextContent(ReportDataModel.formatRORValue(ror3yrQeValue));
                            ror5yrQe.setTextContent(ReportDataModel.formatRORValue(ror5yrQeValue));
                            ror10yrQe.setTextContent(ReportDataModel.formatRORValue(ror10yrQeValue));
                            
                            rorSinceInceptionQe.setTextContent(ReportDataModel.formatRORValue(rorSinceInceptionQeValue));
                        }
                        
                        doc.appendElement(rorQe,ror1yrQe);
                        doc.appendElement(rorQe,ror3yrQe);
                        doc.appendElement(rorQe,ror5yrQe);
                        doc.appendElement(rorQe,ror10yrQe);
                        doc.appendElement(rorQe,rorSinceInceptionQe);
                        
                        doc.appendElement(fundMetricsElement,rorQe);
//                      rorQe - end
                        
                        //exp ratio start
                        
                        Element aicRatio = doc.createElement("aicRatio");
                        String aicValue = ReportDataModel.NON_APPLICABLE_STRING;
                        BigDecimal aic = new BigDecimal(0);

                        if (fundMetrics != null) {
                        	if(!isSVGIFFund) {
                        		aic = fundMetrics.getAicExpenseRatio();
                        	}else {
                        		aic = null;
                        	}
                            
                            if (aic != null) {
                                aicValue = ReportDataModel.formatPercent(aic);
                                aicRatio.setTextContent(aicValue);
                            }else {
                            	if(!isSVGIFFund) {
                            		aicValue = "";
                            	}
                            	aicRatio.setTextContent(aicValue);
                            }
                        } else {
                        	if(!isSVGIFFund) {
                        		aicValue = "";
                        	}
                            aicRatio.setTextContent(aicValue);
                        }
                        
                        Element expRatioQe = doc.createElement("expRatioQe");
                        
                        if (fundMetrics != null) {
                        	String assetClassId = decoratedFund.getFund().getAssetClassId();
                        	/*BEVP. 273
                        	Asset Allocated Funds: All Funds that are:
                        	a)	Selected 
                        	b)	Belonging to Lifecycle and Lifestyle Portfolio Suites
                        	Non Asset Allocated Funds: All Funds that are:
                        	c)	Selected
                        	d)	Not belonging to Lifecycle and Lifestyle Portfolio Suites
                        	e)	Not Guaranteed Interest Accounts */ 

                        	if (decoratedFund.isChecked()
                        			&&(ReportDataModel.ASSET_CLASS_ID_LIFESTYLE.equals(assetClassId)
                        			||ReportDataModel.ASSET_CLASS_ID_LIFECYCLE.equals(assetClassId))){
                        		
                        		sumOfAssetAllocatedExpenseRatio = sumOfAssetAllocatedExpenseRatio
                        					.add(aic != null? aic : BigDecimal.valueOf(0));
                        		aaSelectedFundCount++;
                        	}else if(decoratedFund.isChecked()
                        			&& !ReportDataModel.ASSET_CLASS_ID_LIFESTYLE.equals(assetClassId)
                        			&& !ReportDataModel.ASSET_CLASS_ID_LIFECYCLE.equals(assetClassId)
                        			&& !decoratedFund.getFund().isGuaranteedAccount()){
                        		sumOfNonAssetAllocatedExpenseRatio = sumOfNonAssetAllocatedExpenseRatio
        								.add(aic != null? aic : BigDecimal.valueOf(0));
                        		nonAASelectedFundCount++;
                        	}
                        	
                        	 //CL132125 change
                        	 if (decoratedFund.isChecked()
									&& !decoratedFund.getFund().isGuaranteedAccount()) {
								 // To sum up the Expense ratio of all selected funds.
								sumOfExpenseRatio = sumOfExpenseRatio
										.add(aic != null? aic : BigDecimal.valueOf(0));
								selectedFundCount++;
							}
                        	
                        }
                        doc.appendElement(expRatioQe,aicRatio);
                        
                        doc.appendElement(fundMetricsElement,expRatioQe);
                        
//                      exp ratio end
                        
                        doc.appendElement(fund,fundMetricsElement);
                        
//                      fundMetrics - end
                        
                        // footnotes -start
                        Element footnotes = doc.createElement("footnotes");
                        ArrayList<String> footnoteSymbolList = decoratedFund.getFundFootnoteSymbols();
                        if(footnoteSymbolList!=null && footnoteSymbolList.size()>0){
                            for(String footnoteSymbol : footnoteSymbolList){
                                Element footnoteSymbolElement = doc.createElement("footnote-symbol");
                                footnoteSymbolElement.setTextContent(footnoteSymbol);
                                doc.appendElement(footnotes,footnoteSymbolElement);
                            }
                        }
                        doc.appendElement(fund,footnotes);
                        // footnotes - end
                        
                        if(moneyMarketFundMap.contains(fundID)){
                            Element moneyMarket7DayYield = doc.createElement("moneyMarket7DayYield");
                            
                            Iterator<String> itrList = investmentIdsPlusRateType.iterator();
                            String fundIdRateType = null;
                            while(itrList.hasNext()){
                                String listItem = itrList.next();
                                if(listItem.startsWith(fundID)){
                                    fundIdRateType = listItem;
                                    break;
                                }
                            }
                            if(fundIdRateType != null){
                                FundStandardDeviationAndUnitValues fundStandardDeviationAndUnitValues= FundServiceDelegate.getInstance().getStandardDeviationAndUnitValues(fundID, fundIdRateType.substring(3,6));
                                if(fundStandardDeviationAndUnitValues != null){
									String disclosureCode = fundStandardDeviationAndUnitValues.getFundDisclosureCode();
                                
									if(ReportDataModel.SEVEN_DAY_YIELD_DISCLOSURE.equalsIgnoreCase(disclosureCode)){
										String disclosureText = fundStandardDeviationAndUnitValues.getFundDisclosureText();
										moneyMarket7DayYield.setTextContent(disclosureText);
										doc.appendElement(fund,moneyMarket7DayYield);
									}
                                }
                            }
                        }
                        if(isSVGIFFund) {
                        	Element svgifFundDisclosure = doc.createElement("stableValueFundsCreditRating");
                        	
                                FundStandardDeviationAndUnitValues fundStandardDeviationAndUnitValues= FundServiceDelegate.getInstance().getStandardDeviationAndUnitValues(fundID,BDConstants.CLASS_ZERO);
                                if(fundStandardDeviationAndUnitValues != null){
										String disclosureText = fundStandardDeviationAndUnitValues.getFundDisclosureText();
										svgifFundDisclosure.setTextContent(disclosureText);
										doc.appendElement(fund,svgifFundDisclosure);							
                                }
                            
                        }
                        //commenting as Business requirement to show SVF Forward looking credit disclosure no longer exists
                        /*if(stableValueFundMap.containsKey(fundID)){
                            Element stableValueFwdLookingCreditRate = doc.createElement("stableValueFwdLookingCreditRate");
                            
                            Iterator<String> itrList = investmentIdsPlusRateType.iterator();
                            String fundIdRateType = null;
                            while(itrList.hasNext()){
                                String listItem = itrList.next();
                                if(listItem.startsWith(fundID)){
                                    fundIdRateType = listItem;
                                    break;
                                }
                            }
                            if(fundIdRateType != null){
                                FundStandardDeviationAndUnitValues fundStandardDeviationAndUnitValues= FundServiceDelegate.getInstance().getStandardDeviationAndUnitValues(fundID, fundIdRateType.substring(3,6));
                                String disclosureCode = fundStandardDeviationAndUnitValues.getFundDisclosureCode();
                                
                                if(disclosureCode.equalsIgnoreCase(ReportDataModel.FCR_DICLOSURE)){
                                    String disclosureText = fundStandardDeviationAndUnitValues.getFundDisclosureText();
                                    stableValueFwdLookingCreditRate.setTextContent(disclosureText);
                                    doc.appendElement(fund,stableValueFwdLookingCreditRate);
                                }
                            }
                        }*/
                        
                        doc.appendElement(fundDetails,fund);
                  }
                }
            }
        }
        
        // To display Average Expense ratio of all selected funds.
        Element avgExpenseRatioElement = doc.createElement("avgExpenseRatio");
        BigDecimal averageExpenseRatio =  BigDecimal.ZERO;
      /*BEVP. 273
        Expense Ratio Asset Allocated Funds Factor:  
        Expense Ratio Asset Allocated Funds Factor = Value defined by business [Value = 0.80] */
        BigDecimal aaFundFactorValue =  new BigDecimal(aaFundFactor);
        
      /*BEVP. 273
        Expense Ratio Non Asset Allocated Funds Factor:
        Expense Ratio Non Asset Allocated Funds Factor = 1 - Expense Ratio Asset Allocated Funds Factor */
        BigDecimal nonAAFundFactorValue = BigDecimal.ONE.subtract(aaFundFactorValue);
        /*1.	When the number of Asset Allocated funds is greater than zero 
         		and Non Asset Allocated funds is greater than zero then CALCULATE weighted average of the expense ratio using formula below, rounded to 2 decimals
          2.	When the Number of Asset Allocated funds = 0 then CALCULATE Simple average of the expense ratio using formula below, rounded to 2 decimals
          3.	When the Number of Non Asset Allocated funds = 0 then CALCULATE Simple average of the expense ratio using formula below, rounded to 2 decimals */

        if(aaSelectedFundCount > 0 && nonAASelectedFundCount > 0){
       /* Formula: [(Total Expense Ratios of Asset Allocated Funds/ Number of Asset Allocated Funds) 
        Expense Ratio Asset Allocated Funds Factor] + [(Total Expense Ratios of Non Asset Allocated Funds/ Number of Non Asset Allocated Funds) 
        Expense Ratio Non Asset Allocated Funds Factor]*/
        	averageExpenseRatio = ((sumOfAssetAllocatedExpenseRatio.divide(new BigDecimal(aaSelectedFundCount),2, RoundingMode.HALF_UP))
        							.multiply(aaFundFactorValue)).add((sumOfNonAssetAllocatedExpenseRatio.divide(new BigDecimal(nonAASelectedFundCount),2, RoundingMode.HALF_UP))
                							.multiply(nonAAFundFactorValue));
        }else if(aaSelectedFundCount == 0 && nonAASelectedFundCount > 0){
        	//Formula: [Total Expense Ratios of Non Asset Allocated Funds/ Number of Non Asset Allocated Funds]
        	averageExpenseRatio = sumOfNonAssetAllocatedExpenseRatio.divide(new BigDecimal(nonAASelectedFundCount),2, RoundingMode.HALF_UP);
        }else if(aaSelectedFundCount> 0 && nonAASelectedFundCount == 0){
        	//Formula: [Total Expense Ratios of Asset Allocated Funds/ Number of Asset Allocated Funds]
        	averageExpenseRatio = sumOfAssetAllocatedExpenseRatio.divide(new BigDecimal(aaSelectedFundCount),2, RoundingMode.HALF_UP);
        }
       
        if(avgExpRatioMethod.equals(FundEvaluatorConstants.WEIGHTED_AVERAGE) || avgExpRatioMethod.equals(FundEvaluatorConstants.SIMPLE_AND_WEIGHTED_AVERAGES)){
        avgExpenseRatioElement.setTextContent(ReportDataModel
				.formatPercent(averageExpenseRatio));
		    doc.appendElement(fundDetails,avgExpenseRatioElement);
        }
        Element simpleAvgExpRatioElement = doc.createElement("simpleAvgExpRatio");
        if(selectedFundCount != 0){
        	simpleAvgExpRatioElement.setTextContent(ReportDataModel
					.formatPercent(sumOfExpenseRatio.divide(BigDecimal
							.valueOf(selectedFundCount), 2, RoundingMode.HALF_UP)));
        }else{
        	simpleAvgExpRatioElement.setTextContent("0.00%");
        }
        
        if(avgExpRatioMethod.equals(FundEvaluatorConstants.SIMPLE_AND_WEIGHTED_AVERAGES) || avgExpRatioMethod.equals(FundEvaluatorConstants.SIMPLE_AVERAGE) ){
        doc.appendElement(fundDetails,simpleAvgExpRatioElement);
        }
        
        //Below code is added to fetch Previous & Current month Interest rates for guaranteed accounts.
        if(guaranteedFundInterestRates.getPreviousEffectiveDate() != null && guaranteedFundInterestRates.getCurrentEffectiveDate()!=null){//guaranteed fund interest rates can be null for Class 2,3,8 & 9
            InvestmentCategory ic = rdm.getInvestmentCategoryFor(INVESTMENT_CATEGORY_ID_CONSERVATIVE);
            
            if(ic!=null){
                List<DecoratedFund> decoratedFunds = ic.getDecoratedFunds();
                
                if(decoratedFunds!=null){
                    Iterator<DecoratedFund> itr = decoratedFunds.iterator();
                    
                    Element guaranteedFundInterestRatesElement = doc.createElement("guaranteedFundInterestRates");
                    
                    while (itr.hasNext()) {//loop for each fund
                        
                        DecoratedFund decoratedFund = itr.next();
                        if(StringUtils.equalsIgnoreCase((decoratedFund.getFund().getFundType()),ReportDataModel.GUARANTEED)){
                         // footnotes -start
                            Element footnotes = doc.createElement("footnotes");
                            ArrayList<String> footnoteSymbolList = decoratedFund.getFundFootnoteSymbols();
                            if(footnoteSymbolList!=null && footnoteSymbolList.size()>0){
                                for(String footnoteSymbol : footnoteSymbolList){
                                    Element footnoteSymbolElement = doc.createElement("footnote-symbol");
                                    footnoteSymbolElement.setTextContent(footnoteSymbol);
                                    doc.appendElement(footnotes,footnoteSymbolElement);
                                    doc.appendElement(guaranteedFundInterestRatesElement,footnotes);
                                }
                            }
                            break;//need to take footnotes only once - as in report - needs to be reported generically, not for each fund
                        }
                    }
                    
                    //previous month
                    Element previous = doc.createElement("previous");
                    previous.setAttribute("month", ReportDataModel.formatDateLongFormMonthOnly(guaranteedFundInterestRates.getPreviousEffectiveDate()));
                    
                    previous.setAttribute("threeYear", ReportDataModel.formatPercent(guaranteedFundInterestRates.getPrevious3YrInterestRate()));
                    previous.setAttribute("fiveYear", ReportDataModel.formatPercent(guaranteedFundInterestRates.getPrevious5YrInterestRate()));
                    previous.setAttribute("tenYear", ReportDataModel.formatPercent(guaranteedFundInterestRates.getPrevious10YrInterestRate()));
                    
                    doc.appendElement(guaranteedFundInterestRatesElement,previous);
                    
                    //current month
                    Element current = doc.createElement("current");
                    current.setAttribute("month", ReportDataModel.formatDateLongFormMonthOnly(guaranteedFundInterestRates.getCurrentEffectiveDate()));
                    
                    current.setAttribute("threeYear", ReportDataModel.formatPercent(guaranteedFundInterestRates.getCurrent3YrInterestRate()));
                    current.setAttribute("fiveYear", ReportDataModel.formatPercent(guaranteedFundInterestRates.getCurrent5YrInterestRate()));
                    current.setAttribute("tenYear", ReportDataModel.formatPercent(guaranteedFundInterestRates.getCurrent10YrInterestRate()));
                            
                    doc.appendElement(guaranteedFundInterestRatesElement,current);
                    
                    doc.appendElement(fundDetails,guaranteedFundInterestRatesElement);
                }
            }
        }
        doc.appendElement(root,fundDetails);
            
        StringWriter out = new StringWriter();
        DOMWriter.serializeAsXML(doc.getDocument(), out);
    }//end of method
    
    /**
     * calls utility method converts Percentile to 'Display Rank' for display purposes only. 
     * @param percentileValue
     * @return String
     */
    private String convertIndivPercentileToRankForDisplay(BigDecimal percentileValue){
        String displayRank = new CoreToolHelper().convertPercentileToRankForDisplay(percentileValue);
        return displayRank;
    }
    
    /**
     * Utility method to determine if % sign to be shown against criteria or not
     * @param measureValue
     * @param shortName
     * @return String
     */
    private String formatMeasureValueToPercentIfApplicable(String measureValue, String shortName){
        if(shortName.equals(SelectionCriteria.shortNamesForScreen[SelectionCriteria.TOTAL_RETURN]) || 
				shortName.equals(SelectionCriteria.shortNamesForScreen[SelectionCriteria.TOTAL_RETURN_1YEAR]) ||
				shortName.equals(SelectionCriteria.shortNamesForScreen[SelectionCriteria.TOTAL_RETURN_5YEAR]) ||
				shortName.equals(SelectionCriteria.shortNamesForScreen[SelectionCriteria.TOTAL_RETURN_10YEAR]) ||	   
                shortName.equals(SelectionCriteria.shortNamesForScreen[SelectionCriteria.ALPHA]) || 
                shortName.equals(SelectionCriteria.shortNamesForScreen[SelectionCriteria.R2]) || 
                shortName.equals(SelectionCriteria.shortNamesForScreen[SelectionCriteria.UPSIDE_CAPTURE]) || shortName.equals(SelectionCriteria.shortNamesForScreen[SelectionCriteria.DOWNSIDE_CAPTURE]) || shortName.equals(SelectionCriteria.shortNamesForScreen[SelectionCriteria.STANDARD_DEVIATION])){
            return measureValue + ReportDataModel.PERCENT_SYMBOL;
        }
        else{
            return measureValue;
        }
    }
    
    /**
     * Sets dynamic deployed path to image files
     * @return String
     */
    private static String getImagePath() {
        StringBuffer imagePath = new StringBuffer();
        imagePath.append(PdfHelper.class.getResource(CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX));
        return imagePath.toString();
    }
    
    /**
     * Adds customization node in XML which holds report specific data 
     * @param reportDataModel
     * @param doc
     * @param root
     * @param invCat
     * @throws SystemException 
     */
    public void addCustomizationDetails(ReportDataModel reportDataModel, PDFDocument doc, Element root, Hashtable<Integer, InvestmentGroup> invCat) throws SystemException{
        
        setLegendInfo(reportDataModel, invCat);
        
        Element customization = doc.createElement("customization");
        Element imagePath = doc.createElement("imagePath");
        Element presenterName = doc.createElement("presenterName");
        Element presenterFirmName = doc.createElement("presenterFirmName");
        Element preparedForCompanyName = doc.createElement("preparedForCompanyName");
        Element reportPreparedDate = doc.createElement("reportPreparedDate");
        Element coverImage = doc.createElement("coverImage");
        Element asOfDateRor = doc.createElement("asOfDateRor");
        Element asOfDateRorQe = doc.createElement("asOfDateRorQe");
        Element asOfDateExpRatioQe = doc.createElement("asOfDateExpRatioQe");
        Element legendIconsToInclude = doc.createElement("legendIconsToInclude");
        Element selectedAssetClasses = doc.createElement("selectedAssetClasses");//including asset classes which have proxy
        
        Element isBrokerFirmSmithBarneyAssociated = doc.createElement("isBrokerFirmSmithBarneyAssociated");
        Element gaFundDisclosure = doc.createElement("gaFundDisclosure");
        
        String imagePathStr = getImagePath();

        imagePath.setTextContent(imagePathStr);
        presenterName.setTextContent(reportDataModel.getPresenterName());
        presenterFirmName.setTextContent(reportDataModel.getPresenterFirmName());
        preparedForCompanyName.setTextContent(StringUtils.trimToEmpty(reportDataModel.getPreparedForCompanyName()));
        reportPreparedDate.setTextContent(ReportDataModel.formatDateLongForm(new Date()));
        coverImage.setTextContent(reportDataModel.getCoverSheetImageType());

        //CL123594 fix - FundEvaluator PDF report showing incorrect asOfDate  
        Element asOfDateMET = doc.createElement("asOfDateMET");
        asOfDateMET.setTextContent(ReportDataModel.formatDateLongForm(GlobalData.asOfDateMap.get(ReportDataModel.ASOFDATE_FUNDMETRICS_KEY)));
        asOfDateRor.setTextContent(ReportDataModel.formatDateLongForm(GlobalData.asOfDateMap.get(ReportDataModel.ASOFDATE_ROR_KEY)));
        
        asOfDateRorQe.setTextContent(ReportDataModel.formatDateLongForm(GlobalData.asOfDateMap.get(ReportDataModel.ASOFDATE_RORQE_KEY)));
        asOfDateExpRatioQe.setTextContent(ReportDataModel.formatDateLongForm(GlobalData.asOfDateMap.get(ReportDataModel.ASOFDATE_FUND_EXP_RATIO_KEY)));
        isBrokerFirmSmithBarneyAssociated.setTextContent(reportDataModel.isBrokerFirmSmithBarneyAssociated()?"yes":"no");
        String contentText = ContentHelper.getContentText(BDContentConstants.GA_DISCLOSURE, ContentTypeManager.instance().PAGE_FOOTNOTE, null);
		gaFundDisclosure.setTextContent(StringUtils.isBlank(contentText) ? StringUtils.EMPTY : StringUtils.join(new String[]{"^",contentText}));

        Element icons = doc.createElement("icons");
        Element iconLabels = doc.createElement("iconLabels");
        
        if(showCalculatedFund){ 
            Element icon = doc.createElement("icon");
            icon.setTextContent("showCalculatedFund");
            doc.appendElement(icons, icon);
            
            Element calculatedFundLabel = doc.createElement("calculatedFundLabel");
            calculatedFundLabel.setTextContent(ContentHelper.getContentText(BDContentConstants.ICON_LABEL_CALCULATED_FUND,
            		ContentTypeManager.instance().MISCELLANEOUS , null));
            doc.appendElement(iconLabels, calculatedFundLabel);
        }
        if(showManuallyAddedFund){ 
            Element icon = doc.createElement("icon");
            icon.setTextContent("showManuallyAddedFund");
            doc.appendElement(icons, icon);
            
            Element manuallyAddedFundLabel = doc.createElement("manuallyAddedFundLabel");
            manuallyAddedFundLabel.setTextContent(ContentHelper.getContentText(BDContentConstants.ICON_LABEL_ADDED_FUND,
            		ContentTypeManager.instance().MISCELLANEOUS , null));
            doc.appendElement(iconLabels, manuallyAddedFundLabel);
        }
        if(showManuallyRemovedFund){ 
            Element icon = doc.createElement("icon");
            icon.setTextContent("showManuallyRemovedFund");
            doc.appendElement(icons, icon);
            
            Element manuallyRemovedFundLabel = doc.createElement("manuallyRemovedFundLabel");
            manuallyRemovedFundLabel.setTextContent(ContentHelper.getContentText(BDContentConstants.ICON_LABEL_REMOVED_FUND,
            		ContentTypeManager.instance().MISCELLANEOUS , null));
            doc.appendElement(iconLabels, manuallyRemovedFundLabel);
        }
        if(showClosedToNBIcon){
            Element icon = doc.createElement("icon");
            icon.setTextContent("showClosedToNBIcon");
            doc.appendElement(icons, icon);
            
            Element closedToNBIconLabel = doc.createElement("closedToNBIconLabel");
            closedToNBIconLabel.setTextContent(ContentHelper.getContentText(BDContentConstants.ICON_LABEL_CLOSED_FUND,
            		ContentTypeManager.instance().MISCELLANEOUS , null));
            doc.appendElement(iconLabels, closedToNBIconLabel);
        }
        if(showClosedToNBIconSelectedOnly){
            Element icon = doc.createElement("icon");
            icon.setTextContent("showClosedToNBIconSelectedOnly");
            doc.appendElement(icons, icon);
            
            Element closedToNBIconLabel = doc.createElement("closedToNBIconLabel");
            closedToNBIconLabel.setTextContent(ContentHelper.getContentText(BDContentConstants.ICON_LABEL_CLOSED_FUND,
            		ContentTypeManager.instance().MISCELLANEOUS , null));
            doc.appendElement(iconLabels, closedToNBIconLabel);
        }
        if(showContractFundIcon){
            Element icon = doc.createElement("icon");
            icon.setTextContent("showContractFundIcon");
            doc.appendElement(icons, icon);
            
            Element contractFundIconLabel = doc.createElement("contractFundIconLabel");
            contractFundIconLabel.setTextContent(ContentHelper.getContentText(BDContentConstants.ICON_LABEL_CONTRACT_SELECTED_FUND,
            		ContentTypeManager.instance().MISCELLANEOUS , null));
            doc.appendElement(iconLabels, contractFundIconLabel);
        }
        
        
        Element calculatedFundLabel = doc.createElement("notApplicableIconLabel");
        calculatedFundLabel.setTextContent(ContentHelper.getContentText(BDContentConstants.LABEL_NOT_APPLICABLE,
        		ContentTypeManager.instance().MISCELLANEOUS , null));
        doc.appendElement(iconLabels, calculatedFundLabel);
        
        
        doc.appendElement(legendIconsToInclude, icons);
        doc.appendElement(legendIconsToInclude, iconLabels);
        
        Element assetClasses = doc.createElement("assetClasses");
        Iterator<String> itr = assetClassesWithSelectedFunds.iterator();
        Element fund = doc.createElement("fund");
        
        List<String> targetDateFundNames = new ArrayList<String>();
        List<String> lifeStyleFundNames = new ArrayList<String>();
        
        Element layoutOneTargetDate = doc.createElement("layoutOneTargetDate");
        Element layoutOneLifeStyle = doc.createElement("layoutOneLifeStyle");
        Element layoutOneRow = doc.createElement("layoutOneRow");
        Element isFundSelected = doc.createElement("isFundSelected");
               
        isFundSelected.setAttribute("isUserTargetDateSelectedFunds", lifecycleFundFamiliesSelected.size() > 0 ? "yes":"no");
        isFundSelected.setAttribute("isUserLifeStyleSelectedFunds", lifeStyleFundFamiliesSelected.size() > 0 ? "yes":"no");
        
        doc.appendElement(assetClasses, isFundSelected);
        
        while(itr.hasNext()){
        	String assetClassId = itr.next();
        	Element assetClass = doc.createElement("assetClass");
        	assetClass.setTextContent(assetClassId);

        	if(ReportDataModel.ASSET_CLASS_ID_LIFECYCLE.equals(assetClassId)
        	        || ReportDataModel.ASSET_CLASS_ID_LIFESTYLE.equals(assetClassId)) {        		
    			if(ReportDataModel.ASSET_CLASS_ID_LIFECYCLE.equals(assetClassId)){
    				for(String familyCode : lifecycleFundFamiliesSelected){
    					if(!targetDateFundNames.contains(familyCode)){
    						if((targetDateFundNames.size()%4 == 0)){
    							layoutOneRow = doc.createElement("layoutOneRow");
    							doc.appendElement(layoutOneTargetDate, layoutOneRow);
    						}
    						targetDateFundNames.add(familyCode);	
    						Element assetAndFamilyCode = doc.createElement("assetAndFamilyCode");
    						assetAndFamilyCode.setAttribute("familyCode",familyCode);
    						assetAndFamilyCode.setAttribute("assetCLS",assetClassId);
    						doc.appendElement(layoutOneRow, assetAndFamilyCode);
    					}
    				}
    				doc.appendElement(layoutOneTargetDate, layoutOneRow);
    				doc.appendElement(fund, layoutOneTargetDate);
    			}else{
    				for(String familyCode : lifeStyleFundFamiliesSelected){
    					if(!lifeStyleFundNames.contains(familyCode)){
    						if((lifeStyleFundNames.size()%4 == 0)){
    							layoutOneRow = doc.createElement("layoutOneRow");
    							doc.appendElement(layoutOneLifeStyle, layoutOneRow);
    						}
    						lifeStyleFundNames.add(familyCode);	
    						Element assetAndFamilyCode = doc.createElement("assetAndFamilyCode");
    						assetAndFamilyCode.setAttribute("familyCode",familyCode);
    						assetAndFamilyCode.setAttribute("assetCLS",assetClassId);
    						doc.appendElement(layoutOneRow, assetAndFamilyCode);
    					}        				
    				}
        			doc.appendElement(layoutOneLifeStyle, layoutOneRow);
        			doc.appendElement(fund, layoutOneLifeStyle);
    			}
        	}
        	
        	doc.appendElement(assetClasses, fund);
        	doc.appendElement(assetClasses, assetClass);
        }
        
        if (reportDataModel.isIncludeGIFLSelectFunds()) {
            final boolean hasLifestyle = layoutOneLifeStyle.getChildNodes().getLength() > 0;
            if (! hasLifestyle || layoutOneRow.getChildNodes().getLength() >= 4) {
                layoutOneRow = doc.createElement("layoutOneRow");
            }
            final Element assetAndFamilyCode = doc.createElement("assetAndFamilyCode");
            assetAndFamilyCode.setAttribute("familyCode", "");
            assetAndFamilyCode.setAttribute("assetCLS", "LSG");
            doc.appendElement(layoutOneRow, assetAndFamilyCode);
            if (! hasLifestyle) {
                doc.appendElement(layoutOneLifeStyle, layoutOneRow);
                doc.appendElement(fund, layoutOneLifeStyle);
                doc.appendElement(assetClasses, fund);
                final Element assetClass = doc.createElement("assetClass");
                assetClass.setTextContent("LSG");
                doc.appendElement(assetClasses, assetClass);
            }
        }
        doc.appendElement(selectedAssetClasses, assetClasses);
            
        doc.appendElement(customization, imagePath);
        doc.appendElement(customization, presenterName);
        doc.appendElement(customization, presenterFirmName);
        doc.appendElement(customization, preparedForCompanyName);
        doc.appendElement(customization, reportPreparedDate);
        doc.appendElement(customization, coverImage);
        doc.appendElement(customization, asOfDateMET);
        doc.appendElement(customization, asOfDateRor);
        doc.appendElement(customization, asOfDateRorQe);
        doc.appendElement(customization, asOfDateExpRatioQe);
        doc.appendElement(customization, legendIconsToInclude);
        doc.appendElement(customization, selectedAssetClasses);
        doc.appendElement(customization, isBrokerFirmSmithBarneyAssociated);
        doc.appendElement(customization, gaFundDisclosure);
        doc.appendElement(root, customization);
    }
    
    /**
     * Adds fundLineUp node in XML which has fund line up related details for either new plan/existing plan 
     * @param reportDataModel
     * @param doc
     * @param root
     */
    public void addFundLineupDetails(ReportDataModel reportDataModel, PDFDocument doc, Element root){
        
        Element fundLineUp = doc.createElement("fundLineUp");
        
        Element contract = doc.createElement("contract");
        Element companyCode = doc.createElement("companyCode");
        Element includesClosedFunds = doc.createElement("includesClosedFunds");
        Element includesNML = doc.createElement("includesNML");
        Element includesGIFL = doc.createElement("includesGIFL");
        Element fundMenu = doc.createElement("fundMenu");
        Element fundClass = doc.createElement("fundClass");
        
        contract.setAttribute("contractBaseClass", reportDataModel.getContractBaseClassLabel(reportDataModel.getContractBaseClass()));
        String contractBaseFundPackageSeries = reportDataModel.getContractBaseFundPackageSeries();
        if (StringUtils.equalsIgnoreCase(contractBaseFundPackageSeries, "HYB")){
            contract.setAttribute("contractBaseFundPackageSeries", ReportDataModel.PRODUCT_LABEL_ALL);
        }
        else if (StringUtils.equalsIgnoreCase(contractBaseFundPackageSeries, "IFP")){//Note that in production RET and IFP contracts have now been either DI or IA -so may not be able to test even in home state
            contract.setAttribute("contractBaseFundPackageSeries", ReportDataModel.PRODUCT_LABEL_VENTURE);
        }
        else if (StringUtils.equalsIgnoreCase(contractBaseFundPackageSeries, "RET")){//Note that in production RET and IFP contracts have now been either DI or IA -so may not be able to test even in home state
            contract.setAttribute("contractBaseFundPackageSeries", ReportDataModel.PRODUCT_LABEL_RETAIL);
        }
        else{
            contract.setAttribute("contractBaseFundPackageSeries", ReportDataModel.PRODUCT_LABEL_ALL);
        }
        
        contract.setTextContent(reportDataModel.getContractNumber());
        companyCode.setTextContent(reportDataModel.getManulifeShortName());
        includesClosedFunds.setTextContent(reportDataModel.isIncludeClosedFunds()?"yes":"no");
        includesNML.setTextContent(reportDataModel.isNML()?"yes":"no");
        includesGIFL.setTextContent(reportDataModel.isIncludeGIFLSelectFunds()?"yes":"no");
        fundMenu.setTextContent(reportDataModel.getFundOffering());
        fundClass.setTextContent(reportDataModel.getClassLabel());
            
        doc.appendElement(fundLineUp, contract);
        doc.appendElement(fundLineUp, companyCode);
        doc.appendElement(fundLineUp, includesClosedFunds);
        doc.appendElement(fundLineUp, includesNML);
        doc.appendElement(fundLineUp, includesGIFL);
        doc.appendElement(fundLineUp, fundMenu);
        doc.appendElement(fundLineUp, fundClass);
        doc.appendElement(root, fundLineUp);
    }

    /**
     * Adds the reportLayout node in XML which holds pre-defined Section IDs of sections to be included in report. 
     * Section IDs for optional sections are added based user selections on UI step 5
     * 
     * @param reportDataModel
     * @param doc
     * @param root
     * @param optionalSectionIds
     */
    public void addReportLayout(ReportDataModel reportDataModel, PDFDocument doc, Element root, SessionContext sessionContext){
        /**
         * COVP - Cover page
         * TOCS - Table of Contents
         * SIFP - Selecting Investments For Plan
         * CMCR - Custom measurement criteria
         * REAC - Results: investment options by asset class
         * DIPS - Documenting your due diligence with an Investment Policy Statement 
         * RMTH - Ranking methodology
         * FRSE - Fund rankings by asset class - Selected 
         * FRAV - Fund rankings by asset class - Available
         * PEAC - Performance & Expenses -Asset class
         * PERC - Performance & Expenses - Risk cat
         * GLOS - Glossary
         * IMPN - Important Notes
         * BKCO - Back Cover
         * IPST - Investment Policy Statement, DIOS - Default Inv options sub-section
         * IVSF - Investment Selection Form
         * IREP - Investment Replacement Form
         * GIFL - G.I.F.L. Select Information Sheet
         */
    
        ArrayList<String> optionalSectionIdsList = new ArrayList<String>(Arrays.asList(sessionContext.getOptionalSectionIds()));
        
        Element reportLayout = doc.createElement("reportLayout");
        
        Element sections = doc.createElement("sections");
        
        Element fundSuiteName = doc.createElement("fundSuiteName");
        
        Element section1 = doc.createElement("section");
        section1.setAttribute("sectionId", FundEvaluatorConstants.COVER_PAGE_SECTION);//mandatory
        doc.appendElement(sections, section1);
        
        Element section2 = doc.createElement("section");
        section2.setAttribute("sectionId", FundEvaluatorConstants.TABLE_OF_CONTENTS_SECTION);//mandatory
        doc.appendElement(sections, section2);
        
        Element section3 = doc.createElement("section");
        section3.setAttribute("sectionId", FundEvaluatorConstants.SELECTING_INV_FOR_PLAN_SECTION);//mandatory
        doc.appendElement(sections, section3);
        
        Element section7 = doc.createElement("section");
        section7.setAttribute("sectionId", FundEvaluatorConstants.CUSTOM_MEASUREMENT_CRITERIA_SECTION);//mandatory
        doc.appendElement(sections, section7);
        
        Element section8 = doc.createElement("section");
        section8.setAttribute("sectionId", FundEvaluatorConstants.RESULTS_SECTION);//mandatory
        doc.appendElement(sections, section8);
        
        if(optionalSectionIdsList.contains(FundEvaluatorConstants.IPS_SECTION)){
            Element section10 = doc.createElement("section");
            section10.setAttribute("sectionId", FundEvaluatorConstants.DOCUMENTING_DUE_DILIGENCE_SECTION);//optional
            doc.appendElement(sections, section10);
        }
        if(optionalSectionIdsList.contains(FundEvaluatorConstants.FUND_RANKING_SELECTED_FUNDS_SECTION) || optionalSectionIdsList.contains(FundEvaluatorConstants.FUND_RANKING_ALL_AVAILABLE_FUNDS_SECTION)){
            Element section11 = doc.createElement("section");
            section11.setAttribute("sectionId", FundEvaluatorConstants.RANKING_METHODOLOGY_SECTION);//optional
            doc.appendElement(sections, section11);
        }
        if(optionalSectionIdsList.contains(FundEvaluatorConstants.FUND_RANKING_SELECTED_FUNDS_SECTION)){
            Element section12A = doc.createElement("section");
            section12A.setAttribute("sectionId", FundEvaluatorConstants.FUND_RANKING_SELECTED_FUNDS_SECTION);//Optional - but only type A or B
            doc.appendElement(sections, section12A);
        }
        if(optionalSectionIdsList.contains(FundEvaluatorConstants.FUND_RANKING_ALL_AVAILABLE_FUNDS_SECTION)){
            Element section12B = doc.createElement("section");
            section12B.setAttribute("sectionId", FundEvaluatorConstants.FUND_RANKING_ALL_AVAILABLE_FUNDS_SECTION);//Optional - but only type A or B
            doc.appendElement(sections, section12B);
        }
        if(optionalSectionIdsList.contains(FundEvaluatorConstants.PERFORMANCE_BY_ASSET_CLASS_SECTION)){
            Element section13A = doc.createElement("section");
            section13A.setAttribute("sectionId", FundEvaluatorConstants.PERFORMANCE_BY_ASSET_CLASS_SECTION);//Mandatory - but only type A or B
            doc.appendElement(sections, section13A);
        }
        if(optionalSectionIdsList.contains(FundEvaluatorConstants.PERFORMANCE_BY_RISK_CATEGORY_SECTION)){
            Element section13B = doc.createElement("section");
            section13B.setAttribute("sectionId", FundEvaluatorConstants.PERFORMANCE_BY_RISK_CATEGORY_SECTION);//Mandatory - but only type A or B
            doc.appendElement(sections, section13B);
        }
        if(optionalSectionIdsList.contains(FundEvaluatorConstants.GLOSSARY_SECTION)){
            Element section14 = doc.createElement("section");
            section14.setAttribute("sectionId", FundEvaluatorConstants.GLOSSARY_SECTION);//optional
            doc.appendElement(sections, section14);
        }
        
        Element section15 = doc.createElement("section");
        section15.setAttribute("sectionId", FundEvaluatorConstants.IMPORTANT_NOTES_SECTION);//mandatory
        doc.appendElement(sections, section15);
        
        Element section16 = doc.createElement("section");
        section16.setAttribute("sectionId", FundEvaluatorConstants.BACK_COVER_SECTION);//mandatory
        doc.appendElement(sections, section16);
        
        if(optionalSectionIdsList.contains(FundEvaluatorConstants.IPS_SECTION)){
            Element section17 = doc.createElement("section");
            section17.setAttribute("sectionId", FundEvaluatorConstants.IPS_SECTION);//optional
            doc.appendElement(sections, section17);
            if(optionalSectionIdsList.contains(FundEvaluatorConstants.DEFAULT_INV_OPTION_SECTION)){
                Element section17A = doc.createElement("section");
                section17A.setAttribute("sectionId", FundEvaluatorConstants.DEFAULT_INV_OPTION_SECTION);//optional
                doc.appendElement(sections, section17A);

                //To displays the lifeCycleFundsSuiteName, lifeStyleFundsSuiteName and lifeStyleFundsName in PDF
                ArrayList<String> lifecycleFundNameList = sessionContext.getLifecycleFundSuiteNames();
                Element lifecycleFundSuits = doc.createElement("lifecycleFundSuits");
                if(lifecycleFundNameList != null){
                for(String fundSuite : lifecycleFundNameList){
            		Element lifecycleFundSuiteName = doc.createElement("lifecycleFundSuiteName");
                	lifecycleFundSuiteName.setAttribute("fundSuite",fundSuite);
                    doc.appendElement(lifecycleFundSuits, lifecycleFundSuiteName);
                }
                doc.appendElement(fundSuiteName, lifecycleFundSuits);
                }
                
                Element lifestyleFundSuits = doc.createElement("lifestyleFundSuits");
                
                LinkedHashMap<String, ArrayList<String>> lifestyleFundMap =  (LinkedHashMap<String, ArrayList<String>>)sessionContext.getLifeStyleFunds();
                LinkedHashMap<String, String>  lifestyleFundNameMap =  (LinkedHashMap<String, String>)sessionContext.getLifestyleFundsDetails();
                
                if(lifestyleFundMap != null && lifestyleFundNameMap != null){
                for(Map.Entry<String, String> lifestyleFund : lifestyleFundNameMap.entrySet()){//loop for each fundFamilyCode
            	   if(!FundEvaluatorConstants.RUSSELL_FUND_FAMILY_CODE.equals(lifestyleFund.getKey())){
            		   if(lifestyleFundMap.containsKey(lifestyleFund.getKey())){
                			Element lifestylefundSuiteName = doc.createElement("lifestylefundSuiteName");
                			String fundSuite = lifestyleFund.getValue();
                		
                			lifestylefundSuiteName.setAttribute("fundSuite", fundSuite);
  
                			ArrayList<String> lifestylefundNames = lifestyleFundMap.get(lifestyleFund.getKey());
                			
                			Element lifestyleFundNameGroup = doc.createElement("lifestyleFundNameGroup");
                			for(String fundName : lifestylefundNames){
                				Element lifestylefundName = doc.createElement("lifestylefundName");
                				lifestylefundName.setAttribute("fundName",fundName);
                				doc.appendElement(lifestyleFundNameGroup, lifestylefundName);
                            }
                			doc.appendElement(lifestylefundSuiteName, lifestyleFundNameGroup); 
                			doc.appendElement(lifestyleFundSuits, lifestylefundSuiteName);
                		}
                	}
                }
                doc.appendElement(fundSuiteName, lifestyleFundSuits);
               }  
                
           }
            
        }
        if(optionalSectionIdsList.contains(FundEvaluatorConstants.INV_SELECTION_FORM_SECTION)){
            Element section18 = doc.createElement("section");
            section18.setAttribute("sectionId", FundEvaluatorConstants.INV_SELECTION_FORM_SECTION);//optional
            doc.appendElement(sections, section18);
        }
        
        if(optionalSectionIdsList.contains(FundEvaluatorConstants.GIFL_SELECT_INFORMATION_SECTION)){
            Element giflSelectSection = doc.createElement("section");
            giflSelectSection.setAttribute("sectionId", FundEvaluatorConstants.GIFL_SELECT_INFORMATION_SECTION);//optional
            doc.appendElement(sections, giflSelectSection);
        }
        
        doc.appendElement(reportLayout, fundSuiteName);
        doc.appendElement(reportLayout, sections);
        doc.appendElement(root, reportLayout);
    }

    /**
     * Adds endNotes node in XML which has all general and fund footnotes to be included in Pdf report and retrieves content by site (site for fund line up chosen by user)
     * @param reportDataModel
     * @param doc
     * @param root
     * @throws SystemException
     */
    public void addEndnotes(ReportDataModel reportDataModel, PDFDocument doc, Element root) throws SystemException{
        String site = LOCATION_USA;//setting default as USA
        if(StringUtils.isNotBlank(reportDataModel.getContractNumber())){//if contract is provided
            site = reportDataModel.getContractLocationId();//taking site info based on contract
        }
        else{
            site = reportDataModel.getSubsidiaryId();//taking site info based on user selections on Step 1
        }
        Location location;
        if(site.equalsIgnoreCase(LOCATION_USA) || site.equalsIgnoreCase(COMPANY_ID_USA)){
            location = Location.USA;
        }
        else{
            location = Location.NEW_YORK;
        }
        
        String investmentOptionsContent = ContentHelper.getContentText(BDContentConstants.INVESTMENT_OPTIONS, ContentTypeManager.instance().FOOTNOTE, location);
        String subAdvisorContent = ContentHelper.getContentText(BDContentConstants.SUB_ADVISOR, ContentTypeManager.instance().FOOTNOTE, location);
        String inceptionDateContent = ContentHelper.getContentText(BDContentConstants.INCEPTION_DATE, ContentTypeManager.instance().FOOTNOTE, location);
        //String sinceInceptionContent = ContentHelper.getContentText(BDContentConstants.SINCE_INCEPTION, ContentTypeManager.instance().FOOTNOTE, location);
        String performanceMonthEndContent = ContentHelper.getContentText(BDContentConstants.PERFORMANCE_ASOF_MONTHEND, ContentTypeManager.instance().FOOTNOTE, location);
        //String performanceYearEndContent = ContentHelper.getContentText(BDContentConstants.PERFORMANCE_ASOF_YEAREND, ContentTypeManager.instance().FOOTNOTE, location);
        String expenseRatioContent = ContentHelper.getContentText(BDContentConstants.EXPENSE_RATIO, ContentTypeManager.instance().FOOTNOTE, location);
        String morningStarBenchmarkContent = ContentHelper.getContentText(BDContentConstants.MORNING_STAR_BENCHMARK_CATEGORY, ContentTypeManager.instance().FOOTNOTE, location);
        
        String generalFootnotesBySite = ContentHelper.getContentText(BDContentConstants.GENERAL_FOOTNOTES_BY_SITE, ContentTypeManager.instance().PAGE_FOOTNOTE, location);
        
        String riskDisclosuresBySite = ContentHelper.getContentText(BDContentConstants.RISK_DISCLOSURES_BY_SITE, ContentTypeManager.instance().DISCLAIMER, location);
        
        String importantNoteNotRankedContent = ContentHelper.getContentText(BDContentConstants.IMPORTANT_NOTES_NOT_RANKED, ContentTypeManager.instance().FOOTNOTE, location);
        
        Element endNotes = doc.createElement("endNotes");
        
        Element generalFootnotes = doc.createElement("generalFootnotes");
        Element footnoteElement1 = doc.createElement("footnote");
        Element symbolElement1 = doc.createElement("symbol");
        
        Element footnoteElement2 = doc.createElement("footnote");
        Element symbolElement2 = doc.createElement("symbol");
        
        Element footnoteElement3 = doc.createElement("footnote");
        Element symbolElement3 = doc.createElement("symbol");
        
        Element footnoteElement4 = doc.createElement("footnote");
        Element symbolElement4 = doc.createElement("symbol");
        
        Element footnoteElement5 = doc.createElement("footnote");
        Element symbolElement5 = doc.createElement("symbol");
        
        Element footnoteElement6 = doc.createElement("footnote");
        Element symbolElement6 = doc.createElement("symbol");
        
        Element footnoteElementNotRanked = doc.createElement("footnote");
		Element symbolElementNotRanked = doc.createElement("symbol");
		
        setGeneralFootnotes("  ", importantNoteNotRankedContent, generalFootnotes, footnoteElementNotRanked, symbolElementNotRanked, doc);
        setGeneralFootnotes("*1", performanceMonthEndContent, generalFootnotes, footnoteElement6, symbolElement6, doc);
        setGeneralFootnotes("*2", investmentOptionsContent, generalFootnotes, footnoteElement1, symbolElement1, doc);
        setGeneralFootnotes("*3", subAdvisorContent, generalFootnotes, footnoteElement2, symbolElement2, doc);
        setGeneralFootnotes("*6", expenseRatioContent, generalFootnotes, footnoteElement4, symbolElement4, doc);
        setGeneralFootnotes("*7", morningStarBenchmarkContent, generalFootnotes, footnoteElement5, symbolElement5, doc);
        setGeneralFootnotes("*10", inceptionDateContent, generalFootnotes, footnoteElement3, symbolElement3, doc);
        
        Element fundFootnotes = doc.createElement("fundFootnotes");
        
        String companyId = "";
        if(StringUtils.equalsIgnoreCase(site, LOCATION_USA)
        || StringUtils.equalsIgnoreCase(site, COMPANY_ID_USA)){
            companyId = FundEvaluatorConstants.COMPANY_ID_USA;
        }
        else{
            companyId = FundEvaluatorConstants.COMPANY_ID_NY;
        }
        
        Map<String, String> unsortedFootnotes =     reportDataModel.getFundFootnoteSymbolsInReport();
        
        String[] footnoteSymbolsArray = unsortedFootnotes.keySet().toArray(new String[unsortedFootnotes.keySet().size()]);
        
        if ( footnoteSymbolsArray != null  ) {
			Footnote[] sortedSymbolsArray = 
				FootnoteCacheImpl.getInstance().sortFootnotes(
						footnoteSymbolsArray, companyId);		

			/**
			 * loop through the footnoteSymbolsArray, and create a DOM node for each footnote.
			 */
			for(int i = 0; i < sortedSymbolsArray.length; i++){
				if (sortedSymbolsArray[i] != null){
					Element footnote = doc.createElement("footnote");
		            Element symbol = doc.createElement("symbol");
		            
		            String text = sortedSymbolsArray[i].getText();
		            
		            symbol.setTextContent(sortedSymbolsArray[i].getSymbol());
		            PdfHelper.convertIntoDOM("cmaContent", footnote, doc, text);
		            
		            footnote.appendChild(symbol);
		            
		            if (null == text || "null".equals(text)) text = "";
		            
		            doc.appendElement(fundFootnotes, footnote);
					
				}
			}				
		}
        
        Element riskDisclosuresForSite = doc.createElement("riskDisclosuresForSite");
        PdfHelper.convertIntoDOM("cmaContent", riskDisclosuresForSite, doc, riskDisclosuresBySite);
        
        Element generalFootnotesForSite = doc.createElement("generalFootnotesForSite");
        PdfHelper.convertIntoDOM("cmaContent", generalFootnotesForSite, doc, generalFootnotesBySite);
        
        doc.appendElement(endNotes, generalFootnotes);
        doc.appendElement(endNotes, fundFootnotes);
        doc.appendElement(endNotes, riskDisclosuresForSite);
        doc.appendElement(endNotes, generalFootnotesForSite);
        doc.appendElement(root, endNotes);
    
    }
    /**
     * This method retrieves CMA fund footnotes by site
     * @param isNY
     * @return Map<String, String> footnote map as per site
     * @throws SystemException
     */
    private Map<String, String> retrieveFootnotesFromCMAService(boolean isNY) throws SystemException{
        MutableFootnote[] footnotes = (MutableFootnote[]) FootnoteLookupHelper.retrieveFootnotesFromCMAService(isNY);

        Map<String, String> footnoteMap = new HashMap<String, String>();
        for (int i = 0; i < (null == footnotes ? 0 : footnotes.length); i++) {
            String text = (String)footnotes[i].getText();
            if (null != text) {
                text = text.trim();
            }
            footnoteMap.put(footnotes[i].getSymbol(), text);
        }
        return footnoteMap;
    }
    /**
     * Utility method for general footnotes
     * 
     * @param symbol
     * @param cmaText
     * @param generalFootnotes
     * @param footnoteElement
     * @param symbolElement
     * @param cmaContentElement
     * @param doc
     */
    private void  setGeneralFootnotes(String symbol, String cmaText, Element generalFootnotes, Element footnoteElement, Element symbolElement, PDFDocument doc ){
        symbolElement.setTextContent(symbol);
        PdfHelper.convertIntoDOM("cmaContent", footnoteElement, doc, cmaText);
        footnoteElement.appendChild(symbolElement);
        doc.appendElement(generalFootnotes, footnoteElement);
    }
    
    /**
     * This method determines which legend icons are to be included in report
     * @param rdm
     * @param invCat
     */
    private void setLegendInfo(ReportDataModel rdm, Hashtable<Integer, InvestmentGroup> invCat){
        
        Enumeration<Integer> e1 = invCat.keys();
        while(e1.hasMoreElements()){  //loop on each Inv cat
            int key = e1.nextElement();
            
            if(!showClosedToNBIcon){//if not already set to true
                
                InvestmentCategory ic = rdm.getInvestmentCategoryFor(key);
                if(ic!=null){
                    List<DecoratedFund> decoratedFunds = ic.getDecoratedFunds();
                    
                    if(decoratedFunds!=null){
                        Iterator<DecoratedFund> itr = decoratedFunds.iterator();
                        while (itr.hasNext()) {//loop for each fund
                            DecoratedFund df = itr.next();
                            if(df.isClosedToNB()){
                                showClosedToNBIcon = true;//even 1 fund requires icon - means we include it
                                break;
                            }
                        }
                    }
                }
            }
        }
        e1 = invCat.keys();
        while(e1.hasMoreElements()){  //loop on each Inv cat
            int key = e1.nextElement();
            
            // break the loop if we have already calculated the values
            if(showCalculatedFund && showManuallyAddedFund && showManuallyRemovedFund) {
            	break;
            }
                
            InvestmentCategory ic = rdm.getInvestmentCategoryFor(key);
            if(ic!=null){
                List<DecoratedFund> decoratedFunds = ic.getDecoratedFunds();
                
                if(decoratedFunds!=null){
                    Iterator<DecoratedFund> itr = decoratedFunds.iterator();
                    while (itr.hasNext()) {//loop for each fund
                        DecoratedFund df = itr.next();
                        if(df.isToolSelected()){
                        	showCalculatedFund = true;//even 1 fund requires icon - means we include it
                        } 
                        if(!df.isContractSelected() && !df.isToolSelected() && df.isChecked()) {
                        	showManuallyAddedFund = true;
                        }
                        if((df.isContractSelected() || df.isToolSelected()) && !df.isChecked()) {
                        	showManuallyRemovedFund = true;
                        }
                    }
                }
            }
        }
        e1 = invCat.keys();
        while(e1.hasMoreElements()){  //loop on each Inv cat
            int key = e1.nextElement();
            
            if(!showClosedToNBIconSelectedOnly){//if not already set to true
                
                InvestmentCategory ic = rdm.getInvestmentCategoryFor(key);
                if(ic!=null){
                    List<DecoratedFund> decoratedFunds = ic.getDecoratedFunds();
                    
                    if(decoratedFunds!=null){
                        Iterator<DecoratedFund> itr = decoratedFunds.iterator();
                        while (itr.hasNext()) {//loop for each fund
                            DecoratedFund df = itr.next();
                            if(df.isClosedToNB() && df.isChecked()){
                                showClosedToNBIconSelectedOnly = true;//even 1 fund requires icon - means we include it
                                break;
                            }
                        }
                    }
                }
            }
        }
        e1 = invCat.keys();
        while(e1.hasMoreElements()){  //loop on each Inv cat
            int key = e1.nextElement();
            
            if(!showContractFundIcon){//if not already set to true
                
                InvestmentCategory ic = rdm.getInvestmentCategoryFor(key);
                if(ic!=null){
                    List<DecoratedFund> decoratedFunds = ic.getDecoratedFunds();
                    
                    if(decoratedFunds!=null){
                        Iterator<DecoratedFund> itr = decoratedFunds.iterator();
                        while (itr.hasNext()) {//loop for each fund
                            DecoratedFund df = itr.next();
                            if(df.isContractSelected()){
                                showContractFundIcon = true;//even 1 fund requires icon - means we include it
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Method to format the Number of Funds In Category in the prescribed format
     * 
     * @param numberToFormat
     * @return
     */
	private String formatFundsInCategory(String numberToFormat) {
		NumberFormat formatter = new DecimalFormat("#,###");
		return formatter.format(Integer.parseInt(numberToFormat));
	}
}
