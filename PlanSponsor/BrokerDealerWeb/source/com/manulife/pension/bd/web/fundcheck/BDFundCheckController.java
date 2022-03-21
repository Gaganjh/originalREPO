package com.manulife.pension.bd.web.fundcheck;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseController;

@Controller
@RequestMapping( value = "/fundCheck")
@SessionAttributes({"bdFundCheckForm"})

public class BDFundCheckController extends BaseController {
	@ModelAttribute("bdFundCheckForm")
	public DynaForm populateForm() 
	{
		return new DynaForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("level1Version","redirect:/do/fundcheck/fundCheckL1/");
		forwards.put("level2Version","redirect:/do/fundcheck/fundCheckL2/");
		forwards.put("internalVersion","redirect:/do/fundcheck/fundCheckInternal/");
		}

	public BDFundCheckController() {
		super(BDFundCheckController.class);
	}
	@RequestMapping( method =  RequestMethod.GET) 
	public String doExecute(@Valid @ModelAttribute("bdFundCheckForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if (logger.isDebugEnabled())
			logger.debug("entry -> doExecute");
		String forwardTo=null;
		String forward = null;
		try {
			BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
			if(userProfile != null && userProfile.getRole() != null) {
				BDFundCheckHelper bdFundCheckHelper = new BDFundCheckHelper();
				forward = bdFundCheckHelper.getFundCheckPageVersion(userProfile.getRole().getRoleId());
			}
			forwardTo = forwards.get(forward);
		} catch(Exception ex) {
			logger.error("Exception in BDFundCheckAction : ", ex);
		}
		//The return will forward to the respective page (Level1,Level2 and Internal fundcheck Page)by the user who has logged in. 
		//If the user is not autherized to access the requested page than it will forward to the home page.
		if (logger.isDebugEnabled())
			logger.debug("exit -> doExecute");
		return forwardTo;
	}
	
	 /**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
   	 */ 
	 @Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}

}
