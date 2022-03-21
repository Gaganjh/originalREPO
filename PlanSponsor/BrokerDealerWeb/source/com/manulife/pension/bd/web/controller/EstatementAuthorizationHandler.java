/**
 * 
 */
package com.manulife.pension.bd.web.controller;

import java.util.List;

import com.manulife.pension.bd.web.userprofile.BDExternalUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.BaseUserProfile;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.security.BDUserRoleType;

/**
 * @author narintr
 * 
 */
public class EstatementAuthorizationHandler extends BDAuthorizationHandler {

	@Override
	public boolean isUserAuthorized(AuthorizationSubject subject, String url)
			throws SystemException {

		BaseUserProfile user = subject.getUserProfile();
		long userProfileId = user.getAbstractPrincipal().getProfileId();
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
		BDUserProfile loggedinUserProfile =  ((BDAuthorizationSubject)subject).getLoggedinUserProfile();
		
		if( bdUser.isInMimic() && loggedinUserProfile!= null && BDUserRoleType.RVP.equals(loggedinUserProfile.getBDPrincipal().getBDUserRole().getRoleType())){
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

		if (bdUser instanceof BDExternalUserProfile) {
			List<BrokerDealerFirm> riaFirms = BDWebCommonUtils.getRIAUserFirmList(userProfileId);
			if (riaFirms == null || riaFirms.isEmpty()) {
				return false;
			} else {
				boolean hasPermission = false;
				for(BrokerDealerFirm firm : riaFirms){
					if(firm.isFirmPermission()){
						hasPermission =  true;
					}
				}
				if(!hasPermission){
					return false;
				}
			}
		}
		return true;
	}

}
