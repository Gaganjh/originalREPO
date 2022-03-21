package com.manulife.pension.ps.web.tools.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.service.submission.valueobject.Lockable;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;

/**
 * @author parkand
 */
public class LockManager implements Serializable, HttpSessionBindingListener {

    private static final String TRANSFER_CONTRIBUTION = "X";

    private static final String CONTRIBUTION_TYPE = "C";

    private Map locks = new HashMap();

    private static Object semaphore = new Object();

    /**
     * @author parkand
     * 
     * Immutable key class
     * 
     */
    private class LockKey implements Serializable {

        private static final int UNDEFINED = -1;

        private Integer subId;

        private Integer contractId;

        private String type;

        // private constructor that ensures no null values are set
        private LockKey(Integer subId, Integer contractId, String type) {
            if (subId == null) {
                subId = new Integer(UNDEFINED);
            }
            this.subId = subId;
            if (contractId == null) {
                contractId = new Integer(UNDEFINED);
            }
            this.contractId = contractId;
            if (type == null) {
                type = new String();
            }
            this.type = type;
        }

        /**
         * @return Returns the contractId.
         */
        public Integer getContractId() {
            return contractId;
        }

        /**
         * @return Returns the subId.
         */
        public Integer getSubId() {
            return subId;
        }

        /**
         * @return Returns the type.
         */
        public String getType() {
            return type;
        }

        public boolean equals(Object o) {
            LockKey obj = (LockKey) o;
            if (obj.getSubId().intValue() == this.getSubId().intValue()
                    && obj.getContractId().intValue() == this.getContractId().intValue()
                    && obj.getType().equals(this.getType())) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * construct a hashcode consisting of the submission # (which should be unique) and 0 or 1
         * depending on the submission case type
         */
        public int hashCode() {
            StringBuffer hashStringBuffer = new StringBuffer(this.getSubId().toString());
            if ("C".equals(this.getType())) {
                hashStringBuffer.append("0");
            } else {
                hashStringBuffer.append("1");
            }
            return Integer.parseInt(hashStringBuffer.toString());
        }
    }

    /**
     * 
     */
    public LockManager() {
        super();
    }

    public static LockManager getInstance(HttpSession session) {
        LockManager lockManager = (LockManager) session.getAttribute(Constants.LOCK_MANAGER);
        if (lockManager == null) {
            lockManager = new LockManager();
            session.setAttribute(Constants.LOCK_MANAGER, lockManager);
        }
        return lockManager;
    }

    /**
     * This method is used to lock the {@link Lockable} resouce for the given user. It returns true,
     * if the lock is taken out successfully, false otherwise.
     * 
     * @param submissionCase This is the object that you attempt to lock.
     * @param userId This is the user ID of the user that is accuiring the lock.
     * @return boolean - True if the resource is successfully locked, false otherwise.
     */
    public boolean lock(Lockable submissionCase, String userId) {
        Lock lock = null;

        // If you try to lock something that hasn't yet been submitted (no submission ID),
        // we disallow the lock.
        if (submissionCase.getSubmissionId() == null) {
            return false;
        } // fi

        try {
            lock = SubmissionServiceDelegate.getInstance().acquireLock(submissionCase, userId);
        } catch (SystemException e) {
            return false;
        }
        if (lock == null) {
            return false;
        }
        submissionCase.setLock(lock);
        LockKey key = new LockKey(lock.getSubmissionId(), lock.getContractId(), lock
                .getSubmissionCaseType());
        synchronized (semaphore) {
            locks.put(key, lock);
        }
        return true;
    }


	public boolean lock(Integer submissionNumber, Integer contractId, String type, String userId) {
		Lock lock = null;
		try {
			lock = SubmissionServiceDelegate.getInstance().acquireLock(submissionNumber, contractId, type, userId);
		} catch (SystemException e) {
			return false;
		}
		if ( lock == null ) {
			return false;
		}
		LockKey key = new LockKey(lock.getSubmissionId(),lock.getContractId(),lock.getSubmissionCaseType());
		synchronized (semaphore) {
			locks.put(key,lock);
		}
		return true;
	}
	

    public boolean release(Lockable submissionCase) {
        LockKey key = new LockKey(submissionCase.getSubmissionId(), submissionCase.getContractId(),
                getType(submissionCase));
        synchronized (semaphore) {
            locks.remove(key);
        }
        try {
            SubmissionServiceDelegate.getInstance().releaseLock(submissionCase);
        } catch (SystemException e) {
            return false;
        }
        return true;
    }

    public boolean refresh(Integer submissionNumber, Integer contractId, String type, String userId) {
        LockKey key = new LockKey(submissionNumber, contractId, type);
        synchronized (semaphore) {
            locks.remove(key);
        }
        Lock lock = null;
        try {
            lock = SubmissionServiceDelegate.getInstance().refreshLock(submissionNumber,
                    contractId, type, userId);
        } catch (SystemException e) {
            return false;
        }
        if (lock == null) {
            return false;
        }
        synchronized (semaphore) {
            locks.put(key, lock);
        }
        return true;
    }

    public boolean refresh(Lockable submissionCase, String userId) {
        LockKey key = new LockKey(submissionCase.getSubmissionId(), submissionCase.getContractId(),
                getType(submissionCase));
        synchronized (semaphore) {
            locks.remove(key);
        }
        Lock lock = null;
        try {
            lock = SubmissionServiceDelegate.getInstance().refreshLock(
                    submissionCase.getSubmissionId(), submissionCase.getContractId(),
                    getType(submissionCase), userId);
        } catch (SystemException e) {
            return false;
        }
        if (lock == null) {
            return false;
        }
        submissionCase.setLock(lock);
        synchronized (semaphore) {
            locks.put(key, lock);
        }
        return true;

    }

    public void releaseAllLocks() {
        // iterate through the Map and release each lock
        synchronized (semaphore) {
            Iterator iter = locks.keySet().iterator();
            while (iter.hasNext()) {
                LockKey key = (LockKey) iter.next();
                Lock lock = (Lock) locks.get(key);
                try {
                    SubmissionServiceDelegate.getInstance().releaseLock(lock);
                } catch (SystemException e) {
                    // couldn't release this lock - continue
                    continue;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueBound(HttpSessionBindingEvent arg0) {
        // LockManager being bound - don't need to do anything
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
     *      release all of the locks
     */
    public void valueUnbound(HttpSessionBindingEvent arg0) {
        releaseAllLocks();
    }

    private String getType(Lockable submissionCase) {
        String type = submissionCase.getType();
        if (type.equals(TRANSFER_CONTRIBUTION)) {
            type = CONTRIBUTION_TYPE;
        }
        return type;
    }



}
