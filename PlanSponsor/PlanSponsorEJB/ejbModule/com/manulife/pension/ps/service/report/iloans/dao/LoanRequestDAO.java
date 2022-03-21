package com.manulife.pension.ps.service.report.iloans.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.iloans.valueobject.LoanRequestDetailVO;
import com.manulife.pension.ps.service.report.iloans.valueobject.LoanRequestReportData;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.utility.ConversionUtility;

/**
 * This is the DAO for Participant Addresses
 * 
 * @author stoicsi
 */
public class LoanRequestDAO extends BaseDatabaseDAO {

	private static final String className = LoanRequestDAO.class.getName();
    private static final Logger logger = Logger.getLogger(LoanRequestDAO.class);
	
	private static final String GET_UNEXPIRED_LOAN_REQUEST_LIST =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_UNEXPIRED_LOAN_REQUEST(?,?,?,?,?,?,?)}";
	
    private static final String GET_CONTRACT_LIST =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_CONTRACT_LIST(?,?,?,?,?,?,?,?,?,?)}";

	private static final Map fieldToColumnMap = new HashMap();
	private static final String AND = " and ";
	

		
	// Make sure nobody instanciates this class
	private LoanRequestDAO() {
	}
	
	static {
		fieldToColumnMap.put("contractName", "contract_name");
		fieldToColumnMap.put("contractNumber", "contract_id");
		fieldToColumnMap.put("participantName", "first_name");
		fieldToColumnMap.put("ssn","social_security_no");
		fieldToColumnMap.put("requestDate","req_date");
		fieldToColumnMap.put("status","status");
		fieldToColumnMap.put("initiatedBy","initiate_by");	
	}
	
	public static ReportData getReportData(ReportCriteria criteria) throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getLoanRequestList");
			logger.debug("Report criteria -> " + criteria.toString());
		}
			
		Connection conn = null;
       	CallableStatement stmt = null;
       	ResultSet resultSet = null;
       	LoanRequestReportData vo = null;
       	
       	String contractList = getContractList(criteria);

       	if (contractList != null) {
       	    try {	
       	        // setup the connection and the statement
       	        conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
       	        stmt = conn.prepareCall(GET_UNEXPIRED_LOAN_REQUEST_LIST);
       	        
       	        if (logger.isInfoEnabled()) {
       	            logger.debug("Calling Stored Procedure: "+ GET_UNEXPIRED_LOAN_REQUEST_LIST);
       	        }				
       	        
       	        stmt.setString(1, contractList );
       	        String sortPhrase = createSortPhrase(criteria);
       	        if (logger.isDebugEnabled()) 
       	            logger.debug("Sort phrase: "+ sortPhrase);

       	        stmt.setString(2, sortPhrase);			// sort order
       	        if (criteria.getPageSize() == -1)
       	            stmt.setNull(3, Types.DECIMAL);					// page size
       	        else
       	            stmt.setInt(3, criteria.getPageSize());					// page size
       	        
       	        stmt.setInt(4, criteria.getStartIndex()); 			    // start index	
       	        
       	        // register the output parameters
       	        stmt.registerOutParameter(5, Types.DECIMAL);        	// version
       	        
       	        // register the output parameters
       	        stmt.registerOutParameter(6, java.sql.Types.DECIMAL);   // reasonCode

       	        // register the output parameters
       	        stmt.registerOutParameter(7, java.sql.Types.INTEGER);   // totalCount

       	        // execute the stored procedure
       	        stmt.execute();
       	        resultSet = stmt.getResultSet();
       	        
       	        BigDecimal reasonCode = stmt.getBigDecimal(6);
       	        
       	        if (reasonCode.compareTo(new BigDecimal("0")) > 0 ) {
       	            vo.setDetails(new ArrayList());
       	        } else {
       	        
       	            // set the attributes in the value object
       	            vo = new LoanRequestReportData(criteria, stmt.getInt(7));
       	            
       	            List loanRequests = new ArrayList();
       	            
       	            if (resultSet != null) {
       	                
           	            while (resultSet.next()) {
           	                LoanRequestDetailVO item = new LoanRequestDetailVO(
           	                        resultSet.getString("loan_request_id"),
           	                        resultSet.getString("profile_id"),
           	                        resultSet.getString("contract_id"),
           	                        resultSet.getString("status"),
           	                        resultSet.getDate("req_date"),
           	                        resultSet.getString("contract_name"),
           	                        resultSet.getString("first_name"),
           	                        resultSet.getString("last_name"),
           	                        resultSet.getString("social_security_no"),
           	                        resultSet.getString("initiate_by"),
           	                        resultSet.getString("loan_request_status_code"));
           	                
           	                loanRequests.add(item);			
           	            }
           	            
       	            }
       	            
       	            vo.setDetails(loanRequests);
       	        }
       	        
       	    } catch (SQLException e) {
       	        throw new SystemException(e, className, "getReportData", "Problem occurred during GET_UNEXPIRED_LOAN_REQUEST stored proc call.");
       	    } finally {
       	        close(stmt, conn);
       	    }
       	} else {
   	        vo = new LoanRequestReportData(criteria, 0);
   	        List loanRequests = new ArrayList();				
   	        vo.setDetails(loanRequests);
       	}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getLoanRequestList");
		}
		return vo;
	}
	
	private static String createSortPhrase(ReportCriteria criteria) {
		StringBuffer result = new StringBuffer();
		Iterator sorts = criteria.getSorts().iterator();
		String sortDirection = null;
		String orjSortDirection = null;		

		for (int i=0; sorts.hasNext(); i++) {
			ReportSort sort = (ReportSort)sorts.next();
			
			if (i==0) sortDirection = sort.getSortDirection();

			result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');			
			result.append(sortDirection);
			
			if ("firstName".equals(sort.getSortField()))
				result.append(", last_name ").append(sortDirection);					
				
			if ("status".equals(sort.getSortField()) ||
			    "initiatedBy".equals(sort.getSortField())) {
			    result.append(", req_date ").append(sortDirection);
			}
			result.append(',');
		}
		
		if (result.length() > 0) {
			result.deleteCharAt(result.length() - 1);
		}
		return result.toString();
	}
        
	private static String getContractList(ReportCriteria criteria) throws SystemException {
			
	    if(logger.isDebugEnabled()) {
	        logger.debug("entry -> getContractList");
	    }
	    
	    Connection conn = null;
	    CallableStatement statement = null;
	    ResultSet resultSet = null;
	    StringBuffer contractList = new StringBuffer();
	    List contracts = new ArrayList();
	    
	    try {
	        // setup the connection and the statement
	        conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
	        statement = conn.prepareCall(GET_CONTRACT_LIST);
	        if(logger.isInfoEnabled()) {
	            logger.debug("Calling Stored Procedure: "+GET_CONTRACT_LIST);
	        }
	        
	        // set the input parameters
	        statement.setBigDecimal(1, convertStringToBigDecimal((String)criteria.getFilterValue(LoanRequestReportData.FILTER_PROFILE_ID),LoanRequestReportData.FILTER_PROFILE_ID, className));
	        statement.setString(2, (String)criteria.getFilterValue(LoanRequestReportData.FILTER_SITE_LOCATION));
	        statement.setNull(3, Types.CHAR);
	        statement.setNull(4, Types.INTEGER);
	        statement.setNull(5, Types.INTEGER);
	        statement.setString(6, (String)criteria.getFilterValue(LoanRequestReportData.FILTER_DI_DURATION));
	        statement.setString(7, ConversionUtility.convertToStoredProcRole((UserRole)criteria.getFilterValue(LoanRequestReportData.FILTER_USER_ROLE)));
	        statement.setNull(8, Types.CHAR);
	        
	        // register the output parameters
	        statement.registerOutParameter(9, java.sql.Types.INTEGER);
	        statement.registerOutParameter(10, Types.DECIMAL);        

	        // execute the stored procedure
	        statement.execute();
	        
	        resultSet = statement.getResultSet();
	        int totalCount = statement.getInt(9);
	        
	        if (resultSet != null) {
	            
    	        while (resultSet.next()) {
    	            String	contractId = Integer.toString(resultSet.getInt("CONTRACT_ID"));
    	            contracts.add(contractId);			
    	        }
    	        
	        }
	        
	    } catch (SQLException e) {
	        throw new SystemException(e, className, "getReportData", 
	                "Problem occurred during GET_CONTRACT_LIST stored proc call." +
	                "Input parameters are " +
	                "clientId:" + criteria.getFilterValue(LoanRequestReportData.FILTER_PROFILE_ID) +
	                ", siteLocation:" + criteria.getFilterValue(LoanRequestReportData.FILTER_SITE_LOCATION) );
	    } finally {
	        close(statement, conn);
	    }				
	    
	    if(logger.isDebugEnabled()) 
	        logger.debug("exit <- getContractList");
	    
	    if (contracts != null && contracts.size()> 0) {
	        Iterator iter = contracts.iterator();
	        contractList.append((String)iter.next());
	        
	        while(iter.hasNext()) {
	            contractList.append(",");
	            contractList.append((String) iter.next());
	        }

	        return contractList.toString();
	    } else {
	        return null;
	    }
	}	
}
