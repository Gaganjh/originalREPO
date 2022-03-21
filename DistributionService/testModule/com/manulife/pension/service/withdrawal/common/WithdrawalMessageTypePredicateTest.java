/**
 * 
 */
package com.manulife.pension.service.withdrawal.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.mock;

import org.junit.Test;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalMessageTypePredicate
 *
 * @see com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate
 * @author patelpo
 */
public class WithdrawalMessageTypePredicateTest {

	/**
	 * Parasoft Jtest UTA: Test for evaluate(Object)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate#evaluate(Object)
	 * @author patelpo
	 */
	@Test
	public void testEvaluate() throws Throwable {
		// Given
		WithdrawalMessageType messageType = WithdrawalMessageType.DATE_OF_BIRTH_INVALID; // UTA: default value
		WithdrawalMessageTypePredicate underTest = new WithdrawalMessageTypePredicate(messageType);

		// When
		Object target =mock(WithdrawalMessage.class); // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		// assertFalse(result);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testEvaluate_1() throws Throwable {
		// Given
		WithdrawalMessageType messageType = WithdrawalMessageType.DATE_OF_BIRTH_INVALID; // UTA: default value
		WithdrawalMessageTypePredicate underTest = new WithdrawalMessageTypePredicate(messageType);

		// When
		Object target = null; // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		// assertFalse(result);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testEvaluate_2() throws Throwable {
		// Given
		WithdrawalMessageType messageType = null;
		WithdrawalMessageTypePredicate underTest = new WithdrawalMessageTypePredicate(messageType);

		// When
		Object target = new Object(); // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		// assertFalse(result);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testEvaluate_exception() throws Throwable {
		// Given
		WithdrawalMessageType messageType = WithdrawalMessageType.DATE_OF_BIRTH_INVALID; // UTA: default value
		WithdrawalMessageTypePredicate underTest = new WithdrawalMessageTypePredicate(messageType);

		// When
		Object target = new Object(); // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		// assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getMessageType()
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate#getMessageType()
	 * @author patelpo
	 */
	@Test
	public void testGetMessageType() throws Throwable {
		// Given
		WithdrawalMessageType messageType = WithdrawalMessageType.DATE_OF_BIRTH_INVALID; // UTA: default value
		WithdrawalMessageTypePredicate underTest = new WithdrawalMessageTypePredicate(messageType);

		// When
		WithdrawalMessageType result = underTest.getMessageType();

		// Then
		// assertNotNull(result);
	}
}