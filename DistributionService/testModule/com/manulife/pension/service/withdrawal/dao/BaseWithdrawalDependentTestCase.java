package com.manulife.pension.service.withdrawal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.dao.DistributionJUnitConstants;
import com.manulife.pension.service.testutility.MockContainerEnvironmentTestCase;
import com.manulife.pension.service.withdrawal.dao.WithdrawalDao;

public abstract class BaseWithdrawalDependentTestCase extends MockContainerEnvironmentTestCase {

    Logger logger = Logger.getLogger(BaseWithdrawalDependentTestCase.class);

    private Boolean referenceWithdrawalInserted = false;

    protected static final Integer TEST_SUBMISSION_ID = DistributionJUnitConstants.TEST_SUBMISSION_ID;

    protected static final Integer TEST_CONTRACT_ID = DistributionJUnitConstants.TEST_CONTRACT_ID;

    protected static final Integer TEST_USER_PROFILE_ID = DistributionJUnitConstants.TEST_USER_PROFILE_ID;

    protected static final Integer TEST_USER_PROFILE_ID_2 = TEST_USER_PROFILE_ID + 1;

    protected static final Integer TEST_RECIPIENT_NO = 1;

    protected static final Integer TEST_PAYEE_NO = 1;

    private static final String SUBMISSION_DATA_SOURCE_NAME = "jdbc/customerService";

    protected DataSource datasource = null;

    public BaseWithdrawalDependentTestCase(final String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        datasource = (DataSource) new InitialContext().lookup(SUBMISSION_DATA_SOURCE_NAME);

        logger.debug("inserting reference withdrawal...");
        new WithdrawalDaoTestOld("setupreference").insertSubSubCaseSubWithdrawal();
        referenceWithdrawalInserted = true;
        logger.debug("done.");
    }

    @Override
    public void tearDown() throws Exception {
        try {
            if (referenceWithdrawalInserted) {
                logger.debug("Deleting reference withdrawal...");
                new WithdrawalDao().delete(TEST_CONTRACT_ID, TEST_SUBMISSION_ID,
                        TEST_USER_PROFILE_ID);
                logger.debug("done.");
            }
        } catch (final Exception e) {
            logger.debug("couldn't delete withdrawal : ", e);
        }
        super.tearDown();

    }

    public void verifyRecordCount(final String sql, final Integer expectedRecordCount)
            throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if (datasource == null) {
                datasource = (DataSource) new InitialContext().lookup(SUBMISSION_DATA_SOURCE_NAME);
            }

            conn = datasource.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                assertTrue("no results came back for count", false);
            }
            assertEquals(expectedRecordCount, new Integer(rs.getInt(1)));
        } catch (final Exception e) {
            logger.debug(e);
            fail("found an exception");
        }

        finally {
            BaseDatabaseDAO.close(stmt, conn);
        }

    }

    protected Boolean isTrue(final String arg) {
        return new Boolean(arg.equalsIgnoreCase("y"));
    }

}
