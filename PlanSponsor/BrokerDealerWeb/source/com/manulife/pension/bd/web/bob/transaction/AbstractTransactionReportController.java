package com.manulife.pension.bd.web.bob.transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.report.BOBReportController;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.environment.valueobject.ContractDatesVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.exception.ResourceLimitExceededException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;

/**
 * This is the base class for the TransactionHistoryReportAction and CashAccountReportAction
 * classes.
 * 
 * @author harlomte
 * 
 */
public abstract class AbstractTransactionReportController extends BOBReportController {


	/**
	 * Constructor.
	 */
	public AbstractTransactionReportController(Class className) {
		super(className);
	}

	/**
	 * Returns a contract dates value object from the session (UserProfile).
	 * 
	 * @param request
	 *            The HttpServletRequest object.
	 * @return A ContractDatesVO object.
	 */
	public ContractDatesVO getContractDatesVO(HttpServletRequest request) {

		BobContext bobContext = BDController.getBobContext(request);
		
		Contract currentContract = bobContext.getCurrentContract();

		return currentContract.getContractDates();
	}

	/**
	 * Obtains the actual report data.
	 * 
	 * @see com.manulife.pension.bd.web.report.ReportController#getReportData(java.lang.String,
	 *      com.manulife.pension.service.report.valueobject.ReportCriteria,
	 *      javax.servlet.http.ServletRequest)
	 */
	protected ReportData getReportData(String reportId,
			ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException
	{
		ReportData data = null;
		try {
			data = super.getReportData(reportId, reportCriteria, null);
		} catch (ResourceLimitExceededException e) {
			// see if the selection is wider than one month
			// then re-throw the exception
			// otherwise just throw a system exception
			Date fromDate = (Date)reportCriteria.getFilterValue(
					CashAccountReportData.FILTER_FROM_DATE);
			Date toDate = (Date)reportCriteria.getFilterValue(
					CashAccountReportData.FILTER_TO_DATE);
			if (fromDate != null && toDate != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(fromDate);
				cal.add(Calendar.DATE, 32);
				if (cal.getTime().before(toDate)) {
					// this is a valid case
					throw e;
				}
			}
			throw new SystemException(e, "AbstractTransactionReportAction", 
				"getReportData",
				"Got a resource exceeded exception while the user's selection is less than or equal to one month");			
		}
		
		return data;
	}


	/**
	 * @see BaseReportController#doCommon(ActionMapping, BaseReportForm, HttpServletRequest, HttpServletResponse)
	 */
	protected String doCommon(
		BaseReportForm reportForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws SystemException
	{
		String forward = null;
		
		try {
			forward = super.doCommon( reportForm, request, response);
		} catch (SystemException e) {
			// Log the system exception.
			LogUtility.logSystemException(BDConstants.BD_APPLICATION_ID,e);

			// Show user friendly message.
			List errors = new ArrayList();
			errors.add(new GenericException(BDErrorCodes.TECHNICAL_DIFFICULTIES));
			setErrorsInRequest(request, errors);
			forward="input";
}

		return forward;
	}

}
