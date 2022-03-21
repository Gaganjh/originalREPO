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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWView;
import com.manulife.pension.delegate.BDUserManagementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.service.broker.valueobject.BrokerEntityExtendedProfile;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.valueobject.BrokerEntityAssoc;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerUserProfile;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

/**
 * User Management action for Broker user. It supports resendActivation,
 * removeBrokerEntity, and select an assistant
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping(value = "/manage")

public class BrokerUserManagementController extends AbstractUserManagementController {
	@ModelAttribute("manageBrokerForm")
	public BrokerUserManagementForm populateForm() {
		return new BrokerUserManagementForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("exception", "redirect:/do/usermanagement/search?task=exception");
		forwards.put("back", "redirect:/do/usermanagement/search?task=refresh");
		forwards.put("search", "redirect:/do/usermanagement/search");
		forwards.put("view", "/usermanagement/broker.jsp");
		forwards.put("resendSuccess", "redirect:/do/manage/broker?resendSuccess=y");
		forwards.put("assistant", "redirect:/do/manage/assistant");
		forwards.put("resetPassword", "redirect:/do/usermanagement/resetPassword");
		forwards.put("passcodeView", "redirect:/do/usermanagement/passcodeView");
		forwards.put("exemptPasscode", "redirect:/do/usermanagement/passcodeExemption");

	}

	public BrokerUserManagementController() {
		super(BrokerUserManagementController.class);
	}

	@RequestMapping(value = "/broker", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("manageBrokerForm") BrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		if (!setupForm(request, actionForm, false)) {
			return forwards.get("back");
		} else {
			return forwards.get("view");
		}
	}
	/**
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
	@RequestMapping(value = "/broker", params = { "action=save" }, method = { RequestMethod.POST })
	public String doSave(@Valid @ModelAttribute("manageBrokerForm") BrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		if (!setupForm(request, actionForm, true)) {
			return forwards.get(Back);
		}
		BrokerUserManagementForm form = (BrokerUserManagementForm) actionForm;

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
				errors.add(new ValidationError(BrokerUserManagementForm.FIELD_FIRMS,
						BDErrorCodes.USER_MANAGEMENT_VIEW_RIA_FIRM_PERMISSION_EXCEEDED_BROKER));
			}
		}
		if (errors.isEmpty()) {

			try {
				BDPrincipal principal = BDSessionHelper.getUserProfile(request).getBDPrincipal();
				long userProfileId = form.getBrokerUserProfile().getProfileId();
				BDUserManagementServiceDelegate.getInstance().updateRiaFirmDetails(principal, userProfileId,
						form.getFirms());
				form.setUpdateBrokerSuccess(true);
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
		return forwards.get(View);
	}
	
	@RequestMapping(value = "/broker", params = { "action=mimic" }, method = { RequestMethod.POST })
	public String doMimic(@Valid @ModelAttribute("manageBrokerForm") BrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		String forward = doMimic(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(value = "/broker", params = { "action=exemptPasscode" }, method = { RequestMethod.POST })
	public String doExemptPasscode(@Valid @ModelAttribute("manageBrokerForm") BrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		return forwards.get(doExemptPasscode(actionForm, request, response));
	}
	
	@RequestMapping(value = "/broker", params = { "action=passcodeView" }, method = { RequestMethod.POST })
	public String doPasscodeView(@Valid @ModelAttribute("manageBrokerForm") BrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		return forwards.get(doPasscodeView(actionForm, request, response));
	}
	
	@RequestMapping(value = "/broker", params = { "action=resetPassword" }, method = { RequestMethod.POST })
	public String doResetPassword(@Valid @ModelAttribute("manageBrokerForm") BrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		return forwards.get(doResetPassword(actionForm, request, response));
	}

	@RequestMapping(value = "/broker", params = { "action=delete" }, method = { RequestMethod.POST })
	public String doDelete(@Valid @ModelAttribute("manageBrokerForm") BrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		return forwards.get(doDelete(actionForm, request, response));
	}

	@RequestMapping(value = "/broker", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(@Valid @ModelAttribute("manageBrokerForm") BrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		return forwards.get(super.doCancel(actionForm, request, response));
	}

	@RequestMapping(value = "/broker", params = { "action=resendActivation" }, method = { RequestMethod.POST })
	public String doResendActivation(@Valid @ModelAttribute("manageBrokerForm") BrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		if (!setupForm(request, actionForm, false)) {
			return forwards.get(Back);
		}
		BrokerUserManagementForm form = (BrokerUserManagementForm) actionForm;
		try {
			BrokerEntityExtendedProfile selectedParty = getSelectedBrokerProfile(form);
			if (selectedParty != null) {
				BDUserManagementServiceDelegate.getInstance().resendUserPartyActivation(
						BDSessionHelper.getUserProfile(request).getBDPrincipal(),
						form.getBrokerUserProfile().getProfileId(), selectedParty);
				ControllerRedirect redirect = new ControllerRedirect((ResendSuccess));
				redirect.addParameter("selectedPartyId", form.getSelectedPartyId());
				return forwards.get("resendSuccess");
			}
		} catch (SecurityServiceException e) {
			if (UserManagementHelper.isManagementConflictException(e)) {
				return handleSecurityServiceException(request, e);
			}

			logger.debug("Fail to resendActivation: partyId = " + form.getSelectedPartyId(), e);
			List<ValidationError> errors = new ArrayList<ValidationError>(1);
			errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e)));
			setErrorsInRequest(request, errors);
		}

		return forwards.get("view");
	}

	@RequestMapping(value = "/broker", params = { "action=removeParty" }, method = { RequestMethod.POST })
	public String doRemoveParty(@Valid @ModelAttribute("manageBrokerForm") BrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		if (!setupForm(request, actionForm, false)) {
			return forwards.get(Back);
		}
		BrokerUserManagementForm form = (BrokerUserManagementForm) actionForm;

		try {
			BrokerEntityExtendedProfile selectedParty = getSelectedBrokerProfile(form);
			if (selectedParty != null) {
				BDUserManagementServiceDelegate.getInstance().removeBrokerEntity(
						BDSessionHelper.getUserProfile(request).getBDPrincipal(),
						form.getBrokerUserProfile().getProfileId(), selectedParty);
				UserManagementContext.setCurrentUser(request, BDSessionHelper.getUserProfile(request).getBDPrincipal(),
						form.getBrokerUserProfile().getProfileId());
				form.setBrokerUserProfile(
						(ExtendedBrokerUserProfile) UserManagementContext.getContext(request).getManagedUserProfile());
				form.setUpdateSuccess(true);
			}
		} catch (SecurityServiceException e) {
			if (UserManagementHelper.isManagementConflictException(e)) {
				return handleSecurityServiceException(request, e);
			}

			logger.debug("Fail to remove: partyId = " + form.getSelectedPartyId(), e);
			List<ValidationError> errors = new ArrayList<ValidationError>(1);
			errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e)));
			setErrorsInRequest(request, errors);
		}

		return forwards.get("view");
	}

	@RequestMapping(value = "/broker", params = { "action=selectAssistant" }, method = {RequestMethod.GET,RequestMethod.POST })
	public String doSelectAssistant(@Valid @ModelAttribute("manageBrokerForm") BrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		if (!setupForm(request, actionForm, false)) {
			return forwards.get(Back);
		}
		BrokerUserManagementForm form = (BrokerUserManagementForm) actionForm;
		try {
			UserManagementContext.setCurrentUser(request, BDSessionHelper.getUserProfile(request).getBDPrincipal(),
					form.getSelectedAssistantId());
		} catch (SecurityServiceException e) {
			if (UserManagementHelper.isManagementConflictException(e)) {
				return handleSecurityServiceException(request, e);
			}
			List<ValidationError> errors = new ArrayList<ValidationError>(1);
			errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e)));
			form.setUpdateSuccess(false);
			setErrorsInRequest(request, errors);
			return forwards.get("view");
		}
		return forwards.get("assistant");
	}

	

	private BrokerEntityExtendedProfile getSelectedBrokerProfile(BrokerUserManagementForm form) {
		for (BrokerEntityAssoc b : form.getBrokerUserProfile().getActiveBrokerEntities()) {
			BrokerEntityExtendedProfile p = b.getBrokerEntity();
			if (p.getId() == form.getSelectedPartyId()) {
				return p;
			}
		}
		for (BrokerEntityAssoc b : form.getBrokerUserProfile().getPendingBrokerEntities()) {
			BrokerEntityExtendedProfile p = b.getBrokerEntity();
			if (p.getId() == form.getSelectedPartyId()) {
				return p;
			}
		}
		return null;
	}

	protected boolean setupForm(HttpServletRequest request, ActionForm actionForm, boolean isUpdate) {
		ExtendedBDExtUserProfile managedUserProfile = UserManagementContext.getContext(request).getManagedUserProfile();

		if (managedUserProfile == null
				|| managedUserProfile.getRoleType().compareTo(BDUserRoleType.FinancialRep) != 0) {
			// this should not happen unless using book mark
			logger.error("The selected user is not a Broker User, userProfileId = "
					+ (managedUserProfile == null ? "" : managedUserProfile.getProfileId()));
			return false;
		}
		BrokerUserManagementForm form = (BrokerUserManagementForm) actionForm;
		form.setBrokerUserProfile((ExtendedBrokerUserProfile) managedUserProfile);
		form.setInternalUserRole(request);
		form.setFirms(((ExtendedBrokerUserProfile) managedUserProfile).getRiaFirms());
		UserManagementHelper.sortFirmsById(form.getFirms());
		if (StringUtils.isNotEmpty(request.getParameter("resendSuccess"))) {
			form.setResendActivationSuccess(true);
			form.setEnableResend(false);
		}
		return true;
	}

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */
	@Autowired
	private BDValidatorFWView bdValidatorFWView;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWView);
	}

}
