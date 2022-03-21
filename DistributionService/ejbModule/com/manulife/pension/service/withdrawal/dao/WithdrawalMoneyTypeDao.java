/*
 * LoanMoneyTypeDao.java,v 1.1 2006/09/01 18:42:11 Paul_Glenn Exp
 * LoanMoneyTypeDao.java,v
 * Revision 1.1  2006/09/01 18:42:11  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.service.withdrawal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.withdrawal.exception.WithdrawalDaoException;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;

/**
 * The LoanMoneyTypeDao performs database access operations for
 * {@link WithdrawalRequestMoneyType} objects.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/01 18:42:11
 */
public class WithdrawalMoneyTypeDao extends BaseDatabaseDAO {

    private static final String CLASS_NAME = WithdrawalMoneyTypeDao.class.getName();
    private static final Logger logger = Logger.getLogger(WithdrawalLoanDao.class);

    
    private static final int[] SQL_INSERT_MONEY_TYPE_PARAMETER_TYPES = { Types.INTEGER, Types.CHAR,
        Types.DECIMAL, Types.DECIMAL, Types.CHAR, Types.DECIMAL,
        Types.DECIMAL, Types.DECIMAL, Types.DECIMAL ,Types.DECIMAL };

    private static final String SQL_INSERT_MONEY_TYPE = new StringBuffer()
            .append("insert into ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("WITHDRAWAL_MONEY_TYPE (")
            .append("SUBMISSION_ID,")//Types.INTEGER
            .append("MONEY_TYPE_ID,") //Types.CHAR
            .append("TOTAL_BAL_EXCL_LOAN_PBA_AMT,")// Types.DECIMAL
            .append("VESTING_PCT,")// Types.DECIMAL
            .append("VESTING_PCT_UPDATABLE_IND,")// Types.CHAR
            .append("WITHDRAWAL_AMT,")// Types.DECIMAL
            .append("WITHDRAWAL_PCT,")// Types.DECIMAL
            .append("CREATED_USER_PROFILE_ID,")// Types.DECIMAL
            .append("CREATED_TS,")
            .append("LAST_UPDATED_USER_PROFILE_ID,")// Types.DECIMAL
            .append("LAST_UPDATED_TS,")
            .append("AVAILABLE_HARDSHIP_AMT")
            .append(") VALUES (?,?,?,?,?,?,?,?, CURRENT TIMESTAMP, ?, CURRENT TIMESTAMP,?)").toString();

    private static final String SQL_UPDATE_MONEY_TYPE = new StringBuffer()
            .append("update ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("WITHDRAWAL_MONEY_TYPE set ")
            .append("TOTAL_BAL_EXCL_LOAN_PBA_AMT = ?,") // Types.DECIMAL
            .append("VESTING_PCT = ?,") //Types.DECIMAL
            .append("VESTING_PCT_UPDATABLE_IND = ?,")// Types.CHAR
            .append("WITHDRAWAL_AMT = ?,") // Types.DECIMAL
            .append("WITHDRAWAL_PCT = ?,") // Types.DECIMAL 
            .append("LAST_UPDATED_USER_PROFILE_ID = ?,") // Types.DECIMAL
            .append("LAST_UPDATED_TS  = CURRENT TIMESTAMP, ")
            .append("AVAILABLE_HARDSHIP_AMT =? ")
            .append("WHERE SUBMISSION_ID = ? AND MONEY_TYPE_ID = ?").toString();  // Types.DECIMAL, Types.CHAR
    
    private static final String SQL_CHECK_EXISTS = new StringBuffer()
            .append("select submission_id from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("WITHDRAWAL_MONEY_TYPE ")
            .append("where submission_id = ? and money_type_id = ? ").toString();
            
    private static final String SQL_DELETE_ALL_MONEY_TYPES = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("withdrawal_money_type where submission_id = ?").toString();
    

	private static final int[] UPDATE_TYPES = { Types.DECIMAL, Types.DECIMAL,
			Types.CHAR, Types.DECIMAL, Types.DECIMAL, Types.DECIMAL,
			Types.DECIMAL, Types.CHAR ,Types.DECIMAL};


    /**
	 * This method will inserts the {@link WithdrawalRequestMoneyType} if it
	 * does not exist and update it if it does exist.
	 * 
	 * @param contractId
	 *            The currect Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param moneyTypes
	 *            The collection of WithdrawalRequestMoneyType's
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 */
    public void insertUpdate( final Integer submissionId,final Integer contractId,
            final Integer userProfileId, final Collection<WithdrawalRequestMoneyType> moneyTypes)
            throws DistributionServiceException {
        
        if (moneyTypes == null) {
            return;
        }

        Collection<WithdrawalRequestMoneyType> inserts = 
            new ArrayList<WithdrawalRequestMoneyType>();
        Collection<WithdrawalRequestMoneyType> updates = 
            new ArrayList<WithdrawalRequestMoneyType>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getDefaultConnection(CLASS_NAME, BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME);
            for (WithdrawalRequestMoneyType moneyType : moneyTypes) {

                List<Object> params = new ArrayList<Object>(2);
                params.add(submissionId);
                params.add(moneyType.getMoneyTypeId());

                stmt = conn.prepareStatement(SQL_CHECK_EXISTS);
                stmt.setInt(1, submissionId);
                stmt.setString(2, moneyType.getMoneyTypeId());
                if (logger.isDebugEnabled()) {
//                    logSqlStatement(CLASS_NAME, "insertUpdate", SQL_CHECK_EXISTS, params);
                }
                stmt.execute();
                rs = stmt.getResultSet();
                
                if (rs != null) {
                    
                    if (rs.next()) {
                        updates.add(moneyType);
                    } else {
                        inserts.add(moneyType);
                    }
                    rs.close();
                    
                }
                
                stmt.close();
            }

        } catch (SQLException sqlE) {
            throw new WithdrawalDaoException(new DAOException(sqlE), CLASS_NAME, "insertUpdate",
                    "Problem occurred in prepared call: " + SQL_INSERT_MONEY_TYPE
                            + " for contract ID " + contractId + " and SubmissionId "
                            + submissionId);
        } catch (SystemException sysE) {
            throw new WithdrawalDaoException(new DAOException(sysE), CLASS_NAME, "insertUpdate",
                    "Problem occurred in prepared call: " + SQL_INSERT_MONEY_TYPE
                            + " for contract ID " + contractId + " and SubmissionId "
                            + submissionId);
        } finally {
            close(stmt, conn);
        }

        insert( submissionId,contractId, userProfileId, inserts);
        update( submissionId,contractId, userProfileId, updates);
        
    }
    
    /**
     * This method inserts a set of {@link WithdrawalRequestMoneyType}s into the database.
     * 
     * @param contractId The currect Contract Id
     * @param submissionId The Submission Id
     * @param userProfileId The users profile Id
     * @param moneyTypes The collection of WithdrawalRequestMoneyType's
     * @throws DistributionServiceException Thrown if there is an underlying error
     */
    public void insert( final Integer submissionId, final Integer contractId,
            final Integer userProfileId, final Collection<WithdrawalRequestMoneyType> moneyTypes)
            throws DistributionServiceException {

        if (moneyTypes == null) {
            return;
        }

        List<Object> params = null;
        SQLInsertHandler handler = new SQLInsertHandler(
                BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME, SQL_INSERT_MONEY_TYPE,
                SQL_INSERT_MONEY_TYPE_PARAMETER_TYPES);

        for (WithdrawalRequestMoneyType moneyType : moneyTypes) {
            try {
                params = new ArrayList<Object>();
                params.add(submissionId);
                params.add(moneyType.getMoneyTypeId());
                params.add(moneyType.getTotalBalance());
                params.add(moneyType.getVestingPercentage());
                params.add(moneyType.getVestingPercentageUpdateable());
                params.add(moneyType.getWithdrawalAmount());
                params.add(moneyType.getWithdrawalPercentage());
                params.add(userProfileId);
                params.add(userProfileId);
                params.add(moneyType.getAvailableHarshipAmount());
                handler.insert(params.toArray());
                
            } catch (DAOException e) {
                throw new WithdrawalDaoException(e, CLASS_NAME, "insert",
                        "Problem occurred in prepared call: " + SQL_INSERT_MONEY_TYPE
                                + " for contract ID " + contractId + " and SubmissionId "
                                + submissionId);
            }
        }
    }

    /**
     * Updates a record in the database.
     * 
     * @param contractId The currect Contract Id
     * @param submissionId The Submission Id
     * @param userProfileId The users profile Id
     * @param moneyTypes The collection of WithdrawalRequestMoneyType's
     * @throws DistributionServiceException Thrown if there is an underlying error
     */
    public void update( final Integer submissionId,final Integer contractId,
            final Integer userProfileId, final Collection<WithdrawalRequestMoneyType> moneyTypes)
            throws DistributionServiceException {
        
        if (moneyTypes == null) {
            return;
        }

        List<Object> params = null;
        SQLInsertHandler handler = new SQLInsertHandler(
                BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME, SQL_UPDATE_MONEY_TYPE, UPDATE_TYPES);
        for (WithdrawalRequestMoneyType moneyType : moneyTypes) {

            try {
                params = new ArrayList<Object>();
                params.add(moneyType.getTotalBalance());
                params.add(moneyType.getVestingPercentage());
                params.add(moneyType.getVestingPercentageUpdateable());
                params.add(moneyType.getWithdrawalAmount());
                params.add(moneyType.getWithdrawalPercentage());
                params.add(userProfileId);
                params.add(moneyType.getAvailableHarshipAmount());
                params.add(submissionId);
                params.add(moneyType.getMoneyTypeId());
                handler.insert(params.toArray());

            } catch (DAOException e) {
                throw new WithdrawalDaoException(e, CLASS_NAME, "update",
                        "Problem occurred in prepared call: " + SQL_UPDATE_MONEY_TYPE
                                + " for contract ID " + contractId + " and SubmissionId "
                                + submissionId);
            }
        }
    }
    /**
     * deletes all of the money types related to the given submission id.

     * @param contractId The contract Id
     * @param submissionId the submission id
     * @param userProfileId the user profile id
     * @throws DistributionServiceException If there is a data tier exception.
     */
    public void deleteAll( final Integer submissionId,final Integer contractId,
            final Integer userProfileId)
            throws DistributionServiceException {
        
        try {
            List<Object> params = new ArrayList<Object>();
            params.add(submissionId);
            new SQLDeleteHandler(BaseDatabaseDAO.STP_DATA_SOURCE_NAME, SQL_DELETE_ALL_MONEY_TYPES)
                    .delete(params.toArray());

        } catch (DAOException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "deleteAll",
                    "Problem occurred in prepared call: " + SQL_DELETE_ALL_MONEY_TYPES
                            + " for contract ID " + contractId + " and SubmissionId "
                            + submissionId);
        }
    }    
}
