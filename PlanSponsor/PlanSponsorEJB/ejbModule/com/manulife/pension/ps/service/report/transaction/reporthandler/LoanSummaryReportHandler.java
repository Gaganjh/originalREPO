package com.manulife.pension.ps.service.report.transaction.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.dao.LoanSummaryDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class LoanSummaryReportHandler implements ReportHandler {  

	public ReportData getReportData(ReportCriteria criteria) throws SystemException, ReportServiceException {
		
		LoanSummaryDAO dao = new LoanSummaryDAO();
		return dao.getReportData(criteria);	
	}
}


