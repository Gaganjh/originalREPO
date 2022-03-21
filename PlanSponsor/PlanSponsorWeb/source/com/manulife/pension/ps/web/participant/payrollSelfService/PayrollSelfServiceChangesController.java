package com.manulife.pension.ps.web.participant.payrollSelfService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangesReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

@Controller
@RequestMapping("/participant/payrollSelfService")
@SessionAttributes({ "payrollSelfServiceChangesForm" })
public class PayrollSelfServiceChangesController extends ReportController {

	private static final Logger LOGGER = Logger.getLogger(PayrollSelfServiceChangesController.class);
	private static final String FORWARD_DEFAULT_PAGE = "/participant/payrollSelfService/changes.jsp";
	private static final int FIRST_PAGE = 1;
	private static final String CSV_DOWNLOAD_FILENAME_PART = "PayrollSelfService_";
	
	
	private static final Set<String> SORT_APPLICABLE_TASKS = new HashSet<>(Arrays.asList(null, SORT_TASK, FILTER_TASK, RESET_TASK));

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;
	@Autowired
	private PayrollSelfServiceReportValidator payrollSelfServiceReportValidator;
	@Autowired
	private PayrollSelfServiceChangesCSVReport payrollSelfServiceChangesCSVReport;

	@ModelAttribute("payrollSelfServiceChangesForm")
	public PayrollSelfServiceChangesForm populateForm(HttpServletRequest request) {
		PayrollSelfServiceChangesForm payrollSelfServiceChangesForm = new PayrollSelfServiceChangesForm();		
		payrollSelfServiceChangesForm.setLoanAllowedIndicator(isLoanAllowed(getContractNumber(request)));
		return payrollSelfServiceChangesForm;
		
	}

	@GetMapping
	public String doGet(
			@Valid @ModelAttribute("payrollSelfServiceChangesForm") PayrollSelfServiceChangesForm payrollSelfServiceChangesForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
					throws SystemException {
		return doPost(payrollSelfServiceChangesForm, bindingResult, request, response);
	}

	@PostMapping
	public String doPost(
			@Valid @ModelAttribute("payrollSelfServiceChangesForm") PayrollSelfServiceChangesForm payrollSelfServiceChangesForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
					throws SystemException {

		if(bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}			
			populateReportForm(payrollSelfServiceChangesForm, request);
			PayrollSelfServiceChangesReportData reportData = payrollSelfServiceChangesForm.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);

			return FORWARD_DEFAULT_PAGE;
		}

		// Validate User and Contract for access		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		if(userProfile == null || userProfile.isSelectedAccess() 
				|| userProfile.getCurrentContract().isDiscontinued()) {
			return 	Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}

		String task = request.getParameter("task");
		PayrollSelfServiceChangesReportData report = (PayrollSelfServiceChangesReportData) request.getSession()
				.getAttribute(PayrollSelfServiceChangesReportData.REPORT_NAME);

		// Build Report if none built, yet or filter specifically requested.
		if(report == null 
				|| FILTER_TASK.equalsIgnoreCase(task) 
				|| RESET_TASK.equalsIgnoreCase(task)) {
			if(RESET_TASK.equalsIgnoreCase(task)) {
				super.resetForm( payrollSelfServiceChangesForm, request);
				payrollSelfServiceChangesForm.setLoanAllowedIndicator(isLoanAllowed(getContractNumber(request)));
			}			
			super.doFilter(payrollSelfServiceChangesForm, request, response);
			report = (PayrollSelfServiceChangesReportData) request.getAttribute(CommonConstants.REPORT_BEAN);
		}

		// Sort by default or by request
		if(SORT_APPLICABLE_TASKS.contains(StringUtils.trimToNull(task))) {
			PayrollSelfServiceUtils.sortReportDetails(report, payrollSelfServiceChangesForm.getSortField(),
					payrollSelfServiceChangesForm.getSortDirection());
		}
		report.setPageNumber(payrollSelfServiceChangesForm.getPageNumber());

		// Update form - report details are displayed from this bean.
		payrollSelfServiceChangesForm.setReport(report);

		// Store report as Request Attribute - some tags require it.
		request.setAttribute(CommonConstants.REPORT_BEAN, report);

		// Store report in Session to avoid frequent database calls.
		request.getSession().setAttribute(PayrollSelfServiceChangesReportData.REPORT_NAME, report);

		return FORWARD_DEFAULT_PAGE;
	}

	@GetMapping(params = { "task=download" })
	public void doDownload(
			@Valid @ModelAttribute("payrollSelfServiceChangesForm") PayrollSelfServiceChangesForm payrollSelfServiceChangesForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
					throws SystemException {

		super.doDownload(payrollSelfServiceChangesForm, request, response);
	}

	@Override
	protected String getReportId() {
		return PayrollSelfServiceChangesReportData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return PayrollSelfServiceChangesReportData.REPORT_NAME;
	}

	@Override
	protected String getDefaultSort() {
		return PayrollSelfServiceChangesReportData.DEFAULT_SORT;
	}

	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		final int contractId = getContractNumber(request);
		PayrollSelfServiceChangesForm payrollSelfServiceChangesForm = (PayrollSelfServiceChangesForm) reportForm;
		payrollSelfServiceChangesForm.setContractId(String.valueOf(contractId));
		payrollSelfServiceChangesForm.setContractName(getContractName(contractId));
		return payrollSelfServiceChangesCSVReport.generateCSV(report.getDetails(), payrollSelfServiceChangesForm,
				PayrollSelfServiceChangesWebUtility.requireSSNMask(getUserProfile(request))).getBytes();
	}

	protected String getFileName(HttpServletRequest request) {
		return new StringBuilder().append(CSV_DOWNLOAD_FILENAME_PART)
				.append(getUserProfile(request).getCurrentContract().getContractNumber()).append(CSV_EXTENSION)
				.toString();
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form, HttpServletRequest request)
			throws SystemException {

		PayrollSelfServiceChangesForm payrollSelfServiceChangesForm = (PayrollSelfServiceChangesForm) form;
		final int contractNumber = getContractNumber(request);
		criteria.addFilter(PayrollSelfServiceChangesReportData.FILTER_CONTRACT_ID, Integer.valueOf(contractNumber));
		if (StringUtils.isNotBlank(payrollSelfServiceChangesForm.getLastName())) {
			criteria.addFilter(PayrollSelfServiceChangesReportData.FILTER_LAST_NAME,
					payrollSelfServiceChangesForm.getLastName());
		} else if (StringUtils.isNotBlank(payrollSelfServiceChangesForm.getSSN().toString())) {
			criteria.addFilter(PayrollSelfServiceChangesReportData.FILTER_SSN,
					payrollSelfServiceChangesForm.getSSN().toString());
		}
		if (StringUtils.isNotBlank(payrollSelfServiceChangesForm.getSelfServiceType())) {
			criteria.addFilter(PayrollSelfServiceChangesReportData.FILTER_RECORD_TYPE,
					payrollSelfServiceChangesForm.getSelfServiceType());
		}

		if (Objects.nonNull(payrollSelfServiceChangesForm.getEffectiveDateFrom())) {
			criteria.addFilter(PayrollSelfServiceChangesReportData.FILTER_FROM_EFFECTIVE_DATE,
					payrollSelfServiceChangesForm.getEffectiveDateFrom());
		}
		if (Objects.nonNull(payrollSelfServiceChangesForm.getEffectiveDateTo())) {
			criteria.addFilter(PayrollSelfServiceChangesReportData.FILTER_TO_EFFECTIVE_DATE,
					payrollSelfServiceChangesForm.getEffectiveDateTo());
		}
		if(payrollSelfServiceChangesForm.isLoanAllowedIndicator()) {
			criteria.addFilter(PayrollSelfServiceChangesReportData.FILTER_INCLUDE_LOANS,
					Boolean.TRUE);
		}
	}

	private int getContractNumber(HttpServletRequest request) {
		// always need the contractId
		UserProfile userProfile = getUserProfile(request);
		return userProfile.getCurrentContract().getContractNumber();
	}

	@Override
	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {
		final String task = getTask(request);		
		reportForm.setPageNumber(FIRST_PAGE);

		if (isDefaultTask(task) 
				|| isSortFieldMissing(reportForm)) {
			reportForm.setSortField(getDefaultSort());
		}
		if (isDefaultTask(task) 
				|| isSortFieldDirectionMissing(reportForm)) {
			reportForm.setSortDirection(getDefaultSortDirection());
		}

	}

	private boolean isDefaultTask(final String task) {
		return task.equals(DEFAULT_TASK);
	}

	private boolean isSortFieldMissing(BaseReportForm reportForm) {
		return Objects.isNull(reportForm.getSortField()) || reportForm.getSortField().isEmpty();
	}

	private boolean isSortFieldDirectionMissing(BaseReportForm reportForm) {
		return Objects.isNull(reportForm.getSortDirection()) || reportForm.getSortDirection().isEmpty();
	}

	private String getContractName(int contractId) {
		try {
			return ContractServiceDelegate.getInstance().getContractName(contractId);
		} catch (SystemException exp) {
			LOGGER.error("Excepion while getting contract name for " + contractId + " from Contract service" + exp);
		}
		return null;
	}
	
	private boolean isLoanAllowed(int contractId) {
		try {
			PlanDataLite planDataLite = ContractServiceDelegate.getInstance().getPlanDataLight(contractId);
			if(planDataLite != null) {
				return BooleanUtils.toBoolean(planDataLite.getLoansAllowedInd());
			}
			
		} catch (SystemException exp) {
			LOGGER.error("Excepion while getting loanAllowedInd for " + contractId + " from Contract service" + exp);
		}
		return false;
	}
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
		binder.addValidators(payrollSelfServiceReportValidator);
	}
}
