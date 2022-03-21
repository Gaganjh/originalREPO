package com.manulife.pension.bd.web.navigation;

import java.io.Serializable;

public class UserNavigation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7948012463407568286L;
	private UserMenu userMenu;
	private NavigationHeader navigationHeader;

	public NavigationHeader getNavigationHeader() {
		return navigationHeader;
	}

	public void setNavigationHeader(NavigationHeader navigationHeader) {
		this.navigationHeader = navigationHeader;
	}

	public UserMenu getUserMenu() {
		return userMenu;
	}

	public void setUserMenu(UserMenu userMenu) {
		this.userMenu = userMenu;
	}

}
