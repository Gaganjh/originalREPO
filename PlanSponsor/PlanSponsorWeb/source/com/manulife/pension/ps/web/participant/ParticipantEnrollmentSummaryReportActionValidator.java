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
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantEnrollmentSummaryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.DateRender;
@Component
public class ParticipantEnrollmentSummaryReportActionValidator extends ValidatorUtil implements Validator{

		private static Logger logger = Logger.getLogger(ParticipantAddressSummaryReportValidator.class);
		public static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
		@Override
		public boolean supports(Class<?> arg0) {
			return ParticipantEnrollmentSummaryReportForm.class.equals(arg0);
		}

		@Override
		public void validate(Object target, Errors errors) {
			BindingResult bindingResult = (BindingResult) errors;
			if(!bindingResult.hasErrors()){
			Collection error = new ArrayList();
			String[] errorCodes = new String[10];
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request = attr.getRequest();
			if(logger.isDebugEnabled()) {
			    logger.debug("entry -> doValidate");
		    }
	 	
		    String task = getTask(request);	
		    
			ParticipantEnrollmentSummaryReportForm theForm = (ParticipantEnrollmentSummaryReportForm) target;
				

	if (getTask(request).equals(FILTER_TASK)) {
			Calendar calToDate;
			Calendar calFromDate;
			Date fromDate = new Date();
			Date toDate = new Date();
			boolean validDates = false;
			boolean validFromDate = false;
			boolean validFromDateRange = false;

			//if only the from date is empty
			if((StringUtils.isEmpty(theForm.getFromDate()))  && (!StringUtils.isEmpty(theForm.getToDate()))) {
				error.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY));
			}	

			//if both dates are empty		
			if ((StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
				error.add(new GenericException(ErrorCodes.BOTH_DATES_EMPTY));
			}	


			//valid  To date or from date format
			if (theForm.getToDate() != null && theForm.getFromDate() != null && 
	                theForm.getToDate().trim().length() > 0 && theForm.getFromDate().trim().length() > 0){

				SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);
				format.setLenient(false);
				
				try {
	        		toDate = new Date(format.parse(theForm.getToDate()).getTime());
	        		fromDate = new Date(format.parse(theForm.getFromDate()).getTime());
	       			validFromDate = true;
	       			validDates = true;
	        	} catch(Exception e) {
					error.add(new GenericException(ErrorCodes.INVALID_DATE));
					validDates = false;
					validFromDate = false;
	        	}        	
			}


			//empty From date, invalid to date
			if ((StringUtils.isEmpty(theForm.getFromDate())) && 
	            (theForm.getToDate() != null && theForm.getToDate().trim().length() > 0)){

				SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);
				format.setLenient(false);
				
				try {
	        		toDate = new Date(format.parse(theForm.getToDate()).getTime());
	       			validDates = true;
	        	} catch(Exception e) {
					error.add(new GenericException(ErrorCodes.INVALID_DATE));
					validDates = false;
	        	}        	
			}


			//invalid  from date format and empty to date
			if ((theForm.getFromDate() != null && theForm.getFromDate().trim().length() > 0) && 
	            (theForm.getToDate() == null || theForm.getToDate().trim().length() == 0)){

				SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);
				format.setLenient(false);
				
				try {
	        		fromDate = new Date(format.parse(theForm.getFromDate()).getTime());
	       			validFromDate = true;
	        	} catch(Exception e) {
					error.add(new GenericException(ErrorCodes.INVALID_DATE));
					validFromDate = false;
	        	}        	
			}

			//if from date is populated and to date is empty - and from date is greater than toDate
			if (validFromDate){
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
					calToDate = Calendar.getInstance();
					calFromDate = Calendar.getInstance();
					calToDate.setTime(toDate);
					calFromDate.setTime(fromDate);
	    			
	    			
	    			if (calFromDate.after(calToDate)) {
	       				error.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
	       				validFromDateRange = false;
	       			} else {
	       				validFromDateRange = true;
	    			}	
	       			
				}
			}				


			//if from date is populated and to date is empty - and from date not within the last 24 months of today
			if (validFromDate){
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
					Calendar calEarliestDate = Calendar.getInstance();
					calEarliestDate.add(Calendar.MONTH, -24);
					calFromDate = Calendar.getInstance();
	       			
	       			calFromDate.setTime(fromDate);
	 				
					  		
	    			if (calFromDate.before(calEarliestDate)) {
	       				error.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
	       				validFromDateRange = false;
	       			} else {
	       				validFromDateRange = true;
	    			}	
				}
			}


			//if from date is populated, the date range is corret, and to date is empty - then default the toDate to current date
			if (validFromDateRange){
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
					calToDate = Calendar.getInstance();
					Date dtToDate = calToDate.getTime();
					theForm.setToDate(DateRender.formatByPattern(dtToDate, "", FORMAT_DATE_SHORT_MDY));
				}
			}		
		
			//From date must be earlier than To date
			if (validDates){
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (!StringUtils.isEmpty(theForm.getToDate()))) {

					calToDate = Calendar.getInstance();
					calFromDate = Calendar.getInstance();
					calToDate.setTime(toDate);
					calFromDate.setTime(fromDate);
	    
	    			if (calFromDate.after(calToDate)) {
	       				error.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
	       			}
				}	
			}	
			
			//From date must be within last 24 months of today
			if (validDates){
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (!StringUtils.isEmpty(theForm.getToDate()))) {
					Calendar calEarliestDate = Calendar.getInstance();
					calEarliestDate.add(Calendar.MONTH, -24);
					calFromDate = Calendar.getInstance();
	       			
	       			calFromDate.setTime(fromDate);
	 			
					  		
	    			if (calFromDate.before(calEarliestDate)) {
	       				error.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
	       			}
				}
			}		
	}

			String namePhrase = theForm.getNamePhrase();
	 		if (getTask(request).equals(FILTER_TASK)) {
				if (namePhrase != null && namePhrase.trim().length() > 0) {
					NameRule.getLastNameInstance().validate(
							ParticipantEnrollmentSummaryReportForm.FIELD_LAST_NAME, error, namePhrase);
				}
				if (!theForm.getSsn().isEmpty()) {
					// SSN Number mandatory and standards must be met
					SsnRule.getInstance().validate(
						ParticipantEnrollmentSummaryReportForm.FIELD_SSN,
						error, 
						theForm.getSsn()
					);
				}
			}
		
	 		if (error.size() > 0) {
	 			ParticipantEnrollmentSummaryReportController participantEnrollmentSummaryReportAction=new ParticipantEnrollmentSummaryReportController();
	 			participantEnrollmentSummaryReportAction.populateReportForm( theForm, request);
				ParticipantEnrollmentSummaryReportData reportData = new ParticipantEnrollmentSummaryReportData(null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
			}
	 	    if(logger.isDebugEnabled()) {
			    logger.debug("exit <- doValidate");
		    }
		}
		
}
}
