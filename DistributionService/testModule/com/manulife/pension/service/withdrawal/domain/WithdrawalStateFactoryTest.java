/**
 * 
 */
package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

import org.apache.commons.lang.NotImplementedException;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalStateFactory
 *
 * @see com.manulife.pension.service.withdrawal.domain.WithdrawalStateFactory
 * @author patelpo
 */
@PrepareForTest({ WithdrawalStateFactory.class })
@RunWith(PowerMockRunner.class)
public class WithdrawalStateFactoryTest {

	/**
	 * Parasoft Jtest UTA: Test for getState(WithdrawalStateEnum)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.WithdrawalStateFactory#getState(WithdrawalStateEnum)
	 * @author patelpo
	 */
	@Test
	public void testGetState() throws Throwable {
		// When
		WithdrawalStateEnum withdrawalStateEnum = WithdrawalStateEnum.DRAFT; // UTA: default value
		WithdrawalState result = WithdrawalStateFactory.getState(withdrawalStateEnum);

		// Then
		// assertNotNull(result);
	}

	@Test
	public void testGetState_1() throws Throwable {
		spy(WithdrawalStateFactory.class);

		WithdrawalState getStateResult = mock(WithdrawalState.class);
		doReturn(getStateResult).when(WithdrawalStateFactory.class);
		WithdrawalStateFactory.getState(nullable(WithdrawalStateEnum.class));

		// When
		WithdrawalStateEnum withdrawalStateEnum = WithdrawalStateEnum.DRAFT; // UTA: default value
		WithdrawalState result = WithdrawalStateFactory.getState(withdrawalStateEnum);
		
		// Then
		// assertNotNull(result);
	}
	
	
	

	/**
	 * Parasoft Jtest UTA: Test for updateStateFromStatusCode(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.WithdrawalStateFactory#updateStateFromStatusCode(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testUpdateStateFromStatusCode() throws Throwable {
		
		// When
		Withdrawal withdrawal = mockWithdrawal();
		WithdrawalStateFactory.updateStateFromStatusCode(withdrawal);

	}
	@Test(expected=NotImplementedException.class)
	public void testUpdateStateFromStatusCode_1() throws Throwable {
		
		// When
		Withdrawal withdrawal = mockWithdrawal_1();
		WithdrawalStateFactory.updateStateFromStatusCode(withdrawal);

	}
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		String getStatusCodeResult = "W5"; // UTA: default value
		when(getWithdrawalRequestResult.getStatusCode()).thenReturn(getStatusCodeResult);
		return getWithdrawalRequestResult;
	}
	private static WithdrawalRequest mockWithdrawalRequest_1() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		String getStatusCodeResult = null; // UTA: default value
		when(getWithdrawalRequestResult.getStatusCode()).thenReturn(getStatusCodeResult);
		return getWithdrawalRequestResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}
	private static Withdrawal mockWithdrawal_1() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest_1();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}
}