package com.manulife.pension.ps.service.report.participant.valueobject;


import com.manulife.pension.ps.service.report.participant.reporthandler.TaskCenterTasksReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

public class TaskCenterTasksReportData  extends TaskCenterCommonReportData {
	
	public static final String REPORT_ID = TaskCenterTasksReportHandler.class.getName();

	public static final String REPORT_NAME = "taskCenterTasksReport";
	
	public TaskCenterTasksReportData(ReportCriteria criteria, int count) {
		super(criteria, count);
	}
	
}
