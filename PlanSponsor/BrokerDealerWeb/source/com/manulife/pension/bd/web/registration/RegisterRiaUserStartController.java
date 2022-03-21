/**
 * 
 */
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
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDProcessContextHelper;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.service.security.bd.BDUserProfileStatus;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequestEx;
import com.manulife.pension.validator.ValidationError;

/**
 * The starting point of RegisterRiaUser process. This action enforce that user
 * has to go through the Activation process. If user bookmark the URL, it won't
 * be able to move to next step
 * 
 * @author narintr
 *
 */
@Controller
@RequestMapping( value ="/registerRiaUser")

public class RegisterRiaUserStartController extends BDPublicWizardProcessController  {
	@ModelAttribute("registerStartForm") 
	public RegisterStartForm populateForm() 
	{
		return new RegisterStartForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/registration/registerRiaUserStart.jsp");
		forwards.put("fail","/registration/registerRiaUserStart.jsp");
		}
	private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static String HomeRedirect=HomeRedirect1.getPath();
	public RegisterRiaUserStartController() {
		super(RegisterRiaUserStartController.class,
				RegisterRiaUserProcessContext.class,
				RegisterRiaUserProcessContext.ProcessName,
				RegisterRiaUserProcessContext.Step0);
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
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doContinue");
		}
		
		/* Here, the super.getProcessContext is not used. This is because that
		   we expect the ProcessContext must already be created by UserActivationHandler.
		   Otherwise,it means the user navigate to this url without the proper flow.*/		
		
		RegisterRiaUserProcessContext context = (RegisterRiaUserProcessContext) BDProcessContextHelper
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
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	 
	@RequestMapping(value = "/start",method = {RequestMethod.GET})
	protected String doInput(@Valid @ModelAttribute("registerStartForm") RegisterStartForm form,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		
		
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;	
		}
		String forward=	super.doExecuteInput(request);
		//additionalcode for do processs to be added
		// StringUtils.contains(forward, '/') ? forward : forwards.get(forward);chk if path
		if(forward == null)
		{
		RegisterRiaUserProcessContext context = (RegisterRiaUserProcessContext) BDProcessContextHelper
				.getProcessContext(request, processName);
		List<ValidationError> errors = new ArrayList<ValidationError>(3);
		if (context == null || context.getSecurityRequest() == null) {
			errors.add(new ValidationError("",
					BDErrorCodes.RIAUSER_REG_ACTIVATION_NOT_FOUND));
		} else {
			BDSecurityInteractionRequestEx secRequest = context
					.getSecurityRequest();
			if (secRequest.getProfileStatus() != null
					&& secRequest.getProfileStatus().compareTo(
							BDUserProfileStatus.Deleted) == 0) {
				errors.add(new ValidationError("",
						BDErrorCodes.RIAUSER_USER_PROFILE_DELETED));
			} else {
				switch (secRequest.getStatus()) {
				case Expired:
					errors.add(new ValidationError("",
							BDErrorCodes.RIAUSER_REG_EXPIRED));
					break;
				case Failed:
					errors.add(new ValidationError("",
							BDErrorCodes.RIAUSER_REG_LOCKED));
					break;
				case Completed:
					errors.add(new ValidationError("",
							BDErrorCodes.PROFILE_NOT_IN_NEW_STATUS));
					break;
				}
			}
		}
		setErrorsInRequest(request, errors);
		RegisterStartForm rf = (RegisterStartForm) form;
		rf.setDisable(!errors.isEmpty());

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doInput");
		}
		String forward1=super.doInput( form, request, response);
		return StringUtils.contains(forward1, '/') ? forward1 : forwards.get(forward1); 
		
		}
		return forward ;
	}
	
	
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
				return forwards.get("fail");// if input forward not //available, provided default
			}
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		String forward = super.doCancelContinue(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

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
				return forwards.get("fail");// if input forward not //available, provided default
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
