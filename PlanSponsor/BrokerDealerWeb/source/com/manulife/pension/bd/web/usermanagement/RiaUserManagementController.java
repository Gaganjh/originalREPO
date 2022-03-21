package com.manulife.pension.bd.web.usermanagement;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.manulife.pension.bd.web.registration.util.RegistrationUtils;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.bd.web.validation.rules.EmailAddressRule;
import com.manulife.pension.bd.web.validation.rules.PhoneNumberRule;
import com.manulife.pension.delegate.BDUserManagementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.BDUserProfileStatus;
import com.manulife.pension.service.security.bd.valueobject.BDUserAddressValueObject;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedRiaUserProfile;
import com.manulife.pension.service.security.bd.valueobject.RiaUserCreationValueObject;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject.SiteLocation;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

@Controller
@RequestMapping(value = "/manage")

public class RiaUserManagementController extends AbstractUserManagementController {
	@ModelAttribute("manageRiaForm")
	public RiaUserManagementForm populateForm() {
		return new RiaUserManagementForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/usermanagement/riaPostReg.jsp");
		forwards.put("exception", "redirect:/do/usermanagement/search?task=exception");
		forwards.put("back", "redirect:/do/usermanagement/search?task=refresh");
		forwards.put("search", "redirect:/do/usermanagement/search");
		forwards.put("resendSuccess", "redirect:/do/manage/ria?resendSuccess=y");
		forwards.put("passcodeView", "redirect:/do/usermanagement/passcodeView");
		forwards.put("exemptPasscode", "redirect:/do/usermanagement/passcodeExemption");
		forwards.put("resetPassword", "redirect:/do/usermanagement/resetPassword");
		forwards.put("preRegister", "/usermanagement/riaPreReg.jsp");
		forwards.put("postRegister", "/usermanagement/riaPostReg.jsp");
	}

	private final MandatoryRule firstNameMandatoryRule = new MandatoryRule(BDErrorCodes.MISSING_FIRST_NAME);

	private final MandatoryRule lastNameMandatoryRule = new MandatoryRule(BDErrorCodes.MISSING_LAST_NAME);
	
	private static final RegularExpressionRule invalidFirstAndLastNameValidRErule = new RegularExpressionRule(
			BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);

	// private final MandatoryRule fundListingMandatoryRule = new MandatoryRule(
	// BDErrorCodes.FUND_LISTING_NOT_SELECTED);

	private static final String FIRST_NAME_LABEL = "First Name";

	private static final String LAST_NAME_LABEL = "Last Name";

	private static final String RIA_EMAIL_EXISTS = "exists";

	private static final String RIA_EMAIL_NOT_EXISTS = "notexists";

	private final List<ValidationError> tempArrayList = new ArrayList<ValidationError>();

	public RiaUserManagementController() {
		super(RiaUserManagementController.class);
	}

	@RequestMapping(value = "/ria", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("manageRiaForm") RiaUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				setupForm(request, actionForm, false);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		setupForm(request, actionForm, false);
		return findForward((RiaUserManagementForm) actionForm);
	}

	/**
	 * Save changes and resend the invitation
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
	@RequestMapping(value = "/ria", params = { "action=resendActivation" }, method = { RequestMethod.GET,RequestMethod.POST })
	public String doResendActivation(@Valid @ModelAttribute("manageRiaForm") RiaUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				setupForm(request, actionForm, false);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		if (!setupForm(request, actionForm, true)) {
			return forwards.get(Back);
		}

		RiaUserManagementForm form = (RiaUserManagementForm) actionForm;

		List<BrokerDealerFirm> updatedFirms = BDWebCommonUtils.getRiaFirmsWithPermission(form.getFirmListStr(),
				form.getFirmPermissionsListStr());
		form.setFirms(updatedFirms);

		if (!form.isResendInvitationAllowed()) {
			return forwards.get(Back);
		}

		List<ValidationError> errors = new ArrayList<ValidationError>();
		// validate the input field values
		validatePreRegForm(form, errors);
		if (errors.isEmpty()) {
			RiaUserCreationValueObject vo = getRiaUserCreationVO(form);
			BDPrincipal internalUser = BDSessionHelper.getUserProfile(request).getBDPrincipal();
			long profileId = form.getriaUserProfile().getProfileId();
			try {
				BDUserManagementServiceDelegate.getInstance().updateRiaUserAndResendInvitation(internalUser, profileId,
						vo);
				UserManagementContext.setCurrentUser(request, internalUser, profileId);
				setupForm(request, actionForm, false);
				// clear the access code
				form.setPassCode("");
				form.setResendActivationSuccess(true);
				form.setChanged(false);
				form.setEnableResend(false);
				return forwards.get(ResendSuccess);
			} catch (SecurityServiceException e) {
				if (UserManagementHelper.isManagementConflictException(e)) {
					return handleSecurityServiceException(request, e);
				}
				logger.debug("Update ria user fails", e);
				errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e)));
			}
		}
		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			form.setChanged(true);
		}
		return findForward((RiaUserManagementForm) actionForm);
	}

	@RequestMapping(value = "/ria", params = { "action=mimic" }, method = { RequestMethod.GET})
	public String doMimic(@Valid @ModelAttribute("manageRiaForm") RiaUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				setupForm(request, actionForm, false);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = doMimic(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/ria", params = { "action=cancel" }, method = { RequestMethod.GET,RequestMethod.POST })
	public String doCancel(@Valid @ModelAttribute("manageRiaForm") RiaUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				setupForm(request, actionForm, false);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		return forwards.get(doCancel(actionForm, request, response));
	}

	@RequestMapping(value = "/ria", params = { "action=passcodeView" }, method = { RequestMethod.GET })
	public String doPasscodeView(@Valid @ModelAttribute("manageRiaForm") RiaUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				setupForm(request, actionForm, false);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		return forwards.get(doPasscodeView(actionForm, request, response));
	}

	@RequestMapping(value = "/ria", params = { "action=exemptPasscode" }, method = { RequestMethod.GET })
	public String doExemptPasscode(@Valid @ModelAttribute("manageRiaForm") RiaUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				setupForm(request, actionForm, false);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		return forwards.get(doExemptPasscode(actionForm, request, response));
	}

	@RequestMapping(value = "/ria", params = { "action=resetPassword" }, method = { RequestMethod.GET })
	public String doResetPassword(@Valid @ModelAttribute("manageRiaForm") RiaUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				setupForm(request, actionForm, false);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		return forwards.get(doResetPassword(actionForm, request, response));
	}

	@RequestMapping(value = "/ria", params = { "action=delete" }, method = {  RequestMethod.GET,RequestMethod.POST })
	public String doDelete(@Valid @ModelAttribute("manageRiaForm") RiaUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				setupForm(request, actionForm, false);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		return forwards.get(doDelete(actionForm, request, response));
	}

	/**
	 * Save changes and resend the invitation
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
	@RequestMapping(value = "/ria", params = { "action=save" }, method = { RequestMethod.GET })
	public String doSave(@Valid @ModelAttribute("manageRiaForm") RiaUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				setupForm(request, actionForm, false);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		if (!setupForm(request, actionForm, true)) {
			return forwards.get(Back);
		}

		RiaUserManagementForm form = (RiaUserManagementForm) actionForm;

		if (!form.isUpdateAllowed()) {
			return forwards.get(Back);
		}
		List<BrokerDealerFirm> updatedFirms = BDWebCommonUtils.getRiaFirmsWithPermission(form.getFirmListStr(),
				form.getFirmPermissionsListStr());
		form.setFirms(updatedFirms);

		List<ValidationError> errors = new ArrayList<ValidationError>();
		// validate the input field values
		validatePostRegForm(form, errors);
		if (errors.isEmpty()) {
			RiaUserCreationValueObject vo = getRiaUserCreationVO(form);
			BDUserAddressValueObject addressVO = RegistrationUtils.populateBDAddressVO(form.getAddress());
			SiteLocation defaultFundListing = SiteLocation.getByCode(form.getDefaultSiteLocation());
			BDPrincipal internalUser = BDSessionHelper.getUserProfile(request).getBDPrincipal();
			long profileId = form.getriaUserProfile().getProfileId();
			try {
				BDUserManagementServiceDelegate.getInstance().updateRegisteredRiaUser(internalUser, profileId, vo,
						addressVO, defaultFundListing);
				UserManagementContext.setCurrentUser(request, internalUser, profileId);
				setupForm(request, actionForm, false);
				form.setUpdateSuccess(true);
				form.setChanged(false);
			} catch (SecurityServiceException e) {
				if (UserManagementHelper.isManagementConflictException(e)) {
					return handleSecurityServiceException(request, e);
				}
				logger.debug("Update RIA user fails", e);
				errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e)));
			}
		}
		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			form.setChanged(true);
		}
		return findForward((RiaUserManagementForm) actionForm);
	}

	/**
	 * This method is used to check the existence of email
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/ria", params = { "action=checkDuplicateEmail" }, method = { RequestMethod.POST,RequestMethod.GET })
	public String doCheckDuplicateEmail(@Valid @ModelAttribute("manageRiaForm") RiaUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				setupForm(request, actionForm, false);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCheckDuplicateEmail() in RiaUserManagementAction");
		}

		String contractJsonObj = request.getParameter("jsonObj");
		String responseText = "{";
		String status = StringUtils.EMPTY;
		if (StringUtils.isNotEmpty(contractJsonObj)) {
			int riaEmailCount = BDWebCommonUtils.getRegisteredEmailCount(contractJsonObj);
			if (riaEmailCount != 0) {
				status = RIA_EMAIL_EXISTS;
			} else {
				status = RIA_EMAIL_NOT_EXISTS;
			}
		}
		responseText += "\"Status\":\"" + status + "\"}";

		// Sending the response back to AJAX call
		response.setContentType("text/html");
		PrintWriter out;

		try {
			out = response.getWriter();
		} catch (IOException exception) {
			throw new SystemException(exception,
					"IOException occured while checking the existence of Email " + contractJsonObj);
		}

		out.print(responseText.trim());
		out.flush();
		return null;
	}
	
	@Override
	protected String getView(AutoForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		return findForward((RiaUserManagementForm) actionForm);
	}

	private String findForward(RiaUserManagementForm actionForm) {
		ExtendedRiaUserProfile profile = actionForm.getriaUserProfile();
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

		RiaUserManagementForm form = (RiaUserManagementForm) actionForm;
		form.setInternalUserRole(request);

		if (managedUserProfile == null || managedUserProfile.getRoleType().compareTo(BDUserRoleType.RIAUser) != 0) {
			// this should not happen unless using book mark
			logger.error("The selected user is not a Ria User, userProfileId = "
					+ (managedUserProfile == null ? "" : managedUserProfile.getProfileId()));
			form.setRiaUserProfile(null, false);
			return false;
		} else {
			form.setRiaUserProfile((ExtendedRiaUserProfile) managedUserProfile, isUpdate);
			if (StringUtils.isNotEmpty(request.getParameter("resendSuccess"))) {
				form.setPassCode("");
				form.setResendActivationSuccess(true);
				form.setChanged(false);
				form.setEnableResend(false);
			}
			return true;
		}
	}

	private void validatePreRegForm(RiaUserManagementForm form, List<ValidationError> errors) {
		boolean isValid = false;
		isValid = firstNameMandatoryRule.validate(RiaUserManagementForm.FIELD_FIRST_NAME, errors, form.getFirstName());
		if (isValid) {
			isValid = invalidFirstAndLastNameValidRErule.validate(RiaUserManagementForm.FIELD_FIRST_NAME, tempArrayList,
					form.getFirstName());
			if (!isValid) {
				errors.add(new ValidationError(RiaUserManagementForm.FIELD_FIRST_NAME,
						BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, new Object[] { FIRST_NAME_LABEL }));
			}
		}
		isValid = lastNameMandatoryRule.validate(RiaUserManagementForm.FIELD_LAST_NAME, errors, form.getLastName());
		if (isValid) {
			isValid = invalidFirstAndLastNameValidRErule.validate(RiaUserManagementForm.FIELD_LAST_NAME, tempArrayList,
					form.getLastName());
			if (!isValid) {
				errors.add(new ValidationError(RiaUserManagementForm.FIELD_LAST_NAME,
						BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, new Object[] { LAST_NAME_LABEL }));
			}
		}
		PhoneNumberRule.getInstance().validate(RiaUserManagementForm.FIELD_PHONE_NUMBER, errors, form.getPhoneNumber());
		EmailAddressRule.getInstance().validate(RiaUserManagementForm.FIELD_EMAIL, errors, form.getEmailAddress());

		if (StringUtils.isEmpty(form.getFirmListStr())) {
			errors.add(new ValidationError(RiaUserManagementForm.FIELD_FIRMS,
					BDErrorCodes.USER_MANAGEMENT_MISSING_RIA_FIRMS));
		}

		if (StringUtils.isNotEmpty(form.getFirmPermissionsListStr())) {

			String permissionCheck = form.getFirmPermissionsListStr();
			String[] buf = permissionCheck.split(",");
			if (buf.length > 20) {
				errors.add(new ValidationError(RiaUserManagementForm.FIELD_FIRMS,
						BDErrorCodes.USER_MANAGEMENT_VIEW_RIA_FIRM_PERMISSION_EXCEEDED));
			}
		}

		/*
		 * AccessCodeRule.getInstance().validate(
		 * RiaUserManagementForm.FIELD_PASS_CODE, errors, form.getPassCode());
		 */
		return;
	}

	private void validatePostRegForm(RiaUserManagementForm form, List<ValidationError> errors) {
		boolean isValid = false;
		isValid = firstNameMandatoryRule.validate(RiaUserManagementForm.FIELD_FIRST_NAME, errors, form.getFirstName());
		if (isValid) {
			isValid = invalidFirstAndLastNameValidRErule.validate(RiaUserManagementForm.FIELD_FIRST_NAME, tempArrayList,
					form.getFirstName());
			if (!isValid) {
				errors.add(new ValidationError(RiaUserManagementForm.FIELD_FIRST_NAME,
						BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, new Object[] { FIRST_NAME_LABEL }));
			}
		}
		isValid = lastNameMandatoryRule.validate(RiaUserManagementForm.FIELD_LAST_NAME, errors, form.getLastName());
		if (isValid) {
			isValid = invalidFirstAndLastNameValidRErule.validate(RiaUserManagementForm.FIELD_LAST_NAME, tempArrayList,
					form.getLastName());
			if (!isValid) {
				errors.add(new ValidationError(RiaUserManagementForm.FIELD_LAST_NAME,
						BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, new Object[] { LAST_NAME_LABEL }));
			}
		}
		EmailAddressRule.getInstance().validate(RiaUserManagementForm.FIELD_EMAIL, errors, form.getEmailAddress());

		// AddressRule.getInstance()
		// .validate("address", errors, form.getAddress());

		PhoneNumberRule.getInstance().validate(RiaUserManagementForm.FIELD_PHONE_NUMBER, errors, form.getPhoneNumber());

		if (StringUtils.isEmpty(form.getFirmListStr())) {
			errors.add(new ValidationError(RiaUserManagementForm.FIELD_FIRMS,
					BDErrorCodes.USER_MANAGEMENT_MISSING_RIA_FIRMS));
		}

		if (StringUtils.isNotEmpty(form.getFirmPermissionsListStr())) {

			String permissionCheck = form.getFirmPermissionsListStr();
			String[] buf = permissionCheck.split(",");
			if (buf.length > 20) {
				errors.add(new ValidationError(RiaUserManagementForm.FIELD_FIRMS,
						BDErrorCodes.USER_MANAGEMENT_VIEW_RIA_FIRM_PERMISSION_EXCEEDED));
			}
		}
	}

	private RiaUserCreationValueObject getRiaUserCreationVO(RiaUserManagementForm form) throws SystemException {
		RiaUserCreationValueObject riaUserCreationVO = new RiaUserCreationValueObject();
		riaUserCreationVO.setLastName(form.getLastName());
		riaUserCreationVO.setFirstName(form.getFirstName());
		riaUserCreationVO.setEmailAddress(form.getEmailAddress());
		riaUserCreationVO.setPhoneNumber(form.getPhoneNumber().getValue());
		// this is already set before
		riaUserCreationVO.setFirms(form.getFirms());
		return riaUserCreationVO;
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations as part of the CL#136970.
	 */
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}

}
