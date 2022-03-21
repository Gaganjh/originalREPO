package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
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
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressesReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.census.util.CensusInfoFilterCriteriaHelper;
import com.manulife.pension.ps.web.census.util.CensusLookups;
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.census.util.DeferralUtils;
import com.manulife.pension.ps.web.census.util.FilterCriteriaVo;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;

/**
 * This action handles the creation of the ParticipantAddress download.
 *
 * @author Ramanathan Subramanian
 * @see ReportController for details
 */
@Controller
@RequestMapping(value = "/participant")
@SessionAttributes({"participantAddressesReportForm"})

public class ParticipantAddressSummaryReportController extends ReportController {
	@ModelAttribute("participantAddressesReportForm")
	public ParticipantAddressesReportForm populateForm() {
		return new ParticipantAddressesReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/participant/participantAddresses.jsp");
		forwards.put("default", "/participant/participantAddresses.jsp");
		forwards.put("download", "/participant/participantAddresses.jsp");
	}

	protected static final int SSN_LENGTH = 9;
	protected static final String DOWNLOAD_COLUMN_HEADING = "FirstName,LastName,Address1,Address2,City,State,ZipCode,Country,Division";
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MMddyyyy");

	/**
	 * Constructor for CensusSummaryReportAction.
	 */
	public ParticipantAddressSummaryReportController() {
		super(ParticipantAddressSummaryReportController.class);
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getPageSize(javax.servlet.http.HttpServletRequest)
	 */
	protected int getPageSize(HttpServletRequest request) {
		UserProfile profile = getUserProfile(request);
		return profile.getPreferences().getInt(UserPreferenceKeys.REPORT_PAGE_SIZE, super.getPageSize(request));
	}

	/**
	 * @see ReportController#populateDownloadData(PrintWriter, BaseReportForm,
	 *      ReportData)
	 * @param reportForm
	 *            BaseReportForm
	 * @param report
	 *            ReportData
	 * @param request
	 *            HttpServletRequest
	 * @return byte[]
	 */
	protected byte[] getDownloadData(final BaseReportForm reportForm, final ReportData report,
			final HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		final StopWatch stopWatch = new StopWatch();
		try {
			if (logger.isInfoEnabled()) {
				logger.info("getDownloadData - starting timer.");
			}
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}

		StringBuffer buffer = new StringBuffer();

		// heading and records
		buffer.append(DOWNLOAD_COLUMN_HEADING);

		Iterator iterator = report.getDetails().iterator();
		while (iterator.hasNext()) {
			buffer.append(LINE_BREAK);
			CensusSummaryDetails theItem = (CensusSummaryDetails) iterator.next();
			buffer.append(escapeField(theItem.getFirstName())).append(COMMA);
			buffer.append(escapeField(theItem.getLastName())).append(COMMA);

			if (theItem.getAddressLine1() != null)
				buffer.append(escapeField(theItem.getAddressLine1().trim()));
			buffer.append(COMMA);

			if (theItem.getAddressLine2() != null)
				buffer.append(escapeField(theItem.getAddressLine2().trim()));
			buffer.append(COMMA);

			if (theItem.getCity() != null)
				buffer.append(escapeField(theItem.getCity().trim()));
			buffer.append(COMMA);

			if (theItem.getStateCode() != null)
				buffer.append(escapeField(theItem.getStateCode().trim()));
			buffer.append(COMMA);

			if (theItem.getZipCode() != null) {
				String zipCode = StringUtils.trim(theItem.getZipCode());
				if (zipCode.length() > 0) {
					buffer.append(zipCode.toUpperCase());
				}
			}
			buffer.append(COMMA);

			if (theItem.getCountry() != null)
				buffer.append(escapeField(theItem.getCountry().trim()));
			buffer.append(COMMA);

			if (theItem.getDivision() != null)
				buffer.append(escapeField(theItem.getDivision().trim()));
			buffer.append(COMMA);
		}
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer("getDownloadData - stoping timer - time duration [")
						.append(stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/**
	 * @see ReportController#getDefaultSortDirection()
	 * @return String
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * @see ReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return ParticipantAddressesReportData.DEFAULT_SORT;
	}

	/**
	 * @see ReportController#getReportId()
	 * @return String
	 */
	protected String getReportId() {
		return ParticipantAddressesReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return ParticipantAddressesReportData.REPORT_NAME;
	}

	/**
	 * Given a report ID, returns the downloaded CSV file name.
	 *
	 * @param request
	 *
	 * @return The file name used for the downloaded CSV.
	 */
	protected String getFileName(HttpServletRequest request) {
		// defaults to .csv extension
		String dateString = null;
		synchronized (DATE_FORMATTER) {
			dateString = DATE_FORMATTER.format(new Date());
		}
		return "Participant_Address_Summary_Report_for_"
				+ getUserProfile(request).getCurrentContract().getContractNumber() + "_for_" + dateString
				+ CSV_EXTENSION;
	}

	/**
	 *
	 * @see ReportController#populateReportCriteria(ReportCriteria,
	 *      BaseReportForm, HttpServletRequest)
	 *
	 * @param criteria
	 *            ReportCriteria
	 * @param form
	 *            BaseReportForm
	 * @param request
	 *            HttpServletRequest
	 */
	protected void populateReportCriteria(ReportCriteria criteria, final BaseReportForm form,
			final HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		String task = getTask(request);

		// default sort criteria
		// this is already set in the super

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter("contractNumber", Integer.valueOf(currentContract.getContractNumber()));

		// if external user, don't display Cancelled employees
		criteria.setExternalUser(userProfile.getRole().isExternalUser());
		ParticipantAddressesReportForm psform = (ParticipantAddressesReportForm) form;

		FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);

		if (filterCriteriaVo == null) {
			filterCriteriaVo = new FilterCriteriaVo();
		}

		// If the task is default then reset the page no and sort details that
		// are cached in eligibility tab and deferral tab.
		if (task.equals(DEFAULT_TASK)) {
			filterCriteriaVo.clearDeferralSortDetails();
			filterCriteriaVo.clearEligibilitySortDetails();
		}

		// Populate the filter criterias
		CensusInfoFilterCriteriaHelper.populateAddressesTabFilterCriteria(task, filterCriteriaVo, psform, criteria);

		// set filterCriteriaVo back to session
		SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);

		if (logger.isDebugEnabled()) {
			logger.debug("criteria= " + criteria);
			logger.debug("exit <- populateReportCriteria");
		}
	}

	/**
	 * @param mapping
	 *            ActionMapping
	 * @param reportForm
	 *            BaseReportForm
	 * @param request
	 *            HttpServletRequest
	 */
	protected void populateReportForm(final BaseReportForm reportForm, final HttpServletRequest request) {
		super.populateReportForm(reportForm, request);

		String task = getTask(request);
		if (task.equals(FILTER_TASK)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
	}

	/**
	 * Populate sort criteria in the criteria object using the given FORM.
	 * Default sort: - by last name, first name, middle initial
	 *
	 * @param criteria
	 *            The criteria to populate
	 * @param form
	 *            The Form to populate from.
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
	 * Validate the input form. The search field must not be empty.
	 *
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 *
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            BaseReportForm
	 * @param request
	 *            HttpServletRequest
	 * @return Collection
	 */

	@Autowired
	private ParticipantAddressSummaryReportValidator participantAddressSummaryReportValidator;

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
	@RequestMapping(value = "/participantAddressSummary", method = { RequestMethod.POST, RequestMethod.GET })
	public String execute(
			@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		UserProfile userProfile = SessionHelper.getUserProfile(request);

		// check for selected access
		if (userProfile.isSelectedAccess()) {
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}

		// check if contract is discontinued
		if (userProfile.getCurrentContract().isDiscontinued()) {
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}

		if ("POST".equalsIgnoreCase(request.getMethod())) {

			// do a refresh so that there's no problem using tha back button
			ControllerForward forward = new ControllerForward("refresh",
					"/do" + new UrlPathHelper().getPathWithinApplication(request) + "?task=" + getTask(request), true);

			if (logger.isDebugEnabled()) {
				logger.debug("forward = " + forward);
			}
			return forward.getPath();
		}
		String forward = super.doDefault(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	/**
	 * @see ReportController#doCommon(ActionMapping, ActionForm, HttpServletRequest,
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

	
	
	public String doCommon(
            final BaseReportForm reportForm, final HttpServletRequest request,
            final HttpServletResponse response)
    throws SystemException{
	
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doCommon");
        }
        
        final StopWatch stopWatch = new StopWatch();
		try {
			if (logger.isInfoEnabled()) {
				logger.info("doCommon - starting timer.");
			}
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        String forward = super.doCommon( reportForm, request,
                response);
        ParticipantAddressesReportForm form = (ParticipantAddressesReportForm) reportForm;
        ParticipantAddressesReportData report = (ParticipantAddressesReportData)request.getAttribute(Constants.REPORT_BEAN);
		form.setReport(report);

        // set permission flag for editing
        UserProfile userProfile = getUserProfile(request);
        
        if (DOWNLOAD_TASK.equals(getTask(request))) {
            
	        FunctionalLogger.INSTANCE.log("Download Address Report", userProfile, getClass(), getMethodName( form, request));
	        
        }
        
        form.setAllowedToEdit(userProfile.isAllowedUpdateCensusData()
				&& !Contract.STATUS_CONTRACT_DISCONTINUED.equals(userProfile.getCurrentContract().getStatus()));

        // set permission flag for adding a new employee
        form.setAllowedToAdd( userProfile.isAllowedUpdateCensusData() &&
                              !Contract.STATUS_CONTRACT_DISCONTINUED.equals(userProfile.getCurrentContract().getStatus())
                            );

        int contractId = userProfile.getCurrentContract().getContractNumber();

        // set permission flag for eligibility tab
        boolean isEnabled = userProfile.isInternalUser() || CensusUtils.isAutoEnrollmentEnabled(contractId);
        form.setAllowedToAccessEligibTab(isEnabled);

               
        // set permission flag for deferral tab
        boolean allowedToAccessDeferrals = DeferralUtils.isAllowedToAccessDeferrals(userProfile);
        form.setAllowedToAccessDeferralTab(allowedToAccessDeferrals);

        // set permission flag for How To ... auto enrollment
        form.setAllowedToAutoEnrollment(CensusUtils.isAutoEnrollmentEnabled(contractId));

        // set permission flag for download census report
        form.setAllowedToDownload(
        	(userProfile.isInternalUser() && userProfile.isAllowedUpdateCensusData()) ||
            (userProfile.getRole().isExternalUser()));

        // set permission flag for vesting tab
        form.setAllowedToAccessVestingTab(
                CensusUtils.isVestingEnabled(contractId) &&
                !userProfile.getCurrentContract().isDefinedBenefitContract() /*
                TODO &&
                userProfile.getCurrentContract().isContractAllocated()*/);

        // populate list of employee statuses for the dropdown
        List employeeStatusList = null;
        // if external user, do not display Cancelled status in the dropdown
        if (userProfile.isInternalUser()) {
            employeeStatusList = CensusLookups.getInstance().getEmploymentStatuses();
        } else {
            employeeStatusList = CensusLookups.getInstance().getEmploymentStatusesWithoutC();
        }
        form.setStatusList(employeeStatusList);

        // populate list of segments for the dropdown
        form.setSegmentList(CensusLookups.getInstance().getSegments());

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doCommon - stoping timer - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doCommon");
        }
        return forward;
}
	
	
	

	/**
	 * This method is called when reset button is clicked
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws SystemException
	 */
	@RequestMapping(value = "/participantAddressSummary", params = { "action=filter", "task=filter" }, method = {
			RequestMethod.POST, RequestMethod.GET })
	public String doFilter(
			@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doFilter(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/participantAddressSummary", params = { "action=page", "task=page" }, method = {
			RequestMethod.POST, RequestMethod.GET })
	public String doPage(
			@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doPage(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/participantAddressSummary", params = { "action=sort", "task=sort" }, method = {
			RequestMethod.POST, RequestMethod.GET })
	public String doSort(
			@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doSort(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/participantAddressSummary", params = {"task=download" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String doDownload(
			@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doDownload(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/participantAddressSummary", params = { "action=downloadAll",
			"task=downloadAll" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String doDownloadAll(
			@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doDownload(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/participantAddressSummary", params = { "action=reset", "task=reset" }, method = {
			RequestMethod.POST, RequestMethod.GET })
	public String doReset(
			@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doReset");
		}

		FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);

		// Reset the session object for remebering filter criteria
		if (filterCriteriaVo != null) {
			filterCriteriaVo = new FilterCriteriaVo();
		}

		SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);

		// Reset the form bean
		super.resetForm(actionForm, request);

		String forward = doCommon(actionForm, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doReset");
		}
		return forward;
	}

	private String escapeField(String field) {
		if (field.indexOf(",") != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		}
		return field;
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}

}
