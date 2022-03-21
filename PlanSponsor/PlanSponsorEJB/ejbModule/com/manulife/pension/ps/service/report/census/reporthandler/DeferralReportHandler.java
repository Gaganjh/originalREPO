package com.manulife.pension.ps.service.report.census.reporthandler;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.dao.DeferralSummaryDAO;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralReportData;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * 
 * @author patuadr
 *
 */
public class DeferralReportHandler implements ReportHandler {  
	private Logger logger = Logger.getLogger(DeferralReportHandler.class);
    public final String BLANK = "";
    public final String ENROLLED = "Enrolled";
    public final String NOT_ELIGIBLE = "Not Eligible";
    public final String PENDING_ELIGIBILITY = "Pending Eligibility";
    public final String PENDING_ENROLLMENT = "Pending Enrollment";
    public final String NO_ACCOUNT = "No Account";

    /**
     * Uses the DAO to retrieve the report and calculates derived fields based on 
     * business rules
     * 
     * @param criteria
     */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException {
        DeferralReportData deferralReportData = 
            (DeferralReportData) DeferralSummaryDAO.getReportData(criteria);
        return deferralReportData; 
	}    
}
