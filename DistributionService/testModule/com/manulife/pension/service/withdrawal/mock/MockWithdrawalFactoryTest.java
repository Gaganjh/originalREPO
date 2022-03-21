/**
 * 
 */
package com.manulife.pension.service.withdrawal.mock;

import static org.junit.Assert.assertNotNull;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Parasoft Jtest UTA: Test class for MockWithdrawalFactory
 *
 * @see com.manulife.pension.service.withdrawal.mock.MockWithdrawalFactory
 * @author patelpo
 */
public class MockWithdrawalFactoryTest {

	/**
	 * Parasoft Jtest UTA: Test for getMockWithdrawal(Map)
	 *
	 * @see com.manulife.pension.service.withdrawal.mock.MockWithdrawalFactory#getMockWithdrawal(Map)
	 * @author patelpo
	 */
	@Test
	public void testGetMockWithdrawal() throws Throwable {
		// When
		Map objectMap = new HashMap(); // UTA: default value
		WithdrawalRequest result = MockWithdrawalFactory.getMockWithdrawal(objectMap);

		// Then
		// assertNotNull(result);
	}
}