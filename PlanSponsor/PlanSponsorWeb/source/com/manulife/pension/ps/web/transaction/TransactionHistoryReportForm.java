package com.manulife.pension.ps.web.transaction;

import com.manulife.pension.ps.web.report.ReportForm;

/**
 * @author Charles Chan
 * @author Maria Lee
 */
public class TransactionHistoryReportForm extends ReportForm {

	private String fromDate;
	
	private String toDate;
	
	private String transactionType;
	
	/**
	 * 
	 */
	public TransactionHistoryReportForm() {
		super();
	}


	/**
	 * Gets the fromDate
	 * @return Returns a String
	 */
	public String getFromDate() {
		return fromDate;
	}
	/**
	 * Sets the fromDate
	 * @param fromDate The fromDate to set
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * Gets the toDate
	 * @return Returns a String
	 */
	public String getToDate() {
		return toDate;
	}
	/**
	 * Sets the toDate
	 * @param toDate The toDate to set
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	/**
	 * Gets the transactionType
	 * @return Returns a String
	 */
	public String getTransactionType() {
		return transactionType;
	}
	/**
	 * Sets the transactionType
	 * @param transactionType The transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

}
