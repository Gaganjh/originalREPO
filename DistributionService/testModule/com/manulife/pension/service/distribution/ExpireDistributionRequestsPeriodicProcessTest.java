/**
 * 
 */
package com.manulife.pension.service.distribution;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.util.JdbcHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for ExpireDistributionRequestsPeriodicProcess
 *
 * @see com.manulife.pension.service.distribution.ExpireDistributionRequestsPeriodicProcess
 * @author patelpo
 */
@PrepareForTest({ LoanServiceDelegate.class, WithdrawalServiceDelegate.class, Logger.class  })
@RunWith(PowerMockRunner.class)
public class ExpireDistributionRequestsPeriodicProcessTest {
	Logger getLoggerResult = mock(Logger.class); // UTA: default value
	@Before
	public void setUp() throws Exception {
		
		spy(Logger.class);

		doReturn(getLoggerResult).when(Logger.class, "getLogger", Mockito.any(Class.class));
		when(getLoggerResult.isDebugEnabled()).thenReturn(true);
 }

	/**
	 * Parasoft Jtest UTA: Test for execute()
	 *
	 * @see com.manulife.pension.service.distribution.ExpireDistributionRequestsPeriodicProcess#execute()
	 * @author patelpo
	 */
	@Test
	public void testExecute() throws Throwable {
		spy(LoanServiceDelegate.class);

		LoanServiceDelegate getInstanceResult = mock(LoanServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(LoanServiceDelegate.class, "getInstance");

		// Given
		ExpireDistributionRequestsPeriodicProcess underTest = new ExpireDistributionRequestsPeriodicProcess();
		String distributionType = "L"; // UTA: default value
		underTest.setDistributionType(distributionType);

		// When
		underTest.execute();

	}

	@Test
	public void testExecute1() throws Throwable {

		spy(WithdrawalServiceDelegate.class);

		WithdrawalServiceDelegate getInstanceResult = mock(WithdrawalServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(WithdrawalServiceDelegate.class, "getInstance");

		// Given
		ExpireDistributionRequestsPeriodicProcess underTest = new ExpireDistributionRequestsPeriodicProcess();
		String distributionType = "W"; // UTA: default value
		underTest.setDistributionType(distributionType);

		// When
		underTest.execute();

	}
	

	/**
	 * Parasoft Jtest UTA: Test for setDistributionType(String)
	 *
	 * @see com.manulife.pension.service.distribution.ExpireDistributionRequestsPeriodicProcess#setDistributionType(String)
	 * @author patelpo
	 */
	@Test
	public void testSetDistributionType() throws Throwable {
		// Given
		ExpireDistributionRequestsPeriodicProcess underTest = new ExpireDistributionRequestsPeriodicProcess();

		// When
		String distributionType = ""; // UTA: default value
		underTest.setDistributionType(distributionType);

	}
}