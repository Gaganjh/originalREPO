package com.manulife.pension.platform.web.delegate;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.delegate.AbstractServiceDelegate;
import com.manulife.pension.ps.service.delegate.PsAbstractServiceDelegate;
import com.manulife.pension.ps.service.lock.LockService;
import com.manulife.pension.ps.service.lock.LockServiceHome;
import com.manulife.pension.ps.service.lock.valueobject.Lock;

/**
 * Service Delegate that implement lock function
 * 
 * @author narintr
 */
public class LockServiceDelegate extends PsAbstractServiceDelegate {
	private static LockServiceDelegate instance = new LockServiceDelegate();

	/**
	 * constructor
	 */
	public LockServiceDelegate() {
	}

	/**
	 * @return LockServiceDelegate
	 */
	public static LockServiceDelegate getInstance() {
		return instance;
	}

    /**
     * @see AbstractServiceDelegate#getHomeClassName()
     */
    protected String getHomeClassName() {
        return LockServiceHome.class.getName();
    }

    /**
     * @see AbstractServiceDelegate#getRemote(EJBHome)
     */
    protected EJBObject create() throws SystemException, RemoteException, CreateException {
        return ((LockServiceHome)getHome()).create();
    }
    
    public boolean lock(String componentName, String componentKey, long profileId) throws SystemException {
        boolean locked = false;
        LockService lockService = (LockService)getService();
        
        try {
            locked = lockService.lock(componentName, componentKey, profileId);
        } catch (RemoteException e) {
        	logAndCreateSystemException(e);
        }
        
        return locked;
    }
    
    public void releaseLock(String componentName, String componentKey) throws SystemException {
        LockService lockService = (LockService)getService();
        
        try {
            lockService.releaseLock(componentName, componentKey);
        } catch (RemoteException e) {
        	logAndCreateSystemException(e);
        }
    }
    
    /**
     * Release all locks for the user with the given profileId.
     * 
     * @param profileId The profile id of the user whose locks should be released
     * @throws SystemException
     */
    public void releaseAllLocksForUser(long profileId) throws SystemException {
        LockService lockService = (LockService)getService();
        
        try {
            lockService.releaseAllLocksForUser(profileId);
        } catch (RemoteException e) {
        	logAndCreateSystemException(e);
        }
    }

    public Lock getLockInfo(String componentName, String componentKey) throws SystemException {
        
    	Lock lockInfo = null;

        try {
        	lockInfo = ((LockService)getService()).getLockInfo(componentName, componentKey);
        } catch (RemoteException e) {
        	logAndCreateSystemException(e);
        }
        
        return lockInfo;
    }
}
