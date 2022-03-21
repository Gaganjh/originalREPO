package com.manulife.pension.ps.common.util.log;

import java.util.Date;

import org.apache.log4j.Logger;

import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.util.log.LogEventException;
import com.manulife.pension.util.log.ServiceLogRecord;

public class SubmissionEventLog {
	
	private static final String NULL_IDENTITY = "null";
	private static final int USER_IDENTITY_DB_LIMIT = 30;
	protected static String SERVICE_NAME = "Submission";
	private static final String SEPARATOR = ":";
	
	private String updatedBy;
	private Date updatedTS = new Date();
	private Principal principal=null;	
	private String className=null;
	private String methodName=null;
	private String userName=null;
	protected Logger logger = null;
	private String logData = null;
	
	public SubmissionEventLog() {
		logger = Logger.getLogger(SubmissionEventLog.class);		
	}

	/**
	 * This method logs the event log.
	 *  
	 * @throws LogEventException 
	 * 		If any required information is missing.
	 */
	public void log() throws LogEventException {
		ServiceLogRecord record = new ServiceLogRecord();
		record.setApplicationId(Constants.PS_APPLICATION_ID);
		record.setData(getLogData());
		record.setMethodName(getClassName() + SEPARATOR + getMethodName()); // Logging Point
		if (getPrincipal() != null) {
			record.setUserIdentity(getPrincipal().getProfileId() + SEPARATOR + getPrincipal().getUserName());
		} else if (getUserName() != null) {
			record.setUserIdentity(getUserName().substring(0, Math.min(getUserName().length(), USER_IDENTITY_DB_LIMIT)));
		} else {
			record.setUserIdentity(NULL_IDENTITY);
		}
		record.setServiceName(SERVICE_NAME);
		record.setMilliSeconds(System.currentTimeMillis());

		// Log the record to MRL
		logger.error(record);
	}

	/**
	 * Gets the methodName
	 * @return Returns a String
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Sets the methodName
	 * @param methodName The methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * Gets the className
	 * @return Returns a String
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Sets the className
	 * @param className The className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Gets the userName
	 * @return Returns a String
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the userName
	 * @param userName The userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * Gets the logData
	 * @return Returns a String
	 */
	public String getLogData() {
		return logData;
	}

	/**
	 * Sets the logData
	 * @param logData The logData to set
	 */
	public void setLogData(String logData) {
		this.logData = logData;
	}

	/**
	 * @return Returns the principal.
	 */
	public Principal getPrincipal() {
		return principal;
	}

	/**
	 * @param principal The principal to set.
	 */
	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}
}
