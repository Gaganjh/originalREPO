package com.manulife.pension.service.loan.event;

/**
 * Interface that provides services to prepare and trigger loan related events 
 */

public interface LoanEventGenerator {

	/**
	 * prepares and trigger LoanRequestPendingReview event
	 * { event 60 } 
	 * @param participantProfileId
	 */
	public void prepareAndSendPendingReviewEvent(int participantProfileId) ;

	/**
	 * prepares and trigger LoanRequestPendingAcceptance event
	 * { event 61 } 
	 * @param participantProfileId
	 */
	public void prepareAndSendPendingAcceptanceEvent(int participantProfileId) ;

	/**
	 * prepares and trigger LoanRequestPendingApproval event
	 * { event 62 } 
	 * @param participantProfileId
	 */
	public void prepareAndSendPendingApprovalEvent(int participantProfileId) ;

	/**
	 * prepares and trigger LoanRequestApproved event
	 * { event 63 } 
	 * @param participantProfileId
	 * @param approverRoleCode
	 * @param lastTPAWhoChangedFees
	 * @param lastFeeChangedWasPlanSponsorUserInd
	 */
	public void prepareAndSendApprovedEvent(int participantProfileId, String approverRoleCode, Boolean lastFeeChangedWasPlanSponsorUserInd) ;
	
	/**
	 * prepares and trigger LoanRequestDenied event
	 * { event 64 } 
	 * @param participantProfileId
	 * @param loanStatusBeforeDenied
	 */
	public void prepareAndSendDeniedEvent(int participantProfileId, String loanStatusBeforeDenied);

	/**
	 * prepares and trigger LoanRequestAboutToExpire event
	 * { event 68 } 
	 * @param participantProfileId
	 * @param loanStatus
	 * @param daysToExpire
	 */
	public void prepareAndSendAboutToExpireEvent(int participantProfileId, String loanStatus, int daysToExpire);

	/**
	 * prepares and trigger LoanRequestExpired event
	 * { event 65 } 
	 */
	public void prepareAndSendExpiredEvent() ;

	/**
	 * prepares and trigger LoanRequestRejected event
	 * { event 67 } 
	 * @param participantProfileId
	 */
	public void prepareAndSendParticipantRejectedEvent(int participantProfileId);

	/**
	 * prepares and trigger LoanRequestDeleted event
	 * { event 66 } 
	 * @param participantProfileId,loanStatusBeforeDeleted
	 */
	public void prepareAndSendDeletedEvent(int participantProfileId, String loanStatusBeforeDeleted );

	/**
	 * prepares and trigger LoanRequestLoanPackage event
	 * { event 69 } 
	 * @param participantProfileId
	 */
	public void prepareAndSendLoanPackageEvent(int participantProfileId);
	
	/**
	 * prepares and trigger LoanRequestPendingReview event for participant
	 * { event 60 } 
	 * @param participantProfileId
	 */
	public void prepareAndSendParticipantPendingReviewEvent(int participantProfileId);

	/**
	 * prepares and trigger LoanRequestPendingApproval event for participant
	 * { event 62 } 
	 * @param participantProfileId
	 */
	public void prepareAndSendParticipantPendingApprovalEvent(int participantProfileId); 
	

}
