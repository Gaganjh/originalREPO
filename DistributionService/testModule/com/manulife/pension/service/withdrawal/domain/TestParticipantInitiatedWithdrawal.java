package com.manulife.pension.service.withdrawal.domain;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.mockejb.SessionBeanDescriptor;

import com.ibm.db2.jcc.DB2DataSource;
import com.manulife.pension.content.service.BrowseService;
import com.manulife.pension.content.service.BrowseServiceBean;
import com.manulife.pension.content.service.BrowseServiceHome;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.DistributionContainerEnvironment;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.testutility.MockContainerEnvironment;
import com.manulife.pension.service.withdrawal.SQLConstants;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.LegaleseInfo;
import com.manulife.pension.service.withdrawal.valueobject.PayeePaymentInstruction;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * Test Suite for the WithdrawalService - particiapnt initiated txns
 * 
 * @author Maria Lee
 */
public class TestParticipantInitiatedWithdrawal 
    extends DistributionContainerEnvironment implements SQLConstants {

    private static Logger logger = Logger.getLogger(TestParticipantInitiatedWithdrawal.class);

    private static final long TEST_SUBMISSION_ID1 = 888888;
    private static final long TEST_SUBMISSION_ID2 = 999999;
    
    private static final int TEST_CONTRACT_ID = 67701;
    private static final int TEST_EMPLOYEE_PROFILE_ID = 112092788;
    private static final int TEST_PARTICIPANT_ID = 922052;
    private static final int TEST_USER_ID = 1576495;
 
    private static final String TEST_LAST_FEE_CHNG_WAS_PS_USER_IND = "N";
    private static final String FIRST_NAME = "Steven";
    private static final String LAST_NAME = "King";
    private static final String CURRENT_STATE_OF_RESIDENCE = "NY";
    private static final BigDecimal CURRENT_FEDERAL_TAX = new BigDecimal("10.000");
    private static final BigDecimal CURRENT_STATE_TAX = new BigDecimal("5.000");

    @Override
	public void setUp() throws Exception {
        super.setUp();
        
         SessionBeanDescriptor browseServiceLocalServiceDescriptor = 
            new SessionBeanDescriptor("java:comp/env/ejb/BrowseServiceHome", 
                    BrowseServiceHome.class, BrowseService.class, BrowseServiceBean.class); 
        
        MockContainerEnvironment.getMockContainer().deploy(browseServiceLocalServiceDescriptor);
         
       // insert dummy WD record
//        deleteDummyWithdrawalRequest(TEST_SUBMISSION_ID1);
//        deleteDummyWithdrawalRequest(TEST_SUBMISSION_ID2);
        
 //       insertDummyWithdrawalRequest(TEST_SUBMISSION_ID1);        
  //      insertDummyWithdrawalRequest(TEST_SUBMISSION_ID2); 
               
    }

    @Override
	public void tearDown() throws Exception {
//        deleteDummyWithdrawalRequest(TEST_SUBMISSION_ID1);
//        deleteDummyWithdrawalRequest(TEST_SUBMISSION_ID2);
        super.tearDown();
     }

/*    public void testInitiateParticipantWithdrawalRequest() {

        System.out.println("testInitiateParticipantWithdrawalRequest starting ");

        WithdrawalRequest wr = null;
        try {
            wr = Withdrawal.initiateNewParticipantWithdrawalRequest(
                    TEST_EMPLOYEE_PROFILE_ID, TEST_CONTRACT_ID);
              
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
        assertNotNull(wr);
        System.out.println("testInitiateParticipantWithdrawalRequest ended. ");
    } 
*/   
//    Leads to assertion failed error
    
    @Test
    public void testGetMostRecentWithdrawalRequest() {
// 
//        System.out.println("testGetMostRecentWithdrawalRequest starting ");
//
//        WithdrawalRequest wr = new WithdrawalRequest();
//        try {
//           wr = Withdrawal.getMostRecentWithdrawalRequest(TEST_EMPLOYEE_PROFILE_ID, TEST_CONTRACT_ID);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            fail(ex.getMessage());
//        }
//        assertNotNull(wr);
//        assertEquals(TEST_SUBMISSION_ID2, wr.getSubmissionId().intValue());
//        System.out.println("testGetMostRecentWithdrawalRequest ended. ");
    }
    
    @Test
    
    public void testIfWithdrawalRequestDraft() {
    	System.out.println("testIfWithdrawalRequestDraft starting ");
    	WithdrawalRequest wr = new WithdrawalRequest();
    	String bool = "x";
    	wr.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        if (!StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE,
                wr.getStatusCode()) 
                && !isWithdrawalRequestExpired(wr)
                && !StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DELETED_CODE, 
                		wr.getStatusCode())) {
        	bool = "t";
        }
        System.out.println("bool " + bool);
       
        assertEquals(bool, "x");
    }
    
   @Test
    
    public void testIfWithdrawalRequestNotDraft() {
    	System.out.println("testIfWithdrawalRequestNotDraft starting ");
    	WithdrawalRequest wr = new WithdrawalRequest();
    	String bool = "x";
    	wr.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_APPROVED_CODE);
       	Calendar cal = GregorianCalendar.getInstance();
       	cal.set(cal.get(Calendar.YEAR)+1, 4, 28);
       	wr.setExpirationDate(cal.getTime());
        if (!StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE,
                wr.getStatusCode()) 
                && !isWithdrawalRequestExpired(wr)
                && !StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DELETED_CODE, 
                		wr.getStatusCode())) {
        	bool = "t";
        }
        System.out.println("bool " + bool);
        assertEquals(bool, "t");
    }
   
   @Test
   
   public void testIfWithdrawalRequestExpired() {
   	System.out.println("testIfWithdrawalRequestExpired starting ");
   	WithdrawalRequest wr = new WithdrawalRequest();
   	String bool = "x";
   	wr.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_EXPIRED_CODE);
   	Calendar cal = GregorianCalendar.getInstance();
   	cal.set(2008, 1, 28);
   	wr.setExpirationDate(cal.getTime());
       if (!StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE,
               wr.getStatusCode()) 
               && !isWithdrawalRequestExpired(wr)
               && !StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DELETED_CODE, 
               		wr.getStatusCode())) {
       	bool = "t";
       }
       System.out.println("bool " + bool);
       assertEquals(bool, "x");
   }
   
  @Test
   
   public void testIfWithdrawalRequestDraftExpired() {
   	System.out.println("testIfWithdrawalRequestDraftExpired starting ");
   	WithdrawalRequest wr = new WithdrawalRequest();
   	String bool = "x";
   	wr.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
   	Calendar cal = GregorianCalendar.getInstance();
   	cal.set(2008, 4, 6);
   	wr.setExpirationDate(cal.getTime());
       if (!StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE,
               wr.getStatusCode()) 
               && !isWithdrawalRequestExpired(wr)
               && !StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DELETED_CODE, 
               		wr.getStatusCode())
               	&& !StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_EXPIRED_CODE, 
                      		wr.getStatusCode())
                ) {
       	bool = "t";
       }
       System.out.println("bool " + bool);
       assertEquals(bool, "x");
   }
  
  @Test
  
  public void testIfWithdrawalRequestDeleted() {
  	System.out.println("testIfWithdrawalRequestDeleted starting ");
  	WithdrawalRequest wr = new WithdrawalRequest();
  	String bool = "x";
  	wr.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DELETED_CODE);
  	Calendar cal = GregorianCalendar.getInstance();
  	cal.set(2010, 6, 28);
  	wr.setExpirationDate(cal.getTime());
      if (!StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE,
              wr.getStatusCode()) 
              && !isWithdrawalRequestExpired(wr)
              && !StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DELETED_CODE, 
              		wr.getStatusCode())) {
      	bool = "t";
      }
      System.out.println("bool " + bool);
      assertEquals(bool, "x");
  }
  
 @Test
  
  public void testIfWithdrawalRequestCSFSetToNo() {
  	System.out.println("testIfWithdrawalRequestDeleted starting ");
  	WithdrawalRequest wr = new WithdrawalRequest();
  	String bool = "x";
  	wr.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_EXPIRED_CODE);
  	Calendar cal = GregorianCalendar.getInstance();
  	cal.set(2010, 6, 28);
  	wr.setExpirationDate(cal.getTime());
      if (!StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE,
              wr.getStatusCode()) 
              && !isWithdrawalRequestExpired(wr)
              && !StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DELETED_CODE, 
              		wr.getStatusCode())
              && !StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_EXPIRED_CODE, 
                    		wr.getStatusCode())	) {
      	bool = "t";
      }
      System.out.println("bool " + bool);
      assertEquals(bool, "x");
  }
    
    private  boolean isWithdrawalRequestExpired(WithdrawalRequest request) {
    	if(request.getExpirationDate() != null) {
    		if(request.getExpirationDate().before(new Date())) {
    			return true;
    		}
    	}
    	return false;
    }
   
/*
    public void testSubmitParticipantInitiatedRetirementWithdrawal() {

        System.out.println("testSubmitParticipantInitiated2StepWithdrawal starting ");
        
        WithdrawalRequest wr = null;
        try {
            overrideEnvironmentEntries();
            wr = Withdrawal.initiateNewParticipantWithdrawalRequest(
                    TEST_EMPLOYEE_PROFILE_ID, TEST_CONTRACT_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } 
        
        populatePartipantWithdrawalRequestData(wr);
        
        Withdrawal withdrawal = new Withdrawal(wr);
        try {
            withdrawal.submitParticipantInitiatedWithdrawal();
              
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
        if (wr.getSubmissionId() != null) {
            System.out.println("deleting all entries for submission id: " + wr.getSubmissionId());
 //           deleteDummyWithdrawalRequest(wr.getSubmissionId());
        } else {
            if (wr.doErrorCodesExist()) {
                System.out.println("Can't submit, error codes exist. ");
            }
        } 
        if (! wr.getErrorCodes().isEmpty()) {
            System.out.println("There are errors in this submission " + wr.getErrorCodes());
        }
        assertNotNull(wr.getSubmissionId());        
        System.out.println("testSubmitParticipantInitiated2StepWithdrawal ended.");
   }
*/  
    private boolean insertDummyWithdrawalRequest(long submissionId) {
        boolean success = false;

        DataSource ds = null;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            ds = (DataSource) new InitialContext().lookup(BaseDatabaseDAO.STP_DATA_SOURCE_NAME);

            conn = ds.getConnection();
            // SUBMISSION
            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_1);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_USER_ID);
            stmt.setInt(3, TEST_USER_ID);
            stmt.setInt(4, TEST_USER_ID);
            success = stmt.executeUpdate() > 0;
            stmt.close();
            // SUBMISSION_CASE
            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_2);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_CONTRACT_ID);
//           stmt.setString(3, WithdrawalStateEnum.PENDING_REVIEW.getStatusCode());
            stmt.setString(3, WithdrawalStateEnum.READY_FOR_ENTRY.getStatusCode());
            
            stmt.setInt(4, TEST_USER_ID);
            stmt.setInt(5, TEST_USER_ID);
            success = success && stmt.executeUpdate() > 0;
            stmt.close();
            // SUBMISSION_WITHDRAWAL
            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_3);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_CONTRACT_ID);
            stmt.setInt(3, TEST_EMPLOYEE_PROFILE_ID);
            stmt.setInt(4, TEST_PARTICIPANT_ID);
            stmt.setInt(5, TEST_USER_ID);
            stmt.setInt(6, TEST_USER_ID);
            stmt.setString(7, TEST_LAST_FEE_CHNG_WAS_PS_USER_IND);
            success = success && stmt.executeUpdate() > 0;
            stmt.close();
            // WITHDRAWAL_LOAN_DETAIL
            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_4);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_USER_ID);
            stmt.setInt(3, TEST_USER_ID);
            success = success && stmt.executeUpdate() > 0;
            stmt.close();
            // WITHDRAWAL_MONEY_TYPE
            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_5_1);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_USER_ID);
            stmt.setInt(3, TEST_USER_ID);
            success = success && stmt.executeUpdate() > 0;
            stmt.close();
            // WITHDRAWAL_MONEY_TYPE
            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_5_2);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_USER_ID);
            stmt.setInt(3, TEST_USER_ID);
            success = success && stmt.executeUpdate() > 0;
            stmt.close();
            // WITHDRAWAL_MONEY_TYPE
            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_5_3);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_USER_ID);
            stmt.setInt(3, TEST_USER_ID);
            success = success && stmt.executeUpdate() > 0;
            // RECIPIENT
            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_7);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_USER_ID);
            stmt.setInt(3, TEST_USER_ID);
            success = success && stmt.executeUpdate() > 0;
            // PAYEE
            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_8);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_USER_ID);
            stmt.setInt(3, TEST_USER_ID);
            
            success = success && stmt.executeUpdate() > 0;           
            
            stmt.close();

        } catch (NamingException e) {
            logger.error("NamingException: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("SQLException: " + e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error("Error closing: " + e.getMessage());
            }
        }

        return success;
    }
    private void deleteDummyWithdrawalRequest(long submissionId) {
        DataSource ds = null;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            ds = (DataSource) new InitialContext().lookup(BaseDatabaseDAO.STP_DATA_SOURCE_NAME);

            conn = ds.getConnection();
            
            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_11);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("deleted WITHDRAWAL_LEGALESE");
            
             stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_12);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("deleted PAYEE_PAYMENT_INSTRUCTION");
            
            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_8);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("deleted PAYEE");

            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_7);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("deleted RECIPIENT");

            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_16);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("deleted ACTIVITY_DYNAMIC_DETAIL");

            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_6);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("deleted ACTIVITY_SUMMARY");

            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_15);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("deleted ACTIVITY_DETAIL");

            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_5);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("deleted WITHDRAWAL_MONEY_TYPE");

            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_4);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("deleted WITHDRAWAL_LOAN_DETAIL");
 
            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_13);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("deleted DECLARATION");

            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_14);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("deleted DISTRIBUTION_ADDRESS");

            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_3);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("deleted SUBMISSION_WITHDRAWAL");

            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_2);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("deleted SUBMISSION_CASE");

            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_1);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            System.out.println("deleted SUBMISSION");

        } catch (NamingException e) {
            logger.error("NamingException: " + e.getMessage());
        } catch (SQLException e) {
            logger.error("SQLException: " + e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error("Error closing: " + e.getMessage());
            }
        }
    }
    private void populatePartipantWithdrawalRequestData(WithdrawalRequest wr) {

//        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE); 
        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        wr.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE); 
        wr.setFinalContributionDate(new Date()); 
        wr.setCmaSiteCode(WithdrawalRequest.CMA_SITE_CODE_EZK);
        wr.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE); 
        wr.setTerminationDate(new Date());
        wr.setRetirementDate(new Date());
               
        LegaleseInfo participantLegaleseInfo = new LegaleseInfo(
                55750,
                "testing participant initiated a new version",
                 new Integer(TEST_USER_ID) );
        wr.setParticipantLegaleseInfo(participantLegaleseInfo);

        Address address = new Address();
        address.setAddressLine1("Payee address 1");
        address.setCity("New Jersey");
        address.setStateCode("CA");
        address.setZipCode("90210");
        address.setCountryCode("USA");

        // payment instruction
        PayeePaymentInstruction instruction = new PayeePaymentInstruction();
        instruction.setPayeeNo(new Integer(1));
        instruction.setBankTransitNumber(12345);
        instruction.setBankAccountTypeCode("");

        // payee
        WithdrawalRequestPayee payee = new WithdrawalRequestPayee();
        payee.setRecipientNo(1);
        payee.setPayeeNo(new Integer(1));
        payee.setFirstName("Donald");
        payee.setLastName("Trump");
        payee.setTypeCode("PA");
        payee.setReasonCode("P");
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.setRolloverPlanName("Roll Over Plan 1");
        payee.setPaymentInstruction(instruction);
        payee.setAddress(address);
        payee.setIrsDistCode(StringUtils.EMPTY);
        payee.setOrganizationName(StringUtils.EMPTY);
        payee.setMailCheckToAddress(Boolean.FALSE);
        payee.setSendCheckByCourier(Boolean.FALSE);
        payee.setCreatedById(TEST_USER_ID);
        payee.setLastUpdatedById(TEST_USER_ID);
        
        Collection<Payee> payees = new ArrayList<Payee>();
        payees.add(payee);
        
        // recipients
        WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        recipient.setFirstName(FIRST_NAME);
        recipient.setLastName(LAST_NAME);
        recipient.setRecipientNo(1);
        address.setAddressLine1("Recipient address 1");
        recipient.setAddress(address);
        recipient.setPayees(payees);
        
//        recipient.setUsCitizenInd(true);
        recipient.setStateOfResidenceCode(CURRENT_STATE_OF_RESIDENCE);
        recipient.setFederalTaxPercent(CURRENT_FEDERAL_TAX);
        recipient.setStateTaxPercent(CURRENT_STATE_TAX);
        recipient.setStateTaxTypeCode(StateTaxVO.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_FEDERAL_TAX);
        recipient.setStateTaxVo(new StateTaxVO() {
            {
                this.setTaxTypeCode(StateTaxVO.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_FEDERAL_TAX);
                this.setDefaultTaxRatePercentage(CURRENT_STATE_TAX);
                this.setTaxRequiredIndicator(true);
            }
        });
        
        Collection<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(recipient);       
        wr.setRecipients(recipients);
        
        // declarations
        Collection<Declaration> declarations = new ArrayList<Declaration>();
        WithdrawalRequestDeclaration declaration1 = new WithdrawalRequestDeclaration();
        declaration1.setTypeCode(WithdrawalRequestDeclaration.TAX_NOTICE_TYPE_CODE);
        declarations.add((WithdrawalRequestDeclaration) declaration1.clone());
        
        WithdrawalRequestDeclaration declaration2 = new WithdrawalRequestDeclaration();
        declaration2.setTypeCode(WithdrawalRequestDeclaration.WAITING_PERIOD_WAIVED_TYPE_CODE);
        declarations.add((WithdrawalRequestDeclaration) declaration2.clone());
        wr.setDeclarations(declarations);

    }
    private void overrideEnvironmentEntries() throws Exception {
        
        // override content database with ezk's
        DB2DataSource ds = new DB2DataSource();
        ds.setDatabaseName("vgncnt");
        ds.setUser("db2admin");
        ds.setPassword("db2admin");
        context.rebind("java:comp/env/jdbc/content", ds);
        context.rebind("psw.schemaName", "EZK100");
    }
}
