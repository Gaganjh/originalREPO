package com.manulife.pension.platform.web.secureDocumentUpload;

public class SDUShareInformationVO {
	private String 		sharedWithUserId;
	private String 		sharedWithUserName;
	private String 		sharedWithUserRole;
	private String 		sharedWithEmailAddress;
	private boolean  	sendEmail;
	
	
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
	public boolean isSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}
	
	}
