																		
/*																				
 * DeclarationDaoTest.java,v 1.1 2006/09/07 18:50:35 Paul_Glenn Exp																				
 * DeclarationDaoTest.java,v																				
 * Revision 1.1  2006/09/07 18:50:35  Paul_Glenn																				
 * Initial.																				
 *																				
 */																				
package com.manulife.pension.service.withdrawal.dao;					
import java.math.BigDecimal;																				
import java.sql.Connection;																				
import java.sql.PreparedStatement;																				
import java.sql.ResultSet;																				
import java.sql.Statement;																				
import java.util.ArrayList;																				
import java.util.Collection;					
import junit.framework.Test;																				
import junit.framework.TestSuite;					
import com.manulife.pension.service.dao.BaseDatabaseDAO;																				
import com.manulife.pension.service.distribution.dao.DeclarationDao;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestLoan;					
/**																				
 * DeclarationDaoTest is the test class for the {@link DeclarationDao} class.																				
 * 																				
 * @author Paul_Glenn																				
 * @author glennpa																				
 * @version 1.1 2006/09/07 18:50:35																				
 */																				
public class WithdrawalLoanDaoTestOld extends BaseWithdrawalDependentTestCase {					
    private String DELETE = "delete from stp100.withdrawal_loan_detail where submission_id = " + TEST_SUBMISSION_ID;																				
    private String SELECT = "select SUBMISSION_ID,LOAN_ID,OUTSTANDING_LOAN_AMT,CREATED_USER_PROFILE_ID,CREATED_TS,LAST_UPDATED_USER_PROFILE_ID,LAST_UPDATED_TS from stp100.withdrawal_loan_detail where submission_id = " + TEST_SUBMISSION_ID + " AND LOAN_ID = ?";																				
    protected String SELECT_RECORD_COUNT = "select count(*) from stp100.withdrawal_loan_detail where submission_id = " + TEST_SUBMISSION_ID;					
    																				
    // voA1 & voB1 are for inserts																				
    // voA2 & voB2 are for updating voA1 and voB1 respectively																				
    // voC1 is for deleting																				
    // voA3 is for insertupdating.  it was the a different created by id.																				
    private WithdrawalRequestLoan voA1 = null;																				
    private WithdrawalRequestLoan voA2 = null;																				
    private WithdrawalRequestLoan voA3 = null;																				
    private WithdrawalRequestLoan voB1 = null;																				
    private WithdrawalRequestLoan voB2 = null;																				
    private WithdrawalRequestLoan voC1 = null;					
    private WithdrawalLoanDao dao = null;																				
    public WithdrawalRequestLoan REFERENCE_VO;					
    																				
    public WithdrawalLoanDaoTestOld(String arg0) {																				
        super(arg0);																				
        setupValueObjects();																				
    }					
    public static Test suite() {																				
        TestSuite suite = new TestSuite();																				
        suite.addTest(new WithdrawalLoanDaoTestOld("testInsert"));																				
        suite.addTest(new WithdrawalLoanDaoTestOld("testUpdate"));																				
        suite.addTest(new WithdrawalLoanDaoTestOld("testInsertUpdatePrune"));																				
        suite.addTest(new WithdrawalLoanDaoTestOld("testDelete"));																				
        suite.addTest(new WithdrawalLoanDaoTestOld("testDeleteAll"));																				
        return suite;																				
    }    					
    public void setUp() throws Exception {					
        super.setUp();					
        if (dao == null) {																				
            dao = new WithdrawalLoanDao();																				
        }																				
    }																				
    private void setupValueObjects() {																				
        voA1 = new WithdrawalRequestLoan();																				
        voA2 = new WithdrawalRequestLoan();																				
        voA3 = new WithdrawalRequestLoan();																				
        voB1 = new WithdrawalRequestLoan();																				
        voB2 = new WithdrawalRequestLoan();																				
        voC1 = new WithdrawalRequestLoan();																				
        																				
        REFERENCE_VO = voA1;					
        voA1.setSubmissionId(TEST_SUBMISSION_ID);																				
        voA1.setLoanNo(1);																				
        voA1.setOutstandingLoanAmount(new BigDecimal(1.11).setScale(2, BigDecimal.ROUND_FLOOR));																				
        voA1.setCreatedById(TEST_USER_PROFILE_ID);																				
        voA1.setLastUpdatedById(TEST_USER_PROFILE_ID);					
        voA2.setSubmissionId(TEST_SUBMISSION_ID);																				
        voA2.setLoanNo(voA1.getLoanNo());																				
        voA2.setOutstandingLoanAmount(new BigDecimal(1.12).setScale(2, BigDecimal.ROUND_FLOOR));																				
        voA2.setCreatedById(voA1.getCreatedById());																				
        voA2.setLastUpdatedById(TEST_USER_PROFILE_ID_2);					
        voA3.setSubmissionId(TEST_SUBMISSION_ID);																				
        voA3.setLoanNo(voA1.getLoanNo());																				
        voA3.setOutstandingLoanAmount(voA1.getOutstandingLoanAmount());																				
        voA3.setCreatedById(TEST_USER_PROFILE_ID_2);																				
        voA3.setLastUpdatedById(TEST_USER_PROFILE_ID_2);					
        voB1.setSubmissionId(TEST_SUBMISSION_ID);																				
        voB1.setLoanNo(2);																				
        voB1.setOutstandingLoanAmount(new BigDecimal(2.11).setScale(2, BigDecimal.ROUND_FLOOR));																				
        voB1.setLastUpdatedById(TEST_USER_PROFILE_ID);																				
        voB1.setCreatedById(TEST_USER_PROFILE_ID);					
        voB2.setSubmissionId(TEST_SUBMISSION_ID);																				
        voB2.setLoanNo(voB1.getLoanNo());																				
        voB2.setOutstandingLoanAmount(new BigDecimal(2.12).setScale(2, BigDecimal.ROUND_FLOOR));																				
        voB2.setCreatedById(voB1.getCreatedById());																				
        voB2.setLastUpdatedById(TEST_USER_PROFILE_ID_2);					
        voC1.setSubmissionId(TEST_SUBMISSION_ID);																				
        voC1.setLoanNo(3);																				
        voC1.setOutstandingLoanAmount(new BigDecimal(3.33).setScale(2, BigDecimal.ROUND_FLOOR));																				
        voC1.setCreatedById(TEST_USER_PROFILE_ID);																				
        voC1.setLastUpdatedById(TEST_USER_PROFILE_ID);																				
    }					
    /**																				
     * 1. insert 2 test records ( vo1, vo2 )																				
     * 2. verify values of record 1 ( vo1 )																				
     * 3. verify record count 																				
     */																				
    public final void testInsert() throws Exception {																				
        																				
        					
        Connection conn = datasource.getConnection();;																				
        Statement stmt = null;																				
        																				
        Collection<WithdrawalRequestLoan> loans  = new ArrayList<WithdrawalRequestLoan>();																				
        loans.add(voA1);																				
        loans.add(voB1);					
        try {																				
            dao.insert(TEST_SUBMISSION_ID,TEST_CONTRACT_ID,  TEST_USER_PROFILE_ID, loans);					
            verifyValues(voA1, createVoFromDb(voA1.getLoanNo()));																				
            verifyRecordCount(SELECT_RECORD_COUNT, 2);					
        } catch ( Exception e ) {																				
            assertTrue("found an exception", false);																				
        }																				
        finally {																				
            try {																				
                stmt = conn.createStatement();																				
                stmt.executeUpdate(DELETE);																				
            } catch ( Exception e ) { /* eat it */ }																				
            BaseDatabaseDAO.close(stmt, conn);																				
        }																				
        																				
        																				
    }					
    /**																				
     * 1. insert 2 test records ( vo1, vo2 )																				
     * 2. update 2 test records ( vo3, vo4 )																				
     * 3. compare all values of both records																				
     * 																				
     */																				
    public final void testUpdate() throws Exception {																				
        																				
        Connection conn = datasource.getConnection();																				
        PreparedStatement stmt = null;					
        Collection<WithdrawalRequestLoan> inserts = new ArrayList<WithdrawalRequestLoan>();																				
        Collection<WithdrawalRequestLoan> updates = new ArrayList<WithdrawalRequestLoan>();					
        inserts.add(voA1);																				
        inserts.add(voB1);																				
        updates.add(voA2);																				
        updates.add(voB2);					
        try {																				
            dao.insert(TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, inserts);					
            dao.update(TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID_2, updates);					
            verifyValues(voA2, createVoFromDb(voA2.getLoanNo()));																				
            verifyValues(voB2, createVoFromDb(voB2.getLoanNo()));																				
        } 																				
        																				
        finally {																				
            try {																				
                stmt = conn.prepareStatement(DELETE);																				
                stmt.executeUpdate();																				
            } catch (Exception e ) { /* eat it */ }																				
            BaseDatabaseDAO.close(stmt, conn);																				
        }																				
        																				
        																				
    } 																				
    																				
    /**																				
     * 1st record will be inserted, 2nd record updated, 3rd record pruned.																				
     * 																				
     * 1. verify that 1st record does not exist in database ( vo1 )																				
     * 2. insert 2 records into the database ( vo2, vo5 )																				
     * 3. insertUpdatePrune 2 test records ( vo1 , vo4 )																				
     * 4. verify all values of record 1 ( vo1 )																				
     * 5. verify all values of record 2 ( vo4 )																				
     * 6. verify that 3rd record was deleted ( vo5 )																				
     * ( if 4 and 5 are succesfull, then doing a record count == 2 will be sufficient )																				
     * 																				
     */																				
    public final void testInsertUpdatePrune() throws Exception {																				
        																				
        Connection conn = datasource.getConnection();																				
        PreparedStatement stmt = null;																				
        Collection<WithdrawalRequestLoan> inserts = new ArrayList<WithdrawalRequestLoan>();																				
        Collection<WithdrawalRequestLoan> iup = new ArrayList<WithdrawalRequestLoan>();					
        inserts.add(voB1);																				
        inserts.add(voC1);																				
        iup.add(voA3);																				
        iup.add(voB2);					
        																				
        try {																				
            assertFalse("record 1 did not get deleted", exists(voA3));																				
            dao.insert(TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, inserts);					
            // A3 will be inserted, B1 will get updated to B2, and C1 will get pruned 																				
            dao.insertUpdatePrune(TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID_2, iup);					
            verifyValues(voA3, createVoFromDb(voA3.getLoanNo()));					
            verifyValues(voB2, createVoFromDb(voB2.getLoanNo()));																				
            assertFalse("record 1 did not get deleted", exists(voC1));																				
        } 																				
        																				
        finally {																				
            try {																				
                stmt = conn.prepareStatement(DELETE);																				
                stmt.executeUpdate();																				
            } catch (Exception e ) { /* eat it */ }																				
            BaseDatabaseDAO.close(stmt, conn);																				
        }																				
        																				
        																				
    }        					
    /**																				
     * 1. insert test record																				
     * 2. delete the test record																				
     * 3. verify it was deleted																				
     */																				
    public final void testDelete() throws Exception {																				
        																				
        Connection conn = datasource.getConnection();																				
        PreparedStatement stmt = null;																				
        Collection<WithdrawalRequestLoan> vos = new ArrayList<WithdrawalRequestLoan>();																				
        vos.add(voA1);																				
        vos.add(voB1);																				
        																				
        try {																				
            dao.insert(TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, vos);					
            dao.delete(TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, vos);					
            assertFalse("record 1 did not get deleted", exists(voA1));					
            assertFalse("record 1 did not get deleted", exists(voB1));																				
        } 																				
        																				
        finally {																				
            try {																				
                stmt = conn.prepareStatement(DELETE);																				
                stmt.executeUpdate();																				
            } catch (Exception e) { /* eat it */																				
            }																				
            BaseDatabaseDAO.close(stmt, conn);																				
        }																				
        																				
        																				
    }    																				
    /**																				
     * 1. insert 2 test records																				
     * 2. delete the test records																				
     * 3. verify records are deleted																				
     */																				
    public final void testDeleteAll() throws Exception {																				
        																				
        Connection conn = datasource.getConnection();																				
        PreparedStatement stmt = null;																				
        Collection<WithdrawalRequestLoan> vos = new ArrayList<WithdrawalRequestLoan>();																				
        vos.add(voA1);																				
        vos.add(voB1);																				
        																				
        try {																				
            dao.insert(TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, vos);					
            dao.deleteAll(TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID);					
            verifyRecordCount(SELECT_RECORD_COUNT, 0);					
        } 																				
        																				
        finally {																				
            try {																				
                stmt = conn.prepareStatement(DELETE);																				
                stmt.executeUpdate();																				
            } catch (Exception e) { /* eat it */ }																				
            BaseDatabaseDAO.close(stmt, conn);																				
        }																				
        																				
        																				
    }         																				
    																				
    																				
    private WithdrawalRequestLoan createVoFromDb(Integer loanNo) throws Exception {																				
        Connection conn = null;																				
        PreparedStatement stmt = null;																				
        ResultSet rs = null;																				
        int i = 1;																				
        WithdrawalRequestLoan returnVo = new WithdrawalRequestLoan();																				
        try {																				
            conn = datasource.getConnection();																				
            stmt = conn.prepareStatement(SELECT);																				
            stmt.setInt(1, loanNo);																				
            rs = stmt.executeQuery();																				
            if (rs.next()) {																				
                returnVo.setSubmissionId(new Integer(rs.getInt(i++)));																				
                returnVo.setLoanNo(new Integer(rs.getInt(i++)));																				
                returnVo.setOutstandingLoanAmount(rs.getBigDecimal(i++));																				
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
    																				
    public void verifyValues(WithdrawalRequestLoan vo1, WithdrawalRequestLoan vo2) {																				
        assertEquals(vo1.getSubmissionId(),vo2.getSubmissionId());																				
        assertEquals(vo1.getLoanNo(),vo2.getLoanNo());																				
        assertEquals(vo1.getOutstandingLoanAmount(),vo2.getOutstandingLoanAmount());																				
        assertEquals(vo1.getCreatedById(),vo2.getCreatedById());																				
        assertEquals(vo1.getLastUpdatedById(),vo2.getLastUpdatedById());																				
    }																				
    																				
    private boolean exists(WithdrawalRequestLoan vo) throws Exception {																				
        Connection conn = null;																				
        PreparedStatement stmt = null;																				
        ResultSet rs = null;																				
        try {																				
            conn = datasource.getConnection();																				
            stmt = conn.prepareStatement(SELECT);																				
            stmt.setInt(1, vo.getLoanNo());																				
            rs = stmt.executeQuery();																				
            return rs.next();																				
        }																				
        finally {																				
            BaseDatabaseDAO.close(stmt, conn);																				
        }    																				
    }																				
    public Collection<WithdrawalRequestLoan> getVOs(int count) {					
        Collection<WithdrawalRequestLoan> loans  = new ArrayList<WithdrawalRequestLoan>();																				
        loans.add(voA1);																				
        if ( count > 1 ) {																				
            loans.add(voB1);																				
        }																				
        return loans;																				
    }																				
}																				
