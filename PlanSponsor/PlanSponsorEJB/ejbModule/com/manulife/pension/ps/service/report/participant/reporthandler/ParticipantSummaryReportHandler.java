 package com.manulife.pension.ps.service.report.participant.reporthandler;

import java.util.Date;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.service.environment.EnvironmentServiceHelper;
import com.manulife.pension.service.environment.valueobject.ContractDatesVO;
import com.manulife.pension.ps.service.report.participant.dao.ParticipantSummaryDAO;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryReportData;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.log.ServiceLogRecord;


public class ParticipantSummaryReportHandler implements ReportHandler {  
	private Logger logger = Logger.getLogger(ParticipantSummaryReportHandler.class);

	public ReportData getReportData(ReportCriteria criteria) throws SystemException {
		
		long startMillis = System.currentTimeMillis();
		
		ParticipantSummaryReportData participantSummaryReportData = (ParticipantSummaryReportData)ParticipantSummaryDAO.getReportData(criteria);
		
		if (participantSummaryReportData == null) return null;
		
		ContractDatesVO contractDates = EnvironmentServiceHelper.getInstance().getContractDates(participantSummaryReportData.getContractNumber());

		participantSummaryReportData.setMonthEndDates(contractDates);

		long endMillis = System.currentTimeMillis();
		String contractNumber = (String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_1);
//		logExecutionTime(contractNumber, endMillis - startMillis);			
		
		return participantSummaryReportData;	
	}
	
	/*
	 * CL #133584- These loggers are flooding MRL and ServerLog, found not much 
	 * important and hence commenting. Uncomment if something important is missed.
	private void logExecutionTime(String contractNumber, long executionTime)
	{	
		// Log the execution time.
		ServiceLogRecord record = new ServiceLogRecord();
		Date date = new Date();
		record.setApplicationId(Constants.PS_APPLICATION_ID);
		record.setData("CONTRACT_NUMBER="+contractNumber);
		record.setMethodName(this.getClass().getName()+":"+"getReportData"); // Logging Point
		record.setUserIdentity(contractNumber);
		record.setServiceName("ParticipantSummaryExecutionTime");
		record.setCode((int)executionTime);		
		record.setMilliSeconds(date.getTime());

		// Log the record to MRL
		logger.error(record);
	}
	*/
}
