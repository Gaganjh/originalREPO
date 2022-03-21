package com.manulife.pension.ps.web.participant;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

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
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantStatementsReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.util.content.GenericException;
@Component
public class ParticipantStatementSearchValidator extends ValidatorUtil implements Validator {

private static Logger logger = Logger.getLogger(ParticipantStatementSearchValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		return ParticipantStatementSearchForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult = (BindingResult) errors;
		if(!bindingResult.hasErrors()){
		Collection error = new ArrayList();
		String[] errorCodes = new String[10];
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}
		ParticipantStatementSearchForm theForm = (ParticipantStatementSearchForm) target;
		
		
		String namePhrase = theForm.getNamePhrase();
		String firstName = theForm.getFirstName();

		if (getTask(request).equals(FILTER_TASK) || getTask(request).equals(PRINT_TASK)) {//QC Defect fix #6887
			if (namePhrase != null && namePhrase.trim().length() > 0) {
				NameRule.getLastNameInstance().validate(
						ParticipantStatementSearchForm.FIELD_LAST_NAME, error,
						namePhrase);
			}
			if (firstName != null && firstName.trim().length() > 0) {
				NameRule.getStmtFirstNameInstance().validate(
						ParticipantStatementSearchForm.FIELD_FIRST_NAME, error,
						firstName);
			}
			if (!theForm.getSsn().isEmpty()) {
				// SSN Number mandatory and standards must be met
				SsnRule.getInstance().validate(
						ParticipantStatementSearchForm.FIELD_SSN, error,
						theForm.getSsn());
			}
		}

		if (error.size() > 0) {
			/*ParticipantStatementSearchAction participantStatementSearchAction=new ParticipantStatementSearchAction();
			participantStatementSearchAction.populateReportForm( theForm, request);*/
			SessionHelper.setErrorsInSession(request, error);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
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

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}

	}
}
}
}
