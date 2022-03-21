/**
 * 
 */
package com.manulife.pension.service.loan.dao;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.intware.dao.jdbc.SelectBeanQueryHandler;
import com.intware.dao.jdbc.SelectMultiFieldMultiRowQueryHandler;
import com.intware.dao.jdbc.SelectMultiFieldQueryHandler;
import com.intware.dao.jdbc.SelectSingleOrNoValueQueryHandler;
import com.intware.dao.jdbc.SelectSingleValueQueryHandler;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.loan.valueobject.LoanEventData;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.loan.valueobject.OutstandingLoan;
import com.manulife.pension.util.JdbcHelper;
import com.manulife.pension.util.config.ConfigurationFactory;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for LoanSupportDao
 *
 * @see com.manulife.pension.service.loan.dao.LoanSupportDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, LoanSupportDao.class, LoanDefaults.class,
		ConfigurationFactory.class })
@RunWith(PowerMockRunner.class)
public class LoanSupportDaoTest {
	Logger getLoggerResult = mock(Logger.class); // UTA: default value
	DataSource getCachedDataSourceResult = mock(DataSource.class);
	Connection getConnectionResult = mock(Connection.class);
	PreparedStatement prepareStatementResult = mock(PreparedStatement.class);
	Statement statementResult = mock(Statement.class);
	CallableStatement callableStatementResult = mock(CallableStatement.class);
	ResultSet executeQueryResult = mock(ResultSet.class);

	@Before
	public void setUp() throws Exception {
		spy(JdbcHelper.class);

		spy(Logger.class);

		doReturn(getLoggerResult).when(Logger.class, "getLogger", Mockito.any(Class.class));
		when(getLoggerResult.isDebugEnabled()).thenReturn(true);

		when(statementResult.executeQuery(anyString())).thenReturn(executeQueryResult);
		when(prepareStatementResult.executeQuery()).thenReturn(executeQueryResult);
		when(callableStatementResult.executeQuery(anyString())).thenReturn(executeQueryResult);
		when(getConnectionResult.prepareStatement(anyString())).thenReturn(prepareStatementResult);
		when(getConnectionResult.createStatement()).thenReturn(statementResult);
		when(getConnectionResult.prepareCall(anyString())).thenReturn(callableStatementResult);
		when(getCachedDataSourceResult.getConnection()).thenReturn(getConnectionResult);
		doReturn(getCachedDataSourceResult).when(JdbcHelper.class, "getCachedDataSource", anyString());

	}

	/**
	 * Parasoft Jtest UTA: Test for getContractName(int)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanSupportDao#getContractName(int)
	 * @author patelpo
	 */
	@Test
	public void testGetContractName() throws Throwable {
		String contractName = new String();
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(
				SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenReturn(contractName);

		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		int contractId = 0; // UTA: default value
		String result = underTest.getContractName(contractId);

		// Then
		 assertNotNull(result);
		 assertEquals("", result);
	}

	@Test(expected = LoanDaoException.class)
	public void testGetContractName_Exception() throws Throwable {
		String contractName = new String();
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(
				SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenThrow(new DAOException());

		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		int contractId = 0; // UTA: default value
		String result = underTest.getContractName(contractId);

		// Then
		 assertNotNull(result);
		 assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getLoanDataForEventMessages(int)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanSupportDao#getLoanDataForEventMessages(int)
	 * @author patelpo
	 */
	@Test
	public void testGetLoanDataForEventMessages() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		int submissionId = 0; // UTA: default value
		LoanEventData result = underTest.getLoanDataForEventMessages(submissionId);

		// Then
		 assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getLoanPlanData(Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanSupportDao#getLoanPlanData(Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetLoanPlanData() throws Throwable {
		BigDecimal[] plandatafees = new BigDecimal[4];
		
		SelectMultiFieldQueryHandler newSelectMultiFieldQueryHandlerResult = mock(SelectMultiFieldQueryHandler.class); // UTA: default value
		whenNew(SelectMultiFieldQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectMultiFieldQueryHandlerResult);
		when(newSelectMultiFieldQueryHandlerResult.select(any(Object[].class))).thenReturn(plandatafees);

		LoanPlanData loanPlanData = new LoanPlanData();
		loanPlanData.setThirdPartyAdminId(0);
		loanPlanData.setSpousalConsentReqdInd("U");
		loanPlanData.setPayrollFrequency("U");
		loanPlanData.setMaxNumberOfOutstandingLoans(0);
		loanPlanData.setLoanInterestRateOverPrime(BigDecimal.ONE);
		
		SelectBeanQueryHandler newSelectBeanQueryHandlerResult = mock(SelectBeanQueryHandler.class); // UTA: default value
		whenNew(SelectBeanQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanQueryHandlerResult);
		when(newSelectBeanQueryHandlerResult.select(any(Object[].class))).thenReturn(loanPlanData);
		

		Integer countMoneyTypeAllowedForLoan = new Integer(10);
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(
				SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class)))
				.thenReturn(countMoneyTypeAllowedForLoan);
		spy(ConfigurationFactory.class);

		Configuration getConfigurationResult = mock(Configuration.class); // UTA: default value
		doReturn(getConfigurationResult).when(ConfigurationFactory.class);
		ConfigurationFactory.getConfiguration();

		spy(LoanDefaults.class);

		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		Integer contractId = 0; // UTA: default value
		LoanPlanData result = underTest.getLoanPlanData(contractId);

		// Then
		 assertNotNull(result);
	}
	@Test
	public void testGetLoanPlanData1() throws Throwable {
		BigDecimal[] plandatafees = new BigDecimal[4];
		
		SelectMultiFieldQueryHandler newSelectMultiFieldQueryHandlerResult = mock(SelectMultiFieldQueryHandler.class); // UTA: default value
		whenNew(SelectMultiFieldQueryHandler.class).withAnyArguments()
		.thenReturn(newSelectMultiFieldQueryHandlerResult);
		when(newSelectMultiFieldQueryHandlerResult.select(any(Object[].class))).thenReturn(plandatafees);
		
		LoanPlanData loanPlanData = new LoanPlanData();
		loanPlanData.setThirdPartyAdminId(0);
		loanPlanData.setSpousalConsentReqdInd("U");
		loanPlanData.setPayrollFrequency("U");
		loanPlanData.setMaxNumberOfOutstandingLoans(0);
		
		SelectBeanQueryHandler newSelectBeanQueryHandlerResult = mock(SelectBeanQueryHandler.class); // UTA: default value
		whenNew(SelectBeanQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanQueryHandlerResult);
		when(newSelectBeanQueryHandlerResult.select(any(Object[].class))).thenReturn(loanPlanData);
		
		
		Integer countMoneyTypeAllowedForLoan = new Integer(10);
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(
				SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
		.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class)))
		.thenReturn(countMoneyTypeAllowedForLoan);
		spy(ConfigurationFactory.class);
		
		Configuration getConfigurationResult = mock(Configuration.class); // UTA: default value
		doReturn(getConfigurationResult).when(ConfigurationFactory.class);
		ConfigurationFactory.getConfiguration();
		
		spy(LoanDefaults.class);
		
		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);
		
		// When
		Integer contractId = 0; // UTA: default value
		LoanPlanData result = underTest.getLoanPlanData(contractId);
		
		// Then
		assertNotNull(result);
	}
	@Test(expected = LoanDaoException.class)
	public void testGetLoanPlanData_Exception() throws Throwable {
		BigDecimal[] plandatafees = new BigDecimal[4];
		
		SelectMultiFieldQueryHandler newSelectMultiFieldQueryHandlerResult = mock(SelectMultiFieldQueryHandler.class); // UTA: default value
		whenNew(SelectMultiFieldQueryHandler.class).withAnyArguments()
		.thenReturn(newSelectMultiFieldQueryHandlerResult);
		when(newSelectMultiFieldQueryHandlerResult.select(any(Object[].class))).thenReturn(plandatafees);
		
		LoanPlanData loanPlanData = new LoanPlanData();
		SelectBeanQueryHandler newSelectBeanQueryHandlerResult = mock(SelectBeanQueryHandler.class); // UTA: default value
		whenNew(SelectBeanQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanQueryHandlerResult);
		when(newSelectBeanQueryHandlerResult.select(any(Object[].class))).thenThrow(new DAOException(""));
		
		Integer countMoneyTypeAllowedForLoan = new Integer(10);
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(
				SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
		.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class)))
		.thenReturn(countMoneyTypeAllowedForLoan);
		spy(ConfigurationFactory.class);
		
		Configuration getConfigurationResult = mock(Configuration.class); // UTA: default value
		doReturn(getConfigurationResult).when(ConfigurationFactory.class);
		ConfigurationFactory.getConfiguration();
		
		spy(LoanDefaults.class);
		
		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);
		
		// When
		Integer contractId = 0; // UTA: default value
		LoanPlanData result = underTest.getLoanPlanData(contractId);
		
		// Then
		assertNotNull(result);
	}

	
	
	/**
	 * Parasoft Jtest UTA: Test for getNumberOfOutstandingLoans(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanSupportDao#getNumberOfOutstandingLoans(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetNumberOfOutstandingLoans() throws Throwable {
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(
				SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenReturn(1);

		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		Integer contractId = 0; // UTA: default value
		Integer participantId = 0; // UTA: default value
		Integer result = underTest.getNumberOfOutstandingLoans(contractId, participantId);

		// Then
		 assertNotNull(result);
		 assertEquals(1, result.intValue());
	}

	@Test(expected = LoanDaoException.class)
	public void testGetNumberOfOutstandingLoans_Exception() throws Throwable {
		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		Integer contractId = 0; // UTA: default value
		Integer participantId = 0; // UTA: default value
		Integer result = underTest.getNumberOfOutstandingLoans(contractId, participantId);

		// Then
		 assertNotNull(result);
		 assertEquals(0, result.intValue());
	}

	
	/**
	 * Parasoft Jtest UTA: Test for getOutstandingLoan(Integer, Long, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanSupportDao#getOutstandingLoan(Integer, Long, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetOutstandingLoan() throws Throwable {
		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		Integer contractId = 0; // UTA: default value
		Long profileId = 0L; // UTA: default value
		Integer loanId = 0; // UTA: default value
		OutstandingLoan result = underTest.getOutstandingLoan(contractId, profileId, loanId);

		// Then
	    assertNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getOutstandingOldIloanRequestsCount(int)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanSupportDao#getOutstandingOldIloanRequestsCount(int)
	 * @author patelpo
	 */
	@Test
	public void testGetOutstandingOldIloanRequestsCount() throws Throwable {

		Integer count = new Integer(10);
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(
				SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenReturn(count);

		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		int contractId = 0; // UTA: default value
		Integer result = underTest.getOutstandingOldIloanRequestsCount(contractId);

		// Then
		 assertNotNull(result);
		 assertEquals(10, result.intValue());
	}

	@Test(expected = LoanDaoException.class)
	public void testGetOutstandingOldIloanRequestsCount_Exception() throws Throwable {

		Integer count = new Integer(10);
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(
				SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenThrow(new DAOException());

		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		int contractId = 0; // UTA: default value
		Integer result = underTest.getOutstandingOldIloanRequestsCount(contractId);

		// Then
		 assertNotNull(result);
		 assertEquals(0, result.intValue());
	}

	

	/**
	 * Parasoft Jtest UTA: Test for getPartialLoanSettingsData(Integer[])
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanSupportDao#getPartialLoanSettingsData(Integer[])
	 * @author patelpo
	 */
	@Test
	public void testGetPartialLoanSettingsData() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		Integer[] contractIdList = new java.lang.Integer[0]; // UTA: default value
		Map<Integer, ArrayList<LoanSettings>> result = underTest.getPartialLoanSettingsData(contractIdList);

		// Then
		 assertNotNull(result);
		 assertEquals(1, result.size());
		 assertTrue(result.containsKey(0));
		 assertFalse(result.containsValue(null));
	}


	/**
	 * Parasoft Jtest UTA: Test for getParticipantIdByProfileId(Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanSupportDao#getParticipantIdByProfileId(Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetParticipantIdByProfileId() throws Throwable {
		SelectSingleOrNoValueQueryHandler newSelectSingleOrNoValueQueryHandlerResult = mock(SelectSingleOrNoValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleOrNoValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleOrNoValueQueryHandlerResult);
		when(newSelectSingleOrNoValueQueryHandlerResult.select(any(Object[].class))).thenReturn(10);

		// Given
		DataSource csdbDataSource =getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		Integer profileId = 10; // UTA: default value
		Integer result = underTest.getParticipantIdByProfileId(profileId);

		// Then
		 assertNotNull(result);
		 assertEquals(10, result.intValue());
	}
	@Test(expected = LoanDaoException.class)
	public void testGetParticipantIdByProfileId_Exception() throws Throwable {
		SelectSingleOrNoValueQueryHandler newSelectSingleOrNoValueQueryHandlerResult = mock(SelectSingleOrNoValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleOrNoValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleOrNoValueQueryHandlerResult);
		when(newSelectSingleOrNoValueQueryHandlerResult.select(any(Object[].class))).thenThrow(new DAOException());

		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);
		
		// When
		Integer profileId = 0; // UTA: default value
		Integer result = underTest.getParticipantIdByProfileId(profileId);
		
		// Then
		 assertNotNull(result);
		 assertEquals(0, result.intValue());
	}

	/**
	 * Parasoft Jtest UTA: Test for getParticipantMoneyTypesForLoans(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanSupportDao#getParticipantMoneyTypesForLoans(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetParticipantMoneyTypesForLoans() throws Throwable {
		LoanMoneyType loanMoneyType = new LoanMoneyType();
		loanMoneyType.setMoneyTypeId("Test");
		List<LoanMoneyType> loanMoneyTypes = new ArrayList<LoanMoneyType>();
		loanMoneyTypes.add(loanMoneyType);
		
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(loanMoneyTypes);

		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		Integer contractId = 0; // UTA: default value
		Integer profileId = 0; // UTA: default value
		List<LoanMoneyType> result = underTest.getParticipantMoneyTypesForLoans(contractId, profileId);

		// Then
		 assertNotNull(result);
		 assertEquals(1, result.size());
		 assertFalse(result.contains(null));
	}
	
	@Test(expected = LoanDaoException.class)
	public void testGetParticipantMoneyTypesForLoans_Exception() throws Throwable {
		LoanMoneyType loanMoneyType = new LoanMoneyType();
		loanMoneyType.setMoneyTypeId("Test");
		List<LoanMoneyType> loanMoneyTypes = new ArrayList<LoanMoneyType>();
		loanMoneyTypes.add(loanMoneyType);
		
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenThrow(new DAOException());

		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		Integer contractId = 0; // UTA: default value
		Integer profileId = 0; // UTA: default value
		List<LoanMoneyType> result = underTest.getParticipantMoneyTypesForLoans(contractId, profileId);

		// Then
		 assertNotNull(result);
		 assertEquals(0, result.size());
		 assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Test for getParticipantProfileIdForSubmission(int, int)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanSupportDao#getParticipantProfileIdForSubmission(int, int)
	 * @author patelpo
	 */
	@Test
	public void testGetParticipantProfileIdForSubmission() throws Throwable {
		Integer participantProfileId = new Integer(10);
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenReturn(participantProfileId);

		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		int submissionNumber = 0; // UTA: default value
		int contractId = 0; // UTA: default value
		Integer result = underTest.getParticipantProfileIdForSubmission(submissionNumber, contractId);

		// Then
		 assertNotNull(result);
		 assertEquals(10, result.intValue());
	}
	@Test(expected = LoanDaoException.class)
	public void testGetParticipantProfileIdForSubmission_Exception() throws Throwable {
		Integer participantProfileId = new Integer(10);
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenThrow(new DAOException());

		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		int submissionNumber = 0; // UTA: default value
		int contractId = 0; // UTA: default value
		Integer result = underTest.getParticipantProfileIdForSubmission(submissionNumber, contractId);

		// Then
		 assertNotNull(result);
		 assertEquals(0, result.intValue());
	}


	/**
	 * Parasoft Jtest UTA: Test for hasLoanRecordKeepingProductFeature(int)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanSupportDao#hasLoanRecordKeepingProductFeature(int)
	 * @author patelpo
	 */
	@Test
	public void testHasLoanRecordKeepingProductFeature() throws Throwable {
		Integer count = new Integer(10);
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenReturn(count);

		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		int contractId = 0; // UTA: default value
		boolean result = underTest.hasLoanRecordKeepingProductFeature(contractId);

		// Then
		 assertTrue(result);
	}
	@Test(expected = LoanDaoException.class)
	public void testHasLoanRecordKeepingProductFeature_Exception() throws Throwable {
		Integer count = new Integer(10);
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenThrow(new DAOException());

		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		int contractId = 0; // UTA: default value
		boolean result = underTest.hasLoanRecordKeepingProductFeature(contractId);

		// Then
		 assertFalse(result);
	}


	/**
	 * Parasoft Jtest UTA: Test for isPositivePbaBalance(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanSupportDao#isPositivePbaBalance(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testIsPositivePbaBalance() throws Throwable {
		BigDecimal balance = new BigDecimal("10");
		
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenReturn(balance);

		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		Integer contractId = 0; // UTA: default value
		Integer profileId = 0; // UTA: default value
		boolean result = underTest.isPositivePbaBalance(contractId, profileId);

		// Then
		 assertTrue(result);
	}
	@Test(expected= LoanDaoException.class)
	public void testIsPositivePbaBalance_Exception() throws Throwable {
		// Given
		DataSource csdbDataSource = getCachedDataSourceResult;
		LoanSupportDao underTest = new LoanSupportDao(csdbDataSource);

		// When
		Integer contractId = 0; // UTA: default value
		Integer profileId = 0; // UTA: default value
		boolean result = underTest.isPositivePbaBalance(contractId, profileId);

		// Then
		 assertFalse(result);
	}

}