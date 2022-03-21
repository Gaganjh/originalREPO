package com.manulife.pension.ps.web.participant;

import java.util.Date;
import java.util.List;

import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantStatementItem;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.service.statement.valueobject.ParticipantStatementListVO;

/**
 * To store the Participant Statement Form page form values
 * 
 * @author munusra
 */
public class ParticipantStatementSearchForm extends ReportForm {
	
	public static final String FIELD_SSN = "ssn";
	public static final String FIELD_FIRST_NAME = "firstName";
	public static final String FIELD_LAST_NAME = "lastName";
	public static final int BASE_NUMBER_OF_COLUMNS = 22;			
	public static final int BASE_TABLE_WIDTH = 730;

	private String namePhrase = null;
	private String firstName = null;
	private Ssn ssn;
	private String ssnOne;
	private String ssnTwo;
	private String ssnThree;
    private String participantId = null;
    private String selectedFirstName = null;
    private String selectedLastName = null;
    private List<ParticipantStatementItem> participantDetails = null;
    private ParticipantStatementListVO participantStatementListVo = null;
    private Date stmtGenStartDate = null;
    private Date stmtGenEndDate = null;
    
	/**
	 * Constructor for ParicipantSummaryReportForm
	 */
	public ParticipantStatementSearchForm() {
		super();
	}
	
	/**
	 * Gets the namePhrase
	 * @return Returns a String
	 */
	public String getNamePhrase() {
		return namePhrase;
	}
	/**
	 * Sets the namePhrase
	 * @param namePhrase The namePhrase to set
	 */
	public void setNamePhrase(String namePhrase) {
		this.namePhrase = namePhrase;
	}

	/**
	 * Gets the firstName
	 * @return Returns a String
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * Sets the firstName
	 * @param firstName The firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * resets the form
	 */
	public void clear() {
		namePhrase = null;
		firstName = null;
		ssn = null;
		ssnOne = null;
		ssnTwo = null;
		ssnThree = null;
	}

	/**
	 * Gets the ssn
	 * @return Returns a Ssn
	 */
	public Ssn getSsn() {
		Ssn ssnTemp = new Ssn();
		ssnTemp.setDigits(0,ssnOne);
		ssnTemp.setDigits(1,ssnTwo);
		ssnTemp.setDigits(2,ssnThree);
		return ssnTemp;
	}
	/**
	 * Gets the ssnOne
	 * @return Returns a String
	 */
	public String getSsnOne() {
		return ssnOne;
	}
	/**
	 * Sets the ssnOne
	 * @param ssnOne The ssnOne to set
	 */
	public void setSsnOne(String ssnOne) {
		this.ssnOne = ssnOne;
	}


	/**
	 * Gets the ssnTwo
	 * @return Returns a String
	 */
	public String getSsnTwo() {
		return ssnTwo;
	}
	/**
	 * Sets the ssnTwo
	 * @param ssnTwo The ssnTwo to set
	 */
	public void setSsnTwo(String ssnTwo) {
		this.ssnTwo = ssnTwo;
	}


	/**
	 * Gets the ssnThree
	 * @return Returns a String
	 */
	public String getSsnThree() {
		return ssnThree;
	}
	/**
	 * Sets the ssnThree
	 * @param ssnThree The ssnThree to set
	 */
	public void setSsnThree(String ssnThree) {
		this.ssnThree = ssnThree;
	}

	private int getTableWidthNumber() {
		return BASE_TABLE_WIDTH;
	}
	
	private int getNumberOfColumns() {
		return BASE_NUMBER_OF_COLUMNS;
	}
	
	public String getTableWidth() {
		return String.valueOf(getTableWidthNumber());
	}
	
	public String getColumnSpan() {
		return String.valueOf(getNumberOfColumns());
	}
	
	/**
	 * @param participantDetails the participantDetails to set
	 */
	public void setParticipantDetails(List<ParticipantStatementItem> participantDetails) {
		this.participantDetails = participantDetails;
	}

	/**
	 * @return the participantDetails
	 */
	public List<ParticipantStatementItem> getParticipantDetails() {
		return participantDetails;
	}

	/**
	 * @param participantStatementListVo the participantStatementListVo to set
	 */
	public void setParticipantStatementListVo(ParticipantStatementListVO participantStatementListVo) {
		this.participantStatementListVo = participantStatementListVo;
	}

	/**
	 * @return the participantStatementListVo
	 */
	public ParticipantStatementListVO getParticipantStatementListVo() {
		return participantStatementListVo;
	}

	/**
	 * @param stmtGenStartDate the stmtGenStartDate to set
	 */
	public void setStmtGenStartDate(Date stmtGenStartDate) {
		this.stmtGenStartDate = stmtGenStartDate;
	}

	/**
	 * @return the stmtGenStartDate
	 */
	public Date getStmtGenStartDate() {
		return stmtGenStartDate;
	}

	/**
	 * @param stmtGenEndDate the stmtGenEndDate to set
	 */
	public void setStmtGenEndDate(Date stmtGenEndDate) {
		this.stmtGenEndDate = stmtGenEndDate;
	}

	/**
	 * @return the stmtGenEndDate
	 */
	public Date getStmtGenEndDate() {
		return stmtGenEndDate;
	}

	/**
	 * @param participantId the participantId to set
	 */
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	/**
	 * @return the participantId
	 */
	public String getParticipantId() {
		return participantId;
	}

	/**
	 * @param selectedFirstName the selectedFirstName to set
	 */
	public void setSelectedFirstName(String selectedFirstName) {
		this.selectedFirstName = selectedFirstName;
	}

	/**
	 * @return the selectedFirstName
	 */
	public String getSelectedFirstName() {
		return selectedFirstName;
	}

	/**
	 * @param selectedLastName the selectedLastName to set
	 */
	public void setSelectedLastName(String selectedLastName) {
		this.selectedLastName = selectedLastName;
	}

	/**
	 * @return the selectedLastName
	 */
	public String getSelectedLastName() {
		return selectedLastName;
	}
}

