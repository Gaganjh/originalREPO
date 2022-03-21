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
import com.manulife.pension.bd.web.report.BDReportController;
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
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.service.security.bd.report.valueobject.BDExtUserReportData;
import com.manulife.pension.service.security.bd.report.valueobject.BDExtUserReportDetails;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

@Controller
@RequestMapping(value = "/usermanagement")
@SessionAttributes({ "prospectsReportForm" })

public class ExtUserProspectsReportController extends BDReportController {
	@ModelAttribute("prospectsReportForm")
	public ExtUserProspectsReportForm populateForm() {
		return new ExtUserProspectsReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	private static ReportSortList DefaultSort = new ReportSortList();
	static {
		forwards.put("input", "/usermanagement/prospectsReport.jsp");
		forwards.put("default", "/usermanagement/prospectsReport.jsp");
		forwards.put("page", "/usermanagement/prospectsReport.jsp");
		forwards.put("reset", "/usermanagement/prospectsReport.jsp");
		forwards.put("filter", "/usermanagement/prospectsReport.jsp");
		
		DefaultSort.add(new ReportSort(BDExtUserReportData.SORT_LAST_NAME, ReportSort.ASC_DIRECTION));
		DefaultSort.add(new ReportSort(BDExtUserReportData.SORT_FIRST_NAME, ReportSort.ASC_DIRECTION));
		DefaultSort.add(new ReportSort(BDExtUserReportData.SORT_REGISTRATIONI_DATE, ReportSort.ASC_DIRECTION));
		DefaultSort.add(new ReportSort(BDExtUserReportData.SORT_FIRM_NAME, ReportSort.ASC_DIRECTION));
	}

	
	// SimpleDateFormat is converted to FastDateFormat to make it thread safe
	private FastDateFormat format = FastDateFormat.getInstance(RenderConstants.MEDIUM_MDY_SLASHED);
	private static final String currentDate = DateRender.formatByPattern(new Date(), "",
			BDConstants.DATE_FORMAT_MMDDYYYY);

	private static final String[] DOWNLOAD_COLUMN_HEADINGS = new String[] { "Name", "Registration Date", "Firm Name",
			"Phone Number", "Email Address" };

	private static final String REPORT_TITLE = "Prospect User Reports";

	private static final String FILTERS_USED_LABEL = "Filters used";

	private static final String LAST_NAME_LABEL = "Last Name:";

	private static final String FIRST_NAME_LABEL = "First Name:";

	private static final String STATE_LABEL = "State:";

	private static final String REGISTERED_FROM_LABEL = "Registered from:";

	private static final String REGISTERED_TO_LABEL = "Registered to:";

	private final RegularExpressionRule nameRErule = new RegularExpressionRule(BDErrorCodes.USER_SEARCH_INPUT_INVALID,
			BDRuleConstants.FIRST_NAME_LAST_NAME_RE);

	/**
	 * Constructor
	 */
	public ExtUserProspectsReportController() {
		super(ExtUserProspectsReportController.class);
	}

	/**
	 * This method changes the POST request to GET request to avoid web page
	 * expired messages. The input data will be taken from the form which is in
	 * session. Since the task parameter is in request it will be appended to
	 * the request path. Otherwise it will be lost as we are generating a new
	 * request using redirect.
	 * 
	 * @param mapping
	 *            The Struts Action Mapping object.
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

	@RequestMapping(value = "/reports/prospects", method = { RequestMethod.POST })
	public String execute(ExtUserProspectsReportForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		ControllerForward forward = new ControllerForward("refresh",
				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
		return "redirect:" + forward.getPath();

	}

	@RequestMapping(value = "/reports/prospects", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("prospectsReportForm") ExtUserProspectsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/reports/prospects", params = { "task=filter" }, method = { RequestMethod.GET })
	public String doFilter(@Valid @ModelAttribute("prospectsReportForm") ExtUserProspectsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

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
			//return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);	
			return forwards.get("input");
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/reports/prospects", params = { "task=page" }, method = { RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("prospectsReportForm") ExtUserProspectsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/reports/prospects", params = { "task=download" }, method = { RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("prospectsReportForm") ExtUserProspectsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	/**
	 * This method will be called when the task is reset. This will redirect the
	 * user to the input jsp page and thereby reset the form. This will behave
	 * as if the user visits the page for the first time.
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
	@RequestMapping(value = "/reports/prospects", params = { "task=reset" }, method = { RequestMethod.GET })
	public String doRefresh(@Valid @ModelAttribute("prospectsReportForm") ExtUserProspectsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(request.getAttribute("penTestFlag")!=null && (Boolean)request.getAttribute("penTestFlag")){
			return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");// if input forward not
												// //available, provided default
			}
		}
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
		String task = getTask(request);
		// Show an empty report i) if task is empty or ii) default iii) if no
		// filters are present
		if (StringUtils.isEmpty(task) || StringUtils.equals(DEFAULT_TASK, task)) {
			BDExtUserReportData reportData = new BDExtUserReportData(null, 0);
			request.setAttribute(BDConstants.REPORT_BEAN, reportData);
			return forwards.get("input");
		} else if (StringUtils.equals(FILTER_TASK, task) && !hasFilters(reportForm)) {
			BDExtUserReportData reportData = new BDExtUserReportData(null, 0);
			request.setAttribute(BDConstants.REPORT_BEAN, reportData);
			return forwards.get("input");
		} else if (StringUtils.equals(DOWNLOAD_TASK, task) && !hasFilters(reportForm)) {
			BDExtUserReportData reportData = new BDExtUserReportData(null, 0);
			request.setAttribute(BDConstants.REPORT_BEAN, reportData);
			return null;
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
		return BDExtUserReportData.SORT_LAST_NAME;
	}

	/**
	 * Returns the default sort direction
	 * 
	 * @return String
	 */
	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
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
		ExtUserProspectsReportForm form = (ExtUserProspectsReportForm) reportForm;
		ReportCriteria criteria = theReport.getReportCriteria();
		if (criteria == null) {
			criteria = new ReportCriteria(BDExtUserReportData.PROSPECTS_REPORT_ID);
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

		StringBuffer buffer = new StringBuffer();

		buffer.append(REPORT_TITLE);
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		// Filters
		buffer.append(FILTERS_USED_LABEL).append(COMMA);
		buffer.append(LAST_NAME_LABEL).append(COMMA).append(getCsvString(form.getLastName()));
		buffer.append(LINE_BREAK).append(WHITE_SPACE_CHAR).append(COMMA);
		buffer.append(FIRST_NAME_LABEL).append(COMMA).append(getCsvString(form.getFirstName()));
		buffer.append(LINE_BREAK).append(WHITE_SPACE_CHAR).append(COMMA);
		buffer.append(STATE_LABEL).append(COMMA)
				.append(getCsvString(criteria.getFilterValue(BDExtUserReportData.FILTER_STATE)));
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
				buffer.append(strRegDate).append(COMMA);
				String firmName = userDetails.getFirmName();
				if (StringUtils.isEmpty(firmName)) {
					buffer.append(BDConstants.HYPHON_SYMBOL).append(COMMA);
				} else {
					buffer.append(getCsvString(firmName)).append(COMMA);
				}
				buffer.append(userDetails.getPhoneNumber()).append(COMMA);
				buffer.append(userDetails.getEmailAddress());
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
		return BDExtUserReportData.PROSPECTS_REPORT_ID;
	}

	/**
	 * Returns the report name
	 * 
	 * @return String
	 */
	@Override
	protected String getReportName() {
		return BDExtUserReportData.PROSPECTS_REPORT_NAME + currentDate;
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
		ExtUserProspectsReportForm prospectsReportForm = (ExtUserProspectsReportForm) form;
		String lastName = StringEscapeUtils.escapeSql(prospectsReportForm.getLastName());
		if (StringUtils.isNotEmpty(lastName)) {
			criteria.addFilter(BDExtUserReportData.FILTER_LAST_NAME, StringUtils.upperCase(lastName));
		}
		String firstName = StringEscapeUtils.escapeSql(prospectsReportForm.getFirstName());
		if (StringUtils.isNotEmpty(firstName)) {
			criteria.addFilter(BDExtUserReportData.FILTER_FIRST_NAME, StringUtils.upperCase(firstName));
		}
		if (StringUtils.isNotEmpty(prospectsReportForm.getState())) {
			criteria.addFilter(BDExtUserReportData.FILTER_STATE, prospectsReportForm.getState());
		}
		String regFromDate = prospectsReportForm.getRegFromDate();
		if (StringUtils.isNotEmpty(regFromDate)) {
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
		String regToDate = prospectsReportForm.getRegToDate();
		if (StringUtils.isNotEmpty(regToDate)) {
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
		criteria.setSorts(DefaultSort);
	}

	/**
	 * This method returns a flag that indicates whether any filter expressions
	 * are present or not
	 * 
	 * @param reportForm
	 * @return boolean
	 */
	private boolean hasFilters(BaseReportForm reportForm) {
		ExtUserProspectsReportForm prospectsForm = (ExtUserProspectsReportForm) reportForm;
		if (StringUtils.isEmpty(prospectsForm.getFirstName()) && StringUtils.isEmpty(prospectsForm.getLastName())
				&& StringUtils.isEmpty(prospectsForm.getState()) && StringUtils.isEmpty(prospectsForm.getRegFromDate())
				&& StringUtils.isEmpty(prospectsForm.getRegToDate())) {
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
    protected Collection doValidate(ActionForm form,
            HttpServletRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doValidate");
        }
        ExtUserProspectsReportForm prospectsReportForm = (ExtUserProspectsReportForm) form;
        Collection<ValidationError> errors = super.doValidate( form, request);
        // Validation is only for non-default tasks
        String task = getTask(request);
        if (StringUtils.isNotEmpty(task) && !StringUtils.equals(DEFAULT_TASK, task)
                && !StringUtils.equals(ExtUserReportHelper.RESET_TASK, task)) {
            Date fromDate = null;
            Date toDate = null;
            String firstName = prospectsReportForm.getFirstName();
            String lastName = prospectsReportForm.getLastName();
            if (StringUtils.isNotEmpty(firstName)) {
                nameRErule.validate("", errors, firstName);
            }
            if (errors.size() == 0 && StringUtils.isNotEmpty(lastName)) {
                nameRErule.validate("", errors, lastName);
            }

            // Both dates should be present or both should be empty
            if ((StringUtils.isEmpty(prospectsReportForm.getRegFromDate()) && StringUtils
                    .isNotEmpty(prospectsReportForm.getRegToDate()))
                    || (StringUtils.isNotEmpty(prospectsReportForm.getRegFromDate()) && StringUtils
                            .isEmpty(prospectsReportForm.getRegToDate()))) {
                errors.add(new ValidationError("", BDErrorCodes.INVALID_DATE));
                // validate if both dates are present
            } else if (StringUtils.isNotEmpty(prospectsReportForm.getRegFromDate())
                    && StringUtils.isNotEmpty(prospectsReportForm.getRegToDate())) {
                try {
                    fromDate = ExtUserReportHelper.validateDateFormat(prospectsReportForm
                            .getRegFromDate());
                    toDate = ExtUserReportHelper.validateDateFormat(prospectsReportForm
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
