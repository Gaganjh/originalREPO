package com.manulife.pension.bd.web.exception;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.exception.LoggableException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.utility.CommonConstants;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.controller.ApplicationFacade;
import com.manulife.pension.service.account.SystemUnavailableException;
import com.manulife.pension.util.log.LogUtility;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);
	private ServletRequest servlet;

	@ExceptionHandler(SystemException.class)
	public String getSystemException(SystemException e) throws Exception {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		logDebug("SystemException caught in BDAction:" + ((LoggableException) e).getUniqueId(), e);
		LogUtility.logSystemException(getApplicationFacade(request).getApplicationId(), (SystemException) e);
		request.setAttribute("errorCode", "1099");
		request.setAttribute("uniqueErrorId", ((LoggableException) e).getUniqueId());
		// forward to Error Page
		return "/error.jsp";

	}


	protected void logDebug(String message, Throwable t) {
		if (logger.isDebugEnabled()) {
			logger.debug(message, t);
		}

	}

	protected ApplicationFacade getApplicationFacade() {
		return (ApplicationFacade) getServlet().getServletContext()
				.getAttribute(CommonConstants.APPLICATION_FACADE_KEY);
	}

	
	protected ApplicationFacade getApplicationFacade(HttpServletRequest request) {
		return (ApplicationFacade) request.getServletContext().getAttribute(CommonConstants.APPLICATION_FACADE_KEY);
	}

	
	private ServletRequest getServlet() {

		return (this.servlet);
	}

	@ExceptionHandler(SystemUnavailableException.class)
	public String getSystemUnavailableException(SystemUnavailableException ex) throws GenericException {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		logger.error(ex.getMessage(), ex);
		List<GenericException> errors = new ArrayList<GenericException>();
		errors.add(new GenericException(BDErrorCodes.OUT_OF_SERVICE_HOURS));
		//BDSessionHelper.setErrorsInSession(request, errors);
		request.setAttribute(BDConstants.IS_ERROR, Boolean.TRUE);
		return "/error.jsp";
	}

	@ExceptionHandler(ContentException.class)
	public String getContentException(Exception ex) throws GenericException {
		throw new GenericException(ex);
	}

	@ExceptionHandler(ServletException.class)
	public String getServletException(ServletException t) {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		ServletException ex = (ServletException) t;
		if (ex.getRootCause() != null) {
			logger.error(ex.getRootCause().getMessage(), ex.getRootCause());
		} else {
			logger.error(ex.getMessage(), ex);
		}
		request.getSession().removeAttribute(BdBaseController.ERROR_KEY);
		ArrayList<GenericException> error = new ArrayList<GenericException>();
		error.add(new GenericException(CommonErrorCodes.ERROR_FRWVALIDATION_WITHOUT_GUI_FIELD_NAME));
		request.getSession().setAttribute(BdBaseController.ERROR_KEY, error);
		return "/error.jsp";
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public String getHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		logger.error("Controller not having the handler method for the request URL " + request.getRequestURL().toString() +" and failure method " + ex.getMethod() + ex.getMessage());
		return "redirect:/do/home/";
	}

	@ExceptionHandler(Throwable.class)
	public String getException(Exception t) {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		logger.error(t.getMessage(), t);
		logger.error("GlobalExceptionHandler - > getException() : " + t.getStackTrace());
		request.getSession().removeAttribute(BdBaseController.ERROR_KEY);
		ArrayList<GenericException> error = new ArrayList<GenericException>();
		error.add(new GenericException(CommonErrorCodes.ERROR_FRWVALIDATION_WITHOUT_GUI_FIELD_NAME));
		request.getSession().setAttribute(BdBaseController.ERROR_KEY, error);
		return "/error.jsp";
	}

	

}
