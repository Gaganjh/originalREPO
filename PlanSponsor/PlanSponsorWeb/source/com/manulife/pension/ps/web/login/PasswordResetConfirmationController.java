package com.manulife.pension.ps.web.login;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;

/**
 * @author Chris Shin
 * @version CS 1.0
 */
@Controller
@RequestMapping(value ="/login")
@SessionAttributes({"passwordResetAuthenticationForm"})

public class PasswordResetConfirmationController extends AbstractPasswordResetController {

	@ModelAttribute("passwordResetAuthenticationForm")
	public PasswordResetAuthenticationForm populateForm() {
		return new PasswordResetAuthenticationForm();
	}

	public static Map<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/login/forgotPasswordConfirmation.jsp");
		forwards.put("default", "/login/forgotPasswordConfirmation.jsp");
		forwards.put("errors", "redirect:/login/login.jsp");
		forwards.put("continue", "redirect:/login/loginServlet");
		forwards.put("restart", "redirect:/do/login/passwordResetAuthentication/");
		forwards.put("login", "/login/login.jsp");
	}
	
	@Autowired
    private PSValidatorFWDefault psValidatorFWDefault;

	/**
	 * Constructor.
	 */
	public PasswordResetConfirmationController() {
		super(PasswordResetConfirmationController.class);
	}

	protected int getStep() {
		return PasswordResetAuthenticationForm.STEP_PASSWORD_CONFIRMATION;
	}

	/*
	 */
	protected String processFormValidatedData(PasswordResetAuthenticationForm form, Collection errors,
			 HttpServletRequest request, HttpServletResponse response, String defaultTarget)
			throws SystemException {

		String target = defaultTarget;
		String username = form.getUsername();
		String password = form.getNewPassword();
		form.clear(request);
		request.getSession(false).removeAttribute("passwordResetAuthenticationForm");

		request.getSession(false).setAttribute(LoginServlet.USERNAME_PARAM, username);
		request.getSession(false).setAttribute(LoginServlet.PASSWORD_PARAM, password);
		// try {
		// THIS section should work as well, but for some
		// unknown reason,when this is used, it throws an IllegalStateException.
		// ServletContext servletContext = getServlet().getServletContext();
		// RequestDispatcher rd =
		// servletContext.getRequestDispatcher("/login/loginServlet?userName="+username+"&password="+password);
		// rd.forward(request,response);
		// } catch (IOException e) {
		// } catch (ServletException e) {
		// }

		return target;
	}

	
	@RequestMapping(value ="/passwordResetConfirmation/",method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute ("passwordResetAuthenticationForm") PasswordResetAuthenticationForm form, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SystemException{
		
		if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(errDirect);
            }
       }
		Collection validationErrors = doValidate(form, request);
		if(!validationErrors.isEmpty()){
			return forwards.get("default");
		}
		String forward = super.doExecute(form, request, response);
		return forwards.get(forward);
	}
	
	  protected void processDefault(PasswordResetAuthenticationForm form,
	  HttpServletRequest request) {
	  
	  //nothing to do at the moment 
		  }
	 

	/**
	 * Validate the input action form.
	 * 
	 */
	protected Collection doValidate(ActionForm form, HttpServletRequest request) {

		Collection errors = super.doValidate(form, request);

		return errors;
	}
	
	@RequestMapping(value ="/passwordResetConfirmation/", params={"button=finished"} , method =  {RequestMethod.POST}) 
	public String doSave(@Valid @ModelAttribute ("passwordResetAuthenticationForm") PasswordResetAuthenticationForm form, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SystemException{
		
		if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(errDirect);
            }
       }
		Collection validationErrors = doValidate(form, request);
		if(!validationErrors.isEmpty()){
			return forwards.get("default");
		}
		String forward = super.doExecute(form, request, response);
		return forwards.get(forward);
	}


	 @InitBinder
	   protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		  binder.addValidators(psValidatorFWDefault);
		
	 }

	

}