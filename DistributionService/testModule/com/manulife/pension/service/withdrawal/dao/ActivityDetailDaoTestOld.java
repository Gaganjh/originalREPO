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
import com.manulife.pension.service.distribution.dao.ActivityDetailDao;
import com.manulife.pension.service.distribution.dao.NoteDao;
import com.manulife.pension.service.distribution.valueobject.ActivityDetail;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivityDetail;

/**
 * WithdrawalDaoTest is the test class for the {@link NoteDao} class.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/06 19:55:29
 */
public class ActivityDetailDaoTestOld extends BaseWithdrawalDependentTestCase {

    private WithdrawalActivityDetail voA1 = null;

    private WithdrawalActivityDetail voA2 = null;

    private WithdrawalActivityDetail voB1 = null;

    private WithdrawalActivityDetail voB2 = null;

    protected String SELECT_RECORD_COUNT = "select count(*) from stp100.ACTIVITY_DETAIL where submission_id = "
            + TEST_SUBMISSION_ID;

    private String SELECT = "select SUBMISSION_ID,ITEM_NO,LAST_UPDATED_TS,ACTIVITY_TYPE_CODE,ENTERED_VALUE,LAST_UPDATED_USER_PROFILE_ID from stp100.ACTIVITY_detail where submission_id = "
            + TEST_SUBMISSION_ID + " and item_no = ?";

    private String DELETE = "delete from stp100.ACTIVITY_detail where submission_id = "
            + TEST_SUBMISSION_ID;

    private String EXISTS = "select * from stp100.ACTIVITY_detail where submission_id = ? and item_no = ? and ACTIVITY_TYPE_CODE = ?";

    ActivityDetailDao dao = null;

    public ActivityDetailDaoTestOld(String arg0) {
        super(arg0);
        setupValueObjects();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new ActivityDetailDaoTestOld("testDelete"));
        suite.addTest(new ActivityDetailDaoTestOld("testDeleteAll"));
        suite.addTest(new ActivityDetailDaoTestOld("testInsert"));
        suite.addTest(new ActivityDetailDaoTestOld("testInsertUpdate"));
        
        // Error during the Execution of Store Proc
//        suite.addTest(new ActivityDetailDaoTest("testSelect"));
        suite.addTest(new ActivityDetailDaoTestOld("testUpdate"));
        return suite;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        if (dao == null) {
            dao = new ActivityDetailDao();
        }
    }

    private void setupValueObjects() {
        voA1 = new WithdrawalActivityDetail();
        voA2 = new WithdrawalActivityDetail();
        voB1 = new WithdrawalActivityDetail();
        voB2 = new WithdrawalActivityDetail();

        voA1.setActionCode("A");
        voA1.setItemNumber(1);
        voA1.setLastUpdatedById(TEST_USER_PROFILE_ID);
        voA1.setLastUpdated(new Timestamp(new Date().getTime()));
        voA1.setValue("myVal1");

        voA2.setActionCode(voA1.getActionCode());
        voA2.setItemNumber(voA1.getItemNumber());
        voA2.setLastUpdatedById(TEST_USER_PROFILE_ID_2);
        voA2.setLastUpdated(new Timestamp(new Date().getTime()));
        voA2.setValue("myVal2");

        voB1.setActionCode("B");
        voB1.setItemNumber(2);
        voB1.setLastUpdatedById(TEST_USER_PROFILE_ID);
        voB1.setLastUpdated(new Timestamp(new Date().getTime()));
        voB1.setValue("myValB2");

        voB2.setActionCode(voB1.getActionCode());
        voB2.setItemNumber(voB1.getItemNumber());
        voB2.setLastUpdatedById(TEST_USER_PROFILE_ID_2);
        voB2.setLastUpdated(new Timestamp(new Date().getTime()));
        voB2.setValue("myValB3");

    }

    /**
     * insert 1 VO's verify 1 records were inserted call delete verify no records are found
     * 
     * @throws Exception
     */
    public void testDelete() throws Exception {
        Collection<WithdrawalActivityDetail> inserts = new ArrayList<WithdrawalActivityDetail>();
        Collection<WithdrawalActivityDetail> deletes = new ArrayList<WithdrawalActivityDetail>();
        deletes.add(voA1);

        inserts.add(voA1);
        inserts.add(voB1);
        dao.insert( TEST_SUBMISSION_ID, TEST_CONTRACT_ID,TEST_USER_PROFILE_ID, inserts);

        verifyRecordCount(SELECT_RECORD_COUNT, 2);

        dao.delete( TEST_SUBMISSION_ID, TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, deletes);

        verifyRecordCount(SELECT_RECORD_COUNT, 1);

        verifyValues(voB1, createVoFromDb(voB1));

    }

    /**
     * insert 2 VO's verify 2 records were inserted call deleteAll verify no records are found
     * 
     * @throws Exception
     */
    public void testDeleteAll() throws Exception {
        Collection<WithdrawalActivityDetail> inserts = new ArrayList<WithdrawalActivityDetail>();

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
        Collection<WithdrawalActivityDetail> inserts = new ArrayList<WithdrawalActivityDetail>();
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
        Collection<WithdrawalActivityDetail> inserts = new ArrayList<WithdrawalActivityDetail>();
        Collection<WithdrawalActivityDetail> updates = new ArrayList<WithdrawalActivityDetail>();

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
        Collection<WithdrawalActivityDetail> inserts = new ArrayList<WithdrawalActivityDetail>();
        Collection<WithdrawalActivityDetail> insertUpdates = new ArrayList<WithdrawalActivityDetail>();

        inserts.add(voA1);
        insertUpdates.add(voA2);
        insertUpdates.add(voB2);

        try {
            assertFalse("record 1 did not get deleted", exists(voB1));

            dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, inserts);

            dao.insertUpdate( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID_2,
                    insertUpdates, WithdrawalActivityDetail.class);
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

    /**
     * insert a record, then call select
     * 
     * @throws Exception
     */
    public void testSelect() throws Exception {

        Connection conn = datasource.getConnection();
        Statement stmt = null;

        List<WithdrawalActivityDetail> selects = null;
        Collection<WithdrawalActivityDetail> inserts = new ArrayList<WithdrawalActivityDetail>();
        inserts.add(voA1);
        inserts.add(voB1);
        try {
            dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, inserts);
            selects = (List<WithdrawalActivityDetail>)dao.select( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, WithdrawalActivityDetail.class);
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

    public WithdrawalActivityDetail createVoFromDb(ActivityDetail vo) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        WithdrawalActivityDetail returnVo = new WithdrawalActivityDetail();
        int i = 2;
        try {
            conn = datasource.getConnection();
            stmt = conn.prepareStatement(SELECT);
            stmt.setInt(1, vo.getItemNumber());
            rs = stmt.executeQuery();
            if (rs.next()) {
                returnVo.setItemNumber(rs.getInt(i++));
                returnVo.setLastUpdated(new Timestamp(rs.getDate(i++).getTime()));
                returnVo.setActionCode(rs.getString(i++));
                returnVo.setValue(rs.getString(i++));
                returnVo.setLastUpdatedById(rs.getInt(i++));
            }
            return returnVo;
        } finally {
            BaseDatabaseDAO.close(stmt, conn);
        }
    }

    private boolean exists(WithdrawalActivityDetail vo) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = datasource.getConnection();
            stmt = conn.prepareStatement(EXISTS);
            stmt.setInt(1, TEST_SUBMISSION_ID);
            stmt.setInt(2, vo.getItemNumber());
            stmt.setString(3, vo.getActionCode());

            rs = stmt.executeQuery();
            return rs.next();
        } finally {
            BaseDatabaseDAO.close(stmt, conn);
        }
    }

    public void verifyValues(WithdrawalActivityDetail vo1, WithdrawalActivityDetail vo2) throws Exception {
        assertEquals(vo1.getActionCode(), vo2.getActionCode());
        assertEquals(vo1.getValue(), vo2.getValue());
        assertEquals(vo1.getItemNumber(), vo2.getItemNumber());
        assertEquals(vo1.getLastUpdatedById(), vo2.getLastUpdatedById());

    }

    public void insertReference(Integer count) throws Exception {

        if (dao == null) {
            dao = new ActivityDetailDao();
        }
        Collection<WithdrawalActivityDetail> inserts = new ArrayList<WithdrawalActivityDetail>();
        inserts.add(voA1);
        inserts.add(voB1);
        dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, inserts);
    }
}
