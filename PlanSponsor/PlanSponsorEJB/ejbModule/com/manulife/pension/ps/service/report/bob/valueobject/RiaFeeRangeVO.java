package com.manulife.pension.ps.service.report.bob.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * This class holds VO information to be shown as a row overlay
 */
public class RiaFeeRangeVO implements Serializable {

	private static final long serialVersionUID = 385934334577626810L;

	private BigDecimal minAmt;
	private BigDecimal maxAmt;
	private BigDecimal bandRate;

	
	public void setMinAmt(BigDecimal minAmt) {
		this.minAmt = minAmt;
	}

	public BigDecimal getMaxAmt() {
		return maxAmt;
	}

	public void setMaxAmt(BigDecimal maxAmt) {
		this.maxAmt = maxAmt;
	}
	public BigDecimal getBandRate() {
		return bandRate;
	}

	public void setBandRate(BigDecimal bandRate) {
		this.bandRate = bandRate;
	}

	public BigDecimal getMinAmt() {
		return minAmt;
	}
}
