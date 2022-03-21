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
import com.intware.dao.jdbc.StoredProcedureHandler;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.valueobject.ActivitySummary;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.loan.valueobject.LoanActivitySummary;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.util.JdbcHelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for DistributionAddressDao
 *
 * @see com.manulife.pension.service.distribution.dao.DistributionAddressDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, DistributionAddressDao.class })
@RunWith(PowerMockRunner.class)
public class DistributionAddressDaoTest {
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
	 * Parasoft Jtest UTA: Test for deleteAll(Integer, Integer, Integer, String)
	 *
	 * @see com.manulife.pension.service.distribution.dao.DistributionAddressDao#deleteAll(Integer, Integer, Integer, String)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll() throws Throwable {

		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		DistributionAddressDao underTest = new DistributionAddressDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		String distributionTypeCode = "PA"; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId, distributionTypeCode);

	}

	@Test(expected = DistributionServiceDaoException.class)
	public void testDeleteAll_exception() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenThrow(new DAOException());
		// Given
		DistributionAddressDao underTest = new DistributionAddressDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		String distributionTypeCode = ""; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId, distributionTypeCode);

	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testDeleteAll_exception1() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenThrow(new DAOException());
		// Given
		DistributionAddressDao underTest = new DistributionAddressDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		String distributionTypeCode = "PA"; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId, distributionTypeCode);

	}

	/**
	 * Parasoft Jtest UTA: Test for insertPayeeAddress(Integer, Integer, Integer, Integer, Integer, DistributionAddress)
	 *
	 * @see com.manulife.pension.service.distribution.dao.DistributionAddressDao#insertPayeeAddress(Integer, Integer, Integer, Integer, Integer, DistributionAddress)
	 * @author patelpo
	 */
	@Test
	public void testInsertPayeeAddress() throws Throwable {
		// Given
		DistributionAddressDao underTest = new DistributionAddressDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Integer recipientNo = 0; // UTA: default value
		Integer payeeNo = 0; // UTA: default value
		DistributionAddress addresses = mockDistributionAddress();
		underTest.insertPayeeAddress(submissionId, contractId, userProfileId, recipientNo, payeeNo, addresses);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress() throws Throwable {
		DistributionAddress addresses = mock(DistributionAddress.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(addresses.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(addresses.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(addresses.getCity()).thenReturn(getCityResult);

		String getCountryCodeResult = ""; // UTA: default value
		when(addresses.getCountryCode()).thenReturn(getCountryCodeResult);

		String getStateCodeResult = ""; // UTA: default value
		when(addresses.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(addresses.getZipCode()).thenReturn(getZipCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(addresses.isBlank()).thenReturn(isBlankResult);
		return addresses;
	}

	/**
	 * Parasoft Jtest UTA: Test for insertRecipientAddress(Integer, Integer, Integer, Integer, DistributionAddress)
	 *
	 * @see com.manulife.pension.service.distribution.dao.DistributionAddressDao#insertRecipientAddress(Integer, Integer, Integer, Integer, DistributionAddress)
	 * @author patelpo
	 */
	@Test
	public void testInsertRecipientAddress() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		DistributionAddressDao underTest = new DistributionAddressDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Integer recipientNo = 0; // UTA: default value
		DistributionAddress addresses = mockDistributionAddress2();
		underTest.insertRecipientAddress(submissionId, contractId, userProfileId, recipientNo, addresses);

	}
	@Test
	public void testInsertRecipientAddress_Exception() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);
		
		// Given
		DistributionAddressDao underTest = new DistributionAddressDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Integer recipientNo = 0; // UTA: default value
		DistributionAddress addresses = null;
		underTest.insertRecipientAddress(submissionId, contractId, userProfileId, recipientNo, addresses);
		
	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testInsertRecipientAddress_Exception1() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenThrow(new DAOException());
		
		// Given
		DistributionAddressDao underTest = new DistributionAddressDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Integer recipientNo = 0; // UTA: default value
		DistributionAddress addresses = mockDistributionAddress2();
		underTest.insertRecipientAddress(submissionId, contractId, userProfileId, recipientNo, addresses);
		
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress2() throws Throwable {
		DistributionAddress addresses = mock(DistributionAddress.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(addresses.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(addresses.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(addresses.getCity()).thenReturn(getCityResult);

		String getCountryCodeResult = ""; // UTA: default value
		when(addresses.getCountryCode()).thenReturn(getCountryCodeResult);

		String getStateCodeResult = ""; // UTA: default value
		when(addresses.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(addresses.getZipCode()).thenReturn(getZipCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(addresses.isBlank()).thenReturn(isBlankResult);
		return addresses;
	}

	/**
	 * Parasoft Jtest UTA: Test for select(Integer, Integer, Integer, String, Class)
	 *
	 * @see com.manulife.pension.service.distribution.dao.DistributionAddressDao#select(Integer, Integer, Integer, String, Class)
	 * @author patelpo
	 */
	@Test
	public void testSelect() throws Throwable {
		List<DistributionAddress> returnList = new ArrayList<DistributionAddress>();
		DistributionAddress address = new Address();
		returnList.add(address);
		
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(returnList);

		// Given
		DistributionAddressDao underTest = new DistributionAddressDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		String distributionTypeCode = ""; // UTA: default value
		Class addressType = Class.forName("com.manulife.pension.service.distribution.valueobject.DistributionAddress"); // UTA: default value
		List<? extends DistributionAddress> result = underTest.select(submissionId, contractId, userProfileId,
				distributionTypeCode, addressType);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testSelect_Exception() throws Throwable {
		List<DistributionAddress> returnList = new ArrayList<DistributionAddress>();
		DistributionAddress address = new Address();
		returnList.add(address);
		
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		DistributionAddressDao underTest = new DistributionAddressDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		String distributionTypeCode = ""; // UTA: default value
		Class addressType = Class.forName("com.manulife.pension.service.distribution.valueobject.DistributionAddress"); // UTA: default value
		List<? extends DistributionAddress> result = underTest.select(submissionId, contractId, userProfileId,
				distributionTypeCode, addressType);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
}