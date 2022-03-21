package com.manulife.pension.ps.web.transaction;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.ps.web.report.ReportForm;

/**
 * Form bean to hold the information for the Pending Withdrawal Details page. 
 * 
 * @author Puttaiah Arugunta
 *
 */
public class PendingWithdrawalDetailsForm extends ReportForm  {

	
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	private String selectedTxnNumber;
	
	private String task;
	
	
	/**
	 * Default Constructor
	 */
	public PendingWithdrawalDetailsForm() {
		super();
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
	 * Method to reset the selected transaction number and task
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 * 
	 */
	@Override
	public void reset( HttpServletRequest request) {
		this.task = null;
		this.selectedTxnNumber = null;
		super.reset( request);
	}


}
