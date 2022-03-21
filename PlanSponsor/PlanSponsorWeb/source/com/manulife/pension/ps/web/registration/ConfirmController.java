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
import com.manulife.pension.ps.web.home.HomePageFinderController;
import com.manulife.pension.ps.web.login.LoginServlet;
import com.manulife.pension.ps.web.util.SessionHelper;

/**
 * Handles the fourth step in the registration process
 * 
 * @author Tony Tomasone
 * 
 */
@Controller
@RequestMapping(value = "/registration")
@SessionAttributes({ "registerForm" })

public final class ConfirmController extends BaseRegisterController {
	@ModelAttribute("registerForm")
	public RegisterForm populateForm() {
		return new RegisterForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	private final static String ACTION_INPUT = "input";
	static {
		forwards.put(ACTION_INPUT, "/registration/confirm.jsp");
		forwards.put(FORWARD_AUTHENTICATE, "redirect:/do/registration/authentication/");
		forwards.put(FORWARD_TPAAUTHENTICATE, "redirect:/do/registration/tpaauthentication/");
		forwards.put(FORWARD_TERMS, "redirect:/do/registration/terms/");
		forwards.put(FORWARD_REGISTER, "redirect:/do/registration/register/");
		forwards.put(FORWARD_TPAREGISTER, "redirect:/do/registration/tparegister/");
		forwards.put(FORWARD_CONFIRM, "/registration/confirm.jsp");
		forwards.put(FORWARD_LOGIN, "redirect:/login/loginServlet");
	}

	/**
	 * Constructor for ConfirmAction
	 */
	public ConfirmController() {
		super(AuthenticationController.class);
	}

	/**
	 * Constructor for ConfirmAction
	 */
	public ConfirmController(Class clazz) {
		super(clazz);
	}

	
	@RequestMapping(value = "/confirm/", method = RequestMethod.GET )
	public String doDefault(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}
		SessionHelper.removeErrorsInSession(request);
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(ACTION_INPUT);
        	}
        }
		// the default action just forwards to the input jsp in this case

		// if someone is trying to jump ahead in the
		// registration wizard, send them to the right page/step
		if (!isInCorrectStep(FORWARD_CONFIRM, form))
			return forwards.get(getStepForward(form));

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}
		return forwards.get(ACTION_INPUT);
	}

	/**
	 * Handles the user clicking the continue button on the confirmation page. Uses
	 * the dynamic action framework to call this method. This method trys to login
	 * the user after they have successfully registered.
	 * 
	 * @param form
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ServletException
	 * @throws SystemException
	 * 
	 */
	@RequestMapping(value ={"/confirm/"}, params = "action=continue", method = {RequestMethod.GET,RequestMethod.POST})
	public String doContinue(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doContinue");
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
		if (!isInCorrectStep(FORWARD_CONFIRM, form))
			return forwards.get(getStepForward(form));

		// by this time we've already passed the basic validations
		// now do the business validations
		// MPR 286. System must be able to determin that user is an existing site user
		// if the PIN entered is numeric. All users created on new site are initially
		// provided a alpha PIN.
		// 4.1.4.1.6 System verifies Data
		// MPR 287. System must display an error message if mandatory data has not been
		// entered.
		// MPR 288. System must display an error message if SSN and Contract combination
		// have already registered on new site.
		// 4.1.4.1.7 System verifies data from existing Apollo External User Management
		// tables
		// MPR 289. System must display an error message if Apollo is not available.
		// MPR 290. System must display an error message if Contract #, SSN does not
		// exist on Apollo External User Table
		// MPR 291. System must display an error if PIN entered does not match Contract
		// # and SSN on Apollo External User Table (TLP1206), and increment PIN
		// incorrect PIN count
		// MPR 292. System must display an error message if the SSN is not associated
		// with a Client. (Existing TPA users will not be able to register this way)
		// 4.1.4.1.8 System creates profile
		// MPR 293. System must set the profile status of a new profile to "New"
		// MPR 294. System must set the Password status of a new profile to "reset"
		// 4.1.4.1.10 System must set up permissions for each of Client Users contracts
		// MPR 295. System must retrieve from Apollo all of the contract numbers
		// associated with the SSN entered and set the profile up with roles and
		// permissions identified above for each of those contracts.

		// must remove the form from the session to clean up after we're done
		// call Login method here to login the user and send him/her to the home page
		// finder
		String username = form.getUserName();
		String password = form.getPassword();

	 request.getSession(false).removeAttribute("registerForm");

		request.getSession(false).setAttribute(LoginServlet.USERNAME_PARAM, username);
		request.getSession(false).setAttribute(LoginServlet.PASSWORD_PARAM, password);

		// this causes a message to be displayed on the manage users page. However, if
		// this person only has
		// selected access, the warning message doesn't make sense.

		if (!"Selected access".equals(form.getAccessLevel().getDisplayName())) {
			request.getSession(false).setAttribute(HomePageFinderController.REGISTERED_PARAM, new Boolean(true));
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doContinue");
		}
		return forwards.get(FORWARD_LOGIN);
	}

	
	@RequestMapping(value ={"/confirm/"}, params = "actionLabel=print", method = {RequestMethod.GET,RequestMethod.POST})
	public String doCancel(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doContinue");
		}
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(ACTION_INPUT);
        	}
        }
		
		return forwards.get(ACTION_INPUT);
	}

	
	@Autowired
	private ConfirmValidator confirmValidator;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(confirmValidator);
	}
	

}
