package com.manulife.pension.ps.service.report.participant.valueobject;

import com.manulife.pension.service.environment.valueobject.ContractDatesVO;
import com.manulife.pension.ps.service.report.participant.reporthandler.ParticipantSummaryReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class ParticipantSummaryReportData extends ReportData {

	public static final String REPORT_ID = ParticipantSummaryReportHandler.class.getName();
	public static final String REPORT_NAME = "participantSummaryReport"; 
	public static final String FILTER_FIELD_1 = "contractNumber";
	public static final String FILTER_FIELD_2 = "asOfDate";
	public static final String FILTER_FIELD_3 = "status";
	public static final String FILTER_FIELD_4 = "lastName";
	public static final String FILTER_FIELD_5 = "ssn";
	//Gateway Phase 1 -- start
	//gateway filter setter
	public static final String FILTER_FIELD_6 = "gatewayChecked";
	//Gateway Phase 1 -- end
	
	public static final String FILTER_FIELD_7 = "division";
	
	public static final String FILTER_FIELD_8 = "totalAssetsFrom";
	public static final String FILTER_FIELD_9 = "totalAssetsTo";
	
	public static final String FILTER_FIELD_10 = "employmentStatus";
	public static final String FILTER_FIELD_11 = "firstName";//CL 110234
	public static final String FILTER_FIELD_12 = "stmtGenStartDate";
	public static final String FILTER_FIELD_13 = "stmtGenEndDate";
	public static final String FILTER_FIELD_14 = "managedAccountChecked";
	public static final String DEFAULT_SORT = "lastName";
	public static final int    PAGE_SIZE = 35; 
	
	private int contractNumber;
	private String asOfDate;
	private String status;
	private ParticipantSummaryTotals participantSummaryTotals;
	private ContractDatesVO monthEndDates;
	//Gateway Phase 1 -- start
	//participant gateway indicator
	private String participantGatewayInd;
	//Gateway Phase1 -- end
	private String managedAccountStatusInd; 
	public ParticipantSummaryReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}

	/**
	 * Gets the participantSummaryTotals
	 * @return Returns a ParticipantSummaryTotals
	 */
	public ParticipantSummaryTotals getParticipantSummaryTotals() {
		return participantSummaryTotals;
	}
	/**
	 * Sets the participantSummaryTotals
	 * @param participantSummaryTotals The participantSummaryTotals to set
	 */
	public void setParticipantSummaryTotals(ParticipantSummaryTotals participantSummaryTotals) {
		this.participantSummaryTotals = participantSummaryTotals;
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
	 * Gets the asOfDate
	 * @return Returns a String
	 */
	public String getAsOfDate() {
		return asOfDate;
	}

	/**
	 * Sets the asOfDate
	 * @param asOfDate The asOfDate to set
	 */
	public void setAsOfDate(String asOfDate) {
		this.asOfDate = asOfDate;
	}

	/**
	 * Gets the status
	 * @return Returns a String
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status
	 * @param status The status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the monthEndDates
	 * @return Returns a Collection
	 */
	public ContractDatesVO getMonthEndDates() {
		return monthEndDates;
	}

	/**
	 * Sets the monthEndDates
	 * @param monthEndDates The monthEndDates to set
	 */
	public void setMonthEndDates(ContractDatesVO monthEndDates) {
		this.monthEndDates = monthEndDates;
	}

	//Gateway Phase 1 -- start
	//participant gateway indicator

	/**
	 * Gets the participantGatewayInd
	 * @return Returns a String
	 */
	public String getParticipantGatewayInd() {
		return participantGatewayInd;
	}
	/**
	 * Sets the participantGatewayInd
	 * @param participantGatewayInd The participantGatewayInd to set
	 */
	public void setParticipantGatewayInd(String participantGatewayInd) {
		this.participantGatewayInd = participantGatewayInd;
	}
	//Gateway Phase1 -- end
	/**
	 * Gets the managedAccountStatusInd
	 * @return Returns a String
	 */
	public String getManagedAccountStatusInd() {
		return managedAccountStatusInd;
	}
	/**
	 * Sets the managedAccountStatusInd
	 * @param participantGatewayInd The managedAccountStatusInd to set
	 */
	public void setManagedAccountStatusInd(String managedAccountStatusInd) {
		this.managedAccountStatusInd = managedAccountStatusInd;
	}
	
	public String toString() {
		StringBuffer dump = new StringBuffer();
		dump.append( super.toString() ).append("\n");
		dump.append("contractNmber: ").append(getContractNumber()).append("\n");
		dump.append("asOfDate: ").append(getAsOfDate()).append("\n");
		dump.append("status: ").append(getStatus()).append("\n");		
		return dump.toString();
	}	
}

