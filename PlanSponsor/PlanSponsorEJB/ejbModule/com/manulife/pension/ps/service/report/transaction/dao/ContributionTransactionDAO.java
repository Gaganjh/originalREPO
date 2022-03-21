package com.manulife.pension.ps.service.report.transaction.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.exception.ContractNumberDoesNotmatchException;
import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData.MoneyTypeAmount;
import com.manulife.pension.service.contract.valueobject.ParticipantVO;
import com.manulife.pension.service.report.dao.DirectSqlReportDAOHelper;
import com.manulife.pension.service.report.dao.ReportItemTransformer;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.dao.SqlPair;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.util.tools.MessageFormatHelper;

/**
 * @author Charles Chan
 */
public class ContributionTransactionDAO extends ReportServiceBaseDAO {

	private static final String MONEY_TYPE_CATEGORY_EMPLOYEE_CONTRIB = "EE";

	private static final String className = ContributionTransactionDAO.class
			.getName();

	private static final boolean DEBUG_SQL = false;

	/**
	 * A map of database column keyed by sort field.
	 */
	private static final Map SORT_MAP_FOR_PAGE_REPORT;
	private static final Map SORT_MAP_FOR_DOWNLOAD_REPORT;
	
	private static final ContributionDetailsItemOnPageTransformer STATELESS_TRANSFORMER_FOR_PAGE =
	    new ContributionDetailsItemOnPageTransformer();
	
	private static final Logger logger = Logger
			.getLogger(TransactionHistoryDAO.class);

	/**
	 * The substitutions used in all query strings.
	 */
	private static final Object[] QUERY_SUBSTITUTIONS = new Object[] { APOLLO_SCHEMA_NAME };

	private static final String TRANSACTION_DETAILS_SQL = getTransactionDetailsSql();

	private static final String CONTRIBUTION_SUMMARY_SQL = getContributionSummarySql();

	private static final String MONEY_TYPE_SQL = getMoneyTypeSql();

	private static final SqlPair CONTRIBUTION_DETAILS_BY_MONEY_TYPE_SQL = getContributionDetailsByMoneyTypeSql();

    private static final SqlPair CONTRIBUTION_DETAILS_BY_CATEGORY_SQL = getContributionDetailsByCategorySql();

	private static final String CONTRACT_HAS_TRANSACTION_SQL = getContractHasTransactionSql();

	private static interface Columns {
		
		String CONTRACT_NUMBER = "CNNO";

		String RATE_EFFECTIVE_DATE = "ALRDATE";

		String EMPLOYEE_CONTRIBUTION = "EETAMT";

		String EMPLOYER_CONTRIBUTION = "ERTAMT";

		String TOTAL_INDIVIDUAL_CONTRIBUTION = "PRTAMT";

		String PARTICIPANT_ID = "ALPRTID";

		String PARTICIPANT_FIRSTNAME = "PARTFST";

		String PARTICIPANT_LASTNAME = "PARTLST";

		String PARTICIPANT_SSN = "PARTSSN";

		String NUMBER_OF_PARTICIPANT = "NOOFPRT";

		String PAYROLL_ENDING_DATE = "PAYDATE";

		String TRANSACTION_TYPE = "ALTRTYP";

		String MONEY_TYPE_ID = "MLIMT";

		String MONEY_TYPE_CATEGORY = "MLIMTCAT";

		String MONEY_TYPE_AMOUNT = "MTAMT";

		String MONEY_TYPE_SHORT_NAME = "CONTMT";

		String MONEY_TYPE_LONG_NAME = "CNMMLONG";

		String TOTAL_EMPLOYEE_CONTRIBUTION = "EETOTAL";

		String TOTAL_EMPLOYER_CONTRIBUTION = "ERTOTAL";
	}
	
	private static final String DEFAULT_SORT_COLUMN = Columns.PARTICIPANT_SSN;

	static {
		/*
		 * Sets up the field to column map.
		 */
	    Map sortMap;
	    
	    sortMap = new HashMap();
		sortMap.put(
		        ContributionTransactionReportData.SORT_FIELD_EMPLOYEE_CONTRIBUTION,
				Columns.EMPLOYEE_CONTRIBUTION);
		sortMap.put(
				ContributionTransactionReportData.SORT_FIELD_EMPLOYER_CONTRIBUTION,
				Columns.EMPLOYER_CONTRIBUTION);
		sortMap.put(
		        ContributionTransactionReportData.SORT_FIELD_NAME,
				new String[] {
		                Columns.PARTICIPANT_LASTNAME,
						Columns.PARTICIPANT_FIRSTNAME });
		sortMap.put(
		        ContributionTransactionReportData.SORT_FIELD_SSN,
				Columns.PARTICIPANT_SSN);
		sortMap.put(
				ContributionTransactionReportData.SORT_FIELD_TOTAL_CONTRIBUTION,
				Columns.TOTAL_INDIVIDUAL_CONTRIBUTION);
		SORT_MAP_FOR_PAGE_REPORT = Collections.unmodifiableMap(sortMap);
		
		sortMap = new HashMap();
        sortMap.put(
                ContributionTransactionReportData.SORT_FIELD_EMPLOYEE_CONTRIBUTION,
                DEFAULT_SORT_COLUMN);
        sortMap.put(
                ContributionTransactionReportData.SORT_FIELD_EMPLOYER_CONTRIBUTION,
                DEFAULT_SORT_COLUMN);
        sortMap.put(
                ContributionTransactionReportData.SORT_FIELD_NAME,
                new String[] {
                        Columns.PARTICIPANT_LASTNAME,
                        Columns.PARTICIPANT_FIRSTNAME });
        sortMap.put(
                ContributionTransactionReportData.SORT_FIELD_SSN,
                Columns.PARTICIPANT_SSN);
        sortMap.put(
                ContributionTransactionReportData.SORT_FIELD_TOTAL_CONTRIBUTION,
                DEFAULT_SORT_COLUMN);
        SORT_MAP_FOR_DOWNLOAD_REPORT = Collections.unmodifiableMap(sortMap);
		
	}

	public static ContributionTransactionReportData getReportData(
			ReportCriteria criteria) throws SystemException,
			ReportServiceException {

		Connection connection = null;
		PreparedStatement stmt = null;

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		String transactionNumber = getTransactionNumber(criteria);
		Integer contractNumber = getContractNumber(criteria);
		ContributionTransactionReportData reportData = null;

		try {
			connection = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);

			reportData = new ContributionTransactionReportData();
			reportData.setReportCriteria(criteria);

        boolean isReportTypeDownload =
            ContributionTransactionReportData.FILTER_REPORT_TYPE_DOWNLOAD.equals(
                    (String) criteria.getFilterValue(
                            ContributionTransactionReportData.FILTER_REPORT_TYPE));
        

			reportData.setTransactionNumber(transactionNumber);

			populateTransactionDetails(connection, criteria, reportData);
			
			//The following change in the code is implemented for handling the book marking.
			//Check if the user contract number and database contract numbers are present.
			if(contractNumber==null||reportData.getContractNumber()==null)
 			{
 				logger.debug("Show 1047 error. There is no Contract.");
 				throw new ContractNumberDoesNotmatchException("Show 1047 error. There is no Contract.");
 			}	
			//Check if the user contract number matches with the database contract number.
 			else if((!contractNumber.toString().equals(reportData.getContractNumber()))||(contractNumber.intValue()==0)){
 				
 				logger.debug("Show 1047 error. There is no such record.");
 				throw new ContractNumberDoesNotmatchException("Show 1047 error. There is no Contract.");
 			}
 			else{
			
			populateContributionSummary(connection, criteria, reportData);
			populateMoneyTypes(connection, criteria, reportData);
			populateContributionItems(connection, criteria, reportData, isReportTypeDownload);

			reportData.setTotalCount(
			isReportTypeDownload
			? reportData.getDetails().size()
			: getTotalRecordCount(connection, criteria, CONTRIBUTION_DETAILS_BY_CATEGORY_SQL));
			
			if (logger.isDebugEnabled()) {
				logger.debug("exit -> getReportData");
			}
 			}
		} catch (SQLException e) {
			handleSqlException(e, className,
					"getReportData",
					"Something goes wrong with the statement - Contract ["
							+ contractNumber + "] TX [" + transactionNumber
							+ "]");
		} finally {
			close(stmt, connection);
		}

		return reportData;
	}

	/**
	 * Checks whether the given criteria's transaction number and its contract
	 * number matches.
	 *
	 * @param connection
	 *            The connection object to use.
	 * @param contractNumber
	 * @param transactionNumber
	 * @return
	 * @throws SQLException
	 */
	private static boolean contractHasTransaction(Connection connection,
			Integer contractNumber, String transactionNumber)
			throws SQLException {

		PreparedStatement stmt = connection
				.prepareStatement(CONTRACT_HAS_TRANSACTION_SQL);

		stmt.setString(1, transactionNumber);
		stmt.setInt(2, contractNumber.intValue());
		ResultSet rs = stmt.executeQuery();

		rs.next();
		int count = rs.getInt(1);

		return count == 1;
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
				.getFilterValue(ContributionTransactionReportData.FILTER_CONTRACT_NUMBER);
	}

	/**
	 * Gets the transaction number filter from the report criteria.
	 *
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The transaction number.
	 */
	private static String getTransactionNumber(ReportCriteria criteria) {
		return (String) criteria
				.getFilterValue(ContributionTransactionReportData.FILTER_TRANSACTION_NUMBER);
	}

	/**
	 * Populates transaction details, which includes transaction date,
	 * transaction type, payroll ending date, and number of unique participants.
	 *
	 * @param connection
	 *            The database connection to use.
	 * @param criteria
	 *            The report criteria to use.
	 * @param reportData
	 *            The report data to be populated.
	 * @throws SQLException
	 *             If there's an error when we execute the statement.
	 */
	private static void populateTransactionDetails(Connection connection,
			ReportCriteria criteria,
			ContributionTransactionReportData reportData) throws SQLException {

		if (logger.isDebugEnabled() && DEBUG_SQL) {
			logger.debug(TRANSACTION_DETAILS_SQL);
		}

		PreparedStatement stmt = null;

		String transactionNumber = getTransactionNumber(criteria);
		try {
			stmt = connection.prepareStatement(TRANSACTION_DETAILS_SQL);
			stmt.setString(1, transactionNumber);
			stmt.setString(2, transactionNumber);
			ResultSet rs = stmt.executeQuery();

			if(rs.next())
			{
				reportData.setContractNumber(rs
						.getString(Columns.CONTRACT_NUMBER));
				reportData.setTransactionDate(rs
						.getDate(Columns.RATE_EFFECTIVE_DATE));
				reportData.setTransactionType(getString(rs,
						Columns.TRANSACTION_TYPE));
				reportData.setPayrollEndingDate(rs
						.getDate(Columns.PAYROLL_ENDING_DATE));
				reportData.setNumberOfParticipants(rs
						.getInt(Columns.NUMBER_OF_PARTICIPANT));
			}

		} finally {
			close(stmt, null);
		}
	}

	/**
	 * Populates contribution summary, which includes total employee
	 * contribution, total employer contribution, and total contribution.
	 *
	 * @param connection
	 *            The database connection to use.
	 * @param criteria
	 *            The report criteria to use.
	 * @param reportData
	 *            The report data to be populated.
	 * @throws SQLException
	 *             If there's an error when we execute the statement.
	 */
	private static void populateContributionSummary(Connection connection,
			ReportCriteria criteria,
			ContributionTransactionReportData reportData) throws SQLException {
	    
		if (logger.isDebugEnabled() && DEBUG_SQL) {
			logger.debug(CONTRIBUTION_SUMMARY_SQL);
		}
		
		PreparedStatement stmt = null;

		try {
			stmt = connection.prepareStatement(CONTRIBUTION_SUMMARY_SQL);

			String transactionNumber = getTransactionNumber(criteria);
			stmt.setString(1, transactionNumber);
			ResultSet rs = stmt.executeQuery();

			if(rs.next())
			{

				reportData.setTotalEmployeeContribution(getBigDecimalOrZero(rs,
						Columns.TOTAL_EMPLOYEE_CONTRIBUTION));
				reportData.setTotalEmployerContribution(getBigDecimalOrZero(rs,
						Columns.TOTAL_EMPLOYER_CONTRIBUTION));
			}
		} finally {
			close(stmt, null);
		}
    		
	}

	/**
	 * Populates money types, which includes the money type description and the
	 * associated amount.
	 *
	 * @param connection
	 *            The database connection to use.
	 * @param criteria
	 *            The report criteria to use.
	 * @param reportData
	 *            The report data to be populated.
	 * @throws SQLException
	 *             If there's an error when we execute the statement.
	 */
	private static void populateMoneyTypes(Connection connection,
			ReportCriteria criteria,
			ContributionTransactionReportData reportData) throws SQLException {

		if (logger.isDebugEnabled() && DEBUG_SQL) {
			logger.debug(MONEY_TYPE_SQL);
		}

		PreparedStatement stmt = null;

		try {
			stmt = connection.prepareStatement(MONEY_TYPE_SQL);
			stmt.setString(1, getTransactionNumber(criteria));
			ResultSet rs = stmt.executeQuery();

			boolean hasEmployerContribution = false;
			boolean hasEmployeeContribution = false;

			while (rs.next()) {
				String moneyTypeLongDescription = getString(rs,
						Columns.MONEY_TYPE_LONG_NAME);
				String moneyTypeShortDescription = getString(rs,
				        Columns.MONEY_TYPE_SHORT_NAME);
				BigDecimal moneyTypeAmount = getBigDecimalOrZero(rs,
						Columns.MONEY_TYPE_AMOUNT);
				String moneyTypeCategory = getString(rs,
						Columns.MONEY_TYPE_CATEGORY);
				String moneyTypeId = getString(rs,
				        Columns.MONEY_TYPE_ID);
				
				boolean employeeContribution = moneyTypeCategory
						.equals(MONEY_TYPE_CATEGORY_EMPLOYEE_CONTRIB);

				if (!hasEmployerContribution && !employeeContribution) {
					hasEmployerContribution = true;
					reportData.setHasEmployerContribution(true);
				}

				if (!hasEmployeeContribution && employeeContribution) {
					hasEmployeeContribution = true;
					reportData.setHasEmployeeContribution(true);
				}

				reportData.getMoneyTypes().add(
						new MoneyTypeAmount(
						        moneyTypeId,
						        moneyTypeShortDescription,
						        moneyTypeLongDescription,
                                employeeContribution,
						        moneyTypeAmount));
			}
		} finally {
			close(stmt, null);
		}

	}

	/**
	 * Gets the total number of contribution items.
	 *
	 * @param connection
	 *            The database connection to use.
	 * @param criteria
	 *            The report criteria to use.
	 * @throws SQLException
	 *             If there's an error when we execute the statement.
	 */
	private static int getTotalRecordCount(Connection connection,
			ReportCriteria criteria, SqlPair query) throws SQLException, SystemException {

		if (logger.isDebugEnabled() && DEBUG_SQL) {
			logger.debug(query.getCountQuery());
		}

		PreparedStatement stmt = null;
		int totalCount = 0;

		try {
			stmt = connection.prepareStatement(query.getCountQuery());

			for (int parameterCount = 1; parameterCount <= 2; parameterCount++) {
				stmt.setString(parameterCount, getTransactionNumber(criteria));
			}
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				totalCount += rs.getInt(1);
			}
		} finally {
			close(stmt, null);
		}

		return totalCount;
	}

	/**
	 * Populates the contribution items.
	 *
	 * @param connection
	 *            The database connection to use.
	 * @param criteria
	 *            The report criteria to use.
	 * @param reportData
	 *            The report data to be populated.
	 * @throws SQLException
	 *             If there's an error when we execute the statement.
	 */
	private static void populateContributionItems(Connection connection,
			ReportCriteria criteria,
			ContributionTransactionReportData reportData,
			boolean isReportTypeDownload) throws SQLException,
			SystemException {
	    
	    StringBuilder queryBuilder = new StringBuilder();
	    
        queryBuilder.append(
                isReportTypeDownload
                ? CONTRIBUTION_DETAILS_BY_MONEY_TYPE_SQL.getQuery()
                : CONTRIBUTION_DETAILS_BY_CATEGORY_SQL.getQuery());
        
		/*
		 * Appends the ORDER BY clause
		 */
		queryBuilder.append(" ORDER BY ");
		if (criteria.getSorts().size() > 0) {
		    queryBuilder.append(
		            criteria.getSortPhrase(
		                    isReportTypeDownload
		                    ? SORT_MAP_FOR_DOWNLOAD_REPORT
		                    : SORT_MAP_FOR_PAGE_REPORT))
		            .toString();
		} else {
		    queryBuilder.append(DEFAULT_SORT_COLUMN);
		}
		
		String query = queryBuilder.toString();

		if (logger.isDebugEnabled() && DEBUG_SQL) {
			logger.debug(query);
		}

		PreparedStatement stmt = null;

		try {
			stmt = connection.prepareStatement(query);

			for (int parameterCount = 1; parameterCount <= 2; parameterCount++) {
				stmt.setString(parameterCount, getTransactionNumber(criteria));
			}
			ResultSet rs = stmt.executeQuery();

			List items = DirectSqlReportDAOHelper.getReportItems(
			        criteria,
			        rs,
					isReportTypeDownload
					? new ContributionDetailsItemInDownloadTransformer()
			        : STATELESS_TRANSFORMER_FOR_PAGE);

			reportData.setDetails(items);
		} finally {
			close(stmt, null);
		}
	}

	private static String getContractHasTransactionSql() {
		SqlStringBuffer sql = new SqlStringBuffer()
				.append("SELECT                                               ")
				.append("      COUNT(*) from {0}.VLP7222                      ")
				.append("WHERE TRANNO = ? AND CNNO = ?                        ");
		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);
		return query;
	}

	private static String getTransactionDetailsSql() {
		SqlStringBuffer sql = new SqlStringBuffer()
				.append("SELECT	ALTRAN.CNNO,  								")
				.append("	SUM(ALTRAN.NUMPRTS) AS NOOFPRT,					")
				.append("	ALTRAN.ALTRTYP,									")
				.append("	ALTRAN.ALRDATE,									")
				.append("	ALTRAN.PAYDATE									")
				.append("	FROM  (											")
				.append("SELECT COALESCE(NONIP.CNNO, IPTRANS.CNNO)AS CNNO,  ")
				.append("  COUNT(DISTINCT(                                    ")
				.append("  COALESCE(NONIP.PRTID ,IPTRANS.PRTID))) AS NUMPRTS, ")
				.append("  COALESCE(NONIP.TRTYP ,IPTRANS.TRTYP2) AS ALTRTYP,  ")
				.append("  COALESCE(NONIP.RTEFDT,IPTRANS.RTEFDT) AS ALRDATE,  ")
				.append("  COALESCE(NONIP.CTRIBADT,IPTRANS.CTRIBADT) AS PAYDATE")
				.append("  FROM  (                                            ")
				.append("SELECT   BB.CNNO,                                    ")
				.append("        FF.PRTID,                                    ")
				.append("        CC.TRTYP2,                                   ")
				.append("        BB.RTEFDT,                                   ")
				.append("        BB.CTRIBADT,                                 ")
				.append("        CASE  WHEN GG.MLIMTCAT = 'EE'                ")
				.append("               THEN FF.FIAMT                         ")
				.append("               ELSE DEC(0.00,11,0)                   ")
				.append("        END AS EEPAMT   ,                            ")
				.append("        CASE  WHEN GG.MLIMTCAT = 'ER'                ")
				.append("               THEN     FF.FIAMT                     ")
				.append("               ELSE DEC(0.00,11,0)                   ")
				.append("        END AS ERPAMT                                ")
				.append("  FROM  {0}.VLP1143 CC                               ")
				.append("       ,{0}.VLP1015 BB                               ")
				.append("       ,{0}.VLP1070 FF                               ")
				.append("       ,{0}.VLP1031 GG                               ")
				.append(" WHERE  CC.TRANNO2    = ?                            ")
				.append(" AND    CC.TRANNO1    = BB.TRANNO                    ")
				.append(" AND    CC.TRTYP1     = 'WD'                         ")
				.append(" AND    BB.TRANNO     = FF.TRANNO                    ")
				.append(" AND    BB.PROPNO     = GG.PROPNO                    ")
				.append(" AND    FF.MLIMT      = GG.MLIMT                     ")
				.append(" GROUP BY   BB.CNNO,                                 ")
				.append("        FF.PRTID   ,                                 ")
				.append("        CC.TRTYP2  ,                                 ")
				.append("        BB.RTEFDT  ,                                 ")
				.append("        BB.CTRIBADT,                                 ")
				.append("        FF.FIAMT   ,                                 ")
				.append("        GG.MLIMTCAT ) AS IPTRANS                     ")
				.append("       FULL OUTER JOIN                               ")
				.append("         ( SELECT  B.CNNO,                                  ")
				.append("        D.PRTID   ,                                  ")
				.append("     B.TRTYP   ,                                     ")
				.append("     B.RTEFDT  ,                                     ")
				.append("     F.CTRIBADT,                                     ")
				.append("     CASE  WHEN C.MLIMTCAT = 'EE'                    ")
				.append("            THEN D.GROCNTRB                          ")
				.append("            ELSE DEC(0.00,11,0)                      ")
				.append("     END AS EEGROCA,                                 ")
				.append("     CASE  WHEN C.MLIMTCAT = 'EE'                    ")
				.append("            THEN     D.NETEARNG                      ")
				.append("            ELSE DEC(0.00,11,0)                      ")
				.append("     END AS EENETEN,                                 ")
				.append("     CASE  WHEN C.MLIMTCAT = 'ER'                    ")
				.append("            THEN     D.GROCNTRB                      ")
				.append("            ELSE DEC(0.00,11,0)                      ")
				.append("     END AS ERGROCA,                                 ")
				.append("     CASE  WHEN C.MLIMTCAT = 'ER'                    ")
				.append("            THEN     D.NETEARNG                      ")
				.append("            ELSE DEC(0.00,11,0)                      ")
				.append("     END AS ERNETEN                                  ")
				.append(" FROM  {0}.VLP7222 B                                 ")
				.append("      ,{0}.VLP1031 C                                 ")
				.append("      ,{0}.VLP1077 D                                 ")
				.append("      ,{0}.VLP7226 F                                 ")
				.append("      ,{0}.VLP1014 G                                 ")
				.append("WHERE  B.TRANNO   =  ?                               ")
				.append("AND    B.TXAMT    <> 0.00                            ")
				.append("AND    C.MLIMT    =  D.MLIMT                         ")
				.append("AND    B.TRANNO   =  D.TRANNO                        ")
				.append("AND    B.TRANNO   =  F.TRANNO                        ")
				.append("AND    B.CNNO     =  G.CNNO                          ")
				.append("AND    G.PROPNO   =  C.PROPNO                        ")
				.append("AND    D.ALOCAMT  <> 0.00                            ")
				.append("AND    D.ALOCSTAT =  'OK'                            ")
				.append("GROUP BY  B.CNNO ,                                            ")
				.append("       D.PRTID   ,                                   ")
				.append("       B.TRTYP   ,                                   ")
				.append("       B.RTEFDT  ,                                   ")
				.append("       F.CTRIBADT,                                   ")
				.append("       C.MLIMTCAT,                                   ")
				.append("       D.GROCNTRB,                                   ")
				.append("       D.NETEARNG ) AS NONIP                         ")
				.append("ON IPTRANS.PRTID = NONIP.PRTID                       ")
				.append("GROUP BY  COALESCE(NONIP.CNNO,  IPTRANS.CNNO) ,      ")
				.append(" NONIP.PRTID ,										  ")
				.append(" IPTRANS.PRTID ,									  ")
				.append(" NONIP.TRTYP ,                                       ")
				.append(" IPTRANS.TRTYP2,                                     ")
				.append(" NONIP.RTEFDT,                                       ")
				.append(" IPTRANS.RTEFDT,                                     ")
				.append(" NONIP.CTRIBADT,                                     ")
				.append(" IPTRANS.CTRIBADT                                    ")
				.append(") ALTRAN											  ")
				.append("GROUP BY	ALTRAN.CNNO,										  ")
				.append(" ALTRAN.NUMPRTS,				 					")
				.append(" ALTRAN.ALTRTYP,									")
				.append(" ALTRAN.ALRDATE,									")
				.append(" ALTRAN.PAYDATE									")
				.append("WITH UR											");

		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);

		return query;
	}

	private static String getContributionSummarySql() {
		SqlStringBuffer sql = new SqlStringBuffer()
				.append("SELECT                                               ")
				.append("  SUM(EEERAMT.EEPAMT                 ) AS EETOTAL,   ")
				.append("  SUM(EEERAMT.ERPAMT                 ) AS ERTOTAL,   ")
				.append("  SUM(EEERAMT.EEPAMT + EEERAMT.ERPAMT) AS TOTALAMT   ")
				.append("FROM (SELECT                                         ")
				.append("  CASE  WHEN  D.MLIMTCAT = 'EE'                    ")
				.append("        THEN SUM(C.ALLOCAMT + C.NEGALAMT)            ")
				.append("        ELSE DEC(0.00,11,0)                          ")
				.append("  END AS EEPAMT   ,                                  ")
				.append("  CASE  WHEN  D.MLIMTCAT = 'ER'                    ")
				.append("        THEN SUM(C.ALLOCAMT + C.NEGALAMT)            ")
				.append("        ELSE DEC(0.00,11,0)                          ")
				.append("  END AS ERPAMT                                      ")
				.append("       ,SUM(C.ALLOCAMT + C.NEGALAMT) AS TOTAMT       ")
				.append("  FROM  {0}.VLP7224 C                                ")
				.append("       ,{0}.VLP1027 D                                ")
				.append(" WHERE  C.TRANNO     = ?                             ")
				.append(" AND    C.MLIMT    = D.MLIMT                         ")
				.append("GROUP BY                                             ")
				.append("  D.MLIMTCAT ) EEERAMT                               ");
		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);

		return query;
	}

	private static String getMoneyTypeSql() {
		SqlStringBuffer sql = new SqlStringBuffer()
				.append("SELECT                                               ")
				.append("        D.MLIMTCAT                                   ")
				.append("       ,D.CNMMLONG                                   ")
				.append("       ,D.CNMT                        AS CONTMT      ")
				.append("       ,D.MLIMT                                      ")
				.append("       ,SUM(C.ALLOCAMT  + C.NEGALAMT) AS MTAMT       ")
				.append("  FROM  {0}.VLP7224 C                                ")
				.append("       ,{0}.VLP1031 D                                ")
				.append("       ,{0}.VLP1014 E                                ")
				.append("       ,{0}.VLP7222 F                                ")
				.append(" WHERE  F.TRANNO     = ?                             ")
				.append(" AND    F.TRANNO     = C.TRANNO                      ")
				.append(" AND    F.CNNO       = E.CNNO                        ")
				.append(" AND    C.MLIMT      = D.MLIMT                       ")
				.append(" AND    E.PROPNO    =  D.PROPNO                      ")
				.append(" AND    D.STARTDTE <=  F.TREFDT                      ")
				.append(" AND    D.ENDDTE   >   F.TREFDT                      ")
				.append(" GROUP BY                                            ")
				.append("   D.MLIMTCAT,                                       ")
				.append("   D.CNMMLONG,                                       ")
                .append("   D.CNMT,											  ")
				.append("   D.MLIMT                                           ");
		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);
		return query;
	}

	
    private static SqlPair getContributionDetailsByCategorySql() {
        
        SqlStringBuffer sql = new SqlStringBuffer()
                .append("SELECT                                               ") // Common Log 73132
                .append("    PRTID   AS ALPRTID,                              ")
                .append("    RTEFDT  AS ALRDATE,                              ")
                .append("    SUM(COALESCE(EEPAMT,0.00) +                      ")
                .append("      COALESCE(EEGROCA,0.00) +                       ")
                .append("      COALESCE(EENETEN,0.00)) AS EETAMT,             ")
                .append("    SUM(COALESCE(ERPAMT,0.00) +                      ")
                .append("      COALESCE(ERGROCA,0.00) +                       ")
                .append("      COALESCE(ERNETEN,0.00)) AS ERTAMT,             ")
                .append("    SUM(COALESCE(EEPAMT,0.00) +                      ")
                .append("      COALESCE(EEGROCA,0.00) +                       ")
                .append("      COALESCE(EENETEN,0.00) +                       ")
                .append("      COALESCE(ERPAMT,0.00) +                        ")
                .append("      COALESCE(ERGROCA,0.00) +                       ")
                .append("      COALESCE(ERNETEN,0.00)) AS PRTAMT,             ")
                .append("    PRTSSN  AS PARTSSN,                              ")
                .append("    PRTLSTNM AS PARTLST,                             ")
                .append("    PRTFSTNM               AS PARTFST                ")
                .append("    FROM  (                                          ")
                .append("  SELECT                                             ")
                .append("          FF.PRTID,                                  ")
                .append("          BB.RTEFDT,                                 ")
                .append("          CASE  WHEN GG.MLIMTCAT = 'EE'              ")
                .append("                 THEN SUM(FF.FIAMT)                  ")
                .append("                 ELSE DEC(0.00,11,0)                 ")
                .append("          END AS EEPAMT   ,                          ")
                .append("          DEC(0.00,11,0) AS EEGROCA,                 ")
                .append("          DEC(0.00,11,0) AS EENETEN,                 ")
                .append("          CASE  WHEN GG.MLIMTCAT = 'ER'              ")
                .append("                 THEN SUM(FF.FIAMT)                  ")
                .append("                 ELSE DEC(0.00,11,0)                 ")
                .append("          END AS ERPAMT   ,                          ")
                .append("          DEC(0.00,11,0) AS ERGROCA,                 ")
                .append("          DEC(0.00,11,0) AS ERNETEN,                 ")
                .append("          EE.PRTSSN,                                 ")
                .append("          EE.PRTLSTNM,                               ")
                .append("          EE.PRTFSTNM                                ")
                .append("    FROM  {0}.VLP1143 CC                             ")
                .append("         ,{0}.VLP1066 BB                             ")
                .append("         ,{0}.VLP1074 EE                             ")
                .append("         ,{0}.VLP1070 FF                             ")
                .append("         ,{0}.VLP1031 GG                             ")
                .append("   WHERE  CC.TRANNO2    = ?                          ")
                .append("   AND    CC.TRTYP1     = 'WD'                       ")
                .append("   AND    CC.TRANNO1    = BB.TRANNO                  ")
                .append("   AND    BB.TRANNO     = FF.TRANNO                  ")
                .append("   AND    BB.PROPNO     = GG.PROPNO                  ")
                .append("   AND    FF.MLIMT      = GG.MLIMT                   ")
                .append("  AND    GG.STARTDTE    <= BB.RTEFDT                 ")
                .append("  AND    GG.ENDDTE      > BB.RTEFDT                  ")
                .append("   AND    BB.PRTID      = EE.PRTID                   ")
                .append("   GROUP BY                                          ")
                .append("          FF.PRTID   ,                               ")
                .append("          BB.RTEFDT  ,                               ")
                .append("          GG.MLIMTCAT,                               ")
                .append("          EE.PRTSSN  ,                               ")
                .append("          EE.PRTLSTNM,                               ")
                .append("          EE.PRTFSTNM                                ")
                .append("   UNION ALL                                         ")
                .append("   SELECT                                            ")
                .append("          D.PRTID   ,                                ")                
                .append("          B.RTEFDT  ,                                ")
                .append("          DEC(0.00,11,0) AS EEPAMT ,                 ")
                .append("         CASE  WHEN C.MLIMTCAT = 'EE'                ")
                .append("                THEN SUM(D.GROCNTRB)                 ")
                .append("                ELSE DEC(0.00,11,0)                  ")
                .append("         END AS EEGROCA,                             ")
                .append("         CASE  WHEN C.MLIMTCAT = 'EE'                ")
                .append("                THEN SUM(D.NETEARNG)                 ")
                .append("                ELSE DEC(0.00,11,0)                  ")
                .append("         END AS EENETEN,                             ")
                .append("         DEC(0.00,11,0) AS ERPAMT ,                  ")                
                .append("         CASE  WHEN C.MLIMTCAT = 'ER'                ")
                .append("                THEN SUM(D.GROCNTRB)                 ")
                .append("                ELSE DEC(0.00,11,0)                  ")
                .append("         END AS ERGROCA,                             ")
                .append("         CASE  WHEN C.MLIMTCAT = 'ER'                ")
                .append("                THEN SUM(D.NETEARNG)                 ")
                .append("                ELSE DEC(0.00,11,0)                  ")
                .append("         END AS ERNETEN,                             ")
                .append("         E.PRTSSN,                                   ")
                .append("         E.PRTLSTNM,                                 ")
                .append("         E.PRTFSTNM                                  ")
                .append("    FROM  {0}.VLP7222 B                              ")
                .append("         ,{0}.VLP1031 C                              ")
                .append("         ,{0}.VLP1077 D                              ")
                .append("         ,{0}.VLP1074 E                              ")
                .append("         ,{0}.VLP1014 F                              ")
                .append("   WHERE  B.TRANNO   =  ?                            ")
                .append("   AND    B.TXAMT    <> 0.00                         ")
                .append("   AND    C.MLIMT    =  D.MLIMT                      ")
                .append("   AND    C.STARTDTE <= B.RTEFDT                     ")
                .append("   AND    C.ENDDTE   >  B.RTEFDT                     ")
                .append("   AND    B.TRANNO   =  D.TRANNO                     ")
                .append("   AND    B.CNNO     =  F.CNNO                       ")
                .append("   AND    F.PROPNO   =  C.PROPNO                     ")
                .append("   AND    D.ALOCAMT  <> 0.00                         ")
                .append("   AND    D.ALOCSTAT =  'OK'                         ")
                .append("   AND    D.PRTID    =  E.PRTID                      ")
                .append("   GROUP BY                                          ")
                .append("          D.PRTID   ,                                ")
                .append("          B.RTEFDT  ,                                ")
                .append("          C.MLIMTCAT,                                ")
                .append("         E.PRTSSN  ,                                 ")
                .append("         E.PRTLSTNM,                                 ")
                .append("         E.PRTFSTNM ) TOT                            ")
                .append("  GROUP BY                                           ")
                .append("          TOT.PRTID,                                 ")
                .append("          TOT.RTEFDT,                                ")
                .append("          TOT.PRTSSN,                                ")  
                .append("          TOT.PRTLSTNM,                              ")
                .append("          TOT.PRTFSTNM                               ");

        String query = MessageFormatHelper.format(sql.toString(),
                QUERY_SUBSTITUTIONS);

		String countQuery = new SqlStringBuffer(" SELECT COUNT(*) FROM (")
				.append(query).append(") AS AL_TEMP").toString();
    
		return new SqlPair(countQuery, query);
	}

	private static SqlPair getContributionDetailsByMoneyTypeSql() {
		SqlStringBuffer sql = new SqlStringBuffer()
		        .append("SELECT                                               ")
		        .append("   TOT.PRTID              AS ALPRTID                 ")
		        .append("  ,TOT.RTEFDT             AS ALRDATE                 ")
		        .append("  ,TOT.CNMT               AS CONTMT                  ")
		        .append("  ,TOT.MLIMT              AS MLIMT                   ")
		        .append("  ,TOT.MLIMTCAT           AS MLIMTCAT                ")
		        .append("  ,SUM(TOT.IPAMT + TOT.ALAMT) AS MTAMT               ")
		        .append("  ,TOT.PRTSSN             AS PARTSSN                 ")
		        .append("  ,TOT.PRTLSTNM           AS PARTLST                 ")
		        .append("  ,TOT.PRTFSTNM           AS PARTFST                 ")
		        .append("FROM                                                 ")
		        .append("  (                                                  ")
		        .append("    SELECT                                           ")
		        .append("       FF.PRTID                                      ")
		        .append("      ,BB.RTEFDT                                     ")
		        .append("      ,GG.CNMT                                       ")
		        .append("      ,GG.MLIMT                                      ")
		        .append("      ,GG.MLIMTCAT                                   ")
		        .append("      ,SUM(FF.FIAMT)  AS IPAMT                       ")
		        .append("      ,DEC(0.00,11,0) AS ALAMT                       ")
		        .append("      ,EE.PRTSSN                                     ")
		        .append("      ,EE.PRTLSTNM                                   ")
		        .append("      ,EE.PRTFSTNM                                   ")
		        .append("    FROM                                             ")
		        .append("       {0}.VLP1143 CC                                ")
		        .append("      ,{0}.VLP1066 BB                                ")
		        .append("      ,{0}.VLP1074 EE                                ")
		        .append("      ,{0}.VLP1070 FF                                ")
		        .append("      ,{0}.VLP1031 GG                                ")
		        .append("    WHERE                                            ")
		        .append("          CC.TRANNO2    =  ?                         ")
		        .append("      AND CC.TRTYP1     =  'WD'                      ")
		        .append("      AND CC.TRANNO1    =  BB.TRANNO                 ")
		        .append("      AND BB.TRANNO     =  FF.TRANNO                 ")
		        .append("      AND BB.PROPNO     =  GG.PROPNO                 ")
		        .append("      AND FF.MLIMT      =  GG.MLIMT                  ")
		        .append("      AND GG.STARTDTE   <= BB.RTEFDT                 ")
		        .append("      AND GG.ENDDTE     >  BB.RTEFDT                 ")
		        .append("      AND BB.PRTID      =  EE.PRTID                  ")
		        .append("    GROUP BY                                         ")
		        .append("       FF.PRTID                                      ")
		        .append("      ,BB.RTEFDT                                     ")
		        .append("      ,GG.CNMT                                       ")
		        .append("      ,GG.MLIMT                                      ")
		        .append("      ,GG.MLIMTCAT                                   ")
		        .append("      ,EE.PRTSSN                                     ")
		        .append("      ,EE.PRTLSTNM                                   ")
		        .append("      ,EE.PRTFSTNM                                   ")
		        .append("    UNION ALL                                        ")
		        .append("    SELECT                                           ")
		        .append("       D.PRTID                                       ")
		        .append("      ,B.RTEFDT                                      ")
		        .append("      ,C.CNMT                                        ")
		        .append("      ,C.MLIMT                                       ")
		        .append("      ,C.MLIMTCAT                                    ")
		        .append("      ,DEC(0.00,11,0)          AS IPAMT              ")
		        .append("      ,D.GROCNTRB + D.NETEARNG AS ALAMT              ")
		        .append("      ,E.PRTSSN                                      ")
		        .append("      ,E.PRTLSTNM                                    ")
		        .append("      ,E.PRTFSTNM                                    ")
		        .append("    FROM                                             ")
		        .append("       {0}.VLP7222 B                                 ")
		        .append("      ,{0}.VLP1031 C                                 ")
		        .append("      ,{0}.VLP1077 D                                 ")
		        .append("      ,{0}.VLP1074 E                                 ")
		        .append("      ,{0}.VLP1014 F                                 ")
		        .append("    WHERE                                            ")
		        .append("          B.TRANNO     =  ?                          ")
		        .append("      AND B.TXAMT      <> 0.00                       ")
		        .append("      AND C.MLIMT      =  D.MLIMT                    ")
		        .append("      AND C.STARTDTE   <= B.RTEFDT                   ")
		        .append("      AND C.ENDDTE     >  B.RTEFDT                   ")
		        .append("      AND B.TRANNO     =  D.TRANNO                   ")
		        .append("      AND B.CNNO       =  F.CNNO                     ")
		        .append("      AND F.PROPNO     =  C.PROPNO                   ")
		        .append("      AND D.ALOCAMT    <> 0.00                       ")
		        .append("      AND D.ALOCSTAT   =  'OK'                       ")
		        .append("      AND D.PRTID      =  E.PRTID                    ")
		        .append("  ) TOT                                              ")
		        .append("GROUP BY                                             ")
		        .append("   TOT.PRTID                                         ")
		        .append("  ,TOT.RTEFDT                                        ")
		        .append("  ,TOT.CNMT                                          ")
		        .append("  ,TOT.MLIMT                                         ")
		        .append("  ,TOT.MLIMTCAT                                      ")
		        .append("  ,TOT.PRTSSN                                        ")
		        .append("  ,TOT.PRTLSTNM                                      ")
		        .append("  ,TOT.PRTFSTNM                                      ");

        String query = MessageFormatHelper.format(sql.toString(),
                QUERY_SUBSTITUTIONS);

        String countQuery = new SqlStringBuffer(" SELECT COUNT(*) FROM (")
                .append(query).append(") AS AL_TEMP").toString();
    
        return new SqlPair(countQuery, query);
        
	}

	/**
	 * This inner class transforms a result set into a ContributionDetailsItem
	 * object.
	 */
	private static class ContributionDetailsItemOnPageTransformer extends
			ReportItemTransformer {

		public Object transform(ResultSet rs) throws SQLException {
			ContributionTransactionItem item = new ContributionTransactionItem();
			int participantId = rs.getInt(Columns.PARTICIPANT_ID);
			item.getParticipant().setId(new Integer(participantId));
			item.getParticipant()
					.setSsn(getString(rs, Columns.PARTICIPANT_SSN));
			item.getParticipant().setLastName(
					getString(rs, Columns.PARTICIPANT_LASTNAME));
			item.getParticipant().setFirstName(
					getString(rs, Columns.PARTICIPANT_FIRSTNAME));
			item.setEmployeeContribution(getBigDecimalOrZero(rs,
					Columns.EMPLOYEE_CONTRIBUTION));
			item.setEmployerContribution(getBigDecimalOrZero(rs,
					Columns.EMPLOYER_CONTRIBUTION));
			item.setTotalContribution(getBigDecimalOrZero(rs,
					Columns.TOTAL_INDIVIDUAL_CONTRIBUTION));
			return item;
		}
	}
	
	/**
	 * This inner class transforms a result set into a ContributionDetailsItem
	 * object.
	 */
	private static class ContributionDetailsItemInDownloadTransformer extends
			ReportItemTransformer {

	    private ContributionTransactionItem item;
	    
		public Object transform(ResultSet rs) throws SQLException {
		    
		    boolean isSameItem = true;
		    
			int participantId = rs.getInt(Columns.PARTICIPANT_ID);
			if (item == null
			        || item.getParticipant().getId() != participantId) {
			    
			    item = new ContributionTransactionItem();
                ParticipantVO participant = item.getParticipant();
                
	            participant.setId(
	                    new Integer(participantId));
	            participant.setSsn(
	                    getString(rs, Columns.PARTICIPANT_SSN));
	            participant.setLastName(
	                    getString(rs, Columns.PARTICIPANT_LASTNAME));
	            participant.setFirstName(
	                    getString(rs, Columns.PARTICIPANT_FIRSTNAME));
	            
	            isSameItem = false;
	            
			}
			
			item.addAllocation(
			        getString(rs, Columns.MONEY_TYPE_ID),
			        MONEY_TYPE_CATEGORY_EMPLOYEE_CONTRIB.equals(
			                getString(rs, Columns.MONEY_TYPE_CATEGORY)),
			        getBigDecimalOrZero(rs, Columns.MONEY_TYPE_AMOUNT));
			
			return isSameItem ? null : item;
			
		}
	}
}