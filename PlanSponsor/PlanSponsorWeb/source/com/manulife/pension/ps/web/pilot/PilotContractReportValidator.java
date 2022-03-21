package com.manulife.pension.ps.web.pilot;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

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
import com.manulife.pension.platform.web.validation.rules.ContractNumberNoMandatoryRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.tools.BusinessConversionForm;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.pilot.report.valueobject.PilotContractReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;
@Component
public class PilotContractReportValidator extends ValidatorUtil implements Validator {

private static Logger logger = Logger.getLogger(PilotContractReportValidator.class);
	
	@Override
	public boolean supports(Class<?> arg0) {
		return PilotContractReportForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors errors) {
	
		ArrayList<GenericException> error = new ArrayList<GenericException>();
		String[] errorCodes = new String[10];
		BindingResult bindingResult = (BindingResult)errors;
			

		PilotContractReportForm sForm = (PilotContractReportForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		// TODO Auto-generated method stub
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}
		//
		
		if(!bindingResult.hasErrors()){
		

		String task = getTask(request);
		if (FILTER_TASK.equals(task) || SORT_TASK.equals(task) || DOWNLOAD_TASK.equals(task)) {
			if (sForm.getContractNumber() != null && sForm.getContractNumber().length() > 0) {
				// General contract number rule SCR 35
				boolean isValidFormat = ContractNumberNoMandatoryRule.getInstance()
						.validate(BusinessConversionForm.FIELD_CONTRACT_NUMBER, error, sForm.getContractNumber());

				if (isValidFormat) {
					Contract contract = null;
					try {
						contract = getContract(sForm.getContractNumber());
						if (!isValidContractStatus(contract)) {
							error.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
						}

					} catch (ContractNotExistException ce) {
						error.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
					} catch (SystemException se) {
						error.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
					}
				}
			}
		}

		if ( !error.isEmpty()) {
			
			for(GenericException errorEx :error){
				errorEx.getMessage();
				errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
				bindingResult.addError(new ObjectError((errors)
						.getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
				
			}
			request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, errors);
			request.setAttribute(PsBaseUtil.ERROR_KEY, errors);
			
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
	
	
	protected String getReportId() {
		return PilotContractReportData.REPORT_ID;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportName()
	 */
	protected String getReportName() {
		return PilotContractReportData.REPORT_NAME;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return PilotContractReportData.SORT_FIELD_TEAM_CODE;
	}


	/**
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
	 */
	

	

}
