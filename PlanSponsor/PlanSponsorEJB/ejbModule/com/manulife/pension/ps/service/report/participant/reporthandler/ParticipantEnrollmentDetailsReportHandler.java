package com.manulife.pension.ps.service.report.participant.reporthandler;

import java.util.Date;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.participant.dao.ParticipantEnrollmentSummaryDAO;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantEnrollmentDetailsReportData;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.log.ServiceLogRecord;


public class ParticipantEnrollmentDetailsReportHandler implements ReportHandler {  
	private Logger logger = Logger.getLogger(ParticipantEnrollmentDetailsReportHandler.class);

	public ReportData getReportData(ReportCriteria criteria) throws SystemException {
		
		long startMillis = System.currentTimeMillis();
		
		ParticipantEnrollmentDetailsReportData participantEnrollmentDetailsReportData = (ParticipantEnrollmentDetailsReportData)ParticipantEnrollmentSummaryDAO.getParticipantEnrollmentSummary(criteria);
		
		if (participantEnrollmentDetailsReportData == null) return null;
		
		long endMillis = System.currentTimeMillis();
		String contractNumber = (String)criteria.getFilterValue(ParticipantEnrollmentDetailsReportData.FILTER_FIELD_1);
		logExecutionTime(contractNumber, endMillis - startMillis);			
		
		return participantEnrollmentDetailsReportData;	
	}

	private void logExecutionTime(String contractNumber, long executionTime)
	{	
		// Log the execution time.
		ServiceLogRecord record = new ServiceLogRecord();
		Date date = new Date();
		record.setApplicationId(Constants.PS_APPLICATION_ID);
		record.setData("CONTRACT_NUMBER="+contractNumber);
		record.setMethodName(this.getClass().getName()+":"+"getDetailsReportData"); // Logging Point
		record.setUserIdentity(contractNumber);
		record.setServiceName("ParticipantEnrollmentDetailsExecutionTime");
		record.setCode((int)executionTime);		
		record.setMilliSeconds(date.getTime());

		// Log the record to MRL
		logger.error(record);
	}	
}
