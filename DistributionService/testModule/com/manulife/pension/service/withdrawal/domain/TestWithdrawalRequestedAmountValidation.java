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
 * Tests the range and mandatory validations for the Requested Amount.
 * 
 * @author dickand
 */
public class TestWithdrawalRequestedAmountValidation {

    private static final BigDecimal AMOUNT_INCREMENT = new BigDecimal("0.01");

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        request.setAmountTypeCode(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE);

        // Create money type object
        final WithdrawalRequestMoneyType moneyType = new WithdrawalRequestMoneyType();
        moneyType.setWithdrawalAmount(BigDecimal.TEN);

        final Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>();
        moneyTypes.add(moneyType);
        request.setMoneyTypes(moneyTypes);

        return request;
    }

    /**
     * Tests the validations for the requested amount field.
     */
    @Test
    public void testRequestedAmountFieldLimitWithAmountLessThanFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setWithdrawalAmount(WithdrawalRequestMoneyType.REQUESTED_AMOUNT_MAXIMUM
                .subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform requested amount field limit validation
        withdrawal.validateRequestedAmountFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(moneyType.getErrorCodes()));
    }

    /**
     * Tests the validations for the requested amount field.
     */
    @Test
    public void testRequestedAmountFieldLimitWithAmountEqualToFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setWithdrawalAmount(WithdrawalRequestMoneyType.REQUESTED_AMOUNT_MAXIMUM);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform requested amount field limit validation
        withdrawal.validateRequestedAmountFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(moneyType.getErrorCodes()));
    }

    /**
     * Tests the validations for the requested amount field.
     */
    @Test
    public void testRequestedAmountFieldLimitWithAmountGreaterThanFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setWithdrawalAmount(WithdrawalRequestMoneyType.REQUESTED_AMOUNT_MAXIMUM
                .add(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform requested amount field limit validation
        withdrawal.validateRequestedAmountFieldLimit();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(moneyType.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.REQUESTED_AMOUNT_INVALID).append("].").toString(),
                CollectionUtils.exists(moneyType.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.REQUESTED_AMOUNT_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                moneyType.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.REQUESTED_AMOUNT)));
    }

    /**
     * Tests the validations for the requested amount field.
     */
    @Test
    public void testRequestedAmountFieldLimitWithNullAmount() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setWithdrawalAmount(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform requested amount field limit validation
        withdrawal.validateRequestedAmountFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(moneyType.getErrorCodes()));
    }
}