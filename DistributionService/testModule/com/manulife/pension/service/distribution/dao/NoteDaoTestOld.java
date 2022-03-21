/*																				
 * NoteDaoTest.java,v 1.1 2006/09/06 19:55:29 Paul_Glenn Exp																				
 * NoteDaoTest.java,v																				
 * Revision 1.1  2006/09/06 19:55:29  Paul_Glenn																				
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
import com.manulife.pension.service.distribution.valueobject.Note;
import com.manulife.pension.service.loan.valueobject.LoanNote;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;

/**
 * NoteDaoTest is the test class for the {@link NoteDao} class.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/06 19:55:29
 */
public class NoteDaoTestOld extends BaseSubmissionDependentTestCase {
	private Note voA1 = null;
	protected String SELECT_RECORD_COUNT = "select count(*) from stp100.NOTE where submission_id = ";
	private String SELECT = "select CREATED_TS,NOTE_TYPE_CODE,NOTE,CREATED_USER_PROFILE_ID from stp100.note where submission_id = ";
	NoteDao dao = null;
	public Note REFERENCE_VO;
	private Class type; // support for different implementations of interface
						// FEE

	public NoteDaoTestOld(String arg0, Class type) {
		super(arg0);
		this.type = type;
		setupValueObjects();
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		List<Class> supportedClasses = new ArrayList<Class>();
		supportedClasses.add(WithdrawalRequestNote.class);
		supportedClasses.add(LoanNote.class);
		for (Class myClass : supportedClasses) {
			suite.addTest(new NoteDaoTestOld("testInsert", myClass));
			suite.addTest(new NoteDaoTestOld("testDeleteAll", myClass));
		}
		return suite;
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		if (dao == null) {
			dao = new NoteDao();
		}
	}

	private void setupValueObjects() {
		voA1 = new WithdrawalRequestNote();
		REFERENCE_VO = voA1;

		voA1.setNote("superdupernote");
		voA1.setNoteTypeCode("AP");
		voA1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
	}

	/**
	 * insert 2 VO's verify 2 records were inserted call deleteAll verify no
	 * records are found
	 * 
	 * @throws Exception
	 */
	public void testDeleteAll() throws Exception {

		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1);
		verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 1);
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

		dao.insert(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1);
		verifyValues(voA1, createVoFromDb());
		verifyRecordCount(SELECT_RECORD_COUNT + getSubmissionId(), 1);

		dao.deleteAll(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);

	}

	/**
	 * insert a record, then call select
	 * 
	 * @throws Exception
	 */
	public void testSelect() throws Exception {
		// TODO - finish this test. NoteDao.select() is not part
		// of our requirements for the April 2007 release. It is
		// never called by our application.

	}

	public Note createVoFromDb() throws Exception {
		List<? extends Note> notes = dao.select(getSubmissionId(), WithdrawalRequestNote.class);
		if (notes.size() > 0) {
			return notes.get(0);
		}
		return null;
	}

	public void verifyValues(Note vo1, Note vo2) throws Exception {
		assertEquals(vo1.getNoteTypeCode(), vo2.getNoteTypeCode());
		assertEquals(vo1.getNote().trim(), vo2.getNote().trim());
		assertEquals(vo1.getCreatedById(), vo2.getCreatedById());

	}

	public Note getVO() throws Exception {
		return voA1;
	}
}
