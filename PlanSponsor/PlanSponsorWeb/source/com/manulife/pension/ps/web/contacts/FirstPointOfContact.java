package com.manulife.pension.ps.web.contacts;

import java.io.Serializable;
import java.util.EnumSet;

import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.Constants.FirstClientContactFeatureValue;
import com.manulife.pension.ps.web.Constants.FirstClientContactOtherAttributeValue;
import com.manulife.pension.ps.web.Constants.FirstClientContactOtherTypeAttributeValue;

/**
 * Object to hold the FCC data
 * @author ayyalsa
 *
 */
public class FirstPointOfContact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// contract id
	private int contractId;
	
	// first client contact CSF
	private String firstClientContact = "";
	private FirstClientContactFeatureValue firstClientContactValue;
	
	// first client contact client & other attribute
	private String firstClientContactOther = "";
	private FirstClientContactOtherAttributeValue firstClientContactOtherValue;

	// first client contact client & other type attribute
	private String firstClientContactOtherType = "";
	private FirstClientContactOtherTypeAttributeValue firstClientContactOtherTypeValue;
	
	/**
	 * @return the contractId
	 */
	public int getContractId() {
		return contractId;
	}
	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(int contractId) {
		this.contractId = contractId;
	}
	/**
	 * @return the firstClientContact
	 */
	public String getFirstClientContact() {
		return firstClientContact;
	}
	/**
	 * @param firstClientContact the firstClientContact to set
	 */
	public void setFirstClientContact(String firstClientContact) {
		this.firstClientContact = firstClientContact;
	}
	/**
	 * @return the firstClientContactValue
	 */
	public FirstClientContactFeatureValue getFirstClientContactValue() {
		return firstClientContactValue;
	}
	/**
	 * @param firstClientContactValue the firstClientContactValue to set
	 */
	public void setFirstClientContactValue(
			FirstClientContactFeatureValue firstClientContactValue) {
		this.firstClientContactValue = firstClientContactValue;
	}
	/**
	 * @return the firstClientContactOther
	 */
	public String getFirstClientContactOther() {
		return firstClientContactOther;
	}
	/**
	 * @param firstClientContactOther the firstClientContactOther to set
	 */
	public void setFirstClientContactOther(String firstClientContactOther) {
		this.firstClientContactOther = firstClientContactOther;
	}
	/**
	 * @return the firstClientContactOtherValue
	 */
	public FirstClientContactOtherAttributeValue getFirstClientContactOtherValue() {
		return firstClientContactOtherValue;
	}
	/**
	 * @param firstClientContactOtherValue the firstClientContactOtherValue to set
	 */
	public void setFirstClientContactOtherValue(
			FirstClientContactOtherAttributeValue firstClientContactOtherValue) {
		this.firstClientContactOtherValue = firstClientContactOtherValue;
	}
	/**
	 * @return the firstClientContactOtherType
	 */
	public String getFirstClientContactOtherType() {
		return firstClientContactOtherType;
	}
	/**
	 * @param firstClientContactOtherType the firstClientContactOtherType to set
	 */
	public void setFirstClientContactOtherType(String firstClientContactOtherType) {
		this.firstClientContactOtherType = firstClientContactOtherType;
	}
	/**
	 * @return the firstClientContactOtherTypeValue
	 */
	public FirstClientContactOtherTypeAttributeValue getFirstClientContactOtherTypeValue() {
		return firstClientContactOtherTypeValue;
	}
	/**
	 * @param firstClientContactOtherTypeValue the firstClientContactOtherTypeValue to set
	 */
	public void setFirstClientContactOtherTypeValue(
			FirstClientContactOtherTypeAttributeValue firstClientContactOtherTypeValue) {
		this.firstClientContactOtherTypeValue = firstClientContactOtherTypeValue;
	}
	
	public EnumSet<FirstClientContactFeatureValue> getFirstClientContactValues () {
		return EnumSet.allOf(FirstClientContactFeatureValue.class);
	}
	
	/**
	 * This method returns the dropdown values for first point of client contact other attribute values
	 * @return
	 * 		EnumSet
	 */
	public EnumSet<FirstClientContactOtherAttributeValue> getFirstClientContactOtherAttributeValues () {
		return Constants.FIRST_CLIENT_CONTACT_OTHER_ATTRIBUTE_VALUES;
	}

	/**
	 * This method returns the dropdown values for first point of client contact other type attribute values
	 * @return
	 * 		EnumSet
	 */
	public EnumSet<FirstClientContactOtherTypeAttributeValue> getFirstClientContactOtherTypeAttributeValues () {
		 return Constants.FIRST_CLIENT_CONTACT_OTHER_TYPE_ATTRIBUTE_VALUES;
	}
	
}
