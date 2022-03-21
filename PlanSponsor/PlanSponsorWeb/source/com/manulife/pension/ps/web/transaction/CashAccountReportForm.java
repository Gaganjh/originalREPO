package com.manulife.pension.ps.web.transaction;

import com.manulife.pension.ps.web.report.ReportForm;

/**
 * A simple form to handle the from date and to date drop down.
 * 
 * @author Charles Chan
 */
public class CashAccountReportForm extends ReportForm {

	private String fromDate;

	private String toDate;
	
	/**
	 * Constructor. 
	 */
	public CashAccountReportForm() {
		super();
	}

	/**
	 * @return Returns the fromDate.
	 */
	public String getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate The fromDate to set.
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return Returns the toDate.
	 */
	public String getToDate() {
		return toDate;
	}

	/**
	 * @param toDate The toDate to set.
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
}
