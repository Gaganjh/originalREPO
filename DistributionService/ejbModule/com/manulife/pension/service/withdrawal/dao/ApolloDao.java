package com.manulife.pension.service.withdrawal.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;

import com.intware.dao.DAOException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;

/**
 * @author Paul_Glenn
 * @author Chris Shin
 * 
 * DAO for accessing the Apollo DB on the mainframe.
 */
public class ApolloDao  extends BaseDatabaseDAO {


    public static final String APPLICATION_ERROR_CODE_ZERO = "0000";

    public static final String DEFAULT_COMPLETED_WD_THRESHOLD_PERIOD = "5";

    public static final String DEFAULT_PENDING_WD_THRESHOLD_PERIOD = "30";

    private static final String CLASS_NAME = ApolloDao.class.getName();

    private static final String SQL_SELECT_COMPLETED_WD_TRANSACTION = ""
            + "SELECT COUNT(*) "
            + "FROM "
            + APOLLO_SCHEMA_NAME
            + ".VLP1014 T1014, "
            + APOLLO_SCHEMA_NAME
            + ".VLP1066 T1066 "
            + "WHERE T1014.CNNO   = ? "
            + "  AND T1066.PRTID  = ? "
            + "  AND T1014.PROPNO = T1066.PROPNO "
            + "  AND T1066.TRTYP    = 'WD' "
            + "  AND T1066.TRSTATCD = 'CM' "
	        + "  AND T1066.TRRSNCD NOT IN ('EC','ED','IL','IO','IP','PW','SR','UM') "
            + "  AND T1066.RTEFDT   >= (SELECT CYCLEDTE - CAST(? AS INTEGER) DAYS "
            + "FROM " + APOLLO_SCHEMA_NAME + ".VLP0103 "
            + "  WHERE BUSUNIT = 'BRKRDLR' " + "    AND CYCLEID = 'RUNDATE') "
            + " AND NOT EXISTS (SELECT 1 "
            + "  FROM "
            + APOLLO_SCHEMA_NAME
            + ".VLP1066 A, "       
            + APOLLO_SCHEMA_NAME
            + ".VLP1143 B "
            + "  WHERE B.TRANNO2 = T1066.TRANNO "
            + "  AND   B.TRTYP2 = 'WD' "
            + "  AND   B.TRTYP1 = 'WD' "
            + "  AND   B.ASSORSN = 'RFULL' "
            + "  AND   B.TRANNO1 = A.TRANNO "
            + "  AND   A.TRSTATCD = 'CM' "
            + "  AND   A.TRANMODE = 'R' "
            + ") ";

    private static final String SQL_SELECT_PENDING_WD_TRANSACTION = ""
            + "SELECT COUNT(*) "
            + "FROM "
            + APOLLO_SCHEMA_NAME
            + ".VLP1014 T1014, "
            + APOLLO_SCHEMA_NAME
            + ".VLP1066 T1066 "
            + "WHERE T1014.CNNO   = ? "
            + "  AND T1066.PRTID  = ? "
            + "  AND T1014.PROPNO = T1066.PROPNO "
            + "  AND T1066.TRTYP    = 'WD' "
            + "  AND T1066.TRSTATCD IN ('WB','SM','SI','FL') "
	        + "  AND T1066.TRRSNCD NOT IN ('EC','ED','IL','IO','IP','PW','SR','UM') "
            + "	 AND T1066.RTEFDT   >= (SELECT CYCLEDTE - CAST(? AS INTEGER) DAYS "
            + "                   		  FROM " + APOLLO_SCHEMA_NAME
            + ".VLP0103 " + "  WHERE BUSUNIT = 'BRKRDLR' "
            + "    AND CYCLEID = 'RUNDATE') " 
            + "  AND T1066.ACCTPRDT = '9999-12-31' ";

    /**
     * Default Constructor.
     */
    public ApolloDao() {
        super();
    }

    // ========================================================================

    /**
     * Retrieves the # of completed withdrawal transactions on Apollo for a
     * given timeframe.
     * 
     * @param contractId
     *            The contract ID.
     * @param participantId
     *            The participant ID.
     * @return int The number of Completed Withdrawal Transactions.
     * @throws DAOException
     *             If an exception occurs.
     */
    public int getNumberOfCompletedWithdrawalTransaction(
            final Integer contractId, final Integer participantId) throws DAOException {

        int numberOfCompletedWithdrawalTransactions = 0;
        Integer threshold = null;
        try {
            InitialContext context = new InitialContext();
            threshold = (Integer) context.lookup("java:comp/env/completedApolloWDThreshold");

            Connection apolloConnection = null;
            CallableStatement statement = null;
            try {
                apolloConnection = getDefaultConnection(CLASS_NAME, APOLLO_DATA_SOURCE_NAME);
            } catch (SQLException e) {
                throw new SystemException(e, CLASS_NAME,
                        "getNumberOfCompletedWithdrawalTransaction",
                        "Problem occurred getting APOLLO connection");
            }

            try {
                int i = 1;
                statement = apolloConnection
                        .prepareCall(SQL_SELECT_COMPLETED_WD_TRANSACTION);
                statement.setInt(i++, contractId);
                statement.setInt(i++, participantId);
                statement.setInt(i++, threshold);

                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    numberOfCompletedWithdrawalTransactions = rs.getInt(1);
                }
            } finally {
                close(statement, apolloConnection);
            }
        } catch (Exception exception) {

            StringBuffer buff = new StringBuffer();
            buff.append("ApolloDao.getNumberOfCompletedWithdrawalTransaction,");
            buff.append(" contractId: ");
            buff.append(contractId);
            buff.append(" participantId: ");
            buff.append(participantId);
            buff.append(" threshold: ");
            buff.append(exception.getMessage());
            DAOException daoException = new DAOException(buff.toString(), exception);

            throw daoException;
        }

        return numberOfCompletedWithdrawalTransactions;
    }

    /**
     * Retrieves the # of pending withdrawal transactions on a Apollo for a
     * given timeframe.
     * 
     * @param contractId
     *            The contract ID.
     * @param participantId
     *            The participant ID.
     * @return int The number of Pending Withdrawal Transactions.
     * @throws DAOException
     *             If an exception occurs.
     */
    public int getNumberOfPendingWithdrawalTransaction(final Integer contractId,
            final Integer participantId)
            throws DAOException {

        int numberOfPendingWithdrawalTransactions = 0;
        Integer threshold = null;
        try {
            InitialContext context = new InitialContext();
            threshold = (Integer) context.lookup("java:comp/env/pendingApolloWDThreshold");
            Connection apolloConnection = null;
            CallableStatement statement = null;

            try {
                apolloConnection = getDefaultConnection(CLASS_NAME, APOLLO_DATA_SOURCE_NAME);
            } catch (SQLException e) {
                throw new SystemException(e, CLASS_NAME,
                        "getNumberOfPendingWithdrawalTransaction",
                        "Problem occurred getting APOLLO connection");
            }

            try {
                int i = 1;
                statement = apolloConnection
                        .prepareCall(SQL_SELECT_PENDING_WD_TRANSACTION);
                statement.setInt(i++, contractId);
                statement.setInt(i++, participantId);
                statement.setInt(i++, threshold);

                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    numberOfPendingWithdrawalTransactions = rs.getInt(1);
                }
            } finally {
                close(statement, apolloConnection);
            }
        } catch (Exception exception) {

            StringBuffer buff = new StringBuffer();
            buff.append("ApolloDao.getNumberOfPendingWithdrawalTransaction,");
            buff.append(" contractId: ");
            buff.append(contractId);
            buff.append(" participantId: ");
            buff.append(participantId);
            buff.append(" Exception: ");
            buff.append(exception.getMessage());
            DAOException daoException = new DAOException(buff.toString(), exception);

            throw daoException;
        }

        return numberOfPendingWithdrawalTransactions;
    }

}
