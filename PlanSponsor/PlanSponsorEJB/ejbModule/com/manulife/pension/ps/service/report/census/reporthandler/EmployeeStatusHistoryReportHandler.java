package com.manulife.pension.ps.service.report.census.reporthandler;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.dao.EmployeeStatusHistoryDAO;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeStatusHistoryReportData;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class EmployeeStatusHistoryReportHandler implements ReportHandler {  
	private Logger logger = Logger.getLogger(EmployeeStatusHistoryReportHandler.class);


    /**
     * Uses the DAO to retrieve the report and calculates derived fields based on 
     * business rules
     * 
     * @param criteria
     */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException {
        EmployeeStatusHistoryReportData employeeStatusReportData = 
            (EmployeeStatusHistoryReportData) EmployeeStatusHistoryDAO.getReportData(criteria);
        return employeeStatusReportData; 
	}    
}
