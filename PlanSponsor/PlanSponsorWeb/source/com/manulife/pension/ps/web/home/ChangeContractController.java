package com.manulife.pension.ps.web.home;

import java.io.IOException;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWError;

@Controller
@RequestMapping( value = "/home")

public class ChangeContractController extends PsController {
	
	@ModelAttribute(" homePageForm ") 
	public  HomePageForm  populateForm()
	{
		return new  HomePageForm ();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/home/secureHomePage.jsp");
		forwards.put("selectContractPage","/do/home/selectContractDetail/");
		forwards.put("searchContractPage","/do/home/searchContractDetail/"); 
		forwards.put("error","/home/selectContractPage.jsp");
	}

	

	public ChangeContractController() {
		super(ChangeContractController.class);
	}
	
	@RequestMapping(value ="/ChangeContract/" , method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("homePageForm") HomePageForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("error");//if input forward not //available, provided default
        	}
        }  
		
		// get the user profile object and set the current contract to null
		UserProfile userProfile = getUserProfile(request);
        if (userProfile != null) {
            if (userProfile.getNumberOfContracts() == 1 &&
            	!userProfile.getRole().isTPA() &&
            	userProfile.getCurrentContract() != null) {
                return Constants.HOMEPAGE_FINDER_FORWARD;
            } else {
			userProfile.setCurrentContract(null);
            }
        }
		SessionHelper.clearSession(request);
		
		//need to reset these 2 session variables.
		//can't put it in clearSession method.
		SessionHelper.unsetMCLeftMCFromGlobalContext(request);


		return  userProfile.getNumberOfContracts() <= 20 ? forwards.get("selectContractPage")
																: forwards.get("searchContractPage");
	}
	

	 /** This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	
	@Autowired
	   private PSValidatorFWError  psValidatorFWError;

	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWError);
	}
}