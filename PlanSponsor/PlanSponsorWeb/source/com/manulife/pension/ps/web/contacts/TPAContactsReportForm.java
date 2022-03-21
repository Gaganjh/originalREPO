package com.manulife.pension.ps.web.contacts;

import com.manulife.pension.ps.web.profiles.ManageUsersReportForm;

/**
 * Action Form for TPA contacts tab
 * @author NBalaji
 *
 */
public class TPAContactsReportForm extends ManageUsersReportForm {

	private static final long serialVersionUID = 0L;
	
	private String task;
	private String contractComments;
	private String tpaComments;
	private int tpaFirmContactId;
	private int tpaFirmId;
	private boolean displayActionColumnInd = true;
	private String showAllContactsInd;
	private boolean noActiveUsers;
	private boolean noAvailableUsers;

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getContractComments() {
		return contractComments;
	}

	public void setContractComments(String contractComments) {
		this.contractComments = contractComments;
	}

	public String getTpaComments() {
		return tpaComments;
	}

	public void setTpaComments(String tpaComments) {
		this.tpaComments = tpaComments;
	}

	public int getTpaFirmContactId() {
		return tpaFirmContactId;
	}

	public void setTpaFirmContactId(int tpaFirmContactId) {
		this.tpaFirmContactId = tpaFirmContactId;
	}

	public int getTpaFirmId() {
		return tpaFirmId;
	}

	public void setTpaFirmId(int tpaFirmId) {
		this.tpaFirmId = tpaFirmId;
	}

	public boolean getDisplayActionColumnInd() {
		return displayActionColumnInd;
	}

	public void setDisplayActionColumnInd(boolean displayActionColumnInd) {
		this.displayActionColumnInd = displayActionColumnInd;
	}

	public String getShowAllContactsInd() {
		return showAllContactsInd;
	}

	public void setShowAllContactsInd(String showAllContactsInd) {
		this.showAllContactsInd = showAllContactsInd;
	}

	public boolean isNoActiveUsers() {
		return noActiveUsers;
	}

	public void setNoActiveUsers(boolean noActiveUsers) {
		this.noActiveUsers = noActiveUsers;
	}

	public boolean isNoAvailableUsers() {
		return noAvailableUsers;
	}

	public void setNoAvailableUsers(boolean noAvailableUsers) {
		this.noAvailableUsers = noAvailableUsers;
	}

}
