package com.manulife.pension.ps.service.report.participant.valueobject;

import com.manulife.pension.ps.service.report.participant.reporthandler.ParticipantEnrollmentDetailsReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class ParticipantEnrollmentDetailsReportData extends ReportData {

	public static final String REPORT_ID = ParticipantEnrollmentDetailsReportHandler.class.getName();
	public static final String REPORT_NAME = "participantEnrollmentDetailsReport";
	public static final String FILTER_FIELD_1 = "contractNumber";
	public static final String FILTER_FIELD_2 = "profileId";
	public static final String DEFAULT_SORT = "";

	private int contractNumber;
	private double profileId;

	public ParticipantEnrollmentDetailsReportData(ReportCriteria criteria, int totalCount) {
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

	/**
	 * Gets the profile id
	 * @return Returns a double
	 */
	public double getProfileId() {
		
		return profileId;
	}
	/**
	 * Sets the profileId
	 * @param profileId The profileId to set
	 */
	public void setProfileId(double profileId) {
		this.profileId = profileId;
	}
	
	public String toString() {
		StringBuffer dump = new StringBuffer();
		dump.append( super.toString() ).append("\n");
		dump.append("contractNmber: ").append(getContractNumber()).append("\n");
		dump.append("profileId: ").append(getProfileId()).append("\n");
		return dump.toString();
	}	
}
