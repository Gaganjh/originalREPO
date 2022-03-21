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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import com.manulife.pension.eligibility.EligibilityDataHelper;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.util.PlanDataHelper;
import com.manulife.pension.ps.service.report.census.valueobject.EligibilityIssuesReportVO;
import com.manulife.pension.ps.service.report.census.valueobject.EligibilityReportData;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.service.contract.dao.ContractServiceFeatureDAO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.eligibility.dao.EligibilitySupportDAO;
import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.log.LogUtility;

/**
 * This class is used to get the computation period change report details and 
 * Eligibility change report details.
 * 
 * @author Ramamohan Gulla.
 *
 */
public class EligibilityReportsDAO extends BaseDatabaseDAO{

	
    private static final String className = EligibilityReportsDAO.class.getName();

    private static final Logger logger = Logger.getLogger(EligibilityReportsDAO.class);
	
    private static final String GET_ELIGIBILITY_CHANGE_REPORT =
        	"{call " + CUSTOMER_SCHEMA_NAME + "GET_ELIGIBILITY_CHANGE_REPORT(?,?,?,?,?,?,?,?,?)}";
	
	private static final String SQL_GET_COMP_PERIOD_DATA = "select date (created_ts) as created_date, created_ts, profile_id, contract_id, after_value, before_value, column_name from ezk100.employee_change_history_detail where created_ts in ( "
		+ "select distinct CPED.created_ts from " 
		+ "psw100.employee_change_history_detail CPED left outer join " 
		+ "psw100.employee_change_history_detail ONEYEARRULE on (ONEYEARRULE.contract_id = CPED.contract_id and ONEYEARRULE.created_ts = CPED.created_ts and ONEYEARRULE.column_name LIKE '%_ONE_YR_RULE_APPLIED_IND' ) left outer join " 
		+ "psw100.employee_change_history_detail COMPTYPE on(COMPTYPE.contract_id = CPED.contract_id and COMPTYPE.created_ts = CPED.created_ts and COMPTYPE.column_name LIKE '%_SUBSEQUENT_COMP_PERIOD_COUNT' ) left outer join  "
		+ "psw100.employee_change_history_detail SUBCOMPCOUNT on (SUBCOMPCOUNT.contract_id = CPED.contract_id and SUBCOMPCOUNT.created_ts = CPED.created_ts and SUBCOMPCOUNT.column_name LIKE '%_COMPUTATION_PERIOD' ) "
		+ "where CPED.table_name like 'EMPLOYEE_PLAN_ENTRY_ELIGIBILITY'  "
		+ "and CPED.column_name LIKE '%_COMPUTATION_PERIOD_END_DATE'  "
		+ "and CPED.contract_id = ? and  "
		+ "CPED.after_value is not null and CPED.after_value not in ('', ' ') and "
		+ "((ONEYEARRULE.after_value is not null and ONEYEARRULE.after_value not in ('', ' ') and ONEYEARRULE.before_value is not null and ONEYEARRULE.before_value not in ('', ' ')) "
		+ "or (COMPTYPE.after_value is not null and COMPTYPE.after_value not in ('', ' ') and COMPTYPE.before_value is not null and COMPTYPE.before_value not in ('', ' ')) "
		+ "or (SUBCOMPCOUNT.after_value is not null and SUBCOMPCOUNT.after_value not in ('', ' ') and SUBCOMPCOUNT.before_value is not null ))) ";
	
	private static final String SQL_GET_EMP_DETAILS = "with details as ( "
			+ SQL_GET_COMP_PERIOD_DATA
			+ " ) SELECT DISTINCT EC.PROFILE_ID AS PROFILE_ID, EC.FIRST_NAME AS FIRST_NAME, EC.LAST_NAME AS LAST_NAME, EC.MIDDLE_INITIAL AS MIDDLE_INITIAL, "
			+ " EC.SOCIAL_SECURITY_NO AS SOCIAL_SECURITY_NO, EC.EMPLOYEE_ID AS EMPLOYEE_ID, EC.BIRTH_DATE AS BIRTH_DATE, EC.HIRE_DATE AS HIRE_DATE, "
			+ " EC.EMPLOYER_DIVISION AS EMPLOYER_DIVISION, DL.created_date AS created_date FROM PSW100.EMPLOYEE_CONTRACT EC, DETAILS DL WHERE EC.PROFILE_ID = DL.PROFILE_ID "
			+ " ORDER BY created_date desc, LAST_NAME asc,FIRST_NAME asc,MIDDLE_INITIAL asc,SOCIAL_SECURITY_NO asc for fetch only ";
	
	private static final String SQL_SELECT_INELIGIBLE_EMPLOYEE_TRANSACTION="select EC.PROFILE_ID, EC.CONTRACT_ID, EC.LAST_NAME,"+ 
			"EC.FIRST_NAME, EC.MIDDLE_INITIAL, EC.SOCIAL_SECURITY_NO," +
			"EC.EMPLOYEE_ID, EC.EMPLOYER_DIVISION, IN_EMP.PLAN_ELIGIBLE_IND, IN_EMP.MONEY_TYPE_LIST,"+ 
			"IN_EMP.ELIGIBLE_PLAN_ENTRY_DATE, IN_EMP.MFC_CONTRACT_ENROLLMENT_DATE,"+
			"IN_EMP.TRANSACTION_EFFECTIVE_DATE, IN_EMP.PAYROLL_APPLICABLE_DATE,"+ 
			"IN_EMP.SUBMISSION_ID, IN_EMP.TRANSACTION_ID,"+
			"LUP.LOOKUP_DESC, IN_EMP.ENROLLMENT_METHOD_CODE, IN_EMP.ENROLLMENT_PROCESSED_DATE ,"+
			"CASE WHEN "+
			"IN_EMP.TRANSACTION_EFFECTIVE_DATE IS NOT NULL "+ 
			"THEN "+ 
			"IN_EMP.TRANSACTION_EFFECTIVE_DATE "+
			"ELSE "+
			"IN_EMP.ENROLLMENT_PROCESSED_DATE "+ 
			"END AS TRANSACTION_DATE "+
			"from "+ 
			CUSTOMER_SCHEMA_NAME+
			"INELIGIBLE_EMPLOYEE_TRANSACTION as IN_EMP,"+ 
			CUSTOMER_SCHEMA_NAME+
			"EMPLOYEE_CONTRACT as EC, "+ 
			CUSTOMER_SCHEMA_NAME+
			"LOOKUP as LUP "+ 
			"where IN_EMP.CONTRACT_ID = EC.CONTRACT_ID "+ 
			"and IN_EMP.PROFILE_ID = EC.PROFILE_ID "+ 
			"and LUP.LOOKUP_CODE = IN_EMP.INELIGIBLE_REASON_CODE "+ 
			"and LUP.LOOKUP_TYPE_NAME = 'EMPLOYEE_INELIGIBLE_REASON' "+ 
			"and EC.CONTRACT_ID = ? and (IN_EMP.TRANSACTION_EFFECTIVE_DATE between ? and ? "+ 
			"OR IN_EMP.ENROLLMENT_PROCESSED_DATE between ? and ?) "+ 
			"order by TRANSACTION_DATE asc,"+ 
			"EC.LAST_NAME asc, EC.FIRST_NAME asc, EC.MIDDLE_INITIAL asc, EC.SOCIAL_SECURITY_NO asc,"+ 
			"IN_EMP.TRANSACTION_ID asc, IN_EMP.SUBMISSION_ID asc,"+ 
			"IN_EMP.MONEY_TYPE_LIST asc FOR FETCH ONLY";
	
	public interface Column {
		public static final String PROFILE_ID= "PROFILE_ID";
		public static final String CONTRACT_ID= "CONTRACT_ID";
		public static final String LAST_NAME= "LAST_NAME";
		public static final String FIRST_NAME= "FIRST_NAME";
		public static final String MIDDLE_INITIAL= "MIDDLE_INITIAL";
		public static final String SOCIAL_SECURITY_NO= "SOCIAL_SECURITY_NO";
		public static final String EMPLOYEE_ID= "EMPLOYEE_ID";
		public static final String EMPLOYER_DIVISION= "EMPLOYER_DIVISION";
		public static final String PLAN_ELIGIBLE_IND= "PLAN_ELIGIBLE_IND";
		public static final String MONEY_TYPE_LIST= "MONEY_TYPE_LIST";
		public static final String ELIGIBLE_PLAN_ENTRY_DATE= "ELIGIBLE_PLAN_ENTRY_DATE";
		public static final String MFC_CONTRACT_ENROLLMENT_DATE= "MFC_CONTRACT_ENROLLMENT_DATE";
		public static final String TRANSACTION_DATE="TRANSACTION_DATE";
		public static final String TRANSACTION_EFFECTIVE_DATE= "TRANSACTION_EFFECTIVE_DATE";
		public static final String PAYROLL_APPLICABLE_DATE= "PAYROLL_APPLICABLE_DATE";
		public static final String SUBMISSION_ID= "SUBMISSION_ID";
		public static final String TRANSACTION_ID= "TRANSACTION_ID";
		public static final String INELIGIBLE_REASON_CODE= "LOOKUP_DESC";
		public static final String ENROLLMENT_METHOD_CODE= "ENROLLMENT_METHOD_CODE";
		public static final String ENROLLMENT_PROCESSED_DATE= "ENROLLMENT_PROCESSED_DATE";
	}
	
    /**
     * SimpleDateFormat is converted to FastDateFormat to make it thread safe
     */
    private static FastDateFormat dateFormat = FastDateFormat.getInstance("MM/dd/yyyy");
    
    private static final String ALL_MONEY_TYPE = "ALL";
    
    private static final String EC_SERVICE_FEATURE = "EC";
    
    private static final String DM_SERVICE_FEATURE = "DM";
    
    private static final String AE_SERVICE_FEATURE = "AE";
	
    /**
     * This method is used to get the computation period change report details.
     * @param criteria
     * @return ReportData
     * @throws SystemException
     */
    public static ReportData getCompChangeEligibilityReportData(final ReportCriteria criteria)
	    				throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getCompChangeEligibilityReportData");
			logger.debug("Report criteria -> " + criteria.toString());
		}

		Connection conn = null;
		PreparedStatement stmt = null;
		EligibilityReportData psReportDataVO = new EligibilityReportData();
		int contractId = Integer.parseInt((String) criteria
				.getFilterValue(EligibilityReportData.FILTER_CONTRACT_NUMBER));
		ResultSet resultSet = null;
		List<EmployeeSummaryDetails> summaryDetailsList = new ArrayList<EmployeeSummaryDetails>() ;
		
		try {

			// setup the connection and the statement
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn.prepareStatement(SQL_GET_COMP_PERIOD_DATA);
			stmt.setInt(1, contractId);

			resultSet = stmt.executeQuery();

			Map <Integer, Map <Timestamp, List<EmployeeChangeHistoryVO>>> changeHistoryMap = new HashMap<Integer, Map<Timestamp,List<EmployeeChangeHistoryVO>>>();
			    
			if (resultSet != null) {
				while (resultSet.next()) {
					    Integer profileId = Integer.valueOf(resultSet.getString("PROFILE_ID"));
					    String columnName = resultSet.getString("column_name");
					    String afterValue = resultSet.getString("after_value");
					    String beforeValue = resultSet.getString("before_value");
					    Timestamp createdTs = resultSet.getTimestamp("created_ts");
					    String createdDate = resultSet.getString("created_date");
					      
					    EmployeeChangeHistoryVO vo = new EmployeeChangeHistoryVO();
					    vo.setProfileId(Long.valueOf(profileId));
					    vo.setColumnName(columnName);
					    vo.setPreviousValue(beforeValue);
					    vo.setCurrentValue(afterValue);
					    vo.setCurrentUpdatedTs(createdTs);
					    
					   if (columnName.contains("_ELIGIBILITY_DATE") || columnName.contains("_PLAN_ENTRY_DATE") || columnName.contains("_COMPUTATION_PERIOD_END_DATE")) {
					    
					    if (changeHistoryMap.get(profileId) != null){
					    	Map <Timestamp, List<EmployeeChangeHistoryVO>> changeHistoryVO = changeHistoryMap.get(profileId);
					    	
					    		if (changeHistoryVO.get(createdTs) != null) {
					    			List<EmployeeChangeHistoryVO> voList = changeHistoryVO.get(createdTs);
					    			voList.add(vo);
					    			changeHistoryVO.put(createdTs, voList);
					    			changeHistoryMap.put(profileId, changeHistoryVO);
					    		} else {
					    			List<EmployeeChangeHistoryVO> voList = new ArrayList<EmployeeChangeHistoryVO>();
					    			voList.add(vo);
					    			changeHistoryVO.put(createdTs, voList);
					    			changeHistoryMap.put(profileId, changeHistoryVO);
					    		}
					    } else {
					    	Map <Timestamp, List<EmployeeChangeHistoryVO>> changeHistoryVO = new HashMap<Timestamp, List<EmployeeChangeHistoryVO>>();
					    	List<EmployeeChangeHistoryVO> voList = new ArrayList<EmployeeChangeHistoryVO>();
			    			voList.add(vo);
			    			changeHistoryVO.put(createdTs, voList);
			    			changeHistoryMap.put(profileId, changeHistoryVO);
					    }
					   }
				    }
				     psReportDataVO.setChangeHistoryMap(changeHistoryMap);
				}
			
			stmt = conn.prepareStatement(SQL_GET_EMP_DETAILS);
			stmt.setInt(1, contractId);
			resultSet = stmt.executeQuery();

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
				                    "", //  Enrollment Status Code                      
				                    "", // Enrollment Method Code
				                    null, // Enrollment Processing Date
				                    "", // Eligible to Enroll
				                    ""); // optout Ind

				            item.setBirthDate(resultSet.getDate(EligibilityReportData.BIRTH_DATE_COLUMN));
				            item.setHireDate(resultSet.getDate(EligibilityReportData.HIRE_DATE_COLUMN));
				            String  createdDate =  resultSet.getString(EligibilityReportData.CREATED_DATE_COLUMN);
				            item.setCompPeriodChangeDate(createdDate == null ? "" : createdDate);
				            summaryDetailsList.add(item); 
				}
			}
			psReportDataVO.setDetails(summaryDetailsList);
		
	} catch (SQLException e) {
	    throw new SystemException(e, className, "getCompChangeEligibilityReportData", 
		    	"Problem occurred during GET_EMPLOYEE_ELIGIBILITY_COMPUTATION_CHANGE_REPORT stored proc call. Report criteria:"
			   + criteria);
	}  finally {
	      close(stmt, conn);
	}
			
	if (logger.isDebugEnabled()) {
	    logger.debug("exit <- getCompChangeEligibilityReportData");
	}
			
	return psReportDataVO;    
    }
	
   /**
    *  This method is used to get eligibility change report.
    * @param criteria
    * @return ReportData
    * @throws SystemException
    */
    public static ReportData getEligibilityChangeReportData(final ReportCriteria criteria) throws SystemException {
		
		if (logger.isDebugEnabled()) {

			logger.debug("entry -> getEligibilityChangeReportData");
			logger.debug("Report criteria -> " + criteria.toString());
		}
		
		Connection conn = null;
		CallableStatement stmt = null;

		EligibilityReportData psReportDataVO = new EligibilityReportData();
		// The second HashMap is used to eliminate multiple entries for the same
		// column name
		HashMap<String, HashMap<String, EmployeeChangeHistoryVO>> map = new HashMap<String, HashMap<String, EmployeeChangeHistoryVO>>();
		    
		try {

			// get contract number from criteria
			int contractNumber = (new Integer(
					(String) criteria
							.getFilterValue(EligibilityReportData.FILTER_CONTRACT_NUMBER)))
					.intValue();

			int optOutDays = (new Integer((String) criteria
					.getFilterValue(EligibilityReportData.OPTOUT_DAYS)))
					.intValue();

			String reportedPED = (String) criteria
					.getFilterValue(EligibilityReportData.REPORTED_PLAN_ENTRY_DATE);

			boolean isDMContract = (Boolean) criteria
					.getFilterValue(EligibilityReportData.CONTRACT_DM);

			boolean isIED = (Boolean) criteria
					.getFilterValue(EligibilityReportData.IS_IED);

			String initialEnrollmentDate = (String) criteria
					.getFilterValue(EligibilityReportData.INITIAL_ENROLLMENT_DATE);

			String moneyTypeSelected = (String) criteria
					.getFilterValue(EligibilityReportData.FILTER_MONEY_TYPES);

			String moneyTypeString = (String) criteria
					.getFilterValue(EligibilityReportData.FILTER_MONEY_TYPE_STRING);

			boolean iedSelected = false;

			boolean isAEOn = (Boolean) criteria
					.getFilterValue(EligibilityReportData.FILTER_AUTO_ENROLL_ON);

			boolean isECOn = (Boolean) criteria
					.getFilterValue(EligibilityReportData.IS_EC_ON);

			if (initialEnrollmentDate != null
					&& initialEnrollmentDate.equalsIgnoreCase(reportedPED)
					&& !isECOn && isAEOn
					&& "EEDEF".equalsIgnoreCase(moneyTypeSelected)) {

				iedSelected = true;
			}

			Date reportedDate = null;
			reportedDate = convertDate(reportedPED);

			// From the reported Date calculate the report period start date and
			// End date
			GregorianCalendar rPED = new GregorianCalendar();
			GregorianCalendar currCal = new GregorianCalendar();
			GregorianCalendar tempCal = new GregorianCalendar();
			currCal.setTime(new Date());
			currCal = resetTheTime(currCal);
			rPED.setTime(reportedDate);
			rPED = resetTheTime(rPED);

			// Reporting period End date
			Date reportEndDate = rPED.getTime();
			Date reportStartDate = null;
			// Start date

			Date greaterDate = null;
			Date fiftyTwoDaysBeforePED = rollBackDateTo(reportEndDate,
					(EligibilityDataHelper.getECandDMNotificationPeriod()));
			Date fortyFiveDaysBeforePED = rollBackDateTo(reportEndDate,
					(EligibilityDataHelper.getECNotificationPeriod()));
			Date aeOnDate = ContractServiceFeatureDAO
					.getContractServiceFeatureCreateDate(contractNumber, AE_SERVICE_FEATURE);
			Date dmOnDate = ContractServiceFeatureDAO
					.getContractServiceFeatureCreateDate(contractNumber, DM_SERVICE_FEATURE);
			Date ecOnDate = null;

			if (isECOn) {
				if(ALL_MONEY_TYPE.equalsIgnoreCase(moneyTypeSelected)){
					ecOnDate = ContractServiceFeatureDAO
							.getContractServiceFeatureCreateDate(
									contractNumber, EC_SERVICE_FEATURE);
				} else {
					ecOnDate = EligibilitySupportDAO
							.getMoneyTypeServiceFeatureCreateDate(
									contractNumber, EC_SERVICE_FEATURE, moneyTypeSelected
											.trim());
				}
				
				tempCal.setTime(ecOnDate);
				tempCal.add(Calendar.DATE, 1);
			}

			// check if the contract is DM enabled
			if (isDMContract && dmOnDate != null && isECOn) {

				if (dmOnDate.compareTo(fiftyTwoDaysBeforePED) <= 0) {
					greaterDate = getGreaterDate(tempCal.getTime(),
							fiftyTwoDaysBeforePED);
				} else if (dmOnDate.compareTo(fiftyTwoDaysBeforePED) > 0
						&& dmOnDate.compareTo(fortyFiveDaysBeforePED) < 0) {
					greaterDate = getGreaterDate(tempCal.getTime(), dmOnDate);
				} else if (dmOnDate.compareTo(fortyFiveDaysBeforePED) >= 0) {
					greaterDate = getGreaterDate(fortyFiveDaysBeforePED,
							tempCal.getTime());
				}
				if (greaterDate != null) {
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(greaterDate);
					reportStartDate = resetTheTime(cal).getTime();
				}

			} else if (isECOn) {

				greaterDate = getGreaterDate(fortyFiveDaysBeforePED, tempCal
						.getTime());
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(greaterDate);
				reportStartDate = resetTheTime(cal).getTime();
			} else if (isAEOn) {
				greaterDate = getGreaterDate(fortyFiveDaysBeforePED, aeOnDate);
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(greaterDate);
				reportStartDate = resetTheTime(cal).getTime();
			}

			// Date reportStartDate = rPED.getTime();

			// to get the opt out date
			rPED.setTime(reportEndDate);
			rPED.add(Calendar.DAY_OF_YEAR, -optOutDays);
			Date optOutDate = rPED.getTime();

			if (aeOnDate != null) {
				tempCal.setTime(aeOnDate);
				tempCal = resetTheTime(tempCal);
				if (tempCal.getTime().compareTo(optOutDate) > 0) {
					optOutDate = tempCal.getTime();
				}
			}

			if (iedSelected) {
				reportStartDate = optOutDate;
			}

			// setup the connection and the statement
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn.prepareCall(GET_ELIGIBILITY_CHANGE_REPORT);

			if (logger.isDebugEnabled()) {
				logger.debug("Calling Stored Procedure: "
						+ GET_ELIGIBILITY_CHANGE_REPORT);
			}

			stmt.setBigDecimal(1, intToBigDecimal(contractNumber));
			if (reportStartDate != null) {
				stmt.setDate(2, new java.sql.Date(reportStartDate.getTime()));
			} else {
				stmt.setNull(2, Types.DATE);
			}

			if (reportEndDate != null) {
				stmt.setDate(3, new java.sql.Date(reportEndDate.getTime()));
			} else {
				stmt.setNull(3, Types.DATE);
			}

			if (optOutDate != null) {
				stmt.setDate(4, new java.sql.Date(optOutDate.getTime()));
			} else {
				stmt.setNull(4, Types.DATE);
			}
           
            String frequency = String.valueOf(
            		PlanDataHelper.getPlanEntryFrequencyForEEDEF(contractNumber));
            stmt.setString(5, frequency);

			if ("All".equalsIgnoreCase(moneyTypeSelected)) {
				stmt.setNull(6, Types.VARCHAR);
			} else {
				stmt.setString(6, moneyTypeSelected);
			}

			stmt.registerOutParameter(7, Types.INTEGER);
			stmt.registerOutParameter(8, Types.INTEGER);
			stmt.registerOutParameter(9, Types.INTEGER);

			int numberOfChanges = 0;

			stmt.execute();

			List<EmployeeSummaryDetails> employeeSummaryDetails = new ArrayList<EmployeeSummaryDetails>();

			ResultSet resultSet = stmt.getResultSet();

			if (resultSet != null) {
				while (resultSet.next()) {
					HashMap<String, EmployeeChangeHistoryVO> changeHistory = map
							.get(resultSet
									.getString(EligibilityReportData.PROFILE_ID_COLUMN));

					if (changeHistory == null) {
						changeHistory = new HashMap<String, EmployeeChangeHistoryVO>();
						map.put(resultSet.getString(EligibilityReportData.PROFILE_ID_COLUMN),
										changeHistory);
					}

					if (changeHistory.keySet().contains(resultSet.getString(EligibilityReportData.COLUMN_NAME))) {

						EmployeeChangeHistoryVO historyVo = changeHistory
								.get(resultSet.getString(EligibilityReportData.COLUMN_NAME));
						String columnName = resultSet
								.getString(EligibilityReportData.COLUMN_NAME);
						String afterValue = historyVo.getCurrentValue();
						String beforeValue = historyVo.getPreviousValue();

						Timestamp ts = resultSet
								.getTimestamp(EligibilityReportData.CREATED_TIMESTAMP_COLUMN);

						if (ts.after(historyVo.getCurrentUpdatedTs())) {
							afterValue = resultSet
									.getString(EligibilityReportData.AFTER_VALUE_COLUMN);

						} else if (ts.before(historyVo.getCurrentUpdatedTs())) {
							beforeValue = resultSet
									.getString(EligibilityReportData.BEFORE_VALUE_COLUMN);
						}

						historyVo.setCurrentValue(afterValue);
						historyVo.setPreviousValue(beforeValue);
						historyVo.setColumnName(columnName);
						changeHistory.put(resultSet
								.getString(EligibilityReportData.COLUMN_NAME),
								historyVo);

					} else {

						EmployeeChangeHistoryVO e = new EmployeeChangeHistoryVO();
						e.setColumnName(resultSet
								.getString(EligibilityReportData.COLUMN_NAME));
						e.setCurrentValue(resultSet
										.getString(EligibilityReportData.AFTER_VALUE_COLUMN));
						e.setPreviousValue(resultSet
										.getString(EligibilityReportData.BEFORE_VALUE_COLUMN));
						e.setCurrentUpdatedTs(resultSet
										.getTimestamp(EligibilityReportData.CREATED_TIMESTAMP_COLUMN));
						changeHistory.put(resultSet
								.getString(EligibilityReportData.COLUMN_NAME),
								e);

					}
				}
			}

			// Construct a set with all profile Ids that have been returned
			Set<String> profiles = map.keySet();

			if (stmt.getMoreResults()) {
				resultSet = stmt.getResultSet();

				if (resultSet != null) {
					while (resultSet.next()) {
						// Check if the profile is part of the set with valid
						// changes
						if (!profiles
								.contains(resultSet
										.getString(EligibilityReportData.PROFILE_ID_COLUMN))) {
							continue;
						}

						EmployeeSummaryDetails item = new EmployeeSummaryDetails(
								resultSet.getString(EligibilityReportData.FIRST_NAME_COLUMN),
								resultSet.getString(EligibilityReportData.MIDDLE_INITIAL_COLUMN),
								resultSet.getString(EligibilityReportData.LAST_NAME_COLUMN),
								resultSet.getString(EligibilityReportData.SOCIAL_SECURITY_NO_COLUMN),
								resultSet.getString(EligibilityReportData.EMPLOYEE_ID_COLUMN),
								resultSet.getString(EligibilityReportData.EMPLOYER_DIVISION_COLUMN),
								resultSet.getString(EligibilityReportData.ENROLLMENT_METHOD_COLUMN),
								resultSet.getString(EligibilityReportData.ENROLLMENT_STATUS_COLUMN),
								resultSet.getDate(EligibilityReportData.PROCESSED_DATE_COLUMN),
								resultSet.getString(EligibilityReportData.ELIGIBLE_TO_ENROLL_COLUMN),
								resultSet.getDate(EligibilityReportData.ELIGIBILITY_DATE_COLUMN),
								resultSet.getBoolean(EligibilityReportData.OPT_OUT_SORT_COLUMN),
								getParsedPercent(resultSet
										.getString(EligibilityReportData.DEFERRAL_PCT_COLUMN)),
								resultSet.getString(EligibilityReportData.BEFORE_TAX_DEFER_PCT),
								resultSet.getString(EligibilityReportData.BEFORE_TAX_DEFER_AMT),
								resultSet.getString(EligibilityReportData.DESIG_ROTH_DEF_PCT),
								resultSet.getString(EligibilityReportData.DESIG_ROTH_DEF_AMT),
								resultSet.getString(EligibilityReportData.ENROLL_BEFORE_TAX_DEFER_PCT),
								resultSet.getString(EligibilityReportData.ENROLL_BEFORE_TAX_DEFER_AMT),
								resultSet.getString(EligibilityReportData.ENROLL_DESIG_ROTH_DEF_PCT),
								resultSet.getString(EligibilityReportData.ENROLL_DESIG_ROTH_DEF_AMT),
								resultSet.getString(EligibilityReportData.PROFILE_ID_COLUMN),
								resultSet.getBoolean(EligibilityReportData.PARTICIPANT_IND_COLUMN), "", "");

						item.setApplicablePlanEntryDate(resultSet
										.getDate(EligibilityReportData.APPLICABLE_PLAN_ENTRY_DATE_COLUMN));
						item.setEmploymentStatus(resultSet
										.getString(EligibilityReportData.EMPLOYMENT_STATUS_CODE));
						item.setAutoEnrollOptOutInd(resultSet
										.getString(EligibilityReportData.OPT_OUT_COLUMN));
						item.setBirthDate(resultSet
										.getDate(EligibilityReportData.BIRTH_DATE_COLUMN));
						item.setHireDate(resultSet
										.getDate(EligibilityReportData.HIRE_DATE_COLUMN));
						item.setStatusChanges(new ArrayList<EmployeeChangeHistoryVO>(
										map.get(item.getProfileId()).values()));
						// Update number of changes
						if (map.get(item.getProfileId()) != null
								&& !map.get(item.getProfileId()).isEmpty()) {
							numberOfChanges = numberOfChanges
									+ map.get(item.getProfileId()).size();
						}

						// If LAST_UPDATED_TS == null there is no entry and
						// nothing to set
						setOptOutHistory(resultSet, item);

						employeeSummaryDetails.add(item);
					}
				}
			}

			// Set the total number of status changes
			psReportDataVO.setNumberOfChanges(numberOfChanges);
			psReportDataVO.setEligiblilityChangeData(map);

			psReportDataVO.setDetails(employeeSummaryDetails);
			cleanStaging(stmt.getInt(8), stmt.getInt(9), conn);

			// The below section is used to get the Calculation Override
			// Indicator from the
			// EMPLOYEE_PLAN_ENTRY_ELIGIBILITY table.
			if (profiles.size() > 0) {

				Iterator<String> it = profiles.iterator();

				String profileIds = "";

				Map<String, String> overrideInd = new HashMap<String, String>();
				Map<String, String> eligibilityDates = new HashMap<String, String>();
				Map<String, String> planEntryDates = new HashMap<String, String>();

				int ind = 1;
				while (it.hasNext()) {

					if (ind == 1) {
						profileIds = profileIds + " " + it.next();
					} else {
						profileIds = profileIds + " , " + it.next();
					}
					ind++;
				}

				String sqlQry = " SELECT PROFILE_ID , MONEY_TYPE_ID , PROVIDED_ELIGIBILITY_DATE_IND , "
						+ " ELIGIBILITY_DATE,ELIGIBLE_PLAN_ENTRY_DATE "
						+ " FROM PSW100.EMPLOYEE_PLAN_ENTRY_ELIGIBILITY EG "
						+ " WHERE PROFILE_ID IN ( "
						+ profileIds
						+ ") AND "
						+ " MONEY_TYPE_ID IN ("
						+ moneyTypeString
						+ ") AND "
						+ " CONTRACT_ID = ?  FOR FETCH ONLY";

				PreparedStatement stmtEG = conn.prepareStatement(sqlQry);
				stmtEG.setInt(1, contractNumber);

				ResultSet rs = stmtEG.executeQuery();

				if (rs != null) {

					while (rs.next()) {

						String profileId = rs.getString("PROFILE_ID") == null ? ""
								: rs.getString("PROFILE_ID");
						String moneyTypeId = rs.getString("MONEY_TYPE_ID") == null ? ""
								: rs.getString("MONEY_TYPE_ID").trim();
						String eligibilityDateInd = rs
								.getString("PROVIDED_ELIGIBILITY_DATE_IND") == null ? ""
								: rs.getString("PROVIDED_ELIGIBILITY_DATE_IND");
						Date eligibilityDate = rs.getDate("ELIGIBILITY_DATE");
						Date planEntryDate = rs
								.getDate("ELIGIBLE_PLAN_ENTRY_DATE");
						String eDate = "";
						String ped = "";
						if (eligibilityDate != null) {
							eDate = dateFormat.format(eligibilityDate);
						}
						if (planEntryDate != null) {
							ped = dateFormat.format(planEntryDate);
						}
						overrideInd.put(profileId + moneyTypeId,
								eligibilityDateInd);
						eligibilityDates.put(profileId + moneyTypeId, eDate);
						planEntryDates.put(profileId + moneyTypeId, ped);

					}

				}
				// Notification period start & end date
				psReportDataVO.setNotificationPeriodStartDate(reportStartDate);
				psReportDataVO.setNotificationPeriodEndDate(reportEndDate);
				psReportDataVO.setEligOverrideMap(overrideInd);
				psReportDataVO.setEligDatesMap(eligibilityDates);
				psReportDataVO.setEligPEDMap(planEntryDates);
			}

		} catch (SQLException e) {
			throw new SystemException(
					e,
					className,
					"getEligibilityChangeReportData",
					"Problem occurred during GET_ELIGIBILITY_CHANGE_REPORT stored proc call. Report criteria:"
							+ criteria);

		} catch (ApplicationException e) {
			throw new SystemException(
					e,
					className,
					"getEligibilityChangeReportData",
					"Problem occurred retrieving Plan Entry Frequency to be passed in GET_ELIGIBILITY_CHANGE_REPORT stored proc call. Contract#: "
							+ criteria
									.getFilterValue(EligibilityReportData.FILTER_CONTRACT_NUMBER));
		} finally {
			close(stmt, conn);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getEligibilityChangeReportData");
		}

		return psReportDataVO;
    }
	
    /**
	 * Method used to retrieve the Eligibility Issues Report VO List with
	 * references values.
	 * 
	 * @param reportedFromDate
	 * @param reportedToDate
	 * @return ReportData
	 * @throws SystemException
	 */
	public static ReportData getEligibilityIssuesReportVO(
			final ReportCriteria criteria) throws SystemException {

		final String methodName = "getEligibilityIssuesReportVO";
		LogUtility.logEntry(logger, methodName);

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Map criteriaMap = criteria.getFilters();
		int contractId = Integer.valueOf(criteriaMap.get(
				EligibilityReportData.FILTER_CONTRACT_NUMBER).toString());

		Date reportedFromDate = null;
		Date reportedToDate = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		List<EligibilityIssuesReportVO> eligibilityIssuesReportVOList = null;
		EligibilityReportData eligibilityReportData = new EligibilityReportData();

		try {
			reportedFromDate = dateFormat.parse(String.valueOf(criteriaMap
					.get(EligibilityReportData.REPORTED_FROM_DATE)));
			reportedToDate = dateFormat.parse(String.valueOf(criteriaMap
					.get(EligibilityReportData.REPORTED_TO_DATE)));

			connection = getDefaultConnection(className,
					CUSTOMER_DATA_SOURCE_NAME);
			preparedStatement = connection
					.prepareStatement(SQL_SELECT_INELIGIBLE_EMPLOYEE_TRANSACTION);

			preparedStatement.setInt(1, contractId);
			preparedStatement.setDate(2, new java.sql.Date(reportedFromDate.getTime()));
			preparedStatement.setDate(3, new java.sql.Date(reportedToDate.getTime()));
			preparedStatement.setDate(4, new java.sql.Date(reportedFromDate.getTime()));
			preparedStatement.setDate(5, new java.sql.Date(reportedToDate.getTime()));
			resultSet = preparedStatement.executeQuery();

			eligibilityIssuesReportVOList = createEligibilityIssuesReportVO(resultSet);
			eligibilityReportData.setDetails(eligibilityIssuesReportVOList);

			resultSet.close();

		} catch (SQLException sqlException) {
			throw new SystemException(
					sqlException,
					"Problem occured while retrieving the record data's for Eligibility Issues Report "
							+ "[ From Date : "
							+ reportedFromDate
							+ " To Date]"
							+ reportedToDate + sqlException.getMessage());
		} catch (ParseException parseException) {
			// Drop
		} finally {
			close(preparedStatement, connection);
			LogUtility.logExit(logger, methodName);
		}

		eligibilityReportData
				.setEligibilityIssuesReportVOList(eligibilityIssuesReportVOList);

		return eligibilityReportData;
	}
     
     /**
      * This method is used to reset the time to 00:00:00 a.m.	
      * @param cal
      * @return GregorianCalendar
      */
	private static GregorianCalendar resetTheTime(GregorianCalendar cal) {

		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);

		return cal;
	}
     
    /**
     * Converts a date string to a timestamp using the expected pattern
     * 
     * @param date
     * @return Timestamp
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
    
    /**
     * 
     * @param resultSet
     * @param item
     * @throws SQLException
     */
    private static void setOptOutHistory(ResultSet resultSet, EmployeeSummaryDetails item) throws SQLException {
	
        if(resultSet.getDate(EligibilityReportData.LAST_UPDATED_TS_COLUMN) != null) {
            item.getOptOutHistory().getCurrentUser().setLastName(
                    resultSet.getString(EligibilityReportData.CREATED_LAST_NAME_COLUMN));
            item.getOptOutHistory().getCurrentUser().setFirstName(
                    resultSet.getString(EligibilityReportData.CREATED_FIRST_NAME_COLUMN));
            item.getOptOutHistory().getCurrentUser().setUserIdType(
                    resultSet.getString(EligibilityReportData.CREATED_USER_ID_TYPE_COLUMN));
            item.getOptOutHistory().getCurrentUser().setUserId(
                    resultSet.getString(EligibilityReportData.CREATED_USER_ID_COLUMN));
            item.getOptOutHistory().setCurrentUpdatedTs(resultSet.getTimestamp(EligibilityReportData.LAST_UPDATED_TS_COLUMN));                    
            item.getOptOutHistory().setCurrentValue(resultSet.getString(EligibilityReportData.DATA_ELEMENT_VALUE));
            item.getOptOutHistory().setSourceChannelCode(resultSet.getString(EligibilityReportData.SOURCE_CHANNEL_CODE_COLUMN));                        
        }
    }
    
    /**
     * Utility method that parses the String to show just the meaningful decimals 
     * 
     * @param percentage
     * @return String
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
     * Converts time stamp to date
     * 
     * @param Date
     * @return Date
     */
    private static Date getDateFromTimeStamp(Date date) {

	SimpleDateFormat sf = new SimpleDateFormat("MMMMM dd, yyyy");
	String formattedDate = sf.format(date);
	
	try {
	    date = sf.parse(formattedDate);
	} catch (ParseException e) {
	    // not likely to happen
	}
	return date;

    }
    /**
     * This method rolls back the current date to a specified days
     * @param startDate
     * @param amount
     * @return
     */
    private static Date rollBackDateTo(Date startDate, int amount) {

	Calendar calendar = GregorianCalendar.getInstance();
	if (startDate != null) {
	    calendar.setTime(startDate);
	}
	calendar.add(Calendar.DATE, -(amount));
	return calendar.getTime();

    }
    private static Date getGreaterDate(Date date1,Date date2){
	   if(date1.compareTo(date2)>0){
		  return date1;
	   }else{
		  return date2;

	   }
    }
    
    /**
	 * Method used to iterate the resultSet and create the list of
	 * EligibilityIssuesReportVO Object
	 * 
	 * @param resultSet
	 * @return List<EligibilityIssuesReportVO>
	 * @throws SQLException
	 */
	private static List<EligibilityIssuesReportVO> createEligibilityIssuesReportVO(
			ResultSet resultSet) throws SQLException {

		List<EligibilityIssuesReportVO> eligibilityIssuesReportVOList = new ArrayList<EligibilityIssuesReportVO>();
		EligibilityIssuesReportVO eligibilityIssuesReportVO = null;

		while (resultSet.next()) {
			eligibilityIssuesReportVO = new EligibilityIssuesReportVO();
			eligibilityIssuesReportVO.setProfileId(resultSet
					.getBigDecimal(Column.PROFILE_ID));
			eligibilityIssuesReportVO.setContractId(resultSet
					.getInt(Column.CONTRACT_ID));
			eligibilityIssuesReportVO.setLastName(resultSet
					.getString(Column.LAST_NAME));
			eligibilityIssuesReportVO.setFirstName(resultSet
					.getString(Column.FIRST_NAME));
			eligibilityIssuesReportVO.setMiddleName(resultSet
					.getString(Column.MIDDLE_INITIAL));
			eligibilityIssuesReportVO.setSsn(resultSet
					.getString(Column.SOCIAL_SECURITY_NO));
			eligibilityIssuesReportVO.setEmployeeId(resultSet
					.getString(Column.EMPLOYEE_ID));
			eligibilityIssuesReportVO.setDivision(resultSet
					.getString(Column.EMPLOYER_DIVISION));
			eligibilityIssuesReportVO.setEligibilityIndicator(resultSet
					.getString(Column.PLAN_ELIGIBLE_IND));
			eligibilityIssuesReportVO.setMoneyTypes(resultSet
					.getString(Column.MONEY_TYPE_LIST));
			eligibilityIssuesReportVO.setPlanEntryDate(resultSet
					.getDate(Column.ELIGIBLE_PLAN_ENTRY_DATE));
			eligibilityIssuesReportVO.setTransactionType(resultSet
					.getString(Column.INELIGIBLE_REASON_CODE));
			eligibilityIssuesReportVO.setEnrollmentMethod(resultSet
					.getString(Column.ENROLLMENT_METHOD_CODE));
			eligibilityIssuesReportVO.setEnrollmentEffectiveDate(resultSet
					.getDate(Column.MFC_CONTRACT_ENROLLMENT_DATE));
			eligibilityIssuesReportVO.setTransactionEffectiveDate(resultSet
					.getDate(Column.TRANSACTION_EFFECTIVE_DATE));
			eligibilityIssuesReportVO.setTransactionDate(resultSet
					.getDate(Column.TRANSACTION_DATE));
			eligibilityIssuesReportVO.setEnrollmentProcessedDate(resultSet
					.getDate(Column.ENROLLMENT_PROCESSED_DATE));
			eligibilityIssuesReportVO.setPayrollApplicableDate(resultSet
					.getDate(Column.PAYROLL_APPLICABLE_DATE));
			eligibilityIssuesReportVO.setSubmissionNumber(resultSet
					.getInt(Column.SUBMISSION_ID));
			eligibilityIssuesReportVO.setTransactionId(resultSet
					.getLong(Column.TRANSACTION_ID));

			eligibilityIssuesReportVOList.add(eligibilityIssuesReportVO);
		}

		return eligibilityIssuesReportVOList;
	}
}
