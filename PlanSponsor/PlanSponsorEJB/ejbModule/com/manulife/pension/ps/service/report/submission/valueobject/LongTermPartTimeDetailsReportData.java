package com.manulife.pension.ps.service.report.submission.valueobject;

import java.util.Collection;

import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingReportData;
import com.manulife.pension.ps.service.report.submission.reporthandler.LongTermPartTimeDetailsReportHandler;
import com.manulife.pension.ps.service.submission.valueobject.LongTermPartTimeDetailItem;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

public class LongTermPartTimeDetailsReportData extends CensusVestingReportData implements SubmissionErrorCollection {

	public static String REPORT_ID = LongTermPartTimeDetailsReportHandler.class.getName();
	public static String REPORT_NAME = "longTermPartTimeDetailsReport";
	public static final String FILTER_SUBMISSION_ID = "submissionId";
    
    public static final String SORT_NAME = "name";
    public static final String SORT_SSN = "ssn"; 
    public static final String SORT_RECORD_NUMBER = "sequenceNo";
	
	private Integer submissionId;
	private LongTermPartTimeDetailItem longTermPartTimeData;
	
	public LongTermPartTimeDetailsReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}
	
	public Integer getSubmissionId() {
		return this.submissionId;
	}
	
	public void setSubmissionId(Integer submissionId) {
		this.submissionId= submissionId;
	}
	
	public LongTermPartTimeDetailItem getLongTermPartTimeData() {
		return this.longTermPartTimeData;
	}
	
	public void setLongTermPartTimeDataData(LongTermPartTimeDetailItem longTermPartTimeData) {
		this.longTermPartTimeData = longTermPartTimeData;
	}
	
	public Collection getErrors() {
		return longTermPartTimeData.getReportDataErrors().getErrors();
	}
	
	public void addErrors(Collection additionalErrors) {
		longTermPartTimeData.getReportDataErrors().addErrors(additionalErrors);
	}
    
    public void setErrors(Collection additionalErrors) {
    	longTermPartTimeData.getReportDataErrors().setErrors(additionalErrors);
    }
	
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#getNumErrors()
	 */
	public int getNumErrors() {
		return longTermPartTimeData.getReportDataErrors().getNumErrors();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#getNumSyntaxErrors()
	 */
	public int getNumSyntaxErrors() {
		return longTermPartTimeData.getReportDataErrors().getNumSyntaxErrors();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#getNumWarnings()
	 */
	public int getNumWarnings() {
		return longTermPartTimeData.getReportDataErrors().getNumWarnings();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#isDiscardFlag()
	 */
	public boolean isDiscardFlag() {
		return longTermPartTimeData.getReportDataErrors().isDiscardFlag();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#isSyntaxErrorIndicator()
	 */
	public boolean isSyntaxErrorIndicator() {
		return longTermPartTimeData.getReportDataErrors().isSyntaxErrorIndicator();
	}
}
