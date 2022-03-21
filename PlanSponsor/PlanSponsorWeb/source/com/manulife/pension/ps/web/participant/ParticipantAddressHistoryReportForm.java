package com.manulife.pension.ps.web.participant;

import java.util.Calendar;
import org.apache.commons.lang3.time.FastDateFormat;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressHistoryReportData;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.util.StaticHelperClass;

/**
 * Form in support of the Address History page.
 * 
 * @author Glen Lalonde
 *
 */
public class ParticipantAddressHistoryReportForm extends ReportForm {
	private static final long serialVersionUID= 1L;
	public static final String FIELD_SSN = "ssn";
	public static final String FIELD_LAST_NAME = "lastName";
    
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
    private static final FastDateFormat dateFormat = FastDateFormat.getInstance("MM/dd/yyyy"); 
	
	private String ssnOne;
	private String ssnTwo;
	private String ssnThree;
	private String namePhrase = null;
	private ParticipantAddressHistoryReportData report = null;
	
	private String fromDate;
	private String toDate;
	private String status;
	private String segment;
	private String division;
	
	/**
	 * Constructor for ParicipantSummaryReportForm
	 */
	public ParticipantAddressHistoryReportForm() {
		super(); 

		// establish defaults as per spec.
		Calendar calDate = Calendar.getInstance();
		toDate = dateFormat.format(calDate.getTime());
		
		calDate.add(Calendar.MONTH, -1); 
		fromDate = dateFormat.format(calDate.getTime()); 
	}

	/**
	 * Gets the namePhrase
	 * @return Returns a String
	 */
	public String getNamePhrase() {
		return trim(namePhrase);
	}
	
	/**
	 * Sets the namePhrase
	 * @param namePhrase The namePhrase to set
	 */
	public void setNamePhrase(String namePhrase) {
		this.namePhrase = namePhrase;
	}


	public ParticipantAddressHistoryReportData getReport(){
		return this.report;
	}	


	public void setReport(ParticipantAddressHistoryReportData report){
		this.report = report;
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

	public String getFromDate() { 
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String toString() {
		return StaticHelperClass.toString(this);
	}
	
}
