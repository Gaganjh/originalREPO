package com.manulife.pension.ps.service.report.noticereports.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.manulife.pension.ps.service.report.noticereports.valueobject.BuildYourPackageReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * Provide Data access services for Build Your Package report.
 * 
 */
public class BuildYourPackageReportDAO extends NoticeReportsBaseDAO {

    private static final String className = BuildYourPackageReportDAO.class.getName();
    
    public static FastDateFormat searchDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.S", Locale.US);

    private static final Logger logger = Logger.getLogger(BuildYourPackageReportDAO.class);

    private static final String CONTRACT_NOTICE_MAILING_ORDER_QUALIFIER = "CN.";

    private static final String WEBSITE_USER_ACTION_LOG_QUALIFIER = "WU.";

    private static final String SQL_SELECT_TOTAL_ORDERS_AND_CONTRACTS_MAILED = "SELECT  COUNT(*) AS TOTAL_ORDERS, COUNT(DISTINCT CONTRACT_ID) AS UNIQUE_CONTRACTS_MAILED "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER  "
            + "WHERE ORDER_STATUS_DATE BETWEEN ? AND ? ";

    private static final String SQL_SELECT_TOTAL_CONTRACTS_MAILED_AND_DOWNLOADED = "WITH CONTRACTS_MAILED_DOWNLOAD (CONTRACT_ID) AS (SELECT CONTRACT_ID AS CONTRACTS_MAILED_DOWNLOAD "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER  "
            + "WHERE ORDER_STATUS_DATE BETWEEN ? AND ? "
            + "{0} "
            + "UNION "
            + "SELECT CONTRACT_ID AS CONTRACTS_MAILED_DOWNLOAD "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG "
            + "WHERE USER_ACTION_NAME = ''"
            + PACKAGE_DOWNLOAD
            + "''  "
            + "AND CREATED_TS BETWEEN ? AND ? "
            + "{1} ) "
            + "SELECT COUNT(DISTINCT CONTRACT_ID) AS TOTAL_CONTRACTS FROM CONTRACTS_MAILED_DOWNLOAD";

    private static final String SQL_SELECT_REPEAT_CONTRACTS_MAILED_AND_DOWNLOADED_PACKAGE = "WITH REPEAT_CONTRACTS(CONTRACT_ID, NUMBER_OF_CONTRACTS) AS "
            + "(SELECT CONTRACT_ID, COUNT(*) AS REPEAT_ORDERS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER  "
            + "WHERE ORDER_STATUS_DATE BETWEEN ? AND  ?    "
            + "{0} "
            + "GROUP BY CONTRACT_ID "
            + "HAVING COUNT(*) > 1 "
            + "UNION "
            + "SELECT CONTRACT_ID, COUNT(*) AS REPEAT_ORDERS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG "
            + "WHERE USER_ACTION_NAME = ''"
            + PACKAGE_DOWNLOAD
            + "'' "
            + "AND CREATED_TS BETWEEN ? AND ?  "
            + "{1} "
            + "GROUP BY CONTRACT_ID "
            + "HAVING COUNT(*) > 1) "
            + "SELECT  COUNT(DISTINCT CONTRACT_ID) AS REPEAT_CONTRACTS_COUNT "
            + "FROM REPEAT_CONTRACTS ";

    private static final String SQL_SELECT_MONTH_WITH_MOST_MAILINGS = "WITH ORDER_COUNT(ORDER_YEAR, ORDER_MONTH, ORDER_MONTHLY_COUNT)  "
            + "AS (SELECT YEAR(ORDER_STATUS_DATE), MONTH(ORDER_STATUS_DATE), COUNT(*) "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER  "
            + "WHERE ORDER_STATUS_DATE BETWEEN ? AND  ?  "
            + "{0} "
            + "GROUP BY YEAR(ORDER_STATUS_DATE), MONTH(ORDER_STATUS_DATE)) "
            + "SELECT OC.ORDER_YEAR, OC.ORDER_MONTH, OC.ORDER_MONTHLY_COUNT "
            + "FROM ORDER_COUNT OC "
            + "WHERE OC.ORDER_MONTHLY_COUNT = (SELECT MAX(ORDER_MONTHLY_COUNT) FROM ORDER_COUNT) ";

    private static final String SQL_SELECT_CONTRACTS_NEW_BUSINESS = "WITH CONTRACTS_NEW_BUSINESS (CONTRACT_ID) AS ( "
            + "SELECT CN.CONTRACT_ID AS NB_CONTRACTS " + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_CS C "
            + "WHERE CN.ORDER_STATUS_DATE BETWEEN ? AND ? "
            + "{0} "
            + "AND CN.CONTRACT_ID = C.CONTRACT_ID "
            + "AND C.CONTRACT_STATUS_CODE IN (''PS'', ''DC'', ''PC'', ''CA'') "
            + "UNION "
            + "SELECT WU.CONTRACT_ID AS NB_CONTRACTS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WU, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_CS C "
            + "WHERE WU.USER_ACTION_NAME = ''"
            + PACKAGE_DOWNLOAD
            + "'' "
            + "AND WU.CREATED_TS BETWEEN ? AND ? "
            + "{1} "
            + "AND WU.CONTRACT_ID = C.CONTRACT_ID "
            + "AND C.CONTRACT_STATUS_CODE IN (''PS'', ''DC'', ''PC'', ''CA'') "
            + ") "
            + "SELECT COUNT(DISTINCT CONTRACT_ID) AS NEW_BUSINESS_CONTRACTS FROM CONTRACTS_NEW_BUSINESS ";

    private static final String SQL_SELECT_CONTRACTS_INFORCE = "WITH CONTRACTS_INFORCE (CONTRACT_ID) AS ( "
            + "SELECT CN.CONTRACT_ID AS NB_CONTRACTS " + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_CS C "
            + "WHERE CN.ORDER_STATUS_DATE BETWEEN ? AND ? "
            + "{0} "
            + "AND CN.CONTRACT_ID = C.CONTRACT_ID "
            + "AND C.CONTRACT_STATUS_CODE IN (''AC'', ''CF'') "
            + "UNION "
            + "SELECT WU.CONTRACT_ID AS NB_CONTRACTS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WU, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_CS C "
            + "WHERE WU.USER_ACTION_NAME = ''"
            + PACKAGE_DOWNLOAD
            + "'' "
            + "AND WU.CREATED_TS BETWEEN ? AND ? "
            + "{1} "
            + "AND WU.CONTRACT_ID = C.CONTRACT_ID "
            + "AND C.CONTRACT_STATUS_CODE IN (''AC'', ''CF'') "
            + ") "
            + "SELECT COUNT(CONTRACT_ID) AS INFORCE_CONTRACTS FROM CONTRACTS_INFORCE ";

    private static final String SQL_SELECT_USER_PREFERENCE_DOWNLOAD = "SELECT COUNT(DISTINCT CONTRACT_ID) AS CONTRACTS_DOWNLOAD "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG "
            + "WHERE USER_ACTION_NAME = '"
            + PACKAGE_DOWNLOAD
            + "' "
            + "AND CREATED_TS BETWEEN ? AND ? ";

    private static final String SQL_SELECT_COMPLETED_ORDER = "SELECT  COUNT(*) AS COMPLETED_ORDERS, SUM(PAGE_COUNT) AS TOTAL_PAGES, "
            + " SUM(PARTICIPANT_COUNT) AS PARTICIPANT_COUNT,  "
            + "SUM(TOTAL_MAILING_COST_AMOUNT) AS TOTAL_COST "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER  "
            + "WHERE ORDER_STATUS_DATE BETWEEN ? AND ? "
            + "AND ORDER_STATUS_CODE = 'CM' ";

    private static final String SQL_SELECT_COMPLETED_ORDER_BLACK_AND_WHITE = "SELECT  COUNT(*) AS BLACK_AND_WHITE_ORDERS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER  "
            + "WHERE ORDER_STATUS_CODE = 'CM' "
            + "AND COLOR_PRINT_IND = 'B' "
            + "AND ORDER_STATUS_DATE BETWEEN ? AND  ? ";

    private static final String SQL_SELECT_COMPLETED_ORDER_COLOR = "SELECT  COUNT(*) AS COLOR_ORDERS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER  "
            + "WHERE ORDER_STATUS_CODE = 'CM' "
            + "AND COLOR_PRINT_IND = 'Y' "
            + "AND ORDER_STATUS_DATE BETWEEN ? AND  ? ";

    private static final String SQL_SELECT_JOHN_HANCOCK_COST = "SELECT SUM(TOTAL_MAILING_COST_AMOUNT) AS JH_COST  "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER  "
            + "WHERE ORDER_STATUS_CODE = 'CM' "
            + "AND VIP_ORDER_IND = 'Y' "
            + "AND ORDER_STATUS_DATE BETWEEN ? AND ? ";

    private static final String SQL_SELECT_JOHN_HANCOCK_AMOUNT_TOTAL_CARE_CONTRACTS = "SELECT  SUM(TOTAL_MAILING_COST_AMOUNT) AS TOTAL_COST "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_CS CS "
            + "WHERE CN.ORDER_STATUS_CODE = 'CM' "
            + "AND CN.CONTRACT_ID = CS.CONTRACT_ID "
            + "AND CS.THIRD_PARTY_ADMIN_ID = "
            + TOTAL_CARE_TPA_ADMIN_ID
            + " AND VIP_ORDER_IND = 'Y' "
            + "AND CN.ORDER_STATUS_DATE BETWEEN ? AND ? ";

    private static final String SQL_SELECT_PERCENTAGE_OF_COMPLETED_ORDERS_BY_PLAN_SPONSOR = "SELECT  COUNT(*) AS PLAN_SPONSOR_ORDERS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE CN.ORDER_STATUS_CODE = 'CM' "
            + "AND CN.CONTRACT_ID = UC.CONTRACT_ID "
            + "AND CN.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND UC.SECURITY_ROLE_CODE IN ('PSU', 'TRT', 'AUS', 'ADC') "
            + "AND CN.ORDER_STATUS_DATE BETWEEN ? AND ? ";

    private static final String SQL_SELECT_PERCENTAGE_OF_COMPLETED_ORDERS_BY_INTERMEDIARY_CONTACT = "SELECT COUNT(*) AS INTERMEDIARY_CONTACT_ORDERS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE CN.ORDER_STATUS_CODE = 'CM' "
            + "AND CN.CONTRACT_ID = UC.CONTRACT_ID "
            + "AND CN.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND UC.SECURITY_ROLE_CODE = 'INC' "
            + "AND CN.ORDER_STATUS_DATE BETWEEN ? AND  ?  ";

    private static final String SQL_SELECT_PERCENTAGE_OF_COMPLETED_ORDERS_BY_TPA = "WITH TOTAL_CARE_USERS (USER_PROFILE_ID) AS  "
            + "(SELECT USER_PROFILE_ID FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "EXTERNAL_USER_TPA_FIRM EU WHERE TPA_FIRM_ID = "
            + TOTAL_CARE_TPA_ADMIN_ID
            + " ) "
            + "SELECT  COUNT(*) AS TPA_ORDERS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_PROFILE UP   "
            + "WHERE CN.ORDER_STATUS_CODE = 'CM' "
            + "AND CN.USER_PROFILE_ID = UP.USER_PROFILE_ID "
            + "AND UP.PSW_DIRECTORY_ROLE_CODE  = 'TPA' "
            + "AND UP.USER_PROFILE_ID NOT IN (SELECT USER_PROFILE_ID FROM TOTAL_CARE_USERS) "
            + "AND CN.ORDER_STATUS_DATE BETWEEN ? AND  ? ";

    private static final String SQL_SELECT_PERCENTAGE_OF_COMPLETED_ORDERS_BY_TOTAL_CARE = "WITH TOTAL_CARE_USERS (USER_PROFILE_ID) AS  "
            + "(SELECT USER_PROFILE_ID FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "EXTERNAL_USER_TPA_FIRM EU WHERE TPA_FIRM_ID = "
            + TOTAL_CARE_TPA_ADMIN_ID
            + ") "
            + "SELECT  COUNT(*) AS TOTAL_CARE_ORDERS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_PROFILE UP   "
            + "WHERE CN.ORDER_STATUS_CODE = 'CM' "
            + "AND CN.USER_PROFILE_ID = UP.USER_PROFILE_ID "
            + "AND UP.PSW_DIRECTORY_ROLE_CODE  = 'TPA' "
            + "AND UP.USER_PROFILE_ID IN (SELECT USER_PROFILE_ID FROM TOTAL_CARE_USERS) "
            + "AND CN.ORDER_STATUS_DATE BETWEEN ? AND ? ";

    private static final String SQL_SELECT_NUMBER_ORDER_STAPLED = "SELECT  COUNT(*) AS STAPLED_ORDERS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER  "
            + "WHERE ORDER_STATUS_CODE = 'CM' "
            + "AND ORDER_STAPLED_IND = 'Y' "
            + "AND ORDER_STATUS_DATE BETWEEN ? AND ? ";

    private static final String SQL_SELECT_NUMBER_ORDERS_BOOKLET_ENVELOPE = "SELECT COUNT(*) AS LARGE_ENVELOPE_ORDERS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER  "
            + "WHERE ORDER_STATUS_CODE = 'CM' "
            + "AND LARGE_ENVELOPE_IND = 'Y' "
            + "AND ORDER_STATUS_DATE BETWEEN ? AND ? ";

    private static final String SQL_SELECT_NUMBER_POSTAGE_ORDERS = "SELECT  COUNT(*) AS POSTAGE_ORDERS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER  "
            + "WHERE ORDER_STATUS_CODE = 'CM' "
            + "AND BULK_ORDER_IND <> 'Y' "
            + "AND ORDER_STATUS_DATE BETWEEN ? AND ? ";

    private static final String SQL_SELECT_NUMBER_BULK_ORDERS = "SELECT COUNT(*) AS BULK_ORDERS "
            + "FROM " + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_MAILING_ORDER  "
            + "WHERE ORDER_STATUS_CODE = 'CM' " + "AND BULK_ORDER_IND = 'Y' "
            + "AND ORDER_STATUS_DATE BETWEEN ? AND ? ";

    private static final String SQL_SELECT_NUMBER_SEALED_ORDERS = "SELECT COUNT(*) AS SEALED_ORDERS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER  "
            + "WHERE ORDER_STATUS_CODE = 'CM' "
            + "AND BULK_ORDER_IND = 'Y' "
            + "AND ORDER_SEALED_IND = 'Y' " + "AND ORDER_STATUS_DATE BETWEEN ? AND ? ";
    
    private static final String SQL_SELECT_DOWNLOADED_SELECTED_COUNT = "SELECT COUNT (CONTRACT_ID) "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG "
            + "WHERE USER_ACTION_NAME = ''"
            + PACKAGE_DOWNLOAD
            + "''  "
            + "AND CREATED_TS BETWEEN ? AND ?  {0}";

    /**
     * Gets Report Data for Plan Sponsor Website.
     * 
     * @param criteria
     * @param buildPackageReportData
     * @throws SystemException
     * @throws ReportServiceException
     */
    public static void getReportData(ReportCriteria criteria,
            BuildYourPackageReportData buildPackageReportData) throws SystemException,
            ReportServiceException {

        Integer contractId = getContractNumber(criteria);
        boolean isContractSearch = (contractId != null && contractId.intValue() > 0);
        Connection connection = null;
        PreparedStatement stmt = null;

        try {

            connection = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);

            getUserAndContractStats(connection, stmt, criteria, buildPackageReportData, contractId,
                    isContractSearch);
            getOrderStatistics(connection, stmt, criteria, buildPackageReportData, contractId,
                    isContractSearch);
            getPercentagesOfCompletedOrdersByUserCategory(connection, stmt, criteria,
                    buildPackageReportData, contractId, isContractSearch);

        } catch (SQLException e) {
            handleSqlException(e, className, "getReportData", MessageFormat.format(
                    GET_REPORT_DATA_ERROR_MESSAGE, BuildYourPackageReportData.REPORT_NAME,
                    getContractNumber(criteria), getFromDate(criteria), getToDate(criteria)));

        } finally {
            close(stmt, connection);
        }

    }

    /**
     * Gets the statistics for contracts and users.
     * 
     * @param connection
     * @param stmt
     * @param criteria
     * @param buildPackageReportData
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getUserAndContractStats(Connection connection, PreparedStatement stmt,
            ReportCriteria criteria, BuildYourPackageReportData buildPackageReportData,
            Integer contractId, boolean isContractSearch) throws SQLException, SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getUserAndContractStats");
        }

        // Total Orders and Contracts.
        getTotalOrdersAndContracts(buildPackageReportData, connection, stmt, contractId,
                isContractSearch);

        // Number of repeat contracts using both Download and / or mail service
        getRepeatContracts(buildPackageReportData, connection, stmt, contractId, isContractSearch);

        // Month with most mailings
        getMonthWithMostMailings(buildPackageReportData, connection, stmt, contractId,
                isContractSearch);

        // Number of unique New Business contracts and its percentage that have at least one
        // order placed or download
        getNewBusinessContracts(buildPackageReportData, connection, stmt, contractId,
                isContractSearch);

        // Number of unique inforce contracts and its percentage that have at least one order
        // placed or download
        getInforceContracts(buildPackageReportData, connection, stmt, contractId, isContractSearch);

        // User Preference – Mail % and Download %
        getUserPreferencePercentages(buildPackageReportData, connection, stmt, contractId,
                isContractSearch);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getUserAndContractStats");
        }

    }

    /**
     * Gets the statistics for orders.
     * 
     * @param connection
     * @param stmt
     * @param criteria
     * @param buildPackageReportData
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getOrderStatistics(Connection connection, PreparedStatement stmt,
            ReportCriteria criteria, BuildYourPackageReportData buildPackageReportData,
            Integer contractId, boolean isContractSearch) throws SQLException, SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getOrderStatistics");
        }

        // Total Number of Completed Orders, page counts, participant count, total Order Cost
        // Average number of pages per order, Average number of participants per order
        getCompletedOrdersStats(buildPackageReportData, connection, stmt, contractId,
                isContractSearch);

        // Completed Orders in black and White %
        getCompletedOrdersBlackAndWhite(buildPackageReportData, connection, stmt, contractId,
                isContractSearch);

        // Completed Orders in color % CR 6, no need for colors orders.
        getCompletedOrdersColor(buildPackageReportData, connection, stmt, contractId,
                isContractSearch);

        // Orders Stapled
        buildPackageReportData.setOrderStapledPercentage(getOrdersPercentage(
                buildPackageReportData, connection, stmt, contractId, isContractSearch,
                SQL_SELECT_NUMBER_ORDER_STAPLED));

        // Orders using booklet envelope
        buildPackageReportData.setBookletOrdersPercentage(getOrdersPercentage(
                buildPackageReportData, connection, stmt, contractId, isContractSearch,
                SQL_SELECT_NUMBER_ORDERS_BOOKLET_ENVELOPE));

        // number of completed orders which did not use bulk mail option
        buildPackageReportData.setNumberOfPostageOrders(getOrders(buildPackageReportData,
                connection, stmt, contractId, isContractSearch, SQL_SELECT_NUMBER_POSTAGE_ORDERS));

        // number of completed orders which use bulk mail option
        buildPackageReportData.setNumberOfBulkOrders(getOrders(buildPackageReportData, connection,
                stmt, contractId, isContractSearch, SQL_SELECT_NUMBER_BULK_ORDERS));

        // percentage of bulk orders that had sealed option.
        getSealedOrdersPercentage(buildPackageReportData, connection, stmt, contractId,
                isContractSearch);

        // Amount Paid by John Hancock for VIP orders
        getAmountPaidByJHforVipOrders(buildPackageReportData, connection, stmt, contractId,
                isContractSearch);

        // Total cost of all VIP orders placed by TC contracts and its Percentage.
        getTotalCostOfVipOrdersforTotalCareContracts(buildPackageReportData, connection, stmt,
                contractId, isContractSearch);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getOrderStatistics");
        }

    }

    /**
     * Retrieves number of orders for the given query.
     * 
     * @param buildPackageReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @param orderQuery
     * @return number of orders for the given query.
     * @throws SQLException
     * @throws SystemException 
     */
    private static Integer getOrders(BuildYourPackageReportData buildPackageReportData,
            Connection connection, PreparedStatement stmt, Integer contractId,
            boolean isContractSearch, String orderQuery) throws SQLException, SystemException {
        String query = orderQuery + getContractClausePlain(isContractSearch);
        int orders = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(buildPackageReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            orders = rs.getInt(1);
        }
        return new Integer(orders);
    }

    /**
     * Returns the percentage of completed order for specified query.
     * 
     * @param buildPackageReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SystemException 
     */
    private static BigDecimal getOrdersPercentage(
            BuildYourPackageReportData buildPackageReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch, String orderQuery)
            throws SQLException, SystemException {
        String query = orderQuery + getContractClausePlain(isContractSearch);
        int orders = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(buildPackageReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            orders = rs.getInt(1);
        }

        // Calculate the Percentage of completed orders
        BigDecimal ordersPercentage = new BigDecimal(0);
        ordersPercentage.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (buildPackageReportData.getTotalCompletedOrders().intValue() > 0) {
            ordersPercentage = (new BigDecimal(orders))
                    .divide(new BigDecimal(buildPackageReportData.getTotalCompletedOrders()
                            .intValue()), DIVIDEND_PRECISION, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        }

        return ordersPercentage;
    }

    /**
     * Gets the data for Percentage of completed orders per mail users graph
     * 
     * @param connection
     * @param stmt
     * @param criteria
     * @param buildPackageReportData
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getPercentagesOfCompletedOrdersByUserCategory(Connection connection,
            PreparedStatement stmt, ReportCriteria criteria,
            BuildYourPackageReportData buildPackageReportData, Integer contractId,
            boolean isContractSearch) throws SQLException, SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getPercentagesOfCompletedOrdersByUserCategory");
        }

        String[][] userCategoryCompletedOrderSQL = {
                { PLAN_SPONSOR, SQL_SELECT_PERCENTAGE_OF_COMPLETED_ORDERS_BY_PLAN_SPONSOR },
                { INTERMEDIARY_CONTACT,
                        SQL_SELECT_PERCENTAGE_OF_COMPLETED_ORDERS_BY_INTERMEDIARY_CONTACT },
                { TPA, SQL_SELECT_PERCENTAGE_OF_COMPLETED_ORDERS_BY_TPA },
                { TOTAL_CARE, SQL_SELECT_PERCENTAGE_OF_COMPLETED_ORDERS_BY_TOTAL_CARE } };

        for (int i = 0; i < userCategoryCompletedOrderSQL.length; i++) {
            getPercentageOfCompletedOrdersByUser(buildPackageReportData, connection, contractId,
                    isContractSearch, userCategoryCompletedOrderSQL[i][0],
                    userCategoryCompletedOrderSQL[i][1]);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getPercentagesOfCompletedOrdersByUserCategory");
        }

    }

    /**
     * Gets total number of orders and contracts.
     * 
     * @param buildPackageReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getTotalOrdersAndContracts(
            BuildYourPackageReportData buildPackageReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {

        // total Orders and Mailed Contracts
        String query = (SQL_SELECT_TOTAL_ORDERS_AND_CONTRACTS_MAILED + getContractClause(isContractSearch));
        ResultSet rs;
        stmt = connection.prepareStatement(query);
        setReportFilters(buildPackageReportData, contractId, isContractSearch, stmt);
        rs = stmt.executeQuery();
        int totalOrders = 0;
        int uniqueContractsMailed = 0;
        if (rs != null) {
            while (rs.next()) {
                totalOrders = rs.getInt(1);
                uniqueContractsMailed = rs.getInt(2);
            }
        }
        buildPackageReportData.setTotalOrders(new Integer(totalOrders));
        buildPackageReportData.setNumberOfContractsUsingMailService(uniqueContractsMailed);

        String contractClause = getContractClause(isContractSearch);
        query = MessageFormat.format(SQL_SELECT_TOTAL_CONTRACTS_MAILED_AND_DOWNLOADED,
                contractClause, contractClause);

        rs = null;
        int totalContractsDownloaded = 0;
        PreparedStatement stmt2 = connection.prepareStatement(query);
        setReportFiltersUnionQuery(buildPackageReportData, contractId, isContractSearch, stmt2);
        rs = stmt2.executeQuery();
        if (rs != null && rs.next()) {
            totalContractsDownloaded = rs.getInt(1);
        }

        buildPackageReportData.setNumberOfContractsUsingMailAndDownload(new Integer(
                totalContractsDownloaded));

        // Set Average number of mailings per contract
        BigDecimal avgNoOfMailings = new BigDecimal(0);
        avgNoOfMailings.setScale(AVERAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (buildPackageReportData.getNumberOfContractsUsingMailAndDownload().intValue() > 0) {
            avgNoOfMailings = (new BigDecimal(buildPackageReportData.getTotalOrders().intValue()))
                    .divide(new BigDecimal(buildPackageReportData
                            .getNumberOfContractsUsingMailAndDownload().intValue()),
                            DIVIDEND_PRECISION, RoundingMode.HALF_UP).setScale(AVERAGE_PRECISION,
                            BigDecimal.ROUND_HALF_EVEN);
        }

        buildPackageReportData.setAverageNumberOfMailingsPerContract(avgNoOfMailings);

    }

    /**
     * Gets Number of repeat contracts using both Download and / or mail service
     * 
     * @param buildPackageReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getRepeatContracts(BuildYourPackageReportData buildPackageReportData,
            Connection connection, PreparedStatement stmt, Integer contractId,
            boolean isContractSearch) throws SQLException, SystemException {
        String contractClause = getContractClause(isContractSearch);
        String query = MessageFormat.format(
                SQL_SELECT_REPEAT_CONTRACTS_MAILED_AND_DOWNLOADED_PACKAGE, contractClause,
                contractClause);
        int repeatContracts = 0;
        stmt = connection.prepareStatement(query);
        setReportFiltersUnionQuery(buildPackageReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            repeatContracts = rs.getInt(1);
        }

        buildPackageReportData.setNumberOfRepeatContracts(new Integer(repeatContracts));
    }

    /**
     * Gets Month list with most mailings.
     * 
     * @param buildPackageReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getMonthWithMostMailings(BuildYourPackageReportData buildPackageReportData,
            Connection connection, PreparedStatement stmt, Integer contractId,
            boolean isContractSearch) throws SQLException, SystemException {

        String query = MessageFormat.format(SQL_SELECT_MONTH_WITH_MOST_MAILINGS,
                getContractClause(isContractSearch));
        List<String> monthList = new ArrayList<String>();
        stmt = connection.prepareStatement(query);
        setReportFilters(buildPackageReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                int year = rs.getInt(1);
                int month = rs.getInt(2);
                String monthYear = MONTHS_DESCRIPTION[month - 1] + SINGLE_SPACE_SYMBOL + year;
                monthList.add(monthYear);
            }
        }
        buildPackageReportData.setMonthsWithMostMailings(monthList);
    }

    /**
     * Gets Number of unique New Business contracts that have at least one order placed or download.
     * 
     * @param buildPackageReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getNewBusinessContracts(BuildYourPackageReportData buildPackageReportData,
            Connection connection, PreparedStatement stmt, Integer contractId,
            boolean isContractSearch) throws SQLException, SystemException {

        String query = MessageFormat
                .format(SQL_SELECT_CONTRACTS_NEW_BUSINESS,
                        getContractClauseWithQualifier(isContractSearch,
                                CONTRACT_NOTICE_MAILING_ORDER_QUALIFIER),
                        getContractClauseWithQualifier(isContractSearch,
                                WEBSITE_USER_ACTION_LOG_QUALIFIER));
        int newBusinessContracts = 0;
        stmt = connection.prepareStatement(query);
        setReportFiltersUnionQuery(buildPackageReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            newBusinessContracts = rs.getInt(1);
        }

        buildPackageReportData.setNewBusinessContractsCount(new Integer(newBusinessContracts));

        // Calculate the Percentage of new business Contracts
        BigDecimal newBusinessContractPercentage = new BigDecimal(0);
        newBusinessContractPercentage.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (buildPackageReportData.getNumberOfContractsUsingMailAndDownload().intValue() > 0) {

            newBusinessContractPercentage = (new BigDecimal(buildPackageReportData
                    .getNewBusinessContractsCount().intValue()))
                    .divide(new BigDecimal(buildPackageReportData
                            .getNumberOfContractsUsingMailAndDownload().intValue()),
                            DIVIDEND_PRECISION, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                    .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);

        }

        buildPackageReportData.setNewBusinessContractsPercentage(newBusinessContractPercentage);

    }

    /**
     * Gets percentage of bulk orders that had sealed option.
     * 
     * @param buildPackageReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SystemException 
     */
    private static void getSealedOrdersPercentage(
            BuildYourPackageReportData buildPackageReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {

        Integer sealedOrders = getOrders(buildPackageReportData, connection, stmt, contractId,
                isContractSearch, SQL_SELECT_NUMBER_SEALED_ORDERS);

        // Calculate the Percentage of bulk orders with sealed option
        BigDecimal sealedOrdersPercentage = new BigDecimal(0);
        sealedOrdersPercentage.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (buildPackageReportData.getNumberOfBulkOrders().intValue() > 0) {

            sealedOrdersPercentage = (new BigDecimal(sealedOrders.intValue()))
                    .divide(new BigDecimal(buildPackageReportData.getNumberOfBulkOrders()
                            .intValue()), DIVIDEND_PRECISION, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);

        }

        buildPackageReportData.setSealedOrdersPercentage(sealedOrdersPercentage);
    }

    /**
     * Gets Number of unique inforce contracts that have at least one order placed or download.
     * 
     * @param buildPackageReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getInforceContracts(BuildYourPackageReportData buildPackageReportData,
            Connection connection, PreparedStatement stmt, Integer contractId,
            boolean isContractSearch) throws SQLException, SystemException {

        String query = MessageFormat
                .format(SQL_SELECT_CONTRACTS_INFORCE,
                        getContractClauseWithQualifier(isContractSearch,
                                CONTRACT_NOTICE_MAILING_ORDER_QUALIFIER),
                        getContractClauseWithQualifier(isContractSearch,
                                WEBSITE_USER_ACTION_LOG_QUALIFIER));
        int inforceContracts = 0;
        stmt = connection.prepareStatement(query);
        setReportFiltersUnionQuery(buildPackageReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            inforceContracts = rs.getInt(1);
        }

        buildPackageReportData.setInforceContractsCount(new Integer(inforceContracts));

        // Calculate the Percentage of inforce Contracts
        BigDecimal inforceContractPercentage = new BigDecimal(0);
        inforceContractPercentage.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (buildPackageReportData.getNumberOfContractsUsingMailAndDownload().intValue() > 0) {

            inforceContractPercentage = (new BigDecimal(buildPackageReportData
                    .getInforceContractsCount().intValue()))
                    .divide(new BigDecimal(buildPackageReportData
                            .getNumberOfContractsUsingMailAndDownload().intValue()),
                            DIVIDEND_PRECISION, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                    .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        }

        buildPackageReportData.setInforceContractsPercentage(inforceContractPercentage);

    }

    /**
     * Gets User Preference – Mail % and Download %
     * 
     * @param buildPackageReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getUserPreferencePercentages(
            BuildYourPackageReportData buildPackageReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {
    	
    	String contractClause = getContractClause(isContractSearch);
         String query = MessageFormat.format(SQL_SELECT_DOWNLOADED_SELECTED_COUNT,contractClause);

        ResultSet rs = null;
        int totalContractsDownloaded = 0;
        PreparedStatement stmt2 = connection.prepareStatement(query);
        setReportFiltersForDownload(buildPackageReportData, contractId, isContractSearch, stmt2);
        rs = stmt2.executeQuery();
        if (rs != null && rs.next()) {
            totalContractsDownloaded = rs.getInt(1);
        }

        // Calculate User Preference – Mail %
    	BigDecimal placeOrderSelectedCount,downloadSelectedCount,placeOrderAndDownloadSelectedCount;
        BigDecimal userPreferenceMailPercentage = new BigDecimal(0);
        userPreferenceMailPercentage.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (totalContractsDownloaded > 0) {
        	placeOrderSelectedCount = new BigDecimal(buildPackageReportData.getTotalOrders()
                    .intValue());
        	downloadSelectedCount = new BigDecimal(totalContractsDownloaded);
        placeOrderAndDownloadSelectedCount = placeOrderSelectedCount.add(downloadSelectedCount);
            userPreferenceMailPercentage = (placeOrderSelectedCount)
                    .divide(placeOrderAndDownloadSelectedCount,
                            DIVIDEND_PRECISION, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                    .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        }

        buildPackageReportData.setUserPreferenceMailPercentage(userPreferenceMailPercentage);

        // User Preference – Download %
        String query1 = SQL_SELECT_USER_PREFERENCE_DOWNLOAD + getContractClause(isContractSearch);
        int downloadContracts = 0;
        stmt = connection.prepareStatement(query1);
        setReportFilters(buildPackageReportData, contractId, isContractSearch, stmt);
        ResultSet rs1 = stmt.executeQuery();
        if (rs1 != null && rs1.next()) {
            downloadContracts = rs.getInt(1);
        }

        // Calculate the Percentage of download Contracts
        BigDecimal userPrefDownloadPercentage = new BigDecimal(0);
        BigDecimal placeOrderCount = new BigDecimal(buildPackageReportData.getTotalOrders()
                .intValue());
        userPrefDownloadPercentage.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (totalContractsDownloaded > 0) {
        	BigDecimal placeOrderAndDownloadCount = placeOrderCount.add(new BigDecimal(downloadContracts));

            userPrefDownloadPercentage = (new BigDecimal(downloadContracts))
                    .divide(placeOrderAndDownloadCount,
                            DIVIDEND_PRECISION, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                    .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        }

        buildPackageReportData.setUserPreferenceDownloadPercentage(userPrefDownloadPercentage);

    }

    /**
     * Gets Total Number of Completed Orders, page counts, Participants count, total cost, average
     * number of pages per order and average number of participants per order.
     * 
     * @param buildPackageReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getCompletedOrdersStats(BuildYourPackageReportData buildPackageReportData,
            Connection connection, PreparedStatement stmt, Integer contractId,
            boolean isContractSearch) throws SQLException, SystemException {

        String query = SQL_SELECT_COMPLETED_ORDER + getContractClause(isContractSearch);
        int totalCompletedOrders = 0, pageCount = 0, participantCount = 0;
        BigDecimal totalCost = new BigDecimal(0);
        stmt = connection.prepareStatement(query);
        setReportFilters(buildPackageReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            totalCompletedOrders = rs.getInt(1);
            pageCount = rs.getInt(2);
            participantCount = rs.getInt(3);
            totalCost = rs.getBigDecimal(4);
        }

        // Java keeps the BigDecimal return from db to null if it is null and does not provide
        // default value in case of primitive
        if (totalCost == null) {
            totalCost = new BigDecimal(0);
        }

        totalCost.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        buildPackageReportData.setTotalCompletedOrders(new Integer(totalCompletedOrders));
        buildPackageReportData.setPageCount(new Integer(pageCount));
        buildPackageReportData.setParticpantCount(new Integer(participantCount));
        buildPackageReportData.setTotalOrderCosts(totalCost);

        // average number of pages per order
        BigDecimal avgNoOfPagesPerOrder = new BigDecimal(0);
        avgNoOfPagesPerOrder.setScale(AVERAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (buildPackageReportData.getTotalCompletedOrders().intValue() > 0) {

            avgNoOfPagesPerOrder = (new BigDecimal(buildPackageReportData.getPageCount().intValue()))
                    .divide(new BigDecimal(buildPackageReportData.getTotalCompletedOrders()
                            .intValue()), DIVIDEND_PRECISION, RoundingMode.HALF_UP).setScale(1,
                            BigDecimal.ROUND_HALF_EVEN);
        }

        buildPackageReportData.setAverageNumberOfPagesPerOrder(avgNoOfPagesPerOrder);

        // average number of participants per order
        BigDecimal avgNoOfParticipantsPerOrder = new BigDecimal(0);
        avgNoOfParticipantsPerOrder.setScale(AVERAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (buildPackageReportData.getTotalCompletedOrders().intValue() > 0) {

            avgNoOfParticipantsPerOrder = (new BigDecimal(buildPackageReportData
                    .getParticpantCount().intValue())).divide(
                    new BigDecimal(buildPackageReportData.getTotalCompletedOrders().intValue()),
                    DIVIDEND_PRECISION, RoundingMode.HALF_UP).setScale(AVERAGE_PRECISION,
                    BigDecimal.ROUND_HALF_EVEN);
        }

        buildPackageReportData.setAverageNumberOfPartipicantsPerOrder(avgNoOfParticipantsPerOrder);

        // average cost per order
        BigDecimal avgCostPerOrder = new BigDecimal(0);
        avgCostPerOrder.setScale(0);
        if (buildPackageReportData.getTotalCompletedOrders().intValue() > 0) {

            avgCostPerOrder = buildPackageReportData
                    .getTotalOrderCosts()
                    .divide(new BigDecimal(buildPackageReportData.getTotalCompletedOrders()
                            .intValue()), DIVIDEND_PRECISION, RoundingMode.HALF_UP)
                    .setScale(AMOUNT_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        }

        buildPackageReportData.setAverageCostPerOrder(avgCostPerOrder);
    }

    /**
     * Gets Number of Completed Orders in black and white.
     * 
     * @param buildPackageReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getCompletedOrdersBlackAndWhite(
            BuildYourPackageReportData buildPackageReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {

        String query = SQL_SELECT_COMPLETED_ORDER_BLACK_AND_WHITE
                + getContractClause(isContractSearch);

        int completedOrderInBlackWhite = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(buildPackageReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            completedOrderInBlackWhite = rs.getInt(1);
        }

        BigDecimal blackWhitePercentage = new BigDecimal(0);
        blackWhitePercentage.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (buildPackageReportData.getTotalCompletedOrders().intValue() > 0) {
            blackWhitePercentage = (new BigDecimal(completedOrderInBlackWhite))
                    .divide(new BigDecimal(buildPackageReportData.getTotalCompletedOrders()
                            .intValue()), DIVIDEND_PRECISION, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        }

        buildPackageReportData.setBlackWhiteOrdersPercentage(blackWhitePercentage);
    }

    /**
     * Gets Number of Completed Orders in color %.
     * 
     * @param buildPackageReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getCompletedOrdersColor(BuildYourPackageReportData buildPackageReportData,
            Connection connection, PreparedStatement stmt, Integer contractId,
            boolean isContractSearch) throws SQLException, SystemException {

        String query = SQL_SELECT_COMPLETED_ORDER_COLOR + getContractClause(isContractSearch);

        int completedOrderInColor = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(buildPackageReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            completedOrderInColor = rs.getInt(1);
        }

        BigDecimal colorPercentage = new BigDecimal(0);
        colorPercentage.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (buildPackageReportData.getTotalCompletedOrders().intValue() > 0) {
            colorPercentage = (new BigDecimal(completedOrderInColor))
                    .divide(new BigDecimal(buildPackageReportData.getTotalCompletedOrders()
                            .intValue()), DIVIDEND_PRECISION, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        }

        buildPackageReportData.setColorOrdersPercentage(colorPercentage);
    }

    /**
     * Amount Paid by John Hancock for VIP orders
     * 
     * @param buildPackageReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getAmountPaidByJHforVipOrders(
            BuildYourPackageReportData buildPackageReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {
        String query = SQL_SELECT_JOHN_HANCOCK_COST + getContractClause(isContractSearch);
        BigDecimal totalJHAmount = null;
        stmt = connection.prepareStatement(query);
        setReportFilters(buildPackageReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            totalJHAmount = rs.getBigDecimal(1);
        }
        if (totalJHAmount == null) {
            totalJHAmount = new BigDecimal(0);
        }
        totalJHAmount.setScale(AMOUNT_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        buildPackageReportData.setAmountPaidByJohnHancock(totalJHAmount);

    }

    /**
     * Gets total cost of all VIP orders placed by Total Care contracts and its Percentage.
     * 
     * @param buildPackageReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getTotalCostOfVipOrdersforTotalCareContracts(
            BuildYourPackageReportData buildPackageReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {
        String query = SQL_SELECT_JOHN_HANCOCK_AMOUNT_TOTAL_CARE_CONTRACTS
                + getContractClauseWithQualifier(isContractSearch,
                        CONTRACT_NOTICE_MAILING_ORDER_QUALIFIER);
        BigDecimal totalJHCostForTotalCare = null;
        stmt = connection.prepareStatement(query);
        setReportFilters(buildPackageReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            totalJHCostForTotalCare = rs.getBigDecimal(1);
        }

        if (totalJHCostForTotalCare == null) {
            totalJHCostForTotalCare = new BigDecimal(0);
        }
        totalJHCostForTotalCare.setScale(AMOUNT_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        buildPackageReportData.setAmountPaidByJohnHancockTotalCare(totalJHCostForTotalCare);

        // Calculate the Percentage of total cost of all VIP orders placed by Total Care contracts
        BigDecimal amountPaidByJHTotalCarePercentage = new BigDecimal(0);
        amountPaidByJHTotalCarePercentage
                .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (buildPackageReportData.getAmountPaidByJohnHancock().doubleValue() > 0) {

            amountPaidByJHTotalCarePercentage = totalJHCostForTotalCare
                    .divide(buildPackageReportData.getAmountPaidByJohnHancock(),
                            DIVIDEND_PRECISION, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                    .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        }

        buildPackageReportData
                .setAmountPaidByJohnHancockTotalCarePercentage(amountPaidByJHTotalCarePercentage);
    }

    /**
     * Gets Percentage of completed orders by Intermediary Contact.
     * 
     * @param buildPackageReportData
     * @param connection
     * @param contractId
     * @param isContractSearch
     * @param userQuery
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getPercentageOfCompletedOrdersByUser(
            BuildYourPackageReportData buildPackageReportData, Connection connection,
            Integer contractId, boolean isContractSearch, String userCategory, String userQuery)
            throws SQLException, SystemException {
        PreparedStatement stmt;
        int ordersCompletedByUser = 0;
        String query = userQuery
                + getContractClauseWithQualifier(isContractSearch,
                        CONTRACT_NOTICE_MAILING_ORDER_QUALIFIER);
        stmt = connection.prepareStatement(query);
        setReportFilters(buildPackageReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            ordersCompletedByUser = rs.getInt(1);
        }

        BigDecimal ordersByUserPercentage = new BigDecimal(0);
        ordersByUserPercentage.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (buildPackageReportData.getTotalCompletedOrders().intValue() > 0) {
            ordersByUserPercentage = (new BigDecimal(ordersCompletedByUser))
                    .divide(new BigDecimal(buildPackageReportData.getTotalCompletedOrders()
                            .intValue()), DIVIDEND_PRECISION, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        }

        if (userCategory.equals(PLAN_SPONSOR)) {
            buildPackageReportData.setOrdersByPlanSponsorsPercentage(ordersByUserPercentage);
        } else if (userCategory.equals(INTERMEDIARY_CONTACT)) {
            buildPackageReportData.setOrdersByIntermediaryContactPercentage(ordersByUserPercentage);
        } else if (userCategory.equals(TPA)) {
            buildPackageReportData.setOrdersByTPAPercentage(ordersByUserPercentage);
        } else if (userCategory.equals(TOTAL_CARE)) {
            buildPackageReportData.setOrdersByTotalCarePercentage(ordersByUserPercentage);
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

    /**
     * Sets Report Search Filters.
     * 
     * @param buildPackageReportData
     * @param contractId
     * @param isContractSearch
     * @param stmt
     * @throws SQLException
     * @throws SystemException 
     */
    private static void setReportFilters(BuildYourPackageReportData buildPackageReportData,
            Integer contractId, boolean isContractSearch, PreparedStatement stmt)
            throws SQLException, SystemException {
    	 Date fromDate = null;
 	    Date toDate = null;
 		try {
 			fromDate = (Date) searchDateFormat.parse(buildPackageReportData.getFromDate().toString() + " 00:00:00.0");
 			toDate = (Date) searchDateFormat.parse(buildPackageReportData.getToDate().toString() + " 23:59:59.66");
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
     * Sets Report Search Filters for Union Queries.
     * 
     * @param buildPackageReportData
     * @param contractId
     * @param isContractSearch
     * @param stmt
     * @throws SQLException
     * @throws SystemException 
     */
    private static void setReportFiltersUnionQuery(
            BuildYourPackageReportData buildPackageReportData, Integer contractId,
            boolean isContractSearch, PreparedStatement stmt) throws SQLException, SystemException {
        int paramNumber = 1;
        
        
        Date fromDate = null;
	    Date toDate = null;
		try {
			fromDate = (Date) searchDateFormat.parse(buildPackageReportData.getFromDate().toString() + " 00:00:00.0");
			toDate = (Date) searchDateFormat.parse(buildPackageReportData.getToDate().toString() + " 23:59:59.66");
		} catch (ParseException e) {
			throw new SystemException(
					"Error while parsing fromDate and toDate" + e.getMessage());
		}
			 Timestamp fromTimestamp = new Timestamp(fromDate.getTime());
			 Timestamp toTimestamp = new Timestamp(toDate.getTime());
	        stmt.setTimestamp(paramNumber++, fromTimestamp);
	        stmt.setTimestamp(paramNumber++, toTimestamp);
        
        if (isContractSearch) {
            stmt.setInt(paramNumber++, contractId.intValue());
        }
        stmt.setTimestamp(paramNumber++, fromTimestamp);
        stmt.setTimestamp(paramNumber++, toTimestamp);
        if (isContractSearch) {
            stmt.setInt(paramNumber++, contractId.intValue());
        }

    }
    
    /**
     * Sets Report Search Filters for Union Queries.
     * 
     * @param buildPackageReportData
     * @param contractId
     * @param isContractSearch
     * @param stmt
     * @throws SQLException
     * @throws SystemException 
     */
    private static void setReportFiltersForDownload(
            BuildYourPackageReportData buildPackageReportData, Integer contractId,
            boolean isContractSearch, PreparedStatement stmt) throws SQLException, SystemException {
        int paramNumber = 1;
        
        
        Date fromDate = null;
	    Date toDate = null;
		try {
			fromDate = (Date) searchDateFormat.parse(buildPackageReportData.getFromDate().toString() + " 00:00:00.0");
			toDate = (Date) searchDateFormat.parse(buildPackageReportData.getToDate().toString() + " 23:59:59.66");
		} catch (ParseException e) {
			throw new SystemException(
					"Error while parsing fromDate and toDate" + e.getMessage());
		}
			 Timestamp fromTimestamp = new Timestamp(fromDate.getTime());
			 Timestamp toTimestamp = new Timestamp(toDate.getTime());
	        stmt.setTimestamp(paramNumber++, fromTimestamp);
	        stmt.setTimestamp(paramNumber++, toTimestamp);
        
        if (isContractSearch) {
            stmt.setInt(paramNumber++, contractId.intValue());
        }
        
    }

}
