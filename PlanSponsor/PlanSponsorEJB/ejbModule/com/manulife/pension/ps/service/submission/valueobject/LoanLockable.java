package com.manulife.pension.ps.service.submission.valueobject;

import java.io.Serializable;

import com.manulife.pension.service.loan.domain.LoanConstants;

public class LoanLockable implements Lockable, Serializable {

	private static final long serialVersionUID = 1L;

	private Integer submissionId;
	private Integer contractId;
	private Integer profileId;
	private Lock lock;

	public LoanLockable(Integer submissionId, Integer contractId,
			Integer profileId) {
		super();
		this.submissionId = submissionId;
		this.contractId = contractId;
		this.profileId = profileId;
	}

	public Integer getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	public Lock getLock() {
		return lock;
	}

	public void setLock(Lock lock) {
		this.lock = lock;
	}

	public String getType() {
		return LoanConstants.SUBMISSION_CASE_TYPE_CODE;
	}
}
