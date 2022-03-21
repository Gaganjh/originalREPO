package com.manulife.pension.bd.web.registration.util;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

/**
 * This is a value object class for Address.
 * 
 * @author Ilamparithi
 * 
 */
public class AddressVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String USA_COUNTRY_CODE = "USA";

    private String address1;

    private String address2;

    private String city;

    private String otherState;

    private String usState = "";

    private String otherZipCode;

    private String usZipCode1;

    private String usZipCode2;

    private String country = USA_COUNTRY_CODE;

    /**
     * Returns the address1
     * 
     * @return String
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * Sets the address1
     * 
     * @param address1
     */
    public void setAddress1(String address1) {
        this.address1 = StringUtils.trimToEmpty(address1);
    }

    /**
     * Returns the address2
     * 
     * @return String
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * Sets the address2
     * 
     * @param address2
     */
    public void setAddress2(String address2) {
        this.address2 = StringUtils.trimToEmpty(address2);
    }

    /**
     * Return the city
     * 
     * @return String
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city
     * 
     * @param city
     */
    public void setCity(String city) {
        this.city = StringUtils.trimToEmpty(city);
    }

    /**
     * Returns the state based on the country as state value is stored in two different properties
     * based on the country
     * 
     * @return String
     */
    public String getState() {
        if (StringUtils.equals(USA_COUNTRY_CODE, getCountry())) {
            return usState;
        } else {
            return otherState;
        }
    }

    /**
     * Returns country
     * 
     * @return String
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country
     * 
     * @param country
     */
    public void setCountry(String country) {
        this.country = StringUtils.upperCase(StringUtils.trimToEmpty(country));
        if (StringUtils.isEmpty(this.country)) {
            this.country = USA_COUNTRY_CODE;
        }
    }

    /**
     * Return the usState
     * 
     * @return
     */
    public String getUsState() {
        return usState;
    }

    /**
     * Sets the usState
     * 
     * @param usState
     */
    public void setUsState(String usState) {
        this.usState = StringUtils.upperCase(StringUtils.trimToEmpty(usState));
    }

    /**
     * Returns the first part of US zip code
     * 
     * @return String
     */
    public String getUsZipCode1() {
        return usZipCode1;
    }

    /**
     * Sets the first part of us zip code
     * 
     * @param usZipCode1
     */
    public void setUsZipCode1(String usZipCode1) {
        this.usZipCode1 = usZipCode1;
    }

    /**
     * Returns the second part of US zip code
     * 
     * @return String
     */
    public String getUsZipCode2() {
        return usZipCode2;
    }

    /**
     * Returns the second part of US zip code
     * 
     * @param usZipCode2
     */
    public void setUsZipCode2(String usZipCode2) {
        this.usZipCode2 = StringUtils.trimToEmpty(usZipCode2);
    }

    /**
     * Returns the zip code based on the country value as zip code is stored in two different
     * properties based on the country value.
     * 
     * @return String
     */
    public String getZipCode() {
        if (StringUtils.equals(USA_COUNTRY_CODE, getCountry())) {
            return getUsZipCode1() + getUsZipCode2();
        } else {
            return otherZipCode;
        }
    }

    /**
     * Returns a List of US states
     * 
     * @return List<LabelValueBean>
     */
    public List<LabelValueBean> getStates() {
        return AddressUtil.getInstance().getStatesList();
    }

    /**
     * Returns a List of countries
     * 
     * @return List<LabelValueBean>
     */
    public List<LabelValueBean> getCountries() {
        return AddressUtil.getInstance().getCountriesList();
    }

    /**
     * Returns the state value of country other than US
     * 
     * @return String
     */
    public String getOtherState() {
        return otherState;
    }

    /**
     * Sets the state value of country other than US
     * 
     * @param otherState
     */
    public void setOtherState(String otherState) {
        this.otherState = StringUtils.upperCase(StringUtils.trimToEmpty(otherState));
    }

    /**
     * Returns the zip code value of country other than US
     * 
     * @return String
     */
    public String getOtherZipCode() {
        return otherZipCode;
    }

    /**
     * Sets the zip code value of country other than US
     * 
     * @param otherZipCode
     */
    public void setOtherZipCode(String otherZipCode) {
        this.otherZipCode = StringUtils.trimToEmpty(otherZipCode);
    }

    /**
     * Copy data from one object to another
     * 
     * @param src
     */
    public void copyFrom(AddressVO src) {
        this.address1 = src.getAddress1();
        this.address2 = src.getAddress2();
        this.city = src.getCity();
        this.otherState = src.getOtherState();
        this.usState = src.getUsState();
        this.country = src.getCountry();
        this.otherZipCode = src.getOtherZipCode();
        this.usZipCode1 = src.getUsZipCode1();
        this.usZipCode2 = src.getUsZipCode2();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
