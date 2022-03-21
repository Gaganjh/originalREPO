package com.manulife.pension.ireports.model.report;

import java.util.Map;

public class MarketReportData {
	private Map assetCategoryGroups;
	private Map assetCategories;
	private Map marketIndexIbPerformances;
	private Map footnotes;
	
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
	
	public Map getFootnotes() {
		return footnotes;
	}
	public void setFootnotes(Map footnotes) {
		this.footnotes = footnotes;
	}
}
