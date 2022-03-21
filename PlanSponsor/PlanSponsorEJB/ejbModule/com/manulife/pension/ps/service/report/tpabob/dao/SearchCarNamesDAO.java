package com.manulife.pension.ps.service.report.tpabob.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;

/**
 * This DAO is used to retrieve a list of Car Names for a given userprofileID.
 * 
 * @author harlomte
 * 
 */
public class SearchCarNamesDAO extends BaseDatabaseDAO {
    
    private static final String className = SearchCarNamesDAO.class.getName();

    private static final Logger logger = Logger.getLogger(SearchCarNamesDAO.class);

    private static final String GET_CAR_FOR_USER_PROFILE = "call " + CUSTOMER_SCHEMA_NAME
            + "GET_CAR_FOR_USER_PROFILE(?)";

    private static final String CLIENT_ACCOUNT_REP_NAME = "CLIENT_ACCOUNT_REP_NAME";
    

    /**
     * This method retrieves a list of Car Names of contracts that the user is allowed to access.
     * 
     * @param userProfileId - UserProfile ID.
     * @throws SystemException
     */
    public static ArrayList<String> getCarListForUserProfile(Long userProfileId) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getCarForUserProfile");
        }

        ArrayList<String> carnameList = new ArrayList<String>();

        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet resultSet = null;
        try {
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_CAR_FOR_USER_PROFILE);

            if (logger.isDebugEnabled()) {
                logger.debug("Calling Stored Procedure: " + GET_CAR_FOR_USER_PROFILE);
            }

            int idx = 1;
            if (userProfileId != null) {
                stmt.setBigDecimal(idx++, new BigDecimal(userProfileId));

                // execute the stored procedure
                stmt.execute();
                resultSet = stmt.getResultSet();

                if (resultSet != null) {
                    while (resultSet.next()) {
                        carnameList.add(resultSet.getString(CLIENT_ACCOUNT_REP_NAME));
                    }
                }
            }
        } catch (SQLException e) {
            throw new SystemException(e, className, "getReportData",
                    "Problem occurred during GET_CAR_FOR_USER_PROFILE stored proc call. UserProfile provided: "
                            + userProfileId);
        } finally {
            close(stmt, conn);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getCarForUserProfile");
        }
        return carnameList;
    }
    

}
