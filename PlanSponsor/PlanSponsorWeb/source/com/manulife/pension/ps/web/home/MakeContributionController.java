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
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWMakeContribution;
import com.manulife.pension.service.security.role.ExternalUser;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.UserRole;

/**
 * This class is added to forward the users to appropriate page when they hit
 * Make Contributions button. MPR84-87 implemented.
 * 
 * 
 * @author Ilker Celikyilmaz
 */
@Controller
@RequestMapping( value = "/home")

public class MakeContributionController extends PsController {
	
	@ModelAttribute("dynaForm") 
	public DynaForm populateForm() {
		return  new DynaForm();
	}
	
	
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	
	static{
		forwards.put("toolsPage","redirect:/do/tools/toolsMenu/"); 
		forwards.put("submissionPage","/do/tools/submissionHistory/");
	}
	

    public MakeContributionController() {
        super(MakeContributionController.class);
    }

    /**
     * @see PsController#doExecute(ActionMapping, ActionForm, HttpServletRequest,
     *      HttpServletResponse)
     */
    @RequestMapping(value ="/makeContribution/",  method =  {RequestMethod.GET}) 
    public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
        UserProfile userProfile = getUserProfile(request);
        UserRole role = userProfile.getRole();
        String forward = forwards.get("toolsPage");
        if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("submissionPage");//if input forward not //available, provided default
	       }
		}
        if ((role instanceof ExternalUser && userProfile.isSubmissionAccess()) || 
        		role instanceof InternalUser) {
            forward =forwards.get("submissionPage");
        }
       return forward;
    }
    
    /**
   	 * This code has been changed and added to Validate form and request against
   	 * penetration attack, prior to other validations.
   	 */
    @Autowired
	private PSValidatorFWMakeContribution  psValidatorFWMakeContribution;

    @InitBinder
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWMakeContribution);
	}

    
}
