package com.manulife.pension.ps.web.tpadownload;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.service.report.tpadownload.valueobject.TedHistoryFilesReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;

@Component
public class TedHistoryFileValidator implements Validator{
	
	private static Logger logger = Logger.getLogger(TedHistoryFileValidator.class);
	@Override
	public boolean supports(Class arg0) {
		return TedHistoryFilesForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors errors) {

		boolean redisplayPreviousData = false;
		BindingResult bindingResult = (BindingResult)errors;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();

		if(logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}

		TedHistoryFilesForm theForm = (TedHistoryFilesForm) target;

		if(!bindingResult.hasErrors()){
			if (getTask(request).equalsIgnoreCase("default") || getTask(request).equalsIgnoreCase("print")) {			
				return ;		
			}

			String filterContractNumber = theForm.getContractNumber();

			if(doContractNumberValidator(filterContractNumber)){
				String[] errorCode = new String[]{Integer.toString(ErrorCodes.ESTATEMENTS_CONTRACT_NUMBER_IS_NOT_VALID)};
				bindingResult.addError(new ObjectError("psErrors",errorCode ,new String[]{}, "default"));
			}	

			if ((theForm.getMyAction() != null) && (theForm.getMyAction().equalsIgnoreCase("ERROR_NONE_CHECKED"))) {
				String[] errorCode = new String[]{Integer.toString(ErrorCodes.NO_FILES_SELECTED_TO_DOWNLOAD)};
				bindingResult.addError(new ObjectError("psErrors",errorCode ,new String[]{}, "default"));
				redisplayPreviousData = true;
			}

			if (bindingResult.hasErrors()) {
				populateReportForm( theForm, request);
				request.getSession(false).setAttribute(PsBaseUtil.ERROR_KEY, errors);
				if (!redisplayPreviousData) {
					TedHistoryFilesReportData reportData = new TedHistoryFilesReportData(null, -1);
					request.setAttribute(Constants.REPORT_BEAN, reportData);
				} else {
					request.setAttribute(Constants.REPORT_BEAN, theForm.getReport());
				}

				request.removeAttribute(PsBaseUtil.ERROR_KEY);
				request.setAttribute(PsBaseUtil.ERROR_KEY, errors);
			}
		}
		if(logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}

}
	protected void populateReportForm(TedHistoryFilesForm reportForm, HttpServletRequest request)
    {
        if(logger.isDebugEnabled())
        {
            logger.debug("entry -> populateReportForm");
        }
        String task = getTask(request);
        if(task.equals("default") || task.equals("sort") || task.equals("filter") || task.equals("print") || task.equals("download") || task.equals("printPDF") || task.equals("downloadAll"))
        {
            reportForm.setPageNumber(1);
        }
        if(task.equals("default") || reportForm.getSortDirection() == null || reportForm.getSortDirection().length() == 0)
        {
            reportForm.setSortDirection(getDefaultSortDirection());
        }
        if(task.equals("default") || reportForm.getSortField() == null || reportForm.getSortField().length() == 0)
        {
            reportForm.setSortField(getDefaultSort());
        }
        if(logger.isDebugEnabled())
        {
            logger.debug("exit <- populateReportForm");
        }
    }

    protected String getTask(HttpServletRequest request)
    {
        String task = request.getParameter("task");
        if(task == null)
        {
            task = "default";
        }
        return task;
    }
    protected String getDefaultSort() {
		return TedHistoryFilesReportData.DEFAULT_SORT;
	}

	protected String getDefaultSortDirection() {
		return TedHistoryFilesReportData.DEFAULT_SORT_ORDER;
	}
	/**
     * Utility method to validate the contract  number
     */
    public boolean doContractNumberValidator(String filterContractNumber ){
        boolean isValidContract = false;
        if ( filterContractNumber != null && filterContractNumber.trim().length() > 0 ) {
            int contractLength = filterContractNumber.trim().length();
            if (contractLength < CommonConstants.CONTRACT_NUMBER_MIN_LENGTH || contractLength > CommonConstants.CONTRACT_NUMBER_MAX_LENGTH) {
                isValidContract =  true;
            } else {
                try {
                    int cn = Integer.parseInt(filterContractNumber);
                    if (cn < CommonConstants.CONTRACT_NUMBER_MIN_VALUE || cn > CommonConstants.CONTRACT_NUMBER_MAX_VALUE) {  // check for negative 4 digit numbers, and leading zeros scenario eg) 00100
                        isValidContract =  true;
                    }
                } catch (Exception e) {
                    isValidContract =  true;
                }
            }
        }
        return isValidContract;
    }

}
