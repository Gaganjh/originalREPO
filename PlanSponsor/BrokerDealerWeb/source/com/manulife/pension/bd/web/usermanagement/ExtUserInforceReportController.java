package com.manulife.pension.bd.web.usermanagement;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.firmsearch.FirmSearchUtils;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.util.ProtectedStringBuffer;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWExtUser;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.report.valueobject.BDExtUserReportData;
import com.manulife.pension.service.security.bd.report.valueobject.BDExtUserReportDetails;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * This is the action for Inforce external user report
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping(value = "/usermanagement")
@SessionAttributes({ "inforceReportForm" })

public class ExtUserInforceReportController extends BDReportController {
	@ModelAttribute("inforceReportForm")
	public ExtUserInforceReportForm populateForm() {
		return new ExtUserInforceReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/usermanagement/inforceReport.jsp");
		forwards.put("default", "/usermanagement/inforceReport.jsp");
		forwards.put("page", "/usermanagement/inforceReport.jsp");
		forwards.put("reset", "/usermanagement/inforceReport.jsp");
		forwards.put("sort", "/usermanagement/inforceReport.jsp");
		forwards.put("filter", "/usermanagement/inforceReport.jsp");
	}

	// SimpleDateFormat is converted to FastDateFormat to make it thread safe
	private FastDateFormat format = FastDateFormat.getInstance(RenderConstants.MEDIUM_MDY_SLASHED);

	private static final String currentDate = DateRender.formatByPattern(new Date(), "",
			BDConstants.DATE_FORMAT_MMDDYYYY);

	private static final String[] DOWNLOAD_COLUMN_HEADINGS = new String[] { "Name", "User Role", "Registration Date",
			"Firm Name" };

	private static final String REPORT_TITLE = "Inforce User Reports";

	private static final String FILTERS_USED_LABEL = "Filters used";

	private static final String LAST_NAME_LABEL = "Last Name:";

	private static final String FIRST_NAME_LABEL = "First Name:";

	private static final String CONTRACT_NO_LABEL = "Contract Number:";

	private static final String BD_FIRM_LABEL = "BD Firm:";

	private static final String ROLE_LABEL = "Role:";

	private static final String REGISTERED_FROM_LABEL = "Registered from:";

	private static final String REGISTERED_TO_LABEL = "Registered to:";

	private final RegularExpressionRule nameRErule = new RegularExpressionRule(BDErrorCodes.USER_SEARCH_INPUT_INVALID,
			BDRuleConstants.FIRST_NAME_LAST_NAME_RE);

	/**
	 * Constructor
	 */
	public ExtUserInforceReportController() {
		super(ExtUserInforceReportController.class);
	}

	/**
	 * This method changes the POST request to GET request to avoid web page
	 * expired messages. The input data will be taken from the form which is in
	 * session. Since the task parameter is in request it will be appended to
	 * the request path. Otherwise it will be lost as we are generating a new
	 * request using redirect.
	 * 
	 * @param form
	 *            The current report form.
	 * @param request
	 *            The current request object.
	 * @param response
	 *            The current response object.
	 * @return ActionForward
	 * @throws IOException
	 * @throws ServletException
	 */

	@RequestMapping(value = "/reports/inforce", method = { RequestMethod.POST })
	public String execute(@ModelAttribute("inforceReportForm") ExtUserInforceReportForm form,BindingResult bindingResult, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		ControllerForward forward = new ControllerForward("refresh",
				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
		return "redirect:" + forward.getPath();

	}

	@RequestMapping(value = "/reports/inforce", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("inforceReportForm") ExtUserInforceReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if (StringUtils.isNotBlank(forward) ) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/reports/inforce", params = { "task=filter" }, method = { RequestMethod.GET })
	public String doFilter(@Valid @ModelAttribute("inforceReportForm") ExtUserInforceReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if (StringUtils.isNotBlank(forward) ) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		Collection errors = doValidate(form, request);
		if(errors.size()>0){
			BaseSessionHelper.setErrorsInSession(request, errors);
			return forwards.get("input");
		}
		forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/reports/inforce", params = { "task=download" }, method = { RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("inforceReportForm") ExtUserInforceReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if (StringUtils.isNotBlank(forward) ) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/reports/inforce", params = { "task=sort" }, method = { RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("inforceReportForm") ExtUserInforceReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if (StringUtils.isNotBlank(forward) ) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/reports/inforce", params = { "task=page" }, method = { RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("inforceReportForm") ExtUserInforceReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if (StringUtils.isNotBlank(forward) ) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	/**
	 * This method will be called when the task is reset. This will redirect the
	 * user to the input jsp page and thereby reset the form. This will behave
	 * as if the user visits the page for the first time.
	 * 
	 * @param reportForm
	 *            The current report form.
	 * @param request
	 *            The current request object.
	 * @param response
	 *            The current response object.
	 * @return The ActionForward appropriate for this task.
	 * @throws SystemException
	 */
	@RequestMapping(value = "/reports/inforce", params = { "task=reset" }, method = { RequestMethod.GET })
	public String doRefresh(@Valid @ModelAttribute("inforceReportForm") ExtUserInforceReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doReset");
		}
		BDExtUserReportData reportData = new BDExtUserReportData(null, 0);
		request.setAttribute(BDConstants.REPORT_BEAN, reportData);
		super.resetForm(form, request);
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doReset");
		}
		return forwards.get("input");
	}

	@Override
	protected String preExecute(ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
		ExtUserInforceReportForm f = (ExtUserInforceReportForm) form;

		f.setFirmId(FirmSearchUtils.getFirmPartyId(f.getFirmName()));
		return super.preExecute(form, request, response);
	}

	/**
	 * The common method used by all other doXXX() methods. By default an empty
	 * report should be shown. So when the user visits the report for the first
	 * time with default task an empty report object will be set in request
	 * scope the request will be forwarded to the input jsp page. Otherwise the
	 * superclass method will be called.
	 * 
	 * @param mapping
	 *            The Struts Action Mapping object.
	 * @param reportForm
	 *            The current report form.
	 * @param request
	 *            The current request object.
	 * @param response
	 *            The current response object.
	 * @return The ActionForward appropriate for this task.
	 * @throws SystemException
	 */
	protected String doCommon(BaseReportForm reportForm, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		ExtUserInforceReportForm form = (ExtUserInforceReportForm) reportForm;
		String task = getTask(request);
		// Show an empty report i) if task is empty or ii) default iii) if no
		// filters are present or
		// invalid combination
		if (StringUtils.isEmpty(task) || StringUtils.equals(DEFAULT_TASK, task)) {
			BDExtUserReportData reportData = new BDExtUserReportData(null, 0);
			request.setAttribute(BDConstants.REPORT_BEAN, reportData);
			return forwards.get("input");
		} else if ((StringUtils.equals(FILTER_TASK, task) || StringUtils.equals(SORT_TASK, task))
				&& (!hasFilters(reportForm) || !isValidCombination(reportForm))) {
			if (StringUtils.equals(SORT_TASK, task)) {
				form.setSortField(getDefaultSort());
				form.setSortDirection(getDefaultSortDirection());
			}
			BDExtUserReportData reportData = new BDExtUserReportData(null, 0);
			request.setAttribute(BDConstants.REPORT_BEAN, reportData);
			return forwards.get("input");
		} else if (StringUtils.equals(DOWNLOAD_TASK, task) && !hasFilters(reportForm)) {
			BDExtUserReportData reportData = new BDExtUserReportData(null, 0);
			request.setAttribute(BDConstants.REPORT_BEAN, reportData);
			return null;
		}
		String contractNumStr = form.getContractNum();
		String firmName = form.getFirmName();
		String riaFirmName = form.getSelectedRiaFirmName();

		if ((!StringUtils.isEmpty(contractNumStr) && form.getContractNumValue() == null)
				|| (!StringUtils.isEmpty(firmName) && StringUtils.isEmpty(form.getFirmId()))
				|| (!StringUtils.isEmpty(riaFirmName) && StringUtils.isEmpty(form.getSelectedRiaFirmId()))) {
			BDExtUserReportData reportData = new BDExtUserReportData(null, 0);
			request.setAttribute(BDConstants.REPORT_BEAN, reportData);
			return forwards.get("input");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doCommon");
		}
		return super.doCommon(reportForm, request, response);
	}

	

	/**
	 * Returns the default sort field for the criteria
	 * 
	 * @return String The default sort field for the criteria.
	 */
	@Override
	protected String getDefaultSort() {
		return null;
	}

	/**
	 * Returns the default sort direction
	 * 
	 * @return String
	 */
	@Override
	protected String getDefaultSortDirection() {
		return null;
	}

	/**
	 * This method will return data to create the report in CSV format
	 * 
	 * @param reportForm
	 *            The report form.
	 * @param report
	 *            The report data.
	 * @param request
	 *            The HTTP request.
	 * @return byte array
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getDownloadData");
		}
		BDExtUserReportData theReport = (BDExtUserReportData) report;
		ExtUserInforceReportForm form = (ExtUserInforceReportForm) reportForm;
		ReportCriteria criteria = theReport.getReportCriteria();
		if (criteria == null) {
			criteria = new ReportCriteria(BDExtUserReportData.INFORCE_REPORT_ID);
		}
		String regStartDate = "";
		String regEndDate = "";
		if (criteria.getFilterValue(BDExtUserReportData.FILTER_REGISTRATION_FROM_DATE) != null) {
			regStartDate = DateRender.formatByPattern(
					criteria.getFilterValue(BDExtUserReportData.FILTER_REGISTRATION_FROM_DATE), "",
					RenderConstants.MEDIUM_MDY_SLASHED);
		}
		if (criteria.getFilterValue(BDExtUserReportData.FILTER_REGISTRATION_TO_DATE) != null) {
			regEndDate = DateRender.formatByPattern(
					criteria.getFilterValue(BDExtUserReportData.FILTER_REGISTRATION_TO_DATE), "",
					RenderConstants.MEDIUM_MDY_SLASHED);
		}

		ProtectedStringBuffer buffer = new ProtectedStringBuffer();

		buffer.append(REPORT_TITLE);
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		// Filters
		buffer.append(FILTERS_USED_LABEL).append(COMMA);
		buffer.append(LAST_NAME_LABEL).append(COMMA).append(getCsvString(form.getLastName()));
		buffer.append(LINE_BREAK).append(WHITE_SPACE_CHAR).append(COMMA);
		buffer.append(FIRST_NAME_LABEL).append(COMMA).append(getCsvString(form.getFirstName()));
		buffer.append(LINE_BREAK).append(WHITE_SPACE_CHAR).append(COMMA);
		buffer.append(CONTRACT_NO_LABEL).append(COMMA)
				.append(getCsvString(criteria.getFilterValue(BDExtUserReportData.FILTER_CONTRACT_NUM)));
		buffer.append(LINE_BREAK).append(WHITE_SPACE_CHAR).append(COMMA);
		buffer.append(BD_FIRM_LABEL).append(COMMA).append(getCsvString(form.getFirmName()));
		buffer.append(LINE_BREAK).append(WHITE_SPACE_CHAR).append(COMMA);
		String userRoleStr = "";
		if (criteria.getFilterValue(BDExtUserReportData.FILTER_USER_ROLE) != null) {
			String userRoleCode = (String) criteria.getFilterValue(BDExtUserReportData.FILTER_USER_ROLE);
			userRoleStr = ExtUserSearchReportHelper.getUserRoleDisplay(BDUserRoleType.getByRoleCode(userRoleCode));
		}
		buffer.append(ROLE_LABEL).append(COMMA).append(getCsvString(userRoleStr));
		buffer.append(LINE_BREAK).append(WHITE_SPACE_CHAR).append(COMMA);
		buffer.append(REGISTERED_FROM_LABEL).append(COMMA).append(regStartDate);
		buffer.append(LINE_BREAK).append(WHITE_SPACE_CHAR).append(COMMA);
		buffer.append(REGISTERED_TO_LABEL).append(COMMA).append(regEndDate);
		buffer.append(LINE_BREAK).append(WHITE_SPACE_CHAR).append(COMMA);
		buffer.append(LINE_BREAK);
		// Report Details
		if (theReport.getDetails() != null && theReport.getDetails().size() > 0) {
			for (int i = 0; i < DOWNLOAD_COLUMN_HEADINGS.length; i++) {
				buffer.append(DOWNLOAD_COLUMN_HEADINGS[i]);
				if (i != DOWNLOAD_COLUMN_HEADINGS.length - 1) {
					buffer.append(COMMA);
				}
			}
			buffer.append(LINE_BREAK);
			Iterator<BDExtUserReportDetails> userDetailsIter = theReport.getDetails().iterator();
			while (userDetailsIter.hasNext()) {
				BDExtUserReportDetails userDetails = userDetailsIter.next();
				String strRegDate = DateRender.formatByPattern(userDetails.getRegistrationDate(), "",
						RenderConstants.MEDIUM_MDY_SLASHED);
				buffer.append(userDetails.getFirstName() + " " + userDetails.getLastName()).append(COMMA);
				buffer.append(ExtUserSearchReportHelper.getUserRoleDisplay(userDetails.getRoleType())).append(COMMA);
				buffer.append(strRegDate).append(COMMA);
				String firmName = userDetails.getFirmName();
				if (StringUtils.isEmpty(firmName)) {
					buffer.append(BDConstants.HYPHON_SYMBOL);
				} else {
					buffer.append(getCsvString(firmName));
				}
				buffer.append(LINE_BREAK);
			}
		} else {
			for (int i = 0; i < DOWNLOAD_COLUMN_HEADINGS.length; i++) {
				buffer.append(DOWNLOAD_COLUMN_HEADINGS[i]);
				if (i != DOWNLOAD_COLUMN_HEADINGS.length - 1) {
					buffer.append(COMMA);
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/**
	 * Returns the Report Id
	 * 
	 * @return String
	 */
	@Override
	protected String getReportId() {
		return BDExtUserReportData.INFORCE_REPORT_ID;
	}

	/**
	 * Returns the report name
	 * 
	 * @return String
	 */
	@Override
	protected String getReportName() {
		return BDExtUserReportData.INFORCE_REPORT_NAME + currentDate;
	}

	/**
	 * This method populates a report criteria with the information from the
	 * Action Form and the Request. This method is called every time before
	 * getReportData.
	 * 
	 * @param criteria
	 *            The report criteria to populate
	 * @param form
	 *            The form that contains the user's submitted content.
	 * @param request
	 *            The HTTP request.
	 * 
	 * @throws SystemException
	 */
	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		ExtUserInforceReportForm inforceReportForm = (ExtUserInforceReportForm) form;
		String lastName = StringEscapeUtils.escapeSql(inforceReportForm.getLastName());
		if (!StringUtils.isEmpty(lastName)) {
			criteria.addFilter(BDExtUserReportData.FILTER_LAST_NAME, StringUtils.upperCase(lastName));
		}
		String firstName = StringEscapeUtils.escapeSql(inforceReportForm.getFirstName());
		if (!StringUtils.isEmpty(firstName)) {
			criteria.addFilter(BDExtUserReportData.FILTER_FIRST_NAME, StringUtils.upperCase(firstName));
		}
		if (inforceReportForm.getContractNumValue() != null) {
			criteria.addFilter(BDExtUserReportData.FILTER_CONTRACT_NUM, inforceReportForm.getContractNumValue());
		}
		String firmId = inforceReportForm.getFirmId();

		if (!StringUtils.isEmpty(firmId)) {
			criteria.addFilter(BDExtUserReportData.FILTER_BD_FIRM, Long.parseLong(firmId));
		}

		String riaFirmId = inforceReportForm.getSelectedRiaFirmId();

		if (!StringUtils.isEmpty(riaFirmId)) {
			criteria.addFilter(BDExtUserReportData.FILTER_RIA_FIRM, Long.parseLong(riaFirmId));
		}

		String userRoleCode = inforceReportForm.getUserRoleCode();
		if (!StringUtils.isEmpty(userRoleCode)) {
			criteria.addFilter(BDExtUserReportData.FILTER_USER_ROLE, userRoleCode);
		}
		String regFromDate = inforceReportForm.getRegFromDate();
		if (!StringUtils.isEmpty(regFromDate)) {
			try {
				Date fromDate = format.parse(regFromDate);
				criteria.addFilter(BDExtUserReportData.FILTER_REGISTRATION_FROM_DATE, fromDate);
			} catch (ParseException parseException) {
				if (logger.isDebugEnabled()) {
					logger.debug("ParseException in Registration From Date " + "populateReportCriteria() "
							+ "ExtUserInforceReportAction:", parseException);
				}
			}
		}
		String regToDate = inforceReportForm.getRegToDate();
		if (!StringUtils.isEmpty(regToDate)) {
			try {
				Date toDate = format.parse(regToDate);
				criteria.addFilter(BDExtUserReportData.FILTER_REGISTRATION_TO_DATE, toDate);
			} catch (ParseException parseException) {
				if (logger.isDebugEnabled()) {
					logger.debug("ParseException in Registration To Date " + "populateReportCriteria() "
							+ "ExtUserInforceReportAction:", parseException);
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}
	}

	/**
	 * Populate sort criteria in the criteria object.
	 * 
	 * @param criteria
	 *            The criteria to populate
	 * @param form
	 *            The Form
	 */
	@Override
	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateSortCriteria");
		}

		String sortField = (form.getSortField() == null) ? BDExtUserReportData.SORT_LAST_NAME : form.getSortField();
		String sortDirection = (form.getSortDirection() == null) ? ReportSort.ASC_DIRECTION : form.getSortDirection();

		/* primary sort */
		criteria.insertSort(sortField, sortDirection);

		/* secondary sorts */
		if (BDExtUserReportData.SORT_LAST_NAME.equals(sortField)) {
			criteria.insertSort(BDExtUserReportData.SORT_FIRST_NAME, sortDirection);
			criteria.insertSort(BDExtUserReportData.SORT_REGISTRATIONI_DATE, sortDirection);
			criteria.insertSort(BDExtUserReportData.SORT_FIRM_NAME, sortDirection);
		} else if (BDExtUserReportData.SORT_FIRM_NAME.equals(sortField)) {
			criteria.insertSort(BDExtUserReportData.SORT_LAST_NAME, sortDirection);
			criteria.insertSort(BDExtUserReportData.SORT_FIRST_NAME, sortDirection);
			criteria.insertSort(BDExtUserReportData.SORT_REGISTRATIONI_DATE, sortDirection);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateSortCriteria");
		}
	}

	/**
	 * This method removes sortField and sortDirection properties from the
	 * action form.
	 * 
	 * @param mapping
	 * 
	 * @param reportForm
	 *            The report form to populate.
	 * @param request
	 *            The current request object.
	 */
	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}

		super.populateReportForm(reportForm, request);
		String task = getTask(request);
		ExtUserInforceReportForm theForm = (ExtUserInforceReportForm) reportForm;
		// This has been done to remove the default sort field, sort direction.

		// The sort icon will not be displayed when the user has come to the
		// page by default,
		// before doing any kind of sorting. After the sorting, the sort icon
		// will always be
		// displayed, until the user resets the page with default view or use a
		// different filter criteria.

		if (DEFAULT_TASK.equals(task) || FILTER_TASK.equals(task)) {
			theForm.setSortDirection(null);
			theForm.setSortField(null);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportForm");
		}
	}

	/**
	 * This method returns a flag that indicates whether any filter expressions
	 * are present or not
	 * 
	 * @param reportForm
	 * @return boolean
	 */
	private boolean hasFilters(BaseReportForm reportForm) {
		ExtUserInforceReportForm inforceForm = (ExtUserInforceReportForm) reportForm;
		if (StringUtils.isEmpty(inforceForm.getFirstName()) && StringUtils.isEmpty(inforceForm.getLastName())
				&& StringUtils.isEmpty(inforceForm.getContractNum())
				&& StringUtils.isEmpty(inforceForm.getUserRoleCode()) && StringUtils.isEmpty(inforceForm.getFirmId())
				&& StringUtils.isEmpty(inforceForm.getSelectedRiaFirmId())
				&& StringUtils.isEmpty(inforceForm.getRegFromDate())
				&& StringUtils.isEmpty(inforceForm.getRegToDate())) {
			return false;
		}
		return true;
	}

	/**
	 * This method returns a flag that indicates whether input combination is
	 * valid or not. (If contract number is given and the selected role is
	 * Broker Dealer Firm Rep, results should not be returned.)
	 * 
	 * @param reportForm
	 * @return boolean
	 */
	private boolean isValidCombination(BaseReportForm reportForm) {
		ExtUserInforceReportForm inforceForm = (ExtUserInforceReportForm) reportForm;
		if (inforceForm.getContractNumValue() != null
				&& StringUtils.equals(inforceForm.getUserRoleCode(), BDUserRoleType.FirmRep.getUserRoleCode())) {
			return false;
		}
		return true;
	}

	 /**
     * This method validated the filter input and returns a Collection object with errors if any.
     * 
     * @param mapping The Struts Action Mapping object.
     * @param form Form the current form
     * @param request HttpServletRequest the current request
     * 
     * @return Empty Collection
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Collection doValidate( ActionForm form, HttpServletRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doValidate");
        }
        ExtUserInforceReportForm inforceReportForm = (ExtUserInforceReportForm) form;
        Collection<ValidationError> errors = super.doValidate(form, request);
        // Validation is only for non-default tasks
        String task = getTask(request);
        if (StringUtils.isNotEmpty(task) && !StringUtils.equals(DEFAULT_TASK, task)
                && !StringUtils.equals(ExtUserReportHelper.RESET_TASK, task)) {
            Date fromDate = null;
            Date toDate = null;
            String firstName = inforceReportForm.getFirstName();
            String lastName = inforceReportForm.getLastName();
            if (StringUtils.isNotEmpty(firstName)) {
                nameRErule.validate("", errors, firstName);
            }
            if (errors.size() == 0 && StringUtils.isNotEmpty(lastName)) {
                nameRErule.validate("", errors, lastName);
            }
            
            // Both dates should be present or both should be empty
            if ((StringUtils.isEmpty(inforceReportForm.getRegFromDate()) && StringUtils
                    .isNotEmpty(inforceReportForm.getRegToDate()))
                    || (StringUtils.isNotEmpty(inforceReportForm.getRegFromDate()) && StringUtils
                            .isEmpty(inforceReportForm.getRegToDate()))) {
                errors.add(new ValidationError("", BDErrorCodes.INVALID_DATE));
                // validate if both dates are present
            } else if (StringUtils.isNotEmpty(inforceReportForm.getRegFromDate())
                    && StringUtils.isNotEmpty(inforceReportForm.getRegToDate())) {
                try {
                    fromDate = ExtUserReportHelper.validateDateFormat(inforceReportForm
                            .getRegFromDate());
                    toDate = ExtUserReportHelper.validateDateFormat(inforceReportForm
                            .getRegToDate());
                    if (toDate.before(fromDate)) {
                        errors.add(new ValidationError("", BDErrorCodes.INVALID_DATE));
                    }
                } catch (ParseException pe) {
                    errors.add(new ValidationError("", BDErrorCodes.INVALID_DATE));
                }
            }
            if (errors.size() > 0) {
                BDExtUserReportData reportData = new BDExtUserReportData(null, 0);
                request.setAttribute(BDConstants.REPORT_BEAN, reportData);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doValidate");
        }
        return errors;
    }

	@Autowired
	private BDValidatorFWExtUser bdValidatorFWExtUser;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWExtUser);
	}

}
