package com.manulife.pension.ps.web.registration;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.security.exception.FailedAlmostNTimesException;
import com.manulife.pension.service.security.exception.IncorrectPasswordException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.content.GenericException;

/**
 * Handles the first step in the registration process
 * @author Tony Tomasone
 * 
 */
@Controller
@RequestMapping( value = "/registration")
@SessionAttributes({"registerForm"})

public final class AuthenticationController extends BaseRegisterController {

	@ModelAttribute("registerForm")
	public RegisterForm populateForm() 
	{
		return new RegisterForm();
	}

	public static HashMap<String,String>forwards=new HashMap<String,String>();
	private final static String ACTION_INPUT = "input";
	private final static String ACTION_INPUT_TPA 	= "tpainput";
	private final static String ACTION_CSRF_ERROR	= "csrfErrorPage";
	static{
	forwards.put(ACTION_INPUT,"/registration/authentication.jsp");
	forwards.put(FORWARD_AUTHENTICATE,"/registration/authentication.jsp");
	forwards.put(FORWARD_TERMS, "redirect:/do/registration/terms/");
	forwards.put(FORWARD_LOGIN,"/login/login.jsp");
	forwards.put(ACTION_CSRF_ERROR, "/security/CSRFerrorpage.jsp");
	
	forwards.put(ACTION_INPUT_TPA,"/registration/tpaAuthentication.jsp");
	forwards.put(FORWARD_TPAAUTHENTICATE,"/registration/tpaAuthentication.jsp");	
	}
	
	/**
	 * Constructor for AuthenticationAction
	 */
	public AuthenticationController() {
		super(AuthenticationController.class);
	}

	/**
	 * Constructor for AuthenticationAction
	 */
	public AuthenticationController(Class clazz) {
		super(clazz);
	}

	
	@RequestMapping(value = {"/authentication/"} , method = RequestMethod.GET) 
	public String doDefault (@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		
		if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doDefault");
	    }
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(ACTION_INPUT);
        	}
        }
	    	form.setTpa(false);
	    
		if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doDefault");
	    }
		 
		return forwards.get(ACTION_INPUT);
		
	}
	
	@RequestMapping(value = {"/tpaauthentication/"} , method =  RequestMethod.GET) 
	public String dotpaDefault (@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)  {
		
		if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doDefault");
	    }
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(ACTION_INPUT_TPA);
        	}
        }
	    
		// the default action just forwards to the input jsp in this case
	    // need to know how to render the page differently for TPAs
	    // ie show firm id instead of contract number
	    
	    	form.setTpa(true);
	   	    
		if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doDefault");
	    }
		 
		return forwards.get(ACTION_INPUT_TPA);
		
	}
	/** 
	 * Handles the user clicking the continue button on the authentication page.
	 * Uses the dynamic action framework to call this method.
	 * This method actually processes the authentication page request. It 
	 * populates the UserInfo object and then calls the SecurityService 
	 * to do the action user validation.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ServletException
	 * @throws SystemException
	 * 
	 */
	@RequestMapping(value = "/authentication/" ,params="action=continue", method =  RequestMethod.POST) 
	public String doContinue (@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
			
		if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doContinue");
	    }
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(ACTION_INPUT);
        	}
        }
	    
		//  by this time we've already passed the basic validations
		//  now do the business validations
		//	MPR 286.	System must be able to determine that user is an existing site user if 
		//  the PIN entered is numeric.  All users created on new site are initially provided a alpha PIN.
		//	4.1.4.1.6	System verifies Data  
		//	MPR 287.	System must display an error message if mandatory data has not been entered.
		//	MPR 288.	System must display an error message if SSN and Contract combination have already
		//  registered on new site.
		//	Need a service call here to check if they've already registered

		//	4.1.4.1.7	System verifies data from existing Apollo External User Management tables
		//	MPR 289.	System must display an error message if Apollo is not available.
		//	MPR 290.	System must display an error message if Contract #, SSN does not exist on Apollo 
		//  External User Table
		//	MPR 291.	System must display an error if PIN entered does not match Contract # and SSN on 
		//  Apollo External User Table (TLP1206), and increment PIN incorrect PIN count
		//	MPR 292.	System must display an error message if the SSN is not associated with a Client. 
		//  (Existing TPA users will not be able to register this way)
		//	Need a service call here to call apollo to check that contract/ssn exist and get the contract Name
		// 	First name and last name

		//	4.1.4.1.8	System creates profile
		//	MPR 293.	System must set the profile status of a new profile to "New"
		//	MPR 294.	System must set the Password status of a new profile to "reset"
		//	4.1.4.1.10	System must set up permissions for each of Client Users contracts
		//	MPR 295.	System must retrieve from Apollo all of the contract numbers associated with the 
		//  SSN entered and set the profile up with roles and permissions identified above for each of those contracts. 
		//  MPR455 User ID Must be unique within system
		//	Need a service call here to check if the user name is unique
		
		// call SecurityService.validationForRegistration() 
		
		UserInfo userInfo = new UserInfo();
		
		
		boolean validationErrorFound1 = false;
		boolean validationErrorFound2 = false;
		
		//
		// Anonymous penetration error caught in doValidate() is reported in here,
		// to maintain expected page navigation.
		//
		String emsg = (String) request.getSession().getAttribute("error");
		if (!StringUtils.isBlank(emsg)) {
			validationErrorFound1 = (emsg.compareToIgnoreCase(String.valueOf(ErrorCodes.TPA_FIRM_ID_INVALID)) == 0);
			if (validationErrorFound1 == false) {
				validationErrorFound2 = (emsg.compareToIgnoreCase(String.valueOf(CommonErrorCodes.USER_DOES_NOT_EXIST)) == 0);
			}
		}
	
		if (validationErrorFound1 == true || validationErrorFound2 == true) {
			String forward = forwards.get(FORWARD_TERMS);
			request.getSession().removeAttribute("error");
			
			List errors = new ArrayList();
			if (validationErrorFound1 == true) {
				errors.add(new GenericException(ErrorCodes.TPA_FIRM_ID_INVALID));
			}
			else if (validationErrorFound2 == true) {
				errors.add(new GenericException(CommonErrorCodes.USER_DOES_NOT_EXIST));
			}
			
			form.setAuthenticateValid(false);
			setErrorsInRequest(request, errors);
			forward = forwards.get(ACTION_INPUT);
			
			return forward;
		}
				
		populateUserInfo(request, userInfo, form);

		String forward = forwards.get(FORWARD_TERMS);

		SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
		try {
			UserInfo notRegisteredUser = service.validationForRegistration(userInfo,
					Environment.getInstance().getSiteLocation(), IPAddressUtils.getRemoteIpAddress(request));
			
	         // refresh session in order to protect against session fixation exploit
	        SessionHelper.invalidateSessionKeepCookie(request);
            request.getSession().setAttribute("registerForm", form);
            
			form.setFirstName(notRegisteredUser.getFirstName());
			form.setLastName(notRegisteredUser.getLastName());
			form.setDefaultUserName(notRegisteredUser.getUserName());
			if(notRegisteredUser.getMobileNumber() != null) {
				form.setMobile(notRegisteredUser.getMobileNumber());
			}
			if(notRegisteredUser.getPhoneNumber() != null) {
				form.setPhone(notRegisteredUser.getPhoneNumber());
			}	
			form.setExt(notRegisteredUser.getPhoneExtension());
			if(notRegisteredUser.getFax() != null) {
				form.setFax(notRegisteredUser.getFax());
			}

			if(!SecurityConstants.CHANGE_EMAIL.equals(notRegisteredUser.getEmail()))
			{
				form.setEmail(notRegisteredUser.getEmail());
			}
			
		} catch (SecurityServiceException sse) {
			if ( sse instanceof IncorrectPasswordException 
				|| sse instanceof FailedAlmostNTimesException)
				form.increaseExistingSiteUserFailedCount();
				
			List errors = new ArrayList();
			errors.add(new GenericException(Integer.parseInt(sse.getErrorCode())));
			
			// haven't successfully passed this step
			form.setAuthenticateValid(false);
			
			setErrorsInRequest(request, errors);
			
			// send them back to the input page if we get a
			// SecurityServiceException
			forward = forwards.get(ACTION_INPUT);
		}
			
	    
		if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doContinue");
	    }
	    return forward;
	}
	@RequestMapping(value = "/tpaauthentication/" ,params="action=continue", method =  RequestMethod.POST) 
	public String doContinueTpa (@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws  SystemException {
			
		if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doContinue");
	    }
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(ACTION_INPUT_TPA);
        	}
        }
	    
		//  by this time we've already passed the basic validations
		//  now do the business validations
		//	MPR 286.	System must be able to determine that user is an existing site user if 
		//  the PIN entered is numeric.  All users created on new site are initially provided a alpha PIN.
		//	4.1.4.1.6	System verifies Data  
		//	MPR 287.	System must display an error message if mandatory data has not been entered.
		//	MPR 288.	System must display an error message if SSN and Contract combination have already
		//  registered on new site.
		//	Need a service call here to check if they've already registered

		//	4.1.4.1.7	System verifies data from existing Apollo External User Management tables
		//	MPR 289.	System must display an error message if Apollo is not available.
		//	MPR 290.	System must display an error message if Contract #, SSN does not exist on Apollo 
		//  External User Table
		//	MPR 291.	System must display an error if PIN entered does not match Contract # and SSN on 
		//  Apollo External User Table (TLP1206), and increment PIN incorrect PIN count
		//	MPR 292.	System must display an error message if the SSN is not associated with a Client. 
		//  (Existing TPA users will not be able to register this way)
		//	Need a service call here to call apollo to check that contract/ssn exist and get the contract Name
		// 	First name and last name

		//	4.1.4.1.8	System creates profile
		//	MPR 293.	System must set the profile status of a new profile to "New"
		//	MPR 294.	System must set the Password status of a new profile to "reset"
		//	4.1.4.1.10	System must set up permissions for each of Client Users contracts
		//	MPR 295.	System must retrieve from Apollo all of the contract numbers associated with the 
		//  SSN entered and set the profile up with roles and permissions identified above for each of those contracts. 
		//  MPR455 User ID Must be unique within system
		//	Need a service call here to check if the user name is unique
		
		// call SecurityService.validationForRegistration() 
		
		UserInfo userInfo = new UserInfo();
		
		
		boolean validationErrorFound1 = false;
		boolean validationErrorFound2 = false;
		
		//
		// Anonymous penetration error caught in doValidate() is reported in here,
		// to maintain expected page navigation.
		//
		String emsg = (String) request.getSession().getAttribute("error");
		if (!StringUtils.isBlank(emsg)) {
			validationErrorFound1 = (emsg.compareToIgnoreCase(String.valueOf(ErrorCodes.TPA_FIRM_ID_INVALID)) == 0);
			if (validationErrorFound1 == false) {
				validationErrorFound2 = (emsg.compareToIgnoreCase(String.valueOf(CommonErrorCodes.USER_DOES_NOT_EXIST)) == 0);
			}
		}
	
		if (validationErrorFound1 == true || validationErrorFound2 == true) {
			String forward = forwards.get(FORWARD_TERMS);
			request.getSession().removeAttribute("error");
			
			List errors = new ArrayList();
			if (validationErrorFound1 == true) {
				errors.add(new GenericException(ErrorCodes.TPA_FIRM_ID_INVALID));
			}
			else if (validationErrorFound2 == true) {
				errors.add(new GenericException(CommonErrorCodes.USER_DOES_NOT_EXIST));
			}
			
			form.setAuthenticateValid(false);
			setErrorsInRequest(request, errors);
			forward = forwards.get(ACTION_INPUT_TPA);
			
			return forward;
		}
				
		populateUserInfo(request, userInfo, form);

		String forward = forwards.get(FORWARD_TERMS);

		SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
		try {
			UserInfo notRegisteredUser = service.validationForRegistration(userInfo, Environment.getInstance().getSiteLocation(),IPAddressUtils.getRemoteIpAddress(request));
			
	         // refresh session in order to protect against session fixation exploit
	        SessionHelper.invalidateSessionKeepCookie(request);
            request.getSession().setAttribute("registerForm", form);
            
			form.setFirstName(notRegisteredUser.getFirstName());
			form.setLastName(notRegisteredUser.getLastName());
			form.setDefaultUserName(notRegisteredUser.getUserName());
			if(notRegisteredUser.getMobileNumber() != null) {
				form.setMobile(notRegisteredUser.getMobileNumber());
			}
			if(notRegisteredUser.getPhoneNumber() != null) {
				form.setPhone(notRegisteredUser.getPhoneNumber());
			}	
			form.setExt(notRegisteredUser.getPhoneExtension());
			if(notRegisteredUser.getFax() != null) {
				form.setFax(notRegisteredUser.getFax());
			}

			if(!SecurityConstants.CHANGE_EMAIL.equals(notRegisteredUser.getEmail()))
			{
				form.setEmail(notRegisteredUser.getEmail());
			}
			
		} catch (SecurityServiceException sse) {
			if ( sse instanceof IncorrectPasswordException 
				|| sse instanceof FailedAlmostNTimesException)
				form.increaseExistingSiteUserFailedCount();
				
			List errors = new ArrayList();
			errors.add(new GenericException(Integer.parseInt(sse.getErrorCode())));
			
			// haven't successfully passed this step
			form.setAuthenticateValid(false);
			
			setErrorsInRequest(request, errors);
			
			// send them back to the input page if we get a
			// SecurityServiceException
			forward = forwards.get(ACTION_INPUT_TPA);
		}
			
			
	    
		if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doContinue");
	    }
	    return forward;
	}
	

	/** 
	 * Handles the user clicking the cancel button on the authentication page.
	 * Uses the dynamic action framework to call this method.
	 * This method forward the user to the login page.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ServletException
	 * @throws SystemException
	 * 
	 */
	
	@RequestMapping(value ={"/authentication/","/tpaauthentication/"},params="action=cancel",method = RequestMethod.POST) 
	public String doCancel (@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)  {
	
			
			if(logger.isDebugEnabled()) {
			    logger.debug("entry -> doCancel");
		    }
			
			SessionHelper.invalidateSession(request, response, false);
			
			if(logger.isDebugEnabled()) {
			    logger.debug("exit <- doCancel");
		    }
		    
			return forwards.get(FORWARD_LOGIN);					    
	    
		}
	
	
	/**
	 * Validates the form according to the define validation rules.
	 * 
	 * @param form Form objects reference
	 * @param request HttpServletRequest objects reference
	 * @return Collection
	 * 
	 */
	 @Autowired
	 private Authenticationvalidator authenticationvalidator;
	 @InitBinder
	 public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(authenticationvalidator);
	}

}


