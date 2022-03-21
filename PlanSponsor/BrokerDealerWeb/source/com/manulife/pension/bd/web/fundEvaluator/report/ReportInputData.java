package com.manulife.pension.bd.web.fundEvaluator.report;

import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.fundEvaluator.common.CoreToolConstants;
import com.manulife.pension.bd.web.fundEvaluator.session.SessionContext;
import com.manulife.pension.service.fund.valueobject.Fund;
import com.manulife.pension.service.fund.valueobject.PercentileRankedFund;
import com.manulife.pension.service.fund.coretool.model.common.GlobalData;

/**
 * This class represents input data for the Report generation process. Constructor 
 * takes SessionContext object (which contains all user input & base fund line up details)
 * @author PWakode
 */

public class ReportInputData implements CoreToolConstants
{
	private SessionContext sessionContext;

	private Hashtable<String, Fund> brokerSelectedFundPool = null;
    private Hashtable<String, Fund> toolRecommendedFunds = null;
    private Hashtable<String, Fund> contractFundPool = null;
    private Hashtable<String, Fund> checkedFundPool = null;

	/**
	 * ReportInputData constructor comment.
	 */
	public ReportInputData(SessionContext sessionContext)  {
		this.sessionContext = sessionContext;
	}
	
	public String getPreparedForCompanyName() {
		return sessionContext.getPreparedForCompanyName();
	}	
	
	public String getPresenterName() {
		return sessionContext.getPresenterName();
	}	

	public String getPresenterFirmName() {
		return sessionContext.getPresenterFirmName();
	}	
	
	public boolean isBrokerFirmSmithBarneyAssociated() {
        return sessionContext.isBrokerFirmSmithBarneyAssociated();
    }

	/**
	 * Key = fundId, Value = PercentileRankedFund
	 * N.B.: If there is no benchmark for a fund's asset class then
	 * the value associated to the key for that fund should be NULL.
	 */
	public Map<String, PercentileRankedFund> getAllFundsInReport() {
		 
		Map<String, PercentileRankedFund> result = sessionContext.getCoreToolData().getRankedFundsByFundIDMap();
		
		return result;
	}

	public Date getAsOfDate() {
		return GlobalData.asOfDateMap.get(ReportDataModel.ASOFDATE_FUNDMETRICS_KEY);
	}
	
	// Key = fundId, Value = Fund
	public Map<String, Fund> getBrokerSelectedFunds(Hashtable<String, Fund> fundLineupFundTable) {
        if (brokerSelectedFundPool == null) {
            brokerSelectedFundPool = new Hashtable<String, Fund>();
        
            for (int i = 0; i < sessionContext.getAdditionalFunds().length; i++) {
                String fundId = sessionContext.getAdditionalFunds()[i];
                if(fundId != null){
                    brokerSelectedFundPool.put(fundId, (Fund)fundLineupFundTable.get(fundId));
                }
            }
        }   
        return brokerSelectedFundPool;
    }

	public String getContractNumber() 	{
		return sessionContext.getContract();
	}	
	
	public  LinkedHashMap<Integer, Integer> getCriteriaWeightings() {
		return sessionContext.getMetricSelectionCriteria();
	}

	public String[] getColorForCriteria() {
		return sessionContext.getColorForCriteria();
	}
	
	public String[] getOptionalSectionIds() {
        return sessionContext.getOptionalSectionIds();
    }
	
	public String getCoverSheetImageType() {
        return sessionContext.getCoverSheetImageType();
    }
	
	public String getContractBaseClass() {
        return sessionContext.getContractBaseClass();
    }
	
	public String getContractBaseFundPackageSeries() {
        return sessionContext.getContractBaseFundPackageSeries();
    }
	
	public String getContractLocationId() {
        return sessionContext.getContractLocationId();
    }

	/**
	 * Key = assetClassId
	 * Value = Vector of the PercentileRankedFund-s for a given Asset Class sorted in descending order of rank
	 */
	public Map<String, List<PercentileRankedFund>> getPercentileRankedFundVectors() {
		
		Map<String, List<PercentileRankedFund>>	ret = sessionContext.getCoreToolData().getRankedFundsByAssetClassIDMap();
			
		return ret;
	}

	// Return the subsidiaryId, a.k.a SiteId/
	public String getSubsidiaryId() {
		//return sessionContext.getSiteId();
	    String site = LOCATION_USA;//setting default as USA
		if(StringUtils.isNotBlank(sessionContext.getContract())){//if contract is provided
            site = sessionContext.getContractLocationId();//taking site info based on contract
        }
        else{
            site = sessionContext.getSiteId();//taking site info based on user selections on Step 1
        }
        if(site.equalsIgnoreCase(LOCATION_USA) || site.equalsIgnoreCase(COMPANY_ID_USA)){
            return LOCATION_USA;
        }
        else{
            return LOCATION_NY;
        }
	}	
	
	// Returns Map, where Keys = String fundIds, Values = funds/
	public Map<String, String> getToolRecommendedFunds() {
		
		Map<String, String>	ret = sessionContext.getCoreToolData().getToolRecommendedFundsMap();
		
		return ret;
	}	

	public String getFundMenu() {
		return sessionContext.getFundMenu();
	}

	public boolean isNML() {
		return sessionContext.isIncludeNML();
	}

	public String getClassMenu() {
		return sessionContext.getClassMenu();
	}
	
    // Key = fundId, Value = Fund 
    public Map<String, Fund> getToolRecommendedFunds(Hashtable<String, Fund> fundLineupFundTable) {
        if (toolRecommendedFunds == null) {
        	toolRecommendedFunds = new Hashtable<String, Fund>();
        
            for (int i = 0; i < sessionContext.getToolRecommendedFunds().length; i++) {
                String fundId = sessionContext.getToolRecommendedFunds()[i];
                if(fundId != null){
                	toolRecommendedFunds.put(fundId, (Fund) fundLineupFundTable.get(fundId));
                }
                
            }
        }   
        return toolRecommendedFunds;
    }
    
    // Key = fundId, Value = Fund
    public Map<String, Fund> getCheckedFunds(Hashtable<String, Fund> fundLineupFundTable) {
        if (checkedFundPool == null) {
            checkedFundPool = new Hashtable<String, Fund>();
        
            for (int i = 0; i < sessionContext.getCheckedFunds().length; i++) {
                String fundId = sessionContext.getCheckedFunds()[i];
                if(fundId != null){
                    checkedFundPool.put(fundId,(Fund) fundLineupFundTable.get(fundId));
                }
                
            }
        }   
        return checkedFundPool;
    }
    
    // Key = fundId, Value = Fund 
    public Map<String, Fund> getContractFunds(Hashtable<String, Fund> fundLineupFundTable) {
        if (contractFundPool == null) {
            contractFundPool = new Hashtable<String, Fund>();
        
            for (int i = 0; i < sessionContext.getContractFunds().length; i++) {
                String fundId = sessionContext.getContractFunds()[i];
                if(fundId != null){
                    contractFundPool.put(fundId,(Fund) fundLineupFundTable.get(fundId));
                }
            }
        }   
        return contractFundPool;
    }

    public boolean isIncludeClosedFunds() {
        return sessionContext.isIncludeClosedFunds();
    }
    
    /**
	 * @return includeGIFLSelectFunds
	 */
    public boolean isIncludeGIFLSelectFunds() {
        return sessionContext.isIncludeGIFLSelectFunds();
    }
    
}
