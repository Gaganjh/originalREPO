package com.manulife.pension.bd.web.bob.transaction;

import com.manulife.pension.platform.web.report.BaseReportForm;

/**
 * Form for the ContributionTransaction Report
 * 
 * @author HarikishanRao Lomte
 * 
 */
public class ContributionTransactionReportForm extends BaseReportForm {

    private static final long serialVersionUID = 1L;

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
