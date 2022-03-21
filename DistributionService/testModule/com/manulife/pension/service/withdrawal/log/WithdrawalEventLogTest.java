/**
 * 
 */
package com.manulife.pension.service.withdrawal.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.manulife.pension.util.log.LogEventException;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalEventLog
 *
 * @see com.manulife.pension.service.withdrawal.log.WithdrawalEventLog
 * @author patelpo
 */
public class WithdrawalEventLogTest {

	/**
	 * Parasoft Jtest UTA: Test for getApplicationId()
	 *
	 * @see com.manulife.pension.service.withdrawal.log.WithdrawalEventLog#getApplicationId()
	 * @author patelpo
	 */
	@Test
	public void testGetApplicationId() throws Throwable {
		// Given
		WithdrawalEventLog underTest = new WithdrawalEventLog();

		// When
		String result = underTest.getApplicationId();

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getEventName()
	 *
	 * @see com.manulife.pension.service.withdrawal.log.WithdrawalEventLog#getEventName()
	 * @author patelpo
	 */
	@Test
	public void testGetEventName() throws Throwable {
		// Given
		WithdrawalEventLog underTest = new WithdrawalEventLog();

		// When
		String result = underTest.getEventName();

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for prepareLogData()
	 *
	 * @see com.manulife.pension.service.withdrawal.log.WithdrawalEventLog#prepareLogData()
	 * @author patelpo
	 */
	@Test
	public void testPrepareLogData() throws Throwable {
		// Given
		WithdrawalEventLog underTest = new WithdrawalEventLog();
		
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
	 * @see com.manulife.pension.service.withdrawal.log.WithdrawalEventLog#setApplicationId(String)
	 * @author patelpo
	 */
	@Test
	public void testSetApplicationId() throws Throwable {
		// Given
		WithdrawalEventLog underTest = new WithdrawalEventLog();

		// When
		String applicationId = ""; // UTA: default value
		underTest.setApplicationId(applicationId);

	}

	/**
	 * Parasoft Jtest UTA: Test for validate()
	 *
	 * @see com.manulife.pension.service.withdrawal.log.WithdrawalEventLog#validate()
	 * @author patelpo
	 */
	@Test(expected=LogEventException.class)
	public void testValidate() throws Throwable {
		// Given
		WithdrawalEventLog underTest = new WithdrawalEventLog();
		
		underTest.addLogInfo("SUBMISSION_ID", "TEST");

		// When
		underTest.validate();

	}
	@Test(expected=LogEventException.class)
	public void testValidate_1() throws Throwable {
		// Given
		WithdrawalEventLog underTest = new WithdrawalEventLog();
		
		// When
		underTest.validate();

	}
}