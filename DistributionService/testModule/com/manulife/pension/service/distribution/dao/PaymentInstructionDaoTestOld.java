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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.loan.valueobject.LoanPayee;
import com.manulife.pension.service.loan.valueobject.LoanPaymentInstruction;
import com.manulife.pension.service.loan.valueobject.LoanRecipient;
import com.manulife.pension.service.withdrawal.dao.BaseWithdrawalDependentTestCase;
import com.manulife.pension.service.withdrawal.valueobject.PayeePaymentInstruction;

/**																				
 * FeeDaoTest is the test class for the {@link FeeDao} class.																				
 * 																				
 * @author Dennis_Snowdon																				
 */
public class PaymentInstructionDaoTestOld extends BaseSubmissionDependentTestCase {
	private PaymentInstruction voA1 = null;
	private PaymentInstruction voZ1 = null;
	protected String SELECT_RECORD_COUNT = "select count(*) from stp100.PAYEE_PAYMENT_INSTRUCTION where submission_id = ";
	private final String SELECT = "select SUBMISSION_ID,RECIPIENT_NO,PAYEE_NO,BANK_ACCOUNT_TYPE_CODE,BANK_TRANSIT_NO,BANK_ACCOUNT_NO,BANK_NAME,CREDIT_PARTY_NAME,CREATED_USER_PROFILE_ID,CREATED_TS,LAST_UPDATED_USER_PROFILE_ID,LAST_UPDATED_TS from stp100.payee_payment_instruction where submission_id = ? AND recipient_no = ? and payee_no = ?";
	boolean referenceRecipientInserted = false;
	PaymentInstructionDao dao = null;
	public PaymentInstruction REFERENCE_VO;

	private Class type; //support for different implementations of interface FEE

	public PaymentInstructionDaoTestOld(final String arg0, Class type) throws IllegalAccessException,
			InstantiationException {
		super(arg0);
		this.type = type;
		setupValueObjects();
	}

	public static Test suite() throws IllegalAccessException, InstantiationException {
		final TestSuite suite = new TestSuite();
		List<Class> supportedClasses = new ArrayList<Class>();
		supportedClasses.add(LoanPaymentInstruction.class);
		supportedClasses.add(PayeePaymentInstruction.class);
		for (Class myClass : supportedClasses) {

			suite.addTest(new PaymentInstructionDaoTestOld("testInsert", myClass));
			suite.addTest(new PaymentInstructionDaoTestOld("testDeleteAll", myClass));
		}
		return suite;
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		if (dao == null) {
			dao = new PaymentInstructionDao();
		}
		new RecipientDaoTestOld("insertReference", LoanRecipient.class).insertReference(getSubmissionId(),2);
		referenceRecipientInserted = true;
		new PayeeDaoTestOld("insertReference", LoanPayee.class).insertReference(getSubmissionId());
	}

	private void setupValueObjects() throws IllegalAccessException, InstantiationException {
		voA1 = (PaymentInstruction) type.newInstance();
		voZ1 = (PaymentInstruction) type.newInstance();
		REFERENCE_VO = voA1;
		voA1.setSubmissionId(getSubmissionId());
		voA1.setRecipientNo(TEST_RECIPIENT_NO);
		voA1.setPayeeNo(TEST_PAYEE_NO);
		voA1.setBankAccountTypeCode("X");
		voA1.setBankTransitNumber(1233123);
		voA1.setBankAccountNumber("ABC123");
		voA1.setBankName("Bank Of Mervania");
		voA1.setCreditPartyName("Creditman");
		voA1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voA1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voZ1.setSubmissionId(getSubmissionId());
		voZ1.setRecipientNo(TEST_RECIPIENT_NO);
		voZ1.setPayeeNo(TEST_PAYEE_NO);
		voZ1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
	}

	/**																				
	 * insert 2 VO's verify 2 records were inserted call deleteAll verify no records are found																				
	 * 																				
	 * @throws Exception																				
	 */
	public void testDeleteAll() throws Exception {

		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				TEST_RECIPIENT_NO, TEST_PAYEE_NO, voA1);

		verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 1);

		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 0);

	}

	/**																				
	 * create a colelction of 2 VO's and insert them. verify the values of the first record. and																				
	 * verify the record count is 2																				
	 * 																				
	 * @throws Exception																				
	 */
	public void testInsert() throws Exception {

		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				TEST_RECIPIENT_NO, TEST_PAYEE_NO, voA1);
		verifyValues(voA1, createVoFromDb(TEST_RECIPIENT_NO, TEST_PAYEE_NO));
		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);

	}

	@Override
	public void tearDown() throws Exception {
		try {
			if (referenceRecipientInserted) {
				new RecipientDao().deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
						DistributionJUnitConstants.TEST_USER_PROFILE_ID);
			}
		} catch (final Exception e) {
		}
		// don't need to delete the payee since recipientdao.deleteAll will
		// cascade delete it
		super.tearDown();
	}

	private PaymentInstruction createVoFromDb(final Integer recipientNo, final Integer payeeNo) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i = 1;
		final PaymentInstruction returnVo = (PaymentInstruction) type.newInstance();
		try {
			conn = datasource.getConnection();
			stmt = conn.prepareStatement(SELECT);
			stmt.setInt(1, getSubmissionId());
			stmt.setInt(2, recipientNo);
			stmt.setInt(3, payeeNo);
			rs = stmt.executeQuery();
			if (rs.next()) {
				returnVo.setSubmissionId(rs.getInt(i++));
				returnVo.setRecipientNo(rs.getInt(i++));
				returnVo.setPayeeNo(rs.getInt(i++));
				returnVo.setBankAccountTypeCode(rs.getString(i++));
				returnVo.setBankTransitNumber(new Integer(rs.getInt(i++)));
				returnVo.setBankAccountNumber(rs.getString(i++));
				returnVo.setBankName(rs.getString(i++));
				returnVo.setCreditPartyName(rs.getString(i++));
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

	public void verifyValues(final PaymentInstruction vo1, final PaymentInstruction vo2) throws Exception {
		assertEquals(vo1.getRecipientNo(), vo2.getRecipientNo());
		assertEquals(vo1.getPayeeNo(), vo2.getPayeeNo());
		assertEquals(vo1.getBankAccountTypeCode(), vo2.getBankAccountTypeCode());
		assertEquals(vo1.getBankTransitNumber(), vo2.getBankTransitNumber());
		assertEquals(vo1.getBankAccountNumber(), vo2.getBankAccountNumber());
		assertEquals(vo1.getBankName(), vo1.getBankName());
		assertEquals(vo1.getCreditPartyName(), vo2.getCreditPartyName());
		assertEquals(vo1.getCreatedById(), vo2.getCreatedById());
		assertEquals(vo1.getLastUpdatedById(), vo2.getLastUpdatedById());
		assertEquals(vo1.getCreatedById(), vo2.getCreatedById());
		assertEquals(vo1.getLastUpdatedById(), vo2.getLastUpdatedById());
	}

	public PaymentInstruction getVO() {
		return voA1;
	}
}
