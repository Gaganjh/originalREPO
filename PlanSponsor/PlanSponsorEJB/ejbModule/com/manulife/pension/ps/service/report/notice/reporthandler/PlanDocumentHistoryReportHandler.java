package com.manulife.pension.ps.service.report.notice.reporthandler;



import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.notice.dao.PlanNoticeDocumentHistoryDAO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentHistoryReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Class to get the Contract Notice Document History
 */
public class PlanDocumentHistoryReportHandler implements ReportHandler {

	
	@Override
	public ReportData getReportData(ReportCriteria reportCriteria)
			throws SystemException, ReportServiceException {
		PlanDocumentHistoryReportData reportData = new PlanDocumentHistoryReportData(reportCriteria, 0);
			 PlanNoticeDocumentHistoryDAO.getReportData(reportCriteria, reportData);
		return reportData;
	}

}
