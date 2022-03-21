package com.manulife.pension.bd.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.manulife.pension.bd.web.controller.BDApplicationFacade;
import com.manulife.pension.bd.web.controller.SecurityManager;
import com.manulife.pension.bd.web.controller.SecurityManagerConfigurator;
import com.manulife.pension.bd.web.navigation.UserNavigationFactory;
import com.manulife.pension.bd.web.navigation.generation.NavigationGeneratorImpl;
import com.manulife.pension.bd.web.navigation.generation.SystemMenuItem;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ApplicationFacade;
import com.manulife.pension.platform.web.util.CommonEnvironment;

public class BDApplicationContextInitializer implements ServletContextListener {
	private static final Logger logger = Logger
			.getLogger(BDApplicationContextInitializer.class);

	private static String SecurityInfoFile = "./securityinfo_bd.xml";

	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		CommonEnvironment.initialize(Environment.getInstance());
		ApplicationFacade facade = new BDApplicationFacade();
		facade.setServletContext(context);
		context.setAttribute(CommonConstants.APPLICATION_FACADE_KEY, facade);

		String publicMenuConfigFile = context.getInitParameter("publicMenuConfig");
		String securedMenuConfigFile = context.getInitParameter("securedMenuConfig");
		String pagesConfigFile = context.getInitParameter("pagesConfig");

		SecurityManager securityManager = null;
		try {
			securityManager = new SecurityManagerConfigurator()
					.getSecurityManager(SecurityInfoFile);
			context.setAttribute(ApplicationHelper.SecurityManager,
					securityManager);

		} catch (SystemException e) {
			logger.fatal("SecurityManager is not created successfully", e);
		}

		UserNavigationFactory navigationFactory = new UserNavigationFactory();

		ApplicationContext pubMenuCtx = new FileSystemXmlApplicationContext(
				context.getRealPath(publicMenuConfigFile));
		SystemMenuItem publicRoot = (SystemMenuItem) pubMenuCtx
				.getBean("mainMenu");
		navigationFactory.setNavigationGenerator(new NavigationGeneratorImpl(
				securityManager));
		navigationFactory.setPublicMenu(publicRoot);
		
		ApplicationContext securedMenuCtx = new FileSystemXmlApplicationContext(
				context.getRealPath(securedMenuConfigFile));

		SystemMenuItem securedRoot = (SystemMenuItem) securedMenuCtx
				.getBean("mainMenu");
		navigationFactory.setNavigationGenerator(new NavigationGeneratorImpl(
				securityManager));
		navigationFactory.setSecuredMenu(securedRoot);

		context.setAttribute(ApplicationHelper.UserNavigationFactory, navigationFactory);
		ApplicationContext pagesCtx = new FileSystemXmlApplicationContext(
				context.getRealPath(pagesConfigFile));

		context.setAttribute(ApplicationHelper.LayoutStore, pagesCtx
				.getBean("layoutStore"));

	}

	public void contextDestroyed(ServletContextEvent event) {
	}

}
