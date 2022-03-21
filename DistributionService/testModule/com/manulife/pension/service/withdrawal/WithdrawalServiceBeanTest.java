/**
 * 
 */
package com.manulife.pension.service.withdrawal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.intware.dao.jdbc.SelectBeanQueryHandler;
import com.intware.dao.jdbc.SelectSingleValueQueryHandler;
import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
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
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.AtRiskAddressChangeVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskForgetUserName;
import com.manulife.pension.service.distribution.valueobject.AtRiskPasswordResetVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskWebRegistrationVO;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.environment.valueobject.FederalTaxVO;
import com.manulife.pension.service.loan.dao.LoanSupportDao;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.PrincipalFactory;
import com.manulife.pension.service.security.SystemUser;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.withdrawal.common.WithdrawalDataManager;
import com.manulife.pension.service.withdrawal.dao.WithdrawalDao;
import com.manulife.pension.service.withdrawal.dao.WithdrawalInfoDao;
import com.manulife.pension.service.withdrawal.domain.Withdrawal;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.PendingReviewApproveWithdrawalCount;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.JdbcHelper;
import com.manulife.pension.util.log.EventLog;
import com.manulife.pension.util.log.EventLogFactory;
import java.math.BigDecimal;
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

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalServiceBean
 *
 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean
 * @author patelpo
 */
@PrepareForTest({ Principal.class, WithdrawalRequest.class, AtRiskAddressChangeVO.class, AtRiskForgetUserName.class,
		JdbcHelper.class, Logger.class, EnvironmentServiceDelegate.class, ContractServiceDelegate.class,
		LoanSupportDao.class, ContractServiceFeatureDAO.class, ContractInfo.class, AccountServiceDelegate.class,
		Withdrawal.class, WithdrawalServiceBean.class, EmployeeServiceDelegate.class, WithdrawalDataManager.class,
		TPAServiceDelegate.class, WithdrawalDao.class, PrincipalFactory.class, WithdrawalInfoDao.class,
		EventLogFactory.class })
@RunWith(PowerMockRunner.class)
public class WithdrawalServiceBeanTest {
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
	 * Parasoft Jtest UTA: Test for approve(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#approve(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testApprove() throws Throwable {

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest();

		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);

		WithdrawalRequest result = underTest.approve(withdrawalRequest);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for delete(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#delete(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testDelete() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest2();
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);
		underTest.delete(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest2() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for deny(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#deny(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testDeny() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest3();

		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);

		underTest.deny(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest3() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for ejbActivate()
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#ejbActivate()
	 * @author patelpo
	 */
	@Test
	public void testEjbActivate() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		underTest.ejbActivate();

	}

	/**
	 * Parasoft Jtest UTA: Test for ejbCreate()
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#ejbCreate()
	 * @author patelpo
	 */
	@Test
	public void testEjbCreate() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		underTest.ejbCreate();

	}

	/**
	 * Parasoft Jtest UTA: Test for ejbPassivate()
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#ejbPassivate()
	 * @author patelpo
	 */
	@Test
	public void testEjbPassivate() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		underTest.ejbPassivate();

	}

	/**
	 * Parasoft Jtest UTA: Test for ejbRemove()
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#ejbRemove()
	 * @author patelpo
	 */
	@Test
	public void testEjbRemove() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		underTest.ejbRemove();

	}

	/**
	 * Parasoft Jtest UTA: Test for expireSingleWithdrawal(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#expireSingleWithdrawal(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testExpireSingleWithdrawal() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer profileId = 0; // UTA: default value
		boolean result = underTest.expireSingleWithdrawal(submissionId, profileId);

		// Then
		// assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getAgreedLegaleseText(Integer, String)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getAgreedLegaleseText(Integer, String)
	 * @author patelpo
	 */
	@Test
	public void testGetAgreedLegaleseText() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer submissionId = 0; // UTA: default value
		String cmaSiteCode = ""; // UTA: default value
		String result = underTest.getAgreedLegaleseText(submissionId, cmaSiteCode);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getAllStateTaxOptions(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getAllStateTaxOptions(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testGetAllStateTaxOptions() throws Throwable {

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest4();

		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);

		Collection result = underTest.getAllStateTaxOptions(withdrawalRequest);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest4() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getReasonCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getReasonCode()).thenReturn(getReasonCodeResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for getContractInfo(Integer, Principal)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getContractInfo(Integer, Principal)
	 * @author patelpo
	 */
	@Test
	public void testGetContractInfo() throws Throwable {

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

		Integer count = new Integer(10);
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(
				SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenReturn(count);

		spy(ContractServiceDelegate.class);

		PlanDataWithdrawalVO planDataWithdrawalVO = new PlanDataWithdrawalVO();
		ContractServiceFeature feature = new ContractServiceFeature();
		Contract contract = mock(Contract.class);
		ContactVO contactVO = new ContactVO();
		ContractServiceDelegate getInstanceResult2 = mock(ContractServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult2).when(ContractServiceDelegate.class, "getInstance");
		when(getInstanceResult2.getContractDetails(anyInt(), anyInt())).thenReturn(contract);
		when(getInstanceResult2.getContractServiceFeature(anyInt(), anyString())).thenReturn(feature);
		when(getInstanceResult2.getPlanDataWithdrawal(anyInt())).thenReturn(planDataWithdrawalVO);
		when(getInstanceResult2.getContactsDetail(anyInt())).thenReturn(contactVO);

		spy(EnvironmentServiceDelegate.class);

		Integer contractDiDuration = new Integer(10);
		EnvironmentServiceDelegate getInstanceResult = mock(EnvironmentServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EnvironmentServiceDelegate.class, "getInstance");
		when(getInstanceResult.retrieveContractDiDuration(any(UserRole.class), anyInt(), any(Date.class)))
				.thenReturn(contractDiDuration);

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer contractId = 0; // UTA: default value
		Principal principal = mockPrincipal();
		ContractInfo result = underTest.getContractInfo(contractId, principal);

		// Then
		// assertNotNull(result);
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

	/**
	 * Parasoft Jtest UTA: Test for getLookupData(ContractInfo, String, Collection)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getLookupData(ContractInfo, String, Collection)
	 * @author patelpo
	 */
	@Test
	public void testGetLookupData() throws Throwable {
		spy(EnvironmentServiceDelegate.class);

		EnvironmentServiceDelegate getInstanceResult = mock(EnvironmentServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EnvironmentServiceDelegate.class, "getInstance", anyString());

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		ContractInfo contractInfo = mockContractInfo();
		String participantStatusCode = ""; // UTA: default value
		Collection<String> keys = new ArrayList<String>(); // UTA: default value
		Map result = underTest.getLookupData(contractInfo, participantStatusCode, keys);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo() throws Throwable {
		ContractInfo contractInfo = mock(ContractInfo.class);
		String getDefaultUnvestedMoneyOptionCodeResult = ""; // UTA: default value
		when(contractInfo.getDefaultUnvestedMoneyOptionCode()).thenReturn(getDefaultUnvestedMoneyOptionCodeResult);

		String getStatusResult = ""; // UTA: default value
		when(contractInfo.getStatus()).thenReturn(getStatusResult);

		Collection<DeCodeVO> getUnvestedMoneyOptionsResult = new ArrayList<DeCodeVO>(); // UTA: default value
		doReturn(getUnvestedMoneyOptionsResult).when(contractInfo).getUnvestedMoneyOptions();

		Collection<WithdrawalReason> getWithdrawalReasonsResult = new ArrayList<WithdrawalReason>(); // UTA: default value
		doReturn(getWithdrawalReasonsResult).when(contractInfo).getWithdrawalReasons();
		return contractInfo;
	}

	/**
	 * Parasoft Jtest UTA: Test for getMostRecentWithdrawalRequest(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getMostRecentWithdrawalRequest(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetMostRecentWithdrawalRequest() throws Throwable {
		spy(Withdrawal.class);

		WithdrawalRequest getMostRecentWithdrawalRequestResult = mock(WithdrawalRequest.class); // UTA: default value
		doReturn(getMostRecentWithdrawalRequestResult).when(Withdrawal.class, "getMostRecentWithdrawalRequest",
				any(Integer.class), any(Integer.class));

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer profileId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		WithdrawalRequest result = underTest.getMostRecentWithdrawalRequest(profileId, contractId);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getNumberOfCompletedWithdrawalTransaction(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getNumberOfCompletedWithdrawalTransaction(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetNumberOfCompletedWithdrawalTransaction() throws Throwable {
		spy(AccountServiceDelegate.class);

		CustomerServicePrincipal principal = new CustomerServicePrincipal();
		AccountServiceDelegate getInstanceResult = mock(AccountServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(AccountServiceDelegate.class, "getInstance");
		when(getInstanceResult.isAPOLLOAvailable(principal)).thenReturn(true);

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();
		SessionContext ctx = mockSessionContext();
		underTest.setSessionContext(ctx);

		// When
		Integer contractId = 0; // UTA: default value
		Integer employeeProfileId = 0; // UTA: default value
		int result = underTest.getNumberOfCompletedWithdrawalTransaction(contractId, employeeProfileId);

		// Then
		// assertEquals(0, result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of SessionContext
	 */
	private static SessionContext mockSessionContext() throws Throwable {
		SessionContext ctx = mock(SessionContext.class);
		Object lookupResult = new Object(); // UTA: default value
		when(ctx.lookup(anyString())).thenReturn(lookupResult);
		return ctx;
	}

	/**
	 * Parasoft Jtest UTA: Test for getNumberOfPendingWithdrawalTransaction(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getNumberOfPendingWithdrawalTransaction(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetNumberOfPendingWithdrawalTransaction() throws Throwable {
		spy(AccountServiceDelegate.class);

		CustomerServicePrincipal customerServicePrincipal = new CustomerServicePrincipal();
		AccountServiceDelegate getInstanceResult = mock(AccountServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(AccountServiceDelegate.class, "getInstance");
		when(getInstanceResult.isAPOLLOAvailable(customerServicePrincipal)).thenReturn(true);

		spy(Withdrawal.class);

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();
		SessionContext ctx = mockSessionContext2();
		underTest.setSessionContext(ctx);

		// When
		Integer contractId = 0; // UTA: default value
		Integer employeeProfileId = 0; // UTA: default value
		int result = underTest.getNumberOfPendingWithdrawalTransaction(contractId, employeeProfileId);

		// Then
		// assertEquals(0, result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of SessionContext
	 */
	private static SessionContext mockSessionContext2() throws Throwable {
		SessionContext ctx = mock(SessionContext.class);
		Object lookupResult = new Object(); // UTA: default value
		when(ctx.lookup(anyString())).thenReturn(lookupResult);
		return ctx;
	}

	/**
	 * Parasoft Jtest UTA: Test for getParticipantInfo(int, int, int)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getParticipantInfo(int, int, int)
	 * @author patelpo
	 */
	@Test
	public void testGetParticipantInfo() throws Throwable {
		spy(AccountServiceDelegate.class);

		ParticipantDataValueObject dataValueObject = new ParticipantDataValueObject();
		AccountServiceDelegate getInstanceResult = mock(AccountServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(AccountServiceDelegate.class, "getInstance");
		when(getInstanceResult.getParticipantDataValueObject(any(CustomerServicePrincipal.class), anyString()))
				.thenReturn(dataValueObject);

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		int profileId = 0; // UTA: default value
		int participantId = 0; // UTA: default value
		int contractId = 0; // UTA: default value
		ParticipantInfo result = underTest.getParticipantInfo(profileId, participantId, contractId);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getParticipantPaymentToOptions(ParticipantInfo)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getParticipantPaymentToOptions(ParticipantInfo)
	 * @author patelpo
	 */
	@Test
	public void testGetParticipantPaymentToOptions() throws Throwable {
		spy(EnvironmentServiceDelegate.class);

		Collection<String> lookupDataKeys = new ArrayList<String>();
		lookupDataKeys.add(CodeLookupCache.PAYMENT_TO_TYPE);
		EnvironmentServiceDelegate getInstanceResult = mock(EnvironmentServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EnvironmentServiceDelegate.class, "getInstance", anyString());
		//when(getInstanceResult.getLookupData(lookupDataKeys)).thenReturn

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		ParticipantInfo participantInfo = mockParticipantInfo();
		Collection result = underTest.getParticipantPaymentToOptions(participantInfo);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo() throws Throwable {
		ParticipantInfo participantInfo = mock(ParticipantInfo.class);
		String getChequePayableToCodeResult = ""; // UTA: default value
		when(participantInfo.getChequePayableToCode()).thenReturn(getChequePayableToCodeResult);

		Boolean getHasAfterTaxContributionsResult = false; // UTA: default value
		when(participantInfo.getHasAfterTaxContributions()).thenReturn(getHasAfterTaxContributionsResult);

		boolean getIsMTAContractResult = false; // UTA: default value
		when(participantInfo.getIsMTAContract()).thenReturn(getIsMTAContractResult);
		return participantInfo;
	}

	/**
	 * Parasoft Jtest UTA: Test for getParticipantWithdrawalReasons(String, String)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getParticipantWithdrawalReasons(String, String)
	 * @author patelpo
	 */
	@Test
	public void testGetParticipantWithdrawalReasons() throws Throwable {
		spy(WithdrawalInfoDao.class);

		Collection<DeCodeVO> getParticipantWithdrawalReasonsResult = new ArrayList<DeCodeVO>(); // UTA: default value
		doReturn(getParticipantWithdrawalReasonsResult).when(WithdrawalInfoDao.class);
		WithdrawalInfoDao.getParticipantWithdrawalReasons(nullable(String.class), nullable(String.class));

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		String contractStatus = ""; // UTA: default value
		String participantStatus = ""; // UTA: default value
		Collection result = underTest.getParticipantWithdrawalReasons(contractStatus, participantStatus);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
	}
	@Test(expected = EJBException.class)
	public void testGetParticipantWithdrawalReasons_Exception() throws Throwable {
		spy(WithdrawalInfoDao.class);
		
		Collection<DeCodeVO> getParticipantWithdrawalReasonsResult = new ArrayList<DeCodeVO>(); // UTA: default value
		PowerMockito.doThrow(new SystemException("")).when(WithdrawalInfoDao.class);
		WithdrawalInfoDao.getParticipantWithdrawalReasons(nullable(String.class), nullable(String.class));
		
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();
		
		// When
		String contractStatus = ""; // UTA: default value
		String participantStatus = ""; // UTA: default value
		Collection result = underTest.getParticipantWithdrawalReasons(contractStatus, participantStatus);
		
		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Test for getPendingReviewApproveWdRequestCounts(Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getPendingReviewApproveWdRequestCounts(Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetPendingReviewApproveWdRequestCounts() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer contractId = 0; // UTA: default value
		PendingReviewApproveWithdrawalCount result = underTest.getPendingReviewApproveWdRequestCounts(contractId);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getRequiresSpousalConsentForDistributions(Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getRequiresSpousalConsentForDistributions(Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetRequiresSpousalConsentForDistributions() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer contractNumber = 0; // UTA: default value
		String result = underTest.getRequiresSpousalConsentForDistributions(contractNumber);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	
	/**
	 * Parasoft Jtest UTA: Test for getUserNames(Collection)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getUserNames(Collection)
	 * @author patelpo
	 */
	@Test
	public void testGetUserNames() throws Throwable {
		spy(WithdrawalInfoDao.class);

		Map<Integer, UserName> getUserNamesResult = new HashMap<Integer, UserName>(); // UTA: default value
		doReturn(getUserNamesResult).when(WithdrawalInfoDao.class);
		WithdrawalInfoDao.getUserNames((Collection) any());

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Collection<Integer> userProfileIds = new ArrayList<Integer>(); // UTA: default value
		Map<Integer, UserName> result = underTest.getUserNames(userProfileIds);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.containsKey(0));
		// assertTrue(result.containsValue(null));
	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalInfo(int, int)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getWithdrawalInfo(int, int)
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalInfo() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		int participantId = 0; // UTA: default value
		int contractId = 0; // UTA: default value
		WithdrawalInfo result = underTest.getWithdrawalInfo(participantId, contractId);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalRequestMetaData(Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getWithdrawalRequestMetaData(Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalRequestMetaData() throws Throwable {

		WithdrawalRequestMetaData metaData = new WithdrawalRequestMetaData();
		SelectBeanQueryHandler newSelectBeanQueryHandlerResult = mock(SelectBeanQueryHandler.class); // UTA: default value
		whenNew(SelectBeanQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanQueryHandlerResult);
		when(newSelectBeanQueryHandlerResult.select(any(Object[].class))).thenReturn(metaData);

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer submissionId = 0; // UTA: default value

		WithdrawalRequestMetaData result = underTest.getWithdrawalRequestMetaData(submissionId);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalRequests(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getWithdrawalRequests(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalRequests() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer profileId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Collection result = underTest.getWithdrawalRequests(profileId, contractId);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Test for handleDisableOnlineWithdrawals(Integer, Integer, String)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#handleDisableOnlineWithdrawals(Integer, Integer, String)
	 * @author patelpo
	 */
/*	@Test
	public void testHandleDisableOnlineWithdrawals() throws Throwable {
		Collection<Integer> wdRequests = new ArrayList<Integer>();
		Integer integer = new Integer(1);
		wdRequests.add(integer);
		WithdrawalDao withdrawalDao = mock(WithdrawalDao.class);
		whenNew(WithdrawalDao.class).withAnyArguments().thenReturn(withdrawalDao);
		when(withdrawalDao.getWithdrawalsByContractAndStatus(anyInt(), any(Collection.class), anyString())).thenReturn(wdRequests);

		
		spy(EventLogFactory.class);

		EventLogFactory getInstanceResult = mock(EventLogFactory.class);
		doReturn(getInstanceResult).when(EventLogFactory.class);
		EventLogFactory.getInstance();
		EventLog eventLog = mock(EventLog.class);
		when(getInstanceResult.createEventLog(anyString())).thenReturn(eventLog);
		
		PowerMockito.doNothing().when(eventLog).addLogInfo(nullable(String.class), nullable(Object.class));
		
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer contractId = 0; // UTA: default value
		Integer profileId = 0; // UTA: default value
		String userRoleCode = ""; // UTA: default value
		underTest.handleDisableOnlineWithdrawals(contractId, profileId, userRoleCode);

	}
*/
	/**
	 * Parasoft Jtest UTA: Test for handleEnableOneStepApprovals(Integer, Principal, String)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#handleEnableOneStepApprovals(Integer, Principal, String)
	 * @author patelpo
	 */
	@Test
	public void testHandleEnableOneStepApprovals() throws Throwable {
		WithdrawalRequest newWithdrawalRequestResult = mock(WithdrawalRequest.class); // UTA: default value
		whenNew(WithdrawalRequest.class).withAnyArguments().thenReturn(newWithdrawalRequestResult);

		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(newWithdrawalRequestResult);

		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		when(executeQueryResult.getInt("submission_id")).thenReturn(1);

		WithdrawalDao dao = mock(WithdrawalDao.class);
		whenNew(WithdrawalDao.class).withAnyArguments().thenReturn(dao);
		Collection<Integer> value = new ArrayList<Integer>();
		value.add(100);
		when(dao.getWithdrawalsByContractAndStatus(any(Integer.class), any(Collection.class), anyString()))
				.thenReturn(value);

		spy(PrincipalFactory.class);

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer contractId = 0; // UTA: default value
		Principal principal = mock(Principal.class);

		PrincipalFactory instanceResult = mock(PrincipalFactory.class); // UTA: default value
		doReturn(instanceResult).when(PrincipalFactory.class, "instance");
		when(instanceResult.getSystemPrincipal(any(SystemUser.class))).thenReturn(principal);

		String cmaSiteCode = ""; // UTA: default value
		underTest.handleEnableOneStepApprovals(contractId, principal, cmaSiteCode);

	}

	/**
	 * Parasoft Jtest UTA: Test for handleEnableOnlineWithdrawals(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#handleEnableOnlineWithdrawals(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testHandleEnableOnlineWithdrawals() throws Throwable {

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer contractId = 0; // UTA: default value
		Integer profileId = 0; // UTA: default value
		underTest.handleEnableOnlineWithdrawals(contractId, profileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for handleEnableTwoStepApprovals(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#handleEnableTwoStepApprovals(Integer, Integer)
	 * @author patelpo
	 */
/*	@Test
	public void testHandleEnableTwoStepApprovals() throws Throwable {
		
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer contractId = 0; // UTA: default value
		Integer profileId = 0; // UTA: default value
		underTest.handleEnableTwoStepApprovals(contractId, profileId);

	}
*/
	/**
	 * Parasoft Jtest UTA: Test for initiateNewParticipantWithdrawalRequest(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#initiateNewParticipantWithdrawalRequest(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testInitiateNewParticipantWithdrawalRequest() throws Throwable {
		spy(EmployeeServiceDelegate.class);

		Employee value = new Employee();
		EmployeeServiceDelegate getInstanceResult = mock(EmployeeServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EmployeeServiceDelegate.class, "getInstance", anyString());
		when(getInstanceResult.getEmployeeByProfileId(any(Long.class), any(Integer.class), any(Date.class)))
				.thenReturn(value);
		BaseEnvironment newBaseEnvironmentResult = mock(BaseEnvironment.class); // UTA: default value
		whenNew(BaseEnvironment.class).withAnyArguments().thenReturn(newBaseEnvironmentResult);
		when(newBaseEnvironmentResult.getAppId()).thenReturn("AppId");
		spy(Withdrawal.class);

		WithdrawalRequest initiateNewParticipantWithdrawalRequestResult = mock(WithdrawalRequest.class);
		doReturn(initiateNewParticipantWithdrawalRequestResult).when(Withdrawal.class);
		Withdrawal.initiateNewParticipantWithdrawalRequest(nullable(Integer.class), nullable(Integer.class));

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer profileId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		WithdrawalRequest result = underTest.initiateNewParticipantWithdrawalRequest(profileId, contractId);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for initiateNewWithdrawalRequest(Integer, Integer, Principal)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#initiateNewWithdrawalRequest(Integer, Integer, Principal)
	 * @author patelpo
	 */
	@Test
	public void testInitiateNewWithdrawalRequest() throws Throwable {
		spy(EmployeeServiceDelegate.class);

		Employee value = new Employee();
		EmployeeServiceDelegate getInstanceResult = mock(EmployeeServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EmployeeServiceDelegate.class, "getInstance", anyString());
		when(getInstanceResult.getEmployeeByProfileId(any(Long.class), any(Integer.class), any(Date.class)))
				.thenReturn(value);

		BaseEnvironment newBaseEnvironmentResult = mock(BaseEnvironment.class); // UTA: default value
		whenNew(BaseEnvironment.class).withAnyArguments().thenReturn(newBaseEnvironmentResult);
		when(newBaseEnvironmentResult.getAppId()).thenReturn("APPID");
		spy(Withdrawal.class);

		WithdrawalRequest initiateNewWithdrawalRequestResult = mock(WithdrawalRequest.class); // UTA: default value
		doReturn(initiateNewWithdrawalRequestResult).when(Withdrawal.class);
		Withdrawal.initiateNewWithdrawalRequest(nullable(Integer.class), nullable(Integer.class),
				nullable(Principal.class));

		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();
		SessionContext ctx = mockSessionContext3();
		underTest.setSessionContext(ctx);

		// When
		Integer profileId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Principal principal = mockPrincipal2();
		WithdrawalRequest result = underTest.initiateNewWithdrawalRequest(profileId, contractId, principal);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of SessionContext
	 */
	private static SessionContext mockSessionContext3() throws Throwable {
		SessionContext ctx = mock(SessionContext.class);
		Object lookupResult = new Object(); // UTA: default value
		when(ctx.lookup(anyString())).thenReturn(lookupResult);
		return ctx;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Principal
	 */
	private static Principal mockPrincipal2() throws Throwable {
		Principal principal = mock(Principal.class);
		UserRole getRoleResult = mock(UserRole.class);
		when(principal.getRole()).thenReturn(getRoleResult);
		return principal;
	}

	/**
	 * Parasoft Jtest UTA: Test for markExpiredWithdrawals(Date, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#markExpiredWithdrawals(Date, Integer)
	 * @author patelpo
	 */
	@Test
	public void testMarkExpiredWithdrawals() throws Throwable {
	/*	Collection<Integer> wdRequests = new ArrayList<Integer>();
		Integer integer = new Integer(1);
		wdRequests.add(integer);
		WithdrawalDao withdrawalDao = mock(WithdrawalDao.class);
		whenNew(WithdrawalDao.class).withAnyArguments().thenReturn(withdrawalDao);
		when(withdrawalDao.getExpiringWithdrawals(any(Date.class))).thenReturn(wdRequests);
		when(withdrawalDao.expireWithdrawal(anyInt(), anyInt())).thenReturn(true);
*/		
		spy(WithdrawalDataManager.class);

		WithdrawalRequestMetaData getWithdrawalRequestMetaDataResult = mock(WithdrawalRequestMetaData.class);
		doReturn(getWithdrawalRequestMetaDataResult).when(WithdrawalDataManager.class);
		WithdrawalDataManager.getWithdrawalRequestMetaData(nullable(Integer.class));

		spy(EventLogFactory.class);

		EventLogFactory getInstanceResult = mock(EventLogFactory.class);
		doReturn(getInstanceResult).when(EventLogFactory.class);
		EventLogFactory.getInstance();
		EventLog eventLog = mock(EventLog.class);
		when(getInstanceResult.createEventLog(anyString())).thenReturn(eventLog);
		
		
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();
		SessionContext ctx = mockSessionContext4();
		underTest.setSessionContext(ctx);

		// When
		Date checkDate = mockDate();
		Integer profileId = 0; // UTA: default value
		int result = underTest.markExpiredWithdrawals(checkDate, profileId);

		// Then
		// assertEquals(0, result);
	}
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of SessionContext
	 */
	private static SessionContext mockSessionContext4() throws Throwable {
		SessionContext ctx = mock(SessionContext.class);
		Object lookupResult = new Object(); // UTA: default value
		when(ctx.lookup(anyString())).thenReturn(lookupResult);
		return ctx;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate() throws Throwable {
		Date checkDate = mock(Date.class);
		long getTimeResult = 0L; // UTA: default value
		when(checkDate.getTime()).thenReturn(getTimeResult);
		return checkDate;
	}

	/**
	 * Parasoft Jtest UTA: Test for performReviewDefaultSetup(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#performReviewDefaultSetup(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testPerformReviewDefaultSetup() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest5();
		
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);
		
		WithdrawalRequest result = underTest.performReviewDefaultSetup(withdrawalRequest);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo2() throws Throwable {
		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		Boolean getHasATpaFirmResult = false; // UTA: default value
		when(getContractInfoResult.getHasATpaFirm()).thenReturn(getHasATpaFirmResult);

		Boolean getSpousalConsentRequiredResult = false; // UTA: default value
		when(getContractInfoResult.getSpousalConsentRequired()).thenReturn(getSpousalConsentRequiredResult);

		Collection<DeCodeVO> getUnvestedMoneyOptionsResult = new ArrayList<DeCodeVO>(); // UTA: default value
		doReturn(getUnvestedMoneyOptionsResult).when(getContractInfoResult).getUnvestedMoneyOptions();

		Collection<WithdrawalReason> getWithdrawalReasonsResult = new ArrayList<WithdrawalReason>(); // UTA: default value
		doReturn(getWithdrawalReasonsResult).when(getContractInfoResult).getWithdrawalReasons();
		return getContractInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate2() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean beforeResult = false; // UTA: default value
		when(getExpirationDateResult.before(any(Date.class))).thenReturn(beforeResult);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of FederalTaxVO
	 */
	private static FederalTaxVO mockFederalTaxVO() throws Throwable {
		FederalTaxVO getFederalTaxVoResult = mock(FederalTaxVO.class);
		Boolean getRolloverIndicatorResult = false; // UTA: default value
		when(getFederalTaxVoResult.getRolloverIndicator()).thenReturn(getRolloverIndicatorResult);

		BigDecimal getTaxPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(getFederalTaxVoResult.getTaxPercentage()).thenReturn(getTaxPercentageResult);
		return getFederalTaxVoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress() throws Throwable {
		DistributionAddress getParticipantAddressResult = mock(DistributionAddress.class);
		String getNonMatchedCountryNameResult = ""; // UTA: default value
		when(getParticipantAddressResult.getNonMatchedCountryName()).thenReturn(getNonMatchedCountryNameResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getParticipantAddressResult.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return getParticipantAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo2() throws Throwable {
		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		String getManulifeCompanyIdResult = ""; // UTA: default value
		when(getParticipantInfoResult.getManulifeCompanyId()).thenReturn(getManulifeCompanyIdResult);

		String getParticipantStatusCodeResult = ""; // UTA: default value
		when(getParticipantInfoResult.getParticipantStatusCode()).thenReturn(getParticipantStatusCodeResult);
		return getParticipantInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate3() throws Throwable {
		Date getRetirementDateResult = mock(Date.class);
		boolean beforeResult2 = false; // UTA: default value
		when(getRetirementDateResult.before(any(Date.class))).thenReturn(beforeResult2);
		return getRetirementDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate4() throws Throwable {
		Date getTerminationDateResult = mock(Date.class);
		boolean beforeResult3 = false; // UTA: default value
		when(getTerminationDateResult.before(any(Date.class))).thenReturn(beforeResult3);
		return getTerminationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Address
	 */
	private static Address mockAddress() throws Throwable {
		Address getTrusteeAddressResult = mock(Address.class);
		String getNonMatchedCountryNameResult2 = ""; // UTA: default value
		when(getTrusteeAddressResult.getNonMatchedCountryName()).thenReturn(getNonMatchedCountryNameResult2);
		return getTrusteeAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest5() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		String getCmaSiteCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mockContractInfo2();
		when(withdrawalRequest.getContractInfo()).thenReturn(getContractInfoResult);

		String getContractIssuedStateCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getContractIssuedStateCode()).thenReturn(getContractIssuedStateCodeResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(withdrawalRequest.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Date getExpirationDateResult = mockDate2();
		when(withdrawalRequest.getExpirationDate()).thenReturn(getExpirationDateResult);

		FederalTaxVO getFederalTaxVoResult = mockFederalTaxVO();
		when(withdrawalRequest.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(withdrawalRequest).getFees();

		boolean getHaveInitiateStep1InitialMessagesLoadedResult = false; // UTA: default value
		when(withdrawalRequest.getHaveInitiateStep1InitialMessagesLoaded())
				.thenReturn(getHaveInitiateStep1InitialMessagesLoadedResult);

		boolean getHaveInitiateStep2InitialMessagesLoadedResult = false; // UTA: default value
		when(withdrawalRequest.getHaveInitiateStep2InitialMessagesLoaded())
				.thenReturn(getHaveInitiateStep2InitialMessagesLoadedResult);

		boolean getHaveReviewAndApproveInitialMessagesLoadedResult = false; // UTA: default value
		when(withdrawalRequest.getHaveReviewAndApproveInitialMessagesLoaded())
				.thenReturn(getHaveReviewAndApproveInitialMessagesLoadedResult);

		boolean getIsCensusInfoAvailableResult = false; // UTA: default value
		when(withdrawalRequest.getIsCensusInfoAvailable()).thenReturn(getIsCensusInfoAvailableResult);

		boolean getIsParticipantCreatedResult = false; // UTA: default value
		when(withdrawalRequest.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult);

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(withdrawalRequest).getMoneyTypes();

		DistributionAddress getParticipantAddressResult = mockDistributionAddress();
		when(withdrawalRequest.getParticipantAddress()).thenReturn(getParticipantAddressResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(withdrawalRequest.getParticipantId()).thenReturn(getParticipantIdResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo2();
		when(withdrawalRequest.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(withdrawalRequest.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		String getParticipantStateOfResidenceResult = ""; // UTA: default value
		when(withdrawalRequest.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = ""; // UTA: default value
		when(withdrawalRequest.getPaymentTo()).thenReturn(getPaymentToResult);

		Principal getPrincipalResult = mock(Principal.class);
		when(withdrawalRequest.getPrincipal()).thenReturn(getPrincipalResult);

		String getReasonCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getReasonCode()).thenReturn(getReasonCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(withdrawalRequest).getRecipients();

		Date getRetirementDateResult = mockDate3();
		when(withdrawalRequest.getRetirementDate()).thenReturn(getRetirementDateResult);

		boolean getShowTaxWitholdingSectionResult = false; // UTA: default value
		when(withdrawalRequest.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);

		Date getTerminationDateResult = mockDate4();
		when(withdrawalRequest.getTerminationDate()).thenReturn(getTerminationDateResult);

		BigDecimal getTotalRequestedWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(withdrawalRequest.getTotalRequestedWithdrawalAmount()).thenReturn(getTotalRequestedWithdrawalAmountResult);

		Address getTrusteeAddressResult = mockAddress();
		when(withdrawalRequest.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);

		Boolean getVestingCalledIndResult = false; // UTA: default value
		when(withdrawalRequest.getVestingCalledInd()).thenReturn(getVestingCalledIndResult);

		boolean getWithdrawalReasonSimpleResult = false; // UTA: default value
		when(withdrawalRequest.getWithdrawalReasonSimple()).thenReturn(getWithdrawalReasonSimpleResult);

		boolean isWmsiSelectedResult = false; // UTA: default value
		when(withdrawalRequest.isWmsiSelected()).thenReturn(isWmsiSelectedResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for performStep1DefaultSetup(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#performStep1DefaultSetup(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testPerformStep1DefaultSetup() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest6();
		
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);
		
		WithdrawalRequest result = underTest.performStep1DefaultSetup(withdrawalRequest);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo3() throws Throwable {
		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		Boolean getHasATpaFirmResult = false; // UTA: default value
		when(getContractInfoResult.getHasATpaFirm()).thenReturn(getHasATpaFirmResult);

		Collection<DeCodeVO> getUnvestedMoneyOptionsResult = new ArrayList<DeCodeVO>(); // UTA: default value
		doReturn(getUnvestedMoneyOptionsResult).when(getContractInfoResult).getUnvestedMoneyOptions();

		Collection<WithdrawalReason> getWithdrawalReasonsResult = new ArrayList<WithdrawalReason>(); // UTA: default value
		doReturn(getWithdrawalReasonsResult).when(getContractInfoResult).getWithdrawalReasons();
		return getContractInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate5() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean beforeResult = false; // UTA: default value
		when(getExpirationDateResult.before(any(Date.class))).thenReturn(beforeResult);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress2() throws Throwable {
		DistributionAddress getParticipantAddressResult = mock(DistributionAddress.class);
		String getNonMatchedCountryNameResult = ""; // UTA: default value
		when(getParticipantAddressResult.getNonMatchedCountryName()).thenReturn(getNonMatchedCountryNameResult);
		return getParticipantAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo3() throws Throwable {
		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		String getParticipantStatusCodeResult = ""; // UTA: default value
		when(getParticipantInfoResult.getParticipantStatusCode()).thenReturn(getParticipantStatusCodeResult);
		return getParticipantInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Address
	 */
	private static Address mockAddress2() throws Throwable {
		Address getTrusteeAddressResult = mock(Address.class);
		String getNonMatchedCountryNameResult2 = ""; // UTA: default value
		when(getTrusteeAddressResult.getNonMatchedCountryName()).thenReturn(getNonMatchedCountryNameResult2);
		return getTrusteeAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest6() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mockContractInfo3();
		when(withdrawalRequest.getContractInfo()).thenReturn(getContractInfoResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(withdrawalRequest.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Date getExpirationDateResult = mockDate5();
		when(withdrawalRequest.getExpirationDate()).thenReturn(getExpirationDateResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(withdrawalRequest).getFees();

		boolean getHaveInitiateStep1InitialMessagesLoadedResult = false; // UTA: default value
		when(withdrawalRequest.getHaveInitiateStep1InitialMessagesLoaded())
				.thenReturn(getHaveInitiateStep1InitialMessagesLoadedResult);

		boolean getHaveInitiateStep2InitialMessagesLoadedResult = false; // UTA: default value
		when(withdrawalRequest.getHaveInitiateStep2InitialMessagesLoaded())
				.thenReturn(getHaveInitiateStep2InitialMessagesLoadedResult);

		boolean getHaveReviewAndApproveInitialMessagesLoadedResult = false; // UTA: default value
		when(withdrawalRequest.getHaveReviewAndApproveInitialMessagesLoaded())
				.thenReturn(getHaveReviewAndApproveInitialMessagesLoadedResult);

		boolean getIsParticipantCreatedResult = false; // UTA: default value
		when(withdrawalRequest.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult);

		DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		when(withdrawalRequest.getParticipantAddress()).thenReturn(getParticipantAddressResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(withdrawalRequest.getParticipantId()).thenReturn(getParticipantIdResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo3();
		when(withdrawalRequest.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getPaymentToResult = ""; // UTA: default value
		when(withdrawalRequest.getPaymentTo()).thenReturn(getPaymentToResult);

		String getReasonCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getReasonCode()).thenReturn(getReasonCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(withdrawalRequest).getRecipients();

		boolean getShowTaxWitholdingSectionResult = false; // UTA: default value
		when(withdrawalRequest.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);

		Address getTrusteeAddressResult = mockAddress2();
		when(withdrawalRequest.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for performStep2DefaultSetup(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#performStep2DefaultSetup(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testPerformStep2DefaultSetup() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest7();
		
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);
		
		WithdrawalRequest result = underTest.performStep2DefaultSetup(withdrawalRequest);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo4() throws Throwable {
		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		Boolean getHasATpaFirmResult = false; // UTA: default value
		when(getContractInfoResult.getHasATpaFirm()).thenReturn(getHasATpaFirmResult);

		Boolean getSpousalConsentRequiredResult = false; // UTA: default value
		when(getContractInfoResult.getSpousalConsentRequired()).thenReturn(getSpousalConsentRequiredResult);

		Collection<DeCodeVO> getUnvestedMoneyOptionsResult = new ArrayList<DeCodeVO>(); // UTA: default value
		doReturn(getUnvestedMoneyOptionsResult).when(getContractInfoResult).getUnvestedMoneyOptions();

		Collection<WithdrawalReason> getWithdrawalReasonsResult = new ArrayList<WithdrawalReason>(); // UTA: default value
		doReturn(getWithdrawalReasonsResult).when(getContractInfoResult).getWithdrawalReasons();
		return getContractInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate6() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean beforeResult = false; // UTA: default value
		when(getExpirationDateResult.before(any(Date.class))).thenReturn(beforeResult);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of FederalTaxVO
	 */
	private static FederalTaxVO mockFederalTaxVO2() throws Throwable {
		FederalTaxVO getFederalTaxVoResult = mock(FederalTaxVO.class);
		Boolean getRolloverIndicatorResult = false; // UTA: default value
		when(getFederalTaxVoResult.getRolloverIndicator()).thenReturn(getRolloverIndicatorResult);

		BigDecimal getTaxPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(getFederalTaxVoResult.getTaxPercentage()).thenReturn(getTaxPercentageResult);
		return getFederalTaxVoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress3() throws Throwable {
		DistributionAddress getParticipantAddressResult = mock(DistributionAddress.class);
		String getNonMatchedCountryNameResult = ""; // UTA: default value
		when(getParticipantAddressResult.getNonMatchedCountryName()).thenReturn(getNonMatchedCountryNameResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getParticipantAddressResult.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return getParticipantAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo4() throws Throwable {
		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		String getManulifeCompanyIdResult = ""; // UTA: default value
		when(getParticipantInfoResult.getManulifeCompanyId()).thenReturn(getManulifeCompanyIdResult);

		String getParticipantStatusCodeResult = ""; // UTA: default value
		when(getParticipantInfoResult.getParticipantStatusCode()).thenReturn(getParticipantStatusCodeResult);
		return getParticipantInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Address
	 */
	private static Address mockAddress3() throws Throwable {
		Address getTrusteeAddressResult = mock(Address.class);
		String getNonMatchedCountryNameResult2 = ""; // UTA: default value
		when(getTrusteeAddressResult.getNonMatchedCountryName()).thenReturn(getNonMatchedCountryNameResult2);
		return getTrusteeAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest7() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		String getCmaSiteCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mockContractInfo4();
		when(withdrawalRequest.getContractInfo()).thenReturn(getContractInfoResult);

		String getContractIssuedStateCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getContractIssuedStateCode()).thenReturn(getContractIssuedStateCodeResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(withdrawalRequest.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Date getExpirationDateResult = mockDate6();
		when(withdrawalRequest.getExpirationDate()).thenReturn(getExpirationDateResult);

		FederalTaxVO getFederalTaxVoResult = mockFederalTaxVO2();
		when(withdrawalRequest.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(withdrawalRequest).getFees();

		boolean getHaveInitiateStep1InitialMessagesLoadedResult = false; // UTA: default value
		when(withdrawalRequest.getHaveInitiateStep1InitialMessagesLoaded())
				.thenReturn(getHaveInitiateStep1InitialMessagesLoadedResult);

		boolean getHaveInitiateStep2InitialMessagesLoadedResult = false; // UTA: default value
		when(withdrawalRequest.getHaveInitiateStep2InitialMessagesLoaded())
				.thenReturn(getHaveInitiateStep2InitialMessagesLoadedResult);

		boolean getHaveReviewAndApproveInitialMessagesLoadedResult = false; // UTA: default value
		when(withdrawalRequest.getHaveReviewAndApproveInitialMessagesLoaded())
				.thenReturn(getHaveReviewAndApproveInitialMessagesLoadedResult);

		boolean getIsCensusInfoAvailableResult = false; // UTA: default value
		when(withdrawalRequest.getIsCensusInfoAvailable()).thenReturn(getIsCensusInfoAvailableResult);

		boolean getIsParticipantCreatedResult = false; // UTA: default value
		when(withdrawalRequest.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult);

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(withdrawalRequest).getMoneyTypes();

		DistributionAddress getParticipantAddressResult = mockDistributionAddress3();
		when(withdrawalRequest.getParticipantAddress()).thenReturn(getParticipantAddressResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(withdrawalRequest.getParticipantId()).thenReturn(getParticipantIdResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo4();
		when(withdrawalRequest.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(withdrawalRequest.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		String getParticipantStateOfResidenceResult = ""; // UTA: default value
		when(withdrawalRequest.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = ""; // UTA: default value
		when(withdrawalRequest.getPaymentTo()).thenReturn(getPaymentToResult);

		Principal getPrincipalResult = mock(Principal.class);
		when(withdrawalRequest.getPrincipal()).thenReturn(getPrincipalResult);

		String getReasonCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getReasonCode()).thenReturn(getReasonCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(withdrawalRequest).getRecipients();

		boolean getShowTaxWitholdingSectionResult = false; // UTA: default value
		when(withdrawalRequest.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);

		BigDecimal getTotalRequestedWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(withdrawalRequest.getTotalRequestedWithdrawalAmount()).thenReturn(getTotalRequestedWithdrawalAmountResult);

		Address getTrusteeAddressResult = mockAddress3();
		when(withdrawalRequest.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);

		Boolean getVestingCalledIndResult = false; // UTA: default value
		when(withdrawalRequest.getVestingCalledInd()).thenReturn(getVestingCalledIndResult);

		boolean getWithdrawalReasonSimpleResult = false; // UTA: default value
		when(withdrawalRequest.getWithdrawalReasonSimple()).thenReturn(getWithdrawalReasonSimpleResult);

		boolean isWmsiSelectedResult = false; // UTA: default value
		when(withdrawalRequest.isWmsiSelected()).thenReturn(isWmsiSelectedResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for proceedToStep2(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#proceedToStep2(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testProceedToStep2() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest8();
		
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);
		
		underTest.proceedToStep2(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest8() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for processApproved(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#processApproved(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testProcessApproved() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest9();
		
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);
		
		underTest.processApproved(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest9() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for readWithdrawalRequestForEdit(Integer, Principal)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#readWithdrawalRequestForEdit(Integer, Principal)
	 * @author patelpo
	 */
	@Test
	public void testReadWithdrawalRequestForEdit() throws Throwable {
		WithdrawalRequest newWithdrawalRequestResult = mock(WithdrawalRequest.class); // UTA: default value
		whenNew(WithdrawalRequest.class).withAnyArguments().thenReturn(newWithdrawalRequestResult);
		
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(newWithdrawalRequestResult);


		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer submissionId = 0; // UTA: default value
		Principal principal = mock(Principal.class);
		WithdrawalRequest result = underTest.readWithdrawalRequestForEdit(submissionId, principal);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for readWithdrawalRequestForView(Integer, Principal)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#readWithdrawalRequestForView(Integer, Principal)
	 * @author patelpo
	 */
	@Test
	public void testReadWithdrawalRequestForView() throws Throwable {
		WithdrawalRequest newWithdrawalRequestResult = mock(WithdrawalRequest.class); // UTA: default value
		whenNew(WithdrawalRequest.class).withAnyArguments().thenReturn(newWithdrawalRequestResult);
		
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(newWithdrawalRequestResult);


		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		Integer submissionId = 0; // UTA: default value
		Principal principal = mock(Principal.class);
		WithdrawalRequest result = underTest.readWithdrawalRequestForView(submissionId, principal);

		// Then
		// assertNotNull(result);
	}
	@Test(expected = EJBException.class)
	public void testReadWithdrawalRequestForView1() throws Throwable {
		
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Principal principal = mock(Principal.class);
		WithdrawalRequest result = underTest.readWithdrawalRequestForView(submissionId, principal);
		
		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for recalculate(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#recalculate(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testRecalculate() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest10();
		
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);
		
		underTest.recalculate(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of FederalTaxVO
	 */
	private static FederalTaxVO mockFederalTaxVO3() throws Throwable {
		FederalTaxVO getFederalTaxVoResult = mock(FederalTaxVO.class);
		Boolean getRolloverIndicatorResult = false; // UTA: default value
		when(getFederalTaxVoResult.getRolloverIndicator()).thenReturn(getRolloverIndicatorResult);

		BigDecimal getTaxPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(getFederalTaxVoResult.getTaxPercentage()).thenReturn(getTaxPercentageResult);
		return getFederalTaxVoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate7() throws Throwable {
		Date getRetirementDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getRetirementDateResult.after(any(Date.class))).thenReturn(afterResult);
		return getRetirementDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate8() throws Throwable {
		Date getTerminationDateResult = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getTerminationDateResult.after(any(Date.class))).thenReturn(afterResult2);
		return getTerminationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate9() throws Throwable {
		Date getVestingEventDateResult = mock(Date.class);
		boolean afterResult3 = false; // UTA: default value
		when(getVestingEventDateResult.after(any(Date.class))).thenReturn(afterResult3);
		return getVestingEventDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest10() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);

		String getContractIssuedStateCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getContractIssuedStateCode()).thenReturn(getContractIssuedStateCodeResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(withdrawalRequest.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		FederalTaxVO getFederalTaxVoResult = mockFederalTaxVO3();
		when(withdrawalRequest.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(withdrawalRequest).getFees();

		boolean getIsCensusInfoAvailableResult = false; // UTA: default value
		when(withdrawalRequest.getIsCensusInfoAvailable()).thenReturn(getIsCensusInfoAvailableResult);

		boolean getIsParticipantCreatedResult = false; // UTA: default value
		when(withdrawalRequest.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult);

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(withdrawalRequest).getMoneyTypes();

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(withdrawalRequest.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		String getParticipantStateOfResidenceResult = ""; // UTA: default value
		when(withdrawalRequest.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = ""; // UTA: default value
		when(withdrawalRequest.getPaymentTo()).thenReturn(getPaymentToResult);

		String getReasonCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getReasonCode()).thenReturn(getReasonCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(withdrawalRequest).getRecipients();

		Date getRetirementDateResult = mockDate7();
		when(withdrawalRequest.getRetirementDate()).thenReturn(getRetirementDateResult);

		boolean getShowTaxWitholdingSectionResult = false; // UTA: default value
		when(withdrawalRequest.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);

		Date getTerminationDateResult = mockDate8();
		when(withdrawalRequest.getTerminationDate()).thenReturn(getTerminationDateResult);

		BigDecimal getTotalAvailableWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(withdrawalRequest.getTotalAvailableWithdrawalAmount()).thenReturn(getTotalAvailableWithdrawalAmountResult);

		BigDecimal getTotalBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(withdrawalRequest.getTotalBalance()).thenReturn(getTotalBalanceResult);

		BigDecimal getTotalRequestedWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(withdrawalRequest.getTotalRequestedWithdrawalAmount()).thenReturn(getTotalRequestedWithdrawalAmountResult);

		Boolean getVestingCalledIndResult = false; // UTA: default value
		when(withdrawalRequest.getVestingCalledInd()).thenReturn(getVestingCalledIndResult);

		Date getVestingEventDateResult = mockDate9();
		when(withdrawalRequest.getVestingEventDate()).thenReturn(getVestingEventDateResult);

		BigDecimal getWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(withdrawalRequest.getWithdrawalAmount()).thenReturn(getWithdrawalAmountResult);

		boolean getWithdrawalReasonSimpleResult = false; // UTA: default value
		when(withdrawalRequest.getWithdrawalReasonSimple()).thenReturn(getWithdrawalReasonSimpleResult);

		boolean isWmsiSelectedResult = false; // UTA: default value
		when(withdrawalRequest.isWmsiSelected()).thenReturn(isWmsiSelectedResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for returnToStep1(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#returnToStep1(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testReturnToStep1() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest11();
		
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);
		
		underTest.returnToStep1(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest11() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for returnToStep1WithTerminationDate(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#returnToStep1WithTerminationDate(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testReturnToStep1WithTerminationDate() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest12();
		
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);
		
		underTest.returnToStep1WithTerminationDate(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate10() throws Throwable {
		Date getTerminationDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getTerminationDateResult.after(any(Date.class))).thenReturn(afterResult);
		return getTerminationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest12() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getReasonCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getReasonCode()).thenReturn(getReasonCodeResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);

		Date getTerminationDateResult = mockDate10();
		when(withdrawalRequest.getTerminationDate()).thenReturn(getTerminationDateResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for save(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#save(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testSave() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest13();
		
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);
		PowerMockito.doNothing().when(newWithdrawalResult).save();
		
		underTest.save(withdrawalRequest);

	}
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest13() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for sendToApprover(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#sendToApprover(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testSendToApprover() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest14();
		
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);
		
		WithdrawalRequest result = underTest.sendToApprover(withdrawalRequest);

		// Then
		// assertNotNull(result);
	}
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest14() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for sendToReviewer(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#sendToReviewer(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testSendToReviewer() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest15();
		
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);
		
		WithdrawalRequest result = underTest.sendToReviewer(withdrawalRequest);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest15() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for setSessionContext(javax.ejb.SessionContext)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#setSessionContext(javax.ejb.SessionContext)
	 * @author patelpo
	 */
	@Test
	public void testSetSessionContext() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		SessionContext ctx = mock(SessionContext.class);
		underTest.setSessionContext(ctx);

	}

	/**
	 * Parasoft Jtest UTA: Test for submitParticipantInitiatedWithdrawal(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#submitParticipantInitiatedWithdrawal(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testSubmitParticipantInitiatedWithdrawal() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();
		SessionContext ctx = mockSessionContext5();
		underTest.setSessionContext(ctx);

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest16();
		
		Withdrawal newWithdrawalResult = mock(Withdrawal.class); // UTA: default value
		whenNew(Withdrawal.class).withAnyArguments().thenReturn(newWithdrawalResult);
		when(newWithdrawalResult.getWithdrawalRequest()).thenReturn(withdrawalRequest);
		
		WithdrawalRequest result = underTest.submitParticipantInitiatedWithdrawal(withdrawalRequest);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of SessionContext
	 */
	private static SessionContext mockSessionContext5() throws Throwable {
		SessionContext ctx = mock(SessionContext.class);
		Object lookupResult = new Object(); // UTA: default value
		when(ctx.lookup(anyString())).thenReturn(lookupResult);
		return ctx;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Address
	 */
	private static Address mockAddress4() throws Throwable {
		Address getApprovalAddressResult = mock(Address.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(getApprovalAddressResult.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(getApprovalAddressResult.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(getApprovalAddressResult.getCity()).thenReturn(getCityResult);

		String getCountryCodeResult = ""; // UTA: default value
		when(getApprovalAddressResult.getCountryCode()).thenReturn(getCountryCodeResult);

		String getStateCodeResult = ""; // UTA: default value
		when(getApprovalAddressResult.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(getApprovalAddressResult.getZipCode()).thenReturn(getZipCodeResult);
		return getApprovalAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskAddressChangeVO
	 */
	private static AtRiskAddressChangeVO mockAtRiskAddressChangeVO() throws Throwable {
		AtRiskAddressChangeVO getAddresschangeResult = mock(AtRiskAddressChangeVO.class);
		Address getApprovalAddressResult = mockAddress4();
		when(getAddresschangeResult.getApprovalAddress()).thenReturn(getApprovalAddressResult);

		Integer getApprovalUpdatedProfileIdResult = 0; // UTA: default value
		when(getAddresschangeResult.getApprovalUpdatedProfileId()).thenReturn(getApprovalUpdatedProfileIdResult);

		String getApprovalUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getAddresschangeResult.getApprovalUpdatedUserIdType()).thenReturn(getApprovalUpdatedUserIdTypeResult);

		String getCreatedUserFistNameResult = ""; // UTA: default value
		when(getAddresschangeResult.getCreatedUserFistName()).thenReturn(getCreatedUserFistNameResult);

		String getCreatedUserLastNameResult = ""; // UTA: default value
		when(getAddresschangeResult.getCreatedUserLastName()).thenReturn(getCreatedUserLastNameResult);
		return getAddresschangeResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate11() throws Throwable {
		Date getForgotPasswordRequestedDateResult = mock(Date.class);
		long getTimeResult = 0L; // UTA: default value
		when(getForgotPasswordRequestedDateResult.getTime()).thenReturn(getTimeResult);
		return getForgotPasswordRequestedDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskForgetUserName
	 */
	private static AtRiskForgetUserName mockAtRiskForgetUserName() throws Throwable {
		AtRiskForgetUserName getForgetUserNameResult = mock(AtRiskForgetUserName.class);
		String getForgotPasswordEmailAddressResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordEmailAddress()).thenReturn(getForgotPasswordEmailAddressResult);

		Date getForgotPasswordRequestedDateResult = mockDate11();
		when(getForgetUserNameResult.getForgotPasswordRequestedDate()).thenReturn(getForgotPasswordRequestedDateResult);

		Integer getForgotPasswordUpdatedProfileIdResult = 0; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedProfileId())
				.thenReturn(getForgotPasswordUpdatedProfileIdResult);

		String getForgotPasswordUpdatedUserFirstNameResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserFirstName())
				.thenReturn(getForgotPasswordUpdatedUserFirstNameResult);

		String getForgotPasswordUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserIdType())
				.thenReturn(getForgotPasswordUpdatedUserIdTypeResult);

		String getForgotPasswordUpdatedUserLastNameResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserLastName())
				.thenReturn(getForgotPasswordUpdatedUserLastNameResult);
		return getForgetUserNameResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate12() throws Throwable {
		Date getEmailPasswordResetDateResult = mock(Date.class);
		long getTimeResult2 = 0L; // UTA: default value
		when(getEmailPasswordResetDateResult.getTime()).thenReturn(getTimeResult2);
		return getEmailPasswordResetDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskPasswordResetVO
	 */
	private static AtRiskPasswordResetVO mockAtRiskPasswordResetVO() throws Throwable {
		AtRiskPasswordResetVO getPasswordResetResult = mock(AtRiskPasswordResetVO.class);
		Integer getEmailAddressLastUpdatedProfileIdResult = 0; // UTA: default value
		when(getPasswordResetResult.getEmailAddressLastUpdatedProfileId())
				.thenReturn(getEmailAddressLastUpdatedProfileIdResult);

		String getEmailAddressLastUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getPasswordResetResult.getEmailAddressLastUpdatedUserIdType())
				.thenReturn(getEmailAddressLastUpdatedUserIdTypeResult);

		Date getEmailPasswordResetDateResult = mockDate12();
		when(getPasswordResetResult.getEmailPasswordResetDate()).thenReturn(getEmailPasswordResetDateResult);

		String getEmailPasswordResetEmailAddressResult = ""; // UTA: default value
		when(getPasswordResetResult.getEmailPasswordResetEmailAddress())
				.thenReturn(getEmailPasswordResetEmailAddressResult);

		Integer getEmailPasswordResetInitiatedProfileIdResult = 0; // UTA: default value
		when(getPasswordResetResult.getEmailPasswordResetInitiatedProfileId())
				.thenReturn(getEmailPasswordResetInitiatedProfileIdResult);
		return getPasswordResetResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Address
	 */
	private static Address mockAddress5() throws Throwable {
		Address getAddressResult = mock(Address.class);
		String getAddressLine1Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine1()).thenReturn(getAddressLine1Result2);

		String getAddressLine2Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine2()).thenReturn(getAddressLine2Result2);

		String getCityResult2 = ""; // UTA: default value
		when(getAddressResult.getCity()).thenReturn(getCityResult2);

		String getCountryCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getCountryCode()).thenReturn(getCountryCodeResult2);

		String getStateCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getStateCode()).thenReturn(getStateCodeResult2);

		String getZipCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getZipCode()).thenReturn(getZipCodeResult2);
		return getAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate13() throws Throwable {
		Date getWebRegConfirmationMailedDateResult = mock(Date.class);
		long getTimeResult3 = 0L; // UTA: default value
		when(getWebRegConfirmationMailedDateResult.getTime()).thenReturn(getTimeResult3);
		return getWebRegConfirmationMailedDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate14() throws Throwable {
		Date getWebRegistrationDateResult = mock(Date.class);
		long getTimeResult4 = 0L; // UTA: default value
		when(getWebRegistrationDateResult.getTime()).thenReturn(getTimeResult4);
		return getWebRegistrationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskWebRegistrationVO
	 */
	private static AtRiskWebRegistrationVO mockAtRiskWebRegistrationVO() throws Throwable {
		AtRiskWebRegistrationVO getWebRegistrationResult = mock(AtRiskWebRegistrationVO.class);
		Address getAddressResult = mockAddress5();
		when(getWebRegistrationResult.getAddress()).thenReturn(getAddressResult);

		Integer getConfirmUpdatedProfileIdResult = 0; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedProfileId()).thenReturn(getConfirmUpdatedProfileIdResult);

		String getConfirmUpdatedUserFirstNameResult = ""; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedUserFirstName())
				.thenReturn(getConfirmUpdatedUserFirstNameResult);

		String getConfirmUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedUserIdType()).thenReturn(getConfirmUpdatedUserIdTypeResult);

		String getConfirmUpdatedUserLastNameResult = ""; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedUserLastName()).thenReturn(getConfirmUpdatedUserLastNameResult);

		Date getWebRegConfirmationMailedDateResult = mockDate13();
		when(getWebRegistrationResult.getWebRegConfirmationMailedDate())
				.thenReturn(getWebRegConfirmationMailedDateResult);

		Date getWebRegistrationDateResult = mockDate14();
		when(getWebRegistrationResult.getWebRegistrationDate()).thenReturn(getWebRegistrationDateResult);
		return getWebRegistrationResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskDetailsVO
	 */
	private static AtRiskDetailsVO mockAtRiskDetailsVO() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		AtRiskAddressChangeVO getAddresschangeResult = mockAtRiskAddressChangeVO();
		when(getAtRiskDetailsVOResult.getAddresschange()).thenReturn(getAddresschangeResult);

		AtRiskForgetUserName getForgetUserNameResult = mockAtRiskForgetUserName();
		when(getAtRiskDetailsVOResult.getForgetUserName()).thenReturn(getForgetUserNameResult);

		AtRiskPasswordResetVO getPasswordResetResult = mockAtRiskPasswordResetVO();
		when(getAtRiskDetailsVOResult.getPasswordReset()).thenReturn(getPasswordResetResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);

		AtRiskWebRegistrationVO getWebRegistrationResult = mockAtRiskWebRegistrationVO();
		when(getAtRiskDetailsVOResult.getWebRegistration()).thenReturn(getWebRegistrationResult);
		return getAtRiskDetailsVOResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest16() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		AtRiskDetailsVO getAtRiskDetailsVOResult = mockAtRiskDetailsVO();
		when(withdrawalRequest.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(withdrawalRequest.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		String getPaymentToResult = ""; // UTA: default value
		when(withdrawalRequest.getPaymentTo()).thenReturn(getPaymentToResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(withdrawalRequest).getRecipients();

		String getStatusCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getStatusCode()).thenReturn(getStatusCodeResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for getSessionContext()
	 *
	 * @see com.manulife.pension.service.withdrawal.WithdrawalServiceBean#getSessionContext()
	 * @author patelpo
	 */
/*	@Test
	public void testGetSessionContext() throws Throwable {
		// Given
		WithdrawalServiceBean underTest = new WithdrawalServiceBean();

		// When
		SessionContext result = underTest.getSessionContext();

		// Then
		// assertNotNull(result);
	}
*/
}