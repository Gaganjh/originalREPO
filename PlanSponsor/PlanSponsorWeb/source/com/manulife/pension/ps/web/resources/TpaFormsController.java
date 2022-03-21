package com.manulife.pension.ps.web.resources;

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
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWTpaForms;

/**
 * TPA Forms page Action class 
 * This class is used to forward the users's request to 
 * the TPA Forms page
 * 
 * @author Romeo Harricharran
 */


 @Controller
 @RequestMapping( value ="/resources")
public class TpaFormsController extends PsController
{ 
	 @ModelAttribute("dynaForm") 
		public DynaForm populateForm() 
		{
			return new DynaForm();
			
		}
		public static HashMap<String,String>forwards=new HashMap<String,String>();
		static{
			forwards.put("tpaforms","/resources/tpaGaForms.jsp");
			forwards.put("tpaDBforms","/resources/tpaDbForms.jsp");
		} 
		
	 
	private static final String TPA_FORMS = "tpaforms";
	
	public TpaFormsController() 
	{
		super(TpaFormsController.class);
	} 
	
	@RequestMapping(value = {"/tpaGaForms/"},  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("tpaforms");//if input forward not //available, provided default
	       }
		}

		if ( logger.isDebugEnabled() )
			logger.debug(TpaFormsController.class.getName()+":forwarding to Tpa Forms Page.");

		return forwards.get(TPA_FORMS);
	}
	
	@RequestMapping(value = {"/tpaDbForms/"},  method =  {RequestMethod.GET}) 
	public String doDBExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("tpaDBforms");//if input forward not //available, provided default
	       }
		}

		if ( logger.isDebugEnabled() )
			logger.debug(TpaFormsController.class.getName()+":forwarding to Tpa Forms Page.");

		return forwards.get("tpaDBforms");
	}
	/**This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations.
	 * 
	 */ 
	 @Autowired
	   private PSValidatorFWTpaForms  psValidatorFWTpaForms;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWTpaForms);
	}
	
}
