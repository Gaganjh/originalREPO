package com.manulife.pension.ps.service.report.bob.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * This class holds VO information to be shown as a row overlay
 */
public class RiaFeeDetailsVO implements Serializable {

	private static final long serialVersionUID = 385934334577626810L;

	private BigDecimal riaBpsPercentage;
	private BigDecimal riaBpsMaxAmt;
	private BigDecimal riaBlendRate;
	private BigDecimal riaTieredRate;
	
	public BigDecimal getRiaBpsPercentage() {
		return riaBpsPercentage;
	}

	public void setRiaBpsPercentage(BigDecimal riaBpsPercentage) {
		this.riaBpsPercentage = riaBpsPercentage;
	}

	public BigDecimal getRiaBpsMaxAmt() {
		return riaBpsMaxAmt;
	}

	public void setRiaBpsMaxAmt(BigDecimal riaBpsMaxAmt) {
		this.riaBpsMaxAmt = riaBpsMaxAmt;
	}

	public BigDecimal getRiaBlendRate() {
		return riaBlendRate;
	}

	public void setRiaBlendRate(BigDecimal riaBlendRate) {
		this.riaBlendRate = riaBlendRate;
	}

	public BigDecimal getRiaTieredRate() {
		return riaTieredRate;
	}

	public void setRiaTieredRate(BigDecimal riaTieredRate) {
		this.riaTieredRate = riaTieredRate;
	}

}
