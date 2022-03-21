package com.manulife.pension.service.loan.domain;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.dao.LoanSupportDao;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;

public class TestDraftState extends LoanStateBase {

	private Integer contractId = 11847;
    private Integer participantProfileId = 130747405;
    
    //For Bundled GA
    private Integer bgaContractId = 10007;
    private Integer bgaContractParticipantProfileId = 150585925;
    private Integer bgaCARUserProfileId = 8026064; //BGARep01
    private Integer bgaApproverUserProfileId = 8026007; //BGAppr01

    public void setUp() throws Exception {
        super.setUp();
        insertOnlineLoanCSF(contractId);
        expireAllLoanRequestsForParticipant(contractId, participantProfileId);
        
        //For Bundled GA
        insertOnlineLoanCSF(bgaContractId);
        expireAllLoanRequestsForParticipant(bgaContractId, bgaContractParticipantProfileId);
    }    

	public void testParticipantInitiate() throws Exception {
		Loan loan = initiate(participantProfileId, contractId,
				participantProfileId, LoanConstants.USER_ROLE_PARTICIPANT_CODE);
        loan.getOriginalParameter().setLoanAmount(new BigDecimal(501));
        loan.getOriginalParameter().setPaymentFrequency(
                GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY);
        loan.getOriginalParameter().setInterestRate(new BigDecimal(10.25));
        
		LoanSupportDao supportDao = new LoanSupportDao(BaseDatabaseDAO
				.getDataSource(BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME));

		List<LoanMoneyType> participantMoneyTypes = supportDao
				.getParticipantMoneyTypesForLoans(contractId,
						participantProfileId);

		for (LoanMoneyType participantMoneyType : participantMoneyTypes) {
			boolean found = false;
			for (LoanMoneyType loanMoneyType : loan.getMoneyTypes()) {
				if (loanMoneyType.getMoneyTypeId().equals(
						participantMoneyType.getMoneyTypeId())) {
					found = true;
					break;
				}
			}
			Assert.assertTrue("Money type ["
					+ participantMoneyType.getMoneyTypeId() + "] not found",
					found);
		}

		LoanState state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
		loan = state.saveAndExit(loan);
        discard(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {});
		Assert.assertNotNull(loan.getSubmissionId());
	}

	public void testParticipantSendForReview() throws Exception {
		Loan loan = initiate(participantProfileId, contractId,
				participantProfileId, LoanConstants.USER_ROLE_PARTICIPANT_CODE);
		LoanState state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
		loan = state.sendForReview(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {
				LoanErrorCode.MUST_ENTER_LOAN_REASON,
				LoanErrorCode.LOAN_AMOUNT_BLANK_OR_NON_NUMERIC,
				LoanErrorCode.PAYMENT_FREQUENCY_IS_EMPTY,
				LoanErrorCode.INTEREST_RATE_BLANK_OR_NON_NUMERIC});
		loan.getErrors().clear();
		loan.getMessages().clear();
		loan.setLoanReason("To buy a car");
		loan.getOriginalParameter().setLoanAmount(new BigDecimal(2000));
		loan.getOriginalParameter().setPaymentFrequency(
				GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY);
		loan.getOriginalParameter().setInterestRate(new BigDecimal(12.5));
		loan = state.sendForReview(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {});
		Assert.assertNotNull(loan.getSubmissionId());
		discard(loan);
	}
	
	public void testBundledGaInitiate() throws Exception {
		Loan loan = initiate(bgaContractParticipantProfileId, bgaContractId,
				bgaCARUserProfileId, LoanConstants.USER_ROLE_JH_BUNDLED_GA_CODE);
        loan.getOriginalParameter().setLoanAmount(new BigDecimal(15));
        loan.getOriginalParameter().setPaymentFrequency(
                GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY);
        loan.getOriginalParameter().setInterestRate(new BigDecimal(1.25));
        
		LoanSupportDao supportDao = new LoanSupportDao(BaseDatabaseDAO
				.getDataSource(BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME));

		List<LoanMoneyType> participantMoneyTypes = supportDao
				.getParticipantMoneyTypesForLoans(bgaContractId,
						bgaContractParticipantProfileId);

		for (LoanMoneyType participantMoneyType : participantMoneyTypes) {
			boolean found = false;
			for (LoanMoneyType loanMoneyType : loan.getMoneyTypes()) {
				if (loanMoneyType.getMoneyTypeId().equals(
						participantMoneyType.getMoneyTypeId())) {
					found = true;
					break;
				}
			}
			Assert.assertTrue("Money type ["
					+ participantMoneyType.getMoneyTypeId() + "] not found",
					found);
		}

		LoanState state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
		loan = state.saveAndExit(loan);
        discard(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {});
		Assert.assertNotNull(loan.getSubmissionId());
	}
	
	public void testBundledGaSendForReview() throws Exception {
		Loan loan = initiate(bgaContractParticipantProfileId, bgaContractId,
				bgaCARUserProfileId, LoanConstants.USER_ROLE_JH_BUNDLED_GA_CODE);
		LoanState state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
		loan = state.sendForReview(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {
				LoanErrorCode.MUST_ENTER_LOAN_REASON,
				LoanErrorCode.LOAN_AMOUNT_BLANK_OR_NON_NUMERIC,
				LoanErrorCode.PAYMENT_FREQUENCY_IS_EMPTY,
				LoanErrorCode.INTEREST_RATE_BLANK_OR_NON_NUMERIC});
		loan.getErrors().clear();
		loan.getMessages().clear();
		loan.setLoanReason("To buy a car");
		loan.getOriginalParameter().setLoanAmount(new BigDecimal(15));
		loan.getOriginalParameter().setPaymentFrequency(
				GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY);
		loan.getOriginalParameter().setInterestRate(new BigDecimal(1.25));
		loan = state.sendForReview(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {});
		Assert.assertNotNull(loan.getSubmissionId());
		discard(loan);
	}
	
	public void testBundledGaSendForApprover() throws Exception {
		Loan loan = initiate(bgaContractParticipantProfileId, bgaContractId,
				bgaApproverUserProfileId, LoanConstants.USER_ROLE_JH_BUNDLED_GA_CODE);
		LoanState state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
		
		Calendar firstPayrollDate = Calendar.getInstance();
		firstPayrollDate.add(Calendar.MONTH, 1);
		
		loan.setFirstPayrollDate(firstPayrollDate.getTime());
		loan = state.sendForReview(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {
				LoanErrorCode.MUST_ENTER_LOAN_REASON,
				LoanErrorCode.LOAN_AMOUNT_BLANK_OR_NON_NUMERIC,
				LoanErrorCode.PAYMENT_FREQUENCY_IS_EMPTY,
				LoanErrorCode.INTEREST_RATE_BLANK_OR_NON_NUMERIC});
		loan.getErrors().clear();
		loan.getMessages().clear();
		loan.setLoanReason("To buy a car");
		loan.getOriginalParameter().setLoanAmount(new BigDecimal(15));
		loan.getOriginalParameter().setPaymentFrequency(
				GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY);
		loan.getOriginalParameter().setInterestRate(new BigDecimal(1.25));
		loan = state.sendForApproval(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {});
		Assert.assertNotNull(loan.getSubmissionId());
		discard(loan);
	}

}
