package com.manulife.pension.ps.web.messagecenter;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;	
import org.springframework.web.bind.annotation.InitBinder;	
import org.springframework.web.bind.ServletRequestDataBinder;		
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.HashMap;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;


import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;

import com.manulife.pension.ps.web.DynaForm;

import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWSummary;

/**
 * AJAX implementation for setting message status as VISITED
 * 
 */
@Controller
@RequestMapping(value="/messagecenter")

public class MCMessageVisitController extends MCAbstractController {

	@ModelAttribute("dynaForm") 
	public DynaForm populateForm() 
	{
		return new DynaForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{forwards.put("summary","redirect:/do/messagecenter/summary");
	forwards.put("multiSummary","redirect:/do/messagecenter/summary");
			
	}

	@RequestMapping(value ="/visitMessage",  method = {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("summary");//if input forward not //available, provided default
	       }
		}
	
	
			int messageId = MCUtils.getId(request, ParamMessageId);
			getMessageServiceFacade().visitMessage(
					SessionHelper.getUserProfile(request), messageId);

			response.setHeader("Cache-Control", "must-revalidate");
			response.setContentType("text/plain");
			byte[] jsonResultBytes = "success".getBytes();
			response.setContentLength(jsonResultBytes.length);
			
			try {
				response.getOutputStream().write(jsonResultBytes);
			} catch (IOException ioException) {
				throw new SystemException(ioException,
						"Exception writing result.");
			} finally {
				try {
					response.getOutputStream().close();
				} catch (IOException ioException) {
					throw new SystemException(ioException,
							"Exception closing output stream.");
				}
			}
			return null;
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
