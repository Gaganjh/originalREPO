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
 * Tests the range and mandatory validations for the Vested Percent.
 * 
 * @author dickand
 */
public class TestWithdrawalVestedPercentValidation {

    private static final BigDecimal AMOUNT_INCREMENT = new BigDecimal("0.01");

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);

        // Create money type object
        final WithdrawalRequestMoneyType moneyType = new WithdrawalRequestMoneyType();
        moneyType.setVestingPercentage(BigDecimal.TEN);
        moneyType.setVestingPercentageUpdateable(true);

        final Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>();
        moneyTypes.add(moneyType);
        request.setMoneyTypes(moneyTypes);

        return request;
    }

    /**
     * Tests the validations for the vested percent field.
     */
    @Test
    public void testVestedPercentFieldLimitWithAmountLessThanFieldLimitAndEditable() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setVestingPercentageUpdateable(true);
        moneyType
                .setVestingPercentage(WithdrawalRequestMoneyType.PERCENTAGE_FIELD_LIMIT_MAXIMUM
                        .subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform vesting percent field limit validation
        withdrawal.validateVestingPercentFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(moneyType.getErrorCodes()));
    }

    /**
     * Tests the validations for the vested percent field.
     */
    @Test
    public void testVestedPercentFieldLimitWithAmountEqualToFieldLimitAndEditable() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setVestingPercentageUpdateable(true);
        moneyType
                .setVestingPercentage(WithdrawalRequestMoneyType.PERCENTAGE_FIELD_LIMIT_MAXIMUM);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform vesting percent field limit validation
        withdrawal.validateVestingPercentFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(moneyType.getErrorCodes()));
    }

    /**
     * Tests the validations for the vested percent field.
     */
    @Test
    public void testVestedPercentFieldLimitWithAmountGreaterThanFieldLimitAndEditable() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setStatusCode(WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode());
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setVestingPercentageUpdateable(true);
        moneyType
                .setVestingPercentage(WithdrawalRequestMoneyType.PERCENTAGE_FIELD_LIMIT_MAXIMUM
                        .add(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform vesting percent field limit validation
        withdrawal.validateVestingPercentFieldLimit();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(moneyType.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.VESTED_PERCENTAGE_INVALID).append("].").toString(),
                CollectionUtils.exists(moneyType.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.VESTED_PERCENTAGE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                moneyType.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.VESTING_PERCENTAGE)));
    }

    /**
     * Tests the validations for the vested percent field.
     */
    @Test
    public void testVestedPercentFieldLimitWithNullAmountAndEditable() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setVestingPercentageUpdateable(true);
        moneyType.setVestingPercentage(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform vesting percent field limit validation
        withdrawal.validateVestingPercentFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(moneyType.getErrorCodes()));
    }

    /**
     * Tests the validations for the vested percent field.
     */
    @Test
    public void testVestedPercentFieldLimitWithAmountLessThanFieldLimitAndNonEditable() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setVestingPercentageUpdateable(false);
        moneyType
                .setVestingPercentage(WithdrawalRequestMoneyType.PERCENTAGE_FIELD_LIMIT_MAXIMUM
                        .subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform vesting percent field limit validation
        withdrawal.validateVestingPercentFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(moneyType.getErrorCodes()));
    }

    /**
     * Tests the validations for the vested percent field.
     */
    @Test
    public void testVestedPercentFieldLimitWithAmountEqualToFieldLimitAndNonEditable() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setVestingPercentageUpdateable(false);
        moneyType
                .setVestingPercentage(WithdrawalRequestMoneyType.PERCENTAGE_FIELD_LIMIT_MAXIMUM);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform vesting percent field limit validation
        withdrawal.validateVestingPercentFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(moneyType.getErrorCodes()));
    }

    /**
     * Tests the validations for the vested percent field.
     */
    @Test
    public void testVestedPercentFieldLimitWithAmountGreaterThanFieldLimitAndNonEditable() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setVestingPercentageUpdateable(false);
        moneyType
                .setVestingPercentage(WithdrawalRequestMoneyType.PERCENTAGE_FIELD_LIMIT_MAXIMUM
                        .add(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform vesting percent field limit validation
        withdrawal.validateVestingPercentFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(moneyType.getErrorCodes()));
    }

    /**
     * Tests the validations for the vested percent field.
     */
    @Test
    public void testVestedPercentFieldLimitWithNullAmountAndNonEditable() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setVestingPercentageUpdateable(false);
        moneyType.setVestingPercentage(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform vesting percent field limit validation
        withdrawal.validateVestingPercentFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(moneyType.getErrorCodes()));
    }
}