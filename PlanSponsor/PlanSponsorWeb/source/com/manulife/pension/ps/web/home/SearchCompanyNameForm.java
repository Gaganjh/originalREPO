package com.manulife.pension.ps.web.home;

import com.manulife.pension.ps.web.report.ReportForm;

public class SearchCompanyNameForm extends ReportForm 
{

	private String contractNumber;
	private String companyName;
	
	
	/**
	 * Gets the contractNumber
	 * @return Returns a String
	 */
	public String getContractNumber() {
		return contractNumber;
	}
	/**
	 * Sets the contractNumber
	 * @param contractNumber The contractNumber to set
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber.trim();
	}

	/**
	 * Gets the companyName
	 * @return Returns a String
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * Sets the companyName
	 * @param companyName The companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName.trim();
	}



}

