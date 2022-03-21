package com.manulife.pension.bd.web.bob.transaction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.report.BOBReportController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsDeferral;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsDeferralReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * In support of transaction history details for Deferral changes,
 * 
 * @author AAmbrose
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"deferralChangeDetailsForm"})

public class DeferralChangeDetailsReportController extends BOBReportController {
	@ModelAttribute("deferralChangeDetailsForm") 
	public DeferralChangeDetailsReportForm populateForm() 
	{
		return new DeferralChangeDetailsReportForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{forwards.put("input","/transaction/deferralChangeDetailsReport.jsp");
	forwards.put("default","/transaction/deferralChangeDetailsReport.jsp");
	forwards.put("filter","/transaction/deferralChangeDetailsReport.jsp");
	forwards.put("page","/transaction/deferralChangeDetailsReport.jsp");
	forwards.put("sort","/transaction/deferralChangeDetailsReport.jsp");}


	private static final String TRANSACTION_HISTORY = "TH";

	private static final String DOWNLOAD_REPORT_NAME = "DeferalChangeDetails-";

	private static final String XSLT_FILE_KEY_NAME = "DeferralChangeDetailsReport.XSLFile";

	private static final String DEFERRAL_UPDATE = "Deferral Update";

	public DeferralChangeDetailsReportController() {
		super(DeferralChangeDetailsReportController.class);
	}

	/**
	 * @see BDReportController#getReportId()
	 */
	protected String getReportId() {
		return TransactionDetailsDeferralReportData.REPORT_ID;
	}

	/**
	 * @see BDReportController#getReportName()
	 */
	protected String getReportName() {
		return TransactionDetailsDeferralReportData.REPORT_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return BDConstants.LAST_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria,
	 *      com.manulife.pension.platform.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		if (logger.isDebugEnabled())
			logger.debug("entry -> populateReportCriteria");

		DeferralChangeDetailsReportForm dcForm = (DeferralChangeDetailsReportForm) form;

		int contractNumber = getBobContext(request).getCurrentContract()
				.getContractNumber();

		criteria.addFilter(
				TransactionDetailsDeferralReportData.FILTER_CONTRACTID,
				new Integer(contractNumber));
		criteria.addFilter(
				TransactionDetailsDeferralReportData.FILTER_PROFILE_ID, dcForm
						.getProfileId());
		criteria.addFilter(
				TransactionDetailsDeferralReportData.FILTER_TARGET_DATE, dcForm
						.getTransactionDate());
		criteria.addFilter(TransactionDetailsDeferralReportData.FILTER_SCREEN,
				TRANSACTION_HISTORY);
		criteria.addFilter(
				TransactionDetailsDeferralReportData.FILTER_APPLICATION_ID,
				TransactionDetailsDeferralReportData.PSW_APPLICATION_ID);
		if (!getUserProfile(request).isInternalUser()) {
			criteria
					.addFilter(
							TransactionDetailsDeferralReportData.FILTER_EXTERNAL_USER_VIEW,
							BDConstants.YES);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <-  populateReportCriteria");
	}

	/**
	 * to perform the History Print
	 * 
	 * @param ActionMapping
	 *            mapping
	 * @param HttpServletRequest
	 *            BaseReportForm reportForm
	 * @param HttpServletResponse
	 *            request
	 * @param HttpServletResponse
	 *            response
	 * @return ActionForward forward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/transaction/deferralChangeDetailsReport/", params={"task=historyPrint"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doHistoryPrint(@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		String forward = preExecute(form, request, response);
    	if(StringUtils.isNotBlank(forward)) {
    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
    	}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doHistoryPrint");
		}

		 forward = BDConstants.HISTORY_PRINT;

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doHistoryPrint");
		}

		return forward;
	}


	/* 
	 * This method will return the File Name of the CSV file.
	 * The CSV file will be of the Format 
	 * "DeferalChangeDetails-<ContractNumber>-<LastName>-<mmddyyyy>.csv"
	 * (non-Javadoc)
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#getFileName(com.manulife.pension.platform.web.report.BaseReportForm, javax.servlet.http.HttpServletRequest)
	 */
	protected String getFileName(BaseReportForm reportForm,
			HttpServletRequest request) {
		Contract currentContract = getBobContext(request).getCurrentContract();
		ParticipantAccountDetailsVO participantAccountDetailsVO = (ParticipantAccountDetailsVO) request
				.getAttribute("details");
		return DOWNLOAD_REPORT_NAME
				+ currentContract.getContractNumber()
				+ BDConstants.NO_RULE
				+ participantAccountDetailsVO.getLastName()
				+ BDConstants.NO_RULE
				+ DateRender.formatByPattern(
						((DeferralChangeDetailsReportForm) reportForm)
								.getTransactionDate(), null,
						RenderConstants.MEDIUM_YMD_DASHED,
						BDConstants.DATE_FORMAT_MMDDYYYY) + CSV_EXTENSION;
	}

	/*
	 * (non-Javadoc) Called by framework to generate excel (csv) data
	 * 
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#getDownloadData(com.manulife.pension.platform.web.report.BaseReportForm,
	 *      com.manulife.pension.service.report.valueobject.ReportData,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getDownloadData");
		}

		StringBuffer buffer = new StringBuffer();

		DeferralChangeDetailsReportForm deferralForm = (DeferralChangeDetailsReportForm) reportForm;

		ParticipantAccountDetailsVO detailsVO = (ParticipantAccountDetailsVO) request
				.getAttribute(BDConstants.BEAN_DETAILS);

		Contract currentContract = getBobContext(request).getCurrentContract();
		buffer.append(BDConstants.CONTRACT).append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
				currentContract.getCompanyName()).append(LINE_BREAK);

		buffer.append(BDConstants.DEFERRAL_CHANGE_SUMMARY).append(LINE_BREAK);
		buffer.append(BDConstants.TRANSACTION_TYPE).append(COMMA).append(DEFERRAL_UPDATE)
				.append(LINE_BREAK);
		buffer.append(BDConstants.NAME).append(COMMA).append(
				escapeField(detailsVO.getLastName() + ", "
						+ detailsVO.getFirstName())).append(LINE_BREAK);
		buffer.append(BDConstants.SSN).append(COMMA).append(
				SSNRender.format(detailsVO.getSsn(), SSNRender.MASK)).append(
				LINE_BREAK);

		buffer.append(LINE_BREAK);
		
		BDLayoutBean bean = ApplicationHelper.getLayoutStore(request.getServletContext()).getLayoutBean(
                BDPdfConstants.DEFERRAL_CHANGE_DETAILS_PATH, request);
		LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();
		String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY1_HEADER, null);
		buffer.append(bodyHeader1).append(" as of ").append(COMMA);
		buffer.append(
				
						 deferralForm.getTransactionDateFormatted()).append(
				LINE_BREAK);
		buffer
				.append(
						"Requested Date,Item Changed,Value Before,Value Requested,Value Updated,Status,Changed By,Comments")
				.append(LINE_BREAK);

		Iterator transactionDetailsDeferralIterator = deferralForm.getReport()
				.getDetails().iterator();
		while (transactionDetailsDeferralIterator.hasNext()) {
			TransactionDetailsDeferral transactionDetailsDeferral = (TransactionDetailsDeferral) transactionDetailsDeferralIterator
					.next();

			buffer.append(
					getNotNull(transactionDetailsDeferral.getRequestedDate()))
					.append(COMMA);
			buffer.append(transactionDetailsDeferral.getItemChanged()).append(
					COMMA);
			buffer.append(
					transactionDetailsDeferral.getValueBeforeForDownload())
					.append(COMMA);
			buffer.append(
					getNotNull(transactionDetailsDeferral
							.getValueRequestedForDownload())).append(COMMA);
			buffer.append(
					transactionDetailsDeferral.getValueUpdatedForDownload())
					.append(COMMA);
			buffer.append(getNotNull(transactionDetailsDeferral.getStatus()))
					.append(COMMA);
			buffer.append(getCsvString(transactionDetailsDeferral.getChangedBy()));
			if (transactionDetailsDeferral.genSecondLine()) {
				buffer.append(COMMA);
				buffer.append(BDConstants.DOUBLE_QUOTES);
				buffer.append(transactionDetailsDeferral.getComments());
				buffer.append(BDConstants.DOUBLE_QUOTES);
			}
			buffer.append(LINE_BREAK);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/**
	 * from ParticipantTransactionHistoryAction, jsp setup to pickup from
	 * request.
	 * 
	 * @param profileId
	 * @param request
	 * @throws SystemException
	 */
	public void populateParticipantDetails(String profileId,
			HttpServletRequest request) throws SystemException {
		int contractNumber = getBobContext(request).getCurrentContract()
				.getContractNumber();
		String productId = getBobContext(request).getCurrentContract()
				.getProductId();

		BDPrincipal principal = getUserProfile(request).getBDPrincipal();
		ParticipantAccountVO participantAccountVO = ParticipantServiceDelegate
				.getInstance().getParticipantAccount(principal, contractNumber,
						productId, profileId, null, false, false);
		ParticipantAccountDetailsVO participantDetailsVO = participantAccountVO
				.getParticipantAccountDetailsVO();

		request.setAttribute(BDConstants.BEAN_DETAILS, participantDetailsVO); // needed
																				// by
																				// jsp.
	}

	/**
	 * return empty string
	 * 
	 * @param String
	 *            value
	 * @return String value
	 */
	private String getNotNull(String value) {
		if (value == null) {
			return BDConstants.SPACE_SYMBOL;
		}
		return value;
	}

	/**
	 * don't want excel to think the , is the next field
	 * 
	 * @param String
	 *            field
	 * @return String field
	 */
	private String escapeField(String field) {
		if (field.indexOf(COMMA) != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append(BDConstants.DOUBLE_QUOTES).append(field)
					.append(BDConstants.DOUBLE_QUOTES);
			return newField.toString();
		} else {
			return field;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.bd.web.report.BDReportAction#doCommon(org.apache.struts.action.ActionMapping,
	 *      com.manulife.pension.platform.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public String doCommon(
			DeferralChangeDetailsReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> doCommon");

		String forward = super.doCommon( reportForm, request,
				response);

		

		// set values send from user selection on summary screen
		// setup on initial nav to the screen, print/download just uses value
		// set.
		if (request.getParameter(BDConstants.PROFILE_ID) != null) {
			reportForm.setProfileId(request.getParameter(BDConstants.PROFILE_ID));
			reportForm.setTransactionDate(request
					.getParameter(BDConstants.TRANSACTION_DATE));
		}

		TransactionDetailsDeferralReportData report = (TransactionDetailsDeferralReportData) request
				.getAttribute(BDConstants.REPORT_BEAN);
		reportForm.setReport(report);

		populateParticipantDetails(reportForm.getProfileId(), request);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}

		return forward;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseController#execute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	  @RequestMapping(value ="/transaction/deferralChangeDetailsReport/" ,method =  {RequestMethod.POST}) 
	public String execute(@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	

		/*
		 * // check if contract is discontinued if
		 * (getBobContext(request).getCurrentContract().isDiscontinued()) {
		 * return
		 * mapping.findForward(BDConstants.HOMEPAGE_FINDER_FORWARD_REDIRECT); }
		 */

		if ("POST".equalsIgnoreCase(request.getMethod())) {
			// do a refresh so that there's no problem using the back button
			ControllerForward forward = new ControllerForward("refresh", "/do"
					+ new UrlPathHelper().getPathWithinApplication(request) + "?task=" + getTask(request), true);
			if (logger.isDebugEnabled()) {
				logger.debug("forward = " + forward);
			}
			return forward.getPath();
		}

		return null;
	}

	/**
	 * @See BaseReportAction#prepareXMLFromReport()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Document prepareXMLFromReport(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws ParserConfigurationException {

		DeferralChangeDetailsReportForm form = (DeferralChangeDetailsReportForm) reportForm;
		int rowCount = 1;
		int maxRowsinPDF;

		PDFDocument doc = new PDFDocument();

		// Gets layout page for deferralChangeDetailsReport.jsp
		LayoutPage layoutPageBean = getLayoutPage(
				BDPdfConstants.DEFERRAL_CHANGE_DETAILS_PATH, request);

		Element rootElement = doc
				.createRootElement(BDPdfConstants.DEFERRAL_CHANGE_DETAILS);

		// Sets Logo, Page Name, Contract Details, Intro-1, Intro-2.
		setIntroXMLElements(layoutPageBean, doc, rootElement, request);

		// Sets Summary Info.
		setSummaryInfoXMLElements(doc, rootElement, layoutPageBean, request);

		String bodyHeader1 = ContentUtility.getContentAttributeText(
				layoutPageBean, BDContentConstants.BODY1_HEADER, null);
		PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);

		doc.appendTextNode(rootElement, BDPdfConstants.ASOF_DATE, form
				.getTransactionDateFormatted());

		int noOfRows = getNumberOfRowsInReport(report);
		if (noOfRows > 0) {
			// Transaction Details - start
			Element txnDetailsElement = doc
					.createElement(BDPdfConstants.TXN_DETAILS);
			Element txnDetailElement;
			Iterator iterator = report.getDetails().iterator();
			maxRowsinPDF = form.getCappedRowsInPDF();
			for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {
				txnDetailElement = doc.createElement(BDPdfConstants.TXN_DETAIL);
				TransactionDetailsDeferral theItem = (TransactionDetailsDeferral) iterator
						.next();
				// Sets main report.
				setReportDetailsXMLElements(doc, txnDetailElement, theItem,
						request);
				doc.appendElement(txnDetailsElement, txnDetailElement);
				rowCount++;
			}
			doc.appendElement(rootElement, txnDetailsElement);
			// Transaction Details - end
		}

		if (form.getPdfCapped()) {
			doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED,
					getPDFCappedText());
		}

		// Sets footer, footnotes and disclaimer
		setFooterXMLElements(layoutPageBean, doc, rootElement, request);

		return doc.getDocument();

	}

	/**
	 * This method sets summary information XML elements
	 * 
	 * @param doc
	 * @param rootElement
	 * @param layoutPageBean
	 * @param request
	 */
	private void setSummaryInfoXMLElements(PDFDocument doc,
			Element rootElement, LayoutPage layoutPageBean,
			HttpServletRequest request) {
		Element summaryInfoElement = doc
				.createElement(BDPdfConstants.SUMMARY_INFO);

		String subHeader = ContentUtility.getContentAttributeText(
				layoutPageBean, BDContentConstants.SUB_HEADER, null);
		PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);

		doc.appendTextNode(summaryInfoElement, BDPdfConstants.TXN_TYPE,
				DEFERRAL_UPDATE);

		ParticipantAccountDetailsVO detailsVO = (ParticipantAccountDetailsVO) request
				.getAttribute(BDConstants.BEAN_DETAILS);
		if (detailsVO != null) {
			doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_NAME,
					detailsVO.getFullName());

			String ssnString = SSNRender.format(detailsVO.getSsn(), null);
			doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_SSN,
					ssnString);
		}

		doc.appendElement(rootElement, summaryInfoElement);
	}

	/**
	 * This method sets report details XML elements
	 * 
	 * @param doc
	 * @param txnDetailElement
	 * @param theItem
	 * @param request
	 */
	private void setReportDetailsXMLElements(PDFDocument doc,
			Element txnDetailElement, TransactionDetailsDeferral theItem,
			HttpServletRequest request) {
		if (theItem != null) {
			if (theItem.genSecondLine()) {
				// Show web comments.
			    PdfHelper.convertIntoDOM(BDPdfConstants.WEB_COMMENTS, txnDetailElement, doc, theItem.getWebComments());
			}
			doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_DATE,
					theItem.getRequestedDate());
			doc.appendTextNode(txnDetailElement, BDPdfConstants.ITEM_CHANGED,
					theItem.getItemChanged());
			doc.appendTextNode(txnDetailElement, BDPdfConstants.VALUE_BEFORE,
					theItem.getValueBefore());
			doc.appendTextNode(txnDetailElement, BDPdfConstants.VALUE_UPDATED,
					theItem.getValueUpdated());
			doc
					.appendTextNode(txnDetailElement,
							BDPdfConstants.VALUE_REQUESTED, theItem
									.getValueRequested());
			doc.appendTextNode(txnDetailElement, BDPdfConstants.STATUS, theItem
					.getStatus());
			doc.appendTextNode(txnDetailElement, BDPdfConstants.CHANGED_BY,
					theItem.getChangedBy());
		}
	}

	/**
	 * @See BaseReportAction#getXSLTFileName()
	 */
	@Override
	public String getXSLTFileName() {
		return XSLT_FILE_KEY_NAME;
	}

	/**
     * @See BaseReportAction#getNumberOfRowsInReport()
     */
    @Override
    public Integer getNumberOfRowsInReport(ReportData report) {
        int noOfRows = 0;
        if(report.getDetails() != null) {
            noOfRows = report.getDetails().size();
        }
        return noOfRows;
    }
    
    /** This code has been changed and added  to 
	 /	Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
    @Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
	  @RequestMapping(value ="/transaction/deferralChangeDetailsReport/" ,method =  {RequestMethod.GET}) 
	  public String doDefault (@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			  throws IOException,ServletException, SystemException {
		  String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
		  if(bindingResult.hasErrors()){
			  String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			  if(errDirect!=null){
				  request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				  return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			  }
		  }
		   forward=super.doDefault( form, request, response);
		  return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	  }
	  @RequestMapping(value ="/transaction/deferralChangeDetailsReport/" ,params={"task=page"} , method =  {RequestMethod.GET}) 
	    public String doPage (@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    		throws IOException,ServletException, SystemException {
		  String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
		   if(bindingResult.hasErrors()){
			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			   if(errDirect!=null){
				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			   }
		   }
		    forward=super.doPage( form, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }
	   
	   @RequestMapping(value ="/transaction/deferralChangeDetailsReport/" ,params={"task=sort"}, method =  {RequestMethod.GET}) 
	   public String doSort (@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   throws IOException,ServletException, SystemException {
		   String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
		   if(bindingResult.hasErrors()){
			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			   if(errDirect!=null){
				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			   }
		   }
		    forward=super.doSort( form, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }
	   @RequestMapping(value ="/transaction/deferralChangeDetailsReport/", params={"task=download"},method =  {RequestMethod.GET}) 
	   public String doDownload (@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   throws IOException,ServletException, SystemException {
		   String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
		   if(bindingResult.hasErrors()){
			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			   if(errDirect!=null){
				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			   }
		   }
		    forward=super.doDownload( form, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }
	   
	   @RequestMapping(value ="/transaction/deferralChangeDetailsReport/", params={"task=downloadAll"}, method =  {RequestMethod.GET}) 
	   public String doDownloadAll (@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   throws IOException,ServletException, SystemException {
		   String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
		   if(bindingResult.hasErrors()){
			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			   if(errDirect!=null){
				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			   }
		   }
		    forward=super.doDownloadAll( form, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }
	   @RequestMapping(value = "transaction/deferralChangeDetailsReport/", params = {"task=printPDF"}, method = {RequestMethod.GET })
		public String doPrintPDF(@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm form,
				BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException, SystemException {
			 String forward = preExecute(form, request, response);
		    	if(StringUtils.isNotBlank(forward)) {
		    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
		    	}
			if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// default
				}
			}
			 forward = super.doPrintPDF(form, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
	   
}
