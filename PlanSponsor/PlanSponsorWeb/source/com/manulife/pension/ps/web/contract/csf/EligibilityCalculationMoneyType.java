package com.manulife.pension.ps.web.contract.csf;

import java.util.Date;

import com.manulife.pension.common.BaseSerializableCloneableObject;

/**
 * Utility class for common items of the CSF pages
 * 
 * @author Vanidha Rajendiran
 */
public class EligibilityCalculationMoneyType extends BaseSerializableCloneableObject{

	private static final long serialVersionUID = 1L;

	private String fieldName = "";

	private String moneyTypeName = "";

	private String moneyTypeValue = "";
	
	private String csfValue = "";

	private String moneyTypeDescription = "";
	
	private String eligibilityDate;
	
	private String calculationOverride;
	
    private Date planEntryDate;
    
    private String moneyTypeId;
    
    private String moneyTypeShortName="";

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the moneyTypeName
	 */
	public String getMoneyTypeName() {
		return moneyTypeName;
	}

	/**
	 * @param moneyTypeName the moneyTypeName to set
	 */
	public void setMoneyTypeName(String moneyTypeName) {
		this.moneyTypeName = moneyTypeName;
	}

	/**
	 * @return the moneyTypeValue
	 */
	public String getMoneyTypeValue() {
		return moneyTypeValue;
	}

	/**
	 * @param moneyTypeValue the moneyTypeValue to set
	 */
	public void setMoneyTypeValue(String moneyTypeValue) {
		this.moneyTypeValue = moneyTypeValue;
	}

	/**
	 * @return the csfValue
	 */
	public String getCsfValue() {
		return csfValue;
	}

	/**
	 * @param csfValue the csfValue to set
	 */
	public void setCsfValue(String csfValue) {
		this.csfValue = csfValue;
	}

	/**
	 * @return the moneyTypeDescription
	 */
	public String getMoneyTypeDescription() {
		return moneyTypeDescription;
	}

	/**
	 * @param moneyTypeDescription the moneyTypeDescription to set
	 */
	public void setMoneyTypeDescription(String moneyTypeDescription) {
		this.moneyTypeDescription = moneyTypeDescription;
	}

	/**
	 * @return the eligibilityDate
	 */
	public String getEligibilityDate() {
		return eligibilityDate;
	}

	/**
	 * @param eligibilityDate the eligibilityDate to set
	 */
	public void setEligibilityDate(String eligibilityDate) {
		this.eligibilityDate = eligibilityDate;
	}

	/**
	 * @return the calculationOverride
	 */
	public String getCalculationOverride() {
		return calculationOverride;
	}

	/**
	 * @param calculationOverride the calculationOverride to set
	 */
	public void setCalculationOverride(String calculationOverride) {
		this.calculationOverride = calculationOverride;
	}

	/**
	 * @return the planEntryDate
	 */
	public Date getPlanEntryDate() {
		return planEntryDate;
	}

	/**
	 * @param planEntryDate the planEntryDate to set
	 */
	public void setPlanEntryDate(Date planEntryDate) {
		this.planEntryDate = planEntryDate;
	}

	/**
	 * @return the moneyTypeId
	 */
	public String getMoneyTypeId() {
		return moneyTypeId;
	}

	/**
	 * @param moneyTypeId the moneyTypeId to set
	 */
	public void setMoneyTypeId(String moneyTypeId) {
		this.moneyTypeId = moneyTypeId;
	}

	/**
	 * @return the moneyTypeShortName
	 */
	public String getMoneyTypeShortName() {
		return moneyTypeShortName;
	}

	/**
	 * @param moneyTypeShortName the moneyTypeShortName to set
	 */
	public void setMoneyTypeShortName(String moneyTypeShortName) {
		this.moneyTypeShortName = moneyTypeShortName;
	}
}
