package com.manulife.pension.bd.web.usermanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.delegate.BDUserManagementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.BDUserProfileStatus;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerAssistantUserProfile;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

@Controller
@RequestMapping(value = "/manage")

public class BrokerAssistantUserManagementController extends AbstractUserManagementController {
	@ModelAttribute("manageAssistantForm")
	public BrokerAssistantUserManagementForm populateForm() {
		return new BrokerAssistantUserManagementForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("exception", "redirect:/do/usermanagement/search?task=exception");
		forwards.put("back", "redirect:/do/usermanagement/search?task=refresh");
		forwards.put("search", "redirect:/do/usermanagement/search");
		forwards.put("broker", "redirect:/do/manage/broker");
		forwards.put("resendSuccess", "redirect:/do/manage/assistant?resendSuccess=y");
		forwards.put("resetPassword", "redirect:/do/usermanagement/resetPassword");
		forwards.put("passcodeView", "redirect:/do/usermanagement/passcodeView");
		forwards.put("exemptPasscode", "redirect:/do/usermanagement/passcodeExemption");
		forwards.put("preRegister", "/usermanagement/assistantPreReg.jsp");
		forwards.put("postRegister", "/usermanagement/assistantPostReg.jsp");
		forwards.put("fail", "redirect:/do/manage/assistant");
	}

	public BrokerAssistantUserManagementController() {
		super(BrokerAssistantUserManagementController.class);
	}

	@RequestMapping(value = "/assistant", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("manageAssistantForm") BrokerAssistantUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");// if input forward not //available,
											// provided default
			}
		}
		if (!setupForm(request, actionForm, false)) {
			return forwards.get(Back);
		}
		return findForward(actionForm);
	}

	/**
	 * Update and resend activation
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

	@RequestMapping(value = "/assistant", params = { "action=resendActivation" }, method = { RequestMethod.POST })
	public String doResendActivation(
			@Valid @ModelAttribute("manageAssistantForm") BrokerAssistantUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");// if input forward not //available,
											// provided default
			}
		}
		if (!setupForm(request, actionForm, false)) {
			return forwards.get(Back);
		}

		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		try {
			BDPrincipal internalUser = BDSessionHelper.getUserProfile(request).getBDPrincipal();
			long assistantProfileId = actionForm.getAssistantProfile().getProfileId();
			BDUserManagementServiceDelegate.getInstance().resendAssistantInvitation(internalUser,
					actionForm.getAssistantProfile().getProfileId());
			// reload the assistant profile
			UserManagementContext.setCurrentUser(request, internalUser, assistantProfileId);
			setupForm(request, actionForm, false);
			return forwards.get(ResendSuccess);
		} catch (SecurityServiceException e) {
			if (UserManagementHelper.isManagementConflictException(e)) {
				return handleSecurityServiceException(request, e);
			}
			errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e)));
			actionForm.setUpdateSuccess(false);
			setErrorsInRequest(request, errors);
		}
		return findForward((BrokerAssistantUserManagementForm) actionForm);
	}

	@RequestMapping(value = "/assistant", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(@Valid @ModelAttribute("manageAssistantForm") BrokerAssistantUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");// if input forward not //available,
											// provided default
			}
		}
		return forwards.get(super.doCancel(actionForm, request, response));
	}

	/**
	 * This method is used to handle the changes by RUM user
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
	@RequestMapping(value = "/assistant", params = { "action=save" }, method = { RequestMethod.POST })
	public String doSave(@Valid @ModelAttribute("manageAssistantForm") BrokerAssistantUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");// if input forward not //available,
											// provided default
			}
		}

		if (!setupForm(request, actionForm, true)) {
			return forwards.get(Back);
		}
		BrokerAssistantUserManagementForm form = (BrokerAssistantUserManagementForm) actionForm;

		form.setFirms(
				BDWebCommonUtils.getRiaFirmsWithPermission(form.getFirmListStr(), form.getFirmPermissionsListStr()));

		if (!form.isUpdateAllowed()) {
			return forwards.get(Back);
		}

		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		if (StringUtils.isNotEmpty(form.getFirmPermissionsListStr())) {
			String permissionCheck = form.getFirmPermissionsListStr();
			String[] buf = permissionCheck.split(",");
			if (buf.length > 20) {
				errors.add(new ValidationError(BrokerAssistantUserManagementForm.FIELD_FIRMS,
						BDErrorCodes.USER_MANAGEMENT_VIEW_RIA_FIRM_PERMISSION_EXCEEDED_BROKER));
			}
		}
		if (errors.isEmpty()) {

			try {
				BDPrincipal principal = BDSessionHelper.getUserProfile(request).getBDPrincipal();
				long userProfileId = form.getAssistantProfile().getProfileId();
				BDUserManagementServiceDelegate.getInstance().updateRiaFirmDetails(principal, userProfileId,
						form.getFirms());
				form.setUpdateSuccess(true);
				form.setChanged(false);
			} catch (SecurityServiceException e) {
				if (UserManagementHelper.isManagementConflictException(e)) {
					return handleSecurityServiceException(request, e);
				}
			}
		}
		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			form.setChanged(true);
		}
		return findForward((BrokerAssistantUserManagementForm) actionForm);
	}

	@RequestMapping(value = "/assistant", params = { "action=mimic" }, method = { RequestMethod.POST })
	public String doMimic(@Valid @ModelAttribute("manageAssistantForm") BrokerAssistantUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");// if input forward not //available,
											// provided default
			}
		}
		String forward = doMimic(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/assistant", params = { "action=exemptPasscode" }, method = { RequestMethod.POST })
	public String doExemptPasscode(
			@Valid @ModelAttribute("manageAssistantForm") BrokerAssistantUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");// if input forward not //available,
											// provided default
			}
		}
		return forwards.get(super.doExemptPasscode(actionForm, request, response));
	}

	@RequestMapping(value = "/assistant", params = { "action=passcodeView" }, method = { RequestMethod.POST })
	public String doPasscodeView(
			@Valid @ModelAttribute("manageAssistantForm") BrokerAssistantUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");// if input forward not //available,
											// provided default
			}
		}
		return forwards.get(doPasscodeView(actionForm, request, response));
	}

	@RequestMapping(value = "/assistant", params = { "action=resetPassword" }, method = { RequestMethod.POST })
	public String doResetPassword(
			@Valid @ModelAttribute("manageAssistantForm") BrokerAssistantUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");// if input forward not //available,
											// provided default
			}
		}
		return forwards.get(doResetPassword(actionForm, request, response));
	}

	@RequestMapping(value = "/assistant", params = { "action=delete" }, method = { RequestMethod.POST })
	public String doDelete(@Valid @ModelAttribute("manageAssistantForm") BrokerAssistantUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");// if input forward not //available,
											// provided default
			}
		}
		return forwards.get(doDelete(actionForm, request, response));
	}

	/**
	 * delete the profile
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
	@RequestMapping(value = "/assistant", params = { "action=selectBroker" }, method = { RequestMethod.GET,RequestMethod.POST })
	public String doSelectBroker(
			@Valid @ModelAttribute("manageAssistantForm") BrokerAssistantUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");// if input forward not //available,
											// provided default
			}
		}

		if (!setupForm(request, actionForm, false)) {
			return forwards.get(Back);
		}
		BrokerAssistantUserManagementForm form = (BrokerAssistantUserManagementForm) actionForm;

		try {
			UserManagementContext.setCurrentUser(request, BDSessionHelper.getUserProfile(request).getBDPrincipal(),
					form.getAssistantProfile().getParentBroker().getProfileId());
		} catch (SecurityServiceException e) {
			if (UserManagementHelper.isManagementConflictException(e)) {
				return handleSecurityServiceException(request, e);
			}

			List<ValidationError> errors = new ArrayList<ValidationError>(1);
			errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e)));
			form.setUpdateSuccess(false);
			setErrorsInRequest(request, errors);
			return findForward(form);
		}

		return forwards.get("broker");
	}

	/**
	 * Find the forward based on the assistant profile
	 * 
	 * @param mapping
	 * @param form
	 * @return
	 */
	private String findForward(BrokerAssistantUserManagementForm form) {
		ExtendedBrokerAssistantUserProfile profile = form.getAssistantProfile();
		if (profile == null) {
			return forwards.get("back");
		} else {
			BDUserProfileStatus profileStatus = profile.getProfileStatus();
			if (profileStatus.compareTo(BDUserProfileStatus.New) == 0) {
				return forwards.get("preRegister");
			} else {
				return forwards.get("postRegister");
			}
		}
	}

	protected boolean setupForm(HttpServletRequest request, ActionForm actionForm, boolean isUpdate) {
		ExtendedBDExtUserProfile managedUserProfile = UserManagementContext.getContext(request).getManagedUserProfile();
		BrokerAssistantUserManagementForm form = (BrokerAssistantUserManagementForm) actionForm;
		form.setInternalUserRole(request);
		if (managedUserProfile == null
				|| managedUserProfile.getRoleType().compareTo(BDUserRoleType.FinancialRepAssistant) != 0) {
			// this should not happen unless using book mark
			logger.error("The selected user is not a Broker Assistant User, userProfileId = "
					+ (managedUserProfile == null ? "" : managedUserProfile.getProfileId()));
			form.setAssistantProfile(null);
			return false;
		} else {
			form.setAssistantProfile((ExtendedBrokerAssistantUserProfile) managedUserProfile);
			form.setFirms(((ExtendedBrokerAssistantUserProfile) managedUserProfile).getRiaFirms());
			UserManagementHelper.sortFirmsById(form.getFirms());
			if (StringUtils.isNotEmpty(request.getParameter("resendSuccess"))) {
				form.setResendActivationSuccess(true);
				form.setEnableResend(false);
			}
			return true;
		}
	}

	@Override
	protected String getView(AutoForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		return findForward((BrokerAssistantUserManagementForm) actionForm);
	}

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */
	@Autowired
	private BDValidatorFWFail bdValidatorFWFail;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWFail);
	}

}
