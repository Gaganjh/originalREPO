package com.manulife.pension.ps.web.delegate.mockable;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.EJBMetaData;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.ejb.RemoveException;

import com.manulife.pension.service.account.AccountService;
import com.manulife.pension.service.account.AccountServiceHome;

/**
 * @author drotele
 *
 * Mocked AccountService Home
 * To be used by Service Delegates Unit Tests
 * 
 */
public class MockAccountServiceHome  implements AccountServiceHome {

	/**
	 * @see ApolloServiceHome#create()
	 */
	public AccountService create() throws CreateException, RemoteException {
		return new MockAccountService();
	}

	/**
	 * @see EJBHome#remove(Handle)
	 */
	public void remove(Handle arg0) throws RemoteException, RemoveException {
	}

	/**
	 * @see EJBHome#remove(Object)
	 */
	public void remove(Object arg0) throws RemoteException, RemoveException {
	}
	/**
	 * @see EJBHome#getEJBMetaData()
	 */
	public EJBMetaData getEJBMetaData() throws RemoteException {
		return null;
	}

	/**
	 * @see EJBHome#getHomeHandle()
	 */
	public HomeHandle getHomeHandle() throws RemoteException {
		return null;
	}
    
}


