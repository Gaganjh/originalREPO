package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * Tests the validations for the IRS Code for Withdrawal.
 * 
 * @author dickand
 */
public class TestWithdrawalIrsCodeForWithdrawal {

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest(final int payeeCount, final String irsCode) {
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);

        // Create payees
        final Collection<Payee> payees = new ArrayList<Payee>();
        for (int i = 0; i < payeeCount; i++) {
            payees.add(new WithdrawalRequestPayee() {
                private static final long serialVersionUID = 1L;
                {
                    this.setIrsDistCode(irsCode);

                }
            });
        }
        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        recipient.setPayees(payees);

        final Collection<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(recipient);
        request.setRecipients(recipients);

        return request;
    }

    /**
     * Retrieves a current date with the time components cleared.
     */
    private Date getTimeFreeDate() {
        return DateUtils.truncate(new Date(), Calendar.DATE);
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsDirectToParticipantAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsPlanTrusteeAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToPlanAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToIraAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToPlanAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToIraAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsDirectToParticipantAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsPlanTrusteeAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToPlanAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsRolloverToIraAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToPlanAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsValidAndPaymentToIsAfterTaxRemainderToIraAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsDirectToParticipantAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, StringUtils.EMPTY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be one error
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one error.").toString(), 1, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR).append("].").toString(),
                    CollectionUtils.exists(payee.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsPlanTrusteeAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, StringUtils.EMPTY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToPlanAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, StringUtils.EMPTY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be one error
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one error.").toString(), 1, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR).append("].").toString(),
                    CollectionUtils.exists(payee.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToIraAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, StringUtils.EMPTY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be one error
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one error.").toString(), 1, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR).append("].").toString(),
                    CollectionUtils.exists(payee.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, StringUtils.EMPTY);
        request.setPaymentTo(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be one error
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one error.").toString(), 1, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR).append("].").toString(),
                    CollectionUtils.exists(payee.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, StringUtils.EMPTY);
        request.setPaymentTo(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be one error
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one error.").toString(), 1, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR).append("].").toString(),
                    CollectionUtils.exists(payee.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToPlanAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2, StringUtils.EMPTY);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be one error
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one error.").toString(), 1, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR).append("].").toString(),
                    CollectionUtils.exists(payee.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToIraAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2, StringUtils.EMPTY);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be one error
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one error.").toString(), 1, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR).append("].").toString(),
                    CollectionUtils.exists(payee.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsDirectToParticipantAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, StringUtils.EMPTY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be one warning
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one warning.").toString(), 1, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING).append("].").toString(),
                    CollectionUtils.exists(payee.getWarningCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsPlanTrusteeAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, StringUtils.EMPTY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToPlanAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, StringUtils.EMPTY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be one warning
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one warning.").toString(), 1, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING).append("].").toString(),
                    CollectionUtils.exists(payee.getWarningCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsRolloverToIraAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, StringUtils.EMPTY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be one warning
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one warning.").toString(), 1, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING).append("].").toString(),
                    CollectionUtils.exists(payee.getWarningCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, StringUtils.EMPTY);
        request.setPaymentTo(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be one warning
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one warning.").toString(), 1, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING).append("].").toString(),
                    CollectionUtils.exists(payee.getWarningCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, StringUtils.EMPTY);
        request.setPaymentTo(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be one warning
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one warning.").toString(), 1, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING).append("].").toString(),
                    CollectionUtils.exists(payee.getWarningCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToPlanAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2, StringUtils.EMPTY);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be one warning
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one warning.").toString(), 1, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING).append("].").toString(),
                    CollectionUtils.exists(payee.getWarningCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsBlankAndPaymentToIsAfterTaxRemainderToIraAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2, StringUtils.EMPTY);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be one warning
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one warning.").toString(), 1, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING).append("].").toString(),
                    CollectionUtils.exists(payee.getWarningCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsDirectToParticipantAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, null);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be one error
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one error.").toString(), 1, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR).append("].").toString(),
                    CollectionUtils.exists(payee.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsPlanTrusteeAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, null);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsRolloverToPlanAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, null);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be one error
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one error.").toString(), 1, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR).append("].").toString(),
                    CollectionUtils.exists(payee.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsRolloverToIraAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, null);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be one error
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one error.").toString(), 1, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR).append("].").toString(),
                    CollectionUtils.exists(payee.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsBlankAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, null);
        request.setPaymentTo(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be one error
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one error.").toString(), 1, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR).append("].").toString(),
                    CollectionUtils.exists(payee.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsNullAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, null);
        request.setPaymentTo(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be one error
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one error.").toString(), 1, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR).append("].").toString(),
                    CollectionUtils.exists(payee.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsAfterTaxRemainderToPlanAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2, null);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be one error
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one error.").toString(), 1, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR).append("].").toString(),
                    CollectionUtils.exists(payee.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsAfterTaxRemainderToIraAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2, null);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);

            // Should be one error
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one error.").toString(), 1, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR).append("].").toString(),
                    CollectionUtils.exists(payee.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsDirectToParticipantAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, null);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be one warning
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one warning.").toString(), 1, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING).append("].").toString(),
                    CollectionUtils.exists(payee.getWarningCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsPlanTrusteeAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, null);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be no warnings
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no warnings.").toString(), 0, CollectionUtils
                    .size(payee.getWarningCodes()));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsRolloverToPlanAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, null);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be one warning
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one warning.").toString(), 1, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING).append("].").toString(),
                    CollectionUtils.exists(payee.getWarningCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsRolloverToIraAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, null);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be one warning
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one warning.").toString(), 1, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING).append("].").toString(),
                    CollectionUtils.exists(payee.getWarningCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsBlankAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, null);
        request.setPaymentTo(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be one warning
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one warning.").toString(), 1, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING).append("].").toString(),
                    CollectionUtils.exists(payee.getWarningCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsNullAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1, null);
        request.setPaymentTo(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be one warning
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one warning.").toString(), 1, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING).append("].").toString(),
                    CollectionUtils.exists(payee.getWarningCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsAfterTaxRemainderToPlanAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2, null);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be one warning
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one warning.").toString(), 1, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING).append("].").toString(),
                    CollectionUtils.exists(payee.getWarningCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testExistsWhenIsNullAndPaymentToIsAfterTaxRemainderToIraAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2, null);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code existence validation on all payees
        int i = 1;
        for (Payee myPayee : request.getRecipients().iterator().next().getPayees()) {
 WithdrawalRequestPayee payee = (WithdrawalRequestPayee)myPayee;

            withdrawal.validateIrsCodeForWithdrawalExists(payee,
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            // Should be no errors
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be no errors.").toString(), 0, CollectionUtils
                    .size(payee.getErrorCodes()));
            // Should be one warning
            assertEquals(new StringBuffer("[").append(i == 1 ? "First" : "Second").append(
                    "] payee - there should be one warning.").toString(), 1, CollectionUtils
                    .size(payee.getWarningCodes()));
            assertTrue(new StringBuffer("Expecting message type [").append(
                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING).append("].").toString(),
                    CollectionUtils.exists(payee.getWarningCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
            assertEquals("There should be one warning for the field.", 1, CollectionUtils
                    .countMatches(payee.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
        }
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsDirectToParticipantAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsDirectToParticipantAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsDirectToParticipantAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsDirectToParticipantAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsRolloverToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsRolloverToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsRolloverToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsRolloverToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsRolloverToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsRolloverToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsRolloverToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsRolloverToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsPlanTrusteeAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsPlanTrusteeAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsPlanTrusteeAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsPlanTrusteeAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append(
                "] for payee 2.").toString(), CollectionUtils.exists(payee2.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append(
                "] for payee 2.").toString(), CollectionUtils.exists(payee2.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsDirectToParticipantAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsDirectToParticipantAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsDirectToParticipantAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsDirectToParticipantAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsRolloverToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsRolloverToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsRolloverToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsRolloverToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsRolloverToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsRolloverToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsRolloverToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsRolloverToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsPlanTrusteeAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsPlanTrusteeAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsPlanTrusteeAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsPlanTrusteeAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append(
                "] for payee 2.").toString(), CollectionUtils.exists(payee2.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionExceptionPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append(
                "] for payee 2.").toString(), CollectionUtils.exists(payee2.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsDirectToParticipantAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsDirectToParticipantAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsDirectToParticipantAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsDirectToParticipantAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsRolloverToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsRolloverToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsRolloverToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsRolloverToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsRolloverToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsRolloverToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsRolloverToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsRolloverToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsPlanTrusteeAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsPlanTrusteeAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsPlanTrusteeAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsPlanTrusteeAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append(
                "] for payee 2.").toString(), CollectionUtils.exists(payee2.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testEarlyDistributionTenYearOptionPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append(
                "] for payee 2.").toString(), CollectionUtils.exists(payee2.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsDirectToParticipantAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsDirectToParticipantAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsDirectToParticipantAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                payee1.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsDirectToParticipantAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsRolloverToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsRolloverToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsRolloverToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsRolloverToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsRolloverToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsRolloverToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsRolloverToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsRolloverToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsPlanTrusteeAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsPlanTrusteeAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsPlanTrusteeAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsPlanTrusteeAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("] for payee 2.").toString(),
                CollectionUtils
                        .exists(
                                payee2.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append(
                "] for payee 2.").toString(), CollectionUtils.exists(payee2.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("] for payee 2.").toString(),
                CollectionUtils
                        .exists(
                                payee2.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append(
                "] for payee 2.").toString(), CollectionUtils.exists(payee2.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsDirectToParticipantAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsDirectToParticipantAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsDirectToParticipantAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                payee1.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsDirectToParticipantAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsRolloverToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsRolloverToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsRolloverToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsRolloverToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsRolloverToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsRolloverToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsRolloverToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsRolloverToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsPlanTrusteeAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsPlanTrusteeAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsPlanTrusteeAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsPlanTrusteeAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("] for payee 2.").toString(),
                CollectionUtils
                        .exists(
                                payee2.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append(
                "] for payee 2.").toString(), CollectionUtils.exists(payee2.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("] for payee 2.").toString(),
                CollectionUtils
                        .exists(
                                payee2.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testDisabilityTenYearOptionPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_DISABILITY_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append(
                "] for payee 2.").toString(), CollectionUtils.exists(payee2.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsDirectToParticipantAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsDirectToParticipantAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsDirectToParticipantAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                payee1.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsDirectToParticipantAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsRolloverToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsRolloverToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsRolloverToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsRolloverToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsRolloverToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsRolloverToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsRolloverToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsRolloverToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsPlanTrusteeAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsPlanTrusteeAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsPlanTrusteeAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsPlanTrusteeAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 1 should have no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 1 should have no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 1 should have no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("] for payee 2.").toString(),
                CollectionUtils
                        .exists(
                                payee2.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 1 should have no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append(
                "] for payee 2.").toString(), CollectionUtils.exists(payee2.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 1 should have no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 1 should have no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 1 should have no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("] for payee 2.").toString(),
                CollectionUtils
                        .exists(
                                payee2.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testRolloverPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 1 should have no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL).append(
                "] for payee 2.").toString(), CollectionUtils.exists(payee2.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsDirectToParticipantAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsDirectToParticipantAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsDirectToParticipantAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                payee1.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsDirectToParticipantAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsRolloverToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsRolloverToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsRolloverToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsRolloverToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsRolloverToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsRolloverToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsRolloverToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsRolloverToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsPlanTrusteeAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsPlanTrusteeAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsPlanTrusteeAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsPlanTrusteeAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("] for payee 2.").toString(),
                CollectionUtils
                        .exists(
                                payee2.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("] for payee 2.").toString(),
                CollectionUtils
                        .exists(
                                payee2.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsDirectToParticipantAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsDirectToParticipantAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsDirectToParticipantAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                payee1.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsDirectToParticipantAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsRolloverToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsRolloverToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsRolloverToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsRolloverToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsRolloverToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsRolloverToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsRolloverToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsRolloverToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // There should be one warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append("].")
                .toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsPlanTrusteeAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsPlanTrusteeAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsPlanTrusteeAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsPlanTrusteeAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                1,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee1.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(payee1
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsAfterTaxRemainderToIraAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("] for payee 2.").toString(),
                CollectionUtils
                        .exists(
                                payee2.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsAfterTaxRemainderToIraAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsAfterTaxRemainderToPlanAndReasonDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndUnderFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -59));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 2 should be one warning.", 1, CollectionUtils.size(payee2
                .getWarningCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)
                        .append("] for payee 2.").toString(),
                CollectionUtils
                        .exists(
                                payee2.getWarningCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
        assertEquals("There should be one warning for the field on payee 2.", 1, CollectionUtils
                .countMatches(payee2.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));
    }

    /**
     * Tests the validations for the payee IRS Code for Withdrawals field.
     */
    @Test
    public void testNormalTenYearOptionPaymentToIsAfterTaxRemainderToPlanAndReasonNotDisabledAndOverFiftyNineFive() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest(
                2,
                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        request.setBirthDate(DateUtils.addYears(getTimeFreeDate(), -60));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform IRS code validation on all payees
        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
       final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        withdrawal.validateIrsCodeForWithdrawal(payee1, true);
        withdrawal.validateIrsCodeForWithdrawal(payee2, false);

        // Should be no errors
        assertEquals("Payee 1 should have no errors.", 0, CollectionUtils.size(payee1
                .getErrorCodes()));

        // There should be one warning
        assertEquals("Payee 1 should be one warning.", 1, CollectionUtils.size(payee1
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER).append(
                "] for payee 1.").toString(), CollectionUtils.exists(payee1.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
        assertEquals("There should be one warning for the field on payee 1.", 1, CollectionUtils
                .countMatches(payee1.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL)));

        // Should be no errors
        assertEquals("Payee 2 should have no errors.", 0, CollectionUtils.size(payee2
                .getErrorCodes()));

        // Should be no warnings
        assertEquals("Payee 2 should have no warnings.", 0, CollectionUtils.size(payee2
                .getWarningCodes()));
    }
}
