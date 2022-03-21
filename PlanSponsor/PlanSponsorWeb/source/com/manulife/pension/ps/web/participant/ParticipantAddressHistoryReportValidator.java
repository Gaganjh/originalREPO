package com.manulife.pension.ps.web.participant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressHistoryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.util.content.GenericException;
@Component
public class ParticipantAddressHistoryReportValidator extends ValidatorUtil implements Validator {
	protected static final String DOWNLOAD_COLUMN_HEADING_WITH_SSN =     
			 "Contract Number,Last Name,First Name,Middle Initial,SSN,Address Line1,Address Line2,City,State,Zip,Country,Employer Provided Email Address,Update,Source,Changed by,Segment";
	protected static final String DOWNLOAD_COLUMN_HEADING_WITH_EMPLOYEE_ID = 
			 "Contract Number,Last Name,First Name,Middle Initial,Employee ID,Address Line1,Address Line2,City,State,Zip,Country,Employer Provided Email Address,Update,Source,Changed by,Segment";	
	 protected static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
	 protected static final String FORMAT_DATE_SHORT_MDY_HH_MM = "MM/dd/yyyy HH:mm";
private static Logger logger = Logger.getLogger(ParticipantAddressHistoryReportValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		return ParticipantAddressHistoryReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult=(BindingResult)errors;
		if(!bindingResult.hasErrors()){
			Collection error = new ArrayList();
			String[] errorCodes = new String[10];
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		ParticipantAddressHistoryReportForm theForm = (ParticipantAddressHistoryReportForm) target;
		
		
		
		if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doValidate");
	    }


		// check if this is a 'search' of just the 'default' page load.
		//todo if (!getTask(request).equals(FILTER_TASK)) 
			
			  String namePhrase = theForm.getNamePhrase(); 		// last name
		

		if (getTask(request).equals(FILTER_TASK)) {
			/*if (namePhrase != null && namePhrase.trim().length() > 0) {
				NameRule.getLastNameInstance().validate(
						ParticipantAddressHistoryReportForm.FIELD_LAST_NAME, 
                        error, namePhrase);
                
			} */
			if (!theForm.getSsn().isEmpty()) {
				// SSN Number mandatory and standards must be met
				SsnRule.getInstance().validate(
					ParticipantAddressHistoryReportForm.FIELD_SSN,
					error, 
					theForm.getSsn()
				);
			}
		}
		
		// now for the two date validations, great fun.
		boolean validDates = false;
		Date toDate = null;
		Date fromDate = null; 
		
		// one is missing, but other is filled in
		if (
			((StringUtils.isEmpty(theForm.getFromDate())) && (!StringUtils.isEmpty(theForm.getToDate()))) ||
			((!StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate())))
			) {
			error.add(new GenericException(ErrorCodes.BOTH_DATES_EMPTY));
		}			
				
		//valid  To date or from date format
		if ((theForm.getToDate().trim().length() > 0) && (theForm.getFromDate().trim().length() > 0)){

			SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY); 
			format.setLenient(false);
			SimpleDateFormat formatWithHour = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY_HH_MM);
			formatWithHour.setLenient(false);
			try {
        		toDate = new Date(formatWithHour.parse(theForm.getToDate()+" 23:59").getTime());
        		fromDate = new Date(format.parse(theForm.getFromDate()).getTime());
       			validDates = true;
        	} catch(Exception e) {
				error.add(new GenericException(ErrorCodes.INVALID_DATE));
				validDates = false;
        	}        	
		}

		if (validDates){
			if ((!StringUtils.isEmpty(theForm.getFromDate())) && (!StringUtils.isEmpty(theForm.getToDate()))) {

				Calendar calToDate = Calendar.getInstance();
				Calendar calFromDate = Calendar.getInstance();
				calToDate.setTime(toDate);
				calFromDate.setTime(fromDate);
    
    			if (calFromDate.after(calToDate)) {
       				error.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
       			}
			}	
			
			if (!StringUtils.isEmpty(theForm.getFromDate())) {
				Calendar calEarliestDate = Calendar.getInstance();
				calEarliestDate.add(Calendar.MONTH, -24);
				Calendar calFromDate = Calendar.getInstance();
	   			
	   			calFromDate.setTime(fromDate);
						  		
				if (calFromDate.before(calEarliestDate)) {
	   				error.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
	   			} 
			}
		}	
 
		if (error.size() > 0) {
			ParticipantAddressHistoryReportController participantAddressHistoryReportAction=new ParticipantAddressHistoryReportController();
			participantAddressHistoryReportAction.populateReportForm( theForm, request);
			ParticipantAddressHistoryReportData reportData = theForm.getReport();
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

		}

	    if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doValidate");
	    }
		
	}

	}

