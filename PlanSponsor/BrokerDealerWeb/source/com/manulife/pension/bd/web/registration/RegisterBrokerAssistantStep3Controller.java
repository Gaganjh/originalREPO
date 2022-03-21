package com.manulife.pension.bd.web.registration;

import java.io.IOException;
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
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.service.security.exception.DisabledUserException;
import com.manulife.pension.validator.ValidationError;

/**
 * This is the action class for the third step of BrokerAssistant registration.
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping( value = "/registerBrokerAssistant")

public class RegisterBrokerAssistantStep3Controller extends BDPublicWizardProcessController {
	@ModelAttribute("registrationCompleteForm") 
	public RegistrationCompleteForm populateForm()
	{
		return new RegistrationCompleteForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
    private static final String FAIL = "fail";
    private static final String INPUT = "input";
    private static Map<String, Integer> localSecurityServiceMap = new HashMap<String, Integer>(
			3);
	static{
		forwards.put(INPUT,"/registration/registerBrokerAssistantStep3.jsp");
		forwards.put(FAIL,"/registration/registerBrokerAssistantStep2.jsp");
		localSecurityServiceMap.put(DisabledUserException.class.getName(),
				BDErrorCodes.USER_PROFILE_DELETED);
		}


    /**
     * Constructor
     */
    public RegisterBrokerAssistantStep3Controller() {
        super(RegisterBrokerAssistantStep3Controller.class,
                RegisterBrokerAssistantProcessContext.class,
                RegisterBrokerAssistantProcessContext.ProcessName,
                RegisterBrokerAssistantProcessContext.Step3);

    }

    /**
     * This method will take the user to the next step
     * 
     * (non-Javadoc)
     * @see com.manulife.pension.bd.web.process.BDWizardProcessController#doContinue(com.manulife.pension.ezk.web.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ProcessState doContinue( ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doContinue");
        }
        RegisterBrokerAssistantProcessContext context = (RegisterBrokerAssistantProcessContext) getProcessContext(request);
        try {
            List<ValidationError> errors = LoginHandler.getInstance().doLogin(
					context.getUserName(), context.getPassword(), request,
					response, localSecurityServiceMap);
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
				return forwards.get(INPUT);// if input forward not //available, provided default
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
	@RequestMapping(value = "/step3", params = { "action=continue"}, method = { RequestMethod.POST })
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
				return forwards.get(INPUT);// if input forward not //available, provided default
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
