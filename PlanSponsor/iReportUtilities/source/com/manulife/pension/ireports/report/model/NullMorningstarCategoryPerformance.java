package com.manulife.pension.ireports.report.model;

import java.math.BigDecimal;
import java.util.Date;

import com.manulife.pension.service.fund.standardreports.valueobject.MorningstarCategoryPerformance;

public class NullMorningstarCategoryPerformance extends MorningstarCategoryPerformance {

	public void setEffectiveDate(Date effectiveDate) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setExpenseRatio(BigDecimal expenseRatio) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setMorningstarCategoryId(String morningstarCategoryId) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setMorningstarCategoryName(String morningstarCategoryName) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setRor_10Year(BigDecimal ror_10Year) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setRor_1Month(BigDecimal ror_1Month) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setRor_1Year(BigDecimal ror_1Year) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setRor_3Month(BigDecimal ror_3Month) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setRor_3Year(BigDecimal ror_3Year) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setRor_5Year(BigDecimal ror_5Year) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setRorYtd(BigDecimal rorYtd) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setStandardDeviation_10Year(BigDecimal standardDeviation_10Year) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setStandardDeviation_3Year(BigDecimal standardDeviation_3Year) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

	public void setStandardDeviation_5Year(BigDecimal standardDeviation_5Year) {
		throw new UnsupportedOperationException("cannot modify null object state");
	}

}
