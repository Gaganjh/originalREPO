package com.manulife.pension.platform.web.controller;


import java.lang.reflect.InvocationTargetException;
 import java.lang.reflect.Method;
 import java.net.URLEncoder;

import org.apache.log4j.Logger;
public class ResponseUtils {
	protected static Logger logger = null;
	@SuppressWarnings("rawtypes")
	public ResponseUtils(Class clazz) {
		logger = Logger.getLogger(clazz);
	}
	private static Method encode = null;
	 public static String encodeURL(String url) {
		         return encodeURL(url, "UTF-8");
		     }
	static {
		         try {
		             // get version of encode method with two String args
		             Class[] args = new Class[] { String.class, String.class };
		 
		             encode = URLEncoder.class.getMethod("encode", args);
		         } catch (NoSuchMethodException e) {
		        	 logger.debug("Could not find Java 1.4 encode method.  Using deprecated version.",
		                 e);
		         }
		     }
	public static String encodeURL(String url, String enc) {
		        try {
		            if ((enc == null) || (enc.length() == 0)) {
		                enc = "UTF-8";
		            }
		
		            // encode url with new 1.4 method and UTF-8 encoding
		            if (encode != null) {
		                return (String) encode.invoke(null, new Object[] { url, enc });
		            }
		        } catch (IllegalAccessException e) {
		            logger.debug("Could not find Java 1.4 encode method.  Using deprecated version.",
		                e);
		        } catch (InvocationTargetException e) {
		            logger.debug("Could not find Java 1.4 encode method. Using deprecated version.",
		                e);
		        }
		
		        return URLEncoder.encode(url);
		    }
		

}
