package com.manulife.pension.bd.web.bob.transaction;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.util.content.GenericException;
@Component
public class CashAccountReportValidator implements Validator {
	private static Logger logger = Logger.getLogger(CashAccountReportValidator.class);
	@Override
	public boolean supports(Class clazz) {
		return CashAccountReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult = (BindingResult)errors;
		if(!bindingResult.hasErrors()) {
			ArrayList<GenericException> error= new ArrayList<GenericException>();
			CashAccountReportForm actionForm = (CashAccountReportForm) target;
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request = attr.getRequest();


			// if this is called using the default URL i.e. no parameters
			// do not validate
			if (request.getParameterNames().hasMoreElements()) {



				if (actionForm.getFromDate() != null && actionForm.getToDate() != null) {
					if (Long.valueOf(actionForm.getFromDate()).longValue() > Long
							.valueOf(actionForm.getToDate()).longValue()) {
						error.add(new GenericException(BDErrorCodes.INVALID_DATE_RANGE));
					}
				}

			}
				if ( !error.isEmpty()) {
					String[] errorCodes = new String[10];
					for(GenericException errorEx :error){
						errorEx.getMessage();
						errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
						bindingResult.addError(new ObjectError(errors
								                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
						
					}
					request.getSession().removeAttribute(BdBaseController.ERROR_KEY);
					request.removeAttribute(BdBaseController.ERROR_KEY);
					request.getSession().setAttribute(BdBaseController.ERROR_KEY, errors);
		 			request.setAttribute(BdBaseController.ERROR_KEY, errors);
		 			request.getSession().setAttribute(CommonConstants.ERROR_RDRCT,CommonConstants.INPUT);
			}
		}
	}
	 private static BobContext getBobContext(HttpServletRequest request) {
			return BDController.getBobContext(request);
		}
}
