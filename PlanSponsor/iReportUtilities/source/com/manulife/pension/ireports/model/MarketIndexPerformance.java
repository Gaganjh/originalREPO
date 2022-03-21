package com.manulife.pension.ireports.model;

import java.math.BigDecimal;
import java.util.Date;

import com.manulife.pension.ireports.StandardReportsConstants;

/**
 * Some of these variables could be null. 
 *
 */

public class MarketIndexPerformance implements StandardReportsConstants {
	private String fundInvestmentid;
	private String ratetype;
	private Date effectivedate;
	private BigDecimal ror1mth;
	private BigDecimal ror3mth;
	private BigDecimal rorytd;
	private BigDecimal ror1yr;
	private BigDecimal ror3yr;
	private BigDecimal ror5yr;
	private BigDecimal ror10yr;
	
	public Date getEffectivedate() {
		return effectivedate;
	}
	public void setEffectivedate(Date effectivedate) {
		this.effectivedate = effectivedate;
	}
	public String getFundInvestmentid() {
		return fundInvestmentid;
	}
	public void setFundInvestmentid(String fundInvestmentid) {
		this.fundInvestmentid = fundInvestmentid;
	}
	public String getRatetype() {
		return ratetype;
	}
	public void setRatetype(String ratetype) {
		this.ratetype = ratetype;
	}
	public BigDecimal getRor10yr() {
		return ror10yr;
	}
	public void setRor10yr(BigDecimal ror10yr) {
		this.ror10yr = ror10yr;
	}
	public BigDecimal getRor1mth() {
		return ror1mth;
	}
	public void setRor1mth(BigDecimal ror1mth) {
		this.ror1mth = ror1mth;
	}
	public BigDecimal getRor1yr() {
		return ror1yr;
	}
	public void setRor1yr(BigDecimal ror1yr) {
		this.ror1yr = ror1yr;
	}
	public BigDecimal getRor3mth() {
		return ror3mth;
	}
	public void setRor3mth(BigDecimal ror3mth) {
		this.ror3mth = ror3mth;
	}
	public BigDecimal getRor3yr() {
		return ror3yr;
	}
	public void setRor3yr(BigDecimal ror3yr) {
		this.ror3yr = ror3yr;
	}
	public BigDecimal getRor5yr() {
		return ror5yr;
	}
	public void setRor5yr(BigDecimal ror5yr) {
		this.ror5yr = ror5yr;
	}
	public BigDecimal getRorytd() {
		return rorytd;
	}
	public void setRorytd(BigDecimal rorytd) {
		this.rorytd = rorytd;
	}

}
