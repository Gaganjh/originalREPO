/*
 * Created on August 16, 2004
 *
 */
package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

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
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionHistoryReportData;
import com.manulife.pension.ps.service.report.tools.valueobject.UploadHistoryItem;
import com.manulife.pension.ps.service.report.tools.valueobject.UploadHistoryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.PsProperties;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.environment.valueobject.ContractDatesVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;

/**
 * Action class for populating the submission history page.
 *
 * @author Adrian Robitu
 */
@Controller
@RequestMapping(value="/tools")
@SessionAttributes({"submissionHistoryForm"})

public class SubmissionHistoryController extends ReportController {

	@ModelAttribute("submissionHistoryForm")
	public SubmissionHistoryForm populateForm() {
		return new SubmissionHistoryForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("default","/tools/submissionHistory.jsp");
		forwards.put("sort","/tools/submissionHistory.jsp");
		forwards.put("filter","/tools/submissionHistory.jsp");
		forwards.put("page","/tools/submissionHistory.jsp");
		forwards.put("print","/tools/submissionHistory.jsp");
		forwards.put("input","/tools/submissionHistory.jsp");
		forwards.put("tools","redirect:/do/tools/toolsMenu/");
	}

	private static final String DEFAULT_SORT_FIELD = SubmissionHistoryReportData.SORT_SUBMISSION_DATE;
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;

	private static final int SORT_TRACKING_NO_COL = 0;
	private static final int SORT_FILE_TYPE_COL = 1;
	private static final int SORT_RECEIVED_DATE_COL = 2;
	private static final int SORT_PMT_EFFECTIVE_DATE_COL = 3;
	private static final int SORT_PMT_TOTAL_AMOUNT_COL = 4;

	// CSV Export Related
	private static final String DOWNLOAD_COLUMN_HEADING_SUBMISSIONS = "Submitter ID, Submission Number, Submission Date/Time, Type, Contract Number, Status, Payroll Date, Contribution Total($), Payment Total ($)";

	private static final String MSG_NO_HISTORY_LINE1 = "There is no submission history for this contract within the last two years.";
	private static final String MSG_NO_HISTORY_LINE2 = "If you have any questions please contact your John Hancock representative at ";

	private static final String CONTACT_PHONE_NY = "1-800-574-5557";
	private static final String CONTACT_PHONE_US = "1-800-333-0963";

	private static final String RECEIVED_DATE_MASK = "MMM dd, yyyy hh:mm a z";
	private static final String PAYMENT_EFFECTIVE_DATE_MASK = "MMM dd, yyyy";
	private static final DateFormat SELECTION_DATE_FORMATTER = new SimpleDateFormat("MMMMMMMMM yyyy");
	private static final int FOURTEEN_DAYS = 14;
	private static final int THIRTY_DAYS = 30;
	private static final int TWENTY_FOUR_MONTHS = 24;
	public static final String TIME_SEPARATOR = ":";
	private static final String LAST_2_WEEKS_LABEL = "Last 2 weeks";
	private static final String LAST_30_DAYS_LABEL = "Last 30 days";
	private static final String SELECT_DATE_LABEL = "Select Date...";
	private static final String NO_VALUE_INDICATOR = "-1";

	private static final BigDecimal ZERO = new BigDecimal(0d);

	private static final String SIMPLE_FILTER = "filterSimple";
	private static final String FALSE_STRING = "false";
	private static final String MARKET_CLOSE_ATTRIBUTE = "marketClose";

	private static final int MILLIS_MULTIPLIER = 60 * 1000;

	private static final String PAYMENT_AMOUNT_FORMAT = "########0.00";

	private static final DateFormat TIME_FORMATTER = new SimpleDateFormat("h:mm a");
	
	/**
	 * constructor
	 */
	public SubmissionHistoryController() {
		super(SubmissionHistoryController.class);
		SubmissionHistoryItemActionHelper.getInstance();
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportId()
	 */
	protected String getReportId() {
		return SubmissionHistoryReportData.REPORT_ID;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportName()
	 */
	protected String getReportName() {
		return SubmissionHistoryReportData.REPORT_NAME;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	/**
	 * This method is called to populate a report criteria from the report action
	 * form and the request. It is called right before getReportData is called.
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {

		// get the user profile object
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter(SubmissionHistoryReportData.FILTER_CONTRACT_NO,
				new Integer(currentContract.getContractNumber()));

		SubmissionHistoryForm submissionForm = (SubmissionHistoryForm) form;
		populateSortCriteria(criteria, form);
		String filterSimple = request.getParameter(SIMPLE_FILTER);

		// form values get reset on doDefault
		if (filterSimple != null) {
			if (!filterSimple.equals(FALSE_STRING)) {
				submissionForm.setFilterSimple(true);
			} else {
				submissionForm.setFilterSimple(false);
				// always has active filters when in advanced search mode
				submissionForm.setFilterActive(true);
			}
		}

		Date startDate = null;
		Date endDate = null;

		// check if this this is the simple or advanced filter
		if (submissionForm.isFilterSimple()) { // basic filter
			// CL 76764 fix - Show for drop down empty on Submission history page - start
			String time = submissionForm.getFilterShowDate();
			if (SubmissionHistoryForm.isFieldSet(time)) {
				// CL 76764 fix - Show for drop down empty on Submission history page - end
				StringTokenizer tokenizer = new StringTokenizer(time, TIME_SEPARATOR);
				startDate = new Date(Long.valueOf(tokenizer.nextToken()).longValue());
				endDate = new Date(Long.valueOf(tokenizer.nextToken()).longValue());
				submissionForm.setFilterActive(true);
			}
		} else { // advanced filter
			if (SubmissionHistoryForm.isFieldSet(submissionForm.getFilterStartDate())) {
				startDate = new Date(Long.valueOf(submissionForm.getFilterStartDate()).longValue());
			}
			if (SubmissionHistoryForm.isFieldSet(submissionForm.getFilterEndDate())) {
				endDate = new Date(Long.valueOf(submissionForm.getFilterEndDate()).longValue());
			}
		}

		// see if we are filtering by the submission or payroll dates
		if (submissionForm.isFilterBySubmission()) {
			criteria.addFilter(SubmissionHistoryReportData.FILTER_START_SUBMISSION_DATE, startDate);
			criteria.addFilter(SubmissionHistoryReportData.FILTER_END_SUBMISSION_DATE, endDate);
		} else {
			criteria.addFilter(SubmissionHistoryReportData.FILTER_START_PAYROLL_DATE, startDate);
			criteria.addFilter(SubmissionHistoryReportData.FILTER_END_PAYROLL_DATE, endDate);
		}

		// see if the user wants to or can only see only their submissions

		boolean justMine = false;
		if (submissionForm.isJustMine()) {
			justMine = true;
		} else {
			if (userProfile.getRole() instanceof ThirdPartyAdministrator) {
				/*
				 * We call setShowJustMineFilter to check if Just Mine needs to be show.
				 */
				setShowJustMineFilter(request);

				if (!userProfile.getContractProfile().getShowSubmissionHistoryJustMineFilter().booleanValue()) {
					/*
					 * If Just mine is not shown, we know this TPA user can only view his/her own
					 * submission cases.
					 */
					justMine = true;
				} else {
					// TPA users may only be allowed to view cases submitted by
					// TPA users of the same TPA firm. This will be determined
					// by the backend.
					criteria.addFilter(SubmissionHistoryReportData.FILTER_TPA_USER_ID,
							new Long(userProfile.getPrincipal().getProfileId()));
				}
			} else if (!(userProfile.getRole() instanceof InternalUser)) {
				/*
				 * If he is not an internal user, we need to check if he has view all submission
				 * permission.
				 */
				if (!userProfile.isAllowedToViewAllSubmissions()) {
					justMine = true;
				}
			}
		}

		if (justMine) {
			criteria.addFilter(SubmissionHistoryReportData.FILTER_SUBMITTER_ID,
					new Long(userProfile.getPrincipal().getProfileId()));
			submissionForm.setFilterActive(true);
		}

		if (SubmissionHistoryForm.isFieldSet(submissionForm.getFilterType())) {
			criteria.addFilter(SubmissionHistoryReportData.FILTER_TYPE, submissionForm.getFilterType());
		}
		if (SubmissionHistoryForm.isFieldSet(submissionForm.getFilterStatus())) {
			criteria.addFilter(SubmissionHistoryReportData.FILTER_STATUS, submissionForm.getFilterStatus());
		}
		// criteria.setPageSize(getPageSize(request));

		// set the NYSE close message
		Date marketClose = null;
		try {
			marketClose = AccountServiceDelegate.getInstance().getNextNYSEClosureDate(null);
		} catch (Exception e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "populateReportCriteria",
					"AccountException occurred while getting the NYSE close datetime.");
		}
		Date currentDate = Calendar.getInstance().getTime();
		request.setAttribute(MARKET_CLOSE_ATTRIBUTE, "");
		if (marketClose != null && marketClose.after(currentDate)) {
			long msDiff = marketClose.getTime() - currentDate.getTime();

			// less than 30 min to market close (30 * 60 * 1000)
			if (msDiff < PsProperties.getNYStockClosureTimeLimit() * MILLIS_MULTIPLIER) {
				String marketCloseString = null;
				synchronized (TIME_FORMATTER) {
					marketCloseString = TIME_FORMATTER.format(marketClose);
				}
				request.setAttribute(MARKET_CLOSE_ATTRIBUTE, "NYSE will close at " + marketCloseString
						+ " all submissions must be submitted prior to that time.");
			}
		}
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#populateDownloadData
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		Content message = null;
		UploadHistoryReportData reportData = (UploadHistoryReportData) report;
		StringBuffer buffer = new StringBuffer();
		try {
			// Report Title
			message = ContentCacheManager.getInstance().getContentById(ContentConstants.SUBMISSION_HISTORY_LAYOUT,
					ContentTypeManager.instance().LAYOUT_PAGE);

			buffer.append(ContentUtility.getContentAttribute(message, "body1Header"));

			buffer.append(LINE_BREAK);

			// print Contract Number / Name
			UserProfile up = getUserProfile(request);

			buffer.append("Contract").append(COMMA).append(up.getCurrentContract().getContractNumber()).append(COMMA)
					.append(up.getCurrentContract().getCompanyName());

			buffer.append(LINE_BREAK);
			buffer.append(DOWNLOAD_COLUMN_HEADING_SUBMISSIONS);
			buffer.append(LINE_BREAK);

			// Upload history Detail Data
			if (reportData.getDetails() != null && reportData.getDetails().size() > 0) {
				for (Iterator it = reportData.getDetails().iterator(); it.hasNext();) {
					UploadHistoryItem item = (UploadHistoryItem) it.next();

					// get and format the received date
					String pmtReceived = "";
					if (item.getReceivedDate() != null) {
						pmtReceived = DateRender.format(item.getReceivedDate(), RECEIVED_DATE_MASK);
					}

					// add it to the sb
					buffer.append(QUOTE).append(pmtReceived).append(QUOTE).append(COMMA);
					buffer.append(getCsvString(item.getSubmissionId())).append(COMMA);
					buffer.append(getCsvString(item.getFileType())).append(COMMA);

					// get the payment effective date
					String pmtRequestedPmtEffective = "";
					if (item.getPaymentEffectiveDate() != null) {
						pmtRequestedPmtEffective = DateRender.format(item.getPaymentEffectiveDate(),
								PAYMENT_EFFECTIVE_DATE_MASK);
					}
					buffer.append(pmtRequestedPmtEffective).append(COMMA);

					if (item.getPaymentTotalAmount() != null && !item.getPaymentTotalAmount().equals(ZERO)) {
						buffer.append(NumberRender.formatByPattern(item.getPaymentTotalAmount(), ZERO_AMOUNT_STRING,
								PAYMENT_AMOUNT_FORMAT));
					}
					buffer.append(LINE_BREAK);
				}
			} else {

				// no records found: show the message
				String contactPhone = CONTACT_PHONE_US;
				if (Environment.getInstance().isNY()) {
					contactPhone = CONTACT_PHONE_NY;
				}
				buffer.append(MSG_NO_HISTORY_LINE1);
				buffer.append(MSG_NO_HISTORY_LINE2).append(contactPhone);
			}
		} catch (Throwable t) {
			throw new SystemException(t, getClass().getName(), "populateDownloadData", "Something wrong with CMA");
		}

		return buffer.toString().getBytes();
	}

	/**
	 * This is the method to be extended for validation.
	 *
	 * @param mapping
	 *            TODO
	 * @param form
	 *            Form objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 *
	 * @return Error Collection
	 */
	@SuppressWarnings("rawtypes")
	protected Collection doValidate(ActionForm form, HttpServletRequest request) {

		SubmissionHistoryForm submissionForm = (SubmissionHistoryForm) form;
		// This code has been changed and added to
		// Validate form and request against penetration attack, prior to other
		// validations.

		Collection errors = new ArrayList();

		Date startDate = null;
		Date endDate = null;

		// check if this this is the simple filter
		if (!FILTER_TASK.equals(request.getParameter(TASK_KEY)) || submissionForm.isFilterSimple()) {
			return errors;
		}

		// advanced filter
		if (SubmissionHistoryForm.isFieldSet(submissionForm.getFilterStartDate())) {
			startDate = new Date(Long.valueOf(submissionForm.getFilterStartDate()).longValue());
		}
		if (SubmissionHistoryForm.isFieldSet(submissionForm.getFilterEndDate())) {
			endDate = new Date(Long.valueOf(submissionForm.getFilterEndDate()).longValue());
		}

		// validate the start and end dates
		if (startDate != null && endDate != null && startDate.after(endDate)) {
			errors.add(new GenericException(ErrorCodes.SUBMISSION_DATES_OVERLAP));
		}

		// validate combination of parameters
		if (!submissionForm.isFilterBySubmission() && submissionForm.getFilterType() != null
				&& (submissionForm.getFilterType().equals(GFTUploadDetail.SUBMISSION_TYPE_PAYMENT)
						|| submissionForm.getFilterType().equals(GFTUploadDetail.SUBMISSION_TYPE_CENSUS)
						|| SubmissionHistoryItemActionHelper.SUBMISSION_TYPE_LTPT.equalsIgnoreCase(submissionForm.getFilterType())
						|| submissionForm.getFilterType().equals(GFTUploadDetail.SUBMISSION_TYPE_VESTING)
						|| submissionForm.getFilterType().equals(GFTUploadDetail.SUBMISSION_TYPE_MISCELLANEOUS)
						|| submissionForm.getFilterType().equals(GFTUploadDetail.SUBMISSION_TYPE_CONVERSION))) {
			errors.add(new GenericException(ErrorCodes.SUBMISSION_PAYROLL_DATES_NOT_VALID_FOR_TYPE));
		}

		if (!errors.isEmpty()) {
			SubmissionHistoryReportData emptyReport = new SubmissionHistoryReportData(null, 0);
			emptyReport.setDetails(new ArrayList());
			request.setAttribute(Constants.REPORT_BEAN, emptyReport);
			request.getSession().setAttribute(CommonConstants.ERROR_RDRCT,"input");
			request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, errors);
 			request.setAttribute(PsBaseUtil.ERROR_KEY, errors);
		}

		return errors;
	}

	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
		if (form.getSortField() != null) {
			criteria.insertSort(form.getSortField(), form.getSortDirection());
			// if sorting by type, name or status, secondary sort will be desc submission
			// number
			if (form.getSortField().equals(SubmissionHistoryReportData.SORT_TYPE)
					|| form.getSortField().equals(SubmissionHistoryReportData.SORT_SUBMITTER_NAME)
					|| form.getSortField().equals(SubmissionHistoryReportData.SORT_USER_STATUS)) {
				criteria.insertSort(SubmissionHistoryReportData.SORT_SUBMISSION_ID, ReportSort.DESC_DIRECTION);
			} else {
				criteria.insertSort(getDefaultSort(), getDefaultSortDirection());
			}
		}
	}

	protected String validate(ActionForm actionForm, HttpServletRequest request) {

		/*
		 * Prevent user from copy and paste URL.
		 */
		UserProfile userProfile = getUserProfile(request);

		if (!userProfile.isSubmissionAccess()) {

			// place the not authorized error msg in the request

			Collection error = new ArrayList(1);
			error.add(new ValidationError("NOT AUTHORIZED", ErrorCodes.SUBMISSION_INVALID_PERMISSION));
			setErrorsInSession(request, error);
			return forwards.get("tools");
		}
		Collection errors = doValidate(actionForm,request);

		if (!errors.isEmpty()) {
			
			if (request.getSession().getAttribute(CommonConstants.ERROR_RDRCT) != null) {
				String forward = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forward;
			}
			//return mapping.getInputForward();
			return "input";
			
		}
		return null;
	}

	public String doCommon(BaseReportForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		FunctionalLogger.INSTANCE.log("Submission History", getUserProfile(request), getClass(),
				getMethodName(actionForm, request));

		return super.doCommon(actionForm, request, response);

	}

	@RequestMapping(value = "/submissionHistory/", method = {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("submissionHistoryForm") SubmissionHistoryForm actionForm,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			SubmissionHistoryReportData report = new SubmissionHistoryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				postExecute(actionForm, request, response);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}
		String validationResult = validate(actionForm, request)	;
		if(StringUtils.isNotBlank(validationResult)){
			return StringUtils.contains(validationResult,'/')?validationResult:forwards.get(validationResult);
		}
		// reset the form in the session if any
		// this will ensure that the user always sees
		// the default view of the report
		actionForm = (SubmissionHistoryForm) resetForm(actionForm, request);
		request.getSession(false).removeAttribute("editContributionDetailsForm");
		String forward = doCommon(actionForm, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}
		postExecute(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.manulife.pension.ps.web.controller.PsAction#postExecute(org.apache.struts
	 * .action.ActionMapping, org.apache.struts.action.Form,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	protected void postExecute(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
		super.postExecute(actionForm, request, response);

		setShowJustMineFilter(request);

		UserProfile userProfile = getUserProfile(request);
		if (userProfile.getCurrentContract().isDefinedBenefitContract()) {
			// map name so tag will pickup column, and show as sorted by on screen for DB
			BaseReportForm theForm = (BaseReportForm) actionForm;
			if ("payrollDate".equals(theForm.getSortField())) {
				theForm.setSortField("contribDate");
			}
		}

		request.setAttribute(Constants.SHOW_JUST_MINE_FILTER,
				userProfile.getContractProfile().getShowSubmissionHistoryJustMineFilter().toString());
	}

	/**
	 * This method populates a default form when the report page is first brought
	 * up. This method is called before populateReportCriteria() to allow default
	 * sort and other criteria to be set properly.
	 *
	 * @see com.manulife.pension.ps.web.report.ReportController#populateReportForm(ActionMapping,
	 *      com.manulife.pension.ps.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm(reportForm, request);

		/*
		 * Obtain the contract dates object.
		 */
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		ContractDatesVO contractDates = currentContract.getContractDates();
		List fromDates = new ArrayList();
		List toDates = new ArrayList();

		/*
		 * Using the contract dates, generate the date range (from dates and to dates).
		 */
		ContractDateHelper.populateFromToDates(currentContract, fromDates, toDates);

		// convert the dates to strings
		ArrayList fromMonths = new ArrayList();
		ArrayList toMonths = new ArrayList();
		ArrayList datesCollection = new ArrayList();

		datesCollection.add(new LabelValueBean(SELECT_DATE_LABEL, NO_VALUE_INDICATOR));
		fromMonths.add(new LabelValueBean(SELECT_DATE_LABEL, NO_VALUE_INDICATOR));
		toMonths.add(new LabelValueBean(SELECT_DATE_LABEL, NO_VALUE_INDICATOR));

		// get the current date and time
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		// CL 76764 fix - Show for drop down empty on Submission history page - start
		Calendar to_Date = new GregorianCalendar();
		to_Date.setTime(now);
		to_Date.set(Calendar.HOUR, 23);
		to_Date.set(Calendar.MINUTE, 59);
		to_Date.set(Calendar.SECOND, 59);
		to_Date.set(Calendar.MILLISECOND, 999);
		// CL 76764 fix - Show for drop down empty on Submission history page - end

		// two weeks
		cal.add(Calendar.DATE, -FOURTEEN_DAYS);
		datesCollection.add(new LabelValueBean(LAST_2_WEEKS_LABEL, String.valueOf(cal.getTime().getTime())
				+ TIME_SEPARATOR + String.valueOf(to_Date.getTime().getTime())));

		// 30 days
		cal.add(Calendar.DATE, -(THIRTY_DAYS - FOURTEEN_DAYS));
		datesCollection.add(new LabelValueBean(LAST_30_DAYS_LABEL, String.valueOf(cal.getTime().getTime())
				+ TIME_SEPARATOR + String.valueOf(to_Date.getTime().getTime())));

		Iterator iter = fromDates.iterator();
		while (iter.hasNext()) {
			Date fromDate = (Date) iter.next();
			String date = null;
			synchronized (SELECTION_DATE_FORMATTER) {
				date = SELECTION_DATE_FORMATTER.format(fromDate);
			}
			fromMonths.add(new LabelValueBean(date, String.valueOf(fromDate.getTime())));
		}
		iter = toDates.iterator();
		while (iter.hasNext()) {
			Date toDate = (Date) iter.next();
			String date = null;
			synchronized (SELECTION_DATE_FORMATTER) {
				date = SELECTION_DATE_FORMATTER.format(toDate);
			}
			toMonths.add(new LabelValueBean(date, String.valueOf(toDate.getTime())));

			// determine the 1st of the month
			Calendar gcal = new GregorianCalendar();
			gcal.setTime(toDate);
			gcal.set(Calendar.DAY_OF_MONTH, 1); // set the date to the first of the month
			gcal.set(Calendar.HOUR, 0);
			gcal.set(Calendar.MINUTE, 0);
			gcal.set(Calendar.SECOND, 0);
			gcal.set(Calendar.MILLISECOND, 0);
			Date firstOfMonth = gcal.getTime();

			// modify the toDate to push back the time to the very last millisecond
			gcal.setTime(toDate);
			gcal.set(Calendar.HOUR, 23);
			gcal.set(Calendar.MINUTE, 59);
			gcal.set(Calendar.SECOND, 59);
			gcal.set(Calendar.MILLISECOND, 999);
			toDate = gcal.getTime();

			datesCollection.add(new LabelValueBean(date,
					String.valueOf(firstOfMonth.getTime()) + TIME_SEPARATOR + String.valueOf(toDate.getTime())));
		}

		SubmissionHistoryForm form = (SubmissionHistoryForm) reportForm;
		form.setFromMonthsCollection(fromMonths);
		form.setToMonthsCollection(toMonths);
		form.setDatesCollection(datesCollection);
	}

	/**
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	public static void setShowJustMineFilter(HttpServletRequest request) throws SystemException {
		UserProfile userProfile = getUserProfile(request);

		if (userProfile.getContractProfile().getShowSubmissionHistoryJustMineFilter() == null) {

			boolean showJustMineFilter = false;

			if (userProfile.isAllowedToViewAllSubmissions()) {
				/*
				 * If user has view all submissions permission, we should show Just Mine filter.
				 */
				showJustMineFilter = true;
			} else {
				/*
				 * Otherwise, check if login user is a TPA.
				 */
				if (userProfile.getRole() instanceof ThirdPartyAdministrator) {
					/*
					 * If user is a TPA user, check if user has View Submission Access.
					 */
					UserInfo loginUserInfo = SecurityServiceDelegate.getInstance()
							.getUserInfo(userProfile.getPrincipal());

					Contract currentContract = userProfile.getCurrentContract();
					TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance()
							.getFirmInfoByContractId(currentContract.getContractNumber());

					/*
					 * This checks the permission of the login user against the current firm. It is
					 * NOT the firm's permission.
					 */
					if (firmInfo != null && loginUserInfo.getTpaFirm(firmInfo.getId()).getContractPermission()
							.isViewAllSubmissions()) {
						/*
						 * If so, we should show the Just Mine filter.
						 */
						showJustMineFilter = true;
					}
				}
			}

			userProfile.getContractProfile()
					.setShowSubmissionHistoryJustMineFilter(showJustMineFilter ? Boolean.TRUE : Boolean.FALSE);
		}
	}

	@RequestMapping(value = "/submissionHistory/", params = {"task=filter"}, method = {RequestMethod.GET})
	public String doFilter(@Valid @ModelAttribute("submissionHistoryForm") SubmissionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				postExecute(form, request, response);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String validationResult = validate(form, request)	;
		if(StringUtils.isNotBlank(validationResult)){
			return StringUtils.contains(validationResult,'/')?validationResult:forwards.get(validationResult);
		}
		String forward = super.doFilter(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/submissionHistory/", params = {"task=page" }, method = {RequestMethod.POST})
	public String doPage(@Valid @ModelAttribute("submissionHistoryForm") SubmissionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				postExecute(form, request, response);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String validationResult = validate(form, request);
		if(StringUtils.isNotBlank(validationResult)){
			return StringUtils.contains(validationResult,'/')?validationResult:forwards.get(validationResult);
		}
		String forward = super.doPage(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/submissionHistory/", params = {"task=sort"}, method = {RequestMethod.POST})
	public String doSort(@Valid @ModelAttribute("submissionHistoryForm") SubmissionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				postExecute(form, request, response);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String validationResult = validate(form, request);
		if(StringUtils.isNotBlank(validationResult)){
			return StringUtils.contains(validationResult,'/')?validationResult:forwards.get(validationResult);
		}
		String forward = super.doSort(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/submissionHistory", params = {"task=download"}, method = {RequestMethod.GET})
	public String doDownload(@Valid @ModelAttribute("submissionHistoryForm") SubmissionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String validationResult = validate(form, request);
		if(StringUtils.isNotBlank(validationResult)){
			return StringUtils.contains(validationResult,'/')?validationResult:forwards.get(validationResult);
		}
		String forward = super.doDownload(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

		
	
	@Autowired
	private PSValidatorFWInput  psValidatorFWInput;

    @InitBinder
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	} 
	
}