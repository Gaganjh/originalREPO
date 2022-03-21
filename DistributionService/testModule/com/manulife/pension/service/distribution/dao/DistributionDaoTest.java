/**
 * 
 */
package com.manulife.pension.service.distribution.dao;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
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
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.util.JdbcHelper;

/**
 * Parasoft Jtest UTA: Test class for DistributionDao
 *
 * @see com.manulife.pension.service.distribution.dao.DistributionDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class,  Logger.class })
@RunWith(PowerMockRunner.class)
public class DistributionDaoTest {
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
	 * Parasoft Jtest UTA: Test for expireDistributionRequest(Integer, Integer, String, Timestamp)
	 *
	 * @see com.manulife.pension.service.distribution.dao.DistributionDao#expireDistributionRequest(Integer, Integer, String, Timestamp)
	 * @author patelpo
	 */
	@Test
	public void testExpireDistributionRequest() throws Throwable {
		// Given
		DistributionDao underTest = new DistributionDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		String expiredStatusCode = ""; // UTA: default value
		Timestamp lastUpdatedTs = mock(Timestamp.class);
		boolean result = underTest.expireDistributionRequest(submissionId, userProfileId, expiredStatusCode,
				lastUpdatedTs);

		// Then
		// assertFalse(result);
	}
	@Test(expected=SystemException.class)
	public void testExpireDistributionRequest_exception() throws Throwable {
		// Given
		when(getConnectionResult.prepareStatement(anyString())).thenThrow(new SQLException());
		
		DistributionDao underTest = new DistributionDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		String expiredStatusCode = ""; // UTA: default value
		Timestamp lastUpdatedTs = mock(Timestamp.class);
		boolean result = underTest.expireDistributionRequest(submissionId, userProfileId, expiredStatusCode,
				lastUpdatedTs);
	}

}