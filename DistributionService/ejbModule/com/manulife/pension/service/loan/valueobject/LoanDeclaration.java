package com.manulife.pension.service.loan.valueobject;

import java.sql.Timestamp;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.distribution.valueobject.Declaration;

public class LoanDeclaration extends BaseSerializableCloneableObject implements Declaration {


	private static final long serialVersionUID = 1L;
	private String typeCode;
	private Integer createdById;
	private Integer lastUpdatedById;
	private Timestamp created;
	private Timestamp lastUpdated;
	private Integer submissionId;
	
	public Integer getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public Integer getCreatedById() {
		return createdById;
	}
	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}
	public Integer getLastUpdatedById() {
		return lastUpdatedById;
	}
	public void setLastUpdatedById(Integer lastUpdatedById) {
		this.lastUpdatedById = lastUpdatedById;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public Timestamp getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}		
}
