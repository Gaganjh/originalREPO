package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

public class TestWithdrawalRequestHaveStep1DriverFieldsChanged {

    private static final String STRING_VALUE = "Monty";

    private static final String NEW_STRING_VALUE = "Python";

    private static final Date DATE_VALUE = new Date();

    private static final Date NEW_DATE_VALUE = getPriorDate();

    private static final Date getPriorDate() {
        final Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testStateOfResidenceSameWithValue() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalParticipantStateOfResidence(STRING_VALUE);
        request.setParticipantStateOfResidence(STRING_VALUE);

        // Verify result
        assertFalse("Step 1 driver fields have not changed.", request
                .getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testStateOfResidenceSameWithNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalParticipantStateOfResidence(null);
        request.setParticipantStateOfResidence(null);

        // Verify result
        assertFalse("Step 1 driver fields have not changed.", request
                .getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testStateOfResidenceDifferentWithBothNonNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalParticipantStateOfResidence(STRING_VALUE);
        request.setParticipantStateOfResidence(NEW_STRING_VALUE);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testStateOfResidenceDifferentWithOriginalNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalParticipantStateOfResidence(null);
        request.setParticipantStateOfResidence(STRING_VALUE);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testStateOfResidenceDifferentWithCurrentNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalParticipantStateOfResidence(STRING_VALUE);
        request.setParticipantStateOfResidence(null);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testPaymentToSameWithValue() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalPaymentTo(STRING_VALUE);
        request.setPaymentTo(STRING_VALUE);

        // Verify result
        assertFalse("Step 1 driver fields have not changed.", request
                .getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testPaymentToSameWithNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalPaymentTo(null);
        request.setPaymentTo(null);

        // Verify result
        assertFalse("Step 1 driver fields have not changed.", request
                .getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testPaymentToDifferentWithBothNonNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalPaymentTo(STRING_VALUE);
        request.setPaymentTo(NEW_STRING_VALUE);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testPaymentToDifferentWithOriginalNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalPaymentTo(null);
        request.setPaymentTo(STRING_VALUE);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testPaymentToDifferentWithCurrentNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalPaymentTo(STRING_VALUE);
        request.setPaymentTo(null);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testReasonCodeSameWithValue() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalReasonCode(STRING_VALUE);
        request.setReasonCode(STRING_VALUE);

        // Verify result
        assertFalse("Step 1 driver fields have not changed.", request
                .getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testReasonCodeSameWithNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalReasonCode(null);
        request.setReasonCode(null);

        // Verify result
        assertFalse("Step 1 driver fields have not changed.", request
                .getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testReasonCodeDifferentWithBothNonNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalReasonCode(STRING_VALUE);
        request.setReasonCode(NEW_STRING_VALUE);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testReasonCodeDifferentWithOriginalNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalReasonCode(null);
        request.setReasonCode(STRING_VALUE);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testReasonCodeDifferentWithCurrentNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalReasonCode(STRING_VALUE);
        request.setReasonCode(null);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testBirthDateSameWithValue() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalBirthDate(DATE_VALUE);
        request.setBirthDate(DATE_VALUE);

        // Verify result
        assertFalse("Step 1 driver fields have not changed.", request
                .getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testBirthDateSameWithNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalBirthDate(null);
        request.setBirthDate(null);

        // Verify result
        assertFalse("Step 1 driver fields have not changed.", request
                .getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testBirthDateDifferentWithBothNonNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalBirthDate(DATE_VALUE);
        request.setBirthDate(NEW_DATE_VALUE);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testBirthDateDifferentWithOriginalNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalBirthDate(null);
        request.setBirthDate(DATE_VALUE);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testBirthDateDifferentWithCurrentNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalBirthDate(DATE_VALUE);
        request.setBirthDate(null);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testParticipantLeavingPlanIndicatorSameWithValue() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalParticipantLeavingPlanInd(Boolean.TRUE);
        request.setParticipantLeavingPlanInd(Boolean.TRUE);

        // Verify result
        assertFalse("Step 1 driver fields have not changed.", request
                .getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testParticipantLeavingPlanIndicatorSameWithNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalParticipantLeavingPlanInd(null);
        request.setParticipantLeavingPlanInd(null);

        // Verify result
        assertFalse("Step 1 driver fields have not changed.", request
                .getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testParticipantLeavingPlanIndicatorDifferentWithBothNonNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalParticipantLeavingPlanInd(Boolean.TRUE);
        request.setParticipantLeavingPlanInd(Boolean.FALSE);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testParticipantLeavingPlanIndicatorDifferentWithOriginalNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalParticipantLeavingPlanInd(null);
        request.setParticipantLeavingPlanInd(Boolean.TRUE);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Tests the step 1 driver field change query.
     */
    @Test
    public void testParticipantLeavingPlanIndicatorDifferentWithCurrentNull() {

        // Set up values
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setOriginalParticipantLeavingPlanInd(Boolean.TRUE);
        request.setParticipantLeavingPlanInd(null);

        // Verify result
        assertTrue("Step 1 driver fields have changed.", request.getHaveStep1DriverFieldsChanged());
    }

    /**
     * Creates a suite of Junit 4 tests.
     * 
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestWithdrawalRequestHaveStep1DriverFieldsChanged.class);
    }
}
