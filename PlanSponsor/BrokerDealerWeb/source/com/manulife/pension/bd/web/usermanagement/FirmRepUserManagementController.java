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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.registration.RegisterFirmRepStep2Form;
import com.manulife.pension.bd.web.registration.util.RegistrationUtils;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.bd.web.validation.rules.AccessCodeRule;
import com.manulife.pension.bd.web.validation.rules.AddressRule;
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
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.BDUserProfileStatus;
import com.manulife.pension.service.security.bd.valueobject.BDUserAddressValueObject;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedFirmRepUserProfile;
import com.manulife.pension.service.security.bd.valueobject.FirmRepCreationValueObject;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject.SiteLocation;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

@Controller
@RequestMapping(value ="/manage")
@SessionAttributes({"manageFirmRepForm"})

public class FirmRepUserManagementController extends AbstractUserManagementController {
	@ModelAttribute("manageFirmRepForm") 
	public FirmRepUserManagementForm populateForm() 
	{
		return new FirmRepUserManagementForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/usermanagement/firmrepPostReg.jsp");
		forwards.put("exception","redirect:/do/usermanagement/search?task=exception");
		forwards.put("back","redirect:/do/usermanagement/search?task=refresh");
		forwards.put("search","redirect:/do/usermanagement/search");
		forwards.put("resendSuccess","redirect:/do/manage/firmrep?resendSuccess=y");
		forwards.put("passcodeView","redirect:/do/usermanagement/passcodeView");
		forwards.put("exemptPasscode","redirect:/do/usermanagement/passcodeExemption");
		forwards.put("resetPassword","redirect:/do/usermanagement/resetPassword");
		forwards.put("preRegister","/usermanagement/firmrepPreReg.jsp");
		forwards.put("postRegister","/usermanagement/firmrepPostReg.jsp");
		}

	private final MandatoryRule firstNameMandatoryRule = new MandatoryRule(
			BDErrorCodes.MISSING_FIRST_NAME);

	private final MandatoryRule lastNameMandatoryRule = new MandatoryRule(
			BDErrorCodes.MISSING_LAST_NAME);
    
    private static final RegularExpressionRule invalidFirstAndLastNameValuesRErule = new RegularExpressionRule(
            BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);

	private final MandatoryRule fundListingMandatoryRule = new MandatoryRule(
			BDErrorCodes.FUND_LISTING_NOT_SELECTED);

    private static final String FIRST_NAME_LABEL = "First Name";

    private static final String LAST_NAME_LABEL = "Last Name";

    private final List<ValidationError> tempArrayList = new ArrayList<ValidationError>();

	public FirmRepUserManagementController() {
		super(FirmRepUserManagementController.class);
	}
	

	@RequestMapping(value ="/firmrep", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("manageFirmRepForm") FirmRepUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		setupForm(request, actionForm, false);
		return findForward( (FirmRepUserManagementForm) actionForm);
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
	@RequestMapping(value ="/firmrep", params = {"action=resendActivation"},method = {	RequestMethod.GET,RequestMethod.POST })
	public String doResendActivation(@Valid @ModelAttribute("manageFirmRepForm") FirmRepUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

		if (!setupForm(request, actionForm, true)) {
			return forwards.get(Back);
		}

		FirmRepUserManagementForm form = (FirmRepUserManagementForm) actionForm;

		form.setFirms(BDWebCommonUtils.getFirmList(form.getFirmListStr()));

		if (!form.isResendInvitationAllowed()) {
			return forwards.get(Back);
		}

		List<ValidationError> errors = new ArrayList<ValidationError>();
		// validate the input field values
		validatePreRegForm(form, errors);
		if (errors.isEmpty()) {
			FirmRepCreationValueObject vo = getFirmRepCreationVO(form);
			BDPrincipal internalUser = BDSessionHelper.getUserProfile(request)
					.getBDPrincipal();
			long profileId = form.getFirmRepUserProfile().getProfileId();
			try {
				BDUserManagementServiceDelegate.getInstance()
						.updateFirmRepAndResendInvitation(internalUser,
								profileId, vo);
				UserManagementContext.setCurrentUser(request, internalUser,
						profileId);
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
				logger.debug("Update firm rep fails", e);
				errors.add(new ValidationError("",
						SecurityServiceExceptionHelper.getErrorCode(e)));
			}
		}
		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			form.setChanged(true);
		}
		return findForward( (FirmRepUserManagementForm) actionForm);
	}

	@Override
	protected String getView(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		return findForward((FirmRepUserManagementForm) actionForm);
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
	
	@RequestMapping(value ="/firmrep", params = {"action=save"},method = {RequestMethod.GET })
	public String doSave(@Valid @ModelAttribute("manageFirmRepForm") FirmRepUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

		if (!setupForm(request, actionForm, true)) {
			return forwards.get(Back);
		}

		FirmRepUserManagementForm form = (FirmRepUserManagementForm) actionForm;
		BDUserProfile profile = BDSessionHelper.getUserProfile(request);
		String userType = profile.getRole().getRoleType().getUserRoleCode();
		if (!form.isUpdateAllowed() && !(StringUtils.equals(userType, BDUserRoleType.RIAUserManager.getUserRoleCode()))) {
			return forwards.get(Back);
		}
		form.setFirms(BDWebCommonUtils.getFirmList(form.getFirmListStr()));
		form.setRiafirms(BDWebCommonUtils.getRiaFirmsWithPermission(form.getRiaFirmListStr(), form.getRiaFirmPermissionsListStr()));

		List<ValidationError> errors = new ArrayList<ValidationError>();
		// validate the input field values
		validatePostRegForm(form, errors);
		if (errors.isEmpty()) {
			FirmRepCreationValueObject vo = getFirmRepCreationVO(form);
			BDUserAddressValueObject addressVO = RegistrationUtils
					.populateBDAddressVO(form.getAddress());
			SiteLocation defaultFundListing = SiteLocation.valueOf(form
					.getDefaultSiteLocation());
			BDPrincipal internalUser = BDSessionHelper.getUserProfile(request)
					.getBDPrincipal();
			long profileId = form.getFirmRepUserProfile().getProfileId();
			try {
				BDUserManagementServiceDelegate.getInstance()
						.updateRegisteredFirmRep(internalUser, profileId, vo,
								addressVO, defaultFundListing);
				BDUserManagementServiceDelegate.getInstance().updateRiaFirmDetails(
						internalUser, profileId, form.getRiafirms());
				UserManagementContext.setCurrentUser(request, internalUser,
						profileId);
				setupForm(request, actionForm, false);
				form.setUpdateSuccess(true);
				form.setChanged(false);
			} catch (SecurityServiceException e) {
				if (UserManagementHelper.isManagementConflictException(e)) {
					return handleSecurityServiceException( request, e);
				}				
				logger.debug("Update firm rep fails", e);
				errors.add(new ValidationError("",
						SecurityServiceExceptionHelper.getErrorCode(e)));
			}
		}
		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			form.setChanged(true);
		}
		return findForward((FirmRepUserManagementForm) actionForm);
	}

	@RequestMapping(value = "/firmrep", params = { "action=mimic" }, method = { RequestMethod.GET})
	public String doMimic(@Valid @ModelAttribute("manageFirmRepForm") FirmRepUserManagementForm actionForm,
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
	
	@RequestMapping(value = "/firmrep", params = { "action=exemptPasscode" }, method = { RequestMethod.GET})
	public String doPasscodeExemption(@Valid @ModelAttribute("manageFirmRepForm") FirmRepUserManagementForm actionForm,
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
		String forward = doExemptPasscode(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(value = "/firmrep", params = { "action=passcodeView" }, method = { RequestMethod.GET})
	public String doPasscodeView(@Valid @ModelAttribute("manageFirmRepForm") FirmRepUserManagementForm actionForm,
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
		String forward = doPasscodeView(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(value = "/firmrep", params = { "action=resetPassword" }, method = { RequestMethod.GET})
	public String doResetPassword(@Valid @ModelAttribute("manageFirmRepForm") FirmRepUserManagementForm actionForm,
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
		String forward = doResetPassword(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(value = "/firmrep", params = { "action=delete" }, method = { RequestMethod.GET,RequestMethod.POST})
	public String doDelete(@Valid @ModelAttribute("manageFirmRepForm") FirmRepUserManagementForm actionForm,
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
		String forward = doDelete(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(value = "/firmrep", params = { "action=cancel" }, method = { RequestMethod.GET,RequestMethod.POST})
	public String doCancel(@Valid @ModelAttribute("manageFirmRepForm") FirmRepUserManagementForm actionForm,
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
		String forward = doCancel(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	private String findForward(
			FirmRepUserManagementForm actionForm) {
		ExtendedFirmRepUserProfile profile = actionForm.getFirmRepUserProfile();
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

	protected boolean setupForm(HttpServletRequest request,
			ActionForm actionForm, boolean isUpdate) {
		ExtendedBDExtUserProfile managedUserProfile = UserManagementContext
				.getContext(request).getManagedUserProfile();
		FirmRepUserManagementForm form = (FirmRepUserManagementForm) actionForm;
		form.setInternalUserRole(request);
		if (managedUserProfile == null
				|| managedUserProfile.getRoleType().compareTo(
						BDUserRoleType.FirmRep) != 0) {
			// this should not happen unless using book mark
			logger
					.error("The selected user is not a BDFirmRep User, userProfileId = "
							+ (managedUserProfile == null ? ""
									: managedUserProfile.getProfileId()));
			form.setFirmRepUserProfile(null, false);
			return false;
		} else {
			form.setFirmRepUserProfile(
					(ExtendedFirmRepUserProfile) managedUserProfile, isUpdate);
			if (StringUtils.isNotEmpty(request.getParameter("resendSuccess"))) {
				form.setPassCode("");
				form.setResendActivationSuccess(true);
				form.setChanged(false);
                form.setEnableResend(false);
			}			
			return true;
		}
	}

	private void validatePreRegForm(FirmRepUserManagementForm form,
			List<ValidationError> errors) {
		boolean isValid = false;
		isValid = firstNameMandatoryRule.validate(
				FirmRepUserManagementForm.FIELD_FIRST_NAME, errors, form
						.getFirstName());
		if (isValid) {
            isValid = invalidFirstAndLastNameValuesRErule.validate(FirmRepUserManagementForm.FIELD_FIRST_NAME,
                    tempArrayList, form.getFirstName());
            if (!isValid) {
                errors
                        .add(new ValidationError(FirmRepUserManagementForm.FIELD_FIRST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { FIRST_NAME_LABEL }));
            }
		}
		isValid = lastNameMandatoryRule.validate(
				FirmRepUserManagementForm.FIELD_LAST_NAME, errors, form
						.getLastName());
		if (isValid) {
            isValid = invalidFirstAndLastNameValuesRErule.validate(FirmRepUserManagementForm.FIELD_LAST_NAME,
                    tempArrayList, form.getLastName());
            if (!isValid) {
                errors
                        .add(new ValidationError(FirmRepUserManagementForm.FIELD_LAST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { LAST_NAME_LABEL }));
            }
		}
		PhoneNumberRule.getInstance().validate(
				FirmRepUserManagementForm.FIELD_PHONE_NUMBER, errors,
				form.getPhoneNumber());
		EmailAddressRule.getInstance().validate(
				FirmRepUserManagementForm.FIELD_EMAIL, errors,
				form.getEmailAddress());

		if (StringUtils.isEmpty(form.getFirmListStr())) {
			errors.add(new ValidationError(
					FirmRepUserManagementForm.FIELD_FIRMS,
					BDErrorCodes.USER_MANAGEMENT_MISSING_FIRMS));
		}

		AccessCodeRule.getInstance().validate(
				FirmRepUserManagementForm.FIELD_PASS_CODE, errors,
				form.getPassCode());
		return;
	}

	private void validatePostRegForm(FirmRepUserManagementForm form,
			List<ValidationError> errors) {
		boolean isValid = false;
		isValid = firstNameMandatoryRule.validate(
				FirmRepUserManagementForm.FIELD_FIRST_NAME, errors, form
						.getFirstName());
		if (isValid) {
            isValid = invalidFirstAndLastNameValuesRErule.validate(FirmRepUserManagementForm.FIELD_FIRST_NAME,
                    tempArrayList, form.getFirstName());
            if (!isValid) {
                errors
                        .add(new ValidationError(FirmRepUserManagementForm.FIELD_FIRST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { FIRST_NAME_LABEL }));
            }
		}
		isValid = lastNameMandatoryRule.validate(
				FirmRepUserManagementForm.FIELD_LAST_NAME, errors, form
						.getLastName());
		if (isValid) {
            isValid = invalidFirstAndLastNameValuesRErule.validate(FirmRepUserManagementForm.FIELD_LAST_NAME,
                    tempArrayList, form.getLastName());
            if (!isValid) {
                errors
                        .add(new ValidationError(FirmRepUserManagementForm.FIELD_LAST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { LAST_NAME_LABEL }));
            }
		}
		EmailAddressRule.getInstance().validate(
				FirmRepUserManagementForm.FIELD_EMAIL, errors,
				form.getEmailAddress());

		AddressRule.getInstance()
				.validate("address", errors, form.getAddress());

		PhoneNumberRule.getInstance().validate(
				FirmRepUserManagementForm.FIELD_PHONE_NUMBER, errors,
				form.getPhoneNumber());

		if (StringUtils.isEmpty(form.getFirmListStr())) {
			errors.add(new ValidationError(
					FirmRepUserManagementForm.FIELD_FIRMS,
					BDErrorCodes.USER_MANAGEMENT_MISSING_FIRMS));
		}
		if (StringUtils.isNotEmpty(form.getRiaFirmPermissionsListStr())) {

			String permissionCheck = form.getRiaFirmPermissionsListStr();
			String[] buf = permissionCheck.split(",");
			 if (buf.length > 20) {	
				errors.add(new ValidationError(
						RiaUserManagementForm.FIELD_FIRMS,
						BDErrorCodes.USER_MANAGEMENT_VIEW_RIA_FIRM_PERMISSION_EXCEEDED_BROKER));
			}	
		}
		fundListingMandatoryRule.validate(
				RegisterFirmRepStep2Form.FIELD_DEFAULT_SITE_LOCATION, errors,
				form.getDefaultSiteLocation());
	}

	private FirmRepCreationValueObject getFirmRepCreationVO(
			FirmRepUserManagementForm form) throws SystemException {
		FirmRepCreationValueObject firmRepCreationVO = new FirmRepCreationValueObject();
		firmRepCreationVO.setLastName(form.getLastName());
		firmRepCreationVO.setFirstName(form.getFirstName());
		firmRepCreationVO.setEmailAddress(form.getEmailAddress());
		firmRepCreationVO.setPhoneNumber(form.getPhoneNumber().getValue());
		firmRepCreationVO.setPassCode(form.getPassCode());
		// this is already set before
		firmRepCreationVO.setFirms(form.getFirms());
		return firmRepCreationVO;
	}
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
   	 */
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
	
}
