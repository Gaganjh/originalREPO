package com.manulife.pension.bd.web.fap;

import java.io.IOException;

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

import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWSessionExpired;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.fap.FapForm;
import com.manulife.pension.platform.web.fap.tabs.FapByPerformanceAndFeesController;

/**
 * Wrapper Action class, to enable XSS validation.
 * 
 * @author narimal
 *
 */
@Controller
@RequestMapping(value = "/fap")
public class BDFapByPerformanceAndFeesController extends FapByPerformanceAndFeesController {
	
	@ModelAttribute("fapForm")
	public FapForm populateForm() {
		return new FapForm();
	}

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */

	@Autowired
	private BDValidatorFWSessionExpired bdValidatorFWSessionExpired;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWSessionExpired);
	}

	@RequestMapping(value = "/fapByPerformanceAndFees", params = { "action=filter" }, method = { RequestMethod.POST })
	public String doFilter(@Valid @ModelAttribute("fapForm") FapForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("sessionExpired");// if input forward not
												// //available, provided default
			}
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/fapByPerformanceAndFees", params = {
			"action=filterPerformanceAndFeesMonthly" }, method = { RequestMethod.POST })
	public String doFilterPerformanceAndFeesMonthly(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("sessionExpired");// if input forward not
												// //available, provided default
			}
		}
		String forward = super.doFilterPerformanceAndFeesMonthly(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/fapByPerformanceAndFees", params = {
			"action=filterPerformanceAndFeesQuarterly" }, method = { RequestMethod.POST })
	public String doFilterPerformanceAndFeesQuarterly(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("sessionExpired");// if input forward not
												// //available, provided default
			}
		}
		String forward = super.doFilterPerformanceAndFeesQuarterly(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

}