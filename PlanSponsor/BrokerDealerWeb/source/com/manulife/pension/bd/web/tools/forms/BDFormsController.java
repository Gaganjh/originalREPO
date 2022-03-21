package com.manulife.pension.bd.web.tools.forms;

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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFormsMenu;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;

/**
 * Form list Action class gets required value objects , forwards to forms.jsp
 * new broker dealer web site.
 * 
 * @author aambrose
 */
@Controller
@RequestMapping(value ="/forms/")

public class BDFormsController extends BDController {
	@ModelAttribute("bdFormsForm") 
	public BDFormsForm populateForm()
	{
		return new BDFormsForm();
		}

	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("formsMenu","/tools/forms/bdForms.jsp");
		}

	public BDFormsController() {
		super(BDFormsController.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseAction#doExecute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@RequestMapping( method =  {RequestMethod.POST,RequestMethod.GET}) 
	 public String doExecute(@Valid @ModelAttribute("bdFormsForm") BDFormsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {	
		 if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 forwards.get("formsMenu");//if input forward not //available, provided default
			 }
		 }	

		// check if userProfile is available
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);

		// if there're no userProfile, forward them to the home page finder
		if (userProfile == null) {
			return BDConstants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		BDSessionHelper.removeBOBTabSelectionFromSession(request);
		return forwards.get(CommonConstants.FORMS_MENU);
	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
	
	@Autowired
	   private BDValidatorFWFormsMenu  bdValidatorFWFormsMenu;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWFormsMenu);
	}
}
