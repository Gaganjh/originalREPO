 /**
 * 
 */
package com.manulife.pension.delegate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.nullable;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.spi.NamingManager;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.manulife.pension.content.view.ContentText;
import com.manulife.pension.service.loan.LoanDocumentService;
import com.manulife.pension.service.loan.LoanDocumentServiceHome;
import com.manulife.pension.service.loan.LoanDocumentServiceLocalHome;
import com.manulife.pension.service.loan.LoanDocumentServiceUtil;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.util.JdbcHelper;
import com.sun.naming.internal.ResourceManager;

/**
 * Parasoft Jtest UTA: Test class for LoanDocumentServiceDelegate
 *
 * @see com.manulife.pension.delegate.LoanDocumentServiceDelegate
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, LoanDocumentServiceUtil.class
	,NamingManager.class,ResourceManager.class})
@RunWith(PowerMockRunner.class)
public class LoanDocumentServiceDelegateTest {
	Logger getLoggerResult = mock(Logger.class);
	LoanDocumentService loanDocumentService = mock(LoanDocumentService.class);
	LoanDocumentServiceLocalHome loanDocumentServiceLocalHome = mock(LoanDocumentServiceLocalHome.class); 
	Context context=mock(Context.class);
	@Before
	public void setUp() throws Exception {
		spy(LoanDocumentServiceUtil.class);
		spy(Logger.class);

		doReturn(getLoggerResult).when(Logger.class, "getLogger", Mockito.any(Class.class));
		when(getLoggerResult.isDebugEnabled()).thenReturn(true);

		doReturn(loanDocumentServiceLocalHome).when(LoanDocumentServiceUtil.class, "getLocalHome");
		when(loanDocumentServiceLocalHome.create()).thenReturn(loanDocumentService);
		
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
	} 

	/**
	 * Parasoft Jtest UTA: Test for getAmortizationScheduleHtml(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanDocumentServiceDelegate#getAmortizationScheduleHtml(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetAmortizationScheduleHtml() throws Throwable {
		// Given
		LoanDocumentServiceDelegate underTest = LoanDocumentServiceDelegate.getInstance();

		// When
		Integer userProfileId = 0;
		Integer contractId = 0;
		Integer submissionId = 0;
		String result = underTest.getAmortizationScheduleHtml(userProfileId, contractId, submissionId);

		// Then
		//assertNotNull(result);
		//assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getAmortizationScheduleHtml(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanDocumentServiceDelegate#getAmortizationScheduleHtml(Loan)
	 * @author patelpo
	 */
	@Test
	public void testGetAmortizationScheduleHtml2() throws Throwable {
		// Given
		LoanDocumentServiceDelegate underTest = LoanDocumentServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		String result = underTest.getAmortizationScheduleHtml(loan);

		// Then
		//assertNotNull(result);
		//assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getContentTextById(int)
	 *
	 * @see com.manulife.pension.delegate.LoanDocumentServiceDelegate#getContentTextById(int)
	 * @author patelpo
	 */
	@Test
	public void testGetContentTextById() throws Throwable {
		// Given
		LoanDocumentServiceDelegate underTest = LoanDocumentServiceDelegate.getInstance();

		// When
		int contentId = 0;
		ContentText result = underTest.getContentTextById(contentId);

		// Then
		//assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getContentTextByKey(int)
	 *
	 * @see com.manulife.pension.delegate.LoanDocumentServiceDelegate#getContentTextByKey(int)
	 * @author patelpo
	 */
	@Test
	public void testGetContentTextByKey() throws Throwable {
		// Given
		LoanDocumentServiceDelegate underTest = LoanDocumentServiceDelegate.getInstance();

		// When
		int contentKey = 0;
		ContentText result = underTest.getContentTextByKey(contentKey);

		// Then
		//assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getInstance()
	 *
	 * @see com.manulife.pension.delegate.LoanDocumentServiceDelegate#getInstance()
	 * @author patelpo
	 */
	@Test
	public void testGetInstance() throws Throwable {
		// When
		LoanDocumentServiceDelegate result = LoanDocumentServiceDelegate.getInstance();

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getLoanDocuments(Integer, Integer, Integer, boolean)
	 *
	 * @see com.manulife.pension.delegate.LoanDocumentServiceDelegate#getLoanDocuments(Integer, Integer, Integer, boolean)
	 * @author patelpo
	 */
	@Test
	public void testGetLoanDocuments() throws Throwable {
		byte[] byteArray = new byte[] { (byte) 0xe0, 0x4f, (byte) 0xd0, 0x20, (byte) 0xea, 0x3a, 0x69, 0x10,
				(byte) 0xa2, (byte) 0xd8, 0x08, 0x00, 0x2b, 0x30, 0x30, (byte) 0x9d };
		when(loanDocumentService.getLoanDocuments(nullable(Integer.class), nullable(Integer.class), nullable(Integer.class), anyBoolean())).thenReturn(byteArray);
		// Given
		LoanDocumentServiceDelegate underTest = LoanDocumentServiceDelegate.getInstance();

		// When
		Integer userProfileId = 0;
		Integer contractId = 0;
		Integer submissionId = 0;
		boolean useEffectiveDateFromDB = false;
		byte[] result = underTest.getLoanDocuments(userProfileId, contractId, submissionId, useEffectiveDateFromDB);

		// Then
		assertNotNull(result);
		assertEquals(16, result.length);
	}

	/**
	 * Parasoft Jtest UTA: Test for getLoanPackage(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanDocumentServiceDelegate#getLoanPackage(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetLoanPackage() throws Throwable {
		// Given
		LoanDocumentServiceDelegate underTest = LoanDocumentServiceDelegate.getInstance();

		// When
		Integer userProfileId = 0;
		Integer contractId = 0;
		Integer submissionId = 0;
		byte[] result = underTest.getLoanPackage(userProfileId, contractId, submissionId);

		// Then
		//assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getPhysicalHome()
	 *
	 * @see com.manulife.pension.delegate.LoanDocumentServiceDelegate#getPhysicalHome()
	 * @author patelpo
	 */
	@Test
	public void testGetPhysicalHome() throws Throwable {

		// When
		LoanDocumentServiceHome result = LoanDocumentServiceDelegate.getPhysicalHome();

		// Then
		//assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getPromissoryNoteAndIrrevocablePledgeHtml(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanDocumentServiceDelegate#getPromissoryNoteAndIrrevocablePledgeHtml(Loan)
	 * @author patelpo
	 */
	@Test
	public void testGetPromissoryNoteAndIrrevocablePledgeHtml() throws Throwable {
		// Given
		LoanDocumentServiceDelegate underTest = LoanDocumentServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		String result = underTest.getPromissoryNoteAndIrrevocablePledgeHtml(loan);

		// Then
		//assertNotNull(result);
		//assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getPromissoryNoteAndIrrevocablePledgeHtml(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanDocumentServiceDelegate#getPromissoryNoteAndIrrevocablePledgeHtml(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetPromissoryNoteAndIrrevocablePledgeHtml2() throws Throwable {
	
		// Given
		LoanDocumentServiceDelegate underTest = LoanDocumentServiceDelegate.getInstance();

		// When
		Integer userProfileId = 10;
		Integer contractId = 10;
		Integer submissionId = 10;
		String result = underTest.getPromissoryNoteAndIrrevocablePledgeHtml(userProfileId, contractId, submissionId);

		// Then
		//assertNotNull(result);
		//assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getTruthInLendingNoticeHtml(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanDocumentServiceDelegate#getTruthInLendingNoticeHtml(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetTruthInLendingNoticeHtml() throws Throwable {
		// Given
		LoanDocumentServiceDelegate underTest = LoanDocumentServiceDelegate.getInstance();

		// When
		Integer userProfileId = 0;
		Integer contractId = 0;
		Integer submissionId = 0;
		String result = underTest.getTruthInLendingNoticeHtml(userProfileId, contractId, submissionId);

		// Then
		//assertNotNull(result);
		//assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getTruthInLendingNoticeHtml(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanDocumentServiceDelegate#getTruthInLendingNoticeHtml(Loan)
	 * @author patelpo
	 */
	@Test
	public void testGetTruthInLendingNoticeHtml2() throws Throwable {
		// Given
		LoanDocumentServiceDelegate underTest = LoanDocumentServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		String result = underTest.getTruthInLendingNoticeHtml(loan);

		// Then
		//assertNotNull(result);
		//assertEquals("", result);
	}
}