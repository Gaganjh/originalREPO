package com.manulife.pension.bd.web.myprofile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.navigation.UserNavigationFactory;
import com.manulife.pension.bd.web.registration.RegisterBrokerStep2Form;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.util.PasswordMeterUtility;
import com.manulife.pension.platform.web.validation.rules.NewPasswordRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject.SiteLocation;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;

/**
 * The action handles the internal user update his own profile
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping(value = "/myprofile")

public class MyProfileInternalUserController extends BaseAutoController {
	
	@ModelAttribute("myprofileInternalForm")
	public MyProfileInternalForm populateForm() {
		return new MyProfileInternalForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/myprofile/internalUserProfile.jsp");
		forwards.put("cancel", "redirect:/do/home/");
		forwards.put("fail", "/myprofile/internalUserProfile.jsp");
	}

	private final MandatoryRule licenseMandatoryRule = new MandatoryRule(
			BDErrorCodes.LICENSE_VERIFICATION_NOT_SELECTED);

	private final MandatoryRule fundListingMandatoryRule = new MandatoryRule(BDErrorCodes.FUND_LISTING_NOT_SELECTED);

	public MyProfileInternalUserController() {
		super(MyProfileInternalUserController.class);
	}

	@RequestMapping(value = "/internal", method = { RequestMethod.POST, RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("myprofileInternalForm") MyProfileInternalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");
			}
		}
		populateForm(form, request, true);
		return forwards.get("input");
	}

	@RequestMapping(value = "/internal", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(@Valid @ModelAttribute("myprofileInternalForm") MyProfileInternalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");
			}
		}
		return forwards.get("cancel");
	}

	@RequestMapping(value = "/internal", params = { "action=save" }, method = { RequestMethod.POST})
	public String doSave(@Valid @ModelAttribute("myprofileInternalForm") MyProfileInternalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");
			}
		}

		List<ValidationError> errors = new ArrayList<ValidationError>();

		if (!allPasswordsEmpty(form) && !allPasswordsPresent(form)) {
			errors.add(new ValidationError("", BDErrorCodes.ALL_PASSWORDS_MANDATORY));
		}

		// if either of new/confirmed is entered, needs to validate
		// changes for defects 8589 , 8590
		
		if (StringUtils.isNotEmpty(form.getConfirmedPassword()) || StringUtils.isNotEmpty(form.getNewPassword())) {
			Pair<String, String> pair = new Pair<String, String>(form.getNewPassword(), form.getConfirmedPassword());
			
			NewPasswordRule.getInstance().validate("", errors, pair);
			if(null != errors && errors.isEmpty()) {
				SecurityServiceDelegate serviceInstance = null;
		        String responseText = null;
		        BDUserProfile userProfileDetails = BDSessionHelper.getUserProfile(request);
		        String userName = StringUtils.EMPTY;
		        if(null != userProfileDetails){
		          userName = userProfileDetails.getBDPrincipal().getUserName();
		        }
			serviceInstance = SecurityServiceDelegate.getInstance();
				try {
					responseText = serviceInstance.passwordStrengthValidation(form.getNewPassword(), userName,Boolean.FALSE);
					getPasswordScore(responseText,errors,request);
				} catch (Exception e) {
					logger.debug("Failed to update password", e);
				}
			}
            
            // end changes for defect 8589 , 8590
			//when Deapi is down return forwward to interuserl page with displayed error message
			if(null!= request.getSession(false).getAttribute("Deapi") && request.getSession(false).getAttribute("Deapi").equals("down")){
				return forwards.get("input");
			}
			
	 		
		}
		licenseMandatoryRule.validate(RegisterBrokerStep2Form.FIELD_PRODUCER_LICENSE, errors,
				form.getProducerLicense());
		fundListingMandatoryRule.validate(RegisterBrokerStep2Form.FIELD_DEFAULT_SITE_LOCATION, errors,
				form.getDefaultSiteLocation());

		if (errors.size() == 0) {
			UserSiteInfoValueObject site = new UserSiteInfoValueObject();
			site.setDefaultSiteLocation(SiteLocation.valueOf(form.getDefaultSiteLocation()));
			site.setProducerLicence(form.getProducerLicense());
			 
			try {
				BDUserProfile user = BDSessionHelper.getUserProfile(request);
				BDUserSecurityServiceDelegate.getInstance().updateInternalUserProfile(user.getBDPrincipal(),
						form.getCurrentPassword(), form.getNewPassword(), site, IPAddressUtils.getRemoteIpAddress(request),  request.getSession(false).getId());
				// update the user profile for the current session
				user.getBDPrincipal().setProducerLicense(site.getProducerLicence());
				user.setDefaultFundListing(site.getDefaultSiteLocation());
				ServletContext context = request.getServletContext();
				UserNavigationFactory.getInstance(context).updateNavigation(request, context);
				// clear password fields
				form.setCurrentPassword(null);
				form.setNewPassword(null);
				form.setConfirmedPassword(null);
			} catch (SecurityServiceException e) {
				logger.debug("Update profile fails", e);
				errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e,
						MyProfileUtil.MyProfileSecurityServiceExceptionMapping)));
			}
		}
		if (errors.isEmpty()) {
			form.setSuccess(true);
		} else {
			form.setChanged(true);
			setErrorsInRequest(request, errors);
		}
		populateForm(form, request, false);
		return forwards.get("input");
	}

	private void populateForm(AutoForm actionForm, HttpServletRequest request, boolean initial) {
		MyProfileInternalForm form = (MyProfileInternalForm) actionForm;

		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		BDPrincipal principal = userProfile.getBDPrincipal();
		form.setFirstName(principal.getFirstName());
		form.setLastName(principal.getLastName());
		form.setEmailAddress(principal.getEmailAddress());
		if (initial) {
			form.setProducerLicense(userProfile.getBDPrincipal().getProducerLicense());
			if (form.getProducerLicense() == null) {
				form.setLicenseWarning(true);
			}
			SiteLocation site = userProfile.getDefaultFundListing();
			form.setDefaultSiteLocation(site == null ? "" : site.toString());
		}
	}

	/**
	 * Method to check whether all passwords are present
	 * 
	 * @param form
	 * @return boolean
	 */
	private boolean allPasswordsPresent(MyProfileInternalForm form) {
		return (StringUtils.isNotEmpty(form.getCurrentPassword()) && StringUtils.isNotEmpty(form.getNewPassword())
				&& StringUtils.isNotEmpty(form.getConfirmedPassword()));
	}

	/**
	 * Method to check whether all passwords are empty
	 * 
	 * @param form
	 * @return boolean
	 */
	private boolean allPasswordsEmpty(MyProfileInternalForm form) {
		return (StringUtils.isEmpty(form.getCurrentPassword()) && StringUtils.isEmpty(form.getNewPassword())
				&& StringUtils.isEmpty(form.getConfirmedPassword()));
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
	
	@RequestMapping(value ="/internal/ajaxvalidator/" ,method =  {RequestMethod.POST})
	@ResponseBody
	public String doDeApiAjaxCall(@Valid @ModelAttribute("myProfileInternalForm") MyProfileInternalForm form, BindingResult bindingResult,
           HttpServletRequest request, HttpServletResponse response) throws ServletException, SystemException,SecurityServiceException, IOException{
		
		BDUserProfile userProfileDetails = BDSessionHelper.getUserProfile(request);
      //Password Meter Validation DeApi Call and and Passing Boolean value as True for only FRW External Users to validate
       PasswordMeterUtility.validatePasswordMeter(request,response,userProfileDetails.getBDPrincipal().getUserName().toString(),Boolean.FALSE);
		return null;
		}
	
	private void getPasswordScore(String responseText, Collection errors,HttpServletRequest request) {
		int score = 0;
		String deApiStatus = null;
		JsonElement jsonElement = null;
		final int passwordScore = 2;
		if (null != responseText) {
			jsonElement = new JsonParser().parse(responseText);
		}
		JsonObject jsonObject = null;
		if (null != jsonElement) {
			jsonObject = jsonElement.getAsJsonObject();
		}
		if (null != jsonObject && null != jsonObject.get("Deapi")) {
			deApiStatus = jsonObject.get("Deapi").getAsString();
		}
		if (null != deApiStatus && !deApiStatus.isEmpty() && deApiStatus.equalsIgnoreCase("down")) {
			request.getSession(false).setAttribute("Deapi", "down");
		} else{
		if (null != jsonObject && null != jsonObject.get("score")) {
			score = jsonObject.get("score").getAsInt();
		}
		request.getSession(false).setAttribute("Deapi", "up");
		if (score < passwordScore) {
			errors.add(new ValidationError("", CommonErrorCodes.PASSWORD_FAILS_STANDARDS));
		}
		
	}
	
	}
}
