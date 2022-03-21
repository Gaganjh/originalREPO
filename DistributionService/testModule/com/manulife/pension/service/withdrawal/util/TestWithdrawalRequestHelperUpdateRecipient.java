package com.manulife.pension.service.withdrawal.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.JUnit4TestAdapter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.Test;

import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * Tests the default setup of the payee address for step 2.
 *
 * @author Andrew Dick
 */
public class TestWithdrawalRequestHelperUpdateRecipient {

    private static final int DIRECT_TO_PARTICIPANT_PAYEE_COUNT = 1;

    private static final int ROLLOVER_TO_IRA_PAYEE_COUNT = 1;

    private static final int ROLLOVER_TO_PLAN_PAYEE_COUNT = 1;

    private static final int AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT = 2;

    private static final int AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT = 2;

    private static final int PLAN_TRUSTEE_PAYEE_COUNT = 1;

    private static final String INVALID_PAYMENT_TO = "This is not a valid code";

    private static final String ORGANIZATION_NAME_1 = "Organization name 1";

    private static final String ORGANIZATION_NAME_2 = "Organization name 2";

    private static final Integer PAYEE_NO_1 = 1;

    private static final Integer PAYEE_NO_2 = 2;
    
    private static final Address DEFAULT_ADDRESS_1 = new Address() {
        {

            this.setAddressLine1("Default 1 Address Line 1");
            this.setAddressLine2("Default 1 Address Line 2");
            this.setCity("Default 1 City");
            this.setZipCode("111111111");
            this.setStateCode("CA");
            this.setCountryCode("USA");
        };
    };

    private static final Address DEFAULT_ADDRESS_2 = new Address() {
        {

            this.setAddressLine1("Default 2 Address Line 1");
            this.setAddressLine2("Default 2 Address Line 2");
            this.setCity("Default 2 City");
            this.setZipCode("222222222");
            this.setStateCode("GE");
            this.setCountryCode("USA");
        };
    };

    private static final Address PARTICIPANT_ADDRESS = new Address() {
        {

            this.setAddressLine1("Participant Address Line 1");
            this.setAddressLine2("Participant Address Line 2");
            this.setCity("Participant City");
            this.setZipCode("333333333");
            this.setStateCode("WA");
            this.setCountryCode("USA");
        };
    };

    private static final Address TRUSTEE_ADDRESS = new Address() {
        {

            this.setAddressLine1("Trustee Address Line 1");
            this.setAddressLine2("Trustee Address Line 2");
            this.setCity("Trustee City");
            this.setZipCode("444444444");
            this.setStateCode("FL");
            this.setCountryCode("USA");
        };
    };

    private static final Address BLANK_ADDRESS = new Address();

    /**
     * Creates a base request object populated with a recipient and the specified number of payees.
     *
     * @param payeeCount The number of payees to populate the base object with.
     * @return WithdrawalRequest A base request object.
     */
    private WithdrawalRequest getBaseWithdrawalRequest(final int payeeCount) {

        final WithdrawalRequest request = new WithdrawalRequest();

        // Create payee
        final Collection<Payee> payees = new ArrayList<Payee>();
        for (int i = 0; i < payeeCount; i++) {
            final WithdrawalRequestPayee payee = new WithdrawalRequestPayee();
            payee.setAddress((i == 0) ? DEFAULT_ADDRESS_1 : DEFAULT_ADDRESS_2);
            payee.setOrganizationName((i == 0) ? ORGANIZATION_NAME_1 : ORGANIZATION_NAME_2);
            payee.setPayeeNo(i + 1);

            payees.add(payee);
        }

        // Create recipient
        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();

        recipient.setPayees(payees);
        final Collection<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(recipient);
        request.setRecipients(recipients);

        request.setTrusteeAddress(TRUSTEE_ADDRESS);
        request.setParticipantAddress(PARTICIPANT_ADDRESS);

        return request;
    }

    /**
     * Tests the validation of the payment to field when it is blank.
     */
    @Test(expected = NestableRuntimeException.class)
    public void testPaymentToBlank() {
        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setPaymentTo(StringUtils.EMPTY);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);
    }

    /**
     * Tests the validation of the payment to field when it is null.
     */
    @Test(expected = NestableRuntimeException.class)
    public void testPaymentToNull() {
        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setPaymentTo(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);
    }

    /**
     * Tests the validation of the payment to field when it is invalid.
     */
    @Test(expected = NestableRuntimeException.class)
    public void testPaymentToInvalid() {
        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setPaymentTo(INVALID_PAYMENT_TO);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToDirectToParticipantWithNoOriginalPaymentTo() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(null);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraWithNoOriginalPaymentTo() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(null);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_IRA_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanWithNoOriginalPaymentTo() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(null);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_PLAN_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraWithNoOriginalPaymentTo() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(null);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanWithNoOriginalPaymentTo() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(null);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeWithNoOriginalPaymentTo() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(null);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", PLAN_TRUSTEE_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", TRUSTEE_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", TRUSTEE_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test(expected = NestableRuntimeException.class)
    public void testPaymentToDirectToParticipantWithInvalidOriginalPaymentTo() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(INVALID_PAYMENT_TO);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test(expected = NestableRuntimeException.class)
    public void testPaymentToRolloverToIraWithInvalidOriginalPaymentTo() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(INVALID_PAYMENT_TO);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test(expected = NestableRuntimeException.class)
    public void testPaymentToRolloverToPlanWithInvalidOriginalPaymentTo() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(INVALID_PAYMENT_TO);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test(expected = NestableRuntimeException.class)
    public void testPaymentToAfterTaxContributionRemainderToIraWithInvalidOriginalPaymentTo() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(INVALID_PAYMENT_TO);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test(expected = NestableRuntimeException.class)
    public void testPaymentToAfterTaxContributionRemainderToPlanWithInvalidOriginalPaymentTo() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(INVALID_PAYMENT_TO);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test(expected = NestableRuntimeException.class)
    public void testPaymentToPlanTrusteeWithInvalidOriginalPaymentTo() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(INVALID_PAYMENT_TO);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToDirectToParticipantUnchanged() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToDirectToParticipantChangedToRolloverToIra() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_IRA_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToDirectToParticipantChangedToRolloverToPlan() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_PLAN_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToDirectToParticipantChangedToAfterTaxContributionRemainderToIraWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", DEFAULT_ADDRESS_1, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee 2 organization name should be set properly.", ORGANIZATION_NAME_1,
                payee2.getOrganizationName());
        assertEquals("Payee 2 payment method should be set properly.", WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE,
                payee2.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToDirectToParticipantChangedToAfterTaxContributionRemainderToIraWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", DEFAULT_ADDRESS_1, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee 2 organization name should be set properly.", ORGANIZATION_NAME_1,
                payee2.getOrganizationName());
        assertEquals("Payee 2 payment method should be set properly.", WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE,
                payee2.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToDirectToParticipantChangedToAfterTaxContributionRemainderToIraWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", DEFAULT_ADDRESS_1, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee 2 organization name should be set properly.", ORGANIZATION_NAME_1,
                payee2.getOrganizationName());
        assertEquals("Payee 2 payment method should be set properly.", WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE,
                payee2.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToDirectToParticipantChangedToAfterTaxContributionRemainderToIraWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", DEFAULT_ADDRESS_1, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee 2 organization name should be set properly.", null, payee2
                .getOrganizationName());
        assertEquals("Payee 2 payment method should be set properly.", null,
                payee2.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToDirectToParticipantChangedToAfterTaxContributionRemainderToPlanWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", DEFAULT_ADDRESS_1, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee 2 organization name should be set properly.", ORGANIZATION_NAME_1,
                payee2.getOrganizationName());
        assertEquals("Payee 2 payment method should be set properly.", WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE,
                payee2.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToDirectToParticipantChangedToAfterTaxContributionRemainderToPlanWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", DEFAULT_ADDRESS_1, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee 2 organization name should be set properly.", ORGANIZATION_NAME_1,
                payee2.getOrganizationName());
        assertEquals("Payee 2 payment method should be set properly.", WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE,
                payee2.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToDirectToParticipantChangedToAfterTaxContributionRemainderToPlanWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", DEFAULT_ADDRESS_1, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee 2 organization name should be set properly.", ORGANIZATION_NAME_1,
                payee2.getOrganizationName());
        assertEquals("Payee 2 payment method should be set properly.", WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE,
                payee2.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToDirectToParticipantChangedToAfterTaxContributionRemainderToPlanWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", DEFAULT_ADDRESS_1, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee 2 organization name should be set properly.", null, payee2
                .getOrganizationName());
        assertEquals("Payee 2 payment method should be set properly.", null,
                payee2.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToDirectToParticipantChangedToPlanTrustee() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(DIRECT_TO_PARTICIPANT_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", PLAN_TRUSTEE_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", TRUSTEE_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", TRUSTEE_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraUnchanged() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_IRA_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraChangedToDirectToParticipantWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraChangedToDirectToParticipantWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraChangedToDirectToParticipantWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraChangedToDirectToParticipantWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.", null, payee1.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraChangedToRolloverToPlan() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_PLAN_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraChangedToAfterTaxContributionRemainderToIraWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee 1 payment method should be set correctly.",
                WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee 1 organization name should be set properly.", ORGANIZATION_NAME_1,
                payee1.getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraChangedToAfterTaxContributionRemainderToIraWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee 1 payment method should be set correctly.",
                WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee 1 organization name should be set properly.", ORGANIZATION_NAME_1,
                payee1.getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraChangedToAfterTaxContributionRemainderToIraWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee 1 payment method should be set correctly.",
                WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee 1 organization name should be set properly.", ORGANIZATION_NAME_1,
                payee1.getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraChangedToAfterTaxContributionRemainderToIraWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee 1 payment method should be set correctly.", null, payee1
                .getPaymentMethodCode());
        assertEquals("Payee 1 organization name should be set properly.", null, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraChangedToAfterTaxContributionRemainderToPlanWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraChangedToAfterTaxContributionRemainderToPlanWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraChangedToAfterTaxContributionRemainderToPlanWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraChangedToAfterTaxContributionRemainderToPlanWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToIraChangedToPlanTrustee() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_IRA_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", PLAN_TRUSTEE_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", TRUSTEE_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", TRUSTEE_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanUnchanged() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_PLAN_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanChangedToDirectToParticipantWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanChangedToDirectToParticipantWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanChangedToDirectToParticipantWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanChangedToDirectToParticipantWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.", null, payee1.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanChangedToRolloverToIra() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_IRA_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanChangedToAfterTaxContributionRemainderToIraWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanChangedToAfterTaxContributionRemainderToIraWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanChangedToAfterTaxContributionRemainderToIraWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanChangedToAfterTaxContributionRemainderToIraWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanChangedToAfterTaxContributionRemainderToPlanWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee 1 payment method should be set correctly.",
                WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee 1 organization name should be set properly.", ORGANIZATION_NAME_1,
                payee1.getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanChangedToAfterTaxContributionRemainderToPlanWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee 1 payment method should be set correctly.",
                WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee 1 organization name should be set properly.", ORGANIZATION_NAME_1,
                payee1.getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanChangedToAfterTaxContributionRemainderToPlanWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee 1 payment method should be set correctly.",
                WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee 1 organization name should be set properly.", ORGANIZATION_NAME_1,
                payee1.getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanChangedToAfterTaxContributionRemainderToPlanWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee 1 payment method should be set correctly.", null, payee1
                .getPaymentMethodCode());
        assertEquals("Payee 1 organization name should be set properly.", null, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToRolloverToPlanChangedToPlanTrustee() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(ROLLOVER_TO_PLAN_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", PLAN_TRUSTEE_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", TRUSTEE_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", TRUSTEE_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanUnchanged() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should still have two payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", DEFAULT_ADDRESS_2, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanChangedToDirectToParticipantWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        final Iterator<Payee> initIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        final WithdrawalRequestPayee initPayee1 = (WithdrawalRequestPayee)initIterator.next();
        final WithdrawalRequestPayee initPayee2 = (WithdrawalRequestPayee)initIterator.next();
        initPayee2.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_2, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", ORGANIZATION_NAME_2, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanChangedToDirectToParticipantWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        final Iterator<Payee> initIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        final WithdrawalRequestPayee initPayee1 = (WithdrawalRequestPayee)initIterator.next();
        final WithdrawalRequestPayee initPayee2 = (WithdrawalRequestPayee)initIterator.next();
        initPayee2.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_2, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", ORGANIZATION_NAME_2, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanChangedToDirectToParticipantWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        final Iterator<Payee> initIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        final WithdrawalRequestPayee initPayee1 = (WithdrawalRequestPayee)initIterator.next();
        final WithdrawalRequestPayee initPayee2 = (WithdrawalRequestPayee)initIterator.next();
        initPayee2.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_2, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", ORGANIZATION_NAME_2, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanChangedToDirectToParticipantWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        final Iterator<Payee> initIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        final WithdrawalRequestPayee initPayee1 = (WithdrawalRequestPayee)initIterator.next();
        final WithdrawalRequestPayee initPayee2 = (WithdrawalRequestPayee)initIterator.next();
        initPayee2.setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_2, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.", null, payee1.getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", null, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanChangedToRolloverToIra() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_IRA_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanChangedToRolloverToPlanWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_PLAN_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee 1 payment method should be set correctly.",
                WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", ORGANIZATION_NAME_1, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanChangedToRolloverToPlanWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_PLAN_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee 1 payment method should be set correctly.",
                WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", ORGANIZATION_NAME_1, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanChangedToRolloverToPlanWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_PLAN_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee 1 payment method should be set correctly.",
                WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", ORGANIZATION_NAME_1, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanChangedToRolloverToPlanWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_PLAN_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee 1 payment method should be set correctly.", null, payee1
                .getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", null, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanChangedToAfterTaxContributionRemainderToIraWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        final Iterator<Payee> initIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        final WithdrawalRequestPayee initPayee1 = (WithdrawalRequestPayee)initIterator.next();
        final WithdrawalRequestPayee initPayee2 = (WithdrawalRequestPayee)initIterator.next();
        initPayee2.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanChangedToAfterTaxContributionRemainderToIraWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        final Iterator<Payee> initIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        final WithdrawalRequestPayee initPayee1 = (WithdrawalRequestPayee)initIterator.next();
        final WithdrawalRequestPayee initPayee2 = (WithdrawalRequestPayee)initIterator.next();
        initPayee2.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanChangedToAfterTaxContributionRemainderToIraWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        final Iterator<Payee> initIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        final WithdrawalRequestPayee initPayee1 = (WithdrawalRequestPayee)initIterator.next();
        final WithdrawalRequestPayee initPayee2 = (WithdrawalRequestPayee)initIterator.next();
        initPayee2.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanChangedToAfterTaxContributionRemainderToIraWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlanChangedToPlanTrustee() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", PLAN_TRUSTEE_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", TRUSTEE_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", TRUSTEE_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraUnchanged() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should still have two payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", DEFAULT_ADDRESS_2, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraChangedToDirectToParticipantWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        final Iterator<Payee> initIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        final WithdrawalRequestPayee initPayee1 = (WithdrawalRequestPayee)initIterator.next();
        final WithdrawalRequestPayee initPayee2 = (WithdrawalRequestPayee)initIterator.next();
        initPayee2.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_2, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", ORGANIZATION_NAME_2, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraChangedToDirectToParticipantWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        final Iterator<Payee> initIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        final WithdrawalRequestPayee initPayee1 = (WithdrawalRequestPayee)initIterator.next();
        final WithdrawalRequestPayee initPayee2 = (WithdrawalRequestPayee)initIterator.next();
        initPayee2.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_2, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", ORGANIZATION_NAME_2, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraChangedToDirectToParticipantWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        final Iterator<Payee> initIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        final WithdrawalRequestPayee initPayee1 = (WithdrawalRequestPayee)initIterator.next();
        final WithdrawalRequestPayee initPayee2 = (WithdrawalRequestPayee)initIterator.next();
        initPayee2.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_2, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", ORGANIZATION_NAME_2, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraChangedToDirectToParticipantWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        final Iterator<Payee> initIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        final WithdrawalRequestPayee initPayee1 = (WithdrawalRequestPayee)initIterator.next();
        final WithdrawalRequestPayee initPayee2 = (WithdrawalRequestPayee)initIterator.next();
        initPayee2.setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_2, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.", null, payee1.getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", null, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraChangedToRolloverToIraWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_IRA_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee payment method should be set correctly.",
                WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", ORGANIZATION_NAME_1, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraChangedToRolloverToIraWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_IRA_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee payment method should be set correctly.",
                WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", ORGANIZATION_NAME_1, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraChangedToRolloverToIraWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_IRA_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee payment method should be set correctly.",
                WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", ORGANIZATION_NAME_1, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraChangedToRolloverToIraWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_IRA_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee payment method should be set correctly.", null, payee1
                .getPaymentMethodCode());
        assertEquals("Payee organization name should be set properly.", null, payee1
                .getOrganizationName());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraChangedToRolloverToPlan() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_PLAN_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraChangedToAfterTaxContributionRemainderToPlanWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        final Iterator<Payee> initIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        final WithdrawalRequestPayee initPayee1 = (WithdrawalRequestPayee)initIterator.next();
        final WithdrawalRequestPayee initPayee2 = (WithdrawalRequestPayee)initIterator.next();
        initPayee2.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraChangedToAfterTaxContributionRemainderToPlanWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        final Iterator<Payee> initIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        final WithdrawalRequestPayee initPayee1 = (WithdrawalRequestPayee)initIterator.next();
        final WithdrawalRequestPayee initPayee2 = (WithdrawalRequestPayee)initIterator.next();
        initPayee2.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraChangedToAfterTaxContributionRemainderToPlanWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        final Iterator<Payee> initIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        final WithdrawalRequestPayee initPayee1 = (WithdrawalRequestPayee)initIterator.next();
        final WithdrawalRequestPayee initPayee2 = (WithdrawalRequestPayee)initIterator.next();
        initPayee2.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraChangedToAfterTaxContributionRemainderToPlanWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIraChangedToPlanTrustee() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT);
        request
                .setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", PLAN_TRUSTEE_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", TRUSTEE_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", TRUSTEE_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeUnchanged() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", PLAN_TRUSTEE_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", DEFAULT_ADDRESS_1, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", TRUSTEE_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeChangedToDirectToParticipantWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeChangedToDirectToParticipantWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeChangedToDirectToParticipantWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.",
                WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, payee1.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeChangedToDirectToParticipantWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", DIRECT_TO_PARTICIPANT_PAYEE_COUNT,
                request.getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", PARTICIPANT_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payment method should be set correctly.", null, payee1.getPaymentMethodCode());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeChangedToRolloverToIra() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", PLAN_TRUSTEE_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeChangedToRolloverToPlan() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.", ROLLOVER_TO_PLAN_PAYEE_COUNT, request
                .getRecipients().iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeChangedToAfterTaxContributionRemainderToIraWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeChangedToAfterTaxContributionRemainderToIraWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeChangedToAfterTaxContributionRemainderToIraWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeChangedToAfterTaxContributionRemainderToIraWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeChangedToAfterTaxContributionRemainderToPlanWithCheckPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeChangedToAfterTaxContributionRemainderToPlanWithWirePaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeChangedToAfterTaxContributionRemainderToPlanWithAchPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }

    /**
     * Tests the logical flow for setting the default address and dynamic default address (used on
     * page) based on the Payment To value.
     */
    @Test
    public void testPaymentToPlanTrusteeChangedToAfterTaxContributionRemainderToPlanWithNoPaymentMethod() {

        // Set up default object
        final WithdrawalRequest request = getBaseWithdrawalRequest(PLAN_TRUSTEE_PAYEE_COUNT);
        request.setOriginalPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setPaymentMethodCode(null);

        // Perform the update recipient
        WithdrawalRequestHelper.updateRecipients(request);

        // Verify the number of payees
        assertEquals("Should have correct number of payees.",
                AFTERTAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_COUNT, request.getRecipients()
                        .iterator().next().getPayees().size());

        final Iterator<Payee> iterator = request.getRecipients().iterator().next()
                .getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        // Verify payee addresses
        assertEquals("Payee 1 address should be set properly.", BLANK_ADDRESS, payee1.getAddress());
        assertEquals("Default payee 1 address should be set properly.", BLANK_ADDRESS, payee1
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_1, payee1.getPayeeNo());
        assertEquals("Payee 2 address should be set properly.", BLANK_ADDRESS, payee2.getAddress());
        assertEquals("Default payee 2 address should be set properly.", PARTICIPANT_ADDRESS, payee2
                .getDefaultAddress());
        assertEquals("Payee no should be set properly.", PAYEE_NO_2, payee2.getPayeeNo());
    }
}
