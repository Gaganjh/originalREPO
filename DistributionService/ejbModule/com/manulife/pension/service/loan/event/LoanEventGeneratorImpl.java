package com.manulife.pension.service.loan.event;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.event.LoanRequestAboutToExpireEvent;
import com.manulife.pension.event.LoanRequestApprovedEvent;
import com.manulife.pension.event.LoanRequestDeletedEvent;
import com.manulife.pension.event.LoanRequestDeniedEvent;
import com.manulife.pension.event.LoanRequestExpiredEvent;
import com.manulife.pension.event.LoanRequestLoanPackageEvent;
import com.manulife.pension.event.LoanRequestPendingAcceptanceEvent;
import com.manulife.pension.event.LoanRequestPendingApprovalEvent;
import com.manulife.pension.event.LoanRequestPendingReviewEvent;
import com.manulife.pension.event.LoanRequestRejectedEvent;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.SystemUser;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.util.BaseEnvironment;

/**
 * The implementation of Loan Event Generator Interface.
 * 
 */
public class LoanEventGeneratorImpl implements LoanEventGenerator {

	private int contractId;
	private int submissionId;
	private int eventInitiatorId;

	private static String APP_ID = null;

	static {
		BaseEnvironment environment = new BaseEnvironment();
		APP_ID = environment.getAppId();
	}

	/**
	 * LoanEventGenerator mandates the need for contractId, submissionId and the
	 * initiator
	 * 
	 * @param contractId
	 * @param submissionId
	 * @param eventInitiatorId
	 */
	public LoanEventGeneratorImpl(int contractId, int submissionId,
			int eventInitiatorId) {
		setContractId(contractId);
		setSubmissionId(submissionId);
		setEventInitiatorId(eventInitiatorId);
	}

	public void prepareAndSendParticipantPendingReviewEvent(
			int participantProfileId) {
		try {
			LoanRequestPendingReviewEvent event = new LoanRequestPendingReviewEvent(
					this.getClass().getName(), "prepareLoanPendingReviewEvent");
			event.setContractId(this.contractId);
			event.setSubmissionId(this.submissionId);
			event.setInitiator(SystemUser.EZK.getProfileId());
			event.setParticipantProfileId(participantProfileId);
			//Identify the give contract is bundled or non bundled contract.
			if(!isBundledIndicator()){
				EventClientUtility.getInstance(APP_ID).prepareAndSendJMSMessage(
						event);
			}
			
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	public void prepareAndSendPendingReviewEvent(int participantProfileId) {
		try {
			LoanRequestPendingReviewEvent event = new LoanRequestPendingReviewEvent(
					this.getClass().getName(), "prepareLoanPendingReviewEvent");
			event.setContractId(this.contractId);
			event.setSubmissionId(this.submissionId);
			event.setInitiator(this.eventInitiatorId);
			event.setParticipantProfileId(participantProfileId);
			//Identify the give contract is bundled or non bundled contract.
			if(!isBundledIndicator()){
				EventClientUtility.getInstance(APP_ID).prepareAndSendJMSMessage(
						event);
			}
			
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	public void prepareAndSendAboutToExpireEvent(int participantProfileId,
			String loanStatus, int daysToExpire) {
		try {
			LoanRequestAboutToExpireEvent event = new LoanRequestAboutToExpireEvent(
					this.getClass().getName(),
					"prepareLoanRequestAboutToExpireEvent");
			event.setContractId(this.contractId);
			event.setSubmissionId(this.submissionId);
			event.setInitiator(this.eventInitiatorId);
			event.setParticipantProfileId(participantProfileId);
			event.setLoanRequestStatus(loanStatus);
			event.setDaysToExpire(daysToExpire);
			EventClientUtility.getInstance(APP_ID).prepareAndSendJMSMessage(
					event);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	public void prepareAndSendApprovedEvent(int participantProfileId,
			String approverRoleCode, Boolean lastFeeChangedWasPlanSponsorUserInd) {
		try {
			LoanRequestApprovedEvent event = new LoanRequestApprovedEvent(this
					.getClass().getName(), "prepareLoanRequestApprovedEvent");
			event.setContractId(this.contractId);
			event.setSubmissionId(this.submissionId);
			event.setInitiator(this.eventInitiatorId);
			event.setParticipantProfileId(participantProfileId);
			event.setApproverRoleCode(approverRoleCode);

			if (lastFeeChangedWasPlanSponsorUserInd != null) {
				event
						.setTpaFeesChangedByPSIndicator(lastFeeChangedWasPlanSponsorUserInd);
			}
			EventClientUtility.getInstance(APP_ID).prepareAndSendJMSMessage(
					event);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	public void prepareAndSendDeletedEvent(int participantProfileId,
			String loanStatusBeforeDeleted) {
		try {
			LoanRequestDeletedEvent event = new LoanRequestDeletedEvent(this
					.getClass().getName(), "prepareLoanRequestDeletedEvent");
			event.setContractId(this.contractId);
			event.setSubmissionId(this.submissionId);
			event.setInitiator(this.eventInitiatorId);
			event.setParticipantProfileId(participantProfileId);
			event.setLoanStatusBeforeDeleted(loanStatusBeforeDeleted);
			EventClientUtility.getInstance(APP_ID).prepareAndSendJMSMessage(
					event);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}

	}

	public void prepareAndSendDeniedEvent(int participantProfileId,
			String loanStatusBeforeDenied) {
		try {
			LoanRequestDeniedEvent event = new LoanRequestDeniedEvent(this
					.getClass().getName(), "prepareLoanRequestDeniedEvent");
			event.setContractId(this.contractId);
			event.setSubmissionId(this.submissionId);
			event.setInitiator(this.eventInitiatorId);
			event.setParticipantProfileId(participantProfileId);

			event.setLoanStatusBeforeDenied(loanStatusBeforeDenied);
			EventClientUtility.getInstance(APP_ID).prepareAndSendJMSMessage(
					event);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}

	}

	public void prepareAndSendExpiredEvent() {
		try {
			LoanRequestExpiredEvent event = new LoanRequestExpiredEvent(this
					.getClass().getName(), "prepareLoanRequestExpiredEvent");
			event.setSubmissionId(this.submissionId);
			event.setContractId(this.contractId);
			event.setInitiator(this.eventInitiatorId);
			EventClientUtility.getInstance(APP_ID).prepareAndSendJMSMessage(
					event);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	public void prepareAndSendLoanPackageEvent(int participantProfileId) {
		try {
			LoanRequestLoanPackageEvent event = new LoanRequestLoanPackageEvent(
					this.getClass().getName(),
					"prepareLoanRequestLoanPackageEvent");
			event.setContractId(this.contractId);
			event.setSubmissionId(this.submissionId);
			event.setInitiator(this.eventInitiatorId);
			event.setParticipantProfileId(participantProfileId);
			EventClientUtility.getInstance(APP_ID).prepareAndSendJMSMessage(
					event);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	public void prepareAndSendPendingAcceptanceEvent(int participantProfileId) {
		try {
			LoanRequestPendingAcceptanceEvent event = new LoanRequestPendingAcceptanceEvent(
					this.getClass().getName(),
					"prepareLoanRequestPendingAcceptanceEvent");
			event.setContractId(this.contractId);
			event.setSubmissionId(this.submissionId);
			event.setInitiator(this.eventInitiatorId);
			event.setParticipantProfileId(participantProfileId);

			EventClientUtility.getInstance(APP_ID).prepareAndSendJMSMessage(
					event);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	public void prepareAndSendPendingApprovalEvent(int participantProfileId) {
		try {
			LoanRequestPendingApprovalEvent event = new LoanRequestPendingApprovalEvent(
					this.getClass().getName(),
					"prepareLoanRequestPendingApprovalEvent");
			event.setContractId(this.contractId);
			event.setSubmissionId(this.submissionId);
			event.setInitiator(this.eventInitiatorId);
			// For EZK set event initiator as 2
			if (APP_ID.equals(GlobalConstants.EZK_APPLICATION_ID)) {
				event.setInitiator(SystemUser.EZK.getProfileId());
			}
			event.setParticipantProfileId(participantProfileId);

			// If the Contract is a bundled contract and the contract TPA Firm
			// has Signing Authority then do not generate this message.
			if(!(isBundledIndicator() && getTpaFirmSigningAuthority().isSigningAuthority())){
				EventClientUtility.getInstance(APP_ID).prepareAndSendJMSMessage(
						event);
			}
			
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	public void prepareAndSendParticipantPendingApprovalEvent(
			int participantProfileId) {
		try {
			LoanRequestPendingApprovalEvent event = new LoanRequestPendingApprovalEvent(
					this.getClass().getName(),
					"prepareLoanRequestPendingApprovalEvent");
			event.setContractId(this.contractId);
			event.setSubmissionId(this.submissionId);
			event.setInitiator(SystemUser.EZK.getProfileId());
			event.setParticipantProfileId(participantProfileId);

			// If the Contract is a bundled contract and the contract TPA Firm
			// has Signing Authority then do not generate this message.
			if(!(isBundledIndicator() && getTpaFirmSigningAuthority().isSigningAuthority())){
				EventClientUtility.getInstance(APP_ID).prepareAndSendJMSMessage(
						event);
			}
			
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	public void prepareAndSendParticipantRejectedEvent(int participantProfileId) {
		try {
			LoanRequestRejectedEvent event = new LoanRequestRejectedEvent(this
					.getClass().getName(), "prepareLoanRequestRejectedEvent");
			event.setContractId(this.contractId);
			event.setSubmissionId(this.submissionId);
			event.setInitiator(SystemUser.EZK.getProfileId());
			event.setParticipantProfileId(participantProfileId);

			EventClientUtility.getInstance(APP_ID).prepareAndSendJMSMessage(
					event);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}

	}
	
	/**
	 * Identify the give contract is bundled or non bundled contract.
	 * 
	 * @return the bundledIndicator
	 */
	private boolean isBundledIndicator() throws SystemException {
		return ContractServiceDelegate.getInstance().isBundledGaContract(
				contractId);
	}
	
	/**
	 * Method to retrieving the contract permission.
	 * 
	 * @return the ContractPermission
	 */
	private ContractPermission getTpaFirmSigningAuthority() throws SystemException {
		return SecurityServiceDelegate.getInstance().getTpaFirmContractPermission(contractId);
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public int getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(int submissionId) {
		this.submissionId = submissionId;
	}

	public int getEventInitiatorId() {
		return eventInitiatorId;
	}

	public void setEventInitiatorId(int eventInitiatorId) {
		this.eventInitiatorId = eventInitiatorId;
	}

}
