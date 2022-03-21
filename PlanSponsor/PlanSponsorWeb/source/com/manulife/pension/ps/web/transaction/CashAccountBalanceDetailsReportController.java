package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

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
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountBalanceDetailsReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData;
import com.manulife.pension.ps.web.Constants;

import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * This action class handles the cash account balance details page.
 * 
 * @author Rajesh Rajendran
 */
@Controller
@RequestMapping( value ="/transaction")
@SessionAttributes({"balanceDetailsReportForm"})

public class CashAccountBalanceDetailsReportController extends AbstractTransactionReportController {
	@ModelAttribute("balanceDetailsReportForm") 
	public ReportForm populateForm() 
	{
		return new ReportForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/cashAccountBalanceDetailsReport.jsp"); 
		forwards.put("default","/transaction/cashAccountBalanceDetailsReport.jsp");
		forwards.put("page","/transaction/cashAccountBalanceDetailsReport.jsp"); 
		forwards.put("print","/transaction/cashAccountBalanceDetailsReport.jsp");}

	
	/**
	 * Logger object
	 */
	private static Logger logger = Logger
			.getLogger(CashAccountReportController.class);
	
	private static final String DEFAULT_SORT_FIELD = "transactionDate";
	
	private static final String[] CSV_HEADERS_ITEM_MULTIPLE_CONTRACTS = new String[]{
		"Transaction date", "Type line 1", "Type line 2",
		"Description line 1", "Description line 2",
		"Transaction number", "Original Amount ($)", "Available Amount ($)"};

	/**
	 * Constructor.
	 */
	public CashAccountBalanceDetailsReportController() {
		super(CashAccountBalanceDetailsReportController.class);
	}
	
	@RequestMapping(value ="/cashAccountBalanceDetailsReport/" , method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("balanceDetailsReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	       String forward=super.doDefault( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ="/cashAccountBalanceDetailsReport/" ,params={"task=download"}  , method =  {RequestMethod.GET})	
	 public String doDownload (@Valid @ModelAttribute("balanceDetailsReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

		// get the user profile object and set the current contract to null
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		Integer contractNumber = Integer.valueOf(
				currentContract.getContractNumber());
		
		String clientId = userProfile.getClientId();
		
		Date fromDate = currentContract.getEffectiveDate();
		Date toDate = currentContract.getContractDates().getAsOfDate();

		criteria.addFilter(CashAccountBalanceDetailsReportData.FILTER_CONTRACT_NUMBER,contractNumber);

		criteria.addFilter(CashAccountBalanceDetailsReportData.FILTER_CLIENT_ID, clientId);

		criteria.addFilter(CashAccountBalanceDetailsReportData.FILTER_FROM_DATE, fromDate);
		
		criteria.addFilter(CashAccountBalanceDetailsReportData.FILTER_TO_DATE, toDate);

		if (userProfile.getCurrentContract().isDefinedBenefitContract()) {
			criteria.addFilter(TransactionHistoryReportData.CONTRACT_TYPE_DB, Boolean.TRUE);
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	protected String getReportId() {
		return CashAccountBalanceDetailsReportData.REPORT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	protected String getReportName() {
		return CashAccountBalanceDetailsReportData.REPORT_NAME;
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
		return ReportSort.DESC_DIRECTION;
	}	
	
	/**
	 * Writes out a CSV file using the given form, report data, and request.
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportController#getDownloadData(java.io.PrintWriter,
	 *      com.manulife.pension.service.report.valueobject.ReportData)
	 * @param reportForm
	 * @param report
	 * @param reqest
	 * @return CSV data in byte form
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	protected byte[] getDownloadData(
			BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) throws SystemException {
		
		if (logger.isDebugEnabled())
			logger.debug("entry -> getDownloadData");
		
		CashAccountBalanceDetailsReportData data = (CashAccountBalanceDetailsReportData) report;
		StringBuffer buffer = new StringBuffer();

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

			buffer.append(Constants.CSV_HEADER_FROM_DATE).append(COMMA)
					.append(DateRender.format(fromDate,RenderConstants.EXTRA_LONG_MDY))
					.append(COMMA).append(Constants.CSV_HEADER_TO_DATE)
					.append(COMMA).append(DateRender.format(toDate,
									RenderConstants.EXTRA_LONG_MDY)).append(LINE_BREAK);
			buffer.append(LINE_BREAK);

			/*
			 * Current Balance
			 */
			buffer.append(Constants.CSV_HEADER_CURRENT_BALANCE).append(COMMA).append(
					getCsvString(data.getCurrentBalance()));
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
									ContentConstants.MESSAGE_NO_BALANCE_DETAILS_TRANSACTION_FOR_DATE_SELECTED,
									ContentTypeManager.instance().MESSAGE);
				}
				buffer.append(ContentUtility.getContentAttribute(message, ContentConstants.TEXT))
					.append(LINE_BREAK);
			} else {

				/*
				 * Line Item Header
				 */
				for (int i = 0; i < CSV_HEADERS_ITEM_MULTIPLE_CONTRACTS.length; i++) {
					buffer.append(CSV_HEADERS_ITEM_MULTIPLE_CONTRACTS[i]);
					if (i != CSV_HEADERS_ITEM_MULTIPLE_CONTRACTS.length - 1) {
						buffer.append(COMMA);
					}
				}
				buffer.append(LINE_BREAK);

				/*
				 * Individual Line Items
				 */
		        // determine whether the ssn should be masked on the csv report
		        boolean maskSSN = true;// set the mask ssn flag to true as a default
		        try {
		        	
		        	maskSSN =ReportDownloadHelper.isMaskedSsn(getUserProfile(request), currentContract.getContractNumber() );
		         
		        } catch (SystemException se){
		        	// log exception and output blank ssn
		        	  logger.error(se);
		        }
		        
				Collection<CashAccountItem> details = report.getDetails();
				for(CashAccountItem theItem : details) {
					String transactionDate = DateRender.format(theItem
							.getTransactionDate(), null);
					buffer.append(transactionDate).append(COMMA);
					buffer.append(getCsvString(theItem.getTypeDescription1()))
						.append(COMMA)
						.append(getCsvString(theItem.getTypeDescription2()))
						.append(COMMA);

					if(!maskSSN){
						Iterator it = theItem.getDescriptionsUnmasked().iterator();
						for (int i = 0; i < 2; i++) {
							if (it.hasNext()) {
								String description = (String) it.next();
								if(!StringUtils.equals(description, Constants.IN_PROGRESS)){
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
								if(!StringUtils.equals(description, Constants.IN_PROGRESS)){
									buffer.append(getCsvString(description));								
								}								
							}
							buffer.append(COMMA);
						}
					}					
					buffer.append(getCsvString(theItem.getTransactionNumber()))
							.append(COMMA).append(getCsvString(theItem.getOriginalAmount()))
							.append(COMMA).append(getCsvString(theItem.getAvailableAmount()));

					buffer.append(LINE_BREAK);
				}		        
			}
		} catch (ContentException e) {
			throw new SystemException(e, getClass().getName() + 
					"getDownloadData. Something went wrong while gettting content from CMA");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getDownloadData");
		}
		
		return buffer.toString().getBytes();
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
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	
}