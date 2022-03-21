package com.manulife.pension.ps.service.report.census.dao;

/*
 *
 * This is the DAO for EmployeeStatusUpdateDelete page
 */


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.RowSet;

import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeStatusHistoryDetails;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeStatusHistoryReportData;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
/**
 * DAO used by the Deferral page
 * 
 * @author patuadr
 *
 */
public class EmployeeStatusHistoryDAO extends BaseDatabaseDAO {

    private static final String className = EmployeeStatusHistoryDAO.class.getName();

    private static final Logger logger = Logger.getLogger(EmployeeStatusHistoryDAO.class);
    
    private static final String PARAMETER_NAME = "EMPLOYMENT_STATUS";
    
    private static final String STATUS_HISTORY_COUNT_NON_CANCELLED = "SELECT COUNT (*) ROWCOUNT FROM "+ CUSTOMER_SCHEMA_NAME 
    + "EMPLOYEE_VESTING_PARAMETER "
    + " WHERE PARAMETER_VALUE <> 'C' "
    + " AND CONTRACT_ID = ? "
    + " AND PROFILE_ID = ? " 
    + " AND PARAMETER_NAME = ? ";
    private static final String STATUS_HISTORY_COUNT_CANCELLED = "SELECT COUNT (*) ROWCOUNT FROM "+ CUSTOMER_SCHEMA_NAME 
    + "EMPLOYEE_VESTING_PARAMETER "
    + " WHERE PARAMETER_VALUE = 'C' "
    + " AND CONTRACT_ID = ? "
    + " AND PROFILE_ID = ? " 
    + " AND PARAMETER_NAME = ? ";
    
    private static final String STATUS_HISTORY_COUNT = "SELECT COUNT (*) ROWCOUNT FROM "+ CUSTOMER_SCHEMA_NAME 
    + "EMPLOYEE_VESTING_PARAMETER "
    + " WHERE CONTRACT_ID = ? "
    + " AND PROFILE_ID = ? " 
    + " AND PARAMETER_NAME = ? ";
       
	private final static String GET_EMPLOYEMENT_STATUS= "SELECT EFFECTIVE_DATE, "
	+ " PARAMETER_VALUE STATUS, "
	+ " LAST_UPDATED_USER_ID, "
	+ " LAST_UPDATED_TS, "
	+ " LAST_UPDATED_USER_ID_TYPE, "
	+ " SOURCE_CHANNEL_CODE "
	+ " FROM EZK100.EMPLOYEE_VESTING_PARAMETER "
        + " WHERE PROFILE_ID = ? "
        + " AND CONTRACT_ID = ? "
        + " AND PARAMETER_NAME = ? "
        + " ORDER BY EFFECTIVE_DATE DESC FOR FETCH ONLY";    
    
	
	// stored procedure to delete employment statuses from employee_vesting_parameter
    // update employee_contract status info and history
    private final static String CALL_SQL_DELETE_EMPLOYMENT_STATUS =
		"call " + CUSTOMER_SCHEMA_NAME  + "DELETE_EMPLOYMENT_STATUS (?, ?, ?, ?, ?, ?, ?, ?)";

	
	
	
	
	
	
	private static final Map<String, String> fieldToColumnMap = new HashMap<String, String>();
    /**
     * Make sure nobody instanciates this class.
     */ 
    private EmployeeStatusHistoryDAO() {
    }
    
    /**
     * getStatusHistoryCount
     * 
     * final int contractId, final Long profileId
     * @return ReportData
     * 
     * @throws SystemException
     */ 
    public static int getStatusHistoryCount(final int contractId, final Long profileId)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getStatusHistoryCount");
            logger.debug(" contractId -> " +  contractId + " profileId " +profileId);
        }
        int count =0;
        Connection conn = null;
        PreparedStatement stmt = null;
        EmployeeStatusHistoryReportData psReportDataVO = null;
        EmployeeStatusHistoryDetails detailsVO = null;
        ResultSet rs = null;
        
        
        try {   
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareStatement(STATUS_HISTORY_COUNT);
            stmt.setInt(1,contractId);
            stmt.setLong (2,profileId);
            stmt.setString(3, PARAMETER_NAME);
            rs = stmt.executeQuery();
         
            if (rs.next()) {
            	count =rs.getInt("ROWCOUNT");
            }
            
        } catch (SQLException e) {
            throw new SystemException(e, className, "getStatusHistoryCount", 
            "Problem occurred during STATUS_HISTORY_COUNT stored proc call " );
         } finally {            
             close(stmt, conn);
         }

         if (logger.isDebugEnabled()) {
             logger.debug("exit <- getStstusHistoryCount");
         }
        return count;
    }
    /**
     * getStatusHistoryCountExt - excluding records in cancelled status
     * 
     * final int contractId, final Long profileId
     * @return ReportData
     * 
     * @throws SystemException
     */ 
    public static int getStatusHistoryCountNonCancelled (final int contractId, final Long profileId)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getStatusHistoryCount");
            logger.debug(" contractId -> " +  contractId + " profileId " +profileId);
        }
        int count =0;
        Connection conn = null;
        PreparedStatement stmt = null;
        EmployeeStatusHistoryReportData psReportDataVO = null;
        EmployeeStatusHistoryDetails detailsVO = null;
        ResultSet rs = null;
        
        
        try {   
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareStatement(STATUS_HISTORY_COUNT_NON_CANCELLED);
            stmt.setInt(1,contractId);
            stmt.setLong (2,profileId);
            stmt.setString(3, PARAMETER_NAME);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	count =rs.getInt("ROWCOUNT");
            }
            
        } catch (SQLException e) {
            throw new SystemException(e, className, "getStatusHistoryCount", 
            "Problem occurred during STATUS_HISTORY_COUNT stored proc call " );
         } finally {            
             close(stmt, conn);
         }

         if (logger.isDebugEnabled()) {
             logger.debug("exit <- getStstusHistoryCount");
         }
        return count;
    }
    
    /**
     * getStatusHistoryCountExt - excluding records in cancelled status
     * 
     * final int contractId, final Long profileId
     * @return ReportData
     * 
     * @throws SystemException
     */ 
    public static int getStatusHistoryCountCancelled (final int contractId, final Long profileId)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getStatusHistoryCount");
            logger.debug(" contractId -> " +  contractId + " profileId " +profileId);
        }
        int count =0;
        Connection conn = null;
        PreparedStatement stmt = null;
        EmployeeStatusHistoryReportData psReportDataVO = null;
        EmployeeStatusHistoryDetails detailsVO = null;
        ResultSet rs = null;
        
        
        try {   
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareStatement(STATUS_HISTORY_COUNT_CANCELLED);
            stmt.setInt(1,contractId);
            stmt.setLong (2,profileId);
            stmt.setString(3, PARAMETER_NAME);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	count =rs.getInt("ROWCOUNT");
            }
            
        } catch (SQLException e) {
            throw new SystemException(e, className, "getStatusHistoryCount", 
            "Problem occurred during STATUS_HISTORY_COUNT stored proc call " );
         } finally {            
             close(stmt, conn);
         }

         if (logger.isDebugEnabled()) {
             logger.debug("exit <- getStstusHistoryCount");
         }
        return count;
    }
    
    /**
     * Get report data.
     * 
     * @param criteria ReportCriteria
     * @return ReportData
     * 
     * @throws SystemException
     */ 
    public static ReportData getReportData(final ReportCriteria criteria)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getReportData");
            logger.debug("Report criteria -> " + criteria.toString());
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        EmployeeStatusHistoryReportData psReportDataVO = null;
        EmployeeStatusHistoryDetails detailsVO = null;
        List <EmployeeStatusHistoryDetails> details= null;
        int contractNumber = 0;
        long profileId =0;
        ResultSet rs = null;
            
        try {   
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            StringBuffer sql = new StringBuffer (GET_EMPLOYEMENT_STATUS);
            /* to do - implement sort
            Iterator sorts = criteria.getSorts().iterator();
            String sortDirection = null;  
            ReportSort sort = (ReportSort)sorts.next();           
            sortDirection = sort.getSortDirection();
            sql.append(sortDirection);
            */
            stmt = conn.prepareStatement(sql.toString());
            
            if (logger.isDebugEnabled()) {
                logger.debug("Calling Stored Procedure: " + sql);
            } 
            // get profileId from criteria
            profileId = (new Long((String) criteria.getFilterValue(
                    EmployeeStatusHistoryReportData.FILTER_PROFILE_ID))).longValue();
            stmt.setLong(1, profileId);
            // get contract number from criteria
            contractNumber = (new Integer((String) criteria.getFilterValue(
                    EmployeeStatusHistoryReportData.FILTER_CONTRACT_NUMBER))).intValue();
            stmt.setInt(2, contractNumber);
            
            stmt.setString(3, PARAMETER_NAME);
   
           rs = stmt.executeQuery();
          
           details = new ArrayList<EmployeeStatusHistoryDetails>();
           int recordCount = 0;
           int cancelledCount =0;
           while(rs.next()){
        	   String status = rs.getString(EmployeeStatusHistoryReportData.STATUS_COLUMN);
        	   detailsVO = new EmployeeStatusHistoryDetails ();
        	   detailsVO.setEffectiveDate(rs.getDate(EmployeeStatusHistoryReportData.EFFECTIVE_DATE_COLUMN));
        	   if(status !=null && status.length() >0)
        		   detailsVO.setStatus(status.trim());
        	   detailsVO.setLastUpdatedUserId(rs.getString(EmployeeStatusHistoryReportData.LAST_UPDATED_USER_ID_COLUMN));
        	   detailsVO.setLastUpdatedTs(rs.getDate(EmployeeStatusHistoryReportData.LAST_UPDATED_TS_COLUMN));
        	   detailsVO.setLastUpdatedUserType(rs.getString(EmployeeStatusHistoryReportData.LAST_UPDATED_USER_ID_TYPE_COLUMN));
        	   detailsVO.setSourceChannelCode(rs.getString(EmployeeStatusHistoryReportData.SOURCE_CHANNEL_CODE_COLUMN));
        	   if(EmployeeStatusHistoryReportData.STATUS_CANCELLED.equalsIgnoreCase(status)){
        		   detailsVO.setCancelledStatus(true);
        		   cancelledCount++;
        	   }
        	   detailsVO.setMarkedForDeletion(false);
               details.add(detailsVO);
               recordCount++;
        	   }
           psReportDataVO = new EmployeeStatusHistoryReportData(criteria, recordCount);
            psReportDataVO.setDetails(details); 
            
            // set contractId and profileId from sort criteria
            psReportDataVO.setContractNumber(contractNumber);
            psReportDataVO.setProfileId(profileId);
            psReportDataVO.setNumberOfCancelled(cancelledCount);
            psReportDataVO.setNumberOfRecords(recordCount);

        } catch (SQLException e) {
           throw new SystemException(e, className, "getReportData", 
           "Problem occurred during GET_DEFERRAL_SUMMARY stored proc call. Report criteria:"
           + criteria);
        } finally {            
            close(stmt, conn);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getEmployeeStatusHistory");
        }
        
        return psReportDataVO;

    } 
    public static void deleteEmployeeStatus(String effectiveDates, int contractNumber, Long profileId, String loginUserId, String userType, String sourceChannelCode )throws DAOException {
        String methodName = "deleteEmployeeStatus";
        String callableSQL = null;
        CallableStatement stmt = null;

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> " + methodName + " (profileId" + profileId + " contractNumber " + contractNumber + " loginUser " +loginUserId +")");
        }
            Connection conn = getConnection();
            try {
               int	index =1;
                stmt = conn.prepareCall(CALL_SQL_DELETE_EMPLOYMENT_STATUS);
     			stmt.setString(index++, loginUserId.trim()); 
     			stmt.setLong(index++,profileId);
     			stmt.setInt(index++, contractNumber);
     			stmt.setString(index++, userType.trim());
     			stmt.setString (index++, "UNS");
     			stmt.setString (index++, sourceChannelCode.trim());
     			stmt.setString (index++, effectiveDates.trim());
     			stmt.registerOutParameter (index, Types.INTEGER);	
     			stmt.executeUpdate();
     			int result1 = stmt.getInt(index);

            } catch (SQLException e) {
                throw new DAOException(methodName + ":: Problem occurred during update of  "
                        + "CALL_SQL_DELETE_EMPLOYMENT_STATUS. Input parameters are: loginUser="
                        + loginUserId + " contractNumber " + contractNumber + " profileId " + profileId + "\n CallableSQL (for stored proc): " + CALL_SQL_DELETE_EMPLOYMENT_STATUS, e);
            } finally {
                close(stmt, conn);
            }
    }
	/**
     * Close for the Statement.
     * 
     * @param stmt
     */
	protected static void close(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException ignore) {
		}
	}

	/**
	 * Close for the ResultSet.
	 * 
	 * @param rs
	 */
	protected static void close(ResultSet rs) {
		try {
			if(rs!= null) { rs.close(); }
		}
		catch(SQLException ignore) {
		}
	}



/** 
 * Wrap the base connection.
 * 
 * @return
 * @throws DAOException
 */
protected static Connection getConnection() throws DAOException {
	Connection connection = null;

    try {
        connection = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
    } catch (Exception e) {
        throw new DAOException("getConnection:: Error in retrieving DB connection", e);
    }

    return connection;
}
}
