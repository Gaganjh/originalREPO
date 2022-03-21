package com.manulife.pension.ps.web.participant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.census.EmployeeEnrollmentSummaryReportForm;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.report.participant.transaction.valueobject.TaskCenterHistoryReportData;
import com.manulife.pension.util.content.GenericException;
@Component
public class TaskCenterHistoryReportValidator extends ValidatorUtil  implements Validator{

	private static List EMPTY_LIST = new LinkedList();
	private static final String FILTER_TASK = "filter";
	private static Logger logger = Logger.getLogger(TaskCenterHistoryReportValidator.class);
	@Override
	public boolean supports(Class<?> arg0) {
		return TaskCenterHistoryReportForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult=(BindingResult)errors;
		if(!bindingResult.hasErrors()){
		Collection error = new ArrayList();
		String[] errorCodes = new String[10];
		TaskCenterHistoryReportForm form = (TaskCenterHistoryReportForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();

				// check if this is a 'search(task=filter)' of just the 'default' page load.
				if (!getTask(request).equals(FILTER_TASK))
					return ;
				
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
				
				// validate dates 
				// now for the two date validations, great fun.
				boolean validDates = false;
				Date toDate = null;
				Date fromDate = null; 
				
				// one is missing, but other is filled in
				if (
					((StringUtils.isEmpty(form.getFromDate())) && (!StringUtils.isEmpty(form.getToDate()))) ||
					((!StringUtils.isEmpty(form.getFromDate())) && (StringUtils.isEmpty(form.getToDate())))
					) {
					error.add(new GenericException(ErrorCodes.BOTH_DATES_EMPTY));
				}			
						
				// validate To date or from date format
				if ((form.getToDate().trim().length() > 0) && (form.getFromDate().trim().length() > 0)){
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy"); 
					format.setLenient(false);
					try {
		        		toDate = new Date(format.parse(form.getToDate()).getTime());
		        		fromDate = new Date(format.parse(form.getFromDate()).getTime());
		       			validDates = true;
		        	} catch(Exception e) {
						error.add(new GenericException(ErrorCodes.INVALID_DATE));
						validDates = false;
		        	}        	
				}

				if (validDates) {
					if ((!StringUtils.isEmpty(form.getFromDate())) && (!StringUtils.isEmpty(form.getToDate()))) {
						Calendar calToDate = Calendar.getInstance();
						Calendar calFromDate = Calendar.getInstance();
						calToDate.setTime(toDate);
						calFromDate.setTime(fromDate);
		    
		    			if (calFromDate.after(calToDate)) {
		       				error.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
		       			}
					}	
					
					if (!StringUtils.isEmpty(form.getFromDate())) {
						Calendar calEarliestDate = Calendar.getInstance();
						calEarliestDate.add(Calendar.MONTH, -24);
						
						Calendar calFromDate = Calendar.getInstance();
			   			calFromDate.setTime(fromDate);
						calFromDate.set(Calendar.HOUR_OF_DAY, 23);
						calFromDate.set(Calendar.MINUTE, 59);


						if (calFromDate.before(calEarliestDate)) {
			   				error.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
			   			} 
					}
				}	
				

		 		if (error.size() > 0) {
		 			TaskCenterHistoryReportController taskCenterHistoryReportAction=new TaskCenterHistoryReportController(); 
		 			taskCenterHistoryReportAction.populateReportForm( form, request);
		            TaskCenterHistoryReportData reportData = new TaskCenterHistoryReportData(null, 0);
		            reportData.setDetails(EMPTY_LIST);
		    		request.setAttribute("enablePrint", Boolean.FALSE);
					request.setAttribute(Constants.REPORT_BEAN, reportData);
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

			    if(logger.isDebugEnabled()) {
				    logger.debug("exit <- doValidate");
				    }
}
	
}
}
