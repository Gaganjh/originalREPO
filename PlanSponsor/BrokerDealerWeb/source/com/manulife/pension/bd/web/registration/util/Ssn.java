package com.manulife.pension.bd.web.registration.util;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This is the value object for ssn.
 * 
 * @author Ilamparithi
 */
public class Ssn implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    private String ssn1;

    private String ssn2;

    private String ssn3;

    /**
     * Returns the concatenated SSN value
     * 
     * @return String
     */
    public String getValue() {
        return StringUtils.trimToEmpty(ssn1) + StringUtils.trimToEmpty(ssn2)
                + StringUtils.trimToEmpty(ssn3);
    }

    /**
     * Splits the SSN value
     * 
     * @param value
     */
    public void setValue(String value) {
        ssn1 = StringUtils.substring(value, 0, 3);
        ssn2 = StringUtils.substring(value, 3, 5);
        ssn3 = StringUtils.substring(value, 5, 9);
    }

    /**
     * Returns the first part of SSN
     * 
     * @return String
     */
    public String getSsn1() {
        return ssn1;
    }

    /**
     * Sets the first part of SSN
     * 
     * @param ssn1
     */
    public void setSsn1(String ssn1) {
        this.ssn1 = ssn1;
    }

    /**
     * Returns the second part of SSN
     * 
     * @return String
     */
    public String getSsn2() {
        return ssn2;
    }

    /**
     * Sets the second part of SSN
     * 
     * @param ssn2
     */
    public void setSsn2(String ssn2) {
        this.ssn2 = ssn2;
    }

    /**
     * Returns the last part of SSN
     * 
     * @return String
     */
    public String getSsn3() {
        return ssn3;
    }

    /**
     * Sets the last part of SSN
     * 
     * @param ssn3
     */
    public void setSsn3(String ssn3) {
        this.ssn3 = ssn3;
    }

    /**
     * Copy date from one object to another
     * 
     * @param src
     */
    public void copyFrom(Ssn src) {
        this.ssn1 = src.getSsn1();
        this.ssn2 = src.getSsn2();
        this.ssn3 = src.getSsn3();
    }

    public void clear() {
        ssn1 = null;
        ssn2 = null;
        ssn3 = null;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}