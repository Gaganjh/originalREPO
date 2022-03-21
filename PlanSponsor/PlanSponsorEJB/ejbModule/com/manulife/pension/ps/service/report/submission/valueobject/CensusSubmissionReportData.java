package com.manulife.pension.ps.service.report.submission.valueobject;

import java.io.Serializable;
import java.util.Collection;

import com.manulife.pension.ps.service.report.submission.reporthandler.CensusSubmissionReportHandler;
import com.manulife.pension.ps.service.submission.valueobject.ReportDataErrors;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * @author parkand
 *
 * Report data for address submissions
 */
public class CensusSubmissionReportData extends CensusReportData implements
		Serializable, SubmissionErrorCollection {

	public static final String REPORT_ID = CensusSubmissionReportHandler.class.getName();
	public static final String REPORT_NAME = "censusSubmissionReport";

	public static final String FILTER_SUBMISSION_ID = "submissionId"; // from STP
	public static final String FILTER_SUBMISSION_NO = "submissionNumber"; // from submissionJournal
	
	public static final String SORT_RECORD_NUMBER = "sourceRecordNo";
    public static final String SORT_STATUS = "status";
    public static final String SORT_EMP_ID = "employeeNumber";
    public static final String SORT_DIVISION = "division";
	
	private ReportDataErrors errors;
	
	/**
	 * @param criteria
	 * @param totalCount
	 */
	public CensusSubmissionReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
		sortCode = "Employer Designated ID"; // default value
	}

	/**
	 * @return Returns the errors.
	 */
	public Collection getErrors() {
		return errors.getErrors();
	}
	/**
	 * @param errors The errors to set.
	 */
	public void setErrors(ReportDataErrors errors) {
		this.errors = errors;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#getNumSyntacticErrors()
	 */
	public int getNumErrors() {
		return errors.getNumErrors();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#getNumSyntacticErrors()
	 */
	public int getNumSyntaxErrors() {
		return errors.getNumSyntaxErrors();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#getNumWarnings()
	 */
	public int getNumWarnings() {
		return errors.getNumWarnings();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#isDiscardFlag()
	 */
	public boolean isDiscardFlag() {
		return errors.isDiscardFlag();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#isSyntaxErrorIndicator()
	 */
	public boolean isSyntaxErrorIndicator() {
		return errors.isSyntaxErrorIndicator();
	}
}
