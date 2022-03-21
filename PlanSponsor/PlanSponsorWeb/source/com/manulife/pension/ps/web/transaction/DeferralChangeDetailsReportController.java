package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;

import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.web.Constants;

import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsDeferral;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsDeferralReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.Principal;
import com.manulife.util.render.SSNRender;

/**
 * In support of transaction history details for Deferral changes, 
 * 
 * @author Glen Lalonde
 */
@Controller
@RequestMapping( value ="/transaction")
@SessionAttributes({"deferralChangeDetailsForm"})

public class DeferralChangeDetailsReportController extends ReportController {

	@ModelAttribute("deferralChangeDetailsForm")
	public  DeferralChangeDetailsReportForm populateForm()
	{
		return new  DeferralChangeDetailsReportForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/deferralChangeDetailsReport.jsp"); 
		forwards.put("default","/transaction/deferralChangeDetailsReport.jsp");
		forwards.put("sort","/transaction/deferralChangeDetailsReport.jsp"); 
		forwards.put("filter","/transaction/deferralChangeDetailsReport.jsp");
		forwards.put("page","/transaction/deferralChangeDetailsReport.jsp"); 
		forwards.put("print","/transaction/deferralChangeDetailsReport.jsp");}

	
	private static final String TRANSACTION_HISTORY = "TH";
	private static final String DOWNLOAD_REPORT_NAME = "DeferralchangeReport";
	private static final String DOUBLE_QUOTES = "\"";

	
	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return TransactionDetailsDeferralReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return TransactionDetailsDeferralReportData.REPORT_NAME;
	}

	protected String getDefaultSort() {
		return "lastName";
	}

	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}
	
	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			                              BaseReportForm form, HttpServletRequest request) {
		
		if (logger.isDebugEnabled()) logger.debug("entry -> populateReportCriteria"); 

		DeferralChangeDetailsReportForm dcForm = (DeferralChangeDetailsReportForm) form;

		int contractNumber = getUserProfile(request).getCurrentContract().getContractNumber();
		
		criteria.addFilter(TransactionDetailsDeferralReportData.FILTER_CONTRACTID, new Integer(contractNumber));
        criteria.addFilter(TransactionDetailsDeferralReportData.FILTER_PROFILE_ID, dcForm.getProfileId()); 				
		criteria.addFilter(TransactionDetailsDeferralReportData.FILTER_TARGET_DATE, dcForm.getTransactionDate());
        criteria.addFilter(TransactionDetailsDeferralReportData.FILTER_SCREEN, TRANSACTION_HISTORY);
        criteria.addFilter(TransactionDetailsDeferralReportData.FILTER_APPLICATION_ID, TransactionDetailsDeferralReportData.PSW_APPLICATION_ID);
			
        if (getUserProfile(request).isInternalUser()==false) {
        	criteria.addFilter(TransactionDetailsDeferralReportData.FILTER_EXTERNAL_USER_VIEW, "Y"); 
        }
		        
		if (logger.isDebugEnabled()) logger.debug("exit <-  populateReportCriteria");
	}

	
	@RequestMapping(value ="/deferralChangeDetailsReport/", params={"action=historyPrint","task=historyPrint"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doHistoryPrint(@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {


		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doHistoryPrint");
		}

		String forward = ( "historyPrint");
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doHistoryPrint");
		}

		return forward;
	}
	
		
    protected String getFileName(HttpServletRequest request) {
    	String fileName = DOWNLOAD_REPORT_NAME+CSV_EXTENSION;
    	        
        return fileName.replaceAll("\\ ", "_");  // Replace spaces with underscores
    }

		
	/**
	 * Called by framework to generate excel (csv) data
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}
		
		StringBuffer buffer = new StringBuffer();
		
		DeferralChangeDetailsReportForm deferralForm = (DeferralChangeDetailsReportForm)reportForm;

		ParticipantAccountDetailsVO detailsVO = (ParticipantAccountDetailsVO)request.getAttribute("details");
        //SSE S024 determine wheather the ssn should be masked on the csv report
        boolean maskSSN = true;// set the mask ssn flag to true as a default
        UserProfile user = getUserProfile(request);
        try{
        	maskSSN =ReportDownloadHelper.isMaskedSsn(user, user.getCurrentContract().getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        }	
		buffer.append("Deferral change summary").append(LINE_BREAK);
		buffer.append("Transaction type").append(COMMA).append("Deferral update").append(LINE_BREAK);
		buffer.append("Name").append(COMMA).append(escapeField(detailsVO.getLastName()+", "+detailsVO.getFirstName())).append(LINE_BREAK);
	//	buffer.append("SSN").append(COMMA).append("xxx-xx-"+detailsVO.getSsn().substring(5)).append(LINE_BREAK);
		buffer.append("SSN").append(COMMA).append(SSNRender.format(detailsVO.getSsn(),"", maskSSN)).append(LINE_BREAK);		
		buffer.append(LINE_BREAK);	
		
		buffer.append("Details - "+deferralForm.getTransactionDateFormatted()).append(LINE_BREAK);
		buffer.append("Requested date,Item changed,Value before,Value requested,Value updated,Status,Changed by,Comments").append(LINE_BREAK);

		Iterator it = deferralForm.getReport().getDetails().iterator();
		while(it.hasNext()) {
			TransactionDetailsDeferral item = (TransactionDetailsDeferral)it.next();
			buffer.append(getNotNull(item.getRequestedDate())).append(COMMA);
			buffer.append(item.getItemChanged()).append(COMMA);
			buffer.append(item.getValueBeforeForDownload()).append(COMMA);
			buffer.append(getNotNull(item.getValueRequestedForDownload())).append(COMMA);
			buffer.append(item.getValueUpdatedForDownload()).append(COMMA);
			buffer.append(getNotNull(item.getStatus())).append(COMMA);
			buffer.append(item.getChangedBy());
			if (item.genSecondLine()) {
				buffer.append(COMMA);
				buffer.append(DOUBLE_QUOTES);
				buffer.append(item.getComments());
				buffer.append(DOUBLE_QUOTES);
			} 
			buffer.append(LINE_BREAK);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	
	// from ParticipantTransactionHistoryAction, jsp setup to pickup from request.
	public void populateParticipantDetails(String profileId, HttpServletRequest request) throws SystemException {			
		UserProfile userProfile = getUserProfile(request);
		int contractNumber = userProfile.getCurrentContract().getContractNumber();
		String productId = userProfile.getCurrentContract().getProductId();
		
		Principal principal = getUserProfile(request).getPrincipal();
		ParticipantAccountVO participantAccountVO = ParticipantServiceDelegate.getInstance().getParticipantAccount(
				principal, contractNumber,
				productId, profileId, 
				null, false, false);
		ParticipantAccountDetailsVO participantDetailsVO = participantAccountVO.getParticipantAccountDetailsVO();
		
		request.setAttribute("details", participantDetailsVO); // needed by jsp.
	}
	
	
	private String getNotNull(String value) {
		if (value == null) return "";
		return value;
	}
	
	
	// don't want excel to think the , is the next field
	private String escapeField(String field) {
		if(field.indexOf(",") != -1 ) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else 	{
			return field;
		}
	}
	
	
	protected ReportData getReportData(String reportId,
			ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		ReportServiceDelegate service = ReportServiceDelegate.getInstance();
		ReportData bean = service.getReportData(reportCriteria);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}

		return bean;
	}
	
	 
	public String doCommon ( DeferralChangeDetailsReportForm form,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		 
		if (logger.isDebugEnabled()) logger.debug("entry -> doCommon");

		String forward = super.doCommon( form, request, response);
		
		// set values send from user selection on summary screen
		// setup on initial nav to the screen, print/download just uses value set.
		if (request.getParameter("profileId") != null) { 
			form.setProfileId(request.getParameter("profileId"));
			form.setTransactionDate(request.getParameter("transactionDate"));			
		}

    	TransactionDetailsDeferralReportData report = (TransactionDetailsDeferralReportData)request.getAttribute(Constants.REPORT_BEAN);
		form.setReport(report);
		        		
		populateParticipantDetails(form.getProfileId(), request);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		
		return forward;
	}	
			


	/**
	 * @see PsController#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	/*@RequestMapping(value ="/deferralChangeDetailsReport/" , method =  {RequestMethod.POST,RequestMethod.GET}) */
	public String preExecute (/*@Valid @ModelAttribute("deferralChangeDetailsForm")*/ DeferralChangeDetailsReportForm form,/* BindingResult bindingResult,*/HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		/*if(bindingResult.hasErrors()){
			 TransactionDetailsDeferralReportData report = (TransactionDetailsDeferralReportData) request
					 .getAttribute(CommonConstants.REPORT_BEAN);
			 try {
				 request.removeAttribute(PsBaseAction.ERROR_KEY);
				 populateParticipantDetails(
						 ((DeferralChangeDetailsReportForm) form).getProfileId(),
						 request);
				 request.setAttribute(CommonConstants.REPORT_BEAN, report);
			 } catch (SystemException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(PsBaseAction.ERROR_KEY);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
		*/
		UserProfile userProfile = SessionHelper.getUserProfile(request);
        
        // check for selected access
        if (userProfile.isSelectedAccess()) {
            return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        }
        
        // check if contract is discontinued
        if (userProfile.getCurrentContract().isDiscontinued()) {
            return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        }
		
		if ("POST".equalsIgnoreCase(request.getMethod()) ) {
			// do a refresh so that there's no problem using the back button
			String forward = new UrlPathHelper().getPathWithinApplication(request);
		    if(logger.isDebugEnabled()) {
			    logger.debug("forward = " + forward);
		    }
			return forward;
		}
					
		return null;
	}
	
	public String dopreExecute (DeferralChangeDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	/*	if(bindingResult.hasErrors()){
			 TransactionDetailsDeferralReportData report = (TransactionDetailsDeferralReportData) request
					 .getAttribute(CommonConstants.REPORT_BEAN);
			 try {
				 request.removeAttribute(PsBaseAction.ERROR_KEY);
				 populateParticipantDetails(
						 ((DeferralChangeDetailsReportForm) form).getProfileId(),
						 request);
				 request.setAttribute(CommonConstants.REPORT_BEAN, report);
			 } catch (SystemException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(PsBaseAction.ERROR_KEY);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }*/
	       String forward=super.doExecute( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	@RequestMapping(value ="/deferralChangeDetailsReport/" , method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
		 if(StringUtils.isNotBlank(forward)) {
			return  StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		 }
		if(bindingResult.hasErrors()){
			 TransactionDetailsDeferralReportData report = (TransactionDetailsDeferralReportData) request
					 .getAttribute(CommonConstants.REPORT_BEAN);
			 try {
				 request.removeAttribute(CommonConstants.ERROR_KEY);
				 populateParticipantDetails(
						 ((DeferralChangeDetailsReportForm) form).getProfileId(),
						 request);
				 request.setAttribute(CommonConstants.REPORT_BEAN, report);
			 } catch (SystemException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
	        forward=super.doExecute( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	 @RequestMapping(value ="/deferralChangeDetailsReport/" ,params={"task=filter"}  , method =  {RequestMethod.GET}) 
	    public String doFilter (@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    throws IOException,ServletException, SystemException {
		 String forward=preExecute(form, request, response);
		 if(StringUtils.isNotBlank(forward)) {
			return  StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		 }
		 if(bindingResult.hasErrors()){
			 TransactionDetailsDeferralReportData report = (TransactionDetailsDeferralReportData) request
					 .getAttribute(CommonConstants.REPORT_BEAN);
			 try {
				 request.removeAttribute(CommonConstants.ERROR_KEY);
				 populateParticipantDetails(
						 ((DeferralChangeDetailsReportForm) form).getProfileId(),
						 request);
				 request.setAttribute(CommonConstants.REPORT_BEAN, report);
			 } catch (SystemException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
		        forward=super.doFilter( form, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	    
	    	 @RequestMapping(value ="/deferralChangeDetailsReport/" ,params={"task=page"}  , method =  {RequestMethod.GET}) 
	    	    public String doPage (@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    	    throws IOException,ServletException, SystemException {
	    		 String forward=preExecute(form, request, response);
	    		 if(StringUtils.isNotBlank(forward)) {
	    			return  StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	    		 }
	    		 if(bindingResult.hasErrors()){
    				 TransactionDetailsDeferralReportData report = (TransactionDetailsDeferralReportData) request
    						 .getAttribute(CommonConstants.REPORT_BEAN);
    				 try {
    					 request.removeAttribute(CommonConstants.ERROR_KEY);
    					 populateParticipantDetails(
    							 ((DeferralChangeDetailsReportForm) form).getProfileId(),
    							 request);
    					 request.setAttribute(CommonConstants.REPORT_BEAN, report);
    				 } catch (SystemException e) {
    					 // TODO Auto-generated catch block
    					 e.printStackTrace();
    				 }
    				 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    				 if(errDirect!=null){
    					 request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
    					 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    				 }
    			 }
	    		        forward=super.doPage( form, request, response);
	    				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	    	 }
	    	
	    		 @RequestMapping(value ="/deferralChangeDetailsReport/" ,params={"task=sort"}  , method =  {RequestMethod.GET}) 
	     	    public String doSort (@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	     	    throws IOException,ServletException, SystemException {
	    			 String forward=preExecute(form, request, response);
	    			 if(StringUtils.isNotBlank(forward)) {
	    				return  StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	    			 }
	    			 if(bindingResult.hasErrors()){
	    				 TransactionDetailsDeferralReportData report = (TransactionDetailsDeferralReportData) request
	    						 .getAttribute(CommonConstants.REPORT_BEAN);
	    				 try {
	    					 request.removeAttribute(CommonConstants.ERROR_KEY);
	    					 populateParticipantDetails(
	    							 ((DeferralChangeDetailsReportForm) form).getProfileId(),
	    							 request);
	    					 request.setAttribute(CommonConstants.REPORT_BEAN, report);
	    				 } catch (SystemException e) {
	    					 // TODO Auto-generated catch block
	    					 e.printStackTrace();
	    				 }
	    				 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	    				 if(errDirect!=null){
	    					 request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	    					 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	    				 }
	    			 }
	     		        forward=super.doSort( form, request, response);
	     				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	     	 }
	    		 @RequestMapping(value ="/deferralChangeDetailsReport/" ,params={"task=download"}  , method =  {RequestMethod.GET})	
	    	 public String doDownload (@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    			     throws IOException,ServletException, SystemException {
	    			 String forward=preExecute(form, request, response);
	    			 if(StringUtils.isNotBlank(forward)) {
	    				return  StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	    			 }
	    			 if(bindingResult.hasErrors()){
	    				 TransactionDetailsDeferralReportData report = (TransactionDetailsDeferralReportData) request
	    						 .getAttribute(CommonConstants.REPORT_BEAN);
	    				 try {
	    					 request.removeAttribute(CommonConstants.ERROR_KEY);
	    					 populateParticipantDetails(
	    							 ((DeferralChangeDetailsReportForm) form).getProfileId(),
	    							 request);
	    					 request.setAttribute(CommonConstants.REPORT_BEAN, report);
	    				 } catch (SystemException e) {
	    					 // TODO Auto-generated catch block
	    					 e.printStackTrace();
	    				 }
	    				 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	    				 if(errDirect!=null){
	    					 request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	    					 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	    				 }
	    			 }
	    			  forward=super.doDownload( form, request, response);
    			     return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    			    } 
	    		 @RequestMapping(value ="/deferralChangeDetailsReport/" ,params={"task=dowanloadAll"}  , method =  {RequestMethod.GET})
	    		 public String doDownloadAll (@Valid @ModelAttribute("deferralChangeDetailsForm") DeferralChangeDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    			     throws IOException,ServletException, SystemException {
	    			 String forward=preExecute(form, request, response);
	    			 if(StringUtils.isNotBlank(forward)) {
	    				return  StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	    			 }
	    			 if(bindingResult.hasErrors()){
	    				 TransactionDetailsDeferralReportData report = (TransactionDetailsDeferralReportData) request
	    						 .getAttribute(CommonConstants.REPORT_BEAN);
	    				 try {
	    					 request.removeAttribute(CommonConstants.ERROR_KEY);
	    					 populateParticipantDetails(
	    							 ((DeferralChangeDetailsReportForm) form).getProfileId(),
	    							 request);
	    					 request.setAttribute(CommonConstants.REPORT_BEAN, report);
	    				 } catch (SystemException e) {
	    					 // TODO Auto-generated catch block
	    					 e.printStackTrace();
	    				 }
	    				 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	    				 if(errDirect!=null){
	    					 request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	    					 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	    				 }
	    			 }
	    			      forward=super.doDownloadAll( form, request, response);
	    			     return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	    			    }  
	
	
	/** This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#137697.
	 */
	/*@SuppressWarnings("rawtypes")
	public Collection doValidate( Form form,
			HttpServletRequest request) {
		Collection penErrors = PsValidation.doValidatePenTestAutoAction(form,
				mapping, request, CommonConstants.INPUT);
		if (penErrors != null && penErrors.size() > 0) {
			TransactionDetailsDeferralReportData report = (TransactionDetailsDeferralReportData) request
					.getAttribute(CommonConstants.REPORT_BEAN);
			try {
				request.removeAttribute(PsBaseAction.ERROR_KEY);
				populateParticipantDetails(
						((DeferralChangeDetailsReportForm) form).getProfileId(),
						request);
				request.setAttribute(CommonConstants.REPORT_BEAN, report);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return penErrors;
		}
		return super.doValidate( form, request);
	}
	*/
	    		 @Autowired
	    		   private PSValidatorFWInput  psValidatorFWInput;
	    		 @InitBinder
	    		  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    		    binder.bind( request);
	    		    binder.addValidators(psValidatorFWInput);
	    		}
}
