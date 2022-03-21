package com.manulife.pension.service.withdrawal.valueobject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.manulife.pension.common.BaseSerializableCloneableObject;

/**
 * RequestSummary is the value object (or DTO) used to transfer data for a summary of a request.
 * Used on the Loan And Withdrawal request list page.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.2 2006/08/23 14:42:00
 */
public class RequestSummary extends BaseSerializableCloneableObject {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private String contractName;

    private BigDecimal contractNumber;

    private Date dateOfRequest;

    private Map editLinkAttributes;

    private String initiatedByUserType;

    private String participantName;

    private String referenceNumber;

    private String requestReason;

    private String requestType;

    private String socialSecurityNumber;

    private String status;

    private Map viewLinkAttributes;

    /**
     * @return the contractName
     */
    public String getContractName() {
        return contractName;
    }

    /**
     * @return the contractNumber
     */
    public BigDecimal getContractNumber() {
        return contractNumber;
    }

    /**
     * @return the dateOfRequest
     */
    public Date getDateOfRequest() {
        return dateOfRequest;
    }

    /**
     * @return the editLinkAttributes
     */
    public Map getEditLinkAttributes() {
        return editLinkAttributes;
    }

    /**
     * @return the initiatedByUserType
     */
    public String getInitiatedByUserType() {
        return initiatedByUserType;
    }

    /**
     * @return the participantName
     */
    public String getParticipantName() {
        return participantName;
    }

    /**
     * @return the referenceNumber
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @return the requestReason
     */
    public String getRequestReason() {
        return requestReason;
    }

    /**
     * @return the requestType
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * @return the socialSecurityNumber
     */
    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return the viewLinkAttributes
     */
    public Map getViewLinkAttributes() {
        return viewLinkAttributes;
    }

    /**
     * @param contractName the contractName to set
     */
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    /**
     * @param contractNumber the contractNumber to set
     */
    public void setContractNumber(BigDecimal contractNumber) {
        this.contractNumber = contractNumber;
    }

    /**
     * @param dateOfRequest the dateOfRequest to set
     */
    public void setDateOfRequest(Date dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    /**
     * @param editLinkAttributes the editLinkAttributes to set
     */
    public void setEditLinkAttributes(Map editLinkAttributes) {
        this.editLinkAttributes = editLinkAttributes;
    }

    /**
     * @param initiatedByUserType the initiatedByUserType to set
     */
    public void setInitiatedByUserType(String initiatedByUserType) {
        this.initiatedByUserType = initiatedByUserType;
    }

    /**
     * @param participantName the participantName to set
     */
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    /**
     * @param referenceNumber the referenceNumber to set
     */
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /**
     * @param requestReason the requestReason to set
     */
    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    /**
     * @param requestType the requestType to set
     */
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    /**
     * @param socialSecurityNumber the socialSecurityNumber to set
     */
    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @param viewLinkAttributes the viewLinkAttributes to set
     */
    public void setViewLinkAttributes(Map viewLinkAttributes) {
        this.viewLinkAttributes = viewLinkAttributes;
    }

}
