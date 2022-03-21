package com.manulife.pension.ps.service.report.submission.valueobject;

import java.util.Collection;

import com.manulife.pension.ps.service.report.contract.valueobject.ContributionTemplateReportData;
import com.manulife.pension.ps.service.report.submission.reporthandler.ContributionDetailsReportHandler;
import com.manulife.pension.ps.service.submission.valueobject.ContributionDetailItem;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

public class ContributionDetailsReportData extends ContributionTemplateReportData implements SubmissionErrorCollection {

	public static String REPORT_ID = ContributionDetailsReportHandler.class.getName();
	public static String REPORT_NAME = "contributionDetailsReport";
	public static final String FILTER_FIELD_2 = "submissionId";
	
	private Integer submissionId;
	private ContributionDetailItem contributionData;
	
	public ContributionDetailsReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}
	
	public Integer getSubmissionId() {
		return this.submissionId;
	}
	
	public void setSubmissionId(Integer submissionId) {
		this.submissionId= submissionId;
	}
	
	public ContributionDetailItem getContributionData() {
		return this.contributionData;
	}
	
	public void setContributionData(ContributionDetailItem contributionData) {
		this.contributionData = contributionData;
	}
	
	public Collection getErrors() {
		return contributionData.getReportDataErrors().getErrors();
	}
	
	public void addErrors(Collection additionalErrors) {
		contributionData.getReportDataErrors().addErrors(additionalErrors);
	}
	
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#getNumErrors()
	 */
	public int getNumErrors() {
		return contributionData.getReportDataErrors().getNumErrors();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#getNumSyntaxErrors()
	 */
	public int getNumSyntaxErrors() {
		return contributionData.getReportDataErrors().getNumSyntaxErrors();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#getNumWarnings()
	 */
	public int getNumWarnings() {
		return contributionData.getReportDataErrors().getNumWarnings();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#isDiscardFlag()
	 */
	public boolean isDiscardFlag() {
		return contributionData.getReportDataErrors().isDiscardFlag();
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection#isSyntaxErrorIndicator()
	 */
	public boolean isSyntaxErrorIndicator() {
		return contributionData.getReportDataErrors().isSyntaxErrorIndicator();
	}
}
