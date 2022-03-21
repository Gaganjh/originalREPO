package com.manulife.pension.ps.web.census.util;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.dao.EmployeeStatusHistoryDAO;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeStatusHistoryDetails;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.ps.web.census.PswLastUpdatedDetailImpl;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType;
import com.manulife.pension.util.TimestampUtils;
import com.manulife.pension.util.log.EventLog;
import com.manulife.pension.util.log.EventLogFactory;
import com.manulife.pension.util.log.LogEventException;
/**
 * Helper class  for Employee Status Pages
 * 
 */
public class EmployeeStatusHistoryReportHelper {
	
protected static EmployeeServiceFacade serviceFacade = new EmployeeServiceFacade();

public static final Logger logger = Logger.getLogger(VestingHelper.class);



/**
 * deleteEmployeeStatus
 * calls EmployeeStatusDAO to delete employee statuses
 *  and update employee_contract table and change history tables
 * @param effectiveDates - undilimited string of employee_effectivedates in YYYY-MM-DD format
 * @param contractNumber
 * @param profileId
 * @param loginUserId
 * @param userType
 * @param sourceChannelCode
 * @throws SystemException
 */	
    public static void deleteEmployeeStatus(int totalRecords, String datesLog, String statusLog,String lastUpdatedTsLog, String sourceChannelLog, String userIdLog, String userTypeLog, UserProfile user, String effectiveDates, int contractNumber, Long profileId, String loginUserId, String userType, String sourceChannelCode )throws SystemException {
     	
    	try {

        	
            EmployeeStatusHistoryDAO.deleteEmployeeStatus(effectiveDates	
                    , contractNumber, profileId, loginUserId, userType, sourceChannelCode);
        

    	} catch (DAOException e) {
            throw ExceptionHandlerUtility.wrap(e);
        } catch (RuntimeException e) {
            SystemException se = new SystemException(e, 
                    "Unchecked exception occurred. Input Paramereters are "
                            + "profileId:" + profileId
                            + ", contractNumber:" + contractNumber);
            throw ExceptionHandlerUtility.wrap(se);
        }
        logDelete(totalRecords, user, profileId, datesLog, statusLog, userIdLog, userTypeLog, sourceChannelLog, lastUpdatedTsLog);      

        if (logger.isDebugEnabled())
            logger.debug("exit <- deleteEmployeeStatus"); 
 
    } 
    
    public static Employee getEmployee (String profileId, int contractId, UserProfile userProfile) {
    	
    	Employee employee = new Employee();
    	try{
    	employee = serviceFacade.getEmployee(Long.parseLong(profileId), userProfile, new Date(), true);

    	}catch (SystemException e){
    		throw ExceptionHandlerUtility.wrap(e);
    	}
        if (logger.isDebugEnabled())
            logger.debug("exit <- getEmployee");
    	return employee;
    	
    }
/**
 * getStatusHistoryCount -get count of employment status records from employee_vesting_parameter table
 * @param contractId
 * @param profileId
 * @return
 */
    public static int getStatusHistoryCount (int contractId, String profileId) {
    	
    	int count =0;
    	try{
    	count =EmployeeStatusHistoryDAO.getStatusHistoryCount(contractId, Long.parseLong(profileId));

    	}catch (SystemException e){
    		throw ExceptionHandlerUtility.wrap(e);
    	}
        if (logger.isDebugEnabled())
            logger.debug("exit <- getStatusHistoryCount");
    	return count;
    	
    }
    
    /**
     * getStatusHistoryCount -get count of employment status records from 
     * employee_vesting_parameter table, excludes records with cancelled status
     * @param contractId
     * @param profileId
     * @return
     */
        public static int getStatusHistoryCountExt (int contractId, String profileId) {
        	
        	int count =0;
        	try{
        	count = EmployeeStatusHistoryDAO.getStatusHistoryCountNonCancelled(contractId, Long.parseLong(profileId));

        	}catch (SystemException e){
        		throw ExceptionHandlerUtility.wrap(e);
        	}
            if (logger.isDebugEnabled())
                logger.debug("exit <- getStatusHistoryCount");
        	return count;
        	
        }
    
    /**
     * Copied from employeeService.jar util YearsDiscrepancyRule
     * @param earlierDate
     * @param laterDate
     * @param discrepancy
     * @return
     */
    public static boolean isValidByYearDiscrepancyRule(Date earlierDate, Date laterDate, int discrepancy) {

        if (earlierDate == null || laterDate == null) {
            return true;
        }
        return !DateUtils.addYears(earlierDate, discrepancy).after(laterDate);
    }
    /**
     * getStatusHistoryCount -get count of employment status records from 
     * employee_vesting_parameter table, excludes records with cancelled status
     * @param contractId
     * @param profileId
     * @return
     */
        public static int getStatusHistoryCountCancelled (int contractId, String profileId) {
        	
        	int count =0;
        	try{
        	count = EmployeeStatusHistoryDAO.getStatusHistoryCountCancelled(contractId, Long.parseLong(profileId));

        	}catch (SystemException e){
        		throw ExceptionHandlerUtility.wrap(e);
        	}
            if (logger.isDebugEnabled())
                logger.debug("exit <- getStatusHistoryCount");
        	return count;
        	
        }
        
        public static String getDisplayInfo (EmployeeStatusHistoryDetails theItem, UserProfile userProfile){
        PswLastUpdatedDetailImpl auditInfo = null;
       
        String displayInfo = null;
        auditInfo = new PswLastUpdatedDetailImpl(TimestampUtils
                .convertToTimestamp(theItem.getLastUpdatedTs()), 
                theItem.getLastUpdatedUserId(),theItem.getLastUpdatedUserType(),
                theItem.getSourceChannelCode(), "");
        if(auditInfo!=null)
         displayInfo = VestingHelper.getVestingAuditInfo(auditInfo, userProfile, VestingType.EMPLOYMENT_STATUS);
        return (displayInfo!=null)?displayInfo:"";
        
        }
        public static void logDelete(int totalRecords, UserProfile user, Long profileId, String datesLog, String statusLog, String userIdLog, String userTypeLog, String sourceChannelLog, String lastUpdatedTsLog)
        	throws SystemException {
        	
            Date currentEffectiveDate = null;

            if (logger.isDebugEnabled())
                logger.debug("enter <- logDelete"); 
            try {
            // log activity
          //  EventLog eventLog = (EventLog) EventLogFactory.getInstance().createEventLog(
          //  EventLogFactory.DELETE_EMPLOYEE_STATUS_EVENT_LOG);
            EventLog eventLog = EventLogFactory.getInstance().createEventLog(
                    EventLogFactory.DELETE_EMPLOYEE_STATUS_EVENT_LOG, "PS");

            eventLog.setClassName("com.manulife.pension.ps.web.census.util.EmployeeStatusHistoryReportHelper");
            eventLog.setMethodName("deleteEmployeeStausHistory");
    		eventLog.setPrincipalFirstName(user.getPrincipal().getFirstName()); 	
    		eventLog.setPrincipalLastName(user.getPrincipal().getLastName()); 
    		eventLog.setPrincipalProfileId(Long.toString(user.getPrincipal().getProfileId()));
    		eventLog.setPrincipalUserName(user.getPrincipal().getUserName());
    		eventLog.setPrincipalRole(user.getPrincipal().getRole().getDisplayName());

            eventLog.setUserName(user.getName());
            // add info about who is deleting
            eventLog.addLogInfo("CONTRACT_NUMBER", user.getCurrentContract().getContractNumber());
            eventLog.addLogInfo("PROFILE_ID", profileId);
            eventLog.addLogInfo("TOTAL_RECORDS", totalRecords);// to do have to be string delemeted
            eventLog.addLogInfo("EFFECTIVE_DATE", datesLog);// to do have to be string delemeted
            eventLog.addLogInfo("STATUS", statusLog);//add
            eventLog.addLogInfo("LAST_UPDATED_TS", lastUpdatedTsLog);//add
            eventLog.addLogInfo("SOURCE_CHANNEL", sourceChannelLog);//add        
            eventLog.addLogInfo("LAST_UPDATED_ID", userIdLog);//add
            eventLog.addLogInfo("LAST_UPDATED_TYPE", userTypeLog);//add
            //add other record fields
            eventLog.log();
        
    	} catch (LogEventException e) {
            SystemException se = new SystemException(e, "com.manulife.pension.ps.web.census.util.EmployeeStatusHistoryReportHelper", "deleteUser",
                    "Problem occurred during logging while deleting the user. UserInfo:" + user);
            throw ExceptionHandlerUtility.wrap(se);
        }

        if (logger.isDebugEnabled())
            logger.debug("exit <- logDelete"); 
        }
        /**
         * Web utility returns if MarkedForDeletion checkbox checked
         * 
         * @param item
         * @return
         */
        public static String getMarkedForDeletionChecked(EmployeeStatusHistoryDetails item) {
            if (item.isMarkedForDeletion()) {
                return "checked";
            }
            
            return "";
        }
        
}
