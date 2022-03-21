/**
 * 
 */
package com.manulife.pension.ps.web.census;

/**
 * Defines the common constants census related web tier component
 * will use.
 * 
 * @author guweigu
 *
 */
public interface CensusConstants {
	/**
	 * Source page name
	 */
	final String HOME = "home";
	final String CENSUS_SUMMARY_PAGE = "censusSummary";
	final String CENSUS_VESTING_PAGE = "censusVesting";
	final String EMPLOYEE_SNAPSHOT_PAGE = "employeeSnapshot";
	final String ADDRESS_HISTORY = "addressHistory";
	final String ADDRESS_SUMMARY_PAGE = "addressSummary";
	final String ELIGIBILITY_SUMMARY_PAGE = "eligibilitySummary";
	final String PARTICIPANT_ACCOUNT_PAGE = "participantAccount";
	final String VESTING_INFO_PAGE = "vestingInformation";
	final String DEFERRAL_PAGE = "deferral";
	final String ELIGIBILITY_INFO_PAGE = "eligibilityInformation";
	final String RESET_PWD_PAGE = "resetPassword";
	final String SAMPLE_SOURCE = "eligibilitySummary,censusSummary";
	/**
	 * mapping constants.
	 */


	final String VIEW_EMPLOYEE_SNAPSHOT_PAGE = "viewEmployeeSnapshot";
	final String EDIT_EMPLOYEE_SNAPSHOT_PAGE = "editEmployeeSnapshot";
	final String ADD_EMPLOYEE_PAGE = "addEmployee";

	final String EMPLOYMENT_STATUS_ACTIVE = "A";
	final String EMPLOYMENT_STATUS_CANCEL = "C";
	final String EMPLOYMENT_STATUS_DISABLED = "P";
	final String EMPLOYMENT_STATUS_TERMINATED = "T";
	final String EMPLOYMENT_STATUS_RETIRED = "R";
	final String EMPLOYMENT_STATUS_DECEASED = "D";

	final String USA = "USA";

	final String PED_YEAR_LIST = "PED_YEAR_LIST";
	final String PED_MONTH_DAY_LIST = "PED_MONTH_DAY_LIST";
	final String NEXT_PED = "NEXT_PED";
	final String NEXT_OPT_OUT = "NEXT_OPT_OUT";

	/**
	 * Size constants
	 */
	final int MaxLengthOfMaskSensitiveInfoComment = 254;

	/**
	 * Vesting information session attribute name
	 */
	final String VESTING_INFO = "vestingInfo";

	/**
	 * Request attribute names used in employee snapshot
	 */
	final String EMPLOYEE_ATTRIBUTE = "employee";
	final String EMPLOYEE_STATUS_HISTORY_ATTRIBUTE = "employeeStatusHistory";
	final String EMPLOYEE_HISTORY_ATTRIBUTE = "employeeHistory";
	final String EMPLOYEE_PLAN_HOURS_WORKED_HISTORY ="employeePlanHoursWorkedHistory";
	final String EMPLOYEE_DUPLICATE_DATA = "employeeData";
	
}