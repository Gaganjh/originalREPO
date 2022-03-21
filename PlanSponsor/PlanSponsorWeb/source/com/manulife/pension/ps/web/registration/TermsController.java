package com.manulife.pension.ps.web.registration;

import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.util.SessionHelper;

/**
 * Handles the second step in the registration process
 * @author Tony Tomasone
 * 
 */
@Controller
@RequestMapping( value ="/registration")
@SessionAttributes({"registerForm"})
public final class TermsController extends BaseRegisterController {
	
	
	@ModelAttribute("registerForm") 
	public  RegisterForm populateForm()
	{
		return new  RegisterForm();
	}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	private final static String ACTION_INPUT = "input";
	private final static String ACTION_INPUT_TPA 	= "tpainput";
	private final static String ACTION_CSRF_ERROR	= "csrfErrorPage";
	private final static String ACTION_LOGIN_PAGE	= "loginPage";
	static{
	forwards.put(ACTION_INPUT,"/registration/terms.jsp");
	forwards.put(FORWARD_AUTHENTICATE,"redirect:/do/registration/authentication/");
	forwards.put(FORWARD_TPAAUTHENTICATE,"redirect:/do/registration/tpaauthentication/");
	forwards.put(FORWARD_TERMS,"/registration/terms.jsp");
	forwards.put(FORWARD_REGISTER,"redirect:/do/registration/register/");
	forwards.put(FORWARD_TPAREGISTER, "redirect:/do/registration/tparegister/");
	forwards.put(ACTION_INPUT_TPA,"/registration/terms.jsp");
	forwards.put(ACTION_LOGIN_PAGE, "redirect:/do/login/");
	forwards.put(ACTION_CSRF_ERROR, "/security/CSRFerrorpage.jsp");
	}
	
	/**
	 * Constructor for TermsAction
	 */
	public TermsController() {
		super(TermsController.class);
	}

	/**
	 * Constructor for TermsAction
	 */
	public TermsController(Class clazz) {
		super(clazz);
	}

	
	
	/**
	 * @see PsAutoController#doDefault(RegisterForm,BindingResult, HttpServletRequest, HttpServletResponse)
	 */
	@RequestMapping(value ="/terms/",  method = RequestMethod.GET) 
	public String doDefault(@Valid @ModelAttribute("registerForm") RegisterForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
	
			
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
	
	    // if someone is trying to jump ahead in the 
	    // registration wizard, send them to the right page/step
	    if(!isInCorrectStep(FORWARD_TERMS,actionForm))
	    	return forwards.get(getStepForward(actionForm));
	    	
		if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doDefault");
	    }
	    return forwards.get(ACTION_INPUT);
	}

	/** 
	 * Handles the user clicking the accept button on the terms page.
	 * Uses the dynamic action framework to call this method.
	 * This method just makes sure that the user has accepted the terms.
	 * 
	 * @param actionForm
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ServletException
	 * @throws SystemException
	 * 
	 */
	@RequestMapping(value ="/terms/", params="action=accept" , method =  RequestMethod.POST) 
	public String doAccept(@Valid @ModelAttribute("registerForm") RegisterForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)  {
	
		
		if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doAccept");
	    }
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(ACTION_INPUT);
        	}
        }
	    
	    // if someone is trying to jump ahead in the 
	    // registration wizard, send them to the right page/step
	    if(!isInCorrectStep(FORWARD_TERMS,actionForm))
	    	return forwards.get(getStepForward(actionForm));

		// by this time we've already passed the basic validations
		// now do the business validations
		//	MPR 286.	System must be able to determin that user is an existing site user if the PIN entered is numeric.  All users created on new site are initially provided a alpha PIN.
		//	4.1.4.1.6	System verifies Data  
		//	MPR 287.	System must display an error message if mandatory data has not been entered.
		//	MPR 288.	System must display an error message if SSN and Contract combination have already registered on new site. 
		//	4.1.4.1.7	System verifies data from existing Apollo External User Management tables
		//	MPR 289.	System must display an error message if Apollo is not available.
		//	MPR 290.	System must display an error message if Contract #, SSN does not exist on Apollo External User Table
		//	MPR 291.	System must display an error if PIN entered does not match Contract # and SSN on Apollo External User Table (TLP1206), and increment PIN incorrect PIN count
		//	MPR 292.	System must display an error message if the SSN is not associated with a Client. (Existing TPA users will not be able to register this way)
		//	4.1.4.1.8	System creates profile
		//	MPR 293.	System must set the profile status of a new profile to "New"
		//	MPR 294.	System must set the Password status of a new profile to "reset"
		//	4.1.4.1.10	System must set up permissions for each of Client Users contracts
		//	MPR 295.	System must retrieve from Apollo all of the contract numbers associated with the SSN entered and set the profile up with roles and permissions identified above for each of those contracts. 
	    String forward = null;
   		if(actionForm.getTpa()) 
   			forward = FORWARD_TPAREGISTER;
   		else 
   			forward = FORWARD_REGISTER;
	    
		if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doAccept");
	    }
	    return forwards.get(findForward(forward));
	}
	
	/** 
	 * Handles the user clicking the decline button on the registration page.
	 * Uses the dynamic action framework to call this method.
	 * This method just forwards to the public home page aka the login page
	 * if he or she declines the terms and conditions.
	 * 
	 * @param actionForm
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ServletException
	 * @throws SystemException
	 * 
	 */
	@RequestMapping(value ="/terms/", params="action=decline" , method =  RequestMethod.POST) 
	public String doDecline(@Valid @ModelAttribute("registerForm") RegisterForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response){
	
			
		if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doDecline");
	    }
	    
	    // if the user declines invalidate the session and 
	    // forward them to the login page (aka Public Home Page)
	    SessionHelper.invalidateSession(request, response);
	    	    
		if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doDecline");
	    }
	    return forwards.get(findForward(ACTION_LOGIN_PAGE));
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
	private TermsValidator termsValidator;
	 @InitBinder
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(termsValidator);
	}
	

}

