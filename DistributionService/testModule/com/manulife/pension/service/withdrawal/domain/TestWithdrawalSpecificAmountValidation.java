package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;

/**
 * Tests the range and mandatory validations for the Specific Amount.
 * 
 * @author dickand
 */
public class TestWithdrawalSpecificAmountValidation {

    private static final BigDecimal AMOUNT_INCREMENT = new BigDecimal("0.01");

    private static final BigDecimal TOTAL_AVAILABLE = new BigDecimal("100");

    private static final BigDecimal TOTAL_AVAILABLE_THRESHOLD = new BigDecimal("95");

    private static final BigDecimal TOTAL_BALANCE = new BigDecimal("1000");

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        request.setAmountTypeCode(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE);

        // Create money type object (used for total account balance)
        final WithdrawalRequestMoneyType moneyType = new WithdrawalRequestMoneyType();
        moneyType.setWithdrawalAmount(BigDecimal.TEN);
        moneyType.setTotalBalance(TOTAL_BALANCE);
        moneyType.setAvailableWithdrawalAmount(TOTAL_AVAILABLE);

        final Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>();
        moneyTypes.add(moneyType);
        request.setMoneyTypes(moneyTypes);

        return request;
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testSpecificAmountFieldLimitWithAmountLessThanFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setAmountTypeCode(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE);
        request.setWithdrawalAmount(WithdrawalRequest.SPECIFIC_AMOUNT_DATABASE_FIELD_LIMIT
                .subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount field limit validation
        withdrawal.validateSpecificAmountFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testSpecificAmountFieldLimitWithAmountEqualToFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setWithdrawalAmount(WithdrawalRequest.SPECIFIC_AMOUNT_DATABASE_FIELD_LIMIT);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount field limit validation
        withdrawal.validateSpecificAmountFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testSpecificAmountFieldLimitWithAmountGreaterThanFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setWithdrawalAmount(WithdrawalRequest.SPECIFIC_AMOUNT_DATABASE_FIELD_LIMIT
                .add(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount field limit validation
        withdrawal.validateSpecificAmountFieldLimit();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.SPECIFIC_AMOUNT)));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testSpecificAmountFieldLimitWithNullAmount() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setWithdrawalAmount(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount field limit validation
        withdrawal.validateSpecificAmountFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }
    
    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountLessThanZero() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final BigDecimal totalRequested = BigDecimal.ZERO.subtract(AMOUNT_INCREMENT);
        request.setWithdrawalAmount(totalRequested);
        request.getMoneyTypes().iterator().next().setWithdrawalAmount(totalRequested);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePreRecalculateSpecificAmount();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.SPECIFIC_AMOUNT)));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountEqualToZero() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final BigDecimal totalRequested = BigDecimal.ZERO;
        request.setWithdrawalAmount(totalRequested);
        request.getMoneyTypes().iterator().next().setWithdrawalAmount(totalRequested);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePreRecalculateSpecificAmount();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.SPECIFIC_AMOUNT)));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountGreaterThanZero() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final BigDecimal totalRequested = BigDecimal.ZERO.add(AMOUNT_INCREMENT);
        request.setWithdrawalAmount(totalRequested);
        request.getMoneyTypes().iterator().next().setWithdrawalAmount(totalRequested);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePreRecalculateSpecificAmount();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setWithdrawalAmount(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePreRecalculateSpecificAmount();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.SPECIFIC_AMOUNT)));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountNotEqualToTotalRequested() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final BigDecimal totalRequested = request.getTotalRequestedWithdrawalAmount();
        request.setWithdrawalAmount(totalRequested.subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePreRecalculateSpecificAmount();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TOTAL_REQUESTED_NOT_EQUAL_TO_SPECIFIC).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TOTAL_REQUESTED_NOT_EQUAL_TO_SPECIFIC)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.SPECIFIC_AMOUNT)));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountEqualToTotalRequested() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final BigDecimal totalRequested = request.getTotalRequestedWithdrawalAmount();
        request.setWithdrawalAmount(totalRequested);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePreRecalculateSpecificAmount();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithTotalRequestedNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.getMoneyTypes().iterator().next().setWithdrawalAmount(null);
        request.setWithdrawalAmount(BigDecimal.TEN);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePreRecalculateSpecificAmount();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountLessThanTotalBalance() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final BigDecimal totalBalance = request.getTotalBalance();
        request.setWithdrawalAmount(totalBalance.subtract(AMOUNT_INCREMENT));
        request.getMoneyTypes().iterator().next().setWithdrawalAmount(
                totalBalance.subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePreRecalculateSpecificAmount();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountEqualToTotalBalance() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final BigDecimal totalBalance = request.getTotalBalance();
        request.setWithdrawalAmount(totalBalance);
        request.getMoneyTypes().iterator().next().setWithdrawalAmount(totalBalance);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePreRecalculateSpecificAmount();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountGreaterThanTotalBalance() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final BigDecimal totalBalance = request.getTotalBalance();
        request.setWithdrawalAmount(totalBalance.add(AMOUNT_INCREMENT));
        request.getMoneyTypes().iterator().next().setWithdrawalAmount(
                totalBalance.add(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePreRecalculateSpecificAmount();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.SPECIFIC_AMOUNT_GREATER_THAN_TOTAL_BALANCE).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.SPECIFIC_AMOUNT_GREATER_THAN_TOTAL_BALANCE)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.SPECIFIC_AMOUNT)));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountLessThanTotalBalanceEqualToTotalRequested() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final BigDecimal totalBalance = request.getTotalBalance();
        request.getMoneyTypes().iterator().next().setWithdrawalAmount(totalBalance);
        request.setWithdrawalAmount(totalBalance.subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePreRecalculateSpecificAmount();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TOTAL_REQUESTED_NOT_EQUAL_TO_SPECIFIC).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TOTAL_REQUESTED_NOT_EQUAL_TO_SPECIFIC)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.SPECIFIC_AMOUNT)));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountEqualToTotalBalanceEqualToTotalRequested() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final BigDecimal totalBalance = request.getTotalBalance();
        request.getMoneyTypes().iterator().next().setWithdrawalAmount(totalBalance);
        request.setWithdrawalAmount(totalBalance);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePreRecalculateSpecificAmount();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountGreaterThanTotalBalanceEqualToTotalRequested() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final BigDecimal totalBalance = request.getTotalBalance();
        request.getMoneyTypes().iterator().next().setWithdrawalAmount(totalBalance);
        request.setWithdrawalAmount(totalBalance.add(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePreRecalculateSpecificAmount();

        // Should be an error
        assertEquals("There should be two errors.", 2, CollectionUtils
                .size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.SPECIFIC_AMOUNT_GREATER_THAN_TOTAL_BALANCE).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.SPECIFIC_AMOUNT_GREATER_THAN_TOTAL_BALANCE)));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TOTAL_REQUESTED_NOT_EQUAL_TO_SPECIFIC).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TOTAL_REQUESTED_NOT_EQUAL_TO_SPECIFIC)));
        assertEquals("There should be two errors for the field.", 2, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.SPECIFIC_AMOUNT)));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountLessThanNinetyFivePercentOfTotalAvailableAndSimpleReason() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setWithdrawalAmount(TOTAL_AVAILABLE_THRESHOLD.subtract(AMOUNT_INCREMENT));
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePostRecalculateSpecificAmount();

        // Should be no warnings
        assertTrue("There should be no warnings.", CollectionUtils.isEmpty(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountEqualToNinetyFivePercentOfTotalAvailableAndSimpleReason() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setWithdrawalAmount(TOTAL_AVAILABLE_THRESHOLD);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePostRecalculateSpecificAmount();

        // Should be no warnings
        assertTrue("There should be no warnings.", CollectionUtils.isEmpty(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountGreaterThanNinetyFivePercentOfTotalAvailableAndSimpleReason() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setWithdrawalAmount(TOTAL_AVAILABLE_THRESHOLD.add(AMOUNT_INCREMENT));
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePostRecalculateSpecificAmount();

        // Should be no warnings
        assertTrue("There should be no warnings.", CollectionUtils.isEmpty(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountLessThanNinetyFivePercentOfTotalAvailableAndRobustReason() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setWithdrawalAmount(TOTAL_AVAILABLE_THRESHOLD.subtract(AMOUNT_INCREMENT));
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePostRecalculateSpecificAmount();

        // Should be no warnings
        assertTrue("There should be no warnings.", CollectionUtils.isEmpty(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountEqualToNinetyFivePercentOfTotalAvailableAndRobustReason() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setWithdrawalAmount(TOTAL_AVAILABLE_THRESHOLD);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePostRecalculateSpecificAmount();

        // Should be a warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.SPECIFIC_AMOUNT_WITHIN_THRESHOLD).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.SPECIFIC_AMOUNT_WITHIN_THRESHOLD)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.SPECIFIC_AMOUNT)));
    }

    /**
     * Tests the validations for the specific amount field.
     */
    @Test
    public void testWithSpecificAmountGreaterThanNinetyFivePercentOfTotalAvailableAndRobustReason() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setWithdrawalAmount(TOTAL_AVAILABLE_THRESHOLD.add(AMOUNT_INCREMENT));
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform specific amount validation
        withdrawal.validatePostRecalculateSpecificAmount();

        // Should be a warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.SPECIFIC_AMOUNT_WITHIN_THRESHOLD).append("].").toString(),
                CollectionUtils.exists(request.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.SPECIFIC_AMOUNT_WITHIN_THRESHOLD)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.SPECIFIC_AMOUNT)));
    }
}
