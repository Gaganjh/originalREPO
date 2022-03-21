package com.manulife.pension.bd.web.usermanagement;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manulife.pension.bd.web.mimic.StartMimicController;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.delegate.BDUserManagementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.exception.SecurityServiceException;

/**
 * Super class for all user management action. Implements some common action
 * handling:
 * 
 * @author guweigu
 * 
 */
abstract public class AbstractUserManagementController extends BaseAutoController {
	protected static final String Back = "back";
	protected static final String Search = "search";
	protected static final String PasscodeView = "passcodeView";
	protected static final String View = "view";
	protected static final String ResendSuccess = "resendSuccess";
	protected static final String UserManagementException = "exception";
	protected static final String ResetPassword = "resetPassword";
	protected static final String UnlockPasscode = "passcodeView";
	protected static final String PasscodeExemption = "exemptPasscode";
	protected static final String RemovePasscodeExempt = "removePasscodeExempt";
	
	@SuppressWarnings("unchecked")
	protected AbstractUserManagementController(Class clazz) {
		super(clazz);
	}

	/**
	 * Get the user profile from session and set up the form
	 * 
	 * @param request
	 * @param actionForm
	 * @return true if get the user profile from session successfully false
	 *         otherwise
	 */
	abstract protected boolean setupForm(HttpServletRequest request,
			ActionForm actionForm, boolean isUpdate);

	/**
	 * Handle cancel action, which redirect to search/select page
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doCancel(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		UserManagementContext.clearContext(request);
		request.getSession().removeAttribute("manageBasicBrokerForm");
		return Back;
	}

	/**
	 * Handle reset password action
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doResetPassword(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		if (!setupForm(request, actionForm, false)) {
			UserManagementContext.clearContext(request);
			return Back;
		}
		AbstractUserManagementForm form = (AbstractUserManagementForm) actionForm;
		if (form.isResetPasswordAllowed()) {
			// call it again to make sure it is not deleted
			BDPrincipal principal = BDSessionHelper.getUserProfile(request)
					.getBDPrincipal();

			try {
				UserManagementContext.setCurrentUser(request, principal, form
						.getExtUserProfile().getProfileId());
			} catch (SecurityServiceException e) {
				return handleSecurityServiceException( request, e);
			}

			return ResetPassword;
		} else {
			return Back;
		}
	}

	
	/**
	 * Handle Passcode view action
	 * 
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doPasscodeView(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		if (!setupForm(request, actionForm, false)) {
			UserManagementContext.clearContext(request);
			return Back;
		}
		AbstractUserManagementForm form = (AbstractUserManagementForm) actionForm;
		if (form.isPasscodeViewAllowed()) {
			// call it again to make sure it is not deleted
			BDPrincipal principal = BDSessionHelper.getUserProfile(request)
					.getBDPrincipal();

			try {
				UserManagementContext.setCurrentUser(request, principal, form
						.getExtUserProfile().getProfileId());
			} catch (SecurityServiceException e) {
				return handleSecurityServiceException( request, e);
			}

			return PasscodeView;
		} else {
			return Back;
		}
	}
	
	/**
	 * Handle Unlock passcode action
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doUnlockPasscode(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		if (!setupForm(request, actionForm, false)) {
			UserManagementContext.clearContext(request);
			return Back;
		}
		AbstractUserManagementForm form = (AbstractUserManagementForm) actionForm;
		if (form.isPasscodeViewAllowed()) {
			// call it again to make sure it is not deleted
			BDPrincipal principal = BDSessionHelper.getUserProfile(request)
					.getBDPrincipal();

			try {
				UserManagementContext.setCurrentUser(request, principal, form
						.getExtUserProfile().getProfileId());
			} catch (SecurityServiceException e) {
				return handleSecurityServiceException( request, e);
			}

			return UnlockPasscode;
		} else {
			return Back;
		}
	}
	
	/**
	 * Handle passcode exemption.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doExemptPasscode(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		if (!setupForm(request, actionForm, false)) {
			UserManagementContext.clearContext(request);
			return Back;
		}
		AbstractUserManagementForm form = (AbstractUserManagementForm) actionForm;
		if (form.isPasscodeExemptionAllowed()) {
			// call it again to make sure it is not deleted
			BDPrincipal principal = BDSessionHelper.getUserProfile(request)
					.getBDPrincipal();

			try {
				UserManagementContext.setCurrentUser(request, principal, form
						.getExtUserProfile().getProfileId());
			} catch (SecurityServiceException e) {
				return handleSecurityServiceException( request, e);
			}

			return PasscodeExemption;
		} else {
			return Back;
		}
	}
	
	
	/**
	 * Handle passcode exemption removal.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doRemoveExemptPasscode(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		if (!setupForm(request, actionForm, false)) {
			UserManagementContext.clearContext(request);
			return Back;
		}
		AbstractUserManagementForm form = (AbstractUserManagementForm) actionForm;
		if (form.isPasscodeExemptionAllowed()) {
			// call it again to make sure it is not deleted
			BDPrincipal principal = BDSessionHelper.getUserProfile(request)
					.getBDPrincipal();

			try {
				UserManagementContext.setCurrentUser(request, principal, form
						.getExtUserProfile().getProfileId());
			} catch (SecurityServiceException e) {
				return handleSecurityServiceException( request, e);
			}

			return RemovePasscodeExempt;
		} else {
			return Back;
		}
	}
		
	/**
	 * Mimic action
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doMimic(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		if (!setupForm(request, actionForm, false)) {
			return Back;
		}
		AbstractUserManagementForm form = (AbstractUserManagementForm) actionForm;
		if (form.isMimicAllowed()) {
			ControllerRedirect mimicRedirect = new ControllerRedirect(
					URLConstants.StartMimic);
			request.setAttribute(StartMimicController.PARAM_USER_PROFILE_ID,
					form.getExtUserProfile().getProfileId());
			mimicRedirect.addParameter(StartMimicController.PARAM_USER_PROFILE_ID,
					form.getExtUserProfile().getProfileId());
			return mimicRedirect.getPath();
		} else {
			UserManagementContext.clearContext(request);
			return Back;
		}
	}

	/**
	 * Action to delete the user
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doDelete(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		if (!setupForm(request, actionForm, false)) {
			return Back;
		}
		AbstractUserManagementForm form = (AbstractUserManagementForm) actionForm;
		if (!form.isDeleteAllowed()) {
			return Back;
		}

		try {
			BDUserManagementServiceDelegate.getInstance().deleteUser(
					BDSessionHelper.getUserProfile(request).getBDPrincipal(),
					form.getExtUserProfile().getProfileId());
			// successfully delete go back to fresh search
			return Search;
		} catch (SecurityServiceException e) {
			return handleSecurityServiceException( request, e);			
		}
	}

	protected String getView(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		return View;
	}

	protected String handleSecurityServiceException(
			HttpServletRequest request,
			SecurityServiceException e) throws IOException, ServletException,
			SystemException {
		UserManagementHelper.setUserManagementException(request, e);
		return UserManagementException;
	}
}
