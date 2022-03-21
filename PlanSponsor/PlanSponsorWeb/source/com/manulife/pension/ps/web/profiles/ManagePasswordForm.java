package com.manulife.pension.ps.web.profiles;

import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * This is Form class for ManagePassword
 * 
 * @author Ludmila Stern
 */
public class ManagePasswordForm extends AutoForm {

	private String userName;
	private String email;
	private String firstName;
	private String lastName;
	private String actionLabel;
	private long userProfileId;
	private static final Map ACTION_LABEL_MAP = new HashMap();

	private boolean fromTPAContactsTab;
	private boolean fromPSContactTab;
	
	/*
	 * Maps the button label to the corresponding action.
	 */
	public static final String BUTTON_LABEL_UNLOCK_PASSCODE = "unlock security code";
	public static final String BUTTON_LABEL_RESET_PASSWORD = "reset password";
	public static final String BUTTON_LABEL_UNLOCK_PASSWORD = "unlock password";
	public static final String BUTTON_LABEL_UNLOCK = "unlock";
	public static final String BUTTON_LABEL_FINISH = "finish";

	static {
		ACTION_LABEL_MAP.put(BUTTON_LABEL_RESET_PASSWORD, "reset");
		ACTION_LABEL_MAP.put(BUTTON_LABEL_UNLOCK_PASSWORD, "unlock");
		ACTION_LABEL_MAP.put(BUTTON_LABEL_UNLOCK_PASSCODE, "unlockPasscode");
		ACTION_LABEL_MAP.put(BUTTON_LABEL_UNLOCK, "unlock");
		ACTION_LABEL_MAP.put(BUTTON_LABEL_FINISH, "finish");
	}

	/**
	 * Gets the userName
	 * 
	 * @return Returns a String
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * Sets the userName
	 * 
	 * @param userName
	 *            The userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the email
	 * 
	 * @return Returns a String
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * Sets the email
	 * 
	 * @param email
	 *            The email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the firstName
	 * 
	 * @return Returns a String
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * Sets the firstName
	 * 
	 * @param firstName
	 *            The firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the lastName
	 * 
	 * @return Returns a String
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * Sets the lastName
	 * 
	 * @param lastName
	 *            The lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return Returns the actionLabel.
	 */
	public String getActionLabel() {
		return actionLabel;
	}

	/**
	 * @param actionLabel
	 *            The actionLabel to set.
	 */
	public void setActionLabel(String actionLabel) {
		this.actionLabel = actionLabel;
		setAction((String) ACTION_LABEL_MAP.get(actionLabel));
	}

	public String getAction() {
		if (super.getAction().length() == 0 && actionLabel != null
				&& actionLabel.length() > 0) {
			setAction((String) ACTION_LABEL_MAP.get(actionLabel));
		}
		return super.getAction();
	}
	
	public long getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(long userProfileId) {
		this.userProfileId = userProfileId;
	}
	public boolean isFromTPAContactsTab() {
		return fromTPAContactsTab;
	}
	public void setFromTPAContactsTab(boolean fromTPAContactsTab) {
		this.fromTPAContactsTab = fromTPAContactsTab;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isFromPSContactTab() {
		return fromPSContactTab;
	}
	
	/**
	 * 
	 * @param fromPSContactTab
	 */
	public void setFromPSContactTab(boolean fromPSContactTab) {
		this.fromPSContactTab = fromPSContactTab;
	}
	
	
}