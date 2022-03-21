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
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.delegate.BDPublicSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.security.bd.exception.BDFailedNTimesRegistrationException;
import com.manulife.pension.service.security.bd.valueobject.BrokerAssistantCreationValueObject;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;

/**
 * This is the action class for the First step of BrokerAssistant registration
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping( value = "/registerBrokerAssistant")

public class RegisterBrokerAssistantStep1Controller extends BDPublicWizardProcessController {
	@ModelAttribute("registerBrokerAssistantStep1Form") 
	public RegisterBrokerAssistantValidationForm populateForm() 
	{
		return new RegisterBrokerAssistantValidationForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
    private static final String FAIL = "fail";
    private static final String INPUT = "input";
	private final MandatoryRule lastNameMandatoryRule = new MandatoryRule(
			BDErrorCodes.MISSING_LAST_NAME);

    private static final RegularExpressionRule invalidLastNameValueRErule = new RegularExpressionRule(
            BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);

    private static final String LAST_NAME_LABEL = "Last Name";        

	private final MandatoryRule superLastNameMandatoryRule = new MandatoryRule(
			BDErrorCodes.ASSISTANT_REGISTRATION_MISSING_SUPERVISOR_LAST_NAME);

	private final RegularExpressionRule superLastNameRErule = new RegularExpressionRule(
			BDErrorCodes.ASSISTANT_REGISTRATION_INVALID_SUPERVISOR_LAST_NAME,
			BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);
	static{
		forwards.put(INPUT,"/registration/registerBrokerAssistantStep1.jsp");
		forwards.put(FAIL,"/registration/registerBrokerAssistantStep1.jsp");
		}
	/**
	 * Constructor
	 */
	public RegisterBrokerAssistantStep1Controller() {
		super(RegisterBrokerAssistantStep1Controller.class,
				RegisterBrokerAssistantProcessContext.class,
				RegisterBrokerAssistantProcessContext.ProcessName,
				RegisterBrokerAssistantProcessContext.Step1);
	}

	
	/* (non-Javadoc)
	 * @see com.manulife.pension.bd.web.process.BDWizardProcessController#doContinue(com.manulife.pension.ezk.web.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ProcessState doContinue(
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doContinue");
		}
		RegisterBrokerAssistantValidationForm form = (RegisterBrokerAssistantValidationForm) actionForm;
		List<ValidationError> errors = new ArrayList<ValidationError>();
		errors.addAll(validate(form));
		String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
		if (errors.size() == 0) {
			RegisterBrokerAssistantProcessContext context = (RegisterBrokerAssistantProcessContext) getProcessContext(request);

			try {
				BrokerAssistantCreationValueObject creationVO = BDPublicSecurityServiceDelegate
						.getInstance().validateBrokerAssistantRegInfo(
								context.getSecurityRequest().getId(),
								form.getLastName(),
								form.getSupervisorLastName(),
								ipAddress);
				context.setCreationVO(creationVO);
			} catch (SecurityServiceException e) {
				logger.debug("Assistant Validation Failed. ", e);
				errors
						.add(new ValidationError(
								"",
								SecurityServiceExceptionHelper
										.getErrorCodeForCause(
												e,
												RegisterBrokerAssistantProcessContext.ProcessSecurityServiceExceptionMapping)));
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
	private List<ValidationError> validate(
			RegisterBrokerAssistantValidationForm form) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> validate");
		}
		List<ValidationError> errors = new ArrayList<ValidationError>();
		List<ValidationError> tempArrayList = new ArrayList<ValidationError>();
		boolean isValid = false;
		isValid = lastNameMandatoryRule.validate(
				RegisterBrokerAssistantValidationForm.FIELD_LAST_NAME, errors,
				form.getLastName());
		if (isValid) {
            isValid = invalidLastNameValueRErule.validate(RegisterBrokerAssistantValidationForm.FIELD_LAST_NAME,
                    tempArrayList, form.getLastName());
            if (!isValid) {
                errors
                        .add(new ValidationError(RegisterBrokerAssistantValidationForm.FIELD_LAST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { LAST_NAME_LABEL }));
            }
		}
		isValid = superLastNameMandatoryRule
				.validate(
						RegisterBrokerAssistantValidationForm.FIELD_SUPERVISOR_LAST_NAME,
						errors, form.getSupervisorLastName());
		if (isValid) {
			superLastNameRErule
					.validate(
							RegisterBrokerAssistantValidationForm.FIELD_SUPERVISOR_LAST_NAME,
							errors, form.getSupervisorLastName());
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
	@RequestMapping(value = "/step1", method = { RequestMethod.GET })
	protected String doInput(
			@Valid @ModelAttribute("registerBrokerAssistantStep1Form") RegisterBrokerAssistantValidationForm form,
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
	@RequestMapping(value = "/step1", params = { "action=continue" }, method = { RequestMethod.POST })
	protected String doContinue(
			@Valid @ModelAttribute("registerBrokerAssistantStep1Form") RegisterBrokerAssistantValidationForm form,
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
	@RequestMapping(value = "/step1", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(
			@Valid @ModelAttribute("registerBrokerAssistantStep1Form") RegisterBrokerAssistantValidationForm form,
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
	
}
