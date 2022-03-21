/**
 * 
 */
package com.manulife.pension.service.withdrawal.delegate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import javax.rmi.PortableRemoteObject;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.withdrawal.WithdrawalService;
import com.manulife.pension.service.withdrawal.WithdrawalServiceHome;
import com.manulife.pension.service.withdrawal.WithdrawalServiceLocalHome;
import com.manulife.pension.service.withdrawal.WithdrawalServiceUtil;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.PendingReviewApproveWithdrawalCount;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalServiceDelegate
 *
 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate
 * @author patelpo
 */
@PrepareForTest({ Principal.class, WithdrawalServiceUtil.class, PortableRemoteObject.class,NamingManager.class})
@RunWith(PowerMockRunner.class)
public class WithdrawalServiceDelegateTest {
	WithdrawalService withdrawalService = mock(WithdrawalService.class);
	WithdrawalServiceLocalHome withdrawalServiceLocalHome = mock(WithdrawalServiceLocalHome.class);
	WithdrawalServiceHome withdrawalServiceHome=mock(WithdrawalServiceHome.class);
	Context context=mock(Context.class);
	@Before
	public void setUp() throws Exception {
		spy(WithdrawalServiceUtil.class);
		
		doReturn(withdrawalServiceLocalHome).when(WithdrawalServiceUtil.class, "getLocalHome");
		
		spy(NamingManager.class);
		doReturn(true).when(NamingManager.class);
		NamingManager.hasInitialContextFactoryBuilder();
			
		doReturn(context).when(NamingManager.class);
		NamingManager.getInitialContext(nullable(Hashtable.class));
	}

	/**
	 * Parasoft Jtest UTA: Test for approve(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#approve(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testApprove() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		when(withdrawalService.approve(nullable(WithdrawalRequest.class))).thenReturn(withdrawalRequest);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest result = underTest.approve(withdrawalRequest);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for delete(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#delete(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testDelete() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		PowerMockito.doNothing().when(withdrawalService).delete(nullable(WithdrawalRequest.class));
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		underTest.delete(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Test for deny(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#deny(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testDeny() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		PowerMockito.doNothing().when(withdrawalService).deny(nullable(WithdrawalRequest.class));
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		underTest.deny(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Test for getAgreedLegaleseContent(Integer, String)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getAgreedLegaleseContent(Integer, String)
	 * @author patelpo
	 */
	@Test
	public void testGetAgreedLegaleseContent() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		when(withdrawalService.getAgreedLegaleseText(nullable(Integer.class),nullable(String.class))).thenReturn("TEST");
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer submissionId = 0;
		String cmaSiteCode = "";
		String result = underTest.getAgreedLegaleseContent(submissionId, cmaSiteCode);

		// Then
		assertNotNull(result);
		assertEquals("TEST", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getAllStateTaxOptions(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getAllStateTaxOptions(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testGetAllStateTaxOptions() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		Collection<StateTaxVO> colObj=new ArrayList<StateTaxVO>();
		StateTaxVO stateTaxVO=new StateTaxVO();
		colObj.add(stateTaxVO);
		when(withdrawalService.getAllStateTaxOptions(nullable(WithdrawalRequest.class))).thenReturn(colObj);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Collection<StateTaxVO> result = underTest.getAllStateTaxOptions(withdrawalRequest);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Test for getContractInfo(Integer, Principal)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getContractInfo(Integer, Principal)
	 * @author patelpo
	 */
	@Test
	public void testGetContractInfo() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		ContractInfo contractInfo = mock(ContractInfo.class);
		when(withdrawalService.getContractInfo(nullable(Integer.class),nullable(Principal.class))).thenReturn(contractInfo);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer contractId = 0;
		Principal principal = mock(Principal.class);
		ContractInfo result = underTest.getContractInfo(contractId, principal);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getInstance()
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getInstance()
	 * @author patelpo
	 */
	@Test
	public void testGetInstance() throws Throwable {
		// When
		WithdrawalServiceDelegate result = WithdrawalServiceDelegate.getInstance();

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getLookupData(ContractInfo, String, Collection)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getLookupData(ContractInfo, String, Collection)
	 * @author patelpo
	 */
	@Test
	public void testGetLookupData() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest=mock(WithdrawalRequest.class);
		Map mapObj=new HashMap();
		when(withdrawalService.getLookupData(nullable(ContractInfo.class), nullable(String.class), nullable(Collection.class))).thenReturn(mapObj);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		ContractInfo contractInfo = mock(ContractInfo.class);
		String participantStatusCode = "";
		Collection<String> keys = new ArrayList<String>();
		Map result = underTest.getLookupData(contractInfo, participantStatusCode, keys);

		// Then
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Test for getMostRecentWithdrawalRequest(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getMostRecentWithdrawalRequest(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetMostRecentWithdrawalRequest() throws Throwable {
		
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		when(withdrawalService.getMostRecentWithdrawalRequest(nullable(Integer.class), nullable(Integer.class))).thenReturn(withdrawalRequest);
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer profileId = 0;
		Integer contractId = 0;
		WithdrawalRequest result = underTest.getMostRecentWithdrawalRequest(profileId, contractId);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getNumberOfCompletedWithdrawalTransaction(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getNumberOfCompletedWithdrawalTransaction(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetNumberOfCompletedWithdrawalTransaction() throws Throwable {
		spy(WithdrawalServiceUtil.class);

		WithdrawalServiceLocalHome getLocalHomeResult = mock(WithdrawalServiceLocalHome.class);
		doReturn(getLocalHomeResult).when(WithdrawalServiceUtil.class, "getLocalHome");
		WithdrawalService withdrawalService = mock(WithdrawalService.class);
		when(getLocalHomeResult.create()).thenReturn(withdrawalService);

		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer contractId = 0;
		Integer participantId = 0;
		int result = underTest.getNumberOfCompletedWithdrawalTransaction(contractId, participantId);

		// Then
		assertEquals(0, result);
	}

	@Test(expected = SystemException.class)
	public void testGetNumberOfCompletedWithdrawalTransaction_Exception() throws Throwable {
		spy(WithdrawalServiceUtil.class);

		WithdrawalServiceLocalHome getLocalHomeResult = mock(WithdrawalServiceLocalHome.class);
		doReturn(getLocalHomeResult).when(WithdrawalServiceUtil.class, "getLocalHome");
		WithdrawalService withdrawalService = mock(WithdrawalService.class);
		when(getLocalHomeResult.create()).thenThrow(new CreateException(""));

		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer contractId = 0;
		Integer participantId = 0;
		int result = underTest.getNumberOfCompletedWithdrawalTransaction(contractId, participantId);

	}

	/**
	 * Parasoft Jtest UTA: Test for getNumberOfPendingWithdrawalTransaction(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getNumberOfPendingWithdrawalTransaction(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetNumberOfPendingWithdrawalTransaction() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest=mock(WithdrawalRequest.class);
		when(withdrawalService.getNumberOfPendingWithdrawalTransaction(nullable(Integer.class), nullable(Integer.class))).thenReturn(411);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer contractId = 0;
		Integer participantId = 0;
		int result = underTest.getNumberOfPendingWithdrawalTransaction(contractId, participantId);

		// Then
		assertEquals(411, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getParticipantInfo(int, int, int)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getParticipantInfo(int, int, int)
	 * @author patelpo
	 */
	@Test
	public void testGetParticipantInfo() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		ParticipantInfo participantInfo = mock(ParticipantInfo.class);
		when(withdrawalService.getParticipantInfo(anyInt(),anyInt(),anyInt())).thenReturn(participantInfo);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		int profileId = 0;
		int participantId = 0;
		int contractId = 0;
		ParticipantInfo result = underTest.getParticipantInfo(profileId, participantId, contractId);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getParticipantPaymentToOptions(ParticipantInfo)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getParticipantPaymentToOptions(ParticipantInfo)
	 * @author patelpo
	 */
	@Test
	public void testGetParticipantPaymentToOptions() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		Collection collObj=new ArrayList();
		when(withdrawalService.getParticipantPaymentToOptions(nullable(ParticipantInfo.class))).thenReturn(collObj);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		ParticipantInfo participantInfo = mock(ParticipantInfo.class);
		Collection result = underTest.getParticipantPaymentToOptions(participantInfo);

		// Then
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Test for getParticipantWithdrawalReasons(String, String)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getParticipantWithdrawalReasons(String, String)
	 * @author patelpo
	 */
	@Test
	public void testGetParticipantWithdrawalReasons() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		Collection collObj=new ArrayList();
		when(withdrawalService.getParticipantWithdrawalReasons(nullable(String.class), nullable(String.class))).thenReturn(collObj);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		String contractStatus = "";
		String participantStatus = "";
		Collection result = underTest.getParticipantWithdrawalReasons(contractStatus, participantStatus);

		// Then
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Test for getPendingReviewApproveWithdrawalRequests(Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getPendingReviewApproveWithdrawalRequests(Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetPendingReviewApproveWithdrawalRequests() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		PendingReviewApproveWithdrawalCount pendingReviewApproveWithdrawalCount = mock(PendingReviewApproveWithdrawalCount.class);
		when(withdrawalService.getPendingReviewApproveWdRequestCounts(nullable(Integer.class))).thenReturn(pendingReviewApproveWithdrawalCount);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer contractId = 0;
		PendingReviewApproveWithdrawalCount result = underTest.getPendingReviewApproveWithdrawalRequests(contractId);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getPhysicalHome()
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getPhysicalHome()
	 * @author patelpo
	 */
	@Test
	public void testGetPhysicalHome() throws Throwable {
		
		when(context.lookup(anyString())).thenReturn(withdrawalServiceHome);
		spy(PortableRemoteObject.class);

		WithdrawalServiceHome narrowResult = mock(WithdrawalServiceHome.class);
		doReturn(narrowResult).when(PortableRemoteObject.class);
		PortableRemoteObject.narrow(nullable(Object.class), nullable(Class.class));

		// When
		WithdrawalServiceHome result = WithdrawalServiceDelegate.getPhysicalHome();

		// Then
		 assertNotNull(result);
	}
	
	/**
	 * Parasoft Jtest UTA: Test for getRequiresSpousalConsentForDistributions(Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getRequiresSpousalConsentForDistributions(Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetRequiresSpousalConsentForDistributions() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		when(withdrawalService.getRequiresSpousalConsentForDistributions(nullable(Integer.class))).thenReturn("DISTr");
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer contractNumber = 70300;
		String result = underTest.getRequiresSpousalConsentForDistributions(contractNumber);

		// Then
		assertNotNull(result);
		assertEquals("DISTr", result);
	}
	@Test(expected = NestableRuntimeException.class)
	public void testGetRequiresSpousalConsentForDistributions_Exception() throws Throwable {
		spy(WithdrawalServiceUtil.class);
		WithdrawalServiceLocalHome getLocalHomeResult2 = mock(WithdrawalServiceLocalHome.class);
		PowerMockito.doThrow(new NamingException()).when(WithdrawalServiceUtil.class);
		WithdrawalServiceUtil.getLocalHome();
		
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();
		
		// When
		Integer contractNumber = 0;
		String result = underTest.getRequiresSpousalConsentForDistributions(contractNumber);
		
		// Then
		assertNull(result);
		assertEquals("null", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getUserNamesForIds(Collection)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getUserNamesForIds(Collection)
	 * @author patelpo
	 */
	@Test
	public void testGetUserNamesForIds() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		Map<Integer, UserName> mapObj=new HashMap<Integer, UserName>();
		when(withdrawalService.getUserNames(nullable(Collection.class))).thenReturn(mapObj);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Collection<Integer> userIds = new ArrayList<Integer>();
		Map<Integer, UserName> result = underTest.getUserNamesForIds(userIds);

		// Then
		 assertNotNull(result);
		 assertEquals(0, result.size());
		 assertFalse(result.containsKey(0));
		 assertFalse(result.containsValue(null));
	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalInfo(int, int)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getWithdrawalInfo(int, int)
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalInfo() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalInfo withdrawalInfo=new WithdrawalInfo();
		when(withdrawalService.getWithdrawalInfo(anyInt(), anyInt())).thenReturn(withdrawalInfo);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		int participantId = 45;
		int contractId = 70300;
		WithdrawalInfo result = underTest.getWithdrawalInfo(participantId, contractId);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalRequestMetaData(Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getWithdrawalRequestMetaData(Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalRequestMetaData() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequestMetaData withdrawalRequestMetaData = mock(WithdrawalRequestMetaData.class);
		when(withdrawalService.getWithdrawalRequestMetaData(nullable(Integer.class))).thenReturn(withdrawalRequestMetaData);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer submissionId = 0;
		WithdrawalRequestMetaData result = underTest.getWithdrawalRequestMetaData(submissionId);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalRequests(int, int)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#getWithdrawalRequests(int, int)
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalRequests() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		Collection withdrawalRequest=new ArrayList();
		when(withdrawalService.getWithdrawalRequests(anyInt(),anyInt())).thenReturn(withdrawalRequest);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		int profileId = 0;
		int contractId = 0;
		Collection result = underTest.getWithdrawalRequests(profileId, contractId);

		// Then
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Test for handleDisableOnlineWithdrawals(Integer, Integer, String)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#handleDisableOnlineWithdrawals(Integer, Integer, String)
	 * @author patelpo
	 */
	@Test
	public void testHandleDisableOnlineWithdrawals() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		PowerMockito.doNothing().when(withdrawalService).handleDisableOnlineWithdrawals(nullable(Integer.class),nullable(Integer.class),nullable(String.class));
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer contractId = 0;
		Integer profileId = 0;
		String siteCode = "";
		underTest.handleDisableOnlineWithdrawals(contractId, profileId, siteCode);

	}

	/**
	 * Parasoft Jtest UTA: Test for handleEnableOneStepApprovals(Integer, Principal)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#handleEnableOneStepApprovals(Integer, Principal)
	 * @author patelpo
	 */
	@Test
	public void testHandleEnableOneStepApprovals() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest=mock(WithdrawalRequest.class);
		PowerMockito.doNothing().when(withdrawalService).handleEnableOneStepApprovals(nullable(Integer.class),nullable(Principal.class),nullable(String.class));
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer contractId = 0;
		Principal principal = mock(Principal.class);
		underTest.handleEnableOneStepApprovals(contractId, principal);

	}

	/**
	 * Parasoft Jtest UTA: Test for handleEnableOnlineWithdrawals(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#handleEnableOnlineWithdrawals(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testHandleEnableOnlineWithdrawals() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest=mock(WithdrawalRequest.class);
		PowerMockito.doNothing().when(withdrawalService).handleEnableOnlineWithdrawals(nullable(Integer.class), nullable(Integer.class));
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer contractId = 0;
		Integer profileId = 0;
		underTest.handleEnableOnlineWithdrawals(contractId, profileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for handleEnableTwoStepApprovals(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#handleEnableTwoStepApprovals(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testHandleEnableTwoStepApprovals() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest=mock(WithdrawalRequest.class);
		PowerMockito.doNothing().when(withdrawalService).handleEnableTwoStepApprovals(nullable(Integer.class), nullable(Integer.class));
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer contractId = 0;
		Integer profileId = 0;
		underTest.handleEnableTwoStepApprovals(contractId, profileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for initiateNewParticipantWithdrawalRequest(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#initiateNewParticipantWithdrawalRequest(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testInitiateNewParticipantWithdrawalRequest() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		when(withdrawalService.initiateNewParticipantWithdrawalRequest(nullable(Integer.class),nullable(Integer.class))).thenReturn(withdrawalRequest);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer profileId = 0;
		Integer contractId = 0;
		WithdrawalRequest result = underTest.initiateNewParticipantWithdrawalRequest(profileId, contractId);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for initiateNewWithdrawalRequest(Integer, Integer, Principal)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#initiateNewWithdrawalRequest(Integer, Integer, Principal)
	 * @author patelpo
	 */
	@Test
	public void testInitiateNewWithdrawalRequest() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest=mock(WithdrawalRequest.class);
		when(withdrawalService.initiateNewWithdrawalRequest(nullable(Integer.class), nullable(Integer.class), nullable(Principal.class))).thenReturn(withdrawalRequest);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer profileId = 0;
		Integer contractId = 0;
		Principal principal = mock(Principal.class);
		WithdrawalRequest result = underTest.initiateNewWithdrawalRequest(profileId, contractId, principal);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for markExpiredWithdrawals(java.util.Date, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#markExpiredWithdrawals(java.util.Date, Integer)
	 * @author patelpo
	 */
	/*@Test
	public void testMarkExpiredWithdrawals() throws Throwable {

		when(context.lookup(anyString())).thenReturn(withdrawalServiceHome);
		WithdrawalServiceRemote withdrawalServiceRemote=mock(WithdrawalServiceRemote.class);
		when(withdrawalServiceHome.create()).thenReturn(withdrawalServiceRemote);
		when(withdrawalServiceRemote.markExpiredWithdrawals(nullable(java.util.Date.class),nullable(Integer.class))).thenReturn(100);

		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Date checkDate = mock(Date.class);
		Integer profileId = 0;
		int result = underTest.markExpiredWithdrawals(checkDate, profileId);

		// Then
		assertEquals(100, result);
	}*/
	
	/**
	 * Parasoft Jtest UTA: Test for performReviewDefaultSetup(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#performReviewDefaultSetup(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testPerformReviewDefaultSetup() throws Throwable {
		
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		when(withdrawalService.performReviewDefaultSetup(nullable(WithdrawalRequest.class))).thenReturn(withdrawalRequest);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest result = underTest.performReviewDefaultSetup(withdrawalRequest);

		// Then
		assertNotNull(result);
	}
	

	/**
	 * Parasoft Jtest UTA: Test for performStep1DefaultSetup(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#performStep1DefaultSetup(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testPerformStep1DefaultSetup() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		when(withdrawalService.performStep1DefaultSetup(nullable(WithdrawalRequest.class))).thenReturn(withdrawalRequest);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest result = underTest.performStep1DefaultSetup(withdrawalRequest);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for performStep2DefaultSetup(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#performStep2DefaultSetup(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testPerformStep2DefaultSetup() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		when(withdrawalService.performStep2DefaultSetup(nullable(WithdrawalRequest.class))).thenReturn(withdrawalRequest);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest result = underTest.performStep2DefaultSetup(withdrawalRequest);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for proceedToStep2(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#proceedToStep2(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testProceedToStep2() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest=mock(WithdrawalRequest.class);
		PowerMockito.doNothing().when(withdrawalService).proceedToStep2(nullable(WithdrawalRequest.class));
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		underTest.proceedToStep2(request);

	}

	/**
	 * Parasoft Jtest UTA: Test for processApproved(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#processApproved(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testProcessApproved() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		PowerMockito.doNothing().when(withdrawalService).processApproved(nullable(WithdrawalRequest.class));
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		underTest.processApproved(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Test for readWithdrawalRequestForEdit(Integer, Principal)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#readWithdrawalRequestForEdit(Integer, Principal)
	 * @author patelpo
	 */
	@Test
	public void testReadWithdrawalRequestForEdit() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		when(withdrawalService.readWithdrawalRequestForEdit(nullable(Integer.class),nullable(Principal.class))).thenReturn(withdrawalRequest);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer submissionId = 0;
		Principal principal = mock(Principal.class);
		WithdrawalRequest result = underTest.readWithdrawalRequestForEdit(submissionId, principal);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for readWithdrawalRequestForView(Integer, Principal)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#readWithdrawalRequestForView(Integer, Principal)
	 * @author patelpo
	 */
	@Test
	public void testReadWithdrawalRequestForView() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		when(withdrawalService.readWithdrawalRequestForView(nullable(Integer.class),nullable(Principal.class))).thenReturn(withdrawalRequest);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		Integer submissionId = 0;
		Principal principal = mock(Principal.class);
		WithdrawalRequest result = underTest.readWithdrawalRequestForView(submissionId, principal);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for recalculate(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#recalculate(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testRecalculate() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		PowerMockito.doNothing().when(withdrawalService).recalculate(nullable(WithdrawalRequest.class));
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		underTest.recalculate(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Test for returnToStep1(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#returnToStep1(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testReturnToStep1() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		PowerMockito.doNothing().when(withdrawalService).returnToStep1(nullable(WithdrawalRequest.class));
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		underTest.returnToStep1(request);

	}

	/**
	 * Parasoft Jtest UTA: Test for returnToStep1WithTerminationDate(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#returnToStep1WithTerminationDate(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testReturnToStep1WithTerminationDate() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		PowerMockito.doNothing().when(withdrawalService).returnToStep1WithTerminationDate(nullable(WithdrawalRequest.class));
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		underTest.returnToStep1WithTerminationDate(request);

	}

	/**
	 * Parasoft Jtest UTA: Test for save(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#save(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testSave() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		PowerMockito.doNothing().when(withdrawalService).save(nullable(WithdrawalRequest.class));
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		underTest.save(withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Test for sendForApproval(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#sendForApproval(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testSendForApproval() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		when(withdrawalService.sendToApprover(nullable(WithdrawalRequest.class))).thenReturn(withdrawalRequest);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest result = underTest.sendForApproval(withdrawalRequest);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for sendForReview(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#sendForReview(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testSendForReview() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		when(withdrawalService.sendToReviewer(nullable(WithdrawalRequest.class))).thenReturn(withdrawalRequest);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest result = underTest.sendForReview(withdrawalRequest);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for submitParticipantInitiatedWithdrawal(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate#submitParticipantInitiatedWithdrawal(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testSubmitParticipantInitiatedWithdrawal() throws Throwable {
		when(context.lookup(anyString())).thenReturn(withdrawalServiceLocalHome);
		when(withdrawalServiceLocalHome.create()).thenReturn(withdrawalService);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		when(withdrawalService.submitParticipantInitiatedWithdrawal(nullable(WithdrawalRequest.class))).thenReturn(withdrawalRequest);
		// Given
		WithdrawalServiceDelegate underTest = WithdrawalServiceDelegate.getInstance();

		// When
		WithdrawalRequest result = underTest.submitParticipantInitiatedWithdrawal(withdrawalRequest);

		// Then
		assertNotNull(result);
	}
}