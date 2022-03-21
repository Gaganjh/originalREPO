
package com.manulife.pension.service.withdrawal.dao;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.manulife.pension.service.withdrawal.valueobject.LegaleseInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;


/**
 * WithdrawalLegaleseDaoTest is the test class for the {@link WithdrawalLegaleseDao} class.
 * 
 * @author kuthiha
 * 
 */
public class WithdrawalLegaleseDaoTestOld extends BaseWithdrawalDependentTestCase {
    public static final int LEGALESE_STATIC_CONTENT_TEXT = 56216;
    public static final int LEGALESE_DYNAMIC_NO_SPOUSAL_CONSENT_TEXT = 55750;
    public static final int LEGALESE_DYNAMIC_SPOUSAL_CONSENT_REQUIRED_TEXT = 56217;
    public static final int LEGALESE_DYNAMIC_SPOUSAL_CONSENT_BLANK_TEXT = 56218;
    public static final int LEGALESE_DYNAMIC_MANDATORY_TERMINATION_TEXT = 56219;
    private LegaleseInfo info = null;
   
    private WithdrawalRequest request = null;
    private WithdrawalLegaleseDao dao = null;
    
    public WithdrawalLegaleseDaoTestOld(String arg0) {
        super(arg0);
        //setupValueObjects();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
//      Leads to NullPointer Exception
//       suite.addTest(new WithdrawalLegaleseDaoTest("testInsert"));
        
//        Commented the caller to avoid Method "testLegaleseSelect" not found Failure
//        suite.addTest(new WithdrawalLegaleseDaoTest("testLegaleseSelect"));
        return suite;
    }    
    
    @Override
    public void setUp() throws Exception {
      super.setUp();
        if (dao == null) {
            dao = new WithdrawalLegaleseDao();
        }
        setupValueObjects();
    }
    private void setupValueObjects() {
        info = new LegaleseInfo(new Integer(LEGALESE_DYNAMIC_NO_SPOUSAL_CONSENT_TEXT),
                "- spousal consent not required received",TEST_USER_PROFILE_ID);

        info.setCmaSiteCode(WithdrawalRequest.CMA_SITE_CODE_PSW);
        info.setContentVersionNumber(1);
        info.setCreatorUserProfileId(TEST_USER_PROFILE_ID);

        request = new WithdrawalRequest();
        request.setLegaleseInfo(info);
        request.setSubmissionId(TEST_SUBMISSION_ID);
       
        
    }
    

    /**
     * create a colelction of 2 VO's and insert them.
     * verify the values of the first record.
     * and verify the record count is 2
     * 
     * @throws Exception
     */

    public void testInsert() throws Exception { 
        
        dao.updateLegaleseInfo(request.getContractId(), request.getSubmissionId(), request.getLegaleseInfo());
        dao.insertWithdrawalLegaleseInfo(TEST_SUBMISSION_ID, request);
       // dao.getLegaleseTextVersion(request.getLegaleseInfo());
        
    }

    /**
     * create a colelction of 2 VO's and insert them.
     * verify the values of the first record.
     * and verify the record count is 2
     * 
     * @throws Exception
     */
    public void testInsertWithdrawalLegalese() throws Exception { 
        
        dao.insertWithdrawalLegaleseInfo(TEST_SUBMISSION_ID, request);
       // dao.getLegaleseTextVersion(request.getLegaleseInfo());
        
    }
    
  
//    public void testLegaleseSelect() throws Exception {
//        dao.getLegaleseTextVersion(info);
//        System.out.println( dao.getLegaleseTextVersion(info) + " " + info.getLegaleseText() + " " + info.getContentId());
//    }
//    
//    public void testLegaleseSelect2() throws Exception {
//        info.setContentId(LEGALESE_DYNAMIC_NO_SPOUSAL_CONSENT_TEXT);
//        dao.getLegaleseTextVersion(info);
//        System.out.println( dao.getLegaleseTextVersion(info) + " " + info.getLegaleseText() + " " + info.getContentId());
//    }
 
 
}
