package com.manulife.pension.bd.web.registration.util;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This is a value object class for Tax Id.
 * 
 * @author Ilamparithi
 * 
 */
public class TaxId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String taxId1;

    private String taxId2;

    /**
     * Returns the concatenated taxId
     * 
     * @return String
     */
    public String getValue() {
        return StringUtils.trimToEmpty(taxId1) + StringUtils.trimToEmpty(taxId2);
    }

    /**
     * Splits the taxId value
     * 
     * @param value
     */
    public void setValue(String value) {
        taxId1 = StringUtils.substring(value, 0, 2);
        taxId2 = StringUtils.substring(value, 2, 9);
    }

    /**
     * Returns the first part of taxId
     * 
     * @return String
     */
    public String getTaxId1() {
        return taxId1;
    }

    /**
     * Sets the first part of taxId
     * 
     * @param taxId1
     */
    public void setTaxId1(String taxId1) {
        this.taxId1 = taxId1;
    }

    /**
     * Returns the second part of taxId
     * 
     * @return String
     */
    public String getTaxId2() {
        return taxId2;
    }

    /**
     * Sets the second part of taxId
     * 
     * @param taxId2
     */
    public void setTaxId2(String taxId2) {
        this.taxId2 = taxId2;
    }

    /**
     * Copy date from one object to another
     * 
     * @param src
     */
    public void copyFrom(TaxId src) {
        this.taxId1 = src.getTaxId1();
        this.taxId2 = src.getTaxId2();
    }

    public void clear() {
        taxId1 = null;
        taxId2 = null;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
