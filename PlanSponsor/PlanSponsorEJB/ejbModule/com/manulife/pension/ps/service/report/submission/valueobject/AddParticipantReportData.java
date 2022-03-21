package com.manulife.pension.ps.service.report.submission.valueobject;

import com.manulife.pension.ps.service.report.submission.reporthandler.AddParticipantReportHandler;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.service.submission.valueobject.Lockable;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class AddParticipantReportData extends ReportData implements Lockable {

	public static String REPORT_ID = AddParticipantReportHandler.class.getName();
	public static String REPORT_NAME = "addParticipantReport"; 
	public static final String FILTER_FIELD_1 = "contractNumber";
	public static final String FILTER_FIELD_2 = "submissionId";
	public static final String CONTRIBUTION_TYPE_CODE = "C";	
	
	private int contractNumber;
	private String participantSortOption;
	private String systemStatus;
	private Integer submissionId;
	private Lock lock;
	
	
	public AddParticipantReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}

	/**
	 * Gets the contractNumber
	 * @return Returns a String
	 */
	public int getContractNumber() {
		return contractNumber;
	}

	/**
	 * Sets the contractNumber
	 * @param contractNumber The contractNumber to set
	 */
	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getParticipantSortOption(){
		return this.participantSortOption;
	}
	
	public void setParticipantSortOption(String participantSortOption) {
		this.participantSortOption = participantSortOption;
	}
	
	public String getSystemStatus() {
		return this.systemStatus;
	}
	
	public void setSystemStatus(String systemStatus) {
		this.systemStatus = systemStatus;
	}
	
	public String toString() {
		return super.toString() + "\ncontractNumber: " + getContractNumber() + "\nsubmissionId: " +
				submissionId;
	}	
	
	public Integer getContractId() {
		return new Integer(this.contractNumber);
	}
	public Lock getLock() {
		return this.lock;
	}
	public void setLock(Lock lock){
		this.lock = lock;
	}
	public Integer getSubmissionId() {
		return this.submissionId;
	}
	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}
	public String getType() {
		return CONTRIBUTION_TYPE_CODE;
	}
	
}
