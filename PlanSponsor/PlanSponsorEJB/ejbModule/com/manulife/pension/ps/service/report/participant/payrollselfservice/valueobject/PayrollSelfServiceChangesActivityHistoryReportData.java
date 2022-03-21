package com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject;

import com.manulife.pension.ps.service.report.participant.payrollselfservice.reporthandler.PayrollSelfServiceChangesActivityHistoryReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

public class PayrollSelfServiceChangesActivityHistoryReportData extends PayrollSelfServiceChangesReportData {

	private static final long serialVersionUID = 1L;

	public static final String REPORT_ID = PayrollSelfServiceChangesActivityHistoryReportHandler.class.getName();

	public static final String REPORT_NAME = "payrollSelfServiceChangesActivityHistoryReport";

	public PayrollSelfServiceChangesActivityHistoryReportData() {
		super();
	}

	public PayrollSelfServiceChangesActivityHistoryReportData(ReportCriteria criteria, int count) {
		super(criteria, count);
	}

}
