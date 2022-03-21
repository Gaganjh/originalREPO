/*
 * Created on August 17, 2004
 */
package com.manulife.pension.ps.service.report.submission.valueobject;

import java.io.Serializable;

import com.manulife.pension.ps.service.report.submission.reporthandler.SubmissionHistoryReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * ReportData object for SubmissionHistory page.
 *  
 * @author Adrian Robitu
 */
public class SubmissionHistoryReportData extends ReportData implements Serializable {

	public static final String REPORT_ID = SubmissionHistoryReportHandler.class.getName();
	public static final String REPORT_NAME = "submissionHistoryReport";

	public static final String FILTER_CONTRACT_NO = "contractNumber";
	public static final String FILTER_SUBMITTER_ID = "submitterId";
	public static final String FILTER_TYPE = "type";
	public static final String FILTER_STATUS = "status";
	public static final String FILTER_START_SUBMISSION_DATE = "startSubmissionDate";
	public static final String FILTER_END_SUBMISSION_DATE = "endSubmissionDate";
	public static final String FILTER_START_PAYROLL_DATE = "startPayrollDate";
	public static final String FILTER_END_PAYROLL_DATE = "endPayrollDate";
	public static final String FILTER_TPA_USER_ID = "tpaUserId";
    
	public static final String SORT_SUBMITTER_NAME = "submitterName";
	public static final String SORT_SUBMISSION_ID = "submissionId";
	public static final String SORT_SUBMISSION_DATE = "submissionDate";
	public static final String SORT_TYPE = "type";
	public static final String SORT_USER_STATUS = "userStatus";
	public static final String SORT_PAYROLL_DATE = "payrollDate";
	
	public SubmissionHistoryReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}
}