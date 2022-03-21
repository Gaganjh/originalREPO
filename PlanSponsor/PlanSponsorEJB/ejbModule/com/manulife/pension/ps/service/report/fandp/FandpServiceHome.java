package com.manulife.pension.ps.service.report.fandp;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

public interface FandpServiceHome extends javax.ejb.EJBHome {

    /**
	 * Creates a default instance of Session Bean: FandpService
	 */
	public FandpService create() throws CreateException, RemoteException;
}
