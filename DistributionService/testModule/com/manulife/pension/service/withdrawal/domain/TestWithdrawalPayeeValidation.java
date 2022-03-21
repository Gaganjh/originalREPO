package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.withdrawal.BaseWithdrawalServiceTestCase;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * Tests the validations of the withdrawal request payee.
 * 
 * @author dickand
 */
public class TestWithdrawalPayeeValidation extends BaseWithdrawalServiceTestCase {

    private static final String VALID_ROLLOVER_PLAN_NAME = "Plan name";

    private static final String VALID_ROLLOVER_ACCOUNT_NUMBER = "Account number";

    private static final String VALID_ADDRESS_LINE_1 = "Address";

    private static final String VALID_CITY = "CITY";

    private static final String VALID_COUNTRY = "CA";

    private static final String VALID_ORGANIZATION = "Organization";

    private static final String VALID_BANK_NAME = "Bank name";

    private static final String VALID_BANK_ACCOUNT = "Bank account";

    private static final String VALID_CREDIT_PARTY_NAME = "Party Name";

    private static final Integer VALID_ABA_NUMBER = 123456789;

    private static final String VALID_BANK_ACCOUNT_TYPE_CODE = "Account type";

    private static final String INVALID_ROLLOVER_ORGANIZATION_NAME_TOO_LONG = "Organization name is longer than 30 characters";
    
    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);

        // Create payees
        final Collection<Payee> payees = new ArrayList<Payee>();
        payees.add(new WithdrawalRequestPayee() {
            private static final long serialVersionUID = 1L;
            {
                this.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
                this.setRolloverPlanName(VALID_ROLLOVER_PLAN_NAME);
                this.setRolloverAccountNo(VALID_ROLLOVER_ACCOUNT_NUMBER);
                this.setOrganizationName(VALID_ORGANIZATION);
                this.getPaymentInstruction().setBankName(VALID_BANK_NAME);
                this.getPaymentInstruction().setCreditPartyName(VALID_CREDIT_PARTY_NAME);
                this.getPaymentInstruction().setBankAccountNumber(VALID_BANK_ACCOUNT);
                this.getPaymentInstruction().setBankTransitNumber(VALID_ABA_NUMBER);
                this.setAddress(new Address() {
                    private static final long serialVersionUID = 1L;
                    {
                        this.setAddressLine1(VALID_ADDRESS_LINE_1);
                        this.setCity(VALID_CITY);
                        this.setCountryCode(VALID_COUNTRY);
                    }
                });
            }
        });

        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        recipient.setPayees(payees);

        final Collection<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(recipient);
        request.setRecipients(recipients);

        return request;
    }

    /**
     * Tests the validations for the rollover plan name field.
     */
    @Test
    public void testRolloverPlanNameIsNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setRolloverPlanName(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform validation
        withdrawal.validatePayee(payee);

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(payee.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.NAME_OF_PLAN_INVALID).append("].").toString(),
                CollectionUtils.exists(payee.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.NAME_OF_PLAN_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ROLLOVER_PLAN_NAME)));
    }

    /**
     * Tests the validations for the rollover plan name field.
     */
    @Test
    public void testRolloverPlanNameIsBlank() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setRolloverPlanName(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform validation
        withdrawal.validatePayee(payee);

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(payee.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.NAME_OF_PLAN_INVALID).append("].").toString(),
                CollectionUtils.exists(payee.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.NAME_OF_PLAN_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ROLLOVER_PLAN_NAME)));
    }

    /**
     * Tests the validations for the rollover plan name field.
     */
    @Test
    public void testRolloverPlanNameIsDefault() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setRolloverPlanName(WithdrawalRequestPayee.DEFAULT_ROLLOVER_PLAN_NAME);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform validation
        withdrawal.validatePayee(payee);

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(payee.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.NAME_OF_PLAN_INVALID).append("].").toString(),
                CollectionUtils.exists(payee.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.NAME_OF_PLAN_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ROLLOVER_PLAN_NAME)));
    }

    /**
     * Tests the validations for the rollover plan name field.
     */
    @Test
    public void testRolloverPlanNameIsValid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setRolloverPlanName(VALID_ROLLOVER_PLAN_NAME);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform validation
        withdrawal.validatePayee(payee);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(payee.getErrorCodes()));
    }
    /**
     * Tests the validations for the rollover organization name field length for wire
     */
    @Test
    public void testRolloverOrganizationIsTooLongForWire() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setOrganizationName(INVALID_ROLLOVER_ORGANIZATION_NAME_TOO_LONG);
        payee.setPaymentMethodCode(Payee.WIRE_PAYMENT_METHOD_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform validation
        withdrawal.validatePayee(payee);

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(payee.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ORGANIZATION_NAME_LENGTH_WIRE).append("].").toString(),
                CollectionUtils.exists(payee.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ORGANIZATION_NAME_LENGTH_WIRE)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ORGANIZATION_NAME)));
    }
    /**
     * Tests the validations for the rollover organization name field length for wire
     */
    @Test
    public void testRolloverOrganizationIsTooLongForAch() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setOrganizationName(INVALID_ROLLOVER_ORGANIZATION_NAME_TOO_LONG);
        payee.setPaymentMethodCode(Payee.ACH_PAYMENT_METHOD_CODE);
        payee.getPaymentInstruction().setBankAccountTypeCode(VALID_BANK_ACCOUNT_TYPE_CODE);
        
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform validation
        withdrawal.validatePayee(payee);

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(payee.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ORGANIZATION_NAME_LENGTH_ACH).append("].").toString(),
                CollectionUtils.exists(payee.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ORGANIZATION_NAME_LENGTH_ACH)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                payee.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ORGANIZATION_NAME)));
    }
}
