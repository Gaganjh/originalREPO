/**
 * 
 */
package com.manulife.pension.service.loan.domain;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang.NotImplementedException;

import com.manulife.pension.service.loan.valueobject.Loan;
import org.junit.Test;

/**
 * Parasoft Jtest UTA: Test class for LoanStateFactory
 *
 * @see com.manulife.pension.service.loan.domain.LoanStateFactory
 * @author patelpo
 */
public class LoanStateFactoryTest {

	/**
	 * Parasoft Jtest UTA: Test for getState(LoanStateEnum)
	 *
	 * @see com.manulife.pension.service.loan.domain.LoanStateFactory#getState(LoanStateEnum)
	 * @author patelpo
	 */
	@Test
	public void testGetState() throws Throwable {
		// When
		LoanStateEnum loanStateEnum = LoanStateEnum.PENDING_APPROVAL;
		LoanState result = LoanStateFactory.getState(loanStateEnum);

		// Then
		// assertNotNull(result);
	}
	
	

	/**
	 * Parasoft Jtest UTA: Test for getState(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.LoanStateFactory#getState(Loan)
	 * @author patelpo
	 */
	@Test
	public void testGetState2() throws Throwable {
		// When
		Loan inputLoan = mockLoan();
		LoanState result = LoanStateFactory.getState(inputLoan);

		// Then
		// assertNotNull(result);
	}
	@Test(expected=NullPointerException.class)
	public void testGetStateException() throws Throwable {
		// When
		Loan inputLoan = mockLoan_1();
		LoanState result = LoanStateFactory.getState(inputLoan);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan() throws Throwable {
		Loan inputLoan = mock(Loan.class);
		String getStatusResult = "L4"; // UTA: default value
		when(inputLoan.getStatus()).thenReturn(getStatusResult);
		return inputLoan;
	}
	private static Loan mockLoan_1() throws Throwable {
		Loan inputLoan = mock(Loan.class);
		String getStatusResult = "Test"; // UTA: default value
		when(inputLoan.getStatus()).thenReturn(getStatusResult);
		return inputLoan;
	}
}