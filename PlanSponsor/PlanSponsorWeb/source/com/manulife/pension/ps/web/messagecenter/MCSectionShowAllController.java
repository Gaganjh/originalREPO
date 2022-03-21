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
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.messagecenter.model.MCPreference;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWMCDetail;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;

/**
 * Show all the message in one section
 * 
 * @author guweigu
 *
 */
@Controller
@RequestMapping(value="/messagecenter")

public class MCSectionShowAllController extends MCSectionController {
	
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

	public MCSectionShowAllController() {
		logger = Logger.getLogger(MCSectionShowAllController.class);
	}

	@Override
	protected void updatePreference(HttpServletRequest request,
			MessageCenterComponent selectedTab,
			MessageCenterComponent selectedSection, MCPreference pref)
			throws SystemException {
		pref.setSectionShowAll(selectedSection, 
				"true".equals(request
						.getParameter(MCConstants.ParamShowAll)));
		
	}

	@RequestMapping(value ="/sectionShowAll", method = {RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm form,BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				/*request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return 	forwards.get("detail");//if input forward not //available, provided default
*/		ControllerForward forward =	new ControllerForward("detail"+
		CommonConstants.ERROR_RDRCT, "/do" + "/messagecenter/detail"+"?tabId="+MCUtils.getId(request, ParamTabId),true);
			
		return forward.getPath();
			}
		}
		String forward=super.doExecute( form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
	}
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations.
   	 */
	 @Autowired
	   private PSValidatorFWMCDetail  psValidatorFWMCDetail;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWMCDetail);
	}
}
