package com.manulife.pension.bd.web.myprofile;

import com.manulife.pension.platform.web.controller.BaseForm;

public class ManageBrokerAssistantForm extends BaseForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String action;
	private long userProfileId;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public long getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(long userProfileId) {
		this.userProfileId = userProfileId;
	}

}
