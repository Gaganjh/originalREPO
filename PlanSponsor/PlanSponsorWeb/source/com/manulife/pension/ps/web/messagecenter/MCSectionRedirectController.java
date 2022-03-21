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
import com.manulife.pension.platform.web.controller.ControllerRedirect;

import com.manulife.pension.ps.web.DynaForm;

import com.manulife.pension.ps.web.messagecenter.util.MCUrlGeneratorFactory;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWMCSelectSummary;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;

@Controller
@RequestMapping(value="/messagecenter")


public class MCSectionRedirectController extends MCAbstractController {
	@ModelAttribute("dynaForm") 
	public DynaForm populateForm() 
	{
		return new DynaForm();
	}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("summary","redirect:/do/messagecenter/summary");
		forwards.put("multiSummary","redirect:/do/messagecenter/summary"); 
		forwards.put("detail","/messagecenter/detail_tab.jsp");
		forwards.put("multiDetail","/messagecenter/multi_detail_tab.jsp");
		}

	
	
	@RequestMapping(value="/sectionRedirect", method ={RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
		     String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		     if(errDirect!=null){
		      request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
		      forwards.get("summary");//if input forward not //available, provided default
		    }
		}

		int sectionId = MCUtils.getId(request, ParamSectionId);
		if (sectionId == -1) {
			return forwards.get(getSummaryForward( request));
		}
		MessageCenterComponent top = MCUtils.getMessageCenterTree(request.getServletContext());
		for (MessageCenterComponent tab : top.getChildren()) {
			for (MessageCenterComponent section : tab.getChildren()) {
				if (section.getId().getValue().equals(sectionId)) {
					MCUtils.getPreference(request).reset();
					// find the tab and section redirect to the page
					ControllerRedirect	f= new ControllerRedirect(MCUrlGeneratorFactory.getInstance().getSelectSectionUrl(tab, section));
					return f.getPath();
				}
			}
		}
		return forwards.get(getSummaryForward( request));
	}
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations.
   	 */
	
	
	
	 @Autowired
	   private PSValidatorFWMCSelectSummary  psValidatorFWMCSelectSummary;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWMCSelectSummary);
	}
	
}
