package com.manulife.pension.ps.service.report.notice.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentHistoryReportData;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentReportData;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentChangeHistoryVO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentVO;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.dao.SqlPair;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.util.tools.MessageFormatHelper;

/**
 *  To set the report data into report criteria 
 */
public class PlanNoticeDocumentDAO extends ReportServiceBaseDAO {

	/**
	 * Class name
	 */
	private static final String className = 
			PlanNoticeDocumentDAO.class.getName();
	
	/**
	 * Logger object
	 */
	private static final Logger logger = 
		Logger.getLogger(PlanNoticeDocumentDAO.class);
	
	/**
	 * Name of the document name column in the resultSet. It is
	 * used to construct the ORDER BY clause.
	 */
	private static final String CONTRACT_DISPLAY_ORDER = "DISPLAY_ORDER";

	/**
	 * A map of database column keyed by sort field.
	 */
	private static final Map<String, String> fieldToColumnMap = 
		new HashMap<String, String>();
	
	private static String CONTRACT_NOTICE_DOCUMENT_SQL = 
		"SELECT CND.DOCUMENT_ID,CND.CONTRACT_ID,CND.DOCUMENT_NAME , "+
		  "CND.DISPLAY_ORDER,CND.DOCUMENT_FILE_NAME,CND.POST_TO_PPT_IND,CND.SOFT_DELETE_IND, " +
		  "case when DATE(Current timestamp-5 YEAR)>CLOG.CREATED_TS then 'Y' else 'N'  end  as DOCUMENT_FIVE_MORE FROM   "+
		  PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT CND , " +
		  "PSW100.CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CLOG   WHERE CLOG.CONTRACT_ID = ? AND " +
		  "CND.DOCUMENT_ID = CLOG.DOCUMENT_ID AND CND.VERSION_NO = CLOG.VERSION_NO AND SOFT_DELETE_IND ='N' AND " +  
		  "CLOG.CHANGE_TYPE_CODE IN('UPLD', 'REPL','CHRPB', 'CHRPP','CHRPN')  ORDER BY DISPLAY_ORDER DESC";
	
	private static String CONTRACT_NOTICE_DOCUMENT_HISTORY_SQL = 
			  "	SELECT DOCUMENT_ID,CONTRACT_ID,CLOG.CREATED_TS,CLOG.USER_PROFILE_ID,UP.FIRST_NAME, UP.LAST_NAME, "+ 
			  "	CTYPE.LOOKUP_CODE as CHANGE_TYPE_CODE,CTYPE.LOOKUP_DESC as DOCUMENT_CHANGE_TYPE_DESCRIPTION  FROM  " + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CLOG, "+
			  PLAN_SPONSOR_SCHEMA_NAME + "LOOKUP CTYPE,  " + PLAN_SPONSOR_SCHEMA_NAME + "USER_PROFILE UP "+
			  " WHERE CONTRACT_ID = ? AND DOCUMENT_ID = ? AND CTYPE.LOOKUP_TYPE_NAME = 'DOCUMENT_CHANGE_TYPE_CODE'  AND CLOG.CHANGE_TYPE_CODE = CTYPE.LOOKUP_CODE AND CLOG.USER_PROFILE_ID = UP.USER_PROFILE_ID ORDER by CLOG.CREATED_TS DESC";
	
	private static String USER_NOTICE_MANAGER_ALERT_TIMINGS_SQL = 
			   "SELECT LOOKUP_CODE,LOOKUP_DESC FROM " + PLAN_SPONSOR_SCHEMA_NAME + "LOOKUP CTYPE WHERE CTYPE.LOOKUP_TYPE_NAME = 'ALERT_TIMING_CODE'  ";
	
	private static String USER_ALERT_FREQUENCY_CODES_SQL = 
			   "SELECT LOOKUP_CODE,LOOKUP_DESC FROM " + PLAN_SPONSOR_SCHEMA_NAME + "LOOKUP  CTYPE WHERE CTYPE.LOOKUP_TYPE_NAME = 'ALERT_FREQUENCY_CODE'  ";
	
	private static final String  SQL_SELECT_TERMS_ACCEPTANCE_CODE = 
			"SELECT TERMS_ACCEPTANCE_CODE,USER_PROFILE_ID FROM " +
					PLAN_SPONSOR_SCHEMA_NAME + "USER_NOTICE_MANAGER_TERMS_ACCEPTANCE_LOG WHERE USER_PROFILE_ID = ? ORDER BY CREATED_TS DESC" ;
	
	/*
	 * The substitutions used in all query strings.
	 */
	private static final Object[] QUERY_SUBSTITUTIONS = new Object[] {
			CUSTOMER_SCHEMA_NAME};
	
	static {
		/*
		 * Set up the field to column map.
		 */
		fieldToColumnMap.put(PlanDocumentReportData.SORT_FIELD_DISPLAY_SORT_NUMBER,
				CONTRACT_DISPLAY_ORDER);
		
	}
	
	/**
	 * Generates the report data for Upload and Build pages
	 * @param criteria
	 * @param planDocumentReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	public static void getReportData(ReportCriteria criteria,PlanDocumentReportData planDocumentReportData
			) throws SystemException, ReportServiceException {
		/**
		 * Get the custom contract Notice Document for the contract number
		 */
	    setCustomPlanNoticeDocuments(criteria, planDocumentReportData);
		
		/**
		 * Get the User Manager Alert Frequency Codes for the contract number
		 */
		getUserManagerAlertFrequencyCodes(criteria, planDocumentReportData);
		/**
		 * Get the User Manager Alert Timing Codes for the contract number
		 */
		getUserManagerAlertTimingCodes(criteria, planDocumentReportData);
		
		/**
		 * Set the Term of use acceptance
		 * 
		 */
		getTermsAndAcceptanceInd(criteria, planDocumentReportData);
	}

	/**
	 * returns change history records which will suit the criteria passed
	 * @param criteria
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	private static PlanNoticeDocumentChangeHistoryVO setCustomPlanNoticeDocumentHistory(
			ReportCriteria criteria, Integer documentId) throws SystemException,
			ReportServiceException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangelog = null;
		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);
			Integer contractId = getContractNumber(criteria);

			stmt =  connection.prepareStatement(CONTRACT_NOTICE_DOCUMENT_HISTORY_SQL);
			stmt.setInt(1, contractId);
			stmt.setInt(2, documentId);
			rs = stmt.executeQuery();

			if (rs != null && rs.next()) {
        	    
            		planNoticeDocumentChangelog = new PlanNoticeDocumentChangeHistoryVO();
            		planNoticeDocumentChangelog.setContractId(contractId);
            		planNoticeDocumentChangelog.setChangedProfileId(rs.getBigDecimal("USER_PROFILE_ID"));
            		planNoticeDocumentChangelog.setChangedDate(rs.getTimestamp("CREATED_TS"));
            		String firstName = (rs.getString("FIRST_NAME")).toLowerCase();
            		String FName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
            	    String lastName = rs.getString("LAST_NAME").toLowerCase();
            		String LName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
        	        planNoticeDocumentChangelog.setChangedUserName(FName + " " +LName);
            		planNoticeDocumentChangelog.setDocumentId(rs.getInt("DOCUMENT_ID"));
            		planNoticeDocumentChangelog.setPlanNoticeDocumentChangeTypeDetail(new LookupDescription(rs.getString("CHANGE_TYPE_CODE"),rs.getString("DOCUMENT_CHANGE_TYPE_DESCRIPTION")));
					if (null != planNoticeDocumentChangelog.getChangedDate()
							&& StringUtils.isNotBlank(String.valueOf(planNoticeDocumentChangelog.getChangedDate()))) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(planNoticeDocumentChangelog.getChangedDate());
						cal.add(Calendar.DATE, 367); // 367 days added to the changed date get the date which is passed one year														
														// To avoid leap year confusion Business recommends to add 367 days													
						planNoticeDocumentChangelog.setChangedDatePlusOneYear(new Timestamp(cal.getTime().getTime()));
					}
            	}

		} catch (SQLException e) {
			handleSqlException(e, className,
					"getReportData",
					"Something went wrong while executing the statement -  Contract Number ["
							+ getContractNumber(criteria) + "]"); 
		} finally {
			close(stmt, connection);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}
		return planNoticeDocumentChangelog;
	}

	/**
	 * returns change history records which will suit the contract id/document id
	 * passed
	 * 
	 * @param contractId
	 * @param documentId
	 * @throws SystemException
	 */
	public static PlanNoticeDocumentChangeHistoryVO getCustomPlanNoticeDocumentHistory(Integer contractId,
			Integer documentId) throws SystemException, ReportServiceException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangelog = null;
		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);

			stmt = connection.prepareStatement(CONTRACT_NOTICE_DOCUMENT_HISTORY_SQL);
			stmt.setInt(1, contractId);
			stmt.setInt(2, documentId);
			rs = stmt.executeQuery();

			if (rs != null && rs.next()) {

				planNoticeDocumentChangelog = new PlanNoticeDocumentChangeHistoryVO();
				planNoticeDocumentChangelog.setContractId(contractId);
				planNoticeDocumentChangelog.setChangedProfileId(rs.getBigDecimal("USER_PROFILE_ID"));
				planNoticeDocumentChangelog.setChangedDate(rs.getTimestamp("CREATED_TS"));
				String firstName = (rs.getString("FIRST_NAME")).toLowerCase();
				String FName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
				String lastName = rs.getString("LAST_NAME").toLowerCase();
				String LName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
				planNoticeDocumentChangelog.setChangedUserName(FName + " " + LName);
				planNoticeDocumentChangelog.setDocumentId(rs.getInt("DOCUMENT_ID"));
				planNoticeDocumentChangelog.setPlanNoticeDocumentChangeTypeDetail(new LookupDescription(
						rs.getString("CHANGE_TYPE_CODE"), rs.getString("DOCUMENT_CHANGE_TYPE_DESCRIPTION")));
				if (null != planNoticeDocumentChangelog.getChangedDate()
						&& StringUtils.isNotBlank(String.valueOf(planNoticeDocumentChangelog.getChangedDate()))) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(planNoticeDocumentChangelog.getChangedDate());
					cal.add(Calendar.DATE, 367); // 367 days added to the changed date get the date which is passed
													// one year
													// To avoid leap year confusion Business recommends to add 367
													// days
					planNoticeDocumentChangelog.setChangedDatePlusOneYear(new Timestamp(cal.getTime().getTime()));
				}
			}

		} catch (SQLException e) {
			handleSqlException(e, className, "getReportData",
					"Something went wrong while executing the statement -  Contract Number [" + contractId + "]");
		} finally {
			close(stmt, connection);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}
		return planNoticeDocumentChangelog;
	}
	/**
	 * Updates the report with custom documents list
	 * @param criteria
	 * @param planDocumentReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	private static void setCustomPlanNoticeDocuments(ReportCriteria criteria,
			PlanDocumentReportData planDocumentReportData)
			throws SystemException, ReportServiceException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<PlanNoticeDocumentVO> customContractNotices = null;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);
			Integer contractId = getContractNumber(criteria);
			// get the total record count, used for paging
				int totalCount = getTotalRecordCount(connection, criteria);
						if (logger.isDebugEnabled()) {
							logger.debug("Total Count [" + totalCount + "]");
						}
							
			stmt = getPreparedStatement(connection, criteria, false,CONTRACT_NOTICE_DOCUMENT_SQL);
			 rs = stmt.executeQuery();
			 if (rs != null ) {
				 customContractNotices = new ArrayList<PlanNoticeDocumentVO>();
	            	while (rs.next()) {
	            		
	            		PlanNoticeDocumentVO planNoticeDocument = new PlanNoticeDocumentVO();
	            		planNoticeDocument.setContractId(contractId);
	            		planNoticeDocument.setDocumentId(rs.getInt("DOCUMENT_ID"));
	            		planNoticeDocument.setDocumentName(rs.getString("DOCUMENT_NAME").trim());
	            		planNoticeDocument.setDocumentDisplayOrder(rs.getInt("DISPLAY_ORDER"));
	            		planNoticeDocument.setDocumentFileName(rs.getString("DOCUMENT_FILE_NAME").trim());
	            		planNoticeDocument.setDocumentLocked(false);
	            		planNoticeDocument.setJhDocument(false);
	            		planNoticeDocument.setIccDocument(false);
	            		planNoticeDocument.setPostToPptInd(rs.getString("POST_TO_PPT_IND").trim());
	            		planNoticeDocument.setUploadDocFiveYears("Y".equalsIgnoreCase(rs.getString("DOCUMENT_FIVE_MORE").trim()) ? true : false);
	            		planNoticeDocument.setSoftDelIndicator(rs.getString("SOFT_DELETE_IND").trim());
	            		planNoticeDocument.setPlanNoticeDocumentChangeDetail(setCustomPlanNoticeDocumentHistory(criteria,rs.getInt("DOCUMENT_ID")));	            		
	            		planNoticeDocument.setDocNameAndUpdatedDate(getDocNameAndUpdatedDate(planNoticeDocument.getDocumentName(),planNoticeDocument.getPlanNoticeDocumentChangeDetail()));
	            		customContractNotices.add(planNoticeDocument);
	            	}
				}

			 planDocumentReportData.setCustomPlanNoticeDocuments(customContractNotices);
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
	 * This method will combine the DocumentName and ChangedDate (MMM yyyy)
	 * @param documentName
	 * @param planNoticeDocumentChangeDetail
	 * @return
	 */
	private static String getDocNameAndUpdatedDate(String documentName,
			PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeDetail) {
		String docName = documentName;
		if (null != planNoticeDocumentChangeDetail && null != planNoticeDocumentChangeDetail.getChangedDate()
				&& StringUtils.isNotBlank(String.valueOf(planNoticeDocumentChangeDetail.getChangedDate()))) {
			String pattern = "MMM yyyy";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			String docNameAndUpdatedDate = formatter
					.format(planNoticeDocumentChangeDetail.getChangedDate().toLocalDateTime());
			docName = new StringBuilder().append(documentName).append(" (").append(docNameAndUpdatedDate).append(")")
					.toString();
		}
		return docName;
	}

	/**
	 * Gets the alert frequency codes
	 * @param criteria
	 * @param planDocumentReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	private static void getUserManagerAlertFrequencyCodes(ReportCriteria criteria,
			PlanDocumentReportData planDocumentReportData)
			throws SystemException, ReportServiceException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		LookupDescription userManagerAlertFrequencyCode = null;
		List<LookupDescription> userManagerAlertFrequencyCodes = null;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);

						
						
			stmt = connection.prepareStatement(USER_ALERT_FREQUENCY_CODES_SQL);
			 rs = stmt.executeQuery();
			 if (rs != null ) {
				 userManagerAlertFrequencyCodes = new ArrayList<LookupDescription>();
	            	while (rs.next())
	            	{
	            		userManagerAlertFrequencyCode   = new LookupDescription();
	            		userManagerAlertFrequencyCode.setLookupCode(rs.getString("LOOKUP_CODE"));
	            		userManagerAlertFrequencyCode.setLookupDesc(rs.getString("LOOKUP_DESC"));
	            		userManagerAlertFrequencyCodes.add(userManagerAlertFrequencyCode);
	            	}
	            	planDocumentReportData.setUserManagerAlertFrequencyCodes(userManagerAlertFrequencyCodes);
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
	 * Updates the report object with alert timing codes.
	 * @param criteria
	 * @param planDocumentReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	private static void getUserManagerAlertTimingCodes(ReportCriteria criteria,
			PlanDocumentReportData planDocumentReportData)
			throws SystemException, ReportServiceException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		LookupDescription userManagerAlertTimingCode = null;
		List<LookupDescription> userManagerAlertTimingCodes = null;
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);

						
						
			stmt = connection.prepareStatement(USER_NOTICE_MANAGER_ALERT_TIMINGS_SQL);
			 rs = stmt.executeQuery();

			 if (rs != null ) {
				 userManagerAlertTimingCodes = new ArrayList<LookupDescription>();
	            	while (rs.next())
	            	{
	            		userManagerAlertTimingCode   = new LookupDescription();
	            		userManagerAlertTimingCode.setLookupCode(rs.getString("LOOKUP_CODE"));
	            		userManagerAlertTimingCode.setLookupDesc(rs.getString("LOOKUP_DESC"));
	            		userManagerAlertTimingCodes.add(userManagerAlertTimingCode);
	            	}
	            	planDocumentReportData.setUserManagerAlertTimingCodes(userManagerAlertTimingCodes);
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
	 * 			a. SQL to get complete record of contract notice documents
	 * 				in a contract 
	 * 		OR  b. SQL to get the total number of contract notice documents
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
			Connection connection, ReportCriteria criteria, boolean countOnly,String sql)
	throws SQLException, SystemException {

		StringBuffer query = new StringBuffer(sql);
		StringBuffer selectedQuery = new StringBuffer();
		SqlPair pair = null;
		
		
	    // append the ORDER BY clause when there is any sorting requirement.
		/*if ( criteria.getSorts().size() > 0) {
			query.append(" ORDER BY ").append(
					criteria.getSortPhrase(fieldToColumnMap));
		}*/
		pair = getSqlPair(query.toString());
		if (countOnly) {
			selectedQuery.append(pair.getCountQuery());
		} else {
			selectedQuery.append(pair.getQuery());
		}
		PreparedStatement stmt = connection.prepareStatement(selectedQuery.toString());

		// set filter parameters into the WHERE clause.
		setParameters(stmt, criteria, 1);

		return stmt;
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
				.getFilterValue(PlanDocumentReportData.FILTER_CONTRACT_NUMBER);
		Integer contractNumber = Integer.valueOf(0);
		if(contractNumberString != null){
			contractNumber = Integer.valueOf(contractNumberString);
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
				.getFilterValue(PlanDocumentReportData.FILTER_USER_PROFILE_ID);
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
		Timestamp fromDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
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
		Timestamp toDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
		if(toDateSelected != null){
			toDate =  new Timestamp(toDateSelected.getTime());;
		}
		return toDate;
		
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
	 * Gets the total record count (number of custom notice documents 
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
			stmt = getPreparedStatement(connection, criteria, true,CONTRACT_NOTICE_DOCUMENT_SQL);
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
	 * This method will get the status of terms acceptance.
	 * @param criteria
	 * @param planDocumentReportData
	 * @return
	 * @throws SystemException
	 */
	public static boolean getTermsAndAcceptanceInd(ReportCriteria criteria,PlanDocumentReportData planDocumentReportData) throws SystemException{
		

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean termOfUse = false;
		String termsOfUseCode = "";
		try {
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(SQL_SELECT_TERMS_ACCEPTANCE_CODE);
			stmt.setBigDecimal(1, getUserProfileId(criteria));
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				 termOfUse = true;
				 termsOfUseCode = rs.getString("TERMS_ACCEPTANCE_CODE").trim();
			}
			planDocumentReportData.setTermOfUseAcceptance(termOfUse);
			planDocumentReportData.setTermsOfUseCode(termsOfUseCode);
			}catch (SQLException e) {
				logger.error("Retrieve the terms acceptance code based  on the profile id", e);
				throw new SystemException(e, 
						"Problem occurred during getTermsAndAcceptanceInd sql call. ");
			} finally {
				close(stmt, conn);
			}
			return termOfUse;
	}
	
}
