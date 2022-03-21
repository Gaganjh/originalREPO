/**
 * 
 */
package com.manulife.pension.service.withdrawal.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.EJBException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.withdrawal.exception.WithdrawalDaoException;
import com.manulife.pension.service.withdrawal.valueobject.LegaleseInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.util.JdbcHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalLegaleseDao
 *
 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalLegaleseDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, WithdrawalRequest.class, WithdrawalLegaleseDao.class })
@RunWith(PowerMockRunner.class)
public class WithdrawalLegaleseDaoTest {
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
	 * Parasoft Jtest UTA: Test for getLegaleseTextVersion(LegaleseInfo)
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalLegaleseDao#getLegaleseTextVersion(LegaleseInfo)
	 * @author patelpo
	 */
	@Test
	public void testGetLegaleseTextVersion() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		WithdrawalLegaleseDao underTest = new WithdrawalLegaleseDao();

		// When
		LegaleseInfo info = mockLegaleseInfo();
		Integer result = underTest.getLegaleseTextVersion(info);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.intValue());
	}
	@Test(expected = SystemException.class )
	public void testGetLegaleseTextVersion_Exception() throws Throwable {
		when(getConnectionResult.prepareStatement(anyString())).thenThrow(new SQLException());

		// Given
		WithdrawalLegaleseDao underTest = new WithdrawalLegaleseDao();

		// When
		LegaleseInfo info = mockLegaleseInfo();
		Integer result = underTest.getLegaleseTextVersion(info);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.intValue());
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo() throws Throwable {
		LegaleseInfo info = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult = ""; // UTA: default value
		when(info.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContentIdResult = 0; // UTA: default value
		when(info.getContentId()).thenReturn(getContentIdResult);

		String getLegaleseTextResult = ""; // UTA: default value
		when(info.getLegaleseText()).thenReturn(getLegaleseTextResult);
		return info;
	}

	/**
	 * Parasoft Jtest UTA: Test for insertWithdrawalLegaleseInfo(int, WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalLegaleseDao#insertWithdrawalLegaleseInfo(int, WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testInsertWithdrawalLegaleseInfo() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(new Integer(10));

		// Given
		WithdrawalLegaleseDao underTest = new WithdrawalLegaleseDao();

		// When
		int submissionId = 0; // UTA: default value
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest();
		underTest.insertWithdrawalLegaleseInfo(submissionId, withdrawalRequest);

	}

	
	 
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo2() throws Throwable {
		LegaleseInfo getLegaleseInfoResult = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult2 = ""; // UTA: default value
		when(getLegaleseInfoResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult2);

		Integer getContentIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getContentId()).thenReturn(getContentIdResult);

		Integer getCreatorUserProfileIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult);

		String getLegaleseTextResult = ""; // UTA: default value
		when(getLegaleseInfoResult.getLegaleseText()).thenReturn(getLegaleseTextResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return getLegaleseInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo3() throws Throwable {
		LegaleseInfo getParticipantLegaleseInfoResult = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult3 = ""; // UTA: default value
		when(getParticipantLegaleseInfoResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult3);

		Integer getContentIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getContentId()).thenReturn(getContentIdResult2);

		Integer getCreatorUserProfileIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult2);

		String getLegaleseTextResult2 = ""; // UTA: default value
		when(getParticipantLegaleseInfoResult.getLegaleseText()).thenReturn(getLegaleseTextResult2);

		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);
		return getParticipantLegaleseInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		String getCmaSiteCodeResult = ""; // UTA: default value
		when(withdrawalRequest.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(withdrawalRequest.getContractId()).thenReturn(getContractIdResult);

		LegaleseInfo getLegaleseInfoResult = mockLegaleseInfo2();
		when(withdrawalRequest.getLegaleseInfo()).thenReturn(getLegaleseInfoResult);

		LegaleseInfo getParticipantLegaleseInfoResult = mockLegaleseInfo3();
		when(withdrawalRequest.getParticipantLegaleseInfo()).thenReturn(getParticipantLegaleseInfoResult);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(withdrawalRequest.getSubmissionId()).thenReturn(getSubmissionIdResult3);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Test for updateLegaleseInfo(int, int, LegaleseInfo)
	 *
	 * @see com.manulife.pension.service.withdrawal.dao.WithdrawalLegaleseDao#updateLegaleseInfo(int, int, LegaleseInfo)
	 * @author patelpo
	 */
	@Test
	public void testUpdateLegaleseInfo() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(new Integer(1));

		// Given
		WithdrawalLegaleseDao underTest = new WithdrawalLegaleseDao();

		// When
		int contractId = 0; // UTA: default value
		int submissionId = 0; // UTA: default value
		LegaleseInfo legaleseInfo = mockLegaleseInfo4();
		underTest.updateLegaleseInfo(contractId, submissionId, legaleseInfo);

	}
	@Test
	public void testUpdateLegaleseInfo1() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(new Integer(1));
		
		// Given
		WithdrawalLegaleseDao underTest = new WithdrawalLegaleseDao();
		
		// When
		int contractId = 0; // UTA: default value
		int submissionId = 0; // UTA: default value
		LegaleseInfo legaleseInfo = null;
		underTest.updateLegaleseInfo(contractId, submissionId, legaleseInfo);
		
	}
	@Test(expected = SystemException.class)
	public void testUpdateLegaleseInfo_Exception() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		WithdrawalLegaleseDao underTest = new WithdrawalLegaleseDao();

		// When
		int contractId = 0; // UTA: default value
		int submissionId = 0; // UTA: default value
		LegaleseInfo legaleseInfo = mockLegaleseInfo4();
		underTest.updateLegaleseInfo(contractId, submissionId, legaleseInfo);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo4() throws Throwable {
		LegaleseInfo legaleseInfo = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult = ""; // UTA: default value
		when(legaleseInfo.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContentIdResult = 0; // UTA: default value
		when(legaleseInfo.getContentId()).thenReturn(getContentIdResult);

		Integer getCreatorUserProfileIdResult = 0; // UTA: default value
		when(legaleseInfo.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult);

		String getLegaleseTextResult = ""; // UTA: default value
		when(legaleseInfo.getLegaleseText()).thenReturn(getLegaleseTextResult);
		return legaleseInfo;
	}
}