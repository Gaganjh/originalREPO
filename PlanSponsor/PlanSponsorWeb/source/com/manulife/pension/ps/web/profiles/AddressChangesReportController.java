
package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
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
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.ps.service.report.profiles.reporthandler.AddressChangesExternalReportHandler;
import com.manulife.pension.ps.service.report.profiles.reporthandler.BusinessTeamCodeLookup;
import com.manulife.pension.ps.service.report.profiles.valueobject.AddressChangesExternalReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.SecurityChangeHistory;

/**
 * @author Ranjith
 *
 */
@Controller
@RequestMapping(value = "/profiles")
@SessionAttributes({ "addressChangesReportForm" })

public class AddressChangesReportController extends SecurityReportController {

	@ModelAttribute("addressChangesReportForm")
	public AddressChangesReportForm populateForm() {
		return new AddressChangesReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/profiles/addressChangesInternal.jsp");
		forwards.put("default", "/profiles/addressChangesInternal.jsp");
		forwards.put("save", "/profiles/addressChangesInternal.jsp");
		forwards.put("sort", "/profiles/addressChangesInternal.jsp");
		forwards.put("page", "/profiles/addressChangesInternal.jsp");
		forwards.put("print", "/profiles/addressChangesInternal.jsp");
		forwards.put("filter", "/profiles/addressChangesInternal.jsp");
	}

	public static final String REPORT_NAME_INTERNAL = "addressChangesReport";
	public static final String REPORT_NAME_EXTERNAL = "addressChangesExternal";
	public static final String REPORT_NAME = "reportName";

	/**
	 * CSV file tile
	 */
	public static final String INTERNAL_REPORT_TITLE = "John Hancock USA - User management changes - internal";
	public static final String EXTERNAL_REPORT_TITLE = "John Hancock USA - User management changes - external";

	public static final FastDateFormat DATE_FORMATTER = ContractDateHelper.getDateFormatter("MM/dd/yyyy");
	/**
	 * CSV details heading column
	 */
	protected static final String INTERNAL_DOWNLOAD_COLUMN_HEADING = "Changed by team code, Changed by, Contract number, Contract name, Team code, JH rep,  Date of Change, Item, Old value, New value";
	// protected static final String EXTERNAL_DOWNLOAD_COLUMN_HEADING = "Team code,
	// JH rep, Contract number, Contract name, Change by, User role, User name, Date
	// of Change, Action, Item, Old value, New value, Primary contact, Mail
	// recipient";

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
	 * com.manulife.pension.ps.web.report.ReportAction#getDownloadData(com.manulife.
	 * pension.ps.web.report.BaseReportForm,
	 * com.manulife.pension.service.report.valueobject.ReportData,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		// AddressChangesReportForm form = (AddressChangesReportForm)
		// reportForm;

		// if (!form.isExternalReport()){
		return getInternalDownloadData(reportForm, report, request);
		// } else {
		// return getExternalDownloadData(reportForm, report, request);
		// }

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	@Override
	protected String getReportId() {
		return AddressChangesExternalReportHandler.REPORT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@RequestMapping(value = "/addressChangesInternal/", params = { "task=download" }, method = {
			RequestMethod.GET })
	public String doDownload(
			@Valid @ModelAttribute("addressChangesReportForm") AddressChangesReportForm actionForm,
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

		/*
		 * if (form.isExternalReport()) { request.setAttribute(REPORT_NAME,
		 * REPORT_NAME_EXTERNAL); } else {
		 */
		request.setAttribute(REPORT_NAME, REPORT_NAME_INTERNAL);
		// }
		return super.doDownload(actionForm, request, response);
	}

	@RequestMapping(value = "/addressChangesInternal/", method = { RequestMethod.POST, RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("addressChangesReportForm") AddressChangesReportForm form,
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
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/addressChangesInternal/", params = { "task=filter" }, method = {
			RequestMethod.POST })
	public String doFilter(@Valid @ModelAttribute("addressChangesReportForm") AddressChangesReportForm form,
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
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/addressChangesInternal/", params = { "task=page" }, method = {
			 RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("addressChangesReportForm") AddressChangesReportForm form,
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

	@RequestMapping(value = "/addressChangesInternal/", params = { "task=sort" }, method = {
			RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("addressChangesReportForm") AddressChangesReportForm form,
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

	@RequestMapping(value = "/addressChangesInternal/", params = { 
			"task=downloadAll" }, method = {  RequestMethod.GET })
	public String doDownloadAll(@Valid @ModelAttribute("addressChangesReportForm") AddressChangesReportForm form,
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
	 * com.manulife.pension.ps.web.report.ReportAction#getFileName(javax.servlet.
	 * http.HttpServletRequest)
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
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria(com.
	 * manulife.pension.service.report.valueobject.ReportCriteria,
	 * com.manulife.pension.ps.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm actionForm,
			HttpServletRequest request) throws SystemException {
		logger.debug("entry -> populateReportCriteria");

		AddressChangesReportForm form = (AddressChangesReportForm) actionForm;
		String requestedURL = request.getRequestURI().substring(request.getContextPath().length());
		// form.setExternalReport( (requestedURL != null &&
		// requestedURL.indexOf("External") != -1) );

		criteria.addFilter(AddressChangesExternalReportData.FILTER_ACTION, "ADC");

		if (form.getTeamCode() != null && !"All".equals(form.getTeamCode())) {
			// if (form.isExternalReport()) {
			// criteria.addFilter(AddressChangesExternalReportData.FILTER_TEAM_CODE,
			// form.getTeamCode() );
			// } else {
			criteria.addFilter(AddressChangesExternalReportData.FILTER_CHANGED_BY_TEAM_CODE, form.getTeamCode());
			// }
		}
		if (StringUtils.isNotBlank(form.getContractNumber())) {
			Integer contractNum = new Integer(form.getContractNumber());
			criteria.addFilter(AddressChangesExternalReportData.FILTER_CONTRACT_NUMBER, contractNum);
		}
		criteria.addFilter(AddressChangesExternalReportData.FILTER_START_DATE, form.getFromDate());
		criteria.addFilter(AddressChangesExternalReportData.FILTER_END_DATE, form.getToDate());
		if (StringUtils.isNotBlank(form.getChangedBy())) {
			criteria.addFilter(AddressChangesExternalReportData.FILTER_LAST_NAME, form.getChangedBy());
		}
		if (StringUtils.isNotBlank(form.getUserType()) && "Internal".equalsIgnoreCase(form.getUserType())) {
			criteria.addFilter(AddressChangesExternalReportData.FILTER_USER_TYPE, "IU");
		} else if (StringUtils.isNotBlank(form.getUserType()) && "External".equalsIgnoreCase(form.getUserType())) {
			criteria.addFilter(AddressChangesExternalReportData.FILTER_USER_TYPE, "EU");
		} else {
			criteria.addFilter(AddressChangesExternalReportData.FILTER_USER_TYPE, "AL");
		}
		// TODO need to analyze this section for internal and external users filter.
		// criteria.addFilter(AddressChangesExternalReportData.FILTER_USER_TYPE, new
		// Boolean(false));
		// Set the default sort
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
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
			AddressChangesReportForm reportForm = (AddressChangesReportForm) form;
			// if (reportForm.isExternalReport()) {
			// criteria.insertSort("sortByTeamCode", form.getSortDirection());
			// } else {
			criteria.insertSort("sortByChangeByTeamCode", form.getSortDirection());
			criteria.insertSort("sortByUserName", ReportSort.ASC_DIRECTION);
			// }
			criteria.insertSort("sortByContractNumber", ReportSort.ASC_DIRECTION);
			criteria.insertSort("sortByItem", ReportSort.ASC_DIRECTION);
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

		AddressChangesReportForm form = (AddressChangesReportForm) reportForm;
		form.setTeamCodeList(buildTeamCodeList());
		if (form.getTeamCode() == null) {
			form.setTeamCode(form.getTeamCodeList().get(0).getValue());
		}
		form.setUserTypeList(buildUserTypeList());
		if (form.getUserType() == null) {
			form.setUserType(form.getUserTypeList().get(1).getValue());
		}
		if (form.getTeamCode() == null) {
			form.setTeamCode(form.getTeamCodeList().get(1).getValue());
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

	/**
	 * Checks whether we're in the right state.
	 * 
	 */
	private void setReportData(HttpServletRequest request) {

			AddressChangesExternalReportData report = new AddressChangesExternalReportData();
			report.setReportCriteria(new ReportCriteria(AddressChangesExternalReportHandler.REPORT_ID));
			report.setDetails(new Vector());
			request.setAttribute(Constants.REPORT_BEAN, report);
	}

	@Autowired
	private AddressChangesReportValidator addressChangesReportValidator;
	

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
		binder.addValidators(addressChangesReportValidator);
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

	// get internal download report data
	private byte[] getInternalDownloadData(BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) throws SystemException {
		AddressChangesReportForm form = (AddressChangesReportForm) reportForm;

		// find the contract sort code

		StringBuffer buffer = new StringBuffer();

		// As of and total count
		buffer.append(INTERNAL_REPORT_TITLE).append(LINE_BREAK);

		// filters used

		if (!StringUtils.isEmpty(form.getChangedBy())) {
			buffer.append("Changed by (last name),").append(form.getContractNumber()).append(LINE_BREAK);
		}

		if (!StringUtils.isEmpty(form.getContractNumber())) {
			buffer.append("Contract number,").append(form.getContractNumber()).append(LINE_BREAK);
		}

		if (!StringUtils.isEmpty(form.getUserType())) {
			buffer.append("User role,").append(form.getUserType()).append(LINE_BREAK);
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
		buffer.append(INTERNAL_DOWNLOAD_COLUMN_HEADING);
		buffer.append(LINE_BREAK);

		Iterator iterator = report.getDetails().iterator();
		while (iterator.hasNext()) {
			buffer.append(LINE_BREAK);
			SecurityChangeHistory theItem = (SecurityChangeHistory) iterator.next();
			buffer.append(theItem.getChangedByTeamCode()).append(COMMA);
			buffer.append(theItem.getChangedByFirstName() + " " + theItem.getChangedByLastName()).append(COMMA);
			buffer.append(theItem.getContractNumber() == 0 ? "" : theItem.getContractNumber()).append(COMMA);
			buffer.append(theItem.getContractName() == null ? "" : escapeField(theItem.getContractName()))
					.append(COMMA);
			buffer.append(theItem.getTeamCode()).append(COMMA);
			buffer.append(escapeField(theItem.getJhRepName())).append(COMMA);
			/*
			 * StringBuffer roleBuffer = new
			 * StringBuffer(theItem.getUserRole().getDisplayName()); if
			 * (theItem.getRoleType() != null) {
			 * roleBuffer.append(" (").append(theItem.getRoleType().getDisplayName()).append
			 * (")"); } buffer.append(escapeField(roleBuffer.toString())).append(COMMA);
			 * buffer.append(theItem.getFirstName()+ " " +
			 * theItem.getLastName()).append(COMMA);
			 */
			buffer.append(DATE_FORMATTER.format(theItem.getCreatedTs()).trim()).append(COMMA);
			buffer.append(theItem.getItemName()).append(COMMA);
			buffer.append(getFormattedAddress(theItem.getOldValueDisplayName())).append(COMMA);
			buffer.append(getFormattedAddress(theItem.getNewValueDisplayName())).append(COMMA);

		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();

	}

	/**
	 * @param addressValue
	 * @return
	 */
	private String getFormattedAddress(String addressValue) {
		StringTokenizer tokens = new StringTokenizer(addressValue, "~");
		StringBuffer formattedAddress = new StringBuffer();
		formattedAddress.append("\"");
		int tokenCount = 0;
		while (tokens.hasMoreTokens()) {
			if (tokenCount > 0) {
				formattedAddress.append("\n");
			}
			tokenCount++;
			formattedAddress.append(tokens.nextElement());
		}
		formattedAddress.append("\"");
		return formattedAddress.toString();
	}

	/**
	 * @return
	 */
	private List<LabelValueBean> buildUserTypeList() {
		List<LabelValueBean> userTypeList = new ArrayList<LabelValueBean>();
		userTypeList.add(new LabelValueBean("All", "All"));
		userTypeList.add(new LabelValueBean("Internal", "Internal"));
		userTypeList.add(new LabelValueBean("External", "External"));
		return userTypeList;

	}

	
	/*
	 * avoids token generation as this class acts as intermediate for many
	 * transactions.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired(java.
	 * lang.String)
	 
	@Override
	protected boolean isTokenRequired(String action) {
		return true;
	}

	
	 * Returns true if token has to be validated for the particular action call to
	 * avoid CSRF vulnerability else false. (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseAction#
	 * isTokenValidatorEnabled(java.lang.String)
	 
	@Override
	protected boolean isTokenValidatorEnabled(String action) {
		// avoids methods from validation which ever is not required
		if (StringUtils.isNotEmpty(action) && (StringUtils.contains(action, "Default")
				|| StringUtils.contains(action, "default") || StringUtils.contains(action, "Validate")
				|| StringUtils.contains(action, "Print") || StringUtils.contains(action, "Download")
				|| StringUtils.contains(action, "Page") || StringUtils.contains(action, "Sort")
				|| StringUtils.contains(action, "Reset") || StringUtils.contains(action, "Continue"))) {
			return false;
		}
		return true;
	}*/
}
