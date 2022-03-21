package com.manulife.pension.bd.web.util;

import org.apache.log4j.Logger;

/**
 * Helper class that contains utility method for JSP pages
 * like logging...
 * 
 * @author guweigu
 *
 */
public class JspHelper {
	private static Logger log = Logger.getLogger(JspHelper.class);
	
	/**
	 * Log the exception in a page with pageName, exception and message
	 * 
	 * @param pageName
	 * @param e
	 * @param message
	 */
	public static void log(String pageName, Exception e, String message) {
		log.error(pageName + ": " + message, e);
	}
}
