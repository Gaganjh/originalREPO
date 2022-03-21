package com.manulife.pension.ps.web.participant;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.FastDateFormat;


import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.report.ReportActionCloneableForm;
import com.manulife.pension.service.report.participant.transaction.valueobject.TaskCenterHistoryDetails;
import com.manulife.pension.service.report.participant.transaction.valueobject.TaskCenterHistoryReportData;
import com.manulife.pension.util.StaticHelperClass;

/**
 * Form in support of the TaskCenter(Deferrals) History page.
 * 
 * @author Glen Lalonde
 *
 */
public class TaskCenterHistoryReportForm extends ReportActionCloneableForm {
	private static final long serialVersionUID= 1L;
	
 	private static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
 	
 	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
 	private static final FastDateFormat SIMPLE_DATE_FORMAT = FastDateFormat.getInstance(FORMAT_DATE_SHORT_MDY);

	private TaskCenterHistoryReportData report = null;

	// filter stuff at top of form
	private String ssnOne;
	private String ssnTwo;
	private String ssnThree;
	private String lastName = ""; // defect 6928
	private String division;
	private String fromDate = getCurrentDate(-24);
	private String toDate = getCurrentDate(0);
	
	private String contractNumber; // used for download
	private boolean hasDivisionFeature;
	private boolean hasPayrollNumberFeature;
	
	// user input remarks section
	private String remarks = "";
	private boolean showRemarks = false;
	
	
	/**
	 * Constructor for ParicipantSummaryReportForm
	 */
	public TaskCenterHistoryReportForm() {
		super(); 
	}

	
	public TaskCenterHistoryDetails getTheItem(int index) {
		return ((List<TaskCenterHistoryDetails>)this.getReport().getDetails()).get(index);
	}

	
	public List<TaskCenterHistoryDetails> getDetailsList() {
		return (List<TaskCenterHistoryDetails>)this.getReport().getDetails();
	}
	
	
	// deal with un-checked checkbox not being sent, may have been checked before though.
	// need to reset so it does not say checked.
	public void reset( HttpServletRequest request) {
		super.reset( request);
		// deal with un-checked checkbox not being sent, may have been checked before though.
		// need to reset so it does not say checked.
		String method = request.getMethod();
		if ("GET".equalsIgnoreCase(method)) return; // wtf gets called twice
		if ((this.getReport() !=null) && getDetailsList() !=null) {
	        for (TaskCenterHistoryDetails detailItem : getDetailsList()) {
	        	detailItem.resetOptions(); 
	        }
		}
	}
	
	// get the current date( mm/dd/yyyy format) with # of months offset by arg
	private String getCurrentDate(int monthOffset) {
		Calendar cal = Calendar.getInstance();
		if (monthOffset != 0) cal.add(Calendar.MONTH, monthOffset);
		return SIMPLE_DATE_FORMAT.format(cal.getTime());
	}

	public String getLastName() {
		return trim(this.lastName);
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public TaskCenterHistoryReportData getReport(){
		return this.report;
	}	


	public void setReport(TaskCenterHistoryReportData report){
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

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}
	
	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean isShowRemarks() {
		return showRemarks;
	}


	public void setShowRemarks(boolean showRemarks) {
		this.showRemarks = showRemarks;
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

	
	public String getContractNumber() {
		return contractNumber;
	}


	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	

	public boolean hasDivisionFeature() {
		return hasDivisionFeature;
	}


	public void setHasDivisionFeature(boolean hasDivisionFeature) {
		this.hasDivisionFeature = hasDivisionFeature;
	}

	public boolean hasPayrollNumberFeature() {
		return hasPayrollNumberFeature;
	}


	public void setHasPayrollNumberFeature(boolean hasPayrollNumberFeature) {
		this.hasPayrollNumberFeature = hasPayrollNumberFeature;
	}


	public String toString() {
		return StaticHelperClass.toString(this);
	}	
}
