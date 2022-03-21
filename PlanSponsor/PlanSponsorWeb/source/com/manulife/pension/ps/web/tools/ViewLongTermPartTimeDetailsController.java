package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.participant.valueobject.LongTermPartTimeParticipant;
import com.manulife.pension.ps.service.report.submission.valueobject.LongTermPartTimeDetailsReportData;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.service.submission.valueobject.LongTermPartTimeDetailItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.tools.util.LongTermPartTimeDetailsHelper;
import com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.SSNRender;

/**
 *
 * @author bobbave
 *
 */
@Controller
@RequestMapping(value = "/tools")
@SessionAttributes({ "longTermPartTimeDetailsForm" })
public class ViewLongTermPartTimeDetailsController extends ReportController {

	@ModelAttribute("longTermPartTimeDetailsForm")
	public LongTermPartTimeDetailsForm populateForm() {
		return new LongTermPartTimeDetailsForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("tools", "redirect:/do/tools/toolsMenu/");
		forwards.put("default", "/tools/viewLongTermPartTimeDetails.jsp");
		forwards.put("input", "/tools/viewLongTermPartTimeDetails.jsp");
		forwards.put("sort", "/tools/viewLongTermPartTimeDetails.jsp");
		forwards.put("filter", "/tools/viewLongTermPartTimeDetails.jsp");
		forwards.put("page", "/tools/viewLongTermPartTimeDetails.jsp");
		forwards.put("print", "/tools/viewLongTermPartTimeDetails.jsp");
		forwards.put("history", "redirect:/do/tools/submissionHistory/");
		forwards.put("reset", "/tools/viewLongTermPartTimeDetails.jsp");
	}

	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MMddyyyy");

	private static String DEFAULT_SORT = LongTermPartTimeParticipant.SORT_RECORD_NUMBER;
	private static String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	public static final NumberFormat FORMATTER = new DecimalFormat("00");
	public static final Integer ZERO = new Integer(0);
	public static final BigDecimal BIG_ZERO = new BigDecimal(0d).setScale(2);
	public static final Integer NINETY_NINE = new Integer(99);
	protected static final String TOOLS = "tools";
	private static final String HISTORY = "history";

	/**
	 * Constructor.
	 */
	public ViewLongTermPartTimeDetailsController() {
		super(ViewLongTermPartTimeDetailsController.class);
	}

	/**
	 * Populate sort criteria in the criteria object using the given FORM. Default
	 * sort: - all records with errors - all records with warnings - clean records
	 *
	 * @param criteria The criteria to populate
	 * @param form     The Form to populate from.
	 * 
	 */
	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
		if (form.getSortField() != null) {
			criteria.insertSort(form.getSortField(), form.getSortDirection());
			if (!form.getSortField().equals(getDefaultSort())) {
				criteria.insertSort(getDefaultSort(), getDefaultSortDirection());
			}
		}
	}

	/**
	 * Populates the empty report form with default parameters
	 */
	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}

		String task = getTask(request);

		/*
		 * Reset page number properly.
		 */
		if (task.equals(DEFAULT_TASK) || task.equals(SORT_TASK) || task.equals(FILTER_TASK) || task.equals(PRINT_TASK) || task.equals(DOWNLOAD_TASK)) {
			reportForm.setPageNumber(1);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportForm");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return DEFAULT_SORT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	protected String getReportId() {
		return LongTermPartTimeDetailsReportData.REPORT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	protected String getReportName() {
		return LongTermPartTimeDetailsReportData.REPORT_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria(com.
	 * manulife.pension.service.report.valueobject.ReportCriteria,
	 * com.manulife.pension.ps.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		// get the user profile object
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		LongTermPartTimeDetailsForm longTermPartTimeDetailsForm = (LongTermPartTimeDetailsForm) form;

		criteria.addFilter(LongTermPartTimeDetailsReportData.FILTER_CONTRACT_NUMBER,
				new Integer(currentContract.getContractNumber()));

		// we expect the submission number to be present in the request.
		String subNo = request.getParameter("subNo");
		if (subNo == null || subNo.length() == 0)
			subNo = longTermPartTimeDetailsForm.getSubNo();
		if (subNo == null || subNo.length() == 0)
			subNo = (String) request.getSession().getAttribute("subNo");

		longTermPartTimeDetailsForm.setSubNo(subNo);
		request.getSession().setAttribute("subNo", subNo);

		criteria.addFilter(LongTermPartTimeDetailsReportData.FILTER_SUBMISSION_ID,
				new Integer(longTermPartTimeDetailsForm.getSubNo()));

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#doCommon(org.apache.struts.
	 * action.ActionMapping, com.manulife.pension.ps.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */

	public String doCommon(BaseReportForm reportForm, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {

		UserProfile userProfile = getUserProfile(request);
		LongTermPartTimeDetailsForm form = (LongTermPartTimeDetailsForm) reportForm;

		// confirm that user has submission access
		if (!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()) {
			return forwards.get(TOOLS);
		}

		// we expect the submission number to be present in the request.
		String subNo = request.getParameter("subNo");
		if (subNo == null || subNo.length() == 0)
			subNo = form.getSubNo();
		if (subNo == null || subNo.length() == 0)
			subNo = (String) request.getSession().getAttribute("subNo");
		// if we can't find it, redirect the user to the submission history page
		if (subNo == null) {
			return forwards.get(HISTORY);
		}

		FunctionalLogger.INSTANCE.log("View Submission - Long Term Part Time", userProfile, subNo, getClass(),
				getMethodName(reportForm, request));

		String forward = super.doCommon(reportForm, request, response);
		LongTermPartTimeDetailsReportData theReport = (LongTermPartTimeDetailsReportData) request
				.getAttribute(Constants.REPORT_BEAN);
		request.setAttribute("ServiceFeatureConstants.PROVIDED", ServiceFeatureConstants.PROVIDED);
		request.setAttribute("ServiceFeatureConstants.CALCULATED", ServiceFeatureConstants.CALCULATED);
		LongTermPartTimeDetailItem theItem = null;

		if (theReport == null) {
			Collection errors = new ArrayList();
			errors.add(new GenericException(ErrorCodes.SUBMISSION_HAS_NO_VALID_DATA));
			setErrorsInSession(request, errors);
			return forwards.get(HISTORY);
		} else {
			theItem = theReport.getLongTermPartTimeData();
		}

		// ensure this user is allowed to view this submission
		if (!userProfile.isAllowedToViewAllSubmissions()) {
			long submitterId = 0;
			try {
				submitterId = Long.parseLong(theItem.getSubmitterID());
			} catch (NumberFormatException e) {
				return forwards.get(HISTORY);
			}
			if (userProfile.getPrincipal().getProfileId() != submitterId) {
				return forwards.get(HISTORY);
			}
		}

		// ensure the submission is viewable
		SubmissionHistoryItemActionHelper helper = SubmissionHistoryItemActionHelper.getInstance();
		if (!(helper.isViewAllowed(theItem, userProfile))) {
			return forwards.get(HISTORY);
		}

		form.setTheReport(theReport);
		Contract contract = userProfile.getCurrentContract();

		// We need to check the lock before verifying if
		// edit is available below
		Lock lock = SubmissionServiceDelegate.getInstance().checkLock(theItem, true);
		theItem.setLock(lock);

		// check rules for edit button
		if (theItem != null) {
			form.setEditFunctionAvailable(helper.isEditAllowed(theItem, userProfile));
		} else {
			form.setEditFunctionAvailable(false);
		}

		// allow to download only if submission status is complete,
		// user has "Submit/Update LongTermPartTime" permission and total count > 0
		LongTermPartTimeDetailsHelper longTermPartTimeHelper = new LongTermPartTimeDetailsHelper();
		boolean allowedToDownload = LongTermPartTimeDetailsHelper.STATUS_COMPLETE.equals(theItem.getSystemStatus())
				&& userProfile.isAllowedSubmitUpdateVesting() && (theReport.getTotalCount() > 0);
		longTermPartTimeHelper.setAllowedToDownload(allowedToDownload);
		longTermPartTimeHelper.setPermissable(form.isEditFunctionAvailable());
		longTermPartTimeHelper.setLocked(!theItem.isLockAvailable(String.valueOf(userProfile.getPrincipal().getProfileId())));

		request.getSession(false).setAttribute(Constants.LONG_TERM_PART_TIME_DETAILS_HELPER, longTermPartTimeHelper);

		// do mask SSN in view screen
		form.setMaskSSN(true);

		return forward;
	}

	/**
	 * @see ReportController#getPageSize(HttpServletRequest)
	 */
	protected int getPageSize(HttpServletRequest request) {
		UserProfile profile = getUserProfile(request);
		return profile.getPreferences().getInt(UserPreferenceKeys.REPORT_PAGE_SIZE, super.getPageSize(request));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#getDownloadData(com.manulife.
	 * pension.ps.web.report.BaseReportForm,
	 * com.manulife.pension.service.report.valueobject.ReportData,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		StringBuffer buffer = new StringBuffer();
		LongTermPartTimeDetailsReportData reportData = (LongTermPartTimeDetailsReportData) report;

		// Fill in the header
		Iterator columnLabels = reportData.getColumnLabels().iterator();
		while (columnLabels.hasNext()) {
			buffer.append(columnLabels.next());
			if (columnLabels.hasNext()) {
				buffer.append(COMMA);
			}
		}

		Iterator items = report.getDetails().iterator();
		while (items.hasNext()) {

			LongTermPartTimeParticipant participant = (LongTermPartTimeParticipant) items.next();

			// if record was ignored because of blank or unknown SSN, skip
			if (!LongTermPartTimeDetailsHelper.STATUS_PARTIALLY_COMPLETE_IGNORES.equals(String.valueOf(participant.getRecordStatus()))) {

				buffer.append(LINE_BREAK);
				// only populate transaction code
				// if standard LTPT file was submitted (no TPA system name available)
				if (reportData.getLongTermPartTimeData().getTpaSystemName() == null) {
					appendBuffer(buffer, reportData.getTransactionNumber()).append(COMMA);
				}
				appendBuffer(buffer, reportData.getContractNumber()).append(COMMA);

				if (participant.getSsn() != null) {
					buffer.append(SSNRender.format(participant.getSsn(), "", false));
				}
				buffer.append(COMMA);
				appendBuffer(buffer, participant.getFirstName()).append(COMMA);
				appendBuffer(buffer, participant.getLastName()).append(COMMA);
				appendBuffer(buffer, participant.getMiddleInitial());
				buffer.append(COMMA);
				appendBuffer(buffer, participant.getLongTermPartTimeAssessmentYear());
			}
		} // end while

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	private StringBuffer appendBuffer(StringBuffer buffer, Object o) {
		if (o != null) {
			buffer.append(o);
		}
		return buffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#getFileName(javax.servlet.
	 * http.HttpServletRequest)
	 */
	protected String getFileName(HttpServletRequest request) {
		// defaults to .csv extension
		String dateString = null;
		synchronized (DATE_FORMATTER) {
			dateString = DATE_FORMATTER.format(new Date());
		}
		UserProfile userProfile = getUserProfile(request);
		int contractId = userProfile.getCurrentContract().getContractNumber();
		return "Long_Term_Part_Time_Submission_for_" + String.valueOf(contractId) + "_for_" + dateString + CSV_EXTENSION;
	}

	@RequestMapping(value = "/viewLongTermPartTimeDetails/", params = { "task=reset" }, method = { RequestMethod.POST,RequestMethod.GET })
	public String doReset(@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doReset");
		}

		// clean up
		((LongTermPartTimeDetailsForm) actionForm).clear(request);

		String forward = doCommon(actionForm, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doReset");
		}
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	protected BaseReportForm resetForm(BaseReportForm reportForm, HttpServletRequest request) throws SystemException {
		try {
			// we'll do our own custom reset
			// we need to save the submission number
			LongTermPartTimeDetailsForm theForm = (LongTermPartTimeDetailsForm) reportForm;
			theForm.clear(request);
		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(), "resetForm", "exception in resetting the form");
		}

		return reportForm;
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations .
	 * 
	 */
	@RequestMapping(value = "/viewLongTermPartTimeDetails/", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);// if input forward not available, provided default
			}
		}
		String forward = super.doDefault(form, request, response);

		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/viewLongTermPartTimeDetails/", params = { "task=filter" }, method = { RequestMethod.GET })
	public String doFilter(@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);// if input forward not available,provided default
			}
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/viewLongTermPartTimeDetails/", params = { "task=page" }, method = { RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);// if input forward not available,provided default
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/viewLongTermPartTimeDetails/", params = { "task=sort" }, method = { RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);// if input forward not available,provided default
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/viewLongTermPartTimeDetails/", params = { "task=download" }, method = {
			RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);// if input forward not available,provided default
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/viewLongTermPartTimeDetails/", params = { "task=downloadAll" }, method = {RequestMethod.GET })
	public String doDownloadAll(@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);// if input forward not available,provided default
			}
		}
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}
