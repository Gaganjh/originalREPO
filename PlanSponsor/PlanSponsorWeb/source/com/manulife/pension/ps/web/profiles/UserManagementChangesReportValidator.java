package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
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
import com.manulife.pension.platform.web.validation.rules.ContractNumberNoMandatoryRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.home.SearchContractController;
import com.manulife.pension.ps.web.tools.BusinessConversionForm;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.content.GenericException;
@Component
public class UserManagementChangesReportValidator extends ValidatorUtil implements Validator {

private static Logger logger = Logger.getLogger(UserManagementChangesReportValidator.class);
private static BaseEnvironment baseEnvironment= new BaseEnvironment(); 
	
	@Override
	public boolean supports(Class<?> arg0) {
		return UserManagementChangesReportForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors error) {
	
		ArrayList<GenericException> errors = new ArrayList<GenericException>();
		String[] errorCodes = new String[10];
		BindingResult bindingResult = (BindingResult)error;
		UserManagementChangesReportForm sForm = (UserManagementChangesReportForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}

		// This code has been changed and added to Validate form and request
		// against penetration attack, prior to other validations as part of the
		// CL#137697.

		if(!bindingResult.hasErrors()){
			
			String lastName = sForm.getChangedBy();
			
			String task = getTask(request);
			
			if (DOWNLOAD_TASK.equals(task) || PRINT_TASK.equals(task)) {
				int maxRecordsAllowed;
				try {
					maxRecordsAllowed = Integer.parseInt(
							baseEnvironment.getNamingVariable(Constants.MAX_ALLOWED_RECORDS_CONTROL_REPORTS, null));
				} catch (Exception e) {
					logger.error("Fail to read name space variable"+Constants.MAX_ALLOWED_RECORDS_CONTROL_REPORTS, e);
					maxRecordsAllowed = 25000;
				}

				if (sForm.getTotalRecordsInCSV() > maxRecordsAllowed) {
					errors.add(new GenericException(ErrorCodes.MAXIMUM_ALLOWED_RECORDS_EXCEEDED));
				}

			}
			if (FILTER_TASK.equals(task) || SORT_TASK.equals(task) || DOWNLOAD_TASK.equals(task) || PRINT_TASK.equals(task)) {
				
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
}
