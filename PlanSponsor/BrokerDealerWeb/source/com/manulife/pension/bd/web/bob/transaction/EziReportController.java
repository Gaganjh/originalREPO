package com.manulife.pension.bd.web.bob.transaction;

import static com.manulife.util.render.SSNRender.MASK;

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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BOBReportController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.delegate.DelegateConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.participant.transaction.valueobject.EmployeeChangeHistoryACISettingsItem;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsACIReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * In support of transaction history Change EZincrease settings.
 * 
 * @author ambroar
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"eziReportForm"})

public class EziReportController extends BOBReportController {
	@ModelAttribute("eziReportForm")
	public EziReportForm populateForm() 
	{
		return new EziReportForm();
		}
	 public static HashMap<String,String>forwards = new HashMap<String,String>();
	 static{
		 forwards.put("input","/transaction/ezIncreaseSettingsChangeDetailsReport.jsp");
		 forwards.put("default","/transaction/ezIncreaseSettingsChangeDetailsReport.jsp");
		 forwards.put("filter","/transaction/ezIncreaseSettingsChangeDetailsReport.jsp");
		 forwards.put("page","/transaction/ezIncreaseSettingsChangeDetailsReport.jsp");
		 forwards.put("print","/transaction/ezIncreaseSettingsChangeDetailsReport.jsp");
		 forwards.put( "sort","/transaction/ezIncreaseSettingsChangeDetailsReport.jsp");
		 }

	private static final String DOWNLOAD_REPORT_NAME = "EZincreaseServiceChange-";

	private static final String TRANSACTION_HISTORY = "TH";

	private static final String JH_EZ_INCREASE_SUMMARY = "JH EZincrease service change summary";

	private static final String ITEM_CHANGED_HEADING = "Item Changed,Value Before,Value After,Changed By";

    private static final String XSLT_FILE_KEY_NAME = "EZincreaseChangeDetailsReport.XSLFile";

    private static final String TRANSACTION_TYPE = "JH EZincrease service change";

    private static final String ENABLE_PRINT = "enablePrint";
    
    private static final String AS_OF = "as of";

    public EziReportController() {
        super(EziReportController.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.platform.web.report.BaseReportAction#getReportId()
     */
    protected String getReportId() {
        return TransactionDetailsACIReportData.REPORT_ID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.platform.web.report.BaseReportAction#getReportName()
     */
    protected String getReportName() {
        return TransactionDetailsACIReportData.REPORT_NAME;
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
    protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
            HttpServletRequest request) {

        if (logger.isDebugEnabled())
            logger.debug("entry -> populateReportCriteria");

        EziReportForm dcForm = (EziReportForm) form;

        Contract currentContract = getBobContext(request).getCurrentContract();

        int contractNumber = currentContract.getContractNumber();

        criteria.addFilter(TransactionDetailsACIReportData.FILTER_CONTRACTID, new Integer(
                contractNumber));
        criteria
                .addFilter(TransactionDetailsACIReportData.FILTER_PROFILE_ID, dcForm.getProfileId());
        criteria.addFilter(TransactionDetailsACIReportData.FILTER_TARGET_DATE, dcForm
                .getTransactionDate());
        criteria.addFilter(TransactionDetailsACIReportData.FILTER_SCREEN, TRANSACTION_HISTORY);
        criteria.addFilter(TransactionDetailsACIReportData.FILTER_APPLICATION_ID,
                TransactionDetailsACIReportData.PSW_APPLICATION_ID);
        if (!getUserProfile(request).isInternalUser()) {
            criteria.addFilter(TransactionDetailsACIReportData.FILTER_EXTERNAL_USER_VIEW,
                    BDConstants.YES);
        }
        if (logger.isDebugEnabled())
            logger.debug("exit <-  populateReportCriteria");
    }

	/* 
	 * This method will return the File Name of the CSV file.
	 * The CSV file will be of the Format "Ezi Report
	 * EZincreaseServiceChange-<ContractNumber>-<LastName>-<mmddyyyy>.csv".
	 * 
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
						((EziReportForm) reportForm)
								.getTransactionDate(), null,
						RenderConstants.MEDIUM_YMD_DASHED,
						BDConstants.DATE_FORMAT_MMDDYYYY) + CSV_EXTENSION;
	}
    
    /*
     * Called by framework to gen excel (csv) data (non-Javadoc)
     * 
     * @see com.manulife.pension.platform.web.report.BaseReportAction#getDownloadData(com.manulife.pension.platform.web.report.BaseReportForm,
     *      com.manulife.pension.service.report.valueobject.ReportData,
     *      javax.servlet.http.HttpServletRequest)
     */
    protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getDownloadData");
        }

        StringBuffer buffer = new StringBuffer();

        EziReportForm aciForm = (EziReportForm) reportForm;
        ParticipantAccountDetailsVO detailsVO = (ParticipantAccountDetailsVO) request
                .getAttribute("details");
        Contract currentContract = getBobContext(request).getCurrentContract();
        
        buffer.append(BDConstants.CONTRACT).append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
				currentContract.getCompanyName()).append(LINE_BREAK);
        
        buffer.append(JH_EZ_INCREASE_SUMMARY).append(LINE_BREAK);
        buffer.append(BDConstants.TRANSACTION_TYPE).append(COMMA).append(
                BDConstants.EZINCREASE_SERVICE_CHANGE).append(LINE_BREAK);
        buffer.append(BDConstants.NAME).append(COMMA).append(
                escapeField(detailsVO.getLastName() + ", " + detailsVO.getFirstName())).append(
                LINE_BREAK);
        buffer.append(BDConstants.SSN).append(COMMA).append(
                SSNRender.format(detailsVO.getSsn(), MASK)).append(LINE_BREAK);
        buffer.append(LINE_BREAK);
        
        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.EZINCREASE_SETTINGS_CHANGE_PATH, request);
        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.BODY1_HEADER, null);
        

        buffer.append(ContentUtility.jsEsc(bodyHeader1) + AS_OF + COMMA + aciForm.getTransactionDateFormatted()).append(
                LINE_BREAK);
        buffer.append(ITEM_CHANGED_HEADING).append(LINE_BREAK);

        Iterator employeeChangeHistoryACISettingsItemIt = aciForm.getReport().getDetails()
                .iterator();

        while (employeeChangeHistoryACISettingsItemIt.hasNext()) {
            EmployeeChangeHistoryACISettingsItem employeeChangeHistoryACISettingsItem = (EmployeeChangeHistoryACISettingsItem) employeeChangeHistoryACISettingsItemIt
                    .next();

            buffer.append(employeeChangeHistoryACISettingsItem.getItemChanged()).append(COMMA);
            buffer.append(
                    escapeField(employeeChangeHistoryACISettingsItem.getValueBeforeForDownload()))
                    .append(COMMA);
            buffer.append(
                    escapeField(employeeChangeHistoryACISettingsItem.getValueAfterForDownload()))
                    .append(COMMA);
            buffer.append(employeeChangeHistoryACISettingsItem.getChangedBy()).append(LINE_BREAK);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getDownloadData");
        }

        return buffer.toString().getBytes();
    }

    /**
     * from ParticipantTransactionHistoryAction, jsp setup to pickup from request.
     * 
     * @param String profileId
     * @param HttpServletRequest request
     * @throws SystemException
     */
    public void populateParticipantDetails(String profileId, HttpServletRequest request)
            throws SystemException {

        ParticipantAccountDetailsVO participantDetailsVO = null;

        Contract currentContract = getBobContext(request).getCurrentContract();
        int contractNumber = currentContract.getContractNumber();
        String productId = getBobContext(request).getCurrentContract().getProductId();
        BDPrincipal bdPrincipal = getUserProfile(request).getBDPrincipal();

        ParticipantAccountVO participantAccountVO = ParticipantServiceDelegate.getInstance()
                .getParticipantAccount(bdPrincipal, contractNumber, productId, profileId, null,
                        false, false);
        participantDetailsVO = participantAccountVO.getParticipantAccountDetailsVO();

        request.setAttribute(BDConstants.BEAN_DETAILS, participantDetailsVO); // needed by jsp.
    }

    /**
     * don't want excel to think the , is the next field
     * 
     * @param String field
     * @return String field
     */
    private String escapeField(String field) {
        if (field.indexOf(COMMA) != -1) {
            StringBuffer newField = new StringBuffer();
            newField = newField.append(BDConstants.DOUBLE_QUOTES).append(field).append(
                    BDConstants.DOUBLE_QUOTES);
            return newField.toString();
        } else {
            return field;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.platform.web.report.BaseReportAction#getReportData(java.lang.String,
     *      com.manulife.pension.service.report.valueobject.ReportCriteria,
     *      javax.servlet.http.HttpServletRequest)
     */
    protected ReportData getReportData(String reportId, ReportCriteria reportCriteria,
            HttpServletRequest request) throws SystemException, ReportServiceException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getReportData");
        }

        ReportServiceDelegate service = ReportServiceDelegate.getInstance();
        ReportData bean = service.getReportData(reportCriteria);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getReportData");
        }

        return bean;
    }

    /*
     * Retrieve the Transaction Details ACI Report Data and set the value in form variable.
     * 
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.platform.web.report.BaseReportAction#doCommon(org.apache.struts.action.ActionMapping,
     *      com.manulife.pension.platform.web.report.BaseReportForm,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    
    public String doCommon(EziReportForm actionForm, HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {

        if (logger.isDebugEnabled())
            logger.debug("entry -> doCommon");

        String forward = super.doCommon( actionForm, request, response);

       

        // set values send from user selection on summary screen
        // setup on nav into page, then download link/print just picks up from form
        if (request.getParameter(BDConstants.PROFILE_ID) != null) {
        	actionForm.setProfileId(request.getParameter(BDConstants.PROFILE_ID));
        	actionForm.setTransactionDate(request.getParameter(BDConstants.TRANSACTION_DATE));
        }

        TransactionDetailsACIReportData report = (TransactionDetailsACIReportData) request
                .getAttribute(BDConstants.REPORT_BEAN);
        actionForm.setReport(report);

        request.setAttribute(ENABLE_PRINT, Boolean.FALSE);

        populateParticipantDetails(actionForm.getProfileId(), request);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doCommon");
        }

        return forward;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.platform.web.controller.BaseAction#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @RequestMapping(value ="/transaction/eziReport/" , method =  {RequestMethod.POST}) 
    public String execute(EziReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    
        // BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        // TODO:bd implementation required.
        // check for selected access

        // if (userProfile.isSelectedAccess()) {
        // return mapping.findForward(BDConstants.HOMEPAGE_FINDER_FORWARD_REDIRECT);
        // }
        //        
        // // check if contract is discontinued
        // if (userProfile.getCurrentContract().isDiscontinued()) {
        // return mapping.findForward(BDConstants.HOMEPAGE_FINDER_FORWARD_REDIRECT);
        // }
        //		
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            // do a refresh so that there's no problem using the back button
            ControllerForward forward = new ControllerForward("refresh", "/do" + new UrlPathHelper().getPathWithinApplication(request)
                    + "?task=" + getTask(request), true);
            if (logger.isDebugEnabled()) {
                logger.debug("forward = " + forward);
            }
            return forward.getPath();
        }

        return null;
    }
    @RequestMapping(value ="/transaction/eziReport/" , method =  {RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("eziReportForm") EziReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
    @RequestMapping(value ="/transaction/eziReport/" ,params={"task=page"} , method =  {RequestMethod.GET}) 
    public String doPage (@Valid @ModelAttribute("eziReportForm") EziReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
   
   @RequestMapping(value ="/transaction/eziReport/" ,params={"task=sort"}, method =  {RequestMethod.GET}) 
   public String doSort (@Valid @ModelAttribute("eziReportForm") EziReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
   @RequestMapping(value ="/transaction/eziReport/", params={"task=download"},method =  {RequestMethod.GET}) 
   public String doDownload (@Valid @ModelAttribute("eziReportForm") EziReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
   
   @RequestMapping(value ="/transaction/eziReport/", params={"task=downloadAll"}, method =  {RequestMethod.GET}) 
   public String doDownloadAll (@Valid @ModelAttribute("eziReportForm") EziReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
   @RequestMapping(value = "/transaction/eziReport/", params = {"task=printPDF"}, method = {RequestMethod.GET })
	public String doPrintPDF(@Valid @ModelAttribute("eziReportForm") EziReportForm form,
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
  
    
    /**
     * @See BaseReportAction#prepareXMLFromReport()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Document prepareXMLFromReport(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) throws ParserConfigurationException {

        EziReportForm form = (EziReportForm) reportForm;
        TransactionDetailsACIReportData data = (TransactionDetailsACIReportData) report;
        int rowCount = 1;
        int maxRowsinPDF;

        PDFDocument doc = new PDFDocument();

        // Gets layout page for deferralChangeDetailsReport.jsp
        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.EZINCREASE_SETTINGS_CHANGE_PATH, request);

        Element rootElement = doc.createRootElement(BDPdfConstants.EZINCREASE_SETTINGS_CHANGE);

        setIntroXMLElements(layoutPageBean, doc, rootElement, request);

        // Summary Info
        setSummaryInfoXMLElements(doc, rootElement, layoutPageBean, request);

        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);

        doc.appendTextNode(rootElement, BDPdfConstants.ASOF_DATE, form
                .getTransactionDateFormatted());

        int noOfRows = getNumberOfRowsInReport(report);
        if (noOfRows > 0) {
            // Transaction Details - start
            Element txnDetailsElement = doc.createElement(BDPdfConstants.TXN_DETAILS);
            Element txnDetailElement;
            Iterator iterator = data.getDetails().iterator();
            maxRowsinPDF = form.getCappedRowsInPDF();
            for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {
                txnDetailElement = doc.createElement(BDPdfConstants.TXN_DETAIL);
                EmployeeChangeHistoryACISettingsItem theItem = (EmployeeChangeHistoryACISettingsItem) iterator.next();
                // Sets main report.
                setReportDetailsXMLElements(doc, txnDetailElement, theItem, request);
                doc.appendElement(txnDetailsElement, txnDetailElement);
            }
            doc.appendElement(rootElement, txnDetailsElement);
            // Transaction Details - end
        }

        if (form.getPdfCapped()) {
            doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED, getPDFCappedText());
        }

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
    private void setSummaryInfoXMLElements(PDFDocument doc, Element rootElement,
            LayoutPage layoutPageBean, HttpServletRequest request) {
        Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);

        String subHeader = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.SUB_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);

        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TXN_TYPE, TRANSACTION_TYPE);

        ParticipantAccountDetailsVO detailsVO = (ParticipantAccountDetailsVO) request
                .getAttribute(DelegateConstants.ACCOUNT_VIEW_DETAILS);
        if (detailsVO != null) {
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_NAME, detailsVO.getFullName());
    
            String ssnString = SSNRender.format(detailsVO.getSsn(), null);
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_SSN, ssnString);
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
    private void setReportDetailsXMLElements(PDFDocument doc, Element txnDetailElement,
            EmployeeChangeHistoryACISettingsItem theItem, HttpServletRequest request) {
        if (theItem != null) {
            doc.appendTextNode(txnDetailElement, BDPdfConstants.ITEM_CHANGED, theItem.getItemChanged());
            doc.appendTextNode(txnDetailElement, BDPdfConstants.VALUE_BEFORE, theItem.getValueBefore());
            doc.appendTextNode(txnDetailElement, BDPdfConstants.VALUE_AFTER, theItem.getValueAfter());
            doc.appendTextNode(txnDetailElement, BDPdfConstants.CHANGED_BY, theItem.getChangedBy());
        } 
    }
    
    /**
     * @See BaseReportAction#getXSLTFileName()
     */
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
}
