package com.manulife.pension.service.distribution.valueobject;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface Fee {

	/**
	 * Fee type code for dollars.
	 */
	String DOLLAR_TYPE_CODE = "D";

	/**
	 * Fee type code for percent.
	 */
	String PERCENT_TYPE_CODE = "P";

	/**
	 * Fee value scale.
	 */
	int FEE_VALUE_SCALE = 2;

	/**
	 * Fee dollar max amount.
	 */
	BigDecimal FEE_DATABASE_FIELD_LIMIT = new BigDecimal("999999999.99");

	String getTypeCode();

	void setTypeCode(final String typeCode);

	BigDecimal getValue();

	void setValue(final BigDecimal value);

	Integer getSubmissionId();

	void setSubmissionId(final Integer submissionId);

	Integer getCreatedById();

	void setCreatedById(final Integer createdById);

	Timestamp getCreated();

	void setCreated(final Timestamp created);

	Integer getLastUpdatedById();

	void setLastUpdatedById(final Integer lastUpdatedById);

	Timestamp getLastUpdated();

	void setLastUpdated(final Timestamp lastUpdated);

	boolean isBlank();

}
