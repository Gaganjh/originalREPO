package com.manulife.pension.service.loan.valueobject;

import java.sql.Timestamp;

import com.manulife.pension.service.distribution.valueobject.ActivitySummary;

public class LoanActivitySummary implements ActivitySummary {

    private Timestamp created;
    private Integer createdById;
    private String statusCode;
    private Integer submissionId;
    public Timestamp getCreated() {
        return created;
    }
    public void setCreated(Timestamp created) {
        this.created = created;
    }
    public Integer getCreatedById() {
        return createdById;
    }
    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }
    public String getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
    public Integer getSubmissionId() {
        return submissionId;
    }
    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }
    

}
