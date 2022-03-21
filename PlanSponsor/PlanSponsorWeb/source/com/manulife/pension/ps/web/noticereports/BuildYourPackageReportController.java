package com.manulife.pension.ps.web.noticereports;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.manulife.pension.ps.service.report.noticereports.reporthandler.BuildYourPackageReportHandler;
import com.manulife.pension.ps.service.report.noticereports.valueobject.BuildYourPackageReportData;
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
 * Action Class to get the report data for Build Your package tab to be
 * displayed on the notice manager reports page.
 * 
 * @author Akhil Khanna
 */

@Controller
@RequestMapping(value ="/noticereports")
@SessionAttributes({ "alertsReportForm" })

public class BuildYourPackageReportController extends ReportController {

	@ModelAttribute("alertsReportForm")
	public AlertsReportForm populateForm() {
		return new AlertsReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/noticereports/buildYourPackageReport.jsp");
		forwards.put("default", "/noticereports/buildYourPackageReport.jsp");
		forwards.put("search", "/noticereports/buildYourPackageReport.jsp");
		forwards.put("buildYourPackageReportPage", "/noticereports/buildYourPackageReport.jsp");
		forwards.put("print", "/noticereports/buildYourPackageReport.jsp");
	}

	private static final String DEFAULT_SORT_FIELD = PlanDocumentHistoryReportData.ACTION_DATE_FIELD;

	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;

	public static final String REPORT_NAME = "reportName";

	public static final String REPORT_TITLE = "Build Your Package as of";

	private FastDateFormat fastDateFormat = ContractDateHelper.getDateFormatterLocale("MM/dd/yyyy", Locale.US);

	private static final String PIE_CHART_APPLET_ARCHIVE = "/assets/unmanaged/applets/pieChartApplet.jar";

	private static final String WEDGE1 = "wedge1";

	private static final String WEDGE2 = "wedge2";

	private static final String WEDGE3 = "wedge3";

	private static final String WEDGE4 = "wedge4";

	public static final String WEDGE_FONT_COLOR = "#FFFFFF";

	public static final String INC_COLOR_WEDGE_LABEL = "#89A54E";

	public static final String TOTAL_CARE_COLOR_WEDGE_LABEL = "#71588F";

	public static final String TPA_COLOR_WEDGE_LABEL = "#AA4643";

	public static final String PS_COLOR_WEDGE_LABEL = "#3794AE";

	public static final String BUILD_YOUR_PACKAGE_FREQUENCY_PIECHART = "pieChart";

	public static final String SINGLE_SPACE_SYMBOL = " ";

	public static final String VIEWING_PREFERENCE = "1";

	public static final int NUMBER_0 = 0;

	public static final String COLOR_BORDER = "#EAEAEA";

	public static final String PLAN_SPONSOR = "PS";

	public static final String INTERMEDIARY_CONTACT = "Intermediary Contact";

	public static final String TOTAL_CARE = "Totalcare TPA";

	public static final String TPA = "TPA";

	public static final String NOT_AVAILABLE = "N/A";

	public static final String DOLLAR = "$";

	public static final String INVALID_DATE = "Invalid Date";

	public static final String BLANK = "";

	/**
	 * Method to get the report data after validation.
	 * 
	 * @param mapping
	 * @param request
	 * @param reportForm
	 * @param response
	 * @return ActionForward
	 */

	public String doCommon(BaseReportForm form,  HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		
		super.doCommon(form, request, response);

		BuildYourPackageReportData reportData = (BuildYourPackageReportData) request
				.getAttribute(CommonConstants.REPORT_BEAN);

		PieChartBean buildYourPackageFrequencyPieChartBean = getBuildYourPackageReportFrequencyPieChartBean(reportData);

		request.setAttribute(BUILD_YOUR_PACKAGE_FREQUENCY_PIECHART, buildYourPackageFrequencyPieChartBean);

		return forwards.get("buildYourPackageReportPage");
	}

	/**
	 * Method to validate the form fields
	 * 
	 * @param request
	 * @param mapping
	 * @param form
	 * @return errors
	 */

	@SuppressWarnings("unchecked")
	public Collection doValidate(HttpServletRequest request, AlertsReportForm form) {

		Collection errors = super.doValidate(form, request);

		// from date validation
		Date fromDate = null;
		Date toDate = null;
		try {
			if (StringUtils.isNotBlank(form.getFromDate())) {
				fromDate = fastDateFormat.parse(form.getFromDate());
				if (!fastDateFormat.format(fromDate).equals(form.getFromDate())) {
					throw new ParseException(INVALID_DATE, 0);
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
					throw new ParseException(INVALID_DATE, 0);
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
	@RequestMapping(value ="/buildYourPackageReport/",params = { "task=search" }, method = { RequestMethod.POST})
	public String doSearch(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			BuildYourPackageReportData report = new BuildYourPackageReportData();
			report.setReportCriteria(new ReportCriteria(BuildYourPackageReportHandler.REPORT_ID));
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
			BuildYourPackageReportData report = new BuildYourPackageReportData();
			report.setReportCriteria(new ReportCriteria(BuildYourPackageReportHandler.REPORT_ID));
			request.setAttribute(Constants.REPORT_BEAN, report);
			return forwards.get("input");
		}
		String forward = doCommon(form, request, response);
		return forward;
	}

	/**
	 * Action Method for reset the search criteria.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	@RequestMapping(value ="/buildYourPackageReport/",params = {"task=reset" }, method = {RequestMethod.POST})
	public String doReset(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			BuildYourPackageReportData report = new BuildYourPackageReportData();
			report.setReportCriteria(new ReportCriteria(BuildYourPackageReportHandler.REPORT_ID));
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
		form.setContractNumber(BLANK);
		doCommon(form, request, response);
		return forwards.get("default");

	}

	@RequestMapping(value ="/buildYourPackageReport/", method = {RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			BuildYourPackageReportData report = new BuildYourPackageReportData();
			report.setReportCriteria(new ReportCriteria(BuildYourPackageReportHandler.REPORT_ID));
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

	@RequestMapping(value ="/buildYourPackageReport/", params = {"task=filter" }, method = {RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/buildYourPackageReport/", params = {"task=page" }, method = {RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/buildYourPackageReport/", params ={"task=sort" }, method = {RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/buildYourPackageReport/", params = {"task=download" }, method = { RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
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
		BuildYourPackageReportData buildYourPackageReportData = new BuildYourPackageReportData();
		return getReportName() + SINGLE_SPACE_SYMBOL + buildYourPackageReportData.getTodayDate() + CSV_EXTENSION;
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
		BuildYourPackageReportData buildYourPackageReportData = (BuildYourPackageReportData) report;
		StringBuffer buffer = new StringBuffer();

		buffer.append("\"").append(REPORT_TITLE).append(SINGLE_SPACE_SYMBOL)
				.append(buildYourPackageReportData.getTodayDate()).append("\"").append(LINE_BREAK);
		buffer.append("Contract Number").append(COMMA).append(StringUtils.trimToEmpty(form.getContractNumber()))
				.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append("From Date").append(COMMA).append(form.getFromDate()).append(LINE_BREAK);
		buffer.append("To Date").append(COMMA).append(form.getToDate()).append(LINE_BREAK).append(LINE_BREAK);

		buffer.append("User Statistics").append(LINE_BREAK);

		buffer.append("Number of unique contracts using both Download and / or mail service").append(COMMA)
				.append(buildYourPackageReportData.getNumberOfContractsUsingMailAndDownload()).append(COMMA);

		buffer.append("Contract Status – New Business").append(COMMA);
		if (BigDecimal.ZERO.compareTo(buildYourPackageReportData.getNewBusinessContractsPercentage()) == 0) {
			buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
		} else {
			buffer.append(buildYourPackageReportData.getNewBusinessContractsPercentage()).append(Constants.PERCENT)
					.append(LINE_BREAK);
		}

		buffer.append("Number of repeat contracts using both Download and / or mail service").append(COMMA)
				.append(buildYourPackageReportData.getNumberOfRepeatContracts()).append(COMMA);

		buffer.append("Contract Status – Inforce Business").append(COMMA);

		if (BigDecimal.ZERO.compareTo(buildYourPackageReportData.getInforceContractsPercentage()) == 0) {
			buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
		} else {
			buffer.append(buildYourPackageReportData.getInforceContractsPercentage()).append(Constants.PERCENT)
					.append(LINE_BREAK);
		}
		buffer.append("Average no. of mailings / contract").append(COMMA)
				.append(buildYourPackageReportData.getAverageNumberOfMailingsPerContract()).append(COMMA);

		buffer.append("User Preference – Mail").append(COMMA);

		if (BigDecimal.ZERO.compareTo(buildYourPackageReportData.getUserPreferenceMailPercentage()) == 0) {
			buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
		} else {
			buffer.append(buildYourPackageReportData.getUserPreferenceMailPercentage()).append(Constants.PERCENT)
					.append(LINE_BREAK);
		}

		buffer.append("Month(s) with most mailings").append(COMMA);
		if ((buildYourPackageReportData.getMonthsWithMostMailings() == null)
				|| (buildYourPackageReportData.getMonthsWithMostMailings() != null
						&& buildYourPackageReportData.getMonthsWithMostMailings().isEmpty())) {
			buffer.append(NOT_AVAILABLE);
		} else {

			buffer.append(SINGLE_SPACE_SYMBOL + buildYourPackageReportData.getMonthsWithMostMailings().toString()
					.substring(1, buildYourPackageReportData.getMonthsWithMostMailings().toString().length() - 1));

		}
		buffer.append(COMMA).append("User Preference – Download").append(COMMA);

		if (BigDecimal.ZERO.compareTo(buildYourPackageReportData.getUserPreferenceDownloadPercentage()) == 0) {
			buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
		} else {
			buffer.append(buildYourPackageReportData.getUserPreferenceDownloadPercentage()).append(Constants.PERCENT)
					.append(LINE_BREAK);
		}

		if (!(buildYourPackageReportData.getTotalCompletedOrders()).equals(0)) {
			buffer.append("Order Information").append(LINE_BREAK);

			buffer.append("No. of completed orders").append(COMMA)
					.append(buildYourPackageReportData.getTotalCompletedOrders()).append(LINE_BREAK);
			buffer.append("Orders in black & white").append(COMMA);
			if (BigDecimal.ZERO.compareTo(buildYourPackageReportData.getBlackWhiteOrdersPercentage()) == 0) {
				buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
			} else {
				buffer.append(buildYourPackageReportData.getBlackWhiteOrdersPercentage()).append(Constants.PERCENT)
						.append(LINE_BREAK);
			}

			buffer.append("Orders stapled label").append(COMMA);
			if (BigDecimal.ZERO.compareTo(buildYourPackageReportData.getOrderStapledPercentage()) == 0) {
				buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
			} else {
				buffer.append(buildYourPackageReportData.getOrderStapledPercentage()).append(Constants.PERCENT)
						.append(LINE_BREAK);
			}

			buffer.append("Orders using booklet envelopes").append(COMMA);
			if (BigDecimal.ZERO.compareTo(buildYourPackageReportData.getBookletOrdersPercentage()) == 0) {
				buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
			} else {
				buffer.append(buildYourPackageReportData.getBookletOrdersPercentage()).append(Constants.PERCENT)
						.append(LINE_BREAK);
			}

			buffer.append("No. of  postage orders").append(COMMA);
			if ((buildYourPackageReportData.getNumberOfPostageOrders()).equals(0)
					|| (buildYourPackageReportData.getNumberOfPostageOrders()).equals(null)) {
				buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
			} else {
				buffer.append(buildYourPackageReportData.getNumberOfPostageOrders()).append(LINE_BREAK);
			}

			buffer.append("No. of  bulk orders").append(COMMA);
			if ((buildYourPackageReportData.getNumberOfBulkOrders()).equals(0)
					|| (buildYourPackageReportData.getNumberOfBulkOrders()).equals(null)) {
				buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
			} else {
				buffer.append(buildYourPackageReportData.getNumberOfBulkOrders()).append(LINE_BREAK);
			}

			buffer.append("Bulk orders sealed").append(COMMA);
			if (BigDecimal.ZERO.compareTo(buildYourPackageReportData.getSealedOrdersPercentage()) == 0) {
				buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
			} else {
				buffer.append(buildYourPackageReportData.getSealedOrdersPercentage()).append(Constants.PERCENT)
						.append(LINE_BREAK);
			}

			buffer.append("Average no. of pages/order").append(COMMA);
			if (BigDecimal.ZERO.compareTo(buildYourPackageReportData.getAverageNumberOfPagesPerOrder()) == 0) {
				buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
			} else {
				buffer.append(buildYourPackageReportData.getAverageNumberOfPagesPerOrder()).append(LINE_BREAK);
			}

			buffer.append("Average no. of participants/order").append(COMMA);
			if (BigDecimal.ZERO.compareTo(buildYourPackageReportData.getAverageNumberOfPartipicantsPerOrder()) == 0) {
				buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
			} else {
				buffer.append(buildYourPackageReportData.getAverageNumberOfPartipicantsPerOrder()).append(LINE_BREAK);
			}

			buffer.append("Total no. of participants").append(COMMA);
			if ((buildYourPackageReportData.getParticpantCount()).equals(0)
					|| (buildYourPackageReportData.getParticpantCount()).equals(null)) {
				buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
			} else {
				buffer.append(buildYourPackageReportData.getParticpantCount()).append(LINE_BREAK);
			}

			buffer.append("Average cost/order").append(COMMA);
			if (BigDecimal.ZERO.compareTo(buildYourPackageReportData.getAverageCostPerOrder()) == 0) {
				buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
			} else {
				buffer.append(DOLLAR).append(buildYourPackageReportData.getAverageCostPerOrder()).append(LINE_BREAK);
			}

			buffer.append("Total cost").append(COMMA);
			if (BigDecimal.ZERO.compareTo(buildYourPackageReportData.getTotalOrderCosts()) == 0) {
				buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
			} else {
				buffer.append(DOLLAR).append(buildYourPackageReportData.getTotalOrderCosts()).append(LINE_BREAK);
			}

			buffer.append("Amount paid by John Hancock").append(COMMA);
			if (BigDecimal.ZERO.compareTo(buildYourPackageReportData.getAmountPaidByJohnHancock()) == 0) {
				buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
			} else {
				buffer.append(DOLLAR).append(buildYourPackageReportData.getAmountPaidByJohnHancock())
						.append(LINE_BREAK);
			}

			buffer.append("Percentage of 'Paid by John Hancock' for TotalCare").append(COMMA);
			if (BigDecimal.ZERO
					.compareTo(buildYourPackageReportData.getAmountPaidByJohnHancockTotalCarePercentage()) == 0) {
				buffer.append(NOT_AVAILABLE).append(LINE_BREAK);
			} else {
				buffer.append(buildYourPackageReportData.getAmountPaidByJohnHancockTotalCarePercentage())
						.append(LINE_BREAK);
			}
		}
		form.setTask(null);

		return buffer.toString().getBytes();

	}

	/**
	 * Method to populate the report criteria
	 * 
	 * @param criteria
	 * @param actionForm
	 * @param request
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm actionForm,
			HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		AlertsReportForm alertForm = (AlertsReportForm) actionForm;

		// Add filters to the report criteria
		if (alertForm != null) {

			String contractNumber = alertForm.getContractNumber();
			if (contractNumber != null && !contractNumber.trim().equals(BLANK)) {

				try {
					criteria.addFilter(BuildYourPackageReportData.FILTER_CONTRACT_NUMBER,
							new Integer(contractNumber.trim()));
				} catch (NumberFormatException e) {
					List errors = new ArrayList();
					errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
					setErrorsInRequest(request, errors);
					throw new SystemException("Exception occured while parsing contract number.");
				}
			}

			try {

				Date fromDate = fastDateFormat.parse(alertForm.getFromDate());
				criteria.addFilter(BuildYourPackageReportData.FILTER_FROM_DATE, fromDate);

				Date toDate = fastDateFormat.parse(alertForm.getToDate());
				criteria.addFilter(BuildYourPackageReportData.FILTER_TO_DATE, toDate);

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
				.getAttribute("alertsReportForm");

		if (alertForm != null && alertForm.getTask() != null) {
			task = alertForm.getTask();
		} else {
			task = DEFAULT_TASK;
		}
		return task;
	}

	/**
	 * Get the Report Id
	 */
	@Override
	protected String getReportId() {
		return BuildYourPackageReportHandler.REPORT_ID;
	}

	/**
	 * Get the Report Name
	 */
	@Override
	protected String getReportName() {
		return BuildYourPackageReportData.REPORT_NAME;
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

	/**
	 * Method to create the pie chart properties
	 */
	private PieChartBean createDefaultPieChartBean() {
		PieChartBean pieChart = new PieChartBean();
		pieChart.setAppletArchive(PIE_CHART_APPLET_ARCHIVE);
		pieChart.setPresentationModel(PieChartBean.PRESENTATION_MODEL_SERVLET);
		pieChart.setBorderColor(COLOR_BORDER);
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
	 * Method to get the pie chart filled with the values obtained from reportData
	 * 
	 * @param reportData
	 * @return pieChart
	 */
	private PieChartBean getBuildYourPackageReportFrequencyPieChartBean(BuildYourPackageReportData reportData) {
		PieChartBean pieChart = createDefaultPieChartBean();

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getBuldYourPackageReportFrequencyPieChartBean");
		}

		BigDecimal psOrderPct = new BigDecimal(0);
		BigDecimal tpaOrderPct = new BigDecimal(0);
		BigDecimal incOrderPct = new BigDecimal(0);
		BigDecimal totalCareOrderPct = new BigDecimal(0);

		psOrderPct = reportData.getOrdersByPlanSponsorsPercentage();
		tpaOrderPct = reportData.getOrdersByTPAPercentage();
		incOrderPct = reportData.getOrdersByIntermediaryContactPercentage();
		totalCareOrderPct = reportData.getOrdersByTotalCarePercentage();
		boolean isPieChart = false;

		String[] wedgesArr = { WEDGE1, WEDGE2, WEDGE3, WEDGE4 };
		int wedgeCount = 0;

		if (psOrderPct != null || tpaOrderPct != null || incOrderPct != null || totalCareOrderPct != null) {

			if (psOrderPct.compareTo(BigDecimal.ZERO) > 0) {
				pieChart.addPieWedge(wedgesArr[wedgeCount++], psOrderPct.floatValue(), getWedgeColor(PLAN_SPONSOR),
						SINGLE_SPACE_SYMBOL, VIEWING_PREFERENCE, WEDGE_FONT_COLOR, NUMBER_0);
				isPieChart = true;
			}
			if (tpaOrderPct.compareTo(BigDecimal.ZERO) > 0) {
				pieChart.addPieWedge(wedgesArr[wedgeCount++], tpaOrderPct.floatValue(), getWedgeColor(TPA),
						SINGLE_SPACE_SYMBOL, VIEWING_PREFERENCE, WEDGE_FONT_COLOR, NUMBER_0);
				isPieChart = true;
			}
			if (incOrderPct.compareTo(BigDecimal.ZERO) > 0) {
				pieChart.addPieWedge(wedgesArr[wedgeCount++], incOrderPct.floatValue(),
						getWedgeColor(INTERMEDIARY_CONTACT), SINGLE_SPACE_SYMBOL, VIEWING_PREFERENCE, WEDGE_FONT_COLOR,
						NUMBER_0);
				isPieChart = true;
			}
			if (totalCareOrderPct.compareTo(BigDecimal.ZERO) > 0) {
				pieChart.addPieWedge(wedgesArr[wedgeCount++], totalCareOrderPct.floatValue(), getWedgeColor(TOTAL_CARE),
						SINGLE_SPACE_SYMBOL, VIEWING_PREFERENCE, WEDGE_FONT_COLOR, NUMBER_0);
				isPieChart = true;
			}

		}

		if (isPieChart) {

			pieChart.setAppletWidth(100);
			pieChart.setAppletHeight(115);

			pieChart.setPieWidth(90);
			pieChart.setPieHeight(90);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getBuldYourPackageReportFrequencyPieChartBean");
		}

		return (isPieChart ? pieChart : null);

	}

	/**
	 * Method to get the Wedge color for the pie chart
	 * 
	 * @param frequencyName
	 * @return String
	 */
	private String getWedgeColor(String frequencyName) {

		if (frequencyName.equalsIgnoreCase(PLAN_SPONSOR)) {
			return PS_COLOR_WEDGE_LABEL;
		} else if (frequencyName.equalsIgnoreCase(TPA)) {
			return TPA_COLOR_WEDGE_LABEL;
		} else if (frequencyName.equalsIgnoreCase(INTERMEDIARY_CONTACT)) {
			return INC_COLOR_WEDGE_LABEL;
		} else if (frequencyName.equalsIgnoreCase(TOTAL_CARE)) {
			return TOTAL_CARE_COLOR_WEDGE_LABEL;
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