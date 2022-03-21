package com.manulife.pension.bd.web.bob.investment;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.investment.AbstractIPSAndReviewDetailsController;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * Base Class for IPS Review Actions
 * 
 * @author KArthik
 *
 */
public abstract class BaseIPSAndReviewDetailsController extends AbstractIPSAndReviewDetailsController {

	@Override
	protected String preExecute(ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
	
		/*UserProfile userProfile = SessionHelper.getUserProfile(request);
		Contract contract = userProfile.getCurrentContract();*/
		
		BobContext bob = BDSessionHelper.getBobContext(request) ;
		int contract = bob.getCurrentContract().getContractNumber();
		
		// if there're no current contract forward to home page
		if (contract == 0) {
			//return mapping.findForward(BDContentConstants.HOMEPAGE_FINDER_FORWARD);
			return BDContentConstants.HOMEPAGE_FINDER_FORWARD;
		}
		
		return super.preExecute( form, request, response);
	}
	
	/**
	 * Return the User Id Type
	 * 
	 * @param userProfile
	 * @return
	 */
	protected String getGetUserIdType(BDUserProfile userProfile) {
		String userIdType = "";

		if (userProfile.getRole().getRoleType().isInternal()) {
			userIdType = BDConstants.INTERNAL_USER_ID_TYPE;
		} else {
			userIdType = BDConstants.EXTERNAL_USER_ID_TYPE;
		}

		return userIdType;
	}

	/**
	 * Return the first name and last name of the last modified user
	 * 
	 * @param userId
	 * @param userIdType
	 * @return
	 * @throws SystemException
	 */
	protected UserInfo getLastChangedUserDetail(String userId, String userIdType)
			throws SystemException {
		UserInfo userInfo = null;
		if (userId != null) {
			userInfo = SecurityServiceDelegate.getInstance()
					.getUserProfileByProfileId(Long.parseLong(userId));
		}
		return userInfo;
	}

	/**
	 * Removes the IPS review results form from the session
	 * 
	 * @param request
	 */
	protected void removeIPSResultsFormFromSession(HttpServletRequest request) {
		request.getSession().removeAttribute("iPSReviewResultsForm");
	}
}
