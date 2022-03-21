package com.manulife.pension.service.loan.domain;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import com.manulife.pension.service.distribution.DistributionContainerEnvironment;
import com.manulife.pension.service.employee.EmployeeConstants.ParticipantStatus;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.loan.valueobject.WebLoanSupportDataRetriever;

public class TestDefaultState extends DistributionContainerEnvironment {

	private Loan loan;
	private LoanSettings loanSettings;
	private LoanParticipantData loanParticipantData;
	private LoanPlanData loanPlanData;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		loan = new Loan();
		loan.setDataRetriever(new WebLoanSupportDataRetriever());
		loanSettings = new LoanSettings();
		loanParticipantData = new LoanParticipantData();
		loanPlanData = new LoanPlanData();
	}

	public void testOnlineLoans() throws Exception {
		loanSettings.setLrk01(true);
		loanSettings.setAllowOnlineLoans(true);
		assertCondition(false, "Test allow online loan flag failed", loan,
				LoanErrorCode.ALLOW_ONLINE_LOANS_IS_OFF);
	}

	public void testNoOnlineLoans() throws Exception {
		loanSettings.setLrk01(true);
		loanSettings.setAllowOnlineLoans(false);
		assertCondition(true, "Test allow online loan flag failed", loan,
				LoanErrorCode.ALLOW_ONLINE_LOANS_IS_OFF);
	}

	public void testOnlineLoans2() throws Exception {
		loanSettings.setLrk01(false);
		loanSettings.setAllowOnlineLoans(true);
		assertCondition(false, "Test allow online loan flag failed", loan,
				LoanErrorCode.ALLOW_ONLINE_LOANS_IS_OFF);
	}

	public void testNoOnlineLoans2() throws Exception {
		loanSettings.setLrk01(false);
		loanSettings.setAllowOnlineLoans(false);
		assertCondition(false, "Test allow online loan flag failed", loan,
				LoanErrorCode.ALLOW_ONLINE_LOANS_IS_OFF);
	}

	public void testLrk01() throws Exception {
		loanSettings.setLrk01(true);
		assertCondition(false, "Test LRK01 failed", loan,
				LoanErrorCode.LRK01_IS_OFF);
	}

	public void testNoLrk01() throws Exception {
		loanSettings.setLrk01(false);
		assertCondition(true, "Test LRK01 failed", loan,
				LoanErrorCode.LRK01_IS_OFF);
	}

	public void testParticipantNumberOfLoansNotExceeded() throws Exception {
		loanPlanData.setMaxNumberOfOutstandingLoans(3);
		loanParticipantData.setOutstandingLoansCount(0);
		assertCondition(false, "Test loans exceeded failed", loan,
				LoanErrorCode.PARTICIPANT_NUMBER_OF_LOANS_EXCEEDED);
	}

	public void testParticipantNumberOfLoansExceeded() throws Exception {
		loanPlanData.setMaxNumberOfOutstandingLoans(0);
		loanParticipantData.setOutstandingLoansCount(3);
		assertCondition(true, "Test loans exceeded failed", loan,
				LoanErrorCode.PARTICIPANT_NUMBER_OF_LOANS_EXCEEDED);
	}

	public void testParticipantCurrentAccountBalanceIsNotZero()
			throws Exception {
		LoanMoneyType moneyType = new LoanMoneyType();
		moneyType.setAccountBalance(BigDecimal.ONE);
		loan.getMoneyTypes().add(moneyType);
		assertCondition(false, "Test account balance is zero failed", loan,
				LoanErrorCode.PARTICIPANT_CURRENT_ACCOUNT_BALANCE_IS_ZERO);
	}

	public void testParticipantCurrentBalanceIsZero() throws Exception {
		LoanMoneyType moneyType = new LoanMoneyType();
		moneyType.setAccountBalance(BigDecimal.ZERO);
		loan.getMoneyTypes().add(moneyType);
		assertCondition(true, "Test account balance is zero failed", loan,
				LoanErrorCode.PARTICIPANT_CURRENT_ACCOUNT_BALANCE_IS_ZERO);
	}

	public void testParticipantStatusActive() throws Exception {
		loanParticipantData.setParticipantStatusCode(ParticipantStatus.ACTIVE);
		assertCondition(false, "Test participant status failed", loan,
				LoanErrorCode.PARTICIPANT_STATUS_IS_NOT_ACTIVE);
	}

	public void testParticipantStatusNotActive() throws Exception {
		loanParticipantData
				.setParticipantStatusCode(ParticipantStatus.PARTIAL_DEATH);
		assertCondition(true, "Test participant status failed", loan,
				LoanErrorCode.PARTICIPANT_STATUS_IS_NOT_ACTIVE);
	}

	public void testParticipantHasNoPbaBalance() throws Exception {
		loanParticipantData.setPositivePbaMoneyTypeBalance(false);
		assertCondition(false, "Test participant PBA balance failed", loan,
				LoanErrorCode.PARTICIPANT_HAS_POSITIVE_PBA_BALANCE);
	}

	public void testParticipantHasPbaBalance() throws Exception {
		loanParticipantData.setPositivePbaMoneyTypeBalance(true);
		assertCondition(true, "Test participant PBA balance failed", loan,
				LoanErrorCode.PARTICIPANT_HAS_POSITIVE_PBA_BALANCE);
	}

	public void testParticipantHasNoForwardUnreversedLI() throws Exception {
		loanParticipantData.setForwardUnreversedLoanTransactionExist(false);
		assertCondition(false,
				"Test participant has forward unreversed LI failed", loan,
				LoanErrorCode.FORWARD_UNREVERSED_LOAN_TRANSACTION_EXISTS);
	}

	public void testParticipantHasForwardUnreversedLI() throws Exception {
		loanParticipantData.setForwardUnreversedLoanTransactionExist(true);
		assertCondition(true,
				"Test participant has forward unreversed LI failed", loan,
				LoanErrorCode.FORWARD_UNREVERSED_LOAN_TRANSACTION_EXISTS);
	}

	public void testParticipantHasNoGifl() throws Exception {
		loanParticipantData.setGiflFeatureSelected(false);
		String savedRoleCode = loan.getCreatedByRoleCode();
		loan.setCreatedByRoleCode(LoanConstants.USER_ROLE_PARTICIPANT_CODE);
		assertCondition(false, "Test participant has no GIFL for participant initiated loan failed", loan,
				LoanErrorCode.PARTICIPANT_HAS_GIFL_FOR_PARTICIPANT_INITIATED);
        loan.setCreatedByRoleCode(LoanConstants.USER_ROLE_PLAN_SPONSOR_CODE);
        assertCondition(false, "Test participant has no GIFL for external user initiated loan failed", loan,
                LoanErrorCode.PARTICIPANT_HAS_GIFL_FOR_EXTERNAL_USER_INITIATED);
		loan.setCreatedByRoleCode(savedRoleCode);
	}

	public void testParticipantHasGifl() throws Exception {
		loanParticipantData.setGiflFeatureSelected(true);
        String savedRoleCode = loan.getCreatedByRoleCode();
        loan.setCreatedByRoleCode(LoanConstants.USER_ROLE_PARTICIPANT_CODE);
		assertCondition(true, "Test participant has GIFL for participant initiated loan failed", loan,
				LoanErrorCode.PARTICIPANT_HAS_GIFL_FOR_PARTICIPANT_INITIATED);
        loan.setCreatedByRoleCode(LoanConstants.USER_ROLE_PLAN_SPONSOR_CODE);
        assertCondition(true, "Test participant has no GIFL for external user initiated loan failed", loan,
                LoanErrorCode.PARTICIPANT_HAS_GIFL_FOR_EXTERNAL_USER_INITIATED);
        loan.setCreatedByRoleCode(savedRoleCode);
	}

	public void testParticipantHasNoWithdrawalRequest() throws Exception {
		loanParticipantData.setPendingWithdrawalRequestExist(false);
		assertCondition(false, "Test participant has withdrawal failed", loan,
				LoanErrorCode.PARTICIPANT_HAS_WITHDRAWAL_REQUEST);
	}

	public void testParticipantHasWithdrawalRequest() throws Exception {
		loanParticipantData.setPendingWithdrawalRequestExist(true);
		assertCondition(true, "Test participant has withdrawal failed", loan,
				LoanErrorCode.PARTICIPANT_HAS_WITHDRAWAL_REQUEST);
	}

	public void testLoanAboutToExpire() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 2);
		loan.setExpirationDate(calendar.getTime());
		assertCondition(true, "Test loan about to expire failed", loan,
				LoanErrorCode.LOAN_ABOUT_TO_EXPIRE);
	}

	public void testLoanNotAboutToExpire() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 4);
		loan.setExpirationDate(calendar.getTime());
		assertCondition(false, "Test loan about to expire failed", loan,
				LoanErrorCode.LOAN_ABOUT_TO_EXPIRE);
	}

	private void assertCondition(boolean hasErrorCode, String message,
			Loan loan, LoanErrorCode loanErrorCode) throws Exception {
		LoanState state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
		LoanStateContext context = new LoanStateContext(loan);
		context.setLoan(loan);
		loan.setLoanSettings(loanSettings);
		loan.setLoanParticipantData(loanParticipantData);
		loan.setLoanPlanData(loanPlanData);
		
		((DefaultLoanState) state).populateMessages(context);
		if (hasErrorCode) {
			Assert.assertTrue(message, hasLoanErrorCode(loan.getMessages(),
					loanErrorCode));
		} else {
			Assert.assertTrue(message, !hasLoanErrorCode(loan.getMessages(),
					loanErrorCode));
		}
	}

	private boolean hasLoanErrorCode(List<LoanMessage> messages,
			LoanErrorCode loanErrorCode) {
		for (LoanMessage message : messages) {
			if (message.getErrorCode().equals(loanErrorCode)) {
				return true;
			}
		}
		return false;
	}

}
