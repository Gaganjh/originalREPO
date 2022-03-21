package com.manulife.pension.ps.web.participant;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.validation.BindingResult;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressHistoryReportData;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressesReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.census.CensusSummaryReportForm;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.util.content.GenericException;
@Component
public class ParticipantAddressSummaryReportValidator extends ValidatorUtil  implements Validator {
	private static Logger logger = Logger.getLogger(ParticipantAddressSummaryReportValidator.class);
	
	@Override
	public boolean supports(Class<?> arg0) {
		return ParticipantAddressesReportForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult=(BindingResult)errors;
		if(!bindingResult.hasErrors()){
		Collection error = new ArrayList();
		String[] errorCodes = new String[10];
		ParticipantAddressesReportForm theForm = (ParticipantAddressesReportForm) target;
		String namePhrase = theForm.getNamePhrase();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}

		
		/*
		// check for current errors in session
		Collection currentErrors = SessionHelper.getErrorsInSession(request);
		// clean up empty error collections in session
		if (currentErrors != null && currentErrors.isEmpty()) {
			SessionHelper.removeErrorsInSession(request);
		}*/

		
		if (getTask(request).equals(FILTER_TASK)) {
			if (namePhrase != null && namePhrase.trim().length() > 0) {
				NameRule.getLastNameInstance().validate(
						ParticipantAddressesReportForm.FIELD_LAST_NAME, error,
						namePhrase);
                
			}
			if (!theForm.getSsn().isEmpty()) {
				// SSN Number mandatory and standards must be met
				SsnRule.getInstance().validate(
						ParticipantAddressesReportForm.FIELD_SSN, error,
						theForm.getSsn());
			}
		}

		if (error.size() > 0) {
			ParticipantAddressSummaryReportController ParticipantAddressSummaryReportAction=new ParticipantAddressSummaryReportController();
			ParticipantAddressSummaryReportAction.populateReportForm( theForm, request);
			ParticipantAddressesReportData reportData = new ParticipantAddressesReportData(
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
	}
		
		
		
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}

		
	}

	
	
	
	
	
}
