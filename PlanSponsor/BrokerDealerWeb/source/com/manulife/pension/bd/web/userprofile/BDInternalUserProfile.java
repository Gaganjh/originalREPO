package com.manulife.pension.bd.web.userprofile;

import com.manulife.pension.service.broker.valueobject.NFACodeConstants;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.bd.UserManagementAccessRules;
import com.manulife.pension.service.security.bd.UserManagementAccessRules.UsermanagementOperation;
import com.manulife.pension.service.security.role.BDInternalUser;

/**
 * Internal UserProfile can mimic an External User Profile
 * 
 * @author guweigu
 * 
 */
public class BDInternalUserProfile extends BDUserProfile {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BDExternalUserProfile mimicUserProfile;

	@Override
	public void setBDPrincipal(BDPrincipal principal) throws RuntimeException {
		if (principal != null
				&& !(principal.getBDUserRole() instanceof BDInternalUser)) {
			throw new RuntimeException("Can only have Internal user");
		}
		super.setBDPrincipal(principal);
	}

	@Override
	public boolean canMimic(BDUserProfile user) {
		return UserManagementAccessRules.getInstance().isOperationAllowed(
				this.getBDPrincipal().getBDUserRole().getRoleType(),
				user.getBDPrincipal().getBDUserRole().getRoleType(),
				UsermanagementOperation.Mimic);
	}

	@Override
	public boolean isInMimic() {
		return mimicUserProfile != null;
	}

	@Override
	public boolean isInternalUser() {
		return true;
	}

	@Override
	public String getAssociatedApprovingFirmCode() {
		return NFACodeConstants.Independent;
	}
}
