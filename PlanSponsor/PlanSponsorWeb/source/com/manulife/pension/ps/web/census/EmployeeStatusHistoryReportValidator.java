package com.manulife.pension.ps.web.census;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeStatusHistoryDetails;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeStatusHistoryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.census.util.EmployeeStatusHistoryReportHelper;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.exception.SystemException;
@Component
public class EmployeeStatusHistoryReportValidator extends ValidatorUtil implements Validator{
private static Logger logger = Logger.getLogger(EmployeeStatusHistoryReportValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		return EmployeeStatusHistoryReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		EmployeeStatusHistoryReportForm reportForm = (EmployeeStatusHistoryReportForm) target;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}
		String profileId = null;
		request.removeAttribute("psErrors");
		Collection<GenericException> error = new ArrayList<GenericException>();
		profileId = reportForm.getProfileId();
		if (profileId == null) {
			profileId = (String) request.getParameter("profileId");
			if (profileId != null) {
				reportForm.setProfileId(profileId);
			} else {
				System.out.println("profileId is null in request");
			return;
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
		 if (currentEffectiveDate == null && deletionCount > 0)
			//errors.add(user trying to delete all the records)
			error
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
				error.add(new GenericException(
						ErrorCodes.ERROR_EFF_DATE_LESS_DOB));//BirthDateEmploymentStatusEffDate 7038
			// Rule 29 // LS.cannot be prior to higher date
			valid = EmployeeStatusHistoryReportHelper
					.isValidByYearDiscrepancyRule(employee
							.getEmployeeDetailVO().getHireDate(),
							currentEffectiveDate, 0);
			if (!valid)
				error.add(new GenericException(
						ErrorCodes.ERROR_EFF_DATE_LESS_HIRE_DATE)); //EmploymentStatusEffDateHireDate code 7039
		}
		if(!error.isEmpty()){
			setErrorsInSession(request, error);
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
	}
	protected EmployeeStatusHistoryReportData populateReport(
			ReportForm form, HttpServletRequest request) {
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		EmployeeStatusHistoryReportForm sform = (EmployeeStatusHistoryReportForm) form;
		ReportCriteria criteria = new ReportCriteria(
				EmployeeStatusHistoryReportData.REPORT_ID);
		criteria.addFilter(
				EmployeeStatusHistoryReportData.FILTER_CONTRACT_NUMBER, Integer
						.toString(currentContract.getContractNumber()));
		criteria.addFilter(EmployeeStatusHistoryReportData.FILTER_PROFILE_ID,
				sform.getProfileId());

		EmployeeStatusHistoryReportData reportData = new EmployeeStatusHistoryReportData(
				criteria, sform.getTheItemList().size());
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
	protected void setErrorsInSession(HttpServletRequest request,
			Collection errors) {
		BaseSessionHelper.setErrorsInSession(request, errors);
		
}
	}



