/**
 * 
 */
package com.manulife.pension.service.withdrawal.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalDeclarationPredicate
 *
 * @see com.manulife.pension.service.withdrawal.common.WithdrawalDeclarationPredicate
 * @author patelpo
 */
public class WithdrawalDeclarationPredicateTest {

	/**
	 * Parasoft Jtest UTA: Test for evaluate(Object)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalDeclarationPredicate#evaluate(Object)
	 * @author patelpo
	 */
	@Test
	public void testEvaluate() throws Throwable {
		// Given
		String typeCode = "Test"; // UTA: default value
		WithdrawalDeclarationPredicate underTest = new WithdrawalDeclarationPredicate(typeCode);

		// When
		Object target = new WithdrawalRequestDeclaration(); // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		 assertFalse(result);
	}
	@Test(expected = IllegalArgumentException.class)
	public void testEvaluate_1() throws Throwable {
		// Given
		String typeCode = "Test"; // UTA: default value
		WithdrawalDeclarationPredicate underTest = new WithdrawalDeclarationPredicate(typeCode);

		// When
		Object target = null; // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		 assertFalse(result);
	}
	@Test(expected = IllegalArgumentException.class)
	public void testEvaluate_2() throws Throwable {
		// Given
		String typeCode = null; // UTA: default value
		WithdrawalDeclarationPredicate underTest = new WithdrawalDeclarationPredicate(typeCode);

		// When
		Object target = new Object(); // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		 assertFalse(result);
	}
	@Test(expected= IllegalArgumentException.class)
	public void testEvaluate_exception() throws Throwable {
		// Given
		String typeCode = ""; // UTA: default value
		WithdrawalDeclarationPredicate underTest = new WithdrawalDeclarationPredicate(typeCode);

		// When
		Object target = new Object(); // UTA: default value
		boolean result = underTest.evaluate(target);

		// Then
		// assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getTypeCode()
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalDeclarationPredicate#getTypeCode()
	 * @author patelpo
	 */
	@Test
	public void testGetTypeCode() throws Throwable {
		// Given
		String typeCode = ""; // UTA: default value
		WithdrawalDeclarationPredicate underTest = new WithdrawalDeclarationPredicate(typeCode);

		// When
		String result = underTest.getTypeCode();

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}
}