/*																				
 * FeeDaoTest.java,v 1.1 2006/09/07 22:42:16 Paul_Glenn Exp																				
 * FeeDaoTest.java,v																				
 * Revision 1.1  2006/09/07 22:42:16  Paul_Glenn																				
 * Initial.																				
 *																				
 */
package com.manulife.pension.service.distribution.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.loan.valueobject.LoanAddress;
import com.manulife.pension.service.withdrawal.valueobject.Address;

/**
 * FeeDaoTest is the test class for the {@link FeeDao} class.
 * 
 * @author Dennis_Snowdon
 */
public class DistributionAddressDaoTestOld extends BaseSubmissionDependentTestCase {
	private DistributionAddress voA1 = null;
	private DistributionAddress voB1 = null;
	private DistributionAddress voC1 = null;
	private DistributionAddress voD1 = null;
	public DistributionAddress REFERENCE_RECIPIENT_VO;
	public DistributionAddress REFERENCE_PAYEE_VO;

	protected String SELECT_RECORD_COUNT_PAYEE = "select count(*) from stp100.DISTRIBUTION_ADDRESS where DISTRIBUTION_TYPE_CODE = 'PA' and submission_id = ";
	protected String SELECT_RECORD_COUNT_RECIPIENT = "select count(*) from stp100.DISTRIBUTION_ADDRESS where DISTRIBUTION_TYPE_CODE = 'RT' and submission_id = ";
	private String SELECT_PAYEE = "select SUBMISSION_ID,OCCURRENCE_NO,DISTRIBUTION_TYPE_CODE,RECIPIENT_NO,PAYEE_NO,ADDR_LINE1,ADDR_LINE2,CITY_NAME,STATE_CODE,ZIP_CODE,COUNTRY_CODE,CREATED_USER_PROFILE_ID,CREATED_TS,LAST_UPDATED_USER_PROFILE_ID,LAST_UPDATED_TS from stp100.distribution_address where recipient_no = "
			+ TEST_RECIPIENT_NO
			+ " and payee_no = "
			+ TEST_PAYEE_NO
			+ " and DISTRIBUTION_TYPE_CODE = 'PA' and submission_id =";
	private String SELECT_RECIPIENT = "select SUBMISSION_ID,OCCURRENCE_NO,DISTRIBUTION_TYPE_CODE,RECIPIENT_NO,PAYEE_NO,ADDR_LINE1,ADDR_LINE2,CITY_NAME,STATE_CODE,ZIP_CODE,COUNTRY_CODE,CREATED_USER_PROFILE_ID,CREATED_TS,LAST_UPDATED_USER_PROFILE_ID,LAST_UPDATED_TS from stp100.distribution_address where recipient_no = "
			+ TEST_RECIPIENT_NO + " and DISTRIBUTION_TYPE_CODE = 'RT' and submission_id = ";
	boolean referenceRecipientInserted = false;
	DistributionAddressDao dao = null;

	private Integer TEST_PAYEE_NO_2 = TEST_PAYEE_NO + 1;
	private Integer TEST_RECIPIENT_NO_2 = TEST_RECIPIENT_NO + 1;
	private Class type; // support for different implementations of interface
						// FEE

	public DistributionAddressDaoTestOld(String arg0, Class type) throws IllegalAccessException, InstantiationException {
		super(arg0);
		this.type = type;
		setupValueObjects();
	}

	public static Test suite() throws IllegalAccessException, InstantiationException {
		TestSuite suite = new TestSuite();
		List<Class> supportedClasses = new ArrayList<Class>();
		supportedClasses.add(LoanAddress.class);
		supportedClasses.add(Address.class);
		for (Class myClass : supportedClasses) {

			suite.addTest(new DistributionAddressDaoTestOld("testInsertPayeeAddress", myClass));
			suite.addTest(new DistributionAddressDaoTestOld("testInsertRecipientAddress", myClass));
			suite.addTest(new DistributionAddressDaoTestOld("testDeleteAll", myClass));
		}
		return suite;
	}

	@Override
	public void setUp() throws Exception {

		super.setUp();

		if (dao == null) {
			dao = new DistributionAddressDao();
		}
	}

	private void setupValueObjects() throws IllegalAccessException, InstantiationException {
		voA1 = (DistributionAddress) type.newInstance();
		voB1 = (DistributionAddress) type.newInstance();
		voC1 = (DistributionAddress) type.newInstance();
		voD1 = (DistributionAddress) type.newInstance();
		REFERENCE_RECIPIENT_VO = voC1;
		REFERENCE_PAYEE_VO = voA1;

		// payee 1
		voA1.setSubmissionId(getSubmissionId());
		voA1.setRecipientNo(TEST_RECIPIENT_NO);
		voA1.setDistributionTypeCode(DistributionAddress.PAYEE_TYPE_CODE);
		voA1.setPayeeNo(TEST_PAYEE_NO);
		voA1.setAddressLine1("ALine1");
		voA1.setAddressLine2("ALine2");
		voA1.setCity("ACity");
		voA1.setStateCode("AB");
		voA1.setZipCode("LALALA");
		voA1.setCountryCode("USA");
		voA1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voA1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		// payee 2
		voB1.setSubmissionId(getSubmissionId());
		voB1.setRecipientNo(TEST_RECIPIENT_NO);
		voA1.setDistributionTypeCode(DistributionAddress.PAYEE_TYPE_CODE);
		voB1.setPayeeNo(TEST_PAYEE_NO);
		voB1.setAddressLine1("ALine11");
		voB1.setAddressLine2("ALine22");
		voB1.setCity("ACity12");
		voB1.setStateCode("12");
		voB1.setZipCode("LALA12");
		voB1.setCountryCode("US2");
		voA1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voA1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		// recipient 1
		voC1.setSubmissionId(getSubmissionId());
		voC1.setRecipientNo(TEST_RECIPIENT_NO);
		voC1.setDistributionTypeCode(DistributionAddress.RECIPIENT_TYPE_CODE);
		voC1.setAddressLine1("CLine11");
		voC1.setAddressLine2("CLine22");
		voC1.setCity("CCity12");
		voC1.setStateCode("C2");
		voC1.setZipCode("CALA12");
		voC1.setCountryCode("CS2");
		voC1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voC1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		// recipient 2
		voD1.setSubmissionId(getSubmissionId());
		voD1.setRecipientNo(TEST_RECIPIENT_NO);
		voD1.setDistributionTypeCode(DistributionAddress.RECIPIENT_TYPE_CODE);
		voD1.setAddressLine1("DLine11");
		voD1.setAddressLine2("DLine22");
		voD1.setCity("DCity12");
		voD1.setStateCode("D2");
		voD1.setZipCode("DALA12");
		voD1.setCountryCode("DS2");
		voD1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voD1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
	}

	/**
	 * insert 2 VO's verify 2 records were inserted call deleteAll verify no
	 * records are found
	 * 
	 * @throws Exception
	 */
	public void testDeleteAll() throws Exception {

		dao.insertPayeeAddress(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				TEST_RECIPIENT_NO, TEST_PAYEE_NO, voA1);
		dao.insertPayeeAddress(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				TEST_RECIPIENT_NO, TEST_PAYEE_NO_2, voB1);

		verifyRecordCount(SELECT_RECORD_COUNT_PAYEE + getSubmissionId(), 2);

		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				DistributionAddress.PAYEE_TYPE_CODE);

		verifyRecordCount(SELECT_RECORD_COUNT_PAYEE + getSubmissionId(), 0);

		dao.insertRecipientAddress(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				TEST_RECIPIENT_NO, voC1);
		dao.insertRecipientAddress(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				TEST_RECIPIENT_NO_2, voD1);

		verifyRecordCount(SELECT_RECORD_COUNT_RECIPIENT + getSubmissionId(), 2);

		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				DistributionAddress.RECIPIENT_TYPE_CODE);

		verifyRecordCount(SELECT_RECORD_COUNT_RECIPIENT + getSubmissionId(), 0);

	}

	/**
	 * insert a aVO verify the values of the first record. and verify the record
	 * count is 2
	 * 
	 * @throws Exception
	 */
	public void testInsertPayeeAddress() throws Exception {

		dao.insertPayeeAddress(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				TEST_RECIPIENT_NO, TEST_PAYEE_NO, voA1);
		verifyPayeeValues(voA1, createPayeeVoFromDb());

		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				DistributionAddress.PAYEE_TYPE_CODE);
	}

	/**
	 * insert a aVO verify the values of the first record. and verify the record
	 * count is 2
	 * 
	 * @throws Exception
	 */
	public void testInsertRecipientAddress() throws Exception {

		dao.insertRecipientAddress(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				TEST_RECIPIENT_NO, voC1);
		verifyRecipientValues(voC1, createRecipientVoFromDb());
		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				DistributionAddress.RECIPIENT_TYPE_CODE);

	}

	private DistributionAddress createPayeeVoFromDb() throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i = 1;
		DistributionAddress returnVo = (DistributionAddress) type.newInstance();
		try {
			conn = datasource.getConnection();
			stmt = conn.prepareStatement(SELECT_PAYEE + getSubmissionId());
			rs = stmt.executeQuery();
			if (rs.next()) {
				returnVo.setSubmissionId(new Integer(rs.getInt(i++)));
				i++;// occurance number
				returnVo.setDistributionTypeCode(rs.getString(i++));
				returnVo.setRecipientNo(new Integer(rs.getInt(i++)));
				returnVo.setPayeeNo(new Integer(rs.getInt(i++)));
				returnVo.setAddressLine1(rs.getString(i++));
				returnVo.setAddressLine2(rs.getString(i++));
				returnVo.setCity(rs.getString(i++));
				returnVo.setStateCode(rs.getString(i++));
				returnVo.setZipCode(rs.getString(i++).trim());
				returnVo.setCountryCode(rs.getString(i++));
				returnVo.setCreatedById(rs.getInt(i++));
				returnVo.setCreated(rs.getTimestamp(i++));
				returnVo.setLastUpdatedById(rs.getInt(i++));
				returnVo.setLastUpdated(rs.getTimestamp(i++));
			}
			return returnVo;
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
	}

	private DistributionAddress createRecipientVoFromDb() throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i = 1;
		DistributionAddress returnVo = (DistributionAddress) type.newInstance();
		try {
			conn = datasource.getConnection();
			stmt = conn.prepareStatement(SELECT_RECIPIENT + getSubmissionId());
			rs = stmt.executeQuery();
			if (rs.next()) {
				returnVo.setSubmissionId(new Integer(rs.getInt(i++)));
				i++;// occurance number
				returnVo.setDistributionTypeCode(rs.getString(i++));
				returnVo.setRecipientNo(new Integer(rs.getInt(i++)));
				returnVo.setPayeeNo(new Integer(rs.getInt(i++)));
				returnVo.setAddressLine1(rs.getString(i++));
				returnVo.setAddressLine2(rs.getString(i++));
				returnVo.setCity(rs.getString(i++));
				returnVo.setStateCode(rs.getString(i++).trim());
				returnVo.setZipCode(rs.getString(i++).trim());
				returnVo.setCountryCode(rs.getString(i++).trim());
				returnVo.setCreatedById(rs.getInt(i++));
				returnVo.setCreated(rs.getTimestamp(i++));
				returnVo.setLastUpdatedById(rs.getInt(i++));
				returnVo.setLastUpdated(rs.getTimestamp(i++));
			}
			return returnVo;
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
	}

	public void verifyPayeeValues(DistributionAddress vo1, DistributionAddress vo2) throws Exception {
		assertEquals(vo1.getDistributionTypeCode(), vo2.getDistributionTypeCode());
		assertEquals(vo1.getRecipientNo(), vo2.getRecipientNo());
		assertEquals(vo1.getPayeeNo(), vo2.getPayeeNo());
		assertEquals(vo1.getAddressLine1(), vo2.getAddressLine1());
		assertEquals(vo1.getAddressLine2(), vo2.getAddressLine2());
		assertEquals(vo1.getCity(), vo2.getCity());
		assertEquals(vo1.getStateCode(), vo2.getStateCode());
		assertEquals(vo1.getZipCode().trim(), vo2.getZipCode().trim());
		assertEquals(vo1.getCountryCode(), vo2.getCountryCode());
		assertEquals(vo1.getCreatedById(), vo2.getCreatedById());
		assertEquals(vo1.getLastUpdatedById(), vo2.getLastUpdatedById());
	}

	/**
	 * the vo from the database must be the second vo, since rs.getString will
	 * yeild 0 instead of null
	 * 
	 * @param vo1
	 *            the vo from the CODE
	 * @param vo2
	 *            the vo from the DATABASE
	 */
	public void verifyRecipientValues(final DistributionAddress vo1, final DistributionAddress vo2) {
		assertEquals(vo1.getDistributionTypeCode(), vo2.getDistributionTypeCode());
		assertEquals(vo1.getRecipientNo(), vo2.getRecipientNo());
		assertTrue(vo1.getPayeeNo() == null && vo2.getPayeeNo() == 0);
		assertEquals(vo1.getAddressLine1(), vo2.getAddressLine1());
		assertEquals(vo1.getAddressLine2(), vo2.getAddressLine2());
		assertEquals(vo1.getCity(), vo2.getCity());
		assertEquals(vo1.getStateCode().trim(), vo2.getStateCode().trim());
		assertEquals(vo1.getZipCode().trim(), vo2.getZipCode().trim());
		assertEquals(vo1.getCountryCode().trim(), vo2.getCountryCode().trim());
		assertEquals(vo1.getCreatedById(), vo2.getCreatedById());
		assertEquals(vo1.getLastUpdatedById(), vo2.getLastUpdatedById());
	}

	public DistributionAddress getRecipientVO() throws Exception {
		return voC1;
	}

	public DistributionAddress getPayeeVO() throws Exception {

		return voA1;
	}

}
