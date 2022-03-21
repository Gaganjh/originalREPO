package com.manulife.pension.bd.web.usermanagement;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;

import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.BDUserProfileStatus;
import com.manulife.pension.service.security.bd.UserManagementAccessRules;
import com.manulife.pension.service.security.bd.UserManagementAccessRules.UsermanagementOperation;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerAssistantUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerUserProfile;

/**
 * Defines common utility method for all user management action
 * 
 * @author guweigu
 * 
 */
abstract public class AbstractUserManagementForm extends AutoForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected BDUserRoleType internalUserRole;

	private boolean resendActivationSuccess = false;

	private boolean updateSuccess = false;

	private boolean changed = false;
    
    private boolean enableResend = true;

	/**
	 * Get current external user profile this form contains
	 * 
	 * @return
	 */
	abstract protected ExtendedBDExtUserProfile getExtUserProfile();

	/**
	 * Return the display string for password status
	 * 
	 * @return
	 */
	public String getPasswordStatus() {
		ExtendedBDExtUserProfile profile = getExtUserProfile();
		if (profile != null) {
			return UserManagementHelper.getPasswordStatusMap().get(
					profile.getPasswordStatus());
		} else {
			return "";
		}
	}

	/**
	 * Returns whether the mimic is allowed for the user
	 * 
	 * @param internalUserRole
	 * @return
	 */
	public boolean isMimicAllowed() {
		BDUserProfileStatus profileStatus = getExtUserProfile()
				.getProfileStatus();
		if (profileStatus == null
				|| profileStatus.compareTo(BDUserProfileStatus.Registered) != 0) {
			return false;
		}

		if (getExtUserProfile().getRoleType().compareTo(
				BDUserRoleType.FinancialRepAssistant) == 0) {
			ExtendedBrokerUserProfile broker = ((ExtendedBrokerAssistantUserProfile) getExtUserProfile())
					.getParentBroker();
			if (broker == null
					|| CollectionUtils
							.isEmpty(broker.getActiveBrokerEntities())) {
				return false;
			}
		} else if (getExtUserProfile().getRoleType().compareTo(
				BDUserRoleType.FinancialRep) == 0) {
			ExtendedBrokerUserProfile broker = ((ExtendedBrokerUserProfile) getExtUserProfile());
			if (CollectionUtils.isEmpty(broker.getActiveBrokerEntities())) {
				return false;
			}
		}

		return UserManagementAccessRules.getInstance().isOperationAllowed(
				internalUserRole, getExtUserProfile().getRoleType(),
				UsermanagementOperation.Mimic);
	}

	/**
	 * Whether the user profile is activated
	 * @return
	 */
	public boolean isProfileActivated() {
		ExtendedBDExtUserProfile userProfile = getExtUserProfile();
		if ( userProfile != null) {
			BDUserProfileStatus profileStatus = userProfile
					.getProfileStatus();
			if (profileStatus != null
					&& profileStatus.compareTo(BDUserProfileStatus.Registered) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns whether the reset password is allowed for the user
	 * 
	 * @param internalUserRole
	 * @return
	 */
	public boolean isResetPasswordAllowed() {
		BDUserProfileStatus profileStatus = getExtUserProfile()
				.getProfileStatus();
		if (profileStatus != null
				&& profileStatus.compareTo(BDUserProfileStatus.Registered) == 0) {
			return UserManagementAccessRules.getInstance().isOperationAllowed(
					internalUserRole, getExtUserProfile().getRoleType(),
					UsermanagementOperation.ResetPassword);
		}
		return false;
	}
	
	/**
	 * Returns whether the unlock stepup passcode is  allowed for the user
	 * 
	 * @param internalUserRole
	 * @return
	 */
	public boolean isPasscodeViewAllowed() {
		BDUserProfileStatus profileStatus = getExtUserProfile()
				.getProfileStatus();
		if (profileStatus != null
				&& profileStatus.compareTo(BDUserProfileStatus.Registered) == 0) {
			return UserManagementAccessRules.getInstance().isOperationAllowed(
					internalUserRole, getExtUserProfile().getRoleType(),
					UsermanagementOperation.UnlockPasscode);
		}
		return false;
	}
	
	/**
	 * Returns whether the unlock  passcode exemption is  allowed for the user
	 * 
	 * @param internalUserRole
	 * @return
	 */
	public boolean isPasscodeExemptionAllowed() {
		BDUserProfileStatus profileStatus = getExtUserProfile()
				.getProfileStatus();
		if (profileStatus != null
				&& profileStatus.compareTo(BDUserProfileStatus.Registered) == 0) {
			return UserManagementAccessRules.getInstance().isOperationAllowed(
					internalUserRole, getExtUserProfile().getRoleType(),
					UsermanagementOperation.ExemptPasscode);
		}
		return false;
	}

	/**
	 * Returns whether the reset password is allowed for the user
	 * 
	 * @param internalUserRole
	 * @return
	 */
	public boolean isDeleteAllowed() {
		return UserManagementAccessRules.getInstance().isOperationAllowed(
				internalUserRole, getExtUserProfile().getRoleType(),
				UsermanagementOperation.Delete);
	}

	/**
	 * Returns whether the resend invitation is allowed for the user
	 * 
	 * @param internalUserRole
	 * @return
	 */
	public boolean isResendPartyActivationAllowed() {
		return UserManagementAccessRules.getInstance().isOperationAllowed(
				internalUserRole, getExtUserProfile().getRoleType(),
				UsermanagementOperation.ResendActivation);
	}

	
	/**
	 * Returns whether the resend invitation is allowed for the user
	 * 
	 * @param internalUserRole
	 * @return
	 */
	public boolean isResendInvitationAllowed() {
		BDUserProfileStatus profileStatus = getExtUserProfile()
				.getProfileStatus();
		if (profileStatus != null
				&& profileStatus.compareTo(BDUserProfileStatus.Registered) == 0) {
			return false;
		}
		return UserManagementAccessRules.getInstance().isOperationAllowed(
				internalUserRole, getExtUserProfile().getRoleType(),
				UsermanagementOperation.ResendActivation);
	}

	/**
	 * Returns whether the resend invitation is allowed for the user
	 * 
	 * @param internalUserRole
	 * @return
	 */
	public boolean isUpdateAllowed() {
		return UserManagementAccessRules.getInstance().isOperationAllowed(
				internalUserRole, getExtUserProfile().getRoleType(),
				UsermanagementOperation.Edit);
	}

	public void setInternalUserRole(HttpServletRequest request) {
		internalUserRole = BDSessionHelper.getUserProfile(request)
				.getBDPrincipal().getBDUserRole().getRoleType();
	}

	public boolean isResendActivationSuccess() {
		return resendActivationSuccess;
	}

	public void setResendActivationSuccess(boolean resendActivationSuccess) {
		this.resendActivationSuccess = resendActivationSuccess;
	}

	public boolean isUpdateSuccess() {
		return updateSuccess;
	}

	public void setUpdateSuccess(boolean updateSuccess) {
		this.updateSuccess = updateSuccess;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

    /**
     * Returns a flag to indicate whether to enable the resend button or not
     * 
     * @return the enableResend
     */
    public boolean isEnableResend() {
        return enableResend;
    }

    /**
     * Sets the flag that indicates whether to enable the resend button or not
     * 
     * @param enableResend the enableResend to set
     */
    public void setEnableResend(boolean enableResend) {
        this.enableResend = enableResend;
    }
}
