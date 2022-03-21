/**
 * 
 */
package com.manulife.pension.ps.service.report.participant.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.dao.ParticipantStatementsDAO;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantStatementsReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author arugupu
 *
 */
public class ParticipantStatementsReportHandler implements ReportHandler {  

	public ReportData getReportData(ReportCriteria criteria) throws SystemException,  ReportServiceException {
		
		ParticipantStatementsReportData reportData = 
				new ParticipantStatementsReportData(criteria, 0) ;
		
		ParticipantStatementsDAO.getReportData(criteria,reportData);
		
		return reportData;	
	}
}
