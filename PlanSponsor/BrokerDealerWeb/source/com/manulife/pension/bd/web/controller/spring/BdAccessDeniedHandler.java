package com.manulife.pension.bd.web.controller.spring;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ApplicationFacade;
import com.manulife.pension.platform.web.controller.BaseUserProfile;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.bd.web.controller.spring.HttpSessionUtility;


public class BdAccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
	 
	private static final Logger LOGGER= Logger.getLogger(BdAccessDeniedHandler.class);
	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response, AccessDeniedException exception)
			throws IOException, ServletException {
		RequestDispatcher dispatcher;
		HttpSession session = request.getSession(false);
		StringBuffer csrfLogMsg = new StringBuffer();
        csrfLogMsg.append("CSRF forged Request detected: [Page Request: " + request.getRequestURL() + request.getQueryString());
        
        if (session != null) {
      	  BaseUserProfile userProfile = BaseSessionHelper.getBaseUserProfile(request);
        String ipAddress = request.getHeader(CommonConstants.FORWARD_HEADER_KEY);   
        if (ipAddress == null) {   
                        ipAddress = request.getRemoteAddr();   
        } 
          csrfLogMsg.append(" IP Address: " + ipAddress); 
       } 
        csrfLogMsg.append("]");
        LOGGER.error(csrfLogMsg.toString());
        if (session==null || (exception instanceof MissingCsrfTokenException && request.getRequestedSessionId() != null
  		        && !request.isRequestedSessionIdValid()) ){
      	  HttpSessionUtility.handleUnknownUser(request, response);
        }
    			request.getSession().setAttribute("Error Message", CommonConstants.ERROR_RDRCT);
  		//dispatcher = request.getRequestDispatcher("/WEB-INF/security/CSRFerrorpage.jsp");
  		getApplicationFacade(request).createLayoutBean(request,"/security/CSRFerrorpage.jsp");
  		dispatcher = request.getRequestDispatcher("/WEB-INF/layouts/public_layout.jsp");
  		dispatcher.forward(request, response);
  	}
	
	protected ApplicationFacade getApplicationFacade(HttpServletRequest request) {
		return (ApplicationFacade) request.getServletContext().getAttribute(CommonConstants.APPLICATION_FACADE_KEY);
	}
}
