/*
 * Created on May 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.service.report.iloans.valueobject;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shinchr
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoanRequestDetailVO implements Serializable{

	private String loanRequestId;
	private String profileId;
	private String contractNumber;

    private	String 		requestStatus;
    private	Date 		reqDate = null;


	private String      contractName;
	private String      participantFirstName;
	private String      participantLastName;
	private String      participantSSN;
	private String		initiatedBy;
	private String		loanRequestStatusCode;
	
	public LoanRequestDetailVO(String loanRequestId,
	        String profileId,
	        String contractNumber, 
	        String requestStatus, 
	        Date reqDate, 
	        String contractName, 
	        String participantFirstName, 
	        String participantLastName, 
	        String participantSSN,
	        String initiatedBy,
	        String loanRequestStatusCode)
	{
	    this.loanRequestId = loanRequestId;
	    this.profileId = profileId;
	    this.contractNumber = contractNumber;
	    this.requestStatus = requestStatus;
	    this.reqDate = reqDate;
	    this.contractName = contractName;
	    this.participantFirstName = participantFirstName;
	    this.participantLastName = participantLastName;
	    this.participantSSN = participantSSN;
	    this.initiatedBy = initiatedBy;
	    this.loanRequestStatusCode = loanRequestStatusCode;	
	}
    /**
     * @return Returns the contractName.
     */
    public String getContractName() {
        return contractName;
    }
    /**
     * @param contractName The contractName to set.
     */
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
    /**
     * @return Returns the contractNumber.
     */
    public String getContractNumber() {
        return contractNumber;
    }
    /**
     * @param contractNumber The contractNumber to set.
     */
    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }
    /**
     * @return Returns the loanRequestId.
     */
    public String getLoanRequestId() {
        return loanRequestId;
    }
    /**
     * @param loanRequestId The loanRequestId to set.
     */
    public void setLoanRequestId(String loanRequestId) {
        this.loanRequestId = loanRequestId;
    }
    /**
     * @return Returns the participantFirstName.
     */
    public String getParticipantFirstName() {
        return participantFirstName;
    }
    /**
     * @param participantFirstName The participantFirstName to set.
     */
    public void setParticipantFirstName(String participantFirstName) {
        this.participantFirstName = participantFirstName;
    }
    /**
     * @return Returns the participantLastName.
     */
    public String getParticipantLastName() {
        return participantLastName;
    }
    /**
     * @param participantLastName The participantLastName to set.
     */
    public void setParticipantLastName(String participantLastName) {
        this.participantLastName = participantLastName;
    }
    /**
     * @return Returns the participantSSN.
     */
    public String getParticipantSSN() {
        return participantSSN;
    }
    /**
     * @param participantSSN The participantSSN to set.
     */
    public void setParticipantSSN(String participantSSN) {
        this.participantSSN = participantSSN;
    }
    /**
     * @return Returns the profileId.
     */
    public String getProfileId() {
        return profileId;
    }
    /**
     * @param profileId The profileId to set.
     */
    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
    /**
     * @return Returns the reqDate.
     */
    public Date getReqDate() {
        return reqDate;
    }
    /**
     * @param reqDate The reqDate to set.
     */
    public void setReqDate(Date reqDate) {
        this.reqDate = reqDate;
    }
    /**
     * @return Returns the requestStatus.
     */
    public String getRequestStatus() {
        return requestStatus;
    }
    /**
     * @param requestStatus The requestStatus to set.
     */
    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
    /**
     * @return Returns the initiatedBy.
     */
    public String getInitiatedBy() {
        return initiatedBy;
    }
    /**
     * @param initiatedBy The initiatedBy to set.
     */
    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }
    
    public String getParticipantName() {
        return this.participantFirstName+" "+this.participantLastName;
    }
    /**
     * @return Returns the loanRequestStatusCode.
     */
    public String getLoanRequestStatusCode() {
        return loanRequestStatusCode;
    }
    /**
     * @param loanRequestStatusCode The loanRequestStatusCode to set.
     */
    public void setLoanRequestStatusCode(String loanRequestStatusCode) {
        this.loanRequestStatusCode = loanRequestStatusCode;
    }
}
