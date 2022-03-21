package com.manulife.pension.ps.web.transaction;

import com.manulife.pension.ps.web.report.ReportForm;

/**
 * @author Maria Lee
 */
public class LoanRepaymentTransactionReportForm extends ReportForm {

	private String transactionNumber;
	private String transactionDate;
	
	/**
	 * 
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
