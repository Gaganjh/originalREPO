package com.manulife.pension.ps.web.password;

import com.manulife.pension.ezk.web.ActionForm;
	

/**
 * @author Chris Shin
 */
public class UpdatePasswordForm implements  ActionForm {

	public static final String FIELD_NEW_PASSWORD = "newPassword";
	public static final String FIELD_CONFIRM_PASSWORD = "confirmPassword";	
		

	private String userFullName;
	private String emailAddress;
	private String newPassword;
	private String confirmPassword;
	private String button;
	public boolean loginFlow = false;
	/**
	 * Constructor.
	 */
	public UpdatePasswordForm() {
		super();
	}

	/**
	 * @return Returns the userFullName.
	 */	
	public String getUserFullName() {
		return this.userFullName;
	}
	/**
	 * @param userFullName
	 *            The userFullName to set.
	 */		
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	
	/**
	 * @return Returns the emailAddress.
	 */
	public String getEmailAddress() {
		return this.emailAddress;
	}
	
	/**
	 * @param emailAddress
	 *            The emailAddress to set.
	 */		
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return Returns the newPassword.
	 */	
	public String getNewPassword() {
		return this.newPassword;
	}
	/**
	 * @param newPassword
	 *            The newPassword to set.
	 */		
	
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	/**
	 * @return Returns the confirmPassword.
	 */
	public String getConfirmPassword() {
		return this.confirmPassword;
	}
	
	/**
	 * @param confirmPassword
	 *            The confirmPassword to set.
	 */		
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	/**
	 * @return Returns the button.
	 */
	public String getButton() {
		return this.button;
	}
	
	/**
	 * @param button
	 *            The button to set.
	 */		
	
	public void setButton(String button) {
		this.button = button;
	}

	/**
	 * @return Returns the emailAddress.
	 */
	
	/**
	 * @param emailAddress
	 *            The emailAddress to set.
	 */		
	

	/**
	 * @return Returns the ssn.
	 */

	/**
	 * @param ssn
	 *            The ssn to set.
	 */
	


	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Form#reset(org.apache.struts.action.ActionMapping,
	 *      javax.servlet.http.HttpServletRequest)
	 
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
	}*/
	
	
	

	/**
	 * @return the loginFlow
	 */
	public boolean isLoginFlow() {
		return loginFlow;
	}

	/**
	 * @param loginFlow the loginFlow to set
	 */
	public void setLoginFlow(boolean loginFlow) {
		this.loginFlow = loginFlow;
	}
	
	
}