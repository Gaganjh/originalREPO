package com.manulife.pension.ps.web.census;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.ps.service.report.census.valueobject.EligibilityReportData;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.util.content.GenericException;

@Component
public class EligibilityIssuesReportValidator implements Validator {
	
private static Logger logger = Logger.getLogger(EligibilityIssuesReportValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		return EligibilityReportsForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		GenericException genericException = null;
		Collection errorCollection = new ArrayList();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest httpServletRequest = attr.getRequest();
		EligibilityReportsForm eligibilityReportsForm = (EligibilityReportsForm) target;
	
		if (!EligibilityReportData.ELIGIBILITY_ISSUES_REPORT
				.equalsIgnoreCase(httpServletRequest.getParameter(EligibilityReportData.REPORT_TYPE))) {
			this.setDefaultFromAndToDate(eligibilityReportsForm);
			
		}

		try {
			EligibilityReportData.SIMPLE_DATE_FORMAT.setLenient(false);
			Date fromDate = null;
			if (!StringUtils.isEmpty(eligibilityReportsForm
					.getReportedFromDate())) {
				fromDate = EligibilityReportData.dateParseMMDDYY(eligibilityReportsForm.getReportedFromDate());
			}

			Date toDate = null;
			if (!StringUtils
					.isEmpty(eligibilityReportsForm.getReportedToDate())) {
				toDate = EligibilityReportData.dateParseMMDDYY(eligibilityReportsForm.getReportedToDate());
			}

			if (fromDate != null) {
				
				Date currentDate = new Date();

				Calendar calFromDate = Calendar.getInstance();
				calFromDate.setTime(fromDate);

				Calendar calEarliestDate = Calendar.getInstance();
				calEarliestDate.add(Calendar.MONTH, -24);
				if (calFromDate.before(calEarliestDate) || calFromDate.after(Calendar.getInstance())) {
					genericException = new GenericException(
							ErrorCodes.FROM_DATE_BEFORE_24_MONTHS); // 2267
					errorCollection.add(genericException);
					
					
				}

				
				if (errorCollection.size() == 0 && fromDate.after(toDate)) {
					genericException = new GenericException(
							ErrorCodes.FROM_DATE_AFTER_TO); // 2266
					errorCollection.add(genericException);
				}
			} else {
				genericException = new GenericException(
						ErrorCodes.FROM_DATE_EMPTY); // 2265
				errorCollection.add(genericException);
			}

		} catch (ParseException parseException) {
			genericException = new GenericException(ErrorCodes.INVALID_DATE); // 2268
			errorCollection.add(genericException);
		}
	
	
	
	
	}

	private void setDefaultFromAndToDate(EligibilityReportsForm eligibilityReportsForm) {
		// TODO Auto-generated method stub
		
	}
}
