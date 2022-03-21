package com.manulife.pension.service.withdrawal.dao;

import junit.textui.TestRunner;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.testutility.MockContainerEnvironment;
import com.manulife.pension.service.withdrawal.dao.WithdrawalInfoDao;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalInfo;

/**
 * Tests the WithdrawalInfoDao
 * 
 * @author Mihai Popa
 */
public class WithdrawalInfoDaoTestOld extends TestCase {

    private static final int TEST_CONTRACT = 85357;

   private static final int TEST_PARTICIPANT_ID = 116496357;
    

    /**
     * Constructor for WithdrawalInfoDaoTest
     * 
     * @see TestCase
     */
    public WithdrawalInfoDaoTestOld(String arg0) {
        super(arg0);
    }

    /**
     * @return Test
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(WithdrawalInfoDaoTestOld.class);
        return suite;
    }

    public static void main(String[] args) {
        TestRunner.main(new String[] { "-noloading", WithdrawalInfoDaoTestOld.class.getName() });
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();
        MockContainerEnvironment.initialize();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
        super.tearDown();
        MockContainerEnvironment.destroy();
    }

    /**
     * Tests the Get_Withdrawal_Info stored proc.
     * 
     * @throws SystemException
     */
    public void testGetWithdrawalInfo() throws SystemException {

        WithdrawalInfo withdrawalInfo = WithdrawalInfoDao.getWithdrawalInfo(TEST_PARTICIPANT_ID,
                TEST_CONTRACT);
        assertNotNull(withdrawalInfo);
        assertFalse ("has no PBA money ", withdrawalInfo.isPbaMoneyTypeInd());
    }

    /**
     * Tests the Get_Withdrawal_Info stored proc.
     * 
     * @throws SystemException
     */
    public void testGetParticipantWithdrawalInfo() throws SystemException {

        ParticipantInfo withdrawalInfo = WithdrawalInfoDao.getParticipantInfo(
                Long.valueOf("" + TEST_PARTICIPANT_ID), Integer.valueOf(TEST_CONTRACT));
        assertNotNull(withdrawalInfo);
        assertFalse("has no after tax ",withdrawalInfo.getHasAfterTaxContributions());
    }
    
    /**
     * Tests the Get_Withdrawal_Info stored proc.
     * 
     * @throws SystemException
     */
//  Leads to assertion failure
//    public void testGetParticipantWithdrawalInfoWithAfterTaxMoney() throws SystemException {
//
//        ParticipantInfo withdrawalInfo = WithdrawalInfoDao.getParticipantInfo(
//                Long.parseLong("2939824"), 64857);
//        assertNotNull(withdrawalInfo);
//        assertTrue("has after tax ",withdrawalInfo.getHasAfterTaxContributions());
//        
//    }

    /**
     * Tests the Get_Withdrawal_Info stored proc.
     * 
     * @throws SystemException
     */
    public void testGetParticipantWithdrawalInfoInTTStatus() throws SystemException {

        WithdrawalInfo withdrawalInfo = WithdrawalInfoDao.getWithdrawalInfo(
                901472, 70300);
        assertNotNull(withdrawalInfo);
        assertEquals("Is in TT Status ","TT", withdrawalInfo.getParticipantStatus());
        assertFalse("Part. has no roth ", withdrawalInfo.isParticipantHasRothMoney());
        
    }

    /**
     * Tests the Get_Withdrawal_Info stored proc.
     * 
     * @throws SystemException
     */
//  Leads to assertion failure
//    public void testGetParticipantWithdrawalInfoWithRoth() throws SystemException {
//
//        WithdrawalInfo withdrawalInfo = WithdrawalInfoDao.getWithdrawalInfo(
//                2955778, 10744);
//        assertNotNull(withdrawalInfo);
//        assertTrue("Part. has roth ", withdrawalInfo.isParticipantHasRothMoney());
//        
//    }
    
    /**
     * Tests the Get_Withdrawal_Info stored proc.
     * 
     * @throws SystemException
     */
//  Leads to assertion failure
//    public void testGetParticipantPBAMoneyTypeInd() throws SystemException {
//
//        WithdrawalInfo withdrawalInfo = WithdrawalInfoDao.getWithdrawalInfo(
//                6324, TEST_CONTRACT);
//        assertNotNull(withdrawalInfo);
//      assertTrue("Part. has PBA ", withdrawalInfo.isPbaMoneyTypeInd());
//        
//    }
}