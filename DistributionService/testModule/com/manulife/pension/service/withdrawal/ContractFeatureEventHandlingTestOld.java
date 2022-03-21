package com.manulife.pension.service.withdrawal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.security.MockPrincipalFactory;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

/**
 * Test Suite for Withdrawal Service's Contract Service Feature event handlers
 * 
 * @author Aurelian Penciu
 */
public class ContractFeatureEventHandlingTestOld extends BaseWithdrawalServiceTestCase implements
        SQLConstants {

    private static Logger logger = Logger.getLogger(ContractFeatureEventHandlingTestOld.class);

    private static final long TEST_SUBMISSION_ID = 999999;

    private static final int TEST_CONTRACT_ID = 67701;

    private static final int TEST_EMPLOYEE_PROFILE_ID = 112092788;

    private static final int TEST_PARTICIPANT_ID = 922052;

    private static final int TEST_USER_ID = 1576495;

    protected boolean isDummyWdSet = false;

    /**
     * Constructor
     */
    public ContractFeatureEventHandlingTestOld() {
        super();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        // insert dummy WD record
        deleteDummyWithdrawalRequest(TEST_SUBMISSION_ID);
        isDummyWdSet = insertDummyWithdrawalRequest(TEST_SUBMISSION_ID);
    }

    @After
    @Override
    public void tearDown() throws Exception {
        deleteDummyWithdrawalRequest(TEST_SUBMISSION_ID);
        super.tearDown();
    }

    /**
     * Simulates the action performed by the Contract Service when the oneStep feature is turned on
     * for a contract.
     */
    @Test
    @Ignore
    public void testHandleEnableOneStepApprovals() throws Exception {
        assertTrue(isDummyWdSet);

            Principal principal = MockPrincipalFactory.getInstance().getPrincipal(TEST_USER_ID,
                    "TPA", "", "", "");
            getLocalWithdrawalService().handleEnableOneStepApprovals(TEST_CONTRACT_ID, principal,
                    WithdrawalRequest.USER_ROLE_PLAN_SPONSOR_CODE);

            assertTrue(checkDummyWithdrawalStatus(TEST_SUBMISSION_ID,
                    WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode()));
    }

    /**
     * Simulates the action performed by the Contract Service when Online W/D feature is turned on
     * for a contract.
     */
    @Test
    @Ignore
    public void testHandleEnableTwoStepApprovals() throws Exception {
        assertTrue(isDummyWdSet);

            getLocalWithdrawalService()
                    .handleEnableTwoStepApprovals(TEST_CONTRACT_ID, TEST_USER_ID);
    }

    /**
     * Simulates the action performed by the Contract Service when Online W/D feature is turned off
     * for a contract.
     */
    @Test
    @Ignore
    public void testHandleDisableOnlineWithdrawals() throws Exception {
        assertTrue(isDummyWdSet);

            getLocalWithdrawalService().handleDisableOnlineWithdrawals(TEST_CONTRACT_ID,
                    TEST_USER_ID, WithdrawalRequest.CMA_SITE_CODE_PSW);

            assertTrue(checkDummyWithdrawalExpiryDate(TEST_SUBMISSION_ID));
    }

    /**
     * Simulates the action performed by the Contract Service when the twoStep feature is turned on
     * for a contract.
     */
    @Test
    @Ignore
    public void testHandleEnableOnlineWithdrawals() throws Exception {
        assertTrue(isDummyWdSet);
            getLocalWithdrawalService()
                    .handleEnableTwoStepApprovals(TEST_CONTRACT_ID, TEST_USER_ID);
    }

    private boolean insertDummyWithdrawalRequest(long submissionId) throws Exception {
        boolean success = false;

        DataSource ds = null;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            ds = (DataSource) new InitialContext().lookup(BaseDatabaseDAO.STP_DATA_SOURCE_NAME);

            conn = ds.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_1);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_USER_ID);
            stmt.setInt(3, TEST_USER_ID);
            stmt.setInt(4, TEST_USER_ID);
            success = stmt.executeUpdate() > 0;
            stmt.close();

            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_2);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_CONTRACT_ID);
            stmt.setString(3, WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode());
            stmt.setInt(4, TEST_USER_ID);
            stmt.setInt(5, TEST_USER_ID);
            success = success && stmt.executeUpdate() > 0;
            stmt.close();

            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_3);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_CONTRACT_ID);
            stmt.setInt(3, TEST_EMPLOYEE_PROFILE_ID);
            stmt.setInt(4, TEST_PARTICIPANT_ID);
            stmt.setInt(5, TEST_USER_ID);
            stmt.setInt(6, TEST_USER_ID);
            stmt.setString(7, "N");
            success = success && stmt.executeUpdate() > 0;
            stmt.close();

            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_4);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_USER_ID);
            stmt.setInt(3, TEST_USER_ID);
            success = success && stmt.executeUpdate() > 0;
            stmt.close();

            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_5_1);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_USER_ID);
            stmt.setInt(3, TEST_USER_ID);
            success = success && stmt.executeUpdate() > 0;
            stmt.close();

            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_5_2);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_USER_ID);
            stmt.setInt(3, TEST_USER_ID);
            success = success && stmt.executeUpdate() > 0;
            stmt.close();

            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_5_3);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_USER_ID);
            stmt.setInt(3, TEST_USER_ID);
            success = success && stmt.executeUpdate() > 0;

            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_7);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_USER_ID);
            stmt.setInt(3, TEST_USER_ID);
            success = success && stmt.executeUpdate() > 0;

            stmt = conn.prepareStatement(SQL_INSERT_DUMMY_WITHDRAWAL_8);
            stmt.setLong(1, submissionId);
            stmt.setInt(2, TEST_USER_ID);
            stmt.setInt(3, TEST_USER_ID);
            success = success && stmt.executeUpdate() > 0;

        } finally {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
        }

        return success;
    }

    private boolean checkDummyWithdrawalStatus(long submissionId, String statusCode) throws Exception {
        boolean success = false;

        DataSource ds = null;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            ds = (DataSource) new InitialContext().lookup(BaseDatabaseDAO.STP_DATA_SOURCE_NAME);

            conn = ds.getConnection();
            stmt = conn.prepareStatement(SQL_GET_WITHDRAWAL_STATUS);
            stmt.setLong(1, submissionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                success = rs.getString("PROCESS_STATUS_CODE").equals(statusCode);

            rs.close();
        } finally {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
        }

        return success;
    }

    private boolean checkDummyWithdrawalExpiryDate(long submissionId) throws Exception {
        boolean success = false;

        DataSource ds = null;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            ds = (DataSource) new InitialContext().lookup(BaseDatabaseDAO.STP_DATA_SOURCE_NAME);

            conn = ds.getConnection();
            stmt = conn.prepareStatement(SQL_GET_WITHDRAWAL_EXPIRY_DATE);
            stmt.setLong(1, submissionId);
            GregorianCalendar cal = new GregorianCalendar();
            cal.add(Calendar.DATE, -1);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                success = (rs.getDate("EXPIRATION_DATE").getTime() - cal.getTimeInMillis()) < 1000 * 60 * 60 * 24;
            }
            rs.close();
        } finally {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
        }

        return success;
    }

    private void deleteDummyWithdrawalRequest(long submissionId) throws Exception {
        DataSource ds = null;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            ds = (DataSource) new InitialContext().lookup(BaseDatabaseDAO.STP_DATA_SOURCE_NAME);

            conn = ds.getConnection();

            // Distribution Address
            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_10);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();

            // Payee Payment Instructions
            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_9);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();

            // Payee
            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_8);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();

            // Recipient
            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_7);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();

            // Activity Summary
            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_6);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();

            // Money Types
            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_5);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();

            // Loan Details
            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_4);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();

            // Submission Withdrawal
            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_3);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();

            // Submission Case
            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_2);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();
            stmt.close();

            // Submission
            stmt = conn.prepareStatement(SQL_DELETE_DUMMY_WITHDRAWAL_1);
            stmt.setLong(1, submissionId);
            stmt.executeUpdate();

        } finally {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
        }
    }
}
