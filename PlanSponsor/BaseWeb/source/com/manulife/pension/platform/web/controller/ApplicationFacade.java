package com.manulife.pension.platform.web.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.platform.web.util.CommonEnvironment;

public interface ApplicationFacade {
	void setServletContext(ServletContext context);
	
	CommonEnvironment getEnvironment();

	String createLayoutBean(HttpServletRequest request,
			String forward);
	
	// From refactoring of PSW application that requires
	// a pre execute action for all of the action
	// TODO: consider to move this to a filter
	void actionPreExecute(HttpServletRequest request);
	
	String getApplicationId();
}
