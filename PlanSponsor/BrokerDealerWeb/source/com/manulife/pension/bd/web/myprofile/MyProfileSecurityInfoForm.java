package com.manulife.pension.bd.web.myprofile;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.registration.util.PasswordChallengeInput;
import com.manulife.pension.platform.web.controller.AutoForm;

public class MyProfileSecurityInfoForm extends AutoForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_CONFIRMED_PASSWORD = "confirmedPassword";
	private String newPassword;
	private String confirmedPassword;
	private String[] currentChallengeQuestions = new String[] { "", "" };
	private PasswordChallengeInput challenge1 = new PasswordChallengeInput();
	private PasswordChallengeInput challenge2 = new PasswordChallengeInput();

	private boolean success;

	private String currentPassword;

	private boolean changed = false;
	
	public PasswordChallengeInput getChallenge1() {
		return challenge1;
	}

	public PasswordChallengeInput getChallenge2() {
		return challenge2;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String[] getCurrentChallengeQuestions() {
		return currentChallengeQuestions;
	}

	public void setCurrentChallengeQuestions(String[] currentChallengeQuestions) {
		this.currentChallengeQuestions[0] = (currentChallengeQuestions == null ? ""
				: StringUtils.trimToEmpty(currentChallengeQuestions[0]));
		this.currentChallengeQuestions[1] = (currentChallengeQuestions == null || currentChallengeQuestions.length < 2) ? ""
				: StringUtils.trimToEmpty(currentChallengeQuestions[1]);
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void clear() {
		currentPassword = "";
		newPassword = "";
		confirmedPassword = "";
		challenge1.clear();
		challenge2.clear();
		changed = false;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}
