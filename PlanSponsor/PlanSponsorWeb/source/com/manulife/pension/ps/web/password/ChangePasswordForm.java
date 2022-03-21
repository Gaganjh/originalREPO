package com.manulife.pension.ps.web.password;

import com.manulife.pension.ezk.web.ActionForm;

import com.manulife.pension.platform.web.util.Ssn;
	

/**
 * @author Chris Shin
 */
public class ChangePasswordForm implements  ActionForm {

	public static final String FIELD_NEW_PASSWORD = "newPassword";
	public static final String FIELD_CONFIRM_PASSWORD = "confirmPassword";	
	public static final String FIELD_OLD_PASSWORD = "oldPassword";
	public static final String FIELD_SSN = "ssn";	
	public static final String FIELD_EMPLOYEE_NUMBER = "employeeNumber";	

	private String userFullName;
	private String emailAddress;
	private String newPassword;
	private String confirmPassword;
	private String oldPassword;
	private String button;
	private Ssn ssn = new Ssn();
	private String employeeNumber;
    private boolean passwordReset;
	public boolean loginFlow = false;
	/**
	 * Constructor.
	 */
	public ChangePasswordForm() {
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
	 * @return Returns the oldPassword.
	 */	
	public String getOldPassword() {
		return this.oldPassword;
	}
	/**
	 * @param oldPassword
	 *            The oldPassword to set.
	 */		
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword.trim();
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
	 * @return Returns the ssn.
	 */
	public Ssn getSsn() {
		return ssn;
	}

	/**
	 * @param ssn
	 *            The ssn to set.
	 */
	public void setSsn(Ssn ssn) {
		this.ssn = ssn;
	}

	public String getSsnValue() {
		StringBuffer buf = new StringBuffer();
		buf.append(ssn.getDigits(0));
		buf.append(ssn.getDigits(1));
		buf.append(ssn.getDigits(2));	
		return buf.toString();
	}


	/**
	 * @return Returns the employeeNumber.
	 */
	public String getEmployeeNumber() {
		return this.employeeNumber;
	}
	
	/**
	 * @param employeeNumber
	 *            The employeeNumber to set.
	 */		
	
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Form#reset(org.apache.struts.action.ActionMapping,
	 *      javax.servlet.http.HttpServletRequest)
	 
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
	}*/
	
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("oldPassword [");
		buf.append(oldPassword);
		buf.append("] newPassword [");
		buf.append(newPassword);
		buf.append("] confirmPassword [");
		buf.append(confirmPassword);
		buf.append("] userFullName [");
		buf.append(userFullName);
		buf.append("] button [");
		buf.append(button);
		buf.append("] emailAddress [");
		buf.append(emailAddress);
		buf.append("] employeeNumber [");
		buf.append(employeeNumber);
		buf.append("] ssn [");
		buf.append(getSsnValue());
		buf.append("] \n");

		return buf.toString();
	}

    /**
     * @return the passwordReset
     */
    public boolean isPasswordReset() {
        return passwordReset;
    }

    /**
     * @param passwordReset the passwordReset to set
     */
    public void setPasswordReset(boolean passwordReset) {
        this.passwordReset = passwordReset;
    }

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