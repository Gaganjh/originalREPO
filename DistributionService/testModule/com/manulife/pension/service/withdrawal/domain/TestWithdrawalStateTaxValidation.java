package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.withdrawal.BaseWithdrawalServiceTestCase;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * Tests the range and mandatory validations for the State Tax.
 * 
 * @author dickand
 */
public class TestWithdrawalStateTaxValidation extends BaseWithdrawalServiceTestCase {

    private static final BigDecimal AMOUNT_INCREMENT = new BigDecimal("0.0001");

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);

        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        recipient.setStateTaxPercent(BigDecimal.TEN);
        recipient.setStateTaxVo(new StateTaxVO() {
            {
                this.setDefaultTaxRatePercentage(BigDecimal.TEN);
                this.setTaxPercentageMaximum(BigDecimal.ZERO);
                this.setTaxPercentageMinimum(BigDecimal.ZERO);
            }
        });

        final Collection<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(recipient);
        request.setRecipients(recipients);

        final WithdrawalRequestMoneyType moneyType = new WithdrawalRequestMoneyType();
        moneyType.setWithdrawalAmount(new BigDecimal("400"));

        final Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>();
        moneyTypes.add(moneyType);
        request.setMoneyTypes(moneyTypes);

        return request;
    }

    /**
     * Tests the validations for the state tax field.
     */
    @Test
    public void testStateTaxFieldLimitWithPercentLessThanFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.setStateTaxPercent(WithdrawalRequestRecipient.PERCENTAGE_FIELD_LIMIT_MAXIMUM
                .subtract(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform state tax field limit validation
        withdrawal.validateStateTaxFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(recipient
                .getErrorCodes()));
    }

    /**
     * Tests the validations for the state tax field.
     */
    @Test
    public void testStateTaxFieldLimitWithPercentEqualToFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.setStateTaxPercent(WithdrawalRequestRecipient.PERCENTAGE_FIELD_LIMIT_MAXIMUM);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform state tax field limit validation
        withdrawal.validateStateTaxFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(recipient
                .getErrorCodes()));
    }

    /**
     * Tests the validations for the state tax field.
     */
    @Test
    public void testStateTaxFieldLimitWithPercentGreaterThanFieldLimit() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.setStateTaxPercent(WithdrawalRequestRecipient.PERCENTAGE_FIELD_LIMIT_MAXIMUM
                .add(AMOUNT_INCREMENT));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform state tax field limit validation
        withdrawal.validateStateTaxFieldLimit();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(recipient
                .getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.STATE_TAX_INVALID).append("].").toString(), CollectionUtils
                .exists(recipient.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.STATE_TAX_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                recipient.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.STATE_TAX_PERCENT)));
    }

    /**
     * Tests the validations for the state tax field.
     */
    @Test
    public void testStateTaxFieldLimitWithNullPercent() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.setStateTaxPercent(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform state tax field limit validation
        withdrawal.validateStateTaxFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(recipient
                .getErrorCodes()));
    }

    /**
     * Tests the validations for the state tax field.
     */
    @Test
    public void testStateTaxFieldLimitWithPercentLessThanFieldLimitAndTaxWithholdingSuppressed() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.setStateTaxPercent(WithdrawalRequestRecipient.PERCENTAGE_FIELD_LIMIT_MAXIMUM
                .subtract(AMOUNT_INCREMENT));
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform state tax field limit validation
        withdrawal.validateStateTaxFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(recipient
                .getErrorCodes()));
    }

    /**
     * Tests the validations for the state tax field.
     */
    @Test
    public void testStateTaxFieldLimitWithPercentEqualToFieldLimitAndTaxWithholdingSuppressed() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.setStateTaxPercent(WithdrawalRequestRecipient.PERCENTAGE_FIELD_LIMIT_MAXIMUM);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform state tax field limit validation
        withdrawal.validateStateTaxFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(recipient
                .getErrorCodes()));
    }

    /**
     * Tests the validations for the state tax field.
     */
    @Test
    public void testStateTaxFieldLimitWithPercentGreaterThanFieldLimitAndTaxWithholdingSuppressed() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.setStateTaxPercent(WithdrawalRequestRecipient.PERCENTAGE_FIELD_LIMIT_MAXIMUM
                .add(AMOUNT_INCREMENT));
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform state tax field limit validation
        withdrawal.validateStateTaxFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(recipient
                .getErrorCodes()));
    }

    /**
     * Tests the validations for the state tax field.
     */
    @Test
    public void testStateTaxFieldLimitWithNullPercentAndTaxWithholdingSuppressed() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.setStateTaxPercent(null);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform state tax field limit validation
        withdrawal.validateStateTaxFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(recipient
                .getErrorCodes()));
    }

    /**
     * Tests the validations for the state tax field.
     */
    @Test
    public void testStateTaxFieldLimitWithPercentLessThanFieldLimitAndStateTaxSuppressed() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.setStateTaxPercent(WithdrawalRequestRecipient.PERCENTAGE_FIELD_LIMIT_MAXIMUM
                .subtract(AMOUNT_INCREMENT));
        recipient.getStateTaxVo().setDefaultTaxRatePercentage(BigDecimal.ZERO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform state tax field limit validation
        withdrawal.validateStateTaxFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(recipient
                .getErrorCodes()));
    }

    /**
     * Tests the validations for the state tax field.
     */
    @Test
    public void testStateTaxFieldLimitWithPercentEqualToFieldLimitAndStateTaxSuppressed() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.setStateTaxPercent(WithdrawalRequestRecipient.PERCENTAGE_FIELD_LIMIT_MAXIMUM);
        recipient.getStateTaxVo().setDefaultTaxRatePercentage(BigDecimal.ZERO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform state tax field limit validation
        withdrawal.validateStateTaxFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(recipient
                .getErrorCodes()));
    }

    /**
     * Tests the validations for the state tax field.
     */
    @Test
    public void testStateTaxFieldLimitWithPercentGreaterThanFieldLimitAndStateTaxSuppressed() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.setStateTaxPercent(WithdrawalRequestRecipient.PERCENTAGE_FIELD_LIMIT_MAXIMUM
                .add(AMOUNT_INCREMENT));
        recipient.getStateTaxVo().setDefaultTaxRatePercentage(BigDecimal.ZERO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform state tax field limit validation
        withdrawal.validateStateTaxFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(recipient
                .getErrorCodes()));
    }

    /**
     * Tests the validations for the state tax field.
     */
    @Test
    public void testStateTaxFieldLimitWithNullPercentAndStateTaxSuppressed() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.setStateTaxPercent(null);
        recipient.getStateTaxVo().setDefaultTaxRatePercentage(BigDecimal.ZERO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform state tax field limit validation
        withdrawal.validateStateTaxFieldLimit();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(recipient
                .getErrorCodes()));
    }

    /**
     * Tests the validations for the state tax field.
     */
    @Test
    public void testStateTaxMaximumWithNullPercentAndStateTaxSuppressed() {

        // Set up withdrawal to test
        final WithdrawalRequest withdrawalRequest = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient withdrawalRequestRecipient = (WithdrawalRequestRecipient)withdrawalRequest
                .getRecipients().iterator().next();
        withdrawalRequestRecipient.setStateTaxPercent(null);
        withdrawalRequestRecipient.getStateTaxVo().setTaxPercentageMaximum(BigDecimal.TEN);

        withdrawalRequest.setVestingCalledInd(Boolean.TRUE);

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        // Perform state tax field limit validation
        withdrawal.recalculate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils
                .size(withdrawalRequestRecipient.getErrorCodes()));
    }

}
