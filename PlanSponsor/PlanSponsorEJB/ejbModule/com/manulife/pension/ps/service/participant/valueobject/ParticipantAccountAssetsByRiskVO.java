package com.manulife.pension.ps.service.participant.valueobject;


/**
 * ParticipantAccountAssetsByRisk class
 * This class is used as a value object used on the Participant Account page,
 * for the Allocated Assets pie chart.
 * 
 * @author Simona Stoicescu
 *
 **/


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.service.fund.valueobject.FundVO;

public class ParticipantAccountAssetsByRiskVO implements Serializable{
	
	// risk groups that will be shown on the Participant Account page
	// TODO move this to a common constants class perhaps?
	// It's used on contractSnapshot as well, so it might be worthwhile moving to it to a common package or something
	// didn't want to cross reference to the reporting package
	
	private Map assetsByRisk;
	
	public ParticipantAccountAssetsByRiskVO()
	{
		assetsByRisk = new HashMap();
	}

	/**
	* set asset total for a certain Risk Group
	* @param riskCode Risk category code
	* @param total asset value for this risk category
	*/
	public void setAssetTotal(String riskCode, double total) {
		assetsByRisk.put(riskCode, new Double(total));
	}

	/**
    * get the total assets over all risk groups.
	* @return double totalAssets over all risk groups.
	*/
	public double getTotalAssets() {
	    int riskCategoryCount = FundVO.getRiskCategoryCodes().length;
	    double total = 0.0;
	    for (int i = 0; i < riskCategoryCount; i++) {
		    total += ((Double)assetsByRisk.get(FundVO.getRiskCategoryCodes()[i])).doubleValue();
	    }
	    return total;
    }
	    
	/**
   	* get the total assets in a risk group
	* @param riskCode Risk category code
	* @return double totalAssets in risk.
	*/
 	public double getTotalAssetsByRisk(String riskCode) 
 	{
		return ((Double)assetsByRisk.get(riskCode)).doubleValue();
	}
 	
 	/**
   	* get the total percentage of assets in a risk group
	* @param riskCode Risk category code
	* @return double total percentage Assets in risk.
	*/
 	public double getPercentageTotalByRisk(String riskCode){
 		return (getTotalAssetsByRisk(riskCode) / getTotalAssets());
 	}
}

