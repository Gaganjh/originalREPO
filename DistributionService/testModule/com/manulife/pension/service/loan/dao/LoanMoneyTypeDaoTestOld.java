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

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.dao.DistributionJUnitConstants;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.util.JdbcHelper;

/**
 * LoanMoneyTypeDaoTest is the test case for the LoanMoneyTypeDao object.
 * 
 * @author snowdde
 */
public class LoanMoneyTypeDaoTestOld extends BaseLoanDependentTestCase {

	private String SELECT = "select SUBMISSION_ID,MONEY_TYPE_ID,VESTING_PCT,MONEY_TYPE_ACCT_BALANCE_AMT,MONEY_TYPE_EXCLUDE_IND,CREATED_USER_PROFILE_ID,CREATED_TS,LAST_UPDATED_USER_PROFILE_ID,LAST_UPDATED_TS from stp100.loan_money_type where submission_id = ? AND MONEY_TYPE_ID = ?";
	protected String SELECT_RECORD_COUNT = "select count(*) from stp100.loan_money_type where submission_id = ";
	private LoanMoneyType voA1 = null;
	private LoanMoneyType voA2 = null;
	private LoanMoneyType voA3 = null;
	private LoanMoneyType voB1 = null;
	private LoanMoneyType voB2 = null;
	private LoanMoneyTypeDao dao = null;
	public LoanMoneyType REFERENCE_VO;

	public LoanMoneyTypeDaoTestOld(String arg0) {
		super(arg0);
		setupValueObjects();
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new LoanMoneyTypeDaoTestOld("testInsert"));
		suite.addTest(new LoanMoneyTypeDaoTestOld("testUpdate"));
		suite.addTest(new LoanMoneyTypeDaoTestOld("testInsertUpdate"));
		suite.addTest(new LoanMoneyTypeDaoTestOld("testDeleteAll"));
		suite.addTest(new LoanMoneyTypeDaoTestOld("testRead"));
		return suite;
	}

	public void setUp() throws Exception {

		super.setUp();

		if (dao == null) {
			dao = new LoanMoneyTypeDao();
		}
	}

	private void setupValueObjects() {
		voA1 = new LoanMoneyType();
		voA2 = new LoanMoneyType();
		voA3 = new LoanMoneyType();
		voB1 = new LoanMoneyType();
		voB2 = new LoanMoneyType();

		REFERENCE_VO = voA1;

		voA1.setMoneyTypeId("ERMC2");
		voA1.setVestingPercentage(new BigDecimal("14.560").setScale(3,
				BigDecimal.ROUND_FLOOR));
		voA1.setAccountBalance(new BigDecimal("123456789.10").setScale(2,
				BigDecimal.ROUND_FLOOR));
		voA1.setLoanBalance(BigDecimal.ZERO);
		voA1.setExcludeIndicator(false);
		voA1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voA1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voA1.setCreated(DistributionJUnitConstants.TEST_TIMESTAMP_1);
		voA1.setLastUpdated(DistributionJUnitConstants.TEST_TIMESTAMP_1);

		voA2.setMoneyTypeId(voA1.getMoneyTypeId());
		voA2.setVestingPercentage(new BigDecimal("14.561").setScale(3,
				BigDecimal.ROUND_FLOOR));
		voA2.setAccountBalance(new BigDecimal("123456789.11").setScale(2,
				BigDecimal.ROUND_FLOOR));
		voA2.setLoanBalance(BigDecimal.ZERO);
		voA2.setExcludeIndicator(true);
		voA2.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);
		voA2.setLastUpdated(DistributionJUnitConstants.TEST_TIMESTAMP_2);
		voA2.setCreatedById(voA1.getCreatedById());
		voA2.setCreated(voA1.getCreated());

		voA3.setMoneyTypeId(voA1.getMoneyTypeId());
		voA3.setAccountBalance(voA1.getAccountBalance());
		voA3.setLoanBalance(BigDecimal.ZERO);
		voA3.setVestingPercentage(voA1.getVestingPercentage());
		voA3.setExcludeIndicator(voA1.getExcludeIndicator());
		voA3.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);
		voA3.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);
		voA3.setCreated(DistributionJUnitConstants.TEST_TIMESTAMP_2);
		voA3.setLastUpdated(DistributionJUnitConstants.TEST_TIMESTAMP_2);

		voB1.setMoneyTypeId("ERPS");
		voB1.setVestingPercentage(new BigDecimal("24.560").setScale(3,
				BigDecimal.ROUND_FLOOR));
		voB1.setAccountBalance(new BigDecimal("223456789.10").setScale(2,
				BigDecimal.ROUND_FLOOR));
		voB1.setLoanBalance(BigDecimal.ZERO);
		voB1.setExcludeIndicator(true);
		voB1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voB1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voB1.setCreated(DistributionJUnitConstants.TEST_TIMESTAMP_1);
		voB1.setLastUpdated(DistributionJUnitConstants.TEST_TIMESTAMP_1);

		voB2.setMoneyTypeId(voB1.getMoneyTypeId());
		voB2.setVestingPercentage(new BigDecimal("24.561").setScale(3,
				BigDecimal.ROUND_FLOOR));
		voB2.setAccountBalance(new BigDecimal("223456789.11").setScale(2,
				BigDecimal.ROUND_FLOOR));
		voB2.setLoanBalance(BigDecimal.ZERO);
		voB2.setExcludeIndicator(false);
		voB2.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);
		voB2.setCreatedById(voB1.getCreatedById());
		voB2.setCreated(DistributionJUnitConstants.TEST_TIMESTAMP_1);
		voB2.setLastUpdated(DistributionJUnitConstants.TEST_TIMESTAMP_2);
	}

	/**
	 * Insert a collection of 2 records. then verify all of the fields of record
	 * 1, and verify the record count is 2.
	 * 
	 * {@link com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#insert(com.manulife.pension.service.withdrawal.valueobject.LoanMoneyType)}.
	 */
	public final void testInsert() throws Exception {

		logger.debug("testInsert: begin");

		Connection conn = null;
		Statement stmt = null;

		Collection<LoanMoneyType> moneyTypes1 = new ArrayList<LoanMoneyType>();
		moneyTypes1.add(voA1);
		moneyTypes1.add(voB1);
		Collection<LoanMoneyType> moneyTypes2 = new ArrayList<LoanMoneyType>();
		moneyTypes2.add(voA3);

		try {
			conn = datasource.getConnection();

			dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID,
					DistributionJUnitConstants.TEST_TIMESTAMP_1, moneyTypes1);

			verifyValues(voA1, createVoFromDb(voA1.getMoneyTypeId()));

			verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 2);

			dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		} catch (Exception e) {
			assertTrue("found an exception", false);
			logger.debug(e.toString());
		} finally {

			BaseDatabaseDAO.close(stmt, conn);
		}
		logger.debug("testInsert: end");

	}

	/**
	 * 1. verify that the record we are going to update does not exist in
	 * database. ( don't want to update any unknown data ) 2. update records 3.
	 * compare all values of 1 of the records
	 * 
	 * Test method for
	 * {@link com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#updateMoneyType(com.manulife.pension.service.withdrawal.valueobject.LoanMoneyType)}.
	 */
	public final void testUpdate() throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		Collection<LoanMoneyType> inserts = new ArrayList<LoanMoneyType>();
		Collection<LoanMoneyType> updates1 = new ArrayList<LoanMoneyType>();

		inserts.add(voA1);
		inserts.add(voB1);
		updates1.add(voA2);
		updates1.add(voB2);

		try {

			dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID,
					DistributionJUnitConstants.TEST_TIMESTAMP_1, inserts);

			dao.update(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID_2,
					DistributionJUnitConstants.TEST_TIMESTAMP_2, updates1);

			verifyValues(voA2, createVoFromDb(voA2.getMoneyTypeId()));
			verifyValues(voB2, createVoFromDb(voB2.getMoneyTypeId()));

			dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		}

		finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
		logger.debug("testUpdate: begin");

	}

	/**
	 * 1. make sure that the record we are going to insert does not exist. 2.
	 * insertUpdate a record. verify the values. 3.
	 * 
	 * 
	 * Test method for
	 * {@link com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#insertMoneyType(com.manulife.pension.service.withdrawal.valueobject.LoanMoneyType)}.
	 */
	public final void testInsertUpdate() throws Exception {
		Connection conn = datasource.getConnection();
		PreparedStatement stmt = null;
		Collection<LoanMoneyType> inserts = new ArrayList<LoanMoneyType>();
		Collection<LoanMoneyType> updates = new ArrayList<LoanMoneyType>();

		inserts.add(voB1);
		updates.add(voA3);
		updates.add(voB2);

		try {

			assertFalse("record 1 did not get deleted", exists(voA3));

			dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID,
					DistributionJUnitConstants.TEST_TIMESTAMP_1, inserts);

			dao.insertUpdate(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID_2,
					DistributionJUnitConstants.TEST_TIMESTAMP_2, updates);

			verifyValues(voA3, createVoFromDb(voA3.getMoneyTypeId()));

			verifyValues(voB2, createVoFromDb(voB2.getMoneyTypeId()));

			// cleanup
			dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		}

		finally {
			BaseDatabaseDAO.close(stmt, conn);
		}

	}

	/**
	 * 1. insert 2 test records 2. delete the test records 3. verify records are
	 * deleted
	 */
	public final void testDeleteAll() throws Exception {

		Connection conn = datasource.getConnection();
		PreparedStatement stmt = null;
		Collection<LoanMoneyType> vos = new ArrayList<LoanMoneyType>();
		vos.add(voA1);
		vos.add(voB1);

		try {
			dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID,
					DistributionJUnitConstants.TEST_TIMESTAMP_1, vos);

			dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID);

			verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 0);
		}

		finally {
			BaseDatabaseDAO.close(stmt, conn);
		}

	}

	public void testRead() throws Exception {
		Collection<LoanMoneyType> inserts = new ArrayList<LoanMoneyType>();
		Collection<LoanMoneyType> reads = new ArrayList<LoanMoneyType>();
		inserts.add(voA1);
		inserts.add(voB1);
		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				DistributionJUnitConstants.TEST_TIMESTAMP_1, inserts);

		reads = dao.read(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		assertEquals(reads.size(), 2);
		for (LoanMoneyType vo : reads) {
			if (StringUtils.equals(vo.getMoneyTypeId(), voA1.getMoneyTypeId())) {
				verifyValues(voA1, vo);
			} else if (StringUtils.equals(vo.getMoneyTypeId(), voB1
					.getMoneyTypeId())) {
				verifyValues(voB1, vo);
			} else {
				fail("invalid money type");
			}
		}

		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID);
	}

	private LoanMoneyType createVoFromDb(String moneyTypeId) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		LoanMoneyType returnVo = new LoanMoneyType();
		try {
			conn = datasource.getConnection();
			stmt = conn.prepareStatement(SELECT);
			stmt.setInt(1, getSubmissionId());
			stmt.setString(2, moneyTypeId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				returnVo.setMoneyTypeId(rs.getString("MONEY_TYPE_ID").trim());
				returnVo.setAccountBalance(rs
						.getBigDecimal("MONEY_TYPE_ACCT_BALANCE_AMT"));
				returnVo.setVestingPercentage(rs.getBigDecimal("VESTING_PCT"));
				returnVo.setExcludeIndicator(JdbcHelper.getBoolean(rs,
						"MONEY_TYPE_EXCLUDE_IND", false));
				returnVo.setCreatedById(rs.getInt("CREATED_USER_PROFILE_ID"));
				returnVo.setCreated(rs.getTimestamp("CREATED_TS"));
				returnVo.setLastUpdatedById(rs
						.getInt("LAST_UPDATED_USER_PROFILE_ID"));
				returnVo.setLastUpdated(rs.getTimestamp("LAST_UPDATED_TS"));
			}
			return returnVo;
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
	}

	public void verifyValues(LoanMoneyType vo1, LoanMoneyType vo2) {
		assertEquals(vo1.getMoneyTypeId().trim(), vo2.getMoneyTypeId().trim());
		assertEquals(vo1.getAccountBalance(), vo2.getAccountBalance());
		assertEquals(vo1.getVestingPercentage(), vo2.getVestingPercentage());
		assertEquals(vo1.getExcludeIndicator(), vo2.getExcludeIndicator());
		assertEquals(vo1.getCreatedById(), vo2.getCreatedById());
		assertEquals(vo1.getCreated(), vo2.getCreated());
		assertEquals(vo1.getLastUpdatedById(), vo2.getLastUpdatedById());
		assertEquals(vo1.getLastUpdated(), vo2.getLastUpdated());
	}

	private boolean exists(LoanMoneyType vo) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = datasource.getConnection();
			stmt = conn.prepareStatement(SELECT);
			stmt.setInt(1, getSubmissionId());
			stmt.setString(2, vo.getMoneyTypeId());
			rs = stmt.executeQuery();
			return rs.next();
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
	}

	public Collection<LoanMoneyType> getVOs(int count) {

		Collection<LoanMoneyType> vos = new ArrayList<LoanMoneyType>();
		vos.add(voA1);
		if (count > 1) {
			vos.add(voB1);
		}
		return vos;
	}
}
