package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

import com.manulife.pension.util.StaticHelperClass;

/** 
 * This VO is used to contain the values returned from Apollo before 
 * the fund information is gathered.  It's used for:
 * 
 * - FTF details
 * - Rebalance details
 * - Contribution adjustment details
 * - Contribution details
 */

public class TransactionDetailsItem implements Serializable {
	
	private String fundId;
	private String rateType;
	private String moneyTypeDescription;
	private String subtype;
	private BigDecimal amount;
	private BigDecimal unitValue;
	private BigDecimal numberOfUnits;
	private String comments;
	
	private int sortNo;
	private String riskCategoryCode;
	private BigDecimal percentage;
	
	private BigDecimal employeeAmount;
	private BigDecimal employeePercentage;
	private BigDecimal employerAmount;
	private BigDecimal employerPercentage;
	
	public TransactionDetailsItem() {
	}

	public String toString() {

		return StaticHelperClass.toString(this);		
	}
	/**
	 * Gets the amount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * Sets the amount
	 * @param amount The amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Gets the numberOfUnits
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getNumberOfUnits() {
		return numberOfUnits;
	}
	/**
	 * Sets the numberOfUnits
	 * @param numberOfUnits The numberOfUnits to set
	 */
	public void setNumberOfUnits(BigDecimal numberOfUnits) {
		this.numberOfUnits = numberOfUnits;
	}

	/**
	 * Gets the unitValue
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getUnitValue() {
		return unitValue;
	}
	/**
	 * Sets the unitValue
	 * @param unitValue The unitValue to set
	 */
	public void setUnitValue(BigDecimal unitValue) {
		this.unitValue = unitValue;
	}

	/**
	 * Gets the comments
	 * @return Returns a String
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * Sets the comments
	 * @param comments The comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * Gets the sortNo
	 * @return Returns a int
	 */
	public int getSortNo() {
		return sortNo;
	}
	/**
	 * Sets the sortNo
	 * @param sortNo The sortNo to set
	 */
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	/**
	 * Gets the riskCategoryCode
	 * @return Returns a String
	 */
	public String getRiskCategoryCode() {
		return riskCategoryCode;
	}
	/**
	 * Sets the riskCategoryCode
	 * @param riskCategoryCode The riskCategoryCode to set
	 */
	public void setRiskCategoryCode(String riskCategoryCode) {
		this.riskCategoryCode = riskCategoryCode;
	}

	/**
	 * Gets the fundId
	 * @return Returns a String
	 */
	public String getFundId() {
		return fundId;
	}
	/**
	 * Sets the fundId
	 * @param fundId The fundId to set
	 */
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}


	/**
	 * Gets the moneyTypeDescription
	 * @return Returns a String
	 */
	public String getMoneyTypeDescription() {
		return moneyTypeDescription;
	}
	/**
	 * Sets the moneyTypeDescription
	 * @param moneyTypeDescription The moneyTypeDescription to set
	 */
	public void setMoneyTypeDescription(String moneyTypeDescription) {
		this.moneyTypeDescription = moneyTypeDescription;
	}

	/**
	 * Gets the percentage
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getPercentage() {
		return percentage;
	}
	/**
	 * Sets the percentage
	 * @param percentage The percentage to set
	 */
	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}


	/**
	 * Gets the employeeAmount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getEmployeeAmount() {
		return employeeAmount;
	}
	/**
	 * Sets the employeeAmount
	 * @param employeeAmount The employeeAmount to set
	 */
	public void setEmployeeAmount(BigDecimal employeeAmount) {
		this.employeeAmount = employeeAmount;
	}

	/**
	 * Gets the employeePercentage
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getEmployeePercentage() {
		return employeePercentage;
	}
	/**
	 * Sets the employeePercentage
	 * @param employeePercentage The employeePercentage to set
	 */
	public void setEmployeePercentage(BigDecimal employeePercentage) {
		this.employeePercentage = employeePercentage;
	}

	/**
	 * Gets the employerAmount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getEmployerAmount() {
		return employerAmount;
	}
	/**
	 * Sets the employerAmount
	 * @param employerAmount The employerAmount to set
	 */
	public void setEmployerAmount(BigDecimal employerAmount) {
		this.employerAmount = employerAmount;
	}

	/**
	 * Gets the employerPercentage
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getEmployerPercentage() {
		return employerPercentage;
	}
	/**
	 * Sets the employerPercentage
	 * @param employerPercentage The employerPercentage to set
	 */
	public void setEmployerPercentage(BigDecimal employerPercentage) {
		this.employerPercentage = employerPercentage;
	}

	/**
	 * Gets the subtype
	 * @return Returns a String
	 */
	public String getSubtype() {
		return subtype;
	}
	/**
	 * Sets the subtype
	 * @param subtype The subtype to set
	 */
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	public String getRateType() {
		return rateType;
	}

}