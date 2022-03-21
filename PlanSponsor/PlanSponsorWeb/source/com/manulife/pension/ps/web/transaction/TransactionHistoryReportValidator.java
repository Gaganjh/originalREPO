package com.manulife.pension.ps.web.transaction;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.util.content.GenericException;
@Component
public class TransactionHistoryReportValidator implements Validator {
private static Logger logger = Logger.getLogger(TransactionHistoryReportValidator.class);
	
	@Override
	public boolean supports(Class<?> arg0) {
		return TransactionHistoryReportForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors error) {
	
		Collection errors = new ArrayList();
		TransactionHistoryReportForm actionForm = (TransactionHistoryReportForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
				

		// if this is called using the default URL i.e. no parameters
		// do not validate
		if (request.getParameterNames().hasMoreElements()) {
	
	
			if (actionForm.getFromDate() != null && actionForm.getToDate() != null) {
				if (Long.valueOf(actionForm.getFromDate()).longValue() > Long
						.valueOf(actionForm.getToDate()).longValue()) {
					errors.add(new GenericException(ErrorCodes.INVALID_DATE_RANGE));
				}
			}
	
			/*
			 * Resets the information for JSP to display.
			 */
			if (errors.size() > 0) {
				/*
				 * Repopulates action form and request with default information.
				 */
				//populateReportForm( actionForm, request);
				// signal the JSP to display the date dropdowns again for the user to change
				// their selection
				request.setAttribute("displayDates", "true");
			}
		}
		
		
	}
}
