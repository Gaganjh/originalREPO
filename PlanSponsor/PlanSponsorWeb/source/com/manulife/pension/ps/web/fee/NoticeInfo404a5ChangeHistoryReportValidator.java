package com.manulife.pension.ps.web.fee;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
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

import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.converter.ConverterHelper;



@Component
public class NoticeInfo404a5ChangeHistoryReportValidator  extends ValidatorUtil implements Validator {

private static Logger logger = Logger.getLogger(NoticeInfo404a5ChangeHistoryReportValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		return NoticeInfo404a5ChangeHistoryReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		String[] errorCodes = new String[10];
		BindingResult bindingResult = (BindingResult)errors;

		Collection<GenericException> error = new ArrayList<GenericException>();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		NoticeInfo404a5ChangeHistoryReportForm theForm = (NoticeInfo404a5ChangeHistoryReportForm) target;
	
		if(!bindingResult.hasErrors()){
	 
		// do not validate since the filters are going to be reset
		if (getTask(request).equalsIgnoreCase(DEFAULT_TASK)) {
			error.add(new GenericException(error));
		}

		Date fromDate = null;
		Date toDate = null;
		
		final DateFormat dateFormat = ConverterHelper.getDefaultDateFormat();
		dateFormat.setLenient(false);
		
		final String fromDateStr = StringUtils.trim(theForm.getFromDate());
		final String toDateStr = StringUtils.trim(theForm.getToDate());

		theForm.setFromDate(fromDateStr);
		theForm.setToDate(toDateStr);

		if (StringUtils.isNotBlank(fromDateStr)) {
			try {
				fromDate = dateFormat.parse(fromDateStr);
			} catch (ParseException parseException) {
				error.add(new GenericException(ErrorCodes.INVALID_FROM_DATE_FORMAT));
			}
		} else {
			error.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY));
		}

		if (StringUtils.isNotBlank(toDateStr)) {
			try {
				toDate = dateFormat.parse(toDateStr);
			} catch (ParseException e) {
				error.add(new GenericException(ErrorCodes.INVALID_TO_DATE_FORMAT));
			}
		} else {
			error.add(new GenericException(ErrorCodes.TO_DATE_EMPTY));
		}

		if (fromDate != null && toDate != null) {
			if (fromDate.after(toDate)) {
				error.add(new GenericException(ErrorCodes.FROM_DATE_GREATER_THAN_TO_DATE));
			}
		}
	
		if ( !error.isEmpty()) {
			
			for(GenericException errorEx :error){
				errorEx.getMessage();
				errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
				bindingResult.addError(new ObjectError((errors)
						.getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
				
			}
			request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, errors);
			request.setAttribute(PsBaseUtil.ERROR_KEY, errors);
			
		}	
	}

	}

	
}
