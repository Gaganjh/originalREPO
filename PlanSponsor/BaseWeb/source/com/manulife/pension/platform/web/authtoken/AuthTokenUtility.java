package com.manulife.pension.platform.web.authtoken;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.util.BaseEnvironment;


/**
 * Authentication Token Utility Class.
 * 
 *
 */
public class AuthTokenUtility {

	private static final BaseEnvironment environment = new BaseEnvironment();

	public static final String JWT_TOKEN_SERVICE_URL = environment.getNamingVariable(CommonConstants.JWT_TOKEN_SERVICE_URL, null);
	
	public static final String JWT_TOKEN_SERVICE_PSW_APP_ID = environment.getNamingVariable(CommonConstants.JWT_TOKEN_SERVICE_PSW_APP_ID, null);
	
	public static final String JWT_TOKEN_SERVICE_FRW_APP_ID = environment.getNamingVariable(CommonConstants.JWT_TOKEN_SERVICE_FRW_APP_ID, null);
	
	public static final String BEARER_TOKEN_SERVICE_URL = environment.getNamingVariable(CommonConstants.BEARER_TOKEN_SERVICE_URL, null);

	public static final String GENERATE_BEARER_TOKEN_SERVICE_PSW_CLIENT_ID = environment.getNamingVariable(CommonConstants.GENERATE_BEARER_TOKEN_SERVICE_PSW_CLIENT_ID, null);
	
	public static final String GENERATE_BEARER_TOKEN_SERVICE_PSW_CLIENT_SECRET = environment.getNamingVariable(CommonConstants.GENERATE_BEARER_TOKEN_SERVICE_PSW_CLIENT_SECRET, null);
	
	public static final String GENERATE_BEARER_TOKEN_SERVICE_FRW_CLIENT_ID = environment.getNamingVariable(CommonConstants.GENERATE_BEARER_TOKEN_SERVICE_FRW_CLIENT_ID, null);

	public static final String GENERATE_BEARER_TOKEN_SERVICE_FRW_CLIENT_SECRET = environment.getNamingVariable(CommonConstants.GENERATE_BEARER_TOKEN_SERVICE_FRW_CLIENT_SECRET, null);

	public static final String BEARER_TOKEN_SERVICE_GRANT_TYPE = environment.getNamingVariable(CommonConstants.BEARER_TOKEN_SERVICE_GRANT_TYPE, null);
	
	
    public static final String REDIRECTED_TO_ISAM_EAI_MLC_URL = environment.getNamingVariable(CommonConstants.REDIRECTED_TO_ISAM_EAI_MLC_URL, null);
	
	public static final String REDIRECTED_TO_ISAM_EAI_MLC_AUTHORIZATION = environment.getNamingVariable(CommonConstants.REDIRECTED_TO_ISAM_EAI_MLC_AUTHORIZATION, null);

	public static final String TOKEN_SERVICE_SSL_CIPHER = environment.getNamingVariable(CommonConstants.TOKEN_SERVICE_SSL_CIPHER, null);
	
	/**
	 * Making it private.
	 */
	private AuthTokenUtility() {

	}

	/**
	 * A convenient method to add top level stack trace to error message.
	 * 
	 * @param msg
	 * @param e
	 * @return error message that includes top level stack trace.
	 */
	public static String getErrorMessageWithStackTrace(String msg, Exception e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		String stackTrace = stringWriter.toString();
		return msg + " --> error: " + e.getMessage() + " StackTrace: " + stackTrace.substring(0, 1000);
	}
}
