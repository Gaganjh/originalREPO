package com.manulife.pension.ps.web.home;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//import com.manulife.pension.heartbeat.monitor.SessionMonitor;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;


@Controller
@RequestMapping( value ="/home")
public class SignoutController extends PsController{

	public SignoutController()
	{
		super(SignoutController.class);
	} 
	

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@RequestMapping( value ="/Signout/", method =  RequestMethod.GET)
	public String doExecute(@Valid @ModelAttribute("psDynaForm")DynaForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
					                throws IOException, ServletException, SystemException
    {
		String forward = "/login/loginPage.jsp"; 
		
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return errDirect!=null?errDirect:forward;//if input forward not //available, provided default
        	}
        } 
		
    	
    	HttpSession session = request.getSession(false);
    	if(session != null)
    	{
    		UserProfile userProfile = getUserProfile(request);
    		/*if ( userProfile.getPrincipal().getRole() instanceof UnallocatedUser )
    			// TODO
    			//forward = mapping.findForward("unallocatedLoginPage");
    		*/
    		// release all locks held by user
    		LockServiceDelegate.getInstance().releaseAllLocksForUser(
    				userProfile.getPrincipal().getProfileId());
    		
    		// Invalidate Session, and remove JSESSIONID cookie.
			// This will logout the user.
			SessionHelper.invalidateSession(request, response);
    		//SessionMonitor.getSessionMonitor().removeSession(session);
    	}
    	return forward;
    }
	
	
	  @InitBinder
	  protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		  binder.bind(request);		  
		  binder.addValidators(psValidatorFWInput);
		
	  }
	
	public static UserProfile getUserProfile(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (UserProfile) session.getAttribute(Constants.USERPROFILE_KEY);
		} else {
			return null;
		}
	}

}