/*
 * Created on May 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.tpadownload;

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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;

/**
 * Glossary page Action class 
 * This class is used to forward the users's request to 
 * the Glossary page
 * 
 * @author Mabel Au
 */

@Controller
@RequestMapping(value ="/tpadownload")
@SessionAttributes({"AboutTedForm" })
public class AboutTEDController extends PsController {
	
	private static final String ABOUT_TED_PAGE = "aboutTED";
	
	@ModelAttribute("AboutTedForm") 
	public AboutTEDForm populateForm() 
	{
		return new AboutTEDForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/tpadownload/aboutTED.jsp");
		forwards.put("aboutTED","/tpadownload/aboutTED.jsp");
	}

	
	public AboutTEDController()
	{
		super(AboutTEDController.class);
	}
	
	@RequestMapping(value ="/aboutTED/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("AboutTedForm") AboutTEDForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  

		if ( logger.isDebugEnabled() )
			logger.debug(AboutTEDController.class.getName()+":forwarding to AboutTED Page.");
			
		return forwards.get(ABOUT_TED_PAGE);
	}
	
	/**This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations.
	 * 
	 */
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}
