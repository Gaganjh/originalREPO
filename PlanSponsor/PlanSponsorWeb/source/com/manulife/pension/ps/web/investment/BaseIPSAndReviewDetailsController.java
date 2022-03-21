package com.manulife.pension.ps.web.investment;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.investment.AbstractIPSAndReviewDetailsController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWBaseIPSDefault;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * Base Class for IPS Review Actions
 * 
 * @author KArthik
 *
 */
public abstract class BaseIPSAndReviewDetailsController extends AbstractIPSAndReviewDetailsController {

	@Override
	protected String preExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
	
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		Contract contract = userProfile.getCurrentContract();
		
		// if there're no current contract forward to home page
		if (contract == null) {
			//return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD);
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		return super.preExecute( form, request, response);
	}
	
	/**
	 * Return the User Id Type
	 * 
	 * @param userProfile
	 * @return
	 */
	protected String getGetUserIdType(UserProfile userProfile) {
		String userIdType = "";

		if(userProfile.getPrincipal().getRole().isExternalUser()) {
			userIdType = Constants.EXTERNAL_USER_ID_TYPE;
		} else {
			userIdType = Constants.INTERNAL_USER_ID_TYPE;
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
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
/*	@SuppressWarnings("rawtypes")
	protected Collection doValidate( Form form,
					HttpServletRequest request) {
			Collection penErrors = PsValidation.doValidatePenTestAutoAction(form,
						mapping, request, CommonConstants.DEFAULT);
			if (penErrors != null && penErrors.size() > 0) {
                request.removeAttribute(PsBaseAction.ERROR_KEY);
                String str= mapping.getPath();
             if(StringUtils.equals(str,"/investment/ipsManager/")){
                       // Populate the form bean with necessary data for display
                IPSAndReviewForm ipsRevievForm = (IPSAndReviewForm) form;
                ipsRevievForm.setMode(VIEW_MODE);
             }
             return penErrors;
			}
			return super.doValidate( form, request);
	}*/
	@Autowired
    private PSValidatorFWBaseIPSDefault psValidatorFWBaseIPSDefault;  

	@InitBinder
	protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWBaseIPSDefault);
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
