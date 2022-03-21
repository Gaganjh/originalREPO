package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionType;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionTypeDescription;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;
/**
 * This action class handles the transaction history page.
 * 
 * @author Maria Lee
 */
@Controller
@RequestMapping( value = "/transaction")
@SessionAttributes({"transactionHistoryReportForm"})



public class TransactionHistoryReportController
		extends
			AbstractTransactionReportController {
	
	@ModelAttribute("transactionHistoryReportForm") 
	public TransactionHistoryReportForm populateForm()
	{
		return new TransactionHistoryReportForm();
		}
	
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{forwards.put("input","/transaction/transactionHistoryReport.jsp"); 
	forwards.put("default","/transaction/transactionHistoryReport.jsp");
	forwards.put("sort", "/transaction/transactionHistoryReport.jsp"); 
	forwards.put("filter", "/transaction/transactionHistoryReport.jsp");
	forwards.put("page", "/transaction/transactionHistoryReport.jsp");
	forwards.put("print", "/transaction/transactionHistoryReport.jsp");
	}


	private static Logger logger = Logger
			.getLogger(TransactionHistoryReportController.class);
	private static final String DEFAULT_SORT_FIELD = TransactionHistoryReportData.SORT_FIELD_DATE;
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;

	private static final String CSV_HEADER_FROM_DATE = "From date";
	private static final String CSV_HEADER_TO_DATE = "To date";
	private static final String CSV_HEADER_TRANSACTION_TYPE = "Type";
	private static final String[] DOWNLOAD_COLUMN_HEADINGS = new String[]{
			"Transaction date", "Type line1", "Type line2",
			"Description line 1", "Description line 2", "Amount ($)",
			"Transaction number"};

	public static final String ALL_TYPES = "ALL";

	public static final List TYPES_DROPDOWN = new ArrayList();
	public static final List NO_LOANS_TYPES_DROPDOWN = new ArrayList();

	static {
		TYPES_DROPDOWN.add(new LabelValueBean("All", ALL_TYPES));
		NO_LOANS_TYPES_DROPDOWN.add(new LabelValueBean("All", ALL_TYPES));

		String[] types = new String[]{TransactionType.ADJUSTMENT,
				TransactionType.ALLOCATION,
				TransactionType.INTER_ACCOUNT_TRANSFER,
				TransactionType.LOAN_DEFAULT,
				TransactionType.LOAN_ISSUE, TransactionType.LOAN_REPAYMENT,
				TransactionType.LOAN_TRANSFER,
				TransactionType.MATURITY_REINVESTMENT,
				TransactionType.WITHDRAWAL};
				for (int i = 0; i < types.length; i++) {
			LabelValueBean lvbean = new LabelValueBean(
					TransactionTypeDescription.getDescription(types[i]),
					types[i]);
			TYPES_DROPDOWN.add(lvbean);
						if (types[i] != TransactionType.LOAN_ISSUE
					&& types[i] != TransactionType.LOAN_TRANSFER
					&& types[i] != TransactionType.LOAN_REPAYMENT
					&& types[i] != TransactionType.LOAN_DEFAULT) {
								NO_LOANS_TYPES_DROPDOWN.add(lvbean);
							}
		}

	}

	/**
	 * Constructor.
	 */
	public TransactionHistoryReportController() {
		super(TransactionHistoryReportController.class);
	}

	/**
	 * Validate the input action form. FROM date must be less than TO date.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */

@Autowired 
private TransactionHistoryReportValidator transactionHistoryReportValidator;

	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		TransactionHistoryReportForm theForm = (TransactionHistoryReportForm) form;

		UserProfile userProfile = getUserProfile(request);

		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter(TransactionHistoryReportData.FILTER_CONTRACT_NUMBER,
				new Integer(currentContract.getContractNumber()));
		
		criteria.addFilter(TransactionHistoryReportData.FILTER_PROPOSAL_NUMBER,
				currentContract.getProposalNumber());

		criteria.addFilter(TransactionHistoryReportData.FILTER_FROM_DATE,
				new Date(Long.valueOf(theForm.getFromDate()).longValue()));

		criteria.addFilter(TransactionHistoryReportData.FILTER_TO_DATE,
				new Date(Long.valueOf(theForm.getToDate()).longValue()));

		criteria.addFilter(TransactionHistoryReportData.FILTER_TYPE_LIST,
				getTransactionTypeCode(theForm.getTransactionType()));
		
		if (userProfile.getCurrentContract().isDefinedBenefitContract()) {
			criteria.addFilter(TransactionHistoryReportData.CONTRACT_TYPE_DB, Boolean.TRUE);
		}

	}

	/**
	 * If it's sort by Date, secondary sort is transaction number. If it's sort
	 * by amount, secondary sort is date and number. If it's sort by number,
	 * secondary sort is date.
	 *  
	 */
	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		criteria.insertSort(sortField, sortDirection);
		if (sortField.equals(TransactionHistoryReportData.SORT_FIELD_DATE)) {
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_NUMBER,
					sortDirection);
		} else if (sortField
				.equals(TransactionHistoryReportData.SORT_FIELD_AMOUNT)) {
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_DATE,
					sortDirection);
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_NUMBER,
					sortDirection);
		} else if (sortField
				.equals(TransactionHistoryReportData.SORT_FIELD_NUMBER)) {
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_DATE,
					sortDirection);
		}
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
		List fromDates = new ArrayList();
		List toDates = new ArrayList();

		/*
		 * Using the contract dates, generate the date range (from dates and to
		 * dates).
		 */
		ContractDateHelper.populateFromToDates(getUserProfile(request).getCurrentContract(), fromDates, toDates);

		/*
		 * Stores the from dates, to dates and the transaction types labels into
		 * the request.
		 */
		request.setAttribute(Constants.TXN_HISTORY_FROM_DATES, fromDates);
		request.setAttribute(Constants.TXN_HISTORY_TO_DATES, toDates);
		request.setAttribute(Constants.TRANSACTION_TYPES,
				NO_LOANS_TYPES_DROPDOWN);

		TransactionHistoryReportForm theForm = (TransactionHistoryReportForm) reportForm;

		if (theForm.getFromDate() == null
				|| theForm.getFromDate().length() == 0) {
			theForm.setFromDate(String.valueOf(((Date) fromDates.iterator()
					.next()).getTime()));
		}

		if (theForm.getToDate() == null || theForm.getToDate().length() == 0) {
			theForm.setToDate(String.valueOf(((Date) toDates.iterator().next())
					.getTime()));
		}

		if (theForm.getTransactionType() == null
				|| theForm.getTransactionType().length() == 0) {
			// defaults to all
			theForm.setTransactionType(ALL_TYPES);
		}
	}

	protected String getReportId() {
		return TransactionHistoryReportData.REPORT_ID;
	}

	protected String getReportName() {
		return TransactionHistoryReportData.REPORT_NAME;
	}

	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	protected byte[] getDownloadData(
			BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) throws SystemException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> populateDownloadData");
		TransactionHistoryReportData data = (TransactionHistoryReportData) report;
		StringBuffer buffer = new StringBuffer();
		TransactionHistoryReportForm form = (TransactionHistoryReportForm) reportForm;

        Contract currentContract = getUserProfile(request).getCurrentContract();
        buffer.append("Contract").append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
				currentContract.getCompanyName()).append(LINE_BREAK);

		try {
			/*
			 * Obtain the from date and to date.
			 */
			Date fromDate = (Date) report.getReportCriteria().getFilterValue(
					TransactionHistoryReportData.FILTER_FROM_DATE);
			Date toDate = (Date) report.getReportCriteria().getFilterValue(
					TransactionHistoryReportData.FILTER_TO_DATE);

			String fromDateStr = DateRender.format(fromDate,
					RenderConstants.EXTRA_LONG_MDY);
			String toDateStr = DateRender.format(toDate,
					RenderConstants.EXTRA_LONG_MDY);

			buffer.append(CSV_HEADER_FROM_DATE).append(COMMA);
			buffer.append(fromDateStr).append(COMMA);
			buffer.append(CSV_HEADER_TO_DATE).append(COMMA);
			buffer.append(toDateStr).append(LINE_BREAK);
			buffer.append(LINE_BREAK);
			
			String transactionType = getTransactionTypeDescription(form
					.getTransactionType());

			buffer.append(CSV_HEADER_TRANSACTION_TYPE).append(COMMA);
			buffer.append(transactionType).append(LINE_BREAK);

			buffer.append(LINE_BREAK);
			if (data.getDetails().size() == 0) {
				Content message = null;
				message = ContentCacheManager
						.getInstance()
						.getContentById(
								ContentConstants.MESSAGE_NO_HISTORY_TRANSACTION_FOR_DATE_SELECTED,
								ContentTypeManager.instance().MESSAGE);

				buffer.append(
						ContentUtility.getContentAttribute(message, "text"))
						.append(LINE_BREAK);

			} else {

				for (int i = 0; i < DOWNLOAD_COLUMN_HEADINGS.length; i++) {
					buffer.append(DOWNLOAD_COLUMN_HEADINGS[i]);
					if (i != DOWNLOAD_COLUMN_HEADINGS.length - 1) {
						buffer.append(COMMA);
					}
				}
				buffer.append(LINE_BREAK);
				// SSN
			       //SSE S024 determine wheather the ssn should be masked on the csv report
				boolean maskSSN = true;// set the mask ssn flag to true as a default
				UserProfile user = getUserProfile(request);
				try{
		     	maskSSN =ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber() );
		      
				}catch (SystemException se){
					logger.error(se);
					// log exception and output blank ssn
				}	
				/*
				 * Individual Line Items
				 */
				Iterator iterator = report.getDetails().iterator();
				while (iterator.hasNext()) {
					TransactionHistoryItem theItem = (TransactionHistoryItem) iterator
							.next();
					String transactionDate = DateRender.format(theItem
							.getTransactionDate(), null);
					buffer.append(transactionDate).append(COMMA);
					buffer.append(getCsvString(theItem.getTypeDescription1()))
							.append(COMMA);
					buffer.append(getCsvString(theItem.getTypeDescription2()))
							.append(COMMA);
					if(!maskSSN){
						Iterator it = theItem.getDescriptionsUnmasked().iterator();
						for (int i = 0; i < 2; i++) {
							if (it.hasNext()) {
								buffer.append(getCsvString(it.next()));
							}
							buffer.append(COMMA);
						}				
					}
					else{
					Iterator it = theItem.getDescriptions().iterator();
					for (int i = 0; i < 2; i++) {
						if (it.hasNext()) {
							buffer.append(getCsvString(it.next()));
						}
						buffer.append(COMMA);
					}
					}

					buffer.append(getCsvString(theItem.getAmount())).append(
							COMMA);
					buffer.append(getCsvString(theItem.getTransactionNumber()));

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

	/**
	 * Gets the transaction type description.
	 * 
	 * @return Returns the transaction type description.
	 */
	protected String getTransactionTypeDescription(String code) {
		/*
		 * construct an array of keys if user selected "All" option Array starts
		 * with index 1 i.e. minus key for ALL
		 */
		for (Iterator it = TYPES_DROPDOWN.iterator(); it.hasNext();) {
			LabelValueBean bean = (LabelValueBean) it.next();
			if (bean.getValue().equals(code)) {
				return bean.getLabel();
			}
		}
		throw new IllegalArgumentException("Invalid code [" + code + "]");
	}

	/**
	 * Gets the transactionTypes
	 * 
	 * @return Returns a Collection
	 */
	protected Collection getTransactionTypeCode(String type) {
		/*
		 * construct an array of keys if user selected "All" option Array starts
		 * with index 1 i.e. minus key for ALL
		 */
		List types = new ArrayList();
		if (type.equalsIgnoreCase(ALL_TYPES)) {
			for (Iterator it = TYPES_DROPDOWN.iterator(); it.hasNext();) {
				LabelValueBean bean = (LabelValueBean) it.next();
				if (bean.getValue().equals(ALL_TYPES)) {
					continue;
				}
				types.add(bean.getValue());
			}
		} else {
			types.add(type);
		}
		return types;
	}
	/**
	 * @see ReportController#doCommon(ActionMapping, BaseReportForm,
	 *      HttpServletRequest, HttpServletResponse)
	 */
	
		 
		protected String doCommon (BaseReportForm reportForm, HttpServletRequest request,
				HttpServletResponse response) 
		throws  SystemException {
			
		String forward = super.doCommon( reportForm, request,
				response);

		/*
		 * determine if the results have any loan items in them if not, remove
		 * the loan-related types from the types dropdown
		 */

		TransactionHistoryReportData report = (TransactionHistoryReportData) request
				.getAttribute(Constants.REPORT_BEAN);
		if (report != null && (report.hasLoans()
				|| getUserProfile(request).getCurrentContract().isLoanFeature()) ) {
			request.setAttribute(Constants.TRANSACTION_TYPES, TYPES_DROPDOWN);
		} else {
			request.setAttribute(Constants.TRANSACTION_TYPES,
					NO_LOANS_TYPES_DROPDOWN);
		}

		return forward;
	}
	
	@RequestMapping(value ="/transactionHistoryReport/",params={"task=filter"}, method =  {RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("transactionHistoryReportForm") TransactionHistoryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		Collection errors =doValidate(actionForm, request);
		if(errors.size()>0) 
		{
			BaseSessionHelper.setErrorsInSession(request, errors);
			return forwards.get("input");
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doFilter( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ="/transactionHistoryReport/" ,params={"task=page"} , method =  {RequestMethod.GET}) 
	public String doPage (@Valid @ModelAttribute("transactionHistoryReportForm") TransactionHistoryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doPage( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forwards.get(forward):forwards.get(forward); 
 }
	@RequestMapping(value ="/transactionHistoryReport/" ,params={"task=sort"}, method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("transactionHistoryReportForm") TransactionHistoryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doSort( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ="/transactionHistoryReport/", params={"task=download"}, method =  {RequestMethod.GET}) 
	public String doDownload (@Valid @ModelAttribute("transactionHistoryReportForm") TransactionHistoryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doDownload( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/transactionHistoryReport/", params={"task=downloadAll"}, method = {RequestMethod.GET}) 
	public String doDownloadAll (@Valid @ModelAttribute("transactionHistoryReportForm") TransactionHistoryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doDownloadAll( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	
	@RequestMapping(value ="/transactionHistoryReport/" , method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("transactionHistoryReportForm") TransactionHistoryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if input
			}
		}
		String forward = super.doDefault(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value ="/transactionHistoryReport/",params={"task=print"}, method =  {RequestMethod.GET}) 
	public String doPrint (@Valid @ModelAttribute("transactionHistoryReportForm") TransactionHistoryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doPrint(actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	/*
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of
	 * the CL#137697.
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#doValidate(org
	 * .apache.struts.action.ActionMapping,
	 * org.apache.struts.action.Form,javax
	 * .servlet.http.HttpServletRequest)
	 */
	protected Collection doValidate(ActionForm form,
			HttpServletRequest request)
	{
		Collection errors = super.doValidate(form, request);
		TransactionHistoryReportForm actionForm = (TransactionHistoryReportForm) form;

		// if this is called using the default URL i.e. no parameters
		// do not validate
		if (request.getParameterNames().hasMoreElements()) {
	
	
			if (actionForm.getFromDate() != null && actionForm.getToDate() != null) {
				if (Long.valueOf(actionForm.getFromDate()).longValue() > Long
						.valueOf(actionForm.getToDate()).longValue()) {
					errors.add(new GenericException(ErrorCodes.INVALID_DATE_RANGE));
				}
			}
	
			/*
			 * Resets the information for JSP to display.
			 */
			if (errors.size() > 0) {
				/*
				 * Repopulates action form and request with default information.
				 */
				populateReportForm(actionForm, request);
				// signal the JSP to display the date dropdowns again for the user to change
				// their selection
				request.setAttribute("displayDates", "true");
			}
		}
		
		return errors;
	}
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}

}
