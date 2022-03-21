package com.manulife.pension.bd.web.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.LoginStatus;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.valueobject.BDUserProfileValueObject;
import com.manulife.pension.util.content.GenericException;

/**
 * Broker Dealer Website's session utility class
 * 
 * @author Wei Gu
 */
public class BDSessionHelper extends BaseSessionHelper {
	//protected static final String ERROR_KEY = CommonEnvironment.getInstance()
		//	.getErrorKey();

	/**
	 * Constructor.
	 * 
	 */
	private BDSessionHelper() {
		super();
	}

	/**
	 * Returns the user profile associated with the given request.
	 * 
	 * @param request
	 *            The request object.
	 * @return The user profile object associated with the request (or null if
	 *         none is found).
	 */
	public static BDUserProfile getUserProfile(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		BDUserProfile userProfile = null;
		if (session != null) {
			userProfile = (BDUserProfile) session.getAttribute(BDConstants.USERPROFILE_KEY);
		}
		return userProfile; 
	}
	
	/**
	 * Returns the user profile associated with the given request.
	 * This invalidates the session and removes JSESSIONID cookie if the user profile is not found.
	 * 
	 * @param request
	 *            The request object.
	 * @return The user profile object associated with the request (or null if
	 *         none is found).
	 */
	public static BDUserProfile getUserProfile(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		BDUserProfile userProfile = null;
		if (session != null) {
			userProfile = (BDUserProfile) session.getAttribute(BDConstants.USERPROFILE_KEY);
			if (userProfile == null) {
				BDSessionHelper.invalidateSession(request, response);
			}
		}
		return userProfile; 
	}

	/**
	 * Returns the BOB Context associated with the given request.
	 * 
	 * @param request
	 *            The request object.
	 * @return The BOBContext object associated with the request (or null if
	 *         none is found).
	 */
	public static BobContext getBobContext(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return session == null ? null : (BobContext) session
				.getAttribute(BDConstants.BOBCONTEXT_KEY);
	}

	/**
     * Sets the BOB Context into the Session.
     * 
     * @param request - The request object.
     * @param bobContext - The BOBContext object to be set into the session.
     */
    public static void setBobContext(HttpServletRequest request, BobContext bobContext) {
        request.getSession(false).setAttribute(BDConstants.BOBCONTEXT_KEY, bobContext);
    }

	/**
	 * This checks if any warnings already present in the session and adds the
	 * current warning message to the existing messages.
	 * 
	 * @param request
	 * @param warnings
	 */
	@SuppressWarnings("unchecked")
	public static void setWarningsInSession(final HttpServletRequest request,
			final Collection<GenericException> warnings) {
		if (warnings != null) {
			// check for warnings already in session scope
			Collection<GenericException> existingWarnings = (Collection<GenericException>) request
					.getSession(false).getAttribute(
							BDConstants.WARNING_MESSAGES);
			if (existingWarnings != null) {
				warnings.addAll(existingWarnings);
				request.getSession(false).removeAttribute(
						BDConstants.WARNING_MESSAGES);
			}

			request.getSession(false).setAttribute(
					BDConstants.WARNING_MESSAGES, warnings);
		}
	}

	/**
	 * This checks if any infromational messages already present in the session
	 * and adds the current message to the existing messages.
	 * 
	 * @param request
	 * @param messages
	 */
	@SuppressWarnings("unchecked")
	public static void setMessagesInSession(final HttpServletRequest request,
			final Collection<GenericException> messages) {
		if (messages != null) {
			// check for messages already in session scope
			Collection<GenericException> existingMessages = (Collection<GenericException>) request
					.getSession(false).getAttribute(BDConstants.INFO_MESSAGES);
			if (existingMessages != null) {
				messages.addAll(existingMessages);
				request.getSession(false).removeAttribute(
						BDConstants.INFO_MESSAGES);
			}

			request.getSession(false).setAttribute(BDConstants.INFO_MESSAGES,
					messages);
		}
	}

	/**
	 * Check if the session is in secured or public mode
	 * 
	 * @param request
	 *            Use ServletRequest because WAS tag file uses ServletRequest
	 *            instead of HttpServletRequest
	 * @return
	 */
	public static boolean isInSecuredMode(final ServletRequest servletRequest) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		BDUserProfile profile = BDSessionHelper.getUserProfile(request);
		return (profile != null && LoginStatus.FullyLogin.equals(profile
				.getLoginStatus()));
	}

	/**
	 * Move the error message from session into request
	 * @param request
	 */
	public static void moveMessageIntoRequest(final HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Collection<GenericException> errors = new ArrayList<GenericException>();
        List<ObjectError>  errorList =new ArrayList<ObjectError>();
		if (session == null) {
			return;
		} else {
			
			Object obj = request.getSession(false).getAttribute("psErrors");
			Collection<GenericException> sessionErrors=null;
			if(obj instanceof Collection){
				sessionErrors = getErrorsInSession(request);
				if (sessionErrors!= null && !sessionErrors.isEmpty()) {
				errors.addAll(sessionErrors);}
			} else if ( obj != null && obj instanceof BeanPropertyBindingResult){
				errorList=((BeanPropertyBindingResult) obj).getAllErrors();
					
			}
			obj =  request.getAttribute(BdBaseController.ERROR_KEY);
			
			
			if(obj instanceof Collection){
				sessionErrors = (Collection<GenericException>) obj;
				if (sessionErrors!= null && !sessionErrors.isEmpty()) {
				errors.addAll(sessionErrors);}
			} else if ( obj != null && obj instanceof BeanPropertyBindingResult){
				
				errorList =((BeanPropertyBindingResult) obj).getAllErrors();
				
			}
			if(errorList!=null  && errorList.size()>0){
				request.setAttribute(BdBaseController.ERROR_KEY, errorList);
				removeErrorsInSession(request);
			}
			if(errors!=null && errors.size()>0){
			request.setAttribute(BdBaseController.ERROR_KEY, errors);
			removeErrorsInSession(request);
			}
		} 
	}
	
    /**
     * The tab remembrance which is selected in the BOB screen, will be removed. 
     *
     * @param HttpServletRequest request
     */
	public static void removeBOBTabSelectionFromSession(HttpServletRequest request){
    	
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		} else {
			HttpSession hs = request.getSession();
    	hs.removeAttribute(FapConstants.ATTR_TAB_SELECTED);
		}
    }
	
	public static void setShowPasswordMeterForFRW(HttpServletRequest request, String value) {
		request.getSession(false).setAttribute(BDConstants.FRW_SHOW_PASSWORD_METER_IND, value);
	}
	
	public static boolean isExternalUser(BDUserProfileValueObject profileVO){
		boolean isExternalUser = ((StringUtils.equals(profileVO.getRoleCode(),
					BDUserRoleType.BasicFinancialRep.getUserRoleCode()))
        		|| (StringUtils.equals(profileVO.getRoleCode(),
     					BDUserRoleType.FinancialRep.getUserRoleCode()))
        		|| (StringUtils.equals(profileVO.getRoleCode(),
     					BDUserRoleType.FirmRep.getUserRoleCode()))
        		|| (StringUtils.equals(profileVO.getRoleCode(),
     					BDUserRoleType.FinancialRepAssistant.getUserRoleCode()))
        		|| (StringUtils.equals(profileVO.getRoleCode(),
     					BDUserRoleType.RIAUser.getUserRoleCode())));
		return isExternalUser;
	}
	
	public static boolean isInternalUser(BDUserProfileValueObject profileVO){
		
		boolean isInternalUser = ((StringUtils.equals(profileVO.getRoleCode(),
				BDUserRoleType.CAR.getUserRoleCode()))
    		|| (StringUtils.equals(profileVO.getRoleCode(),
 					BDUserRoleType.SuperCAR.getUserRoleCode()))
    		|| (StringUtils.equals(profileVO.getRoleCode(),
 					BDUserRoleType.InternalBasic.getUserRoleCode()))
    		|| (StringUtils.equals(profileVO.getRoleCode(),
 					BDUserRoleType.Administrator.getUserRoleCode()))
    		|| (StringUtils.equals(profileVO.getRoleCode(),
 					BDUserRoleType.ContentManager.getUserRoleCode()))
    		|| (StringUtils.equals(profileVO.getRoleCode(),
 					BDUserRoleType.NationalAccounts.getUserRoleCode()))
    		|| (StringUtils.equals(profileVO.getRoleCode(),
 					BDUserRoleType.RIAUserManager.getUserRoleCode()))
    		|| (StringUtils.equals(profileVO.getRoleCode(),
 		 					BDUserRoleType.RVP.getUserRoleCode()))
    		);
		return isInternalUser;
		
	}
}