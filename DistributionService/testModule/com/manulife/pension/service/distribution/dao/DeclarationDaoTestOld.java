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
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.loan.valueobject.LoanDeclaration;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee;

/**																				
 * FeeDaoTest is the test class for the {@link FeeDao} class.																				
 * 																				
 * @author Dennis_Snowdon																				
 */
public class DeclarationDaoTestOld extends BaseSubmissionDependentTestCase {

	private Declaration voA1 = null;
	private Declaration voB1 = null;
	public Declaration REFERENCE_VO = null;
	public String SELECT_RECORD_COUNT = "select count(*) from stp100.DECLARATION where submission_id = ";
	private String SELECT = "select SUBMISSION_ID,DECLARATION_TYPE_CODE,CREATED_USER_PROFILE_ID,CREATED_TS from stp100.declaration where submission_id = ";
	DeclarationDao dao = null;
	private Class type; //support for different implementations of interface FEE

	public DeclarationDaoTestOld(String arg0, Class type) throws IllegalAccessException, InstantiationException {
		super(arg0);
		this.type = type;
		setupValueObjects();

	}

	public static Test suite() throws IllegalAccessException, InstantiationException {
		TestSuite suite = new TestSuite();
		List<Class> supportedClasses = new ArrayList<Class>();
		supportedClasses.add(LoanDeclaration.class);
		supportedClasses.add(WithdrawalRequestDeclaration.class);
		for (Class myClass : supportedClasses) {

			suite.addTest(new DeclarationDaoTestOld("testInsert", myClass));
			suite.addTest(new DeclarationDaoTestOld("testDeleteAll", myClass));
		}
		return suite;
	}

	@Override
	public void setUp() throws Exception {

		super.setUp();

		if (dao == null) {
			dao = new DeclarationDao();
		}
	}

	private void setupValueObjects() throws IllegalAccessException, InstantiationException {
		voA1 = (Declaration) type.newInstance();
		voB1 = (Declaration) type.newInstance();
		REFERENCE_VO = voA1;

		voA1.setSubmissionId(getSubmissionId());
		voA1.setTypeCode("X");
		voA1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);

		voB1.setSubmissionId(getSubmissionId());
		voB1.setTypeCode("Y");
		voB1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
	}

	/**																				
	 * insert 2 VO's																				
	 * verify 2 records were inserted																				
	 * call deleteAll																				
	 * verify no records are found																				
	 * @throws Exception																				
	 */
	public void testDeleteAll() throws Exception {

		Collection<Declaration> decls = new ArrayList<Declaration>();
		decls.add(voA1);
		decls.add(voB1);
		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, decls);
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

		Collection<Declaration> decls = new ArrayList<Declaration>();
		decls.add(voA1);
		decls.add(voB1);
		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, decls);
		verifyValues(voA1, createVoFromDb());
		verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 2);
		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);

	}

	private Declaration createVoFromDb() throws Exception {
		List<? extends Declaration> declarations = dao.select(getSubmissionId(), null, null, WithdrawalRequestDeclaration.class);
		if (declarations.size() > 0) {
			return declarations.get(0);
		}
		return null;
	}

	public void verifyValues(Declaration vo1, Declaration vo2) throws Exception {
		assertEquals(vo1.getTypeCode().trim(), vo2.getTypeCode().trim());
		assertEquals(vo1.getCreatedById(), vo2.getCreatedById());
	}

	public Collection<Declaration> getVOs(Integer count) throws Exception {
		Collection<Declaration> vos = new ArrayList<Declaration>();
		vos.add(voA1);
		if (count > 1) {
			vos.add(voB1);
		}
		return vos;
	}
}
