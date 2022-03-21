
package com.manulife.pension.ps.web.resources;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;


/**
 * Form list Action class
 * gets required value objects , forwards to lifestyleSpinoff.jsp
 * PlanSponsor.
 *
 * @author Chris Shin
 * Jan 2004
 */

@Controller
@RequestMapping( value = "/resources")

public class LifestyleSpinoffController extends PsController {

	@ModelAttribute("dynaForm") 
	public DynaForm populateForm() 
	{
		return new DynaForm();
		
	}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("lifestyleSpinoff","/resources/lifestyleSpinoff.jsp");
	
	}
	
	
	public LifestyleSpinoffController()
	{
		super(LifestyleSpinoffController.class);
	}

	@RequestMapping(value ="/lifestyleSpinoff/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		UserProfile userProfile = getUserProfile(request);

		// if there're no current contract, forward them to the home page finder
		if(userProfile.getCurrentContract()==null)
			return Constants.HOMEPAGE_FINDER_FORWARD;

		return forwards.get("lifestyleSpinoff");
    }

	

}
