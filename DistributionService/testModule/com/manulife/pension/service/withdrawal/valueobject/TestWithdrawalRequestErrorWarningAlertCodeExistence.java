package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.JUnit4TestAdapter;

import org.apache.commons.collections.collection.UnmodifiableCollection;
import org.junit.Before;
import org.junit.Test;

import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessage;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;

/**
 * Tests the functionality of the {@link WithdrawalRequest} class error, warning and alert codes.
 * 
 * @author Andrew Dick
 */
public class TestWithdrawalRequestErrorWarningAlertCodeExistence {

    private static final Collection<String> EMPTY_STRING_COLLECTION = (Collection<String>) UnmodifiableCollection
            .decorate(new ArrayList<String>());

    private WithdrawalRequest withdrawalRequest;

    /**
     * Creates a base withdrawal request object.
     */
    @Before
    public void setUp() throws Exception {

        // Create withdrawal request and populate it with all sub-items
        withdrawalRequest = new WithdrawalRequest();

        // Create notes
        withdrawalRequest.setCurrentAdminToAdminNote(new WithdrawalRequestNote());
        withdrawalRequest.setCurrentAdminToParticipantNote(new WithdrawalRequestNote());

        // Create declarations
        final Collection<Declaration> declarations = new ArrayList<Declaration>();
        declarations.add(new WithdrawalRequestDeclaration());
        declarations.add(new WithdrawalRequestDeclaration());
        withdrawalRequest.setDeclarations(declarations);

        // Create money types
        final Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>();
        moneyTypes.add(new WithdrawalRequestMoneyType());
        moneyTypes.add(new WithdrawalRequestMoneyType());
        withdrawalRequest.setMoneyTypes(moneyTypes);

        // Create fees
        final Collection<Fee> fees = new ArrayList<Fee>();
        fees.add(new WithdrawalRequestFee());
        fees.add(new WithdrawalRequestFee());
        withdrawalRequest.setFees(fees);

        // Create recipients, payees and instructions
        final Collection<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(getBaseWithdrawalRecipient());
        recipients.add(getBaseWithdrawalRecipient());
        withdrawalRequest.setRecipients(recipients);

        // Create loans
        final Collection<WithdrawalRequestLoan> loans = new ArrayList<WithdrawalRequestLoan>();
        loans.add(new WithdrawalRequestLoan());
        loans.add(new WithdrawalRequestLoan());
        withdrawalRequest.setLoans(loans);
    }

    /**
     * Creates a base recipient object populated with payees and instructions.
     * 
     * @return WithdrawalRequestRecipient A base recipient object.
     */
    private WithdrawalRequestRecipient getBaseWithdrawalRecipient() {

        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();

        // Create payees
        final Collection<Payee> payees = new ArrayList<Payee>();
        payees.add(getBaseWithdrawalPayee());
        payees.add(getBaseWithdrawalPayee());
        recipient.setPayees(payees);

        return recipient;
    }

    /**
     * Creates a base payee object populated with instructions.
     * 
     * @return WithdrawalRequestPayee A base payee object.
     */
    private WithdrawalRequestPayee getBaseWithdrawalPayee() {

        final WithdrawalRequestPayee payee = new WithdrawalRequestPayee();
        payee.setPaymentInstruction(new PayeePaymentInstruction());
        return payee;
    }

    /**
     * Tests that an object graph with zero errors correctly determines there are no errors.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithZeroErrors() {

        // No errors to setup for this test

        // Verify that error codes query returns expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an withdrawal request error correctly determines that errors
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithWithdrawalRequestError() {

        // Setup an error in the withdrawal request
        withdrawalRequest.getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an admin to admin note error correctly determines that errors
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithAdminToAdminNoteError() {

        // Setup an error in the admin to admin note
        withdrawalRequest.getCurrentAdminToAdminNote().getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an admin to participant note request error correctly
     * determines that errors exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithAdminToParticipantNoteError() {

        // Setup an error in the admin to participant note
        withdrawalRequest.getCurrentAdminToParticipantNote().getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an error on the first declaration correctly determines that
     * errors exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithFirstDeclarationError() {

        // Setup an error in the first declaration
        ((WithdrawalRequestDeclaration)withdrawalRequest.getDeclarations().iterator().next()).getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an error on the second declaration correctly determines that
     * errors exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithSecondDeclarationError() {

        // Setup an error in the second declaration
        final Iterator<Declaration> iterator = withdrawalRequest.getDeclarations()
                .iterator();
        iterator.next();
        ((WithdrawalRequestDeclaration)iterator.next()).getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an error on the first fee correctly determines that errors
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithFirstFeeError() {

        // Setup an error in the first fee
        ((WithdrawalRequestFee)withdrawalRequest.getFees().iterator().next()).getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an error on the second fee correctly determines that errors
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithSecondFeeError() {

        // Setup an error in the second fee
        final Iterator<Fee> iterator = withdrawalRequest.getFees().iterator();
        iterator.next();
		((WithdrawalRequestFee) iterator.next()).getErrorCodes().add(
				new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
						EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an error on the first moneyType correctly determines that
     * errors exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithFirstMoneyTypeError() {

        // Setup an error in the first moneyType
        withdrawalRequest.getMoneyTypes().iterator().next().getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an error on the second moneyType correctly determines that
     * errors exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithSecondMoneyTypeError() {

        // Setup an error in the second moneyType
        final Iterator<WithdrawalRequestMoneyType> iterator = withdrawalRequest.getMoneyTypes()
                .iterator();
        iterator.next();
        iterator.next().getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an error on the first loan correctly determines that errors
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithFirstLoanError() {

        // Setup an error in the first loan
        withdrawalRequest.getLoans().iterator().next().getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an error on the second loan correctly determines that errors
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithSecondLoanError() {

        // Setup an error in the second loan
        final Iterator<WithdrawalRequestLoan> iterator = withdrawalRequest.getLoans()
                .iterator();
        iterator.next();
        iterator.next().getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an error on the first recipient correctly determines that
     * errors exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithFirstRecipientError() {

        // Setup an error in the first recipient
        ((WithdrawalRequestRecipient)withdrawalRequest.getRecipients().iterator().next()).getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an error on the second recipient correctly determines that
     * errors exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithSecondRecipientError() {

        // Setup an error in the second recipient
        final Iterator<Recipient> iterator = withdrawalRequest.getRecipients()
                .iterator();
        iterator.next();
        ((WithdrawalRequestRecipient)iterator.next()).getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an error on the first payee correctly determines that errors
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithFirstPayeeError() {

        // Setup an error in the first payee
        ((WithdrawalRequestPayee)withdrawalRequest.getRecipients().iterator().next().getPayees().iterator().next())
                .getErrorCodes().add(
                        new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                                EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an error on the first payee address correctly determines that
     * errors exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithFirstPayeeAddressError() {

        // Setup an error in the first payee address
        ((Address)withdrawalRequest.getRecipients().iterator().next().getPayees().iterator().next()
                .getAddress()).getErrorCodes().add(
                        new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with a warning on the first payee address correctly determines
     * that warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithFirstPayeeAddressWarning() {

        // Setup a warning in the first payee address
        ((Address)withdrawalRequest.getRecipients().iterator().next().getPayees().iterator().next()
                .getAddress()).getWarningCodes().add(
                        new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the first payee address correctly determines that
     * alerts exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithFirstPayeeAddressAlert() {

        // Setup an alert in the first payee address
        ((Address)withdrawalRequest.getRecipients().iterator().next().getPayees().iterator().next()
                .getAddress()).getAlertCodes().add(
                        new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an error on the second payee correctly determines that errors
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithSecondPayeeError() {

        // Setup an error in the second payee
        final Iterator<Payee> iterator = withdrawalRequest.getRecipients()
                .iterator().next().getPayees().iterator();
        iterator.next();
        ((WithdrawalRequestPayee)iterator.next()).getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an error on the first payee instruction correctly determines
     * that errors exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithFirstPayeeInstructionError() {

        // Setup an error in the first payee
        ((PayeePaymentInstruction)withdrawalRequest.getRecipients().iterator().next().getPayees().iterator().next()
                .getPaymentInstruction()).getErrorCodes().add(
                        new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                                EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an error on the second payee instruction correctly determines
     * that errors exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithSecondPayeeInstructionError() {

        // Setup an error in the second payee
        final Iterator<Payee> iterator = withdrawalRequest.getRecipients()
                .iterator().next().getPayees().iterator();
        iterator.next();
        ((PayeePaymentInstruction)iterator.next().getPaymentInstruction()).getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        EMPTY_STRING_COLLECTION));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that warning and alert codes queries return expected result
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with zero warnings correctly determines there are no warnings.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithZeroWarnings() {

        // No warnings to setup for this test

        // Verify that warning codes query returns expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an withdrawal request warning correctly determines that
     * warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithWithdrawalRequestWarning() {

        // Setup an warning in the withdrawal request
        withdrawalRequest.getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an admin to admin note warning correctly determines that
     * warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithAdminToAdminNoteWarning() {

        // Setup an warning in the admin to admin note
        withdrawalRequest.getCurrentAdminToAdminNote().getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an admin to participant note request warning correctly
     * determines that warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithAdminToParticipantNoteWarning() {

        // Setup an warning in the admin to participant note
        withdrawalRequest.getCurrentAdminToParticipantNote().getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an warning on the first declaration correctly determines that
     * warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithFirstDeclarationWarning() {

        // Setup an warning in the first declaration
        ((WithdrawalRequestDeclaration)withdrawalRequest.getDeclarations().iterator().next()).getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an warning on the second declaration correctly determines
     * that warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithSecondDeclarationWarning() {

        // Setup an warning in the second declaration
        final Iterator<Declaration> iterator = withdrawalRequest.getDeclarations()
                .iterator();
        iterator.next();
        ((WithdrawalRequestDeclaration)iterator.next()).getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an warning on the first fee correctly determines that
     * warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithFirstFeeWarning() {

		// Setup an warning in the first fee
		((WithdrawalRequestFee) withdrawalRequest.getFees().iterator().next()).getWarningCodes()
				.add(new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an warning on the second fee correctly determines that
     * warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithSecondFeeWarning() {

        // Setup an warning in the second fee
        final Iterator<Fee> iterator = withdrawalRequest.getFees().iterator();
        iterator.next();
        ((WithdrawalRequestFee)iterator.next()).getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an warning on the first moneyType correctly determines that
     * warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithFirstMoneyTypeWarning() {

        // Setup an warning in the first moneyType
        withdrawalRequest.getMoneyTypes().iterator().next().getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an warning on the second moneyType correctly determines that
     * warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithSecondMoneyTypeWarning() {

        // Setup an warning in the second moneyType
        final Iterator<WithdrawalRequestMoneyType> iterator = withdrawalRequest.getMoneyTypes()
                .iterator();
        iterator.next();
        iterator.next().getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an warning on the first loan correctly determines that
     * warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithFirstLoanWarning() {

        // Setup an warning in the first loan
        withdrawalRequest.getLoans().iterator().next().getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an warning on the second loan correctly determines that
     * warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithSecondLoanWarning() {

        // Setup an warning in the second loan
        final Iterator<WithdrawalRequestLoan> iterator = withdrawalRequest.getLoans()
                .iterator();
        iterator.next();
        iterator.next().getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an warning on the first recipient correctly determines that
     * warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithFirstRecipientWarning() {

        // Setup an warning in the first recipient
        ((WithdrawalRequestRecipient)withdrawalRequest.getRecipients().iterator().next()).getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an warning on the second recipient correctly determines that
     * warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithSecondRecipientWarning() {

        // Setup an warning in the second recipient
        final Iterator<Recipient> iterator = withdrawalRequest.getRecipients()
                .iterator();
        iterator.next();
        ((WithdrawalRequestRecipient)iterator.next()).getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an warning on the first payee correctly determines that
     * warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithFirstPayeeWarning() {

        // Setup an warning in the first payee
        ((WithdrawalRequestPayee) withdrawalRequest.getRecipients().iterator().next().getPayees()
				.iterator().next()).getWarningCodes().add(
				new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an warning on the second payee correctly determines that
     * warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithSecondPayeeWarning() {

        // Setup an warning in the second payee
        final Iterator<Payee> iterator = withdrawalRequest.getRecipients()
                .iterator().next().getPayees().iterator();
        iterator.next();
        ((WithdrawalRequestPayee)iterator.next()).getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an warning on the first payee instruction correctly
     * determines that warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithFirstPayeeInstructionWarning() {

        // Setup an warning in the first payee
        ((PayeePaymentInstruction)withdrawalRequest.getRecipients().iterator().next().getPayees().iterator().next()
                .getPaymentInstruction()).getWarningCodes().add(
                        new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with an warning on the second payee instruction correctly
     * determines that warnings exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithSecondPayeeInstructionWarning() {

        // Setup an warning in the second payee
        final Iterator<Payee> iterator = withdrawalRequest.getRecipients()
                .iterator().next().getPayees().iterator();
        iterator.next();
        ((PayeePaymentInstruction)iterator.next().getPaymentInstruction()).getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and alert codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
    }

    /**
     * Tests that an object graph with zero alerts correctly determines there are no alerts.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithZeroAlerts() {

        // No alerts to setup for this test

        // Verify that alert codes query returns expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an withdrawal request alert correctly determines that alerts
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithWithdrawalRequestAlert() {

        // Setup an alert in the withdrawal request
        withdrawalRequest.getAlertCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an admin to admin note alert correctly determines that alerts
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithAdminToAdminNoteAlert() {

        // Setup an alert in the admin to admin note
        withdrawalRequest.getCurrentAdminToAdminNote().getAlertCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an admin to participant note request alert correctly
     * determines that alerts exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithAdminToParticipantNoteAlert() {

        // Setup an alert in the admin to participant note
        withdrawalRequest.getCurrentAdminToParticipantNote().getAlertCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the first declaration correctly determines that
     * alerts exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithFirstDeclarationAlert() {

        // Setup an alert in the first declaration
        ((WithdrawalRequestDeclaration)withdrawalRequest.getDeclarations().iterator().next()).getAlertCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the second declaration correctly determines that
     * alerts exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithSecondDeclarationAlert() {

        // Setup an alert in the second declaration
        final Iterator<Declaration> iterator = withdrawalRequest.getDeclarations()
                .iterator();
        iterator.next();
        ((WithdrawalRequestDeclaration)iterator.next()).getAlertCodes()
                .add(new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the first fee correctly determines that alerts
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithFirstFeeAlert() {

        // Setup an alert in the first fee
        ((WithdrawalRequestFee)withdrawalRequest.getFees().iterator().next()).getAlertCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the second fee correctly determines that alerts
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithSecondFeeAlert() {

        // Setup an alert in the second fee
        final Iterator<Fee> iterator = withdrawalRequest.getFees().iterator();
        iterator.next();
        ((WithdrawalRequestFee)iterator.next()).getAlertCodes()
                .add(new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the first moneyType correctly determines that
     * alerts exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithFirstMoneyTypeAlert() {

        // Setup an alert in the first moneyType
        withdrawalRequest.getMoneyTypes().iterator().next().getAlertCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the second moneyType correctly determines that
     * alerts exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithSecondMoneyTypeAlert() {

        // Setup an alert in the second moneyType
        final Iterator<WithdrawalRequestMoneyType> iterator = withdrawalRequest.getMoneyTypes()
                .iterator();
        iterator.next();
        iterator.next().getAlertCodes()
                .add(new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the first loan correctly determines that alerts
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithFirstLoanAlert() {

        // Setup an alert in the first loan
        withdrawalRequest.getLoans().iterator().next().getAlertCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the second loan correctly determines that alerts
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithSecondLoanAlert() {

        // Setup an alert in the second loan
        final Iterator<WithdrawalRequestLoan> iterator = withdrawalRequest.getLoans()
                .iterator();
        iterator.next();
        iterator.next().getAlertCodes()
                .add(new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the first recipient correctly determines that
     * alerts exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithFirstRecipientAlert() {

        // Setup an alert in the first recipient
        ((WithdrawalRequestRecipient)withdrawalRequest.getRecipients().iterator().next()).getAlertCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the second recipient correctly determines that
     * alerts exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithSecondRecipientAlert() {

        // Setup an alert in the second recipient
        final Iterator<Recipient> iterator = withdrawalRequest.getRecipients()
                .iterator();
        iterator.next();
        ((WithdrawalRequestRecipient)iterator.next()).getAlertCodes()
                .add(new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the first recipient address correctly determines
     * that alerts exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoErrorsExistWithFirstRecipientAddressError() {

        // Setup an error in the first recipient address 
        ((Address)withdrawalRequest.getRecipients().iterator().next().getAddress()).getErrorCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID));

        // Verify that error codes query returns expected result
        assertTrue("Error codes should exist.", withdrawalRequest.doErrorCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the first recipient address correctly determines
     * that alerts exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoWarningsExistWithFirstRecipientAddressWarning() {

        // Setup a warning in the first recipient address 
        ((Address)withdrawalRequest.getRecipients().iterator().next().getAddress()).getWarningCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING));

        // Verify that warning codes query returns expected result
        assertTrue("Warning codes should exist.", withdrawalRequest.doWarningCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No alert codes should exist.", withdrawalRequest.doAlertCodesExist());
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
    }


    /**
     * Tests that an object graph with an alert on the first recipient address correctly determines
     * that alerts exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithFirstRecipientAddressAlert() {

        // Setup an alert in the first recipient
        ((Address)withdrawalRequest.getRecipients().iterator().next().getAddress()).getAlertCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the first payee correctly determines that alerts
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithFirstPayeeAlert() {

        // Setup an alert in the first payee
        ((WithdrawalRequestPayee)withdrawalRequest.getRecipients().iterator().next().getPayees().iterator().next())
                .getAlertCodes().add(new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the second payee correctly determines that alerts
     * exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithSecondPayeeAlert() {

        // Setup an alert in the second payee
        final Iterator<Payee> iterator = withdrawalRequest.getRecipients()
                .iterator().next().getPayees().iterator();
        iterator.next();
        ((WithdrawalRequestPayee)iterator.next()).getAlertCodes()
                .add(new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the first payee instruction correctly determines
     * that alerts exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithFirstPayeeInstructionAlert() {

        // Setup an alert in the first payee
        ((PayeePaymentInstruction)withdrawalRequest.getRecipients().iterator().next().getPayees().iterator().next()
                .getPaymentInstruction()).getAlertCodes().add(
                        new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Tests that an object graph with an alert on the second payee instruction correctly determines
     * that alerts exist.
     * 
     * @throws Exception
     */
    @Test
    public void testDoAlertsExistWithSecondPayeeInstructionAlert() {

        // Setup an alert in the second payee
        final Iterator<Payee> iterator = withdrawalRequest.getRecipients()
                .iterator().next().getPayees().iterator();
        iterator.next();
        ((PayeePaymentInstruction)iterator.next().getPaymentInstruction()).getAlertCodes().add(
                new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT));

        // Verify that alert codes query returns expected result
        assertTrue("Alert codes should exist.", withdrawalRequest.doAlertCodesExist());

        // Verify that error and warning codes queries return expected result
        assertFalse("No error codes should exist.", withdrawalRequest.doErrorCodesExist());
        assertFalse("No warning codes should exist.", withdrawalRequest.doWarningCodesExist());
    }

    /**
     * Creates a suite of Junit 4 tests.
     * 
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestWithdrawalRequestErrorWarningAlertCodeExistence.class);
    }
}
