/*																				
 * FeeDaoTest.java,v 1.1 2006/09/07 22:42:16 Paul_Glenn Exp																				
 * FeeDaoTest.java,v																				
 * Revision 1.1  2006/09/07 22:42:16  Paul_Glenn																				
 * Initial.																				
 *																				
 */
package com.manulife.pension.service.distribution.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.loan.valueobject.LoanFee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee;

/**
 * FeeDaoTest is the test class for the {@link WithdrawalFeeDao} class.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/07 22:42:16
 */
public class FeeDaoTestOld extends BaseSubmissionDependentTestCase {

	private String SELECT = "select  SUBMISSION_ID, FEE_TYPE_CODE, FEE_VALUE, CREATED_USER_PROFILE_ID, CREATED_TS, LAST_UPDATED_USER_PROFILE_ID, LAST_UPDATED_TS from stp100.fee where submission_id = ";
	protected String SELECT_RECORD_COUNT = "select count(*) from stp100.fee where submission_id = ";

	// voA1 & voB1 are for inserts
	// voA2 & voB2 are for updating voA1 and voB1 respectively
	// voC1 is for deleting
	// voA3 is for insertupdating. it was the a different created by id.
	private Fee voA1 = null;
	private Fee voA2 = null;
	private Fee voA3 = null;
	private Fee voB1 = null;
	private Fee voB2 = null;
	private Fee voC1 = null;
	private Fee voZ1 = null;

	private FeeDao dao = null;
	public Fee REFERENCE_VO;
	private Class type; // support for different implementations of interface

	// FEE

	public FeeDaoTestOld(String arg0, Class type) throws IllegalAccessException, InstantiationException {
		super(arg0);
		this.type = type;
		setupValueObjects();

	}

	public static Test suite() throws IllegalAccessException, InstantiationException {
		TestSuite suite = new TestSuite();
		suite.addTest(new FeeDaoTestOld("testInsert", LoanFee.class));
		suite.addTest(new FeeDaoTestOld("testUpdate", LoanFee.class));
		suite.addTest(new FeeDaoTestOld("testInsertUpdatePrune", LoanFee.class));
		suite.addTest(new FeeDaoTestOld("testDelete", LoanFee.class));
		suite.addTest(new FeeDaoTestOld("testDeleteAll", LoanFee.class));
		suite.addTest(new FeeDaoTestOld("testSelect", LoanFee.class));

		suite.addTest(new FeeDaoTestOld("testInsert", WithdrawalRequestFee.class));
		suite.addTest(new FeeDaoTestOld("testUpdate", WithdrawalRequestFee.class));
		suite.addTest(new FeeDaoTestOld("testInsertUpdatePrune", WithdrawalRequestFee.class));
		suite.addTest(new FeeDaoTestOld("testDelete", WithdrawalRequestFee.class));
		suite.addTest(new FeeDaoTestOld("testDeleteAll", WithdrawalRequestFee.class));
		suite.addTest(new FeeDaoTestOld("testSelect", WithdrawalRequestFee.class));
		return suite;
	}

	public void setUp() throws Exception {

		super.setUp();

		if (dao == null) {
			dao = new FeeDao();
		}
	}

	private void setupValueObjects() throws IllegalAccessException, InstantiationException {
		voA1 = (Fee) type.newInstance();
		voA2 = (Fee) type.newInstance();
		voA3 = (Fee) type.newInstance();
		voB1 = (Fee) type.newInstance();
		voB2 = (Fee) type.newInstance();
		voC1 = (Fee) type.newInstance();
		voZ1 = (Fee) type.newInstance();

		REFERENCE_VO = voA1;

		voA1.setSubmissionId(getSubmissionId());
		voA1.setTypeCode("A");
		voA1.setValue(new BigDecimal(1.11).setScale(2, BigDecimal.ROUND_FLOOR));
		voA1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voA1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		voA2.setSubmissionId(getSubmissionId());
		voA2.setTypeCode(voA1.getTypeCode());
		voA2.setValue(new BigDecimal(1.12).setScale(2, BigDecimal.ROUND_FLOOR));
		voA2.setCreatedById(voA1.getCreatedById());
		voA2.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);

		voA3.setSubmissionId(getSubmissionId());
		voA3.setTypeCode(voA1.getTypeCode());
		voA3.setValue(voA1.getValue());
		voA3.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);
		voA3.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);

		voB1.setSubmissionId(getSubmissionId());
		voB1.setTypeCode("B");
		voB1.setValue(new BigDecimal(2.11).setScale(2, BigDecimal.ROUND_FLOOR));
		voB1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voB1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		voB2.setSubmissionId(getSubmissionId());
		voB2.setTypeCode(voB1.getTypeCode());
		voB2.setValue(new BigDecimal(2.12).setScale(2, BigDecimal.ROUND_FLOOR));
		voB2.setCreatedById(voB1.getCreatedById());
		voB2.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);

		voC1.setSubmissionId(getSubmissionId());
		voC1.setTypeCode("C");
		voC1.setValue(new BigDecimal(3.33).setScale(2, BigDecimal.ROUND_FLOOR));
		voC1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voC1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		voZ1.setSubmissionId(getSubmissionId());
		voZ1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voZ1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);

	}

	/**
	 * 1. insert 1 test records - voA1 2. verify values of record 1 ( vo1 ) 3.
	 * verify record count
	 */
	public final void testInsert() throws Exception {

		Collection<Fee> inserts1 = new ArrayList<Fee>();
		Collection<Fee> inserts2 = new ArrayList<Fee>();
		inserts1.add(voA1);
		inserts2.add(voZ1);

		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, inserts1);
		verifyValues(voA1, createVoFromDb());
		verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 1);
		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, inserts2);

	}

	/**
	 * 1. insert 1 test records - voA1 2. select the record 3. verify the values
	 */
	public final void testSelect() throws Exception {

		Collection<Fee> inserts = new ArrayList<Fee>();
		inserts.add(voA1);
		List<? extends Fee> selects = null;

		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, inserts);
		selects = dao.select(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, WithdrawalRequestFee.class);
		assertTrue("sizes don't match", selects.size() == 1);
		verifyValues(selects.get(0), voA1);
		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);

	}

	/**
	 * 1. insert 1 test records 2. update 1 test records 3. compare all values
	 * of both records
	 * 
	 */
	public final void testUpdate() throws Exception {

		Collection<Fee> inserts = new ArrayList<Fee>();
		Collection<Fee> updates1 = new ArrayList<Fee>();
		Collection<Fee> updates2 = new ArrayList<Fee>();

		inserts.add(voA1);
		updates1.add(voA2);
		updates2.add(voZ1);

		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, inserts);
		dao.update(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID_2, updates1);
		verifyValues(voA2, createVoFromDb());
		dao.update(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID_2, updates2);

		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);

	}

	/**
	 * 1st record will be inserted, 2nd record updated, 3rd record pruned.
	 * 
	 * 1. verify that 1st record does not exist in database ( vo1 ) 2. insert 2
	 * records into the database ( vo2, vo5 ) 3. insertUpdatePrune 2 test
	 * records ( vo1 , vo4 ) 4. verify all values of record 1 ( vo1 ) 5. verify
	 * all values of record 2 ( vo4 ) 6. verify that 3rd record was deleted (
	 * vo5 ) ( if 4 and 5 are succesfull, then doing a record count == 2 will be
	 * sufficient )
	 * 
	 */
	public final void testInsertUpdatePrune() throws Exception {

		Collection<Fee> inserts = new ArrayList<Fee>();
		Collection<Fee> updates = new ArrayList<Fee>();
		Collection<Fee> prunes = new ArrayList<Fee>();

		inserts.add(voB1);
		updates.add(voB2);

		assertFalse("record 1 did not get deleted", exists(voB1));
		dao.insertUpdatePrune(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				inserts, WithdrawalRequestFee.class);
		verifyValues(voB1, createVoFromDb());
		dao.insertUpdatePrune(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID_2,
				updates, WithdrawalRequestFee.class);
		verifyValues(voB2, createVoFromDb());
		dao.insertUpdatePrune(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID_2,
				prunes, WithdrawalRequestFee.class);
		verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 0);

		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);

	}

	/**
	 * 1. insert test record 2. delete the test record 3. verify it was deleted
	 */
	public final void testDelete() throws Exception {

		Collection<Fee> deletes = new ArrayList<Fee>();
		deletes.add(voA1);

		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, deletes);
		dao.delete(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, deletes);

		assertFalse("record 1 did not get deleted", exists(voA1));

	}

	/**
	 * 1. insert 2 test records 2. delete the test records 3. verify records are
	 * deleted
	 */
	public final void testDeleteAll() throws Exception {

		Collection<Fee> vos = new ArrayList<Fee>();
		vos.add(voA1);

		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, vos);
		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 0);

	}

	private Fee createVoFromDb() throws Exception {
		List<? extends Fee> fees = dao.select(getSubmissionId(), null, null, WithdrawalRequestFee.class);
		if (fees.size() > 0) {
			return fees.get(0);
		}
		return null;
	}

	public void verifyValues(Fee vo1, Fee vo2) {
		assertEquals(vo1.getTypeCode(), vo2.getTypeCode());
		assertEquals(vo1.getValue(), vo2.getValue());
		assertEquals(vo1.getCreatedById(), vo2.getCreatedById());
		assertEquals(vo1.getLastUpdatedById(), vo2.getLastUpdatedById());
	}

	private boolean exists(Fee vo) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = datasource.getConnection();
			stmt = conn.prepareStatement(SELECT + getSubmissionId());
			rs = stmt.executeQuery();
			return rs.next();
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
	}

	public Collection<Fee> getVOs() {
		Collection<Fee> vos = new ArrayList<Fee>();
		vos.add(voA1);
		return vos;
	}
}
