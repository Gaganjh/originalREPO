package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanSummaryItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanSummaryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.transaction.LoanSummaryReportForm;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWLoanSummary;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

@Controller
@RequestMapping(value = "/transaction")
@SessionAttributes({"loanSummaryReportForm"})
public class LoanSummaryReportController extends ReportController {

	@ModelAttribute("loanSummaryReportForm")
	public LoanSummaryReportForm populateForm() {
		return new LoanSummaryReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/transaction/loanSummaryReport.jsp");
		forwards.put("default", "/transaction/loanSummaryReport.jsp");
		forwards.put("sort", "/transaction/loanSummaryReport.jsp");
		forwards.put("filter", "/transaction/loanSummaryReport.jsp");
		forwards.put("page", "/transaction/loanSummaryReport.jsp");
		forwards.put("print", "/transaction/loanSummaryReport.jsp");
		forwards.put("save", "/transaction/loanSummaryReport.jsp");
		forwards.put("ContactXSS", "redirect:/do/transaction/loanSummaryReport/");
	}

	protected static final String DEFAULT_SORT_FIELD = LoanSummaryItem.SORT_NAME;
	protected static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;

	private static final String AMOUNT_FORMAT = "########0.00";
	private static Logger logger = Logger.getLogger(LoanSummaryReportController.class);

	// CSV Export Related
	private static final String DOWNLOAD_COLUMN_HEADING_LOAN_SUMMARY = "Name, SSN,Loan number,Issue Date,Interest rate(%),Original loan amount*($),  Outstanding balance ($), Last payment ($), Last payment date,Maturity date, Alert";

	private static final String MSG_NO_LOANS = "There are currently no loans.";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria(com.
	 * manulife.pension.service.report.valueobject.ReportCriteria,
	 * com.manulife.pension.ps.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) {

		// get the user profile object and set the current contract to null
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter(LoanSummaryReportData.FILTER_CONTRACT_NO, new Integer(currentContract.getContractNumber()));

	}

	

	public String doCommon(LoanSummaryReportForm form, BindingResult bindingResult, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		String forward = super.doCommon(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/loanSummaryReport/", params = {"task=filter"}, method = {RequestMethod.GET})
	public String doFilter(@Valid @ModelAttribute("loanSummaryReportForm") LoanSummaryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			LoanSummaryReportData data = (LoanSummaryReportData) request.getSession()
					.getAttribute(Constants.LOAN_SUMMARY_REPORT_BEAN);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("ContactXSS");
			}
		}
		String forward = super.doFilter(form, request, response);
		updateReportData(request);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value = "/loanSummaryReport/", params = {"task=page"}, method = {RequestMethod.GET})
	public String doPage(@Valid @ModelAttribute("loanSummaryReportForm") LoanSummaryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			LoanSummaryReportData data = (LoanSummaryReportData) request.getSession()
					.getAttribute(Constants.LOAN_SUMMARY_REPORT_BEAN);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("ContactXSS");
			}
		}
		String forward = super.doPage(form, request, response);
		updateReportData(request);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value = "/loanSummaryReport/", params = {"task=sort"}, method = {RequestMethod.GET})
	public String doSort(@Valid @ModelAttribute("loanSummaryReportForm") LoanSummaryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			LoanSummaryReportData data = (LoanSummaryReportData) request.getSession()
					.getAttribute(Constants.LOAN_SUMMARY_REPORT_BEAN);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("ContactXSS");
			}
		} 
		String forward = super.doSort(form, request, response);
		updateReportData(request);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value = "/loanSummaryReport/", params = {"task=download"}, method = {RequestMethod.GET})
	public String doDownload(@Valid @ModelAttribute("loanSummaryReportForm") LoanSummaryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("ContactXSS");
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value = "/loanSummaryReport/", params = {"task=dowanloadAll"}, method = {RequestMethod.GET})
	public String doDownloadAll(@Valid @ModelAttribute("loanSummaryReportForm") LoanSummaryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			LoanSummaryReportData data = (LoanSummaryReportData) request.getSession()
					.getAttribute(Constants.LOAN_SUMMARY_REPORT_BEAN);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("ContactXSS");
			}
		}
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return LoanSummaryReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return LoanSummaryReportData.REPORT_NAME;
	}

	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	protected byte[] getDownloadData(BaseReportForm LoanSummaryReportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> populateDownloadData");

		LoanSummaryReportData reportData = (LoanSummaryReportData) report;
		StringBuffer buffer = new StringBuffer();

		Contract currentContract = getUserProfile(request).getCurrentContract();
		buffer.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(COMMA)
				.append(currentContract.getCompanyName()).append(LINE_BREAK);

		// Loanholder's information
		buffer.append("As of," + DateRender.format(reportData.getAsOfDate(), RenderConstants.MEDIUM_MDY_SLASHED));
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append("Total outstanding loan balance,$"
				+ NumberRender.formatByPattern(reportData.getOutstandingBalance(), ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
		buffer.append(LINE_BREAK);
		buffer.append("Number of loans," + reportData.getNumLoans());
		buffer.append(LINE_BREAK);
		buffer.append("Number of participants with loans," + reportData.getNumParticipants());
		buffer.append(LINE_BREAK);

		buffer.append(",,,,,,");
		buffer.append(LINE_BREAK);
		buffer.append("Loan Summary Details,,,,,,");
		buffer.append(LINE_BREAK);
		buffer.append(DOWNLOAD_COLUMN_HEADING_LOAN_SUMMARY);
		// SSE024, mask ssn if no download report full ssn permission
		boolean maskSSN = true;
		try {
			maskSSN = ReportDownloadHelper.isMaskedSsn(getUserProfile(request), currentContract.getContractNumber());

		} catch (SystemException se) {
			logger.error(se);
			// log exception and output blank ssn
		}
		// Loan Summary Detail Data
		if (reportData.getDetails() != null && reportData.getDetails().size() > 0) {
			for (Iterator it = reportData.getDetails().iterator(); it.hasNext();) {
				LoanSummaryItem item = (LoanSummaryItem) it.next();
				buffer.append(LINE_BREAK);
				buffer.append(QUOTE).append(item.getName()).append(QUOTE).append(COMMA);
				// buffer.append(SSNRender.format(item.getSsn(), "xxx-xx-"))
				buffer.append(SSNRender.format(item.getSsn(), "", maskSSN)).append(COMMA);
				buffer.append(item.getLoanNumber()).append(COMMA);

				buffer.append(DateRender.format(item.getCreationDate(), RenderConstants.MEDIUM_MDY_SLASHED))
						.append(COMMA);
				buffer.append(item.getInterestRate()).append(COMMA);
				buffer.append(item.getLoanAmt()).append(COMMA);

				if (item.getOutstandingBalance() != null) {
					buffer.append(NumberRender.formatByPattern(item.getOutstandingBalance(), ZERO_AMOUNT_STRING,
							AMOUNT_FORMAT)).append(COMMA);
				} else {
					buffer.append(COMMA);
				}

				if (item.isNoRepayment()) {

					buffer.append("n/a").append(COMMA);
				} else {
					buffer.append(
							NumberRender.formatByPattern(item.getLastRepaymentAmt(), ZERO_AMOUNT_STRING, AMOUNT_FORMAT))
							.append(COMMA);
				}
				if (item.isNoRepayment()) {
					buffer.append("n/a").append(COMMA);
				} else {
					buffer.append(DateRender.format(item.getLastRepaymentDate(), RenderConstants.MEDIUM_MDY_SLASHED))
							.append(COMMA);
				}
				buffer.append(DateRender.format(item.getMaturityDate(), RenderConstants.MEDIUM_MDY_SLASHED))
						.append(COMMA);
				String[] alerts = item.getAlerts();
				if (alerts.length > 0) {
					buffer.append(QUOTE);
					for (int i = 0; i < alerts.length; i++) {
						buffer.append(alerts[i]);
						if (i < alerts.length - 1)
							buffer.append(" ");
					}
					buffer.append(QUOTE);
				}

			}
		} else {
			buffer.append(MSG_NO_LOANS);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- populateDownloadData");
		return buffer.toString().getBytes();
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations as part of the CL#136970.
	 */
	/*
	 * @SuppressWarnings("rawtypes") public Collection doValidate( Form form,
	 * HttpServletRequest request) { Collection penErrors =
	 * PsValidation.doValidatePenTestAutoAction(form, mapping, request,
	 * CommonConstants.INPUT); if (penErrors != null && penErrors.size() > 0) {
	 * request.getSession().setAttribute(CommonConstants.ERROR_RDRCT,
	 * mapping.findForward("ContactXSS")); LoanSummaryReportData data =
	 * (LoanSummaryReportData) request .getSession().getAttribute(
	 * Constants.LOAN_SUMMARY_REPORT_BEAN); request.removeAttribute(PsBaseAction.ERROR_KEY);
	 * populateReportForm( (BaseReportForm) form, request);
	 * request.setAttribute(CommonConstants.REPORT_BEAN, data); return penErrors; }
	 * return super.doValidate( form, request); }
	 */
	@RequestMapping(value ="/loanSummaryReport/" , method ={RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("loanSummaryReportForm") LoanSummaryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if input
			}
		}
		String forward = super.doDefault(form, request, response);
		updateReportData(request);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	/*
     * This method to save extended maturity date value to DB ( CARES act work)
    */
	@RequestMapping(value = "/loanSummaryReport/",params = {"task=save"}, method = {RequestMethod.POST})
	public String doSave(@Valid @ModelAttribute("loanSummaryReportForm") LoanSummaryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException,ParseException {
		if (bindingResult.hasErrors()) {
			LoanSummaryReportData data = (LoanSummaryReportData) request.getSession()
					.getAttribute(Constants.LOAN_SUMMARY_REPORT_BEAN);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("ContactXSS");
			}
		}
		Contract currentContract = getUserProfile(request).getCurrentContract();
		Integer tpaProfile = (int) getUserProfile(request).getPrincipal().getProfileId();
		String tpaProfileId = tpaProfile.toString();
		String tpaProfileName = (String) getUserProfile(request).getPrincipal().getFirstName() + " "
				+ (String) getUserProfile(request).getPrincipal().getLastName();
		String role = getUserProfile(request).getRole().getRoleId();
		String[] selectedLoanDetails = form.getLoanDetails();
		super.doCommon(form, request, response);
		ArrayList<String> unselectedLoansList = getUnselectedLoanList(request);
		ParticipantServiceDelegate.getInstance().UpdateLoanDetails(selectedLoanDetails, unselectedLoansList,
				currentContract.getContractNumber(), tpaProfileId, tpaProfileName);

		updateReportData(request);// set the value in LoanDetailsItems so that
		String cuttofDate = getCutoffDate();
		request.setAttribute("errorcheck",
				"Request has been saved successfully. The changes will be reflected after "+cuttofDate);
		form = new LoanSummaryReportForm();// reset the form to clear the old
											// loan details value
		return forwards.get("save");
	}
	
	private ArrayList<String> getUnselectedLoanList(HttpServletRequest request) throws SystemException {
		ArrayList<String> unselectedLoans = new ArrayList<String>();
		List<LoanSummaryItem> details = new ArrayList<LoanSummaryItem>();
			LoanSummaryReportData reportData = (LoanSummaryReportData) request
					.getAttribute(CommonConstants.REPORT_BEAN);
			if (reportData.getDetails() != null && reportData.getDetails().size() > 0) {
				for (Iterator it = reportData.getDetails().iterator(); it.hasNext();) {
					LoanSummaryItem item = (LoanSummaryItem) it.next();
					unselectedLoans.add(item.getParticipantID()+":"+item.getLoanNumber());
					details.add(item);
				}
				reportData.setDetails(details);
			}
		return unselectedLoans;
	}
	
	/*
	 * This method is to set the checkbox selected on the UI for saved values: This method will have to be updated to use value from DB  ( CARES act work)
	 */
	private void updateReportData(HttpServletRequest request) {
		List<LoanSummaryItem> details = new ArrayList<LoanSummaryItem>();
		try {
			List<String> updatedLoansList = ParticipantServiceDelegate.getInstance()
					.getExistingParticipantIDS(getUserProfile(request).getCurrentContract().getContractNumber());
			LoanSummaryReportData reportData = (LoanSummaryReportData) request
					.getAttribute(CommonConstants.REPORT_BEAN);
			if ((null != updatedLoansList && updatedLoansList.size() > 0)
					&& (reportData.getDetails() != null && reportData.getDetails().size() > 0)) {
				for (Iterator it = reportData.getDetails().iterator(); it.hasNext();) {
					LoanSummaryItem item = (LoanSummaryItem) it.next();
					if (updatedLoansList.contains(item.getParticipantID()+":"+item.getLoanNumber())) {
						item.setMaturityDateExtended("yes");
					}
					details.add(item);
				}
				reportData.setDetails(details);
				request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
			}
		} catch (SystemException e) {
			logger.error(e);
		}
	}
	
	private String getCutoffDate() throws SystemException, ParseException {
		String modifiedCuttofDate = "";
		String loanRemortizationCutOffDate = EnvironmentServiceDelegate.getInstance()
				.getBusinessParam("LOAN_REAMORTIZATION_REQUEST_CUTOFF_DATE");
		if (loanRemortizationCutOffDate != null) {

			Date cutoffDate = new SimpleDateFormat("yyyy-MM-dd").parse(loanRemortizationCutOffDate);
			modifiedCuttofDate = new SimpleDateFormat("MM/dd/yyyy").format(cutoffDate);
		}
		return modifiedCuttofDate;
	}
	@Autowired
	private PSValidatorFWLoanSummary psValidatorFWLoanSummary;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWLoanSummary);
	}
}
