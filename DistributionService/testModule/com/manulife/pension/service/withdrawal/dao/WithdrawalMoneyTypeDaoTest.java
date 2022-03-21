/**
 * 
 */
package com.manulife.pension.service.withdrawal.dao;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.withdrawal.exception.WithdrawalDaoException;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.util.JdbcHelper;

import static org.mockito.Matchers.anyString;
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

import javax.ejb.EJBException;
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
 * Parasoft Jtest UTA: Test class for WithdrawalMoneyTypeDao
 *
 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, WithdrawalRequest.class, WithdrawalMoneyTypeDao.class,
		BaseDatabaseDAO.class })
@RunWith(PowerMockRunner.class)
public class WithdrawalMoneyTypeDaoTest {
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
	 * Parasoft Jtest UTA: Test for deleteAll(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#deleteAll(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		WithdrawalMoneyTypeDao underTest = new WithdrawalMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	@Test(expected = DistributionServiceDaoException.class)
	public void testDeleteAll_Exception() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		WithdrawalMoneyTypeDao underTest = new WithdrawalMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for insert(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#insert(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsert() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		WithdrawalMoneyTypeDao underTest = new WithdrawalMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		WithdrawalRequestMoneyType requestMoneyType = new WithdrawalRequestMoneyType();
		moneyTypes.add(requestMoneyType);
		underTest.insert(submissionId, contractId, userProfileId, moneyTypes);

	}

	@Test
	public void testInsert_1() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		WithdrawalMoneyTypeDao underTest = new WithdrawalMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestMoneyType> moneyTypes = null;

		underTest.insert(submissionId, contractId, userProfileId, moneyTypes);

	}

	@Test(expected = DistributionServiceDaoException.class)
	public void testInsert_exception() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		WithdrawalMoneyTypeDao underTest = new WithdrawalMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		WithdrawalRequestMoneyType requestMoneyType = new WithdrawalRequestMoneyType();
		moneyTypes.add(requestMoneyType);
		underTest.insert(submissionId, contractId, userProfileId, moneyTypes);

	}

	/**
	 * Parasoft Jtest UTA: Test for insertUpdate(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#insertUpdate(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsertUpdate() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		WithdrawalMoneyTypeDao underTest = new WithdrawalMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		WithdrawalRequestMoneyType withdrawalRequestMoneyType = new WithdrawalRequestMoneyType();
		moneyTypes.add(withdrawalRequestMoneyType);
		underTest.insertUpdate(submissionId, contractId, userProfileId, moneyTypes);

	}

	@Test
	public void testInsertUpdate_2() throws Throwable {
		// Given
		WithdrawalMoneyTypeDao underTest = new WithdrawalMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		WithdrawalRequestMoneyType withdrawalRequestMoneyType = new WithdrawalRequestMoneyType();
		moneyTypes.add(withdrawalRequestMoneyType);
		underTest.insertUpdate(submissionId, contractId, userProfileId, moneyTypes);

	}

	@Test
	public void testInsertUpdate_1() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		WithdrawalMoneyTypeDao underTest = new WithdrawalMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestMoneyType> moneyTypes = null;

		underTest.insertUpdate(submissionId, contractId, userProfileId, moneyTypes);

	}

	@Test(expected = WithdrawalDaoException.class)
	public void testInsertUpdate_Exception() throws Throwable {
		when(getConnectionResult.prepareStatement(anyString())).thenThrow(new SQLException());
		// Given
		WithdrawalMoneyTypeDao underTest = new WithdrawalMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		WithdrawalRequestMoneyType withdrawalRequestMoneyType = new WithdrawalRequestMoneyType();
		moneyTypes.add(withdrawalRequestMoneyType);
		underTest.insertUpdate(submissionId, contractId, userProfileId, moneyTypes);

	}

	/**
	 * Parasoft Jtest UTA: Test for update(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#update(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testUpdate() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		WithdrawalMoneyTypeDao underTest = new WithdrawalMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		WithdrawalRequestMoneyType requestMoneyType = new WithdrawalRequestMoneyType();
		moneyTypes.add(requestMoneyType);
		underTest.update(submissionId, contractId, userProfileId, moneyTypes);

	}

	@Test
	public void testUpdate_1() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		WithdrawalMoneyTypeDao underTest = new WithdrawalMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestMoneyType> moneyTypes = null;
		underTest.update(submissionId, contractId, userProfileId, moneyTypes);

	}

	@Test(expected = DistributionServiceDaoException.class)
	public void testUpdate_Expected() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		WithdrawalMoneyTypeDao underTest = new WithdrawalMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		WithdrawalRequestMoneyType requestMoneyType = new WithdrawalRequestMoneyType();
		moneyTypes.add(requestMoneyType);
		underTest.update(submissionId, contractId, userProfileId, moneyTypes);

	}

	/**
	 * Parasoft Jtest UTA: Test cloned from
	 * com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDaoTest#testInsertUpdate_2()
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#insertUpdate(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test(expected = WithdrawalDaoException.class)
	public void testInsertUpdate_3() throws Throwable {
		// Given
		mockStatic(JdbcHelper.class); // UTA: modified method call

		String message = ""; // UTA: default value
		

		doThrow(new NamingException()).when(JdbcHelper.class, "getCachedDataSource", anyString());

		WithdrawalMoneyTypeDao underTest = new WithdrawalMoneyTypeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		WithdrawalRequestMoneyType withdrawalRequestMoneyType = new WithdrawalRequestMoneyType();
		moneyTypes.add(withdrawalRequestMoneyType);
		underTest.insertUpdate(submissionId, contractId, userProfileId, moneyTypes);

	}
}