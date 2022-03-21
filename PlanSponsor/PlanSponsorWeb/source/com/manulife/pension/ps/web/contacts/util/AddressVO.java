package com.manulife.pension.ps.web.contacts.util;

import java.io.Serializable;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Address is the data transfer object (DTO, or value object) for address data.
 * 
 */
public class AddressVO implements Serializable, Cloneable {
	
	public static final int ZIP_FIRST_LENGTH = 5;

	public static final int ZIP_SECOND_LENGTH = 4;
	
	public static final int ZIP_CODE_LENGTH = ZIP_FIRST_LENGTH + ZIP_SECOND_LENGTH;

    /**
     * Constant that defines the country code for USA.
     */
    public static final String USA_COUNTRY_CODE = "USA";

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String stateCode;

    private String zipCode1;

    private String zipCode2;

    // Default to USA Country Code
    private String countryCode = USA_COUNTRY_CODE;
    
    private String addressTypeCode;


    /**
     * @return the addressLine1
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * @TODO Should be replaced by an AddressUi object.
     * @return the escaped addressLine1
     */
    public String getEscapedAddressLine1() {
        return StringEscapeUtils.escapeJavaScript(addressLine1);
    }

    /**
     * @param addressLine1 the addressLine1 to set
     */
    public void setAddressLine1(final String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    /**
     * @return the addressLine2
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * @TODO Should be replaced by an AddressUi object.
     * @return the escaped addressLine2
     */
    public String getEscapedAddressLine2() {
        return StringEscapeUtils.escapeJavaScript(addressLine2);
    }

    /**
     * @param addressLine2 the addressLine2 to set
     */
    public void setAddressLine2(final String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @TODO Should be replaced by an AddressUi object.
     * @return the escaped city
     */
    public String getEscapedCity() {
        return StringEscapeUtils.escapeJavaScript(city);
    }

    /**
     * @param city the city to set
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode the countryCode to set
     */
    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return the stateCode
     */
    public String getStateCode() {
        return stateCode;
    }

    /**
     * @param stateCode the stateCode to set
     */
    public void setStateCode(final String stateCode) {
        this.stateCode = stateCode;
    }

    /**
     * @return the zipCode
     */
    public String getZipCode() {
        return new StringBuffer(StringUtils.defaultString(zipCode1)).append(
                StringUtils.defaultString(zipCode2)).toString();
    }

    /**
     * @param zipCode the zipCode to set
     */
    public void setZipCode(final String zipCode) {

        final String nonNullZip = StringUtils.defaultString(zipCode);
        this.zipCode1 = StringUtils.substring(nonNullZip, 0, ZIP_FIRST_LENGTH);
        this.zipCode2 = StringUtils.substring(nonNullZip, ZIP_FIRST_LENGTH, ZIP_CODE_LENGTH);
    }

    /**
     * @return the zipCode1
     */
    public String getZipCode1() {
        return zipCode1;
    }

    /**
     * @param zipCode1 the zipCode1 to set
     */
    public void setZipCode1(final String zipCode1) {
        this.zipCode1 = zipCode1;
    }

    /**
     * @return the zipCode2
     */
    public String getZipCode2() {
        return zipCode2;
    }

    /**
     * @param zipCode2 the zipCode2 to set
     */
    public void setZipCode2(final String zipCode2) {
        this.zipCode2 = zipCode2;
    }
    
    public String getAddressTypeCode() {
		return addressTypeCode;
	}

	public void setAddressTypeCode(String addressTypeCode) {
		this.addressTypeCode = addressTypeCode;
	}

	/**
     * Checks if the default object has been inititalized or not.
     * 
     * @return boolean - True if the object is still blank.
     */
    public boolean isBlank() {
        boolean result = true;

        result &= StringUtils.isBlank(getAddressLine1());
        result &= StringUtils.isBlank(getAddressLine2());
        result &= StringUtils.isBlank(getCity());
        result &= StringUtils.isBlank(getStateCode());
        result &= StringUtils.isBlank(getZipCode1());
        
        return result;
    }

    /**
     * Copy legal to other address
     * @param legalAddress
     */
    public void copyAddressFrom(AddressVO address) {
    	addressLine1 = address.getAddressLine1();
    	addressLine2 = address.getAddressLine2();
    	city = address.getCity();
    	stateCode = address.getStateCode();
    	zipCode1 = address.getZipCode1();
    	zipCode2 = address.getZipCode2();
    }
           
}
