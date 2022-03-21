package com.manulife.pension.bd.web.usermanagement;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.delegate.BDPublicSecurityServiceDelegate;
import com.manulife.pension.delegate.BDUserManagementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.passcode.IllegalPasscodeExemptException;
import com.manulife.pension.service.security.passcode.PasscodeExemptInfo;
import com.manulife.pension.service.security.passcode.UserPasscodeDetailedInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;

@Controller
@RequestMapping(value = "/usermanagement")

public class PasscodeExemptionController extends BaseAutoController {
	
	@ModelAttribute("passcodeExemptionForm")
	public PasscodeExemptionForm populateForm() {
		return new PasscodeExemptionForm();
	}
	private static final String REMOVE = "remove";

	private static final String EXEMPT = "exempt";
	
	private static final String FINISH = "finish";
	
	private static final String CANCEL = "cancel";
	
	private static final String DEFAULT = "default";
	
	private static final String SPACE= " " ;
	
	private static final String DATE_FORMATTER= "MMM dd, yyyy hh:mm:ss a 'ET'";
	
	private static final String EXEMPT_CONFIRM = "exemptConfirm";

	private static final String REMOVE_CONFIRM = "removeConfirm";
	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("cancel", "redirect:/do/usermanagement/search?task=refresh");
		forwards.put("exempt", "/usermanagement/passcodeExemption.jsp");
		forwards.put("remove", "/usermanagement/passcodeExemptRemove.jsp");
		forwards.put("finish", "redirect:/do/usermanagement/search?task=refresh");
		forwards.put("exemptConfirm", "/usermanagement/passcodeExemptConfirm.jsp");
		forwards.put("removeConfirm", "/usermanagement/passcodeExemptRemoveConfirm.jsp");

	}

	@RequestMapping(value = "/passcodeExemption", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("passcodeExemptionForm") PasscodeExemptionForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doDefault");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("exempt");
			}
		}
		ExtendedBDExtUserProfile managedUserProfile = UserManagementContext.getContext(request).getManagedUserProfile();
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		BDPrincipal principal = userProfile.getBDPrincipal();

		actionForm.resetForm();
		if (managedUserProfile != null && managedUserProfile.getProfileId() != 0) {

			PasscodeExemptInfo passcodeExemptInfo  = BDUserManagementServiceDelegate.getInstance().getPasscodeExemptInfo(principal,
					managedUserProfile.getProfileId());

			populateForm(managedUserProfile, actionForm, passcodeExemptInfo);
			actionForm.setUserRole(BDUserRoleDisplayNameUtil.getInstance().getDisplayName(managedUserProfile.getRoleType()));

			if (passcodeExemptInfo == null) {
				return forwards.get(EXEMPT);
			} else {
				request.setAttribute(PasscodeExemptionForm.EXEMPTINFO_KEY, passcodeExemptInfo);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("exit <-- doDefault");
			}
			return forwards.get(REMOVE);
		} else {
			return forwards.get(FINISH);
		}

	}
	
	@RequestMapping(value = "/passcodeExemption", params = { "action=ExemptPasscode" }, method = { RequestMethod.POST })
	public String doExemptPasscode(@Valid @ModelAttribute("passcodeExemptionForm") PasscodeExemptionForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("exempt");
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doExempt");
		}
		ExtendedBDExtUserProfile managedUserProfile = UserManagementContext
				.getContext(request).getManagedUserProfile();
		
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		BDPrincipal	principal = userProfile.getBDPrincipal();
		
		final UserInfo userInfo = populateUserInfo(form);
		userInfo.setProfileId(managedUserProfile.getProfileId());
		
		List<ValidationError> errors;
		errors = validateExemptRequest(managedUserProfile, form);

		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			return forwards.get(EXEMPT);
		}
		
		PasscodeExemptInfo passcodeExemptInfo = populatePasscodeInfo(form,
				request, userInfo);
		
		try {
			BDUserManagementServiceDelegate.getInstance().exemptPasscode(
					principal, managedUserProfile.getProfileId(), passcodeExemptInfo);
		} catch (IllegalPasscodeExemptException e) {
			errors.add(new ValidationError(PasscodeExemptionForm.FIELD_EXEMPTION_REASON,
					BDErrorCodes.ERROR_ALREADY_EXEMPTED_BY_INTERNAL_USER, Type.error));
			if (!errors.isEmpty()) {
				setErrorsInRequest(request, errors);
				return forwards.get(EXEMPT);
			}
		} catch (SecurityServiceException e) {
			if (UserManagementHelper.isManagementConflictException(e)) {
				UserManagementHelper.setUserManagementException(request, e);
				return forwards.get("exception");
			}

			logger.debug("Reset password fails", e);
			errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e)));
		}
		
		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			return forwards.get(EXEMPT);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doExempt");
		}
		return forwards.get(EXEMPT_CONFIRM);
	}
	@RequestMapping(value = "/passcodeExemption", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(@Valid @ModelAttribute("passcodeExemptionForm") PasscodeExemptionForm actionForm,
			BindingResult bindingResult, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		UserManagementContext.clearContext(request);
		return forwards.get(CANCEL);
	}
	
	@RequestMapping(value = "/passcodeExemption", params = { "action=RemoveExempt" }, method = { RequestMethod.POST })
	public String doRemoveExempt(@Valid @ModelAttribute("passcodeExemptionForm") PasscodeExemptionForm form,
			BindingResult bindingResult, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("exempt");
			}
		} 
		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doRemoveExempt");
		}
		boolean removeStatus = false;
		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		ExtendedBDExtUserProfile managedUserProfile = UserManagementContext.getContext(request).getManagedUserProfile();

		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		BDPrincipal principal = userProfile.getBDPrincipal();
		
		final UserInfo userInfo = populateUserInfo(form);
		userInfo.setProfileId(managedUserProfile.getProfileId());

		PasscodeExemptInfo passcodeExemptInfo = populatePasscodeInfo(form,
				request, userInfo);
		passcodeExemptInfo.setRemovedByProfileId(principal.getProfileId());

		try {
			removeStatus = 	BDUserManagementServiceDelegate.getInstance().removePasscodeExempt(
					principal, managedUserProfile.getProfileId(), passcodeExemptInfo);
		} catch (SecurityServiceException e) {

			if (UserManagementHelper.isManagementConflictException(e)) {
				UserManagementHelper.setUserManagementException(request, e);
				return forwards.get("exception");
			}

			logger.debug("Reset password fails", e);
			errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e)));
		
		}
		
		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			return forwards.get(EXEMPT);
		}
		
		if (!removeStatus) {
			errors.add(new ValidationError(PasscodeExemptionForm.FIELD_EXEMPTION_REASON,
					BDErrorCodes.ERROR_ALREADY_REMOVED_BY_INTERNAL_USER, Type.error));
			if (!errors.isEmpty()) {
				setErrorsInRequest(request, errors);
				return forwards.get(REMOVE);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doRemoveExempt");
		}
		 
		return forwards.get(REMOVE_CONFIRM);
	}
	
		
	/**
	 * Validate if the user is allowed to be reset password
	 * 
	 * @param managedUserProfile
	 * @return
	 */
	private List<ValidationError> validateExemptRequest(ExtendedBDExtUserProfile managedUserProfile,
			PasscodeExemptionForm form) {
		List<ValidationError> errors = new ArrayList<ValidationError>(1);

		if (StringUtils.isBlank(form.getExemptionReason())) {
			errors.add(new ValidationError("", BDErrorCodes.ERROR_EXEMPTION_REASON_MANDATORY));
			return errors;
		}
		
		if (StringUtils.isBlank(form.getExemptRequestedName())) {
			errors.add(new ValidationError("", BDErrorCodes.ERROR_REQUESTED_BY_MUST_FULL_NAME));
			return errors;
		}
		
		if (StringUtils.isBlank(form.getPpmTicket())) {
			errors.add(new ValidationError("", BDErrorCodes.ERROR_PPM_TICKET_NUMBER_MANDATORY));
			return errors;
		}
		
		try {
			
			UserPasscodeDetailedInfo details = BDPublicSecurityServiceDelegate.getInstance()
					.getUserPasscodeDetailedInfo(managedUserProfile.getProfileId());
			if (details.isPasscodeLocked()) {
				errors.add(new ValidationError("", BDErrorCodes.ERROR_ALREADY_PASSCODE_LOCKED));
				return errors;
			}

		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return errors;
	}


	protected PasscodeExemptInfo populatePasscodeInfo(PasscodeExemptionForm form, HttpServletRequest request,
			UserInfo userInfo) {
		PasscodeExemptInfo passcodeExemptInfo = new PasscodeExemptInfo();
		passcodeExemptInfo
				.setCreatedByProfileId(BDSessionHelper.getUserProfile(request).getBDPrincipal().getProfileId());
		passcodeExemptInfo.setExemptionReason(form.getExemptionReason());
		passcodeExemptInfo.setRequestedByName(form.getExemptRequestedName());
		passcodeExemptInfo.setPpmNumber(form.getPpmTicket());
		passcodeExemptInfo.setProfileId(userInfo.getProfileId());
		passcodeExemptInfo.setTypeCode(BDConstants.TYPE_CODE);
		return passcodeExemptInfo;
	}
		
	
	protected void populateForm(ExtendedBDExtUserProfile user, PasscodeExemptionForm form,
			PasscodeExemptInfo passcodeExemptInfo) {

		form.setUserName(user.getUserName());
		form.setFirstName(user.getFirstName());
		form.setLastName(user.getLastName());
		form.setEmail(user.getCommunicationEmailAddress());

		if (passcodeExemptInfo != null) {
		
			form.setExemptionType(passcodeExemptInfo.getTypeCode());
			form.setExemptionRequestedBy(passcodeExemptInfo
					.getRequestedByName());
			form.setPpmTicket(passcodeExemptInfo.getPpmNumber());
			form.setExemptTimeStamp(new SimpleDateFormat(DATE_FORMATTER)
					.format(passcodeExemptInfo.getExemptionCreatedTs()));
			form.setExemptProccessedBy(String.valueOf(passcodeExemptInfo
					.getCreatedByProfileId()));
			form.setExemptProccessedByName(passcodeExemptInfo
					.getCreatedByFirstName()
					+ SPACE + passcodeExemptInfo.getCreatedByLastName());
			form.setExemptionReason(passcodeExemptInfo.getExemptionReason());
		}
	}
	
	protected UserInfo populateUserInfo(PasscodeExemptionForm form) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(form.getUserName());
		userInfo.setFirstName(form.getFirstName());
		userInfo.setLastName(form.getLastName());
		userInfo.setEmail(form.getEmail());
		return userInfo;
	}
	
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}
		
}