/**
 * 
 */
package com.manulife.pension.service.withdrawal.common;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;



/**
 * Parasoft Jtest UTA: Test class for WithdrawalMessage
 *
 * @see com.manulife.pension.service.withdrawal.common.WithdrawalMessage
 * @author patelpo
 */
public class WithdrawalMessageTest {

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalMessageType()
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalMessage#getWithdrawalMessageType()
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalMessageType() throws Throwable {
		// Given
		WithdrawalMessageType withdrawalMessageType = WithdrawalMessageType.DATE_OF_BIRTH_INVALID; // UTA: default value
		WithdrawalMessage underTest = new WithdrawalMessage(withdrawalMessageType);

		// When
		WithdrawalMessageType result = underTest.getWithdrawalMessageType();

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetWithdrawalMessageType_1() throws Throwable {
		// Given
		WithdrawalMessageType withdrawalMessageType = WithdrawalMessageType.DATE_OF_BIRTH_INVALID; // UTA: default value
		
		Collection<String> propertyNames = new ArrayList<>();
		propertyNames.add("Test");
		WithdrawalMessage underTest = new WithdrawalMessage(withdrawalMessageType, propertyNames);

		// When
		WithdrawalMessageType result = underTest.getWithdrawalMessageType();

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetWithdrawalMessageType_2() throws Throwable {
		// Given
		WithdrawalMessageType withdrawalMessageType = WithdrawalMessageType.DATE_OF_BIRTH_INVALID; // UTA: default value
		
		String propertyName = "Test";
		
		WithdrawalMessage underTest = new WithdrawalMessage(withdrawalMessageType, propertyName);

		// When
		WithdrawalMessageType result = underTest.getWithdrawalMessageType();

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetWithdrawalMessageType_3() throws Throwable {
		// Given
		WithdrawalMessageType withdrawalMessageType = WithdrawalMessageType.DATE_OF_BIRTH_INVALID; // UTA: default value
		
		String propertyName = "Test";
		
		WithdrawalMessage underTest = new WithdrawalMessage(withdrawalMessageType, propertyName);

		// When
		WithdrawalMessageType result = underTest.getWithdrawalMessageType();

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetWithdrawalMessageType_4() throws Throwable {
		// Given
		WithdrawalMessageType withdrawalMessageType = WithdrawalMessageType.DATE_OF_BIRTH_INVALID; // UTA: default value
		
		String propertyName = "Test";
		
		Collection parameters = new ArrayList();
		parameters.add(true);
		
		WithdrawalMessage underTest = new WithdrawalMessage(withdrawalMessageType, propertyName, parameters);

		// When
		WithdrawalMessageType result = underTest.getWithdrawalMessageType();

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetWithdrawalMessageType_5() throws Throwable {
		// Given
		WithdrawalMessageType withdrawalMessageType = WithdrawalMessageType.DATE_OF_BIRTH_INVALID; // UTA: default value
		
		Collection<String> propertyNames = new ArrayList();
		propertyNames.add("Test");
		
		Collection parameters = new ArrayList();
		parameters.add(true);
		
		WithdrawalMessage underTest = new WithdrawalMessage(withdrawalMessageType, propertyNames, parameters);

		// When
		WithdrawalMessageType result = underTest.getWithdrawalMessageType();

		// Then
		// assertNotNull(result);
	}
	

	/**
	 * Parasoft Jtest UTA: Test for setWithdrawalMessageType(WithdrawalMessageType)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalMessage#setWithdrawalMessageType(WithdrawalMessageType)
	 * @author patelpo
	 */
	@Test
	public void testSetWithdrawalMessageType() throws Throwable {
		// Given
		WithdrawalMessageType withdrawalMessageType = WithdrawalMessageType.DATE_OF_BIRTH_INVALID; // UTA: default value
		WithdrawalMessage underTest = new WithdrawalMessage(withdrawalMessageType);

		// When
		WithdrawalMessageType withdrawalMessageType2 = WithdrawalMessageType.DATE_OF_BIRTH_INVALID; // UTA: default value
		underTest.setWithdrawalMessageType(withdrawalMessageType2);

	}
}