package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.delegate.PlanNoticeDocumentServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryDetails;
import com.manulife.pension.ps.service.report.notice.valueobject.EmployeeEligibleVO;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressesReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.CensusInfoFilterCriteriaHelper;
import com.manulife.pension.ps.web.census.util.CensusLookups;
import com.manulife.pension.ps.web.census.util.CensusSummaryUtils;
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.census.util.DeferralUtils;
import com.manulife.pension.ps.web.census.util.FilterCriteriaVo;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * ParticipantAddressesReportAction class is used to retrieve Participant
 * Addresses for certain contract.
 *
 * @author Ilker Celikyilmaz
 */

@Controller
@RequestMapping(value ="/participant")
@SessionAttributes({"participantAddressesReportForm"})

public class ParticipantAddressesReportController extends ReportController {

	@ModelAttribute("participantAddressesReportForm")
	public ParticipantAddressesReportForm populateForm() {
		return new ParticipantAddressesReportForm();

	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/participant/participantAddresses.jsp");
		forwards.put("default", "/participant/participantAddresses.jsp");
		forwards.put("sort", "/participant/participantAddresses.jsp");
		forwards.put("filter", "/participant/participantAddresses.jsp");
		forwards.put("page", "/participant/participantAddresses.jsp");
		forwards.put("print", "/participant/participantAddresses.jsp");
		forwards.put("reset", "/participant/participantAddresses.jsp");
		forwards.put("error", "/WEB-INF/participant/participantAddresses.jsp");
	}

	protected static final int SSN_LENGTH = 9;
	protected static final String DOWNLOAD_COLUMN_HEADING = "cens.h10,Cont#,SSN#,FirstName,LastName,Initial,NamePrefix,EEID#,Address1,Address2,City,State,ZipCode,Country,"
			+ "StateRes,ERProvEmail,Division,BirthDate,HireDate,EmplStat,EmplStatDate,EligInd,EligDate,OptOutInd,YTDHrs,PlanYTDComp,YTDHrsWkCompDt,"
			+ "BaseSalary,BfTxDefPct,DesigRothPct,BfTxFltDoDef,DesigRothAmt";
	protected static final String MERRILL_DOWNLOAD_COLUMN_HEADING = "FirstName,LastName,Address1,Address2,City,State,ZipCode,Country";
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MMddyyyy");

	protected static final SimpleDateFormat downloadDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	// ME - CL111266
	// Addition of the transaction code column as the first column in the Census
	// Report
	private static final String TRANSACTION_CODE = "cens.d";

	private static final String GENERATED_CENSUS_NAME = "isCensusGenerated";
	private static final String CENSUS_GENERATED = "censusGenerated";
	private static final String NO_CENSUS_INFO = "noCensusInfo";
	private static final String FETCH_PARTICIPANT_ADDRESS_TASK = "fetchParticipantAddress";
	private static final String EMPLOY_GENERATED = "employGenerated";
	private static final String NO_EMPLOY_INFO = "noEmployInfo";
	private static final String GENERATED_EMPLOY_NAME ="isEmployGenerated";

	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		UserProfile userProfile = getUserProfile(request);
		criteria.addFilter("contractNumber", new Integer(userProfile.getCurrentContract().getContractNumber()));
		// ME - CL88275
		// if external user, don't display Cancelled employees
		criteria.setExternalUser(userProfile.getRole().isExternalUser());
		ParticipantAddressesReportForm psform = (ParticipantAddressesReportForm) form;

		// Get the filter object from session.
		FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);

		String task = getTask(request);

		if (filterCriteriaVo == null) {
			filterCriteriaVo = new FilterCriteriaVo();
		}

		// If the task is default then reset the page no and sort detains that
		// are cached in eligibility tab and deferral tab.
		if (task.equals(DEFAULT_TASK)) {
			filterCriteriaVo.clearDeferralSortDetails();
			filterCriteriaVo.clearEligibilitySortDetails();
		}
		// Adding NO LIMIT condition for task which generates report for merrill
		if (FETCH_PARTICIPANT_ADDRESS_TASK.equals(task)) {
			criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);
		}

		// Populate the filter criterias
		CensusInfoFilterCriteriaHelper.populateAddressesTabFilterCriteria(task, filterCriteriaVo, psform, criteria);

		// Put the vo back in session.
		SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}
	}

	/**
	 * @see ReportController#getReportId()
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
	 * @see com.manulife.pension.ps.web.report.ReportController#getPageSize(javax.servlet.http.HttpServletRequest)
	 */
	protected int getPageSize(HttpServletRequest request) {
		UserProfile profile = getUserProfile(request);
		return profile.getPreferences().getInt(UserPreferenceKeys.REPORT_PAGE_SIZE, super.getPageSize(request));
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
		return "Census_Summary_Report_for_" + getUserProfile(request).getCurrentContract().getContractNumber() + "_for_"
				+ dateString + CSV_EXTENSION;
	}

	protected String getDefaultSort() {
		return ParticipantAddressesReportData.DEFAULT_SORT;
	}

	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
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
	 * Populate sort criteria in the criteria object using the given FORM. Default
	 * sort: - by last name, first name, middle initial
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

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		ParticipantAddressesReportForm form = (ParticipantAddressesReportForm) reportForm;
		UserProfile user = getUserProfile(request);
		UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(user.getPrincipal());
		Contract currentContract = user.getCurrentContract();

		// find the contract sort code
		String sortCode = currentContract.getParticipantSortOptionCode();

		String todayDate = DateRender.formatByPattern(Calendar.getInstance().getTime(), "",
				RenderConstants.MEDIUM_MDY_SLASHED);

		StringBuffer buffer = new StringBuffer();

		// SSE S024 determine wheather the ssn should be masked on the csv report
		boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
		try {
			maskSsnFlag = ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber());

		} catch (SystemException se) {
			logger.error(se);
			// log exception and output blank ssn
		}

		// heading and records
		buffer.append(DOWNLOAD_COLUMN_HEADING);

		Iterator iterator = report.getDetails().iterator();
		while (iterator.hasNext()) {
			buffer.append(LINE_BREAK);
			CensusSummaryDetails theItem = (CensusSummaryDetails) iterator.next();
			buffer.append(TRANSACTION_CODE).append(COMMA);
			buffer.append(currentContract.getContractNumber()).append(COMMA);
			buffer.append(SSNRender.format(theItem.getSsn(), null, maskSsnFlag)).append(COMMA);

			buffer.append(escapeField(theItem.getFirstName())).append(COMMA);
			buffer.append(escapeField(theItem.getLastName())).append(COMMA);

			if (theItem.getMiddleInitial() != null)
				buffer.append(escapeField(theItem.getMiddleInitial()));
			buffer.append(COMMA);

			if (theItem.getNamePrefix() != null)
				buffer.append(theItem.getNamePrefix());
			buffer.append(COMMA);

			if (theItem.getEmployeeNumber() != null && theItem.getEmployeeNumber().trim().length() > 0) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < (9 - theItem.getEmployeeNumber().trim().length()); i++) {
					sb.append("0");
				}
				buffer.append(sb.toString());
				buffer.append(theItem.getEmployeeNumber().trim());
			}
			buffer.append(COMMA);

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

			if (theItem.getStateOfResidence() != null)
				buffer.append(escapeField(theItem.getStateOfResidence().trim()));
			buffer.append(COMMA);

			if (theItem.getEmployeeProvidedEmail() != null)
				buffer.append(escapeField(theItem.getEmployeeProvidedEmail().trim()));
			buffer.append(COMMA);

			if (theItem.getDivision() != null)
				buffer.append(escapeField(theItem.getDivision().trim()));
			buffer.append(COMMA);

			buffer.append(DateRender.formatByPattern(theItem.getBirthDate(), "", RenderConstants.MEDIUM_MDY_SLASHED))
					.append(COMMA);
			buffer.append(DateRender.formatByPattern(theItem.getHireDate(), "", RenderConstants.MEDIUM_MDY_SLASHED))
					.append(COMMA);

			if (theItem.getStatus() != null)
				buffer.append(escapeField(theItem.getStatus().trim()));
			buffer.append(COMMA);

			buffer.append(
					DateRender.formatByPattern(theItem.getEmployeeStatusDate(), "", RenderConstants.MEDIUM_MDY_SLASHED))
					.append(COMMA);

			if (theItem.getEligibleToDeferInd() != null)
				buffer.append(escapeField(theItem.getEligibleToDeferInd().trim()));
			buffer.append(COMMA);

			buffer.append(
					DateRender.formatByPattern(theItem.getEligibilityDate(), "", RenderConstants.MEDIUM_MDY_SLASHED))
					.append(COMMA);

			if (theItem.getOptOut() != null)
				buffer.append(escapeField(theItem.getOptOut().trim()));
			buffer.append(COMMA);

			if (theItem.getPlanYTDHoursWorked() != null)
				buffer.append(escapeField(
						NumberRender.formatByType(theItem.getPlanYTDHoursWorked(), "", RenderConstants.INTEGER_TYPE)));
			buffer.append(COMMA);

			buffer.append(escapeField(CensusSummaryUtils.getMaskedValue(theItem.getPlanYTDCompensation(), theItem, user,
					userInfo, false))).append(COMMA);
			buffer.append(DateRender.formatByPattern(theItem.getPlanYTDHoursWorkedEffDate(), "",
					RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

			buffer.append(escapeField(
					CensusSummaryUtils.getMaskedValue(theItem.getAnnualBaseSalary(), theItem, user, userInfo, false)))
					.append(COMMA);
			buffer.append(
					escapeField(NumberRender.formatByPattern(theItem.getBeforeTaxDeferralPercentage(), "", "###.000")))
					.append(COMMA);
			buffer.append(
					escapeField(NumberRender.formatByPattern(theItem.getDesigRothDeferralPercentage(), "", "###.000")))
					.append(COMMA);
			buffer.append(escapeField(NumberRender.formatByType(theItem.getBeforeTaxDeferralAmount(), "",
					RenderConstants.CURRENCY_TYPE, false))).append(COMMA);
			buffer.append(escapeField(NumberRender.formatByType(theItem.getDesigRothDeferralAmount(), "",
					RenderConstants.CURRENCY_TYPE, false))).append(COMMA);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	private String escapeField(String field) {
		if (field.indexOf(",") != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else {
			return field;
		}
	}
	public String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
		HttpServletResponse response) throws SystemException

	{
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}

		String forward = super.doCommon( reportForm, request,
				response);
		ParticipantAddressesReportForm form = (ParticipantAddressesReportForm)reportForm;

		ParticipantAddressesReportData report = (ParticipantAddressesReportData)request.getAttribute(Constants.REPORT_BEAN);
		form.setReport(report);

		// set permission flag for editing
        UserProfile userProfile = getUserProfile(request);
        
        if (! DOWNLOAD_TASK.equals(getTask(request))) {
            
            FunctionalLogger.INSTANCE.log("Address Tab", userProfile, getClass(), getMethodName( reportForm, request));
            
        }
        
        form.setAllowedToEdit(userProfile.isAllowedUpdateCensusData()
				&& !Contract.STATUS_CONTRACT_DISCONTINUED.equals(userProfile
						.getCurrentContract().getStatus()));


        // set permission flag for adding a new employee
        form.setAllowedToAdd( userProfile.isAllowedUpdateCensusData() &&
                              !Contract.STATUS_CONTRACT_DISCONTINUED.equals(userProfile.getCurrentContract().getStatus())
                            );
        int contractId = userProfile.getCurrentContract().getContractNumber();


        // set permission flag for eligibility tab
        boolean isEnabled = userProfile.isInternalUser() || CensusUtils.isAutoEnrollmentEnabled(contractId);
        form.setAllowedToAccessEligibTab(isEnabled);

//      set permission flag for deferral tab
        boolean allowedToAccessDeferrals = DeferralUtils.isAllowedToAccessDeferrals(userProfile);
        form.setAllowedToAccessDeferralTab(allowedToAccessDeferrals);

        // set permission flag for How To ... auto enrollment
        form.setAllowedToAutoEnrollment(CensusUtils.isAutoEnrollmentEnabled(contractId));


        // set permission flag for download census report
        form.setAllowedToDownload(
        	(userProfile.isInternalUser() && userProfile.isAllowedUpdateCensusData()) ||
            (userProfile.getRole().isExternalUser() //&& userProfile.isAllowedDownloadReport()
            ));

        // set permission flag for vesting tab
        form.setAllowedToAccessVestingTab(
                CensusUtils.isVestingEnabled(contractId) &&
                !userProfile.getCurrentContract().isDefinedBenefitContract() /*
                TODO &&
                userProfile.getCurrentContract().isContractAllocated()*/);


        // This is added to accomadate the both View Online Address and Participant Address pages in one bofy & action
        form.setPath(new UrlPathHelper().getPathWithinServletMapping(request));

        // populate list of emploee statuses for the dropdown
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
	@RequestMapping(value ={"/participantAddresses","/participantAddressDownload/"}, params = {"task=reset"}, method = {RequestMethod.GET})
	public String doReset(@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			populateReportForm(form, request);
			ParticipantAddressesReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doReset");
		}

		FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);

		// Reset the session object
		if (filterCriteriaVo != null) {
			filterCriteriaVo = new FilterCriteriaVo();
		}

		SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);

		// Reset the form bean
		super.resetForm(form, request);

		String forward = doCommon(form, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doReset");
		}
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	/**
	 * Validate the input form. The search field must not be empty.
	 *
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */

	@Autowired
	private ParticipantAddressesReportValidator participantAddressesReportValidator;

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
		binder.addValidators(participantAddressesReportValidator);
	}

	/**
	 * @see PsController#execute(ActionMapping, ActionForm, HttpServletRequest,
	 *      HttpServletResponse)
	 */
	@RequestMapping(value = {"/participantAddresses","/participantAddressDownload/"}, method = { RequestMethod.POST})
	public String execute(@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm form,
			BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
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

		   ControllerForward forward = new ControllerForward("refresh",
					"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
			return "redirect:" + forward.getPath();
	}

	/**
	 * Invokes the download task. The first half of this task uses the common
	 * workflow with validateForm set to true. The second half of this task takes
	 * the populated report data object and create the CSV file.
	 *
	 * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
	 *      HttpServletResponse, boolean)
	 * @see #populateDownloadData(PrintWriter, BaseReportForm, ReportData,
	 *      HttpServletRequest)
	 * @return null so that Struts will not try to forward to another page.
	 */
	@RequestMapping(value = {"/participantAddresses","/participantAddressDownload/"}, params = {"task=fetchParticipantAddress"}, method = {RequestMethod.GET })
	public String doFetchParticipantAddress(
			@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);

			}
			populateReportForm(form, request);
			ParticipantAddressesReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			return forwards.get("input");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDownload");
		}
		byte[] downloadData = null;

		doCommon(form, request, response);
		ReportData report = (ReportData) request.getAttribute(CommonConstants.REPORT_BEAN);

		// check for errors
		// if any just stream an error message, otherwise deliver the report
		Collection errors = (Collection) request.getAttribute(CommonConstants.ERROR_RDRCT);
		if (errors != null && errors.size() > 0) {
			downloadData = getErrorText(errors).getBytes();
			if (logger.isDebugEnabled()) {
				logger.debug("found errors: " + getErrorText(errors));
			}
		} else {

			if (logger.isDebugEnabled()) {
				logger.debug("Did not find any errors -- streaming the report data");
			}

			downloadData = getMerrillDownloadAddressData(form, report, request);
		}

		HttpSession session = request.getSession(false);
		try {
			if (isParticipantAddressAvailable(report)) {
				response.getWriter().write(CENSUS_GENERATED + "&" + report.getTotalCount());
				session.setAttribute(GENERATED_CENSUS_NAME, downloadData);
			} else {
				response.getWriter().write(NO_CENSUS_INFO);
				session.setAttribute(GENERATED_CENSUS_NAME, downloadData);
			}
		} catch (IOException e) {
			logger.error(
					"Found issue while adding the address file to session to retrieve it in NMC build your package page");
			e.printStackTrace();
		}
		/**
		 * No need to forward to any other JSP or action. Returns null will make Struts
		 * to return controls back to server immediately.
		 */
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDownload");
		}
		return null;
	}

	/**
	 * 
	 *
	 * @param ReportData
	 *            The ReportData to populate
	 */
	@SuppressWarnings("unchecked")
	protected boolean isParticipantAddressAvailable(ReportData report) {
		Iterator iterator = report.getDetails().iterator();
		while (iterator.hasNext()) {
			CensusSummaryDetails theItem = (CensusSummaryDetails) iterator.next();
			if (theItem.getAddressLine1() != null && !theItem.getAddressLine1().isEmpty()) {
				return true;
			}
			if (theItem.getAddressLine2() != null && !theItem.getAddressLine2().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public byte[] getMerrillDownloadAddressData(BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		ParticipantAddressesReportForm form = (ParticipantAddressesReportForm) reportForm;

		StringBuffer buffer = new StringBuffer();

		// heading and records
		buffer.append(MERRILL_DOWNLOAD_COLUMN_HEADING);

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
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/**
	 * Invokes the download task. The first half of this task uses the common
	 * workflow with validateForm set to true. The second half of this task takes
	 * the populated report data object and create the CSV file.
	 *
	 * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
	 *      HttpServletResponse, boolean)
	 * @see #populateDownloadData(PrintWriter, BaseReportForm, ReportData,
	 *      HttpServletRequest)
	 * @return null so that Struts will not try to forward to another page.
	 */
	@RequestMapping(value = {"/participantAddresses","/participantAddressDownload/"}, params = {"task=downloadAddresses"}, method = {RequestMethod.GET })
	public String doDownloadAddresses(
			@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);

			}
			populateReportForm(form, request);
			ParticipantAddressesReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			return forwards.get("input");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDownload");
		}
		byte[] downloadData = null;

		doCommon(form, request, response);

		// check for errors
		// if any just stream an error message, otherwise deliver the report
		Collection errors = (Collection) request.getAttribute(CommonConstants.ERROR_RDRCT);
		if (errors != null && errors.size() > 0) {
			downloadData = getErrorText(errors).getBytes();
			if (logger.isDebugEnabled()) {
				logger.debug("found errors: " + getErrorText(errors));
			}
		} else {

			if (logger.isDebugEnabled()) {
				logger.debug("Did not find any errors -- streaming the report data");
			}

			ReportData report = (ReportData) request.getAttribute(CommonConstants.REPORT_BEAN);
			downloadData = getMerrillDownloadAddressData(form, report, request);
		}

		streamDownloadData(request, response, getContentType(), getFileName(form, request), downloadData);

		/**
		 * No need to forward to any other JSP or action. Returns null will make Struts
		 * to return controls back to server immediately.
		 */
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDownload");
		}
		return null;
	}

	@RequestMapping(value = {"/participantAddresses","/participantAddressDownload/"}, method = {RequestMethod.GET})
	public String doDefault(
			@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);

			}
			populateReportForm(form, request);
			ParticipantAddressesReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			return forwards.get("input");
		}
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	

	/**
	 * Invokes the download task. The first half of this task uses the common
	 * workflow with validateForm set to true. The second half of this task takes
	 * the eligible Employee data object and create the CSV file.
	 *
	 * @return null so that Struts will not try to forward to another page.
	 */
		
	@RequestMapping(value = "/participantAddresses", params = { "task=downloadEligibleEmployeeAdressFile" }, method = {
			RequestMethod.POST, RequestMethod.GET })
	public String doDownloadEligibleEmployeeAdressFile(
			@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}

		}
		
		StringBuffer buildYourPackageNoticeManagerBuffer = new StringBuffer();
		buildYourPackageNoticeManagerBuffer.append(MERRILL_DOWNLOAD_COLUMN_HEADING);
		List<EmployeeEligibleVO> eligibleEmployeeAddressList =
				PlanNoticeDocumentServiceDelegate.getInstance().getEligibleEmployeeDetails(getUserProfile(request).getCurrentContract().getContractNumber());

		int eligibleemployecount = 0;

		try {

			if (!eligibleEmployeeAddressList.isEmpty() && eligibleEmployeeAddressList.size() > 0) {

				for (EmployeeEligibleVO participantAddressVO : eligibleEmployeeAddressList) {
					buildYourPackageNoticeManagerBuffer.append("\n");
					buildYourPackageNoticeManagerBuffer
							.append(escapeField(participantAddressVO.getParticiapantFirstName())).append(COMMA);
					buildYourPackageNoticeManagerBuffer
							.append(escapeField(participantAddressVO.getParticiapantLastName())).append(COMMA);

					if (participantAddressVO.getParticiapantAddressLine1() != null)
						buildYourPackageNoticeManagerBuffer
								.append(escapeField(participantAddressVO.getParticiapantAddressLine1().trim()));
					buildYourPackageNoticeManagerBuffer.append(COMMA);

					if (participantAddressVO.getParticiapantAddressLine2() != null)
						buildYourPackageNoticeManagerBuffer
								.append(escapeField(participantAddressVO.getParticiapantAddressLine2().trim()));
					buildYourPackageNoticeManagerBuffer.append(COMMA);

					if (participantAddressVO.getParticiapantCity() != null)
						buildYourPackageNoticeManagerBuffer
								.append(escapeField(participantAddressVO.getParticiapantCity().trim()));
					buildYourPackageNoticeManagerBuffer.append(COMMA);

					if (participantAddressVO.getParticiapantState() != null)
						buildYourPackageNoticeManagerBuffer
								.append(escapeField(participantAddressVO.getParticiapantState().trim()));
					buildYourPackageNoticeManagerBuffer.append(COMMA);

					if (participantAddressVO.getParticiapantZip() != null) {
						String zipCode = StringUtils.trim(participantAddressVO.getParticiapantZip());
						if (zipCode.length() > 0) {
							buildYourPackageNoticeManagerBuffer.append(zipCode.toUpperCase());
						}
					}

					buildYourPackageNoticeManagerBuffer.append(COMMA);

					if (participantAddressVO.getParticiapantCountry() != null)
						buildYourPackageNoticeManagerBuffer
								.append(escapeField(participantAddressVO.getParticiapantCountry().trim()));
					buildYourPackageNoticeManagerBuffer.append(COMMA);
					eligibleemployecount++;

				}

			}

			HttpSession session = request.getSession(false);

			if (isParticipantAddressAvailable(eligibleEmployeeAddressList)) {
				response.getWriter().write(EMPLOY_GENERATED + "&" + eligibleemployecount);
				session.setAttribute(GENERATED_EMPLOY_NAME, buildYourPackageNoticeManagerBuffer.toString().getBytes());
			} else {
				response.getWriter().write(NO_EMPLOY_INFO);
				session.setAttribute(GENERATED_EMPLOY_NAME, buildYourPackageNoticeManagerBuffer.toString().getBytes());
			}
		} catch (IOException e) {
			logger.error(
					"Found issue while adding the eligible employee address file to session to retrieve it in NMC build your package page");
			e.printStackTrace();
		}

		return null;

	}
		
	@SuppressWarnings("unchecked")
	protected boolean isParticipantAddressAvailable(List<EmployeeEligibleVO> listParticipantAddressVO) {
		Iterator iterator = listParticipantAddressVO.iterator();
		while (iterator.hasNext()) {
			EmployeeEligibleVO employeeEligibleVO = (EmployeeEligibleVO) iterator.next();
			if (employeeEligibleVO.getParticiapantAddressLine1() != null
					&& !employeeEligibleVO.getParticiapantAddressLine1().isEmpty()) {
				return true;
			}
			if (employeeEligibleVO.getParticiapantAddressLine2() != null
					&& !employeeEligibleVO.getParticiapantAddressLine2().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	@RequestMapping(value = {"/participantAddresses","/participantAddressDownload/"}, params = {"task=filter"}, method = {RequestMethod.GET })
	public String doFilter(@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);

			}
			populateReportForm(form, request);
			ParticipantAddressesReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			return forwards.get("input");
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value = {"/participantAddresses","/participantAddressDownload/"}, params = {"task=page"}, method = {RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);

			}
			populateReportForm(form, request);
			ParticipantAddressesReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			return forwards.get("input");
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value = {"/participantAddresses","/participantAddressDownload/"}, params = {"task=sort"}, method = {RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);

			}
			populateReportForm(form, request);
			ParticipantAddressesReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			return forwards.get("input");
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value = {"/participantAddresses","/participantAddressDownload/"}, params = {"task=download" }, method = {RequestMethod.GET })
	public String doDownload(
			@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);

			}
			populateReportForm(form, request);
			ParticipantAddressesReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			return forwards.get("input");
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value = {"/participantAddresses","/participantAddressDownload/"}, params = {"task=downloadAll" }, method = {RequestMethod.GET })
	public String doDownloadAll(
			@Valid @ModelAttribute("participantAddressesReportForm") ParticipantAddressesReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);

			}
			populateReportForm(form, request);
			ParticipantAddressesReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			return forwards.get("input");
		}
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

}