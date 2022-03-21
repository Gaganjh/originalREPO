
package com.manulife.pension.ps.web.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.HashMap;
import javax.validation.Valid;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.BasePdfAutoController;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.ps.cache.CofidProperties;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.Constants.Cofid338InvestmentOptionProfile;

import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.resources.Cofid338FundReplacementDetails;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.FundReplacementRecommendationVO;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.thoughtworks.xstream.XStream;

/**
 * Form list Action class gets required value objects , forwards to forms.jsp
 * PlanSponsor.
 *
 * @author Chris Shin Jan 2004
 */
@Controller
@RequestMapping(value = "/resources")

public class FormsController extends BasePdfAutoController {
	@ModelAttribute("formsForm")
	public FormsForm populateForm() {
		return new FormsForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/resources/forms.jsp");
		forwards.put("forms","/resources/forms.jsp");
	}

	private static final String XSLT_FILE_KEY_NAME = "Cofid338FundReplacementFormReport.XSLFile";
	private static final String TOP_LOGO_IMAGE_PATH = "/images/";

	public FormsController() {
		super(FormsController.class);
	}

	@RequestMapping(value ="/forms/", method = RequestMethod.GET)
	public String doDefault(@Valid @ModelAttribute("formsForm") FormsForm actionForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}

		UserProfile userProfile = getUserProfile(request);

		// if there're no current contract, forward them to the home page finder
		if (userProfile.getCurrentContract() == null)
			return Constants.HOMEPAGE_FINDER_FORWARD;

		return forwards.get("forms");
	}

	@RequestMapping(value ="/forms/", params ="action=downloadFundReplacementForm", method = {RequestMethod.POST,RequestMethod.GET})
	public String doDownloadFundReplacementForm(@Valid @ModelAttribute("formsForm") FormsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Inside doDownloadFundReplacementForm");
		}

		UserProfile userProfile = getUserProfile(request);

		// if there're no current contract, forward them to the home page finder
		if (userProfile.getCurrentContract() == null)
			return Constants.HOMEPAGE_FINDER_FORWARD;

		ByteArrayOutputStream pdfOutStream = prepareXMLandGeneratePDF(form, null, request);
		String filename = getPdfFilename(form, request);

		response.setHeader("Cache-Control", "must-revalidate,no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		response.setContentLength(pdfOutStream.size());

		try {
			ServletOutputStream sos = response.getOutputStream();
			pdfOutStream.writeTo(sos);
			sos.flush();
		} catch (IOException ioException) {
			throw new SystemException(ioException, "Exception writing pdfData.");
		} finally {
			response.getOutputStream().close();

		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting doDownloadFundReplacementForm");
		}
		return null;
	}

	/**
	 * Returns the user profile associated with the given request.
	 * 
	 * @param request
	 *            The request object.
	 * @return The user profile object associated with the request (or null if
	 *         none is found).
	 */
	public static UserProfile getUserProfile(final HttpServletRequest request) {
		return SessionHelper.getUserProfile(request);
	}

	/**
	 * This method would return the key present in ReportsXSL.properties file.
	 * This key has the value as path to XSLT file, which will be used during
	 * PDF generation.
	 * 
	 * @return String - XSLT file location.
	 */
	public String getXSLTFileName() {
		return XSLT_FILE_KEY_NAME;
	}

	/**
	 * returns file name
	 * 
	 * @param actionForm
	 * @param request
	 * @return file name
	 * @throws SystemException
	 */
	public String getPdfFilename(AutoForm actionForm, HttpServletRequest request) throws SystemException {

		FormsForm form = (FormsForm) actionForm;
		String docketFormNo = form.getDocketFormNo();
		Cofid338InvestmentOptionProfile cofid338InvestmentOptionProfile = Cofid338InvestmentOptionProfile
				.getCofid338InvestmentOptionProfile(form.getFormType());

		return docketFormNo + "_" + cofid338InvestmentOptionProfile.getDescription() + ".pdf";
	}

	/**
	 * This method needs to be overridden by any Report that needs PDF
	 * Generation functionality. This method would generate the XML file.
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @return Object
	 * @throws ParserConfigurationException
	 * @throws SystemException
	 * @throws ContentException
	 */
	public Object prepareXMLFromReport(AutoForm actionForm, ReportData report, HttpServletRequest request)
			throws ParserConfigurationException, SystemException, ContentException {

		UserProfile userProfile = getUserProfile(request);

		Contract contract = userProfile.getCurrentContract();

		FormsForm formsform = (FormsForm) actionForm;

		Cofid338InvestmentOptionProfile cofid338InvestmentOptionProfile = Cofid338InvestmentOptionProfile
				.getCofid338InvestmentOptionProfile(formsform.getFormType());

		Cofid338FundReplacementDetails details = new Cofid338FundReplacementDetails();
		details.setPageTitle(MessageFormat.format(Constants.COFID_338_FUND_SETUP_FORM_TEXT,
				cofid338InvestmentOptionProfile.getDescription()));

		String siteMode = CommonEnvironment.getInstance().getSiteLocation();
		Location location = StringUtils.equals(siteMode, Constants.SITEMODE_USA) ? Location.US : Location.NEW_YORK;

		details.setWilshire338ProfileName(cofid338InvestmentOptionProfile.getDescription());

		details.setReplacementFundText(
				ContentHelper.getContentText(ContentConstants.COFID_338_FUND_SETUP_FORM_REPLACEMENT_FUNDTEXT,
						ContentTypeManager.instance().MISCELLANEOUS, location));

		details.setAuthorizationText(
				ContentHelper.getContentText(ContentConstants.COFID_338_FUND_SETUP_FORM_AUTHORIZATION_TEXT,
						ContentTypeManager.instance().MISCELLANEOUS, location));

		details.setSectionHeaderContent(
				ContentHelper.getContentText(ContentConstants.COFID_338_FUND_SETUP_FORM_SECTION_HEADER_CONTENT_TEXT,
						ContentTypeManager.instance().MISCELLANEOUS, location));

		details.setChangeYourProfileContent(
				ContentHelper.getContentText(ContentConstants.COFID_338_FUND_SETUP_FORM_CHANGE_PROFILE_TEXT,
						ContentTypeManager.instance().MISCELLANEOUS, location));

		details.setDefalutInvestmentOption(
				ContentHelper.getContentText(ContentConstants.COFID_338_FUND_SETUP_FORM_DEFAULT_INVESTMENT_OPTION_TEXT,
						ContentTypeManager.instance().MISCELLANEOUS, location));

		details.setStableValueFundTransferText1(
				ContentHelper.getContentText(ContentConstants.COFID_338_FUND_SETUP_FORM_STABLE_VALUE_TEXT_1,
						ContentTypeManager.instance().MISCELLANEOUS, location));

		details.setStableValueFundTransferText2(
				ContentHelper.getContentText(ContentConstants.COFID_338_FUND_SETUP_FORM_STABLE_VALUE_TEXT_2,
						ContentTypeManager.instance().MISCELLANEOUS, location));

		details.setStableValueFundTransferText3(
				ContentHelper.getContentText(ContentConstants.COFID_338_FUND_SETUP_FORM_STABLE_VALUE_TEXT_3,
						ContentTypeManager.instance().MISCELLANEOUS, location));

		details.setPaymentofWilshire(
				ContentHelper.getContentText(ContentConstants.COFID_338_FUND_SETUP_FORM_PAYMENT_OF_WILSHIRE_TEXT,
						ContentTypeManager.instance().MISCELLANEOUS, location));

		details.setAdditionalTermsAndConditions(ContentHelper.getContentText(
				ContentConstants.COFID_338_FUND_SETUP_FORM_ADDITONAL_TERMS_AND_CONDITIONS_TEXT,
				ContentTypeManager.instance().MISCELLANEOUS, location));

		String monthAndYear = CofidProperties.getInstance().getProperty(CofidProperties.Form_MonthYear);

		details.setFormAndDocket(formsform.getDocketFormNo().concat(" ").concat(monthAndYear));

		if (userProfile.getCurrentContract().getCompanyCode().equals(GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY)) {

			details.setSectionHeading(CofidProperties.getInstance().getProperty(CofidProperties.NY_SubSectionHeading));
		}

		details.setGeneralDisclosure(
				ContentHelper.getContentText(ContentConstants.COFID_338_FUND_SETUP_FORM_DISCLOSURE_TEXT,
						ContentTypeManager.instance().MISCELLANEOUS, location));

		details.setFooterText(ContentHelper.getContentText(ContentConstants.COFID_338_FUND_SETUP_FORM_FOOTER_TEXT,
				ContentTypeManager.instance().MISCELLANEOUS, location));

		details.setCompanyId(contract.getCompanyCode());
		details.setContractName(contract.getCompanyName());
		details.setContractNumber(String.valueOf(contract.getContractNumber()));
		details.setImagePath(getClass().getClassLoader().getResource(TOP_LOGO_IMAGE_PATH).getPath());

		List<FundReplacementRecommendationVO> recommendations = ContractServiceDelegate.getInstance()
				.getContractFundReplacementRecommendations(contract.getContractNumber(),
						cofid338InvestmentOptionProfile.getDBCode(), contract.getCompanyCode());
		details.getRecommendations().addAll(recommendations);

		return createXML(details);
	}

	@RequestMapping(value ="/forms/", params ="task=printPDF", method = RequestMethod.GET)
	public String doPrintPDF(@Valid @ModelAttribute("formsForm") FormsForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doPrintPDF(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * Method to convert the VO to XML
	 * 
	 * @param object
	 * @return
	 */
	public static String createXML(Cofid338FundReplacementDetails vo) {
		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		String xml;

		xstream.alias("cofid_review_report", Cofid338FundReplacementDetails.class);
		xstream.alias("cofid_recommendation", FundReplacementRecommendationVO.class);

		xml = xstream.toXML(vo);

		return xml;
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}

}
