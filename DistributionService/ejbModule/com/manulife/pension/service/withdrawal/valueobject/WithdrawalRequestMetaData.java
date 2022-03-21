package com.manulife.pension.service.withdrawal.valueobject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.service.distribution.valueobject.ActivitySummaryStatusCode;

/**
 * Defines the meta data associated with the lookup and validation of a request to intitiate or
 * review a withdrawal request.
 * 
 * @author dickand
 */
public class WithdrawalRequestMetaData implements Serializable {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private Integer submissionId;

    private Integer contractId;

    private Integer profileId;

    private String statusCode;

    private ContractInfo contractInfo;

    private String initiatedBy;

    private Integer participantId;

    private String participantMaskedSsn;

    private List<WithdrawalActivitySummary> withdrawalActivitySummaries;
    
    private Date expirationDate;

    /**
     * The value if the request was initiated by a participant.
     */
    public static final String INITIATED_BY_PARTICIPANT = "Participant";

    /**
     * @return the initiatedBy
     */
    public String getInitiatedBy() {
        return initiatedBy;
    }

    /**
     * @param initiatedBy the initiatedBy to set
     */
    public void setInitiatedBy(final String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    /**
     * Default constructor.
     */
    public WithdrawalRequestMetaData() {
    }

    /**
     * Constructor.
     * 
     * @param submissionId The submission ID.
     * @param contractId The contract ID.
     * @param profileId The participant profile ID.
     * @param statusCode The status code.
     * @param initiatedBy The profile ID of the user that initiated this request.
     */
    public WithdrawalRequestMetaData(final Integer submissionId, final Integer contractId,
            final Integer profileId, final String statusCode, final String initiatedBy) {
        this.submissionId = submissionId;
        this.contractId = contractId;
        this.profileId = profileId;
        this.statusCode = statusCode;
        this.initiatedBy = initiatedBy;
    }

    /**
     * Constructor.
     * 
     * @param contractId The contract ID.
     * @param profileId The participant profile ID.
     * @param statusCode The status code.
     */
    public WithdrawalRequestMetaData(final Integer contractId, final Integer profileId,
            final String statusCode) {
        this.contractId = contractId;
        this.profileId = profileId;
        this.statusCode = statusCode;
    }

    /**
     * @return the submissionId
     */
    public Integer getSubmissionId() {
        return submissionId;
    }

    /**
     * @param submissionId the submissionId to set
     */
    public void setSubmissionId(final Integer submissionId) {
        this.submissionId = submissionId;
    }

    /**
     * @return the contractId
     */
    public Integer getContractId() {
        return contractId;
    }

    /**
     * @param contractId the contractId to set
     */
    public void setContractId(final Integer contractId) {
        this.contractId = contractId;
    }

    /**
     * This is the participant profile ID.
     * 
     * @return the profileId
     */
    public Integer getProfileId() {
        return profileId;
    }

    /**
     * Sets the participant profile ID.
     * 
     * @param profileId the profileId to set
     */
    public void setProfileId(final Integer profileId) {
        this.profileId = profileId;
    }

    /**
     * @return the statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * 
     * @return the contractInfo
     */
    public ContractInfo getContractInfo() {
        return contractInfo;
    }

    /**
     * Sets the contract info for the selected contract.
     * 
     * @param contractInfo The {@link ContractInfo} object to set.
     */
    public void setContractInfo(final ContractInfo contractInfo) {
        this.contractInfo = contractInfo;
    }

    /**
     * @return the participantId
     */
    public Integer getParticipantId() {
        return participantId;
    }

    /**
     * @param participantId the participantId to set
     */
    public void setParticipantId(final Integer participantId) {
        this.participantId = participantId;
    }

    /**
     * @return the participantMaskedSsn
     */
    public String getParticipantMaskedSsn() {
        return participantMaskedSsn;
    }

    /**
     * @param participantMaskedSsn the participantMaskedSsn to set
     */
    public void setParticipantMaskedSsn(final String participantMaskedSsn) {
        this.participantMaskedSsn = participantMaskedSsn;
    }

    /**
     * @return the isInitiate
     */
    public boolean getIsInitiate() {
        return StringUtils.isBlank(statusCode)
                || StringUtils.equals(statusCode, WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
    }

    /**
     * @return the isNew
     */
    public boolean getIsNew() {
        return StringUtils.isBlank(statusCode);
    }

    /**
     * Returns true if this withdrawal request was initiated by a participant, false otherwise.
     * 
     * @return boolean - True if it's initiated by a participant, false otherwise.
     */
    public boolean getIsInitiatedByAParticipant() {
        return StringUtils.equals(initiatedBy, INITIATED_BY_PARTICIPANT);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {

        return ToStringBuilder.reflectionToString(this,
                GlobalConstants.DEFAULT_TO_STRING_BUILDER_STYLE);
    }

    /**
     * @return List&lt;WithdrawalActivitySummary&gt; - The withdrawalActivitySummaries.
     */
    public List<WithdrawalActivitySummary> getWithdrawalActivitySummaries() {
        return withdrawalActivitySummaries;
    }

    /**
     * @param withdrawalActivitySummaries - The withdrawalActivitySummaries to set.
     */
    public void setWithdrawalActivitySummaries(
            final List<WithdrawalActivitySummary> withdrawalActivitySummaries) {
        this.withdrawalActivitySummaries = withdrawalActivitySummaries;
    }

    /**
     * This gets the profile ID of the user that initiated this request, and returns null if there
     * is no such user.
     * 
     * @return Integer - The profile ID of the user.
     */
    public Integer getInitiator() {
        return getUserProfileId(ActivitySummaryStatusCode.SENT_FOR_REVIEW);
    }

    /**
     * This gets the profile ID of the user that reviewed this request, and returns null if there is
     * no such user.
     * 
     * @return Integer - The profile ID of the user.
     */
    public Integer getReviewer() {
        return getUserProfileId(ActivitySummaryStatusCode.SENT_FOR_APPROVAL);
    }

    /**
     * This gets the profile ID of the user that approved this request, and returns null if there is
     * no such user.
     * 
     * @return Integer - The profile ID of the user.
     */
    public Integer getApprover() {
        return getUserProfileId(ActivitySummaryStatusCode.APPROVED);
    }

    /**
     * This gets the profile ID of the user that deleted this request, and returns null if there is
     * no such user.
     * 
     * @return Integer - The profile ID of the user.
     */
    public Integer getDeletor() {
        return getUserProfileId(ActivitySummaryStatusCode.DELETED);
    }

    /**
     * This gets the profile ID of the user that denied this request, and returns null if there is
     * no such user.
     * 
     * @return Integer - The profile ID of the user.
     */
    public Integer getDenyor() {
        return getUserProfileId(ActivitySummaryStatusCode.DENIED);
    }

    /**
     * Looks up the user from the summaries with the given code.
     * 
     * @param activitySummaryStatusCode The code to lookup for.
     * @return Integer - The user profile ID, or null if they are not found.
     */
    private Integer getUserProfileId(final ActivitySummaryStatusCode activitySummaryStatusCode) {
        if (CollectionUtils.isNotEmpty(withdrawalActivitySummaries)) {
            for (WithdrawalActivitySummary withdrawalActivitySummary : withdrawalActivitySummaries) {
                if (StringUtils.equals(withdrawalActivitySummary.getStatusCode(),
                        activitySummaryStatusCode.getStatusCode())) {
                    return withdrawalActivitySummary.getCreatedById();
                } // fi
            } // end for
        } // fi
        return null;
    }

    /**
     * @param expirationDate - The expirationDate to set.
     */
    public void setExpirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return Date - The expirationDate.
     */
    public Date getExpirationDate() {
        return expirationDate;
    }
}
