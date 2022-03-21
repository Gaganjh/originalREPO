/*																				
 * DeclarationDaoTest.java,v 1.1 2006/09/07 18:50:35 Paul_Glenn Exp																				
 * DeclarationDaoTest.java,v																				
 * Revision 1.1  2006/09/07 18:50:35  Paul_Glenn																				
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

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.loan.valueobject.LoanPayee;
import com.manulife.pension.service.loan.valueobject.LoanRecipient;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.PayeePaymentInstruction;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * DeclarationDaoTest is the test class for the {@link DeclarationDao} class.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/07 18:50:35
 */
public class PayeeDaoTestOld extends BaseSubmissionDependentTestCase {
	private Payee voA1 = null;
	private Payee voB1 = null;
	private Payee voZ1 = null;
	protected String SELECT_RECORD_COUNT = "select count(*) from stp100.PAYEE where submission_id = ";
	private final String SELECT = "select SUBMISSION_ID,RECIPIENT_NO,PAYEE_NO,FIRST_NAME,LAST_NAME,ORGANIZATION_NAME,PAYEE_TYPE_CODE,PAYEE_REASON_CODE,PAYMENT_METHOD_CODE,SHARE_TYPE_CODE,SHARE_VALUE,ROLLOVER_ACCOUNT_NO,ROLLOVER_PLAN_NAME,IRS_DIST_CODE_WITHDRAWAL,MAIL_CHECK_TO_ADDRESS_IND,SEND_CHECK_BY_COURIER_IND,COURIER_COMPANY_CODE,COURIER_NO,CREATED_USER_PROFILE_ID,CREATED_TS,LAST_UPDATED_USER_PROFILE_ID,LAST_UPDATED_TS from stp100.payee where submission_id = ? AND recipient_no = ? and payee_no = ?";
	boolean referenceRecipientInserted = false;
	PayeeDao dao = null;
	public Payee REFERENCE_VO;

	private Class type; // support for different implementations of interface
						// FEE

	public PayeeDaoTestOld(final String arg0, Class type) throws IllegalAccessException, InstantiationException {
		super(arg0);
		this.type = type;
		setupValueObjects();
	}

	public static Test suite() throws IllegalAccessException, InstantiationException {
		final TestSuite suite = new TestSuite();
		List<Class> supportedClasses = new ArrayList<Class>();
		supportedClasses.add(LoanPayee.class);
		supportedClasses.add(WithdrawalRequestPayee.class);
		for (Class myClass : supportedClasses) {

			suite.addTest(new PayeeDaoTestOld("testInsert", myClass));
			suite.addTest(new PayeeDaoTestOld("testDeleteAll", myClass));
		}
		return suite;
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		new RecipientDaoTestOld("insertReference", LoanRecipient.class).insertReference( getSubmissionId(),2 );
		referenceRecipientInserted = true;
		if (dao == null) {
			dao = new PayeeDao();
		}
	}

	private void setupValueObjects() throws IllegalAccessException, InstantiationException {
		voA1 = (Payee) type.newInstance();
		voB1 = (Payee) type.newInstance();
		voZ1 = (Payee) type.newInstance();
		REFERENCE_VO = voA1;
		voA1.setSubmissionId(getSubmissionId());
		voA1.setRecipientNo(TEST_RECIPIENT_NO);
		voA1.setPayeeNo(TEST_PAYEE_NO);
		voA1.setFirstName("firstname1");
		voA1.setLastName("lastname1");
		voA1.setOrganizationName("orgname1");
		voA1.setTypeCode("AA");
		voA1.setReasonCode("A");
		voA1.setPaymentMethodCode("CC");
		voA1.setShareTypeCode("B");
		voA1.setShareValue(new BigDecimal(1.51).setScale(2, BigDecimal.ROUND_FLOOR));
		voA1.setRolloverAccountNo("givemeallyourmoney");
		voA1.setRolloverPlanName("planningonstealingmoney");
		voA1.setIrsDistCode("DDD");
		voA1.setMailCheckToAddress(false);
		voA1.setSendCheckByCourier(false);
		voA1.setCourierCompanyCode("C");
		voA1.setCourierNo("ABC123");
		voA1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voA1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		voB1.setSubmissionId(getSubmissionId());
		voB1.setRecipientNo(TEST_RECIPIENT_NO);
		voB1.setPayeeNo(2);
		voB1.setFirstName("firstname2");
		voB1.setLastName("lastname2");
		voB1.setOrganizationName("orgname2");
		voB1.setTypeCode("EE");
		voB1.setReasonCode("D");
		voB1.setPaymentMethodCode("GG");
		voB1.setShareTypeCode("E");
		voB1.setShareValue(new BigDecimal(2.51).setScale(2, BigDecimal.ROUND_FLOOR));
		voB1.setRolloverAccountNo("givemeallyourcash");
		voB1.setRolloverPlanName("planningonstealingcash");
		voB1.setIrsDistCode("HHH");
		voB1.setMailCheckToAddress(true);
		voB1.setSendCheckByCourier(true);
		voB1.setCourierCompanyCode("F");
		voB1.setCourierNo("BCA123");
		voB1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voB1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voZ1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voZ1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voZ1.setPayeeNo(3);
		voZ1.setReasonCode("D");
		voZ1.setTypeCode("EE");
		voZ1.setRecipientNo(TEST_RECIPIENT_NO);
		voZ1.setSubmissionId(getSubmissionId());
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
		super.tearDown();
	}

	/**
	 * insert 2 VO's verify 2 records were inserted call deleteAll verify no
	 * records are found
	 * 
	 * @throws Exception
	 */
	public void testDeleteAll() throws Exception {

		final Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(voA1);
		payees.add(voB1);

		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				TEST_RECIPIENT_NO, payees);

		verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 2);

		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 0);

	}

	/**
	 * create a colelction of 2 VO's and insert them. verify the values of the
	 * first record. and verify the record count is 2
	 * 
	 * @throws Exception
	 */
	public void testInsert() throws Exception {

		final Collection<Payee> payees1 = new ArrayList<Payee>();
		final Collection<Payee> payees2 = new ArrayList<Payee>();
		payees1.add(voA1);
		payees1.add(voB1);
		payees2.add(voZ1);

		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				TEST_RECIPIENT_NO, payees1);
		verifyValues(voA1, createVoFromDb(voA1.getRecipientNo(), voA1.getPayeeNo()));
		verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 2);
		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				TEST_RECIPIENT_NO, payees2);
		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);		

	}

	public Payee createVoFromDb(final Integer recipientNo, final Integer payeeNo) throws Exception {
		List<? extends Payee> payees = dao.select(getSubmissionId(), null,
				null, WithdrawalRequestPayee.class, Address.class, PayeePaymentInstruction.class);
		for (Payee payee : payees) {
			if (payee.getRecipientNo().equals(recipientNo)
					&& payee.getPayeeNo().equals(payeeNo)) {
				return payee;
			}
		}
		return null;
	}

	public void verifyValues(final Payee vo1, final Payee vo2) {
		assertEquals(vo1.getRecipientNo(), vo2.getRecipientNo());
		assertEquals(vo1.getPayeeNo(), vo2.getPayeeNo());
		assertTrue(StringUtils.equalsIgnoreCase(StringUtils.trim(vo1.getFirstName()), StringUtils.trim(vo2
				.getFirstName())));
		assertTrue(StringUtils.equalsIgnoreCase(StringUtils.trim(vo1.getLastName()), StringUtils
				.trim(vo2.getLastName())));
		assertEquals(vo1.getOrganizationName(), vo2.getOrganizationName());
		assertEquals(vo1.getTypeCode(), vo2.getTypeCode());
		assertEquals(vo1.getReasonCode(), vo2.getReasonCode());
		assertEquals(vo1.getPaymentMethodCode(), vo2.getPaymentMethodCode());
		assertEquals(vo1.getShareTypeCode(), vo2.getShareTypeCode());
		assertEquals(vo1.getShareValue(), vo2.getShareValue());
		assertEquals(vo1.getRolloverAccountNo(), vo2.getRolloverAccountNo());
		assertEquals(vo1.getRolloverPlanName(), vo2.getRolloverPlanName());
		assertEquals(vo1.getIrsDistCode(), vo2.getIrsDistCode());
		assertEquals(vo1.getMailCheckToAddress(), vo2.getMailCheckToAddress());
		assertEquals(vo1.getSendCheckByCourier(), vo2.getSendCheckByCourier());
		assertEquals(vo1.getCourierCompanyCode(), vo2.getCourierCompanyCode());
		assertEquals(vo1.getCourierNo(), vo2.getCourierNo());
		assertEquals(vo1.getCreatedById(), vo2.getCreatedById());
		assertEquals(vo1.getLastUpdatedById(), vo2.getLastUpdatedById());
	}

	public void insertReference(Integer submissionId) throws Exception {
		if (dao == null) {
			dao = new PayeeDao();
		}
		final Collection<Payee> vos = new ArrayList<Payee>();
		vos.add(voA1);

		dao.insert(submissionId, DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID,
				TEST_RECIPIENT_NO, vos);

	}

	public Payee getVO() throws Exception {
		return voA1;
	}
}
