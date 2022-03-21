package com.manulife.pension.platform.web.secureDocumentUpload;

import java.io.Serializable;
import java.sql.Timestamp;


public class SDUSubmissionMetaDataVO  implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Timestamp submissionTs;
	private String submissionStatus;
	private Timestamp submissionStatusTs;
	private String submissionDesc;
	private String submissionStatusDesc;
	private String clientId;
	private String clientUser;
	private String clientUserName;
	private String clientUserRole;
	private String clientContract;
	private String clientContractName;
	private String clientContractStatus;
	private String clientData;
	private Long submissionCount;
	private String storageLocationId;
	private String targetSystem;
	private Boolean shareable;
	private Timestamp shareExpiryTs;
	private Boolean archived;
	private String shareInfo;
	
	public Timestamp getSubmissionTs() {
		return submissionTs;
	}
	public void setSubmissionTs(Timestamp submissionTs) {
		this.submissionTs = submissionTs;
	}
	public String getSubmissionStatus() {
		return submissionStatus;
	}
	public void setSubmissionStatus(String submissionStatus) {
		this.submissionStatus = submissionStatus;
	}
	public Timestamp getSubmissionStatusTs() {
		return submissionStatusTs;
	}
	public void setSubmissionStatusTs(Timestamp submissionStatusTs) {
		this.submissionStatusTs = submissionStatusTs;
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
	public String getClientContract() {
		return clientContract;
	}
	public void setClientContract(String clientContract) {
		this.clientContract = clientContract;
	}
	public String getClientContractName() {
		return clientContractName;
	}
	public void setClientContractName(String clientContractName) {
		this.clientContractName = clientContractName;
	}
	public String getClientContractStatus() {
		return clientContractStatus;
	}
	public void setClientContractStatus(String clientContractStatus) {
		this.clientContractStatus = clientContractStatus;
	}
	public String getClientData() {
		return clientData;
	}
	public void setClientData(String clientData) {
		this.clientData = clientData;
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
	public String getTargetSystem() {
		return targetSystem;
	}
	public void setTargetSystem(String targetSystem) {
		this.targetSystem = targetSystem;
	}
	public Boolean getShareable() {
		return shareable;
	}
	public void setShareable(Boolean shareable) {
		this.shareable = shareable;
	}
	public Timestamp getShareExpiryTs() {
		return shareExpiryTs;
	}
	public void setShareExpiryTs(Timestamp shareExpiryTs) {
		this.shareExpiryTs = shareExpiryTs;
	}
	public Boolean getArchived() {
		return archived;
	}
	public void setArchived(Boolean archived) {
		this.archived = archived;
	}
	public String getShareInfo() {
		return shareInfo;
	}
	public void setShareInfo(String shareInfo) {
		this.shareInfo = shareInfo;
	}
}
