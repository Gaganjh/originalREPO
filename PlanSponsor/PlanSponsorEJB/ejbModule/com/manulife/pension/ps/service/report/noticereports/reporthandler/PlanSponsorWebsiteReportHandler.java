package com.manulife.pension.ps.service.report.noticereports.reporthandler;

import java.sql.Date;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.noticereports.dao.PlanSponsorWebsiteReportDAO;
import com.manulife.pension.ps.service.report.noticereports.valueobject.PlanSponsorWebsiteReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Report Handler for Plan Sponsor Notice Manager page usage.
 * 
 */
public class PlanSponsorWebsiteReportHandler implements ReportHandler {

    public static final String REPORT_ID = PlanSponsorWebsiteReportHandler.class.getName();

    /**
     * Report handler for Plan Sponsor Web site report.
     * 
     */
    @Override
    public ReportData getReportData(ReportCriteria reportCriteria) throws SystemException,
            ReportServiceException {

        PlanSponsorWebsiteReportData reportData = new PlanSponsorWebsiteReportData(reportCriteria,
                0);
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
        PlanSponsorWebsiteReportDAO.getReportData(reportCriteria, reportData);
        return reportData;
    }

}
