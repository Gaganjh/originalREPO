package com.manulife.pension.bd.web.usermanagement;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.process.BDWizardProcessController;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.FrwValidator;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.bd.web.validation.rules.EmailAddressRule;
import com.manulife.pension.bd.web.validation.rules.PhoneNumberRule;
import com.manulife.pension.delegate.BDUserManagementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;

@Controller
@RequestMapping(value ="/createRiaUser")

public class CreateRiaUserController extends BDWizardProcessController {
	@ModelAttribute("createRiaUserForm") 
	public CreateRiaUserForm populateForm() 
	{
		return new CreateRiaUserForm();
		
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/usermanagement/createRiaUser.jsp");
	}

    private final MandatoryRule firstNameMandatoryRule = new MandatoryRule(
            BDErrorCodes.MISSING_FIRST_NAME);

    private final MandatoryRule lastNameMandatoryRule = new MandatoryRule(
            BDErrorCodes.MISSING_LAST_NAME);
    
    private static final RegularExpressionRule invalidFirstAndLastNameValueRErule = new RegularExpressionRule(
            BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);

    private static final String FIRST_NAME_LABEL = "First Name";
    
    private static final String LAST_NAME_LABEL = "Last Name";
    
    private static final String RIA_EMAIL_EXISTS = "exists";
    
    private static final String RIA_EMAIL_NOT_EXISTS = "notexists";
    
    private final List<ValidationError> tempArrayList = new ArrayList<ValidationError>();

    public CreateRiaUserController() {
        super(CreateRiaUserController.class, CreateRiaUserProcessContext.class,
        		CreateRiaUserProcessContext.ProcessName, CreateRiaUserProcessContext.InputState);
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
			CreateRiaUserProcessContext context = (CreateRiaUserProcessContext) getProcessContext(request);
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
        CreateRiaUserForm riaUserForm = (CreateRiaUserForm) form;
        CreateRiaUserProcessContext context = (CreateRiaUserProcessContext) getProcessContext(request);
        List<ValidationError> errors = new ArrayList<ValidationError>();
        
        errors.addAll(validate(riaUserForm, request));
        if (errors.size() == 0) {
            try {
            	BDUserManagementServiceDelegate.getInstance().createRiaUser(
                        BDSessionHelper.getUserProfile(request).getBDPrincipal(),
                        context.getRiaUsercreationVO(),IPAddressUtils.getRemoteIpAddress(request));
            } catch (SecurityServiceException e) {
                logger.debug("Create RIA User Failed. ", e);
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
	 * Validate the current request is in the right state. If not, force it to
	 * the start state of the process. If it is the right state, call doProcess
	 * if it is a post. Otherwise call doInput to rendering the page
	 */
    @RequestMapping(value ="/create", method =  {RequestMethod.POST,RequestMethod.GET})
  	public String doExecute(@Valid @ModelAttribute("createRiaUserForm") CreateRiaUserForm form,BindingResult bindingResult,
  			HttpServletRequest request, HttpServletResponse response)
  					throws IOException, ServletException, SystemException {
    
		String action = request.getParameter("action");
		if(action == null){
			return doInput( form, request, response);
		}
		if(StringUtils.equals("checkDuplicateEmail", action)){
			doCheckDuplicateEmail( form, bindingResult, request, response);
			return null;
		}
		
		String forward=super.doExecute( form, request, response);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
    /**
     * This method is used to check the existence of email 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws SystemException
     */
    @RequestMapping(value = "/create",params= {"action=checkDuplicateEmail"}, method =  {RequestMethod.POST,RequestMethod.GET})
  	public String doCheckDuplicateEmail(@Valid @ModelAttribute("createRiaUserForm") CreateRiaUserForm form,BindingResult bindingResult,
  			HttpServletRequest request, HttpServletResponse response)
  					throws IOException, ServletException, SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doEmailExists() in CreateRiaUserAction");
		}

		String contractJsonObj = request.getParameter("jsonObj");
		String responseText = "{";
		String status = StringUtils.EMPTY;
		if(StringUtils.isNotEmpty(contractJsonObj)){
			int riaEmailCount = BDWebCommonUtils
					.getRegisteredEmailCount(contractJsonObj);
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
					"IOException occured while checking the existence of Email "
							+ contractJsonObj);
		}

		out.print(responseText.trim());
		out.flush();
		return null;
	}

    /**
     * The validation methods for the form
     * 
     * @param riaUserForm
     * @return List<ValidationError>
     * @throws SystemException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private List<ValidationError> validate(CreateRiaUserForm riaUserForm, HttpServletRequest request) throws SystemException {
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

        if (errors.size() > 0 || FrwValidator.getInstance().validateSanitizeCatalogedFormFields(riaUserForm, errors, request) == false) {
        	if(logger.isDebugEnabled()) {
			    logger.debug("exit <- validate");
		    }
			return errors;
		}

        isValid = firstNameMandatoryRule.validate(CreateRiaUserForm.FIELD_FIRST_NAME, errors, riaUserForm
                .getFirstName());
        if (isValid) {
            isValid = invalidFirstAndLastNameValueRErule.validate(CreateRiaUserForm.FIELD_FIRST_NAME,
                    tempArrayList, riaUserForm.getFirstName());
            if (!isValid) {
                errors
                        .add(new ValidationError(CreateRiaUserForm.FIELD_FIRST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { FIRST_NAME_LABEL }));
            }
         }
        isValid = lastNameMandatoryRule.validate(CreateRiaUserForm.FIELD_LAST_NAME, errors, riaUserForm
                .getLastName());
        if (isValid) {
            isValid = invalidFirstAndLastNameValueRErule.validate(CreateRiaUserForm.FIELD_LAST_NAME,
                    tempArrayList, riaUserForm.getLastName());
            if (!isValid) {
                errors
                        .add(new ValidationError(CreateRiaUserForm.FIELD_LAST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { LAST_NAME_LABEL }));
            }
        }
        // Phone Number Validation
		if (StringUtils.isEmpty(riaUserForm.getPhoneNumber().getAreaCode())
				|| StringUtils.isEmpty(riaUserForm.getPhoneNumber()
						.getNumber1())
				|| StringUtils.isEmpty(riaUserForm.getPhoneNumber()
						.getNumber2())) {
			errors.add(new ValidationError(
					CreateRiaUserForm.FIELD_PHONE_NUMBER,
					BDErrorCodes.MISSING_PHONE_NUMBER));
		} else {
			PhoneNumberRule.getInstance().validate(
					CreateRiaUserForm.FIELD_PHONE_NUMBER, errors,
					riaUserForm.getPhoneNumber());
		}
        
		EmailAddressRule.getInstance().validate(CreateRiaUserForm.FIELD_EMAIL, errors,
                riaUserForm.getEmailAddress());
		
        if (StringUtils.isEmpty(riaUserForm.getFirmListStr())) {
            errors.add(new ValidationError(CreateRiaUserForm.FIELD_FIRMS,
                    BDErrorCodes.RIA_USER_CREATION_MISSING_FIRMS));
            
        }
        if (StringUtils.isNotEmpty(riaUserForm.getFirmListStr())) {
			String permissionCheck = riaUserForm.getFirmListStr();
			String[] buf = permissionCheck.split(",");
			 if (buf.length > 20) {
				errors.add(new ValidationError(
						BrokerUserManagementForm.FIELD_FIRMS,
						BDErrorCodes.USER_MANAGEMENT_VIEW_RIA_FIRM_PERMISSION_EXCEEDED_BROKER));
			}	
		}
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> validate");
        }
        return errors;
    }
    
    protected String doInput(@Valid @ModelAttribute("createRiaUserForm") CreateRiaUserForm form, HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if (StringUtils.equals("y", request.getParameter("start"))) {
			CreateRiaUserProcessContext context = (CreateRiaUserProcessContext) getProcessContext(request);
			if (context!= null) {
				context.reset();
			}
		}
		
		String forward=super.doInput( form, request, response);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	
}
