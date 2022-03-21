/**
 * 
 */
package com.manulife.pension.service.withdrawal.dao;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessage;
import com.manulife.pension.service.withdrawal.exception.WithdrawalDaoException;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestLoan;
import com.manulife.pension.util.JdbcHelper;

import static org.mockito.Matchers.anyString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalLoanDao
 *
 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalLoanDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, WithdrawalRequest.class, WithdrawalLoanDao.class,
		BaseDatabaseDAO.class })
@RunWith(PowerMockRunner.class)
public class WithdrawalLoanDaoTest {
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
	 * Parasoft Jtest UTA: Test for delete(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalLoanDao#delete(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testDelete() throws Throwable {

		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		WithdrawalLoanDao underTest = new WithdrawalLoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestLoan> loans = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		WithdrawalRequestLoan requestLoan = new WithdrawalRequestLoan();
		loans.add(requestLoan);
		underTest.delete(submissionId, contractId, userProfileId, loans);

	}
	@Test
	public void testDelete_1() throws Throwable {
		
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);
		
		// Given
		WithdrawalLoanDao underTest = new WithdrawalLoanDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestLoan> loans = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		underTest.delete(submissionId, contractId, userProfileId, loans);
		
	}

	@Test(expected = WithdrawalDaoException.class)
	public void testDelete_exception() throws Throwable {

		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		WithdrawalLoanDao underTest = new WithdrawalLoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestLoan> loans = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		WithdrawalRequestLoan requestLoan = new WithdrawalRequestLoan();
		loans.add(requestLoan);
		underTest.delete(submissionId, contractId, userProfileId, loans);

	}

	/**
	 * Parasoft Jtest UTA: Test for deleteAll(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalLoanDao#deleteAll(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		WithdrawalLoanDao underTest = new WithdrawalLoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	@Test(expected = WithdrawalDaoException.class)
	public void testDeleteAll_Exception() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		WithdrawalLoanDao underTest = new WithdrawalLoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for insert(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalLoanDao#insert(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsert() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		WithdrawalLoanDao underTest = new WithdrawalLoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestLoan> loans = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		WithdrawalRequestLoan requestLoan = new WithdrawalRequestLoan();
		loans.add(requestLoan);
		underTest.insert(submissionId, contractId, userProfileId, loans);

	}

	@Test(expected = WithdrawalDaoException.class)
	public void testInsert_Exception() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		WithdrawalLoanDao underTest = new WithdrawalLoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestLoan> loans = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		WithdrawalRequestLoan requestLoan = new WithdrawalRequestLoan();
		loans.add(requestLoan);
		underTest.insert(submissionId, contractId, userProfileId, loans);

	}

	/**
	 * Parasoft Jtest UTA: Test for insertUpdatePrune(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalLoanDao#insertUpdatePrune(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsertUpdatePrune() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		WithdrawalLoanDao underTest = new WithdrawalLoanDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Collection<WithdrawalRequestLoan> input = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		WithdrawalRequestLoan withdrawalRequestLoan = new WithdrawalRequestLoan();
		withdrawalRequestLoan.setLoanNo(10);
		input.add(withdrawalRequestLoan);
		Map<Integer, WithdrawalRequestLoan> referenceDBMap =  new HashMap<Integer, WithdrawalRequestLoan>();
		referenceDBMap.put(10, withdrawalRequestLoan);
		
		
		underTest.insertUpdatePrune(submissionId, contractId, userProfileId, input);

	}

	@Test
	public void testInsertUpdatePrune_1() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		WithdrawalLoanDao underTest = new WithdrawalLoanDao();

		// When
		Integer submissionId = 0;
		Integer contractId = 0;
		Integer userProfileId = 0;
		Collection<WithdrawalRequestLoan> input = null;
		underTest.insertUpdatePrune(submissionId, contractId, userProfileId, input);
	}

	@Test(expected = WithdrawalDaoException.class)
	public void testInsertUpdatePrune_exception() throws Throwable {
		when(getConnectionResult.prepareStatement(anyString())).thenThrow(new SQLException());
		// Given
		WithdrawalLoanDao underTest = new WithdrawalLoanDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Collection<WithdrawalRequestLoan> input = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		WithdrawalRequestLoan withdrawalRequestLoan = new WithdrawalRequestLoan();
		input.add(withdrawalRequestLoan);
		underTest.insertUpdatePrune(submissionId, contractId, userProfileId, input);

	}

	/**
	 * Parasoft Jtest UTA: Test for update(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalLoanDao#update(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testUpdate() throws Throwable {

		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class); // UTA: default value
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenReturn(10);

		// Given
		WithdrawalLoanDao underTest = new WithdrawalLoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestLoan> loans = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		WithdrawalRequestLoan requestLoan = new WithdrawalRequestLoan();
		loans.add(requestLoan);
		underTest.update(submissionId, contractId, userProfileId, loans);

	}

	@Test(expected = WithdrawalDaoException.class)
	public void testUpdate_Exception() throws Throwable {

		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class); // UTA: default value
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		WithdrawalLoanDao underTest = new WithdrawalLoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestLoan> loans = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		WithdrawalRequestLoan requestLoan = new WithdrawalRequestLoan();
		loans.add(requestLoan);
		underTest.update(submissionId, contractId, userProfileId, loans);

	}

	/**
	 * Parasoft Jtest UTA: Test cloned from
	 * com.manulife.pension.service.withdrawal.dao.WithdrawalLoanDaoTest#testInsertUpdatePrune_1()
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalLoanDao#insertUpdatePrune(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test(expected = WithdrawalDaoException.class)
	public void testInsertUpdatePrune_2() throws Throwable {
		mockStatic(JdbcHelper.class); // UTA: modified method call

		doThrow(new NamingException()).when(JdbcHelper.class, "getCachedDataSource", anyString());
		
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		WithdrawalLoanDao underTest = new WithdrawalLoanDao();

		// When
		Integer submissionId = 0;
		Integer contractId = 0;
		Integer userProfileId = 0;
		Collection<WithdrawalRequestLoan> input = null;
		underTest.insertUpdatePrune(submissionId, contractId, userProfileId, input);
	}
}