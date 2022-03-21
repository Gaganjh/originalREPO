package com.manulife.pension.ps.service.report.notice.reporthandler;



import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.notice.dao.PlanNoticeDocumentDAO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Class to get the contract notice document details 
 */
public class PlanDocumentReportHandler implements ReportHandler {

	@Override
	public ReportData getReportData(ReportCriteria reportCriteria)
			throws SystemException, ReportServiceException {
			PlanDocumentReportData reportData = new PlanDocumentReportData(reportCriteria, 0);
			 PlanNoticeDocumentDAO.getReportData(reportCriteria, reportData);
		return reportData;
	}

}
