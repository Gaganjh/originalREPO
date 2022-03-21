/**
 * 
 */
package com.manulife.pension.service.loan.dao;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.intware.dao.jdbc.SelectBeanArrayQueryHandler;
import com.intware.dao.jdbc.SelectSingleOrNoValueQueryHandler;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.view.ContentText;
import com.manulife.pension.util.JdbcHelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for ContentDao
 *
 * @see com.manulife.pension.service.loan.dao.ContentDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, ContentDao.class })
@RunWith(PowerMockRunner.class)
public class ContentDaoTest {
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
	 * Parasoft Jtest UTA: Test for getContentTextById(String, String, int)
	 *
	 * @see com.manulife.pension.service.loan.dao.ContentDao#getContentTextById(String, String, int)
	 * @author patelpo
	 */
	@Test
	public void testGetContentTextById() throws Throwable {
		Location loc = mock(Location.class);
		ContentText context1 = new ContentText(3, loc, "CONTEXTTT", "TEXT");
		ContentText context2 = new ContentText(4, loc, "HHH", "rrr");
		ContentText[] contextArray = new ContentText[2];
		contextArray[0] = context1;
		contextArray[1] = context2;
		SelectBeanArrayQueryHandler newSelectBeanArrayQueryHandlerResult = mock(SelectBeanArrayQueryHandler.class); // UTA: default value
		whenNew(SelectBeanArrayQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanArrayQueryHandlerResult);
		when(newSelectBeanArrayQueryHandlerResult.select(any(Object[].class))).thenReturn(contextArray);
		spy(Arrays.class);

		List<ContentText> asListResult = new ArrayList<ContentText>(); // UTA: default value
		doReturn(asListResult).when(Arrays.class);
		Arrays.asList((Object[]) any());

		// Given
		ContentDao underTest = new ContentDao();

		// When
		String cmaJndiName = "Test"; // UTA: default value
		String cmaSchemaName = "Test"; // UTA: default value
		int contentId = 10; // UTA: default value
		Set<ContentText> result = underTest.getContentTextById(cmaJndiName, cmaSchemaName, contentId);

		// Then
		assertNotNull(result);
		//assertEquals(2, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Test for getContentTextByKey(String, String, int, boolean, boolean)
	 *
	 * @see com.manulife.pension.service.loan.dao.ContentDao#getContentTextByKey(String, String, int, boolean, boolean)
	 * @author patelpo
	 */
	@Test
	public void testGetContentTextByKey() throws Throwable {
		SelectSingleOrNoValueQueryHandler newSelectSingleOrNoValueQueryHandlerResult = mock(SelectSingleOrNoValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleOrNoValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleOrNoValueQueryHandlerResult);
		
		Integer arg1 = mock(Integer.class);
		when(newSelectSingleOrNoValueQueryHandlerResult.select(any(Object[].class))).thenReturn(arg1);
		
		SelectBeanArrayQueryHandler newSelectBeanArrayQueryHandlerResult = mock(SelectBeanArrayQueryHandler.class); // UTA: default value
		whenNew(SelectBeanArrayQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanArrayQueryHandlerResult);
		ContentText[] contentText = new ContentText[0];
		when(newSelectBeanArrayQueryHandlerResult.select(any(Object[].class))).thenReturn(contentText);

		// Given
		ContentDao underTest = new ContentDao();

		// When
		String cmaJndiName = ""; // UTA: default value
		String cmaSchemaName = ""; // UTA: default value
		int key = 0; // UTA: default value
		boolean isLive = false; // UTA: default value
		boolean isEnabled = false; // UTA: default value
		Set<ContentText> result = underTest.getContentTextByKey(cmaJndiName, cmaSchemaName, key, isLive, isEnabled);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Test for getNyContentText(Set)
	 *
	 * @see com.manulife.pension.service.loan.dao.ContentDao#getNyContentText(Set)
	 * @author patelpo
	 */
	@Test
	public void testGetNyContentText() throws Throwable {
		// Given
		ContentDao underTest = new ContentDao();

		// When
		Set<ContentText> contentTextSet = new HashSet<ContentText>(); // UTA: default value
		ContentText contentText = new ContentText();
		contentText.setContext("NEW_YORK");
		contentTextSet.add(contentText);
		Set<ContentText> result = underTest.getNyContentText(contentTextSet);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Test for getUsContentText(Set)
	 *
	 * @see com.manulife.pension.service.loan.dao.ContentDao#getUsContentText(Set)
	 * @author patelpo
	 */
	@Test
	public void testGetUsContentText() throws Throwable {
		// Given
		ContentDao underTest = new ContentDao();

		// When
		Set<ContentText> contentTextSet = new HashSet<ContentText>(); // UTA: default value
		ContentText contentText = new ContentText();
		contentText.setContext("Abbreviation: USA");
		contentTextSet.add(contentText);
		Set<ContentText> result = underTest.getUsContentText(contentTextSet);

		// Then
		 assertNotNull(result);
		 assertEquals(0, result.size());
		 assertFalse(result.contains(null));
	}
}