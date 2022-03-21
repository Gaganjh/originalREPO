/**
 * 
 */
package com.manulife.pension.service.withdrawal.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import org.junit.Test;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalRequestMoneyTypeByIdPredicate
 *
 * @see com.manulife.pension.service.withdrawal.common.WithdrawalRequestMoneyTypeByIdPredicate
 * @author patelpo
 */
public class WithdrawalRequestMoneyTypeByIdPredicateTest {

	/**
	 * Parasoft Jtest UTA: Test for evaluate(Object)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalRequestMoneyTypeByIdPredicate#evaluate(Object)
	 * @author patelpo
	 */
	@Test
	public void testEvaluate() throws Throwable {
		// Given
		WithdrawalRequestMoneyType withdrawalRequestMoneyType = mockWithdrawalRequestMoneyType();
		WithdrawalRequestMoneyTypeByIdPredicate underTest = new WithdrawalRequestMoneyTypeByIdPredicate(
				withdrawalRequestMoneyType);

		// When
		Object object = new WithdrawalRequestMoneyType(); // UTA: default value
		boolean result = underTest.evaluate(object);

		// Then
		// assertFalse(result);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testEvaluate_1() throws Throwable {
		// Given
		WithdrawalRequestMoneyType withdrawalRequestMoneyType = mockWithdrawalRequestMoneyType();
		WithdrawalRequestMoneyTypeByIdPredicate underTest = new WithdrawalRequestMoneyTypeByIdPredicate(
				withdrawalRequestMoneyType);

		// When
		Object object = null; // UTA: default value
		boolean result = underTest.evaluate(object);

		// Then
		// assertFalse(result);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testEvaluate_2() throws Throwable {
		// Given
		WithdrawalRequestMoneyType withdrawalRequestMoneyType = null;
		WithdrawalRequestMoneyTypeByIdPredicate underTest = new WithdrawalRequestMoneyTypeByIdPredicate(
				withdrawalRequestMoneyType);

		// When
		Object object = new WithdrawalRequestMoneyType(); // UTA: default value
		boolean result = underTest.evaluate(object);

		// Then
		// assertFalse(result);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEvaluate_exception() throws Throwable {
		// Given
		WithdrawalRequestMoneyType withdrawalRequestMoneyType = mockWithdrawalRequestMoneyType();
		WithdrawalRequestMoneyTypeByIdPredicate underTest = new WithdrawalRequestMoneyTypeByIdPredicate(
				withdrawalRequestMoneyType);

		// When
		Object object = new Object(); // UTA: default value
		boolean result = underTest.evaluate(object);

		// Then
		// assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestMoneyType
	 */
	private static WithdrawalRequestMoneyType mockWithdrawalRequestMoneyType() throws Throwable {
		WithdrawalRequestMoneyType withdrawalRequestMoneyType = mock(WithdrawalRequestMoneyType.class);
		String getMoneyTypeIdResult = ""; // UTA: default value
		when(withdrawalRequestMoneyType.getMoneyTypeId()).thenReturn(getMoneyTypeIdResult);
		return withdrawalRequestMoneyType;
	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalRequestMoneyType()
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalRequestMoneyTypeByIdPredicate#getWithdrawalRequestMoneyType()
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalRequestMoneyType() throws Throwable {
		// Given
		WithdrawalRequestMoneyType withdrawalRequestMoneyType = mock(WithdrawalRequestMoneyType.class);
		WithdrawalRequestMoneyTypeByIdPredicate underTest = new WithdrawalRequestMoneyTypeByIdPredicate(
				withdrawalRequestMoneyType);

		// When
		WithdrawalRequestMoneyType result = underTest.getWithdrawalRequestMoneyType();

		// Then
		// assertNotNull(result);
	}
}