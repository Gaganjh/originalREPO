package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

/**
 * Tests the simple withdrawal reason query.
 *
 * @author Andrew Dick
 */
public class TestSimpleWithdrawalReason {

    /**
     * Tests whether the disability withdrawal reason is simple.
     */
    @Test
    public void testDisability() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        assertTrue("Disability withdrawal reason should be simple.", request
                .getWithdrawalReasonSimple());
    }

    /**
     * Tests whether the hardship withdrawal reason is simple.
     */
    @Test
    public void testHardship() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        assertTrue("Hardship withdrawal reason should be simple.", request
                .getWithdrawalReasonSimple());
    }

    /**
     * Tests whether the EE rollover money withdrawal reason is simple.
     */
    @Test
    public void testEeRolloverMoney() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE);
        assertTrue("EE rollover money withdrawal reason should be simple.", request
                .getWithdrawalReasonSimple());
    }

    /**
     * Tests whether the minimum distribution withdrawal reason is simple.
     */
    @Test
    public void testMiniumDistribution() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MINIMUM_DISTRIBUTION_CODE);
        assertTrue("Minimum distribution withdrawal reason should be simple.", request
                .getWithdrawalReasonSimple());
    }

    /**
     * Tests whether the pre-retirement withdrawal withdrawal reason is simple.
     */
    @Test
    public void testPreRetirementWithdrawal() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE);
        assertTrue("Pre-retirement withdrawal withdrawal reason should be simple.", request
                .getWithdrawalReasonSimple());
    }

    /**
     * Tests whether the retirement withdrawal reason is simple.
     */
    @Test
    public void testRetirement() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        assertFalse("Retirement withdrawal reason should be simple.", request
                .getWithdrawalReasonSimple());
    }

    /**
     * Tests whether the termination of employment withdrawal reason is simple.
     */
    @Test
    public void testTerminationOfEmployment() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        assertFalse("Termination of employment withdrawal reason should be simple.", request
                .getWithdrawalReasonSimple());
    }

    /**
     * Tests whether the mandatory distribution term withdrawal reason is simple.
     */
    @Test
    public void testMandatoryDistributionTerm() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        assertFalse("Mandatory distribution term withdrawal reason should not be simple.", request
                .getWithdrawalReasonSimple());
    }

    /**
     * Tests whether the withdrawal of voluntary contributions withdrawal reason is simple.
     */
    @Test
    public void testWithdrawalOfVoluntaryContributions() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        assertTrue("Withdrawal of voluntary contributions withdrawal reason should be simple.",
                request.getWithdrawalReasonSimple());
    }

    /**
     * Creates a suite of Junit 4 tests.
     *
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestSimpleWithdrawalReason.class);
    }
}
