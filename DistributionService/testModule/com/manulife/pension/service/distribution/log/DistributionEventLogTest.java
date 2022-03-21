/**
 * 
 */
package com.manulife.pension.service.distribution.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.util.log.EventLog;
import com.manulife.pension.util.log.LogEventException;

/**
 * Parasoft Jtest UTA: Test class for DistributionEventLog
 *
 * @see com.manulife.pension.service.distribution.log.DistributionEventLog
 * @author patelpo
 */
@PrepareForTest({ Logger.class  })
@RunWith(PowerMockRunner.class)

public class DistributionEventLogTest {
	Logger getLoggerResult = mock(Logger.class); // UTA: default value
	@Before
	public void setUp() throws Exception {
		
		spy(Logger.class);

		doReturn(getLoggerResult).when(Logger.class, "getLogger", Mockito.any(Class.class));
		when(getLoggerResult.isDebugEnabled()).thenReturn(true);
 }

	/**
	 * Parasoft Jtest UTA: Test for getApplicationId()
	 *
	 * @see com.manulife.pension.service.distribution.log.DistributionEventLog#getApplicationId()
	 * @author patelpo
	 */
	@Test
	public void testGetApplicationId() throws Throwable {
		// Given
		DistributionEventLog underTest = new DistributionEventLog();

		// When
		String result = underTest.getApplicationId();

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getEventName()
	 *
	 * @see com.manulife.pension.service.distribution.log.DistributionEventLog#getEventName()
	 * @author patelpo
	 */
	@Test
	public void testGetEventName() throws Throwable {
		// Given
		DistributionEventLog underTest = new DistributionEventLog();

		// When
		String result = underTest.getEventName();

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for prepareLogData()
	 *
	 * @see com.manulife.pension.service.distribution.log.DistributionEventLog#prepareLogData()
	 * @author patelpo
	 */
	@Test
	public void testPrepareLogData() throws Throwable {
		// Given
		DistributionEventLog underTest = new DistributionEventLog();
		
		underTest.addLogInfo("SUBMISSION_ID", "TEST");

		// When
		String result = underTest.prepareLogData();

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setApplicationId(String)
	 *
	 * @see com.manulife.pension.service.distribution.log.DistributionEventLog#setApplicationId(String)
	 * @author patelpo
	 */
	@Test
	public void testSetApplicationId() throws Throwable {
		// Given
		DistributionEventLog underTest = new DistributionEventLog();

		// When
		String applicationId = "10"; // UTA: default value
		underTest.setApplicationId(applicationId);

	}

	/**
	 * Parasoft Jtest UTA: Test for validate()
	 *
	 * @see com.manulife.pension.service.distribution.log.DistributionEventLog#validate()
	 * @author patelpo
	 */
	@Test(expected=LogEventException.class)
	public void testValidate() throws Throwable {
		// Given
		DistributionEventLog underTest = new DistributionEventLog();
		
		underTest.addLogInfo("SUBMISSION_ID", "TEST");
		

		// When
		underTest.validate();

	}
	@Test(expected=LogEventException.class)
	public void testValidate_1() throws Throwable {
		// Given
		DistributionEventLog underTest = new DistributionEventLog();
	
	
		// When
		underTest.validate();

	}
}