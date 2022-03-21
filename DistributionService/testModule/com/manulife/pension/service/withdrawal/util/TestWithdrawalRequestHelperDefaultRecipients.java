package com.manulife.pension.service.withdrawal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import junit.framework.JUnit4TestAdapter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * Tests the default recipients method on the withdrawal helper utility.
 * 
 * @author Andrew Dick
 */
public class TestWithdrawalRequestHelperDefaultRecipients {

    private static final String STATE_OF_RESIDENCE = "CA";

    private static final String FIRST_NAME = "Fox";

    private static final String LAST_NAME = "Mulder";

    /**
     * Retrieves a basic address for testing.
     */
    private Address getDefaultAddress() {

        final Address address = new Address();
        address.setAddressLine1("Address Line 1");
        address.setAddressLine2("Address Line 2");
        address.setCity("City");
        address.setZipCode("123456789");
        address.setStateCode("CA");
        address.setCountryCode("USA");

        return address;
    }

    /**
     * Retrieves a blank address with default country for testing.
     */
    private Address getBlankAddress() {

        final Address address = new Address();
        address.setCountryCode(Address.USA_COUNTRY_CODE);

        return address;
    }

    /**
     * Tests default recipients method works correctly for payment to type Direct to Participant.
     */
    @Test
    public void testPaymentToDirectToParticipant() {

        // Create base request object
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);

        // Generate default recipient collection
        WithdrawalRequestHelper.populateDefaultRecipient(request);

        // Verify one recipient
        assertFalse("Recipient collection should not be empty.", CollectionUtils.isEmpty(request
                .getRecipients()));
        assertEquals("Recipient collection should contain one recipient.", 1, request
                .getRecipients().size());
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();

        // Verify one payee
        assertFalse("Payee collection should not be empty.", CollectionUtils.isEmpty(recipient
                .getPayees()));
        assertEquals("Payee collection should contain one payee.", 1, recipient.getPayees().size());

        // TODO: Verify addresses... etc... need to populate other data above as necessary
    }

    /**
     * Tests default recipients method works correctly for payment to type Rollover to IRA.
     */
    @Test
    public void testPaymentToRolloverToIra() {

        // Create base request object
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);

        // Generate default recipient collection
        WithdrawalRequestHelper.populateDefaultRecipient(request);

        // Verify one recipient
        assertFalse("Recipient collection should not be empty.", CollectionUtils.isEmpty(request
                .getRecipients()));
        assertEquals("Recipient collection should contain one recipient.", 1, request
                .getRecipients().size());
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();

        // Verify one payee
        assertFalse("Payee collection should not be empty.", CollectionUtils.isEmpty(recipient
                .getPayees()));
        assertEquals("Payee collection should contain one payee.", 1, recipient.getPayees().size());

        // TODO: Verify addresses... etc... need to populate other data above as necessary
    }

    /**
     * Tests default recipients method works correctly for payment to type Rollover to plan.
     */
    @Test
    public void testPaymentToRolloverToPlan() {

        // Create base request object
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);

        // Generate default recipient collection
        WithdrawalRequestHelper.populateDefaultRecipient(request);

        // Verify one recipient
        assertFalse("Recipient collection should not be empty.", CollectionUtils.isEmpty(request
                .getRecipients()));
        assertEquals("Recipient collection should contain one recipient.", 1, request
                .getRecipients().size());
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();

        // Verify one payee
        assertFalse("Payee collection should not be empty.", CollectionUtils.isEmpty(recipient
                .getPayees()));
        assertEquals("Payee collection should contain one payee.", 1, recipient.getPayees().size());

        // TODO: Verify addresses... etc... need to populate other data above as necessary
    }

    /**
     * Tests default recipients method works correctly for payment to type After-tax contribution
     * remainder to IRA.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToIra() {

        // Create base request object
        final WithdrawalRequest request = new WithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);

        // Generate default recipient collection
        WithdrawalRequestHelper.populateDefaultRecipient(request);

        // Verify one recipient
        assertFalse("Recipient collection should not be empty.", CollectionUtils.isEmpty(request
                .getRecipients()));
        assertEquals("Recipient collection should contain one recipient.", 1, request
                .getRecipients().size());
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();

        // Verify one payee
        assertFalse("Payee collection should not be empty.", CollectionUtils.isEmpty(recipient
                .getPayees()));
        assertEquals("Payee collection should contain two payees.", 2, recipient.getPayees().size());

        // TODO: Verify addresses... etc... need to populate other data above as necessary
    }

    /**
     * Tests default recipients method works correctly for payment to type After-tax contribution
     * remainder to plan.
     */
    @Test
    public void testPaymentToAfterTaxContributionRemainderToPlan() {

        // Create base request object
        final WithdrawalRequest request = new WithdrawalRequest();
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);

        // Generate default recipient collection
        WithdrawalRequestHelper.populateDefaultRecipient(request);

        // Verify one recipient
        assertFalse("Recipient collection should not be empty.", CollectionUtils.isEmpty(request
                .getRecipients()));
        assertEquals("Recipient collection should contain one recipient.", 1, request
                .getRecipients().size());
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();

        // Verify one payee
        assertFalse("Payee collection should not be empty.", CollectionUtils.isEmpty(recipient
                .getPayees()));
        assertEquals("Payee collection should contain two payees.", 2, recipient.getPayees().size());

        // TODO: Verify addresses... etc... need to populate other data above as necessary
    }

    /**
     * Tests default recipients method works correctly for payment to type Plan Trustee.
     */
    @Test
    public void testPaymentToPlanTrustee() {

        // Create base request object
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);

        // Generate default recipient collection
        WithdrawalRequestHelper.populateDefaultRecipient(request);

        // Verify one recipient
        assertFalse("Recipient collection should not be empty.", CollectionUtils.isEmpty(request
                .getRecipients()));
        assertEquals("Recipient collection should contain one recipient.", 1, request
                .getRecipients().size());
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();

        // Verify one payee
        assertFalse("Payee collection should not be empty.", CollectionUtils.isEmpty(recipient
                .getPayees()));
        assertEquals("Payee collection should contain one payee.", 1, recipient.getPayees().size());

        // TODO: Verify addresses... etc... need to populate other data above as necessary
    }

    /**
     * Tests default recipients method sets the default recipient address with a robust withdrawal
     * reason with a valid country.
     */
    @Test
    public void testDefaultRecipientAddressWithRobustWithdrawalReasonWithValidCountry() {

        // Create base request object
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantAddress(getDefaultAddress());

        // Generate default recipient collection
        WithdrawalRequestHelper.populateDefaultRecipient(request);

        // Verify one recipient
        assertFalse("Recipient collection should not be empty.", CollectionUtils.isEmpty(request
                .getRecipients()));
        assertEquals("Recipient collection should contain one recipient.", 1, request
                .getRecipients().size());
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();

        // Verify that default address has been used
        assertEquals("Default address should be used with non-simple withdrawal reasons.",
                getDefaultAddress(), recipient.getAddress());
    }

    /**
     * Tests default recipients method copies state of residence to created recipient.
     */
    @Test
    public void testRecipientStateOfResidence() {

        // Create base request object
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setParticipantStateOfResidence(STATE_OF_RESIDENCE);

        // Generate default recipient collection
        WithdrawalRequestHelper.populateDefaultRecipient(request);

        // Verify one recipient
        assertFalse("Recipient collection should not be empty.", CollectionUtils.isEmpty(request
                .getRecipients()));
        assertEquals("Recipient collection should contain one recipient.", 1, request
                .getRecipients().size());
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();

        assertEquals("State of residence should be copied.", STATE_OF_RESIDENCE, recipient
                .getStateOfResidenceCode());
    }

    /**
     * Tests default recipients method copies participant name to created recipient.
     */
    @Test
    public void testRecipientParticipantName() {

        // Create base request object
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.setFirstName(FIRST_NAME);
        request.setLastName(LAST_NAME);

        // Generate default recipient collection
        WithdrawalRequestHelper.populateDefaultRecipient(request);

        // Verify one recipient
        assertFalse("Recipient collection should not be empty.", CollectionUtils.isEmpty(request
                .getRecipients()));
        assertEquals("Recipient collection should contain one recipient.", 1, request
                .getRecipients().size());
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();

        assertEquals("First name should be copied.", FIRST_NAME, recipient.getFirstName());
        assertEquals("Last name should be copied.", LAST_NAME, recipient.getLastName());
    }

    /**
     * Creates a suite of Junit 4 tests.
     * 
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestWithdrawalRequestHelperDefaultRecipients.class);
    }
}
