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
import com.manulife.pension.bd.web.process.BDProcessContextHelper;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.service.security.bd.BDUserProfileStatus;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequestEx;
import com.manulife.pension.validator.ValidationError;

/**
 * This is the action class for the initial step of BrokerAssistant registration
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping(value ="/registerBrokerAssistant")

public class RegisterBrokerAssistantStartController extends BDPublicWizardProcessController {
	@ModelAttribute("registerStartForm") 
	public RegisterStartForm populateForm() 
	{
		return new RegisterStartForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
    private static final String FAIL = "fail";
    private static final String INPUT = "input";
	public RegisterBrokerAssistantStartController() {
		super(RegisterBrokerAssistantStartController.class,
				RegisterBrokerAssistantProcessContext.class,
				RegisterBrokerAssistantProcessContext.ProcessName,
				RegisterBrokerAssistantProcessContext.Step0);
	}
	static{
		forwards.put(INPUT,"/registration/registerBrokerAssistantStart.jsp");
		forwards.put(FAIL,"/registration/registerBrokerAssistantStart.jsp");
		}

	
	/* (non-Javadoc)
	 * @see com.manulife.pension.bd.web.process.BDWizardProcessController#doContinue(com.manulife.pension.ezk.web.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ProcessState doContinue( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doContinue");
		}
		// Here, the super.getProcessContext is not used. This is because that
		// we expect the ProcessContext must already be created by
		// UserActivationHandler.
		// Otherwise, it means the user navigate to this url without the proper
		// flow.
		RegisterBrokerAssistantProcessContext context = (RegisterBrokerAssistantProcessContext) BDProcessContextHelper
				.getProcessContext(request, processName);
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doContinue");
		}
		if (context == null || context.getSecurityRequest() == null) {
			return getState();
		} else {
			return getState().getNext(BDWizardProcessContext.ACTION_CONTINUE);
		}
	}

	/**
	 * This method will take the user to the previous step
	 * @param actionForm
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/start",method = {RequestMethod.GET})
	protected String doInput(@Valid @ModelAttribute("registerStartForm") RegisterStartForm actionForm,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return	forwards.get(FAIL);//if input forward not //available, provided default
			}
		}
		
		
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;	
		}
		String forward=	super.doExecuteInput(request);
		//additionalcode for do processs to be added
		// StringUtils.contains(forward, '/') ? forward : forwards.get(forward);chk if path
		if(forward == null)
		{
		RegisterBrokerAssistantProcessContext context = (RegisterBrokerAssistantProcessContext) BDProcessContextHelper
				.getProcessContext(request, processName);
		List<ValidationError> errors = new ArrayList<ValidationError>(3);
		if (context == null || context.getSecurityRequest() == null) {
			errors.add(new ValidationError("",
					BDErrorCodes.ASSITANT_REG_ACTIVATION_NOT_FOUND));
		} else {
			BDSecurityInteractionRequestEx secRequest = context
					.getSecurityRequest();
			if (secRequest.getProfileStatus() != null
					&& secRequest.getProfileStatus().compareTo(
							BDUserProfileStatus.Deleted) == 0) {
				errors.add(new ValidationError("",
						BDErrorCodes.ACTIVATION_PROFILE_DELETED));
			} else {
				switch (secRequest.getStatus()) {
				case Expired:
					errors.add(new ValidationError("",
							BDErrorCodes.ASSISTANT_REG_EXPIRED));
					break;
				case Failed:
					errors.add(new ValidationError("",
							BDErrorCodes.ASSISTANT_REG_LOCKED));
					break;
				case Completed:
					errors.add(new ValidationError("",
							BDErrorCodes.ASSISTANT_REG_REGISTERED));
					break;
				}
			}
		}
		setErrorsInRequest(request, errors);
		actionForm.setDisable(!errors.isEmpty());
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doInput");
		}
		String forward1=super.doInput( actionForm, request, response);
		return StringUtils.contains(forward1, '/') ? forward1 : forwards.get(forward1); 
		
		}
		return forward ;
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
	@RequestMapping(value = "/start", params = { "action=continue" }, method = { RequestMethod.POST })
	protected String doContinue(@Valid @ModelAttribute("registerStartForm") RegisterStartForm form,
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
	@RequestMapping(value = "/start", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(@Valid @ModelAttribute("registerStartForm") RegisterStartForm form,
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
