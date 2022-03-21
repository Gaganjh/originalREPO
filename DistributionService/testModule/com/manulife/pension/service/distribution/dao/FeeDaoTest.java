/**
 * 
 */
package com.manulife.pension.service.distribution.dao;

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
import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.loan.valueobject.LoanFee;
import com.manulife.pension.util.JdbcHelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
 * Parasoft Jtest UTA: Test class for FeeDao
 *
 * @see com.manulife.pension.service.distribution.dao.FeeDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, FeeDao.class })
@RunWith(PowerMockRunner.class)
public class FeeDaoTest {
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
	 * @see com.manulife.pension.service.distribution.dao.FeeDao#delete(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testDelete() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		FeeDao underTest = new FeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Fee> fees = new ArrayList<Fee>(); // UTA: default value
		Fee fee = new LoanFee();
		fees.add(fee);
		underTest.delete(submissionId, contractId, userProfileId, fees);

	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testDelete_1() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);
		
		// Given
		FeeDao underTest = new FeeDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Fee> fees = new ArrayList<Fee>(); // UTA: default value
		Fee fee = new LoanFee();
		Fee fee1 = new LoanFee();
		fees.add(fee);
		fees.add(fee1);
		underTest.delete(submissionId, contractId, userProfileId, fees);
		
	}
	@Test
	public void testDelete_2() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		FeeDao underTest = new FeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Fee> fees = null;
		underTest.delete(submissionId, contractId, userProfileId, fees);

	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testDelete_Exception() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		FeeDao underTest = new FeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Fee> fees = new ArrayList<Fee>(); // UTA: default value
		Fee fee = new LoanFee();
		fees.add(fee);
		underTest.delete(submissionId, contractId, userProfileId, fees);

	}

	/**
	 * Parasoft Jtest UTA: Test for deleteAll(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.distribution.dao.FeeDao#deleteAll(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		FeeDao underTest = new FeeDao();

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
		FeeDao underTest = new FeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for insert(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.FeeDao#insert(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsert() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		FeeDao underTest = new FeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Fee> fees = new ArrayList<Fee>(); // UTA: default value
		Fee fee = new LoanFee();
		fees.add(fee);
		underTest.insert(submissionId, contractId, userProfileId, fees);

	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testInsert_1() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);
		
		// Given
		FeeDao underTest = new FeeDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Fee> fees = new ArrayList<Fee>(); // UTA: default value
		Fee fee = new LoanFee();
		Fee fee1 = new LoanFee();
		fees.add(fee);
		fees.add(fee1);
		underTest.insert(submissionId, contractId, userProfileId, fees);
		
	}

	@Test
	public void testInsert_2() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		FeeDao underTest = new FeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Fee> fees = null;
		underTest.insert(submissionId, contractId, userProfileId, fees);

	}

	@Test(expected = DistributionServiceDaoException.class)
	public void testInsert_Exception() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		FeeDao underTest = new FeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Fee> fees = new ArrayList<Fee>(); // UTA: default value
		Fee fee = new LoanFee();
		fees.add(fee);
		underTest.insert(submissionId, contractId, userProfileId, fees);

	}

	/**
	 * Parasoft Jtest UTA: Test for insertUpdatePrune(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.FeeDao#insertUpdatePrune(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsertUpdatePrune() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		FeeDao underTest = new FeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Fee> input = new ArrayList<Fee>(); // UTA: default value
		Fee fee =new LoanFee();
		
		input.add(fee);
		
		Class feeType = Class.forName("com.manulife.pension.service.distribution.valueobject.Fee"); // UTA: default value
		underTest.insertUpdatePrune(submissionId, contractId, userProfileId, input, feeType);

	}
	@Test
	public void testInsertUpdatePrune_1() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);
		
		// Given
		FeeDao underTest = new FeeDao();
		
		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 20; // UTA: default value
		Integer userProfileId = 30; // UTA: default value
		Collection<Fee> input = new ArrayList<Fee>(); // UTA: default value
		Fee fee = mock(Fee.class);
		when(fee.isBlank()).thenReturn(false);
		
		List tempList = new ArrayList();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(tempList);

		input.add(fee);
		
		Class feeType = Class.forName("com.manulife.pension.service.distribution.valueobject.Fee"); // UTA: default value
		underTest.insertUpdatePrune(submissionId, contractId, userProfileId, input, feeType);
		
	}
	@Test
	public void testInsertUpdatePrune_11() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);
		
		// Given
		FeeDao underTest = new FeeDao();
		
		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 20; // UTA: default value
		Integer userProfileId = 30; // UTA: default value
		Collection<Fee> input = new ArrayList<Fee>(); // UTA: default value
		Fee fee = mock(Fee.class);
		when(fee.isBlank()).thenReturn(false);
		
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(input);
		
		input.add(fee);
		
		Class feeType = Class.forName("com.manulife.pension.service.distribution.valueobject.Fee"); // UTA: default value
		underTest.insertUpdatePrune(submissionId, contractId, userProfileId, input, feeType);
		
	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testInsertUpdatePrune_Exception() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);
		
		// Given
		FeeDao underTest = new FeeDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Fee> input = new ArrayList<Fee>(); // UTA: default value
		Fee fee = new LoanFee();
		Fee fee1 = new LoanFee();
		input.add(fee);
		input.add(fee1);
		
		Class feeType = Class.forName("com.manulife.pension.service.distribution.valueobject.Fee"); // UTA: default value
		underTest.insertUpdatePrune(submissionId, contractId, userProfileId, input, feeType);
		
	}

	/**
	 * Parasoft Jtest UTA: Test for select(Integer, Integer, Integer, Class)
	 *
	 * @see com.manulife.pension.service.distribution.dao.FeeDao#select(Integer, Integer, Integer, Class)
	 * @author patelpo
	 */
	@Test
	public void testSelect() throws Throwable {
		List<Fee> returnList = new ArrayList<Fee>();
		Fee fee = new LoanFee();
		returnList.add(fee);
		SelectBeanListQueryHandler selectBeanListQueryHandler = mock(SelectBeanListQueryHandler.class);
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(selectBeanListQueryHandler);
		when(selectBeanListQueryHandler.select(any(Object[].class))).thenReturn(returnList);
		// Given
		FeeDao underTest = new FeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Class feeType = Class.forName("com.manulife.pension.service.distribution.valueobject.Fee"); // UTA: default value
		List<? extends Fee> result = underTest.select(submissionId, contractId, userProfileId, feeType);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	@Test(expected = DistributionServiceDaoException.class)
	public void testSelect_Exception() throws Throwable {
		List<Fee> returnList = new ArrayList<Fee>();
		Fee fee = new LoanFee();
		returnList.add(fee);
		SelectBeanListQueryHandler selectBeanListQueryHandler = mock(SelectBeanListQueryHandler.class);
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(selectBeanListQueryHandler);
		when(selectBeanListQueryHandler.select(any(Object[].class))).thenThrow(DAOException.class);
		// Given
		FeeDao underTest = new FeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Class feeType = Class.forName("com.manulife.pension.service.distribution.valueobject.Fee"); // UTA: default value
		List<? extends Fee> result = underTest.select(submissionId, contractId, userProfileId, feeType);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Test for update(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.FeeDao#update(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testUpdate() throws Throwable {
		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class); // UTA: default value
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenReturn(10);

		// Given
		FeeDao underTest = new FeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Fee> fees = new ArrayList<Fee>(); // UTA: default value
		Fee fee = new LoanFee();
		fees.add(fee);
		underTest.update(submissionId, contractId, userProfileId, fees);

	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testUpdate_1() throws Throwable {
		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class); // UTA: default value
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenReturn(10);
		
		// Given
		FeeDao underTest = new FeeDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Fee> fees = new ArrayList<Fee>(); // UTA: default value
		Fee fee = new LoanFee();
		Fee fee1 = new LoanFee();
		fees.add(fee);
		fees.add(fee1);
		underTest.update(submissionId, contractId, userProfileId, fees);
		
	}
	@Test
	public void testUpdate_2() throws Throwable {
		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class); // UTA: default value
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenReturn(10);
		
		// Given
		FeeDao underTest = new FeeDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Fee> fees = null;
		underTest.update(submissionId, contractId, userProfileId, fees);
		
	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testUpdate_Exception() throws Throwable {
		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class); // UTA: default value
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		FeeDao underTest = new FeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Fee> fees = new ArrayList<Fee>(); // UTA: default value
		Fee fee = new LoanFee();
		fees.add(fee);
		underTest.update(submissionId, contractId, userProfileId, fees);

	}
	
}