package com.manulife.pension.ps.web.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;



import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.log.ServiceLogRecord;

public class CommonMrlLoggingUtil {
	/** 
	 * Psw - log unAuthorizes access in MRL
	 * @param request
	 * @param transactionName
	 * @param className
	 * @throws SystemException
	 */
	
	public static void logUnAuthAcess(HttpServletRequest request, String transactionName, String className) throws SystemException
   	{	
		if (isMrlLoggingFunctionalityAvailable()){
    	Logger interactionLog = Logger.getLogger(ServiceLogRecord.class);
    	UserProfile userProfile = SessionHelper.getUserProfile(request);
    	if (userProfile != null ){
        ServiceLogRecord record = new ServiceLogRecord();
   		Date date = new Date();
   		StringBuilder sf = new StringBuilder();
   		record.setApplicationId("PlanSponsor");
   		String accessUrl = request.getRequestURI();
   		if (accessUrl.contains("/WEB-INF/")) {
        accessUrl = accessUrl.replace("/WEB-INF/do", "");
   		} else if (accessUrl.contains("/do/")) {
        accessUrl = accessUrl.replace("/do", "");
   		}
   		String profileId = String.valueOf(userProfile.getAbstractPrincipal().getProfileId()) != null ?
   		String.valueOf(userProfile.getAbstractPrincipal().getProfileId()) : null;
   		sf.append("ProfileId :"+ profileId );
   		String userName = userProfile.getAbstractPrincipal().getUserName() != null ?
   		userProfile.getAbstractPrincipal().getUserName() : null;
   		sf.append(",UserName :"+userName);
   		record.setUserIdentity(profileId+" : "+userName); 	
   		sf.append(",Ip Address :" +IPAddressUtils.getRemoteIpAddress(request));
   		sf.append(",AccessUrl :"+accessUrl);
   		record.setData(sf.toString());
   		record.setMethodName(className); // Logging Point
   		record.setServiceName(transactionName);
   		record.setCode(1);		
   		record.setMilliSeconds(date.getTime());
        interactionLog.error(record);
   		}
		}
   	}
	/** 
	 * Check the switch is ON/OFF in Naming variables
	 * @return
	 * @throws SystemException
	 */
	public static boolean isMrlLoggingFunctionalityAvailable()
		    throws SystemException
		  {
		    BaseEnvironment environment = new BaseEnvironment();
		    String accessControlViolationLoggingEnabled = environment.getNamingVariable(Constants.ACCESS_CONTROL_VIOLATION_ENABLED, null);
		    if (StringUtils.isBlank(accessControlViolationLoggingEnabled))
		    {
		  	 throw new IllegalArgumentException("invalid value for the naming variable for accessControlViolationLoggingEnabled: " + accessControlViolationLoggingEnabled);
		    }
		    else if("Y".equalsIgnoreCase(accessControlViolationLoggingEnabled.trim())) {
		      return true;
		    }
		    return false;
		  }

}
