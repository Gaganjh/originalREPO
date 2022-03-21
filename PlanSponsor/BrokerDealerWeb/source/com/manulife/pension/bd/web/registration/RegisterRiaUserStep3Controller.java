
package com.manulife.pension.bd.web.registration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.manulife.pension.bd.web.login.LoginHandler;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.bd.web.validation.pentest.FrwValidator;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.service.security.exception.DisabledUserException;
import com.manulife.pension.validator.ValidationError;

/**
 * The last step of register RIA User. It allows the user to login from this page.
 * 
 * @author narintr
 *
 */
@Controller
@RequestMapping(value="/registerRiaUser")

public class RegisterRiaUserStep3Controller extends BDPublicWizardProcessController {
	@ModelAttribute("registrationCompleteForm") 
	public RegistrationCompleteForm populateForm() 
	{
		return new RegistrationCompleteForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	
	static{
		forwards.put("input","/registration/registerRiaUserStep3.jsp");
		forwards.put("fail","/registration/registerRiaUserStep2.jsp");
		}
	private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static String HomeRedirect=HomeRedirect1.getPath();
	private static Map<String, Integer> localSecurityServiceMap = new HashMap<String, Integer>(
			3);
	static {
		localSecurityServiceMap.put(DisabledUserException.class.getName(),
				BDErrorCodes.RIAUSER_USER_PROFILE_DELETED);
	}

    public RegisterRiaUserStep3Controller() {
        super(RegisterRiaUserStep3Controller.class, RegisterRiaUserProcessContext.class,
        		RegisterRiaUserProcessContext.ProcessName, RegisterRiaUserProcessContext.Step3);

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
    @SuppressWarnings("rawtypes")
    @Override
    protected ProcessState doContinue( ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doContinue");
        }
        

        List errors = new ArrayList();
        if (FrwValidator.getInstance().validateSanitizeCatalogedFormFields(form, errors, request) == false) {
        	 setErrorsInSession(request, errors);
        	 if (logger.isDebugEnabled()) {
                 logger.debug("exit -> validate");
             }
        	 return getState();
        }
        
        RegisterRiaUserProcessContext context = (RegisterRiaUserProcessContext) getProcessContext(request);
        try {
            //List<ValidationError> 
            errors = LoginHandler.getInstance().doLogin(
					context.getUserName(), context.getPassword(), request,
					response, localSecurityServiceMap);
            // TODO:User selects Log Me In button and the user profile status is deleted. Validation
            // pending.
            if (logger.isDebugEnabled()) {
                logger.debug("exit -> doContinue");
            }
            if (errors.size() > 0) {
                setErrorsInSession(request, errors);
                return getState();
            } else {
                return getState().getNext(BDWizardProcessContext.ACTION_CONTINUE);
            }
        } catch (ServletException e) {
            logger.error(e);
            throw new SystemException(e, e.getMessage());
        } catch (IOException e) {
            logger.error(e);
            throw new SystemException(e, e.getMessage());
        }
	}
   
	@RequestMapping(value = "/step3", method = { RequestMethod.GET })
	protected String doInput(@Valid @ModelAttribute("registrationCompleteForm") RegistrationCompleteForm form,
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
		String forward = super.doExecuteInput(request);
		if (forward == null) {
			String forward1 = super.doInput(form, request, response);
			return StringUtils.contains(forward1, '/') ? forward1 : forwards.get(forward1);
		}
		return forward;
	}

	@RequestMapping(value = "/step3", params = { "action=continue" }, method = { RequestMethod.POST })
	protected String doContinue(@Valid @ModelAttribute("registrationCompleteForm") RegistrationCompleteForm form,
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
	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}
	
	
}
