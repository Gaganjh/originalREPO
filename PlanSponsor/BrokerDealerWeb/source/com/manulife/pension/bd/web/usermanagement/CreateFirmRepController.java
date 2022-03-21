package com.manulife.pension.bd.web.usermanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.process.BDWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.FrwValidator;
import com.manulife.pension.bd.web.validation.rules.AccessCodeRule;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.bd.web.validation.rules.EmailAddressRule;
import com.manulife.pension.bd.web.validation.rules.PhoneNumberRule;
import com.manulife.pension.delegate.BDUserManagementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;

/**
 * The firm rep create action
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping( value ="/createFirmRep")

public class CreateFirmRepController extends BDWizardProcessController {
	@ModelAttribute("createFirmRepForm") 
	public CreateFirmRepForm populateForm()
	{
		return new CreateFirmRepForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/usermanagement/createFirmRep.jsp");
		}

    private final MandatoryRule firstNameMandatoryRule = new MandatoryRule(
            BDErrorCodes.MISSING_FIRST_NAME);

    private final MandatoryRule lastNameMandatoryRule = new MandatoryRule(
            BDErrorCodes.MISSING_LAST_NAME);

    private static final RegularExpressionRule invalidFirstAndLastNameValueRErule = new RegularExpressionRule(
            BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);

    private static final String FIRST_NAME_LABEL = "First Name";
    
    private static final String LAST_NAME_LABEL = "Last Name";
    
    private final List<ValidationError> tempArrayList = new ArrayList<ValidationError>();

    public CreateFirmRepController() {
        super(CreateFirmRepController.class, CreateFirmRepProcessContext.class,
                CreateFirmRepProcessContext.ProcessName, CreateFirmRepProcessContext.InputState);
    }

    /**
	 * Perform penetration validation that might affect control (inner) fields,
	 * before proceeding with basic process.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected ProcessState doProcess( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		List errors = new ArrayList<ValidationError>();

		if (FrwValidator.getInstance().validateSanitizeCatalogedFormFields(form, errors, request) == false) {
			CreateFirmRepProcessContext context = (CreateFirmRepProcessContext) getProcessContext(request);
			context.setChanged(true);
        	context.setCompleted(false);
            setErrorsInSession(request, errors);
            return getState();
		}
	    return super.doProcess( form, request, response);
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
        CreateFirmRepForm firmRepForm = (CreateFirmRepForm) form;
        CreateFirmRepProcessContext context = (CreateFirmRepProcessContext) getProcessContext(request);
        List<ValidationError> errors = new ArrayList<ValidationError>();
        errors.addAll(validate(firmRepForm, request));
        if (errors.size() == 0) {
            try {
                BDUserManagementServiceDelegate.getInstance().createFirmRep(
                        BDSessionHelper.getUserProfile(request).getBDPrincipal(),
                        context.getFirmRepcreationVO(),IPAddressUtils.getRemoteIpAddress(request));
            } catch (SecurityServiceException e) {
                logger.debug("Create Firm Rep Failed. ", e);
				errors.add(new ValidationError("",
						SecurityServiceExceptionHelper.getErrorCode(e)));
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doContinue");
        }
        if (!errors.isEmpty()) {
        	context.setChanged(true);
        	context.setCompleted(false);
            setErrorsInSession(request, errors);
            return getState();
        } else {
        	context.setChanged(false);
        	context.setCompleted(true);
            return getState().getNext(BDWizardProcessContext.ACTION_CONTINUE);
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
	private List<ValidationError> validate(CreateFirmRepForm form, HttpServletRequest request)  {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> validate");
        }
        List errors = new ArrayList<ValidationError>();
        boolean isValid = false;
        
        //
        // Earlier errors could have been set and not reported yet by the child - Search Firm Action.
        // In such case Search Firm Action errors shall be reported only, and no check yet for the current form errors.
        //
        Collection e = BaseSessionHelper.getErrorsInSession(request);
        if (e != null) {
        	errors.addAll(e);
        	BaseSessionHelper.removeErrorsInSession(request);
        }

        if (errors.size() > 0 || FrwValidator.getInstance().validateSanitizeCatalogedFormFields(form, errors, request) == false) {	
			if(logger.isDebugEnabled()) {
			    logger.debug("exit <- validate");
		    }
			return errors;
		}
        
        isValid = firstNameMandatoryRule.validate(CreateFirmRepForm.FIELD_FIRST_NAME, errors, form
                .getFirstName());
        if (isValid) {
            isValid = invalidFirstAndLastNameValueRErule.validate(CreateFirmRepForm.FIELD_FIRST_NAME,
                    tempArrayList, form.getFirstName());
            if (!isValid) {
                errors
                        .add(new ValidationError(CreateFirmRepForm.FIELD_FIRST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { FIRST_NAME_LABEL }));
            }
         }
        isValid = lastNameMandatoryRule.validate(CreateFirmRepForm.FIELD_LAST_NAME, errors, form
                .getLastName());
        if (isValid) {
            isValid = invalidFirstAndLastNameValueRErule.validate(CreateFirmRepForm.FIELD_LAST_NAME,
                    tempArrayList, form.getLastName());
            if (!isValid) {
                errors
                        .add(new ValidationError(CreateFirmRepForm.FIELD_LAST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { LAST_NAME_LABEL }));
            }
        }
        PhoneNumberRule.getInstance().validate(CreateFirmRepForm.FIELD_PHONE_NUMBER, errors,
                form.getPhoneNumber());
        EmailAddressRule.getInstance().validate(CreateFirmRepForm.FIELD_EMAIL, errors,
                form.getEmailAddress());

        if (StringUtils.isEmpty(form.getFirmListStr())) {
            errors.add(new ValidationError(CreateFirmRepForm.FIELD_FIRMS,
                    BDErrorCodes.FIRMREP_CREATION_MISSING_FIRMS));
        }
        
        AccessCodeRule.getInstance().validate(CreateFirmRepForm.FIELD_PASS_CODE, errors,
                form.getPassCode());
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> validate");
        }
        return errors;
    }
     
    protected String doInput(@Valid @ModelAttribute("createFirmRepForm") CreateFirmRepForm form, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if (StringUtils.equals("y", request.getParameter("start"))) {
			CreateFirmRepProcessContext context = (CreateFirmRepProcessContext) getProcessContext(request);
			if (context!= null) {
				context.reset();
			}
		}
		return super.doInput( form, request, response);
	}
  //GET-POST both is required since the forward return always will happen through doExecute method
    @RequestMapping(value = "/create",method =  {RequestMethod.GET,RequestMethod.POST})
	public String doExecute(@Valid @ModelAttribute("createFirmRepForm") CreateFirmRepForm form,BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("fail");//if input forward not //available, provided default
			}
		}
		String forward=super.doExecute( form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	
	
}
