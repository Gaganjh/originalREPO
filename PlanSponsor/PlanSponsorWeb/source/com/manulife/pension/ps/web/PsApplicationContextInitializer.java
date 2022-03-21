package com.manulife.pension.ps.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.manulife.pension.platform.web.controller.ApplicationFacade;
import com.manulife.pension.platform.web.util.CommonEnvironment;import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsApplicationFacade;
import com.manulife.pension.ps.web.util.Environment;

public class PsApplicationContextInitializer implements ServletContextListener {
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		CommonEnvironment.initialize(Environment.getInstance());
		ApplicationFacade facade = new PsApplicationFacade();
		facade.setServletContext(context);
		context.setAttribute(Constants.APPLICATION_FACADE_KEY, facade);
	}
	
	public void contextDestroyed(ServletContextEvent event) {
	}
}
