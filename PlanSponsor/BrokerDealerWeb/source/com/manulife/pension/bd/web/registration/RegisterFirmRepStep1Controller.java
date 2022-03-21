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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.bd.web.validation.rules.AccessCodeRule;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.bd.web.validation.rules.PhoneNumberRule;
import com.manulife.pension.delegate.BDPublicSecurityServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.security.bd.exception.BDFailedNTimesRegistrationException;
import com.manulife.pension.service.security.bd.valueobject.FirmRepCreationValueObject;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;

/**
 * This is the action class for step 1 of Firm Rep Registration
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping( value = "/registerFirmRep")

public class RegisterFirmRepStep1Controller extends BDPublicWizardProcessController {
	@ModelAttribute("registerFirmRepStep1Form") 
	public RegisterFirmRepValidationForm populateForm() 
	{
		return new RegisterFirmRepValidationForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/registration/registerFirmRepStep1.jsp");
		forwards.put("fail","/registration/registerFirmRepStep1.jsp");
		}
	private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static String HomeRedirect=HomeRedirect1.getPath();
	private final MandatoryRule lastNameMandatoryRule = new MandatoryRule(
			BDErrorCodes.MISSING_LAST_NAME);

    private static final RegularExpressionRule invalidLastNameValueRErule = new RegularExpressionRule(
            BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);

    private static final String LAST_NAME_LABEL = "Last Name";
        
    
	public RegisterFirmRepStep1Controller() {
		super(RegisterFirmRepStep1Controller.class,
				RegisterFirmRepProcessContext.class,
				RegisterFirmRepProcessContext.ProcessName,
				RegisterFirmRepProcessContext.Step1);
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
		RegisterFirmRepValidationForm form = (RegisterFirmRepValidationForm) actionForm;
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		RegisterFirmRepProcessContext context = (RegisterFirmRepProcessContext) getProcessContext(request);
		// start changes for Password Update US Register Controller 
		String businessParamValue = null;
		EnvironmentServiceDelegate service = null; 
		try {
			service = EnvironmentServiceDelegate
    				.getInstance();
			businessParamValue = service.getBusinessParam(BDConstants.FRW_SHOW_PASSWORD_METER_IND);
		    } catch (SystemException e) {
			logger.error("Fail to retrieve "+BDConstants.FRW_SHOW_PASSWORD_METER_IND+" businessParamValue", e);
		}
		
		
	   
		boolean daoFlag = ("ALL".equals(businessParamValue)) || ("EXT".equals(businessParamValue));
		
		if(!daoFlag){
		     errors.addAll(validate(form));
		}
		
		// End changes for Update Password FRW Register Controller 
		String ipAddress = IPAddressUtils.getRemoteIpAddress(request);

		if (errors.isEmpty()) {
			 context = (RegisterFirmRepProcessContext) getProcessContext(request);

			try {
				FirmRepCreationValueObject creationVO = BDPublicSecurityServiceDelegate
						.getInstance().validateFirmRepRegInfo(
								context.getSecurityRequest().getId(),
								form.getLastName(),
								form.getPhoneNumber().getValue(),
								form.getPassCode(),
								ipAddress);
				context.setCreationVO(creationVO);
			} catch (SecurityServiceException e) {
				logger.debug("Firm Rep Validation Failed. ", e);
				errors
						.add(new ValidationError(
								"",
								SecurityServiceExceptionHelper
										.getErrorCodeForCause(
												e,
												RegisterFirmRepProcessContext.ProcessSecurityServiceExceptionMapping)));
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
	private List<ValidationError> validate(RegisterFirmRepValidationForm form)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> validate");
		}
		List<ValidationError> errors = new ArrayList<ValidationError>();
		List<ValidationError> tempArrayList = new ArrayList<ValidationError>();
		boolean isValid = false;
		isValid = lastNameMandatoryRule.validate(
				RegisterFirmRepValidationForm.FIELD_LAST_NAME, errors, form
						.getLastName());
		if (isValid) {
            isValid = invalidLastNameValueRErule.validate(RegisterFirmRepValidationForm.FIELD_LAST_NAME,
                    tempArrayList, form.getLastName());
            if (!isValid) {
                errors
                        .add(new ValidationError(RegisterFirmRepValidationForm.FIELD_LAST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { LAST_NAME_LABEL }));
            }
		}
		PhoneNumberRule.getInstance().validate(
				RegisterFirmRepValidationForm.FIELD_PHONE_NUMBER, errors,
				form.getPhoneNumber());
		AccessCodeRule.getInstance().validate(
				RegisterFirmRepValidationForm.FIELD_PASSCODE, errors,
				form.getPassCode());
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> validate");
		}
		return errors;
	}
	
	@RequestMapping(value = "/step1", method = { RequestMethod.GET })
	protected String doInput(@Valid @ModelAttribute("registerFirmRepStep1Form") RegisterFirmRepValidationForm form,
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
		
		String businessParamValue = null;
		EnvironmentServiceDelegate service = null; 
		try {
			service = EnvironmentServiceDelegate
    				.getInstance();
			businessParamValue = service.getBusinessParam(BDConstants.FRW_SHOW_PASSWORD_METER_IND);
		    request.getSession(false).setAttribute(BDConstants.FRW_SHOW_PASSWORD_METER_IND,businessParamValue);
		} catch (SystemException e) {
			logger.error("Fail to retrieve "+BDConstants.FRW_SHOW_PASSWORD_METER_IND+" businessParamValue", e);
		}

		if (forward == null) {
			String forward1 = super.doInput(form, request, response);
			return StringUtils.contains(forward1, '/') ? forward1 : forwards.get(forward1);
		}
		return forward;
	}
	
	@RequestMapping(value = "/step1", params = { "action=continue" }, method = { RequestMethod.POST })
	protected String doContinue(@Valid @ModelAttribute("registerFirmRepStep1Form") RegisterFirmRepValidationForm form,
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
	public String doCancel(@Valid @ModelAttribute("registerFirmRepStep1Form") RegisterFirmRepValidationForm form,
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
	 * Validate form and request against penetration attack, prior to other validations.
	 */
	@Autowired
	   private BDValidatorFWFail  bdValidatorFWFail;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWFail);
	}
	
	
}
