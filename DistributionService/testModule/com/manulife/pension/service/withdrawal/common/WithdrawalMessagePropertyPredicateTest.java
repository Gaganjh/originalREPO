/**
 * 
 */
package com.manulife.pension.service.withdrawal.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import static org.powermock.api.mockito.PowerMockito.mock;

import org.junit.Test;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalMessagePropertyPredicate
 *
 * @see com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate
 * @author patelpo
 */
public class WithdrawalMessagePropertyPredicateTest {

	/**
	 * Parasoft Jtest UTA: Test for evaluate(Object)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate#evaluate(Object)
	 * @author patelpo
	 */
	@Test
	public void testEvaluate() throws Throwable {
		// Given
		String property = ""; // UTA: default value
		WithdrawalMessagePropertyPredicate underTest = new WithdrawalMessagePropertyPredicate(property);

		// When
		Object target = mock(WithdrawalMessage.class); // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		// assertFalse(result);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEvaluate_1() throws Throwable {
		// Given
		String property = null; // UTA: default value
		WithdrawalMessagePropertyPredicate underTest = new WithdrawalMessagePropertyPredicate(property);

		// When
		Object target = new Object(); // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		// assertFalse(result);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testEvaluate_2() throws Throwable {
		// Given
		String property = ""; // UTA: default value
		WithdrawalMessagePropertyPredicate underTest = new WithdrawalMessagePropertyPredicate(property);

		// When
		Object target = null; // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		// assertFalse(result);
	}
	@Test(expected= IllegalArgumentException.class)
	public void testEvaluate_EXCEPTION() throws Throwable {
		// Given
		String property = ""; // UTA: default value
		WithdrawalMessagePropertyPredicate underTest = new WithdrawalMessagePropertyPredicate(property);

		// When
		Object target = new Object(); // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		// assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getProperty()
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate#getProperty()
	 * @author patelpo
	 */
	@Test
	public void testGetProperty() throws Throwable {
		// Given
		String property = ""; // UTA: default value
		WithdrawalMessagePropertyPredicate underTest = new WithdrawalMessagePropertyPredicate(property);

		// When
		String result = underTest.getProperty();

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}
}