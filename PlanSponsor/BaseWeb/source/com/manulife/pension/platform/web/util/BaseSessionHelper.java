package com.manulife.pension.platform.web.util;

import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseUserProfile;
import com.manulife.pension.util.content.GenericException;

public class BaseSessionHelper {

	private static final String EMPTY_VALUE = "";
    private static final String JSESSIONID = "JSESSIONID";
	private static final String FORWARD_SLASH = "/";
	private static final String TEXT_HTML = "text/html";

	/*private final static String BdBaseAction.ERROR_KEY = CommonEnvironment.getInstance()
			.getErrorKey();*/

	/**
	 * Constructor.
	 * 
	 */
	protected BaseSessionHelper() {
		super();
	}

	public static void setErrorsInSession(final HttpServletRequest request,
			final Collection<GenericException> errors) {
		if (errors != null) {
			// check for errors already in session scope
			Collection<GenericException> existingErrors = (Collection<GenericException>) request
					.getSession(false).getAttribute("psErrors");
			if (existingErrors != null) {
				errors.addAll(existingErrors);
				request.getSession(false).removeAttribute("psErrors");
			}

			request.getSession(false).setAttribute("psErrors", errors);
		}
	}

	public static Collection<GenericException> getErrorsInSession(
			final HttpServletRequest request) {
		return (Collection<GenericException>) request.getSession(false)
				.getAttribute("psErrors");
	}

	public static void removeErrorsInSession(HttpServletRequest request) {
		request.getSession(false).removeAttribute("psErrors");
	}

	/**
	 * Returns the user profile associated with the given request.
	 * 
	 * @param request
	 *            The request object.
	 * @return The user profile object associated with the request (or null if
	 *         none is found).
	 */
	public static BaseUserProfile getBaseUserProfile(HttpServletRequest request) {
		return (BaseUserProfile) request.getSession(false).getAttribute(
				CommonConstants.USERPROFILE_KEY);
	}


	/**
	 * If you wish to log the user out, use this method.  It invalidates the session and also forces
	 * the JSESSIONID cookie to be overwritten with an empty string, such that it is not hanging around
	 * in the user's browser (which can be used as an exploit on shared machines.)
	 * 
	 * If in doubt, call this method vs the one that takes the additional boolean value.
	 *  
	 * @param request
	 * @param response
	 */
	public static void invalidateSession(HttpServletRequest request, HttpServletResponse response) {	
		invalidateSession(request, response, true);
	}
	
	/**
	 * Used to invalidate the session, and allows control over whether the JSESSIONID is removed.
	 * @see invalidateSession(HttpServletRequest request, HttpServletResponse response) for common usage.
	 * @param request - The HttpServletRequest
	 * @param response - The HttpServletResponse
	 * @param forceSessionCookieRemoval - force the JSESSIONID cookie to be written with an empty string if true.
	 * 									  otherwise, it will attempt to determine if the cookie should be removed.
	 * 									  typically this is only called with "false" when you are first arriving
	 *                                    on the login page, to avoid an empty JSESSION Cookie being written. 
	 */
	public static void invalidateSession(HttpServletRequest request, HttpServletResponse response, boolean forceSessionCookieRemoval) {
		HttpSession session = request.getSession(false);
		if (session != null) {
		    try {
		        session.invalidate();
		    } catch (IllegalStateException e) {
		        // Session is already invalid.
		    }
            removeSessionCookie(request, response);
		} else {
			// Session already null, see if we need to invalidate cookie.
		    Cookie[] cookies = request.getCookies();
		    boolean sanitizeCookieInResponse = forceSessionCookieRemoval;
		    if (!forceSessionCookieRemoval && cookies != null) {
		        for (Cookie cookie : cookies) {
		            if (cookie.getName().equalsIgnoreCase(JSESSIONID)) {
		                sanitizeCookieInResponse = true;
		                break;
		            }
		        }
		    }
            if (sanitizeCookieInResponse == true) {
                removeSessionCookie(request, response);
            }
		}		
	}
	
	public static void invalidateSessionKeepCookie(final HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            try {
                session.invalidate();
            } catch (IllegalStateException e) {
                // Session is already invalid.
            }
        }
	}

    
    /**
     * Removes the JSESSIONID cookie from the response by overwriting it with a dud.
     * @param request
     * @param response
     */
    private static void removeSessionCookie(HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie deadCookie = new Cookie(JSESSIONID, EMPTY_VALUE);
            deadCookie.setPath(FORWARD_SLASH);
            deadCookie.setMaxAge(0);
            response.setContentType(TEXT_HTML);
            response.addCookie(deadCookie);
        } catch (Exception e) {
            // Not important to log this.
        }
    }	
	
}
