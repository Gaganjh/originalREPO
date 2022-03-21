package com.manulife.pension.bd.web.bob.transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.report.BOBReportController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFund;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsRebalReportData;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.account.valueobject.FundGroup;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.exception.ResourceLimitExceededException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

/**
 * 
 * Action class for Rebalance details report
 * 
 * @author Vanidha
 * 
 */
@Controller
@RequestMapping( value = "/bob")
@SessionAttributes({"transactionDetailsRebalForm"})

public class TransactionDetailsRebalController extends BOBReportController {
	@ModelAttribute("transactionDetailsRebalForm") 
	public TransactionDetailsRebalForm populateForm() 
	{
		return new TransactionDetailsRebalForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/transactionDetailsRebalReport.jsp");
		forwards.put("default","/transaction/transactionDetailsRebalReport.jsp");
		forwards.put("page","/transaction/transactionDetailsRebalReport.jsp");
		forwards.put("filter","/transaction/transactionDetailsRebalReport.jsp");
		forwards.put("print","/transaction/transactionDetailsRebalReport.jsp");
		}

	
	private static final BigDecimal ZERO = new BigDecimal(0d);
	private static final int TRANSACTION_BEFORE_SECTION = 0;
	private static final int TRANSACTION_AFTER_SECTION = 1;
	private static final int TRANSACTION_DETAILS_SECTION = 2;

	private static final String REBALANCE_DETAILS_REPORT = "RebalanceDetails";
	private static final String XSLT_FILE_KEY_NAME = "TransactionRebalanceDetailsReport.XSLFile";
	private static final String TXN_TYPE = "Inter-account transfer - Rebalance";

	public TransactionDetailsRebalController() {
		super(TransactionDetailsRebalController.class);
	}

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
	@SuppressWarnings("unchecked")
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		TransactionDetailsRebalReportData data = (TransactionDetailsRebalReportData) report;

		StringBuffer buffer = new StringBuffer();

		buffer.append("Transaction details").append(LINE_BREAK);
		buffer.append("Rebalance").append(LINE_BREAK);

		Contract currentContract = getBobContext(request).getCurrentContract();
		buffer.append("Contract number:").append(COMMA)
				.append(currentContract.getContractNumber());
		buffer.append(LINE_BREAK);
		buffer.append("Company name:").append(COMMA)
				.append(currentContract.getCompanyName());
		buffer.append(LINE_BREAK);

		// get the content objects

		buffer.append("Rebalance summary:").append(LINE_BREAK);
		// Transaction Type
		buffer.append("Transaction type:").append(COMMA)
				.append("Inter-account transfer - Rebalance")
				.append(LINE_BREAK);

		boolean isDB = currentContract.isDefinedBenefitContract();
		if (isDB == false) {
			// Name
			buffer.append("Name:").append(COMMA)
					.append(data.getParticipantName()) // last, first
					.append(LINE_BREAK);

			// SSN
			buffer.append("SSN:").append(COMMA)
					.append(data.getParticipantSSN()).append(LINE_BREAK);
		}

		// Transaction date
		buffer.append("Invested date:").append(COMMA);
		if (data.getTransactionDate() != null) {
			buffer.append(DateRender.format(data.getTransactionDate(),
					RenderConstants.MEDIUM_YMD_SLASHED));
		}
		buffer.append(LINE_BREAK);

		// Payroll Ending
		buffer.append("Request date:")
				.append(COMMA)
				.append(DateRender.format(data.getRequestDate(),
						RenderConstants.MEDIUM_YMD_SLASHED)).append(LINE_BREAK);

		// Transaction number
		buffer.append("Transaction number:").append(COMMA);
		if (data.getTransactionNumber() != null) {
			buffer.append(data.getTransactionNumber());
		}
		buffer.append(LINE_BREAK);

		// Submission method
		buffer.append("Submission method:").append(COMMA)
				.append(data.getMediaCode());

		buffer.append(LINE_BREAK).append(LINE_BREAK);

		// Totals (Regular Contributions)
		// headings
		// fore and aft Line-breaks added within getCVSDetails.
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		boolean totalEEDollarsAreZero = false;
		boolean totalERDollarsAreZero = false;
		if (data.getTotalEEBeforeAmount().doubleValue() == (double) 0.0) {
			totalEEDollarsAreZero = true;
		}
		if (data.getTotalERBeforeAmount().doubleValue() == (double) 0.0) {
			totalERDollarsAreZero = true;
		}

		boolean showComments = (data.getMva().doubleValue() != (double) 0.0)
				|| (data.getRedemptionFees().doubleValue() != (double) 0.0);
		buffer.append("Account Before Rebalance:");
		buffer.append(getColumnHeaders(TRANSACTION_BEFORE_SECTION,
				totalEEDollarsAreZero, totalERDollarsAreZero, isDB,
				showComments));
		buffer.append(LINE_BREAK);

		buffer.append(getCSVDetails((ArrayList) data.getBeforeChange(),
				TRANSACTION_BEFORE_SECTION, totalEEDollarsAreZero,
				totalERDollarsAreZero, isDB, showComments));
		// Before Totals
		buffer.append(COMMA + COMMA + "Total:" + COMMA);
		if (!isDB) {
			if (data.getTotalEEBeforeAmount().doubleValue() != (double) 0.0) {
				buffer.append(data.getTotalEEBeforeAmount()).append(COMMA);
				buffer.append(data.getTotalEEBeforePct()).append(COMMA);
			} else {
				buffer.append(" ").append(COMMA);
				buffer.append(" ").append(COMMA);
			}
		}

		if (data.getTotalERBeforeAmount().doubleValue() != (double) 0.0) {
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
		if (data.getTotalEEAfterAmount().doubleValue() == 0) {
			totalEEDollarsAreZero = true;
		}
		if (data.getTotalERAfterAmount().doubleValue() == 0) {
			totalERDollarsAreZero = true;
		}

		buffer.append("Account After Rebalance:");
		buffer.append(getColumnHeaders(TRANSACTION_AFTER_SECTION,
				totalEEDollarsAreZero, totalERDollarsAreZero, isDB,
				showComments));
		buffer.append(LINE_BREAK);
		buffer.append(getCSVDetails((ArrayList) data.getAfterChange(),
				TRANSACTION_AFTER_SECTION, totalEEDollarsAreZero,
				totalERDollarsAreZero, isDB, showComments));

		// After Totals
		buffer.append(COMMA + COMMA + "Total:" + COMMA);
		if (!isDB) {
			if (data.getTotalEEAfterAmount().doubleValue() != (double) 0.0) {
				buffer.append(data.getTotalEEAfterAmount()).append(COMMA);
				buffer.append(data.getTotalEEAfterPct()).append(COMMA);
			} else {
				buffer.append(" ").append(COMMA);
				buffer.append(" ").append(COMMA);
			}
		}
		if (data.getTotalERAfterAmount().doubleValue() != (double) 0.0) {
			buffer.append(data.getTotalERAfterAmount()).append(COMMA);
			buffer.append(data.getTotalERAfterPct()).append(COMMA);
		} else {
			buffer.append(" ").append(COMMA);
			buffer.append(" ").append(COMMA);
		}

		buffer.append(LINE_BREAK);

		// Insert Redemption Fees / MVA as needed
		String message, contentParams;
		if (data.getRedemptionFees().doubleValue() > (double) 0.0) {
			buffer.append(LINE_BREAK);
			contentParams = NumberRender.formatByType(data.getRedemptionFees()
					.abs().negate(), null, RenderConstants.CURRENCY_TYPE);
			message = ContentHelper.getContentTextWithParamsSubstitution(
					BDContentConstants.MESSAGE_REDEMPTION_FEE_APPLED,
					ContentTypeManager.instance().MISCELLANEOUS, null,
					removeParanthesesAndPrefixMinus(contentParams));
			buffer.append(message);
		}
		if (data.getMva().doubleValue() > (double) 0.0) {
			buffer.append(LINE_BREAK);
			contentParams = NumberRender.formatByType(data.getMva().abs()
					.negate(), null, RenderConstants.CURRENCY_TYPE);
			message = ContentHelper.getContentTextWithParamsSubstitution(
					BDContentConstants.MESSAGE_MVA_APPLIED,
					ContentTypeManager.instance().MISCELLANEOUS, null,
					removeParanthesesAndPrefixMinus(contentParams));
			buffer.append(message);
		}

		buffer.append(LINE_BREAK);
		buffer.append("Rebalance Details:");
		buffer.append(getColumnHeaders(TRANSACTION_DETAILS_SECTION, false,
				false, isDB, showComments));
		buffer.append(LINE_BREAK);
		buffer.append(getCSVDetails((ArrayList) data.getDetails(),
				TRANSACTION_DETAILS_SECTION, false, false, isDB, showComments));
		buffer.append(LINE_BREAK);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/**
	 * Returns back the column header
	 * 
	 * @param type
	 * @param totalEEIsZeroDollars
	 * @param totalERIsZeroDollars
	 * @param isDB
	 * 
	 * @return String
	 */
	private String getColumnHeaders(int type, boolean totalEEIsZeroDollars,
			boolean totalERIsZeroDollars, boolean isDB, boolean showComments) {
		StringBuffer buf = new StringBuffer();
		buf.append(LINE_BREAK);
		if (type == TRANSACTION_DETAILS_SECTION) {
			buf.append(COMMA);
			buf.append("Risk Category:").append(COMMA);
			buf.append("Investment Option:").append(COMMA);
			buf.append("Money Type:").append(COMMA);
			buf.append("Amount ($):").append(COMMA);
			buf.append("Unit Value:").append(COMMA);
			buf.append("Number Of Units:").append(COMMA);
			if (showComments)
				buf.append("Comments:").append(COMMA);
		} else {
			buf.append(COMMA);
			buf.append("Risk Category:").append(COMMA);
			buf.append("Investment Option:").append(COMMA);
			if (isDB == false) {
				if (!totalEEIsZeroDollars) {
					buf.append("Employee Assets ($):").append(COMMA);
					buf.append("% Of Account:").append(COMMA);
				} else {
					buf.append(" ").append(COMMA);
					buf.append(" ").append(COMMA);
				}
			}
			if (!totalERIsZeroDollars) {
				buf.append("Employer Assets ($):").append(COMMA);
				buf.append("% Of Account:").append(COMMA);
			} else {
				buf.append(" ").append(COMMA);
				buf.append(" ").append(COMMA);
			}
		}
		return buf.toString();
	}

	/**
	 * Returns the CSV details
	 * 
	 * @param data
	 * @param type
	 * @param totalEEIsZeroDollars
	 * @param totalERIsZeroDollars
	 * @param isDB
	 * 
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	private String getCSVDetails(ArrayList data, int type,
			boolean totalEEIsZeroDollars, boolean totalERIsZeroDollars,
			boolean isDB, boolean showComments) {
		StringBuffer buf = new StringBuffer();
		buf.append(LINE_BREAK);
		for (int i = 0; i < data.size(); i++) {
			FundGroup group = (FundGroup) data.get(i);
			TransactionDetailsFund fund = null;
			for (int j = 0; j < group.getFunds().length; j++) {
				fund = (TransactionDetailsFund) group.getFunds()[j];
				if (type == TRANSACTION_DETAILS_SECTION) {
					buf.append(COMMA);
					buf.append(group.getGroupName()).append(COMMA);
					buf.append(fund.getName()).append(COMMA);
					buf.append(fund.getMoneyTypeDescription()).append(COMMA);
					buf.append(fund.getAmount()).append(COMMA);
					buf.append(fund.getDisplayUnitValue()).append(COMMA);
					if (fund.getNumberOfUnits().compareTo(ZERO) == 0) {
						buf.append(" ").append(COMMA);
					} else {
						buf.append(fund.getDisplayNumberOfUnits())
								.append(COMMA);
					}
					buf.append(fund.getComments()).append(COMMA);
				} else {
					buf.append(COMMA);
					buf.append(group.getGroupName()).append(COMMA);
					buf.append(fund.getName()).append(COMMA);
					if (isDB == false) {
						if (totalEEIsZeroDollars) {
							buf.append(" ").append(COMMA);
							buf.append(" ").append(COMMA);
						} else {
							buf.append(fund.getEmployeeAmount()).append(COMMA);
							buf.append(fund.getEmployeePercentage()).append(
									COMMA);
						}
					}
					if (totalERIsZeroDollars) {
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

	/**
	 * @see BaseReportController#populateReportCriteria()
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm actionForm, HttpServletRequest request)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		TransactionDetailsRebalForm form = (TransactionDetailsRebalForm) actionForm;

		String transactionNumber = (String) request
				.getParameter("transactionNumber");
		if (StringUtils.isEmpty(transactionNumber)) {
			transactionNumber = form.getTransactionNumber();
		}

		BobContext bobContext = getBobContext(request);
		Contract currentContract = bobContext.getCurrentContract();

		String contractNumber = String.valueOf(currentContract
				.getContractNumber());
		if (contractNumber == null || contractNumber.equals("")
				|| contractNumber.equals("0")) {
			contractNumber = form.getContractNumber();
		}
		String participantId = (String) request.getParameter("pptId");
		if (participantId == null || participantId.equals("")) {
			participantId = (String) request.getParameter("participantId"); // leave
																			// support
																			// for
																			// legacy
																			// reference
																			// just
																			// to
																			// be
																			// safe
			if (participantId == null || participantId.equals("")) {
				participantId = form.getPptId();
			}
		}

		criteria.addFilter(
				TransactionDetailsRebalReportData.FILTER_TRANSACTION_NUMBER,
				transactionNumber);
		criteria.addFilter(
				TransactionDetailsRebalReportData.FILTER_PARTICIPANT_ID,
				participantId);
		criteria.addFilter(
				TransactionDetailsRebalReportData.FILTER_CONTRACT_NUMBER,
				contractNumber);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}
	}

	/**
	 * The method populates the sort criteria
	 * 
	 * @param criteria
	 * @param actionForm
	 * @param request
	 * 
	 * @throws SystemException
	 */
	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm actionForm, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateSortCriteria");
		}

		// default sort is risk category
		TransactionDetailsRebalForm form = (TransactionDetailsRebalForm) actionForm;
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		criteria.insertSort(sortField, sortDirection);

		// add additional sort criteria websrtno and monty type description
		criteria.insertSort(
				TransactionDetailsRebalReportData.SORT_FIELD_WEBSRTNO,
				ReportSort.ASC_DIRECTION);
		criteria.insertSort(
				TransactionDetailsRebalReportData.SORT_FIELD_MONEY_TYPE_DESCRIPTION,
				ReportSort.ASC_DIRECTION);

		if (logger.isDebugEnabled()) {
			logger.debug("populateSortCriteria: inserting sort with field:"
					+ sortField + " and direction: " + sortDirection);
		}
	}

	/**
	 * @see BaseReportController#getReportId()
	 */
	protected String getReportId() {
		return TransactionDetailsRebalReportData.REPORT_ID;
	}

	/**
	 * @see BaseReportController#getReportName()
	 */
	protected String getReportName() {
		return TransactionDetailsRebalReportData.REPORT_NAME;
	}

	/**
	 * The getReportData() method of BaseReportAction class is being overridden,
	 * so that the user cannot see the Resource Limit Exceeded Exception
	 * message. Instead, he will see a Technical Difficulties message.
	 */
	protected ReportData getReportData(String reportId,
			ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {
		ReportData reportData = null;

		try {
			reportData = super.getReportData(reportId, reportCriteria, request);
		} catch (ResourceLimitExceededException e) {
			logger.error("Received a ResourceLimitExceededException: ", e);
			throw new SystemException(
					e,
					"ResourceLimitExceededException occurred. Showing it as TECHNICAL_DIFFICULTIES to the user.");
		}

		return reportData;
	}

	/**
	 * @see BDReportController#doCommon()
	 */
	public String doCommon(
			TransactionDetailsRebalForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}

		String forward = null;
		try {
			forward = super.doCommon( actionForm, request, response);
		} catch (SystemException e) {
			// Log the system exception.
			LogUtility.logSystemException(BDConstants.BD_APPLICATION_ID, e);

			// Show user friendly message.
			List<GenericException> errors = new ArrayList<GenericException>();
			errors.add(new GenericException(BDErrorCodes.TECHNICAL_DIFFICULTIES));
			setErrorsInRequest(request, errors);
			forward = forwards.get("input");
			return forward;
		}

		TransactionDetailsRebalReportData report = (TransactionDetailsRebalReportData) request
				.getAttribute(BDConstants.REPORT_BEAN);
		
		// If the request has errors, return to page with error message.
		// Otherwise proceed.
		if ((report) != null && (report.getDetails().size() != 0)) {
			actionForm.setReport(report);
			actionForm.setTransactionNumber(report.getTransactionNumber());
		}
		return forward;
	}
	@RequestMapping(value ="/transaction/transactionDetailsRebalReport/" , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("transactionDetailsRebalForm") TransactionDetailsRebalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		 String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
		if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	 forward=super.doDefault( form, request, response);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
    @RequestMapping(value ="/transaction/transactionDetailsRebalReport/",params={"task=filter"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doFilter (@Valid @ModelAttribute("transactionDetailsRebalForm") TransactionDetailsRebalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	 String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	 forward=super.doFilter( form, request, response);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }	
    
    	 @RequestMapping(value ="/transaction/transactionDetailsRebalReport/" ,params={"task=page"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
 	    public String doPage (@Valid @ModelAttribute("transactionDetailsRebalForm") TransactionDetailsRebalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 	    		throws IOException,ServletException, SystemException {
    		 String forward = preExecute(form, request, response);
  	    	if(StringUtils.isNotBlank(forward)) {
  	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
  	    	}
 		   if(bindingResult.hasErrors()){
 			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 			   if(errDirect!=null){
 				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
 			   }
 		   }
 		    forward=super.doPage( form, request, response);
 		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 	   }
 	   
 	   @RequestMapping(value ="/transaction/transactionDetailsRebalReport/" ,params={"task=sort"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
 	   public String doSort (@Valid @ModelAttribute("transactionDetailsRebalForm") TransactionDetailsRebalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 	   throws IOException,ServletException, SystemException {
 		  String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
 		   if(bindingResult.hasErrors()){
 			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 			   if(errDirect!=null){
 				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
 			   }
 		   }
 		    forward=super.doSort( form, request, response);
 		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 	   }
 	   @RequestMapping(value ="/transaction/transactionDetailsRebalReport/", params={"task=download"},method =  {RequestMethod.POST,RequestMethod.GET}) 
 	   public String doDownload (@Valid @ModelAttribute("transactionDetailsRebalForm") TransactionDetailsRebalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 	   throws IOException,ServletException, SystemException {
 		  String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
 		   if(bindingResult.hasErrors()){
 			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 			   if(errDirect!=null){
 				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
 			   }
 		   }
 		    forward=super.doDownload( form, request, response);
 		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 	   }
 	   
 	   @RequestMapping(value ="/transaction/transactionDetailsRebalReport/", params={"task=downloadAll"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
 	   public String doDownloadAll (@Valid @ModelAttribute("transactionDetailsRebalForm") TransactionDetailsRebalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 	   throws IOException,ServletException, SystemException {
 		  String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
 		   if(bindingResult.hasErrors()){
 			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 			   if(errDirect!=null){
 				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
 			   }
 		   }
 		    forward=super.doDownloadAll( form, request, response);
 		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 	   }
 	  @RequestMapping(value = "/transaction/transactionDetailsRebalReport/", params = {"task=printPDF"}, method = {RequestMethod.GET })
 			public String doPrintPDF(@Valid @ModelAttribute("transactionDetailsRebalForm") TransactionDetailsRebalForm form,
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
 						return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
 																												// default
 					}
 				}
 				 forward = super.doPrintPDF(form, request, response);
 				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
 			}
	/**
	 * This method is used to frame the file name used for the downloaded CSV.
	 * 
	 * @param BaseReportForm
	 * @param HttpServletRequest
	 * @return The file name used for the downloaded CSV.
	 */

	protected String getFileName(BaseReportForm form,
			HttpServletRequest request) {
		Contract currentContract = getBobContext(request).getCurrentContract();
		String formattedTransactionDate = DateRender.formatByPattern(
				currentContract.getContractDates().getAsOfDate(), null,
				RenderConstants.MEDIUM_YMD_DASHED,
				RenderConstants.MEDIUM_MDY_SLASHED).replace(
				BDConstants.SLASH_SYMBOL, BDConstants.SPACE_SYMBOL);
		String csvFileName = REBALANCE_DETAILS_REPORT + "-"
				+ currentContract.getContractNumber() + "-"
				+ formattedTransactionDate + CSV_EXTENSION;
		return csvFileName;
	}

	/**
	 * @See BaseReportAction#prepareXMLFromReport()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Document prepareXMLFromReport(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws ParserConfigurationException {

		TransactionDetailsRebalForm form = (TransactionDetailsRebalForm) reportForm;
		TransactionDetailsRebalReportData data = (TransactionDetailsRebalReportData) report;
		int rowCount = 1;
		int maxRowsinPDF;

		PDFDocument doc = new PDFDocument();

		// Gets layout page for transactionDetailsRebalReport.jsp
		LayoutPage layoutPageBean = getLayoutPage(
				BDPdfConstants.REBALANCE_DETAILS_PATH, request);

		Element rootElement = doc
				.createRootElement(BDPdfConstants.REBALANCE_DETAILS);

		// Sets Logo, Page Name, Contract Details, Intro-1, Intro-2.
		setIntroXMLElements(layoutPageBean, doc, rootElement, request);

		boolean showComments = (data.getMva().doubleValue() != (double) 0.0)
				|| (data.getRedemptionFees().doubleValue() != (double) 0.0);
		boolean isDBContract = getBobContext(request).getCurrentContract()
				.isDefinedBenefitContract();

		// Sets Summary Info.
		setSummaryInfoXMLElements(doc, rootElement, data, layoutPageBean,
				isDBContract);

		String bodyHeader = ContentUtility.getContentAttributeText(
				layoutPageBean, BDContentConstants.BODY1_HEADER, null);
		PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc,
				bodyHeader);

		bodyHeader = ContentUtility.getContentAttributeText(layoutPageBean,
				BDContentConstants.BODY2_HEADER, null);
		PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER2, rootElement, doc,
				bodyHeader);

		bodyHeader = ContentUtility.getContentAttributeText(layoutPageBean,
				BDContentConstants.BODY3_HEADER, null);
		PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER3, rootElement, doc,
				bodyHeader);

		// Sets Information Messages.
		setMessagesXMLElements(data, doc, rootElement);

		// Sets before_balance and after_balance details.
		setBeforeAfterFundDetailsXMLElements(data, doc, rootElement,
				isDBContract);

		// Gets number of rows present in report page.
		int noOfRows = getNumberOfRowsInReport(report);

		if (noOfRows > 0) {
			// Main Report - start
			Element fundDetailsElement = doc
					.createElement(BDPdfConstants.FUND_DETAILS);
			Iterator categoryIterator = data.getDetails().iterator();
			// Gets number of rows to be shown in PDF.
			maxRowsinPDF = form.getCappedRowsInPDF();
			for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {

				Element fundDetailElement = doc
						.createElement(BDPdfConstants.FUND_DETAIL);
				FundGroup theItem = (FundGroup) categoryIterator.next();
				if (theItem != null) {
					// Sets fund group.
					doc.appendTextNode(fundDetailElement,
							BDPdfConstants.FUND_GROUP, theItem.getGroupName());
					for (Fund fund : theItem.getFunds()) {
						if (rowCount <= maxRowsinPDF) {
							setRebalanceDetailsXMLElements(doc,
									fundDetailElement, fund, showComments);
							rowCount++;
						}
					}
				}
				doc.appendElement(fundDetailsElement, fundDetailElement);

			}
			doc.appendElement(rootElement, fundDetailsElement);
			// Main Report - end
		}

		if (form.getPdfCapped()) {
			doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED,
					getPDFCappedText());
		}

		setFooterXMLElements(layoutPageBean, doc, rootElement, request);

		return doc.getDocument();

	}

	/**
	 * @See BaseReportAction#getNumberOfRowsInReport() Each fund detail
	 *      comprises one row in report table and so this method is overridden
	 *      and modified.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Integer getNumberOfRowsInReport(ReportData report) {
		int noOfRows = 0;
		if (report.getDetails() != null) {
			for (FundGroup theItem : (ArrayList<FundGroup>) report.getDetails()) {
				noOfRows += theItem.getFunds().length;
			}
		}
		return noOfRows;
	}

	/**
	 * @See BaseReportAction#getXSLTFileName()
	 */
	@Override
	public String getXSLTFileName() {
		return XSLT_FILE_KEY_NAME;
	}

	/**
	 * This method sets summary information XML elements
	 * 
	 * @param doc
	 * @param rootElement
	 * @param data
	 * @param layoutPageBean
	 * @param request
	 */
	private void setSummaryInfoXMLElements(PDFDocument doc,
			Element rootElement, TransactionDetailsRebalReportData data,
			LayoutPage layoutPageBean, boolean isDBContract) {

		Element summaryInfoElement = doc
				.createElement(BDPdfConstants.SUMMARY_INFO);

		String subHeader = ContentUtility.getContentAttributeText(
				layoutPageBean, BDContentConstants.SUB_HEADER, null);
		PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement,
				doc, subHeader);

		doc.appendTextNode(summaryInfoElement, BDPdfConstants.TXN_TYPE,
				TXN_TYPE);

		String formattedDate = DateRender.formatByPattern(
				data.getTransactionDate(), null,
				RenderConstants.MEDIUM_YMD_DASHED,
				RenderConstants.MEDIUM_MDY_SLASHED);
		doc.appendTextNode(summaryInfoElement, BDPdfConstants.INVESTED_DATE,
				formattedDate);

		formattedDate = DateRender.formatByPattern(data.getRequestDate(), null,
				RenderConstants.MEDIUM_YMD_DASHED,
				RenderConstants.MEDIUM_MDY_SLASHED);
		doc.appendTextNode(summaryInfoElement, BDPdfConstants.REQUEST_DATE,
				formattedDate);

		doc.appendTextNode(summaryInfoElement, BDPdfConstants.TXN_NUMBER,
				data.getTransactionNumber());

		doc.appendTextNode(summaryInfoElement,
				BDPdfConstants.SUBMISSION_METHOD, data.getMediaCode());

		if (!isDBContract) {
			// Display "Employee Assets($)" and "% of Account" columns in both
			// before_balance
			// and after_balance tables
			doc.appendTextNode(rootElement, BDPdfConstants.NOT_DB_CONTRACT,
					null);
			doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_NAME,
					data.getParticipantName());
			doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_SSN,
					data.getParticipantSSN());
		}

		doc.appendElement(rootElement, summaryInfoElement);

	}

	/**
	 * This method creates XML elements for both before_balance and
	 * after_balance details.
	 * 
	 * @param data
	 * @param doc
	 * @param rootElement
	 */
	@SuppressWarnings("unchecked")
	private void setBeforeAfterFundDetailsXMLElements(
			TransactionDetailsRebalReportData data, PDFDocument doc,
			Element rootElement, boolean isDBContract) {

		// Fund Details - start
		Element beforeBalanceDetailsElement = doc
				.createElement(BDPdfConstants.BEFORE_CHANGE_FUND_DETAILS);
		Element afterBalanceDetailsElement = doc
				.createElement(BDPdfConstants.AFTER_CHANGE_FUND_DETAILS);
		Element fundDetailElement;
		String totalAmt = null;
		String totalPercent = null;

		for (FundGroup categoryIteratorBeforeBalance : (List<FundGroup>) data
				.getBeforeChange()) {
			fundDetailElement = doc.createElement(BDPdfConstants.FUND_DETAIL);
			if (categoryIteratorBeforeBalance != null) {
				// Sets before_balance group.
				doc.appendTextNode(fundDetailElement,
						BDPdfConstants.FUND_GROUP,
						categoryIteratorBeforeBalance.getGroupName());
				// Sets before_balance items.
				setFundItems(data, categoryIteratorBeforeBalance, doc,
						fundDetailElement, true, isDBContract);
			}
			doc.appendElement(beforeBalanceDetailsElement, fundDetailElement);
		}

		for (FundGroup categoryIteratorAfterBalance : (List<FundGroup>) data
				.getAfterChange()) {
			fundDetailElement = doc.createElement(BDPdfConstants.FUND_DETAIL);
			if (categoryIteratorAfterBalance != null) {
				// Sets after_balance group.
				doc.appendTextNode(fundDetailElement,
						BDPdfConstants.FUND_GROUP,
						categoryIteratorAfterBalance.getGroupName());
				// Sets after_balance items.
				setFundItems(data, categoryIteratorAfterBalance, doc,
						fundDetailElement, false, isDBContract);
			}
			doc.appendElement(afterBalanceDetailsElement, fundDetailElement);
		}

		if (!isDBContract) {
			// Sets total employee before amt and percent if not DB contract.
			if (data.getTotalEEBeforeAmount().doubleValue() != (double) 0.0) {
				totalAmt = NumberRender.formatByType(
						data.getTotalEEBeforeAmount(), null, null);
				doc.appendTextNode(beforeBalanceDetailsElement,
						BDPdfConstants.TXN_TOTAL_EE_AMT, totalAmt);
				totalPercent = NumberRender.formatByType(
						data.getTotalEEBeforePct(), null, null);
				doc.appendTextNode(beforeBalanceDetailsElement,
						BDPdfConstants.TXN_TOTAL_EE_PERCENT, totalPercent);
			}
		}
		if (data.getTotalERBeforeAmount().doubleValue() != (double) 0.0) {
			totalAmt = NumberRender.formatByType(data.getTotalERBeforeAmount(),
					null, null);
			doc.appendTextNode(beforeBalanceDetailsElement,
					BDPdfConstants.TXN_TOTAL_ER_AMT, totalAmt);
			totalPercent = NumberRender.formatByType(
					data.getTotalERBeforePct(), null, null);
			doc.appendTextNode(beforeBalanceDetailsElement,
					BDPdfConstants.TXN_TOTAL_ER_PERCENT, totalPercent);
		}
		if (!isDBContract) {
			// Sets total employee after amt and percent if not DB contract.
			if (data.getTotalEEAfterAmount().doubleValue() != (double) 0.0) {
				totalAmt = NumberRender.formatByType(
						data.getTotalEEAfterAmount(), null, null);
				doc.appendTextNode(afterBalanceDetailsElement,
						BDPdfConstants.TXN_TOTAL_EE_AMT, totalAmt);
				totalPercent = NumberRender.formatByType(
						data.getTotalEEAfterPct(), null, null);
				doc.appendTextNode(afterBalanceDetailsElement,
						BDPdfConstants.TXN_TOTAL_EE_PERCENT, totalPercent);
			}
		}
		if (data.getTotalERAfterAmount().doubleValue() != (double) 0.0) {
			totalAmt = NumberRender.formatByType(data.getTotalERAfterAmount(),
					null, null);
			doc.appendTextNode(afterBalanceDetailsElement,
					BDPdfConstants.TXN_TOTAL_ER_AMT, totalAmt);
			totalPercent = NumberRender.formatByType(data.getTotalERAfterPct(),
					null, null);
			doc.appendTextNode(afterBalanceDetailsElement,
					BDPdfConstants.TXN_TOTAL_ER_PERCENT, totalPercent);
		}

		doc.appendElement(rootElement, beforeBalanceDetailsElement);
		doc.appendElement(rootElement, afterBalanceDetailsElement);
		// Fund Details - end

	}

	/**
	 * This method creates XML elements for each fund item for before_balance
	 * and after_balance details.
	 * 
	 * @param data
	 * @param theItem
	 * @param doc
	 * @param isFromFund
	 * @param fundDetailElement
	 */
	private void setFundItems(TransactionDetailsRebalReportData data,
			FundGroup theItem, PDFDocument doc, Element fundDetailElement,
			boolean isFromFund, boolean isDBContract) {

		for (Fund fund : theItem.getFunds()) {
			Element fundTxnElement = doc.createElement(BDPdfConstants.FUND_TXN);
			TransactionDetailsFund txnDetailsFund = (TransactionDetailsFund) fund;
			if (txnDetailsFund != null) {
				doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_NAME,
						txnDetailsFund.getName());
				if (!isDBContract) {
					// Sets employee before amt and percent if not DB contract.
					if (data.getTotalEEBeforeAmount().doubleValue() != (double) 0.0) {
						String employeeAmt = NumberRender.formatByType(
								txnDetailsFund.getEmployeeAmount(), null, null);
						doc.appendTextNode(fundTxnElement,
								BDPdfConstants.FUND_EE_AMT, employeeAmt);
						String employeePercent = NumberRender.formatByType(
								txnDetailsFund.getEmployeePercentage(), null,
								null);
						doc.appendTextNode(fundTxnElement,
								BDPdfConstants.FUND_EE_PERCENT, employeePercent);
					}

				}
				if (data.getTotalERBeforeAmount().doubleValue() != (double) 0.0) {
					// Sets employee after amt and percent if not DB contract.
					String employerAmt = NumberRender.formatByType(
							txnDetailsFund.getEmployerAmount(), null, null);
					doc.appendTextNode(fundTxnElement,
							BDPdfConstants.FUND_ER_AMT, employerAmt);
					String employerPercent = NumberRender.formatByType(
							txnDetailsFund.getEmployerPercentage(), null, null);
					doc.appendTextNode(fundTxnElement,
							BDPdfConstants.FUND_ER_PERCENT, employerPercent);
				}
			}
			doc.appendElement(fundDetailElement, fundTxnElement);
		}

	}

	/**
	 * This method sets XML elements for info messages
	 * 
	 * @param data
	 * @param doc
	 * @param rootElement
	 */
	private void setMessagesXMLElements(TransactionDetailsRebalReportData data,
			PDFDocument doc, Element rootElement) {
		String message, contentParams;
		int msgNum = 0;
		Element infoMessagesElement = doc
				.createElement(BDPdfConstants.INFO_MESSAGES);
		if (data.getRedemptionFees().doubleValue() > (double) 0.0) {
			// Sets Redemption Fees Message if redemption fees has been applied
			// to the transaction.
			Element messageElement = doc.createElement(BDPdfConstants.MESSAGE);
			doc.appendTextNode(messageElement, BDPdfConstants.MESSAGE_NUM,
					String.valueOf(++msgNum));
			contentParams = NumberRender.formatByType(data.getRedemptionFees()
					.abs().negate(), null, RenderConstants.CURRENCY_TYPE);
			message = ContentHelper.getContentTextWithParamsSubstitution(
					BDContentConstants.MESSAGE_REDEMPTION_FEE_APPLED,
					ContentTypeManager.instance().MISCELLANEOUS, null,
					removeParanthesesAndPrefixMinus(contentParams));
			PdfHelper.convertIntoDOM(BDPdfConstants.MESSAGE_TEXT,
					messageElement, doc, message);
			doc.appendElement(infoMessagesElement, messageElement);
		}
		if (data.getMva().doubleValue() > (double) 0.0) {
			// Sets MVA Message if MVA has been applied to the transaction.
			Element messageElement = doc.createElement(BDPdfConstants.MESSAGE);
			doc.appendTextNode(messageElement, BDPdfConstants.MESSAGE_NUM,
					String.valueOf(++msgNum));
			contentParams = NumberRender.formatByType(data.getMva().abs()
					.negate(), null, RenderConstants.CURRENCY_TYPE);
			message = ContentHelper.getContentTextWithParamsSubstitution(
					BDContentConstants.MESSAGE_MVA_APPLIED,
					ContentTypeManager.instance().MISCELLANEOUS, null,
					removeParanthesesAndPrefixMinus(contentParams));
			PdfHelper.convertIntoDOM(BDPdfConstants.MESSAGE_TEXT,
					messageElement, doc, message);
			doc.appendElement(infoMessagesElement, messageElement);
		}
		doc.appendElement(rootElement, infoMessagesElement);
	}

	/**
	 * This method sets fund to fund transfer details XML elements
	 * 
	 * @param doc
	 * @param fundDetailElement
	 * @param fund
	 * @param showComments
	 */
	private void setRebalanceDetailsXMLElements(PDFDocument doc,
			Element fundDetailElement, Fund fund, boolean showComments) {
		Element fundTxnElement = doc.createElement(BDPdfConstants.FUND_TXN);
		TransactionDetailsFund txnDetailsFund = (TransactionDetailsFund) fund;
		if (txnDetailsFund != null) {
			doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_NAME,
					txnDetailsFund.getName());
			if (StringUtils.isNotBlank(txnDetailsFund.getComments())) {
				doc.appendTextNode(fundTxnElement, BDPdfConstants.MONEY_TYPE,
						BDConstants.NO_RULE);
			} else {
				doc.appendTextNode(fundTxnElement, BDPdfConstants.MONEY_TYPE,
						txnDetailsFund.getMoneyTypeDescription());
			}
			String fundAmt = NumberRender.formatByType(
					txnDetailsFund.getAmount(), null,
					RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
			// Needs to remove parantheses surrounded by amt and prefix minus
			// with
			// the amt.
			doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_AMT,
					removeParanthesesAndPrefixMinus(fundAmt));
			if (txnDetailsFund.displayUnitValue()) {
				String unitValue = NumberRender.formatByType(
						txnDetailsFund.getDisplayUnitValue(), null, null);
				doc.appendTextNode(fundTxnElement,
						BDPdfConstants.PS_UNIT_VALUE, unitValue);
			}
			if (txnDetailsFund.displayNumberOfUnits()) {

				String numberOfUnits = NumberRender.formatByType(
						txnDetailsFund.getDisplayNumberOfUnits(), null, null,
						6, BigDecimal.ROUND_HALF_DOWN, 1);
				doc.appendTextNode(fundTxnElement,
						BDPdfConstants.PS_NUM_OF_UNITS, numberOfUnits);
			} else {
				doc.appendTextNode(fundTxnElement,
						BDPdfConstants.PS_NUM_OF_UNITS, BDConstants.NO_RULE);
			}
			if (showComments) {
				// Sets text in "Comments" column
				doc.appendTextNode(fundTxnElement, BDPdfConstants.COMMENTS,
						txnDetailsFund.getComments());
			}
		}
		doc.appendElement(fundDetailElement, fundTxnElement);
	}

	/**
	 /** This code has been changed and added  to 
	 /	Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}

}
