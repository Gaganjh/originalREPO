package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.report.util.WithdrawalDetailsUtility;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFund;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsRebalReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.content.ContentConstants;
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
@SessionAttributes({"transactionDetailsRebalForm"})

public class TransactionDetailsRebalController extends ReportController {
	@ModelAttribute("transactionDetailsRebalForm")
	public  TransactionDetailsRebalForm populateForm()
	{
		return new  TransactionDetailsRebalForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("input","/transaction/transactionDetailsRebalReport.jsp"); 
		forwards.put("default","/transaction/transactionDetailsRebalReport.jsp");
		forwards.put("print","/transaction/transactionDetailsRebalReport.jsp");
		forwards.put("filter","/transaction/transactionDetailsRebalReport.jsp");
		forwards.put("page","/transaction/transactionDetailsRebalReport.jsp");
		}

	
	private static final String SSN_DEFAULT_VALUE = "000000000";
	private static final String DOWNLOAD_FILES_TASK = "downloadFiles";
	private static final BigDecimal ZERO = new BigDecimal(0d);
	private static final int TRANSACTION_BEFORE_SECTION=0;
	private static final int TRANSACTION_AFTER_SECTION=1;
	private static final int TRANSACTION_DETAILS_SECTION=2;
	
	protected String getDefaultSort() {
		return TransactionDetailsRebalReportData.SORT_FIELD_WEBSRTNO;
	}

	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}
	
	/**
	 * Creates the .CSV file based on the LoanRepaymentDetailsReportData object
	 * 
	 * @param out
	 * @param report
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request) throws SystemException {
	
		if (logger.isDebugEnabled())
			logger.debug("entry -> populateDownloadData");
	
		TransactionDetailsRebalReportData data = (TransactionDetailsRebalReportData) report;
		TransactionDetailsRebalForm form = (TransactionDetailsRebalForm)reportForm; 
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Transaction details").append(LINE_BREAK);
		buffer.append("Rebalance").append(LINE_BREAK);
		
	    Contract currentContract = getUserProfile(request).getCurrentContract();
	    buffer.append("Contract number:").append(COMMA).append(currentContract.getContractNumber());
	    buffer.append(LINE_BREAK);
	    buffer.append("Company name:").append(COMMA).append(currentContract.getCompanyName());
		buffer.append(LINE_BREAK);
	
		// get the content objects
		Content message = null;

		buffer.append("Rebalance summary:").append(LINE_BREAK);
		// Transaction Type
		buffer
			.append("Transaction type:")
			.append(COMMA)
			.append("Inter-account transfer - Rebalance")
			.append(LINE_BREAK);

		UserProfile userProfile = getUserProfile(request);
		boolean isDB = userProfile.getCurrentContract().isDefinedBenefitContract();
		if (isDB==false) {
		// Name
		buffer
			.append("Name:")
			.append(COMMA)
				.append(data.getParticipantName()) // last, first
			.append(LINE_BREAK);
		
		// SSN
	       //SSE S024 determine wheather the ssn should be masked on the csv report
        boolean maskSSN = true;// set the mask ssn flag to true as a default
        UserProfile user = getUserProfile(request);
        try{
        	maskSSN =ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber() );
         
        }catch (SystemException se)
        {
      	  logger.error(se);
      	// log exception and output blank ssn
      }
		buffer
			.append("SSN:")
			.append(COMMA)
			.append(SSNRender.format(data.getParticipantUnmaskedSSN(),"",maskSSN))
			//.append(data.getParticipantSSN())
			.append(LINE_BREAK);
		}

		// Transaction date
		buffer.append("Invested date:").append(COMMA);
		if (data.getTransactionDate() != null) {
			buffer.append(DateRender.format(data.getTransactionDate(), RenderConstants.MEDIUM_YMD_SLASHED));
		}
		buffer.append(LINE_BREAK);

		// Payroll Ending
		buffer
			.append("Request date:")
			.append(COMMA)
			.append(DateRender.format(data.getRequestDate(), RenderConstants.MEDIUM_YMD_SLASHED))
			.append(LINE_BREAK);
	
		
		// Transaction number
		buffer.append("Transaction number:").append(COMMA);
		if (data.getTransactionNumber() != null) {
			buffer.append(data.getTransactionNumber());
		}
		buffer.append(LINE_BREAK);

		// Submission method
		buffer
			.append("Submission method:")
			.append(COMMA)
			.append(data.getMediaCode());
		
		buffer.append(LINE_BREAK).append(LINE_BREAK);
	
		// Totals (Regular Contributions)
		// headings
		String headingEeEr = "";
		// fore and aft Line-breaks added withing getCVSDetails.
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);		
		boolean totalEEDollarsAreZero = false;
		boolean totalERDollarsAreZero = false;
		if (data.getTotalEEBeforeAmount().doubleValue() == (double)0.0 ) {
			totalEEDollarsAreZero = true;
		}
		if (data.getTotalERBeforeAmount().doubleValue() == (double)0.0 ) {
			totalERDollarsAreZero = true;
		}

		buffer.append("Account before change:");
		buffer.append(getColumnHeaders(TRANSACTION_BEFORE_SECTION, totalEEDollarsAreZero, 
				                       totalERDollarsAreZero, isDB));
		buffer.append(LINE_BREAK);
		
		buffer.append(getCSVDetails((ArrayList)data.getBeforeChange(), TRANSACTION_BEFORE_SECTION, totalEEDollarsAreZero, totalERDollarsAreZero, isDB));
		// Before Totals 
		buffer.append(COMMA+COMMA+"Total:"+COMMA);
		//CL103592 fix - CSV Report Incorrect Display
		if(!isDB){
			if (data.getTotalEEBeforeAmount().doubleValue() != (double)0.0) {
				buffer.append(data.getTotalEEBeforeAmount()).append(COMMA);
				buffer.append(data.getTotalEEBeforePct()).append(COMMA);
			} else {
			    buffer.append(" ").append(COMMA);		
			    buffer.append(" ").append(COMMA);
			}
		}
		
		if (data.getTotalERBeforeAmount().doubleValue() != (double)0.0) {
			buffer.append(data.getTotalERBeforeAmount()).append(COMMA);
			buffer.append(data.getTotalERBeforePct()).append(COMMA);
		} else {
			buffer.append(" ").append(COMMA);		
			buffer.append(" ").append(COMMA);
		}
		
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
	
		totalEEDollarsAreZero = false;
		totalERDollarsAreZero = false;
		if (data.getTotalEEAfterAmount().doubleValue() == 0 ) {
			totalEEDollarsAreZero = true;
		}
		if (data.getTotalERAfterAmount().doubleValue() == 0 ) {
			totalERDollarsAreZero = true;
		}

		buffer.append("Account after change:");
		buffer.append(getColumnHeaders(TRANSACTION_AFTER_SECTION, totalEEDollarsAreZero, 
				                       totalERDollarsAreZero, isDB));
		buffer.append(LINE_BREAK);
		buffer.append(getCSVDetails((ArrayList)data.getAfterChange(), TRANSACTION_AFTER_SECTION, totalEEDollarsAreZero, totalERDollarsAreZero, isDB));
		
		// After Totals
		buffer.append(COMMA+COMMA+"Total:"+COMMA);
		//CL103592 fix - CSV Report Incorrect Display
		if(!isDB){

			if (data.getTotalEEAfterAmount().doubleValue() != (double)0.0) {
				buffer.append(data.getTotalEEAfterAmount()).append(COMMA);		
				buffer.append(data.getTotalEEAfterPct()).append(COMMA);
			} else {
				buffer.append(" ").append(COMMA);		
				buffer.append(" ").append(COMMA);
			}
		}
		if (data.getTotalERAfterAmount().doubleValue() != (double)0.0) {
			buffer.append(data.getTotalERAfterAmount()).append(COMMA);
			buffer.append(data.getTotalERAfterPct()).append(COMMA);
		} else {
			buffer.append(" ").append(COMMA);		
			buffer.append(" ").append(COMMA);
		}
	
		buffer.append(LINE_BREAK);
		
		// Insert Redemption Fees / MVA as needed
		if (data.getRedemptionFees().doubleValue() > (double)0.0) {
			buffer.append(LINE_BREAK);
			String feeMsg = TransactionDetailsFTFController.getMiscContent(ContentConstants.MESSAGE_REDEMPTION_FEE_APPLED, data.getRedemptionFees().abs().negate());
			buffer.append(feeMsg);
		}
		if (data.getMva().doubleValue() > (double)0.0) {
			buffer.append(LINE_BREAK);
			String feeMsg = TransactionDetailsFTFController.getMiscContent(ContentConstants.MESSAGE_MVA_APPLIED, data.getMva().abs().negate());
			buffer.append(feeMsg);
		}
		
		
		buffer.append(LINE_BREAK);
		buffer.append("Rebalance details:");		
		buffer.append(getColumnHeaders(TRANSACTION_DETAILS_SECTION, false, false, isDB));
		buffer.append(LINE_BREAK);
		buffer.append(getCSVDetails((ArrayList)data.getDetails(), TRANSACTION_DETAILS_SECTION, false, false, isDB));		
		buffer.append(LINE_BREAK);
	
		if (logger.isDebugEnabled())
			logger.debug("exit <- populateDownloadData");
		return buffer.toString().getBytes();
	}

	private String getColumnHeaders(int type, boolean totalEEIsZeroDollars, 
			                        boolean totalERIsZeroDollars, boolean isDB) {
		StringBuffer buf = new StringBuffer();
		buf.append(LINE_BREAK);
		if (type == TRANSACTION_DETAILS_SECTION) {
			buf.append(COMMA);
			buf.append("Risk category:").append(COMMA);
			buf.append("Investment option:").append(COMMA);					
			buf.append("Money type:").append(COMMA);
			buf.append("Amount ($):").append(COMMA);
			buf.append("Unit value:").append(COMMA);
			buf.append("Number of units:").append(COMMA);
			buf.append("Comments:").append(COMMA);					
		} else {
			buf.append(COMMA);
			buf.append("Risk category:").append(COMMA);
			buf.append("Investment option:").append(COMMA);
			if (isDB==false) {
			if (!totalEEIsZeroDollars) {
				buf.append("Employee assets ($):").append(COMMA);
				buf.append("% of account:").append(COMMA);
			} else {
				buf.append(" ").append(COMMA);
				buf.append(" ").append(COMMA);				
			}
			}
			if (!totalERIsZeroDollars) {
				buf.append("Employer assets ($):").append(COMMA);
				buf.append("% of account:").append(COMMA);
			} else {
				buf.append(" ").append(COMMA);
				buf.append(" ").append(COMMA);				
			}
		}
		return buf.toString();
	}
	
	private String getCSVDetails(ArrayList data, int type, boolean totalEEIsZeroDollars, boolean totalERIsZeroDollars, boolean isDB) {
		StringBuffer buf = new StringBuffer();
		buf.append(LINE_BREAK);
		for (int i=0; i < data.size(); i++) {
			FundGroup group = (FundGroup)data.get(i);
			for (int j=0; j < group.getFunds().length; j++) {
				TransactionDetailsFund fund = (TransactionDetailsFund)group.getFunds()[j];
				if (type == TRANSACTION_DETAILS_SECTION) {
					buf.append(COMMA);					
					buf.append(group.getGroupName()).append(COMMA);
					buf.append(fund.getName()).append(COMMA);
					buf.append(fund.getMoneyTypeDescription()).append(COMMA);
					buf.append(fund.getAmount()).append(COMMA);					
					buf.append(fund.getDisplayUnitValue()).append(COMMA);
					if (fund.getNumberOfUnits().compareTo(ZERO)==0) {
						buf.append(" ").append(COMMA);
					} else {
						buf.append(fund.getDisplayNumberOfUnits()).append(COMMA);
					}
					buf.append(fund.getComments()).append(COMMA);					
				} else {
					buf.append(COMMA);					
					buf.append(group.getGroupName()).append(COMMA);
					buf.append(fund.getName()).append(COMMA);					
					if (isDB==false) {
					if ( totalEEIsZeroDollars ) {
						buf.append(" ").append(COMMA);
						buf.append(" ").append(COMMA);							
					} else {
						buf.append(fund.getEmployeeAmount()).append(COMMA);						
						buf.append(fund.getEmployeePercentage()).append(COMMA);
					}
					}
					if ( totalERIsZeroDollars ) {
						buf.append(" ").append(COMMA);							
						buf.append(" ").append(COMMA);							
					} else {
						buf.append(fund.getEmployerAmount()).append(COMMA);
						buf.append(fund.getEmployerPercentage()).append(COMMA);
					}
				}
				buf.append(LINE_BREAK);
			}
		}
		
		return buf.toString();
	}
	
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm actionForm, HttpServletRequest request) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		
		TransactionDetailsRebalForm form = (TransactionDetailsRebalForm)actionForm;

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
		String participantId = (String)request.getParameter("pptId");
		if (participantId == null || participantId.equals("")) {
			participantId = (String)request.getParameter("participantId"); // leave support for legacy reference just to be safe
			if (participantId == null || participantId.equals("")) {
				participantId = form.getPptId();
			}
		}
		
		criteria.addFilter(TransactionDetailsRebalReportData.FILTER_TRANSACTION_NUMBER, transactionNumber);
		criteria.addFilter(TransactionDetailsRebalReportData.FILTER_PARTICIPANT_ID, participantId);
		criteria.addFilter(TransactionDetailsRebalReportData.FILTER_CONTRACT_NUMBER, contractNumber);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}		
	}

	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm actionForm, HttpServletRequest request) throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateSortCriteria");
		}

		// default sort is risk category			
		TransactionDetailsRebalForm form = (TransactionDetailsRebalForm) actionForm;
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();
		
		criteria.insertSort(sortField, sortDirection);
		
		// add additional sort criteria websrtno and monty type description
		criteria.insertSort(TransactionDetailsRebalReportData.SORT_FIELD_WEBSRTNO, ReportSort.ASC_DIRECTION);
		criteria.insertSort(TransactionDetailsRebalReportData.SORT_FIELD_MONEY_TYPE_DESCRIPTION, ReportSort.ASC_DIRECTION);
								
		if (logger.isDebugEnabled()) {
			logger.debug("populateSortCriteria: inserting sort with field:"+sortField+" and direction: " + sortDirection);
		}
	}
	
	
	public TransactionDetailsRebalController() {
		super(TransactionDetailsRebalController.class);
	}
	
	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return TransactionDetailsRebalReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return TransactionDetailsRebalReportData.REPORT_NAME;
	}

	public String doCommon(BaseReportForm reportForm, HttpServletRequest request,
								  HttpServletResponse response) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
				
		String forward=null;
		forward = super.doCommon(reportForm, request, response);
		TransactionDetailsRebalReportData report = (TransactionDetailsRebalReportData)request.getAttribute(Constants.REPORT_BEAN);		
		TransactionDetailsRebalForm form = (TransactionDetailsRebalForm) reportForm;
		//If the request has errors, return to page with error message. Otherwise proceed.
		if ((report)!=null && (report.getDetails().size() != 0)) {		
			form.setReport(report);
			form.setTransactionNumber(report.getTransactionNumber());
		}						
		return forward;
	}
	@RequestMapping(value ="/transactionDetailsRebalReport/", method = {RequestMethod.POST,
			RequestMethod.GET })
	public String doDefault(
			@Valid @ModelAttribute("transactionDetailsRebalForm") TransactionDetailsRebalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
			}
		}
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/transactionDetailsRebalReport/", params= {"task=filter"},method = {
			RequestMethod.GET })
	public String doFilter(
			@Valid @ModelAttribute("transactionDetailsRebalForm") TransactionDetailsRebalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
			}
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		
	}
	@RequestMapping(value ="/transactionDetailsRebalReport/", params= {"task=page"},method = {
			RequestMethod.GET })
	public String doPage(
			@Valid @ModelAttribute("transactionDetailsRebalForm") TransactionDetailsRebalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		
	}	
	@RequestMapping(value ="/transactionDetailsRebalReport/", params={"task=download"} , method =  {RequestMethod.GET}) 
	public String doDownload(@Valid @ModelAttribute("transactionDetailsRebalForm") TransactionDetailsRebalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	@RequestMapping(value = "/transactionDetailsRebalReport/", params = {"task=print"}, method = {RequestMethod.GET })
	public String doPrint(@Valid @ModelAttribute("transactionDetailsRebalForm") TransactionDetailsRebalForm form,
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



