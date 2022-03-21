/**
 * 
 */
package com.manulife.pension.ps.service.report.participant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantStatementItem;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantStatementsReportData;
import com.manulife.pension.service.report.dao.ReportItemTransformer;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.dao.SqlPair;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * @author arugupu
 *
 */
public class ParticipantStatementsDAO extends ReportServiceBaseDAO {

	/**
	 * Class name
	 */
	private static final String className = ParticipantStatementsDAO.class.getName();
	
	/**
	 * Logger object
	 */
	private static final Logger logger = Logger.getLogger(ParticipantStatementsDAO.class);
	
	/**
	 * A map of database column keyed by sort field.
	 */
	private static final Map<String, String> fieldToColumnMap = new HashMap<String, String>();

	/**
	 * Name of the Participant Last Name column in the resultSet. It is
	 * used to construct the ORDER BY clause.
	 */
	private static final String PARTICIPANT_LAST_NAME = "PARTICIPANT_LAST_NAME";

	/**
	 * Name of the Participant First Name in the resultSet. It is used to
	 * construct the ORDER BY clause.
	 */
	private static final String PARTICIPANT_FIRST_NAME = "PARTICIPANT_FIRST_NAME";
	
	/**
	 * Name of the SSN in the resultSet. It is used to
	 * construct the ORDER BY clause.
	 */
	private static final String SOCIAL_SECURITY_NO = "SOCIAL_SECURITY_NO";
	
	private static final String DEFAULT_SORT = "PARTICIPANT_LAST_NAME, PARTICIPANT_FIRST_NAME, SOCIAL_SECURITY_NO";
	
	private static final String AND = " and ";
	
	/**
	 * Transaction Item Transformer 
	 */
	private static final ParticipantStatementItemTransformer transformer = 
		new ParticipantStatementItemTransformer();
	
	
	static {
		/*
		 * Set up the field to column map.
		 */
		fieldToColumnMap.put(ParticipantStatementsReportData.SORT_LAST_NAME,
				PARTICIPANT_LAST_NAME);
		fieldToColumnMap.put(ParticipantStatementsReportData.SORT_FIRST_NAME,
				PARTICIPANT_FIRST_NAME);
		fieldToColumnMap.put(ParticipantStatementsReportData.SORT_SSN_FIELD,
				SOCIAL_SECURITY_NO);
		fieldToColumnMap.put(ParticipantStatementsReportData.DEFAULT_SORT,
				DEFAULT_SORT);
	}
	
	/**
	 * Retrieves all the Participants who can access statements and updates in reportData 
	 * 
	 * @param criteria
	 *            The report criteria to use.
	 * @param reportData
	 *            The report data to populate. Only the total count is updated
	 * @param dataItems
	 *            A list of ParticipantStatementItem. Transformation to
	 *            TransactionHistoryItem is done at the handler level.
	 * @throws SystemException
	 *             If anything goes wrong with the stored proc.
	 */
	@SuppressWarnings("unchecked")
	public static void getReportData(ReportCriteria criteria,
			ParticipantStatementsReportData reportData)
			throws SystemException, ReportServiceException {

		Connection connection = null;
		PreparedStatement stmt = null;

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		List<ParticipantStatementItem> dataItems = 
				new ArrayList<ParticipantStatementItem>();
		
		try {
			connection = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);

			// get the total record count, used for paging
			int totalCount = getTotalRecordCount(connection, criteria);
			if (logger.isDebugEnabled()) {
				logger.debug("Total Count [" + totalCount + "]");
			}

			stmt = getPreparedStatement(connection, criteria, false);
			ResultSet rs = stmt.executeQuery();

			List items = getReportItems(criteria, rs, transformer);

			dataItems.addAll(items);

			reportData.setDetails(dataItems);
			reportData.setTotalCount(totalCount);

		} catch (SQLException e) {
			handleSqlException(e, className,
					"getReportData",
					"Something went wrong while executing the statement - fromDate ["
							+ getFromDate(criteria) + "] toDate ["
							+ getToDate(criteria) + "] Contract Number ["
							+ getContractNumber(criteria) + "]"); 
		} finally {
			close(stmt, connection);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}
	}
	
	/**
	 * 1. Based on the countOnly indicator, creates the 
	 * 			a. SQL to get summary data for all the participants 
	 * 				in a contract to view statements
	 * 		OR  b. SQL to get the total number of participants 
	 * 				in a contract to view statements
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
		
		pair = getSqlPair(createPptStmtQuery(criteria));	
		
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
		query.append(" FOR FETCH only ");

		PreparedStatement stmt = connection.prepareStatement(query.toString());

		// set filter parameters into the WHERE clause.
		setParameters(stmt, criteria);

		return stmt;
	}

	/**
	 * Gets the total record count (number of Participants 
	 * in a contract who can acess Participant Statements) for the given report criteria.
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
	 * Method to pass the required input parameters to the SQL
	 * 
	 * @param stmt
	 * @param criteria
	 * @throws SQLException
	 * @throws SystemException 
	 */
	private static void setParameters(PreparedStatement stmt,
			ReportCriteria criteria) throws SQLException, SystemException {

		java.sql.Date fromDate = getFromDate(criteria);
		java.sql.Date toDate = getToDate(criteria);
		Integer contractNumber = getContractNumber(criteria);
		int parameterCount = 1;

		if (logger.isDebugEnabled()) {
			logger.debug(" Contract Number [" + contractNumber + "]");
			logger.debug(" Statement From Date [" + fromDate + "]");
			logger.debug(" Statement To Date [" + toDate + "]");
		}

		stmt.setDate(parameterCount++, fromDate); 
		stmt.setDate(parameterCount++, toDate);
		stmt.setInt(parameterCount++, contractNumber.intValue());
		stmt.setDate(parameterCount++, fromDate); 
		stmt.setDate(parameterCount++, toDate);
	}
	
	/**
	 * Method to get the Contract Number for the given Report Criteria. 
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The Proposal number.
	 * @throws SQLException 
	 * @throws SystemException 
	 */
	private static Integer getContractNumber(ReportCriteria criteria) throws SystemException {
		String proposalNumberString = (String) criteria.getFilterValue(
				ParticipantStatementsReportData.FILTER_CONTRACT_NUMBER);
		Integer proposalNumber = Integer.valueOf(0);
		if(proposalNumberString != null){
			proposalNumber = Integer.valueOf(proposalNumberString);
		}
		return proposalNumber;
		
	}

	/**
	 * Gets the Statement Start date filter as SQL Date from the report criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The FROM date.
	 */
	private static java.sql.Date getFromDate(ReportCriteria criteria) {
		java.util.Date fromDate = (java.util.Date) criteria.getFilterValue(
				ParticipantStatementsReportData.FILTER_STMT_START_DATE);
		java.sql.Date returnFromDate = null;
		if ( fromDate != null ) {
			returnFromDate = new java.sql.Date(fromDate.getTime());
		} 
		return returnFromDate;
	}

	/**
	 * Gets the Statement End Date filter as SQL Date from the report criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The TO date.
	 */
	private static java.sql.Date getToDate(ReportCriteria criteria) {
		java.util.Date toDate = (java.util.Date) criteria.getFilterValue(
				ParticipantStatementsReportData.FILTER_STMT_END_DATE);
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
	private static SqlPair getSqlPair(String query) {


		String countQuery = new SqlStringBuffer(" SELECT COUNT(*) FROM (")
				.append(query).append(") AS II_TEMP").toString();

		return new SqlPair(countQuery, query);
	}
	
	/**
	 * Inner class to transform the Result Set to ParticipantStatementItem Objects.
	 * @author Puttaiah Arugunta
	 *
	 */
	private static class ParticipantStatementItemTransformer extends
									ReportItemTransformer {

		public Object transform(ResultSet rs) throws SQLException {
			
			ParticipantStatementItem item = new ParticipantStatementItem();
			item.setSsn(getString(rs, "SOCIAL_SECURITY_NO"));
			item.setLastName(getString(rs, "PARTICIPANT_LAST_NAME"));
			item.setFirstName(getString(rs, "PARTICIPANT_FIRST_NAME"));
			item.setProfileId(getString(rs, "PROFILE_ID"));

			return item;
		}
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
	public static List<ParticipantStatementItem> getReportItems(ReportCriteria criteria, ResultSet rs,
			ReportItemTransformer transformer) throws SystemException {

		List<ParticipantStatementItem> dataItems = new ArrayList<ParticipantStatementItem>();
		List<ParticipantStatementItem> dataItemsInitial = new ArrayList<ParticipantStatementItem>();

		try {
			int startIndex = criteria.getStartIndex();
			
			List<ParticipantStatementItem> defaultDateList = new ArrayList<ParticipantStatementItem>();
			
			while (rs.next()) {

				Object item = transformer.transform(rs);

				if (item != null) {
					// Store the data item into the given list.
					dataItemsInitial.add((ParticipantStatementItem) item);
				}
			}
			
			defaultDateList.addAll(dataItemsInitial);
			
			/*
			 * Move result set to the proper location. Start index begins at 1.
			 */
			int transactionCount = 1;
			for (; transactionCount < startIndex  &&  transactionCount < defaultDateList.size(); transactionCount++)
				;

			/*
			 * ResultSet ends before start index...
			 */
			/*if (transactionCount < startIndex) {
				String errorString = "Invalid report criteria start index ["
						+ startIndex + "] result set size [" + (transactionCount - 1) + "]";
				throw new SystemException(new IllegalArgumentException(
						errorString), errorString);
			}*/

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
				ParticipantStatementItem ParticipantStatementItem=defaultDateList.get(transactionCount-1);
				dataItems.add(ParticipantStatementItem);
				recordCount++;
				transactionCount++;
			}
		} catch (SQLException e) {
			throw new SystemException(e, e
					.getMessage());
		}
		return dataItems;
	}
	
	/**
	 * Generates the Query String for Participant Statements
	 * @param criteria
	 * @return
	 */
	private static String createPptStmtQuery(ReportCriteria criteria) {

		StringBuffer result = new StringBuffer(GET_PARTICIPANT_STATEMENTS);
		
		String lastNameValue = (String)criteria.getFilterValue(ParticipantStatementsReportData.FILTER_LAST_NAME);
		String firstNameValue = (String)criteria.getFilterValue(ParticipantStatementsReportData.FILTER_FIRST_NAME);
		String ssnValue = (String)criteria.getFilterValue(ParticipantStatementsReportData.FILTER_SSN);

		if (lastNameValue != null && lastNameValue.trim().length() > 0 ) {
			result.append("PARTICIPANT_LAST_NAME like ").append(wrapInSingleQuotes(lastNameValue + "%").toUpperCase() ).append(AND);
		}

		if (firstNameValue != null && firstNameValue.trim().length() > 0 ) {
			result.append("PARTICIPANT_FIRST_NAME like ").append(wrapInSingleQuotes(firstNameValue + "%").toUpperCase() ).append(AND);
		}

		if (ssnValue != null && ssnValue.trim().length() > 0 ) {
			result.append("SOCIAL_SECURITY_NO = ").append(wrapInSingleQuotes(ssnValue)).append(AND);
		}
		
		if (result.length() > 0 && AND.equals(result.substring(result.length() - AND.length()) ) ) {
			result.delete(result.length() - AND.length(), result.length());
		}
		
		result.append(" GROUP BY PC.PROFILE_ID,P.PARTICIPANT_LAST_NAME,P.PARTICIPANT_FIRST_NAME,P.SOCIAL_SECURITY_NO ");
				
		return (result.toString().trim().length() > 0 ? result.toString() : null);
	}
	
	private static final String GET_PARTICIPANT_STATEMENTS =
			"SELECT P.participant_last_name, "
			+ "       P.participant_first_name, "
			+ "       P.social_security_no, "
			+ "       PC.profile_id "
			+ "FROM   psw100.participant_contract PC "
			+ "       INNER JOIN psw100.participant P "
			+ "               ON PC.participant_id = P.participant_id "
			+ "       LEFT OUTER JOIN psw100.participant_statement_history PSH "
			+ "                    ON P.participant_id = PSH.participant_id "
			+ "                       AND PC.contract_id = PSH.contract_id "
			+ "                       AND PSH.statement_period_end_date BETWEEN ? AND ? "
			+ "WHERE  PC.contract_id = ? "
			+ "       AND ( PC.participant_status_code IN ( 'AC' ) "
			+ "              OR ( PC.participant_status_code NOT IN ( 'CN', 'AC' ) "
			+ "                   AND PC.contract_id = PSH.contract_id "
			+ "                   AND PSH.statement_period_end_date BETWEEN ? AND ? ) ) " +
			AND;
			
}
