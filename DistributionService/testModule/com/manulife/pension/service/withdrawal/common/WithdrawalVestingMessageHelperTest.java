/**
 * 
 */
package com.manulife.pension.service.withdrawal.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.util.VestingMessageType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.util.JdbcHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalVestingMessageHelper
 *
 * @see com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper
 * @author patelpo
 */
@PrepareForTest({ VestingMessageType.class,  Logger.class })
@RunWith(PowerMockRunner.class)
public class WithdrawalVestingMessageHelperTest {
	Logger getLoggerResult = mock(Logger.class); // UTA: default value
	
	@Before
	public void setUp() throws Exception {
		spy(Logger.class);

		doReturn(getLoggerResult).when(Logger.class, "getLogger", Mockito.any(Class.class));
		when(getLoggerResult.isDebugEnabled()).thenReturn(true);
	}

	/**
	 * Parasoft Jtest UTA: Test for addMessageForCriticalException(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper#addMessageForCriticalException(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testAddMessageForCriticalException() throws Throwable {
		// Given
		WithdrawalVestingMessageHelper underTest = WithdrawalVestingMessageHelper.getInstance();

		// When
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		underTest.addMessageForCriticalException(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Test for addVestingErrors(WithdrawalRequest, EmployeeVestingInformation)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper#addVestingErrors(WithdrawalRequest, EmployeeVestingInformation)
	 * @author patelpo
	 */
	@Test
	public void testAddVestingErrors() throws Throwable {
		// Given
		WithdrawalVestingMessageHelper underTest = WithdrawalVestingMessageHelper.getInstance();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest();
		EmployeeVestingInformation employeeVestingInformation = mockEmployeeVestingInformation();
		underTest.addVestingErrors(withdrawalRequest, employeeVestingInformation);

	}
	@Test
	public void testAddVestingErrors_1() throws Throwable {
		// Given
		WithdrawalVestingMessageHelper underTest = WithdrawalVestingMessageHelper.getInstance();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest();
		EmployeeVestingInformation employeeVestingInformation = null;
		underTest.addVestingErrors(withdrawalRequest, employeeVestingInformation);

	}
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Collection<WithdrawalMessage> getMessagesResult = new ArrayList<WithdrawalMessage>(); // UTA: default value
		doReturn(getMessagesResult).when(withdrawalRequest).getMessages();

		String toStringResult = ""; // UTA: default value
		when(withdrawalRequest.toString()).thenReturn(toStringResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of EmployeeVestingInformation
	 */
	private static EmployeeVestingInformation mockEmployeeVestingInformation() throws Throwable {
		EmployeeVestingInformation employeeVestingInformation = mock(EmployeeVestingInformation.class);
		Set getErrorsResult = new HashSet(); // UTA: default value
		VestingMessageType vestingMessageType= mock(VestingMessageType.class);
		getErrorsResult.add(vestingMessageType);
		when(employeeVestingInformation.getErrors()).thenReturn(getErrorsResult);
		return employeeVestingInformation;
	}
	
	
	
	/**
	 * Parasoft Jtest UTA: Test for getAllWithdrawalMessageTypes()
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper#getAllWithdrawalMessageTypes()
	 * @author patelpo
	 */
	@Test
	public void testGetAllWithdrawalMessageTypes() throws Throwable {
		// Given
		WithdrawalVestingMessageHelper underTest = WithdrawalVestingMessageHelper.getInstance();

		// When
		Collection<WithdrawalMessageType> result = underTest.getAllWithdrawalMessageTypes();

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Test for getInstance()
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper#getInstance()
	 * @author patelpo
	 */
	@Test
	public void testGetInstance() throws Throwable {
		// When
		WithdrawalVestingMessageHelper result = WithdrawalVestingMessageHelper.getInstance();

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getVestingMessageTypeFromCode(int)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper#getVestingMessageTypeFromCode(int)
	 * @author patelpo
	 */
	@Test
	public void testGetVestingMessageTypeFromCode() throws Throwable {
		// Given
		WithdrawalVestingMessageHelper underTest = WithdrawalVestingMessageHelper.getInstance();

		// When
		int vestingMessageTypeCode = 1; // UTA: default value
		VestingMessageType result = underTest.getVestingMessageTypeFromCode(vestingMessageTypeCode);

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetVestingMessageTypeFromCode_1() throws Throwable {
		// Given
		WithdrawalVestingMessageHelper underTest = WithdrawalVestingMessageHelper.getInstance();

		// When
		int vestingMessageTypeCode = 5; // UTA: default value
		VestingMessageType result = underTest.getVestingMessageTypeFromCode(vestingMessageTypeCode);

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetVestingMessageTypeFromCode_2() throws Throwable {
		// Given
		WithdrawalVestingMessageHelper underTest = WithdrawalVestingMessageHelper.getInstance();

		// When
		int vestingMessageTypeCode = 4; // UTA: default value
		VestingMessageType result = underTest.getVestingMessageTypeFromCode(vestingMessageTypeCode);

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetVestingMessageTypeFromCode_3() throws Throwable {
		// Given
		WithdrawalVestingMessageHelper underTest = WithdrawalVestingMessageHelper.getInstance();

		// When
		int vestingMessageTypeCode = 3; // UTA: default value
		VestingMessageType result = underTest.getVestingMessageTypeFromCode(vestingMessageTypeCode);

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetVestingMessageTypeFromCode_4() throws Throwable {
		// Given
		WithdrawalVestingMessageHelper underTest = WithdrawalVestingMessageHelper.getInstance();

		// When
		int vestingMessageTypeCode = 2; // UTA: default value
		VestingMessageType result = underTest.getVestingMessageTypeFromCode(vestingMessageTypeCode);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalMessageTypeFromVestingMessageType(VestingMessageType)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper#getWithdrawalMessageTypeFromVestingMessageType(VestingMessageType)
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalMessageTypeFromVestingMessageType() throws Throwable {
		// Given
		WithdrawalVestingMessageHelper underTest = WithdrawalVestingMessageHelper.getInstance();

		// When
		VestingMessageType vestingMessageType = mockVestingMessageType();
		WithdrawalMessageType result = underTest.getWithdrawalMessageTypeFromVestingMessageType(vestingMessageType);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of VestingMessageType
	 */
	private static VestingMessageType mockVestingMessageType() throws Throwable {
		VestingMessageType vestingMessageType = mock(VestingMessageType.class);
		String toStringResult = "Test"; // UTA: default value
		when(vestingMessageType.toString()).thenReturn(toStringResult);
		return vestingMessageType;
	}
}