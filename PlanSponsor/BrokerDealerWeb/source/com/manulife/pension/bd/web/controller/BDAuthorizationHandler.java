package com.manulife.pension.bd.web.controller;

import java.util.List;

import com.manulife.pension.bd.web.content.ContentFirmRestrictionFacade;
import com.manulife.pension.bd.web.userprofile.BDExternalUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.LoginStatus;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.BaseUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ContentFirmRestrictionRule;

public class BDAuthorizationHandler implements WebResourceAuthorizationHandler {
	private boolean mimicAllowed;
	private boolean licenseRequired;
	private Integer firmRestriction;
	private LoginStatus loginStatus = LoginStatus.FullyLogin;

	public boolean hasPermission() {
		return false;
	}

	public boolean needsAuthentication() {
		return true;
	}

	public boolean isMimicAllowed() {
		return mimicAllowed;
	}

	public void setMimicAllowed(boolean mimicAllowed) {
		this.mimicAllowed = mimicAllowed;
	}

	public boolean isLicenseRequired() {
		return licenseRequired;
	}

	public void setLicenseRequired(boolean licenseRequired) {
		this.licenseRequired = licenseRequired;
	}

	public boolean isUserAuthorized(AuthorizationSubject subject, String url)
			throws SystemException {
		BaseUserProfile user = subject.getUserProfile();
		if (!(user instanceof BDUserProfile)) {
			return false;
		}
		BDUserProfile bdUser = (BDUserProfile) user;

		if (bdUser.getLoginStatus() != getLoginStatus()) {
			return false;
		}

		if (!isMimicAllowed() && bdUser.isInMimic() && !subject.isIgnoreMimic()) {
			return false;
		}

		if (isLicenseRequired()) {
			Boolean license = bdUser.getBDPrincipal().getProducerLicense();
			// only if the license requirement is not met, return false
			// otherwise continue with other verification
			if (license == null || !license.booleanValue()) {
				return false;
			}
		}

		if ((bdUser instanceof BDExternalUserProfile)
				&& firmRestriction != null) {
			ContentFirmRestrictionRule rule = ContentFirmRestrictionFacade
					.getInstance().getContentRestictionRule(firmRestriction);
			// Check if the rule is available
			if (rule != null) {
				// check firm restriction
				BDExternalUserProfile extUser = (BDExternalUserProfile) bdUser;
				/** Firm restriction needs to be done **/
				List<Long> bdFirmIds = extUser.getBrokerDealerFirmIds();
				if (!rule.isAllowed(bdFirmIds)) {
					return false;
				}
			}
		}
		return true;
	}

	public Integer getFirmRestriction() {
		return firmRestriction;
	}

	public void setFirmRestriction(Integer firmRestriction) {
		this.firmRestriction = firmRestriction;
	}

	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public String getLoginStatusCode() {
		return this.loginStatus == null ? null : loginStatus.getCode();
	}

	public void setLoginStatusCode(String loginStatusCode) {
		this.loginStatus = LoginStatus.getByCode(loginStatusCode);
	}

}
