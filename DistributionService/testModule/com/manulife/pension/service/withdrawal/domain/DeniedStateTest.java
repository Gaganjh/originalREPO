/**
 * 
 */
package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import org.junit.Test;

/**
 * Parasoft Jtest UTA: Test class for DeniedState
 *
 * @see com.manulife.pension.service.withdrawal.domain.DeniedState
 * @author patelpo
 */
public class DeniedStateTest {

	/**
	 * Parasoft Jtest UTA: Test for applyDefaultDataForEdit(Withdrawal, WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DeniedState#applyDefaultDataForEdit(Withdrawal, WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testApplyDefaultDataForEdit() throws Throwable {
		// Given
		DeniedState underTest = new DeniedState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest defaultVo = mock(WithdrawalRequest.class);
		underTest.applyDefaultDataForEdit(withdrawal, defaultVo);

	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalStateEnum()
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DeniedState#getWithdrawalStateEnum()
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalStateEnum() throws Throwable {
		// Given
		DeniedState underTest = new DeniedState();

		// When
		WithdrawalStateEnum result = underTest.getWithdrawalStateEnum();

		// Then
		// assertNotNull(result);
	}
}