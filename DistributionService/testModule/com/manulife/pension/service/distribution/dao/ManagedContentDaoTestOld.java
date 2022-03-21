package com.manulife.pension.service.distribution.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;

/**
 * public class ManagedContentDaoTest extends BaseSubmissionDependentTestCase { is
 * the test case for the ManagedContentDao object.
 * 
 * @author snowdde
 */
public class ManagedContentDaoTestOld extends BaseSubmissionDependentTestCase {

	private String SELECT = "select CONTENT_KEY,CONTENT_ID,CMA_SITE_CODE,CREATED_USER_PROFILE_ID,CREATED_TS from stp100.managed_content_reference where submission_id = ? AND CONTENT_KEY = ? and CONTENT_ID = ? and CMA_SITE_CODE = ?";
	protected String SELECT_RECORD_COUNT = "select count(*) from stp100.managed_content_reference where submission_id = ";
	private ManagedContent voA1 = null;
	private ManagedContent voB1 = null;
	private ManagedContentDao dao = null;
	public ManagedContent REFERENCE_VO;

	public ManagedContentDaoTestOld(String arg0) {
		super(arg0);
		setupValueObjects();
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new ManagedContentDaoTestOld("testInsert"));
		suite.addTest(new ManagedContentDaoTestOld("testDeleteAll"));
		suite.addTest(new ManagedContentDaoTestOld("testRead"));
		return suite;
	}

	public void setUp() throws Exception {

		super.setUp();

		if (dao == null) {
			dao = new ManagedContentDao();
		}
	}

	private void setupValueObjects() {
		voA1 = new ManagedContent();
		voB1 = new ManagedContent();

		REFERENCE_VO = voA1;

		voA1.setContentKey(1234);
		voA1.setContentId(334);
		voA1.setContentTypeCode("AAA");
		voA1.setCmaSiteCode("PS");
		voA1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voA1.setCreated(DistributionJUnitConstants.TEST_TIMESTAMP_1);

		voB1.setContentKey(1235);
		voB1.setContentId(335);
        voB1.setContentTypeCode("BBB");
		voB1.setCmaSiteCode("PA");
		voB1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voB1.setCreated(DistributionJUnitConstants.TEST_TIMESTAMP_1);

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

		Collection<ManagedContent> inserts = new ArrayList<ManagedContent>();
		inserts.add(voA1);
		inserts.add(voB1);

		try {
			conn = datasource.getConnection();

			dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID,
					DistributionJUnitConstants.TEST_TIMESTAMP_1, inserts);

			verifyValues(voA1, createVoFromDb(voA1.getContentKey(), voA1
					.getContentId(), voA1.getCmaSiteCode()));

			verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 2);

			dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
					DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
		logger.debug("testInsert: end");

	}

	/**
	 * 1. insert 2 test records 2. delete the test records 3. verify records are
	 * deleted
	 */
	public final void testDeleteAll() throws Exception {

		Connection conn = datasource.getConnection();
		PreparedStatement stmt = null;
		Collection<ManagedContent> vos = new ArrayList<ManagedContent>();
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
		Collection<ManagedContent> inserts = new ArrayList<ManagedContent>();
		Collection<ManagedContent> reads = new ArrayList<ManagedContent>();
		inserts.add(voA1);
		inserts.add(voB1);
		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				DistributionJUnitConstants.TEST_TIMESTAMP_1, inserts);

		reads = dao.read(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		assertEquals(reads.size(), 2);
		for (ManagedContent vo : reads) {
			if (ObjectUtils.equals(vo.getContentId(), voA1.getContentId())
					&& ObjectUtils.equals(vo.getContentKey(), voA1
							.getContentKey())
					&& StringUtils.equals(vo.getCmaSiteCode(), voA1
							.getCmaSiteCode())) {
				verifyValues(voA1, vo);
			} else if (ObjectUtils.equals(vo.getContentId(), voB1
					.getContentId())
					&& ObjectUtils.equals(vo.getContentKey(), voB1
							.getContentKey())
					&& StringUtils.equals(vo.getCmaSiteCode(), voB1
							.getCmaSiteCode())) {
				verifyValues(voB1, vo);
			} else {
				fail("invalid money type");
			}
		}

		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
				DistributionJUnitConstants.TEST_USER_PROFILE_ID);
	}

	private ManagedContent createVoFromDb(Integer contentKey,
			Integer contentId, String cmaSiteCode) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ManagedContent returnVo = new ManagedContent();
		try {
			conn = datasource.getConnection();
			stmt = conn.prepareStatement(SELECT);
			stmt.setInt(1, getSubmissionId());
			stmt.setInt(2, contentKey);
			stmt.setInt(3, contentId);
			stmt.setString(4, cmaSiteCode);
			rs = stmt.executeQuery();
			if (rs.next()) {
				returnVo.setCmaSiteCode(rs.getString("CMA_SITE_CODE"));
				returnVo.setContentId(rs.getInt("CONTENT_ID"));
				returnVo.setContentKey(rs.getInt("CONTENT_KEY"));
				returnVo.setCreated(rs.getTimestamp("CREATED_TS"));
				returnVo.setCreatedById(rs.getInt("CREATED_USER_PROFILE_ID"));
			}
			return returnVo;
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
	}

	public void verifyValues(ManagedContent vo1, ManagedContent vo2) {
		assertEquals(vo1.getContentKey(), vo2.getContentKey());
		assertEquals(vo1.getContentId(), vo2.getContentId());
		assertEquals(vo1.getCmaSiteCode(), vo2.getCmaSiteCode());
		assertEquals(vo1.getCreated(), vo2.getCreated());
		assertEquals(vo1.getCreatedById(), vo2.getCreatedById());
	}

	public Collection<ManagedContent> getVOs(int count) {

		Collection<ManagedContent> vos = new ArrayList<ManagedContent>();
		vos.add(voA1);
		if (count > 1) {
			vos.add(voB1);
		}
		return vos;
	}
}
