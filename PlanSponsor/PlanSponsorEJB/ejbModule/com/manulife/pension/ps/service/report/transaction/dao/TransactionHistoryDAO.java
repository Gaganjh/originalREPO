package com.manulife.pension.ps.service.report.transaction.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionType;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData;
import com.manulife.pension.service.report.dao.DirectSqlReportDAOHelper;
import com.manulife.pension.service.report.dao.ReportItemTransformer;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.dao.SqlPair;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.util.tools.MessageFormatHelper;

/**
 * A DAO to retrieve transaction history items.
 * 
 * @author Charles Chan
 */
public class TransactionHistoryDAO extends ReportServiceBaseDAO {

	private static final String className = TransactionHistoryDAO.class
			.getName();

	/**
	 * A map of SQLs keyed by TransactionType.
	 */
	private static final Map queriesMap = new HashMap();

	/**
	 * A map of database column keyed by sort field.
	 */
	private static final Map fieldToColumnMap = new HashMap();

	/**
	 * Name of the transaction effective date column in the result set. It is
	 * used to construct the ORDER BY clause.
	 */
	private static final String TRANSACTION_DATE = "RTEFDT";

	/**
	 * Name of the transaction amount column in the result set. It is used to
	 * construct the ORDER BY clause.
	 */
	private static final String TRANSACTION_AMOUNT = "TXNAMT";

	/**
	 * Name of the transaction number column in the result set. It is used to
	 * construct the ORDER BY clause.
	 */
	private static final String TRANSACTION_NUMBER = "TRANNO";

	/**
	 * The completed status code for a transaction.
	 */
	private static final String COMPLETED_STATUS = "CM";

	/** Transaction Item Transformer */
	private static final TransactionHistoryItemTransformer transformer = new TransactionHistoryItemTransformer();

	/**
	 * The substitutions used in all query strings.
	 */
	private static final Object[] QUERY_SUBSTITUTIONS = new Object[] {
			APOLLO_SCHEMA_NAME, COMPLETED_STATUS };

	private static final Logger logger = Logger
			.getLogger(TransactionHistoryDAO.class);

	private static final String CONTRACT_HAS_LOANS_SQL;

	static {
		/*
		 * Sets up a map of queries, keyed on transaction type.
		 */
		queriesMap.put(TransactionType.ALLOCATION, getAllocationSql());
		queriesMap.put(TransactionType.LOAN_REPAYMENT, getLoanRepaymentSql());
		queriesMap.put(TransactionType.LOAN_ISSUE, getLoanIssueSql());
		queriesMap.put(TransactionType.LOAN_TRANSFER, getLoanTransferSql());
		queriesMap.put(TransactionType.INTER_ACCOUNT_TRANSFER,
				getInterAccountTransferSql());
		queriesMap.put(TransactionType.MATURITY_REINVESTMENT,
				getMaturityReinvestmentSql());
		queriesMap.put(TransactionType.WITHDRAWAL, getWithdrawalSql());
		queriesMap.put(TransactionType.ADJUSTMENT, getAdjustmentSql());
		queriesMap.put(TransactionType.LOAN_DEFAULT, getLoanDefaultSql());

		CONTRACT_HAS_LOANS_SQL = getContractHasLoansSql();

		/*
		 * Sets up the field to column map.
		 */
		fieldToColumnMap.put(TransactionHistoryReportData.SORT_FIELD_DATE,
				TRANSACTION_DATE);
		fieldToColumnMap.put(TransactionHistoryReportData.SORT_FIELD_NUMBER,
				TRANSACTION_NUMBER);
		fieldToColumnMap.put(TransactionHistoryReportData.SORT_FIELD_AMOUNT,
				TRANSACTION_AMOUNT);
	}

	/**
	 * Constructor. This call has only static methods.
	 */
	private TransactionHistoryDAO() {
	}

	/**
	 * Gets the contract number filter from the report criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The contract number.
	 */
	private static Integer getContractNumber(ReportCriteria criteria) {
		return (Integer) criteria
				.getFilterValue(TransactionHistoryReportData.FILTER_CONTRACT_NUMBER);
	}

	/**
	 * Gets the FROM date filter as SQL Date from the report criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The FROM date.
	 */
	private static java.sql.Date getFromDate(ReportCriteria criteria) {
		java.util.Date fromDate = (java.util.Date) criteria.getFilterValue(TransactionHistoryReportData.FILTER_FROM_DATE);
		java.sql.Date rv = null;
		if ( fromDate != null ) {
			rv = new java.sql.Date(fromDate.getTime());
		} 
		return rv;
	}

	/**
	 * Gets the TO date filter as SQL Date from the report criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The TO date.
	 */
	private static java.sql.Date getToDate(ReportCriteria criteria) {
		java.util.Date toDate = (java.util.Date) criteria.getFilterValue(TransactionHistoryReportData.FILTER_TO_DATE);
		java.sql.Date rv = null;
		if ( toDate != null ) {
			rv = new java.sql.Date(toDate.getTime());
		} 
		return rv;
	}

	/**
	 * Gets the types filter from the report criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The types collection.
	 */
	private static Collection getTypes(ReportCriteria criteria) {
		return (Collection) criteria
				.getFilterValue(TransactionHistoryReportData.FILTER_TYPE_LIST);
	}

	/**
	 * Prepare a statement to be executed. It can either prepare a statement to
	 * count the number of records or a statement to return the complete result
	 * set.
	 * 
	 * @param connection
	 *            The connection object to use to prepare the statement.
	 * @param criteria
	 *            The report criteria used to setup the WHERE clause and the
	 *            ORDER by clause.
	 * @param countOnly
	 *            If true, returns a statement that only counts the number of
	 *            records. If false, the complete result set is returned.
	 * @throws SQLException
	 *             If it fails to prepare the statement.
	 * @return PreparedStatement The newly prepared statement.
	 */
	private static PreparedStatement getPreparedStatement(
			Connection connection, ReportCriteria criteria, boolean countOnly)
			throws SQLException, SystemException {

		StringBuffer query = new StringBuffer();
		Collection types = getTypes(criteria);

		/*
		 * UNION all the queries for the different types.
		 */
		for (Iterator it = types.iterator(); it.hasNext();) {
			String type = (String) it.next();
			try {
				SqlPair pair = (SqlPair) queriesMap.get(type);
				if (countOnly) {
					query.append(pair.getCountQuery());
				} else {
					query.append(pair.getQuery());
				}
				if (it.hasNext()) {
					query.append(" UNION ALL ");
				}
			} catch (NullPointerException e) {
				throw new SystemException(e, TransactionHistoryDAO.class
						.getName(), "getPreparedStatement",
						"parameters were: type= " + type + "; queriesMap="
								+ queriesMap);
			}
		}

		/*
		 * Appends the ORDER BY clause when there is any sorting requirement.
		 */
		if (!countOnly && criteria.getSorts().size() > 0) {
			query.append(" ORDER BY ").append(
					criteria.getSortPhrase(fieldToColumnMap));
		}

		PreparedStatement stmt = connection.prepareStatement(query.toString());

		/*
		 * Adds filter into the WHERE clause.
		 */
		setParameters(stmt, criteria, types.size());

		return stmt;
	}

	/**
	 * Gets the total record count for the given report criteria.
	 * 
	 * @param connection
	 *            The Connection object to use to obtain the total record count.
	 * @param criteria
	 *            The ReportCriteria to use.
	 * @return The total record count.
	 * @throws SQLException
	 * @throws SystemException
	 */
	private static int getTotalRecordCount(Connection connection,
			ReportCriteria criteria) throws SQLException, SystemException {

		PreparedStatement stmt = null;
		int totalCount = 0;
		try { 
			/*
			 * Prepare a statement for counting.
			 */
			stmt = getPreparedStatement(connection, criteria,
					true);
			ResultSet rs = stmt.executeQuery();
	
			/*
			 * Typically, we only have one record but in the case when we have a
			 * UNION of all types, we will have multiple records. We should add them
			 * all up to get the total record count.
			 */
			while (rs.next()) {
				totalCount += rs.getInt(1);
			}

		} finally {
			close(stmt, null);
		}

		return totalCount;
	}

	/**
	 * Populates the given report data and the list of items using the specified
	 * report criteria.
	 * 
	 * @param criteria
	 *            The report criteria to use.
	 * @param reportData
	 *            The report data to populate. Only the total count is updated
	 * @param dataItems
	 *            A list of TransactionDataItem. Transformation to
	 *            TransactionHistoryItem is done at the handler level.
	 * @throws SystemException
	 *             If anything goes wrong with the stored proc.
	 */
	public static void getReportData(ReportCriteria criteria,
			TransactionHistoryReportData reportData, List dataItems)
			throws SystemException, ReportServiceException {

		Connection connection = null;
		PreparedStatement stmt = null;

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		try {
			connection = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);
			/*
			 * Tries to obtain the total record count.
			 */
			int totalCount = getTotalRecordCount(connection, criteria);
			if (logger.isDebugEnabled()) {
				logger.debug("Total Count [" + totalCount + "]");
			}

			stmt = getPreparedStatement(connection, criteria, false);

			ResultSet rs = stmt.executeQuery();

			List items = DirectSqlReportDAOHelper.getReportItems(criteria, rs,
					transformer);

			dataItems.addAll(items);

			reportData.setTotalCount(totalCount);

		} catch (SQLException e) {
			handleSqlException(e, className,
					"getReportData",
					"Something went wrong while executing the statement - fromDate ["
							+ getFromDate(criteria) + "] toDate ["
							+ getToDate(criteria) + "] contract ["
							+ getContractNumber(criteria) + "]");
		} finally {
			close(stmt, connection);
		}
		logger.debug("exit <- getReportData");
	}
	
	public static boolean hasLoans(ReportCriteria criteria)
			throws SystemException, ReportServiceException {
		Connection connection = null;
		PreparedStatement stmt = null;
		boolean result = false;

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> hasLoans");
		}

		try {
			connection = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);

			// determine if the contract has any loan transactions
			stmt = connection.prepareStatement(CONTRACT_HAS_LOANS_SQL);
			setParameters(stmt, criteria, 3);
			ResultSet rs = stmt.executeQuery();
			result = rs.next();
			rs.close();
		} catch (SQLException e) {
			handleSqlException(e, className,
					"hasLoans",
					"Something went wrong while executing the statement - "
							+ criteria.toString() + "]");
		} finally {
			close(stmt, connection);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- hasLoans");
		}

		return result;
	}

	private static void setParameters(PreparedStatement stmt,
			ReportCriteria criteria, int count) throws SQLException {

		java.sql.Date fromDate = getFromDate(criteria);
		java.sql.Date toDate = getToDate(criteria);
		Integer contractNumber = getContractNumber(criteria);
		int parameterCount = 1;

		if (logger.isDebugEnabled()) {
			logger.debug(" Contract [" + contractNumber + "]");
			logger.debug("From Date [" + fromDate + "]");
			logger.debug("  To Date [" + toDate + "]");
		}

		for (int i = 0; i < count; i++) {
			stmt.setInt(parameterCount++, contractNumber.intValue());
			stmt.setDate(parameterCount++, fromDate);
			stmt.setDate(parameterCount++, toDate);
		}
	}

	private static SqlPair getAdjustmentSql() {
		SqlStringBuffer sql = new SqlStringBuffer()
				.append("SELECT  A.RTEFDT,                                    ")
				.append("        A.TRTYP,                                     ")
				.append("        A.TRRSNCD ,                                  ")
				.append("        A.PRTID,                                     ")
				.append("        A.CTRIBADT,                                  ")
				.append("        SUM(B.FIAMT) AS TXNAMT,                      ")
				.append("        A.TRANMODE,                                  ")
				.append("        A.TRANNO,                                    ")
				.append("        SUBSTR('     ',1,5) AS MLIMS,                ")
				.append("        SUBSTR(' ',1,1) AS XFERPRIN,                 ")
				.append("        E.PRTSSN		 AS PART_SSN,                 ")
				.append("        E.PRTLSTNM		 AS PART_LAST_NAME,           ")
				.append("        E.PRTFSTNM		 AS PART_FIRST_NAME           ")
				.append("  FROM  {0}.VLP1015 A                                ")
				.append("       ,{0}.VLP1070 B                                ")
				.append("       ,{0}.VLP1074 E                                ")
				.append("  WHERE  CNNO       = ?                              ")
				.append("  AND    A.PRTID    = E.PRTID                        ")
				.append("  AND    A.TRANNO    = B.TRANNO                      ")
				.append("  AND    A.PROPNO   > 0                              ")
				.append("  AND    A.TRTYP    = 'AD'                           ")
				.append("  AND    B.TRSUBTYP IN ('GC', 'NE')                  ")
				.append("  AND    A.TRSTATCD = '{1}'                          ")
				.append("  AND    A.RTEFDT BETWEEN ?                          ")
				.append("                  AND     ?                          ")
				.append("  GROUP BY A.RTEFDT,                     			  ")
				.append("     A.TRTYP,                         				  ").append(
						"     A.TRRSNCD,			                          ").append(
						"     A.PRTID  ,			                          ").append(
						"     A.CTRIBADT,			                          ")
				.append("     A.TRANMODE,			                          ")
				.append("     A.TRANNO,				                          ").append(
						"     E.PRTSSN, 			                          ").append(
						"     E.PRTLSTNM,			                          ")
				.append("     E.PRTFSTNM   		                        	  ");

		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);

		String countQuery = new SqlStringBuffer(" SELECT COUNT(*) FROM (")
				.append(query).append(") AS AD_TEMP").toString();

		return new SqlPair(countQuery, query);
	}

	private static SqlPair getLoanDefaultSql() {
		SqlStringBuffer sql = new SqlStringBuffer()
				.append("SELECT  A.RTEFDT,                                    ")
				.append("        A.TRTYP,                                     ")
				.append("        A.TRRSNCD ,                                  ")
				.append("        A.PRTID,                                     ")
				.append("        A.CTRIBADT,                                  ")
				.append("        A.TXAMT AS TXNAMT,                           ")
				.append("        A.TRANMODE,                                  ")
				.append("        A.TRANNO,                                    ")
				.append("        SUBSTR('     ',1,5) AS MLIMS,                ")
				.append("        SUBSTR(' ',1,1) XFERPRIN,                    ")
				.append("        E.PRTSSN        AS PART_SSN,                 ")
				.append("        E.PRTLSTNM      AS PART_LAST_NAME,           ")
				.append("        E.PRTFSTNM      AS PART_FIRST_NAME           ")
				.append("  FROM  {0}.VLP1015 A                                ")
				.append("       ,{0}.VLP1074 E                                ")
				.append(" WHERE  CNNO       = ?                               ")
				.append(" AND    A.PRTID    = E.PRTID                         ")
				.append(" AND    A.TRTYP    = 'LD'                            ")
				.append(" AND    A.TRSTATCD = '{1}'                           ")
				.append(" AND    A.TRANMODE = 'F'                             ")
				.append(" AND    A.RTEFDT BETWEEN ?                           ")
				.append("                 AND     ?                           ")
				.append("AND NOT EXISTS                                       ")
				.append("(SELECT *                                            ")
				.append("  FROM {0}.VLP1143 V1143                             ")
				.append("  WHERE V1143.ASSORSN =  'RFULL'                     ")
				.append("  AND V1143.TRTYP2 = 'LD'                            ")
				.append("  AND     A.TRANNO = V1143.TRANNO2                   ") // 1 CommonLog 70338
                .append("  AND EXISTS                                         ")                            
                .append("  (SELECT 1                                          ")                               
                .append("   FROM {0}.VLP1015 R                                ")           
                .append("   WHERE V1143.TRANNO1 = R.TRANNO                    ")     
                .append("   AND R.TRSTATCD = 'CM'))                           ");     


		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);

		String countQuery = new SqlStringBuffer(" SELECT COUNT(*) FROM (")
				.append(query).append(") AS LD_TEMP").toString();

		return new SqlPair(countQuery, query);
	}

	private static SqlPair getAllocationSql() {
		SqlStringBuffer sql = new SqlStringBuffer()
				.append("SELECT  V7222_A.RTEFDT,                              ")
				.append("        V7222_A.TRTYP,                               ")
				.append("        V7222_A.TRRSNCD ,                            ")
				.append("        DEC(0,9,0) AS PRTID,                         ")
				.append("        V7226_A.CTRIBADT,                            ")
				.append("        CASE WHEN V7222_A.TRRSNCD = 'IP'             ")
				.append("         THEN (V7222_A.TXAMT + SUM(V7224_A.NEGALAMT))")
				.append("         ELSE V7222_A.TXAMT                          ")
				.append("        END                   AS TXNAMT,             ")
				.append("        V7222_A.TRANMODE,                            ")
				.append("        V7222_A.TRANNO,                              ")
				.append("        V7222_A.MLIMS                                ")
				.append("     ,  SUBSTR(' ',1,1) XFERPRIN                     ")
				.append("     ,  CHAR('000000000')   AS PART_SSN              ")
				.append("     ,  CHAR(' ',20)        AS PART_LAST_NAME        ")
				.append("     ,  CHAR(' ',20)        AS PART_FIRST_NAME       ")
				.append("  FROM  {0}.VLP7222 V7222_A                          ")
				.append("       ,{0}.VLP7224 V7224_A                          ")
				.append("       ,{0}.VLP7226 V7226_A                          ")
				.append(" WHERE  V7222_A.CNNO = ?                             ")
				.append(" AND    V7222_A.TRSTATCD = '{1}'                     ")
				.append(" AND    V7222_A.TRTYP = 'AL'                         ")
				.append(" AND    V7222_A.TRANNO = V7224_A.TRANNO              ")
				.append(" AND    V7222_A.TRANNO = V7226_A.TRANNO              ")
				.append(" AND    V7222_A.RTEFDT BETWEEN ?                     ")
				.append("                       AND     ?                     ")
				.append(" GROUP BY V7222_A.RTEFDT,                            ")
				.append("          V7222_A.TRTYP,                             ")
				.append("          V7222_A.TRRSNCD,                           ")
				.append("          V7226_A.CTRIBADT,                          ")
				.append("          V7222_A.TXAMT,                             ")
				.append("          V7222_A.TRANMODE,                          ")
				.append("          V7222_A.TRANNO,                            ")
				.append("          V7222_A.MLIMS                             ");

		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);

		String countQuery = new SqlStringBuffer(" SELECT COUNT(*) FROM (")
				.append(query).append(") AS AL_TEMP").toString();

		return new SqlPair(countQuery, query);
	}

	private static SqlPair getLoanIssueSql() {
		SqlStringBuffer sql = new SqlStringBuffer()
				.append("SELECT                                               ")
				.append("        A.RTEFDT,                                    ")
				.append("        A.TRTYP,                                     ")
				.append("        A.TRRSNCD ,                                  ")
				.append("        A.PRTID,                                     ")
				.append("        A.CTRIBADT,                                  ")
				.append("        (SUM(B.FIAMT)*-1) AS TXNAMT,                 ")
				.append("        A.TRANMODE,                                  ")
				.append("        A.TRANNO,                                    ")
				.append("        CHAR(' ',5)       AS MLIMS,                  ")
				.append("        SUBSTR(' ',1,1) XFERPRIN,                    ")
				.append("        E.PRTSSN          AS PART_SSN,               ")
				.append("        E.PRTLSTNM        AS PART_LAST_NAME,         ")
				.append("        E.PRTFSTNM        AS PART_FIRST_NAME         ")
				.append("  FROM   {0}.VLP1015 A                               ")
				.append("        ,{0}.VLP1070 B                               ")
				.append("        ,{0}.VLP1074 E                               ")
				.append("  WHERE  CNNO       = ?                              ")
				.append("  AND    A.TRANNO   = B.TRANNO                       ")
				.append("  AND    A.PRTID    = E.PRTID                        ")
				.append("  AND    B.FIAMT    < 0                              ")
				.append("  AND    A.TRTYP    = 'LI'                           ")
				.append("  AND    A.TRSTATCD = 'CM'                           ")
				.append("  AND    A.TRANMODE = 'F'                            ")
				.append("  AND    A.RTEFDT BETWEEN ?                          ")
				.append("                  AND     ?                          ")
				.append(" AND NOT EXISTS                                      ")
				.append(" (SELECT *                                           ")
				.append(" FROM {0}.VLP1143 T1143                              ")
				.append(" WHERE T1143.ASSORSN =  'RFULL'                      ")
				.append(" AND T1143.TRTYP2 = 'LI'                             ")
				.append(" AND     A.TRANNO = T1143.TRANNO2                    ") // 2 CommonLog 70338
                .append("  AND EXISTS                                         ")                            
                .append("  (SELECT 1                                          ")                               
                .append("   FROM {0}.VLP1015 R                                ")           
                .append("   WHERE T1143.TRANNO1 = R.TRANNO                    ")     
                .append("   AND R.TRSTATCD = 'CM'))                           ")     				
				.append(" AND    'Y'        = 'Y'                             ")
				.append("           GROUP BY A.RTEFDT,                        ")
				.append(" A.TRTYP,                                            ")
				.append(" A.TRRSNCD,                                          ")
				.append(" A.PRTID,                                            ")
				.append(" A.CTRIBADT,                                         ")
				.append(" A.TRANMODE,                                         ")
				.append(" A.TRANNO,                                           ")
				.append(" E.PRTSSN,                                           ")
				.append(" E.PRTLSTNM,                                         ")
				.append(" E.PRTFSTNM                                         ");

		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);

		String countQuery = new SqlStringBuffer(" SELECT COUNT(*) FROM (")
				.append(query).append(") AS LI_TEMP").toString();

		return new SqlPair(countQuery, query);
	}
	
	private static SqlPair getLoanTransferSql() {
		SqlStringBuffer sql = new SqlStringBuffer()
		.append("		SELECT A.RTEFDT								")
		.append("			,A.TRTYP								")
		.append("			,A.TRRSNCD								")
		.append("			,A.PRTID								")
		.append("			,A.CTRIBADT								")
		.append("			,A.TXAMT AS TXNAMT						")
		.append("			,A.TRANMODE								")
		.append("			,A.TRANNO								")
		.append("			,CHAR(' ', 5) AS MLIMS					")
		.append("			,SUBSTR(' ', 1, 1) XFERPRIN				")
		.append("			,E.PRTSSN AS PART_SSN					")
		.append("			,E.PRTLSTNM AS PART_LAST_NAME			")
		.append("			,E.PRTFSTNM AS PART_FIRST_NAME			")
		.append("		FROM {0}.VLP1015 A						")
		.append("			,{0}.VLP1157 B						")
		.append("			,{0}.VLP1074 E						")
		.append("		WHERE A.CNNO = ?							")
		.append("		AND A.PRTID = E.PRTID						") 
		.append("		AND A.TRANNO = B.TRANNO						")
		.append("		AND A.TRTYP = 'LT'							")
		.append("		AND A.TRSTATCD = 'CM'						") 
		.append("		AND A.TRANMODE = 'F' 						")
		.append("		AND A.RTEFDT BETWEEN ? AND ? 				")
		.append("		AND NOT EXISTS (							")
		.append("				SELECT *							")
		.append("				FROM {0}.VLP1143 V1143			")
		.append("				WHERE V1143.ASSORSN = 'RFULL'		")
		.append("				AND V1143.TRTYP2 = 'LT' 			")
		.append("				AND A.TRANNO = V1143.TRANNO2		")
		.append("				AND EXISTS (SELECT 1				")
		.append("						FROM {0}.VLP1015 R		")
		.append("						WHERE V1143.TRANNO1 = R.TRANNO AND R.TRSTATCD = 'CM'))");

		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);

		String countQuery = new SqlStringBuffer(" SELECT COUNT(*) FROM (")
				.append(query).append(") AS LT_TEMP").toString();

		return new SqlPair(countQuery, query);
	}

	private static SqlPair getLoanRepaymentSql() {
		SqlStringBuffer sql = new SqlStringBuffer()
				.append("SELECT  V7222_F.RTEFDT,                              ")
				.append("        V7222_F.TRTYP,                               ")
				.append("        V7222_F.TRRSNCD ,                            ")
				.append("        DEC(0,9,0)                AS PARTID,         ")
				.append("        DATE('0001-01-01')      AS CTRIBDT,          ")
				.append("        V7222_F.TXAMT             AS TXNAMT,         ")
				.append("        V7222_F.TRANMODE,                            ")
				.append("        V7222_F.TRANNO,                              ")
				.append("        V7222_F.MLIMS                                ")
				.append("      , SUBSTR(' ',1,1) XFERPRIN                     ")
				.append("      , CHAR('000000000')       AS PART_SSN          ")
				.append("      , CHAR(' ',20)            AS PART_LAST_NAME    ")
				.append("      , CHAR(' ',20)            AS PART_FIRST_NAME   ")
				.append("  FROM  {0}.VLP7222 V7222_F                          ")
				.append(" WHERE  V7222_F.CNNO  = ?                            ")
				.append(" AND    V7222_F.TRTYP = 'LR'                         ")
				.append(" AND    V7222_F.TRSTATCD = '{1}'                     ")
				.append(" AND    V7222_F.RTEFDT BETWEEN ?                     ")
				.append("                       AND     ?                    ");

		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);

		String countQuery = new SqlStringBuffer(" SELECT COUNT(*) FROM (")
				.append(query).append(") AS LR_TEMP").toString();

		return new SqlPair(countQuery, query);
	}

	private static SqlPair getMaturityReinvestmentSql() {
		SqlStringBuffer sql = new SqlStringBuffer()
				.append("SELECT  V1015_D.RTEFDT,                              ")
				.append("        V1015_D.TRTYP,                               ")
				.append("        V1015_D.TRRSNCD ,                            ")
				.append("        V1070_D.PRTID,                               ")
				.append("        V1015_D.CTRIBADT,                            ")
				.append("        (SUM(V1070_D.FIAMT) * -1) AS TXNAMT,         ")
				.append("        V1015_D.TRANMODE,                            ")
				.append("        V1015_D.TRANNO,                              ")
				.append("        CHAR(' ',5) AS MLIMS                         ")
				.append("      , SUBSTR(' ',1,1) XFERPRIN                     ")
				.append("      , V1074_D.PRTSSN             AS PART_SSN       ")
				.append("      , V1074_D.PRTLSTNM           AS PART_LAST_NAME ")
				.append("      , V1074_D.PRTFSTNM           AS PART_FIRST_NAME")
				.append("  FROM  {0}.VLP1015 V1015_D                          ")
				.append("       ,{0}.VLP1070 V1070_D                          ")
				.append("       ,{0}.VLP1074 V1074_D                          ")
				.append(" WHERE  V1015_D.CNNO     = ?                         ")
				.append(" AND    V1015_D.TRSTATCD = '{1}'                     ")
				.append(" AND    V1015_D.TRTYP = 'MR'                         ")
				.append(" AND    V1015_D.TRANMODE = 'F'                       ")
				.append(" AND    V1015_D.TRANNO   = V1070_D.TRANNO            ")
				.append(" AND    V1070_D.TRSUBTYP = 'OUT'                     ")
				.append(" AND    V1070_D.PRTID    = V1074_D.PRTID             ")
				.append(" AND    V1015_D.RTEFDT BETWEEN ?                     ")
				.append("                       AND     ?                     ")
				.append(" AND NOT EXISTS                                      ")
				.append("    (SELECT *                                        ")
				.append("     FROM  {0}.VLP1143 V1143_D                       ")
				.append("     WHERE V1143_D.ASSORSN = 'RFULL'                 ")
				.append("       AND V1015_D.TRANNO = V1143_D.TRANNO2          ")
				.append("       AND V1143_D.TRTYP2 = 'MR'                     ") // 3 CommonLog 70338
                .append("  AND EXISTS                                         ")                            
                .append("  (SELECT 1                                          ")                               
                .append("   FROM {0}.VLP1015 R                                ")           
                .append("   WHERE V1143_D.TRANNO1 = R.TRANNO                  ")     
                .append("   AND R.TRSTATCD = 'CM'))                           ")     				
				.append("GROUP BY V1015_D.RTEFDT,                             ")
				.append("         V1015_D.TRTYP,                              ")
				.append("         V1015_D.TRRSNCD,                            ")
				.append("         V1070_D.PRTID,                              ")
				.append("         V1015_D.CTRIBADT,                           ")
				.append("         V1015_D.TRANMODE,                           ")
				.append("         V1015_D.TRANNO,                             ")
				.append("         V1074_D.PRTSSN,                             ")
				.append("         V1074_D.PRTLSTNM,                           ")
				.append("         V1074_D.PRTFSTNM                           ");

		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);

		String countQuery = new SqlStringBuffer(" SELECT COUNT(*) FROM (")
				.append(query).append(") AS MR_TEMP").toString();

		return new SqlPair(countQuery, query);
	}

	private static SqlPair getWithdrawalSql() {
		SqlStringBuffer sql = new SqlStringBuffer()
				.append("SELECT                                               ")
				.append("        A.RTEFDT,                                    ")
				.append("        A.TRTYP,                                     ")
				.append("        A.TRRSNCD ,                                  ")
				.append("        A.PRTID,                                     ")
				.append("        A.CTRIBADT,                                  ")
				.append("        (SUM(A.TXAMT)*-1) AS TXNAMT,                 ")
				.append("        A.TRANMODE,                                  ")
				.append("        A.TRANNO,                                    ")
				.append("        CHAR(' ',5)       AS MLIMS,                  ")
				.append("        SUBSTR(' ',1,1) XFERPRIN,                    ")
				.append("        E.PRTSSN          AS PART_SSN,               ")
				.append("        E.PRTLSTNM        AS PART_LAST_NAME,         ")
				.append("        E.PRTFSTNM        AS PART_FIRST_NAME         ")
				.append("  FROM   {0}.VLP1015 A                               ")
				.append("        ,{0}.VLP1074 E                               ")
				.append(" WHERE  CNNO       =  ?                              ")
				.append(" AND    A.PRTID    =  E.PRTID                        ")
				.append(" AND    A.TRTYP    =  'WD'                           ")
				.append(" AND    A.TRSTATCD =  'CM'                           ")
				.append(" AND    A.TRANMODE =  'F'                            ")
				.append(" AND    A.RTEFDT BETWEEN ?                           ")
				.append("                 AND     ?                           ")
				.append("AND NOT EXISTS                                       ")
				.append("(SELECT *                                            ")
				.append("FROM {0}.VLP1143 T1143                               ")
				.append("WHERE T1143.ASSORSN =  'RFULL'                       ")
				.append("AND T1143.TRTYP2 = 'WD'                              ")
				.append("AND     A.TRANNO = T1143.TRANNO2                     ") // 4 CommonLog 70338
                .append("  AND EXISTS                                         ")                            
                .append("  (SELECT 1                                          ")                               
                .append("   FROM {0}.VLP1015 R                                ")           
                .append("   WHERE T1143.TRANNO1 = R.TRANNO                    ")     
                .append("   AND R.TRSTATCD = 'CM'))                           ")     				
				.append("AND NOT EXISTS                                       ")
				.append("(SELECT *                                            ")
				.append("FROM {0}.VLP1015 B                                   ")
				.append("WHERE B.TRTYP   =  'WD'                              ")
				.append("AND B.TRRSNCD   =  'IP'                              ")
				.append("AND B.RPTRTYP   =  'AL'                              ")
				.append("AND A.TRANNO    = B.TRANNO)                          ")
				.append("AND    'Y'        = 'Y'                              ")
				.append("GROUP BY A.RTEFDT,                                   ")
				.append("  A.TRTYP,                                           ")
				.append("  A.TRRSNCD,                                         ")
				.append("  A.PRTID,                                           ")
				.append("  A.CTRIBADT,                                        ")
				.append("  A.TRANMODE,                                        ")
				.append("  A.TRANNO,                                          ")
				.append("  E.PRTSSN,                                          ")
				.append("  E.PRTLSTNM,                                        ")
				.append("  E.PRTFSTNM                                        ");

		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);

		String countQuery = new SqlStringBuffer(" SELECT COUNT(*) FROM (")
				.append(query).append(") AS WD_TEMP").toString();

		return new SqlPair(countQuery, query);
	}

	private static SqlPair getInterAccountTransferSql() {
		SqlStringBuffer sql = new SqlStringBuffer()
				.append("SELECT  V1015_B.RTEFDT,                              ")
				.append("        V1015_B.TRTYP,                               ")
				.append("        V1015_B.TRRSNCD,                             ")
				.append("        V1015_B.PRTID,                               ")
				.append("        V1015_B.CTRIBADT,                            ")
				.append("        (SUM(V1070_B.FIAMT) * -1 ) AS TXNAMT,        ")
				.append("        V1015_B.TRANMODE,                            ")
				.append("        V1015_B.TRANNO,                              ")
				.append("        CHAR(' ',5)              AS MLIMS            ")
				.append("      , V1015_B.XFERPRIN                             ")				
				.append("      , V1074_B.PRTSSN             AS PART_SSN       ")
				.append("      , V1074_B.PRTLSTNM           AS PART_LAST_NAME ")
				.append("      , V1074_B.PRTFSTNM           AS PART_FIRST_NAME")
				.append("  FROM  {0}.VLP1015 V1015_B                          ")
				.append("       ,{0}.VLP1070 V1070_B                          ")
				.append("       ,{0}.VLP1074 V1074_B                          ")
				.append(" WHERE  V1015_B.CNNO     = ?                         ")
				.append(" AND    V1015_B.TRSTATCD = '{1}'                     ")
				.append(" AND    V1015_B.TRANNO = V1070_B.TRANNO              ")
				.append(" AND    V1015_B.PRTID  = V1074_B.PRTID               ")
				.append(" AND    V1015_B.TRTYP = 'IT'                         ")
				.append(" AND    V1070_B.TRSUBTYP IN ('TA','MV','RF')         ")
				.append(" AND    V1015_B.TRRSNCD <> 'CC'                      ")
				.append(" AND    V1015_B.TRANMODE = 'F'                       ")
				.append(" AND    V1070_B.FIAMT < 0.00                         ")
				.append(" AND    V1015_B.RTEFDT BETWEEN ?                     ")
				.append("                       AND     ?                     ")
				.append(" AND NOT EXISTS                                      ")
				.append("     (SELECT *                                       ")
				.append("      FROM {0}.VLP1143 V1143_B                       ")
				.append("      WHERE V1143_B.ASSORSN =  'RFULL'               ")
				.append("        AND V1015_B.TRANNO  = V1143_B.TRANNO2        ")
				.append("        AND V1143_B.TRTYP2  = 'IT'                   ") // 5 CommonLog 70338
                .append("      AND EXISTS                                     ")                            
                .append("      (SELECT 1                                      ")                               
                .append("       FROM {0}.VLP1015 R                            ")           
                .append("       WHERE V1143_B.TRANNO1 = R.TRANNO              ")     
                .append("       AND R.TRSTATCD = 'CM'))                       ")				
				.append(" GROUP BY V1015_B.RTEFDT                             ")
				.append("      , V1015_B.TRTYP                                ")
				.append("      , V1015_B.TRRSNCD                              ")
				.append("      , V1015_B.PRTID                                ")
				.append("      , V1015_B.CTRIBADT                             ")
				.append("      , V1015_B.TRANMODE                             ")
				.append("      , V1015_B.TRANNO                               ")
				.append("      , V1074_B.PRTSSN                               ")
				.append("      , V1074_B.PRTLSTNM                             ")
				.append("      , V1074_B.PRTFSTNM                             ")
				.append("      , V1015_B.XFERPRIN                             ");

		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);

		String countQuery = new SqlStringBuffer(" SELECT COUNT(*) FROM (")
				.append(query).append(") AS IT_TEMP").toString();

		return new SqlPair(countQuery, query);
	}

	private static String getContractHasLoansSql() {
		SqlStringBuffer sql = new SqlStringBuffer()
				.append("SELECT  1		                             	      ")
				.append("FROM  {0}.VLP7222 V7222_F                		      ")
				.append("WHERE  V7222_F.CNNO  = ?                 		      ")
				.append("AND    V7222_F.TRTYP = 'LR'                 	      ")
				.append("AND    V7222_F.TRSTATCD = '{1}'             	      ")
				.append("AND    V7222_F.RTEFDT BETWEEN  ?  				      ")
				.append("                 AND    ?         				      ")
				.append("UNION	                            		          ")
				.append("SELECT  1		                               	      ")
				.append("FROM  {0}.VLP1015 V1015_C                  	      ")
				.append(",{0}.VLP1155 V1155_C                       	      ")
				.append("WHERE V1015_C.CNNO     = ?                 	      ")
				.append("AND   V1015_C.TRSTATCD = '{1}'                       ")
				.append("AND   V1015_C.TRTYP    = 'LI'                        ")
				.append("AND   V1155_C.TRSUBTYP = 'LI'                        ")
				.append("AND   V1015_C.TRANMODE = 'F'                         ")
				.append("AND   V1015_C.RTEFDT BETWEEN  ?     			      ")
				.append("                 AND      ?         			      ")
				.append("AND   V1015_C.TRANNO   = V1155_C.TRANNO              ")
				.append("UNION	                            			      ")
				.append("SELECT  1  		                                  ")
				.append("FROM  {0}.VLP1015 A                        	      ")
				.append("WHERE  CNNO       = ?                      	      ")
				.append("AND    A.TRTYP    = 'LD'                             ")
				.append("AND    A.TRSTATCD = '{1}'                            ")
				.append("AND    A.TRANMODE = 'F'                              ")
				.append("AND    A.RTEFDT BETWEEN ?           			      ")
				.append("                 AND     ?						     ");

		return MessageFormatHelper.format(sql.toString(), QUERY_SUBSTITUTIONS);
	}

	private static class TransactionHistoryItemTransformer extends
			ReportItemTransformer {

		public Object transform(ResultSet rs) throws SQLException {
			int outCount = 1;

			TransactionDataItem item = new TransactionDataItem();

			/*
			 * All the transactions returned by the stored proc. are completed.
			 */
			item.setTransactionStatusCode(COMPLETED_STATUS);

			item.setTransactionEffectiveDate(rs.getDate(outCount++));
			item.setTransactionType(getString(rs, outCount++));
			item.setTransactionReasonCode(getString(rs, outCount++));
			int participantId = rs.getInt(outCount++);
			item.getParticipant().setId(new Integer(participantId));
			item.setPayrollEndingDate(rs.getDate(outCount++));
			item.setTransactionAmount(rs.getBigDecimal(outCount++));
			item.setTransactionMode(getString(rs, outCount++));
			item.setTransactionNumber(getString(rs, outCount++));
			item.setMoneySource(getString(rs, outCount++));
			item.setTransferInProtocolCode(getString(rs, outCount++));
			item.getParticipant().setSsn(getString(rs, outCount++));
			item.getParticipant().setLastName(getString(rs, outCount++));
			item.getParticipant().setFirstName(getString(rs, outCount++));

			return item;
		}
	}
}