/*

use these one to delete all loan requests
delete from stp100.DECLARATION  where submission_id in ( select submission_id from stp100.submission_loan )
delete from stp100.DISTRIBUTION_ADDRESS  where submission_id in ( select submission_id from stp100.submission_loan )
delete from stp100.FEE  where submission_id in ( select submission_id from stp100.submission_loan )
delete from stp100.NOTE  where submission_id in ( select submission_id from stp100.submission_loan )
delete from stp100.PAYEE_PAYMENT_INSTRUCTION  where submission_id in ( select submission_id from stp100.submission_loan )
delete from stp100.PAYEE  where submission_id in ( select submission_id from stp100.submission_loan )
delete from stp100.RECIPIENT  where submission_id in ( select submission_id from stp100.submission_loan 	)
delete from stp100.loan_parameter_history  where submission_id in ( select submission_id from stp100.submission_loan)
delete from stp100.managed_content where submission_id in ( select submission_id from stp100.submission_loan)
delete from stp100.loan_money_type where submission_id in ( select submission_id from stp100.submission_loan)
delete from stp100.SUBMISSION_LOAN where submission_id in (select submission_id from stp100.submission_case where submission_case_type_code = 'L')
delete from stp100.SUBMISSION_CASE where submission_case_type_code = 'L'
delete from stp100.SUBMISSION where submission_id not in (select submission_id from stp100.submission_case)	

 */
package com.manulife.pension.service.loan.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.dao.DeclarationDao;
import com.manulife.pension.service.distribution.dao.DistributionJUnitConstants;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.testutility.MockContainerEnvironmentTestCase;
import com.manulife.pension.util.JdbcHelper;

/**
 * DeclarationDaoTest is the test class for the {@link DeclarationDao} class.
 * 
 * 
 */
public class LoanDaoTestOld extends MockContainerEnvironmentTestCase {

	private static final String SELECT_SUBMISSION = new String(
			"select SUBMISSION_ID,SUBMISSION_TS,FILE_NAME,MAP_NAME,INPUT_LOCATION_NAME,USER_ID,CREATED_USER_ID,CREATED_TS,LAST_UPDATED_USER_ID,LAST_UPDATED_TS,PAYMENT_INFO_ONLY_IND from stp100.submission where submission_id = ?");
	private static final String SELECT_SUBMISSION_CASE = new String(
			"select SUBMISSION_ID,CONTRACT_ID,SUBMISSION_CASE_TYPE_CODE,SYNTAX_ERROR_IND,PROCESSED_TS,PROCESS_STATUS_CODE,CREATED_USER_ID,CREATED_TS,LAST_UPDATED_USER_ID,LAST_UPDATED_TS,LAST_LOCKED_BY_USER_ID,LAST_LOCKED_TS from stp100.submission_case where submission_id = ?");
	private static final String SELECT_SUBMISSION_LOAN = new String(
			"select SUBMISSION_ID,CONTRACT_ID,SUBMISSION_CASE_TYPE_CODE,PROFILE_ID,PARTICIPANT_ID,CREATED_BY_ROLE_CODE,LEGALLY_MARRIED_IND,LOAN_TYPE_CODE,LOAN_REASON_EXPLANATION,REQUEST_DATE,EXPIRATION_DATE,LOAN_EFFECTIVE_DATE,LOAN_MATURITY_DATE,FIRST_PAYROLL_DATE,MAX_AMORTIZATION_YEARS,DEFAULT_PROVISION,MAX_OS_LOAN_BAL_LAST12MTHS_AMT,OUTSTANDING_LOANS_COUNT,CURR_OUTSTANDING_LOAN_BAL_AMT,CREATED_USER_PROFILE_ID,CREATED_TS,LAST_UPDATED_USER_PROFILE_ID,LAST_UPDATED_TS, APPLY_IRS_10K_DOLLAR_RULE_IND from stp100.submission_loan where submission_id = ?");
	private static final String SUBMISSION_DATA_SOURCE_NAME = "jdbc/customerService";
	private Loan voA1 = null;
	private Loan voA2 = null;

	private LoanDao dao = null;
	private DataSource datasource = null;

	public LoanDaoTestOld(String arg0) {
		super(arg0);

	}

	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTest(new LoanDaoTestOld("testInsert"));
		suite.addTest(new LoanDaoTestOld("testUpdate"));
		suite.addTest(new LoanDaoTestOld("testRead"));
		suite.addTest(new LoanDaoTestOld("testDelete"));
		return suite;
	}

	public void setUp() throws Exception {

		super.setUp();

		if (dao == null) {
			dao = new LoanDao();
		}
		if (datasource == null) {
			datasource = (DataSource) new InitialContext()
					.lookup(SUBMISSION_DATA_SOURCE_NAME);
		}
		if (voA1 == null) {
			setupValueObjects();
		}

	}

	private void setupValueObjects() {
		voA1 = new Loan();
		voA2 = new Loan();

		voA1.setSubmissionId(null);
		voA1.setContractId(DistributionJUnitConstants.TEST_CONTRACT_ID);
		voA1.setParticipantProfileId(22222);
		voA1.setParticipantId(3333);
		voA1.setCreatedByRoleCode("PS");
		voA1.setLegallyMarriedInd(true);
		voA1.setApplyIrs10KDollarRuleInd(false);
		voA1.setLoanType("T1");
		voA1.setLoanReason("reason 1");
		voA1.setRequestDate(new Date(111222));
		voA1.setExpirationDate(new Date(111333));
		voA1.setEffectiveDate(new Date(111444));
		voA1.setMaturityDate(new Date(111555));
		voA1.setFirstPayrollDate(new Date(111666));
		voA1.setMaximumAmortizationYears(1);
		voA1.setDefaultProvision("provision1");
		voA1.setMaxBalanceLast12Months(new BigDecimal(10.35).setScale(2,
				BigDecimal.ROUND_FLOOR));
		voA1.setOutstandingLoansCount(1);
		voA1.setCurrentOutstandingBalance(new BigDecimal(11.35).setScale(2,
				BigDecimal.ROUND_FLOOR));
		voA1.setCreated(DistributionJUnitConstants.TEST_TIMESTAMP_1);
		voA1.setLastUpdated(DistributionJUnitConstants.TEST_TIMESTAMP_1);
		voA1.setLastUpdatedId(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voA1.setCreatedId(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voA1.setStatus(LoanStateEnum.DELETED.getStatusCode());
		voA1.setSpousalConsentReqdInd("Y");

		voA2.setSubmissionId(null);
		voA2.setContractId(DistributionJUnitConstants.TEST_CONTRACT_ID);
		voA2.setParticipantProfileId(22222);
		voA2.setParticipantId(3333);
		voA2.setCreatedByRoleCode(voA1.getCreatedByRoleCode());
		voA2.setLegallyMarriedInd(false);
		voA2.setApplyIrs10KDollarRuleInd(true);
		voA2.setLoanType("T2");
		voA2.setLoanReason("reason 2");
		voA2.setRequestDate(new Date(111223));
		voA2.setExpirationDate(new Date(111334));
		voA2.setEffectiveDate(new Date(111445));
		voA2.setMaturityDate(new Date(111556));
		voA2.setFirstPayrollDate(new Date(111667));
		voA2.setMaximumAmortizationYears(2);
		voA2.setDefaultProvision("provision2");
		voA2.setMaxBalanceLast12Months(new BigDecimal(10.36).setScale(2,
				BigDecimal.ROUND_FLOOR));
		voA2.setOutstandingLoansCount(2);
		voA2.setCurrentOutstandingBalance(new BigDecimal(11.36).setScale(2,
				BigDecimal.ROUND_FLOOR));
		voA2.setCreated(DistributionJUnitConstants.TEST_TIMESTAMP_1);
		voA2.setLastUpdated(DistributionJUnitConstants.TEST_TIMESTAMP_2);
		voA2.setLastUpdatedId(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);
		voA2.setCreatedId(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voA2.setStatus("2");
		voA1.setSpousalConsentReqdInd("N");

	}

	/**
	 * @throws Exception
	 */
	public void testInsert() throws Exception {

		Integer submissionId = null;

		submissionId = dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1,
				DistributionJUnitConstants.TEST_TIMESTAMP_1);

		voA1.setSubmissionId(submissionId);
		verifyValues(voA1, createVoFromDb(submissionId));

		dao.delete(submissionId, DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		// this tests that the parameter types length is equal
		// to the parameters that i pass Intware handler.
		String oldVal = voA1.getLoanReason();
		voA1.setLoanReason(null);
		submissionId = dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1,
				DistributionJUnitConstants.TEST_TIMESTAMP_1);
		voA1.setLoanReason(oldVal);

		dao.delete(submissionId, DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID);

	}

	public void testRead() throws Exception {
		Integer submissionId = null;

		submissionId = dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1,
				DistributionJUnitConstants.TEST_TIMESTAMP_1);

		Loan loan = dao.read(submissionId, DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		verifyValues(voA1, loan);
		dao.delete(submissionId, DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID);

	}

	public void testDelete() throws Exception {
		Integer submissionId = null;

		submissionId = dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1,
				DistributionJUnitConstants.TEST_TIMESTAMP_1);

		dao.delete(submissionId, DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		assertTrue("record was not deleted", !exists(submissionId,
				SELECT_SUBMISSION)
				&& !exists(submissionId, SELECT_SUBMISSION_CASE)
				&& !exists(submissionId, SELECT_SUBMISSION_LOAN));

	}

	public void testUpdate() throws Exception {
		Integer submissionId = null;

		// #1
		// this one has a different status and therefore processed timestamp
		// should be updated to the new timestamp
		submissionId = dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1,
				DistributionJUnitConstants.TEST_TIMESTAMP_1);
		dao.update(submissionId, DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID_2, voA2,
				DistributionJUnitConstants.TEST_TIMESTAMP_2);
		verifyValues(voA2, createVoFromDb(submissionId));
		dao.delete(submissionId, DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		// #2
		// this one has a same status and therefore processed timestamp should
		// not be updated.
		submissionId = dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1,
				DistributionJUnitConstants.TEST_TIMESTAMP_1);
		voA2.setStatus(voA1.getStatus());
		dao.update(submissionId, DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID_2, voA2,
				DistributionJUnitConstants.TEST_TIMESTAMP_2);
		verifyValues(voA2, createVoFromDb(submissionId));
		dao.delete(submissionId, DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		// #3
		// test the parameter length is accurate.
		String oldVal = voA1.getLoanReason();
		voA1.setLoanReason(null);
		submissionId = dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1,
				DistributionJUnitConstants.TEST_TIMESTAMP_1);
		dao.update(submissionId, DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID_2, voA2,
				DistributionJUnitConstants.TEST_TIMESTAMP_2);
		voA1.setLoanReason(oldVal);

		dao.delete(submissionId, DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID);

	}

	private Loan createVoFromDb(Integer submissionId) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Loan returnVo = new Loan();
		try {
			conn = datasource.getConnection();
			stmt = conn.prepareStatement(SELECT_SUBMISSION_LOAN);
			stmt.setInt(1, submissionId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				returnVo.setSubmissionId(rs.getInt("SUBMISSION_ID"));
				returnVo.setContractId(rs.getInt("CONTRACT_ID"));
				returnVo.setParticipantProfileId(rs.getInt("PROFILE_ID"));
				returnVo.setParticipantId(rs.getInt("PARTICIPANT_ID"));
				returnVo.setCreatedByRoleCode(rs
						.getString("CREATED_BY_ROLE_CODE"));
				returnVo.setLegallyMarriedInd(JdbcHelper.getBoolean(rs,
						"LEGALLY_MARRIED_IND", false));
				returnVo.setApplyIrs10KDollarRuleInd(JdbcHelper.getBoolean(rs,
						"APPLY_IRS_10K_DOLLAR_RULE_IND", false));
				returnVo.setLoanType(rs.getString("LOAN_TYPE_CODE"));
				returnVo.setLoanReason(rs.getString("LOAN_REASON_EXPLANATION"));
				returnVo.setRequestDate(JdbcHelper.getUtilDate(rs,
						"REQUEST_DATE"));
				returnVo.setExpirationDate(JdbcHelper.getUtilDate(rs,
						"EXPIRATION_DATE"));
				returnVo.setEffectiveDate(JdbcHelper.getUtilDate(rs,
						"LOAN_EFFECTIVE_DATE"));
				returnVo.setMaturityDate(JdbcHelper.getUtilDate(rs,
						"LOAN_MATURITY_DATE"));
				returnVo.setFirstPayrollDate(JdbcHelper.getUtilDate(rs,
						"FIRST_PAYROLL_DATE"));
				returnVo.setMaximumAmortizationYears(rs
						.getInt("MAX_AMORTIZATION_YEARS"));
				returnVo.setDefaultProvision(rs.getString("DEFAULT_PROVISION"));
				returnVo.setMaxBalanceLast12Months(rs
						.getBigDecimal("MAX_OS_LOAN_BAL_LAST12MTHS_AMT"));
				returnVo.setOutstandingLoansCount(rs
						.getInt("OUTSTANDING_LOANS_COUNT"));
				returnVo.setCurrentOutstandingBalance(rs
						.getBigDecimal("CURR_OUTSTANDING_LOAN_BAL_AMT"));
				returnVo.setCreatedId(rs.getInt("CREATED_USER_PROFILE_ID"));
				returnVo.setCreated(rs.getTimestamp("CREATED_TS"));
				returnVo.setLastUpdatedId(rs
						.getInt("LAST_UPDATED_USER_PROFILE_ID"));
				returnVo.setLastUpdated(rs.getTimestamp("LAST_UPDATED_TS"));

				rs.close();
				stmt.close();
				stmt = conn.prepareStatement(SELECT_SUBMISSION_CASE);
				stmt.setInt(1, submissionId);
				rs = stmt.executeQuery();
				if (rs.next()) {
					returnVo.setStatus(rs.getString(6));
				}
			}
			return returnVo;
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
	}

	public void verifyValues(Loan vo1, Loan vo2) {
		assertEquals(vo1.getContractId(), vo2.getContractId());
		assertEquals(vo1.getParticipantProfileId(), vo2
				.getParticipantProfileId());
		assertEquals(vo1.getParticipantId(), vo2.getParticipantId());
		assertEquals(vo1.getCreatedByRoleCode(), vo2.getCreatedByRoleCode());
		assertEquals(vo1.getLegallyMarriedInd(), vo2.getLegallyMarriedInd());
		assertEquals(vo1.getApplyIrs10KDollarRuleInd(), vo2.getApplyIrs10KDollarRuleInd());
		assertEquals(vo1.getLoanType(), vo2.getLoanType());
		assertEquals(vo1.getLoanReason(), vo2.getLoanReason());
		verifyDate(vo1.getRequestDate(), vo2.getRequestDate());
		verifyDate(vo1.getExpirationDate(), vo2.getExpirationDate());
		verifyDate(vo1.getEffectiveDate(), vo2.getEffectiveDate());
		verifyDate(vo1.getMaturityDate(), vo2.getMaturityDate());
		verifyDate(vo1.getFirstPayrollDate(), vo2.getFirstPayrollDate());
		assertEquals(vo1.getMaximumAmortizationYears(), vo2
				.getMaximumAmortizationYears());
		assertEquals(vo1.getDefaultProvision(), vo2.getDefaultProvision());
		assertEquals(vo1.getMaxBalanceLast12Months(), vo2
				.getMaxBalanceLast12Months());
		assertEquals(vo1.getOutstandingLoansCount(), vo2
				.getOutstandingLoansCount());
		assertEquals(vo1.getCurrentOutstandingBalance(), vo2
				.getCurrentOutstandingBalance());
		assertEquals(vo1.getCreatedId(), vo2.getCreatedId());
		assertEquals(vo1.getCreated(), vo2.getCreated());
		assertEquals(vo1.getLastUpdatedId(), vo2.getLastUpdatedId());
		assertEquals(vo1.getLastUpdated(), vo2.getLastUpdated());

	}

	private void verifyDate(Date birthDate, Date date) {

		Calendar c1 = new GregorianCalendar();
		Calendar c2 = new GregorianCalendar();
		c1.setTimeInMillis(birthDate.getTime());
		c2.setTimeInMillis(date.getTime());

		assertEquals(c1.get(Calendar.YEAR), c1.get(Calendar.YEAR));
		assertEquals(c1.get(Calendar.DAY_OF_MONTH), c1
				.get(Calendar.DAY_OF_MONTH));
		assertEquals(c1.get(Calendar.MONTH), c1.get(Calendar.MONTH));

	}

	/**
	 * This record calls the withdrawalDAO.insert to insert a dummy record. This
	 * is required for all of the other DAO tests, since they all need foreign
	 * key's to a real submission. call update at end of function to fully
	 * populate all fields
	 * 
	 * 
	 */
	public void insertReference(int count) throws Exception {

		insertSubSubCaseSubLoan();

		if (dao == null) {
			dao = new LoanDao();
		}
		setupValueObjects();
		// voA1.setDeclarations(new DeclarationDaoTest("").getVOs(count));
		// voA1.setFees(new FeeDaoTest("").getVOs());
		// voA1.setLoans(new WithdrawalLoanDaoTest("").getVOs(count));
		// voA1.setMoneyTypes(new LoanMoneyTypeDaoTest("").getVOs(count));
		// voA1.setCurrentAdminToParticipantNote(new NoteDaoTest("").getVO());
		// List<WithdrawalRequestPayee> payees = new
		// ArrayList<WithdrawalRequestPayee>();
		// List<WithdrawalRequestRecipient> recipients = new
		// RecipientDaoTest("").getVO(count);
		// WithdrawalRequestPayee payee = new PayeeDaoTest("").getVO();
		// recipients.get(0).setAddress(new
		// DistributionAddressDaoTest("").getRecipientVO());
		// recipients.get(0).setPayees(payees);
		// payees.add(payee);
		// payee.setPaymentInstruction(new
		// PaymentInstructionDaoTest("").getVO());
		// payee.setAddress(new DistributionAddressDaoTest("").getPayeeVO());
		// voA1.setRecipients(recipients);

		// dao.update(TEST_CONTRACT_ID, TEST_SUBMISSION_ID,
		// TEST_USER_PROFILE_ID, voA1);

	}

	public Integer insertSubSubCaseSubLoan() throws Exception {
		if (dao == null) {
			dao = new LoanDao();
		}
		setupValueObjects();
		return dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1,
				DistributionJUnitConstants.TEST_TIMESTAMP_1);
	}

	private boolean exists(Integer submissionId, String sql) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = datasource.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, submissionId);
			rs = stmt.executeQuery();
			return rs.next();
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
	}

}
