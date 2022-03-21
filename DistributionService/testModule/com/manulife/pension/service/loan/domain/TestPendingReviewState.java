package com.manulife.pension.service.loan.domain;

import java.math.BigDecimal;

import org.junit.Assert;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.valueobject.Loan;

public class TestPendingReviewState extends LoanStateBase {

	private Integer contractId = 11847;
	private Integer participantProfileId = 130747405;
	private Integer reviewerProfileId = 1631092; // TPA

	//Constants for Bundled Contracts
	private Integer bgaContractId = 10007;
	private Integer bgaContractParticipantProfileId = 100004464;
	private Integer bgaCARUserProfileId = 8026064; //BGARep01
	private Integer bgaApproverUserProfileId = 8026007; //BGAppr01

    public void setUp() throws Exception {
        super.setUp();
        insertOnlineLoanCSF(contractId);
        expireAllLoanRequestsForParticipant(contractId, participantProfileId);
    }    
	public void testParticipantSendForAcceptance() throws Exception {
		Loan loan = initiate(participantProfileId, contractId,
				participantProfileId, LoanConstants.USER_ROLE_PARTICIPANT_CODE);
		LoanState state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
		loan.getMessages().clear();
		loan.setLoanReason("To buy a car");
		loan.getOriginalParameter().setLoanAmount(new BigDecimal(2000));
		loan.getOriginalParameter().setPaymentFrequency(
				GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY);
		loan.getOriginalParameter().setInterestRate(new BigDecimal(12.5));
		loan = state.sendForReview(loan);
        discard(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {});
		Assert.assertNotNull(loan.getSubmissionId());

		/*
		 * After the loan is successfully sent for review, we send it for
		 * acceptance.
		 */ 
		 
		state = LoanStateFactory.getState(LoanStateEnum.PENDING_REVIEW);
		loan.setLoginUserProfileId(reviewerProfileId);
		loan.setStatus(LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode());
		loan.getReviewedParameter().setLoanAmount(new BigDecimal(1500));
		loan = state.sendForAcceptance(loan);

		assertLoanMessages(loan, new LoanErrorCode[] {});
	}
	
	/**
	 * This method tests for acceptance of loan request by the participant for BGA contract
	 * @throws Exception
	 */
	public void testParticipantSendForAcceptanceBGAContract() throws Exception {
		
		Loan loan = initiate(bgaContractParticipantProfileId, bgaContractId,
				bgaCARUserProfileId, LoanConstants.USER_ROLE_JH_BUNDLED_GA_CODE);
		LoanState state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
		loan.getMessages().clear();
		loan.setLoanReason("To buy a car");
		loan.getOriginalParameter().setLoanAmount(new BigDecimal(2000));
		loan.getOriginalParameter().setPaymentFrequency(
				GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY);
		loan.getOriginalParameter().setInterestRate(new BigDecimal(12.5));
		loan = state.sendForReview(loan);
        discard(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {});
		Assert.assertNotNull(loan.getSubmissionId());

		/*
		 * After the loan is successfully sent for review, we send it for
		 * acceptance.
		 */
		state = LoanStateFactory.getState(LoanStateEnum.PENDING_REVIEW);
		loan.setLoginUserProfileId(bgaApproverUserProfileId);
		loan.setStatus(LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode());
		loan.getReviewedParameter().setLoanAmount(new BigDecimal(1500));
		loan = state.sendForAcceptance(loan);

		assertLoanMessages(loan, new LoanErrorCode[] {});
	}
}
