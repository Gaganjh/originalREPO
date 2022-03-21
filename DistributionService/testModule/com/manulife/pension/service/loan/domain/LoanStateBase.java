package com.manulife.pension.service.loan.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.junit.Assert;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.service.distribution.DistributionContainerEnvironment;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.dao.LoanDao;
import com.manulife.pension.service.loan.valueobject.Loan;

public abstract class LoanStateBase extends DistributionContainerEnvironment {

    private static final String SQL_SELECT_COUNT_ONLINE_LOAN_CSF =
        "select count(*) from EZK100C.CONTRACT_SERVICE_FEATURE " +
        "where CONTRACT_ID = ? " +
        "and SERVICE_FEATURE_CODE = 'UOL' " +
        "and SERVICE_FEATURE_VALUE = 'Y'";
    
    private static final String SQL_DELETE_ONLINE_LOAN_CSF =
        "delete from EZK100C.CONTRACT_SERVICE_FEATURE " +
        "where CONTRACT_ID = ? " +
        "and SERVICE_FEATURE_CODE = 'UOL'";

    private static final String SQL_INSERT_ONLINE_LOAN_CSF =
        "insert into EZK100C.CONTRACT_SERVICE_FEATURE " +
        "(CONTRACT_ID, SERVICE_FEATURE_CODE, SERVICE_FEATURE_VALUE) values (" +
        "?, 'UOL', 'Y')";
    
    private static final String SQL_UPDATE_EXPIRE_ALL_LOAN_REQUESTS_FOR_PARTICIPANT =
        "update STP100.submission_case " +
        "set process_status_code = 'L7', last_updated_user_id = 'JUNIT', " +
        "    last_updated_ts = CURRENT TIMESTAMP " +
        "where submission_id in " +
        "    (select submission_id from STP100.submission_loan " +
        "     where profile_id = ? and contract_id = ?) ";

    private DataSource csdbDatasource = null;
    
    private DataSource stpDataSource = null;
    
    private List<Loan> loans = new ArrayList<Loan>();

	protected void discard(Loan loan) {
		loans.add(loan);
	}
 
	public void setUp() throws Exception {
        super.setUp();
        if (csdbDatasource == null) {
            csdbDatasource = (DataSource) new InitialContext()
            .lookup("jdbc/customerService");
        }
        
        if (stpDataSource == null) {
        	stpDataSource = (DataSource) new InitialContext()
            .lookup("jdbc/customerService");
        }
	}    

	public void tearDown() throws Exception {
		LoanDao loanDao = new LoanDao();
		for (Loan loan : loans) {
			loanDao.delete(loan.getSubmissionId(), loan.getContractId(), null);
		}
		loans.clear();
	}

	public Loan initiate(Integer participantProfileId, Integer contractId,
			Integer userProfileId, String createdByRoleCode) throws Exception {
		LoanState state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
		Loan loan = state.initiate(participantProfileId, contractId,
				userProfileId);
		Calendar requestCalendar = Calendar.getInstance();
		Calendar expirationCalendar = Calendar.getInstance();
		expirationCalendar.add(Calendar.MONTH, 1);
		loan.setExpirationDate(expirationCalendar.getTime());
		loan.setCreatedByRoleCode(createdByRoleCode);
		loan.setRequestDate(requestCalendar.getTime());
		
		/* Zero out the paymentAmount to prevent an exception being thrown when 
		 * it is null.  The UI ensures it is never null.   
		 */ 
		loan.getCurrentLoanParameter().setPaymentAmount(
		        GlobalConstants.ZERO_AMOUNT);
		return loan;
	}

	protected String formatLoanMessages(Set<LoanMessage> messages) {
		StringBuffer sb = new StringBuffer();
		for (Iterator<LoanMessage> it = messages.iterator(); it.hasNext();) {
			sb.append(it.next().getErrorCode().name());
			if (it.hasNext()) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	protected void assertLoanMessages(Loan loan, LoanErrorCode[] loanErrorCodes) {
		Set<LoanMessage> messages = new HashSet<LoanMessage>();
		messages.addAll(loan.getMessages());
		messages.addAll(loan.getErrors());
		Assert.assertEquals("Wrong message size:"
				+ formatLoanMessages(messages), loanErrorCodes.length, messages
				.size());
		for (LoanErrorCode loanErrorCode : loanErrorCodes) {
			boolean found = false;
			for (LoanMessage message : messages) {
				if (message.getErrorCode().equals(loanErrorCode)) {
					found = true;
					break;
				}
			}
			Assert.assertTrue("Message [" + loanErrorCode + "] not found",
					found);
		}
	}

    public void expireAllLoanRequestsForParticipant(Integer contractId,
            Integer participantProfileId) throws Exception {
        Connection conn = stpDataSource.getConnection();
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(SQL_UPDATE_EXPIRE_ALL_LOAN_REQUESTS_FOR_PARTICIPANT);
            try {
                stmt.setInt(1, participantProfileId);
                stmt.setInt(2, contractId);
                stmt.executeUpdate();
            } finally {
                stmt.close();
            }
        } finally {
            conn.close();
        }
    }

    /**
     * If the Online Loans CSF record does not exist or is not turned on 
     * in CSDB table EZK100C.CONTRACT_SERVICE_FEATURE, for the contractId
     * specified, then delete the existing record for the contractId and  
     * insert a new one with access turned on.  Otherwise, don't do anything.
     * @param contractId
     * @throws Exception
     */
    public void insertOnlineLoanCSF(Integer contractId) throws Exception {
        Connection conn = csdbDatasource.getConnection();
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(SQL_SELECT_COUNT_ONLINE_LOAN_CSF);
            try {
                stmt.setInt(1, contractId);
                ResultSet rs = stmt.executeQuery();
                int rowCount = 0;
                if (rs.next()) {
                    rowCount = rs.getInt(1);
                }
                if (rowCount == 0) {
                    stmt = conn.prepareStatement(SQL_DELETE_ONLINE_LOAN_CSF);
                    stmt.setInt(1, contractId);
                    stmt.executeUpdate();
                    stmt = conn.prepareStatement(SQL_INSERT_ONLINE_LOAN_CSF);
                    stmt.setInt(1, contractId);
                    stmt.executeUpdate();
                }
            } finally {
                stmt.close();
            }
        } finally {
            conn.close();
        }
    }

}
