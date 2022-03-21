/**
 * 
 */
package com.manulife.pension.service.loan.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import com.manulife.pension.util.log.EventLog;
import com.manulife.pension.util.log.LogEventException;

/**
 * Parasoft Jtest UTA: Test class for LoanEventLog
 *
 * @see com.manulife.pension.service.loan.log.LoanEventLog
 * @author patelpo
 */
public class LoanEventLogTest {

	/**
	 * Parasoft Jtest UTA: Test for getApplicationId()
	 *
	 * @see com.manulife.pension.service.loan.log.LoanEventLog#getApplicationId()
	 * @author patelpo
	 */
	@Test
	public void testGetApplicationId() throws Throwable {
		// Given
		LoanEventLog underTest = new LoanEventLog();

		// When
		String result = underTest.getApplicationId();

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getEventName()
	 *
	 * @see com.manulife.pension.service.loan.log.LoanEventLog#getEventName()
	 * @author patelpo
	 */
	@Test
	public void testGetEventName() throws Throwable {
		// Given
		LoanEventLog underTest = new LoanEventLog();

		// When
		String result = underTest.getEventName();

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for prepareLogData()
	 *
	 * @see com.manulife.pension.service.loan.log.LoanEventLog#prepareLogData()
	 * @author patelpo
	 */
	@Test
	public void testPrepareLogData() throws Throwable {
		// Given
		LoanEventLog underTest = new LoanEventLog();
		
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
	 * @see com.manulife.pension.service.loan.log.LoanEventLog#setApplicationId(String)
	 * @author patelpo
	 */
	@Test
	public void testSetApplicationId() throws Throwable {
		// Given
		LoanEventLog underTest = new LoanEventLog();

		// When
		String applicationId = ""; // UTA: default value
		underTest.setApplicationId(applicationId);

	}

	/**
	 * Parasoft Jtest UTA: Test for validate()
	 *
	 * @see com.manulife.pension.service.loan.log.LoanEventLog#validate()
	 * @author patelpo
	 */
	@Test(expected=LogEventException.class)
	public void testValidate() throws Throwable {
		// Given
		LoanEventLog underTest = new LoanEventLog();

		
		underTest.addLogInfo("SUBMISSION_ID", "TEST");
		
		
		// When
		underTest.validate();

	}
	@Test(expected=LogEventException.class)
	public void testValidate_1() throws Throwable {
		// Given
		LoanEventLog underTest = new LoanEventLog();
		
	
		// When
		underTest.validate();

	}
}