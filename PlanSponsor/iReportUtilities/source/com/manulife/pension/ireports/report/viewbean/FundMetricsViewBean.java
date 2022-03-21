package com.manulife.pension.ireports.report.viewbean;

import java.math.BigDecimal;

import com.manulife.pension.ireports.report.model.NullFundMetrics;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.service.fund.standardreports.valueobject.FundMetrics;

public class FundMetricsViewBean {
	
	public static final NullFundMetrics NULL_FUND_METRICS = new NullFundMetrics();
	
	private FundMetrics fundMetrics;
	
	public FundMetricsViewBean(FundMetrics fundMetrics) {
		this.fundMetrics = fundMetrics;
		if (this.fundMetrics == null) {
			this.fundMetrics = NULL_FUND_METRICS;
		}
	}
	
	public String getAlpha() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getAlpha());
	}
	
	public String getAlphaPercent() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getAlphaPercent());
	}
	public String getBeta() {
		return StandardReportsUtils.formatFundDataDecimal(fundMetrics.getBeta());
	}
	
	public String getBetaPercent() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getBetaPercent());
	}
	public String getEffectivedate() {
		return StandardReportsUtils.formatFundDataDate(fundMetrics.getEffectivedate());					
	}

	public String getExpenseratio() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getExpenseratio());
	}
	
	public String getFees() {
		return StandardReportsUtils.formatFundDataDecimal(fundMetrics.getFees());
	}

	public String getFeesPercent() {
		return StandardReportsUtils.formatPercentageZeroToOneRange(fundMetrics.getFeesPercent());
	}
	
	public String getFundInvestmentid() {
		return fundMetrics.getFundInvestmentid();
	}
	
	public String getInceptionDate() {
		return StandardReportsUtils.formatFundDataDate(fundMetrics.getInceptionDate());
	}
	
	public String getInfoRatio() {
		return StandardReportsUtils.formatFundDataDecimal(fundMetrics.getInfoRatio());
	}
	
	public String getInfoRatioPercent() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getInfoRatioPercent());
	}
	
	public String getRatetype() {
		return StandardReportsUtils.formatFundDataString(fundMetrics.getRatetype());
	}

	public String getRor10yr() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getRor10yr());
	}
	public String getRor1mth() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getRor1mth());
	}
	public String getRor1yr() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getRor1yr());
	}
	public String getRor3mth() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getRor3mth());
	}
	public String getRor3yr() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getRor3yr());
	}
	public String getRor5yr() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getRor5yr());
	}
	public String getRorSinceInception() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getRorSinceInception());
	}
	public String getRorytd() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getRorytd());
	}
	public String getRSquared() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getRSquared());
	}
	public String getRSquaredPercent() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getRSquaredPercent());
	}
	public String getSharpeRatio() {
		return StandardReportsUtils.formatFundDataDecimal(fundMetrics.getSharpeRatio());
	}
	public String getSharpeRatioPercent() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getSharpeRatioPercent());
	}
	public String getStandardDeviation() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getStandardDeviation());
	}
	public String getStandardDeviationPercent() {
		return StandardReportsUtils.formatPercentageZeroToOneRange(fundMetrics.getStandardDeviationPercent());
	}
	public String getTotalReturn() {
		return StandardReportsUtils.formatPercentage(fundMetrics.getTotalReturn());
	}
	public String getTotalReturnPercent() {
		return StandardReportsUtils.formatPercentageZeroToOneRange(fundMetrics.getTotalReturnPercent());
	}

	public boolean isRor10yrHypothetical() {
		return fundMetrics.isRor10yrHypothetical();
	}

	public boolean isRor1mthHypothetical() {
		return fundMetrics.isRor1mthHypothetical();
	}

	public boolean isRor1yrHypothetical() {
		return fundMetrics.isRor1yrHypothetical();
	}

	public boolean isRor3mthHypothetical() {
		return fundMetrics.isRor3mthHypothetical();
	}

	public boolean isRor3yrHypothetical() {
		return fundMetrics.isRor3yrHypothetical();
	}

	public boolean isRor5yrHypothetical() {
		return fundMetrics.isRor5yrHypothetical();
	}

	public boolean isRorytdHypothetical() {
		return fundMetrics.isRorytdHypothetical();
	}

	public boolean isRorSinceInceptionHypothetical() {
		return fundMetrics.isRorSinceInceptionHypothetical();
	}
	
	public String getRor1yrqend() {
	    return StandardReportsUtils.formatPercentage(fundMetrics.getRor1yrqend());
	}
	
	public String getRor3yrqend() {
        return StandardReportsUtils.formatPercentage(fundMetrics.getRor3yrqend());
    }

	public String getRor5yrqend() {
	    return StandardReportsUtils.formatPercentage(fundMetrics.getRor5yrqend());
	}

	public String getRor10yrqend() {
	    return StandardReportsUtils.formatPercentage(fundMetrics.getRor10yrqend());
	}

	public String getRorSinceInceptionqend() {
	    return StandardReportsUtils.formatPercentage(fundMetrics.getRorSinceInceptionqend());
	}
	
	public boolean isRor1yrqendHypothetical() {
        return fundMetrics.isRor1yrqendHypothetical();
    }
	
	public boolean isRor3yrqendHypothetical() {
        return fundMetrics.isRor3yrqendHypothetical();
    }
	
	public boolean isRor5yrqendHypothetical() {
        return fundMetrics.isRor5yrqendHypothetical();
    }
	
	public boolean isRor10yrqendHypothetical() {
        return fundMetrics.isRor10yrqendHypothetical();
    }
	
	public boolean getRorSinceInceptionqendHypothetical() {
		return fundMetrics.isRorSinceInceptionqendHypothetical();
    }

}
