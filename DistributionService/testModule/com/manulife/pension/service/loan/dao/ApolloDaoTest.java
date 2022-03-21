/**
 * 
 */
package com.manulife.pension.service.loan.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.intware.dao.DAOException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.util.JdbcHelper;

/**
 * Parasoft Jtest UTA: Test class for ApolloDao
 *
 * @see com.manulife.pension.service.loan.dao.ApolloDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, LoanDefaults.class,BaseDatabaseDAO.class })
@RunWith(PowerMockRunner.class)
public class ApolloDaoTest {
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
		when(callableStatementResult.executeQuery()).thenReturn(executeQueryResult);
		when(getConnectionResult.prepareStatement(anyString())).thenReturn(prepareStatementResult);
		when(getConnectionResult.createStatement()).thenReturn(statementResult);
		when(getConnectionResult.prepareCall(anyString())).thenReturn(callableStatementResult);
		when(getCachedDataSourceResult.getConnection()).thenReturn(getConnectionResult);
		doReturn(getCachedDataSourceResult).when(JdbcHelper.class, "getCachedDataSource", anyString());

	}

	/**
	 * Parasoft Jtest UTA: Test for getNumberOfCompletedLoanTransaction(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.ApolloDao#getNumberOfCompletedLoanTransaction(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetNumberOfCompletedLoanTransaction() throws Throwable {
		spy(LoanDefaults.class);

		int getCompletedApolloLIThresholdResult = 1; // UTA: default value
		doReturn(getCompletedApolloLIThresholdResult).when(LoanDefaults.class);
		LoanDefaults.getCompletedApolloLIThreshold();

		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		when(executeQueryResult.getInt(1)).thenReturn(134);
		// Given
		ApolloDao underTest = new ApolloDao();

		// When
		Integer contractId = 10; // UTA: default value
		Integer participantId = 10; // UTA: default value
		int result = underTest.getNumberOfCompletedLoanTransaction(contractId, participantId);

		// Then
		assertEquals(134, result);
	}
	@Test(expected = DAOException.class)
	public void testGetNumberOfCompletedLoanTransaction_Exception() throws Throwable {
		spy(LoanDefaults.class);
		
		int getCompletedApolloLIThresholdResult = 1; // UTA: default value
		doReturn(getCompletedApolloLIThresholdResult).when(LoanDefaults.class);
		LoanDefaults.getCompletedApolloLIThreshold();
		
		when(getCachedDataSourceResult.getConnection()).thenThrow(new SQLException());
		// Given
		ApolloDao underTest = new ApolloDao();
		
		// When
		Integer contractId = 10; // UTA: default value
		Integer participantId = 10; // UTA: default value
		int result = underTest.getNumberOfCompletedLoanTransaction(contractId, participantId);
	}

	/**
	 * Parasoft Jtest UTA: Test for getNumberOfPendingLoanTransaction(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.ApolloDao#getNumberOfPendingLoanTransaction(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetNumberOfPendingLoanTransaction() throws Throwable {
		spy(LoanDefaults.class);

		int getPendingApolloLIThresholdResult = 1; // UTA: default value
		doReturn(getPendingApolloLIThresholdResult).when(LoanDefaults.class);
		LoanDefaults.getPendingApolloLIThreshold();

		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		when(executeQueryResult.getInt(1)).thenReturn(1);


		// Given
		ApolloDao underTest = new ApolloDao();

		// When
		Integer contractId = 123; // UTA: default value
		Integer participantId = 456; // UTA: default value
		int result = underTest.getNumberOfPendingLoanTransaction(contractId, participantId);

		// Then
		assertEquals(1, result);
	}
	@Test(expected = DAOException.class)
	public void testGetNumberOfPendingLoanTransaction_Exception() throws Throwable {
		spy(LoanDefaults.class);
		
		int getPendingApolloLIThresholdResult = 1; // UTA: default value
		doReturn(getPendingApolloLIThresholdResult).when(LoanDefaults.class);
		LoanDefaults.getPendingApolloLIThreshold();
		
		when(getCachedDataSourceResult.getConnection()).thenThrow(new SQLException());
		when(executeQueryResult.getInt(1)).thenReturn(1);
		
		
		// Given
		ApolloDao underTest = new ApolloDao();
		
		// When
		Integer contractId = 123; // UTA: default value
		Integer participantId = 456; // UTA: default value
		int result = underTest.getNumberOfPendingLoanTransaction(contractId, participantId);
		
		
	}
	
}