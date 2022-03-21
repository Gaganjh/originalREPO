package com.manulife.pension.ps.web.census;

import static com.manulife.pension.platform.web.CommonConstants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
import static com.manulife.pension.platform.web.CommonConstants.PS_APPLICATION_ID;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EligibilityServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.ps.service.report.census.valueobject.EligibilityReportData;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.DeferralUtils;
import com.manulife.pension.ps.web.census.util.FilterCriteriaVo;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractMoneyTypeEligibilityVO;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This class is action class for the Eligibility Change Report.
 * 
 * @author Ramamohan Gulla.
 *
 */
@Controller
@RequestMapping(value = "/census")
@SessionAttributes({"eligibilityReportsForm"})

public class EligibilityChangeReportController extends ReportController {
	@ModelAttribute("eligibilityReportsForm")
	public EligibilityReportsForm populateForm() {
		return new EligibilityReportsForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("default","/census/eligibilityChangeReport.jsp");
		forwards.put("staging","/census/eligibilityReports.jsp");
	}

	public static final String ELIGIBILITY_CHANGE_REPORT = "eligibilityChangeReport";
	public static final String REPORT_TYPE = "reportType";
	public static final String ELIGIBILITY_REPORT_NAME = "Download eligibility report";
	public static final String DATE_PATTERN = "MM/dd/yyyy";
	public static final String JOHN_HANCOCK_REPRESENTATIVE = "John Hancock Representative";
	public static final String EMPLOYEE = "Employee";
	public static final String PENDING_ENROLLMENT = "Pending Enrollment";
	public static final String EEDEF = "EEDEF";
	public static final String ELIGIBILITY_CHANGE_REPORT_NAME = "Eligibility_change_report";

	private static final String NO_RESULT_TEXT = "Curently there are no changes to display.";
	public static final FastDateFormat dateFormat = ContractDateHelper.getDateFormatter("MM/dd/yyyy");
	public static final FastDateFormat fullMonthFormat = ContractDateHelper.getDateFormatter("MMMM dd,yyyy");

	private static Map<String, String> SourceMap = new HashMap<String, String>();
	private static String PSDescription = "Website";
	private static String PADescription = "Participant Website";
	private static String LPDescription = "Administration";

	static {
		SourceMap.put("PS", PSDescription);
		SourceMap.put("PA", PADescription);
		SourceMap.put("LP", LPDescription);
	}

	private static final String className = EligibilityChangeReportController.class.getName();

	@Override
	protected String getDefaultSort() {
		return EligibilityReportData.DEFAULT_SORT;
	}

	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		byte[] bytes = null;

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getDownloadData");
		}

		// Identify the type of report
		if (ELIGIBILITY_CHANGE_REPORT.equalsIgnoreCase(request.getParameter(REPORT_TYPE))) {
			bytes = getEligibilityChangeDownloadData(reportForm, report, request);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getDownloadData");
		}

		return bytes;
	}

	@Override
	protected String getReportId() {
		return EligibilityReportData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return ELIGIBILITY_REPORT_NAME;
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		EligibilityReportsForm psform = (EligibilityReportsForm) form;

		criteria.addFilter(EligibilityReportData.FILTER_CONTRACT_NUMBER,
				Integer.toString(currentContract.getContractNumber()));

		criteria.addFilter(EligibilityReportData.FILTER_REPORT_TYPE, ELIGIBILITY_CHANGE_REPORT);
		criteria.addFilter(EligibilityReportData.IS_EC_ON, psform.isEligibiltyCalcOn());
		criteria.addFilter(EligibilityReportData.FILTER_AUTO_ENROLL_ON, psform.isEZstartOn());
		String moneyTypesString = "";

		String ped = psform.getReportedPED();
		Map<String, List<LabelValueBean>> moneyTypesMap = psform.getMoneyTypePEDMap();

		// Set the selected money types to the Filter Criteria.

		moneyTypesString = psform.getSelectedMoneyType();

		criteria.addFilter(EligibilityReportData.FILTER_MONEY_TYPES, moneyTypesString.trim());

		String moneyTypesStringArray = "";

		if ("All".equalsIgnoreCase(moneyTypesString)) {
			List<LabelValueBean> moneyTypes = moneyTypesMap.get(ped);

			int ind = 1;

			for (LabelValueBean moneyType : moneyTypes) {

				if (ind == 1) {
					moneyTypesStringArray = moneyTypesStringArray + " '" + moneyType.getValue() + "' ";
				} else {
					moneyTypesStringArray = moneyTypesStringArray + " , '" + moneyType.getValue() + "' ";
				}
				ind++;
			}

		} else {
			moneyTypesStringArray = " '" + moneyTypesString.trim() + "'";
		}

		criteria.addFilter(EligibilityReportData.FILTER_MONEY_TYPE_STRING, moneyTypesStringArray);

		// Set the selected Plan Entry Date to the Filter Criteria.
		criteria.addFilter(EligibilityReportData.REPORTED_PLAN_ENTRY_DATE, psform.getReportedPED());
		criteria.addFilter(EligibilityReportData.CONTRACT_DM, new Boolean(psform.isDMContract()));

		// set the Optout days to the filter criteria.

		criteria.addFilter(EligibilityReportData.OPTOUT_DAYS, Integer.toString(psform.getOptOutDays()));
		criteria.addFilter(EligibilityReportData.IS_IED, new Boolean(psform.isHasIED()));
		criteria.addFilter(EligibilityReportData.INITIAL_ENROLLMENT_DATE, psform.getInitialEnrollmentDate());

		// Get the filterCriteriaVo from session
		FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);

		if (filterCriteriaVo == null) {
			filterCriteriaVo = new FilterCriteriaVo();
		}

		// set filterCriteriaVo back to session
		SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);

		// if external user, don't display Canceled employees
		criteria.setExternalUser(userProfile.getRole().isExternalUser());

		if (logger.isDebugEnabled()) {
			logger.debug("criteria= " + criteria);
			logger.debug("exit <- populateReportCriteria");
		}

	}

	protected byte[] getEligibilityChangeDownloadData(BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getOptOutDownloadData");
		}

		EligibilityReportData summaryReport = (EligibilityReportData) request.getAttribute(Constants.REPORT_BEAN);
		EligibilityReportsForm form = (EligibilityReportsForm) reportForm;
		StringBuffer buffer = new StringBuffer();

		// If there are no changes, Then display static text in Eligibility Changes
		// Report
		if (summaryReport.getDetails().size() == 0) {
			buffer.append(NO_RESULT_TEXT);
			return buffer.toString().getBytes();
		}

		Contract currentContract = getUserProfile(request).getCurrentContract();

		UserProfile user = getUserProfile(request);

		if (summaryReport.getDetails().size() == 0) {
			buffer.append("Currently there are no changes to display");
			return buffer.toString().getBytes();
		}
		// Title
		buffer.append("Eligibility Change Report").append(LINE_BREAK);
		// Contract #, Contract name
		buffer.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(COMMA);

		String companyName = currentContract.getCompanyName() != null ? currentContract.getCompanyName() : "";
		companyName = "\"" + companyName + "\"";
		buffer.append(companyName).append(LINE_BREAK);
		buffer.append("Actual Date of Download").append(COMMA)
				.append(DateRender.formatByPattern(new Date(), "", DATE_PATTERN)).append(LINE_BREAK);

		GregorianCalendar reportedPED = new GregorianCalendar();
		try {
			reportedPED.setTime(fullMonthFormat.parse(form.getReportedPED()));
		} catch (ParseException pe) {
			// No parse exception would occur here.
		}
		Date rped = reportedPED.getTime();
		reportedPED.add(Calendar.DAY_OF_YEAR, -form.getOptOutDays());

		Date optoutDeadline = reportedPED.getTime();
		String optOutDate = dateFormat.format(optoutDeadline);
		String reportPlanEntryDate = dateFormat.format(rped);

		buffer.append("Changes for Plan Entry Date").append(COMMA).append(reportPlanEntryDate).append(LINE_BREAK);
		if (form.isEZstartOn()) {
			buffer.append("Opt-out Deadline").append(COMMA).append(optOutDate).append(LINE_BREAK);
		}

		String moneyTypeShortName = "All".equalsIgnoreCase(form.getSelectedMoneyType()) ? "All"
				: form.getMoneyTypeIdName().get(form.getSelectedMoneyType());

		buffer.append("Applicable Money Type").append(COMMA).append(moneyTypeShortName).append(LINE_BREAK);

		if (form.isEZstartOn() && form.isEligibiltyCalcOn()) {
			this.appendContentMessage(ContentConstants.ELIGIBILITY_CHANGE_REPORT_DISCLAIMER_EC_AE, buffer);

			buffer.append(LINE_BREAK);
			this.appendContentMessage(ContentConstants.ELIGIBILITY_CHANGE_REPORT_CHANGE_FIELDS_LINE_EC_AE, buffer);
			this.appendContentMessage(ContentConstants.ELIGIBILITY_CHANGE_REPORT_NEW_EMPLOYEES_LINE_EC_AE, buffer);

			buffer.append(LINE_BREAK).append(LINE_BREAK);

		} else if (!form.isEZstartOn() && form.isEligibiltyCalcOn()) {
			this.appendContentMessage(ContentConstants.ELIGIBILITY_CHANGE_REPORT_DISCLAIMER_EC, buffer);

			buffer.append(LINE_BREAK);
			this.appendContentMessage(ContentConstants.ELIGIBILITY_CHANGE_REPORT_CHANGE_FIELDS_LINE_EC, buffer);
			this.appendContentMessage(ContentConstants.ELIGIBILITY_CHANGE_REPORT_NEW_EMPLOYEES_LINE_EC, buffer);

			buffer.append(LINE_BREAK);
		} else if (form.isEZstartOn() && !form.isEligibiltyCalcOn()) {
			this.appendContentMessage(ContentConstants.ELIGIBILITY_CHANGE_REPORT_DISCLAIMER_AE, buffer);

			buffer.append(LINE_BREAK);
			this.appendContentMessage(ContentConstants.ELIGIBILITY_CHANGE_REPORT_CHANGE_FIELDS_LINE_AE, buffer);
			this.appendContentMessage(ContentConstants.ELIGIBILITY_CHANGE_REPORT_NEW_EMPLOYEES_LINE_AE, buffer);
			buffer.append(LINE_BREAK).append(LINE_BREAK);
		}

		// Column headings
		buffer.append("Employee Last Name").append(COMMA).append("Employee First Name").append(COMMA)
				.append("Employee Middle Initial").append(COMMA).append("SSN").append(COMMA).append("Date of Birth")
				.append(COMMA).append("Hire Date").append(COMMA);

		if (form.isHasPayrollNumberFeature()) {
			buffer.append("Employee ID").append(COMMA);
		}
		if (form.isHasDivisionFeature()) {
			buffer.append("Division").append(COMMA);
		}

		buffer.append("Enrollment Status").append(COMMA).append("Enrollment Method").append(COMMA)
				.append("Enrollment Processing Date").append(COMMA).append("Eligible to Participate").append(COMMA);

		if ("All".equalsIgnoreCase(form.getSelectedMoneyType())) {

			Map<String, List<LabelValueBean>> moneyTypeMap = form.getMoneyTypePEDMap();
			List<LabelValueBean> moneyTypes = moneyTypeMap.get(form.getReportedPED());
			for (LabelValueBean moneyType : moneyTypes) {
				buffer.append("New Eligibility Date - " + moneyType.getLabel()).append(COMMA);
				buffer.append("New Plan Entry Date - " + moneyType.getLabel()).append(COMMA);
				buffer.append("Calculation Override - " + moneyType.getLabel()).append(COMMA);
			}

		} else {
			buffer.append("New Eligibility Date - " + moneyTypeShortName).append(COMMA);
			buffer.append("New Plan Entry Date - " + moneyTypeShortName).append(COMMA);
			buffer.append("Calculation Override - " + moneyTypeShortName).append(COMMA);
		}

		if (form.isEZstartOn()) {
			buffer.append("Opt-out Indicator").append(COMMA);
		}

		if (currentContract.hasRoth()) {
			buffer.append("Before Tax Deferral (%) at Enrollment").append(COMMA)
					.append("Before Tax Flat ($) Deferral at Enrollment").append(COMMA)
					.append("Designated Roth Deferral (%) at Enrollment").append(COMMA)
					.append("Designated Roth Flat ($) Deferral at Enrollment").append(COMMA)
					.append("Before Tax Deferral (%)").append(COMMA).append("Before Tax Flat ($) Deferral)")
					.append(COMMA).append("Designated Roth Deferral (%)").append(COMMA)
					.append("Designated Roth Flat ($) Deferral)").append(COMMA);
		} else {
			buffer.append("Before Tax Deferral (%) at Enrollment").append(COMMA)
					.append("Before Tax Flat ($) Deferral) at Enrollment").append(COMMA)
					.append("Before Tax Deferral (%)").append(COMMA).append("Before Tax Flat ($) Deferral)")
					.append(COMMA);
		}

		buffer.append(LINE_BREAK);
		// SSE S024 determine wheather the ssn should be masked on the csv report
		boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
		try {
			maskSsnFlag = ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber());

		} catch (SystemException se) {
			logger.error(se);
			// log exception and output blank ssn
		}

		// loop through details
		Iterator iterator = summaryReport.getDetails().iterator();
		Map<String, HashMap<String, EmployeeChangeHistoryVO>> map = summaryReport.getEligiblilityChangeData();
		Map<String, String> overrideIndMap = summaryReport.getEligOverrideMap();
		Map<String, String> eligibilityDatesMap = summaryReport.getEligDatesMap();
		Map<String, String> planEntryDatesMap = summaryReport.getEligPEDMap();
		Employee employee = null;
		while (iterator.hasNext()) {
			EmployeeSummaryDetails theItem = (EmployeeSummaryDetails) iterator.next();
			employee = new Employee(false);
			List<String> columnNames = buildListOfColumnNames(theItem.getStatusChanges());

			buffer.append(LINE_BREAK);
			if (form.isEZstartOn() || form.isEligibiltyCalcOn()) {
				appendFieldChangedIndicator(EligibilityReportData.NEW_EMPLOYEE_COLUMN, columnNames, buffer, employee);
			}

			buffer.append(processString(theItem.getLastName())).append(COMMA);
			buffer.append(processString(theItem.getFirstName())).append(COMMA);
			buffer.append(processString(theItem.getMiddleInitial())).append(COMMA);
			buffer.append(SSNRender.format(theItem.getSsn(), null, maskSsnFlag)).append(COMMA);
			buffer.append(theItem.getBirthDate() == null ? ""
					: DateRender.formatByPattern(theItem.getBirthDate(), "", RenderConstants.MEDIUM_MDY_SLASHED))
					.append(COMMA);
			buffer.append(theItem.getHireDate() == null ? ""
					: DateRender.formatByPattern(theItem.getHireDate(), "", RenderConstants.MEDIUM_MDY_SLASHED))
					.append(COMMA);

			if (form.isHasPayrollNumberFeature()) {
				buffer.append(processString(theItem.getEmployerDesignatedID()));
				buffer.append(COMMA);
			}

			if (form.isHasDivisionFeature()) {
				buffer.append(processString(theItem.getDivision()));
				buffer.append(COMMA);
			}

			buffer.append(processString(theItem.getEnrollmentStatus()));
			if (form.isEZstartOn()) {
				if (isChangedWithinOptOutPeriod(currentContract.getContractNumber(),
						Long.valueOf(theItem.getProfileId()), "EMPLOYEE_CONTRACT", "EMPLOYMENT_STATUS_CODE", rped,
						form.getOptOutDays(), employee)) {
					appendFieldChangedIndicator(EligibilityReportData.EMPLOYMENT_STATUS_CODE, columnNames, buffer,
							employee);
				}
			}

			buffer.append(COMMA);

			buffer.append(processString(theItem.getEnrollmentMethod())).append(COMMA);
			buffer.append(DateRender.formatByPattern(theItem.getEnrollmentProcessedDate(), "",
					RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

			if (theItem.getEligibleToEnroll() != null) {
				buffer.append(processString(theItem.getEligibleToEnroll()));
			}
			if (form.isEZstartOn()) {
				if (isChangedWithinOptOutPeriod(currentContract.getContractNumber(),
						Long.valueOf(theItem.getProfileId()), "EMPLOYEE_CONTRACT", "PLAN_ELIGIBLE_IND", rped,
						form.getOptOutDays(), employee)) {
					appendFieldChangedIndicator(EligibilityReportData.ELIGIBLE_TO_ENROLL_COLUMN, columnNames, buffer,
							employee);
				}
			}

			buffer.append(COMMA);

			// Add Eligibility Date, PED, Override indicator for the MoneyTypes

			Map<String, List<LabelValueBean>> moneyTypeMap = form.getMoneyTypePEDMap();
			List<LabelValueBean> moneyTypes = moneyTypeMap.get(form.getReportedPED());

			if ("All".equalsIgnoreCase(form.getSelectedMoneyType())) {

				for (LabelValueBean moneyType : moneyTypes) {

					Map<String, EmployeeChangeHistoryVO> changeHistory = map.get(theItem.getProfileId());

					if (changeHistory.get(moneyType.getValue() + "_ELIGIBILITY_DATE") != null
							|| eligibilityDatesMap.get(theItem.getProfileId() + moneyType.getValue()) != null) {

						EmployeeChangeHistoryVO vo = changeHistory.get(moneyType.getValue() + "_ELIGIBILITY_DATE");
						String eligibilityDate = eligibilityDatesMap.get(theItem.getProfileId() + moneyType.getValue());
						buffer.append((vo == null || vo.getCurrentValue() == null)
								? (eligibilityDate.length() > 0 ? eligibilityDate : "")
								: getDateInMMddYYYYFormat(vo.getCurrentValue()));
						if (vo != null) {

							if (form.isEZstartOn() && !form.isEligibiltyCalcOn()) {
								if (isChangedWithinOptOutPeriod(currentContract.getContractNumber(),
										Long.valueOf(theItem.getProfileId()), "EMPLOYEE_PLAN_ENTRY_ELIGIBILITY",
										moneyType.getValue() + "_ELIGIBILITY_DATE", rped, form.getOptOutDays(),
										employee)) {
									appendFieldChangedIndicator(moneyType.getValue() + "_ELIGIBILITY_DATE", columnNames,
											buffer, employee);
								}
							} else if (form.isEZstartOn() && form.isEligibiltyCalcOn()) {
								if (isChangedWithinOptOutPeriod(currentContract.getContractNumber(),
										Long.valueOf(theItem.getProfileId()), "EMPLOYEE_PLAN_ENTRY_ELIGIBILITY",
										moneyType.getValue() + "_ELIGIBILITY_DATE", rped, form.getOptOutDays(),
										employee)
										|| isChangedWithinNotificationPeriod(currentContract.getContractNumber(),
												Long.valueOf(theItem.getProfileId()), "EMPLOYEE_PLAN_ENTRY_ELIGIBILITY",
												moneyType.getValue() + "_ELIGIBILITY_DATE",
												summaryReport.getNotificationPeriodStartDate(),
												summaryReport.getNotificationPeriodEndDate(), employee)) {
									appendFieldChangedIndicator(moneyType.getValue() + "_ELIGIBILITY_DATE", columnNames,
											buffer, employee);
								}
							} else if (form.isEligibiltyCalcOn() && isChangedWithinNotificationPeriod(
									currentContract.getContractNumber(), Long.valueOf(theItem.getProfileId()),
									"EMPLOYEE_PLAN_ENTRY_ELIGIBILITY", moneyType.getValue() + "_ELIGIBILITY_DATE",
									summaryReport.getNotificationPeriodStartDate(),
									summaryReport.getNotificationPeriodEndDate(), employee)) {
								appendFieldChangedIndicator(moneyType.getValue() + "_ELIGIBILITY_DATE", columnNames,
										buffer, employee);
							}
						}
						buffer.append(COMMA);

					} else {
						buffer.append(COMMA);
					}

					if (changeHistory.get(moneyType.getValue() + "_ELIGIBLE_PLAN_ENTRY_DATE") != null
							|| planEntryDatesMap.get(theItem.getProfileId() + moneyType.getValue()) != null) {

						EmployeeChangeHistoryVO vo = changeHistory
								.get(moneyType.getValue() + "_ELIGIBLE_PLAN_ENTRY_DATE");
						String planEntryDate = planEntryDatesMap.get(theItem.getProfileId() + moneyType.getValue());
						buffer.append((vo == null || vo.getCurrentValue() == null)
								? (planEntryDate.length() > 0 ? planEntryDate : "")
								: getDateInMMddYYYYFormat(vo.getCurrentValue()));

						if (vo != null) {

							if (form.isEZstartOn() && !form.isEligibiltyCalcOn()) {
								if (isChangedWithinOptOutPeriod(currentContract.getContractNumber(),
										Long.valueOf(theItem.getProfileId()), "EMPLOYEE_PLAN_ENTRY_ELIGIBILITY",
										moneyType.getValue() + "_ELIGIBLE_PLAN_ENTRY_DATE", rped, form.getOptOutDays(),
										employee)) {
									appendFieldChangedIndicator(moneyType.getValue() + "_ELIGIBLE_PLAN_ENTRY_DATE",
											columnNames, buffer, employee);
								}
							} else if (form.isEZstartOn() && form.isEligibiltyCalcOn()) {
								if (isChangedWithinOptOutPeriod(currentContract.getContractNumber(),
										Long.valueOf(theItem.getProfileId()), "EMPLOYEE_PLAN_ENTRY_ELIGIBILITY",
										moneyType.getValue() + "_ELIGIBLE_PLAN_ENTRY_DATE", rped, form.getOptOutDays(),
										employee)
										|| isChangedWithinNotificationPeriod(currentContract.getContractNumber(),
												Long.valueOf(theItem.getProfileId()), "EMPLOYEE_PLAN_ENTRY_ELIGIBILITY",
												moneyType.getValue() + "_ELIGIBLE_PLAN_ENTRY_DATE",
												summaryReport.getNotificationPeriodStartDate(),
												summaryReport.getNotificationPeriodEndDate(), employee)) {
									appendFieldChangedIndicator(moneyType.getValue() + "_ELIGIBLE_PLAN_ENTRY_DATE",
											columnNames, buffer, employee);
								}
							} else if (form.isEligibiltyCalcOn()
									&& isChangedWithinNotificationPeriod(currentContract.getContractNumber(),
											Long.valueOf(theItem.getProfileId()), "EMPLOYEE_PLAN_ENTRY_ELIGIBILITY",
											moneyType.getValue() + "_ELIGIBLE_PLAN_ENTRY_DATE",
											summaryReport.getNotificationPeriodStartDate(),
											summaryReport.getNotificationPeriodEndDate(), employee)) {
								appendFieldChangedIndicator(moneyType.getValue() + "_ELIGIBLE_PLAN_ENTRY_DATE",
										columnNames, buffer, employee);
							}
						}

						buffer.append(COMMA);

					} else {
						buffer.append(COMMA);
					}

					if (overrideIndMap.get(theItem.getProfileId() + moneyType.getValue()) != null) {

						String eligOverrideInd = overrideIndMap.get(theItem.getProfileId() + moneyType.getValue());
						buffer.append(eligOverrideInd).append(COMMA);

					} else {

						buffer.append(COMMA);
					}
				}
			} else {

				Map<String, EmployeeChangeHistoryVO> changeHistory = map.get(theItem.getProfileId());

				String moneyType = form.getSelectedMoneyType();
				if (changeHistory.get(moneyType + "_ELIGIBILITY_DATE") != null
						|| eligibilityDatesMap.get(theItem.getProfileId() + moneyType) != null) {

					EmployeeChangeHistoryVO vo = changeHistory.get(moneyType + "_ELIGIBILITY_DATE");
					String eligibilityDate = eligibilityDatesMap.get(theItem.getProfileId() + moneyType);
					buffer.append((vo == null || vo.getCurrentValue() == null)
							? (eligibilityDate.length() > 0 ? eligibilityDate : "")
							: getDateInMMddYYYYFormat(vo.getCurrentValue()));
					if (vo != null) {
						if (form.isEZstartOn() && !form.isEligibiltyCalcOn()) {
							if (isChangedWithinOptOutPeriod(currentContract.getContractNumber(),
									Long.valueOf(theItem.getProfileId()), "EMPLOYEE_PLAN_ENTRY_ELIGIBILITY",
									moneyType + "_ELIGIBILITY_DATE", rped, form.getOptOutDays(), employee)) {
								appendFieldChangedIndicator(moneyType + "_ELIGIBILITY_DATE", columnNames, buffer,
										employee);
							}
						} else if (form.isEZstartOn() && form.isEligibiltyCalcOn()) {
							if (isChangedWithinOptOutPeriod(currentContract.getContractNumber(),
									Long.valueOf(theItem.getProfileId()), "EMPLOYEE_PLAN_ENTRY_ELIGIBILITY",
									moneyType + "_ELIGIBILITY_DATE", rped, form.getOptOutDays(), employee)
									|| isChangedWithinNotificationPeriod(currentContract.getContractNumber(),
											Long.valueOf(theItem.getProfileId()), "EMPLOYEE_PLAN_ENTRY_ELIGIBILITY",
											moneyType + "_ELIGIBILITY_DATE",
											summaryReport.getNotificationPeriodStartDate(),
											summaryReport.getNotificationPeriodEndDate(), employee)) {
								appendFieldChangedIndicator(moneyType + "_ELIGIBILITY_DATE", columnNames, buffer,
										employee);
							}
						} else if (form.isEligibiltyCalcOn()
								&& isChangedWithinNotificationPeriod(currentContract.getContractNumber(),
										Long.valueOf(theItem.getProfileId()), "EMPLOYEE_PLAN_ENTRY_ELIGIBILITY",
										moneyType + "_ELIGIBILITY_DATE", summaryReport.getNotificationPeriodStartDate(),
										summaryReport.getNotificationPeriodEndDate(), employee)) {
							appendFieldChangedIndicator(moneyType + "_ELIGIBILITY_DATE", columnNames, buffer, employee);
						}
					}
					buffer.append(COMMA);

				} else {

					buffer.append(COMMA);
				}

				if (changeHistory.get(moneyType + "_ELIGIBLE_PLAN_ENTRY_DATE") != null
						|| planEntryDatesMap.get(theItem.getProfileId() + moneyType) != null) {

					EmployeeChangeHistoryVO vo = changeHistory.get(moneyType + "_ELIGIBLE_PLAN_ENTRY_DATE");
					String planEntryDate = planEntryDatesMap.get(theItem.getProfileId() + moneyType);
					buffer.append((vo == null || vo.getCurrentValue() == null)
							? (planEntryDate.length() > 0 ? planEntryDate : "")
							: getDateInMMddYYYYFormat(vo.getCurrentValue()));

					if (vo != null) {

						if (form.isEZstartOn() && !form.isEligibiltyCalcOn()) {
							if (isChangedWithinOptOutPeriod(currentContract.getContractNumber(),
									Long.valueOf(theItem.getProfileId()), "EMPLOYEE_PLAN_ENTRY_ELIGIBILITY",
									moneyType + "_ELIGIBLE_PLAN_ENTRY_DATE", rped, form.getOptOutDays(), employee)) {
								appendFieldChangedIndicator(moneyType + "_ELIGIBLE_PLAN_ENTRY_DATE", columnNames,
										buffer, employee);
							}
						} else if (form.isEZstartOn() && form.isEligibiltyCalcOn()) {
							if (isChangedWithinOptOutPeriod(currentContract.getContractNumber(),
									Long.valueOf(theItem.getProfileId()), "EMPLOYEE_PLAN_ENTRY_ELIGIBILITY",
									moneyType + "_ELIGIBLE_PLAN_ENTRY_DATE", rped, form.getOptOutDays(), employee)
									|| isChangedWithinNotificationPeriod(currentContract.getContractNumber(),
											Long.valueOf(theItem.getProfileId()), "EMPLOYEE_PLAN_ENTRY_ELIGIBILITY",
											moneyType + "_ELIGIBLE_PLAN_ENTRY_DATE",
											summaryReport.getNotificationPeriodStartDate(),
											summaryReport.getNotificationPeriodEndDate(), employee)) {
								appendFieldChangedIndicator(moneyType + "_ELIGIBLE_PLAN_ENTRY_DATE", columnNames,
										buffer, employee);
							}
						} else if (form.isEligibiltyCalcOn() && isChangedWithinNotificationPeriod(
								currentContract.getContractNumber(), Long.valueOf(theItem.getProfileId()),
								"EMPLOYEE_PLAN_ENTRY_ELIGIBILITY", moneyType + "_ELIGIBLE_PLAN_ENTRY_DATE",
								summaryReport.getNotificationPeriodStartDate(),
								summaryReport.getNotificationPeriodEndDate(), employee)) {
							appendFieldChangedIndicator(moneyType + "_ELIGIBLE_PLAN_ENTRY_DATE", columnNames, buffer,
									employee);
						}
					}
					buffer.append(COMMA);

				} else {

					buffer.append(COMMA);
				}

				if (overrideIndMap.get(theItem.getProfileId() + moneyType) != null) {

					String eligOverrideInd = overrideIndMap.get(theItem.getProfileId() + moneyType);
					buffer.append(eligOverrideInd).append(COMMA);

				} else {

					buffer.append(COMMA);

				}
			}

			if (form.isEZstartOn()) {

				if ("Y".equalsIgnoreCase(theItem.getAutoEnrollOptOutInd())) {
					buffer.append("Y");
				} else if ("N".equalsIgnoreCase(theItem.getAutoEnrollOptOutInd())) {
					buffer.append("N");
				} else {
					buffer.append("");
				}

				if (form.isEZstartOn()) {
					if (isChangedWithinOptOutPeriod(currentContract.getContractNumber(),
							Long.valueOf(theItem.getProfileId()), "EMPLOYEE_CONTRACT", "AUTO_ENROLL_OPT_OUT_IND", rped,
							form.getOptOutDays(), employee)) {
						appendFieldChangedIndicator(EligibilityReportData.OPT_OUT_COLUMN, columnNames, buffer,
								employee);
					}
				}

				// appendFieldChangedIndicator(EligibilityReportData.OPT_OUT_COLUMN,
				// columnNames, buffer);
				buffer.append(COMMA);
			}

			Double beforeTaxPctAtEnroll = null;
			Double beforeTaxAmtAtEnroll = null;
			Double afterTaxPctAtEnroll = null;
			Double afterTaxAmtAtEnroll = null;
			Double beforeTaxPct = null;
			Double beforeTaxAmt = null;
			Double afterTaxPct = null;
			Double afterTaxAmt = null;

			if (currentContract.hasRoth()) {
				try {
					beforeTaxPctAtEnroll = theItem.getAtEnrollmentBeforeTaxDeferralPct() != null
							? new Double(theItem.getAtEnrollmentBeforeTaxDeferralPct())
							: null;
				} catch (NumberFormatException e) {
					// it should never happen, assume null
				}
				try {
					beforeTaxAmtAtEnroll = theItem.getAtEnrollmentBeforeTaxDeferralAmt() != null
							? new Double(theItem.getAtEnrollmentBeforeTaxDeferralAmt())
							: null;
				} catch (NumberFormatException e) {
					// it should never happen, assume null
				}
				try {
					afterTaxPctAtEnroll = theItem.getAtEnrollmentAfterTaxDeferralPct() != null
							? new Double(theItem.getAtEnrollmentAfterTaxDeferralPct())
							: null;
				} catch (NumberFormatException e) {
					// it should never happen, assume null
				}
				try {
					afterTaxAmtAtEnroll = theItem.getAtEnrollmentAfterTaxDeferralAmt() != null
							? new Double(theItem.getAtEnrollmentAfterTaxDeferralAmt())
							: null;
				} catch (NumberFormatException e) {
					// it should never happen, assume null
				}
				try {
					beforeTaxPct = theItem.getBeforeTaxDeferralPct() != null
							? new Double(theItem.getBeforeTaxDeferralPct())
							: getCalculatedPct(theItem);
				} catch (NumberFormatException e) {
					// it should never happen, assume null
				}
				try {
					beforeTaxAmt = theItem.getBeforeTaxDeferralAmt() != null
							? new Double(theItem.getBeforeTaxDeferralAmt())
							: null;
				} catch (NumberFormatException e) {
					// it should never happen, assume null
				}
				try {
					afterTaxPct = theItem.getAfterTaxDeferralPct() != null
							? new Double(theItem.getAfterTaxDeferralPct())
							: null;
				} catch (NumberFormatException e) {
					// it should never happen, assume null
				}
				try {
					afterTaxAmt = theItem.getAfterTaxDeferralAmt() != null
							? new Double(theItem.getAfterTaxDeferralAmt())
							: null;
				} catch (NumberFormatException e) {
					// it should never happen, assume null
				}

				if (beforeTaxPctAtEnroll != null) {
					buffer.append(NumberRender.formatByPattern(beforeTaxPctAtEnroll, "", "##.###"));
				}
				buffer.append(COMMA);
				if (beforeTaxAmtAtEnroll != null) {
					buffer.append(beforeTaxAmtAtEnroll);
				}
				buffer.append(COMMA);
				if (afterTaxPctAtEnroll != null) {
					buffer.append(NumberRender.formatByPattern(afterTaxPctAtEnroll, "", "##.###"));
				}
				buffer.append(COMMA);
				if (afterTaxAmtAtEnroll != null) {
					buffer.append(afterTaxAmtAtEnroll);
				}
				buffer.append(COMMA);
				if (beforeTaxPct != null) {
					buffer.append(NumberRender.formatByPattern(beforeTaxPct, "", "##.###"));
				}
				buffer.append(COMMA);
				if (beforeTaxAmt != null) {
					buffer.append(beforeTaxAmt);
				}
				buffer.append(COMMA);
				if (afterTaxPct != null) {
					buffer.append(NumberRender.formatByPattern(afterTaxPct, "", "##.###"));
				}
				buffer.append(COMMA);
				if (afterTaxAmt != null) {
					buffer.append(afterTaxAmt);
				}
				buffer.append(COMMA);
			} else {
				try {
					beforeTaxPctAtEnroll = theItem.getAtEnrollmentBeforeTaxDeferralPct() != null
							? new Double(theItem.getAtEnrollmentBeforeTaxDeferralPct())
							: null;
				} catch (NumberFormatException e) {
					// it should never happen, assume null
				}
				try {
					beforeTaxAmtAtEnroll = theItem.getAtEnrollmentBeforeTaxDeferralAmt() != null
							? new Double(theItem.getAtEnrollmentBeforeTaxDeferralAmt())
							: null;
				} catch (NumberFormatException e) {
					// it should never happen, assume null
				}
				try {
					beforeTaxPct = theItem.getBeforeTaxDeferralPct() != null
							? new Double(theItem.getBeforeTaxDeferralPct())
							: getCalculatedPct(theItem);
				} catch (NumberFormatException e) {
					// it should never happen, assume null
				}
				try {
					beforeTaxAmt = theItem.getBeforeTaxDeferralAmt() != null
							? new Double(theItem.getBeforeTaxDeferralAmt())
							: null;
				} catch (NumberFormatException e) {
					// it should never happen, assume null
				}

				if (beforeTaxPctAtEnroll != null) {
					buffer.append(NumberRender.formatByPattern(beforeTaxPctAtEnroll, "", "##.###"));
				}
				buffer.append(COMMA);
				if (beforeTaxAmtAtEnroll != null) {
					buffer.append(beforeTaxAmtAtEnroll);
				}
				buffer.append(COMMA);
				if (beforeTaxPct != null) {
					buffer.append(NumberRender.formatByPattern(beforeTaxPct, "", "##.###"));
				}
				buffer.append(COMMA);
				if (beforeTaxAmt != null) {
					buffer.append(beforeTaxAmt);
				}
				buffer.append(COMMA);
			}

		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getEligibilityChangeDownloadData");
		}

		return buffer.toString().getBytes();

	}

	/**
	 * This method is used to get the list of PEDs and list of the Money types to
	 * populate in the drop downs of Eligibility change report page.
	 * 
	 * 
	 * @param actionForm
	 * @param request
	 * @param response
	 * @throws SystemException
	 * @return forward
	 */
	@RequestMapping(value="/eligibilityChangeReport/",method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}

		UserProfile userProfile = getUserProfile(request);
		EligibilityReportsForm form = (EligibilityReportsForm) actionForm;
		int contractId = userProfile.getCurrentContract().getContractNumber();

		Map csfMap = null;
		ContractServiceDelegate service = ContractServiceDelegate.getInstance();
		try {
			csfMap = service.getContractServiceFeatures(contractId);
		} catch (ApplicationException ae) {
			throw new SystemException(ae, className, "loadContractServiceFeatureData", ae.getDisplayMessage());
		}

		// Set eligibility calculation request values
		List<LabelValueBean> moneyTypes = new ArrayList<LabelValueBean>();

		boolean aeOn = false;
		boolean ecOn = false;

		try {
			aeOn = DeferralUtils.isEZstartOn(contractId);
			ecOn = DeferralUtils.isEligibilityCalcOn(contractId);

		} catch (ApplicationException ae) {
			throw new SystemException(ae, "Problem get CSF value for ezstart,ec");
		}

		form.setEZstartOn(aeOn);
		form.setEligibiltyCalcOn(ecOn);

		ContractServiceFeature aeCSF = (ContractServiceFeature) csfMap
				.get(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
		ContractServiceFeature eligibilityCalculationCSF = (ContractServiceFeature) csfMap
				.get(ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);

		if (!aeOn && !ecOn) {

			return HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}

		try {
			boolean isAEandDMContract = userProfile.getCurrentContract().isDMContract()
					&& (DeferralUtils.isEZstartOn(userProfile.getCurrentContract().getContractNumber()));
			if (isAEandDMContract) {
				form.setDMContract(true);
			} else {
				form.setDMContract(false);
			}
		} catch (ApplicationException ae) {
			throw new SystemException(ae, "Problem get CSF value for ezstart");
		}

		EligibilityServiceDelegate eligibilityServiceDelegate = EligibilityServiceDelegate
				.getInstance(PS_APPLICATION_ID);

		Date initialEnrollmentDate;
		try {
			initialEnrollmentDate = ContractServiceFeature.internalToDate(
					aeCSF.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_INITIAL_ENROLLMENT_DATE));
		} catch (ParseException e) {
			initialEnrollmentDate = null;
		}

		Map<String, List<Date>> pedsMap = new HashMap<String, List<Date>>();
		List<Date> moneyTypePEDS = new ArrayList<Date>();
		List<Date> tempMoneyTypePEDS = new ArrayList<Date>();
		GregorianCalendar currentDate = new GregorianCalendar();
		Date newDate = new Date();
		currentDate.setTime(newDate);
		currentDate = resetTheTime(currentDate);
		newDate = currentDate.getTime();

		List<MoneyTypeVO> contractMoneyTypesVoList = service.getContractMoneyTypes(contractId, true);

		if (form.getMoneyTypes() == null || form.getMoneyTypes().size() == 0) {

			if (eligibilityCalculationCSF != null && !"N".equalsIgnoreCase(eligibilityCalculationCSF.getValue())) {

				// get the money types for the contract

				MoneyTypeVO moneyTypeVO = null;

				// get money types for the contract with EC on
				List<String> moneyTypesList = eligibilityServiceDelegate
						.getMoneyTypesWithEligibilityService(contractId);

				Collections.sort(moneyTypesList);

				for (String moneyTypeId : moneyTypesList) {

					moneyTypeId = StringUtils.trimToEmpty(moneyTypeId);

					// get MoneyTypeVO for money type id
					moneyTypeVO = getMoneyTypeDetailsVO(moneyTypeId, contractMoneyTypesVoList);

					// this is not contract money type
					// hence move to next money type
					if (moneyTypeVO == null) {
						continue;
					}
					moneyTypes.add(new LabelValueBean(moneyTypeVO.getContractShortName(), moneyTypeVO.getId()));
				}

			} else if (aeCSF != null && "Y".equalsIgnoreCase(aeCSF.getValue())) {

				for (MoneyTypeVO vo : contractMoneyTypesVoList) {
					if ("EEDEF".equalsIgnoreCase(vo.getId())) {
						moneyTypes.add(new LabelValueBean(vo.getContractShortName(), vo.getId()));
					}
				}

			}

			// sort the money types based on alphabetical order of money source
			// short names

			Collections.sort(moneyTypes, new Comparator<LabelValueBean>() {
				public int compare(LabelValueBean vo1, LabelValueBean vo2) {
					return vo1.getLabel().compareTo(vo2.getLabel());
				}
			});

			form.setMoneyTypes(moneyTypes);

			if (userProfile.getCurrentContract().hasSpecialSortCategoryInd()) {
				form.setHasDivisionFeature(true);
			} else {
				form.setHasDivisionFeature(false);
			}

			if (Constants.EMPLOYEE_ID_SORT_OPTION_CODE
					.equalsIgnoreCase(userProfile.getCurrentContract().getParticipantSortOptionCode())) {
				form.setHasPayrollNumberFeature(true);
			} else {
				form.setHasPayrollNumberFeature(false);
			}

		}

		moneyTypes = form.getMoneyTypes();

		Map<String, String> moneyTypeIdNames = new HashMap<String, String>();

		for (int no = 0; no < moneyTypes.size(); no++) {

			moneyTypeIdNames.put(moneyTypes.get(no).getValue(), moneyTypes.get(no).getLabel());
		}

		form.setMoneyTypeIdName(moneyTypeIdNames);

		List<ContractMoneyTypeEligibilityVO> totalMoneyTypePEDS = eligibilityServiceDelegate
				.getmoneyTypePastAndFuturePEDs(contractId);

		for (int mped = 0; mped < totalMoneyTypePEDS.size(); mped++) {

			moneyTypePEDS = new ArrayList<Date>();
			ContractMoneyTypeEligibilityVO vo = totalMoneyTypePEDS.get(mped);
			String moneyType = vo.getMoneyType();

			if (moneyTypeIdNames.keySet().contains(moneyType.trim())) {

				if (EEDEF.equalsIgnoreCase(moneyType.trim())) {
					form.setOptOutDays(vo.getOptOutDays());
				}

				// For next Plan Entry Date 1
				moneyTypePEDS = addTheApplicablePEDs(form.isDMContract(), vo.getNextPlanEntryDate(), moneyTypePEDS);
				// For next Plan Entry Date 2
				moneyTypePEDS = addTheApplicablePEDs(form.isDMContract(), vo.getNextPlanEntryDate2(), moneyTypePEDS);
				// For next Plan Entry Date 3
				moneyTypePEDS = addTheApplicablePEDs(form.isDMContract(), vo.getNextPlanEntryDate3(), moneyTypePEDS);

				// If the initial Enrollment date is greater than the current date then don't
				// add the previous PED because
				// the view returns the previous PED as 01/01/0001.
				// If IED is less than the current date and greater than the previous PED then
				// the view returns the
				// Previous PED has the same values as the IED.
				if (initialEnrollmentDate != null && initialEnrollmentDate.compareTo(newDate) >= 0
						&& form.isEZstartOn()) {

					if (EEDEF.equalsIgnoreCase(moneyType.trim())) {
						moneyTypePEDS.add(initialEnrollmentDate);
					}

				} else {
					// For Previous Plan Entry Date
					if (vo.getNextPlanEntryDate().compareTo(newDate) > 0) {
						moneyTypePEDS.add(vo.getPreviousPlanEntryDate());
					}
				}

				pedsMap.put(moneyType.trim(), moneyTypePEDS);
				tempMoneyTypePEDS.addAll(moneyTypePEDS);
			}
			moneyTypePEDS = null;
		}

		if (initialEnrollmentDate != null) {
			form.setInitialEnrollmentDate(fullMonthFormat.format(initialEnrollmentDate));
		}

		// Add the IED only when the EZStart is on.
		/*
		 * if(initialEnrollmentDate != null && form.isEZstartOn()){
		 * 
		 * form.setInitialEnrollmentDate(dateFormat.format(initialEnrollmentDate)); //
		 * If the IED is not one of the PED, then add the IED for money type 'EEDEF'.
		 * if(!tempMoneyTypePEDS.contains(initialEnrollmentDate) && ( eedefPreviousPED
		 * != null && eedefPreviousPED.compareTo(initialEnrollmentDate) <= 0 ) ){
		 * tempMoneyTypePEDS.add(initialEnrollmentDate); if(pedsMap.get(EEDEF)!= null){
		 * List<Date> dates = pedsMap.get(EEDEF); dates.add(initialEnrollmentDate);
		 * pedsMap.put(EEDEF, dates); }else{ List<Date> dates = new ArrayList<Date>();
		 * dates.add(initialEnrollmentDate); pedsMap.put(EEDEF, dates); }
		 * form.setHasIED(true); }else{ List<Date> dates = new ArrayList<Date>();
		 * dates.add(eedefPreviousPED); pedsMap.put(EEDEF, dates);
		 * form.setHasIED(false); } }
		 */

		Set<Date> pedSet = new HashSet<Date>(tempMoneyTypePEDS);

		tempMoneyTypePEDS = new ArrayList<Date>(pedSet);

		// sort the Array of PEDs in descending order.

		Collections.sort(tempMoneyTypePEDS);
		Collections.reverse(tempMoneyTypePEDS);

		Map<String, List<LabelValueBean>> pedMoneyTypes = new HashMap<String, List<LabelValueBean>>();
		List<String> dropDownPEDs = new ArrayList<String>();

		// To get the map with PED as key and value is the list of the money types for
		// that PED.
		for (int ped = 0; ped < tempMoneyTypePEDS.size(); ped++) {

			List<LabelValueBean> moneyTypePEDList = new ArrayList<LabelValueBean>();

			for (int mt = 0; mt < moneyTypes.size(); mt++) {

				if (pedsMap.get(moneyTypes.get(mt).getValue()) != null) {

					if (pedsMap.get(moneyTypes.get(mt).getValue()).contains(tempMoneyTypePEDS.get(ped))) {

						moneyTypePEDList
								.add(new LabelValueBean(moneyTypes.get(mt).getLabel(), moneyTypes.get(mt).getValue()));
					}
				}
			}
			String stringPED = fullMonthFormat.format(tempMoneyTypePEDS.get(ped));
			if (ped == 0) {
				form.setReportedPED(stringPED);
			}
			dropDownPEDs.add(stringPED);
			pedMoneyTypes.put(stringPED, moneyTypePEDList);
		}

		form.setEligibilityChangePEDsList(dropDownPEDs);
		form.setMoneyTypePEDMap(pedMoneyTypes);

		// return mapping.findForward("default");
		return forwards.get("default");
	}

	/**
	 * This method is used to reset the time to 00:00:00 a.m.
	 * 
	 * @param cal
	 * @return
	 */
	private GregorianCalendar resetTheTime(GregorianCalendar cal) {

		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);

		return cal;
	}

	/**
	 * This method is used to return the passed date if the date is within the
	 * notification period. Notification period is 52 days prior to the PED if the
	 * contract is DMContract else the notification period is 45 days prior to the
	 * PED.
	 * 
	 * @param DMContract
	 * @param PED
	 * @param moneyTypePEDS
	 * @return
	 */
	private List<Date> addTheApplicablePEDs(boolean DMContract, Date PED, List<Date> moneyTypePEDS) {

		GregorianCalendar currentDate = new GregorianCalendar();
		Date newDate = new Date();
		currentDate.setTime(newDate);
		resetTheTime(currentDate);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(PED);
		if (DMContract) {
			cal.add(Calendar.DAY_OF_YEAR, -52);
		} else {
			cal.add(Calendar.DAY_OF_YEAR, -45);
		}
		resetTheTime(cal);
		if (cal.getTime().compareTo(currentDate.getTime()) < 0) {
			moneyTypePEDS.add(PED);
		}

		return moneyTypePEDS;

	}

	/**
	 * Builds a list of column names that were changed for this employee
	 * 
	 * @param list
	 * @return
	 */
	private List<String> buildListOfColumnNames(List<EmployeeChangeHistoryVO> list) {

		List<String> names = new ArrayList<String>();

		if (list != null && !list.isEmpty()) {
			for (EmployeeChangeHistoryVO element : list) {
				names.add(element.getColumnName().toUpperCase());
			}
		}

		return names;
	}

	/**
	 * Utility that checks if the column name exists in the list If it does it
	 * appends a '(*)' to buffer
	 * 
	 * @param columnName
	 * @param columnNames
	 * @param buffer
	 * @param employee
	 *            TODO
	 */
	private void appendFieldChangedIndicator(String columnName, List<String> columnNames, StringBuffer buffer,
			Employee employee) {
		if (columnNames != null && !columnNames.isEmpty() && columnName != null) {
			if (columnNames.contains(columnName.toUpperCase())) {
				if ("NEW_EMPLOYEE".equalsIgnoreCase(columnName)) {
					employee.setNewEmployee(true);
					buffer.append("(*)");
				} else {
					buffer.append("*");
				}
			}
		}
	}

	/**
	 * Utility method that prepares strings to be displayed
	 * 
	 * @param field
	 * @return
	 */
	private String processString(String field) {
		if (field == null || "mm/dd/yyyy".equalsIgnoreCase(field)) {
			return "";
		} else {
			return field.trim().replaceAll("\\,", " ");
		}
	}

	/**
	 * Returns the contract default deferral % if the status is 'Pending Enrollment'
	 * The stored procedure is passing the contract default deferral % if the status
	 * is 'Pending Enrollment' in the contributionPct property
	 * 
	 * @param theItem
	 * @return
	 */
	private Double getCalculatedPct(EmployeeSummaryDetails theItem) {
		Double beforeTaxPct = null;

		if (PENDING_ENROLLMENT.equalsIgnoreCase(theItem.getEnrollmentStatus())) {
			try {
				beforeTaxPct = theItem.getContributionPct() != null ? new Double(theItem.getContributionPct()) : null;
			} catch (NumberFormatException e) {
				// it should never happen, assume null
			}
		}

		return beforeTaxPct;
	}

	/**
	 * This method is used to get the file name. The file name format is : contract
	 * number_Type Of Report_date of report creating in mm dd yyyy format.csv
	 */
	protected String getFileName(HttpServletRequest request) {
		String fileName = "";
		String date = "MM dd yyyy";
		SimpleDateFormat dateFormat = new SimpleDateFormat(date);
		date = dateFormat.format(new Date());

		// Identify the type of report
		if (ELIGIBILITY_CHANGE_REPORT.equalsIgnoreCase(request.getParameter(REPORT_TYPE))) {
			fileName = getUserProfile(request).getCurrentContract().getContractNumber() + " "
					+ ELIGIBILITY_CHANGE_REPORT_NAME + " " + date + CSV_EXTENSION;
		}
		// Replace spaces with underscores
		return fileName.replaceAll("\\ ", "_");
	}

	/**
	 * get the MoneyTypeVO for the given money type id
	 * 
	 * @param moneyTypeId
	 * @param moneyTypeVOList
	 * @return MoneyTypeVO
	 */

	private MoneyTypeVO getMoneyTypeDetailsVO(String moneyTypeId, Collection<MoneyTypeVO> moneyTypeVOList) {
		for (MoneyTypeVO moneyTypeVO : moneyTypeVOList) {
			if (StringUtils.equals(moneyTypeId, StringUtils.trimToEmpty(moneyTypeVO.getId()))) {
				return moneyTypeVO;
			}
		}
		return null;

	}

	private String getDateInMMddYYYYFormat(String date) {

		String convertedDate = "";
		try {

			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			convertedDate = format.format(format1.parse(date));

		} catch (ParseException exception) {

		}

		return convertedDate;
	}

	private boolean isChangedWithinOptOutPeriod(int contractId, Long profileId, String tableName, String columnName,
			Date ped, int optOutDays, Employee employee) throws SystemException {

		boolean isChanged = false;

		GregorianCalendar reportedPED = new GregorianCalendar();
		reportedPED.setTime(ped);

		Date rped = reportedPED.getTime();
		reportedPED.add(Calendar.DAY_OF_YEAR, -optOutDays);

		Date optoutDeadline = reportedPED.getTime();

		EmployeeServiceDelegate employeeServiceDelegate = EmployeeServiceDelegate
				.getInstance(Constants.PS_APPLICATION_ID);

		Date latestCreateDate = employeeServiceDelegate.getLatestEmployeeChangeHistCreateDate(contractId, profileId,
				tableName, columnName);

		int count = employeeServiceDelegate.getEmployeeChangeHistoryCount(contractId, BigDecimal.valueOf(profileId),
				columnName, tableName);

		if ((!employee.isNewEmployee() || count > 1) && (latestCreateDate != null)
				&& (latestCreateDate.compareTo(optoutDeadline) >= 0) && (latestCreateDate.compareTo(rped) <= 0)) {
			isChanged = true;
		}

		return isChanged;
	}

	/**
	 * Method used to check eligibility date change with in the notification period
	 * 
	 * @param contractId
	 *            - Unique Contract Id
	 * @param profileId
	 *            - Unique Profile Id
	 * @param tableName
	 *            - Modified Table Name
	 * @param columnName
	 *            - Modified Column Name
	 * @param notificationStartDate
	 *            - Notification Period Start Date
	 * @param notificationEndDate
	 *            - Notification Period End Date
	 * @param employee
	 * @return isChanged
	 * @throws SystemException
	 */
	private boolean isChangedWithinNotificationPeriod(int contractId, Long profileId, String tableName,
			String columnName, Date notificationStartDate, Date notificationEndDate, Employee employee)
			throws SystemException {

		boolean isChanged = false;
		EmployeeServiceDelegate employeeServiceDelegate = EmployeeServiceDelegate
				.getInstance(Constants.PS_APPLICATION_ID);

		Date latestCreateDate = employeeServiceDelegate.getLatestEmployeeChangeHistCreateDate(contractId, profileId,
				tableName, columnName);

		int count = employeeServiceDelegate.getEmployeeChangeHistoryCount(contractId, BigDecimal.valueOf(profileId),
				columnName, tableName);

		if ((!employee.isNewEmployee() || count > 1) && (latestCreateDate != null)
				&& (latestCreateDate.compareTo(notificationStartDate) >= 0)
				&& (latestCreateDate.compareTo(notificationEndDate) <= 0)) {
			isChanged = true;
		}

		return isChanged;
	}

	/**
	 * Method used to append the content message of given content Id
	 * 
	 * @param contentId
	 *            - CMA content Id
	 * @param stringBuffer
	 *            - Buffer
	 * @throws SystemException
	 */
	private void appendContentMessage(int contentId, StringBuffer stringBuffer) throws SystemException {

		final String backwardSlash = "\"";
		final String text = "text";

		try {
			Content message = ContentCacheManager.getInstance().getContentById(contentId,
					ContentTypeManager.instance().MESSAGE);

			String contentMessage = ContentUtility.getContentAttribute(message, text);

			stringBuffer.append(backwardSlash + StringUtils.trimToEmpty(contentMessage) + backwardSlash)
					.append(LINE_BREAK);

		} catch (ContentException contentException) {
			throw new SystemException(contentException, "Unable to get the content from CMA");
		}
	}

	/**
	 * 
	 * @author arugupu
	 *
	 */
	private static class Employee {
		private boolean newEmployee = false;

		/**
		 * @param newEmployee
		 *            the newEmployee to set
		 */
		Employee(boolean newEmployee) {
			this.newEmployee = newEmployee;
		}

		/**
		 * @return the newEmployee
		 */
		public boolean isNewEmployee() {
			return newEmployee;
		}

		/**
		 * @param newEmployee
		 *            the newEmployee to set
		 */
		public void setNewEmployee(boolean newEmployee) {
			this.newEmployee = newEmployee;
		}

	}

	

	@RequestMapping(value="/eligibilityChangeReport/",params = {"task=filter" }, method = {RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value="/eligibilityChangeReport/",params = {"task=page"}, method = {RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value="/eligibilityChangeReport/",params = {"task=sort" }, method = {RequestMethod.GET})
	public String doSort(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value="/eligibilityChangeReport/",params = {"task=download" }, method = {
			RequestMethod.POST})
	public String doDownload(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value="/eligibilityChangeReport/",params = {
			"task=dowanloadAll" }, method = {  RequestMethod.GET })
	public String doDownloadAll(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;
@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}

}
