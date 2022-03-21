package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.report.util.WithdrawalDetailsUtility;
import com.manulife.pension.ps.service.report.transaction.valueobject.ClassConversionDetailsFund;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsClassConversionReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFund;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.account.valueobject.FundGroup;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

@Controller
@RequestMapping( value ="/transaction")
@SessionAttributes({"transactionDetailsClassConversionForm"})

public class TransactionDetailsClassConversionController extends ReportController {
		
	@ModelAttribute("transactionDetailsClassConversionForm") 
	public  TransactionDetailsClassConversionForm populateForm()
	{
		return new  TransactionDetailsClassConversionForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
	forwards.put("input","/transaction/classConversionTransactionReport.jsp");
	forwards.put("default","/transaction/classConversionTransactionReport.jsp");
	forwards.put("path","/transaction/classConversionTransactionReport.jsp");
	forwards.put("page","/transaction/classConversionTransactionReport.jsp");
	forwards.put("print","/transaction/classConversionTransactionReport.jsp");
	forwards.put("filter","/transaction/classConversionTransactionReport.jsp");
	} 

	private static final String NUMBER_FORMAT_PATTERN = "########0.00";
	private static final String DEFAULT_VALUE_ZERO = "0.00";

	private static final DecimalFormat twoDecimals = new DecimalFormat("0.00");
	
	protected String getDefaultSort() {
		return TransactionDetailsClassConversionReportData.SORT_FIELD_WEBSRTNO;
	}

	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}
	
		
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm actionForm, HttpServletRequest request) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		
		TransactionDetailsClassConversionForm form = (TransactionDetailsClassConversionForm)actionForm;

		String transactionNumber = (String)request.getParameter("transactionNumber");
		if (transactionNumber == null || transactionNumber.equals("")) {
			transactionNumber = form.getTransactionNumber();
		}
		
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		String contractNumber = String.valueOf(currentContract.getContractNumber());
		if (contractNumber == null || contractNumber.equals("") || contractNumber.equals("0")) {
			contractNumber = form.getContractNumber();
		}
		
		String participantId = (String)request.getParameter("pptId"); // new reference form.
		if (participantId == null || participantId.equals("")) {
			participantId = (String)request.getParameter("participantId"); // do legacy check incase a link was missed 
			if (participantId == null || participantId.equals("")) {
				participantId = form.getPptId();
			}			
		}
		
		if (userProfile.getCurrentContract().isDefinedBenefitContract()) {
			criteria.addFilter(TransactionDetailsClassConversionReportData.CONTRACT_TYPE_DB, Boolean.TRUE);
		}		
		
		criteria.addFilter(TransactionDetailsClassConversionReportData.FILTER_TRANSACTION_NUMBER, transactionNumber);
		criteria.addFilter(TransactionDetailsClassConversionReportData.FILTER_PARTICIPANT_ID, participantId);
		criteria.addFilter(TransactionDetailsClassConversionReportData.FILTER_CONTRACT_NUMBER, contractNumber);
		

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}		
	}

	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm actionForm, HttpServletRequest request) throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateSortCriteria");
		}

		// default sort is risk category			
		TransactionDetailsFTFForm form = (TransactionDetailsFTFForm) actionForm;
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();
		
		criteria.insertSort(sortField, sortDirection);
		
		// add additional sort criteria websrtno and monty type description
		criteria.insertSort(TransactionDetailsClassConversionReportData.SORT_FIELD_WEBSRTNO, ReportSort.ASC_DIRECTION);
		criteria.insertSort(TransactionDetailsClassConversionReportData.SORT_FIELD_MONEY_TYPE_DESCRIPTION, ReportSort.ASC_DIRECTION);
		
		
								
		if (logger.isDebugEnabled()) {
			logger.debug("populateSortCriteria: inserting sort with field:"+sortField+" and direction: " + sortDirection);
		}
	}
	
	
	public TransactionDetailsClassConversionController() {
		super(TransactionDetailsClassConversionController.class);
	}
	
	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return TransactionDetailsClassConversionReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return TransactionDetailsClassConversionReportData.REPORT_NAME;
	}
	@RequestMapping(value = "/classConversionTransactionReport/", method = {
			RequestMethod.GET})
	public String doDefault(
			@Valid @ModelAttribute("transactionDetailsClassConversionForm") TransactionDetailsClassConversionForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_KEY);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
				ControllerForward forward = new ControllerForward("refresh",
						"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
				return "redirect:" + forward.getPath();
			}
		}
		String forward = super.doDefault(form, request, response);
		//return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	@RequestMapping(value ="/classConversionTransactionReport/",params={"task=filter"}, method =  {RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("transactionDetailsClassConversionForm") TransactionDetailsClassConversionForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	   String selectedTxnNumber = (String)request.getSession(false).getAttribute(Constants.TXN_NUMBER_KEY);
	    		if(selectedTxnNumber == null || StringUtils.isBlank(selectedTxnNumber)){
	    				String[] errorCodes = new String[]{Integer.toString(ErrorCodes.TECHNICAL_DIFFICULTIES)};
	    			
	    			bindingResult.addError(new ObjectError(bindingResult
			                 .getObjectName(),errorCodes , null, null));
	    			ControllerForward forward = new ControllerForward("refresh",
							"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
					return "redirect:" + forward.getPath();
	    		}
	              request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doFilter( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/classConversionTransactionReport/", params={"task=download"} , method =  {RequestMethod.GET}) 
	public String doDownload(@Valid @ModelAttribute("transactionDetailsClassConversionForm") TransactionDetailsClassConversionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	@RequestMapping(value = "/classConversionTransactionReport/", params = {"task=print"}, method = {RequestMethod.GET })
	public String doPrint(@Valid @ModelAttribute("transactionDetailsClassConversionForm") TransactionDetailsClassConversionForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh",
						"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
				return "redirect:" + forward.getPath();																							// default
			}
		}
		String forward = super.doPrint(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	 
	public String doCommon( BaseReportForm form, HttpServletRequest request,HttpServletResponse response) 
	throws  SystemException {
							 
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon()");
		}
				
		String forward=null;
		forward = super.doCommon( form, request, response);
		TransactionDetailsClassConversionReportData report = (TransactionDetailsClassConversionReportData)request.getAttribute(Constants.REPORT_BEAN);				
		TransactionDetailsClassConversionForm transactionDetailsClassConversionFormform = (TransactionDetailsClassConversionForm) form;

		if (report.getDetails().size() != 0) {
			transactionDetailsClassConversionFormform.setReport(report);
			transactionDetailsClassConversionFormform.setTransactionNumber(report.getTransactionNumber());
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doCommon()");
		}
						
		return forward;
	}
			
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		
		TransactionDetailsClassConversionReportData theReport = (TransactionDetailsClassConversionReportData)report;
		UserProfile userProfile = getUserProfile(request);
		
		if (logger.isDebugEnabled())
			logger.debug("entry -> getDownloadData");
		// get the content objects
		StringBuffer buffer = new StringBuffer();

		// Title
		buffer.append("Transaction details").append(LINE_BREAK+LINE_BREAK);
		buffer.append("Class conversion details").append(LINE_BREAK+LINE_BREAK);
		

	    Contract currentContract = getUserProfile(request).getCurrentContract();
	    buffer.append("Contract number:").append(COMMA).append(currentContract.getContractNumber());
	    buffer.append(LINE_BREAK);
	    buffer.append("Company name:").append(COMMA).append(currentContract.getCompanyName());
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		
		buffer.append("Class conversion:").append(LINE_BREAK);
		buffer.append("Transaction type:").append(COMMA).append("Class conversion").append(LINE_BREAK);
		//SSE024, mask ssn if no download report full ssn permission
        boolean maskSSN = true;
		try{
        	maskSSN =ReportDownloadHelper.isMaskedSsn(userProfile, currentContract.getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        }
		if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) {
			buffer.append("Name:").append(COMMA).append(theReport.getParticipantName()).append(LINE_BREAK);
		//	buffer.append("SSN:").append(COMMA).append(theReport.getParticipantSSN()).append(LINE_BREAK);
		buffer.append("SSN:").append(COMMA).append(SSNRender.format(theReport.getParticipantUnmaskedSSN(),"", maskSSN)).append(LINE_BREAK);
		}
		
		buffer.append("Invested date:").append(COMMA).append(
				DateRender.format(theReport.getTransactionDate(),RenderConstants.MEDIUM_YMD_SLASHED))
				.append(LINE_BREAK);
		buffer.append("Request date:").append(COMMA).append(
				DateRender.format(theReport.getRequestDate(),RenderConstants.MEDIUM_YMD_SLASHED))
				.append(LINE_BREAK);
		buffer.append("Total amount transferred out:").append(COMMA).append(theReport.getTotalFromAmount()).append(LINE_BREAK);
		buffer.append("Total amount transferred in:").append(COMMA).append(theReport.getTotalToAmount()).append(LINE_BREAK);
		buffer.append("Transaction number:").append(COMMA).append(theReport.getTransactionNumber()).append(LINE_BREAK);
		buffer.append("Submission method:").append(COMMA).append(theReport.getMediaCode()).append(LINE_BREAK);
		buffer.append("Source of transfer:").append(COMMA).append(theReport.getSourceOfTransfer()).append(LINE_BREAK);

	
		// Titles - Class Conversion Summary section
		buffer.append(LINE_BREAK).append(LINE_BREAK);
		buffer.append("Class conversion summary:").append(LINE_BREAK);
		buffer.append(" ").append(COMMA);		
		buffer.append("Risk category:").append(COMMA);		
		buffer.append("Investment option:").append(COMMA);
		buffer.append("Transfer Out ($):").append(COMMA);
		buffer.append("Unit Value").append(COMMA);
		buffer.append("Number of Units").append(COMMA);
		buffer.append("Transfer In ($):").append(COMMA);
		buffer.append("Unit Value").append(COMMA);
		buffer.append("Number of Units").append(COMMA);
		buffer.append(LINE_BREAK).append(LINE_BREAK);
		
		List summaryList = theReport.getTransferFromsAndTos();
		Iterator it = summaryList.iterator();
		while (it.hasNext()) {
			FundGroup category = (FundGroup)it.next();;
			if (category != null) {
				Object o[] = category.getFunds();
				for (int i=0; i < o.length; i++) {
					buffer.append(" ").append(COMMA);		
					buffer.append(category.getGroupName()).append(COMMA);
					ClassConversionDetailsFund fund = (ClassConversionDetailsFund)o[i];
					if (fund != null) {
						buffer.append(fund.getName()).append(COMMA);
						buffer.append(fund.getAmount()).append(COMMA);
						buffer.append(fund.getDisplayUnitValue()).append(COMMA);
						buffer.append(fund.getNumberOfUnits()).append(COMMA);
						buffer.append(fund.getToAmount()).append(COMMA);
						buffer.append(fund.getDisplayToUnitValue()).append(COMMA);
						buffer.append(fund.getToNumberOfUnits()).append(COMMA);
					}
					buffer.append(LINE_BREAK);
				}	
			}
		} 
		
		// Titles - Details section
		buffer.append(LINE_BREAK).append(LINE_BREAK);
		buffer.append("Class conversion details:").append(LINE_BREAK);
		buffer.append(" ").append(COMMA);		
		buffer.append("Risk category:").append(COMMA);		
		buffer.append("Investment option:").append(COMMA);
		buffer.append("Money Type:").append(COMMA);
		buffer.append("Amount ($):").append(COMMA);
		buffer.append("Unit value:").append(COMMA);
		buffer.append("Number of units:").append(COMMA).append(LINE_BREAK);
		
		Collection detailsList = theReport.getDetails();
		it = detailsList.iterator();
		while (it.hasNext()) {
			FundGroup category = (FundGroup)it.next();;
			if (category != null) {
				Object o[] = category.getFunds();
				for (int i=0; i < o.length; i++) {
					buffer.append(" ").append(COMMA);		
					buffer.append(category.getGroupName()).append(COMMA);
					TransactionDetailsFund fund = (TransactionDetailsFund)o[i];
					if (fund != null) {
						buffer.append(fund.getName()).append(COMMA);
						buffer.append(fund.getMoneyTypeDescription()).append(COMMA);
						buffer.append(fund.getAmount()).append(COMMA);
						if (fund.getUnitValue().doubleValue() == (double)0.0) {
							buffer.append(" ").append(COMMA);
						} else {
							buffer.append(fund.getDisplayUnitValue()).append(COMMA);
						}
						if (fund.getNumberOfUnits().doubleValue() == (double)0.0) {
							buffer.append(" ").append(COMMA);
						} else {
							buffer.append(fund.getNumberOfUnits()).append(COMMA);
						}
					}
					buffer.append(LINE_BREAK);
				}
			}
		}
		

		if (logger.isDebugEnabled())
			logger.debug("exit <- getDownloadData");

		return buffer.toString().getBytes();
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
