package com.manulife.pension.ps.service.lock;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.lock.valueobject.Lock;

public interface LockService extends EJBObject {
    public boolean lock(String componentName, String componentKey, long profileId) throws RemoteException, SystemException;
    public boolean lock(String componentName, String componentKey, long profileId, int totalNoOfAllowedLocks, boolean checkDuplicateUserLock, long lockExpiryTime) throws RemoteException, SystemException;
    public void releaseLock(String componentName, String componentKey) throws RemoteException, SystemException;
    public Lock getLockInfo(String componentName, String componentKey) throws RemoteException, SystemException;
	public void releaseAllLocksForUser(long profileId) throws RemoteException, SystemException;
}
