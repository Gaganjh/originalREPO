/**
 * 
 */
package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.ps.service.report.profiles.reporthandler.BusinessTeamCodeLookup;
import com.manulife.pension.ps.service.report.profiles.reporthandler.UserManagementChangesExternalReportHandler;
import com.manulife.pension.ps.service.report.profiles.valueobject.UserManagementChangesExternalReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.SecurityChangeHistory;

/**
 * @author marcest
 * 
 */
@Controller
@RequestMapping(value = "/profiles")
@SessionAttributes({"userManagementChangesReportForm"})

public class UserManagementChangesExternalReportController extends SecurityReportController {

	@ModelAttribute("userManagementChangesReportForm")
	public UserManagementChangesReportForm populateForm() {
		return new UserManagementChangesReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "redirect:/do/profiles/userManagementChangesExternal/");
		forwards.put("default", "/profiles/userManagementChangesExternal.jsp");
		forwards.put("save", "/profiles/userManagementChangesExternal.jsp");
		forwards.put("sort", "/profiles/userManagementChangesExternal.jsp");
		forwards.put("page", "/profiles/userManagementChangesExternal.jsp");
		forwards.put("print", "/profiles/userManagementChangesExternal.jsp");
		forwards.put("filter", "/profiles/userManagementChangesExternal.jsp");
	}

	public static final String REPORT_NAME_EXTERNAL = "userManagementChangesExternal";
	public static final String REPORT_NAME = "reportName";

	/**
	 * CSV file tile
	 */
	public static final String EXTERNAL_REPORT_TITLE = "John Hancock USA - User management changes - external";

	// SimpleDateFormat is converted to FastDateFormat to make it thread safe
	public static final FastDateFormat DATE_FORMATTER = FastDateFormat.getInstance("MM/dd/yyyy");
	/**
	 * CSV details heading column
	 */
	// Contact Management Removed mail recipient, primary contact column and
	// added a column attribute
	protected static final String EXTERNAL_DOWNLOAD_COLUMN_HEADING = "Team code, JH rep, Contract number, Contract name, Change by, User role, User name, Date of Change, Action, Item, Old value, New value, Attribute";

	private static String dividerValue = "----------------------------";
	private static LabelValueBean listDivider = new LabelValueBean(dividerValue, dividerValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
	 */
	@Override
	protected String getDefaultSort() {
		return "sortByChangeByTeamCode";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
	 */
	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#getDownloadData(com.manulife
	 * .pension.ps.web.report.BaseReportForm,
	 * com.manulife.pension.service.report.valueobject.ReportData,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		UserManagementChangesReportForm form = (UserManagementChangesReportForm) reportForm;

			return getExternalDownloadData(reportForm, report, request);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	@Override
	protected String getReportId() {
		return UserManagementChangesExternalReportHandler.REPORT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * com.manulife.pension.ps.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@RequestMapping(value = "/userManagementChangesExternal/", params = { "task=download" }, method = { RequestMethod.GET })
	public String doDownload(
			@Valid @ModelAttribute("userManagementChangesReportForm") UserManagementChangesReportForm form,
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

			request.setAttribute(REPORT_NAME, REPORT_NAME_EXTERNAL);
			
		return super.doDownload(form, request, response);
	}

	/* This was a validate method in struts code, the body will be called when we have errors to avoid NPE in JSP */
	private void setReportData(HttpServletRequest request) {

			UserManagementChangesExternalReportData report = new UserManagementChangesExternalReportData();
			report.setReportCriteria(new ReportCriteria(
					UserManagementChangesExternalReportHandler.REPORT_ID));
			report.setDetails(new Vector());
			request.setAttribute(Constants.REPORT_BEAN, report);

	}

	@RequestMapping(value = "/userManagementChangesExternal/", method = { RequestMethod.GET, RequestMethod.POST })
	public String doDefault(
			@Valid @ModelAttribute("userManagementChangesReportForm") UserManagementChangesReportForm form,
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
		String forward = super.doDefault(form, request, response);
		
		UserManagementChangesExternalReportData theReport = (UserManagementChangesExternalReportData) request
				.getAttribute(Constants.REPORT_BEAN);
		form.setTotalRecordsInCSV(theReport.getTotalCount());
		
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/userManagementChangesExternal/", params = { "task=filter" }, method = {RequestMethod.POST })
	public String doFilter(
			@Valid @ModelAttribute("userManagementChangesReportForm") UserManagementChangesReportForm form,
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
		String forward = super.doFilter(form, request, response);
		
		UserManagementChangesExternalReportData theReport = (UserManagementChangesExternalReportData) request
				.getAttribute(Constants.REPORT_BEAN);
		form.setTotalRecordsInCSV(theReport.getTotalCount());
		
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/userManagementChangesExternal/", params = { "task=page" }, method = {
			 RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("userManagementChangesReportForm") UserManagementChangesReportForm form,
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

	@RequestMapping(value = "/userManagementChangesExternal/", params = { "task=sort" }, method = {
			 RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("userManagementChangesReportForm") UserManagementChangesReportForm form,
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

	@RequestMapping(value ="/userManagementChangesExternal/", params = { 
			"task=downloadAll" }, method = { RequestMethod.GET })
	public String doDownloadAll(
			@Valid @ModelAttribute("userManagementChangesReportForm") UserManagementChangesReportForm form,
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#getFileName(javax.servlet
	 * .http.HttpServletRequest)
	 */
	protected String getFileName(HttpServletRequest request) {
		return request.getAttribute(REPORT_NAME) + CSV_EXTENSION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	protected String getReportName() {
		// This should never be called but must be implemented
		return REPORT_NAME_EXTERNAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria
	 * (com.manulife.pension.service.report.valueobject.ReportCriteria,
	 * com.manulife.pension.ps.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm actionForm,
			HttpServletRequest request) throws SystemException {
		logger.debug("entry -> populateReportCriteria");

		UserManagementChangesReportForm form = (UserManagementChangesReportForm) actionForm;
		String requestedURL = request.getRequestURI().substring(request.getContextPath().length());
		form.setExternalReport((requestedURL != null && requestedURL.indexOf("External") != -1));

		if (StringUtils.isNotBlank(form.getAction()) && !"All".equals(form.getAction())
				&& !dividerValue.equals(form.getAction())) {
			criteria.addFilter(UserManagementChangesExternalReportData.FILTER_ACTION, form.getAction());
		}
		if (form.getTeamCode() != null && !"All".equals(form.getTeamCode())) {
			if (form.isExternalReport()) {
				criteria.addFilter(UserManagementChangesExternalReportData.FILTER_TEAM_CODE, form.getTeamCode());
			} else {
				criteria.addFilter(UserManagementChangesExternalReportData.FILTER_CHANGED_BY_TEAM_CODE,
						form.getTeamCode());
			}
		}
		if (StringUtils.isNotBlank(form.getContractNumber())) {
			Integer contractNum = new Integer(form.getContractNumber());
			criteria.addFilter(UserManagementChangesExternalReportData.FILTER_CONTRACT_NUMBER, contractNum);
		}
		criteria.addFilter(UserManagementChangesExternalReportData.FILTER_START_DATE, form.getFromDate());
		criteria.addFilter(UserManagementChangesExternalReportData.FILTER_END_DATE, form.getToDate());
		if (StringUtils.isNotBlank(form.getChangedBy())) {
			criteria.addFilter(UserManagementChangesExternalReportData.FILTER_LAST_NAME, form.getChangedBy());
		}
		String selectedRoles = form.getSelectedRoles();
		if (StringUtils.isNotBlank(selectedRoles)) {
			criteria.addFilter(UserManagementChangesExternalReportData.FILTER_ROLES, selectedRoles);
		}
		criteria.addFilter(UserManagementChangesExternalReportData.FILTER_USER_TYPE,
				new Boolean(form.isExternalReport()));

		// Set the default sort
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#populateSortCriteria(
	 * com.manulife.pension.service.report.valueobject.ReportCriteria,
	 * com.manulife.pension.ps.web.report.BaseReportForm)
	 */
	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
		if (StringUtils.isEmpty(form.getSortField()) || getDefaultSort().equals(form.getSortField())) {
			UserManagementChangesReportForm reportForm = (UserManagementChangesReportForm) form;
			if (reportForm.isExternalReport()) {
				criteria.insertSort("sortByTeamCode", form.getSortDirection());
			} else {
				criteria.insertSort("sortByChangeByTeamCode", form.getSortDirection());
				criteria.insertSort("sortByUserName", ReportSort.ASC_DIRECTION);
			}
			criteria.insertSort("sortByContractNumber", ReportSort.ASC_DIRECTION);
			criteria.insertSort("sortByUserRole", ReportSort.DESC_DIRECTION);
			criteria.insertSort("sortByDate", ReportSort.DESC_DIRECTION);
		} else {
			super.populateSortCriteria(criteria, form);
		}
	}

	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}
		super.populateReportForm(reportForm, request);

		UserManagementChangesReportForm form = (UserManagementChangesReportForm) reportForm;
		form.setTeamCodeList(buildTeamCodeList());
		if (form.getTeamCode() == null) {
			form.setTeamCode(form.getTeamCodeList().get(0).getValue());
		}

		form.setInternalActionList(buildInternalActionList());
		form.setExternalActionList(buildExternalActionList());
		if (StringUtils.isEmpty(form.getAction()) || dividerValue.equals(form.getAction())) {
			form.setAction(form.getActionList().get(0).getValue());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportForm");
		}

	}

	private List<LabelValueBean> buildTeamCodeList() {
		List<LabelValueBean> codes = new ArrayList<LabelValueBean>();
		codes.add(new LabelValueBean("All", "All"));
		codes.add(new LabelValueBean("No team code", ""));
		try {
			Collection<String> businessTeamCodes = BusinessTeamCodeLookup.getBusinessTeamCodes();
			for (String businessTeamCode : businessTeamCodes) {
				codes.add(new LabelValueBean(businessTeamCode, businessTeamCode));
			}
		} catch (SystemException se) {
			logger.error(se);
		}
		return codes;
	}

	private List<LabelValueBean> buildInternalActionList() {
		List<LabelValueBean> internalActionList = new ArrayList<LabelValueBean>();
		internalActionList.add(new LabelValueBean("All", "All"));
		internalActionList.addAll(buildClientActionList());
		internalActionList.add(listDivider);
		internalActionList.add(new LabelValueBean("Business conversion", "BCL"));
		// Contact Management added additional action RSG
		internalActionList.add(new LabelValueBean("Remove staging", "RSG"));
		internalActionList.add(listDivider);
		internalActionList.add(new LabelValueBean("Add TPA firm", "ATF"));
		internalActionList.add(new LabelValueBean("Delete TPA firm", "DTF"));
		internalActionList.add(listDivider);
		internalActionList.add(new LabelValueBean("Add TPA user", "ATP"));
		internalActionList.addAll(buildTPAActionList());
		// Internal user Management added additional action UDC
		internalActionList.add(listDivider);
		internalActionList.add(new LabelValueBean("Update Contract", "UDC"));
		return internalActionList;
	}

	private List<LabelValueBean> buildExternalActionList() {
		List<LabelValueBean> externalActionList = new ArrayList<LabelValueBean>();
		externalActionList.add(new LabelValueBean("All", "All"));
		externalActionList.addAll(buildClientActionList());
		externalActionList.add(listDivider);
		externalActionList.addAll(buildTPAActionList());
		return externalActionList;
	}

	private List<LabelValueBean> buildClientActionList() {
		List<LabelValueBean> clientActionList = new ArrayList<LabelValueBean>();
		clientActionList.add(new LabelValueBean("Add client", "ACL"));
		clientActionList.add(new LabelValueBean("Edit client", "ECL"));
		clientActionList.add(new LabelValueBean("Delete client", "DCL"));
		clientActionList.add(new LabelValueBean("Suspend client", "SCL"));
		clientActionList.add(new LabelValueBean("Unsuspend client", "UCL"));
		return clientActionList;
	}

	private List<LabelValueBean> buildTPAActionList() {
		List<LabelValueBean> tpaActionList = new ArrayList<LabelValueBean>();
		tpaActionList.add(new LabelValueBean("Edit TPA user", "ETP"));
		tpaActionList.add(new LabelValueBean("Delete TPA user", "DTP"));
		tpaActionList.add(new LabelValueBean("Suspend TPA user", "STP"));
		tpaActionList.add(new LabelValueBean("Unsuspend TPA user", "UTP"));
		return tpaActionList;
	}

		
	@Autowired
	private UserManagementChangesReportValidator userManagementChangesReportValidator;
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	  @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(userManagementChangesReportValidator);
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


	// get external download report data
	private byte[] getExternalDownloadData(BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) throws SystemException {
		UserManagementChangesReportForm form = (UserManagementChangesReportForm) reportForm;

		// find the contract sort code

		StringBuffer buffer = new StringBuffer();

		// As of and total count
		buffer.append(EXTERNAL_REPORT_TITLE).append(LINE_BREAK);

		// filters used

		if (!StringUtils.isEmpty(form.getContractNumber())) {
			buffer.append("Contract number,").append(form.getContractNumber()).append(LINE_BREAK);
		}

		if (!StringUtils.isEmpty(form.getAction())) {
			buffer.append("Action,").append(form.getAction()).append(LINE_BREAK);
		}

		List<String> selectedRoles = form.getSelectedRoleList();
		if (!selectedRoles.isEmpty()) {
			buffer.append("User role,").append(getSelectedRolesForDisplay(selectedRoles)).append(LINE_BREAK);
		}

		if (!StringUtils.isEmpty(form.getTeamCode())) {
			buffer.append("Team code,").append(form.getTeamCode()).append(LINE_BREAK);
		}

		if (!StringUtils.isEmpty(form.getFromDate())) {
			buffer.append("From date,").append(form.getFromDate().trim()).append(LINE_BREAK);
		}

		if (!StringUtils.isEmpty(form.getToDate())) {
			buffer.append("To date,").append(form.getToDate().trim()).append(LINE_BREAK);
		}

		// heading and records
		buffer.append(LINE_BREAK);
		buffer.append(EXTERNAL_DOWNLOAD_COLUMN_HEADING);
		buffer.append(LINE_BREAK);

		Iterator iterator = report.getDetails().iterator();
		while (iterator.hasNext()) {
			buffer.append(LINE_BREAK);
			SecurityChangeHistory theItem = (SecurityChangeHistory) iterator.next();
			buffer.append(theItem.getTeamCode()).append(COMMA);
			buffer.append(escapeField(theItem.getJhRepName())).append(COMMA);
			buffer.append(theItem.getContractNumber()).append(COMMA);
			buffer.append(escapeField(theItem.getContractName())).append(COMMA);
			buffer.append(theItem.getChangedByFirstName() + " " + theItem.getChangedByLastName()).append(COMMA);
			StringBuffer roleBuffer = new StringBuffer(theItem.getUserRole().getDisplayName());
			if (theItem.getRoleType() != null) {
				roleBuffer.append(" (").append(theItem.getRoleType().getDisplayName()).append(")");
			}
			buffer.append(escapeField(roleBuffer.toString())).append(COMMA);
			buffer.append(theItem.getFirstName() + " " + theItem.getLastName()).append(COMMA);
			buffer.append(DATE_FORMATTER.format(theItem.getCreatedTs()).trim()).append(COMMA);
			buffer.append(theItem.getActionName()).append(COMMA);
			buffer.append(theItem.getItemName()).append(COMMA);
			buffer.append(escapeField(theItem.getOldValueDisplayName())).append(COMMA);
			buffer.append(escapeField(theItem.getNewValueDisplayName())).append(COMMA);
			buffer.append(escapeField(theItem.getAttribute())).append(COMMA);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();

	}

	private String getSelectedRolesForDisplay(List<String> selectedRoleList) {
		StringBuffer roleCodes = new StringBuffer();
		for (String roleCode : selectedRoleList) {
			if (roleCodes.length() > 0) {
				roleCodes.append(",");
			}
			roleCodes.append(SecurityChangeHistory.roleCodeMap.get(roleCode));
		}
		return roleCodes.toString();
	}

}
