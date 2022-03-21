package com.manulife.pension.bd.web.usermanagement;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.report.BaseReportForm;

/**
 * This is the form object for ExtUserInforceReportAction
 * 
 * @author Ilamparithi
 * 
 */
public class ExtUserInforceReportForm extends BaseReportForm {

    private static final long serialVersionUID = 1L;

    private String lastName = "";

    private String firstName = "";

    private String contractNum = "";

    private String firmId = "";

    private String firmName = "";
    
    private String selectedRiaFirmId = "";
    
    private String selectedRiaFirmName="";

    private String userRoleCode = "";

    private String regFromDate = "";

    private String regToDate = "";

    private Integer contractNumValue = null;

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
     * Returns the contractNum
     * 
     * @return the contractNum
     */
    public String getContractNum() {
        return contractNum;
    }

    /**
     * Sets the contractNum. If it is invalid, the contractNumValue is set as null.
     * 
     * @param contractNum the contractNum to set
     */
    public void setContractNum(String contractNum) {
        this.contractNum = StringUtils.trimToEmpty(contractNum);
        if (!StringUtils.isEmpty(this.contractNum)) {
            try {
                contractNumValue = Integer.parseInt(contractNum);
            } catch (NumberFormatException e) {
                contractNumValue = null;
            }
        } else {
            contractNumValue = null;

        }
    }

    /**
     * Returns the contractNumValue
     * 
     * @return the contractNumValue
     */
    public Integer getContractNumValue() {
        return contractNumValue;
    }

    /**
     * Returns the firmId
     * 
     * @return the firmId
     */
    public String getFirmId() {
        return firmId;
    }

    /**
     * Sets the firmId
     * 
     * @param firmId the firmId to set
     */
    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    /**
     * Returns the firmName
     * 
     * @return the firmName
     */
    public String getFirmName() {
        return firmName;
    }

    /**
     * Sets the firmName
     * 
     * @param firmName the firmName to set
     */
    public void setFirmName(String firmName) {
        this.firmName = StringUtils.trimToEmpty(firmName);
    }
    
    /**
	 * @return the selectedRiaFirmId
	 */
	public String getSelectedRiaFirmId() {
		return selectedRiaFirmId;
	}

	/**
	 * @param selectedRiaFirmId the selectedRiaFirmId to set
	 */
	public void setSelectedRiaFirmId(String selectedRiaFirmId) {
		this.selectedRiaFirmId = selectedRiaFirmId;
	}

	/**
	 * @return the selectedRiaFirmName
	 */
	public String getSelectedRiaFirmName() {
		return selectedRiaFirmName;
	}

	/**
	 * @param selectedRiaFirmName the selectedRiaFirmName to set
	 */
	public void setSelectedRiaFirmName(String selectedRiaFirmName) {
		this.selectedRiaFirmName = selectedRiaFirmName;
	}

	/**
     * Returns the userRoleCode
     * 
     * @return the userRoleCode
     */
    public String getUserRoleCode() {
        return userRoleCode;
    }

    /**
     * Sets the userRoleCode
     * 
     * @param userRoleId the userRoleCode to set
     */
    public void setUserRoleCode(String userRoleCode) {
        this.userRoleCode = userRoleCode;
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
                && StringUtils.isEmpty(contractNum) && StringUtils.isEmpty(firmId)
                && StringUtils.isEmpty(userRoleCode) && StringUtils.isEmpty(regFromDate)
                && StringUtils.isEmpty(regToDate);
    }

}
