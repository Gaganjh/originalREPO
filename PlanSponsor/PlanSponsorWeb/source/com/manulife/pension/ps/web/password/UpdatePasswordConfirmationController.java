package com.manulife.pension.ps.web.password;

import java.util.HashMap;

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

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.ps.web.Constants;

/**
 * @author Chris Shin
 * @version CS 1.0
 */
@Controller
@RequestMapping( value = "/password")
@SessionAttributes({"updatePasswordForm"})
public class UpdatePasswordConfirmationController extends PsController {
	@ModelAttribute("updatePasswordForm") 
	public UpdatePasswordForm populateForm() 
	{
		return new UpdatePasswordForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	private static final String DEFAULT = "default";
	private static final String FINISHED = "finished";
	private static final String INPUT = "input";
	private static final String UPDATE_PASSWORD_CONFIRMATION_PAGE = "/password/updatePasswordConfirmation.jsp";
	static {
		forwards.put(INPUT, UPDATE_PASSWORD_CONFIRMATION_PAGE);
		forwards.put(FINISHED, "redirect:/do/home/homePageFinder/");
		forwards.put(DEFAULT, UPDATE_PASSWORD_CONFIRMATION_PAGE);

	}

	/**
	 * Constructor.
	 */
	public UpdatePasswordConfirmationController() {
		super(UpdatePasswordConfirmationController.class);
	}

	
	
	
	@RequestMapping(value = "/updatePasswordConfirmation/",  method =  { RequestMethod.GET,RequestMethod.POST }) 
	public String doExecute(@Valid @ModelAttribute("updatePasswordForm") UpdatePasswordForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	           return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);
	       }
		}

		String forward = null;

		if (actionForm.getButton()!= null) {
		 	if (actionForm.getButton().equals(FINISHED)) {
				request.getSession(false).removeAttribute("updatePasswordForm");
				if(actionForm.isLoginFlow()) {
					forward = CommonConstants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
				}
				else {
					forward = forwards.get(FINISHED);
				}	
		 	} else {
				forward = forwards.get(DEFAULT);					
				actionForm.setButton(null);
			}
		} else {
			forward = CommonConstants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		// changes for US
		request.getSession(false).removeAttribute(Constants.PASSWORD_CHANGE_NEW_USER);
		// end changes for US
		return forward;
	}
	
	
	
		/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations.
   	 */
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		//binder.bind(request);
		//binder.addValidators(psValidatorFWDefault);
	}

}