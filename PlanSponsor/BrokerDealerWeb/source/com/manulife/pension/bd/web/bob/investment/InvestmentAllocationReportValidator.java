package com.manulife.pension.bd.web.bob.investment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
import com.manulife.pension.bd.web.util.ValidatorUtil;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;
@Component
public class InvestmentAllocationReportValidator extends ValidatorUtil implements Validator {
	public static final String VIEW_BY_ACTIVITY = "1";
	private static final int DEDUCT_THIRTEEN = -13;
	public static final int INVALID_ASOFDATE_SELECTED = 3001;
	protected static final String FILTER_TASK = "filter";
	 protected static final String SORT_TASK = "sort";
	 protected static final String PRINT_TASK = "print";
	    protected static final String PAGE_TASK = "page";
	    protected static final String PRINT_PDF_TASK = "printPDF";
	    protected static final String DOWNLOAD_TASK = "download";

	    protected static final String DOWNLOAD_ALL_TASK = "downloadAll";
	private static Logger logger = Logger.getLogger(InvestmentAllocationReportValidator.class);
	@Override
	public boolean supports(Class clazz) {
		return InvestmentAllocationPageForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult = (BindingResult)errors;	
		if(!bindingResult.hasErrors()){
		Collection<GenericException> error = new ArrayList();
		  InvestmentAllocationPageForm actionForm = (InvestmentAllocationPageForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		String[] errorCodes = new String[10];
    	
      
        if (request.getParameterNames().hasMoreElements()) {
            if (actionForm.getViewOption() != null && actionForm.getAsOfDateReport() != null
                    && VIEW_BY_ACTIVITY.equals(actionForm.getViewOption())) {
                Contract contract = getBobContext(request).getCurrentContract();
                ArrayList monthEndDates = (ArrayList) contract.getContractDates()
                        .getMonthEndDates();
                if (monthEndDates != null && monthEndDates.size() > 0) {
                    Date mostRecentMonthEnd = (Date) monthEndDates.get(0);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(mostRecentMonthEnd);
                    cal.add(Calendar.MONTH, DEDUCT_THIRTEEN);
                    Date activityViewMonthEndBoundary = cal.getTime();
                    if (Long.valueOf(actionForm.getAsOfDateReport()).longValue() < activityViewMonthEndBoundary
                            .getTime()) {
                        error.add(new GenericException(INVALID_ASOFDATE_SELECTED));
                    }
                }
            }
            if (error.size() > 0) {
                populateReportForm( actionForm, request);
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
					
					request.getSession().removeAttribute(BdBaseController.ERROR_KEY);
					request.removeAttribute(BdBaseController.ERROR_KEY);
					request.getSession().setAttribute(BdBaseController.ERROR_KEY, bindingResult);
					request.setAttribute(BdBaseController.ERROR_KEY, bindingResult);
			
            }
        }
		}
	
}
	protected static BobContext getBobContext(HttpServletRequest request) {
		return BDController.getBobContext(request);
	}
	 protected void populateReportForm(
	            BaseReportForm reportForm, HttpServletRequest request) {

	        if (logger.isDebugEnabled()) {
	            logger.debug("entry -> populateReportForm");
	        }

	        String task = getTask(request);

	        /*
	         * Reset page number properly.
	         */
	        if (task.equals(DEFAULT_TASK) || task.equals(SORT_TASK)
	                || task.equals(FILTER_TASK) || task.equals(PRINT_TASK)
	                || task.equals(DOWNLOAD_TASK)
	                || task.equals(PRINT_PDF_TASK) || task.equals(DOWNLOAD_ALL_TASK)) {
	            reportForm.setPageNumber(1);
	        }

	        /*
	         * Set default sort if we're in default task.
	         */
	        if (task.equals(DEFAULT_TASK) || reportForm.getSortDirection() == null
	                || reportForm.getSortDirection().length() == 0) {
	            reportForm.setSortDirection(getDefaultSortDirection());
	        }

	        /*
	         * Set default sort direction if we're in default task.
	         */
	        if (task.equals(DEFAULT_TASK) || reportForm.getSortField() == null
	                || reportForm.getSortField().length() == 0) {
	            reportForm.setSortField(getDefaultSort());
	        }

	        if (logger.isDebugEnabled()) {
	            logger.debug("exit <- populateReportForm");
	        }
	    }

	private String getDefaultSort() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getDefaultSortDirection() {
		// TODO Auto-generated method stub
		return null;
	}

}
