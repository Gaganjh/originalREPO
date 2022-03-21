package com.manulife.pension.service.loan.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.dao.DistributionJUnitConstants;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.loan.valueobject.LoanParameter;

/**
 * LoanParameterDaoTest is the test case for the LoanParameterDao object.
 * 
 * @author snowdde
 */
public class LoanParameterDaoTestOld extends BaseLoanDependentTestCase {

	private String SELECT = "select SUBMISSION_ID, SUB_CASE_PROCESS_STATUS_CODE, MAX_LOAN_AVAILABLE_AMT, LOAN_AMOUNT, PAYMENT_AMOUNT, PAYMENT_FREQUENCY_CODE, AMORTIZATION_MONTHS, INTEREST_RATE_PCT, CREATED_USER_PROFILE_ID, CREATED_TS, LAST_UPDATED_USER_PROFILE_ID, LAST_UPDATED_TS from stp100.loan_stage_parameter where submission_id = ? AND SUB_CASE_PROCESS_STATUS_CODE = ?";	
	protected String SELECT_RECORD_COUNT = "select count(*) from stp100.loan_stage_parameter where submission_id = ";
	private LoanParameter voA1 = null;
	private LoanParameter voA2 = null;
	private LoanParameter voA3 = null;
	private LoanParameter voB1 = null;
	private LoanParameter voB2 = null;
	private LoanParameter voB3 = null;
	private LoanParameterDao dao = null;
	public LoanParameter REFERENCE_VO;

	public LoanParameterDaoTestOld(String arg0) {
		super(arg0);
		setupValueObjects();
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new LoanParameterDaoTestOld("testInsert"));
		suite.addTest(new LoanParameterDaoTestOld("testUpdate"));
		suite.addTest(new LoanParameterDaoTestOld("testInsertUpdate"));
		suite.addTest(new LoanParameterDaoTestOld("testDeleteAll"));
		suite.addTest(new LoanParameterDaoTestOld("testRead"));
		suite.addTest(new LoanParameterDaoTestOld("testcheckLoanStatusExists"));
		
		return suite;
	}

	public void setUp() throws Exception {

		super.setUp();

		if (dao == null) {
			dao = new LoanParameterDao();
		}
	}

	private void setupValueObjects() {
		voA1 = new LoanParameter();
		voA2 = new LoanParameter();
		voA3 = new LoanParameter();
		voB1 = new LoanParameter();
		voB2 = new LoanParameter();
		voB3 = new LoanParameter();

		REFERENCE_VO = voA1;

		voA1.setStatusCode(LoanStateEnum.DRAFT.getStatusCode());
		voA1.setMaximumAvailable(new BigDecimal(555.22).setScale(2, BigDecimal.ROUND_FLOOR));
		voA1.setLoanAmount(new BigDecimal(666.22).setScale(2, BigDecimal.ROUND_FLOOR));
		voA1.setPaymentAmount(new BigDecimal(777.22).setScale(2, BigDecimal.ROUND_FLOOR));
		voA1.setPaymentFrequency(GlobalConstants.FREQUENCY_TYPE_MONTHLY);
		voA1.setAmortizationMonths(1);
		voA1.setInterestRate(new BigDecimal(888.234).setScale(3, BigDecimal.ROUND_FLOOR));
		voA1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voA1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voA1.setCreated(DistributionJUnitConstants.TEST_TIMESTAMP_1);
		voA1.setLastUpdated(DistributionJUnitConstants.TEST_TIMESTAMP_1);

		voA2.setStatusCode(LoanStateEnum.DRAFT.getStatusCode());
		voA2.setMaximumAvailable(new BigDecimal(555.23).setScale(2, BigDecimal.ROUND_FLOOR));
		voA2.setLoanAmount(new BigDecimal(666.23).setScale(2, BigDecimal.ROUND_FLOOR));
		voA2.setPaymentAmount(new BigDecimal(777.23).setScale(2, BigDecimal.ROUND_FLOOR));
		voA2.setPaymentFrequency(GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY);
		voA2.setAmortizationMonths(2);
		voA2.setInterestRate(new BigDecimal(888.235).setScale(3, BigDecimal.ROUND_FLOOR));
		voA2.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);
		voA2.setLastUpdated(DistributionJUnitConstants.TEST_TIMESTAMP_2);
		voA2.setCreatedById(voA1.getCreatedById());
		voA2.setCreated(voA1.getCreated());

		voA3.setStatusCode(LoanStateEnum.DRAFT.getStatusCode());
		voA3.setMaximumAvailable(new BigDecimal(555.23).setScale(2, BigDecimal.ROUND_FLOOR));
		voA3.setLoanAmount(new BigDecimal(666.23).setScale(2, BigDecimal.ROUND_FLOOR));
		voA3.setPaymentAmount(new BigDecimal(777.23).setScale(2, BigDecimal.ROUND_FLOOR));
		voA3.setPaymentFrequency(GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY);
		voA3.setAmortizationMonths(2);
		voA3.setInterestRate(new BigDecimal(888.235).setScale(3, BigDecimal.ROUND_FLOOR));
		voA3.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);
		voA3.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);
		voA3.setCreated(DistributionJUnitConstants.TEST_TIMESTAMP_2);
		voA3.setLastUpdated(DistributionJUnitConstants.TEST_TIMESTAMP_2);


		voB1.setStatusCode(LoanStateEnum.PENDING_REVIEW.getStatusCode());
		voB1.setMaximumAvailable(new BigDecimal(556.22).setScale(2, BigDecimal.ROUND_FLOOR));
		voB1.setLoanAmount(new BigDecimal(667.22).setScale(2, BigDecimal.ROUND_FLOOR));
		voB1.setPaymentAmount(new BigDecimal(778.22).setScale(2, BigDecimal.ROUND_FLOOR));
		voB1.setPaymentFrequency(GlobalConstants.FREQUENCY_TYPE_WEEKLY);
		voB1.setAmortizationMonths(25);
		voB1.setInterestRate(new BigDecimal(889.234).setScale(3, BigDecimal.ROUND_FLOOR));
		voB1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voB1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voB1.setCreated(DistributionJUnitConstants.TEST_TIMESTAMP_1);
		voB1.setLastUpdated(DistributionJUnitConstants.TEST_TIMESTAMP_1);


		voB2.setStatusCode(LoanStateEnum.PENDING_REVIEW.getStatusCode());
		voB2.setMaximumAvailable(new BigDecimal(556.23).setScale(2, BigDecimal.ROUND_FLOOR));
		voB2.setLoanAmount(new BigDecimal(667.23).setScale(2, BigDecimal.ROUND_FLOOR));
		voB2.setPaymentAmount(new BigDecimal(778.23).setScale(2, BigDecimal.ROUND_FLOOR));
		voB2.setPaymentFrequency(GlobalConstants.FREQUENCY_TYPE_SEMI_MONTHLY);
		voB2.setAmortizationMonths(26);
		voB2.setInterestRate(new BigDecimal(889.235).setScale(3, BigDecimal.ROUND_FLOOR));
		voB2.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);
		voB2.setCreatedById(voB1.getCreatedById());
		voB2.setCreated(DistributionJUnitConstants.TEST_TIMESTAMP_1);
		voB2.setLastUpdated(DistributionJUnitConstants.TEST_TIMESTAMP_2);
		
		voB3.setStatusCode(LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode());
		voB3.setMaximumAvailable(new BigDecimal(555.22).setScale(2, BigDecimal.ROUND_FLOOR));
		voB3.setLoanAmount(new BigDecimal(666.22).setScale(2, BigDecimal.ROUND_FLOOR));
		voB3.setPaymentAmount(new BigDecimal(777.22).setScale(2, BigDecimal.ROUND_FLOOR));
		voB3.setPaymentFrequency(GlobalConstants.FREQUENCY_TYPE_MONTHLY);
		voB3.setAmortizationMonths(1);
		voB3.setInterestRate(new BigDecimal(888.234).setScale(3, BigDecimal.ROUND_FLOOR));
		voB3.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voB3.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voB3.setCreated(DistributionJUnitConstants.TEST_TIMESTAMP_1);
		voB3.setLastUpdated(DistributionJUnitConstants.TEST_TIMESTAMP_1);
	}

	/**
	 * Insert a collection of 2 records. then verify all of the fields of record
	 * 1, and verify the record count is 2.
	 * 
	 * {@link com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#insert(com.manulife.pension.service.withdrawal.valueobject.LoanParameter)}.
	 */
	public final void testInsert() throws Exception {

		Connection conn = null;
		Statement stmt = null;

		Collection<LoanParameter> inserts1 = new ArrayList<LoanParameter>();
		inserts1.add(voA1);
		inserts1.add(voB1);
		Collection<LoanParameter> inserts2 = new ArrayList<LoanParameter>();
		inserts2.add(voA1);

		try {
			conn = datasource.getConnection();

			dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID, DistributionJUnitConstants.TEST_TIMESTAMP_1, inserts1);

			verifyValues(voA1, createVoFromDb(voA1.getStatusCode()));

			verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 2);

			dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID);

			// test null value
			BigDecimal oldVal = voA3.getMaximumAvailable();
			voA3.setMaximumAvailable(null);
			dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID, DistributionJUnitConstants.TEST_TIMESTAMP_1, inserts2);
			voA3.setMaximumAvailable(oldVal);

			dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		} catch (Exception e) {
			fail("found an exception");
			logger.debug(e.toString());
		} finally {

			BaseDatabaseDAO.close(stmt, conn);
		}

	}

	/**
	 * 1. verify that the record we are going to update does not exist in
	 * database. ( don't want to update any unknown data ) 2. update records 3.
	 * compare all values of 1 of the records
	 * 
	 * Test method for
	 * {@link com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#updateMoneyType(com.manulife.pension.service.withdrawal.valueobject.LoanParameter)}.
	 */
	public final void testUpdate() throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		Collection<LoanParameter> inserts = new ArrayList<LoanParameter>();
		Collection<LoanParameter> updates1 = new ArrayList<LoanParameter>();

		inserts.add(voA1);
		inserts.add(voB1);
		updates1.add(voA2);
		updates1.add(voB2);

		try {

			dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID, DistributionJUnitConstants.TEST_TIMESTAMP_1, inserts);

			dao.update(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID_2, DistributionJUnitConstants.TEST_TIMESTAMP_2, updates1);

			verifyValues(voA2, createVoFromDb(voA2.getStatusCode()));
			verifyValues(voB2, createVoFromDb(voB2.getStatusCode()));

			dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		}

		finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
		logger.debug("testUpdate: begin");

	}


	/**
	 * 1. insert 2 test records 2. delete the test records 3. verify records are
	 * deleted
	 */
	public final void testDeleteAll() throws Exception {

		Connection conn = datasource.getConnection();
		PreparedStatement stmt = null;
		Collection<LoanParameter> vos = new ArrayList<LoanParameter>();
		vos.add(voA1);
		vos.add(voB1);

		try {
			dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID, DistributionJUnitConstants.TEST_TIMESTAMP_1, vos);

			dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID);

			verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 0);
		}

		finally {
			BaseDatabaseDAO.close(stmt, conn);
		}

	}
	/**
	 * 1. make sure that the record we are going to insert does not exist.
	 * 2. insertUpdate a record. verify the values.
	 * 3. 
	 *  
	 * 
	 * Test method for
	 * {@link com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#insertMoneyType(com.manulife.pension.service.withdrawal.valueobject.LoanMoneyType)}.
	 */
	public final void testInsertUpdate() throws Exception {
		Connection conn = datasource.getConnection();
		PreparedStatement stmt = null;
		Collection<LoanParameter> inserts = new ArrayList<LoanParameter>();
		Collection<LoanParameter> updates = new ArrayList<LoanParameter>();

		voB1.setReadyToSave(true);
		voA3.setReadyToSave(true);
		voB2.setReadyToSave(true);
		inserts.add(voB1);
		updates.add(voA3);
		updates.add(voB2);

		try {

			assertFalse("record 1 did not get deleted", exists(voA3));

			dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID, DistributionJUnitConstants.TEST_TIMESTAMP_1, inserts);

			dao.insertUpdate(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID_2, DistributionJUnitConstants.TEST_TIMESTAMP_2, updates);

			verifyValues(voA3, createVoFromDb(voA3.getStatusCode()));

			verifyValues(voB2, createVoFromDb(voB2.getStatusCode()));

			// cleanup
			dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		}

		finally {
			BaseDatabaseDAO.close(stmt, conn);
		}

	}
	
    public void testRead() throws Exception {
		Collection<LoanParameter> inserts = new ArrayList<LoanParameter>();
		Collection<LoanParameter> reads = new ArrayList<LoanParameter>();
		inserts.add(voA1);
		inserts.add(voB1);
		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID, DistributionJUnitConstants.TEST_TIMESTAMP_1, inserts);

		reads = dao.read(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		assertEquals(reads.size(), 2);
		for (LoanParameter vo : reads) {
			if (StringUtils.equals(vo.getStatusCode(), voA1.getStatusCode())) {
				verifyValues(voA1, vo);
			} else if (StringUtils.equals(vo.getStatusCode(), voB1.getStatusCode())) {
				verifyValues(voB1, vo);
			} else {
				fail("invalid status code");
			}
		}

		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID);
	}	

	private LoanParameter createVoFromDb(String statusCode) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		LoanParameter returnVo = new LoanParameter();
		try {
			conn = datasource.getConnection();
			stmt = conn.prepareStatement(SELECT);
			stmt.setInt(1, getSubmissionId());
			stmt.setString(2, statusCode);
			rs = stmt.executeQuery();
			if (rs.next()) {
				returnVo.setStatusCode(StringUtils.trim(rs.getString("SUB_CASE_PROCESS_STATUS_CODE")));
				returnVo.setMaximumAvailable(rs.getBigDecimal("MAX_LOAN_AVAILABLE_AMT"));
				returnVo.setLoanAmount(rs.getBigDecimal("LOAN_AMOUNT"));
				returnVo.setPaymentAmount(rs.getBigDecimal("PAYMENT_AMOUNT"));
				returnVo.setPaymentFrequency(StringUtils.trim(rs.getString("PAYMENT_FREQUENCY_CODE")));
				returnVo.setAmortizationMonths(rs.getInt("AMORTIZATION_MONTHS"));
				returnVo.setInterestRate(rs.getBigDecimal("INTEREST_RATE_PCT"));
                if (returnVo.getInterestRate() != null) {
                    returnVo.setInterestRate(returnVo.getInterestRate().setScale(
                            LoanConstants.LOAN_INTEREST_RATE_SCALE,
                            LoanConstants.LOAN_INTEREST_RATE_ROUND_RULE));
                }
				returnVo.setCreatedById(rs.getInt("CREATED_USER_PROFILE_ID"));
				returnVo.setCreated(rs.getTimestamp("CREATED_TS"));
				returnVo.setLastUpdatedById(rs.getInt("LAST_UPDATED_USER_PROFILE_ID"));
				returnVo.setLastUpdated(rs.getTimestamp("LAST_UPDATED_TS"));
			}
			return returnVo;
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
	}

	public void verifyValues(LoanParameter vo1, LoanParameter vo2) {
		assertEquals(vo1.getStatusCode(), vo2.getStatusCode());
		assertEquals(vo1.getMaximumAvailable(),vo2.getMaximumAvailable());
		assertEquals(vo1.getLoanAmount(),vo2.getLoanAmount());
		assertEquals(vo1.getPaymentAmount(),vo2.getPaymentAmount());
		assertEquals(vo1.getPaymentFrequency(),vo2.getPaymentFrequency());
		assertEquals(vo1.getAmortizationMonths(),vo2.getAmortizationMonths());
		assertEquals(vo1.getInterestRate(),vo2.getInterestRate());
		assertEquals(vo1.getCreatedById(), vo2.getCreatedById());
		assertEquals(vo1.getCreated(), vo2.getCreated());
		assertEquals(vo1.getLastUpdatedById(), vo2.getLastUpdatedById());
		assertEquals(vo1.getLastUpdated(), vo2.getLastUpdated());
	}

	public Collection<LoanParameter> getVOs(int count) {

		Collection<LoanParameter> vos = new ArrayList<LoanParameter>();
		vos.add(voA1);
		if (count > 1) {
			vos.add(voB1);
		}
		return vos;
	}
	private boolean exists(LoanParameter vo) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = datasource.getConnection();
			stmt = conn.prepareStatement(SELECT);
			stmt.setInt(1, getSubmissionId());
			stmt.setString(2, vo.getStatusCode());
			rs = stmt.executeQuery();
			return rs.next();
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
	}
	
	/**
	 * Returns true if there exists a Loan request status history of Sent For
	 * acceptance
	 * 
	 */
	public final void testcheckLoanStatusExists() throws Exception {

		Connection conn = null;
		Statement stmt = null;

		Collection<LoanParameter> inserts1 = new ArrayList<LoanParameter>();
		inserts1.add(voB3);

		try {
			conn = datasource.getConnection();

			dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID, DistributionJUnitConstants.TEST_TIMESTAMP_1, inserts1);
			boolean checkLoanStatusExists = dao.checkLoanStatusExists(
					getSubmissionId(), LoanStateEnum.PENDING_ACCEPTANCE
							.getStatusCode());
			assertEquals(true, checkLoanStatusExists);

			dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		} catch (Exception e) {
			fail("found an exception");
			logger.debug(e.toString());
		} finally {

			BaseDatabaseDAO.close(stmt, conn);
		}
	}
}
