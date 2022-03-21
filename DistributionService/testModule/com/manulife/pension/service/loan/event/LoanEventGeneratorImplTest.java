/**
 * 
 */
package com.manulife.pension.service.loan.event;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.nullable;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.spi.NamingManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.event.Event;
import com.manulife.pension.event.EventBaseUtility.EventDeliveryStatus;
import com.manulife.pension.event.LoanRequestAboutToExpireEvent;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.service.security.role.BasicInternalUser;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.util.BaseEnvironment;
import com.sun.naming.internal.ResourceManager;

/**
 * Parasoft Jtest UTA: Test class for LoanEventGeneratorImpl
 *
 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl
 * @author patelpo
 */
@PrepareForTest({  ContractServiceDelegate.class, NamingManager.class,ResourceManager.class, BaseEnvironment.class,
		EventClientUtility.class, SecurityServiceDelegate.class })
@RunWith(PowerMockRunner.class)
public class LoanEventGeneratorImplTest {

	EventClientUtility getInstanceResult = mock(EventClientUtility.class);
	Context context=mock(Context.class);
	InitialContext icontext=mock(InitialContext.class);
	@Before
	public void setUp() throws Exception {

		spy(NamingManager.class);
		doReturn(true).when(NamingManager.class);
		NamingManager.hasInitialContextFactoryBuilder();
			
		doReturn(context).when(NamingManager.class);

		NamingManager.getInitialContext(nullable(Hashtable.class));
		spy(ResourceManager.class);
		Hashtable<String,Object> hashobj=new Hashtable<String,Object>();
		hashobj.put("java.naming.factory.initial",new Object());
		doReturn(hashobj).when(ResourceManager.class);
		ResourceManager.getInitialEnvironment(nullable(Hashtable.class));

		whenNew(InitialContext.class).withAnyArguments().thenReturn(icontext);
		when(icontext.lookup(nullable(String.class))).thenReturn("ezk");
		
		
		spy(EventClientUtility.class);
		doReturn(getInstanceResult).when(EventClientUtility.class);
		EventClientUtility.getInstance(nullable(String.class));
		EventDeliveryStatus arg0=mock(EventDeliveryStatus.class);
		when(getInstanceResult.prepareAndSendJMSMessage(nullable(Event.class))).thenReturn(arg0);
	}

	/**
	 * Parasoft Jtest UTA: Test for getContractId()
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#getContractId()
	 * @author patelpo
	 */
	@Test
	public void testGetContractId() throws Throwable {
		// Given
		int contractId = 10;  
		int submissionId = 20;  
		int eventInitiatorId = 30;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int result = underTest.getContractId();

		// Then
		assertEquals(10, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getEventInitiatorId()
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#getEventInitiatorId()
	 * @author patelpo
	 */
	@Test
	public void testGetEventInitiatorId() throws Throwable {
		// Given
		int contractId =10;  
		int submissionId = 20;  
		int eventInitiatorId = 30;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int result = underTest.getEventInitiatorId();

		// Then
		assertEquals(30, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getSubmissionId()
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#getSubmissionId()
	 * @author patelpo
	 */
	@Test
	public void testGetSubmissionId() throws Throwable {
		// Given
		int contractId = 10;  
		int submissionId = 20;  
		int eventInitiatorId = 30;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int result = underTest.getSubmissionId();

		// Then
		assertEquals(20, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for prepareAndSendAboutToExpireEvent(int, String, int)
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#prepareAndSendAboutToExpireEvent(int, String, int)
	 * @author patelpo
	 */
	@Test
	public void testPrepareAndSendAboutToExpireEvent() throws Throwable {
		EventDeliveryStatus status = EventDeliveryStatus.SUCCESSFULLY_RECOVERED;
		when(getInstanceResult.prepareAndSendJMSMessage(any(Event.class))).thenReturn(status);
		LoanRequestAboutToExpireEvent newLoanRequestAboutToExpireEventResult = mock(
				LoanRequestAboutToExpireEvent.class);  
		whenNew(LoanRequestAboutToExpireEvent.class).withAnyArguments()
				.thenReturn(newLoanRequestAboutToExpireEventResult);

		// Given
		int contractId = 10;  
		int submissionId = 20;  
		int eventInitiatorId = 30;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int participantProfileId = 10;  
		String loanStatus = "LS";  
		int daysToExpire = 20;  
		underTest.prepareAndSendAboutToExpireEvent(participantProfileId, loanStatus, daysToExpire);

	}

	/**
	 * Parasoft Jtest UTA: Test for prepareAndSendApprovedEvent(int, String, Boolean)
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#prepareAndSendApprovedEvent(int, String, Boolean)
	 * @author patelpo
	 */
	@Test
	public void testPrepareAndSendApprovedEvent() throws Throwable {
		
		// Given
		int contractId = 10;  
		int submissionId = 10;  
		int eventInitiatorId = 20;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int participantProfileId = 10;  
		String approverRoleCode = "ARC";  
		Boolean lastFeeChangedWasPlanSponsorUserInd = true;  
		underTest.prepareAndSendApprovedEvent(participantProfileId, approverRoleCode,
				lastFeeChangedWasPlanSponsorUserInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for prepareAndSendDeletedEvent(int, String)
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#prepareAndSendDeletedEvent(int, String)
	 * @author patelpo
	 */
	@Test
	public void testPrepareAndSendDeletedEvent() throws Throwable {
		// Given
		int contractId =10;  
		int submissionId = 20;  
		int eventInitiatorId = 30;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int participantProfileId = 10;  
		String loanStatusBeforeDeleted = "loan_sts_bef_del";  
		underTest.prepareAndSendDeletedEvent(participantProfileId, loanStatusBeforeDeleted);

	}

	/**
	 * Parasoft Jtest UTA: Test for prepareAndSendDeniedEvent(int, String)
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#prepareAndSendDeniedEvent(int, String)
	 * @author patelpo
	 */
	@Test
	public void testPrepareAndSendDeniedEvent() throws Throwable {
		// Given
		int contractId = 20;  
		int submissionId = 10;  
		int eventInitiatorId = 20;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int participantProfileId = 10;  
		String loanStatusBeforeDenied = "LSBD";  
		underTest.prepareAndSendDeniedEvent(participantProfileId, loanStatusBeforeDenied);

	}

	/**
	 * Parasoft Jtest UTA: Test for prepareAndSendExpiredEvent()
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#prepareAndSendExpiredEvent()
	 * @author patelpo
	 */
	@Test
	public void testPrepareAndSendExpiredEvent() throws Throwable {
		// Given
		int contractId = 30;  
		int submissionId = 20;  
		int eventInitiatorId = 10;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		underTest.prepareAndSendExpiredEvent();

	}

	/**
	 * Parasoft Jtest UTA: Test for prepareAndSendLoanPackageEvent(int)
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#prepareAndSendLoanPackageEvent(int)
	 * @author patelpo
	 */
	@Test
	public void testPrepareAndSendLoanPackageEvent() throws Throwable {
		// Given
		int contractId = 10;  
		int submissionId = 20;  
		int eventInitiatorId =20;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int participantProfileId = 10;  
		underTest.prepareAndSendLoanPackageEvent(participantProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for prepareAndSendParticipantPendingApprovalEvent(int)
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#prepareAndSendParticipantPendingApprovalEvent(int)
	 * @author patelpo
	 */
	@Test
	public void testPrepareAndSendParticipantPendingApprovalEvent() throws Throwable {
		spy(SecurityServiceDelegate.class);
		
		UserRole role= new BasicInternalUser();
		ContractPermission contractPermission = new ContractPermission(role);
		
		SecurityServiceDelegate getInstanceResult22 = mock(SecurityServiceDelegate.class);  
		doReturn(getInstanceResult22).when(SecurityServiceDelegate.class, "getInstance");
		when(getInstanceResult22.getTpaFirmContractPermission(anyInt())).thenReturn(contractPermission);

		spy(ContractServiceDelegate.class);

		ContractServiceDelegate getInstanceResult2 = mock(ContractServiceDelegate.class);  
		doReturn(getInstanceResult2).when(ContractServiceDelegate.class, "getInstance");
		when(getInstanceResult2.isBundledGaContract(anyInt())).thenReturn(true);

		// Given
		int contractId = 10;  
		int submissionId = 6;  
		int eventInitiatorId = 18;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int participantProfileId = 15;  
		underTest.prepareAndSendParticipantPendingApprovalEvent(participantProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for prepareAndSendParticipantPendingReviewEvent(int)
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#prepareAndSendParticipantPendingReviewEvent(int)
	 * @author patelpo
	 */
	@Test
	public void testPrepareAndSendParticipantPendingReviewEvent() throws Throwable {
		spy(ContractServiceDelegate.class);

		ContractServiceDelegate getInstanceResult = mock(ContractServiceDelegate.class);  
		doReturn(getInstanceResult).when(ContractServiceDelegate.class, "getInstance");

		
		// Given
		int contractId = 10;  
		int submissionId = 12;  
		int eventInitiatorId = 15;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int participantProfileId = 15;  
		underTest.prepareAndSendParticipantPendingReviewEvent(participantProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for prepareAndSendParticipantRejectedEvent(int)
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#prepareAndSendParticipantRejectedEvent(int)
	 * @author patelpo
	 */
	@Test
	public void testPrepareAndSendParticipantRejectedEvent() throws Throwable {
		// Given
		int contractId = 22;  
		int submissionId = 20;  
		int eventInitiatorId = 15;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int participantProfileId = 12;  
		underTest.prepareAndSendParticipantRejectedEvent(participantProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for prepareAndSendPendingAcceptanceEvent(int)
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#prepareAndSendPendingAcceptanceEvent(int)
	 * @author patelpo
	 */
	@Test
	public void testPrepareAndSendPendingAcceptanceEvent() throws Throwable {
		// Given
		int contractId = 12;  
		int submissionId = 10;  
		int eventInitiatorId = 20;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int participantProfileId = 10;  
		underTest.prepareAndSendPendingAcceptanceEvent(participantProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for prepareAndSendPendingApprovalEvent(int)
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#prepareAndSendPendingApprovalEvent(int)
	 * @author patelpo
	 */
	@Test
	public void testPrepareAndSendPendingApprovalEvent() throws Throwable {
		spy(ContractServiceDelegate.class);

		ContractServiceDelegate getInstanceResult2 = mock(ContractServiceDelegate.class);  
		doReturn(getInstanceResult2).when(ContractServiceDelegate.class, "getInstance");
	
		// Given
		int contractId = 12;  
		int submissionId = 10;  
		int eventInitiatorId = 20;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int participantProfileId = 10;  
		underTest.prepareAndSendPendingApprovalEvent(participantProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for prepareAndSendPendingReviewEvent(int)
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#prepareAndSendPendingReviewEvent(int)
	 * @author patelpo
	 */
	@Test
	public void testPrepareAndSendPendingReviewEvent() throws Throwable {
		spy(ContractServiceDelegate.class);

		ContractServiceDelegate getInstanceResult2 = mock(ContractServiceDelegate.class);  
		doReturn(getInstanceResult2).when(ContractServiceDelegate.class, "getInstance");
		when(getInstanceResult2.isBundledGaContract(anyInt())).thenReturn(true);

		// Given
		int contractId = 10;  
		int submissionId = 10;  
		int eventInitiatorId = 20;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int participantProfileId = 10;  
		underTest.prepareAndSendPendingReviewEvent(participantProfileId);

	}
	@Test
	public void testPrepareAndSendPendingReviewEvent_1() throws Throwable {
		spy(ContractServiceDelegate.class);

		ContractServiceDelegate getInstanceResult2 = mock(ContractServiceDelegate.class);  
		doReturn(getInstanceResult2).when(ContractServiceDelegate.class, "getInstance");
		when(getInstanceResult2.isBundledGaContract(anyInt())).thenReturn(false);

		// Given
		int contractId = 10;  
		int submissionId = 20;  
		int eventInitiatorId = 20;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int participantProfileId = 20;  
		underTest.prepareAndSendPendingReviewEvent(participantProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for setContractId(int)
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#setContractId(int)
	 * @author patelpo
	 */
	@Test
	public void testSetContractId() throws Throwable {
		// Given
		int contractId = 12;  
		int submissionId = 10;  
		int eventInitiatorId = 20;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int contractId2 = 10;  
		underTest.setContractId(contractId2);

	}

	/**
	 * Parasoft Jtest UTA: Test for setEventInitiatorId(int)
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#setEventInitiatorId(int)
	 * @author patelpo
	 */
	@Test
	public void testSetEventInitiatorId() throws Throwable {
		// Given
		int contractId = 10;  
		int submissionId = 20;  
		int eventInitiatorId = 30;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int eventInitiatorId2 = 10;  
		underTest.setEventInitiatorId(eventInitiatorId2);

	}

	/**
	 * Parasoft Jtest UTA: Test for setSubmissionId(int)
	 *
	 * @see com.manulife.pension.service.loan.event.LoanEventGeneratorImpl#setSubmissionId(int)
	 * @author patelpo
	 */
	@Test
	public void testSetSubmissionId() throws Throwable {
		// Given
		int contractId = 11;  
		int submissionId = 10;  
		int eventInitiatorId = 20;  
		LoanEventGeneratorImpl underTest = new LoanEventGeneratorImpl(contractId, submissionId, eventInitiatorId);

		// When
		int submissionId2 = 20;  
		underTest.setSubmissionId(submissionId2);

	}
}