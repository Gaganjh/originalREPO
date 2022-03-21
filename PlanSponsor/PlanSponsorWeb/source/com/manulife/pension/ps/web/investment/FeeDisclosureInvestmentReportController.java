package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.event.FeeDisclosureViewedReportEvent;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.investment.valueobject.InvestmentCostReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.fee.util.FeeDisclosureUtility;
import com.manulife.pension.ps.web.investment.util.Constants.Location;
import com.manulife.pension.ps.web.investment.util.FeeCmaDataImpl;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWNull;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractDetailsOtherVO;
import com.manulife.pension.service.fee.util.estimatedcosts.EstimatedRecordKeepingCostComponentGenerator;
import com.manulife.pension.service.fee.util.estimatedcosts.FeeDataContextInterface.EstimatedJhRecordKeepingCostSummary;
import com.manulife.pension.service.fee.util.estimatedcosts.FeeDataContextInterface.FeeCmaData;
import com.manulife.pension.service.fee.util.estimatedcosts.FeeDataContextInterface.FootNote;
import com.manulife.pension.service.fee.valueobject.ContractClass;
import com.manulife.pension.service.fee.valueobject.FundFeeVO;
import com.manulife.pension.service.fee.valueobject.InvestmentGroup;
import com.manulife.pension.service.order.delegate.OrderServiceDelegate;
import com.manulife.pension.service.order.domain.OrderServiceConstants.FeeDisclosurePdfTypes;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.ps.web.pagelayout.LayoutBean;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import java.io.ByteArrayOutputStream;
import com.manulife.pension.platform.web.util.ReportsXSLProperties;
import com.manulife.pension.platform.web.util.PDFGenerator;
import com.manulife.pension.content.exception.ContentException;
import org.apache.fop.apps.FOPException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.content.valueobject.LayoutPage;
import org.w3c.dom.Element;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.ps.web.content.ContentConstants;
import org.apache.commons.lang.StringEscapeUtils;
import com.manulife.pension.service.fee.util.estimatedcosts.FeeDataContextInterface.FeeDetail;
import com.manulife.pension.service.fee.util.estimatedcosts.FeeDataContextInterface.DollarBasedFeeDetail;
import com.manulife.util.render.NumberRender;
import java.util.regex.Pattern;
import com.manulife.pension.util.content.Footnotes;
import com.manulife.pension.content.view.MutableFootnote;
import java.util.Arrays;
import org.apache.commons.collections.map.LinkedMap;
import java.util.Iterator;
import com.manulife.pension.content.valueobject.Footnote;
import java.util.Set;
import java.util.LinkedHashMap;
import java.io.StringWriter;
import java.math.BigDecimal;

import javax.xml.transform.TransformerFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import org.w3c.dom.Document;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;




/**
 * action class for investment related costs page
 * 
 * @author  Siby Thomas
 *
 */

@Controller
@RequestMapping( value = "/investments")
@SessionAttributes({"investmentSelectionReportForm"})

public class FeeDisclosureInvestmentReportController extends ReportController {
	
	@ModelAttribute("investmentSelectionReportForm")
	public InvestmentSelectionReportForm populateForm()
	{
		return new InvestmentSelectionReportForm();
		}
	public static Map<String,String> forwards = new HashMap<>();
	public static final String FEE_DISCLOSURE_INVESTMENT_REPORT = "/investment/feeDisclosureInvestmentReport.jsp";
	static{
		forwards.put("input",FEE_DISCLOSURE_INVESTMENT_REPORT);
		forwards.put("default",FEE_DISCLOSURE_INVESTMENT_REPORT);
		forwards.put("print",FEE_DISCLOSURE_INVESTMENT_REPORT);
		forwards.put("filter",FEE_DISCLOSURE_INVESTMENT_REPORT);
	}
	
	private static final String FEE_INVESTMENT_PAGE_DATE_LIMIT = "FEE_INVESTMENT_PAGE_DATE_LIMIT";
	private static final String XSLT_FILE_KEY_NAME = "feeDisclosures.XSLFile";
	private static final String UTF8 = "utf-8";
	private static final String DEFAULT_VALUE = "0";
	private static final String PATTERN = "0.00";
	private static final int SCALE = 2;
	private static final int ROUNDING_MODE = 4;
	private static final int DIGITS = 1;
	private static final boolean SIGN = true;
	private static final Pattern PATTERN_ANCHOR_REPLACE = Pattern.compile("(<a[^>]*>|</a>|<nobr[^>]*>|</nobr>)");
	
	
	public FeeDisclosureInvestmentReportController(){
		super(FeeDisclosureInvestmentReportController.class);
	}
	
	/**
	 * This is an overridden method. This method handles the book marking
	 * scenarios. This will check if contract is not in pilot it will redirect to HOME page
	 * 
	 * @param form
	 *            Form
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws ServletException
	 *             , IOException, SystemException
	 * @return ActionForward
	 * 
	 */
	
	public String preExecute(InvestmentSelectionReportForm actionForm, HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> preExecute");
		}
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		FeeServiceDelegate feeService = FeeServiceDelegate.getInstance(Environment.getInstance().getAppId());	
		Date invesmentFeePageDateLowerLimit = getInvestmentPageLowerLimitDate();
		boolean isGovernmentPlan;
		try {
			isGovernmentPlan = FeeDisclosureUtility.isGovernmentPlan(getUserProfile(request).getCurrentContract().getContractNumber());
		} catch (ContractDoesNotExistException e) {
			logger.error(e);
			return Constants.Home_URL_REDIRECT;
		}
		if (currentContract.isMta()
				|| isGovernmentPlan
				|| !feeService.isInvestmentRelatedCostsPageAvailable(getUserProfile(request).getCurrentContract().getContractNumber(), 
						invesmentFeePageDateLowerLimit)) {
			return Constants.Home_URL_REDIRECT;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> preExecute");
		}
		return super.preExecute( actionForm, request, response);
	}
	
	@RequestMapping(value ="/investmentRelatedCosts/",  method =  {RequestMethod.GET, RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("investmentSelectionReportForm") InvestmentSelectionReportForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
		}
		forward = super.doDefault(actionForm, request, response);
		postExecute(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	
	/**
	 * This method sets the report criteria and gets the report data required for investment 
	 * related cost page.
	 * @param form
	 *            Form
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws SystemException
	 * @return ActionForward
	 * 
	 */
	 
	public String doCommon(BaseReportForm form, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		
		InvestmentSelectionReportForm actionForm = (InvestmentSelectionReportForm) form;	
		String forward = forwards.get("default");
		
		//default selectedView is set to selected funds
		if(StringUtils.equals(Constants.AVAILABLE, request.getParameter(Constants.SELECTED_VIEW))) {
			actionForm.setSelectedView(Constants.AVAILABLE);
		} else {
			actionForm.setSelectedView(Constants.SELECTED);
		}
		
		Date invesmentFeePageDateLowerLimit = getInvestmentPageLowerLimitDate();
		
		Contract contract = getUserProfile(request).getCurrentContract();
		
		List<Date> reportDates = FeeServiceDelegate.getInstance(Environment.getInstance().getAppId())
				.getDatesForInvestmentCostPage(contract.getContractNumber(), invesmentFeePageDateLowerLimit);
		
		
		request.setAttribute(Constants.REPORT_DATES_FEE_DISCLOSURE, reportDates);
		actionForm.setAvailableReportDates(reportDates);
		
		actionForm.setSelectedAsOfDate(request.getParameter(Constants.SELECTED_AS_OF_DATE));
		if(reportDates.size() > 0) {
			if(actionForm.getSelectedAsOfDate() == null) {
				// set the 1st date from the list in this case
				actionForm.setSelectedAsOfDate(String.valueOf(reportDates.get(0).getTime()));
			}
			if(StringUtils.equals(actionForm.getSelectedAsOfDate(), 
					String.valueOf(reportDates.get(0).getTime()))){
				request.setAttribute(Constants.SHOW_LINK, true);
			}
			actionForm.setDisplaySelectedAsOfDate(
					new Date(Long.valueOf(actionForm.getSelectedAsOfDate()).longValue()));
		}
		// Search parameter are set in session for print report
		HttpSession session = request.getSession(false);
		if(StringUtils.equals(PRINT_TASK, request.getParameter(TASK_KEY))
				|| StringUtils.equals(PRINT_PDF_TASK, request.getParameter(TASK_KEY))){
			actionForm.setSelectedView(
					(String)session.getAttribute(Constants.SELECTED_VIEW));
			actionForm.setSelectedAsOfDate(
					(String)session.getAttribute(Constants.SELECTED_AS_OF_DATE));
		} else {
			session.setAttribute(Constants.SELECTED_VIEW, actionForm.getSelectedView());
			session.setAttribute(Constants.SELECTED_AS_OF_DATE, actionForm.getSelectedAsOfDate());
		}
		
		
		forward = super.doCommon( actionForm, request, response);
		
		InvestmentCostReportData reportData = (InvestmentCostReportData) request.getAttribute(Constants.REPORT_BEAN);
	   
		// if there is footnotes , set it to request scope
		if(reportData.getFootNotes().size() > 0) {
			request.setAttribute(Constants.WEB_SYMBOLS_FOOTNOTES, reportData.getFootNotes().toArray(new String[1]));
	    }
		
		if(reportDates.size() > 0) {
			if(StringUtils.equals(actionForm.getSelectedAsOfDate(), String.valueOf(reportDates.get(0).getTime()))){
				actionForm.setShowAvailableOptions(true);
			} else {
				actionForm.setShowAvailableOptions(false);
			}
		} else {
			actionForm.setShowAvailableOptions(false);
		}
		
		FeeCmaDataImpl feeCmaData =  new FeeCmaDataImpl(Constants.COMPANY_NAME_NY.equalsIgnoreCase(Environment
				.getInstance().getSiteLocation()) ? Location.NY
				: Location.US);
		
		// load Total A, Total B Section Fees & FootNotes
		EstimatedJhRecordKeepingCostSummary recordKeepingCostsummary = getSummaryOfRecordKeepingFees(actionForm, reportData, feeCmaData);
		
		// Add N19, N20 & N21 footnotes.
		Map<FootNote, String> reportFootnotes = recordKeepingCostsummary.getReportFootnotes();
		boolean isSVFFund = false;
		List<String> svfFundIdList = new ArrayList<>();
		boolean addN20FootNote = false;
		boolean addN21FootNote = false;
		Map<InvestmentGroup,List<FundFeeVO>> funds = reportData.getInvestmentData();
		for(Map.Entry<InvestmentGroup,List<FundFeeVO>> entry : funds.entrySet()){
			for (FundFeeVO fundFeeVo : entry.getValue()) {
				addN20FootNote = true;
				if (!reportData.getContractDetails().isDefinedBenefitContract()
						&& StringUtils.equals(Constants.FUND_TYPE_GARUNTEED,
								fundFeeVo.getFundType())) {
					addN21FootNote = true;
				}
				if (StringUtils.equals(fundFeeVo.getFundFamilyCatCode(),
						Constants.FUND_FAMILY_CATEGORIZATION_SVF)){
					isSVFFund = true;
					svfFundIdList.add(fundFeeVo.getFundId()); 
				} 
			}
		}
		
		if (isSVFFund) {
			for(String svfFundId : svfFundIdList) {
				if (svfFundId.equalsIgnoreCase(Constants.FUND_CODE_MSV)) {
					reportFootnotes.put(FootNote.FOOTNOTE_N19, feeCmaData.getImpSecN19MSVFootnote());
				} else if (svfFundId.equalsIgnoreCase(Constants.FUND_CODE_NMY)) {
					reportFootnotes.put(FootNote.FOOTNOTE_N19, feeCmaData.getImpSecN19NMYFootnote());
				}
			}
		}
		
		if (addN20FootNote) {
			reportFootnotes.put(FootNote.FOOTNOTE_N20,feeCmaData.getImpSecN20Footnote());
		}

		if (addN21FootNote) {
			reportFootnotes.put(FootNote.FOOTNOTE_N21,feeCmaData.getImpSecN21Footnote());
		}
		
		request.setAttribute(Constants.ESTIMATED_JH_RECORDKEEPING_COST_SUMMARY, recordKeepingCostsummary);
		// Class zero indicator to display dynamic footer
		Date asOfDate = 	new Date(Long.valueOf(actionForm.getSelectedAsOfDate()));
		ContractClass contractClass= FeeServiceDelegate.getInstance(Environment.getInstance().getAppId()).getContractRatetype(contract.getContractNumber(),asOfDate);
		if(contractClass!=null && Constants.CLASS_ZERO.equals(contractClass.getClassId())) {
			actionForm.setClassZero(true);
		}
		boolean svgifInd = false;
		
		if(StringUtils.equals(Constants.SELECTED, actionForm.getSelectedView())) {
			svgifInd = FeeServiceDelegate.getInstance(Environment.getInstance().getAppId()).getSvgIndicatorFlg(contract.getContractNumber(), true);
			actionForm.setSvgifFlag(svgifInd);
		}else {
			svgifInd = FeeServiceDelegate.getInstance(Environment.getInstance().getAppId()).getSvgIndicatorFlg(contract.getContractNumber(), false);
			actionForm.setSvgifFlag(svgifInd);
		}
		return forward;
	}
	
	
	/**
	 * load Total A & B Section Fees
	 * 
	 * @param investmentSelectionReportForm
	 * @param reportData
	 * @throws SystemException
	 */
	private EstimatedJhRecordKeepingCostSummary getSummaryOfRecordKeepingFees(
			InvestmentSelectionReportForm investmentSelectionReportForm,
			InvestmentCostReportData reportData, FeeCmaDataImpl feeCmaData) throws SystemException {
		
		
		return EstimatedRecordKeepingCostComponentGenerator.generateSummaryOfRecordkeepingCostSubComponents(
				FeeServiceDelegate.getInstance(Environment.getInstance().getAppId())
				.isPreAlignmentDate(
						new Date(Long.valueOf(
								investmentSelectionReportForm
										.getSelectedAsOfDate())
								.longValue())),
				reportData.getContractDetails(),
				reportData.getRecordedEstimatedCostOfRKCharges(), (FeeCmaData) feeCmaData, 
				FeeServiceDelegate.getInstance(Environment.getInstance().getAppId())
				.isPreClassZeroPhaseTwo(
						new Date(Long.valueOf(
								investmentSelectionReportForm
										.getSelectedAsOfDate())
								.longValue())));
	
	}
	
	/*
	 * This method used to get the investment page lower date range of effective date 
	 * @return date - lower date range 
	 * @throws SystemException
	 */
	private Date getInvestmentPageLowerLimitDate() throws SystemException {
		Map<String, String> businessParams = EnvironmentServiceDelegate.getInstance(Environment.getInstance().getAppId()).getBusinessParamMap();
		String value = businessParams.get(FEE_INVESTMENT_PAGE_DATE_LIMIT);
		Date lowerLimitDate = null;
		if(StringUtils.isNotEmpty(value)) {
			try {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				lowerLimitDate = formatter.parse(value);
			} catch (ParseException e) {
				logger.error("Exception the business param FEE_INVESTMENT_PAGE_DATE_LIMIT is not " +
						"valid " + e.getMessage());
			    throw new SystemException("Exception the business param FEE_INVESTMENT_PAGE_DATE_LIMIT " +
			    		"is not valid " + e.getMessage());
			}
		}
		return lowerLimitDate;
	}
	
    /**
     * Overridden method returns unique report ID for Investment Cost Report Data.
     * @return reportId
     */
	@Override
	protected String getReportId() {
		return InvestmentCostReportData.REPORT_ID;
	}
    /**
     * Overridden method returns unique report name for Investment Cost Report Data.
     * @return reportName
     */
	@Override
	protected String getReportName() {
		return InvestmentCostReportData.REPORT_NAME;
	}
    /**
     * Overridden method used to set the default sort field for the criteria
     * @return sortFieldName
     */
	@Override
	protected String getDefaultSort() {
		return null;
	}
    /**
     * Overridden method used to set the default sort direction for the criteria
     * @return sortDirection
     */
	@Override
	protected String getDefaultSortDirection() {
		return null;
	}

	/**
	 * validates whether the ML funds are to be included 
	 * 
	 * @param request
	 * @return true, if the contract is 
	 * 					Merrill Lynch contract, 
	 * 					
	 * @throws SystemException
	 */
	protected boolean includeOnlyMerrillCoveredFunds(HttpServletRequest request, int contractNumber) throws SystemException {
		boolean isMerrillContract = false;
		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
    	if(contractNumber != 0){
    		ContractDetailsOtherVO contractDetailsOtherVO = contractServiceDelegate.getContractDetailsOther(contractNumber);
			if(contractDetailsOtherVO != null && contractDetailsOtherVO.isMerrillLynch()){
				isMerrillContract = true;
			}
    	}
        return isMerrillContract;
	}
	
	/* 
	 * This method is used to set up report criteria
	 * (non-Javadoc)
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria, com.manulife.pension.platform.web.report.BaseReportForm, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		InvestmentSelectionReportForm investmentSelectionReportForm = (InvestmentSelectionReportForm) form;
		UserProfile userProfile = getUserProfile(request);
		criteria.addFilter(InvestmentCostReportData.FILTER_CONTRACT_NO, Integer.valueOf(userProfile.getCurrentContract().getContractNumber()));
		criteria.addFilter(InvestmentCostReportData.FILTER_AS_OF_DATE, investmentSelectionReportForm.getSelectedAsOfDate());
		boolean selectedViewFlag = true;
		if(StringUtils.equals(Constants.AVAILABLE, investmentSelectionReportForm.getSelectedView())) {
			selectedViewFlag = false;
			criteria.addFilter(InvestmentCostReportData.FILTER_MERRILL_COVERED_FUNDS, includeOnlyMerrillCoveredFunds(request, userProfile.getCurrentContract().getContractNumber()));
		}else{
			criteria.addFilter(InvestmentCostReportData.FILTER_MERRILL_COVERED_FUNDS, false);
		}
		criteria.addFilter(InvestmentCostReportData.SELECTED_VIEW, selectedViewFlag);
		// check if chosen date is the last date in the report drop down
		if(investmentSelectionReportForm.getSelectedAsOfDate() != null &&
				investmentSelectionReportForm.getAvailableReportDates() != null) {
		    criteria.addFilter(InvestmentCostReportData.FILTER_CHECK_FOR_HISTORY, true);
		} else {
			 criteria.addFilter(InvestmentCostReportData.FILTER_CHECK_FOR_HISTORY, false);
		}
	}
    /**
     * Overrides this method to write the report to a PrintWriter.
     *
     * @param reportForm
     *            The report form.
     * @param report
     *            The report data.
     * @param request
     *            The HTTP request.
     * @return byte array
     */
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, 
			HttpServletRequest request)	throws SystemException {
		// no implementation
		return null;
	}
	
	/*
	 * check if chosen date is the last date in the report drop down
	 * @param availableReportDates
	 * @param currentChosenDate
	 * @return boolean
	 */
	private boolean checkIfLastDateInReportList(List<Date> availableReportDates, Date currentChosenDate) {
		int totalDatesAvailable = availableReportDates.size();
		for(Date date : availableReportDates) {
			totalDatesAvailable --;	
			if(currentChosenDate.compareTo(date) == 0) {
				break;
			}
		}
		if(totalDatesAvailable == 0) {
			return true;
		}
		return false;
	}
	/**
	 * This method used to trigger the viewed report event if the user is a recipient for the report 
	 * and no other user has viewed the report yet.
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            Form
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws SystemException
	 * @return ActionForward
	 * 
	 */
	protected void postExecute(InvestmentSelectionReportForm actionForm, HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		super.postExecute( actionForm, request, response);
		
		if(StringUtils.isNotEmpty(actionForm.getSelectedAsOfDate()) ) {
			
			int contractId = getUserProfile(request).getContractProfile().getContract().getContractNumber();
			long userProfileId = getUserProfile(request).getPrincipal().getProfileId();
			String reportType = FeeDisclosurePdfTypes.SixtyDayNotificationPdf.getType();
			Date reportAsOfDate =  new Date(Long.valueOf(actionForm.getSelectedAsOfDate()).longValue());
			
			OrderServiceDelegate orderService = OrderServiceDelegate.getInstance();
			
			// if the user is a recipient for the report
			// and no other user has viewed the report yet, trigger the event
			if(orderService.isUserARecipientForFeeDisclsoureReport(contractId, userProfileId, reportAsOfDate, reportType)
					&& !orderService.isDisclosureViewedByUser(contractId, reportType, reportAsOfDate)){
				FeeDisclosureViewedReportEvent  feeDisclosureViewedReportEvent = new FeeDisclosureViewedReportEvent();
				feeDisclosureViewedReportEvent.setContractId(contractId);
				feeDisclosureViewedReportEvent.setProfileId(userProfileId);
				feeDisclosureViewedReportEvent.setAsOfDate(reportAsOfDate.getTime());
				feeDisclosureViewedReportEvent.setBatchType(reportType);
				feeDisclosureViewedReportEvent.setInitiator(userProfileId);
				EventClientUtility.getInstance(Environment.getInstance().getAppId()).prepareAndSendJMSMessage(feeDisclosureViewedReportEvent);
			}
		}
	}
	
	@RequestMapping(value = "/investmentRelatedCosts/", params = {"task=printPDF"}, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String doPrintPDF(
			@Valid @ModelAttribute("investmentSelectionReportForm") InvestmentSelectionReportForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		String forward = preExecute(actionForm, request, response);
		if (StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forward = "/do" + new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:" + forward;
			}
		}
		forward = super.doPrintPDF(actionForm, request, response);
		postExecute(actionForm, request, response);

		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	
	protected ByteArrayOutputStream prepareXMLandGeneratePDF(BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) throws SystemException {

		ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
		try {
			InvestmentSelectionReportForm investmentSelectionReportForm = (InvestmentSelectionReportForm) reportForm;
			InvestmentCostReportData data = (InvestmentCostReportData) report;
			Object xmlTree = prepareXMLFromReport(investmentSelectionReportForm, data, request);
			String xsltFileName = getXSLTFileName();
			String configFileName = getFOPConfigFileName();
			if (xmlTree == null || xsltFileName == null) {
				return pdfOutStream;
			}
			String xsltfile = ReportsXSLProperties.get(xsltFileName);
			String configfile = ReportsXSLProperties.get(configFileName);
			String includedXSLPath = ReportsXSLProperties.get(CommonConstants.INCLUDED_XSL_FILES_PATH);
			if (xmlTree instanceof String) {
				pdfOutStream = PDFGenerator.getInstance().generatePDF((String) xmlTree, xsltfile, configfile,
						includedXSLPath, true);
			}

		} catch (Exception exception) {
			String message = null;
			if (exception instanceof ContentException) {
				message = "Error occured while retrieveing CMA Content during PDF creation.";
			} else if (exception instanceof ParserConfigurationException) {
				message = "Error occured while creating Document object during PDF creation.";
			} else if (exception instanceof FOPException || exception instanceof TransformerException
					|| exception instanceof IOException) {
				message = "Error occured during PDF generation.";
			} else {
				message = "Error occured during PDF generation.";
			}

			throw new SystemException(exception, message);
		}
		return pdfOutStream;
	}

	@SuppressWarnings("unchecked")
	public String prepareXMLFromReport(InvestmentSelectionReportForm investmentSelectionReportForm,
			InvestmentCostReportData investmentCostReportData, HttpServletRequest request)
			throws ParserConfigurationException, TransformerException, SystemException {

		PDFDocument doc = new PDFDocument();
		
		LayoutPage layoutPageBean = getLayoutPage(PdfConstants.REGULATOTY_DISCLOSURE_PATH, request);

		Element rootElement = doc.createRootElement(PdfConstants.FEE_DISCLOSURES);

		String regDiscText1 = ContentUtility.getContentAttributeText(layoutPageBean, CommonContentConstants.INTRO2_TEXT,
				null);

		doc.appendTextNode(rootElement, "regDiscText1", regDiscText1);

	

		Contract currentContract = getUserProfile(request).getCurrentContract();
		doc.appendTextNode(rootElement, "currentContract", Integer.toString(currentContract.getContractNumber()));
		doc.appendTextNode(rootElement, "currentContractName", currentContract.getCompanyName());

		HttpSession session = request.getSession(false);
		investmentSelectionReportForm.setSelectedAsOfDate((String) session.getAttribute(Constants.SELECTED_AS_OF_DATE));
		String formattedValue = DateRender.formatByPattern(
				new Date(Long.valueOf(investmentSelectionReportForm.getSelectedAsOfDate()).longValue()), null,
				RenderConstants.MEDIUM_MDY_SLASHED);
		doc.appendTextNode(rootElement, PdfConstants.ASOF_DATE, formattedValue);

		
		setIntroXMLElements(layoutPageBean, doc, rootElement, currentContract);

		EstimatedJhRecordKeepingCostSummary recordKeepingCostsummary = (EstimatedJhRecordKeepingCostSummary) request
				.getAttribute(Constants.ESTIMATED_JH_RECORDKEEPING_COST_SUMMARY);
		String regDiscText2 = recordKeepingCostsummary.getIntroductionText();
		String regDiscText3 = ContentHelper.getContentText(ContentConstants.REPORT_DETAILS_SUMMARY_CONTENT,
				ContentTypeManager.instance().MESSAGE, null);
		String fwiDisclosureText = ContentHelper.getContentText(ContentConstants.FEE_WAIVER_DISCLOSURE_TEXT,
				ContentTypeManager.instance().DISCLAIMER, null);
		String restrictedFundsText = ContentHelper.getContentText(ContentConstants.REGULATORY_RESTRICTED_FUNDS_TEXT,
				ContentTypeManager.instance().MESSAGE, null);
		
		doc.appendTextNode(rootElement, "regDiscText2", regDiscText2);
		doc.appendTextNode(rootElement, "regDiscText3", regDiscText3);
		doc.appendTextNode(rootElement, "fwiDisclosureText",
				StringEscapeUtils.unescapeHtml(ContentUtility.filterCMAContentForPDF(fwiDisclosureText)));

		if (includeOnlyMerrillCoveredFunds(request, currentContract.getContractNumber())) {
			doc.appendTextNode(rootElement, "restrictedFundsText",
					"\n" + StringEscapeUtils.unescapeHtml(ContentUtility.filterCMAContentForPDF(restrictedFundsText)));
		}

		doc.appendTextNode(rootElement, "totalAandBlableText",
				ContentUtility.filterCMAContentForPDF(recordKeepingCostsummary.getLableText()));
		doc.appendTextNode(rootElement, "totalAandB", recordKeepingCostsummary.getTotalAmount());
		doc.appendTextNode(rootElement, "totalAintroductionText",
				parseSuperTagToXSLFO(recordKeepingCostsummary.getSectionTotalA().getIntroductionText()));
		doc.appendTextNode(rootElement, "totalBintroductionText",
				parseSuperTagToXSLFO(recordKeepingCostsummary.getSectionTotalB().getIntroductionText()));
		doc.appendTextNode(rootElement, "totalAlableText",
				parseSuperTagToXSLFO(recordKeepingCostsummary.getSectionTotalA().getLableText()));
		doc.appendTextNode(rootElement, "totalBlableText",
				parseSuperTagToXSLFO(recordKeepingCostsummary.getSectionTotalB().getLableText()));
		doc.appendTextNode(rootElement, "totalA", recordKeepingCostsummary.getSectionTotalA().getTotalAmount());
		doc.appendTextNode(rootElement, "totalB", recordKeepingCostsummary.getSectionTotalB().getTotalAmount());
		doc.appendTextNode(rootElement, "fundsSelected",
				Integer.toString(investmentCostReportData.getNumberOfFundsSelected()));
		doc.appendTextNode(rootElement, "classOfFunds", investmentCostReportData.getContractClassName());
		String view;
		if (investmentSelectionReportForm.getSelectedView().equalsIgnoreCase("selected")) {
			view = "Selected investment options";
		} else {
			view = "All available investment options";
		}
		doc.appendTextNode(rootElement, "shown", view);
		if (!recordKeepingCostsummary.isPreAlignmentIndicator()) {
			doc.appendTextNode(rootElement, "notPreAlignment", "notPreAlignment");
			Element feeDetailsSectionAElement;
			List<FeeDetail> FeeDetailSectionA = recordKeepingCostsummary.getSectionTotalA().getFeeDetails();
			for (FeeDetail feeDetailSecA : FeeDetailSectionA) {
				feeDetailsSectionAElement = doc.createElement("sectionElementsA");
				doc.appendTextNode(feeDetailsSectionAElement, "description",
						parseSuperTagToXSLFO(feeDetailSecA.getDescription()));
				doc.appendTextNode(feeDetailsSectionAElement, "methodOfPayment", feeDetailSecA.getMethodOfPayment());
				doc.appendTextNode(feeDetailsSectionAElement, "amount", feeDetailSecA.getAmount());
				doc.appendElement(rootElement, feeDetailsSectionAElement);
			}

			Element feeDetailsSectionBElement;
			List<FeeDetail> FeeDetailSectionB = recordKeepingCostsummary.getSectionTotalB().getFeeDetails();
			for (FeeDetail feeDetailSecB : FeeDetailSectionB) {
				feeDetailsSectionBElement = doc.createElement("sectionElementsB");
				doc.appendTextNode(feeDetailsSectionBElement, "description",
						parseSuperTagToXSLFO(feeDetailSecB.getDescription()));
				doc.appendTextNode(feeDetailsSectionBElement, "methodOfPayment", feeDetailSecB.getMethodOfPayment());
				doc.appendTextNode(feeDetailsSectionBElement, "amount", feeDetailSecB.getAmount());
				doc.appendElement(rootElement, feeDetailsSectionBElement);
			}

			Element revenueAddendumSectionAElement;
			List<FeeDetail> revenueAddendumDetailsA = recordKeepingCostsummary.getRevenueAddendumDetailsSubSection()
					.getFeeDetails();
			for (FeeDetail feeDetailSecA : revenueAddendumDetailsA) {
				revenueAddendumSectionAElement = doc.createElement("revenueAddendumElementsA");
				doc.appendTextNode(revenueAddendumSectionAElement, "description",
						parseSuperTagToXSLFO(feeDetailSecA.getDescription()));
				doc.appendTextNode(revenueAddendumSectionAElement, "amount", feeDetailSecA.getAmount());
				doc.appendElement(rootElement, revenueAddendumSectionAElement);
			}

			Element revenueAddendumSectionBElement;

			revenueAddendumSectionBElement = doc.createElement("revenueAddendumElementsB");
			doc.appendTextNode(revenueAddendumSectionBElement, "description", parseSuperTagToXSLFO(
					recordKeepingCostsummary.getRevenueAddendumDetailsSubSection().getLableText()));
			doc.appendTextNode(revenueAddendumSectionBElement, "amount",
					recordKeepingCostsummary.getRevenueAddendumDetailsSubSection().getAmount());
			doc.appendElement(rootElement, revenueAddendumSectionBElement);
		}

		// Class Zero Phase 2 Changes - Dollar Based Section

		doc.appendTextNode(rootElement, "dollarBasedSectionHeading", ContentUtility
				.filterCMAContentForPDF(recordKeepingCostsummary.getDollarBasedSection().getIntroductionText()));
		doc.appendTextNode(rootElement, "dollarBasedSectionIntro",
				recordKeepingCostsummary.getDollarBasedSection().getLableText());

		if (!recordKeepingCostsummary.isPreClassZeroPhaseTwoInd()) {
			Element feeDetailsSectionDBElement;
			List<DollarBasedFeeDetail> dollarBasedFeeDetail = recordKeepingCostsummary.getDollarBasedSection()
					.getFeeDetails();
			for (DollarBasedFeeDetail feeDetailDB : dollarBasedFeeDetail) {
				feeDetailsSectionDBElement = doc.createElement("sectionElementsDB");
				doc.appendTextNode(feeDetailsSectionDBElement, "description",
						parseSuperTagToXSLFO(feeDetailDB.getDescription()));
				doc.appendTextNode(feeDetailsSectionDBElement, "methodOfPayment", feeDetailDB.getMethodOfPayment());
				doc.appendTextNode(feeDetailsSectionDBElement, "amount", feeDetailDB.getAmount());
				doc.appendTextNode(feeDetailsSectionDBElement, "frequency", feeDetailDB.getFeeFrequency());
				doc.appendElement(rootElement, feeDetailsSectionDBElement);

				doc.appendTextNode(rootElement, "notPreClassZeroPhaseTwo", "notPreClassZeroPhaseTwo");
			}
		}

		// Class Zero Phase 2 Changes - Dollar Based section Ends
		BigDecimal maxCredit=new BigDecimal(0.15);
		Element fundDetailsElement;
		Element fundDetailElement;
		Boolean isSvpContract= investmentCostReportData.getHasSvpFund();
		Map<InvestmentGroup, List<FundFeeVO>> funds = investmentCostReportData.getInvestmentData();
		int varStatus = 0;
		for (Map.Entry<InvestmentGroup, List<FundFeeVO>> entry : funds.entrySet()) {
			fundDetailsElement = doc.createElement(PdfConstants.FUND_DETAILS);

			doc.appendTextNode(fundDetailsElement, PdfConstants.COLOR_CODE, entry.getKey().getColorcode());

			if (entry.getKey().getGroupname().equals(PdfConstants.GROUPNAME_GROWTHANDINCOME)) {
				doc.appendTextNode(fundDetailsElement, PdfConstants.FRONT_COLOR, "#000000");
			} else {
				doc.appendTextNode(fundDetailsElement, PdfConstants.FRONT_COLOR, "#ffffff");
			}

			doc.appendTextNode(fundDetailsElement, PdfConstants.FUND_GROUP, entry.getKey().getGroupname());
			
			int rowcolorcount = 0;
			for (FundFeeVO fundFeeVo : entry.getValue()) {
				if(isSvpContract && (maxCredit.compareTo(fundFeeVo.getTotalRevenueUsedTowardsPlanCosts())<0)){
					maxCredit=fundFeeVo.getTotalRevenueUsedTowardsPlanCosts();
				}
				
				fundDetailElement = doc.createElement(PdfConstants.FUND_DETAIL);
				setFundDetailsXMLElements(doc, fundDetailElement, fundFeeVo, investmentSelectionReportForm,
						investmentCostReportData, request);

				if (rowcolorcount % 2 == 0) {
					doc.appendTextNode(fundDetailElement, PdfConstants.ROW_COLOR_CHANGE, "#DCECF1");
				} else if (rowcolorcount % 2 != 0) {
					doc.appendTextNode(fundDetailElement, PdfConstants.ROW_COLOR_CHANGE, "#ffffff");
				}

				rowcolorcount++;
				doc.appendElement(fundDetailsElement, fundDetailElement);
			}
			
			
			doc.appendElement(rootElement, fundDetailsElement);
			varStatus++;
		}

		Element theReport;

		theReport = doc.createElement("theReport");
		doc.appendTextNode(theReport, "averageUnderlyingFundNetCost",
				NumberRender.format(investmentCostReportData.getAverageUnderlyingFundNetCost(), DEFAULT_VALUE, PATTERN,
						null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
		doc.appendTextNode(theReport, "averageRevenueFromUnderlyingFund",
				NumberRender.format(investmentCostReportData.getAverageRevenueFromUnderlyingFund(), DEFAULT_VALUE,
						PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
		doc.appendTextNode(theReport, "averageRevenueFromSubAccount",
				NumberRender.format(investmentCostReportData.getAverageRevenueFromSubAccount(), DEFAULT_VALUE, PATTERN,
						null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
		doc.appendTextNode(theReport, "averageTotalRevenueUsedTowardsPlanCosts",
				NumberRender.format(investmentCostReportData.getAverageTotalRevenueUsedTowardsPlanCosts(),
						DEFAULT_VALUE, PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
		doc.appendTextNode(theReport, "averageExpenseRatio",
				NumberRender.format(investmentCostReportData.getAverageExpenseRatio(), DEFAULT_VALUE, PATTERN, null,
						SCALE, ROUNDING_MODE, DIGITS, SIGN));

		doc.appendElement(rootElement, theReport);
		List<String> footnotes = recordKeepingCostsummary.getOrderedFootNotes();
		Element footnotesElement;
		for (String footnote : footnotes) {
            footnote=PATTERN_ANCHOR_REPLACE.matcher(footnote).replaceAll("");
			footnotesElement = doc.createElement(PdfConstants.TAB_FOOTNOTES);
			doc.appendTextNode(footnotesElement, PdfConstants.VALUE, footnote);
			doc.appendElement(rootElement, footnotesElement);
		}
		Map<String, MutableFootnote> footnotesMap = new LinkedMap();
		MutableFootnote[] footnotesUSA;
		try {
			footnotesUSA = retrieveFootnotes();
			Arrays.sort(footnotesUSA, Footnotes.getInstance());
			if (footnotesUSA != null) {
				for (MutableFootnote footnoteUSA : footnotesUSA) {
					footnotesMap.put(footnoteUSA.getSymbol().trim(), footnoteUSA);
				}
			}
		} catch (ContentException e) {
			throw new TransformerException("Exception occured in footnotesElement " + e.toString());
		}

		HashMap<String, String> hash = new HashMap<String, String>();
		HashMap<String, Footnote> hashNotes = new LinkedHashMap<String, Footnote>();
		String[] symbols = investmentCostReportData.getFootNotes().toArray(new String[1]);
		for (int i = 0; i < symbols.length; i++) {
			hash.put(symbols[i], symbols[i]);
		}

		Set<String> keySet = footnotesMap.keySet();
		Iterator<String> it = keySet.iterator();
		String symbol = StringUtils.EMPTY;
		while (it.hasNext()) {
			symbol = it.next();
			if (hash.containsKey(symbol)) {
				hashNotes.put(symbol, footnotesMap.get(symbol));
			}
		}

		Element orderedFootnotesElement;
		for (Map.Entry<String, Footnote> entry : hashNotes.entrySet()) {
			String FOOTNOTE = "ofn";
			orderedFootnotesElement = doc.createElement(FOOTNOTE);
			String footNotesContent = entry.getValue().getText();
			doc.appendTextNode(orderedFootnotesElement, PdfConstants.KEY, entry.getKey());
			doc.appendTextNode(orderedFootnotesElement, PdfConstants.VALUE, footNotesContent);
			doc.appendElement(rootElement, orderedFootnotesElement);
		}

		// Sets footer, footnotes and disclaimer
		String footer;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);

		if (investmentSelectionReportForm.isClassZero() && investmentSelectionReportForm.isSvgifFlag()) {
		String svgifFooter = ContentHelper.getContentText(ContentConstants.SVGIF_DISCLOSURE,
					ContentTypeManager.instance().MESSAGE, null);		
		String sigFooter = ContentHelper.getContentText(ContentConstants.CLASS_ZERO_FOOTER,
					ContentTypeManager.instance().MESSAGE, null);
		 if (sigFooter.contains("{0}")) {
			 sigFooter = sigFooter.replace("{0}",  df.format(maxCredit)+"%");
			}
		footer = svgifFooter+sigFooter;
		}
		else if(investmentSelectionReportForm.isClassZero()) { 
			footer = ContentHelper.getContentText(ContentConstants.CLASS_ZERO_FOOTER,
					ContentTypeManager.instance().MESSAGE, null);
			 if (footer.contains("{0}")) {
				 footer = footer.replace("{0}", df.format(maxCredit)+"%");
				}
		}
		else {
			footer = ContentHelper.getContentText(ContentConstants.NON_CLASS_ZERO_FOOTER,
					ContentTypeManager.instance().MESSAGE, null);
		}

		doc.appendTextNode(rootElement, PdfConstants.FOOTER, footer);

		String footnotesAtBottom = ContentUtility.getPageFootnotes(layoutPageBean, new String[] {}, -1);
		final Pattern PATTERN_LINE_BREAK = Pattern.compile(".<br[^>]*>", Pattern.CASE_INSENSITIVE);
		footnotesAtBottom = PATTERN_LINE_BREAK.matcher(footnotesAtBottom).replaceAll(".<p></p>");
		doc.appendTextNode(rootElement, PdfConstants.FOOTNOTES, footnotesAtBottom);

		String globalDisclosureText = ContentHelper.getContentText(ContentConstants.GLOBAL_DISCLOSURE_PSW,
				ContentTypeManager.instance().MISCELLANEOUS, null);
		final Pattern PATTERN_LINE_BREAK_DISC = Pattern.compile(".<br[^>]*>", Pattern.CASE_INSENSITIVE);
		globalDisclosureText = PATTERN_LINE_BREAK_DISC.matcher(globalDisclosureText).replaceAll("<p></p>");
		doc.appendTextNode(rootElement, PdfConstants.GLOBAL_DISCLOSURE, globalDisclosureText);
		
		
		String gaCode = ContentHelper.getContentText(ContentConstants.GA_CODE,
				ContentTypeManager.instance().MISCELLANEOUS, null);
		doc.appendTextNode(rootElement, PdfConstants.GA_CODE, gaCode);
		

		StringWriter writer = new StringWriter();
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer1 = tf.newTransformer();
		transformer1.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer1.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer1.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer1.setOutputProperty(OutputKeys.ENCODING, UTF8);

		transformer1.transform(new DOMSource((Document) doc.getDocument()), new StreamResult(writer));
		writer.flush();

		return writer.toString();
	}

	@Override
	public String getXSLTFileName() {
		return XSLT_FILE_KEY_NAME;
	}

	public static String parseSuperTagToXSLFO(String content) {

		final Pattern PATTERN_SUP_TAG = Pattern.compile("<sup[^>]*>|<SUP[^>]*>");
		final String FO_INLINE_SUPERSCRIPT = "<FRWSUP>";

		// replace all <sup> with <fo:inline baseline-shift="super">
		content = PATTERN_SUP_TAG.matcher(content).replaceAll(FO_INLINE_SUPERSCRIPT);

		return content;
	}

	private void setFundDetailsXMLElements(PDFDocument doc, Element fundDetailElement, FundFeeVO fundFeeVo,
			InvestmentSelectionReportForm investmentSelectionReportForm,
			InvestmentCostReportData investmentCostReportData, HttpServletRequest request) throws SystemException {

		Element fundTxnElement = doc.createElement(PdfConstants.FUND_TXN);
		if (fundFeeVo != null) {
			doc.appendTextNode(fundTxnElement, PdfConstants.FUND_ID, fundFeeVo.getFundId());
			doc.appendTextNode(fundTxnElement, PdfConstants.FEE_WAIVER_INDICATOR,
					String.valueOf(investmentCostReportData.isFeeWaiverFund(fundFeeVo.getFundId())));

			if (includeOnlyMerrillCoveredFunds(request,
					getUserProfile(request).getCurrentContract().getContractNumber())) {
				doc.appendTextNode(fundTxnElement, PdfConstants.MERRILL_RESRICTED_FUND_INDICATOR,
						String.valueOf(investmentCostReportData.isRestrictedFund(fundFeeVo.getFundId())));
			}
			doc.appendTextNode(fundTxnElement, PdfConstants.FUND_NAME, fundFeeVo.getFundName());
			StringBuffer footNoteMarkers = new StringBuffer();
			if (fundFeeVo.getFootNoteMarkers().size() > 0) {
				for (Iterator iterator = fundFeeVo.getFootNoteMarkers().iterator(); iterator.hasNext();) {
					footNoteMarkers.append((String) iterator.next());
				}
			}

			if (fundFeeVo.getFundType().equals("GA")) {
				footNoteMarkers.append("N21");
			}
			if ((fundFeeVo.getFundId().equals("MSV")) || (fundFeeVo.getFundId().equals("NMY"))) {
				footNoteMarkers.append("N19");
			}

			doc.appendTextNode(fundTxnElement, "footNoteMarkers", footNoteMarkers.toString());

			doc.appendTextNode(fundTxnElement, PdfConstants.UNDERLYING_FUND_NETCOST,
					NumberRender.format(fundFeeVo.getUnderlyingFundNetCost(), DEFAULT_VALUE, PATTERN, null, SCALE,
							ROUNDING_MODE, DIGITS, SIGN));
			doc.appendTextNode(fundTxnElement, PdfConstants.REVENUE_FROM_UNDERLYINGFUND,
					NumberRender.format(fundFeeVo.getRevenueFromUnderlyingFund(), DEFAULT_VALUE, PATTERN, null, SCALE,
							ROUNDING_MODE, DIGITS, SIGN));
			doc.appendTextNode(fundTxnElement, PdfConstants.REVENUE_FROM_SUBACCOUNT,
					NumberRender.format(fundFeeVo.getRevenueFromSubAccount(), DEFAULT_VALUE, PATTERN, null, SCALE,
							ROUNDING_MODE, DIGITS, SIGN));
			doc.appendTextNode(fundTxnElement, PdfConstants.TOTAL_REVENUE_USED_TOWARDS_PLANCOSTS,
					NumberRender.format(fundFeeVo.getTotalRevenueUsedTowardsPlanCosts(), DEFAULT_VALUE, PATTERN, null,
							SCALE, ROUNDING_MODE, DIGITS, SIGN));
			doc.appendTextNode(fundTxnElement, PdfConstants.EXPENSE_RATIO, NumberRender.format(
					fundFeeVo.getExpenseRatio(), DEFAULT_VALUE, PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
			doc.appendTextNode(fundTxnElement, PdfConstants.REDEMPTION_FEE, NumberRender.format(
					fundFeeVo.getRedemptionFee(), DEFAULT_VALUE, PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));

			if (investmentSelectionReportForm.getSelectedView().equals(Constants.SELECTED)) {
				if (fundFeeVo.isFundfeeChanged() || fundFeeVo.isRedemptionFeeChanged()) {
					doc.appendTextNode(fundTxnElement, "fontWeight", "bold");
				}

			} else {
				if (fundFeeVo.isSelectedFund()) {
					if (fundFeeVo.isFundfeeChanged() || fundFeeVo.isRedemptionFeeChanged()) {
						doc.appendTextNode(fundTxnElement, "fontWeight", "bold");
						doc.appendTextNode(fundTxnElement, "rowColor", "#FFFFDB");
					} else {
						doc.appendTextNode(fundTxnElement, "rowColor", "#FFFFDB");
					}
				} else {
					if (!fundFeeVo.isFundfeeChanged() && !fundFeeVo.isRedemptionFeeChanged()) {
						doc.appendTextNode(fundTxnElement, "fontWeight", "bold");
					}
				}
			}
		}
		doc.appendElement(fundDetailElement, fundTxnElement);
	}

	private MutableFootnote[] retrieveFootnotes() throws ContentException {
		MutableFootnote[] footnotes = (MutableFootnote[]) BrowseServiceDelegate.getInstance()
				.findContent(ContentTypeManager.instance().FOOTNOTE);
		return footnotes == null ? new MutableFootnote[0] : footnotes;
	}

	protected LayoutPage getLayoutPage(String id, HttpServletRequest request) {

		LayoutBean layoutBean = LayoutBeanRepository.getInstance()
				.getPageBean("/investment/feeDisclosureInvestmentReport.jsp");
		LayoutPage layoutPageBean = (LayoutPage) layoutBean.getLayoutPageBean();
		return layoutPageBean;
	}

	
	/*
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of the CL#137697.
	 * 
	 */
	 @Autowired
	 private PSValidatorFWNull  psValidatorFWNull;

	 @InitBinder
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	  binder.bind( request);
	  binder.addValidators(psValidatorFWNull);
	}
	
}
