/**
 * 
 */
package com.manulife.pension.service.distribution.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.intware.dao.jdbc.StoredProcedureHandler;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.valueobject.ActivityDynamicDetail;
import com.manulife.pension.service.distribution.valueobject.ActivitySummary;
import com.manulife.pension.service.loan.valueobject.LoanActivitySummary;
import com.manulife.pension.util.JdbcHelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for ActivitySummaryDao
 *
 * @see com.manulife.pension.service.distribution.dao.ActivitySummaryDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class})
@RunWith(PowerMockRunner.class)
public class ActivitySummaryDaoTest {
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
	 * Parasoft Jtest UTA: Test for delete(Integer, Integer, Integer, ActivitySummary)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivitySummaryDao#delete(Integer, Integer, Integer, ActivitySummary)
	 * @author patelpo
	 */
	@Test
	public void testDelete() throws Throwable {
		// Given
		ActivitySummaryDao underTest = new ActivitySummaryDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		ActivitySummary activitySummary = mockActivitySummary();
		underTest.delete(submissionId, contractId, userProfileId, activitySummary);

	}

	@Test
	public void testDelete_1() throws Throwable {
		// Given
		ActivitySummaryDao underTest = new ActivitySummaryDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		ActivitySummary activitySummary = null;
		underTest.delete(submissionId, contractId, userProfileId, activitySummary);

	}

	@Test(expected = DistributionServiceDaoException.class)
	public void testDelete_exception() throws Throwable {
		// Given
		ActivitySummaryDao underTest = new ActivitySummaryDao();
		when(callableStatementResult.execute()).thenThrow(new SQLException());
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		ActivitySummary activitySummary = mockActivitySummary();
		underTest.delete(submissionId, contractId, userProfileId, activitySummary);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ActivitySummary
	 */
	private static ActivitySummary mockActivitySummary() throws Throwable {
		ActivitySummary activitySummary = mock(ActivitySummary.class);
		String getStatusCodeResult = ""; // UTA: default value
		when(activitySummary.getStatusCode()).thenReturn(getStatusCodeResult);
		return activitySummary;
	}

	/**
	 * Parasoft Jtest UTA: Test for deleteAll(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivitySummaryDao#deleteAll(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll() throws Throwable {
		// Given
		ActivitySummaryDao underTest = new ActivitySummaryDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 20; // UTA: default value
		Integer userProfileId = 30; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testDeleteAll_exception() throws Throwable {
		// Given
		ActivitySummaryDao underTest = new ActivitySummaryDao();
		when(callableStatementResult.execute()).thenThrow(new SQLException());
		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 20; // UTA: default value
		Integer userProfileId = 30; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for insert(Integer, Integer, Integer, ActivitySummary)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivitySummaryDao#insert(Integer, Integer, Integer, ActivitySummary)
	 * @author patelpo
	 */
	@Test
	public void testInsert() throws Throwable {
		// Given
		ActivitySummaryDao underTest = new ActivitySummaryDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		ActivitySummary activitySummary = mockActivitySummary2();
		underTest.insert(submissionId, contractId, userProfileId, activitySummary);

	}
	@Test
	public void testInsert_1() throws Throwable {
		// Given
		ActivitySummaryDao underTest = new ActivitySummaryDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		ActivitySummary activitySummary = null;
		underTest.insert(submissionId, contractId, userProfileId, activitySummary);

	}

	@Test(expected = DistributionServiceDaoException.class)
	public void testInsert_exception() throws Throwable {
		// Given
		ActivitySummaryDao underTest = new ActivitySummaryDao();
		when(callableStatementResult.execute()).thenThrow(new SQLException());

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		ActivitySummary activitySummary = mockActivitySummary2();
		underTest.insert(submissionId, contractId, userProfileId, activitySummary);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		int getNanosResult = 0; // UTA: default value
		when(getCreatedResult.getNanos()).thenReturn(getNanosResult);

		long getTimeResult = 0L; // UTA: default value
		when(getCreatedResult.getTime()).thenReturn(getTimeResult);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ActivitySummary
	 */
	private static ActivitySummary mockActivitySummary2() throws Throwable {
		ActivitySummary activitySummary = mock(ActivitySummary.class);
		Timestamp getCreatedResult = mockTimestamp();
		when(activitySummary.getCreated()).thenReturn(getCreatedResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(activitySummary.getStatusCode()).thenReturn(getStatusCodeResult);
		return activitySummary;
	}

	/**
	 * Parasoft Jtest UTA: Test for select(Integer, Integer, Integer, Class)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivitySummaryDao#select(Integer, Integer, Integer, Class)
	 * @author patelpo
	 */
	@Test
	public void testSelect() throws Throwable {
		Object[] templist=new Object[1];
		templist[0]=new LoanActivitySummary();
	    
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(templist);
		// Given
		
		
		
		ActivitySummaryDao underTest = new ActivitySummaryDao();
		

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 20; // UTA: default value
		Integer userProfileId = 30; // UTA: default value
		Class activitySummaryType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.ActivitySummary"); // UTA: default value
		List<? extends ActivitySummary> result = underTest.select(submissionId, contractId, userProfileId,
				activitySummaryType);

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(true);
		
		
		
		// Then
		 assertNotNull(result);
	}
	
	@Test(expected=DistributionServiceDaoException.class)
	public void testSelect_exception() throws Throwable {
		// Given
		ActivitySummaryDao underTest = new ActivitySummaryDao();
		when(callableStatementResult.execute()).thenThrow(new SQLException());

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 20; // UTA: default value
		Integer userProfileId = 30; // UTA: default value
		Class activitySummaryType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.ActivitySummary"); // UTA: default value
		List<? extends ActivitySummary> result = underTest.select(submissionId, contractId, userProfileId,
				activitySummaryType);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
	
}