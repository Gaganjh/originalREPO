package com.manulife.pension.service.withdrawal.valueobject;

import junit.framework.JUnit4TestAdapter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests the has reason code changed query.
 *
 * @author Andrew Dick
 */
public class TestWithdrawalReason {

    /**
     * Tests the has withdrawal reason code changed when the original code is null.
     */
    @Test
    public void testHasReasonCodeChangedWithOriginalNull() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalReasonCode(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);

        assertTrue("Expected reason code to be changed.", request.hasReasonCodeChanged());
    }

    /**
     * Tests the has withdrawal reason code changed when the reason code is null.
     */
    @Test
    public void testHasReasonCodeChangedWithReasonNull() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setReasonCode(null);

        assertTrue("Expected reason code to be changed.", request.hasReasonCodeChanged());
    }

    /**
     * Tests the has withdrawal reason code changed when the original and reason code are null.
     */
    @Test
    public void testHasReasonCodeChangedWithOriginalAndReasonNull() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalReasonCode(null);
        request.setReasonCode(null);

        assertFalse("Expected reason code to not be changed.", request.hasReasonCodeChanged());
    }

    /**
     * Tests the has withdrawal reason code changed when the original and reason code are the same.
     */
    @Test
    public void testHasReasonCodeChangedWithSameOriginalAndReason() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);

        assertFalse("Expected reason code to not be changed.", request.hasReasonCodeChanged());
    }

    /**
     * Tests the has withdrawal reason code changed when the original and reason code are the same.
     */
    @Test
    public void testHasReasonCodeChangedWithDifferentOriginalAndReason() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);

        assertTrue("Expected reason code to be changed.", request.hasReasonCodeChanged());
    }

    /**
     * Tests the has withdrawal payment to code changed when the original code is null.
     */
    @Test
    public void testHasPaymentToCodeChangedWithOriginalNull() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalPaymentTo(null);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);

        assertTrue("Expected payment to code to be changed.", request.hasPaymentToChanged());
    }

    /**
     * Tests the has withdrawal payment to code changed when the payment to code is null.
     */
    @Test
    public void testHasPaymentToCodeChangedWithReasonNull() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setPaymentTo(null);

        assertTrue("Expected payment to code to be changed.", request.hasPaymentToChanged());
    }

    /**
     * Tests the has withdrawal payment to code changed when the original and payment to code are
     * null.
     */
    @Test
    public void testHasPaymentToCodeChangedWithOriginalAndReasonNull() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalPaymentTo(null);
        request.setPaymentTo(null);

        assertFalse("Expected payment to code to not be changed.", request.hasPaymentToChanged());
    }

    /**
     * Tests the has withdrawal payment to code changed when the original and payment to code are
     * the same.
     */
    @Test
    public void testHasPaymentToCodeChangedWithSameOriginalAndReason() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);

        assertFalse("Expected payment to code to not be changed.", request.hasPaymentToChanged());
    }

    /**
     * Tests the has withdrawal payment to code changed when the original and payment to code are
     * the same.
     */
    @Test
    public void testHasPaymentToCodeChangedWithDifferentOriginalAndReason() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);

        assertTrue("Expected payment to code to be changed.", request.hasPaymentToChanged());
    }

    /**
     * Creates a suite of Junit 4 tests.
     *
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestWithdrawalReason.class);
    }
}
