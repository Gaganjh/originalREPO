package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

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
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.validation.rules.ContractNumberNoMandatoryRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.home.SearchContractController;
import com.manulife.pension.ps.web.tools.BusinessConversionForm;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.pilot.report.valueobject.PilotContractReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.util.content.GenericException;
@Component
public class AddressChangesReportValidator extends SecurityReportController implements Validator {

private static Logger logger = Logger.getLogger(AddressChangesReportValidator.class);
	
	@Override
	public boolean supports(Class<?> arg0) {
		return AddressChangesReportForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors error) {
	
		ArrayList<GenericException> errors = new ArrayList<GenericException>();
		String[] errorCodes = null; 
		BindingResult bindingResult = (BindingResult)error;
		AddressChangesReportForm sForm = (AddressChangesReportForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}

		// This code has been changed and added to Validate form and request
		// against penetration attack, prior to other validations as part of the
		// CL#137697.

		
		if(!bindingResult.hasErrors()){
		String lastName = sForm.getChangedBy();

		String task = getTask(request);
		if (FILTER_TASK.equals(task) || SORT_TASK.equals(task) || DOWNLOAD_TASK.equals(task)) {

			if (!StringUtils.isEmpty(lastName) && !Pattern.matches("[a-zA-Z0-9' ]*", lastName)) {
				errors.add(new GenericException(7085));
			}
			if (sForm.getContractNumber() != null && sForm.getContractNumber().length() > 0) {
				// General contract number rule SCR 35
				boolean isValidFormat = ContractNumberNoMandatoryRule.getInstance()
						.validate(BusinessConversionForm.FIELD_CONTRACT_NUMBER, errors, sForm.getContractNumber());

				if (isValidFormat) {
					UserProfile profile = getUserProfile(request);
					UserRole role = profile.getRole();
					int diDuration = SearchContractController.DI_DURATION_24_MONTH;
					try {
						diDuration = EnvironmentServiceDelegate.getInstance().retrieveContractDiDuration(role, 0, null);
					} catch (SystemException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// check to make sure contract exists.
					Contract c = null;
					try {
						c = ContractServiceDelegate.getInstance()
								.getContractDetails(new Integer(sForm.getContractNumber()), diDuration);
						if (c == null) {
							errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
						}
					} catch (ContractNotExistException e) {
						errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
					} catch (NumberFormatException e) {
						errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
					} catch (SystemException e) {
						errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
					}
				}
			}
			// check date of change
			validateFromToDates(sForm.getFromDate(), sForm.getToDate(), errors);
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

	/**
	 * Validates date range
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param errors
	 */
	private void validateFromToDates(String fromDateStr, String toDateStr, Collection<GenericException> errors) {

		if (StringUtils.isEmpty(fromDateStr) && StringUtils.isEmpty(toDateStr)) {
			errors.add(new GenericException(2264));
		} else {
			Date fromDateDt = null;
			if (StringUtils.isEmpty(fromDateStr)) {
				errors.add(new GenericException(2265));
			} else {
				try {
					fromDateDt = DATE_FORMATTER.parse(fromDateStr);
				} catch (Exception e) {
				}
			}

			Date toDateDt = null;
			if (!StringUtils.isEmpty(toDateStr)) {
				try {
					toDateDt = DATE_FORMATTER.parse(toDateStr);
				} catch (Exception e) {
				}
			}

			if ((!StringUtils.isEmpty(fromDateStr) && fromDateDt == null)
					|| (!StringUtils.isEmpty(toDateStr) && toDateDt == null)) {
				errors.add(new GenericException(2268));
			}

			Date today = new Date(System.currentTimeMillis());
			if (fromDateDt != null && fromDateDt.after(today)) {
				errors.add(new GenericException(2268));
			} else if (toDateDt != null && toDateDt.after(today)) {
				errors.add(new GenericException(2268));
			}

		}
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

	@Override
	protected String getDefaultSortDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {
		// TODO Auto-generated method stub
		
	}

}
