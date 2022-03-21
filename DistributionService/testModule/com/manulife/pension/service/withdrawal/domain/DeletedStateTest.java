/**
 * 
 */
package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import org.junit.Test;

/**
 * Parasoft Jtest UTA: Test class for DeletedState
 *
 * @see com.manulife.pension.service.withdrawal.domain.DeletedState
 * @author patelpo
 */
public class DeletedStateTest {

	/**
	 * Parasoft Jtest UTA: Test for delete(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DeletedState#delete(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testDelete() throws Throwable {
		// Given
		DeletedState underTest = new DeletedState();

		// When
		Withdrawal withdrawal = mockWithdrawal();
		underTest.delete(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalStateEnum()
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DeletedState#getWithdrawalStateEnum()
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalStateEnum() throws Throwable {
		// Given
		DeletedState underTest = new DeletedState();

		// When
		WithdrawalStateEnum result = underTest.getWithdrawalStateEnum();

		// Then
		// assertNotNull(result);
	}
}