package com.manulife.pension.bd.web.userprofile;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.role.BDUserRole;

/**
 * Helper class to check the user role of the user profile
 * 
 * @author guweigu
 * 
 */
public class BDUserProfileHelper {
	/**
	 * Returns if a user profile is a Financial Rep
	 * 
	 * @param userProfile
	 * @return
	 */
	public static boolean isInternalUser(BDUserProfile userProfile) {
		return userProfile != null
				&& isInternalUser(userProfile.getBDPrincipal().getBDUserRole());
	}

	/**
	 * Returns if a user role is a Financial Rep
	 * 
	 * @param userProfile
	 * @return
	 */
	public static boolean isInternalUser(BDUserRole userRole) {
		return userRole != null && userRole.getRoleType().isInternal();
	}

	/**
	 * Returns if a user profile is a Financial Rep
	 * 
	 * @param userProfile
	 * @return
	 */
	public static boolean isFinancialRep(BDUserProfile userProfile) {
		return userProfile != null
				&& isFinancialRep(userProfile.getBDPrincipal().getBDUserRole());
	}

	/**
	 * Returns if a user role is a Financial Rep
	 * 
	 * @param userProfile
	 * @return
	 */
	public static boolean isFinancialRep(BDUserRole userRole) {
		return userRole != null
				&& userRole.getRoleType()
						.compareTo(BDUserRoleType.FinancialRep) == 0;
	}

	/**
	 * Returns if a user profile is a Basic Financial Rep
	 * 
	 * @param userProfile
	 * @return
	 */
	public static boolean isBasicFinancialRep(BDUserProfile userProfile) {
		return userProfile != null
				&& isBasicFinancialRep(userProfile.getBDPrincipal()
						.getBDUserRole());
	}

	/**
	 * Returns if a user role is a Basic Financial Rep
	 * 
	 * @param userProfile
	 * @return
	 */
	public static boolean isBasicFinancialRep(BDUserRole userRole) {
		return userRole != null
				&& userRole.getRoleType().compareTo(
						BDUserRoleType.BasicFinancialRep) == 0;
	}

	/**
	 * Returns if a user profile is a Financial Rep Assistant
	 * 
	 * @param userProfile
	 * @return
	 */
	public static boolean isFinancialRepAssistant(BDUserProfile userProfile) {
		return userProfile != null
				&& isFinancialRepAssistant(userProfile.getBDPrincipal()
						.getBDUserRole());
	}

	/**
	 * Returns if a user role is a Financial Rep Assistant
	 * 
	 * @param userProfile
	 * @return
	 */
	public static boolean isFinancialRepAssistant(BDUserRole userRole) {
		return userRole != null
				&& userRole.getRoleType().compareTo(
						BDUserRoleType.FinancialRepAssistant) == 0;
	}

	/**
	 * Returns if a user profile is a Firm Rep
	 * 
	 * @param userProfile
	 * @return
	 */
	public static boolean isFirmRep(BDUserProfile userProfile) {
		return userProfile != null
				&& isFirmRep(userProfile.getBDPrincipal().getBDUserRole());
	}

	/**
	 * Returns if a user role is a Firm Rep
	 * 
	 * @param userProfile
	 * @return
	 */
	public static boolean isFirmRep(BDUserRole userRole) {
		return userRole != null
				&& userRole.getRoleType().compareTo(BDUserRoleType.FirmRep) == 0;
	}

	/**
	 * Returns if a user profile is a RVP
	 * 
	 * @param userProfile
	 * @return
	 */
	public static boolean isRvp(BDUserProfile userProfile) {
		return userProfile != null
				&& isRvp(userProfile.getBDPrincipal().getBDUserRole());
	}

	/**
	 * Returns if a user role is a RVP
	 * 
	 * @param userRole
	 * @return
	 */
	public static boolean isRvp(BDUserRole userRole) {
		return userRole != null
				&& userRole.getRoleType().compareTo(BDUserRoleType.RVP) == 0;
	}

	private static boolean containsApprovingFirm(List<Long> firmIds, String approvingFirmNFACode) {
		for (Long fid : firmIds) {
			if (StringUtils.equals(approvingFirmNFACode, ApprovingFirmUtil.getInstance().getApprovingFirmNFACode(fid))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * If a user role is associated with special approving firm
	 * @param userProfile
	 * @param approvingFirmNFACode
	 * @return
	 */
	public static boolean associatedWithApprovingFirm(
			BDUserProfile userProfile, String approvingFirmNFACode) {
		BDUserRole role = userProfile.getBDPrincipal().getBDUserRole();
		switch (role.getRoleType()) {
		case FinancialRep:
			return containsApprovingFirm(((BDExternalUserProfile) userProfile)
					.getBrokerDealerFirmIds(), approvingFirmNFACode);
		case FinancialRepAssistant:
			return containsApprovingFirm(((BDAssistantUserProfile) userProfile)
					.getBrokerDealerFirmIds(), approvingFirmNFACode);
		case FirmRep:
			return containsApprovingFirm(((BDExternalUserProfile) userProfile)
					.getBrokerDealerFirmIds(), approvingFirmNFACode);
		case BasicFinancialRep:
			return containsApprovingFirm(((BDExternalUserProfile) userProfile)
					.getBrokerDealerFirmIds(), approvingFirmNFACode);
		default:
			return false;
		}
	}
}
