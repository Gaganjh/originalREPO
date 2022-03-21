package com.manulife.pension.ps.service.report.noticereports.reporthandler;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.noticereports.dao.AlertsReportDAO;
import com.manulife.pension.ps.service.report.noticereports.valueobject.AlertsReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class AlertsReportHandler implements ReportHandler {

    public static final String REPORT_ID = AlertsReportHandler.class.getName();

    @Override
    public ReportData getReportData(ReportCriteria reportCriteria) throws SystemException,
            ReportServiceException {

        AlertsReportData reportData = new AlertsReportData(reportCriteria, 0);

        Integer contractNumber = (Integer) reportCriteria
                .getFilterValue(AlertsReportData.FILTER_CONTRACT_NUMBER);
        java.util.Date fromDateUtil = (java.util.Date) reportCriteria
                .getFilterValue(AlertsReportData.FILTER_FROM_DATE);
        java.util.Date toDateUtil = (java.util.Date) reportCriteria
                .getFilterValue(AlertsReportData.FILTER_TO_DATE);

        DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
        Date fromDate = null, toDate = null;

        if (fromDateUtil != null) {
            fromDate = new Date(fromDateUtil.getTime());
        }

        if (toDateUtil != null) {
            toDate = new Date(toDateUtil.getTime());
        }

        reportData.setFromDate(fromDate);
        reportData.setToDate(toDate);

        AlertsReportDAO.getReportData(reportCriteria, reportData);

        return reportData;
    }

}
