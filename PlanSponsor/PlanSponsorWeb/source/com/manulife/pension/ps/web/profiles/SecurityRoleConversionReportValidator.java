package com.manulife.pension.ps.web.profiles;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.validation.rules.ContractNumberNoMandatoryRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.tools.BusinessConversionForm;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.GenericException;



@Component
public class SecurityRoleConversionReportValidator extends ValidatorUtil implements Validator {
	private static Logger logger = Logger.getLogger(SecurityRoleConversionReportValidator.class);
	

	@Override
	public boolean supports(Class<?> arg0) {
		return SecurityRoleConversionReportForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors error) {

		ArrayList<GenericException> errors = new ArrayList<GenericException>();
		String[] errorCodes = new String[10];
		BindingResult bindingResult = (BindingResult)error;
		SecurityRoleConversionReportForm sForm = (SecurityRoleConversionReportForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		UserProfile userProfile = SessionHelper.getUserProfile(request);


		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}

		//This code has been changed and added  to Validate form and request
		//against penetration attack, prior to other validations as part of the CL#137697.


		if(!bindingResult.hasErrors()){

			String task = getTask(request);
			if (FILTER_TASK.equals(task) || SORT_TASK.equals(task) || DOWNLOAD_TASK.equals(task)) {
				if (sForm.getContractNumber() != null && sForm.getContractNumber().length() > 0) {
					// General contract number rule SCR 35
					boolean isValidFormat = ContractNumberNoMandatoryRule.getInstance().validate(
							BusinessConversionForm.FIELD_CONTRACT_NUMBER, errors,
							sForm.getContractNumber());

					if (isValidFormat) {
						Contract contract = null;
						try {

							contract = getContract(sForm.getContractNumber());
							if (!isValidContractStatus(contract)) {
								errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
							}
						} catch (ContractNotExistException ce) {
							// contract does not exist
							errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
						} catch (SystemException se) {
							errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
						}
					}
				}
				// check date of change
				validateDates(sForm.getFromDate(), sForm.getToDate(), errors);
				if ( !errors.isEmpty()) {

					for(GenericException errorEx :errors){
						errorEx.getMessage();
						errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
						bindingResult.addError(new ObjectError((error)
								.getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));

					}
					request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
					request.removeAttribute(PsBaseUtil.ERROR_KEY);
					request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, errors);
					request.setAttribute(PsBaseUtil.ERROR_KEY, errors);

				}	
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}

	}

	private boolean isValidContractStatus(Contract contract) {
		return (Contract.STATUS_PROPOSAL_SIGNED.equals(contract.getStatus())
				|| Contract.STATUS_DETAILS_COMPLETED.equals(contract.getStatus())
				|| Contract.STATUS_PENDING_CONTRACT_APPROVAL.equals(contract.getStatus())
				|| Contract.STATUS_CONTRACT_APPROVED.equals(contract.getStatus())
				|| Contract.STATUS_ACTIVE_CONTRACT.equals(contract.getStatus())
				|| Contract.STATUS_CONTRACT_FROZEN.equals(contract.getStatus()));
	}

	private Contract getContract(String contractNumber) throws ContractNotExistException, SystemException {
		Contract contract = null;
		contract = ContractServiceDelegate.getInstance().getContractDetails(Integer.valueOf(contractNumber).intValue(),
				6);
		if (contract != null && contract.getCompanyName() == null) {
			contract = null;
		}
		return contract;
	}
	

}
