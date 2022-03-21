/**
 * 
 */
package com.manulife.pension.service.distribution.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.loan.exception.LoanDaoException;
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
 * Parasoft Jtest UTA: Test class for ManagedContentDao
 *
 * @see com.manulife.pension.service.distribution.dao.ManagedContentDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, ManagedContentDao.class })
@RunWith(PowerMockRunner.class)
public class ManagedContentDaoTest {
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
	 * Parasoft Jtest UTA: Test for delete(Integer, Integer, List)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ManagedContentDao#delete(Integer, Integer, List)
	 * @author patelpo
	 */
	@Test
	public void testDelete() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class);
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(1);

		// Given
		ManagedContentDao underTest = new ManagedContentDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		List<String> typeCodes = new ArrayList<String>(); // UTA: default value
		typeCodes.add("14");

		underTest.delete(submissionId, contractId, typeCodes);

	}
	@Test(expected =  DistributionServiceDaoException.class)
	public void testDelete_Exception() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class);
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenThrow(new DAOException());


		// Given
		ManagedContentDao underTest = new ManagedContentDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		List<String> typeCodes = new ArrayList<String>(); // UTA: default value
		typeCodes.add("14");

		underTest.delete(submissionId, contractId, typeCodes);

	}

	@Test
	public void testDelete1() throws Throwable {
		// Given
		ManagedContentDao underTest = new ManagedContentDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		List<String> typeCodes = new ArrayList<String>(); // UTA: default value

		underTest.delete(submissionId, contractId, typeCodes);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testDelete2() throws Throwable {
		// Given
		ManagedContentDao underTest = new ManagedContentDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		List<String> typeCodes = new ArrayList<String>(); // UTA: default value
		typeCodes.add("1");
		typeCodes.add("2");
		typeCodes.add("3");
		typeCodes.add("1");
		typeCodes.add("2");
		typeCodes.add("3");
		typeCodes.add("1");
		typeCodes.add("2");
		typeCodes.add("3");
		typeCodes.add("1");
		typeCodes.add("2");
		typeCodes.add("3");
		typeCodes.add("2");
		typeCodes.add("3");

		underTest.delete(submissionId, contractId, typeCodes);

	}

	/**
	 * Parasoft Jtest UTA: Test for deleteAll(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ManagedContentDao#deleteAll(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(new Integer(10));
	

		// Given
		ManagedContentDao underTest = new ManagedContentDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}
	@Test(expected = LoanDaoException.class)
	public void testDeleteAll_exception() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenThrow(DAOException.class);
	

		// Given
		ManagedContentDao underTest = new ManagedContentDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for insert(Integer, Integer, Integer, Timestamp, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ManagedContentDao#insert(Integer, Integer, Integer, Timestamp, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsert() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(new Integer(10));

		// Given
		ManagedContentDao underTest = new ManagedContentDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<ManagedContent> contents = new ArrayList<ManagedContent>(); // UTA: default value
		
		ManagedContent managedContent =new ManagedContent();
		managedContent.setContentId(10);
		managedContent.setContentKey(20);
		managedContent.setCmaSiteCode("Test");
		managedContent.setContentTypeCode("Test1");
		
		contents.add(managedContent);
		underTest.insert(submissionId, contractId, userProfileId, timestamp, contents);

	}
	@Test
	public void testInsert_1() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(new Integer(10));
		
		// Given
		ManagedContentDao underTest = new ManagedContentDao();
		
		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<ManagedContent> contents = null;
		underTest.insert(submissionId, contractId, userProfileId, timestamp, contents);
		
	}
	@Test(expected = LoanDaoException.class)
	public void testInsert_Exception() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		ManagedContentDao underTest = new ManagedContentDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<ManagedContent> contents = new ArrayList<ManagedContent>(); // UTA: default value
		
		ManagedContent managedContent =new ManagedContent();
		managedContent.setContentId(10);
		managedContent.setContentKey(20);
		managedContent.setCmaSiteCode("Test");
		managedContent.setContentTypeCode("Test1");
		
		contents.add(managedContent);
		underTest.insert(submissionId, contractId, userProfileId, timestamp, contents);

	}
	
	

	/**
	 * Parasoft Jtest UTA: Test for read(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ManagedContentDao#read(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testRead() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		ManagedContentDao underTest = new ManagedContentDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		List<ManagedContent> result = underTest.read(submissionId, contractId, userProfileId);

		// Then
		 assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
	/*@Test(expected = LoanDaoException.class)
	public void testRead_exception() throws Throwable {
		
		when(prepareStatementResult.execute()).thenThrow(new SQLException());
		// Given
		ManagedContentDao underTest = new ManagedContentDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		List<ManagedContent> result = underTest.read(submissionId, contractId, userProfileId);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}*/

	/**
	 * Parasoft Jtest UTA: Test for delete(Integer, Integer, List)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ManagedContentDao#delete(Integer, Integer, List)
	 * @author patelpo
	 */
	@Test
	public void testDelete3() throws Throwable {
		// Given
		ManagedContentDao underTest = new ManagedContentDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		List<String> typeCodes = new ArrayList<String>(); // UTA: default value
		underTest.delete(submissionId, contractId, typeCodes);

	}

	/**
	 * Parasoft Jtest UTA: Test for deleteAll(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ManagedContentDao#deleteAll(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll2() throws Throwable {
		// Given
		ManagedContentDao underTest = new ManagedContentDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for insert(Integer, Integer, Integer, Timestamp, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ManagedContentDao#insert(Integer, Integer, Integer, Timestamp, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsert2() throws Throwable {
		// Given
		ManagedContentDao underTest = new ManagedContentDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp timestamp = mock(Timestamp.class);
		Collection<ManagedContent> contents = new ArrayList<ManagedContent>(); // UTA: default value
		underTest.insert(submissionId, contractId, userProfileId, timestamp, contents);

	}

	/**
	 * Parasoft Jtest UTA: Test for read(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ManagedContentDao#read(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testRead2() throws Throwable {
		// Given
		ManagedContentDao underTest = new ManagedContentDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		List<ManagedContent> result = underTest.read(submissionId, contractId, userProfileId);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
}