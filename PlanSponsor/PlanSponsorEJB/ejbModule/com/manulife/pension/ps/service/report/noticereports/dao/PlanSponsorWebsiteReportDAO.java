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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.noticereports.valueobject.PlanSponsorWebsiteReportData;
import com.manulife.pension.ps.service.report.noticereports.valueobject.PlanSponsorWebsiteReportMonthVisitsVO;
import com.manulife.pension.ps.service.report.noticereports.valueobject.PlanSponsorWebsiteReportPagesVisitedVO;
import com.manulife.pension.ps.service.report.noticereports.valueobject.PlanSponsorWebsiteReportVisitorsStatsVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * Provide Data access services for Plan Sponsor Web site report.
 * 
 */
public class PlanSponsorWebsiteReportDAO extends NoticeReportsBaseDAO {

    private static final String className = PlanSponsorWebsiteReportDAO.class.getName();
    private static final Logger logger = Logger.getLogger(PlanSponsorWebsiteReportDAO.class);
    private static final int PLANSPONSOR_IC_PARAMETERS_START_POSITION = 2;
    private static final int TPA_TOTAL_CARE_PARAMETERS_START_POSITION = 1;
    private static final String PLAN_SPONSOR = "Plan Sponsor";
    private static final String TOTAL_CARE = "TotalCare";
    private static final String TPA = "TPA";
    private static final String UPLOAD_AND_SHARE = "Upload & Share";
    private static final String UPLOAD_AND_SHARE_ADD = "Upload & Share: Add";
    private static final String UPLOAD_AND_SHARE_EDIT = "Upload & Share: Edit";
    private static final String UPLOAD_AND_SHARE_TERMS_OF_USE = "Upload & Share: Terms Of Use";
    private static final String BUILD_YOUR_PACKAGE = "Build Your Package";
    private static final String ORDER_STATUS = "Order Status";
    private static final String PSW_NOTICE_MANAGER_UPLOAD_AND_SHARE_PAGE = "PSW_Notice_Manager_Upload_&_Share_Page";
    private static final String PSW_NOTICE_MANAGER_BUILD_YOUR_PACKAGE_PAGE = "PSW_Notice_Manager_Build_Your_Package_Page";
    private static final String PSW_NOTICE_MANAGER_ADD_NOTICE_PAGE = "PSW_Notice_Manager_Add_Notice_Page";
    private static final String PSW_NOTICE_MANAGER_EDIT_NOTICE_PAGE = "PSW_Notice_Manager_Edit_Notice_Page";
    private static final String PSW_NOTICE_MANAGER_TERMS_OF_USE_PAGE = "PSW_Notice_Manager_Terms_Of_Use_Page";
    private static final String PSW_NOTICE_MANAGER_ORDER_STATUS_PAGE = "PSW_Notice_Manager_Order_Status_Page";

    //SimpleDateFormat is converted to FastDateFormat to make it thread safe
    public static FastDateFormat searchDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.S", Locale.US);
    
    private static HashMap<String, String> actionNametoPageMap = new HashMap<String, String>();

    // Initialize the action names to page mappings
    static {
        actionNametoPageMap.put(PSW_NOTICE_MANAGER_UPLOAD_AND_SHARE_PAGE, UPLOAD_AND_SHARE);
        actionNametoPageMap.put(PSW_NOTICE_MANAGER_ADD_NOTICE_PAGE, UPLOAD_AND_SHARE_ADD);
        actionNametoPageMap.put(PSW_NOTICE_MANAGER_EDIT_NOTICE_PAGE, UPLOAD_AND_SHARE_EDIT);
        actionNametoPageMap
                .put(PSW_NOTICE_MANAGER_TERMS_OF_USE_PAGE, UPLOAD_AND_SHARE_TERMS_OF_USE);
        actionNametoPageMap.put(PSW_NOTICE_MANAGER_BUILD_YOUR_PACKAGE_PAGE, BUILD_YOUR_PACKAGE);
        actionNametoPageMap.put(PSW_NOTICE_MANAGER_ORDER_STATUS_PAGE, ORDER_STATUS);

    }

    private static final String SQL_SELECT_NUMBER_OF_PLANSPONSOR_IC_UNIQUE_VISITORS = "SELECT UC.SECURITY_ROLE_CODE, COUNT(DISTINCT WP.USER_PROFILE_ID) AS NUMBER_VISITORS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE WP.CREATED_TS BETWEEN ? AND ? "
            + "AND WP.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND WP.CONTRACT_ID = UC.CONTRACT_ID ";

    private static final String SQL_CONTRACT_FILTER_CLAUSE = "AND WP.CONTRACT_ID = ? ";
    
    private static final String SQL_CONTRACT_FILTER = "AND DL.CONTRACT_ID = ? ";

    private static final String SQL_SELECT_FILTER_PSW_NOTICE_MANAGER_PAGES = "AND WP.USER_ACTION_NAME IN ('"
            + PSW_NOTICE_MANAGER_BUILD_YOUR_PACKAGE_PAGE
            + "', '"
            + PSW_NOTICE_MANAGER_UPLOAD_AND_SHARE_PAGE
            + "', '"
            + PSW_NOTICE_MANAGER_ADD_NOTICE_PAGE
            + "', "
            + "'"
            + PSW_NOTICE_MANAGER_EDIT_NOTICE_PAGE
            + "', '"
            + PSW_NOTICE_MANAGER_TERMS_OF_USE_PAGE
            + "', '" + PSW_NOTICE_MANAGER_ORDER_STATUS_PAGE + "') ";

    private static final String SQL_SELECT_NUMBER_OF_TPA_UNIQUE_VISITORS = "SELECT COUNT(DISTINCT WP.USER_PROFILE_ID) AS NUMBER_VISITORS  "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_PROFILE UP  "
            + "WHERE WP.CREATED_TS BETWEEN ? AND ?  "
            + "AND WP.USER_PROFILE_ID = UP.USER_PROFILE_ID    "
            + "AND UP.PSW_DIRECTORY_ROLE_CODE  = 'TPA'   ";

    private static final String INTERMEDIARY_CONTACT = "Intermediary Contact";
    private static final String UNIQUE_VISITORS = "Unique visitors";
    private static final String REPEAT_VISITORS = "Repeat visitors";
    private static final String USERS_THAT_POSTED_TO_PARTICIPANT_WEBSITE = "Users that posted to participant website";
    private static final String MONTH_WITH_MOST_VISITS = "Month(s) with most visits";
    private static final String[] PAGE_NAMES = { UPLOAD_AND_SHARE, UPLOAD_AND_SHARE_ADD,
            UPLOAD_AND_SHARE_EDIT, UPLOAD_AND_SHARE_TERMS_OF_USE, BUILD_YOUR_PACKAGE, ORDER_STATUS };

    private static final String SQL_SELECT_NUMBER_OF_PLANSPONSOR_IC_REPEAT_VISITORS = "WITH REPEAT_USERS(USER_PROFILE_ID) "
    		+ "     AS (SELECT DISTINCT WP.USER_PROFILE_ID "
    		+ "         FROM   " 
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "WEBSITE_USER_ACTION_LOG WP "
    		+ "WHERE WP.CREATED_TS BETWEEN ? AND  ? "
    		+ "{0} "
            + "{1} "
    		+ "                AND WP.USER_PROFILE_ID <> 0 "
    		+ "         GROUP  BY WP.USER_PROFILE_ID, "
    		+ "                   WP.USER_ACTION_NAME "
    		+ "         HAVING COUNT(*) > 1) "
    		+ "SELECT UC.SECURITY_ROLE_CODE, "
    		+ "       COUNT(DISTINCT RU.USER_PROFILE_ID) AS NUMBER_VISITORS "
    		+ "FROM   REPEAT_USERS RU, "
    		+ PLAN_SPONSOR_SCHEMA_NAME
    		+ "USER_CONTRACT UC "
    		+ "WHERE  RU.USER_PROFILE_ID = UC.USER_PROFILE_ID "
    		+ "       AND UC.SECURITY_ROLE_CODE IN ( 'INC', 'PSU', 'TRT', 'AUS', 'ADC' ) "
    		+ "GROUP  BY UC.SECURITY_ROLE_CODE";

    private static final String SQL_SELECT_NUMBER_OF_TPA_REPEAT_VISITORS = "WITH REPEAT_USERS(USER_PROFILE_ID) AS "
            + "(SELECT DISTINCT WP.USER_PROFILE_ID "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WP "
            + "WHERE WP.CREATED_TS BETWEEN ? AND  ? "
            + "{0} "
            + "{1} "
            + "                AND WP.USER_PROFILE_ID <> 0 "
            + "GROUP BY WP.USER_PROFILE_ID, "
            + "                   WP.USER_ACTION_NAME "
            + "HAVING COUNT(*) > 1) "
            + "SELECT " 
            + "COUNT(DISTINCT RU.USER_PROFILE_ID) AS NUMBER_VISITORS "
            + "FROM REPEAT_USERS RU, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_PROFILE UP "
            + "WHERE RU.USER_PROFILE_ID = UP.USER_PROFILE_ID  "
            + "AND UP.PSW_DIRECTORY_ROLE_CODE  = 'TPA'";

    private static final String SQL_SELECT_FILTER_TPA_EXCLUDING_TOTAL_CARE = " AND UP.USER_PROFILE_ID NOT IN (SELECT USER_PROFILE_ID FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "EXTERNAL_USER_TPA_FIRM EU WHERE TPA_FIRM_ID = "
            + TOTAL_CARE_TPA_ADMIN_ID + ") ";

    private static final String SQL_SELECT_FILTER_TOTAL_CARE_USERS = " AND UP.USER_PROFILE_ID IN (SELECT USER_PROFILE_ID FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "EXTERNAL_USER_TPA_FIRM EU WHERE TPA_FIRM_ID = "
            + TOTAL_CARE_TPA_ADMIN_ID + ") ";

    private static String SQL_SELECT_NUMBER_OF_PLANSPONSOR_IC_USERS_THAT_POSTED_TO_PARTICIPANT_WEBSITE = "WITH DOCUMENT_USERS(USER_PROFILE_ID, DOCUMENT_ID, CONTRACT_ID, VERSION_NO) AS "
            + "(SELECT DL.USER_PROFILE_ID, DL.DOCUMENT_ID, DL.CONTRACT_ID, DL.VERSION_NO "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG DL "
            + "WHERE DL.CHANGED_POST_TO_PPT_IND='Y' "
            + "AND DL.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + ") "
            + "SELECT UC.SECURITY_ROLE_CODE, COUNT(*) AS NUMBER_VISITORS "
            + "FROM DOCUMENT_USERS DU, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE DU.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND DU.CONTRACT_ID = UC.CONTRACT_ID ";

    private static String SQL_SELECT_NUMBER_OF_TPA_USERS_THAT_POSTED_TO_PARTICIPANT_WEBSITE = "WITH DOCUMENT_USERS(USER_PROFILE_ID, DOCUMENT_ID, CONTRACT_ID) AS "
            + "(SELECT DL.USER_PROFILE_ID, DL.DOCUMENT_ID, DL.CONTRACT_ID " + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG DL "
            + "WHERE DL.CHANGED_POST_TO_PPT_IND='Y' "
            + "AND DL.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + ") "
            + "SELECT COUNT(*) AS NUMBER_VISITORS "
            + "FROM DOCUMENT_USERS DU, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_PROFILE UP "
            + "WHERE DU.USER_PROFILE_ID = UP.USER_PROFILE_ID    "
            + "AND UP.PSW_DIRECTORY_ROLE_CODE  = 'TPA' ";

    private static String SQL_SELECT_MONTH_WITH_MOST_VISITS_BY_PLANSPONSOR = "WITH VISIT_COUNT(VISIT_YEAR, VISIT_MONTH, VISIT_MONTHLY_COUNT)  "
            + "AS (SELECT YEAR(WP.CREATED_TS), MONTH(WP.CREATED_TS), COUNT(*) "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE WP.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + "{1} "
            + "AND WP.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND UC.SECURITY_ROLE_CODE IN ('PSU', 'TRT', 'AUS', 'ADC') "
            + "GROUP BY YEAR(WP.CREATED_TS), MONTH(WP.CREATED_TS)) "
            + "SELECT VC.VISIT_YEAR, VC.VISIT_MONTH, VC.VISIT_MONTHLY_COUNT "
            + "FROM VISIT_COUNT VC "
            + "WHERE VC.VISIT_MONTHLY_COUNT = (SELECT MAX(VISIT_MONTHLY_COUNT) FROM VISIT_COUNT) ";

    private static String SQL_SELECT_MONTH_WITH_MOST_VISITS_BY_INTERMEDIARY_CONTACT = "WITH VISIT_COUNT(VISIT_YEAR, VISIT_MONTH, VISIT_MONTHLY_COUNT)  "
            + "AS (SELECT YEAR(WP.CREATED_TS), MONTH(WP.CREATED_TS), COUNT(*) "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE WP.CREATED_TS BETWEEN ? AND  ? "
            + "{0} "
            + "{1} "
            + "AND WP.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND UC.SECURITY_ROLE_CODE = 'INC' "
            + "GROUP BY YEAR(WP.CREATED_TS), MONTH(WP.CREATED_TS)) "
            + "SELECT VC.VISIT_YEAR, VC.VISIT_MONTH, VC.VISIT_MONTHLY_COUNT "
            + "FROM VISIT_COUNT VC "
            + "WHERE VC.VISIT_MONTHLY_COUNT = (SELECT MAX(VISIT_MONTHLY_COUNT) FROM VISIT_COUNT) ";

    private static String SQL_SELECT_MONTH_WITH_MOST_VISITS_BY_TPA = "WITH TOTAL_CARE_USERS (USER_PROFILE_ID) AS  "
            + "(SELECT USER_PROFILE_ID FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "EXTERNAL_USER_TPA_FIRM EU WHERE TPA_FIRM_ID = "
            + TOTAL_CARE_TPA_ADMIN_ID
            + "), "
            + "VISIT_COUNT(VISIT_YEAR, VISIT_MONTH, VISIT_MONTHLY_COUNT)  "
            + "AS (SELECT   YEAR(WP.CREATED_TS), MONTH(WP.CREATED_TS), COUNT(*) "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_PROFILE UP "
            + "WHERE WP.CREATED_TS BETWEEN ? AND  ?  "
            + "{0} "
            + "{1} "
            + "AND WP.USER_PROFILE_ID = UP.USER_PROFILE_ID  "
            + "AND UP.PSW_DIRECTORY_ROLE_CODE  = 'TPA'  "
            + "AND UP.USER_PROFILE_ID NOT IN (SELECT USER_PROFILE_ID FROM TOTAL_CARE_USERS) "
            + "GROUP BY YEAR(WP.CREATED_TS), MONTH(WP.CREATED_TS)) "
            + "SELECT VC.VISIT_YEAR, VC.VISIT_MONTH, VC.VISIT_MONTHLY_COUNT "
            + "FROM VISIT_COUNT VC "
            + "WHERE VC.VISIT_MONTHLY_COUNT = (SELECT MAX(VISIT_MONTHLY_COUNT) FROM VISIT_COUNT) ";

    private static String SQL_SELECT_MONTH_WITH_MOST_VISITS_BY_TOTAL_CARE = "WITH TOTAL_CARE_USERS (USER_PROFILE_ID) AS  "
            + "(SELECT USER_PROFILE_ID FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "EXTERNAL_USER_TPA_FIRM EU WHERE TPA_FIRM_ID = "
            + TOTAL_CARE_TPA_ADMIN_ID
            + "), "
            + "VISIT_COUNT(VISIT_YEAR, VISIT_MONTH, VISIT_MONTHLY_COUNT)  "
            + "AS (SELECT YEAR(WP.CREATED_TS), MONTH(WP.CREATED_TS), COUNT(*) "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_PROFILE UP "
            + "WHERE WP.CREATED_TS BETWEEN ? AND  ? "
            + "{0} "
            + "{1} "
            + "AND WP.USER_PROFILE_ID = UP.USER_PROFILE_ID  "
            + "AND UP.PSW_DIRECTORY_ROLE_CODE  = 'TPA'  "
            + "AND UP.USER_PROFILE_ID IN (SELECT USER_PROFILE_ID FROM TOTAL_CARE_USERS) "
            + "GROUP BY YEAR(WP.CREATED_TS), MONTH(WP.CREATED_TS)) "
            + "SELECT VC.VISIT_YEAR, VC.VISIT_MONTH, VC.VISIT_MONTHLY_COUNT "
            + "FROM VISIT_COUNT VC "
            + "WHERE VC.VISIT_MONTHLY_COUNT = (SELECT MAX(VISIT_MONTHLY_COUNT) FROM VISIT_COUNT) ";

    private static String SQL_SELECT_PAGES_VISITED_BY_PLAN_SPONSOR = "SELECT WP.USER_ACTION_NAME, COUNT(*) AS PLAN_SPONSOR_COUNT "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE WP.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + "{1} "
            + "AND WP.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND WP.CONTRACT_ID = UC.CONTRACT_ID "
            + "AND UC.SECURITY_ROLE_CODE IN ('PSU', 'TRT', 'AUS', 'ADC') "
            + "GROUP BY WP.USER_ACTION_NAME ";

    private static String SQL_SELECT_PAGES_VISITED_BY_INTERMEDIARY_CONTACT = "SELECT  WP.USER_ACTION_NAME, COUNT(*) AS PAGE_COUNT "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE WP.CREATED_TS BETWEEN ? AND  ? "
            + "{0} "
            + "{1} "
            + "AND WP.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND WP.CONTRACT_ID = UC.CONTRACT_ID "
            + "AND UC.SECURITY_ROLE_CODE = 'INC' "
            + "GROUP BY WP.USER_ACTION_NAME ";

    private static String SQL_SELECT_PAGES_VISITED_BY_TPA = "WITH TOTAL_CARE_USERS (USER_PROFILE_ID) AS  "
            + "(SELECT USER_PROFILE_ID FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "EXTERNAL_USER_TPA_FIRM EU WHERE TPA_FIRM_ID = "
            + TOTAL_CARE_TPA_ADMIN_ID
            + ") "
            + "SELECT  WP.USER_ACTION_NAME, COUNT(*) AS PAGE_COUNT "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_PROFILE UP "
            + "WHERE WP.CREATED_TS BETWEEN ? AND ?  "
            + "{0} "
            + "{1} "
            + "AND WP.USER_PROFILE_ID = UP.USER_PROFILE_ID "
            + "AND UP.PSW_DIRECTORY_ROLE_CODE  = 'TPA' "
            + "AND UP.USER_PROFILE_ID NOT IN (SELECT USER_PROFILE_ID FROM TOTAL_CARE_USERS) "
            + "GROUP BY WP.USER_ACTION_NAME ";

    private static String SQL_SELECT_PAGES_VISITED_BY_TOTAL_CARE = "WITH TOTAL_CARE_USERS (USER_PROFILE_ID) AS  "
            + "(SELECT USER_PROFILE_ID FROM PSW100.EXTERNAL_USER_TPA_FIRM EU WHERE TPA_FIRM_ID = "
            + TOTAL_CARE_TPA_ADMIN_ID
            + ") "
            + "SELECT  WP.USER_ACTION_NAME, COUNT(*) AS PAGE_COUNT "
            + "FROM PSW100.WEBSITE_USER_ACTION_LOG WP, PSW100.USER_PROFILE UP  "
            + "WHERE WP.CREATED_TS BETWEEN ? AND  ?  "
            + "{0} "
            + "{1} "
            + "AND WP.USER_PROFILE_ID = UP.USER_PROFILE_ID  "
            + "AND UP.PSW_DIRECTORY_ROLE_CODE  = 'TPA' "
            + "AND UP.USER_PROFILE_ID IN (SELECT USER_PROFILE_ID FROM TOTAL_CARE_USERS)  "
            + "GROUP BY WP.USER_ACTION_NAME ";

    /**
     * Gets Report Data for Plan Sponsor Website.
     * 
     * @param criteria
     * @param pswReportData
     * @throws SystemException
     * @throws ReportServiceException
     */
    public static void getReportData(ReportCriteria criteria,
            PlanSponsorWebsiteReportData pswReportData) throws SystemException,
            ReportServiceException {

        Integer contractId = getContractNumber(criteria);
        boolean isContractSearch = (contractId != null && contractId.intValue() > 0);

        getNumberOfVisitorsByUsers(criteria, pswReportData, contractId, isContractSearch);
        getMonthWithMostVisitsByUsers(criteria, pswReportData, contractId, isContractSearch);
        getPagesVisitedByUsers(criteria, pswReportData, contractId, isContractSearch);

    }

    /**
     * Gets the number of Visitors By Users based on criteria.
     * 
     * @param criteria
     * @throws SystemException
     * @throws ReportServiceException
     */
    private static void getNumberOfVisitorsByUsers(ReportCriteria criteria,
            PlanSponsorWebsiteReportData pswReportData, Integer contractId, boolean isContractSearch)
            throws SystemException, ReportServiceException {
        Connection connection = null;
        PreparedStatement stmt = null;

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getNumberOfVisitorsByUsers");
        }

        try {

            List<PlanSponsorWebsiteReportVisitorsStatsVO> list = getVisitorsStatsList();
            connection = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);

            // Get Unique visitors
            getUniqueVisitors(pswReportData, connection, stmt, list, contractId, isContractSearch,
                    UNIQUE_VISITORS);

            // Get Repeat Visitors
            getRepeatVisitors(pswReportData, connection, stmt, list, contractId, isContractSearch,
                    REPEAT_VISITORS);

            // Get Visitor that posted to participant web site
            getUsersThatPostedtoParticipantWebsite(pswReportData, connection, stmt, list,
                    contractId, isContractSearch, USERS_THAT_POSTED_TO_PARTICIPANT_WEBSITE);

            pswReportData.setVisitorsUsageList(list);

        } catch (SQLException e) {
            handleSqlException(e, className, "getReportData", MessageFormat.format(
                    GET_REPORT_DATA_ERROR_MESSAGE, PlanSponsorWebsiteReportData.REPORT_NAME,
                    getContractNumber(criteria), getFromDate(criteria), getToDate(criteria)));

        } finally {
            close(stmt, connection);

        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getNumberOfVisitorsByUsers");
   }

    }

    /**
     * Gets the month with most visits by Users based on criteria.
     * 
     * @param criteria
     * @throws SystemException
     * @throws ReportServiceException
     */
    private static void getMonthWithMostVisitsByUsers(ReportCriteria criteria,
            PlanSponsorWebsiteReportData pswReportData, Integer contractId, boolean isContractSearch)
            throws SystemException, ReportServiceException {
        Connection connection = null;
        PreparedStatement stmt = null;

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getMonthWithMostVisitsByUsers");
        }

        try {

            connection = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            PlanSponsorWebsiteReportMonthVisitsVO monthVO = new PlanSponsorWebsiteReportMonthVisitsVO(
                    MONTH_WITH_MOST_VISITS);

            String[][] userCategoryMonthSQL = {
                    { PLAN_SPONSOR, SQL_SELECT_MONTH_WITH_MOST_VISITS_BY_PLANSPONSOR, },
                    { INTERMEDIARY_CONTACT,
                            SQL_SELECT_MONTH_WITH_MOST_VISITS_BY_INTERMEDIARY_CONTACT },
                    { TPA, SQL_SELECT_MONTH_WITH_MOST_VISITS_BY_TPA },
                    { TOTAL_CARE, SQL_SELECT_MONTH_WITH_MOST_VISITS_BY_TOTAL_CARE } };

            for (int i = 0; i < userCategoryMonthSQL.length; i++) {
                getMonthListWithMostVisit(pswReportData, userCategoryMonthSQL[i][0],
                        userCategoryMonthSQL[i][1], connection, stmt, contractId, isContractSearch,
                        monthVO);
            }

            pswReportData.setMonthWithMostVisits(monthVO);

        } catch (SQLException e) {
            handleSqlException(e, className, "getReportData", MessageFormat.format(
                    GET_REPORT_DATA_ERROR_MESSAGE, PlanSponsorWebsiteReportData.REPORT_NAME,
                    getContractNumber(criteria), getFromDate(criteria), getToDate(criteria)));
        } finally {
            close(stmt, connection);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getMonthWithMostVisitsByUsers");
        }

    }

    /**
     * Gets the pages visited by Users based on criteria.
     * 
     * @param criteria
     * @throws SystemException
     * @throws ReportServiceException
     */
    private static void getPagesVisitedByUsers(ReportCriteria criteria,
            PlanSponsorWebsiteReportData pswReportData, Integer contractId, boolean isContractSearch)
            throws SystemException, ReportServiceException {
        Connection connection = null;
        PreparedStatement stmt = null;

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getPagesVisitedByUsers");
        }

        try {
            List<PlanSponsorWebsiteReportPagesVisitedVO> list = getPagesVisitedList();
            connection = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            String[][] userCategorySQL = {
                    { PLAN_SPONSOR, SQL_SELECT_PAGES_VISITED_BY_PLAN_SPONSOR },
                    { INTERMEDIARY_CONTACT, SQL_SELECT_PAGES_VISITED_BY_INTERMEDIARY_CONTACT },
                    { TPA, SQL_SELECT_PAGES_VISITED_BY_TPA },
                    { TOTAL_CARE, SQL_SELECT_PAGES_VISITED_BY_TOTAL_CARE } };

            for (int i = 0; i < userCategorySQL.length; i++) {
                getPagesVisitedByUserCategory(pswReportData, userCategorySQL[i][0],
                        userCategorySQL[i][1], connection, stmt, list, contractId, isContractSearch);
            }

            pswReportData.setPagesVisitedList(list);

        } catch (SQLException e) {
            handleSqlException(e, className, "getReportData", MessageFormat.format(
                    GET_REPORT_DATA_ERROR_MESSAGE, PlanSponsorWebsiteReportData.REPORT_NAME,
                    getContractNumber(criteria), getFromDate(criteria), getToDate(criteria)));
        } finally {
            close(stmt, connection);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getPagesVisitedByUsers");
        }
    }

    /**
     * Get Repeat Visitors numbers.
     * 
     * @param pswReportData
     * @param connection
     * @param stmt
     * @param list
     * @param contractId
     * @param isContractSearch
     * @param visitorType
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getRepeatVisitors(PlanSponsorWebsiteReportData pswReportData,
            Connection connection, PreparedStatement stmt,
            List<PlanSponsorWebsiteReportVisitorsStatsVO> list, Integer contractId,
            boolean isContractSearch, String visitorType) throws SQLException, SystemException {

        // Plan Sponsors and Intermediary
        String query = MessageFormat.format(
                replaceSingleQuote(SQL_SELECT_NUMBER_OF_PLANSPONSOR_IC_REPEAT_VISITORS),
                getContractClause(isContractSearch), SQL_SELECT_FILTER_PSW_NOTICE_MANAGER_PAGES);
        getPlanSponsorAndICVisitorsData(pswReportData, connection, stmt, query, list, contractId,
                isContractSearch, visitorType);

        // Get TPA repeat visitors
        query = MessageFormat.format(replaceSingleQuote(SQL_SELECT_NUMBER_OF_TPA_REPEAT_VISITORS),
                getContractClause(isContractSearch), SQL_SELECT_FILTER_PSW_NOTICE_MANAGER_PAGES)
                + SQL_SELECT_FILTER_TPA_EXCLUDING_TOTAL_CARE;
        getTPATotalCareVisitorsData(pswReportData, connection, stmt, query, list, contractId,
                isContractSearch, false, visitorType);

        // Get Total Care Repeat Visitors
        query = MessageFormat.format(replaceSingleQuote(SQL_SELECT_NUMBER_OF_TPA_REPEAT_VISITORS),
                getContractClause(isContractSearch), SQL_SELECT_FILTER_PSW_NOTICE_MANAGER_PAGES)
                + SQL_SELECT_FILTER_TOTAL_CARE_USERS;
        getTPATotalCareVisitorsData(pswReportData, connection, stmt, query, list, contractId,
                isContractSearch, true, visitorType);
    }

    /**
     * Get users that posted documents to participant website.
     * 
     * @param pswReportData
     * @param connection
     * @param stmt
     * @param list
     * @param contractId
     * @param isContractSearch
     * @param visitorType
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getUsersThatPostedtoParticipantWebsite(
            PlanSponsorWebsiteReportData pswReportData, Connection connection,
            PreparedStatement stmt, List<PlanSponsorWebsiteReportVisitorsStatsVO> list,
            Integer contractId, boolean isContractSearch, String visitorType) throws SQLException, SystemException {

        // Get Plan Sponsors and Intermediary Contact users
        String query = MessageFormat
                .format(replaceSingleQuote(SQL_SELECT_NUMBER_OF_PLANSPONSOR_IC_USERS_THAT_POSTED_TO_PARTICIPANT_WEBSITE),
                		getContractSearch(isContractSearch))
                + SQL_SELECT_FILTER_PLANSPONSORS_IC_ROLES + SQL_SELECT_GROUP_BY_PLANSPONSOR_IC;
        getPlanSponsorAndICVisitorsData(pswReportData, connection, stmt, query, list, contractId,
                isContractSearch, visitorType);

        // Get TPA Repeat Visitors excluding totalCare
        String tpaQuery = MessageFormat
                .format(replaceSingleQuote(SQL_SELECT_NUMBER_OF_TPA_USERS_THAT_POSTED_TO_PARTICIPANT_WEBSITE),
                		getContractSearch(isContractSearch));
        query = tpaQuery + SQL_SELECT_FILTER_TPA_EXCLUDING_TOTAL_CARE;
        getTPATotalCareVisitorsData(pswReportData, connection, stmt, query, list, contractId,
                isContractSearch, false, visitorType);

        // Get Total Care Repeat Visitors
        query = tpaQuery + SQL_SELECT_FILTER_TOTAL_CARE_USERS;
        getTPATotalCareVisitorsData(pswReportData, connection, stmt, query, list, contractId,
                isContractSearch, true, visitorType);
    }

    /**
     * Returns the SQL clause if contract filter is active.
     * 
     * @param isContractSearch
     * @return
     */
    private static String getContractClause(boolean isContractSearch) {
        return ((isContractSearch) ? SQL_CONTRACT_FILTER_CLAUSE : "");
    }
    
    /**
     * Returns the SQL clause if contract filter is active.
     * 
     * @param isContractSearch
     * @return
     */
    private static String getContractSearch(boolean isContractSearch) {
        return ((isContractSearch) ? SQL_CONTRACT_FILTER : "");
    }

    /**
     * Get Unique Visitors
     * 
     * @param pswReportData
     * @param connection
     * @param stmt
     * @param list
     * @param contractId
     * @param isContractSearch
     * @param visitorType
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getUniqueVisitors(PlanSponsorWebsiteReportData pswReportData,
            Connection connection, PreparedStatement stmt,
            List<PlanSponsorWebsiteReportVisitorsStatsVO> list, Integer contractId,
            boolean isContractSearch, String visitorType) throws SQLException, SystemException {
        // Get Plan Sponsors and Intermediary Contact users
        String query = SQL_SELECT_NUMBER_OF_PLANSPONSOR_IC_UNIQUE_VISITORS
                + SQL_SELECT_FILTER_PSW_NOTICE_MANAGER_PAGES
                + SQL_SELECT_FILTER_PLANSPONSORS_IC_ROLES + getContractClause(isContractSearch)
                + SQL_SELECT_GROUP_BY_PLANSPONSOR_IC;
        getPlanSponsorAndICVisitorsData(pswReportData, connection, stmt, query, list, contractId,
                isContractSearch, visitorType);

        // Get TPA
        query = SQL_SELECT_NUMBER_OF_TPA_UNIQUE_VISITORS
                + SQL_SELECT_FILTER_PSW_NOTICE_MANAGER_PAGES + getContractClause(isContractSearch)
                + SQL_SELECT_FILTER_TPA_EXCLUDING_TOTAL_CARE;
        getTPATotalCareVisitorsData(pswReportData, connection, stmt, query, list, contractId,
                isContractSearch, false, visitorType);

        // Get Total Care Repeat Visitors
        query = SQL_SELECT_NUMBER_OF_TPA_UNIQUE_VISITORS
                + SQL_SELECT_FILTER_PSW_NOTICE_MANAGER_PAGES + getContractClause(isContractSearch)
                + SQL_SELECT_FILTER_TOTAL_CARE_USERS;
        getTPATotalCareVisitorsData(pswReportData, connection, stmt, query, list, contractId,
                isContractSearch, true, visitorType);
    }

    /**
     * Gets Plan Sponsor and Intermediary Contact visitors data.
     * 
     * @param pswReportData
     * @param connection
     * @param stmt
     * @param query
     * @param list
     * @param contractId
     * @param isContractSearch
     * @param visitorType
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getPlanSponsorAndICVisitorsData(PlanSponsorWebsiteReportData pswReportData,
            Connection connection, PreparedStatement stmt, String query,
            List<PlanSponsorWebsiteReportVisitorsStatsVO> list, Integer contractId,
            boolean isContractSearch, String visitorType) throws SQLException, SystemException {
        ResultSet rs;
        stmt = connection.prepareStatement(query);
        setReportFilters(pswReportData, contractId, isContractSearch, stmt);
        rs = stmt.executeQuery();

        if (rs != null) {

            while (rs.next()) {
                String role = rs.getString(1);
                if (role.equals(INC_ROLE_CODE)) {
                    getVisitorsData(rs, list, visitorType, INTERMEDIARY_CONTACT,
                            PLANSPONSOR_IC_PARAMETERS_START_POSITION);
                } else if (role.equals(PSU_ROLE_CODE) || role.equals(TRT_ROLE_CODE)
                        || role.equals(AUS_ROLE_CODE) || role.equals(ADC_ROLE_CODE)) {
                    getVisitorsData(rs, list, visitorType, PLAN_SPONSOR,
                            PLANSPONSOR_IC_PARAMETERS_START_POSITION);
                }
            }

        }

    }

    /**
     * Gets TPA and Total Care visitors data.
     * 
     * @param pswReportData
     * @param connection
     * @param stmt
     * @param query
     * @param list
     * @param contractId
     * @param isContractSearch
     * @param isTotalCare
     * @param visitorType
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getTPATotalCareVisitorsData(PlanSponsorWebsiteReportData pswReportData,
            Connection connection, PreparedStatement stmt, String query,
            List<PlanSponsorWebsiteReportVisitorsStatsVO> list, Integer contractId,
            boolean isContractSearch, boolean isTotalCare, String visitorType) throws SQLException, SystemException {
        ResultSet rs;
        stmt = connection.prepareStatement(query);
        setReportFilters(pswReportData, contractId, isContractSearch, stmt);
        rs = stmt.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                getVisitorsData(rs, list, visitorType, (isTotalCare ? TOTAL_CARE : TPA),
                        TPA_TOTAL_CARE_PARAMETERS_START_POSITION);
            }
        }
    }

    /**
     * Gets month list with most visits.
     * 
     * @param pswReportData
     * @param userCategory
     * @param userQuery
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @param monthVO
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getMonthListWithMostVisit(PlanSponsorWebsiteReportData pswReportData,
            String userCategory, String userQuery, Connection connection, PreparedStatement stmt,
            Integer contractId, boolean isContractSearch,
            PlanSponsorWebsiteReportMonthVisitsVO monthVO) throws SQLException, SystemException {
        ResultSet rs;

        String query = MessageFormat.format(replaceSingleQuote(userQuery),
                getContractClause(isContractSearch), SQL_SELECT_FILTER_PSW_NOTICE_MANAGER_PAGES);
        stmt = connection.prepareStatement(query);
        setReportFilters(pswReportData, contractId, isContractSearch, stmt);
        rs = stmt.executeQuery();
        List<String> list = new ArrayList<String>();
        if (rs != null) {
            while (rs.next()) {
                int year = rs.getInt(1);
                int month = rs.getInt(2);
                String monthYear = MONTHS_DESCRIPTION[month - 1] + SINGLE_SPACE_SYMBOL + year;
                list.add(monthYear);
            }
        }

        if (userCategory.equals(PLAN_SPONSOR)) {
            monthVO.setPlanSponsorMonths(list);
        } else if (userCategory.equals(INTERMEDIARY_CONTACT)) {
            monthVO.setIntermediaryContactMonths(list);
        } else if (userCategory.equals(TPA)) {
            monthVO.setTpaMonths(list);
        } else if (userCategory.equals(TOTAL_CARE)) {
            monthVO.setTotalCareMonths(list);
        }

    }

    /**
     * Sets Report Search Filters.
     * 
     * @param pswReportData
     * @param contractId
     * @param isContractSearch
     * @param stmt
     * @throws SQLException
     * @throws SystemException 
     */
    private static void setReportFilters(PlanSponsorWebsiteReportData pswReportData,
            Integer contractId, boolean isContractSearch, PreparedStatement stmt)
            throws SQLException, SystemException {
    Date fromDate = null;
    Date toDate = null;
	try {
		fromDate = (Date) searchDateFormat.parse(pswReportData.getFromDate().toString() + " 00:00:00.0");
		toDate = (Date) searchDateFormat.parse(pswReportData.getToDate().toString() + " 23:59:59.66");
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
     * Constructs the visitor statistics list.
     * 
     * @return
     */
    private static List<PlanSponsorWebsiteReportVisitorsStatsVO> getVisitorsStatsList() {
        List<PlanSponsorWebsiteReportVisitorsStatsVO> list = new ArrayList<PlanSponsorWebsiteReportVisitorsStatsVO>();

        String[] visitorTypes = { UNIQUE_VISITORS, REPEAT_VISITORS,
                USERS_THAT_POSTED_TO_PARTICIPANT_WEBSITE };

        for (String visitorType : visitorTypes) {
            list.add(new PlanSponsorWebsiteReportVisitorsStatsVO(visitorType));
        }
        return list;
    }

    /**
     * Get Pages visited list.
     * 
     * @return
     */
    private static List<PlanSponsorWebsiteReportPagesVisitedVO> getPagesVisitedList() {
        List<PlanSponsorWebsiteReportPagesVisitedVO> list = new ArrayList<PlanSponsorWebsiteReportPagesVisitedVO>();
        for (String pageName : PAGE_NAMES) {
            list.add(new PlanSponsorWebsiteReportPagesVisitedVO(pageName));
        }
        return list;
    }

    /**
     * Gets Visitor Data.
     * 
     * @param rs
     * @param list
     * @param visitorsType
     * @param user
     * @param paramNumber
     * @throws SQLException
     */
    private static void getVisitorsData(ResultSet rs,
            List<PlanSponsorWebsiteReportVisitorsStatsVO> list, String visitorsType, String user,
            int paramNumber) throws SQLException {
        PlanSponsorWebsiteReportVisitorsStatsVO visitorsVO = null;
        for (PlanSponsorWebsiteReportVisitorsStatsVO vo : list) {
            if (vo.getStatisticDescription().equals(visitorsType)) {
                visitorsVO = vo;
            }
        }

        if (visitorsVO != null) {
            int count = rs.getInt(paramNumber);
            if (user.equals(PLAN_SPONSOR)) {
            	 if(visitorsVO.getPlanSponsorCount()>0){
            		 visitorsVO.setPlanSponsorCount(rs.getInt(paramNumber)+visitorsVO.getPlanSponsorCount());
            	 }else{
            		 visitorsVO.setPlanSponsorCount(rs.getInt(paramNumber)); 
            	 }
            } else if (user.equals(INTERMEDIARY_CONTACT)) {
                visitorsVO.setIntermediaryContactCount(count);
            } else if (user.equals(TPA)) {
                visitorsVO.setTpaCount(count);
            } else if (user.equals(TOTAL_CARE)) {
                visitorsVO.setTotalCareCount(count);
            }
        }

    }

    /**
     * Get Pages Visited By User Category.
     * 
     * @param pswReportData
     * @param connection
     * @param stmt
     * @param list
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getPagesVisitedByUserCategory(PlanSponsorWebsiteReportData pswReportData,
            String userCategory, String userQuery, Connection connection, PreparedStatement stmt,
            List<PlanSponsorWebsiteReportPagesVisitedVO> list, Integer contractId,
            boolean isContractSearch) throws SQLException, SystemException {

        String query = MessageFormat.format(replaceSingleQuote(userQuery),
                getContractClause(isContractSearch), SQL_SELECT_FILTER_PSW_NOTICE_MANAGER_PAGES);
        ResultSet rs;
        stmt = connection.prepareStatement(query);
        setReportFilters(pswReportData, contractId, isContractSearch, stmt);
        rs = stmt.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                String page = rs.getString(1);
                int count = rs.getInt(2);
                setPageVisitedCount(list, userCategory, page, count);
            }
        }
    }

    /**
     * Sets Pages visited count.
     * 
     * @param list
     * @param userCategory
     * @param page
     * @param count
     */
    private static void setPageVisitedCount(List<PlanSponsorWebsiteReportPagesVisitedVO> list,
            String userCategory, String page, int count) {
        for (PlanSponsorWebsiteReportPagesVisitedVO vo : list) {
            String pageName = actionNametoPageMap.get(page);
            if (pageName != null && vo.getPageName().equalsIgnoreCase(pageName)) {
                if (userCategory.equals(PLAN_SPONSOR)) {
                    vo.setPlanSponsorCount(count);
                } else if (userCategory.equals(INTERMEDIARY_CONTACT)) {
                    vo.setIntermediaryContactCount(count);
                } else if (userCategory.equals(TPA)) {
                    vo.setTpaCount(count);
                } else if (userCategory.equals(TOTAL_CARE)) {
                    vo.setTotalCareCount(count);
                }
                return;

            }
        }
    }

}
