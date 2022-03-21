
package com.manulife.pension.ps.web.contentpages;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWGlossary;

/**
 * Glossary page Action class 
 * This class is used to forward the users's request to 
 * the Glossary page
 * 
 * @author Mabel Au
 */
@Controller
@RequestMapping(value="/contentpages")
public class GlossaryController 
{
	private static final String glossaryPage = "glossary";
	protected Logger logger = null;
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("glossary","/contentpages/glossary.jsp");
	}
	
	@Autowired
    private PSValidatorFWGlossary psValidatorGlossary;
	
	@ModelAttribute("glossaryForm")
	public GlossaryForm populateForm(){
		return new GlossaryForm();
	} 
	@RequestMapping( value ="/glossary", method=RequestMethod.GET)
	public String doExecute(@Valid @ModelAttribute("glossaryForm")GlossaryForm inputForm,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response){
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("glossary");//if input forward not //available, provided default
        	} 
		if ( logger.isDebugEnabled() )
			logger.debug(GlossaryController.class.getName()+":forwarding to Glossary Page.");
		}
			
		return forwards.get(glossaryPage);
	}
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations as part of the CL#137697.
	 */

    /**
    * This code has been changed and added to Validate form and request against
    * penetration attack, prior to other validations.
    */
	@InitBinder
    public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
           binder.bind( request);
           binder.addValidators(psValidatorGlossary);
    }
	
}
