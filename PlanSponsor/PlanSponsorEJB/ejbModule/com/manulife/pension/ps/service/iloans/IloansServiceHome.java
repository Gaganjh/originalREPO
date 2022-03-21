/*
 * Created on May 31, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.service.iloans;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IloansServiceHome extends javax.ejb.EJBHome 
{
	/**
	 * Creates a default instance of Session Bean: SecurityService
	 */
	public IloansService create() throws CreateException, RemoteException;
}


