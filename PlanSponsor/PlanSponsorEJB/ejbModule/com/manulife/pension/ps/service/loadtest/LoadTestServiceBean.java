package com.manulife.pension.ps.service.loadtest;

import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.manulife.pension.exception.SystemException;

public class LoadTestServiceBean implements SessionBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
     * ejbCreate
     */
    public void ejbCreate() throws CreateException {
    }

	public void ejbActivate() throws EJBException, RemoteException {
	}

	public void ejbPassivate() throws EJBException, RemoteException {
	}

	public void ejbRemove() throws EJBException, RemoteException {
	}

	public void setSessionContext(SessionContext arg0) throws EJBException,
			RemoteException {
	}

	/**
	 * Invoke a stored proc with input values and returns the output
	 * 
	 * @param schemaName
	 * @param storedProcName
	 * @param inputValues
	 * @return
	 * @throws SystemException
	 */
	public Map<String, Object> invokeStoredProc(String schemaName, String storedProcName,
			Map<String, String> inputValues) throws SystemException {

		StoredProcInvoker invoker = new StoredProcInvoker();
		return invoker.invoke(schemaName, storedProcName, inputValues);
	}

}
