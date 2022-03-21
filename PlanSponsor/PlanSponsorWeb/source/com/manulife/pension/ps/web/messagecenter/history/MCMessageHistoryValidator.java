package com.manulife.pension.ps.web.messagecenter.history;

import java.util.ArrayList;
import java.util.Collection;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.home.SearchTPAForm;
import com.manulife.pension.ps.web.home.SearchTpaValidator;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.message.report.valueobject.MessageHistoryReportData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
@Component
public class MCMessageHistoryValidator extends ValidatorUtil  implements org.springframework.validation.Validator{

	private static final String FILTER_TASK = "filter";	
	
private static Logger logger = Logger.getLogger(MCMessageHistoryValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		return MCMessageHistoryForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Collection<GenericException> error = new ArrayList();
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		MCMessageHistoryForm form = (MCMessageHistoryForm) target;
	
		if (getTask(request).equals(FILTER_TASK)) {
			
			//must validate the contract id here since MCMessageHistoryForm is used by more than action,
			//and this is only applicable for the history portion
			if ( StringUtils.isNotEmpty(form.getContractId())) {
				try {
				Integer contractId = new Integer(form.getContractId()); 
				if(!getUserProfile(request).getMessageCenterAccessibleContracts().contains(contractId) ) {
					error.add(new ValidationError("contractId", ErrorCodes.CONTRACT_NUMBER_INVALID ));
				}
				} catch (NumberFormatException e ) {
					error.add(new ValidationError("contractId", ErrorCodes.CONTRACT_NUMBER_INVALID ));
				}
			}
			
			form.validate(error);
			if (error.size() > 0) {
				form.sortErrors(error);
				MessageHistoryReportData reportData = new MessageHistoryReportData(
						null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				SessionHelper.setErrorsInSession(request, error);
			}
		}
	
	}

}
