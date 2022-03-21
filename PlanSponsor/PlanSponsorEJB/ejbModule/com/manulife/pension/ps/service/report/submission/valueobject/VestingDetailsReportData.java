package com.manulife.pension.ps.service.report.submission.valueobject;

import java.util.Collection;

import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingReportData;
import com.manulife.pension.ps.service.report.submission.reporthandler.VestingDetailsReportHandler;
import com.manulife.pension.ps.service.submission.valueobject.VestingDetailItem;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

public class VestingDetailsReportData extends CensusVestingReportData implements SubmissionErrorCollection {

	public static String REPORT_ID = VestingDetailsReportHandler.class.getName();
	public static String REPORT_NAME = "vestingDetailsReport";
	public static final String FILTER_SUBMISSION_ID = "submissionId";
    
    public static final String SORT_NAME = "name";
    public static final String SORT_SSN = "ssn"; 
    public static final String SORT_RECORD_NUMBER = "sourceRecordNo";
    public static final String SORT_EMP_ID = "empId";
	
	private Integer submissionId;
	private VestingDetailItem vestingData;
	
	public VestingDetailsReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}
	
	public Integer getSubmissionId() {
		return this.submissionId;
	}
	
	public void setSubmissionId(Integer submissionId) {
		this.submissionId= submissionId;
	}
	
	public VestingDetailItem getVestingData() {
		return this.vestingData;
	}
	
	public void setVestingData(VestingDetailItem vestingData) {
		this.vestingData = vestingData;
	}
	
	public Collection getErrors() {
		return vestingData.getReportDataErrors().getErrors();
	}
	
	public void addErrors(Collection additionalErrors) {
        vestingData.getReportDataErrors().addErrors(additionalErrors);
	}
    
    public void setErrors(Collection additionalErrors) {
        vestingData.getReportDataErrors().setErrors(additionalErrors);
    }
	
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#getNumErrors()
	 */
	public int getNumErrors() {
		return vestingData.getReportDataErrors().getNumErrors();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#getNumSyntaxErrors()
	 */
	public int getNumSyntaxErrors() {
		return vestingData.getReportDataErrors().getNumSyntaxErrors();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#getNumWarnings()
	 */
	public int getNumWarnings() {
		return vestingData.getReportDataErrors().getNumWarnings();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#isDiscardFlag()
	 */
	public boolean isDiscardFlag() {
		return vestingData.getReportDataErrors().isDiscardFlag();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#isSyntaxErrorIndicator()
	 */
	public boolean isSyntaxErrorIndicator() {
		return vestingData.getReportDataErrors().isSyntaxErrorIndicator();
	}
}
