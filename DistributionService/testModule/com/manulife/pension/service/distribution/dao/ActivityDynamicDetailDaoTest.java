/**
 * 
 */
package com.manulife.pension.service.distribution.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.intware.dao.jdbc.StoredProcedureHandler;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.valueobject.ActivityDynamicDetail;
import com.manulife.pension.util.JdbcHelper;

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

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for ActivityDynamicDetailDao
 *
 * @see com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, ActivityDynamicDetailDao.class })
@RunWith(PowerMockRunner.class)
public class ActivityDynamicDetailDaoTest {
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
	 * @see com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao#delete(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testDelete() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDynamicDetail> activityDynamicDetails = new ArrayList<ActivityDynamicDetail>(); // UTA: default value
		ActivityDynamicDetail activityDynamicDetail = new ActivityDynamicDetail();
		activityDynamicDetail.setItemNumber(20);
		activityDynamicDetail.setSecondaryName("Test");
		activityDynamicDetail.setSecondaryNumber(20);
		activityDynamicDetail.setTypeCode("Test1");
		activityDynamicDetail.setValue("TestValue");
		activityDynamicDetails.add(activityDynamicDetail);
		underTest.delete(submissionId, contractId, userProfileId, activityDynamicDetails);

	}

	@Test
	public void testDelete_1() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDynamicDetail> activityDynamicDetails = null;
		underTest.delete(submissionId, contractId, userProfileId, activityDynamicDetails);

	}

	@Test(expected = DistributionServiceDaoException.class)
	public void testDelete_exception() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();
		when(callableStatementResult.execute()).thenThrow(new SQLException());
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDynamicDetail> activityDynamicDetails = new ArrayList<ActivityDynamicDetail>(); // UTA: default value
		ActivityDynamicDetail activityDynamicDetail = new ActivityDynamicDetail();
		activityDynamicDetail.setItemNumber(20);
		activityDynamicDetail.setSecondaryName("Test");
		activityDynamicDetail.setSecondaryNumber(20);
		activityDynamicDetail.setTypeCode("Test1");
		activityDynamicDetail.setValue("TestValue");
		activityDynamicDetails.add(activityDynamicDetail);
		underTest.delete(submissionId, contractId, userProfileId, activityDynamicDetails);

	}

	/**
	 * Parasoft Jtest UTA: Test for deleteAll(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao#deleteAll(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	@Test(expected = DistributionServiceDaoException.class)
	public void testDeleteAll_exception() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();
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
	 * @see com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao#insert(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsert() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Collection<ActivityDynamicDetail> activityDetails = new ArrayList<ActivityDynamicDetail>(); // UTA: default value
		ActivityDynamicDetail activityDynamicDetail = new ActivityDynamicDetail();
		activityDynamicDetail.setItemNumber(20);
		activityDynamicDetail.setSecondaryName("Test");
		activityDynamicDetail.setSecondaryNumber(20);
		activityDynamicDetail.setTypeCode("Test1");
		activityDynamicDetail.setValue("TestValue");
		activityDetails.add(activityDynamicDetail);

		underTest.insert(submissionId, contractId, userProfileId, activityDetails);

	}

	@Test
	public void testInsert_1() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Collection<ActivityDynamicDetail> activityDetails = null;

		underTest.insert(submissionId, contractId, userProfileId, activityDetails);

	}

	@Test(expected = DistributionServiceDaoException.class)
	public void testInsert_exception() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();
		when(callableStatementResult.execute()).thenThrow(new SQLException());
		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Collection<ActivityDynamicDetail> activityDetails = new ArrayList<ActivityDynamicDetail>(); // UTA: default value
		ActivityDynamicDetail activityDynamicDetail = new ActivityDynamicDetail();
		activityDynamicDetail.setItemNumber(20);
		activityDynamicDetail.setSecondaryName("Test");
		activityDynamicDetail.setSecondaryNumber(20);
		activityDynamicDetail.setTypeCode("Test1");
		activityDynamicDetail.setValue("TestValue");
		activityDetails.add(activityDynamicDetail);

		underTest.insert(submissionId, contractId, userProfileId, activityDetails);

	}

	/**
	 * Parasoft Jtest UTA: Test for insertUpdate(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao#insertUpdate(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsertUpdate() throws Throwable {
		Object obj=new Object();
		Object[] value=new Object[1];
		Object[] valueObj=new Object[2];
		value[0]=obj;
		valueObj[0]=value; 
		
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(valueObj);

		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDynamicDetail> details = new ArrayList<ActivityDynamicDetail>(); // UTA: default value
		ActivityDynamicDetail activityDynamicDetail=new ActivityDynamicDetail();
		activityDynamicDetail.setItemNumber(20);
		activityDynamicDetail.setSecondaryName("Test");
		activityDynamicDetail.setSecondaryNumber(20);
		activityDynamicDetail.setTypeCode("Test1");
		activityDynamicDetail.setValue("TestValue");
		details.add(activityDynamicDetail);
		underTest.insertUpdate(submissionId, contractId, userProfileId, details);

	}
	@Test
	public void testInsertUpdate_1() throws Throwable {
		
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(null);

		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDynamicDetail> details = new ArrayList<ActivityDynamicDetail>(); // UTA: default value
		ActivityDynamicDetail activityDynamicDetail=new ActivityDynamicDetail();
		activityDynamicDetail.setItemNumber(20);
		activityDynamicDetail.setSecondaryName("Test");
		activityDynamicDetail.setSecondaryNumber(20);
		activityDynamicDetail.setTypeCode("Test1");
		activityDynamicDetail.setValue("TestValue");
		details.add(activityDynamicDetail);
		underTest.insertUpdate(submissionId, contractId, userProfileId, details);

	}
	@Test
	public void testInsertUpdate_2() throws Throwable {
		
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(null);

		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDynamicDetail> details = null;
		underTest.insertUpdate(submissionId, contractId, userProfileId, details);

	}
	
	@Test(expected=DistributionServiceDaoException.class)
	public void testInsertUpdate_exception() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();
		when(callableStatementResult.execute()).thenThrow(new SQLException());

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Collection<ActivityDynamicDetail> details = new ArrayList<ActivityDynamicDetail>(); // UTA: default value
		ActivityDynamicDetail activityDynamicDetail=new ActivityDynamicDetail();
		activityDynamicDetail.setItemNumber(20);
		activityDynamicDetail.setSecondaryName("Test");
		activityDynamicDetail.setSecondaryNumber(20);
		activityDynamicDetail.setTypeCode("Test1");
		activityDynamicDetail.setValue("TestValue");
		details.add(activityDynamicDetail);
		underTest.insertUpdate(submissionId, contractId, userProfileId, details);

	}

	/**
	 * Parasoft Jtest UTA: Test for insertUpdateIfChanged(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao#insertUpdateIfChanged(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsertUpdateIfChanged() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		

		ActivityDynamicDetail obj=new ActivityDynamicDetail();
		obj.setItemNumber(20);
		obj.setSecondaryName("Test");
		obj.setSecondaryNumber(10);
		obj.setTypeCode("Test1");
		ActivityDynamicDetail[] value=new ActivityDynamicDetail[1];
		Object[] valueObj=new Object[1];
		value[0]=obj;
		valueObj[0]=value; 

		
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(valueObj);

		
		Collection<ActivityDynamicDetail> details = new ArrayList<ActivityDynamicDetail>(); // UTA: default value
		ActivityDynamicDetail activityDynamicDetail=new ActivityDynamicDetail();
		activityDynamicDetail.setItemNumber(20);
		activityDynamicDetail.setSecondaryName("Test");
		activityDynamicDetail.setSecondaryNumber(10);
		activityDynamicDetail.setTypeCode("Test1");
		activityDynamicDetail.setValue("TestValue");
		details.add(activityDynamicDetail);
		
		underTest.insertUpdateIfChanged(submissionId, contractId, userProfileId, details);

	}
	@Test
	public void testInsertUpdateIfChanged_1() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();
		

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		
		Collection<ActivityDynamicDetail> details = new ArrayList<ActivityDynamicDetail>(); // UTA: default value
		ActivityDynamicDetail activityDynamicDetail=new ActivityDynamicDetail();
		activityDynamicDetail.setItemNumber(20);
		activityDynamicDetail.setSecondaryName("Test");
		activityDynamicDetail.setSecondaryNumber(10);
		activityDynamicDetail.setTypeCode("Test1");
		activityDynamicDetail.setValue("TestValue");
		details.add(activityDynamicDetail);
		
		underTest.insertUpdateIfChanged(submissionId, contractId, userProfileId, details);

	}
	@Test
	public void testInsertUpdateIfChanged_2() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();
		

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		
		Collection<ActivityDynamicDetail> details = null;
		
		underTest.insertUpdateIfChanged(submissionId, contractId, userProfileId, details);

	}

	/**
	 * Parasoft Jtest UTA: Test for select(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao#select(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testSelect() throws Throwable {
		
		ActivityDynamicDetail obj=new ActivityDynamicDetail();
		obj.setSecondaryNumber(10);
		ActivityDynamicDetail[] value=new ActivityDynamicDetail[1];
		Object[] valueObj=new Object[1];
		value[0]=obj;
		valueObj[0]=value; 

		
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(valueObj);

		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();
		
		
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		List<ActivityDynamicDetail> result = underTest.select(submissionId, contractId, userProfileId);

		// Then
		 assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
	@Test(expected=DistributionServiceDaoException.class)
	public void testSelect_exception() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();
		when(callableStatementResult.execute()).thenThrow(new SQLException());
		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		List<ActivityDynamicDetail> result = underTest.select(submissionId, contractId, userProfileId);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Test for selectSystemOfRecord(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao#selectSystemOfRecord(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testSelectSystemOfRecord() throws Throwable {
		ActivityDynamicDetail obj=new ActivityDynamicDetail();
		ActivityDynamicDetail[] value=new ActivityDynamicDetail[1];
		Object[] valueObj=new Object[1];
		value[0]=obj;
		valueObj[0]=value; 

		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(valueObj);

		
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer submissionId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		List<ActivityDynamicDetail> result = underTest.selectSystemOfRecord(contractId, submissionId, userProfileId);

		// Then
		 assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
	@Test(expected=DistributionServiceDaoException.class)
	public void testSelectSystemOfRecord_exception() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();
		when(callableStatementResult.execute()).thenThrow(new SQLException());
		// When
		Integer contractId = 0; // UTA: default value
		Integer submissionId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		List<ActivityDynamicDetail> result = underTest.selectSystemOfRecord(contractId, submissionId, userProfileId);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Test for update(Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao#update(Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testUpdate() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Collection<ActivityDynamicDetail> activityDetails = new ArrayList<ActivityDynamicDetail>(); // UTA: default value
		ActivityDynamicDetail activityDynamicDetail=new ActivityDynamicDetail();
		activityDynamicDetail.setItemNumber(20);
		activityDynamicDetail.setSecondaryName("Test");
		activityDynamicDetail.setSecondaryNumber(20);
		activityDynamicDetail.setTypeCode("Test1");
		activityDynamicDetail.setValue("TestValue");
		activityDetails.add(activityDynamicDetail);
		underTest.update(submissionId, contractId, userProfileId, activityDetails);

	}
	@Test
	public void testUpdate_1() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();

		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Collection<ActivityDynamicDetail> activityDetails = null;
		underTest.update(submissionId, contractId, userProfileId, activityDetails);

	}
	@Test(expected=DistributionServiceDaoException.class)
	public void testUpdate_exception() throws Throwable {
		// Given
		ActivityDynamicDetailDao underTest = new ActivityDynamicDetailDao();
		when(callableStatementResult.execute()).thenThrow(new SQLException());
		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Collection<ActivityDynamicDetail> activityDetails = new ArrayList<ActivityDynamicDetail>(); // UTA: default value
		ActivityDynamicDetail activityDynamicDetail=new ActivityDynamicDetail();
		activityDynamicDetail.setItemNumber(20);
		activityDynamicDetail.setSecondaryName("Test");
		activityDynamicDetail.setSecondaryNumber(20);
		activityDynamicDetail.setTypeCode("Test1");
		activityDynamicDetail.setValue("TestValue");
		activityDetails.add(activityDynamicDetail);
		underTest.update(submissionId, contractId, userProfileId, activityDetails);

	}
	
}