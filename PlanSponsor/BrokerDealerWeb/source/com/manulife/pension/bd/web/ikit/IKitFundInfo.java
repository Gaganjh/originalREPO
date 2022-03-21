package com.manulife.pension.bd.web.ikit;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.content.valueobject.Footnote;

/**
 * This class will hold all the Fund related information for contract-selected
 * funds. The information in this VO will be sent to the iKit application.
 * 
 * @author harlomte
 * 
 */
public class IKitFundInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// This will hold the time the iKitFundInfo object was created.
	private Date createdTs;

	/* This will hold the errors encountered during fetching the Fund Information.
	 *    -- key is the error code.
	 *    -- value is the error message.
	 */
	private Map<String, String> errors = new HashMap<String, String>();

	// This will hold the F&P report as of date.
	private Date reportAsOfDate;

	/*
	 * This will hold the as of dates for different contexts.
	 *    -- key is the context name.
	 *    -- value is the as of date for that context.
	 */
	private Map<String, Date> asOfDates;

	// This will hold the Fund Id's in sorted order for each asset class, for
	// contract-selected funds.
	private List<AssetClassOrRiskReturn> fundsByAssetClasses;

	// This will hold the Fund Id's in sorted order for each investment
	// category, for contract-selected funds.
	private List<AssetClassOrRiskReturn> fundsByInvestmentCategories;

	// This will hold the Fund Information for each contract-selected fund
	// (except the Guaranteed Account Funds and the Market Index Funds).
	private List<ReturnsAndFeesVO> fundDetailsList;

	// This will hold the Guaranteed Account Funds information for contract-selected funds.
	private GARateVO gaRateVO;

	// This will hold the Fund Information for each contract-selected Market Index Fund.
	private List<ReturnsAndFeesVO> marketIndexFundDetailsList;

	// This will hold the page Footer.
	private String pageFooter;

	// This will hold the page Footnotes.
	private String pageFootnotes;

	// This will hold the page Disclaimer.
	private String pageDisclaimer;
	
	// This will hold the fee waiver funds.
	private List<String> feeWaiverFunds;
	
	// This will hold the fee waiver Disclaimer.
	private String feeWaiverDisclaimer;

	// This will hold the Fund footnotes in sorted order.
	private List<Footnote> sortedFootnotes;

	public Date getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

	public Map<String, Date> getAsOfDates() {
		return asOfDates;
	}

	public void setAsOfDates(Map<String, Date> asOfDates) {
		this.asOfDates = asOfDates;
	}

	public Date getReportAsOfDate() {
		return reportAsOfDate;
	}

	public void setReportAsOfDate(Date reportAsOfDate) {
		this.reportAsOfDate = reportAsOfDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<AssetClassOrRiskReturn> getFundsByAssetClasses() {
		return fundsByAssetClasses;
	}

	public void setFundsByAssetClasses(
			List<AssetClassOrRiskReturn> fundsByAssetClasses) {
		this.fundsByAssetClasses = fundsByAssetClasses;
	}

	public List<AssetClassOrRiskReturn> getFundsByInvestmentCategories() {
		return fundsByInvestmentCategories;
	}

	public void setFundsByInvestmentCategories(
			List<AssetClassOrRiskReturn> fundsByInvestmentCategories) {
		this.fundsByInvestmentCategories = fundsByInvestmentCategories;
	}

	public List<ReturnsAndFeesVO> getFundDetailsList() {
		return fundDetailsList;
	}

	public void setFundDetailsList(List<ReturnsAndFeesVO> fundDetailsList) {
		this.fundDetailsList = fundDetailsList;
	}

	public GARateVO getGaRateVO() {
		return gaRateVO;
	}

	public void setGaRateVO(GARateVO gaRateVO) {
		this.gaRateVO = gaRateVO;
	}

	public List<ReturnsAndFeesVO> getMarketIndexFundDetailsList() {
		return marketIndexFundDetailsList;
	}

	public void setMarketIndexFundDetailsList(
			List<ReturnsAndFeesVO> marketIndexFundDetailsList) {
		this.marketIndexFundDetailsList = marketIndexFundDetailsList;
	}

	public String getPageFooter() {
		return pageFooter;
	}

	public void setPageFooter(String pageFooter) {
		this.pageFooter = pageFooter;
	}

	public String getPageFootnotes() {
		return pageFootnotes;
	}

	public void setPageFootnotes(String pageFootnotes) {
		this.pageFootnotes = pageFootnotes;
	}

	public String getPageDisclaimer() {
		return pageDisclaimer;
	}

	public void setPageDisclaimer(String pageDisclaimer) {
		this.pageDisclaimer = pageDisclaimer;
	}

	public List<Footnote> getSortedFootnotes() {
		return sortedFootnotes;
	}

	public void setSortedFootnotes(List<Footnote> sortedFootnotes) {
		this.sortedFootnotes = sortedFootnotes;
	}
	
	public List<String> getFeeWaiverFunds() {
		return feeWaiverFunds;
	}

	public void setFeeWaiverFunds(List<String> feeWaiverFunds) {
		this.feeWaiverFunds = feeWaiverFunds;
	}
	
	public String getFeeWaiverDisclaimer() {
		return feeWaiverDisclaimer;
	}

	public void setFeeWaiverDisclaimer(String feeWaiverDisclaimer) {
		this.feeWaiverDisclaimer = feeWaiverDisclaimer;
	}
}