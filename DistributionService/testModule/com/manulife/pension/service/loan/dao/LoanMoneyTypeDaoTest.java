/**
 * 
 */
package com.manulife.pension.service.loan.dao;

import static org.junit.Assert.assertEquals;
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
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.intware.dao.jdbc.SelectBeanQueryHandler;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.util.JdbcHelper;

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

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for LoanMoneyTypeDao
 *
 * @see com.manulife.pension.service.loan.dao.LoanMoneyTypeDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, LoanMoneyTypeDao.class })
@RunWith(PowerMockRunner.class)
public class LoanMoneyTypeDaoTest {
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
		when(prepareStatementResult.execute()).thenReturn(true);
		when(prepareStatementResult.getResultSet()).thenReturn(executeQueryResult);

	}

	/**
	 * Parasoft Jtest UTA: Test for deleteAll(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanMoneyTypeDao#deleteAll(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class);
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(1);

		// Given
		LoanMoneyTypeDao underTest = new LoanMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	@Test(expected = LoanDaoException.class)
	public void testDeleteAll_Exception() throws Throwable {

		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class);
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenThrow(new DAOException());

		// Given
		LoanMoneyTypeDao underTest = new LoanMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for insert(Integer, Integer, Integer, Timestamp, Collection)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanMoneyTypeDao#insert(Integer, Integer, Integer, Timestamp, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsert() throws Throwable {

		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class);
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(1);

		// Given
		LoanMoneyTypeDao underTest = new LoanMoneyTypeDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanMoneyType> moneyTypes = new ArrayList<LoanMoneyType>(); // UTA: default value
		LoanMoneyType loanMoneyType = new LoanMoneyType();
		loanMoneyType.getAccountBalance();
		loanMoneyType.getLoanBalance();
		moneyTypes.add(loanMoneyType);
		underTest.insert(submissionId, contractId, userProfileId, timestamp, moneyTypes);

	}

	@Test(expected = LoanDaoException.class)
	public void testInsert_Exception() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class);
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenThrow(new DAOException());
		// Given
		LoanMoneyTypeDao underTest = new LoanMoneyTypeDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanMoneyType> moneyTypes = new ArrayList<LoanMoneyType>(); // UTA: default value
		LoanMoneyType loanMoneyType = new LoanMoneyType();
		loanMoneyType.getAccountBalance();
		loanMoneyType.getLoanBalance();
		moneyTypes.add(loanMoneyType);
		underTest.insert(submissionId, contractId, userProfileId, timestamp, moneyTypes);

	}

	/**
	 * Parasoft Jtest UTA: Test for insertUpdate(Integer, Integer, Integer, Timestamp, Collection)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanMoneyTypeDao#insertUpdate(Integer, Integer, Integer, Timestamp, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsertUpdate() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		// Given
		LoanMoneyTypeDao underTest = new LoanMoneyTypeDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanMoneyType> moneyTypes = new ArrayList<LoanMoneyType>(); // UTA: default value
		LoanMoneyType loanMoneyType = new LoanMoneyType();
		loanMoneyType.getAccountBalance();
		loanMoneyType.getLoanBalance();
		moneyTypes.add(loanMoneyType);
		underTest.insertUpdate(submissionId, contractId, userProfileId, timestamp, moneyTypes);

	}

	@Test
	public void testInsertUpdate_1() throws Throwable {
		// Given
		LoanMoneyTypeDao underTest = new LoanMoneyTypeDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanMoneyType> moneyTypes = null;
		underTest.insertUpdate(submissionId, contractId, userProfileId, timestamp, moneyTypes);

	}

	@Test(expected = LoanDaoException.class)
	public void testInsertUpdate_exception() throws Throwable {
		when(getConnectionResult.prepareStatement(anyString())).thenThrow(new SQLException());

		// Given
		LoanMoneyTypeDao underTest = new LoanMoneyTypeDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanMoneyType> moneyTypes = new ArrayList<LoanMoneyType>(); // UTA: default value
		LoanMoneyType loanMoneyType = new LoanMoneyType();
		loanMoneyType.getAccountBalance();
		loanMoneyType.getLoanBalance();
		moneyTypes.add(loanMoneyType);
		underTest.insertUpdate(submissionId, contractId, userProfileId, timestamp, moneyTypes);

	}

	/**
	 * Parasoft Jtest UTA: Test for read(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanMoneyTypeDao#read(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testRead() throws Throwable {

		LoanMoneyType loanMoneyType = new LoanMoneyType();
		SelectBeanQueryHandler newSelectBeanQueryHandlerResult = mock(SelectBeanQueryHandler.class); // UTA: default value
		whenNew(SelectBeanQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanQueryHandlerResult);
		when(newSelectBeanQueryHandlerResult.select(any(Object[].class))).thenReturn(loanMoneyType);

		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		// Given
		LoanMoneyTypeDao underTest = new LoanMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		List<LoanMoneyType> result = underTest.read(submissionId, contractId, userProfileId);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	

	/**
	 * Parasoft Jtest UTA: Test for update(Integer, Integer, Integer, Timestamp, Collection)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanMoneyTypeDao#update(Integer, Integer, Integer, Timestamp, Collection)
	 * @author patelpo
	 */
	@Test
	public void testUpdate() throws Throwable {
		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class);
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenReturn(1);

		// Given
		LoanMoneyTypeDao underTest = new LoanMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanMoneyType> moneyTypes = new ArrayList<LoanMoneyType>(); // UTA: default value
		LoanMoneyType loanMoneyType = new LoanMoneyType();
		loanMoneyType.getAccountBalance();
		loanMoneyType.getLoanBalance();
		moneyTypes.add(loanMoneyType);
		underTest.update(submissionId, contractId, userProfileId, timestamp, moneyTypes);

	}
	@Test
	public void testUpdate_1() throws Throwable {
		// Given
		LoanMoneyTypeDao underTest = new LoanMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanMoneyType> moneyTypes = null;
		underTest.update(submissionId, contractId, userProfileId, timestamp, moneyTypes);

	}
	@Test(expected = LoanDaoException.class)
	public void testUpdate_Exception() throws Throwable {
		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class);
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenThrow(new DAOException());
		
		// Given
		LoanMoneyTypeDao underTest = new LoanMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<LoanMoneyType> moneyTypes = new ArrayList<LoanMoneyType>(); // UTA: default value
		LoanMoneyType loanMoneyType = new LoanMoneyType();
		loanMoneyType.getAccountBalance();
		loanMoneyType.getLoanBalance();
		moneyTypes.add(loanMoneyType);
		underTest.update(submissionId, contractId, userProfileId, timestamp, moneyTypes);

	}
}