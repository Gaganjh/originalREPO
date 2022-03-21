package com.manulife.pension.ps.web.noticereports;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.platform.web.validation.rules.ContractNumberNoMandatoryRule;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentHistoryReportData;
import com.manulife.pension.ps.service.report.noticereports.reporthandler.AlertsReportHandler;
import com.manulife.pension.ps.service.report.noticereports.valueobject.AlertsReportData;
import com.manulife.pension.ps.service.report.noticereports.valueobject.AlertsReportDistributionVO;
import com.manulife.pension.ps.service.report.noticereports.valueobject.AlertsReportFreqVO;
import com.manulife.pension.ps.service.report.noticereports.valueobject.AlertsReportUserStatsVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.tools.BusinessConversionForm;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.piechart.PieChartBean;

/**
 * Action Class for Alerts Report.
 * 
 */

@Controller
@RequestMapping(value ="/noticereports")
@SessionAttributes({ "alertsReportForm" })

public class AlertsReportController extends ReportController {

	@ModelAttribute("alertsReportForm")
	public AlertsReportForm populateForm() {
		return new AlertsReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/noticereports/alertsReport.jsp");
		forwards.put("default", "/noticereports/alertsReport.jsp");
		forwards.put("search", "/noticereports/alertsReport.jsp");
		forwards.put("alertsReportPage", "/noticereports/alertsReport.jsp");
		forwards.put("print", "/noticereports/alertsReport.jsp");
	}

	public static final FastDateFormat fastDateFormat = ContractDateHelper.getDateFormatterLocale("MM/dd/yyyy",
			Locale.US);

	private static final String DEFAULT_SORT_FIELD = PlanDocumentHistoryReportData.ACTION_DATE_FIELD;

	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;

	public static final String ALERT_FREQUENCY_PIECHART = "pieChart";

	public static final String REPORT_NAME = "reportName";

	public static final String REPORT_TITLE = "Alerts as of ";

	/**
	 * Provides common functionality such as validation
	 */
	
		 @SuppressWarnings({ "unchecked" })
	    public String doCommon(BaseReportForm reportForm,
	            HttpServletRequest request, HttpServletResponse response) throws SystemException {
	        super.doCommon( reportForm, request, response);
	        AlertsReportData reportData = (AlertsReportData) request
	                .getAttribute(CommonConstants.REPORT_BEAN);
	        PieChartBean alertFrequencyPieChartBean = getAlertFrequencyPieChartBean(reportData);
	        request.setAttribute(ALERT_FREQUENCY_PIECHART, alertFrequencyPieChartBean);
	        return forwards.get(CommonConstants.ALERT_REPORT_PAGE);
	    }

	/**
	 * Form validations.
	 * 
	 * @param request
	 * @param errors
	 * @param form
	 * @return errors found.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection doValidate( HttpServletRequest request, AlertsReportForm form) {
		Collection errors = super.doValidate( form, request);

		// from date validation
		Date fromDate = null;
		Date toDate = null;
		try {
			if (StringUtils.isNotBlank(form.getFromDate())) {
				fromDate = fastDateFormat.parse(form.getFromDate());
				if (!fastDateFormat.format(fromDate).equals(form.getFromDate())) {
					throw new ParseException(CommonConstants.INVALID_DATE, 0);
				}
			} else {
				errors.add(new GenericException(ErrorCodes.NMC_CONTROL_REPORTS_FROM_DATE_INVALID));
			}
		} catch (ParseException pe) {
			errors.add(new GenericException(ErrorCodes.NMC_CONTROL_REPORTS_FROM_DATE_INVALID));
		}

		// to date validation
		try {
			if (StringUtils.isNotBlank(form.getToDate())) {
				toDate = fastDateFormat.parse(form.getToDate());
				if (!fastDateFormat.format(toDate).equals(form.getToDate())) {
					throw new ParseException(CommonConstants.INVALID_DATE, 0);
				}
			} else {
				errors.add(new GenericException(ErrorCodes.NMC_CONTROL_REPORTS_TO_DATE_INVALID));
			}
		} catch (ParseException pe) {
			errors.add(new GenericException(ErrorCodes.NMC_CONTROL_REPORTS_TO_DATE_INVALID));
		}

		if (fromDate != null) {
			Calendar calFromDate = Calendar.getInstance();
			calFromDate.setTime(fromDate);
			if (toDate != null) {
				if (fromDate.after(toDate)) {
					errors.add(new GenericException(ErrorCodes.NMC_CONTROL_REPORTS_FROM_DATE_AFTER_TO_DATE));
				}
			}
		}

		// Validate Contract number if it is specified.
		if (form.getContractNumber() != null && form.getContractNumber().length() > 0) {
			// General contract number rule SCR 35
			boolean isValidFormat = ContractNumberNoMandatoryRule.getInstance()
					.validate(BusinessConversionForm.FIELD_CONTRACT_NUMBER, errors, form.getContractNumber());
			if (isValidFormat) {
				// check to make sure contract exists.
				Contract c = null;
				try {
					UserProfile profile = getUserProfile(request);
					c = ContractServiceDelegate.getInstance().getContractDetails(new Integer(form.getContractNumber()),
							EnvironmentServiceDelegate.getInstance().retrieveContractDiDuration(profile.getRole(), 0,
									null));
					if (c == null) {
						errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
					}
				} catch (ContractNotExistException e) {
					errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
				} catch (NumberFormatException e) {
					errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
				} catch (SystemException e) {
					errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
				}
			}
		}

		return errors;
	}

	/**
	 * Action Method for search functionality.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	@RequestMapping(value = "/alertsReport/", params = {"task=search" }, method = {RequestMethod.POST })
	public String doSearch(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			AlertsReportData report = new AlertsReportData();
			report.setReportCriteria(new ReportCriteria(AlertsReportHandler.REPORT_ID));
			report.setDetails(new Vector());
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		Collection<GenericException> errors = doValidate(request, form);
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			AlertsReportData report = new AlertsReportData();
			report.setReportCriteria(new ReportCriteria(AlertsReportHandler.REPORT_ID));
			report.setDetails(new Vector());
			request.setAttribute(Constants.REPORT_BEAN, report);
			return forwards.get("input");
		}
		String forward = doCommon(form, request, response);
		return forward;
	}

	/**
	 * Action Method for reset button.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws SystemException
	 */
	@RequestMapping(value = "/alertsReport/", params = {"task=reset" }, method = { RequestMethod.POST})
	public String doReset(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			AlertsReportData report = new AlertsReportData();
			report.setReportCriteria(new ReportCriteria(AlertsReportHandler.REPORT_ID));
			report.setDetails(new Vector());
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Collection<GenericException> errors = super.doValidate(form, request);
		form.setFromDate(form.getFromDefaultDate());
		form.setToDate(form.getDefaultDate());
		form.setContractNumber("");

		doCommon(form, request, response);
		return forwards.get(CommonConstants.DEFAULT);
	}

	@RequestMapping(value = "/alertsReport/", method = {RequestMethod.GET,RequestMethod.POST })
	public String doDefault(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			AlertsReportData report = new AlertsReportData();
			report.setReportCriteria(new ReportCriteria(AlertsReportHandler.REPORT_ID));
			report.setDetails(new Vector());
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/alertsReport/", params = {"task=filter" }, method = {RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			AlertsReportData report = new AlertsReportData();
			report.setReportCriteria(new ReportCriteria(AlertsReportHandler.REPORT_ID));
			report.setDetails(new Vector());
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/alertsReport/", params={"task=page"}, method = {RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			AlertsReportData report = new AlertsReportData();
			report.setReportCriteria(new ReportCriteria(AlertsReportHandler.REPORT_ID));
			report.setDetails(new Vector());
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/alertsReport/", params = {"task=sort"}, method = { RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			AlertsReportData report = new AlertsReportData();
			report.setReportCriteria(new ReportCriteria(AlertsReportHandler.REPORT_ID));
			report.setDetails(new Vector());
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/alertsReport/", params = {"task=download" }, method = {RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			AlertsReportData report = new AlertsReportData();
			report.setReportCriteria(new ReportCriteria(AlertsReportHandler.REPORT_ID));
			report.setDetails(new Vector());
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	
	protected String getFileName(HttpServletRequest request) {
		// defaults to .csv extension
		AlertsReportData alertsReportData = new AlertsReportData();
		return getReportName() + " " + alertsReportData.getCurrentDate() + CSV_EXTENSION;
	}

	/**
	 * Method to download the data to CSV file
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @throws SystemException
	 */
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		AlertsReportForm form = (AlertsReportForm) reportForm;
		AlertsReportData alertsReportData = (AlertsReportData) report;
		StringBuffer buffer = new StringBuffer();
		buffer.append("\"").append(REPORT_TITLE).append(" ").append(alertsReportData.getCurrentDate()).append("\"")
				.append(LINE_BREAK);
		buffer.append(CommonConstants.CONTRACT_NUMBER).append(COMMA)
				.append(StringUtils.trimToEmpty(form.getContractNumber())).append(LINE_BREAK);
		buffer.append(CommonConstants.CSV_HEADER_FROM_DATE).append(COMMA).append(form.getFromDate()).append(LINE_BREAK);
		buffer.append(CommonConstants.CSV_HEADER_TO_DATE).append(COMMA).append(form.getToDate()).append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		// Alerts Statistics by users.
		buffer.append(CommonConstants.USERS).append(COMMA).append(CommonConstants.NO_OF_UNIQUE_USERS_SETTINGS_ALERTS)
				.append(COMMA).append(CommonConstants.AVERAGE_ALERTS_USERS).append(COMMA)
				.append(CommonConstants.TOTAL_ALERTS).append(COMMA).append(CommonConstants.URGENT_ALERTS).append(COMMA)
				.append(CommonConstants.NORMAL_ALERTS).append(COMMA).append(CommonConstants.NO_OF_DELETED_ALERTS)
				.append(LINE_BREAK);
		for (AlertsReportUserStatsVO vo : alertsReportData.getAlertUsersStatsList()) {
			buffer.append(vo.getUsers()).append(COMMA).append(vo.getNumberOfAlertUsers()).append(COMMA)
					.append(vo.getAverageNumberOfAlertsPerUser() != null ? vo.getAverageNumberOfAlertsPerUser() : 0)
					.append(COMMA).append(vo.getTotalAlertSetUp()).append(COMMA).append(vo.getUrgentAlerts())
					.append(COMMA).append(vo.getNormalAlerts()).append(COMMA).append(vo.getNumberOfDeletedAlerts())
					.append(LINE_BREAK);
		}
		buffer.append(LINE_BREAK);

		// Alerts Frequency Statistics
		buffer.append(CommonConstants.ALERT_FREQUENCIES).append(COMMA).append(CommonConstants.NUMBER_OF_ALERT_FREQUENCY)
				.append(LINE_BREAK);
		for (AlertsReportFreqVO vo : alertsReportData.getAlertFrequencyStatsList()) {
			buffer.append(vo.getFrequency()).append(COMMA)
					.append(vo.getNumberOfAlertsPerFrequency() != null ? vo.getNumberOfAlertsPerFrequency() : 0)
					.append(LINE_BREAK);
		}
		buffer.append(LINE_BREAK);

		// Number of Reminders distributed by due date
		buffer.append(CommonConstants.DISTRIBUTION_MONTH_SELECTED_BY_USERS).append(LINE_BREAK);
		buffer.append(CommonConstants.Q1_MONTH).append(COMMA).append(CommonConstants.NOTICE_DISTRIBUTION_BY_DUE_DATE)
				.append(COMMA).append(CommonConstants.Q2_MONTH).append(COMMA)
				.append(CommonConstants.NOTICE_DISTRIBUTION_BY_DUE_DATE).append(COMMA).append(CommonConstants.Q3_MONTH)
				.append(COMMA).append(CommonConstants.NOTICE_DISTRIBUTION_BY_DUE_DATE).append(COMMA)
				.append(CommonConstants.Q4_MONTH).append(COMMA).append(CommonConstants.NOTICE_DISTRIBUTION_BY_DUE_DATE)
				.append(LINE_BREAK);
		int itemCount = 0;
		for (AlertsReportDistributionVO vo : alertsReportData.getAlertMonthlyDistributionList()) {
			buffer.append(" " + vo.getMonth().substring(0, 3)).append(" " + vo.getYear()).append(COMMA)
					.append(vo.getNumberOfAlerts());
			itemCount++;

			if (itemCount % 4 == 0) {
				buffer.append(LINE_BREAK);
			} else {
				buffer.append(COMMA);
			}
		}

		form.setTask(null);
		return buffer.toString().getBytes();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm actionForm,
			HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		AlertsReportForm alertForm = (AlertsReportForm) actionForm;

		// Add filters to the report criteria
		if (alertForm != null) {

			String contractNumber = alertForm.getContractNumber();
			if (contractNumber != null && !contractNumber.trim().equals("")) {

				try {
					criteria.addFilter(AlertsReportData.FILTER_CONTRACT_NUMBER, new Integer(contractNumber.trim()));
				} catch (NumberFormatException e) {
					List errors = new ArrayList();
					errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
					setErrorsInRequest(request, errors);
					throw new SystemException("Exception occured while parsing contract number.");
				}
			}

			try {

				Date fromDate = fastDateFormat.parse(alertForm.getFromDate());
				criteria.addFilter(AlertsReportData.FILTER_FROM_DATE, fromDate);

				Date toDate = fastDateFormat.parse(alertForm.getToDate());
				criteria.addFilter(AlertsReportData.FILTER_TO_DATE, toDate);

			} catch (ParseException e) {
				List errors = new ArrayList();
				errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
				setErrorsInRequest(request, errors);
				throw new SystemException("Exception occured while calculating Dates");
			}
		}

		String task = getTask(request);

		// If it is a download task then set the required filter variable
		if (DOWNLOAD_TASK.equals(task)) {
			criteria.addFilter(PlanDocumentHistoryReportData.FILTER_TASK, PlanDocumentHistoryReportData.TASK_DOWNLOAD);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}
	}

	/**
	 * Gets the current task for this request.
	 * 
	 * @param request
	 *            The current request object.
	 * @return The task for this request.
	 */
	protected String getTask(HttpServletRequest request) {
		String task = null;
		AlertsReportForm alertForm = (AlertsReportForm) request.getSession()
				.getAttribute(CommonConstants.ALERT_REPORT_ACTIONFORM);

		if (alertForm != null && alertForm.getTask() != null) {
			task = alertForm.getTask();
		} else {
			task = DEFAULT_TASK;
		}

		return task;
	}

	@Override
	protected String getReportId() {
		return AlertsReportHandler.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return AlertsReportData.REPORT_NAME;
	}

	/**
	 * Get the default sort order
	 */
	@Override
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/**
	 * Get the default sort direction
	 */
	@Override
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	private PieChartBean createDefaultPieChartBean() {
		PieChartBean pieChart = new PieChartBean();
		pieChart.setAppletArchive(CommonConstants.PIE_CHART_APPLET_ARCHIVE);
		pieChart.setPresentationModel(PieChartBean.PRESENTATION_MODEL_SERVLET);
		pieChart.setBorderColor(CommonConstants.COLOR_BORDER);
		pieChart.setShowWedgeLabels(true);
		pieChart.setUsePercentsAsWedgeLabels(true);
		pieChart.setPieStyle(PieChartBean.PIE_STYLE_FLAT);
		pieChart.setBorderWidth((float) 1.5);
		pieChart.setWedgeLabelOffset(75);
		pieChart.setFontSize(10);
		pieChart.setFontBold(true);
		pieChart.setDrawBorders(true);
		pieChart.setWedgeLabelExtrusion(35);
		pieChart.setWedgeLabelExtrusionThreshold(6);
		pieChart.setWedgeLabelExtrusionColor("#000000");
		return pieChart;
	}

	/**
	 * Gets Alert Frequency PieChartBean
	 * 
	 * @param reportData
	 * @return pieChart
	 */
	private PieChartBean getAlertFrequencyPieChartBean(AlertsReportData reportData) {
		PieChartBean pieChart = createDefaultPieChartBean();

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getAlertFrequencyPieChartBean");
		}

		List<AlertsReportFreqVO> freqList = reportData.getAlertFrequencyStatsList();
		boolean isPieChart = false;

		if (freqList.size() > 0) {

			String[] wedgesArr = { CommonConstants.WEDGE1, CommonConstants.WEDGE2, CommonConstants.WEDGE3,
					CommonConstants.WEDGE4, CommonConstants.WEDGE5 };
			int wedgeCount = 0;

			for (AlertsReportFreqVO vo : freqList) {

				if (vo.getNumberOfAlertsPerFrequency() != null && vo.getNumberOfAlertsPerFrequency() > 0) {

					pieChart.addPieWedge(wedgesArr[wedgeCount++], vo.getNumberOfAlertsPerFrequency(),
							getWedgeColor(vo.getFrequency()), CommonConstants.SINGLE_SPACE_SYMBOL,
							CommonConstants.VIEWING_PREFERENCE, CommonConstants.WEDGE_FONT_COLOR,
							CommonConstants.NUMBER_0);
					isPieChart = true;
				}
			}

			if (isPieChart) {

				pieChart.setAppletWidth(120);
				pieChart.setAppletHeight(120);

				pieChart.setPieWidth(110);
				pieChart.setPieHeight(110);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getAlertFrequencyPieChartBean");
		}

		return (isPieChart ? pieChart : null);

	}

	/**
	 * Gets Wedge Color
	 * 
	 * @param frequnecyName
	 * @return
	 */
	public static String getWedgeColor(String frequnecyName) {

		if (frequnecyName.equalsIgnoreCase(AlertsReportData.MONTHLY_FREQUENCY)) {
			return CommonConstants.MONTHLY_COLOR_WEDGE_LABEL;
		} else if (frequnecyName.equalsIgnoreCase(AlertsReportData.QUARTERLY_FREQUENCY)) {
			return CommonConstants.QUARTERLY_COLOR_WEDGE_LABEL;
		} else if (frequnecyName.equalsIgnoreCase(AlertsReportData.SEMI_ANNUALLY_FREQUENCY)) {
			return CommonConstants.SEMI_ANNUALLY_COLOR_WEDGE_LABEL;
		} else if (frequnecyName.equalsIgnoreCase(AlertsReportData.ANNUALLY_FREQUENCY)) {
			return CommonConstants.ANNUALLY_COLOR_WEDGE_LABEL;
		} else if (frequnecyName.equalsIgnoreCase(AlertsReportData.ADHOC_FREQUENCY)) {
			return CommonConstants.ADHOC_COLOR_WEDGE_LABEL;
		}

		return null;
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
