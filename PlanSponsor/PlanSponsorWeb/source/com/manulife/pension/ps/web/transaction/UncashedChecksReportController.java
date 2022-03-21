package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.util.Arrays;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.transaction.valueobject.UncashedChecksReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.UncashedChecksReportItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.PilotHelper;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * UncashedChecksReportAction class - This class is used to get the information
 * for the Uncashed checks page.
 * 
 * @author suresh l
 */
@Controller
@RequestMapping( value = "/transaction")
@SessionAttributes({"uncashedChecksReportForm"})


public class UncashedChecksReportController extends ReportController {

	@ModelAttribute("uncashedChecksReportForm") 
	public  CashAccountReportForm populateForm() 
	{
		return new  CashAccountReportForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/uncashedChecksReport.jsp"); 
		forwards.put("default","/transaction/uncashedChecksReport.jsp");
		forwards.put("sort","/transaction/uncashedChecksReport.jsp"); 
		forwards.put("filter","/transaction/uncashedChecksReport.jsp");
		forwards.put("page","/transaction/uncashedChecksReport.jsp"); 
		forwards.put("print","/transaction/uncashedChecksReport.jsp");}

	
	protected static final String DEFAULT_SORT_FIELD = UncashedChecksReportItem.SORT_CHECK_ISSUE_DATE;
	protected static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;

	private static Logger logger = Logger
			.getLogger(UncashedChecksReportController.class);

	private static final String MSG_NO_CHECKS = "There are no uncashed checks for the current month";
	private static final String AMOUNT_FORMAT = "########0.00";

	// CSV Export Related
	private static final String DOWNLOAD_COLUMN_HEADING_LOAN_SUMMARY = "Check issue date,Participant Name,SSN,Payee type,Payee name,Check amount ($),Transaction date,Transaction type,Transaction number,Check status";
	
	private static final int MAINTENANCE_ERROR_CODE = ErrorCodes.TECHNICAL_DIFFICULTIES;
	
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	/**
	 * Constructor for UncashedChecksReportAction.
	 */
	public UncashedChecksReportController() {
		super(UncashedChecksReportController.class);
	}

	/**
	 * @see PsController#execute(ActionMapping, ActionForm, HttpServletRequest,
	 *      HttpServletResponse)
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            BaseReportForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 */
	public String preExecute(
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException,
			ServletException {

		UserProfile userProfile = SessionHelper.getUserProfile(request);

		int contractNumber = userProfile.getCurrentContract()
				.getContractNumber();

		boolean isInPilot = PilotHelper.isInPilot(contractNumber,
				"Uncashed Checks");
		if (!isInPilot) {
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}

		return null;
	}
	
	@RequestMapping(value = "/uncashedChecksReport/", method = {RequestMethod.GET})
	public String doDefault(
			@Valid @ModelAttribute("uncashedChecksReportForm") CashAccountReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("linkrepay");
			}
		}
		 forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	 
	public String doCommon( CashAccountReportForm actionForm, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {

       String forward = super.doCommon(actionForm, request, response);
       
       // display error message for table maintenance
       // but bypass application logging in BaseReportAction.doCommon
       UncashedChecksReportData reportData = (UncashedChecksReportData) request.getAttribute(CommonConstants.REPORT_BEAN);
       if (reportData != null && reportData.isInMaintenance()) {
           
           // signal to display error message
           setErrorsInRequest(
                   request,
                   Arrays.asList(
                           new GenericException(MAINTENANCE_ERROR_CODE)));
           
           // signal to not render report table
           request.removeAttribute(CommonConstants.REPORT_BEAN);
           
       }
       
       return forward;
       
    }

	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> getDownloadData");

		UncashedChecksReportData reportData = (UncashedChecksReportData) report;
		StringBuffer buffer = new StringBuffer();

		Contract currentContract = getUserProfile(request).getCurrentContract();
		buffer.append("Contract").append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
				currentContract.getCompanyName()).append(LINE_BREAK);

		// Uncashed check's information
		buffer.append("As of,"
				+ DateRender.format(reportData.getAsOfDate(),
						RenderConstants.MEDIUM_MDY_SLASHED));
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		buffer.append("Total value of uncashed checks,$"
				+ NumberRender.formatByPattern(reportData
						.getUncashedChecksValue(), ZERO_AMOUNT_STRING,
						AMOUNT_FORMAT));
		buffer.append(LINE_BREAK);
		buffer.append("Number of stale dated checks,"
				+ reportData.getNumStaleDatedChecks());
		buffer.append(",Total value of stale dated checks,$"
				+ NumberRender.formatByPattern(reportData
						.getStaleDatedChecksValue(), ZERO_AMOUNT_STRING,
						AMOUNT_FORMAT));
		buffer.append(LINE_BREAK);

		buffer.append("Number of outstanding checks,"
				+ reportData.getNumOutstandingChecks());
		buffer.append(",Total value of outstanding checks,$"
				+ NumberRender.formatByPattern(reportData
						.getOutstandingChecksValue(), ZERO_AMOUNT_STRING,
						AMOUNT_FORMAT));

		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		buffer.append("Checks Details");
		buffer.append(LINE_BREAK);

		if (reportData.getDetails() != null
				&& reportData.getDetails().size() > 0) {

			// SSE024, mask ssn if no download report full ssn
			// permission
			boolean maskSSN = true;
			try {
				maskSSN = ReportDownloadHelper.isMaskedSsn(
						getUserProfile(request), currentContract
								.getContractNumber());

			} catch (SystemException se) {
				logger.error(se);
				// log exception and output blank ssn
			}

			buffer.append(DOWNLOAD_COLUMN_HEADING_LOAN_SUMMARY);
			for (Iterator it = reportData.getDetails().iterator(); it.hasNext();) {
				UncashedChecksReportItem item = (UncashedChecksReportItem) it
						.next();
				buffer.append(LINE_BREAK);
				buffer.append(item.getCheckIssueDate());
				buffer.append(COMMA);
				buffer.append((item.getParticipantName() == null || item
						.getParticipantName().equals("")) ? COMMA : "\""
						+ item.getParticipantName() + "\"" + COMMA);
				buffer.append(SSNRender.format(item.getSsn(), "", maskSSN))
						.append(COMMA);
				buffer.append((item.getPayeeType() == null || item
						.getPayeeType().equals("")) ? COMMA :  "\""
						+item.getPayeeType()+ "\"" +COMMA);

				buffer.append((item.getPayeeName() == null || item
						.getPayeeName().equals("")) ? COMMA : "\""+ 
						item.getPayeeName()+ "\""+COMMA);
				buffer.append(NumberRender.formatByPattern(item
						.getCheckAmount(), ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
				buffer.append(COMMA);
				buffer.append((item.getTransactionDate() == null || item
						.getTransactionDate().equals("")) ? COMMA : item
						.getTransactionDate()
						+ COMMA);
				buffer.append((item.getTransactionType() == null || item
						.getTransactionType().equals("")) ? COMMA : "\""
						+item.getTransactionType()+ "\""
						+ COMMA);
				buffer.append((item.getTransactionNumber() == null || item
						.getTransactionNumber().equals("")) ? COMMA : item
						.getTransactionNumber()
						+ COMMA);
				buffer.append(item.getCheckStatus());
			}
		} else {
			buffer.append(MSG_NO_CHECKS);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getDownloadData");
		return buffer.toString().getBytes();
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return UncashedChecksReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return UncashedChecksReportData.REPORT_NAME;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria
	 * (com.manulife.pension.service.report.valueobject.ReportCriteria,
	 * com.manulife.pension.ps.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter(UncashedChecksReportData.FILTER_CONTRACT_NO,
				new Integer(currentContract.getContractNumber()));

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
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
	@RequestMapping(value = "/uncashedChecksReport/", params = {"task=filter"}, method = {RequestMethod.GET})
	public String doFilter(@Valid @ModelAttribute("uncashedChecksReportForm") CashAccountReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("default");
			}
		}
		 forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/uncashedChecksReport/", params = {"task=page"}, method = {RequestMethod.GET})
	public String doPage(@Valid @ModelAttribute("uncashedChecksReportForm") CashAccountReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("default");
			}
		}
		 forward = super.doPage(form, request, response);
		 return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/uncashedChecksReport/", params = {"task=sort"}, method = {RequestMethod.GET})
	public String doSort(@Valid @ModelAttribute("uncashedChecksReportForm") CashAccountReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("default");
			}
		} 
		 forward = super.doSort(form, request, response);
		 return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value ="/uncashedChecksReport/", params={"task=download"}, method =  {RequestMethod.GET}) 
	public String doDownload (@Valid @ModelAttribute("uncashedChecksReportForm") CashAccountReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	        forward=super.doDownload(form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
}