package com.manulife.pension.ps.web.messagecenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.messagecenter.model.MCPreference;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWMCSelectSummary;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;

/**
 * Select a specific section and collapse all the other sections
 * on the same tab
 * 
 * @author guweigu
 *
 */
@Controller
@RequestMapping(value ="/messagecenter")

public class MCSectionSelectController extends MCSectionController {
	
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

	public MCSectionSelectController() {
		logger = Logger.getLogger(MCSectionSelectController.class);
	}
	
	@Override
	protected void updatePreference(HttpServletRequest request,
			MessageCenterComponent selectedTab,
			MessageCenterComponent selectedSection, MCPreference pref)
			throws SystemException {
		if (selectedSection != null) {
			pref.collapseAllSectionButOne(
					selectedTab, selectedSection);
		}

	}
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations.
   	 */
	
	@RequestMapping(value ="/sectionSelect",method={RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm form,BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			
			if(errDirect!=null){
			
				ControllerForward forward = new ControllerForward("summary"+CommonConstants.ERROR_RDRCT,"/do"+ new UrlPathHelper().getPathWithinServletMapping(request),true);

				return forward.getPath();
				
			
			}
		}
		String forward=super.doExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
	}
	
	 @Autowired
	   private PSValidatorFWMCSelectSummary  psValidatorFWMCSelectSummary;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWMCSelectSummary);
	}
}
