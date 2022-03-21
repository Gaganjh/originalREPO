package com.manulife.pension.ps.service.submission.valueobject;

import java.io.Serializable;
import java.util.Date;

/**
 * @author parkand
 */
public class Lock implements Serializable {

	/**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private static final long LOCK_TIMEOUT = 2100000; // 35 minutes
	
	private String userId;
	private String userName;
	private Date lockTS;
	private Integer submissionId;
	private Integer contractId;
	private String submissionCaseType;
	
	public Lock() {
		super();
	}

	public Lock(String userId, Date lockTimestamp, Integer submissionId, Integer contractId) {
		this.userId = userId;
		this.lockTS = lockTimestamp;
		this.submissionId = submissionId; 
		this.contractId = contractId;
	}

	/**
	 * @return Returns the contractId.
	 */
	public Integer getContractId() {
		return contractId;
	}

	/**
	 * @param contractId The contractId to set.
	 */
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	/**
	 * @return Returns the lockTS.
	 */
	public Date getLockTS() {
		return lockTS;
	}

	/**
	 * @param lockTS The lockTS to set.
	 */
	public void setLockTS(Date lockTS) {
		this.lockTS = lockTS;
	}

	/**
	 * @return Returns the submissionId.
	 */
	public Integer getSubmissionId() {
		return submissionId;
	}

	/**
	 * @param submissionId The submissionId to set.
	 */
	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}

	/**
	 * @return Returns the submissionCaseType.
	 */
	public String getSubmissionCaseType() {
		return submissionCaseType;
	}

	/**
	 * @param submissionCaseType The submissionCaseType to set.
	 */
	public void setSubmissionCaseType(String submissionCaseType) {
		this.submissionCaseType = submissionCaseType;
	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public boolean isActive() {
		return (userId != null && lockTS != null && 
			(System.currentTimeMillis()-lockTS.getTime()) < LOCK_TIMEOUT);
	}
	
	public boolean isAvailable(String userId) {
		
		if (userId == null) { // input userid is null - cannot determine if the lock is available for this user
			return false;
		} else if (!isActive()) {// the lock is not currently set - it's available
			return true;
		} else if (userId.equals(this.userId.trim())) { // the lock is set, but it belongs to this user, return true
			return true;
		} else { // lock is set and does not belong to this user, return false
			return false;
		}
	}
	
	public String toString() {
		return "lock: " + this.userId + "," + this.userName + "," + this.contractId + "," + this.submissionId + "," + 
			this.submissionCaseType + "," + this.lockTS;
	}
}
