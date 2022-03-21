package com.manulife.pension.ireports.report.model;

import java.math.BigDecimal;
import java.util.Date;

import com.manulife.pension.service.fund.standardreports.valueobject.FundExpenses;

public class NullFundExpenses extends FundExpenses {

	public void setAnnualInvestmentCharge(BigDecimal annualInvestmentCharge) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setEffectiveDate(Date effectiveDate) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setFundCd(String fundCd) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setFundExpenseRatio(BigDecimal fundExpenseRatio) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setInvestmentid(String investmentid) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setRateType(String rateType) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

}
