/*
 * Created on May 26, 2005
 *
 * This is the DAO for EmployeeEnrollmentSummary.
 */
package com.manulife.pension.ps.service.report.census.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.reporthandler.EligibilityBusinessRulesUtil;
import com.manulife.pension.ps.service.report.census.util.PlanDataHelper;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeEnrollmentSummaryReportData;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.ps.service.report.census.valueobject.StatisticsSummary;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.util.render.DateRender;

/**
 * DAO used by the Eligibility page
 * 
 * @author patuadr
 *
 */
public class EmployeeEnrollmentSummaryDAO extends BaseDatabaseDAO {

    private static final String className = EmployeeEnrollmentSummaryDAO.class.getName();

    private static final Logger logger = Logger.getLogger(EmployeeEnrollmentSummaryDAO.class);
    
    private static final String GET_EMPLOYEE_ENROLLMENT_SUMMARY =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_EMPLOYEE_ENROLLMENT_SUMMARY(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    private static final String GET_STATISTICS_SUMMARY =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_ENROLLMENT_STATISTICS_SUMMARY(?,?,?,?,?,?,?,?,?,?,?)}";
    private static final String GET_ENROLLMENT_STATUS_CHANGES_FOR_PAST_PED = 
        "call " + CUSTOMER_SCHEMA_NAME + "GET_STATUS_CHANGES_FOR_PASSED_PED(?,?,?,?,?,?,?,?,?,?)";
    
    private static final Map<String, String> fieldToColumnMap = new HashMap<String, String>();
    private static final String AND = " and ";
    private static final String ALL = "All"; 
    //private static String PEDSortString = null;
    
    /**
     * Make sure nobody instanciates this class.
     */ 
    private EmployeeEnrollmentSummaryDAO() {
    }

    static {
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.WARNING_IND_FIELD, EmployeeEnrollmentSummaryReportData.WARNING_IND_COLUMN);
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.LAST_NAME_FIELD, EmployeeEnrollmentSummaryReportData.LAST_NAME_COLUMN);
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.SOCIAL_SECURITY_NO_FIELD, EmployeeEnrollmentSummaryReportData.SOCIAL_SECURITY_NO_COLUMN);
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.EMPLOYER_DIVISION_FIELD, EmployeeEnrollmentSummaryReportData.EMPLOYER_DIVISION_COLUMN);
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.EMPLOYEE_ID_FIELD, EmployeeEnrollmentSummaryReportData.EMPLOYEE_ID_COLUMN);
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.ENROLLMENT_STATUS_FIELD, EmployeeEnrollmentSummaryReportData.ENROLLMENT_STATUS_COLUMN);
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.ENROLLMENT_METHOD_FIELD, EmployeeEnrollmentSummaryReportData.ENROLLMENT_METHOD_COLUMN);
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.PROCESSING_DATE_FIELD, EmployeeEnrollmentSummaryReportData.PROCESSING_DATE_COLUMN);
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.ELIGIBLE_TO_ENROLL_FIELD, EmployeeEnrollmentSummaryReportData.ELIGIBLE_TO_ENROLL_COLUMN);
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.ELIGIBILITY_DATE_FIELD, EmployeeEnrollmentSummaryReportData.ELIGIBILITY_DATE_COLUMN);
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.OPT_OUT_FIELD, EmployeeEnrollmentSummaryReportData.OPT_OUT_SORT_COLUMN);        
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.DEFERRAL_PCT_FIELD, EmployeeEnrollmentSummaryReportData.DEFERRAL_PCT_COLUMN);
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.MAILING_DATE_FIELD, EmployeeEnrollmentSummaryReportData.MAILING_DATE_COLUMN);
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.LANGUAGE_IND_FIELD, EmployeeEnrollmentSummaryReportData.LANGUAGE_IND_COLUMN);
        fieldToColumnMap.put(EmployeeEnrollmentSummaryReportData.MONEY_TYPE_PED, EmployeeEnrollmentSummaryReportData.MONEY_TYPE_PED_COLUMN);
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
        CallableStatement stmt = null;
        ResultSet resultSet = null;
        EmployeeEnrollmentSummaryReportData psReportDataVO = null;
            
        try {   
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_EMPLOYEE_ENROLLMENT_SUMMARY);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Calling Stored Procedure: " + GET_EMPLOYEE_ENROLLMENT_SUMMARY);
            }
            
            boolean autoEnrollOn = ((Boolean)criteria.getFilterValue(
                    EmployeeEnrollmentSummaryReportData.FILTER_AUTO_ENROLL_ON)).booleanValue();
            boolean eligibilityCalcOn = ((Boolean)criteria.getFilterValue(
                    EmployeeEnrollmentSummaryReportData.FILTER_ELIGIBILITY_CALC_ON)).booleanValue();
            
            // get contract number from criteria
            int contractNumber = (new Integer((String) criteria.getFilterValue(
                    EmployeeEnrollmentSummaryReportData.FILTER_CONTRACT_NUMBER))).intValue();
            
            String moneyTypeFilter = (String)criteria.getFilterValue(
                    EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPE_SELECTED);
            
            stmt.setBigDecimal(1, intToBigDecimal(contractNumber));
            
			if (criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_REPORT_TYPE) != null) {
				stmt.setString(2, criteria.getFilterValue(
						EmployeeEnrollmentSummaryReportData.FILTER_REPORT_TYPE)
						.toString());
			} else {
				stmt.setNull(2, Types.VARCHAR);
			}
			
            // get filter from criteria
            String[] filterPhrases = createFilterPhrase(criteria);
            String mainFilterPhrase = filterPhrases[0];
            // String benFilterPhrase = filterPhrases[1]; // beneificiary
            
            if (logger.isDebugEnabled()) {
                logger.debug("Filter phrase: " + mainFilterPhrase);
            }
            if (mainFilterPhrase == null) {
                stmt.setNull(3, Types.VARCHAR);
            } else {
                stmt.setString(3, mainFilterPhrase);
            }
            // Deleted beneficiary filter as part of OBDS Phase 2 clean up
           /* if (benFilterPhrase == null) {
            	stmt.setNull(4, Types.VARCHAR); // beneficiary filter, auto filter by contract already.
            } else {
            	stmt.setString(4, benFilterPhrase);
            }*/
            
            // build sorting
            String sortPhrase = createSortPhrase(criteria);
            if (logger.isDebugEnabled()) {
                logger.debug("Sort phrase: " + sortPhrase);
            }
            stmt.setString(4, sortPhrase);
            
            if (criteria.getPageSize() == -1) {
                stmt.setNull(5, Types.DECIMAL);
            } else {
                stmt.setBigDecimal(5, intToBigDecimal(criteria.getPageSize()));
            } 
            stmt.setBigDecimal(6, intToBigDecimal(criteria.getStartIndex()));
                        
            Timestamp startDate = null;
            Timestamp endDate = null;
                        String fromEnroll = (String)criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_ENROLL_START);
            String toEnroll = (String)criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_ENROLL_END);

            if ((fromEnroll != null && !"".equals(fromEnroll)) || (toEnroll != null && !"".equals(toEnroll))) {
                startDate = convertDate(fromEnroll);
                endDate = convertDate(toEnroll);
            }
            
            if (startDate == null) {
                stmt.setNull(7, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(7, startDate);
            }
            if (endDate == null) {
                stmt.setNull(8, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(8, endDate);
            }
            
            Date ped = null;
            Date optoutDeadline = null;
            try {
                ped = getNextPlanEntryDate(contractNumber);
                if(ped != null) {
                    optoutDeadline = getOptOutDeadline(contractNumber, ped);
                }
            } catch (ApplicationException e1) {
                // Just log the error because it should be caused by incomplete data
                // in Contract Service Feature, but we are required to proceed
                logger.error(e1);
            }
            
            // The previous PED is not used in this call
            stmt.setNull(9, Types.DATE);
            
            if(ped != null ) {
                stmt.setDate(10, new java.sql.Date(ped.getTime()));
            } else {                
                stmt.setNull(10, Types.DATE);
            }
            String frequency = String.valueOf(
            		PlanDataHelper.getPlanEntryFrequencyForEEDEF(contractNumber));
            stmt.setString(11, frequency);
            
            // Set the flag for eligibility report
            stmt.setInt(12, 0);
            // Set the flag for past snapshot
            stmt.setInt(13, 0);            
            
            if(optoutDeadline != null ) {
                stmt.setDate(14, new java.sql.Date(optoutDeadline.getTime()));
            } else {                
                stmt.setNull(14, Types.DATE);
            }
                        
     /*
      DM
    * this section is determining and passing the in_hasLifeCycleDIO char(1) par
    * the param is used within the stored proc to determine
    * if missing birthdate warning nust be set for an employee
    * Combined Edits for Employee Data v3.13, # 24
    * The logic below is implemented within the stored proc
    * set missing birthday warning if
    * Birth Date is blank
    * AND
    * (Record is for a non-participating employee
    * OR Record is for a participating employee who was default enrolled)
    * AND 
    * One of the Contract’s default investment option funds is a Lifecycle Funds
    */
            // find out if the contract has lifecycle DIO
            boolean lifecycleDIO = ContractServiceDelegate.getInstance().contractHasLifecycleDIO(contractNumber);
            	String lifecycleDIOStr = null;
            	lifecycleDIOStr = lifecycleDIO?"Y":"N";      
            
            //DM set in_hasLifeCycleDIO char(1) parameter 
            // this is for the missing birthdate warning
              stmt.setString(15, lifecycleDIOStr); 
                       
              String fromPED = (String)criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_FROM_PED);
              String toPED = (String)criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_TO_PED);

              
            if(!StringUtils.isEmpty(fromPED))  {
            	stmt.setDate(16, new java.sql.Date(convertDate(fromPED).getTime()));
            }else {
            	stmt.setNull(16, Types.DATE);
            }
            
            if(!StringUtils.isEmpty(toPED))  {
            	stmt.setDate(17, new java.sql.Date(convertDate(toPED).getTime()));
            }else {
            	stmt.setNull(17, Types.DATE);
            }
              
            if(criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPES)!= null){
            	stmt.setString(18, criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPES).toString());
            }else{
            	stmt.setNull(18, Types.VARCHAR);
            }
            
/*            if(PEDSortString != null){
            	stmt.setString(20, PEDSortString);
            }else{
            	stmt.setNull(20, Types.VARCHAR);
            }
            PEDSortString = null;*/
            
            if(moneyTypeFilter != null){
            	stmt.setString(19, moneyTypeFilter);
            }else{
            	stmt.setNull(19, Types.VARCHAR);
            }
            
            // register the output parameters:  out_employeeCount
            stmt.registerOutParameter(20, Types.INTEGER);
            
            // register the output parameters:  out_sessionId1
            stmt.registerOutParameter(21, Types.INTEGER);
            // register the output parameters:  out_sessionId2
            stmt.registerOutParameter(22, Types.INTEGER);
            
            // register the output parameters:  out_totalEmployeeCount
            stmt.registerOutParameter(23, Types.INTEGER);
            
            // execute the stored procedure
            stmt.execute();
            resultSet = stmt.getResultSet();
            
            
            // set the attributes in the value object
            psReportDataVO = new EmployeeEnrollmentSummaryReportData(criteria, stmt.getInt(20));
            
            psReportDataVO.setTotalNumberOfEmployees(stmt.getInt(23));
            
            List<EmployeeSummaryDetails> employeeSummaryDetails = new ArrayList<EmployeeSummaryDetails>();
            LinkedList[] sponsorLookup = new LinkedList[10]; // help with lookup
            
            if (resultSet != null) {
                while (resultSet.next()) {     
                	EmployeeSummaryDetails details = fillInDataFromResultSet(resultSet, autoEnrollOn, lifecycleDIO, contractNumber);
                	addLookupItem(sponsorLookup, details);
                    employeeSummaryDetails.add(details);            
                }
            }
            
            psReportDataVO.setDetails(employeeSummaryDetails);     
            cleanStaging(stmt.getInt(21), stmt.getInt(22), conn);
            
            //Added For EC project. -- START --
            Map<String,Date> peds = new HashMap<String, Date>();
            Map<String,String> eligibiltyData = new HashMap<String, String>();
            
            if (stmt.getMoreResults()) {
            	ResultSet rsPED = stmt.getResultSet(); // out_planEntryDates ResultSet
            	if(rsPED != null){
	            	while(rsPED.next()){
	            		String profileId = rsPED.getString("profile_id");
	            		String moneyTypeId = (rsPED.getString("MONEY_TYPE_ID")== null)?"":rsPED.getString("MONEY_TYPE_ID").trim();
	            		Date eligibilityPED = rsPED.getDate("ELIGIBLE_PLAN_ENTRY_DATE");
	            		String planEntryDate = DateRender.format(rsPED.getDate("ELIGIBLE_PLAN_ENTRY_DATE"), "");
	            		String eligibilityDate = DateRender.format(rsPED.getDate("ELIGIBILITY_DATE"), "");
	            		String overrideInd = rsPED.getString("PROVIDED_ELIGIBILITY_DATE_IND");
	            		int periodOfWorkedHrs = rsPED.getInt("PERIOD_OF_WORKED_HRS");
	            		String periodOfWorkedEffDate = DateRender.format(rsPED.getDate("PERIOD_OF_WORKED_HRS_EFF_DATE"), "");
	            		String computationPeriod = rsPED.getString("COMPUTATION_PERIOD");
	            		if(computationPeriod != null){
	            		    if("I".equalsIgnoreCase(computationPeriod.trim())){
	            			computationPeriod = "Initial";
	            		    }else if("S".equalsIgnoreCase(computationPeriod.trim())){
	            			computationPeriod = "Subsequent";
	            		    }else{
	            			computationPeriod = "";
	            		    }
	            		}else{
	            		    computationPeriod = "";
	            		}
	            		String compPeriodStartDate = DateRender.format(rsPED.getDate("COMPUTATION_PERIOD_START_DATE"), "");
	            		String compPeriodEndDate = DateRender.format(rsPED.getDate("COMPUTATION_PERIOD_END_DATE"), "");
	            		String periodOfWorkHrs = "";
	            		if((periodOfWorkedEffDate == null || periodOfWorkedEffDate.length()==0 )&& periodOfWorkedHrs == 0){
	            		    periodOfWorkHrs = "";
	            		}else{
	            		    periodOfWorkHrs = Integer.toString(periodOfWorkedHrs);
	            		}
	            		
	            		String eligibilityReport = eligibilityDate+","+planEntryDate+","+overrideInd+","+compPeriodStartDate+","+compPeriodEndDate
					 					 +","+computationPeriod+","+periodOfWorkHrs+","+periodOfWorkedEffDate;
	            		
	            		peds.put(profileId+moneyTypeId, eligibilityPED);
	            		if(!eligibilityCalcOn && autoEnrollOn){
	            		    eligibilityReport = eligibilityDate+","+planEntryDate;
	            		}
	            		eligibiltyData.put(profileId+moneyTypeId, eligibilityReport);
	            	}
            	}
            }
            psReportDataVO.setPlanEntryDatesMap(peds);
            psReportDataVO.setEligibiltyReportMap(eligibiltyData);
            
            // Added For EC project. -- END --
            
        } catch (SQLException e) {
           throw new SystemException(e, className, "getReportData", 
           "Problem occurred during GET_EMPLOYEE_ENROLLMENT_SUMMARY stored proc call. Report criteria:"
           + criteria);
        } catch (ApplicationException e) {
            throw new SystemException(e, className, "getReportData", 
                    "Problem occurred retrieving Plan Entry Frequency to be passed in GET_EMPLOYEE_ENROLLMENT_SUMMARY stored proc call. Contract#: " +
                    criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_CONTRACT_NUMBER));
        } finally {            
            close(stmt, conn);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getEmployeeSummary");
        }
        
        return psReportDataVO;
    }
    
    
    private static void addLookupItem(LinkedList[] lookupList, EmployeeSummaryDetails details) {
    	int index =  Integer.parseInt(details.getProfileId().substring(details.getProfileId().length()-1));
    	if (lookupList[index] == null) {
    		lookupList[index] = new LinkedList();
    	}
    	lookupList[index].add(details);
    }
    
    /**
     * If the current date is after the Opt Out Deadline, 
     * there is no need for the snapshot from the past and the number
     * of changes returned by the GET_EMPLOYEE_ENROLLMENT_SUMMARY that is
     * presentChangedCount parameter is the one used for the number of changes
     * 
     * If the current date is before the Opt Out Deadline, than the count is retrieved
     * by running getEnrollmentStatusChangesForPastPED() for 
     * {previousOOD,  previousPED} time period
     * 
     * @return
     * @throws SystemException 
     * @throws ApplicationException 
     * @throws SQLException 
     */
    private static int getChangedCount(int contractNumber, int presentChangedCount) throws SystemException, ApplicationException, SQLException {
        int changes = 0;
        
        Date ped;
        Date optoutDeadline;
        try {
            ped = getNextPlanEntryDate(contractNumber);
            optoutDeadline = getOptOutDeadline(contractNumber, ped);
        } catch (ApplicationException e1) {
            throw new SystemException("999999");
        }
        
        // Check if the report needs the snapshot from the past
        if(new Date().after(optoutDeadline)) {
            // No need for snapshot
            changes = presentChangedCount;
        } else {
            // Snapshot needed
            Calendar cal = new GregorianCalendar();
            cal.setTime(ped);
            int freq = PlanDataHelper.getPlanEntryFrequencyForEEDEF(contractNumber);
            // Go back to the previous PED
            cal.roll(Calendar.MONTH, -freq);
            
            // Year adjustment
            if(cal.get(java.util.Calendar.MONTH)+freq > 11) {
                cal.roll(java.util.Calendar.YEAR, -1);       
            }
            
            Date previousPED = cal.getTime();               
            Date previousOOD =  getOptOutDeadline(contractNumber, previousPED);
            
            changes = getEnrollmentStatusChangesForPastPED(contractNumber,
                    previousOOD,
                    previousPED,
                    previousPED,
                    previousOOD);
        }
        
        return changes;
    }
    
    /**
     * gets enrollment status changes for past ped 
     * 
     * @throws SystemException
     */ 
    private static int getEnrollmentStatusChangesForPastPED(
             int contractNumber,             
             java.util.Date fromDate,
             java.util.Date toDate,
             java.util.Date planEntryDate,
             java.util.Date previousPED)
throws SystemException {
         if (logger.isDebugEnabled()) {
            logger.debug("entry -> getEnrollmentStatusChangesForPastPED");
         }
        
        int changes = 0;
        Connection conn = null;
        CallableStatement stmt = null;
                              
        try {   
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_ENROLLMENT_STATUS_CHANGES_FOR_PAST_PED);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Calling Stored Procedure: " + GET_ENROLLMENT_STATUS_CHANGES_FOR_PAST_PED);
            }
            
           stmt.setBigDecimal(1, intToBigDecimal(contractNumber));
           stmt.setDate(2, getSqlDate(fromDate));
           stmt.setDate(3, getSqlDate(toDate));
           stmt.setDate(4, getSqlDate(previousPED));
           stmt.setDate(5, getSqlDate(planEntryDate)); 
                        
           // register the output parameters
           stmt.registerOutParameter(6, Types.INTEGER);
           stmt.registerOutParameter(7, Types.INTEGER);
           stmt.registerOutParameter(8, Types.INTEGER);
           stmt.registerOutParameter(9, Types.INTEGER);
           stmt.registerOutParameter(10, Types.INTEGER);
         
           // execute the stored procedure
           stmt.execute();

           int  enrollmentStatusChanges = stmt.getInt(7);
           int  activelyApolloEnrolled = stmt.getInt(8);
           int  activelyWebEnrolled = stmt.getInt(9);
           int  censusFileOrPSWAdded = stmt.getInt(10);
           
           changes = changes 
               + enrollmentStatusChanges
               + activelyApolloEnrolled
               + activelyWebEnrolled
               + censusFileOrPSWAdded;
                                
        } catch (SQLException e) {
            e.printStackTrace();
           throw new SystemException(e, className, "getEnrollmentStatusChangesForPastPED", 
           "Problem occurred during GET_ENROLLMENT_STATUS_CHANGES stored proc call, input params"+contractNumber+","+fromDate+","+previousPED+","+previousPED+","+planEntryDate);
        } finally {
            close(stmt, conn);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getEnrollmentStatusChangesForPastPED");
        }
       
        return changes;

    }

    /**
     * Clean the staging data 
     * 
     * @param callSessionId
     * @param con
     * @throws SQLException
     */
    private static void cleanStaging(int callSessionId1, int callSessionId2, Connection con) throws SQLException {
        String sqlDelete = "delete from PSW100.STORED_PROCEDURE_STAGING where CALL_SESSION_ID = ?";
        
        if(callSessionId1 != 0) {            
            PreparedStatement stmtSession = con.prepareStatement(sqlDelete);
            stmtSession.setInt(1,callSessionId1);
            stmtSession.executeUpdate();
        }
        
        if(callSessionId2 != 0) {
            PreparedStatement stmtSession = con.prepareStatement(sqlDelete);
            stmtSession.setInt(1,callSessionId2);
            stmtSession.executeUpdate();
        }
    }

    /**
     * Creates a Value Object for each row returned by the stored procedure
     * 
     * @param resultSet
     * @param autoEnrollmentOn
     * @return
     * @throws SQLException
     */ 
    private static EmployeeSummaryDetails fillInDataFromResultSet(ResultSet resultSet, boolean autoEnrollmentOn, boolean lifecycleDIO, int contractNumber) throws SQLException {
    	Date mailingDateD =resultSet.getDate(EmployeeEnrollmentSummaryReportData.MAILING_DATE_COLUMN);
    	String mailingDate = DateRender.format (mailingDateD, "MM/DD/YYYY");  	
    	if (EmployeeEnrollmentSummaryReportData.PENDING_DATE.equals(mailingDate.trim()))
    		mailingDate =EmployeeEnrollmentSummaryReportData.PENDING_DISPLAY;
       	if (EmployeeEnrollmentSummaryReportData.NULL_DATE.equals(mailingDate.trim()))
    		mailingDate =EmployeeEnrollmentSummaryReportData.NULL_DATE_DISPLAY;
       	String eligibleToEnroll = resultSet.getString(EmployeeEnrollmentSummaryReportData.ELIGIBLE_TO_ENROLL_COLUMN);
       	if(eligibleToEnroll != null ){
       	    if("Y".equalsIgnoreCase(eligibleToEnroll.trim())){
       		eligibleToEnroll = "Yes";
       	    }else if("N".equalsIgnoreCase(eligibleToEnroll.trim())){
       		eligibleToEnroll = "No";
       	    }else{
       		eligibleToEnroll = "";
       	    }
       	}else{
       	    eligibleToEnroll = "";
       	}
       	    
       	boolean participantInd = false;
       	String partInd = resultSet.getString(EmployeeEnrollmentSummaryReportData.PARTICIPANT_IND_COLUMN);
       	if(partInd != null && "1".equalsIgnoreCase(partInd)){
       	    participantInd = true;
       	}
    	EmployeeSummaryDetails item = new EmployeeSummaryDetails(
            resultSet.getString(EmployeeEnrollmentSummaryReportData.FIRST_NAME_COLUMN),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.MIDDLE_INITIAL_COLUMN),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.LAST_NAME_COLUMN),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.SOCIAL_SECURITY_NO_COLUMN),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.EMPLOYEE_ID_COLUMN),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.EMPLOYER_DIVISION_COLUMN),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.ENROLLMENT_METHOD_COLUMN),                         
            resultSet.getString(EmployeeEnrollmentSummaryReportData.ENROLLMENT_STATUS_COLUMN),
            resultSet.getDate(EmployeeEnrollmentSummaryReportData.PROCESSING_DATE_COLUMN),
            eligibleToEnroll,
            resultSet.getDate(EmployeeEnrollmentSummaryReportData.ELIGIBILITY_DATE_COLUMN),
            resultSet.getBoolean(EmployeeEnrollmentSummaryReportData.OPT_OUT_SORT_COLUMN),
            getParsedPercent(resultSet.getString(EmployeeEnrollmentSummaryReportData.DEFERRAL_PCT_COLUMN)),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.BEFORE_TAX_DEFER_PCT),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.BEFORE_TAX_DEFER_AMT),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.DESIG_ROTH_DEF_PCT),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.DESIG_ROTH_DEF_AMT),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.ENROLL_BEFORE_TAX_DEFER_PCT),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.ENROLL_BEFORE_TAX_DEFER_AMT),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.ENROLL_DESIG_ROTH_DEF_PCT),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.ENROLL_DESIG_ROTH_DEF_AMT),
            resultSet.getString(EmployeeEnrollmentSummaryReportData.PROFILE_ID_COLUMN),
            participantInd,
            mailingDate,
            resultSet.getString(EmployeeEnrollmentSummaryReportData.LANGUAGE_IND_COLUMN));
                                                                                              
        item.setApplicablePlanEntryDate(resultSet.getDate(EmployeeEnrollmentSummaryReportData.APPLICABLE_PLAN_ENTRY_DATE_COLUMN));
        item.setAutoEnrollOptOutInd(resultSet.getString(EmployeeEnrollmentSummaryReportData.OPT_OUT_COLUMN));        
        item.setEmploymentStatus(resultSet.getString(EmployeeEnrollmentSummaryReportData.EMPLOYMENT_STATUS_CODE));
        item.setBirthDate(resultSet.getDate(EmployeeEnrollmentSummaryReportData.BIRTH_DATE_COLUMN));
        item.setHireDate(resultSet.getDate(EmployeeEnrollmentSummaryReportData.HIRE_DATE_COLUMN));
        item.setContributionStatus(resultSet.getString(EmployeeEnrollmentSummaryReportData.CONTRIBUTION_STATUS_COLUMN));
        item.setParticipantStatusCode(resultSet.getString(EmployeeEnrollmentSummaryReportData.PARTICIPANT_STATUS_COLUMN));
        if(item.isParticipantInd() || EmployeeEnrollmentSummaryReportData.PARTICIPANT_STATUS_OPTED_OUT.equals(item.getParticipantStatusCode()))
        	item.setShowHistoryAndNameLink(true);
        item.setWithdrawalElection90Days(resultSet.getString(EmployeeEnrollmentSummaryReportData.AE_90DAYS_OPTOUT_IND_COLUMN));
        item.setHasVestedMoneyOnly("1".equals(resultSet.getString(EmployeeEnrollmentSummaryReportData.OPT_OUT_NOT_VESTED)) ? true: false);
        // If LAST_UPDATED_TS == null there is no entry and nothing to set                    
        setOptOutHistory(resultSet, item);
        
       // DM added check for missing birth date warning
       // boolean missingBirthDateWarning =isMissingBirthDateWarning(item,lifecycleDIO );
         //service.g
        applyBusinessRulesForWarnings(item,                
                resultSet.getInt(EmployeeEnrollmentSummaryReportData.WARNING_COLUMN),
                resultSet.getInt(EmployeeEnrollmentSummaryReportData.WARNING_IND_COLUMN),
                autoEnrollmentOn);
                    
        applyBusinessRulesForOptOut(item);
        return item;
    }
    
    /**
     * Utility method that parses the String to show just the meaningful decimals 
     * 
     * @param percentage
     * @return
     */
    private static String getParsedPercent(String percentage) {
        if(percentage != null && !"".equals(percentage.trim())) {
            java.text.DecimalFormat format = new java.text.DecimalFormat("###.###");
            Number number = null;
    
            try {
                number = format.parse(percentage);
            } catch (java.text.ParseException e) {
                // Do nothing because it should not happen
                // the data comes from database and it should have been validated before
            }
    
            if(number != null) {
                return format.format(number); 
            }    
        }
        
        // Return was was retrieved from database for NULL, BLANK or un-parseable
        return percentage;
    }
    
    /**
     * Sets the type of the warning if any
     * The IDs are based on Combined Edits document
     * 
     * @param details
     * @param warningType
     * @param warningInd
     * @param autoEnrollmentOn
     */
    private static void applyBusinessRulesForWarnings(
            EmployeeSummaryDetails details, 
            int warningType, int warningInd, boolean autoEnrollmentOn) {
    	    boolean dmWarning = false;
    	    //To do - restructure the if statements
// first check the missing birthdate and missing address warnings
// the SP is supposed to check all the warning conditions, so there is no need
// for extra checks here, just look-up the warning number in
// warning type column
     	    if (warningInd!=0)
     	    	
    	    {
     	    	if (warningType ==205){ // missing birth date warning
            		details.getWarnings().setMissingBirthDateWarning();
              		details.setBirthDateStatus(EmployeeSummaryDetails.WARNING);// this is for sorting
    	    	}
            	//to do - change the addressStatus field to numeric
    	    	else if (warningType ==203){ //missing employee address warning
             		details.getWarnings().setMissingEmployeeAddressWarning();
             		details.setEmployeeAddressStatus(EmployeeSummaryDetails.WARNING);// this is for sorting
    	    	} 
    	    	
    	    	else if (warningType ==204){
            		details.getWarnings().setMissingBirthDateAndEmployeeAddressWarning();
            		details.setBirthDateStatus(EmployeeSummaryDetails.WARNING);
            		details.setEmployeeAddressStatus(EmployeeSummaryDetails.WARNING);
    	    	}
    	    // then check elegibility date and indicator 
    	    	else if (warningType == 38) {  			
    	    		if(autoEnrollmentOn && !details.isParticipantInd())                    //  CMA ID 56188
                    details.getWarnings().setEligibilityIndWarning();
    	    	} 
    	    	
    	    	else { // Unknown condition; if there is  no dio warning , assume clean
                details.getWarnings().clearAllWarnings();
            }
                
            } else {// there are no warnings
             details.getWarnings().clearAllWarnings();
        }
    }
    
    /**
     * Sets the type of the warning if any
     * The IDs are based on Combined Edits document
     * 
     * @param details
     * @param warningType
     * @param warningInd
     * @param autoEnrollmentOn
     */
    private static void applyBusinessRulesForOptOut(
            EmployeeSummaryDetails details ) {
       if("Y".equals(details.getAutoEnrollOptOutInd())
    	|| details.getParticipantStatusCode() == null 
    	|| "NT".equals(details.getParticipantStatusCode())) {
    	   
    	   details.setOptOutEditable(true);
       }
       if("Y".equals(details.getAutoEnrollOptOutInd())
    	  && details.isHasVestedMoneyOnly()
    	  && "NT".equals(details.getParticipantStatusCode())){
    	   details.setOptedOutNotVested(true);
       }
       
       //EES 23.1
       if("Y".equals(details.getAutoEnrollOptOutInd()) &&  "NT".equals(details.getParticipantStatusCode()))
       {
    	   details.setApplicablePlanEntryDate(null);
       }
 }
    
    /**
     * 
     * @param resultSet
     * @param item
     * @throws SQLException
     */
    private static void setOptOutHistory(ResultSet resultSet, EmployeeSummaryDetails item) throws SQLException {
        if(resultSet.getDate(EmployeeEnrollmentSummaryReportData.LAST_UPDATED_TS_COLUMN) != null) {
            item.getOptOutHistory().getCurrentUser().setLastName(
                    resultSet.getString(EmployeeEnrollmentSummaryReportData.CREATED_LAST_NAME_COLUMN));
            item.getOptOutHistory().getCurrentUser().setFirstName(
                    resultSet.getString(EmployeeEnrollmentSummaryReportData.CREATED_FIRST_NAME_COLUMN));
            item.getOptOutHistory().getCurrentUser().setUserIdType(
                    resultSet.getString(EmployeeEnrollmentSummaryReportData.CREATED_USER_ID_TYPE_COLUMN));
            item.getOptOutHistory().getCurrentUser().setUserId(
                    resultSet.getString(EmployeeEnrollmentSummaryReportData.CREATED_USER_ID_COLUMN));
            item.getOptOutHistory().setCurrentUpdatedTs(resultSet.getTimestamp(EmployeeEnrollmentSummaryReportData.LAST_UPDATED_TS_COLUMN));                    
            item.getOptOutHistory().setCurrentValue(resultSet.getString(EmployeeEnrollmentSummaryReportData.DATA_ELEMENT_VALUE));
            item.getOptOutHistory().setSourceChannelCode(resultSet.getString(EmployeeEnrollmentSummaryReportData.SOURCE_CHANNEL_CODE_COLUMN));                        
        }
    }
    
    
    /**
     * Converts a date string to a timestamp using the expected pattern
     * 
     * @param date
     * @return
     */
    private static Timestamp convertDate(String date) {
        if(date == null || "".equals(date)) {
            return null;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");        
        Date theDate = null;
        
        try {
            theDate = dateFormat.parse(date);
        } catch (ParseException e) {
            // It should not happen because it passed validation already
            return null;
        }
        
        return new Timestamp(theDate.getTime());
    }
        
    /**
     * Building the sort order.
     * 
     * @param criteria ReportCriteria
     * @return String
     */
    private static String createSortPhrase(ReportCriteria criteria) {
        StringBuilder sortClause = new StringBuilder();
        Iterator sorts = criteria.getSorts().iterator();
        String sortDirection = null;  
        String source = "";
        
        if(criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.SOURCE_PAGE) != null){
        	source = (String)criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.SOURCE_PAGE);
        }
        
        if("eligibilityReports".equalsIgnoreCase(source)){            
        	sortClause.append(" LAST_NAME asc , FIRST_NAME asc, MIDDLE_INITIAL asc , SOCIAL_SECURITY_NO asc ");
        }else{        
            for (int i=0; sorts.hasNext(); i++) {
                ReportSort sort = (ReportSort)sorts.next();                
                sortDirection = sort.getSortDirection();
                
                if(!sort.getSortField().startsWith("moneyType"))
                	sortClause.append(fieldToColumnMap.get(sort.getSortField())).append(' ').append(sortDirection).append(',');           

                if ("lastName".equals(sort.getSortField())) {
                	sortClause.append(" FIRST_NAME ").append(sortDirection); 
                	sortClause.append(", MIDDLE_INITIAL ").append(sortDirection).append(',');
                }
            }
            if (sortClause.length() > 0) {
            	sortClause.deleteCharAt(sortClause.length() - 1);
            }
        }
        return sortClause.toString();
    }

    /**
     * Building the where clause.
     * 
     * @param criteria ReportCriteria
     * @return String
     */
    private static String[] createFilterPhrase(final ReportCriteria criteria) {
         StringBuffer result = new StringBuffer();
         // Deleted Bene Filter as part of OBDS2 clean up
         // String benFilter= null;
         String filterPED = null;
         
         String segment = (String) criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_SEGMENT);
         String status = (String) criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_STATUS);
         String lastNameValue = (String) criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_LAST_NAME);
         String ssnValue = (String) criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_SSN);
         String divisionValue = (String)criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_DIVISION);
         String fromDate = (String)criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_FROM_PED);
         String toDate = (String)criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_TO_PED);
         String empStatus = (String)criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_EMP_STATUS);
         String fromEnroll = (String)criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_ENROLL_START);
         String toEnroll = (String)criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_ENROLL_END);
         
         if (!StringUtils.isEmpty(segment)) {
             result.append("PARTICIPANT_IND = ").append(segment).append(AND);
         } else {
             result.append(" PARTICIPANT_IND != -1  ").append(AND);
         }
         
         if (StringUtils.isEmpty(empStatus)) {
             // Exclude Cancelled employees for external users only
             if (criteria.isExternalUser()) {
                 result.append("VALUE(EMPLOYMENT_STATUS_CODE,'') != 'C'").append(AND);
             }        	 
         } else {
        	 result.append("VALUE(EMPLOYMENT_STATUS_CODE,'') = ").append(wrapInSingleQuotes(empStatus)).append(AND);
         }
         
         if (!StringUtils.isEmpty(status) && !status.equals(ALL)) {
             result.append("UPPER(ENROLLMENT_STATUS) = ").append(wrapInSingleQuotes(status).
             toUpperCase()).append(AND);
         }
         // Deleted Bene Filter as part of OBDS2 clean up     
        if (!StringUtils.isEmpty(lastNameValue)) {
             result.append("UPPER(LAST_NAME) like ").append(wrapInSingleQuotes(lastNameValue + "%").
             toUpperCase()).append(AND);
             //benFilter = "EC.LAST_NAME LIKE "+wrapInSingleQuotes(lastNameValue + "%")+" ";
         }
        
         if (!StringUtils.isEmpty(ssnValue)) {
             result.append("SOCIAL_SECURITY_NO = ").append(wrapInSingleQuotes(ssnValue)).
             append(AND);
             //benFilter = "EC.SOCIAL_SECURITY_NO = "+wrapInSingleQuotes(ssnValue)+" ";
         }
         
         if (!StringUtils.isEmpty(divisionValue)) {
             result.append("UPPER(EMPLOYER_DIVISION) like ").append(wrapInSingleQuotes(divisionValue + "%").
             toUpperCase() ).append(AND);
         }
         
         if (
        		 ((fromEnroll !=null && !"".equals(fromEnroll)) || (toEnroll !=null && !"".equals(toEnroll)))
        	) {                     
             result.append("VALUE(ENROLLMENT_PROCESSED_DATE, DATE('01/01/0001')) >= ? AND VALUE(ENROLLMENT_PROCESSED_DATE, DATE('01/01/0001')) <= ? "); 
         }
         
         if (result.length() > 0 && AND.equals(result.substring(result.length() - AND.length()))) {
             result.delete(result.length() - AND.length(), result.length());
         }
        
         String mailFilterPhrase =  (result.toString().trim().length() > 0 ? result.toString() : null);
         
         return new String[] { mailFilterPhrase /*, benFilter*/ };
    }

    /**
     * Get report data.
     * 
     * @param criteria ReportCriteria
     * @return ReportData
     * 
     * @throws SystemException
     */ 
    /*public static ReportData getOptOutReportData(final ReportCriteria criteria)
            throws SystemException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getReportData");
            logger.debug("Report criteria -> " + criteria.toString());
        }
    
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet resultSet = null;
        EmployeeEnrollmentSummaryReportData psReportDataVO = null;
        // The second HashMap is used to eliminate multiple entries for the same column name
        HashMap<String, HashMap<String, EmployeeChangeHistoryVO>> map = new HashMap<String, HashMap<String, EmployeeChangeHistoryVO>>();
            
        try {  
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_EMPLOYEE_ENROLLMENT_SUMMARY);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Calling Stored Procedure: " + GET_EMPLOYEE_ENROLLMENT_SUMMARY);
            }
            
            // get contract number from criteria
            int contractNumber = (new Integer((String) criteria.getFilterValue(
                    EmployeeEnrollmentSummaryReportData.FILTER_CONTRACT_NUMBER))).intValue();
            stmt.setBigDecimal(1, intToBigDecimal(contractNumber));
            
            if (criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_REPORT_TYPE) != null) {
				stmt.setString(2, criteria.getFilterValue(
						EmployeeEnrollmentSummaryReportData.FILTER_REPORT_TYPE)
						.toString());
			} else {
				stmt.setNull(2, Types.VARCHAR);
			}
            
            stmt.setNull(3, Types.VARCHAR);  // main Filter criteria
            stmt.setNull(4, Types.VARCHAR);  // beneficiary filter
            
            // build sorting
            String sortPhrase = createSortPhrase(criteria);
            if (logger.isDebugEnabled()) {
                logger.debug("Sort phrase: " + sortPhrase);
            }
            stmt.setString(5, sortPhrase);
            
            // Page size
            stmt.setNull(6, Types.DECIMAL);
            // Start index
            stmt.setNull(7, Types.DECIMAL);
                        
            Date ped;
            Date optoutDeadline;
            try {
                ped = getNextPlanEntryDate(contractNumber);
                optoutDeadline = getOptOutDeadline(contractNumber, ped);
            } catch (ApplicationException e1) {
                throw new SystemException("999999");
            }
            
            // Check if the report needs the snapshot from the past
            if(new Date().after(optoutDeadline)) {
                // No need for snapshot
                // Start date
                Calendar cal = new GregorianCalendar();
                cal.setTime(optoutDeadline);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                stmt.setTimestamp(8, new java.sql.Timestamp(cal.getTime().getTime()));
                // End date is PED less 1 day                
                cal.setTime(ped);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 0);
                stmt.setTimestamp(9, new java.sql.Timestamp(getOneDayBefore(cal.getTime()).getTime()));   
                
                // Previous PED is not used
                stmt.setNull(10, Types.DATE);
                // Next PED
                stmt.setDate(11, new java.sql.Date(ped.getTime()));
                stmt.setString(12, getPEF(contractNumber));
                
                // Set the flag for post opt out report
                stmt.setInt(13, 1);
                // Set the flag for past snapshot
                stmt.setInt(14, 0);
            } else {
                // Snapshot needed
                Calendar cal = new GregorianCalendar();
                cal.setTime(ped);
                int freq = getFrequency(contractNumber);
                cal.roll(Calendar.MONTH, -freq);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 0);
                
                // Year adjustment
                if(cal.get(java.util.Calendar.MONTH)+freq > 11) {
                    cal.roll(java.util.Calendar.YEAR, -1);       
                }
                
                Date previousPED = cal.getTime();  
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                Date previousOOD =  getOptOutDeadline(contractNumber, previousPED);
                
                // Start date
                stmt.setTimestamp(8, new java.sql.Timestamp(previousOOD.getTime()));
                // End date is previous PED less 1 day
                stmt.setTimestamp(9, new java.sql.Timestamp(getOneDayBefore(previousPED).getTime()));   
                
                // Previous PED 
                stmt.setDate(10, new java.sql.Date(previousPED.getTime()));
                // Next PED
                stmt.setDate(11, new java.sql.Date(ped.getTime()));
                stmt.setString(12, getPEF(contractNumber));
                
                // Set the flag for post opt out report
                stmt.setInt(13, 1);
                // Set the flag for past snapshot
                stmt.setInt(14, 1);
            }
            
            if(optoutDeadline != null ) {
                stmt.setDate(15, new java.sql.Date(optoutDeadline.getTime()));
            } else {                
                stmt.setNull(15, Types.DATE);
            }
                        
            // DM Set the flag for lifecycleDIO. setting to NUll, 
            // this is for the missing birthdate warning
            // since so the opt-out report neither display warnings
            // no sorts records by warnings,
            // we  pass null as ifecycleDIO parameter
            stmt.setString(16, null);
            
            // register the output parameters:  out_employeeCount
            stmt.registerOutParameter(17, Types.INTEGER);
            
            // register the output parameters:  out_sessionId
            stmt.registerOutParameter(18, Types.INTEGER);
            // register the output parameters:  out_sessionId
            stmt.registerOutParameter(19, Types.INTEGER);
            
            //  register the output parameters:  out_totalEmployeeCount
            stmt.registerOutParameter(20, Types.INTEGER);
            
            // execute the stored procedure
            stmt.execute();
            resultSet = stmt.getResultSet();
            
            int  totalCount = stmt.getInt(17);
            int numberOfChanges = 0;
            
//          set the attributes in the value object
            psReportDataVO = new EmployeeEnrollmentSummaryReportData(criteria, totalCount);
            
            List<EmployeeSummaryDetails> employeeSummaryDetails = new ArrayList<EmployeeSummaryDetails>();
            
            // First set - just the changes
            // The only interest are the column names that have changed for a specific profile
            resultSet = stmt.getResultSet();            
            
            if (resultSet != null) {
                while (resultSet.next()) {
                    HashMap<String, EmployeeChangeHistoryVO> changeHistory = map.get(resultSet.getString(EmployeeEnrollmentSummaryReportData.PROFILE_ID_COLUMN));
                       
                    if(changeHistory == null) {
                        changeHistory = new HashMap<String, EmployeeChangeHistoryVO>();
                        map.put(resultSet.getString(EmployeeEnrollmentSummaryReportData.PROFILE_ID_COLUMN), changeHistory);
                    }
                    
                    EmployeeChangeHistoryVO e = new EmployeeChangeHistoryVO();
                    e.setColumnName(resultSet.getString(EmployeeEnrollmentSummaryReportData.COLUMN_NAME));
                    changeHistory.put(resultSet.getString(EmployeeEnrollmentSummaryReportData.COLUMN_NAME), e);                
                }
            }
            
            // Construct a set with all profile Ids that have been returned
            Set<String> profiles = map.keySet();
                
           // beneficiary result set never resturned for opt-out report
	        if (stmt.getMoreResults()) {
	        	ResultSet rs = stmt.getResultSet(); // benificiary result set data.
	        }
            
            //  3rd - the details for each employee that had changes
            if(stmt.getMoreResults()) {
                resultSet = stmt.getResultSet();
                
                if (resultSet != null) {                    
                    while (resultSet.next()) {
                        // Check if the profile is part of the set with valid changes
                        if(!profiles.contains(resultSet.getString(EmployeeEnrollmentSummaryReportData.PROFILE_ID_COLUMN))) {
                            continue;
                        }
                        
                        EmployeeSummaryDetails item = new EmployeeSummaryDetails(
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.FIRST_NAME_COLUMN),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.MIDDLE_INITIAL_COLUMN),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.LAST_NAME_COLUMN),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.SOCIAL_SECURITY_NO_COLUMN),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.EMPLOYEE_ID_COLUMN),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.EMPLOYER_DIVISION_COLUMN),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.ENROLLMENT_METHOD_COLUMN),                         
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.ENROLLMENT_STATUS_COLUMN),
                                resultSet.getDate(EmployeeEnrollmentSummaryReportData.PROCESSING_DATE_COLUMN),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.ELIGIBLE_TO_ENROLL_COLUMN),
                                resultSet.getDate(EmployeeEnrollmentSummaryReportData.ELIGIBILITY_DATE_COLUMN),
                                resultSet.getBoolean(EmployeeEnrollmentSummaryReportData.OPT_OUT_SORT_COLUMN),
                                getParsedPercent(resultSet.getString(EmployeeEnrollmentSummaryReportData.DEFERRAL_PCT_COLUMN)),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.BEFORE_TAX_DEFER_PCT),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.BEFORE_TAX_DEFER_AMT),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.DESIG_ROTH_DEF_PCT),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.DESIG_ROTH_DEF_AMT),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.ENROLL_BEFORE_TAX_DEFER_PCT),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.ENROLL_BEFORE_TAX_DEFER_AMT),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.ENROLL_DESIG_ROTH_DEF_PCT),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.ENROLL_DESIG_ROTH_DEF_AMT),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.PROFILE_ID_COLUMN),
                                resultSet.getBoolean(EmployeeEnrollmentSummaryReportData.PARTICIPANT_IND_COLUMN),
                                resultSet.getString (EmployeeEnrollmentSummaryReportData.MAILING_DATE_COLUMN),
                                resultSet.getString(EmployeeEnrollmentSummaryReportData.LANGUAGE_IND_COLUMN));
                                                
                        item.setApplicablePlanEntryDate(resultSet.getDate(EmployeeEnrollmentSummaryReportData.APPLICABLE_PLAN_ENTRY_DATE_COLUMN));
                        item.setEmploymentStatus(resultSet.getString(EmployeeEnrollmentSummaryReportData.EMPLOYMENT_STATUS_CODE));
                        item.setAutoEnrollOptOutInd(resultSet.getString(EmployeeEnrollmentSummaryReportData.OPT_OUT_COLUMN));  
                        
                        item.setBirthDate(resultSet.getDate(EmployeeEnrollmentSummaryReportData.BIRTH_DATE_COLUMN));
                        item.setHireDate(resultSet.getDate(EmployeeEnrollmentSummaryReportData.HIRE_DATE_COLUMN));
                    
                        item.setStatusChanges(new ArrayList<EmployeeChangeHistoryVO>(map.get(item.getProfileId()).values()));                          
                        // Update number of changes
                        if(map.get(item.getProfileId()) != null && !map.get(item.getProfileId()).isEmpty()) {                                
                            numberOfChanges = numberOfChanges + map.get(item.getProfileId()).size();
                        }
                        
                        // If LAST_UPDATED_TS == null there is no entry and nothing to set
                        setOptOutHistory(resultSet, item);
                                            
                        employeeSummaryDetails.add(item);
                    }
                }
            }
            
            // Set the total number of status changes                
            psReportDataVO.setNumberOfChanges(numberOfChanges);
            
            psReportDataVO.setDetails(employeeSummaryDetails);  
            cleanStaging(stmt.getInt(18), stmt.getInt(19), conn);
        } catch (SQLException e) {
           throw new SystemException(e, className, "getReportData", 
           "Problem occurred during GET_EMPLOYEE_ENROLLMENT_SUMMARY stored proc call. Report criteria:"
           + criteria);
        } catch (ApplicationException e) {
            throw new SystemException(e, className, "getReportData", 
                    "Problem occurred retrieving Plan Entry Frequency to be passed in GET_POST_OPT_OUT_SUMMARY stored proc call. Contract#: " +
                    criteria.getFilterValue(EmployeeEnrollmentSummaryReportData.FILTER_CONTRACT_NUMBER));
        } finally {
            close(stmt, conn);
        }
    
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getEmployeeSummary");
        }
        
        return psReportDataVO;    
    }*/
    
    /**
     * Returns the date from the day before 
     * 
     * @param calDate
     * @return
     */
    private static Date getOneDayBefore(Date calDate) {  
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(calDate);        
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        
        return calendar.getTime();
    }
        
    /**
     * 
     * @param contractNumber
     * @return
     * @throws ApplicationException
     * @throws SystemException
     */
    private static Date getNextPlanEntryDate(int contractNumber) throws ApplicationException, SystemException {
        Date dateForPEDCalculation = new Date();
        // Always calculate for tomorrow, because the Auto Enrollment Periodic Process 
        // runs 1 day before PED
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(dateForPEDCalculation);        
        calendar.roll(Calendar.DAY_OF_YEAR, 1);
        
        return ContractServiceDelegate.getInstance()
                .getNextPlanEntryDate(contractNumber, calendar.getTime());
    }
    
    /**
     * 
     * @param contractNumber
     * @param nextPED
     * @return
     * @throws ApplicationException
     * @throws SystemException
     */
    private static Date getOptOutDeadline(int contractNumber, Date nextPED) throws ApplicationException, SystemException {
        ContractServiceFeature contractServiceFeature = ContractServiceDelegate.getInstance().getContractServiceFeature(contractNumber,ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);        
        String optOut = contractServiceFeature.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE);
        int optOutNumber = 0;
        
        if(optOut != null) {            
            optOutNumber = Integer.parseInt(optOut);            
        }

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(nextPED);
        
        calendar.roll(Calendar.DAY_OF_YEAR, -optOutNumber);
        
        return calendar.getTime();
    }
    
    /**
     * Returns the VO needed to show the statistics
     * 
     * @param contractId
     * @return
     * @throws SystemException
     */
    public static StatisticsSummary getStatisticsSummary(int contractId) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getStatisticsSummary");
        }
    
        Connection conn = null;
        CallableStatement stmt = null;
        StatisticsSummary statisticsSummary = null;
            
        try {   
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_STATISTICS_SUMMARY);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Calling Stored Procedure: " + GET_STATISTICS_SUMMARY);
            }
            
            stmt.setBigDecimal(1, intToBigDecimal(contractId));
            
            String frequency = String.valueOf(
            		PlanDataHelper.getPlanEntryFrequencyForEEDEF(contractId));
            stmt.setString(2, frequency);
            
            // register the output parameters
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.registerOutParameter(5, Types.INTEGER);
            stmt.registerOutParameter(6, Types.INTEGER);
            stmt.registerOutParameter(7, Types.INTEGER);
            stmt.registerOutParameter(8, Types.INTEGER);
            stmt.registerOutParameter(9, Types.INTEGER);
            stmt.registerOutParameter(10, Types.INTEGER);
            stmt.registerOutParameter(11, Types.INTEGER);
            
            // execute the stored procedure
            stmt.execute();
            
            statisticsSummary = new StatisticsSummary(
                    stmt.getInt(3),                     
                    stmt.getInt(6),
                    stmt.getInt(5),
                    stmt.getInt(4), 
                    stmt.getInt(7), 
                    stmt.getInt(8),
                    stmt.getInt(9), 
                    stmt.getInt(10), 
                    stmt.getInt(11));
            
            logger.error("out_autoEnrolledCount: " + stmt.getInt(3)  
                    + ", out_internetEnrolledCount: " + stmt.getInt(4)  
                    + ", out_paperEnrolledCount: " + stmt.getInt(5)
                    + ", out_defaultEnrolledCount: " + stmt.getInt(6)
                    + ", out_participantsCount: " + stmt.getInt(7)
                    + ", out_optedOutCount: " + stmt.getInt(8)
                    + ", out_pendingEligibilityCount: " + stmt.getInt(9)
                    + ", out_pendingEnrollmentCount: " + stmt.getInt(10)
                    + ", out_notEligibleCount: " + stmt.getInt(11)); 
            
        } catch (SQLException e) {
           throw new SystemException(e, className, "getStatisticsSummary", 
           "Problem occurred during GET_STATISTICS_SUMMARY stored proc call. Report criteria:"
           + contractId);
        } catch (ApplicationException e) {
            throw new SystemException(e, className, "getStatisticsSummary", 
                    "Problem occurred retrieving Plan Entry Frequency to be passed in GET_STATISTICS_SUMMARY stored proc call. Contract#: " +
                    contractId);
        } finally {
            close(stmt, conn);
        }
    
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getStatisticsSummary");
        }
        
        return statisticsSummary;          
    }
 /*
  * DM
  * boolean isMissingBirthDateWarning
  * Combined Edits for Employee Data v3.13
  * #24 Set a Missing Birth Date warning if
  * Birth Date is blank
  * AND
  * (Record is for a non-participating employee
  * OR Record is for a participating employee who was default enrolled)
  * AND 
  * One of the Contract’s default investment option funds is a Lifecycle Funds
  * param@ EmployeeSummaryDetails item, boolean isLifecycleDIO
  */  
    private static boolean isMissingBirthDateWarning (EmployeeSummaryDetails item, boolean isLifecycleDIO )
    {
    	boolean missingBirthDateWarning = false;	
		if(item.getBirthDate()==null && isLifecycleDIO ==true)
			if(!item.isParticipantInd())
				missingBirthDateWarning =true; //Record is for a non-participating employee
			else if (EligibilityBusinessRulesUtil.DEFAULT.equals(item.getEnrollmentMethod())){
				missingBirthDateWarning =true;//Record is for a participating employee who was default enrolled
		}
    	return missingBirthDateWarning;
    }

}
		

