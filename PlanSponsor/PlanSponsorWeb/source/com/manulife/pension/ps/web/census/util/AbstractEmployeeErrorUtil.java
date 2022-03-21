package com.manulife.pension.ps.web.census.util;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.util.EmployeeValidationError;
import com.manulife.pension.service.employee.util.EmployeeValidationErrorCode;
import com.manulife.pension.service.employee.util.EmployeeValidationErrors;
import com.manulife.pension.service.employee.util.EmployeeData.Property;
import com.manulife.pension.service.employee.util.EmployeeValidationError.ErrorType;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;

/**
 * A utility class to convert validation errors from EmployeeDataValidator into a jsp frinedly
 * ValidationError list.
 *
 * @author guweigu
 *
 */
public abstract class AbstractEmployeeErrorUtil {
    private static final Logger log = Logger.getLogger(AbstractEmployeeErrorUtil.class);

    protected static final EnumMap<EmployeeValidationErrorCode, Integer> ErrorMapping = new EnumMap<EmployeeValidationErrorCode, Integer>(
            EmployeeValidationErrorCode.class);

    /**
     * Defines the map between the EmployeeValidationError type and Validation error type
     */
    public static EnumMap<ErrorType, Type> ErrorTypeMap = new EnumMap<ErrorType, Type>(
            ErrorType.class);

    static {
        ErrorTypeMap.put(ErrorType.error, Type.error);
        ErrorTypeMap.put(ErrorType.warning, Type.warning);

        ErrorMapping.put(EmployeeValidationErrorCode.SSNRequired, CensusErrorCodes.SSNRequired);
        ErrorMapping.put(EmployeeValidationErrorCode.InvalidSSN, CensusErrorCodes.InvalidSSN);
        ErrorMapping.put(EmployeeValidationErrorCode.DuplicateSSN, CensusErrorCodes.DuplicateSSN);
        ErrorMapping.put(EmployeeValidationErrorCode.SimilarSSN, CensusErrorCodes.SimilarSSN);
        ErrorMapping.put(EmployeeValidationErrorCode.InvalidNamePrefix,
                CensusErrorCodes.InvalidNamePrefix);
        ErrorMapping.put(EmployeeValidationErrorCode.MissingSSN, CensusErrorCodes.MissingSSN);

        ErrorMapping.put(EmployeeValidationErrorCode.DuplicateSubmittedEmailAddress, CensusErrorCodes.DuplicateSubmittedEmailAddress);
        ErrorMapping.put(EmployeeValidationErrorCode.DuplicateEmailAddress, CensusErrorCodes.DuplicateEmailAddress);
        ErrorMapping.put(EmployeeValidationErrorCode.InvalidEmailAddressDomain, CensusErrorCodes.InvalidDomainEmailAddress);
        
        ErrorMapping.put(EmployeeValidationErrorCode.DIContract, CensusErrorCodes.DIContract);

        ErrorMapping.put(EmployeeValidationErrorCode.LastNameTooShort,
                CensusErrorCodes.LastNameShort);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingEmployeeId,
                CensusErrorCodes.MissingEmployeeID);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingSSNNewEmployeeEE,
                CensusErrorCodes.MissingSSNNewEmployeeEE);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidEmployeeIdSortOptionEE,
                CensusErrorCodes.InvalidEmployeeIDSortOptionEE);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidEmployeeIdSortOptionNotEE,
                CensusErrorCodes.InvalidEmployeeIDSortOptionNotEE);

        ErrorMapping.put(EmployeeValidationErrorCode.DuplicateEmployeeIdSortOptionEE,
                CensusErrorCodes.DuplicateEmployeeIdSortOptionEE); // 19

        ErrorMapping.put(EmployeeValidationErrorCode.DuplicateEmployeeIdSortOptionNotEE,
                CensusErrorCodes.DuplicateEmployeeIdSortOptionNotEE); // 196
        
        ErrorMapping.put(EmployeeValidationErrorCode.DuplicateEmployeeIdAccountHolderSortOptionEE,
                CensusErrorCodes.DuplicateEmployeeIdAccountHolderSortOptionEE); // 208
        
        ErrorMapping.put(EmployeeValidationErrorCode.DuplicateEmployeeIdAccountHolderSortOptionNotEE,
                CensusErrorCodes.DuplicateEmployeeIdAccountHolderSortOptionNotEE); // 209
        
        ErrorMapping.put(EmployeeValidationErrorCode.DuplicateEmployeeIdNonAccountHolderSortOptionEE,
                CensusErrorCodes.DuplicateEmployeeIdNonAccountHolderSortOptionEE); // 206
        
        ErrorMapping.put(EmployeeValidationErrorCode.DuplicateEmployeeIdNonAccountHolderSortOptionNotEE,
                CensusErrorCodes.DuplicateEmployeeIdNonAccountHolderSortOptionNotEE); // 207
        
        ErrorMapping.put(EmployeeValidationErrorCode.MultipleDuplicateEmployeeIdSortOptionEE,
                CensusErrorCodes.MultipleDuplicateEmployeeIdSortOptionEE); // 210
        
        ErrorMapping.put(EmployeeValidationErrorCode.MultipleDuplicateEmployeeIdSortOptionNotEE,
                CensusErrorCodes.MultipleDuplicateEmployeeIdSortOptionNotEE); // 211

        ErrorMapping.put(EmployeeValidationErrorCode.DuplicateSubmittedSSN,
                CensusErrorCodes.DuplicateSubmittedSSN);

        ErrorMapping.put(EmployeeValidationErrorCode.DuplicateSubmittedEmployeeId,
                CensusErrorCodes.DuplicateSubmittedEmployeeId);

        ErrorMapping.put(EmployeeValidationErrorCode.LengthExceedsCSDBLengthError,
                CensusErrorCodes.LengthExceedsCSDBLengthError);

        ErrorMapping.put(EmployeeValidationErrorCode.MinimumAge,
                CensusErrorCodes.MinimumAge);
        ErrorMapping.put(EmployeeValidationErrorCode.MinimumBirthDate,
                CensusErrorCodes.MinimumBirthDate);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingBirthDate,
                CensusErrorCodes.MissingBirthDate);
        ErrorMapping.put(EmployeeValidationErrorCode.FutureHireDate,
                CensusErrorCodes.FutureHireDate);

        ErrorMapping.put(EmployeeValidationErrorCode.BirthDateHireDate,
                CensusErrorCodes.BirthDateHireDate);

        ErrorMapping.put(EmployeeValidationErrorCode.MinimumEligibilityDate,
                CensusErrorCodes.MinimumEligibilityDate);

        ErrorMapping.put(EmployeeValidationErrorCode.EligibilityDateBirthDate,
                CensusErrorCodes.EligibilityDateBirthDate);

        ErrorMapping.put(EmployeeValidationErrorCode.EmploymentStatusEffDateBirthDate,
                CensusErrorCodes.BirthDateEmploymentStatusEffDate);

        ErrorMapping.put(EmployeeValidationErrorCode.RemoveBirthDate,
                CensusErrorCodes.RemoveBirthDate);

        ErrorMapping.put(EmployeeValidationErrorCode.EmploymentStatusEffDateHireDate,
                CensusErrorCodes.EmploymentStatusEffDateHireDate);

        ErrorMapping.put(EmployeeValidationErrorCode.EligibilityDateHireDate,
                CensusErrorCodes.EligibilityDateHireDate);

        ErrorMapping.put(EmployeeValidationErrorCode.HireDateYTDHoursEffDate,
                CensusErrorCodes.YTDEffDateHireDate);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidEmploymentStatus,
                CensusErrorCodes.InvalidEmploymentStatus);

        ErrorMapping.put(EmployeeValidationErrorCode.EmploymentStatusEmpty,
                CensusErrorCodes.EmploymentStatusEmpty);

        ErrorMapping.put(EmployeeValidationErrorCode.EmploymentStatusEffDateEmpty,
                CensusErrorCodes.EmploymentStatusEffDateEmpty);

        ErrorMapping.put(
            EmployeeValidationErrorCode.MissingEmpStatusEffDateAndActiveEmpStatusWithNonEmptyHireDate,
            CensusErrorCodes.MissingEmpStatusEffDateAndActiveEmpStatusWithNonEmptyHireDate);
        ErrorMapping.put(
            EmployeeValidationErrorCode.MissingEmpStatusEffDateAndActiveEmpStatus,
            CensusErrorCodes.MissingEmpStatusEffDateAndActiveEmpStatus);
        ErrorMapping.put(
            EmployeeValidationErrorCode.MissingEmpStatusEffDateAndActiveEmpStatusWithEmptyHireDate,
            CensusErrorCodes.MissingEmpStatusEffDateAndActiveEmpStatusWithEmptyHireDate);
        
        ErrorMapping.put(EmployeeValidationErrorCode.MinimumEmploymentStatusEffDate,
                CensusErrorCodes.MinimumEmploymentStatusEffDate);

        ErrorMapping.put(EmployeeValidationErrorCode.CancelledParticipant,
                CensusErrorCodes.CancelledParticipant);

        ErrorMapping.put(EmployeeValidationErrorCode.CancellingParticipant,
                CensusErrorCodes.CancellingParticipant);

        ErrorMapping.put(EmployeeValidationErrorCode.RemoveEmploymentStatus,
                CensusErrorCodes.RemoveEmploymentStatus);

        ErrorMapping.put(EmployeeValidationErrorCode.RemoveAddressLine1,
                CensusErrorCodes.RemoveAddressLine1);

        ErrorMapping.put(EmployeeValidationErrorCode.RemoveCity, CensusErrorCodes.RemoveCity);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingAddressLine1,
                CensusErrorCodes.MissingAddressLine1);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingCity, CensusErrorCodes.MissingCity);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingCountry,
                CensusErrorCodes.MissingCountry);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingState, CensusErrorCodes.MissingState);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidStateCode,
                CensusErrorCodes.InvalidStateCode);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidCountry,
                CensusErrorCodes.InvalidCountry);

        ErrorMapping.put(EmployeeValidationErrorCode.RemoveCountry, CensusErrorCodes.RemoveCountry);

        ErrorMapping.put(EmployeeValidationErrorCode.StateZipCode, CensusErrorCodes.StateZipCode);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidZipFormat,
                CensusErrorCodes.InvalidZipFormat);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidUSZipFormat,
                CensusErrorCodes.InvalidUSZipFormat);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidStateOfResidence,
                CensusErrorCodes.InvalidStateOfResidence);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidEmailAddressFormat,
                CensusErrorCodes.InvalidEmailAddressFormat);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidEligibilityInd,
                CensusErrorCodes.InvalidEligibilityInd);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingYTDHoursEffDateBoth,
                CensusErrorCodes.MissingYTDHoursEffDateBoth);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingYTDHoursEffDateHours,
                CensusErrorCodes.MissingYTDHoursEffDateHours);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingYTDHoursEffDateCompensation,
                CensusErrorCodes.MissingYTDHoursEffDateCompensation);

        ErrorMapping.put(EmployeeValidationErrorCode.YTDEffDateDiscrepancy,
                CensusErrorCodes.YTDEffDateDiscrepancy);

        ErrorMapping.put(EmployeeValidationErrorCode.MaxYTDHoursEffDate,
                CensusErrorCodes.MaxYTDHoursEffDate);

        ErrorMapping.put(EmployeeValidationErrorCode.PriorYTDHoursEffDate,
                CensusErrorCodes.PriorYTDHoursEffDate);

        ErrorMapping.put(EmployeeValidationErrorCode.BirthDateYTDHoursEffDate,
                CensusErrorCodes.BirthDateYTDHoursEffDate);

        ErrorMapping.put(EmployeeValidationErrorCode.MinYTDHoursEffDate,
                CensusErrorCodes.MinYTDHoursEffDate);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidYTDHoursWorked,
                CensusErrorCodes.InvalidYTDHoursWorked);

        ErrorMapping.put(EmployeeValidationErrorCode.MaxYTDHourWorked,
                CensusErrorCodes.MaxYTDHourWorked);

        ErrorMapping.put(EmployeeValidationErrorCode.BlankEligibilityInd,
                CensusErrorCodes.BlankEligibilityInd);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingEligibilityDateForNonPpt,
                CensusErrorCodes.MissingEligibilityDateForNonPpt);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingEligibilityDateForPpt,
                CensusErrorCodes.MissingEligibilityDateForPpt);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingEligibilityInd,
                CensusErrorCodes.MissingEligibilityInd);

        ErrorMapping.put(EmployeeValidationErrorCode.BeforePlanEffectiveDate,
                CensusErrorCodes.BeforePlanEffectiveDate);

        ErrorMapping.put(EmployeeValidationErrorCode.FutureEligibilityDate,
                CensusErrorCodes.FutureEligibilityDate);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidOptOutInd,
                CensusErrorCodes.InvalidOptOutInd);

        ErrorMapping.put(EmployeeValidationErrorCode.OptOutNonParticipant,
                CensusErrorCodes.OptOutNonParticipant);

        ErrorMapping.put(EmployeeValidationErrorCode.DesignatedRothDefPerNotInRange,
                CensusErrorCodes.DesignatedRothDefPerNotInRange);

        ErrorMapping.put(EmployeeValidationErrorCode.DesignatedRothDefAmtNotInRange,
                CensusErrorCodes.DesignatedRothDefAmtNotInRange);

        ErrorMapping.put(EmployeeValidationErrorCode.BeforeTaxDefPerNotInRange,
                CensusErrorCodes.BeforeTaxDefPerNotInRange);

        ErrorMapping.put(EmployeeValidationErrorCode.BeforeTaxDefAmtNotInRange,
                CensusErrorCodes.BeforeTaxDefAmtNotInRange);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidAsciiCharacter,
                CensusErrorCodes.InvalidAsciiCharacter);

        ErrorMapping.put(EmployeeValidationErrorCode.BirthDateFormatError,
                CensusErrorCodes.BirthDateFormatError);

        ErrorMapping.put(EmployeeValidationErrorCode.HireDateFormatError,
                CensusErrorCodes.HireDateFormatError);

        ErrorMapping.put(EmployeeValidationErrorCode.EmploymentStatusEffDateFormatError,
                CensusErrorCodes.EmploymentStatusEffDateFormatError);

        ErrorMapping.put(EmployeeValidationErrorCode.AnnualBaseSalaryFormatError,
                CensusErrorCodes.AnnualBaseSalaryFormatError);

        ErrorMapping.put(EmployeeValidationErrorCode.EligibleYTDCompFormatError,
                CensusErrorCodes.EligibleYTDCompFormatError);

        ErrorMapping.put(EmployeeValidationErrorCode.YTDHoursWorkedFormatError,
                CensusErrorCodes.YTDHoursWorkedFormatError);

        ErrorMapping.put(EmployeeValidationErrorCode.YTDHoursEffDateFormatError,
                CensusErrorCodes.YTDHoursEffDateFormatError);

        ErrorMapping.put(EmployeeValidationErrorCode.EligibilityDateFormatError,
                CensusErrorCodes.EligibilityDateFormatError);

        ErrorMapping.put(EmployeeValidationErrorCode.DesignatedRothDefPerFormatError,
                CensusErrorCodes.AfterTaxDefPerFormatError);

        ErrorMapping.put(EmployeeValidationErrorCode.BeforeTaxDefPerFormatError,
                CensusErrorCodes.BeforeTaxDefPerFormatError);

        ErrorMapping.put(EmployeeValidationErrorCode.DesignatedRothDefAmtFormatError,
                CensusErrorCodes.AfterTaxDefDollarFormatError);

        ErrorMapping.put(EmployeeValidationErrorCode.BeforeTaxDefDollarFormatError,
                CensusErrorCodes.BeforeTaxDefDollarFormatError);

        ErrorMapping.put(EmployeeValidationErrorCode.OptOutDesignatedRothDefAmt,
                CensusErrorCodes.OptOutDesignatedRothDefAmt);

        ErrorMapping.put(EmployeeValidationErrorCode.OptOutDesignatedRothDefPer,
                CensusErrorCodes.OptOutDesignatedRothDefPer);

        ErrorMapping.put(EmployeeValidationErrorCode.OptOutBeforeTaxDef,
                CensusErrorCodes.OptOutBeforeTaxDef);

        ErrorMapping.put(EmployeeValidationErrorCode.OptOutBeforeTaxDefPer,
                CensusErrorCodes.OptOutBeforeTaxDefPer);

        ErrorMapping.put(EmployeeValidationErrorCode.EligibilityDesignatedRothDefAmt,
                CensusErrorCodes.EligibilityDesignatedRothDefAmt);

        ErrorMapping.put(EmployeeValidationErrorCode.EligibilityDesignatedRothDefPer,
                CensusErrorCodes.EligibilityDesignatedRothDefPer);

        ErrorMapping.put(EmployeeValidationErrorCode.EligibilityBeforeTaxDef,
                CensusErrorCodes.EligibilityBeforeTaxDef);

        ErrorMapping.put(EmployeeValidationErrorCode.EligibilityBeforeTaxDefPer,
                CensusErrorCodes.EligibilityBeforeTaxDefPer);

        ErrorMapping.put(EmployeeValidationErrorCode.BeforeTaxDefPerFlatDollar,
                CensusErrorCodes.BeforeTaxDefPerFlatDollar);

        ErrorMapping.put(EmployeeValidationErrorCode.DesignatedRothDefAmt,
                CensusErrorCodes.DesignatedRothDefAmt);

        ErrorMapping.put(EmployeeValidationErrorCode.FirstNameTooShort,
                CensusErrorCodes.FirstNameTooShort);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingFirstName,
                CensusErrorCodes.MissingFirstName);

        ErrorMapping.put(EmployeeValidationErrorCode.MissingLastName,
                CensusErrorCodes.MissingLastName);

        ErrorMapping.put(EmployeeValidationErrorCode.FutureBirthDate,
                CensusErrorCodes.FutureBirthDate);

        ErrorMapping.put(EmployeeValidationErrorCode.MinimumHireDate,
                CensusErrorCodes.MinimumHireDate);

        ErrorMapping.put(EmployeeValidationErrorCode.CancelledEmployee,
                CensusErrorCodes.CancelledEmployee);

        ErrorMapping.put(EmployeeValidationErrorCode.CityTooShort,
                CensusErrorCodes.CityTooShort);

        ErrorMapping.put(EmployeeValidationErrorCode.AdressLine2TooShort,
                CensusErrorCodes.AdressLine2TooShort);

        ErrorMapping.put(EmployeeValidationErrorCode.AdressLine1TooShort,
                CensusErrorCodes.AdressLine1TooShort);

        ErrorMapping.put(EmployeeValidationErrorCode.FutureEmploymentStatusEffDate,
                CensusErrorCodes.FutureEmploymentStatusEffDate);

        ErrorMapping.put(EmployeeValidationErrorCode.FirstNameRequired,
                CensusErrorCodes.FirstNameRequired);

        ErrorMapping.put(EmployeeValidationErrorCode.LastNameRequired,
                CensusErrorCodes.LastNameRequired);
        
        ErrorMapping.put(EmployeeValidationErrorCode.FirstNameHasNumbers,
                CensusErrorCodes.FirstNameHasNumbers);

        ErrorMapping.put(EmployeeValidationErrorCode.LastNameHasNumbers,
                CensusErrorCodes.LastNameHasNumbers);
        
        ErrorMapping.put(EmployeeValidationErrorCode.FirstNameIsNumeric,
                CensusErrorCodes.FirstNameHasNumbers);

        ErrorMapping.put(EmployeeValidationErrorCode.LastNameIsNumeric,
                CensusErrorCodes.LastNameHasNumbers);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidEligiblePlanYTDCompensation,
                CensusErrorCodes.InvalidEligiblePlanYTDCompensation);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidAnnualBaseSalary,
                CensusErrorCodes.InvalidAnnualBaseSalary);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidPreviousYearsOfService,
                CensusErrorCodes.InvalidPreviousYearsOfService);

        ErrorMapping.put(EmployeeValidationErrorCode.EmployeeIdSSNMismatch,
                CensusErrorCodes.EmployeeIdSSNMismatch);

        ErrorMapping.put(EmployeeValidationErrorCode.MoreThanTwoDecimalDigits,
                CensusErrorCodes.MoreThanTwoDecimalDigits);

        ErrorMapping.put(EmployeeValidationErrorCode.MoreThanThreeDecimalDigits,
                CensusErrorCodes.MoreThanThreeDecimalDigits);

        ErrorMapping.put(EmployeeValidationErrorCode.InvalidDate,
                CensusErrorCodes.InvalidDate);

        ErrorMapping
				.put(
						EmployeeValidationErrorCode.MissingPreviousYearsOfService,
						CensusErrorCodes.MissingPreviousYearsOfService);
        ErrorMapping
		.put(
				EmployeeValidationErrorCode.MissingPreviousYearsOfServiceEffectiveDate,
				CensusErrorCodes.MissingPreviousYearsOfServiceEffectiveDate);

        ErrorMapping
		.put(
				EmployeeValidationErrorCode.FuturePreviousYearsOfServiceEffectiveDate,
				CensusErrorCodes.FuturePreviousYearsOfServiceEffectiveDate);

        ErrorMapping
		.put(
				EmployeeValidationErrorCode.MinPreviousYearsOfServiceEffectiveDate,
				CensusErrorCodes.MinPreviousYearsOfServiceEffectiveDate);

        ErrorMapping
        .put(
                EmployeeValidationErrorCode.PreviousYearsOfServiceEffectiveDateBirthDate,
                CensusErrorCodes.PreviousYearsOfServiceEffectiveDateBirthDate);

        ErrorMapping
		.put(
				EmployeeValidationErrorCode.MissingFullyVestedInd,
				CensusErrorCodes.MissingFullyVestedInd);

        ErrorMapping
		.put(
				EmployeeValidationErrorCode.MissingFullyVestedEffectiveDate,
				CensusErrorCodes.MissingFullyVestedEffectiveDate);

        ErrorMapping
        .put(
                EmployeeValidationErrorCode.MinimumFullyVestedEffectiveDate,
                CensusErrorCodes.MinimumFullyVestedEffectiveDate);

        ErrorMapping
        .put(
                EmployeeValidationErrorCode.FullyVestedEffectiveDateBirthDate,
                CensusErrorCodes.FullyVestedEffectiveDateBirthDate);

        ErrorMapping
		.put(
				EmployeeValidationErrorCode.FutureFullyVestedEffectiveDate,
				CensusErrorCodes.FutureFullyVestedEffectiveDate);

        ErrorMapping
		.put(
				EmployeeValidationErrorCode.MissingHireDate,
				CensusErrorCodes.MissingHireDate);

        ErrorMapping
		.put(
				EmployeeValidationErrorCode.MissingEmploymentStatus,
				CensusErrorCodes.MissingEmploymentStatus);

        ErrorMapping
		.put(
				EmployeeValidationErrorCode.MissingPYOSAndHOS,
				CensusErrorCodes.MissingPYOSAndHOS);

        ErrorMapping
		.put(
				EmployeeValidationErrorCode.InvalidPreviousYearsOfServiceEffectiveDate,
				CensusErrorCodes.InvalidPreviousYearsOfServiceEffectiveDate);

        ErrorMapping.put(EmployeeValidationErrorCode.RemovePlanYTDHoursWorked,
				CensusErrorCodes.RemovePlanYTDHoursWorked);

        ErrorMapping.put(EmployeeValidationErrorCode.Ae90DaysOptOutDesignatedRothDefAmt,
                CensusErrorCodes.Ae90DaysOptOutDesignatedRothDefAmt);

        ErrorMapping.put(EmployeeValidationErrorCode.Ae90DaysOptOutDesignatedRothDefPer,
                CensusErrorCodes.Ae90DaysOptOutDesignatedRothDefPer);

        ErrorMapping.put(EmployeeValidationErrorCode.Ae90DaysOptOutBeforeTaxDef,
                CensusErrorCodes.Ae90DaysOptOutBeforeTaxDef);

        ErrorMapping.put(EmployeeValidationErrorCode.Ae90DaysOptOutBeforeTaxDefPer,
                CensusErrorCodes.Ae90DaysOptOutBeforeTaxDefPer);

        ErrorMapping.put(EmployeeValidationErrorCode.Ae90DaysOptOutHasEEDEFMoney,
                CensusErrorCodes.Ae90DaysOptOutEEDEFMoney);

        ErrorMapping.put(EmployeeValidationErrorCode.Ae90DaysStopNEWithdrawalWarning,
                CensusErrorCodes.Ae90DaysStopNEWithdrawalWarning);

        ErrorMapping.put(EmployeeValidationErrorCode.Ae90DaysOptOutWithdrawalElectionValid,
                CensusErrorCodes.Ae90DaysWithdrawalElectionValidWarning);

        ErrorMapping.put(EmployeeValidationErrorCode.AeeApolloAvailability,
                CensusErrorCodes.AeeApolloAvailability);

        ErrorMapping.put(EmployeeValidationErrorCode.ActiveParticipantOptOut,
                CensusErrorCodes.ActiveParticipantOptOutError);
        
        ErrorMapping.put(EmployeeValidationErrorCode.Ae90DaysOptOutDesignatedRothDefAmtOnly,
                CensusErrorCodes.Ae90DaysOptOutDesignatedRothDefAmtOnly);

        ErrorMapping.put(EmployeeValidationErrorCode.Ae90DaysOptOutDesignatedRothDefPerOnly,
                CensusErrorCodes.Ae90DaysOptOutDesignatedRothDefPerOnly);

        ErrorMapping.put(EmployeeValidationErrorCode.Ae90DaysOptOutBeforeTaxDefOnly,
                CensusErrorCodes.Ae90DaysOptOutBeforeTaxDefOnly);

        ErrorMapping.put(EmployeeValidationErrorCode.Ae90DaysOptOutBeforeTaxDefPerOnly,
                CensusErrorCodes.Ae90DaysOptOutBeforeTaxDefPerOnly);
        
        ErrorMapping.put(EmployeeValidationErrorCode.EcMissingBirthDate,
                CensusErrorCodes.EcMissingBirthDate);
        
        ErrorMapping.put(EmployeeValidationErrorCode.EcMissingHireDate,
                CensusErrorCodes.EcMissingHireDate);
        
        ErrorMapping.put(EmployeeValidationErrorCode.EligibilityDateReset,
                CensusErrorCodes.EligibilityDateReset);
        
        ErrorMapping.put(EmployeeValidationErrorCode.EmptyPlanYTDHoursWorked,
                CensusErrorCodes.EmptyPlanYTDHoursWorked);



        for (EmployeeValidationErrorCode code : EmployeeValidationErrorCode.values()) {
            if (!ErrorMapping.containsKey(code)) {
                ErrorMapping.put(code, CensusErrorCodes.UnexpectedError);
            }
        }
    }
    
    private static final String SEQUENCE_SEPARATOR = ":";
    
    private static final String EMPLOYEE_DEFERRAL_MONEY_TYPE = "EEDEF";
    
    private static final String EMPLOYEE_BIRTH_DATE = "birthDate";
    
    private static final String EMPLOYEE_HIRE_DATE = "hireDate";
    
    private static final String EMPLOYEE_ELIGIBILITY_INDICATOR = "eligibilityInd";
    
    private Set<String> changedSet = null;

    /**
	 * @return the changedSet
	 */
	public Set<String> getChangedSet() {
		return changedSet;
	}

	/**
	 * @param changedSet the changedSet to set
	 */
	public void setChangedSet(Set<String> changedSet) {
		this.changedSet = changedSet;
	}

	/**
     * A mapping method to return the corresponding form's field name from the specified
     * EmployeeData's field name.
     *
     * @param employeeDataFieldName
     * @return
     */
    abstract String getFormFieldName(EmployeeData.Property employeeDataFieldName);

    /**
     * Returns an array of fields ordered by error display order.
     *
     * @return
     */
    abstract EmployeeData.Property[] getFieldsByErrorDisplayOrder();

    /**
     * Convert an employee validation error into a validation error.
     *
     * @param property
     * @param error
     * @return
     */
    public ValidationError convert(Property property, EmployeeValidationError error) {
        Integer vErrorCode = ErrorMapping.get(error.getErrorCode());
        if (vErrorCode != null) {
            String formFieldName = "";
            if (property != null) {
                formFieldName = getFormFieldName(property);
                if(Property.ELIGIBILITY_DATE.getName().equals(formFieldName)) {
                	formFieldName = convert(formFieldName);
                }
            }
            
            ValidationError vError = new ValidationError(formFieldName, vErrorCode, ErrorTypeMap
                    .get(error.getType()));
            vError.setParams(error.getParams());
            return vError;
        } else {
            throw new IllegalArgumentException("Cannot map employee validation error ["
                    + error.getErrorCode() + "] to validation error.");
        }
    }
    
    /**
     * Convert an employee/money type specific validation error into a validation error.
     *
     * @param field
     * @param errors
     * @param vErrros
     * @return
     */
    public void convert(String field, List<EmployeeValidationError> errors, List<ValidationError> vErrors) {
    	
    	 for (EmployeeValidationError error : errors) {
			Integer vErrorCode = ErrorMapping.get(error.getErrorCode());
			if (vErrorCode != null) {
				String fieldName = getFormFieldName(field);
				if(fieldName != null ){
				ValidationError vError = new ValidationError(fieldName, vErrorCode,
						ErrorTypeMap.get(error.getType()));
				vError.setParams(error.getParams());
				addValidationError(vErrors, vError);
				}
			} else {
				throw new IllegalArgumentException(
						"Cannot map employee validation error ["
								+ error.getErrorCode()
								+ "] to validation error.");
			}

		}    	
        
    }

    /**
     * Convert the given employee validation errors into validation errors.
     *
     * @param errors
     * @return
     */
    public List<ValidationError> convert(EmployeeValidationErrors errors) {
        List<ValidationError> vErrors = new ArrayList<ValidationError>();
        return convert(errors, vErrors, null);
    }

    /**
     * Convert the EmployeeDataValidationError map into a ValidationError list.
     * The errors are ordered based on getFieldsByErrorDisplayOrder().
     *
     * @param errors
     * @return
     */
    public List<ValidationError> convert(EmployeeValidationErrors errors,
            List<ValidationError> vErrors, ValidationErrorFilter filter) {
        for (EmployeeValidationError error : errors.getRecordErrors()) {
            ValidationError vError = convert(null, error);
            addValidationError(vErrors, vError);
        }
        // EC error handling
		HashMap<String, List<EmployeeValidationError>> ecErrors = (HashMap<String, List<EmployeeValidationError>>) errors
				.getECErrors();
		Iterator<String> iter = ecErrors.keySet().iterator();

		while (iter.hasNext()) {
			String field = iter.next();
			convert(field, ecErrors.get(field), vErrors);
		}

		for (EmployeeData.Property field : getFieldsByErrorDisplayOrder()) {
			List<EmployeeValidationError> errorsForField = errors
					.getErrors(field);
			if (errorsForField != null) {
				for (EmployeeValidationError error : errorsForField) {
					if (filter == null || filter.shouldInclude(field, error) || filter(field)) {
						ValidationError vError = convert(field, error);
						addValidationError(vErrors, vError);
					}
				}
			}
		}
        return vErrors;
    }

    /**
     * Convert the EmployeeDataValidationError map into a ValidationError list.
     *
     * @param errors
     * @return
     */
    public EmployeeValidationErrors convert(List<ValidationError> vErrors) {

        EmployeeValidationErrors errors = new EmployeeValidationErrors();

        for (ValidationError vError : vErrors) {

            EmployeeValidationErrorCode errorCode = null;
            ErrorType errorType = null;

            for (Map.Entry<EmployeeValidationErrorCode, Integer> entry : ErrorMapping.entrySet()) {
                if (entry.getValue().equals(vError.getErrorCode())) {
                    errorCode = entry.getKey();
                }
            }

            for (Map.Entry<ErrorType, Type> entry : ErrorTypeMap.entrySet()) {
                if (entry.getValue().equals(vError.getType())) {
                    errorType = entry.getKey();
                }
            }

            EmployeeValidationError error = new EmployeeValidationError(errorCode, errorType,
                    vError.getParams());

            for (Object fieldId : vError.getFieldIds()) {
                for (Property p : Property.values()) {
                    if (p.getName().equals(fieldId)) {
                        errors.add(p, error);
                    }
                }
            }
        }

        return errors;
    }

    private void addValidationError(List<ValidationError> errors, ValidationError error) {
        // if there is params, always add it
        Object[] params = error.getParams();
        if (params == null || params.length == 0) {
            for (ValidationError e : errors) {
                if (e.getErrorCode() == error.getErrorCode()
                        && e.getType().compareTo(error.getType()) == 0) {
                    e.getFieldIds().addAll(error.getFieldIds());
                    return;
                }
            }
        }
        errors.add(error);
    }

    /**
     * Chec if the errors in the list are all warnings.
     *
     * @param errors
     * @return true if the errors are all warning type
     */
    public boolean hasWarningsOnly(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            if (error.getType().compareTo(ValidationError.Type.error) == 0) {
                return false;
            }
        }
        return true;
    }

    public EmployeeValidationError getEmployeeValidationError(EmployeeValidationErrors errors,
            Property property, EmployeeValidationErrorCode errorCode) {

        List<EmployeeValidationError> ssnErrors = errors.getErrors(property);
        if (ssnErrors != null) {
            for (EmployeeValidationError error : ssnErrors) {
                if (error.getErrorCode().compareTo(errorCode) == 0) {
                    return error;
                }
            }
        }
        return null;
    }
    
    /**
	 * Check whether the eligibility date for any specific money type is changed
	 * and returns form field name.
	 * 
	 * @param field
	 * @return String
	 */
	public String getFormFieldName(String field) {
		
		if(EMPLOYEE_BIRTH_DATE.equals(field) || EMPLOYEE_HIRE_DATE.equals(field)
				|| EMPLOYEE_ELIGIBILITY_INDICATOR.equals(field)) {
			return field;
		}
		
		if (changedSet != null) {
			for (String fieldName : changedSet) {
				if (fieldName.startsWith(field)) {
					return fieldName.replaceFirst(field + SEQUENCE_SEPARATOR,
							"");
				}
			}
		}
		return null;
	}

	/**
	 * Check whether the eligibility date for EEDEF money type is changed and
	 * returns form field name.
	 * 
	 * @param field
	 * 
	 */
	public String convert(String formFieldName) {
		
		if (changedSet != null) {
			for (String fieldName : changedSet) {
				if (fieldName.startsWith(EMPLOYEE_DEFERRAL_MONEY_TYPE
						+ SEQUENCE_SEPARATOR)) {
					return fieldName.replaceFirst(EMPLOYEE_DEFERRAL_MONEY_TYPE + SEQUENCE_SEPARATOR,
					"");
				}
			}
		}
		return formFieldName;
	}

	/**
	 * Check whether the eligibility date for EEDEF money type(EC feature is on)
	 * is changed and returns form field name.
	 * 
	 * @param field
	 * 
	 */
	public boolean filter(Property property) {

		if (property != null
				&& (Property.ELIGIBILITY_DATE.getName()
						.equals(getFormFieldName(property)))
				&& changedSet != null) {
			for (String fieldName : changedSet) {
				if (fieldName.startsWith(EMPLOYEE_DEFERRAL_MONEY_TYPE
						+ SEQUENCE_SEPARATOR)) {
					return true;
				}
			}
		}
		return false;
	}
}
