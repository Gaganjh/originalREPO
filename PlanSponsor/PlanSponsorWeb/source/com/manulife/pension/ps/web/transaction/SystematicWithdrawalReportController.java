package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.FilterCriteriaVo;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.transaction.util.WithdrawalUtil;
import com.manulife.pension.ps.web.util.ProtectedStringBuffer;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawDataItem;
import com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * SystematicWithdrawal Action class - This class is used to get the information
 * for the Systematioc Withdrawls of the specified contracts in PSW Transactions
 * page.
 * 
 * @author Angel Petricia
 */
@Controller
@RequestMapping(value = "/transaction")
@SessionAttributes({ "systematicWithdrawReportForm" })

public class SystematicWithdrawalReportController extends AbstractTransactionReportController {
	@ModelAttribute("systematicWithdrawReportForm")
	public SystematicWithdrawReportForm populateForm() {
		return new SystematicWithdrawReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/transaction/systematicWithdrawalReport.jsp");
		forwards.put("default", "/transaction/systematicWithdrawalReport.jsp");
		forwards.put("sort", "/transaction/systematicWithdrawalReport.jsp");
		forwards.put("filter", "/transaction/systematicWithdrawalReport.jsp");
		forwards.put("page", "/transaction/systematicWithdrawalReport.jsp");
		forwards.put("print", "/transaction/systematicWithdrawalReport.jsp");
		forwards.put("reset", "/transaction/systematicWithdrawalReport.jsp");
	}

	private static final String DEFAULT_SORT_FIELD = SystematicWithdrawReportData.SORT_FIELD_NAME;
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;
	private static final String LAST_NAME = "participantName";
	private static final String SSN = "ssn";
	private static final RegularExpressionRule ssnRErule = new RegularExpressionRule(CommonErrorCodes.SSN_INVALID,
			CommonConstants.SSN_RE);
	private static final RegularExpressionRule lastNameRErule = new RegularExpressionRule(
			CommonErrorCodes.LAST_NAME_INVALID, CommonConstants.FIRST_NAME_LAST_NAME_RE);

	public static final String LAST_NAME_LABEL = "Last Name";
	public static final String WD_STATUS_LABEL = "Withdrawal status";
	public static final String WD_TYPE_LABEL = "Withdrawal type";
	public static final String SSN_LABEL = "SSN";
	private static final String CONTRACT = "Contract";
	protected static final String SYSTEMATIC_WITHDRAWALS = "Systematic Withdrawals";

	protected static final String DOWNLOAD_COLUMN_HEADING_TOTAL = "Name,SSN,Withdrawal status,Withdrawal type,"
			+ "Setup date,Calculation method,Frequency,Last payment date,Last payment amount, Total current assets";
	private static final String NOT_PROVIDED = "-";
	private static final String FILTERS_USED = "Filters used";
	private static final String AS_OF_DATE = "As of";

	private static Map<String, String> wdStatusMap = new HashMap<String, String>();
	private static Map<String, String> wdTypeMap = new HashMap<String, String>();
	static {
		wdStatusMap.put("ACTIVE", "Active");
		wdStatusMap.put("COMPLETED", "Completed");
		wdStatusMap.put("All", "All");
		wdStatusMap.put("INACTIVE", "Inactive");
		wdStatusMap.put("PENDING", "Pending");
		// Populate Withdrawal status Map
		wdTypeMap.put("All", "All");
		wdTypeMap.put("RMD", "RMD");
		wdTypeMap.put("INSTALLMENT", "Installment");
	}

	private static Logger logger = Logger.getLogger(SystematicWithdrawalReportController.class);

	/**
	 * Constructor for SystematicWithdrawalReportAction.
	 */
	public SystematicWithdrawalReportController() {
		super(SystematicWithdrawalReportController.class);
	}

	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}
	
	/**
	 * This method is called when reset button is clicked
	 * 
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws SystemException
	 */
	@RequestMapping(value = "/systematicWithdrawalReport/", params = {"task=reset" }, method = {RequestMethod.POST })
	public String doReset(@Valid @ModelAttribute("systematicWithdrawReportForm") SystematicWithdrawReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				SystematicWithdrawReportData report = new SystematicWithdrawReportData(null, 0);
				request.setAttribute(Constants.REPORT_BEAN, report);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
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
		super.resetForm(form, request);

		String forward = doCommon(form, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doReset");
		}
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/systematicWithdrawalReport/", method = {RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("systematicWithdrawReportForm") SystematicWithdrawReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				SystematicWithdrawReportData report = new SystematicWithdrawReportData(null, 0);
				request.setAttribute(Constants.REPORT_BEAN, report);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/systematicWithdrawalReport/", params = {"task=filter"}, method = {RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("systematicWithdrawReportForm") SystematicWithdrawReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				SystematicWithdrawReportData report = new SystematicWithdrawReportData(null, 0);
				request.setAttribute(Constants.REPORT_BEAN, report);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Collection errors = doValidate(form, request);
		if(errors.size()>0){
			return forwards.get("input");
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/systematicWithdrawalReport/", params = {"task=page"}, method = {RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("systematicWithdrawReportForm") SystematicWithdrawReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				SystematicWithdrawReportData report = new SystematicWithdrawReportData(null, 0);
				request.setAttribute(Constants.REPORT_BEAN, report);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/systematicWithdrawalReport/", params = { "task=sort" }, method = {RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("systematicWithdrawReportForm") SystematicWithdrawReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				SystematicWithdrawReportData report = new SystematicWithdrawReportData(null, 0);
				request.setAttribute(Constants.REPORT_BEAN, report);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/systematicWithdrawalReport/", params = {"task=download" }, method = {RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("systematicWithdrawReportForm") SystematicWithdrawReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				SystematicWithdrawReportData report = new SystematicWithdrawReportData(null, 0);
				request.setAttribute(Constants.REPORT_BEAN, report);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm(reportForm, request);
		String task = getTask(request);
		if (FILTER_TASK.equals(task)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
	}

	@Override
	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		criteria.insertSort(sortField, sortDirection);

	}

	public String doCommon(BaseReportForm reportForm, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		SystematicWithdrawReportForm form = (SystematicWithdrawReportForm) reportForm;

		String forward = super.doCommon(form, request, response);

		UserProfile userProfile = getUserProfile(request);
		form.setAsOfDate(String.valueOf(userProfile.getCurrentContract().getContractDates().getAsOfDate()));
		List wdStatusList = null;
		wdStatusList = WithdrawalUtil.getInstance().getWdStatusList();
		if (wdStatusList != null) {
			form.setStatusList(wdStatusList);
		}

		List wdTypeList = null;
		wdTypeList = WithdrawalUtil.getInstance().getWdTypeList();
		if (wdTypeList != null) {
			form.setTypeList(wdTypeList);
		}
		return forward;

	}

	protected String getFileName(BaseReportForm form, HttpServletRequest request) {
		return getReportName() + CSV_EXTENSION;
	}

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request) {
		FastDateFormat fdf = FastDateFormat.getInstance("MM/dd/yyyy");
		SystematicWithdrawReportForm form = (SystematicWithdrawReportForm) reportForm;
		ProtectedStringBuffer buffer = new ProtectedStringBuffer();

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		Date asOfDate = currentContract.getContractDates().getAsOfDate();
		buffer.append(SYSTEMATIC_WITHDRAWALS).append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append(CommonConstants.CONTRACT).append(COMMA).append(currentContract.getContractNumber()).append(COMMA)
				.append(getCsvString(currentContract.getCompanyName())).append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append(AS_OF_DATE).append(COMMA).append(fdf.format(asOfDate)).append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		if ((StringUtils.isNotBlank(form.getParticipantName()))
				|| ((StringUtils.isNotBlank(form.getWdStatus())) && (!form.getWdStatus().equalsIgnoreCase("All")))
				|| ((StringUtils.isNotBlank(form.getWdType())) && (!form.getWdType().equalsIgnoreCase("All")))
				|| (StringUtils.isNotBlank(form.getSsnOne()))) {
			buffer.append(FILTERS_USED).append(LINE_BREAK);
		}

		if (StringUtils.isNotBlank(form.getParticipantName()) && (!form.getParticipantName().isEmpty())) {

			buffer.append(LAST_NAME_LABEL).append(COMMA).append(form.getParticipantName()).append(LINE_BREAK);
		}
		if (StringUtils.isNotBlank(form.getWdStatus()) && (!form.getWdStatus().equalsIgnoreCase("All"))) {
			buffer.append(WD_STATUS_LABEL).append(COMMA).append(wdStatusMap.get(form.getWdStatus())).append(LINE_BREAK);
		}
		if (StringUtils.isNotBlank(form.getWdType()) && (!form.getWdType().equalsIgnoreCase("All"))) {
			buffer.append(WD_TYPE_LABEL).append(COMMA).append(wdTypeMap.get(form.getWdType())).append(LINE_BREAK);
		}
		if (form.getSsnOne() != null && (!form.getSsnOne().isEmpty())) {
			buffer.append(SSN_LABEL).append(COMMA).append(form.getSsnOne() + form.getSsnTwo() + form.getSsnThree())
					.append(LINE_BREAK);
		}

		// Quick filter - end
		if ((StringUtils.isNotBlank(form.getParticipantName()))
				|| ((StringUtils.isNotBlank(form.getWdStatus())) && (!form.getWdStatus().equalsIgnoreCase("All")))
				|| ((StringUtils.isNotBlank(form.getWdType())) && (!form.getWdType().equalsIgnoreCase("All")))
				|| (form.getSsnOne() != null)) {
			buffer.append(LINE_BREAK);
		}

		buffer.append(DOWNLOAD_COLUMN_HEADING_TOTAL);
		buffer.append(LINE_BREAK);

		Iterator iterator = report.getDetails().iterator();
		boolean recordAvailable = false;
		while (iterator.hasNext()) {
			SystematicWithdrawDataItem theItem = (SystematicWithdrawDataItem) iterator.next();
			recordAvailable = true;
			buffer.append(getCsvString(theItem.getParticipant().getLastName().trim() + COMMA
					+ CommonConstants.SINGLE_SPACE_SYMBOL + theItem.getParticipant().getFirstName().trim()))
					.append(COMMA);
			if (theItem.getParticipant().getSsn() != null)

			{
				buffer.append(SSNRender.format(theItem.getParticipant().getSsn(), null)).append(COMMA);

			} else
				buffer.append(COMMA);

			buffer.append(getCsvString(theItem.getWdStatus())).append(COMMA);
			buffer.append(getCsvString(theItem.getWdType())).append(COMMA);

			if (theItem.getWdSetupDate() != null) {
				buffer.append(DateRender.formatByPattern(theItem.getWdSetupDate(), NOT_PROVIDED,
						RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
			} else
				buffer.append(COMMA);
			buffer.append(getCsvString(theItem.getCalculationMethod())).append(COMMA);
			buffer.append(getCsvString(theItem.getWdfrequency())).append(COMMA);

			if (theItem.getLastPayDate() != null) {
				buffer.append(DateRender.formatByPattern(theItem.getLastPayDate(), NOT_PROVIDED,
						RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
			} else
				buffer.append(COMMA);

			buffer.append(getCsvString(NumberRender.formatByType(theItem.getLastPaymentAmount(),
					CommonConstants.DEFAULT_VALUE_ZERO, RenderConstants.DECIMAL_TYPE))).append(COMMA);

			buffer.append(getCsvString(NumberRender.formatByType(theItem.getTotalAssets(),
					CommonConstants.DEFAULT_VALUE_ZERO, RenderConstants.LONG_STYLE))).append(COMMA);
			buffer.append(LINE_BREAK);

		}
		if (!recordAvailable) {
			buffer.append("There were no results for the current selections. Please try again.");
			return buffer.toString().getBytes();
		}

		return buffer.toString().getBytes();
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return SystematicWithdrawReportData.REPORT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	protected String getReportName() {
		return SystematicWithdrawReportData.REPORT_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	protected Collection doValidate(ActionForm form, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate of SysWDReportAction");
		}
			ArrayList<GenericException> errors = new ArrayList<GenericException>();

		SystematicWithdrawReportForm theForm = (SystematicWithdrawReportForm) form;
		String namePhrase = theForm.getParticipantName();

		List<ValidationError> tempArrayList = new ArrayList<ValidationError>();
		if (FILTER_TASK.equals(getTask(request))) {
			if (StringUtils.isNotBlank(namePhrase)) {
				if ((!(lastNameRErule.validate(LAST_NAME, tempArrayList, namePhrase)))
						|| (namePhrase.length() > GlobalConstants.LAST_NAME_LENGTH_MAXIMUM)) {
					GenericException exception = new GenericException(CommonErrorCodes.LAST_NAME_INVALID);
					errors.add(exception);
				}
			}

			if (!theForm.getSsn().isEmpty()) {
				int ssnErrorCode = CommonErrorCodes.SSN_INVALID;

				if (!(ssnRErule.validate(SSN, tempArrayList, theForm.getSsn()))) {
					GenericException exception = new GenericException(ssnErrorCode);
					errors.add(exception);
				}
			}
		}
		if (!errors.isEmpty()) {
			populateReportForm(theForm, request);
			SessionHelper.setErrorsInSession(request, errors);
			SystematicWithdrawReportData reportData = new SystematicWithdrawReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate of SysWDReportAction");
		}
		return errors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria
	 * (com.manulife.pension.service.report.valueobject.ReportCriteria,
	 * com.manulife.pension.ps.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		SystematicWithdrawReportForm sysWithdrawForms = (SystematicWithdrawReportForm) form;

		String participantSSN;

		FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);

		if (filterCriteriaVo == null) {
			filterCriteriaVo = new FilterCriteriaVo();
		}

		String task = request.getParameter(TASK_KEY);
		if (task == null) {
			task = DEFAULT_TASK;
		}

		criteria.addFilter("TASK", task);
		if (!task.equals(PRINT_TASK)) {
			criteria.setPageSize(getPageSize(request));
		}

		criteria.addFilter("contractNumber", Integer.toString(currentContract.getContractNumber()));

		// If the task is default then reset the page no and sort details that
		// are cached in eligibility tab and deferral tab.
		if (!task.equals(FILTER_TASK)) {

			if (!StringUtils.isEmpty(filterCriteriaVo.getParticipantName())) {
				criteria.addFilter(SystematicWithdrawReportData.FILTER_NAME, filterCriteriaVo.getParticipantName());
				sysWithdrawForms.setParticipantName(filterCriteriaVo.getParticipantName());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getWdType())) {
				criteria.addFilter(SystematicWithdrawReportData.FILTER_WDTYPE,
						wdTypeMap.get(filterCriteriaVo.getWdType()));
				sysWithdrawForms.setWdType(filterCriteriaVo.getWdType());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getWdStatus())) {
				criteria.addFilter(SystematicWithdrawReportData.FILTER_STATUS,
						wdStatusMap.get(filterCriteriaVo.getWdStatus()));
				sysWithdrawForms.setWdStatus(filterCriteriaVo.getWdStatus());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getSsnOne()) && !StringUtils.isEmpty(filterCriteriaVo.getSsnTwo())
					&& !StringUtils.isEmpty(filterCriteriaVo.getSsnThree())) {
				participantSSN = filterCriteriaVo.getSsnOne() + (filterCriteriaVo.getSsnTwo())
						+ filterCriteriaVo.getSsnThree();

				if (!StringUtils.isEmpty(participantSSN))
					criteria.addFilter(SystematicWithdrawReportData.FILTER_SSN, participantSSN);
				sysWithdrawForms.setSsnOne(filterCriteriaVo.getSsnOne());
				sysWithdrawForms.setSsnTwo(filterCriteriaVo.getSsnTwo());
				sysWithdrawForms.setSsnThree(filterCriteriaVo.getSsnThree());
			}

		}

		// If the request is for filter then populate the session object from
		// form bean and populate the filter criterias.
		if (task.equals(FILTER_TASK)) {

			if (!StringUtils.isEmpty(sysWithdrawForms.getParticipantName())) {
				filterCriteriaVo.setParticipantName(sysWithdrawForms.getParticipantName());
				criteria.addFilter(SystematicWithdrawReportData.FILTER_NAME, filterCriteriaVo.getParticipantName());
			} else {
				filterCriteriaVo.setParticipantName(null);
			}

			if (!StringUtils.isEmpty(sysWithdrawForms.getWdType())) {
				filterCriteriaVo.setWdType(sysWithdrawForms.getWdType());
				criteria.addFilter(SystematicWithdrawReportData.FILTER_WDTYPE, filterCriteriaVo.getWdType());
			} else {
				filterCriteriaVo.setWdType(null);
			}

			if (!StringUtils.isEmpty(sysWithdrawForms.getWdStatus())) {
				filterCriteriaVo.setWdStatus(sysWithdrawForms.getWdStatus());
				criteria.addFilter(SystematicWithdrawReportData.FILTER_STATUS, filterCriteriaVo.getWdStatus());
			} else {
				filterCriteriaVo.setWdStatus(null);
			}
			if (!(sysWithdrawForms.getSsn().isEmpty() && StringUtils.isEmpty(sysWithdrawForms.getSsnOne())
					&& StringUtils.isEmpty(sysWithdrawForms.getSsnTwo())
					&& StringUtils.isEmpty(sysWithdrawForms.getSsnThree()))) {
				filterCriteriaVo.setSsnOne(sysWithdrawForms.getSsnOne());
				filterCriteriaVo.setSsnTwo(sysWithdrawForms.getSsnTwo());
				filterCriteriaVo.setSsnThree(sysWithdrawForms.getSsnThree());
				criteria.addFilter(SystematicWithdrawReportData.FILTER_SSN, filterCriteriaVo.getSsn().toString());
			} else {
				filterCriteriaVo.setSsnOne(null);
				filterCriteriaVo.setSsnTwo(null);
				filterCriteriaVo.setSsnThree(null);
			}
			// set filterCriteriaVo back to session
			SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);
		}

		if (!StringUtils.isEmpty(filterCriteriaVo.getParticipantName())
				|| !StringUtils.isEmpty(filterCriteriaVo.getWdType())
				|| !StringUtils.isEmpty(filterCriteriaVo.getWdStatus())
				|| (!StringUtils.isEmpty(filterCriteriaVo.getSsnOne())
						&& !StringUtils.isEmpty(filterCriteriaVo.getSsnTwo())
						&& !StringUtils.isEmpty(filterCriteriaVo.getSsnThree()))) {
			criteria.addFilter("TASK", FILTER_TASK);
		}

	}

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}


}