/**
 * 
 */
package com.manulife.pension.service.withdrawal.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.intware.dao.jdbc.SelectBeanQueryHandler;
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.account.valueobject.CustomerServicePrincipal;
import com.manulife.pension.service.account.valueobject.ParticipantDataValueObject;
import com.manulife.pension.service.contract.dao.ContractServiceFeatureDAO;
import com.manulife.pension.service.contract.valueobject.ContactVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.ContractVO;
import com.manulife.pension.service.contract.valueobject.PlanDataWithdrawalVO;
import com.manulife.pension.service.employee.valueobject.BasicAddressVO;
import com.manulife.pension.service.loan.dao.LoanSupportDao;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.withdrawal.dao.WithdrawalDao;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;
import com.manulife.pension.util.JdbcHelper;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalDataManager
 *
 * @see com.manulife.pension.service.withdrawal.common.WithdrawalDataManager
 * @author patelpo
 */
@PrepareForTest({ Principal.class, EnvironmentServiceDelegate.class, AccountServiceDelegate.class, JdbcHelper.class,
		Logger.class, WithdrawalDataManager.class, ContractServiceDelegate.class, WithdrawalDao.class,
		LoanDataHelper.class, ContractServiceFeatureDAO.class, TPAServiceDelegate.class })
@RunWith(PowerMockRunner.class)
public class WithdrawalDataManagerTest {
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
	 * Parasoft Jtest UTA: Test for getParticipantEnrollmentDate(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalDataManager#getParticipantEnrollmentDate(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetParticipantEnrollmentDate() throws Throwable {
		spy(AccountServiceDelegate.class);

		AccountServiceDelegate getInstanceResult = mock(AccountServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(AccountServiceDelegate.class, "getInstance");
		ParticipantDataValueObject participantDataValueObject = new ParticipantDataValueObject();
		when(getInstanceResult.getParticipantDataValueObject(any(CustomerServicePrincipal.class), anyString(), anyString())).thenReturn(participantDataValueObject);

		// When
		Integer profileId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Date result = WithdrawalDataManager.getParticipantEnrollmentDate(profileId, contractId);

		// Then
		assertNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getParticipantInfo(int, int, int)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalDataManager#getParticipantInfo(int, int, int)
	 * @author patelpo
	 */
	@Test
	public void testGetParticipantInfo() throws Throwable {
		spy(AccountServiceDelegate.class);

		AccountServiceDelegate getInstanceResult = mock(AccountServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(AccountServiceDelegate.class, "getInstance");

		// When
		int profileId = 0; // UTA: default value
		int participantId = 0; // UTA: default value
		int contractId = 0; // UTA: default value
		ParticipantInfo result = WithdrawalDataManager.getParticipantInfo(profileId, participantId, contractId);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getParticipantPaymentToOptions(ParticipantInfo)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalDataManager#getParticipantPaymentToOptions(ParticipantInfo)
	 * @author patelpo
	 */
	@Test
	public void testGetParticipantPaymentToOptions() throws Throwable {

		spy(EnvironmentServiceDelegate.class);

		Map map = new HashMap<>();
		EnvironmentServiceDelegate getInstanceResult = mock(EnvironmentServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EnvironmentServiceDelegate.class);
		EnvironmentServiceDelegate.getInstance(nullable(String.class));
		when(getInstanceResult.getLookupData(any(Collection.class))).thenReturn(map);

		// When
		ParticipantInfo participantInfo = mockParticipantInfo();
		Collection result = WithdrawalDataManager.getParticipantPaymentToOptions(participantInfo);

		// Then
		 assertNotNull(result);
	     assertEquals(0, result.size());
	}
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo() throws Throwable {
		ParticipantInfo participantInfo = mock(ParticipantInfo.class);
		String getChequePayableToCodeResult = "TR"; // UTA: default value
		when(participantInfo.getChequePayableToCode()).thenReturn(getChequePayableToCodeResult);

		Boolean getHasAfterTaxContributionsResult = false; // UTA: default value
		when(participantInfo.getHasAfterTaxContributions()).thenReturn(getHasAfterTaxContributionsResult);

		boolean getIsMTAContractResult = true; // UTA: default value
		when(participantInfo.getIsMTAContract()).thenReturn(getIsMTAContractResult);
		return participantInfo;
	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalInfo(int, int)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalDataManager#getWithdrawalInfo(int, int)
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalInfo() throws Throwable {
		// When
		int participantId = 0; // UTA: default value
		int contractId = 0; // UTA: default value
		WithdrawalInfo result = WithdrawalDataManager.getWithdrawalInfo(participantId, contractId);

		// Then
		 assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalRequestMetaData(Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalDataManager#getWithdrawalRequestMetaData(Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalRequestMetaData() throws Throwable {
		
		SelectBeanQueryHandler newSelectBeanQueryHandlerResult = mock(SelectBeanQueryHandler.class);
		whenNew(SelectBeanQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanQueryHandlerResult);
		WithdrawalRequestMetaData withdrawalRequestMetaData = new WithdrawalRequestMetaData();
		when(newSelectBeanQueryHandlerResult.select(any(Object[].class))).thenReturn(withdrawalRequestMetaData);

		// When
		Integer submissionId = 0; // UTA: default value
		WithdrawalRequestMetaData result = WithdrawalDataManager.getWithdrawalRequestMetaData(submissionId);

		// Then
		 assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for loadContractInfo(Integer, Principal)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalDataManager#loadContractInfo(Integer, Principal)
	 * @author patelpo
	 */
	@Test
	public void testLoadContractInfo() throws Throwable {
		spy(TPAServiceDelegate.class);

		TPAFirmInfo firmInfo = new TPAFirmInfo();
		TPAServiceDelegate getInstanceResult3 = mock(TPAServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult3).when(TPAServiceDelegate.class, "getInstance");
		when(getInstanceResult3.getFirmInfoByContractId(anyInt())).thenReturn(firmInfo);

		ContractInfo newContractInfoResult = mock(ContractInfo.class); // UTA: default value
		whenNew(ContractInfo.class).withAnyArguments().thenReturn(newContractInfoResult);
				
		spy(ContractServiceFeatureDAO.class);
		Map getContractServiceFeaturesResult = new HashMap(); // UTA: default value
		doReturn(getContractServiceFeaturesResult).when(ContractServiceFeatureDAO.class, "getContractServiceFeatures",
				anyInt(), any(Collection.class));

		

		LoanSupportDao newLoanSupportDaoResult = mock(LoanSupportDao.class); // UTA: default value
		whenNew(LoanSupportDao.class).withAnyArguments().thenReturn(newLoanSupportDaoResult);
		when(newLoanSupportDaoResult.hasLoanRecordKeepingProductFeature(anyInt())).thenReturn(true);

		spy(ContractServiceDelegate.class);

		Contract contract = mock(Contract.class);
		PlanDataWithdrawalVO planDataWithdrawalVO = new PlanDataWithdrawalVO();
		
		ContactVO contactVO = new ContactVO();
		ContractServiceFeature contractServiceFeature = mock(ContractServiceFeature.class);
		ContractServiceDelegate getInstanceResult2 = mock(ContractServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult2).when(ContractServiceDelegate.class, "getInstance");
		when(getInstanceResult2.getContractDetails(anyInt(), anyInt())).thenReturn(contract);
		when(getInstanceResult2.getContractServiceFeature(anyInt(), anyString())).thenReturn(contractServiceFeature);
		when(getInstanceResult2.getPlanDataWithdrawal(anyInt())).thenReturn(planDataWithdrawalVO);
		when(getInstanceResult2.getContactsDetail(anyInt())).thenReturn(contactVO);

		spy(EnvironmentServiceDelegate.class);

		EnvironmentServiceDelegate getInstanceResult = mock(EnvironmentServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EnvironmentServiceDelegate.class, "getInstance");
		
		// When
		Integer contractId = 0; // UTA: default value
		Principal principal = mockPrincipal();
		ContractInfo result = WithdrawalDataManager.loadContractInfo(contractId, principal);

		// Then
		 assertNotNull(result);
	}
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of UserRole
	 */
	private static UserRole mockUserRole() throws Throwable {
		UserRole getRoleResult = mock(UserRole.class);
		String toStringResult = ""; // UTA: default value
		when(getRoleResult.toString()).thenReturn(toStringResult);
		return getRoleResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Principal
	 */
	private static Principal mockPrincipal() throws Throwable {
		Principal principal = mock(Principal.class);
		UserRole getRoleResult = mockUserRole();
		when(principal.getRole()).thenReturn(getRoleResult);

		String toStringResult2 = ""; // UTA: default value
		when(principal.toString()).thenReturn(toStringResult2);
		return principal;
	}
}