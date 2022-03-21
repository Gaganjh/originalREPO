package com.manulife.pension.bd.web.fundEvaluator.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.fundEvaluator.common.AssetClassForInvOption;
import com.manulife.pension.bd.web.fundEvaluator.common.CoreToolConstants;
import com.manulife.pension.bd.web.fundEvaluator.common.CoreToolGlobalData;
import com.manulife.pension.bd.web.fundEvaluator.common.FundForInvOption;
import com.manulife.pension.bd.web.fundEvaluator.report.ReportDataModel;
import com.manulife.pension.bd.web.fundEvaluator.report.ReportDataProcessor;
import com.manulife.pension.bd.web.fundEvaluator.report.ReportInputData;
import com.manulife.pension.bd.web.fundEvaluator.session.SessionContext;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.FundClassUtility;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.service.fund.coretool.model.common.SelectionCriteria;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.AssetClass;
import com.manulife.pension.service.fund.valueobject.ContractFund;
import com.manulife.pension.service.fund.valueobject.Fund;
import com.manulife.pension.service.fund.valueobject.FundMetrics;
import com.manulife.pension.service.fund.valueobject.InvestmentGroup;
import com.manulife.pension.service.fund.valueobject.MorningstarPerformanceAndFundRating;
import com.manulife.pension.service.fund.valueobject.PercentileRankedFund;

/**
 * This class builds value objects required for populating UI Detail or Preview overlays
 * @author: PWakode
 */
public class CoreToolProcessor implements CoreToolConstants{
        
    
    private ArrayList<AssetClassForInvOption > assetClassForInvOptionList = new ArrayList<AssetClassForInvOption >();
    private CoreToolSessionData coreToolSessionData = null;
    private List<String> investmentIdsPlusRateType;
    
    private static final Logger logger = Logger.getLogger(CoreToolProcessor.class);
    
    /**
     * This method set base fund line up details i.e 
     * (1) Funds within each Asset class 
     * (2) Calculates Fund rankings 
     * (3) Identifies funds to be recommended/pre-selected by system based on user input
     * 
     * @param SessionContext object - contains all user input from UI
     * @return - CoreToolSessionData object has base fund line up & ranking information (but not detailed fund metrics)
     * @throws SystemException 
     */
    private CoreToolSessionData createCoreToolData(SessionContext sessionContext) throws SystemException{
        
        logger.debug("CoreToolSessionData start");
        return new CoreToolSessionData(sessionContext);
    }
    
    /**
     * This method sets default ordering of funds if it is not explicitly provided
     */
    public ArrayList<AssetClassForInvOption> populateInvestmentOptionVOs(SessionContext sessionContext) throws SystemException{
        return(populateInvestmentOptionVOs(sessionContext, DEFAULT_ORDER));
    }

	/**
	 * This method is to iterate the Morning Star Funds List and prepare the map object
	 * 
	 * @return
	 * @throws SystemException
	 */
	private Map<String, MorningstarPerformanceAndFundRating> getMorningstarPerformanceAndFundRating()
			throws SystemException {

		Map<String, MorningstarPerformanceAndFundRating> morningstarAndFundRatingMap = 
			new HashMap<String, MorningstarPerformanceAndFundRating>();

		for (MorningstarPerformanceAndFundRating morningstarAndFundRating : FundServiceDelegate
				.getInstance().getAllMorningstarPerformance()) {

				morningstarAndFundRatingMap.put(
						morningstarAndFundRating.getFundId(),
						morningstarAndFundRating);
		}

		return morningstarAndFundRatingMap;
	}
	
    /**
     * This method is to load initial state for UI depending on user selections/shortcuts or contract. 
     * It should load fund objects and set required attributes
     * 
     * @param SessionContext object - contains all user input & base fund line up details
     * @param String order - indicator to determine if ordering of funds should be default (best to worst) or reverse
     * @return - ArrayList -Investment option array list i.e. List of AssetClass objects each having a list of Fund objects set with required attributes
     * @throws SystemException 
     */
    public ArrayList<AssetClassForInvOption> populateInvestmentOptionVOs(SessionContext sessionContext, String order) throws SystemException{
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateInvestmentOptionVOs()");
        }
        try{
            coreToolSessionData = createCoreToolData(sessionContext);
            sessionContext.setCoreToolData(coreToolSessionData);
            
            Hashtable<String, ContractFund> contractFundTable = null;
            
            List<String> investmentIds = coreToolSessionData.getInvestmentIds();
            List<String> contractSelectedFunds = coreToolSessionData.getContractSelectedFunds();
            
          //here we get funds that are already filtered by Contract (if contract # was provided)
            investmentIdsPlusRateType = coreToolSessionData.getInvestmentIdsPlusRateType();
            
            Hashtable<String, Fund> fundLineupFundTable = FundServiceDelegate.getInstance().getFundObjTableForFundList(investmentIds);
            
            Hashtable<String, FundMetrics> fundLineupFundMetricTable = FundServiceDelegate.getInstance().getFundMetricObjTableForFundAndRateList(investmentIdsPlusRateType);

			Map<String, MorningstarPerformanceAndFundRating> morningstarPerformanceAndFundRating =
					getMorningstarPerformanceAndFundRating();
	

            CoreToolHelper coreToolHelper = new CoreToolHelper();
            
            List<String> svfCompetingFunds = new ArrayList<String>();
            
           //if not MMF then SVF is selected .
            if(!sessionContext.isSelectMarketFundsAsDefault()){
            	svfCompetingFunds = FundServiceDelegate.getInstance().getSVFCompetingFunds( Calendar.getInstance().getTime());
            	 if(svfCompetingFunds!= null){
            		 svfCompetingFunds.remove(sessionContext.getSVFFundList());
            	 }
           }
            
            String state = StringUtils.EMPTY;
           if(StringUtils.isNotBlank(sessionContext.getStateCode())){
        	   state= StringUtils.equals("NY",sessionContext.getStateCode())?"NY":"USA";
            }else if(StringUtils.isNotBlank(sessionContext.getContractLocationId())){
            	 state= StringUtils.equals("019",sessionContext.getContractLocationId())?"USA":"NY";
            }
            	
           // retrieves the PBA competing funds
            List<String> PBACompetingFunds = FundServiceDelegate.getInstance().getPBACompetingFunds( Calendar.getInstance().getTime(),state);
            
            boolean isContractHavingAnyLifeStyleFunds = false;
            boolean isContractHavingAnyHQSFunds = false;
            int contractId = 0; 

            List<String> allSvgiFunds = FundServiceDelegate.getInstance().getAllSVGIFunds();
            List<String> nonGenericSvgifFunds = FundServiceDelegate.getInstance().getSVGIFFunds();
			List<String> defaultSvgiFunds = FundServiceDelegate.getInstance().getDefaultSvgiFund();
            
            if(StringUtils.isNotBlank(sessionContext.getContract())){//if contract is provided
                contractId = Integer.parseInt(sessionContext.getContract());
                contractFundTable = coreToolHelper.getContractFunds(contractId);
                
                isContractHavingAnyLifeStyleFunds = coreToolHelper.isContractHavingAnyLifeStyleFunds(contractFundTable);
                isContractHavingAnyHQSFunds = coreToolHelper.isContractHavingAnyHQSFunds(contractFundTable);

    			for (String svgiFund : allSvgiFunds){
    				if(contractSelectedFunds.contains(svgiFund)) {
    			  		allSvgiFunds.remove(svgiFund);
    			  		break;
    			  	}
    			}
    			if(StringUtils.isNotEmpty(sessionContext.getContractBaseClass()) && 
    					sessionContext.getContractBaseClass().equals(BDConstants.CLASS_ZERO)) {
    				for (String defaultSvgiFund : defaultSvgiFunds){
    			    	allSvgiFunds.remove(defaultSvgiFund);
    				}
    			}
            }
         
            String classType = sessionContext.getClassMenu();
            // All the Asset Classes sorted by order
            
            HashMap<String, AssetClass> assetClassMap = FundServiceDelegate.getInstance().getAssetClasses();
            
            Hashtable<String, List<String>> assetHouseStruct = FundServiceDelegate.getInstance().getAssetClassesByAssetHouseID();
            Iterator<String> allAssetHouseIdAndNames = FundServiceDelegate.getInstance().getAssetHouseIdNameOrderedList().iterator();
            while(allAssetHouseIdAndNames.hasNext()){//loop by each asset house
                String key = allAssetHouseIdAndNames.next();
                
                List<String> acList = assetHouseStruct.get(key);
                //loop by each asset class within an asset house
                for (String assetClassID : acList ) {
                	// GIFL funds will be ignored based on user selection
                    // stable value funds will always be ignored
                    if((sessionContext.isIncludeGIFLSelectFunds()? true : !assetClassID.equals(ASSET_CLASS_ID_GIFL)) && !assetClassID.equals(ASSET_CLASS_ID_STABLE_VALUE)){
                        
                        ArrayList<FundForInvOption> fundForInvOptionHashList = new ArrayList<FundForInvOption>();
                        String assetClassId = assetClassID;
                        
                        final AssetClassForInvOption assetClassForInvOption =
                                new AssetClassForInvOption(
                                        assetClassMap.get(assetClassId));
                        
                        //asset class selected only if it appears in CoreToolData
                        assetClassForInvOption.setSelected(
                                sessionContext.getCoreToolData()
                                .getSelectedAssetClassesMap()
                                .get(assetClassId) != null);
                        
                        List<PercentileRankedFund> percentileRankedFundsList =
                                sessionContext.getCoreToolData().getRankedFundsByAssetClassID(assetClassId);
                        
                        if (percentileRankedFundsList == null) {
                        	assetClassForInvOption.setFundForInvOptionList(fundForInvOptionHashList);
                        	assetClassForInvOptionList.add(assetClassForInvOption);
                            continue;
                        }
            
                        List<PercentileRankedFund> percentileRankedFundsListWithoutSVG = new ArrayList<PercentileRankedFund>();

                		if(StringUtils.isNotBlank(sessionContext.getContract())) {
	                        for (PercentileRankedFund percentileRankedFund : percentileRankedFundsList) {
	                        	if(!allSvgiFunds.contains(percentileRankedFund.getFundID())) {
	                        		percentileRankedFundsListWithoutSVG.add(percentileRankedFund);   
	                        	}
	                        }
                        }else {
	                        for (PercentileRankedFund percentileRankedFund : percentileRankedFundsList) {
	                        	if(StringUtils.isNotEmpty(classType) && ((classType.equalsIgnoreCase(BDConstants.CLASS_ZERO) || 
	                        			classType.equalsIgnoreCase(BDConstants.SIGNATURE_PLUS)) && !nonGenericSvgifFunds.contains(percentileRankedFund.getFundID())) || 
	                        			(!(classType.equalsIgnoreCase(BDConstants.CLASS_ZERO) || 
			                        			classType.equalsIgnoreCase(BDConstants.SIGNATURE_PLUS)) && !allSvgiFunds.contains(percentileRankedFund.getFundID()))) {
	                        		percentileRankedFundsListWithoutSVG.add(percentileRankedFund);  
	                        	}
	                		}
                        }
                        
                        // Funds sorted according to selection and sortNumber
                        List<FundForInvOption> sortedFunds = new Vector<FundForInvOption>();
                        // lists all funds
                        for (PercentileRankedFund percentileRankedFund : percentileRankedFundsListWithoutSVG) {
                            
                            String fundId = percentileRankedFund.getFundID(); 
                            
                            Fund fund = fundLineupFundTable.get(percentileRankedFund.getFundID());
                            
                            FundForInvOption fundForInvOption = new FundForInvOption(fund);
                            // if the current fund id exist in the PBA competing funds
                            if(PBACompetingFunds!= null && PBACompetingFunds.contains(fundId)){
                            	fundForInvOption.setPBACompetingFund(true);
                            }
                            
                            fundForInvOption.setRateType(percentileRankedFund.getRateType());//This rate type already set as per each funds individual rate type in CoreToolSessionData
                            
                            fundForInvOption.setRank(percentileRankedFund.getRank());
                            fundForInvOption.setDisplayRank(percentileRankedFund.getDisplayRank());
                            fundForInvOption.setBenchmarkMetricsAvailable(
                                    percentileRankedFund.isBenchmarkMetricsAvailable());
                            fundForInvOption.setClosedToNB(fund.isClosedToNewBusiness());
                            fundForInvOption.setIndex(fund.getAssetClassId().equals(ASSET_CLASS_ID_INDEX)? true:false);
                            
                            if (sessionContext
                                .getCoreToolData()
                                .getToolRecommendedFundsMap()
                                .get(fundId) != null) {
                                // This fund was recommended by the tool
                                fundForInvOption.setToolSelected(true);
                                assetClassForInvOption.setHasToolRecommendedFund(true);
                            } 
                            sortedFunds.add(fundForInvOption);
                        }
                        
                        sortedFunds = coreToolHelper.organizeFundDisplaySequence(sortedFunds, assetClassId, order);
                        
                        for (FundForInvOption fundForInvOption : sortedFunds) {
                            
                            addAttributesToFundForInvOption(                           
                                    sessionContext,
                                    assetClassForInvOption,
                                    fundForInvOption,  
                                    isContractHavingAnyLifeStyleFunds, 
									isContractHavingAnyHQSFunds, svfCompetingFunds, contractSelectedFunds,
									morningstarPerformanceAndFundRating);
                            addFundMetricsToFundForInvOption(sessionContext,fundForInvOption,fundLineupFundMetricTable);
                            
            //              here we need to add FundForInvOption to FundForInvOptionList
                            fundForInvOptionHashList.add(fundForInvOption);
                        }
            //          and add setFundForInvOptionList to AssetClassForInvOption
                        assetClassForInvOption.setFundForInvOptionList(fundForInvOptionHashList);
                        assetClassForInvOptionList.add(assetClassForInvOption);
                    }
                }
            }   
        }
        catch(Throwable e){
            logger.debug("populateInvestmentOptionVOs : Problem occured while populating Investment option VOs" + e);
            throw new SystemException(e, "populateInvestmentOptionVOs : Problem occured while populating Investment option VOs");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> populateInvestmentOptionVOs()");
        }
        return assetClassForInvOptionList;
    }
    
    /**
     * Set required attributes to fund objects
     */
    private void addAttributesToFundForInvOption(SessionContext sessionContext,
            AssetClassForInvOption assetClassForInvOption, FundForInvOption fundForInvOption, 
            boolean isContractHavingAnyLifeStyleFunds, boolean isContractHavingAnyHQSFunds,
			List<String> svfCompetingFunds,
			List<String> contractSelectedFunds,
			Map<String, MorningstarPerformanceAndFundRating> morningstarPerformanceAndFundRating)
			throws SystemException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> addAttributesToFundForInvOption()");
        }
        
        String fundId = fundForInvOption.getFund().getFundId();
        
		if (morningstarPerformanceAndFundRating != null
				&& morningstarPerformanceAndFundRating.containsKey(fundId)) {

			//Number of funds in category for 1,3,5 & 10 years are being returned as 
			//BigDecimal values so casting it to String
			
			String oneYearNumberofFundsInCategory = (morningstarPerformanceAndFundRating
					.get(fundId).getOneYearNumberofFundsInCategory() != null) ? morningstarPerformanceAndFundRating
					.get(fundId).getOneYearNumberofFundsInCategory().toString()
					: null;
			String threeYearNumberofFundsInCategory = (morningstarPerformanceAndFundRating
					.get(fundId).getThreeYearNumberofFundsInCategory() != null) ? morningstarPerformanceAndFundRating
					.get(fundId).getThreeYearNumberofFundsInCategory()
					.toString()
					: null;
			String fiveYearNumberofFundsInCategory = (morningstarPerformanceAndFundRating
					.get(fundId).getFiveYearNumberofFundsInCategory() != null) ? morningstarPerformanceAndFundRating
					.get(fundId).getFiveYearNumberofFundsInCategory()
					.toString()
					: null;
			String tenYearNumberofFundsInCategory = (morningstarPerformanceAndFundRating
					.get(fundId).getTenYearNumberofFundsInCategory() != null) ? morningstarPerformanceAndFundRating
					.get(fundId).getTenYearNumberofFundsInCategory().toString()
					: null;

			fundForInvOption.getFund().setOneYearNumberofFundsInCategory(oneYearNumberofFundsInCategory);
			fundForInvOption.getFund().setThreeYearNumberofFundsInCategory(threeYearNumberofFundsInCategory);
			fundForInvOption.getFund().setFiveYearNumberofFundsInCategory(fiveYearNumberofFundsInCategory);
			fundForInvOption.getFund().setTenYearNumberofFundsInCategory(tenYearNumberofFundsInCategory);
		}
		
        boolean isSelected = false;//tool selection due to Asset Allocation OR Compulsory funds
        boolean isChecked = false;
        boolean isContractSelected = false;
        
        boolean isSelectedAndModifiable = false;//tool selection due to any other reason(eg. top ranked/less than 3 year performance history etc) except above 2 reasons
        
        boolean isSVFCompetingFund = false;
        if(svfCompetingFunds != null && svfCompetingFunds.contains(fundId)){
            isSVFCompetingFund = true;
        }
        boolean isSVFFund= false;
        if(CoreToolGlobalData.defaultSelectedStableValueFunds.contains(fundId)){
            isSVFFund = true;
        }
        
        boolean isBrokerSelected = false;
        boolean isStartFromScratch = sessionContext.isStartFromScratch();
        
       
        boolean isSelectMoneyMarketFunds =  sessionContext.isSelectMarketFundsAsDefault();
        boolean isSelectGIFL =  sessionContext.isIncludeGIFLSelectFunds();
        List<String> lifecycleFundSuites = sessionContext.getLifecycleFundSuites();
        List<String> lifestyleFundSuites = sessionContext.getLifestyleFundSuites();
        
        if (sessionContext.getAdditionalFunds() != null){//during initializing, this is unlikely to have any brokerSelectedFunds
            isBrokerSelected = Arrays.asList(sessionContext.getAdditionalFunds()).contains(fundId);
        }
        if(StringUtils.isNotBlank(sessionContext.getContract())){
            if(contractSelectedFunds.contains(fundId) && !sessionContext.getOldContractSelectedSVFFunds().contains(fundId)){
                isContractSelected = true;
            }
        }
        if (assetClassForInvOption.isSelected() && sessionContext.isSelectAll()) {//this condition is to protect against inconsistent data - where selected asset classes are provided even though option is start from scratch
            
            if (assetClassForInvOption.hasToolRecommendedFund()) {
                isSelectedAndModifiable = fundForInvOption.isToolSelected();//selects top ranked fund if asset class is selected
            }
            
            // we will not preselect any funds for asset class we have not ranked
           /* else {//asset class is selected
                if (!additionalFundsNotToBeSelected.containsKey(fundId)) {
                    // fund is select-able
                    if (AssetClass.hasCoreToolRecommendedFunds(assetClassForInvOption.getId())) {
                        // select all the funds in these classes - AssetClass is part of classes(Multi(3), HYB(5), GUA(3), GLB(1)=12) which do not have CoreTool recommended funds 
                        isSelectedAndModifiable = true;
                    } else if (!fundForInvOption.isBenchmarkMetricsAvailable()) {
                        // fund has < 3 years performance
//                      The fund should have a fund metrics, and an entry in the BenchMark metrics
//                      If insufficient performance history is available for funds, all available funds within that category are included
                        isSelectedAndModifiable = true;
                    }
                }
            }*/
        }
        
        if(StringUtils.isNotBlank(sessionContext.getContract())){//In contract mode, don't select LSF, LCF and FXS funds if contract already has them

            if (!isContractSelected
    					&& lifestyleFundSuites.contains(fundForInvOption.getFund()
    							.getLifeStyleFundFamilyCode()) ) {
    				isSelected = true;
    		}	
		
            if (!isContractSelected
					&& lifecycleFundSuites.contains(fundForInvOption.getFund()
							.getLifeCycleFundFamilyCode())) {
				isSelected = true;
			}
            
            if (!isContractHavingAnyHQSFunds && isSelectMoneyMarketFunds){
                if(CoreToolGlobalData.defaultSelectedMoneyMarketFunds.contains(fundId) ){
                    isSelected = true;
                }
            }
            if (!isContractHavingAnyHQSFunds && !isSelectMoneyMarketFunds){//If not Money Market - it has to be Stable value
                if(CoreToolGlobalData.defaultSelectedStableValueFunds.contains(fundId)){
                    isSelected = true;
                }
            }
        }
        else{
            
        	if (lifecycleFundSuites.contains(fundForInvOption.getFund()
					.getLifeCycleFundFamilyCode())) {
        			isSelected = true;
        	}
            if (lifestyleFundSuites.contains(fundForInvOption.getFund()
							.getLifeStyleFundFamilyCode()) ) {
				isSelected = true;
			}
			if (isSelectGIFL
					&& ASSET_CLASS_ID_GIFL.equals(fundForInvOption.getFund().getAssetClassId())) {
				isSelected = true;
			}
            if (isSelectMoneyMarketFunds){
				if(CoreToolGlobalData.defaultSelectedMoneyMarketFunds.contains(fundId)
						&& sessionContext.getMMFFundList().contains(fundId)){
                    isSelected = true;
                }
            }
            else{//If not Money Market - it has to be Stable value
            	// check if the current is not in SVF competing fund
                if(CoreToolGlobalData.defaultSelectedStableValueFunds.contains(fundId) && !svfCompetingFunds.contains(fundId)){
                    isSelected = true;
                }
            }
        }
        
        if ((isBrokerSelected || isSelected || isContractSelected ||isSelectedAndModifiable) && (!isStartFromScratch)){
            isChecked = true;//This is true only for initial state - subsequently user could select/de-select funds, so checkedFunds list would be updated in SessionContext for display and Pdf purposes
        }
        else if((isBrokerSelected || isSelected || isContractSelected) && (isStartFromScratch)){
            isChecked = true;
        }
        
        fundForInvOption.setSelected(isSelected ? true : false);
        fundForInvOption.setSelectedAndModifiable(isSelectedAndModifiable ? true : false);
        fundForInvOption.setBrokerSelected(isBrokerSelected ? true : false);
        fundForInvOption.setChecked(isChecked ? true : false);
        fundForInvOption.setContractSelected(isContractSelected ? true : false);
        fundForInvOption.setSVFCompetingFund(isSVFCompetingFund ? true : false);
        fundForInvOption.setSVFFund(isSVFFund ? true : false);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> addAttributesToFundForInvOption()");
        }
    }
    
    /**
     * Adds criteria/rank info to Fund object
     */
    @SuppressWarnings("deprecation")//Forced to use @deprecated method getFundClassShortName() in FundClassUtility -because Fund sheet URL implementation uses the class short name i.e. format "C01", "C02" etc
    private void addFundMetricsToFundForInvOption(SessionContext sessionContext,FundForInvOption fundForInvOption, Hashtable<String, FundMetrics> fundLineupFundMetricTable) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> addFundMetricsToFundForInvOption()");
        }
        
        String fundId = fundForInvOption.getFund().getFundId();
        String rateType = fundForInvOption.getRateType();
        FundMetrics fundMetrics = fundLineupFundMetricTable.get(fundId + rateType);
        
        for(Entry<Integer, Integer> entry : sessionContext.getMetricSelectionCriteria().entrySet()) {
        	
        	if(entry.getValue() == 0) {
        		continue;
        	}
        	
        	(fundForInvOption.getCriteriaLongNamesForScreen()).add(SelectionCriteria.longNamesForScreen[entry.getKey()]);
            (fundForInvOption.getCriteriaShortNamesForScreen()).add(SelectionCriteria.shortNamesForScreen[entry.getKey()]);
                 
            if (fundMetrics != null
                    && (fundForInvOption.isBenchmarkMetricsAvailable()
                        || entry.getKey() == SelectionCriteria.FEES && fundMetrics.getValues()[entry.getKey()] != null) && fundMetrics.getPercentileRankings()[entry.getKey()]!=null) {   
                    
                    
                (fundForInvOption.getResultValueCriteria()).add(formatBigDecimalToTwoDigits(fundMetrics.getValues()[entry.getKey()]));
                (fundForInvOption.getResultPercentileCriteria()).add((fundMetrics.getPercentileRankings()[entry.getKey()]).setScale(2, BigDecimal.ROUND_CEILING).toString());
                (fundForInvOption.getResultPercentileConvertedToRankCriteria()).add(convertIndivPercentileToRankForDisplay(fundMetrics.getPercentileRankings()[entry.getKey()]));
                    
                }   else { 
                    (fundForInvOption.getResultValueCriteria()).add(NON_APPLICABLE_STRING_WEB);
                    (fundForInvOption.getResultPercentileCriteria()).add(NON_APPLICABLE_STRING_WEB);
                    (fundForInvOption.getResultPercentileConvertedToRankCriteria()).add(NON_APPLICABLE_STRING_WEB);
                }   
        }
        fundForInvOption.setOverallPercentile(
                fundForInvOption.isBenchmarkMetricsAvailable()
                    ? String.valueOf(fundForInvOption.getRank())
                    : NON_APPLICABLE_STRING_WEB);
        fundForInvOption.setOverallDisplayRank(
                fundForInvOption.isBenchmarkMetricsAvailable()
                    ? String.valueOf(fundForInvOption.getDisplayRank())
                    : NON_APPLICABLE_STRING_WEB);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> addFundMetricsToFundForInvOption()");
        }
      //This is for purpose of Fund Sheet URLs on page. URL links by fund id and class short name
        String assetClassId = fundForInvOption.getFund().getAssetClassId();
        if(fundMetrics != null){//for all asset classes except guaranteed
            if( !(assetClassId.equals(ReportDataModel.ASSET_CLASS_STYLE_ID_YEAR_3_GUARANTEED) ||  assetClassId.equals(ReportDataModel.ASSET_CLASS_STYLE_ID_YEAR_5_GUARANTEED) || assetClassId.equals(ReportDataModel.ASSET_CLASS_STYLE_ID_YEAR_10_GUARANTEED))){
                fundForInvOption.setClassShortName(fundMetrics.getFundClassShortName());
            }
        }
        else if(assetClassId.equals(ReportDataModel.ASSET_CLASS_STYLE_ID_YEAR_3_GUARANTEED) ||  assetClassId.equals(ReportDataModel.ASSET_CLASS_STYLE_ID_YEAR_5_GUARANTEED) || assetClassId.equals(ReportDataModel.ASSET_CLASS_STYLE_ID_YEAR_10_GUARANTEED)){//for Guaranteed funds- use rate type selected by user on Step 1 or use Contract base class rate type
            if(StringUtils.isNotBlank(sessionContext.getContract())){
                fundForInvOption.setClassShortName(FundClassUtility.getInstance().getFundClassShortName(sessionContext.getContractBaseClass()));
            }
            else{
                fundForInvOption.setClassShortName(FundClassUtility.getInstance().getFundClassShortName(sessionContext.getClassMenu()));
            }
        }
    }
    
    private String convertIndivPercentileToRankForDisplay(BigDecimal percentileValue){
        String displayRank = new CoreToolHelper().convertPercentileToRankForDisplay(percentileValue);
        return displayRank;
    }
    
    private String formatBigDecimalToTwoDigits(BigDecimal value) {
        String result = "";
        result = value.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        return result;
    }
    
    /**
     * This method returns string array of all checked fund IDs (includes Tool selected, Broker selected, Contract selected)
     * 
     * @param ArrayList -Investment option array list
     * @return - String[] List of checked fund IDs
     */
    public String[] getUpdatedFundsSelectionStatus(ArrayList<AssetClassForInvOption>assetClassForInvOptionList){
        
        ArrayList<String> selectedFundArrayList = new ArrayList<String>();
        ArrayList<String> totalAvailableFundsArrayList = new ArrayList<String>();
        Iterator<AssetClassForInvOption> it = assetClassForInvOptionList.iterator();
        while(it.hasNext()){   
            AssetClassForInvOption  assetClassForInvOption= it.next();
            ArrayList<FundForInvOption> fundForInvOptionList = assetClassForInvOption.getFundForInvOptionList();
            
            Iterator<FundForInvOption> itr = fundForInvOptionList.iterator();
            while(itr.hasNext()){   
                FundForInvOption  fundForInvOption= itr.next();
                String fundId = fundForInvOption.getFund().getFundId();
                totalAvailableFundsArrayList.add(fundId);
                
                if (fundForInvOption.isChecked()){
                    fundId = fundForInvOption.getFund().getFundId();
                    selectedFundArrayList.add(fundId);
                }
            }
        }
        String latestSelectedFunds[] = selectedFundArrayList.toArray(new String[0]);
        return latestSelectedFunds;
    }
    
    /**
     * This method returns string array of all Tool selected fund IDs
     * 
     * @param ArrayList -Investment option array list
     * @return - String[] List of tool selected fund IDs
     */
    public String[] getToolSelectedFundsStatus(ArrayList<AssetClassForInvOption>assetClassForInvOptionList){
        
        ArrayList<String> toolSelectedFundArrayList = new ArrayList<String>();
        Iterator<AssetClassForInvOption> it = assetClassForInvOptionList.iterator();
        while(it.hasNext()){   
            AssetClassForInvOption  assetClassForInvOption= it.next();
            ArrayList<FundForInvOption> fundForInvOptionList = assetClassForInvOption.getFundForInvOptionList();
            
            Iterator<FundForInvOption> itr = fundForInvOptionList.iterator();
            while(itr.hasNext()){   
                FundForInvOption  fundForInvOption= itr.next();
                String fundId = fundForInvOption.getFund().getFundId();
                
                if (fundForInvOption.isSelected() || fundForInvOption.isSelectedAndModifiable()){
                    fundId = fundForInvOption.getFund().getFundId();
                    toolSelectedFundArrayList.add(fundId);
                }
            }
        }
        String toolSelectedFunds[] = toolSelectedFundArrayList.toArray(new String[0]);
        return toolSelectedFunds;
    }
    
    /**
     * This method returns string array of all Tool selected fund IDs
     * 
     * @param ArrayList -Investment option array list
     * @return - String[] List of tool selected fund IDs
     */
    public String[] getToolRecommendedFunds(ArrayList<AssetClassForInvOption>assetClassForInvOptionList){
        
        ArrayList<String> toolRecommendedFunds = new ArrayList<String>();
        Iterator<AssetClassForInvOption> it = assetClassForInvOptionList.iterator();
        while(it.hasNext()){   
            AssetClassForInvOption  assetClassForInvOption= it.next();
            ArrayList<FundForInvOption> fundForInvOptionList = assetClassForInvOption.getFundForInvOptionList();
            
            Iterator<FundForInvOption> itr = fundForInvOptionList.iterator();
            while(itr.hasNext()){   
                FundForInvOption  fundForInvOption= itr.next();
                String fundId = fundForInvOption.getFund().getFundId();
                
                if (fundForInvOption.isToolSelected()){
                    fundId = fundForInvOption.getFund().getFundId();
                    toolRecommendedFunds.add(fundId);
                }
            }
        }
        String toolRecommendedFundsArray[] = toolRecommendedFunds.toArray(new String[0]);
        return toolRecommendedFundsArray;
    }
    
    /**
     * This method returns string array of all Contract selected fund IDs
     * 
     * @param ArrayList -Investment option array list
     * @return - String[] List of contract selected fund IDs
     */
    public String[] getContractFundsSelectionStatus(ArrayList<AssetClassForInvOption>assetClassForInvOptionList){
        
        ArrayList<String> selectedFundArrayList = new ArrayList<String>();
        Iterator<AssetClassForInvOption> it = assetClassForInvOptionList.iterator();
        while(it.hasNext()){   
            AssetClassForInvOption  assetClassForInvOption= it.next();
            ArrayList<FundForInvOption> fundForInvOptionList = assetClassForInvOption.getFundForInvOptionList();
            
            Iterator<FundForInvOption> itr = fundForInvOptionList.iterator();
            while(itr.hasNext()){   
                FundForInvOption  fundForInvOption= itr.next();
                String fundId = fundForInvOption.getFund().getFundId();
                if (fundForInvOption.isContractSelected()){
                    fundId = fundForInvOption.getFund().getFundId();
                    selectedFundArrayList.add(fundId);
                }
            }
        }
        String latestSelectedFunds[] = selectedFundArrayList.toArray(new String[0]);
        return latestSelectedFunds;
    }
    
    /**
     * This method returns string array of all Broker selected fund IDs
     * 
     * @param ArrayList -Investment option array list
     * @return - String[] List of broker selected fund IDs
     */
    public String[] getBrokerFundsSelectionStatus(ArrayList<AssetClassForInvOption>assetClassForInvOptionList){
        
        ArrayList<String> selectedFundArrayList = new ArrayList<String>();
        Iterator<AssetClassForInvOption> it = assetClassForInvOptionList.iterator();
        while(it.hasNext()){   
            AssetClassForInvOption  assetClassForInvOption= it.next();
            ArrayList<FundForInvOption> fundForInvOptionList = assetClassForInvOption.getFundForInvOptionList();
            Iterator<FundForInvOption> itr = fundForInvOptionList.iterator();
            while(itr.hasNext()){   
                FundForInvOption  fundForInvOption= itr.next();
                String fundId = fundForInvOption.getFund().getFundId();
                if (fundForInvOption.isBrokerSelected()){
                    fundId = fundForInvOption.getFund().getFundId();
                    selectedFundArrayList.add(fundId);
                }
            }
        }
        String brokerSelectedFunds[] = selectedFundArrayList.toArray(new String[0]);
        return brokerSelectedFunds;
    }
    
    /**
     * This method returns Map -keys as lifeStyleFundFamilyCode and values as lifeStyleFundNames 
     * 
     * @param ArrayList -Investment option array list
     * @return - Map<String,ArrayList<String>>  
     */
    public Map<String,ArrayList<String>> getLifeStyleFunds(ArrayList<AssetClassForInvOption>assetClassForInvOptionList){

    	Map<String,ArrayList<String>> lifestyleFundMap = new LinkedHashMap<String,ArrayList<String>>();
    	ArrayList<String> lifestyleFundNameList = null;

    	for(AssetClassForInvOption assetClassForInvOption :assetClassForInvOptionList){   
    		ArrayList<FundForInvOption> fundForInvOptionList = assetClassForInvOption.getFundForInvOptionList();
    		for(FundForInvOption fundForInvOption:fundForInvOptionList){ 
    			if(fundForInvOption.getFund() != null && StringUtils.isNotBlank(fundForInvOption.getFund().getAssetClassId()) 
    					&& ASSET_CLASS_ID_LIFESTYLE.equalsIgnoreCase(fundForInvOption.getFund().getAssetClassId())){
    				String fundFamilyCode = fundForInvOption.getFund().getLifeStyleFundFamilyCode();
    				if(StringUtils.isNotBlank(fundFamilyCode)){
    					if(lifestyleFundMap.size() >=0 && lifestyleFundMap.get(fundFamilyCode)== null){
    						lifestyleFundNameList = new ArrayList <String>();
    						lifestyleFundMap.put(fundFamilyCode, lifestyleFundNameList);
    					}else if(lifestyleFundMap.size() > 0 && lifestyleFundMap.get(fundFamilyCode)!= null){
    						lifestyleFundNameList = lifestyleFundMap.get(fundFamilyCode);
    					}
    					if(lifestyleFundNameList != null){
    						lifestyleFundNameList.add(fundForInvOption.getFund().getFundLongName());
    					}
    				 }
    			 }    
    		}
    	}

    	return lifestyleFundMap;
    }
    
    /**
     * 
     * @return CoreToolSessionData object
     */
    public CoreToolSessionData getCoreToolSessionData(){
        return coreToolSessionData;
    }
    
    /**
     * This method returns XML string to be transformed using XSL FO to generate Pdf
     * 
     * @param SessionContext object - contains all user input & base fund line up details
     * @return - Document (XML)
     * @throws SystemException
     * @throws ParserConfigurationException
     */    
    public Document getPDFXmlDocument(SessionContext sessionContext) throws ParserConfigurationException, SystemException{
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getPDFXml()");
        }
        
        PDFDocument doc = new PDFDocument();
        ReportDataProcessor reportDataProcessor = new ReportDataProcessor();
        
        ReportDataModel dataModel = new ReportDataModel(new ReportInputData(sessionContext));
        
        Element root = doc.createRootElement("iEvaluatorReport");
        
        CoreToolHelper coreToolHelper = new CoreToolHelper();
        
        Hashtable<Integer, InvestmentGroup> invCat = coreToolHelper.getInvestmentGroupTable(sessionContext.isIncludeGIFLSelectFunds());
        
        reportDataProcessor.addFundLineupDetails(dataModel, doc, root);
        
        reportDataProcessor.addReportLayout(dataModel, doc, root, sessionContext);
        
        reportDataProcessor.addEndnotes(dataModel, doc, root);
        
        reportDataProcessor.addCriteriaSelections(dataModel, doc, root);

        String contractId = sessionContext.getContract();
        String rateType = sessionContext.getClassMenu();
        reportDataProcessor.addDynamicPerformanceDisclaimer(doc, root, contractId, rateType);
        
        reportDataProcessor.addFundsByAssetClass(dataModel, doc, root);
        
        reportDataProcessor.addFundsByInvestmentCategory(dataModel, doc, root, invCat);
        
//      call below method for one consolidated fund list
       
        String avgExpRatioMethod = sessionContext.getAverageExpenceRatioMethod();
        reportDataProcessor.addCommonFundDetails(dataModel, doc, root, invCat, investmentIdsPlusRateType, contractId, rateType, avgExpRatioMethod);
        
        reportDataProcessor.addCustomizationDetails(dataModel, doc, root, invCat);//moving this method down so that it can have selected asset class information
        
        return doc.getDocument();   
    }
}
