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
import com.manulife.pension.service.distribution.valueobject.Note;
import com.manulife.pension.service.loan.valueobject.LoanNote;
import com.manulife.pension.util.JdbcHelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
 * Parasoft Jtest UTA: Test class for NoteDao
 *
 * @see com.manulife.pension.service.distribution.dao.NoteDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, NoteDao.class })
@RunWith(PowerMockRunner.class)
public class NoteDaoTest {
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
	 * Parasoft Jtest UTA: Test for deleteAll(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.distribution.dao.NoteDao#deleteAll(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		NoteDao underTest = new NoteDao();

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
		NoteDao underTest = new NoteDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for deleteNoteType(Integer, Integer, Integer, String)
	 *
	 * @see com.manulife.pension.service.distribution.dao.NoteDao#deleteNoteType(Integer, Integer, Integer, String)
	 * @author patelpo
	 */
	@Test
	public void testDeleteNoteType() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		NoteDao underTest = new NoteDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		String noteTypeCode = ""; // UTA: default value
		underTest.deleteNoteType(submissionId, contractId, userProfileId, noteTypeCode);

	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testDeleteNoteType_Exception() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		NoteDao underTest = new NoteDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		String noteTypeCode = ""; // UTA: default value
		underTest.deleteNoteType(submissionId, contractId, userProfileId, noteTypeCode);

	}

	/**
	 * Parasoft Jtest UTA: Test for insert(Integer, Integer, Integer, Note)
	 *
	 * @see com.manulife.pension.service.distribution.dao.NoteDao#insert(Integer, Integer, Integer, Note)
	 * @author patelpo
	 */
	@Test
	public void testInsert() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		NoteDao underTest = new NoteDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Note note = mockNote();
		underTest.insert(submissionId, contractId, userProfileId, note);

	}
	@Test
	public void testInsert_1() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		NoteDao underTest = new NoteDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Note note = null;
		underTest.insert(submissionId, contractId, userProfileId, note);

	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testInsert_Exception() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		NoteDao underTest = new NoteDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Note note = mockNote();
		underTest.insert(submissionId, contractId, userProfileId, note);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Note
	 */
	private static Note mockNote() throws Throwable {
		Note note = mock(Note.class);
		String getNoteResult = ""; // UTA: default value
		when(note.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(note.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(note.isBlank()).thenReturn(isBlankResult);
		return note;
	}

	/**
	 * Parasoft Jtest UTA: Test for select(Integer, Class)
	 *
	 * @see com.manulife.pension.service.distribution.dao.NoteDao#select(Integer, Class)
	 * @author patelpo
	 */
	@Test
	public void testSelect() throws Throwable {
		List<Note> returnList = new ArrayList<Note>();
		Note note = new LoanNote();
		returnList.add(note);
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(returnList);

		// Given
		NoteDao underTest = new NoteDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Class noteType = Class.forName("com.manulife.pension.service.distribution.valueobject.Note"); // UTA: default value
		List<? extends Note> result = underTest.select(submissionId, noteType);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testSelect_Expected() throws Throwable {
		List<Note> returnList = new ArrayList<Note>();
		Note note = new LoanNote();
		returnList.add(note);
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		NoteDao underTest = new NoteDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Class noteType = Class.forName("com.manulife.pension.service.distribution.valueobject.Note"); // UTA: default value
		List<? extends Note> result = underTest.select(submissionId, noteType);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
}