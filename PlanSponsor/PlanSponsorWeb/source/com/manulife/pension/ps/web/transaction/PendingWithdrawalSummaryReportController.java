package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;	
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.HashMap;
import javax.validation.Valid;


import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.ps.service.report.transaction.reporthandler.PendingWithdrawalSummaryReportHandler;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;

import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;

import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.participant.transaction.valueobject.PendingWithdrawalSummaryReportData;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalDetailsReportItem;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalGeneralInfoVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalPayeePaymentVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.SSNRender;

/**
 * Action class to populate the Pending Withdrawal Summary Report page. The following action class
 * will perform the summary report and download the summary report actions.
 *  
 * @author Puttaiah Arugunta
 *
 */
@Controller
@RequestMapping( value ="/transaction")
@SessionAttributes({"pendingWithdrawalSummaryForm"})

public class PendingWithdrawalSummaryReportController extends AbstractTransactionReportController{
	 @ModelAttribute("pendingWithdrawalSummaryForm") 
	 public PendingWithdrawalSummaryForm populateForm() 
	 {
		 return new PendingWithdrawalSummaryForm();
		 }

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/pendingWithdrawalSummaryReport.jsp");
		forwards.put("default","/transaction/pendingWithdrawalSummaryReport.jsp");
		forwards.put("sort","/transaction/pendingWithdrawalSummaryReport.jsp");
		forwards.put("filter","/transaction/pendingWithdrawalSummaryReport.jsp");
		forwards.put("page","/transaction/pendingWithdrawalSummaryReport.jsp");
		forwards.put("print","/transaction/pendingWithdrawalSummaryReport.jsp");
		forwards.put("gotoDetailsPage","redirect:/do/transaction/pendingWithdrawalDetailsReport/");} 

	private static final String PENDING_WITHDRAWAL_SUMMARY_FORM = "pendingWithdrawalSummaryForm";

	private static final String DEFAULT_SORT_FIELD = PendingWithdrawalSummaryReportData.SORT_FIELD_DATE;
	
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;
	
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe		
	private FastDateFormat simpleDateFormat = FastDateFormat.getInstance("MM/dd/yyyy", Locale.US);

	/**
	 * Default Constructor
	 */
	public PendingWithdrawalSummaryReportController() {
		super(PendingWithdrawalSummaryReportController.class);
	}
	
	/**
	 * Get  the default sort order
	 */
	@Override
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/**
	 * Get the default sort direction
	 */
	@Override
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	/**
	 * Method to download the data to CSV file
	 * @param reportForm
	 * @param report
	 * @param request
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request) throws SystemException {
		
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		PendingWithdrawalSummaryForm form  = (PendingWithdrawalSummaryForm) reportForm;
		
		StringBuffer buffer = new StringBuffer();
		
		//DFS TRW .27:check whether SSN should be masked or not in download report.
		boolean maskSSN = true;
		try {
			maskSSN = ReportDownloadHelper.isMaskedSsn(userProfile, 
					currentContract.getContractNumber());
		} catch (SystemException se) {
			// log exception and output blank ssn
			logger.error(se);
			throw se;
		}
		
		// 1. Contract Number
		buffer.append("Contract").append(COMMA);
		buffer.append(currentContract.getContractNumber()).append(COMMA);
		
		// 2. Contract name
		buffer.append(currentContract.getCompanyName());
		buffer.append(LINE_BREAK);
		
		// 3. Selected from Date
		buffer.append("From Date").append(COMMA).append(form.getFromDate())
			.append(COMMA);
		
		// 4. Selected to Date
		buffer.append("To Date").append(COMMA).append(form.getToDate());
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		
		// create the columns 
		buffer.append("Transaction date").append(COMMA);
		buffer.append("Description line 1").append(COMMA);

		if(!currentContract.isDefinedBenefitContract()){
			buffer.append("SSN").append(COMMA);
			buffer.append("Participant name").append(COMMA);
		}

		// create Payee related columns
		buffer.append("Transaction number").append(COMMA)
			.append("Payment to").append(COMMA)
			.append("Payment method").append(COMMA)
			.append("Payee").append(COMMA)
			.append("Address Line 1").append(COMMA)
			.append("Address Line 2").append(COMMA)
			.append("City").append(COMMA)
			.append("State").append(COMMA)
			.append("Zip").append(COMMA)
			.append("Country").append(COMMA)
			.append("Bank/Branch name").append(COMMA)
			.append("ABA/Routing number").append(COMMA)
			.append("Bank account").append(COMMA)
			.append("Account number").append(COMMA)
			.append("Credit party name").append(COMMA);
		
		buffer.append(LINE_BREAK);
		
		WithdrawalGeneralInfoVO withdrawalGeneralInfoVO = null;
		List<WithdrawalPayeePaymentVO> PayeePaymentVOList = null;
		PendingWithdrawalSummaryReportData reportData = (PendingWithdrawalSummaryReportData) report;
		
		if(reportData != null && reportData.getDetails() != null
				&& !reportData.getDetails().isEmpty()){

			Collection<WithdrawalDetailsReportItem> details = reportData.getDetails();

			for(WithdrawalDetailsReportItem withdrawalDetailsReportItem : details) {

				withdrawalGeneralInfoVO = 
					withdrawalDetailsReportItem.getWithdrawalGeneralInfoVO();

				StringBuffer generalInfo = new StringBuffer();

				// 5. Transacion Date
				if(withdrawalGeneralInfoVO.getWithdrawalDate() != null){
					generalInfo.append(withdrawalGeneralInfoVO.getWithdrawalDate());
				}
				generalInfo.append(COMMA);

				// 6. Description line 1
				generalInfo.append("Pending withdrawal of additional contributions")
				.append(COMMA);

				// 7. Description 2 only for DC contracts			
				if(!currentContract.isDefinedBenefitContract()){
					generalInfo.append(SSNRender.format(
							withdrawalGeneralInfoVO.getSsn(), "", maskSSN)).append(COMMA); 
					generalInfo.append(escapeField(withdrawalGeneralInfoVO.getName())).append(COMMA); 
				}

				// 8. Transaction number
				generalInfo.append(withdrawalGeneralInfoVO.getTransactionNumber());

				// Payee Information
				PayeePaymentVOList = withdrawalDetailsReportItem.getWithdrawalPayeePaymentVO();
				if(PayeePaymentVOList != null ){
					for(WithdrawalPayeePaymentVO withdrawalPayeePaymentVO:PayeePaymentVOList){

						buffer.append(generalInfo.toString())
						.append(COMMA)
						// 9. Payment To
						.append(withdrawalPayeePaymentVO.getPaymentTo())
						.append(COMMA)

						// 10. Payment Method
						.append(withdrawalPayeePaymentVO.getPaymentMethod())
						.append(COMMA)

						// 11. Paymee Name
						.append(escapeField(withdrawalPayeePaymentVO.getPayeeName()))
						.append(COMMA)

						// 12. Address Line 1
						.append(escapeField(withdrawalPayeePaymentVO.getAddressLine1())) 
						.append(COMMA)

						// 13. Address Line 2							
						.append(escapeField(withdrawalPayeePaymentVO.getAddressLine2()))
						.append(COMMA)

						// 14. city
						.append(withdrawalPayeePaymentVO.getCity())
						.append(COMMA)

						// 15. state
						.append(withdrawalPayeePaymentVO.getState())
						.append(COMMA)

						// 16. Zip
						.append(withdrawalPayeePaymentVO.getZip())
						.append(COMMA)

						// 17. Country
						.append(withdrawalPayeePaymentVO.getCountry())
						.append(COMMA)

						// 18. Bank/Branch Name
						.append(withdrawalPayeePaymentVO.getBankBranchName())
						.append(COMMA)

						// 19. ABA/Routing number
						.append(withdrawalPayeePaymentVO.getRoutingABAnumber())
						.append(COMMA)

						// 20. Bank account type
						.append(withdrawalPayeePaymentVO.getAccountType())
						.append(COMMA);

						// 21. Account number
						if(StringUtils.isNotBlank(withdrawalPayeePaymentVO.getAccountNumber())){
							buffer.append(CommonConstants.MASK_ACCOUNT_NUMBER)
									.append(COMMA);
						}else{
							buffer.append(COMMA);
						}
						
						// 22. Credit party name
						buffer.append(escapeField(withdrawalPayeePaymentVO.getCreditPayeeName()))
						.append(COMMA);

						buffer.append(LINE_BREAK);
					}
				}else{
					// If payee payment information is not available then display the general information alone.
					buffer.append(generalInfo.toString())
					.append(LINE_BREAK);
				}
			}
		}else{
			//If There were no transactions for the date range selected then display a message
			// "There were no transactions for the date range and type selected.  Please try again."
			buffer.append(getMessage(ContentConstants.MESSAGE_NO_PENDING_TRANSACTION_HISTORY_FOR_DATE_SELECTED)).append(LINE_BREAK);
		}
		
		// Buffered data return as byte.
		return buffer.toString().getBytes();
	}

	/**
	 * Get the report Id
	 */
	@Override
	protected String getReportId() {
		return PendingWithdrawalSummaryReportHandler.class.getName();
	}

	/**
	 * Get the Report Name
	 */
	@Override
	protected String getReportName() {
		return PendingWithdrawalSummaryReportData.REPORT_NAME;
	}

	/**
	 * Participant id ,transaction number,contract id added in 
	 * report criteria object.
	 * 
	 * @param criteria
	 * @param actionForm
	 * @param request
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm actionForm,
			HttpServletRequest request)	throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		PendingWithdrawalSummaryForm pendingWithdrawalSummaryForm = 
			(PendingWithdrawalSummaryForm) actionForm;

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		// add the proposal number to the filter criteria, which will be
		// used to execute the SQLs
		criteria.addFilter(
				PendingWithdrawalSummaryReportData.FILTER_PROPOSAL_NUMBER,
				currentContract.getProposalNumber());

		// add the from & to date to the report criteria
		if (pendingWithdrawalSummaryForm != null ) {
			
			try {
				//Added sort indicator to true  for change in transaction date sort
				if(PendingWithdrawalSummaryReportData.SORT_FIELD_DATE.equalsIgnoreCase(
						pendingWithdrawalSummaryForm.getSortField())) {
					criteria.addFilter(
							PendingWithdrawalSummaryReportData.SORT_BY_TRANSACTION_DATE,
							Boolean.TRUE);
				} else {
					criteria.addFilter(
							PendingWithdrawalSummaryReportData.SORT_BY_TRANSACTION_DATE,
							Boolean.FALSE);
				}
				
				//Added Default date indicator to true  for default date range
				if( pendingWithdrawalSummaryForm.getFromDate().equals(
						pendingWithdrawalSummaryForm.getDefaultFromDate()) &&
						pendingWithdrawalSummaryForm.getToDate().equals(
								pendingWithdrawalSummaryForm.getDefaultToDate()) ){
					criteria.addFilter(
							PendingWithdrawalSummaryReportData.DEFAULT_DATE_IND, 
							Boolean.TRUE);
				}else{
					criteria.addFilter(
							PendingWithdrawalSummaryReportData.DEFAULT_DATE_IND, 
							Boolean.FALSE);
				}

				// if the from date is available in the form, then set the
				// from date to the criteria map
				if (StringUtils.isNotBlank(
						pendingWithdrawalSummaryForm.getFromDate())) {
					
					Date fromDate = simpleDateFormat.parse(
							pendingWithdrawalSummaryForm.getFromDate());
					
					criteria.addFilter(
							PendingWithdrawalSummaryReportData.FILTER_FROM_DATE, 
							fromDate);
				}
				
				// if the To date is available in the form, then set the
				// from date to the criteria map
				if (StringUtils.isNotBlank(
						pendingWithdrawalSummaryForm.getToDate())) {
					
					Date toDate = simpleDateFormat.parse(
							pendingWithdrawalSummaryForm.getToDate());
					
					criteria.addFilter(
							PendingWithdrawalSummaryReportData.FILTER_TO_DATE,
							toDate);
				}
			} catch (ParseException e) {
				List errors = new ArrayList();
				errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
				setErrorsInRequest(request, errors);
				throw new SystemException("Exception occured while calculating Dates");
			}
		}
		
		// the default dates logic is done in the doValidate method
		
		// if the contract is a DB contract, add the DB contract filter
		// to the criteria map with TRUE as the value
		if (userProfile.getCurrentContract().isDefinedBenefitContract()) {
			criteria.addFilter(
					PendingWithdrawalSummaryReportData.CONTRACT_TYPE_DB, 
					Boolean.TRUE);
		}
		
		String task = getTask(request);
		
		// If it is a download task then set the required filter variable
		if(DOWNLOAD_TASK.equals(task)){
			criteria.addFilter(PendingWithdrawalSummaryReportData.FILTER_TASK, 
					PendingWithdrawalSummaryReportData.CSV_DOWNLOAD);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}		
	}
	@RequestMapping(value ="/pendingTransactionHistoryReport/" , method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("pendingWithdrawalSummaryForm") PendingWithdrawalSummaryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		preExecute(form, request, response);
		if(bindingResult.hasErrors()){
			Object selectedTxnNumber = null;
			request.getSession(false).setAttribute(Constants.TXN_NUMBER_KEY, selectedTxnNumber);
			Object selectedParticipant = null;
			request.getSession(false).setAttribute(Constants.PARTICIPANT_ID_KEY, selectedParticipant);
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		
	       String forward=super.doDefault(form,request,response);
	       
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
		
		
		
		 @RequestMapping(value ="/pendingTransactionHistoryReport/" ,params={"task=filter"} , method ={RequestMethod.GET,RequestMethod.POST}) 
		    public String doFilter (@Valid @ModelAttribute("pendingWithdrawalSummaryForm") PendingWithdrawalSummaryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		    throws IOException,ServletException, SystemException {
			 Collection errors =doValidate( form, request);
			 if(errors.size()>0) 
				{
					BaseSessionHelper.setErrorsInSession(request, errors);
					return forwards.get("input");
				}
			 if(bindingResult.hasErrors()){
				 Object selectedTxnNumber = null;
				 request.getSession(false).setAttribute(Constants.TXN_NUMBER_KEY, selectedTxnNumber);
				 Object selectedParticipant = null;
				 request.getSession(false).setAttribute(Constants.PARTICIPANT_ID_KEY, selectedParticipant);
				 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				 if(errDirect!=null){
					 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					 return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
				 }
			 }
			       String forward=super.doFilter( form, request, response);
					return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		 }
		    
		    	 @RequestMapping(value ="/pendingTransactionHistoryReport/" ,params={"task=page"}  , method =  {RequestMethod.GET}) 
		    	    public String doPage (@Valid @ModelAttribute("pendingWithdrawalSummaryForm") PendingWithdrawalSummaryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		    	    throws IOException,ServletException, SystemException {
		    		 doValidate(form, request);
		    	    	if(bindingResult.hasErrors()){
		    	    		Object selectedTxnNumber = null;
		    				request.getSession(false).setAttribute(Constants.TXN_NUMBER_KEY, selectedTxnNumber);
		    				Object selectedParticipant = null;
		    				request.getSession(false).setAttribute(Constants.PARTICIPANT_ID_KEY, selectedParticipant);
		    		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		    		       if(errDirect!=null){
		    		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		    		              return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
		    		       }
		    			}
		    		       String forward=super.doPage( form, request, response);
		    				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		    	 }
		    	
		    		 @RequestMapping(value ="/pendingTransactionHistoryReport/" ,params={"task=sort"}  , method =  {RequestMethod.GET}) 
		     	    public String doSort (@Valid @ModelAttribute("pendingWithdrawalSummaryForm") PendingWithdrawalSummaryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		     	    throws IOException,ServletException, SystemException {
		    			 doValidate(form, request);
		     	    	if(bindingResult.hasErrors()){
		     	    		Object selectedTxnNumber = null;
		     				request.getSession(false).setAttribute(Constants.TXN_NUMBER_KEY, selectedTxnNumber);
		     				Object selectedParticipant = null;
		     				request.getSession(false).setAttribute(Constants.PARTICIPANT_ID_KEY, selectedParticipant);
		     		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		     		       if(errDirect!=null){
		     		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		     		             return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
		     		       }
		     			}
		     		       String forward=super.doSort( form, request, response);
		     				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		     	 }
		    		 @RequestMapping(value ="/pendingTransactionHistoryReport/" ,params={"task=download"}  , method =  {RequestMethod.GET})	
		    	 public String doDownload (@Valid @ModelAttribute("pendingWithdrawalSummaryForm") PendingWithdrawalSummaryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		    			     throws IOException,ServletException, SystemException {
		    			 doValidate(form, request);
		    			 if(bindingResult.hasErrors()){
		    				 Object selectedTxnNumber = null;
		    				 request.getSession(false).setAttribute(Constants.TXN_NUMBER_KEY, selectedTxnNumber);
		    				 Object selectedParticipant = null;
		    				 request.getSession(false).setAttribute(Constants.PARTICIPANT_ID_KEY, selectedParticipant);
		    				 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		    				 if(errDirect!=null){
		    					 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		    					 return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
		    				 }
		    			 }
		    			     String forward=super.doDownload( form, request, response);
		    			     return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		    			    }
		    	
		    		 @RequestMapping(value ="/pendingTransactionHistoryReport/" ,params={"task=downloadAll"}  , method =  {RequestMethod.GET})
		    		 public String doDownloadAll (@Valid @ModelAttribute("pendingWithdrawalSummaryForm") PendingWithdrawalSummaryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		    			     throws IOException,ServletException, SystemException {
		    			 doValidate(form, request);
		    			 if(bindingResult.hasErrors()){
		    				 Object selectedTxnNumber = null;
		    				 request.getSession(false).setAttribute(Constants.TXN_NUMBER_KEY, selectedTxnNumber);
		    				 Object selectedParticipant = null;
		    				 request.getSession(false).setAttribute(Constants.PARTICIPANT_ID_KEY, selectedParticipant);
		    				 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		    				 if(errDirect!=null){
		    					 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		    					 return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
		    				 }
		    			 }
		    			     String forward=super.doDownloadAll( form, request, response);
		    			     return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		    			    }  
		    		 @RequestMapping(value = "/pendingTransactionHistoryReport/", params = {"task=printPDF"}, method = {RequestMethod.GET })
		    			public String doPrintPDF(@Valid @ModelAttribute("pendingWithdrawalSummaryForm") PendingWithdrawalSummaryForm form,
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
		    						return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
		    																												// default
		    					}
		    				}
		    				 forward = super.doPrintPDF(form, request, response);
		    				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		    			}
		    		 @RequestMapping(value = "/pendingTransactionHistoryReport/", params = {"task=print"}, method = {RequestMethod.GET })
		    			public String doPrint(@Valid @ModelAttribute("pendingWithdrawalSummaryForm") PendingWithdrawalSummaryForm form,
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
		    						return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
		    																												// default
		    					}
		    				}
		    				 forward = super.doPrint(form, request, response);
		    				return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
		    			}
	/**
	 * Validate the input action form. FROM date must be less than TO date.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("unchecked")
	protected Collection doValidate(
			ActionForm form,HttpServletRequest request){

		Collection errors = super.doValidate( form, request);
		PendingWithdrawalSummaryForm actionForm = 
			(PendingWithdrawalSummaryForm) form;
		
		// populate the default dates
		populateDefaultDates(request, errors, actionForm);

		try {
			Date fromDate = null;
			if (StringUtils.isNotBlank(actionForm.getFromDate())) {
				fromDate = simpleDateFormat.parse(actionForm.getFromDate());
				
				//Validating the from date for the below scenario
				//scenario 1:if date is enter as 13/01/2010 
				//scenario 2:if date is enter as 4/31/2010 
				//scenario 3:if date is enter as 06/30/20J0 
				//scenario 4:if date is enter as 01/00/2010
				//scenario 5:if date is enter as 01/01/0000
				//for all those scenario's ,throw parse exception with message as "Invalid Date"
				if (! simpleDateFormat.format(fromDate).equals(actionForm.getFromDate())) {
					throw new ParseException("Invalid Date", 0);
				}
				
			} else {
				// from date is blank so add error
				errors.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY));
			}
			
			Date toDate = null;
			if (StringUtils.isNotBlank(actionForm.getToDate())) {
				toDate = simpleDateFormat.parse(actionForm.getToDate());
				//Validating the from and to dates  for the below scenario
				//scenario 1:if date is enter as 13/01/2010 
				//scenario 2:if date is enter as 4/31/2010 
				//scenario 3:if date is enter as 06/30/20J0 
				//scenario 4:if date is enter as 01/00/2010
				//scenario 5:if date is enter as 01/01/0000
				//for all those scenario's ,throw parse exception with message as "Invalid Date"
				if ( ! simpleDateFormat.format(toDate).equals(actionForm.getToDate())) {
					throw new ParseException("Invalid Date", 0);
				}
				
			}
			
			if (fromDate != null) {

				Calendar calFromDate = Calendar.getInstance();
				Calendar contractEffDate = Calendar.getInstance();
				calFromDate.setTime(fromDate);

				// get the contract effective date for validation
				Date contractEffectiveDate = getUserProfile(request).getCurrentContract().getEffectiveDate();
				contractEffDate.setTime(contractEffectiveDate);
				if ( calFromDate.before(contractEffDate)) {
					// from date is < contract effective date, so add error
					errors.add(new GenericException(ErrorCodes.ERROR_INVALID_FROM_DATE_WITH_CONT_EFF_DATE));
				}
				
				if (toDate != null) {
					if (fromDate.after(toDate)) {
						// from date is > to date, so add error
						errors.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
					}
					
					Calendar calToDate = Calendar.getInstance();
					calToDate.setTime(toDate);
					calToDate.add(Calendar.MONTH, -12);
					
					if (calFromDate.before(calToDate)) {
						// From & To date range is > 12 months, so add error
						errors.add(new GenericException(ErrorCodes.ERROR_INVALID_DATES_RANGE));
					}
				}				
			}
		} catch(ParseException pe) {
			// From date or To date is invalid
			errors.add(new GenericException(ErrorCodes.INVALID_DATE));
		} 
		
		// Resets the information for JSP to display.
		if (errors.size() > 0) {
			// Re-populates action form and request with default information.
			//populateReportForm(mapping, actionForm, request);
			// signal the JSP to display the date section again for the user to change
			// their selection
			
			request.setAttribute("displayDates", "true");   
		}
		return errors;
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
		
		populateDefaultDates(request, new ArrayList(), reportForm);
	}
	
	/**
	 * Method to populate the default from date and to dates while the user 
	 * enters the page or sets the default to date if not provided.  
	 * 
	 * @param request
	 * @param errors
	 * @param actionForm
	 */
	@SuppressWarnings("unchecked")
	private void populateDefaultDates(HttpServletRequest request,
			Collection errors, BaseReportForm actionForm) {
		
		PendingWithdrawalSummaryForm form = 
			(PendingWithdrawalSummaryForm) actionForm;
		
		if(StringUtils.isBlank(form.getFromDate()) 
				&& DEFAULT_TASK.equals(getTask(request))){
			 Date fromDate = null;
			try {
				fromDate = ContractDateHelper.determinePendingSummaryFromDate(
						 getUserProfile(request).getCurrentContract());
				form.setDefaultFromDate(simpleDateFormat.format(fromDate));
			} catch (SystemException e) {
				 errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
			}
			 
			 if (fromDate != null) {
				 form.setFromDate(simpleDateFormat.format(fromDate)); 
			 } else {
				 errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
			 }
		}
		
		// PWS.27	For “to” date, system will default to current date 
		// plus 10 business days.
		if(StringUtils.isBlank(form.getToDate())
				&& StringUtils.isNotBlank(form.getFromDate())){ // No need to calculate the To date if From date is blank
			Calendar toCal = Calendar.getInstance();		 
			toCal.add(Calendar.DATE, 10);	

			Date fromDate = null;
			try {
				fromDate = simpleDateFormat.parse(form.getFromDate());
			} catch (ParseException e) {
				errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
			}
			
			Date toDate = null;
			try {
				toDate = ContractDateHelper.determinePendingSummaryToDate();
				form.setDefaultToDate(simpleDateFormat.format(toDate));
			} catch (SystemException e) {
				 errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
			}
			//PWS.34	If “to” date is left blank, then system will set to the default “to” date – see PWS.27. 
			//However, if the default “to” date will make “from” and “to” date range > 12 months period,
			//or make “from” after the “to” date, then, set the “to” date to “from” date + 12 months period to no greater than ‘9999-12-31’.
			Calendar calToDate = Calendar.getInstance();
			calToDate.setTime(toDate);
			calToDate.add(Calendar.MONTH, -12);
			
			Calendar calToDefaultDate = Calendar.getInstance();
			calToDefaultDate.set(9998, Calendar.DECEMBER, 31);
			//Check fromDate is in year 9999 ,then set the toDate to 31st December 9999
			//else set the toData according to Req. no. PWS.34.
			if(fromDate.after(calToDefaultDate.getTime())) {
				toCal.set(9999, Calendar.DECEMBER, 31);
				toDate = toCal.getTime();
			}else if (fromDate.after(toDate)
					||  fromDate.before(calToDate.getTime() ) )  {
				toCal.setTime(fromDate);	
				toCal.add(Calendar.MONTH, 12);
				toDate = toCal.getTime();
			}
			
			form.setToDate(simpleDateFormat.format(toDate));
		}
	}




	/**
	 * If it's sort by Date, secondary sort is transaction number. 
	 * If it's sort by number, secondary sort is date.
	 * 
	 */
	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		criteria.insertSort(sortField, sortDirection);
		if (sortField.equals(
				PendingWithdrawalSummaryReportData.SORT_FIELD_DATE)) {
			
			// secondary sort is by transaction number
			criteria.insertSort(
					PendingWithdrawalSummaryReportData.SORT_FIELD_NUMBER,
					sortDirection);
			
		} else if (sortField.equals(
				PendingWithdrawalSummaryReportData.SORT_FIELD_NUMBER)) {

			// secondary sort is by transaction date
			criteria.insertSort(
					PendingWithdrawalSummaryReportData.SORT_FIELD_DATE,
					sortDirection);
		}
	}
	
	 /**
     * Gets the current task for this request.
     *
     * @param request
     *            The current request object.
     * @return The task for this request.
     */
 	protected String getTask(HttpServletRequest request) {
        String task = null;
        
        PendingWithdrawalSummaryForm pendingWithdrawalSummaryForm = 
        	(PendingWithdrawalSummaryForm) request.getSession().getAttribute(
        			PENDING_WITHDRAWAL_SUMMARY_FORM);
        
        if (pendingWithdrawalSummaryForm != null
        		&& pendingWithdrawalSummaryForm.getTask() != null) {
            task = pendingWithdrawalSummaryForm.getTask() ;
        } else {
        	task = DEFAULT_TASK;
        }
        
        return task;
    }
    
	/**
	 * This method will 
	 * 		1. get the selected transaction number from the form
	 * 		2. sets the transaction number in session and
	 * 		3. redirects to the pending details action
	 *  
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @throws SystemException
	 */
 	@RequestMapping(value ="/pendingTransactionHistoryReport/",  method =  {RequestMethod.POST}) 
 	public String doGotoDetailsPage(@Valid @ModelAttribute("pendingWithdrawalSummaryForm") PendingWithdrawalSummaryForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 	throws IOException,ServletException, SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doGotoDetailsPage");
		}
		
		
		if (actionForm != null) {
			// get the transaction number from the actionForm
			String selectedTxnNumber = 
					actionForm.getSelectedTxnNumber();
			
			// get the participant number from the actionForm
			String selectedParticipant =
					actionForm.getSelectedParticipant();
			
			// check for pendingWithdrawalSummaryForm already in session scope
			String sessionTxnNumber = 
				(String) request.getSession(false).getAttribute(Constants.TXN_NUMBER_KEY);

			String sessionParticipant = 
				(String) request.getSession(false).getAttribute(Constants.PARTICIPANT_ID_KEY);
			
			// if the transaction number is in session, then remove it 
			if (sessionTxnNumber != null) {
				request.getSession(false).removeAttribute(Constants.TXN_NUMBER_KEY);
			}
			// if the participant number is in session, then remove it 
			if (sessionParticipant != null) {
				request.getSession(false).removeAttribute(Constants.PARTICIPANT_ID_KEY);
			}
			
			// set the selected transaction number in session
			request.getSession(false).setAttribute(Constants.TXN_NUMBER_KEY, selectedTxnNumber);
			request.getSession(false).setAttribute(Constants.PARTICIPANT_ID_KEY, selectedParticipant);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("end -> doGotoDetailsPage");
		}

		return forwards.get(getTask(request));
	}
    /**
     * Method to get the Content Message for the provided content Id.
     * 
     * @param int contentId
     * @return String content text
     * @throws SystemException
     */
    private static String getMessage(int contetentId) throws SystemException {
    	String text = null;
        try {
        	
        	  Miscellaneous message = (Miscellaneous) (ContentCacheManager.getInstance()).getContentById(
                      contetentId, ContentTypeManager.instance().MISCELLANEOUS);
              if(message != null)
            	  text =  message.getText();
              
        } catch (ContentException e) {
            throw new SystemException (e.toString() +
                "PendingWithdrawalSummaryReportAction" + "getMessage" + "error getting error text");
        }
        return text;
    }
    /**
     * Method to display comma(,) in downloaded spread sheet.
     * @param field String
     * @return field String
     */
    private String escapeField(String field){
        
    	if(field.indexOf(COMMA) != -1 ){
            StringBuffer newField = new StringBuffer();
            newField = newField.append(QUOTE).append(field).append(QUOTE);
            return newField.toString();
        }else{
            return field;
        }
    }
    
    /**
     * Method to remove "from" date and "to" date from PendingWithdrawalSummaryForm
   	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws SystemException
     */
    protected String preExecute( ActionForm form,
    		HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, SystemException  {

    	if (logger.isDebugEnabled()) {
    		logger.debug("entry ->preExecute");
    	}
    	getApplicationFacade(request).actionPreExecute(request);
    	PendingWithdrawalSummaryForm pendingWithdrawalSummaryForm = 
    		(PendingWithdrawalSummaryForm) form;
    	if( pendingWithdrawalSummaryForm.getTask() == null ) {
    		pendingWithdrawalSummaryForm.setFromDate(null);
    		pendingWithdrawalSummaryForm.setToDate(null);
    	}

    	if (logger.isDebugEnabled()) {
    		logger.debug("end -> preExecute");
    	}
    	return null;
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
	/*@SuppressWarnings({ "rawtypes" })
	protected ActionForward validate(ActionMapping mapping, Form form,
			HttpServletRequest request) {
		Collection penErrors = PsValidation.doValidatePenTestAutoAction(form,
				mapping, request, CommonConstants.INPUT);
		if (penErrors != null && penErrors.size() > 0) {
			Object selectedTxnNumber = null;
			request.getSession(false).setAttribute(Constants.TXN_NUMBER_KEY, selectedTxnNumber);
			Object selectedParticipant = null;
			request.getSession(false).setAttribute(Constants.PARTICIPANT_ID_KEY, selectedParticipant);
			return new ActionForward(CommonConstants.ERROR_RDRCT, "/do"
					+ mapping.getPath(), true);
		}
		return super.validate(mapping, form, request);
	}*/
    @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
   
}
