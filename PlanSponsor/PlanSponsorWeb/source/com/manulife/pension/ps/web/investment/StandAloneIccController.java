package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.HashMap;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping(value = "/iccReport/")
public class StandAloneIccController extends IccReportController {
	
	@ModelAttribute("disclosureForm")
	public RegulatoryDisclosureForm populateForm() {
		return new RegulatoryDisclosureForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("regulatoryDisclosure", "/fee/regulatoryDisclosure.jsp");
	}

	private Logger logger = Logger.getLogger(IccReportController.class);
	boolean isDebugEnabled = logger.isDebugEnabled();
	boolean isErrorEnabled = logger.isEnabledFor(Level.ERROR);

	public StandAloneIccController() {
		super(StandAloneIccController.class);
	}

	@Override
	boolean isDocumentAllowed(Access404a5 access) {
		return access.getAccess(Facility.INVESTMENT_COMPARATIVE_CHART) != null
				&& !access.getAccess(Facility.INVESTMENT_COMPARATIVE_CHART).getTemporarilyMissingInformation()
						.contains(MissingInformation.ICC_CONTACT);
	}

	@Override
	String getPdfSessionCacheAttributeName() {
		return "generatedICCArray";
	}

	@Override
	String getPdfFileName(int contractId) {
		return "ICC_" + contractId + ".pdf";
	}

	@Override
	byte[] getPdfFileStream(int contractId, String topLogoPath, String bottomLogoPath, FeeDisclosureUserDetails context,
			UserAccess user) throws Exception {
		return FundServiceDelegate.getInstance().getIccFileStream(contractId, topLogoPath, bottomLogoPath, context,
				user);
	}

	@RequestMapping(method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("disclosureForm") RegulatoryDisclosureForm actionForm,
			ModelMap model, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("regulatoryDisclosure");
			}
		}
		String forward = super.doDefault(actionForm, model, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(params = { "action=checkPdfReportGenerated" }, method = { RequestMethod.GET })
	public String doCheckPdfReportGenerated(
			@Valid @ModelAttribute("disclosureForm") RegulatoryDisclosureForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("regulatoryDisclosure");
			}
		}
		String forward = super.doCheckPdfReportGenerated(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(params = { "task=openErrorPdf" }, method = { RequestMethod.GET })
	public String doOpenErrorPdf(@Valid @ModelAttribute("disclosureForm") RegulatoryDisclosureForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("regulatoryDisclosure");
			}
		}
		String forward = super.doOpenErrorPdf(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

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
