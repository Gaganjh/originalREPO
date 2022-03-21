/**
 * 
 */
package com.manulife.pension.service.withdrawal.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.manulife.pension.delegate.VestingServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.MoneyTypeVestingPercentage;
import com.manulife.pension.service.vesting.VestingException;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.JdbcHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalVestingEngine
 *
 * @see com.manulife.pension.service.withdrawal.common.WithdrawalVestingEngine
 * @author patelpo
 */
@PrepareForTest({ WithdrawalVestingEngine.class, VestingServiceDelegate.class, Logger.class })
@RunWith(PowerMockRunner.class)
public class WithdrawalVestingEngineTest {
	Logger getLoggerResult = mock(Logger.class); // UTA: default value

	@Before
	public void setUp() throws Exception {
		spy(Logger.class);
		doReturn(getLoggerResult).when(Logger.class, "getLogger", Mockito.any(Class.class));
		when(getLoggerResult.isDebugEnabled()).thenReturn(true);
	}

	/**
	 * Parasoft Jtest UTA: Test for applyVestingInformationToWithdrawalReqeust(EmployeeVestingInformation, WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalVestingEngine#applyVestingInformationToWithdrawalReqeust(EmployeeVestingInformation, WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testApplyVestingInformationToWithdrawalReqeust() throws Throwable {
		// Given
		WithdrawalVestingEngine underTest = new WithdrawalVestingEngine();

		// When
		Map moneyTypeVestingPercentages = new HashMap<>();
		EmployeeVestingInformation employeeVestingInformation = mockEmployeeVestingInformation();
		when(employeeVestingInformation.getVestingServiceFeature()).thenReturn("TPAP");
		when(employeeVestingInformation.getMoneyTypeVestingPercentages()).thenReturn(moneyTypeVestingPercentages);

		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest();
		underTest.applyVestingInformationToWithdrawalReqeust(employeeVestingInformation, withdrawalRequest);

	}
	@Test
	public void testApplyVestingInformationToWithdrawalReqeust_1() throws Throwable {
		// Given
		WithdrawalVestingEngine underTest = new WithdrawalVestingEngine();
		
		// When
		EmployeeVestingInformation employeeVestingInformation = mockEmployeeVestingInformation();
		when(employeeVestingInformation.getVestingServiceFeature()).thenReturn("TPAP");
		when(employeeVestingInformation.getMoneyTypeVestingPercentages()).thenReturn(null);
		
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest();
		underTest.applyVestingInformationToWithdrawalReqeust(employeeVestingInformation, withdrawalRequest);
		
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of EmployeeVestingInformation
	 */
	private static EmployeeVestingInformation mockEmployeeVestingInformation() throws Throwable {
		EmployeeVestingInformation employeeVestingInformation = mock(EmployeeVestingInformation.class);
		Set getErrorsResult = new HashSet(); // UTA: default value
		when(employeeVestingInformation.getErrors()).thenReturn(getErrorsResult);

		Map getMoneyTypeVestingPercentagesResult = new HashMap(); // UTA: default value
		when(employeeVestingInformation.getMoneyTypeVestingPercentages())
				.thenReturn(getMoneyTypeVestingPercentagesResult);

		String getVestingServiceFeatureResult = ""; // UTA: default value
		when(employeeVestingInformation.getVestingServiceFeature()).thenReturn(getVestingServiceFeatureResult);
		return employeeVestingInformation;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		WithdrawalRequestMoneyType withdrawalRequestMoneyType = mock(WithdrawalRequestMoneyType.class);
		when(withdrawalRequestMoneyType.getMoneyTypeId()).thenReturn("Test");
		getMoneyTypesResult.add(withdrawalRequestMoneyType);
		doReturn(getMoneyTypesResult).when(withdrawalRequest).getMoneyTypes();
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for calculate(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalVestingEngine#calculate(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testCalculate() throws Throwable {
		spy(VestingServiceDelegate.class);

		
		BaseEnvironment newBaseEnvironmentResult = mock(BaseEnvironment.class); // UTA: default value
		whenNew(BaseEnvironment.class).withAnyArguments().thenReturn(newBaseEnvironmentResult);
		
		VestingServiceDelegate getInstanceResult = mock(VestingServiceDelegate.class);
		doReturn(getInstanceResult).when(VestingServiceDelegate.class);
		VestingServiceDelegate.getInstance(nullable(String.class));
		EmployeeVestingInformation employeeVestingInformation = mock(EmployeeVestingInformation.class);
		when(getInstanceResult.getEmployeeWithdrawalVestingInformation(any(Integer.class), any(Long.class), any(String.class), any(Date.class))).thenReturn(employeeVestingInformation);


		// Given
		WithdrawalVestingEngine underTest = new WithdrawalVestingEngine();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest2();
		underTest.calculate(withdrawalRequest);

	}
	
	
	@Test
	public void testCalculate_1() throws Throwable {
		spy(VestingServiceDelegate.class);
		
		
		BaseEnvironment newBaseEnvironmentResult = mock(BaseEnvironment.class); // UTA: default value
		whenNew(BaseEnvironment.class).withAnyArguments().thenReturn(newBaseEnvironmentResult);
		
		VestingServiceDelegate getInstanceResult = mock(VestingServiceDelegate.class);
		doReturn(getInstanceResult).when(VestingServiceDelegate.class);
		VestingServiceDelegate.getInstance(nullable(String.class));
		EmployeeVestingInformation employeeVestingInformation = mock(EmployeeVestingInformation.class);
		when(getInstanceResult.getEmployeeWithdrawalVestingInformation(any(Integer.class), any(Long.class), any(String.class), any(Date.class))).thenReturn(employeeVestingInformation);
		
		
		// Given
		WithdrawalVestingEngine underTest = new WithdrawalVestingEngine();
		
		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest2_1();
		underTest.calculate(withdrawalRequest);
		
	}
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate() throws Throwable {
		Date getVestingEventDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getVestingEventDateResult.after(any(Date.class))).thenReturn(afterResult);
		return getVestingEventDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest2() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(withdrawalRequest.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(withdrawalRequest).getMoneyTypes();

		String getReasonCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getReasonCode()).thenReturn(getReasonCodeResult);

		Date getVestingEventDateResult = mockDate();
		when(withdrawalRequest.getVestingEventDate()).thenReturn(getVestingEventDateResult);
		return withdrawalRequest;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_1() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);
		
		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(withdrawalRequest.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);
		
		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(withdrawalRequest).getMoneyTypes();
		
		String getReasonCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getReasonCode()).thenReturn(getReasonCodeResult);
		
		Date getVestingEventDateResult = null;
		when(withdrawalRequest.getVestingEventDate()).thenReturn(getVestingEventDateResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for getEmployeeVestingInformation(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalVestingEngine#getEmployeeVestingInformation(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testGetEmployeeVestingInformation() throws Throwable {

		BaseEnvironment newBaseEnvironmentResult = mock(BaseEnvironment.class); // UTA: default value
		whenNew(BaseEnvironment.class).withAnyArguments().thenReturn(newBaseEnvironmentResult);
		spy(VestingServiceDelegate.class);

		VestingServiceDelegate getInstanceResult = mock(VestingServiceDelegate.class);
		doReturn(getInstanceResult).when(VestingServiceDelegate.class);
		VestingServiceDelegate.getInstance(nullable(String.class));
		EmployeeVestingInformation employeeVestingInformation = mock(EmployeeVestingInformation.class);
		when(getInstanceResult.getEmployeeWithdrawalVestingInformation(any(Integer.class), any(Long.class), any(String.class), any(Date.class))).thenReturn(employeeVestingInformation);

		// Given
		WithdrawalVestingEngine underTest = new WithdrawalVestingEngine();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest3();
		EmployeeVestingInformation result = underTest.getEmployeeVestingInformation(withdrawalRequest);

		// Then
		// assertNotNull(result);
	}
	
	@Test
	public void testGetEmployeeVestingInformation_1() throws Throwable {
		
		BaseEnvironment newBaseEnvironmentResult = mock(BaseEnvironment.class); // UTA: default value
		whenNew(BaseEnvironment.class).withAnyArguments().thenReturn(newBaseEnvironmentResult);
		spy(VestingServiceDelegate.class);
		
		VestingServiceDelegate getInstanceResult = mock(VestingServiceDelegate.class);
		doReturn(getInstanceResult).when(VestingServiceDelegate.class);
		VestingServiceDelegate.getInstance(nullable(String.class));
		EmployeeVestingInformation employeeVestingInformation = mock(EmployeeVestingInformation.class);
		when(getInstanceResult.getEmployeeWithdrawalVestingInformation(any(Integer.class), any(Long.class), any(String.class), any(Date.class))).thenReturn(employeeVestingInformation);
		
		// Given
		WithdrawalVestingEngine underTest = new WithdrawalVestingEngine();
		
		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest3_1();
		EmployeeVestingInformation result = underTest.getEmployeeVestingInformation(withdrawalRequest);
		
		// Then
		// assertNotNull(result);
	}
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest3() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(withdrawalRequest.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		String getReasonCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getReasonCode()).thenReturn(getReasonCodeResult);

		Date getVestingEventDateResult = mock(Date.class);
		when(withdrawalRequest.getVestingEventDate()).thenReturn(getVestingEventDateResult);
		return withdrawalRequest;
	}
	private static WithdrawalRequest mockWithdrawalRequest3_1() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);
		
		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(withdrawalRequest.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);
		
		String getReasonCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getReasonCode()).thenReturn(getReasonCodeResult);
		
		Date getVestingEventDateResult = null;
		when(withdrawalRequest.getVestingEventDate()).thenReturn(getVestingEventDateResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for isVestingNonCriticalErrorWithWarning(EmployeeVestingInformation, WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalVestingEngine#isVestingNonCriticalErrorWithWarning(EmployeeVestingInformation, WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testIsVestingNonCriticalErrorWithWarning() throws Throwable {
		// Given
		WithdrawalVestingEngine underTest = new WithdrawalVestingEngine();

		// When
		EmployeeVestingInformation employeeVestingInformation = mockEmployeeVestingInformation2();
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest4();
		boolean result = underTest.isVestingNonCriticalErrorWithWarning(employeeVestingInformation, withdrawalRequest);

		// Then
		// assertFalse(result);
	}
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of EmployeeVestingInformation
	 */
	private static EmployeeVestingInformation mockEmployeeVestingInformation2() throws Throwable {
		EmployeeVestingInformation employeeVestingInformation = mock(EmployeeVestingInformation.class);
		Set getErrorsResult = new HashSet(); // UTA: default value
		when(employeeVestingInformation.getErrors()).thenReturn(getErrorsResult);

		Map getMoneyTypeVestingPercentagesResult = new HashMap(); // UTA: default value
		when(employeeVestingInformation.getMoneyTypeVestingPercentages())
				.thenReturn(getMoneyTypeVestingPercentagesResult);
		return employeeVestingInformation;
	}
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest4() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		WithdrawalRequestMoneyType withdrawalRequestMoneyType= mock(WithdrawalRequestMoneyType.class);
		
		getMoneyTypesResult.add(withdrawalRequestMoneyType);
		doReturn(getMoneyTypesResult).when(withdrawalRequest).getMoneyTypes();
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(String)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalVestingEngine#lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(String)
	 * @author patelpo
	 */
	@Test
	public void testLookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode() throws Throwable {
		// When
		String reasonCode = "TE"; // UTA: default value
		String result = WithdrawalVestingEngine.lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(reasonCode);

		// Then
		 assertNotNull(result);
		 assertEquals("TE", result);
	}
	@Test
	public void testLookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode_1() throws Throwable {
		// When
		String reasonCode = "DE"; // UTA: default value
		String result = WithdrawalVestingEngine.lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(reasonCode);
		
		// Then
		assertNotNull(result);
		assertEquals("DE", result);
	}
	@Test
	public void testLookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode_2() throws Throwable {
		// When
		String reasonCode = "DI"; // UTA: default value
		String result = WithdrawalVestingEngine.lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(reasonCode);
		
		// Then
		assertNotNull(result);
		assertEquals("DI", result);
	}
	@Test
	public void testLookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode_3() throws Throwable {
		// When
		String reasonCode = "HA"; // UTA: default value
		String result = WithdrawalVestingEngine.lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(reasonCode);
		
		// Then
		assertNotNull(result);
		assertEquals("HA", result);
	}
	@Test
	public void testLookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode_4() throws Throwable {
		// When
		String reasonCode = "MT"; // UTA: default value
		String result = WithdrawalVestingEngine.lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(reasonCode);
		
		// Then
		assertNotNull(result);
		assertEquals("MT", result);
	}
	@Test
	public void testLookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode_5() throws Throwable {
		// When
		String reasonCode = "MD"; // UTA: default value
		String result = WithdrawalVestingEngine.lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(reasonCode);
		
		// Then
		assertNotNull(result);
		assertEquals("MD", result);
	}
	@Test
	public void testLookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode_6() throws Throwable {
		// When
		String reasonCode = "RE"; // UTA: default value
		String result = WithdrawalVestingEngine.lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(reasonCode);
		
		// Then
		assertNotNull(result);
		assertEquals("RE", result);
	}
	@Test
	public void testLookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode_7() throws Throwable {
		// When
		String reasonCode = "IR"; // UTA: default value
		String result = WithdrawalVestingEngine.lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(reasonCode);
		
		// Then
		assertNotNull(result);
		assertEquals("IR", result);
	}
	@Test
	public void testLookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode_8() throws Throwable {
		// When
		String reasonCode = "PD"; // UTA: default value
		String result = WithdrawalVestingEngine.lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(reasonCode);
		
		// Then
		assertNotNull(result);
		assertEquals("PD", result);
	}
	@Test
	public void testLookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode_9() throws Throwable {
		// When
		String reasonCode = "VC"; // UTA: default value
		String result = WithdrawalVestingEngine.lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(reasonCode);
		
		// Then
		assertNotNull(result);
		assertEquals("VC", result);
	}
}