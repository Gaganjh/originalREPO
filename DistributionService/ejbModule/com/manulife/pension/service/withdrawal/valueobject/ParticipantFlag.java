package com.manulife.pension.service.withdrawal.valueobject;

import java.sql.Timestamp;

import com.manulife.pension.common.BaseSerializableCloneableObject;

public class ParticipantFlag extends BaseSerializableCloneableObject {
    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    
	private String profileId;
	private String exceptionFlag;
	private String silRefNum;
	private String lastUpdatedBy;
	private Timestamp lastUpdateDate;
	private String contractId;
	private String exceptionCategoryCode;
	private String createdBy;
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	public String getExceptionFlag() {
		return exceptionFlag;
	}
	public void setExceptionFlag(String exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
	}
	public String getSilRefNum() {
		return silRefNum;
	}
	public void setSilRefNum(String silRefNum) {
		this.silRefNum = silRefNum;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Timestamp getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Timestamp lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getExceptionCategoryCode() {
		return exceptionCategoryCode;
	}
	public void setExceptionCategoryCode(String exceptionCategoryCode) {
		this.exceptionCategoryCode = exceptionCategoryCode;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
}
