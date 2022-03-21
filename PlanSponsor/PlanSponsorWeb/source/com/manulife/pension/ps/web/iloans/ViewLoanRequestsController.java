package com.manulife.pension.ps.web.iloans;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.iloans.valueobject.LoanRequestDetailVO;
import com.manulife.pension.ps.service.report.iloans.valueobject.LoanRequestReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.account.valueobject.CustomerServicePrincipal;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.util.render.SSNRender;

/**
 * Action class for the View loan Requests Report. It gets the data from
 * Customer Service database,
 * 
 *
 * @author Chris Shin
 * @version CS1.0 (March 1, 2004)
 **/
@Controller
@RequestMapping(value = "/iloans")
@SessionAttributes({ "viewLoanRequestsForm" })

public class ViewLoanRequestsController extends ReportController {
	@ModelAttribute("viewLoanRequestsForm")
	public ViewLoanRequestsForm populateForm() {
		return new ViewLoanRequestsForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/iloans/viewLoanRequests.jsp");
		forwards.put("default", "/iloans/viewLoanRequests.jsp");
		forwards.put("sort", "/iloans/viewLoanRequests.jsp");
		forwards.put("filter", "/iloans/viewLoanRequests.jsp");
		forwards.put("page", "/iloans/viewLoanRequests.jsp");
		forwards.put("print", "/iloans/viewLoanRequests.jsp");
		forwards.put("initiateRequest", "redirect:/do/iloans/initiateLoanRequests/");
		forwards.put("loanRequestPage1", "redirect:/do/iloans/loanRequestPage1/");
		forwards.put("confirmationPage", "redirect:/do/iloans/loanRequestConfirmation/");
	}

	private static final String LINE_BREAK = ReportController.LINE_BREAK;
	private static final String COMMA = ReportController.COMMA;
	private static final String DOWNLOAD = "download";
	private static final String TASK = "task";
	private static final String REPORT_ID = "viewLoanRequestsReport";

	protected static final String DEFAULT_SORT_FIELD = "requestDate";
	protected static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;
	private static final String DOWNLOAD_COLUMN_HEADING = "Contract Name, Contract Number, Participant Name, SSN, Date of Request, Status, Initiated by";
	private static final String DI_DURATION_24_MONTH = "24";
	private static final int ILOANS_PAGE_SIZE = 36;
	private static final String INITIATE_REQUEST_FORWARD = "initiateRequest";
	private static final String CONFIRMATION_PAGE_FORWARD = "confirmationPage";
	private static final String PAGE1_FORWARD = "loanRequestPage1";

	public ViewLoanRequestsController() {
		super(ViewLoanRequestsController.class);
	}

	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		UserProfile userProfile = getUserProfile(request);
		criteria.addFilter(LoanRequestReportData.FILTER_PROFILE_ID, "" + userProfile.getPrincipal().getProfileId());

		criteria.addFilter(LoanRequestReportData.FILTER_DI_DURATION, DI_DURATION_24_MONTH);
		criteria.addFilter(LoanRequestReportData.FILTER_SITE_LOCATION, Environment.getInstance().getDBSiteLocation());

		UserProfile profile = getUserProfile(request);
		UserRole role = profile.getRole();
		criteria.addFilter(LoanRequestReportData.FILTER_USER_ROLE, role);

		// criteria.setPageSize(Integer.parseInt(Environment.getInstance().getDefaultPageSize()));

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return LoanRequestReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return LoanRequestReportData.REPORT_NAME;
	}

	protected String getDefaultSort() {
		return LoanRequestReportData.SORT_REQUEST_DATE;
	}

	protected String getDefaultSortDirection() {
		return ReportSort.DESC_DIRECTION;
	}

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(DOWNLOAD_COLUMN_HEADING);
		;
		Iterator iterator = report.getDetails().iterator();
		while (iterator.hasNext()) {
			buffer.append(LINE_BREAK);
			LoanRequestDetailVO theItem = (LoanRequestDetailVO) iterator.next();

			if (theItem.getContractName() != null)
				buffer.append(escapeField(theItem.getContractName().trim()));

			buffer.append(COMMA);

			if (theItem.getContractNumber() != null)
				buffer.append(escapeField(theItem.getContractNumber().trim()));

			buffer.append(COMMA);

			if (theItem.getParticipantFirstName() != null)
				buffer.append(escapeField(
						theItem.getParticipantFirstName().trim() + " " + theItem.getParticipantLastName().trim()));

			buffer.append(COMMA);

			if (theItem.getParticipantSSN() != null)
				buffer.append(escapeField(SSNRender.format(theItem.getParticipantSSN().trim(), null, false)));

			buffer.append(COMMA);

			if (theItem.getReqDate() != null)
				buffer.append(theItem.getReqDate());

			buffer.append(COMMA);

			if (theItem.getRequestStatus() != null)
				buffer.append(escapeField(theItem.getRequestStatus().trim()));

			buffer.append(COMMA);

			if (theItem.getInitiatedBy() != null)
				buffer.append(escapeField(theItem.getInitiatedBy().trim()));

		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	private String escapeField(String field) {
		if (field.indexOf(",") != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else {
			return field;
		}
	}

	
	public String doCommon( BaseReportForm reportForm,
			 HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		// check if any iloan attributes exist in the session - then remove
		HttpSession session = request.getSession(false);
		if (session.getAttribute(IloansHelper.PROFILE_ID_PARM) != null)
			IloansHelper.removeSessionAttributes(session);
		String forward = super.doCommon(reportForm, request, response);
		ViewLoanRequestsForm form = (ViewLoanRequestsForm)reportForm;
		if (request.getParameter("actionInitiate") == null && request.getParameter("viewAndEdit") == null) {
			LoanRequestReportData report = (LoanRequestReportData) request.getAttribute(Constants.REPORT_BEAN);
			form.setLoanRequestReportData(report);
			session.removeAttribute("loanRequestForm");
			session.removeAttribute("createLoanPackageForm");
			session.removeAttribute("iloansParentPage");
		} else if (request.getParameter("actionInitiate") != null) {
			forward = forwards.get(INITIATE_REQUEST_FORWARD);
			session.setAttribute("iloansParentPage", "view");
		} else if (request.getParameter("viewAndEdit") != null) {
			session.setAttribute(IloansHelper.PROFILE_ID_PARM, request.getParameter(IloansHelper.PROFILE_ID_PARM));
			session.setAttribute(IloansHelper.CONTRACT_NUMBER_PARM,
					request.getParameter(IloansHelper.CONTRACT_NUMBER_PARM));
			session.setAttribute(IloansHelper.LOAN_REQUEST_ID_PARM,
					request.getParameter(IloansHelper.LOAN_REQUEST_ID_PARM));
			session.setAttribute("iloansParentPage", "view");
			String loanStatusCode = request.getParameter("viewAndEdit");
			// //TODO saveToken(request);
			if (Constants.ILOANS_STATUS_CODE_REVIEW.equalsIgnoreCase(loanStatusCode)
					|| Constants.ILOANS_STATUS_CODE_PENDING.equalsIgnoreCase(loanStatusCode))

				forward = forwards.get(PAGE1_FORWARD);
			else
				forward = forwards.get(CONFIRMATION_PAGE_FORWARD);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		return forward;
	}

	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}

	@RequestMapping(value = "/viewLoanRequests/", method = {RequestMethod.GET,RequestMethod.POST })
	public String doDefault(@Valid @ModelAttribute("viewLoanRequestsForm") ViewLoanRequestsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			populateReportForm(form, request);
			LoanRequestReportData reportData = form.getLoanRequests();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("defualt");
			}
		}
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/viewLoanRequests/", params = { "action=filter"}, method = {RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("viewLoanRequestsForm") ViewLoanRequestsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			populateReportForm(form, request);
			LoanRequestReportData reportData = form.getLoanRequests();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("defualt");
			}
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/viewLoanRequests/", params = { "task=page" }, method = {RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("viewLoanRequestsForm") ViewLoanRequestsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			populateReportForm(form, request);
			LoanRequestReportData reportData = form.getLoanRequests();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("defualt");
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/viewLoanRequests/", params = {"task=sort" }, method = {RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("viewLoanRequestsForm") ViewLoanRequestsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			populateReportForm(form, request);
			LoanRequestReportData reportData = form.getLoanRequests();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("defualt");
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/viewLoanRequests/", params = {"task=print" }, method = {RequestMethod.GET })
	public String doPrint(@Valid @ModelAttribute("viewLoanRequestsForm") ViewLoanRequestsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			populateReportForm(form, request);
			LoanRequestReportData reportData = form.getLoanRequests();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("defualt");
			}
		}
		String forward = super.doPrint(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/viewLoanRequests/", params = { "task=download" }, method = { RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("viewLoanRequestsForm") ViewLoanRequestsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			populateReportForm(form, request);
			LoanRequestReportData reportData = form.getLoanRequests();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("defualt");
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/viewLoanRequests/", params = { "task=downloadAll" }, method = {RequestMethod.GET })
	public String doDownloadAll(@Valid @ModelAttribute("viewLoanRequestsForm") ViewLoanRequestsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			populateReportForm(form, request);
			LoanRequestReportData reportData = form.getLoanRequests();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("defualt");
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	
	// public String execute(
	// 
	// Form form,
	// HttpServletRequest request,
	// HttpServletResponse response)
	// throws IOException, ServletException
	// {
	// if ("POST".equalsIgnoreCase(request.getMethod()) ) {
	// // do a refresh so that there's no problem using tha back button
	// ActionForward forward = new ActionForward("refresh",
	// "/do" + mapping.getPath() + "?task=" + getTask(request),
	// true);
	// if(logger.isDebugEnabled()) {
	// logger.debug("forward = " + forward);
	// }
	// return forward;
	// }
	//
	//
	// return super.execute( form, request, response);
	// }

	private String addQuote(String text) {

		StringBuffer buff = new StringBuffer();
		buff.append("\"");
		buff.append(text);
		buff.append("\"");
		return buff.toString();

	}

	private CustomerServicePrincipal getCustomerServicePrincipal(String clientId) {

		CustomerServicePrincipal principal = new CustomerServicePrincipal();
		principal.setName(clientId);
		principal.setRoles(new String[] { CustomerServicePrincipal.ROLE_SUPER_USER });

		return principal;
	}

	/**
	 * If it's sort by status code, secondary sort is request date. If it's sort by
	 * initiated by, secondary sort is request date.
	 * 
	 */
	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		criteria.insertSort(sortField, sortDirection);
		if (sortField.equals(LoanRequestReportData.SORT_STATUS_CODE)) {
			criteria.insertSort(LoanRequestReportData.SORT_REQUEST_DATE, sortDirection);
		} else if (sortField.equals(LoanRequestReportData.SORT_INITIATED_BY)) {
			criteria.insertSort(LoanRequestReportData.SORT_REQUEST_DATE, sortDirection);

		}
	}

	
}
