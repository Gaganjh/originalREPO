package com.manulife.pension.ps.service.report.noticereports.reporthandler;

import java.sql.Date;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.noticereports.dao.BuildYourPackageReportDAO;
import com.manulife.pension.ps.service.report.noticereports.valueobject.BuildYourPackageReportData;
import com.manulife.pension.ps.service.report.noticereports.valueobject.PlanSponsorWebsiteReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Report Handler for Notice Manager Build Your Package page usage.
 * 
 */
public class BuildYourPackageReportHandler implements ReportHandler {

    public static final String REPORT_ID = BuildYourPackageReportHandler.class.getName();

    /**
     * Report handler for Build Your Package report.
     * 
     */
    @Override
    public ReportData getReportData(ReportCriteria reportCriteria) throws SystemException,
            ReportServiceException {

        BuildYourPackageReportData reportData = new BuildYourPackageReportData(reportCriteria, 0);
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
        BuildYourPackageReportDAO.getReportData(reportCriteria, reportData);
        return reportData;
    }

}
