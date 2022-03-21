package com.manulife.pension.bd.web.registration;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.messagecenter.BDMessageCenterConstants;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.registration.util.PasswordChallengeInput;
import com.manulife.pension.bd.web.registration.util.RegistrationUtils;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.bd.web.validation.rules.AddressRule;
import com.manulife.pension.bd.web.validation.rules.PasswordChallengeInputRule;
import com.manulife.pension.bd.web.validation.rules.PhoneNumberRule;
import com.manulife.pension.delegate.BDPublicSecurityServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.util.PasswordMeterUtility;
import com.manulife.pension.platform.web.validation.rules.NewPasswordRule;
import com.manulife.pension.platform.web.validation.rules.UserNameRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.service.security.bd.valueobject.BrokerAssistantCreationValueObject;
import com.manulife.pension.service.security.bd.valueobject.BrokerAssistantRegistrationValueObject;
import com.manulife.pension.service.security.bd.valueobject.UserSecurityValueObject;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject.SiteLocation;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;

/**
 * This is the action class for the second step of BrokerAssistant registration.
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping( value = "/registerBrokerAssistant")

public class RegisterBrokerAssistantStep2Controller extends BDPublicWizardProcessController {
	private String userName = null;
	@ModelAttribute("registerBrokerAssistantStep2Form") 
	public RegisterBrokerAssistantStep2Form populateForm() 
	{
		return new RegisterBrokerAssistantStep2Form();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
    private static final String FAIL = "fail";
    private static final String INPUT = "input";
	static {
		forwards.put(INPUT, "/registration/registerBrokerAssistantStep2.jsp");
		forwards.put(FAIL, "/registration/registerBrokerAssistantStep2.jsp");
	}

    private final MandatoryRule pwdMandatoryRule = new MandatoryRule(
            CommonErrorCodes.EMPTY_PASSWORD);

    private final MandatoryRule confirmedPwdMandatoryRule = new MandatoryRule(
            CommonErrorCodes.CONFIRM_PASSWORD_MANDATORY);

    private final MandatoryRule licenseMandatoryRule = new MandatoryRule(
            BDErrorCodes.LICENSE_VERIFICATION_NOT_SELECTED);

    private final MandatoryRule fundListingMandatoryRule = new MandatoryRule(
            BDErrorCodes.FUND_LISTING_NOT_SELECTED);

    /**
     * Constructor
     */
    public RegisterBrokerAssistantStep2Controller() {
        super(RegisterBrokerAssistantStep2Controller.class,
                RegisterBrokerAssistantProcessContext.class,
                RegisterBrokerAssistantProcessContext.ProcessName,
                RegisterBrokerAssistantProcessContext.Step2);
    }


    /* (non-Javadoc)
     * @see com.manulife.pension.bd.web.process.BDWizardProcessController#doContinue(com.manulife.pension.ezk.web.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ProcessState doContinue( ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doContinue");
        }
        RegisterBrokerAssistantStep2Form registerBrokerAssistantForm = (RegisterBrokerAssistantStep2Form) form;
        List<ValidationError> errors = new ArrayList<ValidationError>();
        errors.addAll(validate(registerBrokerAssistantForm,request));
        String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
        String username ;
		if (null != registerBrokerAssistantForm.getUserCredential()
				&& registerBrokerAssistantForm.getUserCredential().getUserId() != null) {
			username = registerBrokerAssistantForm.getUserCredential().getUserId();
		}else{
        	username = userName;
        }
        /*
		 *  This method is to validate only for FRW External users to check following validations.
		 *  1. Any consecutive and sequential characters present in the password
		 *  2. Check user name and password Sequence
		 */
        if (errors.isEmpty()) {
		PasswordMeterUtility.validateSeqConsCharForExUsers(username,
				registerBrokerAssistantForm.getUserCredential().getPassword(), errors);
        } else {
        	// This condition is used to override the error code if logged in user is FRW External User
			for (int index = 0; index < errors.size(); index++) {
				if (errors.get(index).getErrorCode() == CommonErrorCodes.PASSWORD_FAILS_STANDARDS){
				errors.remove(errors.get(index));
				errors.add(new ValidationError("", CommonErrorCodes.FRW_EXTERNAL_USER_PASSWORD_FAILS_STANDARDS));
			}
		}
        }
        if (errors.size() == 0) {
            RegisterBrokerAssistantProcessContext context = (RegisterBrokerAssistantProcessContext) getProcessContext(request);
            BrokerAssistantRegistrationValueObject regVO = createRegistrationVO((RegisterBrokerAssistantStep2Form) form);
            BrokerAssistantCreationValueObject creationVO = context.getCreationVO();
            regVO.setFirstName(creationVO.getFirstName());
            regVO.setLastName(creationVO.getLastName());
            regVO.setEmailAddress(creationVO.getEmailAddress());
            regVO.setUserProfileId(context.getSecurityRequest().getUserProfileId());
            try {
                BDPublicSecurityServiceDelegate.getInstance().registerBrokerAssistant(regVO, ipAddress, request.getSession(false).getId());
                context.setUserName(regVO.getSecurityVO().getUserName());
                context.setPassword(regVO.getSecurityVO().getPassword());
            } catch (SecurityServiceException e) {
                logger.debug("Broker Assistant Registration Failed. ", e);
                errors
                        .add(new ValidationError(
                                "",
                                SecurityServiceExceptionHelper
                                        .getErrorCodeForCause(
                                                e,
                                                RegisterBrokerAssistantProcessContext.ProcessSecurityServiceExceptionMapping)));
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doContinue");
        }
        if (errors.size() == 0) {
            return getState().getNext(BDWizardProcessContext.ACTION_CONTINUE);
        } else {
            setErrorsInSession(request, errors);
            return getState();
        }
    }

    /**
     * Helper method to create the BrokerAssistantRegistrationValueObject from the form object
     * 
     * @param form
     * @return BrokerAssistantRegistrationValueObject
     */
    private BrokerAssistantRegistrationValueObject createRegistrationVO(
            RegisterBrokerAssistantStep2Form form) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> createRegistrationVO");
        }
        BrokerAssistantRegistrationValueObject vo = new BrokerAssistantRegistrationValueObject();
        UserSecurityValueObject securityVO = RegistrationUtils.getSecurityVO(form
                .getUserCredential(), form.getChallenge1(), form.getChallenge2());
        vo.setSecurityVO(securityVO);
        UserSiteInfoValueObject siteInfoVO = new UserSiteInfoValueObject();
        siteInfoVO.setAcceptDisclaimer(form.getAcceptDisclaimer());
        siteInfoVO.setProducerLicence(form.getProducerLicense());
        siteInfoVO.setDefaultSiteLocation(SiteLocation.valueOf(form.getDefaultSiteLocation()));
        vo.setSiteInfoVO(siteInfoVO);
        vo.setAddress(RegistrationUtils.populateBDAddressVO(form.getAddress()));
        vo.setPhoneNumber(form.getPhoneNumber().getValue());
        vo.setEmailNotification(form.getEmailNotification());
        vo
				.setSummaryEmailPref(BDMessageCenterConstants.ReceiveSummaryEmailPreference);
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> createRegistrationVO");
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
    private List<ValidationError> validate(RegisterBrokerAssistantStep2Form form,HttpServletRequest request)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> validate");
        }
        List<ValidationError> errors = new ArrayList<ValidationError>();
        AddressRule.getInstance().validate(RegisterBrokerAssistantStep2Form.FIELD_ADDRESS, errors,
                form.getAddress());
        PhoneNumberRule.getInstance().validate(RegisterBrokerAssistantStep2Form.FIELD_PHONE_NUMBER,
                errors, form.getPhoneNumber());
        // User Name mandatory and standards must be met
        UserNameRule.getInstance().validate(RegisterBrokerAssistantStep2Form.FIELD_USER_ID, errors,
                form.getUserCredential().getUserId());
        // Password mandatory and standards must be met
        pwdMandatoryRule.validate(RegisterBrokerAssistantStep2Form.FIELD_PASSWORD, errors, form
                .getUserCredential().getPassword());
        // Confirm Password mandatory and standards must be met
        confirmedPwdMandatoryRule.validate(
                RegisterBrokerAssistantStep2Form.FIELD_CONFIRMED_PASSWORD, errors, form
                        .getUserCredential().getConfirmedPassword());
        // Changes for defect 8589 , 8590
       
        if (StringUtils.isNotEmpty(form.getUserCredential().getPassword())
                && StringUtils.isNotEmpty(form.getUserCredential().getConfirmedPassword())) {
            Pair<String, String> passwordPair = new Pair<String, String>(form.getUserCredential()
                    .getPassword(), form.getUserCredential().getConfirmedPassword());
            NewPasswordRule.getInstance().validate(RegisterBrokerAssistantStep2Form.FIELD_PASSWORD,
                    errors, passwordPair);
        }
        Pair<PasswordChallengeInput, PasswordChallengeInput> challengePair = new Pair<PasswordChallengeInput, PasswordChallengeInput>(
                form.getChallenge1(), form.getChallenge2());
        PasswordChallengeInputRule.getInstance().validate(
                RegisterBrokerAssistantStep2Form.FIELD_CHALLENGE1, errors, challengePair);

        licenseMandatoryRule.validate(RegisterBrokerAssistantStep2Form.FIELD_PRODUCER_LICENSE,
                errors, form.getProducerLicense());

        fundListingMandatoryRule.validate(
                RegisterBrokerAssistantStep2Form.FIELD_DEFAULT_SITE_LOCATION, errors, form
                        .getDefaultSiteLocation());

        if (!form.getAcceptDisclaimer()) {
            errors.add(new ValidationError(
                    RegisterBrokerAssistantStep2Form.FIELD_ACCEPT_DISCLAIMER,
                    BDErrorCodes.TERMS_AND_CONDITIONS_NOT_CHECKED));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> validate");
        }
        return errors;
	}

	
	  
	/**
	 * @param form
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/step2", method = { RequestMethod.GET })
	protected String doInput(
			@Valid @ModelAttribute("registerBrokerAssistantStep2Form") RegisterBrokerAssistantStep2Form form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(FAIL);// if input forward not //available, provided default
			}
		}

		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		String forward = super.doExecuteInput(request);

		if (forward == null) {
			String forward1 = super.doInput(form, request, response);
			return StringUtils.contains(forward1, '/') ? forward1 : forwards.get(forward1);
		}
		return forward;
	}

	/**
	 * @param form
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/step2", params = { "action=continue" }, method = { RequestMethod.POST })
	protected String doContinue(
			@Valid @ModelAttribute("registerBrokerAssistantStep2Form") RegisterBrokerAssistantStep2Form form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(FAIL);// if input forward not //available, provided default
			}
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		String forward = super.doCancelContinue(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
		
		
		
	/**
	 * @param form
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/step2", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(
			@Valid @ModelAttribute("registerBrokerAssistantStep2Form") RegisterBrokerAssistantStep2Form form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(FAIL);// if input forward not //available, provided default
			}
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}

		String forward = super.doCancelContinue(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
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
	
	@RequestMapping(value ="/step2/ajaxvalidator/" ,method =  {RequestMethod.POST})
	@ResponseBody
	public String doDeAPIAjaxCall(@Valid @ModelAttribute("registerBrokerAssistantStep2Form") RegisterBrokerAssistantStep2Form form, BindingResult bindingResult,
           HttpServletRequest request, HttpServletResponse response) throws ServletException, SystemException,SecurityServiceException, IOException{
		final String jsonObj = IOUtils.toString(request.getInputStream());
		String jsonText2 = "[" + jsonObj + "]";
		JsonParser parser = new JsonParser();
		JsonArray jsonArr = (JsonArray) parser.parse(jsonText2);
		String newpassword  = null;
		if (StringUtils.isNotEmpty(jsonObj)) {
			for (int i = 0; i < jsonArr.size(); i++) {

				JsonObject obj = (JsonObject) jsonArr.get(i);

			newpassword = (obj.get("newPassword")).getAsString();
			userName = (obj.get("userName")).getAsString();
			}
		}
		String password  = newpassword;
		SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
		// Passing Boolean value as True for only FRW External Users to validate
		String errors = service.passwordStrengthValidation(password,userName,Boolean.TRUE);
		String responseText = errors;
			response.setContentLength(responseText.length());
    	response.setContentType("application/json");
    	PrintWriter out = response.getWriter();
        out.print(responseText);
        out.flush();
		return null;
		}
}
