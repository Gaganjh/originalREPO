package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

import com.manulife.pension.ps.service.report.transaction.reporthandler.LoanSummaryReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author grouzan
 * 
 * ReportData object for LoanSummary page. Holds collection of LoanSummaryItem objects (as 
 * "details") as well as totals/header  - LoanSummaryTotals
 *
 */
public class LoanSummaryReportData extends ReportData implements Serializable {
	
	public static final String REPORT_ID = LoanSummaryReportHandler.class.getName();
	
	public static final String REPORT_NAME = "ContractCurrentLoanSummary"; 
	
	public static final String FILTER_CONTRACT_NO = "contractNumber";
	
	private Date asOfDate;
	private BigDecimal outstandingBalance;
	private int numLoans;
	private int numParticipants;

	public LoanSummaryReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
		this.details = new ArrayList(0); // a graceful way to report "No matches"
	}

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
	public int getNumLoans() {
		return numLoans;
	}

	/**
	 * @return
	 */
	public int getNumParticipants() {
		return numParticipants;
	}

	/**
	 * @param i
	 */
	public void setNumLoans(int i) {
		numLoans = i;
	}

	/**
	 * @param i
	 */
	public void setNumParticipants(int i) {
		numParticipants = i;
	}

	/**
	 * @return
	 */
	public BigDecimal getOutstandingBalance() {
		return outstandingBalance;
	}

	/**
	 * @param decimal
	 */
	public void setOutstandingBalance(BigDecimal decimal) {
		outstandingBalance = decimal;
	}

}

