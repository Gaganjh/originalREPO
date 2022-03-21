package com.manulife.pension.bd.web.userprofile;


import java.util.List;

import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.role.BDExternalUser;


/**
 * Represents a BDExternal user
 * @author guweigu
 *
 */
public class BDExternalUserProfile extends BDUserProfile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setBDPrincipal(BDPrincipal principal) throws RuntimeException {
		if (principal != null && !(principal.getBDUserRole() instanceof BDExternalUser)) {
			throw new RuntimeException("Can only have External user");
		}
		super.setBDPrincipal(principal);
	}
	
	public List<Long> getBrokerDealerFirmIds() {
		BDExternalUser role = (BDExternalUser) super.getRole();
		return role.getBrokerDealerFirmIds();
	}

	@Override
	public boolean isInternalUser() {
		return false;
	}

	@Override
	public String getAssociatedApprovingFirmCode() {
		return ApprovingFirmUtil.getInstance().getApprovingFirmNFACode(
				getRole().getGoverningBDFirmPartyId());
	}
}
