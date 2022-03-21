/**
 * 
 */
package com.manulife.pension.service.loan.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.intware.dao.jdbc.SelectMultiFieldMultiRowQueryHandler;
import com.intware.dao.jdbc.SelectSingleValueQueryHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.dao.DeclarationDao;
import com.manulife.pension.service.distribution.dao.DistributionAddressDao;
import com.manulife.pension.service.distribution.dao.FeeDao;
import com.manulife.pension.service.distribution.dao.NoteDao;
import com.manulife.pension.service.distribution.dao.PayeeDao;
import com.manulife.pension.service.distribution.dao.PaymentInstructionDao;
import com.manulife.pension.service.distribution.dao.RecipientDao;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.valueobject.AtRiskAddressChangeVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskForgetUserName;
import com.manulife.pension.service.distribution.valueobject.AtRiskPasswordResetVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskWebRegistrationVO;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.distribution.valueobject.Note;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanDeclaration;
import com.manulife.pension.service.loan.valueobject.LoanFee;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanNote;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.service.loan.valueobject.LoanRecipient;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.util.JdbcHelper;
import com.manulife.pension.util.Pair;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for LoanDao
 *
 * @see com.manulife.pension.service.loan.dao.LoanDao
 * @author patelpo
 */
@PrepareForTest({ AtRiskAddressChangeVO.class, AtRiskForgetUserName.class, JdbcHelper.class, Logger.class,
		LoanDao.class, FeeDao.class, RecipientDao.class, PayeeDao.class, DistributionAddressDao.class,
		PaymentInstructionDao.class, DeclarationDao.class, NoteDao.class, LoanParameterDao.class,
		BaseDatabaseDAO.class })
@RunWith(PowerMockRunner.class)
public class LoanDaoTest {
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
		when(prepareStatementResult.getResultSet()).thenReturn(executeQueryResult);

	}

	/**
	 * Parasoft Jtest UTA: Test for delete(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanDao#delete(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testDelete() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenReturn(10);

		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.delete(submissionId, contractId, userProfileId);

	}

	@Test(expected = LoanDaoException.class)
	public void testDelete_Exception() throws Throwable {
		SQLDeleteHandler newSQLDeleteHandlerResult = mock(SQLDeleteHandler.class); // UTA: default value
		whenNew(SQLDeleteHandler.class).withAnyArguments().thenReturn(newSQLDeleteHandlerResult);
		when(newSQLDeleteHandlerResult.delete(any(Object[].class))).thenThrow(new DAOException(""));


		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		underTest.delete(submissionId, contractId, userProfileId);

	}

	/**
	 * Parasoft Jtest UTA: Test for expireLoan(Integer, Integer, Timestamp)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanDao#expireLoan(Integer, Integer, Timestamp)
	 * @author patelpo
	 */
	@Test
	public void testExpireLoan() throws Throwable {
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Timestamp lastUpdatedTs = mock(Timestamp.class);
		boolean result = underTest.expireLoan(submissionId, userProfileId, lastUpdatedTs);

		// Then
		// assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getAboutToExpireLoans(java.util.Date)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanDao#getAboutToExpireLoans(java.util.Date)
	 * @author patelpo
	 */
	@Test
	public void testGetAboutToExpireLoans() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Date checkDate = mockDate();
		List<Loan> result = underTest.getAboutToExpireLoans(checkDate);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}
	@Test(expected = SystemException.class)
	public void testGetAboutToExpireLoans_exception() throws Throwable {
		when(getConnectionResult.prepareStatement(anyString())).thenThrow(new SQLException());
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Date checkDate = mockDate();
		List<Loan> result = underTest.getAboutToExpireLoans(checkDate);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate() throws Throwable {
		Date checkDate = mock(Date.class);
		long getTimeResult = 0L; // UTA: default value
		when(checkDate.getTime()).thenReturn(getTimeResult);
		return checkDate;
	}

	/**
	 * Parasoft Jtest UTA: Test for getExpiringLoans(java.util.Date)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanDao#getExpiringLoans(java.util.Date)
	 * @author patelpo
	 */
	@Test
	public void testGetExpiringLoans() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Date checkDate = mockDate2();
		Collection<Pair<Integer, Integer>> result = underTest.getExpiringLoans(checkDate);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	@Test(expected = SystemException.class)
	public void testGetExpiringLoans_Exception() throws Throwable {
		when(getConnectionResult.prepareStatement(anyString())).thenThrow(new SQLException());

		// Given
		LoanDao underTest = new LoanDao();

		// When
		Date checkDate = mockDate2();
		Collection<Pair<Integer, Integer>> result = underTest.getExpiringLoans(checkDate);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate2() throws Throwable {
		Date checkDate = mock(Date.class);
		long getTimeResult = 0L; // UTA: default value
		when(checkDate.getTime()).thenReturn(getTimeResult);
		return checkDate;
	}

	/**
	 * Parasoft Jtest UTA: Test for getLastLoanRequest(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanDao#getLastLoanRequest(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetLastLoanRequest() throws Throwable {
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer profileId = 0; // UTA: default value
		Loan result = underTest.getLastLoanRequest(contractId, profileId);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getLoanRequestsByCreatedId(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanDao#getLoanRequestsByCreatedId(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetLoanRequestsByCreatedId() throws Throwable {
		List<Note> returnList7 = new ArrayList<Note>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult7 = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult7);
		when(newSelectBeanListQueryHandlerResult7.select(any(Object[].class))).thenReturn(returnList7);

		List<Declaration> returnList6 = new ArrayList<Declaration>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult6 = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult6);
		when(newSelectBeanListQueryHandlerResult6.select(any(Object[].class))).thenReturn(returnList6);

		List<PaymentInstruction> returnList5 = new ArrayList<PaymentInstruction>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult5 = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult5);
		when(newSelectBeanListQueryHandlerResult5.select(any(Object[].class))).thenReturn(returnList5);

		List<DistributionAddress> returnList4 = new ArrayList<DistributionAddress>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult4 = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult4);
		when(newSelectBeanListQueryHandlerResult4.select(any(Object[].class))).thenReturn(returnList4);

		List<Payee> returnList2 = new ArrayList<Payee>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult3 = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult3);
		when(newSelectBeanListQueryHandlerResult3.select(any(Object[].class))).thenReturn(returnList2);

		List<Recipient> returnList1 = new ArrayList<Recipient>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult2 = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult2);
		when(newSelectBeanListQueryHandlerResult2.select(any(Object[].class))).thenReturn(returnList1);

		List<Fee> returnList = new ArrayList<Fee>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(returnList);

		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		List<Loan> result = underTest.getLoanRequestsByCreatedId(contractId, userProfileId);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Test for getPendingRequestSubmissionIds(Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanDao#getPendingRequestSubmissionIds(Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetPendingRequestSubmissionIds() throws Throwable {
		Object[][] rows = new Object[1][1];
		SelectMultiFieldMultiRowQueryHandler newSelectMultiFieldMultiRowQueryHandlerResult = mock(SelectMultiFieldMultiRowQueryHandler.class); // UTA: default value
		whenNew(SelectMultiFieldMultiRowQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectMultiFieldMultiRowQueryHandlerResult);
		when(newSelectMultiFieldMultiRowQueryHandlerResult.select(any(Object[].class))).thenReturn(rows);

		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer profileId = 0; // UTA: default value
		List<Integer> result = underTest.getPendingRequestSubmissionIds(contractId, profileId);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(0));
	}
	@Test(expected = LoanDaoException.class)
	public void testGetPendingRequestSubmissionIds_Exception() throws Throwable {
		SelectMultiFieldMultiRowQueryHandler newSelectMultiFieldMultiRowQueryHandlerResult = mock(SelectMultiFieldMultiRowQueryHandler.class); // UTA: default value
		whenNew(SelectMultiFieldMultiRowQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectMultiFieldMultiRowQueryHandlerResult);
		when(newSelectMultiFieldMultiRowQueryHandlerResult.select(any(Object[].class))).thenThrow(DAOException.class);

		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer profileId = 0; // UTA: default value
		List<Integer> result = underTest.getPendingRequestSubmissionIds(contractId, profileId);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(0));
	}

	/**
	 * Parasoft Jtest UTA: Test for getUOLCount(Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanDao#getUOLCount(Integer)
	 * @author patelpo
	 */
	@Test
	public void testGetUOLCount() throws Throwable {
		Integer uolCount = new Integer(10);
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(
				SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenReturn(uolCount);

		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer result = underTest.getUOLCount(contractId);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.intValue());
	}

	@Test(expected = LoanDaoException.class)
	public void testGetUOLCount_Exception() throws Throwable {
		Integer uolCount = new Integer(10);
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(
				SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenThrow(new DAOException());

		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer result = underTest.getUOLCount(contractId);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.intValue());
	}

	/**
	 * Parasoft Jtest UTA: Test for hasDraftRequest(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanDao#hasDraftRequest(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testHasDraftRequest() throws Throwable {
		Integer count = new Integer(10);
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(
				SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenReturn(count);

		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer contractId = 10; // UTA: default value
		Integer participantProfileId = 10; // UTA: default value
		Integer createdUserProfileId = 10; // UTA: default value
		boolean result = underTest.hasDraftRequest(contractId, participantProfileId, createdUserProfileId);

		// Then
		assertTrue(result);
	}

	@Test(expected = LoanDaoException.class)
	public void testHasDraftRequest_Exception() throws Throwable {
		Integer count = new Integer(10);
		SelectSingleValueQueryHandler newSelectSingleValueQueryHandlerResult = mock(
				SelectSingleValueQueryHandler.class); // UTA: default value
		whenNew(SelectSingleValueQueryHandler.class).withAnyArguments()
				.thenReturn(newSelectSingleValueQueryHandlerResult);
		when(newSelectSingleValueQueryHandlerResult.select(any(Object[].class))).thenThrow(new DAOException());

		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer participantProfileId = 0; // UTA: default value
		Integer createdUserProfileId = 0; // UTA: default value
		boolean result = underTest.hasDraftRequest(contractId, participantProfileId, createdUserProfileId);

		// Then
		// assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for insert(Integer, Integer, Loan, Timestamp)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanDao#insert(Integer, Integer, Loan, Timestamp)
	 * @author patelpo
	 */
	@Test(expected = LoanDaoException.class)
	public void testInsert_COntractID() throws Throwable {
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer contractId = null; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan loan = mockLoan();
		Timestamp timestamp = mock(Timestamp.class);
		Integer result = underTest.insert(contractId, userProfileId, loan, timestamp);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.intValue());
	}
	
	@Test(expected = LoanDaoException.class)
	public void testInsert_UserProfileID() throws Throwable {
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = null; // UTA: default value
		Loan loan = mockLoan();
		Timestamp timestamp = mock(Timestamp.class);
		Integer result = underTest.insert(contractId, userProfileId, loan, timestamp);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.intValue());
	}

	@Test(expected = LoanDaoException.class)
	public void testInsert_loan() throws Throwable {
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan loan = null;
		Timestamp timestamp = mock(Timestamp.class);
		Integer result = underTest.insert(contractId, userProfileId, loan, timestamp);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.intValue());
	}

	@Test
	public void testInsert_loan1() throws Throwable {
		spy(BaseDatabaseDAO.class);

		Long getNextSequenceValueResult = 10L; // UTA: default value
		doReturn(getNextSequenceValueResult).when(BaseDatabaseDAO.class, "getNextSequenceValue", anyString(),
				anyString());

		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan loan = new Loan();
		loan.setParticipantProfileId(10);
		Timestamp timestamp = mock(Timestamp.class);
		Integer result = underTest.insert(contractId, userProfileId, loan, timestamp);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.intValue());
	}
	@Test
	public void testInsert_loan2() throws Throwable {
		List<Fee> returnList = new ArrayList<Fee>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(returnList);
	
		spy(BaseDatabaseDAO.class);

		Long getNextSequenceValueResult = 10L; // UTA: default value
		doReturn(getNextSequenceValueResult).when(BaseDatabaseDAO.class, "getNextSequenceValue", anyString(),
				anyString());

		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan loan = mockLoan();
		loan.setParticipantProfileId(10);
		Timestamp timestamp = mock(Timestamp.class);
		Integer result = underTest.insert(contractId, userProfileId, loan, timestamp);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.intValue());
	}

	@Test(expected = LoanDaoException.class)
	public void testInsert_timestamp() throws Throwable {
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan loan = mockLoan();
		Timestamp timestamp = null;
		Integer result = underTest.insert(contractId, userProfileId, loan, timestamp);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.intValue());
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getAcceptedParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);

		BigDecimal getInterestRateResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getInterestRate()).thenReturn(getInterestRateResult);

		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);

		BigDecimal getMaximumAvailableResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult);

		BigDecimal getPaymentAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult);

		String getPaymentFrequencyResult = ""; // UTA: default value
		when(getAcceptedParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getAcceptedParameterResult.getStatusCode()).thenReturn(getStatusCodeResult);

		boolean isReadyToSaveResult = false; // UTA: default value
		when(getAcceptedParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult);
		return getAcceptedParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Address
	 */
	private static Address mockAddress() throws Throwable {
		Address getApprovalAddressResult = mock(Address.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(getApprovalAddressResult.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(getApprovalAddressResult.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(getApprovalAddressResult.getCity()).thenReturn(getCityResult);

		String getCountryCodeResult = ""; // UTA: default value
		when(getApprovalAddressResult.getCountryCode()).thenReturn(getCountryCodeResult);

		String getStateCodeResult = ""; // UTA: default value
		when(getApprovalAddressResult.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(getApprovalAddressResult.getZipCode()).thenReturn(getZipCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(getApprovalAddressResult.isBlank()).thenReturn(isBlankResult);
		return getApprovalAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskAddressChangeVO
	 */
	private static AtRiskAddressChangeVO mockAtRiskAddressChangeVO() throws Throwable {
		AtRiskAddressChangeVO getAddresschangeResult = mock(AtRiskAddressChangeVO.class);
		Address getApprovalAddressResult = mockAddress();
		when(getAddresschangeResult.getApprovalAddress()).thenReturn(getApprovalAddressResult);

		Integer getApprovalUpdatedProfileIdResult = 0; // UTA: default value
		when(getAddresschangeResult.getApprovalUpdatedProfileId()).thenReturn(getApprovalUpdatedProfileIdResult);

		String getApprovalUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getAddresschangeResult.getApprovalUpdatedUserIdType()).thenReturn(getApprovalUpdatedUserIdTypeResult);

		String getCreatedUserFistNameResult = ""; // UTA: default value
		when(getAddresschangeResult.getCreatedUserFistName()).thenReturn(getCreatedUserFistNameResult);

		String getCreatedUserLastNameResult = ""; // UTA: default value
		when(getAddresschangeResult.getCreatedUserLastName()).thenReturn(getCreatedUserLastNameResult);
		return getAddresschangeResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate3() throws Throwable {
		Date getForgotPasswordRequestedDateResult = mock(Date.class);
		long getTimeResult = 0L; // UTA: default value
		when(getForgotPasswordRequestedDateResult.getTime()).thenReturn(getTimeResult);

		String toStringResult = ""; // UTA: default value
		when(getForgotPasswordRequestedDateResult.toString()).thenReturn(toStringResult);
		return getForgotPasswordRequestedDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskForgetUserName
	 */
	private static AtRiskForgetUserName mockAtRiskForgetUserName() throws Throwable {
		AtRiskForgetUserName getForgetUserNameResult = mock(AtRiskForgetUserName.class);
		String getForgotPasswordEmailAddressResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordEmailAddress()).thenReturn(getForgotPasswordEmailAddressResult);

		Date getForgotPasswordRequestedDateResult = mockDate3();
		when(getForgetUserNameResult.getForgotPasswordRequestedDate()).thenReturn(getForgotPasswordRequestedDateResult);

		Integer getForgotPasswordUpdatedProfileIdResult = 0; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedProfileId())
				.thenReturn(getForgotPasswordUpdatedProfileIdResult);

		String getForgotPasswordUpdatedUserFirstNameResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserFirstName())
				.thenReturn(getForgotPasswordUpdatedUserFirstNameResult);

		String getForgotPasswordUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserIdType())
				.thenReturn(getForgotPasswordUpdatedUserIdTypeResult);

		String getForgotPasswordUpdatedUserLastNameResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserLastName())
				.thenReturn(getForgotPasswordUpdatedUserLastNameResult);
		return getForgetUserNameResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate4() throws Throwable {
		Date getEmailPasswordResetDateResult = mock(Date.class);
		long getTimeResult2 = 0L; // UTA: default value
		when(getEmailPasswordResetDateResult.getTime()).thenReturn(getTimeResult2);

		String toStringResult2 = ""; // UTA: default value
		when(getEmailPasswordResetDateResult.toString()).thenReturn(toStringResult2);
		return getEmailPasswordResetDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskPasswordResetVO
	 */
	private static AtRiskPasswordResetVO mockAtRiskPasswordResetVO() throws Throwable {
		AtRiskPasswordResetVO getPasswordResetResult = mock(AtRiskPasswordResetVO.class);
		Integer getEmailAddressLastUpdatedProfileIdResult = 0; // UTA: default value
		when(getPasswordResetResult.getEmailAddressLastUpdatedProfileId())
				.thenReturn(getEmailAddressLastUpdatedProfileIdResult);

		String getEmailAddressLastUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getPasswordResetResult.getEmailAddressLastUpdatedUserIdType())
				.thenReturn(getEmailAddressLastUpdatedUserIdTypeResult);

		Date getEmailPasswordResetDateResult = mockDate4();
		when(getPasswordResetResult.getEmailPasswordResetDate()).thenReturn(getEmailPasswordResetDateResult);

		String getEmailPasswordResetEmailAddressResult = ""; // UTA: default value
		when(getPasswordResetResult.getEmailPasswordResetEmailAddress())
				.thenReturn(getEmailPasswordResetEmailAddressResult);

		Integer getEmailPasswordResetInitiatedProfileIdResult = 0; // UTA: default value
		when(getPasswordResetResult.getEmailPasswordResetInitiatedProfileId())
				.thenReturn(getEmailPasswordResetInitiatedProfileIdResult);
		return getPasswordResetResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Address
	 */
	private static Address mockAddress2() throws Throwable {
		Address getAddressResult = mock(Address.class);
		String getAddressLine1Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine1()).thenReturn(getAddressLine1Result2);

		String getAddressLine2Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine2()).thenReturn(getAddressLine2Result2);

		String getCityResult2 = ""; // UTA: default value
		when(getAddressResult.getCity()).thenReturn(getCityResult2);

		String getCountryCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getCountryCode()).thenReturn(getCountryCodeResult2);

		String getStateCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getStateCode()).thenReturn(getStateCodeResult2);

		String getZipCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getZipCode()).thenReturn(getZipCodeResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getAddressResult.isBlank()).thenReturn(isBlankResult2);
		return getAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate5() throws Throwable {
		Date getWebRegConfirmationMailedDateResult = mock(Date.class);
		long getTimeResult3 = 0L; // UTA: default value
		when(getWebRegConfirmationMailedDateResult.getTime()).thenReturn(getTimeResult3);

		String toStringResult3 = ""; // UTA: default value
		when(getWebRegConfirmationMailedDateResult.toString()).thenReturn(toStringResult3);
		return getWebRegConfirmationMailedDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate6() throws Throwable {
		Date getWebRegistrationDateResult = mock(Date.class);
		long getTimeResult4 = 0L; // UTA: default value
		when(getWebRegistrationDateResult.getTime()).thenReturn(getTimeResult4);

		String toStringResult4 = ""; // UTA: default value
		when(getWebRegistrationDateResult.toString()).thenReturn(toStringResult4);
		return getWebRegistrationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskWebRegistrationVO
	 */
	private static AtRiskWebRegistrationVO mockAtRiskWebRegistrationVO() throws Throwable {
		AtRiskWebRegistrationVO getWebRegistrationResult = mock(AtRiskWebRegistrationVO.class);
		Address getAddressResult = mockAddress2();
		when(getWebRegistrationResult.getAddress()).thenReturn(getAddressResult);

		Integer getConfirmUpdatedProfileIdResult = 0; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedProfileId()).thenReturn(getConfirmUpdatedProfileIdResult);

		String getConfirmUpdatedUserFirstNameResult = ""; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedUserFirstName())
				.thenReturn(getConfirmUpdatedUserFirstNameResult);

		String getConfirmUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedUserIdType()).thenReturn(getConfirmUpdatedUserIdTypeResult);

		String getConfirmUpdatedUserLastNameResult = ""; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedUserLastName()).thenReturn(getConfirmUpdatedUserLastNameResult);

		Date getWebRegConfirmationMailedDateResult = mockDate5();
		when(getWebRegistrationResult.getWebRegConfirmationMailedDate())
				.thenReturn(getWebRegConfirmationMailedDateResult);

		Date getWebRegistrationDateResult = mockDate6();
		when(getWebRegistrationResult.getWebRegistrationDate()).thenReturn(getWebRegistrationDateResult);
		return getWebRegistrationResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskDetailsVO
	 */
	private static AtRiskDetailsVO mockAtRiskDetailsVO() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		AtRiskAddressChangeVO getAddresschangeResult = mockAtRiskAddressChangeVO();
		when(getAtRiskDetailsVOResult.getAddresschange()).thenReturn(getAddresschangeResult);

		AtRiskForgetUserName getForgetUserNameResult = mockAtRiskForgetUserName();
		when(getAtRiskDetailsVOResult.getForgetUserName()).thenReturn(getForgetUserNameResult);

		AtRiskPasswordResetVO getPasswordResetResult = mockAtRiskPasswordResetVO();
		when(getAtRiskDetailsVOResult.getPasswordReset()).thenReturn(getPasswordResetResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);

		AtRiskWebRegistrationVO getWebRegistrationResult = mockAtRiskWebRegistrationVO();
		when(getAtRiskDetailsVOResult.getWebRegistration()).thenReturn(getWebRegistrationResult);
		return getAtRiskDetailsVOResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote() throws Throwable {
		LoanNote getCurrentAdministratorNoteResult = mock(LoanNote.class);
		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult3 = false; // UTA: default value
		when(getCurrentAdministratorNoteResult.isBlank()).thenReturn(isBlankResult3);
		return getCurrentAdministratorNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote2() throws Throwable {
		LoanNote getCurrentParticipantNoteResult = mock(LoanNote.class);
		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		boolean isBlankResult4 = false; // UTA: default value
		when(getCurrentParticipantNoteResult.isBlank()).thenReturn(isBlankResult4);
		return getCurrentParticipantNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate7() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		long getTimeResult5 = 0L; // UTA: default value
		when(getEffectiveDateResult.getTime()).thenReturn(getTimeResult5);

		String toStringResult5 = ""; // UTA: default value
		when(getEffectiveDateResult.toString()).thenReturn(toStringResult5);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate8() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		long getTimeResult6 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult6);

		String toStringResult6 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult6);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Fee
	 */
	private static Fee mockFee() throws Throwable {
		Fee getFeeResult = mock(Fee.class);
		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getFeeResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);

		String getTypeCodeResult = ""; // UTA: default value
		when(getFeeResult.getTypeCode()).thenReturn(getTypeCodeResult);

		BigDecimal getValueResult = BigDecimal.ZERO; // UTA: default value
		when(getFeeResult.getValue()).thenReturn(getValueResult);

		boolean isBlankResult5 = false; // UTA: default value
		when(getFeeResult.isBlank()).thenReturn(isBlankResult5);
		return getFeeResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate9() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		long getTimeResult7 = 0L; // UTA: default value
		when(getFirstPayrollDateResult.getTime()).thenReturn(getTimeResult7);

		String toStringResult7 = ""; // UTA: default value
		when(getFirstPayrollDateResult.toString()).thenReturn(toStringResult7);
		return getFirstPayrollDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate10() throws Throwable {
		Date getMaturityDateResult = mock(Date.class);
		long getTimeResult8 = 0L; // UTA: default value
		when(getMaturityDateResult.getTime()).thenReturn(getTimeResult8);

		String toStringResult8 = ""; // UTA: default value
		when(getMaturityDateResult.toString()).thenReturn(toStringResult8);
		return getMaturityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter2() throws Throwable {
		LoanParameter getOriginalParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult2 = 0; // UTA: default value
		when(getOriginalParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult2);

		BigDecimal getInterestRateResult2 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getInterestRate()).thenReturn(getInterestRateResult2);

		BigDecimal getLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult2);

		BigDecimal getMaximumAvailableResult2 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult2);

		BigDecimal getPaymentAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult2);

		String getPaymentFrequencyResult2 = ""; // UTA: default value
		when(getOriginalParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult2);

		String getStatusCodeResult2 = ""; // UTA: default value
		when(getOriginalParameterResult.getStatusCode()).thenReturn(getStatusCodeResult2);

		boolean isReadyToSaveResult2 = false; // UTA: default value
		when(getOriginalParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult2);
		return getOriginalParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress() throws Throwable {
		DistributionAddress getAddressResult2 = mock(DistributionAddress.class);
		String getAddressLine1Result3 = ""; // UTA: default value
		when(getAddressResult2.getAddressLine1()).thenReturn(getAddressLine1Result3);

		String getAddressLine2Result3 = ""; // UTA: default value
		when(getAddressResult2.getAddressLine2()).thenReturn(getAddressLine2Result3);

		String getCityResult3 = ""; // UTA: default value
		when(getAddressResult2.getCity()).thenReturn(getCityResult3);

		String getCountryCodeResult3 = ""; // UTA: default value
		when(getAddressResult2.getCountryCode()).thenReturn(getCountryCodeResult3);

		String getStateCodeResult3 = ""; // UTA: default value
		when(getAddressResult2.getStateCode()).thenReturn(getStateCodeResult3);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(getAddressResult2.getSubmissionId()).thenReturn(getSubmissionIdResult3);

		String getZipCodeResult3 = ""; // UTA: default value
		when(getAddressResult2.getZipCode()).thenReturn(getZipCodeResult3);

		boolean isBlankResult6 = false; // UTA: default value
		when(getAddressResult2.isBlank()).thenReturn(isBlankResult6);
		return getAddressResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult2 = mockDistributionAddress();
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult2);

		BigDecimal getFederalTaxPercentResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getFederalTaxPercent()).thenReturn(getFederalTaxPercentResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getRecipientResult.getFirstName()).thenReturn(getFirstNameResult);

		String getLastNameResult = ""; // UTA: default value
		when(getRecipientResult.getLastName()).thenReturn(getLastNameResult);

		String getOrganizationNameResult = ""; // UTA: default value
		when(getRecipientResult.getOrganizationName()).thenReturn(getOrganizationNameResult);

		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		doReturn(getPayeesResult).when(getRecipientResult).getPayees();

		String getShareTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getShareTypeCode()).thenReturn(getShareTypeCodeResult);

		BigDecimal getShareValueResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getShareValue()).thenReturn(getShareValueResult);

		String getStateOfResidenceCodeResult = ""; // UTA: default value
		when(getRecipientResult.getStateOfResidenceCode()).thenReturn(getStateOfResidenceCodeResult);

		BigDecimal getStateTaxPercentResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getStateTaxPercent()).thenReturn(getStateTaxPercentResult);

		String getStateTaxTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getStateTaxTypeCode()).thenReturn(getStateTaxTypeCodeResult);

		String getTaxpayerIdentNoResult = ""; // UTA: default value
		when(getRecipientResult.getTaxpayerIdentNo()).thenReturn(getTaxpayerIdentNoResult);

		String getTaxpayerIdentTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getTaxpayerIdentTypeCode()).thenReturn(getTaxpayerIdentTypeCodeResult);

		Boolean getUsCitizenIndResult = false; // UTA: default value
		when(getRecipientResult.getUsCitizenInd()).thenReturn(getUsCitizenIndResult);
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate11() throws Throwable {
		Date getRequestDateResult = mock(Date.class);
		long getTimeResult9 = 0L; // UTA: default value
		when(getRequestDateResult.getTime()).thenReturn(getTimeResult9);

		String toStringResult9 = ""; // UTA: default value
		when(getRequestDateResult.toString()).thenReturn(toStringResult9);
		return getRequestDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter3() throws Throwable {
		LoanParameter getReviewedParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult3 = 0; // UTA: default value
		when(getReviewedParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult3);

		BigDecimal getInterestRateResult3 = BigDecimal.ZERO; // UTA: default value
		when(getReviewedParameterResult.getInterestRate()).thenReturn(getInterestRateResult3);

		BigDecimal getLoanAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getReviewedParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult3);

		BigDecimal getMaximumAvailableResult3 = BigDecimal.ZERO; // UTA: default value
		when(getReviewedParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult3);

		BigDecimal getPaymentAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getReviewedParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult3);

		String getPaymentFrequencyResult3 = ""; // UTA: default value
		when(getReviewedParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult3);

		String getStatusCodeResult3 = ""; // UTA: default value
		when(getReviewedParameterResult.getStatusCode()).thenReturn(getStatusCodeResult3);

		boolean isReadyToSaveResult3 = false; // UTA: default value
		when(getReviewedParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult3);
		return getReviewedParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(loan.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);

		AtRiskDetailsVO getAtRiskDetailsVOResult = mockAtRiskDetailsVO();
		when(loan.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);

		String getAtRiskIndResult = ""; // UTA: default value
		when(loan.getAtRiskInd()).thenReturn(getAtRiskIndResult);

		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);

		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);

		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote2();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate7();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		Date getExpirationDateResult = mockDate8();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate9();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		String getLoanReasonResult = ""; // UTA: default value
		when(loan.getLoanReason()).thenReturn(getLoanReasonResult);

		String getLoanTypeResult = ""; // UTA: default value
		when(loan.getLoanType()).thenReturn(getLoanTypeResult);

		List<ManagedContent> getManagedContentsResult = new ArrayList<ManagedContent>(); // UTA: default value
		doReturn(getManagedContentsResult).when(loan).getManagedContents();

		Date getMaturityDateResult = mockDate10();
		when(loan.getMaturityDate()).thenReturn(getMaturityDateResult);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(loan.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		BigDecimal getMaximumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult);

		BigDecimal getMaximumLoanPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		List<LoanMoneyType> getMoneyTypesResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(loan).getMoneyTypes();

		LoanParameter getOriginalParameterResult = mockLoanParameter2();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(loan.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(loan.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);

		LoanRecipient getRecipientResult = mockLoanRecipient();
		when(loan.getRecipient()).thenReturn(getRecipientResult);

		Date getRequestDateResult = mockDate11();
		when(loan.getRequestDate()).thenReturn(getRequestDateResult);

		LoanParameter getReviewedParameterResult = mockLoanParameter3();
		when(loan.getReviewedParameter()).thenReturn(getReviewedParameterResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(loan.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);

		String getStatusResult = ""; // UTA: default value
		when(loan.getStatus()).thenReturn(getStatusResult);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Test for read(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanDao#read(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testRead() throws Throwable {
		List<Fee> returnList = new ArrayList<Fee>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(returnList);
		

		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan result = underTest.read(submissionId, contractId, userProfileId);

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testRead_1() throws Throwable {
		List<Fee> returnList = new ArrayList<Fee>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(returnList);
		
		/*List<PaymentInstruction> returnList5 = new ArrayList<PaymentInstruction>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult5 = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult5);
		when(newSelectBeanListQueryHandlerResult5.select(any(Object[].class))).thenReturn(returnList5);
		
		List<Note> returnList7 = new ArrayList<Note>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult7 = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult7);
		when(newSelectBeanListQueryHandlerResult7.select(any(Object[].class))).thenReturn(returnList7);

		List<Declaration> returnList6 = new ArrayList<Declaration>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult6 = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult6);
		when(newSelectBeanListQueryHandlerResult6.select(any(Object[].class))).thenReturn(returnList6);

		List<Recipient> returnList1 = new ArrayList<Recipient>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult2 = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult2);
		when(newSelectBeanListQueryHandlerResult2.select(any(Object[].class))).thenReturn(returnList1);*/
		
		
		List<LoanParameter> parameters = new ArrayList<LoanParameter>();
		LoanParameter parameter = new LoanParameter();
		parameter.setStatusCode("DRAFT");
		parameters.add(parameter);
		LoanParameterDao parameterDao = mock(LoanParameterDao.class);
		when(parameterDao.read(any(Integer.class), any(Integer.class), any(Integer.class))).thenReturn(parameters);
		

		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		when(executeQueryResult.getInt("SUBMISSION_ID")).thenReturn(20);
		when(executeQueryResult.getInt("CONTRACT_ID")).thenReturn(20);
		when(executeQueryResult.getInt("PROFILE_ID")).thenReturn(20);
		
		Loan mockLoan=mock(Loan.class);
		when(mockLoan.getLoginUserProfileId()).thenReturn(20);
		
		// Given
		LoanDao underTest = new LoanDao();
		
		// When
		Integer submissionId = 10; // UTA: default value
		Integer contractId = 10; // UTA: default value
		Integer userProfileId = 10; // UTA: default value
		Loan result = underTest.read(submissionId, contractId, userProfileId);

		// Then
		// assertNotNull(result);
	}

	@Test(expected = LoanDaoException.class)
	public void testRead_exception() throws Throwable {
		when(getConnectionResult.prepareStatement(anyString())).thenThrow(new SQLException());


		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan result = underTest.read(submissionId, contractId, userProfileId);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for update(Integer, Integer, Integer, Loan, Timestamp)
	 *
	 * @see com.manulife.pension.service.loan.dao.LoanDao#update(Integer, Integer, Integer, Loan, Timestamp)
	 * @author patelpo
	 */
	@Test
	public void testUpdate() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class); // UTA: default value
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenReturn(10);
		
		List<Fee> returnList = new ArrayList<Fee>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(returnList);
		
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan loan = mockLoan2();
		Timestamp timestamp = mockTimestamp4();
		boolean result = underTest.update(submissionId, contractId, userProfileId, loan, timestamp);

		// Then
		// assertFalse(result);
	}
	@Test(expected = LoanDaoException.class)
	public void testUpdate_Exception1() throws Throwable {
		when(getConnectionResult.prepareStatement(anyString())).thenThrow(new SQLException());
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan loan = mockLoan2();
		Timestamp timestamp = mockTimestamp4();
		boolean result = underTest.update(submissionId, contractId, userProfileId, loan, timestamp);

		// Then
		// assertFalse(result);
	}
	@Test
	public void testUpdate11() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class); // UTA: default value
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenReturn(10);
		
		List<Fee> returnList = new ArrayList<Fee>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(returnList);
		
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan loan = mockLoan2_1();
		Timestamp timestamp = mockTimestamp4();
		boolean result = underTest.update(submissionId, contractId, userProfileId, loan, timestamp);

		// Then
		// assertFalse(result);
	}
	@Test
	public void testUpdate22() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class); // UTA: default value
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenReturn(10);
		
		List<Fee> returnList = new ArrayList<Fee>();
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(returnList);
		
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan loan = mockLoan2_2();
		Timestamp timestamp = mockTimestamp4();
		boolean result = underTest.update(submissionId, contractId, userProfileId, loan, timestamp);

		// Then
		// assertFalse(result);
	}
	@Test(expected = LoanDaoException.class)
	public void testUpdate_Exception() throws Throwable {

		SQLUpdateHandler newSQLUpdateHandlerResult = mock(SQLUpdateHandler.class); // UTA: default value
		whenNew(SQLUpdateHandler.class).withAnyArguments().thenReturn(newSQLUpdateHandlerResult);
		when(newSQLUpdateHandlerResult.update(any(Object[].class))).thenThrow(DAOException.class);
		
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan loan = mockLoan2();
		Timestamp timestamp = mockTimestamp4();
		boolean result = underTest.update(submissionId, contractId, userProfileId, loan, timestamp);

		// Then
		// assertFalse(result);
	}
	
	@Test(expected = LoanDaoException.class)
	public void testUpdate_1() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = null; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan loan = mockLoan2();
		Timestamp timestamp = mockTimestamp4();
		boolean result = underTest.update(submissionId, contractId, userProfileId, loan, timestamp);

		// Then
		// assertFalse(result);
	}
	@Test(expected = LoanDaoException.class)
	public void testUpdate_2() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = null; // UTA: default value
		Loan loan = mockLoan2();
		Timestamp timestamp = mockTimestamp4();
		boolean result = underTest.update(submissionId, contractId, userProfileId, loan, timestamp);

		// Then
		// assertFalse(result);
	}
	@Test(expected = LoanDaoException.class)
	public void testUpdate_3() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan loan = null;
		Timestamp timestamp = mockTimestamp4();
		boolean result = underTest.update(submissionId, contractId, userProfileId, loan, timestamp);

		// Then
		// assertFalse(result);
	}
	@Test(expected = LoanDaoException.class)
	public void testUpdate_4() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// Given
		LoanDao underTest = new LoanDao();

		// When
		Integer submissionId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan loan = mockLoan2();
		Timestamp timestamp = null;
		boolean result = underTest.update(submissionId, contractId, userProfileId, loan, timestamp);

		// Then
		// assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter4() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getAcceptedParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);

		BigDecimal getInterestRateResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getInterestRate()).thenReturn(getInterestRateResult);

		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);

		BigDecimal getMaximumAvailableResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult);

		BigDecimal getPaymentAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult);

		String getPaymentFrequencyResult = ""; // UTA: default value
		when(getAcceptedParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getAcceptedParameterResult.getStatusCode()).thenReturn(getStatusCodeResult);

		boolean isReadyToSaveResult = false; // UTA: default value
		when(getAcceptedParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult);

		String toStringResult = ""; // UTA: default value
		when(getAcceptedParameterResult.toString()).thenReturn(toStringResult);
		return getAcceptedParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Address
	 */
	private static Address mockAddress3() throws Throwable {
		Address getApprovalAddressResult = mock(Address.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(getApprovalAddressResult.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(getApprovalAddressResult.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(getApprovalAddressResult.getCity()).thenReturn(getCityResult);

		String getCountryCodeResult = ""; // UTA: default value
		when(getApprovalAddressResult.getCountryCode()).thenReturn(getCountryCodeResult);

		String getStateCodeResult = ""; // UTA: default value
		when(getApprovalAddressResult.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(getApprovalAddressResult.getZipCode()).thenReturn(getZipCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(getApprovalAddressResult.isBlank()).thenReturn(isBlankResult);

		String toStringResult2 = ""; // UTA: default value
		when(getApprovalAddressResult.toString()).thenReturn(toStringResult2);
		return getApprovalAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskAddressChangeVO
	 */
	private static AtRiskAddressChangeVO mockAtRiskAddressChangeVO2() throws Throwable {
		AtRiskAddressChangeVO getAddresschangeResult = mock(AtRiskAddressChangeVO.class);
		Address getApprovalAddressResult = mockAddress3();
		when(getAddresschangeResult.getApprovalAddress()).thenReturn(getApprovalAddressResult);

		Integer getApprovalUpdatedProfileIdResult = 0; // UTA: default value
		when(getAddresschangeResult.getApprovalUpdatedProfileId()).thenReturn(getApprovalUpdatedProfileIdResult);

		String getApprovalUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getAddresschangeResult.getApprovalUpdatedUserIdType()).thenReturn(getApprovalUpdatedUserIdTypeResult);

		String getCreatedUserFistNameResult = ""; // UTA: default value
		when(getAddresschangeResult.getCreatedUserFistName()).thenReturn(getCreatedUserFistNameResult);

		String getCreatedUserLastNameResult = ""; // UTA: default value
		when(getAddresschangeResult.getCreatedUserLastName()).thenReturn(getCreatedUserLastNameResult);
		return getAddresschangeResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate12() throws Throwable {
		Date getForgotPasswordRequestedDateResult = mock(Date.class);
		long getTimeResult = 0L; // UTA: default value
		when(getForgotPasswordRequestedDateResult.getTime()).thenReturn(getTimeResult);

		String toStringResult3 = ""; // UTA: default value
		when(getForgotPasswordRequestedDateResult.toString()).thenReturn(toStringResult3);
		return getForgotPasswordRequestedDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskForgetUserName
	 */
	private static AtRiskForgetUserName mockAtRiskForgetUserName2() throws Throwable {
		AtRiskForgetUserName getForgetUserNameResult = mock(AtRiskForgetUserName.class);
		String getForgotPasswordEmailAddressResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordEmailAddress()).thenReturn(getForgotPasswordEmailAddressResult);

		Date getForgotPasswordRequestedDateResult = mockDate12();
		when(getForgetUserNameResult.getForgotPasswordRequestedDate()).thenReturn(getForgotPasswordRequestedDateResult);

		Integer getForgotPasswordUpdatedProfileIdResult = 0; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedProfileId())
				.thenReturn(getForgotPasswordUpdatedProfileIdResult);

		String getForgotPasswordUpdatedUserFirstNameResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserFirstName())
				.thenReturn(getForgotPasswordUpdatedUserFirstNameResult);

		String getForgotPasswordUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserIdType())
				.thenReturn(getForgotPasswordUpdatedUserIdTypeResult);

		String getForgotPasswordUpdatedUserLastNameResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserLastName())
				.thenReturn(getForgotPasswordUpdatedUserLastNameResult);
		return getForgetUserNameResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate13() throws Throwable {
		Date getEmailPasswordResetDateResult = mock(Date.class);
		long getTimeResult2 = 0L; // UTA: default value
		when(getEmailPasswordResetDateResult.getTime()).thenReturn(getTimeResult2);

		String toStringResult4 = ""; // UTA: default value
		when(getEmailPasswordResetDateResult.toString()).thenReturn(toStringResult4);
		return getEmailPasswordResetDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskPasswordResetVO
	 */
	private static AtRiskPasswordResetVO mockAtRiskPasswordResetVO2() throws Throwable {
		AtRiskPasswordResetVO getPasswordResetResult = mock(AtRiskPasswordResetVO.class);
		Integer getEmailAddressLastUpdatedProfileIdResult = 0; // UTA: default value
		when(getPasswordResetResult.getEmailAddressLastUpdatedProfileId())
				.thenReturn(getEmailAddressLastUpdatedProfileIdResult);

		String getEmailAddressLastUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getPasswordResetResult.getEmailAddressLastUpdatedUserIdType())
				.thenReturn(getEmailAddressLastUpdatedUserIdTypeResult);

		Date getEmailPasswordResetDateResult = mockDate13();
		when(getPasswordResetResult.getEmailPasswordResetDate()).thenReturn(getEmailPasswordResetDateResult);

		String getEmailPasswordResetEmailAddressResult = ""; // UTA: default value
		when(getPasswordResetResult.getEmailPasswordResetEmailAddress())
				.thenReturn(getEmailPasswordResetEmailAddressResult);

		Integer getEmailPasswordResetInitiatedProfileIdResult = 0; // UTA: default value
		when(getPasswordResetResult.getEmailPasswordResetInitiatedProfileId())
				.thenReturn(getEmailPasswordResetInitiatedProfileIdResult);
		return getPasswordResetResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Address
	 */
	private static Address mockAddress4() throws Throwable {
		Address getAddressResult = mock(Address.class);
		String getAddressLine1Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine1()).thenReturn(getAddressLine1Result2);

		String getAddressLine2Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine2()).thenReturn(getAddressLine2Result2);

		String getCityResult2 = ""; // UTA: default value
		when(getAddressResult.getCity()).thenReturn(getCityResult2);

		String getCountryCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getCountryCode()).thenReturn(getCountryCodeResult2);

		String getStateCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getStateCode()).thenReturn(getStateCodeResult2);

		String getZipCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getZipCode()).thenReturn(getZipCodeResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getAddressResult.isBlank()).thenReturn(isBlankResult2);

		String toStringResult5 = ""; // UTA: default value
		when(getAddressResult.toString()).thenReturn(toStringResult5);
		return getAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate14() throws Throwable {
		Date getWebRegConfirmationMailedDateResult = mock(Date.class);
		long getTimeResult3 = 0L; // UTA: default value
		when(getWebRegConfirmationMailedDateResult.getTime()).thenReturn(getTimeResult3);

		String toStringResult6 = ""; // UTA: default value
		when(getWebRegConfirmationMailedDateResult.toString()).thenReturn(toStringResult6);
		return getWebRegConfirmationMailedDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate15() throws Throwable {
		Date getWebRegistrationDateResult = mock(Date.class);
		long getTimeResult4 = 0L; // UTA: default value
		when(getWebRegistrationDateResult.getTime()).thenReturn(getTimeResult4);

		String toStringResult7 = ""; // UTA: default value
		when(getWebRegistrationDateResult.toString()).thenReturn(toStringResult7);
		return getWebRegistrationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskWebRegistrationVO
	 */
	private static AtRiskWebRegistrationVO mockAtRiskWebRegistrationVO2() throws Throwable {
		AtRiskWebRegistrationVO getWebRegistrationResult = mock(AtRiskWebRegistrationVO.class);
		Address getAddressResult = mockAddress4();
		when(getWebRegistrationResult.getAddress()).thenReturn(getAddressResult);

		Integer getConfirmUpdatedProfileIdResult = 0; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedProfileId()).thenReturn(getConfirmUpdatedProfileIdResult);

		String getConfirmUpdatedUserFirstNameResult = ""; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedUserFirstName())
				.thenReturn(getConfirmUpdatedUserFirstNameResult);

		String getConfirmUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedUserIdType()).thenReturn(getConfirmUpdatedUserIdTypeResult);

		String getConfirmUpdatedUserLastNameResult = ""; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedUserLastName()).thenReturn(getConfirmUpdatedUserLastNameResult);

		Date getWebRegConfirmationMailedDateResult = mockDate14();
		when(getWebRegistrationResult.getWebRegConfirmationMailedDate())
				.thenReturn(getWebRegConfirmationMailedDateResult);

		Date getWebRegistrationDateResult = mockDate15();
		when(getWebRegistrationResult.getWebRegistrationDate()).thenReturn(getWebRegistrationDateResult);
		return getWebRegistrationResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskDetailsVO
	 */
	private static AtRiskDetailsVO mockAtRiskDetailsVO2() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		AtRiskAddressChangeVO getAddresschangeResult = mockAtRiskAddressChangeVO2();
		when(getAtRiskDetailsVOResult.getAddresschange()).thenReturn(getAddresschangeResult);

		AtRiskForgetUserName getForgetUserNameResult = mockAtRiskForgetUserName2();
		when(getAtRiskDetailsVOResult.getForgetUserName()).thenReturn(getForgetUserNameResult);

		AtRiskPasswordResetVO getPasswordResetResult = mockAtRiskPasswordResetVO2();
		when(getAtRiskDetailsVOResult.getPasswordReset()).thenReturn(getPasswordResetResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);

		AtRiskWebRegistrationVO getWebRegistrationResult = mockAtRiskWebRegistrationVO2();
		when(getAtRiskDetailsVOResult.getWebRegistration()).thenReturn(getWebRegistrationResult);
		return getAtRiskDetailsVOResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote3() throws Throwable {
		LoanNote getCurrentAdministratorNoteResult = mock(LoanNote.class);
		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult3 = false; // UTA: default value
		when(getCurrentAdministratorNoteResult.isBlank()).thenReturn(isBlankResult3);

		String toStringResult8 = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.toString()).thenReturn(toStringResult8);
		return getCurrentAdministratorNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote4() throws Throwable {
		LoanNote getCurrentParticipantNoteResult = mock(LoanNote.class);
		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		boolean isBlankResult4 = false; // UTA: default value
		when(getCurrentParticipantNoteResult.isBlank()).thenReturn(isBlankResult4);

		String toStringResult9 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.toString()).thenReturn(toStringResult9);
		return getCurrentParticipantNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate16() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		long getTimeResult5 = 0L; // UTA: default value
		when(getEffectiveDateResult.getTime()).thenReturn(getTimeResult5);

		String toStringResult10 = ""; // UTA: default value
		when(getEffectiveDateResult.toString()).thenReturn(toStringResult10);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate17() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		long getTimeResult6 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult6);

		String toStringResult11 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult11);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult12 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult12);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Fee
	 */
	private static Fee mockFee2() throws Throwable {
		Fee getFeeResult = mock(Fee.class);
		Timestamp getLastUpdatedResult = mockTimestamp();
		when(getFeeResult.getLastUpdated()).thenReturn(getLastUpdatedResult);

		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getFeeResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);

		String getTypeCodeResult = ""; // UTA: default value
		when(getFeeResult.getTypeCode()).thenReturn(getTypeCodeResult);

		BigDecimal getValueResult = BigDecimal.ZERO; // UTA: default value
		when(getFeeResult.getValue()).thenReturn(getValueResult);

		boolean isBlankResult5 = false; // UTA: default value
		when(getFeeResult.isBlank()).thenReturn(isBlankResult5);
		return getFeeResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate18() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		long getTimeResult7 = 0L; // UTA: default value
		when(getFirstPayrollDateResult.getTime()).thenReturn(getTimeResult7);

		String toStringResult13 = ""; // UTA: default value
		when(getFirstPayrollDateResult.toString()).thenReturn(toStringResult13);
		return getFirstPayrollDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp2() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		String toStringResult14 = ""; // UTA: default value
		when(getLastUpdatedResult2.toString()).thenReturn(toStringResult14);
		return getLastUpdatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate19() throws Throwable {
		Date getMaturityDateResult = mock(Date.class);
		long getTimeResult8 = 0L; // UTA: default value
		when(getMaturityDateResult.getTime()).thenReturn(getTimeResult8);

		String toStringResult15 = ""; // UTA: default value
		when(getMaturityDateResult.toString()).thenReturn(toStringResult15);
		return getMaturityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter5() throws Throwable {
		LoanParameter getOriginalParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult2 = 0; // UTA: default value
		when(getOriginalParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult2);

		BigDecimal getInterestRateResult2 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getInterestRate()).thenReturn(getInterestRateResult2);

		BigDecimal getLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult2);

		BigDecimal getMaximumAvailableResult2 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult2);

		BigDecimal getPaymentAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult2);

		String getPaymentFrequencyResult2 = ""; // UTA: default value
		when(getOriginalParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult2);

		String getStatusCodeResult2 = ""; // UTA: default value
		when(getOriginalParameterResult.getStatusCode()).thenReturn(getStatusCodeResult2);

		boolean isReadyToSaveResult2 = false; // UTA: default value
		when(getOriginalParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult2);

		String toStringResult16 = ""; // UTA: default value
		when(getOriginalParameterResult.toString()).thenReturn(toStringResult16);
		return getOriginalParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp3() throws Throwable {
		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		String toStringResult17 = ""; // UTA: default value
		when(getLastUpdatedResult3.toString()).thenReturn(toStringResult17);
		return getLastUpdatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress2() throws Throwable {
		DistributionAddress getAddressResult2 = mock(DistributionAddress.class);
		String getAddressLine1Result3 = ""; // UTA: default value
		when(getAddressResult2.getAddressLine1()).thenReturn(getAddressLine1Result3);

		String getAddressLine2Result3 = ""; // UTA: default value
		when(getAddressResult2.getAddressLine2()).thenReturn(getAddressLine2Result3);

		String getCityResult3 = ""; // UTA: default value
		when(getAddressResult2.getCity()).thenReturn(getCityResult3);

		String getCountryCodeResult3 = ""; // UTA: default value
		when(getAddressResult2.getCountryCode()).thenReturn(getCountryCodeResult3);

		Timestamp getLastUpdatedResult3 = mockTimestamp3();
		when(getAddressResult2.getLastUpdated()).thenReturn(getLastUpdatedResult3);

		String getStateCodeResult3 = ""; // UTA: default value
		when(getAddressResult2.getStateCode()).thenReturn(getStateCodeResult3);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(getAddressResult2.getSubmissionId()).thenReturn(getSubmissionIdResult3);

		String getZipCodeResult3 = ""; // UTA: default value
		when(getAddressResult2.getZipCode()).thenReturn(getZipCodeResult3);

		boolean isBlankResult6 = false; // UTA: default value
		when(getAddressResult2.isBlank()).thenReturn(isBlankResult6);
		return getAddressResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient2() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult2 = mockDistributionAddress2();
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult2);

		BigDecimal getFederalTaxPercentResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getFederalTaxPercent()).thenReturn(getFederalTaxPercentResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getRecipientResult.getFirstName()).thenReturn(getFirstNameResult);

		String getLastNameResult = ""; // UTA: default value
		when(getRecipientResult.getLastName()).thenReturn(getLastNameResult);

		String getOrganizationNameResult = ""; // UTA: default value
		when(getRecipientResult.getOrganizationName()).thenReturn(getOrganizationNameResult);

		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		doReturn(getPayeesResult).when(getRecipientResult).getPayees();

		String getShareTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getShareTypeCode()).thenReturn(getShareTypeCodeResult);

		BigDecimal getShareValueResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getShareValue()).thenReturn(getShareValueResult);

		String getStateOfResidenceCodeResult = ""; // UTA: default value
		when(getRecipientResult.getStateOfResidenceCode()).thenReturn(getStateOfResidenceCodeResult);

		BigDecimal getStateTaxPercentResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getStateTaxPercent()).thenReturn(getStateTaxPercentResult);

		String getStateTaxTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getStateTaxTypeCode()).thenReturn(getStateTaxTypeCodeResult);

		String getTaxpayerIdentNoResult = ""; // UTA: default value
		when(getRecipientResult.getTaxpayerIdentNo()).thenReturn(getTaxpayerIdentNoResult);

		String getTaxpayerIdentTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getTaxpayerIdentTypeCode()).thenReturn(getTaxpayerIdentTypeCodeResult);

		Boolean getUsCitizenIndResult = false; // UTA: default value
		when(getRecipientResult.getUsCitizenInd()).thenReturn(getUsCitizenIndResult);

		String toStringResult18 = ""; // UTA: default value
		when(getRecipientResult.toString()).thenReturn(toStringResult18);
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate20() throws Throwable {
		Date getRequestDateResult = mock(Date.class);
		long getTimeResult9 = 0L; // UTA: default value
		when(getRequestDateResult.getTime()).thenReturn(getTimeResult9);

		String toStringResult19 = ""; // UTA: default value
		when(getRequestDateResult.toString()).thenReturn(toStringResult19);
		return getRequestDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter6() throws Throwable {
		LoanParameter getReviewedParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult3 = 0; // UTA: default value
		when(getReviewedParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult3);

		BigDecimal getInterestRateResult3 = BigDecimal.ZERO; // UTA: default value
		when(getReviewedParameterResult.getInterestRate()).thenReturn(getInterestRateResult3);

		BigDecimal getLoanAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getReviewedParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult3);

		BigDecimal getMaximumAvailableResult3 = BigDecimal.ZERO; // UTA: default value
		when(getReviewedParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult3);

		BigDecimal getPaymentAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getReviewedParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult3);

		String getPaymentFrequencyResult3 = ""; // UTA: default value
		when(getReviewedParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult3);

		String getStatusCodeResult3 = ""; // UTA: default value
		when(getReviewedParameterResult.getStatusCode()).thenReturn(getStatusCodeResult3);

		boolean isReadyToSaveResult3 = false; // UTA: default value
		when(getReviewedParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult3);

		String toStringResult20 = ""; // UTA: default value
		when(getReviewedParameterResult.toString()).thenReturn(toStringResult20);
		return getReviewedParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan2() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter4();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(loan.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);

		AtRiskDetailsVO getAtRiskDetailsVOResult = mockAtRiskDetailsVO2();
		when(loan.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);

		String getAtRiskIndResult = "Y"; // UTA: default value
		when(loan.getAtRiskInd()).thenReturn(getAtRiskIndResult);

		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);

		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote3();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote4();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate16();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		Date getExpirationDateResult = mockDate17();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee2();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate18();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult2 = mockTimestamp2();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		String getLoanReasonResult = ""; // UTA: default value
		when(loan.getLoanReason()).thenReturn(getLoanReasonResult);

		String getLoanTypeResult = ""; // UTA: default value
		when(loan.getLoanType()).thenReturn(getLoanTypeResult);

		List<ManagedContent> getManagedContentsResult = new ArrayList<ManagedContent>(); // UTA: default value
		doReturn(getManagedContentsResult).when(loan).getManagedContents();

		Date getMaturityDateResult = mockDate19();
		when(loan.getMaturityDate()).thenReturn(getMaturityDateResult);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(loan.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		BigDecimal getMaximumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult);

		BigDecimal getMaximumLoanPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		List<LoanMoneyType> getMoneyTypesResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(loan).getMoneyTypes();

		LoanParameter getOriginalParameterResult = mockLoanParameter5();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(loan.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(loan.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);

		String getPreviousStatusResult = ""; // UTA: default value
		when(loan.getPreviousStatus()).thenReturn(getPreviousStatusResult);

		LoanRecipient getRecipientResult = mockLoanRecipient2();
		when(loan.getRecipient()).thenReturn(getRecipientResult);

		Date getRequestDateResult = mockDate20();
		when(loan.getRequestDate()).thenReturn(getRequestDateResult);

		LoanParameter getReviewedParameterResult = mockLoanParameter6();
		when(loan.getReviewedParameter()).thenReturn(getReviewedParameterResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(loan.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);

		String getStatusResult = ""; // UTA: default value
		when(loan.getStatus()).thenReturn(getStatusResult);

		boolean isLoginUserPlanSponsorOrTpaResult = true; // UTA: default value
		when(loan.isLoginUserPlanSponsorOrTpa()).thenReturn(isLoginUserPlanSponsorOrTpaResult);

		boolean isParticipantInitiatedResult = true; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		String toStringResult21 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult21);
		return loan;
	}
	private static Loan mockLoan2_1() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter4();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(loan.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);

		AtRiskDetailsVO getAtRiskDetailsVOResult = mockAtRiskDetailsVO2();
		when(loan.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);

		String getAtRiskIndResult = "P"; // UTA: default value
		when(loan.getAtRiskInd()).thenReturn(getAtRiskIndResult);

		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);

		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote3();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote4();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate16();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		Date getExpirationDateResult = mockDate17();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee2();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate18();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult2 = mockTimestamp2();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		String getLoanReasonResult = ""; // UTA: default value
		when(loan.getLoanReason()).thenReturn(getLoanReasonResult);

		String getLoanTypeResult = ""; // UTA: default value
		when(loan.getLoanType()).thenReturn(getLoanTypeResult);

		List<ManagedContent> getManagedContentsResult = new ArrayList<ManagedContent>(); // UTA: default value
		doReturn(getManagedContentsResult).when(loan).getManagedContents();

		Date getMaturityDateResult = mockDate19();
		when(loan.getMaturityDate()).thenReturn(getMaturityDateResult);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(loan.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		BigDecimal getMaximumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult);

		BigDecimal getMaximumLoanPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		List<LoanMoneyType> getMoneyTypesResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(loan).getMoneyTypes();

		LoanParameter getOriginalParameterResult = mockLoanParameter5();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(loan.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(loan.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);

		String getPreviousStatusResult = "DRAFT"; // UTA: default value
		when(loan.getPreviousStatus()).thenReturn(getPreviousStatusResult);

		LoanRecipient getRecipientResult = mockLoanRecipient2();
		when(loan.getRecipient()).thenReturn(getRecipientResult);

		Date getRequestDateResult = mockDate20();
		when(loan.getRequestDate()).thenReturn(getRequestDateResult);

		LoanParameter getReviewedParameterResult = mockLoanParameter6();
		when(loan.getReviewedParameter()).thenReturn(getReviewedParameterResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(loan.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);

		String getStatusResult = ""; // UTA: default value
		when(loan.getStatus()).thenReturn(getStatusResult);

		boolean isLoginUserPlanSponsorOrTpaResult = true; // UTA: default value
		when(loan.isLoginUserPlanSponsorOrTpa()).thenReturn(isLoginUserPlanSponsorOrTpaResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		String toStringResult21 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult21);
		return loan;
	}
	private static Loan mockLoan2_2() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter4();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(loan.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);

		AtRiskDetailsVO getAtRiskDetailsVOResult = mockAtRiskDetailsVO2();
		when(loan.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);

		String getAtRiskIndResult = "P"; // UTA: default value
		when(loan.getAtRiskInd()).thenReturn(getAtRiskIndResult);

		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);

		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote3();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote4();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate16();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		Date getExpirationDateResult = mockDate17();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = null;
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate18();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult2 = mockTimestamp2();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		String getLoanReasonResult = ""; // UTA: default value
		when(loan.getLoanReason()).thenReturn(getLoanReasonResult);

		String getLoanTypeResult = ""; // UTA: default value
		when(loan.getLoanType()).thenReturn(getLoanTypeResult);

		List<ManagedContent> getManagedContentsResult = new ArrayList<ManagedContent>(); // UTA: default value
		doReturn(getManagedContentsResult).when(loan).getManagedContents();

		Date getMaturityDateResult = mockDate19();
		when(loan.getMaturityDate()).thenReturn(getMaturityDateResult);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(loan.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		BigDecimal getMaximumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult);

		BigDecimal getMaximumLoanPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		List<LoanMoneyType> getMoneyTypesResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(loan).getMoneyTypes();

		LoanParameter getOriginalParameterResult = mockLoanParameter5();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(loan.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(loan.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);

		String getPreviousStatusResult = ""; // UTA: default value
		when(loan.getPreviousStatus()).thenReturn(getPreviousStatusResult);

		LoanRecipient getRecipientResult = mockLoanRecipient2();
		when(loan.getRecipient()).thenReturn(getRecipientResult);

		Date getRequestDateResult = mockDate20();
		when(loan.getRequestDate()).thenReturn(getRequestDateResult);

		LoanParameter getReviewedParameterResult = mockLoanParameter6();
		when(loan.getReviewedParameter()).thenReturn(getReviewedParameterResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(loan.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);

		String getStatusResult = ""; // UTA: default value
		when(loan.getStatus()).thenReturn(getStatusResult);

		boolean isLoginUserPlanSponsorOrTpaResult = false; // UTA: default value
		when(loan.isLoginUserPlanSponsorOrTpa()).thenReturn(isLoginUserPlanSponsorOrTpaResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		String toStringResult21 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult21);
		return loan;
	}
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp4() throws Throwable {
		Timestamp timestamp = mock(Timestamp.class);
		String toStringResult22 = ""; // UTA: default value
		when(timestamp.toString()).thenReturn(toStringResult22);
		return timestamp;
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
		LoanDao underTest = new LoanDao();

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
}