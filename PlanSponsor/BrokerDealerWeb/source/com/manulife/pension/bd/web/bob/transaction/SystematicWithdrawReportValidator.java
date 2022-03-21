package com.manulife.pension.bd.web.bob.transaction;

import java.util.ArrayList;
import java.util.List;

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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.util.ValidatorUtil;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.validator.ValidationError;
@Component
public class SystematicWithdrawReportValidator extends ValidatorUtil implements Validator {
	 protected static final String FILTER_TASK = "filter";
	public static final String LAST_NAME_LABEL = "Last Name";
	private static final String LAST_NAME = "last_name";
	private static final String SSN = "ssn";
	private static final String DEFAULT_SORT_FIELD = SystematicWithdrawReportData.SORT_FIELD_NAME;
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;
	private static final RegularExpressionRule lastNameRErule = new RegularExpressionRule(
			BDErrorCodes.LAST_NAME_INVALID,
			BDRuleConstants.FIRST_NAME_LAST_NAME_RE);
	private static final RegularExpressionRule ssnRErule = new RegularExpressionRule(
			BDErrorCodes.STMT_SSN_INVALID, BDRuleConstants.SSN_RE);
	private static Logger logger = Logger.getLogger(SystematicWithdrawReportValidator.class);
	@Override
	public boolean supports(Class clazz) {
		return SystematicWithdrawReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult = (BindingResult) errors;
		if(!bindingResult.hasErrors()){
		String[] errorCodes = new String[10];	
		
		SystematicWithdrawReportForm theForm = (SystematicWithdrawReportForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}
		
		
		String namePhrase = theForm.getCustomparticipantName();
		ArrayList<GenericException> error = new ArrayList<GenericException>();
		List<ValidationError> tempArrayList = new ArrayList<ValidationError>();
		if (FILTER_TASK.equals(getTask(request))) {
			if (StringUtils.isNotBlank(namePhrase)) {
				if ((!(lastNameRErule.validate(LAST_NAME, tempArrayList,
						namePhrase)))
						|| (namePhrase.length() > GlobalConstants.LAST_NAME_LENGTH_MAXIMUM)) {
					GenericException exception = new GenericException(
							BDErrorCodes.LAST_NAME_INVALID);
					error.add(exception);
				}
			}

			if (!theForm.getCustomSsn().isEmpty()) {
				int ssnErrorCode = BDErrorCodes.STMT_SSN_INVALID;

				if (!(ssnRErule.validate(SSN, tempArrayList,
						theForm.getCustomSsn()))) {
					GenericException exception = new GenericException(
							ssnErrorCode);
					error.add(exception);
				}
			}
		}
		if (!error.isEmpty()) {
			populateReportForm( theForm, request);
			SystematicWithdrawReportData reportData = new SystematicWithdrawReportData(
					null, 0);
			
			request.setAttribute(BDConstants.REPORT_BEAN, reportData);
			for (Object e : error) {
				if (e instanceof GenericException) {
					GenericException errorEx=(GenericException) e;
					errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
					bindingResult.addError(new ObjectError(errors
							                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
					
				}
				 request.getSession().removeAttribute(BdBaseController.ERROR_KEY);
					request.removeAttribute(BdBaseController.ERROR_KEY);
					request.getSession().setAttribute(BdBaseController.ERROR_KEY, error);
					request.setAttribute(BdBaseController.ERROR_KEY, error);
		}
			
			
			
			
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}
	}
		}
	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		
		String task = getTask(request);
		if (FILTER_TASK.equals(task)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
	}
		private String getDefaultSort() {
			return DEFAULT_SORT_FIELD;
		}
		private String getDefaultSortDirection() {
			return DEFAULT_SORT_DIRECTION;
		}
}
