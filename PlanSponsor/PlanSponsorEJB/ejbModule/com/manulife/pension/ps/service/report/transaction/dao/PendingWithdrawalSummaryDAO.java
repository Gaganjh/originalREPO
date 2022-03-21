package com.manulife.pension.ps.service.report.transaction.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionType;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.service.report.dao.DirectSqlReportDAOHelper;
import com.manulife.pension.service.report.dao.ReportItemTransformer;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.dao.SqlPair;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.participant.transaction.valueobject.PendingWithdrawalSummaryReportData;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalGeneralInfoVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalPayeePaymentVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.util.tools.MessageFormatHelper;

/**
 * DAO class to populate the Pending Withdrawal Summary Page
 * 
 * @author Puttaiah Arugunta
 *
 */
public class PendingWithdrawalSummaryDAO extends ReportServiceBaseDAO {

	/**
	 * Class name
	 */
	private static final String className = 
		PendingWithdrawalSummaryDAO.class.getName();
	
	/**
	 * Logger object
	 */
	private static final Logger logger = 
		Logger.getLogger(PendingWithdrawalSummaryDAO.class);
	
	/**
	 * A map of database column keyed by sort field.
	 */
	private static final Map<String, String> fieldToColumnMap = 
		new HashMap<String, String>();

	/**
	 * Name of the transaction effective date column in the resultSet. It is
	 * used to construct the ORDER BY clause.
	 */
	private static final String TRANSACTION_DATE = "RTEFDT";

	/**
	 * Name of the transaction number column in the resultSet. It is used to
	 * construct the ORDER BY clause.
	 */
	private static final String TRANSACTION_NUMBER = "TRANNO";
	
	/**
	 * Transaction Item Transformer 
	 */
	private static final TransactionHistoryItemTransformer transformer = 
		new TransactionHistoryItemTransformer();
	
	/**
	 *  Withdrawal General Info Transformer 
	 */
	private static final WithdrawalGeneralInfoTransformer withdrawalGeneInfotransformer = 
		new WithdrawalGeneralInfoTransformer();
	
	/**
	 *  Withdrawal Payee Type Transformer for Contract 
	 */
	private static final WithdrawalPayeeTypeTransformer payeeTypeTransformer = 
		new WithdrawalPayeeTypeTransformer();
	
	/**
	 * The substitutions used in all query strings.
	 */
	private static final Object[] QUERY_SUBSTITUTIONS = new Object[] {
			APOLLO_SCHEMA_NAME};
	
	//Date format
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	private static final FastDateFormat STANDARD_DATE_FORMAT =  FastDateFormat.getInstance( DATE_FORMAT );
	
	/**
	 *  SQL query to get the General(Summary) Information of the 
	 *  Pending Withdrawal Transaction
	 *  
	 *  Used for creating transaction level SQL & contract level SQL
	 *  PART - 1
	 *  
	 *  SQL Name in MF document: SRWDPENS 
	 */
	private static String PENDING_SUMMARY_SQL_1 = 
		  "	SELECT V1066.RTEFDT         	" // Transaction Date
		+ "       ,V1074.PRTID       		" // Participant Id
		+ "       ,V1074.PRTSSN      		" // SSN
		+ "       ,V1074.PRTLSTNM    		" // Last Name
		+ "       ,V1074.PRTFSTNM    		" // First Name
		+ "       ,V1066.TRANNO      		" // Transaction Number
		+ "       ,V1066.FINXTCDT           " // Financial Transaction date 
		+ "       FROM {0}.VLP1066 V1066	"
		+ "           ,{0}.VLP1074 V1074	"
		+ "     WHERE V1066.PROPNO = ?		";

	/**
	 *  SQL query to get the General(Summary) Information of the 
	 *  Pending Withdrawal Transaction
	 *  
	 *  Used for creating transaction level SQL & contract level SQL
	 *  PART - 2
	 */
	private static String PENDING_SUMMARY_SQL_2 = 
		  "  AND V1066.TRTYP = 'WD'             			"
		+ "  AND V1066.TRSTATCD IN ('WB', 'SI', 'SM', 'FL') "
		+ "  AND V1066.TRRSNCD = 'SR'						"
		+ "  AND V1066.TRANMODE = 'F'                    	"
		+ "  AND V1066.PRTID = V1074.PRTID					";

	/**
	 *  SQL where clause to get the General(Summary) Information of the 
	 *  Pending Withdrawal Summary
	 *  
	 *  Used for creating transaction level SQL
	 *  PART - 3
	 */
	private static String PENDING_SUMMARY_SQL_3 = " AND V1066.TRANNO = ? ";
	
	/**
	 *  SQL where clause to get the General(Summary) Information of the 
	 *  Pending Withdrawal Summary
	 *  
	 *  Used for creating Contract level SQL
	 *  PART - 4	
	 */
	private static String  PENDING_SUMMARY_SQL_4 = 	
		  "  AND (V1066.RTEFDT >= ? AND V1066.RTEFDT <= ?  "
		+ "     OR V1066.RTEFDT = '9999-12-31')			" ;
	
	/**
	 *  SQL where clause to get the General(Summary) Information of the 
	 *  Pending Withdrawal Summary excluded effective with '9999-12-31'
	 *  
	 *  Used for creating Contract level SQL
	 *  PART - 5	
	 */
	private static String  PENDING_SUMMARY_SQL_5 = 	
		  "  AND (V1066.RTEFDT >= ? AND V1066.RTEFDT <= ?)  ";
		
		
	/**
	 * Query to get the Pending Summary records at Contract Level.
	 *  User entered date range has a max value of 12 mths
	 * 
	 */
	private static String PENDING_SUMMARY_CONTRACT_LEVEL_SQL = 
		PENDING_SUMMARY_SQL_1 + PENDING_SUMMARY_SQL_2 + PENDING_SUMMARY_SQL_5;
	
	/**
	 * Query to get the Pending Summary records at Contract Level.
	 * 
	 *  Default start date & end date
	 *  Start date 1st bus day of the prev mth or cont eff date whichever is the latetest 
	 *  & end date is curret date + 10 bus days
	 *  User entered date range has a max value of 12 mths
	 *  if the from & to dates are default values, then any txn with 
	 *  rtefdt '9999-12-31' has to be selected as well.     
	 * 
	 */
	private static String PENDING_SUMMARY_CONTRACT_LEVEL_SQL_DEFAULT_DATE = 
		PENDING_SUMMARY_SQL_1 + PENDING_SUMMARY_SQL_2 + PENDING_SUMMARY_SQL_4;
	
	
	//Query to get the Pending Summary records at Contract and Transaction Level.
	private static String PENDING_SUMMARY_TxN_LEVEL_SQL = 
		PENDING_SUMMARY_SQL_1 + PENDING_SUMMARY_SQL_3 + PENDING_SUMMARY_SQL_2;
	
	/* SQL Name in MF documents: SRWDPNDA.
	 SQL for PENDING SR WITHDRAWAL PAYEE DETAIL INFO for all participants (SRWDPNDA)
	 within a contract with case logic
	 
	 Part 1 */

	private static String CONTRACT_PAYEE_DETAILS_SQL_PART1 = "	SELECT  	" +
			" 	 PAYEE.TRANNO												" +
			"	,PAYEE.RTEFDT												" +
			"	,PAYEE.PRTSSN												" +
			"	,PAYEE.PRTLSTNM												" +
			"	,PAYEE.PRTFSTNM												" +
			"	,PAYEE.PYEETYP												" +
			"	,(CASE														" +
			"	    WHEN (V1304.EFTTYPE IS NULL AND (PAYEE.PYMTMTD = 'HA'	" +
			"	       OR PAYEE.PYMTMTD = 'HM'))							" +
			"       THEN 'Paid by check'									" +
			"       WHEN (V1304.EFTTYPE IS NULL AND PAYEE.PYMTMTD = 'WT')	" +
			"	    THEN 'Wire transfer'									" +
			"       WHEN (V1304.EFTTYPE = 'AC'  AND PAYEE.PYMTMTD = 'WT')	" +
			"       THEN 'Direct deposit'									" +
			"	    WHEN (V1304.EFTTYPE = 'WT'  AND PAYEE.PYMTMTD = 'WT')	" +
			"       THEN 'Wire transfer'									" +
			"	    WHEN V1304.EFTTYPE IS NULL								" +
			"       THEN ' '												" +
			"      END) AS PAYMTMTD											" +
			"	 ,COALESCE(V1304.ACHACTTY,' ') AS ACCTTYP					" +
			"    ,(CASE														" +
			"       WHEN (PAYEE.PYEETYP = 'PA' OR							" +
			"             PAYEE.PYEETYP = 'BE' OR							" +
			"        PAYEE.PYEETYP = 'OT')									" +
			"       THEN PAYEE.PYEEFSTN										" +
			"       WHEN (PAYEE.PYEETYP = 'CL' OR							" +
			"        PAYEE.PYEETYP = 'TR')									" +
			"       THEN PAYEE.CNLNGM1										" +
			"       WHEN (PAYEE.PYEETYP = 'DB' OR							" +
			"          PAYEE.PYEETYP = 'FI')								" +
			"       THEN PAYEE.TRCLFINM										" +
			"          ELSE ' '												" +
			"	   END) AS FIRST_NAME										" +
			"      ,(CASE   WHEN (PAYEE.PYEETYP = 'PA' OR					" +
			"         PAYEE.PYEETYP = 'BE' OR								" +
			"            PAYEE.PYEETYP = 'OT')								" +
			"        THEN PAYEE.PYEELSTN									" +
			"         ELSE ' '												" +
			"        END) AS LAST_NAME										" +
			"      ,PAYEE.CNLNGM1											" +
			"	   ,PAYEE.PYEEADR1											" +
			"      ,PAYEE.PYEEADR2											" +
			"      ,PAYEE.PYEECITY											" +
			"      ,PAYEE.PYEESTCD											" +
			"	   ,PAYEE.PYEEZPCD											" +
			"	   ,(CASE													" +
			"         WHEN PAYEE.FRADRIND = 'N'								" +
			"		  THEN 'USA'											" +
			"         ELSE ' '  END) AS CNTRYCD								" +
			"      ,COALESCE(V1304.BANKNAM,' ') AS BANK_NAME				" +
			"      ,COALESCE(V1304.BANKABA,0) AS BRANCH_NUM					" +
			"      ,COALESCE(V1304.ACCTNO,' ')  AS ACCT_NUM					" +
			"      ,COALESCE(V1304.CRDTPRTY,' ') AS ACCT_NAME				" +
			"			 FROM												" +
			"	 (SELECT													" +
			"            V1066.RTEFDT										" +
			"           ,V1074.PRTSSN										" +
			"           ,V1074.PRTLSTNM										" +
			"           ,V1074.PRTFSTNM										" +
			"           ,V1066.TRANNO										" +
			"           ,V1079.CLNTID										" +
			"           ,V1079.PROPNO										" +
			"           ,V1079.PYEETYP										" +
			"           ,V1080.PYMTMTD										" +
			"           ,V1080.PYEEID										" +
			"           ,V1079.PYEEFSTN										" +
			"           ,V1079.PYEELSTN										" +
			"           ,V1079.TRCLFINM										" +
			"           ,V1036.CNLNGM1										" +
			"           ,V1079.PYEEADR1										" +
			"           ,V1079.PYEEADR2										" +
			"           ,V1079.PYEECITY										" +
			"           ,V1079.PYEESTCD										" +
			"           ,V1079.PYEEZPCD										" +
			"           ,V1079.FRADRIND										" +
			"	            FROM {0}.VLP1066 V1066							" +
			"	               ,{0}.VLP1079 V1079							" +
			"                  ,{0}.VLP1080 V1080							" +
			"                  ,{0}.VLP1074 V1074							" +
			"                  ,{0}.VLP1036 V1036							" +
			"              WHERE V1066.PROPNO = ?							" +
			"             AND V1066.TRSTATCD IN ('WB', 'SI', 'SM', 'FL')	";
				
	/* SQL Name in MF documents: SRWDPNDA.
	 SQL for PENDING SR WITHDRAWAL PAYEE DETAIL INFO for all participants (SRWDPNDA)
	 within a contract with case logic
	 
	 Part 2 */
	private static String CONTRACT_PAYEE_DETAILS_SQL_PART2 = " AND V1066.TRRSNCD = 'SR'	" +
	"             AND V1066.TRTYP = 'WD'												" +
	"             AND V1066.TRANMODE = 'F'												" +
	"             AND V1066.PRTID   = V1074.PRTID										" +
	"             AND V1066.TRANNO  = V1080.TRANNO										" +
	"             AND V1080.PYEEID  = V1079.PYEEID										" +
	"             AND V1079.PROPNO  = V1036.PROPNO) AS PAYEE							" +
	"	      LEFT OUTER JOIN {0}.VLP1304 V1304											" +
	"			     ON  V1304.TRANNO = PAYEE.TRANNO									" +
	"		     AND V1304.PYEEID = PAYEE.PYEEID										" +
	"    ORDER BY PAYEE.RTEFDT, PAYEE.TRANNO, PAYEE.PYEEID								";
	
	
	/**
	 *  Query to get the Pending Summary records at Contract Level.
	 *  Default Date range has a max value of 12 mths
	 */
	private static String CONTRACT_PAYEE_DETAILS_SQL_DEFAULT_DATE = 
		CONTRACT_PAYEE_DETAILS_SQL_PART1 + PENDING_SUMMARY_SQL_4 + CONTRACT_PAYEE_DETAILS_SQL_PART2;
	
	/**
	 * Query to get the Pending Summary records at Contract Level.
	 *  User entered date range has a max value of 12 mths
	 */
	private static String CONTRACT_PAYEE_DETAILS_SQL = 
		CONTRACT_PAYEE_DETAILS_SQL_PART1 + PENDING_SUMMARY_SQL_5 + CONTRACT_PAYEE_DETAILS_SQL_PART2;
	
	
	static {
		/*
		 * Set up the field to column map.
		 */
		fieldToColumnMap.put(PendingWithdrawalSummaryReportData.SORT_FIELD_DATE,
				TRANSACTION_DATE);
		fieldToColumnMap.put(PendingWithdrawalSummaryReportData.SORT_FIELD_NUMBER,
				TRANSACTION_NUMBER);
	}
	
	/**
	 * Retrieves all the Pending Withdrawal transactions along with their
	 * summary data and populates it to the reportData 
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
	@SuppressWarnings("unchecked")
	public static void getReportData(ReportCriteria criteria,
			PendingWithdrawalSummaryReportData reportData, 
			List<TransactionDataItem> dataItems)
			throws SystemException, ReportServiceException {

		Connection connection = null;
		PreparedStatement stmt = null;

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		try {
			// get the connection for APOLLO
			connection = getReadUncommittedConnection(
					className, APOLLO_DATA_SOURCE_NAME);

			// get the total record count, used for paging
			int totalCount = getTotalRecordCount(connection, criteria);
			if (logger.isDebugEnabled()) {
				logger.debug("Total Count [" + totalCount + "]");
			}

			stmt = getPreparedStatement(connection, criteria, false);
			ResultSet rs = stmt.executeQuery();

			List items = getReportItems(
					criteria, rs, transformer);

			dataItems.addAll(items);

			reportData.setTotalCount(totalCount);

		} catch (SQLException e) {
			handleSqlException(e, className,
					"getReportData",
					"Something went wrong while executing the statement - fromDate ["
							+ getFromDate(criteria) + "] toDate ["
							+ getToDate(criteria) + "] Proposal Number ["
							+ getProposalNumber(criteria) + "]"); 
		} finally {
			close(stmt, connection);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}
	}
	
	/**
	 * 1. Based on the countOnly indicator, creates the 
	 * 			a. SQL to get summary data for all the Pending SR transactions 
	 * 				in a contract 
	 * 		OR  b. SQL to get the total number of Pending SR transactions 
	 * 				in a contract
	 *
	 * 2. Adds the ORDER BY clause if applicable
	 * 
	 * 3. Creates a PreparedStatment
	 * 
	 * 4. Sets all the input parameters to the SQL
	 * 
	 * 5. Returns the preparedStatement
	 * 
	 * @param connection
	 *            The connection object to use to prepare the statement
	 * @param criteria
	 *            The report criteria used to setup the WHERE clause and the
	 *            ORDER by clause.
	 * @param countOnly
	 *            If true, returns a statement that only counts the number of
	 *            records. If false, the complete result set is returned.
	 * @throws SQLException
	 *             If it fails to prepare the statement.
	 *             
	 * @return PreparedStatement The newly prepared statement.
	 */
	private static PreparedStatement getPreparedStatement(
			Connection connection, ReportCriteria criteria, boolean countOnly)
	throws SQLException, SystemException {

		StringBuffer query = new StringBuffer();
		SqlPair pair = null;
		
		// get the default date  flag from the filter map
		Boolean isDefaultDate = (Boolean)criteria.getFilterValue(
				PendingWithdrawalSummaryReportData.DEFAULT_DATE_IND);
		//Check whether default date indicator to  retrieve transaction 
		//b/w From and To date along with date '9999-12-31'
		if(isDefaultDate) {
			pair = getSqlPair(PENDING_SUMMARY_CONTRACT_LEVEL_SQL_DEFAULT_DATE);
		}else{
			pair = getSqlPair(PENDING_SUMMARY_CONTRACT_LEVEL_SQL);	
		}
		
		if (countOnly) {
			query.append(pair.getCountQuery());
		} else {
			query.append(pair.getQuery());
		}

		// append the ORDER BY clause when there is any sorting requirement.
		if (!countOnly && criteria.getSorts().size() > 0) {
			query.append(" ORDER BY ").append(
					criteria.getSortPhrase(fieldToColumnMap));
		}

		PreparedStatement stmt = connection.prepareStatement(query.toString());

		// set filter parameters into the WHERE clause.
		setParameters(stmt, criteria, 1);

		return stmt;
	}

	/**
	 * Gets the total record count (number of Pending SR Transactions 
	 * in a contract) for the given report criteria.
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
			
			// Prepare a statement for counting
			stmt = getPreparedStatement(connection, criteria, true);
			ResultSet rs = stmt.executeQuery();
		
			/*
			 * Typically, we only have one record but in the case when we have a
			 * UNION of all types, we will have multiple records. We should add them
			 * all up to get the total record count.
			 */
			if (rs != null && rs.next()) {
				totalCount += rs.getInt(1);
			}

		} finally {
			close(stmt, null);
		}

		return totalCount;
	}
	
	/**
	 *  Returns the String for the contract level payee details
	 *   
	 * @return String 
	 */
	private static String getContractPayeeDetailsSql(String sql) {
		
		return  MessageFormatHelper.format(sql, QUERY_SUBSTITUTIONS);
		
	}
	
	/**
	 * Retrieves the WithdrawalGeneralInfoVO for the given Transaction number 
	 * and  Proposal number
	 * 
	 * Used for Pending Details page
	 * 
	 * @param proposalNumber
	 * @param txnNumber
	 * @return WithdrawalGeneralInfoVO withdrawalGeneralInfoVO
	 * @throws SystemException
	 *             If anything goes wrong with the APOLLO db.
	 */
	@SuppressWarnings("unchecked")
	public static WithdrawalGeneralInfoVO getWithdrawalGeneralInfoVOByTransactionNumber(
			String proposalNumber, String txnNumber)
	throws SystemException, ReportServiceException {

		Connection connection = null;
		PreparedStatement stmt = null;
		WithdrawalGeneralInfoVO withdrawalGeneralInfoVO = null;
		
		List<WithdrawalGeneralInfoVO> generalInfoList = null;
        
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getWithdrawalGeneralInfoVOByTransactionNumber");
		}

		try {
			// get the connection for the APOLLO Database
			connection = getReadUncommittedConnection(
					className, APOLLO_DATA_SOURCE_NAME);
			
			stmt = connection.prepareStatement(MessageFormatHelper.format(
					PENDING_SUMMARY_TxN_LEVEL_SQL, QUERY_SUBSTITUTIONS));
			stmt.setString(1, proposalNumber);
			stmt.setString(2, txnNumber);
			
			ResultSet rs = stmt.executeQuery();
      
			generalInfoList = DirectSqlReportDAOHelper.getReportItems(
					rs, withdrawalGeneInfotransformer);
			
			// We know that, only one withdrawalGeneralInfoVO will be available 
			// for the transaction
			if (generalInfoList != null && !generalInfoList.isEmpty()) {
				withdrawalGeneralInfoVO = generalInfoList.get(0);
			}
		} catch (SQLException e) {
			handleSqlException(e, className,
					"getWithdrawalGeneralInfoVOByTransactionNumber",
					"Something went wrong while executing the statement " +
					"for proposalNumber ["+proposalNumber+"] and Transaction Number ["+txnNumber+"]");
		} finally {
			close(stmt, connection);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getWithdrawalGeneralInfoVOByTransactionNumber");	
		}
		
		return withdrawalGeneralInfoVO;
	}
	
	/**
	 * Retrieves the summary info of all the transactions for the provided 
	 * proposal number & date range and creates a List of 
	 * WithdrawalGeneralInfoVO Objects
	 * 
	 * @param criteria
	 * @return list WithdrawalGeneralInfoVOs
	 * @throws SystemException
	 *             If anything goes wrong with the stored proc.
	 */
	@SuppressWarnings("unchecked")
	public static List getWithdrawalGeneralInfoListByProposalNumber(ReportCriteria criteria)
	throws SystemException, ReportServiceException {

		Connection connection = null;
		PreparedStatement stmt = null;
		
        List<WithdrawalGeneralInfoVO> generalInfoList =	null;
        
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getWithdrawalGeneralInfoListByProposalNumber");
		}
		
		//  get the proposal number from the filter map
		String proposalNumber = (String) criteria.getFilterValue(
				PendingWithdrawalSummaryReportData.FILTER_PROPOSAL_NUMBER);
		
		// get the from & to date from the filter Map
		Date fromDate = (Date) criteria.getFilterValue(
				PendingWithdrawalSummaryReportData.FILTER_FROM_DATE);
		
		Date toDate = (Date) criteria.getFilterValue(
				PendingWithdrawalSummaryReportData.FILTER_TO_DATE);
		
		// get the  default date flag from the filter map
		Boolean isDefaultDate = (Boolean)criteria.getFilterValue(
				PendingWithdrawalSummaryReportData.DEFAULT_DATE_IND);
		

		try {
			// get the APOLLO connection object
			connection = getReadUncommittedConnection(
					className, APOLLO_DATA_SOURCE_NAME);
			
			StringBuffer query = new StringBuffer();
			//Check whether default date indicator to  retrieve transaction 
			//b/w From and To date along with date '9999-12-31'
			if(isDefaultDate){
				query.append(MessageFormatHelper.format(
						PENDING_SUMMARY_CONTRACT_LEVEL_SQL_DEFAULT_DATE, QUERY_SUBSTITUTIONS));
			}else
			{
				query.append(MessageFormatHelper.format(
						PENDING_SUMMARY_CONTRACT_LEVEL_SQL, QUERY_SUBSTITUTIONS));
			}
			
			//Appends the ORDER BY clause when there is any sorting requirement.
			if ( criteria.getSorts().size() > 0) {
				query.append(" ORDER BY ").append(
						criteria.getSortPhrase(fieldToColumnMap));
			}
			
			stmt = connection.prepareStatement(query.toString());
			
			
			stmt.setString(1, String.valueOf(proposalNumber));
			stmt.setDate(2, new java.sql.Date(fromDate.getTime()));
			stmt.setDate(3, new java.sql.Date(toDate.getTime()));
			
			ResultSet rs = stmt.executeQuery();
      
			generalInfoList = DirectSqlReportDAOHelper.getReportItems(
					rs, withdrawalGeneInfotransformer);
			
		} catch (SQLException e) {
			handleSqlException(e, className,
					"getWithdrawalGeneralInfoListByProposalNumber",
					"Something went wrong while executing the statement " +
					"for proposalNumber ["+proposalNumber+"] , From Date ["+fromDate+"] and To Date ["+toDate+"]");
		} finally {
			close(stmt, connection);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getWithdrawalGeneralInfoListByProposalNumber");
		}
		
		return generalInfoList;
	}
	
	/**
	 * Retrieves the Payee details info of all the transactions for the provided 
	 * proposal number & date range and creates a Map with Transaction Number 
	 * as key and WithdrawalPayeePaymentVO list as value. 
	 * 
	 * @param proposalNumber
	 * @param fromDate 
	 * @param toDate 
	 * @param defaultDateInd
	 * @return Map<String, List<WithdrawalPayeePaymentVO>> 
	 * 
	 * @throws SystemException
	 *             If anything goes wrong with the stored proc.
	 */
	public static Map<String, List<WithdrawalPayeePaymentVO>> getPayeeListByProposalNumber(
			String proposalNumber, Date fromDate, Date toDate, Boolean defaultDateInd)
	throws SystemException, ReportServiceException {

		Connection connection = null;
		PreparedStatement stmt = null;
		
        Map<String, List<WithdrawalPayeePaymentVO>> payeeListByTransaction = null;
        
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getPayeeListByProposalNumber");
		}

		try {
			// get the APOLLO connection
			connection = getReadUncommittedConnection(
					className, APOLLO_DATA_SOURCE_NAME);
			
			//Check whether default date indicator to  retrieve transaction 
			//b/w From and To date along with date '9999-12-31'
			if(defaultDateInd){
				stmt = connection.prepareStatement(
									getContractPayeeDetailsSql(CONTRACT_PAYEE_DETAILS_SQL_DEFAULT_DATE));
			}else{
				stmt = connection.prepareStatement(
									getContractPayeeDetailsSql(CONTRACT_PAYEE_DETAILS_SQL));
			}
			
			stmt.setString(1, proposalNumber);
			stmt.setDate(2, new java.sql.Date(fromDate.getTime()));
			stmt.setDate(3, new java.sql.Date(toDate.getTime()));
			
			ResultSet rs = stmt.executeQuery();
      
			// Create the Payee List by transaction Map
			payeeListByTransaction = getReportItemsForPayee(
					rs, payeeTypeTransformer);
			
		} catch (SQLException e) {
			handleSqlException(e, className,
					"getPayeeListByProposalNumber",
					"Something went wrong while executing the statement " +
					"for proposalNumber ["+proposalNumber+"] , From Date ["+fromDate+"] and To Date ["+toDate+"]");
		} finally {
			close(stmt, connection);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getPayeeListByProposalNumber");
		}
		
		return payeeListByTransaction;
	}
	
	/**
	 * Method to pass the required input parameters to the SQL
	 * 
	 * @param stmt
	 * @param criteria
	 * @param count
	 * @throws SQLException
	 * @throws SystemException 
	 */
	private static void setParameters(PreparedStatement stmt,
			ReportCriteria criteria, int count) 
	throws SQLException, SystemException {

		java.sql.Date fromDate = getFromDate(criteria);
		java.sql.Date toDate = getToDate(criteria);
		Integer proposalNumber = getProposalNumber(criteria);
		int parameterCount = 1;

		if (logger.isDebugEnabled()) {
			logger.debug(" Proposal Number [" + proposalNumber + "]");
			logger.debug("From Date [" + fromDate + "]");
			logger.debug("  To Date [" + toDate + "]");
		}

		for (int i = 0; i < count; i++) {
			stmt.setInt(parameterCount++, proposalNumber.intValue());
			stmt.setDate(parameterCount++, fromDate); 
			stmt.setDate(parameterCount++, toDate);
		}
	}
	
	/**
	 * Method to get the Proposal Number for the given Report Criteria. 
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The Proposal number.
	 * @throws SQLException 
	 * @throws SystemException 
	 */
	public static Integer getProposalNumber(ReportCriteria criteria) throws SystemException {
		String proposalNumberString = (String) criteria
				.getFilterValue(PendingWithdrawalSummaryReportData.FILTER_PROPOSAL_NUMBER);
		Integer proposalNumber = Integer.valueOf(0);
		if(proposalNumberString != null){
			proposalNumber = Integer.valueOf(proposalNumberString);
		}
		return proposalNumber;
		
	}

	/**
	 * Gets the FROM date filter as SQL Date from the report criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The FROM date.
	 */
	private static java.sql.Date getFromDate(ReportCriteria criteria) {
		java.util.Date fromDate = (java.util.Date) criteria.getFilterValue(
				PendingWithdrawalSummaryReportData.FILTER_FROM_DATE);
		java.sql.Date returnFromDate = null;
		if ( fromDate != null ) {
			returnFromDate = new java.sql.Date(fromDate.getTime());
		} 
		return returnFromDate;
	}

	/**
	 * Gets the TO date filter as SQL Date from the report criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The TO date.
	 */
	private static java.sql.Date getToDate(ReportCriteria criteria) {
		java.util.Date toDate = (java.util.Date) criteria.getFilterValue(
				PendingWithdrawalSummaryReportData.FILTER_TO_DATE);
		java.sql.Date rv = null;
		if ( toDate != null ) {
			rv = new java.sql.Date(toDate.getTime());
		} 
		return rv;
	}
	/**
	 * This method returns SQL pairs for the given SQL statement.
	 * 		1. SQL to get the all the data
	 * 		2. SQL to get the count of records
	 * 
	 * @param sql
	 * 		Actual Sql statement
	 * @return
	 * 		SqlPair object for the given SQL 
	 * 
	 */
	private static SqlPair getSqlPair(String sql) {

		String query = MessageFormatHelper.format(sql, QUERY_SUBSTITUTIONS);

		String countQuery = new SqlStringBuffer(" SELECT COUNT(*) FROM (")
				.append(query).append(") AS II_TEMP").toString();

		return new SqlPair(countQuery, query);
	}
	
	/**
	 * Inner class to transform the Result Set to TransactionDataItem Objects.
	 * @author Puttaiah Arugunta
	 *
	 */
	private static class TransactionHistoryItemTransformer extends
									ReportItemTransformer {

		public Object transform(ResultSet rs) throws SQLException {
			
			TransactionDataItem item = new TransactionDataItem();
			item.setTransactionStatusCode("SR"); // All transactions are Step Rate transactions
			item.setTransactionEffectiveDate(rs.getDate("RTEFDT"));
			item.setTransactionType(TransactionType.WITHDRAWAL);
			int participantId = rs.getInt("PRTID");
			item.getParticipant().setId(Integer.valueOf(participantId));
			item.getParticipant().setSsn(getString(rs, "PRTSSN"));
			item.getParticipant().setLastName(getString(rs, "PRTLSTNM"));
			item.getParticipant().setFirstName(getString(rs, "PRTFSTNM"));
			item.setTransactionNumber(getString(rs, "TRANNO"));

			return item;
		}
	}
	
	/**
	 * Inner class to transform the Result Set to WithdrawalGeneralInfoVO Objects.
	 * @author Puttaiah Arugunta
	 *
	 */
	private static class WithdrawalGeneralInfoTransformer extends
									ReportItemTransformer {

		public Object transform(ResultSet rs) throws SQLException {
			
			WithdrawalGeneralInfoVO item = new WithdrawalGeneralInfoVO();
			item.setWithdrawalDate(rs.getDate("RTEFDT")); 
			item.setSsn(getString(rs, "PRTSSN"));
			item.setName(getString(rs, "PRTLSTNM") +", "+ getString(rs, "PRTFSTNM"));
			item.setTransactionNumber(getString(rs, "TRANNO"));
			item.setExtractTxnDate(rs.getDate("FINXTCDT"));
			item.setTransactionType("Withdrawal");
			item.setTransactionTypeDescription("Withdrawal of additional contributions");

			return item;
		}
	}
	
	/**
	 * Inner class to transform the Result Set to WithdrawalPayeePaymentVO Objects.
	 * @author Puttaiah Arugunta
	 *
	 */
	private static class WithdrawalPayeeTypeTransformer extends
									ReportItemTransformer {

		public Object transform(ResultSet rs) throws SQLException {
			
			
			WithdrawalPayeePaymentVO payeePaymentVO = new WithdrawalPayeePaymentVO();
			
			payeePaymentVO.setPaymentTo(getString(rs, "PYEETYP"));
			payeePaymentVO.setPaymentMethod(getString(rs, "PAYMTMTD"));
			payeePaymentVO.setAccountType(getString(rs, "ACCTTYP"));
			
			StringBuffer fullName = new StringBuffer();			
			String firstName = getString(rs, "FIRST_NAME");
			String lastName = getString(rs, "LAST_NAME");
			if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
				fullName.append(firstName).append(" ").append(lastName);
			} else if (StringUtils.isBlank(lastName) && StringUtils.isNotBlank(firstName)) {
				fullName.append(firstName);
			} else if (StringUtils.isBlank(firstName) && StringUtils.isNotBlank(lastName)) {
				fullName.append(lastName);
			}
			payeePaymentVO.setPayeeName(fullName.toString());
			
			payeePaymentVO.setAddressLine1(getString(rs, "PYEEADR1"));
			payeePaymentVO.setAddressLine2(getString(rs, "PYEEADR2"));
			payeePaymentVO.setCity(getString(rs, "PYEECITY"));
			payeePaymentVO.setState(getString(rs, "PYEESTCD"));
			payeePaymentVO.setZip(getString(rs, "PYEEZPCD"));
			payeePaymentVO.setCountry(getString(rs, "CNTRYCD"));
			payeePaymentVO.setBankBranchName(getString(rs, "BANK_NAME"));
			
			String routingNumber = getString(rs, "BRANCH_NUM");
			if ( Integer.valueOf(routingNumber) == 0){
				routingNumber = StringUtils.EMPTY;
			}else if(routingNumber.length() < 9){
				routingNumber = String.format("%09d", 
						         Integer.valueOf(routingNumber.trim()));
			}
			payeePaymentVO.setRoutingABAnumber(routingNumber);
			
			payeePaymentVO.setAccountNumber(getString(rs, "ACCT_NUM"));
			payeePaymentVO.setCreditPayeeName(getString(rs, "ACCT_NAME"));

			return payeePaymentVO;
		}
	}
	
	/**
	 * This method will
	 * 		1. validate the parameters are not null
	 * 
	 * 		2. Iterates the resultSet and transforms each record to
	 * 			WithdrawalPayeePaymentVO object by using the 
	 * 			specified transformer
	 * 
	 * 		3. Finally build the Map of WithdrawalPayeePaymentVOs with 
	 * 			Transaction number as the key and WithdrawalPayeePaymentVO 
	 * 			list as the value for the given ResultSet.
	 * 
	 * @param rs
	 *            The complete result set.
	 * @param transformer
	 *            The transformer that transforms a record into a 
	 *            WithdrawalPayeePaymentVO item.
	 * @return The WithdrawalPayeePaymentVO items list.
	 * @throws SystemException
	 * @throws ReportServiceException 
	 */
	private static Map<String, List<WithdrawalPayeePaymentVO>> getReportItemsForPayee(
			ResultSet rs, ReportItemTransformer transformer) 
	throws SystemException, ReportServiceException {

		 Map<String, List<WithdrawalPayeePaymentVO>> payeeListByTransaction = 
			 new HashMap<String, List<WithdrawalPayeePaymentVO>>();

		 List<WithdrawalPayeePaymentVO> payeeList = null;
		
		 try {
		
			// validate the input parameters
			if (rs == null || transformer == null ) {
				String errorString = "Invalid input parameters ";
				throw new SystemException(new IllegalArgumentException(
						errorString), 
						className + "getReportItemsForPayee" + errorString);
			}

			while (rs.next()) {
			    
				String transactionNumber = getString(rs, "TRANNO");
				
				WithdrawalPayeePaymentVO withdrawalPayeePaymentVO = 
					(WithdrawalPayeePaymentVO)transformer.transform(rs);
				
				if (payeeListByTransaction.containsKey(transactionNumber)) {
					/*
					 *  if the transaction number is available in the Map
					 *  as a key, then retrieve the appropriate payee list 
					 */
					payeeList = (List<WithdrawalPayeePaymentVO>) 
								payeeListByTransaction.get(transactionNumber);
					
					// add the withdrawalPayeePaymentVO to the list 
					payeeList.add(withdrawalPayeePaymentVO);
					
				} else {
					/*
					 *  if the transaction number is not available in the Map
					 *  as a key, then create a new payee list and put the 
					 *  values in the Map with key as transaction number and 
					 *  the value as payeeList 
					 */
					payeeList = new ArrayList<WithdrawalPayeePaymentVO>();
					payeeList.add(withdrawalPayeePaymentVO);
					payeeListByTransaction.put(transactionNumber, payeeList);
				}
			}
			
		} catch (SQLException e) {
			handleSqlException(e, className,
					"getReportItemsForPayee",
					" Exception occured while transforms each record to	WithdrawalPayeePaymentVO object"); 
		}
		
		// Return the Payee List Map grouped by transaction number
		return payeeListByTransaction;
	}
	

	/**
	 * Obtains the report item list from the given result set. It uses the
	 * report criteria to determine the page number and the size of data to
	 * retrieve. A report item transformer must be provided to transform a
	 * record in the result set into a report item.
	 * 
	 * @param criteria
	 *            The report criteria to use.
	 * @param rs
	 *            The complete result set.
	 * @param transformer
	 *            The transformer that transforms a record into a report item.
	 * @return The report items list.
	 * @throws SystemException
	 */
	public static List<TransactionDataItem> getReportItems(ReportCriteria criteria, ResultSet rs,
			ReportItemTransformer transformer) throws SystemException {

		List<TransactionDataItem> dataItems = new ArrayList<TransactionDataItem>();
		List<TransactionDataItem> dataItemsInitial = new ArrayList<TransactionDataItem>();

		try {
			int startIndex = criteria.getStartIndex();
			
			// get the isTransactionSort flag from the filter map
			Boolean isTransactionSort = (Boolean)criteria.getFilterValue(
					PendingWithdrawalSummaryReportData.SORT_BY_TRANSACTION_DATE);
			List<TransactionDataItem> defaultDateList = null;
			List<TransactionDataItem> otherDateList = null;
			defaultDateList = new ArrayList<TransactionDataItem>();
			
			while (rs.next()) {

				Object item = transformer.transform(rs);

				if (item != null) {
					// Store the data item into the given list.
					dataItemsInitial.add((TransactionDataItem) item);
				}
			}
			
			//Block to validate the sorting of Default Date (9999-12-31) 
			//in  transaction date section
			if(isTransactionSort){
				otherDateList = new ArrayList<TransactionDataItem>();
				Calendar dummyDate = Calendar.getInstance();
				dummyDate.set(9999, Calendar.DECEMBER, 31);

				for(TransactionDataItem transactionDataItem : dataItemsInitial) {
					if (STANDARD_DATE_FORMAT.format(dummyDate.getTime()).equals(
							STANDARD_DATE_FORMAT.format(transactionDataItem.getTransactionEffectiveDate())) ){
						defaultDateList.add(transactionDataItem);
					} else {
						otherDateList.add(transactionDataItem);
					}
				}
				defaultDateList.addAll(otherDateList);
			}else{
				defaultDateList.addAll(dataItemsInitial);
			}
			
			/*
			 * Move result set to the proper location. Start index begins at 1.
			 */
			int transactionCount = 1;
			for (; transactionCount < startIndex  &&  transactionCount < defaultDateList.size(); transactionCount++)
				;

			/*
			 * ResultSet ends before start index...
			 */
			if (transactionCount < startIndex) {
				String errorString = "Invalid report criteria start index ["
						+ startIndex + "] result set size [" + (transactionCount - 1) + "]";
				throw new SystemException(new IllegalArgumentException(
						errorString), errorString);
			}

			boolean limitPageSize = true;

			int recordCount = 0;
			int pageSize = criteria.getPageSize();
			if (pageSize == ReportCriteria.NOLIMIT_PAGE_SIZE) {
				limitPageSize = false;
			}

			/*
			 * Retrieve one page worth of data.
			 */
			while ((!limitPageSize || recordCount < pageSize) 
					&&   transactionCount <= defaultDateList.size()) {
				TransactionDataItem transactionDataItem=defaultDateList.get(transactionCount-1);
				dataItems.add(transactionDataItem);
				recordCount++;
				transactionCount++;
			}
		} catch (SQLException e) {
			throw new SystemException(e, e
					.getMessage());
		}
		return dataItems;
	}
}
