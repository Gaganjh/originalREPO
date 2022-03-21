package com.manulife.pension.ps.web.delegate;

import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.delegate.PsAbstractServiceDelegate;
import com.manulife.pension.ps.service.loadtest.LoadTestService;
import com.manulife.pension.ps.service.loadtest.LoadTestServiceHome;

public class LoadTestServiceDelegate extends PsAbstractServiceDelegate {

	private static final LoadTestServiceDelegate instance = new LoadTestServiceDelegate();

	public static LoadTestServiceDelegate getInstance() {
		return instance;
	}
	
	@Override
	protected EJBObject create() throws SystemException, RemoteException,
			CreateException {
		return ((LoadTestServiceHome) getHome()).create();
	}

	@Override
	protected String getHomeClassName() {
		return LoadTestServiceHome.class.getName();
	}

	@SuppressWarnings("deprecation")
	public Map<String, Object> invokeStoredProc(String schemaName, String storedProcName,
			Map<String, String> inputValues) throws SystemException {
		Map<String, Object> result = null;
		try {
			LoadTestService service = (LoadTestService) getService();
			result = service.invokeStoredProc(schemaName, storedProcName, inputValues);
		} catch (RemoteException e) {
			handleRemoteException(e, "invokeStoredProc");
		}
		return result;
	}
}
