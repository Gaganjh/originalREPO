package com.manulife.pension.ps.service.report.noticereports.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.noticereports.valueobject.AlertsReportData;
import com.manulife.pension.ps.service.report.noticereports.valueobject.AlertsReportDistributionVO;
import com.manulife.pension.ps.service.report.noticereports.valueobject.AlertsReportFreqVO;
import com.manulife.pension.ps.service.report.noticereports.valueobject.AlertsReportUserStatsVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * Provides data access to Notice Manager Alerts Control Report.
 */
public class AlertsReportDAO extends NoticeReportsBaseDAO {
	
	public static FastDateFormat searchDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.S", Locale.US);

    private static final String className = AlertsReportDAO.class.getName();

    private static final Logger logger = Logger.getLogger(AlertsReportDAO.class);

    private static final String SQL_SELECT_NUMBER_OF_ALERT_USERS_PLAN_SPONSOR = "WITH URGENT_IND  (URGENCY_IND) AS (SELECT COUNT(*) " +
    		"AS URGENCY_IND FROM " +
    		PLAN_SPONSOR_SCHEMA_NAME +
    		"USER_NOTICE_MANAGER_ALERT UN, " +
    		 PLAN_SPONSOR_SCHEMA_NAME +
    		"USER_CONTRACT UC WHERE un.ALERT_URGENCY_IND ='U' AND UN.START_DATE " +
    		"BETWEEN ? AND ?" +
    		"{0} " +
    		" AND UN.USER_PROFILE_ID = UC.USER_PROFILE_ID AND UN.CONTRACT_ID = UC.CONTRACT_ID" +
    		" AND UC.SECURITY_ROLE_CODE IN ('PSU', 'TRT', 'AUS', 'ADC') ),NORMAL_IND (NORMAL_IND)  " +
    		"AS (SELECT COUNT(*) AS NORMAL_IND FROM " +
    		PLAN_SPONSOR_SCHEMA_NAME +
    		"USER_NOTICE_MANAGER_ALERT UN, " +
    		PLAN_SPONSOR_SCHEMA_NAME +
    		"USER_CONTRACT UC WHERE UN.ALERT_URGENCY_IND ='N' AND UN.START_DATE " +
    		"BETWEEN ? AND ? " +
    		"{0} " +
    		"AND UN.USER_PROFILE_ID = UC.USER_PROFILE_ID AND UN.CONTRACT_ID = UC.CONTRACT_ID AND " +
    		"UC.SECURITY_ROLE_CODE IN ('PSU', 'TRT', 'AUS', 'ADC')),ALERT_USERS( ALERT_USERS, NUMBER_ALERTS ) " +
    		"AS (SELECT COUNT(DISTINCT UN.USER_PROFILE_ID) AS ALERT_USERS, COUNT(*) AS NUMBER_ALERTS FROM " +
    		PLAN_SPONSOR_SCHEMA_NAME +
    		"USER_NOTICE_MANAGER_ALERT UN, " +
    		PLAN_SPONSOR_SCHEMA_NAME +
    		"USER_CONTRACT UC WHERE UN.START_DATE " +
    		"BETWEEN ? AND ? " +
    		"{0} " +
    		"AND UN.USER_PROFILE_ID = UC.USER_PROFILE_ID AND UN.CONTRACT_ID = UC.CONTRACT_ID " +
    		"AND UC.SECURITY_ROLE_CODE IN ('PSU', 'TRT', 'AUS', 'ADC') ) " +
    		"SELECT URGENCY_IND , NORMAL_IND , ALERT_USERS , NUMBER_ALERTS FROM URGENT_IND,NORMAL_IND ,ALERT_USERS";
    
    private static final String SQL_SELECT_NUMBER_OF_DELETED_ALERTS_PLAN_SPONSOR = "SELECT COUNT(*) AS NUM_DELETED_ALERTS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE WP.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + "AND WP.USER_ACTION_NAME = '"
            + ALERT_DELETE
            + "' "
            + "AND WP.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND WP.CONTRACT_ID = UC.CONTRACT_ID "
            + "AND UC.SECURITY_ROLE_CODE IN ('PSU', 'TRT', 'AUS', 'ADC')";

    private static final String SQL_SELECT_NUMBER_OF_ALERT_USERS_INTERMEDIARY_CONTACT = 
    		"WITH urgent_ind (URGENCY_IND) "
    				+ "     AS (SELECT COUNT(*) AS URGENCY_IND "
    				+ "         FROM   " 
    				+ PLAN_SPONSOR_SCHEMA_NAME 
    				+ "USER_NOTICE_MANAGER_ALERT UN, "
    				+ PLAN_SPONSOR_SCHEMA_NAME 
    				+ "USER_CONTRACT UC "
    				+ "         WHERE  UN.ALERT_URGENCY_IND = 'U' "
    				+ "                AND UN.START_DATE BETWEEN ? AND ? "
    				+ "{0} "
    				+ "                AND UN.USER_PROFILE_ID = UC.USER_PROFILE_ID "
    				+ "                AND UN.CONTRACT_ID = UC.CONTRACT_ID "
    				+ "                AND UC.SECURITY_ROLE_CODE = 'INC'), "
    				+ "     normal_ind (NORMAL_IND) "
    				+ "     AS (SELECT COUNT(*) AS NORMAL_IND "
    				+ "         FROM   "
    				+ PLAN_SPONSOR_SCHEMA_NAME
    				+ "USER_NOTICE_MANAGER_ALERT UN, "
    				+ PLAN_SPONSOR_SCHEMA_NAME
    				+ "USER_CONTRACT UC "
    				+ "         WHERE  UN.ALERT_URGENCY_IND = 'N' "
    				+ "                AND UN.START_DATE BETWEEN ? AND ? "
    				+ "{0} "
    				+ "                AND UN.USER_PROFILE_ID = UC.USER_PROFILE_ID "
    				+ "                AND UN.CONTRACT_ID = UC.CONTRACT_ID "
    				+ "                AND UC.SECURITY_ROLE_CODE = 'INC'), "
    				+ "     alert_users( ALERT_USERS, NUMBER_ALERTS ) "
    				+ "     AS (SELECT COUNT(DISTINCT UN.USER_PROFILE_ID) AS ALERT_USERS, "
    				+ "                COUNT(*)                           AS NUMBER_ALERTS "
    				+ "         FROM   " 
    				+ PLAN_SPONSOR_SCHEMA_NAME
    				+ "USER_NOTICE_MANAGER_ALERT UN, "
    				+ PLAN_SPONSOR_SCHEMA_NAME
    				+ "USER_CONTRACT UC "
    				+ "         WHERE  UN.START_DATE BETWEEN ? AND ? "
    				+ "{0} "
    				+ "                AND UN.USER_PROFILE_ID = UC.USER_PROFILE_ID "
    				+ "                AND UN.CONTRACT_ID = UC.CONTRACT_ID "
    				+ "                AND UC.SECURITY_ROLE_CODE = 'INC') "
    				+ "SELECT URGENCY_IND, "
    				+ "       NORMAL_IND, "
    				+ "       ALERT_USERS, "
    				+ "       NUMBER_ALERTS "
    				+ "FROM   urgent_ind, "
    				+ "       normal_ind, "
    				+ "       alert_users";

    private static final String SQL_SELECT_NUMBER_OF_DELETED_ALERTS_INTERMEDIARY_CONTACT = "SELECT COUNT(*) AS NUM_DELETED_ALERTS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE WP.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + "AND WP.USER_ACTION_NAME = '"
            + ALERT_DELETE
            + "' "
            + "AND WP.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND WP.CONTRACT_ID = UC.CONTRACT_ID " + "AND UC.SECURITY_ROLE_CODE = 'INC' ";;

    private static final String SQL_SELECT_NUMBER_OF_ALERT_USERS_TPA = "WITH total_care_users (USER_PROFILE_ID) "
    		+ "    		      AS (SELECT USER_PROFILE_ID "
    		+ "    		          FROM   " 
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "EXTERNAL_USER_TPA_FIRM EU "
    		+ "    		          WHERE  TPA_FIRM_ID = "
    		+ TOTAL_CARE_TPA_ADMIN_ID
    		+ " ), "
    		+ "    		      urgent_ind (URGENT_IND) "
    		+ "    		      AS (SELECT COUNT(*) AS ALERT_URGENCY_IND "
    		+ "    		          FROM "
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "    		 USER_NOTICE_MANAGER_ALERT UN, "
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "    		 USER_PROFILE UP "
    		+ "    		          WHERE  UN.ALERT_URGENCY_IND = 'U' "
    		+ "    		                 AND UN.START_DATE BETWEEN ? AND ? "
    		+ "{0} "
    		+ "    		                 AND UN.USER_PROFILE_ID = UP.USER_PROFILE_ID "
    		+ "    		                 AND UP.PSW_DIRECTORY_ROLE_CODE = 'TPA' "
    		+ "    		                 AND UN.USER_PROFILE_ID NOT IN (SELECT USER_PROFILE_ID "
    		+ "    		                                                FROM   total_care_users)), "
    		+ "    		      normal_ind (NORMAL_IND) "
    		+ "    		      AS (SELECT COUNT(*) AS ALERT_URGENCY_IND "
    		+ "    		          FROM "
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "    		 USER_NOTICE_MANAGER_ALERT UN, "
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "    		 USER_PROFILE UP "
    		+ "    		          WHERE  UN.ALERT_URGENCY_IND = 'N' "
    		+ "    		                 AND UN.START_DATE BETWEEN ? AND ? "
    		+ "{0} "
    		+ "    		                 AND UN.USER_PROFILE_ID = UP.USER_PROFILE_ID "
    		+ "    		                 AND UP.PSW_DIRECTORY_ROLE_CODE = 'TPA' "
    		+ "    		                 AND UN.USER_PROFILE_ID NOT IN (SELECT USER_PROFILE_ID "
    		+ "    		                                                FROM   total_care_users)) "
    		+ "    		 SELECT URGENT_IND,NORMAL_IND,COUNT(DISTINCT UN.USER_PROFILE_ID) AS ALERT_USERS, "
    		+ "    		        COUNT(*) AS NUMBER_ALERTS "
    		+ "    		 FROM "
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "    		 USER_NOTICE_MANAGER_ALERT UN, "
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "    		 USER_PROFILE UP, "
    		+ "    		        urgent_ind, "
    		+ "    		        normal_ind "
    		+ "    		 WHERE  UN.START_DATE BETWEEN ? AND ? "
    		+ "{0} "
    		+ "    		        AND UN.USER_PROFILE_ID = UP.USER_PROFILE_ID "
    		+ "    		        AND UP.PSW_DIRECTORY_ROLE_CODE = 'TPA' "
    		+ "    		        AND UN.USER_PROFILE_ID NOT IN (SELECT USER_PROFILE_ID "
    		+ "    		                                       FROM   total_care_users) GROUP BY NORMAL_IND,URGENT_IND"; 
    private static final String SQL_SELECT_NUMBER_OF_ALERT_USERS_TOTAL_CARE = "WITH total_care_users (USER_PROFILE_ID) "
    		+ "    		      AS (SELECT USER_PROFILE_ID "
    		+ "    		          FROM   " 
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "EXTERNAL_USER_TPA_FIRM EU "
    		+ "    		          WHERE  TPA_FIRM_ID = "
    		+ TOTAL_CARE_TPA_ADMIN_ID
    		+ " ), "
    		+ "    		      urgent_ind (URGENT_IND) "
    		+ "    		      AS (SELECT COUNT(*) AS ALERT_URGENCY_IND "
    		+ "    		          FROM "
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "    		 USER_NOTICE_MANAGER_ALERT UN, "
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "    		 USER_PROFILE UP "
    		+ "    		          WHERE  UN.ALERT_URGENCY_IND = 'U' "
    		+ "    		                 AND UN.START_DATE BETWEEN ? AND ? "
    		+ "{0} "
    		+ "    		                 AND UN.USER_PROFILE_ID = UP.USER_PROFILE_ID "
    		+ "    		                 AND UP.PSW_DIRECTORY_ROLE_CODE = 'TPA' "
    		+ "    		                 AND UN.USER_PROFILE_ID IN (SELECT USER_PROFILE_ID "
    		+ "    		                                                FROM   total_care_users)), "
    		+ "    		      normal_ind (NORMAL_IND) "
    		+ "    		      AS (SELECT COUNT(*) AS ALERT_URGENCY_IND "
    		+ "    		          FROM "
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "    		 USER_NOTICE_MANAGER_ALERT UN, "
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "    		 USER_PROFILE UP "
    		+ "    		          WHERE  UN.ALERT_URGENCY_IND = 'N' "
    		+ "    		                 AND UN.START_DATE BETWEEN ? AND ? "
    		+ "{0} "
    		+ "    		                 AND UN.USER_PROFILE_ID = UP.USER_PROFILE_ID "
    		+ "    		                 AND UP.PSW_DIRECTORY_ROLE_CODE = 'TPA' "
    		+ "    		                 AND UN.USER_PROFILE_ID IN (SELECT USER_PROFILE_ID "
    		+ "    		                                                FROM   total_care_users)) "
    		+ "    		 SELECT URGENT_IND,NORMAL_IND,COUNT(DISTINCT UN.USER_PROFILE_ID) AS ALERT_USERS, "
    		+ "    		        COUNT(*) AS NUMBER_ALERTS "
    		+ "    		 FROM "
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "    		 USER_NOTICE_MANAGER_ALERT UN, "
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "    		 USER_PROFILE UP, "
    		+ "    		        urgent_ind, "
    		+ "    		        normal_ind "
    		+ "    		 WHERE  UN.START_DATE BETWEEN ? AND ? "
    		+ "{0} "
    		+ "    		        AND UN.USER_PROFILE_ID = UP.USER_PROFILE_ID "
    		+ "    		        AND UP.PSW_DIRECTORY_ROLE_CODE = 'TPA' "
    		+ "    		        AND UN.USER_PROFILE_ID IN (SELECT USER_PROFILE_ID "
    		+ "    		                                       FROM   total_care_users) GROUP BY NORMAL_IND,URGENT_IND"; 

    private static final String SQL_CONTRACT_FILTER_CLAUSE = "AND UN.CONTRACT_ID = ? ";

    private static final String SQL_SELECT_NUMBER_OF_ALERT_FREQUENCY = "SELECT ALERT_FREQUENCY_CODE, COUNT(*) AS NUM_ALERTS  "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_NOTICE_MANAGER_ALERT  "
            + "WHERE START_DATE BETWEEN ? AND ?  {0} " + "GROUP BY ALERT_FREQUENCY_CODE ";

    private static final String WEBSITE_USER_ACTION_QUALIFIER = "WP.";

    private static final String SQL_SELECT_NUMBER_OF_DELETED_ALERTS_TPA = "WITH TOTAL_CARE_USERS (USER_PROFILE_ID) AS  "
            + "(SELECT USER_PROFILE_ID FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "EXTERNAL_USER_TPA_FIRM EU WHERE TPA_FIRM_ID = "
            + TOTAL_CARE_TPA_ADMIN_ID
            + ")  "
            + "SELECT COUNT(*) AS NUM_DELETED_ALERTS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_PROFILE UP "
            + "WHERE WP.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + "AND WP.USER_ACTION_NAME = '"
            + ALERT_DELETE
            + "' "
            + "AND WP.USER_PROFILE_ID = UP.USER_PROFILE_ID "
            + "AND UP.PSW_DIRECTORY_ROLE_CODE  = 'TPA' "
            + "AND UP.USER_PROFILE_ID NOT IN (SELECT USER_PROFILE_ID FROM TOTAL_CARE_USERS) ";

    private static final String SQL_SELECT_NUMBER_OF_DELETED_ALERTS_TOTAL_CARE = "WITH TOTAL_CARE_USERS (USER_PROFILE_ID) AS  "
            + "(SELECT USER_PROFILE_ID FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "EXTERNAL_USER_TPA_FIRM EU WHERE TPA_FIRM_ID = "
            + TOTAL_CARE_TPA_ADMIN_ID
            + ")  "
            + "SELECT COUNT(*) AS NUM_DELETED_ALERTS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_PROFILE UP "
            + "WHERE WP.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + "AND WP.USER_ACTION_NAME = '"
            + ALERT_DELETE
            + "' "
            + "AND WP.USER_PROFILE_ID = UP.USER_PROFILE_ID "
            + "AND UP.PSW_DIRECTORY_ROLE_CODE  = 'TPA' "
            + "AND UP.USER_PROFILE_ID IN (SELECT USER_PROFILE_ID FROM TOTAL_CARE_USERS) ";

    private static final String TOTALS = "Totals";

    private static final String SQL_SELECT_NOTICE_DISTRIBUTION_BY_DUE_DATE = "SELECT START_DATE AS DUE_DATE "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_NOTICE_MANAGER_ALERT "
            + "WHERE START_DATE <= ? " + "AND ALERT_FREQUENCY_CODE = ? ";

    private static final String SQL_SELECT_NOTICE_DISTRIBUTION_BY_DUE_DATE_ADHOC = "SELECT MONTH(START_DATE), COUNT(*) AS NUM_ALERTS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_NOTICE_MANAGER_ALERT "
            + "WHERE ALERT_FREQUENCY_CODE = '"
            + AlertsReportData.ADHOC_FREQUENCY_CODE
            + "' "
            + "AND START_DATE BETWEEN ? AND ? " + "{0} " + "GROUP BY MONTH(START_DATE) ";

    private static final String SQL_SELECT_ALERTS_FOR_CURRENT_YEAR_WITH_FREQUENCY_FILTERED = "SELECT MONTH(START_DATE), YEAR(START_DATE), count(*) COUNT "
        + "FROM "
        + PLAN_SPONSOR_SCHEMA_NAME
        + "USER_NOTICE_MANAGER_ALERT "
        + "WHERE ALERT_FREQUENCY_CODE = ? "
        + "AND START_DATE <= ?  " + "{0} "
        + "GROUP BY MONTH(START_DATE), YEAR(START_DATE)";

    private static final String SQL_SELECT_ALERTS_FOR_CURRENT_YEAR_WITH_ADHOC_FREQUENCY_FILTERED = "SELECT MONTH(START_DATE), count(*) COUNT "
        + "FROM "
        + PLAN_SPONSOR_SCHEMA_NAME
        + "USER_NOTICE_MANAGER_ALERT "
        + "WHERE ALERT_FREQUENCY_CODE = ? "
        + "AND START_DATE <= ?  " + "{0} "
        + "GROUP BY MONTH(START_DATE)";

    private static Map<String, String> alertFrequencyCodeNameMap = initAlertFreqMap();

    private static Map<String, Integer> alertFrequencyCodeMonthsMap = initAlertFreqMonthsMap();

    private static String[] MONTH_NAMES = new DateFormatSymbols().getMonths();
    /**
     * Gets the data for alerts Report.
     * 
     * @param criteria
     * @param alertsReportData
     * @throws SystemException
     * @throws ReportServiceException
     */
    public static void getReportData(ReportCriteria criteria, AlertsReportData alertsReportData)
            throws SystemException, ReportServiceException {

        Connection connection = null;
        PreparedStatement stmt = null;
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getReportData");
        }

        try {
            Integer contractId = getContractNumber(criteria);
            boolean isContractSearch = (contractId != null && contractId.intValue() > 0);
            connection = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);

            getNumberOfAlertUsers(connection, stmt, criteria, alertsReportData, contractId,
                    isContractSearch);
            getAlertFrequencyStats(connection, stmt, criteria, alertsReportData, contractId,
                    isContractSearch);
            getAlertsDistribution(connection, stmt, criteria, alertsReportData, contractId,
                    isContractSearch);

        } catch (SQLException e) {
            handleSqlException(e, className, "getReportData",
                    "Error while accessing Alerts Report data  -  Contract Number ["
                            + getContractNumber(criteria) + "]");
        } finally {
            close(stmt, connection);

        }

    }

    /**
     * Gets the number of Alert Users based on criteria.
     * 
     * @param stmt2
     * @param connection
     * 
     * @param criteria
     * @param isContractSearch
     * @param contractId
     * @throws SystemException
     * @throws ReportServiceException
     * @throws SQLException
     */
    private static void getNumberOfAlertUsers(Connection connection, PreparedStatement stmt,
            ReportCriteria criteria, AlertsReportData alertsReportData, Integer contractId,
            boolean isContractSearch) throws SystemException, ReportServiceException, SQLException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getNumberOfAlertUsers");
        }

        List<AlertsReportUserStatsVO> list = getAlertUserStatsList();
        String[][] alertUserStatsSQL = {
        		{ PLAN_SPONSOR, SQL_SELECT_NUMBER_OF_ALERT_USERS_PLAN_SPONSOR },
                { INTERMEDIARY_CONTACT, SQL_SELECT_NUMBER_OF_ALERT_USERS_INTERMEDIARY_CONTACT },
                { TPA, SQL_SELECT_NUMBER_OF_ALERT_USERS_TPA },
                { TOTAL_CARE, SQL_SELECT_NUMBER_OF_ALERT_USERS_TOTAL_CARE } };

        for (int i = 0; i < alertUserStatsSQL.length; i++) {
            getAlertUserData(connection, alertsReportData, stmt, list, contractId,
                    isContractSearch, alertUserStatsSQL[i][0], alertUserStatsSQL[i][1]);
        }

        String[][] deleteAlertByUserSQL = {
                { PLAN_SPONSOR, SQL_SELECT_NUMBER_OF_DELETED_ALERTS_PLAN_SPONSOR },
                { INTERMEDIARY_CONTACT, SQL_SELECT_NUMBER_OF_DELETED_ALERTS_INTERMEDIARY_CONTACT },
                { TPA, SQL_SELECT_NUMBER_OF_DELETED_ALERTS_TPA },
                { TOTAL_CARE, SQL_SELECT_NUMBER_OF_DELETED_ALERTS_TOTAL_CARE } };

        // get number of deleted alerts
        for (int i = 0; i < deleteAlertByUserSQL.length; i++) {
            getDeletedAlertsByUserCategory(connection, alertsReportData, list, contractId,
                    isContractSearch, deleteAlertByUserSQL[i][0], deleteAlertByUserSQL[i][1]);
        }

        addupUserData(list);
        alertsReportData.setAlertUsersStatsList(list);
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getNumberOfAlertUsers");
        }

    }

    /**
     * Retrieve alert user data for specified user category.
     * 
     * @param connection
     * @param alertsReportData
     * @param stmt
     * @param list
     * @param contractId
     * @param isContractSearch
     * @param query
     * @param userCategory
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getAlertUserData(Connection connection, AlertsReportData alertsReportData,
            PreparedStatement stmt, List<AlertsReportUserStatsVO> list, Integer contractId,
            boolean isContractSearch, String userCategory, String userQuery) throws SQLException, SystemException {
        ResultSet rs;
        String query = MessageFormat.format(replaceSingleQuote(userQuery),
                getContractClause(isContractSearch));
        stmt = connection.prepareStatement(query);
        setReportFiltersData(alertsReportData, contractId, isContractSearch, stmt);
        rs = stmt.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                getUserData(rs, list, userCategory);
            }
        }

    }

    /**
     * Retrieve deleted alert users by category.
     * 
     * @param connection
     * @param alertsReportData
     * @param list
     * @param contractId
     * @param isContractSearch
     * @param userCategory
     * @param userDeleteQuery
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getDeletedAlertsByUserCategory(Connection connection,
            AlertsReportData alertsReportData, List<AlertsReportUserStatsVO> list,
            Integer contractId, boolean isContractSearch, String userCategory,
            String userDeleteQuery) throws SQLException, SystemException {
        PreparedStatement stmt;
        ResultSet rs;
        String query;
        query = MessageFormat.format(replaceSingleQuote(userDeleteQuery),
                getContractClauseWithQualifier(isContractSearch, WEBSITE_USER_ACTION_QUALIFIER));
        stmt = connection.prepareStatement(query);
        setReportFilters(alertsReportData, contractId, isContractSearch, stmt);
        AlertsReportUserStatsVO userVO = getUserVO(list, userCategory);
        rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            userVO.setNumberOfDeletedAlerts(rs.getInt(1));
        }
    }

    /**
     * Sets Report Search filters.
     * 
     * @param alertsReportData
     * @param contractId
     * @param isContractSearch
     * @param stmt
     * @throws SQLException
     * @throws SystemException 
     */
    private static void setReportFilters(AlertsReportData alertsReportData, Integer contractId,
            boolean isContractSearch, PreparedStatement stmt) throws SQLException, SystemException {
    	Date fromDate = null;
    	Date toDate = null;
		try {
			fromDate = (Date) searchDateFormat.parse(alertsReportData.getFromDate().toString() + " 00:00:00.0");
			toDate = (Date) searchDateFormat.parse(alertsReportData.getToDate().toString() + " 23:59:59.66");
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
     * Sets Report Search filters.
     * 
     * @param alertsReportData
     * @param contractId
     * @param isContractSearch
     * @param stmt
     * @throws SQLException
     * @throws SystemException 
     */
	private static void setReportFiltersData(AlertsReportData alertsReportData,
			Integer contractId, boolean isContractSearch, PreparedStatement stmt)
			throws SQLException, SystemException {
		Date fromDate = null;
		Date toDate = null;
		int parm = 1;
		try {
			fromDate = (Date) searchDateFormat.parse(alertsReportData.getFromDate().toString() + " 00:00:00.0");
			toDate = (Date) searchDateFormat.parse(alertsReportData.getToDate().toString() + " 23:59:59.66");
		} catch (ParseException e) {
			throw new SystemException(
					"Error while parsing fromDate and toDate" + e.getMessage());
		}
		Timestamp fromTimestamp = new Timestamp(fromDate.getTime());
		Timestamp toTimestamp = new Timestamp(toDate.getTime());
		stmt.setTimestamp(parm++, fromTimestamp);
		stmt.setTimestamp(parm++, toTimestamp);
		if (isContractSearch) {
			stmt.setInt(parm++, contractId.intValue());
		}
		stmt.setTimestamp(parm++, fromTimestamp);
		stmt.setTimestamp(parm++, toTimestamp);
		if (isContractSearch) {
			stmt.setInt(parm++, contractId.intValue());
		}
		stmt.setTimestamp(parm++, fromTimestamp);
		stmt.setTimestamp(parm++, toTimestamp);
		if (isContractSearch) {
			stmt.setInt(parm++, contractId.intValue());
		}
	}

    /**
     * Sets report search filters for User distribution by limiting the time span range between from
     * and to date to one year.
     * 
     * 
     * @param alertsReportData
     * @param contractId
     * @param isContractSearch
     * @param stmt
     * @param freqCode
     * @throws SQLException
     */
    private static void setReportFiltersForAlertDistribution(AlertsReportData alertsReportData,
            Integer contractId, boolean isContractSearch, PreparedStatement stmt, String freqCode)
            throws SQLException {

        int paramNumber = 1;
        stmt.setDate(paramNumber++, convertSQLDate(alertsReportData.getToDate()));
        stmt.setString(paramNumber++, freqCode);
        if (isContractSearch) {
            stmt.setInt(paramNumber++, contractId.intValue());
        }
    }

    /**
     * Returns adjusted fromDate, if the difference between fromDate and toDate is more than one
     * year than adjust it to max one year difference.
     * 
     * @param fromDate
     * @param toDate
     * @return adjusted fromDate
     */
    private static Date getAdjustedFromDate(Date fromDate, Date toDate) {
        Calendar fromDateCal = GregorianCalendar.getInstance();
        fromDateCal.setTime(fromDate);
        Calendar adjustedFromDateCal = GregorianCalendar.getInstance();
        adjustedFromDateCal.setTime(toDate);
        adjustedFromDateCal.add(Calendar.YEAR, -1);
        adjustedFromDateCal.add(Calendar.DAY_OF_MONTH, -1);
        if (fromDateCal.before(adjustedFromDateCal)) {
            return adjustedFromDateCal.getTime();
        } else {
            return fromDate;
        }
    }

    /**
     * Gets the Alert Frequency Statistics based on criteria.
     * 
     * @param connection
     * @param stmt
     * @param criteria
     * @param alertsReportData
     * @param contractId
     * @param isContractSearch
     * @throws SystemException
     * @throws ReportServiceException
     */
    private static void getAlertFrequencyStats(Connection connection, PreparedStatement stmt,
            ReportCriteria criteria, AlertsReportData alertsReportData, Integer contractId,
            boolean isContractSearch) throws SQLException, SystemException {

        ResultSet rs = null;
        String query = MessageFormat.format(SQL_SELECT_NUMBER_OF_ALERT_FREQUENCY,
                getContractClausePlain(isContractSearch));
        stmt = connection.prepareStatement(query);
        setReportFilters(alertsReportData, contractId, isContractSearch, stmt);
        rs = stmt.executeQuery();
        if (rs != null) {
            List<AlertsReportFreqVO> freqList = getAlertFreqStatsList();
            while (rs.next()) {
                String freqCode = rs.getString(1);
                String freqName = alertFrequencyCodeNameMap.get(freqCode);
                if (freqName != null) {
                    getFrequencyData(rs, freqList, freqName);
                }
            }
            addupFreqData(freqList);
            alertsReportData.setAlertFrequencyStatsList(freqList);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getAlertFrequencyStats");
        }
    }

    /**
     * Gets the Alert Distribution by month.
     * 
     * @param stmt2
     * @param connection2
     * 
     * @param criteria
     * @param isContractSearch2
     * @param contractId2
     * @throws SystemException 
     * @throws SystemException
     * @throws ReportServiceException
     */
    private static void getAlertsDistribution(Connection connection, PreparedStatement stmt,
            ReportCriteria criteria, AlertsReportData alertsReportData, Integer contractId,
            boolean isContractSearch) throws SQLException, SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getAlertsDistribution");
        }

        List<AlertsReportDistributionVO> dList = getMonthlyDistributionStatsList(alertsReportData);
        String query = SQL_SELECT_NOTICE_DISTRIBUTION_BY_DUE_DATE
                + getContractClausePlain(isContractSearch);
        String[] freqCodes = { AlertsReportData.MONTHLY_FREQUENCY_CODE,
                AlertsReportData.QUARTERLY_FREQUENCY_CODE,
                AlertsReportData.SEMI_ANNUALLY_FREQUENCY_CODE,
                AlertsReportData.ANNUALLY_FREQUENCY_CODE };
        stmt = connection.prepareStatement(query);
        for (String freqCode : freqCodes) {
        	getNumberOfAlertsForSpecifiedFrequency(connection, stmt, alertsReportData, isContractSearch,
                    contractId, dList, freqCode);
        }

        getNumberOfAlertsForAdhocFrequency(connection, stmt, alertsReportData, isContractSearch,
                contractId, dList);
        
        alertsReportData.setAlertMonthlyDistributionList(dList);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getAlertsDistribution");
        }
    }

    
    /**
     * Retrieves number of alerts for Specified frequency and adds the repeating alerts for the current system year.
     * 
     * @param connection
     * @param stmt
     * @param alertsReportData
     * @param isContractSearch
     * @param contractId
     * @param dList
     * @param fromDate
     * @throws SystemException 
     */
    private static void getNumberOfAlertsForSpecifiedFrequency(Connection connection,
            PreparedStatement stmt, AlertsReportData alertsReportData, boolean isContractSearch,
            Integer contractId, List<AlertsReportDistributionVO> dList, String frequency)
            throws SQLException, SystemException {
        String query = MessageFormat.format(
                replaceSingleQuote(SQL_SELECT_ALERTS_FOR_CURRENT_YEAR_WITH_FREQUENCY_FILTERED),
                getContractClausePlain(isContractSearch));
        stmt = connection.prepareStatement(query);
        stmt.setString(1, frequency);
        Calendar calForYear = Calendar.getInstance();
        try {
			Date toDate = (Date) searchDateFormat.parse(alertsReportData.getToDate().toString() + " 23:59:59.66");
			stmt.setTimestamp(2, new Timestamp(toDate.getTime()));
			calForYear.setTime(toDate);
		} catch (ParseException e1) {
			throw new SystemException(
					"Error while parsing fromDate and toDate" + e1.getMessage());
		}
		
        if (isContractSearch) {
            stmt.setInt(3, contractId.intValue());
        }
        ResultSet rs = stmt.executeQuery();
        if (rs != null) {
            while (rs.next()) {
            	int month = rs.getInt(1);
            	int year = rs.getInt(2);
            	int count = rs.getInt(3);
                for (AlertsReportDistributionVO vo : dList) {
                	Calendar cal = Calendar.getInstance();
                	try {
						cal.setTime(new SimpleDateFormat("MMM").parse(vo.getMonth()));
					} catch (ParseException e) {
					}
					if(calForYear.get(Calendar.YEAR) == year){
						if(AlertsReportData.MONTHLY_FREQUENCY_CODE.equals(frequency)){
		                	int monthInt = cal.get(Calendar.MONTH) + 2;
		                    if ( monthInt > month) {
		                        vo.setNumberOfAlerts(vo.getNumberOfAlerts() + count);
		                    }
						}
						if(AlertsReportData.QUARTERLY_FREQUENCY_CODE.equals(frequency)){
							int monthInt = cal.get(Calendar.MONTH) + 1;
		                    if ( monthInt == month || monthInt == month + 3 || monthInt == month + 6 || monthInt == month + 9 ) {
		                        vo.setNumberOfAlerts(vo.getNumberOfAlerts() + count);
		                    }
						}
						if(AlertsReportData.SEMI_ANNUALLY_FREQUENCY_CODE.equals(frequency)){
							int monthInt = cal.get(Calendar.MONTH) + 1;
		                    if ( monthInt == month || monthInt == month + 6) {
		                        vo.setNumberOfAlerts(vo.getNumberOfAlerts() + count);
		                    }
						}
						if(AlertsReportData.ANNUALLY_FREQUENCY_CODE.equals(frequency)){
							int monthInt = cal.get(Calendar.MONTH) + 1;
		                    if ( monthInt == month ) {
		                        vo.setNumberOfAlerts(vo.getNumberOfAlerts() + count);
		                    }
						}
					}
					else {
						if(AlertsReportData.MONTHLY_FREQUENCY_CODE.equals(frequency)){
		                        vo.setNumberOfAlerts(vo.getNumberOfAlerts() + count);
						}
						int monthInt = cal.get(Calendar.MONTH) + 1;
						if(AlertsReportData.QUARTERLY_FREQUENCY_CODE.equals(frequency)){
							if(Arrays.binarySearch(getMonths(month, AlertsReportData.QUARTERLY_FREQUENCY_CODE), monthInt) >= 0){
								vo.setNumberOfAlerts(vo.getNumberOfAlerts() + count);
							}
						}
						if(AlertsReportData.SEMI_ANNUALLY_FREQUENCY_CODE.equals(frequency)){
							if(Arrays.binarySearch(getMonths(month, AlertsReportData.SEMI_ANNUALLY_FREQUENCY_CODE), monthInt) >= 0){
			                        vo.setNumberOfAlerts(vo.getNumberOfAlerts() + count);
							}
						}
						if(AlertsReportData.ANNUALLY_FREQUENCY_CODE.equals(frequency)){
		                    if ( monthInt == month ) {
		                        vo.setNumberOfAlerts(vo.getNumberOfAlerts() + count);
		                    }
						}
					}
                }
            }
        }
    }
    
    private static Integer[] getMonths(int month, String frequency ){
    	
    	Integer[] monthsList = {0,0,0,0};
    	if(AlertsReportData.QUARTERLY_FREQUENCY_CODE.equals(frequency)){
    		if(month > 3 ){
    			month = month % 3;
    			if(month == 0){
    				month = 3;
    			}
    		}
    		monthsList[0] = month;
    		monthsList[1] = month + 3;
    		monthsList[2] = month + 6;
    		monthsList[3] = month + 9;
    	}
    	if(AlertsReportData.SEMI_ANNUALLY_FREQUENCY_CODE.equals(frequency)){
    		if(month > 6 ){
    			month = month % 6;
    			if(month == 0){
    				month = 6;
    			}
    		}
    		monthsList[0] = month;
    		monthsList[1] = month + 6;
    	}
    	return monthsList;
    }
    
    /**
     * Retrieves number of alerts for Specified frequency and adds the repeating alerts for the current system year.
     * 
     * @param connection
     * @param stmt
     * @param alertsReportData
     * @param isContractSearch
     * @param contractId
     * @param dList
     * @param fromDate
     * @throws SystemException 
     */
    private static void getNumberOfAlertsForAdhocFrequency(Connection connection,
            PreparedStatement stmt, AlertsReportData alertsReportData, boolean isContractSearch,
            Integer contractId, List<AlertsReportDistributionVO> dList)
            throws SQLException, SystemException {
        String query = MessageFormat.format(
                replaceSingleQuote(SQL_SELECT_ALERTS_FOR_CURRENT_YEAR_WITH_ADHOC_FREQUENCY_FILTERED),
                getContractClausePlain(isContractSearch));
        stmt = connection.prepareStatement(query);
        stmt.setString(1, AlertsReportData.ADHOC_FREQUENCY_CODE);
        try {
			Date toDate = (Date) searchDateFormat.parse(alertsReportData.getToDate().toString() + " 23:59:59.66");
			stmt.setTimestamp(2, new Timestamp(toDate.getTime()));
		} catch (ParseException e1) {
			throw new SystemException(
					"Error while parsing fromDate and toDate" + e1.getMessage());
		}
        if (isContractSearch) {
            stmt.setInt(3, contractId.intValue());
        }
        ResultSet rs = stmt.executeQuery();
        if (rs != null) {
            while (rs.next()) {
            	int month = rs.getInt(1);
            	int count = rs.getInt(2);
                for (AlertsReportDistributionVO vo : dList) {
                	Calendar cal = Calendar.getInstance();
                	try {
						cal.setTime(new SimpleDateFormat("MMM").parse(vo.getMonth()));
					} catch (ParseException e) {
					}
					int monthInt = cal.get(Calendar.MONTH) + 1;
                    if ( monthInt == month ) {
                        vo.setNumberOfAlerts(vo.getNumberOfAlerts() + count);
                    }
                }
            }
        }
    }



    /**
     * Retrieves number of alerts for adhoc frequency.
     * 
     * @param connection
     * @param stmt
     * @param alertsReportData
     * @param isContractSearch
     * @param contractId
     * @param dList
     * @param fromDate
     */
    private static void getNumberOfAlertsForAdhocFrequency(Connection connection,
            PreparedStatement stmt, AlertsReportData alertsReportData, boolean isContractSearch,
            Integer contractId, List<AlertsReportDistributionVO> dList, Date fromDate)
            throws SQLException {
        String query = MessageFormat.format(
                replaceSingleQuote(SQL_SELECT_NOTICE_DISTRIBUTION_BY_DUE_DATE_ADHOC),
                getContractClausePlain(isContractSearch));
        stmt = connection.prepareStatement(query);
        setReportFiltersForAlertDistributionAdhoc(alertsReportData, contractId, isContractSearch,
                stmt, fromDate);
        ResultSet rs = stmt.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                int month = rs.getInt(1);
                int alertsCount = rs.getInt(2);
                incrementMonthAlertCount(month-1, dList, alertsCount);
            }
        }
    }

    /**
     * Retrieves the alerts distribution for a given due date.
     * 
     * @param dList
     * @param dueDate
     * @param fromDate
     * @param toDate
     * @param freqCode
     */
    private static void getAlertsDistributionByDueDate(List<AlertsReportDistributionVO> dList,
            Date dueDate, Date fromDate, Date toDate, String freqCode) {

        Integer monthlyIncrement = alertFrequencyCodeMonthsMap.get(freqCode);
        if (monthlyIncrement == null) {
            return;
        }

        Calendar dueCalendar = Calendar.getInstance();
        dueCalendar.setTime(dueDate);

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(fromDate);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(toDate);

        while (!dueCalendar.after(toCalendar)) {
            if (!dueCalendar.before(fromCalendar)) {
                incrementMonthAlertCount(dueCalendar.get(Calendar.MONTH), dList, 1);
            }
            dueCalendar.add(Calendar.MONTH, monthlyIncrement);
        }

    }

    /**
     * Increments Distribution Month Alert Count.
     * 
     * @param month
     * @param dList
     */
    private static void incrementMonthAlertCount(int month, List<AlertsReportDistributionVO> dList,
            int alertsCount) {
        for (AlertsReportDistributionVO vo : dList) {
            if (vo.getMonth().equals(MONTH_NAMES[month])) {
                vo.setNumberOfAlerts(vo.getNumberOfAlerts() + alertsCount);
            }
        }
    }

    /**
     * Returns list of user distribution numbers.
     * 
     * @return list
     * @throws SystemException 
     */
    private static List<AlertsReportDistributionVO> getMonthlyDistributionStatsList(AlertsReportData alertsReportData) throws SystemException {

        List<AlertsReportDistributionVO> list = new ArrayList<AlertsReportDistributionVO>();
        Date toDate= null;
        try {
        	toDate = (Date) searchDateFormat.parse(alertsReportData.getToDate().toString() + " 23:59:59.66");
		} catch (ParseException e1) {
			throw new SystemException(
					"Error while parsing fromDate and toDate" + e1.getMessage());
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate);
        for (int i = 0; i < 3; i++) {
            for (int j = i; j < 12; j += 3) {
                list.add(new AlertsReportDistributionVO(MONTH_NAMES[j],cal.get(Calendar.YEAR)));
            }
        }
        return list;
    }

    /**
     * Adds up user data to generate total numbers.
     * 
     * @param list
     */
    private static void addupUserData(List<AlertsReportUserStatsVO> list) {
        int sumNumberOfAlertUsers = 0, sumTotalAlertSetUp = 0;
        int sumUrgentAlerts = 0, sumNormalAlerts = 0, numberOfDeletedAlerts = 0;
        for (AlertsReportUserStatsVO vo : list) {
            sumNumberOfAlertUsers += vo.getNumberOfAlertUsers();
            sumTotalAlertSetUp += vo.getTotalAlertSetUp();
            sumUrgentAlerts += vo.getUrgentAlerts();
            sumNormalAlerts += vo.getNormalAlerts();
            numberOfDeletedAlerts += vo.getNumberOfDeletedAlerts();
        }

        AlertsReportUserStatsVO totalVO = new AlertsReportUserStatsVO();
        totalVO.setUsers(TOTALS);
        totalVO.setNumberOfAlertUsers(sumNumberOfAlertUsers);
        BigDecimal avgNumAlertsPerUser = (new BigDecimal(0)).setScale(ZERO_PRECISION);
        if (sumNumberOfAlertUsers > 0) {
            avgNumAlertsPerUser = (new BigDecimal(sumTotalAlertSetUp))
                    .divide(new BigDecimal(sumNumberOfAlertUsers), DIVIDEND_PRECISION,
                            RoundingMode.HALF_UP).setScale(AMOUNT_PRECISION,
                            BigDecimal.ROUND_HALF_EVEN);
        }

        totalVO.setAverageNumberOfAlertsPerUser(avgNumAlertsPerUser);
        totalVO.setUrgentAlerts(sumUrgentAlerts);
        totalVO.setNormalAlerts(sumNormalAlerts);
        totalVO.setTotalAlertSetUp(sumTotalAlertSetUp);
        totalVO.setNumberOfDeletedAlerts(numberOfDeletedAlerts);
        list.add(totalVO);
    }

    /**
     * Adds up Alert Frequency data
     * 
     * @param list
     */
    private static void addupFreqData(List<AlertsReportFreqVO> list) {
        int sumNumberOfAlertFrequencies = 0;
        for (AlertsReportFreqVO vo : list) {
            if (vo.getNumberOfAlertsPerFrequency() != null) {
                sumNumberOfAlertFrequencies += vo.getNumberOfAlertsPerFrequency();
            }
        }
        if (sumNumberOfAlertFrequencies > 0) {
            for (AlertsReportFreqVO vo : list) {
                BigDecimal alertFreqPercentage = new BigDecimal(
                        vo.getNumberOfAlertsPerFrequency() != null ? vo
                                .getNumberOfAlertsPerFrequency().intValue() : 0)
                        .divide(new BigDecimal(sumNumberOfAlertFrequencies), DIVIDEND_PRECISION,
                                RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                        .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
                vo.setAlertFrequencyPercentage(alertFreqPercentage);
            }
        }
    }

    /**
     * Returns the list for alert user statistics.
     * 
     * @return list of alert user statistics VO.
     */
    private static List<AlertsReportUserStatsVO> getAlertUserStatsList() {
        List<AlertsReportUserStatsVO> list = new ArrayList<AlertsReportUserStatsVO>();
        list.add(new AlertsReportUserStatsVO(PLAN_SPONSOR));
        list.add(new AlertsReportUserStatsVO(TPA));
        list.add(new AlertsReportUserStatsVO(INTERMEDIARY_CONTACT));
        list.add(new AlertsReportUserStatsVO(TOTAL_CARE));
        return list;
    }

    /**
     * Returns the list for alert frequency statistics.
     * 
     * @return list of alert frequency VO.
     */
    private static List<AlertsReportFreqVO> getAlertFreqStatsList() {
        List<AlertsReportFreqVO> list = new ArrayList<AlertsReportFreqVO>();
        list.add(new AlertsReportFreqVO(AlertsReportData.MONTHLY_FREQUENCY));
        list.add(new AlertsReportFreqVO(AlertsReportData.QUARTERLY_FREQUENCY));
        list.add(new AlertsReportFreqVO(AlertsReportData.SEMI_ANNUALLY_FREQUENCY));
        list.add(new AlertsReportFreqVO(AlertsReportData.ANNUALLY_FREQUENCY));
        list.add(new AlertsReportFreqVO(AlertsReportData.ADHOC_FREQUENCY));
        return list;
    }

    /**
     * Gets the user data from ResultSet for specific user category.
     * 
     * @param rs
     * @param list
     * @param userCategory
     * @throws SQLException
     */
    private static void getUserData(ResultSet rs, List<AlertsReportUserStatsVO> list,
            String userCategory) throws SQLException {

        AlertsReportUserStatsVO userVO = getUserVO(list, userCategory);
        int paramNumber = 1;

        int urgentAlerts = rs.getInt(paramNumber++);
        int normalAlerts = rs.getInt(paramNumber++);
        userVO.setNumberOfAlertUsers(userVO.getNumberOfAlertUsers() + rs.getInt(paramNumber++));
        int numberOfAlerts = rs.getInt(paramNumber++);
        userVO.setTotalAlertSetUp(userVO.getTotalAlertSetUp() + numberOfAlerts);

		if (urgentAlerts > 0) {
			userVO.setUrgentAlerts(userVO.getUrgentAlerts() + urgentAlerts);
		}
		if (normalAlerts > 0) {
			userVO.setNormalAlerts(userVO.getNormalAlerts() + normalAlerts);
		}
        if (userVO.getNumberOfAlertUsers() > 0) {
            BigDecimal avgNumAlertsPerUser = (new BigDecimal(userVO.getTotalAlertSetUp())).divide(
                    new BigDecimal(userVO.getNumberOfAlertUsers()), DIVIDEND_PRECISION,
                    RoundingMode.HALF_UP).setScale(AMOUNT_PRECISION, BigDecimal.ROUND_HALF_EVEN);
            userVO.setAverageNumberOfAlertsPerUser(avgNumAlertsPerUser);
        }

    }

    /**
     * Returns the corresponding VO for given user category.
     * 
     * @param list
     * @param userCategory
     * @return AlertsReportUserStatsVO
     */
    private static AlertsReportUserStatsVO getUserVO(List<AlertsReportUserStatsVO> list,
            String userCategory) {
        AlertsReportUserStatsVO userVO = null;

        for (AlertsReportUserStatsVO vo : list) {
            if (vo.getUsers().equals(userCategory)) {
                userVO = vo;
            }
        }

        if (userVO == null) {
            userVO = new AlertsReportUserStatsVO();
            userVO.setUsers(userCategory);
            list.add(userVO);
        }
        return userVO;
    }

    /**
     * Retrieves the frequency data from result set for specified frequency Name.
     * 
     * @param rs
     * @param list
     * @param freqName
     * @throws SQLException
     */
    private static void getFrequencyData(ResultSet rs, List<AlertsReportFreqVO> list,
            String freqName) throws SQLException {
        AlertsReportFreqVO freqVO = null;
        for (AlertsReportFreqVO vo : list) {
            if (vo.getFrequency().equals(freqName)) {
                freqVO = vo;
                break;
            }
        }
        if (freqVO == null) {
            freqVO = new AlertsReportFreqVO(freqName);
            list.add(freqVO);
        }
        freqVO.setNumberOfAlertsPerFrequency(rs.getInt(2));
    }

    /**
     * Retrieves the number of alerts by the given frequency. Frequency is being set as parameter to
     * improve performance of query.
     * 
     * @param stmt
     * @param alertsReportData
     * @param isContractSearch
     * @param contractId
     * @param dList
     * @param freqCode
     * @param fromDate
     * @throws SQLException
     */
    private static void getNumberOfAlertsByFrequency(PreparedStatement stmt,
            AlertsReportData alertsReportData, boolean isContractSearch, Integer contractId,
            List<AlertsReportDistributionVO> dList, String freqCode, Date fromDate)
            throws SQLException {
        setReportFiltersForAlertDistribution(alertsReportData, contractId, isContractSearch, stmt,
                freqCode);
        ResultSet rs = stmt.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                Date startDate = rs.getDate(1);
                getAlertsDistributionByDueDate(dList, startDate, fromDate,
                        alertsReportData.getToDate(), freqCode);
            }
        }
    }

    /**
     * Returns the SQL clause if contract filter is active.
     * 
     * @param isContractSearch
     * @return SQL clause
     */
    private static String getContractClause(boolean isContractSearch) {
        String clause = "";
        if (isContractSearch) {
            clause = SQL_CONTRACT_FILTER_CLAUSE;
        }
        return clause;
    }

    /**
     * Sets the months that are in search date range.
     * 
     * @param dList
     * @param fromDate
     * @param toDate
     */
    private static void setMonthsInSearchRange(List<AlertsReportDistributionVO> dList,
            Date fromDate, Date toDate) {
        Calendar fromDateCal = Calendar.getInstance();
        fromDateCal.setTime(fromDate);
        Calendar toDateCal = Calendar.getInstance();
        toDateCal.setTime(toDate);
        while (!fromDateCal.after(toDateCal)) {
            int month = fromDateCal.get(Calendar.MONTH);
            for (AlertsReportDistributionVO vo : dList) {
                if (vo.getMonth().equals(MONTH_NAMES[month])) {
                    vo.setMonthInSearchRange(true);
                }
            }
            fromDateCal.add(Calendar.MONTH, 1);
        }
    }

    /**
     * Sets report filters for retrieving alerts distribution for adhoc frequency.
     * 
     * @param alertsReportData
     * @param contractId
     * @param isContractSearch
     * @param stmt
     * @param fromDate
     */
    private static void setReportFiltersForAlertDistributionAdhoc(
            AlertsReportData alertsReportData, Integer contractId, boolean isContractSearch,
            PreparedStatement stmt, Date fromDate) throws SQLException {
        stmt.setDate(1, convertSQLDate(fromDate));
        stmt.setDate(2, convertSQLDate(alertsReportData.getToDate()));
        if (isContractSearch) {
            stmt.setInt(3, contractId.intValue());
        }
    }

    /**
     * Initialize the alert frequency code and name map.
     * 
     * @return map of alert frequency code with corresponding frquency name.
     */
    private static Map<String, String> initAlertFreqMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(AlertsReportData.MONTHLY_FREQUENCY_CODE, AlertsReportData.MONTHLY_FREQUENCY);
        map.put(AlertsReportData.QUARTERLY_FREQUENCY_CODE, AlertsReportData.QUARTERLY_FREQUENCY);
        map.put(AlertsReportData.SEMI_ANNUALLY_FREQUENCY_CODE,
                AlertsReportData.SEMI_ANNUALLY_FREQUENCY);
        map.put(AlertsReportData.ANNUALLY_FREQUENCY_CODE, AlertsReportData.ANNUALLY_FREQUENCY);
        map.put(AlertsReportData.ADHOC_FREQUENCY_CODE, AlertsReportData.ADHOC_FREQUENCY);
        return Collections.unmodifiableMap(map);
    }

    /**
     * Initialize the alert frequency code and months map.
     * 
     * @return map of alert frequency code with corresponding number of months.
     */
    private static Map<String, Integer> initAlertFreqMonthsMap() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put(AlertsReportData.MONTHLY_FREQUENCY_CODE, new Integer(1));
        map.put(AlertsReportData.QUARTERLY_FREQUENCY_CODE, new Integer(3));
        map.put(AlertsReportData.SEMI_ANNUALLY_FREQUENCY_CODE, new Integer(6));
        map.put(AlertsReportData.ANNUALLY_FREQUENCY_CODE, new Integer(12));
        return Collections.unmodifiableMap(map);
    }
}
