package com.manulife.pension.bd.web.fundEvaluator.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.fundEvaluator.processor.CoreToolSessionData;
import com.manulife.pension.service.fund.coretool.model.common.SelectionCriteria;

/**
 * This object contains data from UI /wizard entered by user
 * and will be updated with base fund line up data, tool, contract and broker 
 * fund selections for report generation. 
 * 
 * General notes :
 * Information entered by user on UI is retained in form.
 * selected fund list is retained in session
 * Other derived information (CoreToolSessionData object) is not retained, but fetched from cache whenever required
 * 
 * @author PWakode
 */
public class SessionContext implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    //variables related to Wizard Step 1
	private String siteId = null;

	private String fundMenu= null;
	private String classMenu= null;
	private boolean includeClosedFunds = false;
	
	private boolean includeNML = false;
	
	private String contract = null;
	private String contractLocationId = null;
    private String contractBaseClass = null;
    private String contractBaseFundPackageSeries = null;
    
    private boolean isBrokerFirmSmithBarneyAssociated = false;
    
    // variables added for ISP for step 1
    private String stateCode = null;
    private boolean excludeEDJ = false;
    private boolean merrillFirmFilter = false;
    
    
    //variables related to Wizard Step 2
    private boolean selectAll = false;
    private boolean startFromScratch = false;
    
    private boolean selectLifeStyle = false;
    private boolean selectLifeCycle = false;
    private List<String> lifecycleFundSuites = new ArrayList<String>();
    private List<String> lifestyleFundSuites = new ArrayList<String>();
    
    private ArrayList<String> lifecycleFundSuiteNames = new ArrayList<String>();
    private Map<String, String> lifestyleFundsDetails = new LinkedHashMap<String, String>();
    
    private Map<String,ArrayList<String>> lifeStyleFunds = new LinkedHashMap<String,ArrayList<String>>();
    
	private boolean selectMarketFundsAsDefault = false;
	
	private String sVFFundList = null;
	private List<String> mMFFundList = new ArrayList<String>();
	
	private List<String> oldContractSelectedSVFFunds = new ArrayList<String>();
	
	//variables related to Wizard Step 3
	private LinkedHashMap<Integer, Integer> metricSelectionCriteria = new LinkedHashMap<Integer, Integer>(SelectionCriteria.TOTAL);
	
	private String[] colorForCriteria = new String[SelectionCriteria.TOTAL-1];
	
	//variables related to Wizard Step 4
	private CoreToolSessionData coreToolData;

	private String[] additionalFunds = new String[0];
    
    private String[] toolRecommendedFunds = new String[0];
    
    private String[] checkedFunds = new String[0];
    
    private String[] contractFunds = new String[0];

	//variables related to Wizard Step 5
	private String presenterName = null;
    private String presenterFirmName = null;
    private String preparedForCompanyName = null;
    private String coverSheetImageType = "standard";
    
    private String[] optionalSectionIds = new String[7];    
    
	private boolean includeGIFLSelectFunds = false;
	
	private String averageExpenceRatioMethod = null;
  
    /**
     * @return the coverSheetImageType
     */
    public String getCoverSheetImageType() {
        return coverSheetImageType;
    }
    
    /**
     * @param coverSheetImageType the coverSheetImageType to set
     */
    public void setCoverSheetImageType(String coverSheetImageType) {
        this.coverSheetImageType = coverSheetImageType;
    }
    
    /**
     * @return the optionalSectionIds
     */
    public String[] getOptionalSectionIds() {
        return optionalSectionIds;
    }
    /**
     * @param optionalSectionIds the optionalSectionIds to set
     */
    public void setOptionalSectionIds(String[] optionalSectionIds) {
        this.optionalSectionIds = optionalSectionIds;
    }
    
    /**
     * @return the colorForCriteria
     */
    public String[] getColorForCriteria() {
        return colorForCriteria;
    }
    
    /**
     * @param colorForCriteria the colorForCriteria to set
     */
    public void setColorForCriteria(String[] colorForCriteria) {
        this.colorForCriteria = colorForCriteria;
    }
    
    /**
     * @return the contractBaseClass
     */
    public String getContractBaseClass() {
        return contractBaseClass;
    }
    
    /**
     * @param contractBaseClass the contractBaseClass to set
     */
    public void setContractBaseClass(String contractBaseClass) {
        this.contractBaseClass = contractBaseClass;
    }
    
    /**
     * @return the presenterName
     */
    public String getPresenterName() {
        return presenterName;
    }
    
    /**
     * @param presenterName the presenterName to set
     */
    public void setPresenterName(String presenterName) {
        this.presenterName = presenterName;
    }
    
    /**
     * @return the presenterFirmName
     */
    public String getPresenterFirmName() {
        return presenterFirmName;
    }
    
    /**
     * @param presenterFirmName the presenterFirmName to set
     */
    public void setPresenterFirmName(String presenterFirmName) {
        this.presenterFirmName = presenterFirmName;
    }
    
    /**
     * @return the preparedForCompanyName
     */
    public String getPreparedForCompanyName() {
        return preparedForCompanyName;
    }
    
    /**
     * @param preparedForCompanyName the preparedForCompanyName to set
     */
    public void setPreparedForCompanyName(String preparedForCompanyName) {
        this.preparedForCompanyName = preparedForCompanyName;
    }
    
    /**
     * @return the isBrokerFirmSmithBarneyAssociated
     */
    public boolean isBrokerFirmSmithBarneyAssociated() {
        return isBrokerFirmSmithBarneyAssociated;
    }
    
    /**
     * @param isBrokerFirmSmithBarneyAssociated the isBrokerFirmSmithBarneyAssociated to set
     */
    public void setBrokerFirmSmithBarneyAssociated(boolean isBrokerFirmSmithBarneyAssociated) {
        this.isBrokerFirmSmithBarneyAssociated = isBrokerFirmSmithBarneyAssociated;
    }
    
    /**
     * @return the contractBaseFundPackageSeries
     */
    public String getContractBaseFundPackageSeries() {
        return contractBaseFundPackageSeries;
    }
    
    /**
     * @param contractBaseFundPackageSeries the contractBaseFundPackageSeries to set
     */
    public void setContractBaseFundPackageSeries(String contractBaseFundPackageSeries) {
        this.contractBaseFundPackageSeries = contractBaseFundPackageSeries;
    }
    /**
     * @return the siteId
     */
    public String getSiteId() {
        return siteId;
    }
    /**
     * @param siteId the siteId to set
     */
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    /**
     * @return the fundMenu
     */
    public String getFundMenu() {
        return fundMenu;
    }
    /**
     * @param fundMenu the fundMenu to set
     */
    public void setFundMenu(String fundMenu) {
        this.fundMenu = fundMenu;
    }
    /**
     * @return the classMenu
     */
    public String getClassMenu() {
        return classMenu;
    }
    /**
     * @param classMenu the classMenu to set
     */
    public void setClassMenu(String classMenu) {
        this.classMenu = classMenu;
    }
    /**
     * @return the includeNML
     */
    public boolean isIncludeNML() {
        return includeNML;
    }
    /**
     * @param includeNML the includeNML to set
     */
    public void setIncludeNML(boolean includeNML) {
        this.includeNML = includeNML;
    }
    /**
     * @return the contract
     */
    public String getContract() {
        return contract;
    }
    /**
     * @param contract the contract to set
     */
    public void setContract(String contract) {
        this.contract = contract;
    }

    /**
     * @return the includeClosedFunds
     */
    public boolean isIncludeClosedFunds() {
        return includeClosedFunds;
    }

    /**
     * @param includeClosedFunds the includeClosedFunds to set
     */
    public void setIncludeClosedFunds(boolean includeClosedFunds) {
        this.includeClosedFunds = includeClosedFunds;
    }

    /**
     * @return the selectAll
     */
    public boolean isSelectAll() {
        return selectAll;
    }

    /**
     * @param selectAll the selectAll to set
     */
    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    /**
     * @return the startFromScratch
     */
    public boolean isStartFromScratch() {
        return startFromScratch;
    }

    /**
     * @param startFromScratch the startFromScratch to set
     */
    public void setStartFromScratch(boolean startFromScratch) {
        this.startFromScratch = startFromScratch;
    }

    /**
     * @return the selectLifeStyle
     */
    public boolean isSelectLifeStyle() {
        return selectLifeStyle;
    }

    /**
     * @param selectLifeStyle the selectLifeStyle to set
     */
    public void setSelectLifeStyle(boolean selectLifeStyle) {
        this.selectLifeStyle = selectLifeStyle;
    }

    /**
     * @return the selectLifeCycle
     */
    public boolean isSelectLifeCycle() {
        return selectLifeCycle;
    }

    /**
     * @param selectLifeCycle the selectLifeCycle to set
     */
    public void setSelectLifeCycle(boolean selectLifeCycle) {
        this.selectLifeCycle = selectLifeCycle;
    }

    /**
     * @return the selectMarketFundsAsDefault
     */
    public boolean isSelectMarketFundsAsDefault() {
        return selectMarketFundsAsDefault;
    }

    /**
     * @param selectMarketFundsAsDefault the selectMarketFundsAsDefault to set
     */
    public void setSelectMarketFundsAsDefault(boolean selectMarketFundsAsDefault) {
        this.selectMarketFundsAsDefault = selectMarketFundsAsDefault;
    }

    /**
     * @return the metricSelectionCriteria
     */
    public LinkedHashMap<Integer, Integer> getMetricSelectionCriteria() {
        return metricSelectionCriteria;
    }

    /**
     * @param metricSelectionCriteria the metricSelectionCriteria to set
     */
    public void setMetricSelectionCriteria(LinkedHashMap<Integer, Integer> metricSelectionCriteria) {
        this.metricSelectionCriteria = metricSelectionCriteria;
    }

    /**
     * @return the coreToolData
     */
    public CoreToolSessionData getCoreToolData() {
        return coreToolData;
    }

    /**
     * @param coreToolData the coreToolData to set
     */
    public void setCoreToolData(CoreToolSessionData coreToolData) {
        this.coreToolData = coreToolData;
    }

    /**
     * @return the additionalFunds
     */
    public String[] getAdditionalFunds() {
        return additionalFunds;
    }

    /**
     * @param additionalFunds the additionalFunds to set
     */
    public void setAdditionalFunds(String[] additionalFunds) {
        this.additionalFunds = additionalFunds;
    }

    /**
     * @return the toolFunds
     */
    public String[] getToolRecommendedFunds() {
        return toolRecommendedFunds;
    }

    /**
     * @param toolFunds the toolFunds to set
     */
    public void setToolRecommendedFunds(String[] toolRecommendedFunds) {
        this.toolRecommendedFunds = toolRecommendedFunds;
    }

    /**
     * @return the checkedFunds
     */
    public String[] getCheckedFunds() {
        return checkedFunds;
    }

    /**
     * @param checkedFunds the checkedFunds to set
     */
    public void setCheckedFunds(String[] checkedFunds) {
        this.checkedFunds = checkedFunds;
    }

    /**
     * @return the contractFunds
     */
    public String[] getContractFunds() {
        return contractFunds;
    }

    /**
     * @param contractFunds the contractFunds to set
     */
    public void setContractFunds(String[] contractFunds) {
        this.contractFunds = contractFunds;
    }
    
    /**
     * @return the contractLocationId
     */
    public String getContractLocationId() {
        return contractLocationId;
    }

    /**
     * @param contractLocationId the contractLocationId to set
     */
    public void setContractLocationId(String contractLocationId) {
        this.contractLocationId = contractLocationId;
    }
    
    /**
     * Debug method only
     */
    public String toString() {
        return toString("webPage");
    }
    public String toString(String mode) {
        StringBuffer sb = new StringBuffer("presenterFirmName=" + presenterFirmName  +
                "|presenterName=" + presenterName + "|preparedForCompanyName=" + preparedForCompanyName + 
                "|CONTRACT=" + contract + "|CONTRACT_BASE_CLASS=" + contractBaseClass+ "|CONTRACT_BASE_FUND_PACKAGE=" + contractBaseFundPackageSeries + "|NML=" + includeNML + "|IS_SMITH_BARNEY=" + isBrokerFirmSmithBarneyAssociated +
                "|EXCLUDE_EDJ=" + excludeEDJ +
                "|MERRILL_FILTER=" + merrillFirmFilter +
                "|FUND_MENU=" + fundMenu + "|CLASS_MENU=" + classMenu + 
                "|SITEID=" + siteId +
                "|STATECODE= " + stateCode +
                "|INCLUDE_CLOSED=" + includeClosedFunds +
                "|SELECT_LIFESTYLE=" + selectLifeStyle +
                "|SELECT_LIFECYCLE=" + selectLifeCycle +
                "|SELECT_ALL=" + selectAll +
                "|SELECT_SCRATCH=" + startFromScratch +
                "|SELECT_MONEYMARKET=" + selectMarketFundsAsDefault +
                "|METRIC_SELECTION_CRITERIA=[");
    
        sb.append("TOTAL_RETURN=").append(metricSelectionCriteria.get(SelectionCriteria.TOTAL_RETURN));
        sb.append(",ALPHA=").append(metricSelectionCriteria.get(SelectionCriteria.ALPHA));
        sb.append(",SHARPE_RATIO=").append(metricSelectionCriteria.get(SelectionCriteria.SHARPE_RATIO));
        sb.append(",INFORMATION_RATIO=").append(metricSelectionCriteria.get(SelectionCriteria.INFORMATION_RATIO));
        sb.append(",R2=").append(metricSelectionCriteria.get(SelectionCriteria.R2));
        sb.append(",UPSIDE_CAPTURE=").append(metricSelectionCriteria.get(SelectionCriteria.UPSIDE_CAPTURE));
        sb.append(",DOWNSIDE_CAPTURE=").append(metricSelectionCriteria.get(SelectionCriteria.DOWNSIDE_CAPTURE));
        sb.append(",STANDARD_DEVIATION=").append(metricSelectionCriteria.get(SelectionCriteria.STANDARD_DEVIATION));
        sb.append(",BETA=").append(metricSelectionCriteria.get(SelectionCriteria.BETA));
        sb.append(",FEES=").append(metricSelectionCriteria.get(SelectionCriteria.FEES));
        sb.append(",5_YEAR_RETURN=").append(metricSelectionCriteria.get(SelectionCriteria.TOTAL_RETURN_5YEAR));
        sb.append(",10_YEAR_RETURN=").append(metricSelectionCriteria.get(SelectionCriteria.TOTAL_RETURN_10YEAR)).append(']');
    
        if(StringUtils.equalsIgnoreCase(mode, "report")){
            sb.append("|COVER_TYPE=" + coverSheetImageType );
            
            sb.append("|TOTAL_CHECKED_FUNDS=" + checkedFunds.length );
            sb.append("|CHECKED_FUNDS=[");
            if (checkedFunds.length >= 1) sb.append(checkedFunds[0]);
        
            for (int i = 1; i < checkedFunds.length; i++) {
                sb.append(',').append(checkedFunds[i]);
            }
            sb.append(']');
            
            sb.append("|TOTAL_TOOL_FUNDS=" + toolRecommendedFunds.length );
            sb.append("|TOOL_FUNDS=[");
            if (toolRecommendedFunds.length >= 1) sb.append(toolRecommendedFunds[0]);
        
            for (int i = 1; i < toolRecommendedFunds.length; i++) {
                sb.append(',').append(toolRecommendedFunds[i]);
            }
            sb.append(']');
            
            sb.append("|TOTAL_CONTRACT_FUNDS=" + contractFunds.length );
            sb.append("|CONTRACT_FUNDS=[");
            if (contractFunds.length >= 1) sb.append(contractFunds[0]);
        
            for (int i = 1; i < contractFunds.length; i++) {
                sb.append(',').append(contractFunds[i]);
            }
            sb.append(']');
            
            sb.append("|TOTAL_ADDITIONAL_FUNDS=" + additionalFunds.length );
            sb.append("|ADDITIONAL_FUNDS=[");
            if (additionalFunds.length >= 1) sb.append(additionalFunds[0]);
        
            for (int i = 1; i < additionalFunds.length; i++) {
                sb.append(',').append(additionalFunds[i]);
            }
            sb.append(']');
            
            sb.append("|OPTIONAL_SECTION_IDS=[");
            if (optionalSectionIds.length >= 1) sb.append(optionalSectionIds[0]);
        
            for (int i = 1; i < optionalSectionIds.length; i++) {
                sb.append(',').append(optionalSectionIds[i]);
            }
            sb.append(']');
        }    
    
        return sb.toString();
    }

    /**
	 * @return includeGIFLSelectFunds
	 */
	public boolean isIncludeGIFLSelectFunds() {
		return includeGIFLSelectFunds;
	}

	/**
     * @param includeGIFLSelectFunds the includeGIFLSelectFunds to set
     */
	public void setIncludeGIFLSelectFunds(boolean includeGIFLSelectFunds) {
		this.includeGIFLSelectFunds = includeGIFLSelectFunds;
	}

	/**
	 * 
	 * @return lifecycleFundSuites
	 */
	public List<String> getLifecycleFundSuites() {
		return lifecycleFundSuites;
	}

	/**
	 * 
	 * @param lifecycleFundSuites
	 */
	public void setLifecycleFundSuites(List<String> lifecycleFundSuites) {
		this.lifecycleFundSuites = lifecycleFundSuites;
	}
	
	/**
	 * Returns lifeStyleFundSuiteName
	 * 
	 * @return lifestyleFundSuites
	 */
	public List<String> getLifestyleFundSuites() {
		return lifestyleFundSuites;
	}

	/**
	 * Set lifeStyleFundSuiteName
	 * 
	 * @param lifecycleFundSuites
	 */
	public void setLifestyleFundSuites(List<String> lifestyleFundSuites) {
		this.lifestyleFundSuites = lifestyleFundSuites;
	}


	/**
	 * Returns lifeCycle fundSuiteNames
	 * 
	 * @return the lifecycleFundSuiteNames
	 */
	public ArrayList<String> getLifecycleFundSuiteNames() {
		return lifecycleFundSuiteNames;
	}

	/**
	 * set lifeCycle fundSuiteNames
	 * @param lifecycleFundSuiteNames the lifecycleFundSuiteNames to set
	 */
	public void setLifecycleFundSuiteNames(ArrayList<String> lifecycleFundSuiteNames) {
		this.lifecycleFundSuiteNames = lifecycleFundSuiteNames;
	}

	/**
	 * Returns lifeStyle fundFamilyCodesAndFundSuiteNames
	 * 
	 * @return the lifestyleFundsDetails
	 */
	public Map<String, String> getLifestyleFundsDetails() {
		return lifestyleFundsDetails;
	}

	/**
	 * Set lifeStyle fundFamilyCodesAndFundSuiteNames
	 * @param lifestyleFundsDetails the lifestyleFundsDetails to set
	 */
	public void setLifestyleFundsDetails(Map<String, String> lifestyleFundsDetails) {
		this.lifestyleFundsDetails = lifestyleFundsDetails;
	}

	/**
	 * Returns lifeStyleFundFamilyCode and lifeStyleFundName
	 * 
	 * @return the lifeStyleFunds
	 */
	public Map<String, ArrayList<String>> getLifeStyleFunds() {
		return lifeStyleFunds;
	}

	/**
	 * Set lifeStyleFamilyCode and lifeStyleFundName
	 * 
	 * @param lifeStyleFunds the lifeStyleFunds to set
	 */
	public void setLifeStyleFunds(Map<String, ArrayList<String>> lifeStyleFunds) {
		this.lifeStyleFunds = lifeStyleFunds;
	}

	public boolean isExcludeEDJ() {
        return excludeEDJ;
    }

    public void setExcludeEDJ(boolean excludeEDJ) {
        this.excludeEDJ = excludeEDJ;
    }

	public boolean isMerrillFirmFilter() {
        return merrillFirmFilter;
    }

    public void setMerrillFirmFilter(boolean merrillFirmFilter) {
        this.merrillFirmFilter = merrillFirmFilter;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

	public String getAverageExpenceRatioMethod() {
		return averageExpenceRatioMethod;
	}

	public void setAverageExpenceRatioMethod(String averageExpenceRatioMethod) {
		this.averageExpenceRatioMethod = averageExpenceRatioMethod;
	}

	public String getSVFFundList() {
		return sVFFundList;
	}

	public void setSVFFundList(String sVFFundList) {
		this.sVFFundList = sVFFundList;
	}

	public List<String> getMMFFundList() {
		return mMFFundList;
	}

	public void setMMFFundList(List<String> mMFFundList) {
		this.mMFFundList = mMFFundList;
	}

	public List<String> getOldContractSelectedSVFFunds() {
		return oldContractSelectedSVFFunds;
	}

	public void setOldContractSelectedSVFFunds(
			List<String> oldContractSelectedSVFFunds) {
		this.oldContractSelectedSVFFunds = oldContractSelectedSVFFunds;
	}
    
	
}