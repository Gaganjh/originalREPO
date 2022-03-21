package com.manulife.pension.ps.service.report.transaction.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionType;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDataItem;
import com.manulife.pension.service.report.dao.ReportItemTransformer;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * This class assembles the data required for the Cash Account transaction. It
 * uses db2connect to access mainframe Apollo tables directly. The schema it
 * uses is defined in the System.properties in the PlanSponsor Server instance.
 *
 * @author Maria Lee
 * @author Charles Chan
 */
public class CashAccountDAO extends ReportServiceBaseDAO {

	private static final String className = CashAccountDAO.class.getName();

	private static final Logger logger = Logger.getLogger(CashAccountDAO.class);
	private static final String OPENING_BALANCE_TABLE = ".vlp7221";
	private static final String DEBIT_TABLE = ".vlp7222";
	private static final String CREDIT_TABLE = ".vlp7223";

	private static final String CONTRIBUTION_TXN = "AL";
	private static final String CURRENT_BALANCE = "currentBalance";
	private static final String OPENING_BALANCE = "openingBalance";
	private static final String TOTAL_CREDITS = "totCredits";
	private static final String TOTAL_DEBITS = "totDebits";
	private static final String TOTAL_DEBITS_REVERSAL = "totDebitsReversal";
	private static final String PAYROLL_ENDING_DATE = "payrollEndingDate";
	private static final String TOTAL_CREDITS_COUNT = "totNumOfCredits";
	private static final String TOTAL_DEBITS_COUNT = "totNumOfDebits";
	private static final String TOTAL_DEBITS_REVERSAL_COUNT = "totDebitsRevCount";

	private static final String TX_EFF_DATE = "txEffDate";
	private static final String TX_TYPE = "txType";
	private static final String TX_NUMBER = "txNumber";
	private static final String TX_REASON_CODE = "txReasonCode";
	private static final String TX_REASON_CODE_EXCESS_WD = "txReasonCodeExWD";
	private static final String TX_STATUS_CODE = "txStatusCode";
	private static final String TX_AMOUNT = "txAmount";
	private static final String TX_MODE = "txMode";
	private static final String MONEY_SOURCE = "moneySource";
	private static final String RATE_EFFECTIVE_DATE = "rateEffectiveDate";
	private static final String PART_SSN = "partSSN";
	private static final String PART_NAME = "partName";
	private static final String FLAG = "flag";
	private static final String PARTY_CODE ="prtyrlcd";
	private static final String FEE_TYPE ="feetype";

	private static final BigDecimal ZERO = new BigDecimal("0");
	private static final int MAXIMUM = 500;

	private static final String CURRENT_BALANCE_QUERY = getCurrentBalanceQueryString();
	private static final String OPENING_BALANCE_QUERY = getOpeningBalanceQueryString();
	private static final String TOTAL_CREDITS_QUERY = getTotalCreditsQueryString();
	private static final String TOTAL_DEBITS_QUERY = getTotalDebitsQueryString();
	private static final String TOTAL_DEBITS_REVERSAL_QUERY = getTotalDebitsReversalQueryString();
	private static final String MULTIPLE_CONTRACTS_QUERY = getMultipleContractsQueryString();
	private static final String TRANSACTION_ITEMS_QUERY = getTransactionItemsQueryString();
	private static final String TOTAL_CREDITS_COUNT_QUERY = getTotalCreditsCountQueryString();
	private static final String TOTAL_DEBITS_COUNT_QUERY = getTotalDebitsCountQueryString();
	private static final String TOTAL_DEBITS_REVERSAL_COUNT_QUERY = getTotalDebitsReversalCountQueryString();

	private String clientId;
	private Integer contractNumber;
	private java.sql.Date fromDate;
	private java.sql.Date toDate;
	private java.sql.Date asOfDate;
	private String task = "";
	private String page = "";
	
	private static final TransactionDataItemTransformer transformer = new TransactionDataItemTransformer();

	/**
	 * Gets the summary data from Apollo.
	 *
	 */
	public CashAccountReportData getSummaryData(ReportCriteria criteria)
			throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getSummaryData");
			logger.debug("schema set to -> " + APOLLO_SCHEMA_NAME);
		}

		getFilterValues(criteria);
		CashAccountReportData reportData = null;

		Connection connection = null;

		try {
			connection = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);

			BigDecimal currentBalance = retrieveCurrentBalance(connection);
			BigDecimal openingBalance = retrieveOpeningBalance(connection);
			

			boolean multipleContracts = retrieveClientHasMultipleContracts(connection);

			// arbitrarily set total count to zero, it will be set later
			reportData = new CashAccountReportData(criteria, 0);
			reportData.setCurrentBalance(currentBalance);
			reportData.setOpeningBalanceForPeriod(openingBalance);
			
			reportData.setHasMultipleContracts(multipleContracts);

		} catch (SQLException e) {
			handleSqlException(e, className,
					"getSummaryData",
					"Client: " + clientId +
					"; contract: " + contractNumber.intValue() +
					"; fromDate:" + fromDate +
					"; toDate:" + toDate);
		} finally {
			close(null, connection);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getSummaryData");
		}

		return reportData;
	}

	/**
	 * Gets the cash account history items from Apollo.
	 *
	 */
	@SuppressWarnings("deprecation")
	public List<TransactionDataItem> getTransactionItems(ReportCriteria criteria,
			CashAccountReportData reportData) throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getTransactionItems");
			logger.debug("client ID is [" + clientId + "]");
			logger.debug("fromDate is [" + fromDate + "]");
			logger.debug("toDate is [" + toDate + "]");
			logger.debug("contractNumber is [" + contractNumber + "]");
		}

		getFilterValues(criteria);
		List<TransactionDataItem> list = null;
		Connection connection = null;

		try {
			connection = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);

			int totalCount = getApproximateTotalCount(connection);
			if(!page.equalsIgnoreCase("PSW") || (task.equalsIgnoreCase("print") || task.equalsIgnoreCase("download"))){
				if (hasTooManyItems(totalCount)) {
					reportData.setTotalCount(totalCount);
					reportData.setHasTooManyItems(true);
					return null;
				}
			}

			list = retrieveTransactionItems(connection);

			// new changes start
			BigDecimal totalDebits = BigDecimal.ZERO;
			BigDecimal totalDebitsSum = BigDecimal.ZERO;
			
			Iterator<TransactionDataItem> iterator = list.iterator();
			while (iterator.hasNext()) {
				TransactionDataItem transactionDataItem = iterator.next();
				if ("F".equals(transactionDataItem.getTransactionMode())
						&& "Y".equals(transactionDataItem.getDebitTiedToCreditFlag())) {

					totalDebits = (BigDecimal) transactionDataItem.getTransactionAmount();
					
					if (totalDebits != null) {
						totalDebitsSum = totalDebitsSum.add(totalDebits);
					}
				}
			}
			
			BigDecimal totalCredits = retrieveTotalCredits(connection);
			
			if (totalCredits != null) {
				BigDecimal totalReversalDebits = retrieveTotalDebitsReversal(connection);
				if (totalReversalDebits != null) {
					// reversals are negatives, we are adding the reversals to
					// the
					// credits
					totalCredits = totalCredits.subtract(totalReversalDebits);
				}
			}
			reportData.setTotalCreditsForPeriod(totalCredits);
			reportData.setTotalDebitsForPeriod(totalDebitsSum);

			reportData.setClosingBalanceForPeriod(
					calculateClosingBalance(reportData.getOpeningBalanceForPeriod(), totalCredits, totalDebitsSum));
			
			reportData.setTotalCount(list.size());		
						
			reportData.setHasTooManyItems(false);
			
		} catch (SQLException e) {
			handleSqlException(e, className,
					"getSummaryData",
					"Client: " + clientId +
					"; contract: " + contractNumber.intValue() +
					"; fromDate:" + fromDate +
					"; toDate:" + toDate);
		} finally {
			close(null, connection);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getTransactionItems");
		}
		return list;
	}

	/**
	 * Calculate closing balance closing balance = opening balance + total
	 * credits - total debits
	 *
	 */
	private BigDecimal calculateClosingBalance(BigDecimal openingBalance,
			BigDecimal totalCredits, BigDecimal totalDebits) {

		BigDecimal closingBalance = openingBalance;

		if (openingBalance != null && totalCredits != null
				&& totalDebits != null) {
			closingBalance = closingBalance.add(totalCredits).subtract(
					totalDebits);
		}
		return closingBalance;
	}

	/**
	 * Gets the current balance from Apollo.
	 *
	 */
	private BigDecimal retrieveCurrentBalance(Connection connection) throws SQLException
	{

		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		BigDecimal amount = ZERO;

		try {
		
			stmt = connection.prepareStatement(CURRENT_BALANCE_QUERY);
			stmt.setString(1, clientId);
			stmt.setDate(2, asOfDate);
			stmt.setDate(3, asOfDate);

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
	 * Gets the opening balance from Apollo.
	 *
	 */
	private BigDecimal retrieveOpeningBalance(Connection connection) throws SQLException
	{

		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		BigDecimal amount = ZERO;

		try {
			stmt = connection.prepareStatement(OPENING_BALANCE_QUERY);
			stmt.setString(1, clientId);
			stmt.setDate(2, fromDate);
			stmt.setDate(3, fromDate);
			stmt.setInt(4, contractNumber.intValue());
			stmt.setDate(5, fromDate);

			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				amount = getBigDecimalOrZero(resultSet, OPENING_BALANCE);
			}
		} finally {
			close(stmt, null);
		}
		return amount;
	}

	/**
	 * Gets the total credits for the requesting period
	 *
	 */
	private BigDecimal retrieveTotalCredits(Connection connection) throws SQLException
	{

		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		BigDecimal amount = ZERO;

		try {
			stmt = connection.prepareStatement(TOTAL_CREDITS_QUERY);
			stmt.setString(1, clientId);
			stmt.setInt(2, contractNumber.intValue());
			stmt.setDate(3, fromDate);
			stmt.setDate(4, toDate);

			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				amount = getBigDecimalOrZero(resultSet, TOTAL_CREDITS);
			}

		} finally {
			close(stmt, null);
		}
		return amount;
	}

	/**
	 * Gets the total credits count for the requesting period
	 *
	 */
	private int retrieveTotalCreditsCount(Connection connection) throws SQLException
	{

		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		int count = 0;

		try {
			stmt = connection.prepareStatement(TOTAL_CREDITS_COUNT_QUERY);
			stmt.setString(1, clientId);
			stmt.setInt(2, contractNumber.intValue());
			stmt.setDate(3, fromDate);
			stmt.setDate(4, toDate);

			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				count = resultSet.getInt(TOTAL_CREDITS_COUNT);
			}

		} finally {
			close(stmt, null);
		}
		return count;
	}

	/**
	 * Gets the total debit reversals for the requesting period
	 *
	 */
	private BigDecimal retrieveTotalDebitsReversal(Connection connection) throws SQLException
	{

		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		BigDecimal amount = ZERO;

		try {
			stmt = connection.prepareStatement(TOTAL_DEBITS_REVERSAL_QUERY);
			stmt.setInt(1, contractNumber.intValue());
			stmt.setDate(2, fromDate);
			stmt.setDate(3, toDate);

			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				amount = getBigDecimalOrZero(resultSet, TOTAL_DEBITS_REVERSAL);
			}

		} finally {
			close(stmt, null);
		}
		return amount;
	}

	/**
	 * Gets the total debit reversals count for the requesting period
	 *
	 */
	private int retrieveTotalDebitsReversalCount(Connection connection) throws SQLException	{

		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		int count = 0;

		try {
			stmt = connection
					.prepareStatement(TOTAL_DEBITS_REVERSAL_COUNT_QUERY);
			stmt.setInt(1, contractNumber.intValue());
			stmt.setDate(2, fromDate);
			stmt.setDate(3, toDate);

			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				count = resultSet.getInt(TOTAL_DEBITS_REVERSAL_COUNT);
			}

		} finally {
			close(stmt, null);
		}
		return count;
	}

	/**
	 * Gets the total debits for the requesting period
	 *
	 */
	private BigDecimal retrieveTotalDebits(Connection connection) throws SQLException
	{

		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		BigDecimal amount = ZERO;

		try {
			stmt = connection.prepareStatement(TOTAL_DEBITS_QUERY);
			stmt.setInt(1, contractNumber.intValue());
			stmt.setDate(2, fromDate);
			stmt.setDate(3, toDate);

			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				amount = getBigDecimalOrZero(resultSet, TOTAL_DEBITS);
			}

		} finally {
			close(stmt, null);
		}
		return amount;
	}

	/**
	 * Gets the total debits count for the requesting period
	 *
	 */
	private int retrieveTotalDebitsCount(Connection connection) throws SQLException
	{

		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		int count = 0;

		try {
			stmt = connection.prepareStatement(TOTAL_DEBITS_COUNT_QUERY);
			stmt.setInt(1, contractNumber.intValue());
			stmt.setDate(2, fromDate);
			stmt.setDate(3, toDate);

			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				count = resultSet.getInt(TOTAL_DEBITS_COUNT);
			}

		} finally {
			close(stmt, null);
		}
		return count;
	}

	/**
	 * Finds out if the contracts has multiple contracts
	 *
	 */
	private boolean retrieveClientHasMultipleContracts(Connection connection) throws SQLException
	{

		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		boolean multipleContracts = false;
		int contractNumber = -1;

		try {
			stmt = connection.prepareStatement(MULTIPLE_CONTRACTS_QUERY);
			stmt.setString(1, clientId);

			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				contractNumber = resultSet
						.getInt(CashAccountReportData.FILTER_CONTRACT_NUMBER);
			}

		} finally {
			close(stmt, null);
		}
		// if sql returns a zero then it is a client with multiple contracts
		if (contractNumber == 0) {
			multipleContracts = true;
		}
		return multipleContracts;
	}
	
	/**
	 * Gets the cash account history for the requesting period
	 *
	 */
	private List<TransactionDataItem> retrieveTransactionItems(Connection connection) throws SQLException
	{

		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		List<TransactionDataItem> transactions = new ArrayList<TransactionDataItem>();
		try {
			stmt = connection.prepareStatement(TRANSACTION_ITEMS_QUERY);
			stmt.setString(1, clientId);                                 
			stmt.setInt(2, contractNumber.intValue());
			stmt.setDate(3, fromDate);
			stmt.setDate(4, toDate);
			
			stmt.setString(5, clientId);
			stmt.setInt(6, contractNumber.intValue());
			stmt.setDate(7, fromDate);
			stmt.setDate(8, toDate);
			
			stmt.setInt(9, contractNumber.intValue());
			stmt.setDate(10, fromDate);
			stmt.setDate(11, toDate);
			
			stmt.setInt(12, contractNumber.intValue());
			stmt.setDate(13, fromDate);
			stmt.setDate(14, toDate);
			
			stmt.setInt(15, contractNumber.intValue());
			stmt.setDate(16, fromDate);
			stmt.setDate(17, toDate);
			
		
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {

				TransactionDataItem item = new TransactionDataItem();

				item.setTransactionEffectiveDate(resultSet.getDate(TX_EFF_DATE));
				item.setTransactionType(getString(resultSet, TX_TYPE));
				item.setTransactionNumber(getString(resultSet, TX_NUMBER));

		/* LS. Fees project November 2006	*/

				if (TransactionType.FEES_TRANSACTION.equalsIgnoreCase(item.getTransactionType()))
				{
					String partyCode = getString(resultSet, PARTY_CODE).trim();
					String feeType = getString(resultSet, FEE_TYPE).trim();
					String reasonCode = partyCode.concat(feeType);
					item.setTransactionReasonCode(reasonCode);
				}
		//fees
				else
				item.setTransactionReasonCode(getString(resultSet, TX_REASON_CODE));
				item.setTransactionReasonCodeExcessWD(getString(resultSet, TX_REASON_CODE_EXCESS_WD));
				item.setTransactionStatusCode(getString(resultSet, TX_STATUS_CODE));
				item.setTransactionAmount(getBigDecimalOrZero(resultSet, TX_AMOUNT));
				item.setTransactionMode(getString(resultSet, TX_MODE));
				item.setMoneySource(getString(resultSet, MONEY_SOURCE));
				item.setRateEffectiveDate(resultSet.getDate(RATE_EFFECTIVE_DATE));
				item.getParticipant().setSsn(getString(resultSet, PART_SSN));

				String participantName = resultSet.getString(PART_NAME);
				if (participantName != null && participantName.length() >= 40) {
					item.getParticipant().setLastName(participantName.substring(0, 19).trim());
					item.getParticipant().setFirstName(participantName.substring(21, 40).trim());
				}
				item.setDebitTiedToCreditFlag(getString(resultSet, FLAG));
				
				if (isContributionType(item.getTransactionType())) {
				item.setPayrollEndingDate( resultSet.getDate(PAYROLL_ENDING_DATE));
				}
				transactions.add(item);

			}
		} finally {
			close(stmt, null);
		}

		return transactions;
	}


	
	

	/**
	 * Determine if the transaction if a contribution type
	 *
	 */
	private boolean isContributionType(String txType) {
		return CONTRIBUTION_TXN.equalsIgnoreCase(txType) ? true : false;
	}

	/**
	 * Determine if the history returned has more than 500 items and the range
	 * can be further reduced.
	 */
	private boolean hasTooManyItems(int size) {
		boolean tooMany = size > MAXIMUM ? true : false;

		if (tooMany) {
			/*
			 * If user cannot further reduce the range, we should still
			 * display everything.
			 */
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
	 * Gets the counts of the various queries.
	 *
	 */
	private int getApproximateTotalCount(Connection connection)
			throws SystemException, ReportServiceException, SQLException {

		int totalCreditsCount = retrieveTotalCreditsCount(connection);
		int totalDebitsCount = retrieveTotalDebitsCount(connection);
		int totalDebitsReversalCount = retrieveTotalDebitsReversalCount(connection);

		return totalCreditsCount + totalDebitsCount + totalDebitsReversalCount;
	}

	/**
	 * Retrieve the values from the ReportCriteria
	 *
	 */
	private void getFilterValues(ReportCriteria criteria)
			throws SystemException {

		clientId = (String) criteria
				.getFilterValue(CashAccountReportData.FILTER_CLIENT_ID);
		contractNumber = (Integer) criteria
				.getFilterValue(CashAccountReportData.FILTER_CONTRACT_NUMBER);
		java.util.Date from = (java.util.Date) criteria
				.getFilterValue(CashAccountReportData.FILTER_FROM_DATE);
		java.util.Date to = (java.util.Date) criteria
				.getFilterValue(CashAccountReportData.FILTER_TO_DATE);
		java.util.Date asOf = (java.util.Date)criteria
				.getFilterValue(CashAccountReportData.FILTER_AS_OF_DATE);
		
		if(criteria.getFilterValue("TASK") != null){
			task = (String) criteria.getFilterValue("TASK");
		}				
		if(criteria
			.getFilterValue(CashAccountReportData.FILTER_PAGE) != null){
			page = (String) criteria
				.getFilterValue(CashAccountReportData.FILTER_PAGE);
		}

		if ( from != null ) {
			fromDate = new java.sql.Date(from.getTime());
		}
		if ( to != null ) {
			toDate = new java.sql.Date(to.getTime());
		}
		if ( asOf != null ) {
			asOfDate = new java.sql.Date(asOf.getTime() );
		}

		return;
	}

	/**
	 * Query for retrieving current balance
	 *
	 */
	private static final String getCurrentBalanceQueryString() {

		SqlStringBuffer stmt = new SqlStringBuffer();
		stmt.append("	  SELECT OBAMT AS ").append(CURRENT_BALANCE)
			.append(" FROM ").append(APOLLO_SCHEMA_NAME).append(OPENING_BALANCE_TABLE)
   			.append(" WHERE 					")
			.append(" CLNTID = ? 					")
			.append(" AND (OBSTRDT - 1 DAY) <= ? 	")
			.append(" AND OBENDDT >  ?				");

		return stmt.toString();
	}

	/**
	 * Query String for retrieving opening balance
	 *
	 */
	private static final String getOpeningBalanceQueryString() {

		SqlStringBuffer stmt = new SqlStringBuffer();
		
		stmt.append(" WITH CTE01 (T7221_BAL) AS ")
		     .append(" ( SELECT OBAMT " )
		     .append(" FROM ")
		     .append(APOLLO_SCHEMA_NAME).append(".VLP7221 ")
		     .append(" WHERE CLNTID = ? ")
		     .append(" AND OBSTRDT <= ? ")
		     .append(" AND OBENDDT >= ? )" )
		     .append(" ,CTE02 (T1066_BAL) AS ")
		     .append(" (SELECT COALESCE(SUM(A.TXAMT),0) " )
		     .append("FROM ")
		     .append(APOLLO_SCHEMA_NAME).append(".VLP1066 A ," )
		     .append(APOLLO_SCHEMA_NAME).append(".VLP1014 B " )
		     .append(" WHERE A.PROPNO   = B.PROPNO ")
		     .append(" AND B.CNNO     = ? ")
		     .append(" AND A.TRSTATCD = 'CM' ")
		     .append(" AND A.TRTYP    IN ('CR','PC')")
		     .append(" AND A.TRRSNCD  IN ('OP','RC') ")
		     .append(" AND ? BETWEEN A.TREFDT AND A.ACCTPRDT ")
		     .append(" AND NOT EXISTS  (SELECT 1 ")
		     .append("  FROM ")
		     .append(APOLLO_SCHEMA_NAME).append(" .VLP1143 T1143 " )
		     .append(" WHERE T1143.TRTYP1     = 'PC' ")
		     .append(" AND   T1143.TRTYP2     = 'PC' ")
		     .append(" AND   T1143.ASSORSN    = 'RV' ")
		     .append(" AND   A.TRANNO         = T1143.TRANNO2 )) ")
		     .append(" SELECT COALESCE((T7221_BAL+T1066_BAL),0) AS openingBalance ")
		     .append(" FROM CTE01, CTE02 ");
	
		return stmt.toString();
	}

	/**
	 * Query String for retrieving total credits
	 *
	 */
	private static final String getTotalCreditsQueryString() {

		SqlStringBuffer stmt = new SqlStringBuffer();
		stmt.append("SELECT ")
			.append("		SUM(CRAMT) AS ")
			.append(TOTAL_CREDITS)
			.append(" FROM ")
			.append(APOLLO_SCHEMA_NAME)
			.append(CREDIT_TABLE)
			.append(" WHERE ")
			.append("  	CLNTID = ?")
			.append(" AND CNNO IN ( 0, ?)")
			.append(" AND CREFDT BETWEEN ? AND ?");

		return stmt.toString();
	}

	/**
	 * Query String for retrieving total reversal debits
	 *
	 */
	private static final String getTotalDebitsReversalQueryString() {

		SqlStringBuffer stmt = new SqlStringBuffer();
		stmt.append("SELECT ")
			.append("		SUM(TXAMT) AS ")
			.append(TOTAL_DEBITS_REVERSAL)
			.append(" FROM ")
			.append(APOLLO_SCHEMA_NAME)
			.append(DEBIT_TABLE)
			.append(" WHERE ")
			.append("  	CNNO = ?")
			.append(" AND CRTIE = 'Y'")
			.append(" AND TRANMODE = 'R'")
			.append(" AND TREFDT BETWEEN ? AND ?");

		return stmt.toString();
	}

	/**
	 * Query String for retrieving total debits
	 *
	 */
	private static final String getTotalDebitsQueryString() {

		SqlStringBuffer stmt = new SqlStringBuffer();
		stmt.append("SELECT ")
			.append("		SUM(TXAMT) AS ")
			.append(TOTAL_DEBITS)
			.append(" FROM ")
			.append(APOLLO_SCHEMA_NAME)
			.append(DEBIT_TABLE)
			.append(" WHERE ")
			.append("  	CNNO = ?")
			.append(" AND CRTIE = 'Y'")
			.append(" AND TRANMODE = 'F'")
			.append(" AND TREFDT BETWEEN ? AND ?");

		return stmt.toString();
	}

	/**
	 * Query String for retrieving multiple contracts
	 *
	 */
	private static final String getMultipleContractsQueryString() {

		SqlStringBuffer stmt = new SqlStringBuffer();
		stmt.append("SELECT ")
			.append("		DISTINCT CNNO AS ")
			.append(CashAccountReportData.FILTER_CONTRACT_NUMBER)
			.append(" FROM ")
			.append(APOLLO_SCHEMA_NAME)
			.append(OPENING_BALANCE_TABLE)
			.append(" WHERE ")
			.append("  	CLNTID = ? ");

		return stmt.toString();
	}

	/**
	 * Query String for retrieving cash account history items
	 *
	 * Order is in txn effective date, debits then credits, transaction number
	 * all descending
	 *
	 */
	private static final String getTransactionItemsQueryString() {

		SqlStringBuffer stmt = new SqlStringBuffer();
		
		stmt.append(" SELECT ")
	    .append(" A.CREFDT txEffDate, ")
        .append(" A.TRTYP txType, ")
        .append(" A.TRANNO txNumber, ")
        .append(" A.CRRSN txReasonCode, ")
        .append(" B.TRRSNCD txReasonCodeExWD, ")
        .append(" A.CASTATCD txStatusCode, ")
        .append(" A.CRAMT txAmount, ")
        .append(" ' ' AS txMode, ")
        .append(" ' ' AS moneySource, ")
        .append(" DATE ('9999365') AS rateEffectiveDate, ")
        .append(" 0, ")
        .append(" C.PRTSSN partSSN, ")
        .append(" C.PRTLSTNM || ',' || C.PRTFSTNM partName, ")
        .append(" ' ' AS flag, ")
        .append(" ' ' AS prtyrlcd, ")
        .append(" ' ' AS feetype, ")
        .append(" DATE ('9999365') AS payrollEndingDate ")
        .append(" FROM ")
        .append(APOLLO_SCHEMA_NAME).append(".VLP7223 A ,")
        .append(APOLLO_SCHEMA_NAME).append(".VLP1066 B , ")
        .append(APOLLO_SCHEMA_NAME).append(".VLP1074 C ")
        .append(" WHERE C.PRTID = B.PRTID ")
        .append(" AND B.TRANNO = A.TRANNO ")
        .append(" AND A.TRTYP = 'WD' ")
        .append(" AND A.CLNTID = ? ")
        .append(" AND A.CNNO IN (0, ?) ")
        .append(" AND A.CREFDT BETWEEN ?  AND ? ")
        .append(" UNION ALL ")
        .append(" SELECT A.CREFDT txEffDate, ")
        .append(" A.TRTYP txType, ")
        .append(" A.TRANNO txNumber, ")
        .append(" A.CRRSN txReasonCode, ")
        .append(" ' ' AS txReasonCodeExWD, ")
        .append(" A.CASTATCD txStatusCode, ")
        .append(" A.CRAMT txAmount, ")
        .append(" ' ' AS txMode, ")
        .append(" ' ' AS moneySource, ")
        .append(" DATE ('9999365') AS rateEffectiveDate, ")
        .append(" 2, ")
        .append(" ' ' AS partSSN, ")
        .append(" ' ' AS partName, ")
        .append(" ' ' AS flag, ")
        .append(" ' ' AS prtyrlcd, ")
        .append(" ' ' AS feetype, ")
        .append(" B.CTRIBADT payrollEndingDate ")
        .append(" FROM ")
        .append(APOLLO_SCHEMA_NAME).append(".VLP7223 A ")
        .append(" LEFT OUTER JOIN ")
        .append(APOLLO_SCHEMA_NAME).append(".VLP7226 B ")
        .append(" ON  B.TRANNO = A.TRANNO ")
        .append(" AND B.CNNO   = A.CNNO ")
        .append(" WHERE A.TRTYP ¬= 'WD' ")
        .append(" AND A.CLNTID = ? ")
        .append(" AND A.CNNO IN (0, ?) ")
        .append(" AND A.CREFDT  BETWEEN ?  AND ? ")
        .append(" UNION ALL ")
        .append(" SELECT A.TREFDT txEffDate, ")
        .append(" A.TRTYP txType, ")
        .append(" A.TRANNO txNumber, ")
        .append(" A.TRRSNCD txReasonCode, ")
        .append(" ' ' AS txReasonCodeExWD, ")
        .append(" A.TRSTATCD txStatusCode, ")
        .append(" A.TXAMT txAmount, ")
        .append(" A.TRANMODE txMode, ")
        .append(" A.MLIMS moneySource, ")
        .append(" A.RTEFDT rateEffectiveDate , ")
        .append(" 3, ")
        .append(" ' ' AS partSSN, ")
        .append(" ' ' AS partName, ")
        .append(" A.CRTIE AS flag, ")
        .append(" A.PRTYRLCD prtyrlcd, ")
        .append(" A.RETRTYP feetype, ")
        .append(" B.CTRIBADT payrollEndingDate ")
        .append(" FROM ")
        .append(APOLLO_SCHEMA_NAME).append(".VLP7222 A ")
        .append(" LEFT OUTER JOIN ")
        .append(APOLLO_SCHEMA_NAME).append(".VLP7226 B ")
        .append(" ON  A.TRANNO = B.TRANNO ")
        .append(" AND  A.CNNO   = B.CNNO ")
        .append(" WHERE A.CNNO IN (   0,  ?) ")
        .append(" AND A.TREFDT  BETWEEN ?  AND ? ")
        .append(" AND A.TRTYP   NOT IN ('CR','PC') ")
        .append(" AND A.TRRSNCD NOT IN ('OP','RC') ")
        .append(" UNION ALL ")
        .append("  SELECT DISTINCT * FROM  ( ")
        .append(" SELECT ")
        .append(" B.ACCTPRDT AS txEffDate, ")
        .append(" A.TRTYP txType, ")
        .append(" A.TRANNO txNumber, ")
        .append(" A.TRRSNCD txReasonCode, ")
        .append(" ' ' AS txReasonCodeExWD, ")
        .append(" A.TRSTATCD txStatusCode, ")
        .append(" A.TXAMT txAmount, ")
        .append(" A.TRANMODE txMode, ")
        .append(" A.MLIMS moneySource, ")
        .append(" A.RTEFDT rateEffectiveDate, ")
        .append(" 4, ")
        .append(" ' ' AS partSSN, ")
        .append(" ' ' AS partName, ")
        .append(" A.CRTIE AS flag, ")
        .append(" A.PRTYRLCD prtyrlcd, ")
        .append(" A.RETRTYP feetype, ")
        .append(" DATE ('9999365') AS payrollEndingDate ")
        .append(" FROM ")
        .append(APOLLO_SCHEMA_NAME).append(".VLP7222 A ,")
        .append(APOLLO_SCHEMA_NAME).append(".VLP1066 B ")
        .append(" WHERE A.CNNO IN (   0, ?   ) ")
        .append(" AND B.ACCTPRDT  BETWEEN ?  AND ? ")
        .append(" AND A.TRANNO  = B.TRANNO ")
        .append(" AND A.TRTYP   IN ('CR','PC') ")
        .append(" AND A.TRRSNCD IN ('OP','RC') ")
        .append(" UNION  ")
		.append(" SELECT A.ACCTPRDT AS txEffDate ")
        .append(" , A.TRTYP    AS txType ")
        .append(" , A.TRANNO   AS txNumber ")
        .append(" , A.TRRSNCD  AS txReasonCode ")
        .append(" , ' '        AS txReasonCodeExWD ")
        .append(" , A.TRSTATCD AS txStatusCode ")
        .append(" , A.TXAMT    AS txAmount ")
        .append(" , A.TRANMODE AS txMode ")
        .append(" , ' '        AS moneySource ")
        .append(" , A.RTEFDT   AS rateEffectiveDate ")
        .append(" , 5 ")
        .append(" , ' '      AS partSSN ")
        .append(" , ' '      AS partName ")
        .append(" , 'Y'      AS flag ")
        .append(" , ' '      AS prtyrlcd ")
        .append(" , ' '      AS feetype ")
        .append(" , DATE ('9999365') AS payrollEndingDate ")
        .append(" FROM ")
        .append(APOLLO_SCHEMA_NAME).append(".VLP1066 A , ")
        .append(APOLLO_SCHEMA_NAME).append(".VLP1014 B  ")
        .append(" WHERE A.PROPNO   = B.PROPNO ")
        .append(" AND B.CNNO = ? ")
        .append(" AND A.TRTYP    IN ('CR','PC') ")
        .append(" AND A.TRRSNCD  IN ('OP','RC') ")
        .append(" AND A.TRSTATCD = 'CM' ")
        .append(" AND A.ACCTPRDT BETWEEN ?  AND ? ")
        .append(" AND A.TREFDT < CURRENT DATE - 27 MONTHS ")
        .append(" AND NOT EXISTS ")
        .append(" (  SELECT 1 ")
        .append(" FROM ")
        .append(APOLLO_SCHEMA_NAME).append(".VLP1143 T1143 ")
        .append(" WHERE T1143.TRTYP1     = 'PC' ")
        .append(" AND T1143.TRTYP2     = 'PC' ")
        .append("AND T1143.ASSORSN = 'RV' ")
        .append("AND A.TRANNO = T1143.TRANNO2))")
        .append(" ORDER BY 1 DESC, ")
        .append(" 10 DESC, ")
        .append(" 3 DESC ");
		

		return stmt.toString();
	}

	/**
	 * Query String for retrieving payroll ending dates
	 *
	 */
	private static final String getPayrollEndingDateQueryString() {

		SqlStringBuffer stmt = new SqlStringBuffer();
		stmt.append("SELECT ")
			.append("       TRANNO tranNumber, ")
			.append("		CTRIBADT payrollEndingDate")
			.append(" FROM ")
			.append(APOLLO_SCHEMA_NAME).append(".VLP7226 ")
			.append(" WHERE ")
			.append("   TRANNO = ? ")
			.append(" 	AND CNNO = ? ");

		return stmt.toString();
	}

	/**
	 * Query String for retrieving total credits count
	 *
	 */
	private static final String getTotalCreditsCountQueryString() {

		SqlStringBuffer stmt = new SqlStringBuffer();
		stmt.append("SELECT ")
			.append("		COUNT(*) AS ")
			.append(TOTAL_CREDITS_COUNT)
			.append(" FROM ")
			.append(APOLLO_SCHEMA_NAME).append(CREDIT_TABLE)
			.append(" WHERE ")
			.append("  	CLNTID = ? ")
			.append("   AND CNNO IN ( 0, ?) ")
			.append("   AND CREFDT BETWEEN ? AND ?");

		return stmt.toString();
	}

	/**
	 * Query String for retrieving total debits count
	 *
	 */
	private static final String getTotalDebitsCountQueryString() {

		SqlStringBuffer stmt = new SqlStringBuffer();
		stmt.append("SELECT ")
			.append("		COUNT(*) AS ")
			.append(TOTAL_DEBITS_COUNT)
			.append(" FROM ")
			.append(APOLLO_SCHEMA_NAME).append(DEBIT_TABLE)
			.append(" WHERE ")
			.append("  	CNNO = ?")
			.append(" AND CRTIE = 'Y'")
			.append(" AND TRANMODE = 'F'")
			.append(" AND TREFDT BETWEEN ? AND ?");

		return stmt.toString();
	}

	/**
	 * Query String for retrieving total reversal debits count
	 *
	 */
	private static final String getTotalDebitsReversalCountQueryString() {

		SqlStringBuffer stmt = new SqlStringBuffer();
		stmt.append("SELECT ")
			.append("		COUNT(*) AS ")
			.append(TOTAL_DEBITS_REVERSAL_COUNT)
			.append(" FROM ")
			.append(APOLLO_SCHEMA_NAME).append(DEBIT_TABLE)
			.append(" WHERE ")
			.append("  	CNNO = ?")
			.append(" AND CRTIE = 'Y'")
			.append(" AND TRANMODE = 'R'")
			.append(" AND TREFDT BETWEEN ? AND ?");

		return stmt.toString();
	}
	
	private static class TransactionDataItemTransformer extends ReportItemTransformer {
		public TransactionDataItemTransformer() {
		}
		
		public TransactionDataItem transform(ResultSet resultSet) throws SQLException {
			TransactionDataItem item = new TransactionDataItem();

			item.setTransactionEffectiveDate(resultSet.getDate(TX_EFF_DATE));
			item.setTransactionType(getString(resultSet, TX_TYPE));
			item.setTransactionNumber(getString(resultSet, TX_NUMBER));

			if (TransactionType.FEES_TRANSACTION.equalsIgnoreCase(item.getTransactionType())) {
				String partyCode = getString(resultSet, PARTY_CODE).trim();
				String feeType = getString(resultSet, FEE_TYPE).trim();
				String reasonCode = partyCode.concat(feeType);
				item.setTransactionReasonCode(reasonCode);
			} else {
				item.setTransactionReasonCode(getString(resultSet, TX_REASON_CODE));
			}
			item.setTransactionReasonCodeExcessWD(getString(resultSet, TX_REASON_CODE_EXCESS_WD));
			item.setTransactionStatusCode(getString(resultSet, TX_STATUS_CODE));
			item.setTransactionAmount(getBigDecimalOrZero(resultSet, TX_AMOUNT));
			item.setTransactionMode(getString(resultSet, TX_MODE));
			item.setMoneySource(getString(resultSet, MONEY_SOURCE));
			item.setRateEffectiveDate(resultSet.getDate(RATE_EFFECTIVE_DATE));
			item.getParticipant().setSsn(getString(resultSet, PART_SSN));

			String participantName = resultSet.getString(PART_NAME);
			if (participantName != null && participantName.length() >= 40) {
				item.getParticipant().setLastName(participantName.substring(0, 19).trim());
				item.getParticipant().setFirstName(participantName.substring(21, 40).trim());
			}
			item.setDebitTiedToCreditFlag(getString(resultSet, FLAG));

			return item;
		}
	}	

}

