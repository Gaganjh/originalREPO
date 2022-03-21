package com.manulife.pension.ps.service.report.noticereports.reporthandler;

import java.sql.Date;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.noticereports.dao.OrderStatusReportDAO;
import com.manulife.pension.ps.service.report.noticereports.valueobject.OrderStatusReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class OrderStatusReportHandler implements ReportHandler {

	public static final String REPORT_ID = OrderStatusReportHandler.class
			.getName();

	@Override
	public ReportData getReportData(ReportCriteria reportCriteria)
			throws SystemException, ReportServiceException {
		Date fromDate = null, toDate = null;
		OrderStatusReportData reportData = new OrderStatusReportData(
				reportCriteria, 0);

		java.util.Date fromDateUtil = (java.util.Date) reportCriteria
				.getFilterValue(OrderStatusReportData.FILTER_FROM_DATE);
		java.util.Date toDateUtil = (java.util.Date) reportCriteria
				.getFilterValue(OrderStatusReportData.FILTER_TO_DATE);
		if (fromDateUtil != null) {
			fromDate = new Date(fromDateUtil.getTime());
		}
		if (toDateUtil != null) {
			toDate = new Date(toDateUtil.getTime());
		}
		reportData.setFromDate(fromDate);
		reportData.setToDate(toDate);

		OrderStatusReportDAO.getReportData(reportCriteria, reportData);

		return reportData;
	}

}
