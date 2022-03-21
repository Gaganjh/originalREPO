package com.manulife.pension.ps.service.lock;

import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.lock.dao.LockDAO;
import com.manulife.pension.ps.service.lock.valueobject.Lock;

/**
 * Bean implementation class for Enterprise Bean: SecurityService
 */
public class LockServiceBean implements SessionBean {
    private static final long serialVersionUID = -2929989432575277461L;

    private Logger logger = Logger.getLogger(LockServiceBean.class);

    private SessionContext mySessionCtx;

    /**
     * getSessionContext
     */
    public SessionContext getSessionContext() {
        return mySessionCtx;
    }

    /**
     * setSessionContext
     */
    public void setSessionContext(SessionContext ctx) {
        mySessionCtx = ctx;
    }

    /**
     * ejbActivate
     */
    public void ejbActivate() {
    }

    /**
     * ejbCreate
     */
    public void ejbCreate() throws CreateException {
    }

    /**
     * ejbPassivate
     */
    public void ejbPassivate() {
    }

    /**
     * ejbRemove
     */
    public void ejbRemove() {
    }

    /**
     * 
     * @param component
     * @param key
     * @param profileId
     * @return true if locked successfully, false if has already been locked
     */
    public boolean lock(String componentName, String componentKey, long profileId)
            throws SystemException {
        Lock lock = LockDAO.getLock(componentName, componentKey);

        // No lock
        if (lock == null) {
            LockDAO.addLock(componentName, componentKey, profileId);
            return true;
        } else {
            // It's my Lock
            if (lock.getLockUserProfileId() == profileId) {
                LockDAO.updateLock(componentName, componentKey, profileId);
                return true;
            } else {
                // It's not my lock but is expired
                if (lock.isExpired()) {
                    LockDAO.deleteLock(componentName, componentKey);
                    LockDAO.addLock(componentName, componentKey, profileId);
                    return true;
                }
            }
        }

        return false;
    }
    /**
     * 
     * @param component
     * @param key
     * @param profileId
     * @return true if locked successfully, false if has already been locked
     */
    public synchronized static boolean lock(String componentName, String componentKey, long profileId, int totalNoOfAllowedLocks, boolean checkDuplicateUserLock, long lockExpiryTime)
    		throws SystemException {
    	List<Lock> lockList = LockDAO.getLock(componentName);
    	boolean lockAvailable=false;
    	//total no of locks available is equal to totalNoOfAllowedLocks, so wait for a lock to release
    	if(lockList!=null && lockList.size()==totalNoOfAllowedLocks){
    		// check if It's my Lock
    		Iterator<Lock> iterator = lockList.iterator();
    		while(iterator.hasNext()){
    			Lock lock = iterator.next();
    				if(checkDuplicateUserLock && lock.getLockUserProfileId()== profileId){
    					LockDAO.updateLock(componentName, componentKey, profileId);
    					lockAvailable= true;
    					break;
    				}
    			// I don't have my lock in, but there is an expired one
    			else if (lock.isExpired(lockExpiryTime)) {
    				LockDAO.deleteLock(componentName, lock.getComponentKey());
    				LockDAO.addLock(componentName, componentKey, profileId);
    				lockAvailable= true;
    				break;
    			}
    		}//end of while loop
    	}	
    	// No current lock, so you are good to go
    	else if(lockList!=null && lockList.size()<totalNoOfAllowedLocks) {
    		LockDAO.addLock(componentName, componentKey, profileId);
    		lockAvailable= true;
    	}

    	return lockAvailable;
    }
    /**
     * 
     * @param component
     * @param key
     */
    public void releaseLock(String componentName, String componentKey) throws SystemException {
        LockDAO.deleteLock(componentName, componentKey);
    }
    
    /**
     * Release all locks for the user with the given profileId.
     * 
     * @param profileId The profile id of the user whose locks should be released
     * @throws SystemException
     */
    public void releaseAllLocksForUser(long profileId) throws SystemException {
        LockDAO.deleteAllLocksForUser(profileId);
    }

    public Lock getLockInfo(String componentName, String componentKey) throws SystemException {
        return LockDAO.getLock(componentName, componentKey);
    }
    
    
}
