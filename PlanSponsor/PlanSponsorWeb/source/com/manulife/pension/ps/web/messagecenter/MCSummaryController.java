package com.manulife.pension.ps.web.messagecenter;

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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.messagecenter.model.MCPreference;
import com.manulife.pension.ps.web.messagecenter.model.MCSummaryTabReportModel;
import com.manulife.pension.ps.web.messagecenter.util.MCEnvironment;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWMCSummary;

/**
 * Action before display the summary_tab.jsp
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping( value ="/messagecenter")

public class MCSummaryController extends MCAbstractSummaryTabController {

	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("summary","/messagecenter/summary.jsp");
		forwards.put("multiSummary","/messagecenter/multi_summary.jsp");
	}

	

	public MCSummaryController() {
		logger = Logger.getLogger(MCSummaryController.class);
	}
	@RequestMapping(value = "/summary", method = {RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws  SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
				//return forwards.get(errDirect) != null ? forwards.get(String.valueOf(errDirect)) : forwards.get("summary");// if
																									// default
	       }
		}
		if ( !MCEnvironment.isMessageCenterAvailable(request)) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		MCPreference preference = MCUtils.getPreference(request);
		MCSummaryTabReportModel model = super.getSummaryTabReportModel(request,
				preference);

		if (model == null) {
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		
		setPrintFriendly(request, model);

		request.setAttribute(AttrModel, model);

		return forwards.get(getSummaryForward(request));
	}
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations.
   	 */
	
	@Autowired
	private PSValidatorFWMCSummary psValidatorFWMCSummary;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWMCSummary);
	}
	
}
