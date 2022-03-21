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

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.ps.service.report.transaction.reporthandler.CashAccountForfeituresReportHandler;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountForfeituresReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;

import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.account.entity.ContractMoneyType;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsACIReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * This action class handles the cash account forfeitures page.
 * 
 * @author Chavva Akhilesh
 */
@Controller
@RequestMapping( value ="/transaction")
@SessionAttributes({"cashAccountForfeituresForm"})

public class CashAccountForfeituresReportController extends
		AbstractTransactionReportController {
	@ModelAttribute("cashAccountForfeituresForm")
	public CashAccountForfeituresReportForm populateForm()
	{
		return new CashAccountForfeituresReportForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/cashAccountForfeituresReport.jsp");
		forwards.put("default","/transaction/cashAccountForfeituresReport.jsp");
		forwards.put("sort","/transaction/cashAccountForfeituresReport.jsp");
		forwards.put("filter","/transaction/cashAccountForfeituresReport.jsp");
		forwards.put("page","/transaction/cashAccountForfeituresReport.jsp");
		forwards.put("print","/transaction/cashAccountForfeituresReport.jsp");}

	public static final String REPORT_ID = CashAccountForfeituresReportHandler.class.getName();
	
	private static final String[] CSV_HEADERS_ITEM = new String[] {
			"Transaction date", "Money Type", "Type line 1", "Type line 2",
			"Description line 1", "Description line 2",
			"Transaction number", "Original amount ($)", "Available amount ($)" };
	private static final String CONTRACT = "Contract";
	private static final String CSV_HEADER_FROM_DATE = "From date";
    private static final String CSV_HEADER_TO_DATE = "To date";
    private static final String CSV_HEADER_MONEY_TYPE_SELECTED = "Money type selected";
	private static final String CSV_HEADER_TOTAL_FORFEITURES_IN_PLAN = "Total Forfeitures in Plan";
	private static final String CSV_HEADER_TOTAL_FORFEITURES_IN_CASH_ACCOUNT = "Total Forfeitures in Cash Account";
	private static final String CSV_HEADER_TOTAL_FORFEITURES_IN_PARTICIPANT_ACCOUNT = "Total Forfeitures in Participant Accounts";
	
	private static final String ALL_MONEY_TYPE_FILTER_LABEL = "All money types";
	private static final String ALL_MONEY_TYPE_FILTER_KEY = "ALL";
	private static final String ADJUSTMENT_MONEY_TYPE_FILTER_LABEL = "Adjustment";
	private static final String ADJUSTMENT_MONEY_TYPE_FILTER_KEY = "AD";
	private static final String CASH_ACCOUNT_FORFEITURE_MONEY_TYPE_ID = "moneyTypeId";
	
	/**
	 * Constructor.
	 */
	public CashAccountForfeituresReportController() {
		super(CashAccountForfeituresReportController.class);
	}
	
	/**
	 * Overwritten method of superclass
	 */
    protected String doCommon(
            BaseReportForm reportForm, HttpServletRequest request,
            HttpServletResponse response) throws SystemException {
    	
    	String forward = null;
    	List<GenericException> errors = new ArrayList<GenericException>();  	
    	
    	try {
        	forward = super.doCommon( reportForm, request, response);
        	
        	CashAccountForfeituresReportData forfeituresReportData = 
        		(CashAccountForfeituresReportData) request.getAttribute(
        				CommonConstants.REPORT_BEAN);
        	
        	CashAccountForfeituresReportForm cashAccountForfeituresForm = 
        		(CashAccountForfeituresReportForm) reportForm;
        	
        	// generate the money type filter list
        	cashAccountForfeituresForm.setListOfContractMoneyTypes(generateMoneyTypeFilters(
        			forfeituresReportData));
            
        } catch (SystemException e) {
			// Log the system exception.
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,e);

			// Show user friendly message.
			errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
			setErrorsInRequest(request, errors); 
			forward = forwards.get("input");
		}
        
        return forward;
    }
    
    /**
     * Generates the money type filter values
     * 
     * @param forfeituresReportData
     * @return list of label value bean
     */
    private List<LabelValueBean> generateMoneyTypeFilters(
    		CashAccountForfeituresReportData forfeituresReportData) {
    	
    	List<LabelValueBean> listOfContractMoneyTypes = new ArrayList<LabelValueBean>();
    	
    	// add the all money type filer
    	listOfContractMoneyTypes.add(new LabelValueBean(ALL_MONEY_TYPE_FILTER_LABEL, 
    			ALL_MONEY_TYPE_FILTER_KEY));
    	
    	// add the adjustment filter
    	listOfContractMoneyTypes.add(new LabelValueBean(ADJUSTMENT_MONEY_TYPE_FILTER_LABEL, 
    			ADJUSTMENT_MONEY_TYPE_FILTER_KEY));

    	if (forfeituresReportData != null) {
	    	List<ContractMoneyType> moneyTypeList = 
	    		forfeituresReportData.getListOfContractMoneyTypes();
	
	    	// if there are UM money types add it the filter list
	    	if (moneyTypeList != null && !moneyTypeList.isEmpty()) {
	    	
	        	for (Iterator<ContractMoneyType> iterator = moneyTypeList.iterator(); iterator.hasNext();) {
					ContractMoneyType contractMoneyType = (ContractMoneyType) iterator.next();
					String moneyId = contractMoneyType.getMoneyTypeId();
					String moneyTypeMediumName = contractMoneyType.getMoneyTypeMediumName();
	
					// add the options to the list
					listOfContractMoneyTypes.add(new LabelValueBean(
							moneyTypeMediumName, moneyId));				
				}
	    	}
    	}
    	
    	return listOfContractMoneyTypes;
    }
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	protected String getReportId() {
		return CashAccountForfeituresReportData.REPORT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	protected String getReportName() {
		return CashAccountForfeituresReportData.REPORT_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return CashAccountForfeituresReportData.DEFAULT_SORT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.DESC_DIRECTION;
	}

	@SuppressWarnings("deprecation")
	@Override
	/**
	 * Writes out a CSV file using the given form, report data, and request.
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#getDownloadData(
	 * com.manulife.pension.platform.web.report.BaseReportForm,
	 * com.manulife.pension.service.report.valueobject.ReportData
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> getDownloadData");
		
		CashAccountForfeituresReportData data = (CashAccountForfeituresReportData) report;
		StringBuffer buffer = new StringBuffer();
		Contract currentContract = getUserProfile(request).getCurrentContract();
		//1. Contract Number and Contract name
        buffer.append(CONTRACT).append(COMMA)
        	  .append(currentContract.getContractNumber()).append(COMMA)
        	  .append(escapeField(currentContract.getCompanyName())).append(LINE_BREAK);

		try {
			
			 //* Obtain the from date and to date.
			Date fromDate = (Date) report.getReportCriteria().getFilterValue(
					CashAccountForfeituresReportData.FILTER_FROM_DATE);
			Date toDate = (Date) report.getReportCriteria().getFilterValue(
					CashAccountForfeituresReportData.FILTER_TO_DATE);
			//2. From and To dates
			buffer.append(CSV_HEADER_FROM_DATE).append(COMMA)
				  .append(DateRender.format(fromDate,RenderConstants.EXTRA_LONG_MDY)).append(COMMA)
				  .append(CSV_HEADER_TO_DATE).append(COMMA)
				  .append(DateRender.format(toDate,RenderConstants.EXTRA_LONG_MDY)).append(LINE_BREAK);
			buffer.append(LINE_BREAK);
			
			//* Money Type ID for Money Type Selected
			CashAccountForfeituresReportForm forfeituresReportForm 
			= (CashAccountForfeituresReportForm) reportForm;
			String moneyTypeId = forfeituresReportForm.getMoneyTypeId();

			List<LabelValueBean> moneyTypes=forfeituresReportForm.getListOfContractMoneyTypes();
			
			String moneyTypeMediumName = null;
			if(moneyTypeId != null){
				for(LabelValueBean contractMoneyType: moneyTypes) {
					if(contractMoneyType.getValue().equals(moneyTypeId)){
						moneyTypeMediumName = contractMoneyType.getLabel();
					}
				}
			}
			if(moneyTypeMediumName == null){
				moneyTypeMediumName = "All money types";
			}
			//3. Money Type Selected
			buffer.append(CSV_HEADER_MONEY_TYPE_SELECTED).append(COMMA)
			      .append(getCsvString(moneyTypeMediumName)).append(LINE_BREAK);
			buffer.append(LINE_BREAK);

			//4. Total Forfeitures in Plan
			buffer.append(CSV_HEADER_TOTAL_FORFEITURES_IN_PLAN).append(COMMA)
				  .append(getCsvString(data.getTotalForfeituresInPlan())).append(LINE_BREAK);

			//5. Total Forfeitures in Cash Account
			buffer.append(CSV_HEADER_TOTAL_FORFEITURES_IN_CASH_ACCOUNT).append(COMMA)
				  .append(getCsvString(data.getTotalForfeituresInCashAccount())).append(LINE_BREAK);

			//6. Total Forfeitures in participant account
			buffer.append(CSV_HEADER_TOTAL_FORFEITURES_IN_PARTICIPANT_ACCOUNT).append(COMMA)
				  .append(getCsvString(data.getTotalForfeituresInParticipant())).append(LINE_BREAK);
			buffer.append(LINE_BREAK);


			// * If there are no transaction...
			if (data.getDetails().size() == 0) {
				Content message = null;
				if (data.isHasTooManyItems()) {
					message = ContentCacheManager
							.getInstance()
							.getContentById(
									ContentConstants.MESSAGE_TOO_MANY_TRANSACTIONS_FOR_MONEY_TYPE__SELECTED,
									ContentTypeManager.instance().MESSAGE);
				} else {
					message = ContentCacheManager
							.getInstance()
							.getContentById(
									ContentConstants.MESSAGE_THERE_WERE_NO_FORFEITURES_TRANSACTIONS,
									ContentTypeManager.instance().MESSAGE);
				}
				buffer.append(ContentUtility.getContentAttribute(message, "text"))
						.append(LINE_BREAK);
			} else {

				// * Line Item Header
				for (int i = 0; i < CSV_HEADERS_ITEM.length; i++) {
					buffer.append(CSV_HEADERS_ITEM[i]);
					if (i != CSV_HEADERS_ITEM.length - 1) {
						buffer.append(COMMA);
					}
				}
				buffer.append(LINE_BREAK);

				// * Individual Line Items
		        boolean maskSSN = true;// set the mask ssn flag to true as a default
		        try{
		        	maskSSN =ReportDownloadHelper.isMaskedSsn(getUserProfile(request), currentContract.getContractNumber() );
		         
		        }catch (SystemException se)
		        {
		        	  logger.error(se);
		        	// log exception and output blank ssn
		        }
		        
		        //7. List of transactions, for each transaction 
				@SuppressWarnings("unchecked")
				Collection<CashAccountItem> details = report.getDetails();
				for(CashAccountItem theItem : details) {
					String transactionDate = DateRender.format(theItem
							.getTransactionDate(), null);
					
					//7.1 Transaction Date
					buffer.append(transactionDate).append(COMMA);
					//7.2 Money Type
					buffer.append(getCsvString(theItem.getMoneyType())).append(COMMA);
					//7.3 Type Line 1
					buffer.append(getCsvString(theItem.getTypeDescription1())).append(COMMA)
					// Type Line 2
						  .append(getCsvString(theItem.getTypeDescription2())).append(COMMA);
					//7.4 Description Line 1, Line 2 - SSN, Name
					if(!maskSSN){
						Iterator it = theItem.getDescriptionsUnmasked().iterator();
						int i = 0;
						for (i=0; i < 2; i++) {
							if (it.hasNext()) {
								String description = (String) it.next();
								if(!StringUtils.equals(description, Constants.IN_PROGRESS)){
									buffer.append(getCsvString(description));								
								}	
							}
							buffer.append(COMMA);
						}
					} else {
						Iterator it = theItem.getDescriptions().iterator();
						int i = 0;
						for (i = 0; i < 2; i++) {
							if (it.hasNext()) {
								String description = (String) it.next();
								if(!StringUtils.equals(description, Constants.IN_PROGRESS)){
									buffer.append(getCsvString(description));
									buffer.append(COMMA);
								}	
							}
						}
					}

					//7.5 Transaction number
					buffer.append(getCsvString(theItem.getTransactionNumber())).append(COMMA)
					//7.6 Original amount
						  .append(getCsvString(theItem.getOriginalAmount())).append(COMMA)
					//7.7 Available amount
						  .append(getCsvString(theItem.getAvailableAmount()));

					buffer.append(LINE_BREAK);
				}
			}
		} catch (ContentException e) {
			throw new SystemException(e, getClass().getName(),
					"getDownloadData", "Something wrong with CMA");
		}

		if (logger.isDebugEnabled()){
			logger.debug("exit <- getDownloadData");
		}
		
		return buffer.toString().getBytes();
		
	}

	@Override
	/**
	 * * This method is called to populate a report criteria from the report
	 * action form and the request. It is called right before getReportData is
	 * called.
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria
	 * (com.manulife.pension.service.report.valueobject.ReportCriteria,
	 * com.manulife.pension.ps.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) throws SystemException {

		CashAccountForfeituresReportForm forfeituresReportForm = 
			(CashAccountForfeituresReportForm) form;
		
		// get the user profile object and set the current contract to null
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		Integer contractNumber = Integer.valueOf(
				currentContract.getContractNumber());
		
		String clientId = userProfile.getClientId();

		// contract number filter
		criteria.addFilter(
				CashAccountForfeituresReportData.FILTER_CONTRACT_NUMBER,
				contractNumber);

		// client id filter
		criteria.addFilter(
				CashAccountForfeituresReportData.FILTER_CLIENT_ID, 
				clientId);
		
		// money type filter
		criteria.addFilter(
				CashAccountForfeituresReportData.FILTER_MONEY_ID, 
				forfeituresReportForm.getMoneyTypeId());

		// From date filter
		criteria.addFilter(
				CashAccountForfeituresReportData.FILTER_FROM_DATE, 
				currentContract.getEffectiveDate());
		
		// To date filter
		criteria.addFilter(
		CashAccountForfeituresReportData.FILTER_TO_DATE, 
		currentContract.getContractDates().getAsOfDate());
										
		// task filter
		String task = request.getParameter(TASK_KEY);
        if (task == null) {
            task = DEFAULT_TASK;
        }
        criteria.addFilter(CashAccountForfeituresReportData.FILTER_TASK, task);	
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
		//When user leaves the page, we have to retain the current value of the Money Type search filter 
		//during the user’s web session so that the user will see the previous selection when 
		//they return to this page.
		//Below code is return to achieve the same
		CashAccountForfeituresReportForm form = (CashAccountForfeituresReportForm) reportForm;
		HttpSession session =request.getSession(false);
		String moneyTypeId = StringUtils.trimToEmpty((String) session.getAttribute(CASH_ACCOUNT_FORFEITURE_MONEY_TYPE_ID));

		if(StringUtils.isEmpty(moneyTypeId) && form.getMoneyTypeId() == null){
			form.setMoneyTypeId(CashAccountForfeituresReportData.ALL_MONEY_TYPES_KEY);
			session.setAttribute(CASH_ACCOUNT_FORFEITURE_MONEY_TYPE_ID, form.getMoneyTypeId());
		}
		else if((!StringUtils.isEmpty(moneyTypeId) && form.getMoneyTypeId() != null) || (StringUtils.isEmpty(moneyTypeId) && form.getMoneyTypeId() != null)){
			session.setAttribute(CASH_ACCOUNT_FORFEITURE_MONEY_TYPE_ID, form.getMoneyTypeId());
		}
		else if(!StringUtils.isEmpty(moneyTypeId) && form.getMoneyTypeId() == null){
			form.setMoneyTypeId((String) session.getAttribute(CASH_ACCOUNT_FORFEITURE_MONEY_TYPE_ID));
		}
		
	}
	
	/**
     * Method to display comma(,) in downloaded spread sheet.
     * @param field String
     * @return field String
     */
    private static String escapeField(String field){
        
    	if(field.indexOf(CommonConstants.COMMA) != -1 ){
            StringBuffer newField = new StringBuffer();
            newField = newField.append(CommonConstants.DOUBLE_QUOTES).append(field).append(CommonConstants.DOUBLE_QUOTES);
            return newField.toString();
        }else{
            return field;
        }
    }
	@RequestMapping(value ="/cashAccountForfeituresReport/", method = {RequestMethod.GET,RequestMethod.POST})
	public String doDefault(@Valid @ModelAttribute("cashAccountForfeituresForm") CashAccountForfeituresReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_KEY);
			TransactionDetailsACIReportData report = (TransactionDetailsACIReportData) request
					.getAttribute(CommonConstants.REPORT_BEAN);
			//TODO
		}
		String forward =  super.doDefault(form, request, response);
		return StringUtils.contains(forward,'/')?forwards.get(forward):forwards.get(forward);
	}
	
	 @RequestMapping(value ="/cashAccountForfeituresReport/" ,params={"task=download"}  , method =  {RequestMethod.GET})	
	 public String doDownload (@Valid @ModelAttribute("cashAccountForfeituresForm") CashAccountForfeituresReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	
	 @RequestMapping(value ="/cashAccountForfeituresReport/" ,params={"task=sort"}  , method =  {RequestMethod.GET})	
	 public String doSort(@Valid @ModelAttribute("cashAccountForfeituresForm") CashAccountForfeituresReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	 @RequestMapping(value ="/cashAccountForfeituresReport/" ,params={"task=filter"}  , method =  {RequestMethod.POST})	
	 public String doFilter (@Valid @ModelAttribute("cashAccountForfeituresForm") CashAccountForfeituresReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
