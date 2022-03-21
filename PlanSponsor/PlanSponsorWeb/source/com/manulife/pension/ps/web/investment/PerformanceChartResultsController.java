package com.manulife.pension.ps.web.investment;

import java.util.HashMap;
import java.util.Map;

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
import com.manulife.pension.platform.web.investment.PerformanceChartInputForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;

/**
 * This is the action class that handles the activities on the results page
 *
 * @author nvintila
 * @date Feb 25, 2004
 * @time 2:44:12 PM
 */
@Controller
@RequestMapping( value = "/investment")
@SessionAttributes({"performanceChartInputForm"})

public class PerformanceChartResultsController extends PsController {

	@ModelAttribute("performanceChartInputForm") 
	public  PerformanceChartInputForm populateForm() 
	{
		return new  PerformanceChartInputForm();
	}

	
	public static Map<String,String> forwards = new HashMap<>();
	static{ 
		forwards.put("results","/investment/performanceChartResults.jsp"); 
		forwards.put("input","/investment/performanceChartInput.jsp");
	}

    public PerformanceChartResultsController() {
        super(PerformanceChartResultsController.class);
    }

    
     @RequestMapping(value ="/performanceChartResult/",  method ={RequestMethod.GET}) 
     public String doExecute(@Valid @ModelAttribute("performanceChartInputForm") PerformanceChartInputForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
     throws SystemException {

  
    	 if(bindingResult.hasErrors()){
         	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
         	if(errDirect!=null){
              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
              return forwards.get("input");
         	}
         } 
		     
        actionForm.setButton("");

        return forwards.get("results");
    }
     
     @RequestMapping(value ="/performanceChartResult/", params={"button=next"}, method =  {RequestMethod.GET}) 
     public String doNext(@Valid @ModelAttribute("performanceChartInputForm") PerformanceChartInputForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
     throws SystemException {

  
    	 if(bindingResult.hasErrors()){
         	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
         	if(errDirect!=null){
              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
              return forwards.get("input");
         	}
         } 
	    actionForm.setButton("");

        return forwards.get("input");
    }
     
     @RequestMapping(value ="/performanceChartResult/",params={"button=reset"},  method =  {RequestMethod.GET}) 
     public String doReset(@Valid @ModelAttribute("performanceChartInputForm") PerformanceChartInputForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
     throws SystemException {

  
    	 if(bindingResult.hasErrors()){
         	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
         	if(errDirect!=null){
              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
              return forwards.get("input");
         	}
         } 
          	actionForm.resetFundSelection();
            request.getSession(false).removeAttribute(Constants.CHART_DATA_BEAN);
        
        actionForm.setButton("");

        return forwards.get("input");
    }
    
    /** This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations as part of the CL#137697.
   	 */
     @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

     @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}

}
