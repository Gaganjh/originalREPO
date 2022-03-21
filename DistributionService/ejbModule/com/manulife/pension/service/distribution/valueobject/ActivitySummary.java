package com.manulife.pension.service.distribution.valueobject;

import java.sql.Timestamp;

public interface ActivitySummary {

	String SENT_FOR_REVIEW = ActivitySummaryStatusCode.SENT_FOR_REVIEW.getStatusCode();
	
    String SENT_FOR_APPROVAL = ActivitySummaryStatusCode.SENT_FOR_APPROVAL.getStatusCode();

    String APPROVED = ActivitySummaryStatusCode.APPROVED.getStatusCode();

    String DENIED = ActivitySummaryStatusCode.DENIED.getStatusCode();

    String DELETED = ActivitySummaryStatusCode.DELETED.getStatusCode();

    String EXPIRED = ActivitySummaryStatusCode.EXPIRED.getStatusCode();

    String SENT_FOR_ACCEPTANCE = ActivitySummaryStatusCode.SENT_FOR_ACCEPTANCE.getStatusCode();

    String LOAN_PACKAGE_REQUESTED = ActivitySummaryStatusCode.LOAN_PACKAGE_REQUESTED
            .getStatusCode();  

	Integer getSubmissionId();
	
	void setSubmissionId(Integer submissionId);
	
	String getStatusCode();
	
	void setStatusCode(String statusCode);
	
	Timestamp getCreated();

	void setCreated(final Timestamp created);

	/**
	 * @return the created by id
	 */
	Integer getCreatedById();

	/**
	 * @param createdById
	 *            the created by id
	 */
	void setCreatedById(final Integer createdById);

}