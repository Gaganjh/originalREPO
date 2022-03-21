package com.manulife.pension.bd.web.fundEvaluator.common;

import java.io.Serializable;
import java.util.ArrayList;

import com.manulife.pension.service.fund.valueobject.AssetClass;

/**
 * This object is wrapper to asset class object
 * @author PWakode
 */
public class AssetClassForInvOption implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private AssetClass assetClass;
    private boolean selected = false;
    private boolean hasToolRecommendedFund = false;
    
    private ArrayList<FundForInvOption> fundForInvOptionList;
    
    public AssetClassForInvOption(AssetClass assetClass) {
        this.assetClass = assetClass;
    }
    
    public String getId() {
        return assetClass.getAssetClass();
    }
    
    public String getDescription() {
        return assetClass.getAssetClassDescription();
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public boolean hasToolRecommendedFund() {
        return hasToolRecommendedFund;
    }
    
    public void setHasToolRecommendedFund(boolean hasToolRecommendedFund) {
        this.hasToolRecommendedFund = hasToolRecommendedFund;
    }
    
    /**
     * @return the fundForInvOptionList
     */
    public ArrayList<FundForInvOption> getFundForInvOptionList() {
        return fundForInvOptionList;
    }

    /**
     * @param fundForInvOptionList the fundForInvOptionList to set
     */
    public void setFundForInvOptionList(ArrayList<FundForInvOption> fundForInvOptionList) {
        this.fundForInvOptionList = fundForInvOptionList;
    }
    
}