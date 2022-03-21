package com.manulife.pension.bd.web.registration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.firmsearch.FirmSearchUtils;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.registration.util.RegistrationUtils;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.FrwValidator;
import com.manulife.pension.bd.web.validation.rules.AddressRule;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.bd.web.validation.rules.EmailAddressRule;
import com.manulife.pension.bd.web.validation.rules.PhoneNumberRule;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.broker.valueobject.impl.BrokerDealerFirmImpl;
import com.manulife.pension.service.security.bd.exception.BDBasicBrokerNotFoundException;
import com.manulife.pension.service.security.bd.exception.BDPeopleSoftNotAvailableException;
import com.manulife.pension.service.security.bd.log.BDEventLog;
import com.manulife.pension.service.security.bd.valueobject.BasicBrokerRegistrationValueObject;
import com.manulife.pension.service.security.bd.valueobject.UserRegistrationValueObject;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.pension.validator.ValidationError;

/**
 * This is the action class for the step 1 of Basic Broker Registration
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping(value = "/registerExternalBroker")

public class RegisterBasicBrokerStep1Controller extends BDPublicWizardProcessController {
	@ModelAttribute("registerBasicBrokerValidationForm")
	public RegisterBasicBrokerValidationForm populateForm() {
		return new RegisterBasicBrokerValidationForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/registration/registerBasicBrokerStep1.jsp");
	}

	private static final String BROKERREST_URI = "bd.brokerSignup.restURI";
	private static final String BROKEROAUTH_URI = "bd.brokerSignup.oauthURI";
	private static String BROKERSIGNUP_GETURL = null;
	private static String BROKERSIGNUP_OAUTHURL = null;
	private static final String CLIENT_ID = "bd.brokerSignup.clientId";
	private static final String CLIENT_SECRET = "bd.brokerSignup.clientSecret";
	private static String BROKERSIGNUP_CLIENT_ID = null;
	private static String BROKERSIGNUP_CLEINT_SECRET = null;
	static {
		BaseEnvironment environment = new BaseEnvironment();
		BROKERSIGNUP_GETURL = environment.getNamingVariable(BROKERREST_URI, null);
		BROKERSIGNUP_OAUTHURL = environment.getNamingVariable(BROKEROAUTH_URI, null);
		BROKERSIGNUP_CLIENT_ID = environment.getNamingVariable(CLIENT_ID, null);
		BROKERSIGNUP_CLEINT_SECRET = environment.getNamingVariable(CLIENT_SECRET, null);
	}

	private final MandatoryRule firstNameMandatoryRule = new MandatoryRule(BDErrorCodes.MISSING_PROFILE_FIRST_NAME);

	private final MandatoryRule lastNameMandatoryRule = new MandatoryRule(BDErrorCodes.MISSING_PROFILE_LAST_NAME);

	private final RegularExpressionRule firstNameRErule = new RegularExpressionRule(
			BDErrorCodes.INVALID_PROFILE_FIRST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);

	private final RegularExpressionRule lastNameRErule = new RegularExpressionRule(
			BDErrorCodes.INVALID_PROFILE_LAST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);

	private static final RegularExpressionRule companyNameRErule = new RegularExpressionRule(
			BDErrorCodes.PERSONAL_INFO_INVALID_VALUE, BDRuleConstants.COMPANY_NAME_RE);
	private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static String HomeRedirect = HomeRedirect1.getPath();
	private static final String FIELD_COMPANY_NAME = "companyName";

	private static final String COMPANY_NAME_LABEL = "Company Name";

	/**
	 * Constructor
	 */
	public RegisterBasicBrokerStep1Controller() {
		super(RegisterBasicBrokerStep1Controller.class, RegisterBasicBrokerProcessContext.class,
				RegisterBasicBrokerProcessContext.ProcessName, RegisterBasicBrokerProcessContext.Step1);
	}

	/**
	 * Perform penetration validation that might affect control (inner) fields,
	 * before proceeding with basic process.
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected ProcessState doProcess(ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		List errors = new ArrayList<ValidationError>();

		if (FrwValidator.getInstance().validateSanitizeCatalogedFormFields(form, errors, request) == false) {
			//
			// Only Internal filed validation failure on registration page
			// results in a
			// detailed message, in other cases generic error message is shown.
			//
			for (Object e : errors) {
				if (e instanceof GenericException) {
					//
					// Anonymous penetration error.
					//
					if (((GenericException) e)
							.getErrorCode() != CommonErrorCodes.ERROR_FRWVALIDATION_WITHOUT_GUI_FIELD_NAME) {
						errors.clear();
						errors.add(new GenericException(BDErrorCodes.BROKER_VERIFICATION_FAIL));
						setErrorsInRequest(request, errors);
						break;
					}
				}
			}

			RegisterBasicBrokerValidationForm basicBrokerValidationForm = (RegisterBasicBrokerValidationForm) form;
			basicBrokerValidationForm.setChanged(true);
			((RegisterBasicBrokerProcessContext) getProcessContext(request)).updateContext(basicBrokerValidationForm);
			setErrorsInSession(request, errors);

			return getState();
		}
		return super.doProcess(form, request, response);
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
	protected ProcessState doContinue(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doContinue");
		}

		RegisterBasicBrokerValidationForm basicBrokerValidationForm = (RegisterBasicBrokerValidationForm) actionForm;
		List<ValidationError> errors = new ArrayList<ValidationError>();
		errors.addAll(validate(basicBrokerValidationForm, request));
		String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
		if (errors.size() == 0) {
			RegisterBasicBrokerProcessContext context = (RegisterBasicBrokerProcessContext) getProcessContext(request);
			BasicBrokerRegistrationValueObject registrationVO = (BasicBrokerRegistrationValueObject) createRegistrationVO(
					basicBrokerValidationForm);

			try {
				validateBasicBrokerInfowithSF(registrationVO, errors, ipAddress, context);
				context.setBasicBrokerRegistrationVO(registrationVO);
			} catch (SecurityServiceException e) {
				logger.debug("Basic Broker Validation Failed. ", e);
				errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCodeForCause(e)));
			} catch (BDPeopleSoftNotAvailableException e) {
				errors.add(new ValidationError("", BDErrorCodes.PEOPLESOFT_NOT_AVAILABLE));
				context.setDisabled(true);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doContinue");
		}
		if (errors.size() == 0) {
			return getState().getNext(BDWizardProcessContext.ACTION_CONTINUE);
		} else {
			basicBrokerValidationForm.setChanged(true);
			((RegisterBasicBrokerProcessContext) getProcessContext(request)).updateContext(basicBrokerValidationForm);
			setErrorsInSession(request, errors);
			return getState();
		}
	}
	public static final String BROKER_SIGNUP_QUERYPARAMS = "?sfdc_path=BROKER_SIGNUP";
	private static final String CLASS_NAME = RegisterBasicBrokerStep1Controller.class.getName();
	/**
	 * This method will takecare of validating the broker details with 
	 * Salesforce DataBase and return true if the Broker is available.
	 * 
	 */
	private boolean validateBasicBrokerInfowithSF(BasicBrokerRegistrationValueObject registrationVO,
			List<ValidationError> errors, String ipAddress, RegisterBasicBrokerProcessContext context)
			throws SystemException, SecurityServiceException, BDPeopleSoftNotAvailableException {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);
 		headers.set("Authorization", "Bearer " + getAccessToken() );
 		//in the below line's of code,Charset is set to false to avoid the 502 Bad Gateway issue,
 		//while calling the Apigee proxy URL to avoid unwanted occurence of charset.
 		List<HttpMessageConverter<?>> converters = new ArrayList<>();
 		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
 		stringConverter.setWriteAcceptCharset(false);
 		converters.add(stringConverter);
 		restTemplate.setMessageConverters(converters);
 		
 		BrokerRequestBody brokerRequest = new BrokerRequestBody();
 		brokerRequest.setLastName(registrationVO.getLastName());
 		brokerRequest.setStateCode(registrationVO.getAddress().getState());
 		brokerRequest.setEmailAddress(registrationVO.getEmailAddress());
 		Gson gson = new Gson();
 		String json = gson.toJson(brokerRequest);
 		HttpEntity<String> entity = new HttpEntity<String>(json, headers); 
		String endpointURL = BROKERSIGNUP_GETURL + BROKER_SIGNUP_QUERYPARAMS;
		ResponseEntity<String> response = restTemplate.postForEntity(endpointURL, entity, String.class);
		String Sample= response.getBody();
		Gson g = new Gson();
		UsercountVo userCount = g.fromJson(Sample, UsercountVo.class);
		try {
			if (userCount.getUserCount() > 0) {
				return true;
			}
			throw new BDBasicBrokerNotFoundException(CLASS_NAME, "validateBasicBrokerInfo",
					"Cound not find the basic broker = " + registrationVO);
		} catch (SecurityServiceException e) {
			throw logSecurityServiceException(new SecurityServiceException(e,
					e.getMessage() + "," + BDEventLog.REMOTE_ADDRESS + ":" + ipAddress));
		}
	}

	protected SystemException logSystemException(SystemException e) {
		LogUtility.logSystemException(SecurityConstants.APPLICATION_ID, e);
		return e;
	}

	public SecurityServiceException logSecurityServiceException(SecurityServiceException e) {
		LogUtility.logApplicationException(e);
		return e;
	}

	public String getGMT() {
		final Date currentTime = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String GMTTime = sdf.format(currentTime);
		return GMTTime;
	}

	private String getAccessToken() throws SystemException {
		RestTemplate restTemplate = new RestTemplate();
		String message = "client_id=" + BROKERSIGNUP_CLIENT_ID + "&client_secret=" + BROKERSIGNUP_CLEINT_SECRET;
		HttpEntity<String> requestBody = new HttpEntity<>(message, getHttpHeaders());
		ResponseEntity<OAuthResponse> result = restTemplate.postForEntity(BROKERSIGNUP_OAUTHURL, requestBody,
				OAuthResponse.class);
		return result.getBody().getAccess_token();

	}

	private HttpHeaders getHttpHeaders() throws SystemException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		return headers;
	}

	/**
	 * Helper method to create the BasicBrokerRegistrationValueObject from the
	 * form object
	 * 
	 * @param form
	 * @return UserRegistrationValueObject
	 */
	private UserRegistrationValueObject createRegistrationVO(RegisterBasicBrokerValidationForm form) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> createRegistrationVO");
		}
		BasicBrokerRegistrationValueObject registrationVO = new BasicBrokerRegistrationValueObject();
		registrationVO.setFirstName(form.getFirstName());
		registrationVO.setLastName(form.getLastName());
		registrationVO.setEmailAddress(form.getEmailAddress());
		registrationVO.setAddress(RegistrationUtils.populateBDAddressVO(form.getAddress()));
		registrationVO.setPhoneNumber(form.getPhoneNumber().getValue());
		if (!StringUtils.isEmpty(form.getFirmId())) {
			BrokerDealerFirmImpl firmInfo = new BrokerDealerFirmImpl();
			firmInfo.setId(Long.parseLong(form.getFirmId()));
			firmInfo.setFirmName(form.getFirmName());
			registrationVO.setBrokerDealerFirm(firmInfo);
		} else {
			registrationVO.setCompanyName(form.getCompanyName());
		}
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<ValidationError> validate(RegisterBasicBrokerValidationForm form, HttpServletRequest request)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> validate");
		}

		List errors = new ArrayList<ValidationError>();
		boolean isValid = false;
		if (FrwValidator.getInstance().validateSanitizeCatalogedFormFields(form, errors, request) == false) {
			//
			// Only Internal filed validation failure on registration page
			// results in a
			// detailed message, in other cases generic error message is shown.
			//
			for (Object e : errors) {
				if (e instanceof GenericException) {
					//
					// Anonymous penetration error.
					//
					if (((GenericException) e)
							.getErrorCode() != CommonErrorCodes.ERROR_FRWVALIDATION_WITHOUT_GUI_FIELD_NAME) {
						errors.clear();
						errors.add(new GenericException(BDErrorCodes.BROKER_VERIFICATION_FAIL));
						setErrorsInRequest(request, errors);
						break;
					}
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("exit <- doValidate");
			}
			return errors;
		}

		isValid = firstNameMandatoryRule.validate(RegisterBasicBrokerValidationForm.FIELD_FIRST_NAME, errors,
				form.getFirstName());
		if (isValid) {
			firstNameRErule.validate(RegisterBasicBrokerValidationForm.FIELD_FIRST_NAME, errors, form.getFirstName());
		}
		isValid = lastNameMandatoryRule.validate(RegisterBasicBrokerValidationForm.FIELD_LAST_NAME, errors,
				form.getLastName());
		if (isValid) {
			lastNameRErule.validate(RegisterBasicBrokerValidationForm.FIELD_LAST_NAME, errors, form.getLastName());
		}
		EmailAddressRule.getInstance().validate(RegisterBasicBrokerValidationForm.FIELD_EMAIL, errors,
				form.getEmailAddress());
		AddressRule.getInstance().validate(RegisterBasicBrokerValidationForm.FIELD_ADDRESS, errors, form.getAddress());
		PhoneNumberRule.getInstance().validate(RegisterBasicBrokerValidationForm.FIELD_PHONE_NUMBER, errors,
				form.getPhoneNumber());
		if (StringUtils.isEmpty(form.getPartyType())) {
			errors.add(new ValidationError(RegisterBasicBrokerValidationForm.FIELD_PARTY_TYPE,
					BDErrorCodes.BOTH_BD_FIRM_AND_INDEPENDENT_NOT_SELECTED));
		}
		if (BDConstants.BD_FIRM_PARTY.equals(form.getPartyType())) {
			if (StringUtils.isEmpty(form.getFirmName())) {
				errors.add(new ValidationError(RegisterBasicBrokerValidationForm.FIELD_FIRM_NAME,
						BDErrorCodes.BD_FIRM_NOT_ENTERED));
			} else {
				form.setFirmId(FirmSearchUtils.getFirmPartyId(form.getFirmName()));
				if (StringUtils.isEmpty(form.getFirmId())) {
					errors.add(new ValidationError(RegisterBasicBrokerValidationForm.FIELD_FIRM_NAME,
							BDErrorCodes.INVALID_BD_FIRM));
				}
			}
		} else {
			List<ValidationError> tempArrayList = new ArrayList<ValidationError>();
			if (StringUtils.isNotEmpty(form.getCompanyName())
					&& !companyNameRErule.validate(FIELD_COMPANY_NAME, tempArrayList, form.getCompanyName())) {
				errors.add(new ValidationError(FIELD_COMPANY_NAME, BDErrorCodes.PERSONAL_INFO_INVALID_VALUE,
						new Object[] { COMPANY_NAME_LABEL }));
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> validate");
		}
		return errors;
	}

	@RequestMapping(value = "/basicBroker/step1", method = { RequestMethod.GET })
	protected String doInput(
			@Valid @ModelAttribute("registerBasicBrokerValidationForm") RegisterBasicBrokerValidationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");// if input forward not
												// //available, provided default
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

	@RequestMapping(value = "/basicBroker/step1", params = { "action=continue" }, method = { RequestMethod.POST })
	protected String doContinue(
			@Valid @ModelAttribute("registerBasicBrokerValidationForm") RegisterBasicBrokerValidationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");// if input forward not
												// //available, provided default
			}
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		String forward = super.doCancelContinue(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/basicBroker/step1", params = { "action=cancel" }, method = { RequestMethod.POST })
	protected String doCancel(
			@Valid @ModelAttribute("registerBasicBrokerValidationForm") RegisterBasicBrokerValidationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");// if input forward not
												// //available, provided default
			}
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		String forward = super.doCancelContinue(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
}
