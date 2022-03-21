package com.manulife.pension.ps.web.messagecenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.messagecenter.model.MCDetailTabReportModel;
import com.manulife.pension.ps.web.messagecenter.model.MCPreference;
import com.manulife.pension.ps.web.messagecenter.util.MCEnvironment;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWMCDetail;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;

/**
 * Action before forwarding to the detail_tab.jsp. Creates the model and prepare
 * for print-friendly if requested
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping( value = "/messagecenter")

public class MCDetailTabController extends MCAbstractDetailTabController {
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static
	{
		forwards.put("summary","redirect:/do/messagecenter/summary");
		forwards.put("multiSummary","redirect:/do/messagecenter/multi_summary");
		forwards.put("detail","/messagecenter/detail_tab.jsp");
		forwards.put("multiDetail","/messagecenter/multi_detail_tab.jsp");
	}


	public MCDetailTabController() {
		logger = Logger.getLogger(MCDetailTabController.class);
	}

	@RequestMapping(value = "/detail", method = {RequestMethod.GET })
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("detail");//if input forward not //available, provided default
	       }
		}
		if (!(getUserProfile(request).getRole().isExternalUser() || getUserProfile(
				request).isBundledGACAR())) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		if ( !MCEnvironment.isMessageCenterAvailable(request)) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		int tabId = MCUtils.getId(request, ParamTabId);

		MessageCenterComponent top = MCUtils.getMessageCenterTree(request.getServletContext());

		MessageCenterComponent selectedTab = (tabId == -1 ? null : top
				.getChild(tabId));

		if (selectedTab == null) {
			return forwards.get(getSummaryForward(request));
		}

		MCPreference preference = MCUtils.getPreference(request);

		MCDetailTabReportModel model = super.getDetailTabReportModel(request,
				preference, top, selectedTab);
		/*
		 * If no selected tab or the selected tab doesn't have messages
		 */
		if (model == null
				|| model.getTabMessageCount(model.getSelectedTab()) == 0) {
			return forwards.get(getSummaryForward(request));
		}

		setPrintFriendly(request, model);
		request.setAttribute(AttrModel, model);
		return forwards.get(getDetailForward(request));
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
