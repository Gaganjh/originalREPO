package com.manulife.pension.bd.web.registration;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.controller.BaseForm;

/**
 * This is the form bean for RegisterBrokerAssistantStep1Action.
 * 
 * @author Ilamparithi
 * 
 */
public class RegisterBrokerAssistantValidationForm extends BaseForm {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_LAST_NAME = "lastName";

    public static final String FIELD_SUPERVISOR_LAST_NAME = "supervisorLastName";

    private String lastName;

    private String supervisorLastName;

    private boolean disabled;
    
    /**
     * Returns the lastName
     * 
     * @return String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the lastName
     * 
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = StringUtils.trimToEmpty(lastName);
    }

    /**
     * Returns the supervisorLastName
     * 
     * @return String
     */
    public String getSupervisorLastName() {
        return supervisorLastName;
    }

    /**
     * Sets the supervisorLastName
     * 
     * @param supervisorLastName
     */
    public void setSupervisorLastName(String supervisorLastName) {
        this.supervisorLastName = StringUtils.trimToEmpty(supervisorLastName);
    }

    /**
     * Copy date from one form to another
     * 
     * @param src
     */
    public void copyFrom(RegisterBrokerAssistantValidationForm src) {
        if (src != null) {
            lastName = src.getLastName();
            supervisorLastName = src.getSupervisorLastName();
        }
    }

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
