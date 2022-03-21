package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

public class TestWithdrawalRequestNoteIsBlank {

    private static final String NON_BLANK_STRING = "Foo";

    private static final Integer NON_BLANK_INTEGER = 1;

    private static final Timestamp NON_BLANK_TIMESTAMP = new Timestamp(new Date().getTime());

    /**
     * Tests the is blank with all blank fields.
     */
    @Test
    public void testAllBlank() {

        final WithdrawalRequestNote note = new WithdrawalRequestNote();
        assertTrue("Note is blank.", note.isBlank());
    }

    /**
     * Tests the is blank with a non blank note type code.
     */
    @Test
    public void testNoteTypeCodeNonBlank() {

        final WithdrawalRequestNote note = new WithdrawalRequestNote();
        note.setNoteTypeCode(NON_BLANK_STRING);
        assertTrue("Note is blank - note type code should be ignored.", note.isBlank());
    }

    /**
     * Tests the is blank with a non blank note.
     */
    @Test
    public void testNoteNonBlank() {

        final WithdrawalRequestNote note = new WithdrawalRequestNote();
        note.setNote(NON_BLANK_STRING);
        assertFalse("Address is not blank.", note.isBlank());
    }

    /**
     * Tests the is blank with a non blank submission id.
     */
    @Test
    public void testSubmissionIdNonBlank() {

        final WithdrawalRequestNote note = new WithdrawalRequestNote();
        note.setSubmissionId(NON_BLANK_INTEGER);
        assertTrue("Address is blank - submission ID should be ignored.", note.isBlank());
    }

    /**
     * Tests the is blank with a non blank created by id.
     */
    @Test
    public void testCreatedByIdNonBlank() {

        final WithdrawalRequestNote note = new WithdrawalRequestNote();
        note.setCreatedById(NON_BLANK_INTEGER);
        assertTrue("Address is blank - created by id should be ignored.", note.isBlank());
    }

    /**
     * Tests the is blank with a non blank last updated by id.
     */
    @Test
    public void testLastUpdatedByIdNonBlank() {

        final WithdrawalRequestNote note = new WithdrawalRequestNote();
        note.setLastUpdatedById(NON_BLANK_INTEGER);
        assertTrue("Address is blank - last updated by id should be ignored.", note.isBlank());
    }

    /**
     * Tests the is blank with a non blank created.
     */
    @Test
    public void testCreatedNonBlank() {

        final WithdrawalRequestNote note = new WithdrawalRequestNote();
        note.setCreated(NON_BLANK_TIMESTAMP);
        assertTrue("Address is blank - created timestamp should be ignored.", note.isBlank());
    }

    /**
     * Tests the is blank with a non blank last updated.
     */
    @Test
    public void testLastUpdatedNonBlank() {

        final WithdrawalRequestNote note = new WithdrawalRequestNote();
        note.setLastUpdated(NON_BLANK_TIMESTAMP);
        assertTrue("Address is blank - last updated timestamp should be ignored.", note.isBlank());
    }

    /**
     * Creates a suite of Junit 4 tests.
     *
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestWithdrawalRequestNoteIsBlank.class);
    }
}
