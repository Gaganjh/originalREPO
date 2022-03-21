package com.manulife.pension.ireports.report.viewbean;

import com.manulife.pension.ireports.report.model.NullFundStandardDeviation;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.service.fund.standardreports.valueobject.FundStandardDeviation;

public class FundStandardDeviationViewBean {

	public static final NullFundStandardDeviation NULL_FUND_STANDARD_DEVIATION = new NullFundStandardDeviation(); 
	
	private FundStandardDeviation standardDeviations;

	public FundStandardDeviationViewBean(FundStandardDeviation standardDeviations) {
		this.standardDeviations = standardDeviations;
		if (standardDeviations == null) {
			this.standardDeviations = NULL_FUND_STANDARD_DEVIATION;
		}
	}

	public String getEffectiveDate() {
		return StandardReportsUtils.formatFundDataDate(standardDeviations.getEffectivedate());
	}

	public String getFundCd() {
		return standardDeviations.getFundCd();
	}

	public String getRateType() {
		return StandardReportsUtils.formatFundDataString(standardDeviations.getRateType());
	}

	public String getRor10yr() {
		return StandardReportsUtils.formatPercentage(standardDeviations.getRor10yr());
	}

	public String getRor3yr() {
		return StandardReportsUtils.formatPercentage(standardDeviations.getRor3yr());
	}

	public String getRor5yr() {
		return StandardReportsUtils.formatPercentage(standardDeviations.getRor5yr());
	}

	public boolean isRor10yrHypothetical() {
		return standardDeviations.isRor10yrHypothetical();
	}

	public boolean isRor3yrHypothetical() {
		return standardDeviations.isRor3yrHypothetical();
	}

	public boolean isRor5yrHypothetical() {
		return standardDeviations.isRor5yrHypothetical();
	}
}
