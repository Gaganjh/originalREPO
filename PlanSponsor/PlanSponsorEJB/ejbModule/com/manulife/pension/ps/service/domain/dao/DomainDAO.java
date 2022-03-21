package com.manulife.pension.ps.service.domain.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;

public final class DomainDAO extends BaseDatabaseDAO {

    private static final String className = DomainDAO.class.getName();

    private static final String GET_AS_OF_DATE_SQL = "{call "
            + CUSTOMER_SCHEMA_NAME + "GET_AS_OF_DATE(?,?)}";

    private static final String GET_CONTRACT_MONTH_END_DATES_SQL = "{call "
            + CUSTOMER_SCHEMA_NAME + "GET_CONTRACT_MONTH_END_DATES(?,?)}";

    private static final String LOOKUP_CODES_SQL = "select LOOKUP_CODE, LOOKUP_DESC FROM "
            + CUSTOMER_SCHEMA_NAME + "LOOKUP where LOOKUP_TYPE_NAME = ?";

    private static final String STP_LOOKUP_CODES_SQL = "select LOOKUP_CODE, LOOKUP_DESC FROM "
            + STP_SCHEMA_NAME + "STP_LOOKUP where LOOKUP_TYPE_NAME = ?";

    private static final String GET_CONTRACT_EFFECTIVE_DATE_SQL = "select EFFECTIVE_DATE from "
            + CUSTOMER_SCHEMA_NAME + "CONTRACT_CS where CONTRACT_ID = ?";

    public static final String SQL_SELECT_VALID_MONEY_TYPES_FOR_MONEY_SOURCE = "SELECT rtrim(MONEY_TYPE_CODE) as moneyTypeId "
            + "FROM "
            + CUSTOMER_SCHEMA_NAME
            + "VALID_MONEY_SOURCE_TYPE "
            + "WHERE rtrim(MONEY_SOURCE_TYPE_CODE) = ? " + "FOR FETCH ONLY";

    private static Logger logger = Logger.getLogger(DomainDAO.class);

    /**
     * Retrieves the as of date data (last business date) from the db2 stored
     * procedure.
     * 
     * @return Date
     * @throws SystemException
     */
    public static Date getAsOfDate() throws SystemException {
        /*
         * From Stored Procedure Declaration EZK100.GET_AS_OF_DATE out
         * AS_OF_DATE String(Format yyyy/mm/dd) DYNAMIC RESULT SETS 1
         */
        if (logger.isDebugEnabled())
            logger.debug("entry -> getAsOfDate");

        String strAsOfDate = null;
        Connection conn = null;
        CallableStatement statement = null;
        try {
            // setup the connection and the statemnt
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            statement = conn.prepareCall(GET_AS_OF_DATE_SQL);

            if (logger.isDebugEnabled())
                logger.debug("Calling Stored Procedure: " + GET_AS_OF_DATE_SQL);

            // set the input parameters
            statement.registerOutParameter(1, Types.VARCHAR);
            statement.registerOutParameter(2, Types.DECIMAL);

            // execute the stored procedure
            statement.execute();

            strAsOfDate = statement.getString(1);

        } catch (SQLException e) {
            throw new SystemException(e, DomainDAO.class.getName(),
                    "getAsOfDate",
                    "Failed during GET_AS_OF_DATE_SQL stored proc call.");
        } finally {
            close(statement, conn);
        }

        int year = Integer.parseInt(strAsOfDate.substring(0, 4));
        int month = Integer.parseInt(strAsOfDate.substring(5, 7)) - 1;
        int day = Integer.parseInt(strAsOfDate.substring(8));
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month, day);

        Date asOfDate = cal.getTime();

        if (logger.isDebugEnabled())
            logger.debug("exit <- getAsOfDate");

        return asOfDate;
    }

    /**
     * Retrieves the month end dates for the last 34 months from the db2 stored
     * procedure.
     * 
     * @param ContractNumber
     * @return Collection of Dates
     * @throws SystemException
     */
    public static Collection getMonthEndDates(int contractNumber)
            throws SystemException {
        /*
         * From Stored Procedure Declaration EZK100.GET_CONTRACT_MONTH_END_DATES
         * in Decimal contractId out_sqlMonthEndDates MotnhEndDates(Format
         * yyyy/mm/dd) DYNAMIC RESULT SETS 1
         */
        if (logger.isDebugEnabled())
            logger.debug("entry -> getMonthEndDates");

        Collection monthEndDates = new ArrayList();
        Connection conn = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        try {
            // setup the connection and the statemnt
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            statement = conn.prepareCall(GET_CONTRACT_MONTH_END_DATES_SQL);

            if (logger.isDebugEnabled())
                logger.debug("Calling Stored Procedure: "
                        + GET_CONTRACT_MONTH_END_DATES_SQL);

            // set the input/output parameters
            statement.setBigDecimal(1, new BigDecimal((new Integer(
                    contractNumber).doubleValue())));
            statement.registerOutParameter(2, Types.DECIMAL);

            // execute the stored procedure
            statement.execute();

            resultSet = statement.getResultSet();
            
            if (resultSet != null) {
                
                while (resultSet.next()) {
                    java.sql.Date tempDate = resultSet
                            .getDate("CONTRACT_MONTH_END_DATE");
                    monthEndDates.add(new Date(tempDate.getTime()));
                }
                
            }
            
        } catch (SQLException e) {
            throw new SystemException(
                    e,
                    DomainDAO.class.getName(),
                    "getMonthEndDates",
                    "Failed during CONTRACT_MONTH_END_DATE stored proc call. Input parameter is contractNumber:"
                            + contractNumber);
        } finally {
            close(statement, conn);
        }

        if (logger.isDebugEnabled())
            logger.debug("exit <- getMonthEndDates");

        return monthEndDates;

    }

    /**
     * Retrieves the Codes and Descriptions from the LOOKUP table for a given
     * type
     * 
     * @param String
     *            lookupType the type to looked up
     * @return java.util.Map
     * @throws SystemException
     */
    public static Map getLookupCodes(String lookupType) throws SystemException {
        if (logger.isDebugEnabled())
            logger.debug("entry -> getFeatureMap");

        java.util.Map codeMap = new java.util.HashMap();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            // setup the connection and the statemnt
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            statement = conn.prepareStatement(LOOKUP_CODES_SQL);
            statement.setString(1, lookupType);

            if (logger.isDebugEnabled())
                logger.debug("Performing query: " + LOOKUP_CODES_SQL);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String code = resultSet.getString(1).trim();
                String desc = resultSet.getString(2);
                codeMap.put(code, desc);
            }

        } catch (SQLException e) {
            throw new SystemException(e, DomainDAO.class.getName(),
                    "getLookupCodes",
                    "Failed during LOOKUP_CODES_SQL stored proc call.");
        } finally {
            close(statement, conn);
        }

        if (logger.isDebugEnabled())
            logger.debug("exit <- getFeatureMap");

        return codeMap;

    }

    /**
     * Retrieves the Codes and Descriptions from the STP_LOOKUP table for a
     * given type
     * 
     * @param String
     *            lookupType the type to looked up
     * @return java.util.Map
     * @throws SystemException
     */
    public static Map getSTPLookupCodes(String lookupType)
            throws SystemException {
        if (logger.isDebugEnabled())
            logger.debug("entry -> getSTPLookupCodes");

        java.util.Map codeMap = new java.util.HashMap();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            // setup the connection and the statemnt
            conn = getReadUncommittedConnection(className, SUBMISSION_DATA_SOURCE_NAME);
            statement = conn.prepareStatement(STP_LOOKUP_CODES_SQL);
            statement.setString(1, lookupType);

            if (logger.isDebugEnabled())
                logger.debug("Performing query: " + STP_LOOKUP_CODES_SQL);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                codeMap.put(resultSet.getString(1).trim(), resultSet
                        .getString(2));
            }

        } catch (SQLException e) {
            throw new SystemException(e, DomainDAO.class.getName(),
                    "getLookupCodes", "Failed during LOOKUP_CODES_SQL call.");
        } finally {
            close(statement, conn);
        }

        if (logger.isDebugEnabled())
            logger.debug("exit <- getSTPLookupCodes");

        return codeMap;

    }

    /**
     * Retrieves the contract effective date from the LOOKUP table for a given
     * type
     * 
     * @param int
     *            Contract number
     * @return java.util.Date
     * @throws SystemException
     */
    public static java.util.Date getEffectiveDate(int contractNumber)
            throws SystemException {
        if (logger.isDebugEnabled())
            logger.debug("entry -> getEffectiveDate");

        java.util.Date effectiveDate = null;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            // setup the connection and the statement
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            statement = conn.prepareStatement(GET_CONTRACT_EFFECTIVE_DATE_SQL);
            statement.setInt(1, contractNumber);

            resultSet = statement.executeQuery();
            resultSet.next();
            effectiveDate = (java.util.Date) resultSet.getDate(1);

        } catch (SQLException e) {
            throw new SystemException(e, DomainDAO.class.getName(),
                    "getEffectiveDate",
                    "Failed while getting the contract effective date for "
                            + contractNumber);
        } finally {
            close(statement, conn);
        }

        if (logger.isDebugEnabled())
            logger.debug("exit <- getEffectiveDate");

        return effectiveDate;
    }

    /**
     * Retrieves all valid money types for the given money source.
     * 
     * @param moneySourceId The money source ID to query
     * @return All valid money types for the given money source.
     * @throws SystemException if there is any SQLException.
     */
    public static Set getValidMoneyTypesForMoneySource(final String moneySourceId)
            throws SystemException {
        Set validMoneyTypes = new HashSet();
        PreparedStatement moneyTypeSelectStatement = null;
        ResultSet moneyTypeResultSet = null;
        Connection csdbConnection = null;
        try {
            csdbConnection = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            moneyTypeSelectStatement = csdbConnection
                    .prepareStatement(SQL_SELECT_VALID_MONEY_TYPES_FOR_MONEY_SOURCE);
            moneyTypeSelectStatement.setString(1, moneySourceId);
            moneyTypeResultSet = moneyTypeSelectStatement.executeQuery();
            while (moneyTypeResultSet.next()) {
                String moneyTypeId = moneyTypeResultSet.getString("moneyTypeId");

                validMoneyTypes.add(moneyTypeId);
            }
            moneyTypeResultSet.close();
            return validMoneyTypes;
        } catch (SQLException e) {
            throw new SystemException(e, className, "getMoneyTypes",
                    "Problem occurred in prepared call: "
                            + SQL_SELECT_VALID_MONEY_TYPES_FOR_MONEY_SOURCE + " for money source "
                            + moneySourceId);
        } finally {
            close(moneyTypeSelectStatement, csdbConnection);
        }
    }
}
