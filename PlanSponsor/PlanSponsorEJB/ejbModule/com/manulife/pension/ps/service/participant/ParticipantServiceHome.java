package com.manulife.pension.ps.service.participant;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Home interface for Enterprise Bean: ParticipantService
 */
public interface ParticipantServiceHome extends EJBHome {
	/**
	 * Creates a default instance of Session Bean: ParticipantService
	 */
	public ParticipantService create() throws CreateException, RemoteException;
}
