package com.manulife.pension.service.withdrawal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.withdrawal.exception.WithdrawalDaoException;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestLoan;

/**
 * @author Dennis
 *
 */
public class WithdrawalLoanDao extends BaseDatabaseDAO {

    private static final String CLASS_NAME = WithdrawalLoanDao.class.getName();
    private static final Logger logger = Logger.getLogger(WithdrawalLoanDao.class);
    
    private static final String SQL_INSERT_LOAN = new StringBuffer()
            .append("insert into ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("WITHDRAWAL_LOAN_DETAIL ( ")
            .append("SUBMISSION_ID,")
            .append("LOAN_ID,")
            .append("OUTSTANDING_LOAN_AMT,")
            .append("CREATED_USER_PROFILE_ID,")
            .append("CREATED_TS,")
            .append("LAST_UPDATED_USER_PROFILE_ID,")
            .append("LAST_UPDATED_TS ) ")
            .append(" values (?,?,?,?,CURRENT TIMESTAMP, ?, CURRENT TIMESTAMP )")
            .toString();
    
    private static final String SQL_UPDATE_LOAN = new StringBuffer()
            .append("update ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("WITHDRAWAL_LOAN_DETAIL set ")
            .append("OUTSTANDING_LOAN_AMT = ?,")
            .append("LAST_UPDATED_USER_PROFILE_ID = ?,")
            .append("LAST_UPDATED_TS = CURRENT TIMESTAMP ")
            .append("WHERE SUBMISSION_ID = ? and LOAN_ID = ?")
            .toString();
    
    private static final String SQL_DELETE_LOAN = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("WITHDRAWAL_LOAN_DETAIL where ") 
            .append("submission_id = ? and loan_id = ? ").toString();
    
    private static final String SQL_SELECT_LOANS = new StringBuffer()
            .append("select loan_id from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("WITHDRAWAL_LOAN_DETAIL WHERE submission_id = ? ").toString();

    private static final String SQL_DELETE_ALL_LOANS = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("WITHDRAWAL_LOAN_DETAIL where submission_id = ?").toString();
    
    /**
     * Inserts a collection of loans into the database.
     * 
     * @param contractId The currect Contract Id
     * @param submissionId The Submission Id
     * @param userProfileId The users profile Id
     * @param loans The collection of WithdrawalRequestMoneyType's
     * @throws DistributionServiceException Thrown if there is an underlying error

     */
    public void insert( final Integer submissionId,final Integer contractId,
            final Integer userProfileId, final Collection<WithdrawalRequestLoan> loans)
            throws DistributionServiceException {

        if (CollectionUtils.isEmpty(loans)) {
            return;
        }

        List<Object> params = null;
        SQLInsertHandler handler = new SQLInsertHandler(
                BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME, SQL_INSERT_LOAN);

        for (WithdrawalRequestLoan loan : loans) {
            try {
                params = new ArrayList<Object>();
                params.add(submissionId);
                params.add(loan.getLoanNo());
                params.add(loan.getOutstandingLoanAmount());
                params.add(userProfileId);
                params.add(userProfileId);
                handler.insert(params.toArray());
                
            } catch (DAOException e) {
                throw new WithdrawalDaoException(e, CLASS_NAME, "insert",
                        "Problem occurred in prepared call: " + SQL_INSERT_LOAN
                                + " for contract ID " + contractId + " and SubmissionId "
                                + submissionId);
            }
        }
    }

    /**
     * updates existing loans in the database.
     * 
     * @param contractId The currect Contract Id
     * @param submissionId The Submission Id
     * @param userProfileId The users profile Id
     * @param loans The collection of WithdrawalRequestMoneyType's
     * @throws DistributionServiceException Thrown if there is an underlying error
     */

    public void update( final Integer submissionId,final Integer contractId,
            final Integer userProfileId, final  Collection<WithdrawalRequestLoan> loans)
            throws DistributionServiceException {
        
        if (CollectionUtils.isEmpty(loans)) {
            return;
        }

        List<Object> params = null;
        SQLUpdateHandler handler = new SQLUpdateHandler(
                BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME, SQL_UPDATE_LOAN);

        for (WithdrawalRequestLoan loan : loans) {
            try {
                params = new ArrayList<Object>();
                params.add(loan.getOutstandingLoanAmount());
                params.add(userProfileId);
                params.add(submissionId);
                params.add(loan.getLoanNo());
                handler.update(params.toArray());
                
            } catch (DAOException e) {
                throw new WithdrawalDaoException(e, CLASS_NAME, "insert",
                        "Problem occurred in prepared call: " + SQL_UPDATE_LOAN
                                + " for contract ID " + contractId + " and SubmissionId "
                                + submissionId);
            }
        }        
    }
    /**
     * Deletes loans from the database.
     * 
     * @param contractId The currect Contract Id
     * @param submissionId The Submission Id
     * @param userProfileId The users profile Id
     * @param loans The collection of WithdrawalRequestMoneyType's
     * @throws DistributionServiceException Thrown if there is an underlying error
     */

    public void delete( final Integer submissionId,final Integer contractId,
            final Integer userProfileId, final Collection<WithdrawalRequestLoan> loans)
            throws DistributionServiceException {
        
        if (CollectionUtils.isEmpty(loans)) {
            return;
        }

        List<Object> params = null;
        SQLDeleteHandler handler = new SQLDeleteHandler(
                BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME, SQL_DELETE_LOAN);
        for (WithdrawalRequestLoan loan : loans) {
            try {
                params = new ArrayList<Object>(2);
                params.add(submissionId);
                params.add(loan.getLoanNo());
                handler.delete(params.toArray());
                
            } catch (DAOException e) {
                throw new WithdrawalDaoException(e, CLASS_NAME, "insert",
                        "Problem occurred in prepared call: " + SQL_DELETE_LOAN
                                + " for contract ID " + contractId + " and SubmissionId "
                                + submissionId);
            }
        }        
    }
    
    /**
     *  Inserts a record if it does not exist.  Updates it if it does exist, 
     *  and prunes all records in the database if they do not exist
     *  in @param referenceInput
     * 
     * @param contractId The currect Contract Id
     * @param submissionId The Submission Id
     * @param userProfileId The users profile Id
     * @param input The collection of WithdrawalRequestMoneyType's
     *                       that to be persisted to the database
     * @throws DistributionServiceException Thrown if there is an underlying error
     */

    public void insertUpdatePrune( final Integer submissionId,final Integer contractId,
            final Integer userProfileId, final Collection<WithdrawalRequestLoan> input)
            throws DistributionServiceException {
        
        //1. Get all loan from database
        //2. Iterate over loan colleciton passed in
        //3. decide for each loan if it is to be inserted, updated or deleted.
        Collection<WithdrawalRequestLoan> referenceInput = null; 
        Collection<WithdrawalRequestLoan> inserts = new ArrayList<WithdrawalRequestLoan>();
        Collection<WithdrawalRequestLoan> updates = new ArrayList<WithdrawalRequestLoan>();
        Collection<WithdrawalRequestLoan> deletes = new ArrayList<WithdrawalRequestLoan>();
        Map<Integer, WithdrawalRequestLoan> referenceDBMap = 
                new HashMap<Integer, WithdrawalRequestLoan>();
        Map<Integer, WithdrawalRequestLoan> referenceInputMap = 
                new HashMap<Integer, WithdrawalRequestLoan>();
        
        if (input == null) {
            referenceInput = new ArrayList<WithdrawalRequestLoan>();
        } else {
            referenceInput = input;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Object> params = new ArrayList<Object>(1);

        try {
            conn = getDefaultConnection(CLASS_NAME, BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME);
            stmt = conn.prepareStatement(SQL_SELECT_LOANS);
            stmt.setInt(1, submissionId);
            params.add(submissionId);
            if (logger.isDebugEnabled()) {
                // logSqlStatement(CLASS_NAME, "insertUpdatePrune",
                // SQL_SELECT_LOANS, params);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                WithdrawalRequestLoan loan = new WithdrawalRequestLoan();
                loan.setLoanNo(rs.getInt(1));
                referenceDBMap.put(loan.getLoanNo(), loan);
            }

            rs.close();
            stmt.close();
            
            for (WithdrawalRequestLoan  loan : referenceInput) {
                    if (referenceDBMap.containsKey(loan.getLoanNo())) {
                        updates.add(loan);
                    } else {
                        inserts.add(loan);
                    }
                    referenceInputMap.put(loan.getLoanNo(), loan);
                    
            }
            for (WithdrawalRequestLoan loan : referenceDBMap.values()) {
                if (!referenceInputMap.containsKey(loan.getLoanNo())) {
                    deletes.add(loan);
                }
            }

        } catch (SQLException sqlE) {
            throw new WithdrawalDaoException(sqlE, CLASS_NAME, "insertUpdatePrune",
                    "Problem occurred in function inserUpdatePrune" 
                            + " for contract ID " + contractId + " and SubmissionId "
                            + submissionId);
        } catch (SystemException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "insertUpdatePrune",
                    "Problem occurred in function inserUpdatePrune" 
                            + " for contract ID " + contractId + " and SubmissionId "
                            + submissionId);
		} finally {
            close(stmt, conn);
        }

        insert( submissionId,contractId, userProfileId, inserts);
        update( submissionId,contractId, userProfileId, updates);
        delete( submissionId,contractId, userProfileId, deletes);
    }
    /**
     * deletes all of the loans related to the given submission id.

     * @param contractId The contract Id
     * @param submissionId the submission id
     * @param userProfileId the user profile id
     * @throws DistributionServiceException If there is a data tier exception.
     */
    public void deleteAll( final Integer submissionId,final Integer contractId,
            final Integer userProfileId) throws DistributionServiceException {
        
        try {
            List<Object> params = new ArrayList<Object>();
            params.add(submissionId);
            new SQLDeleteHandler(BaseDatabaseDAO.STP_DATA_SOURCE_NAME, SQL_DELETE_ALL_LOANS)
                    .delete(params.toArray());

        } catch (DAOException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "deleteAll",
                    "Problem occurred in prepared call: " + SQL_DELETE_ALL_LOANS
                            + " for contract ID " + contractId + " and SubmissionId "
                            + submissionId);
        }
    }    
}
