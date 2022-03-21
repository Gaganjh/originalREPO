package com.manulife.pension.ps.web.withdrawal;


/**
 * WithdrawalPermissions is a value object used by jsp view logic 
 * 
 * @author snowdde
 
 */
public class WithdrawalPermissions {
	private Boolean delete = Boolean.FALSE;

	/**
	 * @return returns true if a user can delete a withdrawal request
	 */
	public Boolean getDelete() {
		return delete;
	}

	/**
	 * @param delete true if the user can delete a withdrawal request
	 */
	public void setDelete(final Boolean delete) {
		this.delete = delete;
	}
}