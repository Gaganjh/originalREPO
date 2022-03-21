package com.manulife.pension.ps.web.census.util;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;

import com.manulife.pension.ps.web.census.EmployeeSnapshotProperties;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.util.EmployeeValidationError;
import com.manulife.pension.service.employee.util.EmployeeValidationErrorCode;
import com.manulife.pension.service.employee.util.EmployeeValidationErrors;
import com.manulife.pension.service.employee.util.EmployeeData.Property;
import com.manulife.pension.service.employee.util.EmployeeValidationError.ErrorType;
import com.manulife.pension.validator.ValidationError;

public class EmployeeSnapshotErrorUtil extends AbstractEmployeeErrorUtil {
    private static EmployeeSnapshotErrorUtil instance = null;

    /**
     * Defines the mapping between the EmployeeData's field name with the EmployeeSnapshotProperty
     * name
     */
    private EnumMap<Property, String> fieldMap = new EnumMap<Property, String>(Property.class);

    private static int[] ErrorOrder = { CensusErrorCodes.InvalidAsciiCharacter,
    		CensusErrorCodes.DIContract,
            CensusErrorCodes.SSNRequired, CensusErrorCodes.FirstNameRequired,
            CensusErrorCodes.LastNameRequired,

            CensusErrorCodes.MissingSSN, CensusErrorCodes.InvalidSSN,
            CensusErrorCodes.DuplicateSSN, CensusErrorCodes.SimilarSSN,
            CensusErrorCodes.InvalidNamePrefix, CensusErrorCodes.InvalidLastname,
            CensusErrorCodes.LastNameShort, CensusErrorCodes.MissingEmployeeID,
            CensusErrorCodes.MissingSSNNewEmployeeEE, CensusErrorCodes.DuplicateEmployeeIdSortOptionEE,
            CensusErrorCodes.DuplicateEmployeeIdSortOptionNotEE,
            CensusErrorCodes.MissingBirthDate, CensusErrorCodes.MinimumAge,
            CensusErrorCodes.MinimumBirthDate,
            
            CensusErrorCodes.InvalidDomainEmailAddress,
            CensusErrorCodes.DuplicateSubmittedEmailAddress,
            CensusErrorCodes.DuplicateEmailAddress,

            CensusErrorCodes.BirthDateHireDate, CensusErrorCodes.BirthDateEmploymentStatusEffDate,
            CensusErrorCodes.EligibilityDateBirthDate, CensusErrorCodes.BirthDateYTDHoursEffDate,
            CensusErrorCodes.RemoveBirthDate,

            CensusErrorCodes.HireDateFormatError, CensusErrorCodes.EmploymentStatusEffDateHireDate,
            CensusErrorCodes.EligibilityDateHireDate, CensusErrorCodes.YTDEffDateHireDate,

            CensusErrorCodes.InvalidEmploymentStatus, CensusErrorCodes.EmploymentStatusEmpty,
            CensusErrorCodes.CancelledParticipant, CensusErrorCodes.RemoveEmploymentStatus,
            CensusErrorCodes.EmploymentStatusEffDateFormatError,
            CensusErrorCodes.EmploymentStatusEffDateEmpty,
            CensusErrorCodes.MissingEmpStatusEffDateAndActiveEmpStatusWithNonEmptyHireDate,
            CensusErrorCodes.MissingEmpStatusEffDateAndActiveEmpStatus,
            CensusErrorCodes.MissingEmpStatusEffDateAndActiveEmpStatusWithEmptyHireDate,

            CensusErrorCodes.MissingYTDHoursEffDateBoth,
            CensusErrorCodes.MissingYTDHoursEffDateHours,
            CensusErrorCodes.MissingYTDHoursEffDateCompensation,
            CensusErrorCodes.YTDEffDateDiscrepancy, CensusErrorCodes.YTDHoursEffDateFormatError,
            CensusErrorCodes.MaxYTDHoursEffDate, CensusErrorCodes.PriorYTDHoursEffDate,
            CensusErrorCodes.InvalidYTDHoursWorked, CensusErrorCodes.MaxYTDHourWorked,

            CensusErrorCodes.RemoveAddressLine1, CensusErrorCodes.MissingCountry,
            CensusErrorCodes.MissingCity, CensusErrorCodes.MissingAddressLine1,
            CensusErrorCodes.RemoveCity, CensusErrorCodes.MissingState,
            CensusErrorCodes.InvalidStateCode, CensusErrorCodes.InvalidCountry,
            CensusErrorCodes.RemoveCountry, CensusErrorCodes.StateZipCode,
            CensusErrorCodes.InvalidZipFormat, CensusErrorCodes.InvalidStateOfResidence,
            CensusErrorCodes.InvalidEmailAddressFormat,

            CensusErrorCodes.InvalidEligibilityInd, CensusErrorCodes.BlankEligibilityInd,
            CensusErrorCodes.MissingEligibilityDateForNonPpt,
            CensusErrorCodes.MissingEligibilityDateForPpt,
            CensusErrorCodes.InvalidPreviousYearsOfService,
            CensusErrorCodes.MissingPreviousYearsOfService,
            CensusErrorCodes.MissingPreviousYearsOfServiceEffectiveDate,
            CensusErrorCodes.MissingFullyVestedInd,
            CensusErrorCodes.MissingFullyVestedEffectiveDate,
            CensusErrorCodes.MinimumFullyVestedEffectiveDate,
            CensusErrorCodes.FullyVestedEffectiveDateBirthDate,
            CensusErrorCodes.FullyVestedEffectiveDateFormat,
            CensusErrorCodes.FutureFullyVestedEffectiveDate,
            CensusErrorCodes.EmptyPlanYTDHoursWorked,
    };

    public static EmployeeSnapshotErrorUtil getInstance() {
        if (instance == null) {
            synchronized (EmployeeSnapshotErrorUtil.class) {
                instance = new EmployeeSnapshotErrorUtil();
            }
        }
        return instance;
    }

    private EmployeeSnapshotErrorUtil() {
        fieldMap.put(EmployeeData.Property.DATE_OF_BIRTH, EmployeeSnapshotProperties.BirthDate);
        fieldMap.put(EmployeeData.Property.ADDRESS_LINE_1, EmployeeSnapshotProperties.AddressLine1);
        fieldMap.put(EmployeeData.Property.ADDRESS_LINE_2, EmployeeSnapshotProperties.AddressLine2);
        fieldMap.put(EmployeeData.Property.CITY_NAME, EmployeeSnapshotProperties.City);
        fieldMap.put(EmployeeData.Property.SOCIAL_SECURITY_NUMBER, EmployeeSnapshotProperties.SSN);
        fieldMap.put(EmployeeData.Property.STATE_CODE, EmployeeSnapshotProperties.State);
        fieldMap.put(EmployeeData.Property.ZIP_CODE, EmployeeSnapshotProperties.ZipCode);
        fieldMap.put(EmployeeData.Property.COUNTRY_NAME, EmployeeSnapshotProperties.Country);
        fieldMap.put(EmployeeData.Property.STATE_OF_RESIDENCE,
                EmployeeSnapshotProperties.StateOfResidence);
        fieldMap.put(EmployeeData.Property.DESIGNATED_ROTH_DEFERRAL_PERCENTAGE,
                EmployeeSnapshotProperties.DesignatedRothDefPer);
        fieldMap.put(EmployeeData.Property.DESIGNATED_ROTH_DEFERRAL,
                EmployeeSnapshotProperties.DesignatedRothDefAmt);
        fieldMap.put(EmployeeData.Property.ANNUAL_BASE_SALARY,
                EmployeeSnapshotProperties.AnnualBaseSalary);
        fieldMap.put(EmployeeData.Property.BEFORE_TAX_DEFERRAL_PERCENTAGE,
                EmployeeSnapshotProperties.BeforeTaxDefPer);
        fieldMap.put(EmployeeData.Property.BEFORE_TAX_FLAT_DOLLAR_DEFERRAL,
                EmployeeSnapshotProperties.BeforeTaxFlatDef);
        fieldMap.put(EmployeeData.Property.DIVISION, EmployeeSnapshotProperties.Division);
        fieldMap.put(EmployeeData.Property.ELIGIBILITY_DATE,
                EmployeeSnapshotProperties.EligibilityDate);
        fieldMap.put(EmployeeData.Property.ELIGIBILITY_INDICATOR,
                EmployeeSnapshotProperties.EligibilityInd);
        fieldMap.put(EmployeeData.Property.EMPLOYEE_NUMBER, EmployeeSnapshotProperties.EmployeeId);
        fieldMap.put(EmployeeData.Property.EMPLOYER_PROVIDED_EMAIL,
                EmployeeSnapshotProperties.EmailAddress);
        fieldMap.put(EmployeeData.Property.EMPLOYMENT_STATUS,
                EmployeeSnapshotProperties.EmploymentStatus);
        fieldMap.put(EmployeeData.Property.EMPLOYMENT_STATUS_DATE,
                EmployeeSnapshotProperties.EmploymentStatusEffectiveDate);
        fieldMap.put(EmployeeData.Property.FIRST_NAME, EmployeeSnapshotProperties.FirstName);
        fieldMap.put(EmployeeData.Property.MIDDLE_INIT, EmployeeSnapshotProperties.MiddleInitial);
        fieldMap.put(EmployeeData.Property.HIRE_DATE, EmployeeSnapshotProperties.HireDate);
        fieldMap.put(EmployeeData.Property.LAST_NAME, EmployeeSnapshotProperties.LastName);
        fieldMap.put(EmployeeData.Property.NAME_PREFIX, EmployeeSnapshotProperties.Prefix);
        fieldMap.put(EmployeeData.Property.OPT_OUT_IND, EmployeeSnapshotProperties.OptOut);
        fieldMap.put(EmployeeData.Property.PLAN_YTD_COMPENSATION,
                EmployeeSnapshotProperties.PlanYearToDateComp);
        fieldMap.put(EmployeeData.Property.PLAN_YTD_HOURS_WORKED,
                EmployeeSnapshotProperties.YearToDatePlanHoursWorked);
        fieldMap.put(EmployeeData.Property.PLAN_YTD_HOURS_WORKED_COMPENSATION_EFFECTIVE_DATE,
                EmployeeSnapshotProperties.YearToDatePlanHoursEffDate);
        fieldMap.put(Property.PREVIOUS_YEARS_OF_SERVICE,
				EmployeeSnapshotProperties.PreviousYearsOfService);
		fieldMap.put(Property.PREVIOUS_YEARS_OF_SERVICE_EFFECTIVE_DATE,
				EmployeeSnapshotProperties.PreviousYearsOfServiceEffectiveDate);
		fieldMap.put(Property.FULLY_VESTED_IND,
				EmployeeSnapshotProperties.FullyVestedInd);
		fieldMap.put(Property.FULLY_VESTED_EFFECTIVE_DATE,
				EmployeeSnapshotProperties.FullyVestedIndEffectiveDate);
		fieldMap.put(EmployeeData.Property.AE_90DAYS_OPT_OUT_IND, EmployeeSnapshotProperties.Ae90DaysOptOut);
    }

    protected final EmployeeData.Property[] snapshotErrorDisplayOrder = {
            EmployeeData.Property.SOCIAL_SECURITY_NUMBER, EmployeeData.Property.NAME_PREFIX,
            EmployeeData.Property.FIRST_NAME, EmployeeData.Property.MIDDLE_INIT,
            EmployeeData.Property.LAST_NAME, EmployeeData.Property.EMPLOYEE_NUMBER,
            EmployeeData.Property.DATE_OF_BIRTH, EmployeeData.Property.HIRE_DATE,
            EmployeeData.Property.DIVISION, EmployeeData.Property.EMPLOYMENT_STATUS,
            EmployeeData.Property.EMPLOYMENT_STATUS_DATE, EmployeeData.Property.ANNUAL_BASE_SALARY,
            EmployeeData.Property.PLAN_YTD_COMPENSATION,
            EmployeeData.Property.PLAN_YTD_HOURS_WORKED,
            EmployeeData.Property.PLAN_YTD_HOURS_WORKED_COMPENSATION_EFFECTIVE_DATE,
            EmployeeData.Property.ADDRESS_LINE_1, EmployeeData.Property.ADDRESS_LINE_2,
            EmployeeData.Property.CITY_NAME, EmployeeData.Property.STATE_CODE,
            EmployeeData.Property.ZIP_CODE, EmployeeData.Property.COUNTRY_NAME,
            EmployeeData.Property.STATE_OF_RESIDENCE,
            EmployeeData.Property.EMPLOYER_PROVIDED_EMAIL,
            EmployeeData.Property.ELIGIBILITY_INDICATOR, 
            EmployeeData.Property.ELIGIBILITY_DATE,
            EmployeeData.Property.OPT_OUT_IND,
            EmployeeData.Property.AE_90DAYS_OPT_OUT_IND,
            EmployeeData.Property.BEFORE_TAX_DEFERRAL_PERCENTAGE,
            EmployeeData.Property.BEFORE_TAX_FLAT_DOLLAR_DEFERRAL,
            EmployeeData.Property.DESIGNATED_ROTH_DEFERRAL_PERCENTAGE,            
            EmployeeData.Property.DESIGNATED_ROTH_DEFERRAL,
            EmployeeData.Property.PREVIOUS_YEARS_OF_SERVICE,
            EmployeeData.Property.PREVIOUS_YEARS_OF_SERVICE_EFFECTIVE_DATE,
            EmployeeData.Property.FULLY_VESTED_IND,
            EmployeeData.Property.FULLY_VESTED_EFFECTIVE_DATE,
             };

    String getFormFieldName(EmployeeData.Property employeeDataFieldName) {
        return fieldMap.get(employeeDataFieldName);
    }

    public EmployeeData.Property[] getFieldsByErrorDisplayOrder() {
        return snapshotErrorDisplayOrder;
    }

    /**
     * Filter out all regular warning but leaves all service feature warning and errors in.
     * 
     * @param errors
     * @param vErrors
     * @param fieldChanged
     * @return
     */
    public List<ValidationError> convertSkipRegularWarnings(EmployeeValidationErrors errors,
            List<ValidationError> vErrors) {
        return convert(errors, vErrors, new ValidationErrorFilter() {
            public boolean shouldInclude(Property field, EmployeeValidationError error) {
                // check only for warning for change
                if (ErrorType.warning.compareTo(error.getType()) == 0) {
                    EmployeeValidationErrorCode errorCode = error.getErrorCode();
                    return errorCode.isServiceFeatureWarning();
                } else {
                    return true;
                }
            }
        });

    }

    /**
     * Filter out the WarningOnChange when the correspond field is not changed The change compare
     * with the value loaded from database
     * 
     * @param fieldChanged
     * @param errors
     */
    public List<ValidationError> convertSkipUnchangedFields(EmployeeValidationErrors errors,
            List<ValidationError> vErrors, final Set<String> fieldChanged) {

        return convert(errors, vErrors, new ValidationErrorFilter() {
            public boolean shouldInclude(Property field, EmployeeValidationError error) {
                // check only for warning for change
                if (ErrorType.warning.compareTo(error.getType()) == 0) {
                    EmployeeValidationErrorCode errorCode = error.getErrorCode();

                    // - always show service feature warning
                    // - StateZip code is special since any address field change
                    // will trigger it as well. The back end validation 
                    // checks if there is a change
                    // - always show warning (E9) when status is Active and empl 
                    // status date is empty and hireDate is not empty
                    if (errorCode.isServiceFeatureWarning()
                        || errorCode.compareTo(EmployeeValidationErrorCode.StateZipCode) == 0
                        || errorCode.compareTo(EmployeeValidationErrorCode.
                                MissingEmpStatusEffDateAndActiveEmpStatusWithNonEmptyHireDate)== 0) {
                        return true;
                    } else {
                        String formFieldName = getFormFieldName(field);
                        // only show regular warning when the related field has been changed
                        return fieldChanged.contains(formFieldName);
                    }
                }
                return true;
            }
        });
    }

    /**
     * Sort the validation errors based on the defined ErrorOrder...
     * 
     * @param errors
     * @return
     */
    public List<ValidationError> sort(List<ValidationError> errors) {
        if (errors == null || errors.size() == 0) {
            return errors;
        }
        List<ValidationError> sortedList = new ArrayList<ValidationError>(errors.size());
        // add all record level error fist
        for (ValidationError error : errors) {
            if (error.getFieldIds().get(0).equals("")) {
                sortedList.add(error);
            }
        }
        for (Property field : getFieldsByErrorDisplayOrder()) {
            String fieldName = getFormFieldName(field);
            for (ValidationError error : errors) {
                if (error.getFieldIds().get(0).equals(fieldName)) {
                    sortedList.add(error);
                }
            }
        }
        return sortedList;
    }

    /**
     * Returns the ValidationError by the error code in the ValidationError list
     * 
     * @param code
     * @param errors
     * @return
     */
    protected ValidationError getValidationErrorByCode(int code, List<ValidationError> errors) {
        for (ValidationError error : errors) {
            if (error.getErrorCode() == code) {
                return error;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        for (EmployeeValidationErrorCode code : EmployeeSnapshotErrorUtil.ErrorMapping.keySet()) {
            if (EmployeeSnapshotErrorUtil.ErrorMapping.get(code) == CensusErrorCodes.UnexpectedError) {
                System.out.println(code);
            }
        }
    }
}
