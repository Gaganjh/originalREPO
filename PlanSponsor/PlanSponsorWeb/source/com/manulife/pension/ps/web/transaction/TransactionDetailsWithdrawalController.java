package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.report.TransactionDetailsWithdrawalForm;
import com.manulife.pension.platform.web.report.util.WithdrawalDetailsUtility;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanSummaryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
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
import com.manulife.pension.util.log.LogUtility;

/**
 * Action class for Completed Withdrawal Details page
 * 
 * @author Tamilarasu Krishnamoorthy
 * 
 */
@Controller
@RequestMapping(value ="/transaction")
@SessionAttributes({"transactionDetailsWithdrawalForm"})

public class TransactionDetailsWithdrawalController extends ReportController {

	@ModelAttribute("transactionDetailsWithdrawalForm")
	public TransactionDetailsWithdrawalForm populateForm()
	{
		return new TransactionDetailsWithdrawalForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/withdrawalTransactionReport.jsp");
		forwards.put("default","/transaction/withdrawalTransactionReport.jsp");
		forwards.put("print","/transaction/withdrawalTransactionReport.jsp");
		}

	/**
	 * Default Constructor
	 */
	public TransactionDetailsWithdrawalController() {
		super(TransactionDetailsWithdrawalController.class);
	}	

	/**
	 * Download complete withdrawal information for the withdrawal transaction 
	 * with respect to transactionNumber and participantId.
	 * 
	 * Note:
	 *  PSW & FRW Common download functionality are coded in 
	 *  withdrawalDetailsUtility class 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @throws SystemException
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
	throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getDownloadData");
		}
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		String message = "";
		String bodyHeader = "";
		Content layoutContent=null;
		 byte[] downloadData=null;
		 List<String> withdrawalFieldLabels = new ArrayList<String>();
		
			
		//DFS TRW .27:check whether SSN should be masked or not in download report.
		boolean maskSSN = true;
		try{
			//Withdrawal general information section label
			withdrawalFieldLabels.add(0,"Payment amount");
			withdrawalFieldLabels.add(1,"Transaction type");  //Obsolete
			withdrawalFieldLabels.add(2,"Type of withdrawal");
			withdrawalFieldLabels.add(3,"Name");
			withdrawalFieldLabels.add(4,"SSN");
			withdrawalFieldLabels.add(5,"Withdrawal date");
			withdrawalFieldLabels.add(6,"Transaction number");
			//Payee Payment section label
			withdrawalFieldLabels.add(7,"Payment to");
			withdrawalFieldLabels.add(8,"Payment method");
			withdrawalFieldLabels.add(9,"Bank account");
			withdrawalFieldLabels.add(10,"Payee address");
			withdrawalFieldLabels.add(11, "Bank details");
			withdrawalFieldLabels.add(12, "Bank / Branch name");
			withdrawalFieldLabels.add(13, "ABA / Routing number");
			withdrawalFieldLabels.add(14, "Account number"); //Obsolete
			withdrawalFieldLabels.add(15, "Credit party name");
			//Withdrawal Detail information section label
			withdrawalFieldLabels.add(16, "Money type");
			withdrawalFieldLabels.add(17, "Withdrawal amount($)");
			withdrawalFieldLabels.add(18, "Account balance($)");
			withdrawalFieldLabels.add(19, "Vesting(%)");
			withdrawalFieldLabels.add(20, "Available amount($)");
			withdrawalFieldLabels.add(21, "Total withdrawal amount");
			withdrawalFieldLabels.add(22, "Total available amount");
			withdrawalFieldLabels.add(23, "Market value adjustment(MVA)");
			withdrawalFieldLabels.add(24, "Funds on deposit interest");
			withdrawalFieldLabels.add(25, "Taxable amount");
			withdrawalFieldLabels.add(26, "State tax");
			withdrawalFieldLabels.add(27, "Federal tax");
			withdrawalFieldLabels.add(28, "Taxable amount - ROTH");
			withdrawalFieldLabels.add(29, "State tax - ROTH");
			withdrawalFieldLabels.add(30, "Federal tax - ROTH");
			withdrawalFieldLabels.add(31, "Total payment amount");
		
			layoutContent = ContentCacheManager.getInstance().getContentById(
					ContentConstants.WITHDRAWAL_TRANSACTION_DETAIL ,
					ContentTypeManager.instance().LAYOUT_PAGE);
			//Withdrawal general information  section header
			bodyHeader = ContentUtility.getContentAttribute(layoutContent, "body1Header");
			withdrawalFieldLabels.add(32, bodyHeader);
			//Payee Payment section header
			bodyHeader = ContentUtility.getContentAttribute(layoutContent, "body2Header");
			
			
			
			withdrawalFieldLabels.add(33, bodyHeader);
			//Withdrawal Detail information header
			bodyHeader = ContentUtility.getContentAttribute(layoutContent, "body3Header");
			withdrawalFieldLabels.add(34, bodyHeader);
		
			
			//wilange
			withdrawalFieldLabels.add(35, "1st Year of Designated Roth Contributions");
			withdrawalFieldLabels.add(36, "Pre-87 After Tax Employee Contributions Withdrawn");
			withdrawalFieldLabels.add(37 ,"Payee name");
			withdrawalFieldLabels.add(38 ,"Money type");
			withdrawalFieldLabels.add(39 ,"Net contributions ($)");
			withdrawalFieldLabels.add(40 ,"Net earnings ($)");
			withdrawalFieldLabels.add(41 ,"Withdrawal amount ($)");
			withdrawalFieldLabels.add(42 ,"After Tax Cost Basis");
			
			
			maskSSN = ReportDownloadHelper.isMaskedSsn(
					userProfile, currentContract.getContractNumber());
			message = ContentHelper.getContentText(
					ContentConstants.CORRECTION_INDICATOR, 
					ContentTypeManager.instance().MESSAGE, null);
			downloadData=WithdrawalDetailsUtility.getDownloadData(reportForm, 
					report, request, currentContract, maskSSN, message,
					withdrawalFieldLabels, Constants.PS_APPLICATION_ID);

		} catch (SystemException se) {
			logger.error(se);
			// log exception and output blank ssn
		}catch(Exception e) {
			logger.error(e);
		}
		
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getDownloadData");
		}
		
		//return  withdrawal information  in byte[] object
		return downloadData;
	}

	/**
	 * Get the report Id for reportCriteria.
	 */
	protected String getReportId() {
		return WithdrawalDetailsUtility.getReportId();		
	}

	/**
	 * Get the Report Name
	 */
	protected String getReportName() {
		return WithdrawalDetailsUtility.getReportName();

	}

	/**
	 * Set Participant id ,transaction number,contract id in 
	 * report criteria object.
	 * 
	 * @param criteria
	 * @param actionForm
	 * @param request
	 * @throws SystemException
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm actionForm, HttpServletRequest request)
	throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		WithdrawalDetailsUtility.populateReportCriteria(criteria, 
				actionForm, request, currentContract);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}
	}

	/**
	 * This method will retrieve the data from APOLLO and 
	 * 	1. Sets the value for TransactionDetailsWithdrawalForm 
	 * 		from reportData
	 *  2. Checks for correctionIndicator to display error message.
	 * 	3. And sets reportData and formBean in request.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @throws  SystemException
	 */
	 
	public String doCommon(BaseReportForm form, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		
		List<GenericException> errors = new ArrayList<GenericException>();
		String forward = null;
		
		try {
			forward = super.doCommon( form, request, response);
			WithdrawalDetailsUtility.doCommon(
					 form, request, response,"");
			
		} catch (SystemException e) {
			// Log the system exception.
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,e);

			// Show user friendly message.
			errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
			setErrorsInRequest(request, errors); 
			forward = forwards.get("input");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doCommon");
		}
		
		return forward;
	}

	/*@RequestMapping(value ="/withdrawalDetailsReport/", params={action=preExecute,task=preExecute} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String dopreExecute (@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {if(bindingResult.hasErrors()){
		LoanSummaryReportData data = (LoanSummaryReportData) request
				.getSession().getAttribute(
						Constants.LOAN_SUMMARY_REPORT_BEAN);
        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       if(errDirect!=null){
              request.getSession().removeAttribute(PsBaseAction.ERROR_KEY);
forwards.get("ContactXSS");//if input forward not //available, provided default
       }
	}
       String forward=super.dopreExecute( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
}*/

	@RequestMapping(value ="/withdrawalDetailsReport/" , method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    		populateReportForm( form, request);
	    		
				request.setAttribute("displayDates", "true");
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doDefault( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
 @RequestMapping(value ="/withdrawalDetailsReport/" ,params={"task=filter"}  , method =  {RequestMethod.GET}) 
    public String doFilter (@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		LoanSummaryReportData data = (LoanSummaryReportData) request
					.getSession().getAttribute(
							Constants.LOAN_SUMMARY_REPORT_BEAN);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              forwards.get("ContactXSS");
	       }
		}
	       String forward=super.doFilter( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
    
    	 @RequestMapping(value ="/withdrawalDetailsReport/" ,params={"task=page"}  , method =  {RequestMethod.GET}) 
    	    public String doPage (@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    	    throws IOException,ServletException, SystemException {
    	    	if(bindingResult.hasErrors()){
    	    		LoanSummaryReportData data = (LoanSummaryReportData) request
    						.getSession().getAttribute(
    								Constants.LOAN_SUMMARY_REPORT_BEAN);
    		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		       if(errDirect!=null){
    		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    		              forwards.get("ContactXSS");
    		       }
    			}
    		       String forward=super.doPage( form, request, response);
    				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    	 }
    	
    		 @RequestMapping(value ="/withdrawalDetailsReport/" ,params={"task=sort"}  , method =  {RequestMethod.GET}) 
     	    public String doSort (@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
     	    throws IOException,ServletException, SystemException {
     	    	if(bindingResult.hasErrors()){
     	    		LoanSummaryReportData data = (LoanSummaryReportData) request
     						.getSession().getAttribute(
     								Constants.LOAN_SUMMARY_REPORT_BEAN);
     		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
     		       if(errDirect!=null){
     		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
     		             forwards.get("ContactXSS");
     		       }
     			}
     		       String forward=super.doSort( form, request, response);
     				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
     	 }
    		
    	
    		 @RequestMapping(value ="/withdrawalDetailsReport/" ,params={"task=downloadAll"}  , method =  {RequestMethod.GET})
    		 public String doDownloadAll (@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    			     throws IOException,ServletException, SystemException {
    			     	   if(bindingResult.hasErrors()){
    			     		  LoanSummaryReportData data = (LoanSummaryReportData) request
    			  					.getSession().getAttribute(
    			  							Constants.LOAN_SUMMARY_REPORT_BEAN);
    			     		     String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    			     		     if(errDirect!=null){
    			     		      request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			     		     forwards.get("ContactXSS");
    			     		    }
    			     }
    			     String forward=super.doDownloadAll( form, request, response);
    			     return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    			    }  

	/**
	 * Invokes the download task. The first half of this task uses the common
	 * workflow with validateForm set to true. The second half of this task
	 * takes the populated report data object and create the CSV file.
	 *
	 * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
	 *      HttpServletResponse, boolean)
	 * @see #populateDownloadData(PrintWriter, BaseReportForm, ReportData,
	 *      HttpServletRequest)
	 * @return null so that Struts will not try to forward to another page.
	 */
	@RequestMapping(value ="/withdrawalDetailsReport/", params={"task=download"} , method =  {RequestMethod.GET}) 
	public String doDownload(@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDownload");
		}

		byte[] downloadData = null;
		Contract currentContract = getUserProfile(request).getCurrentContract();
		doCommon( form, request, response);

		Collection<GenericException> errors = 
			(Collection<GenericException>) request.getAttribute(CommonConstants.ERROR_KEY);

		if (errors != null && errors.size() > 0) {
			//Get Error report in download information to be displayed.
			downloadData = WithdrawalDetailsUtility.getErrorDownload(
					errors,currentContract).getBytes();
		} else {
			ReportData report = (ReportData) request.getAttribute(Constants.REPORT_BEAN);
			downloadData =  getDownloadData(form, report, request);
		}

		super.streamDownloadData(request, response, getContentType(),
				getFileName(form,request), downloadData);

		/**
		 * No need to forward to any other JSP or action. Returns null will make
		 * Struts to return controls back to server immediately.
		 */
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDownload");
		}
		return null;	
		}
	@RequestMapping(value = "/withdrawalDetailsReport/", params = {"task=print"}, method = {RequestMethod.GET })
	public String doPrint(@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																										// default
			}
		}
		String forward = super.doPrint(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * Get the Default sort
	 */
	@Override
	protected String getDefaultSort() {
		return null;
	}
	/**
	 * Get the Default sort Direction.
	 */
	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}
	
	/** This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#137697.
	 */
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}