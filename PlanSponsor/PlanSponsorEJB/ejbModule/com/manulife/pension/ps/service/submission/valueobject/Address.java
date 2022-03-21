package com.manulife.pension.ps.service.submission.valueobject;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import com.manulife.pension.common.BaseSerializableCloneableObject;

/**
 * @author parkand
 */
public class Address extends BaseSerializableCloneableObject {

	private static final long serialVersionUID = 1L;

	// initialize everything to empty Strings so that 'null' doesn't show up in the JSP
	protected String addressLine1 = "";
	protected String addressLine2 = "";
	protected String city = "";
	protected String stateCode = "";
	protected String zipCode = "";
	protected String country = "";
	protected String employerProvidedEmailAddress = "";
	
	// other fields that are shared 
	protected String empId = "";
	protected String ssn = "";
	protected Date lastUpdatedTS;
	protected BigDecimal participantId;
	protected Integer contractNumber;
	protected String sortOptionCode;

	protected String originalPrtIdNum = ""; // required to update Apollo addresses
	
	// collection of address-related errors
	private Collection errors;
	
	/**
	 * 
	 */
	public Address() {
		super();
	}
	/**
	 * @return Returns the addressLine1.
	 */
	public String getAddressLine1() {
		return addressLine1;
	}
	/**
	 * @param address1 The addressLine1 to set.
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	/**
	 * @return Returns the addressLine2.
	 */
	public String getAddressLine2() {
		return addressLine2;
	}
	/**
	 * @param address2 The addressLine2 to set.
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	/**
	 * @return Returns the city.
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city The city to set.
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return Returns the country.
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country The country to set.
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return Returns the stateCode.
	 */
	public String getStateCode() {
		return stateCode;
	}
	/**
	 * @param stateCode The stateCode to set.
	 */
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	/**
	 * @return Returns the zipCode.
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * @param zipCode The zipCode to set.
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	/**
	 * @return Returns the empId.
	 */
	public String getEmpId() {
		return empId;
	}
	/**
	 * @param empId The empId to set.
	 */
	public void setEmpId(String employerDesignatedId) {
		this.empId = employerDesignatedId;
	}

	/**
	 * @return Returns the contractNumber.
	 */
	public Integer getContractNumber() {
		return contractNumber;
	}
	/**
	 * @param contractNumber The contractNumber to set.
	 */
	public void setContractNumber(Integer cnno) {
		this.contractNumber = cnno;
	}
	/**
	 * @return Returns the lastUpdatedTS.
	 */
	public Date getLastUpdatedTS() {
		return lastUpdatedTS;
	}
	/**
	 * @param lastUpdatedTS The lastUpdatedTS to set.
	 */
	public void setLastUpdatedTS(Date lastUpdatedTS) {
		this.lastUpdatedTS = lastUpdatedTS;
	}
	/**
	 * @return Returns the participantId.
	 */
	public BigDecimal getParticipantId() {
		return participantId;
	}
	/**
	 * @param participantId The participantId to set.
	 */
	public void setParticipantId(BigDecimal prtId) {
		this.participantId = prtId;
	}
	
	/**
	 * @return Returns the ssn.
	 */
	public String getSsn() {
		return ssn;
	}
	/**
	 * @param ssn The ssn to set.
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	/**
	 * @return Returns the sortOptionCode.
	 */
	public String getSortOptionCode() {
		return sortOptionCode;
	}
	/**
	 * @param sortOptionCode The sortOptionCode to set.
	 */
	public void setSortOptionCode(String sortOptionCode) {
		this.sortOptionCode = sortOptionCode;
	}
	
	/* this method is to be overwritten by subclasses */
	public String getFullName() {return "";}
	
	
	/**
	 * @return Returns the errors.
	 */
	public Collection getErrors() {
		return errors;
	}
	/**
	 * @param errors The errors to set.
	 */
	public void setErrors(Collection errors) {
		this.errors = errors;
	}
	/**
	 * @return Returns the originalPrtIdNum.
	 */
	public String getOriginalPrtIdNum() {
		return originalPrtIdNum;
	}
	/**
	 * @param originalPrtIdNum The originalPrtIdNum to set.
	 */
	public void setOriginalPrtIdNum(String originalPrtIdNum) {
		this.originalPrtIdNum = originalPrtIdNum;
	}
	/**
	 * @return Returns the employer provided email address.
	 */
	public String getEmployerProvidedEmailAddress() {
		return employerProvidedEmailAddress;
	}
	/**
	 * @param employerProvidedEmailAddress The employerProvidedEmailAddress to set.
	 */
	public void setEmployerProvidedEmailAddress(String employerProvidedEmailAddress) {
		this.employerProvidedEmailAddress = employerProvidedEmailAddress;
	}
}
