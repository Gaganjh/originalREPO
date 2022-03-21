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
import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.loan.valueobject.LoanDeclaration;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;
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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for DeclarationDao
 *
 * @see com.manulife.pension.service.distribution.dao.DeclarationDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, DeclarationDao.class })
@RunWith(PowerMockRunner.class)
public class DeclarationDaoTest {
	DataSource getCachedDataSourceResult = mock(DataSource.class);
	Connection getConnectionResult = mock(Connection.class);
	PreparedStatement prepareStatementResult = mock(PreparedStatement.class);
	Statement statementResult = mock(Statement.class);
	CallableStatement callableStatementResult = mock(CallableStatement.class);
	ResultSet executeQueryResult = mock(ResultSet.class);

	@Before
	public void setUp() throws Exception {
		spy(JdbcHelper.class);

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
	 * @see com.manulife.pension.service.distribution.dao.DeclarationDao#delete(Integer, Integer, List)
	 * @author patelpo
	 */
	@Test
	public void testDelete() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		DeclarationDao underTest = new DeclarationDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		List<String> typeCodes = new ArrayList<String>(); // UTA: default value
		typeCodes.add("TypeCode");
		underTest.delete(submissionId, contractId, typeCodes);

	}

	@Test(expected = DistributionServiceDaoException.class)
	public void testDelete_Exception1() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenThrow(new DAOException());

		// Given
		DeclarationDao underTest = new DeclarationDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		List<String> typeCodes = new ArrayList<String>(); // UTA: default value
		typeCodes.add("TypeCode");
		underTest.delete(submissionId, contractId, typeCodes);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testDelete_Exception() throws Throwable {
		// Given
		DeclarationDao underTest = new DeclarationDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		List<String> typeCodes = new ArrayList<String>(); // UTA: default value
		typeCodes.add("TypeCode");
		typeCodes.add("TypeCode1");
		typeCodes.add("TypeCode2");
		underTest.delete(submissionId, contractId, typeCodes);

	}

	@Test
	public void testDelete_1() throws Throwable {
		// Given
		DeclarationDao underTest = new DeclarationDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		List<String> typeCodes = new ArrayList<String>(); // UTA: default value
		underTest.delete(submissionId, contractId, typeCodes);

	}

	/**
	 * Parasoft Jtest UTA: Test for deleteAll(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.distribution.dao.DeclarationDao#deleteAll(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		DeclarationDao underTest = new DeclarationDao();

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
		DeclarationDao underTest = new DeclarationDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for insert(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.DeclarationDao#insert(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsert() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		DeclarationDao underTest = new DeclarationDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Declaration> declarations = new ArrayList<Declaration>();
		WithdrawalRequestDeclaration with=new WithdrawalRequestDeclaration();
		with.setTypeCode("12");
		declarations.add(with);
		
		underTest.insert(submissionId, contractId, userProfileId, declarations);

	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testInsert_Exception() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenThrow(new DAOException());
		
		// Given
		DeclarationDao underTest = new DeclarationDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<Declaration> declarations = new ArrayList<Declaration>();
		WithdrawalRequestDeclaration with=new WithdrawalRequestDeclaration();
		with.setTypeCode("12");
		declarations.add(with);
		
		underTest.insert(submissionId, contractId, userProfileId, declarations);
		
	}
	@Test
	public void testInsert_1() throws Throwable {
		// Given
		DeclarationDao underTest = new DeclarationDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<? extends Declaration> declarations = null; // UTA: default value
		underTest.insert(submissionId, contractId, userProfileId, declarations);
		
	}

	/**
	 * Parasoft Jtest UTA: Test for select(Integer, Integer, Integer, Class)
	 *
	 * @see com.manulife.pension.service.distribution.dao.DeclarationDao#select(Integer, Integer, Integer, Class)
	 * @author patelpo
	 */
	@Test
	public void testSelect() throws Throwable {
		List<Declaration> returnList = new ArrayList<Declaration>();
		Declaration declaration = new LoanDeclaration();
		returnList.add(declaration);
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(returnList);

		// Given
		DeclarationDao underTest = new DeclarationDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Class declarationType = Class.forName("com.manulife.pension.service.distribution.valueobject.Declaration"); // UTA: default value
		List<? extends Declaration> result = underTest.select(submissionId, contractId, userProfileId, declarationType);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testSelect_Exception() throws Throwable {
		List<Declaration> returnList = new ArrayList<Declaration>();
		Declaration declaration = new LoanDeclaration();
		returnList.add(declaration);
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		DeclarationDao underTest = new DeclarationDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Class declarationType = Class.forName("com.manulife.pension.service.distribution.valueobject.Declaration"); // UTA: default value
		List<? extends Declaration> result = underTest.select(submissionId, contractId, userProfileId, declarationType);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
}