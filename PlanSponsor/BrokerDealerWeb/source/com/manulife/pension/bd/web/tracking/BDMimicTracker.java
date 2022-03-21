package com.manulife.pension.bd.web.tracking;

import java.io.Serializable;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.bd.log.EndMimicLog;
import com.manulife.pension.service.security.bd.log.StartMimicLog;
import com.manulife.pension.util.log.EventLogFactory;
import com.manulife.pension.util.log.LogUtility;

/**
 * The tracker for starting/ending mimic
 * @author guweigu
 *
 */
public class BDMimicTracker implements Serializable, HttpSessionBindingListener {

	private BDPrincipal internalUser;
	private long externalUserProfileId;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BDMimicTracker(BDPrincipal internalUser, long externalUserProfileId) {
		this.internalUser = internalUser;
		this.externalUserProfileId = externalUserProfileId;
	}

	public void valueBound(HttpSessionBindingEvent arg0) {
		try {
			StartMimicLog eventLog = (StartMimicLog) EventLogFactory
					.getInstance().createEventLog(StartMimicLog.class);
			eventLog.setClassName(this.getClass().getName());
			eventLog.setMethodName("valueUnbound");
			eventLog.setUserName(internalUser.getUserName());
			eventLog.setPricipal(internalUser);
			eventLog.setMimickedUserProfileId(externalUserProfileId);
			eventLog.log();
		} catch (Exception e) {
			LogUtility
					.logSystemException(
							BDConstants.BD_APPLICATION_ID,
							new SystemException(e, this.getClass().getName(),
									"valueBound",
									"Problem occurred during logging starting mimic activity."));
		}
	}

	public void valueUnbound(HttpSessionBindingEvent arg0) {
		try {
			EndMimicLog eventLog = (EndMimicLog) EventLogFactory.getInstance()
					.createEventLog(EndMimicLog.class);
			eventLog.setClassName(this.getClass().getName());
			eventLog.setMethodName("valueUnbound");
			eventLog.setUserName(internalUser.getUserName());
			eventLog.setPricipal(internalUser);
			eventLog.setMimickedUserProfileId(externalUserProfileId);
			eventLog.log();
		} catch (Exception e) {
			LogUtility
					.logSystemException(
							BDConstants.BD_APPLICATION_ID,
							new SystemException(e, this.getClass().getName(),
									"valueUnbound",
									"Problem occurred during logging ending mimic activity."));
		}
	}
}
