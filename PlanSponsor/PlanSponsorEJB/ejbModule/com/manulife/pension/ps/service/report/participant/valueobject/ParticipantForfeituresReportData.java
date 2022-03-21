package com.manulife.pension.ps.service.report.participant.valueobject;

import com.manulife.pension.ps.service.report.participant.reporthandler.ParticipantForfeituresReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * ParticipantForfeituresReportData class to hold the Forfeiture Information 
 * for Accounts Forfeitures page.
 * 
 * @author Vinothkumar Balasubramaniyam
 */
public class ParticipantForfeituresReportData extends ReportData {

	/**
	 * Default Serial Version UID.  
	 */
	private static final long serialVersionUID = 1L;
	
	//Attribute to hold the Report ID
	public static final String REPORT_ID = ParticipantForfeituresReportHandler.class.getName();

	//Attribute to hold the Report Name
	public static final String REPORT_NAME = "participantForfeituresReport";
	
	//Attributes to hold the Filter Fields
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	public static final String FILTER_ASOFDATE = "asOfDate";
	public static final String FILTER_STATUS = "status";
	public static final String FILTER_LASTNAME = "lastName";
	public static final String FILTER_SSN = "ssn";
	public static final String FILTER_DIVISION = "division";
	
	//Attributes to hold the Sort Fields
	public static final String DEFAULT_SORT = "lastName";
	public static final String SORT_TERMINATION_DATE = "terminationDate";	
	
	//Attributes to hold the Sort and Filter criteria
	public static final String FILTER_SORT_CRITERIA = "sortFields";
	public static final String FILTER_FILTER_CRITERIA = "filterFields";	
	
	//Instance variables to hold the necessary value
	private int contractNumber;
	private String asOfDate;
	private String status;
	private ParticipantForfeituresTotals participantForfeituresTotals;

	/**
	 * Argument Constructor
	 * 
	 * @param criteria
	 * @param totalParticipantsCount
	 */
	public ParticipantForfeituresReportData(ReportCriteria criteria, int totalParticipantsCount) {
		super(criteria, totalParticipantsCount);
	}

	/**
	 * Gets the participantForfeituresTotals
	 * 
	 * @return Returns a ParticipantForfeituresTotals
	 */
	public ParticipantForfeituresTotals getParticipantForfeituresTotals() {
		return participantForfeituresTotals;
	}
	
	/**
	 * Sets the participantForfeituresTotals
	 * 
	 * @param participantForfeituresTotals The participantForfeituresTotals to set
	 */
	public void setParticipantForfeituresTotals(ParticipantForfeituresTotals participantForfeituresTotals) {
		this.participantForfeituresTotals = participantForfeituresTotals;
	}
	
	/**
	 * Gets the contractNumber
	 * 
	 * @return Returns the contractNumber as integer
	 */
	public int getContractNumber() {
		return contractNumber;
	}

	/**
	 * Sets the contractNumber
	 * 
	 * @param contractNumber The contractNumber to set
	 */
	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}

	/**
	 * Gets the asOfDate
	 * 
	 * @return Returns a String
	 */
	public String getAsOfDate() {
		return asOfDate;
	}

	/**
	 * Sets the asOfDate
	 * 
	 * @param asOfDate The asOfDate to set
	 */
	public void setAsOfDate(String asOfDate) {
		this.asOfDate = asOfDate;
	}

	/**
	 * Gets the status
	 * 
	 * @return Returns a String
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status
	 * 
	 * @param status The status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append( super.toString() ).append("\n");
		result.append("contractNmber: ").append(getContractNumber()).append("\n");
		result.append("asOfDate: ").append(getAsOfDate()).append("\n");
		result.append("status: ").append(getStatus()).append("\n");		
		result.append("participantForfeituresTotals: ")
			.append(participantForfeituresTotals.toString()).append("\n");
		return result.toString();
	}
}
