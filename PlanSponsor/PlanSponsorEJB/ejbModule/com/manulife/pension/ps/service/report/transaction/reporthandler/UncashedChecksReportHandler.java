package com.manulife.pension.ps.service.report.transaction.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.dao.UncashedChecksReportDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class UncashedChecksReportHandler implements ReportHandler {
	
	public static final String REPORT_ID = UncashedChecksReportHandler.class
			.getName();

	public ReportData getReportData(ReportCriteria criteria) throws SystemException, ReportServiceException {
		
		UncashedChecksReportDAO dao = new UncashedChecksReportDAO();
		return dao.getReportData(criteria);	
	}
}
