package com.manulife.pension.service.withdrawal.util;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;

/**
 * Test class for WithdrawalNoteSort.
 * 
 * @author Kristin Kerr
 */
public class TestWithdrawalNoteSort {

    /**
     * Tests sorting of WithdrawalRequestNotes in descending date/time order.
     */
    @Test
    public void testSortWithdrawalNotesArray() {

        Calendar cal = Calendar.getInstance();

        WithdrawalRequestNote first = new WithdrawalRequestNote();
        first.setCreated(new Timestamp(cal.getTimeInMillis()));

        WithdrawalRequestNote second = new WithdrawalRequestNote();
        cal.add(Calendar.DAY_OF_MONTH, -2);
        second.setCreated(new Timestamp(cal.getTimeInMillis()));

        WithdrawalRequestNote third = new WithdrawalRequestNote();
        cal.add(Calendar.DAY_OF_MONTH, -5);
        third.setCreated(new Timestamp(cal.getTimeInMillis()));

        WithdrawalRequestNote[] notes = { third, first, second };
        Arrays.sort(notes, ComparatorUtils.nullHighComparator(ComparatorUtils
                .reversedComparator(new NoteComparator())));

        assertEquals("First note", first, notes[0]);
        assertEquals("Second note", second, notes[1]);
        assertEquals("Third note", third, notes[2]);

    }

    /**
     * Tests sorting of WithdrawalRequestNotes in descending date/time order.
     */
    @Test
    public void testSortWithdrawalNotesArrayWithNullValues() {

        Calendar cal = Calendar.getInstance();

        WithdrawalRequestNote first = new WithdrawalRequestNote();
        cal.add(Calendar.DAY_OF_MONTH, -5);
        first.setCreated(new Timestamp(cal.getTimeInMillis()));

        WithdrawalRequestNote second = new WithdrawalRequestNote();
        second.setCreated(null);

        WithdrawalRequestNote third = null;

        WithdrawalRequestNote[] notes = { third, first, second };
        Arrays.sort(notes, ComparatorUtils.nullHighComparator(ComparatorUtils
                .reversedComparator(new NoteComparator())));

        int i = 0;
        assertEquals("First note", first, notes[i++]);
        assertEquals("Second note", second, notes[i++]);
        assertEquals("Third note", third, notes[i++]);

    }

}
