package com.manulife.pension.ps.service.withdrawal.valueobject;

import java.util.Date;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.content.valueobject.Location;

/**
 * Value object used to hold minimal information about the submission request that should trigger
 * sending email messages on various scenarios. FIXME -- more detail
 * 
 * @author Mihai Popa
 */
public class SubmissionEmailVO extends BaseSerializableCloneableObject {

    private static final long serialVersionUID = 1L;

    private Integer submissionId;

    private Date lastStatusUpdateTS;

    private Integer contractId;

    private String firstName;

    private String lastName;

    private String middleInitial;

    private String contractName;

    private Location siteLocation;

    private String requestStatus;

    private String requestType;

    private Date requestDate;

    private Date expirationDate;

    private String creatorEmail;

    private String contractStatus;
    
    private String createdByRoleCode;



	/**
     * Default Constructor.
     * 
     * @param submissionId
     * @param contractId
     * @param contractName
     * @param contractStatusCode
     * @param lastStatusUpdateTS
     * @param requestStatus
     * @param requestType
     * @param firstName
     * @param lastName
     * @param middleInitial
     * @param siteLocation
     * @param requestDate
     * @param expirationDate
     * @param creatorEmail
     */
    public SubmissionEmailVO(final Integer submissionId, final Integer contractId,
            final String contractName, final String contractStatusCode, final String createdByRoleCode,
            final Date lastStatusUpdateTS, final String requestStatus, final String requestType,
            final String firstName, final String lastName, final String middleInitial,
            final Location siteLocation, final Date requestDate, final Date expirationDate,
            final String creatorEmail) {
        this.submissionId = submissionId;
        this.contractId = contractId;
        this.contractName = contractName;
        this.contractStatus = contractStatusCode;
        this.createdByRoleCode = createdByRoleCode;
        this.requestStatus = requestStatus;
        this.requestType = requestType;
        this.lastStatusUpdateTS = lastStatusUpdateTS;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleInitial = middleInitial;
        this.siteLocation = siteLocation;
        this.requestDate = requestDate;
        this.expirationDate = expirationDate;
        this.creatorEmail = creatorEmail;
    }

    public String getParticipantName() {
        return firstName.trim()
                + " "
                + (middleInitial != null && middleInitial.trim().length() > 0 ? middleInitial
                        .trim()
                        + " " : "") + lastName.trim();
    }

    /**
     * @return Integer - The contractId.
     */
    public Integer getContractId() {
        return contractId;
    }

    /**
     * @return String - The contractName.
     */
    public String getContractName() {
        return contractName;
    }

    /**
     * @return String - The contractStatus.
     */
    public String getContractStatus() {
        return contractStatus;
    }

    /**
     * @return String - The creatorEmail.
     */
    public String getCreatorEmail() {
        return creatorEmail;
    }

    /**
     * @return Date - The expirationDate.
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * @return String - The firstName.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return String - The lastName.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return Date - The lastStatusUpdateTS.
     */
    public Date getLastStatusUpdateTS() {
        return lastStatusUpdateTS;
    }

    /**
     * @return String - The middleInitial.
     */
    public String getMiddleInitial() {
        return middleInitial;
    }

    /**
     * @return Date - The requestDate.
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * @return String - The requestStatus.
     */
    public String getRequestStatus() {
        return requestStatus;
    }

    /**
     * @return String - The requestType.
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * @return Location - The siteLocation.
     */
    public Location getSiteLocation() {
        return siteLocation;
    }

    /**
     * @return Integer - The submissionId.
     */
    public Integer getSubmissionId() {
        return submissionId;
    }
    
    public String getCreatedByRoleCode() {
		return createdByRoleCode;
	}



}