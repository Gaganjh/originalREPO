package com.manulife.pension.ps.service.report.census.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.dao.EmployeeEnrollmentSummaryDAO;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeEnrollmentSummaryReportData;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * 
 * @author patuadr
 *
 */
public class EmployeeOptOutSummaryReportHandler implements ReportHandler {  

    /**
     * Uses the DAO to retrieve the report and calculates derived fields based on 
     * business rules
     * 
     * @param criteria
     */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException {
		ReportData data = null;
       /* EmployeeEnrollmentSummaryReportData employeeEnrollmentSummaryReportData = 
            (EmployeeEnrollmentSummaryReportData) EmployeeEnrollmentSummaryDAO
            .getOptOutReportData(criteria);*/
                
        return data;	
	}
}
