package com.manulife.pension.bd.web.userprofile;

import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationRuntimeException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.bd.valueobject.BDMimicUserSecurityProfile;
import com.manulife.pension.service.security.role.BDUserRole;

/**
 * The factory that create a BDUserProfile based on BDUserPrincipal
 * 
 * @author guweigu
 * 
 */
public class BDUserProfileFactory {
	
	private static final Logger log = Logger.getLogger(BDUserProfileFactory.class);
	
	private static final BDUserProfileFactory instance = new BDUserProfileFactory();

	public static BDUserProfileFactory getInstance() {
		return instance;
	}

	private BDUserProfileFactory() {
	}

	/**
	 * Create a user profile based on principal
	 * 
	 * @param principal
	 * @return
	 * @throws SystemException
	 */
	public BDUserProfile getUserProfile(BDPrincipal principal)
			throws SystemException {
		BDUserProfile profile = null;
		BDUserRole role = principal.getBDUserRole();
		if (BDUserProfileHelper.isFinancialRepAssistant(role)) {
			profile = new BDAssistantUserProfile();
		} else if (BDUserProfileHelper.isFinancialRep(role)) {
			profile = new BDBrokerUserProfile();
		} else if (!BDUserProfileHelper.isInternalUser(role)) { // other external users
			profile = new BDExternalUserProfile();
		} else {
			profile = new BDInternalUserProfile();
		}
		profile.setBDPrincipal(principal);
		profile.setMerrillAdvisor(isMerrillAdvisor(principal, profile.isInMimic()));
		profile.setUBSAdvisor(isUBSAdvisor(principal, profile.isInMimic()));
		profile.setRJAdvisor(isRJAdvisor(principal,profile.isInMimic())||BDUserProfileHelper.isInternalUser(role));
		return profile;
	}

	/**
	 * Create a user profile for a mimicked user
	 * 
	 * @param mimicked
	 * @return
	 * @throws SystemException
	 */
	public BDUserProfile getMimickedUserProfile(
			BDMimicUserSecurityProfile mimicked) throws SystemException {
		BDUserProfile profile = getUserProfile(mimicked.getPrincipal());
		profile.setDefaultFundListing(mimicked.getDefaultFundList());
		if (profile instanceof BDAssistantUserProfile) {
			((BDAssistantUserProfile) profile).setParentPrincipal(mimicked
					.getParentPrincipal());
		}
		return profile;
	}
	
	private boolean isMerrillAdvisor(BDPrincipal principal, boolean isInMimic) {
		boolean isMerrillAdvisor = false;
		try {
			isMerrillAdvisor = ContractServiceDelegate.getInstance().isMerrillLynchUsers(principal.getProfileId(),
					principal.getBDUserRole().getRoleType().getUserRoleCode(), isInMimic);
		} catch (SystemException e) {
			log.error("failed while checking for merrill user");
			throw new ApplicationRuntimeException("failed while checking for merrill user", e);
		}
		return isMerrillAdvisor;
	}
	
	private boolean isUBSAdvisor(BDPrincipal principal, boolean isInMimic) {
		boolean isUBSAdvisor = false;
		try {
			isUBSAdvisor = ContractServiceDelegate.getInstance().isUBSUsers(principal.getProfileId(),
					principal.getBDUserRole().getRoleType().getUserRoleCode(), isInMimic);
		} catch (SystemException e) {
			log.error("failed while checking for UBS user");
			throw new ApplicationRuntimeException("failed while checking for UBS user", e);
		}
		return isUBSAdvisor;
	}
	private boolean isRJAdvisor(BDPrincipal principal, boolean isInMimic) {
		boolean isRJAdvisor = false;
		try {
			isRJAdvisor = ContractServiceDelegate.getInstance().isRJUsers(principal.getProfileId(),
					principal.getBDUserRole().getRoleType().getUserRoleCode(), isInMimic);
		} catch (SystemException e) {
			log.error("failed while checking for RJ user");
			throw new ApplicationRuntimeException("failed while checking for RJ user", e);
		}
		return isRJAdvisor;
	}
	
}
