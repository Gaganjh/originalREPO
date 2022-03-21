package com.manulife.pension.ps.service.submission.valueobject;

/**
 * @author adamthj
 *
 * used by SubmissionDAO to pass data from one method to 
 * another which can be awkward with static methods.
  */
public class ContributionInfo {
	int submissionId;
	String moneySourceId;
	
	public ContributionInfo(int submissionId, String moneySourceId) {
		this.submissionId = submissionId;
		this.moneySourceId = moneySourceId;
	}
	
	public int getSubmissionId() {
		return this.submissionId;
	}
	
	public String getMoneySourceId() {
		return this.moneySourceId;
	}
}
