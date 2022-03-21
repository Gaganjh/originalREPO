/**
 * 
 */
package com.manulife.pension.service.distribution;

import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;

import static org.powermock.api.mockito.PowerMockito.spy;

import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.service.loan.event.EventFactory;
import com.manulife.pension.service.loan.event.LoanEventGenerator;
import com.manulife.pension.service.loan.event.LoanEventGeneratorImpl;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.util.BaseEnvironment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for AboutToExpireDistributionRequestsPeriodicProcess
 *
 * @see com.manulife.pension.service.distribution.AboutToExpireDistributionRequestsPeriodicProcess
 * @author patelpo
 */
@PrepareForTest({ LoanServiceDelegate.class, EventFactory.class, LoanDefaults.class })
@RunWith(PowerMockRunner.class)
public class AboutToExpireDistributionRequestsPeriodicProcessTest {

	/**
	 * Parasoft Jtest UTA: Test for execute()
	 *
	 * @see com.manulife.pension.service.distribution.AboutToExpireDistributionRequestsPeriodicProcess#execute()
	 * @author patelpo
	 */
	@Test
	public void testExecute() throws Throwable {
		BaseEnvironment newBaseEnvironmentResult = mock(BaseEnvironment.class); // UTA: default value
		whenNew(BaseEnvironment.class).withAnyArguments().thenReturn(newBaseEnvironmentResult);

		spy(EventFactory.class);
		spy(LoanServiceDelegate.class);

		Loan loan = new Loan();
		loan.setApplyIrs10KDollarRuleInd(true);
		loan.setAtRiskInd("RISK");
		loan.setContractId(123);
		loan.setSubmissionId(456);
		loan.setParticipantProfileId(789);
		loan.setStatus("STATUS");

		List<Loan> loanRequests = new ArrayList<>();
		loanRequests.add(loan);

		LoanServiceDelegate getInstanceResult = mock(LoanServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(LoanServiceDelegate.class, "getInstance");
		when(getInstanceResult.getAboutToExpireLoanRequests(anyInt())).thenReturn(loanRequests);

		LoanEventGenerator loanEventGenerator = mock(LoanEventGenerator.class);

		EventFactory getInstanceResult2 = mock(EventFactory.class); // UTA: default value
		doReturn(getInstanceResult2).when(EventFactory.class, "getInstance");
		when(getInstanceResult2.getLoanEventGenerator(anyInt(), anyInt(), anyInt())).thenReturn(loanEventGenerator);
		spy(LoanDefaults.class);

		int getAboutToExpireReminderDaysResult = 0; // UTA: default value
		doReturn(getAboutToExpireReminderDaysResult).when(LoanDefaults.class);
		LoanDefaults.getAboutToExpireReminderDays();

		doNothing().when(loanEventGenerator).prepareAndSendAboutToExpireEvent(anyInt(),anyString(), anyInt());
		
		// Given
		AboutToExpireDistributionRequestsPeriodicProcess underTest = new AboutToExpireDistributionRequestsPeriodicProcess();
		String distributionType = "L"; // UTA: default value
		underTest.setDistributionType(distributionType);

		// When
		underTest.execute();

	}
	
	/**
	 * Parasoft Jtest UTA: Test for setDistributionType(String)
	 *
	 * @see com.manulife.pension.service.distribution.AboutToExpireDistributionRequestsPeriodicProcess#setDistributionType(String)
	 * @author patelpo
	 */
	@Test
	public void testSetDistributionType() throws Throwable {
		// Given
		AboutToExpireDistributionRequestsPeriodicProcess underTest = new AboutToExpireDistributionRequestsPeriodicProcess();

		// When
		String distributionType = ""; // UTA: default value
		underTest.setDistributionType(distributionType);

	}
}