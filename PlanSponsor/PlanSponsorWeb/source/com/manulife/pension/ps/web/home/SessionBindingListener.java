package com.manulife.pension.ps.web.home;

import java.io.Serializable;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.SignonCounter;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.UnallocatedUser;
import com.manulife.pension.service.security.utility.log.PsEventLog;
import com.manulife.pension.util.log.EventLogFactory;
import com.manulife.pension.util.log.LogUtility;

/**
 * This class is used to log the Logut/timeout event.
 *
 * @author Ilker Celikyilmaz
 */
public class SessionBindingListener implements HttpSessionBindingListener, Serializable
{
	private static Logger logger = Logger.getLogger(SessionBindingListener.class);

	private String userName;
	private Principal principal;

	public SessionBindingListener(Principal principal, String userName)
	{
		this.principal = principal;
		this.userName = userName;
	}

	public void valueBound(HttpSessionBindingEvent event)
	{
		SignonCounter.increment();
	}

	public void valueUnbound(HttpSessionBindingEvent event)
	{
		try {
			SignonCounter.decrement();

	    	// Log the event
	    	PsEventLog eventLog = null;
	    	if ( principal.getRole() instanceof UnallocatedUser )
	    	{
		    	eventLog = (PsEventLog) EventLogFactory.getInstance().createEventLog(EventLogFactory.UNALLOCATED_LOGOUT_EVENT_LOG);
		    	// For Unallocated user we kept the contract number in profileid attribute
		    	eventLog.addLogInfo(PsEventLog.CONTRACT_NUMBER, Long.toString(principal.getProfileId()));
	    	}
	    	else {
		    	eventLog = (PsEventLog) EventLogFactory.getInstance().createEventLog(EventLogFactory.LOGOUT_EVENT_LOG);
		    	eventLog.setPrincipal(principal);
	    	}

	    	eventLog.setClassName(this.getClass().getName());
	    	eventLog.setMethodName("valueUnbound");
	    	eventLog.setUserName(userName);

			eventLog.log();
		}
		catch (Exception e) {
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(e, this.getClass().getName(), "valueUnbound", "Problem occurred during logging the Logout/Unallocated activity."));
		}
	}
}
