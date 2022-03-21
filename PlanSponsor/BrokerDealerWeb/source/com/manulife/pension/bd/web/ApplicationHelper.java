package com.manulife.pension.bd.web;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.controller.AuthorizationSubject;
import com.manulife.pension.bd.web.controller.BDAuthorizationSubject;
import com.manulife.pension.bd.web.controller.SecurityManager;
import com.manulife.pension.bd.web.navigation.UserNavigationFactory;
import com.manulife.pension.bd.web.pagelayout.LayoutStore;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ApplicationFacade;
import com.manulife.pension.platform.web.controller.BaseUserProfile;

/**
 * Application Helper class 
 * 
 * @author guweigu
 *
 */
public class ApplicationHelper {
	public static final String SecurityManager = "SecurityManager";
	public static final String UserStore = "UserStore";
	public static final String UserProfile = "UserProfile";
	public static final String UserNavigationFactory = "UserNavigationFactory";
	public static final String LayoutStore = "LayoutStore";

	static public ApplicationFacade getApplicationFacade(ServletContext context) {
		return (ApplicationFacade) context
				.getAttribute(CommonConstants.APPLICATION_FACADE_KEY);
	}

	static public SecurityManager getSecurityManager(ServletContext context) {
		return (SecurityManager) context.getAttribute(SecurityManager);
	}

	static public LayoutStore getLayoutStore(ServletContext context) {
		return (LayoutStore) context.getAttribute(LayoutStore);
	}


	static public void setUserProfile(HttpServletRequest request,
			BaseUserProfile userProfile) {
		request.getSession(true).setAttribute(CommonConstants.USERPROFILE_KEY, userProfile);
		
	}
	
	static public void setBobContext(HttpServletRequest request,
			BobContext bobContext) {
		request.getSession(true).setAttribute(BDConstants.BOBCONTEXT_KEY, bobContext);		
	}
	
	public static UserNavigationFactory getUserNavigationFactory(ServletContext context) {
		return (UserNavigationFactory) context.getAttribute(UserNavigationFactory);
	}

	/**
	 * Get Authorization Subject for the request
	 * @param request
	 * @return
	 */
	public static AuthorizationSubject getAuthorizationSubject(
			HttpServletRequest request) {
		BDAuthorizationSubject subject = new BDAuthorizationSubject();
		subject.setUserProfile(BDSessionHelper.getUserProfile(request));
		subject.setBobContext(BDSessionHelper.getBobContext(request));

		if(request.getSession(false) != null){
			Map<String, Object> mimickingUserSession = (Map<String, Object>) request
					.getSession(false).getAttribute(BDConstants.ATTR_MIMICKING_SESSION );
			if (mimickingUserSession != null &&  mimickingUserSession.get(BDConstants.USERPROFILE_KEY) != null) {
				BDUserProfile mimickingInternalUserProfile = (BDUserProfile) mimickingUserSession
						.get(BDConstants.USERPROFILE_KEY);
				subject.setLoggedinUserProfile(mimickingInternalUserProfile);
			}
			
			if (request.getSession(false).getAttribute(BDConstants.USERID_KEY) != null) {
				subject.setUserIdKey((String) request.getSession(false).getAttribute(BDConstants.USERID_KEY));
				//TODOsubject.setUserIdKey((String) request.getSession(false).getAttribute(BDConstants.USERID_KEY));
			}
		}
		return subject;
	}
	
	/**
	 * Set the content location (US or NY) for this specific request
	 * So that at request level, this location will override the location
	 * setup in the web app's deployment.
	 * 
	 * @param request
	 * @param contentLocation: Location.USA or Location.NEW_YORK
	 */
	public static void setRequestContentLocation(HttpServletRequest request,
			Location contentLocation) {
		request.setAttribute(GlobalConstants.CONTENT_LOCATION_ATTRIBUTE_NAME,
				contentLocation);
	}

	/**
	 * Get the content location (US or NY) for this specific request So that at
	 * request level, this location will override the location setup in the web
	 * app's deployment.
	 * 
	 * @param request
	 * @param contentLocation
	 *            : Location.USA or Location.NEW_YORK
	 */
	public static Location getRequestContentLocation(HttpServletRequest request) {
		return (Location) request
				.getAttribute(GlobalConstants.CONTENT_LOCATION_ATTRIBUTE_NAME);
	}
}
