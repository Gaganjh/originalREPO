package com.manulife.pension.ps.web.participant;

import java.util.List;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Message;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.service.report.participant.valueobject.TaskCenterTasksDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.TaskCenterTasksReportData;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.report.ReportActionCloneableForm;
import com.manulife.pension.util.StaticHelperClass;
import com.manulife.pension.util.content.manager.ContentCacheManager;

/**
 * Form in support of the TaskCenter Tasks page.
 * 
 * @author Glen Lalonde
 *
 */
public class TaskCenterTasksReportForm extends ReportActionCloneableForm {
	private static final long serialVersionUID= 1L;
	
	private TaskCenterTasksReportData report = null;

	// filter stuff at top of form
	private String ssnOne;
	private String ssnTwo;
	private String ssnThree;
	private String lastName = null;
	private String division;
	private String contractNumber;
	
	// user input remarks section
	private String remarks = "";
	private boolean showRemarks = false;
	
	// display control
	private boolean showActionButtons = false;
	
	// manually manipulate timestamp field so the listing will show
	// both pending AND processed records done in this session.
	private Long processedTimestamp = null;
	
	// conditinal columns on output
	private boolean hasDivisionFeature;
	private boolean hasPayrollNumberFeature;
	
	/**
	 * Constructor for ParicipantSummaryReportForm
	 */
	public TaskCenterTasksReportForm() {
		super(); 
	}

	
	public TaskCenterTasksDetails getTheItem(int index) {
		return ((List<TaskCenterTasksDetails>)this.getReport().getDetails()).get(index);
	}
	
	public List<TaskCenterTasksDetails> getDetailsList() {
		return (List<TaskCenterTasksDetails>)this.getReport().getDetails();
	}
	
	
	// deal with un-checked checkbox not being sent, may have been checked before though.
	// need to reset so it does not say checked.
	public void reset( HttpServletRequest request) {
		super.reset( request);
		String method = request.getMethod();
		if ("GET".equalsIgnoreCase(method)) return; // wtf gets called twice
		if ((this.getReport() !=null) && getDetailsList() !=null) {
	        for (TaskCenterTasksDetails detailItem : getDetailsList()) {
	        	detailItem.resetOptions(); 
	        }
		}
	}
	
	// ******
	//
	// do some logic here, which is too complex for the form, and since it
	// is only needed once, too big a hasle to put in a tag. Thus we have some
	// code here
		
	public String getAlertText(TaskCenterTasksDetails detailsItem) throws ContentException {
		StringBuffer buffer = new StringBuffer();
		
		if (detailsItem.hasNoDeferralOnFileAlertForAuto()) {
			buffer.append(getMessage(ErrorCodes.NO_DEFERRAL_ON_FILE_AUTO));			
		} else if (detailsItem.hasNoDeferralOnFileAlertForSignup()) {
			buffer.append(getMessage(ErrorCodes.NO_DEFERRAL_ON_FILE_SIGNUP));			
		}
		
		if (detailsItem.hasOverdueAlert()) {
			if (buffer.length()>0) { buffer.append("\n"); }			
			buffer.append(getMessage(ErrorCodes.ACI_OVERDUE));
		}
		
		if (detailsItem.hasOverdueWarningForAuto()) {
			if (buffer.length()>0) { 
				buffer.append("\n"); 
			}
			buffer.append(getMessage(ErrorCodes.ACI_REQUEST_OVERDUE_WARN_AUTO));
		} else if (detailsItem.hasOverdueWarningForSignup()) {
			if (buffer.length()>0) { 
				buffer.append("\n"); 	
			}	
			buffer.append(getMessage(ErrorCodes.ACI_REQUEST_OVERDUE_WARN_SIGNUP));	
		}
		if (detailsItem.hasMulitipleOutstandingRequestsWarningForAuto()) {
			if (buffer.length()>0) { 
				buffer.append("\n"); 	
			}	
			buffer.append(getMessage(ErrorCodes.ACI_OUTSTANDING_REQUESTS_AUTO));
		} else if (detailsItem.hasMulitipleOutstandingRequestsWarningForSignup()) {
			if (buffer.length()>0) { 
				buffer.append("\n"); 		
			}	
			buffer.append(getMessage(ErrorCodes.ACI_OUTSTANDING_REQUESTS_SIGNUP));	
		}
		
		return buffer.toString();
	}
	
	public String getWarningText(TaskCenterTasksDetails detailsItem) throws ContentException {
		// never gets here if we have alerts, so just handle warnings.
		
		StringBuffer buffer = new StringBuffer();
		if (detailsItem.hasOverdueWarningForAuto()) {
			buffer.append(getMessage(ErrorCodes.ACI_REQUEST_OVERDUE_WARN_AUTO));
		} else if (detailsItem.hasOverdueWarningForSignup()) {
			buffer.append(getMessage(ErrorCodes.ACI_REQUEST_OVERDUE_WARN_SIGNUP));
		}
		if (detailsItem.hasMulitipleOutstandingRequestsWarningForAuto()) {
			if (buffer.length()>0) { 
				buffer.append("\n"); 
				buffer.append(getMessage(ErrorCodes.ACI_OUTSTANDING_REQUESTS_AUTO));
			}
		} else if (detailsItem.hasMulitipleOutstandingRequestsWarningForSignup()) {
			if (buffer.length()>0) { 
				buffer.append("\n"); 
				buffer.append(getMessage(ErrorCodes.ACI_OUTSTANDING_REQUESTS_SIGNUP));
			}
		}
		
		return buffer.toString();
	}

	private String getMessage(int id) throws ContentException {
		Message msg = (Message)ContentCacheManager.getInstance().getContentById(id, ContentTypeManager.instance().MESSAGE);
		if (msg==null) return "";
		return msg.getText();
	}
	// ******
	
	
	
	public String getLastName() {
		return trim(this.lastName);
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public TaskCenterTasksReportData getReport(){
		return this.report;
	}	


	public void setReport(TaskCenterTasksReportData report){
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

	

	public String getContractNumber() {
		return contractNumber;
	}


	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	
	public boolean isShowActionButtons() {
		return showActionButtons;
	}


	public void setShowActionButtons(boolean showActionButtons) {
		this.showActionButtons = showActionButtons;
	}
	
	
	public Long getProcessedTimestamp() {
		return processedTimestamp;
	}


	public void setProcessedTimestamp(Long processedTimestamp) {
		this.processedTimestamp = processedTimestamp;
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


	// support stuff
	public String toString() {
		return StaticHelperClass.toString(this);
	}	
}
