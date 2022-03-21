package com.manulife.pension.ps.web.transaction;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;



import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;

import com.manulife.pension.ps.service.report.transaction.reporthandler.PendingWithdrawalDetailsReportHandler;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.participant.transaction.valueobject.PendingWithdrawalDetailsReportData;
import com.manulife.pension.service.report.participant.transaction.valueobject.PendingWithdrawalSummaryReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;	
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.HashMap;
import javax.validation.Valid;

/**
 * Class to handle the Pending Withdrawals Details page. 
 * 
 * @author Puttaiah Arugunta
 *
 */
@Controller
@RequestMapping(value ="/transaction")
@SessionAttributes({"pendingWithdrawalDetailsForm"})


public class PendingWithdrawalDetailsReportController extends AbstractTransactionReportController {

	@ModelAttribute("pendingWithdrawalDetailsForm") public PendingWithdrawalDetailsForm populateForm() 
	{
		return new PendingWithdrawalDetailsForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/pendingWithdrawalTransactionReport.jsp"); 
		forwards.put("default","/transaction/pendingWithdrawalTransactionReport.jsp");
		forwards.put("print","/transaction/pendingWithdrawalTransactionReport.jsp");
	}

	private static final String PENDING_WITHDRAWAL_DETAILS_FORM = "pendingWithdrawalDetailsForm";
	
	/**
	 * Constructor
	 */
	public PendingWithdrawalDetailsReportController() {
		super(PendingWithdrawalDetailsReportController.class);
	}
	/**
	 * Get the default sort order.
	 * Method is been over ridden as per the reporting framework and depreciated because there is no sorting technique in this page
	 */
	@Override
	protected String getDefaultSort() {
		return null;
	}

	/**
	 * Get the default sort direction.
	 * Method is been over ridden as per the reporting framework and depreciated because there is no sorting technique in this page
	 */
	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * Method to download the data to CSV file.
	 * Method is been over ridden as per the reporting framework and depreciated because there is no download functionality in the page
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @throws SystemException
	 */
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
	throws SystemException {
		return null;
	}
	@RequestMapping(value ="/pendingWithdrawalDetailsReport/" ,method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doDefault (@Valid @ModelAttribute("pendingWithdrawalDetailsForm") PendingWithdrawalDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	   String selectedTxnNumber = (String)request.getSession(false).getAttribute(Constants.TXN_NUMBER_KEY);
	    		if(selectedTxnNumber == null || StringUtils.isBlank(selectedTxnNumber)){
	    			String[] errorCodes = new String[]{Integer.toString(ErrorCodes.TECHNICAL_DIFFICULTIES)};
	    			
	    			bindingResult.addError(new ObjectError(bindingResult
			                 .getObjectName(),errorCodes , null, null));
	    		}
	              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doDefault( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ="/pendingWithdrawalDetailsReport/",params={"task=filter"}, method =  {RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("pendingWithdrawalDetailsForm") PendingWithdrawalDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	   String selectedTxnNumber = (String)request.getSession(false).getAttribute(Constants.TXN_NUMBER_KEY);
	    		if(selectedTxnNumber == null || StringUtils.isBlank(selectedTxnNumber)){
	    				String[] errorCodes = new String[]{Integer.toString(ErrorCodes.TECHNICAL_DIFFICULTIES)};
	    			
	    			bindingResult.addError(new ObjectError(bindingResult
			                 .getObjectName(),errorCodes , null, null));
	    		}
	              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doFilter( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/pendingWithdrawalDetailsReport/" ,params={"task=page"} , method =  {RequestMethod.GET}) 
	public String doPage (@Valid @ModelAttribute("pendingWithdrawalDetailsForm") PendingWithdrawalDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	   String selectedTxnNumber = (String)request.getSession(false).getAttribute(Constants.TXN_NUMBER_KEY);
	    		if(selectedTxnNumber == null || StringUtils.isBlank(selectedTxnNumber)){
	String[] errorCodes = new String[]{Integer.toString(ErrorCodes.TECHNICAL_DIFFICULTIES)};
	    			
	    			bindingResult.addError(new ObjectError(bindingResult
			                 .getObjectName(),errorCodes , null, null));
	    		}
	              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doFilter( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/pendingWithdrawalDetailsReport/" ,params={"task=sort"}, method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("pendingWithdrawalDetailsForm") PendingWithdrawalDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	   String selectedTxnNumber = (String)request.getSession(false).getAttribute(Constants.TXN_NUMBER_KEY);
	    		if(selectedTxnNumber == null || StringUtils.isBlank(selectedTxnNumber)){
	    				String[] errorCodes = new String[]{Integer.toString(ErrorCodes.TECHNICAL_DIFFICULTIES)};
	    			
	    			bindingResult.addError(new ObjectError(bindingResult
			                 .getObjectName(),errorCodes , null, null));
	    		}
	              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doSort( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/pendingWithdrawalDetailsReport/", params={"task=download"}, method =  {RequestMethod.GET}) 
	public String doDownload (@Valid @ModelAttribute("pendingWithdrawalDetailsForm") PendingWithdrawalDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	   String selectedTxnNumber = (String)request.getSession(false).getAttribute(Constants.TXN_NUMBER_KEY);
	    		if(selectedTxnNumber == null || StringUtils.isBlank(selectedTxnNumber)){
	    				String[] errorCodes = new String[]{Integer.toString(ErrorCodes.TECHNICAL_DIFFICULTIES)};
	    			
	    			bindingResult.addError(new ObjectError(bindingResult
			                 .getObjectName(),errorCodes , null, null));
	    		}
	              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doDownload( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/pendingWithdrawalDetailsReport/", params={"task=downloadAll"}, method =  {RequestMethod.GET}) 
	public String doDownloadAll (@Valid @ModelAttribute("pendingWithdrawalDetailsForm") PendingWithdrawalDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	   String selectedTxnNumber = (String)request.getSession(false).getAttribute(Constants.TXN_NUMBER_KEY);
	    		if(selectedTxnNumber == null || StringUtils.isBlank(selectedTxnNumber)){
	    				String[] errorCodes = new String[]{Integer.toString(ErrorCodes.TECHNICAL_DIFFICULTIES)};
	    			
	    			bindingResult.addError(new ObjectError(bindingResult
			                 .getObjectName(),errorCodes , null, null));
	    		}
	              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doDownload( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/pendingWithdrawalDetailsReport/",params={"task=print"}, method =  {RequestMethod.GET}) 
	public String doPrint (@Valid @ModelAttribute("pendingWithdrawalDetailsForm") PendingWithdrawalDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	   String selectedTxnNumber = (String)request.getSession(false).getAttribute(Constants.TXN_NUMBER_KEY);
	    		if(selectedTxnNumber == null || StringUtils.isBlank(selectedTxnNumber)){
	    				String[] errorCodes = new String[]{Integer.toString(ErrorCodes.TECHNICAL_DIFFICULTIES)};
	    			
	    			bindingResult.addError(new ObjectError(bindingResult
			                 .getObjectName(),errorCodes , null, null));
	    		}
	              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doPrint(actionForm, request, response);
			return StringUtils.contains(forward,'/')?forwards.get(forward):forwards.get(forward); 
	}
	

	/**
	 * Get the report Id
	 */
	@Override
	protected String getReportId() {
		return PendingWithdrawalDetailsReportHandler.class.getName();
	}

	/**
	 * Get the Report Name
	 */
	@Override
	protected String getReportName() {
		return PendingWithdrawalSummaryReportData.REPORT_NAME;
	}

	/**
	 * Method to populate the Report Criteria object with Transaction Number and Proposal Number

	 * @param criteria
	 * @param actionForm
	 * @param request
	 * @throws SystemException
	 */
	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm actionForm, HttpServletRequest request)
	throws SystemException {
	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		String selectedTxnNumber = (String)request.getSession(false).getAttribute(Constants.TXN_NUMBER_KEY);
		
		String selectedPptNumber = (String)request.getSession(false).getAttribute(Constants.PARTICIPANT_ID_KEY);

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter(PendingWithdrawalDetailsReportData.FILTER_PROPOSAL_NUMBER,
				currentContract.getProposalNumber());
		criteria.addFilter(PendingWithdrawalDetailsReportData.FILTER_TRANSACTION_NUMBER,
				selectedTxnNumber);
		criteria.addFilter(PendingWithdrawalDetailsReportData.FILTER_CONTRACT_NUMBER,
				String.valueOf(currentContract.getContractNumber()));
		criteria.addFilter(PendingWithdrawalDetailsReportData.FILTER_PARTICIPANT_NUMBER,
						selectedPptNumber);
		

		if (userProfile.getCurrentContract().isDefinedBenefitContract()) {
			criteria.addFilter(PendingWithdrawalSummaryReportData.CONTRACT_TYPE_DB, Boolean.TRUE);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}		

	}

	/**
	 * Validate whether selected transaction number is in Session or not. If not,
	 * we should not display the page.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	/*@SuppressWarnings("unchecked")
	protected Collection doValidate( Form form,
			HttpServletRequest request){
		
		// This code has been changed and added  to 
		//Validate form and request against penetration attack, prior to other validations as part of the CL#137697.
		Collection penErrors = PsValidation.doValidatePenTestAutoAction(form, mapping, request, CommonConstants.INPUT);
		if (penErrors != null && penErrors.size() > 0) {
			request.removeAttribute(PsBaseAction.ERROR_KEY);
			return penErrors;
		}
		Collection errors = super.doValidate( form, request);
		
		String selectedTxnNumber = (String)request.getSession(false).getAttribute(Constants.TXN_NUMBER_KEY);
		if(selectedTxnNumber == null || StringUtils.isBlank(selectedTxnNumber)){
			errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
		}
		
		return errors;
	}*/
	

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);

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
        PendingWithdrawalDetailsForm pendingWithdrawalDetailsForm = 
        	(PendingWithdrawalDetailsForm) request.getSession()
        	.getAttribute(PENDING_WITHDRAWAL_DETAILS_FORM);
        if (pendingWithdrawalDetailsForm != null
        		&& pendingWithdrawalDetailsForm.getTask() != null) {
            task = pendingWithdrawalDetailsForm.getTask() ;
        }else{
        	task = DEFAULT_TASK;
        }
        return task;
    }
}
