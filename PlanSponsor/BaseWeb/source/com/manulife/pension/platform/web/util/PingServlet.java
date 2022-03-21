package com.manulife.pension.platform.web.util;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The PingServlet handles the request from the keepAlive tag and
 * just returns a response.  
 *  
 * @author ayyalsa
 *
 */
public class PingServlet extends HttpServlet {


	/** A default Serial Version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#init(ServletConfig)
	 * 
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
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
	 * This method process the request and creates a empty response
	 *  
	 * @param request
	 *            HttpServletRequest object
	 * @param response
	 *            HttpServletResponse object
	 */
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/**
		 * TODO does this statement is required?
		 */
		response.getWriter().println("");
	}
}
