package com.manulife.pension.ps.web.fandp;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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
import com.manulife.pension.platform.web.fap.FapForm;
import com.manulife.pension.platform.web.fap.tabs.FapByFundScorecardController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWSessionExpired;

/**
 * Wrapper Action class, to enable XSS validation.
 * 
 * @author narimal
 *
 */
@Controller
@RequestMapping( value ="/fap")
public class PSFapByFundScorecardController extends FapByFundScorecardController {

	@ModelAttribute("fapForm")
	public FapForm populateForm() {
		return new FapForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("sessionExpired", "/WEB-INF/fap/tpaFandpFilterResults.jsp");
		forwards.put("displayTabs", "forward:/do/fap/tabs/?action=displayTabs");
	}

	@RequestMapping(value ="/fapByFundScorecard/", params = { "action=filter" }, method = { RequestMethod.POST })
	public String doFilter(@Valid @ModelAttribute("fapForm") FapForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("sessionExpired");// if input forward not available, provided default
			}
		}
		
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	//TODO dead code is need to be remove once each and every tab flow has been working successfully
	/*@RequestMapping(value ="/fapByFundScorecard/", method = { RequestMethod.POST, RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("fapForm") FapForm form, ModelMap model,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);

			request.getSession().removeAttribute(PsBaseAction.ERROR_KEY);
			return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("sessionExpired");// if input forward not available, provided default
		}

		String forward = super.doDefault(form, model, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/fapByFundScorecard/", params = { "action=sort,task=sort" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("fapForm") FapForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			FapForm fapForm = (FapForm) form;
			fapForm.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);

			request.getSession().removeAttribute(PsBaseAction.ERROR_KEY);
			return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("sessionExpired");// if input forward not available, provided default
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/fapByFundScorecard/", params = { "action=reportsAndDownload",
			"task=reportsAndDownload" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String doReportsAndDownload(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			FapForm fapForm = (FapForm) form;
			fapForm.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);

			request.getSession().removeAttribute(PsBaseAction.ERROR_KEY);
			return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("sessionExpired");// if input forward not available, provided default
		}
		String forward = super.doReportsAndDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/fapByFundScorecard/", params = { "action=changeDropDownList",
			"task=changeDropDownList" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String doChangeDropDownList(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			FapForm fapForm = (FapForm) form;
			fapForm.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);

			request.getSession().removeAttribute(PsBaseAction.ERROR_KEY);
			return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("sessionExpired");// if input forward not available, provided default

		}
		String forward = super.doChangeDropDownList(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/fapByFundScorecard/", params = { "action=storeFapFormInSession",
			"task=storeFapFormInSession" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String doStoreFapFormInSession(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			FapForm fapForm = (FapForm) form;
			fapForm.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);

			request.getSession().removeAttribute(PsBaseAction.ERROR_KEY);
			return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("sessionExpired");// if input forward not available, provided default

		}
		String forward = super.doStoreFapFormInSession(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}*/

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */
	@Autowired
	private PSValidatorFWSessionExpired psValidatorFWSessionExpired;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWSessionExpired);
	}

}
