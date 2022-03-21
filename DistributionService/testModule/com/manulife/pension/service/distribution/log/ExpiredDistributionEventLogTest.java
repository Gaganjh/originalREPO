/**
 * 
 */
package com.manulife.pension.service.distribution.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.manulife.pension.util.log.LogEventException;

/**
 * Parasoft Jtest UTA: Test class for ExpiredDistributionEventLog
 *
 * @see com.manulife.pension.service.distribution.log.ExpiredDistributionEventLog
 * @author patelpo
 */
public class ExpiredDistributionEventLogTest {

	/**
	 * Parasoft Jtest UTA: Test for getApplicationId()
	 *
	 * @see com.manulife.pension.service.distribution.log.ExpiredDistributionEventLog#getApplicationId()
	 * @author patelpo
	 */
	@Test
	public void testGetApplicationId() throws Throwable {
		// Given
		ExpiredDistributionEventLog underTest = new ExpiredDistributionEventLog();

		// When
		String result = underTest.getApplicationId();

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getEventName()
	 *
	 * @see com.manulife.pension.service.distribution.log.ExpiredDistributionEventLog#getEventName()
	 * @author patelpo
	 */
	@Test
	public void testGetEventName() throws Throwable {
		// Given
		ExpiredDistributionEventLog underTest = new ExpiredDistributionEventLog();

		// When
		String result = underTest.getEventName();

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for prepareLogData()
	 *
	 * @see com.manulife.pension.service.distribution.log.ExpiredDistributionEventLog#prepareLogData()
	 * @author patelpo
	 */
	@Test
	public void testPrepareLogData() throws Throwable {
		// Given
		ExpiredDistributionEventLog underTest = new ExpiredDistributionEventLog();
		
		underTest.addLogInfo("SUMISSION_ID", "TEST");

		// When
		String result = underTest.prepareLogData();

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setApplicationId(String)
	 *
	 * @see com.manulife.pension.service.distribution.log.ExpiredDistributionEventLog#setApplicationId(String)
	 * @author patelpo
	 */
	@Test
	public void testSetApplicationId() throws Throwable {
		// Given
		ExpiredDistributionEventLog underTest = new ExpiredDistributionEventLog();

		// When
		String applicationId = ""; // UTA: default value
		underTest.setApplicationId(applicationId);

	}

	/**
	 * Parasoft Jtest UTA: Test for validate()
	 *
	 * @see com.manulife.pension.service.distribution.log.ExpiredDistributionEventLog#validate()
	 * @author patelpo
	 */
	@Test(expected=LogEventException.class)
	public void testValidate() throws Throwable {
		// Given
		ExpiredDistributionEventLog underTest = new ExpiredDistributionEventLog();

		
		// When
		underTest.validate();

	}
	
}