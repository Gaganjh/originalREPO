package com.manulife.pension.ps.web.participant;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressesReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.util.content.GenericException;
@Component
public class ParticipantAddressesReportValidator extends ValidatorUtil implements Validator {

private static Logger logger = Logger.getLogger(ParticipantAddressesReportValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		return ParticipantAddressesReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult=(BindingResult)errors;
		if(!bindingResult.hasErrors()){
			Collection error = new ArrayList();
			String[] errorCodes = new String[10];
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		ParticipantAddressesReportForm theForm = (ParticipantAddressesReportForm) target;
		 if(logger.isDebugEnabled()) {
			    logger.debug("entry -> doValidate");
		    }
		    // This code has been changed and added  to 
			//Validate form and request against penetration attack, prior to other validations as part of the CL#137697.

			
			String namePhrase = theForm.getNamePhrase();

			if (getTask(request).equals(FILTER_TASK)) {
				if (namePhrase != null && namePhrase.trim().length() > 0) {
					/*NameRule.getLastNameInstance().validate(
							ParticipantAddressesReportForm.FIELD_LAST_NAME,
	                        error, namePhrase);*/
	                
				}
				if (!theForm.getSsn().isEmpty()) {
					// SSN Number mandatory and standards must be met
					SsnRule.getInstance().validate(
						ParticipantAddressesReportForm.FIELD_SSN,
						error,
						theForm.getSsn()
					);
				}
			}

			if (error.size() > 0) {
				ParticipantAddressesReportController participantAddressesReportAction=new ParticipantAddressesReportController();
				participantAddressesReportAction.populateReportForm( theForm, request);
				ParticipantAddressesReportData reportData = theForm.getReport();
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				for (Object e : error) {
					if (e instanceof GenericException) {
						GenericException errorEx=(GenericException) e;
						errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
						bindingResult.addError(new ObjectError(errors
								                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
						
					}
					 request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
						request.removeAttribute(PsBaseUtil.ERROR_KEY);
						request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
						request.setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
			}
		}

			}

		    if(logger.isDebugEnabled()) {
			    logger.debug("exit <- doValidate");
		    }
		
		
		
		
		
		
	}

	}
   
