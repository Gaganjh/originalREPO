package com.manulife.pension.ps.service.report.participant.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.valueobject.TaskCenterCommonReportData;
import com.manulife.pension.ps.service.report.participant.valueobject.TaskCenterTasksDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.TaskCenterTasksReportData;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.StaticHelperClass;

public class TaskCenterTasksDAO  extends BaseDatabaseDAO {
	private static final String className = TaskCenterTasksDAO.class.getName();
    private static final Logger logger = Logger.getLogger(TaskCenterTasksDAO.class);

    private static final Map<String, String> fieldToColumnMap = new HashMap<String, String>();
    private static final Map<String, String[]> fieldSortingMap = new HashMap<String, String []>();
    
    
    static { 
    	// all sortable fields
    	fieldToColumnMap.put(TaskCenterTasksReportData.LAST_NAME_FIELD, "EC.LAST_NAME");
    	fieldToColumnMap.put(TaskCenterTasksReportData.DIVISION_FIELD, "EC."+TaskCenterTasksReportData.DIVISION_COLUMN);
    	fieldToColumnMap.put(TaskCenterTasksReportData.TYPE_FIELD, TaskCenterTasksReportData.CONTRIB_INSTRUCT_SRC_CODE);
    	fieldToColumnMap.put(TaskCenterTasksReportData.INITIATED_FIELD, "PCI."+TaskCenterTasksReportData.INITIATED_COLUMN);
    	fieldToColumnMap.put(TaskCenterTasksReportData.ANV_DATE_FIELD, "PCI."+TaskCenterTasksReportData.ANV_DATE_COLUMN);
    	fieldToColumnMap.put(TaskCenterTasksReportData.EFFECTIVE_DATE_FIELD, "PCI."+TaskCenterTasksReportData.EFFECTIVE_DATE_COLUMN);
    	
    	// what fields to sort by for a give user selection
    	fieldSortingMap.put(TaskCenterTasksReportData.LAST_NAME_FIELD, new String[] {"lastName", "initiated"}); 
    	fieldSortingMap.put(TaskCenterTasksReportData.DIVISION_FIELD, new String[] { "division", "lastName", "initiated" } );
    	fieldSortingMap.put(TaskCenterTasksReportData.TYPE_FIELD, new String[] { "type", "lastName", "initiated" } );
    	fieldSortingMap.put(TaskCenterTasksReportData.INITIATED_FIELD, new String[] { "initiated", "lastName" } );	
    	fieldSortingMap.put(TaskCenterTasksReportData.ANV_DATE_FIELD, new String[] { "anniversary", "lastName"} );
    	fieldSortingMap.put(TaskCenterTasksReportData.EFFECTIVE_DATE_FIELD, new String[] { TaskCenterTasksReportData.EFFECTIVE_DATE_FIELD, TaskCenterTasksReportData.LAST_NAME_FIELD} ); 
    }
    
    
    private static final String GET_TASK_CENTER_DEFERRAL_SUMMARY =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_TASK_CENTER_DEFERRAL_SUMMARY(?,?,?,?,?,?,?,?,?,?,?,?)}";
	
    
	public TaskCenterTasksDAO() {
	}
	
	
	public static ReportData getReportData(final ReportCriteria criteria) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
			logger.debug("Report criteria -> " + criteria.toString());
		}

		try {
			TaskCenterTasksReportData tcdsrd;
			// OB3
		//	Integer contractId = (Integer)criteria.getFilterValue(TaskCenterTasksReportData.FILTER_CONTRACTID); 
		  //  if (contractId == null) {
		 //   	// Null contract implies that the current date is not in the Ad-hoc Freeze Period
		 //   	tcdsrd = new TaskCenterTasksReportData(criteria, 0);
		  //  	tcdsrd.setDetails(new LinkedList<TaskCenterTasksDetails>());
		//	    tcdsrd.setTotalCount(0);
		//    } else {
				StoredProcOutput output = fetchData(criteria);
	
			    tcdsrd = new TaskCenterTasksReportData(criteria, output.getCount());
	
			    tcdsrd.setDetails(output.getDataList());
			    tcdsrd.setTotalCount(output.getCount());
		  //  }
		    
		    return tcdsrd;  	
		} catch(SQLException se) {
			throw new SystemException(se, "SQLException generated:"+se.getMessage());
		} 		
	}
	
	
	private static StoredProcOutput fetchData(final ReportCriteria criteria) throws SQLException, SystemException  {
  		Integer contractId = (Integer)criteria.getFilterValue(TaskCenterTasksReportData.FILTER_CONTRACTID);      		
		String lastNameValue = (String)criteria.getFilterValue(TaskCenterTasksReportData.FILTER_LAST_NAME);
		String ssnValue = (String)criteria.getFilterValue(TaskCenterTasksReportData.FILTER_SSN);
		String division = (String)criteria.getFilterValue(TaskCenterTasksReportData.FILTER_DIVISION);
		String profileId = (String)criteria.getFilterValue(TaskCenterTasksReportData.FILTER_PROFILE_ID);
		Timestamp processedTS = (Timestamp)criteria.getFilterValue(TaskCenterTasksReportData.FILTER_PROCESSED_TS);
	    String externalUserView = (String)criteria.getFilterValue(TaskCenterCommonReportData.FILTER_EXTERNAL_USER_VIEW); // [5.7]
	    Boolean isAdhocFreezePeriod = (Boolean)criteria.getFilterValue(TaskCenterCommonReportData.ADHOC_FREZZE_PERIOD); // [5.7]
	    
	    // OB3
	    String autoOrSignup = ContractServiceDelegate.getInstance().determineSignUpMethod(contractId);
  		      				
		Connection conn = null;
		CallableStatement stmt = null;
      	ResultSet resultSet = null;
      	StoredProcOutput output = null;
	    try {
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_TASK_CENTER_DEFERRAL_SUMMARY);

            String sortOrder = createSortPhrase(criteria);
            
	    	stmt.setInt(1, contractId.intValue());
	    	stmt.setString(2, lastNameValue);
	    	stmt.setString(3, ssnValue);
	    	stmt.setString(4, division);
	    	stmt.setString(5, profileId);
	    	stmt.setString(6, sortOrder);
            stmt.setInt(7, criteria.getStartIndex());
            stmt.setInt(8, criteria.getPageSize());
            stmt.setTimestamp(9, processedTS);
            stmt.setString(10, externalUserView);
            
            if(isAdhocFreezePeriod) {
            	stmt.setString(11, "Y");
            } else {
            	stmt.setString(11, "N");
            }
            
            stmt.registerOutParameter(12, java.sql.Types.INTEGER); 	// count
            
	    	stmt.execute();
	    	resultSet = stmt.getResultSet();
	    	
	    	List dataList = new LinkedList<TaskCenterTasksDetails>();
	    	
	    	if (resultSet != null) {
	    	    
    	    	while(resultSet.next()) {
    	    		dataList.add(readRecord(resultSet, autoOrSignup));
    	    	}
    	    	
	    	}
	    	
	    	
	    	output =  new StoredProcOutput(dataList, stmt.getInt(12));
	    } finally {
	        if (resultSet !=null) resultSet.close();
	        if (stmt !=null) stmt.close();
	        if (conn !=null) conn.close();
	    }
		return output;
	}
	
	
	
	private static String createSortPhrase(ReportCriteria criteria) {
	    StringBuffer result = new StringBuffer();	    
	    Iterator sorts = criteria.getSorts().iterator();
        
        ReportSort sort = (ReportSort)sorts.next();
        String selectedField = sort.getSortField();
        String sortDirection = sort.getSortDirection();
        
        // special case where complex expression in stored proc used.
        if ("default".equals(selectedField)) return null;
        
        String[] sortFields = fieldSortingMap.get(selectedField);
        
        for(int i=0; i < sortFields.length; i++) {
        	result.append(fieldToColumnMap.get(sortFields[i])).append(' ');           
        	result.append(sortDirection);
        	
            // special case
            if ("lastName".equals(sortFields[i])) {
            	result.append(", EC.FIRST_NAME ").append(sortDirection); 
            	result.append(", EC.MIDDLE_INITIAL ").append(sortDirection);
            }
            result.append(',');
        }
                
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
	}
	
	
	private static TaskCenterTasksDetails readRecord(ResultSet resultSet, String autoOrSignup) throws SQLException {
		
		TaskCenterTasksDetails taskCenterTasksDetails = new TaskCenterTasksDetails(
				  resultSet.getString(TaskCenterTasksReportData.PROFILE_ID_COLUMN),
				  resultSet.getString(TaskCenterTasksReportData.LAST_NAME_COLUMN),
				  resultSet.getString(TaskCenterTasksReportData.FIRST_NAME_COLUMN),
				  resultSet.getString(TaskCenterTasksReportData.MIDDLE_INITIAL_COLUMN),
				  resultSet.getString(TaskCenterTasksReportData.SSN_COLUMN),
				  resultSet.getString(TaskCenterTasksReportData.DIVISION_COLUMN),
				  readTimestampField(resultSet.getTimestamp(TaskCenterTasksReportData.CREATED_COLUMN)),
				  resultSet.getString(TaskCenterTasksReportData.CONTRIB_INSTRUCT_SRC_CODE),
				  resultSet.getBigDecimal(TaskCenterTasksReportData.CONTRIB_AMT_COLUMN),
				  resultSet.getBigDecimal(TaskCenterTasksReportData.CONTRIB_PCT_COLUMN),
				  resultSet.getBigDecimal(TaskCenterTasksReportData.CONTRIB_OLD_AMT_COLUMN),
				  resultSet.getBigDecimal(TaskCenterTasksReportData.CONTRIB_OLD_PCT_COLUMN),
				  resultSet.getBigDecimal(TaskCenterTasksReportData.INCREASE_AMT_COLUMN),
				  resultSet.getBigDecimal(TaskCenterTasksReportData.INCREASE_PCT_COLUMN),
				  resultSet.getString(TaskCenterTasksReportData.MONEY_TYPE_CODE_COLUMN),
				  readDateField(resultSet.getDate(TaskCenterTasksReportData.ANV_DATE_COLUMN)),
				  resultSet.getString(TaskCenterTasksReportData.EMPLOYEE_ID_COLUMN),
				  resultSet.getString(TaskCenterTasksReportData.PROCESSED_STATUS_COLUMN),
				  resultSet.getInt(TaskCenterTasksReportData.INSTRUCTION_NO_COLUMN),
				  resultSet.getInt(TaskCenterTasksReportData.COUNTER_COLUMN),
				  resultSet.getString(TaskCenterTasksReportData.PARTICIPANT_STATUS_CODE_COLUMN),
				  resultSet.getBigDecimal(TaskCenterTasksReportData.PARTICIPANT_BALANCE_COLUMN),
				  resultSet.getString(TaskCenterTasksReportData.CREATED_USER_ID_TYPE_COLUMN),
				  resultSet.getString(TaskCenterTasksReportData.CREATED_FIRST_NAME_COLUMN),
				  resultSet.getString(TaskCenterTasksReportData.CREATED_LAST_NAME_COLUMN),
				  resultSet.getString(TaskCenterTasksReportData.CREATED_FIRST_NAME_COLUMN2),
				  resultSet.getString(TaskCenterTasksReportData.CREATED_LAST_NAME_COLUMN2),
				  resultSet.getString(TaskCenterTasksReportData.CREATED_NAME3), 
				  autoOrSignup,
				  resultSet.getDate(TaskCenterTasksReportData.EFFECTIVE_DATE_COLUMN));		
		return taskCenterTasksDetails;
	}
	
	// try to tolerate bad data a bit better
	private static long readTimestampField(Timestamp field) {
		if (field !=null) {
			return field.getTime();
		} else {
			return 0;
		}
	}
	
	// try to tolerate bad data a bit better
	private static long readDateField(Date field) {
		if (field !=null) {
			return field.getTime();
		} else {
			return 0;
		}
	}
	
	
	
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
