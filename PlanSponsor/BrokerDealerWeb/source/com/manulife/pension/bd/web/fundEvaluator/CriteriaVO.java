package com.manulife.pension.bd.web.fundEvaluator;

import java.io.Serializable;


/**
 * iEvaluator criteria value object.
 * @author Ranjith Kumar
 *
 */
public class CriteriaVO implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String criteriaName;
    private String measuredBy;
    private String description;
    private String criteriaSelected = "";  
    private String sliderSerialNumber;
    private String criteriaValue = "0";
    private boolean percentageIndRequired;
    private int selectCriteriaIndex = 0;
    
    
    /**
     * Default constructor
     *
     */
    public CriteriaVO() {
        
    }
    
    /**
     * @return the criteriaSelected
     */
    public String getCriteriaSelected() {
        return criteriaSelected;
    }

    /**
     * @param criteriaSelected the criteriaSelected to set
     */
    public void setCriteriaSelected(String criteriaSelected) {
        this.criteriaSelected = criteriaSelected;
    }

    /**
     * populates the values required for criteria page.
     * @param criteriaName
     * @param measuredBy
     * @param description
     */
    public CriteriaVO(String criteriaName, String measuredBy, String description, boolean percentageIndRequired, int selectCriteriaIndex) {
        super();
        this.criteriaName = criteriaName;
        this.measuredBy = measuredBy;
        this.description = description;
        this.setSliderSerialNumber("0");
        this.setPercentageIndRequired(percentageIndRequired);
        this.selectCriteriaIndex = selectCriteriaIndex;
    }

    /**
     * This method returns the criteria name
     * @return criteriaName
     */
    public String getCriteriaName() {
        return criteriaName;
    }
    
    /**     * 
     * @param criteriaName the criteriaName to set
     */
    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }
    
    /**
     * This method returns the criteria description.
     * @return description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * This method returns the measure by value.
     * @return the measuredBy
     */
    public String getMeasuredBy() {
        return measuredBy;
    }
    
    /**
     * @param measuredBy the measuredBy to set
     */
    public void setMeasuredBy(String measuredBy) {
        this.measuredBy = measuredBy;
    }

    /**
     * @return the sliderSerialNumber
     */
    public String getSliderSerialNumber() {
        return sliderSerialNumber;
    }

    /**
     * @param sliderSerialNumber the sliderSerialNumber to set
     */
    public void setSliderSerialNumber(String sliderSerialNumber) {
        this.sliderSerialNumber = sliderSerialNumber;
    }

    /**
     * This method returns the criteria value
     * @return the criteriaValue
     */
    public String getCriteriaValue() {
        return criteriaValue;
    }

    /**
     * @param criteriaValue the criteriaValue to set
     */
    public void setCriteriaValue(String criteriaValue) {
        this.criteriaValue = criteriaValue;
    }

    /**
     * @return the percentageIndRequired
     */
    public boolean isPercentageIndRequired() {
        return percentageIndRequired;
    }

    /**
     * @param percentageIndRequired the percentageIndRequired to set
     */
    public void setPercentageIndRequired(boolean percentageIndRequired) {
        this.percentageIndRequired = percentageIndRequired;
    }
    
    public int getSelectCriteriaIndex() {
  		return selectCriteriaIndex;
  	}

  	public void setSelectCriteriaIndex(int selectCriteriaIndex) {
  		this.selectCriteriaIndex = selectCriteriaIndex;
  	}
}
