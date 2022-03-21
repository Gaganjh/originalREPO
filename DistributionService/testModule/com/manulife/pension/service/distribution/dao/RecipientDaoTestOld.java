/*																				
 * DeclarationDaoTest.java,v 1.1 2006/09/07 18:50:35 Paul_Glenn Exp																				
 * DeclarationDaoTest.java,v																				
 * Revision 1.1  2006/09/07 18:50:35  Paul_Glenn																				
 * Initial.																				
 *																				
 */
package com.manulife.pension.service.distribution.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.distribution.valueobject.Recipient;
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
public class RecipientDaoTestOld extends BaseSubmissionDependentTestCase {
	private Recipient voA1 = null;
	private Recipient voB1 = null;
	private Recipient voZ1 = null;
	public Recipient REFERENCE_VO = null;
	public String SELECT_RECORD_COUNT = "select count(*) from stp100.RECIPIENT where submission_id = ";
	private String SELECT = "select SUBMISSION_ID,RECIPIENT_NO,FIRST_NAME,LAST_NAME,ORGANIZATION_NAME,US_CITIZEN_IND,RESIDENCE_STATE_CODE,SHARE_TYPE_CODE,SHARE_VALUE,FEDERAL_TAX_PCT,STATE_TAX_PCT,STATE_TAX_TYPE_CODE,TAXPAYER_IDENT_TYPE_CODE,TAXPAYER_IDENT_NO,CREATED_USER_PROFILE_ID,CREATED_TS,LAST_UPDATED_USER_PROFILE_ID,LAST_UPDATED_TS from stp100.recipient where submission_id = ? AND recipient_no = ?";
	RecipientDao dao = null;
	private Class type; //support for different implementations of interface FEE

	public RecipientDaoTestOld(String arg0, Class type) throws IllegalAccessException, InstantiationException {
		super(arg0);
		this.type = type;
		setupValueObjects();
	}

	public static Test suite() throws IllegalAccessException, InstantiationException {
		TestSuite suite = new TestSuite();
		List<Class> supportedClasses = new ArrayList<Class>();
		supportedClasses.add(LoanRecipient.class);
		supportedClasses.add(WithdrawalRequestRecipient.class);
		for (Class myClass : supportedClasses) {

			suite.addTest(new RecipientDaoTestOld("testInsert", myClass));
			suite.addTest(new RecipientDaoTestOld("testDeleteAll", myClass));
		}
		return suite;
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		if (dao == null) {
			dao = new RecipientDao();
		}
	}

	private void setupValueObjects() throws IllegalAccessException, InstantiationException {
		voA1 = (Recipient) type.newInstance();
		voB1 = (Recipient) type.newInstance();
		voZ1 = (Recipient) type.newInstance();

		REFERENCE_VO = voA1;

		voA1.setSubmissionId(getSubmissionId());
		voA1.setRecipientNo(1);
		voA1.setFirstName("firstname1");
		voA1.setLastName("lastname1");
		voA1.setOrganizationName("orgname1");
		voA1.setUsCitizenInd(true);
		voA1.setStateOfResidenceCode("NY");
		voA1.setShareTypeCode("A");
		voA1.setShareValue(new BigDecimal(1.51).setScale(2, BigDecimal.ROUND_FLOOR));
		voA1.setFederalTaxPercent(new BigDecimal(2.5331).setScale(4, BigDecimal.ROUND_FLOOR));
		voA1.setStateTaxPercent(new BigDecimal(3.5414).setScale(4, BigDecimal.ROUND_FLOOR));
		voA1.setStateTaxTypeCode("B");
		voA1.setTaxpayerIdentTypeCode("C");
		voA1.setTaxpayerIdentNo("D");
		voA1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voA1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		voB1.setSubmissionId(getSubmissionId());
		voB1.setRecipientNo(2);
		voB1.setFirstName("firstname2");
		voB1.setLastName("lastname2");
		voB1.setOrganizationName("orgname2");
		voB1.setUsCitizenInd(false);
		voB1.setStateOfResidenceCode("AZ");
		voB1.setShareTypeCode("Z");
		voB1.setShareValue(new BigDecimal(4.61).setScale(2, BigDecimal.ROUND_FLOOR));
		voB1.setFederalTaxPercent(new BigDecimal(5.7131).setScale(4, BigDecimal.ROUND_FLOOR));
		voB1.setStateTaxPercent(new BigDecimal(7.1231).setScale(4, BigDecimal.ROUND_FLOOR));
		voB1.setStateTaxTypeCode("Y");
		voB1.setTaxpayerIdentTypeCode("X");
		voB1.setTaxpayerIdentNo("W");
		voB1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voB1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);

		voZ1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
		voZ1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);
		voZ1.setRecipientNo(9);
		voZ1.setSubmissionId(getSubmissionId());

	}

	/**																				
	 * insert 2 VO's																				
	 * verify 2 records were inserted																				
	 * call deleteAll																				
	 * verify no records are found																				
	 * @throws Exception																				
	 */
	public void testDeleteAll() throws Exception {

		Collection<Recipient> recipients = new ArrayList<Recipient>();
		recipients.add(voA1);
		recipients.add(voB1);

		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, recipients);

		verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 2);

		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 0);

	}

	/**																				
	 * create a colelction of 2 VO's and insert them.																				
	 * verify the values of the first record.																				
	 * and verify the record count is 2																				
	 * 																				
	 * @throws Exception																				
	 */
	public void testInsert() throws Exception {

		Collection<Recipient> recipients1 = new ArrayList<Recipient>();
		Collection<Recipient> recipients2 = new ArrayList<Recipient>();
		recipients1.add(voA1);
		recipients1.add(voB1);
		recipients2.add(voZ1);
		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, recipients1);
		verifyValues(voA1, createVoFromDb(voA1.getRecipientNo()));
		verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 2);
		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, recipients2);
		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);

	}

	private Recipient createVoFromDb(Integer recipientNo) throws Exception {
		List<? extends Recipient> recipients = dao.select(getSubmissionId(),
				null, null, WithdrawalRequestRecipient.class,
				WithdrawalRequestPayee.class, Address.class,
				PayeePaymentInstruction.class);
		for (Recipient recipient: recipients) {
			if (recipientNo.equals(recipient.getRecipientNo())) {
				return recipient;
			}
		}
		return null;
	}

	public void verifyValues(Recipient vo1, Recipient vo2) throws Exception {
		assertEquals(vo1.getRecipientNo(), vo2.getRecipientNo());
		assertTrue(StringUtils.equalsIgnoreCase(StringUtils.trim(vo1.getFirstName()), StringUtils.trim(vo2
				.getFirstName())));
		assertTrue(StringUtils.equalsIgnoreCase(StringUtils.trim(vo1.getLastName()), StringUtils
				.trim(vo2.getLastName())));
		assertEquals(vo1.getOrganizationName(), vo2.getOrganizationName());
		assertEquals(vo1.getUsCitizenInd(), vo2.getUsCitizenInd());
		assertEquals(vo1.getStateOfResidenceCode(), vo2.getStateOfResidenceCode());
		assertEquals(vo1.getShareTypeCode(), vo2.getShareTypeCode());
		assertEquals(vo1.getShareValue(), vo2.getShareValue());
		assertEquals(vo1.getFederalTaxPercent(), vo2.getFederalTaxPercent());
		assertEquals(vo1.getStateTaxPercent(), vo2.getStateTaxPercent());
		assertEquals(vo1.getStateTaxTypeCode(), vo2.getStateTaxTypeCode());
		assertEquals(vo1.getTaxpayerIdentTypeCode(), vo2.getTaxpayerIdentTypeCode());
		assertEquals(vo1.getTaxpayerIdentNo().trim(), vo2.getTaxpayerIdentNo().trim());
		assertEquals(vo1.getCreatedById(), vo2.getCreatedById());
		assertEquals(vo1.getLastUpdatedById(), vo2.getLastUpdatedById());
	}

	public void insertReference(Integer submissionId, Integer count) throws Exception {
		if (dao == null) {
			dao = new RecipientDao();
		}
		Collection<Recipient> vos = new ArrayList<Recipient>();
		vos.add(voA1);
		if (count > 1) {
			vos.add(voB1);
		}

		dao.insert(submissionId, DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, vos);

	}

	public List<Recipient> getVO(int count) throws Exception {
		List<Recipient> vos = new ArrayList<Recipient>();
		vos.add(voA1);
		if (count > 1) {
			vos.add(voB1);
		}
		return vos;
	}

}
