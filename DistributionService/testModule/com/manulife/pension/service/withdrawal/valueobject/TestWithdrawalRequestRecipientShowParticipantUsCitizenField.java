package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * Tests the rules of the show participant US citizen field.
 * 
 * @author Paul Glenn
 */
public class TestWithdrawalRequestRecipientShowParticipantUsCitizenField {

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
            payees.add(new WithdrawalRequestPayee());
        }

        // Create recipient
        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();

        recipient.setPayees(payees);
        final Collection<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(recipient);
        request.setRecipients(recipients);

        return request;
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is outside of the U.S. and the Payment To is Direct to Participant.
     */
    @Test
    public void testStateOfResidenceOutsideUsAndPaymentToDirectToParticipant() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode(
                WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US);
        setPayeeCountries(request, "MEX");

        assertTrue("Participant US citizen field should be displayed.", ((WithdrawalRequestRecipient)request.getRecipients()
                .iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is inside of the U.S. and the Payment To is Direct to Participant.
     */
    @Test
    public void testStateOfResidenceInsideUsAndPaymentToDirectToParticipant() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode("CA");
        setPayeeCountries(request, "MEX");

        assertTrue("Participant US citizen field should be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is outside of the U.S. and the Payment To is Rollover to IRA.
     */
    @Test
    public void testStateOfResidenceOutsideUsAndPaymentToRolloverToIra() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode(
                WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US);
        setPayeeCountries(request, "MEX");

        assertTrue("Participant US citizen field should be displayed.", ((WithdrawalRequestRecipient)request.getRecipients()
                .iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is inside of the U.S. and the Payment To is Rollover to IRA.
     */
    @Test
    public void testStateOfResidenceInsideUsAndPaymentToRolloverToIra() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode("CA");
        setPayeeCountries(request, "MEX");

        assertTrue("Participant US citizen field should be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is outside of the U.S. and the Payment To is Rollover to Plan.
     */
    @Test
    public void testStateOfResidenceOutsideUsAndPaymentToRolloverToPlan() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode(
                WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US);
        setPayeeCountries(request, "MEX");

        assertTrue("Participant US citizen field should be displayed.", ((WithdrawalRequestRecipient)request.getRecipients()
                .iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is inside of the U.S. and the Payment To is Rollover to Plan.
     */
    @Test
    public void testStateOfResidenceInsideUsAndPaymentToRolloverToPlan() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode("CA");
        setPayeeCountries(request, "MEX");

        assertTrue("Participant US citizen field should be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is outside of the U.S. and the Payment To is After-tax contribution remainder to
     * IRA.
     */
    @Test
    public void testStateOfResidenceOutsideUsAndPaymentToAfterTaxContributionRemainderToIra() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode(
                WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US);
        setPayeeCountries(request, "MEX");

        assertTrue("Participant US citizen field should be displayed.", ((WithdrawalRequestRecipient)request.getRecipients()
                .iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is inside of the U.S. and the Payment To is After-tax contribution remainder to
     * IRA.
     */
    @Test
    public void testStateOfResidenceInsideUsAndPaymentToAfterTaxContributionRemainderToIra() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode("CA");
        setPayeeCountries(request, "MEX");

        assertTrue("Participant US citizen field should be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is outside of the U.S. and the Payment To is After-tax contribution remainder to
     * Plan.
     */
    @Test
    public void testStateOfResidenceOutsideUsAndPaymentToAfterTaxContributionRemainderToPlan() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode(
                WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US);
        setPayeeCountries(request, "MEX");

        assertTrue("Participant US citizen field should be displayed.", ((WithdrawalRequestRecipient)request.getRecipients()
                .iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is inside of the U.S. and the Payment To is After-tax contribution remainder to
     * Plan.
     */
    @Test
    public void testStateOfResidenceInsideUsAndPaymentToAfterTaxContributionRemainderToPlan() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode("CA");
        setPayeeCountries(request, "MEX");

        assertTrue("Participant US citizen field should be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is outside of the U.S. and the Payment To is Plan Trustee.
     */
    @Test
    public void testStateOfResidenceOutsideUsAndPaymentToPlanTrustee() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode(
                WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US);
        setPayeeCountries(request, "MEX");

        assertFalse("Participant US citizen field should not be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is inside of the U.S. and the Payment To is Plan Trustee.
     */
    @Test
    public void testStateOfResidenceInsideUsAndPaymentToPlanTrustee() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode("CA");

        assertFalse("Participant US citizen field should not be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is outside of the U.S. and the Payment To is Direct to Participant.
     */
    @Test
    public void testStateOfResidenceOutsideUsAndPaymentToDirectToParticipantAndPayeeCountryIsUs() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode(
                WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US);
        setPayeeCountries(request, GlobalConstants.COUNTRY_CODE_USA);

        assertFalse("Participant US citizen field should not be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is inside of the U.S. and the Payment To is Direct to Participant.
     */
    @Test
    public void testStateOfResidenceInsideUsAndPaymentToDirectToParticipantAndPayeeCountryIsUs() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode("CA");
        setPayeeCountries(request, GlobalConstants.COUNTRY_CODE_USA);

        assertFalse("Participant US citizen field should not be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is outside of the U.S. and the Payment To is Rollover to IRA.
     */
    @Test
    public void testStateOfResidenceOutsideUsAndPaymentToRolloverToIraAndPayeeCountryIsUs() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode(
                WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US);
        setPayeeCountries(request, GlobalConstants.COUNTRY_CODE_USA);

        assertFalse("Participant US citizen field should not be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is inside of the U.S. and the Payment To is Rollover to IRA.
     */
    @Test
    public void testStateOfResidenceInsideUsAndPaymentToRolloverToIraAndPayeeCountryIsUs() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode("CA");
        setPayeeCountries(request, GlobalConstants.COUNTRY_CODE_USA);

        assertFalse("Participant US citizen field should not be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is outside of the U.S. and the Payment To is Rollover to Plan.
     */
    @Test
    public void testStateOfResidenceOutsideUsAndPaymentToRolloverToPlanAndPayeeCountryIsUs() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode(
                WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US);
        setPayeeCountries(request, GlobalConstants.COUNTRY_CODE_USA);

        assertFalse("Participant US citizen field should not be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is inside of the U.S. and the Payment To is Rollover to Plan.
     */
    @Test
    public void testStateOfResidenceInsideUsAndPaymentToRolloverToPlanAndPayeeCountryIsUs() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode("CA");
        setPayeeCountries(request, GlobalConstants.COUNTRY_CODE_USA);

        assertFalse("Participant US citizen field should not be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is outside of the U.S. and the Payment To is After-tax contribution remainder to
     * IRA.
     */
    @Test
    public void testStateOfResidenceOutsideUsAndPaymentToAfterTaxContributionRemainderToIraAndPayeeCountryIsUs() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode(
                WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US);
        setPayeeCountries(request, GlobalConstants.COUNTRY_CODE_USA);

        assertFalse("Participant US citizen field should not be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is inside of the U.S. and the Payment To is After-tax contribution remainder to
     * IRA.
     */
    @Test
    public void testStateOfResidenceInsideUsAndPaymentToAfterTaxContributionRemainderToIraAndPayeeCountryIsUs() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode("CA");
        setPayeeCountries(request, GlobalConstants.COUNTRY_CODE_USA);

        assertFalse("Participant US citizen field should not be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is outside of the U.S. and the Payment To is After-tax contribution remainder to
     * Plan.
     */
    @Test
    public void testStateOfResidenceOutsideUsAndPaymentToAfterTaxContributionRemainderToPlanAndPayeeCountryIsUs() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode(
                WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US);
        setPayeeCountries(request, GlobalConstants.COUNTRY_CODE_USA);

        assertFalse("Participant US citizen field should not be displayed.",((WithdrawalRequestRecipient) request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is inside of the U.S. and the Payment To is After-tax contribution remainder to
     * Plan.
     */
    @Test
    public void testStateOfResidenceInsideUsAndPaymentToAfterTaxContributionRemainderToPlanAndPayeeCountryIsUs() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        request
                .setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode("CA");
        setPayeeCountries(request, GlobalConstants.COUNTRY_CODE_USA);

        assertFalse("Participant US citizen field should not be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is outside of the U.S. and the Payment To is Plan Trustee.
     */
    @Test
    public void testStateOfResidenceOutsideUsAndPaymentToPlanTrusteeAndPayeeCountryIsUs() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode(
                WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US);
        setPayeeCountries(request, GlobalConstants.COUNTRY_CODE_USA);

        assertFalse("Participant US citizen field should not be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Tests that the participant US citizen field suppression works correctly when the state of
     * residence is inside of the U.S. and the Payment To is Plan Trustee.
     */
    @Test
    public void testStateOfResidenceInsideUsAndPaymentToPlanTrusteeAndPayeeCountryIsUs() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.getRecipients().iterator().next().setStateOfResidenceCode("CA");
        setPayeeCountries(request, GlobalConstants.COUNTRY_CODE_USA);

        assertFalse("Participant US citizen field should not be displayed.", ((WithdrawalRequestRecipient)request
                .getRecipients().iterator().next()).getShowParticipantUsCitizenField(request));
    }

    /**
     * Sets PayeeCountries to the given value.
     * 
     * @param withdrawalRequest The request to use.
     * @param countryCode The country code to set.
     */
    private static void setPayeeCountries(final WithdrawalRequest withdrawalRequest,
            final String countryCode) {
        for (Recipient withdrawalRequestRecipient : withdrawalRequest
                .getRecipients()) {
            for (Payee withdrawalRequestPayee : withdrawalRequestRecipient
                    .getPayees()) {
                withdrawalRequestPayee.getAddress().setCountryCode(countryCode);
            } // end for
        } // end for
    }

    /**
     * Creates a suite of Junit 4 tests.
     * 
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(
                TestWithdrawalRequestRecipientShowParticipantUsCitizenField.class);
    }
}
