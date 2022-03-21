/**
 * 
 */
package com.manulife.pension.service.loan.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.util.JdbcHelper;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
 * Parasoft Jtest UTA: Test class for LoanParameterDao
 *
 * @see com.manulife.pension.service.loan.dao.LoanParameterDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, LoanParameterDao.class, BaseDatabaseDAO.class })
@RunWith(PowerMockRunner.class)
public class LoanParameterDaoTest {
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
		when(prepareStatementResult.getResultSet()).thenReturn(executeQueryResult);

	}

	/**
	 * Parasoft Jtest UTA: Test for checkLoanStatusExists(Integer, String)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanParameterDao#checkLoanStatusExists(Integer, String)
	 * @author patelpo
	 */
	@Test
	public void testCheckLoanStatusExists() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		String statusCode = ""; // UTA: default value
		boolean result = underTest.checkLoanStatusExists(submissionId, statusCode);

		// Then
		 assertTrue(result);
	}

	@Test(expected = LoanDaoException.class)
	public void testCheckLoanStatusExists_exception() throws Throwable {
		when(getConnectionResult.prepareStatement(anyString())).thenThrow(new SQLException());

		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		String statusCode = ""; // UTA: default value
		boolean result = underTest.checkLoanStatusExists(submissionId, statusCode);

		// Then
		 assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for deleteAll(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanParameterDao#deleteAll(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	@Test(expected = LoanDaoException.class)
	public void testDeleteAll_Exception() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for insert(Integer, Integer, Integer, Timestamp, Collection)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanParameterDao#insert(Integer, Integer, Integer, Timestamp, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsert() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanParameter> objects = new ArrayList<LoanParameter>(); // UTA: default value
		LoanParameter loanParameter = new LoanParameter();
		objects.add(loanParameter);
		underTest.insert(submissionId, contractId, userProfileId, timestamp, objects);

	}

	@Test
	public void testInsert_1() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanParameter> objects = null;
		underTest.insert(submissionId, contractId, userProfileId, timestamp, objects);

	}

	@Test(expected = LoanDaoException.class)
	public void testInsert_Exception() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanParameter> objects = new ArrayList<LoanParameter>(); // UTA: default value
		LoanParameter loanParameter = new LoanParameter();
		objects.add(loanParameter);
		underTest.insert(submissionId, contractId, userProfileId, timestamp, objects);

	}

	/**
	 * Parasoft Jtest UTA: Test for insertUpdate(Integer, Integer, Integer, Timestamp, Collection)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanParameterDao#insertUpdate(Integer, Integer, Integer, Timestamp, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsertUpdate() throws Throwable {

		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class); // UTA: default value
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenReturn(10);

		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanParameter> objects = new ArrayList<LoanParameter>(); // UTA: default value
		LoanParameter loanParameter = mock(LoanParameter.class);
		when(loanParameter.isReadyToSave()).thenReturn(true);
		objects.add(loanParameter);
		underTest.insertUpdate(submissionId, contractId, userProfileId, timestamp, objects);

	}
	@Test
	public void testInsertUpdate1() throws Throwable {
		
		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class); // UTA: default value
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenReturn(10);
		
		// Given
		LoanParameterDao underTest = new LoanParameterDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanParameter> objects = new ArrayList<LoanParameter>(); // UTA: default value
		LoanParameter loanParameter = mock(LoanParameter.class);
		when(loanParameter.isReadyToSave()).thenReturn(true);
		objects.add(loanParameter);
		underTest.insertUpdate(submissionId, contractId, userProfileId, timestamp, objects);
		
	}

	@Test
	public void testInsertUpdate_1() throws Throwable {
		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class); // UTA: default value
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenReturn(10);

		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanParameter> objects = null;

		underTest.insertUpdate(submissionId, contractId, userProfileId, timestamp, objects);

	}

	@Test
	public void testInsertUpdate_2() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class); // UTA: default value
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenReturn(10);

		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanParameter> objects = new ArrayList<LoanParameter>(); // UTA: default value
		LoanParameter loanParameter = mock(LoanParameter.class);
		when(loanParameter.isReadyToSave()).thenReturn(false);
		objects.add(loanParameter);
		underTest.insertUpdate(submissionId, contractId, userProfileId, timestamp, objects);

	}

	/**
	 * Parasoft Jtest UTA: Test for read(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanParameterDao#read(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testRead() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		when(executeQueryResult.getBigDecimal("INTEREST_RATE_PCT")).thenReturn(BigDecimal.ONE);
		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		List<LoanParameter> result = underTest.read(submissionId, contractId, userProfileId);

		// Then
		 assertNotNull(result);
		 assertEquals(1, result.size());
		 assertFalse(result.contains(null));
	}
	
	@Test(expected = LoanDaoException.class)
	public void testRead_Exception() throws Throwable {
		when(getConnectionResult.prepareStatement(anyString())).thenThrow(new SQLException());
		
		// Given
		LoanParameterDao underTest = new LoanParameterDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		List<LoanParameter> result = underTest.read(submissionId, contractId, userProfileId);
		
		// Then
		 assertNotNull(result);
		 assertEquals(0, result.size());
		 assertTrue(result.contains(null));
	}
	
	@Test(expected = LoanDaoException.class)
	public void testInsertUpdate_exception() throws Throwable {
		when(getConnectionResult.prepareStatement(anyString())).thenThrow(new SQLException());
		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanParameter> objects = new ArrayList<LoanParameter>(); // UTA: default value
		LoanParameter loanParameter = mock(LoanParameter.class);
		when(loanParameter.isReadyToSave()).thenReturn(true);
		objects.add(loanParameter);
		underTest.insertUpdate(submissionId, contractId, userProfileId, timestamp, objects);

	}

	/**
	 * Parasoft Jtest UTA: Test for update(Integer, Integer, Integer, Timestamp, Collection)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanParameterDao#update(Integer, Integer, Integer, Timestamp, Collection)
	 * @author patelpo
	 */
	@Test
	public void testUpdate() throws Throwable {
		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class);
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenReturn(10);

		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanParameter> objects = new ArrayList<LoanParameter>(); // UTA: default value
		LoanParameter loanParameter = new LoanParameter();
		objects.add(loanParameter);
		underTest.update(submissionId, contractId, userProfileId, timestamp, objects);

	}

	@Test
	public void testUpdate_1() throws Throwable {
		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class);
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenReturn(10);

		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanParameter> objects = null;
		underTest.update(submissionId, contractId, userProfileId, timestamp, objects);

	}

	@Test(expected = LoanDaoException.class)
	public void testUpdate_Exception() throws Throwable {
		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class);
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanParameter> objects = new ArrayList<LoanParameter>(); // UTA: default value
		LoanParameter loanParameter = new LoanParameter();
		objects.add(loanParameter);
		underTest.update(submissionId, contractId, userProfileId, timestamp, objects);

	}

	/**
	 * Parasoft Jtest UTA: Test cloned from
	 * com.manulife.pension.service.loan.dao.LoanParameterDaoTest#testCheckLoanStatusExists()
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanParameterDao#checkLoanStatusExists(Integer, String)
	 * @author patelpo
	 */
	@Test(expected = LoanDaoException.class)
	public void testCheckLoanStatusExists2() throws Throwable {
		mockStatic(JdbcHelper.class); // UTA: modified method call

		String message = ""; // UTA: default value
		

		doThrow(new NamingException()).when(JdbcHelper.class, "getCachedDataSource", anyString());
		
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		String statusCode = ""; // UTA: default value
		boolean result = underTest.checkLoanStatusExists(submissionId, statusCode);

		// Then
		 assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test cloned from
	 * com.manulife.pension.service.loan.dao.LoanParameterDaoTest#testRead()
	 * Hint: The 'BaseDatabaseDAO.getDefaultConnection(CLASS_NAME,BaseDatabaseDAO.STP_DATA_SOURCE_NAME)' method call must be configured to throw 'SystemException'.
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanParameterDao#read(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test(expected = LoanDaoException.class)
	public void testRead2() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		
		mockStatic(JdbcHelper.class); // UTA: modified method call

		String message = ""; // UTA: default value
		

		doThrow(new NamingException()).when(JdbcHelper.class, "getCachedDataSource", anyString());
		
		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		List<LoanParameter> result = underTest.read(submissionId, contractId, userProfileId);

		// Then
		 assertNotNull(result);
		 assertEquals(0, result.size());
		 assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Test cloned from
	 * com.manulife.pension.service.loan.dao.LoanParameterDaoTest#testInsertUpdate()
	 * Hint: The 'getDefaultConnection(CLASS_NAME,BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME)' method call must be configured to throw 'SystemException'.
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanParameterDao#insertUpdate(Integer, Integer, Integer, Timestamp, Collection)
	 * @author patelpo
	 */
	@Test(expected = LoanDaoException.class)
	public void testInsertUpdate2() throws Throwable {
		
		mockStatic(JdbcHelper.class); // UTA: modified method call

		String message = ""; // UTA: default value
		

		doThrow(new NamingException()).when(JdbcHelper.class, "getCachedDataSource", anyString());
		// Given
		LoanParameterDao underTest = new LoanParameterDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanParameter> objects = new ArrayList<LoanParameter>(); // UTA: default value
		LoanParameter loanParameter = mock(LoanParameter.class);
		when(loanParameter.isReadyToSave()).thenReturn(true);
		objects.add(loanParameter);
		underTest.insertUpdate(submissionId, contractId, userProfileId, timestamp, objects);

	}
}