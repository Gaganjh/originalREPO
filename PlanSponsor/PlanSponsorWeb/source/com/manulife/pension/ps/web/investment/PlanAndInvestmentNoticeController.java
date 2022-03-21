package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.fee.RegulatoryDisclosureForm;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWRegulatoryDisclosure;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.fund.valueobject.Access404a5.MissingInformation;
import com.manulife.pension.service.fund.valueobject.FeeDisclosureUserDetails;
import com.manulife.pension.service.fund.valueobject.UserAccess;


@Controller
@RequestMapping(value="/planAndInvestmentNotice/")
public class PlanAndInvestmentNoticeController extends IccReportController {
	
	@ModelAttribute("disclosureForm")
	public RegulatoryDisclosureForm populateForm() {
		return new RegulatoryDisclosureForm();
	}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static {
		forwards.put("regulatoryDisclosure", "/fee/regulatoryDisclosure.jsp");
		forwards.put("secondaryWindowError", "/WEB-INF/global/secondaryWindowError.jsp");
	}

	public PlanAndInvestmentNoticeController() {
		super(PlanAndInvestmentNoticeController.class);
	}

	@Override
	boolean isDocumentAllowed(Access404a5 access) {
		return access.getAccess(Facility._404A5_PLAN_AND_INVESTMENT_NOTICE) != null
				&& !access.getAccess(Facility._404A5_PLAN_AND_INVESTMENT_NOTICE).getTemporarilyMissingInformation()
						.contains(MissingInformation.ICC_CONTACT);
	}
    
	@Override
	String getPdfSessionCacheAttributeName() {
		return "generatedPlanAndInvestmentNoticeArray";
	}

	@Override
	String getPdfFileName(int contractId) {
		return "PN_" + contractId + ".pdf";
	}

	@Override
	byte[] getPdfFileStream(int contractId, String topLogoPath, String bottomLogoPath, FeeDisclosureUserDetails context,
			UserAccess user) throws Exception {
		return FundServiceDelegate.getInstance().getPlanAndInvestmentNoticeFileStream(contractId, topLogoPath,
				bottomLogoPath, context, user);
	}
    
	@RequestMapping(method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("disclosureForm") RegulatoryDisclosureForm actionform,
			ModelMap model, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("regulatoryDisclosure");
			}
		}
		String forward = super.doDefault(actionform, model, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(params = { "action=checkPdfReportGenerated" }, method = { RequestMethod.GET,RequestMethod.POST })
	public String doCheckPdfReportGenerated(
			@Valid @ModelAttribute("disclosureForm") RegulatoryDisclosureForm actionform,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("regulatoryDisclosure");
			}
		}
		String forward = super.doCheckPdfReportGenerated(actionform, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(params = { "action=openErrorPdf" }, method = { RequestMethod.GET })
	public String doOpenErrorPdf(@Valid @ModelAttribute("disclosureForm") RegulatoryDisclosureForm actionform,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("regulatoryDisclosure");
			}
		}
		String forward = super.doOpenErrorPdf(actionform, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
   
	/* @RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public String doExecute(@Valid @ModelAttribute("disclosureForm") RegulatoryDisclosureForm actionform,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(PsBaseAction.ERROR_KEY);
				forwards.get("regulatoryDisclosure");// if input forward not
														// //available, provided
														// default
			}
		}
		String forward = super.doExecute(actionform, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	} */
	
	/**
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations.
	 */

	@Autowired
	private PSValidatorFWRegulatoryDisclosure psValidatorFWRegulatoryDisclosure;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWRegulatoryDisclosure);
	}
}
