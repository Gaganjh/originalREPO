package com.manulife.pension.bd.web.password;

import com.manulife.pension.platform.web.controller.AutoForm;

public class UpdatePasswordForm extends AutoForm {

	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = 1L;

	public UpdatePasswordForm() {
		super();
	}

	public static final String ACTION_CHANGE = "change";

	public static final String FIELD_NEW_PASSWORD = "password";
	
	public static final String FIELD_NEW_CONFIRMED_PASSWORD = "confirmedPassword";
	

	private String password;
	private String confirmedPassword;
	private boolean internal;

	// for external user

	private boolean disabled = false;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

	public boolean isInternal() {
		return internal;
	}

	public void setInternal(boolean internal) {
		this.internal = internal;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
