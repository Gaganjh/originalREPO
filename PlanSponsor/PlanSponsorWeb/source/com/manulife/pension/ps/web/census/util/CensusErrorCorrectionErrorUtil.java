package com.manulife.pension.ps.web.census.util;

import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.util.EmployeeData.Property;

public class CensusErrorCorrectionErrorUtil extends AbstractEmployeeErrorUtil {

	private static CensusErrorCorrectionErrorUtil instance = null;

	protected final EmployeeData.Property[] errorDisplayOrder = {
			EmployeeData.Property.SOCIAL_SECURITY_NUMBER,
			EmployeeData.Property.EMPLOYEE_NUMBER,
			EmployeeData.Property.FIRST_NAME,
			EmployeeData.Property.LAST_NAME,
			EmployeeData.Property.MIDDLE_INIT,
			EmployeeData.Property.NAME_PREFIX,
			EmployeeData.Property.ADDRESS_LINE_1,
			EmployeeData.Property.ADDRESS_LINE_2,
			EmployeeData.Property.CITY_NAME,
			EmployeeData.Property.STATE_CODE,
			EmployeeData.Property.ZIP_CODE,
			EmployeeData.Property.COUNTRY_NAME,
			EmployeeData.Property.STATE_OF_RESIDENCE,
			EmployeeData.Property.EMPLOYER_PROVIDED_EMAIL,
			EmployeeData.Property.DIVISION,
			EmployeeData.Property.DATE_OF_BIRTH,
			EmployeeData.Property.HIRE_DATE,
			EmployeeData.Property.EMPLOYMENT_STATUS,
			EmployeeData.Property.EMPLOYMENT_STATUS_DATE,
			EmployeeData.Property.ELIGIBILITY_INDICATOR,
			EmployeeData.Property.ELIGIBILITY_DATE,
			EmployeeData.Property.OPT_OUT_IND,
			EmployeeData.Property.PLAN_YTD_HOURS_WORKED,
			EmployeeData.Property.PLAN_YTD_COMPENSATION,
			EmployeeData.Property.PLAN_YTD_HOURS_WORKED_COMPENSATION_EFFECTIVE_DATE,
			EmployeeData.Property.ANNUAL_BASE_SALARY,
			EmployeeData.Property.BEFORE_TAX_DEFERRAL_PERCENTAGE,
			EmployeeData.Property.DESIGNATED_ROTH_DEFERRAL_PERCENTAGE,
			EmployeeData.Property.BEFORE_TAX_FLAT_DOLLAR_DEFERRAL,
			EmployeeData.Property.DESIGNATED_ROTH_DEFERRAL };

	public static CensusErrorCorrectionErrorUtil getInstance() {
		if (instance == null) {
			synchronized (CensusErrorCorrectionErrorUtil.class) {
				instance = new CensusErrorCorrectionErrorUtil();
			}
		}
		return instance;
	}

	@Override
	Property[] getFieldsByErrorDisplayOrder() {
		return errorDisplayOrder;
	}

	@Override
	String getFormFieldName(Property employeeDataFieldName) {
		return "formValue(" + employeeDataFieldName.getName() + ")";
	}

}
