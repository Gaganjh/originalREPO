package com.manulife.pension.bd.web.tracking;

import java.io.Serializable;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.bd.log.BDLogoutLog;
import com.manulife.pension.util.log.EventLogFactory;
import com.manulife.pension.util.log.LogUtility;

/**
 * Catch session ending event to log the Logout event
 * 
 * @author guweigu
 * 
 */
public class BDLogoutTracker implements HttpSessionBindingListener,
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BDPrincipal principal;

	public BDLogoutTracker(BDPrincipal principal) {
		this.principal = principal;
	}

	public void valueBound(HttpSessionBindingEvent event) {
	}

	public void valueUnbound(HttpSessionBindingEvent event) {
		try {
			BDLogoutLog eventLog = (BDLogoutLog) EventLogFactory.getInstance()
					.createEventLog(BDLogoutLog.class);
			eventLog.setClassName(this.getClass().getName());
			eventLog.setMethodName("valueUnbound");
			eventLog.setUserName(principal.getUserName());
			eventLog.setPricipal(principal);
			eventLog.log();
		} catch (Exception e) {
			LogUtility
					.logSystemException(
							BDConstants.BD_APPLICATION_ID,
							new SystemException(e, this.getClass().getName(),
									"valueUnbound",
									"Problem occurred during logging the Logout/Unallocated activity."));
		}
	}
}
