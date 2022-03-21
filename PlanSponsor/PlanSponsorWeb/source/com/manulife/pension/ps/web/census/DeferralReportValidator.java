package com.manulife.pension.ps.web.census;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralReportData;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
@Component
public class DeferralReportValidator  extends ValidatorUtil implements Validator {

	
private static Logger logger = Logger.getLogger(DeferralReportValidator.class);
protected static final String FILTER_TASK = "filter";
	@Override
	public boolean supports(Class<?> clazz) {
		return DeferralReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult = (BindingResult) errors;
		if(!bindingResult.hasErrors()){
			Collection error = new ArrayList();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		DeferralReportForm theForm = (DeferralReportForm) target;
		if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doValidate");
	    }
		
	   try {
		

 		if (getTask(request).equals(FILTER_TASK)) {
			if (theForm.getNamePhrase() != null && theForm.getNamePhrase().trim().length() > 0) {
				/*NameRule.getLastNameInstance().validate(
						DeferralReportForm.FIELD_LAST_NAME, errors, 
                        theForm.getNamePhrase());
                */
			}
            

			if (theForm.getSsn() != null && !theForm.getSsn().isEmpty()) {
				// SSN Number mandatory and standards must be met
				SsnRule.getInstance().validate(
					DeferralReportForm.FIELD_SSN,
					error,
					theForm.getSsn()
				);
			}
		}

 		if (error.size() > 0) {
			
			SessionHelper.setErrorsInSession(request, error);
            DeferralReportData reportData = new DeferralReportData(null, 0);
			//request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, error);
 			request.setAttribute(PsBaseUtil.ERROR_KEY, error);
		}
 	    if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doValidate");
	    }
	   } catch (Exception e) {
	        e.printStackTrace();
		}
		
		
		}
		
	}
	
}
