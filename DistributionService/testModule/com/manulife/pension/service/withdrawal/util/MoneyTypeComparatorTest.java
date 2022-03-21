/**
 * 
 */
package com.manulife.pension.service.withdrawal.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import org.junit.Test;

/**
 * Parasoft Jtest UTA: Test class for MoneyTypeComparator
 *
 * @see com.manulife.pension.service.withdrawal.util.MoneyTypeComparator
 * @author patelpo
 */
public class MoneyTypeComparatorTest {

	/**
	 * Parasoft Jtest UTA: Test for compare(WithdrawalRequestMoneyType, WithdrawalRequestMoneyType)
	 *
	 * @see com.manulife.pension.service.withdrawal.util.MoneyTypeComparator#compare(WithdrawalRequestMoneyType, WithdrawalRequestMoneyType)
	 * @author patelpo
	 */
	@Test
	public void testCompare() throws Throwable {
		// Given
		MoneyTypeComparator underTest = new MoneyTypeComparator();

		// When
		WithdrawalRequestMoneyType firstMoneyType = mockWithdrawalRequestMoneyType();
		when(firstMoneyType.getMoneyTypeCategoryCode()).thenReturn("100");
		WithdrawalRequestMoneyType secondMoneyType = mockWithdrawalRequestMoneyType2();
		when(secondMoneyType.getMoneyTypeCategoryCode()).thenReturn("100");
		int result = underTest.compare(firstMoneyType, secondMoneyType);

		// Then
		// assertEquals(0, result);
	}
	
	@Test
	public void testCompare_1() throws Throwable {
		// Given
		MoneyTypeComparator underTest = new MoneyTypeComparator();

		// When
		WithdrawalRequestMoneyType firstMoneyType = mockWithdrawalRequestMoneyType();
		when(firstMoneyType.getMoneyTypeCategoryCode()).thenReturn(null);
		WithdrawalRequestMoneyType secondMoneyType = mockWithdrawalRequestMoneyType2();
		int result = underTest.compare(firstMoneyType, secondMoneyType);

		// Then
		// assertEquals(0, result);
	}
	@Test
	public void testCompare_11() throws Throwable {
		// Given
		MoneyTypeComparator underTest = new MoneyTypeComparator();

		// When
		WithdrawalRequestMoneyType firstMoneyType = mockWithdrawalRequestMoneyType();
		when(firstMoneyType.getMoneyTypeAliasId()).thenReturn(null);
		when(firstMoneyType.getMoneyTypeId()).thenReturn(null);
		WithdrawalRequestMoneyType secondMoneyType = mockWithdrawalRequestMoneyType2();
		int result = underTest.compare(firstMoneyType, secondMoneyType);

		// Then
		//assertEquals(0, result);
	}
	@Test
	public void testCompare_2() throws Throwable {
		// Given
		MoneyTypeComparator underTest = new MoneyTypeComparator();

		// When
		WithdrawalRequestMoneyType firstMoneyType = mockWithdrawalRequestMoneyType();
		WithdrawalRequestMoneyType secondMoneyType = mockWithdrawalRequestMoneyType2();
		when(secondMoneyType.getMoneyTypeCategoryCode()).thenReturn(null);
		int result = underTest.compare(firstMoneyType, secondMoneyType);

		// Then
		// assertEquals(0, result);
	}
	@Test
	public void testCompare_22() throws Throwable {
		// Given
		MoneyTypeComparator underTest = new MoneyTypeComparator();

		// When
		WithdrawalRequestMoneyType firstMoneyType = mockWithdrawalRequestMoneyType();
		WithdrawalRequestMoneyType secondMoneyType = mockWithdrawalRequestMoneyType2();
		when(secondMoneyType.getMoneyTypeAliasId()).thenReturn(null);
		when(secondMoneyType.getMoneyTypeId()).thenReturn(null);
		int result = underTest.compare(firstMoneyType, secondMoneyType);

		// Then
		// assertEquals(0, result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestMoneyType
	 */
	private static WithdrawalRequestMoneyType mockWithdrawalRequestMoneyType() throws Throwable {
		WithdrawalRequestMoneyType firstMoneyType = mock(WithdrawalRequestMoneyType.class);
		String getMoneyTypeAliasIdResult = ""; // UTA: default value
		when(firstMoneyType.getMoneyTypeAliasId()).thenReturn(getMoneyTypeAliasIdResult);

		String getMoneyTypeCategoryCodeResult = ""; // UTA: default value
		when(firstMoneyType.getMoneyTypeCategoryCode()).thenReturn(getMoneyTypeCategoryCodeResult);

		String getMoneyTypeIdResult = ""; // UTA: default value
		when(firstMoneyType.getMoneyTypeId()).thenReturn(getMoneyTypeIdResult);
		return firstMoneyType;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestMoneyType
	 */
	private static WithdrawalRequestMoneyType mockWithdrawalRequestMoneyType2() throws Throwable {
		WithdrawalRequestMoneyType secondMoneyType = mock(WithdrawalRequestMoneyType.class);
		String getMoneyTypeAliasIdResult2 = ""; // UTA: default value
		when(secondMoneyType.getMoneyTypeAliasId()).thenReturn(getMoneyTypeAliasIdResult2);

		String getMoneyTypeCategoryCodeResult2 = ""; // UTA: default value
		when(secondMoneyType.getMoneyTypeCategoryCode()).thenReturn(getMoneyTypeCategoryCodeResult2);

		String getMoneyTypeIdResult2 = ""; // UTA: default value
		when(secondMoneyType.getMoneyTypeId()).thenReturn(getMoneyTypeIdResult2);
		return secondMoneyType;
	}
}