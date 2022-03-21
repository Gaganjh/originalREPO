/**
 * 
 */
package com.manulife.pension.service.withdrawal.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.util.JdbcHelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalLookupDataManager
 *
 * @see com.manulife.pension.service.withdrawal.common.WithdrawalLookupDataManager
 * @author patelpo
 */
@PrepareForTest({ EnvironmentServiceDelegate.class, JdbcHelper.class, Logger.class, WithdrawalLookupDataManager.class })
@RunWith(PowerMockRunner.class)
public class WithdrawalLookupDataManagerTest {
	Logger getLoggerResult = mock(Logger.class); // UTA: default value
	DataSource getCachedDataSourceResult = mock(DataSource.class);
	Connection getConnectionResult = mock(Connection.class);
	PreparedStatement prepareStatementResult = mock(PreparedStatement.class);
	Statement statementResult = mock(Statement.class);
	CallableStatement callableStatementResult = mock(CallableStatement.class);
	ResultSet executeQueryResult = mock(ResultSet.class);

	@Before
	public void setUp() throws Exception {
		spy(JdbcHelper.class);

		spy(Logger.class);

		doReturn(getLoggerResult).when(Logger.class, "getLogger", Mockito.any(Class.class));
		when(getLoggerResult.isDebugEnabled()).thenReturn(true);

		when(statementResult.executeQuery(anyString())).thenReturn(executeQueryResult);
		when(prepareStatementResult.executeQuery()).thenReturn(executeQueryResult);
		when(callableStatementResult.executeQuery(anyString())).thenReturn(executeQueryResult);
		when(getConnectionResult.prepareStatement(anyString())).thenReturn(prepareStatementResult);
		when(getConnectionResult.createStatement()).thenReturn(statementResult);
		when(getConnectionResult.prepareCall(anyString())).thenReturn(callableStatementResult);
		when(getCachedDataSourceResult.getConnection()).thenReturn(getConnectionResult);
		doReturn(getCachedDataSourceResult).when(JdbcHelper.class, "getCachedDataSource", anyString());

	}

	/**
	 * Parasoft Jtest UTA: Test for filterDefaultUnvestedMoneyOption(ContractInfo, Collection)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalLookupDataManager#filterDefaultUnvestedMoneyOption(ContractInfo, Collection)
	 * @author patelpo
	 */
	@Test
	public void testFilterDefaultUnvestedMoneyOption() throws Throwable {
		// Given
		ContractInfo contractInfo = mockContractInfo();
		String participantStatusCode = "PS"; // UTA: default value
		Collection<String> lookupKeys = new ArrayList<String>(); // UTA: default value
		lookupKeys.add("ONLINE_WITHDRAWAL_REASONS");
		WithdrawalLookupDataManager underTest = new WithdrawalLookupDataManager(contractInfo, participantStatusCode,
				lookupKeys);

		// When
		ContractInfo contractInfo2 = mockContractInfo2();
		Collection<DeCodeVO> unvestedOptions = new ArrayList<DeCodeVO>(); // UTA: default value
		DeCodeVO deCodeVO = new DeCodeVO("Test", "CA");
		unvestedOptions.add(deCodeVO);

		String result = underTest.filterDefaultUnvestedMoneyOption(contractInfo2, unvestedOptions);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo() throws Throwable {
		ContractInfo contractInfo = mock(ContractInfo.class);
		String getDefaultUnvestedMoneyOptionCodeResult = "CA"; // UTA: default value
		when(contractInfo.getDefaultUnvestedMoneyOptionCode()).thenReturn(getDefaultUnvestedMoneyOptionCodeResult);
		return contractInfo;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo2() throws Throwable {
		ContractInfo contractInfo2 = mock(ContractInfo.class);
		String getDefaultUnvestedMoneyOptionCodeResult2 = "CA"; // UTA: default value
		when(contractInfo2.getDefaultUnvestedMoneyOptionCode()).thenReturn(getDefaultUnvestedMoneyOptionCodeResult2);
		return contractInfo2;
	}

	/**
	 * Parasoft Jtest UTA: Test for filteredOnlineWithdrawalReasonsList(Collection)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalLookupDataManager#filteredOnlineWithdrawalReasonsList(Collection)
	 * @author patelpo
	 */
	@Test
	public void testFilteredOnlineWithdrawalReasonsList() throws Throwable {
		// Given
		ContractInfo contractInfo = mockContractInfo3();
		String participantStatusCode = ""; // UTA: default value
		Collection<String> lookupKeys = new ArrayList<String>(); // UTA: default value
		WithdrawalLookupDataManager underTest = new WithdrawalLookupDataManager(contractInfo, participantStatusCode,
				lookupKeys);

		// When
		Collection<WithdrawalReason> contractReasons = new ArrayList<WithdrawalReason>(); // UTA: default value
		Collection<DeCodeVO> lookupReasons = new ArrayList<DeCodeVO>(); // UTA: default value

		Collection<DeCodeVO> result = underTest.filteredOnlineWithdrawalReasonsList(contractReasons, lookupReasons);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	@Test
	public void testFilteredOnlineWithdrawalReasonsList_1() throws Throwable {
		// Given
		ContractInfo contractInfo = mockContractInfo3_1();
		String participantStatusCode = "PA"; // UTA: default value
		Collection<String> lookupKeys = new ArrayList<String>(); // UTA: default value
		lookupKeys.add("ONLINE_WITHDRAWAL_REASONS");
		WithdrawalLookupDataManager underTest = new WithdrawalLookupDataManager(contractInfo, participantStatusCode,
				lookupKeys);

		// When
		Collection<WithdrawalReason> contractReasons = new ArrayList<WithdrawalReason>(); // UTA: default value
		WithdrawalReason withdrawalReason = new WithdrawalReason("Test", "Result");
		contractReasons.add(withdrawalReason);

		Collection<DeCodeVO> lookupReasons = new ArrayList<DeCodeVO>(); // UTA: default value
		DeCodeVO deCodeVO = new DeCodeVO("Test", "Result");
		lookupReasons.add(deCodeVO);
		Collection<DeCodeVO> result = underTest.filteredOnlineWithdrawalReasonsList(contractReasons, lookupReasons);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo3() throws Throwable {
		ContractInfo contractInfo = mock(ContractInfo.class);
		String toStringResult = ""; // UTA: default value
		when(contractInfo.toString()).thenReturn(toStringResult);
		return contractInfo;
	}

	private static ContractInfo mockContractInfo3_1() throws Throwable {
		ContractInfo contractInfo = mock(ContractInfo.class);
		String toStringResult = "CA"; // UTA: default value
		when(contractInfo.toString()).thenReturn(toStringResult);
		return contractInfo;
	}

	/**
	 * Parasoft Jtest UTA: Test for getLookupData()
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalLookupDataManager#getLookupData()
	 * @author patelpo
	 */
	@Test
	public void testGetLookupData() throws Throwable {
		spy(EnvironmentServiceDelegate.class);

		EnvironmentServiceDelegate getInstanceResult = mock(EnvironmentServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EnvironmentServiceDelegate.class, "getInstance", anyString());

		// Given
		ContractInfo contractInfo = mockContractInfo4();
		String participantStatusCode = "PS"; // UTA: default value

		/*Collection<DeCodeVO> optionsFromLookup = new ArrayList<DeCodeVO>();
		DeCodeVO deCodeVO = new DeCodeVO("Test", "Result");
		optionsFromLookup.add(deCodeVO);*/

		Collection<String> lookupKeys = new ArrayList<String>(); // UTA: default value
		lookupKeys.add("ONLINE_WITHDRAWAL_REASONS");

		WithdrawalLookupDataManager underTest = new WithdrawalLookupDataManager(contractInfo, participantStatusCode,
				lookupKeys);

		// When
		Map result = underTest.getLookupData();

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
	}

	@Test
	public void testGetLookupData_1() throws Throwable {
		spy(EnvironmentServiceDelegate.class);

		EnvironmentServiceDelegate getInstanceResult = mock(EnvironmentServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EnvironmentServiceDelegate.class, "getInstance", anyString());

		// Given
		ContractInfo contractInfo = null;
		String participantStatusCode = "PS"; // UTA: default value

		Collection<String> lookupKeys = new ArrayList<String>(); // UTA: default value
		lookupKeys.add("LOAN_OPTION_TYPE");

		WithdrawalLookupDataManager underTest = new WithdrawalLookupDataManager(contractInfo, participantStatusCode,
				lookupKeys);

		// When
		Map result = underTest.getLookupData();

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
	}

	@Test
	public void testGetLookupData_2() throws Throwable {
		spy(EnvironmentServiceDelegate.class);

		EnvironmentServiceDelegate getInstanceResult = mock(EnvironmentServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EnvironmentServiceDelegate.class, "getInstance", anyString());

		// Given
		ContractInfo contractInfo = mockContractInfo4();
		String participantStatusCode = "PS"; // UTA: default value

		Collection<String> lookupKeys = new ArrayList<String>(); // UTA: default value
		lookupKeys.add("Test");

		WithdrawalLookupDataManager underTest = new WithdrawalLookupDataManager(contractInfo, participantStatusCode,
				lookupKeys);

		// When
		Map result = underTest.getLookupData();

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
	}
	@Test
	public void testGetLookupData_3() throws Throwable {
		spy(EnvironmentServiceDelegate.class);
		
		EnvironmentServiceDelegate getInstanceResult = mock(EnvironmentServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EnvironmentServiceDelegate.class, "getInstance", anyString());
		Map data = new HashMap<>();
		Collection<DeCodeVO> decode = new ArrayList<DeCodeVO>(); 
		DeCodeVO codeVO = mock(DeCodeVO.class);
		when(codeVO.getCode()).thenReturn("Test");
		decode.add(codeVO);
		data.put("OPTIONS_FOR_UNVESTED_AMOUNTS", decode);
		when(getInstanceResult.getLookupData(any(Collection.class))).thenReturn(data);
		
		// Given
		ContractInfo contractInfo = mockContractInfo4();
		String participantStatusCode = "PS"; // UTA: default value
		
		Collection<String> lookupKeys = new ArrayList<String>(); // UTA: default value
		lookupKeys.add("OPTIONS_FOR_UNVESTED_AMOUNTS");
		
		WithdrawalLookupDataManager underTest = new WithdrawalLookupDataManager(contractInfo, participantStatusCode,
				lookupKeys);
		
		// When
		Map result = underTest.getLookupData();
		
		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
	}
	@Test(expected = EJBException.class)
	public void testGetLookupData_Exception() throws Throwable {
		
		// Given
		ContractInfo contractInfo = mockContractInfo4();
		String participantStatusCode = "PS"; // UTA: default value


		Collection<String> lookupKeys = new ArrayList<String>(); // UTA: default value
		lookupKeys.add("ONLINE_WITHDRAWAL_REASONS");

		WithdrawalLookupDataManager underTest = new WithdrawalLookupDataManager(contractInfo, participantStatusCode,
				lookupKeys);

		// When
		Map result = underTest.getLookupData();

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
	}

	
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo4() throws Throwable {
		ContractInfo contractInfo = mock(ContractInfo.class);
		String getDefaultUnvestedMoneyOptionCodeResult = "TEST"; // UTA: default value
		when(contractInfo.getDefaultUnvestedMoneyOptionCode()).thenReturn(getDefaultUnvestedMoneyOptionCodeResult);

		String getStatusResult = "RESULT"; // UTA: default value
		when(contractInfo.getStatus()).thenReturn(getStatusResult);

		Collection<DeCodeVO> getUnvestedMoneyOptionsResult = new ArrayList<DeCodeVO>(); // UTA: default value
		DeCodeVO deCodeVO=mock(DeCodeVO.class);
		getUnvestedMoneyOptionsResult.add(deCodeVO);
		doReturn(getUnvestedMoneyOptionsResult).when(contractInfo).getUnvestedMoneyOptions();

		Collection<WithdrawalReason> getWithdrawalReasonsResult = new ArrayList<WithdrawalReason>(); // UTA: default value
		WithdrawalReason withdrawalReason = new WithdrawalReason("Test", "Result");	
		getWithdrawalReasonsResult.add(withdrawalReason);
		doReturn(getWithdrawalReasonsResult).when(contractInfo).getWithdrawalReasons();

		String toStringResult = "TEST1"; // UTA: default value
		when(contractInfo.toString()).thenReturn(toStringResult);
		return contractInfo;
	}
}