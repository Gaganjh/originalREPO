package com.manulife.pension.bd.web.bob.transaction;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
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
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.transaction.util.WithdrawalUtil;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData;
import com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawDataItem;
import com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This action class handles the cash account history page.
 * 
 * @author Charles Chan
 */
@Controller
@RequestMapping( value = "/bob")
@SessionAttributes({"systematicWithdrawReportForm"})

public class SystematicWithdrawReportController extends
AbstractTransactionReportController {
	private static Logger logger = Logger
			.getLogger(SystematicWithdrawReportController.class);
	private static final String DEFAULT_SORT_FIELD = SystematicWithdrawReportData.SORT_FIELD_NAME;
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;

	private static final String CSV_HEADER_SEARCH_BY_TYPE = "Search By Type";
	private static final String CSV_HEADER_SEARCH_CRITERIA = "Search By Value";

	private static final String[] CSV_HEADERS_ITEM_MULTIPLE_CONTRACTS = new String[] {
		"Participant Name", "Withdrawal Status", "Withdrawal Type",
		"Setup Date", "Calculation Method", "Frequency",
		"Last Payment Date", "Last Payment Amount($)", "Total Asset($)" };

	private static final String XSLT_FILE_KEY_NAME = "SysWDReport.XSLFile";
	protected static final String SYSTEMATIC_WITHDRAWALS = "Systematic Withdrawals";
	protected static final String DOWNLOAD_COLUMN_HEADING_TOTAL = "Name,SSN,Withdrawal Status,Withdrawal Type,"
			+ "Setup Date,Calculation Method,Frequency,Last Payment Date,Last Payment Amount($), Total Current Assets($)";
	private static final String NOT_PROVIDED = "-";
	private static final String AS_OF_DATE = "As of";
	private static final String FILTERS_USED = "Filters used";

	private static final String LAST_NAME = "last_name";
	private static final String WD_STATUS = "wd_status";
	private static final String WD_TYPE = "wd_type";
	private static final String SSN = "ssn";

	 public static final String LAST_NAME_LABEL = "Last Name";
	 public static final String WD_STATUS_LABEL = "Withdrawal Status";
	 public static final String WD_TYPE_LABEL = "Withdrawal Type";
	 public static final String SSN_LABEL = "SSN";
	private static final String CONTRACT = "Contract";
	
	@ModelAttribute("systematicWithdrawReportForm")
	public SystematicWithdrawReportForm populateForm()
	{
		return new SystematicWithdrawReportForm();
		}
	private static final RegularExpressionRule ssnRErule = new RegularExpressionRule(
			BDErrorCodes.STMT_SSN_INVALID, BDRuleConstants.SSN_RE);
	private static final RegularExpressionRule lastNameRErule = new RegularExpressionRule(
			BDErrorCodes.LAST_NAME_INVALID,
			BDRuleConstants.FIRST_NAME_LAST_NAME_RE);

	private static final List<String> SEARCH_BY_DROPDOWN = new ArrayList<String>();
	private static Map<String, String> WDStatusMap = new HashMap<String, String>();
	private static Map<String, String> WDTypeMap = new HashMap<String, String>();
	static {
		WDStatusMap.put("ACTIVE", "Active");
		WDStatusMap.put("COMPLETED", "Completed");
		WDStatusMap.put("All", "All");
		WDStatusMap.put("INACTIVE", "Inactive");
		WDStatusMap.put("PENDING", "Pending");
		// Populate Withdrawal status Map
		WDTypeMap.put("All", "All");
		WDTypeMap.put("RMD", "RMD");
		WDTypeMap.put("INSTALLMENT", "Installment");
	
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/systematicWithdrawalReport.jsp"); 
		forwards.put("default","/transaction/systematicWithdrawalReport.jsp");
		forwards.put("sort","/transaction/systematicWithdrawalReport.jsp");
		forwards.put("filter","/transaction/systematicWithdrawalReport.jsp");
		forwards.put("page","/transaction/systematicWithdrawalReport.jsp");
		forwards.put("download","/transaction/systematicWithdrawalReport.jsp");
		forwards.put("print","/participant/participantSummaryReport.jsp");
		forwards.put("printPDF","/participant/participantSummaryReport.jsp");
				}

	/*
	 * private static final String CSV_HEADER_FROM_DATE = "From date"; private
	 * static final String CSV_HEADER_TO_DATE = "To date"; private static final
	 * String CSV_HEADER_RUNNING_BALANCE = "Running balance($)"; private static
	 * final String CSV_HEADER_CURRENT_BALANCE = "Current balance"; private
	 * static final String CSV_HEADER_AS_OF_DATE = "As of"; private static final
	 * String CSV_HEADER_OPENING_BALANCE = "Opening balance this period";
	 * private static final String CSV_HEADER_CLOSING_BALANCE =
	 * "Closing balance this period"; private static final String
	 * CSV_HEADER_TOTAL_DEBITS = "Total debits this period"; private static
	 * final String CSV_HEADER_TOTAL_CREDITS = "Total credits this period";
	 */

	/**
	 * Constructor.
	 */
	public SystematicWithdrawReportController() {
		super(SystematicWithdrawReportController.class);
	}

	/**
	 * Validate the input action form. FROM date must be less than TO date.
	 * @throws SystemException 
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	/*
	 * protected Collection doValidate(ActionMapping mapping, Form form,
	 * HttpServletRequest request) { Collection errors =
	 * super.doValidate(mapping, form, request);
	 * 
	 * // if this is called using the default URL i.e. no parameters // do not
	 * validate if (request.getParameterNames().hasMoreElements()) {
	 * 
	 * SystematicWithdrawReportForm actionForm =
	 * (SystematicWithdrawReportForm) form;
	 * 
	 * if (actionForm.getSearchByType() != null &&
	 * actionForm.getSearchCriteria() != null) {
	 * 
	 * }
	 * 
	 * /* Resets the information for JSP to display.
	 * 
	 * /* if (errors.size() > 0) { /* Repopulates action form and request with
	 * default information.
	 */
	/*
	 * populateReportForm(mapping, actionForm, request);
	 * 
	 * //request.setAttribute("displayDates", "true");
	 * request.setAttribute("someflag", "true"); } } return errors; }
	 */
	@RequestMapping(value ="/transaction/systematicWithdrawalReport/" , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("systematicWithdrawReportForm") SystematicWithdrawReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
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
	@RequestMapping(value = "/transaction/systematicWithdrawalReport/", params = {"task=printPDF"}, method = {RequestMethod.GET })
	public String doPrintPDF(@Valid @ModelAttribute("systematicWithdrawReportForm") BaseReportForm form,
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
	
	
	@RequestMapping(value = "/transaction/systematicWithdrawalReport/", params = {"task=filter"}, method = {RequestMethod.GET})
	public String doFilter(@Valid @ModelAttribute("systematicWithdrawReportForm") BaseReportForm form,
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
		 forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/transaction/systematicWithdrawalReport/", params = {"task=download"}, method = {RequestMethod.GET})
	public String doDownload(@Valid @ModelAttribute("systematicWithdrawReportForm") BaseReportForm form,
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
		 forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@Override
	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		criteria.insertSort(sortField, sortDirection);
		if (TransactionHistoryReportData.SORT_FIELD_DATE.equals(sortField)) {
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_NUMBER,
					sortDirection);
		} else if (TransactionHistoryReportData.SORT_FIELD_AMOUNT
				.equals(sortField)) {
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_DATE,
					sortDirection);
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_NUMBER,
					sortDirection);
		} else if (TransactionHistoryReportData.SORT_FIELD_NUMBER
				.equals(sortField)) {
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_DATE,
					sortDirection);
		}
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 */
	
	@Autowired
	private SystematicWithdrawReportValidator systematicWithdrawReportValidator;
	
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	    binder.addValidators(systematicWithdrawReportValidator);
	}
	
	

	protected String getFileName(BaseReportForm form,
			HttpServletRequest request) {

		Contract currentContract = getBobContext(request).getCurrentContract();
		Calendar calendar = Calendar.getInstance();
		Date asOfDate = calendar.getTime();
		return getReportName()
				+ BDConstants.HYPHON_SYMBOL
				+ currentContract.getContractNumber()
				+ BDConstants.HYPHON_SYMBOL
				+ DateRender.format(asOfDate,
						RenderConstants.MEDIUM_MDY_SLASHED).replace(
								BDConstants.SLASH_SYMBOL, BDConstants.SPACE_SYMBOL)
								+ CSV_EXTENSION;
	}

	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request) {

		SystematicWithdrawReportForm form = (SystematicWithdrawReportForm) reportForm;
		StringBuilder buffer = new StringBuilder();
		FastDateFormat fdf = FastDateFormat.getInstance("MM/dd/yyyy");

		getData(request, form, buffer, fdf);

		return generateCsv(report, buffer);
	}

	/**
	 * @param request
	 * @param form
	 * @param buffer
	 * @param fdf
	 * @param asOfDate
	 */
	private void getData(HttpServletRequest request,
			SystematicWithdrawReportForm form, StringBuilder buffer,
			FastDateFormat fdf) {
		Contract currentContract = getBobContext(request).getCurrentContract();
		Date asOfDate = currentContract.getContractDates().getAsOfDate();
		buffer.append(BDConstants.CONTRACT).append(COMMA)
		.append(currentContract.getContractNumber()).append(COMMA)
		.append(getCsvString(currentContract.getCompanyName())).append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append(AS_OF_DATE).append(COMMA).append(fdf.format(asOfDate)).append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		if (BDConstants.NO.equals(form.getShowCustomizeFilter())) {
			if ((form.getQuickFilterBy() != null)){
					if(((form.getQuickFilterBy().equals(WD_STATUS))
							|| (form.getQuickFilterBy().equals(WD_TYPE)) 
							|| (form.getQuickFilterBy().equals(LAST_NAME))
							|| (form.getQuickFilterBy().equals(SSN)))
				){
				buffer.append(FILTERS_USED).append(LINE_BREAK);
			}
			String filter = form.getShowCustomizeFilter();
			form.setShowCustomizeFilter(filter);
			if (StringUtils.isNotBlank(form.getParticipantName())) {
				if (form.getQuickFilterBy().equals(LAST_NAME)) {
					buffer.append(LAST_NAME_LABEL).append(COMMA)
					.append(form.getParticipantName())
					.append(LINE_BREAK);
				}
				} else if (form.getQuickFilterBy().equals(WD_STATUS)) {
					buffer.append(WD_STATUS_LABEL).append(COMMA)
					.append(WDStatusMap.get(form.getWdStatus())).append(LINE_BREAK);
				} else if (form.getQuickFilterBy().equals(WD_TYPE)) {
					buffer.append(WD_TYPE_LABEL).append(COMMA)
					.append(WDTypeMap.get(form.getWdType())).append(LINE_BREAK);
				} else if (form.getQuickFilterBy().equals(SSN)) {
					buffer.append(SSN_LABEL)
					.append(COMMA)
					.append(form.getSsnOne() + form.getSsnTwo()
							+ form.getSsnThree()).append(LINE_BREAK);
				}
			buffer.append(LINE_BREAK);

			}
		}
		
		else if (BDConstants.YES.equals(form.getShowCustomizeFilter())) {			
			String filter = form.getShowCustomizeFilter();
			
			if(filter != null){
				form.setShowCustomizeFilter(filter);
				if(form.getCustomWDStatus() == null && form.getCustomWDType() == null){
				}else{
					buffer.append(FILTERS_USED).append(LINE_BREAK);
				}
			}
		
			if (StringUtils.isNotBlank(form.getCustomparticipantName())) {
				buffer.append(LAST_NAME_LABEL).append(COMMA)
				.append(form.getCustomparticipantName())
				.append(LINE_BREAK);
			}
			if (StringUtils.isNotBlank(form.getCustomWDStatus())) {
				buffer.append(WD_STATUS_LABEL).append(COMMA)
				.append(WDStatusMap.get(form.getCustomWDStatus())).append(LINE_BREAK);
			}
			if (StringUtils.isNotBlank(form.getCustomWDType())) {
				buffer.append(WD_TYPE_LABEL).append(COMMA)
				.append(WDTypeMap.get(form.getCustomWDType())).append(LINE_BREAK);
			}
			if (StringUtils.isNotBlank(form.getCustomSsnOne())) {
				buffer.append(SSN_LABEL)
				.append(COMMA)
				.append(form.getCustomSsnOne() + form.getCustomSsnTwo()
						+ form.getCustomSsnThree()).append(LINE_BREAK);
			}
			buffer.append(LINE_BREAK);
		}
	}
	// Quick filter - end

	/**
	 * @param report
	 * @param buffer
	 * @return
	 */
	private byte[] generateCsv(ReportData report, StringBuilder buffer) {
		buffer.append(SYSTEMATIC_WITHDRAWALS);
		buffer.append(LINE_BREAK);
		buffer.append(DOWNLOAD_COLUMN_HEADING_TOTAL);
		buffer.append(LINE_BREAK);

		Iterator<?> iterator = report.getDetails().iterator();
		boolean recordAvailable=false;
		while (iterator.hasNext()) {
			SystematicWithdrawDataItem theItem = (SystematicWithdrawDataItem) iterator
					.next();
			recordAvailable=true;
			buffer.append(
					getCsvString(theItem.getParticipant().getLastName().trim() + COMMA
							+ BDConstants.SINGLE_SPACE_SYMBOL
							+ theItem.getParticipant().getFirstName().trim())).append(
									COMMA);
			
			if(theItem.getParticipant().getSsn()!=null)
			{
			buffer.append(
					SSNRender.format(theItem.getParticipant().getSsn(), null))
					.append(COMMA);
			}
			

			buffer.append(getCsvString(theItem.getWdStatus())).append(COMMA);
			buffer.append(getCsvString(theItem.getWdType())).append(COMMA);
			buffer.append(
					DateRender.formatByPattern(theItem.getWdSetupDate(),
							NOT_PROVIDED, RenderConstants.MEDIUM_MDY_SLASHED))
							.append(COMMA);

			buffer.append(getCsvString(theItem.getCalculationMethod())).append(
					COMMA);
			buffer.append(getCsvString(theItem.getWdfrequency())).append(COMMA);

			buffer.append(
					DateRender.formatByPattern(theItem.getLastPayDate(),
							NOT_PROVIDED, RenderConstants.MEDIUM_MDY_SLASHED))
							.append(COMMA);

			buffer.append(
					getCsvString(NumberRender.formatByType(
							theItem.getLastPaymentAmount(),
							BDConstants.DEFAULT_VALUE_ZERO,
							RenderConstants.DECIMAL_TYPE))).append(COMMA);

			buffer.append(
					getCsvString(NumberRender.formatByType(
							theItem.getTotalAssets(),
							BDConstants.DEFAULT_VALUE_ZERO,
							RenderConstants.DECIMAL_TYPE))).append(COMMA);
			buffer.append(LINE_BREAK);

		}
		if(!recordAvailable) {
			buffer.append("There were no results for the current selections. Please try again.");
			return buffer.toString().getBytes();
		}
		return buffer.toString().getBytes();
	}
	/**
	 * @param reportForm
	 * @param report
	 * @param request
	 * @return
	 */
	 public Document prepareXMLFromReport(BaseReportForm reportForm,
			  ReportData report, HttpServletRequest request) throws
			  ParserConfigurationException {
			  
			  SystematicWithdrawReportForm form =
			 (SystematicWithdrawReportForm) reportForm;
			  SystematicWithdrawReportData data = (SystematicWithdrawReportData)
			  report;
			 
			  int rowCount = 1; 
			  int maxRowsinPDF;
			  
			  PDFDocument doc = new PDFDocument();
			  LayoutPage layoutPageBean =
			  getLayoutPage( BDPdfConstants.SYS_WITHDRAW_PATH, request);
			  
			  Element rootElement = doc.createRootElement(BDPdfConstants.SWD_SUMMARY);
			  
			 // setIntroXMLElements(layoutPageBean, doc, rootElement, request);
			  setSywWithdrawalIntroXMLElements(layoutPageBean, doc, rootElement, request);
			 			  
			  String
			  body1Text = ContentUtility.getContentAttributeText(layoutPageBean,
			  CommonContentConstants.BODY1, null);
			  
			  PdfHelper.convertIntoDOM(PdfConstants.BODY1_TEXT, rootElement, doc,
			  body1Text);
			  
			  
		//	  setSummaryInfoXMLElements(doc, rootElement, data, layoutPageBean, form);

				// Filters
				setFilterXMLElements(doc, rootElement, form);

				int noOfRows = getNumberOfRowsInReport(report);
				
				if (noOfRows > 0) {
					// Transaction Details - start
					Element wdDetailsElement = doc
							.createElement(BDPdfConstants.TXN_DETAILS);
					Element wdDetailElement;
					Iterator iterator = data.getDetails().iterator();
					maxRowsinPDF = form.getCappedRowsInPDF();
					for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {
						wdDetailElement = doc.createElement(BDPdfConstants.TXN_DETAIL);
						SystematicWithdrawDataItem theItem = (SystematicWithdrawDataItem) iterator
								.next();
						setReportDetailsXMLElements(doc, wdDetailElement, theItem);
						doc.appendElement(wdDetailsElement, wdDetailElement);
						rowCount++;
					}
					doc.appendElement(rootElement, wdDetailsElement);
					// Transaction Details - end
				}
				if (form.getPdfCapped()) {
					doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED,
							getPDFCappedText());
				}
				setFooterXMLElements(layoutPageBean, doc, rootElement, request);

				return doc.getDocument();
			 
	 }
	 
	 
	  private void setReportDetailsXMLElements(PDFDocument doc, Element txnDetailElement, SystematicWithdrawDataItem theItem) {
	        if (theItem != null) {
	            doc.appendTextNode(txnDetailElement, BDPdfConstants.LAST_NAME, theItem
	                    .getParticipant().getLastName());
	            doc.appendTextNode(txnDetailElement, BDPdfConstants.FIRST_NAME, theItem
	                    .getParticipant().getFirstName());
	            
	            if(theItem.getParticipant().getSsn()!=null) {
	            doc.appendTextNode(txnDetailElement, BDPdfConstants.PPT_SSN, SSNRender.format(
	                    theItem.getParticipant().getSsn(), null));
	           
	            doc.appendTextNode(txnDetailElement, BDPdfConstants.WD_STATUS, theItem
	                    .getWdStatus());
	            doc.appendTextNode(txnDetailElement, BDPdfConstants.WD_TYPE, theItem
	                    .getWdType());
	            doc.appendTextNode(txnDetailElement, BDPdfConstants.SET_DATE,  DateRender
	                    .formatByPattern(theItem.getWdSetupDate(), NOT_PROVIDED,
	                            RenderConstants.MEDIUM_MDY_SLASHED));
	            doc.appendTextNode(txnDetailElement, BDPdfConstants.CALC_METHOD, theItem
	                    .getCalculationMethod());
	            doc.appendTextNode(txnDetailElement, BDPdfConstants.WD_FREQ, theItem
	                    .getWdfrequency());
	            doc.appendTextNode(txnDetailElement, BDPdfConstants.LAST_PAY_DATE,  DateRender
	                    .formatByPattern(theItem.getLastPayDate(), NOT_PROVIDED,
	                            RenderConstants.MEDIUM_MDY_SLASHED));
	            String lastPayAmt = NumberRender.formatByPattern(theItem.getLastPaymentAmount(),
	                    BDConstants.DEFAULT_VALUE_ZERO, CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS);
	            String totalAssets = NumberRender.formatByPattern(theItem.getTotalAssets(),
	                    BDConstants.DEFAULT_VALUE_ZERO, CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS);
	            
	            doc.appendTextNode(txnDetailElement, BDPdfConstants.LAST_PAY_AMT, lastPayAmt);
	            doc.appendTextNode(txnDetailElement, BDPdfConstants.TOTAL_ASSETS, totalAssets);
	            
			}
		}
	}
	  
	  private void setSywWithdrawalIntroXMLElements(LayoutPage layoutPageBean, PDFDocument doc,
	            Element rootElement, HttpServletRequest request) {
		  Contract currentContract = BDSessionHelper.getBobContext(request).getCurrentContract();
		  setLogoAndPageName(layoutPageBean, doc, rootElement);

	        String contractName = currentContract.getCompanyName();
	        String contractNumber = String.valueOf(currentContract.getContractNumber());
	        doc.appendTextNode(rootElement, PdfConstants.CONTRACT_NAME, contractName);
	        doc.appendTextNode(rootElement, PdfConstants.CONTRACT_NUMBER, contractNumber);
	        Date asOfDate = currentContract.getContractDates().getAsOfDate();
	        FastDateFormat fdf = FastDateFormat.getInstance("MM/dd/yyyy");
			//Date asOfDate = new Date();
	        doc.appendTextNode(rootElement, PdfConstants.ASOF_DATE, fdf.format(asOfDate));
	        
	        
	        setIntro1Intro2XMLElements(layoutPageBean, doc, rootElement);

	    	    }
	 

	/**
	 * @see BaseReportController#doCommon(ActionMapping, BaseReportForm,
	 *      HttpServletRequest, HttpServletResponse)
	 */
	
	  protected String doCommon(
				BaseReportForm reportForm,
				HttpServletRequest request,
				HttpServletResponse response)
				throws SystemException {
		
		String forward = super.doCommon( reportForm, request,
				response);
		SystematicWithdrawReportForm form = (SystematicWithdrawReportForm) reportForm;

		Contract contract = getBobContext(request).getCurrentContract();		
		form.setAsOfDate(String.valueOf(contract.getContractDates().getAsOfDate()));
		  
			List wdStatusList = null;
	       wdStatusList = WithdrawalUtil.getInstance().getWdStatusList();
	       form.setStatusList(wdStatusList);
	       
	       List wdTypeList = null;
	       wdTypeList = WithdrawalUtil.getInstance().getWdTypeList();
	       form.setTypeList(wdTypeList);
	   
		
		/*
		 * determine if the results have any loan items in them if not, remove
		 * the loan-related types from the types drop down
		 */

		return forward;
	}

	/**
	 * This method is called to populate a report criteria from the report
	 * action form and the request. It is called right before getReportData is
	 * called.
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportController#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria,
	 *      com.manulife.pension.ps.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		SystematicWithdrawReportForm sysWithdrawForms = (SystematicWithdrawReportForm) form;

		// get the user profile object and set the current contract to null
		BobContext bobContext = getBobContext(request);
		Contract currentContract = bobContext.getCurrentContract();

		String participantSSN = "";
		
		criteria.addFilter("contractNumber", Integer.toString(currentContract
				.getContractNumber()));

		if (!StringUtils.isEmpty(sysWithdrawForms.getParticipantName())) {
			criteria.addFilter(SystematicWithdrawReportData.FILTER_NAME,
					sysWithdrawForms.getParticipantName());
		}

		if (!StringUtils.isEmpty(sysWithdrawForms
				.getCustomparticipantName())) {
			criteria.addFilter(SystematicWithdrawReportData.FILTER_NAME,
					sysWithdrawForms.getCustomparticipantName());
		}

		if (!StringUtils.isEmpty(sysWithdrawForms.getCustomWDType())) {
			criteria.addFilter(SystematicWithdrawReportData.FILTER_WDTYPE,
					WDTypeMap.get(sysWithdrawForms.getCustomWDType()));
		}

		if (!StringUtils.isEmpty(sysWithdrawForms.getCustomWDStatus())) {
			criteria.addFilter(SystematicWithdrawReportData.FILTER_STATUS,
					WDStatusMap.get(sysWithdrawForms.getCustomWDStatus()));
		}
		if (!StringUtils.isEmpty(sysWithdrawForms.getCustomSsnOne())
				&& !StringUtils.isEmpty(sysWithdrawForms
						.getCustomSsnTwo())
						&& !StringUtils.isEmpty(sysWithdrawForms
								.getCustomSsnThree())) {
			participantSSN = sysWithdrawForms.getCustomSsnOne()
					+ (sysWithdrawForms.getCustomSsnTwo())
					+ sysWithdrawForms.getCustomSsnThree();

			if (!StringUtils.isEmpty(participantSSN))
				criteria.addFilter(SystematicWithdrawReportData.FILTER_SSN,
						participantSSN);
		}

		/*
		 * if ((sysWithdrawForm.getParticipantSSN())!=null) {
		 * criteria.addFilter(SystematicWithdrawReportData.FILTER_SSN,
		 * sysWithdrawForm.getSsn()); } if
		 * (!StringUtils.isEmpty(sysWithdrawForm.getWdStatus())) {
		 * criteria.addFilter(SystematicWithdrawReportData.FILTER_STATUS,
		 * sysWithdrawForm.getWdStatus()); } if
		 * (!StringUtils.isEmpty(sysWithdrawForm.getWdType())) {
		 * criteria.addFilter(SystematicWithdrawReportData.FILTER_WDTYPE,
		 * sysWithdrawForm.getWdType()); }
		 * 
		 * criteria.addFilter(SystematicWithdrawReportData.FILTER_CONTRACT_NUMBER
		 * , contractNumber);
		 * 
		 * 
		 * criteria.addFilter(SystematicWithdrawReportData.FILTER_CLIENT_ID,
		 * clientId);
		 * criteria.addFilter(SystematicWithdrawReportData.FILTER_SEARCH_BY
		 * ,sysWithdrawForm.getSearchByType() );
		 * 
		 * //
		 * criteria.addFilter(SystematicWithdrawReportData.FILTER_SEARCH_DATA,
		 * sysWithdrawForm.getSearchCriteria());
		 */

		String task = request.getParameter(TASK_KEY);
		if (task == null) {
			task = DEFAULT_TASK;
		}
		criteria.addFilter("TASK", task);
		criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);
		// criteria.addFilter(SystematicWithdrawReportData.FILTER_PAGE,"FRW");
	}

	/**
	 * This method populates a default form when the report page is first
	 * brought up. This method is called before populateReportCriteria() to
	 * allow default sort and other criteria to be set properly.
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportController#populateReportForm(ActionMapping,
	 *      com.manulife.pension.ps.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm(reportForm, request);
		String task = getTask(request);
		if (FILTER_TASK.equals(task)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
		/*
		 * Obtain the contract dates object.
		 * 
		 * ContractDatesVO contractDates = getContractDatesVO(request); List
		 * fromDates = new ArrayList(); List toDates = new ArrayList();
		 * 
		 * /* Using the contract dates, generate the date range (from dates and
		 * to dates).
		 * 
		 * ContractDateHelper.populateFromToDates(getUserProfile(request).
		 * getCurrentContract(), fromDates, toDates);
		 * 
		 * /* Stores the from dates and to dates into the request.
		 * 
		 * request.setAttribute(Constants.CASH_ACCOUNT_FROM_DATES, fromDates);
		 * request.setAttribute(Constants.CASH_ACCOUNT_TO_DATES, toDates);
		 * 
		 * CashAccountReportForm form = (CashAccountReportForm)
		 * reportForm; HttpSession session =request.getSession(false); if
		 * (form.getFromDate() == null || form.getFromDate().length() == 0) {
		 * 
		 * String fromDate = StringUtils.trimToEmpty((String)
		 * session.getAttribute(Constants.CASH_ACCOUNT_FROM_DATE)); if
		 * (!StringUtils.isEmpty(fromDate)) { form.setFromDate(fromDate); }else{
		 * form.setFromDate(String .valueOf(((Date)
		 * fromDates.iterator().next()).getTime())); } }
		 * 
		 * if (form.getToDate() == null || form.getToDate().length() == 0) {
		 * String toDate = StringUtils.trimToEmpty((String)
		 * session.getAttribute(Constants.CASH_ACCOUNT_TO_DATE)); if
		 * (!StringUtils.isEmpty(toDate)) { form.setToDate(toDate); }else{
		 * form.setToDate(String.valueOf(((Date) toDates.iterator().next())
		 * .getTime())); } }
		 * session.setAttribute(Constants.CASH_ACCOUNT_FROM_DATE,
		 * form.getFromDate());
		 * session.setAttribute(Constants.CASH_ACCOUNT_TO_DATE,
		 * form.getToDate());
		 */
	}

	 private void setFilterXMLElements(PDFDocument doc, Element rootElement, SystematicWithdrawReportForm form) {
	        Element filterElement = doc.createElement(BDPdfConstants.FILTERS);
	        // Quick filter - start
	        if (BDConstants.NO.equals(form.getShowCustomizeFilter())) { 
	        if ((form.getQuickFilterBy() != null)){
					if(((form.getQuickFilterBy().equals(WD_STATUS))
							|| (form.getQuickFilterBy().equals(WD_TYPE)) 
							|| (form.getQuickFilterBy().equals(LAST_NAME))
							|| (form.getQuickFilterBy().equals(SSN)))
				){
						 doc.appendTextNode(filterElement, BDPdfConstants.FILTERS_USED, form.getShowCustomizeFilter());
			}
	        if(StringUtils.isNotBlank(form.getQuickFilterBy())) {
	            if (form.getQuickFilterBy().equals(LAST_NAME)) {
	                doc.appendTextNode(filterElement, BDPdfConstants.LAST_NAME_LABEL, form
	                        .getParticipantName());
	            }
	            else if (form.getQuickFilterBy().equals(WD_STATUS)) {
	                doc.appendTextNode(filterElement, BDPdfConstants.WD_STATUS_LABEL, (WDStatusMap.get(form.getWdStatus())));
	            }
	            else if (form.getQuickFilterBy().equals(WD_TYPE)) {
	                doc.appendTextNode(filterElement, BDPdfConstants.WD_TYPE_LABEL, (WDTypeMap.get(form.getWdType())));
	            }
	            
	            else if (form.getQuickFilterBy().equals(SSN)) {
	                doc.appendTextNode(filterElement, BDPdfConstants.SSN_LABEL, (form.getSsnOne() + form.getSsnTwo()
							+ form.getSsnThree()));
					}

				}
			}
		}
	      
	        // Quick filter - end
	        
	        // Custom filter - start
	        else if (BDConstants.YES.equals(form.getShowCustomizeFilter())) {
	        	if(form.getCustomWDStatus() == null && form.getCustomWDType() == null){
				}else{
					 doc.appendTextNode(filterElement, BDPdfConstants.FILTERS_USED, form.getShowCustomizeFilter());
				}
	        	 
	            if (StringUtils.isNotBlank(form.getCustomparticipantName())) {
	                doc.appendTextNode(filterElement, BDPdfConstants.LAST_NAME_LABEL, form
	                        .getCustomparticipantName());
	            }
	            if (StringUtils.isNotBlank(form.getCustomWDStatus())) {
	                doc.appendTextNode(filterElement, BDPdfConstants.WD_STATUS_LABEL, 
	                		WDStatusMap.get(form.getCustomWDStatus()));
	            }
	            if (StringUtils.isNotBlank(form.getCustomWDType())) {
	                doc.appendTextNode(filterElement, BDPdfConstants.WD_TYPE_LABEL, (WDTypeMap.get(form
	                        .getCustomWDType())));
	            }
	            if (StringUtils.isNotBlank(form.getCustomSsnOne())) {
	                doc.appendTextNode(filterElement, BDPdfConstants.SSN_LABEL, form.getCustomSsnOne() + form.getCustomSsnTwo()
							+ form.getCustomSsnThree());
	            }
	        }
	            
	        // Custom filter - end
	        doc.appendElement(rootElement, filterElement);
	    }
					
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	protected String getReportId() {
		return SystematicWithdrawReportData.REPORT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	protected String getReportName() {
		return SystematicWithdrawReportData.FRW_REPORT_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}
	@Override
	public String getXSLTFileName() {
		return XSLT_FILE_KEY_NAME;
	}

	/**
	 * Writes out a CSV file using the given form, report data, and request.
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportController#populateDownloadData(java.io.PrintWriter,
	 *      com.manulife.pension.service.report.valueobject.ReportData)
	 */

	  private static String escapeCommaForCsv(String value){
	        return StringUtils.isNotBlank(value) ? "\""+value+"\"" : StringUtils.EMPTY;  
	      }
}
