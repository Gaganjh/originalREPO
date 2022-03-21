package com.manulife.pension.ireports.model.report;

import java.util.List;
import java.util.Map;

public class LifestyleReportData {
	private List funds;
	private Map footnotes;
	
	private Map marketIndexIbPerformances;		// MarketIndexIbPerformances keyed by market index id
	private List<String> feeWaiverFundIds;
	
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
	
	public Map getMarketIndexIbPerformances() {
		return marketIndexIbPerformances;
	}
	public void setMarketIndexIbPerformances(Map marketIndexIbPerformances) {
		this.marketIndexIbPerformances = marketIndexIbPerformances;
	}
	
	public List<String> getFeeWaiverFundIds() {
		return feeWaiverFundIds;
	}
	
	public void setFeeWaiverFundIds(List<String> feeWaiverFundIds) {
		this.feeWaiverFundIds = feeWaiverFundIds;
	}
}
