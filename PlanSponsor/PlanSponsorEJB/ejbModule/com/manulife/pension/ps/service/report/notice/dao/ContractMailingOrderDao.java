/**
 * 
 */
package com.manulife.pension.ps.service.report.notice.dao;

import java.math.BigDecimal;
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
import com.manulife.pension.ps.service.report.notice.valueobject.ContractMailingOrderReportData;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentHistoryReportData;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeMailingOrderVO;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.dao.SqlPair;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.util.tools.MessageFormatHelper;

/**
 * 
 * Dao to manage the data displayed in Order status page
 * @author krishta
 *
 */
public class ContractMailingOrderDao extends ReportServiceBaseDAO {

	private static final String className = ContractMailingOrderDao.class.getName();
	private static final Logger logger = Logger.getLogger(ContractMailingOrderDao.class);
	
	private static final String MAILING_NAME_COLUMN = "ORDER_NAME";
	private static final String ORDER_STATUS_CODE_COLUMN = "ORDER_STATUS_CODE";
	private static final String ORDER_NUMBER_COLUMN = "MERRILL_ORDER_NO";
	private static final String ORDER_STATUS_DATE_COLUMN = "ORDER_STATUS_DATE";
	private static final String ORDER_STATUS_INITIATED = "IN";
	private static final String ORDER_STATUS_NOT_COMPLETED = "IC";
	private static final String ORDER_STATUS_NOT_COMPLETED_DESCRIPTION = "Not Completed";
	private static final long ORDER_STATUS_INITIATED_TIMEOUT = 60 * 60 * 1000;  // 60 minutes in milliseconds
	
	/*
	* A map of database column keyed by sort field.
	 */
	private static final Map<String, String> fieldToColumnOrderMap = new HashMap<String, String>();
	
	private static final String DEFAULT_SORT_ORDER_ASC = " ORDER_STATUS_DATE DESC, MERRILL_ORDER_NO DESC ";
	
	private static String CONTRACT_NOTICE_ORDER_STATUS_CODES_SQL = 
			   "SELECT LOOKUP_CODE,LOOKUP_DESC FROM EZK100C.LOOKUP CTYPE WHERE CTYPE.LOOKUP_TYPE_NAME = 'CONTRACT_NOTICE_ORDER_STATUS_CODE'  ";
	
	
	private static String CONTRACT_NOTICE_MAILING_ORDER_SQL = 
			   "SELECT rownumber() over (order by {0}) as ROW_NUM , MERRILL_ORDER_NO,CONTRACT_ID,USER_PROFILE_ID,CTYPE.LOOKUP_CODE as ORDER_STATUS_CODE,CTYPE.LOOKUP_DESC as ORDER_STATUS_DESCRIPTION, ORDER_STATUS_DATE,COLOR_PRINT_IND,COLOR_PRINT_IND," +
			   "PARTICIPANT_COUNT,TOTAL_MAILING_COST_AMOUNT, ORDER_NAME FROM EZK100C.CONTRACT_NOTICE_MAILING_ORDER CNMO ,EZK100C.LOOKUP CTYPE " +
			   "WHERE CTYPE.LOOKUP_TYPE_NAME = 'CONTRACT_NOTICE_ORDER_STATUS_CODE' AND CNMO.ORDER_STATUS_CODE = CTYPE.LOOKUP_CODE AND CONTRACT_ID = ?";
	
	private static final String  SQL_SELECT_TERMS_ACCEPTANCE_CODE = "SELECT TERMS_ACCEPTANCE_CODE,USER_PROFILE_ID FROM " +
					PLAN_SPONSOR_SCHEMA_NAME + "USER_NOTICE_MANAGER_TERMS_ACCEPTANCE_LOG WHERE USER_PROFILE_ID = ? ORDER BY CREATED_TS DESC" ;
	
	private static String CONTRACT_NOTICE_COUNTED_RECORDS_SQL = "SELECT * FROM (";
	/**
	 * The substitutions used in all query strings.
	 */
	private static final Object[] QUERY_SUBSTITUTIONS = new Object[] {CUSTOMER_SCHEMA_NAME};
	
	static {
		fieldToColumnOrderMap.put(ContractMailingOrderReportData.MAILING_NAME, MAILING_NAME_COLUMN);
		fieldToColumnOrderMap.put(ContractMailingOrderReportData.ORDER_NUMBER, ORDER_NUMBER_COLUMN);
		fieldToColumnOrderMap.put(ContractMailingOrderReportData.ORDER_STATUS, ORDER_STATUS_CODE_COLUMN);
		fieldToColumnOrderMap.put(ContractMailingOrderReportData.ORDER_STATUS_DATE, ORDER_STATUS_DATE_COLUMN);
	}
	
	public static void getReportData(ReportCriteria criteria,ContractMailingOrderReportData contractMailingOrderReportData
			) throws SystemException, ReportServiceException {
		
		/**
		 * Get the Contract Notice Order Status Notice for the contract number
		 */
		getContractNoticeOrderStatusNotice(criteria, contractMailingOrderReportData);
		/**
		 * Get the Contract Notice Mailing Order for the contract number
		 */
		getContractNoticeMailingOrder(criteria, contractMailingOrderReportData);
		
		/**
		 * Set the Term of use acceptance
		 * 
		 */
		getTermsAndAcceptanceInd(criteria, contractMailingOrderReportData);
	}

	/**
	 * @param criteria
	 * @param contractMailingOrderReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	private static void getContractNoticeOrderStatusNotice(ReportCriteria criteria,
			ContractMailingOrderReportData contractMailingOrderReportData)
			throws SystemException, ReportServiceException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		LookupDescription contractNoticeOrderStatusCode = null;
		List<LookupDescription> contractNoticeOrderStatusCodes = null;
		
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);

						
						
			stmt = connection.prepareStatement(CONTRACT_NOTICE_ORDER_STATUS_CODES_SQL);
			 rs = stmt.executeQuery();
			 if (rs != null ) {
				 contractNoticeOrderStatusCodes = new ArrayList<LookupDescription>();
	            	while (rs.next())
	            	{
	            		contractNoticeOrderStatusCode   = new LookupDescription();
	            		contractNoticeOrderStatusCode.setLookupCode(rs.getString("LOOKUP_CODE"));
	            		contractNoticeOrderStatusCode.setLookupDesc(rs.getString("LOOKUP_DESC"));
	            		contractNoticeOrderStatusCodes.add(contractNoticeOrderStatusCode);
	            	}
	            	contractMailingOrderReportData.setContractNoticeOrderStatusCodes(contractNoticeOrderStatusCodes);
			 }


		} catch (SQLException e) {
			handleSqlException(e, className,
					"getReportData",
					"Something went wrong while executing the statement -  Contract Number ["
							+ getContractNumber(criteria) + "]"); 
		} finally {
			close(stmt, connection);
		}
	}
	
	/**
	 * @param criteria
	 * @param contractMailingOrderReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	private static void getContractNoticeMailingOrder(ReportCriteria criteria,
			ContractMailingOrderReportData contractMailingOrderReportData)
			throws SystemException, ReportServiceException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PlanNoticeMailingOrderVO contractNoticeMailingOrder = null;
		List<PlanNoticeMailingOrderVO> contractNoticeMailingOrders = null;
		
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);
			// get the total record count, used for paging
			int totalCount = getTotalChangesMailingOrderRecordCount(connection, criteria);
			if (logger.isDebugEnabled()) {
				logger.debug("Total Count [" + totalCount + "]");
			}
			contractMailingOrderReportData.setTotalCount(totalCount);
			stmt = getOrderPreparedStatement(connection, criteria, false,CONTRACT_NOTICE_MAILING_ORDER_SQL);
			rs = stmt.executeQuery();
			if (rs != null ) {
				contractNoticeMailingOrders = new ArrayList<PlanNoticeMailingOrderVO>();
            	while (rs.next()) {
	            		contractNoticeMailingOrder   = new PlanNoticeMailingOrderVO();
	            		contractNoticeMailingOrder.setColorPrintInd(rs.getString("COLOR_PRINT_IND"));
	            		contractNoticeMailingOrder.setContractId(rs.getInt("CONTRACT_ID"));
	            		contractNoticeMailingOrder.setNoOfParticipant(rs.getInt("PARTICIPANT_COUNT"));
	            		contractNoticeMailingOrder.setOrderNumber(rs.getInt("MERRILL_ORDER_NO"));
	            		contractNoticeMailingOrder.setOrderStatusDate(rs.getDate("ORDER_STATUS_DATE"));
	            		contractNoticeMailingOrder.setProfileId(rs.getBigDecimal("USER_PROFILE_ID"));
	            		contractNoticeMailingOrder.setTotalMailingCost(rs.getBigDecimal("TOTAL_MAILING_COST_AMOUNT"));
	            		contractNoticeMailingOrder.setTotalPageCount(rs.getInt("PARTICIPANT_COUNT"));
	            		// We have a special display rule that says if an order is in "Initiated" status, but it's been more than 60 minutes, then we assume that the order was never completed
	            		// We want to keep the actual status on the database, but we adjust how we display it on the order status page so it makes more sense to the users
	            		String status = rs.getString("ORDER_STATUS_CODE");
	            		String description;
	            		if (status.equals(ORDER_STATUS_INITIATED) && (System.currentTimeMillis() - contractNoticeMailingOrder.getOrderStatusDate().getTime() > ORDER_STATUS_INITIATED_TIMEOUT)) {
            				status = ORDER_STATUS_NOT_COMPLETED;
            				description = ORDER_STATUS_NOT_COMPLETED_DESCRIPTION;
           				} else {
           					description = rs.getString("ORDER_STATUS_DESCRIPTION");
           				}
	            		contractNoticeMailingOrder.setPlanNoticeMailingOrderStatus(new LookupDescription(status, description));
	            		contractNoticeMailingOrder.setMailingName(rs.getString("ORDER_NAME"));
	            		contractNoticeMailingOrders.add(contractNoticeMailingOrder);
	            	}
	            	contractMailingOrderReportData.setPlanNoticeMailingOrders(contractNoticeMailingOrders);
			 }
		} catch (SQLException e) {
			handleSqlException(e, className,
					"getReportData",
					"Something went wrong while executing the statement -  Contract Number ["
							+ getContractNumber(criteria) + "]"); 
		} finally {
			close(stmt, connection);
		}
	}
	
	
	private static PreparedStatement getOrderPreparedStatement(
			Connection connection, ReportCriteria criteria, boolean countOnly,String sql)
	throws SQLException, SystemException {
		StringBuffer query =   new StringBuffer();
		if(!countOnly ){
			query.append(CONTRACT_NOTICE_COUNTED_RECORDS_SQL);
		}
		// append the ORDER BY clause when there is any sorting requirement.
		StringBuffer orderByQuery = new StringBuffer();
		//orderByQuery.append(" ORDER BY ");
		if ( criteria.getSorts().size() > 0) {
			orderByQuery.append(criteria.getSortPhrase(fieldToColumnOrderMap));
		}else {
			orderByQuery.append(DEFAULT_SORT_ORDER_ASC);
		}
		query.append(MessageFormatHelper.format(sql, new Object[]{orderByQuery.toString()}));
		StringBuffer updatedQuery = new StringBuffer();
		Integer startIndex = criteria.getStartIndex();
		Integer pageSize = criteria.getPageSize();
		if(!countOnly){
			if (startIndex > 0) {
				query.append(") WHERE ROW_NUM >= ? ");
			} 
		}

		if(!countOnly ){			
			if ( pageSize > 0) {
				query.append(" FETCH FIRST " + pageSize + " ROWS ONLY ");
			}
		}

		SqlPair pair = null;
		pair = getSqlPair(query.toString());

		if (countOnly) {
			updatedQuery.append(pair.getCountQuery());
		} else {
			updatedQuery.append(pair.getQuery());
		}
		PreparedStatement stmt = connection.prepareStatement(updatedQuery.toString());
		stmt.setInt(1, getContractNumber(criteria));
		if(!countOnly ){
			//  start index
			if (  startIndex>0 ){
				stmt.setInt(2, startIndex);
			}
		}
		return stmt;
	}
	
	/**
	 * Gets the total record count (number of changes history record 
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
	private static int getTotalChangesMailingOrderRecordCount(Connection connection,
			ReportCriteria criteria) throws SQLException, SystemException {

		PreparedStatement stmt = null;
		int totalCount = 0;
		try { 
			
			// Prepare a statement for counting
			stmt = getOrderPreparedStatement(connection, criteria, true,CONTRACT_NOTICE_MAILING_ORDER_SQL);
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
	 * @param count
	 * @throws SQLException
	 * @throws SystemException 
	 */
	private static void setParameters(PreparedStatement stmt,
			ReportCriteria criteria, int count) 
	throws SQLException, SystemException {

		Integer contractId = getContractNumber(criteria);
		int parameterCount = 1;

		if (logger.isDebugEnabled()) {
			logger.debug(" Contract Number [" + contractId + "]");
		}

		for (int i = 0; i < count; i++) {
			stmt.setInt(parameterCount++, contractId.intValue());
			
			
		}
	}
	
	/**
	 * Method to get the Contract Number for the given Report Criteria. 
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The contract number.
	 * @throws SQLException 
	 * @throws SystemException 
	 */
	public static Integer getContractNumber(ReportCriteria criteria) throws SystemException {
		Integer contractNumberString =  (Integer)criteria
				.getFilterValue(ContractMailingOrderReportData.FILTER_CONTRACT_NUMBER);
		Integer contractNumber = Integer.valueOf(0);
		if(contractNumberString != null){
			contractNumber = Integer.valueOf(contractNumberString);
		}
		return contractNumber;
		
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
	 * 
	 * @param criteria
	 * @param planDocumentReportData
	 * @return
	 * @throws SystemException
	 */
	public static boolean getTermsAndAcceptanceInd(ReportCriteria criteria,ContractMailingOrderReportData contractMailingOrderReportData) throws SystemException{
		

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean termOfUse = false; 
		String termsOfUseInd = "";
		try {
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(SQL_SELECT_TERMS_ACCEPTANCE_CODE);
			stmt.setBigDecimal(1, getUserProfileId(criteria));
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
					 termOfUse = true;
					 termsOfUseInd = rs.getString("TERMS_ACCEPTANCE_CODE").trim();
			}
			contractMailingOrderReportData.setUserAccessPermission(termOfUse);
			contractMailingOrderReportData.setTermsOfUseInd(termsOfUseInd);
			}catch (SQLException e) {
				logger.error("Retrieve the terms acceptance code based  on the profile id", e);
				throw new SystemException(e, 
						"Problem occurred during getTermsAndAcceptanceInd sql call. ");
			} finally {
				close(stmt, conn);
			}
			return termOfUse;
	}
	
	/**
	 * Method to get the Profile Id for the given Report Criteria. 
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The contract number.
	 * @throws SQLException 
	 * @throws SystemException 
	 */
	public static BigDecimal getUserProfileId(ReportCriteria criteria) throws SystemException {
		String profileIdString = (String) criteria
				.getFilterValue(PlanDocumentHistoryReportData.FILTER_USER_PROFILE_ID);
		BigDecimal profileId = BigDecimal.ZERO;
		if(profileIdString != null){
			profileId = new BigDecimal(profileIdString);
		}
		return profileId;
		
	}
	
}
