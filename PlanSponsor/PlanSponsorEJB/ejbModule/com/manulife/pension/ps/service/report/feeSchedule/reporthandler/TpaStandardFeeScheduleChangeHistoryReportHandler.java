package com.manulife.pension.ps.service.report.feeSchedule.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.feeSchedule.dao.FeeScheduleChangeHistoryDAO;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TpaStandardFeeScheduleChangeHistoryReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * 	
 * @author Venkata Rajesh A.
 *
 */
public class TpaStandardFeeScheduleChangeHistoryReportHandler implements ReportHandler{

	/**
	 * This method is for retrieve the FeeSheduleData List. 
	 */
	public ReportData getReportData(ReportCriteria reportCriteria)  
			throws SystemException, ReportServiceException {

		TpaStandardFeeScheduleChangeHistoryReportData reportData = new TpaStandardFeeScheduleChangeHistoryReportData(
				reportCriteria, 0);

		return FeeScheduleChangeHistoryDAO.getReportData(reportCriteria,
				reportData);

	}
}
