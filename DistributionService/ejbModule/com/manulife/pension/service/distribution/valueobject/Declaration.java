package com.manulife.pension.service.distribution.valueobject;

import java.sql.Timestamp;

public interface Declaration {

	/**
	 * Declaration type code for tax notice being read.
	 */
	String TAX_NOTICE_TYPE_CODE = "TR";

	/**
	 * Declaration type code for waiting period waived.
	 */
	String WAITING_PERIOD_WAIVED_TYPE_CODE = "WW";

	/**
	 * Declaration type code for WMSI/PENCHECKS - IRA Service Provider.
	 */
	String IRA_SERVICE_PROVIDER_TYPE_CODE = "WP";

    /**
     * Declaration type code for Pin Exposure.
     */
    String AT_RISK_TRANSACTION_TYPE_CODE = "PX";

    /**
	 * Loan's promissory note and irrevocable pledge
	 */
	String PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE = "LP";

	/**
	 * Truth in lending notice
	 */
	String TRUTH_IN_LENDING_NOTICE = "TL";

	/**
	 * Loan participant confirm and apply text
	 */
	String LOAN_PARTICIPANT_AUTHORIZATION = "LA";
	
	/**
	 * Loan approver confirm and apply text
	 */
	String LOAN_APPROVER_CONFIRMATION = "LC";

	Integer getCreatedById();

	void setCreatedById(final Integer createdById);

	Timestamp getCreated();

	void setCreated(final Timestamp created);

	Integer getLastUpdatedById();

	void setLastUpdatedById(final Integer lastUpdatedById);

	Timestamp getLastUpdated();

	void setLastUpdated(final Timestamp lastUpdated);

	String getTypeCode();

	void setTypeCode(String typeCode);

	void setSubmissionId(Integer submissionId);

	Integer getSubmissionId();
}
