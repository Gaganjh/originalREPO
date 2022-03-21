package com.manulife.pension.service.loan.valueobject;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;

public class LoanAddress extends BaseSerializableCloneableObject implements DistributionAddress{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer submissionId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateCode;
    private String zipCode1;
    private String zipCode2;
    // Default to USA Country Code
    private String countryCode = "USA";
    private Integer recipientNo;
    private Integer payeeNo;
    private String distributionTypeCode;
    private String nonMatchedCountryName;
	private Integer createdById;
	private Integer lastUpdatedById;
	private Timestamp created;
	private Timestamp lastUpdated;	
    

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

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getZipCode1() {
		return zipCode1;
	}

	public void setZipCode1(String zipCode1) {
		this.zipCode1 = zipCode1;
	}

	public String getZipCode2() {
		return zipCode2;
	}

	public void setZipCode2(String zipCode2) {
		this.zipCode2 = zipCode2;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Integer getRecipientNo() {
		return recipientNo;
	}

	public void setRecipientNo(Integer recipientNo) {
		this.recipientNo = recipientNo;
	}

	public Integer getPayeeNo() {
		return payeeNo;
	}

	public void setPayeeNo(Integer payeeNo) {
		this.payeeNo = payeeNo;
	}

	public String getDistributionTypeCode() {
		return distributionTypeCode;
	}

	public void setDistributionTypeCode(String distributionTypeCode) {
		this.distributionTypeCode = distributionTypeCode;
	}

	public String getNonMatchedCountryName() {
		return nonMatchedCountryName;
	}

	public void setNonMatchedCountryName(String nonMatchedCountryName) {
		this.nonMatchedCountryName = nonMatchedCountryName;
	}



    public String getZipCode() {
        return new StringBuffer(StringUtils.defaultString(zipCode1)).append(
                StringUtils.defaultString(zipCode2)).toString();
    }

	public boolean isBlank() {
		return false;
	}


	public void setZipCode(String zipCode) {
        final String nonNullZip = StringUtils.defaultString(zipCode);
        this.zipCode1 = StringUtils.substring(nonNullZip, 0, ZIP_FIRST_LENGTH);
        this.zipCode2 = StringUtils.substring(nonNullZip, ZIP_FIRST_LENGTH, ZIP_CODE_LENGTH);
		
	}

	public void setZipCode(Object object) {
		// TODO Auto-generated method stub
		
	}

	public Integer getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}


}
