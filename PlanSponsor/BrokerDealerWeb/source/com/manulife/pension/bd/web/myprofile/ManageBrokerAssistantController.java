package com.manulife.pension.bd.web.myprofile;

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

import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.userprofile.BDBrokerUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

/**
 * The action to handle Remove assistant and Resend invitation email 
 * 
 * @author guweigu
 *
 */
@Controller
@RequestMapping(value ="/myprofile")

public class ManageBrokerAssistantController extends BDController {
	@ModelAttribute("manageBrokerAssistantForm") 
	public ManageBrokerAssistantForm populateForm() 
	{
		return new ManageBrokerAssistantForm();
		}
	public static HashMap<String,String> forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/myprofile/assistants.jsp");
		forwards.put("success","redirect:/do/myprofile/assistants");
		forwards.put("fail","/myprofile/assistants.jsp");
		}

	private static final String ResendActivationAction = "resendActivation";
	private static final String RemoveAction = "remove";

	public ManageBrokerAssistantController() {
		super(ManageBrokerAssistantController.class);
	}

	 @RequestMapping(value = "/manageAssistant", method =  {RequestMethod.POST})
	   	public String doExecute(@Valid @ModelAttribute("manageBrokerAssistantForm") ManageBrokerAssistantForm actionForm,BindingResult bindingResult,
	   			HttpServletRequest request, HttpServletResponse response)
	   					throws IOException, ServletException, SystemException {
	    	
	    	if(bindingResult.hasErrors()){
	       		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       		if(errDirect!=null){
	       			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	       			return forwards.get("input");//if input forward not //available, provided default
	       		}
	       	}
		
		BDBrokerUserProfile broker = (BDBrokerUserProfile) BDSessionHelper.getUserProfile(request);
		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		if (StringUtils.equals(actionForm.getAction(), RemoveAction)) {
			try {
				BDUserSecurityServiceDelegate.getInstance().removeAssistant(
						BDSessionHelper.getUserProfile(request).getBDPrincipal(),
						actionForm.getUserProfileId());
				broker.removeDisableAssistant(actionForm.getUserProfileId());
			} catch (SecurityServiceException e) {
				logger.debug("Fail to remove assistant", e);
				errors
						.add(new ValidationError(
								"",
								SecurityServiceExceptionHelper
										.getErrorCode(
												e,
												MyProfileUtil.MyProfileSecurityServiceExceptionMapping)));
			}
		} else if (StringUtils.equals(actionForm.getAction(), ResendActivationAction)) {
			try {
				BDUserSecurityServiceDelegate.getInstance().resendAssistantActivationEmail(
						BDSessionHelper.getUserProfile(request).getBDPrincipal(),
						actionForm.getUserProfileId());
				broker.addDisableAssistant(actionForm.getUserProfileId());
			} catch (SecurityServiceException e) {
				logger.debug("Fail to resend activation email assistant", e);
				errors
						.add(new ValidationError(
								"",
								SecurityServiceExceptionHelper
										.getErrorCode(
												e,
												MyProfileUtil.MyProfileSecurityServiceExceptionMapping)));
			}
			if (errors.isEmpty()) {
				request.getSession().setAttribute("successContent",
						BDContentConstants.RESNED_INVITE_ASSISTANT_TEXT);
			}
		}
		 if (!errors.isEmpty()) {
			setErrorsInSession(request, errors);
		}
		MyProfileContext context = MyProfileUtil.getContext(request
				.getServletContext(), request);
		context.getNavigation().setCurrentTabId(
				MyProfileNavigation.AssistantsTabId);

		return forwards.get("success");
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
