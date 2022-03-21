package com.manulife.pension.bd.web.migration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.manulife.pension.bd.web.login.LoginHandler;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.registration.RegistrationCompleteForm;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.service.security.exception.DisabledUserException;
import com.manulife.pension.validator.ValidationError;

@Controller
@RequestMapping(value ="/migration")

public class MigrationCompleteController extends BDWizardProcessController {
	@ModelAttribute("registrationCompleteForm") 
	public RegistrationCompleteForm populateForm() 
	{
		return new RegistrationCompleteForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/migration/complete.jsp");
		}
	private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static String HomeRedirect=HomeRedirect1.getPath();
	private static Map<String, Integer> localSecurityServiceMap = new HashMap<String, Integer>(
			3);
	static {
		localSecurityServiceMap.put(DisabledUserException.class.getName(),
				BDErrorCodes.USER_PROFILE_DELETED);
	}
	
	public MigrationCompleteController() {
		super(MigrationCompleteController.class, MigrationProcessContext.class,
				MigrationProcessContext.PROCESS_NAME,
				MigrationProcessContext.CompleteState);
		
	}
	@Override
	protected ProcessState doContinue( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {

		MigrationProcessContext context = (MigrationProcessContext) getProcessContext(request);
		
        try {
            List<ValidationError> errors = LoginHandler.getInstance().doLogin(
					context.getNewUserId(), context.getNewPassword(), request,
					response, localSecurityServiceMap);
            // Login fails, shouldn't happen normally
            if (!errors.isEmpty()) {
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
	/*@RequestMapping(value = "/complete", method =  {RequestMethod.POST,RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("registrationCompleteForm") Form form,BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("input");//if input forward not //available, provided default
			}
		}
		String forward=super.doExecute( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}*/
	
	@RequestMapping(value = "/complete", method = { RequestMethod.GET })
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

	@RequestMapping(value = "/complete", params = { "action=continue"}, method = { RequestMethod.POST })
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
	
}
