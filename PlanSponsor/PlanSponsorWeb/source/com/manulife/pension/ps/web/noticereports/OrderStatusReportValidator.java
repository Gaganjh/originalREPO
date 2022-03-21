package com.manulife.pension.ps.web.noticereports;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
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
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.validation.rules.ContractNumberNoMandatoryRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.home.SearchTPAForm;
import com.manulife.pension.ps.web.tools.BusinessConversionForm;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;
@Component
public class OrderStatusReportValidator implements Validator {

private static Logger logger = Logger.getLogger(OrderStatusReportValidator.class);
private FastDateFormat simpleDateFormat = FastDateFormat.getInstance(
		"MM/dd/yyyy", Locale.US);
	@Override
	public boolean supports(Class<?> clazz) {
		return AlertsReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Collection error = new ArrayList();
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		AlertsReportForm form = (AlertsReportForm) target;
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		BindingResult bindingResult = (BindingResult)errors;
		if(!bindingResult.hasErrors()){
		String[] errorCodes = new String[10];
		Date fromDate = null;
        Date toDate = null;
        try {
            if (StringUtils.isNotBlank(form.getFromDate())) {
                fromDate = simpleDateFormat.parse(form.getFromDate());
                if (!simpleDateFormat.format(fromDate).equals(form.getFromDate())) {
                    throw new ParseException("Invalid Date", 0);
                }
            } else {
                error.add(new GenericException(ErrorCodes.NMC_CONTROL_REPORTS_FROM_DATE_INVALID));
            }
        } catch (ParseException pe) {
        	error.add(new GenericException(ErrorCodes.NMC_CONTROL_REPORTS_FROM_DATE_INVALID));
        }

        // to date validation
        try {
            if (StringUtils.isNotBlank(form.getToDate())) {
                toDate = simpleDateFormat.parse(form.getToDate());
                if (!simpleDateFormat.format(toDate).equals(form.getToDate())) {
                    throw new ParseException("Invalid Date", 0);
                }
            } else {
            	error.add(new GenericException(ErrorCodes.NMC_CONTROL_REPORTS_TO_DATE_INVALID));
            }
        } catch (ParseException pe) {
        	error.add(new GenericException(ErrorCodes.NMC_CONTROL_REPORTS_TO_DATE_INVALID));
        }

        if (fromDate != null) {
            Calendar calFromDate = Calendar.getInstance();
            calFromDate.setTime(fromDate);
            if (toDate != null) {
                if (fromDate.after(toDate)) {
                	error.add(new GenericException(
                            ErrorCodes.NMC_CONTROL_REPORTS_FROM_DATE_AFTER_TO_DATE));
                }
            }
        }
        
		// Validate Contract number if it is specified.
		if (form.getContractNumber() != null
				&& form.getContractNumber().length() > 0) {
			// General contract number rule SCR 35
			boolean isValidFormat = ContractNumberNoMandatoryRule.getInstance()
					.validate(BusinessConversionForm.FIELD_CONTRACT_NUMBER,
							error, form.getContractNumber());
			if (isValidFormat) {
				// check to make sure contract exists.
				Contract c = null;
				try {
					UserProfile profile =getUserProfile(request);
					c = ContractServiceDelegate.getInstance()
							.getContractDetails(
									new Integer(form.getContractNumber()),
									EnvironmentServiceDelegate.getInstance()
									.retrieveContractDiDuration(profile.getRole(), 0,null));
					if (c == null) {
						error.add(new GenericException(
								ErrorCodes.CONTRACT_NUMBER_INVALID));
					}
				} catch (ContractNotExistException e) {
					error.add(new GenericException(
							ErrorCodes.CONTRACT_NUMBER_INVALID));
				} catch (NumberFormatException e) {
					error.add(new GenericException(
							ErrorCodes.CONTRACT_NUMBER_INVALID));
				} catch (SystemException e) {
					error.add(new GenericException(
							ErrorCodes.CONTRACT_NUMBER_INVALID));
				}
			}
		}

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
			//request.removeAttribute(PsBaseUtil.ERROR_KEY);
			request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
			//request.setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
	}
	
		
	}
	}

	private UserProfile getUserProfile(HttpServletRequest request) {
		return PsController.getUserProfile(request);
	}
}
