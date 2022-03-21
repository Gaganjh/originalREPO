package com.manulife.pension.ps.service.report.census.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.dao.EligibilityReportsDAO;
import com.manulife.pension.ps.service.report.census.dao.EligibilitySnapShotDAO;
import com.manulife.pension.ps.service.report.census.valueobject.EligibilityReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class EligibilityReportHandler implements ReportHandler {

	public ReportData getReportData(ReportCriteria reportCriteria)
			throws SystemException, ReportServiceException {
		
		final String eligibilityIssuesReport = "eligibilityIssuesReport";
		
		ReportData reportData = null;
		String reportType = (String)reportCriteria.getFilterValue(EligibilityReportData.FILTER_REPORT_TYPE);
		
		if("historicalReport".equalsIgnoreCase(reportType)){
			 reportData = EligibilitySnapShotDAO.getHistEligibilityReportData(reportCriteria);
		}else if("compPeriodChangeReport".equalsIgnoreCase(reportType)){
			 reportData = EligibilityReportsDAO.getCompChangeEligibilityReportData(reportCriteria);
		}else if("eligibilityChangeReport".equalsIgnoreCase(reportType)){
			 reportData = EligibilityReportsDAO.getEligibilityChangeReportData(reportCriteria);
		} else if (eligibilityIssuesReport.equalsIgnoreCase(reportType)) {
			reportData = EligibilityReportsDAO
					.getEligibilityIssuesReportVO(reportCriteria);
		}
		
		return reportData;
	}

}
