/*
 * RequestSearch.java,v 1.1 2006/08/16 14:17:13 Paul_Glenn Exp
 * RequestSearch.java,v
 * Revision 1.1  2006/08/16 14:17:13  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.service.withdrawal.valueobject;

import java.math.BigDecimal;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.common.Period;

/**
 * RequestSearch is the value object used to search for requests.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/08/16 14:17:13
 */
public class RequestSearch extends BaseSerializableCloneableObject {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private String contractName;

    private BigDecimal contractNumber;

    private String participantLastName;

    private Period requestPeriod;

    private String requestReason;

    private String requestType;

    // private SocialSecurityNumber socialSecurityNumber;

    private String status;

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
     * @return the participantLastName
     */
    public String getParticipantLastName() {
        return participantLastName;
    }

    /**
     * @return the requestPeriod
     */
    public Period getRequestPeriod() {
        return requestPeriod;
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

    // /**
    // * @return the socialSecurityNumber
    // */
    // public SocialSecurityNumber getSocialSecurityNumber() {
    // return socialSecurityNumber;
    // }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
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
     * @param participantLastName the participantLastName to set
     */
    public void setParticipantLastName(String participantLastName) {
        this.participantLastName = participantLastName;
    }

    /**
     * @param requestPeriod the requestPeriod to set
     */
    public void setRequestPeriod(Period requestPeriod) {
        this.requestPeriod = requestPeriod;
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

    // /**
    // * @param socialSecurityNumber
    // * the socialSecurityNumber to set
    // */
    // public void setSocialSecurityNumber(
    // SocialSecurityNumber socialSecurityNumber) {
    // this.socialSecurityNumber = socialSecurityNumber;
    // }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
