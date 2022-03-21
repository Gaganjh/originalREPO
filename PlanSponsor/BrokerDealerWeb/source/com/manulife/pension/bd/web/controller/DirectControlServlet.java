package com.manulife.pension.bd.web.controller;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.util.BDSessionHelper;


/**
 * The Control Servlet to handle direct urls with just /URL pattern
 *  
 * @author guweigu
 *
 */
public class DirectControlServlet extends BDControlServlet {
	private static final long serialVersionUID = 7320813912362272065L;
	protected Logger logger = Logger.getLogger(DirectControlServlet.class);



	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		logger = Logger.getLogger(this.getClass());
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
		super.processRequest(request, response);
		
	}


	protected void handleNoAccessForAuthenticatedUser(
			HttpServletRequest request, HttpServletResponse response,
			String requestedURL) {
		if(requestedURL != null && requestedURL.contains("ViewRIAStatements")){
			dispatch(URLConstants.HomeURL +"?" + "nextURL=" + URLConstants.RiaDirectEstatements, request, response);
		}else{
			dispatch(URLConstants.HomeURL, request, response);
		}
	}

	protected void handleNoAccessForPublicUser(HttpServletRequest request,
			HttpServletResponse response, String requestedURL) {
		if (requestedURL != null && requestedURL.contains("ViewRIAStatements")) {
			dispatch(URLConstants.LoginURL  +"?" + "nextURL=" + URLConstants.RiaDirectEstatements, request, response);
		} else {
			BDSessionHelper.invalidateSession(request, response, false);
			dispatch(URLConstants.LoginURL, request, response);
		}
	}
	
	protected String getRequestedURL (HttpServletRequest request) {
		
		String requestedURL = super.getRequestedURL(request);
		
		if (StringUtils.equalsIgnoreCase(requestedURL, "/ViewRIAStatements")){
			requestedURL = "/do"+ requestedURL;
		}
		
		return requestedURL;
	}
	
}
