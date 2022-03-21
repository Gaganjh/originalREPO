package com.manulife.pension.bd.web.bob.transaction;

import com.manulife.pension.platform.web.report.BaseReportForm;

/**
 * This is the Action Form for Transaction Details - Loan Repayment page.
 * 
 * @author harlomte
 * 
 */
public class LoanRepaymentTransactionReportForm extends BaseReportForm {

    private static final long serialVersionUID = -2841444628947422918L;

    private String transactionNumber;
	private String transactionDate;

    /**
     * Constructor
     */
	public LoanRepaymentTransactionReportForm() {
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

	/**
	 * Gets the transactionDate
	 * @return Returns a String
	 */
	public String getTransactionDate() {
		return transactionDate;
	}
	/**
	 * Sets the transactionDate
	 * @param transactionDate The transactionDate to set
	 */
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

}
