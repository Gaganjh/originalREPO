package com.manulife.pension.ireports.report.model;

import java.math.BigDecimal;
import java.util.Date;

import com.manulife.pension.service.fund.standardreports.valueobject.FundStandardDeviation;

public final class NullFundStandardDeviation extends FundStandardDeviation {

	public void setEffectivedate(Date effectiveDate) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setFundCd(String fundCd) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setRateType(String rateType) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setRor10Yr(BigDecimal standardDeviation_10Year) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setRor3Yr(BigDecimal standardDeviation_3Year) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setRor5Yr(BigDecimal standardDeviation_5Year) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	
	
}
