package com.manulife.pension.bd.web.bob.transaction;

import java.util.Date;

import com.manulife.pension.platform.web.report.BaseReportForm;

/**
 * A simple form to handle the from date and to date drop down.
 * 
 * @author harlomte
 */
public class CashAccountReportForm extends BaseReportForm {

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
	
	/**
     * Get the From Date in Date Format.
     * 
     * @return - fromDate in Date format.
     */
    public Date getFromDateInDateFormat() {
        Date fromDt = new Date(Long.parseLong(fromDate));
        return fromDt;
    }

    /**
     * Get the To Date in Date Format.
     * 
     * @return - toDate in Date Format.
     */
    public Date getToDateInDateFormat() {
        Date toDt = new Date(Long.parseLong(toDate));
        return toDt;
    }
}
