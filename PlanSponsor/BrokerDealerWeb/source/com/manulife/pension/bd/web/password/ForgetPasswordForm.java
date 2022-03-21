package com.manulife.pension.bd.web.password;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.controller.BaseForm;

public class ForgetPasswordForm extends BaseForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String question1;
	private String question2;
	private String answer1;
	private String answer2;
	private String newPassword;
	private String confirmedPassword;
	private boolean disabled;
	private boolean brokerWithNoActiveEntity=false;
	private String passcode;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = StringUtils.trimToEmpty(userId);
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = StringUtils.trimToEmpty(answer1);
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = StringUtils.trimToEmpty(answer2);
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

	public String getQuestion1() {
		return question1;
	}

	public void setQuestion1(String question1) {
		this.question1 = question1;
	}

	public String getQuestion2() {
		return question2;
	}

	public void setQuestion2(String question2) {
		this.question2 = question2;
	}

	public void copyFrom(ForgetPasswordForm src) {
		if (src != null) {
			userId = src.getUserId();
			question1 = src.getQuestion1();
			question2 = src.getQuestion2();
			answer1 = src.getAnswer1();
			answer2 = src.getAnswer2();
			newPassword = src.getNewPassword();
			confirmedPassword = src.getConfirmedPassword();
		}
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isBrokerWithNoActiveEntity() {
		return brokerWithNoActiveEntity;
	}

	public void setBrokerWithNoActiveEntity(boolean brokerWithNoActiveEntity) {
		this.brokerWithNoActiveEntity = brokerWithNoActiveEntity;
	}

	public String getPasscode() {
		return passcode;
	}

	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}
}
