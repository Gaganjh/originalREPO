package com.manulife.pension.bd.web.myprofile;

import java.util.SortedSet;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.registration.util.AddressVO;
import com.manulife.pension.bd.web.registration.util.PhoneNumber;
import com.manulife.pension.bd.web.usermanagement.UserManagementHelper;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBasicBrokerUserProfile;

/**
 * This is the form object for MyProfilePersonalInfoAction.
 * 
 * @author Ilamparithi
 * 
 */
public class MyProfilePersonalInfoForm extends AutoForm {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_FIRST_NAME = "firstName";

    public static final String FIELD_LAST_NAME = "lastName";

    public static final String FIELD_COMPANY_NAME = "companyName";

    public static final String FIELD_EMAIL = "emailAddress";

    public static final String FIELD_ADDRESS = "address";

    public static final String FIELD_PHONE_NUMBER = "phoneNumber";

    private ExtendedBDExtUserProfile externalUserProfile;

    private String roleId;

    private String financialRepName;

    private AddressVO address = new AddressVO();

    private PhoneNumber phoneNumber = new PhoneNumber();

    private boolean changed = false;

    private boolean success = false;

    /**
     * Returns the ExtendedBDExtUserProfile object
     * 
     * @return the externalUserProfile
     */
    public ExtendedBDExtUserProfile getExternalUserProfile() {
        return externalUserProfile;
    }

    /**
     * Sets the ExtendedBDExtUserProfile object
     * 
     * @param externalUserProfile the externalUserProfile to set
     */
    public void setExternalUserProfile(ExtendedBDExtUserProfile externalUserProfile) {
        this.externalUserProfile = externalUserProfile;
    }

    /**
     * Return the roleId of the user
     * 
     * @return the roleId
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * Sets the roleId of the user
     * 
     * @param roleId the roleId to set
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * Returns the financialRepName
     * 
     * @return the financialRepName
     */
    public String getFinancialRepName() {
        return financialRepName;
    }

    /**
     * Sets the financialRepName
     * 
     * @param financialRepName the financialRepName to set
     */
    public void setFinancialRepName(String financialRepName) {
        this.financialRepName = StringUtils.trimToEmpty(financialRepName);
    }

    /**
     * Returns the address
     * 
     * @return the address
     */
    public AddressVO getAddress() {
        return address;
    }

    /**
     * Sets the address
     * 
     * @param address the address to set
     */
    public void setAddress(AddressVO address) {
        this.address = address;
    }

    /**
     * Returns the phoneNumber
     * 
     * @return the phoneNumber
     */
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phoneNumber
     * 
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns a flag that indicates whether the form is dirty or not
     * 
     * @return the changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Sets a flag that indicates whether the form is dirty or not
     * 
     * @param changed the changed to set
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * Return a flag to indicate whether the save action is success or not
     * 
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success flag
     * 
     * @param success the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Returns a flag to indicate whether to show the company name or not
     * 
     * @return boolean
     */
    public boolean isShowCompanyName() {
        if (StringUtils.equals(roleId, BDConstants.BASIC_BROKER_ROLE_ID)) {
            ExtendedBasicBrokerUserProfile basicBrokerUserProfile = (ExtendedBasicBrokerUserProfile) externalUserProfile;
            if (basicBrokerUserProfile.getBrokerDealerFirm() == null
                    || StringUtils.isEmpty(basicBrokerUserProfile.getBrokerDealerFirm()
                            .getFirmName())) {
                return true;
            }
        }
        return false;
    }
    
    /**
	 * Get sorted firm names
	 * 
	 * @return
	 */
	public SortedSet<String> getSortedBrokerDealerFirmNames() {
		return UserManagementHelper.getSortedBDFirmNames(externalUserProfile
				.getBrokerDealerFirms());
	}
	
	/**
	 * Get sorted firm names
	 * 
	 * @return
	 */
	public SortedSet<String> getSortedRiaFirmNames() {
		return UserManagementHelper.getSortedRiaFirmNamesWithPermissions(externalUserProfile.getRiaFirms());
	}
}
