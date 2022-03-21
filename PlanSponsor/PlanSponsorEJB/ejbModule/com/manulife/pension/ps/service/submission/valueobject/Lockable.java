package com.manulife.pension.ps.service.submission.valueobject;

import java.io.Serializable;

/**
 * @author parkand
 */
public interface Lockable extends Serializable {
	public Lock getLock();
	public void setLock(Lock lock);
	public Integer getSubmissionId();
	public Integer getContractId();
	public String getType();
}
