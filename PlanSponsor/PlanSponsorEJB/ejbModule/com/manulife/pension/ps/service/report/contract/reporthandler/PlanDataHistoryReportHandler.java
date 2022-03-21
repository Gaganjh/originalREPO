package com.manulife.pension.ps.service.report.contract.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.contract.dao.PlanDataHistoryDAO;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class PlanDataHistoryReportHandler implements ReportHandler {
    public ReportData getReportData(ReportCriteria criteria) throws SystemException {
        return PlanDataHistoryDAO.findPlanDataHistory(criteria);    
    }
}
