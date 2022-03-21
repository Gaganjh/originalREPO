package com.manulife.pension.ps.web.contract;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.contract.util.PlanDataValidationErrors;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.contract.util.PlanConstants;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.validator.ValidationError;
@Component
public class PlanDataHistoryValidator extends ValidatorUtil implements  Validator {
	private static Logger logger = Logger.getLogger(PlanDataHistoryValidator.class);
	@Override
	public boolean supports(Class<?> arg0) {
		return PlanDataHistoryForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors errors) {
	
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		PlanDataHistoryForm historyForm = (PlanDataHistoryForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		
		

		try {
			if (getTask(request).equals(FILTER_TASK)) {
				PlanDataHistoryValidator validator = new PlanDataHistoryValidator();
				validator.validate(historyForm, (Errors) validationErrors);

				if (validationErrors != null && validationErrors.size() > 0) {
					PlanDataValidationErrors error = new PlanDataValidationErrors(
							validationErrors);
					request.setAttribute(PlanConstants.VALIDATION_ERRORS,
							error);
					//TODO below code need to be moved to controller
				/*	populateReportForm( historyForm, request);
					request.setAttribute(Constants.REPORT_BEAN, historyForm
							.getReport());*/
				}
			}
		} catch (Exception e) {
			logger
					.warn("Exception catched in PlanDataHistoryAction.doValidate:"
							+ e.getMessage());
		}

		
		
	}
}
