package com.manulife.pension.ps.web.delegate;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.delegate.PsAbstractServiceDelegate;
import com.manulife.pension.service.messaging.MessagingException;
import com.manulife.pension.service.messaging.MessagingService;
import com.manulife.pension.service.messaging.MessagingServiceHome;

/**
 * Service Delegate that allows communication with the mainframe via MQ 
 * using of the MessagingService EJB module
 * 
 * @author drotele
 * Created on Mar 30, 2004
 */
public class MQServiceDelegate extends PsAbstractServiceDelegate {

	private static MQServiceDelegate instance = new MQServiceDelegate();

	/**
	 * constructor
	 */
	public MQServiceDelegate() {
	}

	/**
	 * @return MQServiceDelegate
	 */
	public static MQServiceDelegate getInstance() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.delegate.AbstractServiceDelegate#getHomeClassName()
	 */
	protected String getHomeClassName() {
		return MessagingServiceHome.class.getName();
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.delegate.AbstractServiceDelegate#create()
	 */
	protected EJBObject create()
		throws RemoteException, SystemException, CreateException {
		MessagingService service = null;
		try {
			service = ((MessagingServiceHome) getHome()).create();
		} catch (MessagingException e) {
			throw new SystemException(
				e,
				getClass().getName(),
				"MessagingService",
				"Create MessagingService Object");
		}
		return service;
	}

	public String sendRequestAndWaitForReply(String requestMsg)
		throws SystemException {
		MessagingService service = (MessagingService) getService();
		String responseMsg = null;

		try {
			responseMsg = service.sendRequestAndWaitForReply(requestMsg);
		} catch (RemoteException e) {
			handleRemoteException(e, "performMQCall");
		} catch (MessagingException e) {
			throw new SystemException(
				e,
				getClass().getName(),
				"performMQCall",
				e.getMessage());
		}

		return responseMsg;
	}
}
