package com.manulife.pension.bd.web.usermanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.bd.web.validation.rules.AccessCodeRule;
import com.manulife.pension.delegate.BDUserManagementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.BDUserProfileStatus;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerUserProfile;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

@Controller
@RequestMapping(value = "/usermanagement")

public class ResetPasswordController extends BaseAutoController {
	@ModelAttribute("resetPasswordForm")
	public ResetPasswordForm populateForm() {
		return new ResetPasswordForm();

	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("exception", "redirect:/do/usermanagement/search?task=exception");
		forwards.put("cancel", "redirect:/do/usermanagement/search?task=refresh");
		forwards.put("input", "/usermanagement/resetPassword.jsp");
	}

	@RequestMapping(value = "/resetPassword", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("resetPasswordForm") ResetPasswordForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		ExtendedBDExtUserProfile managedUserProfile = UserManagementContext.getContext(request).getManagedUserProfile();

		// security check
		if (managedUserProfile != null) {
			actionForm.populate(managedUserProfile);

			List<ValidationError> errors = validateResetPassword(managedUserProfile);
			if (!errors.isEmpty()) {
				request.setAttribute(BDConstants.WARNING_MESSAGES, errors);
				actionForm.setDisabled(true);
			}
			return forwards.get("input");
		} else {
			return forwards.get("cancel");
		}
	}

	@RequestMapping(value = "/resetPassword", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(@Valid @ModelAttribute("resetPasswordForm") ResetPasswordForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		UserManagementContext.clearContext(request);
		return forwards.get("cancel");
	}

	@RequestMapping(value = "/resetPassword", params = { "action=resetPassword" }, method = { RequestMethod.POST })
	public String doResetPassword(@Valid @ModelAttribute("resetPasswordForm") ResetPasswordForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		List<ValidationError> errors = new ArrayList<ValidationError>(1);

		AccessCodeRule.getInstance().validate(CreateFirmRepForm.FIELD_PASS_CODE, errors, actionForm.getAccessCode());
		if (errors.size() > 0) {
			actionForm.setDisabled(false);
			setErrorsInRequest(request, errors);
		} else {
			ExtendedBDExtUserProfile managedUserProfile = UserManagementContext.getContext(request)
					.getManagedUserProfile();
			try {

				BDUserManagementServiceDelegate.getInstance().resetPassword(
						BDSessionHelper.getUserProfile(request).getBDPrincipal(), managedUserProfile.getProfileId(),
						actionForm.getAccessCode());
			} catch (SecurityServiceException e) {
				if (UserManagementHelper.isManagementConflictException(e)) {
					UserManagementHelper.setUserManagementException(request, e);
					return forwards.get("exception");
				}

				logger.debug("Reset password fails", e);
				errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e)));
			}
			if (errors.size() == 0) {
				actionForm.setDisabled(true);
				actionForm.setSuccess(true);
			} else {
				actionForm.setDisabled(false);
				actionForm.setSuccess(false);
				setErrorsInRequest(request, errors);
			}
		}
		actionForm.populate(UserManagementContext.getContext(request).getManagedUserProfile());
		return forwards.get("input");
	}

	/**
	 * Validate if the user is allowed to be reset password
	 * 
	 * @param managedUserProfile
	 * @return
	 */
	private List<ValidationError> validateResetPassword(ExtendedBDExtUserProfile managedUserProfile) {
		List<ValidationError> errors = new ArrayList<ValidationError>(1);

		// not registered
		BDUserProfileStatus profileStatus = managedUserProfile.getProfileStatus();
		if (profileStatus == null || BDUserProfileStatus.Registered.compareTo(profileStatus) != 0) {
			errors.add(new ValidationError("", BDErrorCodes.RESET_PWD_INVALID_PROFILE_STATUS));
			return errors;
		}

		// check if it has no active broker entity (level 1 upgrade
		if (managedUserProfile.getRoleType().compareTo(BDUserRoleType.FinancialRep) == 0) {
			ExtendedBrokerUserProfile brokerProfile = (ExtendedBrokerUserProfile) managedUserProfile;
			if (CollectionUtils.isEmpty(brokerProfile.getActiveBrokerEntities())) {
				errors.add(new ValidationError("", BDErrorCodes.RESET_PWD_INVALID_PROFILE_STATUS));
				return errors;
			}
		}

		if (StringUtils.isEmpty(managedUserProfile.getCommunicationEmailAddress())) {
			errors.add(new ValidationError("", BDErrorCodes.RESET_PWD_EMAIL_MISSING));
			return errors;
		}
		return errors;
	}

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}

}
