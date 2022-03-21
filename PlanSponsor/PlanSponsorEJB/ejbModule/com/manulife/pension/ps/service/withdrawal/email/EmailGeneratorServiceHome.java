/*
 * Created on Jan 12, 2007
 *
 */
package com.manulife.pension.ps.service.withdrawal.email;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author Mihai Popa
 * 
 */
public interface EmailGeneratorServiceHome extends EJBHome {

	/**
	 * Creates a default instance of Session Bean: EmailGeneratorService
	 */
	public EmailGeneratorService create() throws CreateException, RemoteException;
}
