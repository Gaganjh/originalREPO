package com.manulife.pension.bd.web.fundEvaluator;

import java.io.Serializable;

/**
 * Used for asset classes display on step4 - Investment option selection page.
 * 
 * @author Ranjith Kumar
 *
 */
public class AssetClassDetails implements Serializable {
    
    private static final long serialVersionUID = 0L;
    
    private String assetClassId;
    
    private String assetClassName;
    
    private int totalFundsSelected;
    
    private int totalBaseLineFunds;
    
    /**
     * 
     * @param assetClassId
     * @param assetClassName
     * @param totalFundsSelected
     */
    public AssetClassDetails(String assetClassId, String assetClassName, int totalFundsSelected,
            int totalBaseLineFunds) {
        super();
        this.assetClassId = assetClassId;
        this.assetClassName = assetClassName;
        this.totalFundsSelected = totalFundsSelected;
        this.totalBaseLineFunds = totalBaseLineFunds;
    }
    
    /**
     * Returns asset class id.
     * @return the assetClassId
     */
    public String getAssetClassId() {
        return assetClassId;
    }
    
    /**
     * Sets asset class id.
     * @param assetClassId the assetClassId to set
     */
    public void setAssetClassId(String assetClassId) {
        this.assetClassId = assetClassId;
    }
    
    /**
     * Returns asset class name 
     * @return the assetClassName
     */
    public String getAssetClassName() {
        return assetClassName;
    }
    
    /**
     * Sets asset class name
     * @param assetClassName the assetClassName to set
     */
    public void setAssetClassName(String assetClassName) {
        this.assetClassName = assetClassName;
    }
    
    /**
     * Returns total selected funds of the asset class
     * @return the totalFundsSelected
     */
    public int getTotalFundsSelected() {
        return totalFundsSelected;
    }
    
    /**
     * Sets total selected funds of the asset class
     * @param totalFundsSelected the totalFundsSelected to set
     */
    public void setTotalFundsSelected(int totalFundsSelected) {
        this.totalFundsSelected = totalFundsSelected;
    }

    /**
     * Returns total base line funds of the asset class
     * @return the totalBaseLineFunds
     */
    public int getTotalBaseLineFunds() {
        return totalBaseLineFunds;
    }

    /**
     * Sets total base line funds of the asset class
     * @param totalBaseLineFunds the totalBaseLineFunds to set
     */
    public void setTotalBaseLineFunds(int totalBaseLineFunds) {
        this.totalBaseLineFunds = totalBaseLineFunds;
    }

}