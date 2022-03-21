
package com.manulife.pension.bd.web.registration;

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
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.bd.web.validation.pentest.FrwValidator;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.bd.web.validation.rules.EmailAddressRule;
import com.manulife.pension.bd.web.validation.rules.PhoneNumberRule;
import com.manulife.pension.delegate.BDPublicSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.security.bd.exception.BDFailedNTimesRegistrationException;
import com.manulife.pension.service.security.bd.valueobject.RiaUserCreationValueObject;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * @author narintr
 *
 */
@Controller
@RequestMapping( value ="/registerRiaUser")

public class RegisterRiaUserStep1Controller extends BDPublicWizardProcessController  {
	@ModelAttribute("registerRiaUserStep1Form") 
	public RegisterRiaUserValidationForm populateForm()
	{
		return new RegisterRiaUserValidationForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/registration/registerRiaUserStep1.jsp");
		forwards.put("fail","/registration/registerRiaUserStep1.jsp");
		}
	private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static String HomeRedirect=HomeRedirect1.getPath();
	private final MandatoryRule firstNameMandatoryRule = new MandatoryRule(
			BDErrorCodes.MISSING_FIRST_NAME);
	
	private final MandatoryRule lastNameMandatoryRule = new MandatoryRule(
			BDErrorCodes.MISSING_LAST_NAME);
    
    private static final RegularExpressionRule invalidFirstAndLastNameValueRErule = new RegularExpressionRule(
            BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);
    
    private static final RegularExpressionRule invalidIARDRErule = new RegularExpressionRule(
            BDErrorCodes.INVALID_IARD_NUMBER, BDRuleConstants.IARD_RE);
    
    private static final String LAST_NAME_LABEL = "Last Name";
    
    private static final String FIRST_NAME_LABEL = "First Name";
    
    private static final String PHONE_NO_LABEL = "Phone Number";
    
    private final MandatoryRule iardNumberMandatoryRule = new MandatoryRule(
			BDErrorCodes.MISSING_IARD_NUMBER);
    
    private static final String FIELD_IARD_NUMBER_LABEL = "IARD Number";
        
    
	public RegisterRiaUserStep1Controller() {
		super(RegisterRiaUserStep1Controller.class,
				RegisterRiaUserProcessContext.class,
				RegisterRiaUserProcessContext.ProcessName,
				RegisterRiaUserProcessContext.Step1);
	}
	
	/**
	 * Perform penetration validation that might affect control (inner) fields,
	 * before proceeding with basic process.
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected ProcessState doProcess( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		List errors = new ArrayList<ValidationError>();

		if (FrwValidator.getInstance().validateSanitizeCatalogedFormFields(form, errors, request) == false) {
			//
			// Only Internal filed validation failure on registration page results in a
			// detailed message, in other cases generic error message is shown.
			//
			for (Object e : errors) {
				if (e instanceof GenericException) {
					//
					// Anonymous penetration error.
					//				
					if (((GenericException) e).getErrorCode() != CommonErrorCodes.ERROR_FRWVALIDATION_WITHOUT_GUI_FIELD_NAME) {
						errors.clear();
						errors.add(new GenericException(BDErrorCodes.RIAUSER_REG_VALIDATION_FAIL));
						setErrorsInRequest(request, errors);
						break;
					}
				}
			}

			RegisterRiaUserValidationForm riaValidationForm = (RegisterRiaUserValidationForm) form;
//			riaValidationForm.setChanged(true);
	        ((RegisterRiaUserProcessContext) getProcessContext(request)).updateContext(riaValidationForm);
	        setErrorsInSession(request, errors);

	        return getState();
		}
		 System.out.println("RegisterRIAStep1.doProces proceed with basic validation ");
	    return super.doProcess( form, request, response);
	}
	
	
	@RequestMapping(value = "/step1", method = { RequestMethod.GET })
	protected String doInput(@Valid @ModelAttribute("registerRiaUserStep1Form") RegisterRiaUserValidationForm form,
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

	@RequestMapping(value = "/step1", params = { "action=continue" }, method = { RequestMethod.POST })
	protected String doContinue(@Valid @ModelAttribute("registerRiaUserStep1Form") RegisterRiaUserValidationForm form,
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

	@RequestMapping(value = "/step1", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(@Valid @ModelAttribute("registerRiaUserStep1Form") RegisterRiaUserValidationForm form,
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
	protected ProcessState doContinue(
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doContinue");
		}
		RegisterRiaUserValidationForm form = (RegisterRiaUserValidationForm) actionForm;
		List<ValidationError> errors = new ArrayList<ValidationError>();
		errors.addAll(validate(form, request));
		String ipAddress = IPAddressUtils.getRemoteIpAddress(request);

		if (errors.isEmpty()) {
			RegisterRiaUserProcessContext context = (RegisterRiaUserProcessContext) getProcessContext(request);

			try {
				RiaUserCreationValueObject riaUserCreationValueObject = BDPublicSecurityServiceDelegate
						.getInstance().validateRiaUserRegInfo(
								context.getSecurityRequest().getId(),
								form.getFirstName(),
								form.getLastName(),
								form.getEmailAddress(),
								form.getPhoneNumber().getValue(),
								form.getIardNumber(),ipAddress);
				context.setRiaUserCreationValueObject(riaUserCreationValueObject);
			} catch (SecurityServiceException e) {
				logger.debug("RIA User Validation Failed. ", e);
				errors
						.add(new ValidationError(
								"",
								SecurityServiceExceptionHelper
										.getErrorCodeForCause(
												e,
												RegisterRiaUserProcessContext.ProcessSecurityServiceExceptionMapping)));
				if (e instanceof BDFailedNTimesRegistrationException) {
					context.setDisabled(true);
				}
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
	 * The validation methods for the form
	 * 
	 * @param form
	 * @return List<ValidationError>
	 * @throws SystemException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<ValidationError> validate(RegisterRiaUserValidationForm form, HttpServletRequest request)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> validate");
		}
		List errors = new ArrayList<ValidationError>();
		List tempArrayList = new ArrayList<ValidationError>();
		boolean isValid = false;
        if (FrwValidator.getInstance().validateSanitizeCatalogedFormFields(form, errors, request) == false) {
			//
			// Only Internal filed validation failure on registration page results in a
			// detailed message, in other cases generic error message is shown.
			//
			for (Object e : errors) {
				if (e instanceof GenericException) {
					//
					// Anonymous penetration error.
					//				
					if (((GenericException) e).getErrorCode() != CommonErrorCodes.ERROR_FRWVALIDATION_WITHOUT_GUI_FIELD_NAME) {
						errors.clear();
						errors.add(new GenericException(BDErrorCodes.RIAUSER_REG_VALIDATION_FAIL));
						setErrorsInRequest(request, errors);
						break;
					}
				}
			}
			if(logger.isDebugEnabled()) {
			    logger.debug("exit <- doValidate");
		    }
			return errors;
		}


		isValid = firstNameMandatoryRule.validate(
				RegisterRiaUserValidationForm.FIELD_FIRST_NAME, errors, form
						.getFirstName());
		if (isValid) {
            isValid = invalidFirstAndLastNameValueRErule.validate(RegisterRiaUserValidationForm.FIELD_FIRST_NAME,
                    tempArrayList, form.getFirstName());
            if (!isValid) {
                errors
                        .add(new ValidationError(RegisterRiaUserValidationForm.FIELD_FIRST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { FIRST_NAME_LABEL }));
            }
		}
		isValid = lastNameMandatoryRule.validate(
				RegisterRiaUserValidationForm.FIELD_LAST_NAME, errors, form
						.getLastName());
		if (isValid) {
            isValid = invalidFirstAndLastNameValueRErule.validate(RegisterRiaUserValidationForm.FIELD_LAST_NAME,
                    tempArrayList, form.getLastName());
            if (!isValid) {
                errors
                        .add(new ValidationError(RegisterRiaUserValidationForm.FIELD_LAST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { LAST_NAME_LABEL }));
            }
		}
		EmailAddressRule.getInstance().validate(RegisterRiaUserValidationForm.FIELD_EMAIL, errors, form.getEmailAddress());
		
		// Phone number validation
		if(StringUtils.isEmpty(form.getPhoneNumber().getNumber1())
				|| StringUtils.isEmpty(form.getPhoneNumber().getNumber2())
				|| StringUtils.isEmpty(form.getPhoneNumber().getAreaCode())){
			
			errors.add(new ValidationError(RegisterRiaUserValidationForm.FIELD_PHONE_NUMBER,
                    BDErrorCodes.MISSING_PHONE_NUMBER,
                    new Object[] { PHONE_NO_LABEL }));
		} else{
			PhoneNumberRule.getInstance().validate(
					RegisterRiaUserValidationForm.FIELD_PHONE_NUMBER, errors,
					form.getPhoneNumber());
		}
		isValid = iardNumberMandatoryRule.validate(RegisterRiaUserValidationForm.FIELD_IARD_NUMBER, errors, form.getIardNumber());
		if (isValid) {
            isValid = invalidIARDRErule.validate(RegisterRiaUserValidationForm.FIELD_IARD_NUMBER,
                    tempArrayList, form.getIardNumber());
            if (!isValid) {
                errors
                        .add(new ValidationError(RegisterRiaUserValidationForm.FIELD_IARD_NUMBER,
                                BDErrorCodes.INVALID_IARD_NUMBER,
                                new Object[] { FIELD_IARD_NUMBER_LABEL }));
            }
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
	
}
