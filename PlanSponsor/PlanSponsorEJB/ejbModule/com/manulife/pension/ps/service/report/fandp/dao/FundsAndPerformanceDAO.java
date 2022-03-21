package com.manulife.pension.ps.service.report.fandp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.fandp.valueobject.SavedUserDataFactory;
import com.manulife.pension.ps.service.report.fandp.valueobject.UserSavedData;
import com.manulife.pension.ps.service.report.fandp.valueobject.UserSavedDataLists;
import com.manulife.pension.service.dao.BaseDatabaseDAO;

/**
 * Provides access to database operations for user-related Funds And Performance
 * data, such as "My Saved Lists" and "My Saved Custom Queries"
 * 
 * @author Mark Eldridge Jan 2009
 * 
 */
public final class FundsAndPerformanceDAO extends BaseDatabaseDAO {

    private static final Logger logger = Logger
            .getLogger(FundsAndPerformanceDAO.class);

    private static final String className = FundsAndPerformanceDAO.class
            .getName();

    private static final String SELECT_USER_SAVED_DATA = "call " 
    	+ BROKER_DEALER_SCHEMA_NAME + "SELECT_USER_SAVED_DATA(?, ?)";
    
    private static final String INSERT_USER_SAVED_DATA = "call "
        + BROKER_DEALER_SCHEMA_NAME + "INSERT_USER_SAVED_DATA(?, ?, ?, ?, ?, ?)";
    
    private static final String DELETE_USER_SAVED_DATA = "call "
        + BROKER_DEALER_SCHEMA_NAME + "DELETE_USER_SAVED_DATA(?, ?, ?, ?)";

    /**
     * Get the Saved Fund Lists and Saved Custom Queries from the database.
     * 
     * @return UserSavedDataLists A value object containing the two sets of
     *         saved data (lists/queries) separated into separate structures
     *         which can be accessed by id, or enumerated.
     * @throws SystemException
     */
    public static UserSavedDataLists retrieveUserSavedDataLists(
    		long profileId, String savedDataType) throws SystemException {

		if (logger.isDebugEnabled()) {
		    logger.debug(">>> inside getUserSavedDataLists()");
		}
		
		UserSavedDataLists customQueriesAndFundLists = new UserSavedDataLists();

		Connection conn = null; 
		CallableStatement statement = null; 
		ResultSet resultSet = null; 
		
		try { 
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
 
			statement = conn.prepareCall(SELECT_USER_SAVED_DATA);
			statement.setLong(1, profileId); 
			statement.setString(2, savedDataType);
			statement.execute();
			
			resultSet = statement.getResultSet(); 
			
			if (resultSet != null) {
			    
    			while (resultSet.next()) {
    				//params returned: list id, list name, String of delimited fund 
    				UserSavedData savedData = SavedUserDataFactory.getInstance().getUserSavedDataObject(resultSet);
      
    				customQueriesAndFundLists.addSavedList(savedData);
    			}
    			
			}
			
		} catch (SQLException e) { 
			throw new SystemException(e, className + "getUserSavedFundList()" + 
					"Problem occurred during GET_USER_SAVED_FUND_LIST stored proc call."); 
		} finally { 
			close(statement, conn); 
		}
  
		if (logger.isDebugEnabled()) {
				logger.debug("<<< leaving getUserSavedFundList()"); 
		}

		return customQueriesAndFundLists;
    }
    
    /**
     * Insert the user selected Fund Lists or user entered Custom Queries
     * to the database.
     * 
     * @param profileId
     * @param userSavedData
     * @param 	overwriteEisting, true if the old record is to be replaced
     * 			with the new one
     * @return 'Y if the insert record is a duplicate. 
     * 			i.e with the same name for a single user 
     * @throws SystemException
     */
    public static String insertUserDataLists(
            long profileId, UserSavedData userSavedData, boolean overwriteEisting) throws SystemException {

		if (logger.isDebugEnabled()) {
		    logger.debug(">>> inside insertUserDataLists()");
		}

		String duplicateInd = "";
		
		Connection conn = null; 
		CallableStatement statement = null; 
		
		try { 
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			
			statement = conn.prepareCall(INSERT_USER_SAVED_DATA);
			statement.setLong(1, profileId);
			statement.setString(2, userSavedData.getName()); 
			statement.setString(3, userSavedData.getListType()); 
			statement.setString(4, userSavedData.getDelimtedData()); 
			
			if (overwriteEisting) {
				statement.setString(5, "Y");
			} else {
				statement.setString(5, "N");
			}
				
			statement.registerOutParameter(6, Types.CHAR);
			
			statement.execute();

			duplicateInd = statement.getString(6);
			
		} catch (SQLException e) { 
			throw new SystemException(e, className + "insertUserDataLists()" + 
					"Problem occurred during INSERT_USER_SAVED_DATA stored proc call."); 
		} finally { 
			close(statement, conn); 
		}
  
		if (logger.isDebugEnabled()) {
				logger.debug("<<< leaving insertUserDataLists()"); 
		}

		return duplicateInd;
    }
    
    /**
     * Deletes the specified Saved Fund List or the Saved Custom Query from 
     * the database.
     *  
     * @param profileId
     * @param userSavedData
     * 
     * @throws SystemException
     */
    public static void deleteUserSavedDataLists(
            long profileId, String savedDataName, String savedDataType) throws SystemException {

		if (logger.isDebugEnabled()) {
		    logger.debug(">>> inside deleteUserSavedDataLists()");
		}

		Connection conn = null; 
		CallableStatement statement = null; 
		
		try { 
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			
			statement = conn.prepareCall(DELETE_USER_SAVED_DATA);
			statement.setLong(1, profileId);
			statement.setString(2, savedDataName); 
			statement.setString(3, savedDataType); 
			
			statement.registerOutParameter(4, Types.INTEGER);
			
			statement.execute();
			
		} catch (SQLException e) { 
			throw new SystemException(e, className + "deleteUserSavedDataLists()" + 
					"Problem occurred during DELETE_USER_SAVED_DATA stored proc call."); 
		} finally { 
			close(statement, conn); 
		}
  
		if (logger.isDebugEnabled()) {
				logger.debug("<<< leaving deleteUserDataLists()"); 
		}
    }

}
