package com.manulife.pension.platform.web.secureDocumentUpload;

import java.sql.Timestamp;
import java.util.List;

public class SDUViewTabVO {
	
	
	
	private Long 		submissionId;
	private	Timestamp 	submissionTs;
	private	String 		submissionDesc;
	private	String 		clientId;
	private	String 		clientUser;
	private	String 		clientUserName;
	private	String 		clientUserRole;
	private	Integer 	clientContract;
	private	String 		storageLocationId;
	private	Timestamp 	shareExpiryTs;
	private Long 		fileId;
	private	String 		fileName;
	private	Integer 	fileSizeBytes;
	private List<SDUViewTabShareInfoVO> activeShareSubmissionsModel;
	
	public Long getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}
	public Timestamp getSubmissionTs() {
		return submissionTs;
	}
	public void setSubmissionTs(Timestamp submissionTs) {
		this.submissionTs = submissionTs;
	}
	public String getSubmissionDesc() {
		return submissionDesc;
	}
	public void setSubmissionDesc(String submissionDesc) {
		this.submissionDesc = submissionDesc;
	}
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientUser() {
		return clientUser;
	}
	public void setClientUser(String clientUser) {
		this.clientUser = clientUser;
	}
	public String getClientUserName() {
		return clientUserName;
	}
	public void setClientUserName(String clientUserName) {
		this.clientUserName = clientUserName;
	}
	public String getClientUserRole() {
		return clientUserRole;
	}
	public void setClientUserRole(String clientUserRole) {
		this.clientUserRole = clientUserRole;
	}
	public Integer getClientContract() {
		return clientContract;
	}
	public void setClientContract(Integer clientContract) {
		this.clientContract = clientContract;
	}
	public String getStorageLocationId() {
		return storageLocationId;
	}
	public void setStorageLocationId(String storageLocationId) {
		this.storageLocationId = storageLocationId;
	}
	public Timestamp getShareExpiryTs() {
		return shareExpiryTs;
	}
	public void setShareExpiryTs(Timestamp shareExpiryTs) {
		this.shareExpiryTs = shareExpiryTs;
	}
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getFileSizeBytes() {
		return fileSizeBytes;
	}
	public void setFileSizeBytes(Integer fileSizeBytes) {
		this.fileSizeBytes = fileSizeBytes;
	}
	
	public List<SDUViewTabShareInfoVO> getActiveShareSubmissionsModel() {
		return activeShareSubmissionsModel;
	}
	public void setActiveShareSubmissionsModel(List<SDUViewTabShareInfoVO> activeShareSubmissionsModel) {
		this.activeShareSubmissionsModel = activeShareSubmissionsModel;
	}

	
}
