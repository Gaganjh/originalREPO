package com.manulife.pension.ps.web.census.util;

import com.manulife.pension.service.employee.util.EmployeeValidationErrorCode;

public interface CensusErrorCodes {

    // ///////////////////////////////////////////////////////////////////////////////
    // / UI errors
    // ////////////////////////////////////////////////////////////////////////////

    public static final int UnexpectedError = 7001;

    public static final int InvalidAsciiCharacter = 7000;
    
    public static final int InvalidDate = 8074;

    public static final int SSNRequired = 7003;


    public static final int DIContract = 8002;

    public static final int FirstNameRequired = 8011;

    public static final int LastNameRequired = 8012;
    
    public static final int FirstNameHasNumbers = 3150;

    public static final int LastNameHasNumbers = 3151;

    public static final int MissingSSN = 7008;

    public static final int InvalidSSN = 7009;

    public static final int DuplicateSSN = 7010;
    
    public static final int DuplicateSubmittedSSN = 8075;
    
    public static final int DuplicateEmailAddress = 1416; //Constant value needs to be modified.
    
    public static final int DuplicateSubmittedEmailAddress = 1415; //Constant value needs to be modified. 
    
    public static final int InvalidDomainEmailAddress = 1414; //Constant value needs to be modified.
    
    public static final int DuplicateSubmittedEmployeeId = 8076;
    
    public static final int LengthExceedsCSDBLengthError = 8073;

    public static final int SimilarSSN = 7011;

    public static final int InvalidNamePrefix = 7012;

    public static final int InvalidLastname = 7085;
    
    public static final int FullNameNotParsed = 8094;

    public static final int LastNameShort = 7013;
    
    public static final int MissingFirstName = 8011;
    
    public static final int MissingLastName = 8012;

    public static final int MissingEmployeeID = 7014;

    public static final int MissingSSNNewEmployeeEE = 7015;
    
    public static final int InvalidEmployeeIDSortOptionEE = 8082;
    
    public static final int InvalidEmployeeIDSortOptionNotEE = 8082;

    public static final int DuplicateEmployeeIdSortOptionEE = 7016; // 19
    
    public static final int DuplicateEmployeeIdSortOptionNotEE = 7016; // 196
    
    public static final int DuplicateEmployeeIdAccountHolderSortOptionEE = 7016; // 208
    
    public static final int DuplicateEmployeeIdAccountHolderSortOptionNotEE = 1344; // 209
    
    public static final int DuplicateEmployeeIdNonAccountHolderSortOptionEE = 1347; // 206
    
    public static final int DuplicateEmployeeIdNonAccountHolderSortOptionNotEE = 1341; // 207
    
    public static final int MultipleDuplicateEmployeeIdSortOptionEE = 1342; // 210
    
    public static final int MultipleDuplicateEmployeeIdSortOptionNotEE = 1348; // 211

    public static final int MissingBirthDate = 7017;
    
    public static final int FutureBirthDate = 2596;

    public static final int MinimumAge = 7018;

    public static final int MinimumBirthDate = 7019;
    
    public static final int MinimumHireDate = 2597;

    public static final int BirthDateHireDate = 7020;       // same as 7025
    
    public static final int FutureHireDate = 2591;

    public static final int BirthDateEmploymentStatusEffDate = 7038;   // same as 7021

    public static final int BirthDateYTDHoursEffDate = 7047;    // same as 7023

    public static final int RemoveBirthDate = 7024;

    public static final int EmploymentStatusEffDateHireDate = 7039;   // same as 7026
    
    public static final int MinimumEligibilityDate = 2592;
    
    public static final int MinimumEligibilityDateEligibilityPage = 8084;

    public static final int EligibilityDateBirthDate = 7068;

    public static final int EligibilityDateBirthDateEligibilityPage = 8020;

    public static final int EligibilityDateHireDate = 7069;    // same as 7027

    public static final int EligibilityDateHireDateEligibilityPage = 7101;

    public static final int YTDEffDateHireDate = 7049;          // same as 7028

    public static final int InvalidEmploymentStatus = 7029;

    public static final int EmploymentStatusEmpty = 7040;

    public static final int EmploymentStatusEffDateEmpty = 7037;   // same as  7030, 7040

    public static final int MissingEmpStatusEffDateAndActiveEmpStatusWithNonEmptyHireDate = 2707;
    
    public static final int MissingEmpStatusEffDateAndActiveEmpStatus = 2708;

    public static final int MissingEmpStatusEffDateAndActiveEmpStatusWithEmptyHireDate = 2709;
    
    public static final int MinimumEmploymentStatusEffDate = 2593;

    public static final int CancelledParticipant = 7032;

    public static final int CancellingParticipant = 7033;
    
    public static final int CancelledEmployee = 8083;

    public static final int RemoveEmploymentStatus = 7035;

    public static final int MissingYTDHoursEffDateBoth = 7044;   // same as 7043, 7041

    public static final int MissingYTDHoursEffDateHours = 8024;

    public static final int MissingYTDHoursEffDateCompensation = 8025;

    public static final int YTDEffDateDiscrepancy = 7050;    // same as 7042

    public static final int MaxYTDHoursEffDate = 7045;

    public static final int PriorYTDHoursEffDate = 7046;

    public static final int MinYTDHoursEffDate = 7048;

    public static final int MaxYTDHourWorked = 7052;

    public static final int RemoveAddressLine1 = 7053;

    public static final int MissingAddressLine1 = 2272;

    public static final int MissingCity = 2273;

    public static final int MissingCountry = 2392;

    public static final int RemoveCity = 7055;

    public static final int MissingState = 7056;

    public static final int InvalidStateCode = 2274;

    public static final int InvalidCountry = 7058;

    public static final int RemoveCountry = 7059;

    public static final int StateZipCode = 2382;

    public static final int InvalidZipFormat = 1088;
    
    public static final int InvalidUSZipFormat = 2276;

    public static final int InvalidStateOfResidence = 7062;

    public static final int InvalidEmailAddressFormat = 7063;

    public static final int InvalidEligibilityInd = 7099;

    public static final int BlankEligibilityInd = 7064;
    
    public static final int MoreThanTwoDecimalDigits = 8077;
    
    public static final int MoreThanThreeDecimalDigits = 8078;

    public static final int MissingEligibilityDateForNonPpt = 7104;   // same as 7071

    public static final int MissingEligibilityDateForPpt = 7098;   // same as 7071

    public static final int MissingEligibilityInd = 7065;   // same as 7070

    public static final int EligibilityMissingEligibilityInd = 7103;

    public static final int BeforePlanEffectiveDate = 7107;

    public static final int FutureEligibilityDate = 7073;

    public static final int InvalidOptOutInd = 7074;

    public static final int OptOutNonParticipant = 7075;

    public static final int OptOutBeforeTaxDefPer = 7076;   // same as 7082

    public static final int EligibilityOptOutBeforeTaxDefPer = 7108;   // same as 7082

    public static final int DesignatedRothDefPerNotInRange = 2595;

    public static final int DesignatedRothDefAmtNotInRange = 7079;

    public static final int BeforeTaxDefPerNotInRange = 2594;
    
    public static final int BeforeTaxDefAmtNotInRange = 7083;

    public static final int EligibilityBeforeTaxDefPerLessThanZero = 7105;

    public static final int EligibilityBeforeTaxDefPerMoreThan100 = 7106;

    public static final int OptOutDesignatedRothDefAmt = 7086;

    public static final int OptOutDesignatedRothDefPer = 7086;

    public static final int OptOutBeforeTaxDef = 7086;

    public static final int OptOutDesignatedRothDefPctEligibility = 7087;

    public static final int OptOutDesignatedRothDefAmtEligibility = 7031;

    public static final int EligibilityDesignatedRothDefAmt = 7088;

    public static final int EligibilityDesignatedRothDefPer = 7088;

    public static final int EligibilityBeforeTaxDef = 7088;

    public static final int EligibilityBeforeTaxDefPer = 7088;

    public static final int EligibilityPageBeforeTaxDef = 7109;

    public static final int EligibilityPageBeforeTaxFlat = 8003;

    public static final int EligibilityPageBeforeTaxDefPer = 7109;

    public static final int EligibilityAfterTaxDeferralPct = 7091;

    public static final int EligibilityAfterTaxDeferralAmt = 7034;

    public static final int EligibilityBeforeTaxDeferralAmt = 7005;

    public static final int BirthDateFormatError = 7089;

    public static final int HireDateFormatError = 7093;

    public static final int BeforeTaxDefPerFlatDollar = 8004;

    public static final int DesignatedRothDefAmt = 8005;

    public static final int FirstNameTooShort = 8006;

    public static final int CityTooShort = 8007;

    public static final int AdressLine2TooShort = 8008;

    public static final int AdressLine1TooShort = 8009;

    public static final int FutureEmploymentStatusEffDate = 8010;

    public static final int InvalidPreviousYearsOfService = 8013;

    public static final int InvalidYTDHoursWorked = 8023;

    public static final int InvalidEligiblePlanYTDCompensation = 8033;

    public static final int InvalidAnnualBaseSalary = 8014;

    public static final int BeforeTaxDefAndAmtDiscrepancy = 8019;

    public static final int EmploymentStatusEffDateFormatError = 7092;

    public static final int AnnualBaseSalaryFormatError = 7002;

    public static final int EligibleYTDCompFormatError = 7002;

    public static final int YTDHoursWorkedFormatError = 7002;

    public static final int YTDHoursEffDateFormatError = 7084;

    public static final int EligibilityDateFormatError = 7090;

    public static final int AfterTaxDefPerFormatError = 7002;

    public static final int BeforeTaxDefPerFormatError = 7002;

    public static final int AfterTaxDefDollarFormatError = 7002;

    public static final int BeforeTaxDefDollarFormatError = 7002;

    public static final int EmployeeIdSSNMismatch = 1083;

    public static final int DeferralPlusIncreaseGreaterThanLimit = 2554;
    public static final int DeferralPlusIncreaseAndRothGreaterThanPlanLimit = 2555;
    public static final int IncreaseAndLimitNotGreaterThanZero = 2556;
    public static final int PersonalLimitGreaterThanPlanLimit = 2557;
    public static final int PersonalLimitGreaterThan25pct = 2558;
    public static final int PersonalLimitIsLessThanCurrentContribPlusIncrease = 2559;
    public static final int IncreaseAndLimitNotIntegers = 2562;
    public static final int AnniversaryDateBeforeCurrentDate = 1335; // 2563;
    public static final int AnniversaryDateOptOutDateConflict = 2564;
    public static final int NewAnniversaryDateOptOutDateConflict = 2587;
    public static final int AnniversaryDateTooFarAway = 8092; 
    public static final int AnniversaryDateOnLeapYearDay = 8091;
    public static final int AnniversaryDateFuckedFormat = 8021;


    public static final int MissingPreviousYearsOfService = 8065;

    public static final int MissingPYOSAndHOS = 8062;

    public static final int InvalidPreviousYearsOfServiceEffectiveDate = 8068;

    public static final int MissingPreviousYearsOfServiceEffectiveDate = 8066;

    public static final int MinPreviousYearsOfServiceEffectiveDate = 8061;
    
    public static final int PreviousYearsOfServiceEffectiveDateBirthDate = 2589;

    public static final int FuturePreviousYearsOfServiceEffectiveDate = 8067;

    public static final int PreviousYearsOfServiceBeBlank = 7110;

    public static final int MissingFullyVestedInd = 8069;

    public static final int MissingFullyVestedEffectiveDate = 8070;
    
    public static final int MinimumFullyVestedEffectiveDate = 2599;
    
    public static final int FullyVestedEffectiveDateBirthDate = 2598;

    public static final int FutureFullyVestedEffectiveDate = 8071;

    public static final int FullyVestedEffectiveDateFormat = 8072;

    public static final int MissingHireDate = 8063;

    public static final int MissingEmploymentStatus = 8064;

    public static final int UnspecifiedCalculationMethod = 9054;

    public static final int MissingVestingSchedule = 2550;

    public static final int RemovePlanYTDHoursWorked = 2565;
    
    // AEE error codes
    public static final int Ae90DaysOptOutBeforeTaxDefPer = 1090;   
    
    public static final int Ae90DaysOptOutDesignatedRothDefAmt = 1093;

    public static final int Ae90DaysOptOutDesignatedRothDefPer = 1091;

    public static final int Ae90DaysOptOutBeforeTaxDef = 1092;
    
    public static final int Ae90DaysOptOutEEDEFMoney = 1095;
    
    public static final int Ae90DaysWithdrawalElectionValidWarning = 1097;
    
    public static final int Ae90DaysStopNEWithdrawalWarning = 1098;
    
    public static final int AeeApolloAvailability = 2365;
    
    public static final int ActiveParticipantOptOutError = 1094;
    
 public static final int Ae90DaysOptOutBeforeTaxDefPerOnly = 2703;   
    
    public static final int Ae90DaysOptOutDesignatedRothDefAmtOnly = 2706;

    public static final int Ae90DaysOptOutDesignatedRothDefPerOnly = 2704;

    public static final int Ae90DaysOptOutBeforeTaxDefOnly = 2705;
    
    public static final int EcMissingBirthDate = 8101;
    
    public static final int EcMissingHireDate = 8102;

    public static final int HireDateCannotBeDeleted=1132;
    
    public static final int EligibilityDateReset=1133;
    
    public static final int EmptyPlanYTDHoursWorked = 2954;
    
    public static final int LongTermPartTimeAssessmentYearOverrideError = 1145;
}
