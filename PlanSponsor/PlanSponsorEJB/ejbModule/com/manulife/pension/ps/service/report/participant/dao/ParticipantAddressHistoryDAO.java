package com.manulife.pension.ps.service.report.participant.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressHistory;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressHistoryReportData;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.util.StaticHelperClass;

/**
 * This is the DAO for Address History, calls the GET_ADDRESS_HISTORY_LIST stored procedure
 *  
 * @author Glen Lalonde 
 */
public class ParticipantAddressHistoryDAO extends BaseDatabaseDAO {

	private static final String className = ParticipantAddressHistoryDAO.class.getName();
    private static final Logger logger = Logger.getLogger(ParticipantAddressHistoryDAO.class);
	
	private static final String GET_PARTICIPANTS_ADDRESS_HISTORY_LIST =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_ADDRESS_HISTORY_LIST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	private static final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd"); // format works in sql query string

	// Make sure nobody instanciates this class
	private ParticipantAddressHistoryDAO() {
	}

	
	public static ReportData getReportData(ReportCriteria criteria) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
			logger.debug("Report criteria -> " + criteria.toString());
		}
				
	    StoredProcOutput dataOutput = fetchData(criteria);	
			    
		ParticipantAddressHistoryReportData bean =  
			 new ParticipantAddressHistoryReportData(criteria, dataOutput.getCount());
		
		Integer contractNumber = (Integer)criteria.getFilterValue(ParticipantAddressHistoryReportData.FILTER_FIELD_1);
		bean.setContractNumber(contractNumber.intValue()); 
		bean.setDetails(dataOutput.getDataList());

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}

		return bean;
	}
	

    private static StoredProcOutput fetchData(ReportCriteria criteria) throws SystemException {
    	
    	String sortField = null;
    	String sortDirection = null;
    	
    	ReportSortList sortList = criteria.getSorts();
    	if (sortList !=null) {
    		Iterator sit = sortList.iterator();
	    	if (sit.hasNext()) {
	    		ReportSort sort = (ReportSort)sit.next();
	    		sortField = sort.getSortField();
	    		sortDirection = sort.getSortDirection();
	    	}
    	}
    	
    	List<ParticipantAddressHistory> result = new LinkedList<ParticipantAddressHistory>();
		
		Connection conn = null;
		CallableStatement stmt = null;
      	ResultSet resultSet = null;
      	try {
      		Integer contractNumber = (Integer)criteria.getFilterValue(ParticipantAddressHistoryReportData.FILTER_FIELD_1);      		
    		String lastNameValue = (String)criteria.getFilterValue(ParticipantAddressHistoryReportData.FILTER_FIELD_2);
    		String ssnValue = (String)criteria.getFilterValue(ParticipantAddressHistoryReportData.FILTER_FIELD_3);
    		String status = (String)criteria.getFilterValue(ParticipantAddressHistoryReportData.FILTER_STATUS_FIELD);
    		String segment = (String)criteria.getFilterValue(ParticipantAddressHistoryReportData.FILTER_SEGMENT_FIELD);
    		String division = (String)criteria.getFilterValue(ParticipantAddressHistoryReportData.FILTER_DIVISION);
    		String externalStatusFilter = (String)criteria.getFilterValue(ParticipantAddressHistoryReportData.FILTER_STATUS_FOR_EXTERNAL_FIELD); 
      		      		
			Date fromDateAsDate = (Date)criteria.getFilterValue(ParticipantAddressHistoryReportData.FILTER_FIELD_4);
		    Date endDateAsDate = (Date)criteria.getFilterValue(ParticipantAddressHistoryReportData.FILTER_FIELD_5);
		    
		    boolean hasDate = (criteria.getFilterValue(ParticipantAddressHistoryReportData.FILTER_FIELD_4) !=null);
		    
      		String fromDate = null;
      		String endDate = null;
		    if (hasDate) {
	      		fromDate = dateFormat.format(fromDateAsDate);
	      		endDate = dateFormat.format(endDateAsDate);
		    }  		      		
      	    
      	    // setup call to stored proc
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_PARTICIPANTS_ADDRESS_HISTORY_LIST);
      	    
            stmt.setString(1, lastNameValue);
            stmt.setString(2, ssnValue);
            stmt.setString(3, status);
            stmt.setString(4, segment);
            stmt.setString(5, division);
            stmt.setString(6, externalStatusFilter);
            stmt.setString(7, fromDate);
            stmt.setString(8, endDate);
            stmt.setString(9, sortField);
            stmt.setString(10, sortDirection);
            stmt.setInt(11, contractNumber);
            stmt.setInt(12, criteria.getStartIndex());
            stmt.setInt(13, criteria.getPageSize());
            stmt.registerOutParameter(14, Types.DECIMAL);        	// version
			stmt.registerOutParameter(15, java.sql.Types.INTEGER); 	// count
            
            stmt.execute(); // execute the stored procedure
            
			resultSet = stmt.getResultSet();
			
			if (resultSet != null) {
			    
          	    while(resultSet.next()) {
          	    	result.add(readRecord(resultSet));
          	    }
          	    
			}
			
      	    return new StoredProcOutput(result, stmt.getInt(15));
      	} catch(SQLException e) {
    		if (logger.isDebugEnabled()) {
        		logger.debug("SqlException:"+e);
      		}
    		throw new SystemException(e, className, 
    				  "getReportData", 
    				  "Problem occurred during " + GET_PARTICIPANTS_ADDRESS_HISTORY_LIST +" stored proc call. Report criteria:"+criteria);
      	} finally {
      		close(stmt, conn);
      	}
    }

    	         	
    /**
     * Read an output record from the db.
     */
    private static ParticipantAddressHistory readRecord(ResultSet rs) throws SQLException {
    	
    	ParticipantAddressHistory pah =
    		 new ParticipantAddressHistory (
    				rs.getString("PROFILE_ID"),
    				rs.getString("FIRST_NAME"),
    				rs.getString("MIDDLE_INITIAL"),
    				rs.getString("LAST_NAME"),
    				rs.getString("EMPLOYER_PROVIDED_EMAIL_ADDRESS"),
    				rs.getString("ADDR_LINE1"),
    				rs.getString("ADDR_LINE2"), 
    				rs.getString("CITY_NAME"),
    				rs.getString("STATE_CODE"),
    				rs.getString("ZIP_CODE"),
    				rs.getString("COUNTRY_NAME"),
    				rs.getString("SSN"),
    				rs.getString("EMPLOYEE_ID"),
    				rs.getDate("EMPLOYEE_ADDR_EFF_TS"),
    				rs.getString("CREATED_USER_ID"),
    				rs.getString("CREATED_FIRST_NAME"),
    				rs.getString("CREATED_LAST_NAME"),
    				rs.getString("SOURCE_SYSTEM_CODE"),
    				rs.getString("CREATED_USER_ID_TYPE"),
    				rs.getString("EMPLOYER_DIVISION"),
    				rs.getBoolean("ACCOUNTHOLDER_IND")
    				// rs.getString("EMPLOYMENT_STATUS_CODE") // not used so don't read from result set
    		 );
    	 
    	return pah;
    }
	
    	                                                    	
	
	// nice wrapper on our multi value output
	private static class StoredProcOutput {
		private List dataList;
		private int count;
		
		public StoredProcOutput(List dataList, int count) {
			this.dataList = dataList;
			this.count = count;
		}
		
		public List getDataList() { return this.dataList; }
		public int getCount() { return this.count;    }
		
		public String toString() {
			return StaticHelperClass.toString(this);
		}
	}
}