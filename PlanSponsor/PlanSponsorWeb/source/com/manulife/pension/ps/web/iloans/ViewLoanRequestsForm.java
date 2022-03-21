package com.manulife.pension.ps.web.iloans;

import com.manulife.pension.ps.service.report.iloans.valueobject.LoanRequestReportData;
import com.manulife.pension.ps.web.report.ReportForm;

/*
 File: ViewLoanRequestsForm.java

 Version   Date         Author           Change Description
 -------   ----------   --------------   ------------------------------------------------------------------
 CS1.0     2005-04-28   Chris Shin       Initial version.
 */

/**
 * This class is the action form for the View Loan requests form
 * 
 * @author Chris Shin
 * @version CS1.0 (April 28, 2005)
 */

public class ViewLoanRequestsForm extends ReportForm {

	private LoanRequestReportData loanRequestReportData = null;

	private String action;

	public static final String REQUEST_ACTION = "Create request";

	/**
	 * Default Constructor
	 */
	public ViewLoanRequestsForm() {
		super();
	}

	public LoanRequestReportData getLoanRequests() {
		return this.loanRequestReportData;

	}

	public void setLoanRequestReportData(
			LoanRequestReportData loanRequestReportData) {
		this.loanRequestReportData = loanRequestReportData;
	}

	public void setAction(String actionLabel) {
		this.action = trimString(action);
	}

	/**
	 * @return Returns the actionLabel.
	 */
	public String getAction() {
		return action;
	}

}

