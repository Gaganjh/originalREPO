package com.manulife.pension.ps.web.fee;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.service.fee.valueobject.PartStmtFeesSummaryData;

/**
 * This is ActinForm class is used to creating a Fees Reconciliation tool.
 * 
 * @author eswar
 *
 */
public class PartStmtFeesToolForm extends BaseReportForm {
	
	/**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    private String reportType ;
	private String ssn1;
	private String ssn2;
	private String ssn3;
	private List<Date> stmtPeriodEndDates; 
	private String infoLevel;
	private String selectedStmtDate;
	private String name;
	private String lastName;
	private String firstName;
	private boolean searchResultAllowed;
	private boolean downloadAllowed;
	private PartStmtFeesSummaryData partStmtFeesSummaryData;
	private String requestStatus;
	private BigDecimal participantId = BigDecimal.ZERO;
	private boolean opsInvestgation;
	private boolean statementEndDatesAvailable;
	private boolean requestSuccess;
	
	/**
	 * @return reportType
	 */
	public String getReportType() {
		return reportType;
	}
	
	
	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	/**
	 * @return ssn
	 */
	public String getSsn() {
		return (new StringBuffer(ssn1).append(ssn2).append(ssn3)).toString();
	}
	
	/**
	 * @return stmtPeriodEndDates
	 */
	public List<Date> getStmtPeriodEndDates() {
		return stmtPeriodEndDates;
	}
	
	/**
	 * @param stmtPeriodEndDates the stmtPeriodEndDates to set
	 */
	public void setStmtPeriodEndDates(List<Date> stmtPeriodEndDates) {
		this.stmtPeriodEndDates = stmtPeriodEndDates;
	}
	
	/**
	 * @return infoLevel
	 */
	public String getInfoLevel() {
		return infoLevel;
	}
	
	/**
	 * @param infoLevel the infoLevel to set
	 */
	public void setInfoLevel(String infoLevel) {
		this.infoLevel = infoLevel;
	}
	
	/**
	 * @return selectedStmtDate
	 */
	public String getSelectedStmtDate() {
		return selectedStmtDate;
	}
	
	/**
	 * @param selectedStmtDate the selectedStmtDate to set
	 */
	public void setSelectedStmtDate(String selectedStmtDate) {
		this.selectedStmtDate = selectedStmtDate;
	}
	
	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}


	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	/**
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}


	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return ssn1
	 */
	public String getSsn1() {
		return ssn1;
	}
	
	/**
	 * @param ssn1 the ssn1 to set
	 */
	public void setSsn1(String ssn1) {
		this.ssn1 = ssn1;
	}
	
	/**
	 * @return ssn2
	 */
	public String getSsn2() {
		return ssn2;
	}
	
	/**
	 * @param ssn2 the ssn2 to set
	 */
	public void setSsn2(String ssn2) {
		this.ssn2 = ssn2;
	}
	
	/**
	 * @return ssn3
	 */
	public String getSsn3() {
		return ssn3;
	}
	
	/**
	 * @param ssn3  the ssn3 to set
	 */
	public void setSsn3(String ssn3) {
		this.ssn3 = ssn3;
	}
	
	/**
	 * @return searchResultAllowed
	 */
	public boolean isSearchResultAllowed() {
		return searchResultAllowed;
	}
	
	/**
	 * @param searchResultAllowed the searchResultAllowed to set
	 */
	public void setSearchResultAllowed(boolean searchResultAllowed) {
		this.searchResultAllowed = searchResultAllowed;
	}
	
	/**
	 * @return downloadAllowed
	 */
	public boolean isDownloadAllowed() {
		return downloadAllowed;
	}
	
	/**
	 * @param downloadAllowed the downloadAllowed to set
	 */
	public void setDownloadAllowed(boolean downloadAllowed) {
		this.downloadAllowed = downloadAllowed;
	}


	/**
	 * @return partStmtFeesSummaryData
	 */
	public PartStmtFeesSummaryData getPartStmtFeesSummaryData() {
		return partStmtFeesSummaryData;
	}


	/**
	 * @param partStmtFeesSummaryData the partStmtFeesSummaryData to set
	 */
	public void setPartStmtFeesSummaryData(
			PartStmtFeesSummaryData partStmtFeesSummaryData) {
		this.partStmtFeesSummaryData = partStmtFeesSummaryData;
	}


	/**
	 * @return requestStatus
	 */
	public String getRequestStatus() {
		return requestStatus;
	}


	/**
	 * @param requestStatus the requestStatus to set
	 */
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}


	/**
	 * @return participantId
	 */
	public BigDecimal getParticipantId() {
		return participantId;
	}


	/**
	 * @param participantId the participantId to set
	 */
	public void setParticipantId(BigDecimal participantId) {
		this.participantId = participantId;
	}

	
	/**
	 * @return opsInvestgation
	 */
	public boolean isOpsInvestgation() {
		return opsInvestgation;
	}


	/**
	 * @param opsInvestgation the opsInvestgation to set
	 */
	public void setOpsInvestgation(boolean opsInvestgation) {
		this.opsInvestgation = opsInvestgation;
	}


	/**
	 * @return statementEndDatesAvailable
	 */
	public boolean isStatementEndDatesAvailable() {
		return statementEndDatesAvailable;
	}


	/**
	 * @param statementEndDatesAvailable the statementEndDatesAvailable to set
	 */
	public void setStatementEndDatesAvailable(boolean statementEndDatesAvailable) {
		this.statementEndDatesAvailable = statementEndDatesAvailable;
	}


	/**
	 * @return requestSuccess
	 */
	public boolean isRequestSuccess() {
		return requestSuccess;
	}


	/**
	 * @param requestSuccess the requestSuccess to set
	 */
	public void setRequestSuccess(boolean requestSuccess) {
		this.requestSuccess = requestSuccess;
	}
}
