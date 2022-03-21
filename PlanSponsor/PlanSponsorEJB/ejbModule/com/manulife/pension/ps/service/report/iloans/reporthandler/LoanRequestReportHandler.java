package com.manulife.pension.ps.service.report.iloans.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.iloans.dao.LoanRequestDAO;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class LoanRequestReportHandler implements ReportHandler {  

	public ReportData getReportData(ReportCriteria criteria) throws SystemException {
		return LoanRequestDAO.getReportData(criteria);	
	}
}
