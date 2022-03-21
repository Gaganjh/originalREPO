package com.manulife.pension.ps.service.report.noticereports.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.cache.PsProperties;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * Base DAO for Notice Manager Control Reports.
 * 
 */
public class NoticeReportsBaseDAO extends ReportServiceBaseDAO {

    protected static final String TWO_CONSECUTIVE_SINGLE_QUOTES = "''";

    protected static final String SINGLE_QUOTE = "'";

    protected static final String SQL_SELECT_FILTER_PLANSPONSORS_IC_ROLES = "AND UC.SECURITY_ROLE_CODE IN ('INC', 'PSU', 'TRT', 'AUS', 'ADC') ";

    protected static final String SQL_SELECT_GROUP_BY_PLANSPONSOR_IC = "GROUP BY UC.SECURITY_ROLE_CODE ";

    protected static final String FILTER_CONTRACT_NUMBER = "contractNumber";

    public static final String FILTER_FROM_DATE = "fromDate";

    public static final String FILTER_TO_DATE = "toDate";

    public static final String SINGLE_SPACE_SYMBOL = " ";

    public static final String TOTAL_CARE_TPA_ADMIN_ID_KEY = "MF.noticeManager.reports.totalCareTpaAdminId";

    protected static final String TOTAL_CARE_TPA_ADMIN_ID = PsProperties
            .get(TOTAL_CARE_TPA_ADMIN_ID_KEY);

    protected static final String SQL_CONTRACT_FILTER_CLAUSE_PLAIN = "AND CONTRACT_ID = ? ";

    protected static final String SQL_CONTRACT_FILTER_CLAUSE_WITH_QUALIFIER = "AND {0}CONTRACT_ID = ? ";

    protected static final String GET_REPORT_DATA_ERROR_MESSAGE = "Error while accessing Notice Manager {0} Report data: Contract Number [{1}], From Date [{2}], To Date [{3}] ";

    protected static final String[] MONTHS_DESCRIPTION = { "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    protected static final String INC_ROLE_CODE = "INC";

    protected static final String PSU_ROLE_CODE = "PSU";

    protected static final String TRT_ROLE_CODE = "TRT";

    protected static final String AUS_ROLE_CODE = "AUS";

    protected static final String ADC_ROLE_CODE = "ADC";

    protected static final String PLAN_SPONSOR = "Plan Sponsors";

    protected static final String INTERMEDIARY_CONTACT = "Intermediary Contact";

    protected static final String TOTAL_CARE = "TotalCare TPA";

    protected static final String TPA = "TPA";

    protected static final String PACKAGE_DOWNLOAD = "PSW_Notice_Manager_Download_Package";

    protected static final String ALERT_DELETE = "PSW_Notice_Manager_Delete_Alert";

    protected static final String PLAN_SPONSOR_NEW_BUSINESS = "PS - New Business";

    protected static final String PLAN_SPONSOR_INFORCE = "PS - Inforce";

    protected static final int PERCENTAGE_PRECISION = 3;

    protected static final int DIVIDEND_PRECISION = 6;

    protected static final int AMOUNT_PRECISION = 2;

    protected static final int AVERAGE_PRECISION = 1;

    protected static final int ZERO_PRECISION = 0;

    protected enum UserCategory {
        PLAN_SPONSOR, INTERMEDIARY_CONTACT, TOTAL_CARE, TPA
    }

    /**
     * Default Constructor.
     */
    public NoticeReportsBaseDAO() {
        super();
    }

    /**
     * Method to get the Contract Number for the given Report Criteria.
     * 
     * @param criteria The ReportCriteria that contains the filter.
     * @return The contract number.
     * @throws SQLException
     * @throws SystemException
     */
    public static Integer getContractNumber(ReportCriteria criteria) throws SystemException {
        Integer contractNumberString = (Integer) criteria.getFilterValue(FILTER_CONTRACT_NUMBER);
        Integer contractNumber = Integer.valueOf(0);
        if (contractNumberString != null) {
            contractNumber = Integer.valueOf(contractNumberString);
        }
        return contractNumber;

    }

    /**
     * Method to get the From Date for the given Report Criteria.
     * 
     * @param criteria The ReportCriteria that contains the filter.
     * @return The contract number.
     * @throws SQLException
     * @throws SystemException
     */
    public static Timestamp getFromDate(ReportCriteria criteria) throws SystemException {
        Date fromDateSelected = (Date) criteria.getFilterValue(FILTER_FROM_DATE);
        Timestamp fromDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        if (fromDateSelected != null) {
            fromDate = new Timestamp(fromDateSelected.getTime());
        }
        return fromDate;

    }

    /**
     * Method to get the To Date for the given Report Criteria.
     * 
     * @param criteria The ReportCriteria that contains the filter.
     * @return The contract number.
     * @throws SQLException
     * @throws SystemException
     */
    public static Timestamp getToDate(ReportCriteria criteria) throws SystemException {
        Date toDateSelected = (Date) criteria.getFilterValue(FILTER_TO_DATE);
        Timestamp toDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        if (toDateSelected != null) {
            toDate = new Timestamp(toDateSelected.getTime());
            ;
        }
        return toDate;

    }

    /**
     * Converts Java.util.Date to java.sql.Date.
     * 
     * @param date, java.util
     * @return date, java.sql
     */
    protected static java.sql.Date convertSQLDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    /**
     * Replaces one single quotes in the string to two consecutive single quotes. It is useful when
     * using with MessageFormat format method to construct the SQL queries and those queries contain
     * single quote.
     * 
     * @param queryString
     * @return string where single quotes in the string replaced with two consecutive single quotes.
     */
    protected static String replaceSingleQuote(String queryString) {

        return (queryString != null ? queryString.replace(SINGLE_QUOTE,
                TWO_CONSECUTIVE_SINGLE_QUOTES) : null);
    }

    /**
     * Returns the SQL clause if contract filter is active.
     * 
     * @param isContractSearch
     * @return SQL clause
     */
    protected static String getContractClauseWithQualifier(boolean isContractSearch,
            String qualifier) {
        String clause = "";
        if (isContractSearch) {
            clause = MessageFormat.format(SQL_CONTRACT_FILTER_CLAUSE_WITH_QUALIFIER, qualifier);
        }
        return clause;
    }

    /**
     * Returns the SQL clause if contract filter is active without any qualifier.
     * 
     * @param isContractSearch
     * @return SQL clause
     */
    protected static String getContractClausePlain(boolean isContractSearch) {
        String clause = "";
        if (isContractSearch) {
            clause = SQL_CONTRACT_FILTER_CLAUSE_PLAIN;
        }
        return clause;
    }

}
