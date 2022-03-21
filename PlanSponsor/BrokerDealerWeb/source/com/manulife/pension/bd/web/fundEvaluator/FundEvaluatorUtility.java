package com.manulife.pension.bd.web.fundEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.fundEvaluator.common.AssetClassForInvOption;
import com.manulife.pension.bd.web.fundEvaluator.common.CoreToolConstants;
import com.manulife.pension.bd.web.fundEvaluator.common.FundForInvOption;
import com.manulife.pension.bd.web.fundEvaluator.processor.CoreToolProcessor;
import com.manulife.pension.bd.web.fundEvaluator.session.SessionContext;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.FundClassUtility;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.fund.coretool.model.common.SelectionCriteria;
import com.manulife.pension.service.fund.valueobject.FundClassVO;
import com.manulife.pension.service.fund.valueobject.FundFamilyVO;

public class FundEvaluatorUtility {

    /**
     * Returns collection of fund menus for drop down in label value pair.
     * 
     * @return ArrayList<LabelValueBean>
     */
    public static ArrayList getFundMenuList() {
        ArrayList fundMenuList = new ArrayList<LabelValueBean>();

        fundMenuList.add(new LabelValueBean(FundEvaluatorConstants.FUND_MENU_LABEL_ALL_FUNDS,
                FundEvaluatorConstants.FUND_MENU_VALUE_ALL_FUNDS));
        fundMenuList.add(new LabelValueBean(FundEvaluatorConstants.FUND_MENU_LABEL_RETAIL_FUNDS,
                FundEvaluatorConstants.FUND_MENU_VALUE_RETAIL_FUNDS));
        fundMenuList.add(new LabelValueBean(FundEvaluatorConstants.FUND_MENU_LABEL_SUBADVISED_FUNDS,
                FundEvaluatorConstants.FUND_MENU_VALUE_SUBADVISED_FUNDS));

        return fundMenuList;
    }
    
    /**
     * Returns fund class list
     * @param fundClassesMap
     * @return
     * @throws SystemException
     */
    public static ArrayList getFundClasses(Map<String, FundClassVO> fundClassesMap)
            throws SystemException {
        ArrayList fundClassesList = new ArrayList<LabelValueBean>();

        Collection<FundClassVO> fundClasses = fundClassesMap.values();
        for (FundClassVO fundClass : fundClasses) {
        	if(fundClass.isSpecialRateType()==Boolean.TRUE)
        		continue;
    		fundClassesList.add(new LabelValueBean(FundClassUtility.getInstance()
                .getFundClassName(fundClass.getFundClassId()), fundClass.getFundClassId()));
        }

        return fundClassesList;

    }

    /**
     * Returns states list
     * @param ArrayList
     * @return
     * @throws SystemException
     */
    public static ArrayList<LabelValueBean> getStatesMenuList(Collection<DeCodeVO> statesCollection)
            throws SystemException {
        
        ArrayList<LabelValueBean> statesList = new ArrayList<LabelValueBean>();

        // the first on the list is blank
        statesList.add(new LabelValueBean("", ""));

        // exclude NY state
        for (DeCodeVO state : statesCollection) {
            if (!state.getCode().equals(FundEvaluatorConstants.NY_STATE)) {
                statesList.add(new LabelValueBean(state.getCode(), state.getCode()));
            }
        }
        return statesList;
    }

   /**
    * Initialize the session context object with step1,2 & 3 page input for the base line funds. 
    * @param sessionContext
    * @param ievaluatorForm
    * @param request
    */
    public  static void setSessionContextForWebPages(SessionContext sessionContext, FundEvaluatorForm ievaluatorForm, HttpServletRequest request) {
       
        String checkedFunds[] = getCheckedFunds(request);
        sessionContext.setCheckedFunds(checkedFunds);
        
        //Step 1 - Select Your Client
        sessionContext.setClassMenu(ievaluatorForm.getFundClassSelected());
        sessionContext.setFundMenu(ievaluatorForm.getFundMenuSelected());
        sessionContext.setSiteId(ievaluatorForm.getFundUsa());
        
        sessionContext.setStateCode(ievaluatorForm.getStateSelected());
        sessionContext.setIncludeNML(ievaluatorForm.isNml());
        sessionContext.setExcludeEDJ(ievaluatorForm.isEdwardJones());
        sessionContext.setMerrillFirmFilter(ievaluatorForm.isMerrillFirmFilter());
       
        sessionContext.setContract(ievaluatorForm.getContractNumber());
        sessionContext.setContractBaseClass(ievaluatorForm.getContractBaseClass());
        sessionContext.setContractLocationId(ievaluatorForm.getContractLocationId());
        //ContractBaseFundPackageSeries - is not required for web page

        if(ievaluatorForm.isNewPlanClosedFund() || ievaluatorForm.isExistingClientClosedFund()) {
            sessionContext.setIncludeClosedFunds(true);
        } else {
            sessionContext.setIncludeClosedFunds(false);
        }
        
        sessionContext.setIncludeGIFLSelectFunds(ievaluatorForm.isIncludeGIFLSelectFunds());
        
        //Step 2 - Select Criteria
        LinkedHashMap<Integer, Integer> metricSelectionCriteria =  getMetricSelectionCriterias(ievaluatorForm);
        sessionContext.setMetricSelectionCriteria(metricSelectionCriteria);
        //color info is not required for web page
        
        
        //Step 3 - Narrow your list
        if("1".equalsIgnoreCase(ievaluatorForm.getPreSelectFunds())) {
            sessionContext.setSelectAll(true);
        } else if("2".equalsIgnoreCase(ievaluatorForm.getPreSelectFunds())) {
            sessionContext.setStartFromScratch(true);
        } 
        
        sessionContext.setSelectLifeStyle(ievaluatorForm.isLifeStylePortfolios());
        sessionContext.setSelectLifeCycle(ievaluatorForm.isLifeCyclePortfolios());
        if(ievaluatorForm.getLifecycleFundSuites() != null){
        	sessionContext.setLifecycleFundSuites(Arrays.asList(ievaluatorForm.getLifecycleFundSuites()));
        }
        
        if(ievaluatorForm.getLifestyleFundSuites() != null){
        	sessionContext.setLifestyleFundSuites(Arrays.asList(ievaluatorForm.getLifestyleFundSuites()));
        }
              
        
        if(FundEvaluatorConstants.COMPULSORY_FUND_SVF.equalsIgnoreCase(ievaluatorForm.getCompulsoryFunds())) {
            sessionContext.setSelectMarketFundsAsDefault(false);//If MMF is false, tool will select SVF automatically
            if(StringUtils.isNotEmpty(ievaluatorForm.getStableValueFunds())){
            	sessionContext.setSVFFundList(ievaluatorForm.getStableValueFunds());
            	List<String> svfFund = new ArrayList<String>(); 
            	svfFund.addAll(ievaluatorForm.getSvfFundCodeList());
            	svfFund.remove(ievaluatorForm.getStableValueFunds());
            	sessionContext.setOldContractSelectedSVFFunds(svfFund);
            }
        } else if (FundEvaluatorConstants.COMPULSORY_FUND_MMF.equalsIgnoreCase(ievaluatorForm.getCompulsoryFunds())) {
            sessionContext.setSelectMarketFundsAsDefault(true);
            if(ievaluatorForm.getMoneyMarketFunds() != null){
            	sessionContext.setMMFFundList(Arrays.asList(ievaluatorForm.getMoneyMarketFunds()));
            }
        }       
        
    }
    
    public  static void setSessionContextForReport(SessionContext sessionContext, FundEvaluatorForm ievaluatorForm, HttpServletRequest request) {
        
        
        // Client Info page.
        sessionContext.setClassMenu(ievaluatorForm.getFundClassSelected());
        sessionContext.setFundMenu(ievaluatorForm.getFundMenuSelected());
        sessionContext.setSiteId(ievaluatorForm.getFundUsa());
        
        sessionContext.setContract(ievaluatorForm.getContractNumber());
        sessionContext.setContractLocationId(ievaluatorForm.getContractLocationId());
        sessionContext.setContractBaseClass(ievaluatorForm.getContractBaseClass());
        sessionContext.setContractBaseFundPackageSeries(ievaluatorForm.getContractBaseFundPackageSeries());

        sessionContext.setStateCode(ievaluatorForm.getStateSelected());  
        sessionContext.setIncludeNML(ievaluatorForm.isNml());//required as determines base fund line up
        sessionContext.setExcludeEDJ(ievaluatorForm.isEdwardJones());
        sessionContext.setMerrillFirmFilter(ievaluatorForm.isMerrillFirmFilter());
        
        sessionContext.setSelectAll("1".equalsIgnoreCase(ievaluatorForm.getPreSelectFunds()));
        
        sessionContext.setBrokerFirmSmithBarneyAssociated(ievaluatorForm.isSmithBarneyAssociated());

        if(ievaluatorForm.isNewPlanClosedFund() || ievaluatorForm.isExistingClientClosedFund()) {//required as determines base fund line up
            sessionContext.setIncludeClosedFunds(true);
        } else {
            sessionContext.setIncludeClosedFunds(false);
        }
        
        sessionContext.setIncludeGIFLSelectFunds(ievaluatorForm.isIncludeGIFLSelectFunds());
        
        //Step 2
        LinkedHashMap<Integer, Integer> metricSelectionCriteria =  getMetricSelectionCriterias(ievaluatorForm);
        sessionContext.setMetricSelectionCriteria(metricSelectionCriteria);
        
        String colors = ievaluatorForm.getPieChartcolors();
        StringTokenizer st = new StringTokenizer(colors,",");
        ArrayList<String> colorArray = new ArrayList<String>();
         while (st.hasMoreTokens()) {
        	String color = st.nextToken();
          	if(StringUtils.isBlank(color)) {
         		continue;
        	}
            colorArray.add(color);
        }                              
        String[] colorStrArray = new String[0];
        colorStrArray = colorArray.toArray(new String[0]);
        sessionContext.setColorForCriteria(colorStrArray);
        
        //Step 3
        if(ievaluatorForm.getFundFamilyList() != null){
        	// Here lifecyleFundNamesList contains list of lifeCycle fundSuiteNames.
        	ArrayList<String> lifecycleFundSuiteNames = getLifecyleFundSuiteNames(ievaluatorForm.getFundFamilyList());
        	sessionContext.setLifecycleFundSuiteNames(lifecycleFundSuiteNames);
        }
        if(ievaluatorForm.getLifestyleFamilyList() != null){
        	// Here lifestyleFundMap contains list of lifeStyle fundFamilyCodeAndFundSuiteNames.
        	Map<String, String> lifestyleFundsDetails = getLifestyleFundDetails(ievaluatorForm.getLifestyleFamilyList());
        	sessionContext.setLifestyleFundsDetails(lifestyleFundsDetails);
        }
		
        //Step 4
        String checkedFunds[] = getCheckedFunds(request);
        sessionContext.setCheckedFunds(checkedFunds);
        
        //Step 5
        setStep5Info(sessionContext, ievaluatorForm);
    }

    private static void setStep5Info(SessionContext sessionContext, FundEvaluatorForm ievaluatorForm){
        //Step 5
        sessionContext.setPreparedForCompanyName(ievaluatorForm.getCompanyName());
        sessionContext.setPresenterName(ievaluatorForm.getYourName());
        sessionContext.setPresenterFirmName(ievaluatorForm.getYourFirm());
        
        sessionContext.setCoverSheetImageType(ievaluatorForm.getYourCoverSheet());
        
        
        String[] optionalSectionIds = new String[0];
        ArrayList<String> optionalSections = new ArrayList<String>();
        String performanceTableFundListing = ievaluatorForm.getFundListRiskOrderOrAssetClass();
        if(StringUtils.isNotEmpty(performanceTableFundListing)){//default selected - hence this check may not be required
            if(StringUtils.equalsIgnoreCase(performanceTableFundListing, "assetclass")){
                optionalSections.add(FundEvaluatorConstants.PERFORMANCE_BY_ASSET_CLASS_SECTION);
            }
            else{
                optionalSections.add(FundEvaluatorConstants.PERFORMANCE_BY_RISK_CATEGORY_SECTION);
            }
        }
        String averageExpenceRatioMethod = ievaluatorForm.getAverageExpenceRatioMethod();
        if(StringUtils.isNotEmpty(averageExpenceRatioMethod)){
            if(StringUtils.equalsIgnoreCase(averageExpenceRatioMethod, "simpleAverage")){
            	sessionContext.setAverageExpenceRatioMethod(FundEvaluatorConstants.SIMPLE_AVERAGE);
            }else if(StringUtils.equalsIgnoreCase(averageExpenceRatioMethod, "weightedAverage")){
                sessionContext.setAverageExpenceRatioMethod(FundEvaluatorConstants.WEIGHTED_AVERAGE);
            }else{
            	sessionContext.setAverageExpenceRatioMethod(FundEvaluatorConstants.SIMPLE_AND_WEIGHTED_AVERAGES);
            }
        }
        
        if(StringUtils.isNotEmpty(ievaluatorForm.getIncludedOptItem3())){
            optionalSections.add(FundEvaluatorConstants.DOCUMENTING_DUE_DILIGENCE_SECTION);
            optionalSections.add(FundEvaluatorConstants.IPS_SECTION);
        }
        if(StringUtils.isNotEmpty(ievaluatorForm.getIncludedOptItem4())){
            optionalSections.add(FundEvaluatorConstants.RANKING_METHODOLOGY_SECTION);
            String rankingMethodology = ievaluatorForm.getFundRankingMethodology();
            if(StringUtils.equalsIgnoreCase(rankingMethodology, "selected")){
                optionalSections.add(FundEvaluatorConstants.FUND_RANKING_SELECTED_FUNDS_SECTION);
            }
            else{
                optionalSections.add(FundEvaluatorConstants.FUND_RANKING_ALL_AVAILABLE_FUNDS_SECTION);
            }
        }
        if(StringUtils.isNotEmpty(ievaluatorForm.getIncludedOptItem5())){
            optionalSections.add(FundEvaluatorConstants.GLOSSARY_SECTION);
        }
        if(StringUtils.isNotEmpty(ievaluatorForm.getIncludedOptItem6())){
            optionalSections.add(FundEvaluatorConstants.INV_SELECTION_FORM_SECTION);
        }
        //GIFL P3c
        // Optional item to include GIFL Select Information Sheet.
        if(StringUtils.isNotEmpty(ievaluatorForm.getIncludedOptItem7())){
            optionalSections.add(FundEvaluatorConstants.GIFL_SELECT_INFORMATION_SECTION);
        }
        optionalSectionIds = optionalSections.toArray(new String[0]);
        sessionContext.setOptionalSectionIds(optionalSectionIds);
        
    }
    
    /**
     * Returns user selected fund id's
     * @param request
     * @return String[] - Fund id's array
     */
    public static String[] getCheckedFunds(HttpServletRequest request) {
        
        String[] checkedFunds = {};        
        Hashtable<String, Collection<String>> userSelectedFunds = FundEvaluatorActionHelper.getUserSelectedAssetClsFundsFromSession(request, false);
        Collection<String> checkedFundsCollection = new ArrayList<String>();
        if(userSelectedFunds != null && userSelectedFunds.size() > 0) {
            String[] assetClassIds = {};
            
            assetClassIds = userSelectedFunds.keySet().toArray(assetClassIds);
            for(String assetClsId :assetClassIds) {
                Collection<String> fundIdList = userSelectedFunds.get(assetClsId);
                String[] fundIds = {};
                if(fundIdList != null && fundIdList.size() > 0) {
                    checkedFundsCollection.addAll(fundIdList);
                }
            }
        }
        if(checkedFundsCollection.size() > 0) {
            checkedFunds = checkedFundsCollection.toArray(checkedFunds);
        }
        
        return checkedFunds;
    }
    
    /**
     * Sets metric selection criterias.
     * @param metricSelectionCriteria
     * @param ievaluatorForm
     */
    private static LinkedHashMap<Integer, Integer> getMetricSelectionCriterias(FundEvaluatorForm ievaluatorForm) {
        List<CriteriaVO> criteriaList = ievaluatorForm.getTheItemList();
        LinkedHashMap<Integer, Integer> metricSelectionCriteria = new LinkedHashMap<Integer, Integer>(SelectionCriteria.TOTAL);
        
        if(criteriaList != null && criteriaList.size() > 0) {
            for(CriteriaVO criteria: criteriaList) {
                if(criteria.getCriteriaName().equalsIgnoreCase(criteria.getCriteriaSelected())) {
                    metricSelectionCriteria.put(criteria.getSelectCriteriaIndex(), Integer.parseInt(criteria.getCriteriaValue()));
                }
            }
        }
        
        return metricSelectionCriteria;
    }
    
    /**
     * Returns criteria list
     * @return ArrayList<CriteriaVO>
     */
    public static ArrayList<CriteriaVO> createCriteriaInfo() {
        ArrayList<CriteriaVO> criteriaInfo = new ArrayList<CriteriaVO>();

        criteriaInfo.add(new CriteriaVO("3-year performance", "3 Year Return", "Description for Criteria1", true, SelectionCriteria.TOTAL_RETURN));
        criteriaInfo.add(new CriteriaVO("5-year performance", "5 Year Return", "Description for Criteria1", true, SelectionCriteria.TOTAL_RETURN_5YEAR));
        criteriaInfo.add(new CriteriaVO("10-year performance", "10 Year Return", "Description for Criteria1", true, SelectionCriteria.TOTAL_RETURN_10YEAR));
        criteriaInfo.add(new CriteriaVO("Actual vs. expected performance", "Alpha", "Description for Criteria2", true, SelectionCriteria.ALPHA));
        criteriaInfo.add(new CriteriaVO("Risk-adjusted performance<br/>(using T-bill return)", "Sharpe Ratio","Description for Criteria3", false, SelectionCriteria.SHARPE_RATIO));
        criteriaInfo.add(new CriteriaVO("Risk-adjusted performance<br/>(using benchmark return)", "Information Ratio","Description for Criteria4", false, SelectionCriteria.INFORMATION_RATIO));
        criteriaInfo.add(new CriteriaVO("Correspondence to stated investment style", "R-Squared", "Description for Criteria5", true, SelectionCriteria.R2));
        criteriaInfo.add(new CriteriaVO("Performance in up markets<br/>(relative to the benchmark)","Upside Capture", "Description for Criteria6", false, SelectionCriteria.UPSIDE_CAPTURE));
        criteriaInfo.add(new CriteriaVO("Performance in down markets<br/>(relative to the benchmark)", "Downside Capture", "Description for Criteria7", false, SelectionCriteria.DOWNSIDE_CAPTURE));
        criteriaInfo.add(new CriteriaVO("Portfolio risk", "Standard Deviation", "Description for Criteria8", true, SelectionCriteria.STANDARD_DEVIATION));
        criteriaInfo.add(new CriteriaVO("Systematic risk", "Beta", "Description for Criteria8", false, SelectionCriteria.BETA));
        criteriaInfo.add(new CriteriaVO("Fund level fees", "Expense Ratio", "Description for Criteria8", true, SelectionCriteria.FEES));
        return criteriaInfo;

    }
  
    /**
     * Retrieves the base line investment options based on the user selected values from step1, 2 & 3
     * @param ievaluatorForm
     * @param request
     * @return
     * @throws SystemException
     */
    public static ArrayList<AssetClassForInvOption> getInvestmentOptionDetails(FundEvaluatorForm ievaluatorForm, HttpServletRequest request) throws SystemException {
        ArrayList<AssetClassForInvOption> assetClassForInvOptionList = new ArrayList<AssetClassForInvOption>();
        CoreToolProcessor processor = new CoreToolProcessor();
        SessionContext sessionContext = new SessionContext();
        
        FundEvaluatorUtility.setSessionContextForWebPages(sessionContext, ievaluatorForm, request);
        String order = "";
        if (FundEvaluatorConstants.SORT_ASCENDING.equalsIgnoreCase(ievaluatorForm.getRankingOrder())) {
            order = CoreToolConstants.DEFAULT_ORDER;
        } else {
            order = CoreToolConstants.REVERSE_ORDER;
        }
        assetClassForInvOptionList = processor.populateInvestmentOptionVOs(sessionContext, order);        
        
        return assetClassForInvOptionList;
    }
    
    /**
     * Return asset class details value object.
     * @return HashMap<String, AssetClassDetails>
     */
    public static HashMap<String, AssetClassDetails> initializeAssetClassDetails(ArrayList<AssetClassForInvOption> assetClasses) {
        HashMap<String, AssetClassDetails> assetClassDetailsMap = new HashMap<String, AssetClassDetails>();
        
        for(AssetClassForInvOption assetClass : assetClasses) {
            assetClassDetailsMap.put(assetClass.getId(), new AssetClassDetails(assetClass.getId(), assetClass.getDescription(), getTotalAssetClassSelectedFunds(assetClass), assetClass.getFundForInvOptionList().size()));
        }
        
        return assetClassDetailsMap;
    }
    
    
    /**
     * Returns the number of funds selected in asset class.
     * @param assetClass
     * @return int - total selected funds count.
     */
    public static int getTotalAssetClassSelectedFunds(AssetClassForInvOption assetClass) {
        int totalAssetClassSelectedFunds = 0;
        
        Collection<FundForInvOption> fundsList = assetClass.getFundForInvOptionList();
        
        for(FundForInvOption fund: fundsList) {
            if(fund.isChecked()) {
                totalAssetClassSelectedFunds++;
            }
        }
        
        return totalAssetClassSelectedFunds;
    }
    
    /**
     * Returns lifeCyleFundSuiteNames 
     * 
     * @param lifecycleFundList
     * @return ArrayList<String>.
     */
    public static ArrayList<String> getLifecyleFundSuiteNames(ArrayList<FundFamilyVO> lifecyleFundSuiteNames) {
        
    	ArrayList<String> lifecyleFundNamesList = new ArrayList<String>();
     	
     	for(FundFamilyVO fundFamilyVO : lifecyleFundSuiteNames){
 			lifecyleFundNamesList.add(fundFamilyVO.getFamilyDescription());
 		}
    	return lifecyleFundNamesList;
    }
    
    /**
     * Returns lifeStyleFundFamilyCodesAndFundSuiteNames.
     * 
     * @param lifestyleFundList
     * @return Map<String, String>.
     */
    public static Map<String, String> getLifestyleFundDetails(ArrayList<FundFamilyVO> lifestyleFundList) {
    	
        Map<String, String> lifestyleFundMap = new LinkedHashMap<String, String>();
    	
    	for(FundFamilyVO fundFamilyVO : lifestyleFundList){
    		lifestyleFundMap.put(fundFamilyVO.getFamilyCode(), fundFamilyVO.getFamilyDescription());
		}
    	
        return lifestyleFundMap;
    }
    
}