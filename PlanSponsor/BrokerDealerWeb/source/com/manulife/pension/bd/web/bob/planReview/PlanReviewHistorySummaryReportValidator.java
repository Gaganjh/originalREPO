package com.manulife.pension.bd.web.bob.planReview;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.bd.web.util.ValidatorUtil;
import com.manulife.pension.util.content.GenericException;
@Component
public class PlanReviewHistorySummaryReportValidator extends ValidatorUtil implements Validator {
	 protected static final String FILTER_TASK = "filter";
	private static Logger logger = Logger.getLogger(PlanReviewHistorySummaryReportValidator.class);
	@Override
	public boolean supports(Class clazz) {
		return PlanReviewReportHistoryForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult = (BindingResult)errors;		
		
		PlanReviewReportHistoryForm reportForm = (PlanReviewReportHistoryForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();

		String task = request.getParameter(TASK_KEY);

		// do not validate since the filters are going to be reset
		if (!FILTER_TASK.equalsIgnoreCase(task)) {
			
		}

		
		
		//validateSearchParameters(errorMessages, reportForm, request, mapping.getParameter());
	
	}	
}
