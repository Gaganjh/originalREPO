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
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.registration.util.PasswordChallengeInput;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.bd.web.validation.rules.PasswordChallengeInputRule;
import com.manulife.pension.delegate.BDPublicSecurityServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.util.PasswordMeterUtility;
import com.manulife.pension.platform.web.validation.rules.NewPasswordRule;
import com.manulife.pension.platform.web.validation.rules.UserNameRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.service.security.bd.valueobject.BrokerRegistrationValueObject;
import com.manulife.pension.service.security.bd.valueobject.PasswordChallenge;
import com.manulife.pension.service.security.bd.valueobject.UserRegistrationValueObject;
import com.manulife.pension.service.security.bd.valueobject.UserSecurityValueObject;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject.SiteLocation;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;

/**
 * This is the action class for step 2 of Broker Registration
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping( value = "/registerExternalBroker")

public class RegisterBrokerStep2Controller extends BDPublicWizardProcessController {
	private String userName = null;
	@ModelAttribute("registerBrokerValidationForm") 
	public RegisterBrokerValidationForm populateForm() 
	{
		return new RegisterBrokerValidationForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/registration/registerBrokerStep2.jsp");
		forwards.put("fail","/registration/registerBrokerStep2.jsp");
		}
	private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static String HomeRedirect=HomeRedirect1.getPath();
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
    public RegisterBrokerStep2Controller() {
        super(RegisterBrokerStep2Controller.class, RegisterBrokerProcessContext.class,
                RegisterBrokerProcessContext.ProcessName, RegisterBrokerProcessContext.Step2);
    }

    /**
     * This method will take the user to the next step
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ProcessState a ProcessState object that represents the next step
     * @throws SystemException
     */
    @Override
    protected ProcessState doContinue( ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doContinue");
        }
        String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
        RegisterBrokerStep2Form registerBrokerStep2Form = (RegisterBrokerStep2Form) form;
        List<ValidationError> errors = new ArrayList<ValidationError>();
        errors.addAll(validate(registerBrokerStep2Form,request));
        String username ;
		if (null != registerBrokerStep2Form.getUserCredential()
				&& registerBrokerStep2Form.getUserCredential().getUserId() != null) {
			username = registerBrokerStep2Form.getUserCredential().getUserId();
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
				registerBrokerStep2Form.getUserCredential().getPassword(), errors);
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
            RegisterBrokerProcessContext context = (RegisterBrokerProcessContext) getProcessContext(request);
            BrokerRegistrationValueObject registrationVO = (BrokerRegistrationValueObject) createRegistrationVO(
                    registerBrokerStep2Form, context);
            //registrationVO.setIpAddress(ipAddress);
            try {
                BDPublicSecurityServiceDelegate.getInstance().registerBroker(registrationVO, ipAddress, request.getSession(false).getId());
            } catch (SecurityServiceException e) {
                logger.debug("Broker Registration Failed. ", e);
                errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCodeForCause(e)));
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
     * Helper method to create the BrokerRegistrationValueObject from the form object
     * 
     * @param form
     * @return
     */
    private UserRegistrationValueObject createRegistrationVO(RegisterBrokerStep2Form form,
            RegisterBrokerProcessContext context) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> createRegistrationVO");
        }
        BrokerRegistrationValueObject registrationVO = context.getBrokerRegistrationVO();
        UserSecurityValueObject securityVO = new UserSecurityValueObject();
        securityVO.setUserName(form.getUserCredential().getUserId());
        securityVO.setPassword(form.getUserCredential().getPassword());
        PasswordChallenge challenge1 = new PasswordChallenge(
                form.getChallenge1().getQuestionText(), form.getChallenge1().getAnswer());
        PasswordChallenge challenge2 = new PasswordChallenge(
                form.getChallenge2().getQuestionText(), form.getChallenge2().getAnswer());
        securityVO.setChallenges(new PasswordChallenge[] { challenge1, challenge2 });
        registrationVO.setSecurityVO(securityVO);
        UserSiteInfoValueObject siteInfoVO = new UserSiteInfoValueObject();
        siteInfoVO.setAcceptDisclaimer(form.getAcceptDisclaimer());
        siteInfoVO.setProducerLicence(form.getProducerLicense());
        siteInfoVO.setDefaultSiteLocation(SiteLocation.valueOf(form.getDefaultSiteLocation()));
        registrationVO.setSiteInfoVO(siteInfoVO);
        registrationVO.setEmailNotification(form.getEmailNotification());
        registrationVO
				.setSummaryEmailPref(BDMessageCenterConstants.ReceiveSummaryEmailPreference);
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> createRegistrationVO");
        }
        return registrationVO;
    }

    /**
     * The validation methods for the form
     * 
     * @param form
     * @return List<ValidationError>
     * @throws SystemException
     */
    private List<ValidationError> validate(RegisterBrokerStep2Form form, HttpServletRequest request) throws SystemException {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> validate");
        }
        // User Name mandatory and standards must be met
        UserNameRule.getInstance().validate(RegisterBrokerStep2Form.FIELD_USER_ID, errors,
                form.getUserCredential().getUserId());
        // Password mandatory and standards must be met
        pwdMandatoryRule.validate(RegisterBrokerStep2Form.FIELD_PASSWORD, errors, form
                .getUserCredential().getPassword());
        // Confirm Password mandatory and standards must be met
        confirmedPwdMandatoryRule.validate(RegisterBrokerStep2Form.FIELD_CONFIRMED_PASSWORD,
                errors, form.getUserCredential().getConfirmedPassword());
        // Changes for defect 8589 , 8590
        
        if (StringUtils.isNotEmpty(form.getUserCredential().getPassword())
                && StringUtils.isNotEmpty(form.getUserCredential().getConfirmedPassword())) {
            Pair<String, String> passwordPair = new Pair<String, String>(form.getUserCredential()
                    .getPassword(), form.getUserCredential().getConfirmedPassword());
            NewPasswordRule.getInstance().validate(RegisterBrokerStep2Form.FIELD_PASSWORD, errors,
                    passwordPair);
        }
        Pair<PasswordChallengeInput, PasswordChallengeInput> challengePair = new Pair<PasswordChallengeInput, PasswordChallengeInput>(
                form.getChallenge1(), form.getChallenge2());
        PasswordChallengeInputRule.getInstance().validate(RegisterBrokerStep2Form.FIELD_CHALLENGE,
                errors, challengePair);
        licenseMandatoryRule.validate(RegisterBrokerStep2Form.FIELD_PRODUCER_LICENSE, errors, form
                .getProducerLicense());
        fundListingMandatoryRule.validate(RegisterBrokerStep2Form.FIELD_DEFAULT_SITE_LOCATION,
                errors, form.getDefaultSiteLocation());
        if (form.getAcceptDisclaimer() == null) {
            errors.add(new ValidationError(RegisterBrokerStep2Form.FIELD_ACCEPT_DISCLAIMER,
                    BDErrorCodes.TERMS_AND_CONDITIONS_NOT_CHECKED));
        }
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
		
		@RequestMapping(value = "/broker/step2", method = { RequestMethod.GET })
		protected String doInput(
				@Valid @ModelAttribute("registerBrokerStep2Form") RegisterBrokerStep2Form form,
				BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
				throws SystemException, IOException, ServletException {
			if (logger.isDebugEnabled()) {
				logger.debug("entry -> doInput");
			}

			if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					return forwards.get("fail");// if input forward not //available, provided default
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

		@RequestMapping(value = "/broker/step2", params = { "action=continue" }, method = { RequestMethod.POST })
		protected String doContinue(
				@Valid @ModelAttribute("registerBrokerStep2Form") RegisterBrokerStep2Form form,
				BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
				throws SystemException, IOException, ServletException {
			if (logger.isDebugEnabled()) {
				logger.debug("entry -> doInput");
			}
			if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					return forwards.get("fail");// if input forward not //available, provided default
				}
			}
			if (BDSessionHelper.getUserProfile(request) != null) {
				return HomeRedirect;
			}
			String forward = super.doCancelContinue(form, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}	
		@RequestMapping(value = "/broker/step2", params = { "action=cancel" }, method = { RequestMethod.POST })
		protected String doCancel(
				@Valid @ModelAttribute("registerBrokerStep2Form") RegisterBrokerStep2Form form,
				BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
				throws SystemException, IOException, ServletException {
			if (logger.isDebugEnabled()) {
				logger.debug("entry -> doInput");
			}
			if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					return forwards.get("fail");// if input forward not //available, provided default
				}
			}
			if (BDSessionHelper.getUserProfile(request) != null) {
				return HomeRedirect;
			}
			String forward = super.doCancelContinue(form, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}	
		
		@RequestMapping(value ="/broker/step2/ajaxvalidator/" ,method =  {RequestMethod.POST})
		@ResponseBody
		public String doDeAPIAjaxCall(@Valid @ModelAttribute("registerBrokerStep2Form") RegisterBrokerStep2Form form, BindingResult bindingResult,
	           HttpServletRequest request, HttpServletResponse response) throws ServletException, SystemException,SecurityServiceException, IOException{
			final String jsonObj = IOUtils.toString(request.getInputStream());
			String jsonText2 = "[" + jsonObj + "]";
			JsonParser parser = new JsonParser();
			JsonArray jsonArr = (JsonArray) parser.parse(jsonText2);
			String newpassword  = null;
			//String userName = null;
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
