package com.manulife.pension.bd.web.usermanagement;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.bd.web.myprofile.MyProfileUtil;
import com.manulife.pension.bd.web.registration.util.AddressVO;
import com.manulife.pension.bd.web.registration.util.PhoneNumber;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestCompleteException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestExpiredException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestFailedException;
import com.manulife.pension.service.security.bd.exception.BDSecurityRequestUserDeletedException;
import com.manulife.pension.service.security.bd.exception.BDUserPartyNotExistException;
import com.manulife.pension.service.security.bd.exception.BDUserPartyNotPendingException;
import com.manulife.pension.service.security.bd.valueobject.BDUserAddressValueObject;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedRiaUserProfile;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject.SiteLocation;

/**
 * Form for RIA user
 * 
 * @author guweigu
 * 
 */
public class RiaUserManagementForm extends AbstractUserManagementForm {

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
	
	private ExtendedRiaUserProfile riaUserProfile;

	private String firstName;

	private String lastName;

	private PhoneNumber phoneNumber = new PhoneNumber();

	private String emailAddress;

	private String passCode;

	private AddressVO address = new AddressVO();

	private String defaultSiteLocation;

	private Boolean producerLicense;

	private String firmListStr;
	
	private String firmPermissionsListStr;

	private List<BrokerDealerFirm> firms;
	
	private String selectedFirmId;

	private String selectedFirmName;

	/**
	 * Return the display string for profile status
	 * 
	 * @return
	 */
	public String getProfileStatus() {
		if (riaUserProfile != null) {
			return UserManagementHelper.getCreatedProfileStatusMap().get(
					riaUserProfile.getProfileStatus());
		} else {
			return "";
		}
	}

	@Override
	protected ExtendedBDExtUserProfile getExtUserProfile() {
		return riaUserProfile;
	}

	public ExtendedRiaUserProfile getriaUserProfile() {
		return riaUserProfile;
	}

	/**
	 * Set the Ria user profile from CSDB
	 * 
	 * @param RiaUserProfile
	 * @param isUpdate
	 * @throws SystemException
	 * @throws BDSecurityRequestUserDeletedException
	 * @throws BDUserPartyNotPendingException
	 * @throws BDSecurityRequestExpiredException
	 * @throws BDUserPartyNotExistException
	 * @throws BDSecurityRequestFailedException
	 * @throws BDSecurityRequestCompleteException
	 */
	public void setRiaUserProfile(ExtendedRiaUserProfile riaUserProfile,
			boolean isUpdate) {
		this.riaUserProfile = riaUserProfile;
		if (riaUserProfile == null) {
			return;
		}
		if (!isUpdate) {
			/*
			 * BDSecurityInteractionRequestEx request =
			 * BDSecurityInteractionRequestDao
			 * .getInstance().getRequest(requestId);
			 * BDSecurityInteractionRequestHandler
			 * .getInstance().validateRequest( request); long userProfileId =
			 * request.getUserProfileId();
			 */
			firstName = riaUserProfile.getFirstName();
			lastName = riaUserProfile.getLastName();
			emailAddress = riaUserProfile.getEmailAddress();
			phoneNumber.setValue(riaUserProfile.getPhoneNum());
			BDUserAddressValueObject addr = riaUserProfile.getAddress();
			address = MyProfileUtil.populateAddressVO(addr);
			firms = new ArrayList<BrokerDealerFirm>(
					riaUserProfile.getRiaFirms());
			// UserManagementHelper.sortFirmsByName(firms);
			UserManagementHelper.sortFirmsById(firms);
			SiteLocation site = riaUserProfile.getFundDefaultSite();
			if (site != null) {
				defaultSiteLocation = site.toString();
			}
		}
		producerLicense = riaUserProfile.getProducerLicense();

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

	public String getFirmPermissionsListStr() {
		return firmPermissionsListStr;
	}

	public void setFirmPermissionsListStr(String firmPermissionsListStr) {
		this.firmPermissionsListStr = firmPermissionsListStr;
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
}
