package com.manulife.pension.platform.utility.ireports.valueobject;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.manulife.pension.service.contract.valueobject.Contract;

/**
 * This class will hold the parameters that have been received from the FapForm. These
 * parameters are sent to the i:report PDF generator.
 * 
 * @author harlomte
 * 
 */
public class FundReportParamsHolderVO {

    // As of Date in the online F&P Page
    private Date asOfDate;
    
    //Month End date for Historical IReport
    private Date periodendingDate;
    
	// Group Selected = Asset Class or Risk Return
    private String groupSelected;

    // Report selected = one of the i:reports selected from the Dropdown in F&P page.
    private String reportSelected;

    // The Report Class name that will be used to generate the i:report PDF.
    private String reportClassName;

    // Sub-report selected. This is applicable only for Lifecycle, Lifestyle reports.
    private String subReportSelected;

    // Funds selected by the user in F&P page and which are being displayed in the Main Report in
    // the F&P page.
    private List<String> selectedFundsList;

    // Contract number selected by the user in F&P page.
    private String contractNumberSelected;
    
    // Contract Object of the contract number selected.
    private Contract contractSelected;

    // US / NY Indicator = US if present in US version of Generic F&P page or the contract selected
    // is a US contract. Else, it is NY.
    private String usNyIndicator;
    
    // Holds the company id ('019' or '094')
    private String companyName;

    // This Map contains the Title to be shown in i:report PDF.
    private Map<String, String> reportTitleList;
    
    // ISF Indicator. This is set to true if the user has checked the ISF option in online page.
    private Boolean isfIndicator;
    
    // Additional parameters that we need to pass.

    // Class selected in online F&P page.
    private String classSelected;

    // NML selected by the user in online F&P page.
    private boolean includeNml; 

    // Lifecycle portfolio selected by the user in online F&P page. THis is only applicable when the
    // user has selected the Lifecycle i:report.
    private String lifecyclePortfolio;

    // Lifestyle portfolio selected by the user in online F&P page. THis is only applicable when the
    // user has selected the Lifestyle i:report.
    private String lifestylePortfolio;
    
    // Sub title selected by the user in online F&P page.
    private String subTitle;

    // This parameter is used to tell if the Funds selected by the user match with all the available
    // Funds for a given advanced filter option (All Funds / Retail Funds / Sub-advised Funds /
    // Short List Options / Selected Funds for a Contract)
    private boolean isFundListMatch;
    
    //This parameter is used to set the value 
    private String seletctedGroup;
        
	// This parameter indicates request coming from web-page
	private boolean isSelectedFromHistoricalReport;
	
	private String advisorName;
	private boolean isMerrillAdvisor;
	
   /**
    * 
    * @return isSelectedFromHistoricalReport
    */
	public boolean isSelectedFromHistoricalReport() {
		return isSelectedFromHistoricalReport;
	}
	/**
	 * 
	 * @param isSelectedFromHistoricalReport
	 */
	public void setSelectedFromHistoricalReport(
			boolean isSelectedFromHistoricalReport) {
		this.isSelectedFromHistoricalReport = isSelectedFromHistoricalReport;
	}
	/**
	 * @return the seletctedGroup
	 */
	public String getSeletctedGroup() {
		return seletctedGroup;
	}
	/**
	 * @param seletctedGroup the seletctedGroup to set
	 */
	public void setSeletctedGroup(String seletctedGroup) {
		this.seletctedGroup = seletctedGroup;
	}

	public Date getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Date asOfDate) {
        this.asOfDate = asOfDate;
    }

    public String getGroupSelected() {
        return groupSelected;
    }

    public void setGroupSelected(String groupSelected) {
        this.groupSelected = groupSelected;
    }

    public String getReportSelected() {
        return reportSelected;
    }

    public void setReportSelected(String reportSelected) {
        this.reportSelected = reportSelected;
    }

    public String getReportClassName() {
        return reportClassName;
    }

    public void setReportClassName(String reportClassName) {
        this.reportClassName = reportClassName;
    }

    public String getSubReportSelected() {
        return subReportSelected;
    }

    public void setSubReportSelected(String subReportSelected) {
        this.subReportSelected = subReportSelected;
    }

    public List<String> getSelectedFundsList() {
        return selectedFundsList;
    }

    public void setSelectedFundsList(List<String> selectedFundsList) {
        this.selectedFundsList = selectedFundsList;
    }

    public String getContractNumberSelected() {
        return contractNumberSelected;
    }

    public void setContractNumberSelected(String contractNumberSelected) {
        this.contractNumberSelected = contractNumberSelected;
    }

    public Contract getContractSelected() {
        return contractSelected;
    }

    public void setContractSelected(Contract contractSelected) {
        this.contractSelected = contractSelected;
    }

    public String getUsNyIndicator() {
        return usNyIndicator;
    }

    public void setUsNyIndicator(String usNyIndicator) {
        this.usNyIndicator = usNyIndicator;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Map<String, String> getReportTitleList() {
        return reportTitleList;
    }

    public void setReportTitleList(Map<String, String> reportTitleList) {
        this.reportTitleList = reportTitleList;
    }

    public Boolean getIsfIndicator() {
        return isfIndicator;
    }

    public void setIsfIndicator(Boolean isfIndicator) {
        this.isfIndicator = isfIndicator;
    }

    public String getClassSelected() {
        return classSelected;
    }

    public void setClassSelected(String classSelected) {
        this.classSelected = classSelected;
    }

    public boolean isIncludeNml() {
        return includeNml;
    }

    public void setIncludeNml(boolean includeNml) {
        this.includeNml = includeNml;
    }

    public String getLifecyclePortfolio() {
        return lifecyclePortfolio;
    }

    public void setLifecyclePortfolio(String lifecyclePortfolio) {
        this.lifecyclePortfolio = lifecyclePortfolio;
    }

    public String getLifestylePortfolio() {
        return lifestylePortfolio;
    }

    public void setLifestylePortfolio(String lifestylePortfolio) {
        this.lifestylePortfolio = lifestylePortfolio;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public boolean isFundListMatch() {
        return isFundListMatch;
    }

    public void setFundListMatch(boolean isFundListMatch) {
        this.isFundListMatch = isFundListMatch;
    }
   
    
    /**
	 * @return the periodendingDate
	 */
	public Date getPeriodendingDate() {
		return periodendingDate;
	}
	/**
	 * @param periodendingDate the periodendingDate to set
	 */
	public void setPeriodendingDate(Date periodendingDate) {
		this.periodendingDate = periodendingDate;
	}
	
	public String getAdvisorName() {
		return advisorName;
	}
    
	public void setAdvisorName(String advisorName) {
		this.advisorName = advisorName;
	}
	
	
	public boolean isMerrillAdvisor() {
		return isMerrillAdvisor;
	}
	
	public void setMerrillAdvisor(boolean isMerrillAdvisor) {
		this.isMerrillAdvisor = isMerrillAdvisor;
	}
	
	public String toString() {
        String toStringValue = 
                "<>asOfDate = " + asOfDate + "<>\ngroupSelected = "
                + groupSelected + "<>\nreportSelected = " + reportSelected
                + "<>\nreportClassName = " + reportClassName + "<>\nsubReportSelected = "
                + subReportSelected + "<>\nselectedFundsList = " + selectedFundsList
                + "<>\ncontractNumberSelected = " + contractNumberSelected
                + "<>\ncontractSelected = " + contractSelected + "<>\nusNyIndicator = "
                + usNyIndicator + "<>\nreportTitleList = " + reportTitleList
                + "<>\nisfIndicator = " + isfIndicator + "<>\nclassSelected = " + classSelected
                + "<>\nincludeNml = " + includeNml + "<>\nisMerrillAdvisor = " + isMerrillAdvisor + "<>\nlifecyclePortfolio = "
                + lifecyclePortfolio + "<>\nlifestylePortfolio = " + lifestylePortfolio
                + "<>\nsubTitle = " + subTitle + "<>\nisFundListMatch = " + isFundListMatch;
        
        return toStringValue;
    }
    
}
