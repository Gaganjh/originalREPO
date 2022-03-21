package com.manulife.pension.ps.web.census;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeEnrollmentSummaryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.util.content.GenericException;
@Component
public class EmployeeEnrollmentSummaryReportValidator extends ValidatorUtil implements Validator {


	private static Logger logger = Logger.getLogger(EmployeeEnrollmentSummaryReportValidator.class);
	 private static final FastDateFormat DATE_FORMAT = ContractDateHelper.getDateFormatter("MM/dd/yyyy");
	    private static final FastDateFormat DATE_FORMAT_DB = ContractDateHelper.getDateFormatter("yyyy-MM-dd");
	    private static final String CALCULATE_TASK = "calculate";
	
	@Override
	public boolean supports(Class<?> arg0) {
		return EmployeeEnrollmentSummaryReportForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors errors) {
	
		Collection error = new ArrayList();
		BindingResult bindingResult = (BindingResult)errors;
		String[] errorCodes = new String[10];
   		
		EmployeeEnrollmentSummaryReportForm theForm = (EmployeeEnrollmentSummaryReportForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doValidate");
	    }
	   
	  
    	
		
 		if (getTask(request).equals(FILTER_TASK)) {
			if (theForm.getNamePhrase() != null && theForm.getNamePhrase().trim().length() > 0) {
				/*NameRule.getLastNameInstance().validate(
						EmployeeEnrollmentSummaryReportForm.FIELD_LAST_NAME, errors, 
                        theForm.getNamePhrase());
                */
			}
            
			if (theForm.getSsn() != null && !theForm.getSsn().isEmpty()) {
				// SSN Number mandatory and standards must be met
				SsnRule.getInstance().validate(
					EmployeeEnrollmentSummaryReportForm.FIELD_SSN,
					error, 
					theForm.getSsn()
				);
			}
            
            // From date and To date validation
			if (StringUtils.isEmpty(theForm.getFromPED()) &&
            		!StringUtils.isEmpty(theForm.getToPED())) {
				error.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY));
            	}
            
            	if ( !StringUtils.isEmpty(theForm.getFromPED()) && StringUtils.isEmpty(theForm.getToPED())){
            		 // EES.375
            		Date now = new Date(System.currentTimeMillis());
            		String currentDate = DATE_FORMAT.format(now);
            		theForm.setToPED(currentDate);
            	}
            	
            	try {
            		Date fromDate = null;
            		if (!StringUtils.isEmpty(theForm.getFromPED())) {
            			fromDate = DATE_FORMAT.parse(theForm.getFromPED());
            		}
            		Date toDate = null;
            		if (!StringUtils.isEmpty(theForm.getToPED())) {
            			toDate = DATE_FORMAT.parse(theForm.getToPED());
            		}
            		if (fromDate !=null) {
	                    if (fromDate.after(toDate)) {
	                        error.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
	                    }
	                    
	                    Calendar calFromDate = Calendar.getInstance();
	                    calFromDate.setTime(fromDate);
	                    
	    				Calendar calEarliestDate = Calendar.getInstance();
	    				calEarliestDate.add(Calendar.MONTH, -24);
	    				if (calFromDate.before(calEarliestDate)) {
	    					error.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
	    				}
            		}
            	} catch(ParseException pe) {
            		error.add(new GenericException(ErrorCodes.INVALID_DATE));
            	} 
            
         //   if (theForm.isEZstartOn()==false) { // aci2 stuff, EES.374
            	if ( StringUtils.isEmpty(theForm.getEnrolledFrom()) && !StringUtils.isEmpty(theForm.getEnrolledTo()) ){
            		error.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY));
            	}
            
            	if (!StringUtils.isEmpty(theForm.getEnrolledFrom()) && StringUtils.isEmpty(theForm.getEnrolledTo())){
            		Date now = new Date(System.currentTimeMillis());
            		String currentDate = DATE_FORMAT.format(now);
            		theForm.setEnrolledTo(currentDate);
            	}
            	
            	try {
            		Date fromDate = null;
            		if (!StringUtils.isEmpty(theForm.getEnrolledFrom())) {
            			fromDate = DATE_FORMAT.parse(theForm.getEnrolledFrom());
            		}
            		Date toDate = null;
            		if (!StringUtils.isEmpty(theForm.getEnrolledTo())) {
            			toDate = DATE_FORMAT.parse(theForm.getEnrolledTo());
            		}
            		if (fromDate !=null) {
	                    if (fromDate.after(toDate)) {
	                    	error.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
	                    }
	                    
	                    Calendar calFromDate = Calendar.getInstance();
	                    calFromDate.setTime(fromDate);
	                    
	    				Calendar calEarliestDate = Calendar.getInstance();
	    				calEarliestDate.add(Calendar.MONTH, -24);
	    				if (calFromDate.before(calEarliestDate)) {
	    					error.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
	    				}
            		}
            	} catch(ParseException pe) {
            		error.add(new GenericException(ErrorCodes.INVALID_DATE));
            	}
            	
            //}
		}
	
 		if (error.size() > 0) {
 			//populateReportForm( theForm, request);
			SessionHelper.setErrorsInSession(request, error);
            EmployeeEnrollmentSummaryReportData reportData = new EmployeeEnrollmentSummaryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			 for (Object e : error) {
					if (e instanceof GenericException) {
						GenericException errorEx=(GenericException) e;
						errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
						bindingResult.addError(new ObjectError(errors
								                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
					}
						
					}
             if(request.getSession().getAttribute(CommonConstants.ERROR_RDRCT) == null){
			    	request.getSession().setAttribute(CommonConstants.ERROR_RDRCT, CommonConstants.ERROR_PAGE);
			    }
             
             request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
 			request.removeAttribute(PsBaseUtil.ERROR_KEY);
 			request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, error);
  			request.setAttribute(PsBaseUtil.ERROR_KEY, error);
		}
 	    if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doValidate");
	    }
		
		
		
		
		
		
		
		
	}
	}


