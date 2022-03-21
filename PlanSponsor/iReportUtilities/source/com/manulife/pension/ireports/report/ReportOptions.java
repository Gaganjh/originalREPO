package com.manulife.pension.ireports.report;


import java.util.Date;
import java.util.Map;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.model.FundOffering;
import com.manulife.pension.service.fund.dao.HistoricalIreportDAO;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;


public class ReportOptions implements StandardReportsConstants{
	private FundOffering fundOffering;
	private String reportCode;
	private String selectedValue;
	private HistoricalIreportDAO historicalIreportDAO;
	private boolean isSelectedFromHistoricalReport;
	private String classMenu;	
	private String[] fundsChosen;
	private String lifecycleInvestmentid;
	private String lifestylePortfolio;
	private ContractShortlistOptions contractShortlistOptions;
	private boolean includeISF;
	private Map<String, String> reportTitle;
	private Map<String, Fund> contractFundsMap;
	private Date periodendingDate;
	private boolean isMerrillAdvisor;
	
	public ReportOptions(FundOffering fundOffering, String reportCode, String selectedValue,HistoricalIreportDAO historicalIreportDAO, boolean isSelectedFromHistoricalReport, String classMenu,
			Date periodendingDate,String[] fundsChosen, String lifecyclePortfolio, String lifestylePortfolio,
            ContractShortlistOptions contractShortlistOptions, boolean includeISF,
            Map<String, String> reportTitle, Map<String, Fund> contractFundsMap) {
		this.fundOffering = fundOffering;
		this.reportCode = reportCode;
		this.selectedValue = selectedValue;
		this.historicalIreportDAO = historicalIreportDAO;
		this.isSelectedFromHistoricalReport = isSelectedFromHistoricalReport;
		this.periodendingDate = periodendingDate;
		this.classMenu = classMenu;
		this.fundsChosen = fundsChosen;
		this.lifecycleInvestmentid= lifecyclePortfolio;
		this.lifestylePortfolio = lifestylePortfolio;
		this.contractShortlistOptions = contractShortlistOptions;
		this.includeISF = includeISF;
		this.reportTitle = reportTitle;
		this.contractFundsMap = contractFundsMap;
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


	/**
	 * @return the isSelectedFormHistoricalReport
	 */
	public boolean isSelectedFromHistoricalReport() {
		return isSelectedFromHistoricalReport;
	}

	/**
	 * @param isSelectedFormWebPage the isSelectedFormWebPage to set
	 */
	public void setSelectedFromHistoricalReport(boolean isSelectedFromHistoricalReport) {
		this.isSelectedFromHistoricalReport = isSelectedFromHistoricalReport;
	}
	/**
	 * @return the selectedValue
	 */
	public String getSelectedValue() {
		return selectedValue;
	}
	/**
	 * @param selectedValue the selectedValue to set
	 */
	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}
	public FundOffering getFundOffering() {
		return fundOffering;
	}

	public String getCompanyId() {
		return fundOffering.getCompanyId();
	}
	public String[] getFundsChosen() {
		return fundsChosen;
	}
	public void setFundsChosen(String[] fundsChosen) {
		this.fundsChosen = fundsChosen;
	}
	public int getFundMenuId() {
		return fundOffering.getFundMenu();
	}
	public String getClassMenu() {
		return classMenu;
	}
	public void setClassMenu(String classMenu) {
		this.classMenu = classMenu;
	}
	public String getReportCode() {
		return reportCode;
	}
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	
	public String getLifestylePortfolio() {
		return lifestylePortfolio;
	}
	public void setLifestylePortfolio(String lifestylePortfolio) {
		this.lifestylePortfolio = lifestylePortfolio;
	}
	public ContractShortlistOptions getContractShortlistOptions() {
		return contractShortlistOptions;
	}
	public void setContractShortlistOptions(ContractShortlistOptions contractShortlistOptions) {
		this.contractShortlistOptions = contractShortlistOptions;
	}
	public String getLifecycleInvestmentid() {
		return lifecycleInvestmentid;
	}
	public void setLifecycleInvestmentid(String lifecyclePortfolio) {
		this.lifecycleInvestmentid = lifecyclePortfolio;
	}

	public boolean isIncludeISF() {
		return includeISF;
	}

    public Map<String, String> getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(Map<String, String> reportTitle) {
        this.reportTitle = reportTitle;
    }

	/**
	 * @return the contractFundsMap
	 */
	public Map<String, Fund> getContractFundsMap() {
		return contractFundsMap;
	}
	
	public HistoricalIreportDAO gethistoricalIreportDAO() {
		return historicalIreportDAO;
	}
	public void sethistoricalIreportDAO(HistoricalIreportDAO historicalIreportDAO) {
		this.historicalIreportDAO = historicalIreportDAO;
	}

	public boolean isMerrillAdvisor() {
		return isMerrillAdvisor;
	}
	
	public void setMerrillAdvisor(boolean isMerrillAdvisor) {
		this.isMerrillAdvisor = isMerrillAdvisor;
	}
}
