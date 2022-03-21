package com.manulife.pension.bd.web.userprofile;

import java.util.Date;

import com.manulife.pension.platform.web.controller.BaseUserProfile;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.bd.BDUserPasswordStatus;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequest;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject.SiteLocation;
import com.manulife.pension.service.security.role.BDUserRole;
import com.manulife.pension.util.StaticHelperClass;

/**
 * Super class for BDW's user profile
 * 
 * @author guweigu
 * 
 */
abstract public class BDUserProfile extends BaseUserProfile {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date lastLoginDate;
	private BDUserPasswordStatus passwordStatus;
	private boolean inMimic = false;
	private SiteLocation defaultFundListing;
	private LoginStatus loginStatus;
	// it is set to the activation type, when the login session
	// is from the activation
	private BDSecurityInteractionRequest.Type activationType;
	private Boolean merrillAdvisor;
	private boolean merrillContract;
	private Boolean ubsAdvisor;
	private Boolean rjAdvisor;

	/**
	 * Constructor for User
	 */
	public BDUserProfile() {
		super();
	}

	public String toString() {
		return StaticHelperClass.toString(this);
	}

	/**
	 * Gets the lastLoginDate
	 * 
	 * @return Returns a Date
	 */
	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	/**
	 * Sets the lastLoginDate
	 * 
	 * @param lastLoginDate
	 *            The lastLoginDate to set
	 */
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public BDPrincipal getBDPrincipal() {
		return (BDPrincipal) getAbstractPrincipal();
	}

	public void setBDPrincipal(BDPrincipal principal) {
		super.setAbstractPrincipal(principal);
	}

	/**
	 * Gets the role
	 * 
	 * @return Returns a UserRole
	 */
	public BDUserRole getRole() {
		return getBDPrincipal().getBDUserRole();
	}

	/**
	 * Gets the passwordStatus
	 * 
	 * @return Returns a String
	 */
	public BDUserPasswordStatus getPasswordStatus() {
		return this.passwordStatus;
	}

	/**
	 * Sets the passwordStatus
	 * 
	 * @param passwordStatus
	 *            The passwordStatus to set
	 */
	public void setPasswordStatus(BDUserPasswordStatus passwordStatus) {
		this.passwordStatus = passwordStatus;
	}

	public boolean startMimic(BDUserProfile mimickingUser) {
		if (!inMimic && mimickingUser.canMimic(this)) {
			inMimic = true;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Only the user profile allows mimic can overrider this
	 * 
	 * @return
	 */
	public boolean isInMimic() {
		return inMimic;
	}

	/**
	 * Only Internal user can mimic
	 * 
	 * @return
	 */
	public boolean canMimic(BDUserProfile mimickedUser) {
		return false;
	}

	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}

	public SiteLocation getDefaultFundListing() {
		return defaultFundListing;
	}

	public void setDefaultFundListing(SiteLocation defaultFundListing) {
		this.defaultFundListing = defaultFundListing;
	}

	abstract public boolean isInternalUser();

	public BDSecurityInteractionRequest.Type getActivationType() {
		return activationType;
	}

	public void setActivationType(BDSecurityInteractionRequest.Type activationType) {
		this.activationType = activationType;
	}

	abstract public String getAssociatedApprovingFirmCode();

	public Boolean isMerrillAdvisor() {
		return merrillAdvisor;
	}

	public void setMerrillAdvisor(Boolean merrillAdvisor) {
		this.merrillAdvisor = merrillAdvisor;
	}
	
	
	public boolean isMerrillContract() {
		return merrillContract;
	}
	
	public void setMerrillContract(boolean merrillContract) {
		this.merrillContract = merrillContract;
	}
	
	public Boolean isUBSAdvisor() {
		return ubsAdvisor;
	}

	public void setUBSAdvisor(Boolean ubsAdvisor) {
		this.ubsAdvisor = ubsAdvisor;
	}
	public Boolean isRJAdvisor() {
		return rjAdvisor;
	}
	
	public void setRJAdvisor(Boolean rjAdvisor) {
		this.rjAdvisor = rjAdvisor;
	}
	

}
