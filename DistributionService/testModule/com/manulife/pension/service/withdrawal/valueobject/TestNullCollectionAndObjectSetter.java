package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.JUnit4TestAdapter;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;

/**
 * Complex objects in the object graph are not expected to be null. If a null is set into the
 * objects, we expect that a default blank object, empty collection will be set instead.
 * 
 * @author Andrew Dick
 */
public class TestNullCollectionAndObjectSetter {

    /**
     * Tests the null setting of the recipient collection uses an empty collection.
     */
    @Test
    public void testNullRecipients() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setRecipients(null);
        assertNotNull("Collection should not be null.", request.getRecipients());
        assertTrue("Collection should be empty.", CollectionUtils.isEmpty(request.getRecipients()));
    }

    /**
     * Tests the non-null setting of the recipient collection uses the specified collection.
     */
    @Test
    public void testNonNullRecipients() {

        final Collection<Recipient> recipients = new ArrayList<Recipient>();
        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        recipient.setSubmissionId(1984);
        recipients.add(recipient);

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setRecipients(recipients);
        assertNotNull("Collection should not be null.", request.getRecipients());
        assertEquals("Collection should be the same as passed in.", recipients, request
                .getRecipients());
    }

    /**
     * Tests the null setting of the declaration collection uses an empty collection.
     */
    @Test
    public void testNullDeclarations() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setDeclarations(null);
        assertNotNull("Collection should not be null.", request.getDeclarations());
        assertTrue("Collection should be empty.", CollectionUtils
                .isEmpty(request.getDeclarations()));
    }

    /**
     * Tests the non-null setting of the declaration collection uses the specified collection.
     */
    @Test
    public void testNonNullDeclarations() {

        final Collection<Declaration> declarations = new ArrayList<Declaration>();
        final WithdrawalRequestDeclaration declaration = new WithdrawalRequestDeclaration();
        declaration.setSubmissionId(1984);
        declarations.add(declaration);

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setDeclarations(declarations);
        assertNotNull("Collection should not be null.", request.getDeclarations());
        assertEquals("Collection should be the same as passed in.", declarations, request
                .getDeclarations());
    }

    /**
     * Tests the null setting of the fee collection uses an empty collection.
     */
    @Test
    public void testNullFees() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setFees(null);
        assertNotNull("Collection should not be null.", request.getFees());
        assertTrue("Collection should be empty.", CollectionUtils.isEmpty(request.getFees()));
    }

    /**
     * Tests the non-null setting of the fee collection uses the specified collection.
     */
    @Test
    public void testNonNullFees() {

        final Collection<Fee> fees = new ArrayList<Fee>();
        final WithdrawalRequestFee fee = new WithdrawalRequestFee();
        fee.setSubmissionId(1984);
        fees.add(fee);

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setFees(fees);
        assertNotNull("Collection should not be null.", request.getFees());
        assertEquals("Collection should be the same as passed in.", fees, request.getFees());
    }

    /**
     * Tests the null setting of the moneyType collection uses an empty collection.
     */
    @Test
    public void testNullMoneyTypes() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setMoneyTypes(null);
        assertNotNull("Collection should not be null.", request.getMoneyTypes());
        assertTrue("Collection should be empty.", CollectionUtils.isEmpty(request.getMoneyTypes()));
    }

    /**
     * Tests the non-null setting of the moneyType collection uses the specified collection.
     */
    @Test
    public void testNonNullMoneyTypes() {

        final Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>();
        final WithdrawalRequestMoneyType moneyType = new WithdrawalRequestMoneyType();
        moneyType.setSubmissionId(1984);
        moneyTypes.add(moneyType);

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setMoneyTypes(moneyTypes);
        assertNotNull("Collection should not be null.", request.getMoneyTypes());
        assertEquals("Collection should be the same as passed in.", moneyTypes, request
                .getMoneyTypes());
    }

    /**
     * Tests the null setting of the loan collection uses an empty collection.
     */
    @Test
    public void testNullLoans() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setLoans(null);
        assertNotNull("Collection should not be null.", request.getLoans());
        assertTrue("Collection should be empty.", CollectionUtils.isEmpty(request.getLoans()));
    }

    /**
     * Tests the non-null setting of the loan collection uses the specified collection.
     */
    @Test
    public void testNonNullLoans() {

        final Collection<WithdrawalRequestLoan> loans = new ArrayList<WithdrawalRequestLoan>();
        final WithdrawalRequestLoan loan = new WithdrawalRequestLoan();
        loan.setSubmissionId(1984);
        loans.add(loan);

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setLoans(loans);
        assertNotNull("Collection should not be null.", request.getLoans());
        assertEquals("Collection should be the same as passed in.", loans, request.getLoans());
    }

    /**
     * Tests the null setting of the read only admin to admin notes collection uses an empty
     * collection.
     */
    @Test
    public void testNullReadOnlyAdminToAdminNotes() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReadOnlyAdminToAdminNotes(null);
        assertNotNull("Collection should not be null.", request.getReadOnlyAdminToAdminNotes());
        assertTrue("Collection should be empty.", CollectionUtils.isEmpty(request
                .getReadOnlyAdminToAdminNotes()));
    }

    /**
     * Tests the non-null setting of the read only admin to admin notes collection uses the
     * specified collection.
     */
    @Test
    public void testNonNullReadOnlyAdminToAdminNotes() {

        final Collection<WithdrawalRequestNote> notes = new ArrayList<WithdrawalRequestNote>();
        final WithdrawalRequestNote note = new WithdrawalRequestNote();
        note.setSubmissionId(1984);
        notes.add(note);

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReadOnlyAdminToAdminNotes(notes);
        assertNotNull("Collection should not be null.", request.getReadOnlyAdminToAdminNotes());
        assertEquals("Collection should be the same as passed in.", notes, request
                .getReadOnlyAdminToAdminNotes());
    }

    /**
     * Tests the null setting of the read only admin to participant notes collection uses an empty
     * collection.
     */
    @Test
    public void testNullReadOnlyAdminToParticipantNotes() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReadOnlyAdminToParticipantNotes(null);
        assertNotNull("Collection should not be null.", request
                .getReadOnlyAdminToParticipantNotes());
        assertTrue("Collection should be empty.", CollectionUtils.isEmpty(request
                .getReadOnlyAdminToParticipantNotes()));
    }

    /**
     * Tests the non-null setting of the read only admin to participant notes collection uses the
     * specified collection.
     */
    @Test
    public void testNonNullReadOnlyAdminToParticipantNotes() {

        final Collection<WithdrawalRequestNote> notes = new ArrayList<WithdrawalRequestNote>();
        final WithdrawalRequestNote note = new WithdrawalRequestNote();
        note.setSubmissionId(1984);
        notes.add(note);

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setReadOnlyAdminToParticipantNotes(notes);
        assertNotNull("Collection should not be null.", request
                .getReadOnlyAdminToParticipantNotes());
        assertEquals("Collection should be the same as passed in.", notes, request
                .getReadOnlyAdminToParticipantNotes());
    }

    /**
     * Tests the null setting of the payee collection uses an empty collection.
     */
    @Test
    public void testNullPayees() {

        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        recipient.setPayees(null);
        assertNotNull("Collection should not be null.", recipient.getPayees());
        assertTrue("Collection should be empty.", CollectionUtils.isEmpty(recipient.getPayees()));
    }

    /**
     * Tests the non-null setting of the payee collection uses the specified collection.
     */
    @Test
    public void testNonNullPayees() {

        final Collection<Payee> payees = new ArrayList<Payee>();
        final WithdrawalRequestPayee payee = new WithdrawalRequestPayee();
        payee.setSubmissionId(1984);
        payees.add(payee);

        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        recipient.setPayees(payees);
        assertNotNull("Collection should not be null.", recipient.getPayees());
        assertEquals("Collection should be the same as passed in.", payees, recipient.getPayees());
    }

    /**
     * Tests the null setting of the withdrawal info money types collection uses an empty
     * collection.
     */
    @Test
    public void testNullWithdrawalInfoMoneyTypes() {

        final ParticipantInfo info = new ParticipantInfo();
        info.setMoneyTypes(null);
        assertNotNull("Collection should not be null.", info.getMoneyTypes());
        assertTrue("Collection should be empty.", CollectionUtils.isEmpty(info.getMoneyTypes()));
    }

    /**
     * Tests the non-null setting of the withdrawal info money types collection uses the specified
     * collection.
     */
    @Test
    public void testNonNullWithdrawalInfoMoneyTypes() {

        final Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>();
        final WithdrawalRequestMoneyType moneyType = new WithdrawalRequestMoneyType();
        moneyType.setSubmissionId(1984);
        moneyTypes.add(moneyType);

        final ParticipantInfo info = new ParticipantInfo();
        info.setMoneyTypes(moneyTypes);
        assertNotNull("Collection should not be null.", info.getMoneyTypes());
        assertEquals("Collection should be the same as passed in.", moneyTypes, info
                .getMoneyTypes());
    }

    /**
     * Tests the null setting of the admin to admin note.
     */
    @Test
    public void testNullAdminToAdminNote() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setCurrentAdminToAdminNote(null);
        assertNotNull("Object should not be null.", request.getCurrentAdminToAdminNote());
        assertEquals("Note should be an admin to admin note.",
                WithdrawalRequestNote.ADMIN_TO_ADMIN_TYPE_CODE, request
                        .getCurrentAdminToAdminNote().getNoteTypeCode());
    }

    /**
     * Tests the non-null setting of the admin to admin note without the type code set.
     */
    @Test
    public void testNonNullAdminToAdminNoteWithoutTypeCode() {

        final WithdrawalRequestNote note = new WithdrawalRequestNote();
        note.setSubmissionId(1984);

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setCurrentAdminToAdminNote(note);
        assertNotNull("Object should not be null.", request.getCurrentAdminToAdminNote());
        assertEquals("Note should be an admin to admin note.",
                WithdrawalRequestNote.ADMIN_TO_ADMIN_TYPE_CODE, request
                        .getCurrentAdminToAdminNote().getNoteTypeCode());
        assertEquals("Existing value should not be changed.", new Integer(1984), request
                .getCurrentAdminToAdminNote().getSubmissionId());
    }

    /**
     * Tests the non-null setting of the admin to admin note with the type code set.
     */
    @Test
    public void testNonNullAdminToAdminNoteWithTypeCode() {

        final WithdrawalRequestNote note = new WithdrawalRequestNote();
        note.setSubmissionId(1984);
        note.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_ADMIN_TYPE_CODE);

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setCurrentAdminToAdminNote(note);
        assertNotNull("Object should not be null.", request.getCurrentAdminToAdminNote());
        assertEquals("Note should be an admin to admin note.",
                WithdrawalRequestNote.ADMIN_TO_ADMIN_TYPE_CODE, request
                        .getCurrentAdminToAdminNote().getNoteTypeCode());
        assertEquals("Note should be the same as passed in.", note, request
                .getCurrentAdminToAdminNote());
    }

    /**
     * Tests the null setting of the admin to participant note.
     */
    @Test
    public void testNullAdminToParticipantNote() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setCurrentAdminToParticipantNote(null);
        assertNotNull("Object should not be null.", request.getCurrentAdminToParticipantNote());
        assertEquals("Note should be an admin to participant note.",
                WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE, request
                        .getCurrentAdminToParticipantNote().getNoteTypeCode());
    }

    /**
     * Tests the non-null setting of the admin to participant note without the type code set.
     */
    @Test
    public void testNonNullAdminToParticipantNoteWithoutTypeCode() {

        final WithdrawalRequestNote note = new WithdrawalRequestNote();
        note.setSubmissionId(1984);

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setCurrentAdminToParticipantNote(note);
        assertNotNull("Object should not be null.", request.getCurrentAdminToParticipantNote());
        assertEquals("Note should be an admin to participant note.",
                WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE, request
                        .getCurrentAdminToParticipantNote().getNoteTypeCode());
        //assertEquals("Existing value should not be changed.", 1984, request
                //.getCurrentAdminToParticipantNote().getSubmissionId());
    }

    /**
     * Tests the non-null setting of the admin to participant note with the type code set.
     */
    @Test
    public void testNonNullAdminToParticipantNoteWithTypeCode() {

        final WithdrawalRequestNote note = new WithdrawalRequestNote();
        note.setSubmissionId(1984);
        note.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE);

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setCurrentAdminToParticipantNote(note);
        assertNotNull("Object should not be null.", request.getCurrentAdminToParticipantNote());
        assertEquals("Note should be an admin to participant note.",
                WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE, request
                        .getCurrentAdminToParticipantNote().getNoteTypeCode());
        assertEquals("Note should be the same as passed in.", note, request
                .getCurrentAdminToParticipantNote());
    }

    /**
     * Tests the null setting of the trustee address.
     */
    @Test
    public void testNullTrusteeAddress() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setTrusteeAddress(null);
        assertNotNull("Object should not be null.", request.getTrusteeAddress());
    }

    /**
     * Tests the non-null setting of the trustee address.
     */
    @Test
    public void testNonNullTrusteeAddress() {

        final Address address = new Address();
        address.setSubmissionId(1984);

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setTrusteeAddress(address);
        assertNotNull("Object should not be null.", request.getTrusteeAddress());
        assertEquals("Object should be the same as set.", address, request.getTrusteeAddress());
    }

    /**
     * Tests the null setting of the participant address.
     */
    @Test
    public void testNullParticipantAddress() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setParticipantAddress(null);
        assertNotNull("Object should not be null.", request.getParticipantAddress());
    }

    /**
     * Tests the non-null setting of the participant address.
     */
    @Test
    public void testNonNullParticipantAddress() {

        final Address address = new Address();
        address.setSubmissionId(1984);

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setParticipantAddress(address);
        assertNotNull("Object should not be null.", request.getParticipantAddress());
        assertEquals("Object should be the same as set.", address, request.getParticipantAddress());
    }

    /**
     * Tests the null setting of the recipient address.
     */
    @Test
    public void testNullRecipientAddress() {

        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        recipient.setAddress(null);
        assertNotNull("Object should not be null.", recipient.getAddress());
    }

    /**
     * Tests the non-null setting of the recipient address.
     */
    @Test
    public void testNonNullRecipientAddress() {

        final Address address = new Address();
        address.setSubmissionId(1984);

        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        recipient.setAddress(address);
        assertNotNull("Object should not be null.", recipient.getAddress());
        assertEquals("Object should be the same as set.", address, recipient.getAddress());
    }

    /**
     * Tests the null setting of the payee address.
     */
    @Test
    public void testNullPayeeAddress() {

        final WithdrawalRequestPayee payee = new WithdrawalRequestPayee();
        payee.setAddress(null);
        assertNotNull("Object should not be null.", payee.getAddress());
    }

    /**
     * Tests the non-null setting of the payee address.
     */
    @Test
    public void testNonNullPayeeAddress() {

        final Address address = new Address();
        address.setSubmissionId(1984);

        final WithdrawalRequestPayee payee = new WithdrawalRequestPayee();
        payee.setAddress(address);
        assertNotNull("Object should not be null.", payee.getAddress());
        assertEquals("Object should be the same as set.", address, payee.getAddress());
    }

    /**
     * Tests the null setting of the withdrawal info.
     */
    @Test
    public void testNullWithdrawalInfo() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setParticipantInfo(null);
        assertNotNull("Object should not be null.", request.getParticipantInfo());
    }

    /**
     * Tests the non-null setting of the withdrawal info.
     */
    @Test
    public void testNonNullWithdrawalInfo() {

        final ParticipantInfo info = new ParticipantInfo();
        info.setContractName("Contract Name");

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setParticipantInfo(info);
        assertNotNull("Object should not be null.", request.getParticipantInfo());
        assertEquals("Object should be the same as set.", info, request.getParticipantInfo());
    }

    /**
     * Tests the null setting of the payment instruction.
     */
    @Test
    public void testNullPaymentInstruction() {

        final WithdrawalRequestPayee payee = new WithdrawalRequestPayee();
        payee.setPaymentInstruction(null);
        assertNotNull("Object should not be null.", payee.getPaymentInstruction());
    }

    /**
     * Tests the non-null setting of the payment instruction.
     */
    @Test
    public void testNonNullPaymentInstruction() {

        final PayeePaymentInstruction instruction = new PayeePaymentInstruction();
        instruction.setSubmissionId(1984);

        final WithdrawalRequestPayee payee = new WithdrawalRequestPayee();
        payee.setPaymentInstruction(instruction);
        assertNotNull("Object should not be null.", payee.getPaymentInstruction());
        assertEquals("Object should be the same as set.", instruction, payee
                .getPaymentInstruction());
    }

    /**
     * Creates a suite of Junit 4 tests.
     * 
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestNullCollectionAndObjectSetter.class);
    }
}
