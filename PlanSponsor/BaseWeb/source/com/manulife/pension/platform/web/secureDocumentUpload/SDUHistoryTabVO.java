package com.manulife.pension.platform.web.secureDocumentUpload;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class SDUHistoryTabVO {
	
	private Long 		submissionId;
	private	String 		submissionStatus;	
	private	Integer 	clientContract;
	private	String 		clientData;
	private	Timestamp 	submissionTs;
	private	Timestamp 	submissionStatusTs;
	private	String 		clientId;
	private	String 		clientUser;
	private	String 		clientUserName;
	private	String 		clientUserRole;
	private	String 		submissionDesc;
	private	String 		submissionStatusDesc;
	private	Long 		submissionCount;	
	private	String 		storageLocationId;	
	private	String 		fileName;
	private	Integer 	fileSizeBytes;
	
    private static final Map<String,String> customStatusFieldMap = new HashMap<String,String>();
    static {
        customStatusFieldMap.put("completed", "Received");
        customStatusFieldMap.put("staged", "In Progress");
        customStatusFieldMap.put("failed", "Failed");
    }
	
	public Long getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}
	public String getSubmissionStatus() {
		return submissionStatus;
	}
	public void setSubmissionStatus(String submissionStatus) {		
		this.submissionStatus = customStatusFieldMap.get(submissionStatus);
	}
	public Integer getClientContract() {
		return clientContract;
	}
	public void setClientContract(Integer clientContract) {
		this.clientContract = clientContract;
	}
	public String getClientData() {
		return clientData;
	}
	public void setClientData(String clientData) {
		this.clientData = clientData;
	}
	public Timestamp getSubmissionTs() {
		return submissionTs;
	}
	public void setSubmissionTs(Timestamp submissionTs) {
		this.submissionTs = submissionTs;
	}
	public Timestamp getSubmissionStatusTs() {
		return submissionStatusTs;
	}
	public void setSubmissionStatusTs(Timestamp submissionStatusTs) {
		this.submissionStatusTs = submissionStatusTs;
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
	public String getSubmissionDesc() {
		return submissionDesc;
	}
	public void setSubmissionDesc(String submissionDesc) {
		this.submissionDesc = submissionDesc;
	}
	public String getSubmissionStatusDesc() {
		return submissionStatusDesc;
	}
	public void setSubmissionStatusDesc(String submissionStatusDesc) {
		this.submissionStatusDesc = submissionStatusDesc;
	}
	public Long getSubmissionCount() {
		return submissionCount;
	}
	public void setSubmissionCount(Long submissionCount) {
		this.submissionCount = submissionCount;
	}
	public String getStorageLocationId() {
		return storageLocationId;
	}
	public void setStorageLocationId(String storageLocationId) {
		this.storageLocationId = storageLocationId;
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
}
