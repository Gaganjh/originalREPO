package com.manulife.pension.ireports.report.viewbean;

import com.manulife.pension.ireports.report.model.NullMorningstarCategoryPerformance;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.service.fund.standardreports.valueobject.MorningstarCategoryPerformance;

public class MorningstarCategoryPerformanceViewBean {

	private MorningstarCategoryPerformance performance;

	public MorningstarCategoryPerformanceViewBean(MorningstarCategoryPerformance performance) {
		this.performance = performance;
		if (performance == null) {
			this.performance = new NullMorningstarCategoryPerformance();
		}
	}

	public String getEffectiveDate() {
		return StandardReportsUtils.formatFundDataDate(performance.getEffectiveDate());
	}

	public String getExpenseRatio() {
		return StandardReportsUtils.formatPercentage(performance.getExpenseRatio());
	}

	public String getExpenseRatioQe() {
		return StandardReportsUtils.formatPercentage(performance.getExpenseRatioQe());
	}

	public String getMorningstarCategoryName() {
		return StandardReportsUtils.formatFundDataString(performance.getMorningstarCategoryName());
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
		return StandardReportsUtils.formatPercentage(performance
				.getRor_10YearQe());
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
		return StandardReportsUtils.formatPercentage(performance.getStandardDeviation_10Year());
	}

	public String getStandardDeviation_3YearQe() {
		return StandardReportsUtils.formatPercentage(performance.getStandardDeviation_3Year());
	}

	public String getStandardDeviation_5YearQe() {
		return StandardReportsUtils.formatPercentage(performance.getStandardDeviation_5Year());
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof MorningstarCategoryPerformanceViewBean)) {
			return false;
		}
		MorningstarCategoryPerformanceViewBean otherViewBean = 
			(MorningstarCategoryPerformanceViewBean)obj;
		return this.performance.equals(otherViewBean.performance);
	}
	
	public int hashCode() {
		return performance.hashCode();
	}
}
