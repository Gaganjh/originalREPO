package com.manulife.pension.ps.service.loadtest;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface LoadTestServiceHome extends EJBHome {
	/**
	 * Creates a default instance of Session Bean: LoadTestService
	 */
	public LoadTestService create() throws CreateException, RemoteException;

}
