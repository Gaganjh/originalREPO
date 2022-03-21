package com.manulife.pension.ps.web.participant.payrollSelfService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

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
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangesReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.util.content.GenericException;

@Component
public class PayrollSelfServiceReportValidator extends ValidatorUtil implements Validator {
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public boolean supports(Class clazz) {
		return PayrollSelfServiceChangesForm.class.equals(clazz);
	}
	private static final String FILTER_TASK = "filter";

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult = (BindingResult) errors;
		if (!bindingResult.hasErrors()) {
			// check if this is a 'search(task=filter)' of just the 'default' page load.
			if (!getTask(getRequest()).equals(FILTER_TASK)) {
				return;
			}
			
			PayrollSelfServiceChangesForm form = (PayrollSelfServiceChangesForm) target;
			Set<GenericException> errorSet = validateForm(form);
			if (!errorSet.isEmpty()) {
				collectError(errorSet, getRequest(), form, bindingResult, errors.getObjectName());
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}

	}
	
	public Set<GenericException> validateForm(PayrollSelfServiceChangesForm form){

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> PayrollSelfServiceReportValidator.validate");
		}
		Set<GenericException> errorSet = new LinkedHashSet<>();
		validateLastName(form, errorSet);
		validateSSN(form, errorSet);

		final boolean fromDateEmpty = StringUtils.isEmpty(form.getEffectiveDateFrom());
		final boolean toDateEmpty = StringUtils.isEmpty(form.getEffectiveDateTo());
		final boolean fromDateValid = validateDate(form.getEffectiveDateFrom());
		final boolean toDateValid = validateDate(form.getEffectiveDateTo());
		
		//A valid From Date cannot be more than 2 years earlier
		if (fromDateValid && isDateBeforeNYears(form.getEffectiveDateFrom(), 2) ) {
			errorSet.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS)); //2267
		}
		if (fromDateValid && toDateValid) {
			validateFromDateAfterTo(form.getEffectiveDateFrom(), form.getEffectiveDateTo(), errorSet);
		} else {
			if (fromDateEmpty) {
				errorSet.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY));// 2265
			}
			if ((!fromDateEmpty && !fromDateValid) || (!toDateEmpty && !toDateValid)) {
				errorSet.add(new GenericException(ErrorCodes.INVALID_DATE));// 2268
			}
		}
		return errorSet;
	}
	
	private HttpServletRequest getRequest() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attr.getRequest();
	}
	
	private void validateLastName(PayrollSelfServiceChangesForm form, Set<GenericException> error) {
		if (StringUtils.isNotEmpty(form.getLastName())) {
			NameRule.getLastNameInstance().validate(PayrollSelfServiceChangesReportData.FILTER_LAST_NAME, error,
					form.getLastName());
		}
	}
	
	private void validateSSN(PayrollSelfServiceChangesForm form, Set<GenericException> error) {
		if (Objects.nonNull(form.getSSN()) && !form.getSSN().isEmpty()) {
			// SSN Number mandatory and standards must be met
			SsnRule.getInstance().validate(PayrollSelfServiceChangesReportData.FILTER_SSN, error, form.getSSN());
		}
	}
	//Returns true if validation passed, otherwise false
	private boolean validateDate(String dateAsString) {
		try {
			if(StringUtils.isNotEmpty(dateAsString)) {
				LocalDate.parse(dateAsString, PayrollSelfServiceChangesReportData.DATE_FORMATTER);
			}else {
				return false;
			}			
		} catch (DateTimeParseException parseExp) {
			logger.error(parseExp.getMessage(),parseExp);
			return false;
		}
		return true;
	}
	
	private void validateFromDateAfterTo(String fromDateAsString, String toDateAsString , Set<GenericException> error) {
		
		try {
			LocalDate fromDate =LocalDate.parse(fromDateAsString, PayrollSelfServiceChangesReportData.DATE_FORMATTER);
			LocalDate toDate =LocalDate.parse(toDateAsString, PayrollSelfServiceChangesReportData.DATE_FORMATTER);
			if (fromDate.isAfter(toDate)) {
				error.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO)); // 2266
			}
		} catch (DateTimeParseException parseExp) {
			error.add(new GenericException(ErrorCodes.INVALID_DATE));// 2268
			logger.error(parseExp.getMessage(),parseExp);
		}
		
	}
	
	private static boolean isDateBeforeNYears(String fromDateAsString, int years) {
		if (years < 0) throw new IllegalArgumentException("Years cannot be less than 0");
		
		LocalDate fromDateLocalDate = LocalDate.parse(fromDateAsString, PayrollSelfServiceChangesReportData.DATE_FORMATTER);
		
		return fromDateLocalDate.isBefore(LocalDate.now().minusYears(years));
	}
	
	private void collectError(Set<GenericException> error, HttpServletRequest request, PayrollSelfServiceChangesForm form, BindingResult bindingResult, String objectName) {
		PayrollSelfServiceChangesReportData reportData = new PayrollSelfServiceChangesReportData(null, 0);
		new PayrollSelfServiceChangesController().populateReportForm(form, request);
		reportData.setDetails(Collections.emptyList());
		request.setAttribute(Constants.REPORT_BEAN, reportData);
		form.setReport(reportData);
		for (Object e : error) {
			if (e instanceof GenericException) {
				GenericException errorEx = (GenericException) e;
				String[] errorCodes = new String[] { Integer.toString(errorEx.getErrorCode()) };
				bindingResult.addError(new ObjectError(objectName, errorCodes, errorEx.getParams(),
						errorEx.getMessage()));

			}
			//request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
			request.setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
		}
	}
}
