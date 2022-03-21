package com.manulife.pension.service.distribution.valueobject;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.manulife.pension.common.GlobalConstants;

/**
 * Class is used to set / get the loan and withdraw  Risk information
 *  
 * @author Vasanth Balaji
 *
 */
public class AtRiskDetailsInputVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer submissionId = null;
	private RequestType loanOrWithdrawalReq = null;
	private Integer profileId = null; 
	private Integer contractId = null;
	private boolean isParticipantInitiated = false;
	private Date effectiveDate = null;
	
	/**
	 * @return the submissionId
	 */
	public final Integer getSubmissionId() {
		return submissionId;
	}
	/**
	 * @param submissionId the submissionId to set
	 */
	public final void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}

	/**
	 * @return the loanOrWithdrawalReq
	 */
	public RequestType getLoanOrWithdrawalReq() {
		return loanOrWithdrawalReq;
	}
	/**
	 * @param loanOrWithdrawalReq the loanOrWithdrawalReq to set
	 */
	public void setLoanOrWithdrawalReq(RequestType loanOrWithdrawalReq) {
		this.loanOrWithdrawalReq = loanOrWithdrawalReq;
	}
	
	/**
	 * @return the profileId
	 */
	public final Integer getProfileId() {
		return profileId;
	}
	/**
	 * @param profileId the profileId to set
	 */
	public final void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
	/**
	 * @return the contractId
	 */
	public final Integer getContractId() {
		return contractId;
	}
	/**
	 * @param contractId the contractId to set
	 */
	public final void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	/**
	 * @return the isParticipantInitiated
	 */
	public boolean isParticipantInitiated() {
		return isParticipantInitiated;
	}
	/**
	 * @param isParticipantInitiated the isParticipantInitiated to set
	 */
	public void setParticipantInitiated(boolean isParticipantInitiated) {
		this.isParticipantInitiated = isParticipantInitiated;
	}
	
	
	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                GlobalConstants.DEFAULT_TO_STRING_BUILDER_STYLE);
    }

}
