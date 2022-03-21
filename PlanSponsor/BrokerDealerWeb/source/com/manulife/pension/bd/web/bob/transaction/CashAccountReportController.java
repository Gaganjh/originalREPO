package com.manulife.pension.bd.web.bob.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentType;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

/**
 * This action class handles the cash account history page.
 * 
 * @author harlomte
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"cashAccountReportForm"})

public class CashAccountReportController extends AbstractTransactionReportController {
	@ModelAttribute("cashAccountReportForm") 
	public CashAccountReportForm populateForm() 
	{
		return new CashAccountReportForm();
		
	}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/cashAccountReport.jsp");
		forwards.put("default","/transaction/cashAccountReport.jsp");
		forwards.put( "sort","/transaction/cashAccountReport.jsp");
		forwards.put("filter","/transaction/cashAccountReport.jsp");
		forwards.put("page","/transaction/cashAccountReport.jsp");
		forwards.put(" print","/transaction/cashAccountReport.jsp");
	}

	
	private static Logger logger = Logger
			.getLogger(CashAccountReportController.class);
	private static final String DEFAULT_SORT_FIELD = "transactionDate";
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;
	private static final String[] CSV_HEADERS_ITEM_MULTIPLE_CONTRACTS = new String[]{
			"Transaction Date", "Type Line 1", "Type Line 2",
			"Description Line 1", "Description Line 2", 
			"Transaction Number", "Debits ($)",
            "Credits ($)" };

	private static final String CSV_HEADER_RUNNING_BALANCE = "Running Balance($)";
	private static final String CSV_HEADER_FROM_DATE = "From date";
	private static final String CSV_HEADER_TO_DATE = "To date";
	private static final String CSV_HEADER_CURRENT_BALANCE = "Current balance";
	private static final String CSV_HEADER_AS_OF_DATE = "As of";
	private static final String CSV_HEADER_OPENING_BALANCE = "Opening balance this period";
	private static final String CSV_HEADER_CLOSING_BALANCE = "Closing balance this period";
	private static final String CSV_HEADER_TOTAL_DEBITS = "Total debits this period";
	private static final String CSV_HEADER_TOTAL_CREDITS = "Total credits this period";
	
	private static final String XSLT_FILE_KEY_NAME = "ContractCashAccountReport.XSLFile";
	
	/**
	 * Constructor.
	 */
	public CashAccountReportController() {
		super(CashAccountReportController.class);
	}
	@RequestMapping(value ="/transaction/cashAccountReport/" , method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("cashAccountReportForm") CashAccountReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(form, request, response);
    	if(StringUtils.isNotBlank(forward)) {
    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
    	}
		if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			populateReportForm( form, request);
				request.setAttribute("displayDates", "true");
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
		
    	 forward=super.doDefault( form, request, response);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
	 @RequestMapping(value ="/transaction/cashAccountReport/" ,params={"task=page"} , method =  {RequestMethod.GET}) 
	    public String doPage (@Valid @ModelAttribute("cashAccountReportForm") CashAccountReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    		throws IOException,ServletException, SystemException {
		 String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
		   if(bindingResult.hasErrors()){
			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			   if(errDirect!=null){
				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				   populateReportForm( form, request);
					request.setAttribute("displayDates", "true");
				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			   }
		   }
		    forward=super.doPage( form, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }
	   
	   @RequestMapping(value ="/transaction/cashAccountReport/" ,params={"task=sort"}, method =  {RequestMethod.GET}) 
	   public String doSort (@Valid @ModelAttribute("cashAccountReportForm") CashAccountReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   throws IOException,ServletException, SystemException {
		   String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
		   if(bindingResult.hasErrors()){
			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			   if(errDirect!=null){
				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				   populateReportForm( form, request);
					request.setAttribute("displayDates", "true");
				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			   }
		   }
		  
		    forward=super.doSort( form, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }
	   @RequestMapping(value ="/transaction/cashAccountReport/", params={"task=download"},method =  {RequestMethod.GET}) 
	   public String doDownload (@Valid @ModelAttribute("cashAccountReportForm") CashAccountReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   throws IOException,ServletException, SystemException {
		  
		   String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
		   if(bindingResult.hasErrors()){
			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			   if(errDirect!=null){
				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				   populateReportForm( form, request);
					request.setAttribute("displayDates", "true");
				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			   }
		   }
		   forward=super.doDownload( form, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }
	   
	   @RequestMapping(value ="/transaction/cashAccountReport/", params={"task=filter"},method =  {RequestMethod.GET}) 
	   public String doFilter(@Valid @ModelAttribute("cashAccountReportForm") CashAccountReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   throws IOException,ServletException, SystemException {
		  
		   String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
		   if(bindingResult.hasErrors()){
			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			   if(errDirect!=null){
				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				   populateReportForm( form, request);
					request.setAttribute("displayDates", "true");
				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			   }
		   }
		   forward=super.doFilter( form, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }
	   
	   @RequestMapping(value ="/transaction/cashAccountReport/", params={"task=downloadAll"}, method =  {RequestMethod.GET}) 
	   public String doDownloadAll (@Valid @ModelAttribute("cashAccountReportForm") CashAccountReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   throws IOException,ServletException, SystemException {
		   String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
		   
		   if(bindingResult.hasErrors()){
			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			   if(errDirect!=null){
				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				   populateReportForm( form, request);
					request.setAttribute("displayDates", "true");
				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			   }
		   }
		   forward=super.doDownloadAll( form, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }
	   
	   @RequestMapping(value = "/transaction/cashAccountReport/", params = {"task=printPDF"}, method = {RequestMethod.GET })
		public String doPrintPDF(@Valid @ModelAttribute("cashAccountReportForm") CashAccountReportForm form,
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
					populateReportForm( form, request);
					request.setAttribute("displayDates", "true");
					return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// default
				}
			}
			 forward = super.doPrintPDF(form, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}



	/**
	 * Validate the input action form. FROM date must be less than TO date.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	    binder.addValidators(cashAccountReportValidator);
	}

@Autowired
private CashAccountReportValidator cashAccountReportValidator;

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

		CashAccountReportForm cashAccountReportForm = (CashAccountReportForm) form;

		// get the bobContext object and set the current contract to null
		BobContext bobContext = getBobContext(request);
		Contract currentContract = bobContext.getCurrentContract();

		Integer contractNumber = new Integer(currentContract
				.getContractNumber());
		String clientId = currentContract.getClientId();

		criteria.addFilter(CashAccountReportData.FILTER_CONTRACT_NUMBER,
				contractNumber);

		criteria.addFilter(CashAccountReportData.FILTER_CLIENT_ID, clientId);

		criteria.addFilter(CashAccountReportData.FILTER_FROM_DATE, new Date(
				Long.valueOf(cashAccountReportForm.getFromDate())
						.longValue()));

		criteria.addFilter(CashAccountReportData.FILTER_TO_DATE, new Date(Long
				.valueOf(cashAccountReportForm.getToDate()).longValue()));

		if (currentContract.isDefinedBenefitContract()) {
			criteria.addFilter(TransactionHistoryReportData.CONTRACT_TYPE_DB, Boolean.TRUE);
		}		
		
		/*
		 * No limit to page size. If the # transaction exceeds 500, the backend
		 * will set the hasTooManyTransaction flag in the report data.
		 */
		criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);
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
	@SuppressWarnings("unchecked")
    protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm( reportForm, request);

		/*
		 * Obtain the contract dates object.
		 */
		List fromDates = new ArrayList();
		List toDates = new ArrayList();

		/*
		 * Using the contract dates, generate the date range (from dates and to
		 * dates).
		 */
		ContractDateHelper.populateFromToDates(getBobContext(request).getCurrentContract(), fromDates, toDates);

		/*
		 * Stores the from dates and to dates into the request.
		 */
		request.setAttribute(BDConstants.CASH_ACCOUNT_FROM_DATES, fromDates);
		request.setAttribute(BDConstants.CASH_ACCOUNT_TO_DATES, toDates);

		CashAccountReportForm form = (CashAccountReportForm) reportForm;

		if (form.getFromDate() == null || form.getFromDate().length() == 0) {
			form.setFromDate(String
					.valueOf(((Date) fromDates.iterator().next()).getTime()));
		}

		if (form.getToDate() == null || form.getToDate().length() == 0) {
			form.setToDate(String.valueOf(((Date) toDates.iterator().next())
					.getTime()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	protected String getReportId() {
		return CashAccountReportData.REPORT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	protected String getReportName() {
		return CashAccountReportData.CSV_REPORT_NAME;
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
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	/**
	 * Writes out a CSV file using the given form, report data, and request.
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportController#populateDownloadData(java.io.PrintWriter,
	 *      com.manulife.pension.service.report.valueobject.ReportData)
	 */
	@SuppressWarnings("unchecked")
    protected byte[] getDownloadData(
			BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) throws SystemException {

        if (logger.isDebugEnabled())
            logger.debug("entry -> populateDownloadData");

		CashAccountReportData data = (CashAccountReportData) report;
		StringBuffer buffer = new StringBuffer();
		
		if (data == null) {
            return buffer.toString().getBytes();
        }

		Contract currentContract = getBobContext(request).getCurrentContract();
        buffer.append("Contract").append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
				currentContract.getCompanyName()).append(LINE_BREAK);

		try {
			/*
			 * Obtain the from date and to date.
			 */
			Date fromDate = (Date) report.getReportCriteria().getFilterValue(
					CashAccountReportData.FILTER_FROM_DATE);
			Date toDate = (Date) report.getReportCriteria().getFilterValue(
					CashAccountReportData.FILTER_TO_DATE);

			buffer.append(CSV_HEADER_FROM_DATE).append(COMMA)
					.append(
							DateRender.format(fromDate,
									RenderConstants.EXTRA_LONG_MDY)).append(
							COMMA).append(CSV_HEADER_TO_DATE).append(COMMA)
					.append(
							DateRender.format(toDate,
									RenderConstants.EXTRA_LONG_MDY)).append(
							LINE_BREAK);
			buffer.append(LINE_BREAK);

			/*
			 * Current Balance & As Of Date
			 */
			buffer.append(CSV_HEADER_CURRENT_BALANCE).append(COMMA).append(
					getCsvString(data.getCurrentBalance())).append(COMMA)
					.append(CSV_HEADER_AS_OF_DATE).append(COMMA).append(
							DateRender.format(getContractDatesVO(request)
									.getAsOfDate(),
									RenderConstants.EXTRA_LONG_MDY)).append(
							LINE_BREAK);
			buffer.append(LINE_BREAK);

			/*
			 * Opening Balance (Hide when Multiple Contracts is true)
			 */
			if (!data.getHasMultipleContracts()) {
				buffer.append(CSV_HEADER_OPENING_BALANCE).append(COMMA).append(
						getCsvString(data.getOpeningBalanceForPeriod()))
						.append(COMMA);
			}

			/*
			 * Total Debits
			 */
			buffer.append(CSV_HEADER_TOTAL_DEBITS).append(COMMA).append(
					getCsvString(data.getTotalDebitsForPeriod())).append(
					LINE_BREAK);

			/*
			 * Closing Balance (Hide when Multiple Contracts is true)
			 */
			if (!data.getHasMultipleContracts()) {
				buffer.append(CSV_HEADER_CLOSING_BALANCE).append(COMMA).append(
						getCsvString(data.getClosingBalanceForPeriod()))
						.append(COMMA);
			}

			/*
			 * Total Credits
			 */
			buffer.append(CSV_HEADER_TOTAL_CREDITS).append(COMMA).append(
					getCsvString(data.getTotalCreditsForPeriod())).append(
					LINE_BREAK);
			buffer.append(LINE_BREAK);

			/*
			 * If there are no transaction...
			 */
			if (data.getDetails() != null) {
                if (data.getDetails().size() == 0) {
                    Content message = null;
                    if (data.getHasTooManyItems()) {
                        message = ContentCacheManager
                                .getInstance()
                                .getContentByName(
                                        String
                                                .valueOf(BDContentConstants.MESSAGE_TOO_MANY_TRANSACTIONS_FOR_DATE_SELECTED),
                                        ContentTypeManager.instance().MESSAGE);
                    } else {
                        message = ContentCacheManager
                                .getInstance()
                                .getContentByName(
                                        String
                                                .valueOf(BDContentConstants.MESSAGE_NO_TRANSACTION_FOR_DATE_SELECTED),
                                        ContentTypeManager.instance().MESSAGE);
                    }
                    buffer.append(ContentUtility.getContentAttribute(message, "text")).append(
                            LINE_BREAK);
                    for (int i = 0; i < CSV_HEADERS_ITEM_MULTIPLE_CONTRACTS.length; i++) {
                        buffer.append(CSV_HEADERS_ITEM_MULTIPLE_CONTRACTS[i]);
                        if (i != CSV_HEADERS_ITEM_MULTIPLE_CONTRACTS.length - 1) {
                            buffer.append(COMMA);
                        }
                    }
                    buffer.append(COMMA).append(CSV_HEADER_RUNNING_BALANCE);
                } else {

                    /*
                     * Line Item Header (changes based on multiple contracts)
                     */
                    for (int i = 0; i < CSV_HEADERS_ITEM_MULTIPLE_CONTRACTS.length; i++) {
                        buffer.append(CSV_HEADERS_ITEM_MULTIPLE_CONTRACTS[i]);
                        if (i != CSV_HEADERS_ITEM_MULTIPLE_CONTRACTS.length - 1) {
                            buffer.append(COMMA);
                        }
                    }
                    if (!data.getHasMultipleContracts()) {
                        buffer.append(COMMA).append(CSV_HEADER_RUNNING_BALANCE);
                    }

                    buffer.append(LINE_BREAK);

                    /*
                     * Individual Line Items
                     */
                    ArrayList<CashAccountItem> theItems = (ArrayList<CashAccountItem>) report
                            .getDetails();
                    for (CashAccountItem theItem : theItems) {
                        String transactionDate = DateRender.format(theItem.getTransactionDate(),
                                null);
                        buffer.append(transactionDate).append(COMMA);
                        buffer.append(getCsvString(theItem.getTypeDescription1())).append(COMMA)
                                .append(getCsvString(theItem.getTypeDescription2())).append(COMMA);

                        Iterator it = theItem.getDescriptions().iterator();
                        for (int i = 0; i < 2; i++) {
                            if (it.hasNext()) {
								String description = (String) it.next();
								if(!StringUtils.equals(description, BDConstants.IN_PROGRESS)){								
									buffer.append(getCsvString(description));
								}
                            }
                            buffer.append(COMMA);
                        }

                        buffer.append(getCsvString(theItem.getTransactionNumber())).append(COMMA)
                                .append(getCsvString(theItem.getDebitAmount())).append(COMMA)
                                .append(getCsvString(theItem.getCreditAmount()));

                        if (!data.getHasMultipleContracts()) {
                            buffer.append(COMMA).append(getCsvString(theItem.getRunningBalance()));
                        }

                        buffer.append(LINE_BREAK);
                    }
                }
			}
		} catch (ContentException e) {
			throw new SystemException(e, getClass().getName(),
					"populateDownloadData", "Something wrong with CMA");
		}
		
		if (logger.isDebugEnabled())
			logger.debug("exit <- populateDownloadData");
		return buffer.toString().getBytes();
	}

	/**
	 * This method will return back the FileName for CSV.
	 */
	protected String getFileName(BaseReportForm reportForm, HttpServletRequest request) {
		Contract currentContract = getBobContext(request).getCurrentContract();
		String csvFileName = getReportName()  
							 + BDConstants.HYPHON_SYMBOL
							 + currentContract.getContractNumber() 
							 + BDConstants.HYPHON_SYMBOL 
							 + DateRender.format(getContractDatesVO(request).
                                     getAsOfDate(), RenderConstants.MEDIUM_MDY_SLASHED).
                                     replace(BDConstants.SLASH_SYMBOL, BDConstants.SPACE_SYMBOL)
							 + CSV_EXTENSION;
		return csvFileName;
	}
	
	/**
     * This method validates the Report data recieved and may show informational message to the
     * user.
     */
	protected void validateReportData(ReportData report, 
			BaseReportForm reportForm, HttpServletRequest request) {
		ArrayList<GenericException> infoMessages = new ArrayList<GenericException>();
		if (((CashAccountReportData)report).getDetails().isEmpty()) {
			ContentType contentType = ContentTypeManager.instance().MESSAGE;
			if(((CashAccountReportData)report).getHasTooManyItems()) {
				infoMessages.add(new GenericExceptionWithContentType(
						BDContentConstants.MESSAGE_TOO_MANY_TRANSACTIONS_FOR_DATE_SELECTED_CMA,contentType,false));
			} else {
				infoMessages.add(new GenericExceptionWithContentType(
						BDContentConstants.MESSAGE_NO_TRANSACTION_FOR_DATE_SELECTED_CMA,contentType,false));
			}
		}
		if (!infoMessages.isEmpty()) {
			setMessagesInRequest(request,infoMessages);
		}
	}
	
	/**
     * @See BaseReportAction#prepareXMLFromReport()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Document prepareXMLFromReport(BaseReportForm reportForm,
           ReportData report, HttpServletRequest request) throws ParserConfigurationException {
        CashAccountReportForm form = (CashAccountReportForm) reportForm;
        CashAccountReportData data = (CashAccountReportData) report;

        int rowCount = 1;
        int maxRowsinPDF;

        PDFDocument doc = new PDFDocument();
        
        // Gets layout page for cashAccountReport.jsp
        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.CONTRACT_CASH_ACCOUNT_PATH, request);

        Element rootElement = doc.createRootElement(BDPdfConstants.CONTRACT_CASH_ACCOUNT);

        // Sets Logo, Page Name, Contract Details, Intro-1, Intro-2.
        setIntroXMLElements(layoutPageBean, doc, rootElement, request);

        // Sets from date and to date.
        Date fromDate = (Date) data.getReportCriteria().getFilterValue(CashAccountReportData.FILTER_FROM_DATE);
        doc.appendTextNode(rootElement, BDPdfConstants.FROM_DATE, 
                           DateRender.format(fromDate, RenderConstants.EXTRA_LONG_MDY));
        Date toDate = (Date) data.getReportCriteria().getFilterValue(CashAccountReportData.FILTER_TO_DATE);
        doc.appendTextNode(rootElement, BDPdfConstants.TO_DATE, 
                           DateRender.format(toDate, RenderConstants.EXTRA_LONG_MDY));

        // Sets Summary Info.
        setSummaryInfoXMLElements(doc, rootElement, data, layoutPageBean, request);

        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);
        
        // Gets number of rows present in report.
        int noOfRows = getNumberOfRowsInReport(report);

        if (noOfRows == 0) {
            // Sets Information Message.
            setInfoMessagesXMLElements(doc, rootElement, request);
        } 
        else {
            // Sets total number of transactions.
            doc.appendTextNode(rootElement, BDPdfConstants.TOTAL_TXN, String.valueOf(data.getTotalCount()));
            
            // Main Report - start.
            Element txnDetailsElement = doc.createElement(BDPdfConstants.TXN_DETAILS);
            Iterator iterator = report.getDetails().iterator();
            // Gets number of rows to be shown in PDF.
            maxRowsinPDF = form.getCappedRowsInPDF();
            for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {
                Element txnDetailElement = doc.createElement(BDPdfConstants.TXN_DETAIL);
                CashAccountItem theItem = (CashAccountItem) iterator.next();
                // Sets main report.
                setReportDetailsXMLElements(doc, txnDetailElement, theItem, data);
                doc.appendElement(txnDetailsElement, txnDetailElement);
                rowCount++;
            }
            doc.appendElement(rootElement, txnDetailsElement);
            // Main Report - end.
        }

        if (form.getPdfCapped()) {
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
     * @param request
     */
    private void setSummaryInfoXMLElements(PDFDocument doc, Element rootElement, CashAccountReportData data, 
                 LayoutPage layoutPageBean, HttpServletRequest request) {
        Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);      
        String subHeader = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.SUB_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);

        String formattedDate = DateRender.formatByPattern(
                               getBobContext(request).getCurrentContract().getContractDates().getAsOfDate(), null,
                               RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.CURRENT_BALANCE_AS_OF_DATE, formattedDate);

        String currentBalance = NumberRender.formatByType(data.getCurrentBalance(), null, 
                                RenderConstants.CURRENCY_TYPE);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.CURRENT_BALANCE, currentBalance);

        if (!data.getHasMultipleContracts()) {

            // If data has no multiple contracts, set opening balance and closing balance
            String openingBalance = NumberRender.formatByType(data.getOpeningBalanceForPeriod(), null, 
                                    RenderConstants.CURRENCY_TYPE);
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.OPENING_BALANCE, openingBalance);

            String closingBalance = NumberRender.formatByType(data.getClosingBalanceForPeriod(), null, 
                                    RenderConstants.CURRENCY_TYPE);
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.CLOSING_BALANCE, closingBalance);

        }

        String totalDebits = NumberRender.formatByType(data.getTotalDebitsForPeriod(), null,
                             RenderConstants.CURRENCY_TYPE);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_DEBITS, totalDebits);

        String totalCredits = NumberRender.formatByType(data.getTotalCreditsForPeriod(), null,
                              RenderConstants.CURRENCY_TYPE);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_CREDITS, totalCredits);
        doc.appendElement(rootElement, summaryInfoElement);
    }
    
    /**
     * This method sets report details XML elements
     * 
     * @param doc
     * @param txnDetailElement
     * @param theItem
     * @param data
     */
    @SuppressWarnings("unchecked")
    private void setReportDetailsXMLElements(PDFDocument doc, Element txnDetailElement, CashAccountItem theItem, 
                 CashAccountReportData data) {
        
        if (theItem != null) {
            String formattedTxnDate = DateRender.formatByPattern(theItem.getTransactionDate(), null,
                                      RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED);
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_DATE, formattedTxnDate);
    
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_TYPE_DESCRIPTION1, theItem.getTypeDescription1());
    
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_TYPE_DESCRIPTION2, theItem.getTypeDescription2());
    
            Element txnDescriptions = doc.createElement(BDPdfConstants.TXN_DESCRIPTIONS);
    
            for (String description : (List<String>)theItem.getDescriptions()) {
				if(!StringUtils.equals(description, BDConstants.IN_PROGRESS)){
					doc.appendTextNode(txnDescriptions, BDPdfConstants.TXN_DESCRIPTION, description);								
				}            	
            }
    
            doc.appendElement(txnDetailElement, txnDescriptions);
    
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_NUMBER, theItem.getTransactionNumber());
    
            if (theItem.getDebitAmount() != null) {
                String debitAmount = NumberRender.formatByType(theItem.getDebitAmount(), null, RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
                doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_DEBITS, debitAmount);
            }
    
            if (theItem.getCreditAmount() != null) {
                String creditAmount = NumberRender.formatByType(theItem.getCreditAmount(), null, RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
                doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_CREDITS, creditAmount);
            }
    
            if (!data.getHasMultipleContracts() && (theItem.getRunningBalance() != null)) {
                // If data has no multiple contracts, set Running Balance
                String runningBalanceAmount = NumberRender.formatByType(theItem.getRunningBalance(), null, RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
                // Needs to remove parantheses surrounded by amt and prefix minus with
                // the amt.
                doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_RUNNING_BALANCE,
                        removeParanthesesAndPrefixMinus(runningBalanceAmount));
            }
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
    	CashAccountReportData data = (CashAccountReportData) report;
        int noOfRows = 0;
        if (report != null && !data.getHasTooManyItems()) {
            noOfRows = report.getTotalCount();
        }
        return noOfRows;
    }

    
}
