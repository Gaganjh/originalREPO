package com.manulife.pension.service.distribution.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.loan.dao.LoanDao;
import com.manulife.pension.service.loan.dao.LoanDaoTestOld;
import com.manulife.pension.service.testutility.MockContainerEnvironmentTestCase;
import com.manulife.pension.service.withdrawal.dao.WithdrawalDao;

public class BaseSubmissionDependentTestCase extends MockContainerEnvironmentTestCase {

    Logger logger = Logger.getLogger(BaseSubmissionDependentTestCase.class);

    protected static final Integer TEST_RECIPIENT_NO = 1;

    protected static final Integer TEST_PAYEE_NO = 1;

    private static final String SUBMISSION_DATA_SOURCE_NAME = "jdbc/customerService";

    protected DataSource datasource = null;
    private Integer referenceSubmissionId = null;
    private boolean referenceRecordInserted = false;

    public BaseSubmissionDependentTestCase(final String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        datasource = (DataSource) new InitialContext().lookup(SUBMISSION_DATA_SOURCE_NAME);
        //we don't really need a submission record and a submission case record, since there
        //are no foreign keys for delcaration, address, fee, note, payee, payment instruction and recipient.
        //however, since there should be foreign keys, i will create the reference record now so that
        //this test won't break when the fix the problem in the DB.
        
        //also, it'll just be easier to call an existing dao than write all the code. so i'll just do that. 
   		referenceSubmissionId = new LoanDaoTestOld("setupreference").insertSubSubCaseSubLoan();
		referenceRecordInserted = true;

        
        logger.debug("done.");
    }
    public Integer getSubmissionId() { 
    	return referenceSubmissionId;
    }
    
    @Override
    public void tearDown() throws Exception {
		try {
			if (referenceRecordInserted) {
				new LoanDao().delete(getSubmissionId(), DistributionJUnitConstants.TEST_CONTRACT_ID,
						DistributionJUnitConstants.TEST_USER_PROFILE_ID);
			}
		} catch (final Exception e) {
			logger.debug("couldn't delete Loan : ", e);
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

    public Connection getConnectionSDB() throws Exception {
        Connection conn = null;
        try {
            if (datasource == null) {
                datasource = (DataSource) new InitialContext().lookup(SUBMISSION_DATA_SOURCE_NAME);
            }
            conn = datasource.getConnection();
        } catch (final Exception e) {
            logger.debug(e);
            fail("found an exception");
        }
        return conn;
    }
}
