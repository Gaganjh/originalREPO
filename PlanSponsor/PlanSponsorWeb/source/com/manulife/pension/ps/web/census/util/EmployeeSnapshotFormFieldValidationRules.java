package com.manulife.pension.ps.web.census.util;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.ps.web.census.EmployeeSnapshotProperties;
import com.manulife.pension.service.employee.util.EmployeeData.Property;

/**
 * The form field validation rules for the Employee snapshot
 * 
 * @author guweigu
 *
 */
public class EmployeeSnapshotFormFieldValidationRules extends FormFieldValidationRules implements
        EmployeeSnapshotProperties {
    private List<FormFieldValidationRule> rules = null;

    private static EmployeeSnapshotFormFieldValidationRules instance = null;

    public static EmployeeSnapshotFormFieldValidationRules getInstance() {
        if (instance == null) {
            synchronized (EmployeeSnapshotFormFieldValidationRules.class) {
                instance = new EmployeeSnapshotFormFieldValidationRules();
            }
        }
        return instance;
    }

    protected List<FormFieldValidationRule> getRules() {
		if (rules == null) {
			rules = new ArrayList<FormFieldValidationRule>();
			rules.add(new SSNRule(SSN));
           
			rules.add(new ValidStringFieldRule(BirthDate,
					CensusErrorCodes.BirthDateFormatError));
			rules.add(new FieldTypeRule(BirthDate,
					CensusErrorCodes.BirthDateFormatError));

			// rules.add(new FieldTypeRule(MaskSensitiveInformation,
			// NonPrintableChar);

			rules.add(new ValidStringFieldRule(HireDate,
					CensusErrorCodes.HireDateFormatError));
			rules.add(new FieldTypeRule(HireDate,
					CensusErrorCodes.HireDateFormatError));
			rules.add(new ValidStringFieldRule(EmploymentStatusEffectiveDate,
					CensusErrorCodes.EmploymentStatusEffDateFormatError));
			rules.add(new FieldTypeRule(EmploymentStatusEffectiveDate,
					CensusErrorCodes.EmploymentStatusEffDateFormatError));
			rules.add(new FieldTypeRule(AnnualBaseSalary,
					CensusErrorCodes.AnnualBaseSalaryFormatError, Property.ANNUAL_BASE_SALARY.getLabel()));
			rules.add(new FieldTypeRule(PlanYearToDateComp,
					CensusErrorCodes.EligibleYTDCompFormatError, Property.PLAN_YTD_COMPENSATION.getLabel()));
			rules.add(new FieldTypeRule(YearToDatePlanHoursWorked,
					CensusErrorCodes.YTDHoursWorkedFormatError, Property.PLAN_YTD_HOURS_WORKED.getLabel()));
			rules.add(new ValidStringFieldRule(YearToDatePlanHoursEffDate,
					CensusErrorCodes.YTDHoursEffDateFormatError));
			rules.add(new FieldTypeRule(YearToDatePlanHoursEffDate,
					CensusErrorCodes.YTDHoursEffDateFormatError));

			rules.add(new ValidStringFieldRule(EligibilityDate,
					CensusErrorCodes.EligibilityDateFormatError));
			rules.add(new FieldTypeRule(EligibilityDate,
					CensusErrorCodes.EligibilityDateFormatError));
			rules.add(new FieldTypeRule(DesignatedRothDefPer,
					CensusErrorCodes.AfterTaxDefPerFormatError, Property.DESIGNATED_ROTH_DEFERRAL_PERCENTAGE.getLabel()));
			rules.add(new FieldTypeRule(BeforeTaxDefPer,
					CensusErrorCodes.BeforeTaxDefPerFormatError, Property.BEFORE_TAX_DEFERRAL_PERCENTAGE.getLabel()));
			rules.add(new FieldTypeRule(DesignatedRothDefAmt,
					CensusErrorCodes.AfterTaxDefDollarFormatError, Property.DESIGNATED_ROTH_DEFERRAL.getLabel()));
			rules.add(new FieldTypeRule(BeforeTaxFlatDef,
					CensusErrorCodes.BeforeTaxDefDollarFormatError, Property.BEFORE_TAX_FLAT_DOLLAR_DEFERRAL.getLabel()));
			
			rules.add(new FieldTypeRule(PreviousYearsOfService, CensusErrorCodes.InvalidPreviousYearsOfService,
					Property.PREVIOUS_YEARS_OF_SERVICE.getLabel()));

			rules.add(new FieldTypeRule(FullyVestedIndEffectiveDate, CensusErrorCodes.FullyVestedEffectiveDateFormat,
					Property.FULLY_VESTED_EFFECTIVE_DATE.getLabel()));
		}
		return rules;
	}
}
