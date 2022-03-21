package com.manulife.pension.ps.service.report.participant.valueobject;

import java.util.Date;

import com.manulife.pension.ps.service.report.participant.reporthandler.ParticipantDeferralChangesReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class ParticipantDeferralChangesReportData extends ReportData {

	public static final String REPORT_ID = ParticipantDeferralChangesReportHandler.class.getName();
	public static final String REPORT_NAME = "participantDeferralChangesReport";
	public static final String FILTER_FIELD_1 = "contractNumber";
	public static final String FILTER_FIELD_2 = "fromDate";
	public static final String FILTER_FIELD_3 = "toDate";
	public static final String FILTER_FIELD_4 = "processedInd";
	public static final String DEFAULT_SORT = "changeDate";
	public static final int    PAGE_SIZE = 35;
	public static final int ALL_PROCESS_INDS = 0;
	public static final int UNPROCESSED_INDS_ONLY = 1;
	//Future functionality - allow only processed indicators to be selected
	//public static final int PROCESSED_INDS_ONLY = 2;
	
	
	private int contractNumber;
	private Date fromDate;
	private Date toDate;
	private int processedInd;
	private int numberParticipantsChanged;
	private int nonEmptyPayrollCount;	//count of payroll_no that is not null and == ' '
	private int nonEmptyOrganizationUnitCount; //count of organization_unit_id that is not null and == ' '
	private int rothCount;
	private boolean deferralInstructionInd;		//Stores the indicator to determine which instructions to display
	
	
	public ParticipantDeferralChangesReportData(ReportCriteria criteria, int totalCount) {
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
	 * Gets the fromDate
	 * @return Returns a Date
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * Sets the fromDate
	 * @param fromDate The fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * Gets the toDate
	 * @return Returns a Date
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * Sets the toDate
	 * @param toDate The toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}


	/**
	 * Gets the processedInd
	 * @return Returns a int
	 */
	public int getProcessedInd() {
		return processedInd;
	}



	/**
	 * Sets the processedInd
	 * @param processedInd The processedInd to set
	 */
	public void setProcessedInd(int processedInd) {
		this.processedInd = processedInd; 
	}


	/**
	 * Gets the numberParticipantsChanged
	 * @return Returns an int
	 */
	public int getNumberParticipantsChanged() {
		return numberParticipantsChanged;
	}

	/**
	 * Sets the numberParticipantsChanged
	 * @param numberParticipantsChanged The numberParticipantsChanged to set
	 */
	public void setNumberParticipantsChanged(int numberParticipantsChanged) {
		this.numberParticipantsChanged = numberParticipantsChanged;
	}
	
	/**
	 * Gets the nonEmptyPayrollCount
	 * @return Returns a String
	 */
	public int getNonEmptyPayrollCount() {
		return nonEmptyPayrollCount;
	}

	/**
	 * Sets the nonEmptyPayrollCount
	 * @param nonEmptyPayrollCount The nonEmptyPayrollCount to set
	 */
	public void setNonEmptyPayrollCount(int nonEmptyPayrollCount) {
		this.nonEmptyPayrollCount = nonEmptyPayrollCount;
	}
	
	/**
	 * Gets the nonEmptyOrganizationUnitCount
	 * @return Returns a String
	 */
	public int getNonEmptyOrganizationUnitCount() {
		return nonEmptyOrganizationUnitCount;
	}

	/**
	 * Sets the nonEmptyOrganizationUnitCount
	 * @param nonEmptyOrganizationUnitCount The nonEmptyOrganizationUnitCount to set
	 */
	public void setNonEmptyOrganizationUnitCount(int nonEmptyOrganizationUnitCount) {
		this.nonEmptyOrganizationUnitCount = nonEmptyOrganizationUnitCount;
	}
	
	/**
	 * @return Returns the deferralInstructionInd.
	 */
	public boolean getDeferralInstructionInd() {
		return deferralInstructionInd;
	}
	/**
	 * @param deferralInstructionInd The deferralInstructionInd to set.
	 */
	public void setDeferralInstructionInd(boolean deferralInstructionInd) {
		this.deferralInstructionInd = deferralInstructionInd;
	}
	
	public String toString() {
		StringBuffer dump = new StringBuffer();
		dump.append( super.toString() ).append("\n");
		dump.append("contractNmber: ").append(getContractNumber()).append("\n");
		dump.append("fromDate: ").append(getFromDate()).append("\n");
		dump.append("toDate: ").append(getToDate()).append("\n");		
		return dump.toString();
	}	
	/**
	 * @return Returns the rothCount.
	 */
	public int getRothCount() {
		return rothCount;
	}
	/**
	 * @param rothCount The rothCount to set.
	 */
	public void setRothCount(int rothCount) {
		this.rothCount = rothCount;
	}
}
