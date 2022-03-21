package com.manulife.pension.ps.service.report.transaction.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountBalanceDetailsReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountForfeituresReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.service.account.entity.ContractMoneyType;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.dao.DirectSqlReportDAOHelper;
import com.manulife.pension.service.report.dao.ReportItemTransformer;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportSort;

/**
 * This class assembles the data required for the Cash Account Balance Details and Forfeitures transaction. 
 * It uses db2connect to access mainframe Apollo tables directly. The schema it
 * uses is defined in the System.properties in the PlanSponsor Server instance.
 *
 * @author Rajesh Rajendran
 */
public class CashAccountOtherDetailsDAO extends ReportServiceBaseDAO {

	private static final String className = CashAccountOtherDetailsDAO.class.getName();

	private static final Logger logger = Logger.getLogger(CashAccountOtherDetailsDAO.class);
	
	private static final String TX_EFF_DATE = "txEffDate";
	private static final String TX_TYPE = "txType";
	private static final String TX_NUMBER = "txNumber";
	private static final String TX_REASON_CODE = "txReasonCode";
	private static final String TX_REASON_CODE_EXCESS_WD = "txReasonCodeExWD";
	private static final String TX_STATUS_CODE = "txStatusCode";
	private static final String PART_SSN = "partSSN";
	private static final String PART_NAME = "partName";
	private static final String TX_ORIGINAL_AMOUNT ="txOriginalAmount";
	private static final String TX_AVAILABLE_AMOUNT ="txAvailableAmount";
	private static final String MONEY_TYPE = "moneytypeMedium";
	private static final String TASK_PRINT = "print";
	private static final String TASK_DOWNLOAD = "download";
	private static final String TOTAL_ASSESTS = "TOTAL_ASSESTS";
	private static final String TOTAL_AMOUNT = "TOTAL_AMOUNT";
	
	private static final String TRANSACTION_DATE = "transactionDate";
	private static final String TRANSACTION_NUMBER = "transactionNumber";
	private static final String MONEY_TYPE_CONSTANT = "moneyType";
	private static final String DESCENDING_ORDER = "desc";
	private static final String ASCENDING_ORDER = "asc";
	
	private String clientId;
	private Integer contractNumber;
	private java.sql.Date fromDate;
	private java.sql.Date toDate;
	private String task;
	private String moneyType;
	
	private static final String CURRENT_BALANCE = "currentBalance";
	private static final int MAXIMUM = 500;
	
	private static final BigDecimal ZERO = new BigDecimal("0");

	private static final Map<String,String> fieldToColumnMap = new HashMap<String,String>();
	
	private static final BalanceDetailsTransactionDataItemTransformer balanceDetailsTransformer = 
		new BalanceDetailsTransactionDataItemTransformer();
	
	private static final ForfeituresTransactionDataItemTransformer forfeituresTransformer = 
		new ForfeituresTransactionDataItemTransformer();

	static{
		fieldToColumnMap.put("transactionDate", "txEffDate");
		fieldToColumnMap.put("moneyType", "moneytypeMedium");
		fieldToColumnMap.put("transactionNumber", "txNumber");		
	}
	
	/**
	 * SQL to get the UM money types belonging to a contract
	 */
	private static final String SQL_SELECT_FORFEITURES_MONEY_TYPE =
			  " SELECT "
			+ " 	CONTRACT_MONEY_TYPE_MED_NAME AS MONEY_TYPE_MED_NAME, "
			+ " 	MONEY_TYPE_ID AS MONEY_TYPE_ID, "
			+ " 	CONTRACT_ID "
			+ " FROM "
			+ BaseDatabaseDAO.PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_MONEY_TYPE CMT, "
			+ BaseDatabaseDAO.PLAN_SPONSOR_SCHEMA_NAME + "MONEY_TYPE MT "
			+ " WHERE "
			+ "			CMT.MONEY_TYPE_ID = MT.MONEY_TYPE_CODE "
			+ "		AND MT.MONEY_TYPE_GROUP = 'UM' "
			+ "		AND CONTRACT_ID = ? "
			+ "	ORDER BY CONTRACT_MONEY_TYPE_MED_NAME ASC ";
	
	/**
	 * SQL to get the Total UM balance for a contract
	 */
	private static final String SQL_SELECT_TOTAL_FORFEITURES_IN_PARTICIPANT_ACCOUNT = 
			  " SELECT " 
			+ " 	SUM(TOTAL_BALANCE_AMT) AS TOTAL_ASSESTS, "
			+ " 	MT.MONEY_TYPE_GROUP "
			+ " FROM "
			+ BaseDatabaseDAO.PLAN_SPONSOR_SCHEMA_NAME + "PARTICIPANT_CURRENT_BAL_LSA PCB, "
			+ BaseDatabaseDAO.PLAN_SPONSOR_SCHEMA_NAME + "PARTICIPANT_CONTRACT PC, "
			+ BaseDatabaseDAO.PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_MONEY_TYPE CMT, "
			+ BaseDatabaseDAO.PLAN_SPONSOR_SCHEMA_NAME + "MONEY_TYPE MT "
			+ " WHERE "
			+ "			PC.PARTICIPANT_STATUS_CODE <> 'CN' "
			+ " 	AND PC.CONTRACT_ID = ? "
			+ "		AND PCB.CONTRACT_ID = PC.CONTRACT_ID "
			+ " 	AND PCB.PARTICIPANT_ID = PC.PARTICIPANT_ID "
			+ "		AND PCB.MONEY_TYPE_ID = CMT.MONEY_TYPE_ID "
			+ "		AND PCB.CONTRACT_ID = CMT.CONTRACT_ID "
			+ " 	AND CMT.MONEY_TYPE_ID = MT.MONEY_TYPE_CODE "
			+ " 	AND MT.MONEY_TYPE_GROUP LIKE 'UM%' "
			+ " GROUP BY "
			+ "		(MT.MONEY_TYPE_GROUP) "
			+ " ORDER BY "
			+ "		(MT.MONEY_TYPE_GROUP)";
	
	/**
	 * SQL to get the Total UM balance for a contract
	 */
	private static final String SQL_SELECT_TOTAL_FORFEITURES_IN_CASH_ACCOUNT =
			  " SELECT " 
			+ " 	SUM(A.AVAILAMT) AS TOTAL_AMOUNT "
			+ " FROM " 
			+ BaseDatabaseDAO.APOLLO_SCHEMA_NAME + ".VLP7234 A"
			+ " WHERE " 
			+ " 		A.CLNTID = ? " 
			+ " 	AND A.CNNO IN (0, ?) " 
			+ " 	AND A.CREFDT BETWEEN ? AND ? ";

	/**
	 * SQL to get the Withdrawal transactions having money type
	 */
	private static final String SQL_SELECT_WDW_TRANSACTION_HAVING_MONEY_TYPES =
			  " SELECT "
			+ " 	A.CREFDT txEffDate, "
			+ " 	A.TRTYP txType, " 
			+ " 	A.TRANNO txNumber, " 
			+ "		A.CRRSN txReasonCode, " 
			+ "		B.TRRSNCD txReasonCodeExWD, " 
			+ "		A.CASTATCD txStatusCode, " 
			+ "		C.PRTSSN partSSN, " 
			+ "		C.PRTLSTNM || ',' || C.PRTFSTNM partName, "
			+ "		D. CNMMMED moneytypeMedium, "
			+ "		A.ORICRAMT txOriginalAmount, "
			+ " 	A.AVAILAMT txAvailableAmount "
			+ "	FROM "
			+ BaseDatabaseDAO.APOLLO_SCHEMA_NAME + ".VLP7234 A, "
			+ BaseDatabaseDAO.APOLLO_SCHEMA_NAME + ".VLP1066 B, " 
			+ BaseDatabaseDAO.APOLLO_SCHEMA_NAME + ".VLP1074 C, " 
			+ BaseDatabaseDAO.APOLLO_SCHEMA_NAME + ".VLP1031 D " 
			+ " WHERE "  
			+ "			A.PRTID = B.PRTID " 
			+ "		AND A.PRTID = C.PRTID " 
			+ "		AND B.TRANNO = A.TRANNO " 
			+ "		AND B.PROPNO = D.PROPNO " 
			+ "		AND A.TRTYP = 'WD' " 
			+ "		AND A.MLIMT = D.MLIMT "   
			+ "		AND A.CLNTID = ? " 
			+ "		AND A.CNNO IN (0, ?) " 
			+ "		AND D.STARTDTE <=  CURRENT DATE "
			+ "		AND D.ENDDTE > CURRENT DATE "
			+ "		AND A.CREFDT BETWEEN ? AND ? " ;
	
	/**
	 * SQL to get the Withdrawal transactions which does not have money type
	 */
	private static final String SQL_SELECT_WDW_TRANSACTIONS_NOT_HAVING_MONEY_TYPES =
			  " SELECT "
			+ " 	A.CREFDT txEffDate, "
			+ " 	A.TRTYP txType, " 
			+ " 	A.TRANNO txNumber, " 
			+ "		A.CRRSN txReasonCode, " 
			+ "		B.TRRSNCD txReasonCodeExWD, " 
			+ "		A.CASTATCD txStatusCode, " 
			+ "		C.PRTSSN partSSN, " 
			+ "		C.PRTLSTNM || ',' || C.PRTFSTNM partName, "
			+ "		' ' as moneytypeMedium, "
			+ "		A.ORICRAMT txOriginalAmount, "
			+ " 	A.AVAILAMT txAvailableAmount "
			+ "	FROM "
			+ BaseDatabaseDAO.APOLLO_SCHEMA_NAME + ".VLP7234 A, "
			+ BaseDatabaseDAO.APOLLO_SCHEMA_NAME + ".VLP1066 B, " 
			+ BaseDatabaseDAO.APOLLO_SCHEMA_NAME + ".VLP1074 C " 
			+ " WHERE "  
			+ " 		A.CLNTID = ? "
			+ "		AND A.CNNO IN (0, ?) " 
			+ "		AND B.TRANNO = A.TRANNO "
			+ "		AND A.PRTID = B.PRTID " 
			+ "		AND A.PRTID = C.PRTID  "
			+ "		AND A.TRTYP = 'WD' "
			+ "		AND A.MLIMT = ' ' "
			+ "		AND A.CREFDT BETWEEN ? AND ? ";
	 
	
	/**
	 * Default money type filter clause
	 */
	private static final String SQL_MONEY_TYPE_DEFAULT_FILTER_CLAUSE = 
			" AND A.MLIMT <> ' '";

	/**
	 * Custom money type filter clause
	 */
	private static final String SQL_MONEY_TYPE_FILTER_CLAUSE = 
			" AND A.MLIMT = ? ";
	/**
	 * Order by clause
	 */
	private static final String ORDERY_BY_CLAUSE =
			" ORDER BY ";

	/**
	 * SQL to get all CA transactions having no money type
	 */
	private static final String SQL_SELECT_CA_TRANSACTIONS_NOT_HAVING_MONEY_TYPE =
			  " SELECT " 
		 	+ " 	A.CREFDT txEffDate, " 
		 	+ " 	A.TRTYP txType, " 
		 	+ " 	A.TRANNO txNumber, " 
		 	+ " 	A.CRRSN txReasonCode, " 
		 	+ " 	' ' as txReasonCodeExWD, " 
		 	+ " 	A.CASTATCD txStatusCode, "
		 	+ " 	' ' as partSSN, "
		 	+ " 	' ' as partName, "
		 	+ "		'Adjustment' AS moneytypeMedium, "
		 	+ " 	A.ORICRAMT txOriginalAmount, "
		 	+ " 	A.AVAILAMT txAvailableAmount " 
		 	+ " FROM "
		 	+ BaseDatabaseDAO.APOLLO_SCHEMA_NAME + ".VLP7234 A " 
		 	+ " WHERE " 
		    + "			A.CLNTID = ? "
		    + " 	AND A.CNNO IN (0, ?) " 
		    + " 	AND A.TRTYP = 'CA' " 
		    + "		AND A.MLIMT = ' ' "   
		    + "		AND A.CREFDT BETWEEN ? AND ? ";
	
	/**
	 * UNION ALL String constant
	 */
	private static final String SQL_UNION_ALL_STRING = " UNION ALL ";
	
	/**
	 * SQL to get the Transaction items for the Cash account Balance details page
	 */
	private static final String SELECT_BALANCE_DETAILS_TRANSACTION_ITEMS_QUERY =
			  " SELECT " 
			+ "		A.CREFDT TXEFFDATE, " 
			+ "		A.TRTYP TXTYPE, " 
			+ "		A.TRANNO TXNUMBER, " 
			+ "		A.CRRSN TXREASONCODE, " 
			+ "		B.TRRSNCD TXREASONCODEEXWD, " 
			+ "		A.CASTATCD TXSTATUSCODE, " 
			+ "		C.PRTSSN PARTSSN, " 
			+ "		C.PRTLSTNM || ',' || C.PRTFSTNM PARTNAME, " 
			+ "		A.ORICRAMT TXORIGINALAMOUNT, " 
			+ "		A.LFTOVAMT  TXAVAILABLEAMOUNT " 
			+ "	FROM " 
			+ BaseDatabaseDAO.APOLLO_SCHEMA_NAME + ".VLP7233 A, " 
			+ BaseDatabaseDAO.APOLLO_SCHEMA_NAME + ".VLP1066 B, " 
			+ BaseDatabaseDAO.APOLLO_SCHEMA_NAME + ".VLP1074 C " 
			+ " WHERE " 
			+ "			C.PRTID = B.PRTID " 
			+ "		AND B.TRANNO = A.TRANNO " 
			+ "		AND A.TRTYP = 'WD' " 
			+ "		AND A.CLNTID = ? " 
			+ "		AND A.CNNO IN (0, ?) " 
			+ "		AND A.CREFDT BETWEEN ? AND ? " 
			+ " UNION ALL " 
			+ " SELECT " 
			+ "		A.CREFDT TXEFFDATE, " 
			+ "		A.TRTYP TXTYPE, " 
			+ "		A.TRANNO TXNUMBER, " 
			+ "		A.CRRSN TXREASONCODE, " 
			+ "		' ' AS TXREASONCODEEXWD, " 
			+ "		A.CASTATCD TXSTATUSCODE, " 
			+ "		' ' AS PARTSSN, ' ' AS PARTNAME, " 
			+ "		A.ORICRAMT TXORIGINALAMOUNT, " 
			+ "		A.LFTOVAMT TXAVAILABLEAMOUNT " 
			+ "	FROM " 
			+ BaseDatabaseDAO.APOLLO_SCHEMA_NAME + ".VLP7233 A " 
			+ " WHERE " 
			+ "			A.TRTYP ^= 'WD' " 
			+ " 	AND A.CLNTID = ? " 
			+ "		AND A.CNNO IN (0, ?) " 
			+ "		AND A.CREFDT BETWEEN ? AND ? " 
			+ " ORDER BY TXEFFDATE DESC, TXNUMBER DESC ";	
	
	/**
	 * SQL to get the current balance for the cash account balance details page
	 */
	private static final String SQL_SELECT_CURRENT_BALANCE = 
			  " SELECT " 
			+ "		OBAMT AS " + CURRENT_BALANCE 
			+ " FROM " 
			+ APOLLO_SCHEMA_NAME + ".VLP7221 " 
			+ " WHERE " 
			+ "			CLNTID = ? " 
			+ "		AND (OBSTRDT - 1 DAY) <= ? 	 AND OBENDDT >  ?";

	
	/**
	 * Gets the Cash Account-Balance Details Summary Data from Apollo.
	 * @param criteria
	 * @return reportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	public CashAccountBalanceDetailsReportData getBalanceDetailsSummaryData(
			ReportCriteria criteria)
			throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getBalanceDetailsSummaryData");
			logger.debug("schema set to -> " + APOLLO_SCHEMA_NAME);
		}
		
		getBalanceDetailsFilterValues(criteria);
		CashAccountBalanceDetailsReportData reportData = null;

		Connection connection = null;
		try {
			connection = null;
			connection = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);
			BigDecimal currentBalance = retrieveCurrentBalance(connection);

			// arbitrarily set total count to zero, it will be set later
			reportData = new CashAccountBalanceDetailsReportData(criteria, 0);
			reportData.setCurrentBalance(currentBalance);
			reportData.setFromDate(fromDate);
			reportData.setToDate(toDate);
			
		} catch (SQLException e) {
			handleSqlException(e, className,
					"getBalanceDetailsSummaryData",
					"Client: " + clientId +
					"; contract: " + contractNumber.intValue() +
					"; fromDate:" + fromDate +
					"; toDate:" + toDate);
		} finally {
			close(null, connection);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getBalanceDetailsSummaryData");
		}

		return reportData;
	}

	/**
	 * Gets the cash account balance details items from Apollo. 
	 * 
	 * @param criteria 
	 * @param reportData
	 * @return List of TransactionDataItem
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<TransactionDataItem> getBalanceDetailsTransactionItems(
			ReportCriteria criteria, 
			CashAccountBalanceDetailsReportData reportData) throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getBalanceDetailsTransactionItems");
			logger.debug("client ID is [" + clientId + "]");
			logger.debug("fromDate is [" + fromDate + "]");
			logger.debug("toDate is [" + toDate + "]");
			logger.debug("contractNumber is [" + contractNumber + "]");
		}

		getBalanceDetailsFilterValues(criteria);
		List<TransactionDataItem> transactions = new ArrayList<TransactionDataItem>();
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement stmt = null;		

		try {
			connection = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);
			stmt = connection.prepareStatement(SELECT_BALANCE_DETAILS_TRANSACTION_ITEMS_QUERY, 
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			stmt.setString(1, clientId);
			stmt.setInt(2, contractNumber.intValue());
			stmt.setDate(3, fromDate);
			stmt.setDate(4, toDate);
			stmt.setString(5, clientId);
			stmt.setInt(6, contractNumber.intValue());
			stmt.setDate(7, fromDate);
			stmt.setDate(8, toDate);

			resultSet = stmt.executeQuery();
			
			int totalCount=0;
			if (!resultSet.isLast()){
				resultSet.last();
			}
			totalCount = resultSet.getRow();
			resultSet.beforeFirst();
			
			transactions =
				(List<TransactionDataItem>)DirectSqlReportDAOHelper.getReportItems(
						criteria, resultSet, balanceDetailsTransformer);
			
			reportData.setTotalCount(totalCount);
			
			reportData.setHasTooManyItems(false);
			
		} catch (SQLException e) {
			handleSqlException(e, className,
					"getBalanceDetailsTransactionItems",
					"Client: " + clientId +
					"; contract: " + contractNumber.intValue() +
					"; fromDate:" + fromDate +
					"; toDate:" + toDate);
		} finally {
			close(stmt, connection);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getBalanceDetailsTransactionItems");
		}
		return transactions;
	}
	
	/**
	 * Determine if the history returned has more than 500 items and the range
	 * can be further reduced.
	 * 
	 * @param size
	 * @return TRUE if the size > 500
	 */
	private boolean hasTooManyItems(int size) {
		boolean tooMany = size > MAXIMUM ? true : false;

		if (tooMany) {
			
			 // If user cannot further reduce the range, we should still
			 // display everything.
			Calendar toDateCalendar = Calendar.getInstance();
			Calendar fromDateCalendar = Calendar.getInstance();

			toDateCalendar.setTime(toDate);
			fromDateCalendar.setTime(fromDate);

			fromDateCalendar.add(Calendar.MONTH, 1);
			if (fromDateCalendar.equals(toDateCalendar)
					|| fromDateCalendar.after(toDateCalendar)) {
				tooMany = false;
			}
		}
		return tooMany;
	}	
		
	/**
	 * Gets the summary data from Apollo.
	 *
	 * @param criteria
	 * @return
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	public CashAccountForfeituresReportData getForfeituresSummaryData(
			ReportCriteria criteria) throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getForfeituresSummaryData");
		}

		getForfeituresFilterValues(criteria);
		CashAccountForfeituresReportData cashAccountForfeituresReportData = null;

		try {
			// arbitrarily set total count to zero, it will be set later
			cashAccountForfeituresReportData = 
				new CashAccountForfeituresReportData(criteria, 0);
			
			// total forfeitures in Plan is the sum of total forfeitures 
			// in participant account and cash account
			BigDecimal totalForfeituresInPlan = ZERO;
			
			BigDecimal totalForfeituresInCashAccount = ZERO;
			// get the total forfeitures in cash account
			totalForfeituresInCashAccount = 
				getTotalForfeituresInCashAccount(criteria);
			
			// add the total forfeitures in cash account to the plan total
			totalForfeituresInPlan = totalForfeituresInPlan.add(
					totalForfeituresInCashAccount);
			
			// get the total forfeitures in the participant account
			BigDecimal totalForfeituresInPptAccount = 
				getTotalForfeituresInParticipantAccount(criteria);

			// add the total forfeitures in participant account to the plan total
			totalForfeituresInPlan = totalForfeituresInPlan.add(
					totalForfeituresInPptAccount);
			
			// set the values to the reportData object
			cashAccountForfeituresReportData.setTotalForfeituresInPlan(
					totalForfeituresInPlan);
			cashAccountForfeituresReportData.setTotalForfeituresInCashAccount(
					totalForfeituresInCashAccount);
			cashAccountForfeituresReportData.setTotalForfeituresInParticipant(
					totalForfeituresInPptAccount);
			cashAccountForfeituresReportData.setFromDate(fromDate);
			cashAccountForfeituresReportData.setToDate(toDate);
			
		} catch (Exception e) {
			throw new SystemException(e, className 
					+ ".getForfeituresSummaryData() "
					+ "Client: " + clientId 
					+ "; contract: " + contractNumber.intValue() 
					+ "; fromDate:" + fromDate
					+ "; toDate:" + toDate);
		} 

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getForfeituresSummaryData");
		}

		return cashAccountForfeituresReportData;
	}

	/**
	 * To retrieve the Transaction item for the Cash account Forfeitures page
	 * 
	 * @param criteria
	 * @param cashAccountForfeituresReportData
	 * @return list of the Transaction items
	 * @throws SystemException
	 * @throws ReportServiceException 
	 */
	@SuppressWarnings("unchecked")
	public List<TransactionDataItem> getForfeituresTransactionItemsList(ReportCriteria criteria,
		   CashAccountForfeituresReportData cashAccountForfeituresReportData) throws SystemException, ReportServiceException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getForfeituresTransactionItemsList");
		}
		
		getForfeituresFilterValues(criteria);
		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		Connection connection = null;
		List<TransactionDataItem> transactions = new ArrayList<TransactionDataItem>();
		
		try {
			connection = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);
			stmt = connection.prepareStatement(
					getForfeituresTransactionItemsQueryString(criteria), 
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			if (StringUtils.equalsIgnoreCase(moneyType, 
					CashAccountForfeituresReportData.ALL_MONEY_TYPES_KEY)) {
				
				stmt.setString(1, clientId);
				stmt.setInt(2, contractNumber.intValue());
				stmt.setDate(3, fromDate);
				stmt.setDate(4, toDate);
				stmt.setString(5, clientId);
				stmt.setInt(6, contractNumber.intValue());
				stmt.setDate(7, fromDate);
				stmt.setDate(8, toDate);
				stmt.setString(9, clientId);
				stmt.setInt(10, contractNumber.intValue());
				stmt.setDate(11, fromDate);
				stmt.setDate(12, toDate);
				
			} else if (StringUtils.equalsIgnoreCase(moneyType, 
					CashAccountForfeituresReportData.ADJUSTMENT_MONEY_TYPE_KEY)) {

				stmt.setString(1, clientId);
				stmt.setInt(2, contractNumber.intValue());
				stmt.setDate(3, fromDate);
				stmt.setDate(4, toDate);
				
			} else if (StringUtils.isNotBlank(moneyType)) {
				stmt.setString(1, clientId);
				stmt.setInt(2, contractNumber.intValue());
				stmt.setDate(3, fromDate);
				stmt.setDate(4, toDate);
				stmt.setString(5, moneyType);
			}
			
			resultSet = stmt.executeQuery();
			
			int totalCount=0;
			
			if (!resultSet.isLast()){
				resultSet.last();
			}
			
			totalCount = resultSet.getRow();
			resultSet.beforeFirst();
			
			transactions = (List<TransactionDataItem>)DirectSqlReportDAOHelper.getReportItems(
						criteria, resultSet, forfeituresTransformer);
			
			cashAccountForfeituresReportData.setTotalCount(totalCount);

			cashAccountForfeituresReportData.setHasTooManyItems(false);
			
			// a safety check for the Print report and csv download to validate
			// total transactions are > 500
			if(task.equalsIgnoreCase(TASK_PRINT) || task.equalsIgnoreCase(TASK_DOWNLOAD)) {
				if (hasTooManyItems(totalCount)) {
					cashAccountForfeituresReportData.setHasTooManyItems(true);
					transactions = null;
				}
			}
			
			resultSet.close();
			
		} catch (SQLException e) {
			handleSqlException(e, className,
					"getForfeituresTransactionItemsList",
					"Client: " + clientId +
					"; contract: " + contractNumber.intValue() +
					"; fromDate:" + fromDate +
					"; toDate:" + toDate);
		} finally {
			close(stmt, connection);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getForfeituresTransactionItemsList");
		}
		
		return transactions;
	}
		
	/**
	 * Retrieve the values from the ReportCriteria Object
	 *
	 * @param criteria
	 * @throws SystemException
	 */
	private void getBalanceDetailsFilterValues(ReportCriteria criteria)
			throws SystemException {

		clientId = (String) criteria.getFilterValue(
				CashAccountBalanceDetailsReportData.FILTER_CLIENT_ID);
		
		contractNumber = (Integer) criteria.getFilterValue(
				CashAccountBalanceDetailsReportData.FILTER_CONTRACT_NUMBER);
		
		java.util.Date from = (java.util.Date) criteria.getFilterValue(
				CashAccountBalanceDetailsReportData.FILTER_FROM_DATE);
		
		if ( from != null ) {
			fromDate = new java.sql.Date(from.getTime());
		}
		
		java.util.Date to = (java.util.Date) criteria.getFilterValue(
				CashAccountBalanceDetailsReportData.FILTER_TO_DATE);

		if ( to != null ) {
			toDate = new java.sql.Date(to.getTime());
		}
		
	}
	
	/**
	 * Retrieve the values from the ReportCriteria Object for the Forfeitures
	 *
	 * @param criteria
	 * @throws SystemException
	 */
	private void getForfeituresFilterValues(ReportCriteria criteria)
			throws SystemException {

		clientId = (String) criteria.getFilterValue(
				CashAccountForfeituresReportData.FILTER_CLIENT_ID);
		
		contractNumber = (Integer) criteria.getFilterValue(
				CashAccountForfeituresReportData.FILTER_CONTRACT_NUMBER);
		
		java.util.Date from = (java.util.Date) criteria.getFilterValue(
				CashAccountForfeituresReportData.FILTER_FROM_DATE);
		
		if ( from != null ) {
			fromDate = new java.sql.Date(from.getTime());
		}
		
		java.util.Date to = (java.util.Date) criteria.getFilterValue(
				CashAccountForfeituresReportData.FILTER_TO_DATE);

		if ( to != null ) {
			toDate = new java.sql.Date(to.getTime());
		}
		
		task = (String) criteria.getFilterValue(
				CashAccountForfeituresReportData.FILTER_TASK);
		
		moneyType = (String) criteria.getFilterValue(
				CashAccountForfeituresReportData.FILTER_MONEY_ID);

		return;
	}
	

	/**
	 * Gets the current balance from Apollo.
	 *
	 * @param connection
	 * @return current balance as Big Decimal
	 * @throws SQLException
	 */
	private BigDecimal retrieveCurrentBalance(Connection connection) 
	throws SQLException {

		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		BigDecimal amount = ZERO;

		try {
			stmt = connection.prepareStatement(SQL_SELECT_CURRENT_BALANCE);
			stmt.setString(1, clientId);
			stmt.setDate(2, toDate);
			stmt.setDate(3, toDate);

			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				amount = getBigDecimalOrZero(resultSet, CURRENT_BALANCE);
			}
			resultSet.close();
		} finally {
			close(stmt, null);
		}
		return amount;
	}
	
	/**
	 * Query String for retrieving cash account forfeitures detail items
	 *
	 * Order is in txn effective date, then transaction number
	 * all descending
	 * 
	 * @param criteria
	 * @return Query to retrieve Forfeitures transaction Items
	 */
	private static String getForfeituresTransactionItemsQueryString(
			ReportCriteria criteria){
		
		StringBuffer forfeituresTransactionsSQL = new StringBuffer();
		
		String moneyTypeFilter =  (String) criteria.getFilterValue(
        		CashAccountForfeituresReportData.FILTER_MONEY_ID);
		
		if (StringUtils.equalsIgnoreCase(moneyTypeFilter, 
				CashAccountForfeituresReportData.ALL_MONEY_TYPES_KEY)) {
			
			forfeituresTransactionsSQL.append(SQL_SELECT_WDW_TRANSACTION_HAVING_MONEY_TYPES);
			forfeituresTransactionsSQL.append(SQL_MONEY_TYPE_DEFAULT_FILTER_CLAUSE);
			forfeituresTransactionsSQL.append(SQL_UNION_ALL_STRING);
			forfeituresTransactionsSQL.append(SQL_SELECT_CA_TRANSACTIONS_NOT_HAVING_MONEY_TYPE);
			forfeituresTransactionsSQL.append(SQL_UNION_ALL_STRING);
			forfeituresTransactionsSQL.append(SQL_SELECT_WDW_TRANSACTIONS_NOT_HAVING_MONEY_TYPES);
			
		} else if (StringUtils.equalsIgnoreCase(moneyTypeFilter, 
				CashAccountForfeituresReportData.ADJUSTMENT_MONEY_TYPE_KEY)) {

			forfeituresTransactionsSQL.append(SQL_SELECT_CA_TRANSACTIONS_NOT_HAVING_MONEY_TYPE);
			
		} else if (StringUtils.isNotBlank(moneyTypeFilter)) {
			forfeituresTransactionsSQL.append(SQL_SELECT_WDW_TRANSACTION_HAVING_MONEY_TYPES);
			forfeituresTransactionsSQL.append(SQL_MONEY_TYPE_FILTER_CLAUSE);
		}
		
		//Sort logic Query appending
		forfeituresTransactionsSQL.append(forfeituresTransactionsSortLogic(criteria));
		
		return forfeituresTransactionsSQL.toString();
	}
	
	/**
	 * This method returns sort logic for Forfeitures Transactions SQL
	 * @param criteria
	 * @return StringBuffer object containing sort logic query
	 */
	private static StringBuffer forfeituresTransactionsSortLogic(ReportCriteria criteria){
		
		StringBuffer forfeituresTransactionsSortSQL = new StringBuffer();
		//Sort logic implementation - Start
		String sortDirection = null;
		if(criteria.getSorts().size() > 0){
			ReportSort sort = (ReportSort)criteria.getSorts().get(0);
			sortDirection = sort.getSortDirection();
			forfeituresTransactionsSortSQL.append(ORDERY_BY_CLAUSE);

			/*If TransactionDate is clicked we have to sort the List by
			 * 1. Descending Transaction date then by 
			 * 2. Descending Transaction Number then by
			 * 3. Ascending Money Type
			 * If Transaction Date is again clicked reverse of above happens 
			 * i.e., Ascending Transaction date, Ascending transaction number, Descending Money Type 
			*/
			if (TRANSACTION_DATE.equals(sort.getSortField())){
				forfeituresTransactionsSortSQL.append(" " + fieldToColumnMap.get(sort.getSortField()) + " " + sortDirection);//1
				forfeituresTransactionsSortSQL.append(", " + fieldToColumnMap.get(TRANSACTION_NUMBER));//2
				forfeituresTransactionsSortSQL.append(" " + sortDirection);
				forfeituresTransactionsSortSQL.append(", " + fieldToColumnMap.get(MONEY_TYPE_CONSTANT));//3
				forfeituresTransactionsSortSQL.append(" " + (DESCENDING_ORDER.equals(sortDirection)? ASCENDING_ORDER: DESCENDING_ORDER));
			}
			
			/*If Money Type is clicked we have to sort the List by
			 * 1. Ascending Money Type then by
			 * 2. Descending Transaction date then by 
			 * 3. Descending Transaction Number
			 * If Money Type is again clicked reverse of the above should happen
			*/
			else if(MONEY_TYPE_CONSTANT.equals(sort.getSortField())){
				forfeituresTransactionsSortSQL.append(" " + fieldToColumnMap.get(sort.getSortField()) + " " + sortDirection);//1
				forfeituresTransactionsSortSQL.append(", " + fieldToColumnMap.get(TRANSACTION_DATE));//2
				forfeituresTransactionsSortSQL.append(" " + (ASCENDING_ORDER.equals(sortDirection)? DESCENDING_ORDER: ASCENDING_ORDER));
				forfeituresTransactionsSortSQL.append(", " + fieldToColumnMap.get(TRANSACTION_NUMBER));//3
				forfeituresTransactionsSortSQL.append(" "+(ASCENDING_ORDER.equals(sortDirection)? DESCENDING_ORDER : ASCENDING_ORDER));
			}
			forfeituresTransactionsSortSQL.append(" ");
			//Sort logic implementation -- End
		}
		return forfeituresTransactionsSortSQL;
		
	}
	
	/**
	 * This method returns the values for the money type drop down in 
	 * CashAccount Forfeitures page 
	 * 
	 * @param criteria
	 * @return ContractMoneyType List
	 * @throws SystemException
	 * @throws ReportServiceException
	 * @author Chavva Akhilesh
	 */
	public List<ContractMoneyType> getContractUMMoneyType(
			ReportCriteria criteria) throws SystemException, ReportServiceException {
		
		Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        List<ContractMoneyType> forfeituresMoneyTypeObjects = new ArrayList<ContractMoneyType>();
        
        try {
        	conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
        	
            statement = conn.prepareStatement(SQL_SELECT_FORFEITURES_MONEY_TYPE);
        
            contractNumber = (Integer) criteria.getFilterValue(
            		CashAccountBalanceDetailsReportData.FILTER_CONTRACT_NUMBER);
            
            statement.setInt(1, contractNumber);
            statement.execute();
            rs = statement.getResultSet();
            
            if(rs != null){
            	while(rs.next()){
            		ContractMoneyType contractMoneyType = new ContractMoneyType();
            		contractMoneyType.setMoneyTypeMediumName(rs.getString(CashAccountForfeituresReportData.MONEY_TYPE_MED_NAME));
            		contractMoneyType.setMoneyTypeId(rs.getString(CashAccountForfeituresReportData.MONEY_TYPE_ID));
            		
            		forfeituresMoneyTypeObjects.add(contractMoneyType);
            	}
            	rs.close();
            }        	

        }catch (SQLException e) {
        	handleSqlException(e, className,
					"getContractUMMoneyType",
					" contract: " + contractNumber.intValue());
        } finally {
			close(statement, conn);
        }
        
		return forfeituresMoneyTypeObjects;
		
	} 
	
	/**
	 * Returns the total Forfeitures in Participant account
	 * 
	 * @param criteria
	 * @return value for TotalForfeituresInParticipantAccounts
	 * @throws SystemException
	 * @throws ReportServiceException 
	 */
	private BigDecimal getTotalForfeituresInParticipantAccount(
			ReportCriteria criteria) throws SystemException, ReportServiceException {
		
		Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        BigDecimal totalForfeituresInParticipantAccount = ZERO;
        
        try {
        	conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
        	statement = conn.prepareStatement(SQL_SELECT_TOTAL_FORFEITURES_IN_PARTICIPANT_ACCOUNT);
        	
            contractNumber = (Integer) criteria.getFilterValue(
            		CashAccountBalanceDetailsReportData.FILTER_CONTRACT_NUMBER);
            
            statement.setInt(1, contractNumber);
            statement.execute();
            rs = statement.getResultSet();
            
            if(rs != null){
            	if(rs.next())
            		totalForfeituresInParticipantAccount = 
            			getBigDecimalOrZero(rs, TOTAL_ASSESTS);

            	rs.close();
            }
        }catch (SQLException e) {
        	handleSqlException(e, className,
					"getTotalForfeituresInParticipantAccount",
					"; contract: " + contractNumber.intValue());
        } finally {
			close(statement, conn);
        }
		return totalForfeituresInParticipantAccount;
		
	}
	
	/**
	 * Returns the total Forfeitures in cash account
	 * 
	 * @param criteria
	 * @return value for totalForfeituresInCashAccount
	 * @throws SystemException
	 * @throws ReportServiceException 
	 */
	private BigDecimal getTotalForfeituresInCashAccount(
			ReportCriteria criteria) throws SystemException, ReportServiceException {
		
		Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        BigDecimal totalForfeituresInCashAccount = ZERO;

        getForfeituresFilterValues(criteria);
        
        try {
        	conn = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);
        	statement = conn.prepareStatement(SQL_SELECT_TOTAL_FORFEITURES_IN_CASH_ACCOUNT);
        	
        	statement.setString(1, clientId);
        	statement.setInt(2, contractNumber.intValue());
        	statement.setDate(3, fromDate);
        	statement.setDate(4, toDate);
        	statement.execute();
            rs = statement.getResultSet();
            
            if(rs != null){
            	if(rs.next())
            		totalForfeituresInCashAccount = getBigDecimalOrZero(rs, TOTAL_AMOUNT);

            	rs.close();
            }
        }catch (SQLException e) {
        	handleSqlException(e, className,
					"getTotalForfeituresInCashAccount",
					"; client id : " + clientId 
        			+ "; contract: " + contractNumber.intValue()
					+ "; from date : " + fromDate
					+ "; to date : " + toDate);
        } finally {
			close(statement, conn);
        }
		return totalForfeituresInCashAccount;
	}
	
	/**
	 * This inner class transforms a result set into a TransactionDataItem
	 * object for the balance details page
	 */	
	private static class BalanceDetailsTransactionDataItemTransformer extends ReportItemTransformer {
		public BalanceDetailsTransactionDataItemTransformer() {
		}
		
		public TransactionDataItem transform(ResultSet resultSet) throws SQLException {
			TransactionDataItem item = new TransactionDataItem();

			item.setTransactionEffectiveDate(resultSet.getDate(TX_EFF_DATE));
			item.setTransactionType(getString(resultSet, TX_TYPE));
			item.setTransactionNumber(getString(resultSet, TX_NUMBER));
			item.setTransactionReasonCode(getString(resultSet,TX_REASON_CODE));
			item.setTransactionReasonCodeExcessWD(getString(resultSet,TX_REASON_CODE_EXCESS_WD));
			item.setTransactionStatusCode(getString(resultSet,TX_STATUS_CODE));
			item.setOriginalAmount(getBigDecimalOrZero(resultSet,TX_ORIGINAL_AMOUNT));
			item.setAvailableAmount(getBigDecimalOrZero(resultSet,TX_AVAILABLE_AMOUNT));
			item.getParticipant().setSsn(getString(resultSet, PART_SSN));

			String participantName = resultSet.getString(PART_NAME);
			if (participantName != null && participantName.length() >= 40) {
				item.getParticipant().setLastName(participantName.substring(0, 19).trim());
				item.getParticipant().setFirstName(participantName.substring(21, 40).trim());
			}

			return item;
		}
		
	}
	
	/**
	 * This inner class transforms a result set into a TransactionDataItem
	 * object for the Forfeitures page
	 */	
	private static class ForfeituresTransactionDataItemTransformer extends ReportItemTransformer {
		public ForfeituresTransactionDataItemTransformer() {
		}
		
		public TransactionDataItem transform(ResultSet resultSet) throws SQLException {
			TransactionDataItem item = new TransactionDataItem();

			item.setTransactionEffectiveDate(resultSet.getDate(TX_EFF_DATE));
			item.setTransactionType(getString(resultSet, TX_TYPE));
			item.setTransactionNumber(getString(resultSet, TX_NUMBER));
			item.setTransactionReasonCode(getString(resultSet,TX_REASON_CODE));
			item.setTransactionReasonCodeExcessWD(getString(resultSet,TX_REASON_CODE_EXCESS_WD));
			item.setTransactionStatusCode(getString(resultSet,TX_STATUS_CODE));
			item.setOriginalAmount(getBigDecimalOrZero(resultSet,TX_ORIGINAL_AMOUNT));
			item.setAvailableAmount(getBigDecimalOrZero(resultSet,TX_AVAILABLE_AMOUNT));
			item.setMoneyType(getString(resultSet,MONEY_TYPE));
			item.getParticipant().setSsn(getString(resultSet, PART_SSN));

			String participantName = resultSet.getString(PART_NAME);
			if (participantName != null && participantName.length() >= 40) {
				item.getParticipant().setLastName(participantName.substring(0, 19).trim());
				item.getParticipant().setFirstName(participantName.substring(21, 40).trim());
			}

			return item;
		}		
	}	
}

