package com.manulife.pension.ps.service.report.participant.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.dao.TaskCenterTasksDAO;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class TaskCenterTasksReportHandler implements ReportHandler {
	
	public ReportData getReportData(ReportCriteria criteria) throws SystemException {
		return TaskCenterTasksDAO.getReportData(criteria);
	}

}
