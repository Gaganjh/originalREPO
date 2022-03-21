/**
 * 
 */
package com.manulife.pension.service.distribution.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.powermock.api.mockito.PowerMockito.when;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.StoredProcedureHandler;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.valueobject.ActivityDetail;
import com.manulife.pension.service.distribution.valueobject.ActivityDynamicDetail;
import com.manulife.pension.service.distribution.valueobject.ActivitySummary;
import com.manulife.pension.util.JdbcHelper;

import asposewobfuscated.ob;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * Parasoft Jtest UTA: Test class for ActivityDetailDao
 *
 * @see com.manulife.pension.service.distribution.dao.ActivityDetailDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, ActivityDetailDao.class })
@RunWith(PowerMockRunner.class)
public class ActivityDetailDaoTest {
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
	 * Parasoft Jtest UTA: Test for delete(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivityDetailDao#delete(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testDelete() throws Throwable {
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class);
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		Object[] obj = new Object[1];
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(obj);

		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDetail> activityDetails = new ArrayList<ActivityDetail>(); // UTA: default value
		ActivityDetail activityDetail = mock(ActivityDetail.class);
		activityDetails.add(activityDetail);
		underTest.delete(submissionId, contractId, userProfileId, activityDetails);

	}
	@Test
	public void testDelete_1() throws Throwable {
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class);
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		Object[] obj = new Object[1];
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(obj);
		
		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDetail> activityDetails = null;
		underTest.delete(submissionId, contractId, userProfileId, activityDetails);
		
	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testDelete_Exception() throws Throwable {
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class);
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		Object[] obj = new Object[1];
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenThrow(new DAOException());

		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDetail> activityDetails = new ArrayList<ActivityDetail>(); // UTA: default value
		ActivityDetail activityDetail = mock(ActivityDetail.class);
		activityDetails.add(activityDetail);
		underTest.delete(submissionId, contractId, userProfileId, activityDetails);

	}

	/**
	 * Parasoft Jtest UTA: Test for deleteAll(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivityDetailDao#deleteAll(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll() throws Throwable {
		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testDeleteAll_exception() throws Throwable {
		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();
		when(callableStatementResult.execute()).thenThrow(new SQLException());
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for insert(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivityDetailDao#insert(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsert() throws Throwable {
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class);
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		Object[] arg0 = new Object[1];
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(arg0);

		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDetail> activityDetails = new ArrayList<ActivityDetail>(); // UTA: default value
		ActivityDetail activityDetail = mock(ActivityDetail.class);
		activityDetails.add(activityDetail);
		
		underTest.insert(submissionId, contractId, userProfileId, activityDetails);

	}
	@Test
	public void testInsert_1() throws Throwable {
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class);
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		Object[] arg0 = new Object[1];
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(arg0);
		
		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDetail> activityDetails = null;
		
		underTest.insert(submissionId, contractId, userProfileId, activityDetails);
		
	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testInsert_Exception() throws Throwable {
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class);
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		Object[] arg0 = new Object[1];
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenThrow(new DAOException());

		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDetail> activityDetails = new ArrayList<ActivityDetail>(); // UTA: default value
		ActivityDetail activityDetail = mock(ActivityDetail.class);
		activityDetails.add(activityDetail);
		
		underTest.insert(submissionId, contractId, userProfileId, activityDetails);

	}
	

	/**
	 * Parasoft Jtest UTA: Test for insertUpdate(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivityDetailDao#insertUpdate(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsertUpdate() throws Throwable {
		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();

		// When
		
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Collection<ActivityDetail> details = new ArrayList<ActivityDetail>(); // UTA: default value
		ActivityDetail activityDetail= mock(ActivityDetail.class);
		details.add(activityDetail);
		
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		Object a[]= new Object[1];
		
		Object[] obj = new Object[1];
		obj[0] =a;
		
		
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(obj);
		Class activityDetailType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.ActivityDetail"); // UTA: default value
		underTest.insertUpdate(submissionId, contractId, userProfileId, details, activityDetailType);

	}
	
	@Test
	public void testInsertUpdate_1() throws Throwable {
		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();
		
		// When
		
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Collection<ActivityDetail> details = new ArrayList<ActivityDetail>(); // UTA: default value
		ActivityDetail activityDetail= mock(ActivityDetail.class);
		details.add(activityDetail);
		
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		
		
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(null);
		
		Class activityDetailType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.ActivityDetail"); // UTA: default value
		underTest.insertUpdate(submissionId, contractId, userProfileId, details, activityDetailType);
		
	}
	@Test
	public void testInsertUpdate_2() throws Throwable {
		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();

		// When
		
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Collection<ActivityDetail> details = null;
		
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		Object a[]= new Object[1];
		
		Object[] obj = new Object[1];
		obj[0] =a;
		
		
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(obj);
		Class activityDetailType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.ActivityDetail"); // UTA: default value
		underTest.insertUpdate(submissionId, contractId, userProfileId, details, activityDetailType);

	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testInsertUpdate_Exception() throws Throwable {
		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();

		// When
		
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Collection<ActivityDetail> details = new ArrayList<ActivityDetail>(); // UTA: default value
		ActivityDetail activityDetail= mock(ActivityDetail.class);
		details.add(activityDetail);
		
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenThrow(new DAOException());
		
		Class activityDetailType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.ActivityDetail"); // UTA: default value
		underTest.insertUpdate(submissionId, contractId, userProfileId, details, activityDetailType);

	}

	/**
	 * Parasoft Jtest UTA: Test for insertUpdateIfChanged(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivityDetailDao#insertUpdateIfChanged(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
/*	@Test
	public void testInsertUpdateIfChanged() throws Throwable {
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class);
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		Object a[]= new Object[1];
		Object[] obj = new Object[1];
		obj[0] =a;
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(obj);

		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Collection<ActivityDetail> details = new ArrayList<ActivityDetail>(); // UTA: default value
		ActivityDetail activityDetail= mock(ActivityDetail.class);
		when(activityDetail.getItemNumber()).thenReturn(10);
		details.add(activityDetail);
		
		Class activityDetailType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.ActivityDetail"); // UTA: default value
		underTest.insertUpdateIfChanged(submissionId, contractId, userProfileId, details, activityDetailType);

	}
*/
	/**
	 * Parasoft Jtest UTA: Test for select(Integer, Integer, Integer, Class)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivityDetailDao#select(Integer, Integer, Integer, Class)
	 * @author patelpo
	 */
	@Test
	public void testSelect() throws Throwable {
		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Class activityDetailType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.ActivityDetail"); // UTA: default value
		List<? extends ActivityDetail> result = underTest.select(submissionId, contractId, userProfileId,
				activityDetailType);

		// Then
		 assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
	@Test(expected=DistributionServiceDaoException.class)
	public void testSelect_exception() throws Throwable {
		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();
		when(callableStatementResult.execute()).thenThrow(new SQLException());

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Class activitySummaryType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.ActivitySummary"); // UTA: default value
		List<? extends ActivitySummary> result = underTest.select(submissionId, contractId, userProfileId,
				activitySummaryType);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Test for selectSystemOfRecord(Integer, Integer, Integer, Class)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivityDetailDao#selectSystemOfRecord(Integer, Integer, Integer, Class)
	 * @author patelpo
	 */
	@Test
	public void testSelectSystemOfRecord() throws Throwable {
		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer submissionId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Class activityDetailType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.ActivityDetail"); // UTA: default value
		List<? extends ActivityDetail> result = underTest.selectSystemOfRecord(contractId, submissionId, userProfileId,
				activityDetailType);

		// Then
		 assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
	@Test(expected=DistributionServiceDaoException.class)
	public void testSelectSystemOfRecord_exception() throws Throwable {
		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();
		when(callableStatementResult.execute()).thenThrow(new SQLException());

		// When
				Integer contractId = 0; // UTA: default value
				Integer submissionId = 0; // UTA: default value
				Integer userProfileId = 0; // UTA: default value
				Class activityDetailType = Class
						.forName("com.manulife.pension.service.distribution.valueobject.ActivityDetail"); // UTA: default value
				List<? extends ActivityDetail> result = underTest.selectSystemOfRecord(contractId, submissionId, userProfileId,
						activityDetailType);


		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Test for update(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivityDetailDao#update(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testUpdate() throws Throwable {
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class);
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		Object[] obj = new Object[1];
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(obj);

		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDetail> activityDetails = new ArrayList<ActivityDetail>(); // UTA: default value
		ActivityDetail activityDetail= mock(ActivityDetail.class);
		activityDetails.add(activityDetail);
		underTest.update(submissionId, contractId, userProfileId, activityDetails);

	}
	@Test
	public void testUpdate_1() throws Throwable {
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class);
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		Object[] obj = new Object[1];
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(obj);
		
		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDetail> activityDetails = null;
		underTest.update(submissionId, contractId, userProfileId, activityDetails);
		
	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testUpdate_Exception() throws Throwable {
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class);
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		Object[] obj = new Object[1];
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenThrow(new DAOException());
		
		// Given
		ActivityDetailDao underTest = new ActivityDetailDao();
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDetail> activityDetails = new ArrayList<ActivityDetail>(); // UTA: default value
		ActivityDetail activityDetail= mock(ActivityDetail.class);
		activityDetails.add(activityDetail);
		underTest.update(submissionId, contractId, userProfileId, activityDetails);
		
	}
}