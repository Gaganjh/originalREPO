package com.manulife.pension.ps.web.census;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.EligibilityServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralReportData;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeStatusHistoryDetails;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeStatusHistoryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.census.util.EligibilityValidationErrors;
import com.manulife.pension.ps.web.census.util.EmployeeServiceFacade;
import com.manulife.pension.ps.web.census.util.EmployeeSnapshotSecurityProfile;
import com.manulife.pension.ps.web.census.util.EmployeeStatusHistoryReportHelper;
import com.manulife.pension.ps.web.census.util.ParameterizedActionForward;
import com.manulife.pension.ps.web.contract.csf.EligibilityCalculationMoneyType;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.eligibility.EligibilityRequestConstants;
import com.manulife.pension.service.eligibility.dao.EmployeePlanEntryDAO;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.UserIdType;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;

/**
 * This action handles deletion of Employment Statuses. testing the common and
 * some validation
 *
 * @author sternlu
 */
@Controller
@RequestMapping(value="/census")
@SessionAttributes({"employeeStatusHistoryReportForm"})

public final class EmployeeStatusHistoryReportController extends ReportController {

	@ModelAttribute("employeeStatusHistoryReportForm")
	public EmployeeStatusHistoryReportForm populateForm() {
		return new EmployeeStatusHistoryReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/census/employeeStatusHistory.jsp");
		forwards.put("default","/census/employeeStatusHistory.jsp");
		forwards.put("sort","/census/employeeStatusHistory.jsp");
		forwards.put("filter","/census/employeeStatusHistory.jsp");
		forwards.put("page","/census/employeeStatusHistory.jsp");
		forwards.put("print","/census/employeeStatusHistory.jsp");
		forwards.put("save","/census/employeeStatusHistory.jsp");
		forwards.put("editEmployeeForm","redirect:/do/census/editEmployeeSnapshot/");
	}

	public static final String UPDATE_STATUS_PAGE = "employeeStatusHistoryPage";

	public static final String REPORT_TYPE = "reportType";

	public final static String DATE_DISPLAY_PATTERN = "MMM/dd/yyyy";

	public final static String DATE_DELETE_PATTERN = "yyyy-MM-dd";

	public final static String SOURCE_FUNCTION_CODE = "UNS";

	public final static String EVENT_TYPE_CODE = "CEN";

	protected static final String ELIGIBILITY_MONEY_TYPE_LIST_ATTRIBUTE = "eligbilityMoneyTypeList";

	protected static EmployeeServiceDelegate serviceFacade = EmployeeServiceDelegate
			.getInstance(Constants.PS_APPLICATION_ID);

	/**
	 * Constructor for EmployeeStatusUpdateDeleteAction
	 */
	public EmployeeStatusHistoryReportController() {
		super(EmployeeStatusHistoryReportController.class);
	}
	
	public String doSave(BaseReportForm reportForm, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.info("entry <- doSave");

		}
		EmployeeStatusHistoryReportForm form = (EmployeeStatusHistoryReportForm) reportForm;
		if (form.getProfileId() == null) {
			logger.error("no profileId provided");
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}

		if (!"Y".equals(form.getSavedFlag())) {// to prevent duplicate delete
			form.setSavedFlag("Y");
			updateHistory(form, request);

		}
		if (logger.isDebugEnabled()) {
			logger.info("entry <- doSave");
		}
		triggerEligibilityCalculationOrResetInformation(form, request);
		return doCommon(form, request, response);
	}

	/**
	 * This method checks the employee status and invokes eligibility
	 * calculation or eligibility information reset accordingly.
	 * 
	 * @param form
	 * @param request
	 * @throws SystemException
	 */
	private void triggerEligibilityCalculationOrResetInformation(ActionForm form, HttpServletRequest request)
			throws SystemException {

		UserProfile userProfile = SessionHelper.getUserProfile(request);
		String userIdType = "";
		if (userProfile.isInternalUser()) {
			userIdType = Constants.INTERNAL_USER_ID_TYPE;
		} else {
			userIdType = Constants.EXTERNAL_USER_ID_TYPE;
		}

		EligibilityServiceDelegate eligibilityService = EligibilityServiceDelegate
				.getInstance(GlobalConstants.PSW_APPLICATION_ID);
		EmployeeStatusHistoryReportForm reportForm = (EmployeeStatusHistoryReportForm) form;
		List<EmployeeStatusHistoryDetails> details = reportForm.getTheItemList();
		String statusWithGreatestEffectiveDate = null;
		boolean found = false;

		for (EmployeeStatusHistoryDetails element : details) {
			// if records are in descending order, then the first record
			// not marked for deletion is the the new currentEffectiveDate
			if (!element.isMarkedForDeletion()) {
				if (!found) {
					statusWithGreatestEffectiveDate = element.getStatus();
					found = true;
				}

			}
		}

		// If the greatest effective status is ACTIVE then trigger eligibility
		// date
		// calculation else do eligibility information reset according to the
		// condition
		if (CensusConstants.EMPLOYMENT_STATUS_ACTIVE.equals(statusWithGreatestEffectiveDate)) {
			eligibilityService.calculateEligibilityForEmployee(userProfile.getCurrentContract().getContractNumber(),
					Integer.parseInt(reportForm.getProfileId()), null,
					EligibilityRequestConstants.RequestCauseTypeCode.EMPLOYEE_DATA_UPDATE,
					EligibilityRequestConstants.SourceChannelCode.PLAN_SPONSOR,
					EligibilityRequestConstants.SourceFunctionCode.EES,
					String.valueOf(userProfile.getPrincipal().getProfileId()), userIdType);

		} else if (statusWithGreatestEffectiveDate.equalsIgnoreCase(CensusConstants.EMPLOYMENT_STATUS_DECEASED)
				|| statusWithGreatestEffectiveDate.equalsIgnoreCase(CensusConstants.EMPLOYMENT_STATUS_CANCEL)
				|| statusWithGreatestEffectiveDate.equalsIgnoreCase(CensusConstants.EMPLOYMENT_STATUS_TERMINATED)
				|| statusWithGreatestEffectiveDate.equalsIgnoreCase(CensusConstants.EMPLOYMENT_STATUS_RETIRED)
				|| statusWithGreatestEffectiveDate.equals(CensusConstants.EMPLOYMENT_STATUS_DISABLED)) {

			EmployeeServiceFacade employeeService = new EmployeeServiceFacade();

			Calendar asOfDate = Calendar.getInstance();
			asOfDate.set(9999, 11, 31);

			Employee employee = employeeService.getEmployee(Long.parseLong(reportForm.getProfileId()), userProfile,
					asOfDate.getTime(), true);

			if (request.getSession().getAttribute(ELIGIBILITY_MONEY_TYPE_LIST_ATTRIBUTE) != null) {
				ArrayList eligibleMoneyTypeList = (ArrayList) request.getSession()
						.getAttribute(ELIGIBILITY_MONEY_TYPE_LIST_ATTRIBUTE);

				// Iterate through the money types and check whether it is a
				// reset scenario
				int totalNumberOfMoneyTypes = eligibleMoneyTypeList.size();
				for (int i = 0; i < totalNumberOfMoneyTypes; i++) {

					EligibilityCalculationMoneyType currentECalcMoneyType = (EligibilityCalculationMoneyType) eligibleMoneyTypeList
							.get(i);
					String newCalculationOverride = currentECalcMoneyType.getCalculationOverride();

					if (!("Y").equalsIgnoreCase(newCalculationOverride)) {
						if (!StringUtils.isEmpty(currentECalcMoneyType.getEligibilityDate())
								&& employee.getEmployeeDetailVO().getEmploymentStatusEffDate() != null) {

							// Check whether eligibility date is greater than or
							// equal to status effective date
							if (new Date(currentECalcMoneyType.getEligibilityDate())
									.compareTo(employee.getEmployeeDetailVO().getEmploymentStatusEffDate()) >= 0) {

								// call eligibility reset
								EmployeePlanEntryDAO.eligibilityInformationReset(
										userProfile.getCurrentContract().getContractNumber(),
										BigDecimal.valueOf(employee.getEmployeeDetailVO().getProfileId()),
										currentECalcMoneyType.getMoneyTypeId(), employee.getUserId().toString(),
										employee.getUserIdType(), EligibilityRequestConstants.SOURCE_CHANNEL_CODE_PS,
										EligibilityRequestConstants.SourceFunctionCode.EES.toString(),
										EligibilityRequestConstants.NO);
							}
						}
					}
				}
			}

			// Remove the money type list from session
			request.getSession().removeAttribute(ELIGIBILITY_MONEY_TYPE_LIST_ATTRIBUTE);
		}
	}

	

	protected String doCommon(BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		UserProfile userProfile = getUserProfile(request);
		EmployeeStatusHistoryReportForm form = (EmployeeStatusHistoryReportForm) reportForm;
		Contract currentContract = userProfile.getCurrentContract();
		String profileId = form.getProfileId();

		if (profileId == null) {
			profileId = (String) request.getParameter("profileId");
			if (profileId != null)
				form.setProfileId(profileId);
			else {
				logger.error("no profileId provided");
				return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
			}
		}

		/*
		 * to be done if (profileId == null || !isAllowedPageAccess(userProfile,
		 * currentContract)) { return forwards.get("homePageFinder"); }
		 */
		int contractId = userProfile.getCurrentContract().getContractNumber();
		String forward = super.doCommon(reportForm, request, response);
		// get the Report object
		EmployeeStatusHistoryReportData report = (EmployeeStatusHistoryReportData) request
				.getAttribute(Constants.REPORT_BEAN);
		List<EmployeeStatusHistoryDetails> details = (List<EmployeeStatusHistoryDetails>) report.getDetails();

		if (!hasErrors(request)) {
			form.setTheItemList(details);
			form.storeClonedForm();
		}

		// get the report details
		// List<EmployeeSummaryDetails> details =
		// (List<EmployeeSummaryDetails>)report.getDetails();

		// ActionForward forward = super.doCommon( reportForm,
		// request,response);
		// EmployeeStatusHistoryReportForm sform =
		// (EmployeeStatusHistoryReportForm) reportForm;
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		return forward;
	}
	

	/**
	 * go back to the source page
	 *
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	protected String doCancel( BaseReportForm reportForm,HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		EmployeeStatusHistoryReportForm form = (EmployeeStatusHistoryReportForm) reportForm;
		if (request.getAttribute("psErrors") != null) {
			request.removeAttribute("psErrors");
		}
		if (request.getSession(false).getAttribute("psErrors") != null) {
			request.getSession(false).removeAttribute("psErrors");
		}
		removeFormUponExit(request);
		String configured = forwards.get("editEmployeeForm");
		ParameterizedActionForward realForward = new ParameterizedActionForward(configured);
		realForward.addParameter("profileId", form.getProfileId());
		return realForward.getPath();
	}

	/**
	 * Exit from the current page
	 * 
	 * @param mapping
	 * @param request
	 */
	protected void removeFormUponExit(HttpServletRequest request) {
		request.getSession(false).removeAttribute("vestingInformationForm");
	}

	/**
	 * validate the current date; if errors - return errors if no errors - call
	 * the deleteEmployeeStatus from ServiceDelegate
	 *
	 * @param reportForm
	 * @param request
	 *
	 * @return a list of errors
	 * @throws SystemException
	 */
	protected void updateHistory(EmployeeStatusHistoryReportForm reportForm, HttpServletRequest request)
			throws SystemException {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		// EmployeeStatusHistoryReportForm clonedForm =
		// (EmployeeStatusHistoryReportForm)reportForm.getClonedForm();
		// List<EmployeeStatusHistoryDetails> listFromClone =
		// clonedForm.getTheItemList();
		List<EmployeeStatusHistoryDetails> details = reportForm.getTheItemList();
		List<EmployeeStatusHistoryDetails> recordsToBeDeleted = new ArrayList<EmployeeStatusHistoryDetails>();
		// call service delegate to update status
		// build the String of dates, count the updated records

		// if the size is the same as the size of the employeeStatusInfo list
		// raise "cannot delete all records " error
		// c
		StringBuffer datesToBeDeleted = new StringBuffer();
		StringBuffer datesLog = new StringBuffer();
		StringBuffer statusLog = new StringBuffer();
		StringBuffer userIdLog = new StringBuffer();
		StringBuffer userTypeLog = new StringBuffer();
		StringBuffer lastUpdatedTsLog = new StringBuffer();
		StringBuffer sourceChannelLog = new StringBuffer();
		int count = 0;
		for (EmployeeStatusHistoryDetails element : details) {
			// if records are in descending order, then the first record
			// not marked for deletion is the the new currentEffectiveDate
			if (element.isMarkedForDeletion()) {
				String temp = DateRender.formatByPattern(element.getEffectiveDate(), "", DATE_DELETE_PATTERN);
				if (temp != null && temp.length() > 0)
					datesToBeDeleted.append(temp);
				if (count > 0) {
					datesLog.append(",");
					statusLog.append(",");
					lastUpdatedTsLog.append(",");
					sourceChannelLog.append(",");
					userIdLog.append(",");
					userTypeLog.append(",");
				}
				datesLog.append(temp);
				statusLog.append(element.getStatus());
				lastUpdatedTsLog.append(element.getLastUpdatedTs());
				sourceChannelLog.append(element.getSourceChannelCode());
				userIdLog.append(element.getLastUpdatedUserId());
				userTypeLog.append(element.getLastUpdatedUserType());
				count++;
			}
		}
		if (count > 0) {
			UserProfile user = getUserProfile(request);
			String userId = Long.toString(user.getPrincipal().getProfileId());
			String userType = user.isInternalUser() ? UserIdType.UP_INTERNAL : UserIdType.UP_EXTERNAL;

			// set up the last updated user profile id
			Long pi = new Long(reportForm.getProfileId());

			EmployeeStatusHistoryReportHelper.deleteEmployeeStatus(count, datesLog.toString(), statusLog.toString(),
					lastUpdatedTsLog.toString(), sourceChannelLog.toString(), userIdLog.toString(),
					userTypeLog.toString(), user, datesToBeDeleted.toString(),
					user.getCurrentContract().getContractNumber(), pi, userId, userType, Constants.PS_APPLICATION_ID);

		}
	}

	/**
	 * Utility method used to save into the request the errors and warnings
	 * resulted while saving the updated elements
	 *
	 * @param request
	 * @param errors
	 */
	protected void setErrors(HttpServletRequest request, List<ValidationError> errors) {
		if (errors != null && errors.size() > 0) {
			request.setAttribute("validationErrors", new EligibilityValidationErrors(errors));
		}
		super.setErrorsInRequest(request, errors);
	}

	/**
	 * Utility method that checks if there are errors in the request
	 *
	 * @param request
	 * @return
	 */
	protected boolean hasErrors(HttpServletRequest request) {
		EligibilityValidationErrors errors = (EligibilityValidationErrors) request.getAttribute("validationErrors");

		if (errors != null && errors.getErrors() != null && errors.getErrors().size() > 0) {
			return true;
		}

		return false;
	}

	@Override
	protected String getDefaultSort() {
		return DeferralReportData.DEFAULT_SORT;
	}

	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.DESC_DIRECTION;
	}

	@Override
	protected String getReportId() {
		return EmployeeStatusHistoryReportData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return null;
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		// default sort criteria
		// this is already set in the super

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		EmployeeStatusHistoryReportForm reportForm = (EmployeeStatusHistoryReportForm) form;
		String profileId = reportForm.getProfileId();
		if (profileId == null) {
			profileId = (String) request.getParameter("profileId");
			if (profileId != null) {
				reportForm.setProfileId(profileId);
			} else {
				logger.error("no profileId provided");
			}
		}
		if (profileId != null) {
			criteria.addFilter(EmployeeStatusHistoryReportData.FILTER_CONTRACT_NUMBER,
					Integer.toString(currentContract.getContractNumber()));
			criteria.addFilter(EmployeeStatusHistoryReportData.FILTER_PROFILE_ID, reportForm.getProfileId());

			if (logger.isDebugEnabled()) {
				logger.debug("criteria= " + criteria);
				logger.debug("exit <- populateReportCriteria");
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

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		byte[] bytes = null;
		return bytes;
	}

	protected EmployeeStatusHistoryReportData populateReport(ReportForm form, HttpServletRequest request) {
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		EmployeeStatusHistoryReportForm sform = (EmployeeStatusHistoryReportForm) form;
		ReportCriteria criteria = new ReportCriteria(EmployeeStatusHistoryReportData.REPORT_ID);
		criteria.addFilter(EmployeeStatusHistoryReportData.FILTER_CONTRACT_NUMBER,
				Integer.toString(currentContract.getContractNumber()));
		criteria.addFilter(EmployeeStatusHistoryReportData.FILTER_PROFILE_ID, sform.getProfileId());

		EmployeeStatusHistoryReportData reportData = new EmployeeStatusHistoryReportData(criteria,
				sform.getTheItemList().size());
		String profileId = sform.getProfileId();
		if (profileId == null) {
			profileId = request.getParameter("profileId");
			sform.setProfileId(profileId);
		}
		reportData.setProfileId(new Long(sform.getProfileId()));
		reportData.setDetails(sform.getTheItemList());
		reportData.setTotalCount(sform.getTheItemList().size());
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReport");
		}
		return reportData;
	}

	/**
	 * Reset the edit, reload the values from database
	 *
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doReset(
			@Valid BaseReportForm reportForm,HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		EmployeeStatusHistoryReportForm form = (EmployeeStatusHistoryReportForm) reportForm;
		request.removeAttribute("validationErrors");
		return doCommon(form, request, response);
	}

	/**
	 * @Override. have to check the permission and the profileId
	 */
	@RequestMapping(value ="/employeeStatusHistory/",method = {RequestMethod.GET,RequestMethod.POST})
	public String doExecute(
			@Valid @ModelAttribute("employeeStatusHistoryReportForm") EmployeeStatusHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);																			// default
			}
		}

		String forward = null;

		try {
			// first check profileId
			EmployeeStatusHistoryReportForm reportForm = (EmployeeStatusHistoryReportForm) form;
			String profileId = reportForm.getProfileId();

			if (profileId == null) {
				profileId = (String) request.getParameter("profileId");
				if (profileId != null) {
					reportForm.setProfileId(profileId);
				} else {
					logger.error("no profileId provided");
					return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
				}
			}

			// check security
			UserProfile userProfile = getUserProfile(request);

			EmployeeSnapshotSecurityProfile securityProfile = new EmployeeSnapshotSecurityProfile(userProfile);

			// no update permission
			if (!securityProfile.isUpdateCensusData()) {
				logger.error("no valid access");
				return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;

			}
			String task = request.getParameter("task");
			ReportForm rForm = (ReportForm) form;
			if ("cancel".equals(task))
				return doCancel(form, request, response);
			// first validate
			if ("save".equals(task)) {
				forward = validate(form, request);
			}
			// For XSS Validation ------ CL#137697
			if ("default".equals(task)) {
				forward = validate(form, request);
			}

			if (forward == null) {
				forward = doExecute(form, request, response);
			}
			forward = getApplicationFacade(request).createLayoutBean(request, forward);
		} catch (SystemException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("SystemException caught in PsAction:" + e.getUniqueId(), e);
			} // fi

			LogUtility.logSystemException(Constants.PS_APPLICATION_ID, e);

			request.setAttribute("errorCode", "1099");
			request.setAttribute("uniqueErrorId", e.getUniqueId());

			// forward to Error Page
			return forwards.get(SYSTEM_ERROR_PAGE);
		}
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	protected Collection doValidate(ActionForm form,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}
		String profileId = null;
		request.removeAttribute("psErrors");
		Collection<GenericException> errors = new ArrayList<GenericException>();
		EmployeeStatusHistoryReportForm reportForm = (EmployeeStatusHistoryReportForm) form;
		profileId = reportForm.getProfileId();
		if (profileId == null) {
			profileId = (String) request.getParameter("profileId");
			if (profileId != null) {
				reportForm.setProfileId(profileId);
			} else {
				System.out.println("profileId is null in request");
				return errors;
			}
		}
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		List<EmployeeStatusHistoryDetails> details = reportForm
				.getTheItemList();
		//call service delegate to update status
		// build the String of dates, count the updated records        
		// if the size is the same as the size of the  employeeStatusInfo list 
		// raise "cannot delete all records " error 
		StringBuffer datesToBeDeleted = new StringBuffer();
		Date currentEffectiveDate = null;
		int deletionCount = 0;
		Date tempEffectiveDate = null;
		boolean found = false;
		for (EmployeeStatusHistoryDetails element : details) {
			// if records are in descending order, then the first record  
			// not marked for deletion is the the new currentEffectiveDate
			if (!element.isMarkedForDeletion()) {
				if (!found) {
					currentEffectiveDate = element.getEffectiveDate();
					found = true;
				}
				// if (reportForm.isDescendingOrder())//we use default desc sort now
			} else {
				// else: add the deleted effective date to effectivedate String
				// increase the deletion count
				deletionCount++;
			}
		}
		// to do: if the records are in ascending order
		// then the currentEffectiveDate is the latest date not marked
		// for deletion
		// validation section
		if (deletionCount == 0) {
			errors.add(new GenericException(12345));// this error is nt displayed        		
		} else if (currentEffectiveDate == null && deletionCount > 0)
			//errors.add(user trying to delete all the records)
			errors
					.add(new GenericException(
							ErrorCodes.ERROR_DELETE_ALL_RECORDS));//.DeleteingAllRecordsFromStatusHistory 2717	
		// validate currentEffective date;
		else {
			Employee employee = EmployeeStatusHistoryReportHelper.getEmployee(
					profileId, userProfile.getCurrentContract()
							.getContractNumber(), userProfile);
			// Rule 28 cannote be less then date of birth + 5 yers
			boolean valid = EmployeeStatusHistoryReportHelper
					.isValidByYearDiscrepancyRule(employee
							.getEmployeeDetailVO().getBirthDate(),
							currentEffectiveDate, 5);
			if (!valid)
				errors.add(new GenericException(
						ErrorCodes.ERROR_EFF_DATE_LESS_DOB));//BirthDateEmploymentStatusEffDate 7038
			// Rule 29 // LS.cannot be prior to higher date
			valid = EmployeeStatusHistoryReportHelper
					.isValidByYearDiscrepancyRule(employee
							.getEmployeeDetailVO().getHireDate(),
							currentEffectiveDate, 0);
			if (!valid)
				errors.add(new GenericException(
						ErrorCodes.ERROR_EFF_DATE_LESS_HIRE_DATE)); //EmploymentStatusEffDateHireDate code 7039
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}
		EmployeeStatusHistoryReportData reportData = (EmployeeStatusHistoryReportData) request
				.getAttribute(Constants.REPORT_BEAN);
		if (reportData == null) {
			reportData = populateReport(reportForm, request);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
		}
		reportForm.reset();//reset form for display
		reportForm.storeClonedForm();
		return errors;
	}
	/*@Autowired
	private EmployeeStatusHistoryReportValidator employeeStatusHistoryReportValidator;*/
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
		//binder.addValidators(employeeStatusHistoryReportValidator);
	}
}
