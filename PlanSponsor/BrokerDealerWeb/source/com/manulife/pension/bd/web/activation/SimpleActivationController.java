package com.manulife.pension.bd.web.activation;

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
import com.manulife.pension.bd.web.process.BDProcessContextHelper;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessContext;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.service.security.bd.BDUserProfileStatus;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequest.Status;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequest.Type;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequestEx;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * The reusable activation action for the simple validation and activation
 * process.
 * 
 * Each different type of activation needs to defines its own ActivationHandler
 * in the process context
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping( value = "/activation")

public class SimpleActivationController extends BDPublicWizardProcessController {
	@ModelAttribute("activationValidationForm") 
	public ActivationValidationForm populateForm() 
	{
		return new ActivationValidationForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/activation/activationValidation.jsp");
		forwards.put("fail","/activation/activationValidation.jsp");
		}

	public SimpleActivationController() {
		super(SimpleActivationController.class,
				SimpleActivationProcessContext.class,
				SimpleActivationProcessContext.ProcessName,
				SimpleActivationProcessContext.InputState);
	}

	/**
	 * Handling the case when the Login button is clicked
	 */
	@Override
	protected ProcessState doContinue( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		SimpleActivationProcessContext context = (SimpleActivationProcessContext) getProcessContext(request);
		ActivationHandler handler = context.getActivationHandler();
		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		if (handler == null) {
			// it is not in the right state
			errors.add(new ValidationError("", 0));
			return getState().getNext(BDWizardProcessContext.ACTION_CANCEL);
		}

		// Invoke common context validation, validate the user credential
		errors.addAll(context.validate(request));

		// If validation is OK, invoke the activation
		if (errors.isEmpty()) {
			errors.addAll(handler.activate(context.getSecurityRequest()));
		}

		// If there are errors, return to current page
		if (!errors.isEmpty()) {
			setErrorsInSession(request, errors);
			return getState();
		}

		// successfully activates the record and proceed to login
		try {
			errors = LoginHandler.getInstance().doLogin(context.getUserId(),
					context.getPassword(), request, response, null);
			if (errors.isEmpty()) {
				Type activationType = context.getSecurityRequest().getType();
				BDUserProfile profile = BDSessionHelper.getUserProfile(request);
				/*
				 * Activation type needs to be set in order to direct the post
				 * login flow
				 */
				if (profile != null) {
					profile.setActivationType(activationType);
				}
			} else {
				// special check whether could not login due to 
				// PENDING_BROKER
				if (errors.get(0).getErrorCode() == BDErrorCodes.NO_ACTIVE_BROKER_ENTITY) {
					context.setDisabled(true);
					context.setBrokerPendingWarning(true);
					return getState();
				}
			}
		} catch (ServletException e) {
			logger.error(e);
			throw new SystemException(e, e.getMessage());
		} catch (IOException e) {
			logger.error(e);
			throw new SystemException(e, e.getMessage());
		}
		// it is error for login, so let the flow to home page
		if (!errors.isEmpty()) {
			setErrorsInSession(request, errors);
		}
		return getState().getNext(BDWizardProcessContext.ACTION_CONTINUE);
	}

	/**
	 * This action can not create new context.
	 */
	@Override
	protected ProcessContext getNewProcessContext() throws SystemException {
		return null;
	}

	/**
	 * Validates the current state and display the login page
	 */
	@RequestMapping(value ="/validation", method =  {RequestMethod.GET}) 
	public String doInput(@Valid @ModelAttribute("activationValidationForm") ActivationValidationForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("fail");//if input forward not //available, provided default
			}
		}
		if (BDProcessContextHelper.getProcessContext(request,
				SimpleActivationProcessContext.ProcessName) == null) {
			ControllerRedirect f=	 new ControllerRedirect(URLConstants.HomeURL);
			return f.getPath();
		}
		String forward=	super.doExecuteInput(request);
		if(forward == null)
		{
		SimpleActivationProcessContext context = (SimpleActivationProcessContext) BDProcessContextHelper
				.getProcessContext(request,
						SimpleActivationProcessContext.ProcessName);
		// If the context is not created, send back to home page
		if (context == null) {
			ControllerRedirect f= new ControllerRedirect(URLConstants.HomeURL);
			return f.getPath();
		}
		BDSecurityInteractionRequestEx secRequest = context
				.getSecurityRequest();
		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		// validate the security request reocrd's status to determined whether
		// the activation is allowed
		if (secRequest == null) {
			errors.add(new ValidationError("",
					BDErrorCodes.ACTIVATION_INVALID_REQUEST));
		} else {
			Status status = secRequest.getStatus();
			if (secRequest.getProfileStatus() != null
					&& secRequest.getProfileStatus().compareTo(
							BDUserProfileStatus.Deleted) == 0) {
				errors.add(new ValidationError("",
						BDErrorCodes.ACTIVATION_PROFILE_DELETED));
			} else if (status.compareTo(Status.Open) != 0) {
				// If the user status is not Open
				int errorCode = 0;
				switch (status) {
				case Failed:
					errorCode = BDErrorCodes.ACTIVATION_STATUS_FAILED;
					break;
				case Completed:
					errorCode = BDErrorCodes.ACTIVATION_STATUS_COMPLETE;
					break;
				case Expired:
					errorCode = BDErrorCodes.ACTIVATION_STATUS_EXPIRED;
					break;
				}
				errors.add(new ValidationError("", errorCode));
			}
		}
		context.setDisabled(!errors.isEmpty());
		Collection<GenericException> existingError = BDSessionHelper.getErrorsInSession(request);
		// only set the new error if there is no errors in session
		if (existingError == null || existingError.size() == 0) {
			setErrorsInSession(request, errors);
		}
		String forward1=super.doInput( form, request, response);
		return StringUtils.contains(forward1, '/') ? forward1 : forwards.get(forward1); 
		
		}
		return forward ;
	}
	
	
	@RequestMapping(value = "/validation",params= {"action=continue"},method = {RequestMethod.POST})
	protected String doContinue(@Valid @ModelAttribute("activationValidationForm") ActivationValidationForm form,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return	forwards.get("fail");//if input forward not //available, provided default
			}
		}
		if (BDProcessContextHelper.getProcessContext(request,
				SimpleActivationProcessContext.ProcessName) == null) {
			ControllerRedirect f=	 new ControllerRedirect(URLConstants.HomeURL);
			return f.getPath();
		}
		String forward=super.doCancelContinue( form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/validation",params= {"action=cancel"},method = {RequestMethod.POST})
	protected String doCancel(@Valid @ModelAttribute("activationValidationForm") ActivationValidationForm form,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return	forwards.get("fail");//if input forward not //available, provided default
			}
		}
		if (BDProcessContextHelper.getProcessContext(request,
				SimpleActivationProcessContext.ProcessName) == null) {
			ControllerRedirect f=	 new ControllerRedirect(URLConstants.HomeURL);
			return f.getPath();
		}
		String forward=super.doCancelContinue( form, request, response);
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
