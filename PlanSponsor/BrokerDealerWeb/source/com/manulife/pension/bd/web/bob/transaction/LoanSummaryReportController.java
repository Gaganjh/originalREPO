package com.manulife.pension.bd.web.bob.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BOBReportController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanSummaryItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanSummaryReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * Action class for Current Loan Summary
 * 
 * @author Ramkumar
 *
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"loanSummaryReportForm"})

public class LoanSummaryReportController extends BOBReportController {
	@ModelAttribute("loanSummaryReportForm") 
	public BaseReportForm populateForm() 
	{
		return new BaseReportForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/loanSummaryReport.jsp");
		forwards.put("default","/transaction/loanSummaryReport.jsp");
		forwards.put("filter","/transaction/loanSummaryReport.jsp");
		forwards.put("sort","/transaction/loanSummaryReport.jsp");
		forwards.put("page","/transaction/loanSummaryReport.jsp");
		forwards.put("print","/transaction/loanSummaryReport.jsp");
		}
 
	private static Logger logger = Logger.getLogger(LoanSummaryReportController.class);	

	private static final String DOWNLOAD_COLUMN_HEADING_LOAN_SUMMARY =
		"Name, SSN, Loan Number, Issue Date, Interest Rate(%), Original Loan Amount($), Outstanding Balance($), Last Payment($), Last Payment Date, Maturity Date, Alert";

	private static final String MSG_NO_LOANS = "There are currently no loans.";
    private static final String XSLT_FILE_KEY_NAME = "CurrentLoanSummaryReport.XSLFile";
    private static final String RATE_PATTERN = "###.##";
	
    /**
     * Constructor
     */
	public LoanSummaryReportController() {
		super(LoanSummaryReportController.class);
	}
	
	/**
     * @see BaseReportController#populateReportCriteria()
     */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		// Get the BobContext object and set the current contract
		BobContext bobContext = getBobContext(request);
		Contract currentContract = bobContext.getCurrentContract();

		criteria.addFilter(LoanSummaryReportData.FILTER_CONTRACT_NO,
				new Integer(currentContract.getContractNumber()));
	}
	

	/**
	 * @see BaseReportController#getReportId()
	 */
	protected String getReportId() {
		return LoanSummaryReportData.REPORT_ID;
	}

	/**
	 * @see BaseReportController#getReportName()
	 */
	protected String getReportName() {
		return LoanSummaryReportData.REPORT_NAME;
	}

	/**
     * @see BaseReportController#getDefaultSort()
     */
	protected String getDefaultSort() {
		return LoanSummaryItem.SORT_NAME;
	}

	/**
     * @see BaseReportController#getDefaultSortDirection()
     */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

    /**
     * The method is called when the task is doDownload. It returns the report in a byte[](csv)
     * 
     * @param reportForm The reportForm bean
     * @param report The ReportData object containing the contents for the report
     * @param request The current request object
     * 
     * @return byte[]
     */
    @Override
    @SuppressWarnings("unchecked")
    protected byte[] getDownloadData(
			BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) throws SystemException {

		
		if (logger.isDebugEnabled())
			logger.debug("entry -> getDownloadData");		
		
		LoanSummaryReportData reportData = (LoanSummaryReportData) report;
		StringBuffer buffer = new StringBuffer();

		// Get current contract number and contract name
		Contract currentContract = getBobContext(request).getCurrentContract();
        buffer.append("Contract").append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
				currentContract.getCompanyName()).append(LINE_BREAK);
        
        // Get As of date
		buffer.append("As of," + DateRender.format(reportData.getAsOfDate(),RenderConstants.MEDIUM_MDY_SLASHED));
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		
		// Summary Information
		buffer.append("Contract Current Loan Summary");
		buffer.append(LINE_BREAK);
		buffer.append("Balance of Outstanding Loans," + 
					"$" + reportData.getOutstandingBalance());
		buffer.append(LINE_BREAK);
		buffer.append("Number of Loans," + reportData.getNumLoans());
		buffer.append(LINE_BREAK);
		buffer.append("Number of Participants with Loans," + reportData.getNumParticipants());
		buffer.append(LINE_BREAK);
		buffer.append(",,,,,,");
		buffer.append(LINE_BREAK);
		
		// Loan Summary Details Data
		buffer.append("Current Loan Summary");
		buffer.append(LINE_BREAK);
		buffer.append(DOWNLOAD_COLUMN_HEADING_LOAN_SUMMARY);
		buffer.append(LINE_BREAK);
		if (reportData.getDetails() != null
				&& reportData.getDetails().size() > 0) {
			for (Iterator iterator = reportData.getDetails().iterator(); iterator.hasNext();) {
				LoanSummaryItem item = (LoanSummaryItem) iterator.next();
				buffer.append(QUOTE).append(item.getName()).append(QUOTE)
						.append(COMMA);
				buffer.append(SSNRender.format(item.getSsn(), BDConstants.DEFAULT_SSN_VALUE))
						.append(COMMA);
				buffer.append(item.getLoanNumber()).append(COMMA);
				buffer.append(item.getCreationDate()).append(COMMA);
				buffer.append(NumberRender.formatByPattern(item.getInterestRate(), null, RATE_PATTERN)).append(COMMA);
				buffer.append(item.getLoanAmt()).append(COMMA);
				if (item.getOutstandingBalance() != null) {
					buffer.append(item.getOutstandingBalance()).append(COMMA);
				} 
				else {
				    //Provide empty string under "Outstanding Balance($)" column if Outstanding Balance is null
					buffer.append(COMMA);
				}

				if (item.isNoRepayment()) {
				    //Provide "n/a" under "Last Payment($)" column if LastRepaymentAmt equals zero.
				    buffer.append(BDConstants.NOT_APPLICABLE).append(COMMA);
				} 
				else {
					buffer.append(item.getLastRepaymentAmt()).append(COMMA);
				}
				if (item.isNoRepayment()) {
				    //Provide "n/a" under "Last Payment Date" column if LastRepaymentAmt equals zero.
					buffer.append(BDConstants.NOT_APPLICABLE).append(COMMA);
				}
				else {
					buffer.append(DateRender.format(
							item.getLastRepaymentDate(),
							RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
				}
				buffer.append(DateRender.format(
                        item.getMaturityDate(),
                        RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
				String[] alerts = item.getAlerts();
				if (alerts.length > 0) {
					buffer.append(QUOTE);
					for (int i = 0; i < alerts.length; i++) {
						buffer.append(alerts[i]);
						if (i < alerts.length - 1)
							buffer.append(" ");
					}
					buffer.append(QUOTE);
				}
				buffer.append(LINE_BREAK);
			}
		} else {
			buffer.append(MSG_NO_LOANS);
		}
						
		if (logger.isDebugEnabled())
			logger.debug("exit <- getDownloadData");
		
		return buffer.toString().getBytes();
	}
	
	/**
     * This method is used to frame the file name used for the downloaded CSV.
     * 
     * @param BaseReportForm
     * @param HttpServletRequest
     * @return The file name used for the downloaded CSV.
     */

    protected String getFileName(BaseReportForm form, HttpServletRequest request) {
        
    	Contract currentContract = getBobContext(request).getCurrentContract();
    	String formattedTransactionDate = DateRender.formatByPattern(currentContract
                .getContractDates().getAsOfDate(), null, RenderConstants.MEDIUM_YMD_DASHED,
                RenderConstants.MEDIUM_MDY_SLASHED).replace(
                BDConstants.SLASH_SYMBOL, BDConstants.SPACE_SYMBOL);
    	
        String csvFileName=getReportName()
                        + BDConstants.HYPHON_SYMBOL
                        + currentContract.getContractNumber()
                        + BDConstants.HYPHON_SYMBOL
                        + formattedTransactionDate
                        + CSV_EXTENSION;
        
        return csvFileName;
        
    }
    
    /**
     * This method validates the Report data recieved and may show informational message to the
     * user.
     */
    protected void validateReportData(ReportData report, 
            BaseReportForm reportForm, HttpServletRequest request) {
        List<GenericException> infoMessages = new ArrayList<GenericException>();
        if (((LoanSummaryReportData)report).getDetails().isEmpty()) {
            infoMessages.add(new GenericExceptionWithContentType(CommonContentConstants.MISCELLANEOUS_LOAN_SUMMARY_NO_LOANS, 
                    ContentTypeManager.instance().MISCELLANEOUS,false));
        }
        if (!infoMessages.isEmpty()) {
            setMessagesInRequest(request, infoMessages);
        }
    }
    
    /**
     * @See BaseReportAction#prepareXMLFromReport()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Document prepareXMLFromReport(BaseReportForm reportForm,
           ReportData report, HttpServletRequest request)
           throws ParserConfigurationException {
        
        LoanSummaryReportData data = (LoanSummaryReportData) report;
        int rowCount = 1;
        int maxRowsinPDF;

        PDFDocument doc = new PDFDocument();

        // Gets layout page for loanSummaryReport.jsp
        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.CURRENT_LOAN_SUMMARY_PATH, request);
        
        Element rootElement = doc.createRootElement(BDPdfConstants.CURRENT_LOAN_SUMMARY);
        
        // Sets Logo, Page Name, Contract Details, Intro-1, Intro-2.
        setIntroXMLElements(layoutPageBean, doc, rootElement, request);
        
        // Sets Summary Info.
        setSummaryInfoXMLElements(doc, rootElement, data, layoutPageBean);
        
        String asOfDate = DateRender.formatByPattern(data.getAsOfDate(), null,  RenderConstants.MEDIUM_MDY_SLASHED);
        doc.appendTextNode(rootElement, BDPdfConstants.ASOF_DATE, asOfDate);
        
        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);
        
        // Gets number of rows present in report page.
        int noOfRows = getNumberOfRowsInReport(report);

        if (noOfRows > 0) {
            // Main Report - start
            Element reportDetailsElement = doc.createElement(BDPdfConstants.REPORT_DETAILS);
            if (report.getDetails() != null) {
                Iterator iterator = report.getDetails().iterator();
                // Gets number of rows to be shown in PDF.
                maxRowsinPDF = reportForm.getCappedRowsInPDF();
                for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {
                    Element reportDetailElement = doc.createElement(BDPdfConstants.REPORT_DETAIL);
                    LoanSummaryItem theItem = (LoanSummaryItem) iterator.next();
                    // Sets main report.
                    setReportDetailsXMLElements(doc, reportDetailElement, theItem);    
                    doc.appendElement(reportDetailsElement, reportDetailElement);
                    rowCount++;
                }
            }
            doc.appendElement(rootElement, reportDetailsElement);
            // Main Report - end
        }
        
        else {
            // Sets Information Message.
            setInfoMessagesXMLElements(doc, rootElement, request);
        }

        if (reportForm.getPdfCapped()) {
            // Sets PDF Capped message.
            doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED, getPDFCappedText());
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
     * @param data
     * @param layoutPageBean
     */
    private void setSummaryInfoXMLElements(PDFDocument doc, Element rootElement, LoanSummaryReportData data, 
                 LayoutPage layoutPageBean) {
        
        Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);

        String subHeader = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.SUB_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);
        
        if (data != null) {
            String noOfParticipants = NumberRender.formatByType(data.getNumParticipants(), null, RenderConstants.INTEGER_TYPE);
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.NUM_OF_PPT, noOfParticipants);
        
            String noOfLoans = NumberRender.formatByType(data.getNumLoans(), null, RenderConstants.INTEGER_TYPE);
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.NO_OF_LOANS, noOfLoans);
    
            String outstandingBalance = NumberRender.formatByType(data.getOutstandingBalance(), null, RenderConstants.CURRENCY_TYPE);
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.OUTSTANDING_BALANCE, outstandingBalance);
        }
        doc.appendElement(rootElement, summaryInfoElement);
    }
    
    /**
     * This method sets report details XML elements
     * 
     * @param doc
     * @param reportDetailElement
     * @param theItem
     */
    private void setReportDetailsXMLElements(PDFDocument doc, Element reportDetailElement, LoanSummaryItem theItem) {
        
        if (theItem != null) {
            doc.appendTextNode(reportDetailElement, BDPdfConstants.PPT_NAME, theItem.getName());
    
            String ssnString = SSNRender.format(theItem.getSsn(), BDConstants.DEFAULT_SSN_VALUE);
            doc.appendTextNode(reportDetailElement, BDPdfConstants.PPT_SSN, ssnString);
    
            doc.appendTextNode(reportDetailElement, BDPdfConstants.PPT_LOAN_NUMBER, String.valueOf(theItem.getLoanNumber()));
    
            if (theItem.getOutstandingBalance() != null) {
                //Set Outstanding Balance amount
                doc.appendTextNode(reportDetailElement, BDPdfConstants.OUTSTANDING_BALANCE, 
                                   NumberRender.formatByType(theItem.getOutstandingBalance(), null, RenderConstants.CURRENCY_TYPE, Boolean.FALSE));
            }
            doc.appendTextNode(reportDetailElement, BDPdfConstants.ISSUE_DATE, 
                    DateRender.formatByPattern(theItem.getCreationDate(), null,  RenderConstants.MEDIUM_MDY_SLASHED));
            if (!theItem.isNoRepayment()) {
                //Set LastRepaymentDate if LastRepaymentAmt not equals zero
                doc.appendTextNode(reportDetailElement, BDPdfConstants.LAST_REPAYMENT_DATE, 
                        DateRender.formatByPattern(theItem.getLastRepaymentDate(), null,  RenderConstants.MEDIUM_MDY_SLASHED));
            }
            doc.appendTextNode(reportDetailElement, BDPdfConstants.MATURITY_DATE, 
                    DateRender.formatByPattern(theItem.getMaturityDate(), null,  RenderConstants.MEDIUM_MDY_SLASHED));
            doc.appendTextNode(reportDetailElement, BDPdfConstants.INTEREST, 
                    NumberRender.formatByPattern(theItem.getInterestRate(), null, RATE_PATTERN));
            if (!theItem.isNoRepayment()) {
              //Set LastRepaymentAmt if LastRepaymentAmt not equals zero
                doc.appendTextNode(reportDetailElement, BDPdfConstants.LAST_REPAYMENT_AMT, 
                        NumberRender.formatByType(theItem.getLastRepaymentAmt(), null, RenderConstants.CURRENCY_TYPE, Boolean.FALSE));
            }
            doc.appendTextNode(reportDetailElement, BDPdfConstants.ORIGINAL_LOAN_AMT, 
                    NumberRender.formatByType(theItem.getLoanAmt(), null, RenderConstants.CURRENCY_TYPE, Boolean.FALSE));
            String[] alerts = theItem.getAlerts();
            if (alerts.length > 0) {
                for (int i = 0; i < alerts.length; i++) {
                    doc.appendTextNode(reportDetailElement, BDPdfConstants.ALERT, alerts[i]);
                }
            }
        }

    }
    
    
    @Override
    protected String doCommon(
    		BaseReportForm reportForm, HttpServletRequest request,
    		HttpServletResponse response) throws SystemException {
    	String forward = super.doCommon( reportForm, request, response);
    	LoanSummaryReportData data= (LoanSummaryReportData) request.getAttribute(BDConstants.REPORT_BEAN);
    	HttpSession session = request.getSession();
    	if(session != null){
    		request.getSession().removeAttribute(BDConstants.LOAN_SUMMARY_REPORT_BEAN);
    		request.getSession().setAttribute(BDConstants.LOAN_SUMMARY_REPORT_BEAN, data);
    	}
    	
    	
    	return  forward;
    }
    
    public String preExecute (BaseReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	String forward=super.preExecute( form, request, response);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
    @RequestMapping(value ="/transaction/loanSummaryReport/" , method =  {RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("loanSummaryReportForm") BaseReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws IOException,ServletException, SystemException {
    	 String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			LoanSummaryReportData data =(LoanSummaryReportData) request.getSession().getAttribute(BDConstants.LOAN_SUMMARY_REPORT_BEAN);
    			populateReportForm((BaseReportForm) form, request);
    			request.setAttribute(CommonConstants.REPORT_BEAN, data);
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	 forward=super.doDefault( form, request, response);
    	return StringUtils.contains(forward,'/')?forwards.get(forward):forwards.get(forward); 
    }
    @RequestMapping(value ="/transaction/loanSummaryReport/" ,params={"task=filter"}  , method =  {RequestMethod.GET}) 
    public String doFilter (@Valid @ModelAttribute("loanSummaryReportForm") BaseReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
    	 forward=super.doFilter( form, request, response);
    	return StringUtils.contains(forward,'/')?forwards.get(forward):forwards.get(forward); 
    }
    
    @RequestMapping(value ="/transaction/loanSummaryReport/", params={"task=page"}, method =  {RequestMethod.GET}) 
    public String doPage (@Valid @ModelAttribute("loanSummaryReportForm") BaseReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
    
    @RequestMapping(value ="/transaction/loanSummaryReport/", params={"task=sort"}  , method =  {RequestMethod.GET}) 
    public String doSort (@Valid @ModelAttribute("loanSummaryReportForm") BaseReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
    	return StringUtils.contains(forward,'/')?forwards.get(forward):forwards.get(forward); 
    }
    @RequestMapping(value ="/transaction/loanSummaryReport/", params={"task=download"}, method =  {RequestMethod.GET}) 
    public String doDownload (@Valid @ModelAttribute("loanSummaryReportForm") BaseReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
    @RequestMapping(value ="/transaction/loanSummaryReport/", params={"task=downloadAll"}  , method = {RequestMethod.GET}) 
    	    public String doDownloadAll (@Valid @ModelAttribute("loanSummaryReportForm") BaseReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

	@RequestMapping(value = "/transaction/loanSummaryReport/", params = {"task=printPDF"}, method = {RequestMethod.GET })
	public String doPrintPDF(@Valid @ModelAttribute("loanSummaryReportForm") BaseReportForm form,
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
		return StringUtils.contains(forward, '/') ? forwards.get(forward): forwards.get(forward);
	}

    /**
     * @See BaseReportAction#getXSLTFileName()
     */
    @Override
    public String getXSLTFileName() {
        return XSLT_FILE_KEY_NAME;
    }
    
    /** This code has been changed and added  to 
	 // Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	/*@SuppressWarnings("rawtypes")
	public Collection doValidate(ActionMapping mapping, Form form, HttpServletRequest request) {
		Collection penErrors = FrwValidation.doValidatePenTestAutoAction(form, mapping, request, CommonConstants.INPUT);
		if (penErrors != null && penErrors.size() > 0) {
			LoanSummaryReportData data =(LoanSummaryReportData) request.getSession().getAttribute(BDConstants.LOAN_SUMMARY_REPORT_BEAN);
			populateReportForm(mapping, (BaseReportForm) form, request);
			request.setAttribute(CommonConstants.REPORT_BEAN, data);
			return penErrors;
		}
		return super.doValidate(mapping, form, request);
	}*/
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}
}
