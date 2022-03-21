package com.manulife.pension.bd.web.navigation;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.controller.AuthorizationSubject;
import com.manulife.pension.bd.web.navigation.generation.NavigationGenerator;
import com.manulife.pension.bd.web.navigation.generation.SystemMenuItem;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.LoginStatus;
import com.manulife.pension.exception.SystemException;

/**
 * This is a Helper class that will give back a UserMenu Object.
 * 
 * @author HArlomte
 * 
 */
public class UserNavigationFactory {
	private NavigationGenerator navGenerator;
	private SystemMenuItem securedMenu;
	private SystemMenuItem publicMenu;

	/**
	 * Return the instance of the application context
	 * 
	 * @param context
	 * @return
	 */
	public static UserNavigationFactory getInstance(ServletContext context) {
		return ApplicationHelper.getUserNavigationFactory(context);
	}

	public SystemMenuItem getPublicMenu() {
		return publicMenu;
	}

	public void setPublicMenu(SystemMenuItem systemMenu) {
		this.publicMenu = systemMenu;
	}

	public SystemMenuItem getSecuredMenu() {
		return securedMenu;
	}

	public void setSecuredMenu(SystemMenuItem securedMenu) {
		this.securedMenu = securedMenu;
	}

	public NavigationGenerator getNavigationGenerator() {
		return navGenerator;
	}

	public void setNavigationGenerator(NavigationGenerator navGenerator) {
		this.navGenerator = navGenerator;
	}

	public UserNavigation getUserNavigaion(ServletRequest request,
			ServletContext context) throws SystemException {
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		AuthorizationSubject subject = ApplicationHelper
				.getAuthorizationSubject((HttpServletRequest) request);
		UserNavigation nav = null;
		BDUserProfile userProfile = (BDUserProfile) subject.getUserProfile();
		// In public mode, then use the public system menu
		if (userProfile == null
				|| userProfile.getLoginStatus() != LoginStatus.FullyLogin) {
			nav = (UserNavigation) context
					.getAttribute(BDConstants.USER_NAVIGATION_KEY);
			if (nav == null) {
				nav = new UserNavigation();
				nav
						.setUserMenu(navGenerator.generateMainMenu(publicMenu,
								null));
				nav.setNavigationHeader(navGenerator
						.generateNavigationHeader(null));
				context.setAttribute(BDConstants.USER_NAVIGATION_KEY, nav);
			}
		} else {
			// in secured mode then use Secured version of system menu
			nav = (UserNavigation) session
					.getAttribute(BDConstants.USER_NAVIGATION_KEY);
			if (nav == null) {
				nav = new UserNavigation();
				nav
						.setUserMenu(navGenerator.generateMainMenu(securedMenu,
								subject));
				nav.setNavigationHeader(navGenerator
						.generateNavigationHeader(subject));
				session.setAttribute(BDConstants.USER_NAVIGATION_KEY, nav);
			}
		}
		return nav;
	}
	
	/**
	 * When the profile is changed, update the header for user name
	 * This is in secured mode
	 * @param request
	 * @param context
	 * @throws SystemException
	 */
	public void updateNavigation(ServletRequest request,
			ServletContext context) throws SystemException {
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		UserNavigation nav = (UserNavigation) session
				.getAttribute(BDConstants.USER_NAVIGATION_KEY);
		AuthorizationSubject subject = ApplicationHelper
				.getAuthorizationSubject((HttpServletRequest) request);
		if (nav != null) {
			nav.setNavigationHeader(navGenerator
					.generateNavigationHeader(subject));
			nav.setUserMenu(navGenerator.generateMainMenu(getSecuredMenu(),
					subject));
		}	
	}
}
