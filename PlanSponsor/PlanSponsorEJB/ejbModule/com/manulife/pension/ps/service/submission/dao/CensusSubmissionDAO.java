package com.manulife.pension.ps.service.submission.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.intware.dao.jdbc.SelectBeanQueryHandler;
import com.intware.dao.jdbc.SelectSingleOrNoValueQueryHandler;
import com.intware.dao.jdbc.SelectSingleValueQueryHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.submission.valueobject.CensusSubmissionReportData;
import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionHistoryReportData;
import com.manulife.pension.ps.service.submission.dao.SubmissionConstants.Census;
import com.manulife.pension.ps.service.submission.util.SubmissionErrorHelper;
import com.manulife.pension.ps.service.submission.valueobject.CensusSubmissionItem;
import com.manulife.pension.ps.service.submission.valueobject.ReportDataErrors;
import com.manulife.pension.service.contract.dao.ContractDAO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.util.EmployeeValidationErrorCode;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.submission.SubmissionError;
import com.manulife.pension.submission.util.SubmissionErrorComparator;

/**
 * @author parkand
 *
 */
public class CensusSubmissionDAO extends BaseDatabaseDAO {

	private static final String className = CensusSubmissionDAO.class.getName();
	private static final Logger logger = Logger.getLogger(CensusSubmissionDAO.class);
	private static final Map statusMap = new HashMap(6);
    private static final Map fieldToColumnMap = new HashMap();

	private static final int STP_ERROR_CATEGORY_WARNINGS_ONLY = 1;

	// Note: these display statuses are not used - only here for clarification.  
	// actionStates.xml has the display statuses used on the web
	static {
		statusMap.put(Census.PROCESS_STATUS_CODE_OUTSTANDING_ERROR_1,new CensusStatus(Census.PROCESS_STATUS_CODE_OUTSTANDING_ERROR_1,"Errors outstanding. Please fix errors.",1));
		statusMap.put(Census.PROCESS_STATUS_CODE_OUTSTANDING_ERROR_2,new CensusStatus(Census.PROCESS_STATUS_CODE_OUTSTANDING_ERROR_2,"Errors outstanding. Please fix errors.",2));
		statusMap.put(Census.PROCESS_STATUS_CODE_SUBMISSION_ERROR,new CensusStatus(Census.PROCESS_STATUS_CODE_SUBMISSION_ERROR,"Submission Error. Contact your CAR",3));
		statusMap.put(Census.PROCESS_STATUS_CODE_PROCESSING,new CensusStatus(Census.PROCESS_STATUS_CODE_PROCESSING,"Processing",4));
		statusMap.put(Census.PROCESS_STATUS_CODE_COMPLETE,new CensusStatus(Census.PROCESS_STATUS_CODE_COMPLETE,"Complete",5));
		statusMap.put(Census.PROCESS_STATUS_CODE_CANCELLED,new CensusStatus(Census.PROCESS_STATUS_CODE_CANCELLED,"Cancelled",6));
	}
    
    static {
        fieldToColumnMap.put("name", "LAST_NAME");
        fieldToColumnMap.put("ssn", "SOCIAL_SECURITY_NUMBER");
        fieldToColumnMap.put("employeeNumber", "EMPLOYEE_ID");
        fieldToColumnMap.put("sourceRecordNo", "SOURCE_RECORD_NO");
        fieldToColumnMap.put("status", "PROCESS_STATUS_CODE");
        fieldToColumnMap.put("division", "EMPLOYER_DIVISION");
    }
	
    private static final String SQL_SELECT_CENSUS_SUBMISSION = 
        "SELECT "
            + STP_SCHEMA_NAME + "STP_ERR_CAT(ERROR_COND_STRING) errorCategory,"
            + "SUBMISSION_ID submissionId, "
            + "CONTRACT_ID contractNumber, "
            + "SEQUENCE_NO sequenceNumber, "
            + "FIRST_NAME firstName, "
            + "LAST_NAME lastName, "
            + "SOCIAL_SECURITY_NUMBER ssn, "
            + "EMPLOYEE_ID employeeNumber, " 
            + "ADDR_LINE1 addressLine1, "
            + "ADDR_LINE2 addressLine2, "
            + "CITY_NAME city, "
            + "STATE_CODE stateCode, "
            + "ZIP_CODE zipCode, "
            + "COUNTRY_NAME country, "
            + "NAME_PREFIX namePrefix, "
            + "MIDDLE_INITIAL middleInitial, "
            + "PROCESSED_TS processedTS, "
            + "PROCESS_STATUS_CODE processStatus, "
            + "RESIDENCE_STATE_CODE stateOfResidence, "
            + "ERROR_COND_STRING errorConditionString, "
            + "EMPLOYER_PROVIDED_EMAIL_ADDR employeeProvidedEmail, "
            + "EMPLOYER_DIVISION division, "
            + "SOURCE_RECORD_NO sourceRecordNo, "
            + "PROCESSOR_USER_ID userId, "
            + "CREATED_USER_ID createdUserId, "
            + "CREATED_TS createdTS, "
            + "LAST_UPDATED_USER_ID lastUpdatedUserId, "
            + "LAST_UPDATED_TS lastUpdatedTS, "
            + "EMPLOYMENT_STATUS_CODE employeeStatus, "      
            + "EMPLOYMENT_STATUS_EFF_DATE employeeStatusDate, "
            + "BIRTH_DATE birthDate, "
            + "HIRE_DATE hireDate, "
            + "PLAN_ENTRY_DATE planDate, "        
            + "PLAN_ELIGIBLE_IND eligibilityIndicator, "
            + "ELIGIBILITY_CLASS eligibilityClass, "
            + "ELIGIBILITY_DATE eligibilityDate, "
            + "PLAN_YTD_HRS_WORKED planYTDHoursWorked, "
            + "ANNUAL_BASE_SALARY annualBaseSalary, "
            + "BEFORE_TAX_DEFERRAL_PCT beforeTaxDeferralPerc, "
            + "DESIG_ROTH_DEF_PCT desigRothDeferralPerc, "
            + "PLAN_YTD_HRS_WORK_COMP_EFF_DT planYTDHoursWorkedEffDate, "
            + "AUTO_ENROLL_OPT_OUT_IND optOutIndicator, "
            + "PLAN_YTD_COMP planYTDCompensation, "
            + "BEFORE_TAX_DEFER_AMT  beforeTaxDeferralAmt, "
            + "DESIG_ROTH_DEF_AMT desigRothDeferralAmt "
        + "FROM " + STP_SCHEMA_NAME + "EMPLOYEE_CENSUS "
        + "WHERE PROCESS_STATUS_CODE != '" + Census.PROCESS_STATUS_CODE_SYNTAX_CHECK_FAILED + "' " 
        + "AND SUBMISSION_ID=? AND CONTRACT_ID=? ";
    
    private static final String DEFAULT_SORT = " 1 DESC, SOURCE_RECORD_NO ASC ";
	
	private static final String SQL_SELECT_EMPLOYEE_ID_WHERE_CLAUSE = 
		" AND EMPLOYEE_ID=?";
    
    private static final String SQL_SELECT_SSN_WHERE_CLAUSE = 
        " AND SOCIAL_SECURITY_NUMBER=?";
	
	private static final String SQL_SELECT_ERROR_CATEGORY_WHERE_CLAUSE = 
		 "WHERE errorCategory > ?";

	private static final String SQL_SELECT_EXCLUDE_CANCELLED_CENSUS_WHERE_CLAUSE = " AND PROCESS_STATUS_CODE <> '"
			+ Census.PROCESS_STATUS_CODE_CANCELLED + "'";
	
	private static final String SQL_SELECT_EXCLUDE_COMPLETED_CENSUS_WHERE_CLAUSE = " AND PROCESS_STATUS_CODE <> '"
		+ Census.PROCESS_STATUS_CODE_COMPLETE + "'";

	private static final String SQL_SELECT_COUNT_SYNTACTIC_ERRORS =
		"SELECT "
			+ "COUNT (*) "
		+ "FROM " + STP_SCHEMA_NAME + "EMPLOYEE_CENSUS "
		+ "WHERE PROCESS_STATUS_CODE='" + Census.PROCESS_STATUS_CODE_SYNTAX_CHECK_FAILED + "' AND SUBMISSION_ID=? and CONTRACT_ID=?";
	
	private static final String SQL_SELECT_SYNTAX_ERROR_INDICATOR =
		"SELECT "
			+ "SYNTAX_ERROR_IND "
		+ "FROM " + STP_SCHEMA_NAME + "SUBMISSION_CASE "
		+ "WHERE SUBMISSION_CASE_TYPE_CODE in ('" + SubmissionConstants.SUBMISSION_CASE_TYPE_ADDRESS + "','" 
        + SubmissionConstants.SUBMISSION_CASE_TYPE_CENSUS + "') and SUBMISSION_ID=? and CONTRACT_ID=?";
	
	private static final String SQL_UPDATE_EMPLOYEE_CENSUS_ADDRESS = 
		"update " + STP_SCHEMA_NAME + "employee_census " 
		+ "set "
		+ "ADDR_LINE1 = ?, "
		+ "ADDR_LINE2 = ?, "
		+ "CITY_NAME = ?, "
		+ "STATE_CODE = ?, "
		+ "ZIP_CODE = ?, "
		+ "COUNTRY_NAME = ?, "
		+ "PROCESS_STATUS_CODE = ?, "
		+ "ERROR_COND_STRING = ?, "
		+ "LAST_UPDATED_USER_ID = ?, "
		+ "LAST_UPDATED_TS = CURRENT TIMESTAMP "
		+ "where " 
		+ "SUBMISSION_ID = ? and contract_id=? and employer_designated_id=?";
	
	private static final String SQL_CANCEL_EMPLOYEE_CENSUS =
		"update " + STP_SCHEMA_NAME + "employee_census " 
		+ "set "
		+ "PROCESS_STATUS_CODE = '" + Census.PROCESS_STATUS_CODE_CANCELLED + "', "
		+ "LAST_UPDATED_USER_ID = ?, "
		+ "LAST_UPDATED_TS = CURRENT TIMESTAMP "
		+ "where " 
		+ "contract_id = ? and submission_id = ? and sequence_no = ? ";
	
	private static final String SQL_UPDATE_EMPLOYEE_CENSUS_PROCESS_STATUS_CODE_AND_ERROR_COND_STRING =
		"update " + STP_SCHEMA_NAME + "employee_census " 
		+ "set "
		+ "PROCESS_STATUS_CODE = ?, ERROR_COND_STRING = ?, "
		+ "LAST_UPDATED_USER_ID = ?, "
		+ "LAST_UPDATED_TS = CURRENT TIMESTAMP "
		+ "where " 
		+ "contract_id=? and submission_id = ? and sequence_no = ? ";

	private static final String SQL_DISCARD_SUBMISSION_CASE = 
		"update " + STP_SCHEMA_NAME + "submission_case "
		+ "set "
		+ "SYNTAX_ERROR_IND = 'N', "
		+ "LAST_UPDATED_USER_ID = ?, "
		+ "LAST_UPDATED_TS = CURRENT TIMESTAMP "
		+ "where " 
		+ "SUBMISSION_ID = ? and contract_id=? and submission_case_type_code= ?";

	private static final String SQL_LOG_DISCARD_SUBMISSION_CASE = 
		"insert into " + STP_SCHEMA_NAME + "submission_log_event "
		+ "(EVENT_PROCESS_CODE, EVENT_TYPE_CODE, SUBMISSION_ID, CONTRACT_ID, PROCESSOR_USER_ID, CREATED_USER_ID, CREATED_TS) "
		+ "values ('PS','DS',?,?,?,?,CURRENT TIMESTAMP)";

	private static final String SQL_UPDATE_DUPLICATE_EMAIL_ID_BLANK = "UPDATE "
		+ STP_SCHEMA_NAME
		+ "employee_census set "
		+ " EMPLOYER_PROVIDED_EMAIL_ADDR = ?, LAST_UPDATED_USER_ID = ?, LAST_UPDATED_TS = ? " 
		+ "WHERE CONTRACT_ID = ? AND SUBMISSION_ID = ? AND LOWER(EMPLOYER_PROVIDED_EMAIL_ADDR) = ?"
		+ " AND SEQUENCE_NO <> ? AND PROCESS_STATUS_CODE <> '99' AND PROCESS_STATUS_CODE <> '16'";
	
	private CensusSubmissionDAO() {
	}

	private static List getParams(ReportCriteria criteria) throws SystemException {
		List params = new ArrayList();
		if ( criteria.getFilterValue(CensusSubmissionReportData.FILTER_SUBMISSION_ID) != null) {
			params.add(criteria.getFilterValue(CensusSubmissionReportData.FILTER_SUBMISSION_ID));
		} else {
			params.add(criteria.getFilterValue(CensusSubmissionReportData.FILTER_SUBMISSION_NO));
		}
		params.add(criteria.getFilterValue(CensusSubmissionReportData.FILTER_CONTRACT_NO));
        
		return params;
	}

    private static String createSortPhrase(final ReportCriteria criteria) {
         StringBuffer result = new StringBuffer();
         Iterator sorts = criteria.getSorts().iterator();
         String sortDirection = null; 
        
         for (int i = 0; sorts.hasNext(); i++) {
             ReportSort sort = (ReportSort) sorts.next();
             
             if (i==0) sortDirection = sort.getSortDirection();
             
             if ("name".equals(sort.getSortField())) {
                 result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
                 result.append(sortDirection);
                 result.append(", FIRST_NAME ").append(sortDirection);
                 result.append(", MIDDLE_INITIAL ").append(sortDirection);
             } else {
                 result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
                 // source record number is always in ASC order, unless the first sort
                 if (CensusSubmissionReportData.SORT_RECORD_NUMBER.equals(sort.getSortField()) && (i!=0)) {
                     result.append(sort.ASC_DIRECTION);
                 } else {
                     result.append(sort.getSortDirection());
                 }
             }
                                                      
             result.append(',');
         }
                
         if (result.length() > 0) {
        	 /*
        	  * Delete the last ','
        	  */
             result.deleteCharAt(result.length() - 1);
         } else {
             result.append(DEFAULT_SORT);
         }
         result.insert(0, " ORDER BY ");
         return result.toString();
    }

	
	public static CensusSubmissionItem getCensusSubmissionItem(int contractNumber, int submissionId, String employerDesignatedId) throws SystemException {
		logger.debug("entry -> getCensusSubmissionItem");
		
		CensusSubmissionItem item = null;
        
        // get the contract sort option code
        String contractSortOption = ContractDAO.getSortOptionCode(contractNumber);
		
		StringBuffer query = new StringBuffer(SQL_SELECT_CENSUS_SUBMISSION);
        if (Constants.EMPLOYEE_ID_SORT_OPTION_CODE.equals(contractSortOption)) {
            query.append(SQL_SELECT_EMPLOYEE_ID_WHERE_CLAUSE);
        } else {
            query.append(SQL_SELECT_SSN_WHERE_CLAUSE);
        }
		
		
		SelectBeanQueryHandler handler = new SelectBeanQueryHandler(SUBMISSION_DATA_SOURCE_NAME, query.toString(), CensusSubmissionItem.class);
		try {
			logger.debug("Executing Prepared SQL Statement: " + query);
			item = (CensusSubmissionItem)handler.select(new Object[] { submissionId, contractNumber, employerDesignatedId });
			
			// populate the sort option code for this contract
			item.setSortOptionCode(contractSortOption);
			
			// convert the country to upper case (required to match against country list)
			item.setCountry(item.getCountry().trim().toUpperCase());
			item.setZipCode(item.getZipCode().trim());
			item.setStateCode(item.getStateCode().trim().toUpperCase());

            // sets the errors
            item.setErrors(SubmissionErrorHelper.parseErrorConditionString(item
                    .getErrorConditionString(), item.getSourceRecordNo().intValue()));
            
		} catch (DAOException e) {
			throw new SystemException(e, className, "getCensusSubmissionItem", "Problem occurred prepared call: " + query.toString());
		}
		
		logger.debug("exit <- getCensusSubmissionItem");
		
		return item;
	}

	/**
	 * Returns the list of census submissions that are in error sorted according
	 * to the given sort criteria.
	 * 
	 * @param criteria
	 * @return
	 * @throws SystemException
	 */
	public static List<CensusSubmissionItem> getCensusSubmissionsInError(
			ReportCriteria criteria) throws SystemException {

		logger.debug("entry -> getCensusSubmissionsInError");

		Integer contractNumber = (Integer) criteria
				.getFilterValue(SubmissionHistoryReportData.FILTER_CONTRACT_NO);

		// add sort order
		String sql = "SELECT * FROM (" + SQL_SELECT_CENSUS_SUBMISSION
				+ SQL_SELECT_EXCLUDE_CANCELLED_CENSUS_WHERE_CLAUSE
				+ SQL_SELECT_EXCLUDE_COMPLETED_CENSUS_WHERE_CLAUSE
				+ createSortPhrase(criteria) + ") A "
				+ SQL_SELECT_ERROR_CATEGORY_WHERE_CLAUSE;

		List<CensusSubmissionItem> resultList = new ArrayList<CensusSubmissionItem>();

		if (contractNumber != null) {

			SelectBeanListQueryHandler handler = new SelectBeanListQueryHandler(
					SUBMISSION_DATA_SOURCE_NAME, sql,
					CensusSubmissionItem.class);

			try {
				List params = getParams(criteria);
				params.add(STP_ERROR_CATEGORY_WARNINGS_ONLY);

				logger.debug("Executing Prepared SQL Statement: " + sql);
				resultList = (List<CensusSubmissionItem>) handler.select(params
						.toArray());

			} catch (DAOException e) {
				throw new SystemException(e, className,
						"getCensusSubmissionsInError",
						"Problem occurred for prepared call: " + sql);
			}
		}
		logger.debug("exit <- getCensusSubmissionsInError");
		return resultList;
	}
	
	public static int getNumberOfCensusSubmissionsInError(
			ReportCriteria criteria) throws SystemException {

		logger.debug("entry -> getNumberOfCensusSubmissionsInError");

		Integer contractNumber = (Integer) criteria
				.getFilterValue(SubmissionHistoryReportData.FILTER_CONTRACT_NO);

		// add sort order
		String sql = "SELECT COUNT(*) FROM (" + SQL_SELECT_CENSUS_SUBMISSION
				+ SQL_SELECT_EXCLUDE_CANCELLED_CENSUS_WHERE_CLAUSE
				+ SQL_SELECT_EXCLUDE_COMPLETED_CENSUS_WHERE_CLAUSE
				+ createSortPhrase(criteria) + ") A "
				+ SQL_SELECT_ERROR_CATEGORY_WHERE_CLAUSE;

		Integer result = null;

		if (contractNumber != null) {

			SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
					SUBMISSION_DATA_SOURCE_NAME, sql, Integer.class);

			try {
				List params = getParams(criteria);
				params.add(STP_ERROR_CATEGORY_WARNINGS_ONLY);

				logger.debug("Executing Prepared SQL Statement: " + sql);
				result = (Integer) handler.select(params.toArray());

			} catch (DAOException e) {
				throw new SystemException(e, className,
						"getCensusSubmissionsInError",
						"Problem occurred for prepared call: " + sql);
			}
		}
		logger.debug("exit <- getNumberOfCensusSubmissionsInError");
		return result;
	}

	public static CensusSubmissionReportData getCensusSubmissions(ReportCriteria criteria) throws SystemException {
		logger.debug("entry -> getCensusSubmissions");
		
		CensusSubmissionReportData reportDataVO = null;
		
		Integer contractNumber = (Integer)criteria.getFilterValue(SubmissionHistoryReportData.FILTER_CONTRACT_NO);
        
        // add sort order
        String sql = SQL_SELECT_CENSUS_SUBMISSION + createSortPhrase(criteria);
		
		if (contractNumber != null) {
				
			SelectBeanListQueryHandler handler = 
				new SelectBeanListQueryHandler(SUBMISSION_DATA_SOURCE_NAME, sql, CensusSubmissionItem.class);
			
			try {
				logger.debug("Executing Prepared SQL Statement: "+ sql);
				List resultList = (List)handler.select(getParams(criteria).toArray());
				
				// get the sort option code for this contract
				String sortOptionCode = ContractDAO.getSortOptionCode(contractNumber.intValue());
				
				// iterate through the list and set the display status, status sort order and errors for each
				Iterator iter = resultList.iterator();
				reportDataVO = new CensusSubmissionReportData(criteria, resultList.size());
				ReportDataErrors reportDataErrors = new ReportDataErrors(false);
				while (iter.hasNext()) {
					CensusSubmissionItem item = (CensusSubmissionItem)iter.next();
					CensusStatus status = (CensusStatus)statusMap.get(item.getProcessStatus());
					if (status == null) {
						// for now just use the status code if it's not recognized
						// and set the sort order code to something high so it's moved to the end of the list
						status = new CensusStatus(item.getProcessStatus(),item.getProcessStatus(),99);
					}
					item.setDisplayStatus(status.getDisplayStatus());
					item.setStatusSortOder(status.getDisplayOrder());
                    
                    // set the employee identifier based on contract sort option code
                    item.setEmpId(sortOptionCode);
					
					// set the sort option code 
					item.setSortOptionCode(sortOptionCode);		
					
					// deal with the errors
					item.setErrors(SubmissionErrorHelper.parseErrorConditionString(item.getErrorConditionString(),item.getSourceRecordNo().intValue()));
                    
					// adding arguments to some errors in the collection
                    SubmissionErrorHelper.addArgument(item.getErrors(), EmployeeValidationErrorCode.DuplicateSubmittedSSN.getStpCode(), EmployeeData.formatSSN(item.getSsn()));
                    SubmissionErrorHelper.addArgument(item.getErrors(), EmployeeValidationErrorCode.DuplicateSubmittedEmployeeId.getStpCode(), item.getEmployeeNumber());
					
					// commented out - currently no way to count syntax errors; just check the syntax error indicator
					// reportDataErrors.setNumSyntaxErrors(countSyntacticErrors(criteria));
					
					// deal with individual fields that could be in error
                    item.setContractNumberStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(), SubmissionErrorHelper.CONTRACT_ID_FIELD_KEY));
                    item.setParticipantIdStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(), SubmissionErrorHelper.PARTICIPANT_ID_FIELD_KEY));
                    item.setFirstNameStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.FIRST_NAME_FIELD_KEY));
                    // if any of full name or last name fields found in error string, display error icon in last name field
                    int lastNameStatus = SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.LAST_NAME_FIELD_KEY);
                    int fullNameStatus = SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.FULL_NAME_FIELD_KEY);
                    if (lastNameStatus == CensusSubmissionItem.ERROR || fullNameStatus == CensusSubmissionItem.ERROR)
                        item.setLastNameStatus(CensusSubmissionItem.ERROR);
                    else 
                        item.setLastNameStatus(CensusSubmissionItem.OK);
					item.setAddress1Status(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.ADDRESS1_FIELD_KEY));
                    item.setAddress2Status(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.ADDRESS2_FIELD_KEY));
					item.setCityStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.CITY_FIELD_KEY));
					item.setStateStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.STATE_FIELD_KEY));
					item.setZipStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.ZIP_FIELD_KEY));
                    item.setCountryStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.COUNTRY_FIELD_KEY));
					item.setSsnStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.SSN_FIELD_KEY));
                    item.setEmployeeNumberStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.EMPLOYEE_NUMBER_FIELD_KEY));
                    item.setBirthDateStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.BIRTH_DATE_FIELD_KEY));
                    item.setHireDateStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.HIRE_DATE_FIELD_KEY));
                    item.setStateOfResidenceStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.STATE_OF_RESIDENCE_FIELD_KEY));
                    item.setNamePrefixStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.NAME_PREFIX_FIELD_KEY));
                    item.setMiddleInitialStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.MIDDLE_INITIAL_FIELD_KEY));
                    item.setDivisionStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.DIVISION_FIELD_KEY));
                    item.setEmployeeProvidedEmailStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.EMAIL_FIELD_KEY));
                    item.setEmployeeStatusStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.EMPLOYEE_STATUS_FIELD_KEY));
                    item.setEmployeeStatusDateStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.EMPLOYEE_STATUS_DATE_FIELD_KEY));
                    item.setPlanYTDHoursWorkedStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.YTD_HOURS_WORKED_FIELD_KEY));
                    item.setPlanYTDHoursWorkedEffDateStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.YTD_HOURS_WORKED_EFFDATE_FIELD_KEY));
                    item.setEligibilityIndicatorStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.ELIGIBILITY_INDICATOR_FIELD_KEY));
                    item.setEligibilityDateStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.ELIGIBILITY_DATE_FIELD_KEY));
                    item.setOptOutIndicatorStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.OPT_OUT_INDICATOR_FIELD_KEY));
                    item.setBeforeTaxDeferralPercStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.BEFORE_TAX_DEFER_PERCENTAGE_FIELD_KEY));
                    item.setDesigRothDeferralPercStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.DESIG_ROTH_DEFER_PERCENTAGE_FIELD_KEY));
                    item.setBeforeTaxDeferralAmtStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.BEFORE_TAX_DEFER_AMOUNT_FIELD_KEY));
                    item.setDesigRothDeferralAmtStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.DESIG_ROTH_DEFER_AMOUNT_FIELD_KEY));
                    item.setPlanYTDCompensationStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.PLAN_YTD_COMPENSATION_KEY));
                    item.setAnnualBaseSalaryStatus(SubmissionErrorHelper.getErrorStatus(item.getErrors(),SubmissionErrorHelper.ANNUAL_BASE_SALARY_KEY));
                    
					if ( reportDataVO.getContractNumber() == 0 ) {
						reportDataVO.setContractNumber((item.getContractNumber()).intValue());
					}
                    
                    
                    // figure out the height for each row in the Census Submission detail screen
                    // (if at least one icon is displayed and display status is short, row should be of double height)
                    if (item.getErrorCategory().intValue() > 0 && item.getDisplayStatus().length() <= 10) {
                        item.setDoubleRowHeight(true);
                    }
                    
                    // filter out duplicate error codes per each item
                    reportDataErrors.addErrors(filterDuplicateCodes(item.getErrors()));
					
					// convert the country to upper case (required to match against country list)
					item.setCountry(StringUtils.upperCase(StringUtils.trim(item.getCountry())));
					item.setZipCode(StringUtils.trim(item.getZipCode()));
					item.setStateCode(StringUtils.upperCase(StringUtils.trim(item.getStateCode())));

				}
				reportDataErrors.setSyntaxErrorIndicator(getSyntaxErrorIndicator(criteria));
				reportDataVO.setDetails(resultList);
				reportDataVO.setErrors(reportDataErrors);
			} catch (DAOException e) {
				throw new SystemException(e, className, "getCensusSubmissions", "Problem occurred for prepared call: " + SQL_SELECT_CENSUS_SUBMISSION);
			}
		}
		if ( reportDataVO == null ) {
			throw new SystemException("CensusSubmissionDAO.getCensusSubmission(): CensusSubmissionReportData is null");
		}
		logger.debug("exit <- getCensusSubmissions");
		return reportDataVO;
	}
	
    private static Collection filterDuplicateCodes(Collection itemErrors) {
        Collection errors = new TreeSet(new SubmissionErrorComparator());
        
        Collection contentIds = new ArrayList();
        // iterate through the submission errors for the given item
        Iterator iter = itemErrors.iterator();
        while (iter.hasNext()) {
            SubmissionError error = (SubmissionError)iter.next();
            String errorCode = error.getCode();
            String contentId = String.valueOf(error.getContentId());
            if (!error.hasParams() && contentIds.contains(contentId)) {
                // if message has no params and duplicate content id, filter out error item
            } else {
                contentIds.add(contentId);
                errors.add(error);
            }
        }
        
        return errors;
    }
    
	private static boolean getSyntaxErrorIndicator(ReportCriteria criteria) throws SystemException {
		boolean indicator = false;
		try {
			SelectSingleOrNoValueQueryHandler handler = 
				new SelectSingleOrNoValueQueryHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_SELECT_SYNTAX_ERROR_INDICATOR, String.class);
			String indicatorStr = (String)handler.select(getParams(criteria).toArray());
			if ("Y".equals(indicatorStr.toUpperCase())) {
				indicator = true;
			}
		} catch (DAOException e) {
			throw new SystemException(e, className, "getSyntaxErrorIndicator", "Problem occurred in prepared call: " + SQL_SELECT_SYNTAX_ERROR_INDICATOR);
		}
		return indicator;
	}
	
	/**
	private static final String SQL_UPDATE_PARTICIPANT_ADDRESS = 
		"update " + STP_SCHEMA_NAME + ".employee_census " 
		+ "set "
		+ "ADDR_LINE1 = ?, "
		+ "ADDR_LINE2 = ?, "
		+ "CITY_NAME = ?, "
		+ "STATE_CODE = ?, "
		+ "ZIP_CODE = ?, "
		+ "COUNTRY_NAME = ?, "
		+ "PROCESS_STATUS_CODE = ?, "
		+ "ERROR_COND_STRING = ?, "
		+ "LAST_UPDATED_USER_ID = ?, "
		+ "LAST_UPDATED_TS = CURRENT TIMESTAMP "
		+ "where " 
		+ "SUBMISSION_ID = ? and contract_id=? and employer_designated_id=?";
    */
	public static void updateAddress(CensusSubmissionItem address) throws SystemException {
		logger.debug("entry -> updateAddress");
		
		// build parameter array
		List<Object> params = new ArrayList<Object>(12);
		params.add(address.getAddressLine1());
		params.add(address.getAddressLine2());
		params.add(address.getCity());
		params.add(address.getStateCode());
		params.add(address.getZipCode());
		params.add(address.getCountry());
		params.add(address.getProcessStatus());
		params.add(address.getErrorConditionString());
		params.add(address.getLastUpdatedUserId());
		params.add(address.getSubmissionId());
		params.add(address.getContractNumber());
		if ( "SS".equals(address.getSortOptionCode()) ) {
			params.add(address.getSsn());
		} else {
			params.add(address.getEmpId());
		}
		
		SQLUpdateHandler handler = new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_UPDATE_EMPLOYEE_CENSUS_ADDRESS);
		try {
			logger.debug("Executing Prepared SQL Statement: " + SQL_UPDATE_EMPLOYEE_CENSUS_ADDRESS);
			handler.update(params.toArray());
		} catch (DAOException e) {
			throw new SystemException(e, className, "updateAddress", "Problem occurred prepared call: " + SQL_UPDATE_EMPLOYEE_CENSUS_ADDRESS);
		}
		
		logger.debug("exit <- updateAddress");
	}

	public static void cancelEmployeeCensus(Integer cnno, Integer subId, Integer seqNum, 
        String userId) throws SystemException {
        
		logger.debug("entry -> cancelEmployeeCensus");
        
        String sql = SQL_CANCEL_EMPLOYEE_CENSUS;
            
		SQLUpdateHandler handler = new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME, sql);
		try {
			logger.debug("Executing Prepared SQL Statement: " + sql);
			handler.update(new Object[] { userId, cnno, subId, seqNum });
		} catch (DAOException e) {
			throw new SystemException(e, className, "cancelEmployeeCensus", "Problem occurred for prepared call: " + sql);
		}
		
		logger.debug("exit <- cancelEmployeeCensus");
	}
	
	public static void updateEmployeeCensusProcessStatusCodeAndErrorCondString(
			Integer cnno, Integer subId, Integer seqNo, String userId,
			String processStatusCode, String errorCondString, String contractSortOptionCode)
			throws SystemException {
		logger.debug("entry -> updateEmployeeCensusProcessStatusCodeAndErrorCondString");
        
        String sql = SQL_UPDATE_EMPLOYEE_CENSUS_PROCESS_STATUS_CODE_AND_ERROR_COND_STRING;
            
		SQLUpdateHandler handler = new SQLUpdateHandler(
				SUBMISSION_DATA_SOURCE_NAME, sql );
		try {
			logger.debug("Executing Prepared SQL Statement: " + sql);
            
			handler.update(new Object[] { processStatusCode, errorCondString,
					userId, cnno, subId, seqNo });
		} catch (DAOException e) {
			throw new SystemException(
					e,
					className,
					"updateEmployeeCensusProcessStatusCodeAndErrorCondString",
					"Problem occurred prepared call: " + sql);
		}

		logger.debug("exit <- updateEmployeeCensusProcessStatusCodeAndErrorCondString");
	}

	public static void discardSubmissionCase(Integer subId, Integer cnno, String userId, String caseTypeCode) throws SystemException {
		logger.debug("entry -> discardSubmissionCase");
		
		SQLUpdateHandler handler = new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_DISCARD_SUBMISSION_CASE);
		try {
			logger.debug("Executing Prepared SQL Statement: " + SQL_DISCARD_SUBMISSION_CASE);
			handler.update(new Object[] { userId, subId, cnno, caseTypeCode });
		} catch (DAOException e) {
			throw new SystemException(e, className, "disableDiscardFlag", "Problem occurred prepared call: " + SQL_DISCARD_SUBMISSION_CASE);
		}
		
		logger.debug("exit <- discardSubmissionCase");
	}

	public static void logDiscardSubmissionCase(Integer subId, Integer cnno, String userId) throws SystemException {
		logger.debug("entry -> logDiscardSubmissionCase");
		
		SQLInsertHandler handler = new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_LOG_DISCARD_SUBMISSION_CASE);
		try {
			logger.debug("Executing Prepared SQL Statement: " + SQL_LOG_DISCARD_SUBMISSION_CASE);
			handler.insert(new Object[] { subId, cnno, userId, userId });
		} catch (DAOException e) {
			throw new SystemException(e, className, "logDiscardAddress", "Problem occurred prepared call: " + SQL_LOG_DISCARD_SUBMISSION_CASE);
		}
		
		logger.debug("exit <- logDiscardSubmissionCase");
	}

	/*
	public static int countSyntacticErrors(ReportCriteria criteria) throws SystemException {
		Integer count = new Integer(0);
		List params = new ArrayList();
		try {
			SelectSingleOrNoValueQueryHandler handler = 
				new SelectSingleOrNoValueQueryHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_SELECT_COUNT_SYNTACTIC_ERRORS, Integer.class);
			count = (Integer)handler.select(getParams(criteria).toArray());
		} catch (DAOException e) {
			throw new SystemException(e, className, "countSyntacticErrors", "Problem occurred prepared call: " + SQL_SELECT_COUNT_SYNTACTIC_ERRORS);
		}
		
		return count.intValue();
	}
	*/
	
	/**
	 * This method is to update the duplicate submission
	 * employerProvidedEmailAddress as blank.
	 * 
	 * @param ds
	 * @param contractId
	 * @param submissionId
	 * @param formSequenceNumber
	 * @param employerProvidedEmailAddress
	 * @param userId
	 * @throws DAOException
	 */

	public static void deleteDuplicateEmailAddressesOnFile(DataSource ds,
			Integer contractId, Integer submissionId, Integer formSequenceNumber,
			String employerProvidedEmailAddress, String userId) throws DAOException {
		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			connection = ds.getConnection();
			if (employerProvidedEmailAddress != null) {
				stmt = connection
						.prepareStatement(SQL_UPDATE_DUPLICATE_EMAIL_ID_BLANK);
				stmt.setNull(1, java.sql.Types.VARCHAR);
				stmt.setString(2, userId);
				stmt.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
				stmt.setInt(4, contractId);
				stmt.setInt(5, submissionId);
				stmt.setString(6, employerProvidedEmailAddress.toLowerCase());
				stmt.setInt(7, formSequenceNumber);
				int rowsUpdated = stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			close(stmt, connection);
		}
	}

}