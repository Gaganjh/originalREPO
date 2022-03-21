package com.manulife.pension.ps.web.census;

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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.GenericException;

@Component
public class CensusVestingReportValidator extends ValidatorUtil implements Validator  {

	
private static Logger logger = Logger.getLogger(CensusVestingReportValidator.class);
protected static final String FILTER_TASK = "filter";
	@Override
	public boolean supports(Class<?> clazz) {
		return CensusVestingReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		BindingResult bindingResult = (BindingResult) errors;
		if(!bindingResult.hasErrors()){
		Collection error = new ArrayList();
		String[] errorCodes = new String[10];
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();

		CensusVestingReportForm theForm = (CensusVestingReportForm) target;
		String namePhrase = theForm.getNamePhrase();
		try
		{
			if (logger.isDebugEnabled()) {
				logger.debug("entry -> doValidate");
			}
			
		if (getTask(request).equals(FILTER_TASK)) {
				if (namePhrase != null && namePhrase.trim().length() > 0) {
					
					NameRule.getLastNameInstance().validate(
							CensusVestingReportForm.FIELD_LAST_NAME, error,
							namePhrase);
	                
				}
				if (!theForm.getSsn().isEmpty()) {
					// SSN Number mandatory and standards must be met
					SsnRule.getInstance().validate(
							CensusVestingReportForm.FIELD_SSN, error,
							theForm.getSsn());
				}
			}

		
		if (!error.isEmpty()) {
			CensusVestingReportController censusVestingReportAction=new CensusVestingReportController();
			censusVestingReportAction.populateReportForm(theForm, request);
			SessionHelper.setErrorsInSession(request, error);
			CensusSummaryReportData reportData = new CensusSummaryReportData(
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
					request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, error);
					request.setAttribute(PsBaseUtil.ERROR_KEY, error);
		}
	}
			/*if (error.size() > 0) {
				
				SessionHelper.setErrorsInSession(request, error);
				CensusSummaryReportData reportData = new CensusSummaryReportData(
						null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
			}*/

			if (logger.isDebugEnabled()) {
				logger.debug("exit <- doValidate");
			}

			
		}catch (Exception e) {
	        e.printStackTrace();
		
		
		}
	}
}
}