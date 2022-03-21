/**
 * 
 */
package com.manulife.pension.service.withdrawal.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalStatePredicate
 *
 * @see com.manulife.pension.service.withdrawal.common.WithdrawalStatePredicate
 * @author patelpo
 */
public class WithdrawalStatePredicateTest {

	/**
	 * Parasoft Jtest UTA: Test for evaluate(Object)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalStatePredicate#evaluate(Object)
	 * @author patelpo
	 */
	@Test
	public void testEvaluate() throws Throwable {
		// Given
		Collection<WithdrawalStateEnum> states = new ArrayList<WithdrawalStateEnum>(); // UTA: default value
		WithdrawalStatePredicate underTest = new WithdrawalStatePredicate(states);

		// When
		Object target = new WithdrawalRequest(); // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		// assertFalse(result);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testEvaluate_1() throws Throwable {
		// Given
		Collection<WithdrawalStateEnum> states = null; // UTA: default value
		WithdrawalStatePredicate underTest = new WithdrawalStatePredicate(states);

		// When
		Object target = new Object(); // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		// assertFalse(result);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testEvaluate_2() throws Throwable {
		// Given
		Collection<WithdrawalStateEnum> states = new ArrayList<WithdrawalStateEnum>();
		WithdrawalStatePredicate underTest = new WithdrawalStatePredicate(states);

		// When
		Object target = null; // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		// assertFalse(result);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testEvaluate_exception() throws Throwable {
		// Given
		Collection<WithdrawalStateEnum> states = new ArrayList<WithdrawalStateEnum>(); // UTA: default value
		WithdrawalStatePredicate underTest = new WithdrawalStatePredicate(states);

		// When
		Object target = new Object(); // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		// assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getStates()
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalStatePredicate#getStates()
	 * @author patelpo
	 */
	@Test
	public void testGetStates() throws Throwable {
		// Given
		Collection<WithdrawalStateEnum> states = new ArrayList<WithdrawalStateEnum>(); // UTA: default value
		WithdrawalStatePredicate underTest = new WithdrawalStatePredicate(states);

		// When
		Collection<WithdrawalStateEnum> result = underTest.getStates();

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
}