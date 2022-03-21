/**
 * 
 */
package com.manulife.pension.service.withdrawal.dao;

import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.mock;

import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.intware.dao.DAOException;
import com.manulife.pension.util.JdbcHelper;

/**
 * Parasoft Jtest UTA: Test class for ApolloDao
 *
 * @see com.manulife.pension.service.withdrawal.dao.ApolloDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, ApolloDao.class })
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
	 * Parasoft Jtest UTA: Test for getNumberOfCompletedWithdrawalTransaction(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.ApolloDao#getNumberOfCompletedWithdrawalTransaction(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetNumberOfCompletedWithdrawalTransaction() throws Throwable {
		InitialContext newInitialContextResult = mock(InitialContext.class); // UTA: default value
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(4);
		
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		when(executeQueryResult.getInt("numberOfCompletedWithdrawalTransactions")).thenReturn(1);


		// Given
		ApolloDao underTest = new ApolloDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer participantId = 0; // UTA: default value
		int result = underTest.getNumberOfCompletedWithdrawalTransaction(contractId, participantId);

		// Then
		// assertEquals(0, result);
	}
	@Test(expected = DAOException.class)
	public void testGetNumberOfCompletedWithdrawalTransaction_Exception() throws Throwable {
		InitialContext newInitialContextResult = mock(InitialContext.class); // UTA: default value
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(4);
		
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		when(executeQueryResult.getInt(1)).thenThrow(new SQLException());
		//when(executeQueryResult.getInt("numberOfCompletedWithdrawalTransactions")).thenReturn(1);
		
		
		// Given
		ApolloDao underTest = new ApolloDao();
		
		// When
		Integer contractId = 0; // UTA: default value
		Integer participantId = 0; // UTA: default value
		int result = underTest.getNumberOfCompletedWithdrawalTransaction(contractId, participantId);
		
		// Then
		// assertEquals(0, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getNumberOfPendingWithdrawalTransaction(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.ApolloDao#getNumberOfPendingWithdrawalTransaction(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetNumberOfPendingWithdrawalTransaction() throws Throwable {
		// Given
		ApolloDao underTest = new ApolloDao();
		InitialContext newInitialContextResult = mock(InitialContext.class); // UTA: default value
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(4);
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		when(executeQueryResult.getInt(1)).thenReturn(1);
		
		
		
		// When
		Integer contractId = 0; // UTA: default value
		Integer participantId = 0; // UTA: default value
		int result = underTest.getNumberOfPendingWithdrawalTransaction(contractId, participantId);

		// Then
		// assertEquals(0, result);
	}
	@Test(expected = DAOException.class)
	public void testGetNumberOfPendingWithdrawalTransaction_Exception() throws Throwable {
		// Given
		ApolloDao underTest = new ApolloDao();
		InitialContext newInitialContextResult = mock(InitialContext.class); // UTA: default value
		whenNew(InitialContext.class).withAnyArguments().thenReturn(newInitialContextResult);
		when(newInitialContextResult.lookup(nullable(String.class))).thenReturn(4);
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		when(executeQueryResult.getInt(1)).thenThrow(new SQLException());
		
		
		
		// When
		Integer contractId = 0; // UTA: default value
		Integer participantId = 0; // UTA: default value
		int result = underTest.getNumberOfPendingWithdrawalTransaction(contractId, participantId);
		
		// Then
		// assertEquals(0, result);
	}
}