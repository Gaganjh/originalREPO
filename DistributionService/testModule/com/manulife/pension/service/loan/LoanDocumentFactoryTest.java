/**
 * 
 */
package com.manulife.pension.service.loan;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.reset;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.manulife.pension.service.account.transaction.LoanAmortizationTransaction;
import com.manulife.pension.service.account.transaction.LoanAmortizationTransactionHome;
import com.manulife.pension.service.account.transaction.LoanRequestPackageTransaction;
import com.manulife.pension.service.account.transaction.LoanRequestPackageTransactionHome;
import com.manulife.pension.service.account.transaction.util.LoanRequestTransactionHelper;
import com.manulife.pension.service.account.valueobject.LoanAmortizationSchedule;
import com.manulife.pension.service.account.valueobject.LoanRequestPackageCharges;
import com.manulife.pension.service.account.valueobject.LoanScenario;
import com.manulife.pension.service.cache.Cache;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.loan.dao.LoanDao;
import com.manulife.pension.service.loan.dao.LoanSupportDao;
import com.manulife.pension.service.loan.domain.LoanStateFactory;
import com.manulife.pension.service.loan.domain.LoanValidationHelper;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanRecipient;
import com.manulife.pension.service.loan.valueobject.document.AmortizationSchedule;
import com.manulife.pension.service.loan.valueobject.document.Instructions;
import com.manulife.pension.service.loan.valueobject.document.LoanDocument;
import com.manulife.pension.service.loan.valueobject.document.LoanDocumentBundle;
import com.manulife.pension.service.loan.valueobject.document.LoanForm;
import com.manulife.pension.service.loan.valueobject.document.PromissoryNote;
import com.manulife.pension.service.loan.valueobject.document.TruthInLendingNotice;
import com.manulife.pension.util.JdbcHelper;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.reset;
/**
 * Parasoft Jtest UTA: Test class for LoanDocumentFactory
 *
 * @see com.manulife.pension.service.loan.LoanDocumentFactory
 * @author patelpo
 */

@PrepareForTest({ Cache.class, LoanDocumentFactory.class, PortableRemoteObject.class })
@RunWith(PowerMockRunner.class)
public class LoanDocumentFactoryTest {
	static InitialContext newInitialContextResult;
	static LoanRequestPackageTransactionHome loanRequestPackageTransactionHome;
	static LoanRequestPackageTransaction loanRequestPackageTransaction;
	static LoanAmortizationTransactionHome loanAmortizationTransactionHome;
	
	@BeforeClass
	public static void setUp( ) throws Exception
	{
		newInitialContextResult = mock(InitialContext.class);
		loanRequestPackageTransactionHome=mock(LoanRequestPackageTransactionHome.class);
		loanRequestPackageTransaction=mock(LoanRequestPackageTransaction.class);
		loanAmortizationTransactionHome=mock(LoanAmortizationTransactionHome.class);
	}
	/**
	 * Parasoft Jtest UTA: Test for getDocumentBundle(Loan, Class[])
	 *
	 * @see com.manulife.pension.service.loan.LoanDocumentFactory#getDocumentBundle(Loan, Class[])
	 * @author patelpo
	 */
	
	@Test(expected = EJBException.class)
	public void testGetDocumentBundle() throws Throwable {
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(anyString())).thenReturn(loanRequestPackageTransactionHome);
		when(loanRequestPackageTransactionHome.create()).thenReturn(loanRequestPackageTransaction);
		LoanRequestPackageCharges loanRequestPackageCharges = new LoanRequestPackageCharges();
		when(loanRequestPackageTransaction.getLoanRequestPackageCharges(any(LoanAmortizationSchedule.class), any(Date.class), any(Date.class), any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(loanRequestPackageCharges);
		
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(loanAmortizationTransactionHome);
		LoanAmortizationTransaction loanAmortizationTransaction=mock(LoanAmortizationTransaction.class);
		LoanAmortizationSchedule loanamortizationSchedule = mock(LoanAmortizationSchedule.class);
		LoanScenario loanScenario =mock(LoanScenario.class);
		int[] errorCodes = new int[0];
		
		when(loanScenario.getErrorCodes()).thenReturn(errorCodes);
		when(loanamortizationSchedule.getLoanScenario()).thenReturn(loanScenario);
		int[] errcode =new int[0];
		
		when(loanamortizationSchedule.getErrorCodes()).thenReturn(errcode);
		
		
		
		when(loanAmortizationTransaction.execute(nullable(Date.class), nullable(Date.class), any(LoanScenario

.class))).thenReturn(loanamortizationSchedule);
		when(loanAmortizationTransactionHome.create()).thenReturn(loanAmortizationTransaction);
		spy(PortableRemoteObject.class);
		
		LoanAmortizationSchedule loanAmortizationSchedule=new LoanAmortizationSchedule();
		
		
		
		loanScenario.setErrorCodes(errorCodes);
		loanAmortizationSchedule.setLoanScenario(loanScenario);
		
		// Given
		LoanDocumentFactory underTest = new LoanDocumentFactory();

		// When
		Loan loan = mockLoan();
		Class<LoanDocument>[] documentClasses = new Class[1]; // UTA: default value
		LoanDocument truthInLendingNotice = new TruthInLendingNotice();
		
		documentClasses[0] =  (Class<LoanDocument>) truthInLendingNotice.getClass();
		

		
		LoanDocument document = mock(LoanDocument.class);

		LoanDocumentBundle result = underTest.getDocumentBundle(loan, documentClasses);

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetDocumentBundle_2() throws Throwable {
		PowerMockito.whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(anyString())).thenReturn(loanRequestPackageTransactionHome);
		when(loanRequestPackageTransactionHome.create()).thenReturn(loanRequestPackageTransaction);
		LoanRequestPackageCharges loanRequestPackageCharges = mock(LoanRequestPackageCharges.class);
		when(loanRequestPackageTransaction.getLoanRequestPackageCharges(any(LoanAmortizationSchedule.class), any(Date.class), any(Date.class), any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(loanRequestPackageCharges);
		
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(loanAmortizationTransactionHome);
		LoanAmortizationTransaction loanAmortizationTransaction=mock(LoanAmortizationTransaction.class);
		LoanAmortizationSchedule loanamortizationSchedule = mock(LoanAmortizationSchedule.class);
		LoanScenario loanScenario =mock(LoanScenario.class);
		int[] errorCodes = new int[0];
		
		when(loanScenario.getErrorCodes()).thenReturn(errorCodes);
		when(loanamortizationSchedule.getLoanScenario()).thenReturn(loanScenario);
		int[] errcode =new int[0];
		
		when(loanamortizationSchedule.getErrorCodes()).thenReturn(errcode);
		
		
		
		when(loanAmortizationTransaction.execute(nullable(Date.class), nullable(Date.class), any(LoanScenario
				
				.class))).thenReturn(loanamortizationSchedule);
		when(loanAmortizationTransactionHome.create()).thenReturn(loanAmortizationTransaction);
		spy(PortableRemoteObject.class);
		
		LoanAmortizationSchedule loanAmortizationSchedule=new LoanAmortizationSchedule();
		
		
		
		loanScenario.setErrorCodes(errorCodes);
		loanAmortizationSchedule.setLoanScenario(loanScenario);
		
		// Given
		LoanDocumentFactory underTest = new LoanDocumentFactory();
		
		// When
		Loan loan = mockLoan();
		Class<LoanDocument>[] documentClasses = new Class[1]; // UTA: default value
		LoanDocument promissoryNote = new PromissoryNote();
		
		documentClasses[0] = (Class<LoanDocument>) promissoryNote.getClass();
		
		
		LoanDocument document = mock(LoanDocument.class);
		
		LoanDocumentBundle result = underTest.getDocumentBundle(loan, documentClasses);
		
		// Then
		// assertNotNull(result);
	}
/*	@Test(expected = EJBException.class)
	public void testGetDocumentBundle_3() throws Throwable {
		PowerMockito.whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(anyString())).thenReturn(loanRequestPackageTransactionHome);
		when(loanRequestPackageTransactionHome.create()).thenReturn(loanRequestPackageTransaction);
		LoanRequestPackageCharges loanRequestPackageCharges = mock(LoanRequestPackageCharges.class);
		when(loanRequestPackageTransaction.getLoanRequestPackageCharges(any(LoanAmortizationSchedule.class), any(Date.class), any(Date.class), any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(loanRequestPackageCharges);
		
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(loanAmortizationTransactionHome);
		LoanAmortizationTransaction loanAmortizationTransaction=mock(LoanAmortizationTransaction.class);
		LoanAmortizationSchedule loanamortizationSchedule = mock(LoanAmortizationSchedule.class);
		when(loanamortizationSchedule.getAnnualEffectiveInterestRate()).thenReturn(new BigDecimal("100.00"));
		when(loanamortizationSchedule.getDailyInterestRate()).thenReturn(new BigDecimal("100.00"));
		LoanScenario loanScenario =mock(LoanScenario.class);
		int[] errorCodes = new int[0];
		
		when(loanScenario.getErrorCodes()).thenReturn(errorCodes);
		when(loanamortizationSchedule.getLoanScenario()).thenReturn(loanScenario);
		int[] errcode =new int[0];
		
		when(loanamortizationSchedule.getErrorCodes()).thenReturn(errcode);
		
		
		
		when(loanAmortizationTransaction.execute(nullable(Date.class), nullable(Date.class), any(LoanScenario
				
				.class))).thenReturn(loanamortizationSchedule);
		when(loanAmortizationTransactionHome.create()).thenReturn(loanAmortizationTransaction);
		spy(PortableRemoteObject.class);
		
		LoanAmortizationSchedule loanAmortizationSchedule=new LoanAmortizationSchedule();
		
		loanScenario.setErrorCodes(errorCodes);
		loanAmortizationSchedule.setLoanScenario(loanScenario);
		
		//LoanRequestTransactionHelper loanRequestTransactionHelper = mock(LoanRequestTransactionHelper.class);
		//when(loanRequestTransactionHelper.calculateAdjustedInterestRatePerPeriod(any(BigDecimal.class), anyInt())).thenReturn(new BigDecimal("100.00"));
		
		
		// Given
		LoanDocumentFactory underTest = new LoanDocumentFactory();
		
		// When
		Loan loan = mockLoan();
		Class<LoanDocument>[] documentClasses = new Class[1]; // UTA: default value
		
		AmortizationSchedule amortizationSchedule = new AmortizationSchedule();
		
		documentClasses[0] = (Class<LoanDocument>) amortizationSchedule.getClass();
		
		
		
		LoanDocument document = mock(LoanDocument.class);
		
		LoanDocumentBundle result = underTest.getDocumentBundle(loan, documentClasses);
		
		// Then
		// assertNotNull(result);
	}
*/
	@Test(expected = EJBException.class)
	public void testGetDocumentBundle_4() throws Throwable {
		PowerMockito.whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(anyString())).thenReturn(loanRequestPackageTransactionHome);
		when(loanRequestPackageTransactionHome.create()).thenReturn(loanRequestPackageTransaction);
		LoanRequestPackageCharges loanRequestPackageCharges = mock(LoanRequestPackageCharges.class);
		when(loanRequestPackageTransaction.getLoanRequestPackageCharges(any(LoanAmortizationSchedule.class), any(Date.class), any(Date.class), any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(loanRequestPackageCharges);
		
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(loanAmortizationTransactionHome);
		LoanAmortizationTransaction loanAmortizationTransaction=mock(LoanAmortizationTransaction.class);
		LoanAmortizationSchedule loanamortizationSchedule = mock(LoanAmortizationSchedule.class);
		LoanScenario loanScenario =mock(LoanScenario.class);
		int[] errorCodes = new int[0];
		
		when(loanScenario.getErrorCodes()).thenReturn(errorCodes);
		when(loanamortizationSchedule.getLoanScenario()).thenReturn(loanScenario);
		int[] errcode =new int[0];
		
		when(loanamortizationSchedule.getErrorCodes()).thenReturn(errcode);
		
		
		
		when(loanAmortizationTransaction.execute(nullable(Date.class), nullable(Date.class), any(LoanScenario
				
				.class))).thenReturn(loanamortizationSchedule);
		when(loanAmortizationTransactionHome.create()).thenReturn(loanAmortizationTransaction);
		spy(PortableRemoteObject.class);
		
		LoanAmortizationSchedule loanAmortizationSchedule=new LoanAmortizationSchedule();
		
		
		
		loanScenario.setErrorCodes(errorCodes);
		loanAmortizationSchedule.setLoanScenario(loanScenario);
		
		// Given
		LoanDocumentFactory underTest = new LoanDocumentFactory();
		
		// When
		Loan loan = mockLoan();
		Class<LoanDocument>[] documentClasses = new Class[1]; // UTA: default value
		
		LoanDocument instructions = new Instructions();
		
		documentClasses[0] = (Class<LoanDocument>) instructions.getClass();
		
		
		LoanDocument document = mock(LoanDocument.class);
		
		LoanDocumentBundle result = underTest.getDocumentBundle(loan, documentClasses);
		
		// Then
		// assertNotNull(result);
	}
	@Test(expected = EJBException.class)
	public void testGetDocumentBundle_5() throws Throwable {
		PowerMockito.whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(anyString())).thenReturn(loanRequestPackageTransactionHome);
		when(loanRequestPackageTransactionHome.create()).thenReturn(loanRequestPackageTransaction);
		LoanRequestPackageCharges loanRequestPackageCharges = mock(LoanRequestPackageCharges.class);
		when(loanRequestPackageTransaction.getLoanRequestPackageCharges(any(LoanAmortizationSchedule.class), any(Date.class), any(Date.class), any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(loanRequestPackageCharges);
		
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(loanAmortizationTransactionHome);
		LoanAmortizationTransaction loanAmortizationTransaction=mock(LoanAmortizationTransaction.class);
		LoanAmortizationSchedule loanamortizationSchedule = mock(LoanAmortizationSchedule.class);
		LoanScenario loanScenario =mock(LoanScenario.class);
		int[] errorCodes = new int[0];
		
		when(loanScenario.getErrorCodes()).thenReturn(errorCodes);
		when(loanamortizationSchedule.getLoanScenario()).thenReturn(loanScenario);
		int[] errcode =new int[0];
		
		when(loanamortizationSchedule.getErrorCodes()).thenReturn(errcode);
		
		
		
		when(loanAmortizationTransaction.execute(nullable(Date.class), nullable(Date.class), any(LoanScenario
				
				.class))).thenReturn(loanamortizationSchedule);
		when(loanAmortizationTransactionHome.create()).thenReturn(loanAmortizationTransaction);
		spy(PortableRemoteObject.class);
		
		LoanAmortizationSchedule loanAmortizationSchedule=new LoanAmortizationSchedule();
		
		
		
		loanScenario.setErrorCodes(errorCodes);
		loanAmortizationSchedule.setLoanScenario(loanScenario);
		
		// Given
		LoanDocumentFactory underTest = new LoanDocumentFactory();
		
		// When
		Loan loan = mockLoan();
		Class<LoanDocument>[] documentClasses = new Class[1]; // UTA: default value
		
		LoanDocument loanForm = new LoanForm();
		
		documentClasses[0] = (Class<LoanDocument>) loanForm.getClass();
		
		
		LoanDocument document = mock(LoanDocument.class);
		
		LoanDocumentBundle result = underTest.getDocumentBundle(loan, documentClasses);
		
		// Then
		// assertNotNull(result);
	}
	@Test(expected = EJBException.class)
	public void testGetDocumentBundle_1() throws Throwable {
		PowerMockito.whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(anyString())).thenReturn(loanRequestPackageTransactionHome);
		when(loanRequestPackageTransactionHome.create()).thenReturn(loanRequestPackageTransaction);
		LoanRequestPackageCharges loanRequestPackageCharges = mock(LoanRequestPackageCharges.class);
		when(loanRequestPackageTransaction.getLoanRequestPackageCharges(any(LoanAmortizationSchedule.class), any(Date.class), any(Date.class), any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(loanRequestPackageCharges);
		
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(loanAmortizationTransactionHome);
		LoanAmortizationTransaction loanAmortizationTransaction=mock(LoanAmortizationTransaction.class);
		LoanAmortizationSchedule loanamortizationSchedule = mock(LoanAmortizationSchedule.class);
		LoanScenario loanScenario =mock(LoanScenario.class);
		int[] errorCodes = new int[2];
		errorCodes[0] = 5501;
		errorCodes[1]=6608;
		
		when(loanScenario.getErrorCodes()).thenReturn(errorCodes);
		when(loanamortizationSchedule.getLoanScenario()).thenReturn(loanScenario);
		int[] errcode =new int[2];
		errcode[0] = 5545;
		errcode[1] = 5546;
		
		when(loanamortizationSchedule.getErrorCodes()).thenReturn(errcode);
		
		
		
		when(loanAmortizationTransaction.execute(nullable(Date.class), nullable(Date.class), any(LoanScenario
				
				.class))).thenReturn(loanamortizationSchedule);
		when(loanAmortizationTransactionHome.create()).thenReturn(loanAmortizationTransaction);
		spy(PortableRemoteObject.class);
		
		LoanAmortizationSchedule loanAmortizationSchedule=new LoanAmortizationSchedule();
		
		
		
		loanScenario.setErrorCodes(errorCodes);
		loanAmortizationSchedule.setLoanScenario(loanScenario);
		
		// Given
		LoanDocumentFactory underTest = new LoanDocumentFactory();
		
		// When
		Loan loan = mockLoan();
		Class<LoanDocument>[] documentClasses = new Class[0];
		
		
		LoanDocument document = mock(LoanDocument.class);
		
		LoanDocumentBundle result = underTest.getDocumentBundle(loan, documentClasses);
		
		// Then
		// assertNotNull(result);
	}
	
	
	/**
	 * Parasoft Jtest UTA: Test for getDocumentBundle(Loan, Class[])
	 *
	 * @see com.manulife.pension.service.loan.LoanDocumentFactory#getDocumentBundle(Loan, Class[])
	 * @author patelpo
	 */


	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getAcceptedParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);

		Timestamp getCreatedResult = mockTimestamp();
		when(getAcceptedParameterResult.getCreated()).thenReturn(getCreatedResult);

		BigDecimal getInterestRateResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getInterestRate()).thenReturn(getInterestRateResult);

		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);

		BigDecimal getPaymentAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult);

		String getPaymentFrequencyResult = ""; // UTA: default value
		when(getAcceptedParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult);

		String toStringResult2 = ""; // UTA: default value
		when(getAcceptedParameterResult.toString()).thenReturn(toStringResult2);
		return getAcceptedParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp2() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult3 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult3);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter2() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult2 = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult2);

		Timestamp getCreatedResult2 = mockTimestamp2();
		when(getCurrentLoanParameterResult.getCreated()).thenReturn(getCreatedResult2);

		BigDecimal getInterestRateResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getInterestRate()).thenReturn(getInterestRateResult2);

		BigDecimal getLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult2);

		BigDecimal getPaymentAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult2);

		String getPaymentFrequencyResult2 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult2);

		String toStringResult4 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.toString()).thenReturn(toStringResult4);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		long getTimeResult = 0L; // UTA: default value
		when(getEffectiveDateResult.getTime()).thenReturn(getTimeResult);

		String toStringResult5 = ""; // UTA: default value
		when(getEffectiveDateResult.toString()).thenReturn(toStringResult5);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp3() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult6 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult6);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp4() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult7 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult7);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Fee
	 */
	private static Fee mockFee() throws Throwable {
		Fee getFeeResult = mock(Fee.class);
		Timestamp getCreatedResult3 = mockTimestamp3();
		when(getFeeResult.getCreated()).thenReturn(getCreatedResult3);

		Timestamp getLastUpdatedResult = mockTimestamp4();
		when(getFeeResult.getLastUpdated()).thenReturn(getLastUpdatedResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getFeeResult.getSubmissionId()).thenReturn(getSubmissionIdResult);

		BigDecimal getValueResult = BigDecimal.ZERO; // UTA: default value
		when(getFeeResult.getValue()).thenReturn(getValueResult);
		return getFeeResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate2() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		long getTimeResult2 = 0L; // UTA: default value
		when(getFirstPayrollDateResult.getTime()).thenReturn(getTimeResult2);

		String toStringResult8 = ""; // UTA: default value
		when(getFirstPayrollDateResult.toString()).thenReturn(toStringResult8);
		return getFirstPayrollDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp5() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		String toStringResult9 = ""; // UTA: default value
		when(getLastUpdatedResult2.toString()).thenReturn(toStringResult9);
		return getLastUpdatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult = mock(LoanParticipantData.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCity()).thenReturn(getCityResult);

		String getCountryResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCountry()).thenReturn(getCountryResult);

		String getEmployeeNumberResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getEmployeeNumber()).thenReturn(getEmployeeNumberResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getFirstName()).thenReturn(getFirstNameResult);

		String getLastNameResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getLastName()).thenReturn(getLastNameResult);

		String getMiddleInitialResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getMiddleInitial()).thenReturn(getMiddleInitialResult);

		String getSsnResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getSsn()).thenReturn(getSsnResult);

		String getStateCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getZipCode()).thenReturn(getZipCodeResult);

		String toStringResult10 = ""; // UTA: default value
		when(getLoanParticipantDataResult.toString()).thenReturn(toStringResult10);
		return getLoanParticipantDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		BigDecimal getContractLoanExpenseMarginPctResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanExpenseMarginPct())
				.thenReturn(getContractLoanExpenseMarginPctResult2);

		BigDecimal getContractLoanMonthlyFlatFeeResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult2);

		String getContractNameResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getContractName()).thenReturn(getContractNameResult);

		String getManulifeCompanyIdResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getManulifeCompanyId()).thenReturn(getManulifeCompanyIdResult);

		BigDecimal getMaximumLoanPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult);

		String getPlanLegalNameResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getPlanLegalName()).thenReturn(getPlanLegalNameResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);

		String toStringResult11 = ""; // UTA: default value
		when(getLoanPlanDataResult.toString()).thenReturn(toStringResult11);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp6() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult12 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult12);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp7() throws Throwable {
		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		String toStringResult13 = ""; // UTA: default value
		when(getLastUpdatedResult3.toString()).thenReturn(toStringResult13);
		return getLastUpdatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress() throws Throwable {
		DistributionAddress getAddressResult = mock(DistributionAddress.class);
		String getAddressLine1Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine1()).thenReturn(getAddressLine1Result2);

		String getAddressLine2Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine2()).thenReturn(getAddressLine2Result2);

		String getCityResult2 = ""; // UTA: default value
		when(getAddressResult.getCity()).thenReturn(getCityResult2);

		String getCountryCodeResult = ""; // UTA: default value
		when(getAddressResult.getCountryCode()).thenReturn(getCountryCodeResult);

		Timestamp getCreatedResult4 = mockTimestamp6();
		when(getAddressResult.getCreated()).thenReturn(getCreatedResult4);

		Timestamp getLastUpdatedResult3 = mockTimestamp7();
		when(getAddressResult.getLastUpdated()).thenReturn(getLastUpdatedResult3);

		String getStateCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getStateCode()).thenReturn(getStateCodeResult2);

		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getAddressResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);

		String getZipCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getZipCode()).thenReturn(getZipCodeResult2);
		return getAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mockDistributionAddress();
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		doReturn(getPayeesResult).when(getRecipientResult).getPayees();

		String toStringResult14 = ""; // UTA: default value
		when(getRecipientResult.toString()).thenReturn(toStringResult14);
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(loan.getContractId()).thenReturn(getContractIdResult);

		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);

		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);

		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter2();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		Fee getFeeResult = mockFee();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate2();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Timestamp getLastUpdatedResult2 = mockTimestamp5();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData();
		when(loan.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData();
		when(loan.getLoanPlanData()).thenReturn(getLoanPlanDataResult);

		String getLoanTypeResult = ""; // UTA: default value
		when(loan.getLoanType()).thenReturn(getLoanTypeResult);

		BigDecimal getMaximumLoanPercentageResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult2);

		List<LoanMoneyType> getMoneyTypesWithAccountBalanceResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesWithAccountBalanceResult).when(loan).getMoneyTypesWithAccountBalance();

		LoanRecipient getRecipientResult = mockLoanRecipient();
		when(loan.getRecipient()).thenReturn(getRecipientResult);

		String getSpousalConsentReqdIndResult2 = ""; // UTA: default value
		when(loan.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult2);

		String getStatusResult = ""; // UTA: default value
		when(loan.getStatus()).thenReturn(getStatusResult);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(loan.getSubmissionId()).thenReturn(getSubmissionIdResult3);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		String toStringResult15 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult15);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Test for getHome(Class)
	 *
	 * @see com.manulife.pension.service.loan.LoanDocumentFactory#getHome(Class)
	 * @author patelpo
	 */
	@Test
	public void testGetHome() throws Throwable {
		
		spy(Cache.class);

		EJBHome getFromCacheResult = mock(EJBHome.class); // UTA: default value
		doReturn(getFromCacheResult).when(Cache.class);
		Cache.getFromCache(nullable(String.class), nullable(Object.class));

		// When
		Class homeClass = Class.forName("java.lang.Object"); // UTA: default value
		EJBHome result = LoanDocumentFactory.getHome(homeClass);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getLoanAmortizationSchedule(Loan)
	 *
	 * @see com.manulife.pension.service.loan.LoanDocumentFactory#getLoanAmortizationSchedule(Loan)
	 * @author patelpo
	 */
	@Test(expected = EJBException.class)
	public void testGetLoanAmortizationSchedule() throws Throwable {
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(loanAmortizationTransactionHome);
		LoanAmortizationTransaction loanAmortizationTransaction=mock(LoanAmortizationTransaction.class);
		LoanAmortizationSchedule amortizationSchedule = mock(LoanAmortizationSchedule.class);
		LoanScenario loanScenario =mock(LoanScenario.class);
		int[] errorCodes = new int[6];
		errorCodes[0] = 5501;
		errorCodes[1] = 5502;
		errorCodes[2] = 5503;
		errorCodes[3] = 5504;
		errorCodes[4] = 5505;
		errorCodes[5] = 5544;
		
		when(loanScenario.getErrorCodes()).thenReturn(errorCodes);
		when(amortizationSchedule.getLoanScenario()).thenReturn(loanScenario);
		int[] errcode =new int[2];
		errcode[0] = 5545;
		errcode[1] = 5546;
		
		when(amortizationSchedule.getErrorCodes()).thenReturn(errcode);
		
		
		
		when(loanAmortizationTransaction.execute(nullable(Date.class), nullable(Date.class), any(LoanScenario

.class))).thenReturn(amortizationSchedule);
		when(loanAmortizationTransactionHome.create()).thenReturn(loanAmortizationTransaction);
		// When
		Loan loan = mockLoan2();
		LoanAmortizationSchedule result = LoanDocumentFactory.getLoanAmortizationSchedule(loan);

		// Then
		// assertNotNull(result);
	}
	
	@Test
	public void testGetLoanAmortizationSchedule_1() throws Throwable {
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(loanAmortizationTransactionHome);
		LoanAmortizationTransaction loanAmortizationTransaction=mock(LoanAmortizationTransaction.class);
		LoanAmortizationSchedule amortizationSchedule = mock(LoanAmortizationSchedule.class);
		LoanScenario loanScenario =mock(LoanScenario.class);
		int[] errorCodes = new int[1];
		when(loanScenario.getErrorCodes()).thenReturn(errorCodes);
		when(amortizationSchedule.getLoanScenario()).thenReturn(loanScenario);
		when(loanAmortizationTransaction.execute(nullable(Date.class), nullable(Date.class), any(LoanScenario
				
				.class))).thenReturn(amortizationSchedule);
		when(loanAmortizationTransactionHome.create()).thenReturn(loanAmortizationTransaction);
		// When
		Loan loan = mockLoan2_1();
		LoanAmortizationSchedule result = LoanDocumentFactory.getLoanAmortizationSchedule(loan);
		
		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetLoanAmortizationSchedule_2() throws Throwable {
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(loanAmortizationTransactionHome);
		LoanAmortizationTransaction loanAmortizationTransaction=mock(LoanAmortizationTransaction.class);
		LoanAmortizationSchedule amortizationSchedule = mock(LoanAmortizationSchedule.class);
		LoanScenario loanScenario =mock(LoanScenario.class);
		int[] errorCodes = new int[1];
		when(loanScenario.getErrorCodes()).thenReturn(errorCodes);
		when(amortizationSchedule.getLoanScenario()).thenReturn(loanScenario);
		when(loanAmortizationTransaction.execute(nullable(Date.class), nullable(Date.class), any(LoanScenario
				
				.class))).thenReturn(amortizationSchedule);
		when(loanAmortizationTransactionHome.create()).thenReturn(loanAmortizationTransaction);
		// When
		Loan loan = mockLoan2_2();
		LoanAmortizationSchedule result = LoanDocumentFactory.getLoanAmortizationSchedule(loan);
		
		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetLoanAmortizationSchedule_3() throws Throwable {
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(loanAmortizationTransactionHome);
		LoanAmortizationTransaction loanAmortizationTransaction=mock(LoanAmortizationTransaction.class);
		LoanAmortizationSchedule amortizationSchedule = mock(LoanAmortizationSchedule.class);
		LoanScenario loanScenario =mock(LoanScenario.class);
		int[] errorCodes = new int[1];
		when(loanScenario.getErrorCodes()).thenReturn(errorCodes);
		when(amortizationSchedule.getLoanScenario()).thenReturn(loanScenario);
		when(loanAmortizationTransaction.execute(nullable(Date.class), nullable(Date.class), any(LoanScenario
				
				.class))).thenReturn(amortizationSchedule);
		when(loanAmortizationTransactionHome.create()).thenReturn(loanAmortizationTransaction);
		// When
		Loan loan = mockLoan2_3();
		LoanAmortizationSchedule result = LoanDocumentFactory.getLoanAmortizationSchedule(loan);
		
		// Then
		// assertNotNull(result);
	}
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter3() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);

		BigDecimal getInterestRateResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getInterestRate()).thenReturn(getInterestRateResult);

		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);

		BigDecimal getPaymentAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult);

		String getPaymentFrequencyResult = "W"; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult);
		return getCurrentLoanParameterResult;
	}
	private static LoanParameter mockLoanParameter3_1() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);
		
		BigDecimal getInterestRateResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getInterestRate()).thenReturn(getInterestRateResult);
		
		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);
		
		BigDecimal getPaymentAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult);
		
		String getPaymentFrequencyResult = "B"; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult);
		return getCurrentLoanParameterResult;
	}
	private static LoanParameter mockLoanParameter3_2() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);
		
		BigDecimal getInterestRateResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getInterestRate()).thenReturn(getInterestRateResult);
		
		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);
		
		BigDecimal getPaymentAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult);
		
		String getPaymentFrequencyResult = "H"; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult);
		return getCurrentLoanParameterResult;
	}
	private static LoanParameter mockLoanParameter3_3() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);
		
		BigDecimal getInterestRateResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getInterestRate()).thenReturn(getInterestRateResult);
		
		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);
		
		BigDecimal getPaymentAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult);
		
		String getPaymentFrequencyResult = "M"; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan2() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getCurrentLoanParameterResult = mockLoanParameter3();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		Date getEffectiveDateResult = mock(Date.class);
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		Date getFirstPayrollDateResult = mock(Date.class);
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);
		return loan;
	}
	private static Loan mockLoan2_1() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getCurrentLoanParameterResult = mockLoanParameter3_1();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);
		
		Date getEffectiveDateResult = mock(Date.class);
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);
		
		Date getFirstPayrollDateResult = mock(Date.class);
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);
		return loan;
	}
	private static Loan mockLoan2_2() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getCurrentLoanParameterResult = mockLoanParameter3_2();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);
		
		Date getEffectiveDateResult = mock(Date.class);
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);
		
		Date getFirstPayrollDateResult = mock(Date.class);
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);
		return loan;
	}
	private static Loan mockLoan2_3() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getCurrentLoanParameterResult = mockLoanParameter3_3();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);
		
		Date getEffectiveDateResult = mock(Date.class);
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);
		
		Date getFirstPayrollDateResult = mock(Date.class);
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Test for isDraftDocument(Loan)
	 *
	 * @see com.manulife.pension.service.loan.LoanDocumentFactory#isDraftDocument(Loan)
	 * @author patelpo
	 */
	@Test
	public void testIsDraftDocument() throws Throwable {
		// Given
		LoanDocumentFactory underTest = new LoanDocumentFactory();

		// When
		Loan loan = mockLoan3();
		boolean result = underTest.isDraftDocument(loan);

		// Then
		// assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan3() throws Throwable {
		Loan loan = mock(Loan.class);
		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);

		String getStatusResult = ""; // UTA: default value
		when(loan.getStatus()).thenReturn(getStatusResult);
		return loan;
	}
	@Before
    public void tearDown() throws Exception {

                    reset(newInitialContextResult);
                    reset(loanRequestPackageTransaction);
                    reset(loanRequestPackageTransactionHome);
                    reset(loanAmortizationTransactionHome);

    }
	
}