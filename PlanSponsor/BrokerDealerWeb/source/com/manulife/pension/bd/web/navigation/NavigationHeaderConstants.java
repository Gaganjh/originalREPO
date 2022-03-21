package com.manulife.pension.bd.web.navigation;

import com.manulife.pension.bd.web.content.BDContentConstants;

public interface NavigationHeaderConstants {
	public static final UserMenuItem ContactUs = new UserMenuItem("contactUs",
			"Contact Us", URLConstants.ContactUs);

	public static final UserMenuItem myProfileExternal = new UserMenuItem("myProfile",
			"My Profile", URLConstants.MyProfileExternal);

	public static final UserMenuItem disabledMyProfile = new UserMenuItem(
			"disabledMyProfile", "My Profile", null, false);

	public static final UserMenuItem myProfileInternal = new UserMenuItem("myProfile",
			"My Profile", URLConstants.MyProfileInternal);

	public static final UserMenuItem myProfileHelp = new UserMenuItem(
			BDContentConstants.MY_PROFILE_HELP_LINK);

	public static final UserMenuItem userManagement = new UserMenuItem(
			"userManagement", "User Management", URLConstants.UserManagement);

	public static final UserMenuItem contentManagement = new UserMenuItem(
			"contentManagement", "Firm Content", URLConstants.ContentManagement);

	public static final UserMenuItem globalMessages = new UserMenuItem(
			"globalMessages", "Global Messages", URLConstants.GlobalMessages);
	
	public static final UserMenuItem logOut = new UserMenuItem("logOut",
			"Logout", URLConstants.LogOut);

	public static final UserMenuItem exitMimic = new UserMenuItem("exitMimic",
			"Exit Advisor View", URLConstants.ExitMimic);

}
