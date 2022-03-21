package com.manulife.pension.ps.service.report.participant.reporthandler;

import java.util.Date;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.participant.dao.ParticipantForfeituresDAO;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantForfeituresReportData;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 * ParticipantForfeituresReportHandler class to retrieve the data for 
 * Accounts Forfeitures page. 
 * 
 * @author Vinothkumar Balasubramaniyam
 */

public class ParticipantForfeituresReportHandler implements ReportHandler {
	
	private Logger logger = Logger.getLogger(ParticipantForfeituresReportHandler.class);

	/**
	 * Method to get the Accounts Forfeitures Page contents
	 * 
	 * @param criteria 
	 * 		as ReportCriteria
	 * @return reportData 
	 * 		as ParticipantForfeituresReportData
	 * @throws SystemException
	 */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}
		
		long startMillis = System.currentTimeMillis();		
		ParticipantForfeituresReportData participantForfeituresReportData = null;
		String contractNumber = null;
		
		//Validate the ReportCriteria and throw appropriate exception when its null
		if ( criteria != null ) {
			contractNumber = (String) criteria.getFilterValue(
					ParticipantForfeituresReportData.FILTER_CONTRACT_NUMBER);
	
			if ( contractNumber == null ) {
				String errorText = "ParticipantForfeituresReportHandler.getReportData() - "+ 
					"Missing Contract Number : <<"+contractNumber+">>"; 
				logger.error(errorText);
				throw new EJBException(errorText);
			}
		} else {
			throw ExceptionHandlerUtility
			.wrap(new SystemException(
					"ParticipantForfeituresReportHandler.getReportData() - " +
					"ReportCriteria object is null and cannot be used for processing..."));
		}
		
		try {
			//Invoke the ParticipantForfeituresDAO to get the results
			participantForfeituresReportData = (ParticipantForfeituresReportData)
					ParticipantForfeituresDAO.getReportData(criteria);
		}catch(SystemException systemException){
			throw ExceptionHandlerUtility.wrap(systemException);
		}
		
		if (participantForfeituresReportData == null) 
			return null;
		
		long endMillis = System.currentTimeMillis();
		logExecutionTime(contractNumber, endMillis - startMillis);
		
		if(logger.isDebugEnabled()) {
			logger.debug("exiting <- getReportData");
		}
		
		return participantForfeituresReportData;	
	}

	/**
	 * Method to log the execution time details
	 * 
	 * @param contractNumber
	 * @param executionTime
	 */
	private void logExecutionTime(String contractNumber, long executionTime){
		// Log the execution time.
		ServiceLogRecord record = new ServiceLogRecord();
		Date date = new Date();
		record.setApplicationId(Constants.PS_APPLICATION_ID);
		record.setData("CONTRACT_NUMBER="+contractNumber);
		record.setMethodName(this.getClass().getName()+":"+"getReportData"); // Logging Point
		record.setUserIdentity(contractNumber);
		record.setServiceName("ParticipantForfeituresExecutionTime");
		record.setCode((int)executionTime);
		record.setMilliSeconds(date.getTime());

		// Log the record to MRL
		logger.error(record);
	}
}
