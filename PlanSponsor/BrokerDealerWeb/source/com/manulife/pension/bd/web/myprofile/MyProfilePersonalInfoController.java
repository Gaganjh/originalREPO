package com.manulife.pension.bd.web.myprofile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.navigation.UserNavigationFactory;
import com.manulife.pension.bd.web.registration.util.RegistrationUtils;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.bd.web.validation.rules.AddressRule;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.bd.web.validation.rules.EmailAddressRule;
import com.manulife.pension.bd.web.validation.rules.PhoneNumberRule;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBasicBrokerUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerAssistantUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedFirmRepUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedRiaUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExternalUserPersonalUpdateVO;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;

/**
 * This is the action class for MyProfile Personal Info tab.
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping(value ="/myprofile")
@SessionAttributes({"myprofilePersonalInfoForm"})

public class MyProfilePersonalInfoController extends BaseAutoController {
	@ModelAttribute("myprofilePersonalInfoForm") 
	public MyProfilePersonalInfoForm populateForm() 
	{
		return new MyProfilePersonalInfoForm();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static {
		forwards.put("input", "/myprofile/personalInfo.jsp");
		forwards.put("cancel", "redirect:/do/home/");
		forwards.put("fail", "/myprofile/personalInfo.jsp");
		forwards.put("myprofileDispatch", "redirect:/do/myprofileDispatch/");
	}

    private static final String CANCEL_FORWARD = "cancel";

    private final MandatoryRule firstNameMandatoryRule = new MandatoryRule(
            BDErrorCodes.MISSING_FIRST_NAME);

    private final MandatoryRule lastNameMandatoryRule = new MandatoryRule(
            BDErrorCodes.MISSING_LAST_NAME);
    
    private static final RegularExpressionRule invalidFirstAndLastNameRErule = new RegularExpressionRule(
            BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);

    private static final RegularExpressionRule invalidValueRErule = new RegularExpressionRule(
            BDErrorCodes.PERSONAL_INFO_INVALID_VALUE, BDRuleConstants.INVALID_VALUE_RE);
    
    private static final String FIRST_NAME_LABEL = "First Name";

    private static final String LAST_NAME_LABEL = "Last Name";

    private static final String COMPANY_NAME_LABEL = "Company Name";
    
    private static final String RIA_EMAIL_EXISTS = "exists";
    
    private static final String RIA_EMAIL_NOT_EXISTS = "notexists";
    

    /**
     * Constructor
     */
    public MyProfilePersonalInfoController() {
        super(MyProfilePersonalInfoController.class);
    }

   
    /**
     * This method will be called if the action parameter is default or null. This will retrieve the
     * user personal information from the database.
     * 
      
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return ActionForward The forward to process.
     * @throws IOException When an IO problem occurs.
     * @throws ServletException When an Servlet problem occurs.
     * @throws SystemException When an generic application problem occurs.
     */ 
    
	@RequestMapping(value = "/personalInfo", method = { RequestMethod.GET })
	public String doDefault(
			@Valid @ModelAttribute("myprofilePersonalInfoForm") MyProfilePersonalInfoForm personalInfoForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");
			}
		}
		//if user is bookmarked the URL, we still need to challenge.
    	if(Objects.nonNull(request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND))) {
    		if((boolean)request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND)) {
    			request.getSession().setAttribute("myProfileCurrentTab", MyProfileNavigation.PersonalInfoTabId);
    			return forwards.get("myprofileDispatch");
    		}
    	}
		try {
			MyProfileContext context = MyProfileUtil.getContext(request.getServletContext(), request);
			context.getNavigation().setCurrentTabId(MyProfileNavigation.PersonalInfoTabId);
			BDPrincipal user = BDSessionHelper.getUserProfile(request).getBDPrincipal();
			ExtendedBDExtUserProfile profile = BDUserSecurityServiceDelegate.getInstance()
					.getExtendedBDExtUserProfile(user);
			if (profile.getRoleType().compareTo(BDUserRoleType.BasicFinancialRep) == 0) {
				ExtendedBasicBrokerUserProfile basicBrokerUserProfile = (ExtendedBasicBrokerUserProfile) profile;
				personalInfoForm.setExternalUserProfile(basicBrokerUserProfile);
			} else if (profile.getRoleType().compareTo(BDUserRoleType.FinancialRepAssistant) == 0) {
				ExtendedBrokerAssistantUserProfile finRepAssistantUserProfile = (ExtendedBrokerAssistantUserProfile) profile;
				personalInfoForm.setExternalUserProfile(finRepAssistantUserProfile);
				personalInfoForm.setFinancialRepName(finRepAssistantUserProfile.getParentBroker().getFirstName() + " "
						+ finRepAssistantUserProfile.getParentBroker().getLastName());
			} else if (profile.getRoleType().compareTo(BDUserRoleType.FirmRep) == 0) {
				ExtendedFirmRepUserProfile firmRepUserProfile = (ExtendedFirmRepUserProfile) profile;
				personalInfoForm.setExternalUserProfile(firmRepUserProfile);
			} else if (profile.getRoleType().compareTo(BDUserRoleType.RIAUser) == 0) {
				ExtendedRiaUserProfile riaUserProfile = (ExtendedRiaUserProfile) profile;
				personalInfoForm.setExternalUserProfile(riaUserProfile);
			}
			personalInfoForm.setRoleId(profile.getRoleType().getRoleId());
			personalInfoForm.setAddress(MyProfileUtil.populateAddressVO(profile.getAddress()));
			personalInfoForm.setPhoneNumber(MyProfileUtil.populatePhoneNumberVO(profile.getPhoneNum()));
			// we have to reset these flags since the form is in session
			personalInfoForm.setChanged(false);
			personalInfoForm.setSuccess(false);
		} catch (SecurityServiceException sse) {
			logger.debug("Retrieving Personal Info Failed. ", sse);
			List<ValidationError> errors = new ArrayList<ValidationError>(1);
			errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(sse,
					MyProfileUtil.MyProfileSecurityServiceExceptionMapping)));
			setErrorsInSession(request, errors);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doDefault");
		}
		return forwards.get("input");
	}
    
   	/**
	 * This method is used to check the existence of email
	 * 
	 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/personalInfo", params = { "action=checkDuplicateEmail" }, method = { RequestMethod.POST })
	public String doCheckDuplicateEmail(
			@Valid @ModelAttribute("myprofilePersonalInfoForm") MyProfilePersonalInfoForm personalInfoForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCheckDuplicateEmail() in MyProfilePersonalInfoAction");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");
			}
		}
		MyProfileContext context = MyProfileUtil.getContext(request.getServletContext(), request);
		context.getNavigation().setCurrentTabId(MyProfileNavigation.PersonalInfoTabId);
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

    /**
     * This method will be called if the action parameter is save. This will save the external user
     * personal info to database
     * 
     
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws SystemException
     */
	@RequestMapping(value = "/personalInfo", params = { "action=save" }, method = { RequestMethod.POST })
	public String doSave(@Valid @ModelAttribute("myprofilePersonalInfoForm") MyProfilePersonalInfoForm personalInfoForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSave");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");
			}
		}
		MyProfileContext profileContext = MyProfileUtil.getContext(request.getServletContext(), request);
		profileContext.getNavigation().setCurrentTabId(MyProfileNavigation.PersonalInfoTabId);
		personalInfoForm.getExternalUserProfile().setPhoneNum(personalInfoForm.getPhoneNumber().getValue());
		List<ValidationError> errors = validate(personalInfoForm);
		if (errors.size() == 0) {
			try {
				BDPrincipal principal = BDSessionHelper.getUserProfile(request).getBDPrincipal();
				BDUserSecurityServiceDelegate.getInstance().updateExternalUserPersonalInfo(principal,
						getUpdate(personalInfoForm), IPAddressUtils.getRemoteIpAddress(request));
				principal.setFirstName(personalInfoForm.getExternalUserProfile().getFirstName());
				principal.setLastName(personalInfoForm.getExternalUserProfile().getLastName());
				ServletContext context = request.getServletContext();
				UserNavigationFactory.getInstance(context).updateNavigation(request, context);
			} catch (SecurityServiceException e) {
				logger.debug("Fail to update the personal information", e);
				errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e,
						MyProfileUtil.MyProfileSecurityServiceExceptionMapping)));
			}
		}
		if (errors.size() > 0) {
			setErrorsInSession(request, errors);
			personalInfoForm.setChanged(true);
			personalInfoForm.setSuccess(false);
		} else {
			personalInfoForm.setChanged(false);
			personalInfoForm.setSuccess(true);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doSave");
		}
		return forwards.get("input");
	}
    
    /**
     * This method will be called when the action parameter is cancel. This will forward the user to
     * secure home page.
     * 
     
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
	@RequestMapping(value = "/personalInfo", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(
			@Valid @ModelAttribute("myprofilePersonalInfoForm") MyProfilePersonalInfoForm personalInfoForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCancel");
		}
		MyProfileContext context = MyProfileUtil.getContext(request.getServletContext(), request);
		context.getNavigation().setCurrentTabId(MyProfileNavigation.PersonalInfoTabId);
		return forwards.get(CANCEL_FORWARD);
	}

    
    /**
     * Helper method to create ExternalUserPersonalUpdateVO
     * 
     * @param personalInfoForm
     * @return ExternalUserPersonalUpdateVO
     */
    private ExternalUserPersonalUpdateVO getUpdate(MyProfilePersonalInfoForm personalInfoForm) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getUpdate");
        }
        ExternalUserPersonalUpdateVO vo = new ExternalUserPersonalUpdateVO();
        ExtendedBDExtUserProfile profile = personalInfoForm.getExternalUserProfile();
        vo.setFirstName(profile.getFirstName());
        vo.setLastName(profile.getLastName());
        vo.setAddress(RegistrationUtils.populateBDAddressVO(personalInfoForm.getAddress()));
        vo.setPhoneNumber(personalInfoForm.getPhoneNumber().getValue());
        vo.setEmailAddress(profile.getEmailAddress());
        if (profile instanceof ExtendedBasicBrokerUserProfile) {
            vo.setCompanyName(((ExtendedBasicBrokerUserProfile) profile).getCompanyName());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getUpdate");
        }
        return vo;
    }
    /**
     * The validation methods for the form
     * 
     * @param form
     * @return List<ValidationError>
     * @throws SystemException
     */
    private List<ValidationError> validate(MyProfilePersonalInfoForm form) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> validate");
        }
        List<ValidationError> errors = new ArrayList<ValidationError>();
        ExtendedBDExtUserProfile profile = form.getExternalUserProfile();
        List<ValidationError> tempArrayList = new ArrayList<ValidationError>();
        
        boolean isValid = false;
        isValid = firstNameMandatoryRule.validate(MyProfilePersonalInfoForm.FIELD_FIRST_NAME,
                errors, profile.getFirstName());

        if (isValid) {
            isValid = invalidFirstAndLastNameRErule.validate(MyProfilePersonalInfoForm.FIELD_FIRST_NAME,
                    tempArrayList, profile.getFirstName());
            if (!isValid) {
                errors
                        .add(new ValidationError(MyProfilePersonalInfoForm.FIELD_FIRST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { FIRST_NAME_LABEL }));
            }
        }

        isValid = lastNameMandatoryRule.validate(MyProfilePersonalInfoForm.FIELD_LAST_NAME, errors,
                profile.getLastName());
        if (isValid) {
            isValid = invalidFirstAndLastNameRErule.validate(MyProfilePersonalInfoForm.FIELD_LAST_NAME,
                    tempArrayList, profile.getLastName());
            if (!isValid) {
                errors
                        .add(new ValidationError(MyProfilePersonalInfoForm.FIELD_LAST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { LAST_NAME_LABEL }));
            }
        }
        if (profile.getRoleType().compareTo(BDUserRoleType.BasicFinancialRep) == 0) {
            ExtendedBasicBrokerUserProfile basicBrokerProfile = (ExtendedBasicBrokerUserProfile) profile;
            if (StringUtils.isNotEmpty(basicBrokerProfile.getCompanyName())
                    && !invalidValueRErule.validate(MyProfilePersonalInfoForm.FIELD_COMPANY_NAME,
                            tempArrayList, basicBrokerProfile.getCompanyName())) {
                errors.add(new ValidationError(MyProfilePersonalInfoForm.FIELD_COMPANY_NAME,
                        BDErrorCodes.PERSONAL_INFO_INVALID_VALUE,
                        new Object[] { COMPANY_NAME_LABEL }));
            }
        }

        EmailAddressRule.getInstance().validate(MyProfilePersonalInfoForm.FIELD_EMAIL, errors,
                profile.getEmailAddress());
        AddressRule.getInstance().validate(MyProfilePersonalInfoForm.FIELD_ADDRESS, errors,
                form.getAddress());
        PhoneNumberRule.getInstance().validate(MyProfilePersonalInfoForm.FIELD_ADDRESS, errors,
                form.getPhoneNumber());
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> validate");
        }
        return errors;
	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
	@Autowired
	private BDValidatorFWFail bdValidatorFWFail;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWFail);
	}
	
	
	
}
