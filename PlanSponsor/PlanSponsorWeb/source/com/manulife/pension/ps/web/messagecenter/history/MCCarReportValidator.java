package com.manulife.pension.ps.web.messagecenter.history;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.home.SearchTPAForm;
import com.manulife.pension.ps.web.home.SearchTpaValidator;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.message.report.valueobject.MessageReportData;
import com.manulife.pension.util.content.GenericException;
@Component
public class MCCarReportValidator implements Validator {

private static Logger logger = Logger.getLogger(MCCarReportValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		return MCMessageReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Collection<GenericException> error = new ArrayList();
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		MCMessageHistoryForm reportForm = (MCMessageHistoryForm) target;
		BindingResult bindingResult = (BindingResult) errors;
		if (!bindingResult.hasErrors()) {
		
		//TODO this vallidator need to be used only for filter task if (getTask(request).equals(BaseReportAction.FILTER_TASK)) {
			
			reportForm.validate(error);
			if (error.size() > 0) {
				reportForm.sortErrors(error);
				MessageReportData reportData = new MessageReportData(null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				SessionHelper.setErrorsInSession(request, error);
			}
		}
	}
}
