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
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivitySummary;
/**																				
 * ActivitySummaryDaoTest is the test class for the {@link ActivitySummaryDao} class.																				
 * 																				
 * @author Paul_Glenn																				
 * @author glennpa																				
 * @version 1.1 2006/09/06 19:55:29																				
 */																				
public class ActivitySummaryDaoTestOld extends BaseSubmissionDependentTestCase implements DistributionJUnitConstants {					
    private WithdrawalActivitySummary voA1 = null;					
    private WithdrawalActivitySummary voB1 = null;					
    protected String SELECT_RECORD_COUNT = "select count(*) from stp100.ACTIVITY_SUMMARY where submission_id = "																				
            + TEST_SUBMISSION_ID;					
    private String SELECT = "select SUBMISSION_ID,STATUS_CODE,CREATED_USER_PROFILE_ID,CREATED_TS from stp100.ACTIVITY_summary where submission_id = "																				
            + TEST_SUBMISSION_ID + " and status_code = ?";					
    private String DELETE = "delete from stp100.ACTIVITY_summary where submission_id = "																				
            + TEST_SUBMISSION_ID;					
    ActivitySummaryDao dao = null;					
    public ActivitySummaryDaoTestOld(String arg0) {																				
        super(arg0);																				
        setupValueObjects();																				
    }					
    public static Test suite() {																				
        TestSuite suite = new TestSuite();																				
        suite.addTest(new ActivitySummaryDaoTestOld("testDelete"));																				
        suite.addTest(new ActivitySummaryDaoTestOld("testDeleteAll"));																				
        suite.addTest(new ActivitySummaryDaoTestOld("testInsert"));																				
        suite.addTest(new ActivitySummaryDaoTestOld("testSelect"));																				
        return suite;																				
    }					
    @Override																				
    public void setUp() throws Exception {																				
        super.setUp();																				
        if (dao == null) {																				
            dao = new ActivitySummaryDao();																				
        }																				
    }					
    private void setupValueObjects() {																				
        voA1 = new WithdrawalActivitySummary();																				
        voB1 = new WithdrawalActivitySummary();					
        voA1.setActionCode("AA");																				
        voA1.setActionTimestamp(new Timestamp(new Date().getTime()));																				
        voA1.setCreatedById(TEST_USER_PROFILE_ID);					
        voB1.setActionCode("BB");																				
        voB1.setActionTimestamp(new Timestamp(new Date().getTime()));																				
        voB1.setCreatedById(TEST_USER_PROFILE_ID);																				
    }					
    /**																				
     * insert 1 VO's verify 1 records were inserted call delete verify no records are found																				
     * 																				
     * @throws Exception																				
     */																				
    public void testDelete() throws Exception {																				
        					
        																				
        dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, voA1);																				
        dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, voB1);																				
        					
        																				
        verifyRecordCount(SELECT_RECORD_COUNT, 2);																				
        					
        																				
        dao.delete( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, voA1);																				
        					
        																				
        verifyRecordCount(SELECT_RECORD_COUNT, 1);																				
        					
        																				
        verifyValues(voB1, createVoFromDb(voB1));	
        
        dao.deleteAll( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID);
        					
        					
    }					
    /**																				
     * insert 2 VO's verify 2 records were inserted call deleteAll verify no records are found																				
     * 																				
     * @throws Exception																				
     */																				
    public void testDeleteAll() throws Exception {																				
        					
        																				
        dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, voA1);																				
        dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, voB1);																				
        					
        																				
        verifyRecordCount(SELECT_RECORD_COUNT, 2);																				
        					
        																				
        dao.deleteAll( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID);																				
        					
        																				
        verifyRecordCount(SELECT_RECORD_COUNT, 0);																				
        					
        					
    }					
    /**																				
     * create a colelction of 2 VO's and insert them. verify the values of the first record. and																				
     * verify the record count is 2																				
     * 																				
     * @throws Exception																				
     */																				
    public void testInsert() throws Exception {																				
        					
        Connection conn = null;																				
        Statement stmt = null;					
        try {																				
            conn = datasource.getConnection();					
            dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, voA1);					
            verifyValues(voA1, createVoFromDb(voA1));					
            verifyRecordCount(SELECT_RECORD_COUNT, 1);					
        } catch (Exception e) {																				
            assertTrue("found an exception", false);																				
        } finally {					
            try {																				
                    stmt = conn.createStatement();																				
                stmt.executeUpdate(DELETE);																				
                } catch (Exception e) { /* eat it */																				
            }					
            BaseDatabaseDAO.close(stmt, conn);																				
        }																				
        																				
    }					
    /**																				
     * insert a record, then call select																				
     * 																				
     * @throws Exception																				
     */																				
    public void testSelect() throws Exception {					
        					
        Connection conn = datasource.getConnection();																				
        ;																				
        Statement stmt = null;					
        List<WithdrawalActivitySummary> selects = null;					
        try {																				
            dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, voA1);																				
            dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, voB1);					
            selects = (List<WithdrawalActivitySummary>)dao.select( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, WithdrawalActivitySummary.class);					
            assertTrue("sizes don't match", selects.size() == 2);					
            verifyValues(selects.get(0), voA1);					
        } catch (Exception e) {																				
            assertTrue("found an exception", false);																				
        } finally {					
            try {																				
                stmt = conn.createStatement();																				
                stmt.executeUpdate(DELETE);																				
            } catch (Exception e) { /* eat it */																				
            }					
            BaseDatabaseDAO.close(stmt, conn);																				
        }																				
        																				
    }					
    public WithdrawalActivitySummary createVoFromDb(WithdrawalActivitySummary vo) throws Exception {																				
        Connection conn = null;																				
        PreparedStatement stmt = null;																				
        ResultSet rs = null;																				
        WithdrawalActivitySummary returnVo = new WithdrawalActivitySummary();																				
        int i = 2;																				
        try {																				
            conn = datasource.getConnection();																				
            stmt = conn.prepareStatement(SELECT);																				
            stmt.setString(1, vo.getActionCode());																				
            rs = stmt.executeQuery();																				
            if (rs.next()) {																				
                returnVo.setActionCode(rs.getString(i++));																				
                returnVo.setCreatedById(rs.getInt(i++));																				
                returnVo.setActionTimestamp(new Timestamp(rs.getDate(i++).getTime()));																				
            }																				
            return returnVo;																				
        } finally {																				
            BaseDatabaseDAO.close(stmt, conn);																				
        }																				
    }					
    public void verifyValues(WithdrawalActivitySummary vo1, WithdrawalActivitySummary vo2) throws Exception {																				
        assertEquals(vo1.getActionCode(), vo2.getActionCode());																				
        assertEquals(vo1.getCreatedById(), vo2.getCreatedById());					
    }					
}																				
