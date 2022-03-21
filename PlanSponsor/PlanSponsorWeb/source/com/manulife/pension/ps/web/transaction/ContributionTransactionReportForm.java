package com.manulife.pension.ps.web.transaction;

import com.manulife.pension.ps.web.report.ReportForm;

/**
 * Form for the ContributionTransaction Report
 * @author drotele
 * Created on Apr 28, 2004
 *
 */
public class ContributionTransactionReportForm extends ReportForm {

	private String transactionNumber;

	/**
	 * Constructor
	 */
	public ContributionTransactionReportForm() {
		super();
	}

	/**
	 * Gets the transactionNumber
	 * @return Returns a String
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}
	/**
	 * Sets the transactionNumber
	 * @param transactionNumber The transactionNumber to set
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

}
