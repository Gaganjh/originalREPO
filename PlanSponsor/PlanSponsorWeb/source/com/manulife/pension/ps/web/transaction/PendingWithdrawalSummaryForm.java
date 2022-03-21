package com.manulife.pension.ps.web.transaction;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.ps.web.report.ReportForm;

/**
 * Form bean to hold the information for the Pending Withdrawal Summary page. 
 * 
 * @author Puttaiah Arugunta
 *
 */
public class PendingWithdrawalSummaryForm  extends ReportForm {

	private static final long serialVersionUID = 1L;

	private String fromDate;
	
	private String defaultFromDate;
	
	private String toDate;
	
	private String defaultToDate;
	

	//Variable to get the tasks set in the client side
	private String task;
	
	private String selectedTxnNumber;
	
	private String  selectedParticipant;
	
	/**
	 * default constructor
	 */
	public PendingWithdrawalSummaryForm() {
		super();
	}

	/**
	 * Gets the selectedParticipant
	 * @return Returns a String
	 */
	public String getSelectedParticipant() {
		return selectedParticipant;
	}

	/**
	 * Sets the selectedParticipant
	 * @param selectedParticipant The selectedParticipant to set
	 */
	public void setSelectedParticipant(String selectedParticipant) {
		this.selectedParticipant = selectedParticipant;
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
	 * @return the selectedTxnNumber
	 */
	public String getSelectedTxnNumber() {
		return selectedTxnNumber;
	}

	/**
	 * @param selectedTxnNumber the selectedTxnNumber to set
	 */
	public void setSelectedTxnNumber(String selectedTxnNumber) {
		this.selectedTxnNumber = selectedTxnNumber;
	}

	/**
	 * @return the task
	 */
	public String getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(String task) {
		this.task = task;
	}
	
	/**
	 * @return  defaultFromDate
	 */
	public String getDefaultFromDate() {
		return defaultFromDate;
	}

	/**
	 * 
	 * @param defaultFromDate 
	 * 				To set default from date
	 */
	public void setDefaultFromDate(String defaultFromDate) {
		this.defaultFromDate = defaultFromDate;
	}

	/**
	 * 
	 * @return defaultToDate
	 */
	public String getDefaultToDate() {
		return defaultToDate;
	}

	/**
	 * 
	 * @param defaultToDate
	 * 			To set default to date
	 */
	public void setDefaultToDate(String defaultToDate) {
		this.defaultToDate = defaultToDate;
	}
	

	/**
	 * Method to reset the selected transaction number and task
	 * 
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 * 
	 */
	
	public void reset( HttpServletRequest request) {
		this.task = null;
		this.selectedTxnNumber = null;
		super.reset( request);
	}

}
