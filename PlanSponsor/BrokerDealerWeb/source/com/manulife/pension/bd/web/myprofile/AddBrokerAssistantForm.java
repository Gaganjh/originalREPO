package com.manulife.pension.bd.web.myprofile;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.controller.BaseForm;

public class AddBrokerAssistantForm extends BaseForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_FIRST_NAME = "firstName";

    public static final String FIELD_LAST_NAME = "lastName";

    public static final String FIELD_EMAIL = "email";

    public static final String FIELD_TERM = "term";

    private String firstName;

    private String lastName;

    private String email;

    private Boolean term = false;

    private boolean show = false;
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = StringUtils.trimToEmpty(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = StringUtils.trimToEmpty(lastName);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = StringUtils.trimToEmpty(email);
    }

    public Boolean getTerm() {
        return term;
    }

    public void setTerm(Boolean term) {
        this.term = term;
    }

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

}
