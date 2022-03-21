package com.manulife.pension.ps.service.report.noticereports.reporthandler;

import java.sql.Date;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.noticereports.dao.UploadAndShareReportDAO;
import com.manulife.pension.ps.service.report.noticereports.valueobject.PlanSponsorWebsiteReportData;
import com.manulife.pension.ps.service.report.noticereports.valueobject.UploadAndShareReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Report Handler for Notice Manager Upload and Share page usage.
 * 
 */
public class UploadAndShareReportHandler implements ReportHandler {

    public static final String REPORT_ID = UploadAndShareReportHandler.class.getName();

    /**
     * Gets Report data for Upload and Share control report.
     * 
     */
    @Override
    public ReportData getReportData(ReportCriteria reportCriteria) throws SystemException,
            ReportServiceException {

        UploadAndShareReportData reportData = new UploadAndShareReportData(reportCriteria, 0);
        java.util.Date fromDateUtil = (java.util.Date) reportCriteria
                .getFilterValue(PlanSponsorWebsiteReportData.FILTER_FROM_DATE);
        java.util.Date toDateUtil = (java.util.Date) reportCriteria
                .getFilterValue(PlanSponsorWebsiteReportData.FILTER_TO_DATE);
        Date fromDate = null, toDate = null;
        if (fromDateUtil != null) {
            fromDate = new Date(fromDateUtil.getTime());
        }
        if (toDateUtil != null) {
            toDate = new Date(toDateUtil.getTime());
        }
        reportData.setFromDate(fromDate);
        reportData.setToDate(toDate);
        UploadAndShareReportDAO.getReportData(reportCriteria, reportData);
        return reportData;
    }

}
