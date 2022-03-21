package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Message;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.platform.web.validation.rules.UserNameRule;
import com.manulife.pension.platform.web.validation.rules.generic.NumericRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.mockable.MockManageUsersReportHandler;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.ps.web.validation.rules.EmployeeNumberRule;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.AuthorizedSignor;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.ManageUsersReportData;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.manager.ContentCacheManager;
/**
 * @author Charles Chan
 */
@Controller
@RequestMapping(value = "/profiles")
@SessionAttributes({ "manageUsersReportForm" })

public class ManageTPAUsersReportController extends ReportController {
	
	@ModelAttribute("manageUsersReportForm")
	public ManageUsersReportForm populateForm() {
		return new ManageUsersReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/profiles/manageTpaUsers.jsp");
		forwards.put("default", "/profiles/manageTpaUsers.jsp");
		forwards.put("sort", "/profiles/manageTpaUsers.jsp");
		forwards.put("filter", "/profiles/manageTpaUsers.jsp");
		forwards.put("page", "/profiles/manageTpaUsers.jsp");
		forwards.put("showAll", "/profiles/manageTpaUsers.jsp");
	}

	private static final String FALSE_STRING = "false";

	private static final boolean USE_BACKEND = SecurityServiceDelegate.USE_BACKEND;

	/**
	 * Forward to manage internal user action.
	 */
	private static final String MANAGE_INTERNAL_USER = "internal";

	/**
	 * Forward to manage tpa user action.
	 */
	private static final String MANAGE_TPA_USER = "tpa";
	

	public static final String RESULTS_MESSAGE_KEY = "resultsMessageKey";
	public static final String DISPLAY_SHOW_ALL_KEY = "displayShowAllKey";
	public static final String TPA_FIRM_INFO_KEY = "tpafirmInfoKey";
	public static final String HAS_TPA_FIRM_KEY = "hasTpaFirmKey";
	public static final String SEARCH_BUTTON_HIT = "searchButtonHit";

	private static final RegularExpressionRule userIdRule = new RegularExpressionRule(
			ErrorCodes.INTERNAL_USER_NAME_INVALID, UserNameRule.getInstance().getUserNameRuleRegExp());

	/**
	 * Constructor.
	 */
	public ManageTPAUsersReportController() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param className
	 */
	public ManageTPAUsersReportController(Class className) {
		super(className);
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportId()
	 */
	protected String getReportId() {
		return ManageUsersReportData.REPORT_ID;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportName()
	 */
	protected String getReportName() {
		return ManageUsersReportData.REPORT_NAME;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return ManageUsersReportData.SORT_FIELD_LAST_NAME;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	private boolean isManageTpaUsers(String mapping) {
		return mapping.equals(MANAGE_TPA_USER);
	}

	private boolean isManageInternalUsers(String mapping) {
		return mapping.equals(MANAGE_INTERNAL_USER);
	}

	/**
	 * Sets whether the Manage TPA firm link should be displayed or not.
	 * 
	 * @see com.manulife.pension.ps.web.controller.DynamicController#postInvokeTask(
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void postExecute(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
		super.postExecute(actionForm, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> postExecute");
		}

		UserProfile userProfile = getUserProfile(request);
		UserRole role = userProfile.getRole();

		if (role.isTPA()) {
			request.setAttribute(Constants.SHOW_EDIT_TPA_FIRM, FALSE_STRING);
		} else {
			Contract currentContract = userProfile.getCurrentContract();

			if (currentContract != null) {
				TpaFirmController.setShowManageTpaFirmLink(request);

				if (userProfile.getContractProfile().getShowManageTpaFirmLink() == null) {
					boolean showEditTpaFirm = false;

					if (role.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS)) {
						if ((role.isExternalUser() && !role.hasPermission(PermissionType.SELECTED_ACCESS))
								|| role.isInternalUser()) {
							TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance()
									.getFirmInfoByContractId(currentContract.getContractNumber());

							if (firmInfo != null) {
								showEditTpaFirm = true;
							}
						}
					}

					userProfile.getContractProfile()
							.setShowManageTpaFirmLink(showEditTpaFirm ? Boolean.TRUE : Boolean.FALSE);
				}

				request.setAttribute(Constants.SHOW_EDIT_TPA_FIRM,
						userProfile.getContractProfile().getShowManageTpaFirmLink().toString());
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- postExecute");
		}
	}

	public String doCommon(BaseReportForm form,  HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		UserProfile userProfile = getUserProfile(request);
		UserRole userRole = userProfile.getRole();
		Contract currentContract = userProfile.getCurrentContract();
		// Contact Management removed the current contract check
		// -Internal User Manager can Edit Profiles without the Contract

		SessionHelper.setLastVisitedManageUsersPage(request, "tpa");
		boolean showPayrollEmail = false;
		if (currentContract != null) {
			showPayrollEmail = ContractAccessActionHelper
					.requiresPayrollEmailNotification(currentContract.getContractNumber());
		}
		SessionHelper.setShowPayrollEmailPermission(request, showPayrollEmail);
		String returnForward = super.doCommon(form, request, response);
		if (userRole.isInternalUser()) {
			ManageUsersReportData reportData = (ManageUsersReportData) request.getAttribute(Constants.REPORT_BEAN);
			Map<Long, Boolean> deleteMap = reportData.getDeleteMap();
			boolean hasManageExterUsersTrusteeAndAuthSignor = userRole
					.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS_TRUSTEE_AND_AUTH_SIGNOR);

			// now fill in the the delete map;
			try {
				if (reportData.getDetails() != null) {
					if (hasManageExterUsersTrusteeAndAuthSignor) {
						Iterator it = reportData.getDetails().iterator();
						while (it.hasNext()) {
							UserInfo uinfo = (UserInfo) it.next();
							deleteMap.put(uinfo.getProfileId(), Boolean.TRUE);
						}
					} else {
						// Contact Management added the current contract null check
						// Internal User Manager can Edit Profiles without the Contract
						if (currentContract != null) {
							List userInfos = SecurityServiceDelegate.getInstance()
									.searchUserAllContractPermissions(currentContract.getContractNumber());
							Iterator it = reportData.getDetails().iterator();
							while (it.hasNext()) {
								UserInfo uinfo = (UserInfo) it.next();
								// need to find out if the current uinfo is a
								// trustee or
								// auth signor on any contracts
								Iterator it2 = userInfos.iterator();
								while (it2.hasNext()) {
									UserInfo uinfo2 = (UserInfo) it2.next();
									if (uinfo.getProfileId() == uinfo2.getProfileId()) {
										Iterator it3 = uinfo2.getContractPermissionsAsCollection().iterator();
										boolean foundOne = false;
										while (it3.hasNext()) {
											ContractPermission cp = (ContractPermission) it3.next();
											if (cp.getRole() instanceof Trustee
													|| cp.getRole() instanceof AuthorizedSignor) {
												foundOne = true;
												break;
											}
										}
										deleteMap.put(uinfo.getProfileId(), !foundOne);
										break;
									}
								}
							}
						}
					}
				}
			} catch (SecurityServiceException e) {
				logger.error("Received a Security service exception: ", e);
				List errors = new ArrayList();
				errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
				setErrorsInRequest(request, errors);
				return forwards.get("input");
			}
		}
		if (userRole.isInternalUser()) {
			// Contact Management added the null check for current contract
			// Internal User Manager can Edit Profiles without the Contract
			TPAFirmInfo firmInfo = null;
			if (currentContract != null) {
				firmInfo = TPAServiceDelegate.getInstance()
						.getFirmInfoByContractId(currentContract.getContractNumber());
			}
			if (firmInfo == null) {
				firmInfo = new TPAFirmInfo();
			}
			request.setAttribute(TPA_FIRM_INFO_KEY, firmInfo);
		} else {
			request.setAttribute(TPA_FIRM_INFO_KEY, new TPAFirmInfo());
		}
		return returnForward;
	}

	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}

		super.populateReportForm(reportForm, request);

		ManageUsersReportForm theForm = (ManageUsersReportForm) reportForm;

		/*
		 * Set default sort radio button. LS -TPAUM - change default for tpa user
		 * managers to firm name
		 */
		//the code has been removed because for internal users class we have created "ManageUsersReportAction"
		String filter = theForm.getFilter();
		

			if (filter == null || (!filter.equals(ManageUsersReportData.FILTER_TPA_FIRM_ID)
					&& !filter.equals(ManageUsersReportData.FILTER_TPA_LAST_NAME)
					&& !filter.equals(ManageUsersReportData.FILTER_TPA_FIRM_NAME))) {
				theForm.setFilter(ManageUsersReportData.FILTER_TPA_FIRM_NAME);
				theForm.setFilterValue("");

			}
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
		Exception ex = new UnsupportedOperationException("Downloading data is not supported.");
		throw new SystemException(ex, getClass().getName(), "Downloading data", ex.getMessage());
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

		ManageUsersReportForm theForm = (ManageUsersReportForm) form;

		String filter = theForm.getFilter();
		String filterValue = theForm.getFilterValue();

		/*
		 * We need to check if we are showing the report for the first time. When the
		 * report is shown for the first time, the FORM selection does not correspond to
		 * the criteria we pass to the backend. Specifically, the FORM selection is on
		 * the TPA Firm ID or the Employee Number but the criteria is on the users' last
		 * name.
		 */
		if (filter.equals(ManageUsersReportData.FILTER_EMPLOYEE_NUMBER) && filterValue.length() == 0) {
			criteria.addFilter(ManageUsersReportData.FILTER_FILTER, ManageUsersReportData.FILTER_EMPLOYEE_LAST_NAME);
		} else if (filter.equals(ManageUsersReportData.FILTER_TPA_FIRM_ID) && filterValue.length() == 0) {
			criteria.addFilter(ManageUsersReportData.FILTER_FILTER, ManageUsersReportData.FILTER_TPA_LAST_NAME);
		} else {
			criteria.addFilter(ManageUsersReportData.FILTER_FILTER, filter);
		}

		if (getUserProfile(request).getPrincipal().getRole().isTPA()
				&& getUserProfile(request).getPrincipal().getRole().hasPermission(PermissionType.MANAGE_TPA_USERS)) {
			String task = getTask(request);
			if (ReportController.FILTER_TASK.equals(task)) {
				request.getSession(false).setAttribute(SEARCH_BUTTON_HIT, Boolean.TRUE);
			} else if (ReportController.DEFAULT_TASK.equals(task) || ReportController.SHOWALL_TASK.equals(task)) {
				request.getSession(false).removeAttribute(SEARCH_BUTTON_HIT);
			}
			if (request.getSession(false).getAttribute(SEARCH_BUTTON_HIT) != null) {
				criteria.addFilter(ManageUsersReportData.FILTER_EXCULDE_TUMN, Boolean.TRUE);
			} else {
				criteria.addFilter(ManageUsersReportData.FILTER_EXCULDE_TUMN, Boolean.FALSE);
			}
		}

		criteria.addFilter(ManageUsersReportData.FILTER_VALUE, filterValue);
		criteria.addFilter(ManageUsersReportData.FILTER_PRINCIPAL, getUserProfile(request).getPrincipal());

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}

	}

	private ManageUsersReportData getMockedManageUsersReportData(ReportCriteria reportCriteria,
			HttpServletRequest request) throws SystemException, ReportServiceException {
		MockManageUsersReportHandler handler = new MockManageUsersReportHandler();
		ManageUsersReportData data = (ManageUsersReportData) handler.getReportData(reportCriteria);
		data.setUserInfo(SecurityServiceDelegate.getInstance().getUserInfo(getUserProfile(request).getPrincipal()));
		return data;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportData(java.lang.String,
	 *      com.manulife.pension.service.report.valueobject.ReportCriteria,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected ReportData getReportData(String reportId, ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {
		if (USE_BACKEND) {
			return (ManageUsersReportData) super.getReportData(reportId, reportCriteria, request);

		} else {
			return getMockedManageUsersReportData(reportCriteria, request);
		}
	}

	/**
	 * Checks whether we're in the right state.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#validate(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	protected String validate(ActionForm actionForm, HttpServletRequest request) {

		Collection errors = doValidate(actionForm, request);
		/*
		 * Errors are stored in the session.
		 */
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get("input");
		}

		return null;
	}
	protected Collection doValidate(ActionForm actionForm,HttpServletRequest request){
		Collection errors = new ArrayList();
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}
		ManageUsersReportForm theForm = (ManageUsersReportForm) actionForm;

		String filter = theForm.getFilter();
		String filterValue = theForm.getFilterValue();

		String task = getTask(request);
		if (FILTER_TASK.equals(task)) {
			theForm.setSearchButtonHit(true);
		} else if (SHOWALL_TASK.equals(task)) {
			theForm.setShowAllButtonHit(true);
		}

		// MPR.624d, MPR.634e, MPR.634f for TPAUM After the user pushes the
		// Search link, a Show All link will appear (no matter what the outcome
		// of the Search
		// request was)
		String mapping="tpa";
		if (theForm.isSearchButtonHit()
				&& (isManageTpaUsers(mapping) || isManageInternalUsers(mapping))) {
			request.setAttribute(DISPLAY_SHOW_ALL_KEY, "true");
		}

		/*
		 * If either the filter or the filter value is empty, return an error.
		 */
		if (FILTER_TASK.equals(task)
				|| ((isManageTpaUsers(mapping) || isManageInternalUsers(mapping)) && SORT_TASK
						.equals(task)) && !theForm.isShowAllButtonHit()) {
			if (filter == null || filter.trim().length() == 0
					|| filterValue == null || filterValue.trim().length() == 0) {
				/*
				 * MPR 437 If user has not entered in at least one character
				 * before selecting search, system must display an error message
				 * "you must enter in at least one character to perform search"
				 */
				errors.add(new GenericException(
						ErrorCodes.SEARCH_FIELD_MANDATORY));
			} else {
				/*
				 * MPR 438 If user entered in a numeric and user selected to
				 * search by last name, system must display an error message. If
				 * user entered in alpha characters and selected to search by
				 * TPA firm id, system must display an error message.
				 */
				if (filter
						.equals(ManageUsersReportData.FILTER_EMPLOYEE_LAST_NAME)
						|| filter
								.equals(ManageUsersReportData.FILTER_TPA_LAST_NAME)) {
					NameRule.getLastNameInstance().validate(
							ManageUsersReportForm.FIELD_FILTER_VALUE,
							errors, filterValue);
				} else if (filter
						.equals(ManageUsersReportData.FILTER_EMPLOYEE_NUMBER)) {
					EmployeeNumberRule.getInstance().validate(
							ManageUsersReportForm.FIELD_FILTER_VALUE,
							errors, filterValue);
				} else if (filter
						.equals(ManageUsersReportData.FILTER_TPA_FIRM_ID)) {
					NumericRule rule = new NumericRule(
							ErrorCodes.TPA_FIRM_ID_INVALID);
					rule.validate(
							ManageUsersReportForm.FIELD_FILTER_VALUE,
							errors, filterValue);
				} else if (filter.equals(ManageUsersReportData.FILTER_INTERNAL_USER_ID)) {
					userIdRule.validate(
							ManageUsersReportForm.FIELD_FILTER_VALUE,
							errors, filterValue);
				}
			}
		}

		/*
		 * Resets the information for JSP to display.
		 */
		if (errors.size() > 0) {

			populateReportForm( theForm, request);

			/*
			 * Puts an empty ReportData into the request.
			 */
			ManageUsersReportData reportData = new ManageUsersReportData();
			reportData.setUserInfo(SecurityServiceDelegate.getInstance()
					.getUserInfo(getUserProfile(request).getPrincipal()));
			request.setAttribute(Constants.REPORT_BEAN, reportData);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}
		return errors;
	}

	/**
	 * Validate the input form. The search field must not be empty.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsAction#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	
	/**
	 * @see ReportController#doDefault(ActionMapping, BaseReportForm,
	 *      HttpServletRequest, HttpServletResponse)
	 */

	@RequestMapping(value = "/manageTpaUsers/", method = {RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("manageUsersReportForm") ManageUsersReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				populateReportForm( form, request);
				ManageUsersReportData reportData = new ManageUsersReportData();
				reportData.setUserInfo(SecurityServiceDelegate.getInstance()
						.getUserInfo(getUserProfile(request).getPrincipal()));
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				request.setAttribute(Constants.SHOW_EDIT_TPA_FIRM,"true");
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
		}
		preExecute(form,request,response);
		String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}

		UserProfile userProfile = getUserProfile(request);
		UserRole userRole = userProfile.getRole();
		Contract currentContract = userProfile.getCurrentContract();
		// Contact Management removed the current contract check
		// - Internal User Manager can Edit Profiles without the Contract

		UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(userProfile.getPrincipal());
		request.setAttribute("userInfo", userInfo);
		if (TpaumHelper.isTPAUM(userProfile)) {
			return doShowAll(form, bindingResult, request, response);

		}
		String mapping="tpa";
	//TODO need to check for the external user and access these page
		if (!isManageInternalUsers(mapping) && !isManageTpaUsers(mapping)) {
			// this is the default screen for manage external users

			return doCommon(form, request, response);
		}

		populateReportForm(form, request);

		ManageUsersReportData reportData = new ManageUsersReportData();
		reportData.setUserInfo(userInfo);
		request.setAttribute(Constants.REPORT_BEAN, reportData);
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}
		postExecute(form,request,response);
		return forwards.get("input");

	}

	/**
	 * return a complete list of users
	 */
	@RequestMapping(value = "/manageTpaUsers/", params = {"task=showAll"}, method = {RequestMethod.GET})
	public String doShowAll(@Valid @ModelAttribute("manageUsersReportForm") ManageUsersReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		preExecute(form,request,response);
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				populateReportForm( form, request);
				ManageUsersReportData reportData = new ManageUsersReportData();
				reportData.setUserInfo(SecurityServiceDelegate.getInstance()
						.getUserInfo(getUserProfile(request).getPrincipal()));
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				request.setAttribute(Constants.SHOW_EDIT_TPA_FIRM,"true");
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
		}
		String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doShowAll");
		}

		UserProfile userProfile = getUserProfile(request);
		UserRole userRole = userProfile.getRole();
		Contract currentContract = userProfile.getCurrentContract();

		// Contact Management removed the current contract check - Page can viewed
		// without contract

		form.clear();
		String forward = this.doCommon(form, request, response);
		ManageUsersReportData reportData = (ManageUsersReportData) request.getAttribute(Constants.REPORT_BEAN);

		try {

			if (reportData.getDetails().size() == 0) {
				Message message = (Message) ContentCacheManager.getInstance().getContentById(
						ContentConstants.MESSAGE_NO_ACTIVE_USERS, ContentTypeManager.instance().MESSAGE);

				request.setAttribute(RESULTS_MESSAGE_KEY, message.getText());
				if (logger.isDebugEnabled()) {
					logger.debug("doShowAll produced no results");
				}

			}
		} catch (ContentException e) {

			throw new SystemException(e, this.getClass().getName(), "doShowAll", "Content-related exception");
		}
		// catch(Exception e){
		// System.out.println ("failing here");
		// e.printStackTrace();

		// }

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doShowAll");
		}
		postExecute(form,request,response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	/**
	 * @see ReportController#doFilter(ActionMapping, BaseReportForm,
	 *      HttpServletRequest, HttpServletResponse)
	 */
	@RequestMapping(value = "/manageTpaUsers/", params = { "task=filter"}, method = {RequestMethod.GET })
	public String doFilter(@Valid @ModelAttribute("manageUsersReportForm") ManageUsersReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		preExecute(form,request,response);
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				populateReportForm( form, request);
				ManageUsersReportData reportData = new ManageUsersReportData();
				reportData.setUserInfo(SecurityServiceDelegate.getInstance()
						.getUserInfo(getUserProfile(request).getPrincipal()));
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				request.setAttribute(Constants.SHOW_EDIT_TPA_FIRM,"true");
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
		}
		String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doFilter");
		}

		UserProfile userProfile = getUserProfile(request);
		UserRole userRole = userProfile.getRole();
		Contract currentContract = userProfile.getCurrentContract();

		// Contact Management removed the current contract check
		// -Internal User Manager can Edit Profiles without the Contract

		String forward = super.doFilter(form, request, response);
		ManageUsersReportData reportData = (ManageUsersReportData) request.getAttribute(Constants.REPORT_BEAN);

		try {
			if (reportData.getDetails().size() == 0) {
				Message message = (Message) ContentCacheManager.getInstance()
						.getContentById(ContentConstants.MESSAGE_NO_USERS_FOUND, ContentTypeManager.instance().MESSAGE);

				request.setAttribute(RESULTS_MESSAGE_KEY, message.getText());
				if (logger.isDebugEnabled()) {
					logger.debug("doFilter produced no results");
				}

			}
		} catch (ContentException e) {
			throw new SystemException(e, this.getClass().getName(), "doFilter", "Content-related exception");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doFilter");
		}
		postExecute(form,request,response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/manageTpaUsers/", params = {"task=page"}, method = {RequestMethod.GET,RequestMethod.POST})
	public String doPage(@Valid @ModelAttribute("manageUsersReportForm") ManageUsersReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		preExecute(form,request,response);
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				populateReportForm( form, request);
				ManageUsersReportData reportData = new ManageUsersReportData();
				reportData.setUserInfo(SecurityServiceDelegate.getInstance()
						.getUserInfo(getUserProfile(request).getPrincipal()));
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				request.setAttribute(Constants.SHOW_EDIT_TPA_FIRM,"true");
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
		}
		String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		String forward = super.doPage(form, request, response);
		postExecute(form,request,response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/manageTpaUsers/", params = {"task=sort"}, method = { RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("manageUsersReportForm") ManageUsersReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		preExecute(form,request,response);
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				populateReportForm( form, request);
				ManageUsersReportData reportData = new ManageUsersReportData();
				reportData.setUserInfo(SecurityServiceDelegate.getInstance()
						.getUserInfo(getUserProfile(request).getPrincipal()));
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				request.setAttribute(Constants.SHOW_EDIT_TPA_FIRM,"true");
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
		}
		String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		String forward = super.doSort(form, request, response);
		postExecute(form,request,response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/manageTpaUsers/", params = {"task=download"}, method = {RequestMethod.POST})
	public String doDownload(@Valid @ModelAttribute("manageUsersReportForm") ManageUsersReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		preExecute(form,request,response);
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				populateReportForm( form, request);
				ManageUsersReportData reportData = new ManageUsersReportData();
				reportData.setUserInfo(SecurityServiceDelegate.getInstance()
						.getUserInfo(getUserProfile(request).getPrincipal()));
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				request.setAttribute(Constants.SHOW_EDIT_TPA_FIRM,"true");
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
		}
		String forward = super.doDownload(form, request, response);
		postExecute(form,request,response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/manageTpaUsers/", params = {"task=downloadAll"}, method = {RequestMethod.POST})
	public String doDownloadAll(@Valid @ModelAttribute("manageUsersReportForm") ManageUsersReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		preExecute(form,request,response);
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				populateReportForm( form, request);
				ManageUsersReportData reportData = new ManageUsersReportData();
				reportData.setUserInfo(SecurityServiceDelegate.getInstance()
						.getUserInfo(getUserProfile(request).getPrincipal()));
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				request.setAttribute(Constants.SHOW_EDIT_TPA_FIRM,"true");
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
		}
		String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		String forward = super.doDownloadAll(form, request, response);
		postExecute(form,request,response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

}