package com.manulife.pension.bd.web.bob.contract;

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

import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.exception.SystemException;

/**
 * WarrantyNotMetAction class
 * Forwards to warrantyNotMet.jsp
 */
@Controller
@RequestMapping(value ="/bob")

public class WarrantyNotMetController extends BDController {
	@ModelAttribute("warrantyNotMetForm") 
	public WarrantyNotMetForm populateForm() 
	{
		return new WarrantyNotMetForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contract/warrantyNotMet.jsp");
		forwards.put("default","/contract/warrantyNotMet.jsp");
	}

	public WarrantyNotMetController()
	{
		super(WarrantyNotMetController.class);
	}

	@RequestMapping(value ="/contract/warrantyNotMet/",  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("warrantyNotMetForm") WarrantyNotMetForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

	    if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doExecute");
	    }
	    if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doExecute");
	    }
		return forwards.get("default");
    }

}
