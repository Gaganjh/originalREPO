package com.manulife.pension.bd.web.usermanagement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.registration.util.PhoneNumber;
import com.manulife.pension.platform.web.controller.BaseForm;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;

/**
 * The form bean for firm rep creation
 * 
 * @author guweigu
 * 
 */
public class CreateFirmRepForm extends BaseForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_FIRST_NAME = "firstName";

    public static final String FIELD_LAST_NAME = "lastName";

    public static final String FIELD_EMAIL = "emailAddress";

    public static final String FIELD_PHONE_NUMBER = "phoneNumber";

    public static final String FIELD_PASS_CODE = "passCode";

    public static final String FIELD_FIRMS = "firms";

    private String firstName;

    private String lastName;

    private PhoneNumber phoneNumber = new PhoneNumber();

    private String emailAddress;

    private String passCode;

    private String firmListStr;

    private List<BrokerDealerFirm> firms = new ArrayList<BrokerDealerFirm>(0);

    private String selectedFirmId;

    private String selectedFirmName;

    private boolean completed;
    
    private boolean changed = false;
    
    public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public String getFirmListStr() {
        return firmListStr;
    }

    public void setFirmListStr(String firmListStr) {
        this.firmListStr = firmListStr;
    }

    public List<BrokerDealerFirm> getFirms() {
        return firms;
    }

    public void setFirms(List<BrokerDealerFirm> firms) {
        this.firms = firms;
    }

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

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = StringUtils.trimToEmpty(passCode);
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = StringUtils.trimToEmpty(emailAddress);
    }

    public String getSelectedFirmId() {
        return selectedFirmId;
    }

    public void setSelectedFirmId(String selectedFirmId) {
        this.selectedFirmId = selectedFirmId;
    }

    public String getSelectedFirmName() {
        return selectedFirmName;
    }

    public void setSelectedFirmName(String selectedFirmName) {
        this.selectedFirmName = selectedFirmName;
    }

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}
