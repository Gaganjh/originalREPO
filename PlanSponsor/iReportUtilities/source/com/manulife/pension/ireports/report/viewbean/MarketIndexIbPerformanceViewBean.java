package com.manulife.pension.ireports.report.viewbean;

import com.manulife.pension.ireports.report.model.NullMarketIndexIbPerformance;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.valueobject.MarketIndexIbPerformance;

public class MarketIndexIbPerformanceViewBean {

	private static final MarketIndexIbPerformance NULL_MARKET_INDEX_IB_PERFORMANCE = 
		new NullMarketIndexIbPerformance();
	
	private MarketIndexIbPerformance performance;
	private Fund fund;

	public MarketIndexIbPerformanceViewBean(MarketIndexIbPerformance performance, Fund fund) {
		this.performance = performance;
		this.fund = fund;
		if (performance == null) {
			performance = NULL_MARKET_INDEX_IB_PERFORMANCE;
		}
	}
	
	public String getFundAssetCls() {
		return fund.getAssetcls();
	}
	
	public String getMarketIndexId() {
		return performance.getMarketIndexId();
	}

	public String getEffectiveDate() {
		return StandardReportsUtils.formatFundDataDate(performance.getEffectiveDate());
	}

	public String getMarketIndexName() {
		return StandardReportsUtils.formatFundDataString(performance.getMarketIndexName());
	}

	public String getRor_10Year() {
		return StandardReportsUtils.formatPercentage(performance.getRor_10Year());
	}

	public String getRor_1Month() {
		return StandardReportsUtils.formatPercentage(performance.getRor_1Month());
	}

	public String getRor_1Year() {
		return StandardReportsUtils.formatPercentage(performance.getRor_1Year());
	}

	public String getRor_3Month() {
		return StandardReportsUtils.formatPercentage(performance.getRor_3Month());
	}

	public String getRor_3Year() {
		return StandardReportsUtils.formatPercentage(performance.getRor_3Year());
	}

	public String getRor_5Year() {
		return StandardReportsUtils.formatPercentage(performance.getRor_5Year());
	}

	public String getRor_1YearQe() {
		return StandardReportsUtils.formatPercentage(performance.getRor_1YearQe());
	}

	public String getRor_3YearQe() {
		return StandardReportsUtils.formatPercentage(performance.getRor_3YearQe());
	}

	public String getRor_5YearQe() {
		return StandardReportsUtils.formatPercentage(performance.getRor_5YearQe());
	}

	public String getRor_10YearQe() {
		return StandardReportsUtils.formatPercentage(performance.getRor_10YearQe());
	}

	public String getRorYtd() {
		return StandardReportsUtils.formatPercentage(performance.getRorYtd());
	}

	public String getStandardDeviation_10Year() {
		return StandardReportsUtils.formatPercentage(performance.getStandardDeviation_10Year());
	}

	public String getStandardDeviation_3Year() {
		return StandardReportsUtils.formatPercentage(performance.getStandardDeviation_3Year());
	}

	public String getStandardDeviation_5Year() {
		return StandardReportsUtils.formatPercentage(performance.getStandardDeviation_5Year());
	}
	
	public String getStandardDeviation_10YearQe() {
		return StandardReportsUtils.formatPercentage(performance.getStandardDeviation_10YearQe());
	}

	public String getStandardDeviation_3YearQe() {
		return StandardReportsUtils.formatPercentage(performance.getStandardDeviation_3YearQe());
	}

	public String getStandardDeviation_5YearQe() {
		return StandardReportsUtils.formatPercentage(performance.getStandardDeviation_5YearQe());
	}
	
	/**
	 * @return String FundFamilyCd value
	 */
	public String getFundFamilyCd(){
		return fund.getFundFamilyCd();
	}
	
	/**
	 * @return String Investmentid value
	 */
	public String getInvestmentid(){
		return fund.getInvestmentid();
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof MarketIndexIbPerformanceViewBean)) {
			return false;
		}
		MarketIndexIbPerformanceViewBean otherViewBean = 
			(MarketIndexIbPerformanceViewBean)obj;
		return this.performance.equals(otherViewBean.performance);
	}
	
	public int hashCode() {
		return performance.hashCode();
	}
}
