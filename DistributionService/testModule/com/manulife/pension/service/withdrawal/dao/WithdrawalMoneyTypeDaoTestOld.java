/*																				
 * WithdrawalMoneyTypeDaoTest.java,v 1.1 2006/09/11 18:53:26 Paul_Glenn Exp																				
 * WithdrawalMoneyTypeDaoTest.java,v																				
 * Revision 1.1  2006/09/11 18:53:26  Paul_Glenn																				
 * Fixed tests for refactored class.																				
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
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;					
/**																				
 * WithdrawalMoneyTypeDaoTest is the test case for the WithdrawalMoneyTypeDao object.																				
 * 																				
 * @author Paul_Glenn																				
 * @author glennpa																				
 * @version 1.1 2006/09/11 18:53:26																				
 */																				
public class WithdrawalMoneyTypeDaoTestOld extends BaseWithdrawalDependentTestCase {																				
    																				
    private String DELETE = "delete from stp100.WITHDRAWAL_MONEY_TYPE where submission_id = " + TEST_SUBMISSION_ID;																				
    private String SELECT = "select SUBMISSION_ID,MONEY_TYPE_ID,TOTAL_BAL_EXCL_LOAN_PBA_AMT,VESTING_PCT,VESTING_PCT_UPDATABLE_IND,WITHDRAWAL_AMT,WITHDRAWAL_PCT,CREATED_USER_PROFILE_ID,CREATED_TS,LAST_UPDATED_USER_PROFILE_ID,LAST_UPDATED_TS from stp100.withdrawal_money_type where submission_id = " + TEST_SUBMISSION_ID + " AND MONEY_TYPE_ID = ?";																				
    protected String SELECT_RECORD_COUNT = "select count(*) from stp100.withdrawal_money_type where submission_id = " + TEST_SUBMISSION_ID;																				
    private WithdrawalRequestMoneyType voA1 = null;																				
    private WithdrawalRequestMoneyType voA2 = null;																				
    private WithdrawalRequestMoneyType voA3 = null;																				
    private WithdrawalRequestMoneyType voB1 = null;																				
    private WithdrawalRequestMoneyType voB2 = null;																				
    private WithdrawalMoneyTypeDao dao = null;																				
    public WithdrawalRequestMoneyType REFERENCE_VO;																				
    					
    																				
    public WithdrawalMoneyTypeDaoTestOld(String arg0) {																				
		super(arg0);																		
		setupValueObjects();																		
	}																			
																				
    public static Test suite() {																				
        TestSuite suite = new TestSuite();																				
        suite.addTest(new WithdrawalMoneyTypeDaoTestOld("testInsert"));																				
        suite.addTest(new WithdrawalMoneyTypeDaoTestOld("testUpdate"));																				
        suite.addTest(new WithdrawalMoneyTypeDaoTestOld("testInsertUpdate"));																				
        return suite;																				
    }    																				
    					
    																				
    public void setUp() throws Exception {					
        super.setUp();					
        if (dao == null) {																				
            dao = new WithdrawalMoneyTypeDao();																				
        }																				
    }																				
    																				
    private void setupValueObjects() {																				
        voA1 = new WithdrawalRequestMoneyType();																				
        voA2 = new WithdrawalRequestMoneyType();																				
        voA3 = new WithdrawalRequestMoneyType();																				
        voB1 = new WithdrawalRequestMoneyType();																				
        voB2 = new WithdrawalRequestMoneyType();					
        REFERENCE_VO = voA1;																				
        																				
        voA1.setSubmissionId(TEST_SUBMISSION_ID);																				
        voA1.setMoneyTypeId("1");																				
        voA1																				
                .setTotalBalance(new BigDecimal("123456789.10").setScale(2,																				
                        BigDecimal.ROUND_FLOOR));																				
        voA1.setVestingPercentage(new BigDecimal("14.560").setScale(3, BigDecimal.ROUND_FLOOR));																				
        voA1.setVestingPercentageUpdateable(true);																				
        voA1.setWithdrawalAmount(new BigDecimal("14.56").setScale(2, BigDecimal.ROUND_FLOOR));																				
        voA1.setWithdrawalPercentage(new BigDecimal("14.99")																				
                .setScale(2, BigDecimal.ROUND_FLOOR));																				
        voA1.setLastUpdatedById(TEST_USER_PROFILE_ID);																				
        voA1.setCreatedById(TEST_USER_PROFILE_ID);					
        voA2.setSubmissionId(voA1.getSubmissionId());																				
        voA2.setMoneyTypeId(voA1.getMoneyTypeId());																				
        voA2																				
                .setTotalBalance(new BigDecimal("123456789.11").setScale(2,																				
                        BigDecimal.ROUND_FLOOR));																				
        voA2.setVestingPercentage(new BigDecimal("14.561").setScale(3, BigDecimal.ROUND_FLOOR));																				
        voA2.setVestingPercentageUpdateable(false);																				
        voA2.setWithdrawalAmount(new BigDecimal("14.57").setScale(2, BigDecimal.ROUND_FLOOR));																				
        voA2.setWithdrawalPercentage(new BigDecimal("15.00")																				
                .setScale(2, BigDecimal.ROUND_FLOOR));																				
        voA2.setLastUpdatedById(TEST_USER_PROFILE_ID_2);																				
        voA2.setCreatedById(voA1.getCreatedById());					
        voA3.setSubmissionId(voA1.getSubmissionId());																				
        voA3.setMoneyTypeId(voA1.getMoneyTypeId());																				
        voA3.setTotalBalance(voA1.getTotalBalance());																				
        voA3.setVestingPercentage(voA1.getVestingPercentage());																				
        voA3.setVestingPercentageUpdateable(voA1.getVestingPercentageUpdateable());																				
        voA3.setWithdrawalAmount(voA1.getWithdrawalAmount());																				
        voA3.setWithdrawalPercentage(voA1.getWithdrawalPercentage());																				
        voA3.setLastUpdatedById(TEST_USER_PROFILE_ID_2);																				
        voA3.setCreatedById(TEST_USER_PROFILE_ID_2);					
        voB1.setSubmissionId(TEST_SUBMISSION_ID);																				
        voB1.setMoneyTypeId("2");																				
        voB1																				
                .setTotalBalance(new BigDecimal("223456789.10").setScale(2,																				
                        BigDecimal.ROUND_FLOOR));																				
        voB1.setVestingPercentage(new BigDecimal("24.560").setScale(3, BigDecimal.ROUND_FLOOR));																				
        voB1.setVestingPercentageUpdateable(true);																				
        voB1.setWithdrawalAmount(new BigDecimal("24.56").setScale(2, BigDecimal.ROUND_FLOOR));																				
        voB1.setWithdrawalPercentage(new BigDecimal("24.99")																				
                .setScale(2, BigDecimal.ROUND_FLOOR));																				
        voB1.setLastUpdatedById(TEST_USER_PROFILE_ID);																				
        voB1.setCreatedById(TEST_USER_PROFILE_ID);					
        voB2.setSubmissionId(voB1.getSubmissionId());																				
        voB2.setMoneyTypeId(voB1.getMoneyTypeId());																				
        voB2																				
                .setTotalBalance(new BigDecimal("223456789.11").setScale(2,																				
                        BigDecimal.ROUND_FLOOR));																				
        voB2.setVestingPercentage(new BigDecimal("24.561").setScale(3, BigDecimal.ROUND_FLOOR));																				
        voB2.setVestingPercentageUpdateable(false);																				
        voB2.setWithdrawalAmount(new BigDecimal("24.57").setScale(2, BigDecimal.ROUND_FLOOR));																				
        voB2.setWithdrawalPercentage(new BigDecimal("25.00")																				
                .setScale(2, BigDecimal.ROUND_FLOOR));																				
        voB2.setLastUpdatedById(TEST_USER_PROFILE_ID_2);																				
        voB2.setCreatedById(voB1.getCreatedById());																				
    }        																				
    																				
    					
    /**																				
     * Insert a collection of 2 records.																				
     * then verify all of the fields of record 1, and verify the record count is 2.																				
     * 																				
     * {@link com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#insert(com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType)}.																				
     */																				
    public final void testInsert() throws Exception {																				
        																				
        					
        Connection conn = null;																				
        Statement stmt = null;																				
        																				
        Collection<WithdrawalRequestMoneyType> moneyTypes1  = new ArrayList<WithdrawalRequestMoneyType>();																				
        moneyTypes1.add(voA1);																				
        moneyTypes1.add(voB1);																				
        Collection<WithdrawalRequestMoneyType> moneyTypes2  = new ArrayList<WithdrawalRequestMoneyType>();																				
        moneyTypes2.add(voA3);					
        try {																				
            conn = datasource.getConnection();					
            dao.insert( TEST_SUBMISSION_ID, TEST_CONTRACT_ID,TEST_USER_PROFILE_ID, moneyTypes1);					
            verifyValues(voA1, createVoFromDb(voA1.getMoneyTypeId()));																				
            verifyRecordCount(SELECT_RECORD_COUNT, 2);																				
            dao.deleteAll( TEST_SUBMISSION_ID, TEST_CONTRACT_ID, TEST_USER_PROFILE_ID);																				
            BigDecimal oldVal = voA3.getWithdrawalPercentage();																				
            voA3.setWithdrawalPercentage(null);																				
            dao.insert( TEST_SUBMISSION_ID, TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, moneyTypes2);																				
            voA3.setWithdrawalPercentage(oldVal);					
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
     * 1. verify that the record we are going to update does not exist in database. ( don't want to update any unknown data )																				
     * 2. update  records																				
     * 3. compare all values of 1 of the records																				
     * 																				
     * Test method for																				
     * {@link com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#updateMoneyType(com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType)}.																				
     */																				
    public final void testUpdate() throws Exception {																				
        																				
        Connection conn = null;																				
        PreparedStatement stmt = null;																				
        Collection<WithdrawalRequestMoneyType> inserts = new ArrayList<WithdrawalRequestMoneyType>();																				
        Collection<WithdrawalRequestMoneyType> updates1 = new ArrayList<WithdrawalRequestMoneyType>();																				
        Collection<WithdrawalRequestMoneyType> updates2 = new ArrayList<WithdrawalRequestMoneyType>();					
        inserts.add(voA1);																				
        inserts.add(voB1);																				
        updates1.add(voA2);																				
        updates1.add(voB2);        																				
        updates2.add(voA2);					
        try {																				
            dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, inserts);					
            dao.update( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID_2, updates1);					
            verifyValues(voA2, createVoFromDb(voA2.getMoneyTypeId()));																				
            verifyValues(voB2, createVoFromDb(voB2.getMoneyTypeId()));																				
            BigDecimal oldVal = voA2.getWithdrawalPercentage();																				
            voA2.setWithdrawalPercentage(null);																				
            dao.update( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID_2, updates2);																				
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
     * 1. make sure that the record we are going to insert does not exist.																				
     * 2. insertUpdate a record. verify the values.																				
     * 3. 																				
     *  																				
     * 																				
     * Test method for																				
     * {@link com.manulife.pension.service.withdrawal.dao.WithdrawalMoneyTypeDao#insertMoneyType(com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType)}.																				
     */																				
    public final void testInsertUpdate() throws Exception {																				
        Connection conn = datasource.getConnection();																				
        PreparedStatement stmt = null;																				
        Collection<WithdrawalRequestMoneyType> inserts = new ArrayList<WithdrawalRequestMoneyType>();																				
        Collection<WithdrawalRequestMoneyType> updates = new ArrayList<WithdrawalRequestMoneyType>();					
        inserts.add(voB1);																				
        updates.add(voA3);																				
        updates.add(voB2);					
        																				
        try {																				
            assertFalse("record 1 did not get deleted", exists(voA3));																				
            dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, inserts);					
            dao.insertUpdate( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID_2, updates);					
            verifyValues(voA3, createVoFromDb(voA3.getMoneyTypeId()));					
            verifyValues(voB2, createVoFromDb(voB2.getMoneyTypeId()));																				
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
     * 1. insert 2 test records																				
     * 2. delete the test records																				
     * 3. verify records are deleted																				
     */																				
    public final void testDeleteAll() throws Exception {																				
        																				
        Connection conn = datasource.getConnection();																				
        PreparedStatement stmt = null;																				
        Collection<WithdrawalRequestMoneyType> vos = new ArrayList<WithdrawalRequestMoneyType>();																				
        vos.add(voA1);																				
        vos.add(voB1);																				
        																				
        try {																				
            dao.insert( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID, vos);					
            dao.deleteAll( TEST_SUBMISSION_ID,TEST_CONTRACT_ID, TEST_USER_PROFILE_ID);					
            verifyRecordCount(SELECT_RECORD_COUNT, 0);					
        } 																				
        																				
        finally {																				
            try {																				
                stmt = conn.prepareStatement(DELETE);																				
                stmt.executeUpdate();																				
            } catch (Exception e) { /* eat it */ }																				
            BaseDatabaseDAO.close(stmt, conn);																				
        }																				
        																				
        																				
    }    private WithdrawalRequestMoneyType createVoFromDb(String moneyTypeId) throws Exception {																				
        Connection conn = null;																				
        PreparedStatement stmt = null;																				
        ResultSet rs = null;																				
        int i = 1;																				
        WithdrawalRequestMoneyType returnVo = new WithdrawalRequestMoneyType();																				
        try {																				
            conn = datasource.getConnection();																				
            stmt = conn.prepareStatement(SELECT);																				
            stmt.setString(1, moneyTypeId);																				
            rs = stmt.executeQuery();																				
            if (rs.next()) {																				
                returnVo.setSubmissionId(new Integer(rs.getInt(i++)));																				
                returnVo.setMoneyTypeId(rs.getString(i++).trim());																				
                returnVo.setTotalBalance(rs.getBigDecimal(i++));																				
                returnVo.setVestingPercentage(rs.getBigDecimal(i++));																				
                returnVo.setVestingPercentageUpdateable(isTrue(rs.getString(i++)));																				
                returnVo.setWithdrawalAmount(rs.getBigDecimal(i++));																				
                returnVo.setWithdrawalPercentage(rs.getBigDecimal(i++));																				
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
    public void verifyValues(WithdrawalRequestMoneyType vo1, WithdrawalRequestMoneyType vo2) {																				
        assertEquals(vo1.getSubmissionId(),vo2.getSubmissionId());																				
        assertEquals(vo1.getMoneyTypeId().trim(),vo2.getMoneyTypeId().trim());																				
        assertEquals(vo1.getTotalBalance(),vo2.getTotalBalance());																				
        assertEquals(vo1.getVestingPercentage(),vo2.getVestingPercentage());																				
        assertEquals(vo1.getVestingPercentageUpdateable(),vo2.getVestingPercentageUpdateable());																				
        assertEquals(vo1.getWithdrawalAmount(),vo2.getWithdrawalAmount());																				
        assertEquals(vo1.getWithdrawalPercentage(),vo2.getWithdrawalPercentage());																				
        assertEquals(vo1.getCreatedById(),vo2.getCreatedById());																				
        assertEquals(vo1.getLastUpdatedById(),vo2.getLastUpdatedById());																				
    }																				
    private boolean exists(WithdrawalRequestMoneyType vo) throws Exception {																				
        Connection conn = null;																				
        PreparedStatement stmt = null;																				
        ResultSet rs = null;																				
        try {																				
            conn = datasource.getConnection();																				
            stmt = conn.prepareStatement(SELECT);																				
            stmt.setString(1, vo.getMoneyTypeId());																				
            rs = stmt.executeQuery();																				
            return rs.next();																				
        }																				
        finally {																				
            BaseDatabaseDAO.close(stmt, conn);																				
        }    																				
    }																				
    public  Collection<WithdrawalRequestMoneyType> getVOs(int count)  {					
        Collection<WithdrawalRequestMoneyType> vos  = new ArrayList<WithdrawalRequestMoneyType>();																				
        vos.add(voA1);																				
        if ( count > 1 ) {																				
            vos.add(voB1);																				
        }																				
        return vos;																				
    }    																				
}																				
	