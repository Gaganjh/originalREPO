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
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.bd.web.validation.rules.EmailAddressRule;
import com.manulife.pension.bd.web.validation.rules.SsnRule;
import com.manulife.pension.bd.web.validation.rules.TaxIdRule;
import com.manulife.pension.delegate.BDPublicSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.validation.rules.ContractNumberRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.broker.valueobject.BrokerEntityProfile;
import com.manulife.pension.service.security.bd.valueobject.BrokerEntityVerificationKey;
import com.manulife.pension.service.security.bd.valueobject.BrokerRegistrationValueObject;
import com.manulife.pension.service.security.bd.valueobject.UserRegistrationValueObject;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;
/**
 * This is the action class for Step 1 of Broker Registration
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping( value = "/registerExternalBroker")

public class RegisterBrokerStep1Controller extends BDPublicWizardProcessController {
	@ModelAttribute("registerBrokerValidationForm") 
	public RegisterBrokerValidationForm populateForm() 
	{
		return new RegisterBrokerValidationForm();
		}
	public static HashMap<String,String>forwards= new HashMap<String,String>();
	static{
		forwards.put("input","/registration/registerBrokerStep1.jsp");
		forwards.put("fail","/registration/registerBrokerStep1.jsp");
		}

    private final MandatoryRule firstNameMandatoryRule = new MandatoryRule(
            BDErrorCodes.MISSING_PROFILE_FIRST_NAME);

    private final MandatoryRule lastNameMandatoryRule = new MandatoryRule(
            BDErrorCodes.MISSING_PROFILE_LAST_NAME);

    private final MandatoryRule contractNumberMandatoryRule = new MandatoryRule(
            BDErrorCodes.MISSING_CONTRACT_NUMBER);

    private final RegularExpressionRule firstNameRErule = new RegularExpressionRule(
            BDErrorCodes.INVALID_PROFILE_FIRST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);
 
    private final RegularExpressionRule lastNameRErule = new RegularExpressionRule(
            BDErrorCodes.INVALID_PROFILE_LAST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);
  
    private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static String HomeRedirect=HomeRedirect1.getPath();
    /**
     * Constructor
     */
    public RegisterBrokerStep1Controller() {
        super(RegisterBrokerStep1Controller.class, RegisterBrokerProcessContext.class,
                RegisterBrokerProcessContext.ProcessName, RegisterBrokerProcessContext.Step1);
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
    protected ProcessState doContinue( ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doContinue");
        }
        RegisterBrokerValidationForm brokerValidationForm = (RegisterBrokerValidationForm) actionForm;
        List<ValidationError> errors = new ArrayList<ValidationError>();
        errors.addAll(validate(brokerValidationForm));
        if (errors.size() == 0) {
            RegisterBrokerProcessContext context = (RegisterBrokerProcessContext) getProcessContext(request);

            BrokerRegistrationValueObject registrationVO = (BrokerRegistrationValueObject) createRegistrationVO(brokerValidationForm);

            try {
                BrokerEntityProfile entityProfile = BDPublicSecurityServiceDelegate.getInstance()
                        .getBrokerByKeyContract(registrationVO.getBrokerVerification(), IPAddressUtils.getRemoteIpAddress(request));

                registrationVO.setBrokerEntity(entityProfile);
                context.setBrokerRegistrationVO(registrationVO);
            } catch (SecurityServiceException e) {
                logger.debug("Broker Verification Failed. ", e);
                errors.add(new ValidationError("", SecurityServiceExceptionHelper
                        .getErrorCodeForCause(e)));
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doContinue");
        }
        if (errors.size() == 0) {
            return getState().getNext(BDWizardProcessContext.ACTION_CONTINUE);
        } else {
        	brokerValidationForm.setChanged(true);
            setErrorsInSession(request, errors);
            ((RegisterBrokerProcessContext) getProcessContext(request))
                    .updateContext(brokerValidationForm);
            return getState();
        }
    }

    /**
     * Helper method to create the BrokerRegistrationValueObject from the form object
     * 
     * @param form
     * @return UserRegistrationValueObject
     */
    private UserRegistrationValueObject createRegistrationVO(RegisterBrokerValidationForm form) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> createRegistrationVO");
        }
        BrokerRegistrationValueObject registrationVO = new BrokerRegistrationValueObject();
        registrationVO.setFirstName(form.getFirstName());
        registrationVO.setLastName(form.getLastName());
        registrationVO.setEmailAddress(form.getEmailAddress());
        registrationVO.setBrokerVerification(populateBrokerEntity(form));
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> createRegistrationVO");
        }
        return registrationVO;
    }

    /**
     * Helper method to populate the BrokerEntityVerificationKey object
     * 
     * @param form
     * @return BrokerEntityVerificationKey
     */
    private BrokerEntityVerificationKey populateBrokerEntity(RegisterBrokerValidationForm form) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateBrokerEntity");
        }
        BrokerEntityVerificationKey key = new BrokerEntityVerificationKey();
        key.setContractNum(Integer.parseInt(form.getContractNumber()));
        key.setEmailAddress(form.getEmailAddress());
        key.setSsn(BDWebCommonUtils.getHyphenatedString(form.getSsn().getValue(),
                BDConstants.ID_TYPE_SSN));
        key.setTaxId(BDWebCommonUtils.getHyphenatedString(form.getTaxId().getValue(),
                BDConstants.ID_TYPE_TAX));
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> populateBrokerEntity");
        }
        return key;
    }

    /**
     * The validation methods for the form
     * 
     * @param form
     * @return List<ValidationError>
     * @throws SystemException
     */
    private List<ValidationError> validate(RegisterBrokerValidationForm form)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> validate");
        }
        List<ValidationError> errors = new ArrayList<ValidationError>();
        boolean isValid = false;
        isValid = firstNameMandatoryRule.validate(RegisterBrokerValidationForm.FIELD_FIRST_NAME,
                errors, form.getFirstName());
        if (isValid) {
            firstNameRErule.validate(RegisterBrokerValidationForm.FIELD_FIRST_NAME, errors, form
                    .getFirstName());
        }
        isValid = lastNameMandatoryRule.validate(RegisterBrokerValidationForm.FIELD_LAST_NAME,
                errors, form.getLastName());
        if (isValid) {
            lastNameRErule.validate(RegisterBrokerValidationForm.FIELD_LAST_NAME, errors, form
                    .getLastName());
        }
        EmailAddressRule.getInstance().validate(RegisterBrokerValidationForm.FIELD_EMAIL_ADDRESS,
                errors, form.getEmailAddress());
        isValid = contractNumberMandatoryRule.validate(
                RegisterBrokerValidationForm.FIELD_CONTRACT_NUMBER, errors, form
                        .getContractNumber());
        if (isValid) {
            ContractNumberRule.getInstance().validate(
                    RegisterBrokerValidationForm.FIELD_CONTRACT_NUMBER, errors,
                    form.getContractNumber());
        }
        if (StringUtils.isEmpty(form.getSsn().getValue())
                && StringUtils.isEmpty(form.getTaxId().getValue())) {
            errors.add(new ValidationError("", BDErrorCodes.BOTH_SSN_AND_TAX_ID_BLANK));
        } else if (!StringUtils.isEmpty(form.getSsn().getValue())) {
            SsnRule.getInstance().validate(RegisterBrokerValidationForm.FIELD_SSN, errors,
                    form.getSsn());
        } else if (!StringUtils.isEmpty(form.getTaxId().getValue())) {
            TaxIdRule.getInstance().validate(RegisterBrokerValidationForm.FIELD_TAX_ID, errors,
                    form.getTaxId());
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
	  
		
		@RequestMapping(value = "/broker/step1", method = { RequestMethod.GET })
		protected String doInput(
				@Valid @ModelAttribute("registerBrokerValidationForm") RegisterBrokerValidationForm form,
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

		@RequestMapping(value = "/broker/step1", params = { "action=continue" }, method = { RequestMethod.POST })
		protected String doContinue(
				@Valid @ModelAttribute("registerBrokerValidationForm") RegisterBrokerValidationForm form,
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
   
		@RequestMapping(value = "/broker/step1", params = {"action=cancel" }, method = { RequestMethod.POST })
		protected String doCancel(
				@Valid @ModelAttribute("registerBrokerValidationForm") RegisterBrokerValidationForm form,
				BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
				throws SystemException, IOException, ServletException {
			if (logger.isDebugEnabled()) {
				logger.debug("entry -> doInput");
			}
			if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					return forwards.get("input");// if input forward not //available, provided default
				}
			}
			if (BDSessionHelper.getUserProfile(request) != null) {
				return HomeRedirect;
			}
			String forward = super.doCancelContinue(form, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}	
		
}
