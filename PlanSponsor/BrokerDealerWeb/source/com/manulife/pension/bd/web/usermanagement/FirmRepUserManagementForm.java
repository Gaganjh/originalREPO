package com.manulife.pension.bd.web.usermanagement;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


import com.manulife.pension.bd.web.myprofile.MyProfileUtil;
import com.manulife.pension.bd.web.registration.util.AddressVO;
import com.manulife.pension.bd.web.registration.util.PhoneNumber;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.security.bd.valueobject.BDUserAddressValueObject;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedFirmRepUserProfile;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject.SiteLocation;

/**
 * Form for firm rep user
 * 
 * @author guweigu
 * 
 */
public class FirmRepUserManagementForm extends AbstractUserManagementForm {

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
	
	private ExtendedFirmRepUserProfile firmRepUserProfile;

	private String firstName;

	private String lastName;

	private PhoneNumber phoneNumber = new PhoneNumber();

	private String emailAddress;

	private String passCode;

	private AddressVO address = new AddressVO();

	private String defaultSiteLocation;

	private Boolean producerLicense;

	private String firmListStr;
	
	private List<BrokerDealerFirm> firms;

	private String selectedFirmId;

	private String selectedFirmName;
	
	private String riaFirmListStr;
	
	private String riaFirmPermissionsListStr;

	private List<BrokerDealerFirm> riafirms;

	private String selectedRiaFirmId;

	private String selectedRiaFirmName;

	/**
	 * Return the display string for profile status
	 * 
	 * @return
	 */
	public String getProfileStatus() {
		if (firmRepUserProfile != null) {
			return UserManagementHelper.getCreatedProfileStatusMap().get(
					firmRepUserProfile.getProfileStatus());
		} else {
			return "";
		}
	}

	@Override
	protected ExtendedBDExtUserProfile getExtUserProfile() {
		return firmRepUserProfile;
	}

	public ExtendedFirmRepUserProfile getFirmRepUserProfile() {
		return firmRepUserProfile;
	}

	/**
	 * Set the Firm Rep user profile from CSDB
	 * @param firmRepUserProfile
	 * @param isUpdate
	 */
	public void setFirmRepUserProfile(
			ExtendedFirmRepUserProfile firmRepUserProfile, boolean isUpdate) {
		this.firmRepUserProfile = firmRepUserProfile;
		if (firmRepUserProfile == null) {
			return;
		}
		if (!isUpdate) {
			firstName = firmRepUserProfile.getFirstName();
			lastName = firmRepUserProfile.getLastName();
			emailAddress = firmRepUserProfile.getEmailAddress();
			phoneNumber.setValue(firmRepUserProfile.getPhoneNum());
			BDUserAddressValueObject addr = firmRepUserProfile.getAddress();
			address = MyProfileUtil.populateAddressVO(addr);
			firms = new ArrayList<BrokerDealerFirm>(firmRepUserProfile
					.getBrokerDealerFirms());
			riafirms=new ArrayList<BrokerDealerFirm>(firmRepUserProfile
					.getRiaFirms());
			UserManagementHelper.sortFirmsByName(firms);
			UserManagementHelper.sortFirmsById(riafirms);
			SiteLocation site = firmRepUserProfile.getFundDefaultSite();
			if (site != null) {
				defaultSiteLocation = site.toString();
			}
		}
		producerLicense = firmRepUserProfile.getProducerLicense();

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassCode() {
		return passCode;
	}

	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}

	public String getDefaultSiteLocation() {
		return defaultSiteLocation;
	}

	public void setDefaultSiteLocation(String defaultSiteLocation) {
		this.defaultSiteLocation = defaultSiteLocation;
	}

	public Boolean getProducerLicense() {
		return producerLicense;
	}

	public void setProducerLicense(Boolean producerLicense) {
		this.producerLicense = producerLicense;
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

	public PhoneNumber getPhoneNumber() {
		return phoneNumber;
	}

	public AddressVO getAddress() {
		return address;
	}

	public String getRiaFirmListStr() {
		return riaFirmListStr;
	}

	public void setRiaFirmListStr(String riaFirmListStr) {
		this.riaFirmListStr = riaFirmListStr;
	}

	public String getRiaFirmPermissionsListStr() {
		return riaFirmPermissionsListStr;
	}

	public void setRiaFirmPermissionsListStr(String riaFirmPermissionsListStr) {
		this.riaFirmPermissionsListStr = riaFirmPermissionsListStr;
	}

	public List<BrokerDealerFirm> getRiafirms() {
		return riafirms;
	}

	public void setRiafirms(List<BrokerDealerFirm> riafirms) {
		this.riafirms = riafirms;
	}

	public String getSelectedRiaFirmId() {
		return selectedRiaFirmId;
	}

	public void setSelectedRiaFirmId(String selectedRiaFirmId) {
		this.selectedRiaFirmId = selectedRiaFirmId;
	}

	public String getSelectedRiaFirmName() {
		return selectedRiaFirmName;
	}

	public void setSelectedRiaFirmName(String selectedRiaFirmName) {
		this.selectedRiaFirmName = selectedRiaFirmName;
	}
	 @Override
	    public void reset( HttpServletRequest request) {
	    
	    	super.setUpdateSuccess(false);
	    	this.selectedRiaFirmName=StringUtils.EMPTY;
	    	super.setAction(StringUtils.EMPTY);
	    }
}
