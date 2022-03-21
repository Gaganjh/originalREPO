package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

import com.manulife.pension.ps.service.report.transaction.reporthandler.UncashedChecksReportHandler;


/**
 * @author suresh l
 * 
 * ReportData object for Uncashed checks summary page. Holds collection of UncashedChecksReportItem objects (as 
 * "details") as well as totals/header  - UncashedChecks value, etc.,
 *
 */
public class UncashedChecksReportData extends ReportData implements Serializable {
	
	public static final String REPORT_ID = UncashedChecksReportHandler.class.getName();
	
	public static final String REPORT_NAME = "UncashedCheck"; 
	
	public static final String FILTER_CONTRACT_NO = "contractNumber";
	
	private Date asOfDate;
	private BigDecimal outstandingChecksValue;
	private int numStaleDatedChecks;
	private int numOutstandingChecks;
	private BigDecimal staleDatedChecksValue;
	private BigDecimal uncashedChecksValue;
	
	
	public UncashedChecksReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
		this.details = new ArrayList(0); // a graceful way to report "No matches"
	}
	
	public boolean isInMaintenance() { return asOfDate == null; }
	
	/**
	 * @return
	 */
	public Date getAsOfDate() {
		return asOfDate;
	}

	/**
	 * @param date
	 */
	public void setAsOfDate(Date date) {
		asOfDate = date;
	}

	/**
	 * @return
	 */
	public BigDecimal getOutstandingChecksValue() {
		return outstandingChecksValue;
	}

	/**
	 * @param BigDecimal
	 */
	public void setOutstandingChecksValue(BigDecimal outstandingChecksValue) {
		this.outstandingChecksValue = outstandingChecksValue;
	}
	/**
	 * @return
	 */
	public int getNumStaleDatedChecks() {
		return numStaleDatedChecks;
	}

	/**
	 * @param int
	 */
	public void setNumStaleDatedChecks(int numStaleDatedChecks) {
		this.numStaleDatedChecks = numStaleDatedChecks;
	}
	/**
	 * @return
	 */
	public int getNumOutstandingChecks() {
		return numOutstandingChecks;
	}

	/**
	 * @param int
	 */
	public void setNumOutstandingChecks(int numOutstandingChecks) {
		this.numOutstandingChecks = numOutstandingChecks;
	}
	/**
	 * @return
	 */
	public BigDecimal getStaleDatedChecksValue() {
		return staleDatedChecksValue;
	}

	/**
	 * @param BigDecimal
	 */
	public void setStaleDatedChecksValue(BigDecimal staleDatedChecksValue) {
		this.staleDatedChecksValue = staleDatedChecksValue;
	}

	/**
	 * @return
	 */
	public BigDecimal getUncashedChecksValue() {
		return uncashedChecksValue;
	}

	/**
	 * @param BigDecimal
	 */
	public void setUncashedChecksValue(BigDecimal uncashedChecksValue) {
		this.uncashedChecksValue = uncashedChecksValue;
	}

}