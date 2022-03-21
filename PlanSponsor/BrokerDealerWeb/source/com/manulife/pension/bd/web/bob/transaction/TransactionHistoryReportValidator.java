package com.manulife.pension.bd.web.bob.transaction;

import static com.manulife.pension.bd.web.BDErrorCodes.INVALID_DATE_RANGE;
import static com.manulife.pension.platform.web.CommonConstants.TRANSACTION_TYPES;
import static com.manulife.pension.platform.web.CommonConstants.TXN_HISTORY_FROM_DATES;
import static com.manulife.pension.platform.web.CommonConstants.TXN_HISTORY_TO_DATES;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.util.content.GenericException;
@Component
public class TransactionHistoryReportValidator implements Validator {

	private static Logger logger = Logger.getLogger(TransactionHistoryReportValidator.class);
	 private static final List<LabelValueBean> NO_LOANS_TYPES_DROPDOWN = new ArrayList<LabelValueBean>();
	 private static final String ALL_TYPES = "ALL";
	@Override
	public boolean supports(Class clazz) {
		return TransactionHistoryReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult = (BindingResult)errors;	
		if(!bindingResult.hasErrors()){
		Collection<Exception> error = new ArrayList();
		String[] errorCodes = new String[10];
		TransactionHistoryReportForm actionForm = (TransactionHistoryReportForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
	
		if (logger.isDebugEnabled())
            logger.debug("entry -> doValidate");
	    
	   
        /*
         * if this is called using the default URL i.e. no parameters.Do not validate
         */
        if (request.getParameterNames().hasMoreElements()) {
	
	
			if (actionForm.getFromDate() != null && actionForm.getToDate() != null) {
				if (Long.valueOf(actionForm.getFromDate()).longValue() > Long
						.valueOf(actionForm.getToDate()).longValue()) {
					error.add(new GenericException(INVALID_DATE_RANGE));
					
				}
			}
	
			/*
			 * Resets the information for JSP to display.
			 */
			if (error.size() > 0) {
                /*
                 * Re-populates action form and request with default information.
                 */
				populateReportForm( actionForm, request);
				/*
                 * signal the JSP to display the date drop downs again for the user to change their
                 * selection
                 */
				request.setAttribute("displayDates", "true");

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
					
				request.getSession(false).removeAttribute(BdBaseController.ERROR_KEY);
				request.getSession(false).setAttribute(BdBaseController.ERROR_KEY,bindingResult);
					request.removeAttribute(BdBaseController.ERROR_KEY);
					request.setAttribute(BdBaseController.ERROR_KEY, bindingResult);
			
			}
		}
        
        if (logger.isDebugEnabled())
            logger.debug("exit <- doValidate");
	}
	}
	
	private void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		

		/*
		 * Obtain the contract dates object.
		 */
		List<Date> fromDates = new ArrayList<Date>();
        List<Date> toDates = new ArrayList<Date>();

        /*
         * Using the contract dates, generate the date range (from dates and to dates).
         */
        ContractDateHelper.populateFromToDates(getBobContext(request).getCurrentContract(),
                fromDates, toDates);
        
		/*
		 * Stores the from dates, to dates and the transaction types labels into
		 * the request.
		 */
		request.setAttribute(TXN_HISTORY_FROM_DATES, fromDates);
        request.setAttribute(TXN_HISTORY_TO_DATES, toDates);
        request.setAttribute(TRANSACTION_TYPES,
				NO_LOANS_TYPES_DROPDOWN);

		TransactionHistoryReportForm theForm = (TransactionHistoryReportForm) reportForm;

		if (theForm.getFromDate() == null
				|| theForm.getFromDate().length() == 0) {
			theForm.setFromDate(String.valueOf((fromDates.iterator()
					.next()).getTime()));
		}

		if (theForm.getToDate() == null || theForm.getToDate().length() == 0) {
			theForm.setToDate(String.valueOf(((Date) toDates.iterator().next())
					.getTime()));
		}

		if (theForm.getTransactionType() == null
				|| theForm.getTransactionType().length() == 0) {
			// defaults to all
			theForm.setTransactionType(ALL_TYPES);
		}
		
	}
	protected static BobContext getBobContext(HttpServletRequest request) {
		return BDController.getBobContext(request);
	}
}
