package com.manulife.pension.ps.service.report.notice.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentHistoryReportData;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentReportData;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentChangeHistoryVO;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.dao.SqlPair;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.util.tools.MessageFormatHelper;

/**
 * Dao for Contract changes history record retrieval applied with pagination and sorting
 * @author krishta
 *
 */
public class PlanNoticeDocumentHistoryDAO extends ReportServiceBaseDAO {

	/**
	 * Class name
	 */
	private static final String className = 
		PlanNoticeDocumentHistoryDAO.class.getName();

	/**
	 * Logger object
	 */
	private static final Logger logger = 
		Logger.getLogger(PlanNoticeDocumentHistoryDAO.class);

	/**
	 * A map of database column keyed by sort field.
	 */
	private static final Map<String, String> fieldToColumnMap = 
		new HashMap<String, String>();
	
	private static final String CONTRACT_NOTICE_CHANGES_TIMESTAMP = "CLOG.CREATED_TS ";
	private static final String CONTRACT_CHANGE_TYPE_CODE = "CTYPE.LOOKUP_CODE ";
	private static final String CONTRACT_DOCUMENT_USER_PROFILE_FIRST_NAME = "UP.FIRST_NAME";
	private static final String CONTRACT_DOCUMENT_USER_PROFILE_LAST_NAME = "UP.LAST_NAME";
	private static final String CONTRACT_DOCUMENT_NAME_RECORDED = "DOCUMENT_NAME";
	private static final String CONTRACT_REVISED_DOCUMENT_NAME = "PREV_DOCUMENT_NAME";
	private static final String CONTRACT_DOCUMENT_POST_TO_PPT = "CHANGED_POST_TO_PPT_IND";

	private static final String DEFAULT_SORT_ORDER_ASC = " CLOG.CREATED_TS DESC" +
			"							, CTYPE.LOOKUP_DESC ASC, UP.FIRST_NAME ASC,UP.LAST_NAME ASC, DOCUMENT_NAME ASC ,PREV_DOCUMENT_NAME ASC,CONTRACT_DOCUMENT_POST_TO_PPT ASC ";
	private static String CONTRACT_NOTICE_DOCUMENT_HISTORY_LIST_SQL = 
		  "SELECT rownumber() over (order by {0}) as ROW_NUM,"
		+ "CLOG.DOCUMENT_ID,CLOG.VERSION_NO, CLOG.CONTRACT_ID,CLOG.CREATED_TS,"
		+ "CLOG.USER_PROFILE_ID,UP.FIRST_NAME, UP.LAST_NAME, CTYPE.LOOKUP_CODE "
		+ "as CHANGE_TYPE_CODE,CTYPE.LOOKUP_DESC as DOCUMENT_CHANGE_TYPE_DESCRIPTION,"
		+ "CND.DOCUMENT_NAME AS DOCUMENT_NAME,CLOG.PREV_DOCUMENT_NAME as "
		+ "PREV_DOCUMENT_NAME,CND.POST_TO_PPT_IND as POST_TO_PPT_IND, CLOG.CHANGED_DOCUMENT_NAME,CLOG.CHANGED_POST_TO_PPT_IND "
		+ "FROM "
		+ PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CLOG,"
		+ PLAN_SPONSOR_SCHEMA_NAME + "LOOKUP CTYPE,"
		+ PLAN_SPONSOR_SCHEMA_NAME + "USER_PROFILE UP,"
		+ PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT CND "
		+ "WHERE CLOG.CONTRACT_ID = ? AND CLOG.CONTRACT_ID = CND.CONTRACT_ID AND CTYPE.LOOKUP_TYPE_NAME = 'DOCUMENT_CHANGE_TYPE_CODE' "
		+ "AND CLOG.CHANGE_TYPE_CODE = CTYPE.LOOKUP_CODE AND CLOG.USER_PROFILE_ID = UP.USER_PROFILE_ID "
		+ "AND CND.VERSION_NO = CLOG.VERSION_NO AND CLOG.DOCUMENT_ID = CND.DOCUMENT_ID "
		+ "AND CLOG.CREATED_TS <= TIMESTAMP(DATE(CAST(? AS TIMESTAMP)),'23:59:59') AND CLOG.CREATED_TS >=? ";

	private static String CONTRACT_NOTICE_DOCUMENT_USER_DETAIL_HISTORY_SQL = 
		" SELECT CLOG.USER_PROFILE_ID," +
		" UP.FIRST_NAME, UP.LAST_NAME FROM "
		+ PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CLOG," +
		PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT CND, "+
		PLAN_SPONSOR_SCHEMA_NAME + "USER_PROFILE UP " +
		" WHERE CLOG.CONTRACT_ID = ? AND CLOG.CONTRACT_ID = CND.CONTRACT_ID AND CLOG.CREATED_TS <= TIMESTAMP(DATE(CAST(? AS TIMESTAMP)),'23:59:59') AND CLOG.CREATED_TS >= ? " +
		" AND CLOG.USER_PROFILE_ID = UP.USER_PROFILE_ID order by UP.FIRST_NAME ASC,UP.LAST_NAME ASC";

	private static String CONTRACT_NOTICE_CHANGE_TYPE_CODES_SQL = 
		"SELECT LOOKUP_CODE,LOOKUP_DESC FROM "
		+ PLAN_SPONSOR_SCHEMA_NAME + "LOOKUP CTYPE WHERE CTYPE.LOOKUP_TYPE_NAME = 'DOCUMENT_CHANGE_TYPE_CODE'  ";



	private static String CONTRACT_NOTICE_COUNTED_RECORDS_SQL = "SELECT * FROM (";

	/**
	 * The substitutions used in all query strings.
	 */
	private static final Object[] QUERY_SUBSTITUTIONS = new Object[] {
		CUSTOMER_SCHEMA_NAME};

	static {
		/*
		 * Set up the field to column map.
		 */

		fieldToColumnMap.put(PlanDocumentHistoryReportData.ACTION_DATE_FIELD,
				CONTRACT_NOTICE_CHANGES_TIMESTAMP);
		fieldToColumnMap.put(PlanDocumentHistoryReportData.ACTION_TAKEN_FIELD,
				CONTRACT_CHANGE_TYPE_CODE);
		fieldToColumnMap.put(PlanDocumentHistoryReportData.USER_FIRST_NAME_FIELD,
				CONTRACT_DOCUMENT_USER_PROFILE_FIRST_NAME);
		fieldToColumnMap.put(PlanDocumentHistoryReportData.USER_LAST_NAME_FIELD,
				CONTRACT_DOCUMENT_USER_PROFILE_LAST_NAME);
		fieldToColumnMap.put(PlanDocumentHistoryReportData.DOCUMENT_NAME,
				CONTRACT_DOCUMENT_NAME_RECORDED);
		fieldToColumnMap.put(PlanDocumentHistoryReportData.REVISED_DOCUMENT_FIELD,
				CONTRACT_REVISED_DOCUMENT_NAME);
		fieldToColumnMap.put(PlanDocumentHistoryReportData.POST_TO_PPT_FIELD,
				CONTRACT_DOCUMENT_POST_TO_PPT);
	}
	
	private static String CONTRACT_NOTICE_DOCUMENT_UPLOADED_DOCNAME_SQL = "SELECT PREV_DOCUMENT_NAME FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG WHERE "
			+ "DOCUMENT_ID = ? AND VERSION_NO = ? AND CONTRACT_ID = ? AND CHANGE_TYPE_CODE IN('CHNGB','CHNGN') AND CREATED_TS > ? "
			+ "ORDER BY CREATED_TS ASC";
	public static void getReportData(ReportCriteria criteria,PlanDocumentHistoryReportData planDocumentHistoryReportData
			) throws SystemException, ReportServiceException {

		/**
		 * Get the Custom Contract Notice Documents Logs for the contract number
		 */
		getCustomContractNoticeDocumentsLogs(criteria, planDocumentHistoryReportData);

		/**
		 * Get the Contract Notice DocumentType Codes for the contract number
		 */
		getContractNoticeDocumentTypeCodes(criteria, planDocumentHistoryReportData);

		/**
		 * getContractNoticeUpdatedUserDetails
		 */
		getContractNoticeUpdatedUserDetails(criteria, planDocumentHistoryReportData);
	}


	/**
	 * Get the contract change history record based on the filter condition
	 * @param criteria
	 * @param planDocumentReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	private static void getCustomContractNoticeDocumentsLogs(ReportCriteria criteria,
			PlanDocumentHistoryReportData planDocumentReportData)
					throws SystemException, ReportServiceException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String contractNoticeUploadedDocName = StringUtils.EMPTY;
		LookupDescription contractNoticeDocumentTypeCode = null;
		PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangelog = null;
		List<PlanNoticeDocumentChangeHistoryVO> planNoticeDocumentChangeHistorys = null;

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);
			Integer contractId = getContractNumber(criteria);
			// get the total record count, used for paging
			int totalCount = getTotalChangesHistoryRecordCount(connection, criteria);

			if (logger.isDebugEnabled()) {
				logger.debug("Total Count [" + totalCount + "]");
			}
			planDocumentReportData.setTotalCount(totalCount);

			stmt = getHistoryPreparedStatement(connection, criteria, false,CONTRACT_NOTICE_DOCUMENT_HISTORY_LIST_SQL);
			rs = stmt.executeQuery();

			if (rs != null ) {
				planNoticeDocumentChangeHistorys = new ArrayList<PlanNoticeDocumentChangeHistoryVO>();
				while (rs.next()) {

					planNoticeDocumentChangelog = new PlanNoticeDocumentChangeHistoryVO();
					planNoticeDocumentChangelog.setContractId(contractId);
					planNoticeDocumentChangelog.setChangedProfileId(rs.getBigDecimal("USER_PROFILE_ID"));
					planNoticeDocumentChangelog.setChangedDate(rs.getTimestamp("CREATED_TS"));
					planNoticeDocumentChangelog.setChangedUserName(rs.getString("FIRST_NAME") +" "+rs.getString("LAST_NAME"));
					planNoticeDocumentChangelog.setDocumentId(rs.getInt("DOCUMENT_ID"));
					planNoticeDocumentChangelog.setDocumentName(rs.getString("CHANGED_DOCUMENT_NAME"));
					/*if("UPLD".equals(rs.getString("CHANGE_TYPE_CODE").trim()) || "CHNGP".equals(rs.getString("CHANGE_TYPE_CODE").trim())){
						contractNoticeUploadedDocName = getContractNoticeUploadedDocName(rs.getInt("DOCUMENT_ID"),rs.getInt("VERSION_NO"),rs.getInt("CONTRACT_ID"),rs.getTimestamp("CREATED_TS"));
						if(contractNoticeUploadedDocName.isEmpty()){
						planNoticeDocumentChangelog.setDocumentName(rs.getString("DOCUMENT_NAME"));
						}else{
						planNoticeDocumentChangelog.setDocumentName(contractNoticeUploadedDocName);
						}
					}*/
					if(rs.getString("PREV_DOCUMENT_NAME")!=null){
						planNoticeDocumentChangelog.setReplacedfileName(rs.getString("PREV_DOCUMENT_NAME"));
					} else {
						planNoticeDocumentChangelog.setReplacedfileName(StringUtils.EMPTY);
					}

					if("Y".equalsIgnoreCase(rs.getString("CHANGED_POST_TO_PPT_IND"))){
						planNoticeDocumentChangelog.setChangedPPT("Yes");
					} else if("N".equalsIgnoreCase(rs.getString("CHANGED_POST_TO_PPT_IND"))){
						planNoticeDocumentChangelog.setChangedPPT("No");
					} else {
						planNoticeDocumentChangelog.setChangedPPT(StringUtils.EMPTY);
					} 
					contractNoticeDocumentTypeCode   = new LookupDescription();
			        String lookupCode = rs.getString("CHANGE_TYPE_CODE").trim();
					if("CHNGN".equals(lookupCode) || "CHNGP".equals(lookupCode) || "CHNGB".equals(lookupCode)){
						contractNoticeDocumentTypeCode.setLookupDesc("Changed");
						contractNoticeDocumentTypeCode.setLookupCode(lookupCode);
					}else if("CHRPN".equals(lookupCode) || "CHRPP".equals(lookupCode) || "CHRPB".equals(lookupCode)){
						contractNoticeDocumentTypeCode.setLookupDesc("Changed & Replaced");
						contractNoticeDocumentTypeCode.setLookupCode(lookupCode);
					}else if("REPL".equals(lookupCode)){
						contractNoticeDocumentTypeCode.setLookupDesc("Replaced");
						contractNoticeDocumentTypeCode.setLookupCode(lookupCode);
					}else if("UPLD".equals(lookupCode)){
						contractNoticeDocumentTypeCode.setLookupDesc("Uploaded");
						contractNoticeDocumentTypeCode.setLookupCode(lookupCode);
					}else if("DEL".equals(lookupCode)){
						contractNoticeDocumentTypeCode.setLookupDesc("Deleted");
						contractNoticeDocumentTypeCode.setLookupCode(lookupCode);
					}
					planNoticeDocumentChangelog.setPlanNoticeDocumentChangeTypeDetail(contractNoticeDocumentTypeCode);
					planNoticeDocumentChangeHistorys.add(planNoticeDocumentChangelog);
				}
				planDocumentReportData.setPlanNoticeDocumentChangeHistorys(planNoticeDocumentChangeHistorys);
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
	 * Get the contract notice document changes type code
	 * @param criteria
	 * @param planDocumentReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	private static void getContractNoticeDocumentTypeCodes(ReportCriteria criteria,
			PlanDocumentHistoryReportData planDocumentReportData)
					throws SystemException, ReportServiceException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		LookupDescription contractNoticeDocumentTypeCode = null;
		List<LookupDescription> contractNoticeDocumentTypeCodes = null;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);

			stmt = connection.prepareStatement(CONTRACT_NOTICE_CHANGE_TYPE_CODES_SQL);
			rs = stmt.executeQuery();
			if (rs != null ) {
				contractNoticeDocumentTypeCodes = new ArrayList<LookupDescription>();
				while (rs.next()) {
					contractNoticeDocumentTypeCode   = new LookupDescription();
			        String lookupCode = rs.getString("LOOKUP_CODE").trim();
					if("CHNGN".equals(lookupCode) || "CHNGP".equals(lookupCode) || "CHNGB".equals(lookupCode)){
						contractNoticeDocumentTypeCode.setLookupDesc("Changed");
						contractNoticeDocumentTypeCode.setLookupCode(lookupCode);
					}else if("CHRPN".equals(lookupCode) || "CHRPP".equals(lookupCode) || "CHRPB".equals(lookupCode)){
						contractNoticeDocumentTypeCode.setLookupDesc("Changed & Replaced");
						contractNoticeDocumentTypeCode.setLookupCode(lookupCode);
					}else if("REPL".equals(lookupCode)){
						contractNoticeDocumentTypeCode.setLookupDesc("Replaced");
						contractNoticeDocumentTypeCode.setLookupCode(lookupCode);
					}else if("UPLD".equals(lookupCode)){
						contractNoticeDocumentTypeCode.setLookupDesc("Uploaded");
						contractNoticeDocumentTypeCode.setLookupCode(lookupCode);
					}else if("DEL".equals(lookupCode)){
						contractNoticeDocumentTypeCode.setLookupDesc("Deleted");
						contractNoticeDocumentTypeCode.setLookupCode(lookupCode);
					}
					contractNoticeDocumentTypeCodes.add(contractNoticeDocumentTypeCode);
				}
				planDocumentReportData.setPlanNoticeDocumentChangeTypes(contractNoticeDocumentTypeCodes);
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
	 * 1. Based on the countOnly indicator, creates the 
	 * 			a. SQL to get change history for all the contract history 
	 * 				in a contract 
	 * 		OR  b. SQL to get the total number of changes history record
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
	private static PreparedStatement getHistoryPreparedStatement(
			Connection connection, ReportCriteria criteria, boolean countOnly,String sql)
					throws SQLException, SystemException {
		StringBuffer query =   new StringBuffer();
		String task = (String)criteria.getFilterValue(
						PlanDocumentHistoryReportData.FILTER_TASK);
		if(!countOnly && !PlanDocumentHistoryReportData.TASK_DOWNLOAD.equals(task)){
			query.append(CONTRACT_NOTICE_COUNTED_RECORDS_SQL);
		}
		// append the ORDER BY clause when there is any sorting requirement.
		StringBuffer orderByQuery = new StringBuffer();
		//orderByQuery.append(" ORDER BY ");
		if ( criteria.getSorts().size() > 0) {
			orderByQuery.append(criteria.getSortPhrase(fieldToColumnMap));
		}else {
			orderByQuery.append(DEFAULT_SORT_ORDER_ASC);
		}
		query.append(MessageFormatHelper.format(sql, new Object[]{orderByQuery.toString()}));
		StringBuffer updatedQuery = new StringBuffer();

		int paramCount = 0;
		Integer startIndex = criteria.getStartIndex();
		Integer pageSize = criteria.getPageSize();

		BigDecimal profileId =  getUserProfileId(criteria);
		if(profileId.compareTo(BigDecimal.ZERO)>0){
			query.append(" AND CLOG.USER_PROFILE_ID = ? ");
		}

		String userId = (String)criteria.getFilterValue(PlanDocumentHistoryReportData.FILTER_USER_PROFILE_ID);
		if(!StringUtils.isBlank(userId)){
			query.append(" AND (UP.USER_PROFILE_ID = ? )");
		}

		boolean actionParam =  false;
		String actionChange = (String)criteria
				.getFilterValue(PlanDocumentHistoryReportData.FILTER_ACTION_CHANGE);	
		if(!StringUtils.isBlank(actionChange)){
			if(actionChange.trim().equals("CHNG")){
				query.append(" AND CHANGE_TYPE_CODE IN ('CHNGN','CHNGP','CHNGB')");
			} else if(actionChange.trim().equals("CHRP")){
				query.append(" AND CHANGE_TYPE_CODE IN ('CHRPN','CHRPP','CHRPB')");
			} else  {
				actionParam = true;
				query.append(" AND CHANGE_TYPE_CODE IN (?)");
			}
		}

		String documentName = (String)criteria.getFilterValue(PlanDocumentHistoryReportData.DOCUMENT_NAME);
		if(!StringUtils.isBlank(documentName)){
			query.append(" AND ( LOWER(CLOG.CHANGED_DOCUMENT_NAME) like LOWER(?) OR LOWER(CLOG.PREV_DOCUMENT_NAME) like LOWER(?) ) ");
		}

		if(!countOnly && !PlanDocumentHistoryReportData.TASK_DOWNLOAD.equals(task)){
			if (startIndex > 0) {
				query.append(") WHERE ROW_NUM >= ? ");
			} 
		}

		if(!countOnly && !PlanDocumentHistoryReportData.TASK_DOWNLOAD.equals(task)){			
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
		stmt.setInt(++paramCount, getContractNumber(criteria) );
		stmt.setTimestamp(++paramCount, getToDate(criteria));
		stmt.setTimestamp(++paramCount, getFromDate(criteria));

		if(!StringUtils.isBlank(userId) && profileId.compareTo(BigDecimal.ZERO)>0){
			stmt.setBigDecimal(++paramCount,profileId);
			stmt.setString(++paramCount, userId);
		}

		if(actionParam){
			stmt.setString(++paramCount, actionChange.trim());
		}
		if(!StringUtils.isBlank(documentName)){
			String documentnameappend="%"+documentName.trim()+"%";
			stmt.setString(++paramCount,documentnameappend );
			stmt.setString(++paramCount,documentnameappend );
		}

		if(!countOnly && !PlanDocumentHistoryReportData.TASK_DOWNLOAD.equals(task)){
			//  start index
			if (  startIndex>0 ){
				stmt.setInt(++paramCount, startIndex);
			}
		}
		return stmt;
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
		Integer contractNumberString =  (Integer) criteria
		.getFilterValue(PlanDocumentReportData.FILTER_CONTRACT_NUMBER);
		Integer contractNumber = Integer.valueOf(0);
		if(contractNumberString != null){
			contractNumber = contractNumberString;
		}
		return contractNumber;

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

	/**
	 * Method to get the From Date for the given Report Criteria. 
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The contract number.
	 * @throws SQLException 
	 * @throws SystemException 
	 */
	public static Timestamp getFromDate(ReportCriteria criteria) throws SystemException {
		Date fromDateSelected = (Date) criteria
				.getFilterValue(PlanDocumentHistoryReportData.FILTER_FROM_DATE);
		Calendar toCal = Calendar.getInstance();
		toCal.add(Calendar.YEAR, -2);
		Timestamp fromDate = new Timestamp(toCal.getTimeInMillis());
		if(fromDateSelected != null){
			fromDate = new Timestamp(fromDateSelected.getTime());
		}
		return fromDate;

	}
	/**
	 * Method to get the To Date for the given Report Criteria. 
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The contract number.
	 * @throws SQLException 
	 * @throws SystemException 
	 */
	public static Timestamp getToDate(ReportCriteria criteria) throws SystemException {
		Date toDateSelected = (Date) criteria
				.getFilterValue(PlanDocumentHistoryReportData.FILTER_TO_DATE);
		Calendar toCal = Calendar.getInstance();
		Timestamp toDate = new Timestamp(toCal.getTimeInMillis());
		if(toDateSelected != null){
			toDate = new Timestamp(toDateSelected.getTime());
		}
		return toDate;
	}
	
	/**
	 * This method is used to get the user details who have made updates to the contract notice.
	 *
	 * @param criteria
	 * @param planDocumentReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	private static void getContractNoticeUpdatedUserDetails(ReportCriteria criteria,
			PlanDocumentHistoryReportData planDocumentReportData)
					throws SystemException, ReportServiceException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		LookupDescription noticeChangedUserDetail = null;
		LinkedHashMap<BigDecimal,LookupDescription> noticeChangedUserDetails = null;
		int paramCount = 0;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);


			stmt = connection.prepareStatement(CONTRACT_NOTICE_DOCUMENT_USER_DETAIL_HISTORY_SQL);
			stmt.setInt(++paramCount, getContractNumber(criteria) );
			stmt.setTimestamp(++paramCount, getToDate(criteria));
			stmt.setTimestamp(++paramCount, getFromDate(criteria));
			rs = stmt.executeQuery();
			if (rs != null ) {
				noticeChangedUserDetails = new LinkedHashMap<BigDecimal,LookupDescription>();
				while (rs.next())
				{
					noticeChangedUserDetail   = new LookupDescription();
					noticeChangedUserDetail.setLookupCode(rs.getString("FIRST_NAME"));
					noticeChangedUserDetail.setLookupDesc(rs.getString("LAST_NAME"));
					noticeChangedUserDetails.put(rs.getBigDecimal("USER_PROFILE_ID"), noticeChangedUserDetail);
				}
				planDocumentReportData.setUserProfileDetails(noticeChangedUserDetails);
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
	private static int getTotalChangesHistoryRecordCount(Connection connection,
			ReportCriteria criteria) throws SQLException, SystemException {

		PreparedStatement stmt = null;
		int totalCount = 0;
		try { 

			// Prepare a statement for counting
			stmt = getHistoryPreparedStatement(connection, criteria, true,CONTRACT_NOTICE_DOCUMENT_HISTORY_LIST_SQL);
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
	 * This method is used to get the user details who have made updates to the contract notice.
	 *
	 * @param criteria
	 * @param planDocumentReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	private static String getContractNoticeUploadedDocName(Integer documentId,Integer versionId, Integer contractId, Timestamp createdTimeStamp)throws SystemException, ReportServiceException {
					
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String noticeUplodedDocName = StringUtils.EMPTY;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}
		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = connection.prepareStatement(CONTRACT_NOTICE_DOCUMENT_UPLOADED_DOCNAME_SQL);
			stmt.setInt(1,documentId );
			stmt.setInt(2,versionId );
			stmt.setInt(3,contractId );
			stmt.setTimestamp(4,createdTimeStamp );
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				noticeUplodedDocName	= rs.getString("PREV_DOCUMENT_NAME");
			}


		} catch (SQLException e) {
			handleSqlException(e, className,
					"getReportData",
					"Something went wrong while executing the statement -  Contract Number ["
							+ contractId + "]"); 
		} finally {
			close(stmt, connection);
		}
		return noticeUplodedDocName;
	}


}
