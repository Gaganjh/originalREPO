package com.manulife.pension.ps.web.participant;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.ps.service.report.participant.valueobject.TaskCenterTasksReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.census.EmployeeEnrollmentSummaryReportForm;
import com.manulife.pension.ps.web.profiles.AddEditClientUserForm;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.ps.web.validation.pentest.PsValidator1;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.platform.web.CommonConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

@Component
public class TaskCenterTasksReportValidator extends ValidatorUtil implements Validator{
	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public boolean supports(Class clazz) {
		return TaskCenterTasksReportForm.class.equals(clazz);
	}
	
	private static List EMPTY_LIST = new LinkedList();
	
	private static String FILTER_TASK = "filter";
	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult=(BindingResult)errors;
		if(!bindingResult.hasErrors()){
		Collection error = new ArrayList();
		String[] errorCodes = new String[10];
		TaskCenterTasksReportForm form = (TaskCenterTasksReportForm)target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doValidate");
		}

		// check if this is a 'search(task=filter)' of just the 'default' page load.
		if (!getTask(request).equals(FILTER_TASK)) return;
		
		if (form.getLastName()!= null && form.getLastName().trim().length() > 0) {
			NameRule.getLastNameInstance().validate(
					EmployeeEnrollmentSummaryReportForm.FIELD_LAST_NAME, error, form.getLastName());
		}
        
		if (form.getSsn() != null && !form.getSsn().isEmpty()) {
			// SSN Number mandatory and standards must be met
			SsnRule.getInstance().validate(
				EmployeeEnrollmentSummaryReportForm.FIELD_SSN,
				error, 
				form.getSsn()
			);
		}

 		if (error.size() > 0) {
 			TaskCenterTasksReportController taskCenterTasksReportAction=new TaskCenterTasksReportController();
 			taskCenterTasksReportAction.populateReportForm( form, request);
            TaskCenterTasksReportData reportData = new TaskCenterTasksReportData(null, 0);
            reportData.setDetails(EMPTY_LIST);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("disableButtons", "disabled"); // disable cancel/save
			for (Object e : error) {
				if (e instanceof GenericException) {
					GenericException errorEx=(GenericException) e;
					errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
					bindingResult.addError(new ObjectError(errors
							                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
					
				}
				 request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
					request.removeAttribute(PsBaseUtil.ERROR_KEY);
					request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
					request.setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
		}
	
		}

		}

	    if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doValidate");
	    }
		
	}
}
