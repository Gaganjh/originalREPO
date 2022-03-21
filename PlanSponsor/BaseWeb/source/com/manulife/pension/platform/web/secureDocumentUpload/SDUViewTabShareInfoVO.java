package com.manulife.pension.platform.web.secureDocumentUpload;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SDUViewTabShareInfoVO {

	private Long shareId;
	private Long submissionId;
	private String sharedWithUserId;
	private String sharedWithUserName;
	private String sharedWithUserRole;
	private String sharedWithEmailAddress;
	private Boolean sendEmail;
	private Timestamp emailSentTs;

	public Long getShareId() {
		return shareId;
	}

	public void setShareId(Long shareId) {
		this.shareId = shareId;
	}

	public Long getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}

	public String getSharedWithUserId() {
		return sharedWithUserId;
	}

	public void setSharedWithUserId(String sharedWithUserId) {
		this.sharedWithUserId = sharedWithUserId;
	}

	public String getSharedWithUserName() {
		return sharedWithUserName;
	}

	public void setSharedWithUserName(String sharedWithUserName) {
		this.sharedWithUserName = sharedWithUserName;
	}

	public String getSharedWithUserRole() {
		return sharedWithUserRole;
	}

	public void setSharedWithUserRole(String sharedWithUserRole) {
		this.sharedWithUserRole = sharedWithUserRole;
	}

	public String getSharedWithEmailAddress() {
		return sharedWithEmailAddress;
	}

	public void setSharedWithEmailAddress(String sharedWithEmailAddress) {
		this.sharedWithEmailAddress = sharedWithEmailAddress;
	}

	public Boolean getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(Boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public Timestamp getEmailSentTs() {
		return emailSentTs;
	}

	public void setEmailSentTs(Timestamp emailSentTs) {
		this.emailSentTs = emailSentTs;
	}
}