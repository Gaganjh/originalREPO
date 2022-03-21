/**
 * 
 */
package com.manulife.pension.service.withdrawal.log;

import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.Assert.*;

import com.manulife.pension.service.distribution.log.DistributionEventLog;
import com.manulife.pension.service.security.bd.log.BDLoginLog;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.util.log.EventLog;
import com.manulife.pension.util.log.EventLogFactory;
import com.manulife.pension.util.log.LogEventException;

import sun.util.logging.resources.logging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalLoggingHelper
 *
 * @see com.manulife.pension.service.withdrawal.log.WithdrawalLoggingHelper
 * @author patelpo
 */
@PrepareForTest({ EventLogFactory.class,SecurityConstants.class })
@RunWith(PowerMockRunner.class)
public class WithdrawalLoggingHelperTest {

	/**
	 * Parasoft Jtest UTA: Test for log(WithdrawalRequest, WithdrawalEvent, Class, String)
	 *
	 * @see com.manulife.pension.service.withdrawal.log.WithdrawalLoggingHelper#log(WithdrawalRequest, WithdrawalEvent, Class, String)
	 * @author patelpo
	 */
	@Test
	public void testLog() throws Throwable {
		
		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest();
		WithdrawalEvent withdrawalEvent = WithdrawalEvent.SEND_FOR_REVIEW; // UTA: default value
		Class clazz = Class.forName("java.lang.Object"); // UTA: default value
		String methodName = "TestLog"; // UTA: default value
		WithdrawalLoggingHelper.log(withdrawalRequest, withdrawalEvent, clazz, methodName);

	}
	
	
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Integer getLastUpdatedByIdResult = 0; // UTA: default value
		when(withdrawalRequest.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult);

		String getParticipantSSNResult = ""; // UTA: default value
		when(withdrawalRequest.getParticipantSSN()).thenReturn(getParticipantSSNResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(withdrawalRequest.getSubmissionId()).thenReturn(getSubmissionIdResult);

		String toLogResult = "100*10"; // UTA: default value
		when(withdrawalRequest.toLog()).thenReturn(toLogResult);
		return withdrawalRequest;
	}
	

	/**
	 * Parasoft Jtest UTA: Test for logShort(Integer, Integer, WithdrawalEvent, Class, String)
	 *
	 * @see com.manulife.pension.service.withdrawal.log.WithdrawalLoggingHelper#logShort(Integer, Integer, WithdrawalEvent, Class, String)
	 * @author patelpo
	 */
	@Test
	public void testLogShort() throws Throwable {
		spy(EventLogFactory.class);

		EventLogFactory getInstanceResult = mock(EventLogFactory.class); // UTA: default value
		doReturn(getInstanceResult).when(EventLogFactory.class);
		EventLogFactory.getInstance();
		DistributionEventLog eventLog = new DistributionEventLog();
		eventLog.addLogInfo("ACTION", "TESTT");
		when(getInstanceResult.createEventLog(any(Class.class))).thenReturn(eventLog);
	
		
		// When
		Integer submissionId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		WithdrawalEvent withdrawalEvent = WithdrawalEvent.SEND_FOR_REVIEW; // UTA: default value
		Class clazz = Class.forName("com.manulife.pension.service.distribution.log.DistributionEventLog"); // UTA: default value
		String methodName = "Test"; // UTA: default value
		WithdrawalLoggingHelper.logShort(submissionId, userProfileId, withdrawalEvent, clazz, methodName);

	}
	
	@Test(expected = RuntimeException.class)
	public void testLogShort_exception() throws Throwable {
		spy(EventLogFactory.class);
		
		EventLogFactory getInstanceResult = mock(EventLogFactory.class); // UTA: default value
		doReturn(getInstanceResult).when(EventLogFactory.class);
		EventLogFactory.getInstance();
		DistributionEventLog eventLog = new DistributionEventLog();
		when(getInstanceResult.createEventLog(any(Class.class))).thenReturn(eventLog);
		
		
		// When
		Integer submissionId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		WithdrawalEvent withdrawalEvent = WithdrawalEvent.SEND_FOR_REVIEW; // UTA: default value
		Class clazz = Class.forName("com.manulife.pension.service.distribution.log.DistributionEventLog"); // UTA: default value
		String methodName = "Test"; // UTA: default value
		WithdrawalLoggingHelper.logShort(submissionId, userProfileId, withdrawalEvent, clazz, methodName);
		
	}
	
}