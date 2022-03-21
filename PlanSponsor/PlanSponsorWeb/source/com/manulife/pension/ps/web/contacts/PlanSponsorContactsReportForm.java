package com.manulife.pension.ps.web.contacts;

import com.manulife.pension.ps.web.profiles.ManageUsersReportForm;

/**
 * Plansponsor contacts report form
 * @author Ranjit Kumar
 *
 */
public class PlanSponsorContactsReportForm extends ManageUsersReportForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 0L;
	
	private String task;
	private String contractComments;
	private boolean suppressLine2 = true;
	private String passiveTrustee;

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	/**
	 * Returns contract comments
	 * @return
	 */
	public String getContractComments() {
		return contractComments;
	}

	/**
	 * Sets contract comments
	 * @param contractComments
	 */
	public void setContractComments(String contractComments) {
		this.contractComments = contractComments;
	}

	/**
	 * @return the suppressLine2
	 */
	public boolean isSuppressLine2() {
		return suppressLine2;
	}

	/**
	 * @param suppressLine2 the suppressLine2 to set
	 */
	public void setSuppressLine2(boolean suppressLine2) {
		this.suppressLine2 = suppressLine2;
	}
	
	/**
	 * Returns passiveTrustee
	 * @return
	 */
	public String getPassiveTrustee() {
		return passiveTrustee;
	}

	/**
	 * Sets passiveTrustee
	 * @param passiveTrustee
	 */
	public void setPassiveTrustee(String passiveTrustee) {
		this.passiveTrustee = passiveTrustee;
	}
	
}
