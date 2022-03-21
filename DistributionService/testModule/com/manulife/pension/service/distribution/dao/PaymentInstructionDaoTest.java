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
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.loan.valueobject.LoanPaymentInstruction;
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
 * Parasoft Jtest UTA: Test class for PaymentInstructionDao
 *
 * @see com.manulife.pension.service.distribution.dao.PaymentInstructionDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, PaymentInstructionDao.class })
@RunWith(PowerMockRunner.class)
public class PaymentInstructionDaoTest {
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
	 * Parasoft Jtest UTA: Test for deleteAll(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.distribution.dao.PaymentInstructionDao#deleteAll(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		PaymentInstructionDao underTest = new PaymentInstructionDao();

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
		PaymentInstructionDao underTest = new PaymentInstructionDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for insert(Integer, Integer, Integer, Integer, Integer, PaymentInstruction)
	 *
	 * @see com.manulife.pension.service.distribution.dao.PaymentInstructionDao#insert(Integer, Integer, Integer, Integer, Integer, PaymentInstruction)
	 * @author patelpo
	 */
	@Test
	public void testInsert() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		PaymentInstructionDao underTest = new PaymentInstructionDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Integer recipientNo = 0; // UTA: default value
		Integer payeeNo = 0; // UTA: default value
		PaymentInstruction payment = mockPaymentInstruction();
		underTest.insert(submissionId, contractId, userProfileId, recipientNo, payeeNo, payment);

	}
	@Test
	public void testInsert_1() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		PaymentInstructionDao underTest = new PaymentInstructionDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Integer recipientNo = 0; // UTA: default value
		Integer payeeNo = 0; // UTA: default value
		PaymentInstruction payment = null;
		underTest.insert(submissionId, contractId, userProfileId, recipientNo, payeeNo, payment);

	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testInsert_Exception() throws Throwable {
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		PaymentInstructionDao underTest = new PaymentInstructionDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Integer recipientNo = 0; // UTA: default value
		Integer payeeNo = 0; // UTA: default value
		PaymentInstruction payment = mockPaymentInstruction();
		underTest.insert(submissionId, contractId, userProfileId, recipientNo, payeeNo, payment);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of PaymentInstruction
	 */
	private static PaymentInstruction mockPaymentInstruction() throws Throwable {
		PaymentInstruction payment = mock(PaymentInstruction.class);
		String getBankAccountNumberResult = ""; // UTA: default value
		when(payment.getBankAccountNumber()).thenReturn(getBankAccountNumberResult);

		String getBankAccountTypeCodeResult = ""; // UTA: default value
		when(payment.getBankAccountTypeCode()).thenReturn(getBankAccountTypeCodeResult);

		String getBankNameResult = ""; // UTA: default value
		when(payment.getBankName()).thenReturn(getBankNameResult);

		Integer getBankTransitNumberResult = 0; // UTA: default value
		when(payment.getBankTransitNumber()).thenReturn(getBankTransitNumberResult);

		String getCreditPartyNameResult = ""; // UTA: default value
		when(payment.getCreditPartyName()).thenReturn(getCreditPartyNameResult);

		boolean isBlankResult = false; // UTA: default value
		when(payment.isBlank()).thenReturn(isBlankResult);
		return payment;
	}

	/**
	 * Parasoft Jtest UTA: Test for select(Integer, Integer, Integer, Class)
	 *
	 * @see com.manulife.pension.service.distribution.dao.PaymentInstructionDao#select(Integer, Integer, Integer, Class)
	 * @author patelpo
	 */
	@Test
	public void testSelect() throws Throwable {
		List<PaymentInstruction> returnList = new ArrayList<PaymentInstruction>();
		PaymentInstruction  paymentInstruction = new LoanPaymentInstruction();
		returnList.add(paymentInstruction);
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(returnList);

		// Given
		PaymentInstructionDao underTest = new PaymentInstructionDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Class paymentInstructionType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.PaymentInstruction"); // UTA: default value
		List<? extends PaymentInstruction> result = underTest.select(submissionId, contractId, userProfileId,
				paymentInstructionType);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testSelect_Exception() throws Throwable {
		List<PaymentInstruction> returnList = new ArrayList<PaymentInstruction>();
		PaymentInstruction  paymentInstruction = new LoanPaymentInstruction();
		returnList.add(paymentInstruction);
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		PaymentInstructionDao underTest = new PaymentInstructionDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Class paymentInstructionType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.PaymentInstruction"); // UTA: default value
		List<? extends PaymentInstruction> result = underTest.select(submissionId, contractId, userProfileId,
				paymentInstructionType);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
}