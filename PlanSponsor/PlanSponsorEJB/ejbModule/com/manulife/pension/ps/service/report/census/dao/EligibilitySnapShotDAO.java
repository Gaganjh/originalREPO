package com.manulife.pension.ps.service.report.census.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EligibilityServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.util.PlanDataHelper;
import com.manulife.pension.ps.service.report.census.valueobject.EligibilityReportData;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.eligibility.valueobject.PlanEntryRequirementDetailsVO;
import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.util.render.DateRender;
/**
 * This class is used to get the historical eligibility change report.
 * 
 * @author Ramamohan Gulla.
 *
 */
public class EligibilitySnapShotDAO extends BaseDatabaseDAO{
	
	
      private static final String className = EligibilityReportsDAO.class.getName();

      private static final Logger logger = Logger.getLogger(EligibilityReportsDAO.class);

      private static final String GET_EMPLOYEE_ELIGIBILITY_SNAPSHOT_REPORT =
	    "{call " + CUSTOMER_SCHEMA_NAME + "GET_EMPLOYEE_ELIGIBILITY_SNAPSHOT_REPORT(?,?,?)}";	
	
      private static final String GET_PLAN_ENTRY_REQUIREMENT =
  	    "{call " + CUSTOMER_SCHEMA_NAME + "GET_PLAN_ENTRY_REQUIREMENT(?,?)}";	
      
      public static ReportData getHistEligibilityReportData(final ReportCriteria criteria)
						throws SystemException {

	  if (logger.isDebugEnabled()) {
	      logger.debug("entry -> getReportData");
	      logger.debug("Report criteria -> " + criteria.toString());
	  }
		
	  Connection conn = null;
	  CallableStatement stmt = null;
	  ResultSet resultSet = null;
	  EligibilityReportData psReportDataVO = new EligibilityReportData();
		
	  // The second HashMap is used to eliminate multiple entries for the same column name
	  HashMap<String, HashMap<String, EmployeeChangeHistoryVO>> map = new HashMap<String, HashMap<String, EmployeeChangeHistoryVO>>();
		    
	  try {  
		  
	      // setup the connection and the statement
		conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
		stmt = conn.prepareCall(GET_EMPLOYEE_ELIGIBILITY_SNAPSHOT_REPORT);
		    
		if (logger.isDebugEnabled()) {
		    logger.debug("Calling Stored Procedure: " + GET_EMPLOYEE_ELIGIBILITY_SNAPSHOT_REPORT);
		}
		    
		// get contract number from criteria
		int contractNumber = (new Integer((String) criteria.getFilterValue(
					EligibilityReportData.FILTER_CONTRACT_NUMBER))).intValue();
		stmt.setBigDecimal(1, intToBigDecimal(contractNumber));
		    
		int frequency = PlanDataHelper.getPlanEntryFrequencyForEEDEF(contractNumber);
		stmt.setInt(2, frequency);
		    
		String snapShotDate = (String)criteria.getFilterValue(EligibilityReportData.SNAPSHOT_DATE);
		stmt.setDate(3, new java.sql.Date(convertDate(snapShotDate).getTime()));
		    
		
		// execute the stored procedure
		stmt.execute();
		resultSet = stmt.getResultSet();
		    
		List<EmployeeSummaryDetails> employeeSummaryDetails = new ArrayList<EmployeeSummaryDetails>();
		LinkedList[] sponsorLookup = new LinkedList[10]; // help with lookup
            
		if (resultSet != null) {
		    
		    while (resultSet.next()) {     
                	
                	EmployeeSummaryDetails item = new EmployeeSummaryDetails(
                		resultSet.getString(EligibilityReportData.PROFILE_ID_COLUMN),
                        resultSet.getString(EligibilityReportData.FIRST_NAME_COLUMN),
                        resultSet.getString(EligibilityReportData.MIDDLE_INITIAL_COLUMN),
                        resultSet.getString(EligibilityReportData.LAST_NAME_COLUMN),
                        resultSet.getString(EligibilityReportData.SOCIAL_SECURITY_NO_COLUMN),
                        resultSet.getString(EligibilityReportData.EMPLOYEE_ID_COLUMN),
                        resultSet.getString(EligibilityReportData.EMPLOYER_DIVISION_COLUMN),
                        resultSet.getString(EligibilityReportData.ENROLLMENT_METHOD_CODE_COLUMN),                         
                        resultSet.getString(EligibilityReportData.ENROLLMENT_STATUS_CODE_COLUMN),
                        resultSet.getDate(EligibilityReportData.PROCESSING_DATE_COLUMN),
                        resultSet.getString(EligibilityReportData.ELIGIBLE_TO_ENROLL_COLUMN),
                        resultSet.getString(EligibilityReportData.OPT_OUT_COLUMN));
                	
                                                                                                          
                	item.setBirthDate(resultSet.getDate(EligibilityReportData.BIRTH_DATE_COLUMN));
                	item.setHireDate(resultSet.getDate(EligibilityReportData.HIRE_DATE_COLUMN));
                	item.setEmploymentStatus(resultSet.getString(EligibilityReportData.EMPLOYMENT_STATUS_COLUMN));
                	item.setEmploymentStatusEffDate(resultSet.getDate(EligibilityReportData.EMPLOYMENT_STATUS_EFF_DATE_COLUMN));
                    
                	employeeSummaryDetails.add(item);            
		    }
		    
		}
            
            psReportDataVO.setDetails(employeeSummaryDetails);    
            
            
            Map<String,String> eligibiltyData = new HashMap<String, String>();
            
            if (stmt.getMoreResults()) {
            	ResultSet rsPED = stmt.getResultSet(); // out_planEntryDates ResultSet
            	if(rsPED != null){
	            	while(rsPED.next()){
	            		String profileId = rsPED.getString("PROFILE_ID");
	            		String moneyTypeId = (rsPED.getString("MONEY_TYPE_ID")==null) ? "" :rsPED.getString("MONEY_TYPE_ID").trim() ;
	            		
	            		
	            		String eligibilityDate = DateRender.format(rsPED.getDate("ELIGIBILITY_DATE"), "");
	            		String isPartTimeEligible = rsPED.getString("IS_PART_TIME_ELIGIBLE");
	            		String eligibilityPED = DateRender.format(rsPED.getDate("ELIGIBLE_PLAN_ENTRY_DATE"), "");
	            		String overrideInd = rsPED.getString("PROVIDED_ELIGIBILITY_DATE_IND");
	            		String compPeriodStartDate = DateRender.format(rsPED.getDate("COMPUTATION_PERIOD_START_DATE"), "");
	            		String compPeriodEndDate = DateRender.format(rsPED.getDate("COMPUTATION_PERIOD_END_DATE"), "");
	            		String computationPeriod = rsPED.getString("COMPUTATION_PERIOD");
	            		int periodOfWorkedHrs = rsPED.getInt("PERIOD_OF_WORKED_HRS");
	            		String periodOfWorkedEffDate = DateRender.format(rsPED.getDate("PERIOD_OF_WORKED_HRS_EFF_DATE"), "");
	            		
	            		String periodOfWorkHrs = "";
	            		if((periodOfWorkedEffDate == null || periodOfWorkedEffDate.length()==0 )&& periodOfWorkedHrs == 0){
	            		    periodOfWorkHrs = "";
	            		}else{
	            		    periodOfWorkHrs = Integer.toString(periodOfWorkedHrs);
	            		}
	            		
	            		computationPeriod = (computationPeriod == null )? "" : computationPeriod ;
	            		overrideInd = (overrideInd == null )? "" : overrideInd ;
	            		
	            		String isPartTimeEligibleString = StringUtils.EMPTY;
						if (StringUtils.equalsIgnoreCase("Y", (String) criteria
								.getFilterValue(EligibilityReportData.FILTER_SHOW_LTPT_INFO_INDICATOR + moneyTypeId))) {
							isPartTimeEligibleString = ","
									+ (StringUtils.equalsIgnoreCase("Y", isPartTimeEligible) ? "Yes"
											: StringUtils.EMPTY);
						}
	            		
						String eligibilityReport = eligibilityDate + isPartTimeEligibleString +
								"," + eligibilityPED + "," + overrideInd + "," + compPeriodStartDate + ","
								+ compPeriodEndDate + "," + computationPeriod + "," + periodOfWorkHrs + ","
								+ periodOfWorkedEffDate;
	            		eligibiltyData.put(profileId+moneyTypeId, eligibilityReport);
	            	}
            	}
            }
            psReportDataVO.setEligibiltyHistoryReportMap(eligibiltyData);
            
		} catch (SQLException e) {
		   throw new SystemException(e, className, "getHistEligibilityReportData", 
		   "Problem occurred during GET_EMPLOYEE_ENROLLMENT_SUMMARY stored proc call. Report criteria:"
		   + criteria);
		} catch (ApplicationException e) {
		    throw new SystemException(e, className, "getHistEligibilityReportData", 
		            "Problem occurred retrieving Plan Entry Frequency to be passed in GET_POST_OPT_OUT_SUMMARY stored proc call. Contract#: " +
		            criteria.getFilterValue(EligibilityReportData.FILTER_CONTRACT_NUMBER));
		} finally {
		    close(stmt, conn);
		}
		
		if (logger.isDebugEnabled()) {
		    logger.debug("exit <- getEmployeeSummary");
		}
		
		return psReportDataVO;    
    
      }
  
    /**
     * Converts a date string to a time stamp using the expected pattern.
     * 
     * @param date
     * @return
     */
    private static Timestamp convertDate(String date) {
        if(date == null || "".equals(date)) {
            return null;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd,yyyy");        
        Date theDate = null;
        
        try {
            theDate = dateFormat.parse(date);
        } catch (ParseException e) {
            // It should not happen because it passed validation already
            return null;
        }
        
        return new Timestamp(theDate.getTime());
    }
	
}
