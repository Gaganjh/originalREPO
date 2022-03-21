package com.manulife.pension.ps.web.home;

import com.manulife.pension.ps.web.controller.PsForm;

public class WelcomePageForm extends PsForm {

    private String contractNumber;
    private boolean toolsPageAccess;
    private boolean sumbitDocumentAccess;
    private boolean censusSummaryAccess;
    private boolean csfAccess;
    private boolean planAccess;
    private boolean fundCheckAccess;
	
	
	/**
	 * Gets the contractNumber
	 * @return The contract number as a String value <br/>
	 * 		   Empty String "" if the contract number is null
	 */
	public String getContractNumber() {
		return contractNumber == null ? "" : contractNumber.trim();
	}
	/**
	 * Sets the contractNumber
	 * @param contractNumber The contract number to set
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	/**
	 * @return The number of links/sections to be displayed on the Welcome Page
	 */
	public int getNumberOfLinks() {
        return (toolsPageAccess ? 1 : 0) + (sumbitDocumentAccess ? 1 : 0) + (censusSummaryAccess ? 1 : 0) + 
               (csfAccess ? 1 : 0) + (planAccess ? 1 : 0) + (fundCheckAccess ? 1 : 0);
    }
	/**
	 * @return <strong>true</strong> if at least one of the following links/sections has access
	 * <ol>
   	 * 	<li>Tools</li>
     * 	<li>Submit a Document</li>
     * 	<li>Census information</li>
     * 	<li>Contract service features</li>
     * 	<li>Investment Platform Update</li>
     * 	<li>Plan information</li>
     * </ol>
     * 		   <strong>false</strong> otherwise
	 */
	public boolean isAnyAccess() {
        return (toolsPageAccess || censusSummaryAccess || csfAccess || planAccess || fundCheckAccess || sumbitDocumentAccess);
    }
    public boolean isCensusSummaryAccess() {
        return censusSummaryAccess;
    }
    public void setCensusSummaryAccess(boolean censusSummaryAccess) {
        this.censusSummaryAccess = censusSummaryAccess;
    }
    public boolean isCsfAccess() {
        return csfAccess;
    }
    public void setCsfAccess(boolean csfAccess) {
        this.csfAccess = csfAccess;
    }
    public boolean isToolsPageAccess() {
        return toolsPageAccess;
    }
    public void setToolsPageAccess(boolean toolsPageAccess) {
        this.toolsPageAccess = toolsPageAccess;
    }
    public boolean isPlanAccess() {
        return planAccess;
    }
    public void setPlanAccess(boolean planAccess) {
        this.planAccess = planAccess;
    }
	public boolean isFundCheckAccess() {
		return fundCheckAccess;
	}
	public void setFundCheckAccess(boolean fundCheckAccess) {
		this.fundCheckAccess = fundCheckAccess;
	}
	public boolean isSumbitDocumentAccess() {
		return sumbitDocumentAccess;
	}
	public void setSumbitDocumentAccess(boolean sumbitDocumentAccess) {
		this.sumbitDocumentAccess = sumbitDocumentAccess;
	}
}

