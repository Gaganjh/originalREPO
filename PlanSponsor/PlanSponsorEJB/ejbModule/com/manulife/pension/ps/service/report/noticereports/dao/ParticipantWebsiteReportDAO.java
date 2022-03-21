package com.manulife.pension.ps.service.report.noticereports.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.noticereports.valueobject.ParticipantWebsiteReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * Provides data access to Notice Manager Alerts Control Report.
 * 
 * 
 */

public class ParticipantWebsiteReportDAO extends NoticeReportsBaseDAO {

	/**
	 * Class name
	 */
	private static final String className = ParticipantWebsiteReportDAO.class
			.getName();
	
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe		
	public static FastDateFormat searchDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.S", Locale.US);
	/**
	 * Logger object
	 */
	private static final Logger logger = Logger
			.getLogger(ParticipantWebsiteReportDAO.class);

	private static final String SQL_ALL_VISITS_COUNT = "SELECT COUNT(PARTICIPANT_ID)  FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "WEBSITE_USER_ACTION_LOG WHERE USER_ACTION_NAME = "
			+ "''EZK_Notice_Manager_Important_Documents_Page'' AND CREATED_TS BETWEEN ? AND ?  {0}";

	private static final String SQL_REPEAT_VISITS_COUNT = "SELECT  COUNT(*) FROM "
			+ "( SELECT PARTICIPANT_ID FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "WEBSITE_USER_ACTION_LOG WHERE CREATED_TS BETWEEN ? AND ?  {0} "
			+ "AND USER_ACTION_NAME = "
			+ "''EZK_Notice_Manager_Important_Documents_Page'' "
			+ "GROUP BY  PARTICIPANT_ID HAVING COUNT(*) > 1 )";

	private static final String SQL_NEW_VISITS_COUNT = "SELECT  COUNT(*) FROM "
			+ "( SELECT PARTICIPANT_ID FROM " + PLAN_SPONSOR_SCHEMA_NAME
			+ "WEBSITE_USER_ACTION_LOG WHERE CREATED_TS BETWEEN  ? AND  ?  {0} "
			+ "AND USER_ACTION_NAME = "
			+ "''EZK_Notice_Manager_Important_Documents_Page'' "
			+ "GROUP BY  PARTICIPANT_ID ) ";

	private static final String SQL_MOST_VISITED_MONTH = "WITH MOST_VISITED_MONTH ("
			+ "MONTH_NAME,VYEAR,VISTIS ,MONTH_NUMBER) AS ("
			+ " SELECT MONTHNAME(CREATED_TS) AS MONTH_NAME ,"
			+ "YEAR(CREATED_TS) AS VYEAR, COUNT(PARTICIPANT_ID) AS "
			+ "VISTIS,MONTH(CREATED_TS)AS MONTH_NUMBER FROM	"
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "WEBSITE_USER_ACTION_LOG WHERE CREATED_TS BETWEEN ? AND ?  {0} "
			+ "AND USER_ACTION_NAME = "
			+ "''EZK_Notice_Manager_Important_Documents_Page'' "
			+ "GROUP BY YEAR(CREATED_TS),MONTHNAME(CREATED_TS),MONTH(CREATED_TS) "
			+ "ORDER BY VISTIS DESC) "
			+ "SELECT MONTH_NAME,VYEAR,VISTIS,MONTH_NUMBER FROM MOST_VISITED_MONTH "
			+ "WHERE VISTIS IN( SELECT MAX(VISTIS) FROM MOST_VISITED_MONTH) "
			+ "ORDER BY MONTH_NUMBER ";
	
	private static final String SQL_ALL_DOCUMENT_VISITS_COUNT = "SELECT COUNT(PARTICIPANT_ID)  FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "WEBSITE_USER_ACTION_LOG WHERE USER_ACTION_NAME = "
			+ "''EZK_Notice_Manager_Important_Document_Viewed'' AND CREATED_TS BETWEEN ? AND ?  {0} ";

	public static void getReportData(ReportCriteria criteria,
			ParticipantWebsiteReportData participantWebsiteReportData)
			throws SystemException, ReportServiceException {
		 	Integer contractId = getContractNumber(criteria);
	        boolean isContractSearch = (contractId != null && contractId.intValue() > 0);
		Connection connection = null;
		try {
			connection = getReadUncommittedConnection(className,
					CUSTOMER_DATA_SOURCE_NAME);
			allVisitsCount(criteria, participantWebsiteReportData, connection,contractId,isContractSearch);
			repeatVisitsCount(criteria, participantWebsiteReportData,
					connection,contractId,isContractSearch);
			newVisitsCount(criteria, participantWebsiteReportData, connection,contractId,isContractSearch);
			mostVisitedMonth(criteria, participantWebsiteReportData, connection,contractId,isContractSearch);
			allDocumentVisitsCount(criteria, participantWebsiteReportData, connection,contractId,isContractSearch);
		} catch (SQLException e) {
			handleSqlException(e, className, "getReportData",
					"Error while accessing Participant Website Report data ");
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}

		}
	}

	private static void allVisitsCount(ReportCriteria criteria,
			ParticipantWebsiteReportData participantWebsiteReportData,
			Connection connection,Integer contractId,boolean isContractSearch) throws SystemException,
			ReportServiceException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String contractClause = getContractClause(isContractSearch);
	        String query = MessageFormat.format(SQL_ALL_VISITS_COUNT,
	                contractClause);
	        stmt = connection.prepareStatement(query);
			setReportFilters(participantWebsiteReportData,contractId,isContractSearch, stmt);
			
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				participantWebsiteReportData
						.setTotalVisitorsCount(rs.getInt(1));
			}
		} catch (SQLException e) {
			handleSqlException(e, className, "getReportData",
					"Error while accessing Participant Website Report data ");
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {

				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getAlertFrequencyStats");
		}
	}

	private static void repeatVisitsCount(ReportCriteria criteria,
			ParticipantWebsiteReportData participantWebsiteReportData,
			Connection connection,Integer contractId,boolean isContractSearch) throws SystemException,
			ReportServiceException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String contractClause = getContractClause(isContractSearch);
	        String query = MessageFormat.format(SQL_REPEAT_VISITS_COUNT,
	                contractClause);
	        stmt = connection.prepareStatement(query);
			setReportFilters(participantWebsiteReportData,contractId,isContractSearch, stmt);
			
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				participantWebsiteReportData.setTotalRepeatVisitorsCount(rs
						.getInt(1));
			}
		} catch (SQLException e) {
			handleSqlException(e, className, "getReportData",
					"Error while accessing Participant Website Report data ");
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {

				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getAlertFrequencyStats");
		}
	}

	private static void newVisitsCount(ReportCriteria criteria,
			ParticipantWebsiteReportData participantWebsiteReportData,
			Connection connection,Integer contractId,boolean isContractSearch) throws SystemException,
			ReportServiceException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String contractClause = getContractClause(isContractSearch);
	        String query = MessageFormat.format(SQL_NEW_VISITS_COUNT,
	                contractClause);
	        stmt = connection.prepareStatement(query);
			setReportFilters(participantWebsiteReportData,contractId,isContractSearch, stmt);
			
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				participantWebsiteReportData.setTotalNewVisitorsCount(rs
						.getInt(1));
			}
		} catch (SQLException e) {
			handleSqlException(e, className, "getReportData",
					"Error while accessing Participant Website Report data ");
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {

				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getAlertFrequencyStats");
		}
	}

	private static void mostVisitedMonth(ReportCriteria criteria,
			ParticipantWebsiteReportData participantWebsiteReportData,
			Connection connection,Integer contractId,boolean isContractSearch) throws SystemException,
			ReportServiceException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> monthList = new ArrayList<String>();
		try {
			String contractClause = getContractClause(isContractSearch);
	        String query = MessageFormat.format(SQL_MOST_VISITED_MONTH,
	                contractClause);
	        stmt = connection.prepareStatement(query);
			setReportFilters(participantWebsiteReportData,contractId,isContractSearch, stmt);
			
			rs = stmt.executeQuery();
			while (rs != null && rs.next()) {
				int year = rs.getInt(2);
                int month = rs.getInt(4);
                String monthYear = MONTHS_DESCRIPTION[month - 1] + SINGLE_SPACE_SYMBOL + year;
                monthList.add(monthYear);
			}
			participantWebsiteReportData.setMostVisitedMonth(monthList);
		} catch (SQLException e) {
			handleSqlException(e, className, "getReportData",
					"Error while accessing Participant Website Report data ");
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {

				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getAlertFrequencyStats");
		}
	}
	
	private static void allDocumentVisitsCount(ReportCriteria criteria,
			ParticipantWebsiteReportData participantWebsiteReportData,
			Connection connection,Integer contractId,boolean isContractSearch) throws SystemException,
			ReportServiceException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String contractClause = getContractClause(isContractSearch);
	        String query = MessageFormat.format(SQL_ALL_DOCUMENT_VISITS_COUNT,
	                contractClause);
	        stmt = connection.prepareStatement(query);
			setReportFilters(participantWebsiteReportData,contractId,isContractSearch, stmt);
			
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				participantWebsiteReportData.setTotalDocumentViewCount(rs.getInt(1));
			}
		} catch (SQLException e) {
			handleSqlException(e, className, "getReportData",
					"Error while accessing Participant Website Report data ");
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {

				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getAlertFrequencyStats");
		}
	}

	/**
     * Sets Report Search Filters.
     * 
     * @param participantWebsiteReportData
     * @param contractId
     * @param isContractSearch
     * @param stmt
     * @throws SQLException
	 * @throws SystemException 
     */
    private static void setReportFilters(ParticipantWebsiteReportData participantWebsiteReportData,
            Integer contractId, boolean isContractSearch, PreparedStatement stmt)
            throws SQLException, SystemException {
    	
    	 Date fromDate = null;
    	    Date toDate = null;
    		try {
    			fromDate = (Date) searchDateFormat.parse(participantWebsiteReportData.getFromDate().toString() + " 00:00:00.0");
    			toDate = (Date) searchDateFormat.parse(participantWebsiteReportData.getToDate().toString() + " 23:59:59.66");
    		} catch (ParseException e) {
    			throw new SystemException(
    					"Error while parsing fromDate and toDate" + e.getMessage());
    		}
    			 Timestamp fromTimestamp = new Timestamp(fromDate.getTime());
    			 Timestamp toTimestamp = new Timestamp(toDate.getTime());
    	        stmt.setTimestamp(1, fromTimestamp);
    	        stmt.setTimestamp(2, toTimestamp);
        if (isContractSearch) {
            stmt.setInt(3, contractId.intValue());
        }

    }
	
	/**
     * Returns the SQL clause if contract filter is active.
     * 
     * @param isContractSearch
     * @return
     */
    private static String getContractClause(boolean isContractSearch) {
        String clause = "";
        if (isContractSearch) {
            clause = SQL_CONTRACT_FILTER_CLAUSE_PLAIN;
        }
        return clause;
    }

}
