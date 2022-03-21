/**
 * 
 */
package com.manulife.pension.service.loan.valueobject;

import com.manulife.pension.service.loan.domain.LoanConstants;


/**
 * Holds the data needed for processing Loan related Events
 */
public class LoanEventData {

	public LoanEventData() {
	}
	
	/**
	 * submission Id of the specific loan request
	 */
	private Integer submissionId;
	/**
	 * initiator of loan request
	 */
	private Integer initiatorUserProfileId;
	/**
	 * reviewer of the loan request
	 */
	private Integer reviewerUserProfileId;
	/**
	 * approver of the loan request
	 */
	private Integer approverUserProfileId;
	
	/**
	 * created by role code
	 * @return
	 */
	private String createdByRoleCode;
	
	public Integer getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}
	public Integer getInitiatorUserProfileId() {
		return initiatorUserProfileId;
	}
	public void setInitiatorUserProfileId(Integer initiatorUserProfileId) {
		this.initiatorUserProfileId = initiatorUserProfileId;
	}
	public Integer getReviewerUserProfileId() {
		return reviewerUserProfileId;
	}
	public void setReviewerUserProfileId(Integer reviewerUserProfileId) {
		this.reviewerUserProfileId = reviewerUserProfileId;
	}
	public Integer getApproverUserProfileId() {
		return approverUserProfileId;
	}
	public void setApproverUserProfileId(Integer approverUserProfileId) {
		this.approverUserProfileId = approverUserProfileId;
	}

	public String toString(){
		StringBuffer str = new StringBuffer();
		str.append("Loan Event Data for Submission Id : ");
		str.append(getSubmissionId());
		if (getInitiatorUserProfileId() != null) {
			str.append(" Initiator Profile Id : ");
			str.append(getInitiatorUserProfileId());
		}
		if (getReviewerUserProfileId() != null) {
			str.append("Reviewer Profile Id : ");
			str.append(getReviewerUserProfileId());
		}
		if (getApproverUserProfileId() != null) {
			str.append("Approver Profile Id :  ");
			str.append(getApproverUserProfileId());
		}
		return str.toString();
	}

	public String getCreatedByRoleCode() {
		return createdByRoleCode;
	}
	
	public void setCreatedByRoleCode(String createdByRoleCode) {
		this.createdByRoleCode = createdByRoleCode;
	}
	
	public boolean isParticipantInitiatedIndicator() {
		return LoanConstants.USER_ROLE_PARTICIPANT_CODE.equals(createdByRoleCode);
	}
	public boolean isExternalUserInitiatedIndicator() {
		return !LoanConstants.USER_ROLE_PARTICIPANT_CODE.equals(createdByRoleCode);
	}
	public boolean isNotParticipantInitiatedIndicator() {
		return !LoanConstants.USER_ROLE_PARTICIPANT_CODE.equals(createdByRoleCode);
	}
}
