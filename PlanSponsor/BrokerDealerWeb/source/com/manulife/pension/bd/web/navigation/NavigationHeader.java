package com.manulife.pension.bd.web.navigation;

import java.io.Serializable;
import java.util.List;

/**
 * This stores the information for the Navigation Header
 * @author guweigu
 *
 */
public class NavigationHeader implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<UserMenuItem> links;
	private String welcomeText;
	private UserMenuItem exitMenu;
	private String userNameText;
	private String roleText;

	public String getRoleText() {
		return roleText;
	}

	public void setRoleText(String roleText) {
		this.roleText = roleText;
	}

	public List<UserMenuItem> getLinks() {
		return links;
	}

	public void setLinks(List<UserMenuItem> links) {
		this.links = links;
	}

	public String getWelcomeText() {
		return welcomeText;
	}

	public void setWelcomeText(String welcomeText) {
		this.welcomeText = welcomeText;
	}

	public UserMenuItem getExitMenu() {
		return exitMenu;
	}

	public void setExitMenu(UserMenuItem exitMenu) {
		this.exitMenu = exitMenu;
	}

	public String getUserNameText() {
		return userNameText;
	}

	public void setUserNameText(String userNameText) {
		this.userNameText = userNameText;
	}
}
