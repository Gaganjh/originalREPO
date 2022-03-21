package com.manulife.pension.ps.service.report.census.reporthandler;

import java.util.Date;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.dao.CensusSummaryDAO;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class CensusSummaryReportHandler implements ReportHandler {

	private static final Logger logger = Logger
			.getLogger(CensusSummaryReportHandler.class);

	public ReportData getReportData(ReportCriteria reportCriteria)
			throws SystemException, ReportServiceException {
        
        // process warnings
		CensusSummaryReportData censusSummaryReportData = (CensusSummaryReportData) CensusSummaryDAO
				.getReportData(reportCriteria, true);
        
		return censusSummaryReportData;
	}
}
