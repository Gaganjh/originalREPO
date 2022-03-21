package com.manulife.pension.platform.web.secureDocumentUpload;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SDUShareDocumentsEmailNotificationVO {

	private Long submissionId;
	private String sharedWithUserId;
	private String sharedWithEmailAddress;
	private Boolean sendEmail;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
	private Timestamp emailSentTs;
	private boolean updateSucceed;
	private String updateStatus;

	public void reset(){
		this.submissionId = 0L;
		this.sharedWithUserId = null;
		this.sharedWithEmailAddress = null;
		this.sendEmail = false;
		this.emailSentTs = null;
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
	public boolean isUpdateSucceed() {
		return updateSucceed;
	}
	public void setUpdateSucceed(boolean updateSucceed) {
		this.updateSucceed = updateSucceed;
	}
	public String getUpdateStatus() {
		return updateStatus;
	}
	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}
	
	
}