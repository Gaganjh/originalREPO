package com.manulife.pension.bd.web.registration.util;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This is a value object class for PhoneNumber.
 * 
 * @author Ilamparithi
 * 
 */
public class PhoneNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    private String areaCode;

    private String number1;

    private String number2;

    public PhoneNumber() {
    	areaCode = "";
    	number1 = "";
    	number2 = "";
    }
    /**
     * Returns a concatenated phone number value
     * 
     * @return String
     */
    public String getValue() {
        return StringUtils.trimToEmpty(areaCode) + StringUtils.trimToEmpty(number1)
                + StringUtils.trimToEmpty(number2);
    }

    /**
     * Splits the phone number value
     * 
     * @param value
     */
    public void setValue(String value) {
        areaCode = StringUtils.substring(value, 0, 3);
        number1 = StringUtils.substring(value, 3, 6);
        number2 = StringUtils.substring(value, 6, 10);
    }

    /**
     * set the phone number value from another PhoneNumber object
     * @param another
     */
    public void set(PhoneNumber another) {
    	if (another != null) {
    		areaCode = another.getAreaCode();
    		number1 = another.getNumber1();
    		number2 = another.getNumber2();
    	} else {
    		areaCode = "";
    		number1 = "";
    		number2 = "";
    	}
    }
    /**
     * Returns the areaCode part of phone number
     * 
     * @return String
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * Sets the areaCode part of phone number
     * 
     * @param areaCode
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = StringUtils.trimToEmpty(areaCode);
    }

    /**
     * Returns the number1 part of phone number
     * 
     * @return String
     */
    public String getNumber1() {
        return number1;
    }

    /**
     * Sets the number1 part of phone number
     * 
     * @param number1
     */
    public void setNumber1(String number1) {
        this.number1 = StringUtils.trimToEmpty(number1);
    }

    /**
     * Returns the number2 part of phone number
     * 
     * @return String
     */
    public String getNumber2() {
        return number2;
    }

    /**
     * Sets the number2 part of phone number
     * 
     * @param number2
     */
    public void setNumber2(String number2) {
        this.number2 = StringUtils.trimToEmpty(number2);
    }

    /**
     * This method clears all the properties
     */
    public void clear() {
        this.areaCode = null;
        this.number1 = null;
        this.number2 = null;
    }

    /**
     * Copy data from one object to another
     * 
     * @param src
     */
    public void copyFrom(PhoneNumber src) {
        this.areaCode = src.getAreaCode();
        this.number1 = src.getNumber1();
        this.number2 = src.getNumber2();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
