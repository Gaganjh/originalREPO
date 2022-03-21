package com.manulife.pension.ps.web.transaction;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.util.content.GenericException;


@Component
public class CashAmountReportValidator  implements Validator{

	

	@Override
	public boolean supports(Class<?> clazz) {
		return CashAccountReportForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult = (BindingResult)errors;
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		CashAccountReportForm actionForm = (CashAccountReportForm) target;
		Collection errorList = new ArrayList();
		
		try {
			if (request.getParameterNames().hasMoreElements()) {

				
		
				if (actionForm.getFromDate() != null && actionForm.getToDate() != null) {
					if (Long.valueOf(actionForm.getFromDate()).longValue() > Long
							.valueOf(actionForm.getToDate()).longValue()) {
						errorList.add(new GenericException(ErrorCodes.INVALID_DATE_RANGE));
					}
				}
		
				/*
				 * Resets the information for JSP to display.
				 */
				if (errorList.size() > 0) {
					/*
					 * Repopulates action form and request with default information.
					 */
				//TODO code move inside controller handler method
				}
			}
			
			
		}catch (Exception e) {
			
		}
		
		
	}

	
	
	
}
