package com.manulife.pension.bd.web.userprofile;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.role.BDExternalUser;

public class BDAssistantUserProfile extends BDExternalUserProfile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BDPrincipal parentPrincipal;

	public BDPrincipal getParentPrincipal() {
		return parentPrincipal;
	}

	public void setParentPrincipal(BDPrincipal parentPrincipal) {
		this.parentPrincipal = parentPrincipal;
	}

	@Override
	/*
	 * Assistant should return the parent user's bd firms
	 */
	public List<Long> getBrokerDealerFirmIds() {
		return parentPrincipal == null ? new ArrayList<Long>(0)
				: ((BDExternalUser) parentPrincipal.getBDUserRole())
						.getBrokerDealerFirmIds();
	}

}
