package com.manulife.pension.ps.web.messagecenter;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;	
import org.springframework.web.bind.annotation.InitBinder;	
import org.springframework.web.bind.ServletRequestDataBinder;		
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.HashMap;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;



import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWSummary;
import com.manulife.pension.service.message.valueobject.RecipientMessageInfo;


/**
 * Action to complete the Message.
 * 
 * @author guweigu
 *
 */
@Controller
@RequestMapping(value="/messagecenter")

public class MCMessageCompleteController extends MCMessageController {
	
	@ModelAttribute("dynaForm") 
	public DynaForm populateForm() 
	{
		return new DynaForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("summary","redirect:/do/messagecenter/summary");
		forwards.put("multiSummary","redirect:/do/messagecenter/summary");
		forwards.put("multiSummary","redirect:/do/messagecenter/summary");
		}

	public MCMessageCompleteController() {
		logger = Logger.getLogger(MCMessageCompleteController.class);
	}
	
	/**
	 * Mark the message as completed
	 */
	@Override
	protected void doAction(HttpServletRequest request, RecipientMessageInfo message) throws SystemException {
		getMessageServiceFacade().completeMessage(
				SessionHelper.getUserProfile(request), message.getId());

	}
	@RequestMapping(value ="/completeMessage", method ={RequestMethod.POST,RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm form,BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return 	forwards.get("summary");//if input forward not //available, provided default
			}
		}
		String forward=super.doExecute( form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
	}
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations.
   	 */
	@Autowired
    private PSValidatorFWSummary psValidatorFWSummary;  

	@InitBinder
	protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		binder.addValidators(psValidatorFWSummary);
	}
}
