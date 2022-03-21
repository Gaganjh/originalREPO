/**
 * 
 */
package com.manulife.pension.service.distribution.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;
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
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.loan.valueobject.LoanPayee;
import com.manulife.pension.service.loan.valueobject.LoanPaymentInstruction;
import com.manulife.pension.service.withdrawal.valueobject.Address;
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

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for PayeeDao
 *
 * @see com.manulife.pension.service.distribution.dao.PayeeDao
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, PayeeDao.class })
@RunWith(PowerMockRunner.class)
public class PayeeDaoTest {
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
	 * @see com.manulife.pension.service.distribution.dao.PayeeDao#deleteAll(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDeleteAll() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);
		// Given
		PayeeDao underTest = new PayeeDao();

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
		PayeeDao underTest = new PayeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.deleteAll(submissionId, contractId, userProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for insert(Integer, Integer, Integer, Integer, Collection)
	 *
	 * @see com.manulife.pension.service.distribution.dao.PayeeDao#insert(Integer, Integer, Integer, Integer, Collection)
	 * @author patelpo
	 */
	@Test
	public void testInsert() throws Throwable {
		
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		PayeeDao underTest = new PayeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Integer recipientNo = 0; // UTA: default value
		Collection<Payee> payees = new ArrayList<Payee>(); // UTA: default value
		Payee payee = new LoanPayee();
		payee.getSubmissionId();
		payees.add(payee);
		underTest.insert(submissionId, contractId, userProfileId, recipientNo, payees);

	}
	@Test
	public void testInsert_1() throws Throwable {
		
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenReturn(10);

		// Given
		PayeeDao underTest = new PayeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Integer recipientNo = 0; // UTA: default value
		Collection<Payee> payees = null;
		
		underTest.insert(submissionId, contractId, userProfileId, recipientNo, payees);

	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testInsert_Exception() throws Throwable {
		
		SQLInsertHandler newSQLInsertHandlerResult = mock(SQLInsertHandler.class); // UTA: default value
		whenNew(SQLInsertHandler.class).withAnyArguments().thenReturn(newSQLInsertHandlerResult);
		when(newSQLInsertHandlerResult.insert(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		PayeeDao underTest = new PayeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Integer recipientNo = 0; // UTA: default value
		Collection<Payee> payees = new ArrayList<Payee>(); // UTA: default value
		Payee payee = new LoanPayee();
		payee.getSubmissionId();
		payees.add(payee);
		underTest.insert(submissionId, contractId, userProfileId, recipientNo, payees);

	}

	/**
	 * Parasoft Jtest UTA: Test for select(Integer, Integer, Integer, Class)
	 *
	 * @see com.manulife.pension.service.distribution.dao.PayeeDao#select(Integer, Integer, Integer, Class)
	 * @author patelpo
	 */
	@Test
	public void testSelect() throws Throwable {
		List<PaymentInstruction> paymentInstructions = new ArrayList<PaymentInstruction>();
		PaymentInstruction payment = new LoanPaymentInstruction();
		payment.setPayeeNo(10);
		payment.setRecipientNo(20);
		paymentInstructions.add(payment);
		PaymentInstructionDao newPaymentInstructionDaoResult = mock(PaymentInstructionDao.class); // UTA: default value
		whenNew(PaymentInstructionDao.class).withAnyArguments().thenReturn(newPaymentInstructionDaoResult);
		when(newPaymentInstructionDaoResult.select(anyInt(), anyInt(), anyInt(), any(Class.class))).thenReturn(paymentInstructions);
		

		List<DistributionAddress> distributionAddresses= new ArrayList<DistributionAddress>();
		DistributionAddress address = new Address();
		address.setPayeeNo(10);
		distributionAddresses.add(address);
		DistributionAddressDao newDistributionAddressDaoResult = mock(DistributionAddressDao.class); // UTA: default value
		whenNew(DistributionAddressDao.class).withAnyArguments().thenReturn(newDistributionAddressDaoResult);
		when(newDistributionAddressDaoResult.select(anyInt(), anyInt(), anyInt(), anyString(), any(Class.class))).thenReturn(distributionAddresses);

		
		List<Payee> templist = new ArrayList<Payee>();
		Payee payee = new LoanPayee();
		payee.setPayeeNo(10);
		payee.setRecipientNo(20);
		templist.add(payee);
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(templist);


		// Given
		PayeeDao underTest = new PayeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Class payeeType = Class.forName("com.manulife.pension.service.distribution.valueobject.Payee"); // UTA: default value
		Class distributionAddressType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.DistributionAddress"); // UTA: default value
		Class paymentInstructionType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.PaymentInstruction"); // UTA: default value
		List<? extends Payee> result = underTest.select(submissionId, contractId, userProfileId, payeeType,
				distributionAddressType, paymentInstructionType);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
	@Test(expected = DistributionServiceDaoException.class)
	public void testSelect_Exception() throws Throwable {
				
		List<Payee> templist = new ArrayList<Payee>();
		Payee payee = new LoanPayee();
		payee.setPayeeNo(10);
		payee.setRecipientNo(20);
		templist.add(payee);
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenThrow(DAOException.class);


		// Given
		PayeeDao underTest = new PayeeDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Class payeeType = Class.forName("com.manulife.pension.service.distribution.valueobject.Payee"); // UTA: default value
		Class distributionAddressType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.DistributionAddress"); // UTA: default value
		Class paymentInstructionType = Class
				.forName("com.manulife.pension.service.distribution.valueobject.PaymentInstruction"); // UTA: default value
		List<? extends Payee> result = underTest.select(submissionId, contractId, userProfileId, payeeType,
				distributionAddressType, paymentInstructionType);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
}