package com.manulife.pension.bd.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.util.BDCommonMrlLoggingUtil;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.TrueIPCookie;
import com.manulife.pension.platform.web.controller.BaseUserProfile;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 * The Control Servlet for NBDW site. It delegates to NDBW's security Manager
 * to handler authorization
 *  
 * @author guweigu
 *
 */
public class BDControlServlet extends HttpServlet {
	private SecurityManager securityManager;
	private static final long serialVersionUID = 7320813912362272065L;
	protected Logger logger = Logger.getLogger(BDControlServlet.class);

	private static final String SYSTEM_ERROR_PAGE = "/error.jsp";

	private static final boolean DUMP_PARAMETERS = true;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		logger = Logger.getLogger(this.getClass());
		securityManager = ApplicationHelper
				.getSecurityManager(getServletContext());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * This method process the request and forward the request to the original
	 * request if the URL is public and the user is anonymous or if the users's
	 * credentials authenticated for the requestred url. Otherwise it is
	 * redirected to the login page.
	 * 
	 * @param request
	 *            HttpServletRequest object
	 * @param response
	 *            HttpServletResponse object
	 */
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		logRequestParams(request);
		doLoginAction(request, response);		
		String requestedURL = getRequestedURL(request);
		
		RequestHandler handler = getRequestHandler(requestedURL);
		// if there is a special request handler to handle this url
		// let it handle it and return
		if (handler != null) {
			handler.handleRequest(request, response);
			return;
		}

		BaseUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		boolean isAuthorized = false;
		try {
			AuthorizationSubject subject = ApplicationHelper
					.getAuthorizationSubject(request);
			isAuthorized = securityManager.isUserAuthorized(subject,
					requestedURL);
		} catch (SystemException e) {
			LogUtility.logSystemException(BDConstants.BD_APPLICATION_ID, e);
			getServletContext().getRequestDispatcher(SYSTEM_ERROR_PAGE)
					.forward(request, response);
		}
		// if the user is authorized to access this url, dispatch to it.
		if (isAuthorized) {
			dispatch(requestedURL, request, response);
		} else { // not allowed
			// Code Implemented for Access Control Violation Part of STA 25 Changes.
			try {
				BDCommonMrlLoggingUtil.logUnAuthAcess(request, "User is not authorized",  this.getClass().getName()+":"+"processRequest");
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (userProfile == null) {
				// Let the handler to deal with this url for non authenticated
				// user
				if (isDirectURL(request)) {
					StringBuffer url = request.getRequestURL();
					String queryStr = request.getQueryString();
					if (queryStr != null) {
						url.append("?" + queryStr);
					}
					request.setAttribute(BDConstants.DIRECT_URL_ATTR, url.toString());
				}				
				handleNoAccessForPublicUser(request, response, requestedURL);
			} else {
				// Let the handler to deal with this url for authenticated user
				handleNoAccessForAuthenticatedUser(request, response,
						requestedURL);
			}
		}
	}


	/**
	 * No special request handler for now.
	 * @param url
	 * @return
	 */
	protected RequestHandler getRequestHandler(String url) {
		return null;
	}

	public void dispatch(String url, HttpServletRequest request,
			HttpServletResponse response) {
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/WEB-INF" + url);
		try {
			rd.forward(request, response);
		} catch (Exception e) {
			logger.error("Dispatch Fails for '/WEB-INF" + url + "':", e);
		}
	}

	protected void handleNoAccessForAuthenticatedUser(
			HttpServletRequest request, HttpServletResponse response,
			String requestedURL) {
		dispatch(URLConstants.HomeURL, request, response);
	}

	protected void handleNoAccessForPublicUser(HttpServletRequest request,
			HttpServletResponse response, String requestedURL) {
		BDSessionHelper.invalidateSession(request, response, false);
		dispatch(URLConstants.LoginURL, request, response);
	}

	@SuppressWarnings("unchecked")
	private void logRequestParams(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("Control Servlet!!!");
			logger.debug("requested URI:" + request.getRequestURI());
			if (DUMP_PARAMETERS) {
				logger.debug("Dumping parameters:");
				Enumeration parameters = request.getParameterNames();
				while (parameters.hasMoreElements()) {
					String parameter = (String) parameters.nextElement();
					String[] values = request.getParameterValues(parameter);
					if (values == null) {
						logger.debug("Parameter [" + parameter
								+ "] does not exist.");

					} else {
						StringBuffer valuesBuffer = new StringBuffer();
						for (int i = 0; i < values.length; i++) {
							valuesBuffer.append(values[i]);
							if (i != values.length - 1) {
								valuesBuffer.append(", ");
							}
						}
						logger.debug("Parameter [" + parameter + "] is ["
								+ valuesBuffer + "]");
					}
				}
			}
		}
	}
	

	/**
	 * Returns whether the request is an URL that will redirect to it once login
	 */
	protected boolean isDirectURL(HttpServletRequest request) {
		if (StringUtils.equalsIgnoreCase("GET", request.getMethod())) {
			return true;
		}
		return false;
	}
	
	
	protected String getRequestedURL(HttpServletRequest request) {
		return request.getRequestURI().substring(
				request.getContextPath().length());
	}
	
	public void doLoginAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
    	String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
    	String SITE_PROTOCOL = Environment.getInstance().getSiteProtocol();
    	response.addCookie(new TrueIPCookie(ipAddress, SITE_PROTOCOL, BDConstants.TRUE_COOKIES_SITE_NAME));
    	    
    }
}
