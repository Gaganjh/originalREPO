/**
 * 
 */
package com.manulife.pension.service.withdrawal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyLong;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.anyInt;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.BasicInternalUser;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.JdbcHelper;

import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.SystemException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalHelper
 *
 * @see com.manulife.pension.service.withdrawal.util.WithdrawalHelper
 * @author patelpo
 */
@PrepareForTest({ Principal.class, WithdrawalHelper.class, BaseEnvironment.class, EmployeeServiceDelegate.class,
		EventClientUtility.class, ContractServiceDelegate.class, SecurityServiceDelegate.class })
@RunWith(PowerMockRunner.class)
public class WithdrawalHelperTest {
	@Before
	public void setUp() throws Exception {
		spy(EmployeeServiceDelegate.class);
		EmployeeServiceDelegate getInstanceResult2 = mock(EmployeeServiceDelegate.class);
		doReturn(getInstanceResult2).when(EmployeeServiceDelegate.class);
		EmployeeServiceDelegate.getInstance(nullable(String.class));
		when(getInstanceResult2.getProfileIdByParticipantId(anyLong(), anyInt())).thenReturn("10");

		BaseEnvironment newBaseEnvironmentResult = mock(BaseEnvironment.class); // UTA: default value
		whenNew(BaseEnvironment.class).withAnyArguments().thenReturn(newBaseEnvironmentResult);

		spy(EventClientUtility.class);
		EventClientUtility getInstanceResult = mock(EventClientUtility.class);
		doReturn(getInstanceResult).when(EventClientUtility.class);
		EventClientUtility.getInstance(nullable(String.class));

		
		InitialContext newInitialContextResult = mock(InitialContext.class); // UTA: default value
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
	}

	/**
	 * Parasoft Jtest UTA: Test for fireWithdrawalAboutToExpireEvent(WithdrawalRequestMetaData, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.util.WithdrawalHelper#fireWithdrawalAboutToExpireEvent(WithdrawalRequestMetaData, Integer)
	 * @author patelpo
	 */
	@Test
	public void testFireWithdrawalAboutToExpireEvent() throws Throwable {
		// When
		WithdrawalRequestMetaData withdrawalRequestMetaData = mockWithdrawalRequestMetaData();
		Integer initiator = 0; // UTA: default value
		WithdrawalHelper.fireWithdrawalAboutToExpireEvent(withdrawalRequestMetaData, initiator);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestMetaData
	 */
	private static WithdrawalRequestMetaData mockWithdrawalRequestMetaData() throws Throwable {
		WithdrawalRequestMetaData withdrawalRequestMetaData = mock(WithdrawalRequestMetaData.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequestMetaData.getContractId()).thenReturn(getContractIdResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(withdrawalRequestMetaData.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(withdrawalRequestMetaData.getSubmissionId()).thenReturn(getSubmissionIdResult);

		String toStringResult = ""; // UTA: default value
		when(withdrawalRequestMetaData.toString()).thenReturn(toStringResult);
		return withdrawalRequestMetaData;
	}

	/**
	 * Parasoft Jtest UTA: Test for fireWithdrawalApprovedEvent(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.util.WithdrawalHelper#fireWithdrawalApprovedEvent(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testFireWithdrawalApprovedEvent() throws Throwable {
		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest();
		WithdrawalHelper.fireWithdrawalApprovedEvent(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);

		Integer getLastUpdatedByIdResult = 0; // UTA: default value
		when(withdrawalRequest.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(withdrawalRequest.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(withdrawalRequest.getSubmissionId()).thenReturn(getSubmissionIdResult);

		String toStringResult = ""; // UTA: default value
		when(withdrawalRequest.toString()).thenReturn(toStringResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for fireWithdrawalDeletedEvent(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.util.WithdrawalHelper#fireWithdrawalDeletedEvent(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testFireWithdrawalDeletedEvent() throws Throwable {
		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest2();
		WithdrawalHelper.fireWithdrawalDeletedEvent(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest2() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);

		Integer getLastUpdatedByIdResult = 0; // UTA: default value
		when(withdrawalRequest.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(withdrawalRequest.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(withdrawalRequest.getSubmissionId()).thenReturn(getSubmissionIdResult);

		String toStringResult = ""; // UTA: default value
		when(withdrawalRequest.toString()).thenReturn(toStringResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for fireWithdrawalDeniedEvent(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.util.WithdrawalHelper#fireWithdrawalDeniedEvent(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testFireWithdrawalDeniedEvent() throws Throwable {
		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest3();
		WithdrawalHelper.fireWithdrawalDeniedEvent(withdrawalRequest);

	}
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest3() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);

		Integer getLastUpdatedByIdResult = 0; // UTA: default value
		when(withdrawalRequest.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(withdrawalRequest.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(withdrawalRequest.getSubmissionId()).thenReturn(getSubmissionIdResult);

		String toStringResult = ""; // UTA: default value
		when(withdrawalRequest.toString()).thenReturn(toStringResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for fireWithdrawalExpiredEvent(WithdrawalRequestMetaData, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.util.WithdrawalHelper#fireWithdrawalExpiredEvent(WithdrawalRequestMetaData, Integer)
	 * @author patelpo
	 */
	@Test
	public void testFireWithdrawalExpiredEvent() throws Throwable {
		// When
		WithdrawalRequestMetaData withdrawalRequestMetaData = mockWithdrawalRequestMetaData2();
		Integer initiator = 0; // UTA: default value
		WithdrawalHelper.fireWithdrawalExpiredEvent(withdrawalRequestMetaData, initiator);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestMetaData
	 */
	private static WithdrawalRequestMetaData mockWithdrawalRequestMetaData2() throws Throwable {
		WithdrawalRequestMetaData withdrawalRequestMetaData = mock(WithdrawalRequestMetaData.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequestMetaData.getContractId()).thenReturn(getContractIdResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(withdrawalRequestMetaData.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(withdrawalRequestMetaData.getSubmissionId()).thenReturn(getSubmissionIdResult);

		String toStringResult = ""; // UTA: default value
		when(withdrawalRequestMetaData.toString()).thenReturn(toStringResult);
		return withdrawalRequestMetaData;
	}

	/**
	 * Parasoft Jtest UTA: Test for fireWithdrawalPendingApprovalEvent(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.util.WithdrawalHelper#fireWithdrawalPendingApprovalEvent(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testFireWithdrawalPendingApprovalEvent() throws Throwable {
		spy(SecurityServiceDelegate.class);
		UserRole role= new BasicInternalUser();
		ContractPermission contractPermission = new ContractPermission(role);

		SecurityServiceDelegate getInstanceResult2 = mock(SecurityServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult2).when(SecurityServiceDelegate.class, "getInstance");
		when(getInstanceResult2.getTpaFirmContractPermission(anyInt())).thenReturn(contractPermission);

		spy(ContractServiceDelegate.class);

		ContractServiceDelegate getInstanceResult = mock(ContractServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(ContractServiceDelegate.class, "getInstance");
		when(getInstanceResult.isBundledGaContract(anyInt())).thenReturn(true);

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest4();
		WithdrawalHelper.fireWithdrawalPendingApprovalEvent(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest4() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);

		Integer getLastUpdatedByIdResult = 0; // UTA: default value
		when(withdrawalRequest.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(withdrawalRequest.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(withdrawalRequest.getSubmissionId()).thenReturn(getSubmissionIdResult);

		String toStringResult = ""; // UTA: default value
		when(withdrawalRequest.toString()).thenReturn(toStringResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for fireWithdrawalPendingReviewEvent(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.util.WithdrawalHelper#fireWithdrawalPendingReviewEvent(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testFireWithdrawalPendingReviewEvent() throws Throwable {
		spy(ContractServiceDelegate.class);

		ContractServiceDelegate getInstanceResult = mock(ContractServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(ContractServiceDelegate.class, "getInstance");
		when(getInstanceResult.isBundledGaContract(anyInt())).thenReturn(true);

		
		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest5();
		WithdrawalHelper.fireWithdrawalPendingReviewEvent(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest5() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);

		Integer getLastUpdatedByIdResult = 0; // UTA: default value
		when(withdrawalRequest.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(withdrawalRequest.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(withdrawalRequest.getSubmissionId()).thenReturn(getSubmissionIdResult);

		String toStringResult = ""; // UTA: default value
		when(withdrawalRequest.toString()).thenReturn(toStringResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for getSearchableContracts(Principal, Contract)
	 *
	 * @see com.manulife.pension.service.withdrawal.util.WithdrawalHelper#getSearchableContracts(Principal, Contract)
	 * @author patelpo
	 */
	@Test
	public void testGetSearchableContracts() throws Throwable {
		spy(ContractServiceDelegate.class);
		
		ContractServiceFeature contractServiceFeature= new ContractServiceFeature();

		ContractServiceDelegate getInstanceResult = mock(ContractServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(ContractServiceDelegate.class, "getInstance");
		when(getInstanceResult.getContractServiceFeature(anyInt(), anyString())).thenReturn(contractServiceFeature);
		// When
		Principal principal = mockPrincipal();
		Contract contract = mockContract();
		List<Integer> result = WithdrawalHelper.getSearchableContracts(principal, contract);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(0));
	}
	@Test
	public void testGetSearchableContracts_1() throws Throwable {
		spy(ContractServiceDelegate.class);
		
		ContractServiceFeature contractServiceFeature= new ContractServiceFeature();

		ContractServiceDelegate getInstanceResult = mock(ContractServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(ContractServiceDelegate.class, "getInstance");
		when(getInstanceResult.getContractServiceFeature(anyInt(), anyString())).thenReturn(contractServiceFeature);
		// When
		Principal principal = mockPrincipal();
		Contract contract = mockContract_1();
		List<Integer> result = WithdrawalHelper.getSearchableContracts(principal, contract);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(0));
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of UserRole
	 */
	private static UserRole mockUserRole() throws Throwable {
		UserRole getRoleResult = mock(UserRole.class);
		boolean isTPAResult = false; // UTA: default value
		when(getRoleResult.isTPA()).thenReturn(isTPAResult);
		return getRoleResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Principal
	 */
	private static Principal mockPrincipal() throws Throwable {
		Principal principal = mock(Principal.class);
		UserRole getRoleResult = mockUserRole();
		when(principal.getRole()).thenReturn(getRoleResult);
		return principal;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Contract
	 */
	private static Contract mockContract() throws Throwable {
		Contract contract = mock(Contract.class);
		int getContractNumberResult = 10; // UTA: default value
		when(contract.getContractNumber()).thenReturn(getContractNumberResult);
		return contract;
	}
	private static Contract mockContract_1() throws Throwable {
		Contract contract = mock(Contract.class);
		int getContractNumberResult = 0; // UTA: default value
		when(contract.getContractNumber()).thenReturn(getContractNumberResult);
		return contract;
	}
}