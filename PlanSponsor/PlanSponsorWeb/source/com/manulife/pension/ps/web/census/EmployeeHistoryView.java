package com.manulife.pension.ps.web.census;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO;

/**
 * Provide a View of Employee Previous Value
 * @author guweigu
 *
 */
public class EmployeeHistoryView implements EmployeeSnapshotProperties {
	private static final Map<String, String> NameMapping = new HashMap<String, String>();
	static {
		NameMapping.put("EMPLOYEE_CONTRACT.FIRST_NAME", FirstName);
        NameMapping.put("EMPLOYEE_CONTRACT.MIDDLE_INITIAL", MiddleInitial);
		NameMapping.put("EMPLOYEE_CONTRACT.LAST_NAME", LastName);
        NameMapping.put("EMPLOYEE_CONTRACT.NAME_PREFIX", Prefix);
		NameMapping.put("EMPLOYEE_CONTRACT.SOCIAL_SECURITY_NO", SSN);
        NameMapping.put("EMPLOYEE_CONTRACT.EMPLOYER_DIVISION", Division);        
		NameMapping.put("EMPLOYEE_CONTRACT.EMPLOYEE_ID", EmployeeId);
		NameMapping.put("EMPLOYEE_CONTRACT.EMPLOYMENT_STATUS_CODE", EmploymentStatus);
		NameMapping.put("EMPLOYEE_CONTRACT.EMPLOYMENT_STATUS_EFF_DATE", EmploymentStatusEffectiveDate);
		NameMapping.put("EMPLOYEE_CONTRACT.PLAN_PARTICIPATION_DATE", PlanParticipationDate);
		NameMapping.put("EMPLOYEE_CONTRACT.BIRTH_DATE", BirthDate);
		NameMapping.put("EMPLOYEE_CONTRACT.HIRE_DATE", HireDate);
		NameMapping.put("EMPLOYEE_CONTRACT.PLAN_ELIGIBLE_IND", EligibilityInd);
		NameMapping.put("EMPLOYEE_CONTRACT.ELIGIBILITY_DATE", EligibilityDate);
        
		NameMapping.put("EMPLOYEE_ADDRESS_HISTORY.ADDR_LINE1", AddressLine1);
		NameMapping.put("EMPLOYEE_ADDRESS_HISTORY.ADDR_LINE2", AddressLine2);
		NameMapping.put("EMPLOYEE_ADDRESS_HISTORY.CITY_NAME", City);
		NameMapping.put("EMPLOYEE_ADDRESS_HISTORY.STATE_CODE", State);
		NameMapping.put("EMPLOYEE_CONTRACT.RESIDENCE_STATE_CODE", StateOfResidence);
		NameMapping.put("EMPLOYEE_ADDRESS_HISTORY.COUNTRY_NAME", Country);
		NameMapping.put("EMPLOYEE_ADDRESS_HISTORY.ZIP_CODE", ZipCode);

        NameMapping.put("EMPLOYEE_CONTRACT.EMPLOYER_PROVIDED_EMAIL_ADDR", EmailAddress);
        NameMapping.put("EMPLOYEE_CONTRACT.MASK_SENSITIVE_INFO_COMMENTS", MaskSensitiveInformationComment);
        NameMapping.put("EMPLOYEE_CONTRACT.MASK_SENSITIVE_INFO_IND", MaskSensitiveInformation);
        NameMapping.put("EMPLOYEE_CONTRACT.AUTO_ENROLL_OPT_OUT_IND", OptOut);
        NameMapping.put("EMPLOYEE_CONTRACT.AE_90DAYS_OPTOUT_IND", Ae90DaysOptOut);
        NameMapping.put("EMPLOYEE_CONTRACT.DESIG_ROTH_DEF_AMT", DesignatedRothDefAmt);
        NameMapping.put("EMPLOYEE_CONTRACT.BEFORE_TAX_DEFER_AMT", BeforeTaxFlatDef);
        NameMapping.put("EMPLOYEE_CONTRACT.DESIG_ROTH_DEF_PCT", DesignatedRothDefPer);
        NameMapping.put("EMPLOYEE_CONTRACT.BEFORE_TAX_DEFER_PCT", BeforeTaxDefPer);
        NameMapping.put("EMPLOYEE_CONTRACT.ANNUAL_BASE_SALARY", AnnualBaseSalary);
        
        NameMapping.put("EMPLOYEE_CONTRACT.BEFORE_TAX_DEFERRAL_PCT", BeforeTaxDefPer);
        NameMapping.put("EMPLOYEE_CONTRACT.BEFORE_TAX_DEFERRAL_PCT", BeforeTaxDefPer);
        
        NameMapping.put("EMPLOYEE_CONTRACT.PLAN_YTD_HRS_WORKED", YearToDatePlanHoursWorked);
        NameMapping.put("EMPLOYEE_CONTRACT.PLAN_YTD_HRS_WORK_COMP_EFF_DT", YearToDatePlanHoursEffDate);
        NameMapping.put("EMPLOYEE_CONTRACT.PLAN_YTD_COMP", PlanYearToDateComp);
        
		//NameMapping.put("EMPLOYEE_CONTRACT.BIRTH_DATE", EmailAddress);
	}
	private Map<String,EmployeeChangeHistoryVO> propertyMap = new HashMap<String,EmployeeChangeHistoryVO>();
	
	public EmployeeHistoryView(List<EmployeeChangeHistoryVO> historyList) {
		createEmployeeHistoryMap(historyList);
	}
	
	private void createEmployeeHistoryMap(List<EmployeeChangeHistoryVO> historyList) {
		if (historyList == null) {
			return;
		}
		
		for (EmployeeChangeHistoryVO change : historyList) {
			String key = NameMapping.get(change.getTableName() + "." + change.getColumnName());
			if (key != null) {
				propertyMap.put(key, change);
			}
		}
	}
	
	public EmployeeChangeHistoryVO getValue(String name) {
		return propertyMap.get(name);
	}
    
    public Map<String,EmployeeChangeHistoryVO> getPropertyMap() {
        return propertyMap;
    }
}
