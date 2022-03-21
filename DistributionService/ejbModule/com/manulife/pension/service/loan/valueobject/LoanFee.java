package com.manulife.pension.service.loan.valueobject;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.distribution.valueobject.Fee;

public class LoanFee extends BaseSerializableCloneableObject implements Fee {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Integer createdById;
	private Integer lastUpdatedById;
	private Timestamp created;
	private Timestamp lastUpdated;	
	private String typeCode;
	private Integer submissionId;
	private BigDecimal value;
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
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public Integer getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public boolean isBlank() {
        if ((value == null) && (StringUtils.isBlank(typeCode))) {
            return true;
        } // fi

        return false;
	}


}
