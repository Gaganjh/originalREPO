/*
//use these sql statments to clean out database if an error occurs in a test
delete from stp100.ACTIVITY_DETAIL where submission_id=111222
delete from stp100.ACTIVITY_DYNAMIC_DETAIL where submission_id=111222
delete from stp100.ACTIVITY_SUMMARY where submission_id=111222
delete from stp100.DECLARATION where submission_id=111222
delete from stp100.DISTRIBUTION_ADDRESS where submission_id=111222
delete from stp100.FEE where submission_id=111222
delete from stp100.NOTE where submission_id=111222
delete from stp100.PAYEE_PAYMENT_INSTRUCTION where submission_id=111222
delete from stp100.PAYEE where submission_id=111222
delete from stp100.RECIPIENT where submission_id=111222
delete from stp100.WITHDRAWAL_LOAN_DETAIL where submission_id=111222
delete from stp100.WITHDRAWAL_MONEY_TYPE where submission_id=111222
delete from  STP100.WITHDRAWAL_LEGALESE where submission_id =111222
delete from stp100.SUBMISSION_WITHDRAWAL where submission_id=111222
delete from stp100.SUBMISSION_CASE where submission_id=111222
delete from stp100.SUBMISSION where submission_id=111222

use these one to delete all withdrawal requests
delete from stp100.ACTIVITY_DYNAMIC_DETAIL  where submission_id in ( select submission_id from stp100.submission_withdrawal )
delete from stp100.ACTIVITY_DETAIL where submission_id in ( select submission_id from stp100.submission_withdrawal )
delete from stp100.ACTIVITY_SUMMARY  where submission_id in ( select submission_id from stp100.submission_withdrawal )
delete from stp100.DECLARATION  where submission_id in ( select submission_id from stp100.submission_withdrawal )
delete from stp100.DISTRIBUTION_ADDRESS  where submission_id in ( select submission_id from stp100.submission_withdrawal )
delete from stp100.FEE  where submission_id in ( select submission_id from stp100.submission_withdrawal )
delete from stp100.NOTE  where submission_id in ( select submission_id from stp100.submission_withdrawal )
delete from stp100.PAYEE_PAYMENT_INSTRUCTION  where submission_id in ( select submission_id from stp100.submission_withdrawal )
delete from stp100.PAYEE  where submission_id in ( select submission_id from stp100.submission_withdrawal )
delete from stp100.RECIPIENT  where submission_id in ( select submission_id from stp100.submission_withdrawal )
delete from stp100.WITHDRAWAL_LOAN_DETAIL  where submission_id in ( select submission_id from stp100.submission_withdrawal )
delete from stp100.WITHDRAWAL_MONEY_TYPE  where submission_id in ( select submission_id from stp100.submission_withdrawal )
delete from  STP100.WITHDRAWAL_LEGALESE where submission_id in (  select submission_id from stp100.submission_withdrawal )
delete from stp100.SUBMISSION_WITHDRAWAL where submission_id in (select submission_id from stp100.submission_case where submission_case_type_code = 'W')
delete from stp100.SUBMISSION_CASE where submission_case_type_code = 'W'
delete from stp100.SUBMISSION where submission_id not in (select submission_id from stp100.submission_case)

 */
package com.manulife.pension.service.withdrawal.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.lang.ObjectUtils;

import com.intware.dao.jdbc.SQLInsertHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.dao.DeclarationDao;
import com.manulife.pension.service.distribution.dao.DeclarationDaoTestOld;
import com.manulife.pension.service.distribution.dao.DistributionAddressDaoTestOld;
import com.manulife.pension.service.distribution.dao.DistributionJUnitConstants;
import com.manulife.pension.service.distribution.dao.FeeDaoTestOld;
import com.manulife.pension.service.distribution.dao.NoteDaoTestOld;
import com.manulife.pension.service.distribution.dao.PayeeDaoTestOld;
import com.manulife.pension.service.distribution.dao.PaymentInstructionDaoTestOld;
import com.manulife.pension.service.distribution.dao.RecipientDaoTestOld;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.testutility.MockContainerEnvironmentTestCase;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.LegaleseInfo;
import com.manulife.pension.service.withdrawal.valueobject.PayeePaymentInstruction;
import com.manulife.pension.service.withdrawal.valueobject.PendingReviewApproveWithdrawalCount;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * DeclarationDaoTest is the test class for the {@link DeclarationDao} class.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/07 18:50:35
 */
public class WithdrawalDaoTestOld extends MockContainerEnvironmentTestCase {
   

    private static final int TEST_CONTRACT = 85357;
    private static final int TEST_PARTICIPANT_ID = 116496357;
    
    private static final String SELECT_SUBMISSION = new String("select SUBMISSION_ID,SUBMISSION_TS,FILE_NAME,MAP_NAME,INPUT_LOCATION_NAME,USER_ID,CREATED_USER_ID,CREATED_TS,LAST_UPDATED_USER_ID,LAST_UPDATED_TS,PAYMENT_INFO_ONLY_IND from stp100.submission where submission_id = ?");
    private static final String SELECT_SUBMISSION_CASE = new String("select SUBMISSION_ID,CONTRACT_ID,SUBMISSION_CASE_TYPE_CODE,SYNTAX_ERROR_IND,PROCESSED_TS,PROCESS_STATUS_CODE,CREATED_USER_ID,CREATED_TS,LAST_UPDATED_USER_ID,LAST_UPDATED_TS,LAST_LOCKED_BY_USER_ID,LAST_LOCKED_TS from stp100.submission_case where submission_id = ?");
    private static final String SELECT_SUBMISSION_WITHDRAWAL = new String("select SUBMISSION_ID,CONTRACT_ID,SUBMISSION_CASE_TYPE_CODE,CREATED_BY_ROLE_CODE,PROFILE_ID,PARTICIPANT_ID,RESIDENCE_STATE_CODE,BIRTH_DATE,WITHDRAWAL_REASON_CODE,WITHDRAWAL_REASON_DETAIL_CODE,WITHDRAWAL_REASON_EXPLANATION,PARTICIPANT_LEAVING_PLAN_IND,PAYMENT_TO_CODE,REQUEST_DATE,EXPIRATION_DATE,EVENT_DATE,LAST_CONTRIB_APPLIC_DATE,LATEST_PROCESSED_CONTRIB_DATE,WITHDRAWAL_AMOUNT_TYPE_CODE,WITHDRAWAL_AMOUNT,UNVESTED_AMOUNT_OPTION_CODE,IRS_DIST_CODE_LOAN_CLOSURE,LOAN_OPTION_CODE,IRA_SERVICE_PROVIDER_CODE,APPROVED_TS,LOAN_1099R_NAME,VESTING_CALLED_IND,VESTING_OVERWRITE_IND,EFFECTIVE_DATE,PART_WITH_PBA_MONEY_IND,CREATED_USER_PROFILE_ID,CREATED_TS,LAST_UPDATED_USER_PROFILE_ID,LAST_UPDATED_TS, AT_RISK_IND from stp100.submission_withdrawal where submission_id = ?");
    private static final String SUBMISSION_DATA_SOURCE_NAME = "jdbc/customerService";
    private WithdrawalRequest voA1 = null;
    private WithdrawalRequest voA2 = null;
    

    private WithdrawalDao dao = null;
    private DataSource datasource = null;
    private WithdrawalLegaleseDao legaleseDao = null;

    public WithdrawalDaoTestOld(String arg0) {
        super(arg0);

    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        suite.addTest(new WithdrawalDaoTestOld("testGetWithdrawalRequests"));
        suite.addTest(new WithdrawalDaoTestOld("testDelete"));
        suite.addTest(new WithdrawalDaoTestOld("testInsert"));
        suite.addTest(new WithdrawalDaoTestOld("testUpdate"));
        suite.addTest(new WithdrawalDaoTestOld("testRead"));
        suite.addTest(new WithdrawalDaoTestOld("testPendingReviewApproveCount"));
        suite.addTest(new WithdrawalDaoTestOld("testGetWithdrawalsByStatus"));
        suite.addTest(new WithdrawalDaoTestOld("testSetExpirationDate"));
        suite.addTest(new WithdrawalDaoTestOld("testGetFees"));
        suite.addTest(new WithdrawalDaoTestOld("testGetAgreedLegaleseText"));     

        //        Method implementation not found
//        suite.addTest(new WithdrawalDaoTest("testSetExpirationDateForParticipantIniated"));
        return suite;
    }    
    @Override
	public void setUp() throws Exception {

        super.setUp();

        if (dao == null) {
            dao = new WithdrawalDao();
        }
        if (legaleseDao == null) {
            legaleseDao = new WithdrawalLegaleseDao();
        } 
        if ( datasource == null ) {
            datasource = (DataSource) new InitialContext().lookup(SUBMISSION_DATA_SOURCE_NAME);
        }
        if ( voA1 == null ) {
            setupValueObjects();
        }

    }
    private void setupValueObjects() {
        voA1 = new WithdrawalRequest();
        voA2 = new WithdrawalRequest();
        
        voA1.setSubmissionId(null);
        voA1.setContractId(DistributionJUnitConstants.TEST_CONTRACT_ID);
        voA1.setUserRoleCode("PS");
        voA1.setEmployeeProfileId(22222);
        voA1.setParticipantId(3333);
        voA1.setParticipantStateOfResidence("AZ");
        voA1.setBirthDate(new Date(111111));
        voA1.setReasonCode("22");
        voA1.setReasonDetailCode("33");
        voA1.setReasonDescription("cause i want to, thats why");
        voA1.setParticipantLeavingPlanInd(false);
        voA1.setPaymentTo("ME");
        voA1.setRequestDate(new Date(111222));
        voA1.setExpirationDate(new Date(111333));
        voA1.setTerminationDate(new Date(111444));
        voA1.setRetirementDate(new Date(222444));
        voA1.setDisabilityDate(new Date(111555));
        voA1.setDeathDate(new Date(111666));
        voA1.setFinalContributionDate(new Date(111777));
        voA1.setMostRecentPriorContributionDate(new Date(111888));
        voA1.setAmountTypeCode("44");
        voA1.setWithdrawalAmount(new BigDecimal(123434).setScale(2, BigDecimal.ROUND_FLOOR));
        voA1.setUnvestedAmountOptionCode("55");
        voA1.setIrsDistributionCodeLoanClosure("666");
        voA1.setLoanOption("77");
        voA1.setIraServiceProviderCode("8");
        voA1.setApprovedTimestamp(new Timestamp(111999));
        voA1.setLoan1099RName("a big long name");
        voA1.setVestingCalledInd(false);
        voA1.setVestingOverwriteInd(true);
        voA1.setExpectedProcessingDate(new Date(222000));
        voA1.setPartWithPbaMoneyInd(true);
        voA1.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());
        voA1.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
        voA1.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
        LegaleseInfo legaleseInfo = new LegaleseInfo(new Integer(55750),"This is a test - test ", new Integer(111111));
        legaleseInfo.setContentVersionNumber(1);
        legaleseInfo.setCmaSiteCode(WithdrawalRequest.CMA_SITE_CODE_PSW);
        voA1.setLegaleseInfo(legaleseInfo);
        voA1.setRequestRiskIndicator(true);

        

        voA2.setSubmissionId(null);
        voA2.setContractId(DistributionJUnitConstants.TEST_CONTRACT_ID);
        voA2.setUserRoleCode("PS");
        voA2.setEmployeeProfileId(voA1.getEmployeeProfileId());
        voA2.setParticipantId(voA1.getParticipantId());
        voA2.setParticipantStateOfResidence("NY");
        voA2.setBirthDate(new Date(111112));
        voA2.setReasonCode("23");
        voA2.setReasonDetailCode("34");
        voA2.setReasonDescription("cause i want to, thats why2");
        voA2.setParticipantLeavingPlanInd(true);
        voA2.setPaymentTo("YU");
        voA2.setRequestDate(new Date(111223));
        voA2.setExpirationDate(new Date(111334));
        voA2.setTerminationDate(new Date(111445));
        voA2.setRetirementDate(new Date(222445));
        voA2.setDisabilityDate(new Date(111556));
        voA2.setDeathDate(new Date(111667));
        voA2.setFinalContributionDate(new Date(111778));
        voA2.setMostRecentPriorContributionDate(new Date(111889));
        voA2.setAmountTypeCode("45");
        voA2.setWithdrawalAmount(new BigDecimal(123435).setScale(2, BigDecimal.ROUND_FLOOR));
        voA2.setUnvestedAmountOptionCode("56");
        voA2.setIrsDistributionCodeLoanClosure("667");
        voA2.setLoanOption("78");
        voA2.setIraServiceProviderCode("9");
        voA2.setApprovedTimestamp(new Timestamp(111990));
        voA2.setLoan1099RName("a big long name2");
        voA2.setVestingCalledInd(true);
        voA2.setVestingOverwriteInd(false);
        voA2.setExpectedProcessingDate(new Date(222001));
        voA2.setPartWithPbaMoneyInd(false);
        voA2.setStatusCode("B");
        voA2.setLastUpdatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID_2);
        voA2.setCreatedById(DistributionJUnitConstants.TEST_USER_PROFILE_ID);
        legaleseInfo = new LegaleseInfo(new Integer(44444),"This is a test also", new Integer(543543));
        legaleseInfo.setContentVersionNumber(1);
        legaleseInfo.setCmaSiteCode(WithdrawalRequest.CMA_SITE_CODE_PSW);
        voA2.setLegaleseInfo(legaleseInfo);
        voA1.setRequestRiskIndicator(false);
    }

    /**
     * Tests the getWithdrawalrequests method.
     * @throws Exception
     */
    public void testGetWithdrawalRequests() throws Exception {
        
        Collection withdrawalRequests = new WithdrawalDao().getWithdrawalRequests(TEST_PARTICIPANT_ID, TEST_CONTRACT);
        assertNotNull(withdrawalRequests);
    }

    
    /**
     * 1. Insert a test withdrawal
     * 2. delete the withdrawal
     * 3. verify it was deleted.
     * @throws Exception
     */
    public void testDelete() throws Exception {

        System.out.println("testDelete:begin");
        
        System.out.print("   Inserting reference record...");
        insertReference(1);
        System.out.println("done.");
        
        System.out.print("   Calling delete...");
        dao.delete(DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_SUBMISSION_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);
        System.out.println("done.");
        
        System.out.print("   Verifying record was deleted...");
        assertTrue("record was not deleted",
                   !exists(DistributionJUnitConstants.TEST_SUBMISSION_ID,SELECT_SUBMISSION)
                && !exists(DistributionJUnitConstants.TEST_SUBMISSION_ID,SELECT_SUBMISSION_CASE)
                && !exists(DistributionJUnitConstants.TEST_SUBMISSION_ID,SELECT_SUBMISSION_WITHDRAWAL));
        System.out.println("done.");
        
        System.out.println("testDelete:end");
    }
    
    
    /**
     * @throws Exception
     */
    public void testInsert() throws Exception {
        System.out.println("testInsert:begin");
        Integer submissionId = null;
        
        Collection<String> reasonCodes = new ArrayList<String>();
        reasonCodes.add(WithdrawalReason.TERMINATION);
        reasonCodes.add(WithdrawalReason.RETIREMENT);
        reasonCodes.add(WithdrawalReason.DISABILITY);
        reasonCodes.add(WithdrawalReason.DEATH);
        
        for (String reasonCode : reasonCodes) {

            System.out
                    .print("   Inserting reference record using resonCode " + reasonCode + " ...");
            voA1.setReasonCode(reasonCode);
            submissionId = dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1, DistributionJUnitConstants.TEST_TIMESTAMP_1);
            System.out.println("done.");

            System.out.print("   Verifying Values...");
            voA1.setSubmissionId(submissionId);
            verifyValues(voA1, createVoFromDb(submissionId));
            System.out.println("done.");

            System.out.print("   Deleting reference record...");
            dao.delete(DistributionJUnitConstants.TEST_CONTRACT_ID, submissionId, DistributionJUnitConstants.TEST_USER_PROFILE_ID);
            System.out.println("done.");

        }
        
        //this tests that the parameter types length is equal
        //to the parameters that i pass Intware handler.
        System.out.print("   Inserting a null value...");
        String oldVal = voA1.getReasonDetailCode();
        voA1.setReasonDetailCode(null);
        submissionId = dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1, DistributionJUnitConstants.TEST_TIMESTAMP_1);
        voA1.setReasonDetailCode(oldVal);
        System.out.println("done.");
        
        
        System.out.println("testInsert:end");
    }
    
    public void testUpdate() throws Exception {
        System.out.println("testUpdate:begin");
        Integer submissionId = null;

        //check updating keeping withdrawal reason constant.
        System.out.print("   Inserting reference record using resonCode = TE ...");
        voA1.setReasonCode(WithdrawalReason.TERMINATION);
        submissionId = dao.insert( DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1, DistributionJUnitConstants.TEST_TIMESTAMP_1);
        System.out.println("done.");
        
        System.out.print("   Updating reference record using resonCode = TE ...");
        voA2.setReasonCode(WithdrawalReason.TERMINATION);
        dao.update(DistributionJUnitConstants.TEST_CONTRACT_ID, submissionId,
                DistributionJUnitConstants.TEST_USER_PROFILE_ID_2, voA2);
        System.out.println("done.");

        System.out.print("   Verifying Values...");
        voA2.setSubmissionId(submissionId);
        verifyValues(voA2, createVoFromDb(submissionId));
        System.out.println("done.");

        System.out.print("   Deleting reference record...");
        dao.delete(DistributionJUnitConstants.TEST_CONTRACT_ID, submissionId, DistributionJUnitConstants.TEST_USER_PROFILE_ID);
        System.out.println("done.");
        
        //check upating while changeing withdrawal reason
        //and also set a value to null to check that parameterTypes.length
        //is equal to types.length for the intware handler.
        System.out.print("   Inserting reference record using resonCode = TE ...");
        voA1.setReasonCode(WithdrawalReason.TERMINATION);
        submissionId = dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1, DistributionJUnitConstants.TEST_TIMESTAMP_1);
        System.out.println("done.");
        
        System.out.print("   Updating reference record using resonCode = DI ...");
        String oldVal = voA2.getReasonDetailCode();
        voA2.setReasonCode(WithdrawalReason.DISABILITY);
        voA2.setReasonDetailCode(null);
        dao.update(DistributionJUnitConstants.TEST_CONTRACT_ID, submissionId,
                DistributionJUnitConstants.TEST_USER_PROFILE_ID_2, voA2);
        System.out.println("done.");

        System.out.print("   Verifying Values...");
        voA2.setSubmissionId(submissionId);
        verifyValues(voA2, createVoFromDb(submissionId));
        voA2.setReasonDetailCode(oldVal);
        System.out.println("done.");

//        System.out.print("   Deleting reference record...");
//        dao.delete(TEST_CONTRACT_ID, submissionId, TEST_USER_PROFILE_ID);
//        System.out.println("done.");        
//        System.out.println("testUpdate:end");
    }
    /**
     * Insert referecne records for 
     * 1. submission
     * 2. submission case
     * 3. submission withdrawal
     * 4. declaration
     * 5. fee
     * 6. loan
     * 7. moneytype
     * 8. note
     * 9. recipient
     * 10. recipeint address
     * 11. payee 
     * 12. payee adddress
     * 13. payee payment instruction
     * 
     * 
     * 
     * @throws Exception
     */
    public void testRead() throws Exception {
        System.out.println("testRead:begin");
        
        DeclarationDaoTestOld declTest = new DeclarationDaoTestOld("reference", WithdrawalRequestDeclaration.class);
        FeeDaoTestOld feeTest = new FeeDaoTestOld("reference", WithdrawalRequestFee.class);
        WithdrawalLoanDaoTestOld loanTest = new WithdrawalLoanDaoTestOld("reference");
        WithdrawalMoneyTypeDaoTestOld mtTest = new WithdrawalMoneyTypeDaoTestOld("reference");
        NoteDaoTestOld noteTest = new NoteDaoTestOld("reference", WithdrawalRequestNote.class);
        RecipientDaoTestOld recTest = new RecipientDaoTestOld("reference", WithdrawalRequestRecipient.class);
        DistributionAddressDaoTestOld addrTest = new DistributionAddressDaoTestOld("reference", Address.class);
        PayeeDaoTestOld payeeTest = new PayeeDaoTestOld("reference", WithdrawalRequestPayee.class);
        PaymentInstructionDaoTestOld payTest = new PaymentInstructionDaoTestOld(
                "reference", PayeePaymentInstruction.class);
        
        //1.
        //create the base submission records.
        System.out.print("   Inserting referecne withdrawal for value comparison...");
        insertReference(1); 
        System.out.println("done.");
        
        System.out.print("   Reading withdrawal request...");
        WithdrawalRequest vo = dao.read(DistributionJUnitConstants.TEST_SUBMISSION_ID);
        System.out.println("done.");
        
        System.out.print("   Verfiying values...");
        voA1.setSubmissionId(DistributionJUnitConstants.TEST_SUBMISSION_ID);
        verifyValues(vo, voA1);
        declTest.verifyValues(vo.getDeclarations().iterator().next(), declTest.REFERENCE_VO);
        feeTest.verifyValues(vo.getFees().iterator().next(), feeTest.REFERENCE_VO);
        loanTest.verifyValues(vo.getLoans().iterator().next(), loanTest.REFERENCE_VO);
        mtTest.verifyValues(vo.getMoneyTypes().iterator().next(), mtTest.REFERENCE_VO);
        noteTest.verifyValues(vo.getNotes().iterator().next(), noteTest.REFERENCE_VO);
        Recipient rec = vo.getRecipients().iterator().next();
        recTest.verifyValues(rec, recTest.REFERENCE_VO);
        addrTest.verifyRecipientValues(addrTest.REFERENCE_RECIPIENT_VO, rec.getAddress() );
        Payee payee = rec.getPayees().iterator().next();
        payeeTest.verifyValues(payee, payeeTest.REFERENCE_VO);
        addrTest.verifyPayeeValues(payee.getAddress(), addrTest.REFERENCE_PAYEE_VO);
        payTest.verifyValues(payee.getPaymentInstruction(), payTest.REFERENCE_VO);
        System.out.println("done.");
        
        System.out.print("   Cleanup...");
        dao.delete(DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_SUBMISSION_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);
        System.out.println("done.");
        
        //now test for record length
        //create the base submission records.
        System.out.print("   Inserting referecne withdrawal...");
        insertReference(2); 
        System.out.println("done.");
        
        System.out.print("   Reading withdrawal request for record length...");
        vo = dao.read(DistributionJUnitConstants.TEST_SUBMISSION_ID);
        System.out.println("done.");
        
        System.out.print("   Verfiying record counts...");
        voA1.setSubmissionId(DistributionJUnitConstants.TEST_SUBMISSION_ID);
        declTest.verifyRecordCount(declTest.SELECT_RECORD_COUNT + DistributionJUnitConstants.TEST_SUBMISSION_ID, 2);
        loanTest.verifyRecordCount(loanTest.SELECT_RECORD_COUNT,2);
        mtTest.verifyRecordCount(mtTest.SELECT_RECORD_COUNT,2);
        recTest.verifyRecordCount(recTest.SELECT_RECORD_COUNT +  + DistributionJUnitConstants.TEST_SUBMISSION_ID,2);
        System.out.println("done.");
        
        System.out.print("   Cleanup...");
        dao.delete(DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_SUBMISSION_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID);
        System.out.println("done.");        
        
        System.out.println("testRead:end");
        
    }
    private WithdrawalRequest createVoFromDb(Integer submissionId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String reasonCode = null;
        int i = 1;
        WithdrawalRequest returnVo = new WithdrawalRequest();
        try {
            conn = datasource.getConnection();
            stmt = conn.prepareStatement(SELECT_SUBMISSION_WITHDRAWAL);
            stmt.setInt(1, submissionId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                returnVo.setSubmissionId(new Integer(rs.getInt(i++)));
                returnVo.setContractId(new Integer(rs.getInt(i++)));
                i++; // submission case type code
                returnVo.setUserRoleCode(rs.getString(i++));
                returnVo.setEmployeeProfileId(new Integer(rs.getInt(i++)));
                returnVo.setParticipantId(new Integer(rs.getInt(i++)));
                returnVo.setParticipantStateOfResidence(rs.getString(i++));
                returnVo.setBirthDate(rs.getDate(i++));
                reasonCode = rs.getString(i++);
                returnVo.setReasonCode(reasonCode);
                returnVo.setReasonDetailCode(rs.getString(i++));
                returnVo.setReasonDescription(rs.getString(i++));
                returnVo.setParticipantLeavingPlanInd(isTrue(rs.getString(i++)));
                returnVo.setPaymentTo(rs.getString(i++));
                returnVo.setRequestDate(rs.getDate(i++));
                returnVo.setExpirationDate(rs.getDate(i++));
                if ( WithdrawalReason.isTermination(reasonCode)) {
                    returnVo.setTerminationDate(rs.getDate(i++));
                } else if (reasonCode.equals(WithdrawalReason.DISABILITY)) {
                    returnVo.setDisabilityDate(rs.getDate(i++));
                } else if (reasonCode.equals(WithdrawalReason.DEATH)) {
                    returnVo.setDeathDate(rs.getDate(i++));
                } else if (reasonCode.equals(WithdrawalReason.RETIREMENT)) {
                    returnVo.setRetirementDate(rs.getDate(i++));
                }
                returnVo.setFinalContributionDate(rs.getDate(i++));
                returnVo.setMostRecentPriorContributionDate(rs.getDate(i++));
                returnVo.setAmountTypeCode(rs.getString(i++));
                returnVo.setWithdrawalAmount(rs.getBigDecimal(i++));
                returnVo.setUnvestedAmountOptionCode(rs.getString(i++));
                returnVo.setIrsDistributionCodeLoanClosure(rs.getString(i++));
                returnVo.setLoanOption(rs.getString(i++));
                returnVo.setIraServiceProviderCode(rs.getString(i++));
                returnVo.setApprovedTimestamp(rs.getTimestamp(i++));
                returnVo.setLoan1099RName(rs.getString(i++));
                returnVo.setVestingCalledInd(isTrue(rs.getString(i++)));
                returnVo.setVestingOverwriteInd(isTrue(rs.getString(i++)));
                returnVo.setExpectedProcessingDate(rs.getDate(i++));
                returnVo.setPartWithPbaMoneyInd(isTrue(rs.getString(i++)));
                returnVo.setCreatedById(rs.getInt(i++));
                returnVo.setCreated(rs.getTimestamp(i++));
                returnVo.setLastUpdatedById(rs.getInt(i++));
                returnVo.setLastUpdated(rs.getTimestamp(i++));
                returnVo.setRequestRiskIndicator(isTrue(rs.getString("AT_RISK_IND")));
                
                rs.close();
                stmt.close();
                stmt = conn.prepareStatement(SELECT_SUBMISSION_CASE);
                stmt.setInt(1, submissionId);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    returnVo.setStatusCode(rs.getString(6));
                }
            }
            return returnVo;
        } finally {
            BaseDatabaseDAO.close(stmt, conn);
        }
    }    

    public void verifyValues(WithdrawalRequest vo1, WithdrawalRequest vo2) {
        String reasonCode;
        assertEquals(vo1.getSubmissionId(),vo2.getSubmissionId());
        assertEquals(vo1.getContractId(),vo2.getContractId());
        assertEquals(vo1.getUserRoleCode(),vo2.getUserRoleCode());
        assertEquals(vo1.getEmployeeProfileId(),vo2.getEmployeeProfileId());
        assertEquals(vo1.getParticipantId(),vo2.getParticipantId());
        assertEquals(vo1.getParticipantStateOfResidence(),vo2.getParticipantStateOfResidence());
        verifyDate(vo1.getBirthDate(),vo2.getBirthDate());
        reasonCode = vo1.getReasonCode();
        assertEquals(reasonCode,vo2.getReasonCode());
        assertEquals(vo1.getReasonDetailCode(),vo2.getReasonDetailCode());
        assertEquals(vo1.getReasonDescription(),vo2.getReasonDescription());
        assertEquals(vo1.getParticipantLeavingPlanInd(),vo2.getParticipantLeavingPlanInd());
        assertEquals(vo1.getPaymentTo(),vo2.getPaymentTo());
        verifyDate(vo1.getRequestDate(),vo2.getRequestDate());
        verifyDate(vo1.getExpirationDate(),vo2.getExpirationDate());
        if ( WithdrawalReason.isTermination(reasonCode)) {
            verifyDate(vo1.getTerminationDate(),vo2.getTerminationDate());
        } else if ( reasonCode.equals(WithdrawalReason.DISABILITY)) {
            verifyDate(vo1.getDisabilityDate(),vo2.getDisabilityDate());
        } else if ( reasonCode.equals(WithdrawalReason.RETIREMENT)) {
            verifyDate(vo1.getRetirementDate(),vo2.getRetirementDate());
        } else if ( reasonCode.equals(WithdrawalReason.DEATH)) {
            verifyDate(vo1.getDeathDate(),vo2.getDeathDate());
        }
        verifyDate(vo1.getFinalContributionDate(),vo2.getFinalContributionDate());
        verifyDate(vo1.getMostRecentPriorContributionDate(),vo2.getMostRecentPriorContributionDate());
        assertEquals(vo1.getAmountTypeCode(),vo2.getAmountTypeCode());
        assertEquals(vo1.getWithdrawalAmount(),vo2.getWithdrawalAmount());
        assertEquals(vo1.getUnvestedAmountOptionCode(),vo2.getUnvestedAmountOptionCode());
        assertEquals(vo1.getIrsDistributionCodeLoanClosure(),vo2.getIrsDistributionCodeLoanClosure());
        assertEquals(vo1.getLoanOption(),vo2.getLoanOption());
        assertEquals(vo1.getIraServiceProviderCode(),vo2.getIraServiceProviderCode());
        assertEquals(vo1.getApprovedTimestamp(),vo2.getApprovedTimestamp());
        assertEquals(vo1.getLoan1099RName(),vo2.getLoan1099RName());
        assertEquals(vo1.getVestingCalledInd(),vo2.getVestingCalledInd());
        assertEquals(vo1.getVestingOverwriteInd(),vo2.getVestingOverwriteInd());
        verifyDate(vo1.getExpectedProcessingDate(),vo2.getExpectedProcessingDate());
        assertEquals(vo1.getPartWithPbaMoneyInd(),vo2.getPartWithPbaMoneyInd());
        assertEquals(vo1.getCreatedById(),vo2.getCreatedById());
        assertEquals(vo1.getLastUpdatedById(),vo2.getLastUpdatedById());
        assertEquals(vo1.getStatusCode().trim(), vo2.getStatusCode().trim());
        assertEquals(vo1.getRequestRiskIndicator(), vo2.getRequestRiskIndicator());

    }
    private void verifyDate(Date birthDate, Date date) {

        Calendar c1 = new GregorianCalendar();
        Calendar c2 = new GregorianCalendar();
        c1.setTimeInMillis(birthDate.getTime());
        c2.setTimeInMillis(date.getTime());
        
        assertEquals(c1.get(Calendar.YEAR), c1.get(Calendar.YEAR));
        assertEquals(c1.get(Calendar.DAY_OF_MONTH), c1.get(Calendar.DAY_OF_MONTH));
        assertEquals(c1.get(Calendar.MONTH), c1.get(Calendar.MONTH));
        
    }

    /**
     * This record calls the withdrawalDAO.insert to insert a dummy record.
     * This is required for all of the other DAO tests, since they all 
     * need foreign key's to a real submission.
     * call update at end of function to fully populate all fields
     * 
     * 
     */
    public void insertReference(int count) throws Exception {

        insertSubSubCaseSubWithdrawal();
        
        if ( dao == null ) {
            dao = new WithdrawalDao();
        }
        setupValueObjects();
        voA1.setDeclarations(new DeclarationDaoTestOld("", WithdrawalRequestDeclaration.class).getVOs(count));
        voA1.setFees((new FeeDaoTestOld("", WithdrawalRequestFee.class).getVOs()));
        voA1.setLoans(new WithdrawalLoanDaoTestOld("").getVOs(count));
        voA1.setMoneyTypes(new WithdrawalMoneyTypeDaoTestOld("").getVOs(count));
        voA1.setCurrentAdminToParticipantNote((WithdrawalRequestNote)new NoteDaoTestOld("",WithdrawalRequestNote.class).getVO());
        List<Payee> payees = new ArrayList<Payee>();
        List<Recipient> recipients = new RecipientDaoTestOld("", WithdrawalRequestRecipient.class).getVO(count);
        Payee payee = new PayeeDaoTestOld("", WithdrawalRequestPayee.class).getVO();
        recipients.get(0).setAddress(new DistributionAddressDaoTestOld("", Address.class).getRecipientVO());
        recipients.get(0).setPayees(payees);
        payees.add(payee);
        payee.setPaymentInstruction(new PaymentInstructionDaoTestOld("", PayeePaymentInstruction.class).getVO());
        payee.setAddress(new DistributionAddressDaoTestOld("", Address.class).getPayeeVO());
        voA1.setRecipients(recipients);
        
        dao.update(DistributionJUnitConstants.TEST_CONTRACT_ID,
                DistributionJUnitConstants.TEST_SUBMISSION_ID,
                DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1);
        
        
    }
    
    public void insertSubSubCaseSubWithdrawal() throws Exception {
    	setupValueObjects();
        String sql1 = new String("insert into stp100.submission (CREATED_TS,CREATED_USER_ID,LAST_UPDATED_TS,LAST_UPDATED_USER_ID,SUBMISSION_ID) values (CURRENT TIMESTAMP, ?, CURRENT TIMESTAMP, ? , ? ) ");
        String sql2 = new String("insert into stp100.submission_case (CONTRACT_ID,CREATED_TS,CREATED_USER_ID,LAST_UPDATED_TS,LAST_UPDATED_USER_ID,PROCESS_STATUS_CODE,PROCESSED_TS,SUBMISSION_CASE_TYPE_CODE,SUBMISSION_ID,SUBMIT_COUNT,SYNTAX_ERROR_IND) values (?, CURRENT TIMESTAMP,?,CURRENT TIMESTAMP, ?, ?,CURRENT TIMESTAMP, ?, ?, ?, ?) ");
        String sql3 = new String("insert into stp100.submission_withdrawal (CONTRACT_ID, CREATED_BY_ROLE_CODE,CREATED_TS,CREATED_USER_PROFILE_ID,EXPIRATION_DATE,LAST_UPDATED_TS,LAST_UPDATED_USER_PROFILE_ID,PARTICIPANT_ID,PAYMENT_TO_CODE,PROFILE_ID,REQUEST_DATE,RESIDENCE_STATE_CODE,SUBMISSION_CASE_TYPE_CODE,SUBMISSION_ID,WITHDRAWAL_REASON_CODE) values ( ?,?,CURRENT TIMESTAMP, ? , CURRENT DATE, CURRENT TIMESTAMP, ? , ?, ?, ?, CURRENT DATE, ?, ?, ?, ? )");
        List<Object> params1 = new ArrayList<Object>();
        List<Object> params2 = new ArrayList<Object>();
        List<Object> params3 = new ArrayList<Object>();
        SQLInsertHandler handler1 = new SQLInsertHandler(BaseDatabaseDAO.STP_DATA_SOURCE_NAME, sql1);
        SQLInsertHandler handler2 = new SQLInsertHandler(BaseDatabaseDAO.STP_DATA_SOURCE_NAME, sql2);
        SQLInsertHandler handler3 = new SQLInsertHandler(BaseDatabaseDAO.STP_DATA_SOURCE_NAME, sql3);
        
        params1.add(DistributionJUnitConstants.TEST_USER_PROFILE_ID.toString());
        params1.add(DistributionJUnitConstants.TEST_USER_PROFILE_ID.toString());
        params1.add(DistributionJUnitConstants.TEST_SUBMISSION_ID);
        
        params2.add(DistributionJUnitConstants.TEST_CONTRACT_ID);
        params2.add(DistributionJUnitConstants.TEST_USER_PROFILE_ID.toString());
        params2.add(DistributionJUnitConstants.TEST_USER_PROFILE_ID.toString());
        params2.add("xx");
        params2.add("W");
        params2.add(DistributionJUnitConstants.TEST_SUBMISSION_ID);
        params2.add(2);
        params2.add("N");
        
        params3.add(DistributionJUnitConstants.TEST_CONTRACT_ID);
        params3.add(voA1.getUserRoleCode());
        params3.add(DistributionJUnitConstants.TEST_USER_PROFILE_ID.toString());
        params3.add(DistributionJUnitConstants.TEST_USER_PROFILE_ID.toString());
        params3.add(voA1.getParticipantId());
        params3.add(voA1.getPaymentTo());
        params3.add(voA1.getEmployeeProfileId());
        params3.add(voA1.getParticipantStateOfResidence());
        params3.add("W");
        params3.add(DistributionJUnitConstants.TEST_SUBMISSION_ID);
        params3.add("FU");
        
        handler1.insert(params1.toArray());
        handler2.insert(params2.toArray());
        handler3.insert(params3.toArray());
    }
    private boolean exists(Integer submissionId, String sql) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = datasource.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, submissionId);
            rs = stmt.executeQuery();
            return rs.next();
        }
        finally {
            BaseDatabaseDAO.close(stmt, conn);
        }    
    }
    protected Boolean isTrue(String arg) {
        return new Boolean(arg.equalsIgnoreCase("y"));
    }
    
    public void testPendingReviewApproveCount() {
        PendingReviewApproveWithdrawalCount cnt = new PendingReviewApproveWithdrawalCount();
        try {
            cnt = dao.getPendingReviewApproveCount(DistributionJUnitConstants.TEST_CONTRACT_ID2);
        } catch (SystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(0, cnt.getCountPendingApproveRequests().intValue());
        assertEquals(0, cnt.getCountPendingReviewRequests().intValue());
       // System.out.println("COUNT :: " + cnt.getCountPendingApproveRequests());
       // System.out.println("COUNT :: " + cnt.getCountPendingReviewRequests());
    }
    
    public void testGetWithdrawalsByStatus() {
        ArrayList<WithdrawalStateEnum> statusList = new ArrayList<WithdrawalStateEnum>();
        statusList.add(WithdrawalStateEnum.DRAFT);
        statusList.add(WithdrawalStateEnum.PENDING_APPROVAL);
        statusList.add(WithdrawalStateEnum.PENDING_REVIEW);

        try {
            Integer sid = dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1, DistributionJUnitConstants.TEST_TIMESTAMP_1);
            Collection<Integer> ids = dao.getWithdrawalsByContractAndStatus(
                    DistributionJUnitConstants.TEST_CONTRACT_ID, statusList, WithdrawalRequest.USER_ROLE_PLAN_SPONSOR_CODE);
            System.out.println("Inserted "+sid+"; Retrieved "+ids);
            dao.delete(DistributionJUnitConstants.TEST_CONTRACT_ID, sid, DistributionJUnitConstants.TEST_USER_PROFILE_ID);
        } catch (DistributionServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void testSetExpirationDate() {
        ArrayList<WithdrawalStateEnum> statusList = new ArrayList<WithdrawalStateEnum>();
        statusList.add(WithdrawalStateEnum.DRAFT);
        statusList.add(WithdrawalStateEnum.PENDING_APPROVAL);
        statusList.add(WithdrawalStateEnum.PENDING_REVIEW);

        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.add(Calendar.DATE, -1);
                
            Integer sid = dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1, DistributionJUnitConstants.TEST_TIMESTAMP_1);
            Collection<Integer> ids = dao.getWithdrawalsByContractAndStatus(
                    DistributionJUnitConstants.TEST_CONTRACT_ID, statusList, WithdrawalRequest.USER_ROLE_PLAN_SPONSOR_CODE);

            for(Integer id:ids) {
                System.out.println("Setting expiration date to " + 
                        java.text.SimpleDateFormat.getDateTimeInstance().format(cal.getTime()) + 
                        " for submission ID = " + id + " and contract ID = " + DistributionJUnitConstants.TEST_CONTRACT_ID);
                dao.setExpirationDate(id, new java.sql.Date(cal.getTimeInMillis()), DistributionJUnitConstants.TEST_USER_PROFILE_ID);
            }
            dao.delete(DistributionJUnitConstants.TEST_CONTRACT_ID, sid, DistributionJUnitConstants.TEST_USER_PROFILE_ID);
        } catch (DistributionServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void testGetFees() {
        WithdrawalRequestFee fee = new WithdrawalRequestFee();
        fee.setTypeCode("D");
        BigDecimal bd = new BigDecimal("67.00");
        fee.setValue(bd);
        Collection <Fee> collFee = new ArrayList<Fee>();
        collFee.add(fee);
        voA1.setFees(collFee);
        
        
        try {
            Integer submissionId = dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1, DistributionJUnitConstants.TEST_TIMESTAMP_1);
            System.out.println("Submission id:::  " + submissionId);
            WithdrawalRequestFee fees = dao.getFees(new Integer(submissionId));
            System.out.println("Fee type:: " + fees.getTypeCode());
            System.out.println("Fee value :: " + fees.getValue());
            assertEquals("D", fees.getTypeCode());
                      
            assertTrue(ObjectUtils.equals(fee.getValue(), fees.getValue()));
       
            System.out.print("   Deleting reference record...");
            dao.delete(DistributionJUnitConstants.TEST_CONTRACT_ID, submissionId, DistributionJUnitConstants.TEST_USER_PROFILE_ID);
            System.out.println("done.");
        } catch (Exception e) {
           
            e.printStackTrace();
        }

    }
 
     public void testGetAgreedLegaleseText() {
        System.out.println("testGetAgreedLegaleseText:begin");
      
        try {
            Integer submissionId = dao.insert(DistributionJUnitConstants.TEST_CONTRACT_ID, DistributionJUnitConstants.TEST_USER_PROFILE_ID, voA1, DistributionJUnitConstants.TEST_TIMESTAMP_1);
            System.out.println("Submission id:::  " + submissionId);
            voA1.setSubmissionId(submissionId);
            
            legaleseDao.insertWithdrawalLegaleseInfo(submissionId, voA1);
            
            String text = dao.getAgreedLegaleseText(submissionId, WithdrawalRequest.CMA_SITE_CODE_PSW);
            System.out.println("Agreed legalese text:: " + text);
                      
            assertNotNull(text);
       
            System.out.print("   Deleting reference record...");
            dao.delete(DistributionJUnitConstants.TEST_CONTRACT_ID, submissionId, DistributionJUnitConstants.TEST_USER_PROFILE_ID);
            System.out.println("done.");
            
        } catch (Exception e) {
           
            e.printStackTrace();
        }

    }   

    
 
        
    
}
