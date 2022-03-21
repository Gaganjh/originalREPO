/*
 * NoteDaoTest.java,v 1.1 2006/09/06 19:55:29 Paul_Glenn Exp
 * NoteDaoTest.java,v
 * Revision 1.1  2006/09/06 19:55:29  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.service.withdrawal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao;
import com.manulife.pension.service.distribution.dao.NoteDao;
import com.manulife.pension.service.distribution.valueobject.ActivityDynamicDetail;

/**
 * WithdrawalDaoTest is the test class for the {@link NoteDao} class.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/06 19:55:29
 */
public class ActivityDynamicDetailDaoTestOld extends BaseWithdrawalDependentTestCase {

    private ActivityDynamicDetail voA1 = null;

    private ActivityDynamicDetail voA2 = null;

    private ActivityDynamicDetail voB1 = null;

    private ActivityDynamicDetail voB2 = null;

    protected String SELECT_RECORD_COUNT = "select count(*) from stp100.ACTIVITY_DYNAMIC_DETAIL where submission_id = "
            + TEST_SUBMISSION_ID;

    private String SELECT = "select SUBMISSION_ID,ITEM_NO,ITEM_NO_SUB_NAME,ITEM_NO_SUB_NAME_NO,LAST_UPDATED_TS,ACTIVITY_TYPE_CODE,ENTERED_VALUE,LAST_UPDATED_USER_PROFILE_ID from stp100.ACTIVITY_DYNAMIC_DETAIL where submission_id = "
            + TEST_SUBMISSION_ID
            + " and item_no = ?  and item_no_sub_name = ? and item_no_sub_name_no = ? and ACTIVITY_TYPE_CODE = ?";

    private String DELETE = "delete from stp100.ACTIVITY_DYNAMIC_DETAIL where submission_id = "
            + TEST_SUBMISSION_ID;

    private String EXISTS = "select * from stp100.ACTIVITY_DYNAMIC_DETAIL where submission_id = ? and item_no = ? and item_no_sub_name = ? and item_no_sub_name_no = ? and ACTIVITY_TYPE_CODE = ?";

    ActivityDynamicDetailDao dao = null;

    public ActivityDynamicDetailDaoTestOld(String arg0) {
        super(arg0);
        setupValueObjects();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new ActivityDynamicDetailDaoTestOld("testDelete"));
        suite.addTest(new ActivityDynamicDetailDaoTestOld("testDeleteAll"));
        suite.addTest(new ActivityDynamicDetailDaoTestOld("testInsert"));
        suite.addTest(new ActivityDynamicDetailDaoTestOld("testInsertUpdate"));
        
        // Error during the Execution of Store Proc
//        suite.addTest(new ActivityDynamicDetailDaoTest("testSelect"));
        suite.addTest(new ActivityDynamicDetailDaoTestOld("testUpdate"));
        return suite;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        new ActivityDetailDaoTestOld("insertReference").insertReference(1);

        if (dao == null) {
            dao = new ActivityDynamicDetailDao();
        }
    }

    private void setupValueObjects() {
        voA1 = new ActivityDynamicDetail();
        voA2 = new ActivityDynamicDetail();
        voB1 = new ActivityDynamicDetail();
        voB2 = new ActivityDynamicDetail();

        voA1.setTypeCode("A"); // matches reference record inserted by ActivityDetailDaoTest.
        voA1.setItemNumber(1); // matches reference record inserted by ActivityDetailDaoTest.
        voA1.setSecondaryName("secondname1");
        voA1.setSecondaryNumber(11);
        voA1.setLastUpdatedById(TEST_USER_PROFILE_ID);
        voA1.setLastUpdated(new Timestamp(new Date().getTime()));
        voA1.setValue("myVal1");

        voA2.setTypeCode(voA1.getTypeCode());// matches reference record inserted by
                                                // ActivityDetailDaoTest.
        voA2.setItemNumber(voA1.getItemNumber());// matches reference record inserted by
                                                    // ActivityDetailDaoTest.
        voA2.setSecondaryName(voA1.getSecondaryName());
        voA2.setSecondaryNumber(voA1.getSecondaryNumber());
        voA2.setLastUpdatedById(TEST_USER_PROFILE_ID_2);
        voA2.setLastUpdated(new Timestamp(new Date().getTime()));
        voA2.setValue("myVal2");

        voB1.setTypeCode("B");// matches reference record inserted by ActivityDetailDaoTest.
        voB1.setItemNumber(2);// matches reference record inserted by ActivityDetailDaoTest.
        voB1.setSecondaryName("Bsecondname2");
        voB1.setSecondaryNumber(23);
        voB1.setLastUpdatedById(TEST_USER_PROFILE_ID);
        voB1.setLastUpdated(new Timestamp(new Date().getTime()));
        voB1.setValue("myVal3");

        voB2.setTypeCode(voB1.getTypeCode());// matches reference record inserted by
                                                // ActivityDetailDaoTest.
        voB2.setItemNumber(voB1.getItemNumber());// matches reference record inserted by
                                                    // ActivityDetailDaoTest.
        voB2.setSecondaryName(voB1.getSecondaryName());
        voB2.setSecondaryNumber(voB1.getSecondaryNumber());
        voB2.setLastUpdatedById(TEST_USER_PROFILE_ID_2);
        voB2.setLastUpdated(new Timestamp(new Date().getTime()));
        voB2.setValue("myValB2");
    }

    /**
     * insert 1 VO's verify 1 records were inserted call delete verify no records are found
     * 
     * @throws Exception
     */
    public void testDelete() throws Exception {
        Collection<ActivityDynamicDetail> deletes = new ArrayList<ActivityDynamicDetail>();
        Collection<ActivityDynamicDetail> inserts = new ArrayList<ActivityDynamicDetail>();

        deletes.add(voA1);

        inserts.add(voA1);
        inserts.add(voB1);

        dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, inserts);
        verifyRecordCount(SELECT_RECORD_COUNT, 2);
        dao.delete( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, deletes);
        verifyRecordCount(SELECT_RECORD_COUNT, 1);
        verifyValues(voB1, createVoFromDb(voB1));

    }

    /**
     * insert 2 VO's verify 2 records were inserted call deleteAll verify no records are found
     * 
     * @throws Exception
     */
    public void testDeleteAll() throws Exception {
        Collection<ActivityDynamicDetail> inserts = new ArrayList<ActivityDynamicDetail>();
        inserts.add(voA1);
        inserts.add(voB1);
        dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, inserts);
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
        Collection<ActivityDynamicDetail> inserts = new ArrayList<ActivityDynamicDetail>();
        inserts.add(voA1);

        try {
            conn = datasource.getConnection();

            dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, inserts);
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
     * 1. verify that the record we are going to update does not exist in database. ( don't want to
     * update any unknown data ) 2. update records 3. compare all values of 1 of the records
     * 
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#updateMoneyType(com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType)}.
     */
    public final void testUpdate() throws Exception {

        Connection conn = null;
        PreparedStatement stmt = null;
        Collection<ActivityDynamicDetail> inserts = new ArrayList<ActivityDynamicDetail>();
        Collection<ActivityDynamicDetail> updates = new ArrayList<ActivityDynamicDetail>();

        inserts.add(voA1);
        inserts.add(voB1);
        updates.add(voA2);
        updates.add(voB2);

        try {
            dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, inserts);
            dao.update( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID_2, updates);
            verifyValues(voA2, createVoFromDb(voA2));
            verifyValues(voB2, createVoFromDb(voB2));

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
     * insert a record, then call select
     * 
     * @throws Exception
     */
    public void testSelect() throws Exception {

        Connection conn = datasource.getConnection();
        ;
        Statement stmt = null;

        List<ActivityDynamicDetail> selects = null;
        Collection<ActivityDynamicDetail> inserts = new ArrayList<ActivityDynamicDetail>();
        inserts.add(voA1);
        inserts.add(voB1);
        try {
            dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, inserts);
            selects = dao.select( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID);
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

    /**
     * 1. make sure that the record we are going to insert does not exist. 2. insertUpdate a record.
     * verify the values. 3.
     * 
     * 
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#insertMoneyType(com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType)}.
     */
    public final void testInsertUpdate() throws Exception {
        Connection conn = datasource.getConnection();
        PreparedStatement stmt = null;
        Collection<ActivityDynamicDetail> inserts = new ArrayList<ActivityDynamicDetail>();
        Collection<ActivityDynamicDetail> insertUpdates = new ArrayList<ActivityDynamicDetail>();

        inserts.add(voA1);
        insertUpdates.add(voA2);
        insertUpdates.add(voB2);

        try {
            assertFalse("record 1 did not get deleted", exists(voB1));

            dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, inserts);
            dao.insertUpdate( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID_2,
                    insertUpdates);
            verifyValues(voB2, createVoFromDb(voB2));
            verifyValues(voA2, createVoFromDb(voA2));

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

    public ActivityDynamicDetail createVoFromDb(ActivityDynamicDetail vo) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ActivityDynamicDetail returnVo = new ActivityDynamicDetail();
        int i = 2;
        try {
            conn = datasource.getConnection();
            stmt = conn.prepareStatement(SELECT);
            stmt.setInt(1, vo.getItemNumber());
            stmt.setString(2, vo.getSecondaryName());
            stmt.setInt(3, vo.getSecondaryNumber());
            stmt.setString(4, vo.getTypeCode());
            rs = stmt.executeQuery();
            if (rs.next()) {
                returnVo.setItemNumber(rs.getInt(i++));
                returnVo.setSecondaryName(rs.getString(i++));
                returnVo.setSecondaryNumber(rs.getInt(i++));
                returnVo.setLastUpdated(new Timestamp(rs.getDate(i++).getTime()));
                returnVo.setTypeCode(rs.getString(i++));
                returnVo.setValue(rs.getString(i++));
                returnVo.setLastUpdatedById(rs.getInt(i++));
            }
            return returnVo;
        } finally {
            BaseDatabaseDAO.close(stmt, conn);
        }
    }

    private boolean exists(ActivityDynamicDetail vo) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = datasource.getConnection();
            stmt = conn.prepareStatement(EXISTS);
            stmt.setInt(1, TEST_SUBMISSION_ID);
            stmt.setInt(2, vo.getItemNumber());
            stmt.setString(3, vo.getSecondaryName());
            stmt.setInt(4, vo.getSecondaryNumber());
            stmt.setString(5, vo.getTypeCode());

            rs = stmt.executeQuery();
            return rs.next();
        } finally {
            BaseDatabaseDAO.close(stmt, conn);
        }
    }

    public void verifyValues(ActivityDynamicDetail vo1, ActivityDynamicDetail vo2) throws Exception {
        assertEquals(vo1.getTypeCode(), vo2.getTypeCode());
        assertEquals(vo1.getValue(), vo2.getValue());
        assertEquals(vo1.getItemNumber(), vo2.getItemNumber());
        assertEquals(vo1.getLastUpdatedById(), vo2.getLastUpdatedById());

    }

}
