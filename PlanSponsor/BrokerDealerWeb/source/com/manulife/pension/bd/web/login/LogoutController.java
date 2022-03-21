package com.manulife.pension.bd.web.login;

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

import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.userprofile.BDInternalUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWError;
import com.manulife.pension.platform.web.CommonConstants;


@Controller
@RequestMapping(value ="/logout")

public class LogoutController extends BDController {
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("public","redirect:/do/home/" );
		forwards.put("listing","redirect:/do/usermanagement/search?task=refresh" );
		forwards.put("error","/home/public_home.jsp" );
		}

	public LogoutController() {
		super(LogoutController.class);
	}
	
	 @RequestMapping(value ="/",  method =  {RequestMethod.GET}) 
	 public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException {	
		 if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 forwards.get("error");//if input forward not //available, provided default
			 }
		 }
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);

		// exit mimic
		if (userProfile != null && userProfile instanceof BDInternalUserProfile
				&& ((BDInternalUserProfile) userProfile).isInMimic()) {
			return forwards.get("listing");
		} else {
			BDSessionHelper.invalidateSession(request, response);
			return forwards.get("public");
		}
	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
	 @Autowired
	   private BDValidatorFWError  bdValidatorFWError;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWError);
}
}
