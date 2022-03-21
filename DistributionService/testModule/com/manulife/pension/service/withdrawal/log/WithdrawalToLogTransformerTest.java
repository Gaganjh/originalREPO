/**
 * 
 */
package com.manulife.pension.service.withdrawal.log;

import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.mock;

import org.junit.Test;

import com.manulife.pension.service.withdrawal.valueobject.BaseWithdrawal;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalToLogTransformer
 *
 * @see com.manulife.pension.service.withdrawal.log.WithdrawalToLogTransformer
 * @author patelpo
 */
public class WithdrawalToLogTransformerTest {

	/**
	 * Parasoft Jtest UTA: Test for transform(Object)
	 *
	 * @see com.manulife.pension.service.withdrawal.log.WithdrawalToLogTransformer#transform(Object)
	 * @author patelpo
	 */
	@Test
	public void testTransform() throws Throwable {
		// Given
		WithdrawalToLogTransformer underTest = new WithdrawalToLogTransformer();

		// When
		BaseWithdrawal baseWithdrawal=mock(BaseWithdrawal.class);
		Object input = baseWithdrawal; // UTA: default value
		Object result = underTest.transform(input);

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testTransform_1() throws Throwable {
		// Given
		WithdrawalToLogTransformer underTest = new WithdrawalToLogTransformer();

		// When
		Object input = new Object(); // UTA: default value
		Object result = underTest.transform(input);

		// Then
		// assertNotNull(result);
	}
}