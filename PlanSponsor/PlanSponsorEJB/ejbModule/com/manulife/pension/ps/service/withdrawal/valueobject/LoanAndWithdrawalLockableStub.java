/**
 * Created on January 10, 2007
 */
package com.manulife.pension.ps.service.withdrawal.valueobject;

import java.io.Serializable;

import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.service.submission.valueobject.Lockable;

/**
 * A lightweigth Lockable object to be used by the LockManager, instead
 * of a LoanAndWithdrawalItem object which is heavier. 
 * Basically just a placeholder for submissionId, contractId and 
 * the Lock itself.
 *
 * @author Mihai Popa
 */
public class LoanAndWithdrawalLockableStub implements Serializable, Lockable {

	private static final long serialVersionUID = 1L;
	public static final String WITHDRAWAL_SUBMISSION_TYPE = "W";

	private Integer submissionId;
	private Integer contractId;
	private Integer profileId;
	private Lock lock;
	
	public LoanAndWithdrawalLockableStub(Integer submissionId, Integer contractId, Integer profileId) {
		this.submissionId = submissionId;
		this.contractId = contractId;
		this.profileId = profileId;
	}

	public Integer getProfileId() {
		return profileId;
	}
	
	//implement Lockable 
	public Lock getLock() {
		return lock;
	}
	public void setLock(Lock lock) {
		this.lock = lock;
	}
	public Integer getSubmissionId() {
		return submissionId;
	}
	public Integer getContractId() {
		return contractId;
	}
	public String getType() {
		return WITHDRAWAL_SUBMISSION_TYPE; 
	}

}