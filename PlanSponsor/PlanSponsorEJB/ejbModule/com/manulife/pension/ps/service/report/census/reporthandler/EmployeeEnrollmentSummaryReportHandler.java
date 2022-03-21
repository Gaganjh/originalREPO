package com.manulife.pension.ps.service.report.census.reporthandler;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.census.dao.EmployeeEnrollmentSummaryDAO;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeEnrollmentSummaryReportData;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 *
 * @author patuadr
 *
 */
public class EmployeeEnrollmentSummaryReportHandler implements ReportHandler {
	private Logger logger = Logger.getLogger(EmployeeEnrollmentSummaryReportHandler.class);
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
        EmployeeEnrollmentSummaryReportData employeeEnrollmentSummaryReportData =
            (EmployeeEnrollmentSummaryReportData) EmployeeEnrollmentSummaryDAO
            .getReportData(criteria);

        //processFields(employeeEnrollmentSummaryReportData);

        return employeeEnrollmentSummaryReportData;
	}

    /**
     * Calculates derived fields based on business rules for each element in the report
     * and replaces CHARACTER values with strings
     *
     * @param employeeEnrollmentSummaryReportData
     * @throws ApplicationException
     * @throws SystemException
     */
//    private void processFields(EmployeeEnrollmentSummaryReportData employeeEnrollmentSummaryReportData)
//    throws SystemException {
//        if(employeeEnrollmentSummaryReportData != null) {
//            if(!employeeEnrollmentSummaryReportData.getDetails().isEmpty()) {
//                Date nextPED = getNextPlanEntryDate(employeeEnrollmentSummaryReportData.getContractNumber());
//                for (EmployeeSummaryDetails element : (List<EmployeeSummaryDetails>)employeeEnrollmentSummaryReportData.getDetails()) {
//                    EligibilityBusinessRulesUtil.processEnrollmentStatusAndApplicablePED(element, nextPED);
//                    EligibilityBusinessRulesUtil.processEnrollmentMethod(element);
//                    EligibilityBusinessRulesUtil.processDeferralPercentage(element);
//                }
//            }
//        }
//    }

    /**
     * Utility method that returns next PED starting from today
     *
     * @return
     * @throws SystemException
     * @throws ApplicationException
     */
    private Date getNextPlanEntryDate(int contractNumber) throws SystemException {

        Date nextDate;
        try {
            nextDate = ContractServiceDelegate.getInstance()
                    .getNextPlanEntryDate(contractNumber, new Date());
        } catch (ApplicationException e) {
            throw new SystemException(e, EmployeeEnrollmentSummaryReportHandler.class.getName(), "getApplicablePlanEntryDate", "");
        }

        if(nextDate != null) {
             return nextDate;
        } else {
            return new Date();
        }
    }

	private void logExecutionTime(String contractNumber, long executionTime)
	{
		// Log the execution time.
		ServiceLogRecord record = new ServiceLogRecord();
		Date date = new Date();
		record.setApplicationId(Constants.PS_APPLICATION_ID);
		record.setData("CONTRACT_NUMBER="+contractNumber);
		record.setMethodName(this.getClass().getName()+":"+"getReportData"); // Logging Point
		record.setUserIdentity(contractNumber);
		record.setServiceName("ParticipantEnrollmentSummaryExecutionTime");
		record.setCode((int)executionTime);
		record.setMilliSeconds(date.getTime());

		// Log the record to MRL
		logger.error(record);
	}
}
