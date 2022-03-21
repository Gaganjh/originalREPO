/**
 * 
 */
package com.manulife.pension.service.loan.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

import com.intware.dao.DAOException;
import com.manulife.pension.service.distribution.valueobject.AtRiskAddressChangeVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsInputVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskForgetUserName;
import com.manulife.pension.service.distribution.valueobject.AtRiskPasswordResetVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskWebRegistrationVO;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.util.JdbcHelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
 * Parasoft Jtest UTA: Test class for DistributionAtRiskDetailsDAO
 *
 * @see com.manulife.pension.service.loan.dao.DistributionAtRiskDetailsDAO
 * @author patelpo
 */
@PrepareForTest({ AtRiskAddressChangeVO.class, AtRiskForgetUserName.class, AtRiskDetailsInputVO.class, JdbcHelper.class,  Logger.class })
@RunWith(PowerMockRunner.class)
public class DistributionAtRiskDetailsDAOTest {
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
	 * Parasoft Jtest UTA: Test for insert(AtRiskDetailsVO)
	 *
	 * @see com.manulife.pension.service.loan.dao.DistributionAtRiskDetailsDAO#insert(AtRiskDetailsVO)
	 * @author patelpo
	 */
	@Test
	public void testInsert() throws Throwable {
		// When
		AtRiskDetailsVO objects = mockAtRiskDetailsVO();
		DistributionAtRiskDetailsDAO.insert(objects);

	}
	
	@Test
	public void testInsert_1() throws Throwable {
		// When
		AtRiskDetailsVO objects = null;
		DistributionAtRiskDetailsDAO.insert(objects);

	}
	@Test(expected=LoanDaoException.class)
	public void testInsert_2() throws Throwable {
		// When
		AtRiskDetailsVO objects = mockAtRiskDetailsVO_1();
		DistributionAtRiskDetailsDAO.insert(objects);

	}
	@Test
	public void testInsert_3() throws Throwable {
		// When
		AtRiskDetailsVO objects = mockAtRiskDetailsVO_2();
		DistributionAtRiskDetailsDAO.insert(objects);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Address
	 */
	private static Address mockAddress() throws Throwable {
		Address getApprovalAddressResult = mock(Address.class);
		Object cloneResult = new Object(); // UTA: default value
		when(getApprovalAddressResult.clone()).thenReturn(cloneResult);

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
	private static AtRiskAddressChangeVO mockAtRiskAddressChangeVO_1() throws Throwable {
		AtRiskAddressChangeVO getAddresschangeResult = mock(AtRiskAddressChangeVO.class);
		Address getApprovalAddressResult = null;
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
	private static Date mockDate() throws Throwable {
		Date getForgotPasswordRequestedDateResult = mock(Date.class);
		Object cloneResult2 = new Object(); // UTA: default value
		when(getForgotPasswordRequestedDateResult.clone()).thenReturn(cloneResult2);

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

		Date getForgotPasswordRequestedDateResult = mockDate();
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
	private static Date mockDate2() throws Throwable {
		Date getEmailPasswordResetDateResult = mock(Date.class);
		Object cloneResult3 = new Object(); // UTA: default value
		when(getEmailPasswordResetDateResult.clone()).thenReturn(cloneResult3);

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

		Date getEmailPasswordResetDateResult = mockDate2();
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
		Object cloneResult4 = new Object(); // UTA: default value
		when(getAddressResult.clone()).thenReturn(cloneResult4);

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
		return getAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate3() throws Throwable {
		Date getWebRegConfirmationMailedDateResult = mock(Date.class);
		Object cloneResult5 = new Object(); // UTA: default value
		when(getWebRegConfirmationMailedDateResult.clone()).thenReturn(cloneResult5);

		long getTimeResult3 = 0L; // UTA: default value
		when(getWebRegConfirmationMailedDateResult.getTime()).thenReturn(getTimeResult3);

		String toStringResult3 = ""; // UTA: default value
		when(getWebRegConfirmationMailedDateResult.toString()).thenReturn(toStringResult3);
		return getWebRegConfirmationMailedDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate4() throws Throwable {
		Date getWebRegistrationDateResult = mock(Date.class);
		Object cloneResult6 = new Object(); // UTA: default value
		when(getWebRegistrationDateResult.clone()).thenReturn(cloneResult6);

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

		Date getWebRegConfirmationMailedDateResult = mockDate3();
		when(getWebRegistrationResult.getWebRegConfirmationMailedDate())
				.thenReturn(getWebRegConfirmationMailedDateResult);

		Date getWebRegistrationDateResult = mockDate4();
		when(getWebRegistrationResult.getWebRegistrationDate()).thenReturn(getWebRegistrationDateResult);
		return getWebRegistrationResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskDetailsVO
	 */
	private static AtRiskDetailsVO mockAtRiskDetailsVO() throws Throwable {
		AtRiskDetailsVO objects = mock(AtRiskDetailsVO.class);
		AtRiskAddressChangeVO getAddresschangeResult = mockAtRiskAddressChangeVO();
		when(objects.getAddresschange()).thenReturn(getAddresschangeResult);

		AtRiskForgetUserName getForgetUserNameResult = mockAtRiskForgetUserName();
		when(objects.getForgetUserName()).thenReturn(getForgetUserNameResult);

		AtRiskPasswordResetVO getPasswordResetResult = mockAtRiskPasswordResetVO();
		when(objects.getPasswordReset()).thenReturn(getPasswordResetResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(objects.getSubmissionId()).thenReturn(getSubmissionIdResult);

		AtRiskWebRegistrationVO getWebRegistrationResult = mockAtRiskWebRegistrationVO();
		when(objects.getWebRegistration()).thenReturn(getWebRegistrationResult);
		return objects;
	}
	private static AtRiskDetailsVO mockAtRiskDetailsVO_1() throws Throwable {
		AtRiskDetailsVO objects = mock(AtRiskDetailsVO.class);
		AtRiskAddressChangeVO getAddresschangeResult = mockAtRiskAddressChangeVO();
		when(objects.getAddresschange()).thenReturn(getAddresschangeResult);

		AtRiskForgetUserName getForgetUserNameResult = mockAtRiskForgetUserName();
		when(objects.getForgetUserName()).thenReturn(getForgetUserNameResult);

		AtRiskPasswordResetVO getPasswordResetResult = mockAtRiskPasswordResetVO();
		when(objects.getPasswordReset()).thenReturn(getPasswordResetResult);

		Integer getSubmissionIdResult = null; // UTA: default value
		when(objects.getSubmissionId()).thenReturn(getSubmissionIdResult);

		AtRiskWebRegistrationVO getWebRegistrationResult = mockAtRiskWebRegistrationVO();
		when(objects.getWebRegistration()).thenReturn(getWebRegistrationResult);
		return objects;
	}
	private static AtRiskDetailsVO mockAtRiskDetailsVO_2() throws Throwable {
		AtRiskDetailsVO objects = mock(AtRiskDetailsVO.class);
		AtRiskAddressChangeVO getAddresschangeResult = null;
		when(objects.getAddresschange()).thenReturn(getAddresschangeResult);

		AtRiskForgetUserName getForgetUserNameResult = null;
		when(objects.getForgetUserName()).thenReturn(getForgetUserNameResult);

		AtRiskPasswordResetVO getPasswordResetResult = null;
		when(objects.getPasswordReset()).thenReturn(getPasswordResetResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(objects.getSubmissionId()).thenReturn(getSubmissionIdResult);

		AtRiskWebRegistrationVO getWebRegistrationResult = null;
		when(objects.getWebRegistration()).thenReturn(getWebRegistrationResult);
		return objects;
	}

	/**
	 * Parasoft Jtest UTA: Test for insertOrUpdate(List)
	 *
	 * @see com.manulife.pension.service.loan.dao.DistributionAtRiskDetailsDAO#insertOrUpdate(List)
	 * @author patelpo
	 */
	@Test
	public void testInsertOrUpdate() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		
		// When
		List<AtRiskDetailsVO> objects = new ArrayList<AtRiskDetailsVO>(); // UTA: default value
		AtRiskDetailsVO atRiskDetailsVO = mock(AtRiskDetailsVO.class);
		when(atRiskDetailsVO.getSubmissionId()).thenReturn(10);
		objects.add(atRiskDetailsVO);
		
		
		DistributionAtRiskDetailsDAO.insertOrUpdate(objects);

	}
	@Test(expected = NullPointerException.class)
	public void testInsertOrUpdate_1() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		
		// When
		List<AtRiskDetailsVO> objects = null; // UTA: default value
		AtRiskDetailsVO atRiskDetailsVO = mock(AtRiskDetailsVO.class);
		objects.add(atRiskDetailsVO);
		
		
		DistributionAtRiskDetailsDAO.insertOrUpdate(objects);

	}
	
	@Test(expected=LoanDaoException.class)
	public void testInsertOrUpdate_Exception() throws Throwable {
		when(getConnectionResult.prepareStatement(anyString())).thenThrow(new SQLException());

		// When
		List<AtRiskDetailsVO> objects = new ArrayList<AtRiskDetailsVO>(); // UTA: default value
		AtRiskDetailsVO atRiskDetailsVO = mock(AtRiskDetailsVO.class);
		when(atRiskDetailsVO.getSubmissionId()).thenReturn(null);
		objects.add(atRiskDetailsVO);
		
		DistributionAtRiskDetailsDAO.insertOrUpdate(objects);

	}

	/**
	 * Parasoft Jtest UTA: Test for isWebConfirmationLetterAvailable(AtRiskDetailsInputVO, Timestamp)
	 *
	 * @see com.manulife.pension.service.loan.dao.DistributionAtRiskDetailsDAO#isWebConfirmationLetterAvailable(AtRiskDetailsInputVO, Timestamp)
	 * @author patelpo
	 */
	@Test
	public void testIsWebConfirmationLetterAvailable() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		// When
		AtRiskDetailsInputVO atRiskDetils = mockAtRiskDetailsInputVO();
		Timestamp createdTs = mock(Timestamp.class);
		boolean result = DistributionAtRiskDetailsDAO.isWebConfirmationLetterAvailable(atRiskDetils, createdTs);

		// Then
		// assertFalse(result);
	}
	@Test(expected = LoanDaoException.class)
	public void testIsWebConfirmationLetterAvailable_Exception() throws Throwable {
		when(getConnectionResult.prepareStatement(anyString())).thenThrow(new SQLException());

		// When
		AtRiskDetailsInputVO atRiskDetils = mockAtRiskDetailsInputVO();
		Timestamp createdTs = mock(Timestamp.class);
		boolean result = DistributionAtRiskDetailsDAO.isWebConfirmationLetterAvailable(atRiskDetils, createdTs);

		// Then
		// assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskDetailsInputVO
	 */
	private static AtRiskDetailsInputVO mockAtRiskDetailsInputVO() throws Throwable {
		AtRiskDetailsInputVO atRiskDetils = mock(AtRiskDetailsInputVO.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(atRiskDetils.getContractId()).thenReturn(getContractIdResult);

		Integer getProfileIdResult = 0; // UTA: default value
		when(atRiskDetils.getProfileId()).thenReturn(getProfileIdResult);
		return atRiskDetils;
	}

	/**
	 * Parasoft Jtest UTA: Test for retrieve(AtRiskDetailsInputVO)
	 *
	 * @see com.manulife.pension.service.loan.dao.DistributionAtRiskDetailsDAO#retrieve(AtRiskDetailsInputVO)
	 * @author patelpo
	 */
	@Test
	public void testRetrieve() throws Throwable {
		when(executeQueryResult.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		
		// When
		AtRiskDetailsInputVO atRiskDetils = mockAtRiskDetailsInputVO2();
		AtRiskDetailsVO result = DistributionAtRiskDetailsDAO.retrieve(atRiskDetils);

		// Then
		 assertNotNull(result);
	}
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskDetailsInputVO
	 */
	private static AtRiskDetailsInputVO mockAtRiskDetailsInputVO2() throws Throwable {
		AtRiskDetailsInputVO atRiskDetils = mock(AtRiskDetailsInputVO.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(atRiskDetils.getContractId()).thenReturn(getContractIdResult);

		Integer getProfileIdResult = 0; // UTA: default value
		when(atRiskDetils.getProfileId()).thenReturn(getProfileIdResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(atRiskDetils.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return atRiskDetils;
	}

	/**
	 * Parasoft Jtest UTA: Test for update(AtRiskDetailsVO)
	 *
	 * @see com.manulife.pension.service.loan.dao.DistributionAtRiskDetailsDAO#update(AtRiskDetailsVO)
	 * @author patelpo
	 */
	@Test
	public void testUpdate() throws Throwable {
		// When
		AtRiskDetailsVO objects = mockAtRiskDetailsVO2();
		DistributionAtRiskDetailsDAO.update(objects);

	}
	@Test
	public void testUpdate_1() throws Throwable {
		// When
		AtRiskDetailsVO objects = null;
		DistributionAtRiskDetailsDAO.update(objects);

	}
	@Test
	public void testUpdate_2() throws Throwable {
		// When
		AtRiskDetailsVO objects = mockAtRiskDetailsVO2_1();
		DistributionAtRiskDetailsDAO.update(objects);

	}
	
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Address
	 */
	private static Address mockAddress3() throws Throwable {
		Address getApprovalAddressResult = mock(Address.class);
		Object cloneResult = new Object(); // UTA: default value
		when(getApprovalAddressResult.clone()).thenReturn(cloneResult);

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
	private static Date mockDate5() throws Throwable {
		Date getForgotPasswordRequestedDateResult = mock(Date.class);
		Object cloneResult2 = new Object(); // UTA: default value
		when(getForgotPasswordRequestedDateResult.clone()).thenReturn(cloneResult2);

		long getTimeResult = 0L; // UTA: default value
		when(getForgotPasswordRequestedDateResult.getTime()).thenReturn(getTimeResult);

		String toStringResult = ""; // UTA: default value
		when(getForgotPasswordRequestedDateResult.toString()).thenReturn(toStringResult);
		return getForgotPasswordRequestedDateResult;
	}
	private static Date mockDate5_1() throws Throwable {
		Date getForgotPasswordRequestedDateResult = mock(Date.class);
		Object cloneResult2 = new Object(); // UTA: default value
		when(getForgotPasswordRequestedDateResult.clone()).thenReturn(cloneResult2);

		long getTimeResult = 0l; // UTA: default value
		when(getForgotPasswordRequestedDateResult.getTime()).thenReturn(getTimeResult);

		String toStringResult = null; // UTA: default value
		when(getForgotPasswordRequestedDateResult.toString()).thenReturn(toStringResult);
		return getForgotPasswordRequestedDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskForgetUserName
	 */
	private static AtRiskForgetUserName mockAtRiskForgetUserName2() throws Throwable {
		AtRiskForgetUserName getForgetUserNameResult = mock(AtRiskForgetUserName.class);
		String getForgotPasswordEmailAddressResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordEmailAddress()).thenReturn(getForgotPasswordEmailAddressResult);

		Date getForgotPasswordRequestedDateResult = mockDate5();
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
	private static AtRiskForgetUserName mockAtRiskForgetUserName2_1() throws Throwable {
		AtRiskForgetUserName getForgetUserNameResult = mock(AtRiskForgetUserName.class);
		String getForgotPasswordEmailAddressResult = null; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordEmailAddress()).thenReturn(getForgotPasswordEmailAddressResult);

		Date getForgotPasswordRequestedDateResult = mockDate5_1();
		when(getForgetUserNameResult.getForgotPasswordRequestedDate()).thenReturn(getForgotPasswordRequestedDateResult);

		Integer getForgotPasswordUpdatedProfileIdResult = null; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedProfileId())
				.thenReturn(getForgotPasswordUpdatedProfileIdResult);

		String getForgotPasswordUpdatedUserFirstNameResult = null; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserFirstName())
				.thenReturn(getForgotPasswordUpdatedUserFirstNameResult);

		String getForgotPasswordUpdatedUserIdTypeResult = null; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserIdType())
				.thenReturn(getForgotPasswordUpdatedUserIdTypeResult);

		String getForgotPasswordUpdatedUserLastNameResult = null; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserLastName())
				.thenReturn(getForgotPasswordUpdatedUserLastNameResult);
		return getForgetUserNameResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate6() throws Throwable {
		Date getEmailPasswordResetDateResult = mock(Date.class);
		Object cloneResult3 = new Object(); // UTA: default value
		when(getEmailPasswordResetDateResult.clone()).thenReturn(cloneResult3);

		long getTimeResult2 = 0L; // UTA: default value
		when(getEmailPasswordResetDateResult.getTime()).thenReturn(getTimeResult2);

		String toStringResult2 = ""; // UTA: default value
		when(getEmailPasswordResetDateResult.toString()).thenReturn(toStringResult2);
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

		Date getEmailPasswordResetDateResult = mockDate6();
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
		Object cloneResult4 = new Object(); // UTA: default value
		when(getAddressResult.clone()).thenReturn(cloneResult4);

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
		return getAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate7() throws Throwable {
		Date getWebRegConfirmationMailedDateResult = mock(Date.class);
		Object cloneResult5 = new Object(); // UTA: default value
		when(getWebRegConfirmationMailedDateResult.clone()).thenReturn(cloneResult5);

		long getTimeResult3 = 0L; // UTA: default value
		when(getWebRegConfirmationMailedDateResult.getTime()).thenReturn(getTimeResult3);

		String toStringResult3 = ""; // UTA: default value
		when(getWebRegConfirmationMailedDateResult.toString()).thenReturn(toStringResult3);
		return getWebRegConfirmationMailedDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate8() throws Throwable {
		Date getWebRegistrationDateResult = mock(Date.class);
		Object cloneResult6 = new Object(); // UTA: default value
		when(getWebRegistrationDateResult.clone()).thenReturn(cloneResult6);

		long getTimeResult4 = 0L; // UTA: default value
		when(getWebRegistrationDateResult.getTime()).thenReturn(getTimeResult4);

		String toStringResult4 = ""; // UTA: default value
		when(getWebRegistrationDateResult.toString()).thenReturn(toStringResult4);
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

		Date getWebRegConfirmationMailedDateResult = mockDate7();
		when(getWebRegistrationResult.getWebRegConfirmationMailedDate())
				.thenReturn(getWebRegConfirmationMailedDateResult);

		Date getWebRegistrationDateResult = mockDate8();
		when(getWebRegistrationResult.getWebRegistrationDate()).thenReturn(getWebRegistrationDateResult);
		return getWebRegistrationResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskDetailsVO
	 */
	private static AtRiskDetailsVO mockAtRiskDetailsVO2() throws Throwable {
		AtRiskDetailsVO objects = mock(AtRiskDetailsVO.class);
		AtRiskAddressChangeVO getAddresschangeResult = mockAtRiskAddressChangeVO2();
		when(objects.getAddresschange()).thenReturn(getAddresschangeResult);

		AtRiskForgetUserName getForgetUserNameResult = mockAtRiskForgetUserName2();
		when(objects.getForgetUserName()).thenReturn(getForgetUserNameResult);

		AtRiskPasswordResetVO getPasswordResetResult = mockAtRiskPasswordResetVO2();
		when(objects.getPasswordReset()).thenReturn(getPasswordResetResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(objects.getSubmissionId()).thenReturn(getSubmissionIdResult);

		AtRiskWebRegistrationVO getWebRegistrationResult = mockAtRiskWebRegistrationVO2();
		when(objects.getWebRegistration()).thenReturn(getWebRegistrationResult);
		return objects;
	}
	private static AtRiskDetailsVO mockAtRiskDetailsVO2_1() throws Throwable {
		AtRiskDetailsVO objects = mock(AtRiskDetailsVO.class);
		AtRiskAddressChangeVO getAddresschangeResult = mockAtRiskAddressChangeVO2();
		when(objects.getAddresschange()).thenReturn(getAddresschangeResult);

		AtRiskForgetUserName getForgetUserNameResult = mockAtRiskForgetUserName2_1();
		when(objects.getForgetUserName()).thenReturn(getForgetUserNameResult);

		AtRiskPasswordResetVO getPasswordResetResult = mockAtRiskPasswordResetVO2();
		when(objects.getPasswordReset()).thenReturn(getPasswordResetResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(objects.getSubmissionId()).thenReturn(getSubmissionIdResult);

		AtRiskWebRegistrationVO getWebRegistrationResult = mockAtRiskWebRegistrationVO2();
		when(objects.getWebRegistration()).thenReturn(getWebRegistrationResult);
		return objects;
	}
	private static AtRiskDetailsVO mockAtRiskDetailsVO2_SubmissionID() throws Throwable {
		AtRiskDetailsVO objects = mock(AtRiskDetailsVO.class);
		AtRiskAddressChangeVO getAddresschangeResult = mockAtRiskAddressChangeVO2();
		when(objects.getAddresschange()).thenReturn(getAddresschangeResult);

		AtRiskForgetUserName getForgetUserNameResult = mockAtRiskForgetUserName2_1();
		when(objects.getForgetUserName()).thenReturn(getForgetUserNameResult);

		AtRiskPasswordResetVO getPasswordResetResult = mockAtRiskPasswordResetVO2();
		when(objects.getPasswordReset()).thenReturn(getPasswordResetResult);

		Integer getSubmissionIdResult = null; // UTA: default value
		when(objects.getSubmissionId()).thenReturn(getSubmissionIdResult);

		AtRiskWebRegistrationVO getWebRegistrationResult = mockAtRiskWebRegistrationVO2();
		when(objects.getWebRegistration()).thenReturn(getWebRegistrationResult);
		return objects;
	}
}