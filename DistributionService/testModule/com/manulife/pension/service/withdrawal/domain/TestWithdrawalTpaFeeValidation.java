package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;

/**
 * Tests the range and mandatory validations for the TPA fee including verifying that no duplication
 * of checks exists (which would result in duplicate error messages).
 * 
 * @author dickand
 */
public class TestWithdrawalTpaFeeValidation {

    private static final BigDecimal PERCENT_INCREMENT = new BigDecimal("0.001");

    private static final BigDecimal AMOUNT_INCREMENT = new BigDecimal("0.01");

    private static final BigDecimal PERCENT_MAX = new BigDecimal("100");

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);

        // Create fee object
        final WithdrawalRequestFee fee = new WithdrawalRequestFee();
        fee.setTypeCode(StringUtils.EMPTY);
        fee.setValue(null);

        final Collection<Fee> fees = new ArrayList<Fee>();
        fees.add(fee);
        request.setFees(fees);

        // Create money type object (used for total account balance)
        final WithdrawalRequestMoneyType moneyType = new WithdrawalRequestMoneyType();
        moneyType.setTotalBalance(Withdrawal.FEE_DOLLAR_THRESHOLD.scaleByPowerOfTen(1));

        final Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>();
        moneyTypes.add(moneyType);
        request.setMoneyTypes(moneyTypes);

        return request;
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testFeeFieldLimitWithAmountLessThanFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(WithdrawalRequestFee.FEE_DATABASE_FIELD_LIMIT.subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee field limit validation
        withdrawal.validateTpaFieldLimit();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testFeeFieldLimitWithAmountEqualToFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(WithdrawalRequestFee.FEE_DATABASE_FIELD_LIMIT);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee field limit validation
        withdrawal.validateTpaFieldLimit();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testFeeFieldLimitWithAmountGreaterThanFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(WithdrawalRequestFee.FEE_DATABASE_FIELD_LIMIT.add(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee field limit validation
        withdrawal.validateTpaFieldLimit();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_FEE_AMOUNT_INVALID).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_FEE_AMOUNT_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testFeeFieldLimitWithNullTypeAndAmountLessThanFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(null);
        fee.setValue(WithdrawalRequestFee.FEE_DATABASE_FIELD_LIMIT.subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee field limit validation
        withdrawal.validateTpaFieldLimit();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testFeeFieldLimitWithNullTypeAndAmountEqualToFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(null);
        fee.setValue(WithdrawalRequestFee.FEE_DATABASE_FIELD_LIMIT);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee field limit validation
        withdrawal.validateTpaFieldLimit();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testFeeFieldLimitWithNullTypeAndAmountGreaterThanFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(null);
        fee.setValue(WithdrawalRequestFee.FEE_DATABASE_FIELD_LIMIT.add(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee field limit validation
        withdrawal.validateTpaFieldLimit();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_FEE_AMOUNT_INVALID).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_FEE_AMOUNT_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testFeeFieldLimitWithBlankTypeAndAmountLessThanFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(StringUtils.EMPTY);
        fee.setValue(WithdrawalRequestFee.FEE_DATABASE_FIELD_LIMIT.subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee field limit validation
        withdrawal.validateTpaFieldLimit();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testFeeFieldLimitWithBlankTypeAndAmountEqualToFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(StringUtils.EMPTY);
        fee.setValue(WithdrawalRequestFee.FEE_DATABASE_FIELD_LIMIT);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee field limit validation
        withdrawal.validateTpaFieldLimit();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testFeeFieldLimitWithBlankTypeAndAmountGreaterThanFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(StringUtils.EMPTY);
        fee.setValue(WithdrawalRequestFee.FEE_DATABASE_FIELD_LIMIT.add(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee field limit validation
        withdrawal.validateTpaFieldLimit();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_FEE_AMOUNT_INVALID).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_FEE_AMOUNT_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithPercentLessThanZero() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.PERCENT_TYPE_CODE);
        fee.setValue(BigDecimal.ZERO.subtract(PERCENT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_FEE_PERCENTAGE_INVALID).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_FEE_PERCENTAGE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithPercentZero() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.PERCENT_TYPE_CODE);
        fee.setValue(BigDecimal.ZERO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithPercentGreaterThanZero() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.PERCENT_TYPE_CODE);
        fee.setValue(BigDecimal.ZERO.add(PERCENT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithPercentLessThanOneHundred() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.PERCENT_TYPE_CODE);
        fee.setValue(PERCENT_MAX.subtract(PERCENT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithPercentEqualToOneHundred() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.PERCENT_TYPE_CODE);
        fee.setValue(PERCENT_MAX);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithPercentGreaterThanOneHundred() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.PERCENT_TYPE_CODE);
        fee.setValue(PERCENT_MAX.add(PERCENT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_FEE_PERCENTAGE_INVALID).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_FEE_PERCENTAGE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithAmountLessThanZero() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(BigDecimal.ZERO.subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_FEE_AMOUNT_INVALID).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_FEE_AMOUNT_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithAmountZero() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(BigDecimal.ZERO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithAmountGreaterThanZero() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(BigDecimal.ZERO.add(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithMissingAmountValue() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_FEE_AMOUNT_INVALID).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_FEE_AMOUNT_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithMissingPercentValue() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.PERCENT_TYPE_CODE);
        fee.setValue(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_FEE_PERCENTAGE_INVALID).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_FEE_PERCENTAGE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithValidAmountValue() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(BigDecimal.ZERO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithValidPercentValue() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.PERCENT_TYPE_CODE);
        fee.setValue(BigDecimal.ZERO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithValueAndNullTypeCode() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(null);
        fee.setValue(BigDecimal.ZERO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_FEE_TYPE_INVALID).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_FEE_TYPE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithValueAndBlankTypeCode() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(StringUtils.EMPTY);
        fee.setValue(BigDecimal.ZERO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_FEE_TYPE_INVALID).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_FEE_TYPE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithNullTypeCodeAndNoValue() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(null);
        fee.setValue(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithBlankTypeCodeAndNoValue() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(StringUtils.EMPTY);
        fee.setValue(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithPercentLessThanThreshold() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.PERCENT_TYPE_CODE);
        fee.setValue(Withdrawal.FEE_PERCENT_THRESHOLD.subtract(PERCENT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be no errors
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue("There should be no warnings.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getWarningCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithPercentEqualToThreshold() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.PERCENT_TYPE_CODE);
        fee.setValue(Withdrawal.FEE_PERCENT_THRESHOLD);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be no errors or warnings
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue("There should be no warnings.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getWarningCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithPercentGreaterThanThreshold() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.PERCENT_TYPE_CODE);
        fee.setValue(Withdrawal.FEE_PERCENT_THRESHOLD.add(PERCENT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be a warning
        assertEquals("There should be no errors.", 0, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertEquals("There should be one warning.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_PERCENT_THRESHOLD).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getWarningCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_PERCENT_THRESHOLD)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithAmountLessThanThreshold() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(Withdrawal.FEE_DOLLAR_THRESHOLD.subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be no errors or warnings
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue("There should be no warnings.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getWarningCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithAmountEqualToThreshold() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(Withdrawal.FEE_DOLLAR_THRESHOLD);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be no errors or warnings
        assertTrue("There should be no errors.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue("There should be no warnings.", CollectionUtils.isEmpty(((WithdrawalRequestFee)fee).getWarningCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithAmountGreaterThanThreshold() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(Withdrawal.FEE_DOLLAR_THRESHOLD.add(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        // Should be a warning
        assertEquals("There should be no errors.", 0, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertEquals("There should be one warning.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_DOLLAR_THRESHOLD).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getWarningCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_DOLLAR_THRESHOLD)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithAmountLessThanParticipantAccountBalanceLessThanThreshold() {

        // Set up withdrawal to test
        final BigDecimal totalBalance = Withdrawal.FEE_DOLLAR_THRESHOLD.scaleByPowerOfTen(-1);
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setTotalBalance(totalBalance);
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(totalBalance.subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        assertEquals("There should be no errors.", 0, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(((WithdrawalRequestFee)fee).getWarningCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithAmountEqualToParticipantAccountBalanceLessThanThreshold() {

        // Set up withdrawal to test
        final BigDecimal totalBalance = Withdrawal.FEE_DOLLAR_THRESHOLD.scaleByPowerOfTen(-1);
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setTotalBalance(totalBalance);
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(totalBalance);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        assertEquals("There should be no errors.", 0, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(((WithdrawalRequestFee)fee).getWarningCodes()));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithAmountGreaterThanParticipantAccountBalanceLessThanThreshold() {

        // Set up withdrawal to test
        final BigDecimal totalBalance = Withdrawal.FEE_DOLLAR_THRESHOLD.scaleByPowerOfTen(-1);
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setTotalBalance(totalBalance);
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(totalBalance.add(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        assertEquals("There should be no warnings.", 0, CollectionUtils.size(((WithdrawalRequestFee)fee).getWarningCodes()));
        assertEquals("There should be one error.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_DOLLAR_EXCEEDS_TOTAL_BALANCE).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_DOLLAR_EXCEEDS_TOTAL_BALANCE)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithAmountLessThanParticipantAccountBalanceGreaterThanThreshold() {

        // Set up withdrawal to test
        final BigDecimal totalBalance = Withdrawal.FEE_DOLLAR_THRESHOLD.scaleByPowerOfTen(1);
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setTotalBalance(totalBalance);
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(totalBalance.subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        assertEquals("There should be no errors.", 0, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertEquals("There should be one warning.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_DOLLAR_THRESHOLD).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getWarningCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_DOLLAR_THRESHOLD)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithAmountEqualToParticipantAccountBalanceGreaterThanThreshold() {

        // Set up withdrawal to test
        final BigDecimal totalBalance = Withdrawal.FEE_DOLLAR_THRESHOLD.scaleByPowerOfTen(1);
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setTotalBalance(totalBalance);
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(totalBalance);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        assertEquals("There should be no errors.", 0, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertEquals("There should be one warning.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_DOLLAR_THRESHOLD).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getWarningCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_DOLLAR_THRESHOLD)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithAmountGreaterThanParticipantAccountBalanceGreaterThanThreshold() {

        // Set up withdrawal to test
        final BigDecimal totalBalance = Withdrawal.FEE_DOLLAR_THRESHOLD.scaleByPowerOfTen(1);
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setTotalBalance(totalBalance);
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(totalBalance.add(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        assertEquals("There should be one warning.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_DOLLAR_THRESHOLD).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getWarningCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_DOLLAR_THRESHOLD)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
        assertEquals("There should be one error.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_DOLLAR_EXCEEDS_TOTAL_BALANCE).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_DOLLAR_EXCEEDS_TOTAL_BALANCE)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
    }

    /**
     * Tests the validations for the fee fields.
     */
    @Test
    public void testWithAmountAndParticipantAccountBalanceIsNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestMoneyType moneyType = request.getMoneyTypes().iterator().next();
        moneyType.setTotalBalance(null);
        final Fee fee = request.getFees().iterator().next();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(WithdrawalRequestFee.FEE_DATABASE_FIELD_LIMIT);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform fee validation
        withdrawal.validateTpa();

        assertEquals("There should be one warning.", 1, CollectionUtils.size(((WithdrawalRequestFee)fee).getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TPA_DOLLAR_THRESHOLD).append("].").toString(),
                CollectionUtils.exists(((WithdrawalRequestFee)fee).getWarningCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TPA_DOLLAR_THRESHOLD)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                ((WithdrawalRequestFee)fee).getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FEE_VALUE)));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(((WithdrawalRequestFee)fee).getErrorCodes()));
    }
}
