package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.valueobject.ManageUsersReportData;
@Component
public class ManageUsersReportValidator extends ValidatorUtil implements Validator {

private static Logger logger = Logger.getLogger(ManageUsersReportValidator.class);
protected static final String FILTER_TASK = "filter";
protected static final String SHOWALL_TASK = "showAll";
	@Override
	public boolean supports(Class<?> arg0) {
		return ManageUsersReportForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors error) {
	
		Collection errors = new ArrayList();
		ManageUsersReportForm theForm = (ManageUsersReportForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}
		
		
		

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
		/*if (theForm.isSearchButtonHit() && (isManageTpaUsers(mapping) || isManageInternalUsers(mapping))) {
			request.setAttribute(DISPLAY_SHOW_ALL_KEY, "true");
		}*/

		/*
		 * If either the filter or the filter value is empty, return an error.
		 */
		/*if (FILTER_TASK.equals(task)
				|| ((isManageTpaUsers(mapping) || isManageInternalUsers(mapping)) && SORT_TASK.equals(task))
						&& !theForm.isShowAllButtonHit()) {
			if (filter == null || filter.trim().length() == 0 || filterValue == null
					|| filterValue.trim().length() == 0) {
				
				 * MPR 437 If user has not entered in at least one character before selecting
				 * search, system must display an error message
				 * "you must enter in at least one character to perform search"
				 
				errors.add(new GenericException(ErrorCodes.SEARCH_FIELD_MANDATORY));
			} else {
				
				 * MPR 438 If user entered in a numeric and user selected to search by last
				 * name, system must display an error message. If user entered in alpha
				 * characters and selected to search by TPA firm id, system must display an
				 * error message.
				 
				if (filter.equals(ManageUsersReportData.FILTER_EMPLOYEE_LAST_NAME)
						|| filter.equals(ManageUsersReportData.FILTER_TPA_LAST_NAME)) {
					NameRule.getLastNameInstance().validate(ManageUsersReportForm.FIELD_FILTER_VALUE, errors,
							filterValue);
				} else if (filter.equals(ManageUsersReportData.FILTER_EMPLOYEE_NUMBER)) {
					EmployeeNumberRule.getInstance().validate(ManageUsersReportForm.FIELD_FILTER_VALUE, errors,
							filterValue);
				} else if (filter.equals(ManageUsersReportData.FILTER_TPA_FIRM_ID)) {
					NumericRule rule = new NumericRule(ErrorCodes.TPA_FIRM_ID_INVALID);
					rule.validate(ManageUsersReportForm.FIELD_FILTER_VALUE, errors, filterValue);
				} else if (filter.equals(ManageUsersReportData.FILTER_INTERNAL_USER_ID)) {
					userIdRule.validate(ManageUsersReportForm.FIELD_FILTER_VALUE, errors, filterValue);
				}
			}
		}*/

		/*
		 * Resets the information for JSP to display.
		 */
		if (errors.size() > 0) {

			//populateReportForm(theForm, request);

			/*
			 * Puts an empty ReportData into the request.
			 */
			ManageUsersReportData reportData = new ManageUsersReportData();
			reportData.setUserInfo(
					SecurityServiceDelegate.getInstance().getUserInfo(getUserProfile(request).getPrincipal()));
			request.setAttribute(Constants.REPORT_BEAN, reportData);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}
		
	
	}

	
	
}
