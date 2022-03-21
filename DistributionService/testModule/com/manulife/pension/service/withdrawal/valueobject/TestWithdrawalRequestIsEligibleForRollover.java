package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * Tests the is eligible for rollover query.
 * 
 * @author dickand
 */
public class TestWithdrawalRequestIsEligibleForRollover {

    /**
     * Tests whether the disability withdrawal reason is simple.
     */
    @Test
    public void testDisability() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        assertTrue("Disability is not eligible for rollover.", request.isEligibleForRollover());
    }

    /**
     * Tests whether the hardship withdrawal reason is simple.
     */
    @Test
    public void testHardship() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        assertFalse("Hardship is not eligible for rollover.", request.isEligibleForRollover());
    }

    /**
     * Tests whether the EE rollover money withdrawal reason is simple.
     */
    @Test
    public void testEeRolloverMoney() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE);
        assertTrue("EE rollover money is not eligible for rollover.", request
                .isEligibleForRollover());
    }

    /**
     * Tests whether the minimum distribution withdrawal reason is simple.
     */
    @Test
    public void testMiniumDistribution() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MINIMUM_DISTRIBUTION_CODE);
        assertFalse("Minimum distribution is not eligible for rollover.", request
                .isEligibleForRollover());
    }

    /**
     * Tests whether the pre-retirement withdrawal withdrawal reason is simple.
     */
    @Test
    public void testPreRetirementWithdrawal() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE);
        assertTrue("Pre-retirement is not eligible for rollover.", request.isEligibleForRollover());
    }

    /**
     * Tests whether the retirement withdrawal reason is simple.
     */
    @Test
    public void testRetirement() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        assertTrue("Retirement is not eligible for rollover.", request.isEligibleForRollover());
    }

    /**
     * Tests whether the termination of employment withdrawal reason is simple.
     */
    @Test
    public void testTerminationOfEmployment() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        assertTrue("Termination of employment is not eligible for rollover.", request
                .isEligibleForRollover());
    }

    /**
     * Tests whether the mandatory distribution term withdrawal reason is simple.
     */
    @Test
    public void testMandatoryDistributionTerm() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        assertTrue("Mandatory distribution term is not eligible for rollover.", request
                .isEligibleForRollover());
    }

    /**
     * Tests whether the withdrawal of voluntary contributions withdrawal reason is simple.
     */
    @Test
    public void testWithdrawalOfVoluntaryContributions() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        assertTrue("Voluntary contribution is not eligible for rollover.", request
                .isEligibleForRollover());
    }

    /**
     * Tests the eligible for rollover query.
     */
    @Test
    public void testNullWithdrawalReason() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(null);
        assertTrue("Reason code is null.", request.isEligibleForRollover());
    }

    /**
     * Tests the eligible for rollover query.
     */
    @Test
    public void testBlankWithdrawalReason() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReasonCode(StringUtils.EMPTY);
        assertTrue("Reason code is blank.", request.isEligibleForRollover());
    }
}
