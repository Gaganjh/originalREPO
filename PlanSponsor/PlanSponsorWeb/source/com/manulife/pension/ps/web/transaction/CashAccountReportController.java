package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;

import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.environment.valueobject.ContractDatesVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * This action class handles the cash account history page.
 * 
 * @author Charles Chan
 */
@Controller
@RequestMapping( value ="/transaction")
@SessionAttributes({"cashAccountReportForm"})

public class CashAccountReportController extends AbstractTransactionReportController {
	
	@ModelAttribute("cashAccountReportForm") 
	public  CashAccountReportForm  populateForm() 
	{
		return new  CashAccountReportForm ();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/cashAccountReport.jsp"); 
		forwards.put("default","/transaction/cashAccountReport.jsp");
		forwards.put("sort","/transaction/cashAccountReport.jsp"); 
		forwards.put("filter","/transaction/cashAccountReport.jsp");
		forwards.put("page","/transaction/cashAccountReport.jsp"); 
		forwards.put("print","/transaction/cashAccountReport.jsp");
		}

	
	private static Logger logger = Logger
			.getLogger(CashAccountReportController.class);
	private static final String DEFAULT_SORT_FIELD = "transactionDate";
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;
	private static final String[] CSV_HEADERS_ITEM_MULTIPLE_CONTRACTS = new String[]{
			"Transaction date", "Type line 1", "Type line 2",
			"Description line 1", "Description line 2",
			"Transaction number", "Debits ($)", "Credits ($)"};

	private static final String CSV_HEADER_RUNNING_BALANCE = "Running balance($)";
	private static final String CSV_HEADER_FROM_DATE = "From date";
	private static final String CSV_HEADER_TO_DATE = "To date";
	private static final String CSV_HEADER_CURRENT_BALANCE = "Current balance";
	private static final String CSV_HEADER_AS_OF_DATE = "As of";
	private static final String CSV_HEADER_OPENING_BALANCE = "Opening balance this period";
	private static final String CSV_HEADER_CLOSING_BALANCE = "Closing balance this period";
	private static final String CSV_HEADER_TOTAL_DEBITS = "Total debits this period";
	private static final String CSV_HEADER_TOTAL_CREDITS = "Total credits this period";
	
	/**
	 * Constructor.
	 */
	public CashAccountReportController() {
		super(CashAccountReportController.class);
	}
	@Autowired
	 //@Qualifier("baseValidator")
	 private CashAmountReportValidator cashAmountReportValidator;
	/**
	 * Validate the input action form. FROM date must be less than TO date.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsAction#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	/*protected Collection doValidate( Form form,
			HttpServletRequest request) {
		Collection errors = super.doValidate( form, request);
		
		// if this is called using the default URL i.e. no parameters
		// do not validate
		if (request.getParameterNames().hasMoreElements()) {

			CashAccountReportForm actionForm = (CashAccountReportForm) form;
	
			if (actionForm.getFromDate() != null && actionForm.getToDate() != null) {
				if (Long.valueOf(actionForm.getFromDate()).longValue() > Long
						.valueOf(actionForm.getToDate()).longValue()) {
					errors.add(new GenericException(ErrorCodes.INVALID_DATE_RANGE));
				}
			}
	
			
			 * Resets the information for JSP to display.
			 
			if (errors.size() > 0) {
				
				 * Repopulates action form and request with default information.
				 
				populateReportForm( actionForm, request);
	
				request.setAttribute("displayDates", "true");
			}
		}
		return errors;
	}*/

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

		// get the user profile object and set the current contract to null
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		Integer contractNumber = new Integer(currentContract
				.getContractNumber());
		String clientId = userProfile.getClientId();

		criteria.addFilter(CashAccountReportData.FILTER_CONTRACT_NUMBER,
				contractNumber);

		criteria.addFilter(CashAccountReportData.FILTER_CLIENT_ID, clientId);

		criteria.addFilter(CashAccountReportData.FILTER_FROM_DATE, new Date(
				Long.valueOf(cashAccountReportForm.getFromDate())
						.longValue()));

		criteria.addFilter(CashAccountReportData.FILTER_TO_DATE, new Date(Long
				.valueOf(cashAccountReportForm.getToDate()).longValue()));
		
		if (userProfile.getCurrentContract().isDefinedBenefitContract()) {
			criteria.addFilter(TransactionHistoryReportData.CONTRACT_TYPE_DB, Boolean.TRUE);
		}		
		
        String task = request.getParameter(TASK_KEY);
        if (task == null) {
            task = DEFAULT_TASK;
        }
        criteria.addFilter("TASK", task);
        
		criteria.addFilter(CashAccountReportData.FILTER_PAGE,"PSW");
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
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm( reportForm, request);

		/*
		 * Obtain the contract dates object.
		 */
		ContractDatesVO contractDates = getContractDatesVO(request);
		List fromDates = new ArrayList();
		List toDates = new ArrayList();

		/*
		 * Using the contract dates, generate the date range (from dates and to
		 * dates).
		 */
		ContractDateHelper.populateFromToDates(getUserProfile(request).getCurrentContract(), fromDates, toDates);

		/*
		 * Stores the from dates and to dates into the request.
		 */
		request.setAttribute(Constants.CASH_ACCOUNT_FROM_DATES, fromDates);
		request.setAttribute(Constants.CASH_ACCOUNT_TO_DATES, toDates);

		CashAccountReportForm form = (CashAccountReportForm) reportForm;
		HttpSession session =request.getSession(false);
		if (form.getFromDate() == null || form.getFromDate().length() == 0) {

			String fromDate = StringUtils.trimToEmpty((String) session.getAttribute(Constants.CASH_ACCOUNT_FROM_DATE));
			if (!StringUtils.isEmpty(fromDate)) {
				form.setFromDate(fromDate);
			}else{
				form.setFromDate(String
						.valueOf(((Date) fromDates.iterator().next()).getTime()));
			}
		}

		if (form.getToDate() == null || form.getToDate().length() == 0) {
			String toDate = StringUtils.trimToEmpty((String) session.getAttribute(Constants.CASH_ACCOUNT_TO_DATE));
			if (!StringUtils.isEmpty(toDate)) {
				form.setToDate(toDate);
			}else{
				form.setToDate(String.valueOf(((Date) toDates.iterator().next())
						.getTime()));
			}
		}
		session.setAttribute(Constants.CASH_ACCOUNT_FROM_DATE, form.getFromDate());
		session.setAttribute(Constants.CASH_ACCOUNT_TO_DATE, form.getToDate());

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
		return CashAccountReportData.REPORT_NAME;
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
	protected byte[] getDownloadData(
			BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) throws SystemException {

		CashAccountReportData data = (CashAccountReportData) report;
		StringBuffer buffer = new StringBuffer();
		if (logger.isDebugEnabled())
			logger.debug("entry -> populateDownloadData");

		Contract currentContract = getUserProfile(request).getCurrentContract();
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
			if (data.getDetails().size() == 0) {
				Content message = null;
				if (data.getHasTooManyItems()) {
					message = ContentCacheManager
							.getInstance()
							.getContentById(
									ContentConstants.MESSAGE_TOO_MANY_TRANSACTIONS_FOR_DATE_SELECTED,
									ContentTypeManager.instance().MESSAGE);
				} else {
					message = ContentCacheManager
							.getInstance()
							.getContentById(
									ContentConstants.MESSAGE_NO_TRANSACTION_FOR_DATE_SELECTED,
									ContentTypeManager.instance().MESSAGE);
				}
				buffer.append(
						ContentUtility.getContentAttribute(message, "text"))
						.append(LINE_BREAK);
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
		        //SSE S024 determine wheather the ssn should be masked on the csv report
		        boolean maskSSN = true;// set the mask ssn flag to true as a default
		        try{
		        	maskSSN =ReportDownloadHelper.isMaskedSsn(getUserProfile(request), currentContract.getContractNumber() );
		         
		        }catch (SystemException se)
		        {
		        	  logger.error(se);
		        	// log exception and output blank ssn
		        }
				Iterator iterator = report.getDetails().iterator();
				while (iterator.hasNext()) {
					CashAccountItem theItem = (CashAccountItem) iterator.next();
					String transactionDate = DateRender.format(theItem
							.getTransactionDate(), null);
					buffer.append(transactionDate).append(COMMA);
					buffer
							.append(getCsvString(theItem.getTypeDescription1()))
							.append(COMMA)
							.append(getCsvString(theItem.getTypeDescription2()))
							.append(COMMA);

					if(!maskSSN){
						Iterator it = theItem.getDescriptionsUnmasked().iterator();
						for (int i = 0; i < 2; i++) {
							if (it.hasNext()) {
								String description = (String) it.next();
								if(!StringUtils.equals(description, CommonConstants.IN_PROGRESS)){
									buffer.append(getCsvString(description));								
								}	
							}
							buffer.append(COMMA);
						}				
					}
					else{
					Iterator it = theItem.getDescriptions().iterator();
					for (int i = 0; i < 2; i++) {
						if (it.hasNext()) {
							String description = (String) it.next();
							if(!StringUtils.equals(description, CommonConstants.IN_PROGRESS)){
								buffer.append(getCsvString(description));								
							}	
						}
						buffer.append(COMMA);
					}
					}


					buffer.append(getCsvString(theItem.getTransactionNumber()))
							.append(COMMA).append(
									getCsvString(theItem.getDebitAmount()))
							.append(COMMA).append(
									getCsvString(theItem.getCreditAmount()));

					if (!data.getHasMultipleContracts()) {
						buffer.append(COMMA).append(
								getCsvString(theItem.getRunningBalance()));
					}

					buffer.append(LINE_BREAK);
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
	@RequestMapping(value ="/cashAccountReport/" , method = {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("cashAccountReportForm") CashAccountReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    		populateReportForm(form, request);
				request.setAttribute("displayDates","true");
	              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	              return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);//if input forward not //available, provided default
	       }
		}
	       String forward=super.doDefault(form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
		 @RequestMapping(value ="/cashAccountReport/" ,params={"task=filter"},method =  {RequestMethod.GET}) 
		    public String doFilter (@Valid @ModelAttribute("cashAccountReportForm") CashAccountReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		    throws IOException,ServletException, SystemException {
		    	if(bindingResult.hasErrors()){
			        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			       if(errDirect!=null){
			    		populateReportForm( form, request);
			    		
						request.setAttribute("displayDates", "true");
			              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			       }
				}
			       String forward=super.doFilter( form, request, response);
					return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		 }
		    
		    	 @RequestMapping(value ="/cashAccountReport/" ,params={"task=page"}  , method =  {RequestMethod.GET}) 
		    	    public String doPage (@Valid @ModelAttribute("cashAccountReportForm") CashAccountReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		    	    throws IOException,ServletException, SystemException {
		    	    	if(bindingResult.hasErrors()){
		    		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		    		       if(errDirect!=null){
		    		    		populateReportForm( form, request);
		    		    		
		    					request.setAttribute("displayDates", "true");
		    		              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
		    		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		    		       }
		    			}
		    		       String forward=super.doPage( form, request, response);
		    				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		    	 }
		    	
		    		 @RequestMapping(value ="/cashAccountReport/" ,params={"task=sort"}  , method =  {RequestMethod.GET}) 
		     	    public String doSort (@Valid @ModelAttribute("cashAccountReportForm") CashAccountReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		     	    throws IOException,ServletException, SystemException {
		     	    	if(bindingResult.hasErrors()){
		     		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		     		       if(errDirect!=null){
		     		    		populateReportForm( form, request);
		     		    		
		     					request.setAttribute("displayDates", "true");
		     		              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
		     		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		     		       }
		     			}
		     		       String forward=super.doSort( form, request, response);
		     				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		     	 }
		    		 @RequestMapping(value ="/cashAccountReport/" ,params={"task=download"}  , method =  {RequestMethod.GET})	
		    	 public String doDownload (@Valid @ModelAttribute("cashAccountReportForm") CashAccountReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		    			     throws IOException,ServletException, SystemException {
		    			     	   if(bindingResult.hasErrors()){
		    			     		     String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		    			     		     if(errDirect!=null){
		    			     		    	populateReportForm( form, request);
		    			    	    		
		    			    				request.setAttribute("displayDates", "true");
		    			     		      request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
		    			    return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		    			     		    }
		    			     }
		    			     String forward=super.doDownload( form, request, response);
		    			     return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		    			    }
		    	
		    		 @RequestMapping(value ="/cashAccountReport/" ,params={"task=dowanloadAll"}  , method =  {RequestMethod.GET})
		    		 public String doDownloadAll (@Valid @ModelAttribute("cashAccountReportForm") CashAccountReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		    			     throws IOException,ServletException, SystemException {
		    			     	   if(bindingResult.hasErrors()){
		    			     		     String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		    			     		     if(errDirect!=null){
		    			     		    	populateReportForm( form, request);
		    			    	    		
		    			    				request.setAttribute("displayDates", "true");
		    			     		      request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
		    			    return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		    			     		    }
		    			     }
		    			     String forward=super.doDownloadAll( form, request, response);
		    			     return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		    			    }  
	/** This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#137697.
	 */
		@Autowired
		   private PSValidatorFWInput  psValidatorFWInput;
		@InitBinder
		  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
		    binder.bind(request);
		    binder.addValidators(psValidatorFWInput);
		    binder.addValidators(cashAmountReportValidator);
		}
}
