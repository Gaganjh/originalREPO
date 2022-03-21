package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;

/**
 * Tests the validations for the IRS Code for Withdrawal.
 * 
 * @author dickand
 */
public class TestWithdrawalIrsCodeForLoan {

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);

        return request;
    }

    /**
     * Retrieves a current date with the time components cleared.
     */
    private Date getTimeFreeDate() {
        return DateUtils.truncate(new Date(), Calendar.DATE);
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsDirectToParticipantAndLoanOptionIsCloseAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsDirectToParticipantAndLoanOptionIsCloseAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsDirectToParticipantAndLoanOptionIsRolloverAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsDirectToParticipantAndLoanOptionIsRolloverAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsDirectToParticipantAndLoanOptionIsRepayAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsDirectToParticipantAndLoanOptionIsRepayAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsDirectToParticipantAndLoanOptionIsKeepAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsDirectToParticipantAndLoanOptionIsKeepAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsDirectToParticipantAndLoanOptionIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsDirectToParticipantAndLoanOptionIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsDirectToParticipantAndLoanOptionIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsDirectToParticipantAndLoanOptionIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToPlanAndLoanOptionIsCloseAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToPlanAndLoanOptionIsCloseAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToPlanAndLoanOptionIsRolloverAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToPlanAndLoanOptionIsRolloverAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToPlanAndLoanOptionIsRepayAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToPlanAndLoanOptionIsRepayAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToPlanAndLoanOptionIsKeepAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToPlanAndLoanOptionIsKeepAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToPlanAndLoanOptionIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToPlanAndLoanOptionIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToPlanAndLoanOptionIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToPlanAndLoanOptionIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToIraAndLoanOptionIsCloseAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToIraAndLoanOptionIsCloseAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToIraAndLoanOptionIsRolloverAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToIraAndLoanOptionIsRolloverAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToIraAndLoanOptionIsRepayAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToIraAndLoanOptionIsRepayAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToIraAndLoanOptionIsKeepAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToIraAndLoanOptionIsKeepAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToIraAndLoanOptionIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToIraAndLoanOptionIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToIraAndLoanOptionIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToIraAndLoanOptionIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsCloseAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsCloseAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsRolloverAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsRolloverAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsRepayAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsRepayAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsKeepAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsKeepAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsCloseAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsCloseAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsRolloverAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsRolloverAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsRepayAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsRepayAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsKeepAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsKeepAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsPlanTrusteeAndLoanOptionIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsPlanTrusteeAndLoanOptionIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsBlankAndLoanOptionIsCloseAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsBlankAndLoanOptionIsCloseAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsBlankAndLoanOptionIsRolloverAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsBlankAndLoanOptionIsRolloverAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsBlankAndLoanOptionIsRepayAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsBlankAndLoanOptionIsRepayAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsBlankAndLoanOptionIsKeepAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsBlankAndLoanOptionIsKeepAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsBlankAndLoanOptionIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsBlankAndLoanOptionIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsBlankAndLoanOptionIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsBlankAndLoanOptionIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsDirectToParticipantAndLoanOptionIsCloseAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsDirectToParticipantAndLoanOptionIsCloseAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsDirectToParticipantAndLoanOptionIsRolloverAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsDirectToParticipantAndLoanOptionIsRolloverAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsDirectToParticipantAndLoanOptionIsRepayAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsDirectToParticipantAndLoanOptionIsRepayAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsDirectToParticipantAndLoanOptionIsKeepAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsDirectToParticipantAndLoanOptionIsKeepAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsDirectToParticipantAndLoanOptionIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsDirectToParticipantAndLoanOptionIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsDirectToParticipantAndLoanOptionIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsDirectToParticipantAndLoanOptionIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToPlanAndLoanOptionIsCloseAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToPlanAndLoanOptionIsCloseAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToPlanAndLoanOptionIsRolloverAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToPlanAndLoanOptionIsRolloverAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToPlanAndLoanOptionIsRepayAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToPlanAndLoanOptionIsRepayAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToPlanAndLoanOptionIsKeepAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToPlanAndLoanOptionIsKeepAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToPlanAndLoanOptionIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToPlanAndLoanOptionIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToPlanAndLoanOptionIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToPlanAndLoanOptionIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToIraAndLoanOptionIsCloseAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToIraAndLoanOptionIsCloseAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToIraAndLoanOptionIsRolloverAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToIraAndLoanOptionIsRolloverAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToIraAndLoanOptionIsRepayAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToIraAndLoanOptionIsRepayAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToIraAndLoanOptionIsKeepAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToIraAndLoanOptionIsKeepAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToIraAndLoanOptionIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToIraAndLoanOptionIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToIraAndLoanOptionIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToIraAndLoanOptionIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsCloseAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsCloseAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsRolloverAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsRolloverAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsRepayAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsRepayAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsKeepAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsKeepAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToPlanAndLoanOptionIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsCloseAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsCloseAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsRolloverAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsRolloverAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsRepayAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsRepayAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsKeepAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsKeepAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToIraAndLoanOptionIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsPlanTrusteeAndLoanOptionIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsPlanTrusteeAndLoanOptionIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsBlankAndLoanOptionIsCloseAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsBlankAndLoanOptionIsCloseAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsBlankAndLoanOptionIsRolloverAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsBlankAndLoanOptionIsRolloverAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsBlankAndLoanOptionIsRepayAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsBlankAndLoanOptionIsRepayAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsBlankAndLoanOptionIsKeepAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsBlankAndLoanOptionIsKeepAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsBlankAndLoanOptionIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsBlankAndLoanOptionIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsBlankAndLoanOptionIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);

        // Should be one error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsBlankAndLoanOptionIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        request.setLoanOption(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation
        withdrawal.validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_NORMAL).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_NORMAL)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER).append("].")
                        .toString(), CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER).append("].")
                        .toString(), CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER).append("].")
                        .toString(), CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testYoungerThanFiftyNineFiveWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_NORMAL).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_NORMAL)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER).append("].")
                        .toString(), CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER).append("].")
                        .toString(), CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER).append("].")
                        .toString(), CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testEarlyDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_NORMAL).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_NORMAL)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER).append("].")
                        .toString(), CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER).append("].")
                        .toString(), CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER).append("].")
                        .toString(), CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testDisabilityWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_EARLY_DISTRIBUTION).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_NORMAL).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_NORMAL)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testRolloverWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_EARLY_DISTRIBUTION).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER).append("].")
                        .toString(), CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER).append("].")
                        .toString(), CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER).append("].")
                        .toString(), CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN)));
    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(StringUtils.EMPTY);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testNormalDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(null);
        request
                .setIrsDistributionCodeLoanClosure(WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsCloseAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRolloverAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsRepayAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsKeepAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsBlankAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(null);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(null);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsNotPlanTrusteeAndLoanOptionIsNullAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setBirthDate(null);
        request.setLoanOption(null);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsCloseAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRolloverAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsRepayAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_REPAY_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsKeepAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(WithdrawalRequest.LOAN_KEEP_OPTION);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsBlankAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(StringUtils.EMPTY);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        request.setLoanOption(null);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        request.setLoanOption(null);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }

    /**
     * Tests the validations for the payee IRS Code for Loan field.
     */
    @Test
    public void testUnknownDistributionWithPaymentToIsPlanTrusteeAndLoanOptionIsNullAndAgeUnknown() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setBirthDate(null);
        request.setLoanOption(null);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation
        withdrawal.validateIrsCodeForLoan();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));

    }
}
