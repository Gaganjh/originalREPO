package com.manulife.pension.ps.service.report.tpadownload.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.tpadownload.dao.EStatementsDAO;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Report handler class for EStatements report  
 * 
 * @author Ilker Celikyilmaz
 */
public class EStatementsReportHandler implements ReportHandler {
	
	/**
 	 * @see ReportHandler#getReportData(ReportCriteria)
 	 * 
 	 */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException 
	{
		return EStatementsDAO.getReportData(criteria);
	}

}

