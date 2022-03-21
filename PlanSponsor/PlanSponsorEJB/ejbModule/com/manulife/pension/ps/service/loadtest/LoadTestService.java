package com.manulife.pension.ps.service.loadtest;

import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.EJBObject;


import com.manulife.pension.exception.SystemException;

public interface LoadTestService extends EJBObject {
	public Map<String, Object> invokeStoredProc(String schemaName,
			String storedProcName, Map<String, String> inputValues)
			throws RemoteException, SystemException;
}
