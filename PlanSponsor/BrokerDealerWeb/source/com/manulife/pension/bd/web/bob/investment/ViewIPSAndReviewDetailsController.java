package com.manulife.pension.bd.web.bob.investment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.IPSRServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.investment.AbstractIPSAndReviewDetailsController;
import com.manulife.pension.platform.web.investment.IPSAndReviewDetailsDataValidator;
import com.manulife.pension.platform.web.investment.IPSManagerUtility;
import com.manulife.pension.platform.web.investment.valueobject.IPSReviewDetailsVO;
import com.manulife.pension.platform.web.investment.valueobject.IPSReviewReportDetailsVO;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.service.contract.util.Constants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.InvestmentPolicyStatementVO;
import com.manulife.pension.service.ipsr.valueobject.IPSRReviewRequest;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.pdf.PdfConstants;
import com.manulife.util.piechart.PieChartBean;
import com.manulife.util.piechart.PieChartUtil;
import com.manulife.util.render.DateRender;
import com.thoughtworks.xstream.XStream;

/**
 * Action class for IPS and Review details page
 * 
 * @author Turaga Divya
 * 
 */


@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"ipsAndReviewDetailsForm"})

public class ViewIPSAndReviewDetailsController extends
		AbstractIPSAndReviewDetailsController {
	@ModelAttribute("ipsAndReviewDetailsForm") 
	public IPSAndReviewForm populateForm() 
	{
		return new IPSAndReviewForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/investment/ipsAndReviewDetails.jsp");
		forwards.put("default","/investment/ipsAndReviewDetails.jsp");
		forwards.put("homePage","redirect:/do/home/");
		forwards.put("editIPSAndDetails","redirect:/do/bob/investment/editIPSManager/");
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.platform.web.controller.BaseAutoAction#
	 * doDefault(org.apache.struts.action.ActionMapping, 
	 * com.manulife.pension.platform.web.controller.AutoForm, 
	 * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	
		@RequestMapping(value ="/investment/ipsManager/", method = {RequestMethod.GET})
		public String doDefault(@Valid @ModelAttribute("ipsAndReviewDetailsForm") IPSAndReviewForm actionForm,
				BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException, SystemException {
			if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		       request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		       return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		       }
			}
		BobContext bob = BDSessionHelper.getBobContext(request) ;
		BDUserProfile bdUserProfile = bob.getUserProfile();
		
		Contract contract = getBobContext(request).getCurrentContract();
		int contractId = new Integer(contract.getContractNumber());
		String contractStatus = contract.getStatus();
		IPSAndReviewForm ipsAssistServiceForm = (IPSAndReviewForm) actionForm;
		ContractServiceDelegate delegate = ContractServiceDelegate
				.getInstance();

		InvestmentPolicyStatementVO investmentPolicyStatement = delegate.getIpsBaseData(contractId);
		
		if (investmentPolicyStatement == null) {
			return forwards.get(FRW_HOME_ACTION);
		}
		// Get the IPS criteria and description from database
		Map<String, String> ipsCriteriaDescMap = delegate.getIpsFundMetrics();

		ipsAssistServiceForm.setMode(VIEW_MODE);
		populateFormBean(contractId, ipsAssistServiceForm, ipsCriteriaDescMap);

		// Create a pieChartBean with the weightings percentage and set it to
		// the request
		PieChartBean pieChart = createPieChartBean(ipsAssistServiceForm
				.getCriteriaAndWeighting(), CommonConstants.ipsColorCode);
		request.setAttribute(CommonConstants.IPSR_CRITERIA_WEIGHTING_PIECHART,
				pieChart);
		
		// Set the IPS criteria history in request to render it while doing
		// hover over
		InvestmentPolicyStatementVO investmentPolicyStatementVO = delegate
				.getIPSCriteria(contractId);

		// Set the first name and last name of the last changed user to the vo
		
		UserInfo userInfo = null;
		if (investmentPolicyStatementVO.getCreatedUserId() != null) {
			userInfo = SecurityServiceDelegate.getInstance()
					.getUserProfileByProfileId(
							Long.parseLong(investmentPolicyStatementVO
									.getCreatedUserId()));
		}
		
		if (userInfo != null) {
			investmentPolicyStatementVO.setCreatedUserFirstName(userInfo
					.getFirstName());
			investmentPolicyStatementVO.setCreatedUserLastName(userInfo
					.getLastName());
		}
		request.setAttribute(CHANGE_HISTORY, investmentPolicyStatementVO);
		
		boolean isEditLinkAccesible = false;

		if (!bdUserProfile.isInternalUser()) {
			isEditLinkAccesible = true;
		}
		
		ipsAssistServiceForm.setEditLinkAccessible(isEditLinkAccesible);
		
		// Check RIA designated.
		boolean riaDesignationInd = SecurityServiceDelegate.getInstance().hasRia338DesignationForUser(contractId, BigDecimal.valueOf(bdUserProfile.getBDPrincipal().getProfileId()));
		
		ipsAssistServiceForm.setIs338DesignateRia(riaDesignationInd);
		//ipsAssistServiceForm.setIs338DesignateRia(true);
		
		// Get the IPS Review Requests for Contract
		List<IPSRReviewRequest> ipsReviewRequestList = getIPSReviewRequests(contractId);

		// Check whether there is access to the edit criteria and weightings page
		if (ipsReviewRequestList != null && !ipsReviewRequestList.isEmpty()) {
			if(!IPSAndReviewDetailsDataValidator.isInvalidcontract(contractStatus)){
				if (IPSManagerUtility
						.isEditIPSManagerNotAvailable(ipsReviewRequestList)) {
					try {
						Content message = ContentCacheManager
								.getInstance()
								.getContentById(
										BDContentConstants.MESSAGE_IPS_REVIEW_INPROCESS_WHEN_CHANGING_SERVICE_DATE,
										ContentTypeManager.instance().MISCELLANEOUS);
	
						String contents = ContentUtility.getContentAttribute(
								message, "text");
	
						request.setAttribute(
										BDConstants.IPS_SERVICE_DATE_CHANGE_NOT_AVAILABLE_TEXT,
										StringUtils.trim(contents));
					} catch (ContentException exp) {
						throw new SystemException(exp, "Something wrong with CMA");
					}		
				}
			}
		}
		// Set the form for IPS Review Report Details
		populateIPSReviewDetailsToForm(ipsReviewRequestList,
				ipsAssistServiceForm, contractId, isEditLinkAccesible,
				contractStatus, contract.getCompanyName());
		
		if (ipsAssistServiceForm.isSaveSuccess()) {
			request.setAttribute(SUCCESS_IND, true);
			ipsAssistServiceForm.setSaveSuccess(false);
			ipsAssistServiceForm.setDateChanged(false);
		}
		
		return forwards.get(DEFAULT_ACTION);
	}

	/**
	 * Returns the BOB Context associated with the given request.
	 * 
	 * @param request
	 *            The request object.
	 * @return The BOBContext object associated with the request (or null if
	 *         none is found).
	 */
	public static BobContext getBobContext(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return session == null ? null : (BobContext) session
				.getAttribute(BDConstants.BOBCONTEXT_KEY);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.manulife.pension.platform.web.investment.
	 * AbstractIPSAndReviewDetailsAction
	 * #prepareXMLFromReport(com.manulife.pension
	 * .platform.web.controller.AutoForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected String prepareXMLFromReport(
			AutoForm actionForm,
			HttpServletRequest request) throws SystemException {
		IPSAndReviewForm ipsAssistServiceForm = (IPSAndReviewForm) actionForm;
		
		IPSReviewDetailsVO ipsReviewDetailsVO = new IPSReviewDetailsVO();
		ipsReviewDetailsVO = buildIPSreviewDetailsVO(ipsReviewDetailsVO,
				ipsAssistServiceForm, request);
		String xmlTree = createXMLFromVO(ipsReviewDetailsVO);
		xmlTree = xmlTree.replaceAll("&lt;br/&gt;", "<br/>");
		xmlTree = xmlTree.replaceAll("&lt;strong&gt;", "<strong>");
		xmlTree = xmlTree.replaceAll("&lt;/strong&gt;", "</strong>");
		
		return xmlTree;
	}

	/**
	 * This method will build IPSReviewDetailsVO object which is used as a
	 * parameter to create XML String
	 * 
	 * @param ipsReviewDetailsVO
	 * @param form
	 * @param request
	 * @return IPSReviewDetailsVO
	 */
	private IPSReviewDetailsVO buildIPSreviewDetailsVO(
			IPSReviewDetailsVO ipsReviewDetailsVO,
			IPSAndReviewForm form, HttpServletRequest request) {

		Contract contract = getBobContext(request).getCurrentContract();
		Date asOfDate = contract.getContractDates().getAsOfDate();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String asofDate = formatter.format(asOfDate);
		String contractName = contract.getCompanyName();
		int contractId = contract.getContractNumber();
		ipsReviewDetailsVO.setContractName(contractName);
		ipsReviewDetailsVO.setContractNumber(contractId);
		String contentText;

		// Get layout page for ipsAndReviewDetails.jsp
		LayoutPage layoutPageBean = getLayoutPage(
				BDPdfConstants.IPS_MANAGER_PATH, request);

		StringBuffer logoPath = new StringBuffer();
		// Logo will be the same for both US and NY because, they both see the
		// same page with the same logo.
		logoPath.append(PdfHelper.class
				.getResource(CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX
						+ CommonConstants.JHRPS_LOGO_FILE));
		ipsReviewDetailsVO.setJhLogoPath(logoPath.toString());

		// setting page name
		String pageName = ContentUtility.getContentAttributeText(
				layoutPageBean, CommonContentConstants.PAGE_NAME, null);
		ipsReviewDetailsVO.setPageName(pageName);

		// setting into1 and intro2 elements
		String intro1Text = ContentUtility.getContentAttributeText(
				layoutPageBean, CommonContentConstants.INTRO1_TEXT, null);
		String intro2Text = ContentUtility.getContentAttributeText(
				layoutPageBean, CommonContentConstants.INTRO2_TEXT, null);
		ipsReviewDetailsVO.setIntro1Text(intro1Text);
		ipsReviewDetailsVO.setIntro2Text(intro2Text);

		// setting asOfDate
		ipsReviewDetailsVO.setAsOfDate(asofDate);

		// setting Body Header1
		String subHeader = ContentUtility.getContentAttributeText(
				layoutPageBean, BDContentConstants.SUB_HEADER, null);
		ipsReviewDetailsVO.setBodyHeader1(subHeader);

		// setting IPS Manager Setup Details Section Title
		contentText = ContentUtility.getContentAttributeText(
				layoutPageBean, CommonContentConstants.BODY1_HEADER, null);
		ipsReviewDetailsVO.setIpsManagerDetailsSectionTitle(contentText);

		// setting IPS criteria And Weightings Section Title
		contentText = ContentUtility.getContentAttributeText(
				layoutPageBean, CommonContentConstants.BODY2_HEADER, null);
		ipsReviewDetailsVO.setIpsCriteriaAndWeightingsSectionTitle(contentText);

		// Setting IPS Service Text
		contentText = ContentHelper.getContentText(
				BDContentConstants.IPS_ASSIST_SERVICE_TEXT, ContentTypeManager
						.instance().MESSAGE, null);
		ipsReviewDetailsVO.setIpsAssistServiceText(contentText);

		// set true if IPS Service Available
		ipsReviewDetailsVO.setIPSServiceAvailable(form
				.isiPSAssistServiceAvailable());

		// Setting IPS Schedule Annual Review Date Text
		contentText = ContentHelper.getContentText(
				BDContentConstants.IPS_SCHEDULE_ANNUAL_REVIEW_DATE,
				ContentTypeManager.instance().MESSAGE, null);
		ipsReviewDetailsVO.setIpsScheduleAnnualReviewDateText(contentText);

		// Setting IPS Service Review Comments
		contentText = ContentHelper.getContentText(
				BDContentConstants.IPS_SERVICE_REVIEW_DATE_TEXT,
				ContentTypeManager.instance().MESSAGE, null);
		ipsReviewDetailsVO.setIpsServiceReviewDateText(contentText);

		// setting IPS Annual Review Date
		ipsReviewDetailsVO.setIpsAnnualReviewDate(form.getAnnualReviewDate());

		// setting IPS criteria And Weightings List
		if (form.isiPSAssistServiceAvailable()) {
			ipsReviewDetailsVO.setIpsCriteriaAndWeightingList(form
					.getCriteriaAndWeightingPresentationList());
		}
		
		String portNumber = System.getProperty("webcontainer.http.port") == null ? "9081" : System.getProperty("webcontainer.http.port");
        String baseURI = "http://localhost:" + portNumber;

        Map<String, Integer> ipsCriteriaMap = form.getCriteriaAndWeighting();
        
        // If IPS Service is not available this check will be fail.
        if(ipsCriteriaMap != null) {
        	Set<String> keySet = ipsCriteriaMap.keySet();
        	
        	if(keySet != null && !keySet.isEmpty()) {
        		PieChartBean pieChartBean = createPieChartBean(form
        				.getCriteriaAndWeighting(), CommonConstants.ipsColorCode);
        		String fileName = PieChartUtil.createURLStringFOP(pieChartBean);

        		String pieChartURL = baseURI
        				+ fileName.replaceAll(PdfConstants.AMPERSAND,
        						Constants.AMPERSAND_SYMBOL);
        		// setting PieChart URL.
        		ipsReviewDetailsVO.setPieChartURL(pieChartURL);
        	}
        }
		

		// Setting IPS Schedule Annual Review Date Text
		contentText = ContentHelper.getContentText(
				BDContentConstants.IPS_REVIEW_REPORTS_SECTION_TITLE,
				ContentTypeManager.instance().MESSAGE, null);
		ipsReviewDetailsVO.setIpsReviewReportSectionTitle(contentText);
		
		List<IPSReviewReportDetailsVO> ipsReviewReportDetailsList = form.getIpsReviewReportDetailsList();
		// setting IPS Review Details List
		if (ipsReviewReportDetailsList != null
				&& !ipsReviewReportDetailsList.isEmpty()) {
			for(IPSReviewReportDetailsVO ipsReviewReportDetailsVO : ipsReviewReportDetailsList) {
				if(ipsReviewReportDetailsVO != null) {
					if(ipsReviewReportDetailsVO.getAnnualReviewDate() != null) {
						contentText = ContentHelper.getContentTextWithParamsSubstitution(
								BDContentConstants.IPS_CURRENT_REPORT_LABEL_LINK,
								ContentTypeManager.instance().MESSAGE, null, ipsReviewReportDetailsVO.getAnnualReviewDate());
						ipsReviewReportDetailsVO.setViewResultsDisplay(contentText);
					}
				}
			}
			ipsReviewDetailsVO.setIpsReviewReportDetailsList(form
					.getIpsReviewReportDetailsList());
		} else {
			boolean ipsReviewReportDetailsNotAvailable = true;
			ipsReviewDetailsVO
					.setIpsReviewReportDetailsNotAvailable(ipsReviewReportDetailsNotAvailable);
			contentText = ContentHelper.getContentText(
					BDContentConstants.IPS_NO_CURRENT_OR_PREVIOUS_REPORT,
					ContentTypeManager.instance().MESSAGE, null);
			ipsReviewDetailsVO.setIpsNoCurrentOrPreviousReport(contentText);
		}
		
		// Get the footer.
		String footer = ContentUtility.getPageFooter(layoutPageBean, null);
		// setting footer
		if (!StringUtils.isBlank(footer)) {
			footer = ContentUtility.filterCMAContentForPDF(footer);
			ipsReviewDetailsVO.setFooter(footer);
		}
		
		// Get the disclaimer.
		String disclaimer = ContentUtility.getPageDisclaimer(layoutPageBean,
				null, -1);
		// setting disclaimer
		if (!StringUtils.isBlank(disclaimer)) {
			disclaimer = ContentUtility.filterCMAContentForPDF(disclaimer);
			ipsReviewDetailsVO.setDisclaimer(disclaimer);
		}
		// get the Global Disclosure
		contentText = ContentHelper.getContentText(
				BDContentConstants.BD_GLOBAL_DISCLOSURE, ContentTypeManager
						.instance().MESSAGE, null);

		if (!StringUtils.isBlank(contentText)) {
			contentText = ContentUtility.filterCMAContentForPDF(contentText);
			ipsReviewDetailsVO.setGlobalDisclosure(contentText);
		}
		// Get the standard footer text.
		String standardFooter = ContentUtility.getContentAttributeText(
				layoutPageBean, CommonContentConstants.TEXT, null);
		// setting Standard Footer
		if (!StringUtils.isBlank(standardFooter)) {
			standardFooter = ContentUtility.filterCMAContentForPDF(standardFooter);
			ipsReviewDetailsVO.setStandardFooter(standardFooter);
		}
		// setting total criteria and weighting percentage
		ipsReviewDetailsVO.setTotalweighting(form.getTotalWeighting());
		String lastModifiedDate =StringUtils.EMPTY;
		if(form.getLastModifiedOn() != null) {
			lastModifiedDate = formatter.format(form.getLastModifiedOn());
		}
		 
		ipsReviewDetailsVO.setLastModifiedDate(lastModifiedDate);
		
		String fundInfoIconPath = PdfHelper.class.getResource(
				CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX
						+ CommonConstants.INFO_ICON_FILE).toString();
		ipsReviewDetailsVO.setFundInfoIconPath(fundInfoIconPath);
		
		//setting IPS service deactivated text
		contentText = ContentHelper.getContentText(
				BDContentConstants.IPS_ASSIST_SERVICE_TEXT_DEACTIVATED,
				ContentTypeManager.instance().MESSAGE, null);
		ipsReviewDetailsVO.setIpsServiceDeactivated(contentText);
		
		return ipsReviewDetailsVO;
	}

	/**
	 * This method is used to get XSL file name for PDF generation
	 * 
	 * @return String XSLT file name
	 */
	protected String getXSLTFileName() {
		return BDPdfConstants.XSL_FILE_NAME;
	}

	/**
	 * This method gets layout page for the given layout id.
	 * 
	 * @param id
	 * @param request
	 * @return LayoutPage
	 */
	protected LayoutPage getLayoutPage(String id, HttpServletRequest request) {
		BDLayoutBean bean = ApplicationHelper.getLayoutStore(
				request.getServletContext()).getLayoutBean(id, request);
		LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();
		return layoutPageBean;
	}
	
	/**
	 * Method used to convert VO to XML
	 * 
	 * @param ipsReviewDetailsVO
	 * @return String strXml
	 */
	public static String createXMLFromVO(IPSReviewDetailsVO ipsReviewDetailsVO) {

		XStream xStream = getIPSReviewReportAlias(ipsReviewDetailsVO);
		String strXml = xStream.toXML(ipsReviewDetailsVO);

		return strXml;
	}

	/**
	 * Set the alias for XSL tags
	 * 
	 * @param ipsReviewDetailsVO
	 * @return XStream xStream
	 */
	private static XStream getIPSReviewReportAlias(
			IPSReviewDetailsVO ipsReviewDetailsVO) {
		XStream xStream = new XStream();
		if (ipsReviewDetailsVO != null) {
			xStream
					.alias(
							"ips_review_report",
							com.manulife.pension.platform.web.investment.valueobject.IPSReviewDetailsVO.class);
			xStream
					.alias(
							"criteria_weighting_info",
							com.manulife.pension.platform.web.investment.valueobject.CriteriaAndWeightingPresentation.class);
			xStream
					.alias(
							"ips_review_report_details",
							com.manulife.pension.platform.web.investment.valueobject.IPSReviewReportDetailsVO.class);
		}

		return xStream;
	}
	
	/**
	 * This method is used to generate Annual Review Report
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws SystemException
	 * @throws IOException 
	 */
	@RequestMapping(value ="/investment/ipsManager/",params= {"action=generateReviewReport"}, method = {RequestMethod.GET})
	public String doGenerateReviewReport(@Valid @ModelAttribute("ipsAndReviewDetailsForm") IPSAndReviewForm actionForm,ModelMap model,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	
		
		if (logger.isDebugEnabled()) {
			logger.debug("Inside doGenerateReviewReport");
		}
		
		Contract contract = getBobContext(request).getCurrentContract();
		
		Collection<GenericException> errors = new ArrayList<GenericException>();
		
		populateReviewReport(request, contract, response, errors);
		
		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			String forwrad=doDefault(actionForm, bindingResult, request, response);
			return forwards.get(forwrad)!=null?forwards.get(forwrad):forwards.get("input");
			//return doDefault( actionForm,model, request, response);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting doGenerateReviewReport");
		}
		return null;
	}
	
	/**
	 * This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}

	//RIA Fee Calculation
	
	/**
	 * Validates the contract status and forwards to the edit action
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	  @RequestMapping(value ="/investment/ipsManager/",params= {"action=edit"}, method = {RequestMethod.GET})
		public String doEdit(@Valid @ModelAttribute("ipsAndReviewDetailsForm") IPSAndReviewForm actionForm,ModelMap model,
				BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException, SystemException {
			if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		       }
			}
	  
		
		Contract contract = getBobContext(request).getCurrentContract();
		String contractStatus = contract.getStatus();
		List<GenericException> errors = new ArrayList<GenericException>();

		if (IPSAndReviewDetailsDataValidator.isInvalidcontract(contractStatus)) {
			IPSAndReviewDetailsDataValidator.addError(errors, IPSAndReviewDetailsDataValidator.NO_ACCESS_ERROR);
			BDSessionHelper.setErrorsInSession(request, errors);
			return doDefault( actionForm,model, request, response);
		}
		return forwards.get(EDIT_IPS_AND_DETAILS_PAGE);
	}
	
	/**
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws SystemException
	 */
	  @RequestMapping(value ="/investment/ipsManager/",params= {"action=printInterimReport"}, method = {RequestMethod.GET})
		public String doPrintInterimReport(@Valid @ModelAttribute("ipsAndReviewDetailsForm") IPSAndReviewForm actionForm,
				BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException, SystemException {
			if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		       }
			}
	  
		
		if (logger.isDebugEnabled()) {
			logger.debug("Inside doPrintInterimReport");
		}
		BobContext bob = BDSessionHelper.getBobContext(request) ;
		int contractId = bob.getCurrentContract().getContractNumber();
		
		IPSRServiceDelegate delegate = IPSRServiceDelegate.getInstance();
		
		// Creates Interim Report
		byte[] interimReport = delegate.generateInterimRport(contractId);
		
		ByteArrayOutputStream pdfOutStream=new ByteArrayOutputStream();
		
		try {
			
			pdfOutStream.write(interimReport);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setContentType("application/pdf");
			String filename = "IPSM-Interim-"
					+ DateRender.formatByPattern(new Date(),
							Constants.EMPTY_STRING,
							CommonConstants.MEDIUM_MDY_DASHED) + "-"
					+ bob.getCurrentContract().getCompanyName().trim()
					+ ".pdf";
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ filename + "\"");
			response.setContentLength(pdfOutStream.size());
			ServletOutputStream sos = response.getOutputStream();
			pdfOutStream.writeTo(sos);
			sos.flush();
			
		} catch (IOException ioException) {
			throw new SystemException(ioException, "Exception writing pdfData.");
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException ioException) {
				throw new SystemException(ioException,
						"Exception writing pdfData.");
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting doPrintInterimReport");
		}
		return null;
	}
	  @RequestMapping(value ="/investment/ipsManager/",params= {"action=printPDF"}, method = {RequestMethod.GET})
		public String doPrintPDF(@Valid @ModelAttribute("ipsAndReviewDetailsForm") IPSAndReviewForm form,
				BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException, SystemException {
			if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		       }
			}
			String forward=super.doPrintPDF( form, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
	
}
