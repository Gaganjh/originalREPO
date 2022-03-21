package com.manulife.pension.bd.web.registration;

import java.io.IOException;
import java.util.HashMap;

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

import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;

/**
 * This is the action class for the step 3 of Broker Registration
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping( value = "/registerExternalBroker")

public class RegisterBrokerStep3Controller extends BDPublicWizardProcessController {
	@ModelAttribute("registrationCompleteForm") 
	public RegistrationCompleteForm populateForm() 
	{
		return new RegistrationCompleteForm();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/registration/registerBrokerStep3.jsp");
		forwards.put("fail","/registration/registerBrokerStep2.jsp");
		}
	private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static String HomeRedirect=HomeRedirect1.getPath();
    /**
     * Constructor
     */
    public RegisterBrokerStep3Controller() {
        super(RegisterBrokerStep3Controller.class, RegisterBrokerProcessContext.class,
                RegisterBrokerProcessContext.ProcessName, RegisterBrokerProcessContext.Step3);
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
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("entry -> doContinue");
            }
            return getState().getNext(BDWizardProcessContext.ACTION_CONTINUE);
        } catch (Exception e) {
            logger.error(e);
            throw new SystemException(e, e.getMessage());
        }
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
	
	  @RequestMapping(value = "/broker/step3", method = { RequestMethod.GET })
		protected String doInput(
				@Valid @ModelAttribute("registrationCompleteForm") RegistrationCompleteForm form,
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

		@RequestMapping(value = "/broker/step3", params = { "action=continue" }, method = { RequestMethod.POST })
		protected String doContinue(
				@Valid @ModelAttribute("registrationCompleteForm") RegistrationCompleteForm form,
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
		@RequestMapping(value = "/broker/step3", params = { "action=cancel" }, method = { RequestMethod.POST })
		protected String doCancel(
				@Valid @ModelAttribute("registrationCompleteForm") RegistrationCompleteForm form,
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
}
