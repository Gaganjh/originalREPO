package com.manulife.pension.service.distribution.valueobject;

import java.sql.Timestamp;

public interface DistributionAddress {

	String PAYEE_TYPE_CODE = "PA";

	String RECIPIENT_TYPE_CODE = "RT";

	String LOAN_TYPE_CODE = "LT";

	int ZIP_FIRST_LENGTH = 5;

	int ZIP_SECOND_LENGTH = 4;

	int ZIP_CODE_LENGTH = ZIP_FIRST_LENGTH + ZIP_SECOND_LENGTH;

	Integer getCreatedById();

	void setCreatedById(final Integer createdById);

	Timestamp getCreated();

	void setCreated(final Timestamp created);

	Integer getLastUpdatedById();

	void setLastUpdatedById(final Integer lastUpdatedById);

	Timestamp getLastUpdated();

	void setLastUpdated(final Timestamp lastUpdated);

	boolean isBlank();

	String getAddressLine1();

	void setAddressLine1(String addressLine1);

	String getAddressLine2();

	void setAddressLine2(String addressLine2);

	String getCity();

	void setCity(String city);

	String getStateCode();

	void setStateCode(String stateCode);

	String getZipCode1();

	void setZipCode1(String zipCode1);

	String getZipCode2();

	void setZipCode2(String zipCode2);

	String getCountryCode();

	void setCountryCode(String countryCode);

	Integer getRecipientNo();

	void setRecipientNo(Integer recipientNo);

	Integer getPayeeNo();

	void setPayeeNo(Integer payeeNo);

	String getDistributionTypeCode();

	void setDistributionTypeCode(String distributionTypeCode);

	String getNonMatchedCountryName();

	void setNonMatchedCountryName(String nonMatchedCountryName);

	String getZipCode();

	void setZipCode(String zipCode);

	void setSubmissionId(Integer submissionId);

	Integer getSubmissionId();
}
