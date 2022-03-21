package com.manulife.pension.service.loan.valueobject;

import java.io.Serializable;

public class LoanTypeVO implements  Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String loanTypeCode; 
	private Integer maxAmortizationYear; 
	private boolean defaultSelected;  
	private String toolTipText;
	private boolean disabled;
	
	public String getLoanTypeCode() {
		return loanTypeCode;
	}
	public void setLoanTypeCode(String loanTypeCode) {
		this.loanTypeCode = loanTypeCode;
	}
	
	public Integer getMaxAmortizationYear() {
		return maxAmortizationYear;
	}
	
	public void setMaxAmortizationYear(Integer maxAmortizationYear) {
		this.maxAmortizationYear = maxAmortizationYear;
	}
	
	public boolean isDefaultSelected() {
		return defaultSelected;
	}
	public void setDefaultSelected(boolean defaultSelected) {
		this.defaultSelected = defaultSelected;
	}
	public String getToolTipText() {
		return toolTipText;
	}
	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
}
