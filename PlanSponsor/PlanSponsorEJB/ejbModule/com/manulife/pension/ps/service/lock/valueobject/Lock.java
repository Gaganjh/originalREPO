package com.manulife.pension.ps.service.lock.valueobject;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This is the value object for the lock
 * 
 * @author David Li
 */
public class Lock implements Serializable {
    private static final long serialVersionUID = 7984907852803868856L;

    private static final long LOCK_TIMEOUT = 1800000; // 30 minutes
	
    private String componentName;
    private String componentKey;
    private Timestamp lockCreateTs;
    private long lockUserProfileId;
    
	public Lock() {
        super();
	}

	/**
     * @param componentName
     * @param componentKey
     * @param lockCreateTs
     * @param lockUserProfileId
     */
    public Lock(String componentName, String componentKey, Timestamp lockCreateTs, long lockUserProfileId) {
        super();
        this.componentName = componentName;
        this.componentKey = componentKey;
        this.lockCreateTs = lockCreateTs;
        this.lockUserProfileId = lockUserProfileId;
    }

    /**
     * @return the componentKey
     */
    public String getComponentKey() {
        return componentKey;
    }

    /**
     * @param componentKey the componentKey to set
     */
    public void setComponentKey(String componentKey) {
        this.componentKey = componentKey;
    }

    /**
     * @return the componentName
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @param componentName the componentName to set
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * @return the lockCreateTs
     */
    public Timestamp getLockCreateTs() {
        return lockCreateTs;
    }

    /**
     * @param lockCreateTs the lockCreateTs to set
     */
    public void setLockCreateTs(Timestamp lockTs) {
        this.lockCreateTs = lockTs;
    }

    /**
     * @return the lockUserProfileId
     */
    public long getLockUserProfileId() {
        return lockUserProfileId;
    }

    /**
     * @param lockUserProfileId the lockUserProfileId to set
     */
    public void setLockUserProfileId(long profileId) {
        this.lockUserProfileId = profileId;
    }

    /**
     * @return if the lock is active
     */
    public boolean isExpired() {
		return (System.currentTimeMillis() - lockCreateTs.getTime()) > LOCK_TIMEOUT;
	}
    
    /**
     * @param timeoutPeriod Timeout value in milliseconds
     * @return if the lock is active
     */
    public boolean isExpired(long timeoutPeriod) {
		return (System.currentTimeMillis() - lockCreateTs.getTime()) > timeoutPeriod;
	}
	
    /**
     * @return lock string
     */
	public String toString() {
		return new StringBuffer("lock: ")
            .append(componentName).append(", ")
            .append(componentKey).append(", ")
            .append(lockCreateTs).append(", ")
            .append(lockUserProfileId).append(", ")
            .toString();
	}
}
