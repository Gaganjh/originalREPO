package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.ps.service.report.profiles.reporthandler.BusinessTeamCodeLookup;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.report.valueobject.SecurityRoleConversionDetails;
import com.manulife.pension.service.security.report.valueobject.SecurityRoleConversionReportData;

/**
 * Security role conversion report action
 * 
 * @author Steven
 */
@Controller
@RequestMapping(value = "/profiles")
@SessionAttributes({ "securityRoleConversionReportForm" })

public class SecurityRoleConversionReportController extends SecurityReportController {
	@ModelAttribute("securityRoleConversionReportForm")
	public SecurityRoleConversionReportForm populateForm() {
		return new SecurityRoleConversionReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/profiles/securityRoleConversionReport.jsp");
		forwards.put("default", "/profiles/securityRoleConversionReport.jsp");
		forwards.put("save", "/profiles/securityRoleConversionReport.jsp");
		forwards.put("sort", "/profiles/securityRoleConversionReport.jsp");
		forwards.put("page", "/profiles/securityRoleConversionReport.jsp");
		forwards.put("print", "/profiles/securityRoleConversionReport.jsp");
		forwards.put("filter", "/profiles/securityRoleConversionReport.jsp");
	}

	/**
	 * CSV file tile
	 */
	public static final String REPORT_TITLE = "John Hancock USA - Security role conversion";

	/**
	 * CSV details heading column
	 */
	protected static final String DOWNLOAD_COLUMN_HEADING = "Teamcode,JH Rep, Contract number, Contract Name, Conversion indicator, Date of Change, TPA firm ID, TPA firm name, Number of client contacts";

	/**
	 * Constructor.
	 */
	public SecurityRoleConversionReportController() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param className
	 */
	public SecurityRoleConversionReportController(Class className) {
		super(className);
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportId()
	 */
	protected String getReportId() {
		return SecurityRoleConversionReportData.REPORT_ID;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportName()
	 */
	protected String getReportName() {
		return SecurityRoleConversionReportData.REPORT_NAME;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return SecurityRoleConversionReportData.SORT_FIELD_TEAM_CODE;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * common task for security role conversion task
	 */

	@RequestMapping(value ="/securityRoleConversionReport/", method = {
			RequestMethod.POST, RequestMethod.GET })
	protected String doDefault(
			@Valid @ModelAttribute("securityRoleConversionReportForm") SecurityRoleConversionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			setReportData(request);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}else{
				return forwards.get("input");
			}
		}

		String forward = super.doCommon(form, request, response);

		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/securityRoleConversionReport/", params = { "task=filter" }, method = {
			RequestMethod.POST })
	public String doFilter(
			@Valid @ModelAttribute("securityRoleConversionReportForm") SecurityRoleConversionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		populateReportForm(form, request);
		if (bindingResult.hasErrors()) {
			setReportData(request);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}else{
				return forwards.get("input");
			}
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/securityRoleConversionReport/", params = { "task=page" }, method = {
			 RequestMethod.GET })
	public String doPage(
			@Valid @ModelAttribute("securityRoleConversionReportForm") SecurityRoleConversionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
			if (bindingResult.hasErrors()) {
			setReportData(request);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}else{
				return forwards.get("input");
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/securityRoleConversionReport/", params = {  "task=sort" }, method = {
			 RequestMethod.GET })
	public String doSort(
			@Valid @ModelAttribute("securityRoleConversionReportForm") SecurityRoleConversionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			setReportData(request);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}else{
				return forwards.get("input");
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/securityRoleConversionReport/", params = { 
			"task=download" }, method = { RequestMethod.GET })
	public String doDownload(
			@Valid @ModelAttribute("securityRoleConversionReportForm") SecurityRoleConversionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			setReportData(request);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}else{
				return forwards.get("input");
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/securityRoleConversionReport/", params = { 
			"task=dowanloadAll" }, method = { RequestMethod.GET })
	public String doDownloadAll(
			@Valid @ModelAttribute("securityRoleConversionReportForm") SecurityRoleConversionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			setReportData(request);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}else{
				return forwards.get("input");
			}
		}
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * Populate report action
	 */
	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}
		super.populateReportForm(reportForm, request);
		SecurityRoleConversionReportForm form = (SecurityRoleConversionReportForm) reportForm;
		if (getTask(request).equals(DEFAULT_TASK)) {
			form.setFromDate(getPreviousDay());
			form.setToDate(getPreviousDay());
			form.setTeamCode("All");
			form.setContractNumber(null);

		}

		// TODO: need code table service for team code.
		form.setTeamCodeList(buildTeamCodeList());

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportForm");
		}

	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#populateDownloadData(java.io.PrintWriter,
	 *      com.manulife.pension.ps.web.report.BaseReportForm,
	 *      com.manulife.pension.service.report.valueobject.ReportData,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		SecurityRoleConversionReportForm form = (SecurityRoleConversionReportForm) reportForm;

		// find the contract sort code

		StringBuffer buffer = new StringBuffer();

		// As of and total count
		buffer.append(REPORT_TITLE).append(LINE_BREAK);

		// filters used
		if (!StringUtils.isEmpty(form.getContractNumber())) {
			buffer.append("Contract number,").append(form.getContractNumber()).append(LINE_BREAK);
		}

		if (!StringUtils.isEmpty(form.getFromDate())) {
			buffer.append("From date:").append(form.getFromDate()).append(LINE_BREAK);
		}

		if (!StringUtils.isEmpty(form.getToDate())) {
			buffer.append("To date:").append(form.getToDate()).append(LINE_BREAK);
		}

		if (!StringUtils.isEmpty(form.getTeamCode())) {
			buffer.append("Team code").append(form.getTeamCode()).append(LINE_BREAK);
		}

		// heading and records
		buffer.append(LINE_BREAK);
		buffer.append(DOWNLOAD_COLUMN_HEADING);
		buffer.append(LINE_BREAK);

		Iterator iterator = report.getDetails().iterator();
		while (iterator.hasNext()) {
			buffer.append(LINE_BREAK);
			SecurityRoleConversionDetails theItem = (SecurityRoleConversionDetails) iterator.next();

			buffer.append(theItem.getTeamCode()).append(COMMA);
			buffer.append(escapeField(theItem.getTeamName())).append(COMMA);
			buffer.append(theItem.getContractNumber()).append(COMMA);
			buffer.append(escapeField(theItem.getContractName())).append(COMMA);
			buffer.append(theItem.getConversionIndicator()).append(COMMA);
			buffer.append(theItem.getChangeDate()).append(COMMA);
			buffer.append(theItem.getTpaFirmId() == null ? "" : theItem.getTpaFirmId()).append(COMMA);
			buffer.append(escapeField(theItem.getTpaFirmName())).append(COMMA);
			buffer.append(theItem.getClientContactNumber()).append(COMMA);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria,
	 *      com.manulife.pension.ps.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		SecurityRoleConversionReportForm srcForm = (SecurityRoleConversionReportForm) form;

		if (!StringUtils.isEmpty(srcForm.getContractNumber())) {
			criteria.addFilter(SecurityRoleConversionReportData.FILTER_CONTRACT_NUMBER, srcForm.getContractNumber());
		}

		if (!StringUtils.isEmpty(srcForm.getFromDate())) {
			try {
				criteria.addFilter(SecurityRoleConversionReportData.FILTER_FROM_DATE,
						SecurityReportController.dateParser(srcForm.getFromDate()));
			} catch (ParseException pe) {
				if (logger.isDebugEnabled()) {
					logger.debug(
							"ParseException in fromDate populateReportCriteria() SecurityRoleConversionReportAction:",
							pe);
				}
			}
		}

		if (!StringUtils.isEmpty(srcForm.getToDate())) {
			try {
				criteria.addFilter(SecurityRoleConversionReportData.FILTER_TO_DATE,
						SecurityReportController.dateParser(srcForm.getToDate()));
			} catch (ParseException pe) {
				if (logger.isDebugEnabled()) {
					logger.debug(
							"ParseException in toDate populateReportCriteria() SecurityRoleConversionReportAction:",
							pe);
				}
			}
		}

		if (!StringUtils.isEmpty(srcForm.getTeamCode())) {
			criteria.addFilter(SecurityRoleConversionReportData.FILTER_TEAM_CODE, srcForm.getTeamCode());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#populateSortCriteria(com.
	 * manulife.pension.service.report.valueobject.ReportCriteria,
	 * com.manulife.pension.ps.web.report.BaseReportForm)
	 */
	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
		if (StringUtils.isEmpty(form.getSortField()) || getDefaultSort().equals(form.getSortField())) {
			criteria.insertSort(getDefaultSort(), form.getSortDirection());
			criteria.insertSort(SecurityRoleConversionReportData.SORT_FIELD_CONTRACT_NUMBER, ReportSort.ASC_DIRECTION);
		} else {
			super.populateSortCriteria(criteria, form);
		}
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportData(java.lang.String,
	 *      com.manulife.pension.service.report.valueobject.ReportCriteria,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected ReportData getReportData(String reportId, ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		ReportServiceDelegate service = ReportServiceDelegate.getInstance();
		ReportData bean = service.getReportData(reportCriteria);
		// ReportData bean = getMockedSecurityRoleReportReportData(1, request);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}

		return bean;

	}

	/**
	 * Checks whether we're in the right state.
	 * 
	 */
	private void setReportData(HttpServletRequest request) {

			SecurityRoleConversionReportData report = new SecurityRoleConversionReportData();
			request.setAttribute(Constants.REPORT_BEAN, report);
	}

	
	@Autowired 
	SecurityRoleConversionReportValidator securityRoleConversionReportValidator;
	
	@Autowired
	private PSValidatorFWInput  psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(securityRoleConversionReportValidator);
	}

	/**
	 * Dummy data for web tier developing
	 * 
	 * @param pageNumber
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	private SecurityRoleConversionReportData getMockedSecurityRoleReportReportData(int pageNumber,
			HttpServletRequest request) throws SystemException {
		SecurityRoleConversionReportData data = new SecurityRoleConversionReportData();
		// data.setDetails(buildDetails(pageNumber));
		data.setTotalCount(20);
		ReportCriteria criteria = new ReportCriteria(getReportId());
		criteria.setPageSize(DEFAULT_PAGE_SIZE);
		criteria.setPageNumber(pageNumber);
		data.setReportCriteria(criteria);

		return data;
	}

	private Contract getContract(String contractNumber) throws ContractNotExistException, SystemException {
		Contract contract = null;
		contract = ContractServiceDelegate.getInstance().getContractDetails(Integer.valueOf(contractNumber).intValue(),
				6);
		if (contract != null && contract.getCompanyName() == null) {
			contract = null;
		}
		return contract;
	}

	/**
	 * Determine whether a given contract has a valid status for conversion.
	 * 
	 * @param contract
	 * @return true if the given contract has a valid status for conversion
	 */
	private boolean isValidContractStatus(Contract contract) {
		return (Contract.STATUS_PROPOSAL_SIGNED.equals(contract.getStatus())
				|| Contract.STATUS_DETAILS_COMPLETED.equals(contract.getStatus())
				|| Contract.STATUS_PENDING_CONTRACT_APPROVAL.equals(contract.getStatus())
				|| Contract.STATUS_CONTRACT_APPROVED.equals(contract.getStatus())
				|| Contract.STATUS_ACTIVE_CONTRACT.equals(contract.getStatus())
				|| Contract.STATUS_CONTRACT_FROZEN.equals(contract.getStatus()));
	}

	// dummy data for team code list
	private List<LabelValueBean> buildTeamCodeList() {
		List<LabelValueBean> codes = new ArrayList<LabelValueBean>();
		codes.add(new LabelValueBean("All", "All"));
		for (String code : getBusinessTeamCodes()) {
			codes.add(new LabelValueBean(code, code));
		}
		return codes;
	}

	private Collection<String> getBusinessTeamCodes() {
		Collection<String> codes = null;

		try {
			codes = BusinessTeamCodeLookup.getBusinessTeamCodes();
		} catch (SystemException se) {
			logger.error(se);
		}
		return codes;
	}

	/**
	 * Get previous day
	 * 
	 * @return
	 */
	private String getPreviousDay() {
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(System.currentTimeMillis());
		cl.add(Calendar.DAY_OF_MONTH, -1);
		return SecurityReportController.dateFormatter(new Date(cl.getTimeInMillis()));
	}

	private String escapeField(String field) {
		if (field == null) {
			return "";
		} else if (field.indexOf(",") != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else {
			return field;
		}
	}
}