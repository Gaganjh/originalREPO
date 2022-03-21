package com.manulife.pension.bd.web.usermanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringEscapeUtils;
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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWExtUser;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.service.security.bd.report.valueobject.BDExtUserSearchData;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

/**
 * The report action for search external users
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping(value = "/usermanagement")
@SessionAttributes({ "searchExtUserForm", "userManagementDispatchForm", "createRiaUserForm" })

public class ExtUserSearchReportController extends BDReportController {
	@ModelAttribute("searchExtUserForm")
	public ExtUserSearchReportForm populateForm() {
		return new ExtUserSearchReportForm();
	}

	@ModelAttribute("userManagementDispatchForm")
	public UserManagementDispatchForm populatemanagForm() {
		return new UserManagementDispatchForm();
	}

	@ModelAttribute("createRiaUserForm")
	public CreateRiaUserForm populateRiaForm() {
		return new CreateRiaUserForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	private static ReportSortList DefaultSort = new ReportSortList();
	static {
		forwards.put("input", "/usermanagement/searchUser.jsp");
		forwards.put("default", "/usermanagement/searchUser.jsp");
		forwards.put("page", "/usermanagement/searchUser.jsp");
		forwards.put("refresh", "/usermanagement/searchUser.jsp");
		forwards.put("sort", "/usermanagement/searchUser.jsp");
		forwards.put("filter", "/usermanagement/searchUser.jsp");
		forwards.put("print", "/usermanagement/searchUser.jsp");
		forwards.put("exception", "/usermanagement/searchUser.jsp");
		
		DefaultSort.add(new ReportSort(BDExtUserSearchData.SORT_LAST_NAME, ReportSort.ASC_DIRECTION));
		DefaultSort.add(new ReportSort(BDExtUserSearchData.SORT_FIRST_NAME, ReportSort.ASC_DIRECTION));
	}

	private final RegularExpressionRule nameRErule = new RegularExpressionRule(BDErrorCodes.USER_SEARCH_INPUT_INVALID,
			BDRuleConstants.FIRST_NAME_LAST_NAME_RE);

	public ExtUserSearchReportController() {
		super(ExtUserSearchReportController.class);
	}

	@RequestMapping(value = "/search", method = { RequestMethod.POST })
	public String execute(ExtUserSearchReportForm form, BindingResult bindingResult, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, SystemException {
		if(request.getAttribute("penTestFlag")!=null && (Boolean)request.getAttribute("penTestFlag")){
			return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
		}
		ControllerForward forward = new ControllerForward("refresh",
				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
		return "redirect:" + forward.getPath();
	}

	@RequestMapping(value = "/search", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("searchExtUserForm") ExtUserSearchReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(request.getAttribute("penTestFlag")!=null && (Boolean)request.getAttribute("penTestFlag")){
			return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("exception");// if input forward not
													// //available, provided
													// default
			}
		}
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/search", params = { "task=filter" }, method = { RequestMethod.GET })
	public String doFilter(@Valid @ModelAttribute("searchExtUserForm") ExtUserSearchReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(request.getAttribute("penTestFlag")!=null && (Boolean)request.getAttribute("penTestFlag")){
			return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("exception");
			}
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * Refresh the current search result
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/search", params = { "task=refresh" }, method = { RequestMethod.GET })
	public String doRefresh(@Valid @ModelAttribute("searchExtUserForm") ExtUserSearchReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(request.getAttribute("penTestFlag")!=null && (Boolean)request.getAttribute("penTestFlag")){
			return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("exception");
			}
		}

		String forward = doCommon(form, request, response);

		return forwards.get(forward);
	}

	@RequestMapping(value = "/search", params = { "task=page" }, method = { RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("searchExtUserForm") ExtUserSearchReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("exception");// if input forward not
													// //available, provided
													// default
			}
		}

		return forwards.get(super.doPage(form, request, response));

	}

	/**
	 * Refresh the current search result and display the exception's error
	 * message
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/search", params = { "task=exception" }, method = { RequestMethod.GET })
	public String doException(@Valid @ModelAttribute("searchExtUserForm") ExtUserSearchReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("exception");
			}
		}
		String forward = doCommon(form, request, response);

		SecurityServiceException e = UserManagementHelper.getUserManagementException(request);
		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e,
				UserManagementHelper.UserManagementSecurityServiceExceptionMapping)));
		setErrorsInSession(request, errors);

		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	protected String doCommon(BaseReportForm reportForm, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		ExtUserSearchReportForm f = (ExtUserSearchReportForm) reportForm;
		// BDUserProfile profile = BDSessionHelper.getUserProfile(request);
		// String userType = profile.getRole().getRoleId();
		String contractNumStr = f.getContractNum();
		// userType = "RIA";
		String pcStr = f.getProducerCode();// need to add condition to check for
											// user role accessing the page
		if ((!StringUtils.isEmpty(contractNumStr) && f.getContractNumValue() == null)
				|| (!StringUtils.isEmpty(pcStr) && f.getProducerCodeValue() == null)) {

			BDExtUserSearchData reportData = new BDExtUserSearchData(null, 0);
			request.setAttribute(BDConstants.REPORT_BEAN, reportData);
			return forwards.get("input");
		}
		return super.doCommon(reportForm, request, response);
	}

	@Override
	protected String getDefaultSort() {
		return BDExtUserSearchData.SORT_LAST_NAME;
	}

	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		throw new SystemException("Not supported");
	}

	@Override
	protected String getReportId() {
		return BDExtUserSearchData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return BDExtUserSearchData.REPORT_NAME;
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {

		// Get the User profile
		BDUserProfile profile = BDSessionHelper.getUserProfile(request);

		ExtUserSearchReportForm f = (ExtUserSearchReportForm) form;
		String lastName = StringEscapeUtils.escapeSql(f.getLastName());
		if (!StringUtils.isEmpty(lastName)) {
			criteria.addFilter(BDExtUserSearchData.FILTER_LAST_NAME, StringUtils.upperCase(lastName));
		}
		String firstName = StringEscapeUtils.escapeSql(f.getFirstName());
		if (!StringUtils.isEmpty(firstName)) {
			criteria.addFilter(BDExtUserSearchData.FILTER_FIRST_NAME, StringUtils.upperCase(firstName));
		}
		if (f.getProducerCodeValue() != null) {
			criteria.addFilter(BDExtUserSearchData.FILTER_PRODUCER_CODE, f.getProducerCodeValue());
		}
		if (f.getContractNumValue() != null) {
			criteria.addFilter(BDExtUserSearchData.FILTER_CONTRACT_NUM, f.getContractNumValue());

		}
		String emailAddress = StringEscapeUtils.escapeSql(f.getEmailAddress());
		if (!StringUtils.isEmpty(emailAddress)) {
			criteria.addFilter(BDExtUserSearchData.FILTER_EMAIL_ADDRESS, emailAddress);
		}
		// set the criteria with role filter to decide on the Data access
		String userRoleType = profile.getRole().getRoleType().getUserRoleCode();
		if (!StringUtils.isEmpty(userRoleType)) {
			criteria.addFilter(BDExtUserSearchData.FILTER_USER_ROLE_CODE, userRoleType);
		}
	}

	@Override
	protected BaseReportForm resetForm(BaseReportForm reportForm, HttpServletRequest request)
			throws SystemException {

		ExtUserSearchReportForm form = (ExtUserSearchReportForm) super.resetForm(reportForm, request);
		return form;
	}

	@Override
	protected ReportData getReportData(String reportId, ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {
		// do not search for no filtering criteria and when userRoleCode is the
		// only filtering criterion.
		if (reportCriteria.getFilters().isEmpty() || (reportCriteria.getFilters().size() == 1
				&& reportCriteria.getFilters().containsKey(BDExtUserSearchData.FILTER_USER_ROLE_CODE))) {
			BDExtUserSearchData reportData = new BDExtUserSearchData(reportCriteria, 0);
			return reportData;
		} else {
			return super.getReportData(reportId, reportCriteria, request);
		}
	}

	@Override
	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
		criteria.setSorts(DefaultSort);
	}

	private static String stripHtmlBrackets(String value) {
		if (value != null) {
			value = value.replaceAll("<", "").replaceAll(">", "");
		}
		return value;
	}

	@Autowired
	private BDValidatorFWExtUser bdValidatorFWExtUser;
	@Autowired
	private ExtUserSearchReportValidator extUserSearchReportValidator;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWExtUser);
		binder.addValidators(extUserSearchReportValidator);
	}
	
	
}
