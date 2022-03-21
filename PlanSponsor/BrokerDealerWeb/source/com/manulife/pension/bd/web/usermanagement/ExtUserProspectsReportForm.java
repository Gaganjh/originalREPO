package com.manulife.pension.bd.web.usermanagement;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.report.BaseReportForm;

public class ExtUserProspectsReportForm extends BaseReportForm {

    private static final long serialVersionUID = 1L;

    private String lastName = "";

    private String firstName = "";

    private String state = "";

    private String regFromDate = "";

    private String regToDate = "";

    /**
     * Returns the lastName
     * 
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the lastName
     * 
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = StringUtils.trimToEmpty(lastName);
    }

    /**
     * Returns the firstName
     * 
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the firstName
     * 
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = StringUtils.trimToEmpty(firstName);
    }

    /**
     * Returns the state
     * 
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state
     * 
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Returns the regFromDate
     * 
     * @return the regFromDate
     */
    public String getRegFromDate() {
        return regFromDate;
    }

    /**
     * Sets the regFromDate
     * 
     * @param regFromDate the regFromDate to set
     */
    public void setRegFromDate(String regFromDate) {
        this.regFromDate = regFromDate;
    }

    /**
     * Returns the regToDate
     * 
     * @return the regToDate
     */
    public String getRegToDate() {
        return regToDate;
    }

    /**
     * Sets the regToDate
     * 
     * @param regToDate the regToDate to set
     */
    public void setRegToDate(String regToDate) {
        this.regToDate = regToDate;
    }

    /**
     * Return whether the form is empty
     * 
     * @return boolean
     */
    public boolean isEmptyForm() {
        return StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName)
                && StringUtils.isEmpty(state) && StringUtils.isEmpty(regFromDate)
                && StringUtils.isEmpty(regToDate);
    }
}
