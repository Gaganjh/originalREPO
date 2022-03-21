package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;

/**
 * Tests validations for the amount type field.
 * 
 * @author dickand
 */
public class TestWithdrawalAmountTypeValidation {

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        return request;
    }

    /**
     * Tests the validations for the amount type field.
     */
    @Test
    public void testSpecificAmount() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setAmountTypeCode(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateAmountType();

        // Should be no errors
        assertTrue("There should be no error.", CollectionUtils.isEmpty(withdrawal
                .getWithdrawalRequest().getErrorCodes()));
    }

    /**
     * Tests the validations for the amount type field.
     */
    @Test
    public void testMaximumAvailable() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setAmountTypeCode(WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateAmountType();

        // Should be no errors
        assertTrue("There should be no error.", CollectionUtils.isEmpty(withdrawal
                .getWithdrawalRequest().getErrorCodes()));

    }

    /**
     * Tests the validations for the amount type field.
     */
    @Test
    public void testPercentageByMoneyType() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setAmountTypeCode(WithdrawalRequest.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateAmountType();

        // Should be no errors
        assertTrue("There should be no error.", CollectionUtils.isEmpty(withdrawal
                .getWithdrawalRequest().getErrorCodes()));

    }

    /**
     * Tests the validations for the amount type field.
     */
    @Test
    public void testBlank() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setAmountTypeCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateAmountType();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(withdrawal
                .getWithdrawalRequest().getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.AMOUNT_TYPE_REQUIRED).append("].").toString(),
                CollectionUtils.exists(withdrawal.getWithdrawalRequest().getErrorCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.AMOUNT_TYPE_REQUIRED)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                withdrawal.getWithdrawalRequest().getErrorCodes(),
                new WithdrawalMessagePropertyPredicate(WithdrawalRequestProperty.AMOUNT_TYPE_CODE)));
    }

    /**
     * Tests the validations for the amount type field.
     */
    @Test
    public void testNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setAmountTypeCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateAmountType();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(withdrawal
                .getWithdrawalRequest().getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.AMOUNT_TYPE_REQUIRED).append("].").toString(),
                CollectionUtils.exists(withdrawal.getWithdrawalRequest().getErrorCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.AMOUNT_TYPE_REQUIRED)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                withdrawal.getWithdrawalRequest().getErrorCodes(),
                new WithdrawalMessagePropertyPredicate(WithdrawalRequestProperty.AMOUNT_TYPE_CODE)));
    }
}
