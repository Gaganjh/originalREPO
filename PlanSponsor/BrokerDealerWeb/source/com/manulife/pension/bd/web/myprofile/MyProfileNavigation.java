package com.manulife.pension.bd.web.myprofile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.bd.web.controller.AuthorizationSubject;
import com.manulife.pension.bd.web.controller.BDAuthorizationSubject;
import com.manulife.pension.bd.web.controller.SecurityManager;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.navigation.UserMenuItem;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.exception.SystemException;

public class MyProfileNavigation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<UserMenuItem> tabs;

	public static final String PersonalInfoTabId = "personalInfo";
	public static final String BrokerPersonalInfoTabId = "brokerPersonalInfo";
	public static final String SecurityInfoTabId = "securityInfo";
	public static final String AssistantsTabId = "assistants";
	public static final String LicenseTabId = "license";
	public static final String PreferenceTabId = "preference";
	public static final String AddBOBTabId = "addBOB";
	public static final String CreateBOBTabId = "createBOB";

	private static final UserMenuItem PersonalTab = new UserMenuItem(
			PersonalInfoTabId, "Personal Information",
			URLConstants.MyProfilePersonalTab);

	private static final UserMenuItem BrokerPersonalInfoTab = new UserMenuItem(
			BrokerPersonalInfoTabId, "Personal Information",
			URLConstants.MyProfileBrokerPersonalTab);

	private static final UserMenuItem SecurityInfoTab = new UserMenuItem(
			SecurityInfoTabId, "Security Details",
			URLConstants.MyProfileSecurityTab);

	private static final UserMenuItem AssistantsTab = new UserMenuItem(
			AssistantsTabId, "Assistants", URLConstants.MyProfileAssistantsTab);

	private static final UserMenuItem LicenseTab = new UserMenuItem(
			LicenseTabId, "License", URLConstants.MyProfileLicenseTab);

	private static final UserMenuItem PreferenceTab = new UserMenuItem(
			PreferenceTabId, "Preferences", URLConstants.MyProfilePreferenceTab);

	private static final UserMenuItem AddBOBTab = new UserMenuItem(AddBOBTabId,
			"Add Another Profile", URLConstants.MyProfileAddBOBTab);

	private static final UserMenuItem CreateBOBTab = new UserMenuItem(
			CreateBOBTabId, "Add My Contracts",
			URLConstants.MyProfileCreateBOBTab);

	private String currentTabId = null;

	public MyProfileNavigation(BDUserProfile userProfile,
			SecurityManager securityManager) throws SystemException {
		tabs = new ArrayList<UserMenuItem>();
		addLink(userProfile, securityManager, tabs, PersonalTab);
		addLink(userProfile, securityManager, tabs, BrokerPersonalInfoTab);
		addLink(userProfile, securityManager, tabs, SecurityInfoTab);
		addLink(userProfile, securityManager, tabs, AssistantsTab);
		addLink(userProfile, securityManager, tabs, LicenseTab);
		addLink(userProfile, securityManager, tabs, PreferenceTab);
		addLink(userProfile, securityManager, tabs, AddBOBTab);
		addLink(userProfile, securityManager, tabs, CreateBOBTab);
		if (tabs.size() > 0) {
			currentTabId = tabs.get(0).getId();
		}
	}

	private void addLink(BDUserProfile user, SecurityManager securityManager,
			List<UserMenuItem> list, UserMenuItem item) throws SystemException {

		if (securityManager.isUserAuthorized(createAuthorizationSubject(user),
				item.getActionURL())) {
			list.add(item);
		}
		return;
	}

	public String getCurrentTabId() {
		return currentTabId;
	}

	public void setCurrentTabId(String currentTabId) {
		this.currentTabId = currentTabId;
	}

	public List<UserMenuItem> getTabs() {
		return tabs;
	}

	private AuthorizationSubject createAuthorizationSubject(BDUserProfile user) {
		BDAuthorizationSubject subject = new BDAuthorizationSubject();
		subject.setUserProfile(user);
		return subject;
	}
}
