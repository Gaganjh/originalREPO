package com.manulife.pension.ps.web.withdrawal;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.ContractNumberRule;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.ps.service.withdrawal.valueobject.SearchParticipantWithdrawalReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;


@Component
public class SearchParticipantRequestWithdrawalValidator extends ValidatorUtil  implements Validator{

private static Logger logger = Logger.getLogger(SearchParticipantRequestWithdrawalValidator.class);
public static final String ACTION_FORWARD_CANCEL = "cancel";

public static final String ACTION_FORWARD_PARTICIPANT_SEARCH = "participantSearch";

public static final String ACTION_FORWARD_LOAN_PARTICIPANT_SEARCH = "loanParticipantSearch";

public static final String ACTION_FORWARD_FROM_SESSION = "fromSession";

private static final String DEFAULT_SORT_FIELD = SearchParticipantWithdrawalReportData.SORT_PARTICIPANT_NAME;

private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;

private static final String SESSION_FORM = "searchParticipantReportForm";
@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult = (BindingResult) errors;
		if(!bindingResult.hasErrors()){
		Collection error = new ArrayList();
		String[] errorCodes = new String[25];
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		SearchParticipantRequestWithdrawalForm theForm = (SearchParticipantRequestWithdrawalForm) target;


       

        // do not validate since we leave or return to the page
        if (getTask(request).equalsIgnoreCase(ACTION_FORWARD_CANCEL)
                || getTask(request).equalsIgnoreCase(ACTION_FORWARD_PARTICIPANT_SEARCH)
                ||getTask(request).equalsIgnoreCase(ACTION_FORWARD_LOAN_PARTICIPANT_SEARCH)
                || getTask(request).equalsIgnoreCase(ACTION_FORWARD_FROM_SESSION)) {
            request.getSession().removeAttribute(TASK_KEY);
           return;
        }
        

        

        final String contractId = StringUtils.trim(theForm.getFilterContractId());
        theForm.setFilterContractId(contractId);

        if (StringUtils.isNotBlank(contractId)) {
            ContractNumberRule.getInstance().validate(
                    SearchParticipantWithdrawalReportData.FILTER_CONTRACT_ID, error, contractId);
        }

        final String lastName = StringUtils.trim(theForm.getFilterParticipantLastName());
        theForm.setFilterParticipantLastName(lastName);

        if (StringUtils.isNotBlank(lastName)) {
            NameRule.getLastNameInstance().validate(
                    SearchParticipantWithdrawalReportData.FILTER_LAST_NAME, error, lastName);
        }

        // Trim the extra spaces from the search fields.
        theForm.setSsnOne(StringUtils.trim(theForm.getSsnOne()));
        theForm.setSsnTwo(StringUtils.trim(theForm.getSsnTwo()));
        theForm.setSsnThree(StringUtils.trim(theForm.getSsnThree()));

        if (!theForm.getSsn().isEmpty()) {
            // SSN Number mandatory and standards must be met
            SsnRule.getInstance().validate(SearchParticipantWithdrawalReportData.FILTER_SSN,
                    error, theForm.getSsn());
        }

        if (theForm.getSsn() != null && theForm.getSsn().isEmpty()
                && StringUtils.isBlank(theForm.getFilterParticipantLastName())) {
            error.add(new ValidationError(new String[] { "lastName", "ssn" },
                    ErrorCodes.LAST_NAME_OR_SSN_MANDATORY));
        }

        UserProfile userProfile = (UserProfile) request.getSession(false).getAttribute(
                Constants.USERPROFILE_KEY);
        boolean isTPA = userProfile.getPrincipal().getRole().isTPA();
        String task = getTask(request);
        if (isTPA && userProfile.getCurrentContract() == null && StringUtils.isBlank(contractId)
                && !task.equalsIgnoreCase(DEFAULT_TASK)) {
            error.add(new ValidationError("contractId",
                    ErrorCodes.CONTRACT_NUMBER_MANDATORY_SEARCH));
        }

        // in order to keep the sql manageable, put the following tests here
        // but do not show any error on the page, just return empty list
        // WSP-36
        // FIXME - implement once available
        // WSP 37
        // FIXME - implement once available
        // WSP 39
        // FIXME - implement once available
        // WSP 40
        // FIXME - implement once available

        if(!error.isEmpty()){
			for (Object e : error) {
				if (e instanceof GenericException) {
					GenericException errorEx=(GenericException) e;
					errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
					bindingResult.addError(new ObjectError(errors
							                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
					
				}
				
			}
			if(request.getSession().getAttribute(CommonConstants.ERROR_RDRCT) == null){
		    	request.getSession().setAttribute(CommonConstants.ERROR_RDRCT, CommonConstants.ERROR_PAGE);
		    }
				
				request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
				request.removeAttribute(PsBaseUtil.ERROR_KEY);
				request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, error);
				request.setAttribute(PsBaseUtil.ERROR_KEY, error);
		}
        
		}
    	
	}
	 protected String getTask(final HttpServletRequest request) {
	        final HttpSession session = request.getSession();
	        String task = (String) session.getAttribute(TASK_KEY);
	        if (task == null) {
	            task = super.getTask(request);
	        }
			return task;
	        
}


	
}