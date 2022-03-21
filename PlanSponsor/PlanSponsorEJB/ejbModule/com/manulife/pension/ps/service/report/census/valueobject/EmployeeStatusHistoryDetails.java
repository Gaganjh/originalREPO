package com.manulife.pension.ps.service.report.census.valueobject;

import java.io.Serializable;
import java.util.Date;

public class EmployeeStatusHistoryDetails implements Serializable, Cloneable{

	private static final long serialVersionUID = 1L;
	private Date effectiveDate;   
	private String status;
	private String lastUpdatedUserId;
	private Date lastUpdatedTs;
	private String lastUpdatedUserType;
	private String sourceChannelCode;
	private boolean markedForDeletion;
	private boolean markedForDeletionDisplay;
	private boolean cancelledStatus;
	
	public EmployeeStatusHistoryDetails(){
		this.effectiveDate =null;   
		this.status =null;
		this.lastUpdatedUserId =null;
		this.lastUpdatedTs =null;
		this.lastUpdatedUserType =null;
		this.sourceChannelCode =null;
		this.markedForDeletion = false;
		this.markedForDeletionDisplay = false;
		this.cancelledStatus = false;		
	}
	public EmployeeStatusHistoryDetails (Date effectiveDate, String status, String lastUpdatedUserId, Date lastUpdatedTs, String lastUpdatedUserType, boolean markedForDeletion, boolean cancelledStatus ){
		this.effectiveDate =effectiveDate;   
		this.status =status;
		this.lastUpdatedUserId =lastUpdatedUserId;
		this.lastUpdatedTs =lastUpdatedTs;
		this.lastUpdatedUserType =lastUpdatedUserType;
		this.markedForDeletion = markedForDeletion;
		this.markedForDeletionDisplay = false;
		this.cancelledStatus = cancelledStatus;		
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setLastUpdatedTs(Date lastUpdatedTs) {
		this.lastUpdatedTs = lastUpdatedTs;
	}
	public Date getLastUpdatedTs() {
		return lastUpdatedTs;
	}
	public void setLastUpdatedUserType(String lastUpdatedUserType) {
		this.lastUpdatedUserType = lastUpdatedUserType;
	}
	public String getLastUpdatedUserType() {
		return lastUpdatedUserType;
	}
	public void setMarkedForDeletion(boolean markedForDeletion) {
		this.markedForDeletion = markedForDeletion;
	}
	public boolean isMarkedForDeletion() {
		return markedForDeletion;
	}
	public void setCancelledStatus(boolean cancelledStatus) {
		this.cancelledStatus = cancelledStatus;
	}
	public boolean isCancelledStatus() {
		return cancelledStatus;
	}
	   @Override
	    public Object clone() throws CloneNotSupportedException {
	        EmployeeStatusHistoryDetails details = new EmployeeStatusHistoryDetails();
			details.setEffectiveDate(this.effectiveDate);   
			details.setStatus(this.status);
			details.setLastUpdatedUserId(this.lastUpdatedUserId);
			details.setLastUpdatedTs(this.lastUpdatedTs);
			details.setLastUpdatedUserType(this.lastUpdatedUserType);
			details.setSourceChannelCode(this.sourceChannelCode);
			details.setMarkedForDeletion(this.markedForDeletion);
			details.setMarkedForDeletion(this.markedForDeletionDisplay);
			details.setCancelledStatus(this.cancelledStatus);	
	        
	        return details;
	    }
	public void setSourceChannelCode(String sourceChannelCode) {
		this.sourceChannelCode = sourceChannelCode;
	}
	public String getSourceChannelCode() {
		return sourceChannelCode;
	}
	public void setMarkedForDeletionDisplay(boolean markedForDeletionDisplay) {
		this.markedForDeletionDisplay = markedForDeletionDisplay;
	}
	public boolean isMarkedForDeletionDisplay() {
		return markedForDeletionDisplay;
	}
}
