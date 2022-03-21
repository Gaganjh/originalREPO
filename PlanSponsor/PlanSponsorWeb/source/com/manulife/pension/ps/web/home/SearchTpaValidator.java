package com.manulife.pension.ps.web.home;

import java.util.ArrayList;
import java.util.Collection;

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

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.TPAUsersReportData;
import com.manulife.pension.util.content.GenericException;

@Component
public class SearchTpaValidator implements Validator {

	public static final String RESULTS_MESSAGE_KEY = "resultsMessageKey";
	private final RegularExpressionRule tpaUserNameRERule = new RegularExpressionRule(
			ErrorCodes.TPA_USERNAME_VIOLATE_NAMING_STANDARD, Constants.FIRST_NAME_LAST_NAME_RE);

	private static Logger logger = Logger.getLogger(SearchTpaValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public void validate(Object target, Errors error) {
		BindingResult bindingResult = (BindingResult) error;
		if(!bindingResult.hasErrors()){
			ArrayList<GenericException> errors= new ArrayList<GenericException>();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		SearchTPAForm theForm = (SearchTPAForm) target;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}

		if ("Y".equals(request.getParameter("first"))) {
			theForm.reset();
		}

		if ("search".equals(request.getParameter("search"))) {

			if (theForm.getTpaFirmId() != null && theForm.getTpaFirmId().length() > 0) {
				if (!StringUtils.isNumeric(theForm.getTpaFirmId())) {
					errors.add(new GenericException(ErrorCodes.TPA_FIRMID_NON_NUMERIC));
				} else {
					theForm.setFilter(TPAUsersReportData.FILTER_TPA_FIRM_ID);
					theForm.setFilterValue(theForm.getTpaFirmId());
				}
			} else if (theForm.getTpaFirmName() != null && theForm.getTpaFirmName().length() > 0) {

				theForm.setFilter(TPAUsersReportData.FILTER_TPA_FIRM_NAME);
				theForm.setFilterValue(theForm.getTpaFirmName());

			} else if (theForm.getTpaUserName() != null && theForm.getTpaUserName().length() > 0) {
				boolean valid = tpaUserNameRERule.validate(TPAUsersReportData.FILTER_TPA_LAST_NAME, errors,
						theForm.getTpaUserName());
				if (valid) {
					theForm.setFilter(TPAUsersReportData.FILTER_TPA_LAST_NAME);
					theForm.setFilterValue(theForm.getTpaUserName());
				}
			} else {
				theForm.setFilter(null);
				theForm.setFilterValue(null);
			}
		}

		// Resets the information for JSP to display.
		String[] errorCodes = new String[10];
		if (errors.size() > 0) {
			for(GenericException errorEx :errors){
				errorEx.getMessage();
				errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
				bindingResult.addError(new ObjectError(error
						                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
				
			}
			populateReportForm(theForm, request);
			theForm.setStoredProcExecute("N");
			
			
			request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, error);
 			request.setAttribute(PsBaseUtil.ERROR_KEY, error);
			// Puts an empty ReportData into the request.
			TPAUsersReportData reportData = new TPAUsersReportData();

			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.getSession().setAttribute(CommonConstants.ERROR_RDRCT,CommonConstants.INPUT);
		
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}
	}
	}

	protected void populateReportForm(SearchTPAForm reportForm, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}
		String task = getTask(request);
		if (task.equals("default") || task.equals("sort") || task.equals("filter") || task.equals("print")
				|| task.equals("download") || task.equals("printPDF") || task.equals("downloadAll")) {
			reportForm.setPageNumber(1);
		}
		if (task.equals("default") || reportForm.getSortDirection() == null
				|| reportForm.getSortDirection().length() == 0) {
			reportForm.setSortDirection(getDefaultSortDirection());
		}
		if (task.equals("default") || reportForm.getSortField() == null || reportForm.getSortField().length() == 0) {
			reportForm.setSortField(getDefaultSort());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportForm");
		}
	}

	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	protected String getDefaultSort() {
		return TPAUsersReportData.SORT_FIELD_LAST_NAME;
	}

	protected String getTask(HttpServletRequest request) {
		String task = request.getParameter("task");
		if (task == null) {
			task = "default";
		}
		return task;
	}

}
