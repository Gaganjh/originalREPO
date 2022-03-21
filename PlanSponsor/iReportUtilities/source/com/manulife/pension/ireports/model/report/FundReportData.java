package com.manulife.pension.ireports.model.report;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.manulife.pension.service.fund.standardreports.valueobject.CurrentAsOfDate;
import com.manulife.pension.service.fund.valueobject.Fund;
public class FundReportData {
	private List funds;
	private Map guaranteedAccountRates;
	private Map footnotes;
	private Map marketIndexIbPerformances;
	private Map morningstarCategoryPerformances;
	
	//these categorization maps do not depend on which funds/series were chosen.
	private Map investmentGroups;
	private Map assetClasses;
	private Map assetCategoryGroups;
	private Map assetCategories;
	private Map<String, CurrentAsOfDate> currentAsOfDates;
	private Map morningstarFootNotes;
	private Map<String, String> morningstarStaticFootNotes;
	private Map companyIdFootnotes;
	//changes done as part of ACR REWRITE (historical iReport changes)
	private Map<String, String> marketIndexFootnote;
	private Map footnotesByCompany;
	private List<String> feeWaiverFundIds;
	private Map<String, Fund>restrictedFunds;
	private boolean isMerrillAdvisor;
	
	private boolean isSelectedFromHistoricalReport;
	
	
	/**
	 * @return the isSelectedFromHistoricalReport
	 */
	public boolean isSelectedFromHistoricalReport() {
		return isSelectedFromHistoricalReport;
	}
	/**
	 * @param isSelectedFromHistoricalReport the isSelectedFromHistoricalReport to set
	 */
	public void setSelectedFromHistoricalReport(
			boolean isSelectedFromHistoricalReport) {
		this.isSelectedFromHistoricalReport = isSelectedFromHistoricalReport;
	}
	
	public List getFunds() {
		return funds;
	}
	public void setFunds(List funds) {
		this.funds = funds;
	}
	
	public Map getFootnotes() {
		return footnotes;
	}
	public void setFootnotes(Map footnotes) {
		this.footnotes = footnotes;
	}
	
	public Map getGuaranteedAccountRates() {
		return guaranteedAccountRates;
	}
	public void setGuaranteedAccountRates(Map guaranteedAccountRates) {
		this.guaranteedAccountRates = guaranteedAccountRates;
	}
	
	public Map getInvestmentGroups() {
		return investmentGroups;
	}
	public void setInvestmentGroups(Map investmentGroups) {
		this.investmentGroups = investmentGroups;
	}
	
	/**
	 * retrieves all asset classes sorted by AssetClass.order property.
	 * 
	 * @return
	 */
	public Map getAssetClasses() {
		return assetClasses;
	}
	public void setAssetClasses(Map assetClasses) {
		this.assetClasses = assetClasses;
	}
	
	public Map getAssetCategories() {
		return assetCategories;
	}
	public void setAssetCategories(Map assetCategories) {
		this.assetCategories = assetCategories;
	}
	
	public Map getAssetCategoryGroups() {
		return assetCategoryGroups;
	}
	public void setAssetCategoryGroups(Map assetCategoryGroups) {
		this.assetCategoryGroups = assetCategoryGroups;
	}
	
	public Map getMarketIndexIbPerformances() {
		return marketIndexIbPerformances;
	}
	public void setMarketIndexIbPerformances(Map marketIndexIbPerformances) {
		this.marketIndexIbPerformances = marketIndexIbPerformances;
	}
	
	public Map getMorningstarCategoryPerformances() {
		return morningstarCategoryPerformances;
	}
	public void setMorningstarCategoryPerformances(Map morningstarCategoryPerformances) {
		this.morningstarCategoryPerformances = morningstarCategoryPerformances;
	}
    public Map<String, CurrentAsOfDate> getCurrentAsOfDates() {
        return currentAsOfDates;
    }

    public void setCurrentAsOfDates(Map<String, CurrentAsOfDate> currentAsOfDates) {
        this.currentAsOfDates = currentAsOfDates;
    }
    
    /**
     * This is a helper method that returns the Date, given the context.
     * 
     * @param context
     * @return
     */
    public Date getAsOfDateForContext(String context) {
        if (currentAsOfDates == null) {
            return null;
        }

        CurrentAsOfDate currentAsOfDate = currentAsOfDates.get(context);
        return currentAsOfDate == null ? null : currentAsOfDate.getAsofdate();
    }
    
	/**
	 * @return the morningstarFootNotes
	 */
	public Map getMorningstarFootNotes() {
		return morningstarFootNotes;
	}
	
	/**
	 * @param morningstarFootNotes the morningstarFootNotes to set
	 */
	public void setMorningstarFootNotes(Map morningstarFootNotes) {
		this.morningstarFootNotes = morningstarFootNotes;
	}
	
	/**
	 * @return the morningstarStaticFootNotes
	 */
	public Map<String, String> getMorningstarStaticFootNotes() {
		return morningstarStaticFootNotes;
	}
	
	/**
	 * @param morningstarStaticFootNotes the morningstarStaticFootNotes to set
	 */
	public void setMorningstarStaticFootNotes(Map<String, String> morningstarStaticFootNotes) {
		this.morningstarStaticFootNotes = morningstarStaticFootNotes;
	}
	
	/**
	 * @return the companyIdFootnotes
	 */
	public Map getCompanyIdFootnotes() {
		return companyIdFootnotes;
	}
	
	/**
	 * @param companyIdFootnotes the companyIdFootnotes to set
	 */
	public void setCompanyIdFootnotes(Map companyIdFootnotes) {
		this.companyIdFootnotes = companyIdFootnotes;
	}
	/**
	 * @return the footnotesByCompany
	 */
	public Map getFootnotesByCompany() {
		return footnotesByCompany;
	}


	/**
	 * @param footnotesByCompany the footnotesByCompany to set
	 */
	public void setFootnotesByCompany(Map footnotesByCompany) {
		this.footnotesByCompany = footnotesByCompany;
	}

	/**
	 * @return the marketIndexFootnote
	 */
	public Map<String, String> getMarketIndexFootnote() {
		return marketIndexFootnote;
	}

	/**
	 * @param marketIndexFootnote the marketIndexFootnote to set
	 */
	public void setMarketIndexFootnote(Map<String, String> marketIndexFootnote) {
		this.marketIndexFootnote = marketIndexFootnote;
	}
	
	public List<String> getFeeWaiverFundIds() {
		return feeWaiverFundIds;
	}
	
	public void setFeeWaiverFundIds(List<String> feeWaiverFundIds) {
		this.feeWaiverFundIds = feeWaiverFundIds;
	}


	public Map<String, Fund> getRestrictedFunds() {
		return restrictedFunds;
	}
	
	public void setRestrictedFunds(Map<String, Fund> restrictedFunds) {
		this.restrictedFunds = restrictedFunds;
	}
	
	public boolean isMerrillAdvisor() {
		return isMerrillAdvisor;
	}
	
	public void setMerrillAdvisor(boolean isMerrillAdvisor) {
		this.isMerrillAdvisor = isMerrillAdvisor;
	}
}
