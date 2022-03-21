package com.manulife.pension.service.loan.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.intware.dao.DAOException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.loan.LoanDefaults;

public class ApolloDao extends BaseDatabaseDAO {

	private static final String CLASS_NAME = ApolloDao.class.getName();

	private static final String SQL_SELECT_COMPLETED_LI_TRANSACTION = ""
			+ "SELECT COUNT(*) " + "FROM "
			+ APOLLO_SCHEMA_NAME
			+ ".VLP1014 T1014, "
			+ APOLLO_SCHEMA_NAME
			+ ".VLP1066 T1066 "
			+ "WHERE T1014.CNNO   = ? "
			+ "  AND T1066.PRTID  = ? "
			+ "  AND T1014.PROPNO = T1066.PROPNO "
			+ "  AND T1066.TRTYP    = 'LI' "
			+ "  AND T1066.TRSTATCD = 'CM' "
			+ "  AND T1066.RTEFDT   >= (SELECT CYCLEDTE - CAST(? AS INTEGER) DAYS "
			+ "FROM "
			+ APOLLO_SCHEMA_NAME
			+ ".VLP0103 "
			+ "  WHERE BUSUNIT = 'BRKRDLR' "
			+ "    AND CYCLEID = 'RUNDATE') "
			+ " AND NOT EXISTS (SELECT 1 "
			+ "  FROM "
			+ APOLLO_SCHEMA_NAME
			+ ".VLP1066 A, "
			+ APOLLO_SCHEMA_NAME
			+ ".VLP1143 B "
			+ "  WHERE B.TRANNO2 = T1066.TRANNO "
			+ "  AND   B.TRTYP2 = 'LI' "
			+ "  AND   B.TRTYP1 = 'LI' "
			+ "  AND   B.ASSORSN = 'RFULL' "
			+ "  AND   B.TRANNO1 = A.TRANNO "
			+ "  AND   A.TRSTATCD = 'CM' "
			+ "  AND   A.TRANMODE = 'R' " + ") ";

	private static final String SQL_SELECT_PENDING_LI_TRANSACTION = ""
			+ "SELECT COUNT(*) "
			+ "FROM "
			+ APOLLO_SCHEMA_NAME
			+ ".VLP1014 T1014, "
			+ APOLLO_SCHEMA_NAME
			+ ".VLP1066 T1066 "
			+ "WHERE T1014.CNNO   = ? "
			+ "  AND T1066.PRTID  = ? "
			+ "  AND T1014.PROPNO = T1066.PROPNO "
			+ "  AND T1066.TRTYP    = 'LI' "
			+ "  AND T1066.TRSTATCD IN ('WB','SM','SI','FL') "
			+ "	 AND T1066.RTEFDT   >= (SELECT CYCLEDTE - CAST(? AS INTEGER) DAYS "
			+ "                   		  FROM " + APOLLO_SCHEMA_NAME + ".VLP0103 "
			+ "  WHERE BUSUNIT = 'BRKRDLR' " + "    AND CYCLEID = 'RUNDATE') "
			+ "  AND T1066.ACCTPRDT = '9999-12-31' ";

	/**
	 * Default Constructor.
	 */
	public ApolloDao() {
		super();
	}

	// ========================================================================

	/**
	 * Retrieves the # of completed loan transactions on Apollo for a given
	 * timeframe.
	 * 
	 * @param contractId
	 *            The contract ID.
	 * @param participantId
	 *            The participant ID.
	 * @return int The number of Completed Loan Transactions.
	 * @throws DAOException
	 *             If an exception occurs.
	 */
	public int getNumberOfCompletedLoanTransaction(final Integer contractId,
			final Integer participantId) throws DAOException {

		int numberOfCompletedLoanTransactions = 0;
		Integer threshold = null;
		try {
			threshold = LoanDefaults.getCompletedApolloLIThreshold();

			Connection apolloConnection = null;
			CallableStatement statement = null;
			try {
				apolloConnection = getDefaultConnection(CLASS_NAME,
						APOLLO_DATA_SOURCE_NAME);
			} catch (SQLException e) {
				throw new SystemException(e,
						"Problem occurred getting APOLLO connection");
			}

			try {
				int i = 1;
				statement = apolloConnection
						.prepareCall(SQL_SELECT_COMPLETED_LI_TRANSACTION);
				statement.setInt(i++, contractId);
				statement.setInt(i++, participantId);
				statement.setInt(i++, threshold);

				ResultSet rs = statement.executeQuery();
				if (rs.next()) {
					numberOfCompletedLoanTransactions = rs.getInt(1);
				}
			} finally {
				close(statement, apolloConnection);
			}
		} catch (Exception exception) {

			StringBuffer buff = new StringBuffer();
			buff.append("ApolloDao.getNumberOfCompletedLoanTransaction,");
			buff.append(" contractId: ");
			buff.append(contractId);
			buff.append(" participantId: ");
			buff.append(participantId);
			buff.append(" threshold: ");
			buff.append(exception.getMessage());
			DAOException daoException = new DAOException(buff.toString(),
					exception);

			throw daoException;
		}

		return numberOfCompletedLoanTransactions;
	}

	/**
	 * Retrieves the # of pending loan transactions on a Apollo for a given
	 * timeframe.
	 * 
	 * @param contractId
	 *            The contract ID.
	 * @param participantId
	 *            The participant ID.
	 * @return int The number of Pending Loan Transactions.
	 * @throws DAOException
	 *             If an exception occurs.
	 */
	public int getNumberOfPendingLoanTransaction(final Integer contractId,
			final Integer participantId) throws DAOException {

		int numberOfPendingLoanTransactions = 0;
		Integer threshold = null;
		try {
			threshold = LoanDefaults.getPendingApolloLIThreshold();
			Connection apolloConnection = null;
			CallableStatement statement = null;

			try {
				apolloConnection = getDefaultConnection(CLASS_NAME,
						APOLLO_DATA_SOURCE_NAME);
			} catch (SQLException e) {
				throw new SystemException(e,
						"Problem occurred getting APOLLO connection");
			}

			try {
				int i = 1;
				statement = apolloConnection
						.prepareCall(SQL_SELECT_PENDING_LI_TRANSACTION);
				statement.setInt(i++, contractId);
				statement.setInt(i++, participantId);
				statement.setInt(i++, threshold);

				ResultSet rs = statement.executeQuery();
				if (rs.next()) {
					numberOfPendingLoanTransactions = rs.getInt(1);
				}
			} finally {
				close(statement, apolloConnection);
			}
		} catch (Exception exception) {

			StringBuffer buff = new StringBuffer();
			buff.append("ApolloDao.getNumberOfPendingLoanTransaction,");
			buff.append(" contractId: ");
			buff.append(contractId);
			buff.append(" participantId: ");
			buff.append(participantId);
			buff.append(" Exception: ");
			buff.append(exception.getMessage());
			DAOException daoException = new DAOException(buff.toString(),
					exception);

			throw daoException;
		}

		return numberOfPendingLoanTransactions;
	}

}
