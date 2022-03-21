/**
 * 
 */
package com.manulife.pension.service.withdrawal.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;
import java.sql.Timestamp;
import org.junit.Test;

/**
 * Parasoft Jtest UTA: Test class for NoteComparator
 *
 * @see com.manulife.pension.service.withdrawal.util.NoteComparator
 * @author patelpo
 */
public class NoteComparatorTest {

	/**
	 * Parasoft Jtest UTA: Test for compare(WithdrawalRequestNote, WithdrawalRequestNote)
	 *
	 * @see com.manulife.pension.service.withdrawal.util.NoteComparator#compare(WithdrawalRequestNote, WithdrawalRequestNote)
	 * @author patelpo
	 */
	@Test
	public void testCompare() throws Throwable {
		// Given
		NoteComparator underTest = new NoteComparator();

		
		// When
		WithdrawalRequestNote first = mockWithdrawalRequestNote();
		WithdrawalRequestNote second = mockWithdrawalRequestNote2();
		int result = underTest.compare(first, second);

		// Then
		// assertEquals(0, result);
	}
	@Test
	public void testCompare_1() throws Throwable {
		// Given
		NoteComparator underTest = new NoteComparator();

		
		// When
		WithdrawalRequestNote first = mockWithdrawalRequestNote();
		when(first.getCreated()).thenReturn(null);
		WithdrawalRequestNote second = mockWithdrawalRequestNote2();
		when(second.getCreated()).thenReturn(null);
		int result = underTest.compare(first, second);

		// Then
		// assertEquals(0, result);
	}
	@Test
	public void testCompare_2() throws Throwable {
		// Given
		NoteComparator underTest = new NoteComparator();

		
		// When
		WithdrawalRequestNote first = mockWithdrawalRequestNote();
		when(first.getCreated()).thenReturn(null);
		WithdrawalRequestNote second = mockWithdrawalRequestNote2();
		int result = underTest.compare(first, second);

		// Then
		// assertEquals(0, result);
	}
	@Test
	public void testCompare_3() throws Throwable {
		// Given
		NoteComparator underTest = new NoteComparator();

		
		// When
		WithdrawalRequestNote first = mockWithdrawalRequestNote();
		WithdrawalRequestNote second = mockWithdrawalRequestNote2();
		when(second.getCreated()).thenReturn(null);
		int result = underTest.compare(first, second);

		// Then
		// assertEquals(0, result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		int compareToResult = 0; // UTA: default value
		when(getCreatedResult.compareTo(any(Timestamp.class))).thenReturn(compareToResult);

		long getTimeResult = 0L; // UTA: default value
		when(getCreatedResult.getTime()).thenReturn(getTimeResult);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote() throws Throwable {
		WithdrawalRequestNote first = mock(WithdrawalRequestNote.class);
		Timestamp getCreatedResult = mockTimestamp();
		when(first.getCreated()).thenReturn(getCreatedResult);
		return first;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp2() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		int compareToResult2 = 0; // UTA: default value
		when(getCreatedResult2.compareTo(any(Timestamp.class))).thenReturn(compareToResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getCreatedResult2.getTime()).thenReturn(getTimeResult2);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote2() throws Throwable {
		WithdrawalRequestNote second = mock(WithdrawalRequestNote.class);
		Timestamp getCreatedResult2 = mockTimestamp2();
		when(second.getCreated()).thenReturn(getCreatedResult2);
		return second;
	}
}